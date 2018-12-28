package com.cw.wizbank.message;

import java.io.*;
import java.sql.*;
import java.util.*;


import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.db.*;
import com.cw.wizbank.ServletModule;






public class MessageModule extends ServletModule {
        
    
    //Insert Success Message
    public final static String INSERT_SUCCESS   =   "INSERT SUCCESS";
    
    // Recipient Type
    public final static String CARBONCOPY       =   "CARBONCOPY";
    public final static String RECIPIENT        =   "RECIPIENT";

    
    // Message type
    public final static String JI               =   "JI";
    public final static String REMINDER         =   "REMINDER";
    
  /*
    //for qdb auto login
    public final static String login_url        =   "http://202.134.90.203:84/servlet/qdb.qdbAction?cmd=auto_login";    
  */  
    public void process()
        throws SQLException, IOException, cwException {

            //String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);
            
            MessageReqParam urlp = new MessageReqParam(request, clientEnc, static_env.ENCODING);
            
            PrintWriter out = response.getWriter();    
            

            // command no need to login
            if (prof == null) {

                if( urlp.cmd.equalsIgnoreCase("send_msg") ) {
                    urlp.send_msg();
                    Message.send(con, urlp.msg_id, static_env, request);
                    out.println("end");
                    return;
                } else if( urlp.cmd.equalsIgnoreCase("etray") ) {
                    String eTrayMessage = request.getParameter("eTrayMessage");
                    out.println(xmlObj.checkEtray(eTrayMessage));
                } else {

                    response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
                    
                }
                
            } else {                

                if(urlp.cmd == null) {

                    throw new cwException("Invalid Command");
                
                } else if( urlp.cmd.equalsIgnoreCase("preview") 
                     ||  urlp.cmd.equalsIgnoreCase("preview_xml")) {

                        urlp.preview();
                        String xml = Message.preview( con, urlp.itm_id, urlp.msg_type, urlp.msg_subtype, 
                                                      urlp.dbMsg, urlp.id, urlp.id_type, urlp.url_cmd, urlp.url_redirect, prof, static_env );

                        xml = formatXML(xml,"message");
                        if( urlp.cmd.equalsIgnoreCase("preview") ) {
                            generalAsHtml(xml, out, urlp.stylesheet);
                        } else
                            static_env.outputXML(out, xml);

                        return;
                        
                } else if( urlp.cmd.equalsIgnoreCase("ins_recip") ) {

                    Message msg = new Message();
                    urlp.ins_recip();

                    String[] entIdStr = dbUtils.split(urlp.ent_id_str, "~");
                    if(entIdStr != null)
                        msg.rec_ent_id = dbUtils.string2long(entIdStr);
                    String[] ccEntIdStr = dbUtils.split(urlp.cc_ent_id_str, "~");
                    if(ccEntIdStr != null)
                        msg.cc_ent_id = dbUtils.string2long(ccEntIdStr);

                    if(urlp.ji_status == 1) {
                        msg.constructTemplateId(con, urlp.msg_type, urlp.msg_subtype);
                        msg.cloneJI(con, urlp.itm_id, urlp.msg_type, Message.replaceToCurrentTime(con,urlp.send_date,0), prof);
                    }

                    long ji_msg_id = msg.msg_id;
                    long[] msg_ids = DbMgItmSelectedMessage.getMessageIds(con, urlp.itm_id, "REMINDER");
                    msg.msg_id = msg_ids[0];

                    if( urlp.rem_status == 1 ) {
                        long[] cc_ent_id = msg.cc_ent_id;
                        Vector ccEntIdVec = DbMgView.getItemMessageRecipient(con, urlp.itm_id, REMINDER, CARBONCOPY);
                        msg.cc_ent_id = cwUtils.vec2longArray(ccEntIdVec);

                        msg.removeCarbonCopy(con, urlp.itm_id, REMINDER);
                        msg.cc_ent_id = cc_ent_id;
                        DbMgView.resetRecipientStatus(con, urlp.itm_id);
                        msg.insertRecipients(con, urlp.itm_id, REMINDER, Message.replaceToCurrentTime(con, urlp.rem_send_date, 10), prof);
                        
                        msg.updateSender(con, prof);

                    } else if( urlp.rem_status == 2 ) {

                        msg.removeRecipients(con, urlp.itm_id, REMINDER);
                        msg.removeCarbonCopy(con, urlp.itm_id, REMINDER);
                    }

                    con.commit();
                    /*
                    if( qdbAction.msgThread != null ) {
                        qdbAction.msgThread.addMessageIdToQueue(ji_msg_id);
                        qdbAction.msgThread.addMessageIdToQueue(msg.msg_id);
                    }
                    */
                    out.println(INSERT_SUCCESS);

                } else if( urlp.cmd.equalsIgnoreCase("del_recip") ) {
                    urlp.del_recip();                    
                    //should find only 1 message id 
                    long[] msgIds = DbMgItmSelectedMessage.getMessageIds(con, urlp.itm_id, REMINDER);   
                    
                    long[] rec_ent_id;
                    String[] entIdStr = dbUtils.split(urlp.ent_id_str, "~");
                    if( entIdStr != null  && msgIds != null ) {
                        rec_ent_id = dbUtils.string2long(entIdStr);
                        DbMgRecHistory.delRecords(con, msgIds[0], rec_ent_id);
                        DbMgRecipient.delRecords(con, msgIds[0], rec_ent_id);
                    }
                    
                    con.commit();
                    //msgBox(MSG_STATUS, new cwSysMessage("XMG001"), urlp.url_success, out); 
                    response.sendRedirect(urlp.url_success);
                    
                    return;
                    
                } else if( urlp.cmd.equalsIgnoreCase("ins_ji") ) {

                    Message msg = new Message();
                    urlp.ins_ji();
                    
                    //find the template depend on the selected type and subtype
                    msg.constructTemplateId(con, urlp.msg_type, urlp.msg_subtype);

                    // insert message
                    msg.ins(con, urlp.usr_id, urlp.dbMsg, urlp.params);

                    //  insert a record into item selected message table
                    DbMgItmSelectedMessage dbMgItm = new DbMgItmSelectedMessage();
                    dbMgItm.ism_itm_id = urlp.itm_id;
                    dbMgItm.ism_msg_id = msg.msg_id;
                    dbMgItm.ism_type = urlp.msg_type;
                    dbMgItm.ins(con);
                    
                    // if reminder not exist for this item, create Reminder                    
                    long msg_id = DbMgView.getLatestItmMessage(con, urlp.itm_id, REMINDER);
                    if( msg_id == 0 ) {
                        urlp.params.remove(2);
                        msg.constructTemplateId(con, REMINDER, urlp.msg_subtype);
                        urlp.dbMsg.msg_subject = urlp.reminder_subject;//"Reminder";
                        msg.ins(con, urlp.usr_id, urlp.dbMsg, urlp.params);
                        dbMgItm.ism_msg_id = msg.msg_id;
                        dbMgItm.ism_type = REMINDER;
                        dbMgItm.ins(con);
                    }

                    con.commit();
                    response.sendRedirect(urlp.url_success);

                    return;
                    
                } else if( urlp.cmd.equalsIgnoreCase("upd_ji") ) {
                    
                    Message msg = new Message();
                    urlp.ins_ji();
                    
                    DbMgMessage msgObj = DbMgView.getOriginalItmMessage(con, urlp.itm_id, "JI");
                    if (msgObj != null) {
                        msg.msg_id = msgObj.msg_id;
                        urlp.dbMsg.msg_id = msg.msg_id;
                        urlp.dbMsg.msg_update_usr_id = prof.usr_id;
                        urlp.dbMsg.upd(con);
                        
                        msg.constructTemplateId(con, "JI", urlp.msg_subtype);
                        msg.updParamValue(con, urlp.params);
                    } else {
                        throw new cwException("Failed to update JI message");
                    }
                    Timestamp curTime;
                    try{
                        curTime = dbUtils.getTime(con);
                    }catch( qdbException e) {
                        throw new cwException ("Failed to get timestamp from database.");
                    }

                    msg.updOldMessage(con, urlp.itm_id, urlp.usr_id, urlp.dbMsg, urlp.params, curTime);
                    
                    con.commit();
                    //msgBox(MSG_STATUS, new cwSysMessage("XMG003"), urlp.url_success, out);
                    response.sendRedirect(urlp.url_success);
                
                } else if( urlp.cmd.equalsIgnoreCase("ins_link_notify") ) {

                    boolean flag = false;       //if send message now, set to true
                    Message msg = new Message();                    
                    urlp.ins_link_notify();
                    
                    //find the template depend on the selected type and subtype
                    msg.constructTemplateId(con, urlp.msg_type, urlp.msg_subtype);

                    String[] entIdStr = dbUtils.split(urlp.ent_id_str, "~");
                    if(entIdStr != null)
                        msg.rec_ent_id = dbUtils.string2long(entIdStr);

                    String[] ccEntIdStr = dbUtils.split(urlp.cc_ent_id_str, "~");
                    if(ccEntIdStr != null)
                        msg.cc_ent_id = dbUtils.string2long(ccEntIdStr);

                    // null means send now
                    if(urlp.dbMsg.msg_target_datetime == null) {
                        try{
                            urlp.dbMsg.msg_target_datetime = dbUtils.getTime(con);
                            flag = true;
                        }catch( qdbException e) {
                            throw new cwException ("Failed to get timestamp from database.");
                        }                        
                    }

                    msg.ins(con, urlp.usr_id, urlp.dbMsg, urlp.params);
                    
                    if(flag && qdbAction.msgThread != null)
                        qdbAction.msgThread.addMessageIdToQueue(msg.msg_id);
                    
                    con.commit();                    
                    out.println(INSERT_SUCCESS);
                    
                    return;                                

                } else if( urlp.cmd.equalsIgnoreCase("ins_notify") ) {

                    boolean flag = false;       //if send message now, set to true
                    Message msg = new Message();                    
                    urlp.ins_notify();
                    
                    //find the template depend on the selected type and subtype
                    msg.constructTemplateId(con, urlp.msg_type, urlp.msg_subtype);
                    String[] entIdStr = dbUtils.split(urlp.ent_id_str, "~");
                    if(entIdStr != null)
                        msg.rec_ent_id = dbUtils.string2long(entIdStr);
                    String[] ccEntIdStr = dbUtils.split(urlp.cc_ent_id_str, "~");
                    if(ccEntIdStr != null)
                        msg.cc_ent_id = dbUtils.string2long(ccEntIdStr);
                    
                    if(urlp.dbMsg.msg_target_datetime == null) {
                        try{
                            urlp.dbMsg.msg_target_datetime = dbUtils.getTime(con);
                            flag = true;
                        }catch( qdbException e) {
                            throw new cwException ("Failed to get timestamp from database.");
                        }
                    }

                    msg.ins(con, urlp.usr_id, urlp.dbMsg, urlp.params);

                    //  insert a record into item selected message table
                    if( urlp.itm_id > 0 ) {
                        DbMgItmSelectedMessage dbMgItm = new DbMgItmSelectedMessage();
                        dbMgItm.ism_itm_id = urlp.itm_id;
                        dbMgItm.ism_msg_id = msg.msg_id;
                        dbMgItm.ism_type = urlp.msg_type;
                        dbMgItm.ins(con);
                    }
                    
                    if(flag && qdbAction.msgThread != null)
                        qdbAction.msgThread.addMessageIdToQueue(msg.msg_id);                    

                    con.commit();
                    out.println(INSERT_SUCCESS);
                    
                } else if( urlp.cmd.equalsIgnoreCase("get_itm_msg_status_xml") ) {
                    
                    urlp.getItmMsgStatus();
                    String[] entIds = dbUtils.split(urlp.ent_id_str, "~");
                    Message msg = new Message();
                    String xml = msg.getItmMsgStatus(con, urlp.itm_id, entIds);                    
                    out.println(xml.trim());
                    
                }
                /*
                else if( urlp.cmd.equalsIgnoreCase("TESTING") ) {
                    
                    long[] ent_id = { 406, 407, 408 };
                    long[] cc_ent_id = { 406, 407, 408 };
                    long id = 7;
                    Timestamp sendTime = cwSQL.getTime(con);
                    Hashtable params = new Hashtable();
                    params.put("template_type", "ENROLLMENT_CONFIRMED");
                    
                    aeXMessage aeXMsg = new aeXMessage();
                    aeXMsg.insNotify(con, prof, prof.usr_id, ent_id, cc_ent_id, id, sendTime, params);
                    con.commit();
                }
                */
            }

        }
        
}