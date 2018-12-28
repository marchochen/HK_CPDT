package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Desc :小牌时数
 */
public class CpdtGroupHours implements Serializable{
	
	private static final long serialVersionUID = -1253820499758069518L;
	/**
	 * 主键
	 */
	private Long cgh_id;
	/**
	 * 小牌周期的ID cpdGroupPeriod.cgp_id
	 */
	private Long cgh_cgp_id ;
	/**
	 * 申报月份 (每个有效时间至少有一条)
	 */
	private Integer cgh_declare_month;
	/**
	 * 核心时数
	 */
	private Float cgh_core_hours ;
	/**
	 * 非核心时数
	 */
	private Float cgh_non_core_hours ;
	/**
	 * 创建用户ID reguser.usr_ent_id
	 */
	private Long cgh_create_usr_ent_id ;
	/**
	 * 创建时间
	 */
	private Date cgh_create_datetime ;
	/**
	 * 更新用户ID reguser.usr_ent_id
	 */
	private Long cgh_update_usr_ent_id;
	/**
	 * 更新时间
	 */
	private Date cgh_update_datetime ;
	/**
	 * 状态(OK : 正常 DEL：已删除)
	 */
	private String cgh_status ;
	
	public Long getCgh_id() {
		return cgh_id;
	}
	public void setCgh_id(Long cgh_id) {
		this.cgh_id = cgh_id;
	}
	public Long getCgh_cgp_id() {
		return cgh_cgp_id;
	}
	public void setCgh_cgp_id(Long cgh_cgp_id) {
		this.cgh_cgp_id = cgh_cgp_id;
	}
	public Integer getCgh_declare_month() {
		return cgh_declare_month;
	}
	public void setCgh_declare_month(Integer cgh_declare_month) {
		this.cgh_declare_month = cgh_declare_month;
	}
	public Float getCgh_core_hours() {
		return cgh_core_hours;
	}
	public void setCgh_core_hours(Float cgh_core_hours) {
		this.cgh_core_hours = cgh_core_hours;
	}
	public Float getCgh_non_core_hours() {
		return cgh_non_core_hours;
	}
	public void setCgh_non_core_hours(Float cgh_non_core_hours) {
		this.cgh_non_core_hours = cgh_non_core_hours;
	}
	public Long getCgh_create_usr_ent_id() {
		return cgh_create_usr_ent_id;
	}
	public void setCgh_create_usr_ent_id(Long cgh_create_usr_ent_id) {
		this.cgh_create_usr_ent_id = cgh_create_usr_ent_id;
	}
	public Date getCgh_create_datetime() {
		return cgh_create_datetime;
	}
	public void setCgh_create_datetime(Date cgh_create_datetime) {
		this.cgh_create_datetime = cgh_create_datetime;
	}
	public Long getCgh_update_usr_ent_id() {
		return cgh_update_usr_ent_id;
	}
	public void setCgh_update_usr_ent_id(Long cgh_update_usr_ent_id) {
		this.cgh_update_usr_ent_id = cgh_update_usr_ent_id;
	}
	public Date getCgh_update_datetime() {
		return cgh_update_datetime;
	}
	public void setCgh_update_datetime(Date cgh_update_datetime) {
		this.cgh_update_datetime = cgh_update_datetime;
	}
	public String getCgh_status() {
		return cgh_status;
	}
	public void setCgh_status(String cgh_status) {
		this.cgh_status = cgh_status;
	}
	
	
}
