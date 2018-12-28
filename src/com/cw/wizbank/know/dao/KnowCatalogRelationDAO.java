package com.cw.wizbank.know.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import com.cw.wizbank.know.db.KnowCatalogDB;
import com.cw.wizbank.know.db.KnowCatalogRelationDB;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;

/**
 * @author DeanChen
 * 
 */
public class KnowCatalogRelationDAO {

    public static final String KCA_PARENT_KCA = "KCA_PARENT_KCA";

    public static final String QUE_PARENT_KCA = "QUE_PARENT_KCA";

    private static final String INS_RELATION = "INSERT INTO knowCatalogRelation (kcr_child_kca_id, kcr_ancestor_kca_id, kcr_type, kcr_order, kcr_parent_ind, kcr_create_usr_id, kcr_create_timestamp) VALUES(?,?,?,?,?,?,?) ";

    public void ins(Connection con, KnowCatalogRelationDB relationDb)
            throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(INS_RELATION);
            int index = 1;
            stmt.setInt(index++, relationDb.getKcr_child_kca_id());
            stmt.setInt(index++, relationDb.getKcr_ancestor_kca_id());
            stmt.setString(index++, relationDb.getKcr_type());
            stmt.setInt(index++, relationDb.getKcr_order());
            stmt.setBoolean(index++, relationDb.isKcr_parent_ind());
            stmt.setString(index++, relationDb.getKcr_create_usr_id());
            stmt.setTimestamp(index++, relationDb.getKcr_create_timestamp());

            int insCount = stmt.executeUpdate();
            if (insCount != 1) {
                con.rollback();
                throw new SQLException(
                        " Failed to add a record of knowCatalogRelation. ");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    private static final String GET_RELATION_BY_CAT_ID = " SELECT kcr_ancestor_kca_id, kcr_order "
            + " FROM knowCatalogRelation WHERE kcr_type = ? AND kcr_child_kca_id = ? "
            + " ORDER BY kcr_order ASC ";

    public Vector getRelationByCatId(Connection con, int catId, int queId,
            String userId) throws SQLException {
        Timestamp curTime = cwSQL.getTime(con);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector relationVec = new Vector();
        int order = 0;
        try {
            stmt = con.prepareStatement(GET_RELATION_BY_CAT_ID);
            int index = 1;
            stmt.setString(index++, KCA_PARENT_KCA);
            stmt.setInt(index++, catId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                KnowCatalogRelationDB relationDb = new KnowCatalogRelationDB();
                relationDb.setKcr_child_kca_id(queId);
                relationDb.setKcr_ancestor_kca_id(rs
                        .getInt("kcr_ancestor_kca_id"));
                relationDb.setKcr_type(QUE_PARENT_KCA);
                order = rs.getInt("kcr_order");
                relationDb.setKcr_order(order);
                relationDb.setKcr_parent_ind(false);
                relationDb.setKcr_create_usr_id(userId);
                relationDb.setKcr_create_timestamp(curTime);
                relationVec.addElement(relationDb);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        // add relation of direct parent
        KnowCatalogRelationDB relationDb = new KnowCatalogRelationDB();
        relationDb.setKcr_child_kca_id(queId);
        relationDb.setKcr_ancestor_kca_id(catId);
        relationDb.setKcr_type(QUE_PARENT_KCA);
        order += 1;
        relationDb.setKcr_order(order);
        relationDb.setKcr_parent_ind(true);
        relationDb.setKcr_create_usr_id(userId);
        relationDb.setKcr_create_timestamp(curTime);
        relationVec.addElement(relationDb);

        return relationVec;
    }
    
    public Vector getRelationCatByCatId(Connection con, int catId, int childId,
            String userId) throws SQLException {
        Timestamp curTime = cwSQL.getTime(con);

        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector relationVec = new Vector();
        int order = 0;
        try {
            stmt = con.prepareStatement(GET_RELATION_BY_CAT_ID);
            int index = 1;
            stmt.setString(index++, KCA_PARENT_KCA);
            stmt.setInt(index++, catId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                KnowCatalogRelationDB relationDb = new KnowCatalogRelationDB();
                relationDb.setKcr_child_kca_id(childId);
                relationDb.setKcr_ancestor_kca_id(rs
                        .getInt("kcr_ancestor_kca_id"));
                relationDb.setKcr_type(KCA_PARENT_KCA);
                order = rs.getInt("kcr_order");
                relationDb.setKcr_order(order);
                relationDb.setKcr_parent_ind(false);
                relationDb.setKcr_create_usr_id(userId);
                relationDb.setKcr_create_timestamp(curTime);
                relationVec.addElement(relationDb);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }

        // add relation of direct parent
        KnowCatalogRelationDB relationDb = new KnowCatalogRelationDB();
        relationDb.setKcr_child_kca_id(childId);
        relationDb.setKcr_ancestor_kca_id(catId);
        relationDb.setKcr_type(KCA_PARENT_KCA);
        order += 1;
        relationDb.setKcr_order(order);
        relationDb.setKcr_parent_ind(true);
        relationDb.setKcr_create_usr_id(userId);
        relationDb.setKcr_create_timestamp(curTime);
        relationVec.addElement(relationDb);
        return relationVec;
    }
    
    private static final String GET_DIRECT_PARENT_CAT = " SELECT kcr_ancestor_kca_id FROM knowCatalogRelation " +
    		" WHERE kcr_type = ? AND kcr_child_kca_id = ? AND kcr_parent_ind = ? ";
    
    public static int getDirectParentCat(Connection con, int catId) throws SQLException {
        int parentCatId = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(GET_DIRECT_PARENT_CAT);
            int index = 1;
            stmt.setString(index++, KCA_PARENT_KCA);
            stmt.setInt(index++, catId);
            stmt.setBoolean(index++, true);
            rs = stmt.executeQuery();
            while (rs.next()) {
                parentCatId = rs.getInt("kcr_ancestor_kca_id");
            }
            
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
            
        return parentCatId;
    }
    
    /**
	 * 检查目录是否存在子目录
	 * @param con
	 * @param cat_id
	 * @return
	 */
    private static final String CHECK_CAT_HAS_CHILD_CAT = " SELECT kcr_child_kca_id FROM knowCatalogRelation "
			+ " WHERE kcr_type = ? AND kcr_ancestor_kca_id = ? AND kcr_parent_ind = ? ";
	public static boolean isCatalogHasChildCatalog(Connection con, int cat_id) throws SQLException {
		boolean hasChild = false;

		ResultSet rs = null;
		PreparedStatement stmt = null;

		stmt = con.prepareStatement(CHECK_CAT_HAS_CHILD_CAT);
		int index = 1;
		stmt.setString(index++, KCA_PARENT_KCA);
		stmt.setInt(index++, cat_id);
		stmt.setBoolean(index++, true);
		rs = stmt.executeQuery();
		if (rs.next()) {
			hasChild = true;
		}
		rs.close();
		stmt.close();
		return hasChild;
	}
		
	/**
	 * 检查当前目录下是否有问题
	 * @param con
	 * @param cat_id
	 * @return
	 * @throws SQLException
	 */
	private static final String CHECK_CAT_HAS_QUE = " SELECT kcr_child_kca_id FROM knowCatalogRelation "
		+ " WHERE kcr_type = ? AND kcr_ancestor_kca_id = ? AND kcr_parent_ind = ? ";
	public static boolean isCatalogHasQue(Connection con, int cat_id) throws SQLException {
		boolean hasQue = false;
		
		ResultSet rs = null;
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(CHECK_CAT_HAS_QUE);
		int index = 1;
		stmt.setString(index++, QUE_PARENT_KCA);
		stmt.setInt(index++, cat_id);
		stmt.setBoolean(index++, true);
		rs = stmt.executeQuery();
		if (rs.next()) {
			hasQue = true;
		}
		
		rs.close();
		stmt.close();
		return hasQue;
	}
	
	/**
	 * 获取当前目录下属子目录的数量
	 * @param con
	 * @param kca_id
	 * @return
	 */
	private static final String COUNT_CHILD_CAT = " SELECT count(distinct(kcr_child_kca_id)) as sub_count "
        + " FROM knowCatalogRelation WHERE kcr_type = ? AND kcr_ancestor_kca_id = ? and kcr_parent_ind = ? ";
	public static int getChildCatCount(Connection con, int kca_id) throws SQLException {
		int count = 0;
		
		ResultSet rs = null;
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(COUNT_CHILD_CAT);
		int index = 1;
		stmt.setString(index++, KCA_PARENT_KCA);
		stmt.setInt(index++, kca_id);
		stmt.setInt(index++, 1);
		rs = stmt.executeQuery();
		if (rs.next()) {
			count = rs.getInt("sub_count");
		}
		
		rs.close();
		stmt.close();
		return count;
	}

	/**
	 * 获取当前目录下的目录和问题信息，以XML格式返回
	 * @param con
	 * @param steEntId
	 * @param cat_id
	 * @return
	 */
	public static String getCatalogChildAsXML(Connection con, int kca_id, cwPagination page, loginProfile prof, boolean tc_independent) throws SQLException {
		StringBuffer xml = new StringBuffer("");
		
		ResultSet rs = null;
		PreparedStatement stmt = null;
		if (kca_id > 0) {
			KnowCatalogDB catDb = new KnowCatalogDB();
			catDb.setKca_id(kca_id);
			KnowCatalogDAO dao = new KnowCatalogDAO();
			dao.get(con, catDb);

			StringBuffer sql = new StringBuffer();
			sql.append(" select kca_id, kca_title from knowcatalog where (kca_id in ( ");
			sql.append(" select kcr_ancestor_kca_id from knowCatalogRelation where kcr_child_kca_id = ? ");
			sql.append(" and kcr_type = ?) or kca_id = ?)");
			
			if(!prof.current_role.equalsIgnoreCase("ADM_1") && tc_independent){
				sql.append(" and kca_tcr_id in (select tcn_child_tcr_id from tcrelation where tcn_ancestor = ? union select ? from tcrelation) ");
			}
			
			sql.append(" order by kca_id asc");
			int index = 1;
			stmt = con.prepareStatement(sql.toString());
			stmt.setInt(index++, kca_id);
			stmt.setString(index++, KCA_PARENT_KCA);
			stmt.setInt(index++, kca_id);
			if(!prof.current_role.equalsIgnoreCase("ADM_1") && tc_independent){
				stmt.setLong(index++, prof.my_top_tc_id);
				stmt.setLong(index++, prof.my_top_tc_id);
			}
			rs = stmt.executeQuery();
			xml.append("<nav>");
			while (rs.next()) {
				xml.append("<node node_id=\"").append(rs.getInt("kca_id")).append("\">");
				xml.append("<title>").append(cwUtils.esc4XML(rs.getString("kca_title"))).append("</title>");
				xml.append("</node>");
			}
			stmt.close();
			xml.append("</nav>");
			xml.append("<cur_kca id=\"").append(kca_id).append("\" public_ind=\"").append(catDb.isKca_public_ind() ? 1 : 0);
			xml.append("\" title=\"").append(cwUtils.esc4XML(catDb.getKca_title())).append("\" ");
			xml.append(" create_usr_name=\"").append(cwUtils.esc4XML(dbRegUser.getDisplayBil(con, catDb.getKca_create_usr_id()))).append("\" ");
			xml.append(" update_timestamp=\"").append(catDb.getKca_update_timestamp()).append("\"> ");
		}
		StringBuffer sql = new StringBuffer("");
		sql.append(" select kca_id, kca_code, kca_title, kca_public_ind, kca_que_count, ");
		sql.append(" kca_create_timestamp, kca_update_timestamp ");
		sql.append("  from knowCatalog ");
		sql.append(" inner join tcTrainingCenter on (tcr_id = kca_tcr_id and tcr_ste_ent_id = ?)");
		if (kca_id > 0) {
			sql.append(" inner join knowCatalogRelation on (kca_id = kcr_child_kca_id and kcr_parent_ind = ? and kcr_type = ? and kcr_ancestor_kca_id = ?) ");
		} 
		sql.append(" where kca_type = ?");
		if(!prof.current_role.equalsIgnoreCase("ADM_1") && tc_independent){
			sql.append(" and kca_tcr_id in (select tcn_child_tcr_id from tcrelation where tcn_ancestor = ? union select ? from tcrelation)");
		}
		
		sql.append(" order by kca_que_count desc, kca_title, kca_id ");
		
		stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, prof.root_ent_id);
		if (kca_id > 0) {
			stmt.setInt(index++, 1);
			stmt.setString(index++, KCA_PARENT_KCA);
			stmt.setInt(index++, kca_id);
			stmt.setString(index++, KnowCatalogDAO.KCA_TYPE_NORMAL);
		} else {
			stmt.setString(index++, KnowCatalogDAO.KCA_TYPE_CATALOG);
		}
		if(!prof.current_role.equalsIgnoreCase("ADM_1") && tc_independent){
			stmt.setLong(index++, prof.my_top_tc_id);
			stmt.setLong(index++, prof.my_top_tc_id);
		}
		rs = stmt.executeQuery();
		xml.append("<kca_lst>");
		while (rs.next()) {
			int sub_kca_id = rs.getInt("kca_id");
			String kca_title = rs.getString("kca_title");
			int kca_que_count = rs.getInt("kca_que_count");
			int kca_public_ind = rs.getInt("kca_public_ind");
			xml.append("<kca id=\"").append(sub_kca_id).append("\" title=\"").append(cwUtils.esc4XML(kca_title)).append("\" ");
			xml.append(" public_ind=\"").append(kca_public_ind).append("\"");
			xml.append(" que_count=\"").append(kca_que_count).append("\"");
			xml.append(" sub_kca_count=\"").append(getChildCatCount(con, sub_kca_id)).append("\"/>");
		}
		stmt.close();
		xml.append("</kca_lst>");
		
		if (kca_id > 0) {
	        if (page == null) {
	            page = new cwPagination();
	            page.curPage = 1;
	            page.pageSize = cwPagination.defaultPageSize;
	        }
	        if (page.sortCol == null || page.sortCol.length() == 0) {
	            page.sortCol = "que_id";
	        }
	        if (page.sortOrder == null || page.sortOrder.length() == 0) {
	            page.sortOrder = "desc";
	        }
	        
	        String tmp_sql = " from knowQuestion inner join reguser on ( usr_ent_id = que_create_ent_id )  " 
				 + " inner join knowCatalogRelation on (que_id = kcr_child_kca_id and kcr_parent_ind = ? and kcr_type = ? and kcr_ancestor_kca_id = ?) ";
			StringBuffer queSql = new StringBuffer("");
			StringBuffer cntSql = new StringBuffer("");
			cntSql.append("select count(*) ").append(tmp_sql);
			queSql.append(" select que_id, que_title, que_content, que_type, que_answered_timestamp, ");
			queSql.append(" que_popular_ind, que_popular_timestamp, que_reward_credits, que_status, ");
			queSql.append(" que_create_ent_id, que_create_timestamp, que_update_ent_id, que_update_timestamp, usr_nickname, usr_display_bil, ");
			queSql.append("  kcr_create_timestamp ");
			queSql.append(tmp_sql);
			queSql.append(" order by ").append(page.sortCol).append(" ").append(page.sortOrder);
			
			stmt = con.prepareStatement(cntSql.toString());
			index = 1;
			stmt.setInt(index++, 1);
			stmt.setString(index++, QUE_PARENT_KCA);
			stmt.setInt(index++, kca_id);
			rs = stmt.executeQuery();
			int total = 0;
			if (rs.next()) {
				total = rs.getInt(1);
			}
			stmt.close();
			page.totalRec = total;
			
			page.totalPage = page.totalRec / page.pageSize;
			if (page.totalRec % page.pageSize != 0) {
				page.totalPage++;
			}

			stmt = con.prepareStatement(queSql.toString());
			index = 1;
			stmt.setInt(index++, 1);
			stmt.setString(index++, QUE_PARENT_KCA);
			stmt.setInt(index++, kca_id);
			rs = stmt.executeQuery();
			
			Vector xmlVec = new Vector();
			String name = null;
			while(rs.next()) {
				StringBuffer rec = new StringBuffer();
				rec.append("<que id=\"").append(rs.getInt("que_id")).append("\"");		
				rec.append(" title=\"").append(cwUtils.esc4XML(rs.getString("que_title"))).append("\"");
				rec.append(" create_usr_ent_id=\"").append(rs.getInt("que_create_ent_id")).append("\"");
				
				name = rs.getString("usr_nickname");
				if (name == null || name.length() == 0) {
					name = rs.getString("usr_display_bil");
				}
				rec.append(" create_usr_name=\"").append(cwUtils.esc4XML(name)).append("\"");
				rec.append(" create_timestamp=\"").append(rs.getTimestamp("que_create_timestamp")).append("\"");
				rec.append(" update_timestamp=\"").append(rs.getTimestamp("que_update_timestamp")).append("\"");
				rec.append(" kcr_create_timestamp=\"").append(rs.getTimestamp("kcr_create_timestamp")).append("\"");
				rec.append(" que_type=\"").append(rs.getString("que_type")).append("\" />");
				xmlVec.add(rec.toString());
			}
			stmt.close();
			xml.append("<que_lst>");
			for (int i = (page.curPage - 1) * page.pageSize; i < xmlVec.size() && i < (page.curPage * page.pageSize); i++) {
				String rec = (String) xmlVec.elementAt(i);
				xml.append(rec);
			}
			xml.append("</que_lst>");
			xml.append(page.asXML());
			xml.append("</cur_kca>");
		}
	
		return xml.toString();
	}

	public static Timestamp getQueKcrCreateTimestamp(Connection con, long que_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = null;
		
		StringBuffer queSql = new StringBuffer("");
		queSql.append(" select que_id, kcr_create_timestamp from knowQuestion  ");
		queSql.append(" inner join knowCatalogRelation on (que_id = kcr_child_kca_id and kcr_parent_ind = ? and kcr_type = ? and kcr_child_kca_id = ?) ");
		stmt = con.prepareStatement(queSql.toString());
		int index = 1;
		stmt.setInt(index++, 1);
		stmt.setString(index++, QUE_PARENT_KCA);
		stmt.setLong(index++, que_id);
		rs = stmt.executeQuery();
		
		Timestamp result = null;
		if (rs.next()) {
			result = rs.getTimestamp("kcr_create_timestamp");
		}
		rs.close();
		stmt.close();
		return result;
	}	
	
	private static final String DEL_RELATION = "delete from knowcatalogrelation where kcr_type = ? and kcr_child_kca_id = ?";

	public static void delRelationOfQue(Connection con, long queId) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(DEL_RELATION);
			int index = 1;
			stmt.setString(index++, QUE_PARENT_KCA);
			stmt.setLong(index++, queId);
			stmt.executeUpdate();
		} finally {
			cwSQL.closePreparedStatement(stmt);
		}
	}
	
	public static int getQueParentCatId(Connection con, long que_id) throws SQLException {
		int parentCatId = 0;
		String sql = "SELECT kcr_ancestor_kca_id FROM knowCatalogRelation WHERE kcr_type = ? AND kcr_child_kca_id = ? AND kcr_parent_ind = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setString(index++, QUE_PARENT_KCA);
		stmt.setLong(index++, que_id);
		stmt.setBoolean(index++, true);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			parentCatId = rs.getInt("kcr_ancestor_kca_id");
		}
        if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
		return parentCatId;
	}
}
