package com.cw.wizbank.JsonMod.user.bean;

import java.sql.Timestamp;

public class SkillEvaluationBean {

	private long asm_id;
	private String asm_title;
	private String asm_type;
	private String asu_type;
	private Timestamp asm_eff_end_datetime;
	private String assessee;
	private long asu_sks_skb_id;

	public long getAsm_id() {
		return asm_id;
	}

	public void setAsm_id(long asm_id) {
		this.asm_id = asm_id;
	}

	public String getAsm_title() {
		return asm_title;
	}

	public void setAsm_title(String asm_title) {
		this.asm_title = asm_title;
	}

	public String getAsm_type() {
		return asm_type;
	}

	public void setAsm_type(String asm_type) {
		this.asm_type = asm_type;
	}

	public String getAsu_type() {
		return asu_type;
	}

	public void setAsu_type(String asu_type) {
		this.asu_type = asu_type;
	}

	public Timestamp getAsm_eff_end_datetime() {
		return asm_eff_end_datetime;
	}

	public void setAsm_eff_end_datetime(Timestamp asm_eff_end_datetime) {
		this.asm_eff_end_datetime = asm_eff_end_datetime;
	}

	public String getAssessee() {
		return assessee;
	}

	public void setAssessee(String assessee) {
		this.assessee = assessee;
	}

	public long getAsu_sks_skb_id() {
		return asu_sks_skb_id;
	}

	public void setAsu_sks_skb_id(long asu_sks_skb_id) {
		this.asu_sks_skb_id = asu_sks_skb_id;
	}
	
}
