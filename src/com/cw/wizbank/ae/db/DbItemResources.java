package com.cw.wizbank.ae.db;

import java.sql.*;
import com.cw.wizbank.ae.db.sql.*;


public class DbItemResources{

    //table column names 
    
    public long ire_itm_id;
    public long ire_res_id;
    public String ire_type;

    public DbItemResources() {
    }  
    
    public void ins(Connection con) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_itemResources);
            int para = 1;
            
            stmt.setLong(para++, ire_itm_id);
            stmt.setLong(para++, ire_res_id);
            stmt.setString(para++, ire_type);
                                
            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new SQLException("Failed to insert ItemMote.");
            }
            stmt.close();
    }    
    
    public void del(Connection con) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_del_itemResources);
            int para = 1;
            
            stmt.setLong(para++, ire_itm_id);
            stmt.setLong(para++, ire_res_id);
            stmt.setString(para++, ire_type);
                                
            if (stmt.executeUpdate()!= 1) {
                con.rollback();
                throw new SQLException("Failed to insert ItemMote.");
            }
            stmt.close();
    }    

    public static long getResId(Connection con, long itm_id, String type) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_res_id_itemResources);
            int para = 1;
            long result = 0; 
            stmt.setLong(para++, itm_id);
            stmt.setString(para++, type);
            ResultSet rs = stmt.executeQuery();
                                
            if (rs.next())
            {
                result = rs.getInt(1);
            }else{
            }            
            stmt.close();
            return result;
    }    

    public static long getItmId(Connection con, long res_id, String type) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_itm_id_itemResources);
            int para = 1;
            long result = 0; 
            stmt.setLong(para++, res_id);
            stmt.setString(para++, type);
            ResultSet rs = stmt.executeQuery();
                    
            if (rs.next())
            {
                result = rs.getInt(1);
            }else{
            }            
            stmt.close();
            return result;
    }    
    
    


}