package com.cw.wizbank.newmessage.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.cw.wizbank.util.cwSQL;

public class MessageParamName {
	long mpn_id;
	long mpn_mtp_id;
	String mpn_name;
	String mpn_name_desc;
	public long getMpn_id() {
		return mpn_id;
	}
	public void setMpn_id(long mpn_id) {
		this.mpn_id = mpn_id;
	}
	public long getMpn_mtp_id() {
		return mpn_mtp_id;
	}
	public void setMpn_mtp_id(long mpn_mtp_id) {
		this.mpn_mtp_id = mpn_mtp_id;
	}
	public String getMpn_name() {
		return mpn_name;
	}
	public void setMpn_name(String mpn_name) {
		this.mpn_name = mpn_name;
	}
	public String getMpn_name_desc() {
		return mpn_name_desc;
	}
	public void setMpn_name_desc(String mpn_name_desc) {
		this.mpn_name_desc = mpn_name_desc;
	}
	
	/**获取模板参数对象
	 * @param con
	 * @param mtp_id
	 * @return
	 * @throws SQLException
	 */
	public List<MessageParamName> getByMtpId(Connection con, long mtp_id) throws SQLException {
		List<MessageParamName> msgParamList = new ArrayList<MessageParamName>();
		MessageParamName msgParam = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int index = 1;

		String sql = "select mpn_id, mpn_name, mpn_name_desc from messageParamName " +
					 "where mpn_mtp_id = ?  ";

		try {
			stmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			stmt.setLong(index++, mtp_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				msgParam = new MessageParamName();
				msgParam.setMpn_id(rs.getLong("mpn_id"));
				msgParam.setMpn_mtp_id(mtp_id);
				msgParam.setMpn_name(rs.getString("mpn_name"));
				msgParam.setMpn_name_desc(rs.getString("mpn_name_desc"));
				
				msgParamList.add(msgParam);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return msgParamList;
	}
	
	/**获取模板参数名
	 * @param con
	 * @param mtp_id
	 * @return mpn_name_vec
	 * @throws SQLException
	 */
	public static Vector<String> getParamNameVec(Connection con, long mtp_id) throws SQLException {
		Vector<String> mpn_name_vec = new Vector<String>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String sql = "select mpn_name from messageParamName where mpn_mtp_id = ?";
		try {
			stmt = con.prepareStatement(sql);
			
			int index = 1;
			stmt.setLong(index++, mtp_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				mpn_name_vec.add(rs.getString("mpn_name"));
				
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return mpn_name_vec;
	}
	
	
	public static void copyRecord(Connection con, long root_mtp_id, long des_mtp_id) throws SQLException {
		StringBuffer sql = new StringBuffer("insert into messageParamName (mpn_mtp_id,mpn_name,mpn_name_desc) select ?, mpn_name,mpn_name_desc from messageParamName where  mpn_mtp_id = ?");
		

		PreparedStatement stmt = null;

		try {
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setLong(index++, des_mtp_id);
			stmt.setLong(index++, root_mtp_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
}
