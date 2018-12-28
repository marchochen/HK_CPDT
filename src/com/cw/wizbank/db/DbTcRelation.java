package com.cw.wizbank.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.util.cwSQL;

public class DbTcRelation {
	public long tcn_child_tcr_id;
	public long tcn_ancestor;
	public long tcn_order;
	public String tcn_create_usr_id;
	public Timestamp tcn_create_timestamp;
	
	public static String ins_sql = "insert into tcRelation values(?,?,?,?,?)";
	
	public static Vector getByChildId (Connection con, long tcr_id) throws SQLException {
		 String get_by_child_id_sql = "select * from tcRelation where tcn_child_tcr_id = ? order by tcn_order ";
		 Vector vec = new Vector();
		 PreparedStatement stmt = null;
		 ResultSet rs = null;
		 try {
			stmt = con.prepareStatement(get_by_child_id_sql);
			stmt.setLong(1, tcr_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				DbTcRelation dbtcr = new DbTcRelation();
				dbtcr.tcn_child_tcr_id = rs.getLong("tcn_child_tcr_id");
				dbtcr.tcn_ancestor = rs.getLong("tcn_ancestor");
				dbtcr.tcn_order = rs.getLong("tcn_order");
				dbtcr.tcn_create_usr_id = rs.getString("tcn_create_usr_id");
				dbtcr.tcn_create_timestamp = rs.getTimestamp("tcn_create_timestamp");
				vec.add(dbtcr);
			}		 	
		 } finally {
		 	stmt.close();
		 }
		 return vec;
	}

	
	public static Vector getSubTcList(Connection con, long tcr_id) throws SQLException{
		String sql = "select tcn_child_tcr_id from tcRelation inner join tcTrainingCenter on (tcr_id = tcn_child_tcr_id) where tcn_ancestor = ? and tcr_status = ?";
		PreparedStatement stmt = null;
		Vector sub_tc_list = new Vector();
		try{
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, tcr_id);
			stmt.setString(2, DbTrainingCenter.STATUS_OK);
			ResultSet rs = stmt.executeQuery();
	        while(rs.next()){
	                sub_tc_list.add(new Long(rs.getLong("tcn_child_tcr_id")));
	        }
		} finally {
			 cwSQL.closePreparedStatement(stmt);
	  	}
        stmt.close();
        return sub_tc_list;
	}
	
	public static Vector getSubTcList(Connection con, long[] tcr_id_lst) throws SQLException{
		String sql = "select tcn_child_tcr_id from tcRelation inner join tcTrainingCenter on (tcr_id = tcn_child_tcr_id) where tcn_ancestor in" + aeUtils.prepareSQLList(tcr_id_lst) +" and tcr_status = ?";
		PreparedStatement stmt = null;
		Vector sub_tc_list = new Vector();
		try{
			stmt = con.prepareStatement(sql);
			stmt.setString(1, DbTrainingCenter.STATUS_OK);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				sub_tc_list.add(new Long(rs.getLong("tcn_child_tcr_id")));
			}
		} finally {
			 cwSQL.closePreparedStatement(stmt);
	  	}
        stmt.close();
		return sub_tc_list;
	}
	
	public static void insert (Connection con, long parent_tcr_id, long new_tcr_id, String cur_usr_id, Timestamp cur_time) throws SQLException {
		Vector vec = getByChildId(con, parent_tcr_id);
		DbTcRelation dbtc = null;
		long max_order = 0;
		PreparedStatement stmt = null;
		for (int i=0; i<vec.size(); i++) {
			dbtc = (DbTcRelation)vec.elementAt(i);
			max_order = dbtc.tcn_order;
			stmt = con.prepareStatement(ins_sql);
			int index = 1;
			stmt.setLong(index++, new_tcr_id);
			stmt.setLong(index++, dbtc.tcn_ancestor);
			stmt.setLong(index++, dbtc.tcn_order);
			stmt.setString(index++, cur_usr_id);
			stmt.setTimestamp(index++, cur_time);
			stmt.executeUpdate();
            stmt.close();
		}		
		stmt = con.prepareStatement(ins_sql);
		int index = 1;
		stmt.setLong(index++, new_tcr_id);
		stmt.setLong(index++, parent_tcr_id);
		stmt.setLong(index++, max_order+1);
		stmt.setString(index++, cur_usr_id);
		stmt.setTimestamp(index++, cur_time);
		stmt.executeUpdate();
		if (stmt != null) {
			stmt.close();
		}
	}

}
