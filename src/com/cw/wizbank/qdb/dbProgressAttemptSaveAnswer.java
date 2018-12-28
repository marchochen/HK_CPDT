package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

public class dbProgressAttemptSaveAnswer {
	public long psaTkhId;
	public long psaPgrResId;
	public long psaIntResId;
	public long psaIntOrder;
	public String psaResponseBil;
	public Timestamp psa_create_datetime;
	public String psa_create_usr_id;
	public Timestamp psa_update_datetime;
	public String psa_update_usr_id;
	
	public final String DB_FLAG_INS = "ins";
	public final String DB_FLAG_UPD = "upd";
	public final String DB_FLAG_DEL = "del";
	
	private static final String SQL_GET = "select * from ProgressAttemptSaveAnswer where psa_tkh_id = ? and psa_res_id = ? and psa_int_res_id = ? order by psa_int_order";
	
	private static final String SQL_UPD = "update ProgressAttemptSaveAnswer set psa_response_bil = ?, psa_update_datetime = ?, psa_update_usr_id = ? where psa_tkh_id = ? and psa_res_id = ? and psa_int_res_id = ? and psa_int_order = ? ";
	
	private static final String SQL_INS = "insert into ProgressAttemptSaveAnswer values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String SQL_DEL = "delete from ProgressAttemptSaveAnswer where psa_tkh_id = ? and psa_res_id = ? and psa_int_res_id = ? and psa_int_order = ? ";
	
	private static final String SQL_DEL_ALL = "delete from ProgressAttemptSaveAnswer where psa_tkh_id = ? and psa_res_id = ? ";
	
	private static final String SQL_CHECK = "select count(*) as cnt from ProgressAttemptSaveAnswer where psa_tkh_id = ? and psa_res_id = ? ";
	
	public Vector get (Connection con) throws SQLException {
		PreparedStatement stmt = null; 
		ResultSet rs = null;
		Vector vec = new Vector();
		try {
			stmt = con.prepareStatement(SQL_GET);
			int index = 1;
			stmt.setLong(index++, this.psaTkhId);
			stmt.setLong(index++, this.psaPgrResId);
			stmt.setLong(index++, this.psaIntResId);
			//stmt.setLong(index++, this.psaIntOrder);
			rs = stmt.executeQuery();
			while (rs.next()) {
				dbProgressAttemptSaveAnswer dbpsa = new dbProgressAttemptSaveAnswer();
				dbpsa.psaTkhId = rs.getLong("psa_tkh_id");
				dbpsa.psaPgrResId = rs.getLong("psa_res_id");
				dbpsa.psaIntResId = rs.getLong("psa_int_res_id");
				dbpsa.psaIntOrder = rs.getLong("psa_int_order");
				dbpsa.psaResponseBil = rs.getString("psa_response_bil");
				vec.add(dbpsa);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
		return vec;
	}
	
	public void upd (Connection con, Timestamp cur_time) throws SQLException {
		PreparedStatement stmt = null;
		try {
		stmt = con.prepareStatement(SQL_UPD);
		int index = 1;
		stmt.setString(index++, this.psaResponseBil);
		stmt.setTimestamp(index++, cur_time);
		stmt.setString(index++, this.psa_update_usr_id);
		stmt.setLong(index++, this.psaTkhId);
		stmt.setLong(index++, this.psaPgrResId);
		stmt.setLong(index++, this.psaIntResId);
		stmt.setLong(index++, this.psaIntOrder);
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
		stmt.setLong(index++, this.psaTkhId);
		stmt.setLong(index++, this.psaPgrResId);
		stmt.setLong(index++, this.psaIntResId);
		stmt.setLong(index++, this.psaIntOrder);
		stmt.setString(index++, this.psaResponseBil);
		stmt.setTimestamp(index++, cur_time);
		stmt.setString(index++, this.psa_create_usr_id);
		stmt.setTimestamp(index++, cur_time);
		stmt.setString(index++, this.psa_update_usr_id);
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
		stmt.setLong(index++, this.psaTkhId);
		stmt.setLong(index++, this.psaPgrResId);
		stmt.setLong(index++, this.psaIntResId);
		stmt.setLong(index++, this.psaIntOrder);
		stmt.execute();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public void delAll (Connection con) throws SQLException {
		PreparedStatement stmt = null;
		try {
		stmt = con.prepareStatement(SQL_DEL_ALL);
		int index = 1;
		stmt.setLong(index++, this.psaTkhId);
		stmt.setLong(index++, this.psaPgrResId);
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
			stmt.setLong(index++, this.psaTkhId);
			stmt.setLong(index++, this.psaPgrResId);
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
