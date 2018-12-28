package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * Desc:学员获得CPD的记录表
 */
public class CpdtLrnAwardRecord implements Serializable{
	
	private static final long serialVersionUID = 8147595131317996601L;
	/**
	 * 主键ID
	 */
	private long clar_id;
	/**
	 * 关联用户ID reguser.usr_ent_id
	 */
	private long clar_usr_ent_id;
	/**
	 * 关联课程ID aeItem.itm_id
	 */
	private long clar_itm_id;
	/**
	 * 关联出席记录ID aeApplication.app_id
	 */
	private long clar_app_id;
	/**
	 * 是否手动修改过
	 */
	private int clar_manul_ind;
	/**
	 * 大牌的ID（冗余数据） cpdType. ct_id
	 */
	private long clar_ct_id;
	/**
	 * 小牌的ID（冗余数据） cpdGroup. cr_id
	 */
	private long clar_cg_id;
	/**
	 * 课程所属CPD ID aeItemCPDGourpItem.acgi_id
	 */
	private long clar_acgi_id;
	/**
	 * 获得的核心时数
	 */
	private Float clar_award_core_hours;
	/**
	 * 获得的非核心时数
	 */
	private Float clar_award_non_core_hours;
	/**
	 * 获得分数的数据 这里指的是课程的结训时间
	 */
	private Date clar_award_datetime;
	/**
	 * 创建用户ID reguser.usr_ent_id
	 */
	private Long clar_create_usr_ent_id;
	/**
	 * 创建时间
	 */
	private Date clar_create_datetime;
	/**
	 * 更新用户ID reguser.usr_ent_id
	 */
	private Long clar_update_usr_ent_id;
	/**
	 * 更新时间
	 */
	private Date clar_update_datetime;
	
	
	public long getClar_id() {
		return clar_id;
	}
	public void setClar_id(long clar_id) {
		this.clar_id = clar_id;
	}
	public long getClar_usr_ent_id() {
		return clar_usr_ent_id;
	}
	public void setClar_usr_ent_id(long clar_usr_ent_id) {
		this.clar_usr_ent_id = clar_usr_ent_id;
	}
	public long getClar_itm_id() {
		return clar_itm_id;
	}
	public void setClar_itm_id(long clar_itm_id) {
		this.clar_itm_id = clar_itm_id;
	}
	public long getClar_app_id() {
		return clar_app_id;
	}
	public void setClar_app_id(long clar_app_id) {
		this.clar_app_id = clar_app_id;
	}
	public int getClar_manul_ind() {
		return clar_manul_ind;
	}
	public void setClar_manul_ind(int clar_manul_ind) {
		this.clar_manul_ind = clar_manul_ind;
	}
	public long getClar_ct_id() {
		return clar_ct_id;
	}
	public void setClar_ct_id(long clar_ct_id) {
		this.clar_ct_id = clar_ct_id;
	}
	public long getClar_cg_id() {
		return clar_cg_id;
	}
	public void setClar_cg_id(long clar_cg_id) {
		this.clar_cg_id = clar_cg_id;
	}
	public long getClar_acgi_id() {
		return clar_acgi_id;
	}
	public void setClar_acgi_id(long clar_acgi_id) {
		this.clar_acgi_id = clar_acgi_id;
	}
	public Float getClar_award_core_hours() {
		return clar_award_core_hours;
	}
	public void setClar_award_core_hours(Float clar_award_core_hours) {
		this.clar_award_core_hours = clar_award_core_hours;
	}
	public Float getClar_award_non_core_hours() {
		return clar_award_non_core_hours;
	}
	public void setClar_award_non_core_hours(Float clar_award_non_core_hours) {
		this.clar_award_non_core_hours = clar_award_non_core_hours;
	}
	public Date getClar_award_datetime() {
		return clar_award_datetime;
	}
	public void setClar_award_datetime(Date clar_award_datetime) {
		this.clar_award_datetime = clar_award_datetime;
	}
	public Long getClar_create_usr_ent_id() {
		return clar_create_usr_ent_id;
	}
	public void setClar_create_usr_ent_id(Long clar_create_usr_ent_id) {
		this.clar_create_usr_ent_id = clar_create_usr_ent_id;
	}
	public Date getClar_create_datetime() {
		return clar_create_datetime;
	}
	public void setClar_create_datetime(Date clar_create_datetime) {
		this.clar_create_datetime = clar_create_datetime;
	}
	public Long getClar_update_usr_ent_id() {
		return clar_update_usr_ent_id;
	}
	public void setClar_update_usr_ent_id(Long clar_update_usr_ent_id) {
		this.clar_update_usr_ent_id = clar_update_usr_ent_id;
	}
	public Date getClar_update_datetime() {
		return clar_update_datetime;
	}
	public void setClar_update_datetime(Date clar_update_datetime) {
		this.clar_update_datetime = clar_update_datetime;
	}
}
