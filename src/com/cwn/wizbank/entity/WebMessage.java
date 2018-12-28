package com.cwn.wizbank.entity;

import java.util.Date;


public class WebMessage implements java.io.Serializable {
    private static final long serialVersionUID = -6128943386998444333L;
    /**
     * pk
     * 邮件ID
     **/
    String wmsg_id;
    /**
     * 模板ID
     **/
    Integer wmsg_mtp_id;
    /**
     * 发送人
     **/
    Long wmsg_send_ent_id;

    RegUser sendUser;
    /**
     * 收件人
     **/
    Long wmsg_rec_ent_id;

    RegUser recUser;
    /**
     * 主题
     **/
    String wmsg_subject;
    /**
     * 内容(电脑)
     **/
    String wmsg_content_pc;
    /**
     * 内容(电脑)
     **/
    String wmsg_admin_content_pc;
    /**
     * 内容(手机)
     **/
    String wmsg_content_mobile;
    /**
     * 定时发送时间
     **/
    Date wmsg_target_datetime;
    /**
     * 发送类型
     **/
    String wmsg_type;
    /**
     * 创建者
     **/
    Long wmsg_create_ent_id;
    /**
     * 创建时间
     **/
    Date wmsg_create_timestamp;

    WebMsgReadHistory readHistory;

    long wmsg_type_total;

    public WebMessage() {
    }

    public String getWmsg_id() {
        return wmsg_id;
    }

    public void setWmsg_id(String wmsg_id) {
        this.wmsg_id = wmsg_id;
    }

    public Integer getWmsg_mtp_id() {
        return this.wmsg_mtp_id;
    }

    public void setWmsg_mtp_id(Integer wmsg_mtp_id) {
        this.wmsg_mtp_id = wmsg_mtp_id;
    }

    public Long getWmsg_send_ent_id() {
        return this.wmsg_send_ent_id;
    }

    public void setWmsg_send_ent_id(Long wmsg_send_ent_id) {
        this.wmsg_send_ent_id = wmsg_send_ent_id;
    }

    public RegUser getSendUser() {
        return sendUser;
    }

    public void setSendUser(RegUser sendUser) {
        this.sendUser = sendUser;
    }

    public Long getWmsg_rec_ent_id() {
        return this.wmsg_rec_ent_id;
    }

    public void setWmsg_rec_ent_id(Long wmsg_rec_ent_id) {
        this.wmsg_rec_ent_id = wmsg_rec_ent_id;
    }

    public String getWmsg_subject() {
        return this.wmsg_subject;
    }

    public void setWmsg_subject(String wmsg_subject) {
        this.wmsg_subject = wmsg_subject;
    }

    public String getWmsg_content_pc() {
        return this.wmsg_content_pc;
    }

    public void setWmsg_content_pc(String wmsg_content_pc) {
        this.wmsg_content_pc = wmsg_content_pc;
    }

    public String getWmsg_content_mobile() {
        return this.wmsg_content_mobile;
    }

    public void setWmsg_content_mobile(String wmsg_content_mobile) {
        this.wmsg_content_mobile = wmsg_content_mobile;
    }

    public Date getWmsg_target_datetime() {
        return this.wmsg_target_datetime;
    }

    public void setWmsg_target_datetime(Date wmsg_target_datetime) {
        this.wmsg_target_datetime = wmsg_target_datetime;
    }

    public String getWmsg_type() {
        return this.wmsg_type;
    }

    public void setWmsg_type(String wmsg_type) {
        this.wmsg_type = wmsg_type;
    }

    public Long getWmsg_create_ent_id() {
        return this.wmsg_create_ent_id;
    }

    public void setWmsg_create_ent_id(Long wmsg_create_ent_id) {
        this.wmsg_create_ent_id = wmsg_create_ent_id;
    }

    public Date getWmsg_create_timestamp() {
        return this.wmsg_create_timestamp;
    }

    public void setWmsg_create_timestamp(Date wmsg_create_timestamp) {
        this.wmsg_create_timestamp = wmsg_create_timestamp;
    }

    public WebMsgReadHistory getReadHistory() {
        return readHistory;
    }

    public void setReadHistory(WebMsgReadHistory readHistory) {
        this.readHistory = readHistory;
    }

    public String getWmsg_admin_content_pc() {
        return wmsg_admin_content_pc;
    }

    public void setWmsg_admin_content_pc(String wmsg_admin_content_pc) {
        this.wmsg_admin_content_pc = wmsg_admin_content_pc;
    }

    public RegUser getRecUser() {
        return recUser;
    }

    public void setRecUser(RegUser recUser) {
        this.recUser = recUser;
    }

    public long getWmsg_type_total() {
        return wmsg_type_total;
    }

    public void setWmsg_type_total(long wmsg_type_total) {
        this.wmsg_type_total = wmsg_type_total;
    }
}