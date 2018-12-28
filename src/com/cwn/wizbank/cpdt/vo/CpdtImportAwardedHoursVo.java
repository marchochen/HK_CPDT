package com.cwn.wizbank.cpdt.vo;

import java.util.Date;

/**
 * 导入CPD/CPT获取时数
* Title: CpdtImportAwardedHoursVo.java 
* Description: 包含导入CPD/CPT获取时数需要的相关属性
* @author Jaren  
* @date 2018年11月30日
 */
public class CpdtImportAwardedHoursVo {
	
	/** 用户名 **/
	private String UserId;
	/** 课程或班级编号 **/
	private String CourseCode;
	/** 牌照类型编号 **/
	private String licenseType;
	/** 小牌编号 **/
	private String groupCode;
	/** 获取时数日期(String) **/
	private String strHoursAwardDate;
	/** 获取时数日期(Date) **/
	private Date hoursAwardDate;
	/** 核心时数 **/
	private float coreHours;
	/** 非核心时数 **/
	private float nonCoreHours;
	/** 大牌ID **/
	private long ctId;
	/** 小牌ID **/
	private long cgId;
	/** 课程时数规则id **/
	private long acgiId;
	/** 课程ID **/
	private long itmId;
	
	public String getUserId() {
		return UserId;
	}
	public void setUserId(String userId) {
		UserId = userId;
	}
	public String getCourseCode() {
		return CourseCode;
	}
	public void setCourseCode(String courseCode) {
		CourseCode = courseCode;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getStrHoursAwardDate() {
		return strHoursAwardDate;
	}
	public void setStrHoursAwardDate(String strHoursAwardDate) {
		this.strHoursAwardDate = strHoursAwardDate;
	}
	public Date getHoursAwardDate() {
		return hoursAwardDate;
	}
	public void setHoursAwardDate(Date hoursAwardDate) {
		this.hoursAwardDate = hoursAwardDate;
	}
	public float getCoreHours() {
		return coreHours;
	}
	public void setCoreHours(float coreHours) {
		this.coreHours = coreHours;
	}
	public float getNonCoreHours() {
		return nonCoreHours;
	}
	public void setNonCoreHours(float nonCoreHours) {
		this.nonCoreHours = nonCoreHours;
	}
	public long getCtId() {
		return ctId;
	}
	public void setCtId(long ctId) {
		this.ctId = ctId;
	}
	public long getCgId() {
		return cgId;
	}
	public void setCgId(long cgId) {
		this.cgId = cgId;
	}
	public long getAcgiId() {
		return acgiId;
	}
	public void setAcgiId(long acgiId) {
		this.acgiId = acgiId;
	}
	public long getItmId() {
		return itmId;
	}
	public void setItmId(long itmId) {
		this.itmId = itmId;
	}
	
	

}
