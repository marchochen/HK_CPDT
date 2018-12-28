package com.cwn.wizbank.entity;



public class UserPositionRelation implements java.io.Serializable {

	 /**
	 * 
	 */
	private static final long serialVersionUID = -2174914433278594790L;
	private long upr_upt_id;
	 private long upr_usr_ent_id;
	public long getUpr_upt_id() {
		return upr_upt_id;
	}
	public void setUpr_upt_id(long upr_upt_id) {
		this.upr_upt_id = upr_upt_id;
	}
	public long getUpr_usr_ent_id() {
		return upr_usr_ent_id;
	}
	public void setUpr_usr_ent_id(long upr_usr_ent_id) {
		this.upr_usr_ent_id = upr_usr_ent_id;
	}
		
}