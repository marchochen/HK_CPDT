package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.Vector;

public class ViewCourseEvaluationAttendance{
    
    public long app_id;
    public String usr_ste_usr_id;
    public String att_rate;
    public String cov_status;
    public String att_remark;
    public Timestamp att_timestamp;
    public String cov_score;
    public Timestamp cov_last_acc_datetime;
    public float cov_total_time;
    public String cov_comment; 
    
    public static Vector getUserResult(Connection con, long itm_id, boolean run_ind, Timestamp startTime, Timestamp endTime)
        throws SQLException{
            
        StringBuffer SQL = new StringBuffer();
        if (run_ind){
            SQL.append(" SELECT app_id, app_create_timestamp, usr_ste_usr_id, att_rate, cov_status, att_remark, att_timestamp, cov_score, cov_last_acc_datetime, cov_total_time, cov_comment FROM Course, CourseEvaluation, aeAttendance, aeApplication, aeItemRelation , RegUser ");
            SQL.append(" WHERE app_itm_id = ? ");
            SQL.append(" AND cos_itm_id = ire_parent_itm_id ");
            SQL.append(" AND att_itm_id = ire_child_itm_id ");
            SQL.append(" AND cos_res_id = cov_cos_id ");
            SQL.append(" AND cov_tkh_id = app_tkh_id ");
            SQL.append(" AND cov_ent_id = app_ent_id ");
            SQL.append(" AND cov_ent_id = usr_ent_id ");
            SQL.append(" AND app_itm_id = att_itm_id ");
            SQL.append(" AND app_id = att_app_id ");           
        }else{
            SQL.append(" SELECT app_id, app_create_timestamp, usr_ste_usr_id, att_rate, cov_status, att_remark, att_timestamp, cov_score, cov_last_acc_datetime, cov_total_time, cov_comment FROM Course, CourseEvaluation, aeAttendance, aeApplication, RegUser ");
            SQL.append(" WHERE app_itm_id = ? ");
            SQL.append(" AND cos_itm_id = att_itm_id ");
            SQL.append(" AND cos_res_id = cov_cos_id ");
            SQL.append(" AND cov_tkh_id = app_tkh_id ");
            SQL.append(" AND cov_ent_id = app_ent_id ");
            SQL.append(" AND cov_ent_id = usr_ent_id ");
            SQL.append(" AND app_itm_id = att_itm_id "); 
            SQL.append(" AND app_id = att_app_id ");
        }
        
        if( startTime != null )
            SQL.append(" AND (att_update_timestamp > ? OR cov_update_timestamp > ? ) ");
        if( endTime != null )
            SQL.append(" AND (att_update_timestamp < ? OR cov_update_timestamp < ? ) ");
            
        SQL.append(" ORDER BY app_create_timestamp ASC ");
        PreparedStatement stmt = con.prepareStatement(SQL.toString());
            int index = 1;
            stmt.setLong(index++, itm_id);
            if( startTime != null )
                stmt.setTimestamp(index++, startTime);
            if( endTime != null )
                stmt.setTimestamp(index++, endTime);
            
            Vector idResult = new Vector();
            ViewCourseEvaluationAttendance viewCea = null;
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){ 
                viewCea = new ViewCourseEvaluationAttendance();
                viewCea.app_id = rs.getLong("app_id");
                viewCea.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
                viewCea.att_rate = rs.getString("att_rate");
                viewCea.cov_status = rs.getString("cov_status");
                viewCea.att_remark = rs.getString("att_remark");
                viewCea.att_timestamp = rs.getTimestamp("att_timestamp");
                viewCea.cov_score = rs.getString("cov_score");
                viewCea.cov_last_acc_datetime = rs.getTimestamp("cov_last_acc_datetime");
                viewCea.cov_total_time = rs.getFloat("cov_total_time");
                viewCea.cov_comment = rs.getString("cov_comment");
                idResult.addElement(viewCea);
            }
            stmt.close();
            return idResult;
        }
    
 
    
}
