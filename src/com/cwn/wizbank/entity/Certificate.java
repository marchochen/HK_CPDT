package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 证书
 * @author leon.li
 * 2014-8-7 下午5:56:19
 */
public class Certificate implements java.io.Serializable {
	private static final long serialVersionUID = -3553264914449377929L;
		/**
		 * pk
		 * 
		 **/
		Long cfc_id;
		/**
		 * 证书标题
		 **/
		String cfc_title;
		/**
		 * 证书背景
		 **/
		String cfc_img;
		/**
		 * 证书所属培训中心
		 **/
		Long cfc_tcr_id;
		/**
		 * 证书状态
		 **/
		String cfc_status;
		/**
		 * 证书创建时间
		 **/
		Date cfc_create_datetime;
		/**
		 * 证书创建者
		 **/
		String cfc_create_user_id;
		/**
		 * 证书更新时间
		 **/
		Date cfc_update_datetime;
		/**
		 * 证书更新者
		 **/
		String cfc_update_user_id;
		/**
		 * 证书删除时间
		 **/
		Date cfc_delete_datetime;
		/**
		 * 证书删除者
		 **/
		String cfc_delete_user_id;
		/**
		 * 证书编号
		 **/
		String cfc_code;
		/**
		 * 证书有效期 
		 **/
		Date cfc_end_date;
		
		AeItem aeItem;
		
		Long app_tkh_id;
	
		public Certificate(){
		}

		public Long getCfc_id() {
			return cfc_id;
		}

		public void setCfc_id(Long cfc_id) {
			this.cfc_id = cfc_id;
		}

		public String getCfc_title() {
			return cfc_title;
		}

		public void setCfc_title(String cfc_title) {
			this.cfc_title = cfc_title;
		}

		public String getCfc_img() {
			return cfc_img;
		}

		public void setCfc_img(String cfc_img) {
			this.cfc_img = cfc_img;
		}

		public Long getCfc_tcr_id() {
			return cfc_tcr_id;
		}

		public void setCfc_tcr_id(Long cfc_tcr_id) {
			this.cfc_tcr_id = cfc_tcr_id;
		}

		public String getCfc_status() {
			return cfc_status;
		}

		public void setCfc_status(String cfc_status) {
			this.cfc_status = cfc_status;
		}

		public Date getCfc_create_datetime() {
			return cfc_create_datetime;
		}

		public void setCfc_create_datetime(Date cfc_create_datetime) {
			this.cfc_create_datetime = cfc_create_datetime;
		}

		public String getCfc_create_user_id() {
			return cfc_create_user_id;
		}

		public void setCfc_create_user_id(String cfc_create_user_id) {
			this.cfc_create_user_id = cfc_create_user_id;
		}

		public Date getCfc_update_datetime() {
			return cfc_update_datetime;
		}

		public void setCfc_update_datetime(Date cfc_update_datetime) {
			this.cfc_update_datetime = cfc_update_datetime;
		}

		public String getCfc_update_user_id() {
			return cfc_update_user_id;
		}

		public void setCfc_update_user_id(String cfc_update_user_id) {
			this.cfc_update_user_id = cfc_update_user_id;
		}

		public Date getCfc_delete_datetime() {
			return cfc_delete_datetime;
		}

		public void setCfc_delete_datetime(Date cfc_delete_datetime) {
			this.cfc_delete_datetime = cfc_delete_datetime;
		}

		public String getCfc_delete_user_id() {
			return cfc_delete_user_id;
		}

		public void setCfc_delete_user_id(String cfc_delete_user_id) {
			this.cfc_delete_user_id = cfc_delete_user_id;
		}

		public String getCfc_code() {
			return cfc_code;
		}

		public void setCfc_code(String cfc_code) {
			this.cfc_code = cfc_code;
		}

		public Date getCfc_end_date() {
			return cfc_end_date;
		}

		public void setCfc_end_date(Date cfc_end_date) {
			this.cfc_end_date = cfc_end_date;
		}

		public AeItem getAeItem() {
			return aeItem;
		}

		public void setAeItem(AeItem aeItem) {
			this.aeItem = aeItem;
		}

		public Long getApp_tkh_id() {
			return app_tkh_id;
		}

		public void setApp_tkh_id(Long app_tkh_id) {
			this.app_tkh_id = app_tkh_id;
		}
	
}