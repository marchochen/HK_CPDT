package com.cw.wizbank.entity;

import java.io.*;
import java.sql.*;
import javax.servlet.http.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.db.DbIndustryCode;
import com.cw.wizbank.db.view.ViewEntityRelation;
//import com.cw.wizbank.db.DbEntityDefinition;

public class EntityModule extends ServletModule {
    
    public final static String GET_ENT_TREE = "get_usr_ent_tree";
    public final static String ASSIGN_USR_ENT = "assign_usr_ent"; 
    public final static String GET_ENT_CNT_LST = "get_ent_cnt_lst";
    public final static String ADD_ENT = "add_ent";
    public final static String DEL_ENT = "del_ent";
    public final static String UPD_ENT = "upd_ent";
    public final static String GET_ENT = "get_ent";
    /*public final static String GET_ENT_DEF = "get_ent_def";*/
    public final static String GET_USR_ROL = "get_usr_rol";
    public final static String GET_ASSIGNED_ROL = "get_assigned_rol";
    public final static String XML          =   "_xml";
        
    private final static String ENTITY_MODULE = "entity_module";

    public DbIndustryCode dbidc;
    
    public ViewEntityRelation view;
    
    public EntityModule() { ; }
    
    public void process()
        throws SQLException, IOException, cwException {        

//            String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);
           
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
//                response.sendRedirect(url_relogin);        
            }

            PrintWriter out = response.getWriter();

            EntityReqParam urlp = new EntityReqParam(request, clientEnc, static_env.ENCODING);
            
            urlp.myCommon();
            
            HttpSession sess = request.getSession(true);
            if(urlp.cmd == null) {
             
                throw new cwException("Invalid Command");
            }

            //assigned user attributes to user (e.g. industry code)
            else if( urlp.cmd.equalsIgnoreCase(ASSIGN_USR_ENT)) {
                if(urlp.entityType.equalsIgnoreCase("IDC")) {
                    urlp.assign_idc();
                    urlp.idc.assignUserIndustryCode(con, urlp.idc_ent_ids, urlp.relationType, prof.usr_id);
                }
                con.commit();
                response.sendRedirect(urlp.url_success);
            }
            
            // get the tree structure of industry code and check if prof.usr_ent_id has each of industry codes
            else if( urlp.cmd.equalsIgnoreCase(GET_ENT_TREE) || 
                      urlp.cmd.equalsIgnoreCase(GET_ENT_TREE + XML) ) {

                StringBuffer xmlBuf = new StringBuffer(1024);
                String xml = "";
                if(urlp.entityType.equalsIgnoreCase("IDC")) {
                    urlp.get_idc_tree();
                    urlp.idc.rootEntId = prof.root_ent_id;
                    
                    //xmlBuf.append(urlp.idc.getUserIndustryCodeAsXML(con, EntityReqParam.LONG_PARAMETER_NOT_FOUND, 10));
                    xmlBuf.append("<entity_attribute>");
                    xmlBuf.append(urlp.idc.getUserAsXML(con));
                    xmlBuf.append(urlp.idc.getUserIndustryCodeAsXML(con));
                    xmlBuf.append(urlp.idc.getIndustryCodeTreeAsXML(con, EntityReqParam.LONG_PARAMETER_NOT_FOUND, 10));
                    xmlBuf.append("</entity_attribute>");
                    
                    xml = formatXML(xmlBuf.toString(), "", ENTITY_MODULE);
                }                
                if (urlp.cmd.equalsIgnoreCase(GET_ENT_TREE + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                                               
            }

            // get the member industry code 
            else if( urlp.cmd.equalsIgnoreCase(GET_ENT_CNT_LST) || 
                      urlp.cmd.equalsIgnoreCase(GET_ENT_CNT_LST + XML) ) {

                StringBuffer xmlBuf = new StringBuffer(1024);
                StringBuffer metaBuf = new StringBuffer(1024);
                String xml = "";
                if(urlp.entityType.equalsIgnoreCase("IDC")) {
                    urlp.get_idc_cnt_lst();
                    urlp.idc.rootEntId = prof.root_ent_id;
                    
                    xmlBuf.append(urlp.idc.getMemberIndustryCodeAsXML(con));
                    //metaBuf.append(EntityDefinition.getAllEntityDefinitionAsXML(con));
                    metaBuf.append(IndustryCode.getRootIndustryCodeAsXML(con, prof.root_ent_id));
                    xml = formatXML(xmlBuf.toString(), metaBuf.toString(), ENTITY_MODULE);
                }                
                if (urlp.cmd.equalsIgnoreCase(GET_ENT_CNT_LST + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                                               
            }

            else if( urlp.cmd.equalsIgnoreCase(ADD_ENT) ) {
                try {
                    
                    String entityType = "Industry Code";
                    if(urlp.entityType.equalsIgnoreCase("IDC")) {
                        
                        urlp.add_idc();
                        urlp.idc.rootEntId = prof.root_ent_id;
                        urlp.idc.addIndustryCode(con, prof.usr_id);
                    }
                    con.commit();
                    cwSysMessage e = new cwSysMessage("ENT003", entityType);
                    msgBox(ServletModule.MSG_STATUS,  e, urlp.url_success, out);
                    //response.sendRedirect(urlp.url_success);
                }
                catch(cwSysMessage e) {
                    con.rollback();
                    msgBox(MSG_STATUS, e, urlp.url_failure, out);
                    return;            
                }
            }

            else if( urlp.cmd.equalsIgnoreCase(DEL_ENT) ) {
                try {
                    String entityType = "";
                    
                    if(urlp.entityType.equalsIgnoreCase("IDC")) {
                        
                        entityType = "Industry Code";
                        urlp.del_idc();
                        for(int i=0; i<urlp.idc_ent_ids.length; i++) {
                            urlp.idc.ent_id = urlp.idc_ent_ids[i];
                            urlp.idc.ent_upd_date = urlp.idc_upd_timestamps[i];
                            urlp.idc.delIndustryCode(con, prof.usr_id);
                        }
                    }
                    con.commit();
                    cwSysMessage e = new cwSysMessage("ENT001", entityType);
                    msgBox(ServletModule.MSG_STATUS,  e, urlp.url_success, out);
                    //response.sendRedirect(urlp.url_success);
                }
                catch(cwSysMessage e) {
                    con.rollback();
                    msgBox(MSG_STATUS, e, urlp.url_failure, out);
                    return;            
                }
            }

            else if( urlp.cmd.equalsIgnoreCase(UPD_ENT) ) {
                try {
                    
                    String entityType = "";
                    if(urlp.entityType.equalsIgnoreCase("IDC")) {
                        
                        entityType = "Industry Code";
                        urlp.upd_idc();
                        //urlp.idc.rootEntId = prof.root_ent_id;
                        urlp.idc.updIndustryCode(con, prof.usr_id);
                    }
                    con.commit();
                    cwSysMessage e = new cwSysMessage("ENT002", entityType);
                    msgBox(ServletModule.MSG_STATUS,  e, urlp.url_success, out);
                    //response.sendRedirect(urlp.url_success);
                }
                catch(cwSysMessage e) {
                    con.rollback();
                    msgBox(MSG_STATUS, e, urlp.url_failure, out);
                    return;            
                }
            }

            // get the industry code 
            else if( urlp.cmd.equalsIgnoreCase(GET_ENT) || 
                      urlp.cmd.equalsIgnoreCase(GET_ENT + XML) ) {

                StringBuffer xmlBuf = new StringBuffer();                
                String xml = "";
                if(urlp.entityType.equalsIgnoreCase("IDC")) {
                    urlp.get_idc();
                    //urlp.idc.rootEntId = prof.root_ent_id;
                    
                    xmlBuf.append(urlp.idc.asXML(con));
                    xml = formatXML(xmlBuf.toString(), "", ENTITY_MODULE);
                }                
                if (urlp.cmd.equalsIgnoreCase(GET_ENT + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                                               
            }
            // get the entity definition
            /*
            else if( urlp.cmd.equalsIgnoreCase(GET_ENT_DEF) || 
                      urlp.cmd.equalsIgnoreCase(GET_ENT_DEF + XML) ) {

                StringBuffer xmlBuf = new StringBuffer();                
                String xml;
                urlp.get_ent_def();
               
                xmlBuf.append(urlp.end.asXML(con));
                xml = formatXML(xmlBuf.toString(), "", ENTITY_MODULE);
                
                if (urlp.cmd.equalsIgnoreCase(GET_ENT_DEF + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                                               
            }
            */
            // get a xml of assigned role of a user
            else if( urlp.cmd.equalsIgnoreCase(GET_USR_ROL) || 
                      urlp.cmd.equalsIgnoreCase(GET_USR_ROL + XML) ) {

                StringBuffer xmlBuf = new StringBuffer(1024);
                String xml = "";
                urlp.get_usr_rol();
                
                xmlBuf.append("<user_role>");
                xmlBuf.append(urlp.user.getUserAsXML(con));
                xmlBuf.append(urlp.user.getUserRoleAsXML(con));
                xmlBuf.append(dbUtils.getAllRoleAsXML(con, "role_list", prof.root_ent_id));
                xmlBuf.append("</user_role>");
                xml = formatXML(xmlBuf.toString(), "", ENTITY_MODULE);

                if (urlp.cmd.equalsIgnoreCase(GET_USR_ROL + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                                               
            }

            // get a xml of assigned role of a user
            else if( urlp.cmd.equalsIgnoreCase(GET_ASSIGNED_ROL) || 
                      urlp.cmd.equalsIgnoreCase(GET_ASSIGNED_ROL + XML) ) {

                StringBuffer xmlBuf = new StringBuffer();                
                String xml = "";
                urlp.get_assigned_rol();
                                
                xmlBuf.append("<user_roles>");
                
                xmlBuf.append(urlp.user.getUserAsXML(con));
                xmlBuf.append(urlp.user.getUserRoleAsXML(con));
                xmlBuf.append(dbUtils.getAllRoleAsXML(con, "role_list", prof.root_ent_id));
                
                xmlBuf.append("</user_roles>");

                xml = formatXML(xmlBuf.toString(), "", ENTITY_MODULE);
                
                if (urlp.cmd.equalsIgnoreCase(GET_ASSIGNED_ROL + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                                               
            }
        }
}