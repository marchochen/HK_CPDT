/**
 * 
 */
package com.cw.wizbank.credit.db;

import java.sql.Timestamp;

/**
 * @author DeanChen
 * 
 */
public class UserCreditsDetailLogDB {

    private int ucl_usr_ent_id;

    private int ucl_bpt_id;

    private String ucl_relation_type;

    private long ucl_source_id;

	private float ucl_point;

	private Timestamp ucl_create_timestamp;

    private String ucl_create_usr_id;
    
    private long ucl_app_id;

    public int getUcl_usr_ent_id() {
        return ucl_usr_ent_id;
    }

    public void setUcl_usr_ent_id(int ucl_usr_ent_id) {
        this.ucl_usr_ent_id = ucl_usr_ent_id;
    }

    public int getUcl_bpt_id() {
        return ucl_bpt_id;
    }

    public void setUcl_bpt_id(int ucl_bpt_id) {
        this.ucl_bpt_id = ucl_bpt_id;
    }

    public String getUcl_relation_type() {
        return ucl_relation_type;
    }

    public void setUcl_relation_type(String ucl_relation_type) {
        this.ucl_relation_type = ucl_relation_type;
    }

    public long getUcl_source_id() {
        return ucl_source_id;
    }

    public void setUcl_source_id(long ucl_source_id) {
        this.ucl_source_id = ucl_source_id;
    }

    
    public float getUcl_point() {
		return ucl_point;
	}

	public void setUcl_point(float uclPoint) {
		ucl_point = uclPoint;
	}
	
    public Timestamp getUcl_create_timestamp() {
        return ucl_create_timestamp;
    }

    public void setUcl_create_timestamp(Timestamp ucl_create_timestamp) {
        this.ucl_create_timestamp = ucl_create_timestamp;
    }

    public String getUcl_create_usr_id() {
        return ucl_create_usr_id;
    }

    public void setUcl_create_usr_id(String ucl_create_usr_id) {
        this.ucl_create_usr_id = ucl_create_usr_id;
    }

	public long getUcl_app_id() {
		return ucl_app_id;
	}

	public void setUcl_app_id(long ucl_app_id) {
		this.ucl_app_id = ucl_app_id;
	}

}
