package com.cw.wizbank.know.db;

import java.sql.Timestamp;

/**
 * @author DeanChen
 * 
 */
public class KnowCatalogRelationDB {
    private int kcr_child_kca_id;

    private int kcr_ancestor_kca_id;

    private String kcr_type;

    private int kcr_order;

    private boolean kcr_parent_ind;

    private String kcr_create_usr_id;

    private Timestamp kcr_create_timestamp;

    public int getKcr_child_kca_id() {
        return kcr_child_kca_id;
    }

    public void setKcr_child_kca_id(int kcr_child_kca_id) {
        this.kcr_child_kca_id = kcr_child_kca_id;
    }

    public int getKcr_ancestor_kca_id() {
        return kcr_ancestor_kca_id;
    }

    public void setKcr_ancestor_kca_id(int kcr_ancestor_kca_id) {
        this.kcr_ancestor_kca_id = kcr_ancestor_kca_id;
    }

    public String getKcr_type() {
        return kcr_type;
    }

    public void setKcr_type(String kcr_type) {
        this.kcr_type = kcr_type;
    }

    public int getKcr_order() {
        return kcr_order;
    }

    public void setKcr_order(int kcr_order) {
        this.kcr_order = kcr_order;
    }

    public boolean isKcr_parent_ind() {
        return kcr_parent_ind;
    }

    public void setKcr_parent_ind(boolean kcr_parent_ind) {
        this.kcr_parent_ind = kcr_parent_ind;
    }

    public String getKcr_create_usr_id() {
        return kcr_create_usr_id;
    }

    public void setKcr_create_usr_id(String kcr_create_usr_id) {
        this.kcr_create_usr_id = kcr_create_usr_id;
    }

    public Timestamp getKcr_create_timestamp() {
        return kcr_create_timestamp;
    }

    public void setKcr_create_timestamp(Timestamp kcr_create_timestamp) {
        this.kcr_create_timestamp = kcr_create_timestamp;
    }

}
