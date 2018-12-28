package com.cw.wizbank.profession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.util.cwUtils;


public class Profession {
	public long pfs_id;
	public String pfs_title;
	public Timestamp pfs_create_time;
	public long pfs_create_usr_id;
	public Timestamp pfs_update_time;
	public long pfs_update_usr_id;
	
	public void ins(Connection con) throws SQLException {
		String sql = "insert into Profession (pfs_title, pfs_create_time, pfs_create_usr_id) values (?,?,?)";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, pfs_title);
		stmt.setTimestamp(index++, pfs_create_time);
		stmt.setLong(index++, pfs_create_usr_id);
		stmt.executeUpdate();
		stmt.close();
	}
	
	public void upd(Connection con) throws SQLException {
		String sql = "update Profession set pfs_title = ?, pfs_update_time = ?, pfs_update_usr_id = ? where pfs_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, pfs_title);
		stmt.setTimestamp(index++, pfs_update_time);
		stmt.setLong(index++, pfs_update_usr_id);
		stmt.setLong(index++, pfs_id);
		stmt.executeUpdate();
		stmt.close();
	}
	
	public static Vector getAll(Connection con) throws SQLException {
		String sql = "select pfs_id, pfs_title, pfs_create_time, pfs_create_usr_id, pfs_update_time, pfs_update_usr_id from Profession";
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		Vector result = null;
		if(rs != null) {
			result = new Vector();
			while(rs.next()) {
				Profession pfs = new Profession();
				pfs.pfs_id = rs.getLong("pfs_id");
				pfs.pfs_title = rs.getString("pfs_title");
				pfs.pfs_create_time = rs.getTimestamp("pfs_create_time");
				pfs.pfs_create_usr_id = rs.getLong("pfs_create_usr_id");
				pfs.pfs_update_time = rs.getTimestamp("pfs_update_time");
				pfs.pfs_update_usr_id = rs.getLong("pfs_update_usr_id");
				result.add(pfs);
			}
		}
		rs.close();
		stmt.close();
		return result;
	}
	
	public static String getAllAsXml(Connection con) throws SQLException {
		Vector allPfs = getAll(con);
		StringBuffer sb = new StringBuffer();
		sb.append("<pfs_list>");
		for(int i = 0;i<allPfs.size();i++) {
			Profession pfs = (Profession)allPfs.get(i);
			sb.append("<pfs id=\"").append(pfs.pfs_id).append("\">");
			sb.append("<pfs_title>").append(cwUtils.esc4XML(pfs.pfs_title)).append("</pfs_title>");
			sb.append("<pfs_create_time>").append(pfs.pfs_create_time).append("</pfs_create_time>");
			sb.append("<pfs_create_usr_id>").append(pfs.pfs_create_usr_id).append("</pfs_create_usr_id>");
			sb.append("<pfs_update_time>").append(pfs.pfs_update_time).append("</pfs_update_time>");
			sb.append("<pfs_update_usr_id>").append(pfs.pfs_update_usr_id).append("</pfs_update_usr_id>");
			sb.append("</pfs>");
		}
		sb.append("</pfs_list>");
		return sb.toString();
	}
	
	public static Profession get(Connection con, long pfs_id) throws SQLException{
		String sql = "select pfs_id, pfs_title, pfs_create_usr_id, pfs_create_time, pfs_update_usr_id, pfs_update_time from profession where pfs_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, pfs_id);
		ResultSet rs = stmt.executeQuery();
		Profession pfs = null;
		if(rs.next()) {
			pfs = new Profession();
			pfs.pfs_id = rs.getLong("pfs_id");
			pfs.pfs_title = rs.getString("pfs_title");
			pfs.pfs_create_usr_id = rs.getLong("pfs_create_usr_id");
			pfs.pfs_create_time = rs.getTimestamp("pfs_create_time");
			pfs.pfs_update_usr_id = rs.getLong("pfs_update_usr_id");
			pfs.pfs_update_time = rs.getTimestamp("pfs_update_time");
		}
		rs.close();
		stmt.close();
		return pfs;
	}
	
	public String asXml() {
		StringBuffer sb = new StringBuffer();
		sb.append("<pfs id=\"").append(pfs_id).append("\">");
		sb.append("<pfs_title>").append(cwUtils.esc4XML(pfs_title)).append("</pfs_title>");
		sb.append("<pfs_create_time>").append(pfs_create_time).append("</pfs_create_time>");
		sb.append("<pfs_create_usr_id>").append(pfs_create_usr_id).append("</pfs_create_usr_id>");
		sb.append("<pfs_update_time>").append(pfs_update_time).append("</pfs_update_time>");
		sb.append("<pfs_update_usr_id>").append(pfs_update_usr_id).append("</pfs_update_usr_id>");
		sb.append("</pfs>");
		return sb.toString();
	}
	
	public void del(Connection con) throws SQLException {
		String sql = "delete Profession where pfs_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, pfs_id);
		stmt.executeUpdate();
		stmt.close();
	}
	
}