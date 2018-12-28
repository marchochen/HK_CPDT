package com.cwn.wizbank.entity;

public class CourseTabsRemind {
	// 目标学员
	private boolean rmdTargetLearner;
	// 网上内容
	private boolean rmdOnlineContent;
	// 计分规则
	private boolean rmdCourseScoreSettings;
	// 结训条件设置
	private boolean rmdCompletionCriteriaSettings;
	// 日程表
	private boolean rmdTimetable;
	// 课程包
	private boolean rmdCoursePackage;
	// 班级管理
	private boolean rmdClassManagement;

	public boolean isRmdTargetLearner() {
		return rmdTargetLearner;
	}

	public void setRmdTargetLearner(boolean rmdTargetLearner) {
		this.rmdTargetLearner = rmdTargetLearner;
	}

	public boolean isRmdOnlineContent() {
		return rmdOnlineContent;
	}

	public void setRmdOnlineContent(boolean rmdOnlineContent) {
		this.rmdOnlineContent = rmdOnlineContent;
	}

	public boolean isRmdCourseScoreSettings() {
		return rmdCourseScoreSettings;
	}

	public void setRmdCourseScoreSettings(boolean rmdCourseScoreSettings) {
		this.rmdCourseScoreSettings = rmdCourseScoreSettings;
	}

	public boolean isRmdCompletionCriteriaSettings() {
		return rmdCompletionCriteriaSettings;
	}

	public void setRmdCompletionCriteriaSettings(boolean rmdCompletionCriteriaSettings) {
		this.rmdCompletionCriteriaSettings = rmdCompletionCriteriaSettings;
	}

	public boolean isRmdTimetable() {
		return rmdTimetable;
	}

	public void setRmdTimetable(boolean rmdTimetable) {
		this.rmdTimetable = rmdTimetable;
	}

	public boolean isRmdCoursePackage() {
		return rmdCoursePackage;
	}

	public void setRmdCoursePackage(boolean rmdCoursePackage) {
		this.rmdCoursePackage = rmdCoursePackage;
	}

	public boolean isRmdClassManagement() {
		return rmdClassManagement;
	}

	public void setRmdClassManagement(boolean rmdClassManagement) {
		this.rmdClassManagement = rmdClassManagement;
	}
}
