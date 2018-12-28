package com.cw.wizbank.supplier.entity;

import java.util.Date;
/**
 * SupplierComment entity. @author MyEclipse Persistence Tools
 */

public class SupplierComment implements java.io.Serializable {

	// Fields

	private Integer scmId;
	private Integer scmSplId;
	private Supplier supplier;
	private Integer scmEntId;
	private Double scmDesignScore;
	private Double scmTeachingScore;
	private Double scmPriceScore;
	private String scmComment;
	private Date scmUpdateDatetime;
	private Date scmCreateDatetime;

	// Constructors

	/** default constructor */
	public SupplierComment() {
	}

	public Integer getScmId() {
		return scmId;
	}

	public void setScmId(Integer scmId) {
		this.scmId = scmId;
	}

	public Integer getScmSplId() {
		return scmSplId;
	}

	public void setScmSplId(Integer scmSplId) {
		this.scmSplId = scmSplId;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Integer getScmEntId() {
		return scmEntId;
	}

	public void setScmEntId(Integer scmEntId) {
		this.scmEntId = scmEntId;
	}

	public Double getScmDesignScore() {
		return scmDesignScore;
	}

	public void setScmDesignScore(Double scmDesignScore) {
		this.scmDesignScore = scmDesignScore;
	}

	public Double getScmTeachingScore() {
		return scmTeachingScore;
	}

	public void setScmTeachingScore(Double scmTeachingScore) {
		this.scmTeachingScore = scmTeachingScore;
	}

	public Double getScmPriceScore() {
		return scmPriceScore;
	}

	public void setScmPriceScore(Double scmPriceScore) {
		this.scmPriceScore = scmPriceScore;
	}

	public String getScmComment() {
		return scmComment;
	}

	public void setScmComment(String scmComment) {
		this.scmComment = scmComment;
	}

	public Date getScmUpdateDatetime() {
		return scmUpdateDatetime;
	}

	public void setScmUpdateDatetime(Date scmUpdateDatetime) {
		this.scmUpdateDatetime = scmUpdateDatetime;
	}

	public Date getScmCreateDatetime() {
		return scmCreateDatetime;
	}

	public void setScmCreateDatetime(Date scmCreateDatetime) {
		this.scmCreateDatetime = scmCreateDatetime;
	}


}