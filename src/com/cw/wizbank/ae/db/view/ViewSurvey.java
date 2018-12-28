package com.cw.wizbank.ae.db.view;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.qdb.dbModule;
/*
*   to find the run's info with the course itm_id 
*/
public class ViewSurvey{

    static public class Item{
        public long itm_id;
        public String itm_title;
        public int count;
        Item(){
            itm_title = "";    
        }        
    }

/*    public static Vector getRunLst(Connection con, long itm_id) throws cwException{
        try{
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
        } catch(SQLException e) {
            throw new cwException("SQL Error: " + e.getMessage()); 
        }                  

    } 
*/
/*
    public static Vector getRunLstEvn(Connection con, long itm_id) throws SQLException{
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
    } 
*/
    // old method, no order
    public static Vector getItmModLst(Connection con, long itm_id, long mod_id, String mod_type, Vector usrId, String orderSQL) throws SQLException{
            String SQL = null;
            if (itm_id != 0){
                SQL = "SELECT res_id, res_title, res_usr_id_owner, mod_eff_start_datetime, mod_eff_end_datetime FROM aeItem, aeItemResources, Resources , Module " + 
                        " WHERE ire_itm_id = itm_id and ire_res_id = res_id and mod_res_id = res_id " + 
                        " and itm_id = " + itm_id;
            }else{
                SQL = "SELECT res_id, res_title, res_usr_id_owner, mod_eff_start_datetime, mod_eff_end_datetime FROM Resources , Module, RegUser " + 
                        " WHERE mod_res_id = res_id and usr_id = res_usr_id_owner"; 
            }
            if (mod_id != 0){
                SQL += " AND mod_res_id = " + mod_id;
            }
            SQL += " AND mod_type = '" + mod_type + "'";
            if (orderSQL != null)
                SQL += orderSQL; 
            
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
    } 
    // for tna only, support owner sorting
    public static Vector getItmModLst(Connection con, Vector usrId, String orderSQL) throws SQLException{
            String SQL = null;
            SQL = "SELECT res_id, res_title, res_usr_id_owner, mod_eff_start_datetime, mod_eff_end_datetime FROM Resources , Module, RegUser " 
                    + " WHERE mod_res_id = res_id and usr_id = res_usr_id_owner and mod_type = 'TNA' "
                    + orderSQL;

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
    } 
    
    /*
    public class order{
        public String colName;
        public String order;
            order(){
                colName = "";
                order = "";
            }
    }
    
    public String buildOrderSQL(Vector vtOrder){
        StringBuffer SQL = new StringBuffer();
        SQL.append(" ORDER BY ");
        
        for (int i=0; i<vtOrder.size(); i++){
                
        }
        
    } */
}