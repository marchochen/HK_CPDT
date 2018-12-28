package com.cw.wizbank.know.db;

import java.sql.Timestamp;

/**
 * @author DeanChen
 * 
 */
public class KnowAnswerDB {

    private int ans_id;

    private int ans_que_id;

    private String ans_content;// ntext
    private String ans_content_search;// ntext
    
    private boolean ans_right_ind;

    private int ans_vote_total;

    private int ans_vote_for;

    private int ans_vote_down;

    private int ans_temp_vote_total;

    private int ans_temp_vote_for;

    private int ans_temp_vote_for_down_diff;

    private String ans_status;

    private int ans_create_ent_id;

    private Timestamp ans_create_timestamp;

    private int ans_update_ent_id;

    private Timestamp ans_update_timestamp;

    private String ans_refer_content;

    public int getAns_id() {
        return ans_id;
    }

    public void setAns_id(int ans_id) {
        this.ans_id = ans_id;
    }

    public int getAns_que_id() {
        return ans_que_id;
    }

    public void setAns_que_id(int ans_que_id) {
        this.ans_que_id = ans_que_id;
    }

    public String getAns_content() {
        return ans_content;
    }

    public void setAns_content(String ans_content) {
        this.ans_content = ans_content;
    }

    public String getAns_content_search() {
		return ans_content_search;
	}

	public void setAns_content_search(String ans_content_search) {
		this.ans_content_search = ans_content_search;
	}

	public boolean isAns_right_ind() {
        return ans_right_ind;
    }

    public void setAns_right_ind(boolean ans_right_ind) {
        this.ans_right_ind = ans_right_ind;
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

    public int getAns_temp_vote_total() {
        return ans_temp_vote_total;
    }

    public void setAns_temp_vote_total(int ans_temp_vote_total) {
        this.ans_temp_vote_total = ans_temp_vote_total;
    }

    public int getAns_temp_vote_for() {
        return ans_temp_vote_for;
    }

    public void setAns_temp_vote_for(int ans_temp_vote_for) {
        this.ans_temp_vote_for = ans_temp_vote_for;
    }

    public int getAns_temp_vote_for_down_diff() {
		return ans_temp_vote_for_down_diff;
	}

	public void setAns_temp_vote_for_down_diff(int ans_temp_vote_for_down_diff) {
		this.ans_temp_vote_for_down_diff = ans_temp_vote_for_down_diff;
	}

	public String getAns_status() {
        return ans_status;
    }

    public void setAns_status(String ans_status) {
        this.ans_status = ans_status;
    }

    public int getAns_create_ent_id() {
        return ans_create_ent_id;
    }

    public void setAns_create_ent_id(int ans_create_ent_id) {
        this.ans_create_ent_id = ans_create_ent_id;
    }

    public Timestamp getAns_create_timestamp() {
        return ans_create_timestamp;
    }

    public void setAns_create_timestamp(Timestamp ans_create_timestamp) {
        this.ans_create_timestamp = ans_create_timestamp;
    }

    public int getAns_update_ent_id() {
        return ans_update_ent_id;
    }

    public void setAns_update_ent_id(int ans_update_ent_id) {
        this.ans_update_ent_id = ans_update_ent_id;
    }

    public Timestamp getAns_update_timestamp() {
        return ans_update_timestamp;
    }

    public void setAns_update_timestamp(Timestamp ans_update_timestamp) {
        this.ans_update_timestamp = ans_update_timestamp;
    }

    public String getAns_refer_content() {
        return ans_refer_content;
    }

    public void setAns_refer_content(String ans_refer_content) {
        this.ans_refer_content = ans_refer_content;
    }

}
