package com.cw.wizbank.course.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeTreeNodeRelation;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.util.cwUtils;

/**
 * the view about table aeTreeNode
 * 
 * @author DeanChen
 * 
 */
public class ViewAeTreeNodeDAO {

    public static final String COS_CAT_LST = "cos_cat_lst";

    public static final String COS_CAT_ID_LST = "cos_cat_id_lst";
    
    public static final String MAP_KEY_TND_ID = "tnd_id";
    
    public static final String MAP_KEY_TND_TITLE = "tnd_title";

    private static final String GET_COS_CATALOG_LST = "select distinct parent.tnd_id, parent.tnd_title, parent.tnd_status, cat_status, cat_public_ind"
            + " from aeTreeNode child"
            + " inner join aeCatalog on (child.tnd_cat_id = cat_id) "
            + " inner join aeCatalogAccess on (cac_cat_id = cat_id) "
            + " inner join aeTreeNode parent on (parent.tnd_id = child.tnd_parent_tnd_id) "
            + " where child.tnd_type = ? and child.tnd_itm_id = ? "
            + " and ( "
            + "     cac_ent_id in ( "
            + "         Select ern_ancestor_ent_id  From EntityRelation "
            + "         Where ern_type = ? And ern_child_ent_id = ? "
            + "     ) or cac_ent_id = ? " + " ) ORDER BY parent.tnd_title asc ";

    /**
     * to get the catalog that course belong to.
     * 
     * @param con
     * @param itmId
     * @param userEntId
     * @return
     * @throws SQLException
     */
	public static HashMap getCosCatalogLst(Connection con, long itmId, long userEntId) throws SQLException {
		HashMap cosCatMap = new HashMap();
		Vector cosCatVec = null;
		Vector cosCatIdVec = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(GET_COS_CATALOG_LST);
			int index = 1;
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
			stmt.setLong(index++, itmId);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setLong(index++, userEntId);
			stmt.setLong(index++, userEntId);

            rs = stmt.executeQuery();
            while (rs.next()) {
                if (cosCatVec == null) {
                    cosCatVec = new Vector();
                }
                if (cosCatIdVec == null) {
                    cosCatIdVec = new Vector();
                }
                long tndId = rs.getLong("tnd_id");
                // set course catalog
                HashMap catMap = new HashMap();
                catMap.put(MAP_KEY_TND_ID, new Long(tndId));
                catMap.put(MAP_KEY_TND_TITLE, rs.getString("tnd_title"));
                cosCatVec.addElement(catMap);

                // set course catalog id
                cosCatIdVec.addElement(new Long(tndId));
            }

            cosCatMap.put(COS_CAT_LST, cosCatVec);
            cosCatMap.put(COS_CAT_ID_LST, cosCatIdVec);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return cosCatMap;
    }
    
    private static final String GET_TND_PARETN_TITLE_LST = " select tnr_child_tnd_id, tnr_ancestor_tnd_id, tnd_title from aeTreeNode" +
    		" inner join aeTreeNodeRelation on ( " +
    		"     tnr_ancestor_tnd_id = tnd_id and tnr_type = ? and tnr_child_tnd_id in ? " +
    		"  ) order by tnr_child_tnd_id asc, tnr_order asc ";
    
    /**
     * to get title list of parent tnd by list of child id.
     * @param con
     * @param tndIdList
     * @return title list of parent
     * @throws SQLException
     */
    public static HashMap getParentTndTitle(Connection con, Vector tndIdVec) throws SQLException{
        String tndIdListStr = cwUtils.vector2list(tndIdVec);
        HashMap parentTndLstMap = new HashMap();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = " select tnr_child_tnd_id, tnr_ancestor_tnd_id, tnd_title from aeTreeNode"
                    + " inner join aeTreeNodeRelation on ("
                    + "     tnr_ancestor_tnd_id = tnd_id and tnr_type = ? and tnr_child_tnd_id in "
                    + tndIdListStr
                    + " ) order by tnr_child_tnd_id asc, tnr_order asc";
            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setString(index++, aeTreeNodeRelation.TNR_TYPE_TND_PARENT_TND);
            
            rs = stmt.executeQuery();
            while(rs.next()) {
                Long childTndId = new Long(rs.getLong("tnr_child_tnd_id"));
                String parentTndTitle = rs.getString("tnd_title");
                
                // check whether the childTndId is exist in map
                // and initialize parentTndVec
                Vector parentTndVec = null;
                if(parentTndLstMap.get(childTndId) == null) {
                    parentTndVec = new Vector();
                } else {
                    parentTndVec = (Vector) parentTndLstMap.get(childTndId);
                }
                parentTndVec.addElement(parentTndTitle);
                parentTndLstMap.put(childTndId, parentTndVec);
            }
        }finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return parentTndLstMap;
    }

}
