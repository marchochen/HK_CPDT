package com.cwn.wizbank.entity;

import java.util.Date;


public class MeasurementEvaluation implements java.io.Serializable {
	private static final long serialVersionUID = -5411565468498181434L;
		/**
		 * null
		 **/
		Integer mtv_cos_id;
		/**
		 * null
		 **/
		Integer mtv_ent_id;
		/**
		 * null
		 **/
		Integer mtv_cmt_id;
		/**
		 * null
		 **/
		String mtv_status;
		/**
		 * null
		 **/
		Double mtv_score;
		/**
		 * null
		 **/
		Date mtv_create_timestamp;
		/**
		 * null
		 **/
		String mtv_create_usr_id;
		/**
		 * null
		 **/
		Date mtv_update_timestamp;
		/**
		 * null
		 **/
		String mtv_update_usr_id;
		/**
		 * null
		 **/
		Integer mtv_tkh_id;
	
		public MeasurementEvaluation(){
		}
	
		public Integer getMtv_cos_id(){
			return this.mtv_cos_id;
		}		
		public void setMtv_cos_id(Integer mtv_cos_id){
			this.mtv_cos_id = mtv_cos_id;
		}
		public Integer getMtv_ent_id(){
			return this.mtv_ent_id;
		}		
		public void setMtv_ent_id(Integer mtv_ent_id){
			this.mtv_ent_id = mtv_ent_id;
		}
		public Integer getMtv_cmt_id(){
			return this.mtv_cmt_id;
		}		
		public void setMtv_cmt_id(Integer mtv_cmt_id){
			this.mtv_cmt_id = mtv_cmt_id;
		}
		public String getMtv_status(){
			return this.mtv_status;
		}		
		public void setMtv_status(String mtv_status){
			this.mtv_status = mtv_status;
		}
		public Double getMtv_score(){
			return this.mtv_score;
		}		
		public void setMtv_score(Double mtv_score){
			this.mtv_score = mtv_score;
		}
		public Date getMtv_create_timestamp(){
			return this.mtv_create_timestamp;
		}		
		public void setMtv_create_timestamp(Date mtv_create_timestamp){
			this.mtv_create_timestamp = mtv_create_timestamp;
		}
		public String getMtv_create_usr_id(){
			return this.mtv_create_usr_id;
		}		
		public void setMtv_create_usr_id(String mtv_create_usr_id){
			this.mtv_create_usr_id = mtv_create_usr_id;
		}
		public Date getMtv_update_timestamp(){
			return this.mtv_update_timestamp;
		}		
		public void setMtv_update_timestamp(Date mtv_update_timestamp){
			this.mtv_update_timestamp = mtv_update_timestamp;
		}
		public String getMtv_update_usr_id(){
			return this.mtv_update_usr_id;
		}		
		public void setMtv_update_usr_id(String mtv_update_usr_id){
			this.mtv_update_usr_id = mtv_update_usr_id;
		}
		public Integer getMtv_tkh_id(){
			return this.mtv_tkh_id;
		}		
		public void setMtv_tkh_id(Integer mtv_tkh_id){
			this.mtv_tkh_id = mtv_tkh_id;
		}
	
}