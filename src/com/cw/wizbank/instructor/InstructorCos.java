package com.cw.wizbank.instructor;

import java.sql.Timestamp;

public class InstructorCos {
	private long ics_id;
	private long ics_ent_id;
	private String ics_title;
	private float ics_fee;
	private float ics_hours;
	private String ics_target;
	private String ics_content;

	private String itm_type;
	private String cos_itm_title;
	private String run_itm_title;
	private String itm_status;
	private Timestamp itm_eff_start_datetime;
	private Timestamp itm_eff_end_datetime;
	private boolean itm_blend_ind;
	private boolean itm_exam_ind;
	private boolean itm_ref_ind;
	private boolean itm_integrated_ind;
	private long cos_itm_id;
	private long run_itm_id;

	public long getIcs_id() {
		return ics_id;
	}

	public void setIcs_id(long ics_id) {
		this.ics_id = ics_id;
	}

	public long getIcs_ent_id() {
		return ics_ent_id;
	}

	public void setIcs_ent_id(long ics_ent_id) {
		this.ics_ent_id = ics_ent_id;
	}

	public String getIcs_title() {
		return ics_title;
	}

	public void setIcs_title(String ics_title) {
		this.ics_title = ics_title;
	}

	public float getIcs_fee() {
		return ics_fee;
	}

	public void setIcs_fee(float ics_fee) {
		this.ics_fee = ics_fee;
	}

	public float getIcs_hours() {
		return ics_hours;
	}

	public void setIcs_hours(float ics_hours) {
		this.ics_hours = ics_hours;
	}

	public String getIcs_target() {
		return ics_target;
	}

	public void setIcs_target(String ics_target) {
		this.ics_target = ics_target;
	}

	public String getIcs_content() {
		return ics_content;
	}

	public void setIcs_content(String ics_content) {
		this.ics_content = ics_content;
	}

	public String getItm_type() {
		return itm_type;
	}

	public void setItm_type(String itm_type) {
		this.itm_type = itm_type;
	}

	public String getCos_itm_title() {
		return cos_itm_title;
	}

	public void setCos_itm_title(String cos_itm_title) {
		this.cos_itm_title = cos_itm_title;
	}

	public String getRun_itm_title() {
		return run_itm_title;
	}

	public void setRun_itm_title(String run_itm_title) {
		this.run_itm_title = run_itm_title;
	}

	public String getItm_status() {
		return itm_status;
	}

	public void setItm_status(String itm_status) {
		this.itm_status = itm_status;
	}

	public Timestamp getItm_eff_start_datetime() {
		return itm_eff_start_datetime;
	}

	public void setItm_eff_start_datetime(Timestamp itm_eff_start_datetime) {
		this.itm_eff_start_datetime = itm_eff_start_datetime;
	}

	public Timestamp getItm_eff_end_datetime() {
		return itm_eff_end_datetime;
	}

	public void setItm_eff_end_datetime(Timestamp itm_eff_end_datetime) {
		this.itm_eff_end_datetime = itm_eff_end_datetime;
	}

	public boolean isItm_blend_ind() {
		return itm_blend_ind;
	}

	public void setItm_blend_ind(boolean itm_blend_ind) {
		this.itm_blend_ind = itm_blend_ind;
	}

	public boolean isItm_exam_ind() {
		return itm_exam_ind;
	}

	public void setItm_exam_ind(boolean itm_exam_ind) {
		this.itm_exam_ind = itm_exam_ind;
	}

	public boolean isItm_ref_ind() {
		return itm_ref_ind;
	}

	public void setItm_ref_ind(boolean itm_ref_ind) {
		this.itm_ref_ind = itm_ref_ind;
	}

	public boolean isItm_integrated_ind() {
		return itm_integrated_ind;
	}

	public void setItm_integrated_ind(boolean itm_integrated_ind) {
		this.itm_integrated_ind = itm_integrated_ind;
	}

	public long getCos_itm_id() {
		return cos_itm_id;
	}

	public void setCos_itm_id(long cos_itm_id) {
		this.cos_itm_id = cos_itm_id;
	}

	public long getRun_itm_id() {
		return run_itm_id;
	}

	public void setRun_itm_id(long run_itm_id) {
		this.run_itm_id = run_itm_id;
	}

}