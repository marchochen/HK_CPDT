package com.cw.wizbank.JsonMod.studyGroup.bean;

import java.sql.Timestamp;
import java.util.Vector;

public class StudyGroupBean {
	private long sgp_id;
	private String sgp_title;
	private String sgp_title_noescape;
	private int sgm_member_total;
	private int sgr_topic_total;
	private int sgr_res_total;
	private String sgp_desc;
	private String sgp_desc_noescape;
	private Timestamp sgp_upd_timestamp;
	private long tcr_id;
	private String tcr_title;
	private int sgp_public_type;
	private int sgp_send_email_ind;
	private boolean can_view;
	//for home page of learner
	private String sgp_type;
	private long sgp_tcr_id;
	
	private Vector mgtVc;
	
	
	public Vector getMgtVc() {
		return mgtVc;
	}
	public void setMgtVc(Vector mgtVc) {
		this.mgtVc = mgtVc;
	}
	public String getTcr_title() {
		return tcr_title;
	}
	public void setTcr_title(String tcr_title) {
		this.tcr_title = tcr_title;
	}
	public int getSgm_member_total() {
		return sgm_member_total;
	}
	public void setSgm_member_total(int sgm_member_total) {
		this.sgm_member_total = sgm_member_total;
	}
	public int getSgr_res_total() {
		return sgr_res_total;
	}
	public void setSgr_res_total(int sgr_res_total) {
		this.sgr_res_total = sgr_res_total;
	}
	public int getSgr_topic_total() {
		return sgr_topic_total;
	}
	public void setSgr_topic_total(int sgr_topic_total) {
		this.sgr_topic_total = sgr_topic_total;
	}
	public long getSgp_id() {
		return sgp_id;
	}
	public void setSgp_id(long sgp_id) {
		this.sgp_id = sgp_id;
	}
	public String getSgp_title() {
		return sgp_title;
	}
	public void setSgp_title(String sgp_title) {
		this.sgp_title = sgp_title;
	}
	public String getSgp_desc() {
		return sgp_desc;
	}
	public void setSgp_desc(String sgp_desc) {
		this.sgp_desc = sgp_desc;
	}
	public Timestamp getSgp_upd_timestamp() {
		return sgp_upd_timestamp;
	}
	public void setSgp_upd_timestamp(Timestamp sgp_upd_timestamp) {
		this.sgp_upd_timestamp = sgp_upd_timestamp;
	}

	public long getTcr_id() {
		return tcr_id;
	}
	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}
	public int getSgp_public_type() {
		return sgp_public_type;
	}
	public void setSgp_public_type(int sgp_public_type) {
		this.sgp_public_type = sgp_public_type;
	}
	public boolean isCan_view() {
		return can_view;
	}
	public void setCan_view(boolean can_view) {
		this.can_view = can_view;
	}
	public String getSgp_type() {
		return sgp_type;
	}
	public void setSgp_type(String sgp_type) {
		this.sgp_type = sgp_type;
	}
	public long getSgp_tcr_id() {
		return sgp_tcr_id;
	}
	public void setSgp_tcr_id(long sgp_tcr_id) {
		this.sgp_tcr_id = sgp_tcr_id;
	}
	public String getSgp_title_noescape() {
		return sgp_title_noescape;
	}
	public void setSgp_title_noescape(String sgp_title_noescape) {
		this.sgp_title_noescape = sgp_title_noescape;
	}
	public String getSgp_desc_noescape() {
		return sgp_desc_noescape;
	}
	public void setSgp_desc_noescape(String sgp_desc_noescape) {
		this.sgp_desc_noescape = sgp_desc_noescape;
	}
	public int getSgp_send_email_ind() {
		return sgp_send_email_ind;
	}
	public void setSgp_send_email_ind(int sgp_send_email_ind) {
		this.sgp_send_email_ind = sgp_send_email_ind;
	}
}
