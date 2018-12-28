package com.cw.wizbank.tpplan.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class dbTpYearSetting {

	public long ysg_tcr_id;
	public long ysg_year;
	public String ysg_child_tcr_id_lst;
	public Timestamp ysg_submit_start_datetime;
	public Timestamp ysg_submit_end_datetime;
	public Timestamp ysg_create_timestamp;
	public String ysg_create_usr_id;
	public Timestamp ysg_update_timestamp;
	public String ysg_update_usr_id;
	
	public void ins(Connection con) throws SQLException {
		String sql = "insert into tpYearSetting(ysg_tcr_id, ysg_year, ysg_child_tcr_id_lst, ysg_submit_start_datetime, ysg_submit_end_datetime, ysg_create_timestamp, ysg_create_usr_id, ysg_update_timestamp, ysg_update_usr_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, this.ysg_tcr_id);
		stmt.setLong(index++, this.ysg_year);
		stmt.setString(index++, this.ysg_child_tcr_id_lst);
		stmt.setTimestamp(index++, this.ysg_submit_start_datetime);
		stmt.setTimestamp(index++, this.ysg_submit_end_datetime);
		stmt.setTimestamp(index++, this.ysg_create_timestamp);
		stmt.setString(index++, this.ysg_create_usr_id);
		stmt.setTimestamp(index++, this.ysg_update_timestamp);
		stmt.setString(index++, this.ysg_update_usr_id);
		stmt.executeUpdate();
		stmt.close();
	}
	
	public void del(Connection con) throws SQLException {
		String sql = "delete from tpYearSetting where ysg_tcr_id = ? and ysg_year = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, this.ysg_tcr_id);
		stmt.setLong(index++, this.ysg_year);
		stmt.executeUpdate();
		stmt.close();
	}
	
	public void upd(Connection con) throws SQLException {
		String sql = "update tpYearSetting set ysg_child_tcr_id_lst = ?, ysg_submit_start_datetime = ?, ysg_submit_end_datetime = ?, ysg_update_timestamp = ?, ysg_update_usr_id = ? where ysg_tcr_id = ? and ysg_year = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, this.ysg_child_tcr_id_lst);
		stmt.setTimestamp(index++, this.ysg_submit_start_datetime);
		stmt.setTimestamp(index++, this.ysg_submit_end_datetime);
		stmt.setTimestamp(index++, this.ysg_update_timestamp);
		stmt.setString(index++, this.ysg_update_usr_id);
		stmt.setLong(index++, this.ysg_tcr_id);
		stmt.setLong(index++, this.ysg_year);
		stmt.executeUpdate();
		stmt.close();
	}
	
	public static dbTpYearSetting get(Connection con, long ysg_tcr_id, long ysg_year) throws SQLException {
		dbTpYearSetting tys = null;
		String sql = "select * from tpYearSetting where ysg_tcr_id = ? and ysg_year = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, ysg_tcr_id);
		stmt.setLong(index++, ysg_year);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			tys = new dbTpYearSetting();
			tys.ysg_tcr_id = rs.getLong("ysg_tcr_id");
			tys.ysg_year = rs.getLong("ysg_year");
			tys.ysg_child_tcr_id_lst = rs.getString("ysg_child_tcr_id_lst");
			tys.ysg_submit_start_datetime = rs.getTimestamp("ysg_submit_start_datetime");
			tys.ysg_submit_end_datetime = rs.getTimestamp("ysg_submit_end_datetime");
			tys.ysg_create_timestamp = rs.getTimestamp("ysg_create_timestamp");
			tys.ysg_create_usr_id = rs.getString("ysg_update_usr_id");
			tys.ysg_update_timestamp = rs.getTimestamp("ysg_update_timestamp");
			tys.ysg_update_usr_id = rs.getString("ysg_update_usr_id");
		}
		stmt.close();
		return tys;
	}
}
