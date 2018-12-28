package com.cwn.wizbank.entity;

import java.util.Date;

public class EnterpriseInfoPortal implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long eip_id;
	private String eip_code;
	private String eip_name;
	private Long eip_tcr_id;
	private Long eip_account_num;
	private String eip_status;
	private String eip_domain;
	private Date eip_create_timestamp;
	private String eip_create_usr_id;
	private Date eip_update_timestamp;
	private String eip_update_usr_id;
	private Long eip_live_max_count;
	private String eip_live_mode;//直播模式
	
	private String eip_live_qcloud_secretid; //简易直播模式账号
	
	private String eip_live_qcloud_secretkey;//简易直播模式密码
	
	public Long getEip_id() {
		return eip_id;
	}
	public void setEip_id(Long eip_id) {
		this.eip_id = eip_id;
	}
	public String getEip_code() {
		return eip_code;
	}
	public void setEip_code(String eip_code) {
		this.eip_code = eip_code;
	}
	public String getEip_name() {
		return eip_name;
	}
	public void setEip_name(String eip_name) {
		this.eip_name = eip_name;
	}
	public Long getEip_tcr_id() {
		return eip_tcr_id;
	}
	public void setEip_tcr_id(Long eip_tcr_id) {
		this.eip_tcr_id = eip_tcr_id;
	}
	public Long getEip_account_num() {
		return eip_account_num;
	}
	public void setEip_account_num(Long eip_account_num) {
		this.eip_account_num = eip_account_num;
	}
	public String getEip_status() {
		return eip_status;
	}
	public void setEip_status(String eip_status) {
		this.eip_status = eip_status;
	}
	public String getEip_domain() {
		return eip_domain;
	}
	public void setEip_domain(String eip_domain) {
		this.eip_domain = eip_domain;
	}
	public Date getEip_create_timestamp() {
		return eip_create_timestamp;
	}
	public void setEip_create_timestamp(Date eip_create_timestamp) {
		this.eip_create_timestamp = eip_create_timestamp;
	}
	public String getEip_create_usr_id() {
		return eip_create_usr_id;
	}
	public void setEip_create_usr_id(String eip_create_usr_id) {
		this.eip_create_usr_id = eip_create_usr_id;
	}
	public Date getEip_update_timestamp() {
		return eip_update_timestamp;
	}
	public void setEip_update_timestamp(Date eip_update_timestamp) {
		this.eip_update_timestamp = eip_update_timestamp;
	}
	public String getEip_update_usr_id() {
		return eip_update_usr_id;
	}
	public void setEip_update_usr_id(String eip_update_usr_id) {
		this.eip_update_usr_id = eip_update_usr_id;
	}
	public Long getEip_live_max_count() {
		return eip_live_max_count;
	}
	public void setEip_live_max_count(Long eip_live_max_count) {
		this.eip_live_max_count = eip_live_max_count;
	}
	public String getEip_live_mode() {
		return eip_live_mode;
	}
	public void setEip_live_mode(String eip_live_mode) {
		this.eip_live_mode = eip_live_mode;
	}
	public String getEip_live_qcloud_secretid() {
		return eip_live_qcloud_secretid;
	}
	public void setEip_live_qcloud_secretid(String eip_live_qcloud_secretid) {
		this.eip_live_qcloud_secretid = eip_live_qcloud_secretid;
	}
	public String getEip_live_qcloud_secretkey() {
		return eip_live_qcloud_secretkey;
	}
	public void setEip_live_qcloud_secretkey(String eip_live_qcloud_secretkey) {
		this.eip_live_qcloud_secretkey = eip_live_qcloud_secretkey;
	}
	
	

}
