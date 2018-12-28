package com.cw.wizbank.ae.db;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;

public class DbFigureType{
    
    public long fgt_id;
    public int fgt_seq_id;
    public String fgt_type;
    public String fgt_subtype;    
    public long fgt_owner_ent_id;
    public String fgt_xml;
    
    /**
    * Get from database and populate to DbFigureType object
    *pre-define fgt_id
    */
    public void get(Connection con) 
        throws SQLException {
            
            String SQL = " SELECT fgt_type, fgt_subtype, fgt_owner_ent_id, fgt_xml "
                       + " FROM aeFigureType "
                       + " WHERE fgt_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, this.fgt_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() ) {
                this.fgt_type = rs.getString("fgt_type");
                this.fgt_subtype = rs.getString("fgt_subtype");
                this.fgt_owner_ent_id = rs.getLong("fgt_owner_ent_id");
                this.fgt_xml = cwSQL.getClobValue(rs, "fgt_xml");
            }else
                throw new SQLException("Failed to get Figure type, id = " + this.fgt_id);

            stmt.close();
            return;
        }
    
    
        
        
    /**
    * Get all figure(s) type in the organization
    @param owner_ent_id organization entity id
    @return DbFigureType array 
    */
    public static DbFigureType[] getAll(Connection con, long owner_ent_id)
        throws SQLException {
            Vector vTmp = new Vector();
            DbFigureType dbfiguretype = null;
            String SQL = " SELECT fgt_id, fgt_seq_id, fgt_type, fgt_subtype, fgt_xml " 
                       + " FROM aeFigureType "
                       + " WHERE fgt_owner_ent_id = ? "
                       + " ORDER BY fgt_seq_id ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, owner_ent_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
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
    * Get figure type id by type and subtype
    @param owner_ent_id organization entity id
    @param type figure type
    @param subtype figure subtype
    @return long figure id
    */
    public static long getIdByType(Connection con, long owner_ent_id, String type, String subtype) throws SQLException, cwException{
        
        String SQL = " SELECT fgt_id " 
                       + " FROM aeFigureType "
                       + " WHERE fgt_owner_ent_id = ? AND fgt_type = ? ";

        if (subtype!=null){
            SQL += " AND fgt_subtype = ? ";
        }else{
            SQL += " AND fgt_subtype IS NULL ";
        }
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, owner_ent_id);
        stmt.setString(2, type);
        if (subtype!=null){
            stmt.setString(3, subtype);
        }
        
        long figure_type_id;
        ResultSet rs = stmt.executeQuery();
        if (rs.next()){
            figure_type_id = rs.getLong("fgt_id");
        }else{
            throw new cwException("No figure found by type : " + type + " , subtype : " + subtype);
        }
        stmt.close();
        return figure_type_id;
    }
}