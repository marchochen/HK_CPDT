package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Desc: 课程所属CPD表 课程与CPD的关系表
 */
public class AeItemCpdtItem  implements Serializable{

	private static final long serialVersionUID = -1707854326922908110L;
	/**
	 * 主键
	 */
	private Long aci_id;
	/**
	 * 课程id aeItem.itm_id
	 */
	private Long aci_itm_id;
	/**
	 * accreditation code
	 */
	private String aci_accreditation_code;
	/**
	 * 能获得时数的截止日期
	 */
	private Date aci_hours_end_date;
	/**
	 * 创建用户ID reguser.usr_ent_id
	 */
	private Long aci_create_usr_ent_id;
	/**
	 * 创建时间
	 */
	private Date aci_create_datetime;
	/**
	 * 更新用户ID reguser.usr_ent_id
	 */
	private Long aci_update_usr_ent_id;
	/**
	 * 更新时间
	 */
	private Date aci_update_datetime;
	
	public Long getAci_id() {
		return aci_id;
	}
	public void setAci_id(Long aci_id) {
		this.aci_id = aci_id;
	}
	public Long getAci_itm_id() {
		return aci_itm_id;
	}
	public void setAci_itm_id(Long aci_itm_id) {
		this.aci_itm_id = aci_itm_id;
	}
	public String getAci_accreditation_code() {
		return aci_accreditation_code;
	}
	public void setAci_accreditation_code(String aci_accreditation_code) {
		this.aci_accreditation_code = aci_accreditation_code;
	}
	public Date getAci_hours_end_date() {
		return aci_hours_end_date;
	}
	public void setAci_hours_end_date(Date aci_hours_end_date) {
		this.aci_hours_end_date = aci_hours_end_date;
	}
	public Long getAci_create_usr_ent_id() {
		return aci_create_usr_ent_id;
	}
	public void setAci_create_usr_ent_id(Long aci_create_usr_ent_id) {
		this.aci_create_usr_ent_id = aci_create_usr_ent_id;
	}
	public Date getAci_create_datetime() {
		return aci_create_datetime;
	}
	public void setAci_create_datetime(Date aci_create_datetime) {
		this.aci_create_datetime = aci_create_datetime;
	}
	public Long getAci_update_usr_ent_id() {
		return aci_update_usr_ent_id;
	}
	public void setAci_update_usr_ent_id(Long aci_update_usr_ent_id) {
		this.aci_update_usr_ent_id = aci_update_usr_ent_id;
	}
	public Date getAci_update_datetime() {
		return aci_update_datetime;
	}
	public void setAci_update_datetime(Date aci_update_datetime) {
		this.aci_update_datetime = aci_update_datetime;
	}
	
}
