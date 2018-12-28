package com.cw.wizbank.content;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.Vector;

import javax.servlet.http.*;

import com.oreilly.servlet.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.*;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeApplication;
// to be remove
import com.cw.wizbank.qdb.qdbException;
import com.cwn.wizbank.utils.CommonLog;


public class SurveyModule extends ServletModule {
    
    public static final String moduleName = "Survey"; 
    ServletUtils sutils = new ServletUtils();

    public void process() throws SQLException, IOException, cwException {

//        String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);

        SurveyReqParam urlp = null;

        urlp = new SurveyReqParam(request, clientEnc, static_env.ENCODING);

        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        
        PrintWriter out = response.getWriter();

        try {
            // if all command need authorized users
            if (prof == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
//                response.sendRedirect(url_relogin);
            }
            // get the survey to play
            else if (urlp.cmd.equalsIgnoreCase("GET_SVY") || urlp.cmd.equalsIgnoreCase("GET_SVY_XML")){
                try{
                    urlp.module();
                    urlp.svy.get(con);
                        
                    // Contains the question ids in the test
                    Vector queIdVec = new Vector();
                    String result = urlp.svy.svyAsXML(con, prof, static_env.INI_DIR_UPLOAD, queIdVec);

                    if(urlp.cmd.equalsIgnoreCase("GET_SVY_XML"))
                        static_env.outputXML(out, result);
                    if(urlp.cmd.equalsIgnoreCase("GET_SVY"))
                        generalAsHtml(result, out, urlp.stylesheet);
                } catch(qdbException e) {
                    throw new cwException(e.toString());
                }
            }
            else if (urlp.cmd.equalsIgnoreCase("DL_SVY_RPT") ||
                urlp.cmd.equalsIgnoreCase("DL_SVY_RPT_XML")) {
              try{                    
                urlp.module();

                StringBuffer result = new StringBuffer(formatXML(urlp.svy.getSurveyReport(con, prof, urlp.startDate, urlp.endDate, null,null, false, urlp.ent_id_lst), "survey_report"));

                if(urlp.cmd.equalsIgnoreCase("DL_SVY_RPT_XML"))
                   static_env.outputXML(out, result.toString());
                if(urlp.cmd.equalsIgnoreCase("DL_SVY_RPT")){
                    response.setHeader("Cache-Control", ""); 
                    response.setHeader("Pragma", ""); 
                    response.setHeader("Content-Disposition", "attachment; filename=survey_report" + urlp.svy.res_id + ".xls;"); 
                    cwUtils.setContentType("application/vnd.ms-excel", response, wizbini);
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }
              } catch(qdbException e) {
                    throw new cwException(e.toString());
              }
            }      
            else if (urlp.cmd.equalsIgnoreCase("DL_SVY_RPT_BY_ITM") ||
                urlp.cmd.equalsIgnoreCase("DL_SVY_RPT_BY_ITM_XML")) {
              try{                    
                urlp.module();
                if (urlp.itm_id > 0 && (urlp.ent_id_lst == null || urlp.ent_id_lst.length == 0) ){
                    aeItem itm = new aeItem();
                    itm.itm_id = urlp.itm_id;
                    itm.itm_run_ind = itm.getRunInd(con);
                    itm.itm_create_run_ind = itm.getCreateRunInd(con);
                    if (itm.itm_run_ind){
                        urlp.ent_id_lst = aeApplication.getAdmittedUserList(con, urlp.itm_id);
                    }else if (itm.itm_create_run_ind && urlp.run_id_lst != null && urlp.run_id_lst.length > 0){
                        Vector vtAdmitUserInRuns = new Vector();
                        for (int i=0; i<urlp.run_id_lst.length; i++){
                            long[] entId = aeApplication.getAdmittedUserList(con, urlp.run_id_lst[i]);
                            for (int j = 0; j < entId.length; j++) {
                                Long tmpLong = new Long(entId[j]);
                                if (!vtAdmitUserInRuns.contains(tmpLong)){
                                    vtAdmitUserInRuns.addElement(tmpLong);
                                }
                            }
                        }
                        if (vtAdmitUserInRuns.size()==0){
                            vtAdmitUserInRuns.addElement(new Long(0));
                        }
                        urlp.ent_id_lst = cwUtils.vec2longArray(vtAdmitUserInRuns);
                    }

                }
                StringBuffer result = new StringBuffer(formatXML(urlp.svy.getSurveyReport(con, prof, urlp.startDate, urlp.endDate, null,null, true, urlp.ent_id_lst), "survey_report"));

                if(urlp.cmd.equalsIgnoreCase("DL_SVY_RPT_BY_ITM_XML")){
                   static_env.outputXML(out, result.toString());
                }else {
                    response.setHeader("Cache-Control", ""); 
                    response.setHeader("Pragma", ""); 
                    response.setHeader("Content-Disposition", "attachment; filename=survey_report" + urlp.svy.res_id + ".csv;"); 
                    cwUtils.setContentType("application/vnd.ms-excel", response, wizbini);
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }
              } catch(qdbException e) {
                    throw new cwException(e.toString());
              }
            }      
            
            else if (urlp.cmd.equalsIgnoreCase("EXPORT_SVY_RPT") ||
                urlp.cmd.equalsIgnoreCase("EXPORT_SVY_RPT_XML")) {
              try{                    
                urlp.module();
                
                String tkh_ids = "";
                try{
                	tkh_ids = request.getParameter("tkh_ids");
                }catch(Exception e){
                }
                
                StringBuffer result = new StringBuffer(formatXML(urlp.svy.getSurveyReport(con, prof, urlp.mov_status, wizbini, tkh_ids), "survey_report"));
                if(urlp.cmd.equalsIgnoreCase("EXPORT_SVY_RPT_XML"))
                   static_env.outputXML(out, result.toString());
                if(urlp.cmd.equalsIgnoreCase("EXPORT_SVY_RPT")){
                    response.setHeader("Cache-Control", ""); 
                    response.setHeader("Pragma", ""); 
                    response.setHeader("Content-Disposition", "attachment; filename=survey_report" + urlp.svy.res_id + ".xls;"); 
                    cwUtils.setContentType("application/vnd.ms-excel", response, wizbini);
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }
              } catch(qdbException e) {
                    throw new cwException(e.toString());
              }
            }      
            
/*
            // get all tna
            else if (urlp.cmd.equalsIgnoreCase("get_tna_lst") || urlp.cmd.equalsIgnoreCase("get_tna_lst_xml")) {
                urlp.module();
                // itm_id = 0
                StringBuffer result = new StringBuffer(formatXML(Survey.getItmModLstAsXML(con, urlp.itm_id, urlp.svy.res_id, urlp.svy.MOD_TYPE_TNA, urlp.svy.vtOrder, urlp.curPage), "applyeasy"));
                if (urlp.cmd.equals("get_tna_lst_xml")) {
                    out.println(result.toString());
                } else {
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }
            }
            // get the list of progress and submission time, notify time
            else if (urlp.cmd.equalsIgnoreCase("GET_NOTIFY_USR") || urlp.cmd.equalsIgnoreCase("GET_NOTIFY_USR_XML")) {
                try{
                    urlp.module();
                    urlp.svy.get(con);
                    urlp.svy.getNotifyLstAsXML(con, prof, urlp.usr_order, urlp.curPage);
    
                    String result = urlp.svy.asXML(con, prof, urlp.dpo_view);   
                    if(urlp.cmd.equalsIgnoreCase("GET_NOTIFY_USR_XML"))
                        static_env.outputXML(out, result);
                    if(urlp.cmd.equalsIgnoreCase("GET_NOTIFY_USR"))
                        generalAsHtml(result, out, urlp.stylesheet);
                } catch(qdbException e) {
                    throw new cwException(e.toString());
                }
            }
            // insert progress record for picked user and redirect to messaging
            else if (urlp.cmd.equalsIgnoreCase("PICK_NOTIFY_USR")) {
                try{
                    urlp.module();
                    urlp.messaging();
                    long[] usr_ent_id_lst = dbUtils.string2long(sutils.split(urlp.ent_ids, "~"));
                    urlp.svy.pickUsr(con, usr_ent_id_lst);
                    con.commit();
                    long[] ent_ids = dbUserGroup.constructEntId(con, urlp.ent_ids);
                    long[] cc_ent_ids = dbUserGroup.constructEntId(con, urlp.cc_ent_ids);
                    urlp.url_redirect_param += "&ent_id="+dbUtils.longArray2String(ent_ids,"~") + "&cc_ent_id="+dbUtils.longArray2String(cc_ent_ids,"~");
                    urlp.url_redirect_param += "&usr_id="+prof.usr_id;

                    try{
                        System.out.println( returnByUrl( "http://" + request.getServerName()+":"+request.getServerPort()+"/servlet/Dispatcher", urlp.url_redirect_param, request ) );
                    }catch(Exception e) {
                        throw new cwException("Failed to get the message detials : " + e);
                    }

                    msgBox(MSG_STATUS, new cwSysMessage("XMG001"), urlp.url_success, out); 
                    //response.sendRedirect(urlp.url_success);
                } catch(qdbException e) {
                    throw new cwException(e.toString());
                }
            }
            else if (urlp.cmd.equalsIgnoreCase("PICK_TNA")) {
                    urlp.module();
                    DbItemResources dbItmRes = new DbItemResources();           
                    dbItmRes.ire_itm_id = urlp.itm_id;
                    dbItmRes.ire_res_id = urlp.svy.res_id;
                    dbItmRes.ire_type = urlp.svy.MOD_TYPE_TNA;
                    dbItmRes.ins(con);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
            }
            else if (urlp.cmd.equalsIgnoreCase("DROP_TNA")) {
                    urlp.module();
                    DbItemResources dbItmRes = new DbItemResources();           
                    dbItmRes.ire_itm_id = urlp.itm_id;
                    dbItmRes.ire_res_id = urlp.svy.res_id;
                    dbItmRes.ire_type = urlp.svy.MOD_TYPE_TNA;
                    dbItmRes.del(con);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
            }
            */
            /*
            // get the evns that attach to that run
            else if (urlp.cmd.equalsIgnoreCase("get_evn_lst") || urlp.cmd.equalsIgnoreCase("get_evn_lst_xml")) {
                try{
                        urlp.item();
                        urlp.module();
                        boolean checkStatus;
                        long[] usr_ent_ids={0};
                        usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);

                        
//                        if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))
//                            checkStatus = false;
//                        else
//                            checkStatus = true;
//                        
                        
                        StringBuffer tmpResult = new StringBuffer();

                        if(urlp.itm.tnd_id != 0) {
                            aeTreeNode tnd = new aeTreeNode();
                            tnd.tnd_id = urlp.itm.tnd_id;
                            
                            //access control
                            tnd.tnd_cat_id = tnd.getCatalogId(con);
                            AcTreeNode actnd = new AcTreeNode(con);
                            if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role,
                                                        tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
                                throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                            }
                            
//                            tnd.get(con, usr_ent_ids, checkStatus);
                            tnd.get(con);
                            tmpResult.append(tnd.contentAsXML(con));
                        }
                        else{
                        }
                        
                        urlp.itm.getItem(con);
            
                        tmpResult.append(urlp.itm.contentAsXML(con));
                        tmpResult.append(Survey.ItmRateAsXML(con, urlp.itm_id, urlp.svy.res_id));
                        
                        StringBuffer result = new StringBuffer(formatXML(tmpResult.toString(),"applyeasy"));
                        if (urlp.cmd.equals("get_evn_lst_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet);
                        }
                } catch(qdbException e) {
                    throw new cwException(e.toString());
                }

            }
            */
            /*
            // xml page for insert a tna notify
            else if(urlp.cmd.equalsIgnoreCase("ins_notify") || urlp.cmd.equalsIgnoreCase("ins_notify_xml")) {
System.out.println("Insert Notify Page");
                HttpSession sess = request.getSession(true);
                urlp.ins_notify();
                
                boolean checkStatus;
                long[] usr_ent_ids={0};
                usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))
                    checkStatus = false;
                else
                    checkStatus = true;

                StringBuffer result = new StringBuffer();

                if(urlp.tnd_id != 0) {
                    aeTreeNode tnd = new aeTreeNode();
                    tnd.tnd_id = urlp.tnd_id;
                    
                    //access control
                    tnd.tnd_cat_id = tnd.getCatalogId(con);
                    AcTreeNode actnd = new AcTreeNode(con);
                    if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role,
                                                tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
                        throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                    }
                    
//                    tnd.get(con, usr_ent_ids, checkStatus);
                    tnd.get(con);
                    result.append(tnd.contentAsXML(con));
                }

                result.append(Survey.insNotifyXML(con, sess, urlp.mod_id, urlp.refresh));

                if (urlp.cmd.equals("ins_notify_xml")) {
                    out.println(formatXML(result.toString(),"applyeasy"));
                } else {
                    generalAsHtml(formatXML(result.toString(),"applyeasy"), out, urlp.stylesheet);
                }
            }
  */
  /*
            // entity list for picking user to receive the tna notify
            else if(urlp.cmd.equalsIgnoreCase("pick_ent_lst") || urlp.cmd.equalsIgnoreCase("pick_ent_lst_xml")) {
System.out.println("Pick entity");                
                HttpSession sess = request.getSession(true);
                urlp.pick_ent_lst();
                
                String xml = Survey.pickEntityList(con, sess, prof, urlp.type, urlp.cur_page, urlp.pagesize, urlp.timestamp, urlp.ent_id);

                if (urlp.cmd.equals("pick_ent_lst_xml")) {
                    out.println(formatXML(xml,"applyeasy"));
                } else {
                    generalAsHtml(formatXML(xml,"applyeasy"), out, urlp.stylesheet);
                }
            
            }
            
            // pick entity command used in the pick entity id list to store the picked entity id in session
            else if(urlp.cmd.equalsIgnoreCase("pick_ent")) {
System.out.println("Pick Entity");                
                HttpSession sess = request.getSession(true);
                urlp.pick_ent();
                if( urlp.ent_ids == null || urlp.ent_ids.trim().length() == 0 )
                    urlp.ent_ids = "";
                if( urlp.type.equalsIgnoreCase("REC") )
                    sess.setAttribute("Survey_MSG_REC_ENT_ID", urlp.ent_ids);
                else
                    sess.setAttribute("Survey_MSG_CC_ENT_ID", urlp.ent_ids);
                
                
                // put value into session
                if( urlp.msg_method == null ) urlp.msg_method = "";
                sess.setAttribute("Survey_MSG_METHOD", urlp.msg_method);
            
                if( urlp.msg_subject == null )   urlp.msg_subject ="";
                sess.setAttribute("Survey_MSG_SUBJECT", urlp.msg_subject);
                
                if( urlp.msg_body == null ) urlp.msg_body ="";
                sess.setAttribute("Survey_MSG_BODY", urlp.msg_body);
                
                if( urlp.dd == null ) urlp.dd ="";
                sess.setAttribute("Survey_MSG_TIME_DD", urlp.dd);

                if( urlp.yy == null ) urlp.yy ="";
                sess.setAttribute("Survey_MSG_TIME_YY", urlp.yy);

                if( urlp.mm == null ) urlp.mm ="";
                sess.setAttribute("Survey_MSG_TIME_MM", urlp.mm);
                
            
                response.sendRedirect(urlp.url_success);
            
            }
            
            // picked entity id list in xml
            else if( urlp.cmd.equalsIgnoreCase("picked_ent_lst") || urlp.cmd.equalsIgnoreCase("picked_ent_lst_xml") ) {
System.out.println("Picked entity list");
                HttpSession sess = request.getSession(true);
                urlp.pick_ent_lst();
                
                String xml = Survey.pickedEntityList(con, sess, prof, urlp.msg_subject, urlp.msg_body, urlp.msg_method, urlp.msg_send_time, urlp.type);

                if (urlp.cmd.equals("picked_ent_lst_xml")) {
                    out.println(formatXML(xml, "applyeasy"));
                } else {
                    generalAsHtml(formatXML(xml,"applyeasy"), out, urlp.stylesheet);
                }

            }
    */        
            else {
                throw new cwException("Invalid Command || command retired");
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
    /*
  private String formatXML(String in, loginProfile prof) {
    StringBuffer outBuf = new StringBuffer(2500);

    outBuf.append(cwUtils.xmlHeader).append(cwUtils.NEWL);    
    outBuf.append("<applyeasy>").append(cwUtils.NEWL);
    if(prof!=null)
        outBuf.append(prof.asXML()).append(cwUtils.NEWL);
    outBuf.append(in).append(cwUtils.NEWL);
    outBuf.append("</applyeasy>");
    
    String out = new String(outBuf);
    return out;
  }
*/
  public static long[] usrGroups(long usr_ent_id, Vector v) {
    long[] list = new long[v.size() + 1];
    list[0] = usr_ent_id;
    for(int i=0;i<v.size();i++)
        list[i+1] = ((Long) v.elementAt(i)).longValue();
        
    return list;
  }



    //call the url by http connection
    public static String returnByUrl(String url, String args, HttpServletRequest request)
        throws Exception {
                                    
            StringBuffer xml = new StringBuffer();
            
            try{
                args += "&";

                StringBuffer requestParam = new StringBuffer();
                int index = args.indexOf("&");
                while( index > 0 ) {
                    String element = args.substring(0, index);
                    int subIndex = element.indexOf("=");
                    int strLength = element.length();
                    requestParam.append(URLEncoder.encode(element.substring(0,subIndex)));
                    requestParam.append("=");
                    requestParam.append(URLEncoder.encode(element.substring(subIndex+1,strLength)));
                    requestParam.append("&");
                    args = args.substring(index+1);
                    index = args.indexOf("&");
                }
                if(url != null && url.length() > 0){
                    xml.append( SendHttpRequest.sendUrl(url, requestParam.toString(), null, request, null));
                }

            } catch (Exception e) {                
                CommonLog.error(e.getMessage(),e);
            }
            
            return xml.toString();
        }


        
}

