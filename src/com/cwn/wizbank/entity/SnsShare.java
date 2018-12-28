package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 分享
 * @author leon.li
 * 2014-8-7 下午5:50:39
 */
public class SnsShare implements java.io.Serializable {
	private static final long serialVersionUID = -929956137383932811L;
		/**
		 * pk
		 **/
		Long s_sha_id;
		/**
		 * 分享标题
		 **/
		String s_sha_title;
		/**
		 * 分享链接
		 **/
		String s_sha_url;
		/**
		 * 分享时间
		 **/
		Date s_sha_create_datetime;
		/**
		 * 分享人
		 **/
		Long s_sha_uid;
		/**
		 * 分享模块
		 **/
		String s_sha_module;
		/**
		 * 分享对象
		 **/
		Long s_sha_target_id;
	
		public SnsShare(){
		}

		public Long getS_sha_id() {
			return s_sha_id;
		}

		public void setS_sha_id(Long s_sha_id) {
			this.s_sha_id = s_sha_id;
		}

		public String getS_sha_title() {
			return s_sha_title;
		}

		public void setS_sha_title(String s_sha_title) {
			this.s_sha_title = s_sha_title;
		}

		public String getS_sha_url() {
			return s_sha_url;
		}

		public void setS_sha_url(String s_sha_url) {
			this.s_sha_url = s_sha_url;
		}

		public Date getS_sha_create_datetime() {
			return s_sha_create_datetime;
		}

		public void setS_sha_create_datetime(Date s_sha_create_datetime) {
			this.s_sha_create_datetime = s_sha_create_datetime;
		}

		public Long getS_sha_uid() {
			return s_sha_uid;
		}

		public void setS_sha_uid(Long s_sha_uid) {
			this.s_sha_uid = s_sha_uid;
		}

		public String getS_sha_module() {
			return s_sha_module;
		}

		public void setS_sha_module(String s_sha_module) {
			this.s_sha_module = s_sha_module;
		}

		public Long getS_sha_target_id() {
			return s_sha_target_id;
		}

		public void setS_sha_target_id(Long s_sha_target_id) {
			this.s_sha_target_id = s_sha_target_id;
		}
	
	
	
}