package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;

import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.db.sql.SqlStatements;
import com.cw.wizbank.db.view.ViewFmReservation;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

public class DbFmFacilityType {
    private final static boolean DEBUG = false;
    private Connection con = null;

    /**
     * constructor
     * check the connection
     */
    public DbFmFacilityType(Connection con) throws SQLException {
        if (con == null)
            throw new SQLException("connection not available");
        else
            this.con = con;
    }

    /**
     * get the parent ftp_id with the specified type_id
     * @param:    int        id of the type
     * @return:    int        id of the parent type
     */
    public int getParentType(int ftp_id) throws SQLException {
        int p_ftp = -1;
        PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_get_parent_type);
        stmt.setInt(1, ftp_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            p_ftp = rs.getInt(1);
        }
        stmt.close();
        return p_ftp;
    }

    /**
     * get the name with specified type_id
     * @param:    int        id of the type
     * @return:    String    name of the class
     */
    public String getTypeClass(int ftp_id) throws SQLException {
        String ftp_name = null;
        PreparedStatement stmt = this.con.prepareStatement(SqlStatements.sql_get_type_class);
        stmt.setInt(1, ftp_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            ftp_name = rs.getString(1);
        }
        stmt.close();
        return ftp_name;
    }

    /**
     * get all the facility type & sub_type
     * @param owner_id
     * @return
     * @throws SQLException
     */
    public Hashtable[] getAllType(int owner_id) throws SQLException {
        PreparedStatement stmt = null;
        stmt = con.prepareStatement(SqlStatements.sql_get_all_type);
        stmt.setInt(1, owner_id);

        ResultSet rs = stmt.executeQuery();

        Vector result = new Vector();
        Hashtable record = null;
        while (rs.next()) {
            record = new Hashtable();
            // fac_id, fac_status, fac_title, fac_ftp_id, rom_ftp_id sub_type_id
            record.put("type_id", rs.getString("type_id"));
            record.put("type_title_xml", rs.getString("type_title_xml"));
            record.put("ftp_main_indc", rs.getString("ftp_main_indc"));
            record.put("ftp_xsl_prefix", rs.getString("ftp_xsl_prefix"));
            record.put("sub_type_id", rs.getString("sub_type_id"));
            record.put("sub_type_title_xml", rs.getString("sub_type_title_xml"));
            result.addElement(record);
        }
        stmt.close();

        if (this.DEBUG)
        	CommonLog.debug(String.valueOf( result.size()));

        Hashtable[] resultArray = new Hashtable[result.size()];
        return (Hashtable[])result.toArray(resultArray);
    }
    
    public List getAllMainType(long owner_id) throws SQLException {
    	String sql = " SELECT ftp_id FROM fmFacilityType WHERE ftp_owner_ent_id = ? AND ftp_parent_ftp_id is null ";
    	PreparedStatement pst = null;
    	ResultSet rs = null;
    	try {
    		pst = con.prepareStatement(sql);
    		pst.setLong(1, owner_id);
    		rs = pst.executeQuery();
    		List ids = new ArrayList();
    		while (rs.next()) {
    			ids.add(new Long(rs.getLong(1)));
    		}
    		return ids;
    	} finally {
    		cwSQL.cleanUp(rs, pst);
    	}
    }
    
    /**
     *
     * @param root_ent_id
     * @return
     * @throws SQLException
     */
    public Hashtable[] getFtp(int root_ent_id) throws SQLException, cwSysMessage {
        PreparedStatement stmt = con.prepareStatement(SqlStatements.sql_get_ftpParentIsNull,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

        stmt.setInt(1,root_ent_id);

        ResultSet rs = stmt.executeQuery();

        Hashtable[] resultTb = ViewFmReservation.procResult(rs);
        stmt.close();
        return resultTb;
    }
}