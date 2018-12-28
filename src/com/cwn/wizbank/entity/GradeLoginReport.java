package com.cwn.wizbank.entity;

import java.util.Date;

public class GradeLoginReport extends SystemLog {

	/**
	 * 职级id
	 */
	int gdlrUgrId;
	/**
	 * 年份
	 */
	int gdlrYear;
	/**
	 * 月份
	 */
	int gdlrMonth;
    /**
     * 登录入口
     */
	int gdlrLoginMode;
	/**
	 * 登录人次
	 */
	int gdlrTotleLoginNumber;
	/**
	 * 最后更新日期
	 */
	Date gdlrLastUpdateDate;
	public int getGdlrUgrId() {
		return gdlrUgrId;
	}
	public void setGdlrUgrId(int gdlrUgrId) {
		this.gdlrUgrId = gdlrUgrId;
	}
	public int getGdlrYear() {
		return gdlrYear;
	}
	public void setGdlrYear(int gdlrYear) {
		this.gdlrYear = gdlrYear;
	}
	public int getGdlrMonth() {
		return gdlrMonth;
	}
	public void setGdlrMonth(int gdlrMonth) {
		this.gdlrMonth = gdlrMonth;
	}
	public int getGdlrLoginMode() {
		return gdlrLoginMode;
	}
	public void setGdlrLoginMode(int gdlrLoginMode) {
		this.gdlrLoginMode = gdlrLoginMode;
	}
	public int getGdlrTotleLoginNumber() {
		return gdlrTotleLoginNumber;
	}
	public void setGdlrTotleLoginNumber(int gdlrTotleLoginNumber) {
		this.gdlrTotleLoginNumber = gdlrTotleLoginNumber;
	}
	public Date getGdlrLastUpdateDate() {
		return gdlrLastUpdateDate;
	}
	public void setGdlrLastUpdateDate(Date gdlrLastUpdateDate) {
		this.gdlrLastUpdateDate = gdlrLastUpdateDate;
	}
	
}
