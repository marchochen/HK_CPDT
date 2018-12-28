package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class dbScormResource {
	public long srs_res_id;

	public String srs_structure_xml;

	public String srs_aicc_version;

	public String srs_vendor;

	public long srs_max_normal;

	private static final String sql_ins = "insert into ScormResource(srs_res_id, srs_aicc_version,srs_vendor, srs_max_normal) values(?,?,?,?)";
	public void ins(Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql_ins);
			int index = 1;
			pstmt.setLong(index++, srs_res_id);
			pstmt.setString(index++, srs_aicc_version);
			pstmt.setString(index++, srs_vendor);
			pstmt.setLong(index++, srs_max_normal);
			pstmt.executeUpdate();
			String condition = "srs_res_id = " + srs_res_id;
			cwSQL.updateClobFields(con, "ScormResource", new String[]{"srs_structure_xml"}, new String[]{srs_structure_xml}, condition);
		} finally {
			if (pstmt != null) pstmt.close();
		}
	}
	
	private static final String sql_upd = "update ScormResource set srs_aicc_version = ?,srs_vender = ?, srs_max_normal = ?) where srs_res_id = ?";
	public void upd(Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql_upd);
			int index = 1;
			pstmt.setString(index++, srs_aicc_version);
			pstmt.setString(index++, srs_vendor);
			pstmt.setLong(index++, srs_max_normal);
			pstmt.setLong(index++, srs_res_id);
			pstmt.executeUpdate();
			String condition = "srs_res_id = " + srs_res_id;
			cwSQL.updateClobFields(con, "ScormResource", new String[]{"srs_structure_xml"}, new String[]{srs_structure_xml}, condition);
		} finally {
			if (pstmt != null) pstmt.close();
		}
	}
	
	private static final String sql_del = "delete from ScormResource where srs_res_id = ?";
	public void del(Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sql_del);
			int index = 1;
			pstmt.setLong(index++, srs_res_id);
			pstmt.executeUpdate();
		} finally {
			if (pstmt != null) pstmt.close();
		}
	}
	
	private static final String sql_get = "select * from ScormResource where srs_res_id = ?";
	public void get(Connection con) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql_get);
			int index = 1;
			pstmt.setLong(index++, srs_res_id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				this.srs_aicc_version = rs.getString("srs_aicc_version");
				this.srs_max_normal = rs.getLong("srs_max_normal");
				this.srs_vendor = rs.getString("srs_vendor");
				this.srs_structure_xml = cwSQL.getClobValue(rs, "srs_structure_xml");
			}
		} finally {
			if (pstmt != null) pstmt.close();
		}
	}
	
	public static HashMap getStructureMapByResIds(Connection con, Vector resIdVec) throws SQLException {
		HashMap structureMap = new HashMap();
		
		String sql = "select srs_res_id, srs_structure_xml from ScormResource";
		if(resIdVec != null && resIdVec.size() > 0) {
			sql += " where srs_res_id in" + cwUtils.vector2list(resIdVec);
		} else {
			sql += " where srs_res_id in(0)";
		}
		sql += " order by srs_res_id asc";
			
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				long resId = rs.getLong("srs_res_id");
				String srsStructureXml = cwSQL.getClobValue(rs, "srs_structure_xml");
				structureMap.put(new Long(resId), srsStructureXml);
			}
		} finally {
			if (stmt != null) stmt.close();
		}
		
		return structureMap;
	}
}
