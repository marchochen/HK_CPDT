package com.cwn.wizbank.entity;

import java.util.Date;


public class IntegCompleteCondition implements java.io.Serializable {
	private static final long serialVersionUID = -99633348335461255L;
	
	public static final String COMPULSORY = "COMPULSORY";
	public static final String ELECTIVE = "ELECTIVE";

		/**
		 * pk
		 * null
		 **/
		Integer icd_id;
		/**
		 * null
		 **/
		Integer icd_icc_id;
		/**
		 * null
		 **/
		Integer icd_completed_item_count;
		/**
		 * null
		 **/
		String icd_type;
		/**
		 * null
		 **/
		Date icd_create_timestamp;
		/**
		 * null
		 **/
		String icd_create_usr_id;
		/**
		 * null
		 **/
		Date icd_update_timestamp;
		/**
		 * null
		 **/
		String icd_update_usr_id;
	
		public IntegCompleteCondition(){
		}
	
		public Integer getIcd_id(){
			return this.icd_id;
		}		
		public void setIcd_id(Integer icd_id){
			this.icd_id = icd_id;
		}
		public Integer getIcd_icc_id(){
			return this.icd_icc_id;
		}		
		public void setIcd_icc_id(Integer icd_icc_id){
			this.icd_icc_id = icd_icc_id;
		}
		public Integer getIcd_completed_item_count(){
			return this.icd_completed_item_count;
		}		
		public void setIcd_completed_item_count(Integer icd_completed_item_count){
			this.icd_completed_item_count = icd_completed_item_count;
		}
		public String getIcd_type(){
			return this.icd_type;
		}		
		public void setIcd_type(String icd_type){
			this.icd_type = icd_type;
		}
		public Date getIcd_create_timestamp(){
			return this.icd_create_timestamp;
		}		
		public void setIcd_create_timestamp(Date icd_create_timestamp){
			this.icd_create_timestamp = icd_create_timestamp;
		}
		public String getIcd_create_usr_id(){
			return this.icd_create_usr_id;
		}		
		public void setIcd_create_usr_id(String icd_create_usr_id){
			this.icd_create_usr_id = icd_create_usr_id;
		}
		public Date getIcd_update_timestamp(){
			return this.icd_update_timestamp;
		}		
		public void setIcd_update_timestamp(Date icd_update_timestamp){
			this.icd_update_timestamp = icd_update_timestamp;
		}
		public String getIcd_update_usr_id(){
			return this.icd_update_usr_id;
		}		
		public void setIcd_update_usr_id(String icd_update_usr_id){
			this.icd_update_usr_id = icd_update_usr_id;
		}
	
}