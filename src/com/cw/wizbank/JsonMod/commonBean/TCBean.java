package com.cw.wizbank.JsonMod.commonBean;

public class TCBean {
	private long tcr_id;			// 培训中心ID	
	private String tcr_code; // 培训中心编号
	private String tcr_title;		// 培训中心名
	private int tcr_parent_tcr_id;	// 培训中心的父结点
	
	public int getTcr_parent_tcr_id() {
		return tcr_parent_tcr_id;
	}
	public void setTcr_parent_tcr_id(int tcr_parent_tcr_id) {
		this.tcr_parent_tcr_id = tcr_parent_tcr_id;
	}	
	public long getTcr_id() {
		return tcr_id;
	}
	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}
	public String getTcr_title() {
		return tcr_title;
	}
	public void setTcr_title(String tcr_title) {
		this.tcr_title = tcr_title;
	}
	public String getTcr_code() {
		return tcr_code;
	}
	public void setTcr_code(String tcr_code) {
		this.tcr_code = tcr_code;
	}
	
}
