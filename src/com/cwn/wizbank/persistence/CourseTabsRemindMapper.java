package com.cwn.wizbank.persistence;

import org.apache.ibatis.annotations.Param;

import com.cwn.wizbank.entity.CourseCriteria;
import com.cwn.wizbank.entity.CourseTabsRemind;
//查询课程的目标学员、结训条件、分数等是否已经设置
public interface CourseTabsRemindMapper extends BaseMapper<CourseTabsRemind> {
	//目标学员
	public long getRmdTargetLearner(@Param("itm_id")long itm_id);
	//分数设置
	public long getRmdCourseScoreSettings(@Param("itm_id")long itm_id);
	//结训条件设置
	public long getRmdCompletionCriteriaSettings(@Param("itm_id")long itm_id);
	//网上内容
	public long getRmdOnlineContent(@Param("itm_id")long itm_id);
	//日程表
	public long getRmdTimetable(@Param("itm_id")long itm_id);
	//课程包
	public long getRmdCoursePackage(@Param("itm_id")long itm_id);
	//班级管理
	public long getRmdClassManagement(@Param("itm_id")long itm_id);
	//结训条件
	public CourseCriteria getCourseCriteria(@Param("itm_id")long itm_id);
}