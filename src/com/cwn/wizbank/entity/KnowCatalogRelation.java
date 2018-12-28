package com.cwn.wizbank.entity;

import java.util.Date;


public class KnowCatalogRelation implements java.io.Serializable {
	private static final long serialVersionUID = -4724452646897224289L;
	
	public static final String QUE_PARENT_KCA = "QUE_PARENT_KCA";
	
		/**
		 * pk
		 * null
		 **/
		Long kcr_child_kca_id;
		/**
		 * pk
		 * null
		 **/
		Long kcr_ancestor_kca_id;
		/**
		 * pk
		 * null
		 **/
		String kcr_type;
		/**
		 * null
		 **/
		Integer kcr_order;
		/**
		 * null
		 **/
		Integer kcr_parent_ind;
		/**
		 * null
		 **/
		Date kcr_syn_timestamp;
		/**
		 * null
		 **/
		Integer kcr_remain_on_syn;
		/**
		 * null
		 **/
		String kcr_create_usr_id;
		/**
		 * null
		 **/
		Date kcr_create_timestamp;
	
		public KnowCatalogRelation(){
		}
	
		public Long getKcr_child_kca_id(){
			return this.kcr_child_kca_id;
		}		
		public void setKcr_child_kca_id(Long kcr_child_kca_id){
			this.kcr_child_kca_id = kcr_child_kca_id;
		}
		public Long getKcr_ancestor_kca_id(){
			return this.kcr_ancestor_kca_id;
		}		
		public void setKcr_ancestor_kca_id(Long kcr_ancestor_kca_id){
			this.kcr_ancestor_kca_id = kcr_ancestor_kca_id;
		}
		public String getKcr_type(){
			return this.kcr_type;
		}		
		public void setKcr_type(String kcr_type){
			this.kcr_type = kcr_type;
		}
		public Integer getKcr_order(){
			return this.kcr_order;
		}		
		public void setKcr_order(Integer kcr_order){
			this.kcr_order = kcr_order;
		}
		public Integer getKcr_parent_ind(){
			return this.kcr_parent_ind;
		}		
		public void setKcr_parent_ind(Integer kcr_parent_ind){
			this.kcr_parent_ind = kcr_parent_ind;
		}
		public Date getKcr_syn_timestamp(){
			return this.kcr_syn_timestamp;
		}		
		public void setKcr_syn_timestamp(Date kcr_syn_timestamp){
			this.kcr_syn_timestamp = kcr_syn_timestamp;
		}
		public Integer getKcr_remain_on_syn(){
			return this.kcr_remain_on_syn;
		}		
		public void setKcr_remain_on_syn(Integer kcr_remain_on_syn){
			this.kcr_remain_on_syn = kcr_remain_on_syn;
		}
		public String getKcr_create_usr_id(){
			return this.kcr_create_usr_id;
		}		
		public void setKcr_create_usr_id(String kcr_create_usr_id){
			this.kcr_create_usr_id = kcr_create_usr_id;
		}
		public Date getKcr_create_timestamp(){
			return this.kcr_create_timestamp;
		}		
		public void setKcr_create_timestamp(Date kcr_create_timestamp){
			this.kcr_create_timestamp = kcr_create_timestamp;
		}
	
}