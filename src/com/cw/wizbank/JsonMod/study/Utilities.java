package com.cw.wizbank.JsonMod.study;

import java.text.DecimalFormat;

/**
 * 实用工具类
 * @author kimyu
 */
public class Utilities {
	
	/**
	 * 解析秒数为时间格式
	 * 例如：将202.2秒可以解析为"00:03:22"这样的格式
	 * @param src 秒数
	 * @return 预设的时间格式
	 */
	public static String parseSecond2Time(double src) {
		
		int hour = (int)(src / 3600);
		int minute = (int)((src - hour * 3600) / 60);
		int second = (int)(src - hour * 3600 - minute * 60);
		
		DecimalFormat df = new DecimalFormat("00");

		return df.format(hour) + ":" + df.format(minute) + ":" + df.format(second);
	}

	/**
	 * 过滤的字符串
	 * @param inputStr 待过滤的某个字符串
	 * @return 过滤后的字符串
	 */
	public static String filter(String inputStr) {
		if (!hasSpecialChars(inputStr)) {	// 若待过滤的字符串中没有非法字符，则直接返回该字符串
			return inputStr;
		}
		
		StringBuffer filteredStr = new StringBuffer(inputStr.length());
		char ch;
		for (int i = 0; i < inputStr.length(); i++) {
			ch = inputStr.charAt(i);
			switch(ch) {
				case '<': filteredStr.append("&lt;");
						  break;
				case '>': filteredStr.append("&gt;");
				  		  break;
				case '"': filteredStr.append("&quot;");
						  break;
				case '&': filteredStr.append("&amp;");
				          break;
				default: filteredStr.append(ch);
			} // end switch
			
		} // end for
		
		return filteredStr.toString();
	}
	
	/**
	 * 判断是否有需要过滤的字符
	 * @param inputStr 待过滤的某个字符串
	 * @return 是否有需要过滤的字符
	 */
	private static boolean hasSpecialChars(String inputStr) {
		boolean flag = false;
		
		if ((inputStr != null) && (inputStr.length() > 0)) {
			char ch;
			
			for (int i = 0; i < inputStr.length(); i++) {
				ch = inputStr.charAt(i);
				
				switch(ch) {
					case '<': flag = true;
							  break;
					case '>': flag = true;
							  break;
					case '"': flag = true;
							  break;
					case '&': flag = true;
					          break;
				} // end switch
				
			} // end for
		}
		
		return flag;
	}
}
