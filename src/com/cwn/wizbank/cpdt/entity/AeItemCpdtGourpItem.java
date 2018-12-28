package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * Desc:课程所属小牌表 课程与小牌的关系表
 *
 */
public class AeItemCpdtGourpItem implements Serializable{
	
	private static final long serialVersionUID = -677431533283780338L;
	/**
	 * 主键ID
	 */
	private long acgi_id;
	/**
	 * 小牌的ID cpdGroup. cr_id
	 */
	private long acgi_cg_id;
	/**
	 * 课程所属的大牌ID aeItemCPDItem.aci_id
	 */
	private long acgi_aci_id;
	/**
	 * 课程ID
	 */
	private long acgi_itm_id;
	/**
	 * 能获得的核心时数
	 */
	private Float acgi_award_core_hours;
	/**
	 * 能获得的非核心时数
	 */
	private Float acgi_award_non_core_hours;
	/**
	 * 创建用户ID reguser.usr_ent_id
	 */
	private long acgi_create_usr_ent_id;
	/**
	 * 创建时间
	 */
	private Date acgi_create_datetime;
	/**
	 * 更新用户ID reguser.usr_ent_id
	 */
	private long acgi_update_usr_ent_id;
	/**
	 * 更新时间
	 */
	private Date acgi_update_datetime;
	
	public long getAcgi_id() {
		return acgi_id;
	}
	public void setAcgi_id(long acgi_id) {
		this.acgi_id = acgi_id;
	}
	public long getAcgi_cg_id() {
		return acgi_cg_id;
	}
	public void setAcgi_cg_id(long acgi_cg_id) {
		this.acgi_cg_id = acgi_cg_id;
	}
	public long getAcgi_aci_id() {
		return acgi_aci_id;
	}
	public void setAcgi_aci_id(long acgi_aci_id) {
		this.acgi_aci_id = acgi_aci_id;
	}
	public long getAcgi_itm_id() {
		return acgi_itm_id;
	}
	public void setAcgi_itm_id(long acgi_itm_id) {
		this.acgi_itm_id = acgi_itm_id;
	}
	public Float getAcgi_award_core_hours() {
		return acgi_award_core_hours;
	}
	public void setAcgi_award_core_hours(Float acgi_award_core_hours) {
		this.acgi_award_core_hours = acgi_award_core_hours;
	}
	public Float getAcgi_award_non_core_hours() {
		return acgi_award_non_core_hours;
	}
	public void setAcgi_award_non_core_hours(Float acgi_award_non_core_hours) {
		this.acgi_award_non_core_hours = acgi_award_non_core_hours;
	}
	public long getAcgi_create_usr_ent_id() {
		return acgi_create_usr_ent_id;
	}
	public void setAcgi_create_usr_ent_id(long acgi_create_usr_ent_id) {
		this.acgi_create_usr_ent_id = acgi_create_usr_ent_id;
	}
	public Date getAcgi_create_datetime() {
		return acgi_create_datetime;
	}
	public void setAcgi_create_datetime(Date acgi_create_datetime) {
		this.acgi_create_datetime = acgi_create_datetime;
	}
	public long getAcgi_update_usr_ent_id() {
		return acgi_update_usr_ent_id;
	}
	public void setAcgi_update_usr_ent_id(long acgi_update_usr_ent_id) {
		this.acgi_update_usr_ent_id = acgi_update_usr_ent_id;
	}
	public Date getAcgi_update_datetime() {
		return acgi_update_datetime;
	}
	public void setAcgi_update_datetime(Date acgi_update_datetime) {
		this.acgi_update_datetime = acgi_update_datetime;
	}
}
