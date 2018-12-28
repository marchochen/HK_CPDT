package com.cwn.wizbank.entity;

import java.sql.Timestamp;

/**
 * 知识中心 - 知识点与知识目录关系类
 */
public class KbItemCat {

	private KbItem kbItem;
	private KbCatalog kbCatalog;
	private Timestamp kic_create_datetime;
	private String kic_create_user_id;

	public KbItem getKbItem() {
		return kbItem;
	}

	public void setKbItem(KbItem kbItem) {
		this.kbItem = kbItem;
	}

	public KbCatalog getKbCatalog() {
		return kbCatalog;
	}

	public void setKbCatalog(KbCatalog kbCatalog) {
		this.kbCatalog = kbCatalog;
	}

	public Timestamp getKic_create_datetime() {
		return kic_create_datetime;
	}

	public void setKic_create_datetime(Timestamp kic_create_datetime) {
		this.kic_create_datetime = kic_create_datetime;
	}

	public String getKic_create_user_id() {
		return kic_create_user_id;
	}

	public void setKic_create_user_id(String kic_create_user_id) {
		this.kic_create_user_id = kic_create_user_id;
	}

}
