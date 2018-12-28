package com.cwn.wizbank.entity.vo;


public class UserVo {
	
	long usr_ent_id;
    String usr_login_id;
    String usr_name;
  	String usr_pwd;
    String usr_qq;
    String usr_email;
    String usr_phone;
    String usr_sex;
    String usr_del_ind;
    String usg_ent_id;
	String usg_grade;
	String usg_position;

	public String getUsr_name() {
		return usr_name;
	}

	public void setUsr_name(String usr_name) {
		this.usr_name = usr_name;
	}

	public String getUsr_pwd() {
		return usr_pwd;
	}

	public void setUsr_pwd(String usr_pwd) {
		this.usr_pwd = usr_pwd;
	}

	public String getUsr_qq() {
		return usr_qq;
	}

	public void setUsr_qq(String usr_qq) {
		this.usr_qq = usr_qq;
	}

	public String getUsr_email() {
		return usr_email;
	}

	public void setUsr_email(String usr_email) {
		this.usr_email = usr_email;
	}

	public String getUsr_sex() {
		return usr_sex;
	}

	public void setUsr_sex(String usr_sex) {
		this.usr_sex = usr_sex;
	}

	public String getUsr_del_ind() {
		return usr_del_ind;
	}

	public void setUsr_del_ind(String usr_del_ind) {
		this.usr_del_ind = usr_del_ind;
	}

	public String getUsg_ent_id() {
		return usg_ent_id;
	}

	public void setUsg_ent_id(String usg_ent_id) {
		this.usg_ent_id = usg_ent_id;
	}

	public String getUsr_login_id() {
		return usr_login_id;
	}

	public void setUsr_login_id(String usr_login_id) {
		this.usr_login_id = usr_login_id;
	}

	@Override
	public String toString() {
		return "UserVo [usr_ent_id=" + usr_ent_id + ", usr_login_id="
				+ usr_login_id + ", usr_name=" + usr_name + ", usr_pwd="
				+ usr_pwd + ", usr_qq=" + usr_qq + ", usr_email=" + usr_email
				+ ", usr_sex=" + usr_sex + ", usr_del_ind=" + usr_del_ind
				+ ", usg_grade=" + usg_grade + ", usg_position=" + usr_del_ind
				+ ", usg_ent_id=" + usg_ent_id + "]";
	}

	public long getUsr_ent_id() {
		return usr_ent_id;
	}

	public void setUsr_ent_id(long usr_ent_id) {
		this.usr_ent_id = usr_ent_id;
	}

	public String getUsr_phone() {
		return usr_phone;
	}

	public void setUsr_phone(String usr_phone) {
		this.usr_phone = usr_phone;
	}

	public String getUsg_grade() {
		return usg_grade;
	}

	public void setUsg_grade(String usg_grade) {
		this.usg_grade = usg_grade;
	}

	public String getUsg_position() {
		return usg_position;
	}

	public void setUsg_position(String usg_position) {
		this.usg_position = usg_position;
	}
}
