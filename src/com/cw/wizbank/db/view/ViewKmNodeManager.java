package com.cw.wizbank.db.view;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.db.DbKmLibraryObject;
import com.cw.wizbank.db.DbKmLibraryObjectBorrow;
import com.cw.wizbank.db.DbKmNode;
import com.cw.wizbank.db.DbKmNodeAccess;
import com.cw.wizbank.db.DbKmNodeActnHistory;
import com.cw.wizbank.db.DbKmNodeSubscription;
import com.cw.wizbank.db.view.ViewKmLibraryObject.itemDetails;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

/**
A database class to get result related to kmNode
*/
public class ViewKmNodeManager {

    public static final String[] SUBSCRIBE_TYPE_DOMAIN_LIST = {
                                                    DbKmNodeActnHistory.TYPE_DOMAIN_MOD_OWN, 
                                                    DbKmNodeActnHistory.TYPE_DOMAIN_NEW_SUB, 
                                                    DbKmNodeActnHistory.TYPE_DOMAIN_DEL_SUB, 
                                                    DbKmNodeActnHistory.TYPE_DOMAIN_NEW_PUB, 
                                                    DbKmNodeActnHistory.TYPE_DOMAIN_DEL_PUB
                                                    };


    private static final String SQL_GET_NODE_ACCESS = " SELECT nac_access_type, nac_ent_id, usr_display_bil AS DISP "
                                                    + "  From kmNodeAccess, RegUser "
                                                    + "  WHERE nac_nod_id = ? AND usr_ent_id = nac_ent_id " 
                                                    + " UNION "
                                                    + " SELECT nac_access_type, nac_ent_id, usg_display_bil AS DISP "
                                                    + "  From kmNodeAccess, UserGroup "
                                                    + "  WHERE nac_nod_id = ? AND usg_ent_id = nac_ent_id " 
                                                    + " ORDER BY nac_access_type, DISP ";




    /**
    Get the access xml of a node
    */
    public static String getNodeAccessAsXML(Connection con, long node_id)
        throws SQLException, cwSysMessage {
        
        StringBuffer xml = new StringBuffer(1024);
        
        String cur_type = null;
        String prv_type = null;
        boolean flag = false; // to check for the READER
        
        DbKmNode kmNode = new DbKmNode();
        kmNode.nod_id = node_id;
        kmNode.get(con);
        
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(SQL_GET_NODE_ACCESS);
            stmt.setLong(1, node_id);
            stmt.setLong(2, node_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cur_type = rs.getString("nac_access_type");
                if (prv_type == null || !cur_type.equals(prv_type)) {
                    if (prv_type != null) {
                        xml.append("</assigned_entity_list>");
                    }
                    xml.append("<assigned_entity_list type=\"").append(rs.getString("nac_access_type")).append("\" ");
                    if(rs.getString("nac_access_type").equalsIgnoreCase(DbKmNodeAccess.ACCESS_TYPE_READER)) {
                        flag = true;
                        if(kmNode.nod_display_option_ind == 1) {
                            xml.append(" display_option=\"true\"");
                        } else {
                            xml.append(" display_option=\"false\"");
                        }
                    }
                    xml.append(">");
                }
                xml.append("<entity id=\"").append(rs.getLong("nac_ent_id"))
                   .append("\" name=\"").append(cwUtils.esc4XML(rs.getString("DISP")))
                   .append("\"/>");
                prv_type = cur_type;
            }
            if (prv_type !=null) {
                xml.append("</assigned_entity_list>");
            }
            if(!flag) {
                xml.append("<assigned_entity_list type=\"").append(DbKmNodeAccess.ACCESS_TYPE_READER).append("\" ");
                if(kmNode.nod_display_option_ind == 1) {
                    xml.append(" display_option=\"true\"");
                } else {
                    xml.append(" display_option=\"false\"");
                }
                xml.append("/>");
            }                
        } finally {
            if(stmt!=null) stmt.close();
        }

        return xml.toString();
    }

    /**
    Get all the subscription of the user by using the email send type and email send timestamp
    */
    public static void getSubscriptionByEmailSendType(Connection con, long usr_ent_id, String email_send_type, Timestamp email_send_timestamp, Hashtable actionHash, Hashtable subscriptionHash)
        throws SQLException {

        String sql
            = " SELECT nah_id, nah_nod_id, nsb_type, nsb_email_send_type, nsb_email_from_timestamp, nsb_from_timestamp, nah_type, nah_xml  "
            + "  FROM kmNodeSubscription, kmNodeActnHistory "
            + " WHERE nah_nod_id = nsb_nod_id "
            + "  AND nsb_usr_ent_id = ? "
            + "  AND nsb_email_send_type = ? "
            + "  AND nah_update_timestamp > nsb_email_from_timestamp "
            + "  AND nah_update_timestamp <= ? "
            + " ORDER by nsb_type, nah_nod_id, nah_update_timestamp ";        
        
        PreparedStatement stmt =con.prepareStatement(sql);
        stmt.setLong(1, usr_ent_id);
        stmt.setString(2, email_send_type);
        stmt.setTimestamp(3, email_send_timestamp);

        ResultSet rs = stmt.executeQuery();
CommonLog.debug("getSubscriptionByEmailSendType 1");        
        getSubscription(rs, actionHash, subscriptionHash);
CommonLog.debug("getSubscriptionByEmailSendType 2");
        stmt.close();
    }

    /**
    Get all the subscription of the user by using the action history id
    */
    public static void getSubscriptionByActionID(Connection con, long usr_ent_id, long action_id, Hashtable actionHash, Hashtable subscriptionHash)
        throws SQLException {

        String sql
            = " SELECT distinct(nah_id), nah_nod_id, nsb_type, nsb_email_send_type, nsb_email_from_timestamp, nsb_from_timestamp, nah_type, nah_xml  "
            + "  FROM kmNodeSubscription, kmNodeActnHistory "
            + " WHERE nah_nod_id = nsb_nod_id "
            + "  AND nah_id = ? and nsb_usr_ent_id = ? ";
        
        PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt = con.prepareStatement(sql);
        stmt.setLong(1, action_id);
        stmt.setLong(2, usr_ent_id);
        rs = stmt.executeQuery();
        getSubscription(rs, actionHash, subscriptionHash);

        stmt.close();
    }
    
    /**
    Get all the subscription of the user by using the nsb_from_timestamp
    */
    public static void getSubscriptionByUser(Connection con, long usr_ent_id, Hashtable actionHash, Hashtable subscriptionHash)
        throws SQLException {

        String sql
            = " SELECT nah_id, nah_nod_id, nsb_type, nsb_email_send_type, nsb_email_from_timestamp, nsb_from_timestamp, nah_type, nah_xml  "
            + "  FROM kmNodeSubscription, kmNodeActnHistory "
            + " WHERE nsb_usr_ent_id = ? AND nah_nod_id = nsb_nod_id "
            + "  AND nah_update_timestamp >= nsb_from_timestamp "
            + " ORDER by nsb_type, nah_nod_id, nah_update_timestamp ";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt = con.prepareStatement(sql);
        stmt.setLong(1, usr_ent_id);
        rs = stmt.executeQuery();
        getSubscription(rs, actionHash, subscriptionHash);

        stmt.close();
    }

    private static void getSubscription(ResultSet rs, Hashtable actionHash, Hashtable subscriptionHash)
        throws SQLException {

        Vector domainTypeVec = cwUtils.String2vector(SUBSCRIBE_TYPE_DOMAIN_LIST);
            
        DbKmNodeActnHistory ah = null;
        DbKmNodeSubscription ns = null;

        Vector actionVec = null;
        Long nodeID = null;
        String nodeType = null;
        
        while (rs.next())  {
            nodeID = new Long(rs.getLong("nah_nod_id"));

            ns = (DbKmNodeSubscription) subscriptionHash.get(nodeID);
            if (ns == null) {
                ns = new DbKmNodeSubscription();
                ns.nsb_nod_id = nodeID.longValue();
                ns.nsb_type = rs.getString("nsb_type");
                ns.nsb_email_send_type = rs.getString("nsb_email_send_type");
                ns.nsb_email_from_timestamp = rs.getTimestamp("nsb_email_from_timestamp");
                ns.nsb_from_timestamp = rs.getTimestamp("nsb_from_timestamp");
                subscriptionHash.put(nodeID, ns);
            }
            
            ah = new DbKmNodeActnHistory();
            ah.nah_nod_id = ns.nsb_nod_id;
            ah.nah_type = rs.getString("nah_type");
            boolean validAction = false;
            if (ns.nsb_type.equals(DbKmNodeSubscription.SUBSCRIPTION_TYPE_DOMAIN) &&
                domainTypeVec.contains(ah.nah_type)) {
                    validAction = true;
            }
            
            if (validAction) {
                actionVec = (Vector) actionHash.get(nodeID);
                if (actionVec == null) {
                    actionVec = new Vector();
                }
                ah.nah_xml = rs.getString("nah_xml");
                actionVec.addElement(ah);
                actionHash.put(nodeID, actionVec);
            }
        
        }

    }

    /**
    Get the list of users who will recieve notification on the subscribed node
    If a user subscribed to several nodes and all the nodes have not changed since the last send timestamp to the current timestamp, the users will be exclued.
    */
    public static Vector getUsersToNotify(Connection con, String email_send_type, Timestamp email_send_timestamp, long node_id)
        throws SQLException {
        
        Vector domainTypeVec = cwUtils.String2vector(SUBSCRIBE_TYPE_DOMAIN_LIST);

        String sql 
            = " SELECT nsb_usr_ent_id, nah_nod_id, nsb_type, nah_type  "
            + "  FROM kmNodeSubscription, kmNodeActnHistory "
            + " WHERE nah_nod_id = nsb_nod_id "
            + "  AND nah_update_timestamp > nsb_email_from_timestamp "
            + "  AND nah_update_timestamp <= ? "
            + "  AND nsb_email_send_type = ? ";
         if (node_id > 0) {
            sql += "  AND nsb_nod_id = " + node_id;
         }
         sql += " ORDER by nsb_usr_ent_id, nah_nod_id, nah_update_timestamp ";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt = con.prepareStatement(sql);
        stmt.setTimestamp(1, email_send_timestamp);
        stmt.setString(2, email_send_type);
        rs = stmt.executeQuery();
        
        Vector notifyVec = new Vector();

        while (rs.next())  {
            Long entID = new Long(rs.getLong("nsb_usr_ent_id"));
            if (!notifyVec.contains(entID)) {
                // Check if the action is included in the notify list
                String nsbType = rs.getString("nsb_type");
                String actnType = rs.getString("nah_type");
                if (nsbType.equals(DbKmNodeSubscription.SUBSCRIPTION_TYPE_DOMAIN) &&
                    domainTypeVec.contains(actnType)) {
                    notifyVec.addElement(entID);
                }
            }
        }

        stmt.close();
        return notifyVec;
    }

    /**
    Get all the folder and object information
    */
    public static ResultSet getNodeInfo(Connection con, Vector idVec) throws SQLException {
        
        String id_lst = cwUtils.vector2list(idVec);
        
        String SQL_GET_NODE_INFO = " SELECT nod_id AS NODEID, nod_type AS NTYPE, fld_type AS NSUBTYPE, fld_title AS NTITLE, usr_display_bil AS NDISPLAY, fld_update_timestamp AS NTIME"
                                + "  From kmNode, kmFolder, RegUser "
                                + "  WHERE fld_update_usr_id = usr_id AND nod_id = fld_nod_id AND nod_id IN " + id_lst
                                + " UNION "
                                + " SELECT nod_id AS NODEID, nod_type AS NTYPE, obj_type AS NSUBTYPE, obj_title AS NTITLE, usr_display_bil AS NDISPLAY, obj_update_timestamp AS NTIME"
                                + "  From kmNode, kmObject, RegUser "
                                + "  WHERE obj_update_usr_id = usr_id AND nod_id = obj_bob_nod_id AND nod_id IN " + id_lst
                                + " ORDER BY NTYPE, NSUBTYPE, NTITLE ";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        stmt = con.prepareStatement(SQL_GET_NODE_INFO);
        rs = stmt.executeQuery();

        return rs;

    }

    /**
    Get the system usr_id of the organization which is the same as the user with usr_ent_id
    */
    public static String getSysUsrId(Connection con, long usr_ent_id)
        throws SQLException {
        
        String sql =  " SELECT SYSUSER.usr_id FROM acSite, RegUser NUSER, Reguser SYSUSER "
                +     " Where ste_default_sys_ent_id = SYSUSER.usr_ent_id " 
                +     "    AND NUSER.usr_ste_ent_id = ste_ent_id "
                +     "    AND NUSER.usr_ent_id = ? ";
        
        PreparedStatement stmt = null;
        String sys_usr_id = null; 
        try {
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, usr_ent_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sys_usr_id = rs.getString(1);
            }
        } finally {
            if(stmt!=null) stmt.close();
        }

        return sys_usr_id;
    }
    
    public static Vector getNodeCatalog(Connection con, String usr_group_lst, long nod_id) throws SQLException {
    	Vector vec = new Vector();
		String sql = "select distinct nod_create_timestamp, nod_parent_nod_id,fld_title,nod_create_timestamp, nod_create_usr_id,lnk_title,lnk_target_nod_id,fld_cpty,"
				+ " bob_code,obj_type,obj_title,obj_author,lio_num_copy_available, lio_num_copy, usr_email, usr_tel_1"
				+ " from kmnodeaccess,kmnode as kmn,kmlink as kml,kmfolder,kmObject,kmBaseObject,kmLibraryObject,reguser"
				+ " where obj_update_usr_id = usr_id and nac_nod_id=fld_nod_id and lnk_target_nod_id=bob_nod_id and bob_nod_id=obj_bob_nod_id and"
				+ " lio_bob_nod_id = bob_nod_id and kmn.nod_id=kml.lnk_nod_id and nac_nod_id=kmn.nod_parent_nod_id and nac_ent_id in " + usr_group_lst
				+ " and exists(select nod_id  from kmnode where nac_nod_id=nod_parent_nod_id and exists(select lnk_nod_id "
				+ " from kmlink where lnk_nod_id=nod_id )) and lnk_target_nod_id = ? ";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, nod_id);
		ResultSet rs = stmt.executeQuery();
		String competency = "";
		String title = "";
		while (rs.next()) {
			String tmp = rs.getString("fld_title");
			title += (competency.equals("")) ? tmp : "; " + tmp;
			
			tmp = rs.getString("fld_cpty");
			if (tmp != null && tmp.trim().length() > 0) {
				competency += (competency.equals("")) ? tmp : "; " + tmp;
			}
		}
		rs.close();
		stmt.close();
		
		vec.add(title);
		vec.add(competency);
		return vec;
    }
    
    public static String getNewItemsAsXml(Connection con,long usr_ent_id,String usr_group_lst)
		throws SQLException{
    	String sql="select distinct objNode.nod_create_timestamp as obj_ct, kmn.nod_parent_nod_id, fld_title, kmn.nod_create_timestamp ,"+
    		" kmn.nod_create_usr_id, lnk_title, lnk_target_nod_id, fld_cpty,"+
    		" bob_code, obj_type, obj_title, obj_author, lio_num_copy_available, lio_num_copy, usr_email, usr_tel_1 "+
    		" from kmnodeaccess, kmnode as kmn, kmlink as kml, kmfolder, kmObject, kmBaseObject, kmLibraryObject, reguser, kmnode as objNode "+
    		" where obj_update_usr_id = usr_id and nac_nod_id=fld_nod_id and lnk_target_nod_id=bob_nod_id and bob_nod_id=obj_bob_nod_id and"+
    		" lio_bob_nod_id = bob_nod_id and kmn.nod_id=kml.lnk_nod_id and nac_nod_id=kmn.nod_parent_nod_id "+
    		" and bob_nod_id = objNode.nod_id "+
    		" and obj_online_ind = ? "+
    		" and nac_ent_id in " + usr_group_lst + " and exists( select a.nod_id from kmnode a "+
    		" where nac_nod_id = a.nod_parent_nod_id and exists( select b.lnk_nod_id  "+
    		" from kmlink b where b.lnk_nod_id = a.nod_id )) order by objNode.nod_create_timestamp desc";
    	Vector nod_vec=new Vector();
    	StringBuffer xml = new StringBuffer();
    	PreparedStatement stmt=null;
    	ResultSet rs=null;
    	boolean is_exists = true;
		try {
			stmt = con.prepareStatement(sql);
			stmt.setBoolean(1, true);
			rs = stmt.executeQuery();
			xml.append("<itm_lst>").append(cwUtils.NEWL);
			while (rs.next()) {
				is_exists = true;
				if (nod_vec.size() > 0) {
					if (nod_vec.size() > 4) {
						break;
					} else {
						for (int i = 0; i < nod_vec.size(); i++) {
							String nod_id = (String) nod_vec.elementAt(i);
							if (new Long(rs.getLong("lnk_target_nod_id")).toString().equalsIgnoreCase(nod_id)) {
								break;
							} else if (i == (nod_vec.size() - 1) && !new Long(rs.getLong("lnk_target_nod_id")).toString().equalsIgnoreCase(nod_id)) {
								nod_vec.add(new Long(rs.getLong("lnk_target_nod_id")).toString());
								is_exists = false;
							}
						}
					}
				} else {
					nod_vec.add(new Long(rs.getLong("lnk_target_nod_id")).toString());
					is_exists = false;
				}
    			if(is_exists==false){
    				long nod_id = rs.getLong("lnk_target_nod_id");
    				Vector v = getNodeCatalog(con, usr_group_lst, nod_id);
    				
    				String cat_title = (String) v.elementAt(0);
    				String cat_cpty = (String) v.elementAt(1);
    				
    			xml.append("<itm nod_id=\"").append(nod_id).append("\"")    			
    			.append(" nod_create_date=\"").append(rs.getTimestamp("obj_ct")).append("\"")
    			.append(" competency=\"").append(cwUtils.esc4XML(cat_cpty)).append("\"")
    			.append(" catalog=\"").append(cwUtils.esc4XML(cat_title)).append("\"")
    			.append(" type=\"").append(rs.getString("obj_type")).append("\"")
    			.append(" title=\"").append(cwUtils.esc4XML(rs.getString("obj_title"))).append("\"")
    			.append(" call_num=\"").append(cwUtils.esc4XML(rs.getString("bob_code"))).append("\"")
    			.append(" avail_copy=\"").append(rs.getLong("lio_num_copy_available")).append("\"")
    			.append(" num_copy=\"").append(rs.getLong("lio_num_copy")).append("\"")
    			.append(" num_reserve=\"").append(DbKmLibraryObjectBorrow.getLibraryBorrowStatusCount(con,rs.getLong("lnk_target_nod_id"), DbKmLibraryObjectBorrow.STATUS_RESERVE)).append("\" ")
    			.append(" author=\"").append(cwUtils.esc4XML(rs.getString("obj_author"))).append("\"")
    			.append(" adm_email=\"").append(cwUtils.esc4XML(cwUtils.escNull(rs.getString("usr_email")))).append("\"")
    			.append(" adm_tel=\"").append(cwUtils.esc4XML(cwUtils.escNull(rs.getString("usr_tel_1")))).append("\"")
    			.append(" >");    			
    			/////
    			 ResultSet borrowRS = ViewKmLibraryObject.getBorrowInfo(con, usr_ent_id, rs.getLong("lnk_target_nod_id"));    			
    			xml.append("<borrow_list>");
//                for(int j=0;j<vLibObj.itm.lob_status.size();j++) {
    			 while(borrowRS.next()) {
                    String lob_status = borrowRS.getString("lob_status");
                    long lob_id = borrowRS.getLong("lob_id");
                    long lob_renew_no = new Long(borrowRS.getInt("lob_renew_no")).longValue();
                    xml.append("<borrow ")
                       .append(" lob_id=\"" + lob_id + "\" ")
                       .append(" renewal=\"" + lob_renew_no + "\" ")
                       .append(" status=\"" + lob_status + "\" ")
                       .append(" />");
                }
                xml.append("</borrow_list>");
       
                xml.append("</itm>");
    			}
    		}
    		xml.append("</itm_lst>").append(cwUtils.NEWL);
    	}finally{
    		if(stmt!=null) stmt.close();
    	}
    	return xml.toString();
    }
    
}

