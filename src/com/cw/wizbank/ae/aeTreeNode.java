package com.cw.wizbank.ae;

import com.cw.wizbank.ae.db.DbCatalogItemType;
import com.cw.wizbank.ae.db.sql.SqlStatements;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.*;

//import com.cw.wizbank.accesscontrol.AccessControlWZB;

public class aeTreeNode {

    public static final String TND_LIST_ITM = "item";
    public static final String TND_LIST_USR = "user";

    public static final String TND_STATUS_ON = "ON";
    public static final String TND_STATUS_OFF = "OFF";

    public static final String TND_TYPE_CAT = "CATALOG";
    public static final String TND_TYPE_NORMAL = "NORMAL";
    public static final String TND_TYPE_LINK = "LINK";
    public static final String TND_TYPE_ITEM = "ITEM";

    public static final String NAV_CATALOG = "CATALOG";

    // session keys
    public static final String TND_SEARCH_ARG1 = "TND_SEARCH_ARG1";
    public static final String TND_SEARCH_ARG2 = "TND_SEARCH_ARG2";
    public static final String TND_SEARCH_ARG3 = "TND_SEARCH_ARG3";
    public static final String TND_SEARCH_ARG4 = "TND_SEARCH_ARG4";

    public static final int TND_SEARCH_PAGE_SIZE = 10;

    public long tnd_id;
    public String tnd_title;
    public long tnd_itm_cnt;
    public long tnd_on_itm_cnt;
    public long tnd_cat_id;
    public long tnd_parent_tnd_id;
    public long tnd_link_tnd_id;
    public long tnd_itm_id;
    public Timestamp tnd_create_timestamp;
    public String tnd_create_usr_id;
    public Timestamp tnd_upd_timestamp;
    public String tnd_upd_usr_id;
    public boolean tnd_cat_mobile_ind;
    public String tnd_status;
    public String tnd_type;
    public String tnd_ext1;
    public String tnd_ext2;
    public String tnd_ext3;
    public String tnd_ext4;
    public String tnd_ext5;
    public boolean public_ind; //public ind of Catalog
    // order of the node
    public long tnd_order;
    public String tnd_desc;

    public String tnd_code;
    public long tnd_owner_ent_id;
    public Timestamp tnd_syn_timestamp;

    public static final String TND_TITLE = "TND_TITLE";
    public static final String TND_STATUS = "TND_STATUS";
    public static final String TND_PARENT_TND_ID = "TND_PARENT_TND_ID";
    public static final String TND_LINK_TND_ID = "TND_LINK_TND_ID";
    public static final String TND_ID = "TND_ID";

    //system message
    public static final String NOT_DEL_HAS_CHILD = "AETN01"; //"You cannot delete this catalog since there are nodes attached to it"
    public static final String LOOPING_TREE_MSG = "AETN02"; //"Cannot move node as a loop will be formed"
    public static final String TND_OFFLINE_MSG = "AETN03"; //"Tree node is in offline status"
    public static final String TND_DEAD_LINK_MSG = "AETN04";
    public static final String TND_PUT_ITM_ORPHAN = "AETN05";
    public static final String TND_NOT_PUT_ITM_ORPHAN = "AETN06";
	public static final String TND_ITEM_NAME_EXISTED = "AETN07";
	public static final String TND_NOT_EXIST = "AETN08";

    //del an item node
    //update the item status to OFF if the item becomes to an orphan
    //if the item becomes orphan, return true
    //else return false
    public boolean delItem(Connection con, long owner_ent_id, Timestamp upd_timestamp)
      throws SQLException, qdbException, cwSysMessage, cwException {
        boolean isOrphan=false;
        del(con, owner_ent_id, upd_timestamp);

        if(isItemNode()) {
            aeItem itm = new aeItem();
            itm.itm_id = tnd_itm_id;
            if(itm.isOrphan(con)) {
                Timestamp curTime = cwSQL.getTime(con);
                itm.get(con);
                itm.updItemStatus(con, itm.itm_id, aeItem.ITM_STATUS_OFF, itm.itm_upd_timestamp, itm.itm_owner_ent_id, itm.itm_upd_usr_id, curTime);
                isOrphan = true;
            }
            else
                isOrphan = false;
        }
        else
            throw new qdbException("TreeNode is not an Item Node");
        return isOrphan;
    }

    //owner_ent_id is not used in this case, can remove it from input param
    public void del(Connection con, long owner_ent_id, Timestamp upd_timestamp)
      throws SQLException, cwSysMessage {
        tnd_cat_id = getCatalogId(con);

        if(!isLastUpd(con, upd_timestamp))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

        if(hasChild(con))
            throw new cwSysMessage(NOT_DEL_HAS_CHILD);

        tnd_type = getType(con);
        setLinkIdNull(con, tnd_id);
        tnd_itm_id = getItemId(con);
        aeItem itm = new aeItem();

        if (isItemNode()) {
            itm.itm_id = tnd_itm_id;
        }
        
        if(isLinkNode() || (isItemNode() && !itm.getItemDeprecatedInd(con))) {
            tnd_parent_tnd_id = getParentId(con);
            aeTreeNode parentNode = new aeTreeNode();
            parentNode.tnd_id = tnd_parent_tnd_id;
            parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);
            parentNode.cascadeUpdItmCntBy(con, -1);
            if(isLinkNode()) {
                String status = getNodeStatus(con);
                if(status.equals(TND_STATUS_ON))
                    parentNode.cascadeUpdOnItmCntBy(con, -1);
            }
            else {
                itm = new aeItem();
                itm.itm_id = tnd_itm_id;
                String status = itm.getItemStatus(con);
                if(status.equals(aeItem.ITM_STATUS_ON))
                    parentNode.cascadeUpdOnItmCntBy(con, -1);
            }
        }
        aeTreeNodeRelation.delTnrByTnd(con, tnd_id);
        del(con);
    }


    public void del(Connection con) throws SQLException, cwSysMessage{
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Delete From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        int row = stmt.executeUpdate();
        stmt.close();

        if(row == 0)
            //throw new SQLException(" Cannot find Tree Node, tnd_id = " + tnd_id);
            throw new SQLException (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);
        else if(row > 1)
            throw new SQLException(" More than a Tree Node have tnd_id = " + tnd_id);

    }


    public String getType(Connection con) throws SQLException, cwSysMessage {
        String type;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_type From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL);
            stmt.setLong(1, tnd_id);
            rs = stmt.executeQuery();

            if(rs.next())
                type = rs.getString("tnd_type");
            else
                //throw new SQLException("Cannot find TreeNode. tnd_id = " + tnd_id);
                throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        return type;
    }

    //check if the groups are in a loop
    boolean willNodesLoopBack(Connection con, Vector v, aeTreeNode node)
    throws SQLException, qdbException ,cwSysMessage {
        boolean result;

        if(v.contains(new Long(node.tnd_id)))
            //a loop is found
            result = true;
        else {
            //get the parentID
            node.tnd_parent_tnd_id = node.getParentId(con);
            aeTreeNode parentNode= new aeTreeNode();
            parentNode.tnd_id = node.tnd_parent_tnd_id;

            if(node.tnd_parent_tnd_id == 0)
                //reach the top
                result = false;
            else {
                //search again
                v.addElement(new Long(node.tnd_id));
                result = willNodesLoopBack(con, v, parentNode);
            }
        }
        return result;
    }

    public void updLinkedNodeStatus(Connection con) throws SQLException {

        StringBuffer SQLBuf = new StringBuffer(256);
        SQLBuf.append("Update aeTreeNode Set tnd_status = ? ");
        SQLBuf.append("Where tnd_link_tnd_id = ? ");
        SQLBuf.append("And tnd_type = ? ");
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setString(1, this.tnd_status);
        stmt.setLong(2, this.tnd_id);
        stmt.setString(3, TND_TYPE_LINK);
        stmt.executeUpdate();
        stmt.close();
        return;
    }

    public void upd(Connection con, long owner_ent_id, String upd_usr_id, Timestamp upd_timestamp)
      throws SQLException, qdbException, cwSysMessage {

        if(!isLastUpd(con, upd_timestamp))
            throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);

        String prev_status = getNodeStatus(con);
        String new_status = tnd_status;
        upd(con, upd_usr_id);
        updLinkedNodeStatus(con);

        if(prev_status != null && new_status != null && !prev_status.equals(new_status)) {
            tnd_parent_tnd_id = getParentId(con);
            aeTreeNode parentNode = new aeTreeNode();
            parentNode.tnd_id = tnd_parent_tnd_id;
            parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);
            int sign;
            if(new_status.equals(aeTreeNode.TND_STATUS_OFF))
                sign = -1;
            else
                sign = 1;
            if(isLinkNode() || isItemNode())
                parentNode.cascadeUpdOnItmCntBy(con, sign);
            else {
                long amt = getOnItemCnt(con);
                parentNode.cascadeUpdOnItmCntBy(con, sign*amt);
            }
        }
    }


    public void upd(Connection con, String upd_usr_id) throws SQLException, qdbException ,cwSysMessage {
        try {
            Timestamp cur_time = dbUtils.getTime(con);

            StringBuffer SQLBuf = new StringBuffer(300);
            SQLBuf.append(" Update aeTreeNode Set ");
            SQLBuf.append(" tnd_title = ? ");
            SQLBuf.append(" ,tnd_upd_usr_id = ? ");
            SQLBuf.append(" ,tnd_upd_timestamp = ? ");
            SQLBuf.append(" ,tnd_status = ? ");
            SQLBuf.append(" ,tnd_ext1 = ? ");
            SQLBuf.append(" ,tnd_ext2 = ? ");
            SQLBuf.append(" ,tnd_ext3 = ? ");
            SQLBuf.append(" ,tnd_ext4 = ? ");
            SQLBuf.append(" ,tnd_ext5 = ? ");
            SQLBuf.append(" ,tnd_desc = ? ");

            tnd_type = getType(con);
            if(isLinkNode())
                SQLBuf.append(" ,tnd_link_tnd_id = ").append(tnd_link_tnd_id);
            SQLBuf.append(" Where tnd_id = ? ");
            String SQL = new String(SQLBuf);

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, tnd_title);
            stmt.setString(2, upd_usr_id);
            stmt.setTimestamp(3, cur_time);
            stmt.setString(4, tnd_status);
            stmt.setString(5, tnd_ext1);
            stmt.setString(6, tnd_ext2);
            stmt.setString(7, tnd_ext3);
            stmt.setString(8, tnd_ext4);
            stmt.setString(9, tnd_ext5);
            stmt.setString(10, tnd_desc);
            stmt.setLong(11, tnd_id);
            int row = stmt.executeUpdate();
            stmt.close();

            if(row == 0)
                //throw new SQLException("Cannot find TreeNode. tnd_id = " + tnd_id);
                throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);
            if(row > 1)
                throw new SQLException("More than 1 TreeNode have tnd_id = " + tnd_id);
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public boolean hasChild(Connection con) throws SQLException {
        boolean result;
        int count;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select count(*) From aeTreeNode ");
        SQLBuf.append(" Where tnd_parent_tnd_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            count = rs.getInt(1);
        else
            throw new SQLException(" Cannot get count(*) from aeTreeNode, null ResultSet is returned");

        stmt.close();

        result = count != 0;

        return result;
    }

    public String prepUpdAsXML(Connection con, long owner_ent_id)
      throws SQLException, qdbException, cwSysMessage {

        return contentAsXML(con);
    }

    public String contentHeaderAsXML() {
        StringBuffer xmlBuf = new StringBuffer(2500);
        xmlBuf.append("<node node_id=\"").append(aeUtils.escZero(tnd_id)).append("\"");
        xmlBuf.append(" item_count=\"").append(tnd_itm_cnt).append("\"");
        xmlBuf.append(" on_item_count=\"").append(tnd_on_itm_cnt).append("\"");
        xmlBuf.append(" status=\"").append(aeUtils.escNull(tnd_status)).append("\"");
        xmlBuf.append(" type=\"").append(aeUtils.escNull(tnd_type)).append("\"");
        xmlBuf.append(" tnd_cat_mobile_ind=\"").append(tnd_cat_mobile_ind).append("\"");
        if(isItemNode())
            xmlBuf.append(" itm_id=\"").append(aeUtils.escZero(tnd_itm_id)).append("\"");
        xmlBuf.append(" parent_tnd_id=\"").append(aeUtils.escZero(tnd_parent_tnd_id)).append("\"");
        xmlBuf.append(" cat_id=\"").append(aeUtils.escZero(tnd_cat_id)).append("\"");
        if(isCatNode() || public_ind)
            xmlBuf.append(" public_ind=\"").append(public_ind).append("\"");
        xmlBuf.append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext1))).append("\"");
        xmlBuf.append(" ext2=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext2))).append("\"");
        xmlBuf.append(" ext3=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext3))).append("\"");
        xmlBuf.append(" ext4=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext4))).append("\"");
        xmlBuf.append(" ext5=\"").append(dbUtils.esc4XML(aeUtils.escNull(tnd_ext5))).append("\">");
        xmlBuf.append(dbUtils.NEWL);

        String xml = new String(xmlBuf);
        return xml;
    }


    public String contentAsXML(Connection con) throws SQLException ,cwSysMessage {

        StringBuffer xmlBuf = new StringBuffer(2500);

        xmlBuf.append(contentHeaderAsXML());
        xmlBuf.append(getNavigatorAsXML(con));
        xmlBuf.append(dbUtils.NEWL);

        xmlBuf.append("<title>").append(dbUtils.esc4XML(tnd_title)).append("</title>");
        xmlBuf.append(dbUtils.NEWL);
        xmlBuf.append("<desc>").append(dbUtils.esc4XML(tnd_desc)).append("</desc>");
        xmlBuf.append(dbUtils.NEWL);

        if(isLinkNode()) {
            xmlBuf.append("<link link_id=\"").append(tnd_link_tnd_id).append("\">").append(dbUtils.NEWL);
            aeTreeNode linkNode = new aeTreeNode();
            linkNode.tnd_id = tnd_link_tnd_id;
            linkNode.getNavInfo(con);
            xmlBuf.append(linkNode.getNavigatorAsXML(con));
            xmlBuf.append("</link>");
        }
        else
            xmlBuf.append("<link></link>").append(dbUtils.NEWL);

        xmlBuf.append("<creator usr_id=\"").append(tnd_create_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(tnd_create_timestamp).append("\">");
        //xmlBuf.append(aeCatalog.getUserName(con, tnd_create_usr_id));
        xmlBuf.append("</creator>");
        xmlBuf.append(dbUtils.NEWL);

        xmlBuf.append("<last_updated usr_id=\"").append(tnd_upd_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(tnd_upd_timestamp).append("\">");
        //xmlBuf.append(aeCatalog.getUserName(con, tnd_upd_usr_id));
        xmlBuf.append("</last_updated>");
        xmlBuf.append(dbUtils.NEWL);

        xmlBuf.append("</node>").append(dbUtils.NEWL);
        String xml = new String(xmlBuf);

        return xml;
    }

    public boolean isCatNode() {
        boolean result;
        if(tnd_type == null)
            result = false;
        else result = tnd_type.equals(aeTreeNode.TND_TYPE_CAT);

        return result;
    }

    public boolean isLinkNode() {
        boolean result;
        if(tnd_type == null)
            result = false;
        else result = tnd_type.equals(aeTreeNode.TND_TYPE_LINK);

        return result;
    }

    public boolean isItemNode() {
        boolean result;
        if(tnd_type == null)
            result = false;
        else result = tnd_type.equals(aeTreeNode.TND_TYPE_ITEM);

        return result;
    }



    public String getNodeAndChildrenAsXML(Connection con, long owner_ent_id, long[] usr_ent_ids
                                         ,boolean checkStatusCat, boolean checkStatusItm
                                         ,String list, cwPagination page, loginProfile prof)
      throws SQLException, qdbException, cwSysMessage {

        return getNodeAndChildrenAsXML(con, owner_ent_id, usr_ent_ids
                                         ,checkStatusCat, checkStatusItm
                                         , null
                                         ,list, page, prof);
    }

    // if itmLst is not null, get the item which is in the list only
    public String getNodeAndChildrenAsXML(Connection con, long owner_ent_id, long[] usr_ent_ids
                                         ,boolean checkStatusCat, boolean checkStatusItm
                                         , Vector itmLst
                                         ,String list, cwPagination page, loginProfile prof)
      throws SQLException, qdbException, cwSysMessage {
        String xml;
        if(isLinkNode()) {
            if(tnd_link_tnd_id == 0)
                throw new cwSysMessage(TND_DEAD_LINK_MSG);

            //if this is a link node, get the children of the linked node
            aeTreeNode trueNode = new aeTreeNode();
            trueNode.tnd_id = tnd_link_tnd_id;
            trueNode.get(con, usr_ent_ids, checkStatusCat);
            xml = trueNode.getNodeAndChildrenAsXML(con, owner_ent_id, usr_ent_ids, checkStatusCat, checkStatusItm, itmLst, list, page, prof);
        }
        else {

            StringBuffer xmlBuf = new StringBuffer(2500);
            StringBuffer tc_info = new StringBuffer();
            if(isCatNode()) {
                aeCatalog cat = new aeCatalog();
                cat.cat_id = tnd_cat_id;
                cat.get(con, usr_ent_ids, checkStatusCat);
                tnd_status = cat.cat_status;
                tnd_create_timestamp = cat.cat_create_timestamp;
                tnd_create_usr_id = cat.cat_create_usr_id;
                tnd_upd_timestamp = cat.cat_upd_timestamp;
                tnd_upd_usr_id = cat.cat_upd_usr_id;
                tnd_cat_mobile_ind = cat.cat_mobile_ind;
                public_ind = cat.cat_public_ind;
                //add training center info
                tc_info.append("<training_center id=\"").append(cat.cat_tcr_id).append("\">")
	    			.append("<title>").append(cwUtils.esc4XML(cat.cat_tcr_title)).append("</title>")
	    			.append("<isSuperTcr>").append(cat.cat_tcr_id == DbTrainingCenter.getSuperTcId(con, prof.root_ent_id)).append("</isSuperTcr>")
	    			.append("</training_center>");
            }

            xmlBuf.append(contentHeaderAsXML());
            xmlBuf.append(aeCatalog.existsMobildCatAsXML(con, prof.root_ent_id));
            xmlBuf.append(aeItem.getAllItemTypeTitleInOrg(con, owner_ent_id));
            xmlBuf.append(DbCatalogItemType.getCatalogItemTypeAsXML(con, tnd_cat_id));
            xmlBuf.append(getNavigatorAsXML(con));
            xmlBuf.append(dbUtils.NEWL);

            xmlBuf.append("<title>").append(dbUtils.esc4XML(tnd_title)).append("</title>");
            xmlBuf.append(dbUtils.NEWL);
            xmlBuf.append("<desc>").append(dbUtils.esc4XML(tnd_desc)).append("</desc>");
            xmlBuf.append(dbUtils.NEWL);
            xmlBuf.append("<code>").append(dbUtils.esc4XML(tnd_code)).append("</code>");
            xmlBuf.append(dbUtils.NEWL);

            xmlBuf.append("<creator usr_id=\"").append(tnd_create_usr_id).append("\"");
            xmlBuf.append(" timestamp=\"").append(tnd_create_timestamp).append("\">");
            xmlBuf.append(cwUtils.esc4XML(aeCatalog.getUserName(con, tnd_create_usr_id)));
            xmlBuf.append("</creator>");
            xmlBuf.append(dbUtils.NEWL);

            xmlBuf.append("<last_updated usr_id=\"").append(tnd_upd_usr_id).append("\"");
            xmlBuf.append(" timestamp=\"").append(tnd_upd_timestamp).append("\">");
            xmlBuf.append(cwUtils.esc4XML(aeCatalog.getUserName(con, tnd_upd_usr_id)));
            xmlBuf.append("</last_updated>");
            xmlBuf.append(dbUtils.NEWL);
            Vector child_tnd_vec = new Vector();
            xmlBuf.append(getChildNodesAsXML(con, checkStatusCat, child_tnd_vec));
            if(prof.isLrnRole) {
            	Vector v_catalog_id = new Vector();
            	for(int i=0; i<child_tnd_vec.size();i++) {
            		Long tnd_id = (Long)child_tnd_vec.elementAt(i);
            		v_catalog_id.add(tnd_id);
            	}
            	Hashtable hash_catalog = aeCatalog.getCatalogTargetLrn(con, v_catalog_id, prof.usr_ent_id);
            	xmlBuf.append(aeCatalog.getCatalogByLrnAsXML(hash_catalog));
            }
            xmlBuf.append(dbUtils.NEWL);
            xmlBuf.append(getItemNodesAsXML(con, checkStatusItm, itmLst, page, usr_ent_ids[0], prof.isLrnRole));
            aeCatalogAccess cac = new aeCatalogAccess();
            cac.cac_cat_id = tnd_cat_id;
            if(public_ind) {
                xmlBuf.append(cac.getAssignedSitesAsXML(con));
            } else {
                xmlBuf.append("<assigned_user public_ind=\"").append(public_ind).append("\">").append(dbUtils.NEWL);
                xmlBuf.append(cac.getAssignEntityAsXML(con, owner_ent_id));
                xmlBuf.append("</assigned_user>");
            }
            xmlBuf.append(dbUtils.NEWL);
            //add tc info into xml
            xmlBuf.append(tc_info);

            xmlBuf.append("</node>");
            xml = new String(xmlBuf);
        }
        return xml;
    }


    public void ins(Connection con, long owner_ent_id, String usr_id)
      throws SQLException, cwSysMessage {

        aeTreeNode parentNode = new aeTreeNode();
        parentNode.tnd_id = tnd_parent_tnd_id;
        parentNode.tnd_cat_id = parentNode.getCatalogId(con);
        tnd_cat_id = parentNode.tnd_cat_id;
        if(tnd_link_tnd_id != 0)
            insLinkNode(con, owner_ent_id, usr_id);
        else if(tnd_itm_id != 0)
            insItmNode(con, owner_ent_id, usr_id);
        else{
            insNormalNode(con, owner_ent_id, usr_id);
            aeTreeNodeRelation tnr = new aeTreeNodeRelation();
            tnr.insTnr(con, usr_id, aeTreeNodeRelation.TNR_TYPE_TND_PARENT_TND, this.tnd_id, tnd_parent_tnd_id);
     
            if (tnd_code == null){
                autoGenTndCode(con);
            }
        }
    }


    public void insNormalNode(Connection con, long owner_ent_id, String usr_id)
      throws SQLException, cwSysMessage {
        try {

            Timestamp cur_time = dbUtils.getTime(con);
            tnd_create_timestamp = cur_time;
            tnd_upd_timestamp = cur_time;
            tnd_create_usr_id = usr_id;
            tnd_upd_usr_id = usr_id;

            StringBuffer SQLBuf = new StringBuffer(300);
            SQLBuf.append(" Insert into aeTreeNode ");
            SQLBuf.append(" (tnd_title, tnd_itm_cnt, tnd_on_itm_cnt, tnd_cat_id, tnd_parent_tnd_id, tnd_status, tnd_type ");
            SQLBuf.append(" ,tnd_create_timestamp, tnd_create_usr_id ");
            SQLBuf.append(" ,tnd_upd_timestamp, tnd_upd_usr_id ");
            SQLBuf.append(" ,tnd_ext1, tnd_ext2, tnd_ext3, tnd_ext4, tnd_ext5, tnd_desc, tnd_code, tnd_owner_ent_id) ");
            SQLBuf.append(" Values ");
            SQLBuf.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
            String SQL = new String(SQLBuf);
            PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, tnd_title);
            stmt.setLong(2, tnd_itm_cnt);
            stmt.setLong(3, tnd_on_itm_cnt);
            stmt.setLong(4, tnd_cat_id);
            stmt.setLong(5, tnd_parent_tnd_id);
            stmt.setString(6, tnd_status);
            stmt.setString(7, aeTreeNode.TND_TYPE_NORMAL);
            stmt.setTimestamp(8, tnd_create_timestamp);
            stmt.setString(9, tnd_create_usr_id);
            stmt.setTimestamp(10, tnd_upd_timestamp);
            stmt.setString(11, tnd_upd_usr_id);
            stmt.setString(12, tnd_ext1);
            stmt.setString(13, tnd_ext2);
            stmt.setString(14, tnd_ext3);
            stmt.setString(15, tnd_ext4);
            stmt.setString(16, tnd_ext5);
            stmt.setString(17, tnd_desc);
            stmt.setString(18, tnd_code);
            stmt.setLong(19, owner_ent_id);

            stmt.executeUpdate();
            tnd_id = cwSQL.getAutoId(con, stmt, "aeTreeNode", "tnd_id");
            stmt.close();
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }


    public void insLinkNode(Connection con, long owner_ent_id, String usr_id)
      throws SQLException, cwSysMessage {
        try {

            Timestamp cur_time = dbUtils.getTime(con);
            tnd_create_timestamp = cur_time;
            tnd_upd_timestamp = cur_time;
            tnd_create_usr_id = usr_id;
            tnd_upd_usr_id = usr_id;
            aeTreeNode linkedNode = new aeTreeNode();
            linkedNode.tnd_id = tnd_link_tnd_id;
            //tnd_status = linkedNode.getNodeStatus(con);

            StringBuffer SQLBuf = new StringBuffer(300);
            SQLBuf.append(" Insert into aeTreeNode ");
            SQLBuf.append(" (tnd_title, tnd_itm_cnt, tnd_on_itm_cnt, tnd_cat_id, tnd_parent_tnd_id ");
            SQLBuf.append(" ,tnd_link_tnd_id, tnd_status, tnd_type ");
            SQLBuf.append(" ,tnd_create_timestamp, tnd_create_usr_id ");
            SQLBuf.append(" ,tnd_upd_timestamp, tnd_upd_usr_id ");
            SQLBuf.append(" ,tnd_ext1, tnd_ext2, tnd_ext3, tnd_ext4, tnd_ext5, tnd_desc, tnd_code, tnd_owner_ent_id) ");
            SQLBuf.append(" Values ");
            SQLBuf.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
            String SQL = new String(SQLBuf);
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, tnd_title);
            stmt.setLong(2, 0);  //tnd_itm_cnt = 0
            stmt.setLong(3, 0);
            stmt.setLong(4, tnd_cat_id);
            stmt.setLong(5, tnd_parent_tnd_id);
            stmt.setLong(6, tnd_link_tnd_id);
            stmt.setString(7, tnd_status);
            stmt.setString(8, aeTreeNode.TND_TYPE_LINK);
            stmt.setTimestamp(9, tnd_create_timestamp);
            stmt.setString(10, tnd_create_usr_id);
            stmt.setTimestamp(11, tnd_upd_timestamp);
            stmt.setString(12, tnd_upd_usr_id);
            stmt.setString(13, tnd_ext1);
            stmt.setString(14, tnd_ext2);
            stmt.setString(15, tnd_ext3);
            stmt.setString(16, tnd_ext4);
            stmt.setString(17, tnd_ext5);
            stmt.setString(18, tnd_desc);
            stmt.setString(19, tnd_code);
            stmt.setLong(20, owner_ent_id);

            stmt.executeUpdate();
            stmt.close();

            if(tnd_parent_tnd_id != 0) {
                aeTreeNode parentNode = new aeTreeNode();
                parentNode.tnd_id = tnd_parent_tnd_id;
                parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);
                parentNode.cascadeUpdItmCntBy(con, 1);
                if(tnd_status.equals(TND_STATUS_ON)) {
                    parentNode.cascadeUpdOnItmCntBy(con, 1);
                }
            }
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public long parentItemCount(Connection con, long itm_id) throws SQLException {
        long count;
        final String SQL = " Select count(*) From aeTreeNode "
                         + " Where tnd_parent_tnd_id = ? "
                         + " And tnd_itm_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_parent_tnd_id);
        stmt.setLong(2, itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            count = rs.getLong(1);
        else
            throw new SQLException("Cannot get count(*) from aeTreeNode, tnd_id = " + tnd_parent_tnd_id + ", tnd_itm_id = " + itm_id);
        stmt.close();
        
        return count;
    }

    public long insItmNode(Connection con, long owner_ent_id, String usr_id)
      throws SQLException, cwSysMessage {
        try {
            long result=0;
            if(parentItemCount(con, tnd_itm_id) == 0) { //prevent for duplication
                Timestamp cur_time = dbUtils.getTime(con);
                tnd_create_timestamp = cur_time;
                tnd_upd_timestamp = cur_time;
                tnd_create_usr_id = usr_id;
                tnd_upd_usr_id = usr_id;

                result = insItmNode(con, owner_ent_id);
            }
            return result;
        }
        catch (qdbException e) {
            throw new SQLException(e.getMessage());
      }
    }


    public long insItmNode(Connection con, long owner_ent_id)
      throws SQLException, cwSysMessage {
         StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Insert into aeTreeNode ");
        SQLBuf.append(" (tnd_itm_cnt, tnd_on_itm_cnt, tnd_cat_id, tnd_parent_tnd_id ");
        SQLBuf.append(" ,tnd_itm_id, tnd_type, tnd_title ");
        SQLBuf.append(" ,tnd_create_timestamp, tnd_create_usr_id ");
        SQLBuf.append(" ,tnd_upd_timestamp, tnd_upd_usr_id ");
        SQLBuf.append(" ,tnd_ext1, tnd_ext2, tnd_ext3, tnd_ext4, tnd_ext5, tnd_owner_ent_id, tnd_code) ");
        SQLBuf.append(" Values ");
        SQLBuf.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, 0);  //tnd_itm_cnt = 0
        stmt.setLong(2, 0);
        stmt.setLong(3, tnd_cat_id);
        stmt.setLong(4, tnd_parent_tnd_id);
        stmt.setLong(5, tnd_itm_id);
        stmt.setString(6, aeTreeNode.TND_TYPE_ITEM);
        stmt.setString(7, aeItem.getItemTitle(con, tnd_itm_id));
        stmt.setTimestamp(8, tnd_create_timestamp);
        stmt.setString(9, tnd_create_usr_id);
        stmt.setTimestamp(10, tnd_upd_timestamp);
        stmt.setString(11, tnd_upd_usr_id);
        stmt.setString(12, tnd_ext1);
        stmt.setString(13, tnd_ext2);
        stmt.setString(14, tnd_ext3);
        stmt.setString(15, tnd_ext4);
        stmt.setString(16, tnd_ext5);
        stmt.setLong(17, owner_ent_id);
        stmt.setString(18, tnd_code);
        
        stmt.executeUpdate();
        tnd_id = cwSQL.getAutoId(con, stmt, "aeTreeNode", "tnd_id");
        stmt.close();
        
        if(tnd_parent_tnd_id != 0) {
            aeTreeNode parentNode = new aeTreeNode();
            parentNode.tnd_id = tnd_parent_tnd_id;
            parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);
            aeItem itm = new aeItem();
            itm.itm_id = tnd_itm_id;
            parentNode.cascadeUpdItmCntBy(con, 1);
            if(!itm.isItemOff(con))
                parentNode.cascadeUpdOnItmCntBy(con, 1);
        }
        return tnd_id;
    }


    public long insCatNode(Connection con) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Insert into aeTreeNode ");
        SQLBuf.append(" (tnd_itm_cnt, tnd_on_itm_cnt, tnd_cat_id, tnd_type ");
        SQLBuf.append(" ,tnd_create_timestamp, tnd_create_usr_id ");
        SQLBuf.append(" ,tnd_upd_timestamp, tnd_upd_usr_id ");
        SQLBuf.append(" ,tnd_ext1, tnd_ext2, tnd_ext3, tnd_ext4, tnd_ext5, tnd_title, tnd_desc, tnd_order, tnd_code, tnd_owner_ent_id) ");
        SQLBuf.append(" Values ");
        SQLBuf.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
        stmt.setLong(1, tnd_itm_cnt);
        stmt.setLong(2, tnd_on_itm_cnt);
        stmt.setLong(3, tnd_cat_id);
        stmt.setString(4, aeTreeNode.TND_TYPE_CAT);
        stmt.setTimestamp(5, tnd_create_timestamp);
        stmt.setString(6, tnd_create_usr_id);
        stmt.setTimestamp(7, tnd_upd_timestamp);
        stmt.setString(8, tnd_upd_usr_id);
        stmt.setString(9, tnd_ext1);
        stmt.setString(10, tnd_ext2);
        stmt.setString(11, tnd_ext3);
        stmt.setString(12, tnd_ext4);
        stmt.setString(13, tnd_ext5);
        stmt.setString(14, tnd_title);
        stmt.setString(15, tnd_desc);
        stmt.setLong(16, tnd_order);
        stmt.setString(17, tnd_code);
        stmt.setLong(18, tnd_owner_ent_id);

        stmt.executeUpdate();
        tnd_id = cwSQL.getAutoId(con, stmt, "aeTreeNode", "tnd_id");
        stmt.close();
        
        return tnd_id;
    }


    public boolean isLastUpd(Connection con, Timestamp upd_timestamp) throws SQLException ,cwSysMessage {
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(300);
         SQLBuf.append(" Select tnd_upd_timestamp From aeTreeNode ");
         SQLBuf.append(" Where tnd_id = ? ");
         String SQL = new String(SQLBuf);

         PreparedStatement stmt = con.prepareStatement(SQL);
         stmt.setLong(1, tnd_id);
         ResultSet rs = stmt.executeQuery();
         if(rs.next())
            tnd_upd_timestamp = rs.getTimestamp("tnd_upd_timestamp");
         else
            //throw new SQLException("Cannot find TreeNode, tnd_id = " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);

         if(upd_timestamp == null || tnd_upd_timestamp == null)
            result = false;
         else {
            //upd_timestamp.setNanos(tnd_upd_timestamp.getNanos());
             result = upd_timestamp.equals(tnd_upd_timestamp);
         }
         stmt.close();
         return result;
    }

    public void cascadeUpdOnItmCntBy(Connection con, long on_amt)
    throws SQLException, cwSysMessage {
        updOnItmCntBy(con, on_amt);
        String status = getNodeStatus(con);
        if(tnd_parent_tnd_id != 0  && status != null && !status.equals(TND_STATUS_OFF)) {
            //System.out.println("Goes up to parent, my tnd_id = " + tnd_id);
            aeTreeNode parentNode = new aeTreeNode();
            parentNode.tnd_id = tnd_parent_tnd_id;
            parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);
            parentNode.cascadeUpdOnItmCntBy(con, on_amt);
        }
    }

    public void updOnItmCntBy(Connection con, long on_amt)
      throws SQLException, cwSysMessage {

        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Update aeTreeNode Set ");
        SQLBuf.append(" tnd_on_itm_cnt = tnd_on_itm_cnt + ? ");
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, on_amt);
        stmt.setLong(2, tnd_id);
        int row = stmt.executeUpdate();
        stmt.close();
        //System.out.println("Execute upd on itm cnt");
        //System.out.println(SQL + " on_amt = " + on_amt + " tnd_id = " + tnd_id);
        if(row == 0)
            //throw new SQLException("Cannot find TreeNode. tnd_id = " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);
        else if(row > 1)
            throw new SQLException("More than 1 TreeNode have tnd_id = " + tnd_id);
    }

    public void cascadeUpdItmCntBy(Connection con, long amt)
    throws SQLException, cwSysMessage {
        updItmCntBy(con, amt);
        if(tnd_parent_tnd_id != 0) {
            aeTreeNode parentNode = new aeTreeNode();
            parentNode.tnd_id = tnd_parent_tnd_id;
            parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);
            parentNode.cascadeUpdItmCntBy(con, amt);
        }
    }


    public void updItmCntBy(Connection con, long amt)
      throws SQLException, cwSysMessage {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Update aeTreeNode Set ");
        SQLBuf.append(" tnd_itm_cnt = tnd_itm_cnt + ? ");
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, amt);
        stmt.setLong(2, tnd_id);
        int row = stmt.executeUpdate();
        stmt.close();

        if(row == 0)
            //throw new SQLException("Cannot find TreeNode. tnd_id = " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);
        else if(row > 1)
            throw new SQLException("More than 1 TreeNode have tnd_id = " + tnd_id);
    }

    private long getOnItemCnt(Connection con) throws SQLException, qdbException ,cwSysMessage {
        long itm_cnt;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_on_itm_cnt From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            itm_cnt = rs.getLong("tnd_on_itm_cnt");
        else
            //throw new SQLException ("Cannot find TreeNode. tnd_id = " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);

        stmt.close();
        return itm_cnt;
    }

    private long getItemCnt(Connection con) throws SQLException, qdbException ,cwSysMessage {
        long itm_cnt;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_itm_cnt From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            itm_cnt = rs.getLong("tnd_itm_cnt");
        else
            //throw new SQLException ("Cannot find TreeNode. tnd_id = " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);

        stmt.close();
        return itm_cnt;
    }

    //get the tnd_id, itm_id of the child item nodes matching the input params
    //NOTE: this method is retired!!
    public long[][] getItemNodeId(Connection con, boolean checkStatus, String itm_status
                                 ,String itm_code, String itm_title
                                 ,Timestamp itm_appn_from, Timestamp itm_appn_to
                                 ,Timestamp itm_eff_from, Timestamp itm_eff_to
                                 ,String[] itm_types, boolean exact
                                 ,boolean filterRetire, Timestamp curTime)
      throws SQLException {
        Vector tnd_ids = new Vector();
        Vector itm_ids = new Vector();
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_id, tnd_itm_id From aeTreeNode, aeItem ");
        SQLBuf.append(" Where tnd_parent_tnd_id = ? ");
        SQLBuf.append(" And tnd_type = ? ");
        SQLBuf.append(" And tnd_itm_id is not null ");
        SQLBuf.append(" And itm_id = tnd_itm_id ");

        if(checkStatus) {
            SQLBuf.append(" And itm_status <> ? ");
            SQLBuf.append(" And itm_life_status is null ");
        }
        else if(itm_status != null && itm_status.length() > 0)
            SQLBuf.append( " AND itm_status = ? ");

        if (itm_title != null && itm_title.length() > 0)
            SQLBuf.append(" AND lower(itm_title) LIKE ? ");

        if (itm_code != null && itm_code.length() > 0)
            SQLBuf.append(" AND lower(itm_code) LIKE ? ");

        if (itm_types != null && itm_types.length != 0) {
            SQLBuf.append(" AND ( itm_type = ? ");

            for (int i=1; i<itm_types.length; i++) {
                SQLBuf.append(" OR itm_type = ? ");
            }

            SQLBuf.append(" )");
        }

        if (itm_appn_from != null && !itm_appn_from.equals(aeUtils.EMPTY_DATE))
            SQLBuf.append(" AND itm_appn_start_datetime >= ? ");

        if (itm_appn_to != null && !itm_appn_to.equals(aeUtils.EMPTY_DATE))
            SQLBuf.append(" AND itm_appn_end_datetime <= ? ");

        if (itm_eff_from != null && !itm_eff_from.equals(aeUtils.EMPTY_DATE))
            SQLBuf.append(" AND itm_eff_start_datetime >= ? ");

        if (itm_eff_to != null && !itm_eff_to.equals(aeUtils.EMPTY_DATE))
            SQLBuf.append(" AND itm_eff_end_datetime <= ? ");

        if (filterRetire && curTime != null)
            SQLBuf.append(" AND (itm_eff_end_datetime > ? OR itm_eff_end_datetime is null) ");

        String SQL = new String(SQLBuf);
        //System.out.println(SQL);
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        stmt.setLong(index++, tnd_id);  //set parent = this.tnd_id
        stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
        if(checkStatus)
            stmt.setString(index++, aeItem.ITM_STATUS_OFF);
        else if(itm_status != null && itm_status.length() > 0)
            stmt.setString(index++, itm_status);

        if (itm_title != null && itm_title.length() > 0) {
            if (exact)
                stmt.setString(index++, itm_title.toLowerCase());
            else
                stmt.setString(index++, "%" + itm_title.toLowerCase() + "%");
        }

        if (itm_code != null && itm_code.length() > 0)
            stmt.setString(index++, "%" + itm_code.toLowerCase() + "%");

        if (itm_types != null && itm_types.length != 0) {
            for (int i=0; i<itm_types.length; i++) {
                stmt.setString(index++, itm_types[i]);
            }
        }

        if (itm_appn_from != null && !itm_appn_from.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_appn_from);

        if (itm_appn_to != null && !itm_appn_to.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_appn_to);

        if (itm_eff_from != null && !itm_eff_from.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_eff_from);

        if (itm_eff_to != null && !itm_eff_to.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_eff_to);

        if (filterRetire && curTime != null)
            stmt.setTimestamp(index++, curTime);

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            tnd_ids.addElement(new Long(rs.getLong("tnd_id")));
            itm_ids.addElement(new Long(rs.getLong("tnd_itm_id")));
        }
        stmt.close();
        return aeUtils.vec2long2DimArray(tnd_ids, itm_ids);
    }

    //get the tnd_id, itm_id of the child item nodes matching the input params
    //only search for one item type
    public long[][] getItemNodeId(Connection con, boolean checkStatus, boolean checkLifeStatus
                                 ,String itm_status
                                 ,String itm_code, String itm_title, String itm_title_code
                                 ,Timestamp itm_appn_from, Timestamp itm_appn_to
                                 ,Timestamp itm_eff_from, Timestamp itm_eff_to
                                 ,String itm_appn_from_operator, String itm_appn_to_operator
                                 ,String itm_eff_from_operator, String itm_eff_to_operator
                                 ,String itm_type, String itm_life_status, boolean exact
                                 ,boolean allow_null_datetime, boolean filterRetire
                                 ,Timestamp curTime
                                 ,String itm_appn_from_flag, String itm_appn_to_flag
                                 ,String itm_eff_from_flag, String itm_eff_to_flag)
      throws SQLException {
        Vector tnd_ids = new Vector();
        Vector itm_ids = new Vector();
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_id, tnd_itm_id From aeTreeNode, aeItem ");
        SQLBuf.append(" Where tnd_parent_tnd_id = ? ");
        SQLBuf.append(" And tnd_type = ? ");
        SQLBuf.append(" And tnd_itm_id is not null ");
        SQLBuf.append(" And itm_id = tnd_itm_id ");
        SQLBuf.append(" And itm_run_ind = ? ");
        SQLBuf.append(" And itm_session_ind = ? ");
        SQLBuf.append(" And itm_deprecated_ind = ? ");

        if(checkStatus) {
            SQLBuf.append(" And itm_status <> ? ");
        }
        else if(itm_status != null && itm_status.length() > 0)
            SQLBuf.append( " AND itm_status = ? ");

        if (itm_title != null && itm_title.length() > 0)
            SQLBuf.append(" AND lower(itm_title) LIKE ? ");

        if (itm_code != null && itm_code.length() > 0)
            SQLBuf.append(" AND lower(itm_code) LIKE ? ");

        if (itm_title_code != null && itm_title_code.length() > 0)
            SQLBuf.append(" AND (lower(itm_title) LIKE ? OR lower(itm_code) LIKE (?))");

        if (itm_type != null) {
        	SQLBuf.append(aeItemDummyType.genSqlByItemDummyType(itm_type, null, true));
        }

        if (itm_appn_from != null && !itm_appn_from.equals(aeUtils.EMPTY_DATE)) {
            String itm_appn_datetime;
            if (itm_appn_from_flag != null && itm_appn_from_flag.equalsIgnoreCase("END")) {
                itm_appn_datetime = "itm_appn_end_datetime";
            } else {
                itm_appn_datetime = "itm_appn_start_datetime";
            }
            SQLBuf.append(" AND (").append(itm_appn_datetime).append(" ").append(itm_appn_from_operator).append(" ? ");
            if(allow_null_datetime) {
                SQLBuf.append(" OR ").append(itm_appn_datetime).append(" is null ");
            }
            SQLBuf.append(")");
        }
        if (itm_appn_to != null && !itm_appn_to.equals(aeUtils.EMPTY_DATE)) {
            String itm_appn_datetime;
            if (itm_appn_to_flag != null && itm_appn_to_flag.equalsIgnoreCase("START")) {
                itm_appn_datetime = "itm_appn_start_datetime";
            } else {
                itm_appn_datetime = "itm_appn_end_datetime";
            }
            SQLBuf.append(" AND (").append(itm_appn_datetime).append(" ").append(itm_appn_to_operator).append(" ? ");
            if(allow_null_datetime) {
                SQLBuf.append(" OR ").append(itm_appn_datetime).append(" is null ");
            }
            SQLBuf.append(")");
        }
        if (itm_eff_from != null && !itm_eff_from.equals(aeUtils.EMPTY_DATE)) {
            String itm_eff_datetime;
            if (itm_eff_from_flag != null && itm_eff_from_flag.equalsIgnoreCase("END")) {
                itm_eff_datetime = "itm_eff_end_datetime";
            } else {
                itm_eff_datetime = "itm_eff_start_datetime";
            }
            SQLBuf.append(" AND (").append(itm_eff_datetime).append(" ").append(itm_eff_from_operator).append(" ? ");
            if(allow_null_datetime) {
                SQLBuf.append(" OR ").append(itm_eff_datetime).append(" is null ");
            }
            SQLBuf.append(")");
        }
        if (itm_eff_to != null && !itm_eff_to.equals(aeUtils.EMPTY_DATE)) {
            String itm_eff_datetime;
            if (itm_eff_to_flag != null && itm_eff_to_flag.equalsIgnoreCase("START")) {
                itm_eff_datetime = "itm_eff_start_datetime";
            } else {
                itm_eff_datetime = "itm_eff_end_datetime";
            }
            SQLBuf.append(" AND (").append(itm_eff_datetime).append(" ").append(itm_eff_to_operator).append(" ? ") ;
            if(allow_null_datetime) {
                SQLBuf.append(" OR ").append(itm_eff_datetime).append(" is null ");
            }
            SQLBuf.append(")");
        }
/*        if (itm_appn_from != null && !itm_appn_from.equals(aeUtils.EMPTY_DATE)) {
            SQLBuf.append(" AND (itm_appn_start_datetime ").append(itm_appn_from_operator).append(" ? ");
            if(allow_null_datetime) {
                SQLBuf.append(" OR itm_appn_start_datetime is null ");
            }
            SQLBuf.append(")");
        }
        if (itm_appn_to != null && !itm_appn_to.equals(aeUtils.EMPTY_DATE)) {
            SQLBuf.append(" AND (itm_appn_end_datetime ").append(itm_appn_to_operator).append(" ? ");
            if(allow_null_datetime) {
                SQLBuf.append(" OR itm_appn_end_datetime is null ");
            }
            SQLBuf.append(")");
        }
        if (itm_eff_from != null && !itm_eff_from.equals(aeUtils.EMPTY_DATE)) {
            SQLBuf.append(" AND (itm_eff_start_datetime ").append(itm_eff_from_operator).append(" ? ");
            if(allow_null_datetime) {
                SQLBuf.append(" OR itm_eff_start_datetime is null ");
            }
            SQLBuf.append(")");
        }
        if (itm_eff_to != null && !itm_eff_to.equals(aeUtils.EMPTY_DATE)) {
            SQLBuf.append(" AND (itm_eff_end_datetime ").append(itm_eff_to_operator).append(" ? ");
            if(allow_null_datetime) {
                SQLBuf.append(" OR itm_eff_end_datetime is null ");
            }
            SQLBuf.append(")");
        }*/
        if (filterRetire && curTime != null)
            SQLBuf.append(" AND (itm_eff_end_datetime > ? OR itm_eff_end_datetime is null) ");

        if(checkLifeStatus) {
            SQLBuf.append(" And itm_life_status is null ");
        }
        else if(itm_life_status != null && itm_life_status.length() > 0) {
            if(itm_life_status.equals(aeItem.SEARCH_NULL_LIFE_STATUS)) {
                SQLBuf.append(" And itm_life_status is null ");
            }
            else {
                SQLBuf.append(" And itm_life_status = ? ");
            }
        }
        String SQL = new String(SQLBuf);
        //System.out.println(SQL);
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        stmt.setLong(index++, tnd_id);  //set parent = this.tnd_id
        stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
        stmt.setBoolean(index++, false);
        stmt.setBoolean(index++, false);
        stmt.setBoolean(index++, false);
        if(checkStatus)
            stmt.setString(index++, aeItem.ITM_STATUS_OFF);
        else if(itm_status != null && itm_status.length() > 0)
            stmt.setString(index++, itm_status);

        if (itm_title != null && itm_title.length() > 0) {
            if (exact)
                stmt.setString(index++, itm_title.toLowerCase());
            else
                stmt.setString(index++, "%" + itm_title.toLowerCase() + "%");
        }

        if (itm_code != null && itm_code.length() > 0)
            stmt.setString(index++, "%" + itm_code.toLowerCase() + "%");

        if (itm_title_code != null && itm_title_code.length() > 0) {
            stmt.setString(index++, "%" + itm_title_code.toLowerCase() + "%");
            stmt.setString(index++, "%" + itm_title_code.toLowerCase() + "%");
        }

        if (itm_appn_from != null && !itm_appn_from.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_appn_from);

        if (itm_appn_to != null && !itm_appn_to.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_appn_to);

        if (itm_eff_from != null && !itm_eff_from.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_eff_from);

        if (itm_eff_to != null && !itm_eff_to.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_eff_to);

        if (filterRetire && curTime != null)
            stmt.setTimestamp(index++, curTime);

        if(!checkLifeStatus && itm_life_status != null && itm_life_status.length() > 0) {
            if(!itm_life_status.equals(aeItem.SEARCH_NULL_LIFE_STATUS)) {
                stmt.setString(index++, itm_life_status);
            }
        }
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            tnd_ids.addElement(new Long(rs.getLong("tnd_id")));
            itm_ids.addElement(new Long(rs.getLong("tnd_itm_id")));
        }
        stmt.close();
        return aeUtils.vec2long2DimArray(tnd_ids, itm_ids);
    }

    //get the tnd_id, itm_id of the child item nodes matching the input params
    /*
    public long[][] getItemNodeIdHasRun(Connection con, boolean checkStatus, String itm_status
                                       ,String itm_code, String itm_title
                                       ,Timestamp itm_appn_from, Timestamp itm_appn_to
                                       ,Timestamp itm_eff_from, Timestamp itm_eff_to
                                       ,String itm_appn_from_operator, String itm_appn_to_operator
                                       ,String itm_eff_from_operator, String itm_eff_to_operator
                                       ,String itm_type, String itm_life_status, boolean exact
                                       ,boolean filterRetire, Timestamp curTime)
      */
    public long[][] getItemNodeIdHasRun(Connection con, boolean checkStatus, boolean checkLifeStatus
                                       ,String itm_status, String itm_code, String itm_title
                                       ,String itm_type, String itm_life_status, boolean exact)
      throws SQLException {
        Vector tnd_ids = new Vector();
        Vector itm_ids = new Vector();
        StringBuffer SQLBuf = new StringBuffer(300);
        //SQLBuf.append(" Select tnd_id, tnd_itm_id From aeTreeNode, aeItem parent, aeItem run, aeItemRelation ");
        SQLBuf.append(" Select tnd_id, tnd_itm_id From aeTreeNode, aeItem parent ");
        SQLBuf.append(" Where tnd_parent_tnd_id = ? ");
        SQLBuf.append(" And tnd_type = ? ");
        SQLBuf.append(" And tnd_itm_id is not null ");
        SQLBuf.append(" And parent.itm_id = tnd_itm_id ");
        SQLBuf.append(" And parent.itm_run_ind = ? ");
        SQLBuf.append(" And parent.itm_session_ind = ? ");
        SQLBuf.append(" And parent.itm_deprecated_ind = ? ");

        if(checkStatus) {
            SQLBuf.append(" And parent.itm_status <> ? ");
        }
        else if(itm_status != null && itm_status.length() > 0)
            SQLBuf.append( " AND parent.itm_status = ? ");

        if (itm_title != null && itm_title.length() > 0)
            SQLBuf.append(" AND lower(parent.itm_title) LIKE ? ");

        if (itm_code != null && itm_code.length() > 0)
            SQLBuf.append(" AND lower(parent.itm_code) LIKE ? ");

        if (itm_type != null) {
        	SQLBuf.append(aeItemDummyType.genSqlByItemDummyType(itm_type, "parent", true));
        }

        /*
        if (itm_appn_from != null && !itm_appn_from.equals(aeUtils.EMPTY_DATE))
            SQLBuf.append(" AND run.itm_appn_start_datetime ").append(itm_appn_from_operator).append(" ? ");

        if (itm_appn_to != null && !itm_appn_to.equals(aeUtils.EMPTY_DATE))
            SQLBuf.append(" AND run.itm_appn_end_datetime ").append(itm_appn_to_operator).append(" ? ");

        if (itm_eff_from != null && !itm_eff_from.equals(aeUtils.EMPTY_DATE))
            SQLBuf.append(" AND run.itm_eff_start_datetime ").append(itm_eff_from_operator).append(" ? ");

        if (itm_eff_to != null && !itm_eff_to.equals(aeUtils.EMPTY_DATE))
            SQLBuf.append(" AND run.itm_eff_end_datetime ").append(itm_eff_to_operator).append(" ? ");

        if (filterRetire && curTime != null)
            SQLBuf.append(" AND (run.itm_eff_end_datetime > ? OR run.itm_eff_end_datetime is null) ");
        */
        if(checkLifeStatus) {
            SQLBuf.append(" And parent.itm_life_status is null ");
        }
        else if(itm_life_status != null && itm_life_status.length() > 0) {
            if(itm_life_status.equals(aeItem.SEARCH_NULL_LIFE_STATUS)) {
                SQLBuf.append(" And parent.itm_life_status is null ");
            }
            else {
                SQLBuf.append(" And parent.itm_life_status = ? ");
            }
        }

        String SQL = new String(SQLBuf);
        //System.out.println(SQL);
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        stmt.setLong(index++, tnd_id);  //set parent = this.tnd_id
        stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
        stmt.setBoolean(index++, false);
        stmt.setBoolean(index++, false);
        stmt.setBoolean(index++, false);

        if(checkStatus)
            stmt.setString(index++, aeItem.ITM_STATUS_OFF);
        else if(itm_status != null && itm_status.length() > 0)
            stmt.setString(index++, itm_status);

        if (itm_title != null && itm_title.length() > 0) {
            if (exact)
                stmt.setString(index++, itm_title.toLowerCase());
            else
                stmt.setString(index++, "%" + itm_title.toLowerCase() + "%");
        }

        if (itm_code != null && itm_code.length() > 0)
            stmt.setString(index++, itm_code.toLowerCase());

        /*
        if (itm_appn_from != null && !itm_appn_from.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_appn_from);

        if (itm_appn_to != null && !itm_appn_to.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_appn_to);

        if (itm_eff_from != null && !itm_eff_from.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_eff_from);

        if (itm_eff_to != null && !itm_eff_to.equals(aeUtils.EMPTY_DATE))
            stmt.setTimestamp(index++, itm_eff_to);

        if (filterRetire && curTime != null)
            stmt.setTimestamp(index++, curTime);
        */
        if(!checkLifeStatus && itm_life_status != null && itm_life_status.length() > 0) {
            if(!itm_life_status.equals(aeItem.SEARCH_NULL_LIFE_STATUS)) {
                stmt.setString(index++, itm_life_status);
            }
        }

        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            tnd_ids.addElement(new Long(rs.getLong("tnd_id")));
            itm_ids.addElement(new Long(rs.getLong("tnd_itm_id")));
        }
        stmt.close();
        return aeUtils.vec2long2DimArray(tnd_ids, itm_ids);
    }

    //get the tnd_id of all the child nodes except item nodes
    public long[] getChildNodeId(Connection con, boolean checkStatus) throws SQLException {
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_id From aeTreeNode ");
        SQLBuf.append(" Where tnd_parent_tnd_id = ? ");
        SQLBuf.append(" And tnd_type <> ? ");
        //SQLBuf.append(" And tnd_id not in ").append(list);

        if(checkStatus)
            SQLBuf.append(" And tnd_status <> '").append(TND_STATUS_OFF).append("' ");

        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);  //set parent = this.tnd_id
        stmt.setString(2, aeTreeNode.TND_TYPE_ITEM);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()) {
            v.addElement(new Long(rs.getLong("tnd_id")));
        }
        stmt.close();
        return aeUtils.vec2longArray(v);
    }

    //get the tnd_id of all the child nodes
    public static Vector getChildAndItemNodeVector(Connection con, boolean checkStatus, long node_id, boolean notIncludeItemNode) throws SQLException {
        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(300);

        SQLBuf.append("select tnd_id AS iden, tnd_title AS title, tnd_type AS type, 1 AS flag from aeTreeNode where tnd_parent_tnd_id = ? and tnd_type <> 'ITEM'");

        if (checkStatus) {
            SQLBuf.append(" and tnd_status <> 'OFF'");
        }

        if (! notIncludeItemNode) {
            SQLBuf.append(" union select tnd_id AS iden, tnd_title AS title, tnd_type AS type, 2 AS flag from aeTreeNode, aeItem where tnd_parent_tnd_id = ? and tnd_type = 'ITEM' and tnd_itm_id = itm_id and itm_ref_ind = 0");

            if (checkStatus) {
                SQLBuf.append(" and itm_status <> 'OFF'");
            }
        }

        SQLBuf.append(" order by flag asc, title asc");
        PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
        stmt.setLong(1, node_id);  //set parent = this.tnd_id

        if (! notIncludeItemNode) {
            stmt.setLong(2, node_id);  //set parent = this.tnd_id
        }

        ResultSet rs = stmt.executeQuery();
        aeTreeNode tree_node;

        while(rs.next()) {
            tree_node = new aeTreeNode();
            tree_node.tnd_id = rs.getLong("iden");
            tree_node.tnd_title = rs.getString("title");
            tree_node.tnd_type = rs.getString("type");
            v.addElement(tree_node);
        }

        stmt.close();
        return v;
    }

    public String getChildNodesAsXML(Connection con, boolean checkStatus, Vector child_tnd_vec) throws SQLException, qdbException ,cwSysMessage {
        String xml = "<child_nodes>" + dbUtils.NEWL;

        long child_tnd_id, child_tnd_itm_cnt, child_tnd_link_tnd_id, child_tnd_on_itm_cnt;
        String child_tnd_title, child_tnd_status, child_tnd_type;
        Timestamp child_tnd_create_timestamp, child_tnd_upd_timestamp;
        String child_tnd_create_usr_id, child_tnd_upd_usr_id;
        String child_tnd_ext1, child_tnd_ext2, child_tnd_ext3, child_tnd_ext4, child_tnd_ext5;
        String child_tnd_desc;

        String SQL = " Select tnd_id, tnd_title, tnd_itm_cnt, tnd_on_itm_cnt, "
                   + " tnd_link_tnd_id, "
                   + " tnd_create_timestamp, tnd_create_usr_id, "
                   + " tnd_upd_timestamp, tnd_upd_usr_id, "
                   + " tnd_status, tnd_type, "
                   + " tnd_ext1, tnd_ext2, tnd_ext3, tnd_ext4, tnd_ext5, "
                   + " tnd_desc "
                   + " From aeTreeNode "
                   + " Where tnd_parent_tnd_id = ? "
                   + " And tnd_type <> ? ";

                if(checkStatus)
                    SQL += " And tnd_status <> '" + TND_STATUS_OFF + "' ";

                SQL += " Order by tnd_title asc ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);  //set parent = this.tnd_id
        stmt.setString(2, aeTreeNode.TND_TYPE_ITEM);
        ResultSet rs = stmt.executeQuery();
        if (child_tnd_vec == null) {
        	child_tnd_vec = new Vector();
        }
        while(rs.next()) {
            child_tnd_id = rs.getLong("tnd_id");
            child_tnd_itm_cnt = rs.getLong("tnd_itm_cnt");
            child_tnd_on_itm_cnt = rs.getLong("tnd_on_itm_cnt");
            child_tnd_link_tnd_id = rs.getLong("tnd_link_tnd_id");
            child_tnd_title = rs.getString("tnd_title");
            child_tnd_create_timestamp = rs.getTimestamp("tnd_create_timestamp");
            child_tnd_create_usr_id = rs.getString("tnd_upd_usr_id");
            child_tnd_upd_timestamp = rs.getTimestamp("tnd_upd_timestamp");
            child_tnd_upd_usr_id = rs.getString("tnd_upd_usr_id");
            child_tnd_status = rs.getString("tnd_status");
            child_tnd_type = rs.getString("tnd_type");
            child_tnd_ext1 = rs.getString("tnd_ext1");
            child_tnd_ext2 = rs.getString("tnd_ext2");
            child_tnd_ext3 = rs.getString("tnd_ext3");
            child_tnd_ext4 = rs.getString("tnd_ext4");
            child_tnd_ext5 = rs.getString("tnd_ext5");
            child_tnd_desc = rs.getString("tnd_desc");
            if (child_tnd_desc == null) {
                child_tnd_desc = new String();
            }
            child_tnd_vec.add(new Long(child_tnd_id));
            xml += " <child ";
            xml += " node_id=\"" + aeUtils.escZero(child_tnd_id) + "\"";
            xml += " child_node_count=\"" + getChildNodeCount(con, child_tnd_id, TND_TYPE_NORMAL, checkStatus) + "\"";
            xml += " item_count=\"" + child_tnd_itm_cnt + "\"";
            xml += " on_item_count=\"" + child_tnd_on_itm_cnt + "\"";
            xml += " cur_level_item_count=\"" + aeTreeNode.getChildNodeCount(con, child_tnd_id, aeTreeNode.TND_TYPE_ITEM, checkStatus) + "\"";
            xml += " link_id=\"" + aeUtils.escZero(child_tnd_link_tnd_id) + "\"";
            xml += " status=\"" + aeUtils.escNull(child_tnd_status) + "\"";
            xml += " type=\"" + aeUtils.escNull(child_tnd_type) + "\"";
            xml += " ext1=\"" + dbUtils.esc4XML(aeUtils.escNull(child_tnd_ext1)) + "\"";
            xml += " ext2=\"" + dbUtils.esc4XML(aeUtils.escNull(child_tnd_ext2)) + "\"";
            xml += " ext3=\"" + dbUtils.esc4XML(aeUtils.escNull(child_tnd_ext3)) + "\"";
            xml += " ext4=\"" + dbUtils.esc4XML(aeUtils.escNull(child_tnd_ext4)) + "\"";
            xml += " ext5=\"" + dbUtils.esc4XML(aeUtils.escNull(child_tnd_ext5)) + "\"";
            xml += " >" + dbUtils.NEWL;
            xml += " <title>" + dbUtils.esc4XML(child_tnd_title) + "</title>" + dbUtils.NEWL;
            xml += " <desc>" + dbUtils.esc4XML(child_tnd_desc) + "</desc>" + dbUtils.NEWL;
            xml += " <creator usr_id=\"" + child_tnd_create_usr_id + "\"";
            xml += " timestamp=\"" + child_tnd_create_timestamp + "\"" + "> ";
            //xml += dbUtils.esc4XML(aeCatalog.getUserName(con, child_tnd_create_usr_id));
            xml += " </creator>" + dbUtils.NEWL;
            xml += " <last_updated usr_id=\"" + child_tnd_upd_usr_id + "\"";
            xml += " timestamp=\"" + child_tnd_upd_timestamp + "\"" + "> ";
            //xml += dbUtils.esc4XML(aeCatalog.getUserName(con, child_tnd_upd_usr_id));
            xml += " </last_updated> " + dbUtils.NEWL;
            xml += " </child> " + dbUtils.NEWL;
        }
        xml += "</child_nodes>" + dbUtils.NEWL;
        stmt.close();
        return xml;
    }

    public static int getChildNodeCount(Connection con, long child_tnd_id, String node_type, boolean checkStatus) throws SQLException {
    	String sql = "select count(*) from aeTreeNode left join aeItem on tnd_itm_id = itm_id where tnd_parent_tnd_id = ? and tnd_type = ?";
    	if(checkStatus && node_type.equals(aeTreeNode.TND_TYPE_ITEM)){
    		sql += " and itm_status= ?";
    	} else if(checkStatus && node_type.equals(aeTreeNode.TND_TYPE_NORMAL)){
    		sql += " and tnd_status=?";
    	}
        PreparedStatement stmt = null;
        int count = 0;
        try {
        	stmt = con.prepareStatement(sql);
        	int index = 1;
        	stmt.setLong(index++, child_tnd_id);
        	stmt.setString(index++, node_type);
        	if(checkStatus && node_type.equals(aeTreeNode.TND_TYPE_ITEM)){
        		stmt.setString(index++, aeItem.ITM_STATUS_ON);
        	}else if(checkStatus && node_type.equals(aeTreeNode.TND_TYPE_NORMAL)){
        		stmt.setString(index++, aeTreeNode.TND_STATUS_ON);
        	}
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

	private static final String sql_get_node_item_1 =
        "and itm_status <> ? and itm_life_status is null ";
    private static final String sql_get_node_item_2 =
        "and itm_id in ";
    private static final String sql_get_node_item_3 =
        "order by ";

	private static final String sql_get_node_item_4 = 	  
		 "and (itm_access_type = 'ALL' or itm_access_type is NULL) ";

	private static final String sql_get_node_item_5 = 
		 "select tnd_itm_id, itm_code, itm_type, itm_status, itm_blend_ind,itm_exam_ind,itm_ref_ind,tnd_title "
	   + " , itm_app_approval_type, itm_offline_pkg "
	   + "from aeTreeNode, aeItem "
	   + "where tnd_itm_id = itm_id "
	   + "and tnd_parent_tnd_id = ? "
	   + "and tnd_type = ? "
	   + "and itm_deprecated_ind = ? "
//                 屏蔽考试
       +" AND itm_exam_ind != 1" ;

    public String getItemNodesAsXML(Connection con, boolean checkStatus, Vector itmLst, cwPagination page, long usr_ent_id, boolean isUsrLrnRole)
      throws SQLException, qdbException  ,cwSysMessage{
		
    	PreparedStatement stmt = getItemNodesAsXML_stmt(con, checkStatus, itmLst, page, usr_ent_id, isUsrLrnRole);
        ResultSet rs = stmt.executeQuery();

        StringBuffer xmlBuf = new StringBuffer(2500);
        xmlBuf.append("<item_nodes orderby=\"").append(page.sortCol).append("\"");
        xmlBuf.append(" sortorder=\"").append(page.sortOrder).append("\">");
        xmlBuf.append(dbUtils.NEWL);
        xmlBuf.append(aeTimeField.curTimeAsXML(con));

        int count = 1;
        if (page.pageSize <= 0) {
            page.pageSize = Integer.MAX_VALUE;
            //page.pageSize = cwPagination.defaultPageSize;
        }

        if (page.curPage <= 0) {
            page.curPage = 1;
        }

        while(rs.next()) {
            if ((count>(page.curPage-1)*page.pageSize) && (count<=page.curPage*page.pageSize)) {
                int idx = 1;
                xmlBuf.append("<item item_id=\"")
                      .append(rs.getLong("tnd_itm_id"))
                      .append("\" item_code=\"")
                      .append(cwUtils.esc4XML(rs.getString("itm_code")))
                      .append("\" item_type=\"")
                      .append(rs.getString("itm_type"))
                      .append("\" item_dummy_type=\"")
                      .append(aeItemDummyType.getDummyItemType(rs.getString("itm_type"), rs.getBoolean("itm_blend_ind"), rs.getBoolean("itm_exam_ind"), rs.getBoolean("itm_ref_ind")))
                      .append("\" status=\"")
                      .append(rs.getString("itm_status"))
                      .append("\"><title>")
                      .append(cwUtils.esc4XML(rs.getString("tnd_title")))
                      .append("</title></item>")
                      ;
            }

            page.totalRec++;
            count++;
        }

        stmt.close();
        xmlBuf.append("</item_nodes>");

        page.totalPage = (int)Math.ceil((float)page.totalRec/(float)page.pageSize);

        xmlBuf.append(page.asXML());
        String xml = new String(xmlBuf);
        return xml;
    }

    public PreparedStatement getItemNodesAsXML_stmt(Connection con, boolean checkStatus, Vector itmLst, cwPagination page, long usr_ent_id, boolean isUsrLrnRole) throws SQLException{
    	String groupAncesterSql = dbEntityRelation.getAncestorListSql(usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		String gradeAncesterSql = dbEntityRelation.getAncestorListSql(usr_ent_id, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);

        StringBuffer sqlExec = new StringBuffer();
        StringBuffer sqlcon1 = new StringBuffer();
		StringBuffer sqlcon2 = new StringBuffer();

		String sql_get_node_item =
			" SELECT distinct tnd_itm_id, itm_code, itm_type, itm_status, itm_blend_ind, itm_exam_ind ,itm_ref_ind, tnd_title "
			+ " , itm_app_approval_type, itm_offline_pkg "
			+ " FROM aeTreeNode, aeItem, aeItemTargetRuleDetail"
			+ " WHERE tnd_itm_id = itm_id "
			+ " AND tnd_parent_tnd_id = ? "
			+ " AND tnd_type = ? "
			+ " AND itm_deprecated_ind = ? " +
			" AND ird_itm_id = itm_id" +
                    " AND itm_exam_ind != 1" +
                    " AND (ird_group_id in (" + groupAncesterSql + ") and ird_grade_id in (" + gradeAncesterSql + "))";
				
        if (checkStatus) {
			sqlcon1.append(sql_get_node_item_1);
        }
	    if (itmLst!=null){
	        if (itmLst.size()==0){
    	        itmLst.addElement(new Long(0));
	        }
			sqlcon1.append(sql_get_node_item_2)
	               .append(cwUtils.vector2list(itmLst))
	               ;
	    }

        if(page.sortCol == null || page.sortCol.length() == 0) {
            page.sortCol = "itm_code";
        }

        if(page.sortOrder == null || page.sortOrder.length() == 0) {
            page.sortOrder = "asc";
        }

		sqlcon2.append(sql_get_node_item_3);
		sqlcon2.append(page.sortCol).append(" ").append(page.sortOrder);
        if (!page.sortCol.equalsIgnoreCase("tnd_title")) {
			sqlcon2.append(", tnd_title ").append(page.sortOrder);
        }
		
		if(isUsrLrnRole){
			sqlExec.append(sql_get_node_item).append(sqlcon1);
			sqlExec.append(" UNION ");
			sqlExec.append(sql_get_node_item_5).append(sql_get_node_item_4).append(sqlcon1).append(sqlcon2);
		}else{
			sqlExec.append(sql_get_node_item_5).append(sqlcon1).append(sqlcon2);
		}
        PreparedStatement stmt = con.prepareStatement(sqlExec.toString());
        int index = 1;
		if (isUsrLrnRole) {
			stmt.setLong(index++, tnd_id);  //set the parent_id = this.tnd_id
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
			stmt.setBoolean(index++, false);
			if(checkStatus){
				stmt.setString(index++, aeItem.ITM_STATUS_OFF);				
			}
			stmt.setLong(index++, tnd_id);  //set the parent_id = this.tnd_id
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
			stmt.setBoolean(index++, false);
			if(checkStatus){
				stmt.setString(index++, aeItem.ITM_STATUS_OFF);				
			}
		}else{
			stmt.setLong(index++, tnd_id);  //set the parent_id = this.tnd_id
			stmt.setString(index++, aeTreeNode.TND_TYPE_ITEM);
			stmt.setBoolean(index++, false);
			if(checkStatus){
				stmt.setString(index++, aeItem.ITM_STATUS_OFF);				
			}
		}
		return stmt;
    }

    public long getCatalogId(Connection con) throws SQLException ,cwSysMessage {
        long id;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_cat_id From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            id = rs.getLong("tnd_cat_id");
        else {
        	if( tnd_title != null && tnd_title.length() > 0 ) {
				throw new cwSysMessage ("AECA04");
        	} else {
	            //throw new SQLException("Cannot find TreeNode. tnd_id = " + tnd_id);
    	        throw new cwSysMessage ("AECA04");
        	}
        }
        stmt.close();
        return id;
    }

    public void get(Connection con, long[] usr_ent_ids, boolean checkStatus)
      throws SQLException,  cwSysMessage {
        /*
        tnd_cat_id = getCatalogId(con);

        if(!aeCatalog.isPublic(con, tnd_cat_id) && !aeCatalogAccess.hasAccessRight(con, tnd_cat_id, usr_ent_ids))
            throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
        */

        /*
        if(checkStatus) {
            if(aeCatalog.isCatOff(con, tnd_cat_id))
                throw new cwSysMessage(aeCatalog.CAT_OFFLINE_MSG);

            if(isNodeOff(con))
                throw new cwSysMessage(TND_OFFLINE_MSG);
        }
        */

        get(con);
        /*
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select * From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? " );

        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            tnd_title = rs.getString("tnd_title");
            tnd_itm_cnt = rs.getLong("tnd_itm_cnt");
            tnd_cat_id = rs.getLong("tnd_cat_id");
            tnd_parent_tnd_id = rs.getLong("tnd_parent_tnd_id");
            tnd_link_tnd_id = rs.getLong("tnd_link_tnd_id");
            tnd_itm_id = rs.getLong("tnd_itm_id");
            tnd_create_timestamp = rs.getTimestamp("tnd_create_timestamp");
            tnd_create_usr_id = rs.getString("tnd_create_usr_id");
            tnd_upd_timestamp = rs.getTimestamp("tnd_upd_timestamp");
            tnd_upd_usr_id = rs.getString("tnd_upd_usr_id");
            tnd_status = rs.getString("tnd_status");
            tnd_type = rs.getString("tnd_type");
        }
        else
            //throw new SQLException("Cannot find TreeNode. tnd_id =  " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);

        stmt.close();

        if(isParentNode()) {
            tnd_title = getCatalogTitle(con);
        }
        */
    }

    public void get(Connection con)
      throws SQLException ,cwSysMessage {
        tnd_cat_id = getCatalogId(con);

        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select * From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? " );

        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            tnd_title = rs.getString("tnd_title");
            tnd_itm_cnt = rs.getLong("tnd_itm_cnt");
            tnd_on_itm_cnt = rs.getLong("tnd_on_itm_cnt");
            tnd_cat_id = rs.getLong("tnd_cat_id");
            tnd_parent_tnd_id = rs.getLong("tnd_parent_tnd_id");
            tnd_link_tnd_id = rs.getLong("tnd_link_tnd_id");
            tnd_itm_id = rs.getLong("tnd_itm_id");
            tnd_create_timestamp = rs.getTimestamp("tnd_create_timestamp");
            tnd_create_usr_id = rs.getString("tnd_create_usr_id");
            tnd_upd_timestamp = rs.getTimestamp("tnd_upd_timestamp");
            tnd_upd_usr_id = rs.getString("tnd_upd_usr_id");
            tnd_status = rs.getString("tnd_status");
            tnd_type = rs.getString("tnd_type");
            tnd_ext1 = rs.getString("tnd_ext1");
            tnd_ext2 = rs.getString("tnd_ext2");
            tnd_ext3 = rs.getString("tnd_ext3");
            tnd_ext4 = rs.getString("tnd_ext4");
            tnd_ext5 = rs.getString("tnd_ext5");
            tnd_desc = rs.getString("tnd_desc");
            tnd_owner_ent_id = rs.getLong("tnd_owner_ent_id");
            tnd_code = rs.getString("tnd_code");
            if (tnd_desc == null) {
                tnd_desc = new String();
            }
        }
        else
            //throw new SQLException("Cannot find TreeNode. tnd_id =  " + tnd_id);
            throw new cwSysMessage ("AEIT27");

        stmt.close();

        if(isParentNode()) {
            tnd_title = getCatalogTitle(con);
        }
    }

    public boolean isParentNode() {
        boolean result;
        result = tnd_parent_tnd_id == 0;

        return result;
    }

    public long getParentId(Connection con) throws SQLException ,cwSysMessage {
        long result;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_parent_tnd_id From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? " );
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            result = rs.getLong("tnd_parent_tnd_id");
        }
        else
            //throw new SQLException("Cannot find TreeNode. tnd_id =  " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);

        stmt.close();
        return result;
    }


    public void getNavInfo(Connection con) throws SQLException ,cwSysMessage {
        if(tnd_id != 0) {
            final String SQL = " Select tnd_title, tnd_status, tnd_parent_tnd_id, tnd_itm_id, tnd_cat_id From aeTreeNode "
                             + " Where tnd_id = ? ";

//            StringBuffer SQLBuf = new StringBuffer(300);
//            SQLBuf.append(" Select tnd_title, tnd_parent_tnd_id, tnd_itm_id, tnd_cat_id From aeTreeNode ");
//            SQLBuf.append(" Where tnd_id = ? " );
//            String SQL = new String(SQLBuf);

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, tnd_id);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                tnd_title = rs.getString("tnd_title");
                tnd_status = rs.getString("tnd_status");
                tnd_parent_tnd_id = rs.getLong("tnd_parent_tnd_id");
                tnd_itm_id = rs.getLong("tnd_itm_id");
                tnd_cat_id = rs.getLong("tnd_cat_id");
            }
            else
                //throw new SQLException("Cannot find TreeNode. tnd_id =  " + tnd_id);
                throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);

            stmt.close();
        }
    }

    public String getCatalogTitle(Connection con) throws SQLException  ,cwSysMessage{
        String title;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select cat_title From aeCatalog ");
        SQLBuf.append(" Where cat_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_cat_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            title = rs.getString("cat_title");
        else
            //throw new SQLException("Cannot find Catalog. cat_id = " + tnd_cat_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Catalog ID = " + tnd_cat_id);

        stmt.close();
        return title;
    }

    //find out the children that meet the search criteria
    public long[] searchChild(Connection con, boolean checkStatus, String child_tnd_title
                             ,String child_tnd_ext1 ,String child_tnd_ext2, String child_tnd_ext3
                             ,String child_tnd_ext5)
      throws SQLException {
        Vector v = new Vector();

        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_id From aeTreeNode ");
        SQLBuf.append(" Where tnd_parent_tnd_id = ? ");
        if(checkStatus)
            SQLBuf.append(" And tnd_status <> '").append(TND_STATUS_OFF).append("' ");
        if(child_tnd_title != null && child_tnd_title.length() > 0)
            SQLBuf.append(" And lower(tnd_title) like ? " );
        if(child_tnd_ext1 != null && child_tnd_ext1.length() > 0)
            SQLBuf.append(" And lower(tnd_ext1) = ? " );
        if(child_tnd_ext2 != null && child_tnd_ext2.length() > 0)
            SQLBuf.append(" And lower(tnd_ext2) = ? " );
        if(child_tnd_ext3 != null && child_tnd_ext3.length() > 0)
            SQLBuf.append(" And lower(tnd_ext3) like ? " );
        if(child_tnd_ext5 != null && child_tnd_ext5.length() > 0)
            SQLBuf.append(" And lower(tnd_ext5) like ? " );

        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        int index = 2;
        if(child_tnd_title != null && child_tnd_title.length() > 0)
            stmt.setString(index++, child_tnd_title.toLowerCase() + "%");
        if(child_tnd_ext1 != null && child_tnd_ext1.length() > 0)
            stmt.setString(index++, child_tnd_ext1.toLowerCase());
        if(child_tnd_ext2 != null && child_tnd_ext2.length() > 0)
            stmt.setString(index++, child_tnd_ext2.toLowerCase());
        if(child_tnd_ext3 != null && child_tnd_ext3.length() > 0)
            stmt.setString(index++, child_tnd_ext3.toLowerCase() + "%");
        if(child_tnd_ext5 != null && child_tnd_ext5.length() > 0)
            stmt.setString(index++, child_tnd_ext5.toLowerCase() + "%");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v.addElement(new Long(rs.getLong("tnd_id")));
        }
        stmt.close();
        return aeUtils.vec2longArray(v);
    }

    public static long[] getTndIDFromTitle(Connection con, String tnd_title, boolean allNull)
      throws SQLException {
        //if all input are null, don't perform any search
        if(allNull)
            return new long[0];

        Vector v = new Vector();
        StringBuffer SQLBuf = new StringBuffer(300);
        //only find the TreeNodes that don't have ext1 (e.g Location in PNS)
        SQLBuf.append(" Select tnd_id From aeTreeNode ");
        SQLBuf.append(" Where tnd_ext1 is null ");

        if(tnd_title != null && tnd_title.length() > 0)
            SQLBuf.append(" And lower(tnd_title) like ? ");


        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);

        int index = 1;
        if(tnd_title != null && tnd_title.length() > 0)
            stmt.setString(index++, tnd_title.toLowerCase());

        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {
            v.addElement(new Long(rs.getLong("tnd_id")));
        }
        stmt.close();
        return aeUtils.vec2longArray(v);
    }

    //get the tnd_itm_id of an item node
    public long getItemId(Connection con) throws SQLException ,cwSysMessage {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select tnd_itm_id From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            tnd_itm_id = rs.getLong("tnd_itm_id");
        else
            //throw new SQLException("Cannot find TreeNode. tnd_id = " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);

        stmt.close();
        return tnd_itm_id;
    }

    public String getItemTitle(Connection con) throws SQLException ,cwSysMessage {
        String title;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select itm_title From aeItem ");
        SQLBuf.append(" Where itm_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_itm_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            title = rs.getString("itm_title");
        else
            //throw new SQLException("Cannot find Item. itm_id = " + tnd_itm_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Item ID = " + tnd_itm_id);

        stmt.close();
        return title;
    }

    public boolean isNodeExist(Connection con) throws SQLException {
        boolean result;
        long count=0;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select count(*) From aeTreeNode ");
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            count = rs.getLong(1);

        result = count > 0;

        stmt.close();
        return result;
    }

    //get tnd_status from database
    //but it will not update this.tnd_status
    public String getNodeStatus(Connection con) throws SQLException, cwSysMessage {
        String status;
        final String SQL = " Select tnd_status from aeTreeNode "
                         + " Where tnd_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            status = rs.getString("tnd_status");
        }
        else
            //throw new SQLException("Cannot find TreeNode, tnd_id = " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);
        stmt.close();
        return status;
    }


    public static boolean isNodeOff(Connection con, long tnd_id)
        throws SQLException, cwSysMessage {
        aeTreeNode tnd = new aeTreeNode();
        tnd.tnd_id = tnd_id;
        return tnd.isNodeOff(con);
    }


    //check if the node status is on or off
    //if the node is on, it will go up to parent to find out it should be on or off
    private boolean isNodeOff(Connection con) throws SQLException ,cwSysMessage {
        boolean result;
        String status;
        long parentid;

        StringBuffer SQLBuf = new StringBuffer(2500);
        SQLBuf.append(" Select tnd_status, tnd_parent_tnd_id ");
        SQLBuf.append(" From aeTreeNode " );
        SQLBuf.append(" Where tnd_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, tnd_id);
        ResultSet rs = stmt.executeQuery();

        if(rs.next()) {
            status = rs.getString("tnd_status");
            parentid = rs.getLong("tnd_parent_tnd_id");

            if(parentid == 0)
                result = false;
            else if(status != null && status.equalsIgnoreCase(TND_STATUS_OFF))
                result = true;
            else {
                aeTreeNode parentNode = new aeTreeNode();
                parentNode.tnd_id = parentid;
                result = parentNode.isNodeOff(con);
            }
        }
        else
            //throw new SQLException("Cannot find TreeNode, tnd_id = " + tnd_id);
            throw new cwSysMessage (aeUtils.MSG_REC_NOT_FOUND, "Tree Node ID = " + tnd_id);
        stmt.close();
        return result;
    }


    public String getNavigatorAsXML(Connection con) throws SQLException  ,cwSysMessage{
        StringBuffer xmlBuf = new StringBuffer(2500);
        xmlBuf.append("<nav>");
        xmlBuf.append(dbUtils.NEWL);

        if(isNodeExist(con)) {
            aeTreeNode[] navTreeNode = getNavigatorTreeNode(con);
            for(int i=0; i<navTreeNode.length; i++) {
                xmlBuf.append("<node node_id=\"").append(navTreeNode[i].tnd_id).append("\">");
                xmlBuf.append(dbUtils.NEWL);
                xmlBuf.append("<title>").append(dbUtils.esc4XML(navTreeNode[i].tnd_title)).append("</title>");
                xmlBuf.append("<status>").append(navTreeNode[i].tnd_status).append("</status>");
                xmlBuf.append(dbUtils.NEWL);
                xmlBuf.append("</node>");
                xmlBuf.append(dbUtils.NEWL);
            }
        }
        xmlBuf.append("</nav>");
        String xml = new String(xmlBuf);
        return xml;
    }


    public aeTreeNode[] getNavigatorTreeNode(Connection con) throws SQLException  ,cwSysMessage{
        aeTreeNode[] navTreeNode;
        Vector navVector = new Vector();
        navVector = getNavigatorVector(con, tnd_id, navVector);
        navVector = reverseVector(navVector);
        navTreeNode = Vector2TreeNode(navVector);

        return navTreeNode;
    }


    public static Vector getNavigatorVector(Connection con, long tnd_id, Vector v)
    throws SQLException  ,cwSysMessage{
        aeTreeNode treeNode = new aeTreeNode();
        treeNode.tnd_id = tnd_id;
        treeNode.getNavInfo(con);
        if(treeNode.tnd_parent_tnd_id == 0){
            aeCatalog aeCat = new aeCatalog();
            aeCat.cat_id = treeNode.tnd_cat_id;
            aeCat.get(con, null, false); 
            treeNode.tnd_title = aeCat.cat_title;
            treeNode.tnd_status = aeCat.cat_status;
        }
        else if(treeNode.tnd_itm_id !=0){
            aeItem itm = new aeItem();
            itm.itm_id = treeNode.tnd_itm_id;
            treeNode.tnd_title = treeNode.getItemTitle(con);
            treeNode.tnd_status = itm.getItemStatus(con);
        }

        v.addElement(treeNode);

        if(treeNode.tnd_parent_tnd_id != 0)
            v = getNavigatorVector(con, treeNode.tnd_parent_tnd_id, v);

        return v;
    }


    public static Vector reverseVector(Vector vin) {
        Vector vout = new Vector();
        if(vin != null) {
            int size = vin.size();
            for(int i=size-1;i>=0;i--)
                vout.addElement(vin.elementAt(i));
        }
        else
            vout = null;

        return vout;
    }


    public static void setLinkIdNull(Connection con, long link_tnd_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Update aeTreeNode Set ");
        SQLBuf.append(" tnd_link_tnd_id = null ");
        SQLBuf.append(" Where tnd_link_tnd_id = ? ");
        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, link_tnd_id);
        stmt.executeUpdate();
        stmt.close();
    }


    public static void setItemIdNull(Connection con, long itm_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Update aeTreeNode Set ");
        SQLBuf.append(" tnd_itm_id = null ");
        SQLBuf.append(" Where tnd_itm_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void delItemNodes(Connection con, long itm_id) throws SQLException {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Delete From aeTreeNode ");
        SQLBuf.append(" Where tnd_itm_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, itm_id);
        stmt.executeUpdate();
        stmt.close();
    }

    public static aeTreeNode[] Vector2TreeNode(Vector v) {
        aeTreeNode[] tn;
        if(v != null) {
            int size = v.size();
            tn = new aeTreeNode[size];
            for(int i=0;i<size;i++)
                tn[i] = (aeTreeNode) v.elementAt(i);
        }
        else
            tn = null;

        return tn;
    }

    public static void updCatNodeTitle(Connection con, long tnd_cat_id, String tnd_title) throws SQLException {
        updCatNodeTitle(con, tnd_cat_id, tnd_title, null);
    }

    public static void updCatNodeTitle(Connection con, long tnd_cat_id, String tnd_title, String upd_usr_id) throws SQLException {
        String SQL = " Update aeTreeNode Set tnd_title = ? ";
        if (upd_usr_id!=null){
            SQL += " ,tnd_upd_usr_id = ? ";
            SQL += " ,tnd_upd_timestamp = ? ";
        }
        SQL += " Where tnd_cat_id = ? And tnd_type = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        stmt.setString(index++, tnd_title);
        if (upd_usr_id!=null){
            stmt.setString(index++, upd_usr_id);
            stmt.setTimestamp(index++, cwSQL.getTime(con));
        }
        stmt.setLong(index++, tnd_cat_id);
        stmt.setString(index++, TND_TYPE_CAT);
        stmt.executeUpdate();
        stmt.close();
    }

    public static void updCatNodeDesc(Connection con, long tnd_cat_id, String tnd_desc) throws SQLException {
        updCatNodeDesc(con, tnd_cat_id, tnd_desc, null);
    }

    public static void updCatNodeDesc(Connection con, long tnd_cat_id, String tnd_desc, String upd_usr_id) throws SQLException {
        String SQL = " Update aeTreeNode Set tnd_desc = ? ";
        if (upd_usr_id!=null){
            SQL += " ,tnd_upd_usr_id = ? ";
            SQL += " ,tnd_upd_timestamp = ? ";
        }
        SQL += " Where tnd_cat_id = ? And tnd_type = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        int index = 1;
        stmt.setString(index++, tnd_desc);
        if (upd_usr_id!=null){
            stmt.setString(index++, upd_usr_id);
            stmt.setTimestamp(index++, cwSQL.getTime(con));
        }
        stmt.setLong(index++, tnd_cat_id);
        stmt.setString(index++, TND_TYPE_CAT);
        stmt.executeUpdate();
        stmt.close();
        
    }


    public static void updItemNodeTitle(Connection con, long tnd_itm_id, String tnd_title) throws SQLException {
        final String SQL = " Update aeTreeNode Set "
                         + " tnd_title = ? "
                         + " Where tnd_itm_id = ? ";

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, tnd_title);
        stmt.setLong(2, tnd_itm_id);
        stmt.executeUpdate();
        stmt.close();
    }


    public static String blankContentAsXML(Connection con, long tnd_parent_tnd_id,
                                           String tnd_title, long link_tnd_id,
                                           String tnd_status, long owner_ent_id)

      throws SQLException, qdbException, cwSysMessage {
        try {
            Timestamp cur_time = dbUtils.getTime(con);

            if(tnd_status == null || tnd_status.length() == 0)
                tnd_status = TND_STATUS_OFF;

            StringBuffer xmlBuf = new StringBuffer(2500);

            aeTreeNode parentNode = new aeTreeNode();
            parentNode.tnd_id = tnd_parent_tnd_id;
            parentNode.tnd_parent_tnd_id = parentNode.getParentId(con);
            parentNode.tnd_cat_id = parentNode.getCatalogId(con);

            xmlBuf.append("<node status=\"").append(tnd_status).append("\"");
            xmlBuf.append(" parent_tnd_id=\"").append(parentNode.tnd_id).append("\"");
            xmlBuf.append(" cat_id=\"").append(parentNode.tnd_cat_id).append("\"");
            xmlBuf.append(" ext1=\"").append(dbUtils.esc4XML(aeUtils.escNull(parentNode.tnd_ext1))).append("\"");
            xmlBuf.append(" ext2=\"").append(dbUtils.esc4XML(aeUtils.escNull(parentNode.tnd_ext2))).append("\"");
            xmlBuf.append(" ext3=\"").append(dbUtils.esc4XML(aeUtils.escNull(parentNode.tnd_ext3))).append("\"");
            xmlBuf.append(" ext4=\"").append(dbUtils.esc4XML(aeUtils.escNull(parentNode.tnd_ext4))).append("\"");
            xmlBuf.append(" ext5=\"").append(dbUtils.esc4XML(aeUtils.escNull(parentNode.tnd_ext5))).append("\">");
            xmlBuf.append(dbUtils.NEWL);

            xmlBuf.append(aeItem.getAllItemTypeTitleInOrg(con, owner_ent_id) +
                                    DbCatalogItemType.getCatalogItemTypeAsXML(con, parentNode.tnd_cat_id));

            xmlBuf.append(parentNode.getNavigatorAsXML(con)).append(dbUtils.NEWL);

//            xmlBuf.append("<nav></nav>");
//            xmlBuf.append(dbUtils.NEWL);

            if(tnd_title != null)
                xmlBuf.append("<title>").append(tnd_title).append("</title>");
            else
                xmlBuf.append("<title></title>");
            xmlBuf.append(dbUtils.NEWL);

            if(link_tnd_id != 0) {
                xmlBuf.append("<link link_id=\"").append(link_tnd_id).append("\">").append(dbUtils.NEWL);
                aeTreeNode linkNode = new aeTreeNode();
                linkNode.tnd_id = link_tnd_id;
                linkNode.getNavInfo(con);
                xmlBuf.append(linkNode.getNavigatorAsXML(con));
                xmlBuf.append("</link>");
            }
            else
                xmlBuf.append("<link></link>").append(dbUtils.NEWL);

            xmlBuf.append("<creator></creator>");
            xmlBuf.append(dbUtils.NEWL);

            xmlBuf.append("<last_updated timestamp=\"").append(cur_time).append("\">");
            xmlBuf.append("</last_updated>");
            xmlBuf.append(dbUtils.NEWL);

            xmlBuf.append("</node>");
            String xml = new String(xmlBuf);

            return xml;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }

    public String searchNodeAsXML(Connection con, HttpSession sess,
                                    Timestamp search_timestamp, int page,
                                    String phrase, boolean exact, String code, 
                                    String title_code,
                                    String[] types, Timestamp appn_from,
                                    Timestamp appn_to, Timestamp eff_from,
                                    Timestamp eff_to, String status,
                                    loginProfile prof, boolean all_ind,
                                    boolean checkStatusCat, boolean checkStatusItm, WizbiniLoader wizbini)
                                    throws SQLException, cwSysMessage, qdbException {
        PreparedStatement stmt;
        ResultSet rs;
        long[] itm_lst;
        aeItem item;
        Hashtable itemMatch = new Hashtable();
        Hashtable cat_item_hash = new Hashtable();
        StringBuffer result = new StringBuffer();
        StringBuffer elementsXML = new StringBuffer();
        String items_str = "";
        StringBuffer temp = new StringBuffer();
        StringBuffer SQL = new StringBuffer();
        Enumeration enumeration;
        Long cat_id;
        int index = 1;
        int start = 0;
        int count = 0;
        Vector itm_vec;
        aeTreeNode node = new aeTreeNode();
        Long sess_tnd_id = (Long)sess.getAttribute(TND_SEARCH_ARG1);
        Timestamp sess_timestamp = (Timestamp)sess.getAttribute(TND_SEARCH_ARG2);
        Timestamp cur_timestamp = dbUtils.getTime(con);
        aeTimeField timeField = new aeTimeField(dbUtils.getTime(con));
        //AccessControlWZB acl = new AccessControlWZB();

        if (sess_tnd_id != null && tnd_id == sess_tnd_id.longValue() &&
            sess_timestamp != null && sess_timestamp.equals(search_timestamp) &&
            page != 0) {
            cat_item_hash = (Hashtable)sess.getAttribute(TND_SEARCH_ARG3);
            itemMatch = (Hashtable)sess.getAttribute(TND_SEARCH_ARG4);
//System.out.println("From Session");
        } else {
//System.out.println("From Database");
        	if(all_ind){ //search for all catalogs
                items_str = aeTreeNode.getAllCatalogItems(con, cat_item_hash, prof, checkStatusCat,  wizbini);
            }
            else {   //search under this tree node
                items_str = aeTreeNode.getNodeItems(con, tnd_id, cat_item_hash, prof, checkStatusCat);
            }
            
            Vector tnd_temp_vec = new Vector();
            tnd_temp_vec.add(new Long(0));
            long[]  tem = cwUtils.splitToLong(items_str, ",");
            if(tem != null && tem.length > 0){
                for(long ids : tem){
                    tnd_temp_vec.add(new Long(ids));
                }
            }
            
            String colName = "tmp_id";
            String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName, tnd_temp_vec, cwSQL.COL_TYPE_LONG);
            
            SQL.append("SELECT tnd_id, tnd_itm_id, tnd_create_timestamp, tnd_create_usr_id, tnd_upd_timestamp, tnd_upd_usr_id, itm_type, itm_code, itm_status, itm_fee, itm_fee_ccy, itm_appn_start_datetime, itm_appn_end_datetime, itm_capacity, itm_unit, itm_eff_start_datetime, itm_eff_end_datetime, itm_title, itm_owner_ent_id FROM aeTreeNode, aeItem,"+tableName +" WHERE tnd_id = " + colName+ " AND itm_id = tnd_itm_id ");

            /*if(prof == null || ! prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
            if(checkStatusItm)
                SQL.append(" AND itm_status = ? ");
            else if(status != null && status.length() > 0)
                SQL.append( " AND itm_status = ? ");

            if (phrase != null && phrase.length() > 0)
                SQL.append(" AND lower(tnd_title) LIKE ? ");

            if (code != null && code.length() > 0)
                SQL.append(" AND lower(itm_code) LIKE ? ");

            if (title_code != null && title_code.length() > 0) {
                SQL.append(" AND (lower(tnd_title) LIKE ? OR lower(itm_code) LIKE ?)");
            }

            if (types != null && types.length != 0) {
                SQL.append(" AND ( itm_type = ? ");

                for (int i=1; i<types.length; i++) {
                    SQL.append(" OR itm_type = ? ");
                }

                SQL.append(" )");
            }

            if (appn_from != null)
                SQL.append(" AND itm_appn_start_datetime >= ? ");

            if (appn_to != null)
                SQL.append(" AND itm_appn_end_datetime <= ? ");

            if (eff_from != null)
                SQL.append(" AND itm_eff_start_datetime >= ? ");

            if (eff_to != null)
                SQL.append(" AND itm_eff_end_datetime <= ? ");

            stmt = con.prepareStatement(SQL.toString());
            //System.out.println(SQL.toString());
            /*if(prof == null || ! prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
            if(checkStatusItm)
                stmt.setString(index++, aeItem.ITM_STATUS_ON);
            else if(status != null && status.length() > 0)
                stmt.setString(index++, status);

            if (phrase != null && phrase.length() > 0) {
                if (exact)
                    stmt.setString(index++, phrase.toLowerCase());
                else
                    stmt.setString(index++, "%" + phrase.toLowerCase() + "%");
            }

            if (code != null && code.length() > 0)
                stmt.setString(index++, "%" + code.toLowerCase() + "%");

            if (title_code != null && title_code.length() > 0) {
                stmt.setString(index++, "%" + title_code.toLowerCase() + "%");
                stmt.setString(index++, "%" + title_code.toLowerCase() + "%");
            }

            if (types != null && types.length != 0) {
                for (int i=0; i<types.length; i++) {
                    stmt.setString(index++, types[i]);
                }
            }

            if (appn_from != null)
                stmt.setTimestamp(index++, appn_from);

            if (appn_to != null)
                stmt.setTimestamp(index++, appn_to);

            if (eff_from != null)
                stmt.setTimestamp(index++, eff_from);

            if (eff_to != null)
                stmt.setTimestamp(index++, eff_to);

            rs = stmt.executeQuery();

            while (rs.next()) {
                item = new aeItem();
                item.itm_id = rs.getLong("tnd_itm_id");
                item.itm_type = rs.getString("itm_type");
                item.itm_code = rs.getString("itm_code");
                item.itm_status = rs.getString("itm_status");
                item.itm_fee = rs.getFloat("itm_fee");
                item.itm_fee_ccy = rs.getString("itm_fee_ccy");
                item.itm_appn_start_datetime = rs.getTimestamp("itm_appn_start_datetime");
                item.itm_appn_end_datetime = rs.getTimestamp("itm_appn_end_datetime");
                item.itm_capacity = rs.getLong("itm_capacity");
                item.itm_unit = rs.getLong("itm_unit");
                item.itm_eff_start_datetime = rs.getTimestamp("itm_eff_start_datetime");
                item.itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
                item.itm_title = rs.getString("itm_title");
                item.itm_owner_ent_id = rs.getLong("itm_owner_ent_id");

                itemMatch.put(new Long(rs.getLong("tnd_id")), item.TreeViewContentAsXML(con, rs.getLong("tnd_id"), rs.getString("tnd_create_usr_id"), rs.getTimestamp("tnd_create_timestamp"), rs.getString("tnd_upd_usr_id"), rs.getTimestamp("tnd_upd_timestamp"), null, false));
            }

            stmt.close();
            if(tableName != null && tableName.length() > 0){
                cwSQL.dropTempTable(con, tableName);
            }
            if (page == 0)
                page = 1;

            search_timestamp = cur_timestamp;
            sess.setAttribute(TND_SEARCH_ARG1, new Long(tnd_id));
            sess.setAttribute(TND_SEARCH_ARG2, cur_timestamp);
            sess.setAttribute(TND_SEARCH_ARG3, cat_item_hash);
            sess.setAttribute(TND_SEARCH_ARG4, itemMatch);
        }

        start = TND_SEARCH_PAGE_SIZE * (page-1);

        if (cat_item_hash != null && itemMatch != null) {
            enumeration = cat_item_hash.keys();

            while (enumeration.hasMoreElements()) {
                cat_id = (Long)enumeration.nextElement();
                node.tnd_id = cat_id.longValue();
                node.get(con);
                itm_vec = (Vector)cat_item_hash.get(cat_id);
                temp = new StringBuffer();

                if (itm_vec != null) {
                    for (int i=0; i<itm_vec.size(); i++) {
                        if (itemMatch.containsKey(itm_vec.elementAt(i))) {
                            if (count >= start && count < start+TND_SEARCH_PAGE_SIZE) {
                                temp.append(itemMatch.get(itm_vec.elementAt(i)));
                            }

                            count++;
                        }
                    }
                }

                if (temp != null && temp.toString().length() != 0) {
                    elementsXML.append("<node id=\"").append(node.tnd_id).append("\" title=\"").append(node.tnd_title).append("\">").append(dbUtils.NEWL).append(temp.toString()).append("</node>").append(dbUtils.NEWL);
                }
            }
        }

        result.append(getNavigatorAsXML(con)).append(dbUtils.NEWL);
        result.append(timeField.asXML("cur_time"));
        result.append("<search time=\"").append(search_timestamp).append("\" page_size=\"").append(TND_SEARCH_PAGE_SIZE).append("\" cur_page=\"").append(page).append("\" total_search=\"").append(count).append("\"/>").append(dbUtils.NEWL);
        result.append(elementsXML.toString());

        return result.toString();
    }

    private static String getAllCatalogItems(Connection con, Hashtable cat_item_hash, loginProfile prof, boolean checkStatus, WizbiniLoader wizbini)
      throws SQLException, qdbException ,cwSysMessage {
        //boolean checkStatus;
        long[] usr_ent_ids = {0};

        /*
        AccessControlWZB acl = new AccessControlWZB();

        if(prof == null) {
            checkStatus = true;
        }
        else {
            usr_ent_ids = aeAction.usrGroups(prof.usr_ent_id, prof.usrGroups);
            //if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))
            if(acl.hasUserPrivilege(con, prof.usr_ent_id, prof.current_role, aeCatalog.FTN_CAT_OFF_READ))
                checkStatus = false;
            else
                checkStatus = true;
        }
        */
        Vector v_catalog = aeCatalog.catalogListAsVector(con, usr_ent_ids, checkStatus, null,  wizbini, prof);
        aeCatalog cat;
        StringBuffer result = new StringBuffer(2500);
        long node_id;
        for(int i=0; i<v_catalog.size(); i++) {
            cat = (aeCatalog) v_catalog.elementAt(i);
            node_id = cat.cat_treenode.tnd_id;
            result.append(getNodeItems(con, node_id, cat_item_hash, prof, checkStatus));
        }
        return result.toString();
    }

    public static String getNodeItems(Connection con, long node_id, Hashtable cat_item_hash, loginProfile prof, boolean checkStatus) throws SQLException {
        StringBuffer SQL = new StringBuffer();
        PreparedStatement stmt;
        ResultSet rs;
        String type;
        long id;
        StringBuffer result = new StringBuffer();
        Vector item_vec = new Vector();
        //AccessControlWZB acl = new AccessControlWZB();

        if (cat_item_hash == null) {
            cat_item_hash = new Hashtable();
        }

        SQL.append("SELECT tnd_id, tnd_type FROM aeTreeNode WHERE tnd_parent_tnd_id = ? ");

        /*if(prof == null || ! prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) {*/
        if(checkStatus) {
            SQL.append(" AND (tnd_status = ? OR tnd_status is NULL)");
        }

        stmt = con.prepareStatement(SQL.toString());
        stmt.setLong(1, node_id);

        /*if(prof == null || ! prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) {*/
        if(checkStatus) {
            stmt.setString(2, TND_STATUS_ON);
        }

        rs = stmt.executeQuery();

        while (rs.next()) {
            type = rs.getString("tnd_type");
            id = rs.getLong("tnd_id");

            if (type.equals(TND_TYPE_ITEM)) {
                result.append(", ").append(id);
                item_vec.addElement(new Long(id));
            } else if (type.equals(TND_TYPE_NORMAL)) {
                result.append(getNodeItems(con, id, cat_item_hash, prof, checkStatus));
            }
        }

        cat_item_hash.put(new Long(node_id), item_vec);
        stmt.close();

        return result.toString();
    }

    public static long getMaxTndId(Connection con) throws SQLException {
        long result;
        String SQL = " Select max(tnd_id) from aeTreeNode ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        ResultSet rs = stmt.executeQuery();
        if(rs.next())
            result = rs.getLong(1);
        else
            result = 0;

       stmt.close();
       return result;
    }

    public static Hashtable getDisplayName(Connection con, String tnd_id_lst) throws SQLException {
        Hashtable hash = new Hashtable();
        PreparedStatement stmt = con.prepareStatement("SELECT tnd_id, tnd_title FROM aeTreeNode WHERE tnd_id IN " + tnd_id_lst);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            hash.put(rs.getString("tnd_id"), rs.getString("tnd_title"));
        }

        stmt.close();

        return hash;
    }

    // Get all Item Ids (with run) from a tree node
    public static void getItemsFromNode(Connection con, String[] tnd_id_lst, List itm_id_vec) throws SQLException {
    	if(tnd_id_lst != null && tnd_id_lst.length > 0) {
    		
			Vector tnd_temp_vec = new Vector();
			tnd_temp_vec.add(new Long(0));
			for (String ids : tnd_id_lst) {
				tnd_temp_vec.add(new Long(ids));
			}
			String colName = "tmp_id";
			String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
			cwSQL.insertSimpleTempTable(con, tableName, tnd_temp_vec, cwSQL.COL_TYPE_LONG);

	    	Vector itm_temp_vec = new Vector();
	        
	    	String sql = "select tnd_itm_id from aeTreenodeRelation, aeTreenode,aeItem"
	    			+ " where tnr_type = 'ITEM_PARENT_TND' and tnd_id = tnr_child_tnd_id and tnd_type = 'ITEM'"
	    			+ " and tnd_itm_id = itm_id and itm_type != 'AUDIOVIDEO'"
	        		+ " and tnr_ancestor_tnd_id in (select "+colName+" from "+tableName+")";
	        PreparedStatement stmt = con.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();
	
	        while (rs.next()) {
	            Long itm_id = new Long(rs.getLong("tnd_itm_id"));
	
	            if (itm_id.longValue() != 0) {
	                itm_temp_vec.addElement(itm_id);
	
	                if (!itm_id_vec.contains(itm_id)) {
	                    itm_id_vec.add(itm_id);
	                }
	            }
	        }
	
	        String colName_1 = "tmp_id";
	        String tableName_1 = cwSQL.createSimpleTemptable(con, colName_1, cwSQL.COL_TYPE_LONG, 0);
	        cwSQL.insertSimpleTempTable(con, tableName_1, itm_temp_vec, cwSQL.COL_TYPE_LONG);
	        
	        stmt = con.prepareStatement("select distinct ire_child_itm_id from aeItemRelation where ire_parent_itm_id in (select "+colName_1+" from "+tableName_1+")");
	        rs = stmt.executeQuery();
	
	        while (rs.next()) {
	            Long itm_id = new Long(rs.getLong("ire_child_itm_id"));
	
	            if (!itm_id_vec.contains(itm_id)) {
	                itm_id_vec.add(itm_id);
	            }
	        }
	
	        stmt.close();
	        cwSQL.dropTempTable(con, tableName);
	        cwSQL.dropTempTable(con, tableName_1);
    	}
    }

    public static long getMaxCatOrder(Connection con, long owner_ent_id)
      throws SQLException ,cwSysMessage {

        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select max(tnd_order) From aeTreeNode, aeCatalog ");
        SQLBuf.append(" Where cat_id = tnd_cat_id " );
        SQLBuf.append(" and cat_owner_ent_id = ? " );
        SQLBuf.append(" and tnd_type = ? " );

        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, owner_ent_id);
        stmt.setString(2, TND_TYPE_CAT);
        ResultSet rs = stmt.executeQuery();

        long max_order = 0;
        if(rs.next()) {
            max_order = rs.getLong(1);
        }

        stmt.close();

        return max_order;
    }

    public static  void updOrder(Connection con, long[] tnd_id_lst)
      throws SQLException  {

        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Update aeTreeNode set tnd_order = ?  ");
        SQLBuf.append(" Where tnd_id = ? " );

        String SQL = new String(SQLBuf);

        PreparedStatement stmt = con.prepareStatement(SQL);
        // tnd_id_lst is already in sequence
        for (int i=0;i<tnd_id_lst.length;i++) {
            stmt.setLong(1, i+1);
            stmt.setLong(2, tnd_id_lst[i]);
            stmt.executeUpdate();
        }

        stmt.close();

    }

    /*
        return a vector of aeTreeNode, only tnd_id, tnd_title has value.
    */
    public static Vector getItemParentNode(Connection con, long itm_id) throws SQLException{
        Vector vtParentNode = new Vector();
        StringBuffer SQL = new StringBuffer();
        SQL.append("select node.tnd_id tnd_id , node.tnd_title tnd_title ");
        SQL.append("from aetreenode itemNode, aetreenode node ");
        SQL.append("where itemNode.tnd_type = ? ");
        SQL.append("and itemNode.tnd_itm_id = ? and node.tnd_id = itemNode.tnd_parent_tnd_id");

        PreparedStatement stmt = con.prepareStatement(SQL.toString());
        stmt.setString(1, TND_TYPE_ITEM);
        stmt.setLong(2, itm_id);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            aeTreeNode node = new aeTreeNode();
            node.tnd_id = rs.getLong("tnd_id");
            node.tnd_title = rs.getString("tnd_title");
            vtParentNode.addElement(node);
        }
        stmt.close();
        return vtParentNode;
    }

    /**
    * Check the category name existed in the same level
    * @return true if catalog name not existed
    */
    public boolean validCategoryName(Connection con, long id)
        throws SQLException{

            String SQL = " SELECT COUNT(tnd_id) "
                       + " FROM aeTreeNode "
                       + " WHERE tnd_parent_tnd_id = ? "
                       + " AND tnd_title = ? ";
            if( id != 0 )
                SQL += " AND tnd_id <> ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, tnd_parent_tnd_id);
            stmt.setString(2, tnd_title);
            if( id != 0 )
                stmt.setLong(3, id);
            boolean flag = true;
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                if( rs.getLong(1) > 0 )
                    flag = false;
            }
            stmt.close();
            return flag;

        }

    /**
    * Get the tree node id by tree node code and site entity id
    */
	public static long getTreeNodeIdByCode(Connection con, String tnd_code, long site_id)
	    throws SQLException{

	        String SQL = " SELECT tnd_id "
	                   + " FROM aeTreeNode "
	                   + " WHERE tnd_code = ? "
	                   + " AND tnd_owner_ent_id = ? ";

	        PreparedStatement stmt = con.prepareStatement(SQL);
	        stmt.setString(1, tnd_code);
	        stmt.setLong(2, site_id);
	        ResultSet rs = stmt.executeQuery();
	        long tnd_id = 0;
	        if(rs.next())
	            tnd_id = rs.getLong("tnd_id");
	        stmt.close();
	        return tnd_id;

	    }

    
    /**
    * Update title and description of the tree node. 
    * If it is a catalog tree node, catalog title will also be updated
    * @param flag false to not updated catalog title
    */
	public void updTitleDesc(Connection con, boolean flag)
	    throws SQLException{

	        String SQL = " UPDATE aeTreeNode "
	                   + " SET tnd_title = ?, "
	                   + " tnd_desc = ?, "
	                   + " tnd_upd_usr_id = ?, "
	                   + " tnd_upd_timestamp = ?, "
	                   + " tnd_syn_timestamp = ? "
	                   + " WHERE tnd_id = ? ";
	        PreparedStatement stmt = con.prepareStatement(SQL);
	        stmt.setString(1, tnd_title);
	        stmt.setString(2, tnd_desc);
	        stmt.setString(3, tnd_upd_usr_id);
	        stmt.setTimestamp(4, tnd_upd_timestamp);
	        stmt.setTimestamp(5, tnd_syn_timestamp);
	        stmt.setLong(6, tnd_id);
	        if( stmt.executeUpdate() != 1 )
	            throw new SQLException("Failed to update tree node title and description, tnd_id = " + tnd_id );
	        stmt.close();

	        if( flag ) {
	            if( tnd_type.equalsIgnoreCase(TND_TYPE_CAT) ) {
	                aeCatalog aeCat = new aeCatalog();
	                aeCat.cat_id = tnd_cat_id;
	                aeCat.cat_upd_usr_id = tnd_upd_usr_id;
	                aeCat.cat_upd_timestamp = tnd_upd_timestamp;
	                aeCat.cat_title = tnd_title;
	                aeCat.updTitle(con);
	            }
	        }
	        return;
	    }

    /**
    * Update the synchronization date of the tree node
    */
	public void updSynDate(Connection con)
	    throws SQLException {

	        if( tnd_syn_timestamp == null )
	            tnd_syn_timestamp = cwSQL.getTime(con);

	        String SQL = " UPDATE aeTreeNode Set tnd_syn_timestamp = ? "
	                   + " WHERE tnd_id = ? ";

	        PreparedStatement stmt = con.prepareStatement(SQL);
	        stmt.setTimestamp(1, tnd_syn_timestamp);
	        stmt.setLong(2, tnd_id);
	        if( stmt.executeUpdate() != 1 )
	            throw new SQLException("Failed to update tree node syn date, tnd_id = " + tnd_id);
	        stmt.close();
	        return;
	    }
    
    /**
    * Get tree node id by item id and parent tree node id
    */
	public long getTreeNode(Connection con)
	    throws SQLException {

	        String SQL = " SELECT tnd_id, tnd_code "
	                   + " FROM aeTreeNode "
	                   + " WHERE tnd_itm_id = ? "
	                   + " AND tnd_parent_tnd_id = ? ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, tnd_itm_id);
            stmt.setLong(2, tnd_parent_tnd_id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                tnd_id = rs.getLong("tnd_id");
                tnd_code = rs.getString("tnd_code");
            }else
                tnd_id = 0;
            stmt.close();
            return tnd_id;
	    }

    /**
    * Get not in synchronized tree node which belong to the specified tree node 
    */
	public static Vector getNotInSynChild(Connection con, long parent_tnd_id, Timestamp syn_date)
	    throws SQLException{

	        String SQL = " SELECT tnd_id, tnd_itm_id, tnd_type, tnd_code "
	                   + " FROM aeTreeNode "
	                   + " WHERE tnd_parent_tnd_id = ? "
	                   + " AND tnd_code is not null "
	                   + " AND ( tnd_syn_timestamp < ? OR tnd_syn_timestamp is null )";

	        PreparedStatement stmt = con.prepareStatement(SQL);
	        stmt.setLong(1, parent_tnd_id);
	        stmt.setTimestamp(2, syn_date);
	        ResultSet rs = stmt.executeQuery();
	        Vector tndVec = new Vector();
	        aeTreeNode aeTnd = null;
	        while(rs.next()){
	            aeTnd = new aeTreeNode();
	            aeTnd.tnd_id = rs.getLong("tnd_id");
	            aeTnd.tnd_itm_id = rs.getLong("tnd_itm_id");
	            aeTnd.tnd_type = rs.getString("tnd_type");
	            tndVec.addElement(aeTnd);
	        }
	        stmt.close();
	        return tndVec;
	    }


	public static Vector getNotInSynTreeNode(Connection con, long site_id, Timestamp syn_date)
	    throws SQLException{

	        String SQL = " SELECT tnd_id, tnd_type, tnd_parent_tnd_id, tnd_itm_id, tnd_cat_id, tnd_code "
	                   + " FROM aeTreeNode "
	                   + " WHERE tnd_code is not null "
	                   + " AND ( tnd_syn_timestamp < ? OR tnd_syn_timestamp is null )"
	                   + " AND tnd_owner_ent_id = ? "
	                   + " ORDER BY tnd_id DESC ";

	        PreparedStatement stmt = con.prepareStatement(SQL);
	        stmt.setTimestamp(1, syn_date);
	        stmt.setLong(2, site_id);
	        ResultSet rs = stmt.executeQuery();
	        Vector tndVec = new Vector();
	        aeTreeNode aeTnd = null;
	        while(rs.next()){
	            aeTnd = new aeTreeNode();
	            aeTnd.tnd_id = rs.getLong("tnd_id");
	            aeTnd.tnd_parent_tnd_id = rs.getLong("tnd_parent_tnd_id");
	            aeTnd.tnd_type = rs.getString("tnd_type");
	            aeTnd.tnd_itm_id = rs.getLong("tnd_itm_id");
	            aeTnd.tnd_cat_id = rs.getLong("tnd_cat_id");
	            aeTnd.tnd_code = rs.getString("tnd_code");
	            tndVec.addElement(aeTnd);
	        }
	        stmt.close();
	        return tndVec;
	    }

	public Vector getCatalogChildTreeNode(Connection con, long cat_id)
	    throws SQLException{

	        String SQL = " SELECT tnd_id, tnd_code, tnd_itm_id "
	                   + " FROM aeTreeNode "
	                   + " WHERE tnd_cat_id = ? "
	                   + " AND tnd_type <> ? ";
	        PreparedStatement stmt = con.prepareStatement(SQL);
	        stmt.setLong(1, cat_id);
	        stmt.setString(2, TND_TYPE_CAT);
	        ResultSet rs = stmt.executeQuery();
	        aeTreeNode aeTnd = null;
	        Vector tndVec = new Vector();
	        while(rs.next()){
	            aeTnd = new aeTreeNode();
	            aeTnd.tnd_id = rs.getLong("tnd_id");
	            aeTnd.tnd_code = rs.getString("tnd_code");
	            aeTnd.tnd_itm_id = rs.getLong("tnd_itm_id");
	            tndVec.addElement(aeTnd);
	        }
	        stmt.close();
	        return tndVec;
	    }

	public Vector getChildTreeNode(Connection con, long parent_tnd_id)
	    throws SQLException{

	        Vector vec = new Vector();
	        Vector parentIdVec = new Vector();
	        parentIdVec.addElement(new Long(parent_tnd_id));
	        putChildTndToVec(con, vec, parentIdVec);
	        return vec;

	    }

	public void putChildTndToVec(Connection con, Vector vec, Vector parentIdVec)
	    throws SQLException{

	        if( parentIdVec == null || parentIdVec.isEmpty() )
	            return;

	        String SQL = " SELECT tnd_id, tnd_code, tnd_type, tnd_itm_id "
	                   + " FROM aeTreeNode "
	                   + " WHERE tnd_parent_tnd_id IN " + cwUtils.vector2list(parentIdVec);

	        PreparedStatement stmt = con.prepareStatement(SQL);
	        ResultSet rs = stmt.executeQuery();
	        parentIdVec = new Vector();
	        aeTreeNode aeTnd = null;
	        while(rs.next()){
	            aeTnd = new aeTreeNode();
	            aeTnd.tnd_id = rs.getLong("tnd_id");
	            aeTnd.tnd_code = rs.getString("tnd_code");
	            aeTnd.tnd_itm_id = rs.getLong("tnd_itm_id");
	            if( (rs.getString("tnd_type")).equalsIgnoreCase(TND_TYPE_NORMAL) )
	                parentIdVec.addElement(new Long(aeTnd.tnd_id));
	            vec.addElement(aeTnd);
	        }
            stmt.close();
            putChildTndToVec(con, vec, parentIdVec);
            return;
	    }

	public static Vector getSynedTreeNodeId(Connection con, long site_id, Timestamp syn_date)
      throws SQLException {
          String SQL = " SELECT tnd_id FROM aeTreeNode "
                     + " WHERE tnd_owner_ent_id = ? "
                     + " AND tnd_syn_timestamp > ? "
                     + " AND tnd_code is not null";

          PreparedStatement stmt = con.prepareStatement(SQL);
          stmt.setLong(1, site_id);
          stmt.setTimestamp(2, syn_date);
          ResultSet rs = stmt.executeQuery();
          Vector tndIdVec = new Vector();
          while(rs.next())
              tndIdVec.addElement(new Long(rs.getLong("tnd_id")));
          stmt.close();
          return tndIdVec;

      }
  
    public void delChildTreeNode(Connection con, long parent_id)
        throws SQLException{
            
            String SQL = " DELETE FROM aeTreeNode WHERE tnd_parent_tnd_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, parent_id);
            stmt.executeUpdate();
            stmt.close();
            return;            
        }    
    
    
    public void updTndCode(Connection con)
        throws SQLException {
            
            if( tnd_upd_timestamp == null )
                tnd_upd_timestamp = cwSQL.getTime(con);
                
            String SQL = " UPDATE aeTreeNode "
                       + " SET tnd_code = ?, "
	                   + " tnd_upd_usr_id = ?, "
	                   + " tnd_upd_timestamp = ? "
                       + " WHERE tnd_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, tnd_code);
            stmt.setString(2, tnd_upd_usr_id);
            stmt.setTimestamp(3, tnd_upd_timestamp);
            stmt.setLong(4, tnd_id);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to update tree node code, tnd_id = " + tnd_id);
            stmt.close();
            return;
        }
    /*
        itmLst = possible item (from itemtype)
    */
    public static Vector getUpdatedItemTreeNode(Connection con, Timestamp startDate, Timestamp endDate, String[] itemTypeLst) throws SQLException{
        Vector vtUpdatedItem = new Vector();
        String SQL = " SELECT tnd_itm_id FROM aeTreeNode WHERE tnd_type  = '" + TND_TYPE_ITEM + "' ";
        if (itemTypeLst!=null && itemTypeLst.length>0){
	        SQL += " AND tnd_itm_id in (SELECT itm_id from aeItem WHERE itm_type IN " + cwUtils.array2list(itemTypeLst) + " ) ";
	    }
        if( startDate != null )
            SQL += " AND tnd_upd_timestamp > ? ";
        if( endDate != null )
            SQL += " AND tnd_upd_timestamp < ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index=1;
        if( startDate != null )
            stmt.setTimestamp(index++, startDate);
        if( endDate != null )
            stmt.setTimestamp(index++, endDate);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            vtUpdatedItem.addElement(new Long(rs.getLong("tnd_itm_id")));
        }
        stmt.close();
        return vtUpdatedItem;
    }
    
    // get the update treenode
    // and the tree node contain update item treenode with specific item
    public static Hashtable getUpdatedTreeNode(Connection con, Timestamp startDate, Timestamp endDate, Vector itmLst) throws SQLException{
        Hashtable htTreeNode = new Hashtable(); 
        
        String SQL = "SELECT tnd_id , tnd_itm_id FROM aeTreeNode WHERE tnd_type IN ('" + TND_TYPE_NORMAL + "' , '" + TND_TYPE_CAT + "') ";
        if( startDate != null )
            SQL += " AND tnd_upd_timestamp > ? ";
        if( endDate != null )
            SQL += " AND tnd_upd_timestamp < ? ";
        SQL += " UNION ";        
        SQL += " SELECT tnd_parent_tnd_id as tnd_id, tnd_itm_id FROM aeTreeNode WHERE tnd_type  = '" + TND_TYPE_ITEM + "' ";
        SQL += " AND tnd_parent_tnd_id IS NOT NULL ";
        if (itmLst!=null){
	        if (itmLst.size()==0){
    	        itmLst.addElement(new Long(0));
	        }
	        SQL += " AND tnd_itm_id in " + cwUtils.vector2list(itmLst);
	    }
        SQL += " order by tnd_id, tnd_itm_id ";
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index=1;
        if( startDate != null )
            stmt.setTimestamp(index++, startDate);
        if( endDate != null )
            stmt.setTimestamp(index++, endDate);

        ResultSet rs = stmt.executeQuery();
        Long l_tnd_id;
        Long l_itm_id;
        Vector vtCatItm;
        while (rs.next()){
            l_tnd_id = new Long(rs.getLong("tnd_id"));
            l_itm_id = new Long(rs.getLong("tnd_itm_id"));
            vtCatItm = (Vector)htTreeNode.get(l_tnd_id);
            if (vtCatItm==null){
                vtCatItm = new Vector();
            }
            if (!rs.wasNull()){
                vtCatItm.addElement(l_itm_id);
            }
            htTreeNode.put(l_tnd_id, vtCatItm);
        }
        stmt.close();
        return htTreeNode;
        
            	    

    }

/*
    public Vector getUpdatedTreeNode(Connection con, Timestamp startDate, Timestamp endDate, Vector itmLst) throws SQLException{

        Vector tndLst = new Vector();

        String SQL = "SELECT tnd_id FROM aeTreeNode WHERE tnd_type IN ('" + TND_TYPE_NORMAL + "' , '" + TND_TYPE_CAT + "') ";
        if( startDate != null )
            SQL += " AND tnd_upd_timestamp > ? ";
        if( endDate != null )
            SQL += " AND tnd_upd_timestamp < ? ";
        
        SQL += " UNION ";        

        SQL += " SELECT tnd_parent_tnd_id AS tnd_id FROM aeTreeNode WHERE tnd_type  = '" + TND_TYPE_ITEM + "' ";
        SQL += " AND tnd_parent_tnd_id IS NOT NULL ";
        if (itmLst!=null){
	        if (itmLst.size()==0){
    	        itmLst.addElement(new Long(0));
	        }
	        SQL += " AND tnd_itm_id in " + cwUtils.vector2list(itmLst);
	    }
        if( startDate != null )
            SQL += " AND tnd_upd_timestamp > ? ";
        if( endDate != null )
            SQL += " AND tnd_upd_timestamp < ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        int index=1;
        if( startDate != null )
            stmt.setTimestamp(index++, startDate);
        if( endDate != null )
            stmt.setTimestamp(index++, endDate);
        if( startDate != null )
            stmt.setTimestamp(index++, startDate);
        if( endDate != null )
            stmt.setTimestamp(index++, endDate);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            tndLst.addElement(new Long(rs.getLong("tnd_id")));
        }
        return tndLst;
    }
*/

    // get title, parent_tnd_id of all treenodes that are potential parent 
    // return hashtable, tnd_id as key, aeTreeNode object as value
    public Hashtable getAllParentTreeNode(Connection con) throws SQLException{
        Hashtable htTreeNode = new Hashtable();
        String SQL = " SELECT tnd_id , tnd_title , tnd_parent_tnd_id  FROM aeTreeNode "
                    + " WHERE tnd_type IN (?, ?) ORDER BY tnd_id ";
                    
        PreparedStatement stmt = con.prepareStatement(SQL);                  
        stmt.setString(1, TND_TYPE_CAT);        
        stmt.setString(2, TND_TYPE_NORMAL);        
                            
        ResultSet rs = stmt.executeQuery();
        Long tndId;
        aeTreeNode tnd; 
        while (rs.next()){
            tnd = new aeTreeNode();
            tnd.tnd_id              = rs.getLong("tnd_id");
            tnd.tnd_title           = rs.getString("tnd_title");
            tnd.tnd_parent_tnd_id   = rs.getLong("tnd_parent_tnd_id");
            htTreeNode.put(new Long(tnd.tnd_id), tnd);                        
        }
        stmt.close();
        return htTreeNode;
    }
    
    // return hashtable, itm_id as key, vector of parent_tnd_id as value 
    public Hashtable getAllItemTreeNode(Connection con) throws SQLException{
        Hashtable htItemTreeNode = new Hashtable();
        String SQL = " SELECT tnd_itm_id , tnd_parent_tnd_id  FROM aeTreeNode "
                    + " WHERE tnd_type = ?  ORDER BY tnd_itm_id ";
        PreparedStatement stmt = con.prepareStatement(SQL);                  
        stmt.setString(1, TND_TYPE_ITEM);        
        
        ResultSet rs = stmt.executeQuery();
        Long itmId;
        Long parentTndId;
        Vector vtParentTndId;
        while (rs.next()){      
            itmId = new Long(rs.getLong("tnd_itm_id"));
            vtParentTndId = (Vector)htItemTreeNode.get(itmId);
            if (vtParentTndId==null){
                vtParentTndId = new Vector();
            }
            vtParentTndId.addElement(new Long(rs.getLong("tnd_parent_tnd_id")));
            htItemTreeNode.put(itmId, vtParentTndId);
        }
        stmt.close();
        return htItemTreeNode;
    }
    /*
    auto gen code after inserting 
    Pre-define variable:     tnd_id
    */

    public String autoGenTndCode(Connection con) throws SQLException {
        Random random = new Random();
        tnd_code = "" + tnd_id + random.nextInt(10);
        CommonLog.debug("test tnd_code: " + tnd_code);
        if (isTndCodeExist(con, tnd_id)){
            autoGenTndCode(con);
        }else{
            updTndCode(con);
        }
        return this.tnd_code;
    }
    
    private static final String sql_is_tnd_code_exist =
        " Select tnd_code From aeTreeNode Where tnd_code = ? ";
    /**
    Check if the tnd_code exists in the organization<BR>
    Pre-define variable:
    <ul>
    <li>tnd_code</li>
    </ul>
    */
    private boolean isTndCodeExist(Connection con, long tnd_id) throws SQLException {

        boolean result;
        String sql = sql_is_tnd_code_exist;
        if (tnd_id != 0) {
            sql += " and tnd_id <> ? ";
        }

        PreparedStatement stmt = con.prepareStatement(sql);

        int index = 1;
        stmt.setString(index++, this.tnd_code);
        if (tnd_id != 0)
            stmt.setLong(index++, this.tnd_id);

        ResultSet rs = stmt.executeQuery();
        result = rs.next();
        stmt.close();

        return result;
    }

	public static long getTcrIdByTndId(Connection con, long in_tnd_id, long root_ent_id) throws SQLException {
		String sql = "select cat_tcr_id from aeCatalog, aeTreeNode where tnd_id = ? and tnd_cat_id = cat_id and cat_owner_ent_id = ?";
		PreparedStatement stmt = null;
		long tcr_id = 0;
		try {
	        stmt = con.prepareStatement(sql);
	        int index = 1;
	        stmt.setLong(index++, in_tnd_id);
	        stmt.setLong(index++, root_ent_id);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	        	tcr_id = rs.getLong("cat_tcr_id");
	        }
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return tcr_id;
	}
	
	//update all item under spec catalog to share folder.
	public static void updateItemToShareByCatId(Connection con, long root_ent_id, long cat_id) throws SQLException {
		String sql = "delete from aeTreeNode where tnd_type = ? and tnd_cat_id = ? and tnd_owner_ent_id = ?";
		String updateCatItemCountsql = "update aeTreeNode set tnd_itm_cnt = 0 where tnd_type in (?, ?) and tnd_cat_id = ? and tnd_owner_ent_id = ?";
		PreparedStatement stmt = null;
		long tcr_id = 0;
		try {
	        stmt = con.prepareStatement(sql);
	        int index = 1;
	        stmt.setString(index++, TND_TYPE_ITEM);
	        stmt.setLong(index++, cat_id);
	        stmt.setLong(index++, root_ent_id);
	        int count = stmt.executeUpdate();
	        if (count > 0) {
	        	stmt = con.prepareStatement(updateCatItemCountsql);
		        index = 1;
		        stmt.setString(index++, TND_TYPE_CAT);
		        stmt.setString(index++, TND_TYPE_NORMAL);
		        stmt.setLong(index++, cat_id);
		        stmt.setLong(index++, root_ent_id);
		        stmt.executeUpdate();
	        }
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
	
	public static Vector getTreeNodeRelation(Connection con, String tndType) throws SQLException {
		Vector normalTreeNodeVec = new Vector();
    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	
    	try {
    		stmt = con.prepareStatement(SqlStatements.getTreeNodeRelation());
    		int index = 1;
    		stmt.setString(index++, tndType);
    		rs = stmt.executeQuery();
    		while(rs.next()) {
    			Vector normalTreeNode = new Vector();
    			normalTreeNode.addElement(new Long(rs.getLong("tnd_id")));
    			normalTreeNode.addElement(new Long(rs.getLong("tnd_parent_tnd_id")));
    			normalTreeNodeVec.addElement(normalTreeNode);
    		}
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}
    	return normalTreeNodeVec;
    }
	
	public static Vector getMobileItemParentNode(Connection con, long itm_id) throws SQLException{
        Vector vtParentNode = new Vector();
        StringBuffer SQL = new StringBuffer();
        SQL.append("select node.tnd_id tnd_id , node.tnd_title tnd_title ");
        SQL.append("from aetreenode itemNode, aetreenode node ");
        SQL.append("where itemNode.tnd_type = ? ");
        SQL.append("and itemNode.tnd_itm_id = ? and node.tnd_id = itemNode.tnd_parent_tnd_id ");
        SQL.append("and node.tnd_id in (SELECT tnd.tnd_id FROM aeTreeNode tnd ");
        SQL.append("WHERE tnd.tnd_parent_tnd_id IN ");
        SQL.append("(SELECT tnd.tnd_id FROM aeTreeNode tnd ");
        SQL.append("INNER JOIN aeCatalog cat ON (cat.cat_id = tnd.tnd_cat_id) ");
        SQL.append("WHERE tnd.tnd_parent_tnd_id IS NULL AND cat.cat_status = 'ON' AND cat.cat_mobile_ind = 1) ");
        SQL.append("AND tnd.tnd_type = 'NORMAL')");

        PreparedStatement stmt = con.prepareStatement(SQL.toString());
        stmt.setString(1, TND_TYPE_ITEM);
        stmt.setLong(2, itm_id);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()){
            aeTreeNode node = new aeTreeNode();
            node.tnd_id = rs.getLong("tnd_id");
            node.tnd_title = rs.getString("tnd_title");
            vtParentNode.addElement(node);
        }
        stmt.close();
        return vtParentNode;
    }
	
	/**
	 * 删除子培训目录
	 * @param cat_id 目录id
	 */
	public static void delChildCatalogByCatId(Connection con, long cat_id) throws SQLException {
		String sql = " delete from aeTreeNode where tnd_cat_id = ? and tnd_parent_tnd_id is not null ";
		PreparedStatement stmt = null;
		try {
	        stmt = con.prepareStatement(sql);
	        stmt.setLong(1, cat_id);
	        stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}
}