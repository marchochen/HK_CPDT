package com.cw.wizbank.profession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cw.wizbank.util.cwUtils;

public class ProfessionItem {

	public long psi_id;
	public long psi_pfs_id;
	public String psi_ugr_id;
	public String psi_itm;
	
	public void ins(Connection con) throws SQLException {
		String sql = "insert into ProfessionItem (psi_pfs_id, psi_ugr_id, psi_itm) values (?,?,?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, psi_pfs_id);
		stmt.setString(index++, psi_ugr_id);
		stmt.setString(index++, psi_itm);
		stmt.executeUpdate();
		stmt.close();
	}
	
	public void upd(Connection con) throws SQLException {
		String sql = "update ProfessionItem set psi_pfs_id = ?, psi_ugr_id = ?, psi_itm = ? where psi_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, psi_pfs_id);
		stmt.setString(index++, psi_ugr_id);
		stmt.setString(index++, psi_itm);
		stmt.setLong(index++, psi_id);
		stmt.executeUpdate();
		stmt.close();
	}
	
	public static void delByPfsid(Connection con, long pfs_id) throws SQLException {
		String sql = "delete professionItem where psi_pfs_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, pfs_id);
		stmt.executeUpdate();
		stmt.close();
	}
	
	public static ProfessionItem getByPfsid(Connection con, long pfs_id) throws SQLException{
		String sql = "select psi_id, psi_pfs_id, psi_ugr_id, psi_itm from professionItem where psi_pfs_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, pfs_id);
		ResultSet rs = stmt.executeQuery();
		ProfessionItem psi = null;
		if(rs.next()) {
			psi = new ProfessionItem();
			psi.psi_id = rs.getLong("psi_id");
			psi.psi_pfs_id = rs.getLong("psi_pfs_id");
			psi.psi_ugr_id = rs.getString("psi_ugr_id");
			psi.psi_itm = rs.getString("psi_itm");
		}
		rs.close();
		stmt.close();
		return psi;
	}
	
	public static String getItmLstAsXml(Connection con, String[] itm_id_lst) throws SQLException {
		if(itm_id_lst == null || itm_id_lst.length <= 0) {
			return "";
		}
		StringBuffer result = new StringBuffer();
		String sql = "select itm_id, itm_title from aeItem where itm_id in " + cwUtils.array2list(itm_id_lst);
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		result.append("<itm_lst>");
		while(rs.next()) {
			result.append("<itm>");
			result.append("<itm_id>").append(rs.getLong("itm_id")).append("</itm_id>");
			result.append("<itm_title>").append(cwUtils.esc4XML(rs.getString("itm_title"))).append("</itm_title>");
			result.append("</itm>");
		}
		result.append("</itm_lst>");
		rs.close();
		stmt.close();
		return result.toString();
	}
}
