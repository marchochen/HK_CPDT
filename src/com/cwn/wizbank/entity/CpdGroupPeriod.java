package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class CpdGroupPeriod implements java.io.Serializable{

	private static final long serialVersionUID = 1423619340809017474L;

	private Long cgp_id ;

    @DateTimeFormat(pattern="yyyy-MM-dd")   
	private Date cgp_effective_time ;
    
	private Long cgp_ct_id ;
	private Long cgp_cg_id ;
	private Long cgp_create_usr_ent_id ;
	private Date cgp_create_datetime ;
	private Long cgp_update_usr_ent_id ;
	private Date cgp_update_datetime ;
	private String cgp_status ;
	
	private List<CpdGroupHours> cpdGroupHours; //该时期里面的需要时数集合
	
	//额外新增属性
	private String ct_name; //大牌名称
	private String cg_name; //小牌名称
	private String period; //修改的周期
	
	
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
	public List<CpdGroupHours> getCpdGroupHours() {
		return cpdGroupHours;
	}
	public void setCpdGroupHours(List<CpdGroupHours> cpdGroupHours) {
		this.cpdGroupHours = cpdGroupHours;
	}
	public String getCt_name() {
		return ct_name;
	}
	public void setCt_name(String ct_name) {
		this.ct_name = ct_name;
	}
	public String getCg_name() {
		return cg_name;
	}
	public void setCg_name(String cg_name) {
		this.cg_name = cg_name;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	
	
}
