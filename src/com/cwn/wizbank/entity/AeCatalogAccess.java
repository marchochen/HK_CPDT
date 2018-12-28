package com.cwn.wizbank.entity;

import java.util.Date;


public class AeCatalogAccess implements java.io.Serializable {
	private static final long serialVersionUID = -3392391113832981519L;
		/**
		 * null
		 **/
		Integer cac_ent_id;
		/**
		 * null
		 **/
		Integer cac_cat_id;
		/**
		 * null
		 **/
		Date cac_create_timestamp;
		/**
		 * null
		 **/
		String cac_create_usr_id;
		/**
		 * pk
		 * null
		 **/
		Integer cac_id;
	
		public AeCatalogAccess(){
		}
	
		public Integer getCac_ent_id(){
			return this.cac_ent_id;
		}		
		public void setCac_ent_id(Integer cac_ent_id){
			this.cac_ent_id = cac_ent_id;
		}
		public Integer getCac_cat_id(){
			return this.cac_cat_id;
		}		
		public void setCac_cat_id(Integer cac_cat_id){
			this.cac_cat_id = cac_cat_id;
		}
		public Date getCac_create_timestamp(){
			return this.cac_create_timestamp;
		}		
		public void setCac_create_timestamp(Date cac_create_timestamp){
			this.cac_create_timestamp = cac_create_timestamp;
		}
		public String getCac_create_usr_id(){
			return this.cac_create_usr_id;
		}		
		public void setCac_create_usr_id(String cac_create_usr_id){
			this.cac_create_usr_id = cac_create_usr_id;
		}
		public Integer getCac_id(){
			return this.cac_id;
		}		
		public void setCac_id(Integer cac_id){
			this.cac_id = cac_id;
		}
	
}