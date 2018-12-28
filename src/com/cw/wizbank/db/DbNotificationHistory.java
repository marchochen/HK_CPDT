package com.cw.wizbank.db;

import java.sql.*;
import com.cw.wizbank.util.*;

public class DbNotificationHistory {
    
    // Item
    public static final String NF_TARGET_TYPE_ITEM = "ITEM";
    // Tracking History
    public static final String NF_TARGET_TYPE_TKH  = "TKH";
    // Item Req Due Date
    public static final String NF_TARGET_TYPE_REQDUE  = "REQDUE";
    
    //table column names
    public long     nfh_target_id;
    public String   nfh_target_type;
    public String   nfh_type;
    public Timestamp nfh_sent_timestamp;
    
    private static final String sql_ins = "Insert into NotificationHistory "
        +   " ( nfh_target_id, nfh_target_type, nfh_type, nfh_sent_timestamp ) "
        +   " values (?,?,?,?) " ;

    public void ins(Connection con) throws SQLException, cwException{

        PreparedStatement stmt = con.prepareStatement(sql_ins);
        if (nfh_sent_timestamp == null) {
            nfh_sent_timestamp = cwSQL.getTime(con);
        }
        stmt.setLong(1, nfh_target_id);
        stmt.setString(2, nfh_target_type);
        stmt.setString(3, nfh_type);
        stmt.setTimestamp(4, nfh_sent_timestamp);
                                
        if (stmt.executeUpdate()!= 1) {
            con.rollback();
            throw new cwException("Failed to create notification history.");
        }
        stmt.close();
    }    


}