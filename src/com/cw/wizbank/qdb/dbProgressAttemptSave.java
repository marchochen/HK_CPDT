package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.util.cwSQL;

public class dbProgressAttemptSave {
	public long pasTkhId;
	public long pasResId;
	public long pasTimeLeft;
	public String pasFlag;
	public Timestamp pas_create_datetime;
	public String pas_create_usr_id;
	public Timestamp pas_update_datetime;
	public String pas_update_usr_id;
	
	private static final String SQL_CHECK = "select count(*) as cnt from ProgressAttemptSave where pas_tkh_id = ? and pas_res_id = ?";
	
	private static final String SQL_GET = "select * from ProgressAttemptSave where pas_tkh_id = ? and pas_res_id = ?";
	
	private static final String SQL_UPD = "update ProgressAttemptSave set pas_time_remain = ?, pas_flag = ?, pas_update_datetime = ?, pas_update_usr_id = ? where pas_tkh_id = ? and pas_res_id = ? ";
	
	private static final String SQL_INS = "insert into ProgressAttemptSave values (?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_DEL = "delete from ProgressAttemptSave where pas_tkh_id = ? and pas_res_id = ? ";
	
	//private static final String SQL_CHECK_FOR_EXIST = "select * from ProgressAttemptSave where pas_tkh_id = ? and pas_res_id = ?";
	
	public void get (Connection con) throws SQLException {
		PreparedStatement stmt = null; 
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(SQL_GET);
			int index = 1;
			stmt.setLong(index++, this.pasTkhId);
			stmt.setLong(index++, this.pasResId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.pasTimeLeft = rs.getLong("pas_time_remain");
				this.pasFlag = cwSQL.getClobValue(rs, "pas_flag");
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public void upd (Connection con, Timestamp cur_time) throws SQLException {
		PreparedStatement stmt = null;
		try {
		stmt = con.prepareStatement(SQL_UPD);
		int index = 1;
		stmt.setLong(index++, this.pasTimeLeft);
		stmt.setString(index++, this.pasFlag);
		stmt.setTimestamp(index++, cur_time);
		stmt.setString(index++, this.pas_update_usr_id);
		stmt.setLong(index++, this.pasTkhId);
		stmt.setLong(index++, this.pasResId);
		stmt.execute();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public void ins (Connection con,Timestamp cur_time) throws SQLException {
		PreparedStatement stmt = null;
		try {
		stmt = con.prepareStatement(SQL_INS);
		int index = 1;
		stmt.setLong(index++, this.pasTkhId);
		stmt.setLong(index++, this.pasResId);
		stmt.setLong(index++, this.pasTimeLeft);
		stmt.setString(index++, this.pasFlag);
		stmt.setTimestamp(index++, cur_time);
		stmt.setString(index++, this.pas_create_usr_id);
		stmt.setTimestamp(index++, cur_time);
		stmt.setString(index++, this.pas_update_usr_id);
		stmt.execute();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public void del (Connection con) throws SQLException {
		PreparedStatement stmt = null;
		try {
		stmt = con.prepareStatement(SQL_DEL);
		int index = 1;
		stmt.setLong(index++, this.pasTkhId);
		stmt.setLong(index++, this.pasResId);
		stmt.execute();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public boolean chkforExist (Connection con) throws SQLException {
		boolean exist = false;
		PreparedStatement stmt = null; 
		ResultSet rs = null;
		long count = 0;
		try {
			stmt = con.prepareStatement(SQL_CHECK);
			int index = 1;
			stmt.setLong(index++, this.pasTkhId);
			stmt.setLong(index++, this.pasResId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				count = rs.getLong("cnt");
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		if(count > 0){
			exist = true;
		}
		return exist;
	}
	
}
