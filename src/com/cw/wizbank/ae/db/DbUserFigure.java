package com.cw.wizbank.ae.db;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.*;

public class DbUserFigure {
    
    public long ufg_att_app_id;
    public long ufg_fig_id;

    public void ins(Connection con)
        throws SQLException {
            
            String SQL = " INSERT INTO aeUserFigure "
                       + " ( ufg_att_app_id, ufg_fig_id ) "
                       + " VALUES ( ?, ? ) ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.ufg_att_app_id);
            stmt.setLong(2, this.ufg_fig_id);
            if( stmt.executeUpdate() != 1 ){
                stmt.close();
                throw new SQLException("Failed to insert an User Figure");
            }
            stmt.close();
            return;

        }
    
    public static void del(Connection con, Vector vec)
        throws SQLException{
            if( vec == null || vec.isEmpty() )
                return;
            String SQL = " DELETE FROM aeUserFigure "
                       + " WHERE ufg_fig_id IN " + cwUtils.vector2list(vec);

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }

    
    public static void delByAppn(Connection con, long app_id)
        throws SQLException {
            
            String SQL = " DELETE FROM aeUserFigure WHERE ufg_att_app_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, app_id);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }
}
