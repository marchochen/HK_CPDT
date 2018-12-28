package com.cw.wizbank.ae.db.view;

import java.util.*;
import java.sql.*;
import com.cw.wizbank.ae.db.*;
import com.cw.wizbank.util.*;

public class ViewFigure {
    public static class ViewFigureDetail{
        public long fgt_id;
        public String fgt_type;
        public String fgt_subtype;
        public String fgt_xml;
        public long fig_id;
        public float fig_value;
        public long ifg_itm_id;
    }

    /**
    * Get figure detail
    @param fig_id
    */
    public static ViewFigureDetail[] getFigureDetail(Connection con, long fig_id)
        throws SQLException {
            Vector vTmp = new Vector();
            ViewFigureDetail viewFigureDetail = null;
            String SQL = " SELECT fig_id, fig_value, "
                       + " fgt_type, fgt_subtype, fgt_xml " 
                       + " FROM aeFigure, aeFigureType "
                       + " WHERE fig_fgt_id = fgt_id "
                       + " AND fig_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, fig_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                viewFigureDetail = new ViewFigureDetail();
                viewFigureDetail.fgt_type = rs.getString("fgt_type");
                viewFigureDetail.fgt_subtype = rs.getString("fgt_subtype");
                viewFigureDetail.fgt_xml = cwSQL.getClobValue(rs, "fgt_xml");
                viewFigureDetail.fig_id = rs.getLong("fig_id");
                viewFigureDetail.fig_value = rs.getFloat("fig_value");
                vTmp.addElement(viewFigureDetail);
            }
            stmt.close();
            
            ViewFigureDetail result[] = new ViewFigureDetail[vTmp.size()];
            result = (ViewFigureDetail[])vTmp.toArray(result);
            return result;
        }



    /**
    * Get item figure detail
    */
    public static ViewFigureDetail[] getFigureDetail(Connection con, Vector v_itm_id, String[] fgt_type, String[] fgt_subtype, String sortCol, String sortOrder)
        throws SQLException {
            Vector vTmp = new Vector();
            ViewFigureDetail viewFigureDetail = null;
            
            if( v_itm_id == null || v_itm_id.isEmpty() ) {
                v_itm_id = new Vector();
                v_itm_id.addElement(new Long(0));
            }
            String SQL = " SELECT ifg_itm_id, fig_id, fig_value, "
                       + " fgt_type, fgt_subtype, fgt_id, fgt_xml "
                       + " FROM aeFigure, aeItemFigure, aeFigureType "
                       + " WHERE ifg_itm_id IN " + cwUtils.vector2list(v_itm_id)
                       + " AND ifg_fig_id = fig_id "
                       + " AND fig_fgt_id = fgt_id ";
                       
                       
                       if( fgt_type != null && fgt_type.length > 0 ) {
                            SQL += " AND fgt_type IN ( ? ";
                            for(int i=1; i<fgt_type.length; i++)
                                SQL += " ,? ";
                            SQL += " ) ";
                       }
                        
                       if( fgt_subtype != null && fgt_subtype.length > 0 ) {
                            SQL += " AND fgt_subtype IN ( ? ";
                            for(int i=1; i<fgt_subtype.length; i++)
                                SQL += " ,? ";
                            SQL += " ) ";
                       }
                       
                       if( sortCol != null && sortOrder != null )
                            SQL += " ORDER BY " + sortCol + " " + sortOrder;
                       else
                            SQL += " ORDER BY fig_id ASC ";


            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            if( fgt_type != null && fgt_type.length > 0 ) {
                for(int i=0; i<fgt_type.length; i++) {
                    stmt.setString(index, fgt_type[i]);
                    index++;
                }
            }
            
            if( fgt_subtype != null && fgt_subtype.length > 0 ) {
                for(int i=0; i<fgt_subtype.length; i++) {
                    stmt.setString(index, fgt_subtype[i]);
                    index++;
                }
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                viewFigureDetail = new ViewFigureDetail();
                viewFigureDetail.fgt_type = rs.getString("fgt_type");
                viewFigureDetail.fgt_subtype = rs.getString("fgt_subtype");
                viewFigureDetail.fgt_id = rs.getLong("fgt_id");
                viewFigureDetail.fgt_xml = cwSQL.getClobValue(rs, "fgt_xml");
                viewFigureDetail.fig_id = rs.getLong("fig_id");
                viewFigureDetail.fig_value = rs.getFloat("fig_value");
                viewFigureDetail.ifg_itm_id = rs.getLong("ifg_itm_id");
                vTmp.addElement(viewFigureDetail);
            }
            stmt.close();
            
            ViewFigureDetail result[] = new ViewFigureDetail[vTmp.size()];
            result = (ViewFigureDetail[])vTmp.toArray(result);
            return result;
            
        }

    
    
    
    
    
    
    
    /**
    * Get item figure value
    */
    public static DbFigure[] getItemFigure(Connection con, long itm_id, long fgt_id)
        throws SQLException {
            Vector vTmp = new Vector();
            DbFigure dbfigure = null;
            String SQL = " SELECT fig_id, fig_fgt_id, fig_value "
                       + " FROM aeFigure, aeItemFigure "
                       + " WHERE ifg_itm_id = ? " 
                       + " AND ifg_fig_id = fig_id ";

                   if( fgt_id != 0 )
                    SQL += " AND fig_fgt_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            if( fgt_id != 0 )
                stmt.setLong(2, fgt_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                dbfigure = new DbFigure();
                dbfigure.fig_id = rs.getLong("fig_id");
                dbfigure.fig_fgt_id = rs.getLong("fig_fgt_id");
                dbfigure.fig_value = rs.getFloat("fig_value");
                vTmp.addElement(dbfigure);
            }
            rs.close();
            stmt.close();
            
            DbFigure result[] = new DbFigure[vTmp.size()];
            result = (DbFigure[])vTmp.toArray(result);
            return result;
        }

    public static DbFigure[] getItemFigure(Connection con, Vector v_itm_id, long fgt_id)
        throws SQLException {
            Vector vTmp = new Vector();
            DbFigure dbfigure = null;
            
            if( v_itm_id == null || v_itm_id.isEmpty() ){
                v_itm_id = new Vector();
                v_itm_id.addElement(new Long(0));
            }
            String SQL = " SELECT fig_id, fig_fgt_id, fig_value "
                       + " FROM aeFigure, aeItemFigure "
                       + " WHERE ifg_itm_id IN " + cwUtils.vector2list(v_itm_id)
                       + " AND ifg_fig_id = fig_id ";

                   if( fgt_id != 0 )
                    SQL += " AND fig_fgt_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            if( fgt_id != 0 )
                stmt.setLong(1, fgt_id);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()){
                dbfigure = new DbFigure();
                dbfigure.fig_id = rs.getLong("fig_id");
                dbfigure.fig_fgt_id = rs.getLong("fig_fgt_id");
                dbfigure.fig_value = rs.getFloat("fig_value");
                vTmp.addElement(dbfigure);
            }
            stmt.close();
            
            DbFigure result[] = new DbFigure[vTmp.size()];
            result = (DbFigure[])vTmp.toArray(result);
            return result;
        }

    public static DbFigureType[] getItemFigureDetail(Connection con, long itm_id, long fgt_id)
        throws SQLException {
            Vector vTmp = new Vector();
            DbFigureType dbfiguretype = null;
            
            String SQL = " SELECT fgt_id, fgt_seq_id, fgt_type, fgt_subtype, fgt_xml "
                       + " FROM aeFigure, aeItemFigure, aeFigureType "
                       + " WHERE ifg_itm_id = ? " 
                       + " AND ifg_fig_id = fig_id "
                       + " AND fig_fgt_id = fgt_id ";

                   if( fgt_id != 0 )
                    SQL += " AND fig_fgt_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            if( fgt_id != 0 )
                stmt.setLong(2, fgt_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                dbfiguretype = new DbFigureType();
                dbfiguretype.fgt_id = rs.getLong("fgt_id");
                dbfiguretype.fgt_seq_id = rs.getInt("fgt_seq_id");
                dbfiguretype.fgt_type = rs.getString("fgt_type");
                dbfiguretype.fgt_subtype = rs.getString("fgt_subtype");
                dbfiguretype.fgt_xml = cwSQL.getClobValue(rs, "fgt_xml");
                vTmp.addElement(dbfiguretype);
            }
            stmt.close();
            
            DbFigureType result[] = new DbFigureType[vTmp.size()];
            result = (DbFigureType[])vTmp.toArray(result);
            return result;

        }







    /**
     * @deprecated (2003-07-31 kawai)
     * please modify to not returning a ResultSet if this method is to be reused
     */
    /**
    * Get the item figure effective period by figure type
    */
    /*
    public static ResultSet getItemFigureEffPeriod(Connection con, long itm_id, long fgt_id)
        throws SQLException {

            String SQL = " SELECT fig_eff_start_datetime, fig_eff_end_datetime "
                       + " FROM aeFigure, aeItemFigure "
                       + " WHERE ifg_itm_id = ? "
                       + " AND ifg_fig_id = fig_id "
                       + " AND fig_fgt_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            stmt.setLong(2, fgt_id);
            ResultSet rs = stmt.executeQuery();
            return rs;
        }
    */
    
    
    
    
    
    /**
     * @deprecated (2003-07-31 kawai)
     * please modify to not returning a ResultSet if this method is to be reused
     */
    /*
    public static ResultSet getUserItemFigure(Connection con, long usr_ent_id, long fgt_id)
        throws SQLException {
            
            String SQL = " SELECT fig_fgt_id, fig_id "
                       + " FROM aeUserFigure, aeItemFigure "
                       + " WHERE uad_itm_id = ? "
                       + " AND uad_icv_id = icv_id ";
                       if( usr_ent_id != 0 )
                        SQL += " AND  uad_usr_ent_id = ? ";
                       if( ict_id != 0 )
                        SQL += " AND icv_ict_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setLong(index++, itm_id);
            if( usr_ent_id != 0 )
                stmt.setLong(index++, usr_ent_id);            
            if( ict_id != 0 )
                stmt.setLong(index++, ict_id);
            ResultSet rs = stmt.executeQuery();
            return rs;
            
        }
    */
 
 
 
    /**
    * Get user figure value
    */
    public static DbFigure[] getUserFigureValue(Connection con, long app_id)
        throws SQLException {
            Vector vTmp = new Vector();
            DbFigure dbfigure = null;
            
            String SQL = " SELECT fig_id, fig_fgt_id, fig_value "
                       + " FROM aeUserFigure, aeFigure "
                       + " WHERE ufg_att_app_id = ? "
                       + " AND ufg_fig_id = fig_id ";
                       
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, app_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                dbfigure = new DbFigure();
                dbfigure.fig_id = rs.getLong("fig_id");
                dbfigure.fig_fgt_id = rs.getLong("fig_fgt_id");
                dbfigure.fig_value = rs.getFloat("fig_value");
                vTmp.addElement(dbfigure);
            }
            rs.close();
            stmt.close();
            
            DbFigure result[] = new DbFigure[vTmp.size()];
            result = (DbFigure[])vTmp.toArray(result);
            return result;
        }

    public static Vector getUserFigureIdByItemId(Connection con, long itm_id, long fgt_id)
        throws SQLException {
            
            String SQL = " SELECT ufg_fig_id "
                       + " FROM aeUserFigure, aeApplication, aeFigure "
                       + " WHERE ufg_att_app_id = app_id "
                       + " AND ufg_fig_id = fig_id "
                       + " AND app_itm_id = ? ";
                       if( fgt_id > 0 )
                        SQL += " AND fig_fgt_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);
            if( fgt_id > 0 )
                stmt.setLong(2, fgt_id);
            ResultSet rs = stmt.executeQuery();
            Vector v_fig_id = new Vector();
            while(rs.next()){
                v_fig_id.addElement(new Long(rs.getLong("ufg_fig_id")));
            }
            stmt.close();
            return v_fig_id;
        }



    /**
    * Get user figure detail
    */
    public static ViewFigureDetail[] getUserFigureDetail(Connection con, long app_id)
        throws SQLException {
            Vector vTmp = new Vector();
            ViewFigureDetail viewFigureDetail = null;
            
            String SQL = " SELECT fig_id, fig_value, fgt_id, fgt_type, fgt_subtype " 
                       + " FROM aeUserFigure, aeFigure, aeFigureType "
                       + " WHERE ufg_att_app_id = ? "
                       + " AND ufg_fig_id = fig_id "
                       + " AND fig_fgt_id = fgt_id ";
                       
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, app_id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                viewFigureDetail = new ViewFigureDetail();
                viewFigureDetail.fgt_id = rs.getLong("fgt_id");
                viewFigureDetail.fgt_type = rs.getString("fgt_type");
                viewFigureDetail.fgt_subtype = rs.getString("fgt_subtype");
                viewFigureDetail.fig_id = rs.getLong("fig_id");
                viewFigureDetail.fig_value = rs.getFloat("fig_value");
                vTmp.addElement(viewFigureDetail);
            }
            stmt.close(); 
            
            ViewFigureDetail result[] = new ViewFigureDetail[vTmp.size()];
            result = (ViewFigureDetail[])vTmp.toArray(result);
            return result;
        }


    public static DbFigure[] getAllItemFigure(Connection con, long itm_id)
        throws SQLException, cwException {
            Vector vTmp = new Vector();
            DbFigure dbfigure = null;
            
            String SQL = " SELECT fig_id, fig_fgt_id, fig_value "
                       + " FROM aeFigure, aeItemFigure "
                       + " WHERE ifg_fig_id = fig_id "
                       + " AND ifg_itm_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, itm_id);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                dbfigure = new DbFigure();
                dbfigure.fig_id = rs.getLong("fig_id");
                dbfigure.fig_fgt_id = rs.getLong("fig_fgt_id");
                dbfigure.fig_value = rs.getFloat("fig_value");
                vTmp.addElement(dbfigure);
            }
            stmt.close();
            
            DbFigure result[] = new DbFigure[vTmp.size()];
            result = (DbFigure[])vTmp.toArray(result);
            return result;
        }



    public static Vector getNotInListItemFigId(Connection con, Vector v_itm_id, Vector v_fgt_id)
        throws SQLException{
            Vector vFigId = new Vector();
            String SQL = " SELECT fig_id "
                       + " FROM aeItemFigure, aeFigure "
                       + " WHERE ifg_fig_id = fig_id "
                       + " AND ifg_itm_id IN " + cwUtils.vector2list(v_itm_id)
                       + " AND fig_fgt_id NOT IN " + cwUtils.vector2list(v_fgt_id);
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                vFigId.addElement(new Long(rs.getLong("fig_id")));
            }
            stmt.close();
            return vFigId;
        }
    
    
    public static Vector getNotInListUserFigId(Connection con, Vector v_itm_id, Vector v_fgt_id)
        throws SQLException{
            Vector vFigId = new Vector();
            
            String SQL = " Select fig_id "
                       + " FROM aeUserFigure, aeApplication, aeFigure "
                       + " WHERE ufg_fig_id = fig_id "
                       + " AND ufg_att_app_id = app_id "
                       + " AND app_itm_id IN " + cwUtils.vector2list(v_itm_id)
                       + " AND fig_fgt_id NOT IN " + cwUtils.vector2list(v_fgt_id);
            PreparedStatement stmt = con.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                vFigId.addElement(new Long(rs.getLong("fig_id")));
            }
            stmt.close();
            return vFigId;
        }

}