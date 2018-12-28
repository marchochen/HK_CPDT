package com.cwn.wizbank.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 *  专题培训
 * <p>Title:UserSpecialTopic</p>
 * <p>Description: </p>
 * @author halo.pan
 *
 * @date 2016年4月12日 下午4:30:23
 *
 */
public class UserSpecialTopic implements java.io.Serializable {
	private static final long serialVersionUID = 5753308122031091787L;
	private long ust_id;
	private String ust_title;
	private String ust_img;
	private long ust_tcr_id;
	private String ust_summary;
	private String ust_content;
	private int ust_showindex;
	private int ust_hits;
	private Timestamp ust_create_time;
	private long ust_create_usr_id;
	private Timestamp ust_update_time;
	private long ust_update_usr_id;
	private int ust_status;
	
	private String date;
	private List<UserSpecialExpert> experts;
	private List<UserSpecialItem> items;
	private String abs_img;
	
	private String encrypt_ust_id;
	
	public String getAbs_img() {
		return abs_img;
	}
	public void setAbs_img(String abs_img) {
		this.abs_img = abs_img;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public long getUst_id() {
		return ust_id;
	}
	public void setUst_id(long ust_id) {
		this.ust_id = ust_id;
	}
	public String getUst_title() {
		return ust_title;
	}
	public void setUst_title(String ust_title) {
		this.ust_title = ust_title;
	}
	public String getUst_img() {
		return ust_img;
	}
	public void setUst_img(String ust_img) {
		this.ust_img = ust_img;
	}
	public long getUst_tcr_id() {
		return ust_tcr_id;
	}
	public void setUst_tcr_id(long ust_tcr_id) {
		this.ust_tcr_id = ust_tcr_id;
	}
	public String getUst_summary() {
		return ust_summary;
	}
	public void setUst_summary(String ust_summary) {
		this.ust_summary = ust_summary;
	}
	public String getUst_content() {
		return ust_content;
	}
	public void setUst_content(String ust_content) {
		this.ust_content = ust_content;
	}
	public int getUst_showindex() {
		return ust_showindex;
	}
	public void setUst_showindex(int ust_showindex) {
		this.ust_showindex = ust_showindex;
	}
	public int getUst_hits() {
		return ust_hits;
	}
	public void setUst_hits(int ust_hits) {
		this.ust_hits = ust_hits;
	}
	public Timestamp getUst_create_time() {
		return ust_create_time;
	}
	public void setUst_create_time(Timestamp ust_create_time) {
		this.ust_create_time = ust_create_time;
	}
	public long getUst_create_usr_id() {
		return ust_create_usr_id;
	}
	public void setUst_create_usr_id(long ust_create_usr_id) {
		this.ust_create_usr_id = ust_create_usr_id;
	}
	public Timestamp getUst_update_time() {
		return ust_update_time;
	}
	public void setUst_update_time(Timestamp ust_update_time) {
		this.ust_update_time = ust_update_time;
	}
	public long getUst_update_usr_id() {
		return ust_update_usr_id;
	}
	public void setUst_update_usr_id(long ust_update_usr_id) {
		this.ust_update_usr_id = ust_update_usr_id;
	}
	public int getUst_status() {
		return ust_status;
	}
	public void setUst_status(int ust_status) {
		this.ust_status = ust_status;
	}
	public List<UserSpecialExpert> getExperts() {
		return experts;
	}
	public void setExperts(List<UserSpecialExpert> experts) {
		this.experts = experts;
	}
	public List<UserSpecialItem> getItems() {
		return items;
	}
	public void setItems(List<UserSpecialItem> items) {
		this.items = items;
	}
	public String getEncrypt_ust_id() {
		return encrypt_ust_id;
	}
	public void setEncrypt_ust_id(String encrypt_ust_id) {
		this.encrypt_ust_id = encrypt_ust_id;
	}
	
}