package com.cwn.wizbank.entity;

import java.sql.Timestamp;

/**
 * 知识中心 - 知识浏览表
 */
public class KbItemView {
	private Long kiv_usr_ent_id;
	private KbItem kbItem;
	private Timestamp kiv_create_datetime;
	private Timestamp kiv_update_datetime;

	public Long getKiv_usr_ent_id() {
		return kiv_usr_ent_id;
	}

	public void setKiv_usr_ent_id(Long kiv_usr_ent_id) {
		this.kiv_usr_ent_id = kiv_usr_ent_id;
	}

	public KbItem getKbItem() {
		return kbItem;
	}

	public void setKbItem(KbItem kbItem) {
		this.kbItem = kbItem;
	}

	public Timestamp getKiv_create_datetime() {
		return kiv_create_datetime;
	}

	public void setKiv_create_datetime(Timestamp kiv_create_datetime) {
		this.kiv_create_datetime = kiv_create_datetime;
	}

	public Timestamp getKiv_update_datetime() {
		return kiv_update_datetime;
	}

	public void setKiv_update_datetime(Timestamp kiv_update_datetime) {
		this.kiv_update_datetime = kiv_update_datetime;
	}

}
