package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * Desc : CPT/D牌照组別（小牌）周期
 */
public class CpdtGroupPeriod implements Serializable{
	
	private static final long serialVersionUID = -852036813334578319L;
	/**
	 * 主键
	 */
	private Long cgp_id; 
	/**
	 * 生效时间(yyyy-mm-dd) 生效年份不能重复对于生效日期的月及日，是自动从大牌中设置的生效月份带出来
	 */
	private Date cgp_effective_time;
	/**
	 * 大牌的ID cpdType. ct_id
	 */
	private Long cgp_ct_id;
	/**
	 * 小牌ID cpdGroup.cg_id
	 */
	private Long cgp_cg_id;
	/**
	 * 创建用户ID reguser.usr_ent_id
	 */
	private Long cgp_create_usr_ent_id;
	/**
	 * 创建时间
	 */
	private Date cgp_create_datetime;
	/**
	 * 更新用户ID reguser.usr_ent_id
	 */
	private Long cgp_update_usr_ent_id;
	/**
	 * 更新时间
	 */
	private Date cgp_update_datetime;
	/**
	 * 状态(OK : 正常 DEL：已删除)
	 */
	private String cgp_status;
	
	/**
	 * 该时期里面的需要时数集合
	 */
	private List<CpdtGroupHours> cpdGroupHours;

	public Long getCgp_id() {
		return cgp_id;
	}
	public void setCgp_id(Long cgp_id) {
		this.cgp_id = cgp_id;
	}
	public Date getCgp_effective_time() {
		return cgp_effective_time;
	}
	public void setCgp_effective_time(Date cgp_effective_time) {
		this.cgp_effective_time = cgp_effective_time;
	}
	public Long getCgp_ct_id() {
		return cgp_ct_id;
	}
	public void setCgp_ct_id(Long cgp_ct_id) {
		this.cgp_ct_id = cgp_ct_id;
	}
	public Long getCgp_cg_id() {
		return cgp_cg_id;
	}
	public void setCgp_cg_id(Long cgp_cg_id) {
		this.cgp_cg_id = cgp_cg_id;
	}
	public Long getCgp_create_usr_ent_id() {
		return cgp_create_usr_ent_id;
	}
	public void setCgp_create_usr_ent_id(Long cgp_create_usr_ent_id) {
		this.cgp_create_usr_ent_id = cgp_create_usr_ent_id;
	}
	public Date getCgp_create_datetime() {
		return cgp_create_datetime;
	}
	public void setCgp_create_datetime(Date cgp_create_datetime) {
		this.cgp_create_datetime = cgp_create_datetime;
	}
	public Long getCgp_update_usr_ent_id() {
		return cgp_update_usr_ent_id;
	}
	public void setCgp_update_usr_ent_id(Long cgp_update_usr_ent_id) {
		this.cgp_update_usr_ent_id = cgp_update_usr_ent_id;
	}
	public Date getCgp_update_datetime() {
		return cgp_update_datetime;
	}
	public void setCgp_update_datetime(Date cgp_update_datetime) {
		this.cgp_update_datetime = cgp_update_datetime;
	}
	public String getCgp_status() {
		return cgp_status;
	}
	public void setCgp_status(String cgp_status) {
		this.cgp_status = cgp_status;
	}
	public List<CpdtGroupHours> getCpdGroupHours() {
		return cpdGroupHours;
	}
	public void setCpdGroupHours(List<CpdtGroupHours> cpdGroupHours) {
		this.cpdGroupHours = cpdGroupHours;
	}


}
