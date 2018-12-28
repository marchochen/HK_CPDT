package com.cwn.wizbank.entity;

import java.util.Date;


public class CourseModuleCriteria implements java.io.Serializable {
	private static final long serialVersionUID = -1422769921236852321L;
		/**
		 * pk
		 * null
		 **/
		Integer cmr_id;
		/**
		 * null
		 **/
		Integer cmr_ccr_id;
		/**
		 * null
		 **/
		Integer cmr_res_id;
		/**
		 * null
		 **/
		String cmr_status;
		/**
		 * null
		 **/
		Double cmr_contri_rate;
		/**
		 * null
		 **/
		Boolean cmr_is_contri_by_score;
		/**
		 * null
		 **/
		Boolean cmr_is_contri_on_status;
		/**
		 * null
		 **/
		Date cmr_create_timestamp;
		/**
		 * null
		 **/
		String cmr_create_usr_id;
		/**
		 * null
		 **/
		Date cmr_upd_timestamp;
		/**
		 * null
		 **/
		String cmr_upd_usr_id;
		/**
		 * null
		 **/
		Date cmr_del_timestamp;
		/**
		 * null
		 **/
		String cmr_status_desc_option;
		/**
		 * null
		 **/
		Integer cmr_cmr_id_parent;
	
		public CourseModuleCriteria(){
		}
	
		public Integer getCmr_id(){
			return this.cmr_id;
		}		
		public void setCmr_id(Integer cmr_id){
			this.cmr_id = cmr_id;
		}
		public Integer getCmr_ccr_id(){
			return this.cmr_ccr_id;
		}		
		public void setCmr_ccr_id(Integer cmr_ccr_id){
			this.cmr_ccr_id = cmr_ccr_id;
		}
		public Integer getCmr_res_id(){
			return this.cmr_res_id;
		}		
		public void setCmr_res_id(Integer cmr_res_id){
			this.cmr_res_id = cmr_res_id;
		}
		public String getCmr_status(){
			return this.cmr_status;
		}		
		public void setCmr_status(String cmr_status){
			this.cmr_status = cmr_status;
		}
		public Double getCmr_contri_rate(){
			return this.cmr_contri_rate;
		}		
		public void setCmr_contri_rate(Double cmr_contri_rate){
			this.cmr_contri_rate = cmr_contri_rate;
		}
		public Boolean getCmr_is_contri_by_score(){
			return this.cmr_is_contri_by_score;
		}		
		public void setCmr_is_contri_by_score(Boolean cmr_is_contri_by_score){
			this.cmr_is_contri_by_score = cmr_is_contri_by_score;
		}
		public Boolean getCmr_is_contri_on_status(){
			return this.cmr_is_contri_on_status;
		}		
		public void setCmr_is_contri_on_status(Boolean cmr_is_contri_on_status){
			this.cmr_is_contri_on_status = cmr_is_contri_on_status;
		}
		public Date getCmr_create_timestamp(){
			return this.cmr_create_timestamp;
		}		
		public void setCmr_create_timestamp(Date cmr_create_timestamp){
			this.cmr_create_timestamp = cmr_create_timestamp;
		}
		public String getCmr_create_usr_id(){
			return this.cmr_create_usr_id;
		}		
		public void setCmr_create_usr_id(String cmr_create_usr_id){
			this.cmr_create_usr_id = cmr_create_usr_id;
		}
		public Date getCmr_upd_timestamp(){
			return this.cmr_upd_timestamp;
		}		
		public void setCmr_upd_timestamp(Date cmr_upd_timestamp){
			this.cmr_upd_timestamp = cmr_upd_timestamp;
		}
		public String getCmr_upd_usr_id(){
			return this.cmr_upd_usr_id;
		}		
		public void setCmr_upd_usr_id(String cmr_upd_usr_id){
			this.cmr_upd_usr_id = cmr_upd_usr_id;
		}
		public Date getCmr_del_timestamp(){
			return this.cmr_del_timestamp;
		}		
		public void setCmr_del_timestamp(Date cmr_del_timestamp){
			this.cmr_del_timestamp = cmr_del_timestamp;
		}
		public String getCmr_status_desc_option(){
			return this.cmr_status_desc_option;
		}		
		public void setCmr_status_desc_option(String cmr_status_desc_option){
			this.cmr_status_desc_option = cmr_status_desc_option;
		}
		public Integer getCmr_cmr_id_parent(){
			return this.cmr_cmr_id_parent;
		}		
		public void setCmr_cmr_id_parent(Integer cmr_cmr_id_parent){
			this.cmr_cmr_id_parent = cmr_cmr_id_parent;
		}
	
}