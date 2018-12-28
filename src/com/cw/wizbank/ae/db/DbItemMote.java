package com.cw.wizbank.ae.db;

import java.sql.*;
import com.cw.wizbank.ae.db.sql.*;

public class DbItemMote{

    //table column names 
    public int imt_itm_id;
    public String imt_budget_cmt;
    public String imt_participant_cmt;
    public String imt_rating_cmt;
    public String imt_pos_cmt;
    public String imt_neg_cmt;
    public String imt_ist_cmt;
    public String imt_suggestion;
    public String imt_status;

    public Timestamp imt_create_timestamp;
    public String imt_create_usr_id;
    public Timestamp imt_upd_timestamp;
    public String imt_upd_usr_id;

    public DbItemMote() {
    }  
    
    public void ins(Connection con) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_itemMote);
            int para = 1;
            stmt.setInt(para++, imt_itm_id);
            stmt.setString(para++, imt_budget_cmt);
            stmt.setString(para++, imt_participant_cmt);
            stmt.setString(para++, imt_rating_cmt);
            stmt.setString(para++, imt_pos_cmt);
            stmt.setString(para++, imt_neg_cmt);
            stmt.setString(para++, imt_ist_cmt);
            stmt.setString(para++, imt_suggestion);
            stmt.setString(para++, imt_status);

            stmt.setTimestamp(para++, imt_create_timestamp);
            stmt.setString(para++, imt_create_usr_id);
            stmt.setTimestamp(para++, imt_upd_timestamp);
            stmt.setString(para++, imt_upd_usr_id);
                                
            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new SQLException("Failed to insert ItemMote.");
            }
            stmt.close();
    }    

    // with itm_id initialized
    // assume one-one, one course, one itmMote  
    public void get(Connection con) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_itemMote);
            stmt.setInt(1, imt_itm_id);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next())
            {
                imt_itm_id = rs.getInt("imt_itm_id");
                imt_budget_cmt = rs.getString("imt_budget_cmt");
                imt_participant_cmt = rs.getString("imt_participant_cmt");
                imt_rating_cmt = rs.getString("imt_rating_cmt");
                imt_pos_cmt = rs.getString("imt_pos_cmt");
                imt_neg_cmt = rs.getString("imt_neg_cmt");
                imt_ist_cmt = rs.getString("imt_ist_cmt");
                imt_suggestion = rs.getString("imt_suggestion");
                imt_status = rs.getString("imt_status");
                imt_create_timestamp = rs.getTimestamp("imt_create_timestamp");
                imt_create_usr_id = rs.getString("imt_create_usr_id");
                imt_upd_timestamp = rs.getTimestamp("imt_upd_timestamp");
                imt_upd_usr_id = rs.getString("imt_upd_usr_id");
            }else{

            }            
            
            stmt.close();
    }    
    
    public void updItemMote(Connection con) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_itemMote);
            
            int para = 1;
            stmt.setString(para++, imt_budget_cmt);
            stmt.setString(para++, imt_participant_cmt);
            stmt.setString(para++, imt_rating_cmt);
            stmt.setString(para++, imt_pos_cmt);
            stmt.setString(para++, imt_neg_cmt);
            stmt.setString(para++, imt_ist_cmt);
            stmt.setString(para++, imt_suggestion);
            stmt.setString(para++, imt_status);
            stmt.setTimestamp(para++, imt_upd_timestamp);
            stmt.setString(para++, imt_upd_usr_id);

            stmt.setInt(para++, imt_itm_id);
                                
            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new SQLException("Failed to update ItemMote.");
            }
            stmt.close();
    } 

}