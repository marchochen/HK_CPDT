package com.cwn.wizbank.entity;

import java.util.Date;


public class TrackingHistory implements java.io.Serializable {
    
    Integer tkh_id;
    
    String tkh_type;
    
    Integer tkh_usr_ent_id;
    
    Integer tkh_cos_res_id;
    
    Date tkh_create_timestamp;

    public Integer getTkh_id() {
        return tkh_id;
    }

    public void setTkh_id(Integer tkh_id) {
        this.tkh_id = tkh_id;
    }

    public String getTkh_type() {
        return tkh_type;
    }

    public void setTkh_type(String tkh_type) {
        this.tkh_type = tkh_type;
    }

    public Integer getTkh_usr_ent_id() {
        return tkh_usr_ent_id;
    }

    public void setTkh_usr_ent_id(Integer tkh_usr_ent_id) {
        this.tkh_usr_ent_id = tkh_usr_ent_id;
    }

    public Integer getTkh_cos_res_id() {
        return tkh_cos_res_id;
    }

    public void setTkh_cos_res_id(Integer tkh_cos_res_id) {
        this.tkh_cos_res_id = tkh_cos_res_id;
    }

    public Date getTkh_create_timestamp() {
        return tkh_create_timestamp;
    }

    public void setTkh_create_timestamp(Date tkh_create_timestamp) {
        this.tkh_create_timestamp = tkh_create_timestamp;
    }

	
}