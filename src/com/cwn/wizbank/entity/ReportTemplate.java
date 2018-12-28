package com.cwn.wizbank.entity;

import java.util.Date;


public class ReportTemplate implements java.io.Serializable {
	private static final long serialVersionUID = -1211683577336324161L;
		/**
		 * pk
		 * null
		 **/
		Integer rte_id;
		/**
		 * null
		 **/
		String rte_title_xml;
		/**
		 * null
		 **/
		String rte_type;
		/**
		 * null
		 **/
		String rte_get_xsl;
		/**
		 * null
		 **/
		String rte_exe_xsl;
		/**
		 * null
		 **/
		String rte_dl_xsl;
		/**
		 * null
		 **/
		String rte_meta_data_url;
		/**
		 * null
		 **/
		Integer rte_seq_no;
		/**
		 * null
		 **/
		Integer rte_owner_ent_id;
		/**
		 * null
		 **/
		String rte_create_usr_id;
		/**
		 * null
		 **/
		Date rte_create_timestamp;
		/**
		 * null
		 **/
		String rte_upd_usr_id;
		/**
		 * null
		 **/
		Date rte_upd_timestamp;
	
		public ReportTemplate(){
		}
	
		public Integer getRte_id(){
			return this.rte_id;
		}		
		public void setRte_id(Integer rte_id){
			this.rte_id = rte_id;
		}
		public String getRte_title_xml(){
			return this.rte_title_xml;
		}		
		public void setRte_title_xml(String rte_title_xml){
			this.rte_title_xml = rte_title_xml;
		}
		public String getRte_type(){
			return this.rte_type;
		}		
		public void setRte_type(String rte_type){
			this.rte_type = rte_type;
		}
		public String getRte_get_xsl(){
			return this.rte_get_xsl;
		}		
		public void setRte_get_xsl(String rte_get_xsl){
			this.rte_get_xsl = rte_get_xsl;
		}
		public String getRte_exe_xsl(){
			return this.rte_exe_xsl;
		}		
		public void setRte_exe_xsl(String rte_exe_xsl){
			this.rte_exe_xsl = rte_exe_xsl;
		}
		public String getRte_dl_xsl(){
			return this.rte_dl_xsl;
		}		
		public void setRte_dl_xsl(String rte_dl_xsl){
			this.rte_dl_xsl = rte_dl_xsl;
		}
		public String getRte_meta_data_url(){
			return this.rte_meta_data_url;
		}		
		public void setRte_meta_data_url(String rte_meta_data_url){
			this.rte_meta_data_url = rte_meta_data_url;
		}
		public Integer getRte_seq_no(){
			return this.rte_seq_no;
		}		
		public void setRte_seq_no(Integer rte_seq_no){
			this.rte_seq_no = rte_seq_no;
		}
		public Integer getRte_owner_ent_id(){
			return this.rte_owner_ent_id;
		}		
		public void setRte_owner_ent_id(Integer rte_owner_ent_id){
			this.rte_owner_ent_id = rte_owner_ent_id;
		}
		public String getRte_create_usr_id(){
			return this.rte_create_usr_id;
		}		
		public void setRte_create_usr_id(String rte_create_usr_id){
			this.rte_create_usr_id = rte_create_usr_id;
		}
		public Date getRte_create_timestamp(){
			return this.rte_create_timestamp;
		}		
		public void setRte_create_timestamp(Date rte_create_timestamp){
			this.rte_create_timestamp = rte_create_timestamp;
		}
		public String getRte_upd_usr_id(){
			return this.rte_upd_usr_id;
		}		
		public void setRte_upd_usr_id(String rte_upd_usr_id){
			this.rte_upd_usr_id = rte_upd_usr_id;
		}
		public Date getRte_upd_timestamp(){
			return this.rte_upd_timestamp;
		}		
		public void setRte_upd_timestamp(Date rte_upd_timestamp){
			this.rte_upd_timestamp = rte_upd_timestamp;
		}
	
}