package com.cwn.wizbank.entity;

import java.util.Date;


public class AeTreeNodeRelation implements java.io.Serializable {
	private static final long serialVersionUID = -3982427552993882537L;
		/**
		 * pk
		 * 
		 **/
		Integer tnr_child_tnd_id;
		/**
		 * pk
		 * 
		 **/
		Integer tnr_ancestor_tnd_id;
		/**
		 * 
		 **/
		String tnr_type;
		/**
		 * 
		 **/
		Integer tnr_order;
		/**
		 * 
		 **/
		Integer tnr_parent_ind;
		/**
		 * 
		 **/
		Integer tnr_remain_on_syn;
		/**
		 * 
		 **/
		Date tnr_create_timestamp;
		/**
		 * 
		 **/
		String tnr_create_usr_id;
		
		
		
	
		public AeTreeNodeRelation(){
		}
	
		public Integer getTnr_child_tnd_id(){
			return this.tnr_child_tnd_id;
		}		
		public void setTnr_child_tnd_id(Integer tnr_child_tnd_id){
			this.tnr_child_tnd_id = tnr_child_tnd_id;
		}
		public Integer getTnr_ancestor_tnd_id(){
			return this.tnr_ancestor_tnd_id;
		}		
		public void setTnr_ancestor_tnd_id(Integer tnr_ancestor_tnd_id){
			this.tnr_ancestor_tnd_id = tnr_ancestor_tnd_id;
		}
		public String getTnr_type(){
			return this.tnr_type;
		}		
		public void setTnr_type(String tnr_type){
			this.tnr_type = tnr_type;
		}
		public Integer getTnr_order(){
			return this.tnr_order;
		}		
		public void setTnr_order(Integer tnr_order){
			this.tnr_order = tnr_order;
		}
		public Integer getTnr_parent_ind(){
			return this.tnr_parent_ind;
		}		
		public void setTnr_parent_ind(Integer tnr_parent_ind){
			this.tnr_parent_ind = tnr_parent_ind;
		}
		public Integer getTnr_remain_on_syn(){
			return this.tnr_remain_on_syn;
		}		
		public void setTnr_remain_on_syn(Integer tnr_remain_on_syn){
			this.tnr_remain_on_syn = tnr_remain_on_syn;
		}
		public Date getTnr_create_timestamp(){
			return this.tnr_create_timestamp;
		}		
		public void setTnr_create_timestamp(Date tnr_create_timestamp){
			this.tnr_create_timestamp = tnr_create_timestamp;
		}
		public String getTnr_create_usr_id(){
			return this.tnr_create_usr_id;
		}		
		public void setTnr_create_usr_id(String tnr_create_usr_id){
			this.tnr_create_usr_id = tnr_create_usr_id;
		}
	
}