package com.cw.wizbank.newmessage.entity;

import java.sql.Timestamp;

public class WebMessage {
	String wmsg_id;
	String wmsg_id_str;
	long wmsg_mtp_id;
	long wmsg_send_ent_id;
	long wmsg_rec_ent_id;
	String wmsg_type;
	Timestamp wmsg_target_datetime;
	String wmsg_subject;
	String wmsg_content_pc;
	String wmsg_admin_content_pc;
	String wmsg_content_mobile;
	String wmsg_attachment;
	long wmsg_create_ent_id;
	Timestamp wmsg_create_timestamp;
	
	long wmsg_type_total;

	public static String WMSG_TYPE_SYS = "SYS";
	public static String WMSG_TYPE_PERSON = "PERSON";

	public String getWmsg_id_str() {
		return wmsg_id_str;
	}

	public void setWmsg_id_str(String wmsg_id_str) {
		this.wmsg_id_str = wmsg_id_str;
	}

	public String getWmsg_id() {
		return wmsg_id;
	}

	public void setWmsg_id(String wmsg_id) {
		this.wmsg_id = wmsg_id;
	}

	public String getWmsg_attachment() {
		return wmsg_attachment;
	}
	public void setWmsg_attachment(String wmsg_attachment) {
		this.wmsg_attachment = wmsg_attachment;
	}
	public long getWmsg_mtp_id() {
		return wmsg_mtp_id;
	}
	public void setWmsg_mtp_id(long wmsg_mtp_id) {
		this.wmsg_mtp_id = wmsg_mtp_id;
	}
	public long getWmsg_send_ent_id() {
		return wmsg_send_ent_id;
	}
	public void setWmsg_send_ent_id(long wmsg_send_ent_id) {
		this.wmsg_send_ent_id = wmsg_send_ent_id;
	}
	public long getWmsg_rec_ent_id() {
		return wmsg_rec_ent_id;
	}
	public void setWmsg_rec_ent_id(long wmsg_rec_ent_id) {
		this.wmsg_rec_ent_id = wmsg_rec_ent_id;
	}
	public String getWmsg_type() {
		return wmsg_type;
	}
	public void setWmsg_type(String wmsg_type) {
		this.wmsg_type = wmsg_type;
	}
	public Timestamp getWmsg_target_datetime() {
		return wmsg_target_datetime;
	}
	public void setWmsg_target_datetime(Timestamp wmsg_target_datetime) {
		this.wmsg_target_datetime = wmsg_target_datetime;
	}
	public String getWmsg_subject() {
		return wmsg_subject;
	}
	public void setWmsg_subject(String wmsg_subject) {
		this.wmsg_subject = wmsg_subject;
	}
	public String getWmsg_content_pc() {
		return wmsg_content_pc;
	}
	public void setWmsg_content_pc(String wmsg_content_pc) {
		this.wmsg_content_pc = wmsg_content_pc;
	}
	public String getWmsg_content_mobile() {
		return wmsg_content_mobile;
	}
	public void setWmsg_content_mobile(String wmsg_content_mobile) {
		this.wmsg_content_mobile = wmsg_content_mobile;
	}
	public long getWmsg_create_ent_id() {
		return wmsg_create_ent_id;
	}
	public void setWmsg_create_ent_id(long wmsg_create_ent_id) {
		this.wmsg_create_ent_id = wmsg_create_ent_id;
	}
	public Timestamp getWmsg_create_timestamp() {
		return wmsg_create_timestamp;
	}
	public void setWmsg_create_timestamp(Timestamp wmsg_create_timestamp) {
		this.wmsg_create_timestamp = wmsg_create_timestamp;
	}
	public String getWmsg_admin_content_pc() {
		return wmsg_admin_content_pc;
	}
	public void setWmsg_admin_content_pc(String wmsg_admin_content_pc) {
		this.wmsg_admin_content_pc = wmsg_admin_content_pc;
	}
	public long getWmsg_type_total() {
		return wmsg_type_total;
	}
	public void setWmsg_type_total(long wmsg_type_total) {
		this.wmsg_type_total = wmsg_type_total;
	} 
	
}
