package com.cwn.wizbank.controller.admin;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SnsGroup;
import com.cwn.wizbank.entity.SnsGroupMember;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.AcRoleService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SnsGroupMemberService;
import com.cwn.wizbank.services.SnsGroupService;
import com.cwn.wizbank.services.UserGroupService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.utils.RequestStatus;
import com.cwn.wizbank.web.validation.SnsGroupValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;

/**
 * 
 * @author lance 群组
 */
@Controller("adminGroupController")
@RequestMapping("admin/group")
@HasPermission(value = {AclFunction.FTN_AMD_SNS_GROUP_MAIN,AclFunction.FTN_AMD_SNS_GROUP_VIEW})
public class GroupController {
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	SnsGroupService snsGroupService;
	
	@Autowired
	SnsGroupMemberService snsGroupMemberService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@Autowired
	AcRoleService acRoleService;
	
	@Autowired
	UserGroupService uerGroupService;

	@RequestMapping("")
	public ModelAndView list(loginProfile prof,ModelAndView model,@RequestParam(value="isView", required=false, defaultValue="false") boolean isView) {
		model.addObject("userEntId", prof.usr_ent_id);
		model.addObject("isView", isView);
		model.setViewName("admin/group/list");
		return model;
	}
	
	
	/**
	 * 获取群组列表 +
	 * @param type 类型
	 * @param searchText 搜索内容
	 * @return
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public String getSnsGroupList(Model model, Page<SnsGroup> page, loginProfile prof, WizbiniLoader wizbini,
			@RequestParam(value="searchText", required=false, defaultValue="") String searchText) {
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		page = snsGroupService.getAllSnsGroupList(page, tcrId, searchText, prof.usr_ent_id, prof.current_role);
		

 
		return JsonFormat.format(model, page);
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
		
		page.getParams().put("current_role", prof.current_role);
		page = snsGroupService.getSnsGroupList(page, usr_ent_id, prof.my_top_tc_id, prof.usr_ent_id == usr_ent_id, type, searchContent, "", prof);
		
		return JsonFormat.format(model, page);
	}
	
	
	
	/**
	 * 群组页面 + 
	 * @param command
	 * 				: groupList 我的群组
	 * 				: groupFind 发现群组
	 *              : groupOpen 开发群组
	 * 				: groupCreate 新建群组
	 * 				: groupDetail 群组详情
	 * @return
	 * @throws AuthorityException 
	 */
	@RequestMapping(value = "{command}/{grpId}", method = RequestMethod.GET)
	public ModelAndView toPage(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini,
			@PathVariable(value = "command") String command, @PathVariable(value = "grpId") String encrypt_s_grp_id) throws AuthorityException {
		mav = new ModelAndView("admin/group/" + command);
		mav.addObject("command", command);
		mav.addObject("usrEntId", prof.usr_ent_id);
		Long s_grp_id = null;
		if(!StringUtils.isEmpty(encrypt_s_grp_id)){
			s_grp_id = EncryptUtil.cwnDecrypt(encrypt_s_grp_id);
		}
		mav.addObject("s_grp_id", s_grp_id);
		
		//获取用户信息
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		mav.addObject("is_admin", prof.current_role.equals(AcRole.ROLE_ADM_1) || prof.current_role.equals(AcRole.ROLE_TADM_1));
		if(command.equalsIgnoreCase("detail")){
			mav.addObject("isGroupMember", snsGroupMemberService.checkGroupMember(prof.usr_ent_id, s_grp_id, 1));
			//获取群组信息
			SnsGroup snsGroup = snsGroupService.getSnsGroupDetail(s_grp_id, 0, prof.usr_ent_id);
			if(snsGroup == null || snsGroup.getS_grp_status().equalsIgnoreCase("DELETED")){
				throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_group_dissolved"));
			}
			mav.addObject("s_grp_private", snsGroup.getS_grp_private());
			mav.addObject("snsGroup", ImageUtil.combineImagePath(snsGroup));
			mav.addObject("isManager", snsGroupService.hasAdminPrivilege(prof, snsGroup.getS_grp_uid()));
			mav.addObject("desc_length", snsGroup.getS_grp_desc() == null? 0 : snsGroup.getS_grp_desc().getBytes().length);
			mav.addObject("isNormal", !snsGroupService.hasAdminPrivilege(prof, snsGroup.getS_grp_uid()));
		}
		else{
			mav.addObject("isNormal", !snsGroupService.hasAdminPrivilege(prof, 0));
		}
		return mav;
	}
	
	/**
	 * 更新群组信息 + 
	 * @param snsGroup
	 * @param image
	 */
	@RequestMapping(value = "detail/update")
	public String updateSnsGroup(Model model, @ModelAttribute(value = "snsGroup") SnsGroup snsGroup,
			BindingResult result, loginProfile prof, WizbiniLoader wizbini, 
			@RequestParam(value="image", required = false) MultipartFile image, RedirectAttributes attr) throws Exception{
		SnsGroup group = snsGroupService.get(snsGroup.getS_grp_id());

		if(group == null) {
			throw new DataNotFoundException();
		}
		boolean isAdmin = snsGroupService.hasAdminPrivilege(prof, group.getS_grp_uid());		
		WzbValidationUtils.invokeValidator(new SnsGroupValidator(prof), snsGroup, result);
		attr.addAttribute("tab", 4);
		if (result.hasErrors()) {
			attr.addAttribute(RequestStatus.STATUS, result.getAllErrors().get(0).getDefaultMessage());
			return "redirect:/app/admin/group/detail/" + snsGroup.getS_grp_id();
		} else {
			SnsGroup checkObj = snsGroupService.getByName(snsGroup.getS_grp_title());
			if (checkObj != null && checkObj.getS_grp_id().intValue() != snsGroup.getS_grp_id().intValue()) {
				model.addAttribute("isNormal", snsGroupService.hasAdminPrivilege(prof, 0));
				attr.addAttribute(RequestStatus.ERROR, LabelContent.get(prof.cur_lan, "group_add_error"));
				return "redirect:/app/admin/group/detail/" + snsGroup.getS_grp_id();
			}
			if(isAdmin){
				snsGroupService.updateSnsGroup(wizbini, group, snsGroup, prof.usr_ent_id, image);
				model.addAttribute(RequestStatus.STATUS,  RequestStatus.SUCCESS);
				attr.addAttribute(RequestStatus.STATUS,  RequestStatus.SUCCESS);
			}
		}
		return "redirect:/app/admin/group/detail/" + snsGroup.getS_grp_id();
	}
	
	@RequestMapping(value = "toAdd", method = RequestMethod.GET)
	public ModelAndView toAdd(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini,
			 @ModelAttribute(value = "snsGroup") SnsGroup snsGroup) {
		if(snsGroup == null) {
			snsGroup = new SnsGroup();
		}
		mav = new ModelAndView("admin/group/add", "snsGroup", snsGroup);
		mav.addObject("usrEntId", prof.usr_ent_id);		
		//获取用户信息
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		mav.addObject("isNormal", snsGroupService.hasAdminPrivilege(prof, 0));
		mav.addObject("hasInstrRole", snsGroupService.hasInstrRole(prof.current_role));
		return mav;
	}
	/**
	 * 新建群组
	 * @param snsGroup 
	 * @param image 群组名片
	 * @return
	 */
	@RequestMapping(value = "add")
	public String addGroup(loginProfile prof, WizbiniLoader wizbini, Model model, 
			@ModelAttribute(value = "snsGroup") SnsGroup snsGroup, BindingResult result,
			@RequestParam(value="image", required = false) MultipartFile image) throws Exception{
		 
		WzbValidationUtils.invokeValidator(new SnsGroupValidator(prof), snsGroup, result);
		if (result.hasErrors()) {
			return "admin/group/add";
		} else {
			if(null != snsGroup.getS_grp_title()){
				snsGroup.setS_grp_title(cwUtils.esc4Json(snsGroup.getS_grp_title()));
			}
			if (snsGroupService.getByName(snsGroup.getS_grp_title()) != null) {
				model.addAttribute("isNormal", snsGroupService.hasAdminPrivilege(prof, 0));
				result.rejectValue("s_grp_title", null, null, LabelContent.get(prof.cur_lan, "group_add_error"));
				return "admin/group/add";
			}
			snsGroupService.addSnsGroup(wizbini, snsGroup, prof.usr_ent_id,  prof.my_top_tc_id, image);
			forCallOldAPIService.updUserCredits(null, Credit.CREATE_GROUP, prof.usr_ent_id, prof.usr_id, 0, 0);
			model.addAttribute(RequestStatus.STATUS,  RequestStatus.SUCCESS);
		}
		return "redirect:/app/admin/group";
	}
	
	/**
	 *  清空组成员 +
	 * @param model
	 * @param prof
	 * @param param
	 * @param s_grp_id
	 * @param usr_ent_id
	 * @return
	 */
	@RequestMapping(value = "clear/{grpId}")
	@ResponseBody
	public String clearMember(Model model, loginProfile prof, 
			Params param,
			@PathVariable(value = "grpId") long grpId,
			@RequestParam(value = "userIds", required=true) String userIds){
		SnsGroup group = snsGroupService.get(grpId);
		if(group == null) {
		//	model.addAttribute(RequestStatus.ERROR, RequestStatus.ERROR);
			model.addAttribute(RequestStatus.ERROR, "145");
			 
		} else {
			boolean isAdmin = snsGroupService.hasAdminPrivilege(prof, group.getS_grp_uid());
			if(isAdmin) {
				snsGroupMemberService.clear(userIds, grpId, prof.usr_ent_id);
				model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			}
			else{
			//	model.addAllAttributes(R)
				model.addAttribute(RequestStatus.ERROR,"183");
			}
		}
		return JsonFormat.format(param, model);
	}
	
	/**
	 * 更新审批状态 +
	 * @param s_grp_id 群组id 
	 * @param s_gpm_status 审批状态
	 * @param s_gpm_id_list 审批id
	 * @return
	 */
	@RequestMapping(value = "updateGpmStatus/{grpId}/{status}")
	@ResponseBody
	public String updateGpmStatus(Model model, loginProfile prof, 
			@PathVariable(value = "status") long status,
			@PathVariable(value = "grpId") long grpId,
			@RequestParam(value = "gpmIds", required = true) Long[] gpmIds, 
			@RequestParam(value = "userIds", required = true) Long[] userIds) throws Exception{
		if(gpmIds == null || gpmIds.length < 1) {
			model.addAttribute(RequestStatus.STATUS, RequestStatus.ERROR);
	 		return JsonFormat.format(model);
		}
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		snsGroupMemberService.updateGpmStatus(Arrays.asList(gpmIds), Arrays.asList(userIds), prof.usr_ent_id, status, prof.usr_id, "upd", grpId);
		return JsonFormat.format(model);
	}
	
	/**
	 * 添加群组成员 + 
	 * @param s_grp_id 群组id
	 * @param usr_ent_id 添加成员id
	 */
	@RequestMapping(value = "addMembers/{grpId}")
	@ResponseBody
	public String addGroupMember(Model model, loginProfile prof, 
			@PathVariable(value = "grpId") long grpId, 
			@RequestParam(value = "userIds") Long[] userIds) throws Exception {
		SnsGroup group = snsGroupService.get(grpId);
		if(group == null) {
			//model.addAttribute(RequestStatus.ERROR, RequestStatus.ERROR);
			model.addAttribute(RequestStatus.ERROR, "139");
		} else {
			boolean isAdmin = snsGroupService.hasAdminPrivilege(prof, group.getS_grp_uid());
			if(isAdmin) {
				snsGroupMemberService.addSnsGroupMember(userIds, grpId, prof);
				model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			}
			else{
				model.addAttribute(RequestStatus.ERROR,"183");
			}
		}
		return JsonFormat.format(model);
	}
	
	
	/**
	 * 申请加入群组
	 * @param s_grp_id 群组id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "applyJoinGroup/{s_grp_id}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String applyJoinGroup(Model model, loginProfile prof, Page page,
			@PathVariable(value = "s_grp_id") long s_grp_id ) throws Exception {
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
	 * 退出群组（如果是群组管理员就解散群组）
	 * @param s_grp_id 群组id 
	 * @return
	 */
	@RequestMapping(value = "signOutGroup/{s_grp_id}")
	@ResponseBody
	public void signOutGroup(loginProfile prof, @PathVariable(value = "s_grp_id") long s_grp_id) {
		//判断当前用户是否是群组管理员
		SnsGroup snsGroup = snsGroupService.getSnsGroupDetail(s_grp_id, 0, prof.usr_ent_id);
		if(snsGroupService.hasAdminPrivilege(prof, snsGroup.getS_grp_uid())){
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
	 * @param s_grp_id 群组id 
	 * @param searchContent 搜索内容
	 * @return
	 */
	@RequestMapping("getSnsGroupMemberList/{type}/{s_grp_id}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getSnsGroupMemberList(Model model, Page page, loginProfile prof, @PathVariable(value = "type") String type,
			@PathVariable(value = "s_grp_id") long s_grp_id, @RequestParam(value="searchContent", defaultValue="", required=false) String searchContent) {
		snsGroupMemberService.getSnsGroupMemberList(page, s_grp_id, 0, type, searchContent);
		return JsonFormat.format(model, page);
	}

	
	/**
	 * 群组成员列表
	 * @param s_grp_id 群组id
	 * @param searchContent 搜索内容
	 */
	@RequestMapping(value = "findMembers/{s_grp_id}")
	@ResponseBody
	public String findGroupMemberList(Model model, Page<RegUser> page,WizbiniLoader wizbini, loginProfile prof, @PathVariable(value = "s_grp_id") long s_grp_id,
			@RequestParam(value="searchContent", defaultValue="", required = false) String searchContent,
			@RequestParam(value="instrOnly", defaultValue="false", required = false) boolean instrOnly){
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
	 * @param s_grp_id 群组id
	 * @param usr_ent_id 添加成员id
	 */
	@RequestMapping(value = "addGroupMember/{s_grp_id}/{usr_ent_id}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String addGroupMember(Model model, Page page, loginProfile prof, 
			@PathVariable(value = "s_grp_id") long s_grp_id, 
			@PathVariable(value = "usr_ent_id") long usr_ent_id) throws Exception {
		List<SnsGroupMember> list = snsGroupMemberService.getSnsGroupMemberList(page, s_grp_id, usr_ent_id, "", "").getResults();
		if(list == null || list.size() == 0){
			SnsGroupMember sgp = snsGroupMemberService.getByGroupIdAndUserId(usr_ent_id,s_grp_id);
			if(sgp == null) {
				snsGroupMemberService.addSnsGroupMember(s_grp_id, usr_ent_id, 1, 2);
				forCallOldAPIService.updUserCredits(null, Credit.SYS_JION_GROUP, usr_ent_id, prof.usr_id, 0, 0);
			}
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
			//model.addAttribute("error", "add_member_error");
			
			model.addAttribute("error","139");//139表示失败 通过前台拼接前缀来查找对应的标签  意思是添加失败
		}
		return JsonFormat.format(page.getParams(), model);
	}
	
	/**
	 * 删除群组成员
	 * @param s_grp_id 群组id
	 * @param usr_ent_id 删除成员id
	 */
	@RequestMapping(value = "deleteGroupMember/{s_grp_id}/{usr_ent_id}")
	@ResponseBody
	public String deleteGroupMember(Model model, loginProfile prof, 
			Params param,
			@PathVariable(value = "s_grp_id") long s_grp_id, @PathVariable(value = "usr_ent_id") long usr_ent_id){
		if(snsGroupMemberService.checkGroupMember(usr_ent_id, s_grp_id, 1) && usr_ent_id != prof.usr_ent_id){
			snsGroupMemberService.deleteGroupMember(usr_ent_id, s_grp_id);
		} else {
			model.addAttribute("error", "error_delete_fail");
		}
		return JsonFormat.format(param, model);
	}
	
	/**
	 * 转让群组管理员
	 * @param s_grp_id 群组id
	 * @param usr_ent_id 转让给成员id
	 */
	@RequestMapping(value = "changeManager/{s_grp_id}/{usr_ent_id}")
	@ResponseBody
	public String changeManager(Model model, loginProfile prof,
			Params param,
			@PathVariable(value = "s_grp_id") long s_grp_id, @PathVariable(value = "usr_ent_id") long usr_ent_id){
		if(snsGroupMemberService.checkGroupMember(usr_ent_id, s_grp_id, 1) && usr_ent_id != prof.usr_ent_id){
			snsGroupService.updateGrpUid(usr_ent_id, prof.usr_ent_id, s_grp_id);
			snsGroupMemberService.changeManager(usr_ent_id, s_grp_id, 1);
			snsGroupMemberService.changeManager(prof.usr_ent_id, s_grp_id, 2);
		} else {
			model.addAttribute("error", "142");//转让失败",
			//model.addAttribute("error", "group_assignment_error");转让失败",
		}
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping(value = "del/{s_grp_id}")
	@ResponseBody
	public String delGroup(@PathVariable(value="s_grp_id") long s_grp_id, Model model, loginProfile prof) throws AuthorityException{
		SnsGroup group = snsGroupService.get(s_grp_id);
		if(group != null && snsGroupService.hasAdminPrivilege(prof, group.getS_grp_uid())){
			snsGroupService.deleteAll(s_grp_id);
			model.addAttribute(RequestStatus.STATUS,  RequestStatus.SUCCESS);
		} else {
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_group_dissolved"));
		}
		return JsonFormat.format(model);
	}
	

}
