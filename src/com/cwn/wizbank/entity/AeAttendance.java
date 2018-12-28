package com.cwn.wizbank.entity;

import java.util.Date;


public class AeAttendance implements java.io.Serializable {
	private static final long serialVersionUID = -2387293479244373566L;
		/**
		 * pk
		 * null
		 **/
		Integer att_app_id;
		/**
		 * null
		 **/
		Integer att_ats_id;
		/**
		 * null
		 **/
		String att_remark;
		/**
		 * null
		 **/
		String att_create_usr_id;
		/**
		 * null
		 **/
		Date att_create_timestamp;
		/**
		 * null
		 **/
		String att_update_usr_id;
		/**
		 * null
		 **/
		Date att_update_timestamp;
		/**
		 * null
		 **/
		Double att_rate;
		/**
		 * null
		 **/
		Integer att_itm_id;
		/**
		 * null
		 **/
		Date att_timestamp;
		/**
		 * null
		 **/
		String att_rate_remark;
	
		public AeAttendance(){
		}
		
		private AeApplication aeApplication;
	
		public Integer getAtt_app_id(){
			return this.att_app_id;
		}		
		public void setAtt_app_id(Integer att_app_id){
			this.att_app_id = att_app_id;
		}
		public Integer getAtt_ats_id(){
			return this.att_ats_id;
		}		
		public void setAtt_ats_id(Integer att_ats_id){
			this.att_ats_id = att_ats_id;
		}
		public String getAtt_remark(){
			return this.att_remark;
		}		
		public void setAtt_remark(String att_remark){
			this.att_remark = att_remark;
		}
		public String getAtt_create_usr_id(){
			return this.att_create_usr_id;
		}		
		public void setAtt_create_usr_id(String att_create_usr_id){
			this.att_create_usr_id = att_create_usr_id;
		}
		public Date getAtt_create_timestamp(){
			return this.att_create_timestamp;
		}		
		public void setAtt_create_timestamp(Date att_create_timestamp){
			this.att_create_timestamp = att_create_timestamp;
		}
		public String getAtt_update_usr_id(){
			return this.att_update_usr_id;
		}		
		public void setAtt_update_usr_id(String att_update_usr_id){
			this.att_update_usr_id = att_update_usr_id;
		}
		public Date getAtt_update_timestamp(){
			return this.att_update_timestamp;
		}		
		public void setAtt_update_timestamp(Date att_update_timestamp){
			this.att_update_timestamp = att_update_timestamp;
		}
		public Double getAtt_rate(){
			return this.att_rate;
		}		
		public void setAtt_rate(Double att_rate){
			this.att_rate = att_rate;
		}
		public Integer getAtt_itm_id(){
			return this.att_itm_id;
		}		
		public void setAtt_itm_id(Integer att_itm_id){
			this.att_itm_id = att_itm_id;
		}
		public Date getAtt_timestamp(){
			return this.att_timestamp;
		}		
		public void setAtt_timestamp(Date att_timestamp){
			this.att_timestamp = att_timestamp;
		}
		public String getAtt_rate_remark(){
			return this.att_rate_remark;
		}		
		public void setAtt_rate_remark(String att_rate_remark){
			this.att_rate_remark = att_rate_remark;
		}
		public AeApplication getAeApplication() {
			return aeApplication;
		}
		public void setAeApplication(AeApplication aeApplication) {
			this.aeApplication = aeApplication;
		}
	
}