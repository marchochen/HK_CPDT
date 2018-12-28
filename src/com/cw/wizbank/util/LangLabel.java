package com.cw.wizbank.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import net.sf.json.JSONObject;

import com.cw.wizbank.upload.ImportTemplate;
import com.cwn.wizbank.utils.CommonLog;
/**
 * this class loads all UI labels into memory and provide access method to
 * retrieve label value from label name
 * (kawai 2004-09-27)
 */
public class LangLabel {
	public static final String LabelFileEncoding = "UTF8";
    //public static final String LabelDirName = "label";
    public static final String LabelDirName = "js";
    public static final String LabelFileNamePrefix = "label_";
    //public static final String LabelFileNameSubfix = ".txt";
    public static final String LabelFileNameSubfix = ".js";
    
    public static final String Encoding_en_us = "ISO-8859-1";
    public static final String Encoding_zh_cn = "GB2312";
    public static final String Encoding_zh_hk = "Big5";
    public static final String LangCode_en_us = "en-us";
    public static final String LangCode_zh_cn = "zh-cn";
    public static final String LangCode_zh_hk = "zh-hk";
    
    
    public static HashMap allLangLabel;
    public static ArrayList allLangEncoding;
    public static Properties labelName;
    
    public static void init(File webRootDir, PrintWriter out) throws IOException {
    	allLangLabel = new HashMap();
        allLangEncoding = new ArrayList();
        
        String BR = "<br>";
	    String PRE1 = "<pre>";
	    String PRE2 = "</pre>";
	    if(out == null){
	      out = new PrintWriter(System.out, true);
	      BR = "";
	      PRE1 = "";
	      PRE2 = "";
	     }
        
        File labelDirObj = new File(webRootDir, LabelDirName);
        File[] labelFileList = labelDirObj.listFiles();
        
        for (int i = 0; labelFileList != null && i < labelFileList.length; i++) {
        	//判断文件格式
        	if (labelFileList[i].isHidden()
            || !labelFileList[i].isFile()
            || !labelFileList[i].getName().startsWith(LabelFileNamePrefix)
            || !labelFileList[i].getName().endsWith(LabelFileNameSubfix)
            || labelFileList[i].getName().equals(LabelFileNamePrefix + LabelFileNameSubfix)) {
                continue;
            }
            
            BufferedReader in = null;
            try {
                //获取label的语言
            	String labelFileLang = labelFileList[i].getName().substring(LabelFileNamePrefix.length(), labelFileList[i].getName().length() - LabelFileNameSubfix.length());
            	CommonLog.info("Loading label file:" + labelFileList[i].getAbsolutePath());
                // second level hashmap for language specific labels
                HashMap curLangLabel = new HashMap();
                //读取文件
                in = new BufferedReader(new InputStreamReader(cwUtils.openUTF8FileStream(labelFileList[i]), LabelFileEncoding));
                //统计label数量
                int labCnt = 0;
                String inLine = null;
                StringBuffer buf = new StringBuffer();
                // a line whose first non-whitespace character is '#' is skipped
                // a line without any equal sign is skipped
                // take the first occurrence of equal sign as name-value separator
                // not to do any space trimming to leave any space as part of the name or the value
                //把文件转换为字符串
                while ((inLine = in.readLine()) != null) {
                    buf.append(inLine);
                }
                
                String labels = new String(buf.toString());
        		//转换为json格式
                labels = labels.substring(labels.indexOf("{"));
                //转换为json对象
        		JSONObject obj = JSONObject.fromObject(labels);
        		Iterator itr = obj.keys();
        		
        		while(itr.hasNext()) {
        			//label的名字
        			String labName = (String) itr.next();
        			//label的值
        			try{
	        			String labValue = (String)obj.get(labName);
	        			if (curLangLabel.containsKey(labName)) {
	                        out.println("Label duplicated:" + labName + ". Its previous value will be overwritten." + BR);
	                    } else {
	                        labCnt++;
	                    }
	                    curLangLabel.put(labName, labValue);
        			} catch(Exception e) {
        				CommonLog.error(" label 有重复：" + labName);
        			}
        		}
                allLangLabel.put(labelFileLang, curLangLabel);
                
                // map the language to encoding as there are cases that encoding is used to query labels 
                String thisEncoding = null;
                if (labelFileLang.equalsIgnoreCase(LangCode_en_us)) {
                    thisEncoding = Encoding_en_us;
                }
                else
                if (labelFileLang.equalsIgnoreCase(LangCode_zh_cn)) {
                    thisEncoding = Encoding_zh_cn;
                }
                else
                if (labelFileLang.equalsIgnoreCase(LangCode_zh_hk)) {
                    thisEncoding = Encoding_zh_hk;
                }
                allLangLabel.put(thisEncoding, curLangLabel);
                allLangEncoding.add(thisEncoding);
                
                out.println("No. of labels loaded:" + labCnt + BR);
            } catch (UnsupportedEncodingException e) {
            	out.println("Unknown encoding:" + e.getMessage() + BR);
            	out.println("Label file skipped:" + labelFileList[i].getAbsolutePath() + BR);
          } catch (FileNotFoundException e) {
              	out.println(e.getMessage() + BR);
              	out.println("Label file skipped:" + labelFileList[i].getAbsolutePath() + BR);
          } catch (Exception e) {
              	out.println(e.getMessage() + BR);
              	out.println("Label file skipped:" + labelFileList[i].getAbsolutePath() + BR);
              	out.println(PRE1);
              	e.printStackTrace(out);
              	CommonLog.error(e.getMessage(),e);
              	out.println(PRE2);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
    }
    
    public static String getValue(String inLang, String inName) {
        String outValue = null;
        
        if (allLangLabel != null) {
            HashMap curLangLabel = (HashMap) allLangLabel.get(inLang);
            if (curLangLabel != null) {
                outValue = (String) curLangLabel.get(inName);
            }
        }
        if (outValue == null) {
            StringBuffer result = new StringBuffer();
            result.append("!!!").append(inLang).append(".").append(inName);
            outValue = result.toString();
        }
        
     //   System.out.println("LangLabel key : " + inName + " value: " + outValue);
        
        return outValue;
    }
    
    public static void reload(File webRootDir, PrintWriter out) throws IOException {
        if (allLangLabel != null) {
            Set allLang = allLangLabel.keySet();
            for (Iterator itr = allLang.iterator();itr.hasNext();) {
                ((HashMap) allLangLabel.get(itr.next())).clear();
            }
            allLangLabel.clear();
        }
        init(webRootDir, out);
    }
    
    public static void reload(File inTXTDir, File webRootDir, File webConfigDir, PrintWriter out, Connection conn) throws IOException {
    	LangLabel.reload(webRootDir, out);

    	ImportTemplate impTem = new ImportTemplate();
    	impTem.rebuild(inTXTDir, webRootDir, webConfigDir, out, conn);
    }
    
	public static void init(File inTXTDir, File webRootDir, File webConfigDir, PrintWriter out, Connection conn) throws IOException {
		init(webRootDir,out);
		loadCustomizedRoleTitle(conn);
		ImportTemplate impTem = new ImportTemplate();
		impTem.genTplFile(inTXTDir, webRootDir, webConfigDir, out, conn);
	}

	private static final String sql_get_all_customized_role = "select rol_ext_id,rol_title from acRole where rol_type = 'NORMAL' and rol_status = 'OK'";
	/**
	 * load customized role title into langlabel.
	 * @param conn
	 * @throws SQLException
	 */
	public static void loadCustomizedRoleTitle(Connection conn) {
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    try {
	        pstmt = conn.prepareStatement(sql_get_all_customized_role);
	        rs = pstmt.executeQuery();
	        while (rs.next()) {
	            String rol_ext_id = rs.getString("rol_ext_id");
	            String rol_title = rs.getString("rol_title");
	            setCustomizedRoleTitle(rol_ext_id, rol_title);
	        }
	    } catch (SQLException e) {
            CommonLog.error(e.getMessage(),e);
        } finally {
	        if (pstmt != null)
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    CommonLog.error(e.getMessage(),e);
                }
	    }
	}
	
	public static void setCustomizedRoleTitle(String rol_ext_id, String rol_title) {
	    setLangLabel("lab_rol_" + rol_ext_id, rol_title);
	}
	/**
	 * set label with specified key:value. use same string for all language.
	 * @param key
	 * @param value
	 */
	public static void setLangLabel(String key, String value) {
	    for (int i = 0; i < allLangEncoding.size(); i++) {
            ((HashMap) allLangLabel.get(allLangEncoding.get(i))).put(key, value);
        }
	}
}
