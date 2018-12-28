package com.cw.wizbank.km.library;

import java.io.*;
import java.sql.*;
import java.util.*;

import javax.servlet.http.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.accesscontrol.AcKmLibrary;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AcXslQuestion;
import com.cw.wizbank.accesscontrol.AcKmNode;
import com.cw.wizbank.*;
import com.cw.wizbank.db.*;
import com.cw.wizbank.db.view.*;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.km.KMModule;
import com.cw.wizbank.km.library.KMLibraryObjectManager;
import com.cwn.wizbank.utils.CommonLog;

public class KMLibraryModule extends KMModule {
    public static final String SESS_KM_LIB_CHILD_OBJECTS_TS = "SESS_KM_LIB_CHILD_OBJECTS_TS";
    public static final String SESS_KM_LIB_CHILD_OBJECTS_VEC = "SESS_KM_LIB_CHILD_OBJECTS_VEC";
    public static final String SESS_KM_SEARCH_ITEM_USER_STATUS = "SESS_KM_SEARCH_ITEM_USER_STATUS";
    public static final String SESS_KM_SEARCH_ITEM_STATUS = "SESS_KM_SEARCH_ITEM_STATUS";
    
    public static final String MODULENAME = "library";
    public void process() throws SQLException, IOException, cwException {

        HttpSession sess = request.getSession(false);

        KMLibraryReqParam urlp = null;

        urlp = new KMLibraryReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        
        PrintWriter out = response.getWriter();

        try {
            if (prof == null) {
            	if( urlp.cmd.equalsIgnoreCase("reserve_notify_xml") ){
            		urlp.reserveNotify();
            		
            		try{
            			String xml = KMLibraryObjectManager.getReserveNotifyXML(con, urlp.site_id, urlp.senderUsrId, urlp.usrEntId, urlp.lobId, static_env.MAIL_ACCOUNT, static_env.DES_KEY);
            			out.println(xml.trim());
            		}catch(Exception e) {
            			CommonLog.error(e.getMessage(),e);
            			out.println("FAILED");
            		}            		
            		return;
            	} else if( urlp.cmd.equalsIgnoreCase("borrow_notify_xml") ){
            		urlp.borrowNotify();
            		
            		try{
            			String xml = KMLibraryObjectManager.getBorrowNotifyXML(con, urlp.site_id, urlp.senderUsrId, urlp.usrEntId, urlp.lobId, static_env.MAIL_ACCOUNT, static_env.DES_KEY);
            			out.println(xml.trim());
            		}catch(Exception e) {
            			CommonLog.error(e.getMessage(),e);
            			out.println("FAILED");
            		}            		
            		return;
            	} else if( urlp.cmd.equalsIgnoreCase("checkout_notify_xml") ){
            		urlp.checkoutNotify();
            		
            		try{
            			String xml = KMLibraryObjectManager.getCheckoutNotifyXML(con, urlp.site_id, urlp.senderUsrId, urlp.usrEntId, urlp.lobId, static_env.MAIL_ACCOUNT, static_env.DES_KEY);
            			out.println(xml.trim());
            		}catch(Exception e) {
            			CommonLog.error(e.getMessage(),e);
            			out.println("FAILED");
            		}            		
            		return;
            	} else {
                	response.sendRedirect(static_env.URL_RELOGIN);
                }
            }else if (urlp.cmd.equalsIgnoreCase("GET_PROF") || 
                      urlp.cmd.equalsIgnoreCase("GET_PROF_XML")) {

                String xml = formatXML(new String(), MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_PROF")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("CHECK_IN_PREP") ||
                      urlp.cmd.equalsIgnoreCase("CHECK_IN_PREP_XML")) {
                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasCheckinPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_CHECKIN_PERMISSION);
                }
                        
                String xml = formatXML("", MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("CHECK_IN_PREP")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("CHECK_IN_LIST") ||
                      urlp.cmd.equalsIgnoreCase("CHECK_IN_LIST_XML")) {
                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasCheckinPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_CHECKIN_PERMISSION);
                }
                        
                urlp.checkin();
                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                try {
                    kmLibObjMgr.validateCheckInItem(con, urlp.callNum, urlp.copyNum);
                    String xml = kmLibObjMgr.getCheckInListXML(con, urlp.callNum, urlp.copyNum);
                            
                    //xml = formatXML(xml, MODULENAME);
                    xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                    if (urlp.cmd.equalsIgnoreCase("CHECK_IN_LIST")) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    }else {
                        static_env.outputXML(out, xml);
                    }
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
            }else if (urlp.cmd.equalsIgnoreCase("CHECK_IN_EXEC") ||
                      urlp.cmd.equalsIgnoreCase("CHECK_IN_EXEC_XML")) {
                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasCheckinPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_CHECKIN_PERMISSION);
                }
                        
                urlp.checkin();
                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                try {
                    kmLibObjMgr.validateCheckInItem(con, urlp.callNum, urlp.copyNum);
                    String xml = kmLibObjMgr.checkin(con, prof, static_env, prof.usr_id, urlp.callNum, urlp.copyNum);
                    con.commit();
                    if(xml.length() > 0) {
                        xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                        if (urlp.cmd.equalsIgnoreCase("CHECK_IN_EXEC")) {
                            generalAsHtml(xml, out, urlp.stylesheet);
                        }else {
                            static_env.outputXML(out, xml);
                        }
                    } else {
                        response.sendRedirect(urlp.url_success);
                    }
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
            }else if (urlp.cmd.equalsIgnoreCase("CHECK_OUT_LIST") ||
                      urlp.cmd.equalsIgnoreCase("CHECK_OUT_LIST_XML")) {
                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasCheckoutPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_CHECKOUT_PERMISSION);
                }
                        
                urlp.checkout();
                urlp.pagination();
                sess = request.getSession(true);

                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = kmLibObjMgr.getCheckOutListXML(con, prof, static_env, sess, urlp.cwPage);
                                
                //xml = formatXML(xml, MODULENAME);
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                if (urlp.cmd.equalsIgnoreCase("CHECK_OUT_LIST")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("CHECK_OUT_DETAILS") ||
                      urlp.cmd.equalsIgnoreCase("CHECK_OUT_DETAILS_XML")) {
                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasCheckoutPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_CHECKOUT_PERMISSION);
                }
                        
                urlp.checkout();
                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = kmLibObjMgr.getCheckOutDetailsXML(con, prof, static_env, urlp.callId);
                String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);            
                                            
                //xml = formatXML(xml, MODULENAME);
                xml = formatXML(xml, metaXML, MODULENAME, urlp.stylesheet);
                if (urlp.cmd.equalsIgnoreCase("CHECK_OUT_DETAILS")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("CHECK_OUT_EXEC") ||
                      urlp.cmd.equalsIgnoreCase("CHECK_OUT_EXEC_XML")) {
                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasCheckoutPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_CHECKOUT_PERMISSION);
                }
                        
                urlp.checkout();
                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                kmLibObjMgr.validateCheckoutUser(con, static_env, urlp.usrEntId);
                long lobId = kmLibObjMgr.checkout(con, static_env, prof.usr_id, urlp.usrEntId, urlp.callId, urlp.copyId);
                
                if(urlp.recEntId != -1) {
                    kmLibObjMgr.checkoutNotify(con, prof, urlp.mesgSubject, urlp.senderUsrId, urlp.recEntId, urlp.ccEntIds, urlp.bccEntIds, lobId);
                }            
                con.commit();
                
                cwSysMessage sms = new cwSysMessage(KMLibraryObjectManager.SMSG_ITEM_CHECKOUT);
                msgBox(ServletModule.MSG_STATUS, sms, urlp.url_success, out);
                return;
                //xml = formatXML(xml, MODULENAME);
                /*
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                if (urlp.cmd.equalsIgnoreCase("CHECK_OUT_EXEC")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
                */
            }else if (urlp.cmd.equalsIgnoreCase("OBJ_COPY_LST") || 
                      urlp.cmd.equalsIgnoreCase("OBJ_COPY_LST_XML")) {
                urlp.getObject();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjReadPrivilege(urlp.obj_bob_nod_id, 0)) {
                    throw new cwSysMessage(KMModule.SMSG_KM_NO_READ_PERMISSIOIN);
                }

                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = objectManager.getObjWithCopiesXML(con, urlp.obj_bob_nod_id, urlp.loc_id, prof.root_ent_id);  
                //xml = formatXML(xml, MODULENAME);
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("OBJ_COPY_LST")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("PREP_INS_OBJ_COPY") || 
                      urlp.cmd.equalsIgnoreCase("PREP_INS_OBJ_COPY_XML")) {
                urlp.getObject();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjMgtPrivilege(urlp.obj_bob_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }

                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = objectManager.getObjXML(con, urlp.obj_bob_nod_id, prof.root_ent_id);  
                //xml = formatXML(xml, MODULENAME);
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("PREP_INS_OBJ_COPY")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("INS_OBJ_COPY")) {
                urlp.insObjectCopy();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjMgtPrivilege(urlp.obj_bob_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }
                
                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                objectManager.insObjectCopy(con, prof, urlp.obj_bob_nod_id, urlp.loc_copy, urlp.loc_desc);  

                con.commit();
                response.sendRedirect(urlp.url_success);
            }else if (urlp.cmd.equalsIgnoreCase("UPD_OBJ_COPY")) {
                urlp.updObjectCopy();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjMgtPrivilege(urlp.obj_bob_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }
                
                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                objectManager.updObjectCopy(con, prof, urlp.obj_bob_nod_id, urlp.loc_id, urlp.loc_copy, urlp.loc_desc, urlp.loc_update_timestamp);  

                con.commit();
                response.sendRedirect(urlp.url_success);
            }else if (urlp.cmd.equalsIgnoreCase("DEL_OBJ_COPY")) {
                urlp.delObjectCopy();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjMgtPrivilege(urlp.obj_bob_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }
                
                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                objectManager.delObjectCopy(con, prof, urlp.obj_bob_nod_id, urlp.loc_id, urlp.loc_update_timestamp);  

                con.commit();
                response.sendRedirect(urlp.url_success);
            }else if (urlp.cmd.equalsIgnoreCase("INS_OBJ")) {
                urlp.insObject();

                //access control
                int index = urlp.vNodColName.indexOf("nod_parent_nod_id");
                long parent_nod_id = ((Long)urlp.vNodColValue.elementAt(index)).longValue();
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
              
                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);                
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
             }else if (urlp.cmd.equalsIgnoreCase("EDIT_OBJ")) {
                urlp.checkInObject();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjMgtPrivilege(urlp.obj_bob_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }
                    
                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                ViewKmObject newVersion = 
                objectManager.editObject(con, static_env, prof, tmpUploadPath,
                                                urlp.obj_bob_nod_id, urlp.obj_update_timestamp,
                                                urlp.vObjColName, urlp.vObjColType, urlp.vObjColValue,
                                                urlp.vObjClobColName, urlp.vObjClobColValue, 
                                                urlp.vFileName, urlp.bob_code);
                    
                //    if( urlp.keepCheckOut ) 
                //        objectManager.keepCheckOutObject( con, prof, urlp.obj_bob_nod_id);
                con.commit();
                cwSysMessage sms = new cwSysMessage("GEN003");
                msgBox(ServletModule.MSG_STATUS, sms, urlp.url_success, out);

            } else if( urlp.cmd.equalsIgnoreCase("elib_get_user_rec") ||
                       urlp.cmd.equalsIgnoreCase("elib_get_user_rec_xml") ){
                urlp.getUserRec();

                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                //View Self Record only
                String xml = kmLibObjMgr.getUserRecAsXML(con, urlp.usr_ent_id, urlp.sort_by, urlp.order_by);
                //xml = formatXML(xml, DbKmLibraryObjectBorrow.geteLibRuleAsXML(static_env.KM_LIB_BORROW_LIMIT, static_env.KM_LIB_OVERDUE_LIMIT, static_env.KM_LIB_RENEW_LIMIT, static_env.KM_LIB_DUE_DAY), MODULENAME);
                xml = formatXML(xml, DbKmLibraryObjectBorrow.geteLibRuleAsXML(static_env.KM_LIB_BORROW_LIMIT, static_env.KM_LIB_OVERDUE_LIMIT, static_env.KM_LIB_RENEW_LIMIT, static_env.KM_LIB_DUE_DAY), MODULENAME, urlp.stylesheet);
                if (urlp.cmd.equalsIgnoreCase("elib_get_user_rec")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
                return;
            } else if ( urlp.cmd.equalsIgnoreCase("elib_get_item_rec") ||
                        urlp.cmd.equalsIgnoreCase("elib_get_item_rec_xml") ) {
                urlp.getItemRec();
                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = null;
                if( urlp.nod_id > 0 )
                    xml = kmLibObjMgr.getItemRecAsXML(con, prof, static_env, urlp.nod_id, urlp.tvw_id);
                else if ( urlp.call_num != null && urlp.call_num.length() > 0 )
                    xml = kmLibObjMgr.getItemRecAsXML(con, prof, static_env, urlp.call_num, urlp.tvw_id);
                //xml = formatXML(xml, DbKmLibraryObjectBorrow.geteLibRuleAsXML(static_env.KM_LIB_BORROW_LIMIT, static_env.KM_LIB_OVERDUE_LIMIT, static_env.KM_LIB_RENEW_LIMIT, static_env.KM_LIB_DUE_DAY), MODULENAME);
                xml = formatXML(xml, DbKmLibraryObjectBorrow.geteLibRuleAsXML(static_env.KM_LIB_BORROW_LIMIT, static_env.KM_LIB_OVERDUE_LIMIT, static_env.KM_LIB_RENEW_LIMIT, static_env.KM_LIB_DUE_DAY), MODULENAME, urlp.stylesheet);
                if( urlp.cmd.equalsIgnoreCase("elib_get_item_rec") ) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
                return;
            } else if( urlp.cmd.equalsIgnoreCase("elib_get_item_list") ||
                       urlp.cmd.equalsIgnoreCase("elib_get_item_list_xml") ){
                try{
                    urlp.pagination();
                    urlp.getItemList();
                    KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                    String xml = kmLibObjMgr.getLibObjAsXML(con, sess, urlp.nod_id, /*urlp.usr_ent_id*/prof.usr_ent_id, prof.usrGroupsList(), urlp.cwPage);
                    xml = formatXML(xml, DbKmLibraryObjectBorrow.geteLibRuleAsXML(static_env.KM_LIB_BORROW_LIMIT, static_env.KM_LIB_OVERDUE_LIMIT, static_env.KM_LIB_RENEW_LIMIT, static_env.KM_LIB_DUE_DAY), MODULENAME, urlp.stylesheet);
                    if( urlp.cmd.equalsIgnoreCase("elib_get_item_list") ) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    } else {
                        static_env.outputXML(out, xml);
                    }
                }catch(Exception e){
                	CommonLog.error(e.getMessage(),e);
                }
                return;            
            } else if ( urlp.cmd.equalsIgnoreCase("HIST_USER") ||
                        urlp.cmd.equalsIgnoreCase("HIST_USER_XML") ) {
                urlp.history();

                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasUserHistoryPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_USER_HISTORY_PERMISSION);
                }

                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = kmLibObjMgr.getUserHistoryXML(con, prof, urlp.usrEntId);
                //xml = formatXML(xml, MODULENAME);
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                if( urlp.cmd.equalsIgnoreCase("HIST_USER") ) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
                return;
            } else if ( urlp.cmd.equalsIgnoreCase("HIST_COPY") ||
                        urlp.cmd.equalsIgnoreCase("HIST_COPY_XML") ) {
                urlp.history();

                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasCopyHistoryPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_COPY_HISTORY_PERMISSION);
                }

                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = kmLibObjMgr.getCopyHistoryXML(con, prof, urlp.copyId);
                //xml = formatXML(xml, MODULENAME);
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                if( urlp.cmd.equalsIgnoreCase("HIST_COPY") ) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                } else {
                    static_env.outputXML(out, xml);
                }
                return;
            }else if (urlp.cmd.equalsIgnoreCase("GET_OBJ") || 
                      urlp.cmd.equalsIgnoreCase("GET_OBJ_XML")) {
                // a new GET_OBJ cmd is needed since the next version is a major version instrad of a minor version
                urlp.getObject();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjReadPrivilege(urlp.obj_bob_nod_id, urlp.parent_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_READ_PERMISSIOIN);
                }
                
                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = objectManager.getObjectVersionAsXML(con, prof, static_env,
                                                                 urlp.obj_bob_nod_id, 
                                                                 urlp.obj_version,
                                                                 urlp.obj_type, 
                                                                 urlp.tvw_id,
                                                                 urlp.parent_nod_id);                                                           
                /* page variant BEGIN */
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                acPageVariant.prof = prof;
                acPageVariant.ent_id = prof.usr_ent_id;
                acPageVariant.instance_id = urlp.obj_bob_nod_id;
                String metaXML = acPageVariant.answerPageVariantAsXML(AcXslQuestion.getOneXslQuestions(urlp.stylesheet, xslQuestions));
                /* page variant END */

                //xml = formatXML(xml, metaXML, super.MODULENAME);
                xml = formatXML(xml, null, super.MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("GET_OBJ")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("MARK_DEL_OBJ")) {
                urlp.delObject();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjMgtPrivilege(urlp.obj_bob_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }

                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                objectManager.markDel(con, static_env, urlp.obj_bob_nod_id, urlp.obj_update_timestamp, prof.usr_id);
                
/*                String dir = static_env.INI_OBJ_DIR_UPLOAD + dbUtils.SLASH + urlp.obj_bob_nod_id;
                try {
                    dbUtils.delDir(dir);
                } catch(qdbException e) {
                    throw new cwException (e.getMessage());
                }*/
                con.commit();
                response.sendRedirect(urlp.url_success);
                
                
            } else if ( urlp.cmd.equalsIgnoreCase("RENEW_PREP") ||
                        urlp.cmd.equalsIgnoreCase("RENEW_PREP_XML") ) {
                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasRenewPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_RENEW_PERMISSION);
                }
                urlp.renew();
                try {
                    KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                    DbKmLibraryObjectBorrow borrow = new DbKmLibraryObjectBorrow();
                    borrow.lob_id = urlp.lobId;
                    borrow.lob_lio_bob_nod_id = urlp.callId;
                    borrow.get(con);
                    String xml = kmLibObjMgr.getRenewXML(con, prof, static_env, urlp.callId, borrow.lob_loc_id);
                    xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                    if( urlp.cmd.equalsIgnoreCase("RENEW_PREP") ) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    } else {
                        static_env.outputXML(out, xml);
                    }
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
                return;
            } else if ( urlp.cmd.equalsIgnoreCase("RENEW_EXEC") ) {
                urlp.renew();

                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasRenewPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_RENEW_PERMISSION);
                }
                if(prof.usr_ent_id != urlp.usrEntId) {
                    AcKmNode acKmNode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                    ViewKmObject viewKmObj = new ViewKmObject();
                    viewKmObj.dbObject = new DbKmObject();
                    viewKmObj.dbObject.obj_bob_nod_id = urlp.callId;
                    Vector fldNodIdVec = viewKmObj.getParentFolderId(con);
                    Long fldNodIdObj = (Long) fldNodIdVec.firstElement();
                    
                    if(!acKmNode.hasMgtPrivilege(fldNodIdObj.longValue())) {
                        throw new cwSysMessage(KMModule.SMSG_KM_NO_READ_PERMISSIOIN);
                    }
                }

                try {
                    KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                    kmLibObjMgr.renew(con, static_env, prof.usr_id, urlp.callId, urlp.copyId);
                    con.commit();

                    response.sendRedirect(urlp.url_success);
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
            } else if ( urlp.cmd.equalsIgnoreCase("RESERVE_PREP") ||
                        urlp.cmd.equalsIgnoreCase("RESERVE_PREP_XML") ) {
                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasReservePrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_RESERVE_PERMISSION);
                }
                urlp.reserve();
                try {
                    KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                    String xml = kmLibObjMgr.getReserveXML(con, prof, static_env, urlp.usrEntId, urlp.callId);
                    xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                    if( urlp.cmd.equalsIgnoreCase("RESERVE_PREP") ) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    } else {
                        static_env.outputXML(out, xml);
                    }
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
                return;
            } else if ( urlp.cmd.equalsIgnoreCase("RESERVE_EXEC") ) {
                urlp.reserve();

                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasReservePrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_RESERVE_PERMISSION);
                }
                if(prof.usr_ent_id != urlp.usrEntId) {
                    AcKmNode acKmNode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                    ViewKmObject viewKmObj = new ViewKmObject();
                    viewKmObj.dbObject = new DbKmObject();
                    viewKmObj.dbObject.obj_bob_nod_id = urlp.callId;
                    Vector fldNodIdVec = viewKmObj.getParentFolderId(con);
                    Long fldNodIdObj = (Long) fldNodIdVec.firstElement();
                    
                    if(!acKmNode.hasMgtPrivilege(fldNodIdObj.longValue())) {
                        throw new cwSysMessage(KMModule.SMSG_KM_NO_READ_PERMISSIOIN);
                    }
                }

                try {
                    KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                    long lobId = kmLibObjMgr.reserve(con, prof, static_env, prof.usr_id, urlp.usrEntId, urlp.callId);
                    if(urlp.recEntId != -1) {
                        kmLibObjMgr.reserveNotify(con, prof, urlp.mesgSubject, urlp.recEntId, urlp.ccEntIds, urlp.bccEntIds, lobId);
                    }
                    con.commit();

                    response.sendRedirect(urlp.url_success);
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
            } else if ( urlp.cmd.equalsIgnoreCase("RESERVE_CANCEL") ) {
                urlp.reserveCancel();

                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasCancelPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_CANCEL_PERMISSION);
                }
                if(prof.usr_ent_id != urlp.usrEntId) {
                    AcKmNode acKmNode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                    ViewKmObject viewKmObj = new ViewKmObject();
                    viewKmObj.dbObject = new DbKmObject();
                    viewKmObj.dbObject.obj_bob_nod_id = urlp.callId;
                    Vector fldNodIdVec = viewKmObj.getParentFolderId(con);
                    Long fldNodIdObj = (Long) fldNodIdVec.firstElement();
                    
                    if(!acKmNode.hasMgtPrivilege(fldNodIdObj.longValue())) {
                        throw new cwSysMessage(KMModule.SMSG_KM_NO_READ_PERMISSIOIN);
                    }
                }

                try {
                    KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                    kmLibObjMgr.reserveCancel(con, prof, static_env, prof.usr_id, urlp.usrEntId, urlp.callId);
                    con.commit();

                    response.sendRedirect(urlp.url_success);
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
            } else if ( urlp.cmd.equalsIgnoreCase("BORROW_PREP") ||
                        urlp.cmd.equalsIgnoreCase("BORROW_PREP_XML") ) {
                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasBorrowPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_BORROW_PERMISSION);
                }
                urlp.reserve();
                try {
                    KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                    String xml = kmLibObjMgr.getBorrowXML(con, prof, static_env, urlp.usrEntId, urlp.callId);
                    xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                    if( urlp.cmd.equalsIgnoreCase("BORROW_PREP") ) {
                        generalAsHtml(xml, out, urlp.stylesheet);
                    } else {
                        static_env.outputXML(out, xml);
                    }
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
                return;
            } else if ( urlp.cmd.equalsIgnoreCase("BORROW_EXEC") ) {
                urlp.borrow();

                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasBorrowPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_BORROW_PERMISSION);
                }
                if(prof.usr_ent_id != urlp.usrEntId) {
                    AcKmNode acKmNode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                    ViewKmObject viewKmObj = new ViewKmObject();
                    viewKmObj.dbObject = new DbKmObject();
                    viewKmObj.dbObject.obj_bob_nod_id = urlp.callId;
                    Vector fldNodIdVec = viewKmObj.getParentFolderId(con);
                    Long fldNodIdObj = (Long) fldNodIdVec.firstElement();
                    
                    if(!acKmNode.hasMgtPrivilege(fldNodIdObj.longValue())) {
                        throw new cwSysMessage(KMModule.SMSG_KM_NO_READ_PERMISSIOIN);
                    }
                }

                try {
                    KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                    long lobId = kmLibObjMgr.borrow(con, static_env, prof.usr_id, urlp.usrEntId, urlp.callId);
                    if(urlp.recEntId != -1) {
                        kmLibObjMgr.borrowNotify(con, prof, urlp.mesgSubject, urlp.recEntId, urlp.ccEntIds, urlp.bccEntIds, lobId);
                    }
                    con.commit();

                    response.sendRedirect(urlp.url_success);
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
            } else if ( urlp.cmd.equalsIgnoreCase("BORROW_CANCEL") ) {
                urlp.borrowCancel();

                // access control
                AcKmLibrary acKmLib = new AcKmLibrary(con, prof.usr_ent_id);
                if(!acKmLib.hasCancelPrivilege(prof.current_role)) {
                    throw new cwSysMessage(KMLibraryObjectManager.SMSG_NO_CANCEL_PERMISSION);
                }
                if(prof.usr_ent_id != urlp.usrEntId) {
                    AcKmNode acKmNode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                    ViewKmObject viewKmObj = new ViewKmObject();
                    viewKmObj.dbObject = new DbKmObject();
                    viewKmObj.dbObject.obj_bob_nod_id = urlp.callId;
                    Vector fldNodIdVec = viewKmObj.getParentFolderId(con);
                    Long fldNodIdObj = (Long) fldNodIdVec.firstElement();
                    
                    if(!acKmNode.hasMgtPrivilege(fldNodIdObj.longValue())) {
                        throw new cwSysMessage(KMModule.SMSG_KM_NO_READ_PERMISSIOIN);
                    }
                }

                try {
                    KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                    kmLibObjMgr.borrowCancel(con, prof, static_env, prof.usr_id, urlp.usrEntId, urlp.callId);
                    con.commit();

                    response.sendRedirect(urlp.url_success);
                } catch (cwSysMessage msg){
                    msgBox(ServletModule.MSG_ERROR,  msg, urlp.url_failure, out);
                }
            }else if (urlp.cmd.equalsIgnoreCase("GET_OVERDUE_LIST") ||
                      urlp.cmd.equalsIgnoreCase("GET_OVERDUE_LIST_XML")) {
                urlp.pagination();
                sess = request.getSession(true);

                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);

                String xml = kmLibObjMgr.getOverdueListXML(con, sess, urlp.cwPage);
                String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);            
                            
                xml = formatXML(xml, metaXML, MODULENAME);
                if (urlp.cmd.equalsIgnoreCase("GET_OVERDUE_LIST")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);

                }
            }else if (urlp.cmd.equalsIgnoreCase("PREP_ADD_DOMAIN") || 
                      urlp.cmd.equalsIgnoreCase("PREP_ADD_DOMAIN_XML")) {
                urlp.prepPublish();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjMgtPrivilege(urlp.obj_bob_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }
                
                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = objectManager.getPublishXML(con, urlp.obj_bob_nod_id);  
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                
                if (urlp.cmd.equalsIgnoreCase("PREP_ADD_DOMAIN")) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }else {
                    static_env.outputXML(out, xml);
                }
            }else if (urlp.cmd.equalsIgnoreCase("ADD_DOMAIN")) {
                urlp.publishObject();

                //access control
                AcKmNode acnode = new AcKmNode(con, prof.usr_ent_id, prof.usrGroupsList());
                if(!acnode.hasObjMgtPrivilege(urlp.obj_bob_nod_id)) {
                    throw new cwSysMessage(SMSG_KM_NO_UPDATE_PERMISSIOIN);
                }
                KMLibraryObjectManager objectManager = new KMLibraryObjectManager(con, prof.root_ent_id);
                objectManager.addDomain(con, static_env, prof,
                                                 urlp.obj_update_timestamp, 
                                                 urlp.obj_bob_nod_id, urlp.domain_id_list);
                    
                con.commit();
                response.sendRedirect(urlp.url_success);
            } else if ( urlp.cmd.equalsIgnoreCase("ADV_SEARCH") ||
                        urlp.cmd.equalsIgnoreCase("ADV_SEARCH_XML") ) {
                
                urlp.pagination();
                urlp.advSearch();
                KMLibraryObjectManager kmLibObjMgr = new KMLibraryObjectManager(con, prof.root_ent_id);
                String xml = kmLibObjMgr.getLrnAdvSearchResultAsXML(con, sess, prof.usr_ent_id, prof.usrGroupsList(), urlp.obj_title, urlp.obj_author, urlp.obj_type_list, urlp.words, urlp.call_num, urlp.nod_id_lst, urlp.cwPage, static_env.SEARCH_DB_DIR, urlp.show_deleted);
                xml = formatXML(xml, null, MODULENAME, urlp.stylesheet);
                if( urlp.cmd.equalsIgnoreCase("ADV_SEARCH") ) {
                    generalAsHtml(xml, out, urlp.stylesheet);
                } else {
                    static_env.outputXML(out,xml);
                }
                return;
                
            } else {
                throw new cwSysMessage("GEN000");
            }

        }catch (cwSysMessage se) {
            try {
            	CommonLog.error("cwSysMessage : " + se.getSystemMessage(prof.label_lan));
                 con.rollback();
                 msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
             } catch (SQLException sqle) {
                 out.println("SQL error: " + sqle.getMessage());
             }
        }
    }
 
        
    /**
    Include a km specific open-end tag and user profile xml with the input data XML
    */
    public String formatXML(String in, String moduleName) throws IOException, SQLException{
        StringBuffer outBuf = new StringBuffer(2500);
    
        String metaXML = new String();
        return super.formatXML(in, metaXML, moduleName);
    }
}


