package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;


/**
A database class to manage table cmSkillScale
*/
public class DbCmSkillScale{
    
    public long         ssl_id;
    public String       ssl_title;
    public boolean      ssl_share_ind;
    public long         ssl_owner_ent_id;
    public String       ssl_create_usr_id;
    public Timestamp    ssl_create_timestamp;
    public String       ssl_update_usr_id;
    public Timestamp    ssl_update_timestamp;
    public String       ssl_delete_usr_id;
    public Timestamp    ssl_delete_timestamp;
    
    // indicator of whether the scale is a new one.
    public boolean      isNew;
        
    private static String SQL_SOFT_DELETE_SCALE = " UPDATE cmSkillScale SET "
                                  + "  ssl_update_usr_id = ?, " 
                                  + "  ssl_update_timestamp = ?, "
                                  + "  ssl_delete_usr_id = ?, "
                                  + "  ssl_delete_timestamp = ? "
                                  + "  WHERE ssl_id = ? ";
                                  
    private static String SQL_INS_CMSKILL_SCALE = " INSERT INTO cmSkillScale ( " 
                                  + "  ssl_title, ssl_share_ind, ssl_owner_ent_id, " 
                                  + "  ssl_create_usr_id, ssl_create_timestamp, "
                                  + "  ssl_update_usr_id, ssl_update_timestamp ) "
                                  + " VALUES (?,?,?, ?,?, ?,?) ";

    private static String SQL_GET_CMSKILL_SCALE = " SELECT ssl_id, " 
                                  + "  ssl_title, ssl_share_ind, ssl_owner_ent_id, " 
                                  + "  ssl_create_usr_id, ssl_create_timestamp, "
                                  + "  ssl_update_usr_id, ssl_update_timestamp "
                                  + " FROM cmSkillScale "
                                  + " WHERE ssl_id = ? ";


    private static String SQL_UPD_CMSKILL_SCALE = " UPDATE cmSkillScale SET "
                                  + "  ssl_title = ? , "
                                  + "  ssl_share_ind = ?, "
                                  + "  ssl_update_usr_id = ? , "
                                  + "  ssl_update_timestamp = ? "
                                  + " WHERE ssl_id = ? ";

    private static String SQL_DEL_CMSKILL_SCALE = " DELETE From cmSkillScale "
                                  + " WHERE ssl_id = ? ";


    private static final String SQL_GET_SCALE_INFO = 
       " SELECT ssl_id, ssl_title, ssl_share_ind "
     + " FROM cmSkillScale WHERE ssl_id IN " ;
     


    /**
    Inser a new skillscale
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException, cwSysMessage {
        int index =1;
        PreparedStatement stmt = con.prepareStatement(SQL_INS_CMSKILL_SCALE, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setString(index++, ssl_title);
        stmt.setBoolean(index++, ssl_share_ind);
        stmt.setLong(index++, ssl_owner_ent_id);
        stmt.setString(index++, ssl_create_usr_id);
        stmt.setTimestamp(index++, ssl_create_timestamp);
        stmt.setString(index++, ssl_update_usr_id);
        stmt.setTimestamp(index++, ssl_update_timestamp);

        int code = stmt.executeUpdate();
        ssl_id = cwSQL.getAutoId(con, stmt, "cmSkillScale", "ssl_id");
        stmt.close();
        
        return code;
    }

    /**
    Get all field of the specified skill scale from the database
    */
    public void get(Connection con)
        throws SQLException, cwSysMessage {
            
        PreparedStatement stmt = con.prepareStatement(SQL_GET_CMSKILL_SCALE);
        stmt.setLong(1, ssl_id);
            
        ResultSet rs = stmt.executeQuery();
        if( rs.next() ) {
            ssl_title           = rs.getString("ssl_title");
            ssl_share_ind      = rs.getBoolean("ssl_share_ind");
            ssl_owner_ent_id    = rs.getLong("ssl_owner_ent_id");
            ssl_create_usr_id   = rs.getString("ssl_create_usr_id");
            ssl_create_timestamp= rs.getTimestamp("ssl_create_timestamp");
            ssl_update_usr_id   = rs.getString("ssl_update_usr_id");
            ssl_update_timestamp= rs.getTimestamp("ssl_update_timestamp");
        }
        else {
            throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " scale id = " + ssl_id);
        }
        
        stmt.close();
    }

    /**
    Update a specified skill scale from the database
    @return the row count for UPDATE
    */
    public int upd(Connection con)
        throws SQLException {
        int index =1;
        PreparedStatement stmt = con.prepareStatement(SQL_UPD_CMSKILL_SCALE);
        stmt.setString(index++, ssl_title);
        stmt.setBoolean(index++, ssl_share_ind);
        stmt.setString(index++, ssl_update_usr_id);
        stmt.setTimestamp(index++, ssl_update_timestamp);
        stmt.setLong(index++, ssl_id);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }

    /**
    Delete a specified skill scale from the database
    @return the row count for DELETE    
    */
    public int del(Connection con)
        throws SQLException {
            
        PreparedStatement stmt = con.prepareStatement(SQL_DEL_CMSKILL_SCALE);
        stmt.setLong(1, ssl_id);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }
    
    /**
    Delete a specified skill scale from the database
    @return the row count for DELETE    
    */
    public String asXML()
        throws SQLException {
            
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<scale id=\"").append(ssl_id).append("\" owner_ent_id=\"")
              .append(ssl_owner_ent_id).append("\" share=\"").append(ssl_share_ind)
              .append("\">").append(cwUtils.NEWL);
        xmlBuf.append("<title>").append(cwUtils.esc4XML(ssl_title)).append("</title>").append(cwUtils.NEWL);
        xmlBuf.append("<create usr_id=\"").append(ssl_create_usr_id)
              .append("\" timestamp=\"").append(ssl_create_timestamp).append("\"/>").append(cwUtils.NEWL)
              .append("<update usr_id=\"").append(ssl_update_usr_id)
              .append("\" timestamp=\"").append(ssl_update_timestamp).append("\"/>").append(cwUtils.NEWL);
        xmlBuf.append("</scale>");
        
        return xmlBuf.toString();
    }

    /**
    Check if the timestamp are equal
    @return booelan true if the timestamp are equal, false otherwise
    */
    public boolean equalsTimestamp(Connection con)
        throws SQLException {

        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(" SELECT ssl_update_timestamp FROM CmSkillScale ")
              .append(" WHERE ssl_id = ").append(ssl_id);

        boolean isEqual = cwSQL.equalsTimestamp(con, SQLBuf.toString() , ssl_update_timestamp);
        
        return isEqual;
    }
    
    
    
    /**
    Get Scale info by the specified id
    @return result set of the skill scale
    */
    public static ResultSet getScaleByIds(Connection con, String sort_by, String order_by, Vector idVec)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET_SCALE_INFO 
                                                        + cwUtils.vector2list(idVec) 
                                                        + "ORDER BY " + order_by 
                                                        + "  " + sort_by );
            ResultSet rs = stmt.executeQuery();
            return rs;

        }

    public void softDelete(Connection con, String usr_id) throws SQLException {
        PreparedStatement stmt = null;
        try {
            Timestamp curTime = cwSQL.getTime(con);
            stmt = con.prepareStatement(SQL_SOFT_DELETE_SCALE);
            stmt.setString(1, usr_id);
            stmt.setTimestamp(2, curTime);
            stmt.setString(3, usr_id);
            stmt.setTimestamp(4, curTime);
            stmt.setLong(5, this.ssl_id);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }    

}
