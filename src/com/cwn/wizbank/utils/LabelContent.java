/**
 * 
 */
package com.cwn.wizbank.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加载所有的label
 * @see get
 * @author leon.li 2014-7-29 上午10:32:16
 */
public class LabelContent {
	
	public static Logger logger = LoggerFactory.getLogger(LabelContent.class);
	
	public static final String FileEncoding = "UTF8";
	public static final String FileNamePrefix = "label_";
	public static final String FileNameSubfix = ".js";

	public static final String Encoding_en_us = "ISO-8859-1";
	public static final String Encoding_zh_cn = "GB2312";
	public static final String Encoding_zh_hk = "Big5";
	public static final String LangCode_en_us = "en-us";
	public static final String LangCode_zh_cn = "zh-cn";
	public static final String LangCode_zh_hk = "zh-hk";
	
//	private static String currentLang;		//记录当前语言
	
	private static String labelDirName;
	private static Map<String, Map<String, String>> allLabels;
	private static Map<String, String> curLabels;
	
	public LabelContent(String dirPath) throws Exception {
		labelDirName = dirPath;
		try {
			initLabel();
			initLabelV2();
		} catch (IOException e) {
			CommonLog.error(e.getMessage(),e);
		}
	}
	
	/**
	 * this function reads labels from i18n 
	 * it stores values by map 
	 * the key is filename
	 * eg. label_rm_en-us
	 * 		
	 */
	public void initLabelV2() throws Exception
	{
	 
		//File dir = new File(labelDirName);getClass()
		File dir = new File(labelDirName);
		logger.debug("【init label】label 文件夹的路径为：" + labelDirName);
		if(!dir.exists()){
			throw new FileNotFoundException("【init label】label 文件夹没有找到");
		}
		File[] files = dir.listFiles();
		if (files == null) {
			// 找不到文件
			throw new FileNotFoundException("【init label】LabelContent : initLabel 不能找到国际化的label");
		} else {
			//allLabels = new HashMap<String, Map<String, String>>();
			
			// 读取文件
			int count = 0;	//统计数量
			for (File f : files) {
				
				
				if(f.isHidden())
				{
					continue;
				}
				
				if(f.isDirectory())
				{
					for(File detailFile : f.listFiles())
					{
						if(detailFile.isHidden())
						{
							continue;
						}
						String lang = getCurrentLang(detailFile.getName());	//当前语言
						String labelStr = "";
						curLabels = new LinkedHashMap<String,String>();
						//读取label文件成string
						labelStr = FileUtils.readFileToString(detailFile, "utf-8");
						labelStr = labelStr.substring(labelStr.indexOf("{"), labelStr.indexOf("};") + 1);
						JsonConfig config = new JsonConfig();
						JSONObject obj = JSONObject.fromObject(labelStr, config);

						Iterator<?> it = obj.keys();
						while (it.hasNext()) {
							String key = ((String) it.next()).trim();
							String value = obj.getString(key);
							if(value == null || "".equals(value)) continue;
							
							if(obj.get(key) instanceof JSONArray) {
								throw new Exception("【label ：" + key + " 】   " + value + "有重复记录");
							} else {
								curLabels.put(key, value);
								++count;
							}
						}
						//如果map存在就put到后面
						
						
						String keyName = detailFile.getName().split(".js")[0];
						
						if(!allLabels.containsKey(keyName)){
							allLabels.put(keyName, curLabels);					
						}else{
							Map<String, String> map = allLabels.get(lang);
							map.putAll(curLabels);
						}
						logger.debug("【init label】 成功加载文件： " + f.getName());
					}
				}
	
			}
			//System.out.println("【init label】 总计： " + count);
			logger.debug("【init label】 总计： " + count);
		}
	}
	
	
	/**
	 * 初始化label
	 * @return
	 * @throws Exception 
	 */
	public void initLabel() throws Exception {
		File dir = new File(labelDirName);
		logger.debug("【init label】label 文件夹的路径为：" + labelDirName);
		if(!dir.exists()){
			throw new FileNotFoundException("【init label】label 文件夹没有找到");
		}
		File[] files = dir.listFiles();
		if (files == null) {
			// 找不到文件
			throw new FileNotFoundException("【init label】LabelContent : initLabel 不能找到国际化的label");
		} else {
			allLabels = new HashMap<String, Map<String, String>>();
			
			// 读取文件
			int count = 0;	//统计数量
			for (File f : files) {
				
				if(f.isHidden()){
					continue;
				}
				
				if(f.isDirectory())
				{
					for(File detailFiles : f.listFiles())
					{
						if(detailFiles.isHidden())
						{
							continue;
						}
						String lang = getCurrentLang(detailFiles.getName());	//当前语言
						String labelStr = "";
						curLabels = new LinkedHashMap<String,String>();
						//读取label文件成string
						labelStr = FileUtils.readFileToString(detailFiles, "utf-8");
						labelStr = labelStr.substring(labelStr.indexOf("{"), labelStr.indexOf("};") + 1);
						JsonConfig config = new JsonConfig();
						JSONObject obj = JSONObject.fromObject(labelStr, config);

						Iterator<?> it = obj.keys();
						while (it.hasNext()) {
							String key = ((String) it.next()).trim();
							String value = obj.getString(key);
							if(value == null || "".equals(value)) continue;
							
							if(obj.get(key) instanceof JSONArray) {
								throw new Exception("【label ：" + key + " 】   " + value + "有重复记录");
							} else {
								curLabels.put(key, value);
								++count;
							}
						}
						//如果map存在就put到后面
						if(!allLabels.containsKey(lang)){
							allLabels.put(lang, curLabels);					
						}else{
							Map<String, String> map = allLabels.get(lang);
							map.putAll(curLabels);
						}
						logger.debug("【init label】 成功加载文件： " + detailFiles.getName());
					}
				}
			}
			//System.out.println("【init label】 总计： " + count);
			logger.debug("【init label】 总计： " + count);
		}
	}
	
	/**
	 * 获取当前文件的语言
	 * @param name
	 * @return
	 */
	public String getCurrentLang(String fileName){
		String defaultEncoding = LangCode_zh_cn;
		if(fileName.indexOf(LangCode_zh_cn) > -1){
			return defaultEncoding;
		} else if(fileName.indexOf(LangCode_zh_hk) > -1){
			return LangCode_zh_hk;
		} else if(fileName.indexOf(LangCode_en_us) > -1){
			return LangCode_en_us;
		}
		return defaultEncoding;
	}
	
	public static String getCurrentEncoding(String curLang){
		if(LangCode_zh_cn.equals(curLang)) {
			return Encoding_zh_cn;
		} else if(LangCode_zh_hk.equals(curLang)){
			return Encoding_zh_hk;
		} else if(LangCode_en_us.equals(curLang)){
			return Encoding_en_us;
		}
		return null;
	}
	
	/**
	 * 获取label
	 * @param curLang
	 * @param key
	 * @return
	 */
	public static String get(String curLang, String key){
        String value = null;
        if (allLabels != null) {
        	
        	
        	String[] module = key.split("\\.");
        	
        	HashMap<String,String> curLangLabel = null;
        	
        	if(module != null   && module.length==2)
        	{
        		curLangLabel = (HashMap<String, String>) allLabels.get(module[0] + "_" + curLang);
        		key = module[1];
        	}
        	else
        	{
        		curLangLabel = (HashMap<String,String>) allLabels.get(curLang);
        	}
        	
            if (curLangLabel != null) {
                value = (String) curLangLabel.get(key);
            }
             
               
        }
        if (value == null) {
            StringBuffer result = new StringBuffer();
            result.append("!!!").append(curLang).append(".").append(key);
            value = result.toString();
        }
        //logger.debug("key : " + key + ">>  value : " + value);
        
        
      // System.out.println("curLang:" + curLang +"; key: " + key +"; value :" + value);
        
        return value;
		
	}

	
	

	public static void main(String[] args) {
		//String path = "E:\\projects\\wizbank\\trunk\\www\\static\\js\\i18n";
		//LabelContent lc = new LabelContent(path);
		//LabelContent.get("wizbank_version", Encoding_en_us);
		String path = System.getProperty("user.dir");
		System.out.println(path);
	}
	
}
