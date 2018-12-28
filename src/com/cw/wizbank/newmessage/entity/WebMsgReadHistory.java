package com.cw.wizbank.newmessage.entity;

import java.sql.Timestamp;

public class WebMsgReadHistory {

	long wmrh_wmsg_id;
	String wmrh_status;
	Timestamp wmrh_read_datetime;
	
	public long getWmrh_wmsg_id() {
		return wmrh_wmsg_id;
	}
	public void setWmrh_wmsg_id(long wmrh_wmsg_id) {
		this.wmrh_wmsg_id = wmrh_wmsg_id;
	}
	public String getWmrh_status() {
		return wmrh_status;
	}
	public void setWmrh_status(String wmrh_status) {
		this.wmrh_status = wmrh_status;
	}
	public Timestamp getWmrh_read_datetime() {
		return wmrh_read_datetime;
	}
	public void setWmrh_read_datetime(Timestamp wmrh_read_datetime) {
		this.wmrh_read_datetime = wmrh_read_datetime;
	}

}
