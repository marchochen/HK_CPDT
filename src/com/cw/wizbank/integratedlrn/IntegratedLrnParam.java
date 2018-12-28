package com.cw.wizbank.integratedlrn;

import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.BaseParam;

public class IntegratedLrnParam extends BaseParam {

	private long itm_id;
	private long icc_id;
	private long icd_id;
	private int icd_completed_item_count;
	private int icc_completed_elective_count;
	private String icd_type;
	private String itm_condition_list;
	private Timestamp icd_update_timestamp;
	private Timestamp icc_update_timestamp;

	public Timestamp getIcc_update_timestamp() {
		return icc_update_timestamp;
	}

	public void setIcc_update_timestamp(Timestamp icc_update_timestamp) {
		this.icc_update_timestamp = icc_update_timestamp;
	}

	public long getItm_id() {
		return itm_id;
	}

	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}

	public String getIcd_type() {
		return icd_type;
	}

	public void setIcd_type(String icd_type) {
		this.icd_type = icd_type;
	}

	public long getIcc_id() {
		return icc_id;
	}

	public void setIcc_id(long icc_id) {
		this.icc_id = icc_id;
	}

	public long getIcd_id() {
		return icd_id;
	}

	public void setIcd_id(long icd_id) {
		this.icd_id = icd_id;
	}

	public String getItm_condition_list() {
		return itm_condition_list;
	}

	public void setItm_condition_list(String itm_condition_list) {
		this.itm_condition_list = itm_condition_list;
	}

	public int getIcd_completed_item_count() {
		return icd_completed_item_count;
	}

	public void setIcd_completed_item_count(int icd_completed_item_count) {
		this.icd_completed_item_count = icd_completed_item_count;
	}

	public Timestamp getIcd_update_timestamp() {
		return icd_update_timestamp;
	}

	public void setIcd_update_timestamp(Timestamp icd_update_timestamp) {
		this.icd_update_timestamp = icd_update_timestamp;
	}

	public int getIcc_completed_elective_count() {
		return icc_completed_elective_count;
	}

	public void setIcc_completed_elective_count(int icc_completed_elective_count) {
		this.icc_completed_elective_count = icc_completed_elective_count;
	}
}
