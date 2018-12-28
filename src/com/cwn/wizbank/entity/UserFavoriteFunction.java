package com.cwn.wizbank.entity;

import java.util.Date;


public class UserFavoriteFunction implements java.io.Serializable {
	private static final long serialVersionUID = -3528736139714411859L;
		/**
		 * pk
		 * null
		 **/
		Long uff_usr_ent_id;
		/**
		 * null
		 **/
		String uff_role_ext_id;
		/**
		 * pk
		 * null
		 **/
		Integer uff_fun_id;
		/**
		 * null
		 **/
		Date uff_create_datetime;
		
		AcFunction acFunction;
	
		public UserFavoriteFunction(){
		}
	
		public Long getUff_usr_ent_id(){
			return this.uff_usr_ent_id;
		}		
		public void setUff_usr_ent_id(Long uff_usr_ent_id){
			this.uff_usr_ent_id = uff_usr_ent_id;
		}
		public String getUff_role_ext_id(){
			return this.uff_role_ext_id;
		}		
		public void setUff_role_ext_id(String uff_role_ext_id){
			this.uff_role_ext_id = uff_role_ext_id;
		}
		public Integer getUff_fun_id(){
			return this.uff_fun_id;
		}		
		public void setUff_fun_id(Integer uff_fun_id){
			this.uff_fun_id = uff_fun_id;
		}
		public Date getUff_create_datetime(){
			return this.uff_create_datetime;
		}		
		public void setUff_create_datetime(Date uff_create_datetime){
			this.uff_create_datetime = uff_create_datetime;
		}

		public AcFunction getAcFunction() {
			return acFunction;
		}

		public void setAcFunction(AcFunction acFunction) {
			this.acFunction = acFunction;
		}

}