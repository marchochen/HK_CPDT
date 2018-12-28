package com.cwn.wizbank.utils;

import java.util.List;

/**
 * 字符串处理帮助类
 * <p>Title:StringUtils</p>
 * <p>Description: </p>
 * @author halo.pan
 *
 * @date 2016年4月12日 下午1:36:24
 *
 */
public class StringUtils extends org.springframework.util.StringUtils{
	 
	/**
	 *  list转成字符串 以逗号隔开
	 * @param stringList
	 * @return
	 */
	public static String listToString(List<String> stringList,String regex){
	        if (stringList==null) {
	            return null;
	        }
	        StringBuilder result=new StringBuilder();
	        boolean flag=false;
	        for (String string : stringList) {
	            if (flag) {
	                result.append(regex);
	            }else {
	                flag=true;
	            }
	            result.append(string);
	        }
	        return result.toString();
	    }
	/**
	 * 将"&","<",">"转义
	 * @param text
	 * @return
	 */
	public static String replaceUtils(String text){	
		
		return text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
	/**
	 * 将 ' 
	 * @param text
	 * @return
	 */
	public static String escapeUtils(String text){	
		
		return text.replaceAll("\'", "\\\\\'").replaceAll("\\\\\\\\\'", "\\\\\'");
	}
	/**
	 * 将alibaba fastjson toJSONString后的 '转义
	 * @param text
	 * @return
	 */
	public static String escapeJsonUtils(String text){	
		
		return text.replaceAll("\'", "\\\\\'");
	}
	
}
