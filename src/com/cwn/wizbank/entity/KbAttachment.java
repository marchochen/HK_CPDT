package com.cwn.wizbank.entity;

import java.sql.Timestamp;

/**
 * 知识中心 - 附件
 */
public class KbAttachment {
	private Long kba_id;
	private String kba_filename;
	private String kba_file;
	private String kba_remark;
	private Timestamp kba_create_datetime;
	private String kba_create_user_id;
	private Timestamp kba_update_datetime;
	private String kba_update_user_id;
	// 附件的完整连接
	private String kba_url;

	public Long getKba_id() {
		return kba_id;
	}

	public void setKba_id(Long kba_id) {
		this.kba_id = kba_id;
	}

	public String getKba_filename() {
		return kba_filename;
	}

	public void setKba_filename(String kba_filename) {
		this.kba_filename = kba_filename;
	}

	public String getKba_file() {
		return kba_file;
	}

	public void setKba_file(String kba_file) {
		this.kba_file = kba_file;
	}

	public String getKba_remark() {
		return kba_remark;
	}

	public void setKba_remark(String kba_remark) {
		this.kba_remark = kba_remark;
	}

	public Timestamp getKba_create_datetime() {
		return kba_create_datetime;
	}

	public void setKba_create_datetime(Timestamp kba_create_datetime) {
		this.kba_create_datetime = kba_create_datetime;
	}

	public String getKba_create_user_id() {
		return kba_create_user_id;
	}

	public void setKba_create_user_id(String kba_create_user_id) {
		this.kba_create_user_id = kba_create_user_id;
	}

	public Timestamp getKba_update_datetime() {
		return kba_update_datetime;
	}

	public void setKba_update_datetime(Timestamp kba_update_datetime) {
		this.kba_update_datetime = kba_update_datetime;
	}

	public String getKba_update_user_id() {
		return kba_update_user_id;
	}

	public void setKba_update_user_id(String kba_update_user_id) {
		this.kba_update_user_id = kba_update_user_id;
	}

	public String getKba_url() {
		return kba_url;
	}

	public void setKba_url(String kba_url) {
		this.kba_url = kba_url;
	}

}
