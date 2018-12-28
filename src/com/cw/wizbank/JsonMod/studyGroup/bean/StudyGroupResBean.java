package com.cw.wizbank.JsonMod.studyGroup.bean;

import java.sql.Timestamp;

public class StudyGroupResBean {
	private long sgs_id;
	private String sgs_title;
	private String sgs_title_noescape;
	private String sgs_type;
	private String sgs_content;
	private String sgs_content_noescape;
	private String sgs_desc;
	private String sgs_desc_noescape;
	private Timestamp sgs_upd_timestamp;
	private boolean is_creator;
	private long sgr_sgp_id;
	
	public long getSgr_sgp_id() {
		return sgr_sgp_id;
	}
	public void setSgr_sgp_id(long sgr_sgp_id) {
		this.sgr_sgp_id = sgr_sgp_id;
	}
	public boolean isIs_creator() {
		return is_creator;
	}
	public void setIs_creator(boolean is_creator) {
		this.is_creator = is_creator;
	}
	public String getSgs_content() {
		return sgs_content;
	}
	public void setSgs_content(String sgs_content) {
		this.sgs_content = sgs_content;
	}
	public String getSgs_desc() {
		return sgs_desc;
	}
	public void setSgs_desc(String sgs_desc) {
		this.sgs_desc = sgs_desc;
	}
	public long getSgs_id() {
		return sgs_id;
	}
	public void setSgs_id(long sgs_id) {
		this.sgs_id = sgs_id;
	}

	public String getSgs_title() {
		return sgs_title;
	}
	public void setSgs_title(String sgs_title) {
		this.sgs_title = sgs_title;
	}
	public String getSgs_type() {
		return sgs_type;
	}
	public void setSgs_type(String sgs_type) {
		this.sgs_type = sgs_type;
	}
	public Timestamp getSgs_upd_timestamp() {
		return sgs_upd_timestamp;
	}
	public void setSgs_upd_timestamp(Timestamp sgs_upd_timestamp) {
		this.sgs_upd_timestamp = sgs_upd_timestamp;
	}
	public String getSgs_title_noescape() {
		return sgs_title_noescape;
	}
	public void setSgs_title_noescape(String sgs_title_noescape) {
		this.sgs_title_noescape = sgs_title_noescape;
	}
	public String getSgs_content_noescape() {
		return sgs_content_noescape;
	}
	public void setSgs_content_noescape(String sgs_content_noescape) {
		this.sgs_content_noescape = sgs_content_noescape;
	}
	public String getSgs_desc_noescape() {
		return sgs_desc_noescape;
	}
	public void setSgs_desc_noescape(String sgs_desc_noescape) {
		this.sgs_desc_noescape = sgs_desc_noescape;
	}
}
