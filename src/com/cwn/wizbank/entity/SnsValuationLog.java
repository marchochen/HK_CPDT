package com.cwn.wizbank.entity;

import java.util.Date;


public class SnsValuationLog implements java.io.Serializable {
	private static final long serialVersionUID = -2832271198879533559L;
		/**
		 * pk
		 * null
		 **/
		Long s_vtl_log_id;
		/**
		 * null
		 **/
		String s_vtl_type;
		/**
		 * null
		 **/
		Long s_vtl_score;
		/**
		 * null
		 **/
		Date s_vtl_create_datetime;
		/**
		 * null
		 **/
		Long s_vtl_uid;
		/**
		 * null
		 **/
		String s_vtl_module;
		/**
		 * null
		 **/
		Long s_vtl_target_id;
		
		int s_vtl_is_comment;
	
		public SnsValuationLog(){
		}
	
		public Long getS_vtl_log_id(){
			return this.s_vtl_log_id;
		}		
		public void setS_vtl_log_id(Long s_vtl_log_id){
			this.s_vtl_log_id = s_vtl_log_id;
		}
		public String getS_vtl_type(){
			return this.s_vtl_type;
		}		
		public void setS_vtl_type(String s_vtl_type){
			this.s_vtl_type = s_vtl_type;
		}
		public Long getS_vtl_score(){
			return this.s_vtl_score;
		}		
		public void setS_vtl_score(Long s_vtl_score){
			this.s_vtl_score = s_vtl_score;
		}
		public Date getS_vtl_create_datetime(){
			return this.s_vtl_create_datetime;
		}		
		public void setS_vtl_create_datetime(Date s_vtl_create_datetime){
			this.s_vtl_create_datetime = s_vtl_create_datetime;
		}
		public Long getS_vtl_uid(){
			return this.s_vtl_uid;
		}		
		public void setS_vtl_uid(Long s_vtl_uid){
			this.s_vtl_uid = s_vtl_uid;
		}
		public String getS_vtl_module(){
			return this.s_vtl_module;
		}		
		public void setS_vtl_module(String s_vtl_module){
			this.s_vtl_module = s_vtl_module;
		}
		public Long getS_vtl_target_id(){
			return this.s_vtl_target_id;
		}		
		public void setS_vtl_target_id(Long s_vtl_target_id){
			this.s_vtl_target_id = s_vtl_target_id;
		}

		public int getS_vtl_is_comment() {
			return s_vtl_is_comment;
		}

		public void setS_vtl_is_comment(int s_vtl_is_comment) {
			this.s_vtl_is_comment = s_vtl_is_comment;
		}
	
}