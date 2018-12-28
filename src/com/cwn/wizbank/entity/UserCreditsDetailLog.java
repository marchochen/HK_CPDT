package com.cwn.wizbank.entity;

import java.util.Date;


public class UserCreditsDetailLog implements java.io.Serializable {
	private static final long serialVersionUID = -7844625223718877414L;
		/**
		 * null
		 **/
		Long ucl_usr_ent_id;
		/**
		 * null
		 **/
		Integer ucl_bpt_id;
		/**
		 * null
		 **/
		String ucl_relation_type;
		/**
		 * null
		 **/
		Integer ucl_source_id;
		/**
		 * null
		 **/
		Double ucl_point;
		/**
		 * null
		 **/
		Date ucl_create_timestamp;
		/**
		 * null
		 **/
		String ucl_create_usr_id;
		/**
		 * null
		 **/
		Integer ucl_app_id;
		/**
		 * 总积分
		 **/
		Double total_credits;
	
		CreditsType creditsType;
		
		AeItem aeItem;
		
		public UserCreditsDetailLog(){
		}
	
		public Long getUcl_usr_ent_id(){
			return this.ucl_usr_ent_id;
		}		
		public void setUcl_usr_ent_id(Long ucl_usr_ent_id){
			this.ucl_usr_ent_id = ucl_usr_ent_id;
		}
		public Integer getUcl_bpt_id(){
			return this.ucl_bpt_id;
		}		
		public void setUcl_bpt_id(Integer ucl_bpt_id){
			this.ucl_bpt_id = ucl_bpt_id;
		}
		public String getUcl_relation_type(){
			return this.ucl_relation_type;
		}		
		public void setUcl_relation_type(String ucl_relation_type){
			this.ucl_relation_type = ucl_relation_type;
		}
		public Integer getUcl_source_id(){
			return this.ucl_source_id;
		}		
		public void setUcl_source_id(Integer ucl_source_id){
			this.ucl_source_id = ucl_source_id;
		}
		public Double getUcl_point(){
			return this.ucl_point;
		}		
		public void setUcl_point(Double ucl_point){
			this.ucl_point = ucl_point;
		}
		public Date getUcl_create_timestamp(){
			return this.ucl_create_timestamp;
		}		
		public void setUcl_create_timestamp(Date ucl_create_timestamp){
			this.ucl_create_timestamp = ucl_create_timestamp;
		}
		public String getUcl_create_usr_id(){
			return this.ucl_create_usr_id;
		}		
		public void setUcl_create_usr_id(String ucl_create_usr_id){
			this.ucl_create_usr_id = ucl_create_usr_id;
		}
		public Integer getUcl_app_id(){
			return this.ucl_app_id;
		}		
		public void setUcl_app_id(Integer ucl_app_id){
			this.ucl_app_id = ucl_app_id;
		}
		public CreditsType getCreditsType() {
			return creditsType;
		}
		public void setCreditsType(CreditsType creditsType) {
			this.creditsType = creditsType;
		}
		public AeItem getAeItem() {
			return aeItem;
		}
		public void setAeItem(AeItem aeItem) {
			this.aeItem = aeItem;
		}
		public Double getTotal_credits() {
			return total_credits;
		}
		public void setTotal_credits(Double total_credits) {
			this.total_credits = total_credits;
		}
	
}