package com.cw.wizbank.ae.db;

import java.sql.*;
import com.cw.wizbank.util.*;

public class DbItemActn {
	public long iatId;
	public String iatType;

	private static final String SQL_GET = " SELECT iat_type FROM aeItemActn "
										+ " WHERE iat_id = ? ";

	private static final String SQL_INS = " INSERT INTO aeItemActn "
										+ " ( iat_type ) VALUES (?) ";


	private static final String SQL_UPD = " UPDATE aeItemActn " 
										+ " SET iat_type = ? "
										+ " WHERE iat_id = ? ";
	
	private static final String SQL_DEL = " DELETE FROM aeItemActn "
										+ " WHERE iat_id = ? ";

	/*
    *Get a record from table
    *Pre-define iat_id
    */
	public void get(Connection con) throws SQLException, cwSysMessage  { 
		PreparedStatement stmt = con.prepareStatement(SQL_GET);
		int index = 1;
		stmt.setLong(index++, this.iatId);

        ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			this.iatType = rs.getString("iat_type");
		} else {
            throw new cwSysMessage(cwSysMessage.GEN_REC_NOT_FOUND, " iat id = " + this.iatId);
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
		PreparedStatement stmt = con.prepareStatement(SQL_INS, PreparedStatement.RETURN_GENERATED_KEYS);
		int index = 1;

		stmt.setString(index++, this.iatType);

		if( stmt.executeUpdate() != 1 )
			throw new SQLException("Failed to insert record into the aeItemActn");
		this.iatId = cwSQL.getAutoId(con, stmt, "aeItemActn", "iat_id");
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
		stmt.setLong(index++, this.iatId);
		stmt.setString(index++, this.iatType);
		stmt.executeUpdate();
		stmt.close();
		return;
	}

    /*
    *Del the record by itm_id and order
    *Pre-define itr_itm_id, itr_order
    */
    public void del(Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SQL_DEL);
		stmt.setLong(1, this.iatId);
		stmt.executeUpdate();
		stmt.close();
		return;
	}

}