package com.cwn.wizbank.entity;


/**
 *  专题对应课程
 * <p>Title:UserSpecialItem</p>
 * <p>Description: </p>
 * @author halo.pan
 *
 * @date 2016年4月12日 下午4:30:23
 *
 */
public class UserSpecialItem implements java.io.Serializable {
	private static final long serialVersionUID = -1518935225748027835L;
	private long usi_id; 
	private long ust_utc_id; 
	private long usi_itm_id;
	
	private String title;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public long getUsi_id() {
		return usi_id;
	}
	public void setUsi_id(long usi_id) {
		this.usi_id = usi_id;
	}
	public long getUst_utc_id() {
		return ust_utc_id;
	}
	public void setUst_utc_id(long ust_utc_id) {
		this.ust_utc_id = ust_utc_id;
	}
	public long getUsi_itm_id() {
		return usi_itm_id;
	}
	public void setUsi_itm_id(long usi_itm_id) {
		this.usi_itm_id = usi_itm_id;
	} 
	
}