package com.cwn.wizbank.utils;

/**
 * 操作字符工具类
 * @author andrew.xiao
 *
 */
public class CharUtils {
	
	/**
	 * 判断字段真实长度（中文2个字符，英文1个字符）
	 * @param value
	 * @return
	 */
	public static int getStringLength(String value) {
		if(value == null){
			return 0;
		}
		int valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}
	
	/**
	 * 获取0--@param len的字符，其余用@param decoretor填充
	 * 
	 * @param source
	 * @param len
	 * @param decoretor 默认是...
	 * @return
	 */
	public static String subStringWithDecoretor(String source,int len,String decoretor){
		
		if(source == null || source.length() == 0){
			return "";
		}
		
		int length = getStringLength(source);
		
		if(length<=len){
			return source;
		}
		
		String result = "";
		char[] charArray = source.toCharArray();
		
		int limitLen = 0;
		
		for(char c : charArray){
			String cStr = String.valueOf(c);
			int cLen = getStringLength(cStr);
			limitLen += cLen;
			result += cStr;
			if(limitLen >= len){
				break;
			}
		}
		
		if(getStringLength(result) > len){
			result = result.substring(0, result.length()-1);
		}
		
		if(decoretor==null){
			result += "...";
		}else{
			result += decoretor;
		}
		
		return result;
	}
	
	/**
	 * 过滤掉html字符（注意：&lt;&gt;类的标签也会被去掉）
	 * @param in
	 * @return
	 */
	public static String filterHTML(String in) {
		in = in.replaceAll("&lt;", "<");   
		in = in.replaceAll("&gt;", ">");
		in = in.replaceAll("</?[^>]+>","");
		in = in.replace("&nbsp;",""); 
		in = in.replace("\n",""); 
		in = in.replace("\r","");
		in = in.replace("\"","‘");
		in = in.replace("'","‘");
		return in;
    }
}
