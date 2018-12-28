package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

public class CurrentActiveUser {

	public long cau_usr_ent_id;
	public Timestamp cau_login_date;
	public String cau_sess_id;

	private static Object lock = new Object();

	// private static int cur_active_user_count;
	/**
	 * add current active user when login (except SYS user)
	 * 
	 * @param con
	 * @param usr_ent_id
	 *            login user
	 * @param sess_id
	 *            loging session id
	 * @param my_top_tc_id
	 * @throws SQLException
	 */
	public static void addCAU(Connection con, long usr_ent_id, String sess_id,
			long my_top_tc_id,String login_type) throws SQLException {

		subAllopatryCAU(con, usr_ent_id,login_type);

		// synchronized (lock) {
		String insSql = "insert into CurrentActiveUser (cau_usr_ent_id,cau_login_date,cau_sess_id,cau_eip_tcr_id,cau_login_type) values (?, ?, ?,?,?)";
		PreparedStatement stmt = con.prepareStatement(insSql);
		stmt.setLong(1, usr_ent_id);
		stmt.setTimestamp(2, cwSQL.getTime(con));
		stmt.setString(3, sess_id);
		stmt.setLong(4, my_top_tc_id);
		stmt.setString(5, login_type);
		stmt.executeUpdate();
		stmt.close();
		// add one
		// cur_active_user_count++;
		// }

	}

	/**
	 * subtract current active user when user login on other places
	 * 
	 * @param con
	 * @param usr_ent_id
	 * @param login_type
	 * @throws SQLException
	 */
	public static void subAllopatryCAU(Connection con, long usr_ent_id, String login_type)
			throws SQLException {
		// synchronized (lock) {
		String delSql = "delete from CurrentActiveUser where cau_usr_ent_id = ?";
		if(null != login_type && !login_type.equals("")){
			delSql += " and cau_login_type = ?";
		}else{
			delSql += " and cau_login_type is null";
		}
		PreparedStatement stmt = con.prepareStatement(delSql);
		stmt.setLong(1, usr_ent_id);
		if(null != login_type && !login_type.equals("")){
			stmt.setString(2, login_type);
		}
		stmt.executeUpdate();
		stmt.close();
		// }
	}

	/**
	 * subtract current active user when user out | session time out
	 * 
	 * @param con
	 * @param usr_ent_id
	 * @throws SQLException
	 */
	public static void subCAU(Connection con, String sess_id)
			throws SQLException {
		// synchronized (lock) {
		String delSql = "delete from CurrentActiveUser where cau_sess_id = ?";
		PreparedStatement stmt = con.prepareStatement(delSql);
		stmt.setString(1, sess_id);
		stmt.executeUpdate();
		stmt.close();
		// subtract one
		// cur_active_user_count--;
		// }

	}

	/**
	 * clean all current active user when the System reset
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public static void cleanAllCAU(Connection con) throws SQLException {

		String cleanSql = "delete from CurrentActiveUser";
		PreparedStatement stmt = con.prepareStatement(cleanSql);
		stmt.executeUpdate();
		stmt.close();
		con.commit();
		// reset
		// cur_active_user_count = 0;

	}

	public static String getCurActUserXml(Connection con, loginProfile prof,
			int page, int page_size, String sortCol, String sortOrder)
			throws SQLException {

		if (sortCol == null || sortCol.length() == 0) {
			sortCol = "cau_login_date";
		}
		if (sortOrder == null || sortOrder.length() == 0) {
			sortOrder = "desc";
		}
		if (page == 0) {
			page = 1;
		}
		if (page_size == 0) {
			page_size = 10;
		}
		int start = page_size * (page - 1);
		int count = 0;

		StringBuffer sql = new StringBuffer();
		sql.append(
				"select cau_usr_ent_id, cau_login_date, usr_ste_usr_id, usr_display_bil, ern_ancestor_ent_id group_id, cau_login_type")
				.append(" from currentActiveUser,regUser,EntityRelation")
				.append(" where cau_usr_ent_id = usr_ent_id")
				.append(" and ern_child_ent_id = usr_ent_id")
				.append(" and ern_type = ?").append(" and ern_parent_ind = ?");
		sql.append(" ORDER BY " + cwPagination.esc4SortSql(sortCol) + " " + cwPagination.esc4SortSql(sortOrder));

		StringBuffer cauXml = new StringBuffer();
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		stmt.setString(1, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		stmt.setBoolean(2, true);
		ResultSet rs = stmt.executeQuery();
		cauXml.append("<cur_act_user_list>");
		EntityFullPath entityfullpath = EntityFullPath.getInstance(con);
		while (rs.next()) {
			if (count >= start && count < start + page_size) {
				cauXml.append("<active_user usr_ste_usr_id=\"")
						.append(cwUtils.esc4XML(rs.getString("usr_ste_usr_id")))
						.append("\" usr_display_bil=\"")
						.append(cwUtils.esc4XML(rs.getString("usr_display_bil")))
						.append("\" group=\"")
						.append(cwUtils.esc4XML(entityfullpath.getFullPath(con,
								rs.getLong("group_id"))))
						.append("\" login_time=\"")
						.append(rs.getTimestamp("cau_login_date"))
						.append("\" login_type=\"")
						.append(rs.getString("cau_login_type"))
						.append("\" usr_ent_id=\"")
						.append(cwUtils.esc4XML(rs.getString("cau_usr_ent_id")))
						.append("\"/>");
			}
			count++;
		}
		cauXml.append("</cur_act_user_list>");
		cwPagination pagn = new cwPagination();
		pagn.totalRec = count;
		pagn.totalPage = (int) Math.ceil((float) count / page_size);
		pagn.pageSize = page_size;
		pagn.curPage = page;
		pagn.sortCol = cwPagination.esc4SortSql(sortCol);
		pagn.sortOrder = cwPagination.esc4SortSql(sortOrder);
		pagn.ts = null;
		cauXml.append(pagn.asXML());
		stmt.close();
		return cauXml.toString();
	}

	public static int getcurActiveUserCount(Connection conn)
			throws SQLException {
		return getCurActUserCnt(conn);
	}

	private static int getCurActUserCnt(Connection con) throws SQLException {
		int count = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select count(*) from currentActiveUser "
				+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) "
						: "")
				+ ",regUser"
				+ (cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType()) ? " with(nolock) "
						: ""));
		sb.append(" where usr_ent_id = cau_usr_ent_id and usr_status = 'OK'");

		PreparedStatement pStmt = null;
		ResultSet rs = null;
		try {
			pStmt = con.prepareStatement(sb.toString());
			rs = pStmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			cwSQL.cleanUp(rs, pStmt);
		}
		return count;
	}

	/**
	 * 获取企业培训中心下面所有已登录用户的个数
	 * @param con
	 * @param eipTcrId
	 * @return
	 * @throws SQLException
	 */
	public static long getCurActUserCntByEipTcrId(Connection con, long eipTcrId)
			throws SQLException {
		int count = 0;
		StringBuffer sb = new StringBuffer();
		sb.append("select count(cau_usr_ent_id) from CurrentActiveUser ")
		.append(" where cau_eip_tcr_id = ? ")
		.append(" or cau_eip_tcr_id in (select tcn_child_tcr_id from tcRelation where tcn_ancestor = ?) ");

		PreparedStatement pStmt = null;
		ResultSet rs = null;
		try {
			pStmt = con.prepareStatement(sb.toString());
			pStmt.setLong(1, eipTcrId);
			pStmt.setLong(2, eipTcrId);
			rs = pStmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			cwSQL.cleanUp(rs, pStmt);
		}
		return count;
	}

	public static boolean isSessionExisted(Connection con, String sess_id)
			throws SQLException {
		int count = 0;
		String sql = "select count(cau_sess_id) c from currentActiveUser where cau_sess_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = null;
		try {
			stmt.setString(1, sess_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
				if (count > 0) {
					return true;
				}
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return false;
	}

	/**
	 * Number of the current user login on
	 * 
	 * @param con
	 * @param usr_ent_id
	 * @return
	 * @throws SQLException
	 */
	public static int userLoginCount(Connection con, Long usr_ent_id)
			throws SQLException {
		int count = 0;
		String sql = "select count(cau_usr_ent_id) c from currentActiveUser where cau_usr_ent_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs = null;
		try {
			stmt.setLong(1, usr_ent_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} finally {
			cwSQL.cleanUp(rs, stmt);
		}
		return count;
	}
}
