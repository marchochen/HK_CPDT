package com.cw.wizbank.db;

import java.sql.*;
import java.util.Hashtable;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.sql.*;
import com.cw.wizbank.qdb.dbUserGroup;


public class DbUserGroup extends dbUserGroup{
    /**
    Get a list of not-in-syn usergroup (not system role)
    @param con Connection to database
    @param synTimestamp start syn timestamp 
    @param site id
    @return Hashtable with usg_ent_id as key and ent_ste_uid as value
    */
    public static Hashtable getNotInSynUsgNUid(Connection con, Timestamp synTimestamp, long siteId) 
        throws SQLException {
        Hashtable h = new Hashtable();            
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_not_syn_usergroup);
            stmt.setLong(1, siteId);
            stmt.setBoolean(2, true);
            stmt.setTimestamp(3, synTimestamp);
            ResultSet rs = stmt.executeQuery();
            String UID;            
            while(rs.next()) {
                UID = rs.getString("ent_ste_uid");
                if (UID==null){
                    UID = "ID-"+rs.getLong("ent_id");
                }
                h.put(new Long(rs.getLong("ent_id")), UID);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return h;
    }
    
    
    public static boolean hadChild(Connection con, long usg_ent_id) throws SQLException {
        Hashtable h = new Hashtable();
        PreparedStatement stmt = null;
        boolean re = false;
        try {
            stmt = con.prepareStatement(" SELECT ern_child_ent_id FROM EntityRelation WHERE ern_parent_ind = ? ");
            stmt.setLong(1, usg_ent_id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                re = true;
            }

        } finally {
            if (stmt != null)
                stmt.close();
        }
        return re;
    }
    
    /**
    get a list of usergroup without parent group
    @param connection
    @param siteId
    @return Hashtable with usg_ent_id as key and ent_ste_uid as value
    */
    public static Hashtable getOrphan(Connection con, long siteId) throws SQLException{
        Hashtable h = new Hashtable();            
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement(SqlStatements.sql_get_orphan_usg);
            stmt.setLong(1, siteId);
            stmt.setString(2, DbEntityRelation.ERN_TYPE_USG_PARENT_USG);
            stmt.setBoolean(3, true);

            ResultSet rs = stmt.executeQuery();
            String UID;
            while(rs.next()) {
                UID = rs.getString("ent_ste_uid");
                if (UID==null){
                    UID = "ID-"+rs.getLong("ent_id");
                }
                h.put(new Long(rs.getLong("ent_id")), UID);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return h;
    }
   
}