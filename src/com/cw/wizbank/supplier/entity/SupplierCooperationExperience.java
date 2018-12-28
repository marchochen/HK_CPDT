package com.cw.wizbank.supplier.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SupplierCooperationExperience entity. @author MyEclipse Persistence Tools
 */

public class SupplierCooperationExperience implements java.io.Serializable {

	// Fields

	private Integer sceId;
	private Integer sceSplId;
	private String sceItmName;
	private Date sceStartDate;
	private Date sceEndDate;
	private String sceDesc;
	private String sceDpt;
	private Date sceUpdateDatetime;
	private String sceUpdateUsrId;
	
	private String splStatus;
	private String splName;
	private String splRepresentative;
	
	public Integer getSceId() {
		return sceId;
	}
	public void setSceId(Integer sceId) {
		this.sceId = sceId;
	}

	public String getSceItmName() {
		return sceItmName;
	}
	public void setSceItmName(String sceItmName) {
		this.sceItmName = sceItmName;
	}
	public String getSceDesc() {
		return sceDesc;
	}
	public void setSceDesc(String sceDesc) {
		this.sceDesc = sceDesc;
	}
	public String getSceDpt() {
		return sceDpt;
	}
	public void setSceDpt(String sceDpt) {
		this.sceDpt = sceDpt;
	}
	public String getSceUpdateUsrId() {
		return sceUpdateUsrId;
	}
	public void setSceUpdateUsrId(String sceUpdateUsrId) {
		this.sceUpdateUsrId = sceUpdateUsrId;
	}
	public Integer getSceSplId() {
		return sceSplId;
	}
	public void setSceSplId(Integer sceSplId) {
		this.sceSplId = sceSplId;
	}
	public void setSceUpdateDatetime(Timestamp sceUpdateDatetime) {
		this.sceUpdateDatetime = sceUpdateDatetime;
	}
	public Date getSceStartDate() {
		return sceStartDate;
	}
	public void setSceStartDate(Date sceStartDate) {
		this.sceStartDate = sceStartDate;
	}
	public Date getSceEndDate() {
		return sceEndDate;
	}
	public void setSceEndDate(Date sceEndDate) {
		this.sceEndDate = sceEndDate;
	}
	public Date getSceUpdateDatetime() {
		return sceUpdateDatetime;
	}
	public void setSceUpdateDatetime(Date sceUpdateDatetime) {
		this.sceUpdateDatetime = sceUpdateDatetime;
	}
	public String getSplStatus() {
		return splStatus;
	}
	public void setSplStatus(String splStatus) {
		this.splStatus = splStatus;
	}
	public String getSplName() {
		return splName;
	}
	public void setSplName(String splName) {
		this.splName = splName;
	}
	public String getSplRepresentative() {
		return splRepresentative;
	}
	public void setSplRepresentative(String splRepresentative) {
		this.splRepresentative = splRepresentative;
	}

}