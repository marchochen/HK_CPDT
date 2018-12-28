package com.cwn.wizbank.controller;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.SnsGroup;
import com.cwn.wizbank.entity.SnsGroupMember;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SnsGroupMemberService;
import com.cwn.wizbank.services.SnsGroupService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

/**
 * 
 * @author lance 群组
 */
@Controller
@RequestMapping("group")
@HasPermission(value = {AclFunction.FTN_LRN_GROUP_MGT, AclFunction.FTN_AMD_SNS_GROUP_MAIN,AclFunction.FTN_AMD_SNS_GROUP_VIEW})
public class GroupController {
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	SnsGroupService snsGroupService;
	
	@Autowired
	SnsGroupMemberService snsGroupMemberService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	/**
	 * 群组页面
	 * @param command
	 * 				: groupList 我的群组
	 * 				: groupFind 发现群组
	 *              : groupOpen 开发群组
	 * 				: groupCreate 新建群组
	 * 				: groupDetail 群组详情
	 * @return
	 */
	@RequestMapping(value = "{command}/{enc_s_grp_id}", method = RequestMethod.GET)
	public ModelAndView toPage(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini,
			@PathVariable(value = "command") String command, @PathVariable(value = "enc_s_grp_id") String enc_s_grp_id) throws AuthorityException {
		
		long s_grp_id;
		if("0".equals(enc_s_grp_id)){
			s_grp_id = 0;
		}else{
			s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		}
		mav = new ModelAndView("group/" + command);
		mav.addObject("command", command);
		mav.addObject("usrEntId", prof.usr_ent_id);
		mav.addObject("s_grp_id", s_grp_id);
		boolean isNormal = true;
		isNormal = snsGroupService.checkNormal(prof);
		//获取用户信息
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		if(command.equalsIgnoreCase("groupDetail")){
			mav.addObject("isGroupMember", snsGroupMemberService.checkGroupMember(prof.usr_ent_id, s_grp_id, 1));
			//获取群组信息
			SnsGroup snsGroup = snsGroupService.getSnsGroupDetail(s_grp_id, 0, prof.usr_ent_id);
			if(snsGroup.getS_grp_status().equalsIgnoreCase("DELETED")){
				throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_group_dissolved"));
			}
			mav.addObject("s_grp_private", snsGroup.getS_grp_private());
			mav.addObject("snsGroup", ImageUtil.combineImagePath(snsGroup));
			mav.addObject("isManager", snsGroup.getS_grp_uid() == prof.usr_ent_id);
			mav.addObject("desc_length", snsGroup.getS_grp_desc() == null? 0 : snsGroup.getS_grp_desc().getBytes().length);
 
		}
		 
			mav.addObject("isNormal",isNormal);
		 

		return mav;
	}
	
	/**
	 * 获取群组列表
	 * @param type 类型
	 * @param usr_ent_id 用户id 
	 * @param searchContent 搜索内容
	 * @return
	 */
	@RequestMapping(value = "getSnsGroupList/{type}/{usr_ent_id}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getSnsGroupList(Model model, Page page, loginProfile prof, @PathVariable(value = "type") String type,
			@PathVariable(value = "usr_ent_id") long usr_ent_id, @RequestParam(value="searchContent", defaultValue="", required = false) String searchContent) {
		
		boolean isManager = snsGroupService.checkNormal(prof);
		
		if(searchContent != null && !"".equals(searchContent)){
			searchContent = "%" + searchContent.toLowerCase() + "%";
		}
		if(!isManager){
			page = snsGroupService.getAllSnsGroupList(page,prof.my_top_tc_id, searchContent, prof.usr_ent_id, prof.current_role);
		}else
		{
			page = snsGroupService.getSnsGroupList(page, usr_ent_id, prof.my_top_tc_id, prof.usr_ent_id == usr_ent_id, type, searchContent, "", prof);
		}
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 申请加入群组
	 * @param enc_s_grp_id 群组id加密
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "applyJoinGroup/{enc_s_grp_id}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String applyJoinGroup(Model model, loginProfile prof, Page page,
			@PathVariable(value = "enc_s_grp_id") String enc_s_grp_id ) throws Exception {
		
		long s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		
		List<SnsGroupMember> member_list = snsGroupMemberService.getSnsGroupMemberList(page, s_grp_id, prof.usr_ent_id, "" , "").getResults();
		if(member_list == null || member_list.size() < 1){
			snsGroupMemberService.addSnsGroupMember(s_grp_id, prof.usr_ent_id, 0, 2);
			model.addAttribute("message", "group_apply_ok");
		} else if(member_list != null && member_list.size() > 0 && member_list.get(0).getS_gpm_status() == 3){
			List<Long> s_gpm_id_list = new ArrayList<Long>();
			s_gpm_id_list.add(member_list.get(0).getS_gpm_id());
			snsGroupMemberService.updateGpmStatus(s_gpm_id_list, new ArrayList<Long>(), prof.usr_ent_id, 0, prof.usr_id, "apply", s_grp_id);
			model.addAttribute("message", "group_apply_ok");
		} else {
			model.addAttribute("message", "group_apply_error");
		}
		return JsonFormat.format(page.getParams(), model);
	}
	
	/**
	 * 新建群组
	 * @param snsGroup 
	 * @param image 群组名片
	 * @return
	 */
	@RequestMapping(value = "groupCreate/add/snsGroup")
/*	@ResponseBody*/
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String addGroup(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini, Page page, @Param(value = "snsGroup") SnsGroup snsGroup,
			@RequestParam(value="image", required = false) MultipartFile image,RedirectAttributes attr) throws Exception{
		 boolean isNormal = true;
		 isNormal = snsGroupService.checkNormal(prof);
		//判断群组名称是否已存在
		    attr.addAttribute("command", "groupList");
		    attr.addAttribute("usrEntId", prof.usr_ent_id);
		    //attr.addAttribute("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		    attr.addAttribute("isNormal", isNormal);
			/*mav = new ModelAndView("group/groupList");
			mav.addObject("command", "groupList");
			mav.addObject("usrEntId", prof.usr_ent_id);
			mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
			mav.addObject("isNormal", isNormal);*/
			snsGroupService.addSnsGroup(wizbini, snsGroup, prof.usr_ent_id, prof.my_top_tc_id, image);
			forCallOldAPIService.updUserCredits(null, Credit.CREATE_GROUP, prof.usr_ent_id, prof.usr_id, 0, 0);
		return "redirect:/app/group/groupList/0";
	}
	
	@RequestMapping("groupCreate/checkGroupName")
	@ResponseBody
	public int checkGroupName(@Param(value = "snsGroup") SnsGroup snsGroup) throws Exception{
		return snsGroupService.checkGroupName(snsGroup);
	}
	
	/**
	 * 退出群组（如果是群组管理员就解散群组）
	 * @param enc_s_grp_id 群组id 加密
	 * @return
	 */
	@RequestMapping(value = "signOutGroup/{enc_s_grp_id}")
	@ResponseBody
	public void signOutGroup(loginProfile prof, @PathVariable(value = "enc_s_grp_id") String enc_s_grp_id) {
		
		long s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		//判断当前用户是否是群组管理员
		SnsGroup snsGroup = snsGroupService.getSnsGroupDetail(s_grp_id, 0, prof.usr_ent_id);
		if(snsGroupService.checkManger(snsGroup.getS_grp_uid(), prof)){
			try {
				snsGroupService.dissolveSnsGroup(prof.usr_ent_id, s_grp_id, prof.cur_lan);
			} catch (MessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			snsGroupMemberService.deleteGroupMember(prof.usr_ent_id, s_grp_id);
		}
	}
	
	/**
	 * 获取群组成员列表
	 * @param type
	 * @param enc_s_grp_id 群组id 加密
	 * @param searchContent 搜索内容
	 * @return
	 */
	@RequestMapping("getSnsGroupMemberList/{type}/{enc_s_grp_id}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getSnsGroupMemberList(Model model, Page page, loginProfile prof, @PathVariable(value = "type") String type,
			@PathVariable(value = "enc_s_grp_id") String enc_s_grp_id, @RequestParam(value="searchContent", defaultValue="", required=false) String searchContent) {
		snsGroupMemberService.getSnsGroupMemberList(page, EncryptUtil.cwnDecrypt(enc_s_grp_id), 0, type, searchContent);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 更新审批状态
	 * @param enc_s_grp_id 群组id 加密
	 * @param s_gpm_status 审批状态
	 * @param s_gpm_id_list 审批id
	 * @return
	 */
	@RequestMapping(value = "updateGpmStatus/{enc_s_grp_id}/{s_gpm_status}/{s_gpm_id_list}/{usr_ent_id_list}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String updateGpmStatus(Model model, Page page, loginProfile prof, @PathVariable(value = "s_gpm_status") long s_gpm_status, 
			@PathVariable(value = "s_gpm_id_list") List<Long> s_gpm_id_list, @PathVariable(value = "enc_s_grp_id") String enc_s_grp_id,
			@PathVariable(value = "usr_ent_id_list") List<Long> usr_ent_id_list) throws Exception{
		long s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		snsGroupMemberService.updateGpmStatus(s_gpm_id_list, usr_ent_id_list, prof.usr_ent_id, s_gpm_status, prof.usr_id, "upd", s_grp_id);
		snsGroupMemberService.getSnsGroupMemberList(page, s_grp_id, 0, "pending", "");
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 更新群组信息
	 * @param snsGroup
	 * @param image
	 */
	@RequestMapping(value = "groupDetail/update/snsGroup")
	@ResponseBody
	public ModelAndView updateSnsGroup(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini, @Param(value = "snsGroup") SnsGroup snsGroup,
			@RequestParam(value="image", required = false) MultipartFile image) throws Exception{
		boolean isNormal = true;
		isNormal = snsGroupService.checkNormal(prof);
		mav = new ModelAndView("group/groupDetail");
		mav.addObject("usrEntId", prof.usr_ent_id);
		mav.addObject("s_grp_id", snsGroup.getS_grp_id());
		mav.addObject("isNormal",isNormal);
		mav.addObject("tab", "group_set");
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		SnsGroup group = snsGroupService.getSnsGroupDetail(snsGroup.getS_grp_id(), prof.usr_ent_id, prof.usr_ent_id);
		if(group != null || !isNormal){
			snsGroupService.updateSnsGroup(wizbini, group, snsGroup, prof.usr_ent_id, image);
			mav.addObject("result_msg", "update_ok");
		} else {
			mav.addObject("snsGroup", snsGroupService.getSnsGroupDetail(snsGroup.getS_grp_id(), 0, prof.usr_ent_id));
			mav.addObject("result_msg", "power_error");
		}
		
		mav.addObject("isGroupMember", snsGroupMemberService.checkGroupMember(prof.usr_ent_id, snsGroup.getS_grp_id(), 1));
		//获取群组信息
		SnsGroup new_snsGroup = snsGroupService.getSnsGroupDetail(snsGroup.getS_grp_id(), 0, prof.usr_ent_id);
		mav.addObject("snsGroup", ImageUtil.combineImagePath(new_snsGroup));
		mav.addObject("isManager", new_snsGroup.getS_grp_uid() == prof.usr_ent_id);
		mav.addObject("desc_length", snsGroup.getS_grp_desc().getBytes().length);
		return mav;
	} 
	
	/**
	 * 群组成员列表
	 * @param enc_s_grp_id 群组id 加密
	 * @param searchContent 搜索内容
	 */
	@RequestMapping(value = "findGroupMemberList/{enc_s_grp_id}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String findGroupMemberList(Model model, Page page,WizbiniLoader wizbini, loginProfile prof, @PathVariable(value = "enc_s_grp_id") String enc_s_grp_id,
			@RequestParam(value="searchContent", defaultValue="", required = false) String searchContent,
			@RequestParam(value="instrOnly", defaultValue="false", required = false) boolean instrOnly
			){
		long s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			long tcp_id = prof.my_top_tc_id;//改成获取当前培训中心
			page.getParams().put("cfgTcEnabled", true);
			page.getParams().put("tcp_id", tcp_id);
		}
		snsGroupMemberService.getNotJoinGroupMemberList(page, prof.usr_ent_id, prof.my_top_tc_id, s_grp_id, 1, searchContent, instrOnly);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 添加群组成员
	 * @param enc_s_grp_id 群组id 加密
	 * @param usr_ent_id 添加成员id
	 */
	@RequestMapping(value = "addGroupMember/{enc_s_grp_id}/{usr_ent_id}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String addGroupMember(Model model, Page page, loginProfile prof, 
			@PathVariable(value = "enc_s_grp_id") String enc_s_grp_id, 
			@PathVariable(value = "usr_ent_id") long usr_ent_id) throws Exception {
		
		long s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		
		List<SnsGroupMember> list = snsGroupMemberService.getSnsGroupMemberList(page, s_grp_id, usr_ent_id, "", "").getResults();
		if(list == null || list.size() == 0){
			snsGroupMemberService.addSnsGroupMember(s_grp_id, usr_ent_id, 1, 2);
			forCallOldAPIService.updUserCredits(null, Credit.SYS_JION_GROUP, usr_ent_id, prof.usr_id, 0, 0);
		} else if(list.get(0).getS_gpm_status() == 3){
			List<Long> s_gpm_id_list = new ArrayList<Long>();
			s_gpm_id_list.add(list.get(0).getS_gpm_id());
			
			List<Long> usr_ent_id_list = new ArrayList<Long>();
			usr_ent_id_list.add(usr_ent_id);
			snsGroupMemberService.updateGpmStatus(s_gpm_id_list, usr_ent_id_list, prof.usr_ent_id, 1, prof.usr_id, "add", s_grp_id);
		} else if(list.get(0).getS_gpm_status() == 0){
			List<Long> s_gpm_id_list = new ArrayList<Long>();
			s_gpm_id_list.add(list.get(0).getS_gpm_id());
			
			List<Long> usr_ent_id_list = new ArrayList<Long>();
			usr_ent_id_list.add(usr_ent_id);
			snsGroupMemberService.updateGpmStatus(s_gpm_id_list, usr_ent_id_list, prof.usr_ent_id, 1, prof.usr_id, "upd", s_grp_id);
		} else {
		//	model.addAttribute("error", "add_member_error");
 
			
			model.addAttribute("error","139");//139表示失败 通过前台拼接前缀来查找对应的标签  意思是添加失败
		}
		return JsonFormat.format(page.getParams(), model);
	}
	
	/**
	 * 删除群组成员
	 * @param enc_s_grp_id 群组id 加密
	 * @param usr_ent_id 删除成员id
	 */
	@RequestMapping(value = "deleteGroupMember/{enc_s_grp_id}/{usr_ent_id}")
	@ResponseBody
	public String deleteGroupMember(Model model, loginProfile prof, 
			Params param,
			@PathVariable(value = "enc_s_grp_id") String enc_s_grp_id, @PathVariable(value = "usr_ent_id") long usr_ent_id){
		
		long s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		
		if(snsGroupMemberService.checkGroupMember(usr_ent_id, s_grp_id, 1) ){
			snsGroupMemberService.deleteGroupMember(usr_ent_id, s_grp_id);
		}
		return JsonFormat.format(param, model);
	}
	
	/**
	 * 转让群组管理员
	 * @param enc_s_grp_id 群组id 加密
	 * @param usr_ent_id 转让给成员id
	 */
	@RequestMapping(value = "changeManager/{enc_s_grp_id}/{usr_ent_id}")
	@ResponseBody
	public String changeManager(Model model, loginProfile prof,
			Params param,
			@PathVariable(value = "enc_s_grp_id") String enc_s_grp_id, @PathVariable(value = "usr_ent_id") long usr_ent_id){
		long s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		
		if(snsGroupMemberService.checkGroupMember(usr_ent_id, s_grp_id, 1)){
			SnsGroup snsGroup = snsGroupService.get(s_grp_id);
			snsGroupService.updateGrpUid(usr_ent_id, prof.usr_ent_id, s_grp_id);
			snsGroupMemberService.changeManager(usr_ent_id, s_grp_id, 1);
		//	snsGroupMemberService.changeManager(prof.usr_ent_id, s_grp_id, 2);
			
			snsGroupMemberService.changeManager(snsGroup.getS_grp_uid(), s_grp_id, 2);
			
		} else {
			model.addAttribute("error", "group_assignment_error");
		}
		return JsonFormat.format(param, model);
	}
	
	/**
	 * 切换语言报错问题处理
	 * @param command 
	 * @return
	 */
	@RequestMapping(value = "{command}/{operation}/snsGroup", method = RequestMethod.GET)
	public String message(){
		return "redirect:/app/group/groupList/0";
	}
	/*
	 * 手机端群组首页使用
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("main/json")
	@ResponseBody
	public String mainJson(Model model, loginProfile prof, WizbiniLoader wizbini, Page page){
		model.addAttribute("myGroupTotal", snsGroupService.getSnsGroupList(page, prof.usr_ent_id, prof.my_top_tc_id, true, "my", "", "").getTotalRecord());
		model.addAttribute("openGroupTotal", snsGroupService.getSnsGroupList(page, prof.usr_ent_id, prof.my_top_tc_id, true, "open", "", "").getTotalRecord());
		model.addAttribute("findGroupTotal", snsGroupService.getSnsGroupList(page, prof.usr_ent_id, prof.my_top_tc_id, true, "find", "", "").getTotalRecord());
		return JsonFormat.format(model, page);
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping("detail/json/{encGroupId}")
	@ResponseBody
	public String detailJson(Model model, loginProfile prof, WizbiniLoader wizbini, Page page,  @PathVariable(value = "encGroupId") String encGroupId) throws AuthorityException{
		
		long groupId = EncryptUtil.cwnDecrypt(encGroupId);
		
		boolean isNormal = true;
		isNormal = snsGroupService.checkNormal(prof);
		model.addAttribute("isGroupMember", snsGroupMemberService.checkGroupMember(prof.usr_ent_id, groupId, 1));
		//获取群组信息
		SnsGroup snsGroup = snsGroupService.getSnsGroupDetail(groupId, 0, prof.usr_ent_id);
		if(snsGroup.getS_grp_status().equalsIgnoreCase("DELETED")){
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_group_dissolved"));
		}
		model.addAttribute("s_grp_private", snsGroup.getS_grp_private());
		model.addAttribute("snsGroup", ImageUtil.combineImagePath(snsGroup));
		model.addAttribute("isManager", snsGroup.getS_grp_uid() == prof.usr_ent_id);
		model.addAttribute("desc_length", snsGroup.getS_grp_desc() == null? 0 : snsGroup.getS_grp_desc().getBytes().length);
		model.addAttribute("isNormal",isNormal);
		return JsonFormat.format(model, page);
	}
	/**
	 * 更新群组信息(手机端使用)
	 * @param model
	 * @param prof
	 * @param wizbini
	 * @param page
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("detail/mobile/update")
	@ResponseBody
	public String updateGroup(Model model, loginProfile prof, WizbiniLoader wizbini, Page page) throws Exception{
		boolean isNormal = true;
		isNormal = snsGroupService.checkNormal(prof);
		String jsonStr = (String) page.getParams().get("json");
		JSONObject jsons = JSONObject.fromObject(jsonStr); 
		SnsGroup snsGroup=(SnsGroup) JSONObject.toBean(jsons, SnsGroup.class);
		SnsGroup group = snsGroupService.getSnsGroupDetail(snsGroup.getS_grp_id(), prof.usr_ent_id, prof.usr_ent_id);
		if(group != null || !isNormal){
			snsGroupService.updateSnsGroup(wizbini, group, snsGroup, prof.usr_ent_id, null);
			model.addAttribute("status", "success");
		}
		return JsonFormat.format(model, page);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("addGroupMembers/{enc_s_grp_id}")
	@ResponseBody
	public String addGroupMembers(Model model, loginProfile prof, WizbiniLoader wizbini, Page page, @PathVariable(value = "enc_s_grp_id") String enc_s_grp_id) throws Exception{
		
		long s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		
		String[]usrEntIds = page.getParams().get("usrEntIds").toString().split(",");
		for(String usr_ent_id : usrEntIds ){
			List<SnsGroupMember> list = snsGroupMemberService.getSnsGroupMemberList(page, s_grp_id, Long.parseLong(usr_ent_id), "", "").getResults();
			if(list == null || list.size() == 0){
				snsGroupMemberService.addSnsGroupMember(s_grp_id, Long.parseLong(usr_ent_id), 1, 2);
				forCallOldAPIService.updUserCredits(null, Credit.SYS_JION_GROUP, Long.parseLong(usr_ent_id), prof.usr_id, 0, 0);
			} else if(list.get(0).getS_gpm_status() == 3){
				List<Long> s_gpm_id_list = new ArrayList<Long>();
				s_gpm_id_list.add(list.get(0).getS_gpm_id());				
				List<Long> usr_ent_id_list = new ArrayList<Long>();
				usr_ent_id_list.add(Long.parseLong(usr_ent_id));
				snsGroupMemberService.updateGpmStatus(s_gpm_id_list, usr_ent_id_list, prof.usr_ent_id, 1, prof.usr_id, "add", s_grp_id);
			} else if(list.get(0).getS_gpm_status() == 0){
				List<Long> s_gpm_id_list = new ArrayList<Long>();
				s_gpm_id_list.add(list.get(0).getS_gpm_id());
				
				List<Long> usr_ent_id_list = new ArrayList<Long>();
				usr_ent_id_list.add(Long.parseLong(usr_ent_id));
				snsGroupMemberService.updateGpmStatus(s_gpm_id_list, usr_ent_id_list, prof.usr_ent_id, 1, prof.usr_id, "upd", s_grp_id);
			}					
		}
		model.addAttribute("status", "success");
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 删除群组成员们
	 * @param enc_s_grp_id 群组id 加密
	 * @param usr_ent_id 删除成员id
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "deleteGroupMembers/{enc_s_grp_id}")
	@ResponseBody
	public String deleteGroupMembers(Model model, loginProfile prof, Page page,@PathVariable(value = "enc_s_grp_id") String enc_s_grp_id){
		long s_grp_id = EncryptUtil.cwnDecrypt(enc_s_grp_id);
		String[] usrEntIds = page.getParams().get("usrEntIds").toString().split(",");
		for(String usr_ent_id : usrEntIds ){
			snsGroupMemberService.deleteGroupMember(Long.parseLong(usr_ent_id), s_grp_id);
		}
		model.addAttribute("status", "success");
		return JsonFormat.format(model, page);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("mobile/create")
	@ResponseBody
	public String createGroup(Model model, loginProfile prof, WizbiniLoader wizbini, Page page) throws Exception{
		 SnsGroup snsGroup = new SnsGroup();
		 snsGroup.setS_grp_title(page.getParams().get("s_grp_title").toString());
		 snsGroup.setS_grp_desc(page.getParams().get("s_grp_desc").toString());
		 snsGroup.setS_grp_private(Long.parseLong(page.getParams().get("s_grp_private").toString()));
		//判断群组名称是否已存在
		if(snsGroupService.getSnsGroupList(page, prof.usr_ent_id, 0, false, "", "", snsGroup.getS_grp_title()).getTotalRecord() > 0){
			model.addAttribute("status", "error");
		} else {
			snsGroupService.addSnsGroup(wizbini, snsGroup, prof.usr_ent_id, prof.my_top_tc_id, null);
			forCallOldAPIService.updUserCredits(null, Credit.CREATE_GROUP, prof.usr_ent_id, prof.usr_id, 0, 0);
			model.addAttribute("status", "success");
		}
		return JsonFormat.format(model, page);
	}
}
