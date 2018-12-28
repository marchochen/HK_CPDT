package com.cwn.wizbank.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Creating to 2016-05-27 12:12
 * @author mt-201
 *
 */
public class LiveRecords implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 观看人ID
	 */
	private String lr_usr_id;
	/**
	 * 直播ID
	 */
	private long lr_live_id;
	/**
	 * 记录创建时间
	 */
	private Date lr_create_time;
	/**
	 * 状态
	 */
	private int lr_status;
	
	private RegUser regUser;
	
	public String getLr_usr_id() {
		return lr_usr_id;
	}
	
	public void setLr_usr_id(String lr_usr_id) {
		this.lr_usr_id = lr_usr_id;
	}
	
	public long getLr_live_id() {
		return lr_live_id;
	}
	
	public void setLr_live_id(long lr_live_id) {
		this.lr_live_id = lr_live_id;
	}
	
	public Date getLr_create_time() {
		return lr_create_time;
	}
	
	public void setLr_create_time(Date lr_create_time) {
		this.lr_create_time = lr_create_time;
	}

	public int getLr_status() {
		return lr_status;
	}

	public void setLr_status(int lr_status) {
		this.lr_status = lr_status;
	}

	public RegUser getRegUser() {
		return regUser;
	}

	public void setRegUser(RegUser regUser) {
		this.regUser = regUser;
	}

	
}
