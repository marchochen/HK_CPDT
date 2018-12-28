package com.cwn.wizbank.entity;

import java.util.Date;

public class CpdReportRemark implements java.io.Serializable{

	private static final long serialVersionUID = -1919766849107003223L;
	

	public static String OUTSTANDING_REMARK_CODE = "outstanding_remark";
	
	public static String INDIVIDUAL_REMARK_CODE = "individual_remark";
	
	public static String AWARDED_REMARK_CODE = "awarded_remark";
	
	public static String LICENSE_REGISTRATION_CODE = "license_registration";

	private Long crpm_id ;
	private String crpm_report_code ;
	private String crpm_report_remark ;
	private Date crpm_create_datetime ;
	private Long crpm_create_usr_ent_id ;
	private Date crpm_update_datetime ;
	private Long crpm_update_usr_ent_id ;
	
	public Long getCrpm_id() {
		return crpm_id;
	}
	public void setCrpm_id(Long crpm_id) {
		this.crpm_id = crpm_id;
	}
	public String getCrpm_report_code() {
		return crpm_report_code;
	}
	public void setCrpm_report_code(String crpm_report_code) {
		this.crpm_report_code = crpm_report_code;
	}
	public String getCrpm_report_remark() {
		return crpm_report_remark;
	}
	public void setCrpm_report_remark(String crpm_report_remark) {
		this.crpm_report_remark = crpm_report_remark;
	}
	public Date getCrpm_create_datetime() {
		return crpm_create_datetime;
	}
	public void setCrpm_create_datetime(Date crpm_create_datetime) {
		this.crpm_create_datetime = crpm_create_datetime;
	}
	public Long getCrpm_create_usr_ent_id() {
		return crpm_create_usr_ent_id;
	}
	public void setCrpm_create_usr_ent_id(Long crpm_create_usr_ent_id) {
		this.crpm_create_usr_ent_id = crpm_create_usr_ent_id;
	}
	public Date getCrpm_update_datetime() {
		return crpm_update_datetime;
	}
	public void setCrpm_update_datetime(Date crpm_update_datetime) {
		this.crpm_update_datetime = crpm_update_datetime;
	}
	public Long getCrpm_update_usr_ent_id() {
		return crpm_update_usr_ent_id;
	}
	public void setCrpm_update_usr_ent_id(Long crpm_update_usr_ent_id) {
		this.crpm_update_usr_ent_id = crpm_update_usr_ent_id;
	}

	
	
}
