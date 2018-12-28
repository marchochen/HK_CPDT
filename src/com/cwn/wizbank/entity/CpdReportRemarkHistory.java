package com.cwn.wizbank.entity;

import java.util.Date;

public class CpdReportRemarkHistory implements java.io.Serializable{

	private static final long serialVersionUID = 3481811200991342315L;
	
	private Long crmh_id ;
	private Long crmh_crpm_id ;
	private String crmh_report_code ;
	private String crmh_report_remark ;
	private Date crpm_his_create_datetime ;
	private Integer crpm_his_period;
	private Integer crpm_his_save_month;
	
	public CpdReportRemarkHistory(){}
	
	public CpdReportRemarkHistory(CpdReportRemark reamrk , int period,int month,Date createDate){
		this.crmh_crpm_id = reamrk.getCrpm_id();
		this.crmh_report_code = reamrk.getCrpm_report_code();
		this.crmh_report_remark = reamrk.getCrpm_report_remark();
		this.crpm_his_create_datetime = createDate;
		this.crpm_his_period = period;
		this.crpm_his_save_month = month;
	}
	
	public Long getCrmh_id() {
		return crmh_id;
	}
	public void setCrmh_id(Long crmh_id) {
		this.crmh_id = crmh_id;
	}
	public Long getCrmh_crpm_id() {
		return crmh_crpm_id;
	}
	public void setCrmh_crpm_id(Long crmh_crpm_id) {
		this.crmh_crpm_id = crmh_crpm_id;
	}
	public String getCrmh_report_code() {
		return crmh_report_code;
	}
	public void setCrmh_report_code(String crmh_report_code) {
		this.crmh_report_code = crmh_report_code;
	}
	public String getCrmh_report_remark() {
		return crmh_report_remark;
	}
	public void setCrmh_report_remark(String crmh_report_remark) {
		this.crmh_report_remark = crmh_report_remark;
	}
	public Date getCrpm_his_create_datetime() {
		return crpm_his_create_datetime;
	}
	public void setCrpm_his_create_datetime(Date crpm_his_create_datetime) {
		this.crpm_his_create_datetime = crpm_his_create_datetime;
	}
	public Integer getCrpm_his_period() {
		return crpm_his_period;
	}
	public void setCrpm_his_period(Integer crpm_his_period) {
		this.crpm_his_period = crpm_his_period;
	}
	public Integer getCrpm_his_save_month() {
		return crpm_his_save_month;
	}
	public void setCrpm_his_save_month(Integer crpm_his_save_month) {
		this.crpm_his_save_month = crpm_his_save_month;
	}

}
