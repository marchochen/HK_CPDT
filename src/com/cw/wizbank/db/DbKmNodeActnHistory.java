package com.cw.wizbank.db;

import java.sql.*;
import com.cw.wizbank.util.*;

/**
A database class to manage table kmNodeActnHistory
*/
public class DbKmNodeActnHistory {

    /* Possible values for nah_type */
    public static final String TYPE_DOMAIN_MOD_OWN  = "DOMAIN_MOD_OWN";
    public static final String TYPE_DOMAIN_NEW_SUB  = "DOMAIN_NEW_SUB";
    public static final String TYPE_DOMAIN_DEL_SUB  = "DOMAIN_DEL_SUB";
    public static final String TYPE_DOMAIN_NEW_PUB  = "DOMAIN_NEW_PUB";
    public static final String TYPE_DOMAIN_DEL_PUB  = "DOMAIN_DEL_PUB";

    public static final String TYPE_WORK_MOD_OWN  = "WORK_MOD_OWN";
    public static final String TYPE_WORK_NEW_SUB  = "WORK_NEW_SUB";
    public static final String TYPE_WORK_DEL_SUB  = "WORK_DEL_SUB";
    public static final String TYPE_WORK_NEW_OBJ  = "WORK_NEW_PUB";
    public static final String TYPE_WORK_DEL_OBJ  = "WORK_DEL_PUB";
    
    public static final String TYPE_OBJ_MOD_OWN   = "OBJ_MOD_OWN";
    public static final String TYPE_OBJ_CHECKIN   = "OBJ_CHECKIN";
    public static final String TYPE_OBJ_PUBLISH   = "OBJ_PUBLISH";

    // Database field
    public long         nah_id;
    public long         nah_nod_id;
    public String       nah_type;
    public Timestamp    nah_update_timestamp;
    public String       nah_xml;
    
    private static final String SQL_INS    = " INSERT INTO kmNodeActnHistory ( " 
                                                + "  nah_nod_id, nah_type, "
                                                + "  nah_update_timestamp, "
                                                + "  nah_xml " 
                                                + "  ) "
                                                + " VALUES (?,?,?,?) ";

    private static final String SQL_DEL_BY_NODE = " DELETE FROM kmNodeActnHistory " 
                                                + " WHERE nah_nod_id = ? ";

    /**
    Inser a new node action history
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException, cwSysMessage {
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            //get database time
            if(nah_update_timestamp == null) {
                nah_update_timestamp = cwSQL.getTime(con);
            }
            
            //try to insert 
            int index =1;
            stmt = con.prepareStatement(SQL_INS, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setLong(index++, nah_nod_id);
            stmt.setString(index++, nah_type);
            stmt.setTimestamp(index++, nah_update_timestamp);
            stmt.setString(index++, nah_xml);
            code = stmt.executeUpdate();
            nah_id = cwSQL.getAutoId(con, stmt, "kmNodeActnHistory", "nah_id");
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }
    
    /**
    Remove action history of a node
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

}

