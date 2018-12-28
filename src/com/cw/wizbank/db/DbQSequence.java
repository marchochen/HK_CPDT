package com.cw.wizbank.db;

import java.util.*;
import java.sql.*;

import com.cw.wizbank.util.cwUtils;

public class DbQSequence {

    public long qse_mod_id;
    public long qse_order;
    public long qse_que_res_id;
    public long qse_obj_id_ancestor;
    public long qse_obj_id;

    public void ins(Connection con)
        throws SQLException {

            String SQL = " INSERT INTO qSequence "
                       + " ( qse_mod_id, qse_order, qse_que_res_id, qse_obj_id_ancestor, qse_obj_id ) "
                       + " VALUES ( ?, ?, ?, ?, ? ) ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qse_mod_id);
            stmt.setLong(2, qse_order);
            stmt.setLong(3, qse_que_res_id);
            stmt.setLong(4, qse_obj_id_ancestor);
            stmt.setLong(5, qse_obj_id);
            stmt.executeUpdate();
            stmt.close();
            return;

        }
        
    public void del(Connection con)
        throws SQLException {
            
            String SQL = " DELETE FROM qSequence "
                       + " WHERE qse_mod_id = ? AND qse_order = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qse_mod_id);
            stmt.setLong(2, qse_order);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
        
    /**
    * Delete all questions belong to the module in the table
    */
    public void delAll(Connection con)
        throws SQLException {
            
            String SQL = " DELETE FROM qSequence WHERE qse_mod_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qse_mod_id);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }


    /**
    * Delete all questions belong to the module and objective in the table    
    */
    public void delByObj(Connection con)
        throws SQLException {
            
            String SQL = " DELETE FROM qSequence "
                       + " WHERE qse_mod_id = ? AND qse_obj_id_ancestor = ? ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qse_mod_id);
            stmt.setLong(2, qse_obj_id_ancestor);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
    


    /**
    * Get the module id which the question belong to
    */
    public Vector getModId(Connection con)
        throws SQLException {
            
            String SQL = " SELECT que_mod_id FROM qSequence WHERE qse_mod_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qse_mod_id);
            ResultSet rs = stmt.executeQuery();
            Vector modIdVec = new Vector();
            while( rs.next() )
                modIdVec.addElement(new Long(rs.getLong("qse_mod_id")));
            
            stmt.close();
            return modIdVec;
        }
        
        
        
    /**
    * Get order of the question in the module
    */
    public void getOrder(Connection con)
        throws SQLException {
            
            String SQL = " SELECT qse_order FROM qSequence "
                       + " WHERE qse_mod_id = ? AND qse_que_res_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qse_mod_id);
            stmt.setLong(2, qse_que_res_id);
            ResultSet rs = stmt.executeQuery();            
            if( rs.next() )
                qse_order = rs.getLong("qse_order");
            stmt.close();
            return;
        }
        

        
    /**
    * Update the question order in the module
    */
    public void updOrder(Connection con, long order)
        throws SQLException {
            
            String SQL = " UPDATE qSequence SET qse_order = ? "
                       + " WHERE qse_mod_id = ? AND qse_order = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, order);
            stmt.setLong(2, qse_mod_id);
            stmt.setLong(3, qse_order);
            stmt.executeUpdate();
            stmt.close();
            return;
        }
        
        
    /**
    * Get the question id of the specified order in the module
    * @param required order of the question in the module
    * @return Hashtable (key = order : value = quesiton id)
    */
    public static Hashtable getQueId(Connection con, long res_id, long[] order_list)
        throws SQLException {
            
            String SQL = " SELECT qse_order, qse_que_res_id "
                       + " FROM qSequence "
                       + " WHERE qse_mod_id = ? " 
                       + " AND qse_order IN " + cwUtils.array2list(order_list);
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, res_id);
            ResultSet rs = stmt.executeQuery();
            Hashtable orderTable = new Hashtable();
            while(rs.next())
                orderTable.put(new Long(rs.getLong("qse_order")), new Long(rs.getLong("qse_que_res_id")));
            stmt.close();
            return orderTable;
        }
        
}