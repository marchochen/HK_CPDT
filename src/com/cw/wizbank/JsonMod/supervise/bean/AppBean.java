package com.cw.wizbank.JsonMod.supervise.bean;

import java.sql.Timestamp;
import java.util.Vector;

public class AppBean {
	private int app_id;
	private long usr_ent_id;
	private String usr_display_bil;
	private String usr_ste_usr_id;
	private String itm_title;
	private Timestamp itm_start_date;
	private Timestamp itm_end_date;
	private Timestamp app_create_timestamp;
	private Timestamp app_upd_timestamp;
	private Vector appr_lst;
	private Vector nextApp;
	
	
	public Vector getNextApp() {
		return nextApp;
	}
	public void setNextApp(Vector nextApp) {
		this.nextApp = nextApp;
	}
	public Timestamp getApp_create_timestamp() {
		return app_create_timestamp;
	}
	public void setApp_create_timestamp(Timestamp app_create_timestamp) {
		this.app_create_timestamp = app_create_timestamp;
	}
	public Vector getAppr_lst() {
		return appr_lst;
	}
	public void setAppr_lst(Vector appr_lst) {
		this.appr_lst = appr_lst;
	}
	public Timestamp getItm_end_date() {
		return itm_end_date;
	}
	public void setItm_end_date(Timestamp itm_end_date) {
		this.itm_end_date = itm_end_date;
	}
	public int getApp_id() {
		return app_id;
	}
	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}
	public long getUsr_ent_id() {
		return usr_ent_id;
	}
	public void setUsr_ent_id(long usr_ent_id) {
		this.usr_ent_id = usr_ent_id;
	}
	public String getItm_title() {
		return itm_title;
	}
	public void setItm_title(String itm_title) {
		this.itm_title = itm_title;
	}
	public String getUsr_display_bil() {
		return usr_display_bil;
	}
	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}
	public String getUsr_ste_usr_id() {
		return usr_ste_usr_id;
	}
	public void setUsr_ste_usr_id(String usr_ste_usr_id) {
		this.usr_ste_usr_id = usr_ste_usr_id;
	}
	public Timestamp getItm_start_date() {
		return itm_start_date;
	}
	public void setItm_start_date(Timestamp itm_start_date) {
		this.itm_start_date = itm_start_date;
	}
	public Timestamp getApp_upd_timestamp() {
		return app_upd_timestamp;
	}
	public void setApp_upd_timestamp(Timestamp app_upd_timestamp) {
		this.app_upd_timestamp = app_upd_timestamp;
	}
	
}
