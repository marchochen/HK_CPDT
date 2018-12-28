package com.cw.wizbank.know.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.JsonMod.know.bean.KnowCatalogBean;
import com.cw.wizbank.know.db.KnowCatalogDB;
import com.cw.wizbank.util.cwSQL;

/**
 * @author DeanChen
 * 
 */
public class KnowCatalogDAO {

	public static final String KCA_TYPE_CATALOG = "CATALOG";

	public static final String KCA_TYPE_NORMAL = "NORMAL";

	private static final String GET_CATALOG_BY_CAT_ID = " SELECT kca_tcr_id,kca_code,kca_title,kca_type,"
			+ " kca_public_ind,kca_que_count,kca_create_usr_id," + " kca_create_timestamp,kca_update_usr_id,kca_update_timestamp "
			+ " FROM knowCatalog WHERE kca_id = ? ";

	public void get(Connection con, KnowCatalogDB knowCatDb) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(GET_CATALOG_BY_CAT_ID);
			int index = 1;
			stmt.setInt(index++, knowCatDb.getKca_id());
			rs = stmt.executeQuery();
			if (rs.next()) {
				int kca_tcr_id = rs.getInt("kca_tcr_id");
				String kca_code = rs.getString("kca_code");
				String kca_title = rs.getString("kca_title");
				String kca_type = rs.getString("kca_type");
				boolean kca_public_ind = rs.getBoolean("kca_public_ind");
				int kca_que_count = rs.getInt("kca_que_count");
				String kca_create_usr_id = rs.getString("kca_create_usr_id");
				Timestamp kca_create_timestamp = rs.getTimestamp("kca_create_timestamp");
				String kca_update_usr_id = rs.getString("kca_update_usr_id");
				Timestamp kca_update_timestamp = rs.getTimestamp("kca_update_timestamp");

				knowCatDb.setKca_tcr_id(kca_tcr_id);
				knowCatDb.setKca_code(kca_code);
				knowCatDb.setKca_title(kca_title);
				knowCatDb.setKca_type(kca_type);
				knowCatDb.setKca_public_ind(kca_public_ind);
				knowCatDb.setKca_que_count(kca_que_count);
				knowCatDb.setKca_create_usr_id(kca_create_usr_id);
				knowCatDb.setKca_create_timestamp(kca_create_timestamp);
				knowCatDb.setKca_update_usr_id(kca_update_usr_id);
				knowCatDb.setKca_update_timestamp(kca_update_timestamp);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		}
	}

	private static final String INS_CATALOG = " insert into knowCatalog(kca_code, kca_tcr_id, kca_title, kca_type, "
			+ " kca_public_ind, kca_que_count, kca_create_usr_id, kca_create_timestamp, kca_update_usr_id, kca_update_timestamp) "
			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	public int ins(Connection con, KnowCatalogDB knowCatDb) throws SQLException {
		Timestamp curTime = cwSQL.getTime(con);

		PreparedStatement stmt = null;
		stmt = con.prepareStatement(INS_CATALOG, PreparedStatement.RETURN_GENERATED_KEYS);
		int index = 1;
		stmt.setNull(index++, Types.VARCHAR);
		stmt.setInt(index++, knowCatDb.getKca_tcr_id());
		stmt.setString(index++, knowCatDb.getKca_title());
		stmt.setString(index++, knowCatDb.getKca_type());
		stmt.setBoolean(index++, knowCatDb.isKca_public_ind());
		stmt.setInt(index++, 0);
		stmt.setString(index++, knowCatDb.getKca_create_usr_id());
		stmt.setTimestamp(index++, curTime);
		stmt.setString(index++, knowCatDb.getKca_update_usr_id());
		stmt.setTimestamp(index++, curTime);
		int insCount = stmt.executeUpdate();
		if (insCount != 1) {
            con.rollback();
            throw new SQLException("Failed to add a record of knowCatalog.");
        }
		int kca_Id = (int) cwSQL.getAutoId(con, stmt, "knowCatalog", "kca_id");
		knowCatDb.setKca_id(kca_Id);

		stmt.close();
		return kca_Id;
	}

	private static final String UPD_CATALOG = " update knowCatalog set kca_title = ?, kca_public_ind = ?,  "
			+ " kca_update_usr_id = ?, kca_update_timestamp = ? where kca_id = ? ";

	public void upd(Connection con, KnowCatalogDB knowCatDb) throws SQLException {
		Timestamp curTime = cwSQL.getTime(con);

		PreparedStatement stmt = null;
		stmt = con.prepareStatement(UPD_CATALOG);
		int index = 1;
		stmt.setString(index++, knowCatDb.getKca_title());
		stmt.setBoolean(index++, knowCatDb.isKca_public_ind());
		stmt.setString(index++, knowCatDb.getKca_update_usr_id());
		stmt.setTimestamp(index++, curTime);
		stmt.setInt(index++, knowCatDb.getKca_id());
		stmt.executeUpdate();

		stmt.close();
	}

	/**
	 * 检查目录是否存在
	 */
	private static String CHECK_CAT_IS_EXISTS = " select kca_id from knowCatalog where kca_id = ? ";

	public static boolean isCatalogExists(Connection con, int kca_id) throws SQLException {
		boolean exists = false;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(CHECK_CAT_IS_EXISTS);
		stmt.setInt(1, kca_id);
		rs = stmt.executeQuery();
		if (rs.next()) {
			exists = true;
		}
		rs.close();
		stmt.close();
		return exists;
	}

	/**
	 * 检查目录名是否已经存在
	 */
	private static String CHECK_CAT_TITLE_IS_EXISTS = " select kca_id from knowCatalog " +
	 		" inner join tcTrainingCenter on (tcr_id = kca_tcr_id and tcr_ste_ent_id = ?)" +
			" where kca_title = ? and kca_id <> ? ";

	public static boolean isCatalogTitleExists(Connection con, String kca_title, int kca_id, long steEntId) throws SQLException {
		boolean exists = false;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(CHECK_CAT_TITLE_IS_EXISTS);
		int index = 1;
		stmt.setLong(index++, steEntId);
		stmt.setString(index++, kca_title);
		stmt.setInt(index++, kca_id);
		rs = stmt.executeQuery();
		if (rs.next()) {
			exists = true;
		}
		rs.close();
		stmt.close();
		return exists;
	}

	/**
	 * 删除目录
	 */
	private static final String DEL_CAT_RAL = " delete from knowCatalogRelation where kcr_child_kca_id = ? ";
	private static final String DEL_CAT = " delete from knowCatalog where kca_id = ? ";
	public static void del(Connection con, long kca_id) throws SQLException {
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(DEL_CAT_RAL); // 关联信息
		stmt.setLong(1, kca_id);
		stmt.executeUpdate();
		
		stmt = con.prepareStatement(DEL_CAT); // 删除问题
		stmt.setLong(1, kca_id);
		int delCount = stmt.executeUpdate();
		if (delCount != 1) {
			throw new SQLException(" Failed to delete a record of knowCatalog. kca id :" + kca_id);
		}
		stmt.close();
	}

	/**
	 * 检查是否别人已经更新目录
	 */
	private static final String KCA_HAS_UPDATED = "SELECT kca_id FROM knowCatalog WHERE kca_id = ? AND kca_update_timestamp = ?";
	public static boolean hasUpdated(Connection con, int kca_id, Timestamp updateDate) throws SQLException {
		boolean hasUpdated = true;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		stmt = con.prepareStatement(KCA_HAS_UPDATED);
		int index = 1;
		stmt.setInt(index++, kca_id);
		stmt.setTimestamp(index++, updateDate);
		rs = stmt.executeQuery();
		if (rs.next()) {
			hasUpdated = false;
		}
		rs.close();
		stmt.close();
		return hasUpdated;
	}
	
	public static final String _GET_CATALOG_TREE = "select kca_id from knowCatalog"
			+ " inner join tcTrainingCenter on (tcr_id = kca_tcr_id and tcr_ste_ent_id = ?) " 
			+ " where kca_type = ?";
	
	public static Vector getCatalogTreeVec(Connection con, long steEntId) throws SQLException {
		Vector vec = new Vector();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		stmt = con.prepareStatement(_GET_CATALOG_TREE);
		int index = 1;
		stmt.setLong(index++, steEntId);
		stmt.setString(index++, KnowCatalogDAO.KCA_TYPE_CATALOG);
		rs = stmt.executeQuery();
		while (rs.next()) {
			int id = rs.getInt("kca_id");
			vec.add(getCatalogTreeNode(con, id));
		}
		rs.close();
		stmt.close();
		return vec;
	}

	public static Map getCatalogTreeNode(Connection con, int kca_id) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String sql = " select kca_id, kca_title, child_count from knowCatalog inner join ( ";
		sql += " select count(kcr_ancestor_kca_id) as child_count, "+kca_id+" as child_kca_id from knowCatalogRelation ";
		sql += " where kcr_type = ? and kcr_parent_ind = ? and kcr_ancestor_kca_id = ? ";
		sql += " ) child on (child_kca_id = kca_id) where kca_id = ? ";
		stmt = con.prepareStatement(sql);
		int index = 1;
//		stmt.setInt(index++, kca_id);
		stmt.setString(index++, KnowCatalogRelationDAO.KCA_PARENT_KCA);
		stmt.setInt(index++, 1);
		stmt.setInt(index++, kca_id);
		stmt.setInt(index++, kca_id);
		rs = stmt.executeQuery();

		int id = 0;
		String text = null;
		int count = 0;
		if (rs.next()) {
			id = rs.getInt("kca_id");
			text = rs.getString("kca_title");
			count = rs.getInt("child_count");
		}
		rs.close();
		stmt.close();

		Map catMap = new LinkedHashMap();
		if (id > 0) {
			catMap.put("id", String.valueOf(id));
			catMap.put("text", text);
			if (count > 0) {
				catMap.put("expanded", new Boolean(true));
				sql = " select kcr_child_kca_id from knowCatalogRelation ";
				sql += " where kcr_type = ? and kcr_parent_ind = ? and kcr_ancestor_kca_id = ? ";
				stmt = con.prepareStatement(sql);
				index = 1;
				stmt.setString(index++, KnowCatalogRelationDAO.KCA_PARENT_KCA);
				stmt.setInt(index++, 1);
				stmt.setInt(index++, kca_id);
				rs = stmt.executeQuery();

				Vector vec = new Vector();
				while (rs.next()) {
					vec.add(getCatalogTreeNode(con, rs.getInt("kcr_child_kca_id")));
				}
				rs.close();
				stmt.close();
				catMap.put("children", vec);
			}
		}
		return catMap;
	}
	
	private static final String GET_TOP_CAT_LST = " select kca_id,kca_title,kca_que_count FROM knowCatalog"
		 	+ " inner join tcTrainingCenter on (tcr_id = kca_tcr_id and tcr_ste_ent_id = ?)"
            + " where kca_type = ? and kca_public_ind = ? order by kca_title asc";
	
	public static Vector getTopCatalogList(Connection con, long steEntId) throws SQLException {
	    Vector topCatVec = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    try {
	        stmt = con.prepareStatement(GET_TOP_CAT_LST);
	        int index = 1;
	        stmt.setLong(index++, steEntId);
	        stmt.setString(index++, KCA_TYPE_CATALOG);
	        stmt.setBoolean(index++, true);
	        rs = stmt.executeQuery();
	        
	        while(rs.next()) {
	            if(topCatVec == null) {
	                topCatVec = new Vector();
	            }
	            KnowCatalogBean catBean = new KnowCatalogBean();
	            catBean.setId(rs.getInt("kca_id"));
	            catBean.setText(rs.getString("kca_title"));
	            topCatVec.addElement(catBean);
	        }
	        
	    } finally {
	        if(stmt != null) {
	            stmt.close();
	        }
	    }
	    return topCatVec;
	}
	
}
