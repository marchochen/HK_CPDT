package com.cwn.wizbank.entity;



public class AeAttendanceStatus implements java.io.Serializable {
	private static final long serialVersionUID = -4697515993195651278L;
		/**
		 * pk
		 * null
		 **/
		Integer ats_id;
		/**
		 * null
		 **/
		String ats_title_xml;
		/**
		 * null
		 **/
		String ats_type;
		/**
		 * null
		 **/
		Boolean ats_attend_ind;
		/**
		 * null
		 **/
		Boolean ats_default_ind;
		/**
		 * null
		 **/
		Integer ats_ent_id_root;
		/**
		 * null
		 **/
		String ats_cov_status;
	
		public AeAttendanceStatus(){
		}
	
		public Integer getAts_id(){
			return this.ats_id;
		}		
		public void setAts_id(Integer ats_id){
			this.ats_id = ats_id;
		}
		public String getAts_title_xml(){
			return this.ats_title_xml;
		}		
		public void setAts_title_xml(String ats_title_xml){
			this.ats_title_xml = ats_title_xml;
		}
		public String getAts_type(){
			return this.ats_type;
		}		
		public void setAts_type(String ats_type){
			this.ats_type = ats_type;
		}
		public Boolean getAts_attend_ind(){
			return this.ats_attend_ind;
		}		
		public void setAts_attend_ind(Boolean ats_attend_ind){
			this.ats_attend_ind = ats_attend_ind;
		}
		public Boolean getAts_default_ind(){
			return this.ats_default_ind;
		}		
		public void setAts_default_ind(Boolean ats_default_ind){
			this.ats_default_ind = ats_default_ind;
		}
		public Integer getAts_ent_id_root(){
			return this.ats_ent_id_root;
		}		
		public void setAts_ent_id_root(Integer ats_ent_id_root){
			this.ats_ent_id_root = ats_ent_id_root;
		}
		public String getAts_cov_status(){
			return this.ats_cov_status;
		}		
		public void setAts_cov_status(String ats_cov_status){
			this.ats_cov_status = ats_cov_status;
		}
	
}