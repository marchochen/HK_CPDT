package com.cwn.wizbank.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class AeItemCPDItem implements java.io.Serializable{

	private static final long serialVersionUID = -762852430566403538L;
	
	private Long aci_id ;
	private Long aci_itm_id ;
	private String aci_accreditation_code ;
	@DateTimeFormat(pattern="yyyy-MM-dd")  
	private Date aci_hours_end_date ;
	private Long aci_create_usr_ent_id ;
	private Date aci_create_datetime ;
	private Long aci_update_usr_ent_id ;
	private Date aci_update_datetime ;
	
	private AeItemCPDItem aeItemCPDItem;
	
	private AeItemCPDGourpItem[]  aeCPDGourpItemList;
	
	//额外新增的属性
	private String itm_code; //课程code
	private String itm_name; //课程  > 班级
	
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
	public AeItemCPDItem getAeItemCPDItem() {
		return aeItemCPDItem;
	}
	public void setAeItemCPDItem(AeItemCPDItem aeItemCPDItem) {
		this.aeItemCPDItem = aeItemCPDItem;
	}
	public AeItemCPDGourpItem[] getAeCPDGourpItemList() {
		return aeCPDGourpItemList;
	}
	public void setAeCPDGourpItemList(AeItemCPDGourpItem[] aeCPDGourpItemList) {
		this.aeCPDGourpItemList = aeCPDGourpItemList;
	}
	public String getItm_code() {
		return itm_code;
	}
	public void setItm_code(String itm_code) {
		this.itm_code = itm_code;
	}
	public String getItm_name() {
		return itm_name;
	}
	public void setItm_name(String itm_name) {
		this.itm_name = itm_name;
	}

}
