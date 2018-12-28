package com.cwn.wizbank.entity;

import java.util.Date;

public class AeItemCPDGourpItem implements java.io.Serializable{
	
	private static final long serialVersionUID = 6605328963994374685L;
	
	private Long acgi_id ;
	private Long acgi_cg_id ;
	private Long acgi_aci_id ;
	private Long acgi_itm_id ;
	private Float acgi_award_core_hours ;
	private Float acgi_award_non_core_hours ;
	private Long acgi_create_usr_ent_id ;
	private Date acgi_create_datetime ;
	private Long acgi_update_usr_ent_id ;
	private Date acgi_update_datetime ;
	
	private AeItemCPDItem aeItemCPDItem;
	private CpdGroup cpdGroup;
	
	public Long getAcgi_id() {
		return acgi_id;
	}
	public void setAcgi_id(Long acgi_id) {
		this.acgi_id = acgi_id;
	}
	public Long getAcgi_cg_id() {
		return acgi_cg_id;
	}
	public void setAcgi_cg_id(Long acgi_cg_id) {
		this.acgi_cg_id = acgi_cg_id;
	}
	public Long getAcgi_aci_id() {
		return acgi_aci_id;
	}
	public void setAcgi_aci_id(Long acgi_aci_id) {
		this.acgi_aci_id = acgi_aci_id;
	}
	public Long getAcgi_itm_id() {
		return acgi_itm_id;
	}
	public void setAcgi_itm_id(Long acgi_itm_id) {
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
	public Long getAcgi_create_usr_ent_id() {
		return acgi_create_usr_ent_id;
	}
	public void setAcgi_create_usr_ent_id(Long acgi_create_usr_ent_id) {
		this.acgi_create_usr_ent_id = acgi_create_usr_ent_id;
	}
	public Date getAcgi_create_datetime() {
		return acgi_create_datetime;
	}
	public void setAcgi_create_datetime(Date acgi_create_datetime) {
		this.acgi_create_datetime = acgi_create_datetime;
	}
	public Long getAcgi_update_usr_ent_id() {
		return acgi_update_usr_ent_id;
	}
	public void setAcgi_update_usr_ent_id(Long acgi_update_usr_ent_id) {
		this.acgi_update_usr_ent_id = acgi_update_usr_ent_id;
	}
	public Date getAcgi_update_datetime() {
		return acgi_update_datetime;
	}
	public void setAcgi_update_datetime(Date acgi_update_datetime) {
		this.acgi_update_datetime = acgi_update_datetime;
	}
	public AeItemCPDItem getAeItemCPDItem() {
		return aeItemCPDItem;
	}
	public void setAeItemCPDItem(AeItemCPDItem aeItemCPDItem) {
		this.aeItemCPDItem = aeItemCPDItem;
	}
	public CpdGroup getCpdGroup() {
		return cpdGroup;
	}
	public void setCpdGroup(CpdGroup cpdGroup) {
		this.cpdGroup = cpdGroup;
	}
	
}
