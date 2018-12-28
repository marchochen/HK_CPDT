package com.cwn.wizbank.controller.admin;

import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.UserGroup;
import com.cwn.wizbank.entity.vo.UserVo;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.AcSiteMapper;
import com.cwn.wizbank.services.EnterpriseInfoPortalService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.LoginService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SitePosterService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.services.UserGroupService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.utils.RequestStatus;

/**
 * 
 * @author lance 登录
 */
@Controller("adminUserController")
@RequestMapping("admin/user")
public class UserController {
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	@Autowired
	private EnterpriseInfoPortalService eipService; 
	@Autowired
	private SitePosterService spService;
	@Autowired
	private TcTrainingCenterService tcrService;
	@Autowired
	AcSiteMapper acSiteMapper;
	@Autowired
	UserGroupService userGroupService;
	@Autowired
	RegUserService regUserService;
	@Autowired
	LoginService loginService;
	@Autowired
	UserGroupService uerGroupService;
	
	@Autowired
	EnterpriseInfoPortalService enterpriseInfoPortalService;
	
	@RequestMapping(value = "getUserList")
	@ResponseBody
	public String userlist(Model model, loginProfile prof, WizbiniLoader wizbini,@RequestParam(value="epi_code", required=false) String epi_code,Params params) throws Exception {
		if(enterpriseInfoPortalService.canManageInterprise( wizbini, prof, epi_code)){
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			model.addAttribute("usr_list", regUserService.getList(params));
			return JsonFormat.format(model);
		}else{
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
	
		
	}
	
	
	@RequestMapping(value = "getUserGroupList")
	@ResponseBody
	public String userGrouplist(Model model, loginProfile prof, WizbiniLoader wizbini,@RequestParam(value="epi_code", required=false) String epi_code,Params params) throws Exception {
		if(enterpriseInfoPortalService.canManageInterprise( wizbini, prof, epi_code)){
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			model.addAttribute("usg_list", userGroupService.getList(params));
			model = RequestStatus.setSuss(model);

		}else{
			model = RequestStatus.setError(model,RequestStatus.CODE_403,LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		return JsonFormat.format(model);
	}
	
	
	@RequestMapping("addUsg")
	@ResponseBody
	public Model addUsg (
			@RequestParam(value="usg_name", required=true) String usgName,
			@RequestParam(value="usg_parent_usg_code", required=true) String usg_parent_usg_code,
			@RequestParam(value="epi_code", required=false) String epi_code,
			loginProfile prof, Model model,WizbiniLoader wizbini
			) throws Exception {
		if(enterpriseInfoPortalService.canManageInterprise( wizbini, prof, epi_code)){
			
			UserGroup p_usg = this.userGroupService.getByUsgCode(usg_parent_usg_code);
			if(p_usg != null) {
			} else {
				throw new DataNotFoundException();
			}
			
			long usgEntId = userGroupService.create(usgName, p_usg.getUsg_ent_id(), prof.usr_id);
			model.addAttribute("usg_code", usgEntId);
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			model = RequestStatus.setSuss(model);
			
		}else{
			model = RequestStatus.setError(model,RequestStatus.CODE_403,LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		return model;
//		return JsonFormat.format(model);
	}
	
	
	@RequestMapping("editUsg")
	@ResponseBody
	public Model editUsg (
			@RequestParam(value="usg_code", required=true) String usg_code,
			@RequestParam(value="usg_name", required=true) String usgName,
			@RequestParam(value="epi_code", required=false) String epi_code,
			loginProfile prof, Model model,WizbiniLoader wizbini
			)throws Exception {
		if(enterpriseInfoPortalService.canManageInterprise( wizbini, prof, epi_code)){
			UserGroup usg = this.userGroupService.getByUsgCode(usg_code);
			if(usg != null) {
				usg.setUsg_name(usgName);
				userGroupService.update(usg);
				model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			} else {
				throw new DataNotFoundException();
			}
			model = RequestStatus.setSuss(model);
		}else{
			model = RequestStatus.setError(model,RequestStatus.CODE_403,LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		return model;
//		return JsonFormat.format(model);
		
	}
	
	@RequestMapping("delUsg")
	@ResponseBody
	public Model delUsg (
			@RequestParam(value="usg_code", required=true) String usg_code,
			@RequestParam(value="epi_code", required=false) String epi_code,
			loginProfile prof, Model model,WizbiniLoader wizbini
			) throws Exception{
		if(enterpriseInfoPortalService.canManageInterprise( wizbini, prof, epi_code)){
			userGroupService.deleteUsg(usg_code, prof.usr_id, true);
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			model = RequestStatus.setSuss(model);
		}else{
			model = RequestStatus.setError(model,RequestStatus.CODE_403,LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		return model;
//		return JsonFormat.format(model);
	}
	
	
	@RequestMapping(value = "addUsr", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Model addUsr (
			@RequestParam(value="usr_usg_code", required=true) String usg_code,
			@RequestParam(value="usr_rol_lst", required=false) String rolelist,
			@ModelAttribute("usr") UserVo usr,
			@RequestParam(value="epi_code", required=false) String epi_code,
			HttpServletRequest request,
			loginProfile prof, Model model,
			HttpSession session,
			WizbiniLoader wizbini
			) throws Exception{
		if(enterpriseInfoPortalService.canManageInterprise( wizbini, prof, epi_code)){
			UserGroup usg = this.userGroupService.getByUsgCode(usg_code);
			long usgEntId = 0;
			if(usg != null) {
				usgEntId = usg.getUsg_ent_id();
				try {
					regUserService.addUsr(usr, usgEntId, rolelist, prof.usr_ent_id, prof.root_ent_id);
					model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
					model = RequestStatus.setSuss(model);
				}catch(MessageException e){
					String[] msg = e.getMessage().split("-");
					model = RequestStatus.setError(model, msg[0], msg.length > 1 ? msg[1] : "");
				}
			} else {
				model = RequestStatus.setError(model,"405", "上级用户组不存在");
			}
		}else{
			model = RequestStatus.setError(model,RequestStatus.CODE_403,LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		return model;
	}
	
	@RequestMapping(value = "editUsr", produces = "application/json; charset=utf-8" )
	@ResponseBody
	public Model editUsr (
			@RequestParam(value="usr_usg_code", required=true) String usg_code,
			@RequestParam(value="usr_rol_lst", required=false) String rolelist,
			@ModelAttribute("usr") UserVo usr,
			@RequestParam(value="epi_code", required=false) String epi_code,
			loginProfile prof, Model model,WizbiniLoader wizbini
			) throws Exception{
		if(enterpriseInfoPortalService.canManageInterprise( wizbini, prof, epi_code)){
			UserGroup usg = this.userGroupService.getByUsgCode(usg_code);
			long usgEntId = 0;
			if(usg != null) {
				usgEntId = usg.getUsg_ent_id();
				try {
					regUserService.edit(usr, usgEntId, rolelist, prof.usr_ent_id);
					model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
					model = RequestStatus.setSuss(model);
				}catch(MessageException e){
					String[] msg = e.getMessage().split("-");
					model = RequestStatus.setError(model, msg[0], msg.length > 1 ? msg[1] : "");
				}
			} else {
				model = RequestStatus.setError(model,"405", "上级用户组不存在");
			}
		}else{
			model = RequestStatus.setError(model,RequestStatus.CODE_403,LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		return model;
//		return JsonFormat.format(model);
	}
	
	@RequestMapping(value = "delUsr", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Model delUsr (
			@RequestParam(value="usr_login_id", required=true) String usrSteUsrId,
			@RequestParam(value="epi_code", required=false) String epi_code,
			loginProfile prof, Model model, WizbiniLoader wizbini
			) throws Exception{
		if(enterpriseInfoPortalService.canManageInterprise( wizbini, prof, epi_code)){
			try {
				regUserService.del(usrSteUsrId, prof.usr_ent_id, prof.root_ent_id, prof.usr_id);
				model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
				model = RequestStatus.setSuss(model);
			}catch (MessageException me){
				String[] msg = me.getMessage().split("-");
				model = RequestStatus.setError(model, msg[0], msg.length > 1 ? msg[1] : "");
			}
		}else{
			model = RequestStatus.setError(model,RequestStatus.CODE_403,LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		return model;
	}
	
	@RequestMapping("editPwd")
	@ResponseBody
	public Model editPwd (
			@ModelAttribute("usr") UserVo usr,
			@RequestParam(value="epi_code", required=false) String epi_code,
			loginProfile prof, Model model,WizbiniLoader wizbini
			) throws Exception{
		if(enterpriseInfoPortalService.canManageInterprise( wizbini, prof, epi_code)){
			regUserService.editPwd(usr, prof.usr_ent_id);
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			model = RequestStatus.setSuss(model);
		}else{
			model = RequestStatus.setError(model,RequestStatus.CODE_403,LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		return model;
//		return JsonFormat.format(model);

	}
	
	
	/**
	 * 跳转到批量导出用户页面
	 * @param mav
	 * @param prof
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "exportUserInfo")
	public ModelAndView exportUserInfo(ModelAndView mav, loginProfile prof) throws Exception{
		mav = new ModelAndView("admin/user/exportUserInfo");
		return mav;
	}
	
	/**
	 * 批量导出用户信息
	 * @param uIdList
	 * @param gIdList
	 * @param model
	 * @param prof
	 * @param wizbini
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "expor")
	@ResponseBody
	public Model expor(
			@RequestParam(value="usr_id_lst", required=false) String uIdList,
			@RequestParam(value="grp_id_lst", required=false) String gIdList,
			Model model, loginProfile prof, WizbiniLoader wizbini) throws Exception{
		//导出用户，生成文件
	    String fileName =	regUserService.expor(prof, wizbini, uIdList, gIdList);
		model.addAttribute("fileUri","/temp/"+fileName);
		return model;
	}
	
	
	
	@RequestMapping("pageGroupUserJson")
	@ResponseBody
	public String pageGroupUserJson(Page<RegUser> page, loginProfile prof, Model model,WizbiniLoader wizbini,
			String searchText,
			@RequestParam(value = "showSubordinate", required = false, defaultValue = "false") boolean showSubordinateInd) throws ParseException{
		String groupIds = uerGroupService.getGroupIdByUserId(prof.getUsr_ent_id(), wizbini.cfgSysSetupadv.isTcIndependent(),
				prof.my_top_tc_id, prof.current_role, AccessControlWZB.isRoleTcInd(prof.current_role));
		page = regUserService.getPageUserByGroupId(page,groupIds,prof,wizbini,searchText,showSubordinateInd);
		return JsonFormat.format(model, page);
	}
}
