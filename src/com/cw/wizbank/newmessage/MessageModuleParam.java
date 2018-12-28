package com.cw.wizbank.newmessage;

import com.cw.wizbank.JsonMod.BaseParam;

public class MessageModuleParam extends BaseParam {
	long mtp_id;
	String mtp_subject;
	String mtp_content;
	boolean mtp_web_message_ind;
	boolean mtp_active_ind;
	
	long msg_id;
	
	String header_img;
	String footer_img;
	String header_img_select;
	String footer_img_select;
	
	public long getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(long msg_id) {
		this.msg_id = msg_id;
	}

	public long getMtp_id() {
		return mtp_id;
	}

	public void setMtp_id(long mtp_id) {
		this.mtp_id = mtp_id;
	}

	public String getMtp_subject() {
		return mtp_subject;
	}

	public void setMtp_subject(String mtp_subject) {
		this.mtp_subject = mtp_subject;
	}

	public String getMtp_content() {
		return mtp_content;
	}

	public void setMtp_content(String mtp_content) {
		this.mtp_content = mtp_content;
	}

	public boolean isMtp_web_message_ind() {
		return mtp_web_message_ind;
	}

	public void setMtp_web_message_ind(boolean mtp_web_message_ind) {
		this.mtp_web_message_ind = mtp_web_message_ind;
	}

	public boolean isMtp_active_ind() {
		return mtp_active_ind;
	}

	public void setMtp_active_ind(boolean mtp_active_ind) {
		this.mtp_active_ind = mtp_active_ind;
	}

	public String getHeader_img() {
		return header_img;
	}

	public void setHeader_img(String header_img) {
		this.header_img = header_img;
	}

	public String getFooter_img() {
		return footer_img;
	}

	public void setFooter_img(String footer_img) {
		this.footer_img = footer_img;
	}

	public String getHeader_img_select() {
		return header_img_select;
	}

	public void setHeader_img_select(String header_img_select) {
		this.header_img_select = header_img_select;
	}

	public String getFooter_img_select() {
		return footer_img_select;
	}

	public void setFooter_img_select(String footer_img_select) {
		this.footer_img_select = footer_img_select;
	}

}
