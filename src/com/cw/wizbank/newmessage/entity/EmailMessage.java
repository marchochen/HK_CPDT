package com.cw.wizbank.newmessage.entity;

import java.sql.Timestamp;

public class EmailMessage {
	long emsg_itm_id;
	long emsg_id;
	long emsg_mtp_id;
	long emsg_send_ent_id;
	String emsg_rec_ent_ids;
	String emsg_cc_ent_ids;
	String emsg_subject;
	String emsg_content;
	String emsg_attachment;
	Timestamp emsg_target_datetime;
	long emsg_create_ent_id;
	Timestamp emsg_create_timestamp;
	String mtp_type;
	String emsg_cc_email;
	
	public long getEmsg_id() {
		return emsg_id;
	}
	public void setEmsg_id(long emsg_id) {
		this.emsg_id = emsg_id;
	}
	public long getEmsg_mtp_id() {
		return emsg_mtp_id;
	}
	public void setEmsg_mtp_id(long emsg_mtp_id) {
		this.emsg_mtp_id = emsg_mtp_id;
	}
	public long getEmsg_send_ent_id() {
		return emsg_send_ent_id;
	}
	public void setEmsg_send_ent_id(long emsg_send_ent_id) {
		this.emsg_send_ent_id = emsg_send_ent_id;
	}
	public String getEmsg_rec_ent_ids() {
		return emsg_rec_ent_ids;
	}
	public void setEmsg_rec_ent_ids(String emsg_rec_ent_ids) {
		this.emsg_rec_ent_ids = emsg_rec_ent_ids;
	}
	public String getEmsg_cc_ent_ids() {
		return emsg_cc_ent_ids;
	}
	public void setEmsg_cc_ent_ids(String emsg_cc_ent_ids) {
		this.emsg_cc_ent_ids = emsg_cc_ent_ids;
	}
	public String getEmsg_subject() {
		return emsg_subject;
	}
	public void setEmsg_subject(String emsg_subject) {
		this.emsg_subject = emsg_subject;
	}
	public String getEmsg_content() {
		return emsg_content;
	}
	public void setEmsg_content(String emsg_content) {
		this.emsg_content = emsg_content;
	}
	public String getEmsg_attachment() {
		return emsg_attachment;
	}
	public void setEmsg_attachment(String emsg_attachment) {
		this.emsg_attachment = emsg_attachment;
	}
	public Timestamp getEmsg_target_datetime() {
		return emsg_target_datetime;
	}
	public void setEmsg_target_datetime(Timestamp emsg_target_datetime) {
		this.emsg_target_datetime = emsg_target_datetime;
	}
	public long getEmsg_create_ent_id() {
		return emsg_create_ent_id;
	}
	public void setEmsg_create_ent_id(long emsg_create_ent_id) {
		this.emsg_create_ent_id = emsg_create_ent_id;
	}
	public Timestamp getEmsg_create_timestamp() {
		return emsg_create_timestamp;
	}
	public void setEmsg_create_timestamp(Timestamp emsg_create_timestamp) {
		this.emsg_create_timestamp = emsg_create_timestamp;
	}
	public long getEmsg_itm_id() {
		return emsg_itm_id;
	}
	public void setEmsg_itm_id(long emsg_itm_id) {
		this.emsg_itm_id = emsg_itm_id;
	}
	public String getMtp_type() {
		return mtp_type;
	}
	public void setMtp_type(String mtp_type) {
		this.mtp_type = mtp_type;
	}
	public void setEmsg_cc_email(String emsg_cc_email) {
		this.emsg_cc_email = emsg_cc_email;
	}
	public String getEmsg_cc_email() {
		return emsg_cc_email;
	}
	
}
