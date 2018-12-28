package com.cwn.wizbank.entity;

import java.util.Date;


public class KnowVoteDetail implements java.io.Serializable {
	private static final long serialVersionUID = -2167957914762385362L;
		/**
		 * pk
		 * null
		 **/
		Long kvd_que_id;
		/**
		 * pk
		 * null
		 **/
		Long kvd_ans_id;
		/**
		 * pk
		 * null
		 **/
		Long kvd_ent_id;
		/**
		 * null
		 **/
		String kvd_create_usr_id;
		/**
		 * null
		 **/
		Date kvd_create_timestamp;
	
		public KnowVoteDetail(){
		}
	
		public Long getKvd_que_id(){
			return this.kvd_que_id;
		}		
		public void setKvd_que_id(Long kvd_que_id){
			this.kvd_que_id = kvd_que_id;
		}
		public Long getKvd_ans_id(){
			return this.kvd_ans_id;
		}		
		public void setKvd_ans_id(Long kvd_ans_id){
			this.kvd_ans_id = kvd_ans_id;
		}
		public Long getKvd_ent_id(){
			return this.kvd_ent_id;
		}		
		public void setKvd_ent_id(Long kvd_ent_id){
			this.kvd_ent_id = kvd_ent_id;
		}
		public String getKvd_create_usr_id(){
			return this.kvd_create_usr_id;
		}		
		public void setKvd_create_usr_id(String kvd_create_usr_id){
			this.kvd_create_usr_id = kvd_create_usr_id;
		}
		public Date getKvd_create_timestamp(){
			return this.kvd_create_timestamp;
		}		
		public void setKvd_create_timestamp(Date kvd_create_timestamp){
			this.kvd_create_timestamp = kvd_create_timestamp;
		}
	
}