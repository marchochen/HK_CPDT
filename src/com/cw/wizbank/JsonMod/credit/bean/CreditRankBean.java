package com.cw.wizbank.JsonMod.credit.bean;

public class CreditRankBean {
	private long sort_id;
	private long usr_ent_id;
	private String usr_ste_usr_id;
	private String usr_display_bil;
	private String usg_display_bil;
	private float uct_total;

	public long getSort_id() {
		return sort_id;
	}

	public void setSort_id(long sort_id) {
		this.sort_id = sort_id;
	}

	public long getUsr_ent_id() {
		return usr_ent_id;
	}

	public void setUsr_ent_id(long usr_ent_id) {
		this.usr_ent_id = usr_ent_id;
	}

	public String getUsr_ste_usr_id() {
		return usr_ste_usr_id;
	}

	public void setUsr_ste_usr_id(String usr_ste_usr_id) {
		this.usr_ste_usr_id = usr_ste_usr_id;
	}

	public String getUsr_display_bil() {
		return usr_display_bil;
	}

	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}

	public String getUsg_display_bil() {
		return usg_display_bil;
	}

	public void setUsg_display_bil(String usg_display_bil) {
		this.usg_display_bil = usg_display_bil;
	}

	public float getUct_total() {
		return uct_total;
	}

	public void setUct_total(float uct_total) {
		this.uct_total = uct_total;
	}

}
