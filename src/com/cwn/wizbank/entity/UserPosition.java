package com.cwn.wizbank.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>
 * Title:UserPosition
 * </p>
 * <p>
 * Description: 岗位
 * </p>
 * 
 * @author halo.pan
 *
 * @date 2016年3月25日 下午3:00:55
 *
 */
public class UserPosition implements java.io.Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private long upt_id;
	private long upc_id;
	private long upm_id;
	private String upt_code;
	private String upt_title;
	private String upt_desc;
	private long upt_tcr_id;
	private long pfs_update_usr_id;
	private Timestamp pfs_update_time;
	private String upc_title;
	private String upm_img;
	private long upm_seq_no;
	private long upm_status;
	private long upt_upc_id;
    private List<UserPositionLrnItem> items;
    private String abs_img;
    
    public String getAbs_img() {
    	return abs_img;
    }
    
    public void setAbs_img(String abs_img) {
    	this.abs_img = abs_img;
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

	public long getUpm_id() {
		return upm_id;
	}

	public void setUpm_id(long upm_id) {
		this.upm_id = upm_id;
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

	public String getUpt_desc() {
		return upt_desc;
	}

	public void setUpt_desc(String upt_desc) {
		this.upt_desc = upt_desc;
	}

	public long getUpt_tcr_id() {
		return upt_tcr_id;
	}

	public void setUpt_tcr_id(long upt_tcr_id) {
		this.upt_tcr_id = upt_tcr_id;
	}

	public long getPfs_update_usr_id() {
		return pfs_update_usr_id;
	}

	public void setPfs_update_usr_id(long pfs_update_usr_id) {
		this.pfs_update_usr_id = pfs_update_usr_id;
	}

	public Timestamp getPfs_update_time() {
		return pfs_update_time;
	}

	public void setPfs_update_time(Timestamp pfs_update_time) {
		this.pfs_update_time = pfs_update_time;
	}

	public String getUpc_title() {
		return upc_title;
	}

	public void setUpc_title(String upc_title) {
		this.upc_title = upc_title;
	}

	public long getUpm_seq_no() {
		return upm_seq_no;
	}

	public void setUpm_seq_no(long upm_seq_no) {
		this.upm_seq_no = upm_seq_no;
	}

	public long getUpm_status() {
		return upm_status;
	}

	public void setUpm_status(long upm_status) {
		this.upm_status = upm_status;
	}

	public long getUpt_upc_id() {
		return upt_upc_id;
	}

	public void setUpt_upc_id(long upt_upc_id) {
		this.upt_upc_id = upt_upc_id;
	}

	public List<UserPositionLrnItem> getItems() {
		return items;
	}

	public void setItems(List<UserPositionLrnItem> items) {
		this.items = items;
	}

	public String getUpm_img() {
		return upm_img;
	}

	public void setUpm_img(String upm_img) {
		this.upm_img = upm_img;
	}

}