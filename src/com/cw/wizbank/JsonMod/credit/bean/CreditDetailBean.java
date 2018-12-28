package com.cw.wizbank.JsonMod.credit.bean;

import java.sql.Timestamp;

public class CreditDetailBean {
	private String cty_code;
	private String source;
	private float ucl_point;
	private boolean cty_manual_ind;
	private boolean cty_deduction_ind;
	private String type;
	private Timestamp ucl_create_timestamp;

	public String getCty_code() {
		return cty_code;
	}

	public void setCty_code(String cty_code) {
		this.cty_code = cty_code;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public float getUcl_point() {
		return ucl_point;
	}

	public void setUcl_point(float uclPoint) {
		ucl_point = uclPoint;
	}

	public boolean isCty_manual_ind() {
		return cty_manual_ind;
	}

	public void setCty_manual_ind(boolean cty_manual_ind) {
		this.cty_manual_ind = cty_manual_ind;
	}

	public boolean isCty_deduction_ind() {
		return cty_deduction_ind;
	}

	public void setCty_deduction_ind(boolean cty_deduction_ind) {
		this.cty_deduction_ind = cty_deduction_ind;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getUcl_create_timestamp() {
		return ucl_create_timestamp;
	}

	public void setUcl_create_timestamp(Timestamp ucl_create_timestamp) {
		this.ucl_create_timestamp = ucl_create_timestamp;
	}

}
