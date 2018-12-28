package com.cw.wizbank.position;

import java.sql.Timestamp;

public class UserPostion {
	String upt_code;
	String upt_title;
	String upt_desc;
	Timestamp pfs_update_time;
	public String getUpt_code() {
		return upt_code;
	}
	public void setUpt_code(String upt_code) {
		this.upt_code = upt_code;
	}
	public String getUpt_title() {
		return upt_title;
	}
	public void setUpt_title(String upt_title) {
		this.upt_title = upt_title;
	}
	public String getUpt_desc() {
		return upt_desc;
	}
	public void setUpt_desc(String upt_desc) {
		this.upt_desc = upt_desc;
	}
	public Timestamp getPfs_update_time() {
		return pfs_update_time;
	}
	public void setPfs_update_time(Timestamp pfs_update_time) {
		this.pfs_update_time = pfs_update_time;
	}
	
}
