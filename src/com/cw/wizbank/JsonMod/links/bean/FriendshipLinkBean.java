package com.cw.wizbank.JsonMod.links.bean;

import java.sql.Timestamp;
public class FriendshipLinkBean {
	private long fsl_id;
	private String fsl_title;
	private String fsl_url;
	private String fsl_status;
	private String fsl_create_usr_id;
	private Timestamp fsl_create_timestamp;
	private String fsl_update_usr_id;
	private String update_usr_display_bil;
	private Timestamp fsl_update_timestamp;
	
	
	public long getFsl_id() {
		return fsl_id;
	}
	public void setFsl_id(long fsl_id) {
		this.fsl_id = fsl_id;
	}
	public String getFsl_title() {
		return fsl_title;
	}
	public void setFsl_title(String fsl_title) {
		this.fsl_title = fsl_title;
	}
	public String getFsl_url() {
		return fsl_url;
	}
	public void setFsl_url(String fsl_url) {
		this.fsl_url = fsl_url;
	}
	public String getFsl_status() {
		return fsl_status;
	}
	public void setFsl_status(String fsl_status) {
		this.fsl_status = fsl_status;
	}
	public String getFsl_create_usr_id() {
		return fsl_create_usr_id;
	}
	public void setFsl_create_usr_id(String fsl_create_usr_id) {
		this.fsl_create_usr_id = fsl_create_usr_id;
	}
	public Timestamp getFsl_create_timestamp() {
		return fsl_create_timestamp;
	}
	public void setFsl_create_timestamp(Timestamp fsl_create_timestamp) {
		this.fsl_create_timestamp = fsl_create_timestamp;
	}
	public String getFsl_update_usr_id() {
		return fsl_update_usr_id;
	}
	public void setFsl_update_usr_id(String fsl_update_usr_id) {
		this.fsl_update_usr_id = fsl_update_usr_id;
	}
	public Timestamp getFsl_update_timestamp() {
		return fsl_update_timestamp;
	}
	public void setFsl_update_timestamp(Timestamp fsl_update_timestamp) {
		this.fsl_update_timestamp = fsl_update_timestamp;
	}
	public String getUpdate_usr_display_bil() {
		return update_usr_display_bil;
	}
	public void setUpdate_usr_display_bil(String update_usr_display_bil) {
		this.update_usr_display_bil = update_usr_display_bil;
	}
}
