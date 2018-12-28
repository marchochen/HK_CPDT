package com.cwn.wizbank.cpd.vo;

import java.util.Date;

import com.cwn.wizbank.entity.CpdReportRemark;

public class CpdReportRemarkVO implements java.io.Serializable{

	private static final long serialVersionUID = -548563114899764478L;

	private String outstandingRemark;
	
	private String individualRemark;
	
	private String awardedRemark;
	
	private String licenseRegRemark;

	public String getOutstandingRemark() {
		return outstandingRemark;
	}

	public void setOutstandingRemark(String outstandingRemark) {
		this.outstandingRemark = outstandingRemark;
	}

	public String getIndividualRemark() {
		return individualRemark;
	}

	public void setIndividualRemark(String individualRemark) {
		this.individualRemark = individualRemark;
	}

	public String getAwardedRemark() {
		return awardedRemark;
	}

	public void setAwardedRemark(String awardedRemark) {
		this.awardedRemark = awardedRemark;
	}

	public String getLicenseRegRemark() {
		return licenseRegRemark;
	}

	public void setLicenseRegRemark(String licenseRegRemark) {
		this.licenseRegRemark = licenseRegRemark;
	}
	
	public static CpdReportRemark conver2Po(String code , String _remark,Long optUsrId){
		CpdReportRemark remark = new CpdReportRemark();
		remark.setCrpm_report_code(code);
		remark.setCrpm_report_remark(_remark);
		remark.setCrpm_create_usr_ent_id(optUsrId);
		remark.setCrpm_update_usr_ent_id(optUsrId);
		remark.setCrpm_create_datetime(new Date());
		remark.setCrpm_update_datetime(new Date());
		return remark;
	}

}
