package com.cwn.wizbank.entity.vo;


public class UserGroupVo {
	
	String eip_code;
	String parent_usg_code;
	String usg_code;
	String usg_name;
	String usg_desc;
	String usg_last_upd_date;
	String usg_level;
	String usg_del_ind;
	
	
	public String getParent_usg_code() {
		return parent_usg_code;
	}
	public void setParent_usg_code(String parent_usg_code) {
		this.parent_usg_code = parent_usg_code;
	}
	public String getUsg_code() {
		return usg_code;
	}
	public void setUsg_code(String usg_code) {
		this.usg_code = usg_code;
	}
	public String getUsg_name() {
		return usg_name;
	}
	public void setUsg_name(String usg_name) {
		this.usg_name = usg_name;
	}
	public String getUsg_desc() {
		return usg_desc;
	}
	public void setUsg_desc(String usg_desc) {
		this.usg_desc = usg_desc;
	}
	public String getUsg_last_upd_date() {
		return usg_last_upd_date;
	}
	public void setUsg_last_upd_date(String usg_last_upd_date) {
		this.usg_last_upd_date = usg_last_upd_date;
	}
	public String getUsg_level() {
		return usg_level;
	}
	public void setUsg_level(String usg_level) {
		this.usg_level = usg_level;
	}
	public String getUsg_del_ind() {
		return usg_del_ind;
	}
	public void setUsg_del_ind(String usg_del_ind) {
		this.usg_del_ind = usg_del_ind;
	}
	@Override
	public String toString() {
		return "UserGroupVo [parent_usg_code=" + parent_usg_code
				+ ", usg_code=" + usg_code + ", usg_name=" + usg_name
				+ ", usg_desc=" + usg_desc + ", usg_last_upd_date="
				+ usg_last_upd_date + ", usg_level=" + usg_level
				+ ", usg_del_ind=" + usg_del_ind + "]";
	}
	public String getEip_code() {
		return eip_code;
	}
	public void setEip_code(String eip_code) {
		this.eip_code = eip_code;
	}
	
}
