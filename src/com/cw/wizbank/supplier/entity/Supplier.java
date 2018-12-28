package com.cw.wizbank.supplier.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Supplier entity. @author MyEclipse Persistence Tools
 */

public class Supplier implements java.io.Serializable {

	// Fields

	private Integer splId;
	private String splName;
	private String splRepresentative;
	private Timestamp splEstablishedDate;
	private String splRegisteredCapital;

	private String splType;
	private String splAddress;
	private String splPostNum;
	private String splContact;
	private String splTel;
	private String splMobile;
	private String splEmail;
	private Integer splTotalStaff;
	private Integer splFullTimeInst;
	private Integer splPartTimeInst;
	private String splExpertise;
	private String splCourse;
	private String splStatus;
	private String splAttachment1;
	private String splAttachment2;
	private String splAttachment3;
	private String splAttachment4;
	private String splAttachment5;
	private String splAttachment6;
	private String splAttachment7;
	private String splAttachment8;
	private String splAttachment9;
	private String splAttachment10;
	private Timestamp splCreateDatetime;
	private String splCreateUsrId;
	private Timestamp splUpdateDatetime;
	private String splUpdateUsrId;

	private Float scmScore;
	private Long splTcrId;

	// Constructors

	/** default constructor */
	public Supplier() {
	}

	/** minimal constructor */
	public Supplier(Integer splId, String splName) {
		this.splId = splId;
		this.splName = splName;
	}

	public Integer getSplId() {
		return splId;
	}

	public void setSplId(Integer splId) {
		this.splId = splId;
	}

	public String getSplName() {
		return splName;
	}

	public void setSplName(String splName) {
		this.splName = splName;
	}

	public String getSplRegisteredCapital() {
		if(splRegisteredCapital == null || "".equals(splRegisteredCapital)){
			return "0";
		}
		return splRegisteredCapital;
	}

	public void setSplRegisteredCapital(String splRegisteredCapital) {
		this.splRegisteredCapital = splRegisteredCapital;
	}

	public String getSplType() {
		return splType;
	}

	public void setSplType(String splType) {
		this.splType = splType;
	}

	public String getSplAddress() {
		return splAddress;
	}

	public void setSplAddress(String splAddress) {
		this.splAddress = splAddress;
	}

	public String getSplPostNum() {
		return splPostNum;
	}

	public void setSplPostNum(String splPostNum) {
		this.splPostNum = splPostNum;
	}

	public String getSplContact() {
		return splContact;
	}

	public void setSplContact(String splContact) {
		this.splContact = splContact;
	}

	public String getSplTel() {
		return splTel;
	}

	public void setSplTel(String splTel) {
		this.splTel = splTel;
	}

	public String getSplMobile() {
		return splMobile;
	}

	public void setSplMobile(String splMobile) {
		this.splMobile = splMobile;
	}

	public String getSplEmail() {
		return splEmail;
	}

	public void setSplEmail(String splEmail) {
		this.splEmail = splEmail;
	}

	public Integer getSplTotalStaff() {
		if(splTotalStaff == null){
			splTotalStaff = 0;
		}
		return splTotalStaff;
	}

	public void setSplTotalStaff(Integer splTotalStaff) {
		this.splTotalStaff = splTotalStaff;
	}

	public Integer getSplFullTimeInst() {
		if(splFullTimeInst == null){
			splFullTimeInst = 0;
		}
		return splFullTimeInst;
	}

	public void setSplFullTimeInst(Integer splFullTimeInst) {
		this.splFullTimeInst = splFullTimeInst;
	}

	public Integer getSplPartTimeInst() {
		if(splPartTimeInst == null){
			splPartTimeInst = 0;
		}
		return splPartTimeInst;
	}

	public void setSplPartTimeInst(Integer splPartTimeInst) {
		this.splPartTimeInst = splPartTimeInst;
	}

	public String getSplExpertise() {
		return splExpertise;
	}

	public void setSplExpertise(String splExpertise) {
		this.splExpertise = splExpertise;
	}

	public String getSplCourse() {
		return splCourse;
	}

	public void setSplCourse(String splCourse) {
		this.splCourse = splCourse;
	}

	public String getSplStatus() {
		return splStatus;
	}

	public void setSplStatus(String splStatus) {
		this.splStatus = splStatus;
	}

	public String getSplAttachment1() {
		return splAttachment1;
	}

	public void setSplAttachment1(String splAttachment1) {
		this.splAttachment1 = splAttachment1;
	}

	public String getSplAttachment2() {
		return splAttachment2;
	}

	public void setSplAttachment2(String splAttachment2) {
		this.splAttachment2 = splAttachment2;
	}

	public String getSplAttachment3() {
		return splAttachment3;
	}

	public void setSplAttachment3(String splAttachment3) {
		this.splAttachment3 = splAttachment3;
	}

	public String getSplAttachment4() {
		return splAttachment4;
	}

	public void setSplAttachment4(String splAttachment4) {
		this.splAttachment4 = splAttachment4;
	}

	public String getSplAttachment5() {
		return splAttachment5;
	}

	public void setSplAttachment5(String splAttachment5) {
		this.splAttachment5 = splAttachment5;
	}

	public String getSplAttachment6() {
		return splAttachment6;
	}

	public void setSplAttachment6(String splAttachment6) {
		this.splAttachment6 = splAttachment6;
	}

	public String getSplAttachment7() {
		return splAttachment7;
	}

	public void setSplAttachment7(String splAttachment7) {
		this.splAttachment7 = splAttachment7;
	}

	public String getSplAttachment8() {
		return splAttachment8;
	}

	public void setSplAttachment8(String splAttachment8) {
		this.splAttachment8 = splAttachment8;
	}

	public String getSplAttachment9() {
		return splAttachment9;
	}

	public void setSplAttachment9(String splAttachment9) {
		this.splAttachment9 = splAttachment9;
	}

	public String getSplAttachment10() {
		return splAttachment10;
	}

	public void setSplAttachment10(String splAttachment10) {
		this.splAttachment10 = splAttachment10;
	}

	public String getSplCreateUsrId() {
		return splCreateUsrId;
	}

	public void setSplCreateUsrId(String splCreateUsrId) {
		this.splCreateUsrId = splCreateUsrId;
	}

	public String getSplUpdateUsrId() {
		return splUpdateUsrId;
	}

	public void setSplUpdateUsrId(String splUpdateUsrId) {
		this.splUpdateUsrId = splUpdateUsrId;
	}

	public Float getScmScore() {
		return scmScore;
	}

	public void setScmScore(Float scmScore) {
		this.scmScore = scmScore;
	}

	public Timestamp getSplEstablishedDate() {
		return splEstablishedDate;
	}

	public void setSplEstablishedDate(Timestamp splEstablishedDate) {
		this.splEstablishedDate = splEstablishedDate;
	}

	public Timestamp getSplCreateDatetime() {
		return splCreateDatetime;
	}

	public void setSplCreateDatetime(Timestamp splCreateDatetime) {
		this.splCreateDatetime = splCreateDatetime;
	}

	public Timestamp getSplUpdateDatetime() {
		return splUpdateDatetime;
	}

	public void setSplUpdateDatetime(Timestamp splUpdateDatetime) {
		this.splUpdateDatetime = splUpdateDatetime;
	}

	public String getSplRepresentative() {
		return splRepresentative;
	}

	public void setSplRepresentative(String splRepresentative) {
		this.splRepresentative = splRepresentative;
	}

	public Long getSplTcrId() {
		return splTcrId;
	}

	public void setSplTcrId(Long splTcrId) {
		this.splTcrId = splTcrId;
	}
	
}