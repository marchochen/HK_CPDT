package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 积分类型
 * @author leon.li
 * 2014-8-7 下午5:54:01
 */
public class CreditsType implements java.io.Serializable {
	private static final long serialVersionUID = -7763812625681391377L;
		/**
		 * pk
		 **/
		Long cty_id;
		/**
		 * 
		 **/
		String cty_code;
		/**
		 * 标题
		 **/
		String cty_title;
		/**
		 * 
		 **/
		Long cty_deduction_ind;
		/**
		 * 
		 **/
		Long cty_manual_ind;
		/**
		 * 
		 **/
		Long cty_deleted_ind;
		/**
		 * 
		 **/
		Long cty_relation_total_ind;
		/**
		 * 
		 **/
		String cty_relation_type;
		/**
		 * 
		 **/
		Long cty_default_credits_ind;
		/**
		 * 
		 **/
		Float cty_default_credits;
		/**
		 * 
		 **/
		String cty_create_usr_id;
		/**
		 * 
		 **/
		Date cty_create_timestamp;
		/**
		 * 
		 **/
		String cty_update_usr_id;
		/**
		 * 
		 **/
		Date cty_update_timestamp;
		/**
		 * 
		 **/
		Long cty_hit;
		/**
		 * 
		 **/
		String cty_period;
		/**
		 * 培训中心ID
		 **/
		Long cty_tcr_id;
	
		public CreditsType(){
		}

		public Long getCty_id() {
			return cty_id;
		}

		public void setCty_id(Long cty_id) {
			this.cty_id = cty_id;
		}

		public String getCty_code() {
			return cty_code;
		}

		public void setCty_code(String cty_code) {
			this.cty_code = cty_code;
		}

		public String getCty_title() {
			return cty_title;
		}

		public void setCty_title(String cty_title) {
			this.cty_title = cty_title;
		}

		public Long getCty_deduction_ind() {
			return cty_deduction_ind;
		}

		public void setCty_deduction_ind(Long cty_deduction_ind) {
			this.cty_deduction_ind = cty_deduction_ind;
		}

		public Long getCty_manual_ind() {
			return cty_manual_ind;
		}

		public void setCty_manual_ind(Long cty_manual_ind) {
			this.cty_manual_ind = cty_manual_ind;
		}

		public Long getCty_deleted_ind() {
			return cty_deleted_ind;
		}

		public void setCty_deleted_ind(Long cty_deleted_ind) {
			this.cty_deleted_ind = cty_deleted_ind;
		}

		public Long getCty_relation_total_ind() {
			return cty_relation_total_ind;
		}

		public void setCty_relation_total_ind(Long cty_relation_total_ind) {
			this.cty_relation_total_ind = cty_relation_total_ind;
		}

		public String getCty_relation_type() {
			return cty_relation_type;
		}

		public void setCty_relation_type(String cty_relation_type) {
			this.cty_relation_type = cty_relation_type;
		}

		public Long getCty_default_credits_ind() {
			return cty_default_credits_ind;
		}

		public void setCty_default_credits_ind(Long cty_default_credits_ind) {
			this.cty_default_credits_ind = cty_default_credits_ind;
		}

		public Float getCty_default_credits() {
			return cty_default_credits;
		}

		public void setCty_default_credits(Float cty_default_credits) {
			this.cty_default_credits = cty_default_credits;
		}

		public String getCty_create_usr_id() {
			return cty_create_usr_id;
		}

		public void setCty_create_usr_id(String cty_create_usr_id) {
			this.cty_create_usr_id = cty_create_usr_id;
		}

		public Date getCty_create_timestamp() {
			return cty_create_timestamp;
		}

		public void setCty_create_timestamp(Date cty_create_timestamp) {
			this.cty_create_timestamp = cty_create_timestamp;
		}

		public String getCty_update_usr_id() {
			return cty_update_usr_id;
		}

		public void setCty_update_usr_id(String cty_update_usr_id) {
			this.cty_update_usr_id = cty_update_usr_id;
		}

		public Date getCty_update_timestamp() {
			return cty_update_timestamp;
		}

		public void setCty_update_timestamp(Date cty_update_timestamp) {
			this.cty_update_timestamp = cty_update_timestamp;
		}

		public Long getCty_hit() {
			return cty_hit;
		}

		public void setCty_hit(Long cty_hit) {
			this.cty_hit = cty_hit;
		}

		public String getCty_period() {
			return cty_period;
		}

		public void setCty_period(String cty_period) {
			this.cty_period = cty_period;
		}

		public Long getCty_tcr_id() {
			return cty_tcr_id;
		}

		public void setCty_tcr_id(Long cty_tcr_id) {
			this.cty_tcr_id = cty_tcr_id;
		}
	
	
}