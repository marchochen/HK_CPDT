package com.cwn.wizbank.entity;

import java.util.Date;

public class UsrPwdResetHis implements java.io.Serializable {
	
	private static final long serialVersionUID = -6128943389998845333L;
	
	Long prh_id;
	
	Long prh_ent_id;
	
	String prh_ip;
	
	String prh_status;
	
	Long prh_attempted;
	
	Date prh_create_timestamp;
	
	RegUser user;

	public Long getPrh_id() {
		return prh_id;
	}

	public void setPrh_id(Long prh_id) {
		this.prh_id = prh_id;
	}

	public Long getPrh_ent_id() {
		return prh_ent_id;
	}

	public void setPrh_ent_id(Long prh_ent_id) {
		this.prh_ent_id = prh_ent_id;
	}

	public String getPrh_ip() {
		return prh_ip;
	}

	public void setPrh_ip(String prh_ip) {
		this.prh_ip = prh_ip;
	}

	public String getPrh_status() {
		return prh_status;
	}

	public void setPrh_status(String prh_status) {
		this.prh_status = prh_status;
	}

	public Long getPrh_attempted() {
		return prh_attempted;
	}

	public void setPrh_attempted(Long prh_attempted) {
		this.prh_attempted = prh_attempted;
	}

	public Date getPrh_create_timestamp() {
		return prh_create_timestamp;
	}

	public void setPrh_create_timestamp(Date prh_create_timestamp) {
		this.prh_create_timestamp = prh_create_timestamp;
	}

	public RegUser getUser() {
		return user;
	}

	public void setUser(RegUser user) {
		this.user = user;
	}
	
}
