package com.cw.wizbank.ae.db;

import java.sql.*;
import com.cw.wizbank.util.*;

public class DbItemAttActn {
	public long iaaIatId;
	public String iaaToAttStatus;

	private static final String SQL_GET = " SELECT iaa_to_att_status FROM aeItemAttActn "
										+ " WHERE iaa_iat_id = ? ";

	private static final String SQL_INS = " INSERT INTO aeItemAttActn "
										+ " ( iaa_iat_id, iaa_to_att_status ) "
										+ " VALUES (?, ?) ";
	
	private static final String SQL_UPD = " UPDATE aeItemAttActn " 
										+ " SET iaa_to_att_status = ? WHERE iaa_iat_id = ? ";
	
	private static final String SQL_DEL = " DELETE FROM aeItemAttActn "
										+ " WHERE iaa_iat_id = ? ";

	/*
    *Get a record from table
    *Pre-define iat_id
    */
	public void get(Connection con) throws SQLException, cwSysMessage { 
		PreparedStatement stmt = con.prepareStatement(SQL_GET);
		int index = 1;
		stmt.setLong(index++, this.iaaIatId);

        ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			this.iaaToAttStatus = rs.getString("iaa_to_att_status");
		} else {
            throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " iaa_iat_id = " + this.iaaIatId);
		}
		rs.close();
		stmt.close();

        return;
	}
	
	/*
    *Insert a record into table
    *Pre-define all fields in the table
    */
    public void ins(Connection con) throws SQLException, cwSysMessage {
		PreparedStatement stmt = con.prepareStatement(SQL_INS);
		int index = 1;

		stmt.setLong(index++, this.iaaIatId);
		stmt.setString(index++, this.iaaToAttStatus);

		if( stmt.executeUpdate() != 1 )
			throw new SQLException("Failed to insert record into the aeItemAttActn");
		stmt.close();
		return;
	}
    
    /*
    *Update the record by itm_id and order
    *Pre-define itr_itm_id, itr_order
    */
    public void upd(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SQL_UPD);
		int index = 1;

		stmt.setString(index++, this.iaaToAttStatus);
		stmt.setLong(index++, this.iaaIatId);

		stmt.executeUpdate();
		stmt.close();
		return;
	}

    /*
    *Del the record by itm_id and order
    *Pre-define itr_itm_id, itr_order
    */
    public void del(Connection con) throws SQLException {
        int index = 1;

		PreparedStatement stmt = con.prepareStatement(SQL_DEL);
		stmt.setLong(index++, this.iaaIatId);
		stmt.executeUpdate();
		stmt.close();
		return;
	}

	
    public static final String get_att_status_to = "Select iaa_to_att_status From AeItemAttActn "
         +   " Where iaa_iat_id = ?  ";
         
    /* DbItemAttActn.java
    Get the "to" attendance status of the action
    @param action_id action id
    */
    public static String getAttStatusTo(Connection con, long action_id) throws SQLException {
        
        String status = null;
        PreparedStatement stmt = con.prepareStatement(get_att_status_to);
        stmt.setLong(1, action_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            status = rs.getString("iaa_to_att_status");
        }
        stmt.close();
        return status;
    }		


}