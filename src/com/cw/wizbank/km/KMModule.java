package com.cw.wizbank.km;

import java.io.*;
import java.sql.*;

import javax.servlet.http.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.accesscontrol.AcKmNode;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AcXslQuestion;
import com.cw.wizbank.*;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.*;
import com.cwn.wizbank.utils.CommonLog;

public class KMModule extends ServletModule {
    
    public static final String MODULENAME = "km";
    public static final String NOTIFICATION_CMD                 = "subscription_notify_xml";

    public static final String SMSG_KM_NO_READ_PERMISSIOIN      = "KMM001";
    public static final String SMSG_KM_NO_UPDATE_PERMISSIOIN    = "KMM002";
    public static final String SMSG_KM_INS_SUBSCRIPTION_SUCCESS = "KMM003";
    public static final String SMSG_KM_SUBSCRIPTION_EXISTS      = "KMM004";
    public static final String SMSG_KM_CHECK_IN_OBJ_OK          = "KMM005";
    public static final String SMSG_KM_ERR_CHECKED_OUT          = "KMM006";
    public static final String SMSG_KM_ERR_NOT_CHECKED_OUT      = "KMM007";
    public static final String SMSG_KM_NODE_ASSIGN_SUCCESS      = "KMM008";
    public static final String SMSG_KM_NODE_SELFADD_SUCCESS     = "KMM009";
    
    
    public static final String SMSG_KM_INCORRECT_TIMESTAMP      = "GEN006";
    
    public static final String SESS_KM_HEADER_XML               = "km_header_xml";
    
    public static final String SESS_KM_CHILD_OBJECTS_TS         = "km_child_objects_ts";
    public static final String SESS_KM_CHILD_OBJECTS_VEC        = "km_child_objects_vec";
    public static final String SESS_KM_SEARCH_RESULT_TS         = "km_search_result_ts";
    public static final String SESS_KM_SEARCH_RESULT_VEC        = "km_search_result_vec";

    
    public void process() throws SQLException, IOException, cwException {

        HttpSession sess = request.getSession(false);

        KMReqParam urlp = null;

        urlp = new KMReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        
        PrintWriter out = response.getWriter();

        try {
            if (urlp.cmd.equalsIgnoreCase(NOTIFICATION_CMD)) {
            	CommonLog.info(NOTIFICATION_CMD + " >>>");
                try {
                    urlp.getNotify();
                    String xml = KMNotify.getNotifyXML(con, urlp.sub.nsb_usr_ent_id, urlp.sub.nsb_email_send_type, 
                            urlp.email_send_timestamp, urlp.action_id, urlp.sender_usr_id, static_env.MAIL_ACCOUNT, static_env.DES_KEY);
                    //  Updated the email send time in the kmNodeSubscription table
                    con.commit();
                    out.println(xml.trim());
                }catch(Exception e) {
                	CommonLog.error(e.getMessage());
            		out.println("FAILED");
            	}            		
            	return;

            }else if (prof == null) {

                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);

            }else if (urlp.cmd.equalsIgnoreCase("GET_PROF") || 
                      urlp.cmd.equalsIgnoreCase("GET_PROF_XML")) {

                String xml = formatXML(new String(), MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_PROF")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if (urlp.cmd.equalsIgnoreCase("INS_FOLDER")) {
                urlp.getFolder();
                urlp.folder.nod_owner_ent_id = prof.root_ent_id;
                
               
                
                KMFolderManager fldmgr = new KMFolderManager(urlp.folder, prof.usr_id, prof.usr_display_bil);
                fldmgr.reader_ent_lst = urlp.reader_ent_lst;
                fldmgr.author_ent_lst = urlp.author_ent_lst;
                fldmgr.owner_ent_lst = urlp.owner_ent_lst;
                
                fldmgr.insFolder(con, prof.usr_ent_id);
                
                con.commit();

                response.sendRedirect(urlp.url_success);
                
            }else if (urlp.cmd.equalsIgnoreCase("UPD_FOLDER")) {
                urlp.getFolder();

                
                KMFolderManager fldmgr = new KMFolderManager(urlp.folder, prof.usr_id, prof.usr_display_bil);
                fldmgr.reader_ent_lst = urlp.reader_ent_lst;
                fldmgr.author_ent_lst = urlp.author_ent_lst;
                fldmgr.owner_ent_lst = urlp.owner_ent_lst;

                fldmgr.updFolder(con, prof.usr_ent_id, prof.usrGroupsList());
                
                con.commit();

                response.sendRedirect(urlp.url_success);
                
            }else if (urlp.cmd.equalsIgnoreCase("DEL_FOLDER")) {
                urlp.getFolder();
                
                         
                KMFolderManager fldmgr = new KMFolderManager(urlp.folder, prof.usr_id, prof.usr_display_bil);
                fldmgr.delFolder(con);
                
                con.commit();

                response.sendRedirect(urlp.url_success);
            }else if (urlp.cmd.equalsIgnoreCase("GET_FOLDER_MAIN") || 
                      urlp.cmd.equalsIgnoreCase("GET_FOLDER_MAIN_XML")) {
                urlp.getFolderBasic();
                /* page variant BEGIN */
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                acPageVariant.prof = prof;
                acPageVariant.ent_id = prof.usr_ent_id;
                acPageVariant.rol_ext_id = prof.current_role;
                String metaXML = acPageVariant.answerPageVariantAsXML(AcXslQuestion.getOneXslQuestions(urlp.stylesheet, xslQuestions));
                /* page variant END */
                KMFolderManager fldmgr = new KMFolderManager(urlp.folder, prof.usr_id, prof.usr_display_bil);
                //String xml = formatXML(fldmgr.folderMainAsXML(con, prof.root_ent_id, prof.usrGroupsList(), true), metaXML, MODULENAME);
                String xml = formatXML(fldmgr.folderMainAsXML(con, prof.root_ent_id, prof.usrGroupsList(), true, prof), metaXML, MODULENAME, urlp.stylesheet, urlp.folder.fld_nature);
                if (urlp.cmd.equalsIgnoreCase("GET_FOLDER_MAIN")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if (urlp.cmd.equalsIgnoreCase("GET_FOLDER_LST") || 
                      urlp.cmd.equalsIgnoreCase("GET_FOLDER_LST_XML")) {
                
                urlp.getFolderBasic();
                urlp.pagination();

          
                /* page variant BEGIN */
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                acPageVariant.prof = prof;
                acPageVariant.ent_id = prof.usr_ent_id;
                acPageVariant.rol_ext_id = prof.current_role;
                acPageVariant.instance_id = urlp.folder.fld_nod_id;
                String metaXML = acPageVariant.answerPageVariantAsXML(AcXslQuestion.getOneXslQuestions(urlp.stylesheet, xslQuestions));
                /* page variant END */

                KMFolderManager fldmgr = new KMFolderManager(urlp.folder, prof.usr_id, prof.usr_display_bil);
                String xml = formatXML(fldmgr.folderListAsXML(con, sess, prof.usrGroupsList(), urlp.cwPage, prof), metaXML, MODULENAME, urlp.stylesheet);

                if (urlp.cmd.equalsIgnoreCase("GET_FOLDER_LST")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if (urlp.cmd.equalsIgnoreCase("GET_FOLDER") || 
                      urlp.cmd.equalsIgnoreCase("GET_FOLDER_XML")) {
                urlp.getFolderBasic();

             
                
                KMFolderManager fldmgr = new KMFolderManager(urlp.folder, prof.usr_id, prof.usr_display_bil);
                String xml = formatXML(fldmgr.folderAsXML(con), null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("GET_FOLDER")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if (urlp.cmd.equalsIgnoreCase("INS_SUB")) {
                urlp.getSubscription();

                KMSubscriptionManager.insSub(con, urlp.sub, prof.usr_ent_id);

                con.commit();

                cwSysMessage msg = new cwSysMessage(SMSG_KM_INS_SUBSCRIPTION_SUCCESS);
                msgBox(ServletModule.MSG_STATUS,  msg, urlp.url_success, out);

                response.sendRedirect(urlp.url_success);

            }else if (urlp.cmd.equalsIgnoreCase("CLEAR_SUB")) {
                urlp.getNodeList();

                KMSubscriptionManager.clearSub(con, urlp.nod_id_lst, prof.usr_ent_id);

                con.commit();
                response.sendRedirect(urlp.url_success);

            }else if (urlp.cmd.equalsIgnoreCase("DEL_SUB")) {
                urlp.getNodeList();

                KMSubscriptionManager.delSub(con, urlp.nod_id_lst, prof.usr_ent_id);

                con.commit();
                response.sendRedirect(urlp.url_success);

            }else if (urlp.cmd.equalsIgnoreCase("GET_SUB_LST") || 
                      urlp.cmd.equalsIgnoreCase("GET_SUB_LST_XML")) {
                
                String xml = KMSubscriptionManager.getSubscriptionAsXML(con, prof.usr_ent_id);
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("GET_SUB_LST")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if (urlp.cmd.equalsIgnoreCase("PREP_INS_OBJ") || 
                      urlp.cmd.equalsIgnoreCase("PREP_INS_OBJ_XML")) {
                urlp.prepInsObject();

            
                
                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                String xml = formatXML(objectManager.getInsertXML(con, static_env, 
                                                                  urlp.obj_type, 
                                                                  urlp.tvw_id, 
                                                                  urlp.parent_nod_id,
                                                                  urlp.nature)
                                                                  , null, MODULENAME, urlp.stylesheet);
                


                
                if (urlp.cmd.equalsIgnoreCase("PREP_INS_OBJ")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if (urlp.cmd.equalsIgnoreCase("INS_OBJ")) {
                urlp.insObject();

                //access control
                int index = urlp.vNodColName.indexOf("nod_parent_nod_id");
                long parent_nod_id = ((Long)urlp.vNodColValue.elementAt(index)).longValue();
               
                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);

                
                ViewKmObject object = objectManager.insObject(con, prof,
                                        urlp.vNodColName, urlp.vNodColType, urlp.vNodColValue,
                                        urlp.vObjColName, urlp.vObjColType, urlp.vObjColValue,
                                        urlp.vObjClobColName, urlp.vObjClobColValue, 
                                        urlp.vFileName, urlp.bob_code); 
                 
                if(bMultipart) {
                    procUploadedFiles(object.dbObject.obj_bob_nod_id, 
                                      object.dbObject.obj_version, 
                                      tmpUploadPath);
                }
                con.commit();
                cwSysMessage sms = new cwSysMessage(SMSG_KM_CHECK_IN_OBJ_OK);
                msgBox(ServletModule.MSG_STATUS, sms, urlp.url_success, out);

            }else if (urlp.cmd.equalsIgnoreCase("MOVE_OBJ")) {
                urlp.getObject();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());

   
                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);

                objectManager.moveObject(con, urlp.obj_bob_nod_id, urlp.parent_nod_id); 

                con.commit();
                response.sendRedirect(urlp.url_success);  
            }else if (urlp.cmd.equalsIgnoreCase("GET_OBJ") || 
                      urlp.cmd.equalsIgnoreCase("GET_OBJ_XML")) {
                urlp.getObject();

               
                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                String xml = objectManager.getObjectVersionAsXML(con, prof, static_env,
                                                                 urlp.obj_bob_nod_id, 
                                                                 urlp.obj_version,
                                                                 urlp.obj_type, 
                                                                 urlp.tvw_id,
                                                                 urlp.parent_nod_id);                                                           
                /* page variant BEGIN */
                DbKmBaseObject baseObj = new DbKmBaseObject();
                baseObj.bob_nod_id = urlp.obj_bob_nod_id;
                baseObj.get(con);
                
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                acPageVariant.prof = prof;
                acPageVariant.ent_id = prof.usr_ent_id;
                acPageVariant.instance_id = urlp.obj_bob_nod_id;
                acPageVariant.rol_ext_id = prof.current_role;
                acPageVariant.setBaseObject(baseObj);
                
                String metaXML = acPageVariant.answerPageVariantAsXML(AcXslQuestion.getOneXslQuestions(urlp.stylesheet, xslQuestions));
                /* page variant END */

                xml = formatXML(xml, metaXML, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("GET_OBJ")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if (urlp.cmd.equalsIgnoreCase("CHECK_OUT_OBJ")) {
                urlp.checkOutObject();

                //no access control
                
                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                objectManager.checkOutObject(con, prof, urlp.obj_bob_nod_id, 
                                             urlp.obj_update_timestamp);

                con.commit();
                response.sendRedirect(urlp.url_success);                
            
            }else if (urlp.cmd.equalsIgnoreCase("CHECK_IN_OBJ")) {
                urlp.checkInObject();

               
                    
                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                ViewKmObject newVersion = 
                    objectManager.checkInObject(con, static_env, prof, tmpUploadPath,
                                                urlp.obj_bob_nod_id, urlp.obj_update_timestamp,
                                                urlp.vObjColName, urlp.vObjColType, urlp.vObjColValue,
                                                urlp.vObjClobColName, urlp.vObjClobColValue, 
                                                urlp.vFileName, urlp.bob_code);
                    
                    if( urlp.keepCheckOut ) 
                        objectManager.keepCheckOutObject( con, prof, urlp.obj_bob_nod_id);
                con.commit();
                cwSysMessage sms = new cwSysMessage(SMSG_KM_CHECK_IN_OBJ_OK);
                msgBox(ServletModule.MSG_STATUS, sms, urlp.url_success, out);
            }else if (urlp.cmd.equalsIgnoreCase("PREP_PUBLISH_OBJ") || 
                      urlp.cmd.equalsIgnoreCase("PREP_PUBLISH_OBJ_XML")) {
                urlp.prepPublish();

                
                
                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                String xml = objectManager.getPublishXML(con, urlp.obj_bob_nod_id);  
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("PREP_PUBLISH_OBJ")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("PUBLISH_OBJ")) {
                urlp.publishObject();

               
                
                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                KMObjectManager.synPublishObject(con, static_env, prof, objectManager,
                                                 urlp.obj_update_timestamp, 
                                                 urlp.obj_bob_nod_id, urlp.domain_id_list);
                    
                con.commit();
                response.sendRedirect(urlp.url_success);
            
            }else if (urlp.cmd.equalsIgnoreCase("GET_OBJ_ATT") || 
                      urlp.cmd.equalsIgnoreCase("GET_OBJ_ATT_XML")) {
                urlp.getObjectAtt();

             

                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                String xml = objectManager.getObjectVersionAttAsXML(con, prof,
                                                                    urlp.obj_bob_nod_id,
                                                                    urlp.obj_version,
                                                                    urlp.parent_nod_id);

                
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("GET_OBJ_ATT")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if (urlp.cmd.equalsIgnoreCase("GET_OBJ_HIST") || 
                      urlp.cmd.equalsIgnoreCase("GET_OBJ_HIST_XML")) {
                urlp.getObjectHist();

               

                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                String xml = objectManager.getObjectVersionHistAsXML(con, 
                                                                     urlp.obj_bob_nod_id);
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("GET_OBJ_HIST")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if (urlp.cmd.equalsIgnoreCase("DEL_OBJ")) {
                urlp.delObject();

               

                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                objectManager.del(con, static_env, urlp.obj_bob_nod_id, urlp.obj_update_timestamp);
                
                String dir = static_env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + urlp.obj_bob_nod_id;
                dbUtils.delDir(dir);
                con.commit();
                response.sendRedirect(urlp.url_success);
            }else if (urlp.cmd.equalsIgnoreCase("MARK_DEL_OBJ")) {
                urlp.delObject();

              

                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                objectManager.markDel(con, static_env, urlp.obj_bob_nod_id, urlp.obj_update_timestamp, prof.usr_id);
                
/*                String dir = static_env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + urlp.obj_bob_nod_id;
                try {
                    dbUtils.delDir(dir);
                } catch(qdbException e) {
                    throw new cwException (e.getMessage());
                }*/
                con.commit();
                response.sendRedirect(urlp.url_success);
                
            
            }else if (urlp.cmd.equalsIgnoreCase("SIMPLE_SEARCH") || 
                      urlp.cmd.equalsIgnoreCase("SIMPLE_SEARCH_XML")) {
                
                urlp.pagination();
                urlp.simpleSearch();
                String xml = KMObjectManager.getSearchResultAsXML(con, sess, prof.usrGroupsList(), urlp.obj_type, urlp.nod_id, urlp.words, urlp.cwPage, static_env.SEARCH_DB_DIR,urlp.show_deleted);

                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("SIMPLE_SEARCH")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            } else if ( urlp.cmd.equalsIgnoreCase("ADV_SEARCH") ||
                        urlp.cmd.equalsIgnoreCase("ADV_SEARCH_XML") ) {
                
                urlp.pagination();
                urlp.advSearch();
                String xml = KMObjectManager.getAdvSearchResultAsXML(con, sess, prof.usrGroupsList(), urlp.obj_title, urlp.obj_author, urlp.obj_type_list, urlp.words, urlp.call_num, urlp.nod_id_lst, urlp.cwPage, static_env.SEARCH_DB_DIR, urlp.show_deleted);
                
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                if( urlp.cmd.equalsIgnoreCase("ADV_SEARCH") ) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                } else {
                    static_env.outputXML(out,xml);
                }
                return;
            }else if (urlp.cmd.equalsIgnoreCase("RELOAD_OBJ_TYPE")) {
                
                KMObjectManager.loadObjectTypes(con, true);
                response.sendRedirect(static_env.URL_RELOGIN);
            
            }else if(urlp.cmd.equalsIgnoreCase("add_folder_to_workplace")){
                
                urlp.addToMyWorkplace();
                
                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasReadPrivilege(urlp.dbNodeAss.nam_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_READ_PERMISSIOIN);
                }
                
                KMNodeAssignment nodeAss = new  KMNodeAssignment();
                nodeAss.addToMyWorkplace(con, prof, urlp.dbNodeAss);
                con.commit();
                response.sendRedirect(urlp.url_success);
                /*
                cwSysMessage sms = new cwSysMessage(SMSG_KM_NODE_SELFADD_SUCCESS);
                msgBox(ServletModule.MSG_STATUS, sms, urlp.url_success, out);
                */
            }else if(urlp.cmd.equalsIgnoreCase("remove_folder_from_workplace")){
                
                urlp.removeFromMyWorkplace();
               
                KMNodeAssignment nodeAss = new  KMNodeAssignment();
                nodeAss.removeFromMyWorkplace(con, prof, urlp.dbNodeAss);
                con.commit();
                response.sendRedirect(urlp.url_success);
                
            }else if(urlp.cmd.equalsIgnoreCase("assign_folder_workplace")) {
                
                urlp.assignWorkplace();
                
                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasMgtPrivilege(urlp.dbNodeAss.nam_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }
                
                KMNodeAssignment nodeAss = new  KMNodeAssignment();
                nodeAss.assignWorkplace(con, prof, urlp.dbNodeAss, urlp.assign_ent_id);
                con.commit();
                cwSysMessage sms = new cwSysMessage(SMSG_KM_NODE_SELFADD_SUCCESS);
                msgBox(ServletModule.MSG_STATUS, sms, urlp.url_success, out);
            
            }else if(urlp.cmd.equalsIgnoreCase("add_obj_to_workplace")){
                
                urlp.addToMyWorkplace();
                
                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjReadPrivilege(urlp.dbNodeAss.nam_nod_id, urlp.parent_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_READ_PERMISSIOIN);
                }
                
                KMNodeAssignment nodeAss = new  KMNodeAssignment();
                nodeAss.addToMyWorkplace(con, prof, urlp.dbNodeAss);
                con.commit();
                cwSysMessage sms = new cwSysMessage(SMSG_KM_NODE_SELFADD_SUCCESS);
                msgBox(ServletModule.MSG_STATUS, sms, urlp.url_success, out);
            
            }else if(urlp.cmd.equalsIgnoreCase("assign_obj_workplace")) {
                
                urlp.assignWorkplace();
                
                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjMgtPrivilege(urlp.dbNodeAss.nam_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }
                
                KMNodeAssignment nodeAss = new  KMNodeAssignment();
                nodeAss.assignWorkplace(con, prof, urlp.dbNodeAss, urlp.assign_ent_id);
                con.commit();
                cwSysMessage sms = new cwSysMessage(SMSG_KM_NODE_SELFADD_SUCCESS);
                msgBox(ServletModule.MSG_STATUS, sms, urlp.url_success, out);
            
            }else if(urlp.cmd.equalsIgnoreCase("get_folder_assigned_workplace") ||
                     urlp.cmd.equalsIgnoreCase("get_folder_assigned_workplace_xml")){
                
                urlp.getFolderAssignedWorkplace();
                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasReadPrivilege(urlp.nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_READ_PERMISSIOIN);
                }
                KMNodeAssignment assNode = new KMNodeAssignment();
                String xml = assNode.getFolderAssignedWorkplaceAsXML(con, urlp.nod_id);
                
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("get_folder_assigned_workplace")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
                
            }else if(urlp.cmd.equalsIgnoreCase("get_obj_assigned_workplace") ||
                     urlp.cmd.equalsIgnoreCase("get_obj_assigned_workplace_xml")){

                urlp.getObjAssignedWorkplace();
                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjReadPrivilege(urlp.nod_id, urlp.parent_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_READ_PERMISSIOIN);
                }                
                KMNodeAssignment assNode = new KMNodeAssignment();
                String xml = assNode.getObjAssignedWorkplaceAsXML(con, urlp.nod_id);
                
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("get_obj_assigned_workplace")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if(urlp.cmd.equalsIgnoreCase("get_my_workplace") ||
                     urlp.cmd.equalsIgnoreCase("get_my_workplace_xml")){

                KMNodeAssignment assNode = new KMNodeAssignment();
                String xml = assNode.getMyWorkplace(con, prof);
                
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("get_my_workplace")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else if(urlp.cmd.equalsIgnoreCase("get_usr") ||
                     urlp.cmd.equalsIgnoreCase("get_usr_xml")){

                urlp.getUser();
                KMNodeAssignment assNode = new KMNodeAssignment();
                
                String xml = null;                
                if( urlp.usr_ent_id > 0 )
                    xml = assNode.getUserAsXMLByEntId(con, urlp.usr_ent_id, prof);
                else if( urlp.usr_id != null && urlp.usr_id.length() > 0 ) 
                    xml = assNode.getUserAsXMLByUsrId(con, urlp.usr_id, prof);
                else
                    xml = new String();
                
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("get_usr")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            
            }else {
                throw new cwSysMessage("GEN000");
            }

        }catch (cwSysMessage se) {
            try {
            	CommonLog.error("cwSysMessage : " + se.getSystemMessage(prof.label_lan));
                 con.rollback();
                 msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
             } catch (SQLException sqle) {
            	 CommonLog.error("SQL error: " + sqle.getMessage());
             }
        }
    }
 
        
    /**
    Include a km specific open-end tag and user profile xml with the input data XML
    */
    public String formatXML(String in, String moduleName) throws IOException, SQLException {
        StringBuffer outBuf = new StringBuffer(2500);
    
        String metaXML = new String();
        return formatXML(in, metaXML, moduleName, null);
    }

    /**
    Include a km specific xml for the search 
    @param dataXML input data XML
    @param metaXML meta data XML
    @param moduleName start, end root tag (e.g. "home_page")
    @return an XML contain <cur_usr> and the input data XML
    */
    public String formatXML(String dataXML, String metaXML, String moduleName, String stylesheet) throws IOException, SQLException{
        return formatXML(dataXML, metaXML, moduleName, stylesheet, null);
    }
    public String formatXML(String dataXML, String metaXML, String moduleName, String stylesheet, String nature) throws IOException, SQLException{
        StringBuffer outBuf = new StringBuffer(2500);

        HttpSession sess = request.getSession(false);
        StringBuffer headerXML = null;//(StringBuffer) sess.getAttribute(SESS_KM_HEADER_XML);
        if (headerXML == null) {
            headerXML = new StringBuffer();
            try {
                KMObjectManager objectManager = new KMObjectManager(con, prof.root_ent_id);
                headerXML.append(objectManager.getObjectTypeXML(nature));
                headerXML.append("<domain>"); 
                DbKmFolder folder = new DbKmFolder();
                folder.fld_type = DbKmFolder.FOLDER_TYPE_DOMAIN;
                KMFolderManager fldmgr = new KMFolderManager(folder, prof.usr_id, prof.usr_display_bil);
                headerXML.append(fldmgr.folderMainAsXML(con, prof.root_ent_id, prof.usrGroupsList(), false));
                headerXML.append("</domain>");
            }catch (Exception e) {
            	CommonLog.error("Header xml exception : " + e);
            }
            sess.setAttribute(SESS_KM_HEADER_XML, headerXML);
        }
        if( metaXML == null )
            metaXML = headerXML.toString();
        else
            metaXML += headerXML.toString();
        
        String finalXML = super.formatXML(dataXML, metaXML, moduleName, stylesheet);
        return finalXML;
    }

    public void procUploadedFiles(long objId, String version, String tmpSaveDirPath)
        throws cwException
    {
        String saveDirPath = static_env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + objId + dbUtils.SLASH + version;
        try {
            dbUtils.moveDir(tmpSaveDirPath, saveDirPath);
        } catch(qdbException e) {
            throw new cwException(e.getMessage());
        }
    }
    

    
}


