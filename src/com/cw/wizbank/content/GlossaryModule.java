package com.cw.wizbank.content;

import java.io.*;
import java.sql.*;

import javax.servlet.http.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcModule;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cwn.wizbank.utils.CommonLog;

public class GlossaryModule extends ServletModule {
    
    public final static String INS_KEY      =   "ins_key";
    public final static String UPD_KEY      =   "upd_key";
    public final static String DEL_KEY      =   "del_key";
    public final static String GET_KEYS     =   "get_keys";
    public final static String GET_KEY      =   "get_key";
    public final static String XML          =   "_xml";
    public GlossaryModule() { ; }
    
    public void process()
        throws SQLException, IOException, cwException {        

//            String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);
            
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
//                response.sendRedirect(url_relogin);        
            }

            PrintWriter out = response.getWriter();
            
            GlossaryReqParam urlp = new GlossaryReqParam(request, clientEnc, static_env.ENCODING);
        
            HttpSession sess = request.getSession(true);
            if(urlp.cmd == null) {
             
                throw new cwException("Invalid Command");
            
            }
            // insert keyword
            else if( urlp.cmd.equalsIgnoreCase(INS_KEY) ) {                
                Glossary glo = new Glossary();
                urlp.ins_key();
                try{
                    AcModule acMod = new AcModule(con);
                    if (!acMod.checkModifyPermission(prof, urlp.dbGlossary.glo_res_id)){
                        throw new cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                    }
                    glo.ins(con, urlp.dbGlossary, prof);
                }catch(cwSysMessage e) {
                    con.rollback();
                    msgBox(ServletModule.MSG_STATUS, e, urlp.url_success, out);
                    return;
                }
                con.commit();
                msgBox(MSG_STATUS, new cwSysMessage("GLO001"), urlp.url_success, out);                 
            }
            
            // update keyword
            else if( urlp.cmd.equalsIgnoreCase(UPD_KEY) ) {                
                Glossary glo = new Glossary();
                urlp.upd_key();
                try{                    
                    AcModule acMod = new AcModule(con);
                    if (!acMod.checkModifyPermission(prof, urlp.dbGlossary.getResId(con))){
                        throw new cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                    }
                    glo.upd(con, urlp.dbGlossary, prof);
                }catch(cwSysMessage e) {
                    con.rollback();
                    msgBox(ServletModule.MSG_STATUS, e, urlp.url_success, out);
                    return;
                }                
                con.commit();
                msgBox(MSG_STATUS, new cwSysMessage("GLO002"), urlp.url_success, out);                 
            }
            
            // delete keyword
            else if( urlp.cmd.equalsIgnoreCase(DEL_KEY) ) {                
                Glossary glo = new Glossary();
                urlp.del_key();
                try{
                    AcModule acMod = new AcModule(con);
                    if (!acMod.checkModifyPermission(prof, urlp.dbGlossary.getResId(con))){
                        throw new cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
                    }
                    glo.del(con, urlp.dbGlossary, prof);
                }catch(cwSysMessage e) {
                    con.rollback();
                    msgBox(ServletModule.MSG_STATUS, e, urlp.url_success, out);
                    return;
                }                    
                con.commit();
                msgBox(MSG_STATUS, new cwSysMessage("GLO003"), urlp.url_success, out); 
                
            }
            
            // get a keyword and definition
            else if( urlp.cmd.equalsIgnoreCase(GET_KEY) || 
                      urlp.cmd.equalsIgnoreCase(GET_KEY + XML) ) {

                StringBuffer xml = new StringBuffer();                
                Glossary glo = new Glossary();
                urlp.get_key();
                xml.append(glo.asXML(con, urlp.dbGlossary, prof));
                
                if (urlp.cmd.equalsIgnoreCase(GET_KEY + XML)) {
                    static_env.outputXML(out, xml.toString());
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                
                                
            }
            
            // get key and definition list
            else if( urlp.cmd.equalsIgnoreCase(GET_KEYS) || 
                      urlp.cmd.equalsIgnoreCase(GET_KEYS + XML) ) {

                StringBuffer xml = new StringBuffer();                
                Glossary glo = new Glossary();
                urlp.get_keys();
                if (urlp.myDbModule.tkh_id < 0) {
                	urlp.myDbModule.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con, urlp.myDbModule.mod_res_id, prof.usr_ent_id);
                }
                try {
					if (urlp.myDbModule.tkh_id != 0 && urlp.myDbModule.tkh_id != DbTrackingHistory.TKH_ID_NOT_FOUND && DbTrackingHistory.getAppTrackingIDByCos(con, urlp.myDbModule.tkh_id, prof.usr_ent_id, dbModule.getCosId(con, urlp.myDbModule.mod_res_id), urlp.myDbModule.mod_res_id) != 1){
					    msgBox(MSG_ERROR, new cwSysMessage("USR033"), urlp.url_failure, out);
					    return;
					}
				} catch (qdbException e) {
					con.rollback();
					CommonLog.error(e.getMessage(),e);
				}
                xml.append(glo.asXML(con, urlp.dbGlossary, prof, urlp.letter, urlp.indexTimestamp, sess, urlp.myDbModule.tkh_id));
                
                if (urlp.cmd.equalsIgnoreCase(GET_KEYS + XML)) {
                    static_env.outputXML(out, xml.toString());
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                

            }
        }
        
        
}