package com.cwn.wizbank.cpd.vo;

import java.util.Date;
import java.util.Map;

import com.cwn.wizbank.entity.CpdLrnAwardRecord;

public class CpdLrnAwardRecordVO implements java.io.Serializable{

	private static final long serialVersionUID = 5866724135590596259L;
	private Long clar_id ;
	private Long clar_usr_ent_id ;
	private Long clar_itm_id ;
	private Long clar_app_id ;
	private int clar_manul_ind ;
	private Long clar_ct_id ;
	private Long clar_cg_id ;
	private Long clar_acgi_id ;
	private Float clar_award_core_hours ;
	private Float clar_award_non_core_hours ;
	private Date clar_award_datetime ;
	private Long clar_create_usr_ent_id ;
	private Date clar_create_datetime ;
	private Long clar_update_usr_ent_id ;
	private Date clar_update_datetime ;
	
	private String usr_ste_usr_id;
	private String usr_display_bil;
	private String usr_group;
	private Map<String,Float> hoursMap;
	public Long getClar_id() {
		return clar_id;
	}
	public void setClar_id(Long clar_id) {
		this.clar_id = clar_id;
	}
	public Long getClar_usr_ent_id() {
		return clar_usr_ent_id;
	}
	public void setClar_usr_ent_id(Long clar_usr_ent_id) {
		this.clar_usr_ent_id = clar_usr_ent_id;
	}
	public Long getClar_itm_id() {
		return clar_itm_id;
	}
	public void setClar_itm_id(Long clar_itm_id) {
		this.clar_itm_id = clar_itm_id;
	}
	public Long getClar_app_id() {
		return clar_app_id;
	}
	public void setClar_app_id(Long clar_app_id) {
		this.clar_app_id = clar_app_id;
	}
	public int getClar_manul_ind() {
		return clar_manul_ind;
	}
	public void setClar_manul_ind(int clar_manul_ind) {
		this.clar_manul_ind = clar_manul_ind;
	}
	public Long getClar_ct_id() {
		return clar_ct_id;
	}
	public void setClar_ct_id(Long clar_ct_id) {
		this.clar_ct_id = clar_ct_id;
	}
	public Long getClar_cg_id() {
		return clar_cg_id;
	}
	public void setClar_cg_id(Long clar_cg_id) {
		this.clar_cg_id = clar_cg_id;
	}
	public Long getClar_acgi_id() {
		return clar_acgi_id;
	}
	public void setClar_acgi_id(Long clar_acgi_id) {
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
	public String getUsr_ste_usr_id() {
		return usr_ste_usr_id;
	}
	public void setUsr_ste_usr_id(String usr_ste_usr_id) {
		this.usr_ste_usr_id = usr_ste_usr_id;
	}
	public String getUsr_display_bil() {
		return usr_display_bil;
	}
	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}
	public String getUsr_group() {
		return usr_group;
	}
	public void setUsr_group(String usr_group) {
		this.usr_group = usr_group;
	}
	public Map<String, Float> getHoursMap() {
		return hoursMap;
	}
	public void setHoursMap(Map<String, Float> hoursMap) {
		this.hoursMap = hoursMap;
	}
	
	public static CpdLrnAwardRecordVO entity2Vo(CpdLrnAwardRecord record){
		CpdLrnAwardRecordVO vo =new CpdLrnAwardRecordVO();
		vo.setClar_id(record.getClar_id());
		vo.setClar_usr_ent_id(record.getClar_usr_ent_id());
		vo.setClar_itm_id(record.getClar_itm_id());
		vo.setClar_app_id(record.getClar_app_id());
		vo.setClar_manul_ind(record.getClar_manul_ind());
		vo.setClar_ct_id(record.getClar_ct_id());
		vo.setClar_cg_id(record.getClar_cg_id());
		vo.setClar_acgi_id(record.getClar_acgi_id());
		vo.setClar_award_core_hours(record.getClar_award_core_hours());
		vo.setClar_award_non_core_hours(record.getClar_award_non_core_hours());
		vo.setClar_award_datetime(record.getClar_award_datetime());
		vo.setClar_create_usr_ent_id(record.getClar_create_usr_ent_id());
		vo.setClar_create_datetime(record.getClar_create_datetime());
		vo.setClar_update_datetime(record.getClar_update_datetime());
		vo.setClar_update_usr_ent_id(record.getClar_update_usr_ent_id());
		vo.setUsr_ste_usr_id(record.getUsr_ste_usr_id());
		vo.setUsr_display_bil(record.getUsr_display_bil());
		vo.setUsr_group(record.getUsg_name());
		return vo;
	}
}
