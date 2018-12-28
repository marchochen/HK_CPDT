package com.cwn.wizbank.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.NativeWebRequest;

public class Params {

	public static Logger logger = LoggerFactory.getLogger(Params.class);

	Map<String, Object> params = new HashMap<String, Object>();


	public Params(){
		
	}
	
	public Params(NativeWebRequest webRequest) {
		String param = null;
		String value = null;

		// 输出查询参数
		Iterator<String> it = webRequest.getParameterNames();
		while (it.hasNext()) {
			param = it.next();
			value = webRequest.getParameter(param);

			this.params.put(param, value);

			logger.debug("param [{}] values [{}]", new Object[] { param, value });
		}
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
}
