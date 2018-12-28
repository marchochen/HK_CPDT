package com.cw.wizbank.ae.db.view;

import java.util.Vector;
import java.sql.*;

// deprecated class, use ViewCourseEvaluationAttendance
public class ViewItemAttendance{
    
    public String usr_ste_usr_id;
    public String ats_cov_status;
    public String att_remark;
    
    // deprecated
    public static Vector getUserResult(Connection con, long itm_id, Timestamp startTime, Timestamp endTime)
        throws SQLException{
            
            String SQL = " SELECT usr_ste_usr_id, att_remark, ats_cov_status, att_rate "
                       + " FROM aeApplication, aeAttendance, RegUser, aeAttendanceStatus "
                       + " WHERE att_app_id = app_id "
                       + " AND app_ent_id = usr_ent_id "
                       + " AND att_ats_id = ats_id "
                       + " AND att_itm_id = app_itm_id "
                       + " AND app_itm_id = ? ";
            if( startTime != null )
                SQL += " AND att_update_timestamp > ? ";
            if( endTime != null )
                SQL += " AND att_update_timestamp < ? ";
            
                SQL += " ORDER BY usr_ste_usr_id ASC ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, itm_id);
            if( startTime != null )
                stmt.setTimestamp(index++, startTime);
            if( endTime != null )
                stmt.setTimestamp(index++, endTime);
            
            Vector idResult = new Vector();
            ViewItemAttendance viewIa = null;
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){ 
                viewIa = new ViewItemAttendance();
                viewIa.usr_ste_usr_id = rs.getString("usr_ste_usr_id");                
                viewIa.att_remark = rs.getString("att_remark");
                viewIa.ats_cov_status = rs.getString("ats_cov_status");
                idResult.addElement(viewIa);
            }
            stmt.close();
            return idResult;
        }
    
    
}