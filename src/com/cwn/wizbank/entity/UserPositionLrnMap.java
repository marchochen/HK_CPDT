package com.cwn.wizbank.entity;

import java.sql.Timestamp;

/**
 * <p>
 * Title:UserPositionLrnMap
 * </p>
 * <p>
 * Description: 岗位发展序列
 * </p>
 * 
 * @author halo.pan
 *
 * @date 2016年3月25日 下午3:01:07
 *
 */
public class UserPositionLrnMap implements java.io.Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private long upm_id;
	private long upm_upt_id;
	private long upm_seq_no;
	private String upm_img;
	private long upm_create_usr_id;
	private long upm_update_usr_id;
	private Timestamp upm_create_time;
	private Timestamp upm_update_time;
	private long upm_tcr_id;
	private long upm_status;
	
	private long upt_id;
	private long upc_id;
	private String upt_code;
	private String upt_desc;
	private String upt_title;
	private String upc_title;
	private String abs_img;
	public String getUpt_desc() {
		return upt_desc;
	}

	public void setUpt_desc(String upt_desc) {
		this.upt_desc = upt_desc;
	}

	public long getUpt_id() {
		return upt_id;
	}

	public void setUpt_id(long upt_id) {
		this.upt_id = upt_id;
	}

	public long getUpc_id() {
		return upc_id;
	}

	public void setUpc_id(long upc_id) {
		this.upc_id = upc_id;
	}

	public String getUpt_code() {
		return upt_code;
	}

	public void setUpt_code(String upt_code) {
		this.upt_code = upt_code;
	}

	public String getUpt_title() {
		return upt_title;
	}

	public void setUpt_title(String upt_title) {
		this.upt_title = upt_title;
	}

	public String getUpc_title() {
		return upc_title;
	}

	public void setUpc_title(String upc_title) {
		this.upc_title = upc_title;
	}

	public long getUpm_id() {
		return upm_id;
	}

	public void setUpm_id(long upm_id) {
		this.upm_id = upm_id;
	}


	public long getUpm_upt_id() {
		return upm_upt_id;
	}

	public void setUpm_upt_id(long upm_upt_id) {
		this.upm_upt_id = upm_upt_id;
	}

	public long getUpm_seq_no() {
		return upm_seq_no;
	}

	public void setUpm_seq_no(long upm_seq_no) {
		this.upm_seq_no = upm_seq_no;
	}

	public String getUpm_img() {
		return upm_img;
	}

	public void setUpm_img(String upm_img) {
		this.upm_img = upm_img;
	}

	public long getUpm_create_usr_id() {
		return upm_create_usr_id;
	}

	public void setUpm_create_usr_id(long upm_create_usr_id) {
		this.upm_create_usr_id = upm_create_usr_id;
	}

	public long getUpm_update_usr_id() {
		return upm_update_usr_id;
	}

	public void setUpm_update_usr_id(long upm_update_usr_id) {
		this.upm_update_usr_id = upm_update_usr_id;
	}

	public Timestamp getUpm_create_time() {
		return upm_create_time;
	}

	public void setUpm_create_time(Timestamp upm_create_time) {
		this.upm_create_time = upm_create_time;
	}

	public Timestamp getUpm_update_time() {
		return upm_update_time;
	}

	public void setUpm_update_time(Timestamp upm_update_time) {
		this.upm_update_time = upm_update_time;
	}

	public long getUpm_tcr_id() {
		return upm_tcr_id;
	}

	public void setUpm_tcr_id(long upm_tcr_id) {
		this.upm_tcr_id = upm_tcr_id;
	}

	public long getUpm_status() {
		return upm_status;
	}

	public void setUpm_status(long upm_status) {
		this.upm_status = upm_status;
	}

	public String getAbs_img() {
		return abs_img;
	}

	public void setAbs_img(String abs_img) {
		this.abs_img = abs_img;
	}

}