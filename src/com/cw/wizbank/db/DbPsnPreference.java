package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

//import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;


public class DbPsnPreference {

    public static final String INVALID_TIMESTAMP_MSG = "PFR001"; //"Record modified by other user." ; 

    //table column names 
    public long pfr_ent_id;
    public String pfr_skin_id;
    public String pfr_lang;
    public Timestamp pfr_create_timestamp;
    public String pfr_create_usr_id;
    public Timestamp pfr_update_timestamp;
    public String pfr_update_usr_id;
    
    public DbPsnPreference() {
    }  
    
    private static final String sql_ins_preference = "INSERT INTO psnPreference (pfr_ent_id, pfr_skin_id, pfr_lang, pfr_create_timestamp, pfr_create_usr_id, pfr_update_timestamp, pfr_update_usr_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String sql_get_preference_by_ent_id = "SELECT pfr_skin_id, pfr_lang, pfr_create_timestamp, pfr_create_usr_id, pfr_update_timestamp, pfr_update_usr_id FROM psnPreference "+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) ":"") + " WHERE pfr_ent_id = ? ";
    
    private static final String sql_upd_preference = "UPDATE psnPreference SET pfr_skin_id = ? , pfr_lang = ? , pfr_update_timestamp = ? , pfr_update_usr_id = ? WHERE pfr_ent_id = ? ";
    private static final String sql_del_preference = "DELETE FROM psnPreference WHERE pfr_ent_id = ? ";
    /**
    *   get skin_id, lang by ent_id, return true if hasRecord
    */    
    public boolean get(Connection con) throws SQLException{
            boolean hasResult;
            
            PreparedStatement stmt = con.prepareStatement(sql_get_preference_by_ent_id);
            stmt.setLong(1, pfr_ent_id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                pfr_skin_id = rs.getString("pfr_skin_id");  
                pfr_lang = rs.getString("pfr_lang");  
                pfr_create_timestamp = rs.getTimestamp("pfr_create_timestamp");  
                pfr_create_usr_id = rs.getString("pfr_create_usr_id");  
                pfr_update_timestamp = rs.getTimestamp("pfr_update_timestamp");  
                pfr_update_usr_id = rs.getString("pfr_update_usr_id");  
                hasResult = true;
            }else{
                hasResult = false;
            }            
            stmt.close();
            return hasResult;

    }    
    
    public void ins(Connection con, String create_usr_id) throws SQLException{
            Timestamp curTime = cwSQL.getTime(con);
            PreparedStatement stmt = con.prepareStatement(sql_ins_preference);
            stmt.setLong(1, pfr_ent_id);
            stmt.setString(2, pfr_skin_id);
            stmt.setString(3, pfr_lang);
            stmt.setTimestamp(4, curTime);
            stmt.setString(5, create_usr_id);
            stmt.setTimestamp(6, curTime);
            stmt.setString(7, create_usr_id);
                                
            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new SQLException("Failed to create preference.");
            }
            stmt.close();
    }    
    
    public boolean checkTimestamp(Connection con) throws SQLException, cwSysMessage{
        PreparedStatement stmt = con.prepareStatement(
            " SELECT pfr_update_timestamp FROM psnPreference WHERE pfr_ent_id = ? " );
         
            stmt.setLong(1, pfr_ent_id);
            ResultSet rs = stmt.executeQuery();
            boolean bTSOk = false;
            if(rs.next())
            {
                Timestamp tTmp = rs.getTimestamp(1);
                tTmp.setNanos(pfr_update_timestamp.getNanos());
                if(tTmp.equals(pfr_update_timestamp))
                    bTSOk = true;
            }
            stmt.close();
            return bTSOk;
    }


    public void upd(Connection con, String update_usr_id) throws SQLException{
            Timestamp curTime = cwSQL.getTime(con);
            PreparedStatement stmt = con.prepareStatement(sql_upd_preference);
            stmt.setString(1, pfr_skin_id);
            stmt.setString(2, pfr_lang);
            stmt.setTimestamp(3, curTime);
            stmt.setString(4, update_usr_id);
            stmt.setLong(5, pfr_ent_id);
                                
            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new SQLException("Failed to update preference.");
            }
            stmt.close();
    }  
    
    public void del(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(sql_del_preference);
        stmt.setLong(1, pfr_ent_id);
        stmt.executeUpdate();
        stmt.close();        
    }
    

}