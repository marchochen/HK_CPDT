package com.cw.wizbank.JsonMod.mobile;

import com.cw.wizbank.JsonMod.BaseParam;

public class MobileModuleParam extends BaseParam {

	
	private long site_id;
	private String usr_id;
	private String usr_pwd;
	private String login_role;
	private String mode;
	private String json_obj;
	
	private long itm_id;

	public void setSite_id(long site_id) {
		this.site_id = site_id;
	}

	public long getSite_id() {
		return site_id;
	}

	public void setUsr_id(String usr_id) {
		this.usr_id = usr_id;
	}

	public String getUsr_id() {
		return usr_id;
	}

	public void setUsr_pwd(String usr_pwd) {
		this.usr_pwd = usr_pwd;
	}

	public String getUsr_pwd() {
		return usr_pwd;
	}

	public void setLogin_role(String login_role) {
		this.login_role = login_role;
	}

	public String getLogin_role() {
		return login_role;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}

	public long getItm_id() {
		return itm_id;
	}

	public void setJson_obj(String json_obj) {
		this.json_obj = json_obj;
	}

	public String getJson_obj() {
		return json_obj;
	}
    
    
}
