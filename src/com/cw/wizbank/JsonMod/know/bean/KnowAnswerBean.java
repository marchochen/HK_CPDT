package com.cw.wizbank.JsonMod.know.bean;

import java.sql.Timestamp;

/**
 * @author DeanChen
 * 
 */
public class KnowAnswerBean {

    private int ans_que_id;

    private int ans_id;

    private boolean ans_right_ind;

    private int ans_vote_total;

    private int ans_vote_for;

    private int ans_vote_down;

    private int ans_vote_for_rate;

    private int ans_vote_down_rate;

    private String ans_content;

    private String ans_refer_content;

    private int ans_create_ent_id;

    private String usr_nickname;
    
    private long usr_ent_id;

    private String usr_photo;

    private Timestamp ans_create_timestamp;

    public int getAns_que_id() {
        return ans_que_id;
    }

    public void setAns_que_id(int ans_que_id) {
        this.ans_que_id = ans_que_id;
    }

    public int getAns_id() {
        return ans_id;
    }

    public void setAns_id(int ans_id) {
        this.ans_id = ans_id;
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

    public String getAns_content() {
        return ans_content;
    }

    public void setAns_content(String ans_content) {
        this.ans_content = ans_content;
    }

    public int getAns_create_ent_id() {
        return ans_create_ent_id;
    }

    public void setAns_create_ent_id(int ans_create_ent_id) {
        this.ans_create_ent_id = ans_create_ent_id;
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

    public String getUsr_photo() {
        return usr_photo;
    }

    public void setUsr_photo(String usr_photo) {
        this.usr_photo = usr_photo;
    }

    public Timestamp getAns_create_timestamp() {
        return ans_create_timestamp;
    }

    public void setAns_create_timestamp(Timestamp ans_create_timestamp) {
        this.ans_create_timestamp = ans_create_timestamp;
    }

    public String getAns_refer_content() {
        return ans_refer_content;
    }

    public void setAns_refer_content(String ans_refer_content) {
        this.ans_refer_content = ans_refer_content;
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

}
