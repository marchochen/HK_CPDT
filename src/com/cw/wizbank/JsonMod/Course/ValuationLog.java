package com.cw.wizbank.JsonMod.Course;

import java.sql.Timestamp;

/**
 * 评价
 */
public class ValuationLog {
	private long s_vtl_log_id;
	private String s_vtl_type;
	private long s_vtl_score;
	private Timestamp s_vtl_create_datetime;
	private long s_vtl_uid;
	private String s_vtl_module;
	private long s_vtl_target_id;

	private Course course;

	public long getS_vtl_log_id() {
		return s_vtl_log_id;
	}

	public void setS_vtl_log_id(long s_vtl_log_id) {
		this.s_vtl_log_id = s_vtl_log_id;
	}

	public String getS_vtl_type() {
		return s_vtl_type;
	}

	public void setS_vtl_type(String s_vtl_type) {
		this.s_vtl_type = s_vtl_type;
	}

	public long getS_vtl_score() {
		return s_vtl_score;
	}

	public void setS_vtl_score(long s_vtl_score) {
		this.s_vtl_score = s_vtl_score;
	}

	public Timestamp getS_vtl_create_datetime() {
		return s_vtl_create_datetime;
	}

	public void setS_vtl_create_datetime(Timestamp s_vtl_create_datetime) {
		this.s_vtl_create_datetime = s_vtl_create_datetime;
	}

	public long getS_vtl_uid() {
		return s_vtl_uid;
	}

	public void setS_vtl_uid(long s_vtl_uid) {
		this.s_vtl_uid = s_vtl_uid;
	}

	public String getS_vtl_module() {
		return s_vtl_module;
	}

	public void setS_vtl_module(String s_vtl_module) {
		this.s_vtl_module = s_vtl_module;
	}

	public long getS_vtl_target_id() {
		return s_vtl_target_id;
	}

	public void setS_vtl_target_id(long s_vtl_target_id) {
		this.s_vtl_target_id = s_vtl_target_id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

}