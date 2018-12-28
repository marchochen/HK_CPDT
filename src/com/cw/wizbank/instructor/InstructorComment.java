package com.cw.wizbank.instructor;

import java.sql.Timestamp;

public class InstructorComment {
	private long itc_id;
	private long itc_ent_id;
	private long itc_itm_id;
	private long itc_iti_ent_id;
	private float itc_style_score;
	private float itc_quality_score;
	private float itc_structure_score;
	private float itc_interaction_score;
	private float itc_score;
	private String usr_display_bil;
	private String itc_comment;
	private String itc_create_usr_id;
	private Timestamp itc_create_datetime;
	private String itc_upd_usr_id;
	private Timestamp itc_upd_datetime;

	public long getItc_id() {
		return itc_id;
	}

	public void setItc_id(long itc_id) {
		this.itc_id = itc_id;
	}

	public long getItc_ent_id() {
		return itc_ent_id;
	}

	public void setItc_ent_id(long itc_ent_id) {
		this.itc_ent_id = itc_ent_id;
	}

	public long getItc_itm_id() {
		return itc_itm_id;
	}

	public void setItc_itm_id(long itc_itm_id) {
		this.itc_itm_id = itc_itm_id;
	}

	public long getItc_iti_ent_id() {
		return itc_iti_ent_id;
	}

	public void setItc_iti_ent_id(long itc_iti_ent_id) {
		this.itc_iti_ent_id = itc_iti_ent_id;
	}

	public float getItc_style_score() {
		return itc_style_score;
	}

	public void setItc_style_score(float itc_style_score) {
		this.itc_style_score = itc_style_score;
	}

	public float getItc_quality_score() {
		return itc_quality_score;
	}

	public void setItc_quality_score(float itc_quality_score) {
		this.itc_quality_score = itc_quality_score;
	}

	public float getItc_structure_score() {
		return itc_structure_score;
	}

	public void setItc_structure_score(float itc_structure_score) {
		this.itc_structure_score = itc_structure_score;
	}

	public float getItc_interaction_score() {
		return itc_interaction_score;
	}

	public void setItc_interaction_score(float itc_interaction_score) {
		this.itc_interaction_score = itc_interaction_score;
	}

	public float getItc_score() {
		return itc_score;
	}

	public void setItc_score(float itc_score) {
		this.itc_score = itc_score;
	}

	public String getItc_comment() {
		return itc_comment;
	}

	public void setItc_comment(String itc_comment) {
		this.itc_comment = itc_comment;
	}

	public String getItc_create_usr_id() {
		return itc_create_usr_id;
	}

	public void setItc_create_usr_id(String itc_create_usr_id) {
		this.itc_create_usr_id = itc_create_usr_id;
	}

	public Timestamp getItc_create_datetime() {
		return itc_create_datetime;
	}

	public void setItc_create_datetime(Timestamp itc_create_datetime) {
		this.itc_create_datetime = itc_create_datetime;
	}

	public String getItc_upd_usr_id() {
		return itc_upd_usr_id;
	}

	public void setItc_upd_usr_id(String itc_upd_usr_id) {
		this.itc_upd_usr_id = itc_upd_usr_id;
	}

	public Timestamp getItc_upd_datetime() {
		return itc_upd_datetime;
	}

	public void setItc_upd_datetime(Timestamp itc_upd_datetime) {
		this.itc_upd_datetime = itc_upd_datetime;
	}

	public String getUsr_display_bil() {
		return usr_display_bil;
	}

	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}

}