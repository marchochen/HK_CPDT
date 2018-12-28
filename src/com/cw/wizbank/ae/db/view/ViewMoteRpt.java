package com.cw.wizbank.ae.db.view;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.ae.db.sql.*;
import com.cw.wizbank.ae.db.*;
import com.cw.wizbank.ae.*;
/*
*   to find the run's info with the course itm_id 
*/
public class ViewMoteRpt{

    public static final int UNLIMITED_CAPACITY = -1;
    public static final String ACTUAL_OVERALL_RATING = "OVRA";
    public static final String TARGET_OVERALL_RATING = "OVRT";
    
/*    static public class Item{
        public long itm_id;
        public String itm_title;
        public int count;
        Item(){
            itm_title = "";    
        }        
    }
*/
    public static int getCosCapacity(Connection con, long itm_id) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_run_capacity);
            stmt.setLong(1, itm_id);

            ResultSet rs = stmt.executeQuery();
            int  totalCapacity = 0;
            int  runCapacity = 0;
            int  runCnt  =  0;  
            while (rs.next()){
                runCnt++;
                runCapacity = rs.getInt(1); 

                if  (rs.wasNull() || runCapacity == UNLIMITED_CAPACITY){
                    totalCapacity = UNLIMITED_CAPACITY;    
                }else{
                    totalCapacity += runCapacity;     
                }
            }                                
            stmt.close();
            if  (runCnt  ==  0){
                throw new SQLException("no run for ITEM " + itm_id); 
            }

            return totalCapacity;
    }    

    public static Vector getCosRating(Connection con, long itm_id, String irt_type) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_cos_rating_lst);
            stmt.setLong(1, itm_id);
            stmt.setString(2, irt_type);
            stmt.setLong(3, itm_id);
            stmt.setString(4, irt_type);
            
            ResultSet rs = stmt.executeQuery();
            Vector result = new Vector();
            
            while (rs.next()){
                DbItemRating myItmRate = new DbItemRating();
                myItmRate.irt_itm_id = rs.getLong("irt_itm_id"); 
                myItmRate.irt_rate = rs.getFloat("irt_rate");
                myItmRate.irt_type= irt_type;
                result.addElement(myItmRate);
            }                                
            stmt.close();
            return result;

    }    
    
    public static Vector getRunLst(Connection con, long itm_id) throws SQLException{
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_run_lst);
            stmt.setLong(1, itm_id);
            ResultSet rs = stmt.executeQuery();
            Vector runLst = new Vector();
            aeItem myItem = null;        
                
            while (rs.next()){
                myItem = new aeItem();                        
                myItem.itm_id = rs.getLong("itm_id");
                myItem.itm_title = rs.getString("itm_title");
                runLst.addElement(myItem);
            }    
            stmt.close();
            return runLst;
    } 
/*
    public static Vector getRunLstEvn(Connection con, long itm_id) throws cwException{
        try {
            PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_run_lst_evn);
            stmt.setLong(1, itm_id);
            ResultSet rs = stmt.executeQuery();
            Vector runLst = new Vector();
            Item myItem = null;        
                
            while (rs.next()){                
                myItem = new Item();                        
                myItem.itm_id = rs.getLong("itm_id");
                myItem.itm_title = rs.getString("itm_title");
                myItem.count = rs.getInt("evn_count");
                runLst.addElement(myItem);
            }    
            stmt.close();
            return runLst;
       } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage()); 
       }                  
    } 
    
    public static Vector getItmModLst(Connection con, long itm_id, long mod_id, String mod_type, Vector usrId) throws cwException{
        try {
            String SQL = null;
            if (itm_id != 0){
                SQL = "SELECT res_id, res_title, res_usr_id_owner, mod_eff_start_datetime, mod_eff_end_datetime FROM aeItem, aeItemResources, Resources , Module " + 
                        " WHERE ire_itm_id = itm_id and ire_res_id = res_id and mod_res_id = res_id " + 
                        " and itm_id = " + itm_id;
            }else{
                SQL = "SELECT res_id, res_title, res_usr_id_owner, mod_eff_start_datetime, mod_eff_end_datetime FROM Resources , Module " + 
                        " WHERE mod_res_id = res_id "; 
            }
            if (mod_id != 0){
                SQL += " AND mod_res_id = " + mod_id;
            }
            SQL += " AND mod_type = '" + mod_type + "'";

            PreparedStatement stmt = con.prepareStatement(SQL);
            
            ResultSet rs = stmt.executeQuery();
            Vector resLst = new Vector();
            dbModule myMod = null;        
                
            while (rs.next()){                
                myMod = new dbModule();                        
                myMod.res_id = rs.getLong("res_id");
                myMod.mod_res_id = myMod.res_id;
                myMod.res_title = rs.getString("res_title");
                myMod.res_usr_id_owner = rs.getString("res_usr_id_owner");
                myMod.mod_eff_start_datetime = rs.getTimestamp("mod_eff_start_datetime");
                myMod.mod_eff_end_datetime = rs.getTimestamp("mod_eff_end_datetime");
                resLst.addElement(myMod);
                usrId.addElement(myMod.res_usr_id_owner);
            }    
            stmt.close();
            return resLst;
       } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage()); 
       }                  
    } 
*/
}