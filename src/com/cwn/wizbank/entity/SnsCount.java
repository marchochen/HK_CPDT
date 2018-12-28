package com.cwn.wizbank.entity;

import java.util.Date;


public class SnsCount implements java.io.Serializable {
	private static final long serialVersionUID = -4216546321635824376L;
		/**
		 * pk
		 * null
		 **/
		Integer s_cnt_id;
		/**
		 * null
		 **/
		Integer s_cnt_target_id;
		/**
		 * null
		 **/
		Integer s_cnt_collect_count;
		/**
		 * null
		 **/
		Integer s_cnt_share_count;
		/**
		 * null
		 **/
		Integer s_cnt_like_count;
		/**
		 * null
		 **/
		Date s_cnt_update_time;
		/**
		 * null
		 **/
		Date s_cnt_create_time;
		/**
		 * null
		 **/
		Integer s_cnt_create_usr_id;
		/**
		 * null
		 **/
		Integer s_cnt_update_usr_id;
		
		int s_cnt_is_comment;
		
		String s_cnt_module;
	
		public String getS_cnt_module() {
			return s_cnt_module;
		}

		public void setS_cnt_module(String s_cnt_module) {
			this.s_cnt_module = s_cnt_module;
		}

		public SnsCount(){
		}
	
		public Integer getS_cnt_id(){
			return this.s_cnt_id;
		}		
		public void setS_cnt_id(Integer s_cnt_id){
			this.s_cnt_id = s_cnt_id;
		}
		public Integer getS_cnt_target_id(){
			return this.s_cnt_target_id;
		}		
		public void setS_cnt_target_id(Integer s_cnt_target_id){
			this.s_cnt_target_id = s_cnt_target_id;
		}
		public Integer getS_cnt_collect_count(){
			return this.s_cnt_collect_count;
		}		
		public void setS_cnt_collect_count(Integer s_cnt_collect_count){
			this.s_cnt_collect_count = s_cnt_collect_count;
		}
		public Integer getS_cnt_share_count(){
			return this.s_cnt_share_count;
		}		
		public void setS_cnt_share_count(Integer s_cnt_share_count){
			this.s_cnt_share_count = s_cnt_share_count;
		}
		public Integer getS_cnt_like_count(){
			return this.s_cnt_like_count;
		}		
		public void setS_cnt_like_count(Integer s_cnt_like_count){
			this.s_cnt_like_count = s_cnt_like_count;
		}
		public Date getS_cnt_update_time(){
			return this.s_cnt_update_time;
		}		
		public void setS_cnt_update_time(Date s_cnt_update_time){
			this.s_cnt_update_time = s_cnt_update_time;
		}
		public Date getS_cnt_create_time(){
			return this.s_cnt_create_time;
		}		
		public void setS_cnt_create_time(Date s_cnt_create_time){
			this.s_cnt_create_time = s_cnt_create_time;
		}
		public Integer getS_cnt_create_usr_id(){
			return this.s_cnt_create_usr_id;
		}		
		public void setS_cnt_create_usr_id(Integer s_cnt_create_usr_id){
			this.s_cnt_create_usr_id = s_cnt_create_usr_id;
		}
		public Integer getS_cnt_update_usr_id(){
			return this.s_cnt_update_usr_id;
		}		
		public void setS_cnt_update_usr_id(Integer s_cnt_update_usr_id){
			this.s_cnt_update_usr_id = s_cnt_update_usr_id;
		}

		public int getS_cnt_is_comment() {
			return s_cnt_is_comment;
		}

		public void setS_cnt_is_comment(int s_cnt_is_comment) {
			this.s_cnt_is_comment = s_cnt_is_comment;
		}
	
}