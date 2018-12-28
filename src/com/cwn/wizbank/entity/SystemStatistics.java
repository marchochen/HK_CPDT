package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 系统数据统计实体
 * @author andrew.xiao
 *
 */
public class SystemStatistics {

	/**
	 * 企业培训中心
	 */
	private long ssc_eip_tcr_id;
	
	/**
	 * 更新时间
	 */
	private Date ssc_update_time;
	
	/**
	 * 网上课程数
	 */
	private long ssc_web_base_couse_count;
	
	/**
	 * 面授课程数
	 */
	private long ssc_classroom_course_count;
	
	/**
	 * 项目培训数
	 */
	private long ssc_integrated_course_count;
	
	/**
	 * 网上考试数
	 */
	private long ssc_web_base_exam_count;
	
	/**
	 * 离线考试数
	 */
	private long ssc_classroom_exam_count;
	
	/**
	 * 公开课总数
	 */
	private long ssc_open_course_count;
	
	/**
	 * 专题总数
	 */
	private long ssc_special_topic_count;
	
	/**
	 * 知识中心管理员分享数
	 */
	private long ssc_admin_know_share_count;
	
	/**
	 * 学员分享量
	 */
	private long ssc_learner_know_share_count;
	
	/**
	 * 当前在线人数
	 */
	private long ssc_user_online_count;
	
	/**
	 * 总用户数 
	 */
	private long ssc_user_count;
	
	/**
	 * 用户组数
	 */
	private long ssc_user_group_count;
	
	/**
	 * 移动APP登录数
	 */
	private long ssc_mobile_app_user_count;
	
	/**
	 * 微信公众号数
	 */
	private long ssc_wechat_user_count;

	public long getSsc_eip_tcr_id() {
		return ssc_eip_tcr_id;
	}

	public void setSsc_eip_tcr_id(long ssc_eip_tcr_id) {
		this.ssc_eip_tcr_id = ssc_eip_tcr_id;
	}

	public Date getSsc_update_time() {
		return ssc_update_time;
	}

	public void setSsc_update_time(Date ssc_update_time) {
		this.ssc_update_time = ssc_update_time;
	}
	
	public long getSsc_web_base_couse_count() {
		return ssc_web_base_couse_count;
	}

	public void setSsc_web_base_couse_count(long ssc_web_base_couse_count) {
		this.ssc_web_base_couse_count = ssc_web_base_couse_count;
	}

	public long getSsc_classroom_course_count() {
		return ssc_classroom_course_count;
	}

	public void setSsc_classroom_course_count(long ssc_classroom_course_count) {
		this.ssc_classroom_course_count = ssc_classroom_course_count;
	}

	public long getSsc_integrated_course_count() {
		return ssc_integrated_course_count;
	}

	public void setSsc_integrated_course_count(
			long ssc_integrated_course_count) {
		this.ssc_integrated_course_count = ssc_integrated_course_count;
	}

	public long getSsc_web_base_exam_count() {
		return ssc_web_base_exam_count;
	}

	public void setSsc_web_base_exam_count(
			long ssc_web_base_exam_count) {
		this.ssc_web_base_exam_count = ssc_web_base_exam_count;
	}

	public long getSsc_classroom_exam_count() {
		return ssc_classroom_exam_count;
	}

	public void setSsc_classroom_exam_count(long ssc_classroom_exam_count) {
		this.ssc_classroom_exam_count = ssc_classroom_exam_count;
	}

	public long getSsc_open_course_count() {
		return ssc_open_course_count;
	}

	public void setSsc_open_course_count(long ssc_open_course_count) {
		this.ssc_open_course_count = ssc_open_course_count;
	}

	public long getSsc_special_topic_count() {
		return ssc_special_topic_count;
	}

	public void setSsc_special_topic_count(long ssc_special_topic_count) {
		this.ssc_special_topic_count = ssc_special_topic_count;
	}

	public long getSsc_admin_know_share_count() {
		return ssc_admin_know_share_count;
	}

	public void setSsc_admin_know_share_count(
			long ssc_admin_know_share_count) {
		this.ssc_admin_know_share_count = ssc_admin_know_share_count;
	}

	public long getSsc_learner_know_share_count() {
		return ssc_learner_know_share_count;
	}

	public void setSsc_learner_know_share_count(
			long ssc_learner_know_share_count) {
		this.ssc_learner_know_share_count = ssc_learner_know_share_count;
	}

	public long getSsc_user_online_count() {
		return ssc_user_online_count;
	}

	public void setSsc_user_online_count(long ssc_user_online_count) {
		this.ssc_user_online_count = ssc_user_online_count;
	}

	public long getSsc_user_count() {
		return ssc_user_count;
	}

	public void setSsc_user_count(long ssc_user_count) {
		this.ssc_user_count = ssc_user_count;
	}

	public long getSsc_user_group_count() {
		return ssc_user_group_count;
	}

	public void setSsc_user_group_count(long ssc_user_group_count) {
		this.ssc_user_group_count = ssc_user_group_count;
	}

	public long getSsc_mobile_app_user_count() {
		return ssc_mobile_app_user_count;
	}

	public void setSsc_mobile_app_user_count(long ssc_mobile_app_user_count) {
		this.ssc_mobile_app_user_count = ssc_mobile_app_user_count;
	}

	public long getSsc_wechat_user_count() {
		return ssc_wechat_user_count;
	}

	public void setSsc_wechat_user_count(long ssc_wechat_user_count) {
		this.ssc_wechat_user_count = ssc_wechat_user_count;
	}
	
}
