/*
 * Created on 2004-9-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.cw.wizbank.db;

import java.sql.Timestamp;

import com.cw.wizbank.util.cwUtils;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbTrainOfficer {
	private long tco_tcr_id;
	private long tco_usr_ent_id;
	private String tco_rol_ext_id;
	private Timestamp tco_create_timestamp;
	private String tco_create_usr_id;
	private String ent_display_bil;
	
	
	public DbTrainOfficer(long id1,long id2){
		if(id1 == 0 || id2 == 0){
			throw new RuntimeException("tcTrainingCenterOfficer:primary key cannot be null");
		}
		tco_tcr_id = id1;
		tco_usr_ent_id = id2;
	}
	
	public String obj2SimpleXml(){
		StringBuffer sb = new StringBuffer(64);
		sb.append("<entity id=\"").append(getTco_usr_ent_id()).append("\">")
		  .append(cwUtils.esc4XML(getEnt_display_bil()))		  .append("</entity>");
		return sb.toString();
	}
	
	public String getEnt_display_bil() {
		return ent_display_bil;
	}


	public Timestamp getTco_create_timestamp() {
		return tco_create_timestamp;
	}


	public String getTco_create_usr_id() {
		return tco_create_usr_id;
	}


	public String getTco_rol_ext_id() {
		return tco_rol_ext_id;
	}

	
	public long getTco_tcr_id() {
		return tco_tcr_id;
	}


	public long getTco_usr_ent_id() {
		return tco_usr_ent_id;
	}


	public void setEnt_display_bil(String string) {
		ent_display_bil = string;
	}


	public void setTco_create_timestamp(Timestamp timestamp) {
		tco_create_timestamp = timestamp;
	}


	public void setTco_create_usr_id(String string) {
		tco_create_usr_id = string;
	}

	public void setTco_rol_ext_id(String string) {
		tco_rol_ext_id = string;
	}

}
