package com.cwn.wizbank.entity.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LearnerReportParamVo implements Serializable{

	private static final long serialVersionUID = -1787236288821412359L;

	public static final String  COURSE_STATUS_I = "I"; //进行中
	public static final String  COURSE_STATUS_C = "C"; //已完成
	public static final String  COURSE_STATUS_F = "F"; //不合格
	public static final String  COURSE_STATUS_W = "W"; //已放弃
	
	private Integer exportUser;  //导出学员 0:全部学员 1：指定学员 2：指定用户组
	
	private boolean includeDelUser ; //是否包含被删除的用户
	
	private Integer exportCourse; //导出课程类型 0:全部课程 1：指定课程 2：指定目录
	
	private List<Integer> courseType; // 0:网上课程  1:面试课程  2：网上考试  3:离线考试 4:项目式培训
	
	private List courseTypeCondition;
	
	private Date appnStartDatetime;//报名开始日期
	
	private String appnStartDisplayDatetime ;
	
	private Date appnEndDatetime;//报名结束日期
	
	private String appnEndDisplayDatetime ;
	
	private Date attStartTime;//结训开始日期
	
	private String attStartDispalyTime ;
	
	private Date attEndTime;//结训结束日期
	
	private String attEndDisplayTime ;
	
	private List courseStatus; //学习状态  InProgress = "I"  Completed = "C" FAIL = "F" Withdrawn = "W"
	
	private List appStatus; //报名状态   PENDING = "Pending" ADMITTED = "Admitted"  WAITING = "Waiting"  REJECTED = "Rejected" WITHDRAWN = "Withdrawn"

	private Integer resultDataStatistic; //结果数据统计方式 0:以课程为主统计数据，导出数据以课程编号排序 1:以学员为主统计数据，导出数据以用户名排序
	
	private Integer includeNoDataCourse ; //是否包含没有数据的课程
	
	private Integer includeNoDataUser ; //是否包含没有数据的学员
	
	private Boolean isExportDetail; //是否导出明细
	
	private List userInfo;
	
	private List courseInfo;
	
	private List otherInfo;
	
	private boolean isRoleTcInd;
	
	private String userRole;
	
	private Long myTopTcId;
	
	private Long userEntId;
	
	private List exportUserIds; //选择的用户ID
	
	private List exportGroupIds; //选择的用户组ID
	
	private List exportCourseIds ; //选择的课程
	
	private List exportCatalogIds; //选择的课程目录
	
	private Integer analysisType;//统计类型 0：课程 1：用户
	
	private List exportUserNames; // 选择的用户的名称
	
	private List exportGroupNames; // 选择的用户组的名称
	
	private List exportCourseNames; // 选择的课程的名称
	
	private List exportCatalogNames; // 选择的课程目录的名称
	
	private String language;
	
	public Integer getExportUser() {
		return exportUser;
	}

	public void setExportUser(Integer exportUser) {
		this.exportUser = exportUser;
	}

	public Integer getExportCourse() {
		return exportCourse;
	}

	public void setExportCourse(Integer exportCourse) {
		this.exportCourse = exportCourse;
	}

	public List getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(List courseStatus) {
		this.courseStatus = courseStatus;
	}

	public List getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(List appStatus) {
		this.appStatus = appStatus;
	}

	public Integer getIncludeNoDataCourse() {
		return includeNoDataCourse;
	}

	public void setIncludeNoDataCourse(Integer includeNoDataCourse) {
		this.includeNoDataCourse = includeNoDataCourse;
	}

	public Integer getIncludeNoDataUser() {
		return includeNoDataUser;
	}

	public void setIncludeNoDataUser(Integer includeNoDataUser) {
		this.includeNoDataUser = includeNoDataUser;
	}

	public Boolean getIsExportDetail() {
		return isExportDetail;
	}

	public void setIsExportDetail(Boolean isExportDetail) {
		this.isExportDetail = isExportDetail;
	}

	public boolean isRoleTcInd() {
		return isRoleTcInd;
	}

	public void setRoleTcInd(boolean isRoleTcInd) {
		this.isRoleTcInd = isRoleTcInd;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Long getMyTopTcId() {
		return myTopTcId;
	}

	public void setMyTopTcId(Long myTopTcId) {
		this.myTopTcId = myTopTcId;
	}

	public Long getUserEntId() {
		return userEntId;
	}

	public void setUserEntId(Long userEntId) {
		this.userEntId = userEntId;
	}

	public boolean isIncludeDelUser() {
		return includeDelUser;
	}

	public void setIncludeDelUser(boolean includeDelUser) {
		this.includeDelUser = includeDelUser;
	}

	public List getExportUserIds() {
		return exportUserIds;
	}

	public void setExportUserIds(List exportUserIds) {
		this.exportUserIds = exportUserIds;
	}

	public List getExportGroupIds() {
		return exportGroupIds;
	}

	public void setExportGroupIds(List exportGroupIds) {
		this.exportGroupIds = exportGroupIds;
	}

	public List getExportCourseIds() {
		return exportCourseIds;
	}

	public void setExportCourseIds(List exportCourseIds) {
		this.exportCourseIds = exportCourseIds;
	}

	public List getExportCatalogIds() {
		return exportCatalogIds;
	}

	public void setExportCatalogIds(List exportCatalogIds) {
		this.exportCatalogIds = exportCatalogIds;
	}

	public List getCourseTypeCondition() {
		return courseTypeCondition;
	}

	public void setCourseTypeCondition(List courseTypeCondition) {
		this.courseTypeCondition = courseTypeCondition;
	}

	public List<Integer> getCourseType() {
		return courseType;
	}

	public void setCourseType(List<Integer> courseType) {
		this.courseType = courseType;
	}

	public Date getAppnStartDatetime() {
		return appnStartDatetime;
	}

	public void setAppnStartDatetime(Date appnStartDatetime) {
		this.appnStartDatetime = appnStartDatetime;
	}

	public Date getAppnEndDatetime() {
		return appnEndDatetime;
	}

	public void setAppnEndDatetime(Date appnEndDatetime) {
		this.appnEndDatetime = appnEndDatetime;
	}

	public Date getAttStartTime() {
		return attStartTime;
	}

	public void setAttStartTime(Date attStartTime) {
		this.attStartTime = attStartTime;
	}

	public Date getAttEndTime() {
		return attEndTime;
	}

	public void setAttEndTime(Date attEndTime) {
		this.attEndTime = attEndTime;
	}

	public Integer getAnalysisType() {
		return analysisType;
	}

	public void setAnalysisType(Integer analysisType) {
		this.analysisType = analysisType;
	}

	public List getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(List userInfo) {
		this.userInfo = userInfo;
	}

	public List getCourseInfo() {
		return courseInfo;
	}

	public void setCourseInfo(List courseInfo) {
		this.courseInfo = courseInfo;
	}

	public List getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(List otherInfo) {
		this.otherInfo = otherInfo;
	}
	public Integer getResultDataStatistic() {
		return resultDataStatistic;
	}

	public void setResultDataStatistic(Integer resultDataStatistic) {
		this.resultDataStatistic = resultDataStatistic;
	}

	public String getAppnStartDisplayDatetime() {
		return appnStartDisplayDatetime;
	}

	public void setAppnStartDisplayDatetime(String appnStartDisplayDatetime) {
		this.appnStartDisplayDatetime = appnStartDisplayDatetime;
	}

	public String getAppnEndDisplayDatetime() {
		return appnEndDisplayDatetime;
	}

	public void setAppnEndDisplayDatetime(String appnEndDisplayDatetime) {
		this.appnEndDisplayDatetime = appnEndDisplayDatetime;
	}

	public String getAttStartDispalyTime() {
		return attStartDispalyTime;
	}

	public void setAttStartDispalyTime(String attStartDispalyTime) {
		this.attStartDispalyTime = attStartDispalyTime;
	}

	public String getAttEndDisplayTime() {
		return attEndDisplayTime;
	}

	public void setAttEndDisplayTime(String attEndDisplayTime) {
		this.attEndDisplayTime = attEndDisplayTime;
	}

	public List getExportUserNames() {
		return exportUserNames;
	}

	public void setExportUserNames(List exportUserNames) {
		this.exportUserNames = exportUserNames;
	}

	public List getExportGroupNames() {
		return exportGroupNames;
	}

	public void setExportGroupNames(List exportGroupNames) {
		this.exportGroupNames = exportGroupNames;
	}

	public List getExportCourseNames() {
		return exportCourseNames;
	}

	public void setExportCourseNames(List exportCourseNames) {
		this.exportCourseNames = exportCourseNames;
	}

	public List getExportCatalogNames() {
		return exportCatalogNames;
	}

	public void setExportCatalogNames(List exportCatalogNames) {
		this.exportCatalogNames = exportCatalogNames;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	
}