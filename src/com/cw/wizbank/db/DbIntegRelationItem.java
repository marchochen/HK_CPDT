package com.cw.wizbank.db;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.util.cwUtils;

public class DbIntegRelationItem {
	public long iri_icd_id;
	public long iri_relative_itm_id;

	public void ins(Connection con) throws SQLException {
		String sql = "insert into IntegRelationItem(iri_icd_id, iri_relative_itm_id) values(?, ?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, iri_icd_id);
		stmt.setLong(index++, iri_relative_itm_id);
		stmt.executeUpdate();
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public void ins(Connection con, String codition_list) throws SQLException {
		Vector course = cwUtils.splitToVec(codition_list, "~");
		String sql = "insert into IntegRelationItem(iri_icd_id, iri_relative_itm_id) values(?, ?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		for(int i=0; i<course.size(); i++) {
			long itm_id = ((Long)course.elementAt(i)).longValue();
			int index = 1;
			stmt.setLong(index++, iri_icd_id);
			stmt.setLong(index++, itm_id);
			stmt.addBatch();
		}
		stmt.executeBatch();
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public void del(Connection con) throws SQLException {
		String sql = "delete from IntegRelationItem where iri_icd_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, iri_icd_id);
		stmt.executeUpdate();
		if(stmt != null) {
			stmt.close();
		}
	}

	public void del(Connection con, String icdIds) throws SQLException {
		String sql = "delete from IntegRelationItem where iri_icd_id in " + icdIds;
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.executeUpdate();
		if(stmt != null) {
			stmt.close();
		}
	}
	
	public Vector get(Connection con) throws SQLException {
		Vector vec = new Vector();
		String sql = "select * from IntegRelationItem where iri_icd_id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, iri_icd_id);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			DbIntegRelationItem dbIri = new DbIntegRelationItem();
			dbIri.iri_icd_id = iri_icd_id;
			dbIri.iri_relative_itm_id = rs.getLong("iri_relative_itm_id");
			vec.add(dbIri);
		}
		if(rs != null) {
			rs.close();
		}
		if(stmt != null) {
			stmt.close();
		}
		return vec;
	}
	
	public static Vector getRelItmLst(Connection con, long iri_icd_id) throws SQLException {
        Vector vec = new Vector();
        String sql = "select * from IntegRelationItem where iri_icd_id=?";
        PreparedStatement stmt = con.prepareStatement(sql);
        int index = 1;
        stmt.setLong(index++, iri_icd_id);
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            long itm_id= rs.getLong("iri_relative_itm_id");
            vec.add(new Long(itm_id));
        }
        if(rs != null) {
            rs.close();
        }
        if(stmt != null) {
            stmt.close();
        }
        return vec;
    }

}