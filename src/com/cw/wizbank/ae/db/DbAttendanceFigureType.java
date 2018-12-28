package com.cw.wizbank.ae.db;

import java.sql.*;
import java.util.Hashtable;

public class DbAttendanceFigureType  {
    
    public long afg_ats_id;
    public long afg_fgt_id;
    public float afg_multiplier;
    
   
    /**
    *pre-define afg_ats_id, afg_fgt_id
    */
    public float getMultiplier(Connection con)
        throws SQLException {
            
            String SQL = " SELECT afg_multiplier FROM aeAttendanceFigureType "
                       + " WHERE afg_ats_id = ? AND afg_fgt_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.afg_ats_id);
            stmt.setLong(2, this.afg_fgt_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                this.afg_multiplier = rs.getFloat("afg_multiplier");
           rs.close();
            stmt.close();
            return this.afg_multiplier;
        }
        
    public static Hashtable getByFigureType(Connection con, long fgt_id)
        throws SQLException {
            
            Hashtable h_ats_multiple = new Hashtable();
            if( fgt_id == 0 )
                return h_ats_multiple;
            
            String SQL = " SELECT afg_fgt_id, afg_ats_id, afg_multiplier "
                       + " FROM aeAttendanceFigureType "
                       + " WHERE afg_fgt_id = ? ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, fgt_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next())
                h_ats_multiple.put(new Long(rs.getLong("afg_ats_id")), new Float(rs.getFloat("afg_multiplier")));
            stmt.close();
            return h_ats_multiple;
        }
        
}
