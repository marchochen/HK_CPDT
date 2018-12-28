package com.cw.wizbank;

import java.sql.Connection;
import java.sql.SQLException;

import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.systemSetting.SystemSetting;
import com.cw.wizbank.systemSetting.SystemSettingReqParam;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.utils.CommonLog;

public class Application {
	// 标记是否已经初始化
	private static boolean INIT = false;

	/**
	 * 邮件服务器
	 * MAIL_SERVER_HOST 		： 邮件服务器
	 * MAIL_SERVER_AUTH_ENABLED ： 是否启用用户验证
	 * MAIL_SERVER_USER 		： 用户名
	 * MAIL_SERVER_PASSWORD 	： 密码
	 * MAIL_SCHEDULER_DOMAIN 	： 发送邮件线程的域名
	 */
	public static String MAIL_SERVER_HOST = null;
	public static boolean MAIL_SERVER_AUTH_ENABLED = false;
	public static String MAIL_SERVER_ACCOUNT_TYPE = null;
	public static String MAIL_SERVER_USER = null;
	public static String MAIL_SERVER_PASSWORD = null;
	public static String MAIL_SCHEDULER_DOMAIN = null;
	
	public static String SYS_WEBTEAM_URL = null;

	/**
	 * 微信
	 * WECHAT_DOMAIN 				： 邮件服务器
	 * WECHAT_MOBILE_DOMAIN 		： 邮件服务器
	 * WECHAT_SERVER_ID 			： 是否启用用户验证
	 * WECHAT_PORT 					： 用户名
	 * WECHAT_MAX_MESSAGE 			： 控制微信消息的条数
	 * WECHAT_ENABLE_TRIAL_ACCOUNT 	： 启用体验帐号
	 */
	public static String WECHAT_DOMAIN = null;
	public static String WECHAT_MOBILE_DOMAIN = null;
	public static String WECHAT_SERVER_ID = null;
	public static int WECHAT_PORT = 0;
	public static int WECHAT_MAX_MESSAGE = 0;
	public static boolean WECHAT_TRIAL_ACCOUNT_ENABLED = false;
	public static String WECHAT_TOKEN_URL = null;

	/**
	 * OpenOffile
	 * OPENOFFICE_ENABLED 		： 是否启用服务
	 * OPENOFFICE_ENVIRONMENT 	： 所对应操作系统（Window | Linux）
	 * OPENOFFICE_PATH 			： 执行路径
	 * OPENOFFICE_HOST 			： 域名
	 * OPENOFFICE_PORT 			： 端口
	 */
	public static boolean OPENOFFICE_ENABLED = false;
	public static String OPENOFFICE_ENVIRONMENT = null;
	public static String OPENOFFICE_PATH = null;
	public static String OPENOFFICE_HOST = null;
	public static int OPENOFFICE_PORT = 0;

	/**
	 * LDAP Server
	 * LDAP_URL 	： 是否启用服务
	 * LDAP_SUFFIX 	： 所对应操作系统（Window | Linux）
	 */
	public static String LDAP_URL = null;
	public static String LDAP_SUFFIX = null;
	
	//不允许上传的文件格式
	public static String UPLOAD_FORBIDDEN = null;
	
	//I DOC VIEW host
	public static String I_DOC_VIEW_HOST ;
	public static String I_DOC_VIEW_PREVIEW_HOST;
	public static String I_DOC_VIEW_TOKEN;
	public static String I_DOC_VIEW_PREVIEW_IP;
	
	public static boolean MULTIPLE_LOGIN;
	
	
	/*
	 * 密码安全策略
	 */
	public static String PASSWORD_POLICY_COMPARE_COUNT;
	public static String PASSWORD_POLICY_PERIOD;
	public static String PASSWORD_POLICY_PERIOD_FORCE;
	 
    /*
     * 默认密码
     */
	public static String SYS_DEFAULT_USER_PASSWORD;
	
	// 初始化，如果已经初始化完成，那么跳过，不重复做初始化
	public static void init(Connection con) throws qdbException, cwSysMessage, SQLException {
		init(con, false);
	}

	// 强制初始化
	public static void forceInit(Connection con) throws qdbException, cwSysMessage, SQLException {
		init(con, true);
	}

	public static void init(Connection con, boolean forceInit) throws qdbException, cwSysMessage, SQLException {
		if (forceInit || !INIT) {
			CommonLog.info("Application.init() start...");
			SystemSetting ss = new SystemSetting();
			SystemSettingReqParam config = ss.get(con);

			// Mail Server
			MAIL_SERVER_HOST = config.mail_server_host;
			MAIL_SERVER_AUTH_ENABLED = config.mail_server_auth_enabled;
			MAIL_SERVER_ACCOUNT_TYPE = config.mail_server_account_type;
			MAIL_SERVER_USER = config.mail_server_user;
			MAIL_SERVER_PASSWORD = config.mail_server_password;
			MAIL_SCHEDULER_DOMAIN = config.mail_scheduler_domain;
			
			SYS_WEBTEAM_URL = config.webTeam_url;

			// WeChat
			WECHAT_DOMAIN = config.wechat_domain;
			WECHAT_MOBILE_DOMAIN = config.wechat_mobile_domain;
			WECHAT_SERVER_ID = config.wechat_server_id;
			WECHAT_PORT = config.wechat_port;
			WECHAT_MAX_MESSAGE = config.wechat_max_message;
			WECHAT_TRIAL_ACCOUNT_ENABLED = config.wechat_trial_account_enabled;
			WECHAT_TOKEN_URL = config.wechat_token_url;

			// OpenOffice
			OPENOFFICE_ENABLED = config.openoffice_enabled;
			OPENOFFICE_ENVIRONMENT = config.openoffice_environment;
			OPENOFFICE_PATH = config.openoffice_path;
			OPENOFFICE_HOST = config.openoffice_host;
			OPENOFFICE_PORT = config.openoffice_port;
			

			// LDAP
			LDAP_URL = config.ldap_url;
			LDAP_SUFFIX = config.ldap_suffix;
			
			UPLOAD_FORBIDDEN = config.upload_forbidden;

			//i doc view host
			I_DOC_VIEW_HOST = config.i_doc_view_host;
			I_DOC_VIEW_PREVIEW_HOST = config.i_doc_view_preview_host;
			I_DOC_VIEW_TOKEN=config.i_doc_view_token;
			I_DOC_VIEW_PREVIEW_IP=config.i_doc_view_preview_IP;
			
			MULTIPLE_LOGIN = "1".equals(config.multiple_login_ind);
			
			PASSWORD_POLICY_COMPARE_COUNT =  config.password_policy_compare_count;
			PASSWORD_POLICY_PERIOD =  config.password_policy_period;
			PASSWORD_POLICY_PERIOD_FORCE = config.password_policy_period_force;
			
			SYS_DEFAULT_USER_PASSWORD = config.system_default_user_password;
			
			CommonLog.info("Application.init() end...");
		}
	}
}
