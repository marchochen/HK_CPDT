package com.cwn.wizbank.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.qdbEnv;

public class WzbInterceptor extends HandlerInterceptorAdapter {
	public final static String SCXT_WIZBINI = WizbiniLoader.SCXT_WIZBINI;
	public final static String SCXT_STATIC_ENV = WizbiniLoader.SCXT_STATIC_ENV;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		ServletContext servletContext = request.getSession().getServletContext();
		WizbiniLoader wizbini = WizbiniLoader.getInstance(servletContext);
		qdbEnv static_env = (qdbEnv) servletContext.getAttribute(WizbiniLoader.SCXT_STATIC_ENV);

		if (wizbini != null) {
			request.setAttribute(SCXT_WIZBINI, wizbini);
			//加载配置参数到session，推荐用驼峰法命名
			request.getSession().setAttribute("sns_enabled", wizbini.cfgSysSetupadv.isSnsEnabled());
			request.getSession().setAttribute("isTcIndependent", wizbini.cfgSysSetupadv.isTcIndependent());
			request.getSession().setAttribute("showloginHeaderInd", wizbini.show_login_header_ind);
			request.getSession().setAttribute("showAllFooterInd", wizbini.show_all_footer_ind);
		}
		if (static_env != null) {
			request.setAttribute(SCXT_STATIC_ENV, static_env);
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

		
	}
}
