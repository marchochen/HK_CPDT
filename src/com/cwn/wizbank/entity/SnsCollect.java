package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 我的收藏
 * @author leon.li
 * 2014-8-7 下午5:43:26
 */
public class SnsCollect implements java.io.Serializable {
	private static final long serialVersionUID = -3394221321817343347L;

		Long s_clt_id;
		/**
		 * 标题
		 **/
		String s_clt_title;
		/**
		 * 链接
		 **/
		String s_clt_url;
		/**
		 * 赞的时间
		 **/
		Date s_clt_create_datetime;
		/**
		 * 操作人
		 **/
		Long s_clt_uid;
		/**
		 * 赞的模块
		 **/
		String s_clt_module;
		/**
		 * 赞的对象
		 **/
		Long s_clt_target_id;
	
		public SnsCollect(){
		}

		public Long getS_clt_id() {
			return s_clt_id;
		}

		public void setS_clt_id(Long s_clt_id) {
			this.s_clt_id = s_clt_id;
		}

		public String getS_clt_title() {
			return s_clt_title;
		}

		public void setS_clt_title(String s_clt_title) {
			this.s_clt_title = s_clt_title;
		}

		public String getS_clt_url() {
			return s_clt_url;
		}

		public void setS_clt_url(String s_clt_url) {
			this.s_clt_url = s_clt_url;
		}

		public Date getS_clt_create_datetime() {
			return s_clt_create_datetime;
		}

		public void setS_clt_create_datetime(Date s_clt_create_datetime) {
			this.s_clt_create_datetime = s_clt_create_datetime;
		}

		public Long getS_clt_uid() {
			return s_clt_uid;
		}

		public void setS_clt_uid(Long s_clt_uid) {
			this.s_clt_uid = s_clt_uid;
		}

		public String getS_clt_module() {
			return s_clt_module;
		}

		public void setS_clt_module(String s_clt_module) {
			this.s_clt_module = s_clt_module;
		}

		public Long getS_clt_target_id() {
			return s_clt_target_id;
		}

		public void setS_clt_target_id(Long s_clt_target_id) {
			this.s_clt_target_id = s_clt_target_id;
		}
	

	
}