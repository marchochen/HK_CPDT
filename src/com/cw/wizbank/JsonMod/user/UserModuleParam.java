package com.cw.wizbank.JsonMod.user;

import com.cw.wizbank.JsonMod.BaseParam;

public class UserModuleParam extends BaseParam {

	private long usr_ent_id;

	// for home page of learner activetab
	private String type;

	// for select language on page
	private String lang;
	
	private String usr_id;
	
	private String usr_email;
	
	private String usr_pwd;
	
	//for sending notify
	
	private String sender_id;
	private String sid;
	
	private long tcr_id;
	private String isPreView;
	private String id_lst;

	public String getId_lst() {
		return id_lst;
	}

	public void setId_lst(String id_lst) {
		this.id_lst = id_lst;
	}

	public String getUsr_email() {
		return usr_email;
	}

	public void setUsr_email(String usr_email) {
		this.usr_email = usr_email;
	}

	public String getUsr_id() {
		return usr_id;
	}

	public void setUsr_id(String usr_id) {
		this.usr_id = usr_id;
	}

	public long getUsr_ent_id() {
		return usr_ent_id;
	}

	public void setUsr_ent_id(long user_id) {
		this.usr_ent_id = user_id;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSender_id() {
		return sender_id;
	}

	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUsr_pwd() {
		return usr_pwd;
	}

	public void setUsr_pwd(String usr_pwd) {
		this.usr_pwd = usr_pwd;
	}
	
	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}
	
	public long getTcr_id() {
		return tcr_id;
	}

	public String getIsPreView() {
		return isPreView;
	}

	public void setIsPreView(String isPreView) {
		this.isPreView = isPreView;
	}


}