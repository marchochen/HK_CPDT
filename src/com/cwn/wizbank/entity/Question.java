package com.cwn.wizbank.entity;



public class Question implements java.io.Serializable {
	private static final long serialVersionUID = -2574135954114822347L;
		/**
		 * pk
		 * null
		 **/
		Long que_res_id;
		/**
		 * null
		 **/
		String que_xml;
		/**
		 * null
		 **/
		Long que_score;
		/**
		 * null
		 **/
		String que_type;
		/**
		 * null
		 **/
		Long que_int_count;
		/**
		 * null
		 **/
		String que_prog_lang;
		/**
		 * null
		 **/
		Boolean que_media_ind;
		/**
		 * null
		 **/
		Long que_submit_file_ind;
	
		public Question(){
		}
	
		public Long getQue_res_id(){
			return this.que_res_id;
		}		
		public void setQue_res_id(Long que_res_id){
			this.que_res_id = que_res_id;
		}
		public String getQue_xml(){
			return this.que_xml;
		}		
		public void setQue_xml(String que_xml){
			this.que_xml = que_xml;
		}
		public Long getQue_score(){
			return this.que_score;
		}		
		public void setQue_score(Long que_score){
			this.que_score = que_score;
		}
		public String getQue_type(){
			return this.que_type;
		}		
		public void setQue_type(String que_type){
			this.que_type = que_type;
		}
		public Long getQue_int_count(){
			return this.que_int_count;
		}		
		public void setQue_int_count(Long que_int_count){
			this.que_int_count = que_int_count;
		}
		public String getQue_prog_lang(){
			return this.que_prog_lang;
		}		
		public void setQue_prog_lang(String que_prog_lang){
			this.que_prog_lang = que_prog_lang;
		}
		public Boolean getQue_media_ind(){
			return this.que_media_ind;
		}		
		public void setQue_media_ind(Boolean que_media_ind){
			this.que_media_ind = que_media_ind;
		}
		public Long getQue_submit_file_ind(){
			return this.que_submit_file_ind;
		}		
		public void setQue_submit_file_ind(Long que_submit_file_ind){
			this.que_submit_file_ind = que_submit_file_ind;
		}
	
}