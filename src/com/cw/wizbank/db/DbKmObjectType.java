package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

/**
A database class to represent table kmObjectType 
*/
public class DbKmObjectType {

    public long         oty_owner_ent_id;
    public String       oty_code;
    public String       oty_nature;
    public long         oty_seq_no;
    public String       oty_create_usr_id;
    public Timestamp    oty_create_timestamp;

    public static final String SQL_GET_ALL_OTY = 
        " SELECT oty_owner_ent_id, oty_code, oty_seq_no, " + 
        " oty_create_usr_id, oty_create_timestamp, oty_nature " +
        " FROM kmObjectType " +
        " ORDER by oty_owner_ent_id ASC, oty_seq_no ASC ";

    public static final String SQL_GET_OTY_BY_CODE = 
        " SELECT oty_owner_ent_id, oty_code, oty_seq_no, " + 
        " oty_create_usr_id, oty_create_timestamp, oty_nature " +
        " FROM kmObjectType " +
        " WHERE oty_code = ? ";

    
    /**
    Perform a deep clone on this DbKmObjectType
    @return a new image of this DbKmObjectType
    */
    public DbKmObjectType deepClone() {
        DbKmObjectType imageObjectType = new DbKmObjectType();
        imageObjectType.oty_owner_ent_id = this.oty_owner_ent_id;
        imageObjectType.oty_code = this.oty_code;
        imageObjectType.oty_seq_no = this.oty_seq_no;
        imageObjectType.oty_create_usr_id = this.oty_create_usr_id;
        imageObjectType.oty_create_timestamp = this.oty_create_timestamp;
        return imageObjectType;
    }
    
    
    /**
    Get all the Object Types from database.
    @param con Connection to database
    @return Vector of DbObjectType
    */
    public static Vector getAllObjectTypes(Connection con) throws SQLException {
        
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            stmt = con.prepareStatement(SQL_GET_ALL_OTY);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                DbKmObjectType dbOty = new DbKmObjectType();
                dbOty.oty_owner_ent_id      = rs.getLong("oty_owner_ent_id");
                dbOty.oty_code              = rs.getString("oty_code");
                dbOty.oty_nature            = rs.getString("oty_nature");
                dbOty.oty_seq_no            = rs.getLong("oty_seq_no");
                dbOty.oty_create_usr_id     = rs.getString("oty_create_usr_id");
                dbOty.oty_create_timestamp  = rs.getTimestamp("oty_create_timestamp");
                v.addElement(dbOty);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }
        return v;
    }
    
    public static String getNature(Connection con, String code) throws SQLException {
        String nature = null;
        PreparedStatement stmt = con.prepareStatement(SQL_GET_OTY_BY_CODE);
        stmt.setString(1, code);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            nature = rs.getString("oty_nature");
        }
        
        stmt.close();

        return nature;
    }

}