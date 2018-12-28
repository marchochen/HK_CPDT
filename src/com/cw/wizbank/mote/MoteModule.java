package com.cw.wizbank.mote;

import java.io.*;
import java.sql.*;
import java.util.Vector;
import javax.servlet.http.*;

import com.oreilly.servlet.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.*;

// to be remove
import com.cw.wizbank.qdb.qdbException;


public class MoteModule extends ServletModule {
    
    public static final String moduleName = "Mote"; 
    ServletUtils sutils = new ServletUtils();

    public void process() throws SQLException, IOException, cwException{

//        String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);

        MoteReqParam urlp = null;

        urlp = new MoteReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        
        PrintWriter out = response.getWriter();

        try {
            // if all command need authorized users
            if (prof == null) {
//                response.sendRedirect(url_relogin);
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            }
            else if (urlp.cmd.equalsIgnoreCase("get_resp_mote_lst") || urlp.cmd.equalsIgnoreCase("get_resp_mote_lst_xml")){
                try{
                    urlp.mote(clientEnc, static_env.ENCODING);
                    urlp.pagination();
                    HttpSession sess = request.getSession(true);

                    StringBuffer result = new StringBuffer(formatXML(urlp.mote.getMoteLstAsXML(con, sess, prof, prof.root_ent_id, urlp.mote_status, urlp.itm_id, urlp.cwPage, true), null,"moteModule"));

                    if(urlp.cmd.equalsIgnoreCase("get_resp_mote_lst_xml"))
                        static_env.outputXML(out, result.toString());
                    if(urlp.cmd.equalsIgnoreCase("get_resp_mote_lst")){
                        generalAsHtml(result.toString(), out, urlp.stylesheet);
                    }
                } catch(qdbException e) {
                    throw new cwException(e.toString());
                }
            }
            else if (urlp.cmd.equalsIgnoreCase("get_mote_lst_by_itm") || urlp.cmd.equalsIgnoreCase("get_mote_lst_by_itm_xml")){
                try{
                    urlp.mote(clientEnc, static_env.ENCODING);
                    urlp.pagination();
                    HttpSession sess = request.getSession(true);

                    String xml = urlp.mote.getMoteLstAsXML(con, sess, prof, prof.root_ent_id, urlp.mote_status, urlp.itm_id, urlp.cwPage, false);
                    StringBuffer result = new StringBuffer(formatXML(xml , null,"moteModule"));

                    if(urlp.cmd.equalsIgnoreCase("get_mote_lst_by_itm_xml"))
                        static_env.outputXML(out, result.toString());
                    if(urlp.cmd.equalsIgnoreCase("get_mote_lst_by_itm")){
                        generalAsHtml(result.toString(), out, urlp.stylesheet);
                    }
                } catch(qdbException e) {
                    throw new cwException(e.toString());
                }
            }
            else if (urlp.cmd.equalsIgnoreCase("get_mote") || urlp.cmd.equalsIgnoreCase("get_mote_xml")){
                try{
                    urlp.mote(clientEnc, static_env.ENCODING);
                    StringBuffer result = new StringBuffer(formatXML(urlp.mote.getMoteAsXML(con, urlp.mote.imt_id, urlp.itm_id, prof.root_ent_id).toString() , null,"moteModule"));

                    if(urlp.cmd.equalsIgnoreCase("get_mote_xml"))
                    static_env.outputXML(out, result.toString());
                    if(urlp.cmd.equalsIgnoreCase("get_mote")){
                        generalAsHtml(result.toString(), out, urlp.stylesheet);
                    }
                } catch(qdbException e) {
                    throw new cwException(e.toString());
                }
            }
            else if (urlp.cmd.equalsIgnoreCase("get_top_mote_by_itm") || urlp.cmd.equalsIgnoreCase("get_top_mote_by_itm_xml")){
                try{
                    urlp.mote(clientEnc, static_env.ENCODING);
                    long imt_id = 0;

                    Vector vtImt = urlp.mote.getMoteLstByItm(con, urlp.itm_id);
                    if (vtImt != null && vtImt.size() > 0){
                        imt_id = ((Mote)vtImt.elementAt(0)).imt_id;

                        StringBuffer result = new StringBuffer(formatXML(urlp.mote.getMoteAsXML(con, imt_id, urlp.itm_id, prof.root_ent_id).toString() , null,"moteModule"));

                        if(urlp.cmd.equalsIgnoreCase("get_top_mote_by_itm_xml"))
                        static_env.outputXML(out, result.toString());
                        if(urlp.cmd.equalsIgnoreCase("get_top_mote_by_itm")){
                            generalAsHtml(result.toString(), out, urlp.stylesheet);
                        }

                    }else{
                        msgBox(MSG_STATUS, new cwSysMessage("MOT005"), urlp.url_failure, out);                 
                    }
                } catch(qdbException e) {
                    throw new cwException(e.toString());
                }
            }

            else if (urlp.cmd.equalsIgnoreCase("upd_mote")){
                    urlp.mote(clientEnc, static_env.ENCODING);
                    urlp.mote.updMote(con, prof.usr_id, urlp.itm_type);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
            }

            else if (urlp.cmd.equalsIgnoreCase("upd_mote_status")){
                    urlp.mote(clientEnc, static_env.ENCODING);
                    urlp.mote.updMoteStatus(con, prof.usr_id);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
            }
            // for selfstudy only
            else if (urlp.cmd.equalsIgnoreCase("ins_mote")){
                    urlp.mote(clientEnc, static_env.ENCODING);
                    urlp.mote.insMote(con, urlp.mote.imd_id, prof.usr_id);
                    con.commit();
                    msgBox(MSG_STATUS, new cwSysMessage("MOT001"), urlp.url_success, out);                 
            }
            // for selfstudy only
            else if (urlp.cmd.equalsIgnoreCase("ins_prep_mote") || urlp.cmd.equalsIgnoreCase("ins_prep_mote_xml")){
                    urlp.mote(clientEnc, static_env.ENCODING);
                    StringBuffer xml = new StringBuffer();
                    xml.append("<mote imd_id=\"").append(urlp.mote.imd_id).append("\" />");
                    StringBuffer result = new StringBuffer(formatXML(xml.toString(), null,"moteModule"));

                    if(urlp.cmd.equalsIgnoreCase("ins_prep_mote_xml"))
                    static_env.outputXML(out, result.toString());
                    if(urlp.cmd.equalsIgnoreCase("ins_prep_mote")){
                        generalAsHtml(result.toString(), out, urlp.stylesheet);
                    }
            }
            // for selfstudy only
            else if (urlp.cmd.equalsIgnoreCase("del_multi_mote")){
                    urlp.mote(clientEnc, static_env.ENCODING);
                    Vector vtImt = urlp.mote.getMoteLstByItm(con, urlp.itm_id);
                    if (urlp.imt_id_lst == null || urlp.imt_id_lst.length >= vtImt.size()){
                        msgBox(MSG_ERROR, new cwSysMessage("MOT004"), urlp.url_failure, out);                 
                    }else{
                        for (int i=0; urlp.imt_id_lst != null && i<urlp.imt_id_lst.length ; i++){
                            urlp.mote.imt_id = urlp.imt_id_lst[i];                       
                            urlp.mote.del(con);
                        }
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                    }
            }
            // for selfstudy only
            /*
            else if (urlp.cmd.equalsIgnoreCase("del_mote")){
                
                    urlp.mote(clientEnc, static_env.ENCODING);
                    urlp.mote.del(con);
                    con.commit();
                    msgBox(MSG_STATUS, new cwSysMessage("MOT003"), urlp.url_success, out);                 
            }
*/

            // testing only
            else if (urlp.cmd.equalsIgnoreCase("ins_mote_def_n_mote")){
                    urlp.mote(clientEnc, static_env.ENCODING);
                    urlp.mote.insMoteDefaultNMote(con, prof.usr_id);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
            }
            else {
                throw new cwException("Invalid Command");
            }
        }catch (cwSysMessage se) {
            try {
                 con.rollback();
                 msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
             } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
             }
        }
        
    }
    
}

