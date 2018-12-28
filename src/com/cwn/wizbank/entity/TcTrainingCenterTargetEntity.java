package com.cwn.wizbank.entity;

import java.util.Date;


public class TcTrainingCenterTargetEntity implements java.io.Serializable {
	private static final long serialVersionUID = -4263526886766971982L;
		/**
		 * pk
		 * null
		 **/
		Long rol_id;
		/**
		 * pk
		 * null
		 **/
		Long tce_tcr_id;
		/**
		 * pk
		 * null
		 **/
		Long tce_ent_id;
		/**
		 * null
		 **/
		Date tce_create_timestamp;
		/**
		 * null
		 **/
		String tce_create_usr_id;
	
		public TcTrainingCenterTargetEntity(){
		}
	
		public Long getRol_id(){
			return this.rol_id;
		}		
		public void setRol_id(Long rol_id){
			this.rol_id = rol_id;
		}
		public Long getTce_tcr_id(){
			return this.tce_tcr_id;
		}		
		public void setTce_tcr_id(Long tce_tcr_id){
			this.tce_tcr_id = tce_tcr_id;
		}
		public Long getTce_ent_id(){
			return this.tce_ent_id;
		}		
		public void setTce_ent_id(Long tce_ent_id){
			this.tce_ent_id = tce_ent_id;
		}
		public Date getTce_create_timestamp(){
			return this.tce_create_timestamp;
		}		
		public void setTce_create_timestamp(Date tce_create_timestamp){
			this.tce_create_timestamp = tce_create_timestamp;
		}
		public String getTce_create_usr_id(){
			return this.tce_create_usr_id;
		}		
		public void setTce_create_usr_id(String tce_create_usr_id){
			this.tce_create_usr_id = tce_create_usr_id;
		}
	
}