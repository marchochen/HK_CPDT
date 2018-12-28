package com.cw.wizbank.dataMigrate.imp.bean;


public class CPDRegistrationBean extends ImportObject {
	
	public String user_id;
	public String License_type;
	public String Reg_no;
	public String Reg_date;
	public String De_reg_date;
	public String CPD_group_code;
	public String Initial_date;
	public String Expiry_date;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getLicense_type() {
		return License_type;
	}
	public void setLicense_type(String license_type) {
		License_type = license_type;
	}
	public String getReg_no() {
		return Reg_no;
	}
	public void setReg_no(String reg_no) {
		Reg_no = reg_no;
	}
	public String getReg_date() {
		return Reg_date;
	}
	public void setReg_date(String reg_date) {
		Reg_date = reg_date;
	}
	public String getDe_reg_date() {
		return De_reg_date;
	}
	public void setDe_reg_date(String de_reg_date) {
		De_reg_date = de_reg_date;
	}
	public String getCPD_group_code() {
		return CPD_group_code;
	}
	public void setCPD_group_code(String cPD_group_code) {
		CPD_group_code = cPD_group_code;
	}
	public String getInitial_date() {
		return Initial_date;
	}
	public void setInitial_date(String initial_date) {
		Initial_date = initial_date;
	}
	public String getExpiry_date() {
		return Expiry_date;
	}
	public void setExpiry_date(String expiry_date) {
		Expiry_date = expiry_date;
	}
	
	
}
