package com.cw.wizbank.db;

//import java.util.*;
import java.sql.*;
import java.util.Hashtable;
import com.cw.wizbank.util.*;
import com.cw.wizbank.db.sql.*;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbErrMessage;


public class DbRegUser extends dbRegUser{
    /**
    Get a list of not-in-syn user (but currently active) usr_ent_id 
    @param con Connection to database
    @param synTimestamp start syn timestamp 
    @param site id
    @return Hashtable with usr_ent_id as key and usr_ste_usr_id as value
    */
    public static Hashtable getNotInSynUsrEntIdNUsrSteUsrId(Connection con, Timestamp synTimestamp, long siteId, String usr_source) 
        throws SQLException {
        Hashtable h = new Hashtable();            
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SqlStatements.sql_get_not_syn_usr_ent_id_n_ste_usr_id);
            stmt.setLong(1, siteId);
            stmt.setBoolean(2, true);
            stmt.setTimestamp(3, synTimestamp);
            stmt.setString(4, USR_STATUS_OK);
            stmt.setString(5, USR_STATUS_PENDING);
            stmt.setString(6, usr_source);
            ResultSet rs = stmt.executeQuery();
            String  UID;
            while(rs.next()) {
                UID = rs.getString("usr_ste_usr_id");
                if (UID==null){
                    UID = "ID-"+rs.getLong("usr_ent_id");
                }
                h.put(new Long(rs.getLong("usr_ent_id")), UID);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return h;
    }
    
    /**
    delete user with Recycle bin Id
    pre-define usr_ent_id
    @param connection
    @param Recycle bin Id
    */
    public void deleteUser(Connection con, String ent_delete_usr_id, Timestamp deleteTime)
    throws SQLException, qdbErrMessage, qdbException{
    	
        changeStatus(con, USR_STATUS_DELETED); 
        delAllEntityRelation(con, ent_delete_usr_id, DbEntityRelation.ERN_TYPE_USR_PARENT_USG, deleteTime);
        delAllEntityRelation(con, ent_delete_usr_id, DbEntityRelation.ERN_TYPE_USR_CURRENT_UGR, deleteTime);
        dbEntity dbEnt = new dbEntity();
        dbEnt.ent_id = usr_ent_id;
        dbEnt.del(con, ent_delete_usr_id, deleteTime);
    }
 
    /**
    get a list of active person without parent group
    @param connection
    @param siteId
    @return Hashtable with usr_ent_id as key and usr_ste_usr_id as value
    */
    public static Hashtable getOrphan(Connection con, long siteId) throws SQLException{
        Hashtable h = new Hashtable();            
        PreparedStatement stmt = null;
        try{
            stmt = con.prepareStatement(SqlStatements.sql_get_orphan_user);
            stmt.setLong(1, siteId);
            stmt.setString(2, DbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            stmt.setBoolean(3, true);
            stmt.setString(4, USR_STATUS_OK);

            ResultSet rs = stmt.executeQuery();
            String  UID;
            while(rs.next()) {
                UID  =  rs.getString("usr_ste_usr_id");
                if (UID==null){
                    UID = "ID-"+rs.getLong("usr_ent_id");
                }
                h.put(new Long(rs.getLong("usr_ent_id")), UID);
            }
        }finally{
            if (stmt!=null)     stmt.close();    
        }
        return h;
    }
    
    public static String getUserNameByAppId(Connection con,long app_id) throws SQLException{
        String usr_display_bil = null;
        String sql = "select usr_display_bil from RegUser where usr_ent_id = " +            "(select app_ent_id from aeApplication where app_id = ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,app_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            usr_display_bil = rs.getString("usr_display_bil");
        }
        pstmt.close();
        return usr_display_bil;
    }
 
    public static long getUsrEntIdByAppId(Connection con,long app_id) throws SQLException{
        long  usr_ent_id = 0;
        String sql = "select usr_ent_id from RegUser where usr_ent_id = " +
            "(select app_ent_id from aeApplication where app_id = ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setLong(1,app_id);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            usr_ent_id = rs.getLong("usr_ent_id");
        }
        pstmt.close();
        return usr_ent_id;
    }
    
    public static void updateUserSelfDesc(Connection con, long userEntId, String userSelfDesc) throws SQLException {
    	PreparedStatement stmt = null;
    	try {
    		stmt = con.prepareStatement(SqlStatements.updateUserSelfDesc());
    		int index = 1;
    		stmt.setString(index++, userSelfDesc);
    		stmt.setLong(index++, userEntId);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}
    }
    
    public static long getUsrEntIdByUsrId(Connection con,String usrId) throws SQLException{
        long  usr_ent_id = 0;
        String sql = "select usr_ent_id from RegUser where usr_id = ? " ;
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1,usrId);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            usr_ent_id = rs.getLong("usr_ent_id");
        }
        pstmt.close();
        return usr_ent_id;
    }

}