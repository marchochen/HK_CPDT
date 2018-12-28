package com.cwn.wizbank.entity;

import java.util.Date;

public class HelpQuestion implements java.io.Serializable {
	
	private static final long serialVersionUID = 7368717703761693002L;
	
	/**
	 * pk 
	 */
	private Integer hq_id ;
	
	/**
	 * 所属类型id
	 */
	private Integer hq_type_id ;
	
	/**
	 * 问题标题
	 */
	private String hq_title;
	
	/**
	 * 中文内容
	 */
	private String hq_content_cn;
	
	
	/**
	 * 英文内容
	 */
	private String hq_content_us;
	
	/**
	 * 创建时间
	 */
	private Date hq_create_timestamp;
	
	/**
	 * 更新时间
	 */
	private Date hq_update_timestamp;
	
	/**
	 * 编号
	 */
	private String hqt_number;
	
	/**
	 * 宽度
	 */
	private Integer hq_width;
	
	/**
	 * 高度
	 */
	private Integer hq_height;
	
	/**
	 * 模板
	 */
	private String hq_template;

	public Integer getHq_id() {
		return hq_id;
	}

	public void setHq_id(Integer hq_id) {
		this.hq_id = hq_id;
	}

	public Integer getHq_type_id() {
		return hq_type_id;
	}

	public void setHq_type_id(Integer hq_type_id) {
		this.hq_type_id = hq_type_id;
	}

	public String getHq_title() {
		return hq_title;
	}

	public void setHq_title(String hq_title) {
		this.hq_title = hq_title;
	}

	public String getHq_content_cn() {
		return hq_content_cn;
	}

	public void setHq_content_cn(String hq_content_cn) {
		this.hq_content_cn = hq_content_cn;
	}

	public String getHq_content_us() {
		return hq_content_us;
	}

	public void setHq_content_us(String hq_content_us) {
		this.hq_content_us = hq_content_us;
	}

	public Date getHq_create_timestamp() {
		return hq_create_timestamp;
	}

	public void setHq_create_timestamp(Date hq_create_timestamp) {
		this.hq_create_timestamp = hq_create_timestamp;
	}

	public Date getHq_update_timestamp() {
		return hq_update_timestamp;
	}

	public void setHq_update_timestamp(Date hq_update_timestamp) {
		this.hq_update_timestamp = hq_update_timestamp;
	}

	public String getHqt_number() {
		return hqt_number;
	}

	public void setHqt_number(String hqt_number) {
		this.hqt_number = hqt_number;
	}

	public Integer getHq_width() {
		return hq_width;
	}

	public void setHq_width(Integer hq_width) {
		this.hq_width = hq_width;
	}

	public Integer getHq_height() {
		return hq_height;
	}

	public void setHq_height(Integer hq_height) {
		this.hq_height = hq_height;
	}

	public String getHq_template() {
		return hq_template;
	}

	public void setHq_template(String hq_template) {
		this.hq_template = hq_template;
	}
	
}
