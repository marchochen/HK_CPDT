package com.cw.wizbank.db.view;

import java.util.*;
import java.sql.*;

import com.cw.wizbank.util.cwUtils;

public class ViewUploadLog {
    public String usr_display_bil;
    public long ulg_id;
    public String ulg_process;
    public String ulg_type;
    public String ulg_subtype;
    public String ulg_file_name;
    public String ulg_desc;
    public Timestamp ulg_create_datetime;
    public String ulg_method;
    
    public static ViewUploadLog[] getHistory(Connection con, String ulg_process, String ulg_type, String ulg_subtype, String ulg_status, Vector v_ulg_id, String sort_col, String sort_order)
        throws SQLException {
        
        if( sort_order == null || sort_order.length() == 0 )
            sort_order = "DESC";
        if( sort_col == null || sort_col.length() == 0 )
            sort_col = "ulg_create_datetime";
        
        String SQL = " SELECT usr_display_bil, ulg_id, ulg_process, ulg_type, ulg_subtype, ulg_file_name, ulg_desc, ulg_create_datetime, ulg_method " 
                   + " FROM RegUSer, UploadLog "
                   + " WHERE ulg_usr_id_owner = usr_id ";

                   if( ulg_process != null && ulg_process.length() > 0 )
                        SQL += " AND ulg_process = ? ";

                   if( ulg_type != null && ulg_type.length() > 0 )
                        SQL += " AND ulg_type = ? ";

                   if( ulg_subtype != null && ulg_subtype.length() > 0 )
                        SQL += " AND ulg_subtype = ? ";

                   if( ulg_status != null && ulg_status.length() > 0 )
                        SQL += " AND ulg_status = ? ";

                   if( v_ulg_id != null ) {
                        if( !v_ulg_id.isEmpty() )
                            SQL += " AND ulg_id IN " + cwUtils.vector2list(v_ulg_id);
                        else
                            SQL += " AND ulg_id = 0 "; //Return a empty resultset
                   } 
               SQL += " Order By " + sort_col + " " + sort_order;
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector tempResult = null;
        try {
            stmt = con.prepareStatement(SQL);
            int index = 1;
            if( ulg_process != null && ulg_process.length() > 0 )
                stmt.setString(index++, ulg_process);
            if( ulg_type != null && ulg_type.length() > 0 )
                stmt.setString(index++, ulg_type);
            if( ulg_subtype != null && ulg_subtype.length() > 0 )
                stmt.setString(index++, ulg_subtype);
            if( ulg_status != null && ulg_status.length() > 0 )
                stmt.setString(index++, ulg_status);
            rs = stmt.executeQuery();
            
            tempResult = new Vector();
            while (rs.next()) {
                ViewUploadLog ulgLog = new ViewUploadLog();
                ulgLog.usr_display_bil = rs.getString("usr_display_bil");
                ulgLog.ulg_id = rs.getLong("ulg_id");
                ulgLog.ulg_process = rs.getString("ulg_process");
                ulgLog.ulg_type = rs.getString("ulg_type");
                ulgLog.ulg_subtype = rs.getString("ulg_subtype");
                ulgLog.ulg_file_name = rs.getString("ulg_file_name");
                ulgLog.ulg_desc = rs.getString("ulg_desc");
                ulgLog.ulg_create_datetime = rs.getTimestamp("ulg_create_datetime");
                ulgLog.ulg_method = rs.getString("ulg_method");
                tempResult.addElement(ulgLog);
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        ViewUploadLog result[] = new ViewUploadLog[tempResult.size()];
        result = (ViewUploadLog[])tempResult.toArray(result);
        
        return result;
    }
}
