package com.cw.wizbank.JsonMod.mobile;

public class MCourse {

	private long id;//即itm_id
	private String name;
	private String content;
	private String path;
	private boolean approval_required;
	private String start_time;
	private String end_time;

	private String app_status;
	private long tkh_id;
	private long cos_id;
	private long mod_id;
	private String cov_status;
	private String cov_last_acc_datetime;
	private String cov_total_time;
	
	public MCourse(){
		setName(null);
		setContent(null);
		setPath(null);
		setApproval_required(false);
		setStart_time(null);
		setEnd_time(null);
		setApp_status(null);
		setTkh_id(0);
		setCos_id(0);
		setCov_status(null);
		setCov_last_acc_datetime(null);
		setCov_total_time(null);
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}
	public void setApproval_required(boolean approval_required) {
		this.approval_required = approval_required;
	}
	public boolean getApproval_required() {
		return approval_required;
	}
	public void setTkh_id(long tkh_id) {
		this.tkh_id = tkh_id;
	}
	public long getTkh_id() {
		return tkh_id;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setApp_status(String app_status) {
		this.app_status = app_status;
	}
	public String getApp_status() {
		return app_status;
	}
	public void setCos_id(long cos_id) {
		this.cos_id = cos_id;
	}
	public long getCos_id() {
		return cos_id;
	}
	public void setCov_status(String cov_status) {
		this.cov_status = cov_status;
	}
	public String getCov_status() {
		return cov_status;
	}
	public void setCov_last_acc_datetime(String cov_last_acc_datetime) {
		this.cov_last_acc_datetime = cov_last_acc_datetime;
	}
	public String getCov_last_acc_datetime() {
		return cov_last_acc_datetime;
	}
	public void setCov_total_time(String cov_total_time) {
		this.cov_total_time = cov_total_time;
	}
	public String getCov_total_time() {
		return cov_total_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setMod_id(long mod_id) {
		this.mod_id = mod_id;
	}
	public long getMod_id() {
		return mod_id;
	}
	
}