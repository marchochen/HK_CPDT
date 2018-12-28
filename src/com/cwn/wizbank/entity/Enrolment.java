package com.cwn.wizbank.entity;

import java.util.Date;


/**
 * 
 * @author leon.li
 * 2014-8-7 下午5:53:34
 */
public class Enrolment implements java.io.Serializable {
	private static final long serialVersionUID = -8329384451481146256L;
		/**
		 * pk
		 * 
		 **/
		Long enr_ent_id;
		/**
		 * pk
		 * 
		 **/
		Long enr_res_id;
		/**
		 * 
		 **/
		String enr_status;
		/**
		 * 
		 **/
		Date enr_create_timestamp;
		/**
		 * 
		 **/
		String enr_create_usr_id;
	
		public Enrolment(){
		}

		public Long getEnr_ent_id() {
			return enr_ent_id;
		}

		public void setEnr_ent_id(Long enr_ent_id) {
			this.enr_ent_id = enr_ent_id;
		}

		public Long getEnr_res_id() {
			return enr_res_id;
		}

		public void setEnr_res_id(Long enr_res_id) {
			this.enr_res_id = enr_res_id;
		}

		public String getEnr_status() {
			return enr_status;
		}

		public void setEnr_status(String enr_status) {
			this.enr_status = enr_status;
		}

		public Date getEnr_create_timestamp() {
			return enr_create_timestamp;
		}

		public void setEnr_create_timestamp(Date enr_create_timestamp) {
			this.enr_create_timestamp = enr_create_timestamp;
		}

		public String getEnr_create_usr_id() {
			return enr_create_usr_id;
		}

		public void setEnr_create_usr_id(String enr_create_usr_id) {
			this.enr_create_usr_id = enr_create_usr_id;
		}
	
}