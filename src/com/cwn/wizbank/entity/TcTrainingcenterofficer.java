package com.cwn.wizbank.entity;

import java.util.Date;

public class TcTrainingcenterofficer {

	Long tco_tcr_id	;
	Long tco_usr_ent_id;
	String tco_rol_ext_id;
	Date tco_create_timestamp;
	String tco_create_usr_id;
	int tco_major_ind;
	Date tco_update_timestamp;
	String tco_update_usr_id;
	public Long getTco_tcr_id() {
		return tco_tcr_id;
	}
	public void setTco_tcr_id(Long tcoTcrId) {
		tco_tcr_id = tcoTcrId;
	}
	public Long getTco_usr_ent_id() {
		return tco_usr_ent_id;
	}
	public void setTco_usr_ent_id(Long tcoUsrEntId) {
		tco_usr_ent_id = tcoUsrEntId;
	}
	public String getTco_rol_ext_id() {
		return tco_rol_ext_id;
	}
	public void setTco_rol_ext_id(String tcoRolExtId) {
		tco_rol_ext_id = tcoRolExtId;
	}
	public Date getTco_create_timestamp() {
		return tco_create_timestamp;
	}
	public void setTco_create_timestamp(Date tcoCreateTimestamp) {
		tco_create_timestamp = tcoCreateTimestamp;
	}
	public String getTco_create_usr_id() {
		return tco_create_usr_id;
	}
	public void setTco_create_usr_id(String tcoCreateUsrId) {
		tco_create_usr_id = tcoCreateUsrId;
	}
	public int getTco_major_ind() {
		return tco_major_ind;
	}
	public void setTco_major_ind(int tcoMajorInd) {
		tco_major_ind = tcoMajorInd;
	}
	public Date getTco_update_timestamp() {
		return tco_update_timestamp;
	}
	public void setTco_update_timestamp(Date tcoUpdateTimestamp) {
		tco_update_timestamp = tcoUpdateTimestamp;
	}
	public String getTco_update_usr_id() {
		return tco_update_usr_id;
	}
	public void setTco_update_usr_id(String tcoUpdateUsrId) {
		tco_update_usr_id = tcoUpdateUsrId;
	}
	
}
