package com.cw.wizbank.JsonMod.studyGroup.bean;

public class StudyGroupMgtBean {
	private String usr_nickname;
	private String usr_display_bil;
	private String usr_email;
	private long usr_ent_id;
	public long getUsr_ent_id() {
		return usr_ent_id;
	}
	public void setUsr_ent_id(long usr_ent_id) {
		this.usr_ent_id = usr_ent_id;
	}
	public String getUsr_email() {
		return usr_email;
	}
	public void setUsr_email(String usr_email) {
		this.usr_email = usr_email;
	}
	public String getUsr_nickname() {
		return usr_nickname;
	}
	public void setUsr_nickname(String usr_nickname) {
		this.usr_nickname = usr_nickname;
	}
	public String getUsr_display_bil() {
		return usr_display_bil;
	}
	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}
	
}
