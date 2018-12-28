package com.cw.wizbank.db;

import java.sql.*;
import java.util.Hashtable;
//import com.cw.wizbank.qdb.*;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.util.*;


public class DbPsnBiography {

    //table column names 
    public long pbg_ent_id;
    public String pbg_option;
    public String pbg_self_desc;
    public Timestamp pbg_create_timestamp;
    public String pbg_create_usr_id;
    public Timestamp pbg_update_timestamp;
    public String pbg_update_usr_id;
    
    public static final int _PBG_SELF_DESC_LENGTH = 500;
    public static final int _PBG_OPTION_LENGTH = 400;
    public static final String _PBG_OPTION_INTERVAL = "~";
    
    public DbPsnBiography() {
    }  
    
//    private static final String sql_ins_biography = "INSERT INTO psnBiography (pbg_ent_id, pbg_option, pbg_self_desc, pbg_create_timestamp, pbg_create_usr_id, pbg_update_timestamp, pbg_update_usr_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String sql_get_biography_by_ent_id = "SELECT pbg_option , pbg_self_desc FROM psnBiography WHERE pbg_ent_id = ? ";
    
    private static final String sql_upd_biography = "UPDATE psnBiography SET pbg_option = ? , pbg_update_timestamp = ? , pbg_update_usr_id = ? WHERE pbg_ent_id = ? ";
    private static final String sql_del_biography = "DELETE FROM psnBiography WHERE pbg_ent_id = ? ";
    /**
    *   get option list by ent_id
    */    
    public boolean getBiographyByEntId(Connection con, long pbg_ent_id) throws SQLException{
            boolean bExist;
            PreparedStatement stmt = con.prepareStatement(sql_get_biography_by_ent_id);
            stmt.setLong(1, pbg_ent_id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                pbg_option = rs.getString("pbg_option");  
                pbg_self_desc = cwSQL.getClobValue(rs, "pbg_self_desc");
                bExist = true;
            }else{
                bExist = false;
            }
            stmt.close();
            return bExist;
    }    
    
    public static boolean isExist(Connection con, long ent_id) throws SQLException{
        boolean bResult;
        PreparedStatement stmt = con.prepareStatement(sql_get_biography_by_ent_id);
        stmt.setLong(1, ent_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next())
            bResult = true;  
        else
            bResult = false;

        stmt.close();
        return bResult;
    }
    
    public void ins(Connection con, String create_usr_id) throws SQLException{
            String sql_ins_biography = "INSERT INTO psnBiography (pbg_ent_id, pbg_option, pbg_self_desc, pbg_create_timestamp, pbg_create_usr_id, pbg_update_timestamp, pbg_update_usr_id) VALUES (?, ?, null , ?, ?, ?, ?)";

            Timestamp curTime = cwSQL.getTime(con);
            
            PreparedStatement stmt = con.prepareStatement(sql_ins_biography);
            stmt.setLong(1, pbg_ent_id);
            stmt.setString(2, pbg_option);
            stmt.setTimestamp(3, curTime);
            stmt.setString(4, create_usr_id);
            stmt.setTimestamp(5, curTime);
            stmt.setString(6, create_usr_id);
                                
            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new SQLException("Failed to create biography.");
            }
            stmt.close();
            if (pbg_self_desc!=null){
                String conditions = "pbg_ent_id = " + pbg_ent_id;
                String tableName = "psnBiography";
                String[] colName = {"pbg_self_desc"};
                String[] colValue = {pbg_self_desc};
                cwSQL.updateClobFields(con, tableName, colName, colValue, conditions);
            }
    }    
    
    public void upd(Connection con, String update_usr_id) throws SQLException{
            Timestamp curTime = cwSQL.getTime(con);
            PreparedStatement stmt = con.prepareStatement(sql_upd_biography);
            stmt.setString(1, pbg_option);
            stmt.setTimestamp(2, curTime);
            stmt.setString(3, update_usr_id);
            stmt.setLong(4, pbg_ent_id);
                                
            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new SQLException("Failed to update biography.");
            }
            stmt.close();
            
            String conditions = "pbg_ent_id = " + pbg_ent_id;
            String tableName = "psnBiography";
            String[] colName = {"pbg_self_desc"};
            String[] colValue = {pbg_self_desc};
            cwSQL.updateClobFields(con, tableName, colName, colValue, conditions);

    }  
    
    public void del(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(sql_del_biography);
        stmt.setLong(1, pbg_ent_id);
        stmt.executeUpdate();
        stmt.close();        
    }
    
    public static Hashtable getUserSelfDesc(Connection con) throws SQLException {
    	Hashtable userSelfDescMap = null;
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	
    	try {
    		stmt = con.prepareStatement(SqlStatements.getUserSelfDescOfPsn());
    		rs = stmt.executeQuery();
    		while(rs.next()) {
    			if(userSelfDescMap == null) {
    				userSelfDescMap = new Hashtable();
    			}
    			String pbgSelfDesc = rs.getString("pbg_self_desc");
    			if(pbgSelfDesc != null && pbgSelfDesc.length() > _PBG_SELF_DESC_LENGTH) {
    				pbgSelfDesc = pbgSelfDesc.substring(0, _PBG_SELF_DESC_LENGTH);
    			} 
    			userSelfDescMap.put(new Long(rs.getLong("pbg_ent_id")), pbgSelfDesc);
    		}
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}
    	
    	return userSelfDescMap;
    }
    
    public static String _GET_ALL_PBG_OPTION = "select pbg_ent_id, pbg_option from psnBiography where pbg_option is not null and pbg_option <> ''";
    
    public static Hashtable getAllPbgOption(Connection con) throws SQLException {
    	Hashtable pbgOptionMap = new Hashtable();
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	
    	try {
    		stmt = con.prepareStatement(_GET_ALL_PBG_OPTION);
    		rs = stmt.executeQuery();
    		while(rs.next()) {
    			pbgOptionMap.put(new Long(rs.getLong("pbg_ent_id")), rs.getString("pbg_option"));
    		}
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}
    	
    	return pbgOptionMap;
    }
    
    public static String _UPD_PBG_OPTION = "update psnBiography set pbg_option = ? where pbg_ent_id = ?";
    
    public static void updatePbgOption(Connection con, long pbgEntId, String pbgOption) throws SQLException {
    	PreparedStatement stmt = null;
    	try {
    		stmt = con.prepareStatement(_UPD_PBG_OPTION);
    		int index = 1;
    		stmt.setString(index++, pbgOption);
    		stmt.setLong(index++, pbgEntId);
    		stmt.executeUpdate();
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}
    }
}