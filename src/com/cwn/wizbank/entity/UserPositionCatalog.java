package com.cwn.wizbank.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * Title:UserPositionCatalog
 * </p>
 * <p>
 * Description: 岗位分类
 * </p>
 * 
 * @author halo.pan
 *
 * @date 2016年3月25日 下午3:00:44
 *
 */
public class UserPositionCatalog implements java.io.Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private long upt_id;
	private String upt_title;
	private String upt_code;
	private long upc_id;
	private String upc_title;
	private String upc_desc;
	private int upc_status;
	private long upc_tcr_id;
	private long upc_create_user_id;
	private Timestamp upc_create_datetime;
	private long upc_update_user_id;
	private Timestamp upc_update_datetime;
	private List<UserPosition> items;
	private int num;
	public long getUpt_id() {
		return upt_id;
	}

	public void setUpt_id(long upt_id) {
		this.upt_id = upt_id;
	}

	public String getUpt_title() {
		return upt_title;
	}

	public void setUpt_title(String upt_title) {
		this.upt_title = upt_title;
	}

	public String getUpt_code() {
		return upt_code;
	}

	public void setUpt_code(String upt_code) {
		this.upt_code = upt_code;
	}

	public long getUpc_id() {
		return upc_id;
	}

	public void setUpc_id(long upc_id) {
		this.upc_id = upc_id;
	}

	public String getUpc_title() {
		return upc_title;
	}

	public void setUpc_title(String upc_title) {
		this.upc_title = upc_title;
	}

	public String getUpc_desc() {
		return upc_desc;
	}

	public void setUpc_desc(String upc_desc) {
		this.upc_desc = upc_desc;
	}


	public int getUpc_status() {
		return upc_status;
	}

	public void setUpc_status(int upc_status) {
		this.upc_status = upc_status;
	}

	public long getUpc_tcr_id() {
		return upc_tcr_id;
	}

	public void setUpc_tcr_id(long upc_tcr_id) {
		this.upc_tcr_id = upc_tcr_id;
	}

	public long getUpc_create_user_id() {
		return upc_create_user_id;
	}

	public void setUpc_create_user_id(long upc_create_user_id) {
		this.upc_create_user_id = upc_create_user_id;
	}

	public Timestamp getUpc_create_datetime() {
		return upc_create_datetime;
	}

	public void setUpc_create_datetime(Timestamp upc_create_datetime) {
		this.upc_create_datetime = upc_create_datetime;
	}

	public long getUpc_update_user_id() {
		return upc_update_user_id;
	}

	public void setUpc_update_user_id(long upc_update_user_id) {
		this.upc_update_user_id = upc_update_user_id;
	}

	public Timestamp getUpc_update_datetime() {
		return upc_update_datetime;
	}

	public void setUpc_update_datetime(Timestamp upc_update_datetime) {
		this.upc_update_datetime = upc_update_datetime;
	}

	public List<UserPosition> getItems() {
		return items;
	}

	public void setItems(List<UserPosition> items) {
		this.items = items;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}