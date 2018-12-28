package com.cwn.wizbank.entity.vo;

public class LearnerUserStatisticalReportVo {
	
	private Long userEntId;
	
	private String userFullName;
	
	private String userDispalyName;
	
	private String gourpName;
	
	private Integer enrollCount = 0;
	
	private Integer failCount = 0;//不合格
	
	private Integer withdrawnCount = 0;//放弃百
	
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
	
	private String totleCovTotleDisplayTime ;
	
	private Double avgCovTotleTime = 0d; //平均学习时长
	
	private String avgCovTotleDisplayTime ;
	
	private Double avgAttRatePercentage; //平均出席率
	
	private Double totleAttRate = 0d;//总出席率
	
	private Double minScore;//最小分数
	
	private Double maxScore;//最大分数
	
	private Double avgScore; //平均分
	
	private Double totleScore = 0d;//总分
	
	private String staffNo;//员工编号
	
    private Double totalIesCredit; //总学分
    
    private boolean hasIesCredit; //是否有课程设置了学分

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserDispalyName() {
		return userDispalyName;
	}

	public void setUserDispalyName(String userDispalyName) {
		this.userDispalyName = userDispalyName;
	}

	public String getGourpName() {
		return gourpName;
	}

	public void setGourpName(String gourpName) {
		this.gourpName = gourpName;
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
	
	public Double getAvgAttRatePercentage() {
		return avgAttRatePercentage;
	}

	public void setAvgAttRatePercentage(Double avgAttRatePercentage) {
		this.avgAttRatePercentage = avgAttRatePercentage;
	}

	public Double getTotleAttRate() {
		return totleAttRate;
	}

	public void setTotleAttRate(Double totleAttRate) {
		this.totleAttRate = totleAttRate;
	}

	public Double getMinScore() {
		return minScore;
	}

	public void setMinScore(Double minScore) {
		this.minScore = minScore;
	}

	public Double getMaxScore() {
		return maxScore;
	}

	public void setMaxScore(Double maxScore) {
		this.maxScore = maxScore;
	}

	public Double getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(Double avgScore) {
		this.avgScore = avgScore;
	}

	public Double getTotleScore() {
		return totleScore;
	}

	public void setTotleScore(Double totleScore) {
		this.totleScore = totleScore;
	}

	public Long getUserEntId() {
		return userEntId;
	}

	public void setUserEntId(Long userEntId) {
		this.userEntId = userEntId;
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

    public Double getTotalIesCredit() {
        return totalIesCredit;
    }

    public void setTotalIesCredit(Double totalIesCredit) {
        this.totalIesCredit = totalIesCredit;
    }
    
	public String getStaffNo() {
		return staffNo;
	}

    public boolean getHasIesCredit() {
        return hasIesCredit;
    }

    public void setHasIesCredit(boolean hasIesCredit) {
        this.hasIesCredit = hasIesCredit;
    }

	public void setStaffNo(String staffNo) {
		this.staffNo = staffNo;
	}
	
}
