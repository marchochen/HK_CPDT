package com.cw.wizbank.ae.db;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.*;

public class DbFigure {

    public long fig_id;
    public long fig_fgt_id;
    public float fig_value;
    public String fig_update_usr_id;
    public Timestamp fig_update_timestamp;
    public String fig_create_usr_id;
    public Timestamp fig_create_timestamp;

    public void ins(Connection con)
        throws SQLException {

            String SQL = " INSERT INTO aeFigure "
                       + " ( fig_fgt_id, fig_value, "
                       + "   fig_update_usr_id, fig_update_timestamp, "
                       + "   fig_create_usr_id, fig_create_timestamp ) "
                       + " VALUES ( ?, ?, ?, ?, ?, ? ) ";

            PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            int index = 1;
            stmt.setLong(index++, this.fig_fgt_id);
            stmt.setFloat(index++, this.fig_value);
            stmt.setString(index++, this.fig_update_usr_id);
            stmt.setTimestamp(index++, this.fig_update_timestamp);
            stmt.setString(index++, this.fig_create_usr_id);
            stmt.setTimestamp(index++, this.fig_create_timestamp);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to insert a figure");
            this.fig_id = cwSQL.getAutoId(con, stmt, "aeFigure", "fig_id");
            stmt.close();
            return;
        }
    
    
    
    /*
    * Get record from database and populate to DbFigure object
    * pre-define fig_id
    */
    public void get(Connection con)
        throws SQLException {
            
            String SQL = " SELECT fig_fgt_id, fig_value, "
                       + " fig_update_usr_id, fig_update_timestamp, "
                       + " fig_create_usr_id, fig_create_timestamp "
                       + " FROM aeFigure "
                       + " WHERE fig_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.fig_id);
            ResultSet rs = stmt.executeQuery();

            if( rs.next() ) {

                this.fig_fgt_id = rs.getLong("fig_fgt_id");
                this.fig_value = rs.getFloat("fig_value");
                this.fig_update_usr_id = rs.getString("fig_update_usr_id");
                this.fig_update_timestamp = rs.getTimestamp("fig_update_timestamp");
                this.fig_create_usr_id = rs.getString("fig_create_usr_id");
                this.fig_create_timestamp = rs.getTimestamp("fig_create_timestamp");
                
            } else {
            	if(rs!=null)rs.close();
                stmt.close();
                throw new SQLException("Failed to get figure, id = " + this.fig_id);
            }
            rs.close();
            stmt.close();
            return;
        }
    
    
    
    
    /**
    *Update ths record 
    */
    public void upd(Connection con)
        throws SQLException {
            
            String SQL = " UPDATE aeFigure "
                       + " SET fig_fgt_id = ?, "
                       + " fig_value = ?, "
                       + " fig_update_usr_id = ?, "
                       + " fig_update_timestamp = ? "
                       + " WHERE fig_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, this.fig_fgt_id);
            stmt.setFloat(index++, this.fig_value);
            stmt.setString(index++, this.fig_update_usr_id);
            stmt.setTimestamp(index++, this.fig_update_timestamp);
            stmt.setLong(index++, this.fig_id);
            
            if( stmt.executeUpdate() != 1 ){
                stmt.close();
                throw new SQLException("Failed to update the figure, fig_id = " + this.fig_id);
            }
            
            stmt.close();
            return;
        }
        
        
    
    
    
    /**
    * Update the figure value
    */
    public void updValue(Connection con)
        throws SQLException {
            
            String SQL = " UPDATE aeFigure "
                       + " SET fig_value = ? "
                       + " WHERE fig_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setFloat(1, this.fig_value);
            stmt.setLong(2, this.fig_id);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to update the figure, fig_id = " + this.fig_id);
            stmt.close();
            return;
        }
    

    
    /**
    * Delete record(s) from dataase
    */
    public static void del(Connection con, Vector vec)
        throws SQLException {
            
            if( vec == null || vec.isEmpty() )
                return;
            String SQL = " DELETE FROM aeFigure "
                       + " WHERE fig_id IN " 
                       + cwUtils.vector2list(vec);
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
        
}
