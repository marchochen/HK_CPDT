/*
 * for auto-enrol, override method(s) in parent class 
 */
package com.cw.wizbank.ae;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;

public class aeAttendanceAutoEnrol extends aeAttendance {
    public static void saveStatus(Connection con, long app_id, long itm_id, long cos_id, long usr_ent_id, boolean setFinal, int ats_id, String upd_usr_id, long app_tkh_id, aeApplication app) throws cwException, SQLException, qdbException {
    	Timestamp curTime = cwSQL.getTime(con);
    	
        aeAttendance att = new aeAttendance();
        att.att_update_usr_id = upd_usr_id;
    	att.att_app_id = app.app_id;
        att.att_ats_id = ats_id;
        att.att_itm_id = itm_id;

        aeAttendanceStatus ats = new aeAttendanceStatus();
        ats.ats_id = ats_id;
        ats.get(con);

        DbTrackingHistory dbTkh = new DbTrackingHistory();
        dbTkh.tkh_usr_ent_id = usr_ent_id;
        dbTkh.tkh_cos_res_id = cos_id;
        dbTkh.tkh_type = DbTrackingHistory.TKH_TYPE_APPLICATION;
        dbTkh.tkh_create_timestamp = curTime;
        
        dbTkh.ins(con);
		app.app_tkh_id = dbTkh.tkh_id;
		if(app.app_id == 0) {
			app.ins(con);
		}
        dbCourseEvaluation newcov = new dbCourseEvaluation();
        newcov.cov_ent_id = usr_ent_id;
        newcov.cov_cos_id = cos_id;
        newcov.cov_tkh_id = dbTkh.tkh_id;
        newcov.cov_status = ats.ats_cov_status;
        if (ats.ats_attend_ind > 0){
            newcov.cov_complete_datetime = curTime;
        }
        newcov.cov_final_ind = setFinal;
        newcov.ins(con);
    	att.att_app_id = app.app_id;
        att.saveStatus(con);
    }
}
