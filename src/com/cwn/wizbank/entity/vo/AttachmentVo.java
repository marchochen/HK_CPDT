package com.cwn.wizbank.entity.vo;

import java.util.Date;

public class AttachmentVo {

	/**
	 * pk
	 * null
	 **/
	Long id;
	/**
	 * null
	 **/
	Long target_id;
	/**
	 * null
	 **/
	String module;
	/**
	 * null
	 **/
	Long usr_id;
	/**
	 * null
	 **/
	String file_type;
	/**
	 * 
	 */
	String type;
	/**
	 * null
	 **/
	String file_name;
	/**
	 * null
	 **/
	String file_rename;
	/**
	 * null
	 **/
	Long file_size;
	/**
	 * null
	 **/
	Date create_time;
	/**
	 * null
	 **/
	String url;
		
	String full_path;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTarget_id() {
		return target_id;
	}
	public void setTarget_id(Long target_id) {
		this.target_id = target_id;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public Long getUsr_id() {
		return usr_id;
	}
	public void setUsr_id(Long usr_id) {
		this.usr_id = usr_id;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_rename() {
		return file_rename;
	}
	public void setFile_rename(String file_rename) {
		this.file_rename = file_rename;
	}
	public Long getFile_size() {
		return file_size;
	}
	public void setFile_size(Long file_size) {
		this.file_size = file_size;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFull_path() {
		return full_path;
	}
	public void setFull_path(String full_path) {
		this.full_path = full_path;
	}
	
	
	
}
