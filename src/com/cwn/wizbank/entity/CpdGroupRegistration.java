package com.cwn.wizbank.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CpdGroupRegistration implements java.io.Serializable{

	private static final long serialVersionUID = -3424267422743959566L;
	
	private Long cgr_id;
	private Long cgr_usr_ent_id ;
	private Long cgr_cr_id ;

    @DateTimeFormat(pattern="yyyy-MM-dd")   
	private Date cgr_initial_date ;

	private Date cgr_expiry_date ;
	private int cgr_first_ind ;
	
    @DateTimeFormat(pattern="yyyy-MM-dd")   
	private Date cgr_actual_date ;
    
	private Long cgr_create_usr_ent_id ;
	private Date cgr_create_datetime ;
	private Long cgr_update_usr_ent_id ;
	private Date cgr_update_datetime ;
	private String cgr_status ;
	private Long cgr_cg_id;
	
	private CpdGroup cpdGroup;
	private CpdGroupRegHours cpdGroupRegHours;
	
	public Long getCgr_id() {
		return cgr_id;
	}
	public void setCgr_id(Long cgr_id) {
		this.cgr_id = cgr_id;
	}
	public Long getCgr_usr_ent_id() {
		return cgr_usr_ent_id;
	}
	public void setCgr_usr_ent_id(Long cgr_usr_ent_id) {
		this.cgr_usr_ent_id = cgr_usr_ent_id;
	}
	public Long getCgr_cr_id() {
		return cgr_cr_id;
	}
	public void setCgr_cr_id(Long cgr_cr_id) {
		this.cgr_cr_id = cgr_cr_id;
	}
	public Date getCgr_initial_date() {
		return cgr_initial_date;
	}
	public void setCgr_initial_date(Date cgr_initial_date) {
		this.cgr_initial_date = cgr_initial_date;
	}
	public Date getCgr_expiry_date() {
		return cgr_expiry_date;
	}
	public void setCgr_expiry_date(Date cgr_expiry_date) {
		this.cgr_expiry_date = cgr_expiry_date;
	}
	public int getCgr_first_ind() {
		return cgr_first_ind;
	}
	public void setCgr_first_ind(int cgr_first_ind) {
		this.cgr_first_ind = cgr_first_ind;
	}
	public Date getCgr_actual_date() {
		return cgr_actual_date;
	}
	public void setCgr_actual_date(Date cgr_actual_date) {
		this.cgr_actual_date = cgr_actual_date;
	}
	public Long getCgr_create_usr_ent_id() {
		return cgr_create_usr_ent_id;
	}
	public void setCgr_create_usr_ent_id(Long cgr_create_usr_ent_id) {
		this.cgr_create_usr_ent_id = cgr_create_usr_ent_id;
	}
	public Date getCgr_create_datetime() {
		return cgr_create_datetime;
	}
	public void setCgr_create_datetime(Date cgr_create_datetime) {
		this.cgr_create_datetime = cgr_create_datetime;
	}
	public Long getCgr_update_usr_ent_id() {
		return cgr_update_usr_ent_id;
	}
	public void setCgr_update_usr_ent_id(Long cgr_update_usr_ent_id) {
		this.cgr_update_usr_ent_id = cgr_update_usr_ent_id;
	}
	public Date getCgr_update_datetime() {
		return cgr_update_datetime;
	}
	public void setCgr_update_datetime(Date cgr_update_datetime) {
		this.cgr_update_datetime = cgr_update_datetime;
	}
	public String getCgr_status() {
		return cgr_status;
	}
	public void setCgr_status(String cgr_status) {
		this.cgr_status = cgr_status;
	}
	public Long getCgr_cg_id() {
		return cgr_cg_id;
	}
	public void setCgr_cg_id(Long cgr_cg_id) {
		this.cgr_cg_id = cgr_cg_id;
	}
	public CpdGroup getCpdGroup() {
		return cpdGroup;
	}
	public void setCpdGroup(CpdGroup cpdGroup) {
		this.cpdGroup = cpdGroup;
	}
	public CpdGroupRegHours getCpdGroupRegHours() {
		return cpdGroupRegHours;
	}
	public void setCpdGroupRegHours(CpdGroupRegHours cpdGroupRegHours) {
		this.cpdGroupRegHours = cpdGroupRegHours;
	}
	
}
