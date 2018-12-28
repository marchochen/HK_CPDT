package com.cw.wizbank.qdb;

import java.sql.*;
import java.util.Vector;

public class dbEntityRelationHistory{

    public long erh_id;
    public long erh_child_ent_id;
    public long erh_ancestor_ent_id;
    public int erh_order;
    public String erh_type;
    public boolean erh_parent_ind;
    public Timestamp erh_start_timestamp;
    public Timestamp erh_end_timestamp;
    public Timestamp erh_create_timestamp;
    public String erh_create_usr_id;
    
    public boolean ins(Connection con) throws SQLException{
    	
        String insSql = "INSERT INTO EntityRelationHistory (erh_child_ent_id"
													+", erh_ancestor_ent_id"
													+", erh_order"
													+", erh_type"
													+", erh_parent_ind"
													+", erh_start_timestamp"
													+", erh_end_timestamp"
													+", erh_create_usr_id"
													+", erh_create_timestamp"
													+") values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = con.prepareStatement(insSql);
		int index = 1;
		stmt.setLong(index++, erh_child_ent_id);
		stmt.setLong(index++, erh_ancestor_ent_id);
		stmt.setInt(index++, erh_order);
		stmt.setString(index++, erh_type);
		stmt.setBoolean(index++, erh_parent_ind);
		stmt.setTimestamp(index++, erh_start_timestamp);
		stmt.setTimestamp(index++, erh_end_timestamp);
		stmt.setString(index++, erh_create_usr_id);
		stmt.setTimestamp(index++, erh_create_timestamp);

        int stmtResult=stmt.executeUpdate();
        stmt.close();
        if ( stmtResult!=1)                            
        {
            con.rollback();
            throw new SQLException("Failed to insert EntityRelationHistory.");
        }
        return true;
    }
    /**
     * just for check user's group is exist
     * @param con
     * @param child_ent_id
     * @return
     * @throws SQLException
     */
    		
    public static boolean isExistUserGroup(Connection con, long child_ent_id) throws SQLException {
        boolean isExistUserGroup = false;
        String SQL = " select * from Entity , EntityRelationHistory "
                   + " where ent_id = erh_ancestor_ent_id " 
                   + " and erh_child_ent_id = ? "
                   + " and erh_type = ? "
                   + " and erh_parent_ind = ? "
                   + " and ent_type = ? "
                   + " and ent_delete_timestamp is null";
     
        PreparedStatement stmt = con.prepareStatement(SQL);
        
        int index = 1;
        stmt.setLong(index++, child_ent_id);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, dbEntity.ENT_TYPE_USER_GROUP);
                                    
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
        	isExistUserGroup = true;
        }
        stmt.close();

        return isExistUserGroup;
    }
    
    
    public static long getParentId(Connection con, long child_ent_id) throws SQLException {
        long erh_ancestor_ent_id = 0;
        String SQL = " select erh_ancestor_ent_id from Entity , EntityRelationHistory "
                   + " where ent_id = erh_ancestor_ent_id " 
                   + " and erh_child_ent_id = ? "
                   + " and erh_type = ? "
                   + " and erh_parent_ind = ? "
                   + " and ent_type = ? ";
     
        PreparedStatement stmt = con.prepareStatement(SQL);
        
        int index = 1;
        stmt.setLong(index++, child_ent_id);
        stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        stmt.setBoolean(index++, true);
        stmt.setString(index++, dbEntity.ENT_TYPE_USER_GROUP);
                                    
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
        	erh_ancestor_ent_id = rs.getLong("erh_ancestor_ent_id");
        }
        stmt.close();

        return erh_ancestor_ent_id;
    }
    
    public static void delAll(Connection con, long erh_child_ent_id, String erh_type) throws SQLException{
    	String sql = "delete from entityRelationHistory where  erh_child_ent_id = ? and erh_type = ?";

    	PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
		stmt.setLong(index++, erh_child_ent_id);
		stmt.setString(index++, erh_type);
        stmt.executeUpdate();
        
        if(stmt != null) stmt.close();
    }
} 