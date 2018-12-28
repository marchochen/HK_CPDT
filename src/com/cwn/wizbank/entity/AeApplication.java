package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 报名
 * 2014-8-7 下午5:55:37
 */
public class AeApplication implements java.io.Serializable {
	private static final long serialVersionUID = -5768443513145979374L;
	
	public final static String APP_STATUS_WITHDRAWN = "Withdrawn";	//已取消
	public final static String APP_STATUS_PENDING = "Pending";	//等待审批
	public final static String APP_STATUS_REJECTED = "Rejected"; //审批未通过
	public final static String APP_STATUS_ADMITTED = "Admitted"; //已报名成功
	public final static String APP_STATUS_WAITING= "Waiting"; //已报名成功

		/**
		 * pk
		 * 
		 **/
		Long app_id;
		/**
		 * 用户的ID
		 **/
		Long app_ent_id;
		/**
		 * 课程的ID
		 **/
		Long app_itm_id;
		/**
		 * 报名状态
		 * Withdrawn : 已取消
		 * Pending	 : 等待审批
		 * Rejected" : 审批未通过
		 * Admitted : 已报名成功
		 * Waiting : 已报名成功
		 **/
		String app_status;
		/**
		 * 
		 **/
		String app_process_status;
		/**
		 * 
		 **/
		String app_process_xml;
		/**
		 * 
		 **/
		String app_xml;
		/**
		 * 
		 **/
		Date app_create_timestamp;
		/**
		 * 
		 **/
		String app_create_usr_id;
		/**
		 * 
		 **/
		Date app_upd_timestamp;
		/**
		 * 
		 **/
		String app_upd_usr_id;
		/**
		 * 
		 **/
		String app_ext1;
		/**
		 * 
		 **/
		Long app_ext2;
		/**
		 * 
		 **/
		String app_ext3;
		/**
		 * 
		 **/
		Long app_notify_status;
		/**
		 * 
		 **/
		Date app_notify_datetime;
		/**
		 * 
		 **/
		Date app_syn_date;
		/**
		 * 
		 **/
		String app_ext4;
		/**
		 * 
		 **/
		String app_usr_prof_xml;
		/**
		 * 
		 **/
		String app_priority;
		/**
		 * 
		 **/
		Long app_tkh_id;
		/**
		 * 
		 **/
		String app_note;
		
		/**
		 * 报名推荐类型，
		 * SUP：上司推推荐，TADM:管理员推荐
		 **/
		String app_nominate_type;

		/**
		 * 审批时间
		 **/
		Date aal_action_timestamp;
		/**
		 * 审批操作
		 **/
		String aal_action_taken;
		/**
		 * 审批者名称
		 **/
		String aal_user_name;
		
		/**
		 * 报名总人数
		 **/
		Long app_total;
		
		AeItem itm;
	
		RegUser user;
						
		CourseEvaluation cov;
		
		AeAttendance att;
		
		ItemTargetLrnDetail itd;
		
		public AeApplication(){
		}

		public Long getApp_id() {
			return app_id;
		}

		public void setApp_id(Long app_id) {
			this.app_id = app_id;
		}

		public Long getApp_ent_id() {
			return app_ent_id;
		}

		public void setApp_ent_id(Long app_ent_id) {
			this.app_ent_id = app_ent_id;
		}

		public Long getApp_itm_id() {
			return app_itm_id;
		}

		public void setApp_itm_id(Long app_itm_id) {
			this.app_itm_id = app_itm_id;
		}

		public String getApp_status() {
			return app_status;
		}

		public void setApp_status(String app_status) {
			this.app_status = app_status;
		}

		public String getApp_process_status() {
			return app_process_status;
		}

		public void setApp_process_status(String app_process_status) {
			this.app_process_status = app_process_status;
		}

		public String getApp_process_xml() {
			return app_process_xml;
		}

		public void setApp_process_xml(String app_process_xml) {
			this.app_process_xml = app_process_xml;
		}

		public String getApp_xml() {
			return app_xml;
		}

		public void setApp_xml(String app_xml) {
			this.app_xml = app_xml;
		}

		public Date getApp_create_timestamp() {
			return app_create_timestamp;
		}

		public void setApp_create_timestamp(Date app_create_timestamp) {
			this.app_create_timestamp = app_create_timestamp;
		}

		public String getApp_create_usr_id() {
			return app_create_usr_id;
		}

		public void setApp_create_usr_id(String app_create_usr_id) {
			this.app_create_usr_id = app_create_usr_id;
		}

		public Date getApp_upd_timestamp() {
			return app_upd_timestamp;
		}

		public void setApp_upd_timestamp(Date app_upd_timestamp) {
			this.app_upd_timestamp = app_upd_timestamp;
		}

		public String getApp_upd_usr_id() {
			return app_upd_usr_id;
		}

		public void setApp_upd_usr_id(String app_upd_usr_id) {
			this.app_upd_usr_id = app_upd_usr_id;
		}

		public String getApp_ext1() {
			return app_ext1;
		}

		public void setApp_ext1(String app_ext1) {
			this.app_ext1 = app_ext1;
		}

		public Long getApp_ext2() {
			return app_ext2;
		}

		public void setApp_ext2(Long app_ext2) {
			this.app_ext2 = app_ext2;
		}

		public String getApp_ext3() {
			return app_ext3;
		}

		public void setApp_ext3(String app_ext3) {
			this.app_ext3 = app_ext3;
		}

		public Long getApp_notify_status() {
			return app_notify_status;
		}

		public void setApp_notify_status(Long app_notify_status) {
			this.app_notify_status = app_notify_status;
		}

		public Date getApp_notify_datetime() {
			return app_notify_datetime;
		}

		public void setApp_notify_datetime(Date app_notify_datetime) {
			this.app_notify_datetime = app_notify_datetime;
		}

		public Date getApp_syn_date() {
			return app_syn_date;
		}

		public void setApp_syn_date(Date app_syn_date) {
			this.app_syn_date = app_syn_date;
		}

		public String getApp_ext4() {
			return app_ext4;
		}

		public void setApp_ext4(String app_ext4) {
			this.app_ext4 = app_ext4;
		}

		public String getApp_usr_prof_xml() {
			return app_usr_prof_xml;
		}

		public void setApp_usr_prof_xml(String app_usr_prof_xml) {
			this.app_usr_prof_xml = app_usr_prof_xml;
		}

		public String getApp_priority() {
			return app_priority;
		}

		public void setApp_priority(String app_priority) {
			this.app_priority = app_priority;
		}

		public Long getApp_tkh_id() {
			return app_tkh_id;
		}

		public void setApp_tkh_id(Long app_tkh_id) {
			this.app_tkh_id = app_tkh_id;
		}

		public String getApp_note() {
			return app_note;
		}

		public void setApp_note(String app_note) {
			this.app_note = app_note;
		}

		public AeItem getItem() {
			return itm;
		}

		public void setItem(AeItem itm) {
			this.itm = itm;
		}

		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public CourseEvaluation getCov() {
			return cov;
		}

		public void setCov(CourseEvaluation cov) {
			this.cov = cov;
		}

		public ItemTargetLrnDetail getItd() {
			return itd;
		}

		public void setItd(ItemTargetLrnDetail itd) {
			this.itd = itd;
		}

		public Long getApp_total() {
			return app_total;
		}

		public void setApp_total(Long app_total) {
			this.app_total = app_total;
		}
		public String getApp_nominate_type() {
			return app_nominate_type;
		}

		public void setApp_nominate_type(String appNominateType) {
			app_nominate_type = appNominateType;
		}
		public AeAttendance getAtt() {
			return att;
		}
		public void setAtt(AeAttendance att) {
			this.att = att;
		}

		public Date getAal_action_timestamp() {
			return aal_action_timestamp;
		}

		public void setAal_action_timestamp(Date aal_action_timestamp) {
			this.aal_action_timestamp = aal_action_timestamp;
		}

		public String getAal_action_taken() {
			return aal_action_taken;
		}

		public void setAal_action_taken(String aal_action_taken) {
			this.aal_action_taken = aal_action_taken;
		}

		public String getAal_user_name() {
			return aal_user_name;
		}

		public void setAal_user_name(String aal_user_name) {
			this.aal_user_name = aal_user_name;
		}

		
	
}