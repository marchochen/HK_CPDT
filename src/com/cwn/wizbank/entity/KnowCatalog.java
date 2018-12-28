package com.cwn.wizbank.entity;

import java.util.Date;


public class KnowCatalog implements java.io.Serializable {
	private static final long serialVersionUID = -98492563277466747L;
		/**
		 * pk
		 * null
		 **/
		Long kca_id;
		/**
		 * null
		 **/
		Long kca_tcr_id;
		/**
		 * null
		 **/
		String kca_code;
		/**
		 * null
		 **/
		String kca_title;
		/**
		 * null
		 **/
		String kca_type;
		/**
		 * null
		 **/
		Long kca_public_ind;
		/**
		 * null
		 **/
		Long kca_que_count;
		/**
		 * null
		 **/
		String kca_create_usr_id;
		/**
		 * null
		 **/
		Date kca_create_timestamp;
		/**
		 * null
		 **/
		String kca_update_usr_id;
		/**
		 * null
		 **/
		Date kca_update_timestamp;
		/**
		 * 分类里的问题更新数
		 **/
		Long upd_count;
	
		KnowCatalogRelation knowCatalogRelation;
		
		public KnowCatalog(){
		}
	
		public Long getKca_id(){
			return this.kca_id;
		}		
		public void setKca_id(Long kca_id){
			this.kca_id = kca_id;
		}
		public Long getKca_tcr_id(){
			return this.kca_tcr_id;
		}		
		public void setKca_tcr_id(Long kca_tcr_id){
			this.kca_tcr_id = kca_tcr_id;
		}
		public String getKca_code(){
			return this.kca_code;
		}		
		public void setKca_code(String kca_code){
			this.kca_code = kca_code;
		}
		public String getKca_title(){
			return this.kca_title;
		}		
		public void setKca_title(String kca_title){
			this.kca_title = kca_title;
		}
		public String getKca_type(){
			return this.kca_type;
		}		
		public void setKca_type(String kca_type){
			this.kca_type = kca_type;
		}
		public Long getKca_public_ind(){
			return this.kca_public_ind;
		}		
		public void setKca_public_ind(Long kca_public_ind){
			this.kca_public_ind = kca_public_ind;
		}
		public Long getKca_que_count(){
			return this.kca_que_count;
		}		
		public void setKca_que_count(Long kca_que_count){
			this.kca_que_count = kca_que_count;
		}
		public String getKca_create_usr_id(){
			return this.kca_create_usr_id;
		}		
		public void setKca_create_usr_id(String kca_create_usr_id){
			this.kca_create_usr_id = kca_create_usr_id;
		}
		public Date getKca_create_timestamp(){
			return this.kca_create_timestamp;
		}		
		public void setKca_create_timestamp(Date kca_create_timestamp){
			this.kca_create_timestamp = kca_create_timestamp;
		}
		public String getKca_update_usr_id(){
			return this.kca_update_usr_id;
		}		
		public void setKca_update_usr_id(String kca_update_usr_id){
			this.kca_update_usr_id = kca_update_usr_id;
		}
		public Date getKca_update_timestamp(){
			return this.kca_update_timestamp;
		}		
		public void setKca_update_timestamp(Date kca_update_timestamp){
			this.kca_update_timestamp = kca_update_timestamp;
		}
		public Long getUpd_count() {
			return upd_count;
		}
		public void setUpd_count(Long upd_count) {
			this.upd_count = upd_count;
		}
		public KnowCatalogRelation getKnowCatalogRelation() {
			return knowCatalogRelation;
		}
		public void setKnowCatalogRelation(KnowCatalogRelation knowCatalogRelation) {
			this.knowCatalogRelation = knowCatalogRelation;
		}
	
}