package com.cw.wizbank.supplier.entity;

import java.util.Date;
/**
 * SupplierMainCourse entity. @author MyEclipse Persistence Tools
 */

public class SupplierMainCourse implements java.io.Serializable {

	// Fields

	private Integer smcId;
	private String smcName;
	private String smcInst;
	private Double smcPrice;
	private String smcPriceShow;
	private Date smcUpdateDatetime;
	private String smcUpdateUsrId;
	private Integer smcSplId;
	
	public Integer getSmcId() {
		return smcId;
	}
	public void setSmcId(Integer smcId) {
		this.smcId = smcId;
	}
	public Integer getSmcSplId() {
		return smcSplId;
	}
	public void setSmcSplId(Integer smcSplId) {
		this.smcSplId = smcSplId;
	}
	
	public String getSmcName() {
		return smcName;
	}
	public void setSmcName(String smcName) {
		this.smcName = smcName;
	}
	public String getSmcInst() {
		return smcInst;
	}
	public void setSmcInst(String smcInst) {
		this.smcInst = smcInst;
	}
	public Double getSmcPrice() {
		return smcPrice;
	}
	public void setSmcPrice(Double smcPrice) {
		this.smcPrice = smcPrice;
		if(smcPrice >= 0){
			this.smcPriceShow = String.format("%.2f", smcPrice);
		}
	}
	public Date getSmcUpdateDatetime() {
		return smcUpdateDatetime;
	}
	public void setSmcUpdateDatetime(Date smcUpdateDatetime) {
		this.smcUpdateDatetime = smcUpdateDatetime;
	}
	public String getSmcUpdateUsrId() {
		return smcUpdateUsrId;
	}
	public void setSmcUpdateUsrId(String smcUpdateUsrId) {
		this.smcUpdateUsrId = smcUpdateUsrId;
	}
	public String getSmcPriceShow() {
		return smcPriceShow;
	}
	public void setSmcPriceShow(String smcPriceShow) {
		this.smcPriceShow = smcPriceShow;
	}

}