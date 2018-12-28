package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 用户历史密码保存实体
 * @author andrew.xiao 2016-11-16 16:22
 *
 */
public class UserPasswordHistory {

	public static final String UPH_CLIENT_TYPE_MOBILE = "mobile";
	public static final String UPH_CLIENT_TYPE_PC = "pc";
	
	/**
	 * 用户ID
	 */
	private long uph_usr_ent_id;
	
	/**
	 * 用户历史密码（已加密的）
	 */
	private String uph_pwd;
	
	/**
	 * 修改人id
	 */
	private long uph_update_usr_ent_id;
	
	/**
	 * 修改的终端：PC或者Mobile
	 */
	private String uph_client_type;
	
	/**
	 * 创建时间
	 */
	private Date uph_create_time;
	
	
	public UserPasswordHistory(){
	}

	public UserPasswordHistory(long uph_usr_ent_id, String uph_pwd,
			long uph_update_usr_ent_id, String uph_client_type,
			Date uph_create_time) {
		super();
		this.uph_usr_ent_id = uph_usr_ent_id;
		this.uph_pwd = uph_pwd;
		this.uph_update_usr_ent_id = uph_update_usr_ent_id;
		this.uph_client_type = uph_client_type;
		this.uph_create_time = uph_create_time;
	}

	public long getUph_usr_ent_id() {
		return uph_usr_ent_id;
	}

	public void setUph_usr_ent_id(long uph_usr_ent_id) {
		this.uph_usr_ent_id = uph_usr_ent_id;
	}

	public String getUph_pwd() {
		return uph_pwd;
	}

	public void setUph_pwd(String uph_pwd) {
		this.uph_pwd = uph_pwd;
	}

	public long getUph_update_usr_ent_id() {
		return uph_update_usr_ent_id;
	}

	public void setUph_update_usr_ent_id(long uph_update_usr_ent_id) {
		this.uph_update_usr_ent_id = uph_update_usr_ent_id;
	}

	public String getUph_client_type() {
		return uph_client_type;
	}

	public void setUph_client_type(String uph_client_type) {
		this.uph_client_type = uph_client_type;
	}

	public Date getUph_create_time() {
		return uph_create_time;
	}

	public void setUph_create_time(Date uph_create_time) {
		this.uph_create_time = uph_create_time;
	}
}
