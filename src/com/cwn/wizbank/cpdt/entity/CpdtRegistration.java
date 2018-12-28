package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Desc：CPT/D大牌注册信息
 */
public class CpdtRegistration implements Serializable{

	private static final long serialVersionUID = 2374437852607638692L;
	/**
	 * 主键
	 */
	private Long cr_id;
	/**
	 * 注册用户id reguser.usr_ent_id
	 */
	private Long cr_usr_ent_id;
	/**
	 * 大牌的ID cpdType. ct_id
	 */
	private Long cr_ct_id;
	/**
	 * 注册时间 yyyy-mm-dd
	 */
	private Date cr_reg_datetime;
	/**
	 * 除牌时间  yyyy-mm-dd
	 */   
	private Date cr_de_reg_datetime;
	/**
	 * 创建用户ID reguser.usr_ent_id
	 */
	private Long cr_create_usr_ent_id;
	/**
	 * 创建时间
	 */
	private Date cr_create_datetime;
	/**
	 * 更新用户ID reguser.usr_ent_id
	 */
	private Long cr_update_usr_ent_id;
	/**
	 * 更新时间
	 */
	private Date cr_update_datetime;
	/**
	 * 状态(OK : 正常 DEL：已删除)
	 */
	private String cr_status;
	/**
	 * 注册号码
	 */
	private String cr_reg_number;
	
	public Long getCr_id() {
		return cr_id;
	}
	public void setCr_id(Long cr_id) {
		this.cr_id = cr_id;
	}
	public Long getCr_usr_ent_id() {
		return cr_usr_ent_id;
	}
	public void setCr_usr_ent_id(Long cr_usr_ent_id) {
		this.cr_usr_ent_id = cr_usr_ent_id;
	}
	public Long getCr_ct_id() {
		return cr_ct_id;
	}
	public void setCr_ct_id(Long cr_ct_id) {
		this.cr_ct_id = cr_ct_id;
	}
	public Date getCr_reg_datetime() {
		return cr_reg_datetime;
	}
	public void setCr_reg_datetime(Date cr_reg_datetime) {
		this.cr_reg_datetime = cr_reg_datetime;
	}
	public Date getCr_de_reg_datetime() {
		return cr_de_reg_datetime;
	}
	public void setCr_de_reg_datetime(Date cr_de_reg_datetime) {
		this.cr_de_reg_datetime = cr_de_reg_datetime;
	}
	public Long getCr_create_usr_ent_id() {
		return cr_create_usr_ent_id;
	}
	public void setCr_create_usr_ent_id(Long cr_create_usr_ent_id) {
		this.cr_create_usr_ent_id = cr_create_usr_ent_id;
	}
	public Date getCr_create_datetime() {
		return cr_create_datetime;
	}
	public void setCr_create_datetime(Date cr_create_datetime) {
		this.cr_create_datetime = cr_create_datetime;
	}
	public Long getCr_update_usr_ent_id() {
		return cr_update_usr_ent_id;
	}
	public void setCr_update_usr_ent_id(Long cr_update_usr_ent_id) {
		this.cr_update_usr_ent_id = cr_update_usr_ent_id;
	}
	public Date getCr_update_datetime() {
		return cr_update_datetime;
	}
	public void setCr_update_datetime(Date cr_update_datetime) {
		this.cr_update_datetime = cr_update_datetime;
	}
	public String getCr_status() {
		return cr_status;
	}
	public void setCr_status(String cr_status) {
		this.cr_status = cr_status;
	}
	public String getCr_reg_number() {
		return cr_reg_number;
	}
	public void setCr_reg_number(String cr_reg_number) {
		this.cr_reg_number = cr_reg_number;
	}
	
}
