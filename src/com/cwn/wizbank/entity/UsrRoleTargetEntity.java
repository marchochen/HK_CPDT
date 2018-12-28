package com.cwn.wizbank.entity;

import java.util.Date;


public class UsrRoleTargetEntity implements java.io.Serializable {
	private static final long serialVersionUID = -7148435726478419399L;
		/**
		 * pk
		 * null
		 **/
		Integer rte_usr_ent_id;
		/**
		 * pk
		 * null
		 **/
		String rte_rol_ext_id;
		/**
		 * pk
		 * null
		 **/
		Integer rte_group_id;
		/**
		 * pk
		 * null
		 **/
		Integer rte_ent_id;
		/**
		 * null
		 **/
		Date rte_create_timestamp;
		/**
		 * null
		 **/
		String rte_create_usr_id;
		/**
		 * null
		 **/
		Date rte_syn_timestamp;
		/**
		 * null
		 **/
		Date rte_eff_start_datetime;
		/**
		 * null
		 **/
		Date rte_eff_end_datetime;
	
		public UsrRoleTargetEntity(){
		}
	
		public Integer getRte_usr_ent_id(){
			return this.rte_usr_ent_id;
		}		
		public void setRte_usr_ent_id(Integer rte_usr_ent_id){
			this.rte_usr_ent_id = rte_usr_ent_id;
		}
		public String getRte_rol_ext_id(){
			return this.rte_rol_ext_id;
		}		
		public void setRte_rol_ext_id(String rte_rol_ext_id){
			this.rte_rol_ext_id = rte_rol_ext_id;
		}
		public Integer getRte_group_id(){
			return this.rte_group_id;
		}		
		public void setRte_group_id(Integer rte_group_id){
			this.rte_group_id = rte_group_id;
		}
		public Integer getRte_ent_id(){
			return this.rte_ent_id;
		}		
		public void setRte_ent_id(Integer rte_ent_id){
			this.rte_ent_id = rte_ent_id;
		}
		public Date getRte_create_timestamp(){
			return this.rte_create_timestamp;
		}		
		public void setRte_create_timestamp(Date rte_create_timestamp){
			this.rte_create_timestamp = rte_create_timestamp;
		}
		public String getRte_create_usr_id(){
			return this.rte_create_usr_id;
		}		
		public void setRte_create_usr_id(String rte_create_usr_id){
			this.rte_create_usr_id = rte_create_usr_id;
		}
		public Date getRte_syn_timestamp(){
			return this.rte_syn_timestamp;
		}		
		public void setRte_syn_timestamp(Date rte_syn_timestamp){
			this.rte_syn_timestamp = rte_syn_timestamp;
		}
		public Date getRte_eff_start_datetime(){
			return this.rte_eff_start_datetime;
		}		
		public void setRte_eff_start_datetime(Date rte_eff_start_datetime){
			this.rte_eff_start_datetime = rte_eff_start_datetime;
		}
		public Date getRte_eff_end_datetime(){
			return this.rte_eff_end_datetime;
		}		
		public void setRte_eff_end_datetime(Date rte_eff_end_datetime){
			this.rte_eff_end_datetime = rte_eff_end_datetime;
		}
	
}