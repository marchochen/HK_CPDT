package com.cw.wizbank.JsonMod.know.bean;

import java.util.Vector;

/**
 * @author DeanChen
 * 
 */
public class KnowHelpBean {

    private int zd_vote_duration;

    private int available_question_days;

    private int popular_question_count;

    private int vote_for_rate;

    private Vector credits_type_lst;

    public int getZd_vote_duration() {
        return zd_vote_duration;
    }

    public void setZd_vote_duration(int zd_vote_duration) {
        this.zd_vote_duration = zd_vote_duration;
    }

    public int getAvailable_question_days() {
        return available_question_days;
    }

    public void setAvailable_question_days(int available_question_days) {
        this.available_question_days = available_question_days;
    }

    public int getPopular_question_count() {
        return popular_question_count;
    }

    public void setPopular_question_count(int popular_question_count) {
        this.popular_question_count = popular_question_count;
    }

    public int getVote_for_rate() {
        return vote_for_rate;
    }

    public void setVote_for_rate(int vote_for_rate) {
        this.vote_for_rate = vote_for_rate;
    }

    public Vector getCredits_type_lst() {
        return credits_type_lst;
    }

    public void setCredits_type_lst(Vector credits_type_lst) {
        this.credits_type_lst = credits_type_lst;
    }

}
