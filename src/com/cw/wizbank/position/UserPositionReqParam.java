package com.cw.wizbank.position;

import com.cw.wizbank.JsonMod.BaseParam;

public class UserPositionReqParam extends BaseParam{
	public String upt_code;
	public String upt_title;
	public String upt_desc;
	public String search_info;
	public String upt_code_list;
	public String upt_tcr_id;
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
	public String getSearch_info() {
		return search_info;
	}
	public void setSearch_info(String search_info) {
		this.search_info = search_info;
	}
	public String getUpt_code_list() {
		return upt_code_list;
	}
	public void setUpt_code_list(String upt_code_list) {
		this.upt_code_list = upt_code_list;
	}
	public String getUpt_tcr_id() {
		return upt_tcr_id;
	}
	public void setUpt_tcr_id(String upt_tcr_id) {
		this.upt_tcr_id = upt_tcr_id;
	}
	
}
