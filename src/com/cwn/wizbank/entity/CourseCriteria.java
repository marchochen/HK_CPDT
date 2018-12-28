package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;


public class CourseCriteria implements java.io.Serializable {
	private static final long serialVersionUID = -6856234751528628261L;
		/**
		 * pk
		 * null
		 **/
		Integer ccr_id;
		/**
		 * null
		 **/
		Double ccr_pass_score;
		/**
		 * null
		 **/
		Integer ccr_duration;
		/**
		 * null
		 **/
		Boolean ccr_pass_ind;
		/**
		 * null
		 **/
		Boolean ccr_all_cond_ind;
		/**
		 * null
		 **/
		Date ccr_create_timestamp;
		/**
		 * null
		 **/
		String ccr_create_usr_id;
		/**
		 * null
		 **/
		Date ccr_upd_timestamp;
		/**
		 * null
		 **/
		String ccr_upd_usr_id;
		/**
		 * null
		 **/
		String ccr_type;
		/**
		 * null
		 **/
		String ccr_upd_method;
		/**
		 * null
		 **/
		Integer ccr_itm_id;
		/**
		 * null
		 **/
		Integer ccr_attendance_rate;
		/**
		 * null
		 **/
		String ccr_offline_condition;
		/**
		 * null
		 **/
		Integer ccr_ccr_id_parent;
		List<CourseMeasurement>  cmt_lst ;
		List<CourseMeasurement>  score_itm_lst ;
		
		public CourseCriteria(){
		}
	
		public Integer getCcr_id(){
			return this.ccr_id;
		}		
		public void setCcr_id(Integer ccr_id){
			this.ccr_id = ccr_id;
		}
		public Double getCcr_pass_score(){
			return this.ccr_pass_score;
		}		
		public void setCcr_pass_score(Double ccr_pass_score){
			this.ccr_pass_score = ccr_pass_score;
		}
		public Integer getCcr_duration(){
			return this.ccr_duration;
		}		
		public void setCcr_duration(Integer ccr_duration){
			this.ccr_duration = ccr_duration;
		}
		public Boolean getCcr_pass_ind(){
			return this.ccr_pass_ind;
		}		
		public void setCcr_pass_ind(Boolean ccr_pass_ind){
			this.ccr_pass_ind = ccr_pass_ind;
		}
		public Boolean getCcr_all_cond_ind(){
			return this.ccr_all_cond_ind;
		}		
		public void setCcr_all_cond_ind(Boolean ccr_all_cond_ind){
			this.ccr_all_cond_ind = ccr_all_cond_ind;
		}
		public Date getCcr_create_timestamp(){
			return this.ccr_create_timestamp;
		}		
		public void setCcr_create_timestamp(Date ccr_create_timestamp){
			this.ccr_create_timestamp = ccr_create_timestamp;
		}
		public String getCcr_create_usr_id(){
			return this.ccr_create_usr_id;
		}		
		public void setCcr_create_usr_id(String ccr_create_usr_id){
			this.ccr_create_usr_id = ccr_create_usr_id;
		}
		public Date getCcr_upd_timestamp(){
			return this.ccr_upd_timestamp;
		}		
		public void setCcr_upd_timestamp(Date ccr_upd_timestamp){
			this.ccr_upd_timestamp = ccr_upd_timestamp;
		}
		public String getCcr_upd_usr_id(){
			return this.ccr_upd_usr_id;
		}		
		public void setCcr_upd_usr_id(String ccr_upd_usr_id){
			this.ccr_upd_usr_id = ccr_upd_usr_id;
		}
		public String getCcr_type(){
			return this.ccr_type;
		}		
		public void setCcr_type(String ccr_type){
			this.ccr_type = ccr_type;
		}
		public String getCcr_upd_method(){
			return this.ccr_upd_method;
		}		
		public void setCcr_upd_method(String ccr_upd_method){
			this.ccr_upd_method = ccr_upd_method;
		}
		public Integer getCcr_itm_id(){
			return this.ccr_itm_id;
		}		
		public void setCcr_itm_id(Integer ccr_itm_id){
			this.ccr_itm_id = ccr_itm_id;
		}
		public Integer getCcr_attendance_rate(){
			return this.ccr_attendance_rate;
		}		
		public void setCcr_attendance_rate(Integer ccr_attendance_rate){
			this.ccr_attendance_rate = ccr_attendance_rate;
		}
		public String getCcr_offline_condition(){
			return this.ccr_offline_condition;
		}		
		public void setCcr_offline_condition(String ccr_offline_condition){
			this.ccr_offline_condition = ccr_offline_condition;
		}
		public Integer getCcr_ccr_id_parent(){
			return this.ccr_ccr_id_parent;
		}		
		public void setCcr_ccr_id_parent(Integer ccr_ccr_id_parent){
			this.ccr_ccr_id_parent = ccr_ccr_id_parent;
		}
		
		public List<CourseMeasurement> getCmt_lst() {
			return cmt_lst;
		}

		public void setCmt_lst(List<CourseMeasurement> cmtLst) {
			cmt_lst = cmtLst;
		}
		
		public List<CourseMeasurement> getScore_itm_lst() {
			return score_itm_lst;
		}

		public void setScore_itm_lst(List<CourseMeasurement> scoreItmLst) {
			score_itm_lst = scoreItmLst;
		}

	
}