/**
 * 
 */
package com.cwn.wizbank.utils;

/**
 * 上下问路径，在session过滤器里面设置
 */
public class ContextPath {

	private static String contextPath = "";

	public static String getContextPath() {
		return contextPath;
	}

	/**
	 * request.getContextPath() 
	 * @param contextPath
	 */
	public static void setContextPath(String contextPath) {
		ContextPath.contextPath = contextPath;
	}
	
	
}
