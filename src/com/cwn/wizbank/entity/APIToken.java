package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 对外接口令牌
 * @author leon.li
 * 2014-8-7 下午5:57:03
 */
public class APIToken implements java.io.Serializable {
	private static final long serialVersionUID = -6847175888246839915L;
	
		public final static String API_DEVELOPER_MOBILE = "mobile";
		public final static String API_DEVELOPER_WEIXIN = "weixin";
		public final static String API_DEVELOPER_API = "api";
		
		/**
		 * pk
		 * 
		 **/
		String atk_id;
		/**
		 * 
		 **/
		String atk_usr_id;
		/**
		 * 
		 **/
		Long atk_usr_ent_id;
		/**
		 * 
		 **/
		Date atk_create_timestamp;
		/**
		 * 
		 **/
		Date atk_expiry_timestamp;
		/**
		 * 
		 **/
		String atk_developer_id;
		
		
		String atk_wechat_open_id;
		
		RegUser user;
		
	
		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public String getAtk_wechat_open_id() {
			return atk_wechat_open_id;
		}

		public void setAtk_wechat_open_id(String atk_wechat_open_id) {
			this.atk_wechat_open_id = atk_wechat_open_id;
		}

		public APIToken(){
		}

		public String getAtk_id() {
			return atk_id;
		}

		public void setAtk_id(String atk_id) {
			this.atk_id = atk_id;
		}

		public String getAtk_usr_id() {
			return atk_usr_id;
		}

		public void setAtk_usr_id(String atk_usr_id) {
			this.atk_usr_id = atk_usr_id;
		}

		public Long getAtk_usr_ent_id() {
			return atk_usr_ent_id;
		}

		public void setAtk_usr_ent_id(Long atk_usr_ent_id) {
			this.atk_usr_ent_id = atk_usr_ent_id;
		}

		public Date getAtk_create_timestamp() {
			return atk_create_timestamp;
		}

		public void setAtk_create_timestamp(Date atk_create_timestamp) {
			this.atk_create_timestamp = atk_create_timestamp;
		}

		public Date getAtk_expiry_timestamp() {
			return atk_expiry_timestamp;
		}

		public void setAtk_expiry_timestamp(Date atk_expiry_timestamp) {
			this.atk_expiry_timestamp = atk_expiry_timestamp;
		}

		public String getAtk_developer_id() {
			return atk_developer_id;
		}

		public void setAtk_developer_id(String atk_developer_id) {
			this.atk_developer_id = atk_developer_id;
		}
	

}