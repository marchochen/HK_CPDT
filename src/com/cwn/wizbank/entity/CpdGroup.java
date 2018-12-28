package com.cwn.wizbank.entity;

import java.util.Date;

public class CpdGroup implements java.io.Serializable{

	private static final long serialVersionUID = 481508853213188842L;
	
	private Long cg_id ;
	private String cg_code ;
	private String cg_alias;
	private Integer cg_display_order;
	private Integer cg_contain_non_core_ind ;
	private Integer cg_display_in_report_ind;
	private Long cg_ct_id;
	private Long cg_create_usr_ent_id ;
	private Date cg_create_datetime ;
	private Long cg_update_usr_ent_id ;
	private Date cg_update_datetime ;
	private String cg_status ;
	
	private CpdType  cpdType;
	
    private CpdGroupRegistration  cpdGroupRegistration;
	
	public Long getCg_id() {
		return cg_id;
	}
	public void setCg_id(Long cg_id) {
		this.cg_id = cg_id;
	}
	public String getCg_code() {
		return cg_code;
	}
	public void setCg_code(String cg_code) {
		this.cg_code = cg_code;
	}
	public String getCg_alias() {
		return cg_alias;
	}
	public void setCg_alias(String cg_alias) {
		this.cg_alias = cg_alias;
	}
	public Integer getCg_display_order() {
		return cg_display_order;
	}
	public void setCg_display_order(Integer cg_display_order) {
		this.cg_display_order = cg_display_order;
	}
	public Integer getCg_contain_non_core_ind() {
		return cg_contain_non_core_ind;
	}
	public void setCg_contain_non_core_ind(Integer cg_contain_non_core_ind) {
		this.cg_contain_non_core_ind = cg_contain_non_core_ind;
	}
	public Integer getCg_display_in_report_ind() {
		return cg_display_in_report_ind;
	}
	public void setCg_display_in_report_ind(Integer cg_display_in_report_ind) {
		this.cg_display_in_report_ind = cg_display_in_report_ind;
	}
	public Long getCg_ct_id() {
		return cg_ct_id;
	}
	public void setCg_ct_id(Long cg_ct_id) {
		this.cg_ct_id = cg_ct_id;
	}
	public Long getCg_create_usr_ent_id() {
		return cg_create_usr_ent_id;
	}
	public void setCg_create_usr_ent_id(Long cg_create_usr_ent_id) {
		this.cg_create_usr_ent_id = cg_create_usr_ent_id;
	}
	public Date getCg_create_datetime() {
		return cg_create_datetime;
	}
	public void setCg_create_datetime(Date cg_create_datetime) {
		this.cg_create_datetime = cg_create_datetime;
	}
	public Long getCg_update_usr_ent_id() {
		return cg_update_usr_ent_id;
	}
	public void setCg_update_usr_ent_id(Long cg_update_usr_ent_id) {
		this.cg_update_usr_ent_id = cg_update_usr_ent_id;
	}
	public Date getCg_update_datetime() {
		return cg_update_datetime;
	}
	public void setCg_update_datetime(Date cg_update_datetime) {
		this.cg_update_datetime = cg_update_datetime;
	}
	public String getCg_status() {
		return cg_status;
	}
	public void setCg_status(String cg_status) {
		this.cg_status = cg_status;
	}
	public CpdType getCpdType() {
		return cpdType;
	}
	public void setCpdType(CpdType cpdType) {
		this.cpdType = cpdType;
	}
    public CpdGroupRegistration getCpdGroupRegistration() {
        return cpdGroupRegistration;
    }
    public void setCpdGroupRegistration(CpdGroupRegistration cpdGroupRegistration) {
        this.cpdGroupRegistration = cpdGroupRegistration;
    }
	
	
}
