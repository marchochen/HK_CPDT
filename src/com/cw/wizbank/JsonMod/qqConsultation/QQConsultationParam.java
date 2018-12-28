package com.cw.wizbank.JsonMod.qqConsultation;

import com.cw.wizbank.JsonMod.tcrCommon.TcrParam;

public class QQConsultationParam extends TcrParam {
	private String operating = "";
	private long cpq_id;
	private String cpq_code;
	private String cpq_title;
	private String cpq_number;
	private String cpq_desc;
	private long[] cpq_id_lst;
	
	public String getOperating() {
		return operating;
	}
	public void setOperating(String operating) {
		this.operating = operating;
	}
	public long getCpq_id() {
		return cpq_id;
	}
	public void setCpq_id(long cpq_id) {
		this.cpq_id = cpq_id;
	}
	public String getCpq_code() {
		return cpq_code;
	}
	public void setCpq_code(String cpq_code) {
		this.cpq_code = cpq_code;
	}
	public String getCpq_title() {
		return cpq_title;
	}
	public void setCpq_title(String cpq_title) {
		this.cpq_title = cpq_title;
	}
	public String getCpq_number() {
		return cpq_number;
	}
	public void setCpq_number(String cpq_number) {
		this.cpq_number = cpq_number;
	}
	public String getCpq_desc() {
		return cpq_desc;
	}
	public void setCpq_desc(String cpq_desc) {
		this.cpq_desc = cpq_desc;
	}
	public void setCpq_id_lst(long[] cpq_id_lst) {
		this.cpq_id_lst = cpq_id_lst;
	}
	public long[] getCpq_id_lst() {
		return cpq_id_lst;
	}
}
