package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * Desc:CPT/D报表备注 - 报表备注历史表
 */
public class CpdtReportRemark implements Serializable{

	private static final long serialVersionUID = -2272967293874610362L;
	/**
	 * 递增主键
	 */
	private long crpm_id;
	/**
	 * 报表编号
	 */
	private String crpm_report_code;
	/**
	 * 报表备注
	 */
	private String crpm_report_remark;
	/**
	 * 创建时间
	 */
	private Date crpm_create_datetime;
	/**
	 * 用户ID
	 */
	private long crpm_create_usr_ent_id;
	/**
	 * 更新时间
	 */
	private Date crpm_update_datetime;
	/**
	 * 更新用户的ID
	 */
	private long crpm_update_usr_ent_id;
	
	public long getCrpm_id() {
		return crpm_id;
	}
	public void setCrpm_id(long crpm_id) {
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
	public long getCrpm_create_usr_ent_id() {
		return crpm_create_usr_ent_id;
	}
	public void setCrpm_create_usr_ent_id(long crpm_create_usr_ent_id) {
		this.crpm_create_usr_ent_id = crpm_create_usr_ent_id;
	}
	public Date getCrpm_update_datetime() {
		return crpm_update_datetime;
	}
	public void setCrpm_update_datetime(Date crpm_update_datetime) {
		this.crpm_update_datetime = crpm_update_datetime;
	}
	public long getCrpm_update_usr_ent_id() {
		return crpm_update_usr_ent_id;
	}
	public void setCrpm_update_usr_ent_id(long crpm_update_usr_ent_id) {
		this.crpm_update_usr_ent_id = crpm_update_usr_ent_id;
	}
}
