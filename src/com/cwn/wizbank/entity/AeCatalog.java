package com.cwn.wizbank.entity;

import java.util.Date;


public class AeCatalog implements java.io.Serializable {
	private static final long serialVersionUID = -8541333753247128547L;
	
    public static final String CAT_STATUS_ON = "ON";
    public static final String CAT_STATUS_OFF = "OFF";
		/**
		 * pk
		 **/
		Integer cat_id;
		/**
		 * 名称
		 **/
		String cat_title;
		/**
		 * 
		 **/
		Boolean cat_public_ind;
		/**
		 * 状态
		 **/
		String cat_status;
		/**
		 * 集团ID acsite.ste_ent_id
		 **/
		Integer cat_owner_ent_id;

		/**
		 * 目录编号
		 **/
		String cat_code;
		/**
		 * 培训中心ID
		 **/
		Integer cat_tcr_id;
		/**
		 *  是否手机显示
		 **/
		Integer cat_mobile_ind;
	
		Date cat_create_timestamp;

		String cat_create_usr_id;

		Date cat_upd_timestamp;

		String cat_upd_usr_id;
		
		public AeCatalog(){
		}
	
		public Integer getCat_id(){
			return this.cat_id;
		}		
		public void setCat_id(Integer cat_id){
			this.cat_id = cat_id;
		}
		public String getCat_title(){
			return this.cat_title;
		}		
		public void setCat_title(String cat_title){
			this.cat_title = cat_title;
		}
		public Boolean getCat_public_ind(){
			return this.cat_public_ind;
		}		
		public void setCat_public_ind(Boolean cat_public_ind){
			this.cat_public_ind = cat_public_ind;
		}
		public String getCat_status(){
			return this.cat_status;
		}		
		public void setCat_status(String cat_status){
			this.cat_status = cat_status;
		}
		public Integer getCat_owner_ent_id(){
			return this.cat_owner_ent_id;
		}		
		public void setCat_owner_ent_id(Integer cat_owner_ent_id){
			this.cat_owner_ent_id = cat_owner_ent_id;
		}
		public Date getCat_create_timestamp(){
			return this.cat_create_timestamp;
		}		
		public void setCat_create_timestamp(Date cat_create_timestamp){
			this.cat_create_timestamp = cat_create_timestamp;
		}
		public String getCat_create_usr_id(){
			return this.cat_create_usr_id;
		}		
		public void setCat_create_usr_id(String cat_create_usr_id){
			this.cat_create_usr_id = cat_create_usr_id;
		}
		public Date getCat_upd_timestamp(){
			return this.cat_upd_timestamp;
		}		
		public void setCat_upd_timestamp(Date cat_upd_timestamp){
			this.cat_upd_timestamp = cat_upd_timestamp;
		}
		public String getCat_upd_usr_id(){
			return this.cat_upd_usr_id;
		}		
		public void setCat_upd_usr_id(String cat_upd_usr_id){
			this.cat_upd_usr_id = cat_upd_usr_id;
		}
		public String getCat_code(){
			return this.cat_code;
		}		
		public void setCat_code(String cat_code){
			this.cat_code = cat_code;
		}
		public Integer getCat_tcr_id(){
			return this.cat_tcr_id;
		}		
		public void setCat_tcr_id(Integer cat_tcr_id){
			this.cat_tcr_id = cat_tcr_id;
		}
		public Integer getCat_mobile_ind(){
			return this.cat_mobile_ind;
		}		
		public void setCat_mobile_ind(Integer cat_mobile_ind){
			this.cat_mobile_ind = cat_mobile_ind;
		}
	
}