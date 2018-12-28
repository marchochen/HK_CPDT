package com.cwn.wizbank.entity.vo;

import java.io.Serializable;

public class CatalogTreeVo implements Serializable{

	private static final long serialVersionUID = 5927095338354617314L;
	
	private Long tndItmId ;
	private Long tnrOrder ;
	private Long tndId ;
	private Long parentTndId = null;
	private String tndTitle ;
	private Long nextParent;
	
	public Long getTndItmId() {
		return tndItmId;
	}
	public void setTndItmId(Long tndItmId) {
		this.tndItmId = tndItmId;
	}
	public Long getTnrOrder() {
		return tnrOrder;
	}
	public void setTnrOrder(Long tnrOrder) {
		this.tnrOrder = tnrOrder;
	}
	public Long getTndId() {
		return tndId;
	}
	public void setTndId(Long tndId) {
		this.tndId = tndId;
	}
	public Long getParentTndId() {
		return parentTndId;
	}
	public void setParentTndId(Long parentTndId) {
		this.parentTndId = parentTndId;
	}
	public String getTndTitle() {
		return tndTitle;
	}
	public void setTndTitle(String tndTitle) {
		this.tndTitle = tndTitle;
	}
	public Long getNextParent() {
		return nextParent;
	}
	public void setNextParent(Long nextParent) {
		this.nextParent = nextParent;
	}
	
}
