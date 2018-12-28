package com.cw.wizbank.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import com.cw.wizbank.util.cwSQL;

public class DbIntegCompleteCondition {
	public long icd_id;
	public long icd_icc_id;
	public int icd_completed_item_count;
	public String icd_type;
	public Timestamp icd_create_timestamp;
	public String icd_create_usr_id;
	public Timestamp icd_update_timestamp;
	public String icd_update_usr_id;
	
	public Vector IntegItmlst;
	
	public static final String TYPE_COMPULSORY = "COMPULSORY";
	public static final String TYPE_ELECTIVE = "ELECTIVE";
	
	public void ins(Connection con) throws SQLException {
		String sql = "insert into IntegCompleteCondition(icd_icc_id, icd_completed_item_count, icd_type, icd_create_timestamp, icd_create_usr_id, icd_update_timestamp, icd_update_usr_id) values(?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		int index = 1;
		stmt.setLong(index++, icd_icc_id);
		stmt.setInt(index++, icd_completed_item_count);
		stmt.setString(index++, icd_type);
		stmt.setTimestamp(index++, icd_create_timestamp);
		stmt.setString(index++, icd_create_usr_id);
		stmt.setTimestamp(index++, icd_update_timestamp);
		stmt.setString(index++, icd_update_usr_id);
		stmt.executeUpdate();
		icd_id = cwSQL.getAutoId(con, stmt, "IntegCompleteCondition", "icd_id");
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public void upd(Connection con) throws SQLException {
		String sql = "update IntegCompleteCondition set icd_type=?, icd_completed_item_count=?, icd_update_timestamp=?, icd_update_usr_id=? where icd_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, icd_type);
		stmt.setInt(index++, icd_completed_item_count);
		stmt.setTimestamp(index++, icd_update_timestamp);
		stmt.setString(index++, icd_update_usr_id);
		stmt.setLong(index++, icd_id);
		stmt.executeUpdate();
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public void del(Connection con) throws SQLException {
		String sql = "delete from IntegCompleteCondition where icd_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, icd_id);
		stmt.executeUpdate();
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public void delByIccId(Connection con) throws SQLException {
		String sql = "delete from IntegCompleteCondition where icd_icc_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, icd_icc_id);
		stmt.executeUpdate();
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public void get(Connection con) throws SQLException {
		String sql = "select * from IntegCompleteCondition where icd_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, icd_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			this.icd_icc_id = rs.getLong("icd_icc_id");
			this.icd_completed_item_count = rs.getInt("icd_completed_item_count");
			this.icd_type = rs.getString("icd_type");
			this.icd_create_timestamp = rs.getTimestamp("icd_create_timestamp");
			this.icd_create_usr_id = rs.getString("icd_create_usr_id");
			this.icd_update_timestamp = rs.getTimestamp("icd_update_timestamp");
			this.icd_update_usr_id = rs.getString("icd_update_usr_id");
		}
		if(rs != null) {
			rs.close();
		}
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public Vector getByIccId(Connection con, boolean addZero) throws SQLException {
		Vector ids = new Vector();
		if(addZero) {
			ids.add(new Long(0));
		}
		String sql = "select * from IntegCompleteCondition where icd_icc_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, icd_icc_id);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			ids.add(new Long(rs.getLong("icd_id")));
		}
		if(rs != null) {
			rs.close();
		}
		if(stmt != null) {
			stmt.close();
		}
		return ids;
	}
	
	public static Timestamp getUpdTimestamp(Connection con, long icd_id) throws SQLException {
		Timestamp t = null;
		String sql = "select icd_update_timestamp from IntegCompleteCondition where icd_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, icd_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			t = rs.getTimestamp("icd_update_timestamp");
		}
		if(rs != null) {
			rs.close();
		}
		if(stmt != null) {
			stmt.close();
		}
		return t;
	}

}
