package com.cwn.wizbank.entity.vo;

import java.io.Serializable;
import java.util.Date;

public class HelpQuestionTypeVo implements Serializable {
	
	private static final long serialVersionUID = -8311793297442374912L;

	/**
	 * pk
	 */
	private Integer id;
	
	/**
	 * 类型名称
	 */
	private String type_name;
	
	/**
	 * 上级类型id 0为父类型
	 */
	private Integer pid;
	
	/**
	 * 置顶指数 指数越高排序越前
	 */
	private Integer top_index;
	
	/**
	 * 是否发布 1:是 0:否
	 */
	private Integer is_publish;
	
	/**
	 * 创建时间
	 */
	private Date create_timestamp;
	
	private String language;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getTop_index() {
		return top_index;
	}

	public void setTop_index(Integer top_index) {
		this.top_index = top_index;
	}

	public Integer getIs_publish() {
		return is_publish;
	}

	public void setIs_publish(Integer is_publish) {
		this.is_publish = is_publish;
	}

	public Date getCreate_timestamp() {
		return create_timestamp;
	}

	public void setCreate_timestamp(Date create_timestamp) {
		this.create_timestamp = create_timestamp;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	
}
