package com.cw.wizbank.db.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

public class ViewCourseModuleCriteria{
    public static class ViewByCcrId {
        public String res_title;
        public String mod_type;
        public String res_src_type;
        public long mod_res_id;
        public float mod_max_score;
        public long cmr_id;
        public long cmr_ccr_id;
        public String cmr_status;
        public float cmr_contri_rate;
        public boolean cmr_is_contri_by_score;
        public Timestamp cmr_upd_timestamp;
    }
    public static class ViewAttendDate {
        public long cos_res_id;
        public long ccr_id;
        public long ccr_duration;
        public float ccr_pass_score;
        public boolean ccr_pass_ind;
        public boolean ccr_all_cond_ind;
        public int ccr_attendance_rate;
        public long ccr_itm_id;
        public String ccr_type;
        public String ccr_upd_method;
        public int itm_content_eff_duration;
        public Timestamp att_create_timestamp;
        public Timestamp itm_content_eff_start_datetime;
        public Timestamp itm_content_eff_end_datetime;
        public long app_itm_id;
        public boolean itm_create_session_ind;
        public long app_id;
        public String cov_status;
        public float cov_score;
        public long cov_ent_id;
        public long cov_tkh_id;
    }
    
    public static class ViewCmr {
        public long cmr_id;
        public String cmt_title;
        public long cmr_ccr_id;
        public long cmr_res_id;
        public long cmt_id;
        public String cmr_status_desc_option;
        public String cmr_status;
        public String res_subtype;
        public String res_title;
    }
    
    // kim no changed
    public static ViewByCcrId[] getByCcrId(Connection con, long ccr_id) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_module_criteria_lst);
            stmt.setLong(1, ccr_id);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewByCcrId modCrit = new ViewByCcrId();
                modCrit.res_title = rs.getString("res_title");
                modCrit.mod_type = rs.getString("mod_type");
                modCrit.res_src_type = rs.getString("res_src_type");
                modCrit.mod_res_id = rs.getLong("mod_res_id");
                modCrit.mod_max_score = rs.getFloat("mod_max_score");
                modCrit.cmr_id = rs.getLong("cmr_id");
                modCrit.cmr_ccr_id = rs.getLong("cmr_ccr_id");
                modCrit.cmr_status = rs.getString("cmr_status");
                modCrit.cmr_contri_rate = rs.getFloat("cmr_contri_rate");
                modCrit.cmr_is_contri_by_score = rs.getBoolean("cmr_is_contri_by_score");
                modCrit.cmr_upd_timestamp = rs.getTimestamp("cmr_upd_timestamp");
                tempResult.addElement(modCrit);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewByCcrId result[] = new ViewByCcrId[tempResult.size()];
        result = (ViewByCcrId[])tempResult.toArray(result);
        
        return result;
    }
    
    // kim todo -- for reminder
    //    no action on ccr_duration = 0
    /*
    public static ResultSet getNotificationActiveList(Connection con, long root_ent_id) throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_notification_active_list());
        stmt.setLong(1, root_ent_id);
        stmt.setString(2, DbCourseCriteria.TYPE_NOTIFICATION);
        stmt.setTimestamp(3, curTime);
        stmt.setTimestamp(4, curTime);
        stmt.setTimestamp(5, curTime);
        stmt.setTimestamp(6, curTime);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }
    */
    // expired method, no need to set attendane or score during course eff period
    /*
    public static ResultSet getActiveList(Connection con, long root_ent_id, String ccr_type) throws SQLException{
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_active_list());
        stmt.setLong(1, root_ent_id);
        stmt.setString(2, ccr_type);
        stmt.setTimestamp(3, curTime);
        stmt.setTimestamp(4, curTime);
        stmt.setTimestamp(5, curTime);
        stmt.setTimestamp(6, curTime);
        ResultSet rs = stmt.executeQuery();
        return rs;
    }*/

    // kim :todo -- in reminder 
    /*    
    public static Hashtable getCompletionActiveList(Connection con, long root_ent_id) throws SQLException{
        Hashtable htCourse = new Hashtable();
        ResultSet rs = getActiveList(con, root_ent_id, DbCourseCriteria.TYPE_COMPLETION);
        while (rs.next()){
            Long cos_id = new Long(rs.getLong("cos_res_id"));
            Long ent_id = new Long(rs.getLong("cov_ent_id"));
            Vector vtEntIdInCourse = (Vector)htCourse.get(cos_id);
            if (vtEntIdInCourse == null){
                vtEntIdInCourse = new Vector();
                vtEntIdInCourse.addElement(ent_id);
            }else{
                vtEntIdInCourse.addElement(ent_id);
            }
            htCourse.put(cos_id, vtEntIdInCourse);
        }
        return htCourse;
    }
    */
    // kim:todo -- for reminder 
    //no action on ccr_duration = 0
    /*
    public static Vector getNoCompletionCriteriaActiveCourse(Connection con, long root_ent_id)
        throws SQLException{

        Vector cosIDs = new Vector();
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_no_completion_criteria_active_course());
        stmt.setTimestamp(1, curTime);
        stmt.setTimestamp(2, curTime);
        stmt.setLong(3, root_ent_id);
        stmt.setString(4, DbCourseCriteria.TYPE_COMPLETION);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            cosIDs.addElement(new Long(rs.getLong("cos_res_id")));
        }
        return cosIDs;
    }*/
    
    // get list that the user is still active in the course
    // independent on ccr_type
    public static ViewAttendDate[] getActiveList(Connection con, long root_ent_id, String ccr_type) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SqlStatements.sql_get_set_attend_list(false, true, 0, 0));
            int index = 1;
            stmt.setBoolean(index++, false);
            stmt.setLong(index++, root_ent_id);
            stmt.setString(index++, ccr_type);
            stmt.setInt(index++, 0);
            stmt.setTimestamp(index++, curTime);
            stmt.setTimestamp(index++, curTime);
            stmt.setTimestamp(index++, curTime);
            stmt.setInt(index++, 0);
            // union
            stmt.setBoolean(index++, false);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, root_ent_id);
            stmt.setString(index++, ccr_type);
            stmt.setTimestamp(index++, curTime);
            stmt.setTimestamp(index++, curTime);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewAttendDate modCrit = new ViewAttendDate();
                modCrit.cos_res_id = rs.getLong("cos_res_id");
                modCrit.ccr_id = rs.getLong("ccr_id");
                modCrit.ccr_duration = rs.getLong("ccr_duration");
                modCrit.ccr_pass_score = rs.getFloat("ccr_pass_score");
                modCrit.ccr_pass_ind = rs.getBoolean("ccr_pass_ind");
                modCrit.ccr_all_cond_ind = rs.getBoolean("ccr_all_cond_ind");
                modCrit.ccr_attendance_rate = rs.getInt("ccr_attendance_rate");
                modCrit.ccr_itm_id = rs.getLong("ccr_itm_id");
                modCrit.ccr_type = rs.getString("ccr_type");
                modCrit.ccr_upd_method = rs.getString("ccr_upd_method");
                modCrit.itm_content_eff_duration = rs.getInt("itm_content_eff_duration");
                modCrit.att_create_timestamp = rs.getTimestamp("att_create_timestamp");
                modCrit.itm_content_eff_start_datetime = rs.getTimestamp("itm_content_eff_start_datetime");
                modCrit.itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
                modCrit.app_itm_id = rs.getLong("app_itm_id");
                modCrit.itm_create_session_ind = rs.getBoolean("itm_create_session_ind");
                modCrit.app_id = rs.getLong("app_id");
                modCrit.cov_status = rs.getString("cov_status");
                modCrit.cov_score = rs.getFloat("cov_score");
                modCrit.cov_ent_id = rs.getLong("cov_ent_id");
                modCrit.cov_tkh_id = rs.getLong("cov_tkh_id");
                tempResult.addElement(modCrit);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewAttendDate result[] = new ViewAttendDate[tempResult.size()];
        result = (ViewAttendDate[])tempResult.toArray(result);
        
        return result;
    }

    public static ViewAttendDate[] getManualList(Connection con, long root_ent_id, String ccr_type, long app_itm_id, long app_id) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SqlStatements.sql_get_set_attend_list(false, false, app_itm_id, app_id));
            int index = 1;
            stmt.setBoolean(index++, false);
            stmt.setLong(index++, root_ent_id);
            stmt.setString(index++, ccr_type);
            // union
            stmt.setBoolean(index++, false);
            stmt.setBoolean(index++, true);
            stmt.setLong(index++, root_ent_id);
            stmt.setString(index++, ccr_type);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewAttendDate modCrit = new ViewAttendDate();
                modCrit.cos_res_id = rs.getLong("cos_res_id");
                modCrit.ccr_id = rs.getLong("ccr_id");
                modCrit.ccr_duration = rs.getLong("ccr_duration");
                modCrit.ccr_pass_score = rs.getFloat("ccr_pass_score");
                modCrit.ccr_pass_ind = rs.getBoolean("ccr_pass_ind");
                modCrit.ccr_all_cond_ind = rs.getBoolean("ccr_all_cond_ind");
                modCrit.ccr_attendance_rate = rs.getInt("ccr_attendance_rate");
                modCrit.ccr_itm_id = rs.getLong("ccr_itm_id");
                modCrit.ccr_type = rs.getString("ccr_type");
                modCrit.ccr_upd_method = rs.getString("ccr_upd_method");
                modCrit.itm_content_eff_duration = rs.getInt("itm_content_eff_duration");
                modCrit.att_create_timestamp = rs.getTimestamp("att_create_timestamp");
                modCrit.itm_content_eff_start_datetime = rs.getTimestamp("itm_content_eff_start_datetime");
                modCrit.itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
                modCrit.app_itm_id = rs.getLong("app_itm_id");
                modCrit.itm_create_session_ind = rs.getBoolean("itm_create_session_ind");
                modCrit.app_id = rs.getLong("app_id");
                modCrit.cov_status = rs.getString("cov_status");
                modCrit.cov_score = rs.getFloat("cov_score");
                modCrit.cov_ent_id = rs.getLong("cov_ent_id");
                modCrit.cov_tkh_id = rs.getLong("cov_tkh_id");
                tempResult.addElement(modCrit);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewAttendDate result[] = new ViewAttendDate[tempResult.size()];
        result = (ViewAttendDate[])tempResult.toArray(result);
        
        return result;
    }

    public static ViewAttendDate[] getExpiredList(Connection con, long root_ent_id, String ccr_type, boolean itm_integrated_ind) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            //stmt = con.prepareStatement(SqlStatements.sql_get_set_attend_list(true, false, 0, 0));
			stmt = con.prepareStatement(SqlStatements.sql_get_lrnr_value(con, true, false, 0, 0));
            int index = 1;
            stmt.setBoolean(index++, false);
            stmt.setLong(index++, root_ent_id);
            stmt.setString(index++, ccr_type);
            stmt.setBoolean(index++, itm_integrated_ind);
            stmt.setInt(index++, 0);
            stmt.setBoolean(index++, false);
            stmt.setBoolean(index++, true);
            stmt.setTimestamp(index++, curTime);
            stmt.setBoolean(index++, false);
            stmt.setTimestamp(index++, curTime);
            stmt.setTimestamp(index++, curTime);
            stmt.setBoolean(index++, true);
            stmt.setTimestamp(index++, curTime);
            stmt.setInt(index++, 0);
            stmt.setBoolean(index++, false);
            // union
//            stmt.setBoolean(index++, false);
//            stmt.setBoolean(index++, true);
//            stmt.setLong(index++, root_ent_id);
//            stmt.setString(index++, ccr_type);
//            stmt.setTimestamp(index++, curTime);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewAttendDate modCrit = new ViewAttendDate();
                modCrit.cos_res_id = rs.getLong("cos_res_id");
                modCrit.ccr_id = rs.getLong("ccr_id");
                modCrit.ccr_duration = rs.getLong("ccr_duration");
                modCrit.ccr_pass_score = rs.getFloat("ccr_pass_score");
                modCrit.ccr_pass_ind = rs.getBoolean("ccr_pass_ind");
                modCrit.ccr_all_cond_ind = rs.getBoolean("ccr_all_cond_ind");
                modCrit.ccr_attendance_rate = rs.getInt("ccr_attendance_rate");
                modCrit.ccr_itm_id = rs.getLong("ccr_itm_id");
                modCrit.ccr_type = rs.getString("ccr_type");
                modCrit.ccr_upd_method = rs.getString("ccr_upd_method");
                modCrit.itm_content_eff_duration = rs.getInt("itm_content_eff_duration");
                modCrit.att_create_timestamp = rs.getTimestamp("att_create_timestamp");
                modCrit.itm_content_eff_start_datetime = rs.getTimestamp("itm_content_eff_start_datetime");
                modCrit.itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");
                modCrit.app_itm_id = rs.getLong("app_itm_id");
                modCrit.itm_create_session_ind = rs.getBoolean("itm_create_session_ind");
                modCrit.app_id = rs.getLong("app_id");
                modCrit.cov_status = rs.getString("cov_status");
                modCrit.cov_score = rs.getFloat("cov_score");
                modCrit.cov_ent_id = rs.getLong("cov_ent_id");
                modCrit.cov_tkh_id = rs.getLong("cov_tkh_id");
                tempResult.addElement(modCrit);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewAttendDate result[] = new ViewAttendDate[tempResult.size()];
        result = (ViewAttendDate[])tempResult.toArray(result);
        
        return result;
    }
    
    public static Vector getProgressAppList(Connection con, long root_ent_id, boolean itm_integrated_ind) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = new Vector();
        try {
            Timestamp curTime = cwSQL.getTime(con);
            //stmt = con.prepareStatement(SqlStatements.sql_get_set_attend_list(true, false, 0, 0));
            stmt = con.prepareStatement("select cov_cos_id, app_itm_id,app_id,cov_status,cov_ent_id ,cov_tkh_id " +
            		" from aeItem,aeApplication,courseEvaluation " +
            		" where app_itm_id= itm_id and app_tkh_id = cov_tkh_id and itm_integrated_ind = ? and itm_owner_ent_id  =  ? and (cov_status = ? or cov_status is null)");
            int index = 1;

            stmt.setBoolean(index++, itm_integrated_ind);
            stmt.setLong(index++, root_ent_id);
            stmt.setString(index++, "I");
            rs = stmt.executeQuery();
            
            
            while (rs.next()) {
                ViewAttendDate modCrit = new ViewAttendDate();
                modCrit.cos_res_id = rs.getLong("cov_cos_id");
                modCrit.app_itm_id = rs.getLong("app_itm_id");
                modCrit.app_id = rs.getLong("app_id");
                modCrit.cov_status = rs.getString("cov_status");
                modCrit.cov_ent_id = rs.getLong("cov_ent_id");
                modCrit.cov_tkh_id = rs.getLong("cov_tkh_id");
                tempResult.addElement(modCrit);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        
        return tempResult;
    }

    public static ViewByCcrId[] getByCrrIdNDate(Connection con, long ccr_id, Timestamp eff_start_date, Timestamp eff_end_date) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_module_criteria_lst_by_date);
            // tricky:
            // AND cmr_create_timestamp <= eff_end_date AND (cmr_del_timestamp >= eff_start_date OR cmr_del_timestamp is null)
            stmt.setLong(1, ccr_id);
            stmt.setTimestamp(2, eff_end_date);
            stmt.setTimestamp(3, eff_start_date);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewByCcrId modCrit = new ViewByCcrId();
                modCrit.res_title = rs.getString("res_title");
                modCrit.mod_type = rs.getString("mod_type");
                modCrit.res_src_type = rs.getString("res_src_type");
                modCrit.mod_res_id = rs.getLong("mod_res_id");
                modCrit.mod_max_score = rs.getFloat("mod_max_score");
                modCrit.cmr_id = rs.getLong("cmr_id");
                modCrit.cmr_ccr_id = rs.getLong("cmr_ccr_id");
                modCrit.cmr_status = rs.getString("cmr_status");
                modCrit.cmr_contri_rate = rs.getFloat("cmr_contri_rate");
                modCrit.cmr_is_contri_by_score = rs.getBoolean("cmr_is_contri_by_score");
                modCrit.cmr_upd_timestamp = rs.getTimestamp("cmr_upd_timestamp");
                tempResult.addElement(modCrit);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewByCcrId result[] = new ViewByCcrId[tempResult.size()];
        result = (ViewByCcrId[])tempResult.toArray(result);
        
        return result;
    }
    
    
    // course end date can be itm content eff end date or att_date + completion ccr_duration
    public static Timestamp getCourseEndDate(Connection con, long itm_id, long ent_id) throws SQLException, cwException{
        Timestamp end;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_course_start_end_date);
        stmt.setLong(1, ent_id);
        stmt.setLong(2, itm_id);
        stmt.setBoolean(3, true);
        stmt.setLong(4, ent_id);
        stmt.setLong(5, itm_id);

            rs = stmt.executeQuery();
        if (rs.next()){
            Timestamp content_end = rs.getTimestamp("itm_content_eff_end_datetime");
            if (content_end!=null){
                end = content_end;
            }else{
                int duration = rs.getInt("itm_content_eff_duration");
                if (duration!=0){
                    end = new Timestamp(rs.getTimestamp("att_create_timestamp").getTime() + (long)duration*24*60*60*1000);  
                }else{
                    end = null;                                        
                }
            }
            return end;
        }else{
            throw new cwException("no record found in getCourseEndDate, ent_id:" + ent_id + " itm_id:" + itm_id);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
    
    
    private static final String crtSql = "select cos_res_id, ccr_id, ccr_duration, ccr_pass_score , ccr_pass_ind, ccr_all_cond_ind, ccr_attendance_rate, ccr_itm_id, ccr_type, ccr_upd_method,app_itm_id, app_id, cov_status, cov_score, cov_ent_id , cov_tkh_id "+
                                    " from courseCriteria,course,aeApplication,courseEvaluation,aeAttendance "+
                                    " where ccr_id = ? and ccr_itm_id = app_itm_id and app_tkh_id = cov_tkh_id and att_app_id = app_id and cos_itm_id = ccr_itm_id"+
                                    " and app_id in ( select id from ( select MAX(app_id) id,app_ent_id from aeApplication where app_itm_id = (select ccr_itm_id  from courseCriteria where ccr_id = ?) group by app_ent_id) temp )";

	public static ViewAttendDate[] getAppWithCoscrt(Connection con,long ccr_id){
		List list = new ArrayList();
		ViewAttendDate[] views = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = con.prepareStatement(crtSql);
			pst.setLong(1,ccr_id);
			pst.setLong(2,ccr_id);
			rs = pst.executeQuery();
			while(rs.next()){
				ViewAttendDate modCrit = new ViewAttendDate();
				modCrit.cos_res_id = rs.getLong("cos_res_id");
				modCrit.ccr_id = rs.getLong("ccr_id");
				modCrit.ccr_duration = rs.getLong("ccr_duration");
				modCrit.ccr_pass_score = rs.getFloat("ccr_pass_score");
				modCrit.ccr_pass_ind = rs.getBoolean("ccr_pass_ind");
				modCrit.ccr_all_cond_ind = rs.getBoolean("ccr_all_cond_ind");
				modCrit.ccr_attendance_rate = rs.getInt("ccr_attendance_rate");
				modCrit.ccr_itm_id = rs.getLong("ccr_itm_id");
				modCrit.ccr_type = rs.getString("ccr_type");
				modCrit.ccr_upd_method = rs.getString("ccr_upd_method");
				modCrit.app_itm_id = rs.getLong("app_itm_id");
				modCrit.app_id = rs.getLong("app_id");
				modCrit.cov_status = rs.getString("cov_status");
				modCrit.cov_score = rs.getFloat("cov_score");
				modCrit.cov_ent_id = rs.getLong("cov_ent_id");
				modCrit.cov_tkh_id = rs.getLong("cov_tkh_id");
				list.add(modCrit);
			}
			views = new ViewAttendDate[list.size()];
			views = (ViewAttendDate[])list.toArray(views);
		}catch(SQLException e){
			throw new RuntimeException("Server error: "+e.getMessage());
		}finally{
			cwSQL.cleanUp(rs,pst);
		}
		return views;
	}
    
    // kim: todo    
    /*
    public static Timestamp getCourseStartDate(Connection con, long cos_res_id, long ent_id) throws SQLException, cwException{
//System.out.println("cos_id:" + cos_res_id + " ent_id:" + ent_id);
        PreparedStatement stmt = con.prepareStatement(OuterJoinSqlStatements.sql_get_course_start_end_date(con));
        stmt.setString(1, DbCourseCriteria.TYPE_COMPLETION);
        stmt.setLong(2, ent_id);
        stmt.setLong(3, cos_res_id);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            int duration = rs.getInt("ccr_duration");
            Timestamp enr_start = rs.getTimestamp("enr_create_timestamp");
            Timestamp cos_start = rs.getTimestamp("cos_eff_start_datetime");

            Timestamp start = null;
            if (cos_start == null){
                start = enr_start;
            }else if (duration == 0){
                start = cos_start;
            }else if (enr_start.after(cos_start)){
                start = enr_start;
            }else{
                start = cos_start;
            }

            return start;
        }else{
            throw new cwException("no record found in getCourseStartDate, ent_id:" + ent_id + " cos_id:" + cos_res_id);
        }
    }*/
    /**
     * 
     * @param con the Database Connection Object
     * @param ccr_id 
     * @param cmrType 0:Online Item;1:Scoring Item
     * @return Vector of DbCourseMeasurement
     * @throws SQLException
     * @throws cwSysMessage
     */
    public static ViewCmr[] getCmrLstByType(Connection con, long ccr_id, int cmrType) throws SQLException, cwSysMessage{
        String sql = "select cmr_id, cmr_ccr_id, cmr_res_id, cmt_title, cmt_id, cmr_status_desc_option,cmr_status, res_subtype, res_title "
        			+ " from CourseMeasurement, CourseModuleCriteria, Resources"
            		+ " where cmt_cmr_id=cmr_id and cmr_ccr_id=cmt_ccr_id and cmr_res_id = res_id"
            		+ " and cmr_ccr_id = ?"
        			+ " and cmr_del_timestamp is null and cmt_delete_timestamp is null";
        if(cmrType == 0){
           sql += " and cmr_is_contri_by_score = 0";
        }else if(cmrType == 1){
            sql += " and cmr_is_contri_by_score = 1";
        }
        sql += " order by cmt_id";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, ccr_id);
		ResultSet rs = stmt.executeQuery();
        Vector tmpVec = new Vector();
		while (rs.next()) {
		    ViewCmr cmr = new ViewCmr();
		    cmr.cmr_id = rs.getLong("cmr_id");
		    cmr.cmr_ccr_id = rs.getLong("cmr_ccr_id");
		    cmr.cmr_res_id = rs.getLong("cmr_res_id");
		    cmr.cmt_title = rs.getString("cmt_title");
		    cmr.cmt_id = rs.getLong("cmt_id");
		    cmr.cmr_status_desc_option = rs.getString("cmr_status_desc_option");
		    cmr.cmr_status = rs.getString("cmr_status");
		    cmr.res_subtype = rs.getString("res_subtype");
		    cmr.res_title = rs.getString("res_title");
		    tmpVec.addElement(cmr);
		}
		ViewCmr[] result = new ViewCmr[tmpVec.size()];
		result = (ViewCmr[]) tmpVec.toArray(result);
		rs.close();
		if(stmt != null){
		    stmt.close();
		}
		return result;
	}
}