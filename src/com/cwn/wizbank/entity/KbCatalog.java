package com.cwn.wizbank.entity;

import java.sql.Timestamp;

/**
 * 知识中心 - 目录
 */
public class KbCatalog {
	// 状态
	public final static String STATUS_ON = "ON"; // 已发布
	public final static String STATUS_OFF = "OFF"; // 未发布
	public final static String TYPE_TEMP = "TEMP"; // 临时目录

	private Long kbc_id;
	private String kbc_title;
	private String kbc_desc;
	private String kbc_status;
	private TcTrainingCenter tcTrainingCenter;
	private Timestamp kbc_create_datetime;
	private String kbc_create_user_id;
	private Timestamp kbc_update_datetime;
	private String kbc_update_user_id;
	private Long kbc_knowledge_number;// 知识数
	private String kbc_type;
	private String isChecked;// 0表示否，1表示是
	private String encrypt_kbc_id;

	public Long getKbc_id() {
		return kbc_id;
	}

	public void setKbc_id(Long kbc_id) {
		this.kbc_id = kbc_id;
	}

	public String getKbc_title() {
		return kbc_title;
	}

	public void setKbc_title(String kbc_title) {
		this.kbc_title = kbc_title;
	}

	public String getKbc_desc() {
		return kbc_desc;
	}

	public void setKbc_desc(String kbc_desc) {
		this.kbc_desc = kbc_desc;
	}

	public String getKbc_status() {
		return kbc_status;
	}

	public void setKbc_status(String kbc_status) {
		this.kbc_status = kbc_status;
	}

	public TcTrainingCenter getTcTrainingCenter() {
		return tcTrainingCenter;
	}

	public void setTcTrainingCenter(TcTrainingCenter tcTrainingCenter) {
		this.tcTrainingCenter = tcTrainingCenter;
	}

	public Timestamp getKbc_create_datetime() {
		return kbc_create_datetime;
	}

	public void setKbc_create_datetime(Timestamp kbc_create_datetime) {
		this.kbc_create_datetime = kbc_create_datetime;
	}

	public String getKbc_create_user_id() {
		return kbc_create_user_id;
	}

	public void setKbc_create_user_id(String kbc_create_user_id) {
		this.kbc_create_user_id = kbc_create_user_id;
	}

	public Timestamp getKbc_update_datetime() {
		return kbc_update_datetime;
	}

	public void setKbc_update_datetime(Timestamp kbc_update_datetime) {
		this.kbc_update_datetime = kbc_update_datetime;
	}

	public String getKbc_update_user_id() {
		return kbc_update_user_id;
	}

	public void setKbc_update_user_id(String kbc_update_user_id) {
		this.kbc_update_user_id = kbc_update_user_id;
	}

	public Long getKbc_knowledge_number() {
		return kbc_knowledge_number;
	}

	public void setKbc_knowledge_number(Long kbc_knowledge_number) {
		this.kbc_knowledge_number = kbc_knowledge_number;
	}

	public String getKbc_type() {
		return kbc_type;
	}

	public void setKbc_type(String kbc_type) {
		this.kbc_type = kbc_type;
	}

	public String getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
	}

	public String getEncrypt_kbc_id() {
		return encrypt_kbc_id;
	}

	public void setEncrypt_kbc_id(String encrypt_kbc_id) {
		this.encrypt_kbc_id = encrypt_kbc_id;
	}
	
}
