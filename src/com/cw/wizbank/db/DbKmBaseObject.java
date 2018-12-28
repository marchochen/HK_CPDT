package com.cw.wizbank.db;

import java.sql.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.km.library.KMLibraryObjectManager;
import com.cwn.wizbank.utils.CommonLog;

/**
A database class to manage table kmObject and kmObjectHistory
*/
public class DbKmBaseObject /*extends DbKmNode*/{

    public long         bob_nod_id;
    public String       bob_nature;
    public String       bob_code;
    public String       bob_delete_usr_id;
    public Timestamp    bob_delete_timestamp;
        
    private static final String SQL_INS_BASE_OBJECT  = " INSERT INTO kmBaseObject ( " 
                                                + "  bob_nod_id, bob_nature, "
                                                + "  bob_code " 
                                                + "  ) "
                                                + " VALUES (?,?,?) ";
                                                
    private static final String SQL_UPD_BASE_OBJECT  = " UPDATE kmBaseObject SET "
//                                                + "  bob_nature = ?, "
                                                + "  bob_code = ? "
//                                                + "  bob_delete_usr_id = ?, "
//                                                + "  bo_delete_timestamp = ? "
                                                + " WHERE bob_nod_id = ? ";

    private static final String SQL_MARK_DEL_BASE_OBJECT  = " UPDATE kmBaseObject SET "
                                                + "  bob_delete_usr_id = ?, "
                                                + "  bob_delete_timestamp = ? "
                                                + " WHERE bob_nod_id = ? ";
                                                                                          
    private static final String SQL_DEL_BASE_OBJECT  = " DELETE FROM kmBaseObject " 
                                                     + " WHERE bob_nod_id = ? ";
    
    private static final String SQL_CHECK_DEL  = " select count(*) from kmBaseObject " 
                                               + " WHERE bob_nod_id = ? and bob_delete_usr_id is not null ";
    
    private static final String SQL_GET_BASE_OBJECT  = " SELECT bob_nod_id, bob_nature, bob_code, bob_delete_usr_id, bob_delete_timestamp FROM kmBaseObject "
                                                     + " WHERE bob_nod_id = ? ";

    private static final String SQL_CHECK_CODE_EXISTED = " SELECT count(*) cnt "
                                                       + " FROM kmBaseObject "
                                                       + " WHERE bob_nature = ? and bob_delete_timestamp is null and bob_code = ? ";

    private static final String SQL_CHECK_CODE_EXISTED_EXCLUDE_SELF = " SELECT count(*) cnt "
                                                                    + " FROM kmBaseObject "
                                                                    + " WHERE bob_nature = ? and bob_delete_timestamp is null and bob_code = ? and bob_nod_id <> ? ";

     
     
     
    private static final String SQL_NOT_DELETED_ID = " SELECT bob_nod_id FROM kmBaseObject WHERE bob_delete_timestamp IS NOT NULL AND bob_nod_id IN ";
    
    public int ins(Connection con)
        throws SQLException, cwSysMessage {
        
        PreparedStatement stmt = null;
        int code = 0;
        try {
            //try to insert 
            int index =1;
            stmt = con.prepareStatement(SQL_INS_BASE_OBJECT);
               
            stmt.setLong(index++, bob_nod_id);
            stmt.setString(index++, bob_nature);
            stmt.setString(index++, bob_code);
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }   
    
    
    public int upd(Connection con)
        throws SQLException {
        PreparedStatement stmt=null;
        int code=0;
        try {
            int index =1;
            stmt = con.prepareStatement(SQL_UPD_BASE_OBJECT);
//            stmt.setString(index++, bob_nature);
            stmt.setString(index++, bob_code);
//            stmt.setString(index++,  bob_delete_usr_id);
//            stmt.setTimestamp(index++,  bob_delete_timestamp);
            stmt.setLong(index++,  bob_nod_id);
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }
    
     public int markDel(Connection con)
        throws SQLException {
        PreparedStatement stmt=null;
        int code=0;
        try {
            int index =1;
            stmt = con.prepareStatement(SQL_MARK_DEL_BASE_OBJECT);
            stmt.setString(index++,  bob_delete_usr_id);
            stmt.setTimestamp(index++,  bob_delete_timestamp);
            stmt.setLong(index++,  bob_nod_id);
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }
    
   public int del(Connection con)
        throws SQLException {
        
        PreparedStatement stmt=null;
        int code=0;
        try {
            stmt = con.prepareStatement(SQL_DEL_BASE_OBJECT);
            stmt.setLong(1, bob_nod_id);
            
            CommonLog.debug("delete base sql:" + SQL_DEL_BASE_OBJECT);
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }        
        
        return code;
    }
    
    public boolean checkDel(Connection con)
        throws SQLException {
        PreparedStatement stmt=null;
        int count = 0;
        
        try {
            int index =1;
            stmt = con.prepareStatement(SQL_CHECK_DEL);
            stmt.setLong(index++,  bob_nod_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                count = rs.getInt(1);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        if(count == 0)
            return false;
        else
            return true;
        
    }   
    
    public static String getNature(Connection con, long nod_id) throws SQLException {
        DbKmBaseObject baseObj = new DbKmBaseObject();
        baseObj.bob_nod_id = nod_id;
        baseObj.get(con);
        
        return baseObj.bob_nature;
    }    

    public void get(Connection con) throws SQLException {
        PreparedStatement stmt = con.prepareStatement(SQL_GET_BASE_OBJECT);
        stmt.setLong(1, bob_nod_id);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            bob_nature = rs.getString("bob_nature");
            bob_code = rs.getString("bob_code");
            bob_delete_usr_id = rs.getString("bob_delete_usr_id");
            bob_delete_timestamp = rs.getTimestamp("bob_delete_timestamp");
        }

        stmt.close();
    }    

   public boolean isCodeExisted(Connection con) throws SQLException {
        boolean result = true;
        
        PreparedStatement stmt = null;
        
        if (bob_nod_id != 0) {
            stmt = con.prepareStatement(SQL_CHECK_CODE_EXISTED_EXCLUDE_SELF);            
        } else {
            stmt = con.prepareStatement(SQL_CHECK_CODE_EXISTED);
        }
        
        int index = 1;
        stmt.setString(index++, KMLibraryObjectManager.KM_NATURE_LIBRARY);
        stmt.setString(index++, bob_code);

        if (bob_nod_id != 0) {
            stmt.setLong(index++, bob_nod_id);
        }
        
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            if (rs.getInt("cnt") == 0) {
                result = false;
            }
        }
 
        stmt.close();
        
        return result;        
    }
    
    /*
    public Vector getNonDeletedIdList(Connection con, String list)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_NOT_DELETED_ID + list);
            ResultSet rs = stmt.executeQuery();
            Vector vec = new Vector();
            while(rs.next()){
                vec.addElement(new Long("bob_nod_id"));
            }
            stmt.close();
            return vec;
        }
    */
}