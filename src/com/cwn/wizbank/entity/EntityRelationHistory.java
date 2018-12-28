package com.cwn.wizbank.entity;

import java.util.Date;


public class EntityRelationHistory implements java.io.Serializable {
	private static final long serialVersionUID = -8447363468675551672L;
		/**
		 * pk
		 * null
		 **/
		Integer erh_id;
		/**
		 * null
		 **/
		Integer erh_child_ent_id;
		/**
		 * null
		 **/
		Integer erh_ancestor_ent_id;
		/**
		 * null
		 **/
		Integer erh_order;
		/**
		 * null
		 **/
		String erh_type;
		/**
		 * null
		 **/
		Integer erh_parent_ind;
		/**
		 * null
		 **/
		Date erh_start_timestamp;
		/**
		 * null
		 **/
		Date erh_end_timestamp;
		/**
		 * null
		 **/
		Date erh_create_timestamp;
		/**
		 * null
		 **/
		String erh_create_usr_id;
	
		public EntityRelationHistory(){
		}
	
		public Integer getErh_id(){
			return this.erh_id;
		}		
		public void setErh_id(Integer erh_id){
			this.erh_id = erh_id;
		}
		public Integer getErh_child_ent_id(){
			return this.erh_child_ent_id;
		}		
		public void setErh_child_ent_id(Integer erh_child_ent_id){
			this.erh_child_ent_id = erh_child_ent_id;
		}
		public Integer getErh_ancestor_ent_id(){
			return this.erh_ancestor_ent_id;
		}		
		public void setErh_ancestor_ent_id(Integer erh_ancestor_ent_id){
			this.erh_ancestor_ent_id = erh_ancestor_ent_id;
		}
		public Integer getErh_order(){
			return this.erh_order;
		}		
		public void setErh_order(Integer erh_order){
			this.erh_order = erh_order;
		}
		public String getErh_type(){
			return this.erh_type;
		}		
		public void setErh_type(String erh_type){
			this.erh_type = erh_type;
		}
		public Integer getErh_parent_ind(){
			return this.erh_parent_ind;
		}		
		public void setErh_parent_ind(Integer erh_parent_ind){
			this.erh_parent_ind = erh_parent_ind;
		}
		public Date getErh_start_timestamp(){
			return this.erh_start_timestamp;
		}		
		public void setErh_start_timestamp(Date erh_start_timestamp){
			this.erh_start_timestamp = erh_start_timestamp;
		}
		public Date getErh_end_timestamp(){
			return this.erh_end_timestamp;
		}		
		public void setErh_end_timestamp(Date erh_end_timestamp){
			this.erh_end_timestamp = erh_end_timestamp;
		}
		public Date getErh_create_timestamp(){
			return this.erh_create_timestamp;
		}		
		public void setErh_create_timestamp(Date erh_create_timestamp){
			this.erh_create_timestamp = erh_create_timestamp;
		}
		public String getErh_create_usr_id(){
			return this.erh_create_usr_id;
		}		
		public void setErh_create_usr_id(String erh_create_usr_id){
			this.erh_create_usr_id = erh_create_usr_id;
		}
	
}