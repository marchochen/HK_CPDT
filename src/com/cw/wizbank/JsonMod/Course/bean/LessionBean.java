package com.cw.wizbank.JsonMod.Course.bean;

import java.sql.Timestamp;
import java.util.Vector;

public class LessionBean {
	private long ils_id;
	private String ils_title;
	private int ils_day;
	private Timestamp ils_start_time;
	private Timestamp ils_end_time;
	private String ils_place;
	private Vector teachers;
	
	
	public int getIls_day() {
		return ils_day;
	}
	public void setIls_day(int ils_day) {
		this.ils_day = ils_day;
	}
	public Timestamp getIls_end_time() {
		return ils_end_time;
	}
	public void setIls_end_time(Timestamp ils_end_time) {
		this.ils_end_time = ils_end_time;
	}
	public long getIls_id() {
		return ils_id;
	}
	public void setIls_id(long ils_id) {
		this.ils_id = ils_id;
	}
	public String getIls_place() {
		return ils_place;
	}
	public void setIls_place(String ils_place) {
		this.ils_place = ils_place;
	}
	public Timestamp getIls_start_time() {
		return ils_start_time;
	}
	public void setIls_start_time(Timestamp ils_start_time) {
		this.ils_start_time = ils_start_time;
	}
	public String getIls_title() {
		return ils_title;
	}
	public void setIls_title(String ils_title) {
		this.ils_title = ils_title;
	}
	public Vector getTeachers() {
		return teachers;
	}
	public void setTeachers(Vector teachers) {
		this.teachers = teachers;
	}
	
}
