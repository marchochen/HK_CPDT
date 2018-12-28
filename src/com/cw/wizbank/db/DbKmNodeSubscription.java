package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;

/**
A database class to manage table kmNodeSubscription
*/
public class DbKmNodeSubscription {

    public static final String      EMAIL_SEND_TYPE_NONE = "NONE";
    public static final String      EMAIL_SEND_TYPE_IMMD = "IMMD";
    public static final String      EMAIL_SEND_TYPE_DAY = "DAY";
    public static final String      EMAIL_SEND_TYPE_WEEK = "WEEK";
    
    public static final String      SUBSCRIPTION_TYPE_DOMAIN = "DOMAIN";
    public static final String      SUBSCRIPTION_TYPE_WORK = "WORK";
    
    public long         nsb_nod_id;
    public long         nsb_usr_ent_id;
    public String       nsb_type;
    public String       nsb_email_send_type;
    public Timestamp    nsb_email_from_timestamp;
    public Timestamp    nsb_from_timestamp;
    public Timestamp    nsb_create_timestamp;

    private static final String SQL_INS         = " INSERT INTO kmNodeSubscription ( " 
                                                + "  nsb_nod_id, nsb_usr_ent_id, "
                                                + "  nsb_type, nsb_email_send_type, nsb_email_from_timestamp, " 
                                                + "  nsb_from_timestamp, nsb_create_timestamp " 
                                                + "  ) "
                                                + " VALUES (?,?,?,?,?,?,?) ";

    private static final String SQL_DEL         = " DELETE FROM kmNodeSubscription " 
                                                + " WHERE nsb_usr_ent_id = ?  AND  nsb_nod_id IN ";


    private static final String SQL_GET_CNT     = " SELECT COUNT(nsb_nod_id) FROM kmNodeSubscription " 
                                                + " WHERE nsb_nod_id = ?   AND  nsb_usr_ent_id = ?  ";


    private static final String SQL_DEL_BY_NODE = " DELETE FROM kmNodeSubscription " 
                                                + " WHERE nsb_nod_id = ? ";

    private static final String SQL_UPD         = " UPDATe kmNodeSubscription " 
                                                + "  SET nsb_from_timestamp = ?, nsb_email_from_timestamp = ? " 
                                                + " WHERE nsb_usr_ent_id = ?  AND  nsb_nod_id IN ";

    private static final String SQL_UPD_EMAIL_SEND_TIME     = " UPDATe kmNodeSubscription " 
                                                + "  SET nsb_email_from_timestamp = ? " 
                                                + " WHERE nsb_usr_ent_id = ?  AND  nsb_email_send_type = ? ";


    private static final String SQL_GET_ALL     = " SELECT nsb_type, nsb_nod_id, nsb_email_send_type, nsb_email_from_timestamp, nsb_from_timestamp From kmNodeSubscription " 
                                                + "  WHERE nsb_usr_ent_id = ? " 
                                                + " ORDER by nsb_type, nsb_nod_id ";
    /**
    Inser a new node subscription
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException {
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            int index =1;
            if (nsb_email_from_timestamp == null) {
                nsb_email_from_timestamp = cwSQL.getTime(con);
            }
            if (nsb_from_timestamp == null) {
                nsb_from_timestamp = cwSQL.getTime(con);
            }
            if (nsb_create_timestamp == null) {
                nsb_create_timestamp = cwSQL.getTime(con);
            }
            
            stmt = con.prepareStatement(SQL_INS);
            stmt.setLong(index++, nsb_nod_id);
            stmt.setLong(index++, nsb_usr_ent_id);
            stmt.setString(index++, nsb_type);
            stmt.setString(index++, nsb_email_send_type);
            stmt.setTimestamp(index++, nsb_email_from_timestamp);
            stmt.setTimestamp(index++, nsb_from_timestamp);
            stmt.setTimestamp(index++, nsb_create_timestamp);

            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }


    /**
    Check if a subscription already existed
    */
    public boolean exist(Connection con)
        throws SQLException {
        
        PreparedStatement stmt = null;
        boolean bExist = false;
        try {
            stmt = con.prepareStatement(SQL_GET_CNT);
            stmt.setLong(1, nsb_nod_id);
            stmt.setLong(2, nsb_usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) > 0) {
                    bExist = true;
                }
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return bExist;
    }

    /**
    Update subscription / Update the messages from date
    */
    public static void upd(Connection con, long[] node_lst, long usr_ent_id, Timestamp from_timestamp)
        throws SQLException {
        
        if (node_lst == null || node_lst.length ==0) {
            return;
        }
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            int index =1;
            stmt = con.prepareStatement(SQL_UPD + cwUtils.array2list(node_lst));

            stmt.setTimestamp(index++, from_timestamp);
            stmt.setTimestamp(index++, from_timestamp);
            stmt.setLong(index++, usr_ent_id);

            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

    }

    /**
    Remove subscriptions
    */
    public static void del(Connection con, long[] node_lst, long usr_ent_id)
        throws SQLException {
        
        if (node_lst == null || node_lst.length == 0) {
            return;
        }
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            stmt = con.prepareStatement(SQL_DEL + cwUtils.array2list(node_lst));
            stmt.setLong(1, usr_ent_id);

            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

    }
    
    /**
    Remove subscriptions of a node
    */
    public static void delByNode(Connection con, long node_id)
        throws SQLException {
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            stmt = con.prepareStatement(SQL_DEL_BY_NODE);
            stmt.setLong(1, node_id);

            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

    }
    
    /**
    Remove subscriptions of a node
    */
    public static void updEmailFromTime(Connection con, long usr_ent_id, String email_send_type, Timestamp email_send_timestamp)
        throws SQLException {
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            stmt = con.prepareStatement(SQL_UPD_EMAIL_SEND_TIME);
            stmt.setTimestamp(1, email_send_timestamp);
            stmt.setLong(2, usr_ent_id);
            stmt.setString(3, email_send_type);

            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

    }
    
    /**
    Remove subscriptions of a node
    */
    public static Hashtable getAll(Connection con, long usr_ent_id)
        throws SQLException {
        
        PreparedStatement stmt = null;
        Hashtable subHash = new Hashtable();
        try {
            stmt = con.prepareStatement(SQL_GET_ALL);
            stmt.setLong(1, usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DbKmNodeSubscription sub = new DbKmNodeSubscription();
                sub.nsb_nod_id = rs.getLong("nsb_nod_id");
                sub.nsb_usr_ent_id = usr_ent_id;
                sub.nsb_type = rs.getString("nsb_type");
                sub.nsb_email_send_type = rs.getString("nsb_email_send_type");
                sub.nsb_email_from_timestamp = rs.getTimestamp("nsb_email_from_timestamp");
                sub.nsb_from_timestamp = rs.getTimestamp("nsb_from_timestamp");
                subHash.put(new Long(sub.nsb_nod_id), sub);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return subHash;
    }

}

