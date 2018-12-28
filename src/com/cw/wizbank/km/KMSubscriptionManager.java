package com.cw.wizbank.km;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.*;



public class KMSubscriptionManager{
    
    /**
    Get the subscription of the user
    @param usr_ent_id entity id of the user
    */
    public static String getSubscriptionAsXML(Connection con, long usr_ent_id) throws SQLException {
        
        Hashtable subscriptionHash = new Hashtable();
        Hashtable actionHash = new Hashtable();
        ViewKmNodeManager.getSubscriptionByUser(con, usr_ent_id, actionHash, subscriptionHash);
        
        // Get the the subscriptio no matter the subscribed node has changes or not
        subscriptionHash = DbKmNodeSubscription.getAll(con, usr_ent_id);
        
        return formatSubscriptionXML(con, actionHash, subscriptionHash);
    }
    
    public static String formatSubscriptionXML(Connection con, Hashtable actionHash, Hashtable subscriptionHash) throws SQLException {

        StringBuffer xml = new StringBuffer(2048);
        xml.append("<subscription>");
        xml.append("<child_node_list>");

        Enumeration ids = subscriptionHash.keys();
        Vector idVec = new Vector();
        while(ids.hasMoreElements()) {
            idVec.addElement((Long) ids.nextElement());
        }

        DbKmNodeSubscription ns = null;
        DbKmNodeActnHistory ah = null;
        Vector actionVec = null;
        if (idVec.size() > 0) {
            Hashtable ancestorXMLHash = ViewKmFolderManager.getFolderAncestorAsXML(con, idVec, false);

            ResultSet rs = ViewKmNodeManager.getNodeInfo(con, idVec);
            while (rs.next()) {
                Long nodeID = new Long(rs.getLong("NODEID"));
                ns = (DbKmNodeSubscription) subscriptionHash.get(nodeID);
                xml.append("<child id=\"").append(nodeID.longValue())
                   .append("\" type=\"").append(rs.getString("NTYPE"))
                   .append("\" subtype=\"").append(rs.getString("NSUBTYPE"))
                   .append("\" email_send_type=\"").append(ns.nsb_email_send_type)
                   .append("\" email_from_timestamp=\"").append(ns.nsb_email_from_timestamp)
                   .append("\" from_timestamp=\"").append(ns.nsb_from_timestamp)
                   .append("\">");
                if (rs.getString("NTYPE").equals(DbKmNode.NODE_TYPE_FOLDER)) {
                    xml.append(ancestorXMLHash.get(nodeID));
                }
                xml.append("<title>").append(cwUtils.esc4XML(rs.getString("NTITLE"))).append("</title>");
                xml.append("<update display_bil=\"").append(cwUtils.esc4XML(rs.getString("NDISPLAY")))
                   .append("\" timestamp=\"").append(rs.getTimestamp("NTIME")).append("\"/>");
                xml.append("<action_history_list>");
                actionVec = (Vector) actionHash.get(nodeID);
                if (actionVec != null) {
                    for(int i=0;i<actionVec.size();i++) {
                        ah = (DbKmNodeActnHistory) actionVec.elementAt(i);
                        xml.append(ah.nah_xml);
                    }
                }
                xml.append("</action_history_list>");
                xml.append("</child>");
            }
            rs.close();
        }
        
        xml.append("</child_node_list>");
        xml.append("</subscription>");
        
        return xml.toString();

    }


    /**
    Insert a new subscription
    @param usr_ent_id entity id of the user
    */
    public static void insSub(Connection con, DbKmNodeSubscription sub, long usr_ent_id) throws SQLException, cwSysMessage {
        
        sub.nsb_usr_ent_id = usr_ent_id;
        if (sub.exist(con)) {
            throw new cwSysMessage(KMModule.SMSG_KM_SUBSCRIPTION_EXISTS);
        }
        
        Timestamp curTime = cwSQL.getTime(con);
        sub.nsb_from_timestamp = curTime;
        sub.nsb_create_timestamp = curTime;
        sub.ins(con);

    }

    /**
    Clear the subscription of a list of node
    @param node_lst ids of the nodes
    @param usr_ent_id entity id of the user
    */
    public static void clearSub(Connection con, long[] node_lst, long usr_ent_id) throws SQLException {
        
        DbKmNodeSubscription.upd(con, node_lst, usr_ent_id, cwSQL.getTime(con));

    }

    /**
    Delete the subscription of a list of node
    @param node_lst ids of the nodes
    @param usr_ent_id entity id of the user
    */
    public static void delSub(Connection con, long[] node_lst, long usr_ent_id) throws SQLException {
        
        DbKmNodeSubscription.del(con, node_lst, usr_ent_id);

    }

    /**
    Insert an action of the node in the action history
    Notify user who subscribed the node and recieve email when change occur
    */
    public static void insAction(Connection con, ViewKmNodeActn action) throws cwSysMessage, SQLException, cwException {
        
        action.save(con);
        KMScheduler.addAction(action);
        
    }



}
