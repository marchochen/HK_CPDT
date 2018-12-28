package com.cw.wizbank.db;

import java.sql.*;
import com.cw.wizbank.util.*;

/**
A database class to manage table kmFolder
*/
public class DbKmFolder extends DbKmNode {

    /* Possible values for fld_type */
    public static final String FOLDER_TYPE_WORK = "WORK";
    public static final String FOLDER_TYPE_DOMAIN = "DOMAIN";
    
    public long         fld_nod_id;
    public String       fld_type;
    public String       fld_title;
    public String       fld_desc;
    public long         fld_obj_cnt;
    public String       fld_update_usr_id;
    public Timestamp    fld_update_timestamp;
    
    // new add by Tim   
    public String fld_nature;
    
    public long         target_nod_id;
    
    private static final String SQL_INS_KMFOLDER  = " INSERT INTO kmFolder ( " 
                                                + "  fld_nod_id, fld_type, "
                                                + "  fld_title, fld_desc, " 
                                                + "  fld_obj_cnt, fld_update_usr_id, fld_update_timestamp, fld_nature "
                                                + "  ) "
                                                + " VALUES (?,?,?,?,?,?,?,?) ";



    private static final String SQL_GET_KMFOLDER  = " SELECT fld_type, fld_title, " 
                                                + "  fld_desc, "
                                                + "  fld_obj_cnt, " 
                                                + "  fld_update_usr_id, fld_update_timestamp, fld_nature "
                                                + " FROM kmFolder WHERE fld_nod_id = ? ";

    private static final String SQL_GET_TYPE  = " SELECT fld_type " 
                                                + " FROM kmFolder WHERE fld_nod_id = ? ";

    private static final String SQL_GET_TITLE  = " SELECT fld_title " 
                                                + " FROM kmFolder WHERE fld_nod_id = ? ";

    private static final String SQL_UPD_KMFOLDER  = " UPDATE kmFolder SET "
                                                + "  fld_title = ?, "
                                                + "  fld_desc = ?, "
                                                + "  fld_update_usr_id = ?, "
                                                + "  fld_update_timestamp = ? "
                                                + " WHERE fld_nod_id = ? ";
                                  
    private static final String SQL_DEL_KMFOLDER  = " DELETE FROM kmFolder " 
                                                + " WHERE fld_nod_id = ? ";

    private static final String SQL_NATURE_BY_ID = " SELECT fld_nature FROM kmFolder "
                                                 + " WHERE fld_nod_id = ? ";


    /**
    Inser a new folder
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException, cwSysMessage {
        
        super.ins(con);
        fld_nod_id = nod_id;

        PreparedStatement stmt = null;
        int code = 0;
        try {
            //get database time
            if(fld_update_timestamp == null ) {
                fld_update_timestamp = cwSQL.getTime(con);
            }
            //try to insert 
            int index =1;
            stmt = con.prepareStatement(SQL_INS_KMFOLDER);
               
            stmt.setLong(index++, fld_nod_id);
            stmt.setString(index++, fld_type);
            stmt.setString(index++, fld_title);
            stmt.setString(index++, fld_desc);
            stmt.setLong(index++, fld_obj_cnt);
            stmt.setString(index++, fld_update_usr_id);
            stmt.setTimestamp(index++, fld_update_timestamp);
            stmt.setString(index++, fld_nature);

            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }

    /**
    Get all field of the specified folder from the database
    */
    public void get(Connection con)
        throws SQLException, cwSysMessage {
        nod_id = fld_nod_id;
        super.get(con);
        
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_KMFOLDER);
            stmt.setLong(1, fld_nod_id);

            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                fld_type             = rs.getString("fld_type");
                fld_title            = rs.getString("fld_title");
                fld_desc             = rs.getString("fld_desc");
                fld_obj_cnt          = rs.getLong("fld_obj_cnt");
                fld_update_usr_id    = rs.getString("fld_update_usr_id");
                fld_update_timestamp = rs.getTimestamp("fld_update_timestamp");
                fld_nature           = rs.getString("fld_nature");
            }
            else {
                throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " folder id = " + nod_id);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

    /**
    Get folder type of the specified folder from the database
    */
    public String getType(Connection con)
        throws SQLException, cwSysMessage {

        PreparedStatement stmt = null;
        String type = null;
        try {
            stmt = con.prepareStatement(SQL_GET_TYPE);
            stmt.setLong(1, fld_nod_id);

            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                type            = rs.getString("fld_type");
            }
            else {
                throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " folder id = " + nod_id);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return type;
    }

    /**
    Get folder title of the specified folder from the database
    */
    public String getTitle(Connection con)
        throws SQLException, cwSysMessage {

        PreparedStatement stmt = null;
        String title = null;
        try {
            stmt = con.prepareStatement(SQL_GET_TITLE);
            stmt.setLong(1, fld_nod_id);

            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                title            = rs.getString("fld_title");
            }
            else {
                throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " folder id = " + nod_id);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return title;
    }


    /**
    Update a specified folder from the database
    @return the row count for UPDATE    
    */
    public int upd(Connection con)
        throws SQLException {
        nod_id = fld_nod_id;
        super.upd(con);
        PreparedStatement stmt=null;
        int code=0;
        try {
            int index =1;
            stmt = con.prepareStatement(SQL_UPD_KMFOLDER);
            stmt.setString(index++, fld_title);
            stmt.setString(index++, fld_desc);
            stmt.setString(index++,  fld_update_usr_id);
            stmt.setTimestamp(index++,  fld_update_timestamp);
            stmt.setLong(index++,  fld_nod_id);
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }

        return code;
    }
    

    /**
    Delete a specified folder from the database
    @return the row count for DELETE    
    */
    public int del(Connection con)
        throws SQLException {
        
        nod_id = fld_nod_id;
        
        PreparedStatement stmt=null;
        int code=0;
        try {
            stmt = con.prepareStatement(SQL_DEL_KMFOLDER);
            stmt.setLong(1, fld_nod_id);
            
            code = stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }        
        
        super.del(con);
        
        return code;
    }
    
    /**
    Check the timestamp for concurrency control
    @return boolean whether the timstamp is equal or not
    */
    public boolean equalsTimstamp(Connection con)
        throws SQLException {
        
        String sql = "SELECT fld_update_timestamp From kmFolder Where fld_nod_id = " + fld_nod_id;
        return (cwSQL.equalsTimestamp(con, sql, fld_update_timestamp));

    }

 
    public static String getFolderNature(Connection con, long fld_nod_id)
        throws SQLException {
            PreparedStatement stmt = con.prepareStatement(SQL_NATURE_BY_ID);
            stmt.setLong(1, fld_nod_id);
            ResultSet rs = stmt.executeQuery();
            String nature = null;
            if(rs.next())
                nature = rs.getString("fld_nature");
            stmt.close();
            return nature;
        }
 
}

