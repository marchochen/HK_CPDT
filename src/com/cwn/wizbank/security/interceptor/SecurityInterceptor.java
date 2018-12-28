package com.cwn.wizbank.security.interceptor;

import javax.servlet.http.Cookie;
import java.util.Date;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cw.wizbank.Application;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.SessionListener;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.entity.APIToken;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.exception.MultipleLoginException;
import com.cwn.wizbank.exception.SessionTimeOutException;
import com.cwn.wizbank.persistence.RegUserMapper;
import com.cwn.wizbank.security.annotation.Anonymous;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AcRoleService;
import com.cwn.wizbank.services.LoginService;
import com.cwn.wizbank.utils.CookieUtil;

/**
 * 访问控制 
 */
public class SecurityInterceptor extends HandlerInterceptorAdapter {

	Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

	@Autowired
	LoginService loginService;

	@Autowired
	RegUserMapper regUserMapper;
	
	@Autowired
	AclService aclService;
	
	@Autowired
	AcRoleService acRoleService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		
		String developer = request.getParameter("developer");

		HandlerMethod handlerMethod = (HandlerMethod) handler;
		HasPermission hasPermissionClassAnnotation = handlerMethod.getBean().getClass().getAnnotation(HasPermission.class);
		HasPermission hasPermissionMethodAnnotation = handlerMethod.getMethodAnnotation(HasPermission.class);

		Anonymous anonymousClassAnnotation = handlerMethod.getBean().getClass().getAnnotation(Anonymous.class);
		Anonymous anonymousMethodAnnotation = handlerMethod.getMethodAnnotation(Anonymous.class);

		loginProfile prof = null;
		// 检查Session是否已经包含登录信息
		if (session.getAttribute(qdbAction.AUTH_LOGIN_PROFILE) != null) {
			prof = (loginProfile) session.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
			//处理是否能一次登陆多个账户
			if(!handleMultipleLogin(prof, session)){
				throw new MultipleLoginException("error_landed_somewhere_else");
			};
		}
		
		// 检查移动端API是否已经包含登录信息
		if ((APIToken.API_DEVELOPER_MOBILE.equalsIgnoreCase(developer) || APIToken.API_DEVELOPER_API.equalsIgnoreCase(developer))
				&& session != null 
				&& request.getRequestURI().indexOf("/user/sitePoster") == -1) {
			return filterMobileUrl(request, prof, developer);
		}

		String lang = null;
		if(prof != null) {
			CookieUtil.addCookie(response, "user_lan", prof.cur_lan, dbRegUser.LOGIN_LAN_AGE);
		}
		
		if (anonymousClassAnnotation == null && anonymousMethodAnnotation == null && prof == null) {
			//如果出现Session 过期，直接重定Session过期控制器
			response.sendRedirect("/app/session/out");
			throw new SessionTimeOutException();
		}
		if (hasPermissionMethodAnnotation != null) {
			String[] permissions = hasPermissionMethodAnnotation.value();
			if (!aclService.hasAnyPermission(prof.current_role, permissions)) {
				throw new AuthorityException();
			}
		} else if (hasPermissionClassAnnotation != null) {
			String[] permissions = hasPermissionClassAnnotation.value();
			if (!aclService.hasAnyPermission(prof.current_role, permissions)) {
				throw new AuthorityException();
			}
		}
		return true;
	}
	public void postHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler,  
            ModelAndView modelAndView) throws Exception {  
			if(modelAndView!=null)
			System.out.println("modelAndView  :     " + modelAndView.getViewName());
  
    }	
	//处理是否能一次登陆多个账户
	private boolean handleMultipleLogin(loginProfile prof, HttpSession session){
		try {
			if (prof != null && prof.usr_id != null && prof.usr_id.length() > 0) {
				if (!Application.MULTIPLE_LOGIN && prof.login_date != null
						&& !prof.login_date.equals(regUserMapper.selectLastLoginTime(prof.usr_ent_id))) {
					session.invalidate();
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

	private boolean filterMobileUrl(HttpServletRequest request, loginProfile prof, String login_type) throws MessageException, cwException, InstantiationException, IllegalAccessException, ClassNotFoundException, qdbException, SQLException, cwSysMessage, NamingException {
		//如果是登陆页面，放行
		HttpSession sess = request.getSession();

		String token = request.getParameter("token");
		if(prof != null){
			if(!regUserMapper.isUserExist(prof.getUsr_ste_usr_id())){
				token = null;
			}
		}
		if(request.getRequestURI().indexOf("app/login") > -1){
			prof = null;
			sess.invalidate();
			return true;
		}
		if (prof == null || prof.usr_id == null || StringUtils.isEmpty(token)) {
			prof = new loginProfile();
			String id = prof.getUsr_ste_usr_id()==null ? "" : prof.getUsr_ste_usr_id();
			if(!regUserMapper.isUserExist(id)){
				token = "";
			}
			loginService.initProfFromToken(prof, token);

			if (prof != null) {
				if(regUserMapper.isUserExist(id)){
					prof = null;
				}
				sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
				if(!(prof.usr_status != null && prof.usr_status.equals(dbRegUser.USR_STATUS_SYS))){
					String browserType = request.getHeader("User-Agent");//浏览器类型
					if (browserType!=null && browserType.contains("MicroMessenger")) {
						login_type = "weixin";
					}
                	SessionListener sessionListener=new SessionListener(WizbiniLoader.getInstance(), prof, request.getRemoteAddr(), sess.getId(),login_type);  
	                sess.setAttribute("listener", sessionListener);
            	}
			}
		}	
		return true;
	}
}
