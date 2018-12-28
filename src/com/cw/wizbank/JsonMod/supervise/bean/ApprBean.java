package com.cw.wizbank.JsonMod.supervise.bean;

import java.sql.Timestamp;

public class ApprBean {
	private String usr_display_bil;
	private Timestamp aal_action_timestamp;
	private String action_taken;
	public String getAction_taken() {
		return action_taken;
	}
	public void setAction_taken(String action_taken) {
		this.action_taken = action_taken;
	}
	public Timestamp getAal_action_timestamp() {
		return aal_action_timestamp;
	}
	public void setAal_action_timestamp(Timestamp aal_action_timestamp) {
		this.aal_action_timestamp = aal_action_timestamp;
	}
	public String getUsr_display_bil() {
		return usr_display_bil;
	}
	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}
	
}
