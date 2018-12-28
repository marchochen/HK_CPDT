package com.cwn.wizbank.utils;

import java.util.HashMap;  
import java.util.LinkedList;  
import java.util.List;  
import java.util.Map;  

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
  
public class Xml2MapUtil {  
    /** 
     * 将一个xml格式的字符串转换为map
     * @param xml xml格式的字符串 
     * @return 成功返回map;失败反回null 
     */  
    public static Map<String, Object> xml2Map(String xml) {  
        try {  
            Document doc = (Document) DocumentHelper.parseText(xml);  
            Element root = doc.getRootElement();  
            return iterateElement(root);  
        } catch (Exception e) {  
            CommonLog.error(e.getMessage(),e);
            return null;  
        }  
    }    
  
    /** 
     * 一个迭代方法 
     * @param element : org.jdom.Element 
     * @return java.util.Map 实例 
     */  
	@SuppressWarnings("unchecked")
	private static Map<String, Object> iterateElement(Element element) {  
        List<Element> jiedian = element.elements();  
        List<Attribute> shuxing = element.attributes();
        Element et = null;  
        Attribute atr = null;
        Map<String, Object> map = new HashMap<String, Object>();  
        List<Object> list = null;  
        for(int i=0;i<shuxing.size();i++){
        	atr = shuxing.get(i);
        	map.put(atr.getName(), atr.getValue());
        }
        for (int i = 0; i < jiedian.size(); i++) {
        	list = new LinkedList<Object>(); 
            et = (Element) jiedian.get(i);  
            if(map.containsKey(et.getName())){  
                list = (List<Object>)map.get(et.getName());  
            }  
            Map<String, Object> itet = new HashMap<String, Object>();
            if(et.elements().size() > 0 ||  et.attributes().size() > 0){
            	itet = iterateElement(et); 
            }
            if(!"null".equalsIgnoreCase(et.getTextTrim()) && !"".equalsIgnoreCase(et.getTextTrim())){
            	itet.put("text", et.getTextTrim());
            }
            list.add(itet);  
            map.put(et.getName(), list);  
        }  
        return map;  
    }  
 
}  