package com.cwn.wizbank.entity;

import java.util.List;

public class UserGrade implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6484384445820751830L;
	private long ugr_ent_id;
	private String ugr_display_bil;
	private long ugr_ent_id_root;
	private String ugr_type;
	private long ugr_seq_no;
	private long ugr_default_ind;
	private long ugr_trc_id;
	private String ugr_code;
     
	List<ProfessionLrnItem> items;
	String itemValue;
	
	public long getUgr_ent_id() {
		return ugr_ent_id;
	}

	public void setUgr_ent_id(long ugr_ent_id) {
		this.ugr_ent_id = ugr_ent_id;
	}

	public String getUgr_display_bil() {
		return ugr_display_bil;
	}

	public void setUgr_display_bil(String ugr_display_bil) {
		this.ugr_display_bil = ugr_display_bil;
	}

	public long getUgr_ent_id_root() {
		return ugr_ent_id_root;
	}

	public void setUgr_ent_id_root(long ugr_ent_id_root) {
		this.ugr_ent_id_root = ugr_ent_id_root;
	}

	public String getUgr_type() {
		return ugr_type;
	}

	public void setUgr_type(String ugr_type) {
		this.ugr_type = ugr_type;
	}

	public long getUgr_seq_no() {
		return ugr_seq_no;
	}

	public void setUgr_seq_no(long ugr_seq_no) {
		this.ugr_seq_no = ugr_seq_no;
	}

	public long getUgr_default_ind() {
		return ugr_default_ind;
	}

	public void setUgr_default_ind(long ugr_default_ind) {
		this.ugr_default_ind = ugr_default_ind;
	}

	public long getUgr_trc_id() {
		return ugr_trc_id;
	}

	public void setUgr_trc_id(long ugr_trc_id) {
		this.ugr_trc_id = ugr_trc_id;
	}

	public String getUgr_code() {
		return ugr_code;
	}

	public void setUgr_code(String ugr_code) {
		this.ugr_code = ugr_code;
	}

	public List<ProfessionLrnItem> getItems() {
		return items;
	}

	public void setItems(List<ProfessionLrnItem> items) {
		this.items = items;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

}