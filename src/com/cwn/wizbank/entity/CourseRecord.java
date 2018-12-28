package com.cwn.wizbank.entity;

import java.util.Date;

public class CourseRecord {
	private int pcr_usr_ent_id;
	private int pcr_itm_id;
	private int pcr_mod_id;
	private long pcr_duration;
	private Date pcr_last_acc;
	private String pcr_note;

	public CourseRecord() {
	}
    
	public CourseRecord(int pcr_usr_ent_id, int pcr_itm_id, int pcr_mod_id) {
		this.pcr_usr_ent_id = pcr_usr_ent_id;
		this.pcr_itm_id = pcr_itm_id;
		this.pcr_mod_id = pcr_mod_id;
	}

	public CourseRecord(int pcr_usr_ent_id, int pcr_itm_id, int pcr_mod_id,
			Date pcr_last_acc) {
		this.pcr_usr_ent_id = pcr_usr_ent_id;
		this.pcr_itm_id = pcr_itm_id;
		this.pcr_mod_id = pcr_mod_id;
		this.pcr_last_acc = pcr_last_acc;
	}

	public CourseRecord(int pcr_usr_ent_id, int pcr_itm_id, int pcr_mod_id,
			long pcr_duration) {
		this.pcr_usr_ent_id = pcr_usr_ent_id;
		this.pcr_itm_id = pcr_itm_id;
		this.pcr_mod_id = pcr_mod_id;
		this.pcr_duration = pcr_duration;
	}

	public CourseRecord(int pcr_usr_ent_id, int pcr_itm_id, int pcr_mod_id,
			String pcr_note) {
		this.pcr_usr_ent_id = pcr_usr_ent_id;
		this.pcr_itm_id = pcr_itm_id;
		this.pcr_mod_id = pcr_mod_id;
		this.pcr_note = pcr_note;
	}

	public int getPcr_usr_ent_id() {
		return pcr_usr_ent_id;
	}

	public void setPcr_usr_ent_id(int pcr_usr_ent_id) {
		this.pcr_usr_ent_id = pcr_usr_ent_id;
	}

	public int getPcr_itm_id() {
		return pcr_itm_id;
	}

	public void setPcr_itm_id(int pcr_itm_id) {
		this.pcr_itm_id = pcr_itm_id;
	}

	public int getPcr_mod_id() {
		return pcr_mod_id;
	}

	public void setPcr_mod_id(int pcr_mod_id) {
		this.pcr_mod_id = pcr_mod_id;
	}

	public long getPcr_duration() {
		return pcr_duration;
	}

	public void setPcr_duration(long pcr_duration) {
		this.pcr_duration = pcr_duration;
	}

	public Date getPcr_last_acc() {
		return pcr_last_acc;
	}

	public void setPcr_last_acc(Date pcr_last_acc) {
		this.pcr_last_acc = pcr_last_acc;
	}

	public String getPcr_note() {
		return pcr_note;
	}

	public void setPcr_note(String pcr_note) {
		this.pcr_note = pcr_note;
	}
}
