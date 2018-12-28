package com.cwn.wizbank.entity;

import java.util.Date;


public class AeItemRelation implements java.io.Serializable {
	private static final long serialVersionUID = -6163172388353246529L;
		/**
		 * pk
		 * 
		 **/
		Long ire_parent_itm_id;
		/**
		 * pk
		 * 
		 **/
		Long ire_child_itm_id;
		/**
		 * 
		 **/
		Date ire_create_timestamp;
		/**
		 * 
		 **/
		String ire_create_usr_id;
	
		public AeItemRelation(){
		}

		public Long getIre_parent_itm_id() {
			return ire_parent_itm_id;
		}

		public void setIre_parent_itm_id(Long ire_parent_itm_id) {
			this.ire_parent_itm_id = ire_parent_itm_id;
		}

		public Long getIre_child_itm_id() {
			return ire_child_itm_id;
		}

		public void setIre_child_itm_id(Long ire_child_itm_id) {
			this.ire_child_itm_id = ire_child_itm_id;
		}

		public Date getIre_create_timestamp() {
			return ire_create_timestamp;
		}

		public void setIre_create_timestamp(Date ire_create_timestamp) {
			this.ire_create_timestamp = ire_create_timestamp;
		}

		public String getIre_create_usr_id() {
			return ire_create_usr_id;
		}

		public void setIre_create_usr_id(String ire_create_usr_id) {
			this.ire_create_usr_id = ire_create_usr_id;
		}
	

	
}