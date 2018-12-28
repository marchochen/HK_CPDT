package com.cwn.wizbank.entity;

import java.util.Date;


public class ResourcePermission implements java.io.Serializable {
	private static final long serialVersionUID = -7538132787755295798L;
		/**
		 * null
		 **/
		Integer rpm_res_id;
		/**
		 * null
		 **/
		Integer rpm_ent_id;
		/**
		 * null
		 **/
		String rpm_rol_ext_id;
		/**
		 * null
		 **/
		Boolean rpm_read;
		/**
		 * null
		 **/
		Boolean rpm_write;
		/**
		 * null
		 **/
		Boolean rpm_execute;
	
		public ResourcePermission(){
		}
	
		public Integer getRpm_res_id(){
			return this.rpm_res_id;
		}		
		public void setRpm_res_id(Integer rpm_res_id){
			this.rpm_res_id = rpm_res_id;
		}
		public Integer getRpm_ent_id(){
			return this.rpm_ent_id;
		}		
		public void setRpm_ent_id(Integer rpm_ent_id){
			this.rpm_ent_id = rpm_ent_id;
		}
		public String getRpm_rol_ext_id(){
			return this.rpm_rol_ext_id;
		}		
		public void setRpm_rol_ext_id(String rpm_rol_ext_id){
			this.rpm_rol_ext_id = rpm_rol_ext_id;
		}
		public Boolean getRpm_read(){
			return this.rpm_read;
		}		
		public void setRpm_read(Boolean rpm_read){
			this.rpm_read = rpm_read;
		}
		public Boolean getRpm_write(){
			return this.rpm_write;
		}		
		public void setRpm_write(Boolean rpm_write){
			this.rpm_write = rpm_write;
		}
		public Boolean getRpm_execute(){
			return this.rpm_execute;
		}		
		public void setRpm_execute(Boolean rpm_execute){
			this.rpm_execute = rpm_execute;
		}
	
}