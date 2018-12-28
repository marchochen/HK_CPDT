package com.cw.wizbank.ae.db;

import java.sql.*;
import java.util.Vector;
import com.cw.wizbank.util.*;

public class DbItemFigure{
    
    public long ifg_itm_id;
    public long ifg_fig_id;
    
    
    /**
    * Get the item id
    * pre-defined ifg_fig_id
    @return long item id
    */
    public long getItmId(Connection con) 
        throws SQLException {
            
            String SQL = " SELECT ifg_itm_id "
                       + " FROM aeItemFigure "
                       + " WHERE ifg_fig_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.ifg_fig_id);
            ResultSet rs = stmt.executeQuery();            
            if( rs.next() )
                this.ifg_itm_id = rs.getLong("ifg_itm_id");
            else
                throw new SQLException("Failed to get item figure, id = " + this.ifg_fig_id);
            stmt.close();
            return this.ifg_itm_id;
        }
    
    
    
    /**
    * Insert record to database
    *pre-define ifg_itm_id, ifg_fig_id
    */
    public void ins(Connection con)
        throws SQLException {
            
            String SQL = " INSERT INTO aeItemFigure "
                       + " ( ifg_itm_id, ifg_fig_id ) "
                       + " VALUES ( ?, ? ) ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.ifg_itm_id);
            stmt.setLong(2, this.ifg_fig_id);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to insert a Item Figure");
            stmt.close();
            return;

        }        
        
        

    /**
    * Delete record from database
    * pre-define ifg_itm_id, ifg_fig_id
    */
    public void del(Connection con)
        throws SQLException {
            
            String SQL = " DELETE FROM aeItemFigure "
                       + " WHERE ifg_itm_id = ? AND ifg_fig_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.ifg_itm_id);
            stmt.setLong(2, this.ifg_fig_id);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }


    /**
    * Delete record(s) from database
    @param vec Vector of ifg_fig_id
    * pre-define ifg_itm_id
    */
    public static void del(Connection con, Vector vec)
        throws SQLException {
            if( vec == null || vec.isEmpty() )
                return;
            String SQL = " DELETE FROM aeItemFigure "
                       + " WHERE ifg_fig_id IN " + cwUtils.vector2list(vec);
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.executeUpdate();
            stmt.close();
            return;
            
        }

    /**
    * Get figure id by item id
    @param itm_id item id
    */
    public static Vector getFigureId(Connection con, long itm_id)
        throws SQLException {
            
            String SQL = " SELECT ifg_fig_id FROM aeItemFigure "
                       + " WHERE ifg_itm_id = ? " ;
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            ResultSet rs = stmt.executeQuery();
            Vector idVec = new Vector();
            while(rs.next()){
                idVec.addElement(new Long(rs.getLong("ifg_fig_id")));
            }
            stmt.close();
            return idVec;
        }



    public static DbFigureType[] getAll(Connection con, long owner_ent_id)
        throws SQLException {
            Vector vTmp = new Vector();
            DbFigureType dbfigureType = null;
            String SQL = " SELECT fgt_id, fgt_type, fgt_subtype, fgt_xml "
                       + " FROM aeFigureType "
                       + " WHERE fgt_owner_ent_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, owner_ent_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                dbfigureType = new DbFigureType();
                dbfigureType.fgt_id = rs.getLong("fgt_id");
                dbfigureType.fgt_type = rs.getString("fgt_type");
                dbfigureType.fgt_subtype = rs.getString("fgt_subtype");
                dbfigureType.fgt_xml = cwSQL.getClobValue(rs, "fgt_xml");
                vTmp.addElement(dbfigureType);                
            }
            stmt.close();
            
            DbFigureType result[] = new DbFigureType[vTmp.size()];
            result = (DbFigureType[])vTmp.toArray(result);
            return result;
        }



    
}