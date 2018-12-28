package com.cw.wizbank.db;

import java.sql.*;

import com.cw.wizbank.util.cwSQL;

public class DbIntegCourseCriteria {
	public long icc_id;
	public long icc_itm_id;
	public int icc_completed_elective_count;
	public Timestamp icc_create_timestamp;
	public String icc_create_usr_id;
	public Timestamp icc_update_timestamp;
	public String icc_update_usr_id;
	
	public void ins(Connection con) throws SQLException {
		String sql = "insert into IntegCourseCriteria(icc_itm_id, icc_completed_elective_count, icc_create_timestamp, icc_create_usr_id, icc_update_timestamp, icc_update_usr_id) values(?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		int index = 1;
		stmt.setLong(index++, icc_itm_id);
		stmt.setInt(index++, icc_completed_elective_count);
		stmt.setTimestamp(index++, icc_create_timestamp);
		stmt.setString(index++, icc_create_usr_id);
		stmt.setTimestamp(index++, icc_update_timestamp);
		stmt.setString(index++, icc_update_usr_id);
		stmt.executeUpdate();
		icc_id = cwSQL.getAutoId(con, stmt, "IntegCourseCriteria", "icc_id");
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public void upd(Connection con) throws SQLException {
		String sql = "update IntegCourseCriteria set icc_completed_elective_count=?, icc_update_timestamp=?, icc_update_usr_id=? where icc_itm_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setInt(index++, icc_completed_elective_count);
		stmt.setTimestamp(index++, icc_update_timestamp);
		stmt.setString(index++, icc_update_usr_id);
		stmt.setLong(index++, icc_itm_id);
		stmt.executeUpdate();
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public void del(Connection con) throws SQLException {
		String sql = "delete from IntegCourseCriteria where icc_itm_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, icc_itm_id);
		stmt.executeUpdate();
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public void get(Connection con) throws SQLException {
		String sql = "select * from IntegCourseCriteria where icc_itm_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, this.icc_itm_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			this.icc_id = rs.getLong("icc_id");
			this.icc_completed_elective_count = rs.getInt("icc_completed_elective_count");
			this.icc_create_timestamp = rs.getTimestamp("icc_create_timestamp");
			this.icc_create_usr_id = rs.getString("icc_create_usr_id");
			this.icc_update_timestamp = rs.getTimestamp("icc_update_timestamp");
			this.icc_update_usr_id = rs.getString("icc_update_usr_id");
		}
		if(rs != null) {
			rs.close();
		}
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public static Timestamp getUpdTimestamp(Connection con, long itm_id) throws SQLException {
		Timestamp t = null;
		String sql = "select * from IntegCourseCriteria where icc_itm_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, itm_id);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			t = rs.getTimestamp("icc_update_timestamp");
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
