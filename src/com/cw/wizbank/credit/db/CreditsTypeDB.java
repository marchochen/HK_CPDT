/**
 * 
 */
package com.cw.wizbank.credit.db;

import java.sql.Timestamp;

/**
 * @author DeanChen
 * 
 */
public class CreditsTypeDB {

    private int cty_id;

    private String cty_code;

    private String cty_title;

    private boolean cty_deduction_ind;

    private boolean cty_manual_ind;

    private boolean cty_deleted_ind;

    private boolean cty_relation_total_ind;

    private String cty_relation_type;

    private boolean cty_default_credits_ind;

    private float cty_default_credits;

    private String cty_create_usr_id;

    private Timestamp cty_create_timestamp;
    
    private String cty_update_usr_id;

    private Timestamp cty_update_timestamp;
    
    private int cty_hit;

    private String cty_period;
    private long cty_tcr_id;
    
    public long getCty_tcr_id() {
        return cty_tcr_id;
    }

    public void setCty_tcr_id(long ctyTcrId) {
        cty_tcr_id = ctyTcrId;
    }

    public int getCty_id() {
        return cty_id;
    }

    public void setCty_id(int cty_id) {
        this.cty_id = cty_id;
    }

    public String getCty_code() {
        return cty_code;
    }

    public void setCty_code(String cty_code) {
        this.cty_code = cty_code;
    }

    public String getCty_title() {
        return cty_title;
    }

    public void setCty_title(String cty_title) {
        this.cty_title = cty_title;
    }

    public boolean isCty_deduction_ind() {
        return cty_deduction_ind;
    }

    public void setCty_deduction_ind(boolean cty_deduction_ind) {
        this.cty_deduction_ind = cty_deduction_ind;
    }

    public boolean isCty_manual_ind() {
        return cty_manual_ind;
    }

    public void setCty_manual_ind(boolean cty_manual_ind) {
        this.cty_manual_ind = cty_manual_ind;
    }

    public boolean isCty_deleted_ind() {
        return cty_deleted_ind;
    }

    public void setCty_deleted_ind(boolean cty_deleted_ind) {
        this.cty_deleted_ind = cty_deleted_ind;
    }

    public boolean isCty_relation_total_ind() {
        return cty_relation_total_ind;
    }

    public void setCty_relation_total_ind(boolean cty_relation_total_ind) {
        this.cty_relation_total_ind = cty_relation_total_ind;
    }

    public String getCty_relation_type() {
        return cty_relation_type;
    }

    public void setCty_relation_type(String cty_relation_type) {
        this.cty_relation_type = cty_relation_type;
    }

    public boolean isCty_default_credits_ind() {
        return cty_default_credits_ind;
    }

    public void setCty_default_credits_ind(boolean cty_default_credits_ind) {
        this.cty_default_credits_ind = cty_default_credits_ind;
    }

    public Float getCty_default_credits() {
        return cty_default_credits;
    }

    public void setCty_default_credits(float cty_default_credits) {
        this.cty_default_credits = cty_default_credits;
    }

    public String getCty_create_usr_id() {
        return cty_create_usr_id;
    }

    public void setCty_create_usr_id(String cty_create_usr_id) {
        this.cty_create_usr_id = cty_create_usr_id;
    }

    public Timestamp getCty_create_timestamp() {
        return cty_create_timestamp;
    }

    public void setCty_create_timestamp(Timestamp cty_create_timestamp) {
        this.cty_create_timestamp = cty_create_timestamp;
    }

	public String getCty_update_usr_id() {
		return cty_update_usr_id;
	}

	public void setCty_update_usr_id(String cty_update_usr_id) {
		this.cty_update_usr_id = cty_update_usr_id;
	}

	public Timestamp getCty_update_timestamp() {
		return cty_update_timestamp;
	}

	public void setCty_update_timestamp(Timestamp cty_update_timestamp) {
		this.cty_update_timestamp = cty_update_timestamp;
	}

	public int getCty_hit() {
		return cty_hit;
	}

	public void setCty_hit(int cty_hit) {
		this.cty_hit = cty_hit;
	}

	public String getCty_period() {
		return cty_period;
	}

	public void setCty_period(String cty_period) {
		this.cty_period = cty_period;
	}
}
