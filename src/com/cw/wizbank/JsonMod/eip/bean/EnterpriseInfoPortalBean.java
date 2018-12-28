package com.cw.wizbank.JsonMod.eip.bean;

import java.sql.Timestamp;

public class EnterpriseInfoPortalBean {
	private long eip_id;                        //企业自增ID
	private String eip_code;                    //企业编号
	private String eip_name;					//企业名称
	private long eip_tcr_id;					//企业关联的培训中心
	private String tcr_title;					//培训中心名称
	private long eip_account_num;				//租用账户数目
	private long account_used;					//已使用账户数目
	private String eip_status;					//企业状态
	private String eip_domain;					//企业域名
	private String eip_create_display_bil;		
	private Timestamp eip_create_timestamp;
	private String eip_update_display_bil;
	private Timestamp eip_update_timestamp;
	private String eip_price;
	private long peak_count; 				//企业在线人数
	private long eip_max_peak_count;	//企业最大在线人数
	private long eip_live_max_count;
	
	private String eip_live_mode; //直播模式
	
	private String eip_live_qcloud_secretid; //简易直播模式账号
	
	private String eip_live_qcloud_secretkey;//简易直播模式密码
	
	public long getEip_account_num() {
		return eip_account_num;
	}
	public void setEip_account_num(long eip_account_num) {
		this.eip_account_num = eip_account_num;
	}
	public String getEip_code() {
		return eip_code;
	}
	public void setEip_code(String eip_code) {
		this.eip_code = eip_code;
	}
	public Timestamp getEip_create_timestamp() {
		return eip_create_timestamp;
	}
	public void setEip_create_timestamp(Timestamp eip_create_timestamp) {
		this.eip_create_timestamp = eip_create_timestamp;
	}
	public String getEip_domain() {
		return eip_domain;
	}
	public void setEip_domain(String eip_domain) {
		this.eip_domain = eip_domain;
	}
	public long getEip_id() {
		return eip_id;
	}
	public void setEip_id(long eip_id) {
		this.eip_id = eip_id;
	}
	public String getEip_name() {
		return eip_name;
	}
	public void setEip_name(String eip_name) {
		this.eip_name = eip_name;
	}
	public String getEip_status() {
		return eip_status;
	}
	public void setEip_status(String eip_status) {
		this.eip_status = eip_status;
	}
	public long getEip_tcr_id() {
		return eip_tcr_id;
	}
	public void setEip_tcr_id(long eip_tcr_id) {
		this.eip_tcr_id = eip_tcr_id;
	}
	public Timestamp getEip_update_timestamp() {
		return eip_update_timestamp;
	}
	public void setEip_update_timestamp(Timestamp eip_update_timestamp) {
		this.eip_update_timestamp = eip_update_timestamp;
	}
	public long getAccount_used() {
		return account_used;
	}
	public void setAccount_used(long account_used) {
		this.account_used = account_used;
	}
	public String getTcr_title() {
		return tcr_title;
	}
	public void setTcr_title(String tcr_title) {
		this.tcr_title = tcr_title;
	}
	public String getEip_update_display_bil() {
		return eip_update_display_bil;
	}
	public void setEip_update_display_bil(String eip_update_display_bil) {
		this.eip_update_display_bil = eip_update_display_bil;
	}
	public String getEip_create_display_bil() {
		return eip_create_display_bil;
	}
	public void setEip_create_display_bil(String eip_create_display_bil) {
		this.eip_create_display_bil = eip_create_display_bil;
	}
	public String getEip_price() {
		return eip_price;
	} 
	public void setEip_price(String eip_price) {
		this.eip_price = eip_price;
	}
	public long getPeak_count() {
		return peak_count;
	}
	public void setPeak_count(long peak_count) {
		this.peak_count = peak_count;
	}
	public long getEip_max_peak_count() {
		return eip_max_peak_count;
	}
	public void setEip_max_peak_count(long eip_max_peak_count) {
		this.eip_max_peak_count = eip_max_peak_count;
	}
	public long getEip_live_max_count() {
		return eip_live_max_count;
	}
	public void setEip_live_max_count(long eip_live_max_count) {
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
