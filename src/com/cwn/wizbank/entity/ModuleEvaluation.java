package com.cwn.wizbank.entity;

import java.util.Date;


public class ModuleEvaluation implements java.io.Serializable {
	private static final long serialVersionUID = -4697974169442338545L;
	public static final String SING_PROGRESS = "I";

    public static final String SING_COMPLETE = "C";

    public static final String SING_PASS = "P";

    public static final String SING_FAIL = "F";
		/**
		 * null
		 **/
		Integer mov_cos_id;
		/**
		 * pk
		 * null
		 **/
		Integer mov_ent_id;
		/**
		 * pk
		 * null
		 **/
		Integer mov_mod_id;
		/**
		 * null
		 **/
		Date mov_last_acc_datetime;
		/**
		 * null
		 **/
		String mov_ele_loc;
		/**
		 * null
		 **/
		Double mov_total_time;
		/**
		 * null
		 **/
		Integer mov_total_attempt;
		/**
		 * null
		 **/
		String mov_status;
		/**
		 * null
		 **/
		Double mov_score;
		/**
		 * null
		 **/
		Double mov_max_score;
		/**
		 * null
		 **/
		Double mov_min_score;
		/**
		 * null
		 **/
		String mov_core_lesson;
		/**
		 * null
		 **/
		String mov_status_flag;
		/**
		 * null
		 **/
		String mov_aicc_score;
		/**
		 * null
		 **/
		String mov_credit;
		/**
		 * null
		 **/
		String mov_data_xml;
		/**
		 * null
		 **/
		String mov_create_usr_id;
		/**
		 * null
		 **/
		Date mov_create_timestamp;
		/**
		 * null
		 **/
		String mov_update_usr_id;
		/**
		 * null
		 **/
		Date mov_update_timestamp;
		/**
		 * pk
		 * null
		 **/
		Integer mov_tkh_id;
		/**
		 * null
		 **/
		Integer mov_not_mark_ind;
	
		public ModuleEvaluation(){
		}
	
		public Integer getMov_cos_id(){
			return this.mov_cos_id;
		}		
		public void setMov_cos_id(Integer mov_cos_id){
			this.mov_cos_id = mov_cos_id;
		}
		public Integer getMov_ent_id(){
			return this.mov_ent_id;
		}		
		public void setMov_ent_id(Integer mov_ent_id){
			this.mov_ent_id = mov_ent_id;
		}
		public Integer getMov_mod_id(){
			return this.mov_mod_id;
		}		
		public void setMov_mod_id(Integer mov_mod_id){
			this.mov_mod_id = mov_mod_id;
		}
		public Date getMov_last_acc_datetime(){
			return this.mov_last_acc_datetime;
		}		
		public void setMov_last_acc_datetime(Date mov_last_acc_datetime){
			this.mov_last_acc_datetime = mov_last_acc_datetime;
		}
		public String getMov_ele_loc(){
			return this.mov_ele_loc;
		}		
		public void setMov_ele_loc(String mov_ele_loc){
			this.mov_ele_loc = mov_ele_loc;
		}
		public Double getMov_total_time(){
			return this.mov_total_time;
		}		
		public void setMov_total_time(Double mov_total_time){
			this.mov_total_time = mov_total_time;
		}
		public Integer getMov_total_attempt(){
			return this.mov_total_attempt;
		}		
		public void setMov_total_attempt(Integer mov_total_attempt){
			this.mov_total_attempt = mov_total_attempt;
		}
		public String getMov_status(){
			return this.mov_status;
		}		
		public void setMov_status(String mov_status){
			this.mov_status = mov_status;
		}
		public Double getMov_score(){
			return this.mov_score;
		}		
		public void setMov_score(Double mov_score){
			this.mov_score = mov_score;
		}
		public Double getMov_max_score(){
			return this.mov_max_score;
		}		
		public void setMov_max_score(Double mov_max_score){
			this.mov_max_score = mov_max_score;
		}
		public Double getMov_min_score(){
			return this.mov_min_score;
		}		
		public void setMov_min_score(Double mov_min_score){
			this.mov_min_score = mov_min_score;
		}
		public String getMov_core_lesson(){
			return this.mov_core_lesson;
		}		
		public void setMov_core_lesson(String mov_core_lesson){
			this.mov_core_lesson = mov_core_lesson;
		}
		public String getMov_status_flag(){
			return this.mov_status_flag;
		}		
		public void setMov_status_flag(String mov_status_flag){
			this.mov_status_flag = mov_status_flag;
		}
		public String getMov_aicc_score(){
			return this.mov_aicc_score;
		}		
		public void setMov_aicc_score(String mov_aicc_score){
			this.mov_aicc_score = mov_aicc_score;
		}
		public String getMov_credit(){
			return this.mov_credit;
		}		
		public void setMov_credit(String mov_credit){
			this.mov_credit = mov_credit;
		}
		public String getMov_data_xml(){
			return this.mov_data_xml;
		}		
		public void setMov_data_xml(String mov_data_xml){
			this.mov_data_xml = mov_data_xml;
		}
		public String getMov_create_usr_id(){
			return this.mov_create_usr_id;
		}		
		public void setMov_create_usr_id(String mov_create_usr_id){
			this.mov_create_usr_id = mov_create_usr_id;
		}
		public Date getMov_create_timestamp(){
			return this.mov_create_timestamp;
		}		
		public void setMov_create_timestamp(Date mov_create_timestamp){
			this.mov_create_timestamp = mov_create_timestamp;
		}
		public String getMov_update_usr_id(){
			return this.mov_update_usr_id;
		}		
		public void setMov_update_usr_id(String mov_update_usr_id){
			this.mov_update_usr_id = mov_update_usr_id;
		}
		public Date getMov_update_timestamp(){
			return this.mov_update_timestamp;
		}		
		public void setMov_update_timestamp(Date mov_update_timestamp){
			this.mov_update_timestamp = mov_update_timestamp;
		}
		public Integer getMov_tkh_id(){
			return this.mov_tkh_id;
		}		
		public void setMov_tkh_id(Integer mov_tkh_id){
			this.mov_tkh_id = mov_tkh_id;
		}
		public Integer getMov_not_mark_ind(){
			return this.mov_not_mark_ind;
		}		
		public void setMov_not_mark_ind(Integer mov_not_mark_ind){
			this.mov_not_mark_ind = mov_not_mark_ind;
		}
	
}