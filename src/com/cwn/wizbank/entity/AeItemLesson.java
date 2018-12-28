package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;


public class AeItemLesson implements java.io.Serializable {
	private static final long serialVersionUID = -5978722638833986537L;
		/**
		 * pk
		 * null
		 **/
		Integer ils_id;
		/**
		 * null
		 **/
		Integer ils_itm_id;
		/**
		 * null
		 **/
		String ils_title;
		/**
		 * null
		 **/
		Integer ils_day;
		/**
		 * null
		 **/
		Date ils_start_time;
		/**
		 * null
		 **/
		Date ils_end_time;
		/**
		 * null
		 **/
		Date ils_create_timestamp;
		/**
		 * null
		 **/
		String ils_create_usr_id;
		/**
		 * null
		 **/
		Date ils_update_timestamp;
		/**
		 * null
		 **/
		String ils_update_usr_id;
		/**
		 * null
		 **/
		String ils_place;
		
		RegUser user;
		
		List<RegUser> userList;
	
		public AeItemLesson(){
		}
	
		public Integer getIls_id(){
			return this.ils_id;
		}		
		public void setIls_id(Integer ils_id){
			this.ils_id = ils_id;
		}
		public Integer getIls_itm_id(){
			return this.ils_itm_id;
		}		
		public void setIls_itm_id(Integer ils_itm_id){
			this.ils_itm_id = ils_itm_id;
		}
		public String getIls_title(){
			return this.ils_title;
		}		
		public void setIls_title(String ils_title){
			this.ils_title = ils_title;
		}
		public Integer getIls_day(){
			return this.ils_day;
		}		
		public void setIls_day(Integer ils_day){
			this.ils_day = ils_day;
		}
		public Date getIls_start_time(){
			return this.ils_start_time;
		}		
		public void setIls_start_time(Date ils_start_time){
			this.ils_start_time = ils_start_time;
		}
		public Date getIls_end_time(){
			return this.ils_end_time;
		}		
		public void setIls_end_time(Date ils_end_time){
			this.ils_end_time = ils_end_time;
		}
		public Date getIls_create_timestamp(){
			return this.ils_create_timestamp;
		}		
		public void setIls_create_timestamp(Date ils_create_timestamp){
			this.ils_create_timestamp = ils_create_timestamp;
		}
		public String getIls_create_usr_id(){
			return this.ils_create_usr_id;
		}		
		public void setIls_create_usr_id(String ils_create_usr_id){
			this.ils_create_usr_id = ils_create_usr_id;
		}
		public Date getIls_update_timestamp(){
			return this.ils_update_timestamp;
		}		
		public void setIls_update_timestamp(Date ils_update_timestamp){
			this.ils_update_timestamp = ils_update_timestamp;
		}
		public String getIls_update_usr_id(){
			return this.ils_update_usr_id;
		}		
		public void setIls_update_usr_id(String ils_update_usr_id){
			this.ils_update_usr_id = ils_update_usr_id;
		}
		public String getIls_place(){
			return this.ils_place;
		}		
		public void setIls_place(String ils_place){
			this.ils_place = ils_place;
		}

		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public List<RegUser> getUserList() {
			return userList;
		}

		public void setUserList(List<RegUser> userList) {
			this.userList = userList;
		}
	
}