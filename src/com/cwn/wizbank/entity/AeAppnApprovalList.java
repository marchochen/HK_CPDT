package com.cwn.wizbank.entity;

import java.util.Date;


public class AeAppnApprovalList implements java.io.Serializable {
	private static final long serialVersionUID = -1991319819315636365L;
		/**
		 * pk
		 * null
		 **/
		Integer aal_id;
		/**
		 * null
		 **/
		Integer aal_usr_ent_id;
		/**
		 * null
		 **/
		Integer aal_app_id;
		/**
		 * null
		 **/
		Integer aal_app_ent_id;
		/**
		 * null
		 **/
		String aal_approval_role;
		/**
		 * null
		 **/
		Integer aal_approval_usg_ent_id;
		/**
		 * null
		 **/
		String aal_status;
		/**
		 * null
		 **/
		Date aal_create_timestamp;
		/**
		 * null
		 **/
		Integer aal_action_taker_usr_ent_id;
		/**
		 * null
		 **/
		String aal_action_taker_approval_role;
		/**
		 * null
		 **/
		String aal_action_taken;
		/**
		 * null
		 **/
		Date aal_action_timestamp;
		/**
		 * null
		 **/
		Integer aal_aah_id;
	
		public AeAppnApprovalList(){
		}
	
		public Integer getAal_id(){
			return this.aal_id;
		}		
		public void setAal_id(Integer aal_id){
			this.aal_id = aal_id;
		}
		public Integer getAal_usr_ent_id(){
			return this.aal_usr_ent_id;
		}		
		public void setAal_usr_ent_id(Integer aal_usr_ent_id){
			this.aal_usr_ent_id = aal_usr_ent_id;
		}
		public Integer getAal_app_id(){
			return this.aal_app_id;
		}		
		public void setAal_app_id(Integer aal_app_id){
			this.aal_app_id = aal_app_id;
		}
		public Integer getAal_app_ent_id(){
			return this.aal_app_ent_id;
		}		
		public void setAal_app_ent_id(Integer aal_app_ent_id){
			this.aal_app_ent_id = aal_app_ent_id;
		}
		public String getAal_approval_role(){
			return this.aal_approval_role;
		}		
		public void setAal_approval_role(String aal_approval_role){
			this.aal_approval_role = aal_approval_role;
		}
		public Integer getAal_approval_usg_ent_id(){
			return this.aal_approval_usg_ent_id;
		}		
		public void setAal_approval_usg_ent_id(Integer aal_approval_usg_ent_id){
			this.aal_approval_usg_ent_id = aal_approval_usg_ent_id;
		}
		public String getAal_status(){
			return this.aal_status;
		}		
		public void setAal_status(String aal_status){
			this.aal_status = aal_status;
		}
		public Date getAal_create_timestamp(){
			return this.aal_create_timestamp;
		}		
		public void setAal_create_timestamp(Date aal_create_timestamp){
			this.aal_create_timestamp = aal_create_timestamp;
		}
		public Integer getAal_action_taker_usr_ent_id(){
			return this.aal_action_taker_usr_ent_id;
		}		
		public void setAal_action_taker_usr_ent_id(Integer aal_action_taker_usr_ent_id){
			this.aal_action_taker_usr_ent_id = aal_action_taker_usr_ent_id;
		}
		public String getAal_action_taker_approval_role(){
			return this.aal_action_taker_approval_role;
		}		
		public void setAal_action_taker_approval_role(String aal_action_taker_approval_role){
			this.aal_action_taker_approval_role = aal_action_taker_approval_role;
		}
		public String getAal_action_taken(){
			return this.aal_action_taken;
		}		
		public void setAal_action_taken(String aal_action_taken){
			this.aal_action_taken = aal_action_taken;
		}
		public Date getAal_action_timestamp(){
			return this.aal_action_timestamp;
		}		
		public void setAal_action_timestamp(Date aal_action_timestamp){
			this.aal_action_timestamp = aal_action_timestamp;
		}
		public Integer getAal_aah_id(){
			return this.aal_aah_id;
		}		
		public void setAal_aah_id(Integer aal_aah_id){
			this.aal_aah_id = aal_aah_id;
		}
	
}