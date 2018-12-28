package com.cwn.wizbank.utils;

import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cw.wizbank.Application;

public class IdocViewUtils {
	
	static Logger logger = LoggerFactory.getLogger(IdocViewUtils.class);
	

	/**
	 * 获取I doc view预览的URL地址 用于移动端
	 * @param filePath
	 * @return
	 */
	public static String getViewPath(String filePath){
		String path = "";
		 try {
			path = java.net.URLEncoder.encode(filePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("[com.cwn.wizbank.utils.IdocViewUtils] err : " + e.getMessage());
		}
		return Application.I_DOC_VIEW_HOST+"view/url?url="+path+"&token="+Application.I_DOC_VIEW_TOKEN+"&type=mobile";
	}
	

	/**
	 * 获取I doc view预览的URL地址 用于网页端
	 * @param filePath
	 * @return
	 */
	public static String getViewPathForWeb(String filePath){
		String path = "";
		 try {
			path = java.net.URLEncoder.encode(filePath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("[com.cwn.wizbank.utils.IdocViewUtils] err : " + e.getMessage());
		}
		return Application.I_DOC_VIEW_HOST+"view/url?url="+path+"&token="+Application.I_DOC_VIEW_TOKEN;
	}

}
