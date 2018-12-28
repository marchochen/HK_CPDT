package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cw.wizbank.qdb.dbSns;

public class DbSnsGroup {
	
	/**
	 * 彻底删除群组
	 * @param 群组id
	 */
	public void completeDelSnsGroup(Connection con, long grp_id) throws SQLException {
		dbSns.deleteSnsGroupLikeDoingComment(con, grp_id);
		delSnsGroupMember(con, grp_id);
		delSnsGroup(con, grp_id);
	}
	
	/**
	 * 删除群组
	 * @param 群组id
	 */
	public void delSnsGroup(Connection con, long grp_id) throws SQLException {
		String sql = " delete sns_group where s_grp_id = ? ";
		PreparedStatement stmt = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, grp_id);
			stmt.executeUpdate();
		} finally {
			if(stmt != null){
				stmt.close();
			}
		}
	}
	
	/**
	 * 删除群组成员
	 * @param 群组id
	 */
	public void delSnsGroupMember(Connection con, long grp_id) throws SQLException {
		String sql = " delete sns_group_member where s_gpm_grp_id = ? ";
		PreparedStatement stmt = null;
		try{
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, grp_id);
			stmt.executeUpdate();
		} finally {
			if(stmt != null){
				stmt.close();
			}
		}
	}

}
