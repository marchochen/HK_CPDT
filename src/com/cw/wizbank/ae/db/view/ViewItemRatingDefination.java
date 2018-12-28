package com.cw.wizbank.ae.db.view;

import java.sql.*;
import com.cw.wizbank.ae.db.sql.SqlStatements;

public class ViewItemRatingDefination{

    public static String RANGE_COL = "ird_range_xml";
    public static String Q_COL = "ird_q_xml";
    
    public static String getCol(Connection con, long root_ent_id, String colName) throws SQLException{
        String result = null;
        String sql = " SELECT " + colName + " FROM aeItemRatingDefination, acSite WHERE ste_ird_id = ird_id AND ste_ent_id = ? "; 
        String sql_4_default = " SELECT " + colName + " FROM aeItemRatingDefination WHERE ird_default_ind = 1 "; 

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, root_ent_id);        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            result = rs.getString(1);                
        }else{
            PreparedStatement stmt2 = con.prepareStatement(sql_4_default);
            ResultSet rs2 = stmt2.executeQuery();
            if (rs2.next()){
                result = rs2.getString(1);    
            }else{	  
			    stmt2.close();
                throw new SQLException("No record for rating");     
            }        
            stmt2.close();
        }
        stmt.close();
        return result;
    }    
    
    public static long getIdBySite(Connection con, long root_ent_id) throws SQLException{
        long Id;
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_itm_rate_def_by_site);
        stmt.setLong(1, root_ent_id);        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            Id = rs.getLong(1);
        }else{
            Id = 0;
        }
        stmt.close();
        return Id;
    }

}