package com.cwn.wizbank.entity;

import java.util.Date;

public class HelpQuestionType implements java.io.Serializable  {

	private static final long serialVersionUID = -1378376917898139485L;
	
	/**
	 * pk
	 */
	private Integer hqt_id;
	
	/**
	 * 类型名称
	 */
	private String hqt_type_name;
	
	/**
	 * 上级类型id 0为父类型
	 */
	private Integer hqt_pid;
	
	/**
	 * 置顶指数 指数越高排序越前
	 */
	private Integer hqt_top_index;
	
	/**
	 * 是否发布 1:是 0:否
	 */
	private Integer hqt_is_publish;
	
	/**
	 * 创建时间
	 */
	private Date hqt_create_timestamp;
	
	private String hqt_language;

	public Integer getHqt_id() {
		return hqt_id;
	}

	public void setHqt_id(Integer hqt_id) {
		this.hqt_id = hqt_id;
	}

	public String getHqt_type_name() {
		return hqt_type_name;
	}

	public void setHqt_type_name(String hqt_type_name) {
		this.hqt_type_name = hqt_type_name;
	}

	public Integer getHqt_pid() {
		return hqt_pid;
	}

	public void setHqt_pid(Integer hqt_pid) {
		this.hqt_pid = hqt_pid;
	}

	public Integer getHqt_top_index() {
		return hqt_top_index;
	}

	public void setHqt_top_index(Integer hqt_top_index) {
		this.hqt_top_index = hqt_top_index;
	}

	public Integer getHqt_is_publish() {
		return hqt_is_publish;
	}

	public void setHqt_is_publish(Integer hqt_is_publish) {
		this.hqt_is_publish = hqt_is_publish;
	}

	public Date getHqt_create_timestamp() {
		return hqt_create_timestamp;
	}

	public void setHqt_create_timestamp(Date hqt_create_timestamp) {
		this.hqt_create_timestamp = hqt_create_timestamp;
	}

	public String getHqt_language() {
		return hqt_language;
	}

	public void setHqt_language(String hqt_language) {
		this.hqt_language = hqt_language;
	}

	
}
