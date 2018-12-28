package com.cwn.wizbank.entity;

import java.util.Date;


public class Progress implements java.io.Serializable {
	private static final long serialVersionUID = -948548362648149444L;
		/**
		 * pk
		 * null
		 **/
		String pgr_usr_id;
		/**
		 * pk
		 * null
		 **/
		Integer pgr_res_id;
		/**
		 * pk
		 * null
		 **/
		Integer pgr_attempt_nbr;
		/**
		 * null
		 **/
		Double pgr_score;
		/**
		 * null
		 **/
		String pgr_grade;
		/**
		 * null
		 **/
		Double pgr_max_score;
		/**
		 * null
		 **/
		String pgr_rank_bil;
		/**
		 * null
		 **/
		Date pgr_schedule_datetime;
		/**
		 * null
		 **/
		Date pgr_start_datetime;
		/**
		 * null
		 **/
		Date pgr_complete_datetime;
		/**
		 * null
		 **/
		Date pgr_last_acc_datetime;
		/**
		 * null
		 **/
		String pgr_status;
		/**
		 * pk
		 * null
		 **/
		Integer pgr_tkh_id;
		/**
		 * null
		 **/
		String pgr_completion_status;
		/**
		 * 是否正确 
		 **/
		int pgr_correct_ind;
		/**
		 * 题数
		 **/
		Long pgr_total;
		
		/**
		 * 未评分
		 */
		int pgr_not_score_cnt;
		
		Module mod;
	
		public Progress(){
		}
	
		public String getPgr_usr_id(){
			return this.pgr_usr_id;
		}		
		public void setPgr_usr_id(String pgr_usr_id){
			this.pgr_usr_id = pgr_usr_id;
		}
		public Integer getPgr_res_id(){
			return this.pgr_res_id;
		}		
		public void setPgr_res_id(Integer pgr_res_id){
			this.pgr_res_id = pgr_res_id;
		}
		public Integer getPgr_attempt_nbr(){
			return this.pgr_attempt_nbr;
		}		
		public void setPgr_attempt_nbr(Integer pgr_attempt_nbr){
			this.pgr_attempt_nbr = pgr_attempt_nbr;
		}
		public Double getPgr_score(){
			return this.pgr_score;
		}		
		public void setPgr_score(Double pgr_score){
			this.pgr_score = pgr_score;
		}
		public String getPgr_grade(){
			return this.pgr_grade;
		}		
		public void setPgr_grade(String pgr_grade){
			this.pgr_grade = pgr_grade;
		}
		public Double getPgr_max_score(){
			return this.pgr_max_score;
		}		
		public void setPgr_max_score(Double pgr_max_score){
			this.pgr_max_score = pgr_max_score;
		}
		public String getPgr_rank_bil(){
			return this.pgr_rank_bil;
		}		
		public void setPgr_rank_bil(String pgr_rank_bil){
			this.pgr_rank_bil = pgr_rank_bil;
		}
		public Date getPgr_schedule_datetime(){
			return this.pgr_schedule_datetime;
		}		
		public void setPgr_schedule_datetime(Date pgr_schedule_datetime){
			this.pgr_schedule_datetime = pgr_schedule_datetime;
		}
		public Date getPgr_start_datetime(){
			return this.pgr_start_datetime;
		}		
		public void setPgr_start_datetime(Date pgr_start_datetime){
			this.pgr_start_datetime = pgr_start_datetime;
		}
		public Date getPgr_complete_datetime(){
			return this.pgr_complete_datetime;
		}		
		public void setPgr_complete_datetime(Date pgr_complete_datetime){
			this.pgr_complete_datetime = pgr_complete_datetime;
		}
		public Date getPgr_last_acc_datetime(){
			return this.pgr_last_acc_datetime;
		}		
		public void setPgr_last_acc_datetime(Date pgr_last_acc_datetime){
			this.pgr_last_acc_datetime = pgr_last_acc_datetime;
		}
		public String getPgr_status(){
			return this.pgr_status;
		}		
		public void setPgr_status(String pgr_status){
			this.pgr_status = pgr_status;
		}
		public Integer getPgr_tkh_id(){
			return this.pgr_tkh_id;
		}		
		public void setPgr_tkh_id(Integer pgr_tkh_id){
			this.pgr_tkh_id = pgr_tkh_id;
		}
		public String getPgr_completion_status(){
			return this.pgr_completion_status;
		}		
		public void setPgr_completion_status(String pgr_completion_status){
			this.pgr_completion_status = pgr_completion_status;
		}
		public int getPgr_correct_ind() {
			return pgr_correct_ind;
		}
		public void setPgr_correct_ind(int pgr_correct_ind) {
			this.pgr_correct_ind = pgr_correct_ind;
		}
		public Long getPgr_total() {
			return pgr_total;
		}
		public void setPgr_total(Long pgr_total) {
			this.pgr_total = pgr_total;
		}
		public Module getMod() {
			return mod;
		}
		public void setMod(Module mod) {
			this.mod = mod;
		}

		public int getPgr_not_score_cnt() {
			return pgr_not_score_cnt;
		}

		public void setPgr_not_score_cnt(int pgr_not_score_cnt) {
			this.pgr_not_score_cnt = pgr_not_score_cnt;
		}
		
}