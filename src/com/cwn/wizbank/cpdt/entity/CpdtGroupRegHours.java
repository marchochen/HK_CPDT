package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *  Desc: CPT/D小牌注册时数记录 冗余表
 */
public class CpdtGroupRegHours implements Serializable{
	
	private static final long serialVersionUID = 2514767546489259136L;
	/**
	 * 递增主键
	 */
	private Long cgrh_id;
	/**
	 * 注册用户id reguser.usr_ent_id
	 */
	private Long cgrh_usr_ent_id;
	/**
	 * 小牌注册信息的ID cpdGroupRegistration. cgr_id
	 */
	private Long cgrh_cgr_id;
	/**
	 * 大牌注册信息的ID cpdRegistration.cr_id
	 */
	private Long cgrh_cr_id;
	/**
	 * 所属周期(yyyy)
	 */
	private Integer cgrh_cgr_period;
	/**
	 * 要求时数计算的月份
	 */
	private int cgrh_cal_month;
	/**
	 * cpdGroupPeriod.cgp_id
	 */
	private Long cgrh_cgp_id;
	/**
	 * 计算时数的开始时间（用于课程时数计算）
	 */
	private Date cgrh_cal_start_date;
	/**
	 * 计算时数的结束时间（用于课程时数计算）
	 */
	private Date cgrh_cal_end_date;
	/**
	 * 手动修改的核心时数
	 */
	private Float cgrh_manul_core_hours;
	/**
	 * 手动修改的非核心时数
	 */
	private Float cgrh_manul_non_core_hours;
	/**
	 * 是否手动修改过(1、是   2、否)
	 */
	private int cgrh_manul_ind;
	/**
	 * 需要的核心时数(根据cpdGroupHours计算出来)
	 */
	private Float cgrh_req_core_hours;
	/**
	 * 需要的非核心时数 (根据cpdGroupHours计算出来)
	 */
	private Float cgrh_req_non_core_hours;
	/**
	 * 实际应用计算的核心时数 (如果没有手动修改过 与cgrh_req_core_hours相同 否则与cgrh_manul_core_hours 相同)
	 */
	private Float  cgrh_execute_core_hours;
	/**
	 * 实际应用计算的非核心时数 ( 如果没有手动修改过 与cgrh_req_non_core_hours相同 否则与cgrh_manul_non_core_hours 相同)
	 */
	private Float cgrh_execute_non_core_hours;
	/**
	 * 创建用户ID reguser.usr_ent_id
	 */
	private Long cgrh_create_usr_ent_id;
	/**
	 * 创建时间
	 */
	private Date cgrh_create_datetime;
	/**
	 * 更新用户ID reguser.usr_ent_id
	 */
	private Long cgrh_update_usr_ent_id;
	/**
	 * 更新时间
	 */
	private Date cgrh_update_datetime;
	
	
	public Long getCgrh_id() {
		return cgrh_id;
	}
	public void setCgrh_id(Long cgrh_id) {
		this.cgrh_id = cgrh_id;
	}
	public Long getCgrh_usr_ent_id() {
		return cgrh_usr_ent_id;
	}
	public void setCgrh_usr_ent_id(Long cgrh_usr_ent_id) {
		this.cgrh_usr_ent_id = cgrh_usr_ent_id;
	}
	public Long getCgrh_cgr_id() {
		return cgrh_cgr_id;
	}
	public void setCgrh_cgr_id(Long cgrh_cgr_id) {
		this.cgrh_cgr_id = cgrh_cgr_id;
	}
	public Long getCgrh_cr_id() {
		return cgrh_cr_id;
	}
	public void setCgrh_cr_id(Long cgrh_cr_id) {
		this.cgrh_cr_id = cgrh_cr_id;
	}
	public Integer getCgrh_cgr_period() {
		return cgrh_cgr_period;
	}
	public void setCgrh_cgr_period(Integer cgrh_cgr_period) {
		this.cgrh_cgr_period = cgrh_cgr_period;
	}
	public int getCgrh_cal_month() {
		return cgrh_cal_month;
	}
	public void setCgrh_cal_month(int cgrh_cal_month) {
		this.cgrh_cal_month = cgrh_cal_month;
	}
	public Long getCgrh_cgp_id() {
		return cgrh_cgp_id;
	}
	public void setCgrh_cgp_id(Long cgrh_cgp_id) {
		this.cgrh_cgp_id = cgrh_cgp_id;
	}
	public Date getCgrh_cal_start_date() {
		return cgrh_cal_start_date;
	}
	public void setCgrh_cal_start_date(Date cgrh_cal_start_date) {
		this.cgrh_cal_start_date = cgrh_cal_start_date;
	}
	public Date getCgrh_cal_end_date() {
		return cgrh_cal_end_date;
	}
	public void setCgrh_cal_end_date(Date cgrh_cal_end_date) {
		this.cgrh_cal_end_date = cgrh_cal_end_date;
	}
	public Float getCgrh_manul_core_hours() {
		return cgrh_manul_core_hours;
	}
	public void setCgrh_manul_core_hours(Float cgrh_manul_core_hours) {
		this.cgrh_manul_core_hours = cgrh_manul_core_hours;
	}
	public Float getCgrh_manul_non_core_hours() {
		return cgrh_manul_non_core_hours;
	}
	public void setCgrh_manul_non_core_hours(Float cgrh_manul_non_core_hours) {
		this.cgrh_manul_non_core_hours = cgrh_manul_non_core_hours;
	}
	public int getCgrh_manul_ind() {
		return cgrh_manul_ind;
	}
	public void setCgrh_manul_ind(int cgrh_manul_ind) {
		this.cgrh_manul_ind = cgrh_manul_ind;
	}
	public Float getCgrh_req_core_hours() {
		return cgrh_req_core_hours;
	}
	public void setCgrh_req_core_hours(Float cgrh_req_core_hours) {
		this.cgrh_req_core_hours = cgrh_req_core_hours;
	}
	public Float getCgrh_req_non_core_hours() {
		return cgrh_req_non_core_hours;
	}
	public void setCgrh_req_non_core_hours(Float cgrh_req_non_core_hours) {
		this.cgrh_req_non_core_hours = cgrh_req_non_core_hours;
	}
	public Float getCgrh_execute_core_hours() {
		return cgrh_execute_core_hours;
	}
	public void setCgrh_execute_core_hours(Float cgrh_execute_core_hours) {
		this.cgrh_execute_core_hours = cgrh_execute_core_hours;
	}
	public Float getCgrh_execute_non_core_hours() {
		return cgrh_execute_non_core_hours;
	}
	public void setCgrh_execute_non_core_hours(Float cgrh_execute_non_core_hours) {
		this.cgrh_execute_non_core_hours = cgrh_execute_non_core_hours;
	}
	public Long getCgrh_create_usr_ent_id() {
		return cgrh_create_usr_ent_id;
	}
	public void setCgrh_create_usr_ent_id(Long cgrh_create_usr_ent_id) {
		this.cgrh_create_usr_ent_id = cgrh_create_usr_ent_id;
	}
	public Date getCgrh_create_datetime() {
		return cgrh_create_datetime;
	}
	public void setCgrh_create_datetime(Date cgrh_create_datetime) {
		this.cgrh_create_datetime = cgrh_create_datetime;
	}
	public Long getCgrh_update_usr_ent_id() {
		return cgrh_update_usr_ent_id;
	}
	public void setCgrh_update_usr_ent_id(Long cgrh_update_usr_ent_id) {
		this.cgrh_update_usr_ent_id = cgrh_update_usr_ent_id;
	}
	public Date getCgrh_update_datetime() {
		return cgrh_update_datetime;
	}
	public void setCgrh_update_datetime(Date cgrh_update_datetime) {
		this.cgrh_update_datetime = cgrh_update_datetime;
	}
}
