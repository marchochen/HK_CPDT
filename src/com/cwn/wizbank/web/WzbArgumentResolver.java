package com.cwn.wizbank.web;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbEnv;
import com.cwn.wizbank.interceptor.WzbInterceptor;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

@SuppressWarnings("rawtypes")
public class WzbArgumentResolver implements WebArgumentResolver {

	public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
		if (methodParameter.getParameterType().equals(loginProfile.class)) {
			return webRequest.getAttribute(qdbAction.AUTH_LOGIN_PROFILE, RequestAttributes.SCOPE_SESSION);
		} else if (methodParameter.getParameterType().equals(WizbiniLoader.class)) {
			return webRequest.getAttribute(WzbInterceptor.SCXT_WIZBINI, RequestAttributes.SCOPE_REQUEST);
		} else if (methodParameter.getParameterType().equals(qdbEnv.class)) {
			return webRequest.getAttribute(WzbInterceptor.SCXT_STATIC_ENV, RequestAttributes.SCOPE_REQUEST);
		} else if (methodParameter.getParameterType().equals(Page.class)) {
			return new Page(webRequest);
		} else if(methodParameter.getParameterType().equals(Params.class)){
			return new Params(webRequest);
		}
		return UNRESOLVED;
	}
}
