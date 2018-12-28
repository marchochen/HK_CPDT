package com.cw.wizbank.JsonMod.Course.bean;

public class CCRBean {
	private long ccr_id;
	private float ccr_pass_score;
	private int att_rate;
	private int ccr_attendance_rate;
	private String ccr_offline_condition;
	
	private boolean ccr_pass_ind;
	public int getAtt_rate() {
		return att_rate;
	}
	public void setAtt_rate(int att_rate) {
		this.att_rate = att_rate;
	}
	public int getCcr_attendance_rate() {
		return ccr_attendance_rate;
	}
	public void setCcr_attendance_rate(int ccr_attendance_rate) {
		this.ccr_attendance_rate = ccr_attendance_rate;
	}
	public long getCcr_id() {
		return ccr_id;
	}
	public void setCcr_id(long ccr_id) {
		this.ccr_id = ccr_id;
	}
	public String getCcr_offline_condition() {
		return ccr_offline_condition;
	}
	public void setCcr_offline_condition(String ccr_offline_condition) {
		this.ccr_offline_condition = ccr_offline_condition;
	}
	public float getCcr_pass_score() {
		return ccr_pass_score;
	}
	public void setCcr_pass_score(float ccr_pass_score) {
		this.ccr_pass_score = ccr_pass_score;
	}
	public boolean isCcr_pass_ind() {
		return ccr_pass_ind;
	}
	public void setCcr_pass_ind(boolean ccr_pass_ind) {
		this.ccr_pass_ind = ccr_pass_ind;
	}

}
