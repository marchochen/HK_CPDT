package com.cw.wizbank.ae.db;

import java.sql.*;
import com.cw.wizbank.ae.db.sql.*;


public class DbItemBudget{

    //table column names 
    public int ibd_itm_id;
    public float ibd_target;
    public float ibd_actual;
    public Timestamp ibd_create_timestamp;
    public String ibd_create_usr_id;
    public Timestamp ibd_update_timestamp;
    public String ibd_update_usr_id;

    public DbItemBudget() {
    }  
    
    public void ins(Connection con) throws SQLException{
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_itemBudget);
        int para = 1;
            
        stmt.setInt(para++, ibd_itm_id);
        stmt.setFloat(para++, ibd_target);
        stmt.setFloat(para++, ibd_actual);
        stmt.setTimestamp(para++, ibd_create_timestamp);
        stmt.setString(para++, ibd_create_usr_id);
        stmt.setTimestamp(para++, ibd_update_timestamp);
        stmt.setString(para++, ibd_update_usr_id);
                                
        if (stmt.executeUpdate()!= 1) {
            con.rollback();
            throw new SQLException("Failed to insert ItemMote.");
        }
        stmt.close();
    }    

    // with itm_id initialized
    // assume one-one, one course, one itmMote  
    public void get(Connection con) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_itemBudget);
            stmt.setInt(1, ibd_itm_id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                ibd_itm_id = rs.getInt("ibd_itm_id");
                ibd_target = rs.getFloat("ibd_target");
                ibd_actual = rs.getFloat("ibd_actual");
                ibd_create_timestamp = rs.getTimestamp("ibd_create_timestamp");
                ibd_create_usr_id = rs.getString("ibd_create_usr_id");
                ibd_update_timestamp = rs.getTimestamp("ibd_update_timestamp");
                ibd_update_usr_id = rs.getString("ibd_update_usr_id");
            }else{
            }            
            
            stmt.close();
    }    
    
    public void updItemMote(Connection con) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_itemBudget);
            
            int para = 1;
            stmt.setFloat(para++, ibd_target);
            stmt.setFloat(para++, ibd_actual);
            stmt.setTimestamp(para++, ibd_update_timestamp);
            stmt.setString(para++, ibd_update_usr_id);

            stmt.setInt(para++, ibd_itm_id);

            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new SQLException("Failed to update ItemMote.");
            }
            stmt.close();

    } 

}