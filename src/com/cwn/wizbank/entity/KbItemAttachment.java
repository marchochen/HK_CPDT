package com.cwn.wizbank.entity;

import java.sql.Timestamp;

/**
 * 知识中心 - 知识点与知识目录关系类
 */
public class KbItemAttachment {

	private KbItem kbItem;
	private KbAttachment kbAttachment;
	private Timestamp kia_create_datetime;
	private String kia_create_user_id;

	public KbItem getKbItem() {
		return kbItem;
	}

	public void setKbItem(KbItem kbItem) {
		this.kbItem = kbItem;
	}

	public KbAttachment getKbAttachment() {
		return kbAttachment;
	}

	public void setKbAttachment(KbAttachment kbAttachment) {
		this.kbAttachment = kbAttachment;
	}

	public Timestamp getKia_create_datetime() {
		return kia_create_datetime;
	}

	public void setKia_create_datetime(Timestamp kia_create_datetime) {
		this.kia_create_datetime = kia_create_datetime;
	}

	public String getKia_create_user_id() {
		return kia_create_user_id;
	}

	public void setKia_create_user_id(String kia_create_user_id) {
		this.kia_create_user_id = kia_create_user_id;
	}

}
