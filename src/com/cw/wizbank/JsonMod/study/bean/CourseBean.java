package com.cw.wizbank.JsonMod.study.bean;

import java.sql.Timestamp;

/**
 * 学员报名课程的基类(例如：该类可以是待审批状态的课程的基类，也可以是正在学习课程的基类，等等)
 * @author kimyu
 */
public class CourseBean {
	private long app_id; 		// 对应学员报名记录表aeApplication的app_id
	private long app_itm_id;	// 对应学员报名记录表aeApplication的app_itm_id
	private long itm_id;		// 学员报名课程ID
	private String app_status;	// 报名状态(可选值：Admitted、Pending、Waiting)
	private String itm_title; 	// 课程名称itm_title
	private String itm_type; 	// 课程itm_type(网上课程、离线课程、...)
	private String itm_icon;	// 课程图标
	private String lab_itm_type;// 用于前台显示的课程类型标签
	private Timestamp itm_content_eff_start_datetime; 	// 混合课程的网上内容-开始时间
	private Timestamp itm_content_eff_end_datetime; 	// 混合课程的网上内容-结束时间
	private Timestamp itm_eff_start_datetime; 			// 网上课程的开始时间或离线课程的班级的开始时间
	private Timestamp itm_eff_end_datetime; 			// 网上课程的结束时间或离线课程的班级的结束时间
	
	private boolean itm_blend_ind;
	private boolean itm_create_run_ind;
	private boolean itm_run_ind;
	private boolean itm_ref_ind;
	private boolean itm_exam_ind;
	private boolean itm_integrated_ind;
	
	public long getApp_id() {
		return app_id;
	}

	public void setApp_id(long app_id) {
		this.app_id = app_id;
	}
	
	public long getItm_id() {
		return itm_id;
	}

	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}

	public String getApp_status() {
		return app_status;
	}

	public void setApp_status(String app_status) {
		this.app_status = app_status;
	}

	public String getItm_title() {
		return itm_title;
	}

	public void setItm_title(String itm_title) {
		this.itm_title = itm_title;
	}

	public String getItm_type() {
		return itm_type;
	}

	public void setItm_type(String itm_type) {
		this.itm_type = itm_type;
	}

	public Timestamp getItm_content_eff_start_datetime() {
		return itm_content_eff_start_datetime;
	}

	public void setItm_content_eff_start_datetime(
			Timestamp itm_content_eff_start_datetime) {
		this.itm_content_eff_start_datetime = itm_content_eff_start_datetime;
	}

	public Timestamp getItm_content_eff_end_datetime() {
		return itm_content_eff_end_datetime;
	}

	public void setItm_content_eff_end_datetime(
			Timestamp itm_content_eff_end_datetime) {
		this.itm_content_eff_end_datetime = itm_content_eff_end_datetime;
	}

	public String getLab_itm_type() {
		return lab_itm_type;
	}

	public void setLab_itm_type(String lab_itm_type) {
		this.lab_itm_type = lab_itm_type;
	}

	public Timestamp getItm_eff_start_datetime() {
		return itm_eff_start_datetime;
	}

	public void setItm_eff_start_datetime(Timestamp itm_eff_start_datetime) {
		this.itm_eff_start_datetime = itm_eff_start_datetime;
	}

	public Timestamp getItm_eff_end_datetime() {
		return itm_eff_end_datetime;
	}

	public void setItm_eff_end_datetime(Timestamp itm_eff_end_datetime) {
		this.itm_eff_end_datetime = itm_eff_end_datetime;
	}

	public long getApp_itm_id() {
		return app_itm_id;
	}

	public void setApp_itm_id(long app_itm_id) {
		this.app_itm_id = app_itm_id;
	}

	public String getItm_icon() {
		return itm_icon;
	}

	public void setItm_icon(String itm_icon) {
		this.itm_icon = itm_icon;
	}

	public boolean isItm_blend_ind() {
		return itm_blend_ind;
	}

	public void setItm_blend_ind(boolean itm_blend_ind) {
		this.itm_blend_ind = itm_blend_ind;
	}

	public boolean isItm_create_run_ind() {
		return itm_create_run_ind;
	}

	public void setItm_create_run_ind(boolean itm_create_run_ind) {
		this.itm_create_run_ind = itm_create_run_ind;
	}

	public boolean isItm_run_ind() {
		return itm_run_ind;
	}

	public void setItm_run_ind(boolean itm_run_ind) {
		this.itm_run_ind = itm_run_ind;
	}

	public boolean isItm_ref_ind() {
		return itm_ref_ind;
	}

	public void setItm_ref_ind(boolean itm_ref_ind) {
		this.itm_ref_ind = itm_ref_ind;
	}

	public boolean isItm_exam_ind() {
		return itm_exam_ind;
	}

	public void setItm_exam_ind(boolean itm_exam_ind) {
		this.itm_exam_ind = itm_exam_ind;
	}

	public boolean isItm_integrated_ind() {
		return itm_integrated_ind;
	}

	public void setItm_integrated_ind(boolean itm_integrated_ind) {
		this.itm_integrated_ind = itm_integrated_ind;
	}
	
}
