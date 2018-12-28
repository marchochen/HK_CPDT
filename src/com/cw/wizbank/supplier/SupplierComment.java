package com.cw.wizbank.supplier;

import java.sql.Timestamp;

public class SupplierComment {
	private long scm_id;
	private long scm_spl_id;
	private long scm_ent_id;
	private float scm_design_score;
	private float scm_teaching_score;
	private float scm_price_score;
	private float scm_score;
	private String scm_comment;
	private Timestamp scm_update_datetime;
	private Timestamp scm_create_datetime;
	private float scm_management_score;
	private String usr_display_bil;
	
	
	
	
	public long getScm_id() {
		return scm_id;
	}
	public void setScm_id(long scm_id) {
		this.scm_id = scm_id;
	}
	public long getScm_spl_id() {
		return scm_spl_id;
	}
	public void setScm_spl_id(long scm_spl_id) {
		this.scm_spl_id = scm_spl_id;
	}
	public long getScm_ent_id() {
		return scm_ent_id;
	}
	public void setScm_ent_id(long scm_ent_id) {
		this.scm_ent_id = scm_ent_id;
	}
	public float getScm_design_score() {
		return scm_design_score;
	}
	public void setScm_design_score(float scm_design_score) {
		this.scm_design_score = scm_design_score;
	}
	public float getScm_teaching_score() {
		return scm_teaching_score;
	}
	public void setScm_teaching_score(float scm_teaching_score) {
		this.scm_teaching_score = scm_teaching_score;
	}
	public float getScm_price_score() {
		return scm_price_score;
	}
	public void setScm_price_score(float scm_price_score) {
		this.scm_price_score = scm_price_score;
	}
	public float getScm_score() {
		return scm_score;
	}
	public void setScm_score(float scm_score) {
		this.scm_score = scm_score;
	}
	public String getScm_comment() {
		return scm_comment;
	}
	public void setScm_comment(String scm_comment) {
		this.scm_comment = scm_comment;
	}
	public Timestamp getScm_update_datetime() {
		return scm_update_datetime;
	}
	public void setScm_update_datetime(Timestamp scm_update_datetime) {
		this.scm_update_datetime = scm_update_datetime;
	}
	public Timestamp getScm_create_datetime() {
		return scm_create_datetime;
	}
	public void setScm_create_datetime(Timestamp scm_create_datetime) {
		this.scm_create_datetime = scm_create_datetime;
	}
	public float getScm_management_score() {
		return scm_management_score;
	}
	public void setScm_management_score(float scm_management_score) {
		this.scm_management_score = scm_management_score;
	}
	public String getUsr_display_bil() {
		return usr_display_bil;
	}
	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}
	
	
	
}
