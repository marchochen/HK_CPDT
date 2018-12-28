package com.cwn.wizbank.entity;

import java.sql.Timestamp;

/**
 * <p>
 * Title:UserPositionLrnItem
 * </p>
 * <p>
 * Description: 岗位序列对应课程
 * </p>
 * 
 * @author halo.pan
 *
 * @date 2016年3月25日 下午3:01:02
 *
 */
public class UserPositionLrnItem implements java.io.Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private long upi_id;
	private long upi_upm_id;
	private long upi_itm_id;
    private String title;
	public long getUpi_id() {
		return upi_id;
	}

	public void setUpi_id(long upi_id) {
		this.upi_id = upi_id;
	}

	public long getUpi_upm_id() {
		return upi_upm_id;
	}

	public void setUpi_upm_id(long upi_upm_id) {
		this.upi_upm_id = upi_upm_id;
	}


	public long getUpi_itm_id() {
		return upi_itm_id;
	}

	public void setUpi_itm_id(long upi_itm_id) {
		this.upi_itm_id = upi_itm_id;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}