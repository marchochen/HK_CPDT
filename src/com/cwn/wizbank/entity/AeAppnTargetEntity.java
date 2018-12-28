package com.cwn.wizbank.entity;

import java.util.Date;


public class AeAppnTargetEntity implements java.io.Serializable {
	private static final long serialVersionUID = -6256642672297698722L;
		/**
		 * pk
		 * null
		 **/
		Integer ate_app_id;
		/**
		 * pk
		 * null
		 **/
		Integer ate_usr_ent_id;
		/**
		 * pk
		 * null
		 **/
		String ate_rol_ext_id;
		/**
		 * null
		 **/
		Date ate_create_timestamp;
		/**
		 * null
		 **/
		String ate_create_usr_id;
	
		public AeAppnTargetEntity(){
		}
	
		public Integer getAte_app_id(){
			return this.ate_app_id;
		}		
		public void setAte_app_id(Integer ate_app_id){
			this.ate_app_id = ate_app_id;
		}
		public Integer getAte_usr_ent_id(){
			return this.ate_usr_ent_id;
		}		
		public void setAte_usr_ent_id(Integer ate_usr_ent_id){
			this.ate_usr_ent_id = ate_usr_ent_id;
		}
		public String getAte_rol_ext_id(){
			return this.ate_rol_ext_id;
		}		
		public void setAte_rol_ext_id(String ate_rol_ext_id){
			this.ate_rol_ext_id = ate_rol_ext_id;
		}
		public Date getAte_create_timestamp(){
			return this.ate_create_timestamp;
		}		
		public void setAte_create_timestamp(Date ate_create_timestamp){
			this.ate_create_timestamp = ate_create_timestamp;
		}
		public String getAte_create_usr_id(){
			return this.ate_create_usr_id;
		}		
		public void setAte_create_usr_id(String ate_create_usr_id){
			this.ate_create_usr_id = ate_create_usr_id;
		}
	
}