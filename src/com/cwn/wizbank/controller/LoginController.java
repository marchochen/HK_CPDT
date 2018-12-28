/**
 * 
 */
package com.cwn.wizbank.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.SessionListener;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

import com.cwn.wizbank.entity.APIToken;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.services.APITokenService;

import com.cwn.wizbank.services.AcRoleFunctionService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.LoginService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;

/**
 *
 */
@Controller
@RequestMapping("login")
public class LoginController {

	@Autowired
	LoginService loginService;
	@Autowired
	AcRoleFunctionService acRoleFunctionService;
	@Autowired
	RegUserService regUserService;
	@Autowired
	APITokenService apiTokenService;
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@RequestMapping("")
	@ResponseBody
	public String login(Model model,
			loginProfile prof,
			WizbiniLoader wizbini,
			Params params,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "username", required = false, defaultValue = "") String username,
			@RequestParam(value = "password", required = false, defaultValue = "") String password,
			@RequestParam(value = "siteId", required = false, defaultValue = "1") long siteId,
			@RequestParam(value = "developer", required = false, defaultValue = "mobile") String developer,
			@RequestParam(value = "isRemember", required = false, defaultValue = "0") boolean isRemember,
			@RequestParam(value = "loginRole", required = false, defaultValue = "NLRN_1") String loginRole,
			@RequestParam(value = "loginLan", required = false, defaultValue = "en-us") String loginLan
			) throws qdbException, cwException, SQLException, cwSysMessage{
		prof = new loginProfile();
		//如果是移动端
		if(APIToken.API_DEVELOPER_MOBILE.equalsIgnoreCase(developer)){
			prof.isMobileDeviceClien = true;
		}
		String loginStatus  = loginService.login(model,request, prof, wizbini, username, password, loginRole,
				siteId, developer, isRemember, loginLan);
		String browserType = request.getHeader("User-Agent");//浏览器类型
		if (browserType!=null && browserType.contains("MicroMessenger")) {
			developer = "weixin";
		}
		if (dbRegUser.CODE_LOGIN_SUCCESS.equals(loginStatus)) {
			SessionListener sessionListener=new SessionListener(wizbini, prof, request.getRemoteAddr(), session.getId(),developer);  
			session.setAttribute("listener", sessionListener);
			session.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
		}else{
			session.invalidate();
		}
		model.addAttribute("loginUser", prof);
		return JsonFormat.format(params, model);
	}
	
	@RequestMapping(value = "forgetPassword")
	@ResponseBody
	public String findPassword(Model model, HttpServletRequest request, loginProfile prof, WizbiniLoader wizbini,
			@Param(value = "usr_ste_usr_id") String usr_ste_usr_id, @Param(value = "usr_email") String usr_email) throws Exception{
		if (prof == null) {
			prof = new loginProfile();
			prof.root_id = "cw";
			prof.cur_lan = wizbini.cfgSysSkinList.getDefaultLang();
			prof.root_ent_id = 1;
		}
		if(forCallOldAPIService.forget_pwd(null, request, prof, usr_ste_usr_id, usr_email, wizbini, null)){
			model.addAttribute("type", "find");
		} else {
			model.addAttribute("message", "login_find_error");
		}
		return JsonFormat.format(model);
	}
	
	/**
	 * 登錄需修改密碼
	 * @param model
	 * @param usr_ent_id
	 * @param usr_old_pwd
	 * @param usr_new_pwd
	 */
	@RequestMapping(value = "changePwd/{curLang}")
	@ResponseBody
	public String changePwdOnLogin(Model model,
			@RequestParam(value = "usr_ste_usr_id") String usr_ste_usr_id,
			@RequestParam(value = "usr_pwd_old") String usr_pwd_old,
			@RequestParam(value = "usr_pwd_new") String usr_pwd_new,
			@RequestParam(value = "developer", required = false, defaultValue = "mobile") String developer,
			@PathVariable(value = "curLang") String lang) throws Exception {
		
		RegUser regUser = regUserService.getUserDetailByUserSteUsrId(usr_ste_usr_id);
		String encrypt_pwd_old = dbRegUser.encrypt(usr_pwd_old.toLowerCase(), new StringBuffer(usr_ste_usr_id).reverse().toString());
		if(encrypt_pwd_old.equals(regUser.getUsr_pwd())) {
			String result = regUserService.updateUserPwd(regUser.getUsr_ent_id(), usr_pwd_old, usr_pwd_new, usr_ste_usr_id ,developer);
			APIToken atk = null;
			if("update_ok".equals(result)) {
				atk = apiTokenService.getTokenBySteId(usr_ste_usr_id, developer, null);
			}
			model.addAttribute("result", result);
			model.addAttribute("token", atk);
		} else {
			model.addAttribute("result", "pwd_error");
		}
		model.addAttribute("loginUser", regUser);
		return JsonFormat.format(model);
	}
	
	/**
	 * 外部系统鉴权接口
	 * @param model
	 * @param prof
	 * @param wizbini
	 * @param params
	 * @param session
	 * @param username 用户名
	 * @param password 密码
	 * @param eip_code 企业标识---暂时没有用，为了以后拓展
	 * @return
	 * @throws qdbException
	 * @throws cwException
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	@RequestMapping(value = "auth",method = RequestMethod.POST)
	@ResponseBody
	public String authForOuter(Model model,
			loginProfile prof,
			WizbiniLoader wizbini,
			Params params,
			HttpSession session,
			HttpServletRequest request,
			@RequestParam(value = "username", required = false, defaultValue = "") String username,
			@RequestParam(value = "password", required = false, defaultValue = "") String password,
			@RequestParam(value = "eip_code", required = false) String eip_code
			) throws qdbException, cwException, SQLException, cwSysMessage{
		prof = new loginProfile();
		loginService.login(model,request, prof, wizbini, username, password, AccessControlWZB.ROL_EXT_ID_NLRN,
				1, "api", false, null);
		
		model.addAttribute("loginUser", prof.usr_display_bil);
		return JsonFormat.format(params, model);
	}
	
	@RequestMapping("out")
	@ResponseBody
	public String loginOut(Model model, HttpServletRequest request, loginProfile prof, WizbiniLoader wizbini, Params params){
		HttpSession session = request.getSession(false);
		if (session != null) session.invalidate();
		model.addAttribute("status", "success");
		return JsonFormat.format(params, model);
	}
}
