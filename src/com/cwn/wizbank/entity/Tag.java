package com.cwn.wizbank.entity;

import java.sql.Timestamp;

public class Tag {
	private Long tag_id;
	private String tag_title;
	private TcTrainingCenter tcTrainingCenter;
	private Timestamp tag_create_datetime;
	private String tag_create_user_id;
	private Timestamp tag_update_datetime;
	private String tag_update_user_id;
	private Long tag_knowledge_number;
	private String isChecked;
	private String encrypt_tag_id;

	public Long getTag_id() {
		return tag_id;
	}

	public void setTag_id(Long tag_id) {
		this.tag_id = tag_id;
	}

	public String getTag_title() {
		return tag_title;
	}

	public void setTag_title(String tag_title) {
		this.tag_title = tag_title;
	}

	public TcTrainingCenter getTcTrainingCenter() {
		return tcTrainingCenter;
	}

	public void setTcTrainingCenter(TcTrainingCenter tcTrainingCenter) {
		this.tcTrainingCenter = tcTrainingCenter;
	}

	public Timestamp getTag_create_datetime() {
		return tag_create_datetime;
	}

	public void setTag_create_datetime(Timestamp tag_create_datetime) {
		this.tag_create_datetime = tag_create_datetime;
	}

	public Timestamp getTag_update_datetime() {
		return tag_update_datetime;
	}

	public void setTag_update_datetime(Timestamp tag_update_datetime) {
		this.tag_update_datetime = tag_update_datetime;
	}

	public Long getTag_knowledge_number() {
		return tag_knowledge_number;
	}

	public void setTag_knowledge_number(Long tag_knowledge_number) {
		this.tag_knowledge_number = tag_knowledge_number;
	}

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	public String getTag_create_user_id() {
		return tag_create_user_id;
	}

	public void setTag_create_user_id(String tag_create_user_id) {
		this.tag_create_user_id = tag_create_user_id;
	}

	public String getTag_update_user_id() {
		return tag_update_user_id;
	}

	public void setTag_update_user_id(String tag_update_user_id) {
		this.tag_update_user_id = tag_update_user_id;
	}

	public String getEncrypt_tag_id() {
		return encrypt_tag_id;
	}

	public void setEncrypt_tag_id(String encrypt_tag_id) {
		this.encrypt_tag_id = encrypt_tag_id;
	}
	
}
