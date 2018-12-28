package com.cwn.wizbank.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.AcSite;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.UsrPwdResetHis;
import com.cwn.wizbank.persistence.AcSiteMapper;
import com.cwn.wizbank.security.annotation.Anonymous;
import com.cwn.wizbank.services.APITokenService;
import com.cwn.wizbank.services.AcSiteService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SitePosterService;
import com.cwn.wizbank.utils.CookieUtil;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

/**
 * 
 * @author lance 登录
 */
@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	RegUserService regUserService;	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	@Autowired
	private SitePosterService spService;
	@Autowired
	AcSiteMapper acSiteMapper;
	@Autowired
	APITokenService apiTokenService;
	
	
	@Autowired
	AcSiteService acSiteService;
	
	/**
	 * 登录页面
	 * 
	 * @param command
	 *            : userLogin 登录页面
	 *            : userRegister 新用户注册
	 *            : forgetPassword 忘记密码
	 * @return
	 */
	@Anonymous
	@RequestMapping(value = "{command}/{code}", method = RequestMethod.GET)
	public ModelAndView login(ModelAndView mav, WizbiniLoader wizbini, @PathVariable(value = "command") String command, 
			@PathVariable(value = "code") String code, @RequestHeader(value = "Host") String host, 
			HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "lang", required = false) String lang) throws Exception {
		mav = new ModelAndView("user/" + command);
		mav.addObject("code", cwUtils.esc4JS(code.substring(1)));
		AcSite acSite =  acSiteService.getSite();
		if(acSite != null){
			mav.addObject("loginMaxTrial", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getAccountSuspension().getMaxTrial());
			mav.addObject("isActive", ((UserManagement) wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getAccountSuspension().isActive());
			mav.addObject("ste_ent_id", acSite.getSte_ent_id());
			if(command.equalsIgnoreCase("userRegister")){
				mav.addObject("usrIdMinLength", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getUserProfile().getProfileAttributes().getUserId().getMinLength());
				mav.addObject("usrIdMaxLength", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getUserProfile().getProfileAttributes().getUserId().getMaxLength());
				mav.addObject("pwdMinLength", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getUserProfile().getProfileAttributes().getPassword().getMinLength());
				mav.addObject("pwdMaxLength", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getUserProfile().getProfileAttributes().getPassword().getMaxLength());
			} else if("userLogin".equals(command)){
				HttpSession session = request.getSession(false);
				if(session != null){
					session.invalidate();
					
					Cookie[] cookieList = request.getCookies(); //获取cookie
					if(null != cookieList && cookieList.length > 0){
						for(Cookie cookie : cookieList){
							cookie.setMaxAge(0);//让cookie过期
						}
					}
				   // Cookie cookie = request.getCookies()[0];//获取cookie
			       // cookie.setMaxAge(0);//让cookie过期
				}
				//request.getSession(true);//生成新会话
				
				request.getSession().setAttribute("showloginHeaderInd", wizbini.show_login_header_ind);
				request.getSession().setAttribute("showAllFooterInd", wizbini.show_all_footer_ind);
			}
			Cookie cookie = null;
			cookie = CookieUtil.getCookie(request, qdbAction.REMEMBER_ME_USER_ID);
			if(cookie != null) {
				String userId = cookie.getValue();
				mav.addObject("user_id", userId);
			}
			mav.addObject("site", acSite.getSte_id());
			mav.addObject("sitePoster", spService.getSitePoster(host));
			cookie = CookieUtil.getCookie(request, dbRegUser.LOGIN_LAN);
			String login_lan = null;
			if(cookie != null) {
				login_lan = cookie.getValue();
			}
			if(lang != null){
				login_lan = lang;
			}
			if("forgetPassword".equals(command)) {
				CookieUtil.addCookie(response, dbRegUser.LOGIN_LAN, lang, dbRegUser.LOGIN_LAN_AGE);
				getSystemLang(mav, wizbini, lang);
			} else {
				getSystemLang(mav, wizbini, login_lan);
			}
		}else{
			
		}
		return mav;
	}
	@Anonymous
	@RequestMapping(value = "sitePoster")
	@ResponseBody
	public String sitePoster(Model model, WizbiniLoader wizbini,  @RequestHeader(value = "Host") String host ){
		AcSite acSite = acSiteMapper.selectAll().get(0);
		model.addAttribute("ste_ent_id", acSite.getSte_ent_id());
		model.addAttribute("site", acSite.getSte_id());
		model.addAttribute("sitePoster", spService.getSitePoster(host, 1));
		return JsonFormat.format(model);
	}
	/**
	 * 新用户注册
	 * @param regUser
	 * @param birthday 出生日期
	 * @param join_datetime 加入日期
	 * @return
	 */
	@RequestMapping(value = "userRegister/register", method = RequestMethod.POST)
	@ResponseBody
	@Anonymous
	public ModelAndView register(ModelAndView mav, WizbiniLoader wizbini, @Param(value = "regUser") RegUser regUser,
			@Param(value = "birthday") String birthday, @Param(value = "join_datetime") String join_datetime, @RequestHeader(value = "Host") String host) throws Exception{
		AcSite acSite = acSiteMapper.selectAll().get(0);
		if(forCallOldAPIService.checkUserNumExceedLimit(null, acSite.getSte_ent_id())){
			mav = new ModelAndView("user/userRegister");
			mav.addObject("message", "error_exceed_max_usr");
		}else{
			if(!regUserService.checkUserName(regUser.getUsr_ste_usr_id())){
				regUserService.userRegister(regUser, birthday, join_datetime);
				mav = new ModelAndView("user/messageOk");
				mav.addObject("type", "register");
			} else {
				mav = new ModelAndView("user/userRegister");
				mav.addObject("message", "error_user_name_exist");
			}
		}
		
		
		mav.addObject("site", acSite.getSte_id());
		mav.addObject("sitePoster", spService.getSitePoster(host));		
		getSystemLang(mav, wizbini);
		
		return mav;
	}
	
	/**
	 * 用户名检测
	 * @param usr_ste_usr_id 用户名
	 * @return
	 */
	@Anonymous
	@RequestMapping(value = "checkUserName", method = RequestMethod.POST)
	@ResponseBody
	public String checkUserName(Model model, 	
			Params params,
			@RequestParam(value="usr_ste_usr_id", defaultValue="", required=false) String usr_ste_usr_id){
		model.addAttribute("message",regUserService.checkUserName(usr_ste_usr_id));
		return JsonFormat.format(params, model);
	}
	
	/**
	 * 找回密码
	 * @param usr_ste_usr_id 用户名
	 * @param usr_email 邮箱
	 * @return
	 */
	@RequestMapping(value = "forgetPassword/findPassword", method = RequestMethod.POST)
	@ResponseBody
	@Anonymous
	public ModelAndView findPassword(ModelAndView mav, HttpServletRequest request, loginProfile prof, WizbiniLoader wizbini,
			@Param(value = "usr_ste_usr_id") String usr_ste_usr_id, 
			@Param(value = "usr_email") String usr_email,
			@Param(value = "lang") String lang) throws Exception{
		if (prof == null) {
			prof = new loginProfile();
			prof.root_id = "cw";
			prof.cur_lan = wizbini.cfgSysSkinList.getDefaultLang();
			prof.root_ent_id = 1;
		}
		if(forCallOldAPIService.forget_pwd(null, request, prof, usr_ste_usr_id, usr_email, wizbini, lang)){
			mav = new ModelAndView("user/messageOk_");
			mav.addObject("type", "find");
		} else {
			mav = new ModelAndView("user/forgetPassword");
			mav.addObject("message", "login_find_error");
		}
		
		getSystemLang(mav, wizbini, lang);
		return mav;
	}
	
	/**
	 * 重置密码
	 * @param prh_id 重置密码id
	 * @return
	 */
	@RequestMapping(value = "userResetPwd/{prh_id}", method = RequestMethod.GET)
	@Anonymous
	public ModelAndView userResetPwd(ModelAndView mav, WizbiniLoader wizbini, 
			@PathVariable(value = "prh_id") long prh_id, @RequestHeader(value = "Host") String host, 
			@RequestParam(value = "key") long key, @RequestParam(value = "lang") String lang,
			HttpServletResponse response) throws Exception {
		AcSite acSite = acSiteMapper.selectAll().get(0);
		
        // 对id进行解密
		boolean is_invalid = false;
		prh_id = prh_id - CwnUtil.FORGET_PWD_KEY_1 ;
		long temp_key = key + prh_id + CwnUtil.FORGET_PWD_KEY_2;
		
		if(temp_key == CwnUtil.FORGET_PWD_KEY_1){
			is_invalid = true;
		}
		
		long max_attempt =((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getForgetPassword().getMaxAttempt();
		long link_last_days = ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getForgetPassword().getLinkLastDays();
		UsrPwdResetHis usrPwdResetHis = regUserService.getUsrPwdResetHis(prh_id, max_attempt, link_last_days);
		if( !is_invalid || usrPwdResetHis == null){
			mav = new ModelAndView("user/userLogin");
			mav.addObject("message", "login_connect_fail");
			mav.addObject("loginMaxTrial", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getAccountSuspension().getMaxTrial());
			mav.addObject("isActive", ((UserManagement) wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getAccountSuspension().isActive());
			mav.addObject("ste_ent_id", acSite.getSte_ent_id());
		} else {
			mav = new ModelAndView("user/userResetPwd");
			String usr_ste_usr_id = usrPwdResetHis.getUser().getUsr_ste_usr_id();
			String tmp_str = "";
			if(usr_ste_usr_id.length() > 3) {
				for(int i=0; i<usr_ste_usr_id.length() - 3; i++) {
					tmp_str += "*";
				}
			}
			String usr_ste_usr_id_page = usr_ste_usr_id.substring(0, 3) + tmp_str;
			mav.addObject("usr_ste_usr_id_page", usr_ste_usr_id_page);
			mav.addObject("usr_ste_usr_id", usr_ste_usr_id);
			mav.addObject("prh_id", prh_id);
			mav.addObject("pwdMinLength", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getUserProfile().getProfileAttributes().getPassword().getMinLength());
			mav.addObject("pwdMaxLength", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getUserProfile().getProfileAttributes().getPassword().getMaxLength());
		}
		
		mav.addObject("site",acSite.getSte_id());
		mav.addObject("sitePoster", spService.getSitePoster(host));
		
		if(lang != null) {
			CookieUtil.addCookie(response, dbRegUser.LOGIN_LAN, lang, dbRegUser.LOGIN_LAN_AGE);
			getSystemLang(mav, wizbini, lang);
		} else {
			getSystemLang(mav, wizbini);
		}
		return mav;
	}
	
	/**
	 * 修改密码
	 * @param prh_id 重置密码id
	 * @param usr_ste_usr_id 用户名
	 * @param usr_pwd 用户密码
	 * @return
	 */
	@RequestMapping(value = "userResetPwd/updatePwd", method = RequestMethod.POST)
	@Anonymous
	public ModelAndView updatePwd(ModelAndView mav, WizbiniLoader wizbini,
			@RequestParam(value = "prh_id", defaultValue = "", required = false) long prh_id,
			@RequestParam(value = "usr_ste_usr_id", defaultValue = "", required = false) String usr_ste_usr_id,
			@RequestParam(value = "usr_pwd", defaultValue = "", required = false) String usr_pwd, 
			@RequestParam(value = "lang", defaultValue = "en-us", required = false) String lang, 
			@RequestHeader(value = "Host") String host,loginProfile prof) throws Exception {
		AcSite acSite = acSiteMapper.selectAll().get(0);
		String message = forCallOldAPIService.resetPassword(null, wizbini, prh_id, usr_ste_usr_id, usr_pwd,prof);
		if(message.equalsIgnoreCase("login_update_pwd_ok")){
			regUserService.updatePrhStatus(prh_id);
			mav = new ModelAndView("user/messageOk_");
			mav.addObject("loginMaxTrial", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getAccountSuspension().getMaxTrial());
			mav.addObject("isActive", ((UserManagement) wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getAccountSuspension().isActive());
			mav.addObject("ste_ent_id", acSite.getSte_ent_id());
			mav.addObject("type", "resetPwd");
		} else {
			mav = new ModelAndView("user/userResetPwd");
			mav.addObject("usr_ste_usr_id", usr_ste_usr_id);
			mav.addObject("prh_id", prh_id);
			mav.addObject("pwdMinLength", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getUserProfile().getProfileAttributes().getPassword().getMinLength());
			mav.addObject("pwdMaxLength", ((UserManagement)wizbini.cfgOrgUserManagement.get(acSite.getSte_id())).getUserProfile().getProfileAttributes().getPassword().getMaxLength());
			mav.addObject("message", message);
		}
		
		mav.addObject("site",acSite.getSte_id());
		mav.addObject("sitePoster", spService.getSitePoster(host));
		
		getSystemLang(mav, wizbini, lang);
		return mav;
	}

	/**
	 * 多语言切换
	 * @param curLang
	 * @return
	 */
	@RequestMapping(value = "changeLang/{curLang}")
	@ResponseBody
	public String changeLang(Model model, loginProfile prof, @PathVariable(value = "curLang") String curLang) throws Exception {
		forCallOldAPIService.changeLang(null, prof, curLang);
		return JsonFormat.format(model);
	}
	
	/**
	 * 获取所设置语言
	 * @return
	 */
	public ModelAndView getSystemLang(ModelAndView mav, WizbiniLoader wizbini){
		String lang = wizbini.cfgSysSkinList.getDefaultLang();
		String encoding = cwUtils.langToLabel(lang);
		String label_lan = "en";
		if (encoding.equals("Big5")) {
			label_lan = "ch";
		} else if (encoding.equals("GB2312")) {
			label_lan = "gb";
		}
		mav.addObject("lang", lang);
		mav.addObject("label_lan", label_lan);
		mav.addObject("encoding", encoding);
		return mav;
	}
	
	public ModelAndView getSystemLang(ModelAndView mav, WizbiniLoader wizbini, String login_lan){
		String lang = null;
		if(login_lan != null && !"".equals(login_lan) && login_lan.length() > 0) {
			lang = login_lan;
		} else {
			lang = "en-us";
		}
		wizbini.cfgSysSkinList.setDefaultLang(lang);//设置页面皮肤语言
		String encoding = cwUtils.langToLabel(lang);
		String label_lan = "en";
		if (encoding.equals("Big5")) {
			label_lan = "ch";
		} else if (encoding.equals("GB2312")) {
			label_lan = "gb";
		}
		mav.addObject("lang", lang);
		mav.addObject("label_lan", label_lan);
		mav.addObject("encoding", encoding);
		return mav;
	}
	
	/**
	 * 离线签到
	 * @param itm_id
	 * @return 
	 */
	@Anonymous
	@RequestMapping(value = "qiandao/{ils_id}", method = RequestMethod.GET)
	@ResponseBody
	public String QianD(Model model, WizbiniLoader wizbini, loginProfile prof, @PathVariable(value = "ils_id") long ils_id) throws Exception {
		Map<String, String> map = forCallOldAPIService.offlineRegistration(null, prof, ils_id);
		model.addAttribute("msg", map.get("msg"));
		model.addAttribute("status", map.get("status"));
		return JsonFormat.format(model);
	}
	
	@RequestMapping("getInstructors")
	@ResponseBody
	public String getInstructors(Model model, WizbiniLoader wizbini, Page<RegUser> page, loginProfile prof,
			@RequestParam(value="searchContent", defaultValue="", required = false) String searchContent,
			@RequestParam(value="instrOnly", defaultValue="false", required = false) boolean instrOnly){
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		this.regUserService.getInstructors(page, tcrId, searchContent, instrOnly);
		return JsonFormat.format(model, page);
	}
	
}
