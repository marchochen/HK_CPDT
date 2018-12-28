package com.cw.wizbank.ae;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

public class aeSession extends aeItem{
    /*
    public aeSession(aeItem itm) {
        this.itm_title = itm.itm_title;
        this.itm_type = itm.itm_type;
        this.itm_appn_start_datetime = itm.itm_appn_start_datetime;
        this.itm_appn_end_datetime = itm.itm_appn_end_datetime;
        this.itm_capacity = itm.itm_capacity;
        //this.itm_ext2 = itm.itm_ext2;
        this.itm_unit = itm.itm_unit;
        this.itm_fee_ccy = itm.itm_fee_ccy;
        this.itm_fee = itm.itm_fee;
        this.itm_xml = itm.itm_xml;
        this.itm_status = itm.itm_status;
        this.itm_owner_ent_id = itm.itm_owner_ent_id;
        this.itm_create_timestamp = itm.itm_create_timestamp;
        this.itm_create_usr_id = itm.itm_create_usr_id;
        this.itm_upd_timestamp = itm.itm_upd_timestamp;
        this.itm_upd_usr_id = itm.itm_upd_usr_id;
        this.itm_eff_start_datetime = itm.itm_eff_start_datetime;
        this.itm_eff_end_datetime = itm.itm_eff_end_datetime;
        this.itm_code = itm.itm_code;
        this.itm_ext1 = itm.itm_ext1;
        this.itm_create_run_ind = itm.itm_create_run_ind;
        this.itm_create_session_ind = itm.itm_create_session_ind;
        this.itm_run_ind = itm.itm_run_ind;
        this.itm_session_ind = itm.itm_session_ind;
        this.itm_has_attendance_ind = itm.itm_has_attendance_ind;
        this.itm_apply_ind = itm.itm_apply_ind;
        this.itm_deprecated_ind = itm.itm_deprecated_ind;
        this.itm_life_status = itm.itm_life_status;
        this.itm_apply_method = itm.itm_apply_method;
        this.itm_qdb_ind = itm.itm_qdb_ind;
        this.itm_auto_enrol_qdb_ind = itm.itm_auto_enrol_qdb_ind;
        this.itm_version_code = itm.itm_version_code;
        this.itm_min_capacity = itm.itm_min_capacity;
        this.itm_person_in_charge = itm.itm_person_in_charge;
        this.itm_cancellation_reason = itm.itm_cancellation_reason;
    }
    */

    public static void insSessionAttendance(Connection con, long parent_itm_id, Vector v_session_itm_id, Vector v_app_id, int ats_id, String upd_usr_id, long root_ent_id)
        throws cwSysMessage, cwException, SQLException{
        if (v_session_itm_id==null){
            v_session_itm_id = aeItemRelation.getChildItemId(con, parent_itm_id, true);
        }                    
        if (v_app_id==null){
            v_app_id = aeApplication.getLatestItmAppnLstWAtt(con, parent_itm_id);
        }
        if (ats_id==0){
            ats_id = aeAttendanceStatus.getIdByType(con, root_ent_id, aeAttendanceStatus.STATUS_TYPE_PROGRESS);
        }
        long[] appIdLst = cwUtils.vec2longArray(v_app_id);
        for (int i=0; i<v_session_itm_id.size();i++){
            long session_itm_id = ((Long) v_session_itm_id.elementAt(i)).longValue();
            aeAttendance att = new aeAttendance();
            att.updMultiStatus(con, upd_usr_id, appIdLst, ats_id, session_itm_id); 
        }
    }
    
    /*
    public void delSessionAttendance(Connection con, long parent_itm_id, Vector v_session_itm_id) throws SQLException{
        if (v_session_itm_id==null){
            v_session_itm_id = aeItemRelation.getChildItemId(con, parent_itm_id);
        }
        for (int i=0; i<v_session_itm_id.size(); i++){
            aeAttendance.delByItem(con, ((Long)v_session_itm_id.elementAt(i)).longValue());
        }
    }
    */

}