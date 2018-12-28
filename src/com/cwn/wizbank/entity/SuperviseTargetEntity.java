package com.cwn.wizbank.entity;

import java.util.Date;


public class SuperviseTargetEntity implements java.io.Serializable {
	private static final long serialVersionUID = -3535553277816258714L;
		/**
		 * pk
		 * null
		 **/
		Integer spt_source_usr_ent_id;
		/**
		 * pk
		 * null
		 **/
		String spt_type;
		/**
		 * pk
		 * null
		 **/
		Integer spt_target_ent_id;
		/**
		 * null
		 **/
		Date spt_create_timestamp;
		/**
		 * null
		 **/
		String spt_create_usr_id;
		/**
		 * null
		 **/
		Date spt_syn_timestamp;
		/**
		 * null
		 **/
		Date spt_eff_start_datetime;
		/**
		 * null
		 **/
		Date spt_eff_end_datetime;
	
		public SuperviseTargetEntity(){
		}
	
		public Integer getSpt_source_usr_ent_id(){
			return this.spt_source_usr_ent_id;
		}		
		public void setSpt_source_usr_ent_id(Integer spt_source_usr_ent_id){
			this.spt_source_usr_ent_id = spt_source_usr_ent_id;
		}
		public String getSpt_type(){
			return this.spt_type;
		}		
		public void setSpt_type(String spt_type){
			this.spt_type = spt_type;
		}
		public Integer getSpt_target_ent_id(){
			return this.spt_target_ent_id;
		}		
		public void setSpt_target_ent_id(Integer spt_target_ent_id){
			this.spt_target_ent_id = spt_target_ent_id;
		}
		public Date getSpt_create_timestamp(){
			return this.spt_create_timestamp;
		}		
		public void setSpt_create_timestamp(Date spt_create_timestamp){
			this.spt_create_timestamp = spt_create_timestamp;
		}
		public String getSpt_create_usr_id(){
			return this.spt_create_usr_id;
		}		
		public void setSpt_create_usr_id(String spt_create_usr_id){
			this.spt_create_usr_id = spt_create_usr_id;
		}
		public Date getSpt_syn_timestamp(){
			return this.spt_syn_timestamp;
		}		
		public void setSpt_syn_timestamp(Date spt_syn_timestamp){
			this.spt_syn_timestamp = spt_syn_timestamp;
		}
		public Date getSpt_eff_start_datetime(){
			return this.spt_eff_start_datetime;
		}		
		public void setSpt_eff_start_datetime(Date spt_eff_start_datetime){
			this.spt_eff_start_datetime = spt_eff_start_datetime;
		}
		public Date getSpt_eff_end_datetime(){
			return this.spt_eff_end_datetime;
		}		
		public void setSpt_eff_end_datetime(Date spt_eff_end_datetime){
			this.spt_eff_end_datetime = spt_eff_end_datetime;
		}
	
}