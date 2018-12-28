package com.cw.wizbank.JsonMod.Course.bean;

import java.util.Vector;

public class ScoreMeasureBean {
	private float total_max_score;                
	private float total_pass_score_rate;
	private float total_pass_score;
	private float total_score;
	private Vector measurement_lst;
	
	public Vector getMeasurement_lst() {
		return measurement_lst;
	}
	public void setMeasurement_lst(Vector measurement_lst) {
		this.measurement_lst = measurement_lst;
	}
	public float getTotal_max_score() {
		return total_max_score;
	}
	public void setTotal_max_score(float total_max_score) {
		this.total_max_score = total_max_score;
	}
	public float getTotal_pass_score() {
		return total_pass_score;
	}
	public void setTotal_pass_score(float total_pass_score) {
		this.total_pass_score = total_pass_score;
	}
	public float getTotal_pass_score_rate() {
		return total_pass_score_rate;
	}
	public void setTotal_pass_score_rate(float total_pass_score_rate) {
		this.total_pass_score_rate = total_pass_score_rate;
	}
	public float getTotal_score() {
		return total_score;
	}
	public void setTotal_score(float total_score) {
		this.total_score = total_score;
	}
	

}
