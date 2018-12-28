package com.cw.wizbank.db;

import java.sql.*;
import com.cw.wizbank.util.*;

public class DbSuperviseTargetEntity{ 
    
    public static final String SPT_TYPE_DIRECT_SUPERVISE = "DIRECT_SUPERVISE";
    public static final String SPT_TYPE_SUPERVISE = "SUPERVISE";
    
    public long spt_source_usr_ent_id;
    public String spt_type;
    public long spt_target_ent_id;
    
    public Timestamp spt_create_timestamp;
    public String spt_create_usr_id;
    public Timestamp spt_syn_timestamp;
    public Timestamp spt_eff_start_datetime;
    public Timestamp spt_eff_end_datetime;
    
    public static final String SQL_INS_SUPERVISE_TARGET_ENTITY = "INSERT INTO SuperviseTargetEntity (spt_source_usr_ent_id, spt_type, spt_target_ent_id , spt_create_timestamp, spt_create_usr_id, spt_syn_timestamp , spt_eff_start_datetime, spt_eff_end_datetime ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_DEL_SUPERVISE_TARGET_ENTITY_BY_SOURCE_ENT_ID = "DELETE FROM SuperviseTargetEntity WHERE spt_source_usr_ent_id = ? ";
    public static final String SQL_DEL_SUPERVISE_TARGET_ENTITY_BY_TARGET_ENT_ID = "DELETE FROM SuperviseTargetEntity WHERE spt_target_ent_id = ? ";

//    public static final String SQL_GET_SUPERVISE_TARGET_ENTITY_BY_SOURCE_ENT_ID = "SELECT spt_source_usr_ent_id, spt_type, spt_target_ent_id , spt_syn_timestamp , spt_eff_start_datetime, spt_eff_end_datetime FROM SuperviseTargetEntity WHERE spt_source_usr_ent_id = ? ";
//    public static final String SQL_GET_SUPERVISE_TARGET_ENTITY_BY_TARGET_ENT_ID = "SELECT spt_source_usr_ent_id, spt_type, spt_target_ent_id , spt_syn_timestamp , spt_eff_start_datetime, spt_eff_end_datetime FROM SuperviseTargetEntity WHERE spt_target_ent_id = ? ";
    
    public void ins(Connection con, String upd_usr_id) throws SQLException 
    {
        Timestamp curTime = cwSQL.getTime(con);
        PreparedStatement stmt = con.prepareStatement(SQL_INS_SUPERVISE_TARGET_ENTITY);
        stmt.setLong(1, spt_source_usr_ent_id);
        stmt.setString(2, spt_type);
        stmt.setLong(3, spt_target_ent_id);
        stmt.setTimestamp(4, curTime);
        stmt.setString(5, upd_usr_id);
        stmt.setTimestamp(6, spt_syn_timestamp);
        stmt.setTimestamp(7, spt_eff_start_datetime);
        stmt.setTimestamp(8, spt_eff_end_datetime);
        
        if( stmt.executeUpdate() != 1 ){
        	stmt.close();
            throw new SQLException("Failed to insert into SuperviseTargetEntity");        	
        }                
        stmt.close();    
        return;
    } 
    /*
    public static Vector getBySourceEntId(Connection con, long source_ent_id, String type){
        Vector vtSpt = new Vector();
        String sql = SQL_GET_SUPERVISE_TARGET_ENTITY_BY_SOURCE_ENT_ID;
        if (type!=null){
            sql += " and spt_type = ? ";  
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, source_ent_id);
        if (type!=null){
            stmt.setString(2, type);
        }
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            DbSuperviseTargetEntity dbspt = new DbSuperviseTargetEntity();
            dbspt.spt_source_usr_ent_id = source_ent_id;
            dbspt.spt_type = rs.getString("spt_type");
            dbspt.spt_target_ent_id = rs.getLong("spt_target_ent_id");
            dbspt.spt_syn_timestamp = rs.getTimestamp("spt_syn_timestamp");
            dbspt.spt_eff_start_datetime = rs.getTimestamp("spt_eff_start_datetime");
            dbspt.spt_eff_end_datetime = rs.getTimestamp("spt_eff_end_datetime");
            vtSpt.addElement(dbspt);
        }
        stmt.close();
        return vtSpt;
    }
    
    public static Vector getByTargetEntId(Connection con, long target_ent_id, String type){
        Vector vtSpt = new Vector();
        String sql = SQL_GET_SUPERVISE_TARGET_ENTITY_BY_TARGET_ENT_ID;
        if (type!=null){
            sql += " and spt_type = ? ";  
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, target_ent_id);
        if (type!=null){
            stmt.setString(2, type);
        }
        ResultSet rs = stmt.executeQuery();
        
        while(rs.next()){
            DbSuperviseTargetEntity dbspt = new DbSuperviseTargetEntity();
            dbspt.spt_source_usr_ent_id = rs.getLong("spt_source_usr_ent_id");
            dbspt.spt_type = rs.getString("spt_type");
            dbspt.spt_target_ent_id = target_ent_id;
            dbspt.spt_syn_timestamp = rs.getTimestamp("spt_syn_timestamp");
            dbspt.spt_eff_start_datetime = rs.getTimestamp("spt_eff_start_datetime");
            dbspt.spt_eff_end_datetime = rs.getTimestamp("spt_eff_end_datetime");
            vtSpt.addElement(dbspt);
        }
        stmt.close();
        return vtSpt;
    }
    */
    public static void delBySourceEntId(Connection con, long source_ent_id, String type) throws SQLException{
        String sql = SQL_DEL_SUPERVISE_TARGET_ENTITY_BY_SOURCE_ENT_ID;
        if (type!=null){
            sql += " and spt_type = ? ";  
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, source_ent_id);
        if (type!=null){
            stmt.setString(2, type);
        }
        stmt.executeUpdate();
        stmt.close();
    }
    
    public static void delByTargetEntId(Connection con, long target_ent_id, String type) throws SQLException{
        String sql = SQL_DEL_SUPERVISE_TARGET_ENTITY_BY_TARGET_ENT_ID;
        if (type!=null){
            sql += " and spt_type = ? ";  
        }

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, target_ent_id);
        if (type!=null){
            stmt.setString(2, type);
        }
        stmt.executeUpdate();
        stmt.close();
        
    }
    
}