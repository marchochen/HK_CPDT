/*
 * Created on 2004-9-22
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
public class DbTrainTargetEntity {
    private long tce_tcr_id;
    private long tce_ent_id;
    private Timestamp tce_create_timestamp;
    private String tce_create_usr_id;
    private String ent_display_bil;
    
    
    
    public String obj2SimpleXml(){
    	StringBuffer sb = new StringBuffer(64);
    	sb.append("<entity id=\"").append(this.getTce_ent_id()).append("\">")
    	  .append(cwUtils.esc4XML(getEnt_display_bil())).append("</entity>");
    	return sb.toString();
    }
    
	public String obj2Xml(){
		   StringBuffer sb = new StringBuffer(64);
		   sb.append("<tct_entity tcr_id=\"").append(this.getTce_tcr_id())
		     .append("\" ent_id=\"").append(this.getTce_ent_id())  
		     .append("\" create_timestamp=\"").append(this.getTce_create_timestamp())
		     .append("\" create_usr_id").append(this.getTce_create_usr_id())
		     .append("\" display_bil=\"").append(cwUtils.esc4XML(getEnt_display_bil())).append("\" />");
		   return sb.toString();
	   }
    
    public DbTrainTargetEntity(long id1,long id2){
    	if(id1 == 0 || id2 == 0){
    		throw new RuntimeException("tcTraingCenterTargetEntity:Primary key cannot be null");
    	}
    	tce_tcr_id = id1;
    	tce_ent_id = id2;
    }

	public Timestamp getTce_create_timestamp() {
		return tce_create_timestamp;
	}


	public String getTce_create_usr_id() {
		return tce_create_usr_id;
	}


	public long getTce_ent_id() {
		return tce_ent_id;
	}


	public long getTce_tcr_id() {
		return tce_tcr_id;
	}


	public void setTce_create_timestamp(Timestamp timestamp) {
		tce_create_timestamp = timestamp;
	}

	public void setTce_create_usr_id(String string) {
		tce_create_usr_id = string;
	}



	public String getEnt_display_bil() {
		return ent_display_bil;
	}

	public void setEnt_display_bil(String string) {
		ent_display_bil = string;
	}

}
