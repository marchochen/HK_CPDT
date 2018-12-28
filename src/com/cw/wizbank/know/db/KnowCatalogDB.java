package com.cw.wizbank.know.db;

import java.sql.Timestamp;

/**
 * @author DeanChen
 * 
 */
public class KnowCatalogDB {

    private int kca_id;

    private int kca_tcr_id;

    private String kca_code;

    private String kca_title;

    private String kca_type;

    private int kca_parent_kca_id;

    private boolean kca_public_ind;

    private int kca_que_count;

    private String kca_create_usr_id;

    private Timestamp kca_create_timestamp;

    private String kca_update_usr_id;

    private Timestamp kca_update_timestamp;

    public int getKca_id() {
        return kca_id;
    }

    public void setKca_id(int kca_id) {
        this.kca_id = kca_id;
    }

    public int getKca_tcr_id() {
        return kca_tcr_id;
    }

    public void setKca_tcr_id(int kca_tcr_id) {
        this.kca_tcr_id = kca_tcr_id;
    }

    public String getKca_code() {
        return kca_code;
    }

    public void setKca_code(String kca_code) {
        this.kca_code = kca_code;
    }

    public String getKca_title() {
        return kca_title;
    }

    public void setKca_title(String kca_title) {
        this.kca_title = kca_title;
    }

    public String getKca_type() {
        return kca_type;
    }

    public void setKca_type(String kca_type) {
        this.kca_type = kca_type;
    }

    public int getKca_parent_kca_id() {
        return kca_parent_kca_id;
    }

    public void setKca_parent_kca_id(int kca_parent_kca_id) {
        this.kca_parent_kca_id = kca_parent_kca_id;
    }

    public boolean isKca_public_ind() {
        return kca_public_ind;
    }

    public void setKca_public_ind(boolean kca_public_ind) {
        this.kca_public_ind = kca_public_ind;
    }

    public int getKca_que_count() {
        return kca_que_count;
    }

    public void setKca_que_count(int kca_que_count) {
        this.kca_que_count = kca_que_count;
    }

    public String getKca_create_usr_id() {
        return kca_create_usr_id;
    }

    public void setKca_create_usr_id(String kca_create_usr_id) {
        this.kca_create_usr_id = kca_create_usr_id;
    }

    public Timestamp getKca_create_timestamp() {
        return kca_create_timestamp;
    }

    public void setKca_create_timestamp(Timestamp kca_create_timestamp) {
        this.kca_create_timestamp = kca_create_timestamp;
    }

    public String getKca_update_usr_id() {
        return kca_update_usr_id;
    }

    public void setKca_update_usr_id(String kca_update_usr_id) {
        this.kca_update_usr_id = kca_update_usr_id;
    }

    public Timestamp getKca_update_timestamp() {
        return kca_update_timestamp;
    }

    public void setKca_update_timestamp(Timestamp kca_update_timestamp) {
        this.kca_update_timestamp = kca_update_timestamp;
    }

}
