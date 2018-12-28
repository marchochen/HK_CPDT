package com.cw.wizbank.systemSetting;

import com.cw.wizbank.JsonMod.BaseParam;

public class SystemSettingReqParam extends BaseParam {
	public String mail_server_host;
	public boolean mail_server_auth_enabled;
	public String mail_server_account_type;
	public String mail_server_user;
	public String mail_server_password;
	public String mail_scheduler_domain;

	public String wechat_domain;
	public String wechat_mobile_domain;
	public String wechat_server_id;
	public int wechat_port;
	public int wechat_max_message;
	public boolean wechat_trial_account_enabled;

	public String ldap_url;
	public String ldap_suffix;

	public boolean openoffice_enabled;
	public String openoffice_environment;
	public String openoffice_path;
	public String openoffice_host;
	public int openoffice_port;
	
	public String wechat_token_url;
	public String wechat_menu;
	
	public String webTeam_url;

	public String upload_forbidden;
	
	//i doc view host
	public String i_doc_view_host;
	public String i_doc_view_preview_host ;
	public String i_doc_view_token ;
	public String i_doc_view_preview_IP;
	
	
	public String multiple_login_ind;
	
	/*
	 * 密码安全设置策略
	 */
	public String password_policy_period_force;
	public String password_policy_period;
	public String password_policy_compare_count;
	
	/*
	 * 默认密码
	 */
	public String system_default_user_password;
	
    public String getI_doc_view_preview_IP() {
		return i_doc_view_preview_IP;
	}

	public void setI_doc_view_preview_IP(String i_doc_view_preview_IP) {
		this.i_doc_view_preview_IP = i_doc_view_preview_IP;
	}

	public String getI_doc_view_token() {
		return i_doc_view_token;
	}

	public void setI_doc_view_token(String i_doc_view_token) {
		this.i_doc_view_token = i_doc_view_token;
	}

	public String getWebTeam_url() {
		return webTeam_url;
	}

	public void setWebTeam_url(String webTeam_url) {
		this.webTeam_url = webTeam_url;
	}

	public String getMail_server_host() {
		return mail_server_host;
	}

	public void setMail_server_host(String mail_server_host) {
		this.mail_server_host = mail_server_host;
	}

	public boolean isMail_server_auth_enabled() {
		return mail_server_auth_enabled;
	}

	public void setMail_server_auth_enabled(boolean mail_server_auth_enabled) {
		this.mail_server_auth_enabled = mail_server_auth_enabled;
	}

	public String getMail_server_user() {
		return mail_server_user;
	}

	public void setMail_server_user(String mail_server_user) {
		this.mail_server_user = mail_server_user;
	}

	public String getMail_server_password() {
		return mail_server_password;
	}

	public void setMail_server_password(String mail_server_password) {
		this.mail_server_password = mail_server_password;
	}

	public String getMail_scheduler_domain() {
		return mail_scheduler_domain;
	}

	public void setMail_scheduler_domain(String mail_scheduler_domain) {
		this.mail_scheduler_domain = mail_scheduler_domain;
	}

	public String getWechat_domain() {
		return wechat_domain;
	}

	public void setWechat_domain(String wechat_domain) {
		this.wechat_domain = wechat_domain;
	}

	public String getWechat_mobile_domain() {
		return wechat_mobile_domain;
	}

	public void setWechat_mobile_domain(String wechat_mobile_domain) {
		this.wechat_mobile_domain = wechat_mobile_domain;
	}

	public String getWechat_server_id() {
		return wechat_server_id;
	}

	public void setWechat_server_id(String wechat_server_id) {
		this.wechat_server_id = wechat_server_id;
	}

	public int getWechat_port() {
		return wechat_port;
	}

	public void setWechat_port(int wechat_port) {
		this.wechat_port = wechat_port;
	}

	public int getWechat_max_message() {
		return wechat_max_message;
	}

	public void setWechat_max_message(int wechat_max_message) {
		this.wechat_max_message = wechat_max_message;
	}

	public boolean isWechat_trial_account_enabled() {
		return wechat_trial_account_enabled;
	}

	public void setWechat_trial_account_enabled(boolean wechat_trial_account_enabled) {
		this.wechat_trial_account_enabled = wechat_trial_account_enabled;
	}

	public String getLdap_url() {
		return ldap_url;
	}

	public void setLdap_url(String ldap_url) {
		this.ldap_url = ldap_url;
	}

	public String getLdap_suffix() {
		return ldap_suffix;
	}

	public void setLdap_suffix(String ldap_suffix) {
		this.ldap_suffix = ldap_suffix;
	}

	public boolean isOpenoffice_enabled() {
		return openoffice_enabled;
	}

	public void setOpenoffice_enabled(boolean openoffice_enabled) {
		this.openoffice_enabled = openoffice_enabled;
	}

	public String getOpenoffice_environment() {
		return openoffice_environment;
	}

	public void setOpenoffice_environment(String openoffice_environment) {
		this.openoffice_environment = openoffice_environment;
	}

	public String getOpenoffice_path() {
		return openoffice_path;
	}

	public void setOpenoffice_path(String openoffice_path) {
		this.openoffice_path = openoffice_path;
	}

	public String getOpenoffice_host() {
		return openoffice_host;
	}

	public void setOpenoffice_host(String openoffice_host) {
		this.openoffice_host = openoffice_host;
	}

	public int getOpenoffice_port() {
		return openoffice_port;
	}

	public void setOpenoffice_port(int openoffice_port) {
		this.openoffice_port = openoffice_port;
	}

	public String getMail_server_account_type() {
		return mail_server_account_type;
	}

	public void setMail_server_account_type(String mail_server_account_type) {
		this.mail_server_account_type = mail_server_account_type;
	}

	public String getWechat_token_url() {
		return wechat_token_url;
	}

	public void setWechat_token_url(String wechat_token_url) {
		this.wechat_token_url = wechat_token_url;
	}

	public String getWechat_menu() {
		return wechat_menu;
	}

	public void setWechat_menu(String wechat_menu) {
		this.wechat_menu = wechat_menu;
	}
	
	public String getUpload_forbidden() {
		return upload_forbidden;
	}

	public void setUpload_forbidden(String upload_forbidden) {
		this.upload_forbidden = upload_forbidden;
	}

	public String getMultiple_login_ind() {
		return multiple_login_ind;
	}

	public void setMultiple_login_ind(String multiple_login_ind) {
		this.multiple_login_ind = multiple_login_ind;
	}

	public String getPassword_policy_period_force() {
		return password_policy_period_force;
	}

	public void setPassword_policy_period_force(String password_policy_period_force) {
		this.password_policy_period_force = password_policy_period_force;
	}

	public String getPassword_policy_period() {
		return password_policy_period;
	}

	public void setPassword_policy_period(String password_policy_period) {
		this.password_policy_period = password_policy_period;
	}

	public String getPassword_policy_compare_count() {
		return password_policy_compare_count;
	}

	public void setPassword_policy_compare_count(String password_policy_compare_count) {
		this.password_policy_compare_count = password_policy_compare_count;
	}

	public String getI_doc_view_host() {
		return i_doc_view_host;
	}

	public void setI_doc_view_host(String i_doc_view_host) {
		this.i_doc_view_host = i_doc_view_host;
	}

	public String getI_doc_view_preview_host() {
		return i_doc_view_preview_host;
	}

	public void setI_doc_view_preview_host(String i_doc_view_preview_host) {
		this.i_doc_view_preview_host = i_doc_view_preview_host;
	}

	public String getSystem_default_user_password() {
		return system_default_user_password;
	}

	public void setSystem_default_user_password(String system_default_user_password) {
		this.system_default_user_password = system_default_user_password;
	}

	
}
