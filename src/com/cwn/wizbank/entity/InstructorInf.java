package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;


public class InstructorInf implements java.io.Serializable {
		private static final long serialVersionUID = -4128459276885763228L;
	
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
		/**
		 * pk
		 * null
		 **/
		Integer iti_ent_id;
		/**
		 * null
		 **/
		String iti_name;
		/**
		 * null
		 **/
		String iti_gender;
		/**
		 * null
		 **/
		Date iti_bday;
		/**
		 * null
		 **/
		String iti_mobile;
		/**
		 * null
		 **/
		String iti_email;
		/**
		 * null
		 **/
		String iti_img;
		/**
		 * null
		 **/
		String iti_introduction;
		/**
		 * null
		 **/
		String iti_level;
		/**
		 * null
		 **/
		String iti_cos_type;
		/**
		 * null
		 **/
		String iti_main_course;
		/**
		 * null
		 **/
		String iti_type;
		/**
		 * null
		 **/
		String iti_property;
		/**
		 * null
		 **/
		String iti_highest_educational;
		/**
		 * null
		 **/
		String iti_graduate_institutions;
		/**
		 * null
		 **/
		String iti_address;
		/**
		 * null
		 **/
		String iti_work_experience;
		/**
		 * null
		 **/
		String iti_education_experience;
		/**
		 * null
		 **/
		String iti_training_experience;
		/**
		 * null
		 **/
		String iti_expertise_areas;
		/**
		 * null
		 **/
		String iti_good_industry;
		/**
		 * null
		 **/
		String iti_training_company;
		/**
		 * null
		 **/
		String iti_training_contacts;
		/**
		 * null
		 **/
		String iti_training_tel;
		/**
		 * null
		 **/
		String iti_training_email;
		/**
		 * null
		 **/
		String iti_training_address;
		/**
		 * null
		 **/
		String iti_status;
		/**
		 * null
		 **/
		String iti_type_mark;
		/**
		 * null
		 **/
		Double iti_score;
		/**
		 * null
		 **/
		Date iti_create_datetime;
		/**
		 * null
		 **/
		String iti_create_user_id;
		/**
		 * null
		 **/
		Date iti_update_datetime;
		/**
		 * null
		 **/
		String iti_update_user_id;
		/**
		 * null
		 **/
		Integer iti_tcr_id;
		
		int iti_recommend;
	
		
		List<InstructorCos> cosList;
		
		String upt_title;
		public InstructorInf(){
		}
	
		public Integer getIti_ent_id(){
			return this.iti_ent_id;
		}		
		public void setIti_ent_id(Integer iti_ent_id){
			this.iti_ent_id = iti_ent_id;
		}
		public String getIti_name(){
			return this.iti_name;
		}		
		public void setIti_name(String iti_name){
			this.iti_name = iti_name;
		}
		public String getIti_gender(){
			return this.iti_gender;
		}		
		public void setIti_gender(String iti_gender){
			this.iti_gender = iti_gender;
		}
		public Date getIti_bday(){
			return this.iti_bday;
		}		
		public void setIti_bday(Date iti_bday){
			this.iti_bday = iti_bday;
		}
		public String getIti_mobile(){
			return this.iti_mobile;
		}		
		public void setIti_mobile(String iti_mobile){
			this.iti_mobile = iti_mobile;
		}
		public String getIti_email(){
			return this.iti_email;
		}		
		public void setIti_email(String iti_email){
			this.iti_email = iti_email;
		}
		public String getIti_img(){
			return this.iti_img;
		}		
		public void setIti_img(String iti_img){
			this.iti_img = iti_img;
		}
		public String getIti_introduction(){
			return this.iti_introduction;
		}		
		public void setIti_introduction(String iti_introduction){
			this.iti_introduction = iti_introduction;
		}
		public String getIti_level(){
			return this.iti_level;
		}		
		public void setIti_level(String iti_level){
			this.iti_level = iti_level;
		}
		public String getIti_cos_type(){
			return this.iti_cos_type;
		}		
		public void setIti_cos_type(String iti_cos_type){
			this.iti_cos_type = iti_cos_type;
		}
		public String getIti_main_course(){
			return this.iti_main_course;
		}		
		public void setIti_main_course(String iti_main_course){
			this.iti_main_course = iti_main_course;
		}
		public String getIti_type(){
			return this.iti_type;
		}		
		public void setIti_type(String iti_type){
			this.iti_type = iti_type;
		}
		public String getIti_property(){
			return this.iti_property;
		}		
		public void setIti_property(String iti_property){
			this.iti_property = iti_property;
		}
		public String getIti_highest_educational(){
			return this.iti_highest_educational;
		}		
		public void setIti_highest_educational(String iti_highest_educational){
			this.iti_highest_educational = iti_highest_educational;
		}
		public String getIti_graduate_institutions(){
			return this.iti_graduate_institutions;
		}		
		public void setIti_graduate_institutions(String iti_graduate_institutions){
			this.iti_graduate_institutions = iti_graduate_institutions;
		}
		public String getIti_address(){
			return this.iti_address;
		}		
		public void setIti_address(String iti_address){
			this.iti_address = iti_address;
		}
		public String getIti_work_experience(){
			return this.iti_work_experience;
		}		
		public void setIti_work_experience(String iti_work_experience){
			this.iti_work_experience = iti_work_experience;
		}
		public String getIti_education_experience(){
			return this.iti_education_experience;
		}		
		public void setIti_education_experience(String iti_education_experience){
			this.iti_education_experience = iti_education_experience;
		}
		public String getIti_training_experience(){
			return this.iti_training_experience;
		}		
		public void setIti_training_experience(String iti_training_experience){
			this.iti_training_experience = iti_training_experience;
		}
		public String getIti_expertise_areas(){
			return this.iti_expertise_areas;
		}		
		public void setIti_expertise_areas(String iti_expertise_areas){
			this.iti_expertise_areas = iti_expertise_areas;
		}
		public String getIti_good_industry(){
			return this.iti_good_industry;
		}		
		public void setIti_good_industry(String iti_good_industry){
			this.iti_good_industry = iti_good_industry;
		}
		public String getIti_training_company(){
			return this.iti_training_company;
		}		
		public void setIti_training_company(String iti_training_company){
			this.iti_training_company = iti_training_company;
		}
		public String getIti_training_contacts(){
			return this.iti_training_contacts;
		}		
		public void setIti_training_contacts(String iti_training_contacts){
			this.iti_training_contacts = iti_training_contacts;
		}
		public String getIti_training_tel(){
			return this.iti_training_tel;
		}		
		public void setIti_training_tel(String iti_training_tel){
			this.iti_training_tel = iti_training_tel;
		}
		public String getIti_training_email(){
			return this.iti_training_email;
		}		
		public void setIti_training_email(String iti_training_email){
			this.iti_training_email = iti_training_email;
		}
		public String getIti_training_address(){
			return this.iti_training_address;
		}		
		public void setIti_training_address(String iti_training_address){
			this.iti_training_address = iti_training_address;
		}
		public String getIti_status(){
			return this.iti_status;
		}		
		public void setIti_status(String iti_status){
			this.iti_status = iti_status;
		}
		public String getIti_type_mark(){
			return this.iti_type_mark;
		}		
		public void setIti_type_mark(String iti_type_mark){
			this.iti_type_mark = iti_type_mark;
		}
		public Double getIti_score(){
			return this.iti_score;
		}		
		public void setIti_score(Double iti_score){
			this.iti_score = iti_score;
		}
		public Date getIti_create_datetime(){
			return this.iti_create_datetime;
		}		
		public void setIti_create_datetime(Date iti_create_datetime){
			this.iti_create_datetime = iti_create_datetime;
		}
		public String getIti_create_user_id(){
			return this.iti_create_user_id;
		}		
		public void setIti_create_user_id(String iti_create_user_id){
			this.iti_create_user_id = iti_create_user_id;
		}
		public Date getIti_update_datetime(){
			return this.iti_update_datetime;
		}		
		public void setIti_update_datetime(Date iti_update_datetime){
			this.iti_update_datetime = iti_update_datetime;
		}
		public String getIti_update_user_id(){
			return this.iti_update_user_id;
		}		
		public void setIti_update_user_id(String iti_update_user_id){
			this.iti_update_user_id = iti_update_user_id;
		}
		public Integer getIti_tcr_id(){
			return this.iti_tcr_id;
		}		
		public void setIti_tcr_id(Integer iti_tcr_id){
			this.iti_tcr_id = iti_tcr_id;
		}

		public List<InstructorCos> getCosList() {
			return cosList;
		}

		public void setCosList(List<InstructorCos> cosList) {
			this.cosList = cosList;
		}

		public int getIti_recommend() {
			return iti_recommend;
		}

		public void setIti_recommend(int iti_recommend) {
			this.iti_recommend = iti_recommend;
		}

		public String getUpt_title() {
			return upt_title;
		}

		public void setUpt_title(String upt_title) {
			this.upt_title = upt_title;
		}
	
}