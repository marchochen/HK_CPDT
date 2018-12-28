package com.cw.wizbank.JsonMod.Course.bean;

import java.sql.Timestamp;

public class CosCommentBean {
	private long itm_id;
	private long total_score;
	private long total_count;
	private float avg_score;
	private boolean hasCommented;
	private long itm_cfc_id;
	private long ict_id;
	private long ict_tkh_id;
	private long ict_itm_id;
	private long ict_ent_id;
	private long ict_score;
	private String ict_comment;
	private Timestamp ict_create_timestamp;
	
	private String Ict_ent_name;		// 评分学员的姓名
	private String type;
	
	
	public long getItm_cfc_id() {
		return itm_cfc_id;
	}
	public void setItm_cfc_id(long itm_cfc_id) {
		this.itm_cfc_id = itm_cfc_id;
	}
	public float getAvg_score() {
		return avg_score;
	}
	public void setAvg_score(float avg_score) {
		this.avg_score = avg_score;
	}
	public boolean isHasCommented() {
		return hasCommented;
	}
	public void setHasCommented(boolean hasCommented) {
		this.hasCommented = hasCommented;
	}
	public long getItm_id() {
		return itm_id;
	}
	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}
	public long getTotal_count() {
		return total_count;
	}
	public void setTotal_count(long total_count) {
		this.total_count = total_count;
	}
	public long getTotal_score() {
		return total_score;
	}
	public void setTotal_score(long total_score) {
		this.total_score = total_score;
	}
	public long getIct_id() {
		return ict_id;
	}
	public long getIct_tkh_id() {
		return ict_tkh_id;
	}
	public long getIct_itm_id() {
		return ict_itm_id;
	}
	public long getIct_ent_id() {
		return ict_ent_id;
	}
	public long getIct_score() {
		return ict_score;
	}
	public String getIct_comment() {
		return ict_comment;
	}
	public Timestamp getIct_create_timestamp() {
		return ict_create_timestamp;
	}
	public void setIct_id(long ict_id) {
		this.ict_id = ict_id;
	}
	public void setIct_tkh_id(long ict_tkh_id) {
		this.ict_tkh_id = ict_tkh_id;
	}
	public void setIct_itm_id(long ict_itm_id) {
		this.ict_itm_id = ict_itm_id;
	}
	public void setIct_ent_id(long ict_ent_id) {
		this.ict_ent_id = ict_ent_id;
	}
	public void setIct_score(long ict_score) {
		this.ict_score = ict_score;
	}
	public void setIct_comment(String ict_comment) {
		this.ict_comment = ict_comment;
	}
	public void setIct_create_timestamp(Timestamp ict_create_timestamp) {
		this.ict_create_timestamp = ict_create_timestamp;
	}
	public String getIct_ent_name() {
		return Ict_ent_name;
	}
	public void setIct_ent_name(String ict_ent_name) {
		Ict_ent_name = ict_ent_name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
