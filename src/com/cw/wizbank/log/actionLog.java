package com.cw.wizbank.log;

import java.util.Date;

public class actionLog {
    public long atl_usr_ent_id;

    public String atl_session_id;

    public String atl_usr_ste_usr_id;

    public String atl_cmd;

    public Date atl_start_date;

    public long atl_duration;

    public String atl_remote;

    public String atl_server;

    public String atl_status;// Success/ Fail

    public String atl_error_msg;

    public String atl_ids;

    public String atl_ids_type; // usr/usg/grade/itm/resource/mod/ccr/app

    public String atl_login_code;

    public String atl_description;

    public long getAtl_usr_ent_id() {
        return atl_usr_ent_id;
    }

    public void setAtl_usr_ent_id(long atlUsrEntId) {
        atl_usr_ent_id = atlUsrEntId;
    }

    public String getAtl_session_id() {
        return atl_session_id;
    }

    public void setAtl_session_id(String atlSessionId) {
        atl_session_id = atlSessionId;
    }

    public String getAtl_usr_ste_usr_id() {
        return atl_usr_ste_usr_id;
    }

    public void setAtl_usr_ste_usr_id(String atlUsrSteUsrId) {
        atl_usr_ste_usr_id = atlUsrSteUsrId;
    }

    public String getAtl_cmd() {
        return atl_cmd;
    }

    public void setAtl_cmd(String atlCmd) {
        atl_cmd = atlCmd;
    }

    public Date getAtl_datetime() {
        return atl_start_date;
    }

    public void setAtl_datetime(Date atlStartDate) {
        atl_start_date = atlStartDate;
    }

    public long getAtl_duration() {
        return atl_duration;
    }

    public void setAtl_duration(long atlDuration) {
        atl_duration = atlDuration;
    }

    public String getAtl_remote() {
        return atl_remote;
    }

    public void setAtl_remote_ip(String atlRemote) {
        atl_remote = atlRemote;
    }

    public String getAtl_server() {
        return atl_server;
    }

    public void setAtl_server_ip(String atlServer) {
        atl_server = atlServer;
    }

    public String getAtl_status() {
        return atl_status;
    }

    public void setAtl_status(String atlStatus) {
        atl_status = atlStatus;
    }

    public String getAtl_error_msg() {
        return atl_error_msg;
    }

    public void setAtl_error_msg(String atlErrorMsg) {
        atl_error_msg = atlErrorMsg;
    }

    public String getAtl_ids() {
        return atl_ids;
    }

    public void setAtl_ids(String atlIds) {
        atl_ids = atlIds;
    }

    public String getAtl_ids_type() {
        return atl_ids_type;
    }

    public void setAtl_ids_type(String atlIdsType) {
        atl_ids_type = atlIdsType;
    }

    public String getAtl_login_code() {
        return atl_login_code;
    }

    public void setAtl_login_code(String atlLoginCode) {
        atl_login_code = atlLoginCode;
    }

    public String getAtl_description() {
        return atl_description;
    }

    public void setAtl_description(String atlDescription) {
        atl_description = atlDescription;
    }

}
