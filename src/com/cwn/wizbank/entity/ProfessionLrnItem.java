package com.cwn.wizbank.entity;

public  class ProfessionLrnItem implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6804690535902071147L;
	private long psi_id;
	private long psi_pfs_id;
	private String psi_ugr_id;
	private String psi_itm_id;
	
	private String title;
	public long getPsi_id() {
		return psi_id;
	}
	public void setPsi_id(long psi_id) {
		this.psi_id = psi_id;
	}
	public long getPsi_pfs_id() {
		return psi_pfs_id;
	}
	public void setPsi_pfs_id(long psi_pfs_id) {
		this.psi_pfs_id = psi_pfs_id;
	}
	public String getPsi_ugr_id() {
		return psi_ugr_id;
	}
	public void setPsi_ugr_id(String psi_ugr_id) {
		this.psi_ugr_id = psi_ugr_id;
	}
	public String getPsi_itm_id() {
		return psi_itm_id.trim();
	}
	public void setPsi_itm_id(String psi_itm_id) {
		this.psi_itm_id = psi_itm_id.trim();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
}
