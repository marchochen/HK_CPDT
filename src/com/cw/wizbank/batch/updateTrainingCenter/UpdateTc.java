package com.cw.wizbank.batch.updateTrainingCenter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Vector;

import com.cw.wizbank.batch.batchUtil.BatchUtils;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwIniFile;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class UpdateTc {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        String app_root = args[0];
        Connection con = null;
        try {
        	if (app_root == null) {
        		return;
        	}
        	con = BatchUtils.openDB(app_root);
        	String inifile = args[1];
        	cwIniFile ini = new cwIniFile(inifile);
			//check current training center number
			long root_ent_id = Long.parseLong(ini.getValue("ROOT_ENT_ID"));
			String owner = getUserIdBySteUserId(con, ini.getValue("OWNER"), root_ent_id);
			int tc_count = getTcCount(con, root_ent_id);
			if (tc_count > 1) {
				long super_tcr_id = addSuperTc(con, root_ent_id, owner);
				//link all tc to super tc
				linkTcToSuperTc(con, super_tcr_id, root_ent_id, owner);
				//Set All User Group as the default TC's responsible user group
				setUserGroupToSuperTc(con, super_tcr_id, root_ent_id, owner);
				//Link all Learning Catalogs, Top level Resource Folders, Announcements, Forums and Evaluation Templates to the default TC
				linkCatToSuperTc(con, super_tcr_id, root_ent_id);
				linkResFolderToSuperTc(con, super_tcr_id, root_ent_id);
				linkAnnToSuperTc(con, super_tcr_id, root_ent_id);
				assinAllTaToSuperTc(con, super_tcr_id, root_ent_id, owner);
			} else if (tc_count == 1) {
				long super_tcr_id = DbTrainingCenter.getSuperTcId(con, root_ent_id);
				//change the default tc to super tc,and rename to Head Training Center.
				updateTcTitle(con);
				//clean all user group relation and set All User Group as the default TC's responsible user group
				CleanAllUserGroupRelation(con, root_ent_id);
				setUserGroupToSuperTc(con, super_tcr_id, root_ent_id, owner);
				//Link all Learning Catalogs, Top level Resource Folders, Announcements, Forums and Evaluation Templates to the default TC
				linkCatToSuperTc(con, super_tcr_id, root_ent_id);
				linkResFolderToSuperTc(con, super_tcr_id, root_ent_id);
				linkAnnToSuperTc(con, super_tcr_id, root_ent_id);
			}
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			CommonLog.error(e.getMessage(),e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}

	private static void updateTcTitle(Connection con) throws SQLException {
		String sql = "update tcTrainingCenter set tcr_title = 'Head Training Center' where tcr_parent_tcr_id is null and tcr_status = 'OK'";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static String getUserIdBySteUserId(Connection con, String usr_ste_usr_id, long root_ent_id) throws SQLException {
		String sql = "select usr_id from RegUser where usr_ste_usr_id = ? and usr_ste_ent_id = ? ";
		String value = null;
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, usr_ste_usr_id);
			stmt.setLong(index++, root_ent_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				value = rs.getString("usr_id");
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return value;
	}

	private static void assinAllTaToSuperTc(Connection con, long super_tcr_id, long root_ent_id, String owner) throws SQLException {
		String sql_get_officer = "select distinct tco_usr_ent_id, tco_rol_ext_id from tcTrainingCenterOfficer, tcTrainingCenter  where tco_tcr_id = tcr_id and tcr_ste_ent_id = ? ";
		
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql_get_officer);
			int index = 1;
			stmt.setLong(index++, root_ent_id);
			ResultSet rs = stmt.executeQuery();
			Vector officer_vec = new Vector();
			String rol_ext = null;
			while (rs.next()) {
				officer_vec.addElement(new Long(rs.getLong("tco_usr_ent_id")));
				rol_ext = rs.getString("tco_rol_ext_id");
			}
			if (officer_vec.size() > 0) {
				long[] officer_list = cwUtils.vec2longArray(officer_vec);
				DbTrainingCenter dbTc = new DbTrainingCenter();
				dbTc.tcr_id = super_tcr_id;
				dbTc.setTcr_update_usr_id(owner);
				dbTc.storeRelateOfficer(con, officer_list, rol_ext);
			}

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static void linkTcToSuperTc(Connection con, long super_tcr_id, long root_ent_id, String owner) throws SQLException {
		Vector tc_lst = getAllTcButSuperTc(con, super_tcr_id, root_ent_id);
		Iterator iter = tc_lst.iterator();
		while (iter.hasNext()) {
			long child_tcr_id = ((Long)iter.next()).longValue();
			cleanTcRelation(con, child_tcr_id);
			insTcRelation(con, super_tcr_id, child_tcr_id, owner);
			updateTcParent(con, super_tcr_id, child_tcr_id);
		}
	}

	private static void updateTcParent(Connection con, long super_tcr_id, long child_tcr_id) throws SQLException {
		String sql = "update tcTrainingCenter set tcr_parent_tcr_id = ? where tcr_id = ? ";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, super_tcr_id);
			stmt.setLong(index++, child_tcr_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static void cleanTcRelation(Connection con, long child_tcr_id) throws SQLException {
		String sql = "delete tcRelation where tcn_child_tcr_id = ? ";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, child_tcr_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static void insTcRelation(Connection con, long super_tcr_id, long child_tc_id, String owner) throws SQLException {
		String sql = "insert into tcRelation values (? , ?, ?, ?, ?)";
		PreparedStatement stmt = null;
		Timestamp cur_time = cwSQL.getTime(con);
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, child_tc_id);
			stmt.setLong(index++, super_tcr_id);
			stmt.setLong(index++, 1);
			stmt.setString(index++, owner);
			stmt.setTimestamp(index++, cur_time);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static Vector getAllTcButSuperTc(Connection con, long super_tcr_id, long root_ent_id) throws SQLException {
		String sql = "select tcr_id from tcTrainingCenter where tcr_id <> ? and tcr_ste_ent_id = ? ";
		PreparedStatement stmt = null;
		Vector tcr_ids = new Vector();
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, super_tcr_id);
			stmt.setLong(index++, root_ent_id);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				tcr_ids.addElement(new Long(rs.getLong(1)));
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return tcr_ids;
	}

	private static long addSuperTc(Connection con, long root_ent_id, String owner) throws SQLException {
		String insert = "insert into tcTrainingCenter (tcr_code,tcr_title,tcr_ste_ent_id,tcr_status,tcr_create_timestamp,tcr_create_usr_id,tcr_update_timestamp,tcr_update_usr_id, tcr_parent_tcr_id, tcr_user_mgt_ind ) "
            +" values (?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement stmt = null;
		PreparedStatement pst1 = null;
		long tcr_id = 0;
		Timestamp cur_time = cwSQL.getTime(con);
		try {
			stmt = con.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
			int index = 1;
			stmt.setString(1,"Head Training Center");
			stmt.setString(2,"Head Training Center");
			stmt.setLong(3,root_ent_id);
			stmt.setString(4,"OK");
			stmt.setTimestamp(5, cur_time);
			stmt.setString(6,owner);
			stmt.setTimestamp(7, cur_time);
			stmt.setString(8,owner);
			stmt.setNull(9, java.sql.Types.INTEGER);
			stmt.setBoolean(10, true);
			stmt.executeUpdate();
			tcr_id = cwSQL.getAutoId(con, stmt, "tcTrainingCenter", "tcr_id");
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return tcr_id;
	}

	private static void linkAnnToSuperTc(Connection con, long super_tcr_id, long root_ent_id) throws SQLException {
		String sql = "update Message set msg_tcr_id = ? where msg_type = 'SYS' and msg_root_ent_id = ? ";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, super_tcr_id);
			stmt.setLong(index++, root_ent_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static void linkResFolderToSuperTc(Connection con, long super_tcr_id, long root_ent_id) throws SQLException {
		String sql = "update objective set obj_tcr_id = ? where obj_ancester is null and obj_syl_id in (" +
				     " select syl_id from Syllabus where syl_ent_id_root = ? )";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, super_tcr_id);
			stmt.setLong(index++, root_ent_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static void linkCatToSuperTc(Connection con, long super_tcr_id, long root_ent_id) throws SQLException {
		String sql = "update aeCatalog set cat_tcr_id = ? where cat_owner_ent_id = ? ";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, super_tcr_id);
			stmt.setLong(index++, root_ent_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static void CleanAllUserGroupRelation(Connection con, long root_ent_id) throws SQLException {
		String sql = "delete tcTrainingCenterTargetEntity where tce_ent_id = ? ";
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, root_ent_id);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static void setUserGroupToSuperTc(Connection con, long super_tcr_id, long root_ent_id, String owner) throws SQLException {
		String sql = "insert into tcTrainingCenterTargetEntity values (?, ?, ?, ?)";
		PreparedStatement stmt = null;
		Timestamp cur_time = cwSQL.getTime(con);
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, super_tcr_id);
			stmt.setLong(index++, root_ent_id);
			stmt.setTimestamp(index++, cur_time);
			stmt.setString(index++, owner);
			stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static int getTcCount(Connection con, long root_ent_id) throws SQLException {
		String sql = "select count(*) from tcTrainingCenter where tcr_ste_ent_id = ? ";
		PreparedStatement stmt = null;
		int count = 0;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, root_ent_id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return count;
	}

}
