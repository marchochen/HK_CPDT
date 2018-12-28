package com.cw.wizbank.JsonMod.Course;

/**
 * 评价
 */
public class Valuation {
	private long s_vlt_id;
	private String s_vlt_type;
	private int s_vlt_score;
	private String s_vlt_module;
	private long s_vlt_target_id;

	public String getS_vlt_type() {
		return s_vlt_type;
	}

	public void setS_vlt_type(String s_vlt_type) {
		this.s_vlt_type = s_vlt_type;
	}

	public long getS_vlt_id() {
		return s_vlt_id;
	}

	public void setS_vlt_id(long s_vlt_id) {
		this.s_vlt_id = s_vlt_id;
	}

	public int getS_vlt_score() {
		return s_vlt_score;
	}

	public void setS_vlt_score(int s_vlt_score) {
		this.s_vlt_score = s_vlt_score;
	}

	public String getS_vlt_module() {
		return s_vlt_module;
	}

	public void setS_vlt_module(String s_vlt_module) {
		this.s_vlt_module = s_vlt_module;
	}

	public long getS_vlt_target_id() {
		return s_vlt_target_id;
	}

	public void setS_vlt_target_id(long s_vlt_target_id) {
		this.s_vlt_target_id = s_vlt_target_id;
	}

}