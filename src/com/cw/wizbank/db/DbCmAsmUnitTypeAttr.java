package com.cw.wizbank.db;

import java.sql.*;
import java.util.*;

public class DbCmAsmUnitTypeAttr {
    
    public long aua_asm_id;
    public String aua_asu_type;
    public Timestamp aua_eff_start_timestamp;
    public Timestamp aua_eff_end_timestamp;
    
    private static final String SQL_GET_AUA = 
          " SELECT aua_asm_id, aua_asu_type, "
        + " aua_eff_start_timestamp, aua_eff_end_timestamp "
        + " FROM cmAsmUnitTypeAttr "
        + " WHERE aua_asm_id = ? "
        + " AND aua_asu_type = ? ";
    /**
    get from table cmAsmUnitTypeAttr into this object
    pre-define class variables:
    aua_asm_id
    aua_asu_type
    @param con Connection to database
    */
    
    public void get(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_AUA);
            stmt.setLong(1, this.aua_asm_id);
            stmt.setString(2, this.aua_asu_type);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                this.aua_eff_start_timestamp = rs.getTimestamp("aua_eff_start_timestamp");
                this.aua_eff_end_timestamp = rs.getTimestamp("aua_eff_end_timestamp");
            } 
            stmt.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }
    
    public boolean isExist(Connection con) throws SQLException {
        boolean returnVal = false;
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_AUA);
            stmt.setLong(1, this.aua_asm_id);
            stmt.setString(2, this.aua_asu_type);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                returnVal = true;
            } 
            stmt.close();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return returnVal;
    }
        
    private static final String SQL_INS_AUA = 
          " INSERT INTO cmAsmUnitTypeAttr "
        + " ( aua_asm_id, aua_asu_type "
        + " , aua_eff_start_timestamp, aua_eff_end_timestamp) " 
        + " VALUES (?, ?, ?, ?) ";
    /**
    insert this object into table cmAsmUnitTypeAttr
    pre-define class variables:
    aua_asm_id
    aua_asu_type
    aua_eff_start_timestamp
    aua_eff_end_timestamp
    @param con Connection to database
    */
    
    public void ins(Connection con) throws SQLException {
            
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_INS_AUA);
            stmt.setLong(1, this.aua_asm_id);
            stmt.setString(2, this.aua_asu_type);
            stmt.setTimestamp(3, this.aua_eff_start_timestamp);
            stmt.setTimestamp(4, this.aua_eff_end_timestamp);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }
    
    private static final String SQL_UPD_AUA = 
          " UPDATE cmAsmUnitTypeAttr SET "
        + "  aua_eff_start_timestamp = ? "
        + ", aua_eff_end_timestamp = ? " 
        + " WHERE aua_asm_id = ? "
        + " AND aua_asu_type = ? ";
    /**
    update this object to table cmAsmUnitTypeAttr
    pre-define class variables:
    aua_asm_id
    aua_asu_type
    aua_eff_start_timestamp
    aua_eff_end_timestamp
    @param con Connection to database
    */
    public void upd(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_UPD_AUA);
            stmt.setTimestamp(1, this.aua_eff_start_timestamp);
            stmt.setTimestamp(2, this.aua_eff_end_timestamp);
            stmt.setLong(3, this.aua_asm_id);
            stmt.setString(4, this.aua_asu_type);
            stmt.executeUpdate();
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }
    
    private static final String SQL_DEL_AUA = 
          " DELETE FROM cmAsmUnitTypeAttr "
        + " WHERE aua_asm_id = ? "
        + " AND aua_asu_type = ? ";
    /**
    delete this object from table cmAsmUnitTypeAttr
    pre-define class variables:
    aua_asm_id
    aua_asu_type
    @param con Connection to database
    */
    public void del(Connection con) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_DEL_AUA);
            stmt.setLong(1, this.aua_asm_id);
            stmt.setString(2, this.aua_asu_type);
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

    private static final String SQL_DEL_AUA_BY_ASM = 
          " DELETE FROM cmAsmUnitTypeAttr "
        + " WHERE aua_asm_id = ? ";
    /**
    delete this object from table cmAsmUnitTypeAttr
    @param con Connection to database
    */
    public static void delByAsm(Connection con, long asm_id) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_DEL_AUA_BY_ASM);
            stmt.setLong(1, asm_id);
        } finally {
            if(stmt!=null) stmt.close();
        }
        return;
    }

	/**
	 * Get all types of assessee's effective start timestamp of the specified assessment.  
	 * @param con
	 * @param asm_id Assessment id
	 * @return Hashtable with type as a key and start timestamp as a value
	 * @throws SQLException
	 */
	public static Hashtable getAllTypeEffStartTimestamp(Connection con, long asm_id)
		throws SQLException {
			Hashtable h_type_ts = new Hashtable();
			String SQL = " Select aua_asu_type, aua_eff_start_timestamp " +
				"From cmAsmUnitTypeAttr " +
				"Where aua_asm_id = ? ";
			PreparedStatement stmt = con.prepareStatement(SQL);
			stmt.setLong(1, asm_id);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				h_type_ts.put(rs.getString("aua_asu_type"), rs.getTimestamp("aua_eff_start_timestamp"));
			}
			stmt.close();
			return h_type_ts;
		}
    
}

    

