package com.cw.wizbank.km;

import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.util.*;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.accesscontrol.*;

public class KMFolderManager{
    
    private DbKmFolder folder = null;
    public long[] reader_ent_lst = null;
    public long[] author_ent_lst = null;
    public long[] owner_ent_lst = null;
    
    public String usr_id;
    public String usr_display_bil;
    
    /**
    Contructor
    @param folder_ folder object
    @param usr_id_ usr_id of the user
    @param display_bil_ display name of the user
    */
    public KMFolderManager(DbKmFolder folder_, String usr_id_, String display_bil_) {
        folder = folder_;
        usr_id = usr_id_;
        usr_display_bil = display_bil_;
    		//$$ KMFolderManager1.move(0,0);
}
    
    /**
    Insert a new Worder folder or Domain
    @param usr_ent_id entity id of the creator
    @return id of the new folder
    */
    public long insFolder(Connection con, long usr_ent_id) throws SQLException, cwSysMessage, cwException {
        
        Timestamp curTime = cwSQL.getTime(con);

        folder.nod_type = DbKmNode.NODE_TYPE_FOLDER;
        folder.nod_create_usr_id = usr_id;
        folder.nod_create_timestamp = curTime;
        folder.fld_update_usr_id = usr_id;
        folder.fld_update_timestamp = curTime;
        
        folder.ins(con);

        if (reader_ent_lst !=null) {
            DbKmNodeAccess.insByType(con, folder.fld_nod_id, DbKmNodeAccess.ACCESS_TYPE_READER, reader_ent_lst);
        }
        
        // Make sure at least the creator have owner right on the folder
        if (owner_ent_lst == null) {
            owner_ent_lst = new long[1];
            owner_ent_lst[0] = usr_ent_id;
        }
        DbKmNodeAccess.insByType(con, folder.fld_nod_id, DbKmNodeAccess.ACCESS_TYPE_OWNER, owner_ent_lst);

        if (folder.fld_type.equals(DbKmFolder.FOLDER_TYPE_WORK) && author_ent_lst !=null) {
            DbKmNodeAccess.insByType(con, folder.fld_nod_id, DbKmNodeAccess.ACCESS_TYPE_AUTHOR, author_ent_lst);
        }    
        
        // Action History
        if (folder.nod_parent_nod_id > 0) {
            ViewKmNodeActn action = new ViewKmNodeActn();
            if (folder.fld_type.equals(DbKmFolder.FOLDER_TYPE_WORK)) {
                action.type = DbKmNodeActnHistory.TYPE_WORK_NEW_SUB;
            }else {
                action.type = DbKmNodeActnHistory.TYPE_DOMAIN_NEW_SUB;
            }
            action.node_id = folder.nod_parent_nod_id;
            action.modified_node_id = folder.fld_nod_id;
            action.title = folder.fld_title;
            action.usr_id = usr_id;
            action.usr_display_bil = usr_display_bil;
            action.update_timestamp = curTime;
            KMSubscriptionManager.insAction(con, action);
        }
        
        return folder.fld_nod_id;
    }
    
    /**
    update a Worder folder or Domain
    Propagate its permission to its child if permission changed
    @param usr_ent_id entity id of the user who update the record
    @param usrGroupsList ancestor group list of the user (included his/her own enity id)
    */
    public void updFolder(Connection con, long usr_ent_id, String usrGroupsList) throws SQLException, cwSysMessage, cwException {
        
        folder.fld_type = folder.getType(con);
        
        if (!folder.equalsTimstamp(con)) {
            throw new cwSysMessage(KMModule.SMSG_KM_INCORRECT_TIMESTAMP);
        }
        
        // Make sure at least the creator have owner right on the folder
        if (owner_ent_lst == null) {
            owner_ent_lst = new long[1];
            owner_ent_lst[0] = usr_ent_id;
        }
        updSelfAndInheritedSuccessor(con, folder.fld_nod_id, folder.fld_type, reader_ent_lst, author_ent_lst, owner_ent_lst, usrGroupsList);

        Timestamp curTime = cwSQL.getTime(con);
        folder.fld_update_usr_id = usr_id;
        folder.fld_update_timestamp = curTime;
        
        // Action History
        ViewKmNodeActn action = new ViewKmNodeActn();
        if (folder.fld_type.equals(DbKmFolder.FOLDER_TYPE_WORK)) {
            action.type = DbKmNodeActnHistory.TYPE_WORK_MOD_OWN;
        }else {
            action.type = DbKmNodeActnHistory.TYPE_DOMAIN_MOD_OWN;
        }
        action.node_id = folder.fld_nod_id;
        action.modified_node_id = folder.fld_nod_id;
        action.title_org = folder.getTitle(con);            // Original title
        action.title = folder.fld_title;                    // Current tilte (maybe changed)
        action.usr_id = usr_id;
        action.usr_display_bil = usr_display_bil;
        action.update_timestamp = curTime;
        KMSubscriptionManager.insAction(con, action);
        
        folder.upd(con);
        
        // update kmNode display_option_ind
        DbKmNode kmNode = new DbKmNode();
        kmNode.nod_id = folder.fld_nod_id;
        kmNode.get(con);
        kmNode.nod_display_option_ind = folder.nod_display_option_ind;
        kmNode.upd(con);
    }
    
    /**
    Delete a Worder folder or Domain
    */
    public void delFolder(Connection con) throws SQLException, cwSysMessage, cwException {

        if (!folder.equalsTimstamp(con)) {
            throw new cwSysMessage(KMModule.SMSG_KM_INCORRECT_TIMESTAMP);
        }

        if (DbKmNode.hasChild(con, folder.fld_nod_id)) {
            throw new cwSysMessage("The folder has child attached to it.");
        }
        
        // Delete all the node action history
        DbKmNodeActnHistory.delByNode(con, folder.fld_nod_id);

        // Delete all the node subscription
        DbKmNodeSubscription.delByNode(con, folder.fld_nod_id);

        // Delete all the node access
        DbKmNodeAccess.delByNode(con, folder.fld_nod_id);

        // Delete all the link that targeted to this folder
        DbKmLink.delTarget(con, folder.fld_nod_id);

        //Delete all link that assign workplace to this folder
        DbKmNodeAssignment.delAllByID(con, folder.fld_nod_id);
        
        
        long parent_nod_id = DbKmNode.getParentID(con, folder.fld_nod_id);

        // Action History
        if (parent_nod_id > 0) {
            ViewKmNodeActn action = new ViewKmNodeActn();
            folder.fld_type = folder.getType(con);
            if (folder.fld_type.equals(DbKmFolder.FOLDER_TYPE_WORK)) {
                action.type = DbKmNodeActnHistory.TYPE_WORK_DEL_SUB;
            }else {
                action.type = DbKmNodeActnHistory.TYPE_DOMAIN_DEL_SUB;
            }
            action.node_id = parent_nod_id;
            action.modified_node_id = folder.fld_nod_id;
            action.title = folder.getTitle(con);
            action.usr_id = usr_id;
            action.usr_display_bil = usr_display_bil;
            action.update_timestamp = cwSQL.getTime(con);
            KMSubscriptionManager.insAction(con, action);
        }

        folder.del(con);

    }
    
    /**
    Get the xml of a Worder folder or Domain
    */
    public String folderAsXML(Connection con) throws SQLException, cwSysMessage {

        folder.get(con);
        String ancestor_xml = ViewKmFolderManager.getFolderAncestorAsXML(con, folder.fld_nod_id);
        String access_xml = ViewKmNodeManager.getNodeAccessAsXML(con, folder.fld_nod_id);
        
        StringBuffer xml = new StringBuffer(1024);
        xml.append("<node id=\"").append(folder.fld_nod_id)
           .append("\" type=\"").append(folder.fld_type)
           .append("\" nature=\"").append(folder.fld_nature)
           .append("\" obj_cnt=\"").append(folder.fld_obj_cnt)
           .append("\" parent_nod_id=\"").append(folder.nod_parent_nod_id)
           .append("\">");
        xml.append(ancestor_xml);
        xml.append("<title>").append(cwUtils.esc4XML(folder.fld_title)).append("</title>");
        xml.append("<desc>").append(cwUtils.esc4XML(folder.fld_desc)).append("</desc>");
        xml.append("<create usr_id=\"").append(folder.nod_create_usr_id)
           .append("\" timestamp=\"").append(folder.nod_create_timestamp).append("\"/>");
        xml.append("<update usr_id=\"").append(folder.fld_update_usr_id)
           .append("\" timestamp=\"").append(folder.fld_update_timestamp).append("\"/>");
        xml.append("<access_control inherit_ind=\"").append(folder.nod_acl_inherit_ind).append("\">");
        xml.append(access_xml);
        xml.append("</access_control>");
        
        xml.append("</node>");
        
        return xml.toString();
    }
    
    /**
    Get the xml of Worder folder or Domain Main page (Root level)
    @param owner_ent_id orgnanization id of the user
    @param usrGroupsList ancestor group list of the user (included his/her own enity id)
    */
    public String folderMainAsXML(Connection con, long owner_ent_id, String usrGroupsList, boolean bShowChild) throws SQLException, cwSysMessage {
        return folderMainAsXML(con, owner_ent_id, usrGroupsList, bShowChild, null);
    }

    public String folderMainAsXML(Connection con, long owner_ent_id, String usrGroupsList, boolean bShowChild, loginProfile prof) throws SQLException, cwSysMessage {
        
        DbKmFolder fld = null;
        Vector childVec = null;
        Vector folderVec = new Vector();
        Vector folderIDVec = new Vector();
        
        folderVec = ViewKmFolderManager.getRootFolders(con, folder.fld_type, owner_ent_id);
        for (int i=0;i<folderVec.size();i++) {
            fld = (DbKmFolder) folderVec.elementAt(i);
            folderIDVec.addElement(new Long(fld.fld_nod_id));
        }
        
        Hashtable folderHash = null;
        if (bShowChild) {
            folderHash = ViewKmFolderManager.getChildFoldersHash(con, folderIDVec);
        }else {
            folderHash = new Hashtable();
        }
        
        Vector allVec = new Vector();
        for (int i=0;i<folderVec.size();i++) {
            fld = (DbKmFolder) folderVec.elementAt(i);
            allVec.addElement(new Long(fld.target_nod_id));
            
            childVec = (Vector) folderHash.get(new Long(fld.fld_nod_id));
            if (childVec != null) {
                for (int j=0;j<childVec.size();j++) {
                    fld = (DbKmFolder) childVec.elementAt(j);
                    allVec.addElement(new Long(fld.target_nod_id));
                }
            }
        }
        
        Vector readableVec = new Vector();
        if (allVec.size() > 0) {
            String accessTypes = "('" + DbKmNodeAccess.ACCESS_TYPE_READER + "','" + DbKmNodeAccess.ACCESS_TYPE_AUTHOR + "','" + DbKmNodeAccess.ACCESS_TYPE_OWNER + "')";
            if(prof == null) { 
                readableVec = DbKmNodeAccess.getAccessibleNode(con, allVec, accessTypes, usrGroupsList);
//                readableVec = DbKmNodeAccess.getReadableNode(con, allVec, usrGroupsList);
            } else {
            	 readableVec = allVec;
//                AcKmNode knode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
//                if(knode.hasFunctionPrivilege(prof.usr_ent_id, prof.current_role, AcKmNode.FTN_KB_MGT_ADMIN)){
//                    readableVec = allVec;
//                } else {
//                    readableVec = DbKmNodeAccess.getAccessibleNode(con, allVec, accessTypes, usrGroupsList);
////                    readableVec = DbKmNodeAccess.getReadableNode(con, allVec, usrGroupsList);
//                }
            }
        }

        StringBuffer xml = new StringBuffer(1024);
        xml.append("<node type=\"").append(folder.fld_type).append("\">"); 
        xml.append("<child_node_list>");

        for (int i=0;i<folderVec.size();i++) {
            fld = (DbKmFolder) folderVec.elementAt(i);
            if (readableVec.contains(new Long(fld.fld_nod_id))) {
                xml.append("<child id=\"").append(fld.fld_nod_id)
                .append("\" type=\"").append(fld.nod_type)
                .append("\" order=\"").append(fld.nod_order)
                .append("\" obj_cnt=\"").append(fld.fld_obj_cnt)
                .append("\" target_nod_id=\"").append(fld.target_nod_id)
                .append("\">");
                xml.append("<title>").append(cwUtils.esc4XML(fld.fld_title)).append("</title>");
                if (bShowChild) {
                    xml.append("<child_node_list>");
                    childVec = (Vector) folderHash.get(new Long(fld.fld_nod_id));
                    if (childVec != null) {
                        for (int j=0;j<childVec.size();j++) {
                            fld = (DbKmFolder) childVec.elementAt(j);
                            if (readableVec.contains(new Long(fld.fld_nod_id))) {
                                xml.append("<child id=\"").append(fld.fld_nod_id).append("\" order=\"")
                                .append(fld.nod_order).append("\" type=\"").append(fld.nod_type)
                                .append("\" obj_cnt=\"").append(fld.fld_obj_cnt)
                                .append("\" target_nod_id=\"").append(fld.target_nod_id).append("\">");
                                xml.append("<title>").append(cwUtils.esc4XML(fld.fld_title)).append("</title>");
                                xml.append("</child>");
                            }
                        }
                    }
                    xml.append("</child_node_list>");
                }
                xml.append("</child>");            
            }
        }
        xml.append("</child_node_list>");
        xml.append("</node>");
        
        return xml.toString();
    }
    
    /**
    Get the child folder and objects of a give folder
    @param con Database connection
    @param sess HttpSession
    @param usrGroupsList ancestor group list of the user (included his/her own enity id)
    @param cwPage pagination setting
    @return xml of the folder
    */
    public String folderListAsXML(Connection con, HttpSession sess, String usrGroupsList, cwPagination cwPage) throws SQLException, cwSysMessage {
        return folderListAsXML(con, sess, usrGroupsList, cwPage, null);
    }
    
    public String folderListAsXML(Connection con, HttpSession sess, String usrGroupsList, cwPagination cwPage, loginProfile prof) throws SQLException, cwSysMessage {
        folder.get(con);
        String ancestor_xml = ViewKmFolderManager.getFolderAncestorAsXML(con, folder.fld_nod_id);
        
        DbKmFolder fld = null;
        Vector folderVec = ViewKmFolderManager.getChildFolders(con, folder.fld_nod_id);

        Vector allVec = new Vector();
        for (int i=0;i<folderVec.size();i++) {
            fld = (DbKmFolder) folderVec.elementAt(i);
            allVec.addElement(new Long(fld.target_nod_id));
        }
        
        Vector readableVec = new Vector();
        if (allVec.size() > 0) {
            if(prof == null) { 
                readableVec = DbKmNodeAccess.getReadableNode(con, allVec, usrGroupsList);
            } else {
            	readableVec = allVec;
//                AcKmNode knode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
//                if(knode.hasFunctionPrivilege(prof.usr_ent_id, prof.current_role, AcKmNode.FTN_KB_MGT_ADMIN)){
//                    readableVec = allVec;
//                } else {
//                    readableVec = DbKmNodeAccess.getReadableNode(con, allVec, usrGroupsList);
//                }
            }
        }

        StringBuffer xml = new StringBuffer(1024);
        xml.append("<node id=\"").append(folder.fld_nod_id)
           .append("\" type=\"").append(folder.fld_type)
           .append("\" nature=\"").append(folder.fld_nature)           
           .append("\" obj_cnt=\"").append(folder.fld_obj_cnt)
           .append("\" parent_nod_id=\"").append(folder.nod_parent_nod_id)
           .append("\">");
        xml.append(ancestor_xml);
        xml.append("<title>").append(cwUtils.esc4XML(folder.fld_title)).append("</title>");
        xml.append("<desc>").append(cwUtils.esc4XML(folder.fld_desc)).append("</desc>");
        xml.append("<create usr_id=\"").append(folder.nod_create_usr_id)
           .append("\" timestamp=\"").append(folder.nod_create_timestamp).append("\"/>");
        xml.append("<update usr_id=\"").append(folder.fld_update_usr_id)
           .append("\" timestamp=\"").append(folder.fld_update_timestamp).append("\"/>");

        xml.append("<child type=\"").append(folder.fld_type).append("\">");
        xml.append("<child_node_list>");
        for (int j=0;j<folderVec.size();j++) {
            fld = (DbKmFolder) folderVec.elementAt(j);
            if (readableVec.contains(new Long(fld.target_nod_id))) {
                xml.append("<child id=\"").append(fld.fld_nod_id)
                .append("\" order=\"").append(fld.nod_order)
                .append("\" type=\"").append(fld.nod_type)
                .append("\" obj_cnt=\"").append(fld.fld_obj_cnt)
                .append("\" target_nod_id=\"").append(fld.target_nod_id).append("\">");
                xml.append("<title>").append(cwUtils.esc4XML(fld.fld_title)).append("</title>");
                xml.append("</child>");
            }
        }
        xml.append("</child_node_list>");
        xml.append("</child>");
        xml.append("<child type=\"").append(DbKmNode.NODE_TYPE_OBJECT).append("\">");
        xml.append("<child_node_list>");
        xml.append(getChildAsXML(con, sess, cwPage));
        xml.append("</child_node_list>");
        xml.append("</child>");
        
        xml.append("</node>");
        
        return xml.toString();
        
    }

    /*
    Get the child objects of a folder
    pre-set value
        folder.fld_nod_id
        folder.fld_type
    */
    private String getChildAsXML(Connection con, HttpSession sess, cwPagination cwPage) throws SQLException, cwSysMessage
    {
        Vector childVec = null;
        boolean useSession = false;

        Timestamp sess_time = (Timestamp) sess.getAttribute(KMModule.SESS_KM_CHILD_OBJECTS_TS);
        if (cwPage.ts != null && sess_time != null && sess_time.equals(cwPage.ts)) {
                // use session 
                childVec = (Vector) sess.getAttribute(KMModule.SESS_KM_CHILD_OBJECTS_VEC);
                if (childVec != null && childVec.size() > 0) {
                    useSession = true;
                }
        }
        
        if (!useSession) {
            childVec = ViewKmFolderManager.getChildObjects(con, folder.fld_nod_id, DbKmObject.OBJ_STATUS_DELETE);
            cwPage.ts = cwSQL.getTime(con);
            sess.setAttribute(KMModule.SESS_KM_CHILD_OBJECTS_TS, cwPage.ts);
            sess.setAttribute(KMModule.SESS_KM_CHILD_OBJECTS_VEC, childVec);
        }
        
        cwPage.totalRec  = childVec.size();
        cwPage.totalPage = (int)Math.ceil((float)cwPage.totalRec/(float)cwPage.pageSize);
        int begin=(cwPage.curPage-1)*cwPage.pageSize;
        int end=begin+cwPage.pageSize;
        if(end>childVec.size()) {
            end = childVec.size();
        }
        
        
        Vector realObjectId = new Vector();
        DbKmObject obj = null;
        for(int i=begin; i<end; i++) {
            obj = (DbKmObject) childVec.elementAt(i);
            realObjectId.addElement(new Long(obj.target_nod_id));
        }

        Hashtable objHash = null;
        if (folder.fld_type.equals(DbKmFolder.FOLDER_TYPE_WORK)) {
            objHash =   ViewKmObject.getLatestVersion(con, realObjectId);
        
        }else {
            objHash =   ViewKmObject.getPublishedVersion(con, realObjectId);
        }

        StringBuffer xml = new StringBuffer();
        xml.append(cwPage.asXML());
        ViewKmObject realObj = null;
        for(int i=begin; i<end; i++) {
            obj = (DbKmObject) childVec.elementAt(i);
                
            xml.append("<child id=\"").append(obj.obj_bob_nod_id)
               .append("\" type=\"").append(obj.nod_type)
               .append("\" target_nod_id=\"").append(obj.target_nod_id).append("\">");
            xml.append("<target>");
            realObj = (ViewKmObject) objHash.get(new Long(obj.target_nod_id));
            if (realObj != null) {
                xml.append(objAsXML(realObj, null));
            }
            xml.append("</target>");
            xml.append("</child>");
             
        }
        
        return xml.toString();
    }

    /**
    Format a object to xml
    The object has basic information and attachment information
    */
    public static String objAsXML(ViewKmObject vObj, Vector ancestorXMLVec) throws SQLException 
    {
        DbKmObject obj = vObj.dbObject;
        StringBuffer xml = new StringBuffer();
        xml.append("<node id=\"").append(obj.obj_bob_nod_id).append("\">");
        
        if (ancestorXMLVec != null) {
            xml.append("<ancestor_list>");
            for (int i=0;i<ancestorXMLVec.size();i++) {
                xml.append((String) ancestorXMLVec.elementAt(i));
            }
            xml.append("</ancestor_list>");
        }
        
         xml.append("<version>").append(obj.obj_version).append("</version>")            
            .append("<type>").append(obj.obj_type).append("</type>")
            .append("<nature>").append(obj.obj_nature).append("</nature>")
            .append("<code>").append(obj.obj_code).append("</code>")
            .append("<status>").append(obj.obj_status).append("</status>")
            .append("<title>").append(cwUtils.esc4XML(obj.obj_title)).append("</title>")
            .append("<desc>").append(cwUtils.esc4XML(obj.obj_desc)).append("</desc>")
            .append("<author>").append(cwUtils.esc4XML(obj.obj_author)).append("</author>")
            .append("<last_update_usr_id>").append(cwUtils.esc4XML(obj.obj_update_usr_id)).append("</last_update_usr_id>")
            .append("<last_update_timestamp>").append(obj.obj_update_timestamp).append("</last_update_timestamp>")
            .append("<last_update_user_display_bil>").append(cwUtils.esc4XML(obj.obj_update_usr_display_bil)).append("</last_update_user_display_bil>")
            .append("<call_num>").append(cwUtils.esc4XML(obj.obj_code)).append("</call_num>")
            .append("<file_list>");
            if (vObj.vAttachment != null) {
                for(int i=0; i<vObj.vAttachment.size(); i++) {
                    String filename = ((dbAttachment)vObj.vAttachment.elementAt(i)).att_filename;
                    xml.append("<file name=\"").append(cwUtils.esc4XML(filename)).append("\"/>");
                }
            }
         xml.append("</file_list>")
            .append("</node>");
        
         return xml.toString();
    }
    
    /*
    Check if the vector and the array have the same set of ids
    */
    private static boolean equalNodeAccess(Vector srcVec, long[] target)
    {
        // Prepare data structure for comparison
        if (srcVec == null) {
            srcVec = new Vector();
        }
        
        Vector targetVec = null; 
        if (target == null || target.length == 0) {
            targetVec = new Vector();
        }else {
            targetVec = cwUtils.long2vector(target);
        }
        
        if (srcVec.size() != targetVec.size()) {
            return false;
        }
        
        for (int i=0;i<srcVec.size();i++) {
            Long entID = (Long) srcVec.elementAt(i);
            if (!targetVec.contains(entID)) {
                return false;
            }
        }
        return true;
    }


    /**
    Update the node's access control and all its successor which are inherited by parent.
    */
    public static void updSelfAndInheritedSuccessor(Connection con, long folder_id, String folder_type, long[] reader_ent_lst, long[] author_ent_lst, long[] owner_ent_lst, String usrGroupsList)
        throws SQLException {
            
        // The access contro maybe changed
        boolean bEqual = false;
        Hashtable accessHash = DbKmNodeAccess.getAll(con, folder_id);

        bEqual = equalNodeAccess((Vector) accessHash.get(DbKmNodeAccess.ACCESS_TYPE_READER), reader_ent_lst);
        
        if (bEqual && folder_type.equals(DbKmFolder.FOLDER_TYPE_WORK)) {
            bEqual = equalNodeAccess((Vector) accessHash.get(DbKmNodeAccess.ACCESS_TYPE_AUTHOR), author_ent_lst);
        }
        if (bEqual) {
            bEqual = equalNodeAccess((Vector) accessHash.get(DbKmNodeAccess.ACCESS_TYPE_OWNER), owner_ent_lst);
        }
    
        // Modify only if acl is changed.
        if (!bEqual) {
            DbKmNodeAccess.delByNode(con, folder_id);
            if (reader_ent_lst !=null) {
                DbKmNodeAccess.insByType(con, folder_id, DbKmNodeAccess.ACCESS_TYPE_READER, reader_ent_lst);
            }
            DbKmNodeAccess.insByType(con, folder_id, DbKmNodeAccess.ACCESS_TYPE_OWNER, owner_ent_lst);

            if (folder_type.equals(DbKmFolder.FOLDER_TYPE_WORK) && author_ent_lst !=null) {
                DbKmNodeAccess.insByType(con, folder_id, DbKmNodeAccess.ACCESS_TYPE_AUTHOR, author_ent_lst);
            }    
        }
        
        Vector childVec = ViewKmFolderManager.getNonLinkChildFolders(con, folder_id);
        Vector allVec = new Vector();
        DbKmFolder fld = null; 
        
        for (int i=0;i<childVec.size();i++) {
            fld = (DbKmFolder) childVec.elementAt(i);
            allVec.addElement(new Long(fld.fld_nod_id));
        }
        
        Vector writeableVec = new Vector();
        if (allVec.size() > 0) {
            writeableVec = DbKmNodeAccess.getEditableNode(con, allVec, usrGroupsList);
        }

        for (int i=0;i<childVec.size();i++) {
            fld = (DbKmFolder) childVec.elementAt(i);
            if (fld.nod_acl_inherit_ind && writeableVec.contains(new Long(fld.fld_nod_id))) {
                updSelfAndInheritedSuccessor (con, fld.fld_nod_id, fld.fld_type, reader_ent_lst, author_ent_lst, owner_ent_lst, usrGroupsList);
            }
        }
    }
    
    
    public static String getUserOwnedDomain(Connection con, long usr_ent_id)
        throws SQLException, cwException {
            
            String groupList = null;
            try{
                groupList = cwUtils.vector2list( dbUserGroup.traceParentID(con, usr_ent_id) );
            }catch(qdbException e){
                throw new cwException(e.getMessage());
            }
            StringBuffer xml = new StringBuffer();
            ResultSet rs = ViewKmFolderManager.getUserOwnedDomain(con, usr_ent_id, groupList);
            xml.append("<domain_node_list>");
            Long nodId;
            Vector v_nodId = new Vector();
            while(rs.next()){
                nodId = new Long(rs.getLong("fld_nod_id"));
                if( v_nodId.indexOf(nodId) == - 1 ) {
                    xml.append("<node id=\"").append(nodId).append("\">")
                    .append(ViewKmFolderManager.getFolderAncestorAsXML(con, nodId.longValue()))
                    .append("<title>").append(cwUtils.esc4XML(rs.getString("fld_title"))).append("</title>")
                    .append("</node>");
                    v_nodId.addElement(nodId);
                }
            }
            xml.append("</domain_node_list>");
            rs.close();
            return xml.toString();
            
        }
    
    public DbKmFolder getFolder(){
        
        return folder;
        
    }
}
