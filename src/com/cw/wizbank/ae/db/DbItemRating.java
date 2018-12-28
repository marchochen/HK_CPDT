package com.cw.wizbank.ae.db;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.ae.db.sql.*;

public class DbItemRating{
    
        public static final String ACTUAL_OVERALL_RATING = "OVRA";
        public static final String TARGET_OVERALL_RATING = "OVRT";
        public static final String RATING_TYPE_USR = "USR";

        public long irt_itm_id;
        public long irt_ent_id;
        public String irt_type;        
        public float irt_rate;
        public Timestamp irt_create_timestamp;
        public String irt_create_usr_id;
        public Timestamp irt_update_timestamp;        
        public String irt_update_usr_id;
        
        public DbItemRating(){};
        
        // irt_itm_id, irt_type must have value before save
        // irt_rate should have value before save
        //WaiLun: irt_ent_id optional pre-define
        public void save(Connection con, loginProfile prof) throws SQLException{
            Timestamp curTime = cwSQL.getTime(con);
            if( irt_ent_id == 0 )
                irt_ent_id = prof.usr_ent_id;
            irt_create_timestamp = curTime;
            irt_create_usr_id = prof.usr_id;
            irt_update_timestamp = curTime;
            irt_update_usr_id = prof.usr_id;
            ins(con);    
        }

        public void ins(Connection con) throws SQLException{
                PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_ins_itemRating);
                int para = 1;
                
                stmt.setLong(para++, irt_itm_id);
                stmt.setLong(para++, irt_ent_id);
                stmt.setString(para++, irt_type);
                stmt.setFloat(para++, irt_rate);
                stmt.setTimestamp(para++, irt_create_timestamp);
                stmt.setString(para++, irt_create_usr_id);
                stmt.setTimestamp(para++, irt_update_timestamp);
                stmt.setString(para++, irt_update_usr_id);
                                    
                if (stmt.executeUpdate()!= 1) {
                    con.rollback();
                    throw new SQLException("Failed to insert ItemRating.");
                }
                stmt.close();
        }    
        
        public void upd(Connection con) throws SQLException{
                PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_upd_itemRating);
            
                int para = 1;
                stmt.setFloat(para++, irt_rate);
                stmt.setTimestamp(para++, irt_update_timestamp);
                stmt.setString(para++, irt_update_usr_id);

                stmt.setLong(para++, irt_itm_id);
                stmt.setString(para++, irt_type);

                if (stmt.executeUpdate()!= 1) {
                    con.rollback();
                    throw new SQLException("Failed to update ItemMote.");
                }
                stmt.close();
        }

        public static Vector getByItmId(Connection con, long itm_id, String type) throws SQLException{
            Vector rateLst = new Vector();
                PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_itemRating);
                stmt.setLong(1, itm_id);
                stmt.setString(2, type);
            
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next())
                {
                    DbItemRating rate = new DbItemRating();
                    rate.irt_ent_id = rs.getInt("irt_ent_id");
                    rate.irt_rate = rs.getFloat("irt_rate");
                    rate.irt_create_timestamp = rs.getTimestamp("irt_create_timestamp");
                    rate.irt_create_usr_id = rs.getString("irt_create_usr_id");
                    rate.irt_update_timestamp = rs.getTimestamp("irt_update_timestamp");
                    rate.irt_update_usr_id = rs.getString("irt_update_usr_id");
                    rateLst.addElement(rate);
                }
                stmt.close();
                return rateLst;
        }
        
    public static float getAvgRating(Connection con, long itm_id, String type, Timestamp eff_start_date, Timestamp eff_end_date) throws SQLException{
        float avg_rating =0;
        String sql = SqlStatements.sql_get_avg_itemRating;
        boolean append_cond;
        if (eff_start_date!= null && eff_end_date != null){
            sql += " AND irt_update_timestamp between ? AND ? ";    
            append_cond = true;
        }else{
            append_cond = false;   
        }
        
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, itm_id);
        stmt.setString(2, type);
        if (append_cond){
            stmt.setTimestamp(3, eff_start_date);
            stmt.setTimestamp(4, eff_end_date);
        }
        
        ResultSet rs = stmt.executeQuery();
                
        if (rs.next())
            avg_rating = rs.getFloat("avg_rating");
        stmt.close();
        return avg_rating;
    }
    
} 
