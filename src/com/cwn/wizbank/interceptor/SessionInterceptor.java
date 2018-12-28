package com.cwn.wizbank.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.exception.SessionTimeOutException;
import com.cwn.wizbank.services.LoginService;

public class SessionInterceptor extends HandlerInterceptorAdapter {
	private List<String> urlList = null;
	private List<String> mobileApi = null;
	
	@Autowired
	LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 在这里判断，如果Session过期，把页面重定向到过期页面。
		HttpSession sess = request.getSession(false);
		String developer = request.getParameter("developer");
		loginProfile prof = null;
		if (request.getRequestURI().indexOf("app/error/") > 0 || request.getRequestURI().indexOf("app\\error\\") > 0) {
		
		} else if (getUrlList(request.getRequestURI()) >= 0) {
			return true;
		} else if(StringUtils.isNotEmpty(developer) && sess != null){
			prof = (loginProfile) sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
			return filterMobileUrl(request, prof, developer);
		}  else if (sess != null) {
			prof = (loginProfile) sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
			if (prof == null || prof.usr_id == null) {
				throw new SessionTimeOutException();
			}
		} else {
			throw new SessionTimeOutException();
		}
		return true;
	}

	private boolean filterMobileUrl(HttpServletRequest request, loginProfile prof, String developer) throws MessageException {
		if(request.getRequestURI().indexOf("app/login") > -1){
			HttpSession sess = request.getSession(false);
			if(sess != null) {
				sess.invalidate(); 
			}
			prof = null;
		}
		if(prof == null || prof.usr_id == null){
			prof = new loginProfile();
			String token = request.getParameter("token");
			loginService.initProfFromToken(prof, token);
			if (prof != null) {
				HttpSession sess = request.getSession(false);
				sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
			}
		}
		return true;
	}

	public int getUrlList(String url) {
		int num = 0;
		for(int i=0;i<urlList.size();i++){
			num = url.indexOf(urlList.get(i));
			if(num > -1){
				break;
			}
		}
		return num;
	}
	
	public int getMobileApi(String url) {
        //int num = 0;
        if(url == null){
            return 0;
        }
        if(mobileApi != null && mobileApi.size() > 0){
            for(int i =0; i< mobileApi.size(); i++){
                String api = mobileApi.get(i);
                if(api != null && url.indexOf(api) >=0){
                    return 1;
                }
            }
            
        }
        return 0;
    }
	

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}
	
	public void setMobileApi(List<String> mobileApi) {
        this.mobileApi = mobileApi;
    }

}