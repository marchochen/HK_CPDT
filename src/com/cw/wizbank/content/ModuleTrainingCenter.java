package com.cw.wizbank.content;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.util.cwSQL;

public class ModuleTrainingCenter {

	public long mtc_mod_id;
	public long mtc_tcr_id;
	public Timestamp mtc_create_timestamp;
	public String mtc_create_usr_id;
	
	public static final String KEY_TCR_ID = "TCR_ID";
	public static final String KEY_TCR_TITLE = "TCR_TITLE";
	public boolean mtc_mobile_ind = false;

	private static final String INS = "insert into moduleTrainingCenter(mtc_mod_id, mtc_tcr_id, mtc_create_timestamp, mtc_create_usr_id, mtc_mobile_ind) values(?, ?, ?, ?, ?)";

	public void insert(Connection con) throws SQLException {
		PreparedStatement stmt = null;
		Timestamp curTime = cwSQL.getTime(con);
		try {
			stmt = con.prepareStatement(INS);
			int index = 1;
			stmt.setLong(index++, mtc_mod_id);
			stmt.setLong(index++, mtc_tcr_id);
			stmt.setTimestamp(index++, curTime);
			stmt.setString(index++, mtc_create_usr_id);
			stmt.setBoolean(index++, mtc_mobile_ind);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public static String getDeleteSql() {
		String sql = "delete from moduleTrainingCenter where mtc_mod_id = ?";
		return sql;
	}
	
	public static void delete(Connection con, long mtcModId) throws SQLException  {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(getDeleteSql());
			int index = 1;
			stmt.setLong(index++, mtcModId);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static final String UPD = "update moduleTrainingCenter set mtc_tcr_id = ?, mtc_mobile_ind = ? where mtc_mod_id = ?";

	public void update(Connection con) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(UPD);
			int index = 1;
			stmt.setLong(index++, mtc_tcr_id);
			stmt.setBoolean(index++, mtc_mobile_ind);
			stmt.setLong(index++, mtc_mod_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static final String GET_TC_BY_MOD_ID = "select tcr_id, tcr_title from moduleTrainingCenter "
			+ " inner join tcTrainingCenter on (tcr_id = mtc_tcr_id and tcr_status = ?) where mtc_mod_id = ?";
	
	public HashMap getTcByModId(Connection con) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		HashMap tcMap = null;
		try{
			stmt = con.prepareStatement(GET_TC_BY_MOD_ID);
			int index = 1;
			stmt.setString(index++, DbTrainingCenter.STATUS_OK);
			stmt.setLong(index++, mtc_mod_id);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				tcMap = new HashMap();
				tcMap.put(KEY_TCR_ID, new Long(rs.getLong("tcr_id")));
				tcMap.put(KEY_TCR_TITLE, rs.getString("tcr_title"));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return tcMap;
	}
	
	public boolean isMobile(Connection con) throws SQLException{
		boolean isMobile = false;
		String sql = "select mtc_mobile_ind from moduletrainingcenter where mtc_mod_id = ?" ;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, mtc_mod_id);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				isMobile = rs.getBoolean("mtc_mobile_ind");
			}
		} finally {
			if(rs != null){
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return isMobile;
	}
}
