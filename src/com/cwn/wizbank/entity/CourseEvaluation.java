package com.cwn.wizbank.entity;

import java.util.Date;

public class CourseEvaluation implements java.io.Serializable {

	private static final long serialVersionUID = 7895406354130000419L;
	
	public static String InProgress = "I";
	public static String Completed = "C";
	public static String FAIL = "F";
	public static String Withdrawn = "W";
	
	
	/**
	 * pk null
	 **/
	Long cov_cos_id;
	/**
	 * pk null
	 **/
	Long cov_ent_id;
	/**
	 * 第一次访问时间
	 **/
	Date cov_commence_datetime;
	/**
	 * 最后一次访问时间
	 **/
	Date cov_last_acc_datetime;
	/**
	 * 学习总时长,以为秒为单位
	 **/
	Double cov_total_time;
	/**
	 * 学习状态
	 **/
	String cov_status;
	/**
	 * null
	 **/
	Boolean cov_status_ovrdn_ind;
	/**
	 * 分数
	 **/
	Double cov_score;
	/**
	 * null
	 **/
	Double cov_max_score;
	/**
	 * null
	 **/
	String cov_comment;
	/**
	 * null
	 **/
	Boolean cov_final_ind;
	/**
	 * null
	 **/
	Date cov_complete_datetime;
	/**
	 * 获得已完成状态的时间
	 **/
	Date cov_update_timestamp;
	/**
	 * pk null
	 **/
	Long cov_tkh_id;
	/**
	 * 学习进度
	 **/
	Double cov_progress;
	

	AeAttendance att;
	
	private AeApplication aeApplication;
	
	public CourseEvaluation() {
	}

	public Long getCov_cos_id() {
		return this.cov_cos_id;
	}
	public void setCov_cos_id(Long cov_cos_id) {
		this.cov_cos_id = cov_cos_id;
	}
	public Long getCov_ent_id() {
		return this.cov_ent_id;
	}
	public void setCov_ent_id(Long cov_ent_id) {
		this.cov_ent_id = cov_ent_id;
	}
	public Date getCov_commence_datetime() {
		return this.cov_commence_datetime;
	}
	public void setCov_commence_datetime(Date cov_commence_datetime) {
		this.cov_commence_datetime = cov_commence_datetime;
	}
	public Date getCov_last_acc_datetime() {
		return this.cov_last_acc_datetime;
	}
	public void setCov_last_acc_datetime(Date cov_last_acc_datetime) {
		this.cov_last_acc_datetime = cov_last_acc_datetime;
	}
	public Double getCov_total_time() {
		return this.cov_total_time;
	}
	public void setCov_total_time(Double cov_total_time) {
		this.cov_total_time = cov_total_time;
	}
	public String getCov_status() {
		return this.cov_status;
	}
	public void setCov_status(String cov_status) {
		this.cov_status = cov_status;
	}
	public Boolean getCov_status_ovrdn_ind() {
		return this.cov_status_ovrdn_ind;
	}
	public void setCov_status_ovrdn_ind(Boolean cov_status_ovrdn_ind) {
		this.cov_status_ovrdn_ind = cov_status_ovrdn_ind;
	}
	public Double getCov_score() {
		return this.cov_score;
	}
	public void setCov_score(Double cov_score) {
		this.cov_score = cov_score;
	}
	public Double getCov_max_score() {
		return this.cov_max_score;
	}
	public void setCov_max_score(Double cov_max_score) {
		this.cov_max_score = cov_max_score;
	}
	public String getCov_comment() {
		return this.cov_comment;
	}
	public void setCov_comment(String cov_comment) {
		this.cov_comment = cov_comment;
	}
	public Boolean getCov_final_ind() {
		return this.cov_final_ind;
	}
	public void setCov_final_ind(Boolean cov_final_ind) {
		this.cov_final_ind = cov_final_ind;
	}
	public Date getCov_complete_datetime() {
		return this.cov_complete_datetime;
	}
	public void setCov_complete_datetime(Date cov_complete_datetime) {
		this.cov_complete_datetime = cov_complete_datetime;
	}
	public Date getCov_update_timestamp() {
		return this.cov_update_timestamp;
	}
	public void setCov_update_timestamp(Date cov_update_timestamp) {
		this.cov_update_timestamp = cov_update_timestamp;
	}
	public Long getCov_tkh_id() {
		return this.cov_tkh_id;
	}
	public void setCov_tkh_id(Long cov_tkh_id) {
		this.cov_tkh_id = cov_tkh_id;
	}
	public Double getCov_progress() {
		return this.cov_progress;
	}
	public void setCov_progress(Double cov_progress) {
		this.cov_progress = cov_progress;
	}

	public AeAttendance getAtt() {
		return att;
	}

	public void setAtt(AeAttendance att) {
		this.att = att;
	}
	public AeApplication getAeApplication() {
		return aeApplication;
	}
	public void setAeApplication(AeApplication aeApplication) {
		this.aeApplication = aeApplication;
	}

}