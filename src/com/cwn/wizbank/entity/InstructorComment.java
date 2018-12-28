package com.cwn.wizbank.entity;

import java.util.Date;


public class InstructorComment implements java.io.Serializable {
	private static final long serialVersionUID = -3777922248188677635L;
		/**
		 * pk
		 * 
		 **/
		Long itc_id;
		/**
		 * 课程ID
		 **/
		Long itc_itm_id;
		/**
		 * 学员ID
		 **/
		Long itc_ent_id;
		/**
		 * 教师ID
		 **/
		Long itc_iti_ent_id;
		/**
		 * 授课风格
		 **/
		Double itc_style_score;
		/**
		 * 教学质量
		 **/
		Double itc_quality_score;
		/**
		 * 内容结构
		 **/
		Double itc_structure_score;
		/**
		 *  交流互动
		 **/
		Double itc_interaction_score;
		/**
		 * 
		 **/
		Double itc_score;
		/**
		 * 评论
		 **/
		String itc_comment;

		Date itc_create_datetime;
		String itc_create_user_id;
		Date itc_update_datetime;
		String itc_update_user_id;
		
		RegUser user;
		
		RegUser fromUser;
	
		public RegUser getFromUser() {
			return fromUser;
		}

		public void setFromUser(RegUser fromUser) {
			this.fromUser = fromUser;
		}

		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public InstructorComment(){
		}
	
		public Long getItc_id(){
			return this.itc_id;
		}		
		public void setItc_id(Long itc_id){
			this.itc_id = itc_id;
		}
		public Long getItc_itm_id(){
			return this.itc_itm_id;
		}		
		public void setItc_itm_id(Long itc_itm_id){
			this.itc_itm_id = itc_itm_id;
		}
		public Long getItc_ent_id(){
			return this.itc_ent_id;
		}		
		public void setItc_ent_id(Long itc_ent_id){
			this.itc_ent_id = itc_ent_id;
		}
		public Long getItc_iti_ent_id(){
			return this.itc_iti_ent_id;
		}		
		public void setItc_iti_ent_id(Long itc_iti_ent_id){
			this.itc_iti_ent_id = itc_iti_ent_id;
		}
		public Double getItc_style_score(){
			return this.itc_style_score;
		}		
		public void setItc_style_score(Double itc_style_score){
			this.itc_style_score = itc_style_score;
		}
		public Double getItc_quality_score(){
			return this.itc_quality_score;
		}		
		public void setItc_quality_score(Double itc_quality_score){
			this.itc_quality_score = itc_quality_score;
		}
		public Double getItc_structure_score(){
			return this.itc_structure_score;
		}		
		public void setItc_structure_score(Double itc_structure_score){
			this.itc_structure_score = itc_structure_score;
		}
		public Double getItc_interaction_score(){
			return this.itc_interaction_score;
		}		
		public void setItc_interaction_score(Double itc_interaction_score){
			this.itc_interaction_score = itc_interaction_score;
		}
		public Double getItc_score(){
			return this.itc_score;
		}		
		public void setItc_score(Double itc_score){
			this.itc_score = itc_score;
		}
		public String getItc_comment(){
			return this.itc_comment;
		}		
		public void setItc_comment(String itc_comment){
			this.itc_comment = itc_comment;
		}
		public Date getItc_create_datetime(){
			return this.itc_create_datetime;
		}		
		public void setItc_create_datetime(Date itc_create_datetime){
			this.itc_create_datetime = itc_create_datetime;
		}
		public String getItc_create_user_id(){
			return this.itc_create_user_id;
		}		
		public void setItc_create_user_id(String itc_create_user_id){
			this.itc_create_user_id = itc_create_user_id;
		}
		public Date getItc_update_datetime(){
			return this.itc_update_datetime;
		}		
		public void setItc_update_datetime(Date itc_update_datetime){
			this.itc_update_datetime = itc_update_datetime;
		}
		public String getItc_update_user_id(){
			return this.itc_update_user_id;
		}		
		public void setItc_update_user_id(String itc_update_user_id){
			this.itc_update_user_id = itc_update_user_id;
		}
	
}