package com.cw.wizbank.JsonMod.Course.bean;

public class PreModuleBean {
	private boolean complete_pre;
	private long id;
	private String  checked_status;
	private String pre_res_title;
	
	public String getChecked_status() {
		return checked_status;
	}
	public void setChecked_status(String checked_status) {
		this.checked_status = checked_status;
	}
	public boolean isComplete_pre() {
		return complete_pre;
	}
	public void setComplete_pre(boolean complete_pre) {
		this.complete_pre = complete_pre;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPre_res_title() {
		return pre_res_title;
	}
	public void setPre_res_title(String pre_res_title) {
		this.pre_res_title = pre_res_title;
	}

}
