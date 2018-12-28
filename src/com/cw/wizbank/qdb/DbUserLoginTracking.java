package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.util.cwSQL;

public class DbUserLoginTracking {

	private static String sql_ins_ult = "Insert into UserLoginTracking"+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(rowlock) ":"") 
                    + "(ult_usr_ent_id, ult_type, ult_login_timestamp) values(?,?,?)";
	public static void ins(Connection con, long usr_ent_id, String ult_type, Timestamp curTime) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(sql_ins_ult);
		int index = 1;
		stmt.setLong(index++, usr_ent_id);
		stmt.setString(index++, ult_type);
		stmt.setTimestamp(index++, curTime);
		stmt.executeUpdate();
		stmt.close();
	}
}
