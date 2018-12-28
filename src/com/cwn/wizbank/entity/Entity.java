package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * Entity
 * @author lance
 */
public class Entity implements java.io.Serializable {
	private static final long serialVersionUID = -682152617913319999L;
	
	public Long ent_id;
	
	public String ent_type;
	
	public Date ent_upd_date;
	
	public Date ent_syn_date;
	
	public String ent_ste_uid;
	
	public Long ent_syn_ind;
	
	public Long ent_delete_usr_id;
	
	public Date ent_delete_timestamp;

	public Long getEnt_id() {
		return ent_id;
	}

	public void setEnt_id(Long ent_id) {
		this.ent_id = ent_id;
	}

	public String getEnt_type() {
		return ent_type;
	}

	public void setEnt_type(String ent_type) {
		this.ent_type = ent_type;
	}

	public Date getEnt_upd_date() {
		return ent_upd_date;
	}

	public void setEnt_upd_date(Date ent_upd_date) {
		this.ent_upd_date = ent_upd_date;
	}

	public Date getEnt_syn_date() {
		return ent_syn_date;
	}

	public void setEnt_syn_date(Date ent_syn_date) {
		this.ent_syn_date = ent_syn_date;
	}

	public String getEnt_ste_uid() {
		return ent_ste_uid;
	}

	public void setEnt_ste_uid(String ent_ste_uid) {
		this.ent_ste_uid = ent_ste_uid;
	}

	public Long getEnt_syn_ind() {
		return ent_syn_ind;
	}

	public void setEnt_syn_ind(Long ent_syn_ind) {
		this.ent_syn_ind = ent_syn_ind;
	}

	public Long getEnt_delete_usr_id() {
		return ent_delete_usr_id;
	}

	public void setEnt_delete_usr_id(Long ent_delete_usr_id) {
		this.ent_delete_usr_id = ent_delete_usr_id;
	}

	public Date getEnt_delete_timestamp() {
		return ent_delete_timestamp;
	}

	public void setEnt_delete_timestamp(Date ent_delete_timestamp) {
		this.ent_delete_timestamp = ent_delete_timestamp;
	}
	
	
}
