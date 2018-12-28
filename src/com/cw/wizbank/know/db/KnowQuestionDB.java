package com.cw.wizbank.know.db;

import java.sql.Timestamp;

/**
 * The mapping of table knowQuestion in database.
 * 
 * @author DeanChen
 */
public class KnowQuestionDB {

    private int que_id;

    private int que_tcr_id;

    private int que_kca_id;

    private String que_title;

    private String que_content;// type:ntext

    private Timestamp que_answered_timestamp;

    private boolean que_popular_ind;

    private Timestamp que_popular_timestamp;

    private int que_reward_credits;// the reward credits of question

    private String que_status;

    private int que_create_ent_id;

    private Timestamp que_create_timestamp;

    private int que_update_ent_id;

    private Timestamp que_update_timestamp;

    private int que_parent_kca_id;
    
    private String que_type;

	public int getQue_id() {
        return que_id;
    }

    public void setQue_id(int que_id) {
        this.que_id = que_id;
    }

    public int getQue_tcr_id() {
        return que_tcr_id;
    }

    public void setQue_tcr_id(int que_tcr_id) {
        this.que_tcr_id = que_tcr_id;
    }

    public int getQue_kca_id() {
        return que_kca_id;
    }

    public void setQue_kca_id(int que_kca_id) {
        this.que_kca_id = que_kca_id;
    }

    public String getQue_title() {
        return que_title;
    }

    public void setQue_title(String que_title) {
        this.que_title = que_title;
    }

    public String getQue_content() {

        return que_content;
    }

    public void setQue_content(String que_content) {
        this.que_content = que_content;
    }

    public Timestamp getQue_answered_timestamp() {
        return que_answered_timestamp;
    }

    public void setQue_answered_timestamp(Timestamp que_answered_timestamp) {
        this.que_answered_timestamp = que_answered_timestamp;
    }

    public boolean isQue_popular_ind() {
        return que_popular_ind;
    }

    public void setQue_popular_ind(boolean que_popular_ind) {
        this.que_popular_ind = que_popular_ind;
    }

    public Timestamp getQue_popular_timestamp() {
        return que_popular_timestamp;
    }

    public void setQue_popular_timestamp(Timestamp que_popular_timestamp) {
        this.que_popular_timestamp = que_popular_timestamp;
    }

    public int getQue_reward_credits() {
        return que_reward_credits;
    }

    public void setQue_reward_credits(int que_reward_credits) {
        this.que_reward_credits = que_reward_credits;
    }

    public String getQue_status() {
        return que_status;
    }

    public void setQue_status(String que_status) {
        this.que_status = que_status;
    }

    public int getQue_create_ent_id() {
        return que_create_ent_id;
    }

    public void setQue_create_ent_id(int que_create_ent_id) {
        this.que_create_ent_id = que_create_ent_id;
    }

    public Timestamp getQue_create_timestamp() {
        return que_create_timestamp;
    }

    public void setQue_create_timestamp(Timestamp que_create_timestamp) {
        this.que_create_timestamp = que_create_timestamp;
    }

    public int getQue_update_ent_id() {
        return que_update_ent_id;
    }

    public void setQue_update_ent_id(int que_update_ent_id) {
        this.que_update_ent_id = que_update_ent_id;
    }

    public Timestamp getQue_update_timestamp() {
        return que_update_timestamp;
    }

    public void setQue_update_timestamp(Timestamp que_update_timestamp) {
        this.que_update_timestamp = que_update_timestamp;
    }

    public int getQue_parent_kca_id() {
        return que_parent_kca_id;
    }

    public void setQue_parent_kca_id(int que_parent_kca_id) {
        this.que_parent_kca_id = que_parent_kca_id;
    }
    
    public String getQue_type() {
		return que_type;
	}

	public void setQue_type(String que_type) {
		this.que_type = que_type;
	}
	
}
