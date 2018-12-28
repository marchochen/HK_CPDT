package com.cw.wizbank.JsonMod.Course.bean;

import java.sql.Timestamp;

/**
 * 培训中心
 * @author kimyu
 */
public class TrainingCenterBean {
	private long tcr_id; 					// 培训中心ID
//	private String tcr_code; 				// 培训中心编号
	private String tcr_title; 				// 培训中心名称
//	private long tcr_ste_ent_id;			// 培训中心所属的机构ID
//	private String tcr_status; 				// 培训中心状态(可选值：OK、DELETE)
//	private Timestamp tcr_create_timestamp; // 创建时间
//	private String tcr_create_usr_id; 		// 创建者
//	private String tcr_update_usr_id; 		// 更新者
//	private Timestamp tcr_update_timestamp; // 更新时间
//	private long tcr_parent_tcr_id; 		// 培训中心所属的上层结点ID

	public long getTcr_id() {
		return tcr_id;
	}

	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}

//	public String getTcr_code() {
//		return tcr_code;
//	}
//
//	public void setTcr_code(String tcr_code) {
//		this.tcr_code = tcr_code;
//	}

	public String getTcr_title() {
		return tcr_title;
	}

	public void setTcr_title(String tcr_title) {
		this.tcr_title = tcr_title;
	}

//	public long getTcr_ste_ent_id() {
//		return tcr_ste_ent_id;
//	}
//
//	public void setTcr_ste_ent_id(long tcr_ste_ent_id) {
//		this.tcr_ste_ent_id = tcr_ste_ent_id;
//	}
//
//	public String getTcr_status() {
//		return tcr_status;
//	}
//
//	public void setTcr_status(String tcr_status) {
//		this.tcr_status = tcr_status;
//	}
//
//	public Timestamp getTcr_create_timestamp() {
//		return tcr_create_timestamp;
//	}
//
//	public void setTcr_create_timestamp(Timestamp tcr_create_timestamp) {
//		this.tcr_create_timestamp = tcr_create_timestamp;
//	}
//
//	public String getTcr_create_usr_id() {
//		return tcr_create_usr_id;
//	}
//
//	public void setTcr_create_usr_id(String tcr_create_usr_id) {
//		this.tcr_create_usr_id = tcr_create_usr_id;
//	}
//
//	public String getTcr_update_usr_id() {
//		return tcr_update_usr_id;
//	}
//
//	public void setTcr_update_usr_id(String tcr_update_usr_id) {
//		this.tcr_update_usr_id = tcr_update_usr_id;
//	}
//
//	public Timestamp getTcr_update_timestamp() {
//		return tcr_update_timestamp;
//	}
//
//	public void setTcr_update_timestamp(Timestamp tcr_update_timestamp) {
//		this.tcr_update_timestamp = tcr_update_timestamp;
//	}
//
//	public long getTcr_parent_tcr_id() {
//		return tcr_parent_tcr_id;
//	}
//
//	public void setTcr_parent_tcr_id(long tcr_parent_tcr_id) {
//		this.tcr_parent_tcr_id = tcr_parent_tcr_id;
//	}
	
}
