/***********************************************
    Outstanding items
    1) catalogListAsXML, get the owner name, DONE
    2) catalogListAsXML, filter status/delete flag for diff roles
    3) catalogListAsXML, get creator name and last upd usr name, DONE
************************************************/
package com.cw.wizbank.ae;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.qdb.*;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.DbCatalogItemType;
import com.cw.wizbank.accesscontrol.AcCatalog;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;

public class aeCatalog {
    
    //Session Value
    public static final String CAT_ID = "CAT_ID";
    public static final String CAT_CREATE_TIMESTAMP = "CAT_CREATE_TIMESTAMP";
    
    public static final String CAT_STATUS_ON = "ON";
    public static final String CAT_STATUS_OFF = "OFF";
    
    public static final String NO_ACCESS_RIGHT = "AECA01"; //"You don't have prilvilege to access this catalog"
    public static final String CAT_OFFLINE_MSG = "AECA02"; //"Catalog is in offline status"
    public static final String CAT_NAME_EXISTED = "AECA03"; //Catalog name existed
    public static final String CAT_NOT_IN_TC ="AECA05";
    public static final String DEL_SUCCESS = "AECA07";
    public static final String NO_OPT_RIGHT = "ACL002";
    
    public long cat_id;
    public long cat_tcr_id;
	public String cat_tcr_title;
    public String cat_title;
    public boolean cat_public_ind;
    public boolean cat_show_all;
    public String cat_status;
    public long cat_owner_ent_id;
    public Timestamp cat_create_timestamp;
    public String cat_create_usr_id;
    public Timestamp cat_upd_timestamp;
    public String cat_upd_usr_id;
    public aeTreeNode cat_treenode;    
    public String cat_desc;
    public String cat_code;
    public boolean cat_mobile_ind;	// 是否是移动课程目录
    
    public void updMobileCatStatus(Connection con, long root_ent_id, Timestamp upd_timestamp) throws SQLException, cwSysMessage {
    	
    	if (cat_mobile_ind && existsMobildCat(con, root_ent_id)) {
    		throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
    	}
    	
    	if(!isLastUpd(con, upd_timestamp)) {
    		throw new cwSysMessage(dbMessage.MSG_CATALOG_MOBILE_EXSITS);
    	}
    	
    	if (updMobileCatStatus(con) <= 0) {
    		throw new cwSysMessage(dbMessage.MSG_CATALOG_MOBILE_UPD_FAILED);
    	}
    	
    }
    
    private int updMobileCatStatus(Connection con) throws SQLException {
    	String sql = "UPDATE aeCatalog SET cat_mobile_ind = ?, cat_upd_timestamp = ? WHERE cat_id = ?";
    	
    	PreparedStatement ps = null;
    	int rs = -1;
    	
    	try {
			ps = con.prepareStatement(sql);
			ps.setInt(1, cat_mobile_ind ? 1 : 0);
			ps.setTimestamp(2, cwSQL.getTime(con));
			ps.setLong(3, this.cat_id);
			
			rs = ps.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		} finally {
			cwSQL.cleanUp(null, ps);
		}
    	
    	return rs;
	}

	public void del(Connection con, long owner_ent_id, Timestamp upd_timestamp) 
      throws SQLException, cwSysMessage {
		/*
    	if(!isLastUpd(con, upd_timestamp)) 
    		throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
         */ 
        //check if there are nodes attached
        aeTreeNode myTreeNode = getMyTreeNode(con);
        if(myTreeNode.hasChild(con)) 
            throw new cwSysMessage(aeTreeNode.NOT_DEL_HAS_CHILD);
            
        //del Access
        aeCatalogAccess.delCat(con, cat_id);
        //del tree node relation
        aeTreeNodeRelation.delTnrByTnd(con, myTreeNode.tnd_id);
        //del dummy cat tree node
        myTreeNode.del(con);
        
        //del aeCatalogItemType
        DbCatalogItemType.delByCatalog(con, this.cat_id);
        
        del(con);
    }
	
	public void delByDelTcr(Connection con) throws SQLException, cwSysMessage {
        //check if there are nodes attached
        aeTreeNode myTreeNode = getMyTreeNode(con);
        if(myTreeNode.hasChild(con)) 
            throw new cwSysMessage(aeTreeNode.NOT_DEL_HAS_CHILD);
            
        //del Access
        aeCatalogAccess.delCat(con, cat_id);
        //del tree node relation
        aeTreeNodeRelation.delTnrByTnd(con, myTreeNode.tnd_id);
        //del dummy cat tree node
        myTreeNode.del(con);
        
        //del aeCatalogItemType
        DbCatalogItemType.delByCatalog(con, this.cat_id);
        
        del(con);
    }

    public void del(Connection con) throws SQLException, cwSysMessage{
        String SQL = " Delete FROM aeCatalog Where cat_id = ? ";
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        int row = stmt.executeUpdate();
        stmt.close();
        
        if(row == 0) 
            //throw new SQLException("Cannot find Catalog. cat_id = " + cat_id );
            throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND, "Catalog ID = " + cat_id);
        else if(row > 1)
            throw new SQLException("More than one catalog have cat_id = " + cat_id );
        
    }

    public void getCreator(Connection con) throws SQLException, qdbException {
        StringBuffer SQLBuf= new StringBuffer(300);
        SQLBuf.append(" Select cat_create_usr_id, cat_create_timestamp From aeCatalog ");
        SQLBuf.append(" Where cat_id = ? ");
        String SQL = new String(SQLBuf);
        
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) {
            cat_create_usr_id = rs.getString("cat_create_usr_id");
            cat_create_timestamp = rs.getTimestamp("cat_create_timestamp");
        }
        else
            throw new SQLException("Cannot get creator for catalog " + cat_id);
            
        stmt.close();
    }

    
    
    /*
    public boolean hasUpdPrivilege(Connection con, long owner_ent_id) 
        throws SQLException ,cwSysMessage {
        //return aeCatalogAccess.hasWriteRight(con, cat_id, usr_ent_ids);
            //throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
         boolean result;
         StringBuffer SQLBuf = new StringBuffer(300);
         SQLBuf.append(" Select cat_owner_ent_id From aeCatalog ");
         SQLBuf.append(" Where cat_id = ? ");
         String SQL = new String(SQLBuf);
         
         PreparedStatement stmt = con.prepareStatement(SQL);
         stmt.setLong(1, cat_id);
         ResultSet rs = stmt.executeQuery();
         if(rs.next()) 
            cat_owner_ent_id = rs.getLong("cat_owner_ent_id");
         else
            //throw new SQLException("Cannot find Catalog, cat_id = " + cat_id);
            throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND, "Catalog ID = " + cat_id);
            
            
         if(cat_owner_ent_id == owner_ent_id)
            result = true;
         else
            result = false;
         
         stmt.close();
         return result;
    }
    */

    public boolean isLastUpd(Connection con, Timestamp upd_timestamp)
        throws SQLException, cwSysMessage {
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(300);
         SQLBuf.append(" Select cat_upd_timestamp From aeCatalog ");
         SQLBuf.append(" Where cat_id = ? ");
         String SQL = new String(SQLBuf);
         
         PreparedStatement stmt = con.prepareStatement(SQL);
         stmt.setLong(1, cat_id);
         ResultSet rs = stmt.executeQuery();
         if(rs.next()) 
            cat_upd_timestamp = rs.getTimestamp("cat_upd_timestamp");
         else
            //throw new SQLException("Cannot find Catalog, cat_id = " + cat_id);
            throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND, "Catalog ID = " + cat_id);
         
         if(upd_timestamp == null || cat_upd_timestamp == null)
            result = false;
         else {
            //upd_timestamp.setNanos(cat_upd_timestamp.getNanos());
            if(upd_timestamp.equals(cat_upd_timestamp))
                result = true;
            else
                result = false;
         }
         stmt.close();
         return result;
    }
    
    public void updAccess(Connection con, long owner_ent_id, String upd_usr_id, Timestamp upd_timestamp, 
                    long[] ent_id) 
      throws SQLException, qdbException, cwSysMessage {
        try {
            if(!isLastUpd(con, upd_timestamp)) 
                throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
            
            getCreator(con);
            aeCatalogAccess.updAccess(con, ent_id, dbRegUser.getEntId(con, cat_create_usr_id), 
                                        cat_id, upd_usr_id, cat_upd_timestamp);
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }

    

    public void upd(Connection con, long owner_ent_id, String upd_usr_id, Timestamp upd_timestamp, 
                    long[] ent_id) 
      throws SQLException, qdbException, cwSysMessage {
        try {
            if(!isLastUpd(con, upd_timestamp)) 
                throw new cwSysMessage(dbMessage.INVALID_TIMESTAMP_MSG);
            
            
            upd(con, upd_usr_id);
            
            //upd aeTreeNode
            aeTreeNode.updCatNodeTitle(con, cat_id, cat_title, upd_usr_id);
            aeTreeNode.updCatNodeDesc(con, cat_id, cat_desc, upd_usr_id);
            
            //upd aeCatalogAccess
            if(cat_public_ind) {
                getCreator(con);
                aeCatalogAccess.updAccess(con, ent_id, dbRegUser.getEntId(con, cat_create_usr_id), 
                                          cat_id, upd_usr_id, cat_upd_timestamp);
            }
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }
    

    public void upd(Connection con, String upd_usr_id) throws SQLException, qdbException {
        try {
            Timestamp cur_time = dbUtils.getTime(con);
            cat_upd_timestamp = cur_time;
            cat_upd_usr_id = upd_usr_id;
        
            StringBuffer SQLBuf = new StringBuffer(300);
            SQLBuf.append(" Update aeCatalog Set ");
            SQLBuf.append(" cat_title = ? ");
            SQLBuf.append(" ,cat_public_ind = ? ");
            SQLBuf.append(" ,cat_upd_timestamp = ? ");
            SQLBuf.append(" ,cat_upd_usr_id = ? ");
            SQLBuf.append(" ,cat_status = ? ");
            if (cat_tcr_id > 0) {
                SQLBuf.append(" ,cat_tcr_id = ? ");
            }
            SQLBuf.append(" Where cat_id = ? ");
            String SQL = new String(SQLBuf);
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            int index = 1;
            stmt.setString(index++, cat_title);
            stmt.setBoolean(index++, cat_public_ind);
            stmt.setTimestamp(index++, cat_upd_timestamp);
            stmt.setString(index++, cat_upd_usr_id);
            stmt.setString(index++, cat_status);
            if (cat_tcr_id > 0) {
                stmt.setLong(index++, cat_tcr_id);
            }
            stmt.setLong(index++, cat_id);
            int row = stmt.executeUpdate();
            stmt.close();
            
            if(row != 1) 
                throw new SQLException("Update aeCatalog where cat_id = " + cat_id + " will affect " + row + " row");
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }
    

    public long ins(Connection con, long owner_ent_id, String usr_id, long[] ent_ids, String[] ity_ids) 
      throws SQLException, cwException, cwSysMessage {
        try {
            if(ity_ids == null || ity_ids.length == 0) {
                throw new cwException("aeCatalog.ins: catalog has no item type");
            }
            
            Timestamp cur_time = cwSQL.getTime(con);
            cat_create_timestamp = cur_time;
            cat_upd_timestamp = cur_time;
            cat_create_usr_id = usr_id;
            cat_upd_usr_id = usr_id;
            cat_owner_ent_id = owner_ent_id;
            
            ins(con);
            
            //ins a record for creator into aeCatalogAccess
            aeCatalogAccess acc = new aeCatalogAccess();
            acc.cac_cat_id = cat_id;
            acc.cac_ent_id = dbRegUser.getEntId(con, usr_id);
            acc.cac_create_timestamp = cur_time;
            acc.cac_create_usr_id = usr_id;
            acc.ins(con);
            
            //ins a dummy node into aeTreeNode
            cat_treenode = new aeTreeNode();
            cat_treenode.tnd_itm_cnt = 0;
            cat_treenode.tnd_cat_id = cat_id;
            cat_treenode.tnd_title = cat_title;
            cat_treenode.tnd_create_timestamp = cur_time;
            cat_treenode.tnd_create_usr_id = usr_id;
            cat_treenode.tnd_upd_timestamp = cur_time;
            cat_treenode.tnd_upd_usr_id = usr_id;
            cat_treenode.tnd_desc = cat_desc;
            // get the maximum order
            cat_treenode.tnd_order = aeTreeNode.getMaxCatOrder(con, owner_ent_id) + 1;
            cat_treenode.tnd_desc = cat_desc;
            
            cat_treenode.tnd_code = cat_code;
            cat_treenode.tnd_owner_ent_id = owner_ent_id;
            
            cat_treenode.insCatNode(con);
            if (cat_treenode.tnd_code==null){
                cat_treenode.autoGenTndCode(con);
            }
            //upd aeCatalogAccess ent list
            aeCatalogAccess.updAccess(con, ent_ids, dbRegUser.getEntId(con, cat_create_usr_id), 
                                      cat_id, cat_upd_usr_id, cat_upd_timestamp);
            
            //ins into aeCatalogItemType
            DbCatalogItemType dbCit = new DbCatalogItemType();
            dbCit.cit_cat_id = this.cat_id;
            dbCit.cit_ity_owner_ent_id = owner_ent_id;
            for(int i=0; i<ity_ids.length; i++) {
                dbCit.cit_ity_id = ity_ids[i];
                dbCit.ins(con, usr_id, cur_time);
            }
            
            return cat_id;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }


    public void ins(Connection con) throws SQLException, qdbException, cwSysMessage {
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Insert into aeCatalog ");
        SQLBuf.append(" (cat_title, cat_public_ind, cat_status ");
        SQLBuf.append(" ,cat_owner_ent_id, cat_create_timestamp, cat_create_usr_id, cat_upd_timestamp, cat_upd_usr_id, cat_tcr_id)");
        SQLBuf.append(" Values ");
        SQLBuf.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS);
        int index = 1;
        stmt.setString(index++, cat_title);
        stmt.setBoolean(index++, cat_public_ind);
        stmt.setString(index++, cat_status);
        stmt.setLong(index++, cat_owner_ent_id);
        stmt.setTimestamp(index++, cat_create_timestamp);
        stmt.setString(index++, cat_create_usr_id);
        stmt.setTimestamp(index++, cat_upd_timestamp);
        stmt.setString(index++, cat_upd_usr_id);
        stmt.setLong(index++, cat_tcr_id);
        stmt.executeUpdate();

        cat_id = cwSQL.getAutoId(con, stmt, "aeCatalog", "cat_id");
        
        stmt.close();
        
    }

    public String getAssignedSitesAsXML(Connection con) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(1024);
        aeCatalogAccess cac = new aeCatalogAccess();
        cac.cac_cat_id = cat_id;
        xmlBuf.append(cac.getAssignedSitesAsXML(con));
        return xmlBuf.toString();
    }

    public String getAssignedEntityAsXML(Connection con, long owner_ent_id) throws SQLException {
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<assigned_user public_ind=\"").append(cat_public_ind).append("\">").append(dbUtils.NEWL);
        aeCatalogAccess cac = new aeCatalogAccess();
        cac.cac_cat_id = cat_id;
        xmlBuf.append(cac.getAssignEntityAsXML(con, owner_ent_id));
        xmlBuf.append("</assigned_user>");
        return xmlBuf.toString();
    }

    public String prepUpdAsXML(Connection con, long owner_ent_id)
      throws SQLException, qdbException, cwSysMessage {
        
        return contentAsXML(con);
    }

    public String catIdAsXML() {
        StringBuffer xmlBuf = new StringBuffer(2500);
        xmlBuf.append("<catalog ");
        xmlBuf.append(" cat_id=\"").append(cat_id).append("\"");
        xmlBuf.append(" upd_timestamp=\"").append(cat_create_timestamp).append("\"");
        xmlBuf.append(">").append(dbUtils.NEWL);
        xmlBuf.append("</catalog>");
        String xml = new String(xmlBuf);
        return xml;
    }

    public String contentAsXML(Connection con) 
      throws SQLException, qdbException, cwSysMessage {
        
        StringBuffer xmlBuf = new StringBuffer(2500);
        xmlBuf.append("<catalog ");
        xmlBuf.append(" cat_id=\"").append(cat_id).append("\"");
        xmlBuf.append(" item_count=\"").append(cat_treenode.tnd_itm_cnt).append("\"");   
        xmlBuf.append(" public_ind=\"").append(cat_public_ind).append("\"");
        xmlBuf.append(" status=\"").append(cat_status).append("\"");
        xmlBuf.append(">").append(dbUtils.NEWL);
        
        xmlBuf.append(cat_treenode.getNavigatorAsXML(con));
        xmlBuf.append(dbUtils.NEWL);
        
        xmlBuf.append("<title>").append(dbUtils.esc4XML(cat_title)).append("</title>").append(dbUtils.NEWL);
        xmlBuf.append("<desc>").append(dbUtils.esc4XML(cat_treenode.tnd_desc)).append("</desc>").append(dbUtils.NEWL);
        
        if(this.cat_public_ind) {
            xmlBuf.append(getAssignedSitesAsXML(con));
        } else {
            xmlBuf.append(getAssignedEntityAsXML(con, cat_owner_ent_id));
        }
        xmlBuf.append(dbUtils.NEWL);

        xmlBuf.append(aeItem.getAllItemTypeTitleInOrg(con, this.cat_owner_ent_id));
        xmlBuf.append(DbCatalogItemType.getCatalogItemTypeAsXML(con, this.cat_id));
        xmlBuf.append(dbUtils.NEWL);
        
        xmlBuf.append("<owner ent_id=\"").append(cat_owner_ent_id).append("\">");
        xmlBuf.append(dbUtils.esc4XML(getOwnerName(con,cat_owner_ent_id)));
        xmlBuf.append("</owner>");
        xmlBuf.append(dbUtils.NEWL);
        
        xmlBuf.append("<creator usr_id=\"").append(cat_create_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(cat_create_timestamp).append("\">");        
        xmlBuf.append(dbUtils.esc4XML(getUserName(con,cat_create_usr_id)));
        xmlBuf.append("</creator>");
        xmlBuf.append(dbUtils.NEWL);        
        
        xmlBuf.append("<last_updated usr_id=\"").append(cat_upd_usr_id).append("\"");
        xmlBuf.append(" timestamp=\"").append(cat_upd_timestamp).append("\">");
        xmlBuf.append(dbUtils.esc4XML(getUserName(con,cat_upd_usr_id)));
        xmlBuf.append("</last_updated>");
        xmlBuf.append(dbUtils.NEWL);
        xmlBuf.append("<training_center id=\"").append(cat_tcr_id).append("\">")
	          .append("<title>").append(dbUtils.esc4XML(cat_tcr_title)).append("</title>")
	          .append("</training_center>");
        xmlBuf.append("</catalog>");
        
        String xml = new String(xmlBuf);
        return xml;
    }
    

    public aeTreeNode getMyTreeNode(Connection con) 
        throws SQLException, cwSysMessage{
        aeTreeNode treenode = new aeTreeNode();
        
        String SQL = " Select tnd_id, tnd_itm_cnt , tnd_desc "
                   + " From aeTreeNode "
                   + " Where tnd_parent_tnd_id is null "
                   + " And tnd_cat_id = ? ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);  //set tnd_cat_id = this.cat_id
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            treenode.tnd_cat_id = cat_id;  
            treenode.tnd_title = cat_title;
            treenode.tnd_id = rs.getLong("tnd_id");
            treenode.tnd_itm_cnt = rs.getLong("tnd_itm_cnt");
            treenode.tnd_type = aeTreeNode.TND_TYPE_CAT;
            treenode.tnd_desc = rs.getString("tnd_desc");
        }
        else
            //throw new SQLException("Cannot find the catalog in TreeNode. cat_id = " + cat_id);
            throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND, "Catalog ID = " + cat_id);
        
        stmt.close();    
        return treenode;
    }

       
    
    public void get(Connection con, long[] usr_ent_ids, boolean checkStatus) 
      throws SQLException, cwSysMessage {
        
        /*
        if(checkStatus && isCatOff(con, cat_id))
            throw new cwSysMessage(CAT_OFFLINE_MSG);
            
    
        if(!isPublic(con, cat_id) && !aeCatalogAccess.hasAccessRight(con, cat_id, usr_ent_ids))
            throw new cwSysMessage(NO_ACCESS_RIGHT);
        */

        String SQL = " Select cat_title, "
                   + " cat_public_ind, cat_status, "
                   + " cat_owner_ent_id, cat_create_timestamp, "
                   + " cat_create_usr_id, cat_upd_timestamp, "
                   + " cat_upd_usr_id, cat_mobile_ind, tcr_id, tcr_title "
                   + " From aeCatalog, tcTrainingCenter "
                   + " Where cat_id = ? "
                   + " and cat_tcr_id = tcr_id ";

        if(checkStatus)
            SQL += " And cat_status <> '" + CAT_STATUS_OFF + "' ";
                   
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) {
            cat_title = rs.getString("cat_title");
            cat_public_ind = rs.getBoolean("cat_public_ind");
            cat_status = rs.getString("cat_status");
            cat_owner_ent_id = rs.getLong("cat_owner_ent_id");
            cat_create_timestamp = rs.getTimestamp("cat_create_timestamp");
            cat_create_usr_id = rs.getString("cat_create_usr_id");
            cat_upd_timestamp = rs.getTimestamp("cat_upd_timestamp");
            cat_upd_usr_id = rs.getString("cat_upd_usr_id");
            cat_treenode = getMyTreeNode(con);
            cat_tcr_id = rs.getLong("tcr_id");
            cat_tcr_title = rs.getString("tcr_title");
            cat_mobile_ind = rs.getBoolean("cat_mobile_ind");
        }
        else
            //throw new SQLException("Cannot find Catalog with cat_id = " + cat_id);
            throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND, "Catalog ID = " + cat_id);
            
        stmt.close();
    }
    
    
    public static boolean isPublic(Connection con, long cat_id)
    throws SQLException  ,cwSysMessage{
        boolean result;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select cat_public_ind From aeCatalog ");
        SQLBuf.append(" Where cat_id = ? ");
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()) 
            result = rs.getBoolean("cat_public_ind");
        else
            //throw new SQLException("Cannot find Catalog. cat_id = " + cat_id);
            throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND, "Catalog ID = " + cat_id);
        
        stmt.close();
        return result;
    }
    

    
    //Get the global cat_id of an user can see
    //catalogItemType, ity_id of the catalogs, if want to show all catalog item types, pass in null
    public static Vector globalCatalogListAsVector(Connection con,
                                                   long[] usr_ent_ids,
                                                   boolean checkStatus, 
                                                   String[] catalogItemTypes) 
        throws SQLException, cwSysMessage {
            aeCatalog catalog;
            Vector v = new Vector();
                        
            StringBuffer SQLBuf = new StringBuffer(1024);
            if(usr_ent_ids != null && usr_ent_ids.length > 0) {
                String usr_ent_ids_list = aeUtils.prepareSQLList(usr_ent_ids);
                if(catalogItemTypes == null || catalogItemTypes.length == 0) { 
                //if no catalog type is specified, just get all catalogs
                    SQLBuf.append(" Select cat_id, cat_public_ind, cat_status, cat_title, ")
                        .append(" cat_owner_ent_id, ")
                        .append(" cat_create_timestamp, cat_create_usr_id, ")
                        .append(" cat_upd_timestamp, cat_upd_usr_id, ")
                        .append(" tnd_id, tnd_itm_cnt, tnd_desc, tnd_order ")
                        .append(" From aeCatalog, aeTreeNode, aeCatalogAccess ")
                        .append(" Where cat_public_ind = ? ")
                        .append(" And tnd_cat_id = cat_id ")
                        .append(" And tnd_parent_tnd_id is null ")
                        .append(" And tnd_type = '").append(aeTreeNode.TND_TYPE_CAT).append("' ");

                    if(checkStatus) {
                        SQLBuf.append(" And cat_status <> '" + CAT_STATUS_OFF + "' ");
                    }
                    // for catalogs user has access
                    SQLBuf.append(" And cac_cat_id = cat_id ")
                        .append(" And (cac_ent_id in ").append(usr_ent_ids_list).append(" Or cac_ent_id is NULL) ");

                    SQLBuf.append("Order by cat_title asc ");

                }
                else {
                    //if catalog types are specified, join aeCatalogItemType to get those catalog type
                    StringBuffer itemTypeList = new StringBuffer(128);
                    itemTypeList.append("('").append(catalogItemTypes[0]).append("'");
                    for(int i=1; i<catalogItemTypes.length; i++) {
                        itemTypeList.append(",'").append(catalogItemTypes[i]).append("'");
                    }
                    itemTypeList.append(")");
                    
                    SQLBuf.append(" Select distinct cat_id, cat_public_ind, cat_status, cat_title, ")
                        .append(" cat_owner_ent_id, ")
                        .append(" cat_create_timestamp, cat_create_usr_id, ")
                        .append(" cat_upd_timestamp, cat_upd_usr_id, ")
                        .append(" tnd_id, tnd_itm_cnt, tnd_desc, tnd_order ")
                        .append(" From aeCatalog, aeCatalogItemType, aeTreeNode, aeCatalogAccess ")
                        .append(" Where cat_public_ind = ? ")
                        .append(" And cat_id = cit_cat_id ")
                        .append(" And tnd_cat_id = cat_id ")
                        .append(" And tnd_parent_tnd_id is null ")
                        .append(" And tnd_type = '").append(aeTreeNode.TND_TYPE_CAT).append("' ")
                        .append(" And (cit_ity_id in ").append(itemTypeList).append(" Or cit_ity_id is null)");
                    if(checkStatus) {
                        SQLBuf.append(" And cat_status <> '" + CAT_STATUS_OFF + "' ");
                    }
                    // for catalogs user has access
                    SQLBuf.append(" And cac_cat_id = cat_id ")
                        .append(" And (cac_ent_id in ").append(usr_ent_ids_list).append(" Or cac_ent_id is NULL) ");
                    SQLBuf.append("Order by tnd_order, cat_title asc ");

                }
            } else {
                if(catalogItemTypes == null || catalogItemTypes.length == 0) { 
                //if no catalog type is specified, just get all catalogs
                    SQLBuf.append(" Select cat_id, cat_public_ind, cat_status, cat_title, ")
                        .append(" cat_owner_ent_id, ")
                        .append(" cat_create_timestamp, cat_create_usr_id, ")
                        .append(" cat_upd_timestamp, cat_upd_usr_id, ")
                        .append(" tnd_id, tnd_itm_cnt, tnd_desc, tnd_order ")
                        .append(" From aeCatalog, aeTreeNode ")
                        .append(" Where cat_public_ind = ? ")
                        .append(" And tnd_cat_id = cat_id ")
                        .append(" And tnd_parent_tnd_id is null ")
                        .append(" And tnd_type = '").append(aeTreeNode.TND_TYPE_CAT).append("' ");

                    if(checkStatus) {
                        SQLBuf.append(" And cat_status <> '" + CAT_STATUS_OFF + "' ");
                    }
                    SQLBuf.append("Order by cat_title asc ");

                }
                else {
                    //if catalog types are specified, join aeCatalogItemType to get those catalog type
                    StringBuffer itemTypeList = new StringBuffer(128);
                    itemTypeList.append("('").append(catalogItemTypes[0]).append("'");
                    for(int i=1; i<catalogItemTypes.length; i++) {
                        itemTypeList.append(",'").append(catalogItemTypes[i]).append("'");
                    }
                    itemTypeList.append(")");
                    
                    SQLBuf.append(" Select distinct cat_id, cat_public_ind, cat_status, cat_title, ")
                        .append(" cat_owner_ent_id, ")
                        .append(" cat_create_timestamp, cat_create_usr_id, ")
                        .append(" cat_upd_timestamp, cat_upd_usr_id, ")
                        .append(" tnd_id, tnd_itm_cnt, tnd_desc, tnd_order ")
                        .append(" From aeCatalog, aeCatalogItemType, aeTreeNode ")
                        .append(" Where cat_public_ind = ? ")
                        .append(" And cat_id = cit_cat_id ")
                        .append(" And tnd_cat_id = cat_id ")
                        .append(" And tnd_parent_tnd_id is null ")
                        .append(" And tnd_type = '").append(aeTreeNode.TND_TYPE_CAT).append("' ")
                        .append(" And (cit_ity_id in ").append(itemTypeList).append(" Or cit_ity_id is null)");
                    if(checkStatus) {
                        SQLBuf.append(" And cat_status <> '" + CAT_STATUS_OFF + "' ");
                    }
                    SQLBuf.append("Order by tnd_order, cat_title asc ");
                }
            }
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            stmt.setBoolean(1, true);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                catalog = new aeCatalog();
                catalog.cat_treenode = new aeTreeNode();
                catalog.cat_id = rs.getLong("cat_id");
                catalog.cat_public_ind = rs.getBoolean("cat_public_ind");
                catalog.cat_status = rs.getString("cat_status");
                catalog.cat_title = rs.getString("cat_title");
                catalog.cat_owner_ent_id = rs.getLong("cat_owner_ent_id");
                catalog.cat_create_timestamp = rs.getTimestamp("cat_create_timestamp");
                catalog.cat_create_usr_id = rs.getString("cat_create_usr_id");
                catalog.cat_upd_timestamp = rs.getTimestamp("cat_upd_timestamp");
                catalog.cat_upd_usr_id = rs.getString("cat_upd_usr_id");
                catalog.cat_treenode.tnd_cat_id = catalog.cat_id;
                catalog.cat_treenode.tnd_id = rs.getLong("tnd_id");
                catalog.cat_treenode.tnd_itm_cnt = rs.getLong("tnd_itm_cnt");
                catalog.cat_treenode.tnd_type = aeTreeNode.TND_TYPE_CAT;
                catalog.cat_treenode.tnd_title = catalog.cat_title;
                catalog.cat_treenode.tnd_desc = rs.getString("tnd_desc");
                if (catalog.cat_treenode.tnd_desc == null) {
                    catalog.cat_treenode.tnd_desc = new String();
                }
                
                catalog.cat_treenode.tnd_order = rs.getLong("tnd_order");
                //catalog.cat_treenode = catalog.getMyTreeNode(con);
                v.addElement(catalog);
            }    
          stmt.close();
          return v;
    }
    
    public static String globalCatalogListAsXML(Connection con, boolean checkStatus) 
      throws SQLException, qdbException ,cwSysMessage{
        aeCatalog catalog;
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<catalog_list>").append(dbUtils.NEWL)
              .append("<nav/>").append(dbUtils.NEWL); 
                   
        Vector v_catalog = globalCatalogListAsVector(con, null, checkStatus, null);
        for(int i=0; i<v_catalog.size();i++) {
            catalog = (aeCatalog)v_catalog.elementAt(i);
            xmlBuf.append(catalog.asXML(con, checkStatus));
        }
        
        xmlBuf.append("</catalog_list>");
        return xmlBuf.toString();
    }
    
    public static Vector catalogListAsVector(Connection con, long[] usr_ent_ids,
            boolean checkStatus, String[] catalogItemTypes, WizbiniLoader wizbini,loginProfile prof) 
            throws SQLException, cwSysMessage {
    	return catalogListAsVector(con, usr_ent_ids, checkStatus, catalogItemTypes, 0, null, null, null,  wizbini,prof);
    }
    
    //Get the cat_id of an user can see
    //usr_ent_ids, ent_id of the usr and the ancesters
    //catalogItemType, ity_id of the catalogs, if want to show all catalog item types, pass in null
    public static Vector catalogListAsVector(Connection con, long[] usr_ent_ids,
                                             boolean checkStatus, String[] catalogItemTypes, long cat_tcr_id, 
                                             String sort_col, String sort_order, String filter_type, WizbiniLoader wizbini,loginProfile prof) 
                                             throws SQLException, cwSysMessage {
    	
          aeCatalog catalog;
          Vector v = new Vector();
          
        
            StringBuffer SQLBuf = new StringBuffer(1024);
            if(catalogItemTypes == null || catalogItemTypes.length == 0) { 
            //if no catalog type is specified, just get all catalogs
                SQLBuf.append(" Select distinct cat_id, cat_public_ind, cat_status, cat_title, ")
                      .append(" cat_owner_ent_id, ")
                      .append(" cat_create_timestamp, cat_create_usr_id, ")
                      .append(" cat_upd_timestamp, cat_upd_usr_id, ")
                      .append(" tnd_id, tnd_itm_cnt, tnd_desc, tnd_order, cat_tcr_id, tcr_title ")
                      .append(" From aeCatalog, aeTreeNode, tcTrainingCenter ")
                      .append(" Where ")
                      .append(" tnd_cat_id = cat_id ")
                      .append(" And cat_tcr_id = tcr_id ")
                      .append(" And tnd_parent_tnd_id is null ")
                      .append(" And tnd_type = '").append(aeTreeNode.TND_TYPE_CAT).append("' ");

                if(checkStatus) {
                    SQLBuf.append(" And cat_status <> '" + CAT_STATUS_OFF + "' ");
                }
      
                if (filter_type != null) {
                	
                	if (filter_type.equalsIgnoreCase("lrn_filter")) {
                		SQLBuf.append("and cat_tcr_id in (");
                		SQLBuf.append(ViewTrainingCenter.getLrnFliter( wizbini));
                		SQLBuf.append(")");
                	} else if(AccessControlWZB.isRoleTcInd(prof.current_role)){
                		SQLBuf.append("and cat_tcr_id in (");
                		SQLBuf.append(ViewTrainingCenter.role_fliter);
                		SQLBuf.append(")");
                	}else{
                		
                	}
                }
                
                if (cat_tcr_id > 0) {
                	SQLBuf.append(" And (cat_tcr_id in (select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ) or cat_tcr_id =?)");
                	SQLBuf.append(" And cat_tcr_id = ? ");
                	
                }
                
                if (sort_col == null) {
                    SQLBuf.append("Order by cat_tcr_id,cat_title asc ");
                }

            }
            else {
                //if catalog types are specified, join aeCatalogItemType to get those catalog type
                StringBuffer itemTypeList = new StringBuffer(128);
                itemTypeList.append("('").append(catalogItemTypes[0]).append("'");
                for(int i=1; i<catalogItemTypes.length; i++) {
                    itemTypeList.append(",'").append(catalogItemTypes[i]).append("'");
                }
                itemTypeList.append(")");
                
                SQLBuf.append(" Select distinct cat_id, cat_public_ind, cat_status, cat_title, ")
                      .append(" cat_owner_ent_id, ")
                      .append(" cat_create_timestamp, cat_create_usr_id, ")
                      .append(" cat_upd_timestamp, cat_upd_usr_id, ")
                      .append(" tnd_id, tnd_itm_cnt, tnd_desc, tnd_order, cat_tcr_id, tcr_title ")
                      .append(" From aeCatalog, aeCatalogItemType, aeTreeNode, tcTrainingCenter ")
                      .append(" Where ")
                      .append(" tnd_cat_id = cat_id ")
                      .append(" And cat_tcr_id = tcr_id ")
                      .append(" And tnd_parent_tnd_id is null ")
                      .append(" And tnd_type = '").append(aeTreeNode.TND_TYPE_CAT).append("' ")
                      .append(" And cat_id = cit_cat_id ")
                      .append(" And (cit_ity_id in ").append(itemTypeList).append(" Or cit_ity_id is null) ");
                if (filter_type != null) {
                	
                	if (filter_type.equalsIgnoreCase("lrn_filter")) {
                		SQLBuf.append("and cat_tcr_id in (");
                		SQLBuf.append(ViewTrainingCenter.getLrnFliter( wizbini));
                		SQLBuf.append(")");
                	} else if(AccessControlWZB.isRoleTcInd(prof.current_role)){
                		SQLBuf.append("and cat_tcr_id in (");
                		SQLBuf.append(ViewTrainingCenter.role_fliter);
                		SQLBuf.append(")");
                	}else{
                		
                	}
                }
                
                if (cat_tcr_id > 0) {
                	SQLBuf.append(" And (cat_tcr_id in (select tcn_child_tcr_id from tcRelation where tcn_ancestor = ? ) or cat_tcr_id =?)");
                    SQLBuf.append(" And cat_tcr_id = ? ");
                }
         
                if(checkStatus) {
                    SQLBuf.append(" And cat_status <> '" + CAT_STATUS_OFF + "' ");
                }
        
                if (sort_col == null) {
                    SQLBuf.append("Order by tnd_order, cat_title asc ");
                }

            }
            if (sort_col != null) {
                SQLBuf.append(" order by ").append(sort_col).append(" ").append(sort_order);
            }
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            
            if (filter_type != null) {
            	
            	if (filter_type.equalsIgnoreCase("lrn_filter")) {
            		stmt.setLong(index++, usr_ent_ids[0]);
            		stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
            		//修复Bug 6557 对树选择所有培训中心时进行过滤不是该培训中心下的不显示
//            		stmt.setLong(index++, usr_ent_ids[usr_ent_ids.length-1]);
            		
            	} else if(AccessControlWZB.isRoleTcInd(prof.current_role)){
            		stmt.setLong(index++, usr_ent_ids[0]);
            		stmt.setString(index++, prof.current_role);
            	}else{
            		
            	}
            }


            if (cat_tcr_id > 0) {
                prof.my_top_tc_id = cat_tcr_id;
                stmt.setLong(index++, prof.my_top_tc_id);
                stmt.setLong(index++, prof.my_top_tc_id);
            	stmt.setLong(index++, cat_tcr_id);
  
            }

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                catalog = new aeCatalog();
                catalog.cat_treenode = new aeTreeNode();
                catalog.cat_id = rs.getLong("cat_id");
                catalog.cat_public_ind = rs.getBoolean("cat_public_ind");
                catalog.cat_status = rs.getString("cat_status");
                catalog.cat_title = rs.getString("cat_title");
                catalog.cat_owner_ent_id = rs.getLong("cat_owner_ent_id");
                catalog.cat_create_timestamp = rs.getTimestamp("cat_create_timestamp");
                catalog.cat_create_usr_id = rs.getString("cat_create_usr_id");
                catalog.cat_upd_timestamp = rs.getTimestamp("cat_upd_timestamp");
                catalog.cat_upd_usr_id = rs.getString("cat_upd_usr_id");
                catalog.cat_treenode.tnd_cat_id = catalog.cat_id;
                catalog.cat_treenode.tnd_id = rs.getLong("tnd_id");
                catalog.cat_treenode.tnd_itm_cnt = rs.getLong("tnd_itm_cnt");
                catalog.cat_treenode.tnd_type = aeTreeNode.TND_TYPE_CAT;
                catalog.cat_treenode.tnd_title = catalog.cat_title;
                catalog.cat_treenode.tnd_desc = rs.getString("tnd_desc");
                catalog.cat_tcr_id = rs.getLong("cat_tcr_id");
                catalog.cat_tcr_title = rs.getString("tcr_title");
                if (catalog.cat_treenode.tnd_desc == null) {
                    catalog.cat_treenode.tnd_desc = new String();
                }
                
                catalog.cat_treenode.tnd_order = rs.getLong("tnd_order");
                //catalog.cat_treenode = catalog.getMyTreeNode(con);
                v.addElement(catalog);
            }

          stmt.close();
         
          return v;
    }

    //old api for tc_disabled
    public static String catalogListAsXML(Connection con, long[] usr_ent_ids, boolean checkStatus, loginProfile prof, WizbiniLoader wizbini) 
      throws SQLException, qdbException ,cwSysMessage{
        return catalogListAsXML(con, usr_ent_ids, checkStatus, 0, 0, 0, null, null, false, null, prof,  wizbini);
    }

    public static String catalogListAsXML(Connection con, long[] usr_ent_ids, boolean checkStatus, long tcr_id, int page,
    										 int page_size, String sort_col, String sort_order, boolean tc_enabled, String filter_type, loginProfile prof, WizbiniLoader wizbini) 
    										 throws SQLException, cwSysMessage, qdbException {
        aeCatalog catalog;
        StringBuffer xmlBuf = new StringBuffer(1024);
        xmlBuf.append("<cat_tcr_id>").append(tcr_id).append("</cat_tcr_id>");
        xmlBuf.append("<catalog_list>").append(dbUtils.NEWL);
        //get defult tcr_id
        if (tcr_id == -1) {
	    	if (tc_enabled && !prof.isLrnRole) {
	    		tcr_id = ViewTrainingCenter.getDefaultTc(con, prof);
	    	} else if (tc_enabled && prof.isLrnRole) {
	    		tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
	    	}
        }
        
    	//if select all_tcr.tcr_id == 0
        if (tcr_id == 0) {
        	xmlBuf.append("<show_all>true</show_all>"); 
        }
        xmlBuf.append("<nav/>").append(dbUtils.NEWL);
    	
    		if (sort_col == null) {
    			sort_col = "cat_title";
    		}
    		if (sort_order == null) {
    			sort_order = "ASC";
    		}
    
    	 Vector v_catalog = catalogListAsVector(con, usr_ent_ids, checkStatus, null, tcr_id, sort_col, sort_order, filter_type,  wizbini,prof);
       
        if(prof.isLrnRole) {
        	Vector v_catalog_id = new Vector();
        	for(int i=0; i<v_catalog.size();i++) {
        		aeCatalog cata = (aeCatalog)v_catalog.elementAt(i);
        		v_catalog_id.add(new Long(cata.cat_treenode.tnd_id ));
        	}
        	Hashtable hash_catalog = getCatalogTargetLrn(con, v_catalog_id, prof.usr_ent_id);
        	xmlBuf.append(getCatalogByLrnAsXML(hash_catalog));
        }
    	if (tc_enabled && tcr_id == 0) {
    		if (page == 0) {
    			page = 1;
    		}
    		if (page_size == 0) {
    			page_size = 10;
    		}
            //page control
            int start = page_size * (page-1);
            int count = 0;
            for(int i=0; i<v_catalog.size();i++) {
                if (count >= start && count < start+page_size) {
                    catalog = (aeCatalog)v_catalog.elementAt(i);
                    xmlBuf.append(catalog.asXML(con,checkStatus));
                }
                count++;
            }
    		cwPagination pagn = new cwPagination();
    		pagn.totalRec = v_catalog.size();
    		pagn.totalPage = (int)Math.ceil((float)count/page_size);
    		pagn.pageSize = page_size;
    		pagn.curPage = page;
    		pagn.sortCol = cwPagination.esc4SortSql(sort_col);
    		pagn.sortOrder = cwPagination.esc4SortSql(sort_order);
    		pagn.ts = null;
    		xmlBuf.append(pagn.asXML());
    	} else {
            for(int i=0; i<v_catalog.size();i++) {
                catalog = (aeCatalog)v_catalog.elementAt(i);
                xmlBuf.append(catalog.asXML(con,checkStatus));
            }
    	}
        xmlBuf.append(TrainingCenter.getTcrAsXml(con, tcr_id, prof.root_ent_id));
        xmlBuf.append("</catalog_list>");
        //stmt.close();
    	
    	return xmlBuf.toString();
    }
    public static String getCatalogByLrnAsXML(Hashtable hash) {
    	StringBuffer xml = new StringBuffer();
    	xml.append("<tnd_cnt_lst>");
    	if (hash != null) {
    		Enumeration enum_cata = hash.keys();
    		while (enum_cata.hasMoreElements()) {
    			Long tnd_id = (Long)enum_cata.nextElement();
    			Long cnt = (Long)hash.get(tnd_id);
    			xml.append("<tnd_cnt tnd_id=\"").append(tnd_id).append("\"");
    			xml.append(" cnt=\"").append(cnt).append("\">");
    			xml.append("</tnd_cnt>");
    		}
    	}
    	xml.append("</tnd_cnt_lst>");
    	return xml.toString();
    }
    //Get the cat_id of an user can see in his/her organization
    //usr_ent_ids, ent_id of the usr and the ancesters
    //catalogItemType, ity_id of the catalogs, if want to show all catalog item types, pass in null
    public static Vector orgCatalogListAsVector(Connection con, long[] usr_ent_ids,
            boolean checkStatus, String[] catalogItemTypes, long tcr_id, boolean tcEnabled) 
            throws SQLException, cwSysMessage {
    	
          aeCatalog catalog;
          Vector v = new Vector();

            String usr_ent_ids_list = "(0)";
            
            if(usr_ent_ids != null) 
                usr_ent_ids_list = aeUtils.prepareSQLList(usr_ent_ids);
            
            StringBuffer SQLBuf = new StringBuffer(1024);
            if(catalogItemTypes == null || catalogItemTypes.length == 0) {
            //if no catalog type is specified, just get all catalogs
                SQLBuf.append(" Select distinct cat_id, cat_public_ind, cat_status, cat_title, ")
                      .append(" cat_owner_ent_id, ")
                      .append(" cat_create_timestamp, cat_create_usr_id, ")
                      .append(" cat_upd_timestamp, cat_upd_usr_id, ")
                      .append(" tnd_id, tnd_itm_cnt, tnd_desc, tnd_order ")
                      .append(" From aeCatalog, aeCatalogAccess, aeTreeNode ")
                      .append(" Where ")
                      .append(" tnd_cat_id = cat_id ")
                      .append(" And tnd_parent_tnd_id is null ")
                      .append(" And tnd_type = '").append(aeTreeNode.TND_TYPE_CAT).append("' ")
                      .append(" And cat_public_ind = ? ");

                if(checkStatus) {
                    SQLBuf.append(" And cat_status <> '" + CAT_STATUS_OFF + "' ");
                }
                if(tcEnabled && tcr_id != 0) {
            		//在上司报表中，上司可以选所有目录 
            		 SQLBuf.append("  And (cat_tcr_id = ? or cat_tcr_id in (select tcn_child_tcr_id from tcRelation where tcn_ancestor =?  ) )  ");
                }
                // for catalogs user has access
                SQLBuf.append(" And cac_cat_id = cat_id And cac_ent_id in ")
                      .append(usr_ent_ids_list)
                      .append("Order by cat_title asc ");

            }
            else {
                //if catalog types are specified, join aeCatalogItemType to get those catalog type
                StringBuffer itemTypeList = new StringBuffer(128);
                itemTypeList.append("('").append(catalogItemTypes[0]).append("'");
                for(int i=1; i<catalogItemTypes.length; i++) {
                    itemTypeList.append(",'").append(catalogItemTypes[i]).append("'");
                }
                itemTypeList.append(")");
                
                SQLBuf.append(" Select distinct cat_id, cat_public_ind, cat_status, cat_title, ")
                      .append(" cat_owner_ent_id, ")
                      .append(" cat_create_timestamp, cat_create_usr_id, ")
                      .append(" cat_upd_timestamp, cat_upd_usr_id, ")
                      .append(" tnd_id, tnd_itm_cnt, tnd_desc, tnd_order ")
                      .append(" From aeCatalog, aeCatalogAccess, aeCatalogItemType, aeTreeNode ")
                      .append(" Where ")
                      .append(" tnd_cat_id = cat_id ")
                      .append(" And tnd_parent_tnd_id is null ")
                      .append(" And tnd_type = '").append(aeTreeNode.TND_TYPE_CAT).append("' ")
                      .append(" And cat_id = cit_cat_id ")
                      .append(" And (cit_ity_id in ").append(itemTypeList).append(" Or cit_ity_id is null) ")
                      .append(" And cat_public_ind = ? ");
                           
                if(checkStatus) {
                    SQLBuf.append(" And cat_status <> '" + CAT_STATUS_OFF + "' ");
                }
                if(tcEnabled && tcr_id != 0) {
                	//在添加修改课程时，只是选中课程所指定培训中心下的目录 

                	SQLBuf.append("  And (cat_tcr_id = ? )  ");
                	
                }

                // for catalogs user has access
                SQLBuf.append(" And cac_cat_id = cat_id And cac_ent_id in ")
                      .append(usr_ent_ids_list)
                      .append("Order by tnd_order, cat_title asc ");

            }
            PreparedStatement stmt = con.prepareStatement(SQLBuf.toString());
            int index = 1;
            stmt.setBoolean(index++, false);
            if(tcEnabled && tcr_id != 0){
                stmt.setLong(index++, tcr_id);
                if(catalogItemTypes == null || catalogItemTypes.length == 0) {
                	 stmt.setLong(index++, tcr_id);
                }
               
            }
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                catalog = new aeCatalog();
                catalog.cat_treenode = new aeTreeNode();
                catalog.cat_id = rs.getLong("cat_id");
                catalog.cat_public_ind = rs.getBoolean("cat_public_ind");
                catalog.cat_status = rs.getString("cat_status");
                catalog.cat_title = rs.getString("cat_title");
                catalog.cat_owner_ent_id = rs.getLong("cat_owner_ent_id");
                catalog.cat_create_timestamp = rs.getTimestamp("cat_create_timestamp");
                catalog.cat_create_usr_id = rs.getString("cat_create_usr_id");
                catalog.cat_upd_timestamp = rs.getTimestamp("cat_upd_timestamp");
                catalog.cat_upd_usr_id = rs.getString("cat_upd_usr_id");
                catalog.cat_treenode.tnd_cat_id = catalog.cat_id;
                catalog.cat_treenode.tnd_id = rs.getLong("tnd_id");
                catalog.cat_treenode.tnd_itm_cnt = rs.getLong("tnd_itm_cnt");
                catalog.cat_treenode.tnd_type = aeTreeNode.TND_TYPE_CAT;
                catalog.cat_treenode.tnd_title = catalog.cat_title;
                catalog.cat_treenode.tnd_desc = rs.getString("tnd_desc");
                if (catalog.cat_treenode.tnd_desc == null) {
                    catalog.cat_treenode.tnd_desc = new String();
                }
                
                catalog.cat_treenode.tnd_order = rs.getLong("tnd_order");
                //catalog.cat_treenode = catalog.getMyTreeNode(con);
                v.addElement(catalog);
            }    
          stmt.close();
          return v;
    }
    
    private static String getActiveSitesAsXML(Connection con) throws SQLException {
        Vector vSite = acSite.getActiveSites(con);
        StringBuffer xmlBuf = new StringBuffer(512);
        xmlBuf.append("<site_list>");
        for(int i=0; i<vSite.size(); i++) {
            acSite site = (acSite)vSite.elementAt(i);
            xmlBuf.append("<site ent_id=\"").append(site.ste_ent_id).append("\">")
                    .append("<name>").append(dbUtils.esc4XML(site.ste_name)).append("</name>")
                    .append("</site>");
        }
        xmlBuf.append("</site_list>");
        return xmlBuf.toString();
    }

    public static String prepInsGlbCatalog(Connection con, long owner_ent_id) 
      throws SQLException, qdbException {
        try {
            Timestamp cur_time = dbUtils.getTime(con);
            
            StringBuffer xmlBuf = new StringBuffer(2500);
            xmlBuf.append("<catalog ");
            //xmlBuf.append(" public_ind=\"").append(false).append("\"");
            //xmlBuf.append(" status=\"").append(aeCatalog.CAT_STATUS_OFF).append("\"");
            xmlBuf.append(">").append(dbUtils.NEWL);
            
            xmlBuf.append("<nav></nav>");
            xmlBuf.append(dbUtils.NEWL);
            
            //xmlBuf.append("<assigned_user public_ind=\"").append(false).append("\"/>");
            xmlBuf.append(getActiveSitesAsXML(con));
            xmlBuf.append(dbUtils.NEWL);
            
            //item type list
            /*
            xmlBuf.append(aeItem.getAllItemTypeTitleInOrg(con, owner_ent_id));
            xmlBuf.append(dbUtils.NEWL);
            */
            
            xmlBuf.append("<owner></owner>");
            xmlBuf.append(dbUtils.NEWL);
            
            xmlBuf.append("<creator></creator>");
            xmlBuf.append(dbUtils.NEWL);        
            
            xmlBuf.append("<last_updated timestamp=\"").append(cur_time).append("\">");
            xmlBuf.append("</last_updated>");
            xmlBuf.append(dbUtils.NEWL);
            
            xmlBuf.append("</catalog>");
            
            String xml = new String(xmlBuf);
            return xml;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }
    
    public static String blankContentAsXML(Connection con, loginProfile prof, boolean from_show_all,String cat_tcr_id_str) 
      throws SQLException, qdbException {
        try {
            Timestamp cur_time = dbUtils.getTime(con);
            
            StringBuffer xmlBuf = new StringBuffer(2500);
            xmlBuf.append("<catalog ");
            xmlBuf.append(" public_ind=\"").append(false).append("\"");
            xmlBuf.append(" status=\"").append(aeCatalog.CAT_STATUS_OFF).append("\"");
            xmlBuf.append(">").append(dbUtils.NEWL);
            
            xmlBuf.append("<nav></nav>");
            xmlBuf.append(dbUtils.NEWL);
            
            //xmlBuf.append(getAssignedEntityAsXML(con, owner_ent_id));
            xmlBuf.append("<assigned_user public_ind=\"").append(false).append("\"/>");
  
            //xmlBuf.append(getBlankCatGroupsAsXML(con, owner_ent_id));
            xmlBuf.append(dbUtils.NEWL);
            
            //item type list
            xmlBuf.append(aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id));
            xmlBuf.append(dbUtils.NEWL);
            
            xmlBuf.append("<owner></owner>");
            xmlBuf.append(dbUtils.NEWL);
            
            xmlBuf.append("<creator></creator>");
            xmlBuf.append(dbUtils.NEWL);        
            
            xmlBuf.append("<last_updated timestamp=\"").append(cur_time).append("\">");
            xmlBuf.append("</last_updated>");
            xmlBuf.append(dbUtils.NEWL);
            
            xmlBuf.append("</catalog>");
            xmlBuf.append("<from_show_all>").append(from_show_all).append("</from_show_all>");
            long tcr_id = 0;
            if(cat_tcr_id_str != null && cat_tcr_id_str.length()>0){
            	try{
            		tcr_id = Long.parseLong(cat_tcr_id_str);
            	}catch(NumberFormatException e){
            		tcr_id = ViewTrainingCenter.getDefaultTcId(con, prof);
            	}
            }else{
            	tcr_id = ViewTrainingCenter.getDefaultTcId(con, prof);
            }
            DbTrainingCenter tcr = DbTrainingCenter.getInstance(con, tcr_id);
            if(tcr != null) {
                xmlBuf.append("<training_center id=\"").append(tcr.tcr_id).append("\">")
    	          .append("<title>").append(dbUtils.esc4XML(tcr.tcr_title)).append("</title>")
    	          .append("</training_center>");
            }
            String xml = new String(xmlBuf);
            return xml;
        }
        catch(qdbException e) {
            throw new SQLException(e.getMessage());
        }
    }
    

    public static boolean isCatOff(Connection con, long cat_id)
    throws SQLException  ,cwSysMessage{
        boolean result;
        String status;
        StringBuffer SQLBuf = new StringBuffer(300);
        
        SQLBuf.append(" Select cat_status From aeCatalog ");
        SQLBuf.append(" Where cat_id = ? ");
        String SQL = new String(SQLBuf);
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.prepareStatement(SQL);
        stmt.setLong(1, cat_id);
            rs = stmt.executeQuery();
        if(rs.next()) 
            status = rs.getString("cat_status");
        else
            //throw new SQLException("Cannot find Catalog, cat_id = " + cat_id);
            throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND, "Catalog ID = " + cat_id);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        if(status.equalsIgnoreCase(CAT_STATUS_OFF))
            result = true;
        else
            result = false;
            
        return result;
    }
    

    public static String getOwnerName(Connection con, long owner_ent_id) 
    throws SQLException, qdbException  ,cwSysMessage{
        String result;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select usg_display_bil ");
        SQLBuf.append(" From UserGroup ");
        SQLBuf.append(" Where usg_ent_id = ? ");
        
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setLong(1, owner_ent_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) 
            result = rs.getString("usg_display_bil");
        else
            //throw new SQLException("Cannot find the owner from UserGroup. ent_id = " + owner_ent_id);
            throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND, "Entity ID = " + owner_ent_id);
        
        stmt.close();
        return result;
    }
 
    
    public static String getUserName(Connection con, String usr_id) 
    throws SQLException  ,cwSysMessage{
        String result;
        StringBuffer SQLBuf = new StringBuffer(300);
        SQLBuf.append(" Select usr_display_bil ");
        SQLBuf.append(" From RegUser ");
        SQLBuf.append(" Where usr_id = ? ");
        
        String SQL = new String(SQLBuf);
        PreparedStatement stmt = con.prepareStatement(SQL);
        stmt.setString(1, usr_id);
        ResultSet rs = stmt.executeQuery();
        
        if(rs.next()) 
            result = rs.getString("usr_display_bil");
        else
            //throw new SQLException("Cannot find the user from RegUser. ent_id = " + usr_id);
            throw new cwSysMessage(aeUtils.MSG_REC_NOT_FOUND, "User ID = " + usr_id);
        
        stmt.close();
        return result;
    }    
    
    /**
	 * 检测同一培训中心下是否有同名的顶层目录
	 * @param con
	 * @param id
	 * @return true if catalog name not existed
	 * @throws SQLException
	 */
    public boolean validCatalogName(Connection con, long id)
        throws SQLException{
            
    	String sql = "select cat_id from aeCatalog where cat_tcr_id = ? and cat_title = ? ";
        if( id != 0 )
            sql += " and cat_id <> ? ";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setLong(1, cat_tcr_id);
        stmt.setString(2, cat_title);
        
        if( id != 0 )
            stmt.setLong(3, id);
        boolean flag = true;
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            flag = false;
        }
        stmt.close();
        return flag;
            
    }
    
    
    
    public void updTitle(Connection con)
        throws SQLException{
            
            String SQL = " UPDATE aeCatalog "
                       + " SET cat_title = ?, "
                       + " cat_upd_usr_id = ?, "
                       + " cat_upd_timestamp = ? "
                       + " WHERE cat_id = ? ";
            
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setString(1, cat_title);
            stmt.setString(2, cat_upd_usr_id);
            stmt.setTimestamp(3, cat_upd_timestamp);
            stmt.setLong(4, cat_id);
            if( stmt.executeUpdate() != 1 )
                throw new SQLException("Failed to update catalog title, cat_id = " + cat_id);
            stmt.close();
            return;
        }
    
    
    public String getTitleListAsXML(Connection con, loginProfile prof, boolean checkStatus, String filter_type, WizbiniLoader wizbini)
        throws SQLException, qdbException, cwSysMessage {
            
            long[] usr_ent_ids = aeAction.usrGroups(prof.usr_ent_id, prof.usrGroups);
            Vector catLst = new Vector();
            if(filter_type != null) {
            	catLst = catalogListAsVector(con, usr_ent_ids, checkStatus, null, 0, null, null, filter_type,  wizbini,prof);
            } else {
            	catLst = catalogListAsVector(con, usr_ent_ids, checkStatus, null,  wizbini, prof);
            }
            Vector catVec = (this.cat_public_ind) ?
                            globalCatalogListAsVector(con, null, checkStatus, null) :
                            catLst;
            StringBuffer xml = new StringBuffer();
            xml.append("<catalog_list>");
            for(int i=0; i<catVec.size(); i++){
                aeCatalog cat = (aeCatalog)catVec.elementAt(i);
                xml.append("<catalog ")
                   .append(" cat_id=\"").append(cat.cat_id).append("\" ") 
                   .append(" order=\"").append(cat.cat_treenode.tnd_order).append("\" ")
                   .append(" status=\"").append(cat.cat_status).append("\" ")
                   .append(" tnd_id=\"").append(cat.cat_treenode.tnd_id).append("\" ")
                   .append(" public_ind=\"").append(cat.cat_public_ind).append("\" ")
                   .append(" >")
			       .append("<title>").append(dbUtils.esc4XML(cat.cat_title)).append("</title>")
			       .append("</catalog>");
			}
			xml.append("</catalog_list>");
			
			return xml.toString();
			
		}

    public String getTitleListAsXML(Connection con, long[]usr_ent_ids, boolean checkStatus, WizbiniLoader wizbini, loginProfile prof)
        throws SQLException, qdbException, cwSysMessage {
            
            Vector catVec = catalogListAsVector(con, usr_ent_ids, checkStatus, null,  wizbini, prof);
            StringBuffer xml = new StringBuffer();
            xml.append("<catalog_list>");
            for(int i=0; i<catVec.size(); i++){
                aeCatalog cat = (aeCatalog)catVec.elementAt(i);
                xml.append("<catalog ")
                   .append(" cat_id=\"").append(cat.cat_id).append("\" ") 
                   .append(" order=\"").append(cat.cat_treenode.tnd_order).append("\" ")
                   .append(" status=\"").append(cat.cat_status).append("\" ")
                   .append(" tnd_id=\"").append(cat.cat_treenode.tnd_id).append("\" ")
                   .append(" public_ind=\"").append(cat.cat_public_ind).append("\" ")
                   .append(" >")
			       .append("<title>").append(dbUtils.esc4XML(cat.cat_title)).append("</title>")
			       .append("</catalog>");
			}
			xml.append("</catalog_list>");
			
			return xml.toString();
			
		}

    private String asXML(Connection con, boolean checkStatus) 
        throws SQLException, cwSysMessage, qdbException {
        StringBuffer xmlBuf = new StringBuffer(256);
        xmlBuf.append("<catalog ")
              .append(" cat_id=\"").append(this.cat_id).append("\"")
              .append(" order=\"").append(this.cat_treenode.tnd_order).append("\"")
              .append(" public_ind=\"").append(this.cat_public_ind).append("\"")
              .append(" status=\"").append(this.cat_status).append("\"")
              .append(" item_count=\"").append(this.cat_treenode.tnd_itm_cnt).append("\"")
              .append(" cur_level_item_count=\"").append(aeTreeNode.getChildNodeCount(con, this.cat_treenode.tnd_id, aeTreeNode.TND_TYPE_ITEM, checkStatus)).append("\"")
              .append(" tnd_id=\"").append(this.cat_treenode.tnd_id).append("\"").append("> ").append(dbUtils.NEWL)
              .append(" <title>").append(dbUtils.esc4XML(this.cat_title)).append("</title> ").append(dbUtils.NEWL)
              .append(" <desc>").append(dbUtils.esc4XML(this.cat_treenode.tnd_desc)).append("</desc> ").append(dbUtils.NEWL)
              .append(" <owner ent_id=\"").append(this.cat_owner_ent_id).append("\"").append("> ")
              .append(" </owner> ").append(dbUtils.NEWL)
              .append(" <creator usr_id=\"").append(this.cat_create_usr_id).append("\"")
              .append(" timestamp=\"").append(this.cat_create_timestamp).append("\"").append("> ")
              .append(" </creator>").append(dbUtils.NEWL)
              .append(" <last_updated usr_id=\"").append(this.cat_upd_usr_id).append("\"")
              .append(" timestamp=\"").append(this.cat_upd_timestamp).append("\"").append("> ")
              .append(" </last_updated>")
              .append(dbUtils.NEWL)
              .append(this.cat_treenode.getChildNodesAsXML(con, checkStatus, null)).append(dbUtils.NEWL)
              .append("<training_center id=\"").append(this.cat_tcr_id).append("\">")
              .append("<title>").append(dbUtils.esc4XML(this.cat_tcr_title)).append("</title>")
              .append("</training_center>")
              .append("</catalog>").append(dbUtils.NEWL);
        return xmlBuf.toString();
    }

	public static boolean canEditByTcadmin(Connection con, long tnd_cat_id, long usr_ent_id) throws SQLException {
		boolean has_right = false;
        PreparedStatement stmt = null;
        String sql = "select * from aeCatalog " +
        			 "where cat_id = ? " +
        			 "and cat_tcr_id in (" +
        			 ViewTrainingCenter.ta_fliter + 
        			 ")";
        try {
        	stmt = con.prepareStatement(sql);
        	int index = 1;
        	stmt.setLong(index++, tnd_cat_id);
        	stmt.setLong(index++, usr_ent_id);
        	
        	ResultSet rs = stmt.executeQuery();
        	if (rs.next()) {
        		has_right = true;
        	}
        } finally {
        	if (stmt != null) {
        		stmt.close();
        	}
        }
		return has_right;
	}

	public static boolean canReadByLrn(Connection con, long tnd_cat_id, long root_ent_id, long usr_ent_id, WizbiniLoader wizbini) throws SQLException {
		String sql = " select cat_id from aeCatalog " +
					 " where cat_id = ? " +
					 " and cat_owner_ent_id = ? " +
					 " and cat_tcr_id in (" +
					 ViewTrainingCenter.getLrnFliter( wizbini) +
					 ")";
		
		boolean has_right = false;
        PreparedStatement stmt = null;
        try {
        	stmt = con.prepareStatement(sql);
        	int index = 1;
        	stmt.setLong(index++, tnd_cat_id);
        	stmt.setLong(index++, root_ent_id);
        	stmt.setLong(index++, usr_ent_id);
        	stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
        	ResultSet rs = stmt.executeQuery();
        	if (rs.next()) {
        		has_right = true;
        	}
        } finally {
        	if (stmt != null) {
        		stmt.close();
        	}
        }
		return has_right;
	}

	public long getTcrByCatId(Connection con, long root_ent_id) throws SQLException {
		String sql = " select cat_tcr_id from aeCatalog where cat_id = ? and cat_owner_ent_id = ? ";
		
		PreparedStatement stmt = null;
		long tcr_id = 0;
		try {
		stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, cat_id);
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


	public static void delMultCata(Connection con, boolean tc_enabled, long[] cat_id_lst, Timestamp[] cat_in_upd_timestamp_lst, loginProfile prof) throws SQLException, cwSysMessage, qdbException {
        aeCatalog cat = new aeCatalog();
        Vector undelVec = new Vector();
		for(int i=0;i<cat_id_lst.length;i++) {
            cat.cat_id = cat_id_lst[i];
            //access control
            AcCatalog accat = new AcCatalog(con);
            if(!accat.hasUpdPrivilege(cat.cat_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
            	throw new cwSysMessage(NO_OPT_RIGHT);
            }
            //只是与角色相关的功能才判断是否有执行权限
            //fix Bug 6226 - 自定义角色未与培训中心关联，课程目录新建报权限不足
            if(AccessControlWZB.isRoleTcInd(prof.current_role)){
                if (tc_enabled && !aeCatalog.canEditByTcadmin(con, cat.cat_id, prof.usr_ent_id)) {
                	throw new cwSysMessage(NO_OPT_RIGHT);
                }
            }
            try {
                cat.del(con, prof.root_ent_id, cat_in_upd_timestamp_lst[i]);
            } catch (cwSysMessage e) {
            	undelVec.addElement(new Long(cat.cat_id));
            	continue;
            }
        }
		
		if (undelVec.size() > 0) {
        	throw new cwSysMessage("AETN09", getUndelCatTitleAsStr(con, undelVec));
		}
	}


	private static String getUndelCatTitleAsStr(Connection con, Vector undelVec) throws SQLException {
		boolean isFirst = true;
		String sql = " select cat_title from aeCatalog where cat_id in " + cwUtils.vector2list(undelVec);
		PreparedStatement stmt = null;
		StringBuffer result = new StringBuffer();
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				if (!isFirst) {
					result.append(",");
				}
				result.append(cwUtils.esc4XML(rs.getString("cat_title")));
				isFirst = false;
			}
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return result.toString();
	}
	public static Hashtable getCatalogTargetLrn(Connection con, Vector catalog_vec, long usr_ent_id) throws SQLException {
		String groupAncesterSql = dbEntityRelation.getAncestorListSql(usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
		String gradeAncesterSql = dbEntityRelation.getAncestorListSql(usr_ent_id, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
		
		String tableName = cwSQL.createSimpleTemptable(con, "tmp_tnd_id", cwSQL.COL_TYPE_LONG, 0);
		cwSQL.insertSimpleTempTable(con, tableName, catalog_vec, cwSQL.COL_TYPE_LONG);

		StringBuffer sqlBuf = new StringBuffer();
		sqlBuf.append(" SELECT tnd_parent_tnd_id, sum(cnt) cnt_sum ")
			.append(" FROM (SELECT tnd_parent_tnd_id, count(distinct itm_id) cnt")
			.append(" FROM aeItem, aeItemTargetRuleDetail, aeTreeNode")
			.append(" WHERE itm_id = tnd_itm_id")
			.append(" AND tnd_parent_tnd_id in ( ").append(" SELECT tmp_tnd_id FROM ").append(tableName).append(")")
			.append(" AND itm_status = ? ")
			.append(" ANd itm_id = ird_itm_id")
			.append(" AND itm_access_type = ird_type")
			.append(" AND ird_group_id in ( ").append(groupAncesterSql).append(" )")
			.append(" AND ird_grade_id in ( ").append(gradeAncesterSql).append(" )")
			.append(" GROUP BY tnd_parent_tnd_id ")
			.append(" UNION ALL")
			.append(" SELECT tnd_parent_tnd_id, COUNT(itm_id) cnt")
			.append(" FROM aeItem, aeTreeNode")
			.append(" WHERE itm_id = tnd_itm_id")
			.append(" AND tnd_parent_tnd_id in ( ").append(" SELECT tmp_tnd_id FROM ").append(tableName).append(")")
			.append(" AND itm_status = ? ")
			.append(" AND (itm_access_type = ?  or itm_access_type is NULL)")
			.append(" GROUP BY tnd_parent_tnd_id ) cata ")
			.append(" GROUP BY tnd_parent_tnd_id ");
		
		PreparedStatement stmt = con.prepareStatement(sqlBuf.toString());
		int index = 1;
		stmt.setString(index++, aeItem.ITM_STATUS_ON);
		stmt.setString(index++, aeItem.ITM_STATUS_ON);
		stmt.setString(index++, "ALL");
		ResultSet rs = stmt.executeQuery();
		Hashtable hash = new Hashtable();
		while (rs.next()) {
			long tnd_cat_id = rs.getLong("tnd_parent_tnd_id");
			long cnt = rs.getLong("cnt_sum");
			hash.put(new Long(tnd_cat_id), new Long(cnt));
		}
		rs.close();
		stmt.close();
		return hash;
	}

	public static long getTcrIdByTndId(Connection con, long tnd_id) throws SQLException {
    	long tcr_id = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("select cat_tcr_id")
			.append(" from aeCatalog, aeTreeNode")
			.append(" where cat_id = tnd_cat_id")
			.append(" and tnd_id = ?");
		PreparedStatement stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setLong(index++, tnd_id);
		ResultSet rs = stmt.executeQuery();
		if (rs.next()) {
			tcr_id = rs.getLong("cat_tcr_id");
		}
		rs.close();
		stmt.close();
    	return tcr_id;
    }

	public static String existsMobildCatAsXML(Connection con, long root_ent_id) throws SQLException {
		String xml = "<existsMobileCat>" + existsMobildCat(con, root_ent_id) + "</existsMobileCat>";
		
		return xml;
	}
	
	/**
	 * 是否已经存在移动课程目录
	 * @param con
	 * @param root_ent_id
	 * @return
	 * @throws SQLException
	 */
	public static boolean existsMobildCat(Connection con, long root_ent_id) throws SQLException {
		boolean existsMobileCat = false;
		
//		String sql = "SELECT cat.cat_id FROM aeCatalog cat " +
//					  "INNER JOIN tcTrainingCenter tcr ON (tcr.tcr_id = cat.cat_tcr_id) " +
//					  "WHERE tcr.tcr_ste_ent_id = ? AND cat.cat_mobile_ind = 1";
//		
//		PreparedStatement ps = null;
//		ResultSet rs = null;
//		
//		try {
//			ps = con.prepareStatement(sql);
//			ps.setLong(1, root_ent_id);
//			
//			rs = ps.executeQuery();
//			
//			if (rs.next()) {
//				existsMobileCat = true;
//			}
//		} catch (SQLException e) {
//			throw e;
//		} finally {
//			cwSQL.cleanUp(rs, ps);
//		}
		
		return existsMobileCat;
	}
	
	/**
	 * 删除培训目录和与其关联的信息 
	 */
	public void delCatalogAllInfo(Connection con) throws SQLException, cwSysMessage {
		aeTreeNodeRelation.delTnrByCatId(con, cat_id);
		aeTreeNode.delChildCatalogByCatId(con, cat_id);
        delByDelTcr(con);
    }
}
    
