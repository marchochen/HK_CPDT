/**
 * 
 */
package com.cwn.wizbank.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.IntegCompleteCondition;

/**
 * @author leon.li
 * 2014-8-29 下午3:45:25
 */
public class CwnUtil {
	
	  public static final String IMMEDIATE = "IMMEDIATE";
	  public static final long FORGET_PWD_KEY_1 = 92157862; 
	  public static final long FORGET_PWD_KEY_2 = 321; 
	  public static final String UNLIMITED = "UNLIMITED";  
	  public static final String MIN_TIMESTAMP = "1753-01-01 00:00:00";  
	  public static final String MAX_TIMESTAMP = "9999-12-31 23:59:59"; 
	  
	/**
	 * 获取课程类型
	 * 
	 * @param courseType
	 * @return
	 */
	public static String getCourseTypeStr(String courseType, String lang) {
		
		if (AeItem.CLASSROOM.equals(courseType)) {
			return LabelContent.get(lang, "classroom");
		} else if (AeItem.INTEGRATED.equals(courseType)) {
			return LabelContent.get(lang, "integrated");		
		} else if (AeItem.SELFSTUDY.equals(courseType)) {
			return LabelContent.get(lang, "selfstudy");	
		}
		return courseType;
	}
	
	public static String getExamTypeStr(String courseType, String lang) {
		
		if (AeItem.CLASSROOM.equals(courseType)) {
			return LabelContent.get(lang, "exam_classroom");
		} else if (AeItem.SELFSTUDY.equals(courseType)) {
			return LabelContent.get(lang, "exam_selfstudy");	
		}
		return courseType;
	}
	
	
	/**
	 * 	status_inprogress : '进行中',
	 *  status_completed : '已完成',
	 *  status_admitted : '已报名',
	 *  status_pending : '审批中',
	 *  status_waiting ： '等待队列'
	 * @param appStatus
	 * @param curLang
	 * @return
	 */
	public static String getAppStatusStr(String appStatus, String curLang){
		if(StringUtils.isEmpty(appStatus)) return "";
		appStatus = appStatus.toLowerCase();
		if("i".equals(appStatus)) {
			return LabelContent.get(curLang, "status_inprogress");	//进行中
		} else if("c".equals(appStatus)) {
			return LabelContent.get(curLang, "status_completed");	//已完成
		} else if("f".equals(appStatus)){
			return LabelContent.get(curLang, "status_fail");	//fail,未完成
		} else if("p".equals(appStatus)){
			return LabelContent.get(curLang, "status_pass");	//pass,已通过
		} else if("w".equals(appStatus)){
			return LabelContent.get(curLang, "status_withdrawn");	//已放弃
		} else if("notapp".equals(appStatus)) {
			return LabelContent.get(curLang, "status_notapp");	//未报名
		} else if("pending".equals(appStatus)) {
			return LabelContent.get(curLang, "status_pending");	//审批中
		} else if("admitted".equals(appStatus)) {
			return LabelContent.get(curLang, "status_admitted");	//已报名
		}else if("waiting".equals(appStatus)) {
			return LabelContent.get(curLang, "status_waiting");	//等待队列
		}
		return LabelContent.get(curLang, "status_notapp");
	}
	
	
	/**
	 * 获取是否必修选修的
	 * @param icdType
	 * @param curLang
	 * @return
	 */
	public static String getCompulsoryStr(String icdType, String curLang){
		if(StringUtils.isEmpty(icdType)) return "";
		if(IntegCompleteCondition.COMPULSORY.equals(icdType)) {
			return LabelContent.get(curLang, "condition_required");
		} else if(IntegCompleteCondition.ELECTIVE.equals(icdType)) {
			return LabelContent.get(curLang, "condition_elective");
		} 
		return "";
	}
	
	 public static boolean isMaxTimestamp(Timestamp ts) {
	      if(ts.equals(Timestamp.valueOf(MAX_TIMESTAMP)))
	          return true;
	      else
	          return false;
	 }

	 
	 /**
		 * 按指定的标准对double类型的变量进行格式化(小数部分四舍五入)
		 *
		 * @param number 需要转换的数值
		 * @param scale 小数部份最大保留位数
		 * @return 格式化后的数值字符串
		 */
		public static double formatNumber(double number, int scale) {
			BigDecimal bd = new BigDecimal(number);
			if (scale < 0) {
				scale = 0;
			}
			return Double.valueOf(bd.setScale(scale, BigDecimal.ROUND_HALF_UP).toString());
		}
		
		public static double formatNumber(double number, int scale , int roundingMode) {
			BigDecimal bd = new BigDecimal(number);
			if (scale < 0) {
				scale = 0;
			}
			return Double.valueOf(bd.setScale(scale, roundingMode).toString());
		}
		
		public static float formatNumber(float number, int scale) {
			BigDecimal bd = new BigDecimal(number);
			if (scale < 0) {
				scale = 0;
			}
			return Float.valueOf(bd.setScale(scale, BigDecimal.ROUND_HALF_UP).toString());
		}
		
		public static String file2String(File file) {
			String result = "";
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));// 构造一个BufferedReader类来读取文件
				String s = null;
				while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
					result = result + "\n" + s;
				}
				br.close();
			} catch (Exception e) {
				CommonLog.error(e.getMessage(),e);
			}
			return result;
		}
		
		
		// default not trim when split
	    public static String[] splitToString(String in, String delimiter) {
	        return splitToString(in, delimiter, false);
	    }
	    public static String[] splitToString(String in, String delimiter, boolean bTrim) {

	        String obj[] = null;
	        if( in == null || in.length() == 0 ) {
	            obj = new String[0];
	            return obj;
	        }

	        Vector q = new Vector();
	        int pos =0;
	        pos = in.indexOf(delimiter);

	        while (pos >= 0) {
	            String val = new String();
	            if (pos>0) {
	                val = in.substring(0,pos);
	            }
	            q.addElement(val);
	            in = in.substring(pos + delimiter.length(), in.length());
	            pos = in.indexOf(delimiter);
	        }

	        if (in.length() > 0) {
	            q.addElement(in);
	        }

	        obj = new String[q.size()];
	        for (int i=0; i<obj.length;i++) {
	            obj[i] = (String)q.elementAt(i);
	            if (bTrim){
	                obj[i] = obj[i].trim();
	            }
	        }

	        return obj;
	    }
	    
	    public static String getIpAddr(HttpServletRequest request) {

	    	String ip = request.getHeader("x-forwarded-for");

	    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    		ip = request.getHeader("Proxy-Client-IP");
	    	}

	    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	    		ip = request.getHeader("WL-Proxy-Client-IP");
	    	}

	    	if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {

	    		ip = request.getRemoteAddr();
	    	}
	    	return ip;
	   }
}
