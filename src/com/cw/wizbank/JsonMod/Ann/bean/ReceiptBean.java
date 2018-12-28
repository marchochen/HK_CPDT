package com.cw.wizbank.JsonMod.Ann.bean;

import java.sql.Timestamp;

public class ReceiptBean {
	private long rec_id;
	private long rec_msg_id;
	private long rec_ent_id;
	private Timestamp receipt_date;
	private String usr_display_bil;
	private String usg_display_bil;
	private long rec_usg_id;
	private String msg_type;
	
	
	public ReceiptBean(long rec_msg_id, long rec_ent_id, long rec_usg_id, Timestamp receipt_date) {
		super();
		this.rec_msg_id = rec_msg_id;
		this.rec_ent_id = rec_ent_id;
		this.rec_usg_id = rec_usg_id;
		this.receipt_date = receipt_date;
	}
	public String getUsr_display_bil() {
		return usr_display_bil;
	}
	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}
	public String getMsg_type() {
		return msg_type;
	}
	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}
	public long getRec_id() {
		return rec_id;
	}
	public void setRec_id(long rec_id) {
		this.rec_id = rec_id;
	}
	public long getRec_msg_id() {
		return rec_msg_id;
	}
	public void setRec_msg_id(long rec_msg_id) {
		this.rec_msg_id = rec_msg_id;
	}
	public long getRec_ent_id() {
		return rec_ent_id;
	}
	public void setRec_ent_id(long rec_ent_id) {
		this.rec_ent_id = rec_ent_id;
	}
	public Timestamp getReceipt_date() {
		return receipt_date;
	}
	public void setReceipt_date(Timestamp receipt_date) {
		this.receipt_date = receipt_date;
	}
	public long getRec_usg_id() {
		return rec_usg_id;
	}
	public void setRec_usg_id(long rec_usg_id) {
		this.rec_usg_id = rec_usg_id;
	}
	public String getUsg_display_bil() {
		return usg_display_bil;
	}
	public void setUsg_display_bil(String usg_display_bil) {
		this.usg_display_bil = usg_display_bil;
	}
	
}
