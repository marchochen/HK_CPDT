package com.cwn.wizbank.entity;

import java.util.Date;

public class AcEntityRole implements java.io.Serializable {
	private static final long serialVersionUID = -5768443583845979374L;
	
	Long erl_ent_id;
	
	Long erl_rol_id;
	
	Date erl_creation_timestamp;
	
	Date erl_syn_timestamp;
	
	Date erl_eff_start_datetime;
	
	Date erl_eff_end_datetime;
	
	String rol_ext_id;

	public Long getErl_ent_id() {
		return erl_ent_id;
	}

	public void setErl_ent_id(Long erl_ent_id) {
		this.erl_ent_id = erl_ent_id;
	}

	public Long getErl_rol_id() {
		return erl_rol_id;
	}

	public void setErl_rol_id(Long erl_rol_id) {
		this.erl_rol_id = erl_rol_id;
	}

	public Date getErl_creation_timestamp() {
		return erl_creation_timestamp;
	}

	public void setErl_creation_timestamp(Date erl_creation_timestamp) {
		this.erl_creation_timestamp = erl_creation_timestamp;
	}

	public Date getErl_syn_timestamp() {
		return erl_syn_timestamp;
	}

	public void setErl_syn_timestamp(Date erl_syn_timestamp) {
		this.erl_syn_timestamp = erl_syn_timestamp;
	}

	public Date getErl_eff_start_datetime() {
		return erl_eff_start_datetime;
	}

	public void setErl_eff_start_datetime(Date erl_eff_start_datetime) {
		this.erl_eff_start_datetime = erl_eff_start_datetime;
	}

	public Date getErl_eff_end_datetime() {
		return erl_eff_end_datetime;
	}

	public void setErl_eff_end_datetime(Date erl_eff_end_datetime) {
		this.erl_eff_end_datetime = erl_eff_end_datetime;
	}

	public String getRol_ext_id() {
		return rol_ext_id;
	}

	public void setRol_ext_id(String rol_ext_id) {
		this.rol_ext_id = rol_ext_id;
	}
	
}
