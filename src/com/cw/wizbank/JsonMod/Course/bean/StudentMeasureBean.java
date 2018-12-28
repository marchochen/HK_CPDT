package com.cw.wizbank.JsonMod.Course.bean;

public class StudentMeasureBean {
	 private long id;
     private String title;
     private String status;
     private String status_desc_option;
     private boolean offline;
     private String res_title;
     private String res_subtype;
     private long res_id;
     private String mov_status;
     private String res_status;    
     private boolean contri_by_score;
     
	public boolean isContri_by_score() {
		return contri_by_score;
	}
	public void setContri_by_score(boolean contri_by_score) {
		this.contri_by_score = contri_by_score;
	}
	public String getRes_status() {
		return res_status;
	}
	public void setRes_status(String res_status) {
		this.res_status = res_status;
	}
	public String getMov_status() {
		return mov_status;
	}
	public void setMov_status(String mov_status) {
		this.mov_status = mov_status;
	}
	public boolean isOffline() {
		return offline;
	}
	public void setOffline(boolean offline) {
		this.offline = offline;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatus_desc_option() {
		return status_desc_option;
	}
	public void setStatus_desc_option(String status_desc_option) {
		this.status_desc_option = status_desc_option;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getRes_id() {
		return res_id;
	}
	public void setRes_id(long res_id) {
		this.res_id = res_id;
	}
	public String getRes_subtype() {
		return res_subtype;
	}
	public void setRes_subtype(String res_subtype) {
		this.res_subtype = res_subtype;
	}
	public String getRes_title() {
		return res_title;
	}
	public void setRes_title(String res_title) {
		this.res_title = res_title;
	}             
}
