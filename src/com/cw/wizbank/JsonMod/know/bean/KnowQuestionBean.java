package com.cw.wizbank.JsonMod.know.bean;

import java.sql.Timestamp;

/**
 * @author DeanChen
 * 
 */
public class KnowQuestionBean {

    private int lst_order;

    private long que_id;

    private String que_title;
    
    private String que_type;

    private boolean que_popular_ind;

    private Timestamp que_create_timestamp;
    
    private Timestamp que_update_timestamp;

    private int ans_count;

    // add catalog
    private int kca_id;

    private String kca_title;

    // for search
    private String que_content;

    private int que_create_ent_id;

    private String usr_nickname;
    
    private long usr_ent_id;

    private int ans_vote_total;

    private int ans_vote_for;

    private int ans_vote_for_rate;

    private int ans_vote_down;

    private int ans_vote_down_rate;
    
    // for the detail of question
    private String usr_photo;

    private Timestamp que_answered_timestamp;

    // for modify catalog relation of question
    private Timestamp kcr_create_timestamp;

    public int getAns_count() {
        return ans_count;
    }

    public void setAns_count(int ans_count) {
        this.ans_count = ans_count;
    }

    public int getLst_order() {
        return lst_order;
    }

    public void setLst_order(int lst_order) {
        this.lst_order = lst_order;
    }

    public String getQue_type() {
		return que_type;
	}

	public void setQue_type(String que_type) {
		this.que_type = que_type;
	}

    public Timestamp getQue_create_timestamp() {
        return que_create_timestamp;
    }

    public void setQue_create_timestamp(Timestamp que_create_timestamp) {
        this.que_create_timestamp = que_create_timestamp;
    }
    
    public Timestamp getQue_update_timestamp() {
        return que_update_timestamp;
    }

    public void setQue_update_timestamp(Timestamp que_update_timestamp) {
        this.que_update_timestamp = que_update_timestamp;
    }

    public long getQue_id() {
        return que_id;
    }

    public void setQue_id(long que_id) {
        this.que_id = que_id;
    }

    public String getQue_title() {
        return que_title;
    }

    public void setQue_title(String que_title) {
        this.que_title = que_title;
    }

    public int getKca_id() {
        return kca_id;
    }

    public void setKca_id(int kca_id) {
        this.kca_id = kca_id;
    }

    public String getKca_title() {
        return kca_title;
    }

    public void setKca_title(String kca_title) {
        this.kca_title = kca_title;
    }

    public String getQue_content() {
        return que_content;
    }

    public void setQue_content(String que_content) {
        this.que_content = que_content;
    }

    public int getQue_create_ent_id() {
        return que_create_ent_id;
    }

    public void setQue_create_ent_id(int que_create_ent_id) {
        this.que_create_ent_id = que_create_ent_id;
    }

    public String getUsr_nickname() {
        return usr_nickname;
    }

    public void setUsr_nickname(String usr_nickname) {
        this.usr_nickname = usr_nickname;
    }
    
    public long getUsr_ent_id() {
        return usr_ent_id;
    }

    public void setUsr_ent_id(long usr_ent_id) {
        this.usr_ent_id = usr_ent_id;
    }
    
    public int getAns_vote_total() {
        return ans_vote_total;
    }

    public void setAns_vote_total(int ans_vote_total) {
        this.ans_vote_total = ans_vote_total;
    }

    public int getAns_vote_for() {
        return ans_vote_for;
    }

    public void setAns_vote_for(int ans_vote_for) {
        this.ans_vote_for = ans_vote_for;
    }

    public int getAns_vote_down() {
        return ans_vote_down;
    }

    public void setAns_vote_down(int ans_vote_down) {
        this.ans_vote_down = ans_vote_down;
    }

    public String getUsr_photo() {
        return usr_photo;
    }

    public void setUsr_photo(String usr_photo) {
        this.usr_photo = usr_photo;
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

    public int getAns_vote_for_rate() {
        return ans_vote_for_rate;
    }

    public void setAns_vote_for_rate(int ans_vote_for_rate) {
        this.ans_vote_for_rate = ans_vote_for_rate;
    }

    public int getAns_vote_down_rate() {
        return ans_vote_down_rate;
    }

    public void setAns_vote_down_rate(int ans_vote_down_rate) {
        this.ans_vote_down_rate = ans_vote_down_rate;
    }

    public Timestamp getKcr_create_timestamp() {
        return kcr_create_timestamp;
    }

    public void setKcr_create_timestamp(Timestamp kcr_create_timestamp) {
        this.kcr_create_timestamp = kcr_create_timestamp;
    }

}
