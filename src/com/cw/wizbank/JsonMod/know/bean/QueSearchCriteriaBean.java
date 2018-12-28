package com.cw.wizbank.JsonMod.know.bean;

/**
 * @author DeanChen
 * 
 */
public class QueSearchCriteriaBean {
    
    private String srh_key;

    private String srh_key_type;

    private long[] srh_catalog_id;

    private String[] srh_que_type;

    private String srh_que_start_period;

    private String srh_tab_que_type;
    
    private boolean srh_simple_srh_ind;

    public String getSrh_key() {
    	if (srh_key != null) {
    		srh_key = srh_key.toLowerCase();
    	}
    	
        return srh_key;
    }

    public void setSrh_key(String srh_key) {
        this.srh_key = srh_key;
    }

    public String getSrh_key_type() {
        return srh_key_type;
    }

    public void setSrh_key_type(String srh_key_type) {
        this.srh_key_type = srh_key_type;
    }

    public long[] getSrh_catalog_id() {
        return srh_catalog_id;
    }

    public void setSrh_catalog_id(long[] srh_catalog_id) {
        this.srh_catalog_id = srh_catalog_id;
    }

    public String[] getSrh_que_type() {
        return srh_que_type;
    }

    public void setSrh_que_type(String[] srh_que_type) {
        this.srh_que_type = srh_que_type;
    }

    public String getSrh_que_start_period() {
        return srh_que_start_period;
    }

    public void setSrh_que_start_period(String srh_que_start_period) {
        this.srh_que_start_period = srh_que_start_period;
    }

    public String getSrh_tab_que_type() {
        return srh_tab_que_type;
    }

    public void setSrh_tab_que_type(String srh_tab_que_type) {
        this.srh_tab_que_type = srh_tab_que_type;
    }

    public boolean isSrh_simple_srh_ind() {
        return srh_simple_srh_ind;
    }

    public void setSrh_simple_srh_ind(boolean srh_simple_srh_ind) {
        this.srh_simple_srh_ind = srh_simple_srh_ind;
    }

}
