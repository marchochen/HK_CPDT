package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 关注
 * @author leon.li
 * 2014-8-7 下午5:54:45
 */
public class SnsAttention implements java.io.Serializable {
	private static final long serialVersionUID = -1976211227585751449L;

		Long s_att_id;
		/**
		 * 关注人
		 **/
		Long s_att_source_uid;
		/**
		 * 被关注人
		 **/
		Long s_att_target_uid;
		/**
		 * 关注时间
		 **/
		Date s_att_create_datetime;
		
		RegUser regUser;
		
		RegUserExtension regUserExtension;
	
		public SnsAttention(){
		}

		public Long getS_att_id() {
			return s_att_id;
		}

		public void setS_att_id(Long s_att_id) {
			this.s_att_id = s_att_id;
		}

		public Long getS_att_source_uid() {
			return s_att_source_uid;
		}

		public void setS_att_source_uid(Long s_att_source_uid) {
			this.s_att_source_uid = s_att_source_uid;
		}

		public Long getS_att_target_uid() {
			return s_att_target_uid;
		}

		public void setS_att_target_uid(Long s_att_target_uid) {
			this.s_att_target_uid = s_att_target_uid;
		}

		public Date getS_att_create_datetime() {
			return s_att_create_datetime;
		}

		public void setS_att_create_datetime(Date s_att_create_datetime) {
			this.s_att_create_datetime = s_att_create_datetime;
		}

		public RegUser getRegUser() {
			return regUser;
		}

		public void setRegUser(RegUser regUser) {
			this.regUser = regUser;
		}

		public RegUserExtension getRegUserExtension() {
			return regUserExtension;
		}

		public void setRegUserExtension(RegUserExtension regUserExtension) {
			this.regUserExtension = regUserExtension;
		}
	

	
}