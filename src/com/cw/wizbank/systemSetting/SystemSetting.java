package com.cw.wizbank.systemSetting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.Application;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class SystemSetting {
	
	public static SystemSettingReqParam param_config = null;
	
	public String getAsXml(Connection con,String type,WizbiniLoader wizbini,loginProfile prof) throws qdbException, cwSysMessage, SQLException {
		SystemSettingReqParam config = get(con);

		String xml = "<config>";
		
		if("sys".equalsIgnoreCase(type)){
			// Mail Server
			xml += "<mail>";
			xml += "<mail_server_host>" + cwUtils.esc4XML(config.mail_server_host) + "</mail_server_host>";
			xml += "<mail_server_auth_enabled>" + config.mail_server_auth_enabled + "</mail_server_auth_enabled>";
			xml += "<mail_server_account_type>" + config.mail_server_account_type + "</mail_server_account_type>";
			xml += "<mail_server_user>" + cwUtils.esc4XML(config.mail_server_user) + "</mail_server_user>";
			xml += "<mail_server_password>" + cwUtils.esc4XML(config.mail_server_password) + "</mail_server_password>";
			xml += "<mail_scheduler_domain>" + cwUtils.esc4XML(config.mail_scheduler_domain) + "</mail_scheduler_domain>";
			xml += "</mail>";
			// OpenOffice
			xml += "<openoffice>";
			xml += "<openoffice_enabled>" + cwUtils.esc4XML(String.valueOf(config.openoffice_enabled)) + "</openoffice_enabled>";
			xml += "<openoffice_environment>" + cwUtils.esc4XML(config.openoffice_environment) + "</openoffice_environment>";
			xml += "<openoffice_path>" + cwUtils.esc4XML(config.openoffice_path) + "</openoffice_path>";
			xml += "<openoffice_host>" + cwUtils.esc4XML(config.openoffice_host) + "</openoffice_host>";
			xml += "<openoffice_port>" + cwUtils.esc4XML(String.valueOf(config.openoffice_port)) + "</openoffice_port>";
			xml += "</openoffice>";
			// LDAP
			xml += "<ldap>";
			xml += "<ldap_url>" + cwUtils.esc4XML(config.ldap_url) + "</ldap_url>";
			xml += "<ldap_suffix>" + cwUtils.esc4XML(config.ldap_suffix) + "</ldap_suffix>";
			xml += "</ldap>";
			
			// weTeam
			xml += "<weTeam>";
			xml += "<url>" + cwUtils.esc4XML(config.webTeam_url) + "</url>";
			xml += "</weTeam>";
			
			// upload
			xml += "<upload>";
			xml += "<forbidden>" + cwUtils.esc4XML(config.upload_forbidden) + "</forbidden>";
			xml += "</upload>";
			
			// i_doc_view
			xml += "<i_doc_view>";
			xml += "<host>" + cwUtils.esc4XML(config.i_doc_view_host) + "</host>";
			xml += "<preview_host>" + cwUtils.esc4XML(config.i_doc_view_preview_host) + "</preview_host>";
			xml += "<token>" + cwUtils.esc4XML(config.i_doc_view_token) + "</token>";
			xml += "<preview_IP>" + cwUtils.esc4XML(config.i_doc_view_preview_IP) + "</preview_IP>";
			xml += "</i_doc_view>";
			
			//密码安全策略
			int maxLength=((UserManagement)wizbini.cfgOrgUserManagement.get(prof.root_id)).getUserProfile().getProfileAttributes().getPassword().getMaxLength();
			int minLength=((UserManagement)wizbini.cfgOrgUserManagement.get(prof.root_id)).getUserProfile().getProfileAttributes().getPassword().getMinLength();
			xml += "<password_policy maxLength=\""+maxLength+"\" minLength=\""+minLength+"\">";
			xml += "<period force=\""+config.password_policy_period_force+"\">"+config.password_policy_period+"</period>";
			xml += "<compare_count>"+config.password_policy_compare_count+"</compare_count>";
			xml += "</password_policy>";
			
			//默认密码
			xml += "<default_user_password>";
			xml += "<user_password>"+config.system_default_user_password+"</user_password>";
			xml += "</default_user_password>";
		}else{
			// WeChat
			xml += "<wechat>";
			xml += "<wechat_domain>" + cwUtils.esc4XML(config.wechat_domain) + "</wechat_domain>";
			xml += "<wechat_mobile_domain>" + cwUtils.esc4XML(config.wechat_mobile_domain) + "</wechat_mobile_domain>";
			xml += "<wechat_server_id>" + cwUtils.esc4XML(config.wechat_server_id) + "</wechat_server_id>";
			xml += "<wechat_port>" + cwUtils.esc4XML(String.valueOf(config.wechat_port)) + "</wechat_port>";
			xml += "<wechat_max_message>" + cwUtils.esc4XML(String.valueOf(config.wechat_max_message)) + "</wechat_max_message>";
			xml += "<wechat_trial_account_enabled>" + cwUtils.esc4XML(String.valueOf(config.wechat_trial_account_enabled)) + "</wechat_trial_account_enabled>";
			xml += "<wechat_menu>" + cwUtils.esc4XML(config.wechat_menu) + "</wechat_menu>";
			xml += "<wechat_token_url>" + cwUtils.esc4XML(config.wechat_token_url) + "</wechat_token_url>";
			xml += "</wechat>";
		}
		
		xml += "</config>";
		return xml;
	}

	public SystemSettingReqParam get(Connection con) throws qdbException, cwSysMessage, SQLException {
		SystemSettingReqParam param_config = new SystemSettingReqParam();

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {

			String sys_cfg_type = null;
			String sys_cfg_value = null;

			String sql = " select sys_cfg_value, sys_cfg_type from systemsetting ";

			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				sys_cfg_type = rs.getString("sys_cfg_type");
				sys_cfg_value = rs.getString("sys_cfg_value");

				// Mail Server
				if (equals(sys_cfg_type, "SYS_MAIL_SERVER_HOST")) {
					param_config.mail_server_host = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_MAIL_SERVER_AUTH_ENABLED")) {
					param_config.mail_server_auth_enabled = Boolean.valueOf(sys_cfg_value);
				} else if (equals(sys_cfg_type, "SYS_MAIL_SERVER_ACCOUNT_TYPE")) {
					param_config.mail_server_account_type = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_MAIL_SERVER_USER")) {
					param_config.mail_server_user = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_MAIL_SERVER_PASSWORD")) {
					param_config.mail_server_password = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_MAIL_SCHEDULER_DOMAIN")) {
					param_config.mail_scheduler_domain = sys_cfg_value;
				}
				
				else if (equals(sys_cfg_type, "WETEAM_URL")) {
					param_config.webTeam_url = sys_cfg_value;
				}
				// WECHAT
				else if (equals(sys_cfg_type, "SYS_WECHAT_DOMAIN")) {
					param_config.wechat_domain = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_WECHAT_MOBILE_DOMAIN")) {
					param_config.wechat_mobile_domain = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_WECHAT_SERVER_ID")) {
					param_config.wechat_server_id = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_WECHAT_PORT")) {
					param_config.wechat_port = Integer.valueOf(sys_cfg_value);
				} else if (equals(sys_cfg_type, "SYS_WECHAT_MAX_MESSAGE")) {
					param_config.wechat_max_message = Integer.valueOf(sys_cfg_value);
				} else if (equals(sys_cfg_type, "SYS_WECHAT_TRIAL_ACCOUNT_ENABLED")) {
					param_config.wechat_trial_account_enabled = Boolean.valueOf(sys_cfg_value);
				}else if (equals(sys_cfg_type, "WECHAT_TOKEN_URL")) {
					param_config.wechat_token_url = sys_cfg_value;
				} else if (equals(sys_cfg_type, "WECHAT_MENU")) {
					param_config.wechat_menu = sys_cfg_value;
				} 
				// LDAP
				else if (equals(sys_cfg_type, "SYS_LDAP_URL")) {
					param_config.ldap_url = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_LDAP_SUFFIX")) {
					param_config.ldap_suffix = sys_cfg_value;
				}
				// OpenOffice
				else if (equals(sys_cfg_type, "SYS_OPENOFFICE_ENABLED")) {
					param_config.openoffice_enabled = Boolean.valueOf(sys_cfg_value);
				} else if (equals(sys_cfg_type, "SYS_OPENOFFICE_ENVIRONMENT")) {
					param_config.openoffice_environment = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_OPENOFFICE_PATH")) {
					param_config.openoffice_path = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_OPENOFFICE_HOST")) {
					param_config.openoffice_host = sys_cfg_value;
				} else if (equals(sys_cfg_type, "SYS_OPENOFFICE_PORT")) {
					param_config.openoffice_port = Integer.valueOf(sys_cfg_value);
				}
				
				//upload
				else if(equals(sys_cfg_type, "SYS_UPLOAD_FORBIDDEN")){
					param_config.upload_forbidden = sys_cfg_value;
				}
				
				
				else if(equals(sys_cfg_type, "SYS_I_DOC_VEIW_HOST")){
					param_config.i_doc_view_host = sys_cfg_value;
				}
				
				else if(equals(sys_cfg_type, "SYS_I_DOC_VIEW_PREVIEW_FILE_HOST")){
					param_config.i_doc_view_preview_host = sys_cfg_value;
				}
				else if(equals(sys_cfg_type, "SYS_I_DOC_VIEW_TOKEN")){
					param_config.i_doc_view_token = sys_cfg_value;
				}
				else if(equals(sys_cfg_type, "SYS_I_DOC_VIEW_PREVIEW_IP")){
					param_config.i_doc_view_preview_IP = sys_cfg_value;
				}
				else if(equals(sys_cfg_type, "MULTIPLE_LOGIN_IND")){
					param_config.multiple_login_ind = sys_cfg_value;
				}
				
				//密码安全策略
				else if(equals(sys_cfg_type, "PASSWORD_POLICY_PERIOD_FORCE")){
					param_config.password_policy_period_force = sys_cfg_value;
				}
				else if(equals(sys_cfg_type, "PASSWORD_POLICY_PERIOD")){
					param_config.password_policy_period = sys_cfg_value;
				}
				else if(equals(sys_cfg_type, "PASSWORD_POLICY_COMPARE_COUNT")){
					param_config.password_policy_compare_count = sys_cfg_value;
				}
				//默认密码
				else if(equals(sys_cfg_type, "SYS_DEFAULT_USER_PASSWORD")){
					param_config.system_default_user_password = sys_cfg_value;
				}
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return param_config;
	}

	private boolean equals(String source, String target) {
		return cwUtils.notEmpty(source) && cwUtils.notEmpty(source) && source.trim().equalsIgnoreCase(target);
	}

	public void update(Connection con, loginProfile prof, SystemSettingReqParam param,String type) throws qdbException, cwSysMessage, SQLException {
		Timestamp curTIme = cwSQL.getTime(con);

		if("sys".equalsIgnoreCase(type)){
			// Mail Server
			updateItem(con, prof, curTIme, "SYS_MAIL_SERVER_HOST", param.mail_server_host);
			updateItem(con, prof, curTIme, "SYS_MAIL_SERVER_AUTH_ENABLED", String.valueOf(param.mail_server_auth_enabled));
			updateItem(con, prof, curTIme, "SYS_MAIL_SERVER_USER", param.mail_server_user);
//			updateItem(con, prof, curTIme, "SYS_MAIL_SERVER_ACCOUNT_TYPE", param.mail_server_account_type);
			updateItem(con, prof, curTIme, "SYS_MAIL_SERVER_PASSWORD", param.mail_server_password);
			updateItem(con, prof, curTIme, "SYS_MAIL_SCHEDULER_DOMAIN", param.mail_scheduler_domain);
			
			updateItem(con, prof, curTIme, "WETEAM_URL", param.webTeam_url);

			// LDAP
			updateItem(con, prof, curTIme, "SYS_LDAP_URL", param.ldap_url);
			updateItem(con, prof, curTIme, "SYS_LDAP_SUFFIX", param.ldap_suffix);

			// OpenOffice
			updateItem(con, prof, curTIme, "SYS_OPENOFFICE_ENABLED", String.valueOf(param.openoffice_enabled));
			updateItem(con, prof, curTIme, "SYS_OPENOFFICE_ENVIRONMENT", param.openoffice_environment);
			updateItem(con, prof, curTIme, "SYS_OPENOFFICE_PATH", param.openoffice_path);
			updateItem(con, prof, curTIme, "SYS_OPENOFFICE_HOST", param.openoffice_host);
			updateItem(con, prof, curTIme, "SYS_OPENOFFICE_PORT", String.valueOf(param.openoffice_port));
			
			//upload
			updateItem(con, prof, curTIme, "SYS_UPLOAD_FORBIDDEN", String.valueOf(param.upload_forbidden));
			
			//i doc view
			updateItem(con, prof, curTIme, "SYS_I_DOC_VEIW_HOST", param.i_doc_view_host);
			updateItem(con, prof, curTIme, "SYS_I_DOC_VIEW_PREVIEW_FILE_HOST", param.i_doc_view_preview_host);
			updateItem(con, prof, curTIme, "SYS_I_DOC_VIEW_TOKEN", param.i_doc_view_token);
			updateItem(con, prof, curTIme, "SYS_I_DOC_VIEW_PREVIEW_IP", param.i_doc_view_preview_IP);
			
			
			//密码安全策略
			updateItem(con, prof, curTIme, "PASSWORD_POLICY_COMPARE_COUNT", param.password_policy_compare_count+"");
			updateItem(con, prof, curTIme, "PASSWORD_POLICY_PERIOD", param.password_policy_period+"");
			updateItem(con, prof, curTIme, "PASSWORD_POLICY_PERIOD_FORCE", param.password_policy_period_force+"");
			
			//默认密码
			updateItem(con, prof, curTIme, "SYS_DEFAULT_USER_PASSWORD", param.system_default_user_password);
		}else {
			// WeChat
			updateItem(con, prof, curTIme, "SYS_WECHAT_DOMAIN", param.wechat_domain);
			if (param.wechat_domain != null) {
				param.wechat_mobile_domain = param.wechat_domain + "/mobile/";
			}
			updateItem(con, prof, curTIme, "SYS_WECHAT_MOBILE_DOMAIN", param.wechat_mobile_domain);
			updateItem(con, prof, curTIme, "SYS_WECHAT_SERVER_ID", param.wechat_server_id);
			updateItem(con, prof, curTIme, "SYS_WECHAT_PORT", String.valueOf(param.wechat_port));
			updateItem(con, prof, curTIme, "SYS_WECHAT_MAX_MESSAGE", String.valueOf(param.wechat_max_message));
			updateItem(con, prof, curTIme, "SYS_WECHAT_TRIAL_ACCOUNT_ENABLED", String.valueOf(param.wechat_trial_account_enabled));
			updateItem(con, prof, curTIme, "WECHAT_TOKEN_URL", param.wechat_token_url);
			updateItem(con, prof, curTIme, "WECHAT_MENU", param.wechat_menu);
		}
		param = null;
		// 所有配置修改完成后，强制初始化Application的配置信息
		Application.forceInit(con);
	}

	public void updateItem(Connection con, loginProfile prof, Timestamp curTime, String key, String val) throws qdbException, cwSysMessage, SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			String selectSql = " select * from SystemSetting where sys_cfg_type = '" + key + "' ";
			stmt = con.prepareStatement(selectSql);
			rs =  stmt.executeQuery();
			if(rs.next()){
				executeUpdate(con,prof,curTime,key,val);
			}else{
				executeInsert(con,prof,curTime,key,val);
			}
		}finally{
			cwSQL.cleanUp(rs, stmt);
		}
	}
	
	private void executeInsert(Connection con,loginProfile prof, Timestamp curTime, String key, String val) throws qdbException, cwSysMessage, SQLException{
		PreparedStatement stmt = null;
		try {
			String insertSql = " insert into SystemSetting(sys_cfg_type,sys_cfg_value,sys_cft_update_usr_id,sys_cfg_create_timestamp,sys_cfg_create_usr_id,sys_cfg_update_timestamp) values(?,?,?,?,?,?) ";
			stmt = con.prepareStatement(insertSql);
			stmt.setString(1, key);
			stmt.setString(2, val);
			stmt.setString(3, prof.usr_id);
			stmt.setTimestamp(4, curTime);
			stmt.setString(5, prof.usr_id);
			stmt.setTimestamp(6, curTime);
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(null, stmt);
		}
	}

	private void executeUpdate(Connection con, loginProfile prof, Timestamp curTime, String key, String val) throws qdbException, cwSysMessage, SQLException{
		PreparedStatement stmt = null;
		try {
			String updateSql = " update systemsetting set sys_cfg_value = ?, sys_cfg_update_timestamp = ?, sys_cft_update_usr_id = ? where sys_cfg_type = ?  ";
			stmt = con.prepareStatement(updateSql);
			stmt.setString(1, val);
			stmt.setTimestamp(2, curTime);
			stmt.setString(3, prof.usr_id);
			stmt.setString(4, key);
			stmt.executeUpdate();
		} finally {
			cwSQL.cleanUp(null, stmt);
		}
	}

}
