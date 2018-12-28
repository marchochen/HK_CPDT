package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;


public class Knowanswer implements java.io.Serializable {
	private static final long serialVersionUID = -94781911686551737L;
		/**
		 * pk
		 * null
		 **/
		Long ans_id;
		/**
		 * null
		 **/
		Long ans_que_id;
		/**
		 * null
		 **/
		String ans_content;
		/**
		 * null
		 **/
		String ans_refer_content;
		/**
		 * null
		 **/
		Integer ans_right_ind;
		/**
		 * null
		 **/
		Integer ans_vote_total;
		/**
		 * null
		 **/
		Integer ans_vote_for;
		/**
		 * null
		 **/
		Integer ans_vote_down;
		/**
		 * null
		 **/
		Integer ans_temp_vote_total;
		/**
		 * null
		 **/
		Integer ans_temp_vote_for;
		/**
		 * null
		 **/
		Integer ans_temp_vote_for_down_diff;
		/**
		 * null
		 **/
		String ans_status;
		/**
		 * null
		 **/
		Long ans_create_ent_id;
		/**
		 * null
		 **/
		Date ans_create_timestamp;
		/**
		 * null
		 **/
		Long ans_update_ent_id;
		/**
		 * null
		 **/
		Date ans_update_timestamp;
		/**
		 * null
		 **/
		String ans_content_search;
		
		RegUser user;
		
		SnsCount snsCount;
		
		Long is_user_like;
		
		/**
		 * 文件列表
		 */
		List<ModuleTempFile> fileList;
	
		public Knowanswer(){
		}
	
		public Long getAns_id(){
			return this.ans_id;
		}		
		public void setAns_id(Long ans_id){
			this.ans_id = ans_id;
		}
		public Long getAns_que_id(){
			return this.ans_que_id;
		}		
		public void setAns_que_id(Long ans_que_id){
			this.ans_que_id = ans_que_id;
		}
		public String getAns_content(){
			return this.ans_content;
		}		
		public void setAns_content(String ans_content){
			this.ans_content = ans_content;
		}
		public String getAns_refer_content(){
			return this.ans_refer_content;
		}		
		public void setAns_refer_content(String ans_refer_content){
			this.ans_refer_content = ans_refer_content;
		}
		public Integer getAns_right_ind(){
			return this.ans_right_ind;
		}		
		public void setAns_right_ind(Integer ans_right_ind){
			this.ans_right_ind = ans_right_ind;
		}
		public Integer getAns_vote_total(){
			return this.ans_vote_total;
		}		
		public void setAns_vote_total(Integer ans_vote_total){
			this.ans_vote_total = ans_vote_total;
		}
		public Integer getAns_vote_for(){
			return this.ans_vote_for;
		}		
		public void setAns_vote_for(Integer ans_vote_for){
			this.ans_vote_for = ans_vote_for;
		}
		public Integer getAns_vote_down(){
			return this.ans_vote_down;
		}		
		public void setAns_vote_down(Integer ans_vote_down){
			this.ans_vote_down = ans_vote_down;
		}
		public Integer getAns_temp_vote_total(){
			return this.ans_temp_vote_total;
		}		
		public void setAns_temp_vote_total(Integer ans_temp_vote_total){
			this.ans_temp_vote_total = ans_temp_vote_total;
		}
		public Integer getAns_temp_vote_for(){
			return this.ans_temp_vote_for;
		}		
		public void setAns_temp_vote_for(Integer ans_temp_vote_for){
			this.ans_temp_vote_for = ans_temp_vote_for;
		}
		public Integer getAns_temp_vote_for_down_diff(){
			return this.ans_temp_vote_for_down_diff;
		}		
		public void setAns_temp_vote_for_down_diff(Integer ans_temp_vote_for_down_diff){
			this.ans_temp_vote_for_down_diff = ans_temp_vote_for_down_diff;
		}
		public String getAns_status(){
			return this.ans_status;
		}		
		public void setAns_status(String ans_status){
			this.ans_status = ans_status;
		}
		public Long getAns_create_ent_id(){
			return this.ans_create_ent_id;
		}		
		public void setAns_create_ent_id(Long ans_create_ent_id){
			this.ans_create_ent_id = ans_create_ent_id;
		}
		public Date getAns_create_timestamp(){
			return this.ans_create_timestamp;
		}		
		public void setAns_create_timestamp(Date ans_create_timestamp){
			this.ans_create_timestamp = ans_create_timestamp;
		}
		public Long getAns_update_ent_id(){
			return this.ans_update_ent_id;
		}		
		public void setAns_update_ent_id(Long ans_update_ent_id){
			this.ans_update_ent_id = ans_update_ent_id;
		}
		public Date getAns_update_timestamp(){
			return this.ans_update_timestamp;
		}		
		public void setAns_update_timestamp(Date ans_update_timestamp){
			this.ans_update_timestamp = ans_update_timestamp;
		}
		public String getAns_content_search(){
			return this.ans_content_search;
		}		
		public void setAns_content_search(String ans_content_search){
			this.ans_content_search = ans_content_search;
		}
		public RegUser getRegUser() {
			return user;
		}
		public void setRegUser(RegUser user) {
			this.user = user;
		}

		public SnsCount getSnsCount() {
			return snsCount;
		}

		public void setSnsCount(SnsCount snsCount) {
			this.snsCount = snsCount;
		}

		public Long getIs_user_like() {
			return is_user_like;
		}

		public void setIs_user_like(Long is_user_like) {
			this.is_user_like = is_user_like;
		}

		public List<ModuleTempFile> getFileList() {
			return fileList;
		}

		public void setFileList(List<ModuleTempFile> fileList) {
			this.fileList = fileList;
		}
		
		
	
}