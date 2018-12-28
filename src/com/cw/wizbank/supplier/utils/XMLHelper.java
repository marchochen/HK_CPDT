/**
 * 
 */
package com.cw.wizbank.supplier.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cw.wizbank.util.cwUtils;

/**
 * @author Leon.li
 * 2013-5-28 4:54:27
 * message xml helper
 */
public class XMLHelper {

	/**
	 * Put obj into the XML
	 * 注意： 如果自定义对象实现了Comparable接口，则不能用此方法
	 * @param 
	 * @return
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static String javaBeanToXML(Object obj) throws IllegalArgumentException, IllegalAccessException{
		if(obj == null){
			return "";
		}
		Field[] fs = obj.getClass().getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		String objName = camel4underline(obj.getClass().getSimpleName());

		sb.append("<"+objName+">");
		for(Field f :fs) {
			f.setAccessible(true);
			String coulmn = camel4underline(f.getName());
			if(f.get(obj)!=null && !"".equals(f.get(obj))){
				if(Modifier.isFinal(f.getModifiers())) {
					continue;
				}
				if(!(f.get(obj) instanceof Comparable)){
					sb.append(javaBeanToXML(f.get(obj)));
				} else{
					sb.append("<"+coulmn+">")
					.append(cwUtils.esc4XML(f.get(obj)+""))
					.append("</"+coulmn+">");
				}
			}
		}
		sb.append("</"+objName+">");
		return sb.toString();
	}
	
	
	
	/**
	 * Find the letters and Numbers on the front and the underline
	 * @param param
	 * @return
	 */
	public static String camel4underline(String param){  
        Pattern  p=Pattern.compile("[A-Z]|(([1-9]\\d*\\.?\\d*)|(0\\.\\d*[1-9]))");  
        if(param==null ||param.equals("")){  
            return "";  
        }  
        StringBuilder builder=new StringBuilder(param);  
        Matcher mc=p.matcher(param);  
        int i=0;  
        while(mc.find()){  
            builder.replace(mc.start()+i, mc.end()+i, "_"+mc.group().toLowerCase());  
            i++;  
        }  
        if('_' == builder.charAt(0)){  
            builder.deleteCharAt(0);  
        }  
        return builder.toString();  
    }
	

}
