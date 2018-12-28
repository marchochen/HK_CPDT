package com.cwn.wizbank.cpd.vo;

import java.util.Date;

public class CpdPeriodVO implements java.io.Serializable{

	private static final long serialVersionUID = -6743270934885410025L;
	
	private int period;
	
	private Date startTime;
	
	private Date endTime;
	
    private String startDate;
    
    private String endDate;

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
	
	

}
