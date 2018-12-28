package com.cw.wizbank.content;

import java.io.*;
import java.sql.SQLException;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.accesscontrol.AcModule;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cwn.wizbank.utils.CommonLog;


public class ReferenceModule extends ServletModule
{
    
    public ReferenceModule() {;}
    
    public void process() throws SQLException, cwException, IOException {
    	CommonLog.debug("IN ReferenceModule");
        
        if (prof ==null)
        	CommonLog.info("login profile is null.");
        else 
        	CommonLog.info("loginProfile  > usr_id :" + prof.usr_id);
        // get output stream for normal content to client
        PrintWriter out = response.getWriter();
        
        ReferenceReqParam urlp = new ReferenceReqParam(request, clientEnc, static_env.ENCODING);

        // service processing starts here
        try {
//            String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);
            
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
//                response.sendRedirect(url_relogin);            
            }
            else if (urlp.cmd == null) {
                throw new cwException("invalid command");

            }
            else if (urlp.cmd.toUpperCase().startsWith("GET_REFERENCE_LIST")) {
                String xml = "";
                urlp.reference_info();
                if (urlp.myDbModule.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
                	urlp.myDbModule.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, urlp.myDbModule.mod_res_id, prof.usr_ent_id);
                }
                if (urlp.myDbModule.tkh_id != 0 && urlp.myDbModule.tkh_id != DbTrackingHistory.TKH_ID_NOT_FOUND && DbTrackingHistory.getAppTrackingIDByCos(con, urlp.myDbModule.tkh_id, prof.usr_ent_id, dbModule.getCosId(con, urlp.myDbModule.mod_res_id), urlp.myDbModule.mod_res_id) != 1){
				    msgBox(MSG_ERROR, new cwSysMessage("USR033"), urlp.url_failure, out);
				    return;
                }
                
                Reference myReference = new Reference(urlp.myDbReference, urlp.myDbModule);
                xml += myReference.getReferenceList(con, prof);
                if (urlp.cmd.equalsIgnoreCase("GET_REFERENCE_LIST_XML")) {
                    out.println(xml);
                }
                else {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }
            }
            else if (urlp.cmd.toUpperCase().startsWith("GET_REFERENCE")) {
                String xml = "";
                urlp.reference_info();
                Reference myReference = new Reference(urlp.myDbReference, urlp.myDbModule);
                xml += myReference.getReference(con, prof);
                if (urlp.cmd.equalsIgnoreCase("GET_REFERENCE_XML")) {
                    out.println(xml);
                }
                else {
                    generalAsHtml(xml, out, urlp.stylesheet);
                }
            }
            else if (urlp.cmd.toUpperCase().startsWith("INS_REFERENCE")) {
                int ref_id = 0;
                urlp.reference_info();
                
                AcModule acMod = new AcModule(con);
                if (!acMod.checkModifyPermission(prof, urlp.myDbModule.mod_res_id)){
                    throw new cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                Reference myReference = new Reference(urlp.myDbReference, urlp.myDbModule);
                ref_id = myReference.insReference(con, prof);
                if (ref_id > 0) {
                    con.commit();
                    //msgBox(MSG_STATUS, new cwSysMessage("REF001"), urlp.url_success, out);
                    response.sendRedirect(urlp.url_success);
                }
                else {
                    con.rollback();
                    //msgBox(MSG_STATUS, new cwSysMessage("REF002"), urlp.url_success, out);
                    response.sendRedirect(urlp.url_success);
                }
            }
            else if (urlp.cmd.toUpperCase().startsWith("UPD_REFERENCE")) {
                int ref_id = 0;
                urlp.reference_info();
                AcModule acMod = new AcModule(con);
                if (!acMod.checkModifyPermission(prof, urlp.myDbModule.mod_res_id)){
                    throw new cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                Reference myReference = new Reference(urlp.myDbReference, urlp.myDbModule);

                ref_id = myReference.updReference(con, prof);
                con.commit();
                //msgBox(MSG_STATUS, new cwSysMessage("REF003"), urlp.url_success, out);
                response.sendRedirect(urlp.url_success);
            }
            else if (urlp.cmd.toUpperCase().startsWith("DEL_REFERENCE")) {
                int ref_id = 0;
                urlp.reference_info();
                AcModule acMod = new AcModule(con);
                if (!acMod.checkModifyPermission(prof, urlp.myDbModule.mod_res_id)){
                    throw new cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                }

                Reference myReference = new Reference(urlp.myDbModule);
                ref_id = myReference.delReference(con, prof, urlp.ref_id_list);
                con.commit();
                //msgBox(MSG_STATUS, new cwSysMessage("REF005"), urlp.url_success, out);
                response.sendRedirect(urlp.url_success);
            }
            else {
                // do nothing
            }
        } catch (cwSysMessage e) {
             try {
                 con.rollback();
                 msgBox(ServletModule.MSG_STATUS, e, urlp.url_failure, out);
             } catch (cwException ce) {
                out.println("Server error: " + e.getMessage());
             } catch (SQLException se) {
                out.println("SQL error: " + e.getMessage());
             }             
		} catch (qdbException e) {
			CommonLog.error(e.getMessage(),e);
	        try {
	            con.rollback();
	            static_env.dispDebugMesg (out , e.getMessage());
	        } catch (SQLException err) {
                out.println("SQL error: " + err.getMessage());
                CommonLog.error("SQL error: " + err.getMessage(),err);
	        }
		}
    }
}