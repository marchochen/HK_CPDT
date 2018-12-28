package com.cwn.wizbank.entity;

import java.util.Date;


public class InstructorCos implements java.io.Serializable {
	private static final long serialVersionUID = -3449158545149161525L;
		/**
		 * pk
		 * null
		 **/
		Integer ics_id;
		/**
		 * null
		 **/
		Integer ics_iti_ent_id;
		/**
		 * null
		 **/
		String ics_title;
		/**
		 * null
		 **/
		Double ics_fee;
		/**
		 * null
		 **/
		Double ics_hours;
		/**
		 * null
		 **/
		String ics_target;
		/**
		 * null
		 **/
		String ics_content;
	
		public InstructorCos(){
		}
	
		public Integer getIcs_id(){
			return this.ics_id;
		}		
		public void setIcs_id(Integer ics_id){
			this.ics_id = ics_id;
		}
		public Integer getIcs_iti_ent_id(){
			return this.ics_iti_ent_id;
		}		
		public void setIcs_iti_ent_id(Integer ics_iti_ent_id){
			this.ics_iti_ent_id = ics_iti_ent_id;
		}
		public String getIcs_title(){
			return this.ics_title;
		}		
		public void setIcs_title(String ics_title){
			this.ics_title = ics_title;
		}
		public Double getIcs_fee(){
			return this.ics_fee;
		}		
		public void setIcs_fee(Double ics_fee){
			this.ics_fee = ics_fee;
		}
		public Double getIcs_hours(){
			return this.ics_hours;
		}		
		public void setIcs_hours(Double ics_hours){
			this.ics_hours = ics_hours;
		}
		public String getIcs_target(){
			return this.ics_target;
		}		
		public void setIcs_target(String ics_target){
			this.ics_target = ics_target;
		}
		public String getIcs_content(){
			return this.ics_content;
		}		
		public void setIcs_content(String ics_content){
			this.ics_content = ics_content;
		}
	
}