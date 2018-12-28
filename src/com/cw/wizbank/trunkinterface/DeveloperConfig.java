package com.cw.wizbank.trunkinterface;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class DeveloperConfig {

	public final static String DEVELOPER_ID_WEB = "WEB";
	public final static String DEVELOPER_ID_MOBILE = "MOBILE";
	public final static String DEVELOPER_ID_OFFLINE = "OFFLINE";
	

	public static int WEB_TIME_OUT = 0;
	public static int MOBILE_TIME_OUT = 0;
	public static int OFFLINE_TIME_OUT = 0;
	

	public List<String> VALIDE_DEVELOPER_ID = new Vector<String>();//有效的DEVELOPER_ID（在数据库表acSite的ste_developer_id字段配置）
	public HashMap<String, List<String>> DEVELOPER_FUNCTIONS = new HashMap<String, List<String>>();//每个DEVELOPER_ID包含的CMD（在文件developer.properties配置）

	@SuppressWarnings("unchecked")
	public void init (Connection con, String CfgFileDeveloperDir){
		try{
			//从数据库表acSite的ste_developer_id字段中获取有效的DEVELOPER_ID
			String ste_developer_id = acSite.getSteDeveloperId(con, 1);
			if(ste_developer_id != null && ste_developer_id.length() > 0){
				VALIDE_DEVELOPER_ID = cwUtils.splitToVecString(ste_developer_id, "~");
			}
			//从配置文件developer.properties中获取有效的CMD
			Properties props = new Properties();
			props.load(new FileInputStream (new File(CfgFileDeveloperDir)));
			String web_cmd_list = (String)props.get("web_cmd_list");
			String mobile_cmd_list = (String)props.get("mobile_cmd_list");
			String offline_cmd_list = (String)props.get("offline_cmd_list");
			
			if(web_cmd_list != null && web_cmd_list.length() >0){
				DEVELOPER_FUNCTIONS.put(DEVELOPER_ID_WEB, cwUtils.splitToVecString(web_cmd_list, ","));
			}
			if(mobile_cmd_list != null && mobile_cmd_list.length() >0){
				DEVELOPER_FUNCTIONS.put(DEVELOPER_ID_MOBILE, cwUtils.splitToVecString(mobile_cmd_list, ","));
			}
			if(offline_cmd_list != null && offline_cmd_list.length() >0){
				DEVELOPER_FUNCTIONS.put(DEVELOPER_ID_OFFLINE, cwUtils.splitToVecString(offline_cmd_list, ","));
			}
			
			String web_time_out = (String)props.get("web_time_out");
			String mobile_time_out = (String)props.get("mobile_time_out");
			String offline_time_out = (String)props.get("offline_time_out");
			
			if(web_time_out != null && web_time_out.length() >0){
				WEB_TIME_OUT = Integer.parseInt(web_time_out);
			}
			if(mobile_time_out != null && mobile_time_out.length() >0){
				MOBILE_TIME_OUT = Integer.parseInt(mobile_time_out);
			}
			if(offline_time_out != null && offline_time_out.length() >0){
				OFFLINE_TIME_OUT = Integer.parseInt(offline_time_out);
			}
			
		} catch (Exception e) {//这里catch全部Exception是因为不希望影响系统的整体运行
			CommonLog.error("Dispatcher init() error : DeveloperConfig.init().");
			CommonLog.error(e.getMessage(),e);
		}
	}
	
	public static int getDeveloperTimeOut(String developer_id){
		int time_out = 0;
		if(developer_id != null){
			if(developer_id.equalsIgnoreCase(DEVELOPER_ID_WEB)){
				time_out = WEB_TIME_OUT;
			} else if(developer_id.equalsIgnoreCase(DEVELOPER_ID_MOBILE)){
				time_out = MOBILE_TIME_OUT;
			} else if(developer_id.equalsIgnoreCase(DEVELOPER_ID_OFFLINE)){
				time_out = OFFLINE_TIME_OUT;
			}
		}
		return time_out;
	}

}
