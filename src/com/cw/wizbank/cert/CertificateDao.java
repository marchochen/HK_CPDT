package com.cw.wizbank.cert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class CertificateDao {

	public static final String FSLINK_STATUS_ON = "ON";
	public static final String FSLINK_STATUS_OFF = "OFF";

	// certificate list
	public static Vector getAllCertificate(Connection con,loginProfile prof, boolean status_active, long tcr_id) throws SQLException {
		Vector cerVec = new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select cfc_id ,cfc_title,cfc_img,cfc_tcr_id,cfc_status,cfc_create_datetime,cfc_create_user_id,cfc_update_datetime,cfc_update_user_id,cfc_delete_datetime,cfc_delete_user_id,cfc_code,cfc_end_date from certificate where cfc_delete_datetime is null and cfc_delete_user_id is null";
		if (status_active) 
			sql += " and cfc_status = ?";
		if (tcr_id > 0) {
			sql += " and cfc_tcr_id = ?";
		} 
		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			sql += " and cfc_tcr_id in (select tco_tcr_id from tctrainingcenterofficer where tco_usr_ent_id = ? ";
			sql += " union select tcn_child_tcr_id from tcRelation,tctrainingcenterofficer where tco_tcr_id = tcn_ancestor and tco_usr_ent_id = ? ";
			sql += " )";
		}
		sql += " order by cfc_update_datetime desc ";
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			if (status_active) {
				stmt.setString(index, FSLINK_STATUS_ON);
			}
			if (tcr_id > 0) {
				stmt.setLong(index++, tcr_id);
			}
			if(AccessControlWZB.isRoleTcInd(prof.current_role)){
				stmt.setLong(index++, prof.usr_ent_id);
				stmt.setLong(index++, prof.usr_ent_id);
			}

			rs = stmt.executeQuery();
			while (rs.next()) {
				CertificateBean cerbean = new CertificateBean();
				cerbean.setCfc_id(rs.getInt("cfc_id"));
				cerbean.setCfc_title(rs.getString("cfc_title"));
				cerbean.setCfc_img(rs.getString("cfc_img"));
				cerbean.setCfc_tcr_id(rs.getInt("cfc_tcr_id"));// Training Center
				cerbean.setCfc_status(rs.getString("cfc_status"));
				cerbean.setCfc_create_datetime(rs.getTimestamp("cfc_create_datetime"));
				cerbean.setCfc_create_user_id(rs.getString("cfc_create_user_id"));
				cerbean.setCfc_update_datetime(rs.getTimestamp("cfc_update_datetime"));
				cerbean.setCfc_update_user_id(rs.getString("cfc_update_user_id"));
				cerbean.setCfc_delete_datetime(rs.getTimestamp("cfc_delete_datetime"));
				cerbean.setCfc_delete_user_id(rs.getString("cfc_delete_user_id"));
				cerbean.setCfc_code(rs.getString("cfc_code"));
				cerbean.setCfc_end_datetime(rs.getTimestamp("cfc_end_date"));
				cerVec.add(cerbean);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return cerVec;
	}

	// ins certificate
	public long ins(Connection con, CertificateBean cert) throws SQLException {
		String sql = " insert into certificate(cfc_title,cfc_img, cfc_tcr_id, cfc_status, cfc_create_datetime,cfc_create_user_id,cfc_update_datetime,cfc_update_user_id,cfc_code,cfc_end_date)"
				+ " values(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmt = null;
		long cert_id = 0;
		try {
			stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			int index = 1;
			stmt.setString(index++, cert.getCfc_title());
			stmt.setString(index++, cert.getCfc_img());
			stmt.setLong(index++, cert.getCfc_tcr_id());
			stmt.setString(index++, cert.getCfc_status());
			stmt.setTimestamp(index++, cert.getCfc_create_datetime());
			stmt.setString(index++, cert.getCfc_create_user_id());
			stmt.setTimestamp(index++, cert.getCfc_update_datetime());
			stmt.setString(index++, cert.getCfc_update_user_id());
			stmt.setString(index++, cert.getCfc_code());
			stmt.setTimestamp(index++, cert.getCfc_end_datetime());
			int i =stmt.executeUpdate();
			if(i>0)
				cert_id = cwSQL.getAutoId(con, stmt, "certificate", "cfc_id");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

		return cert_id;
	}

	// del certificate
	public static void del(Connection con, long cert_id) throws SQLException {
		String sql = " delete certificate where cfc_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, cert_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	// byID select
	public static CertificateBean getCertByID(Connection con, long cert_id) throws SQLException {
		CertificateBean cert = new CertificateBean();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select cfc_title,cfc_img,cfc_tcr_id,cfc_status,cfc_create_datetime,cfc_create_user_id,cfc_update_datetime,cfc_update_user_id,cfc_delete_datetime,cfc_delete_user_id,cfc_code,cfc_end_date from certificate"
				+ " where cfc_id = ?";

		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index, cert_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				cert.setCfc_title(rs.getString("cfc_title"));
				cert.setCfc_img(rs.getString("cfc_img"));
				cert.setCfc_tcr_id(rs.getInt("cfc_tcr_id"));// Training Center
				cert.setCfc_status(rs.getString("cfc_status"));
				cert.setCfc_create_datetime(rs.getTimestamp("cfc_create_datetime"));
				cert.setCfc_create_user_id(rs.getString("cfc_create_user_id"));
				cert.setCfc_update_datetime(rs.getTimestamp("cfc_update_datetime"));
				cert.setCfc_update_user_id(rs.getString("cfc_update_user_id"));
				cert.setCfc_delete_datetime(rs.getTimestamp("cfc_delete_datetime"));
				cert.setCfc_delete_user_id(rs.getString("cfc_delete_user_id"));
				cert.setCfc_code(rs.getString("cfc_code"));
				cert.setCfc_end_datetime(rs.getTimestamp("cfc_end_date"));
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return cert;
	}

	public void upd(Connection con, CertificateBean cert) throws SQLException {
		String sql = "";
		if (cwUtils.isEmpty(cert.getCfc_img())) {
			sql = " update certificate set cfc_title=?, cfc_tcr_id=?, cfc_status=?,cfc_update_datetime=?,cfc_update_user_id=?,cfc_code=?,cfc_end_date=? " + " where cfc_id = ?";
		} else {
			sql = " update certificate set cfc_title=?, cfc_tcr_id=?, cfc_status=?,cfc_update_datetime=?,cfc_update_user_id=?,cfc_img=?,cfc_code=?,cfc_end_date=?" + " where cfc_id = ?";
		}

		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, cert.getCfc_title());
			stmt.setLong(index++, cert.getCfc_tcr_id());
			stmt.setString(index++, cert.getCfc_status());
			stmt.setTimestamp(index++, cert.getCfc_update_datetime());
			stmt.setString(index++, cert.getCfc_update_user_id());
			if (cert.getCfc_img() != null && cert.getCfc_img() != "") {
				stmt.setString(index++, cert.getCfc_img());
			}
			stmt.setString(index++, cert.getCfc_code());
			stmt.setTimestamp(index++, cert.getCfc_end_datetime());
			stmt.setLong(index++, cert.getCfc_id());
			stmt.executeUpdate();
			stmt.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public void del(Connection con, CertificateBean cert) throws SQLException {
		String sql = "update certificate set cfc_delete_datetime=?, cfc_delete_user_id=?" + " where cfc_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setTimestamp(index++, cert.getCfc_delete_datetime());
			stmt.setString(index++, cert.getCfc_delete_user_id());
			stmt.setLong(index++, cert.getCfc_id());
			stmt.executeUpdate();
			stmt.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public void tcDel(Connection con, CertificateBean cert) throws SQLException {
		String sql = "update certificate set cfc_tcr_id = ?, cfc_delete_datetime=?, cfc_delete_user_id=?" + " where cfc_tcr_id = ?";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, 1);
			stmt.setTimestamp(index++, cert.getCfc_delete_datetime());
			stmt.setString(index++, cert.getCfc_delete_user_id());
			stmt.setLong(index++, cert.getCfc_tcr_id());
			stmt.executeUpdate();
			stmt.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	public boolean isCertStatus(Connection con, long cert_id) throws SQLException {
		CertificateBean cert = new CertificateBean();
		boolean status = false;
		String sql = "select cfc_status from certificate where cfc_id = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, cert_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				cert.setCfc_status(rs.getString("cfc_status"));
			}
			if (cert.getCfc_status().equalsIgnoreCase("ON")) {
				status = true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return status;
	}

	public boolean isCertaeItem(Connection con, long cert_id) throws SQLException {
		boolean isCite = false;
		String sql = "select itm_cfc_id from aeItem where itm_cfc_id = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, cert_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				isCite = true;
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return isCite;
	}
	
	public String canDelCertificate(Connection con, long cert_id) throws SQLException {
		String title = "";

		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			String sql = "select  distinct itm_id, itm_title from  aeItem where	itm_cfc_id = ? order by itm_title";

			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, cert_id);
			rs = stmt.executeQuery();

			while (rs.next()) {
				title += (cwUtils.notEmpty(title) ? ", " : "") + rs.getString("itm_title");
			}
			return title;
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
	}

	public String[] getCertBytkhID(Connection con, long tkh_id) throws SQLException {
		String[] entity = new String[6];
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select app_id, app_tkh_id, usr_ent_id, usr_display_bil, att_update_timestamp,att_create_timestamp from aeapplication, reguser, aeattendance"
				+ " where app_ent_id = usr_ent_id and app_id = att_app_id and app_tkh_id = ?";
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index, tkh_id);
			rs = stmt.executeQuery();

			while (rs.next()) {
				entity[0] = rs.getString("app_id");
				entity[1] = rs.getString("app_tkh_id");
				entity[2] = rs.getString("usr_ent_id");
				entity[3] = rs.getString("usr_display_bil");
				entity[4] = cwUtils.format(rs.getTimestamp("att_update_timestamp"));
				entity[5] = cwUtils.format(rs.getTimestamp("att_create_timestamp"));
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return entity;
	}

	public String[] getItemTitle(Connection con, long item_id) throws SQLException {
		String[] entity = new String[3];
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = " select itm_title,tcr_title,itm_code from aeItem left join tcTrainingCenter on itm_tcr_id = tcr_id where itm_id=?";
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index, item_id);
			rs = stmt.executeQuery();

			while (rs.next()) {
				entity[0] = rs.getString("itm_title");
				entity[1] = rs.getString("tcr_title");
				entity[2] = rs.getString("itm_code");//课程编号
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return entity;
	}
	
	
	public static Vector searchAllCertificate(Connection con,loginProfile prof, String status,String cert_core,String cert_title,long tcr_id) throws SQLException {
		Vector cerVec = new Vector();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "select cfc_id ,cfc_title,cfc_img,cfc_tcr_id,cfc_status,cfc_create_datetime,cfc_create_user_id,cfc_update_datetime,cfc_update_user_id,cfc_delete_datetime,cfc_delete_user_id,cfc_code,cfc_end_date from certificate where cfc_delete_datetime is null and cfc_delete_user_id is null";
		if (status.equals("valid")){
			 sql +=" and cfc_end_date > " + cwSQL.getDate();
		} 
		if(status.equals("out_date")){
			sql +=" and cfc_end_date < " + cwSQL.getDate();
		} 
		if(cert_core!=null && cert_core.trim().length()>0){
			sql +=" and (cfc_code like ? or cfc_title like ? )";
		}
//		if(cert_title!=null && cert_title.trim().length()>0){
//			sql +=" and cfc_title like ? ";
//		}
		if (tcr_id > 0) {
			sql += " and cfc_tcr_id = ?";
		} 
		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			sql += " and cfc_tcr_id in (select tco_tcr_id from tctrainingcenterofficer where tco_usr_ent_id = ? ";
			sql += " union select tcn_child_tcr_id from tcRelation,tctrainingcenterofficer where tco_tcr_id = tcn_ancestor and tco_usr_ent_id = ? ";
			sql += " )";
		}
		sql += " order by cfc_title ";
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			if(cert_core!=null && cert_core.trim().length()>0){
				stmt.setString(index++, "%" + cert_core + "%");
				stmt.setString(index++, "%" + cert_core + "%");
			}
//			if(cert_title!=null && cert_title.trim().length()>0){
//				stmt.setString(index++, "%" + cert_core + "%");
//			}
			if (tcr_id > 0) {
				if(AccessControlWZB.isRoleTcInd(prof.current_role)){
					stmt.setLong(index++, tcr_id);
				}else{
					stmt.setLong(index++, prof.my_top_tc_id);
				}
			}
			if(AccessControlWZB.isRoleTcInd(prof.current_role)){
				stmt.setLong(index++, prof.usr_ent_id);
				stmt.setLong(index++, prof.usr_ent_id);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				CertificateBean cerbean = new CertificateBean();
				cerbean.setCfc_id(rs.getInt("cfc_id"));
				cerbean.setCfc_title(rs.getString("cfc_title"));
				cerbean.setCfc_img(rs.getString("cfc_img"));
				cerbean.setCfc_tcr_id(rs.getInt("cfc_tcr_id"));// Training Center
				cerbean.setCfc_status(rs.getString("cfc_status"));
				cerbean.setCfc_create_datetime(rs.getTimestamp("cfc_create_datetime"));
				cerbean.setCfc_create_user_id(rs.getString("cfc_create_user_id"));
				cerbean.setCfc_update_datetime(rs.getTimestamp("cfc_update_datetime"));
				cerbean.setCfc_update_user_id(rs.getString("cfc_update_user_id"));
				cerbean.setCfc_delete_datetime(rs.getTimestamp("cfc_delete_datetime"));
				cerbean.setCfc_delete_user_id(rs.getString("cfc_delete_user_id"));
				cerbean.setCfc_code(rs.getString("cfc_code"));
				cerbean.setCfc_end_datetime(rs.getTimestamp("cfc_end_date"));
				cerVec.add(cerbean);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return cerVec;
	}
}
