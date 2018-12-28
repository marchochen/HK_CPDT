package com.cw.wizbank.db;

import java.sql.*;

public class DbQPointer {

    public long qpt_qse_mod_id;
    public long qpt_qse_order_ptr;
    public long qpt_max_ptr;

    /**
     * Update the value of the pointer if record exists
     * otherwise insert a record
     */
    public void setPointer(Connection con)
        throws SQLException {

            if( upd(con) != 1 )
                ins(con);

            return;
        }

    public int upd(Connection con)
        throws SQLException {

            String SQL = " UPDATE qPointer SET qpt_qse_order_ptr = ? , qpt_max_ptr = ? "
                       + " WHERE qpt_qse_mod_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qpt_qse_order_ptr);
            stmt.setLong(2, qpt_max_ptr);
            stmt.setLong(3, qpt_qse_mod_id);
            int count = stmt.executeUpdate();
            stmt.close();
            return count;
        }

    public void ins(Connection con)
        throws SQLException {

            String SQL = " INSERT INTO qPointer (qpt_qse_mod_id, qpt_qse_order_ptr, qpt_max_ptr)"
                       + " VALUES ( ?, ?, ? ) ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qpt_qse_mod_id);
            stmt.setLong(2, qpt_qse_order_ptr);
            stmt.setLong(3, qpt_max_ptr);
            stmt.executeUpdate();
            stmt.close();
            return;

        }


    public void del(Connection con)
        throws SQLException {
            
            String SQL = " DELETE FROM qPointer WHERE qpt_qse_mod_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qpt_qse_mod_id);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }

    public void get(Connection con)
        throws SQLException {

            String SQL = " SELECT qpt_qse_order_ptr, qpt_max_ptr "
                       + " FROM qPointer WHERE qpt_qse_mod_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qpt_qse_mod_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                qpt_qse_order_ptr = rs.getLong("qpt_qse_order_ptr");
                qpt_max_ptr = rs.getLong("qpt_max_ptr");
            } else {
                qpt_qse_order_ptr = 0;
                qpt_max_ptr = 0;
            }
            stmt.close();
            return;
        }




    /**
    * Update max. pointer in the table
    * @param number of question increased in the qSequence table
    */
    public void updMaxPointer(Connection con, long qnum)
        throws SQLException {
            
            String SQL = " UPDATE qPointer SET qpt_max_ptr = qpt_max_ptr + ? "
                       + " WHERE qpt_qse_mod_id = ? ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, qnum);
            stmt.setLong(2, qpt_qse_mod_id);
            
            stmt.executeUpdate();
            stmt.close();
            return;
        }

}
