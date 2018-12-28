package com.cwn.wizbank.entity.vo;

public class LearnerCourseStatisticalReportVo {
	
	private Long itmId;
	
	private String itmCode;
	
	private String itmTitle;

	private String tcName;
	
	private Integer enrollCount = 0;
	
	private Integer failCount = 0;//不合格
	
	private Integer withdrawnCount = 0;//放弃
	
	private Integer completedCount = 0;//已完成
	
	private Integer penddingCount = 0;//等待审批
	
	private Integer waitingCount = 0;//等待
	
	private Integer rejectedCount = 0;//拒绝
	
	private Integer inProgressCount= 0;//进行中状态
	
	private Double failPercentage = 0d;//不合格
	
	private Double withdrawnPercentage = 0d;//放弃百分比
	
	private Double completedPercentage = 0d;//已完成百分比
	
	private Double penddingPercentage = 0d;//等待审批百分比
	
	private Double waitingPercentage = 0d;//等待百分比
	
	private Double rejectedPercentage = 0d;//拒绝百分比
	
	private Double inProgressPercentage = 0d;//进行中状态百分比 
	
	private Double totleCovTotleTime = 0d;//学习总时长
	
	private Double avgCovTotleTime = 0d; //平均学习时长
	
	private String totleCovTotleDisplayTime ;//用于显示学习总时
	
	private String avgCovTotleDisplayTime ; //用于显示平均学习时长
	
	private Integer courseType;// 0:网上课程  1:面授课程  2：网上考试  3:离线考试 4:项目式培训
	
	private Double totalIesCredit;//学员获得的总学分
	
	private Double iesCredit;//课程设置的学分

	public String getItmTitle() {
		return itmTitle;
	}

	public void setItmTitle(String itmTitle) {
		this.itmTitle = itmTitle;
	}

	public Integer getEnrollCount() {
		return enrollCount;
	}

	public void setEnrollCount(Integer enrollCount) {
		this.enrollCount = enrollCount;
	}

	public Double getWithdrawnPercentage() {
		return withdrawnPercentage;
	}

	public void setWithdrawnPercentage(Double withdrawnPercentage) {
		this.withdrawnPercentage = withdrawnPercentage;
	}

	public Double getCompletedPercentage() {
		return completedPercentage;
	}

	public void setCompletedPercentage(Double completedPercentage) {
		this.completedPercentage = completedPercentage;
	}

	public Double getPenddingPercentage() {
		return penddingPercentage;
	}

	public void setPenddingPercentage(Double penddingPercentage) {
		this.penddingPercentage = penddingPercentage;
	}

	public Double getWaitingPercentage() {
		return waitingPercentage;
	}

	public void setWaitingPercentage(Double waitingPercentage) {
		this.waitingPercentage = waitingPercentage;
	}

	public Double getRejectedPercentage() {
		return rejectedPercentage;
	}

	public void setRejectedPercentage(Double rejectedPercentage) {
		this.rejectedPercentage = rejectedPercentage;
	}

	public Double getInProgressPercentage() {
		return inProgressPercentage;
	}

	public void setInProgressPercentage(Double inProgressPercentage) {
		this.inProgressPercentage = inProgressPercentage;
	}

	public Double getTotleCovTotleTime() {
		return totleCovTotleTime;
	}

	public void setTotleCovTotleTime(Double totleCovTotleTime) {
		this.totleCovTotleTime = totleCovTotleTime;
	}

	public Double getAvgCovTotleTime() {
		return avgCovTotleTime;
	}

	public void setAvgCovTotleTime(Double avgCovTotleTime) {
		this.avgCovTotleTime = avgCovTotleTime;
	}

	public Integer getFailCount() {
		return failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	public Integer getWithdrawnCount() {
		return withdrawnCount;
	}

	public void setWithdrawnCount(Integer withdrawnCount) {
		this.withdrawnCount = withdrawnCount;
	}

	public Integer getCompletedCount() {
		return completedCount;
	}

	public void setCompletedCount(Integer completedCount) {
		this.completedCount = completedCount;
	}

	public Integer getPenddingCount() {
		return penddingCount;
	}

	public void setPenddingCount(Integer penddingCount) {
		this.penddingCount = penddingCount;
	}

	public Integer getWaitingCount() {
		return waitingCount;
	}

	public void setWaitingCount(Integer waitingCount) {
		this.waitingCount = waitingCount;
	}

	public Integer getRejectedCount() {
		return rejectedCount;
	}

	public void setRejectedCount(Integer rejectedCount) {
		this.rejectedCount = rejectedCount;
	}

	public Integer getInProgressCount() {
		return inProgressCount;
	}

	public void setInProgressCount(Integer inProgressCount) {
		this.inProgressCount = inProgressCount;
	}

	public Double getFailPercentage() {
		return failPercentage;
	}

	public void setFailPercentage(Double failPercentage) {
		this.failPercentage = failPercentage;
	}

	public Long getItmId() {
		return itmId;
	}

	public void setItmId(Long itmId) {
		this.itmId = itmId;
	}

	public String getTcName() {
		return tcName;
	}

	public void setTcName(String tcName) {
		this.tcName = tcName;
	}

	public String getItmCode() {
		return itmCode;
	}

	public void setItmCode(String itmCode) {
		this.itmCode = itmCode;
	}

	public String getTotleCovTotleDisplayTime() {
		return totleCovTotleDisplayTime;
	}

	public void setTotleCovTotleDisplayTime(String totleCovTotleDisplayTime) {
		this.totleCovTotleDisplayTime = totleCovTotleDisplayTime;
	}

	public String getAvgCovTotleDisplayTime() {
		return avgCovTotleDisplayTime;
	}

	public void setAvgCovTotleDisplayTime(String avgCovTotleDisplayTime) {
		this.avgCovTotleDisplayTime = avgCovTotleDisplayTime;
	}

	public Integer getCourseType() {
		return courseType;
	}

	public void setCourseType(Integer courseType) {
		this.courseType = courseType;
	}

	public Double getTotalIesCredit() {
		return totalIesCredit;
	}

	public void setTotalIesCredit(Double totalIesCredit) {
		this.totalIesCredit = totalIesCredit;
	}

	public Double getIesCredit() {
		return iesCredit;
	}

	public void setIesCredit(Double iesCredit) {
		this.iesCredit = iesCredit;
	}

	
}
