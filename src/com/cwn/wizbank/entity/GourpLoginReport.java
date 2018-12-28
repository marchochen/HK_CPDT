package com.cwn.wizbank.entity;

import java.util.Date;

public class GourpLoginReport extends SystemLog {

	/**
	 * 用户组id
	 */
	int gplrGrpId;
	/**
	 * 年份
	 */
	int gplrYear;
	/**
	 * 月份
	 */
	int gplrMonth;
    /**
     * 登录入口
     */
	int gplrLoginMode;
	/**
	 * 登录人次
	 */
	int gplrTotleLoginNumber;
	/**
	 * 最后更新日期
	 */
	Date gplrLastUpdateDate;
	public int getGplrGrpId() {
		return gplrGrpId;
	}
	public void setGplrGrpId(int gplrGrpId) {
		this.gplrGrpId = gplrGrpId;
	}
	public int getGplrYear() {
		return gplrYear;
	}
	public void setGplrYear(int gplrYear) {
		this.gplrYear = gplrYear;
	}
	public int getGplrMonth() {
		return gplrMonth;
	}
	public void setGplrMonth(int gplrMonth) {
		this.gplrMonth = gplrMonth;
	}
	public int getGplrLoginMode() {
		return gplrLoginMode;
	}
	public void setGplrLoginMode(int gplrLoginMode) {
		this.gplrLoginMode = gplrLoginMode;
	}
	public int getGplrTotleLoginNumber() {
		return gplrTotleLoginNumber;
	}
	public void setGplrTotleLoginNumber(int gplrTotleLoginNumber) {
		this.gplrTotleLoginNumber = gplrTotleLoginNumber;
	}
	public Date getGplrLastUpdateDate() {
		return gplrLastUpdateDate;
	}
	public void setGplrLastUpdateDate(Date gplrLastUpdateDate) {
		this.gplrLastUpdateDate = gplrLastUpdateDate;
	}
	
	

}
