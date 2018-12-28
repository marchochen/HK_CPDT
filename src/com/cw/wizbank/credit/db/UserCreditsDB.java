/**
 * 
 */
package com.cw.wizbank.credit.db;

import java.sql.Timestamp;

/**
 * The mapping of table userCredits in database.
 * 
 * @author DeanChen
 * 
 */
public class UserCreditsDB {

    public int uct_ent_id;

	public float uct_total;

    public Timestamp uct_update_timestmp;

    public int uct_zd_total;

    public int getUct_ent_id() {
        return uct_ent_id;
    }

    public void setUct_ent_id(int uct_ent_id) {
        this.uct_ent_id = uct_ent_id;
    }

    public float getUct_total() {
		return uct_total;
	}

	public void setUct_total(float uctTotal) {
		uct_total = uctTotal;
	}

	public Timestamp getUct_update_timestmp() {
        return uct_update_timestmp;
    }

    public void setUct_update_timestmp(Timestamp uct_update_timestmp) {
        this.uct_update_timestmp = uct_update_timestmp;
    }

    public int getUct_zd_total() {
        return uct_zd_total;
    }

    public void setUct_zd_total(int uct_zd_total) {
        this.uct_zd_total = uct_zd_total;
    }

}
