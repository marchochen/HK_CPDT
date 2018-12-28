package com.cw.wizbank.JsonMod.Course.bean;

import java.sql.Timestamp;

public class AiccDataBean {
	 private long tkh_id;
     private long course_id;
     private long student_id;
     private long lesson_id;
     private Timestamp last_acc_datetime;
     private Timestamp last_update_timestamp;
     private String used_time;
     private String number;
     private String status;
     private String score;
     private String location;
     private String pgr_status;
     private String pgr_completion_status;
	public long getCourse_id() {
		return course_id;
	}
	public void setCourse_id(long course_id) {
		this.course_id = course_id;
	}
	public Timestamp getLast_acc_datetime() {
		return last_acc_datetime;
	}
	public void setLast_acc_datetime(Timestamp last_acc_datetime) {
		this.last_acc_datetime = last_acc_datetime;
	}
	public Timestamp getLast_update_timestamp() {
		return last_update_timestamp;
	}
	public void setLast_update_timestamp(Timestamp last_update_timestamp) {
		this.last_update_timestamp = last_update_timestamp;
	}
	public long getLesson_id() {
		return lesson_id;
	}
	public void setLesson_id(long lesson_id) {
		this.lesson_id = lesson_id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public long getStudent_id() {
		return student_id;
	}
	public void setStudent_id(long student_id) {
		this.student_id = student_id;
	}
	public long getTkh_id() {
		return tkh_id;
	}
	public void setTkh_id(long tkh_id) {
		this.tkh_id = tkh_id;
	}
	public String getUsed_time() {
		return used_time;
	}
	public void setUsed_time(String used_time) {
		this.used_time = used_time;
	}
	public String getPgr_status() {
		return pgr_status;
	}
	public void setPgr_status(String pgr_status) {
		this.pgr_status = pgr_status;
	}
	public String getPgr_completion_status() {
		return pgr_completion_status;
	}
	public void setPgr_completion_status(String pgr_completion_status) {
		this.pgr_completion_status = pgr_completion_status;
	}

}
