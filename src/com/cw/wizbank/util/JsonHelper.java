package com.cw.wizbank.util;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

import com.cw.wizbank.JsonMod.commonBean.GoldenManBean;
import com.cw.wizbank.JsonMod.commonBean.OptionBean;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessorMatcher;

public class JsonHelper {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	public static class TimestampConverter implements Converter {
		public TimestampConverter() {
		}

		/*
		 * (non-Javadoc) @see org.apache.commons.beanutils.Converter#convert(java.lang.Class, java.lang.Object)
		 */
		public Object convert(Class type, Object value) throws ConversionException {
			if (value == null || value.toString().trim().length() == 0) {
				return null;
			}
			// Support Calendar and Timestamp conversion
			if (value instanceof String) {
			    if (cwUtils.IMMEDIATE.equalsIgnoreCase(value.toString())) {
			        return  Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
			    } else if (cwUtils.UNLIMITED.equalsIgnoreCase(value.toString())) {
			        return Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
			    } else {
			        return Timestamp.valueOf(value.toString());
			    }
			} else if (value instanceof Timestamp) {
				return value;
			} else if (value instanceof Date) {
				return new Timestamp(((Date) value).getTime());
			} else if (value instanceof Calendar) {
				return new Timestamp(((Calendar) value).getTime().getTime());
			} else {
				throw new ConversionException("Type not supported: " + value.getClass().getName());
			}
		}
	}
	
	public static JSONObject obj2Json(Object o, JsonConfig config) {
		JSONObject jsObj = new JSONObject();
		jsObj = JSONObject.fromObject(o, config);
		return jsObj;
	}

	/**
	 * 向前台输出json对象
	 * @param obj
	 * @param config
	 * @param pw
	 */
	public static void writeJson(Object obj, JsonConfig config, PrintWriter pw) {
		if (pw != null && obj != null)
			pw.write(JsonHelper.obj2Json(obj, config).toString());
	}

	/**
	 * 配置默认的json输出方式
	 * @param jsonConfig
	 */
	public static void setDefaultJsonconfig(JsonConfig jsonConfig) {
		jsonConfig.registerJsonValueProcessor(Timestamp.class, new WzbJsonValueProcessors.DateJsonValueProcessor());
		jsonConfig.registerJsonValueProcessor(Date.class, new WzbJsonValueProcessors.DateJsonValueProcessor());
		//escape "<","&" for json output.
		jsonConfig.registerJsonValueProcessor(String.class, new WzbJsonValueProcessors.DefaultEsc4HtmlStringProcessor());
		//disable html escape for goldenman html string.
		JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.commonBean.GoldenManBean.class, "value", jsonConfig);
		JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.commonBean.OptionBean.class, "text", jsonConfig);
//		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		jsonConfig.setDefaultValueProcessorMatcher(DefaultValueProcessorMatcher.DEFAULT);
		jsonConfig.setJsonPropertyFilter(new JsonPropertyFilter());
/*		jsonConfig.setJsonPropertyFilter( new PropertyFilter(){  //属性过滤
           public boolean apply( Object source, String name, Object value ) {     
              if(value==null){  
                 return true;  
              }  
              return false;  
           } 
		});*/
	}

	public static String toJsonDateFormat(Timestamp time) {
		if (time == null)
			return "";
		return time.toString();
		/*SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return format.format(time);*/
	}
	
	/**
	 * 输出json时不对html中的特殊字符做过滤"<","&".
	 * TODO 单独对某个属性或者某个key的设置.
	 * @param jsonConfig
	 */
	public static void disableEsc4JsonAll(JsonConfig jsonConfig) {
		if (jsonConfig.findJsonValueProcessor(String.class) != null) {
			jsonConfig.unregisterJsonValueProcessor(String.class);
		}
	}
	
	/**
	 * 设置某个bean中的对应的属性不做html字符的escape处理
	 * @param beanClass
	 * @param key
	 * @param jsonConfig
	 */
	public static void disableEsc4Json(Class beanClass, String propertyName, JsonConfig jsonConfig) {
		jsonConfig.registerJsonValueProcessor(beanClass, propertyName, new WzbJsonValueProcessors.NotEsc4HtmlStringProcessor());
	}
	
	/**
	 * 设置map中的某个属性不做html字符的escape处理,对property和hashmap的key都有效
	 * @param key
	 * @param jsonConfig
	 */
	public static void disableEsc4Json(String key, JsonConfig jsonConfig) {
		jsonConfig.registerJsonValueProcessor(key, new WzbJsonValueProcessors.NotEsc4HtmlStringProcessor());
	}
}
