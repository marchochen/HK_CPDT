package com.cw.wizbank.JsonMod.Course.bean;

import java.util.Vector;

public class CompletionCriteriaBean {
	 private String condition; 
	 private boolean is_available;
	 private ScoreMeasureBean score_measurement;
	 private Vector student_measurement;
	 private AttBean attendance;
	public AttBean getAttendance() {
		return attendance;
	}
	public void setAttendance(AttBean attendance) {
		this.attendance = attendance;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public ScoreMeasureBean getScore_measurement() {
		return score_measurement;
	}
	public void setScore_measurement(ScoreMeasureBean score_measurement) {
		this.score_measurement = score_measurement;
	}
	public Vector getStudent_measurement() {
		return student_measurement;
	}
	public void setStudent_measurement(Vector student_measurement) {
		this.student_measurement = student_measurement;
	}
	public boolean isIs_available() {
		return is_available;
	}
	public void setIs_available(boolean is_available) {
		this.is_available = is_available;
	}
      
}
