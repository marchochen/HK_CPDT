package com.cwn.wizbank.entity;

import java.util.Date;


public class CourseMeasurement implements java.io.Serializable {
	private static final long serialVersionUID = -985152988749339135L;
		/**
		 * pk
		 * null
		 **/
		Integer cmt_id;
		/**
		 * null
		 **/
		String cmt_title;
		/**
		 * null
		 **/
		Integer cmt_ccr_id;
		/**
		 * null
		 **/
		Integer cmt_cmr_id;
		/**
		 * null
		 **/
		Double cmt_max_score;
		/**
		 * null
		 **/
		String cmt_status;
		/**
		 * null
		 **/
		Double cmt_contri_rate;
		/**
		 * null
		 **/
		Integer cmt_is_contri_by_score;
		/**
		 * null
		 **/
		Date cmt_create_timestamp;
		/**
		 * null
		 **/
		String cmt_create_usr_id;
		/**
		 * null
		 **/
		Date cmt_update_timestamp;
		/**
		 * null
		 **/
		String cmt_update_usr_id;
		/**
		 * null
		 **/
		Date cmt_delete_timestamp;
		/**
		 * null
		 **/
		Double cmt_pass_score;
		/**
		 * null
		 **/
		String cmt_status_desc_option;
		/**
		 * null
		 **/
		Integer cmt_order;
		/**
		 * null
		 **/
		Integer cmt_cmt_id_parent;
		/**
		 * null
		 **/
		Double cmt_duration;
		/**
		 * null
		 **/
		String cmt_place;
		/**
		 * null
		 **/
		String cmt_cmr_status;
		
		ModuleEvaluation mov;
		
		Resources res;
		
		Integer cmt_order_res = 0;
		
		/**
		 * 学员是否已通过该条件，true：已通过； false：未通过
		 **/
		boolean cmt_lrn_pass_ind;
	

		public CourseMeasurement(){
		}
	
		public Integer getCmt_id(){
			return this.cmt_id;
		}		
		public void setCmt_id(Integer cmt_id){
			this.cmt_id = cmt_id;
		}
		public String getCmt_title(){
			return this.cmt_title;
		}		
		public void setCmt_title(String cmt_title){
			this.cmt_title = cmt_title;
		}
		public Integer getCmt_ccr_id(){
			return this.cmt_ccr_id;
		}		
		public void setCmt_ccr_id(Integer cmt_ccr_id){
			this.cmt_ccr_id = cmt_ccr_id;
		}
		public Integer getCmt_cmr_id(){
			return this.cmt_cmr_id;
		}		
		public void setCmt_cmr_id(Integer cmt_cmr_id){
			this.cmt_cmr_id = cmt_cmr_id;
		}
		public Double getCmt_max_score(){
			return this.cmt_max_score;
		}		
		public void setCmt_max_score(Double cmt_max_score){
			this.cmt_max_score = cmt_max_score;
		}
		public String getCmt_status(){
			return this.cmt_status;
		}		
		public void setCmt_status(String cmt_status){
			this.cmt_status = cmt_status;
		}
		public Double getCmt_contri_rate(){
			return this.cmt_contri_rate;
		}		
		public void setCmt_contri_rate(Double cmt_contri_rate){
			this.cmt_contri_rate = cmt_contri_rate;
		}
		public Integer getCmt_is_contri_by_score(){
			return this.cmt_is_contri_by_score;
		}		
		public void setCmt_is_contri_by_score(Integer cmt_is_contri_by_score){
			this.cmt_is_contri_by_score = cmt_is_contri_by_score;
		}
		public Date getCmt_create_timestamp(){
			return this.cmt_create_timestamp;
		}		
		public void setCmt_create_timestamp(Date cmt_create_timestamp){
			this.cmt_create_timestamp = cmt_create_timestamp;
		}
		public String getCmt_create_usr_id(){
			return this.cmt_create_usr_id;
		}		
		public void setCmt_create_usr_id(String cmt_create_usr_id){
			this.cmt_create_usr_id = cmt_create_usr_id;
		}
		public Date getCmt_update_timestamp(){
			return this.cmt_update_timestamp;
		}		
		public void setCmt_update_timestamp(Date cmt_update_timestamp){
			this.cmt_update_timestamp = cmt_update_timestamp;
		}
		public String getCmt_update_usr_id(){
			return this.cmt_update_usr_id;
		}		
		public void setCmt_update_usr_id(String cmt_update_usr_id){
			this.cmt_update_usr_id = cmt_update_usr_id;
		}
		public Date getCmt_delete_timestamp(){
			return this.cmt_delete_timestamp;
		}		
		public void setCmt_delete_timestamp(Date cmt_delete_timestamp){
			this.cmt_delete_timestamp = cmt_delete_timestamp;
		}
		public Double getCmt_pass_score(){
			return this.cmt_pass_score;
		}		
		public void setCmt_pass_score(Double cmt_pass_score){
			this.cmt_pass_score = cmt_pass_score;
		}
		public String getCmt_status_desc_option(){
			return this.cmt_status_desc_option;
		}		
		public void setCmt_status_desc_option(String cmt_status_desc_option){
			this.cmt_status_desc_option = cmt_status_desc_option;
		}
		public Integer getCmt_order(){
			return this.cmt_order;
		}		
		public void setCmt_order(Integer cmt_order){
			this.cmt_order = cmt_order;
		}
		public Integer getCmt_cmt_id_parent(){
			return this.cmt_cmt_id_parent;
		}		
		public void setCmt_cmt_id_parent(Integer cmt_cmt_id_parent){
			this.cmt_cmt_id_parent = cmt_cmt_id_parent;
		}
		public Double getCmt_duration(){
			return this.cmt_duration;
		}		
		public void setCmt_duration(Double cmt_duration){
			this.cmt_duration = cmt_duration;
		}
		public String getCmt_place(){
			return this.cmt_place;
		}		
		public void setCmt_place(String cmt_place){
			this.cmt_place = cmt_place;
		}
		public String getCmt_cmr_status(){
			return this.cmt_cmr_status;
		}		
		public void setCmt_cmr_status(String cmt_cmr_status){
			this.cmt_cmr_status = cmt_cmr_status;
		}
		public ModuleEvaluation getMov() {
			return mov;
		}

		public void setMov(ModuleEvaluation mov) {
			this.mov = mov;
		}
		
		public boolean isCmt_lrn_pass_ind() {
			return cmt_lrn_pass_ind;
		}

		public void setCmt_lrn_pass_ind(boolean cmtLrnPassInd) {
			cmt_lrn_pass_ind = cmtLrnPassInd;
		}

		public Resources getRes() {
			return res;
		}

		public void setRes(Resources res) {
			this.res = res;
		}
		public Integer getCmt_order_res(){
			return this.cmt_order_res;
		}		
		public void setCmt_order_res(Integer cmt_order_res){
			this.cmt_order_res = cmt_order_res;
		}


	
}