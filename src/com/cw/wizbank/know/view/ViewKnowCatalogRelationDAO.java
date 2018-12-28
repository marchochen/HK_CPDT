package com.cw.wizbank.know.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

import com.cw.wizbank.JsonMod.know.bean.CatalogNavBean;
import com.cw.wizbank.JsonMod.know.bean.KnowCatalogBean;
import com.cw.wizbank.JsonMod.know.bean.KnowQuestionBean;
import com.cw.wizbank.know.dao.KnowAnswerDAO;
import com.cw.wizbank.know.dao.KnowCatalogDAO;
import com.cw.wizbank.know.dao.KnowCatalogRelationDAO;
import com.cw.wizbank.know.dao.KnowQuestionDAO;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.util.cwSQL;

/**
 * @author DeanChen
 *
 */
public class ViewKnowCatalogRelationDAO {

	public static final String MAP_KEY_KNOW_CATALOG = "know_catalog";
	public static final String MAP_KEY_KNOW_CATALOG_IDS_ORDER = "know_catalog_ids_order";

    /**
     * to update the question count of catalog increased by one.
     *
     * @param con
     * @param kca_id
     * @throws SQLException
     */
    public static void updCatQueCount(Connection con, int kcaId, int updQueCount)
            throws SQLException {
        PreparedStatement stmt = null;
        Timestamp curTime = cwSQL.getTime(con);
        try {
            String UPD_CAT_QUE_COUNT = " UPDATE knowCatalog SET kca_que_count = kca_que_count + ("
                    + updQueCount
                    + ") , kca_update_timestamp = ? WHERE  kca_id = ?  "
                    + " OR kca_id IN ("
                    + "     SELECT kcr_ancestor_kca_id FROM knowCatalogRelation "
                    + "     WHERE kcr_type = ? AND kcr_child_kca_id = ? "
                    + " ) ";
            stmt = con.prepareStatement(UPD_CAT_QUE_COUNT);
            int index = 1;
            stmt.setTimestamp(index++, curTime);
            stmt.setInt(index++, kcaId);
            stmt.setString(index++, KnowCatalogRelationDAO.KCA_PARENT_KCA);
            stmt.setInt(index++, kcaId);
            stmt.execute();
            int updCount = stmt.getUpdateCount();
            if (updCount < 1) {
                throw new SQLException(" Failed to update table knowCatalog. ");
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    public static final String getKnowCatalogStructureSql(String sortSql) {
    	String sql = " SELECT parent.kca_id AS p_kca_id,parent.kca_title AS p_kca_title,parent.kca_que_count AS p_kca_que_count,child.kca_id AS c_kca_id, child.kca_title AS c_kca_title,child.kca_que_count AS c_kca_que_count "
            + " FROM ("
            + "     SELECT kca_id,kca_title,kca_que_count FROM knowCatalog"
            + " 	inner join tcTrainingCenter on (tcr_id = kca_tcr_id and tcr_ste_ent_id = ?)"
            + "     WHERE kca_type = ? AND kca_public_ind = 1 "
            + " ) parent"
            + " LEFT JOIN knowCatalogRelation ON (parent.kca_id = kcr_ancestor_kca_id "
            + "     AND kcr_type = ? AND kcr_parent_ind = 1) "
            + " LEFT JOIN ( "
            + "     SELECT kca_id,kca_title,kca_que_count FROM knowCatalog "
            + "     WHERE kca_type = ? AND kca_public_ind = 1"
            + " ) child on (kcr_child_kca_id = child.kca_id ) ";
    	if(sortSql != null) {
    		sql += "" + sortSql;
    	}
    	return sql;
	}

    /**
     * to get the structure of know's catalog.
     *
     * @param con
     * @param isSortByCatCount
     * @param steEntId
     * @return the hashmap of catalog's structure
     * @throws SQLException
     */
    public static HashMap getKnowCatalogStucture(Connection con, boolean isShowCount, boolean isSortByCatCount, long steEntId) throws SQLException {
    	HashMap resultMap = new HashMap();
		HashMap catalogMap = null;
		Vector catalogIdsOrderVec = null;

    	PreparedStatement stmt = null;
        ResultSet rs = null;

        String sortSql = null;
        if(isSortByCatCount) {
        	sortSql = " ORDER BY parent.kca_que_count desc, child.kca_que_count desc";
        } else {
        	sortSql = " ORDER BY parent.kca_title asc, child.kca_title asc";
        }

        try {
            stmt = con.prepareStatement(getKnowCatalogStructureSql(sortSql));
            int index = 1;
            stmt.setLong(index++, steEntId);
            stmt.setString(index++, KnowCatalogDAO.KCA_TYPE_CATALOG);
            stmt.setString(index++, KnowCatalogRelationDAO.KCA_PARENT_KCA);
            stmt.setString(index++, KnowCatalogDAO.KCA_TYPE_NORMAL);

            rs = stmt.executeQuery();

            while (rs.next()) {
            	if(catalogMap == null) {
            		catalogMap = new HashMap();
            	}
            	if(catalogIdsOrderVec == null) {
            		catalogIdsOrderVec = new Vector();
            	}

                // set top catalog
                int kcaId = rs.getInt("p_kca_id");

                // set catalog ids order
                Long kcaIdObj = new Long(kcaId);
                int queCount = 0;
                if(!catalogIdsOrderVec.contains(kcaIdObj)) {
                	catalogIdsOrderVec.addElement(kcaIdObj);
                	queCount = getCatVisiableQueCnt(con, kcaId);
                }

                String catTitle = null;
                KnowCatalogBean parentCatalogBean = null;
                if (catalogMap.get(kcaIdObj) != null) {
                    parentCatalogBean = (KnowCatalogBean) catalogMap.get(kcaIdObj);
                } else {
                    parentCatalogBean = new KnowCatalogBean();
                    parentCatalogBean.setId(kcaId);
                    catTitle = rs.getString("p_kca_title");
                    parentCatalogBean.setCount(queCount);
                    parentCatalogBean.setTnd_title(catTitle);
                    if (isShowCount) {
                        catTitle += "  (" + queCount + ")";
                    } else {
                        parentCatalogBean.setQue_count(queCount);
                    }
                    parentCatalogBean.setText(catTitle);
                }

                // add sub-catalog list to top catalog
                Vector childCatalogVec = parentCatalogBean.getChildren();
                if (childCatalogVec == null) {
                    childCatalogVec = new Vector();
                }
                kcaId = rs.getInt("c_kca_id");
                if (kcaId > 0) {
                    KnowCatalogBean childCatalogBean = new KnowCatalogBean();
                    childCatalogBean.setId(kcaId);
                    childCatalogBean.setText(rs.getString("c_kca_title"));
                    queCount = rs.getInt("c_kca_que_count");
                    if (queCount < 0) {
                        queCount = 0;
                    }
                    childCatalogBean.setQue_count(queCount);
                    childCatalogBean.setCount(queCount);

                    childCatalogVec.addElement(childCatalogBean);
                    parentCatalogBean.setChildren(childCatalogVec);
                }

                catalogMap.put(kcaIdObj, parentCatalogBean);
            }

            resultMap.put(MAP_KEY_KNOW_CATALOG, catalogMap);
            resultMap.put(MAP_KEY_KNOW_CATALOG_IDS_ORDER, catalogIdsOrderVec);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return resultMap;
    }

    // get sub-layout catalogs by parent id of catalog
    private static final String GET_CAT_STRUCTURE_BY_CAT_ID = " SELECT kca_id, kca_title,kca_que_count "
            + " FROM knowCatalog "
            + " INNER JOIN knowCatalogRelation ON (kca_id = kcr_child_kca_id AND kcr_parent_ind = ? "
            + " AND kcr_type = ? AND  kcr_ancestor_kca_id = ? )   "
            + " WHERE kca_type= ? AND kca_public_ind = ?  "
            + " ORDER BY kca_que_count desc";

    public static Vector getSubCatStuctByCatId(Connection con, int catId)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector catalogVec = new Vector();
        try {
            stmt = con.prepareStatement(GET_CAT_STRUCTURE_BY_CAT_ID);
            int index = 1;
            stmt.setBoolean(index++, true);
            stmt.setString(index++, KnowCatalogRelationDAO.KCA_PARENT_KCA);
            stmt.setInt(index++, catId);
            stmt.setString(index++, KnowCatalogDAO.KCA_TYPE_NORMAL);
            stmt.setBoolean(index++, true);
            rs = stmt.executeQuery();
            while (rs.next()) {
                KnowCatalogBean catalogBean = new KnowCatalogBean();
                catalogBean.setId(rs.getInt("kca_id"));
                catalogBean.setText(rs.getString("kca_title"));
                catalogBean.setQue_count(rs.getInt("kca_que_count"));
                catalogVec.addElement(catalogBean);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return catalogVec;
    }

    private static final String GET_PARENT_CAT_LIST = " SELECT kca_id, kca_title, kcr_order "
            + " FROM knowCatalogRelation"
            + " INNER JOIN knowCatalog ON ("
            + "     kca_id = kcr_ancestor_kca_id AND kca_public_ind = 1"
            + " ) " + " WHERE kcr_type = ? AND kcr_child_kca_id = ?  ";

    /**
     * to get the list of parent catalog by catalog id.
     *
     * @param con
     * @param catId
     * @return
     * @throws SQLException
     */
    public static Vector getParentCatalogList(Connection con, int catId)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector catNavVec = new Vector();
        try {
            stmt = con.prepareStatement(GET_PARENT_CAT_LIST);
            int index = 1;
            stmt.setString(index++, KnowCatalogRelationDAO.KCA_PARENT_KCA);
            stmt.setInt(index++, catId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CatalogNavBean catNavBean = new CatalogNavBean();
                catNavBean.setOrder(rs.getInt("kcr_order"));
                catNavBean.setId(rs.getInt("kca_id"));
                catNavBean.setTitle(rs.getString("kca_title"));
                catNavVec.addElement(catNavBean);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return catNavVec;
    }

    private static final String GET_CAT_TITLE_BY_CAT_ID = " SELECT kca_title FROM knowCatalog " +
    		" WHERE kca_public_ind = ? and (kca_id IN (  " +
    		"     SELECT kcr_ancestor_kca_id FROM knowCatalogRelation " +
    		"     WHERE kcr_type = ? AND kcr_child_kca_id = ? " +
    		"     ) OR kca_id = ? ) ";

    /**
     * to get title list of all parent catalog and itself by catalog id.
     *
     * @param con
     * @param catId
     * @param keepDuplicate
     * @return
     * @throws SQLException
     */
    public static Vector getCatalogTitleByCatId(Connection con, int catId, boolean keepDuplicate)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector catTitleVec = null;
        try {
            stmt = con.prepareStatement(GET_CAT_TITLE_BY_CAT_ID);
            int index = 1;
            stmt.setBoolean(index++, true);
            stmt.setString(index++, KnowCatalogRelationDAO.KCA_PARENT_KCA);
            stmt.setInt(index++, catId);
            stmt.setInt(index++, catId);

            rs = stmt.executeQuery();
            HashMap catMap = new HashMap();
            while (rs.next()) {
                if (catTitleVec == null) {
                    catTitleVec = new Vector();
                }
                String kcaTitle = rs.getString("kca_title");
                if(keepDuplicate) {
                    catTitleVec.addElement(kcaTitle);
                } else {
                    if(catMap.get(kcaTitle) == null) {
                        catTitleVec.addElement(kcaTitle);
                        catMap.put(kcaTitle, kcaTitle);
                    }
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return catTitleVec;
    }

    private static final String GET_CAT_TITLE_BY_QUE_ID = " SELECT kca_title FROM knowCatalogRelation "
            + " INNER JOIN knowCatalog ON"
            + "     (kca_id = kcr_ancestor_kca_id AND kcr_type = ?)"
            + " WHERE kcr_child_kca_id = ?  ";

    /**
     * to get title list of all parent catalog by question id.
     *
     * @param con
     * @param queId
     * @param keepDuplicate
     * @return
     * @throws SQLException
     */
    public static Vector getCatalogTitleByQueId(Connection con, int queId, boolean keepDuplicate)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Vector catTitleVec = null;
        try {
            stmt = con.prepareStatement(GET_CAT_TITLE_BY_QUE_ID);
            int index = 1;
            stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
            stmt.setInt(index++, queId);

            rs = stmt.executeQuery();
            HashMap catMap = new HashMap();
            while (rs.next()) {
                if(catTitleVec == null) {
                    catTitleVec = new Vector();
                }
                String kcaTitle = rs.getString("kca_title");
                if(keepDuplicate) {
                    catTitleVec.addElement(kcaTitle);
                } else {
                    if(catMap.get(kcaTitle) == null) {
                        catTitleVec.addElement(kcaTitle);
                        catMap.put(kcaTitle, kcaTitle);
                    }
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return catTitleVec;
    }

    private static final String GET_QUE_DETAIL_BY_ID = " SELECT que_id, kcr_ancestor_kca_id, que_title,que_content,que_create_timestamp,que_create_ent_id,usr_display_bil, "
        + " que_type,que_answered_timestamp,que_popular_ind,ans_count, usr_display_bil, usr_nickname, usr_ent_id, urx_extra_43, kcr_create_timestamp"
        + " FROM knowQuestion"
        + " INNER JOIN RegUser ON (que_create_ent_id = usr_ent_id)" +
        		" inner join ReguserExtension on (urx_usr_ent_id = usr_ent_id)"
        + " LEFT JOIN ( "
        + "     SELECT ans_que_id , COUNT(ans_id) ans_count FROM knowAnswer"
        + "     WHERE ans_status = ? GROUP BY ans_que_id"
        + " ) ans ON (que_id = ans.ans_que_id) "
        + " inner join knowCatalogRelation on (kcr_child_kca_id = que_id and kcr_type = ? and kcr_parent_ind = ? ) "
        + " WHERE que_status = ? AND que_id = ? ";

    /**
     * to get the detail of question.
     *
     * @param con
     * @param queID
     * @param uploadUsrDirAbs
     * @param defaultUserPhotoPath
     * @throws SQLException
     */
    public static KnowQuestionBean getQueDetailByQueId(Connection con, int queID, String uploadUsrDirAbs, String defaultUserPhotoPath)
            throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        KnowQuestionBean knowQueBean = null;
        try {
            stmt = con.prepareStatement(GET_QUE_DETAIL_BY_ID);
            int index = 1;
            stmt.setString(index++, KnowAnswerDAO.ANS_STATUS_OK);
            stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
            stmt.setBoolean(index++, true);
            stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
            stmt.setInt(index++, queID);
            rs = stmt.executeQuery();
            String name = null;
            if (rs.next()) {
                knowQueBean = new KnowQuestionBean();
                knowQueBean.setQue_id(rs.getLong("que_id"));
                knowQueBean.setKca_id(rs.getInt("kcr_ancestor_kca_id"));
                knowQueBean.setQue_title(rs.getString("que_title"));
                knowQueBean.setQue_content(rs.getString("que_content"));
                knowQueBean.setQue_create_timestamp(rs
                        .getTimestamp("que_create_timestamp"));
                knowQueBean
                        .setQue_create_ent_id(rs.getInt("que_create_ent_id"));

                name = rs.getString("usr_nickname");
				if (name == null || name.length() == 0) {
					name = rs.getString("usr_display_bil");
				}
				knowQueBean.setUsr_nickname(name);
                knowQueBean.setUsr_ent_id(rs.getLong("usr_ent_id"));
                knowQueBean.setQue_type(rs.getString("que_type"));
                knowQueBean.setQue_answered_timestamp(rs.getTimestamp("que_answered_timestamp"));
                knowQueBean.setQue_popular_ind(rs.getBoolean("que_popular_ind"));
                knowQueBean.setKcr_create_timestamp(rs.getTimestamp("kcr_create_timestamp"));
                knowQueBean.setAns_count(rs.getInt("ans_count"));

                String usrPhotoPath = null;
                String usrPhotoName = rs.getString("urx_extra_43");
                if(usrPhotoName != null && !"".equals(usrPhotoName)) {
                	usrPhotoPath = uploadUsrDirAbs + "/" + rs.getLong("usr_ent_id") + "/" + usrPhotoName;
                } else {
                	usrPhotoPath = uploadUsrDirAbs + "/" + defaultUserPhotoPath;
                }
                knowQueBean.setUsr_photo(usrPhotoPath);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        return knowQueBean;
    }

    /**
     * ???????????μ??????? = ????????????????? + ??????????????????μ??????????
     * ??knowCatalog?е?kca_que_count????????μ??????????????δ???????????μ?????????????????δ??????????????kca_que_count???????????????????????sql???
     * @param con
     * @param cat_id
     * @return
     * @throws SQLException
     */
    public static int getCatVisiableQueCnt(Connection con, long cat_id) throws SQLException {
    	int cnt = 0;
    	StringBuffer sql = new StringBuffer();
    	sql.append("select count(que_id) cnt")
    		.append(" from knowQuestion")
    		.append(" inner join knowCatalogRelation que on (que_id = que.kcr_child_kca_id and que.kcr_ancestor_kca_id = ? and que.kcr_type = ?)")
    		.append(" inner join knowCatalogRelation cat on (cat.kcr_child_kca_id = que_id and cat.kcr_type = ? and cat.kcr_parent_ind = ?)")
    		.append(" inner join knowCatalog on (kca_id = cat.kcr_ancestor_kca_id and kca_public_ind = ?)")
    		.append(" where que_status = ?");
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql.toString());
    		int index = 1;
    		stmt.setInt(index++, new Integer(cat_id+"").intValue());
    		stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
    		stmt.setString(index++, KnowCatalogRelationDAO.QUE_PARENT_KCA);
    		stmt.setBoolean(index++, true);
    		stmt.setBoolean(index++, true);
    		stmt.setString(index++, KnowQuestionDAO.QUE_STATUS_OK);
    		rs = stmt.executeQuery();
    		if(rs.next()) {
    			cnt = rs.getInt("cnt");
    		}
    	} finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
    	}
    	return cnt;
    }
}
