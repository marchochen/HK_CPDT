package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 培训中心
 * @author leon.li
 * 2014-8-7 下午5:51:35
 */
public class TcTrainingCenter implements java.io.Serializable {
	private static final long serialVersionUID = -6914228313191827529L;
		/**
		 * pk
		 **/
		Long tcr_id;
		/**
		 * 
		 **/
		String tcr_code;
		/**
		 * 培训中心的标题
		 **/
		String tcr_title;
		/**
		 * 
		 **/
		Long tcr_ste_ent_id;
		/**
		 * 
		 **/
		String tcr_status;
		/**
		 * 
		 **/
		Date tcr_create_timestamp;
		/**
		 * 
		 **/
		String tcr_create_usr_id;
		/**
		 * 
		 **/
		Date tcr_update_timestamp;
		/**
		 * 
		 **/
		String tcr_update_usr_id;
		/**
		 * 
		 **/
		Long tcr_parent_tcr_id;
		/**
		 * 
		 **/
		Long tcr_user_mgt_ind;
	
		
		EntityRelation entityRelation;
		
		
		public TcTrainingCenter(){
		}
		
		public Long getTcr_id() {
			return tcr_id;
		}

		public void setTcr_id(Long tcr_id) {
			this.tcr_id = tcr_id;
		}

		public String getTcr_code() {
			return tcr_code;
		}

		public void setTcr_code(String tcr_code) {
			this.tcr_code = tcr_code;
		}

		public String getTcr_title() {
			return tcr_title;
		}

		public void setTcr_title(String tcr_title) {
			this.tcr_title = tcr_title;
		}

		public Long getTcr_ste_ent_id() {
			return tcr_ste_ent_id;
		}

		public void setTcr_ste_ent_id(Long tcr_ste_ent_id) {
			this.tcr_ste_ent_id = tcr_ste_ent_id;
		}

		public String getTcr_status() {
			return tcr_status;
		}

		public void setTcr_status(String tcr_status) {
			this.tcr_status = tcr_status;
		}

		public Date getTcr_create_timestamp() {
			return tcr_create_timestamp;
		}

		public void setTcr_create_timestamp(Date tcr_create_timestamp) {
			this.tcr_create_timestamp = tcr_create_timestamp;
		}

		public String getTcr_create_usr_id() {
			return tcr_create_usr_id;
		}

		public void setTcr_create_usr_id(String tcr_create_usr_id) {
			this.tcr_create_usr_id = tcr_create_usr_id;
		}

		public Date getTcr_update_timestamp() {
			return tcr_update_timestamp;
		}

		public void setTcr_update_timestamp(Date tcr_update_timestamp) {
			this.tcr_update_timestamp = tcr_update_timestamp;
		}

		public String getTcr_update_usr_id() {
			return tcr_update_usr_id;
		}

		public void setTcr_update_usr_id(String tcr_update_usr_id) {
			this.tcr_update_usr_id = tcr_update_usr_id;
		}

		public Long getTcr_parent_tcr_id() {
			return tcr_parent_tcr_id;
		}

		public void setTcr_parent_tcr_id(Long tcr_parent_tcr_id) {
			this.tcr_parent_tcr_id = tcr_parent_tcr_id;
		}

		public Long getTcr_user_mgt_ind() {
			return tcr_user_mgt_ind;
		}

		public void setTcr_user_mgt_ind(Long tcr_user_mgt_ind) {
			this.tcr_user_mgt_ind = tcr_user_mgt_ind;
		}

		public EntityRelation getEntityRelation() {
			return entityRelation;
		}

		public void setEntityRelation(EntityRelation entityRelation) {
			this.entityRelation = entityRelation;
		}
	
	
	
}