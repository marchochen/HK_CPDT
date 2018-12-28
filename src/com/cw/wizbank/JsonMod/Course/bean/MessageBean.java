package com.cw.wizbank.JsonMod.Course.bean;

import java.sql.Timestamp;

public class MessageBean {
	private long msg_id;
	private String msg_type;
	private String msg_title;
    private String msg_body;
    private Timestamp msg_begin_date;
    private Timestamp msg_end_date;
    private Timestamp msg_update_date;
    
    public String getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	public Timestamp getMsg_begin_date() {
		return msg_begin_date;
	}
	public void setMsg_begin_date(Timestamp msg_begin_date) {
		this.msg_begin_date = msg_begin_date;
	}
	public String getMsg_body() {
		return msg_body;
	}
	public void setMsg_body(String msg_body) {
		this.msg_body = msg_body;
	}
	public Timestamp getMsg_end_date() {
		return msg_end_date;
	}
	public void setMsg_end_date(Timestamp msg_end_date) {
		this.msg_end_date = msg_end_date;
	}
	public long getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(long msg_id) {
		this.msg_id = msg_id;
	}
	public String getMsg_title() {
		return msg_title;
	}
	public void setMsg_title(String msg_title) {
		this.msg_title = msg_title;
	}
	public Timestamp getMsg_update_date() {
		return msg_update_date;
	}
	public void setMsg_update_date(Timestamp msg_update_date) {
		this.msg_update_date = msg_update_date;
	}

}
