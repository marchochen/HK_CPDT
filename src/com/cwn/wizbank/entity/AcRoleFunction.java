package com.cwn.wizbank.entity;

import java.util.Date;


public class AcRoleFunction implements java.io.Serializable {
	private static final long serialVersionUID = -7688624324295782577L;
		/**
		 * pk
		 * null
		 **/
		Integer rfn_rol_id;
		/**
		 * pk
		 * null
		 **/
		Integer rfn_ftn_id;
		/**
		 * null
		 **/
		String rfn_create_usr_id;
		/**
		 * null
		 **/
		Date rfn_create_timestamp;
		/**
		 * null
		 **/
		Integer rfn_ftn_parent_id;
		/**
		 * null
		 **/
		String rfn_ftn_favorite;
		/**
		 * null
		 **/
		String rfn_ftn_order;
	
		public AcRoleFunction(){
		}
	
		public Integer getRfn_rol_id(){
			return this.rfn_rol_id;
		}		
		public void setRfn_rol_id(Integer rfn_rol_id){
			this.rfn_rol_id = rfn_rol_id;
		}
		public Integer getRfn_ftn_id(){
			return this.rfn_ftn_id;
		}		
		public void setRfn_ftn_id(Integer rfn_ftn_id){
			this.rfn_ftn_id = rfn_ftn_id;
		}
		public String getRfn_create_usr_id(){
			return this.rfn_create_usr_id;
		}		
		public void setRfn_create_usr_id(String rfn_create_usr_id){
			this.rfn_create_usr_id = rfn_create_usr_id;
		}
		public Date getRfn_create_timestamp(){
			return this.rfn_create_timestamp;
		}		
		public void setRfn_create_timestamp(Date rfn_create_timestamp){
			this.rfn_create_timestamp = rfn_create_timestamp;
		}
		public Integer getRfn_ftn_parent_id(){
			return this.rfn_ftn_parent_id;
		}		
		public void setRfn_ftn_parent_id(Integer rfn_ftn_parent_id){
			this.rfn_ftn_parent_id = rfn_ftn_parent_id;
		}
		public String getRfn_ftn_favorite(){
			return this.rfn_ftn_favorite;
		}		
		public void setRfn_ftn_favorite(String rfn_ftn_favorite){
			this.rfn_ftn_favorite = rfn_ftn_favorite;
		}

		public String getRfn_ftn_order() {
			return rfn_ftn_order;
		}

		public void setRfn_ftn_order(String rfn_ftn_order) {
			this.rfn_ftn_order = rfn_ftn_order;
		}

	
}