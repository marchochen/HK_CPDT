package com.cwn.wizbank.entity;

import java.util.Date;

public class PositionLoginReport extends SystemLog {

	
	/**
	 * 岗位id
	 */
	int pslrUptId;
	/**
	 * 年份
	 */
	int pslrYear;
	/**
	 * 月份
	 */
	int pslrMonth;
    /**
     * 登录入口
     */
	int pslrLoginMode;
	/**
	 * 登录人次
	 */
	int pslrTotleLoginNumber;
	/**
	 * 最后更新日期
	 */
	Date pslrLastUpdateDate;
	public int getPslrUptId() {
		return pslrUptId;
	}
	public void setPslrUptId(int pslrUptId) {
		this.pslrUptId = pslrUptId;
	}
	public int getPslrYear() {
		return pslrYear;
	}
	public void setPslrYear(int pslrYear) {
		this.pslrYear = pslrYear;
	}
	public int getPslrMonth() {
		return pslrMonth;
	}
	public void setPslrMonth(int pslrMonth) {
		this.pslrMonth = pslrMonth;
	}
	public int getPslrLoginMode() {
		return pslrLoginMode;
	}
	public void setPslrLoginMode(int pslrLoginMode) {
		this.pslrLoginMode = pslrLoginMode;
	}
	public int getPslrTotleLoginNumber() {
		return pslrTotleLoginNumber;
	}
	public void setPslrTotleLoginNumber(int pslrTotleLoginNumber) {
		this.pslrTotleLoginNumber = pslrTotleLoginNumber;
	}
	public Date getPslrLastUpdateDate() {
		return pslrLastUpdateDate;
	}
	public void setPslrLastUpdateDate(Date pslrLastUpdateDate) {
		this.pslrLastUpdateDate = pslrLastUpdateDate;
	}
	
}
