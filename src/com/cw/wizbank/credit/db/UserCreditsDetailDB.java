/**
 * 
 */
package com.cw.wizbank.credit.db;

import java.sql.Timestamp;

/**
 * @author DeanChen
 * 
 */
public class UserCreditsDetailDB {

    private int ucd_ent_id;

    private int ucd_cty_id;

    private long ucd_itm_id;

	private float ucd_total;

    private int ucd_hit;

    private int ucd_hit_temp;

    private Timestamp ucd_create_timestamp;

    private String ucd_create_usr_id;

    private Timestamp ucd_update_timestamp;

    private String ucd_update_usr_id;
    
    private long ucd_app_id;
    
    public int getUcd_ent_id() {
        return ucd_ent_id;
    }

    public void setUcd_ent_id(int ucd_ent_id) {
        this.ucd_ent_id = ucd_ent_id;
    }

    public int getUcd_cty_id() {
        return ucd_cty_id;
    }

    public void setUcd_cty_id(int ucd_cty_id) {
        this.ucd_cty_id = ucd_cty_id;
    }

    public long getUcd_itm_id() {
        return ucd_itm_id;
    }

    public void setUcd_itm_id(long ucd_itm_id) {
        this.ucd_itm_id = ucd_itm_id;
    }

    public float getUcd_total() {
		return ucd_total;
	}

	public void setUcd_total(float ucdTotal) {
		ucd_total = ucdTotal;
	}
    
	public int getUcd_hit() {
        return ucd_hit;
    }

    public void setUcd_hit(int ucd_hit) {
        this.ucd_hit = ucd_hit;
    }

    public int getUcd_hit_temp() {
        return ucd_hit_temp;
    }

    public void setUcd_hit_temp(int ucd_hit_temp) {
        this.ucd_hit_temp = ucd_hit_temp;
    }

    public Timestamp getUcd_create_timestamp() {
        return ucd_create_timestamp;
    }

    public void setUcd_create_timestamp(Timestamp ucd_create_timestamp) {
        this.ucd_create_timestamp = ucd_create_timestamp;
    }

    public String getUcd_create_usr_id() {
        return ucd_create_usr_id;
    }

    public void setUcd_create_usr_id(String ucd_create_usr_id) {
        this.ucd_create_usr_id = ucd_create_usr_id;
    }

    public Timestamp getUcd_update_timestamp() {
        return ucd_update_timestamp;
    }

    public void setUcd_update_timestamp(Timestamp ucd_update_timestamp) {
        this.ucd_update_timestamp = ucd_update_timestamp;
    }

    public String getUcd_update_usr_id() {
        return ucd_update_usr_id;
    }

    public void setUcd_update_usr_id(String ucd_update_usr_id) {
        this.ucd_update_usr_id = ucd_update_usr_id;
    }
    
    public long getUcd_app_id() {
        return ucd_app_id;
    }

    public void setUcd_app_id(long ucd_app_id) {
        this.ucd_app_id = ucd_app_id;
    }
    
}
