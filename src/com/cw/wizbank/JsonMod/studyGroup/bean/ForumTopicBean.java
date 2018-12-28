package com.cw.wizbank.JsonMod.studyGroup.bean;

public class ForumTopicBean {
	private long fto_id;
	private long fto_res_id;
	private String fto_title;
	private long mod_id;
	public long getMod_id() {
		return mod_id;
	}
	public void setMod_id(long mod_id) {
		this.mod_id = mod_id;
	}
	public long getFto_id() {
		return fto_id;
	}
	public void setFto_id(long fto_id) {
		this.fto_id = fto_id;
	}
	public String getFto_title() {
		return fto_title;
	}
	public void setFto_title(String fto_title) {
		this.fto_title = fto_title;
	}
	public long getFto_res_id() {
		return fto_res_id;
	}
	public void setFto_res_id(long fto_res_id) {
		this.fto_res_id = fto_res_id;
	}
}
