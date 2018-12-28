package com.cw.wizbank.accesscontrol;

import java.io.*;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;

import javax.servlet.http.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.ServletModule;
import com.cwn.wizbank.utils.CommonLog;
//import com.cw.wizbank.accesscontrol.AcHomePage;
//import com.cw.wizbank.accesscontrol.AcEntity;

public class AccessControlModule extends ServletModule {
    
    //session keys
    public final static String AC_XSL_QUESTION = "AC_XSL_QUESTION";
    
    //servlet API
    public final static String GET_ROL_FTN = "get_rol_ftn";
    public final static String SAVE_ROL_FTN = "save_rol_ftn";
    public final static String GET_GRANTED_FTN = "get_granted_ftn";
    public final static String GET_ASSIGNED_ENT = "get_assigned_ent";
    public final static String ASSIGN_ENT = "assign_ent";
    public final static String REMOVE_ENT = "remove_ent";
    public final static String GET_ROL_FTN_X = "get_rol_ftn_x";
    public final static String SAVE_ROL_FTN_X = "save_rol_ftn_x";
    public final static String XML          =   "_xml";
    public final static String TEST_AC_WRK = "test_ac_wrk";
        
    private final static String ACCESS_CONTROL = "access_control";
    private final static String HOME_PAGE = "home_page";
    
    public AccessControlModule() { ; }
    
    public void process()
        throws SQLException, IOException, cwException {        

//            String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);
            
            if (prof == null) {
//                response.sendRedirect(url_relogin);
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            }

            PrintWriter out = response.getWriter();
            
            AccessControlReqParam urlp = new AccessControlReqParam(request, clientEnc, static_env.ENCODING);
        
           // AcHomePage acHomePage = new AcHomePage(con);

            HttpSession sess = request.getSession(true);
            CommonLog.debug("cmd = " + urlp.cmd);
            if(urlp.cmd == null) {
             
                throw new cwException("Invalid Command");
            
            }
            // save homepage functions for a role
            // 1st remove the existing homepage function
            // then insert the function according to ftn_ext_ids
            else if( urlp.cmd.equalsIgnoreCase(SAVE_ROL_FTN) ) {                
                urlp.save_rol_ftn();
               // acHomePage.save_rol_ftn(urlp.rol_ext_id, urlp.ftn_ext_ids, prof.usr_id);
                con.commit();
                msgBox(MSG_STATUS, new cwSysMessage("ACL001"), urlp.url_success, out);                 
            }
            
            // get a xml of all roles and homepage functions of one of the roles
            else if( urlp.cmd.equalsIgnoreCase(GET_ROL_FTN) || 
                      urlp.cmd.equalsIgnoreCase(GET_ROL_FTN + XML) ) {

                StringBuffer xmlBuf = new StringBuffer();                
                String xml;
                urlp.get_rol_ftn();
                
               // xmlBuf.append(acHomePage.getRoleFunctionAsXML(prof, urlp.rol_ext_id, prof.root_ent_id, wizbini.cfgTcEnabled));
                xml = formatXML(xmlBuf.toString(), ACCESS_CONTROL);
                
                if (urlp.cmd.equalsIgnoreCase(GET_ROL_FTN + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                                               
            }

            // get a xml of all roles and homepage functions of one of the roles
            else if( urlp.cmd.equalsIgnoreCase(GET_GRANTED_FTN) || 
                      urlp.cmd.equalsIgnoreCase(GET_GRANTED_FTN + XML) ) {

                StringBuffer xmlBuf = new StringBuffer();                
                String xml;
                urlp.get_granted_ftn();
                
               // xmlBuf.append(acHomePage.getGrantedFunctionAsXML(urlp.rol_ext_id, prof, wizbini.cfgTcEnabled));
                xml = formatXML(xmlBuf.toString(), HOME_PAGE);
                
                if (urlp.cmd.equalsIgnoreCase(GET_GRANTED_FTN + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                                               
            }

            // get a xml of assigned entity role of an entity
            // e.g. xml of assigned approver of user group
            /*
            else if( urlp.cmd.equalsIgnoreCase(GET_ASSIGNED_ENT) || 
                      urlp.cmd.equalsIgnoreCase(GET_ASSIGNED_ENT + XML) ) {

                StringBuffer xmlBuf = new StringBuffer();                
                String xml = "";
                urlp.get_assigned_ent();
                
                if(urlp.instance_type != null && urlp.rol_ext_id != null) {
                    if(urlp.instance_type.equalsIgnoreCase("ENTITY")) {
                        AcEntity acent = new AcEntity(con);
                        if(urlp.rol_ext_id.equalsIgnoreCase(dbRegUser.ROLE_USR_APPR)) {
                            xmlBuf.append(acent.getAssignedApproverAsXML(urlp.instance_id));
                        }
                    }
                }
                xml = formatXML(xmlBuf.toString(), dbUtils.getAllRoleAsXML(con, "all_role_list", prof.root_ent_id), ACCESS_CONTROL);
                
                if (urlp.cmd.equalsIgnoreCase(GET_ASSIGNED_ENT + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }                                               
            }
            
            // assign a list of entities to an instance
            else if( urlp.cmd.equalsIgnoreCase(ASSIGN_ENT) ) {                
                urlp.assign_ent();
                                
                AccessControlWZB acl = new AccessControlWZB();
                dbEntity ent;
                
                for(int i=0; i<urlp.ent_id_lst.length; i++) {
                    
                    try {
                        //get the entity type in ent_id_lst
                        ent = new dbEntity();
                        ent.ent_id = urlp.ent_id_lst[i];
                        ent.get(con);
                    }
                    catch(qdbException e) {
                        throw new cwException(e.getMessage());
                    }
                    
                    //if the entity is user, check the user role relation existance
                    //if not exists, grant user the role
                    if(ent.ent_type.equalsIgnoreCase(dbEntity.ENT_TYPE_USER)) {
                        if(!acl.hasUserRole(con, urlp.ent_id_lst[i], urlp.rol_ext_id)) {
                            acl.assignUser2Role(con, urlp.ent_id_lst[i], urlp.rol_ext_id);
                        }
                    }
                    
                    //assign the entity, role to instance
                    if(urlp.instance_type.equalsIgnoreCase("ENTITY")) {
                        AcEntity acent = new AcEntity(con);
                        Timestamp curTime = cwSQL.getTime(con);
                        
                        acent.assignEntity(urlp.instance_id, urlp.ent_id_lst[i],
                                            urlp.rol_ext_id, false, prof.usr_ste_usr_id, 
                                            curTime);
                    }
                }
                con.commit();
                response.sendRedirect(urlp.url_success);
            }
            // assign a list of entities to an instance
            else if( urlp.cmd.equalsIgnoreCase(REMOVE_ENT) ) {                
                urlp.remove_ent();
                
                if(urlp.ent_id_lst != null) {
                    for(int i=0; i<urlp.ent_id_lst.length; i++) {
                            
                        //assign the entity, role to instance
                        if(urlp.instance_type.equalsIgnoreCase("ENTITY")) {
                            AcEntity acent = new AcEntity(con);

                            acent.rmEntity(urlp.instance_id, urlp.ent_id_lst[i],
                                                urlp.rol_ext_id);
                        }
                    }
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
            }
            */
            // get the functions of a role
            else if( urlp.cmd.equalsIgnoreCase(GET_ROL_FTN_X) ) {                
                urlp.get_rol_ftn_x();
                
                AcRoleFunction acrolftn = new AcRoleFunction(con);
                
                Hashtable h = acrolftn.getRoleFunction(urlp.rol_ext_id);
                Vector v_ftn_types = (Vector) h.get(AcRoleFunction.FTN_TYPES);
                Vector v_ftn_ext_ids = (Vector) h.get(AcRoleFunction.FTN_EXT_IDS);
                Vector v_ftn_ids = (Vector) h.get(AcRoleFunction.FTN_IDS);
                Vector v_ftn_pick_inds = (Vector) h.get(AcRoleFunction.FTN_PICK_INDS);
            
            
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Role Function</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<p align=\"center\"><b>Role Function</b></p>");
                out.println("<form name=\"form1\" method=\"post\" action=\"../servlet/Dispatcher\">");
                out.println("  <div align=\"center\"><b>rol_ext_id:</b> ");
                out.println("    <input type=\"text\" name=\"rol_ext_id\" value=\"" + urlp.rol_ext_id + "\">");
                out.println("    <input type=\"submit\" name=\"go\" value=\"GO\">");
                out.println("    <input type=\"hidden\" name=\"module\" value=\"accesscontrol.AccessControlModule\">");
                out.println("    <input type=\"hidden\" name=\"cmd\" value=\"get_rol_ftn_x\">");
                out.println("  </div>");
                out.println("</form>");
                out.println("<hr>");
                out.println("<form name=\"form2\" method=\"post\" action=\"../servlet/Dispatcher\">");
                out.println("  <table width=\"70%\" border=\"1\" align=\"center\">");
                out.println("    <tr>");
                out.println("      <td width=\"14%\">");
                out.println("        <div align=\"center\"><b>grant</b></div>");
                out.println("        <input type=\"hidden\" name=\"rol_ext_id\" value=\"" + urlp.rol_ext_id + "\">");
                out.println("        <input type=\"hidden\" name=\"module\" value=\"accesscontrol.AccessControlModule\">");
                out.println("        <input type=\"hidden\" name=\"cmd\" value=\"save_rol_ftn_x\">");
                out.println("      </td>");
                out.println("      <td width=\"46%\"> ");
                out.println("        <div align=\"center\"><b>ftn_ext_id</b></div>");
                out.println("                   </td>");
                out.println("      <td width=\"40%\"> ");
                out.println("        <div align=\"center\"><b>ftn_type</b></div>");
                out.println("      </td>");
                out.println("    </tr>");

                for(int i=0; i<v_ftn_ext_ids.size(); i++) {
                    String ftn_type = (String)v_ftn_types.elementAt(i);
                    String ftn_ext_id = (String)v_ftn_ext_ids.elementAt(i);
                    boolean ftn_pick_ind = ((Boolean)v_ftn_pick_inds.elementAt(i)).booleanValue();
                    
                    out.println("    <tr>");
                    out.println("      <td width=\"14%\"> ");
                    out.println("        <div align=\"center\">");
                    out.println("          <input type=\"checkbox\" name=\"ftn_ext_id\" value=\"" + ftn_ext_id + "\"");
                    if(ftn_pick_ind) {
                        out.println(" checked");
                    }
                    out.println(" >");
                    out.println("        </div>");
                    out.println("      </td>");
                    out.println("      <td width=\"46%\">");
                    out.println("        <div align=\"center\"><b>" + ftn_ext_id + "</b></div>");
                    out.println("      </td>");
                    out.println("      <td width=\"40%\">");
                    out.println("        <div align=\"center\"><b>" + ftn_type + "</b></div>");
                    out.println("      </td>");
                    out.println("    </tr>");
                }                
                
                
                out.println("  </table>");
                out.println("  <p align=\"center\"> ");
                out.println("    <input type=\"submit\" name=\"save\" value=\"SAVE\">");
                out.println("  </p>");
                out.println("</form>");
                out.println("<p>&nbsp; </p>");
                out.println("</body>");
                out.println("</html>");
            }
            else if( urlp.cmd.equalsIgnoreCase(SAVE_ROL_FTN_X) ) {
                urlp.save_rol_ftn_x();
                AcRoleFunction acrolftn = new AcRoleFunction(con);
                acrolftn.saveRoleFunction(urlp.rol_ext_id, urlp.ftn_ext_ids);
                /*
                out.println("rol_ext_id = " + urlp.rol_ext_id + "<BR>");
                out.println("ftn_ext_id <BR>");
                for(int i=0; i<urlp.ftn_ext_ids.length; i++) {
                    out.println(urlp.ftn_ext_ids[i] + "<BR>");
                }
                */
                con.commit();
                response.sendRedirect("../servlet/Dispatcher?module=accesscontrol.AccessControlModule&cmd=" + GET_ROL_FTN_X + "&rol_ext_id=" + urlp.rol_ext_id);
            }
            /*
            else if( urlp.cmd.equalsIgnoreCase(TEST_AC_WRK) ) {
                try {
                    urlp.test_ac_wrk();
                    aeApplication app = new aeApplication();
                    app.app_id = urlp.app_id;
                    app.getWithItem(con);
                    aeTemplate tpl = new aeTemplate();
                    aeItem itm = new aeItem();
                    itm.itm_id = app.app_itm_id;
                    itm.get(con);
                    tpl.tpl_id = itm.getTemplateId(con, aeTemplate.WORKFLOW);
                    tpl.get(con);
                    AcWorkFlow acWorkFlow = new AcWorkFlow(con, tpl.tpl_xml);
                    out.println("acc_control_result = " + acWorkFlow.checkPrivilege(prof.usr_ent_id, prof.current_role, urlp.pid, urlp.sid, urlp.aid, app));
                }
                catch(cwSysMessage se) {
                    out.println("cwSysMessage: " + se.getMessage());
                }
                catch(qdbException e) {
                    out.println("qdbException: " + e.getMessage());
                }
            }
            */
        }
}