package com.cw.wizbank.newmessage.entity;

import java.sql.Timestamp;

public class EmailMsgRecHistory {

	long emrh_emsg_id;
	String emrh_status;
	Timestamp emrh_sent_datetime;
	long emrh_attempted;

	public static String SEND_TYPE_YES = "Y";
	public static String SEND_TYPE_NO = "N";
	
	public long getEmrh_emsg_id() {
		return emrh_emsg_id;
	}
	public void setEmrh_emsg_id(long emrh_emsg_id) {
		this.emrh_emsg_id = emrh_emsg_id;
	}
	public String getEmrh_status() {
		return emrh_status;
	}
	public void setEmrh_status(String emrh_status) {
		this.emrh_status = emrh_status;
	}
	public Timestamp getEmrh_sent_datetime() {
		return emrh_sent_datetime;
	}
	public void setEmrh_sent_datetime(Timestamp emrh_sent_datetime) {
		this.emrh_sent_datetime = emrh_sent_datetime;
	}
	public long getEmrh_attempted() {
		return emrh_attempted;
	}
	public void setEmrh_attempted(long emrh_attempted) {
		this.emrh_attempted = emrh_attempted;
	}

}
