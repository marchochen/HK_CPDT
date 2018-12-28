package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.db.DbKmLink;
import com.cw.wizbank.db.DbKmNode;
import com.cw.wizbank.db.DbKmFolder;
import com.cw.wizbank.db.DbKmObject;
import com.cw.wizbank.db.DbKmNodeAccess;

import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.*;

/**
A database class to get result related to Work Folder and Domain
*/
public class ViewKmFolderManager {

    private static final String SQL_ROOT_FOLDER =
        " SELECT fld_nod_id, nod_order, nod_type, fld_title, fld_obj_cnt "
     +  "   FROM kmNode , kmFolder "
     +  "   WHERE fld_nod_id = nod_id "
     +  "         AND fld_type = ? "
     +  "         AND nod_parent_nod_id is null AND nod_owner_ent_id = ? "
     +  "   ORDER BY nod_order, fld_title ";
    private static final String SQL_ROOT_FOLDER_CPTY = 
            " SELECT fld_nod_id, nod_order, nod_type, fld_title, fld_obj_cnt, fld_cpty "
         +  "   FROM kmNode , kmFolder "
         +  "   WHERE fld_nod_id = nod_id " 
         +  "         AND fld_type = ? "
         +  "         AND nod_parent_nod_id is null AND nod_owner_ent_id = ? "
         +  "   ORDER BY nod_order, fld_title ";
    private static final String SQL_FOLDER_CPTY_UNSPECI = 
        " SELECT fld_nod_id, nod_order, nod_type, fld_title, fld_obj_cnt, fld_cpty "
     +  "   FROM kmNode , kmFolder "
     +  "   WHERE fld_nod_id = nod_id " 
     +  "         AND fld_type = ? "
     +  "         AND nod_parent_nod_id is null AND nod_owner_ent_id = ? AND (fld_cpty ='' or fld_cpty is null)"
     +  "   ORDER BY nod_order, fld_title ";
    private static final String SQL_FOLDER_CPTY = 
        " SELECT distinct fld_cpty"
     +  "   FROM kmNode , kmFolder "
     +  "   WHERE fld_nod_id = nod_id " 
     +  "         AND fld_cpty is not null "
     +  "         AND nod_parent_nod_id is null "
     +  "   ORDER BY fld_cpty ";

    private static final String SQL_ALL_CHILD_FOLDERS =
        " SELECT fld_nod_id  "
     +  "   FROM kmNode , kmFolder "
     +  "   WHERE fld_nod_id = nod_id "
     +  "         AND nod_ancestor like ? " ;

    private static final String SQL_FOLDER_TITLE  = " SELECT fld_nod_id, fld_title "
                                                + "  From kmNode, kmFolder "
                                                + " WHERE nod_id = fld_nod_id "
                                                + " AND nod_id IN ";

    /**
    Get the ancestor xml of a node and not including itself
    */
    public static String getFolderAncestorAsXML(Connection con, long node_id)
        throws SQLException {

        Vector nodeVec = new Vector();
        nodeVec.addElement(new Long(node_id));

        Hashtable ancestorHash = getFolderAncestorAsXML(con, nodeVec, false);
        return ((String) ancestorHash.get(new Long(node_id)));

    }

    /**
    Get the ancestor xml of a dummy object (e.g. in the case of insert)
    @param con Connection to databse
    @param node_id nod_id of the parent folder node of the virtual object
    @return XML of ancestor list contains node_id's ancestor and itself
    */
    public static String getVirtualObjectAncestorAsXML(Connection con, long node_id)
        throws SQLException {

        Vector nodeVec = new Vector();
        nodeVec.addElement(new Long(node_id));

        Hashtable ancestorHash = getFolderAncestorAsXML(con, nodeVec, true);
        return ((String) ancestorHash.get(new Long(node_id)));
    }

    /**
    Get the ancestor xml of a list of nodes
    @param nodeVec vector of the node
    @param bIncludeSelf whether include the node itself in the ancestor list
    @return Hashtable with node id as keys and ancesotr xml of the node as value
    */
    public static Hashtable getFolderAncestorAsXML(Connection con, Vector nodeVec, boolean bIncludeSelf)
        throws SQLException {

        // Storing the real ancestor list as a vector (include itself if bIncludeSelf is true)
        Hashtable ancestorVecHash = new Hashtable();

        Hashtable ancestorHash = DbKmNode.getAncestor(con, nodeVec);
        // Storing all the folder that needs to get the folder title
        Vector folderVec = new Vector();
        Vector ancestorVec = null;
        long[] ancestor_ids = null;
        Long folderID = null;
        Long curNodeID = null;
        // Get the required folder
        // Construct each node's ancestor as a vector
        for (int i=0;i<nodeVec.size();i++) {
            curNodeID = (Long) nodeVec.elementAt(i);
            String ancestor = (String) ancestorHash.get(curNodeID);
            ancestorVec = new Vector();
            if (ancestor != null) {
                ancestor_ids = cwUtils.splitToLong(ancestor, DbKmNode.ID_SEPARATOR);
                for (int j=0;j<ancestor_ids.length;j++) {
                    folderID = new Long(ancestor_ids[j]);
                    if (!folderVec.contains(folderID)) {
                        folderVec.addElement(folderID);
                    }
                    ancestorVec.addElement(folderID);
                }
            }
            if (bIncludeSelf) {
                if (!folderVec.contains(curNodeID)) {
                    folderVec.addElement(curNodeID);
                }
                ancestorVec.addElement(curNodeID);
            }
            ancestorVecHash.put(curNodeID, ancestorVec);
        }

        // Get all the folder's title
        Hashtable folderHash = new Hashtable();
        if (folderVec.size() > 0) {
            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement(SQL_FOLDER_TITLE + cwUtils.vector2list(folderVec));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    folderHash.put(new Long(rs.getLong("fld_nod_id")), rs.getString("fld_title"));
                }
            } finally {
                if(stmt!=null) stmt.close();
            }
        }

        // Storing the ancestor xml of each node
        Hashtable ancestorXMLHash = new Hashtable();
        StringBuffer xml = null;
        for (int i=0;i<nodeVec.size();i++) {
            xml = new StringBuffer();
            xml.append("<ancestor_node_list>");
            curNodeID = (Long) nodeVec.elementAt(i);
            ancestorVec = (Vector)  ancestorVecHash.get(curNodeID);

            for (int j=0;j<ancestorVec.size();j++) {
                folderID = (Long) ancestorVec.elementAt(j);

                xml.append("<node id=\"").append(folderID.longValue()).append("\" ")
                   .append(" nature=\"").append(DbKmFolder.getFolderNature(con, folderID.longValue())).append("\">")
                   .append("<title>").append(cwUtils.esc4XML((String) folderHash.get(folderID))).append("</title>")
                   .append("</node>");
            }
            xml.append("</ancestor_node_list>");
            ancestorXMLHash.put(curNodeID, xml.toString());
        }

        return ancestorXMLHash;
    }

    /**
    Get the top Doman or Work Folder of a site
    */
    public static Vector getRootFolders(Connection con, String fld_type, long owner_ent_id)
        throws SQLException {

        DbKmFolder fld = null;
        Vector folderVec = new Vector();
        PreparedStatement stmt = null;
        stmt = con.prepareStatement(SQL_ROOT_FOLDER);
        stmt.setString(1, fld_type);
        stmt.setLong(2, owner_ent_id);

        ResultSet rs = stmt.executeQuery();
        while (rs.next())  {
            fld = new DbKmFolder();
            fld.nod_order = rs.getLong("nod_order");
            fld.nod_type = rs.getString("nod_type");
            fld.fld_nod_id = rs.getLong("fld_nod_id");
            fld.fld_type = fld_type;
            fld.fld_title = rs.getString("fld_title");
            fld.fld_obj_cnt = rs.getLong("fld_obj_cnt");
            fld.target_nod_id = fld.fld_nod_id;
            folderVec.addElement(fld);
        }
        rs.close();
        stmt.close();
        return folderVec;

    }
    /**
    Get the top Doman or Work Folder of a site
    */
	public static Vector getRootFolders(Connection con, String fld_type, long owner_ent_id, String fld_cpty_) throws SQLException {
		Vector folderVec = new Vector();
		if (fld_cpty_.equalsIgnoreCase(cwTree.ALL))
			return getRootFolders(con, fld_type, owner_ent_id);
		DbKmFolder fld = null;
		PreparedStatement stmt = null;
		if (fld_cpty_.equalsIgnoreCase(cwTree.UNSPECIFIED)) {
			stmt = con.prepareStatement(SQL_FOLDER_CPTY_UNSPECI);
			stmt.setString(1, fld_type);
			stmt.setLong(2, owner_ent_id);
		} else {
			stmt = con.prepareStatement(SQL_ROOT_FOLDER_CPTY);
			stmt.setString(1, fld_type);
			stmt.setLong(2, owner_ent_id);
		}
		ResultSet rs = stmt.executeQuery();
        while (rs.next())  {
			boolean match = false;
			String fld_cpty = rs.getString("fld_cpty");
			if (fld_cpty != null && !fld_cpty.trim().equalsIgnoreCase("")) {
				if (fld_cpty.indexOf(",") > 0) {
					String[] str = cwUtils.splitToString(fld_cpty, ",");
					if (str != null) {
						for (int i = 0; i < str.length; i++) {
							String temp = str[i];
							if (temp != null && fld_cpty_ != null && temp.trim().equals(fld_cpty_)) {
								match = true;
								break;
							}
						}
					}
				} else {
					if (fld_cpty != null && fld_cpty_ != null && fld_cpty.trim().equals(fld_cpty_)) {
						match = true;
					}
				}
			}else if (fld_cpty_ != null && fld_cpty_.equalsIgnoreCase(cwTree.UNSPECIFIED) && fld_cpty == null) {
				match = true;
			}
			if (match) {
				fld = new DbKmFolder();
				fld.nod_order = rs.getLong("nod_order");
				fld.nod_type = rs.getString("nod_type");
				fld.fld_nod_id = rs.getLong("fld_nod_id");
				fld.fld_type = fld_type;
				fld.fld_title = rs.getString("fld_title");
				fld.fld_obj_cnt = rs.getLong("fld_obj_cnt");
				fld.target_nod_id = fld.fld_nod_id;
				folderVec.addElement(fld);
			}
        }
        rs.close();
        stmt.close();
        return folderVec;

    }
    /**
    Get the top Doman or Work Folder of a site
    */
    public static Vector getFoldersCPTY(Connection con) throws SQLException {
    	Vector folderVec = new Vector();
    	PreparedStatement stmt = null;
    	stmt = con.prepareStatement(SQL_FOLDER_CPTY);

    	ResultSet rs = stmt.executeQuery();
    	while (rs.next()) {
    		String fld_cpty = rs.getString("fld_cpty");
    		if (fld_cpty != null && !fld_cpty.trim().equalsIgnoreCase("")) {
    			if (fld_cpty.indexOf(",") > 0) {
    				String[] str = cwUtils.splitToString(fld_cpty, ",");
    				if (str != null) {
    					for (int i = 0; i < str.length; i++) {
    						String temp = str[i];
    						if (temp != null && !folderVec.contains(temp.trim())) {
    							folderVec.addElement(temp.trim());
    						}
    					}
    				}
    			} else {
    				if (fld_cpty != null && !folderVec.contains(fld_cpty.trim())) {
    					folderVec.addElement(fld_cpty.trim());
    				}
    			}
    		}
    	}
    	rs.close();
    	stmt.close();
    	return folderVec;
    }

    /**
    Get all the child folder (non-link) of a node
    */
    public static Vector getAllChildFoldersID(Connection con, long parent_id)
        throws SQLException {

        PreparedStatement stmt = null;
        stmt = con.prepareStatement(SQL_ALL_CHILD_FOLDERS);
        stmt.setString(1, "% " + parent_id + " %");
        Vector childVec = new Vector();
        ResultSet rs = stmt.executeQuery();
        while (rs.next())  {
            childVec.addElement(new Long(rs.getLong("fld_nod_id")));
        }
        rs.close();
        return childVec;

    }

    /**
    Get the sub-domain/sub-folder of a node
    */
    public static Vector getChildFolders(Connection con, long parent_id)
        throws SQLException {
        Vector parentVec = new Vector();
        parentVec.addElement(new Long(parent_id));
        Hashtable folderHash = getChildFoldersHash(con, parentVec);
        Vector childVec = (Vector) folderHash.get(new Long(parent_id));
        if (childVec == null) {
            childVec = new Vector();
        }
        return childVec;
    }

    /**
    Get the sub-domain/sub-folder of a list of node
    */
    public static Hashtable getChildFoldersHash(Connection con, Vector parentVec)
        throws SQLException {

        String id_lst = new String();
        if (parentVec == null || parentVec.size() ==0) {
            id_lst = "(0)";
        }else {
            id_lst = cwUtils.vector2list(parentVec);
        }

        String sql = new String();
        sql = " SELECT nod_parent_nod_id AS PARENTID, fld_nod_id AS NODEID, nod_order AS NORDER, fld_title AS NTITLE, nod_type AS NTYPE, fld_nod_id AS TARGETID, fld_obj_cnt AS NOBJCNT "
            + "   FROM kmNode , kmFolder "
            + "   WHERE fld_nod_id = nod_id and nod_parent_nod_id IN " + id_lst
            + " UNION "
            + " SELECT nod_parent_nod_id AS PARENTID, lnk_nod_id AS NODEID, nod_order AS NORDER, lnk_title AS NTITLE, nod_type AS NTYPE, lnk_target_nod_id AS TARGETID, 0 AS NOBJCNT "
            + "   FROM kmNode , kmLink "
            + "   WHERE lnk_nod_id = nod_id and nod_parent_nod_id IN " + id_lst
            + "     AND lnk_type <> ? "
            + " ORDER BY PARENTID, NORDER, NTITLE ";

        PreparedStatement stmt = null;
        Hashtable folderHash = new Hashtable();

        try {
            Vector childVec = null;
            DbKmFolder fld = null;

            stmt = con.prepareStatement(sql);
            stmt.setString(1, DbKmLink.LINK_TYPE_OBJECT);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long parentID = new Long(rs.getLong("PARENTID"));
                childVec = (Vector) folderHash.get(parentID);
                if (childVec == null) {
                    childVec = new Vector();
                }

                fld = new DbKmFolder();
                fld.fld_nod_id = rs.getLong("NODEID");
                fld.fld_title = rs.getString("NTITLE");
                fld.fld_obj_cnt = rs.getLong("NOBJCNT");
                fld.nod_order = rs.getLong("NORDER");
                fld.nod_type = rs.getString("NTYPE");
                fld.target_nod_id = rs.getLong("TARGETID");
                childVec.addElement(fld);

                folderHash.put(parentID, childVec);
            }
        }finally {
                if(stmt!=null) stmt.close();
        }

        return folderHash;
    }


      public static Vector getChildObjects(Connection con, long parent_id)
        throws SQLException {

           return  ViewKmFolderManager.getChildObjects(con,parent_id,null);

      }


    /**
    Get the child object list of a node
    */
    public static Vector getChildObjects(Connection con, long parent_id, String not_obj_status)
        throws SQLException {

        String sql = new String();
        sql = " SELECT obj_bob_nod_id AS NODEID, obj_title AS NTITLE, nod_type AS NTYPE, obj_bob_nod_id AS TARGETID "
            + "   FROM kmNode , kmObject "
            + "   WHERE obj_bob_nod_id = nod_id and nod_parent_nod_id = ? "
            + "   AND obj_latest_ind = ? ";
         if(not_obj_status != null){
            sql += " and obj_status != ? ";
         }
         sql += " UNION "
            + " SELECT lnk_nod_id AS NODEID, lnk_title AS NTITLE, nod_type AS NTYPE, lnk_target_nod_id AS TARGETID "
            + "   FROM kmNode , kmLink "
            + "   WHERE lnk_nod_id = nod_id and nod_parent_nod_id = ? "
            + "     AND lnk_type= ? "
            + " ORDER BY NTITLE ";

        PreparedStatement stmt = null;
        Vector childVec = new Vector();

        try {
            DbKmObject obj = null;

            stmt = con.prepareStatement(sql);
            int index = 1;
            stmt.setLong(index++, parent_id);
            stmt.setBoolean(index++, true);
            if(not_obj_status != null){
                stmt.setString(index++, not_obj_status);
            }
            stmt.setLong(index++, parent_id);
            stmt.setString(index++, DbKmLink.LINK_TYPE_OBJECT);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                obj = new DbKmObject();
                obj.obj_bob_nod_id = rs.getLong("NODEID");
                obj.obj_title = rs.getString("NTITLE");
                obj.nod_type = rs.getString("NTYPE");
                obj.target_nod_id = rs.getLong("TARGETID");
                childVec.addElement(obj);
            }
        }finally {
                if(stmt!=null) stmt.close();
        }

        return childVec;
    }

    /**
    Get the child folder which are not link
    */
    public static Vector getNonLinkChildFolders(Connection con, long parent_id)
        throws SQLException {

        Vector parentVec = new Vector();
        parentVec.addElement(new Long(parent_id));
        Hashtable folderHash = getNonLinkChildFoldersHash(con, parentVec);
        Vector childVec = (Vector) folderHash.get(new Long(parent_id));
        if (childVec == null) {
            childVec = new Vector();
        }
        return childVec;
    }


    /**
    Get the child folder which are not link
    */
    public static Hashtable getNonLinkChildFoldersHash(Connection con, Vector parentVec)
        throws SQLException {

        String sql = new String();
        sql = " SELECT nod_parent_nod_id, fld_nod_id, fld_type, fld_title, nod_acl_inherit_ind, fld_obj_cnt "
            + "   FROM kmNode , kmFolder "
            + "   WHERE fld_nod_id = nod_id and nod_parent_nod_id IN " + cwUtils.vector2list(parentVec)
            + " ORDER by fld_title ";

        PreparedStatement stmt = null;
        Hashtable folderHash = new Hashtable();
        Vector childVec = null;

        try {
            DbKmFolder fld = null;

            stmt = con.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fld = new DbKmFolder();
                fld.nod_parent_nod_id = rs.getLong("nod_parent_nod_id");
                childVec = (Vector) folderHash.get(new Long(fld.nod_parent_nod_id));
                if (childVec == null) {
                    childVec = new Vector();
                }
                fld.fld_nod_id = rs.getLong("fld_nod_id");
                fld.fld_type = rs.getString("fld_type");
                fld.fld_title = rs.getString("fld_title");
                fld.nod_acl_inherit_ind = rs.getBoolean("nod_acl_inherit_ind");
                fld.target_nod_id = fld.fld_nod_id;
                fld.fld_obj_cnt = rs.getLong("fld_obj_cnt");
                childVec.addElement(fld);
                folderHash.put(new Long(fld.nod_parent_nod_id), childVec);
            }
        }finally {
                if(stmt!=null) stmt.close();
        }

        return folderHash;
    }


    /**
    Get the published node of an objec
    @param objVec vector storing the objects' id
    @param publishedHash return the domain(s) for each node
    @param parentVec return the distinct domain(s) of all those objects
    */
    public static void getPublishedDomains(Connection con, Vector objVec,  Hashtable domainsHash, Vector domainsVec)
        throws SQLException {

        String SQL_GET_PUBLISHED_NODES = "SELECT lnk_target_nod_id, fld_nod_id From "
                + "  kmNode LNODE, kmNode DNODE, kmLink, kmFolder WHERE "
                + "  LNODE.nod_id = lnk_nod_id  "
                + "  AND LNODE.nod_parent_nod_id = DNODE.nod_id "
                + "  AND DNODE.nod_id = fld_nod_id "
                + "  AND fld_type = ? "
                + "  AND lnk_target_nod_id IN ";

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_PUBLISHED_NODES + cwUtils.vector2list(objVec)
                        + " ORDER BY lnk_target_nod_id ");
            stmt.setString(1, DbKmFolder.FOLDER_TYPE_DOMAIN);

            ResultSet rs = stmt.executeQuery();
            Long targetID = null;
            Long nodeID = null;
            Vector publishedVec = null;
            while (rs.next())  {
                targetID = new Long(rs.getLong("lnk_target_nod_id"));
                nodeID = new Long(rs.getLong("fld_nod_id"));
                if (!domainsVec.contains(nodeID)) {
                    domainsVec.addElement(new Long(nodeID.longValue()));
                }
                publishedVec = (Vector) domainsHash.get(targetID);
                if (publishedVec == null) {
                    publishedVec = new Vector();
                }
                publishedVec.addElement(new Long(nodeID.longValue()));
                domainsHash.put(targetID, publishedVec);

            }
        }finally {
                if(stmt!=null) stmt.close();
        }

    }


    public static ResultSet getUserOwnedDomain(Connection con, long usr_ent_id, String usr_grp_lst)
        throws SQLException{

            String SQL = " SELECT fld_nod_id, fld_title "
                       + " FROM kmFolder, kmNodeAccess "
                       + " WHERE nac_nod_id = fld_nod_id "
                       + " AND nac_access_type = ? "
                       + " AND fld_type = ? "
                       + " AND ( nac_ent_id = ? OR nac_ent_id IN " + usr_grp_lst + " ) "
                       + " ORDER BY fld_title ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setString(index++, DbKmNodeAccess.ACCESS_TYPE_OWNER);
            stmt.setString(index++, DbKmFolder.FOLDER_TYPE_DOMAIN);
            stmt.setLong(index++, usr_ent_id);
            return stmt.executeQuery();

        }

}

