package com.cwn.wizbank.entity;

import java.util.Date;


public class ModuleTempFile implements java.io.Serializable {
	private static final long serialVersionUID = -8294174598351819845L;
		/**
		 * pk
		 * null
		 **/
		Long mtf_id;
		/**
		 * null
		 **/
		Long mtf_target_id;
		/**
		 * null
		 **/
		String mtf_module;
		/**
		 * null
		 **/
		Long mtf_usr_id;
		/**
		 * null
		 **/
		String mtf_file_type;
		/**
		 * 
		 */
		String mtf_type;
		/**
		 * null
		 **/
		String mtf_file_name;
		/**
		 * null
		 **/
		String mtf_file_rename;
		/**
		 * null
		 **/
		Long mtf_file_size;
		/**
		 * null
		 **/
		Date mtf_create_time;
		/**
		 * null
		 **/
		String mtf_url;
	
		public ModuleTempFile(){
		}
	
		public Long getMtf_id(){
			return this.mtf_id;
		}		
		public void setMtf_id(Long mtf_id){
			this.mtf_id = mtf_id;
		}
		public Long getMtf_target_id(){
			return this.mtf_target_id;
		}		
		public void setMtf_target_id(Long mtf_target_id){
			this.mtf_target_id = mtf_target_id;
		}
		public String getMtf_module(){
			return this.mtf_module;
		}		
		public void setMtf_module(String mtf_module){
			this.mtf_module = mtf_module;
		}
		public Long getMtf_usr_id(){
			return this.mtf_usr_id;
		}		
		public void setMtf_usr_id(Long mtf_usr_id){
			this.mtf_usr_id = mtf_usr_id;
		}
		public String getMtf_file_type(){
			return this.mtf_file_type;
		}		
		public void setMtf_file_type(String mtf_file_type){
			this.mtf_file_type = mtf_file_type;
		}
		public String getMtf_file_name(){
			return this.mtf_file_name;
		}		
		public void setMtf_file_name(String mtf_file_name){
			this.mtf_file_name = mtf_file_name;
		}
		public String getMtf_file_rename(){
			return this.mtf_file_rename;
		}		
		public void setMtf_file_rename(String mtf_file_rename){
			this.mtf_file_rename = mtf_file_rename;
		}
		public Long getMtf_file_size(){
			return this.mtf_file_size;
		}		
		public void setMtf_file_size(Long mtf_file_size){
			this.mtf_file_size = mtf_file_size;
		}
		public Date getMtf_create_time(){
			return this.mtf_create_time;
		}		
		public void setMtf_create_time(Date mtf_create_time){
			this.mtf_create_time = mtf_create_time;
		}
		public String getMtf_url(){
			return this.mtf_url;
		}		
		public void setMtf_url(String mtf_url){
			this.mtf_url = mtf_url;
		}
		public String getMtf_type() {
			return mtf_type;
		}
		public void setMtf_type(String mtf_type) {
			this.mtf_type = mtf_type;
		}
	
}