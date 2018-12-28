package com.cwn.wizbank.entity;

import java.sql.Timestamp;

/**
 * 知识中心 - 知识点与知识标签关系类
 */
public class KbItemTag {
	private Tag tag;
	private KbItem kbItem;
	private Timestamp kit_create_datetime;
	private String kit_create_user_id;

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public KbItem getKbItem() {
		return kbItem;
	}

	public void setKbItem(KbItem kbItem) {
		this.kbItem = kbItem;
	}

	public Timestamp getKit_create_datetime() {
		return kit_create_datetime;
	}

	public void setKit_create_datetime(Timestamp kit_create_datetime) {
		this.kit_create_datetime = kit_create_datetime;
	}

	public String getKit_create_user_id() {
		return kit_create_user_id;
	}

	public void setKit_create_user_id(String kit_create_user_id) {
		this.kit_create_user_id = kit_create_user_id;
	}

}
