package com.cwn.wizbank.entity;

/**
 * 文章类型
 * @author lance
 * 2014-9-11
 */
public class ArticleType implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 文章类型id
	 */
	Long aty_id;
	/**
	 * 文章类型名称
	 */
	String aty_title;
	/**
	 * 培训中心id
	 */
	Long aty_tcr_id;
	
	public Long getAty_id() {
		return aty_id;
	}
	public void setAty_id(Long aty_id) {
		this.aty_id = aty_id;
	}
	public String getAty_title() {
		return aty_title;
	}
	public void setAty_title(String aty_title) {
		this.aty_title = aty_title;
	}
	public Long getAty_tcr_id() {
		return aty_tcr_id;
	}
	public void setAty_tcr_id(Long aty_tcr_id) {
		this.aty_tcr_id = aty_tcr_id;
	}
	
}
