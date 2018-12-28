package com.cwn.wizbank.entity;

import java.util.Date;


public class AeItemAccess implements java.io.Serializable {
	private static final long serialVersionUID = -6375918722364329957L;
		/**
		 * pk
		 * null
		 **/
		Long iac_itm_id;
		/**
		 * pk
		 * null
		 **/
		Long iac_ent_id;
		/**
		 * null
		 **/
		String iac_access_type;
		/**
		 * pk
		 * null
		 **/
		String iac_access_id;
		/**
		 * null
		 **/
		Date iac_syn_timestamp;
		
		RegUser user;
		
		AeItem item;
	
		public AeItemAccess(){
		}
	
		public Long getIac_itm_id(){
			return this.iac_itm_id;
		}		
		public void setIac_itm_id(Long iac_itm_id){
			this.iac_itm_id = iac_itm_id;
		}
		public Long getIac_ent_id(){
			return this.iac_ent_id;
		}		
		public void setIac_ent_id(Long iac_ent_id){
			this.iac_ent_id = iac_ent_id;
		}
		public String getIac_access_type(){
			return this.iac_access_type;
		}		
		public void setIac_access_type(String iac_access_type){
			this.iac_access_type = iac_access_type;
		}
		public String getIac_access_id(){
			return this.iac_access_id;
		}		
		public void setIac_access_id(String iac_access_id){
			this.iac_access_id = iac_access_id;
		}
		public Date getIac_syn_timestamp(){
			return this.iac_syn_timestamp;
		}		
		public void setIac_syn_timestamp(Date iac_syn_timestamp){
			this.iac_syn_timestamp = iac_syn_timestamp;
		}

		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public AeItem getItem() {
			return item;
		}

		public void setItem(AeItem item) {
			this.item = item;
		}
	
}