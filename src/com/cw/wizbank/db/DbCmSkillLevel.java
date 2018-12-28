package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.cwUtils;

/**
A database class to manage table cmSkillLevel
*/
public class DbCmSkillLevel{
    
    public long         sle_ssl_id;
    public int          sle_level;
    public String       sle_label;
    public String       sle_description;

    private static String SQL_GET_MAX_SLE_LEVEL = " select max(sle_level) as max_level from cmSkillLevel where sle_ssl_id = ? ";
    
    private static String SQL_INS_CMSKILL_LEVEL = " INSERT INTO cmSkillLevel ( " 
                                  + " sle_ssl_id,  sle_level, sle_label, sle_description ) " 
                                  + " VALUES (?,?,?,?) ";

    private static String SQL_UPD_CMSKILL_LEVEL = " UPDATE cmSkillLevel SET "
                                  + "  sle_label = ? , "
                                  + "  sle_description = ?  "
                                  + " WHERE sle_ssl_id = ? "
                                  + "   AND sle_level = ? " ;

    private static String SQL_GET_CMSKILL_LEVEL = " SELECT sle_level, "
                                + "  sle_label, sle_description "
                                + " FROM cmSkillLevel  ";
                                  
    private static String SQL_DEL_CMSKILL_SCALE = " DELETE From cmSkillLevel "
                                  + " WHERE sle_ssl_id = ? AND sle_level = ? ";

    private static String SQL_DEL_ALL_LEVEL = " DELETE From cmSkillLevel "
                                  + " WHERE sle_ssl_id = ? ";
                                  
    private static final String SQL_GET = " SELECT sls_label, sle_description "
                                    +     " FROM cmSkillLevel "
                                    +     " WHERE sle_ssl_id = ? AND sle_level = ? ";

    /**
    Inser a new skill level
    @return the row count for INSERT
    */
    public int ins(Connection con)
        throws SQLException {
        int index =1;
        PreparedStatement stmt = con.prepareStatement(SQL_INS_CMSKILL_LEVEL);
        stmt.setLong(index++, sle_ssl_id);
        stmt.setInt(index++, sle_level);
        stmt.setString(index++, sle_label);
        stmt.setString(index++, sle_description);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }

    /**
    Update a specified skill scale from the database
    @return the row count for UPDATE
    */
    public int upd(Connection con)
        throws SQLException {
        int index =1;
        PreparedStatement stmt = con.prepareStatement(SQL_UPD_CMSKILL_LEVEL);
        stmt.setString(index++, sle_label);
        stmt.setString(index++, sle_description);
        stmt.setLong(index++, sle_ssl_id);
        stmt.setInt(index++, sle_level);
        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }

    /**
    Delete a specified skill scale and skill level from the database
    @return the row count for DELETE
    */
    public int del(Connection con)
        throws SQLException {
            
        PreparedStatement stmt = con.prepareStatement(SQL_DEL_CMSKILL_SCALE);
        stmt.setLong(1, sle_ssl_id);
        stmt.setInt(2, sle_level);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }
    
    /**
    Get detial of the skill level
    */
    public void get(Connection con)
        throws SQLException {
            
            PreparedStatement stmt = con.prepareStatement(SQL_GET);
            stmt.setLong(1, sle_ssl_id);
            stmt.setLong(2, sle_level);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                sle_label = rs.getString("sle_label");
                sle_description = rs.getString("sle_description");
            }
            stmt.close();
            return;            
        }
    
    /**
    Select a specified skill scale from the database
    @return a Vector of DbCmSkillLevel which is order by level
    */
    public Vector getById(Connection con)
        throws SQLException {
        
        Vector skillLevelVec = new Vector();
        StringBuffer SQLBuf = new StringBuffer();
        SQLBuf.append(SQL_GET_CMSKILL_LEVEL)
              .append(" WHERE sle_ssl_id = ? ")
              .append(" ORDER BY sle_level ");
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, sle_ssl_id);
        
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            DbCmSkillLevel sle = new DbCmSkillLevel();
            sle.sle_ssl_id = sle_ssl_id;
            sle.sle_level = rs.getInt("sle_level");
            sle.sle_label = rs.getString("sle_label");
            sle.sle_description = rs.getString("sle_description");
            skillLevelVec.addElement(sle);
        }
        stmt.close();
        return skillLevelVec;
    }

    /**
    Delete a specified skill scale from the database
    @return the row count for DELETE
    */
    public int delAllLevel(Connection con)
        throws SQLException {
            
        PreparedStatement stmt = con.prepareStatement(SQL_DEL_ALL_LEVEL);
        stmt.setLong(1, sle_ssl_id);

        int code = stmt.executeUpdate();
        stmt.close();
        return code;
    }

    /** 
    Get the xml of an scale and level
    */
    public String asXML() {
        
        StringBuffer xmlBuf = new StringBuffer();
        xmlBuf.append("<level scale_id=\"").append(sle_ssl_id).append("\" value=\"").append(sle_level)
                .append("\" label=\"").append(cwUtils.esc4XML(sle_label)).append("\">")
                .append(cwUtils.esc4XML(sle_description)).append("</level>").append(cwUtils.NEWL);
    
        return xmlBuf.toString();
    
    }

    /**
    Get maximum level of input scale
    */
    public static int getMaxLevel(Connection con, long scale_id) throws SQLException {
        PreparedStatement stmt = null;
        int max=0;
        try {
            stmt = con.prepareStatement(SQL_GET_MAX_SLE_LEVEL);
            stmt.setLong(1, scale_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                max = rs.getInt("max_level");
            }
        }finally {
            if(stmt!=null)stmt.close();
        }
        return max;        
    }
}
