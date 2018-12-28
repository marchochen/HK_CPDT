package com.cw.wizbank.instructor;

import java.io.Serializable;
import java.sql.Timestamp;

import com.cw.wizbank.qdb.dbEntity;

public class Instructor extends dbEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	// 讲师类型
	public final static String TYPE_PART_TIME = "P";// 兼职
	public final static String TYPE_FULL_TIME = "F";// 专职
	// 讲师属性
	public final static String PROPERTY_HEAD = "H";// 兼职
	public final static String PROPERTY_BRANCH = "B";// 专职
	// 讲师级别
	public final static String LEVEL_JUNIOR = "J";// 初级
	public final static String LEVEL_DEDIUM = "M";// 中级
	public final static String LEVEL_SENIOR = "S";// 高级
	public final static String LEVEL_DISTINGUISHED = "D";// 特聘
	// 类型
	public final static String TYPE_MARK_EXT = "EXT";// 外部讲师
	public final static String TYPE_MARK_INT = "IN";// 内部讲师
	// 状态
	public final static String STATUS_OK = "OK"; // 正常
	public final static String STATUS_DELETE = "DELETE";// 已删除

	// InstructorInf
	public long iti_ent_id;
	public String iti_name;
	public String iti_gender;
	public Timestamp iti_bday;
	public String iti_mobile;
	public String iti_email;
	public String iti_img;
	public String iti_introduction;
	public String iti_level;
	public String iti_cos_type;
	public String iti_main_course;
	public String iti_type;
	public String iti_property;
	public String iti_highest_educational;
	public String iti_graduate_institutions;
	public String iti_address;
	public String iti_work_experience;
	public String iti_education_experience;
	public String iti_training_experience;
	public String iti_expertise_areas;
	public String iti_good_industry;
	public String iti_training_company;
	public String iti_training_contacts;
	public String iti_training_tel;
	public String iti_training_email;
	public String iti_training_address;
	public String iti_status;
	public String iti_type_mark;
	public float iti_score;
	public String iti_create_usr_id;
	public Timestamp iti_create_datetime;
	public String iti_upd_usr_id;
	public Timestamp iti_upd_datetime;

	public String iti_sks_title; // 内部讲师的岗位title
	public Timestamp iti_join_datetime; // 内部讲师的加入公司时间
	public String iti_user_group; // 内部讲师的用户组
	public String iti_user_grade; // 内部讲师的职级
	
	public int iti_recommend;
	
	private Long iti_tcr_id;


	public long getEnt_id() {
		return ent_id;
	}

	public void setEnt_id(long ent_id) {
		this.ent_id = ent_id;
	}

	public String getEnt_type() {
		return ent_type;
	}

	public void setEnt_type(String ent_type) {
		this.ent_type = ent_type;
	}

	public Timestamp getEnt_upd_date() {
		return ent_upd_date;
	}

	public void setEnt_upd_date(Timestamp ent_upd_date) {
		this.ent_upd_date = ent_upd_date;
	}

	public boolean isEnt_syn_ind() {
		return ent_syn_ind;
	}

	public void setEnt_syn_ind(boolean ent_syn_ind) {
		this.ent_syn_ind = ent_syn_ind;
	}

	public Timestamp getEnt_syn_date() {
		return ent_syn_date;
	}

	public void setEnt_syn_date(Timestamp ent_syn_date) {
		this.ent_syn_date = ent_syn_date;
	}

	public String getEnt_ste_uid() {
		return ent_ste_uid;
	}

	public void setEnt_ste_uid(String ent_ste_uid) {
		this.ent_ste_uid = ent_ste_uid;
	}

	public Timestamp getEnt_delete_timestamp() {
		return ent_delete_timestamp;
	}

	public void setEnt_delete_timestamp(Timestamp ent_delete_timestamp) {
		this.ent_delete_timestamp = ent_delete_timestamp;
	}

	public long getIti_ent_id() {
		return iti_ent_id;
	}

	public void setIti_ent_id(long iti_ent_id) {
		this.iti_ent_id = iti_ent_id;
	}

	public String getIti_name() {
		return iti_name;
	}

	public void setIti_name(String iti_name) {
		this.iti_name = iti_name;
	}

	public String getIti_gender() {
		return iti_gender;
	}

	public void setIti_gender(String iti_gender) {
		this.iti_gender = iti_gender;
	}

	public Timestamp getIti_bday() {
		return iti_bday;
	}

	public void setIti_bday(Timestamp iti_bday) {
		this.iti_bday = iti_bday;
	}

	public String getIti_mobile() {
		return iti_mobile;
	}

	public void setIti_mobile(String iti_mobile) {
		this.iti_mobile = iti_mobile;
	}

	public String getIti_email() {
		return iti_email;
	}

	public void setIti_email(String iti_email) {
		this.iti_email = iti_email;
	}

	public String getIti_img() {
		return iti_img;
	}

	public void setIti_img(String iti_img) {
		this.iti_img = iti_img;
	}

	public String getIti_introduction() {
		return iti_introduction;
	}

	public void setIti_introduction(String iti_introduction) {
		this.iti_introduction = iti_introduction;
	}

	public String getIti_level() {
		return iti_level;
	}

	public void setIti_level(String iti_level) {
		this.iti_level = iti_level;
	}

	public String getIti_cos_type() {
		return iti_cos_type;
	}

	public void setIti_cos_type(String iti_cos_type) {
		this.iti_cos_type = iti_cos_type;
	}

	public String getIti_main_course() {
		return iti_main_course;
	}

	public void setIti_main_course(String iti_main_course) {
		this.iti_main_course = iti_main_course;
	}

	public String getIti_type() {
		return iti_type;
	}

	public void setIti_type(String iti_type) {
		this.iti_type = iti_type;
	}

	public String getIti_property() {
		return iti_property;
	}

	public void setIti_property(String iti_property) {
		this.iti_property = iti_property;
	}

	public String getIti_highest_educational() {
		return iti_highest_educational;
	}

	public void setIti_highest_educational(String iti_highest_educational) {
		this.iti_highest_educational = iti_highest_educational;
	}

	public String getIti_graduate_institutions() {
		return iti_graduate_institutions;
	}

	public void setIti_graduate_institutions(String iti_graduate_institutions) {
		this.iti_graduate_institutions = iti_graduate_institutions;
	}

	public String getIti_address() {
		return iti_address;
	}

	public void setIti_address(String iti_address) {
		this.iti_address = iti_address;
	}

	public String getIti_work_experience() {
		return iti_work_experience;
	}

	public void setIti_work_experience(String iti_work_experience) {
		this.iti_work_experience = iti_work_experience;
	}

	public String getIti_education_experience() {
		return iti_education_experience;
	}

	public void setIti_education_experience(String iti_education_experience) {
		this.iti_education_experience = iti_education_experience;
	}

	public String getIti_training_experience() {
		return iti_training_experience;
	}

	public void setIti_training_experience(String iti_training_experience) {
		this.iti_training_experience = iti_training_experience;
	}

	public String getIti_expertise_areas() {
		return iti_expertise_areas;
	}

	public void setIti_expertise_areas(String iti_expertise_areas) {
		this.iti_expertise_areas = iti_expertise_areas;
	}

	public String getIti_good_industry() {
		return iti_good_industry;
	}

	public void setIti_good_industry(String iti_good_industry) {
		this.iti_good_industry = iti_good_industry;
	}

	public String getIti_training_company() {
		return iti_training_company;
	}

	public void setIti_training_company(String iti_training_company) {
		this.iti_training_company = iti_training_company;
	}

	public String getIti_training_contacts() {
		return iti_training_contacts;
	}

	public void setIti_training_contacts(String iti_training_contacts) {
		this.iti_training_contacts = iti_training_contacts;
	}

	public String getIti_training_tel() {
		return iti_training_tel;
	}

	public void setIti_training_tel(String iti_training_tel) {
		this.iti_training_tel = iti_training_tel;
	}

	public String getIti_training_email() {
		return iti_training_email;
	}

	public void setIti_training_email(String iti_training_email) {
		this.iti_training_email = iti_training_email;
	}

	public String getIti_training_address() {
		return iti_training_address;
	}

	public void setIti_training_address(String iti_training_address) {
		this.iti_training_address = iti_training_address;
	}

	public String getIti_status() {
		return iti_status;
	}

	public void setIti_status(String iti_status) {
		this.iti_status = iti_status;
	}

	public String getIti_type_mark() {
		return iti_type_mark;
	}

	public void setIti_type_mark(String iti_type_mark) {
		this.iti_type_mark = iti_type_mark;
	}

	public String getIti_create_usr_id() {
		return iti_create_usr_id;
	}

	public void setIti_create_usr_id(String iti_create_usr_id) {
		this.iti_create_usr_id = iti_create_usr_id;
	}

	public Timestamp getIti_create_datetime() {
		return iti_create_datetime;
	}

	public void setIti_create_datetime(Timestamp iti_create_datetime) {
		this.iti_create_datetime = iti_create_datetime;
	}

	public String getIti_upd_usr_id() {
		return iti_upd_usr_id;
	}

	public void setIti_upd_usr_id(String iti_upd_usr_id) {
		this.iti_upd_usr_id = iti_upd_usr_id;
	}

	public Timestamp getIti_upd_datetime() {
		return iti_upd_datetime;
	}

	public void setIti_upd_datetime(Timestamp iti_upd_datetime) {
		this.iti_upd_datetime = iti_upd_datetime;
	}

	public String getIti_sks_title() {
		return iti_sks_title;
	}

	public void setIti_sks_title(String iti_sks_title) {
		this.iti_sks_title = iti_sks_title;
	}

	public float getIti_score() {
		return iti_score;
	}

	public void setIti_score(float iti_score) {
		this.iti_score = iti_score;
	}

	public String getIti_user_group() {
		return iti_user_group;
	}

	public void setIti_user_group(String iti_user_group) {
		this.iti_user_group = iti_user_group;
	}

	public String getIti_user_grade() {
		return iti_user_grade;
	}

	public void setIti_user_grade(String iti_user_grade) {
		this.iti_user_grade = iti_user_grade;
	}

	public Timestamp getIti_join_datetime() {
		return iti_join_datetime;
	}

	public void setIti_join_datetime(Timestamp iti_join_datetime) {
		this.iti_join_datetime = iti_join_datetime;
	}

	public int getIti_recommend() {
		return iti_recommend;
	}

	public void setIti_recommend(int iti_recommend) {
		this.iti_recommend = iti_recommend;
	}

	public Long getIti_tcr_id() {
		return iti_tcr_id;
	}

	public void setIti_tcr_id(Long iti_tcr_id) {
		this.iti_tcr_id = iti_tcr_id;
	}

}