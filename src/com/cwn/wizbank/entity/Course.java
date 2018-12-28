package com.cwn.wizbank.entity;

import java.util.Date;


public class Course implements java.io.Serializable {
	private static final long serialVersionUID = -4831473664536585282L;
		/**
		 * pk
		 * null
		 **/
		Integer cos_res_id;
		/**
		 * null
		 **/
		Integer cos_itm_id;
		/**
		 * null
		 **/
		String cos_pre_test_req_bil;
		/**
		 * null
		 **/
		String cos_post_test_req_bil;
		/**
		 * null
		 **/
		Date cos_eff_start_datetime;
		/**
		 * null
		 **/
		Date cos_eff_end_datetime;
		/**
		 * null
		 **/
		String cos_content_xml;
		/**
		 * null
		 **/
		String cos_structure_xml;
		/**
		 * null
		 **/
		String cos_import_xml;
		/**
		 * null
		 **/
		Date cos_import_datetime;
		/**
		 * null
		 **/
		String cos_aicc_version;
		/**
		 * null
		 **/
		String cos_vendor;
		/**
		 * null
		 **/
		Integer cos_max_normal;
		/**
		 * null
		 **/
		String cos_lic_key;
		/**
		 * null
		 **/
		String cos_structure_json;
	
		public Course(){
		}
	
		public Integer getCos_res_id(){
			return this.cos_res_id;
		}		
		public void setCos_res_id(Integer cos_res_id){
			this.cos_res_id = cos_res_id;
		}
		public Integer getCos_itm_id(){
			return this.cos_itm_id;
		}		
		public void setCos_itm_id(Integer cos_itm_id){
			this.cos_itm_id = cos_itm_id;
		}
		public String getCos_pre_test_req_bil(){
			return this.cos_pre_test_req_bil;
		}		
		public void setCos_pre_test_req_bil(String cos_pre_test_req_bil){
			this.cos_pre_test_req_bil = cos_pre_test_req_bil;
		}
		public String getCos_post_test_req_bil(){
			return this.cos_post_test_req_bil;
		}		
		public void setCos_post_test_req_bil(String cos_post_test_req_bil){
			this.cos_post_test_req_bil = cos_post_test_req_bil;
		}
		public Date getCos_eff_start_datetime(){
			return this.cos_eff_start_datetime;
		}		
		public void setCos_eff_start_datetime(Date cos_eff_start_datetime){
			this.cos_eff_start_datetime = cos_eff_start_datetime;
		}
		public Date getCos_eff_end_datetime(){
			return this.cos_eff_end_datetime;
		}		
		public void setCos_eff_end_datetime(Date cos_eff_end_datetime){
			this.cos_eff_end_datetime = cos_eff_end_datetime;
		}
		public String getCos_content_xml(){
			return this.cos_content_xml;
		}		
		public void setCos_content_xml(String cos_content_xml){
			this.cos_content_xml = cos_content_xml;
		}
		public String getCos_structure_xml(){
			return this.cos_structure_xml;
		}		
		public void setCos_structure_xml(String cos_structure_xml){
			this.cos_structure_xml = cos_structure_xml;
		}
		public String getCos_import_xml(){
			return this.cos_import_xml;
		}		
		public void setCos_import_xml(String cos_import_xml){
			this.cos_import_xml = cos_import_xml;
		}
		public Date getCos_import_datetime(){
			return this.cos_import_datetime;
		}		
		public void setCos_import_datetime(Date cos_import_datetime){
			this.cos_import_datetime = cos_import_datetime;
		}
		public String getCos_aicc_version(){
			return this.cos_aicc_version;
		}		
		public void setCos_aicc_version(String cos_aicc_version){
			this.cos_aicc_version = cos_aicc_version;
		}
		public String getCos_vendor(){
			return this.cos_vendor;
		}		
		public void setCos_vendor(String cos_vendor){
			this.cos_vendor = cos_vendor;
		}
		public Integer getCos_max_normal(){
			return this.cos_max_normal;
		}		
		public void setCos_max_normal(Integer cos_max_normal){
			this.cos_max_normal = cos_max_normal;
		}
		public String getCos_lic_key(){
			return this.cos_lic_key;
		}		
		public void setCos_lic_key(String cos_lic_key){
			this.cos_lic_key = cos_lic_key;
		}
		public String getCos_structure_json(){
			return this.cos_structure_json;
		}		
		public void setCos_structure_json(String cos_structure_json){
			this.cos_structure_json = cos_structure_json;
		}
	
}