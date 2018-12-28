package com.cw.wizbank.JsonMod.Ann.bean;

import java.sql.Timestamp;

public class AnnBean {
	private long msg_id;
	private String msg_title;
	private String msg_body;
	private Timestamp msg_begin_date;
	private boolean is_content_cut;
	private String usr_display_bil;
	private String msg_desc;
	private String msg_type;
	private boolean newest_ind;
	
	public String getUsr_display_bil() {
		return usr_display_bil;
	}
	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}
	public String getMsg_body() {
		return msg_body;
	}
	public void setMsg_body(String msg_body) {
		this.msg_body = msg_body;
	}
	public Timestamp getMsg_begin_date() {
		return msg_begin_date;
	}
	public void setMsg_begin_date(Timestamp msg_begin_date) {
		this.msg_begin_date = msg_begin_date;
	}
	public long getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(long msg_id) {
		this.msg_id = msg_id;
	}
	public boolean isIs_content_cut() {
		return is_content_cut;
	}
	public void setIs_content_cut(boolean is_content_cut) {
		this.is_content_cut = is_content_cut;
	}
	public String getMsg_title() {
		return msg_title;
	}
	public void setMsg_title(String msg_title) {
		this.msg_title = msg_title;
	}
	public String getMsg_desc() {
		return msg_desc;
	}
	public void setMsg_desc(String msg_desc) {
		this.msg_desc = msg_desc;
	}
	public String getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	public boolean isNewest_ind() {
		return newest_ind;
	}
	public void setNewest_ind(boolean newest_ind) {
		this.newest_ind = newest_ind;
	}
}
