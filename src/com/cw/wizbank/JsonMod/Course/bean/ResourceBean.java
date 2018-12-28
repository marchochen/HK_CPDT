package com.cw.wizbank.JsonMod.Course.bean;

import java.sql.Timestamp;

public class ResourceBean {
	private long id;
	private String title;
	private String type;
	private String subtype;
	private String status;
	private String privilege;
	private long parent_item_id;
	private String parent_item_title;
	private Timestamp eff_start_datetime;
	private Timestamp eff_ent_datetime;
	private Timestamp cur_time;
	private Timestamp timestamp;
	private long tkh_id;
	private String itm_icon;
	private String itm_dummy_type;
	private String content_status;
	private String cov_status;
	private Timestamp itm_content_eff_start_datetime;
	private Timestamp itm_content_eff_end_datetime;
	private boolean exam_ind;
	private String itm_desc;
	private String ies_audience;
	private String ies_duration;
	private long itm_content_eff_duration;

	public boolean isExam_ind() {
		return exam_ind;
	}

	public void setExam_ind(boolean exam_ind) {
		this.exam_ind = exam_ind;
	}

	public Timestamp getItm_content_eff_end_datetime() {
		return itm_content_eff_end_datetime;
	}

	public void setItm_content_eff_end_datetime(Timestamp itm_content_eff_end_datetime) {
		this.itm_content_eff_end_datetime = itm_content_eff_end_datetime;
	}

	public Timestamp getItm_content_eff_start_datetime() {
		return itm_content_eff_start_datetime;
	}

	public void setItm_content_eff_start_datetime(Timestamp itm_content_eff_start_datetime) {
		this.itm_content_eff_start_datetime = itm_content_eff_start_datetime;
	}

	public String getCov_status() {
		return cov_status;
	}

	public void setCov_status(String cov_status) {
		this.cov_status = cov_status;
	}

	public String getContent_status() {
		return content_status;
	}

	public void setContent_status(String content_status) {
		this.content_status = content_status;
	}

	public long getTkh_id() {
		return tkh_id;
	}

	public void setTkh_id(long tkh_id) {
		this.tkh_id = tkh_id;
	}

	public Timestamp getCur_time() {
		return cur_time;
	}

	public void setCur_time(Timestamp cur_time) {
		this.cur_time = cur_time;
	}

	public Timestamp getEff_ent_datetime() {
		return eff_ent_datetime;
	}

	public void setEff_ent_datetime(Timestamp eff_ent_datetime) {
		this.eff_ent_datetime = eff_ent_datetime;
	}

	public Timestamp getEff_start_datetime() {
		return eff_start_datetime;
	}

	public void setEff_start_datetime(Timestamp eff_start_datetime) {
		this.eff_start_datetime = eff_start_datetime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParent_item_id() {
		return parent_item_id;
	}

	public void setParent_item_id(long parent_item_id) {
		this.parent_item_id = parent_item_id;
	}

	public String getParent_item_title() {
		return parent_item_title;
	}

	public void setParent_item_title(String parent_item_title) {
		this.parent_item_title = parent_item_title;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItm_dummy_type() {
		return itm_dummy_type;
	}

	public void setItm_dummy_type(String itm_dummy_type) {
		this.itm_dummy_type = itm_dummy_type;
	}

	public String getItm_icon() {
		return itm_icon;
	}

	public void setItm_icon(String itm_icon) {
		this.itm_icon = itm_icon;
	}

	public String getIes_audience() {
		return ies_audience;
	}

	public void setIes_audience(String ies_audience) {
		this.ies_audience = ies_audience;
	}

	public String getItm_desc() {
		return itm_desc;
	}

	public void setItm_desc(String itm_desc) {
		this.itm_desc = itm_desc;
	}

	public String getIes_duration() {
		return ies_duration;
	}

	public void setIes_duration(String ies_duration) {
		this.ies_duration = ies_duration;
	}
	public long getItm_content_eff_duration() {
		return itm_content_eff_duration;
	}

	public void setItm_content_eff_duration(long itmContentEffDuration) {
		this.itm_content_eff_duration = itmContentEffDuration;
	}
}
