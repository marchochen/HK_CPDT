package com.cw.wizbank.message;

import com.cw.wizbank.db.*;
import com.cw.wizbank.qdb.*;

import java.sql.*;

import com.cw.wizbank.accesscontrol.acSite;

import java.util.*;

import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

import java.net.*;
import java.util.Date;
import java.io.*;

import javax.servlet.http.*;

public class Message {

    public final static String YES                      =   "Y";
    public final static String NO                       =   "N";

    public final static String COLON                    =   ":";
    public final static String STATIC                   =   "STATIC";
    public final static String DYNAMIC                  =   "DYNAMIC";
    public final static String EQUAL                    =   "=";
    public final static String AMP                      =   "&";
    public final static String QMARK                    =   "?";

    //Dynamic value in the mgxslParamValue
    public final static String GET_ENT_ID               =   "GET_ENT_ID";
    public final static String GET_MSG_ID               =   "GET_MSG_ID";
    public final static String GET_SENDER_ID            =   "GET_SENDER_ID";


    public final static String MESSAGE_ID               =   "msg_id";
    public final static String ENTITY_ID                =   "ent_id";
    public final static String COURSE_ID                =   "cos_id";

    // Recipient Type
    public final static String CARBONCOPY               =   "CARBONCOPY";
    public final static String RECIPIENT                =   "RECIPIENT";
    public final static String BLINDCARBONCOPY          =   "BLINDCARBONCOPY";
    public final static String REPLYTO					=   "REPLYTO";


    // Template type
    public final static String JI                       =   "JI";
    public final static String NOTIFY                   =   "NOTIFY";
    public final static String REMINDER                 =   "REMINDER";

    //Tempalte Subtype
    public final static String ATTACHMENT               =   "ATTACHMENT";
    public final static String HTML                     =   "HTML";
    public final static String PLAIN_TEXT               =   "PLAIN_TEXT";
    
    public final static String MAIN                     =   "MAIN";
    
    // Path of the attachment relative to the www root
    // Mail attachment should be saved under www_root/MSG_ATTACHMENT_PATH/msg_id/
    public final static String MSG_ATTACHMENT_PATH      =   "message";
    
    public long[] rec_ent_id;
    public long[] cc_ent_id;
    public long[] xtp_id;
    public long msg_id;

    public void ins(Connection con, String usr_id, DbMgMessage dbMsg, Vector params )
        throws SQLException, cwException {

            Vector paramsName  = (Vector)params.elementAt(0);

            Vector paramsType  = (Vector)params.elementAt(1);

            Vector paramsValue = (Vector)params.elementAt(2);

            Timestamp curTime;
            try{
                curTime = dbUtils.getTime(con);
            }catch(qdbException e) {
                throw new cwException("Get TimeStamp Error in insert Message : " + e);
            }

            // insert the message
            dbMsg.msg_send_usr_id = usr_id;
            dbMsg.msg_create_usr_id = usr_id;
            dbMsg.msg_create_timestamp = curTime;
            dbMsg.msg_update_usr_id = usr_id;
            dbMsg.msg_update_timestamp = curTime;

            if( dbMsg.msg_subject == null || dbMsg.msg_subject.length() <= 0 ){
                if( xtp_id.length > 0 )
                    dbMsg.msg_subject = subToken( DbXslTemplate.getTemplateTitle(con, xtp_id[0]), dbMsg.msg_subject_token);
            }
            dbMsg.ins(con);
            msg_id = dbMsg.msg_id;

            //insert Selected Template Record
            for(int i=0; i<xtp_id.length; i++) {
                DbXslMgSelectedTemplate dbMst = new DbXslMgSelectedTemplate();
                dbMst.mst_msg_id = msg_id;
                dbMst.mst_xtp_id = xtp_id[i];
                dbMst.mst_type = MAIN;
                dbMst.ins(con);
            }

            //Insert Recipients record
            if( rec_ent_id != null )
                for(int i=0; i<rec_ent_id.length; i++)
                    insertRecipient(con, rec_ent_id[i], xtp_id, RECIPIENT);

            if( cc_ent_id != null )
                for(int i=0; i<cc_ent_id.length; i++)
                    insertRecipient(con, cc_ent_id[i], xtp_id, CARBONCOPY);


            // insert params value of each selected template id
            for(int i=0; i<xtp_id.length; i++) {

                Hashtable paramsIdNameTable = DbXslParamName.getParamsName(con, xtp_id[i], true);
                DbMgXslParamValue dbParams = new DbMgXslParamValue();
                for(int j=0; j<paramsName.size(); j++) {
                    dbParams.xpv_mst_msg_id = msg_id;
                    dbParams.xpv_mst_xtp_id = xtp_id[i];
                    dbParams.xpv_xpn_id = ((Long)paramsIdNameTable.get((String)paramsName.elementAt(j))).longValue();
                    dbParams.xpv_type = (String)paramsType.elementAt(j);
                    dbParams.xpv_value = (String)paramsValue.elementAt(j);
                    dbParams.ins(con);
                }
            }

            return;
        }

    /**
    Update the old messages
    @param item id
    @param update user id
    @param DbMessage Class
    @param Vector of param value
    */
    public void updOldMessage(Connection con, long itm_id, String usr_id, DbMgMessage dbMsg, Vector params, Timestamp beforeTime)
        throws SQLException, cwException {

            Hashtable msgDatetime = DbMgView.getItemMessage(con, itm_id, "JI");
            Enumeration enumeration = msgDatetime.keys();
            Timestamp datetime = null;
            Long msgId;
            while( enumeration.hasMoreElements() ) {
                msgId = (Long)enumeration.nextElement();
                datetime = (Timestamp)msgDatetime.get(msgId);
                if( datetime.after(beforeTime) ) {
                    msg_id = msgId.longValue();
                    dbMsg.msg_id = msg_id;
                    dbMsg.upd(con);

                    xtp_id = DbXslMgSelectedTemplate.getXtpIds(con, msg_id, MAIN);
                    updParamValue(con, params);
                }
            }

            return;

        }






    /**
    Clone a new JI
    */
    public void cloneJI(Connection con, long itm_id, String msg_type, Timestamp send_date, loginProfile prof)
        throws SQLException, cwException {

            Timestamp curTime;
            try{
                curTime = dbUtils.getTime(con);
            }catch( qdbException e) {
                throw new cwException ("Failed to get timestamp from database.");
            }

            DbMgMessage dbMsg = DbMgView.getOriginalItmMessage(con, itm_id, "JI");
            if (dbMsg != null) {
                dbMsg.msg_target_datetime = send_date;

                Vector params = DbMgView.getMessageParams(con, dbMsg.msg_id, xtp_id[0]);
                //Replace the render id
                Vector paramName = (Vector)params.elementAt(0);
                Vector paramValue = (Vector)params.elementAt(2);
                for(int i=0; i<paramName.size(); i++){
                    if( ((String)paramName.elementAt(i)).equalsIgnoreCase("sender_id") ){
                        paramValue.setElementAt(prof.usr_id, i);
                        break;
                    }
                }
                params.setElementAt(paramValue, 2);
                ins(con, prof.usr_id, dbMsg, params);
                msg_id = dbMsg.msg_id;


                DbMgItmSelectedMessage dbMgItm = new DbMgItmSelectedMessage();
                dbMgItm.ism_itm_id = itm_id;
                dbMgItm.ism_msg_id = msg_id;
                dbMgItm.ism_type = "JI";
                dbMgItm.ins(con);
            } else {
                throw new cwException("JI not exists , itm id = "  + itm_id);
            }
            return;

        }

    /**
    Update param value of the message
    */
    public void updParamValue(Connection con, Vector params )
        throws SQLException, cwException {

            Vector paramsName  = (Vector)params.elementAt(0);

            Vector paramsType  = (Vector)params.elementAt(1);

            Vector paramsValue = (Vector)params.elementAt(2);


            // update params value of each selected template id
            for(int i=0; i<xtp_id.length; i++) {

                Hashtable paramsIdNameTable = DbXslParamName.getParamsName(con, xtp_id[i], true);
                DbMgXslParamValue dbParams = new DbMgXslParamValue();
                for(int j=0; j<paramsName.size(); j++) {

                    dbParams.xpv_mst_msg_id = msg_id;
                    dbParams.xpv_mst_xtp_id = xtp_id[i];
                    dbParams.xpv_xpn_id = ((Long)paramsIdNameTable.get((String)paramsName.elementAt(j))).longValue();
                    dbParams.xpv_type = (String)paramsType.elementAt(j);
                    dbParams.xpv_value = (String)paramsValue.elementAt(j);
                    dbParams.upd(con);
                }
            }

            return;

        }







    /**
    Insert a recipient record
    @param ent_id long value of user entity id
    @param xtp_id long array of template id
    @param type string of recipient type { RECIPIENT | CARBONCOPY | REPLYTO}
    */
    public void insertRecipient(Connection con, long ent_id, long[] xtp_id, String type)
        throws SQLException, cwException {

            DbMgRecipient dbRec = new DbMgRecipient();
            dbRec.rec_msg_id = msg_id;
            dbRec.rec_ent_id = ent_id;
            dbRec.rec_type = type;
            dbRec.ins(con);

            if (!type.equalsIgnoreCase(Message.REPLYTO)) {
	            //insert Recipient History Record
	            for(int j=0; j<xtp_id.length; j++) {
	                DbMgRecHistory dbHis = new DbMgRecHistory();
	                dbHis.mgh_rec_id = dbRec.rec_id;
	                dbHis.mgh_mst_msg_id = msg_id;
	                dbHis.mgh_mst_xtp_id = xtp_id[j];
	                dbHis.ins(con);
	            }
            }
            return;
        }











    /**
    Insert Recipients to the specified message
    @param itm_id long value of the item which message belong to
    @param type string of message type
    */
    public void insertRecipients(Connection con, long itm_id, String type, Timestamp send_date, loginProfile prof)
        throws SQLException, cwException {


            Timestamp curTime = null;

            xtp_id = DbXslMgSelectedTemplate.getXtpIds(con, msg_id, MAIN);

            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_id = msg_id;

            if( send_date == null ) {
                try{
                    curTime = dbUtils.getTime(con);
                }catch( qdbException e ) {
                    throw new cwException("Failed to get timestamp from database." + e);
                }
            }else
                curTime = send_date;
            dbMsg.msg_target_datetime = curTime;
            dbMsg.updTimestamp(con, prof.usr_id);

            //do not insert duplicated user
            if( type.equalsIgnoreCase(REMINDER) ) {
                Vector recEntIdVec = DbMgView.getItemMessageRecipient(con, itm_id, REMINDER, RECIPIENT);
                if( rec_ent_id != null ) {
                    for(int i=0; i<rec_ent_id.length; i++) {
                        if( recEntIdVec.indexOf(new Long(rec_ent_id[i])) != -1 )
                            rec_ent_id[i] = 0;
                    }
                }
            }

            if( rec_ent_id != null )
                for(int i=0; i<rec_ent_id.length; i++) {
                    if( rec_ent_id[i] == 0 )
                        continue;
                    insertRecipient(con, rec_ent_id[i], xtp_id, RECIPIENT);
                }

            if( cc_ent_id != null )
                for(int i=0; i<cc_ent_id.length; i++) {
                    if( cc_ent_id[i] == 0 )
                        continue;
                    insertRecipient(con, cc_ent_id[i], xtp_id, CARBONCOPY);
                }
            return;
        }










    //remove recipient
    public void removeRecipients(Connection con, long itm_id, String type)
        throws SQLException, cwException{

            if( rec_ent_id != null && rec_ent_id.length > 0 ) {
                msg_id = DbMgView.getLatestItmMessage(con, itm_id, type);
                DbMgRecHistory.delRecords(con, msg_id, rec_ent_id);
                DbMgRecipient.delRecords(con, msg_id, rec_ent_id);
            }
            return;

        }





    //remove carbon copy
    public void removeCarbonCopy(Connection con, long itm_id, String type)
        throws SQLException, cwException{

            if( cc_ent_id != null && cc_ent_id.length > 0 ) {
                msg_id = DbMgView.getLatestItmMessage(con, itm_id, type);
                DbMgRecHistory.delRecords(con, msg_id, cc_ent_id);
                DbMgRecipient.delRecords(con, msg_id, cc_ent_id);
            }
            return;

        }





    //preview
    public static String preview(Connection con, long itm_id, String type, String subtype, DbMgMessage dbmsg, long id, String id_type, String cmd, String url_redirect, loginProfile prof, qdbEnv static_env)
        throws SQLException, cwException {

            long xtpId;
            StringBuffer url = new StringBuffer();
            String cmd_url = null;
            StringBuffer args = new StringBuffer();
            Hashtable paramsName = new Hashtable();
            Vector paramsXpnId = new Vector();
            Vector paramsType = new Vector();
            Vector paramsValue = new Vector();

            StringBuffer xml = new StringBuffer();

            DbMgMessage dbMsg = dbmsg;

            if( type.equalsIgnoreCase("JI") || type.equalsIgnoreCase("REMINDER")) {
                // if of these types, retrieve all DbMgMessage information from database and 
                // ignore the one passed in from caller
                dbMsg = DbMgView.getOriginalItmMessage(con, itm_id, type);
                if (dbMsg == null) {
                    xml.append("<message itm_id=\"").append(itm_id).append("\"/>");
                    return xml.toString();
                }

                xtpId = DbMgView.getXtpId(con, dbMsg.msg_id, subtype);

                //get param name and xpn_id into hashtable with xpn_id to be the key
                paramsName = DbXslParamName.getParamsName(con, xtpId, false);

                // url of the specificed template
                url = new StringBuffer(DbXslTemplate.getUrl(con, xtpId));

                // params detail of the specificed message and template
                Vector params = DbMgXslParamValue.getParams(con, dbMsg.msg_id, xtpId);
                paramsXpnId = (Vector)params.elementAt(0);
                paramsType  = (Vector)params.elementAt(1);
                paramsValue = (Vector)params.elementAt(2);

                args = new StringBuffer();
                for(int l=0; l<paramsXpnId.size(); l++) {

                    args.append((String)paramsName.get((Long)paramsXpnId.elementAt(l)));
                    args.append(EQUAL);

                    if( ((String)paramsType.elementAt(l)).equalsIgnoreCase(STATIC) ) {

                        args.append((String)paramsValue.elementAt(l));

                    }
                    args.append(AMP);
                }

            } else {
                long xtp_id = DbXslTemplate.getXtpId(con, type, subtype);
                url = new StringBuffer(DbXslTemplate.getUrl(con, xtp_id));

                args.append("cmd=").append(cmd).append("&id=").append(id)
                    .append("&sender_id=").append(prof.usr_id)
                    .append("&id_type=").append(id_type);
            }

                if( itm_id > 0 )
                    xml.append("<message itm_id=\"").append(itm_id).append("\">").append(dbUtils.NEWL);
                else
                    xml.append("<message>").append(dbUtils.NEWL);

                if( dbMsg.msg_subject != null )
                    xml.append("<subject>").append(dbUtils.esc4XML(dbMsg.msg_subject)).append("</subject>").append(dbUtils.NEWL);
                else
                    xml.append("<subject/>").append(dbUtils.NEWL);

                if( dbMsg.msg_addition_note != null )
                    xml.append("<body>").append(dbUtils.esc4XML(dbMsg.msg_addition_note)).append("</body>").append(dbUtils.NEWL);
                else
                    xml.append("<body/>").append(dbUtils.NEWL);

                xml.append("<parameters>").append(dbUtils.NEWL);
                for(int m=0; m<paramsXpnId.size(); m++) {
                    xml.append("<param name=\"").append(dbUtils.esc4XML((String)paramsName.get((Long)paramsXpnId.elementAt(m)))).append("\" ");
                    xml.append(" type=\"").append(dbUtils.esc4XML((String)paramsType.elementAt(m))).append("\" ");
                    xml.append(" value=\"").append(dbUtils.esc4XML((String)paramsValue.elementAt(m))).append("\"/>").append(dbUtils.NEWL);
                }
                xml.append("<param name=\"url_redirect\" value=\"").append(cwUtils.esc4XML(url_redirect)).append("\"/>").append(dbUtils.NEWL);
                xml.append("</parameters>");
                
                //Hardcode : if notify/comment no need to get information from external link when previewing                    
                if( type.equalsIgnoreCase("NOTIFY") || type.equalsIgnoreCase("COMMENT") ) {
                    xml.append("<information>").append(dbUtils.NEWL);
                    xml.append("<sender display_name=\"").append(prof.usr_display_bil).append("\"");
                    xml.append(" usr_id=\"").append(prof.usr_id).append("\"/>");
                    xml.append("</information>").append(dbUtils.NEWL);
                }
                else {
                    xml.append("<information>").append(dbUtils.NEWL);

                    String returnValue;
                    try{
                        returnValue = (returnByUrl(url.toString(), args.toString(), static_env.ENCODING)).trim();
                    }catch(Exception e) {
                        throw new cwException("Failed to get the message details : " + e);
                    }

                    if( returnValue.equalsIgnoreCase("FAILED") )
                        throw new cwException("Failed to get the message details.");
                    else
                        xml.append(returnValue);
                    xml.append("</information>").append(dbUtils.NEWL);
                }

                xml.append("</message>");
                return xml.toString();
        }





    // one message for one recipient
    public static void send(Connection con, long msg_id, qdbEnv static_env, HttpServletRequest request)
        throws SQLException, cwException, IOException {

                DbMgMessage dbMsg = new DbMgMessage();
                dbMsg.msg_id = msg_id;
                dbMsg.get(con);
                //one message may be sent by more than one xslTemplate
                long[] xtpIds = DbXslMgSelectedTemplate.getXtpIds(con, msg_id, MAIN);
                String xslFile;
                String sysEmail = null;
                long site_id = dbRegUser.getSiteEntId(con, dbMsg.msg_send_usr_id);
                if( dbMsg.msg_bcc_sys_ind ) {
                    if( (static_env.MAIL_ACCOUNT).equalsIgnoreCase("EMAIL") )
                        sysEmail = acSite.getSysEmail_1(con, site_id);
                    else
                        sysEmail = acSite.getSysEmail_2(con, site_id);
                }
                //check recipient entity id
                //if contains user group id, find all users belong to the group/subgroup
                //and insert record into table mgRecipient and mgRecHistory                
                Vector vec = null;
                Vector entIdVec = null;
                Vector usgEntIdVec = null;
                Vector usrEntIdVec = null;
                Vector recTypeVec = null;
                Message msgObj = new Message();
                msgObj.msg_id = msg_id;
                
                vec = DbMgView.getRecipientStatus(con, dbMsg.msg_id, null, "N", 0, static_env.MAIL_ATTEMPT);
                entIdVec = (Vector)vec.elementAt(1);
                recTypeVec = (Vector)vec.elementAt(3);
                Vector recEntIdVec = new Vector();
                Vector ccEntIdVec = new Vector();
                Vector bccEntIdVec = new Vector();
                for(int i=0; i<entIdVec.size(); i++) {
                    if( ((String)recTypeVec.elementAt(i)).equalsIgnoreCase(RECIPIENT) )
                        recEntIdVec.addElement(new Long((String)entIdVec.elementAt(i)));
                    else if( ((String)recTypeVec.elementAt(i)).equalsIgnoreCase(CARBONCOPY) )
                        ccEntIdVec.addElement(new Long((String)entIdVec.elementAt(i)));
                    else if( ((String)recTypeVec.elementAt(i)).equalsIgnoreCase(BLINDCARBONCOPY) )
                        bccEntIdVec.addElement(new Long((String)entIdVec.elementAt(i)));
                }                
                recTypeVec = null;

                //For type Recipient
                usgEntIdVec = dbEntity.getUserGroupEntityId(con, recEntIdVec);
                if( !usgEntIdVec.isEmpty() ) {
                    msgObj.updRecStatus(con, usgEntIdVec, RECIPIENT);
                    usrEntIdVec = getAllUserEntId(con, usgEntIdVec);

                    //Remove the entity id which already exsited
                    for(int i=0; i<recEntIdVec.size(); i++)
                        usrEntIdVec.removeElement(recEntIdVec.elementAt(i));

                    for(int j=0; j<usrEntIdVec.size(); j++)
                        msgObj.insertRecipient(con, ((Long)usrEntIdVec.elementAt(j)).longValue(), xtpIds, RECIPIENT);
                }

                //For type Carbon Copy
                usgEntIdVec = dbEntity.getUserGroupEntityId(con, ccEntIdVec);
                if( !usgEntIdVec.isEmpty() ) {
                    msgObj.updRecStatus(con, usgEntIdVec, CARBONCOPY);
                    usrEntIdVec = getAllUserEntId(con, usgEntIdVec);
                    //Remove the entity id which already exsited
                    for(int i=0; i<ccEntIdVec.size(); i++)
                        usrEntIdVec.removeElement(ccEntIdVec.elementAt(i));

                    for(int j=0; j<usrEntIdVec.size(); j++)
                        msgObj.insertRecipient(con, ((Long)usrEntIdVec.elementAt(j)).longValue(), xtpIds, CARBONCOPY);
                }
                
                //For type Blind Carbon Copy
                usgEntIdVec = dbEntity.getUserGroupEntityId(con, bccEntIdVec);
                if( !usgEntIdVec.isEmpty() ) {
                    msgObj.updRecStatus(con, usgEntIdVec, BLINDCARBONCOPY);
                    usrEntIdVec = getAllUserEntId(con, usgEntIdVec);
                    //Remove the entity id which already exsited
                    for(int i=0; i<bccEntIdVec.size(); i++)
                        usrEntIdVec.removeElement(bccEntIdVec.elementAt(i));

                    for(int j=0; j<usrEntIdVec.size(); j++)
                        msgObj.insertRecipient(con, ((Long)usrEntIdVec.elementAt(j)).longValue(), xtpIds, BLINDCARBONCOPY);
                }
                
                //for reply to user
                Vector replyVec = DbMgView.getReplyEntIdAndName(con, dbMsg.msg_id, REPLYTO);
                
                Vector xtpDetail = DbXslTemplate.getDetail(con,xtpIds);
                Vector xtpMethod = (Vector)xtpDetail.elementAt(0);
                Vector xtpChannel = (Vector)xtpDetail.elementAt(1);
                Vector xtpApi = (Vector)xtpDetail.elementAt(2);
                Vector xtpXsl = (Vector)xtpDetail.elementAt(3);
                Vector xtpMailMerge = (Vector)xtpDetail.elementAt(4);
                for(int j=0; j<xtpIds.length; j++) {
                    /*
                    String type = DbXslTemplate.getType(con, xtpIds[j]);
                    if( type.equalsIgnoreCase(NOTIFY) || type.equalsIgnoreCase("COMMENT")
                    ||  type.equalsIgnoreCase("ENROLLMENT_WAITLISTED_NOTIFICATION")
                    ||  type.equalsIgnoreCase("ENROLLMENT_REJECTED_NOTIFICATION")
                    ||  type.equalsIgnoreCase("ENROLLMENT_APPROVAL_NOTIFICATION")
                    ||  type.equalsIgnoreCase("ENROLLMENT_NOTIFICATION") ) {
                    */
                    if( !((Boolean)xtpMailMerge.elementAt(j)).booleanValue() ) {
                        sendMessage(con, dbMsg, xtpIds[j], (String)xtpMethod.elementAt(j), (String)xtpApi.elementAt(j), (String)xtpXsl.elementAt(j), static_env);
                        continue;
                    }

                    //get param name and xpn_id into hashtable with xpn_id as a key
                    Hashtable paramsName = DbXslParamName.getParamsName(con, xtpIds[j], false);

                    // url of the specificed template
                    StringBuffer url = new StringBuffer(DbXslTemplate.getUrl(con, xtpIds[j]));

                    // the recipients of the specificed message and template
                    Vector recipVec = DbMgView.getRecipientStatus(con, dbMsg.msg_id, null, "N", xtpIds[j], static_env.MAIL_ATTEMPT);

                    Vector recIdVec = (Vector)recipVec.elementAt(0);
                    entIdVec = (Vector)recipVec.elementAt(1);

                    // if no recipient, send the other message, should not be occured
                    if( recIdVec.isEmpty() )
                        continue;

                    // params detail of the specificed message and template
                    Vector params = DbMgXslParamValue.getParams(con, dbMsg.msg_id, xtpIds[j]);

                    Vector paramsXpnId = (Vector)params.elementAt(0);

                    Vector paramsType  = (Vector)params.elementAt(1);

                    Vector paramsValue = (Vector)params.elementAt(2);

                    for(int k=0; k<entIdVec.size(); k++) {
                        Hashtable returnedRecTable = new Hashtable();
                        Hashtable recTable = new Hashtable();

                        recTable.put( (String)entIdVec.elementAt(k), (Long)recIdVec.elementAt(k) );

                        StringBuffer args = new StringBuffer();
                        for(int l=0; l<paramsXpnId.size(); l++) {

                            args.append((String)paramsName.get((Long)paramsXpnId.elementAt(l)));
                            args.append(EQUAL);

                            // static param : use the value stored in database
                            if( ((String)paramsType.elementAt(l)).equalsIgnoreCase(STATIC) ) {
                                args.append((String)paramsValue.elementAt(l));
                            }
                            else if( ((String)paramsType.elementAt(l)).equalsIgnoreCase(DYNAMIC) ) {

                                if( ((String)paramsValue.elementAt(l)).equalsIgnoreCase("GET_ENT_ID") )
                                    args.append(entIdVec.elementAt(k));
                            }
                            args.append(AMP);
                        }


                        // construct the xml message
                        StringBuffer xml = new StringBuffer();
                        xml.append("<message target_datetime=\"").append(dbMsg.msg_target_datetime).append("\">").append(dbUtils.NEWL);
                        xml.append("<subject>").append(dbUtils.esc4XML(dbMsg.msg_subject)).append("</subject>").append(dbUtils.NEWL);
                        if(dbMsg.msg_addition_note != null)
                            xml.append("<body>").append(dbUtils.esc4XML(dbMsg.msg_addition_note)).append("</body>").append(dbUtils.NEWL);
                        else
                            xml.append("<body/>").append(dbUtils.NEWL);

                        xml.append("<parameters>").append(dbUtils.NEWL);
                        for(int m=0; m<paramsXpnId.size(); m++) {
                            xml.append("<param name=\"").append(dbUtils.esc4XML((String)paramsName.get((Long)paramsXpnId.elementAt(m)))).append("\" ");
                            xml.append(" type=\"").append(dbUtils.esc4XML((String)paramsType.elementAt(m))).append("\" ");
                            xml.append(" value=\"").append(dbUtils.esc4XML((String)paramsValue.elementAt(m))).append("\"/>").append(dbUtils.NEWL);
                        }

                        xml.append("<param name=\"url\" type=\"\" value=\"")
                           .append(cwUtils.getRealPath(request, "../servlet/qdbAction?" ))
                           .append("\"/>").append(dbUtils.NEWL);

                        xml.append("<param name=\"login_url\" type=\"\" value=\"")
                           .append(cwUtils.getRealPath(request, "../" + dbUtils.esc4XML(static_env.URL_RELOGIN)))
                           .append("\"/>").append(dbUtils.NEWL);

                        xml.append("</parameters>").append(dbUtils.NEWL);

                        //xml.append("<information>").append(dbUtils.NEWL);

                        String returnValue = null;
                        boolean urlError = false;
                        try{
                            returnValue = returnByUrl(url.toString(), args.toString(), static_env.ENCODING);
                        }catch(Exception e) {
                        	CommonLog.error("Failed to call url by http connection : " + e);
                            errorToLog(con, xtpIds[j], msg_id, e.getMessage(), static_env);
                            urlError = true;
                        }
                        String mghStatus;
                        //failed to get information, send to next recipient
                        if( !urlError ) {
                            if( returnValue.equalsIgnoreCase("FAILED") ) {
                                errorToLog(con, xtpIds[j], msg_id, "External link error, url = " + url.toString() + " args = " + args.toString() , static_env);
                                returnedRecTable.put(recIdVec.elementAt(k), NO);
                            } else {
                                returnValue = "<information>" + returnValue + "</information>";

                                returnValue = xmlObj.addMsgIdAttr(returnValue, recTable, null);
                                xml.append(returnValue);
                                xml.append("</message>");
                                // sned the xml message by the specificed template id
                                try{
                                	MessageOutbox msgBox = new MessageOutbox(static_env);
                                    returnedRecTable = msgBox.send((String)xtpMethod.elementAt(j), (String)xtpApi.elementAt(j), (String)xtpXsl.elementAt(j), xml.toString(), static_env, con, sysEmail, msg_id, replyVec);
                                } catch( Exception e) {
                                    // if exception catch, send the other message
                                	CommonLog.error("Send Message Error : " + e);
                                    errorToLog(con, xtpIds[j], msg_id, e.getMessage() , static_env);
                                    returnedRecTable.put(recIdVec.elementAt(k), NO);
                                }
                            }
                        }
                        //update recipient history of the specificed message and template
                        DbMgRecHistory dbMgHis = new DbMgRecHistory();
                        dbMgHis.mgh_rec_id = ((Long)recIdVec.elementAt(k)).longValue();
                        dbMgHis.mgh_mst_msg_id = dbMsg.msg_id;
                        dbMgHis.mgh_mst_xtp_id = xtpIds[j];
                        dbMgHis.get(con);
                        if( urlError )
                            mghStatus = NO;
                        else {
                            if( (mghStatus = (String)returnedRecTable.get(recIdVec.elementAt(k))) != null )
                                dbMgHis.mgh_status = mghStatus;
                            else
                                dbMgHis.mgh_status = YES;
                        }
                        dbMgHis.mgh_attempted++;
                        Timestamp curTime = cwSQL.getTime(con);
                        dbMgHis.mgh_sent_datetime = curTime;
                        dbMgHis.upd(con);
                        //commit the database for each message sent
                        con.commit();
                    }
                }

            return;

        }








    // one message for all recipients,
    public static void sendMessage(Connection con, DbMgMessage dbMsg, long xtp_id, String method, String api, String xslFile, qdbEnv static_env)
        throws SQLException, cwException {

                String sysEmail = null;
                long site_id = dbRegUser.getSiteEntId(con, dbMsg.msg_send_usr_id);
                if( dbMsg.msg_bcc_sys_ind ) {
                    if( (static_env.MAIL_ACCOUNT).equalsIgnoreCase("EMAIL") )
                        sysEmail = acSite.getSysEmail_1(con, site_id);
                    else
                        sysEmail = acSite.getSysEmail_2(con, site_id);
                }

                    //get param name and xpn_id into hashtable with xpn_id to be the key
                    Hashtable paramsName = DbXslParamName.getParamsName(con, xtp_id, false);

                    // url of the specificed template
                    StringBuffer url = new StringBuffer(DbXslTemplate.getUrl(con, xtp_id));

                    // the recipients of the specificed message and template
                    Vector recipVec = DbMgView.getRecipientStatus(con, dbMsg.msg_id, RECIPIENT, "N", xtp_id, static_env.MAIL_ATTEMPT);

                    Vector recIdVec = (Vector)recipVec.elementAt(0);
                    Vector entIdVec = (Vector)recipVec.elementAt(1);
                   

                    Hashtable recTable = new Hashtable();
                    Hashtable returnedRecTable = null;

                    for(int i=0; i<entIdVec.size(); i++) {
                        recTable.put((String)entIdVec.elementAt(i), (Long)recIdVec.elementAt(i));
                    }
                    // cc user entity id
                    Vector ccRecipVec = DbMgView.getRecipientStatus(con, dbMsg.msg_id, CARBONCOPY, "N", xtp_id, static_env.MAIL_ATTEMPT);
                    Vector ccRecIdVec = (Vector)ccRecipVec.elementAt(0);
                    Vector ccEntIdVec = (Vector)ccRecipVec.elementAt(1);

                    Hashtable ccTable = new Hashtable();
                    for(int i=0; i<ccEntIdVec.size(); i++)
                        ccTable.put((String)ccEntIdVec.elementAt(i), (Long)ccRecIdVec.elementAt(i));

                    // bcc user entity id
                    Vector bccRecipVec = DbMgView.getRecipientStatus(con, dbMsg.msg_id, BLINDCARBONCOPY, "N", xtp_id, static_env.MAIL_ATTEMPT);
                    Vector bccRecIdVec = (Vector)bccRecipVec.elementAt(0);
                    Vector bccEntIdVec = (Vector)bccRecipVec.elementAt(1);

                    Hashtable bccTable = new Hashtable();
                    for(int i=0; i<bccEntIdVec.size(); i++)
                        bccTable.put((String)bccEntIdVec.elementAt(i), (Long)bccRecIdVec.elementAt(i));

                    // params detail of the specificed message and template
                    Vector params = DbMgXslParamValue.getParams(con, dbMsg.msg_id, xtp_id);
                    Vector paramsXpnId = (Vector)params.elementAt(0);
                    Vector paramsType  = (Vector)params.elementAt(1);
                    Vector paramsValue = (Vector)params.elementAt(2);

                    //reply user entity id
                    Vector replyVec = DbMgView.getReplyEntIdAndName(con, dbMsg.msg_id, REPLYTO);

                    StringBuffer args = new StringBuffer();
                    for(int i=0; i<paramsXpnId.size(); i++) {

                        args.append((String)paramsName.get((Long)paramsXpnId.elementAt(i)));
                        args.append(EQUAL);

                        if( ((String)paramsType.elementAt(i)).equalsIgnoreCase(STATIC) ) {

                            args.append((String)paramsValue.elementAt(i));

                        }
                        else if( ((String)paramsType.elementAt(i)).equalsIgnoreCase(DYNAMIC) ) {

                            if( ((String)paramsValue.elementAt(i)).equalsIgnoreCase("GET_ENT_ID") && entIdVec != null && !entIdVec.isEmpty()) {
                                args.append(entIdVec.elementAt(0));
                                for(int j=1; j<entIdVec.size(); j++)
                                    args.append("~").append(entIdVec.elementAt(j));
                            }

                            if( ((String)paramsValue.elementAt(i)).equalsIgnoreCase("GET_CC_ENT_ID") && ccEntIdVec != null && !ccEntIdVec.isEmpty()) {
                                if( !ccEntIdVec.isEmpty() ) {
                                    args.append(ccEntIdVec.elementAt(0));
                                    for(int j=1; j<ccEntIdVec.size(); j++)
                                        args.append("~").append(ccEntIdVec.elementAt(j));
                                }
                            }

                            if( ((String)paramsValue.elementAt(i)).equalsIgnoreCase("GET_BCC_ENT_ID") && bccEntIdVec != null && !bccEntIdVec.isEmpty()) {
                                if( !bccEntIdVec.isEmpty() ) {
                                    args.append(bccEntIdVec.elementAt(0));
                                    for(int j=1; j<bccEntIdVec.size(); j++)
                                        args.append("~").append(bccEntIdVec.elementAt(j));
                                }
                            }                        
                        }

                        args.append(AMP);
                    }
                        // construct the xml message
                        StringBuffer xml = new StringBuffer();
                        xml.append("<message target_datetime=\"").append(dbMsg.msg_target_datetime).append("\">").append(dbUtils.NEWL);
                        xml.append("<subject>").append(dbUtils.esc4XML(dbMsg.msg_subject)).append("</subject>").append(dbUtils.NEWL);
                        if( dbMsg.msg_addition_note != null )
                            xml.append("<body>").append(dbUtils.esc4XML(dbMsg.msg_addition_note)).append("</body>").append(dbUtils.NEWL);
                        else
                            xml.append("<body/>").append(dbUtils.NEWL);

                        xml.append("<parameters>").append(dbUtils.NEWL);
                        for(int i=0; i<paramsXpnId.size(); i++) {
                            xml.append("<param name=\"").append(dbUtils.esc4XML((String)paramsName.get((Long)paramsXpnId.elementAt(i)))).append("\" ");
                            xml.append(" type=\"").append(dbUtils.esc4XML((String)paramsType.elementAt(i))).append("\" ");
                            xml.append(" value=\"").append(dbUtils.esc4XML((String)paramsValue.elementAt(i))).append("\"/>").append(dbUtils.NEWL);
                        }

                        xml.append("</parameters>").append(dbUtils.NEWL);
                        String returnValue = null;
                        boolean urlError = false;
                        boolean returnError = false;
                        try{
                            returnValue = returnByUrl(url.toString(), args.toString(), static_env.ENCODING);
                            if( returnValue.equalsIgnoreCase("FAILED") ) {
                                returnError = true;
                            }
                        }catch(Exception e) {
                            //to log
                        	CommonLog.error("Failed to call url by http connection : " + e);
                            errorToLog(con, xtp_id, dbMsg.msg_id, e.getMessage() , static_env);
                            urlError = true;
                        }
                        if( !urlError && !returnError) {
                            returnValue = "<information>" + returnValue + "</information>";
                            returnValue = xmlObj.addMsgIdAttr(returnValue, recTable, ccTable, bccTable);
                            xml.append(returnValue);
                            xml.append("</message>");
                            try{
                                MessageOutbox msgBox = new MessageOutbox(static_env);
                                returnedRecTable = msgBox.send(method, api, xslFile, xml.toString(), static_env, con, sysEmail, dbMsg.msg_id, replyVec);
                            }catch( Exception e ) {
                                //to log file
                                //System.out.println("Send Message Error : " + e);
                                errorToLog(con, xtp_id, dbMsg.msg_id, e.getMessage() , static_env);
                                urlError = true;
                            }
                        }
                        String mghStatus;
                        for(int i=0; i<recIdVec.size(); i++) {
                            //update recipient history of the specificed message and template
                            DbMgRecHistory dbMgHis = new DbMgRecHistory();
                            dbMgHis.mgh_rec_id = ((Long)recIdVec.elementAt(i)).longValue();
                            dbMgHis.mgh_mst_msg_id = dbMsg.msg_id;
                            dbMgHis.mgh_mst_xtp_id = xtp_id;
                            dbMgHis.get(con);
                            if( urlError || returnError ){
                                dbMgHis.mgh_status = NO;
                                }
                            else {
                                if( returnedRecTable != null ) {
                                    if( (mghStatus = (String)returnedRecTable.get(recIdVec.elementAt(i)) ) != null ){
                                        dbMgHis.mgh_status = mghStatus;
                                        }
                                    else{
                                        dbMgHis.mgh_status = YES;
                                        }
                                } else
                                    dbMgHis.mgh_status = NO;
                            }
                            dbMgHis.mgh_attempted++;
                            Timestamp curTime = cwSQL.getTime(con);
                            dbMgHis.mgh_sent_datetime = curTime;
                            dbMgHis.upd(con);
                        }



                        for(int i=0; i<ccRecIdVec.size(); i++) {
                            //update recipient history of the specificed message and template
                            DbMgRecHistory dbMgHis = new DbMgRecHistory();
                            dbMgHis.mgh_rec_id = ((Long)ccRecIdVec.elementAt(i)).longValue();
                            dbMgHis.mgh_mst_msg_id = dbMsg.msg_id;
                            dbMgHis.mgh_mst_xtp_id = xtp_id;
                            dbMgHis.get(con);
                            if( returnedRecTable != null ) {
                                if( (mghStatus = (String)returnedRecTable.get( ccRecIdVec.elementAt(i) )) != null )
                                    dbMgHis.mgh_status = mghStatus;
                                else
                                    dbMgHis.mgh_status = YES;
                            } else
                                dbMgHis.mgh_status = NO;
                            dbMgHis.mgh_attempted++;
                            Timestamp curTime = cwSQL.getTime(con);
                            dbMgHis.mgh_sent_datetime = curTime;

                            dbMgHis.upd(con);
                        }

                        for(int i=0; i<bccRecIdVec.size(); i++) {
                            //update recipient history of the specificed message and template
                            DbMgRecHistory dbMgHis = new DbMgRecHistory();
                            dbMgHis.mgh_rec_id = ((Long)bccRecIdVec.elementAt(i)).longValue();
                            dbMgHis.mgh_mst_msg_id = dbMsg.msg_id;
                            dbMgHis.mgh_mst_xtp_id = xtp_id;
                            dbMgHis.get(con);
                            if( returnedRecTable != null ) {
                                if( (mghStatus = (String)returnedRecTable.get( bccRecIdVec.elementAt(i) )) != null )
                                    dbMgHis.mgh_status = mghStatus;
                                else
                                    dbMgHis.mgh_status = YES;
                            } else
                                dbMgHis.mgh_status = NO;
                            dbMgHis.mgh_attempted++;
                            Timestamp curTime = cwSQL.getTime(con);
                            dbMgHis.mgh_sent_datetime = curTime;

                            dbMgHis.upd(con);
                        }

                        //commit the database for each message sent
                        con.commit();
                        return;

        }








    /**
    Create a new JI for the item
    */
    /*
    public void newItmMessage(Connection con, long itm_id, String type, loginProfile prof)
        throws SQLException, cwException {
            // store the latest message id
            long msgId = msg_id;

            // insert a new message record
            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_id = msg_id;
            dbMsg.get(con);
            dbMsg.msg_send_usr_id = prof.usr_id;
            Timestamp curTime;
            try{
                curTime = dbUtils.getTime(con);
            } catch( qdbException e ) {
                throw new cwException("Failed to get timestamp from database : " + e);
            }
            dbMsg.msg_target_datetime = curTime;
            dbMsg.msg_update_usr_id = prof.usr_id;
            dbMsg.msg_update_timestamp = curTime;
            dbMsg.ins(con);


            //insert a new item selected message record
            msg_id = DbMgMessage.getMaxId(con);
            DbMgItmSelectedMessage dbMgItm = new DbMgItmSelectedMessage();
            dbMgItm.ism_itm_id = itm_id;
            dbMgItm.ism_msg_id = msg_id;
            dbMgItm.ism_type = type;
            dbMgItm.ins(con);


            //insert a new message selected template record
            long[] xtp_ids = DbXslMgSelectedTemplate.getXtpIds(con, msgId);
            DbXslMgSelectedTemplate mgTpx = new DbXslMgSelectedTemplate();
            for(int i=0; i<xtp_ids.length; i++) {
                mgTpx.mst_msg_id = msg_id;
                mgTpx.mst_xtp_id = xtp_ids[i];
                mgTpx.ins(con);

                //insert Param Value of the new message
                //long[] xpn_id = DbMgXslParamValue.getParamsId(con, msgId);
                Vector vec = DbMgXslParamValue.getParams(con, msgId, xtp_ids[i]);
                Vector xpnVec = (Vector)vec.elementAt(0);
                Vector typeVec = (Vector)vec.elementAt(1);
                Vector valueVec = (Vector)vec.elementAt(2);

                DbMgXslParamValue dbParam = new DbMgXslParamValue();
                for(int j=0; j<xpnVec.size(); j++) {
                    dbParam.xpv_mst_msg_id = msg_id;
                    dbParam.xpv_mst_xtp_id = xtp_ids[i];
                    dbParam.xpv_xpn_id = ((Long)xpnVec.elementAt(j)).longValue();
                    dbParam.xpv_type = (String)typeVec.elementAt(j);
                    dbParam.xpv_value = (String)valueVec.elementAt(j);
                    dbParam.ins(con);
                }
            }

            return;
        }
*/







/*
    // send message type message to outbox
    public static void outbox(String channel, String api, String xslFile, StringBuffer xml, qdbEnv static_env)
        throws SQLException, cwException, IOException {

            xslFile = static_env.DOC_ROOT + dbUtils.SLASH  + static_env.INI_XSL_HOME + dbUtils.SLASH + xslFile;

            xmlObj xObj = new xmlObj(xml.toString());
            xObj.matchTag("recipient");
            String[] recipMails = xObj.getAttributeValues("entity", "email");
            String[] recipNames = xObj.getAttributeValues("entity", "display_name");
            xObj.matchTag("carboncopy");
            String[] cc = xObj.getAttributeValues("entity", "email");
            String[] ccNames = xObj.getAttributeValues("entity", "display_name");
            xObj.matchTag("\\");
            String senderMail = xObj.getAttributeValue("sender", "email");
            String senderName = xObj.getAttributeValue("sender", "display_name");
            String msgSubject = xObj.getNodeValue("subject");
            String msgBody = xObj.getNodeValue("body");

            try{
                msgBody = xmlObj.transformByXSL(xml.toString(), xslFile, static_env.ENCODING);
            }catch( SAXException se ) {
                throw new cwException("SAXException, Failed to transform by xsl : " + se);
            }

            MessageOutbox msgOut = new MessageOutbox(channel, api);
            msgOut.setMailServer(static_env.INI_MAIL_SERVER);
            msgOut.setMsgRecipients(recipMails, recipNames);
            msgOut.setMsgCC(cc, ccNames);
            msgOut.setMsgSender(senderMail, senderName);
            msgOut.setMsgSubject(msgSubject);
            msgOut.setMsgBody(msgBody);
            msgOut.send();

            return;
        }

*/





    public static String getRecipientsStatusXml(Connection con, long msg_id, String idGroup)
        throws SQLException, cwException {

            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_id = msg_id;
            dbMsg.get(con);

            Vector vec = DbMgView.getRecipientStatus(con, msg_id, null, null, 0, idGroup, 0);
            Vector entIdVec = new Vector();
            Vector statusVec = new Vector();

            if(!vec.isEmpty()) {
                entIdVec = (Vector)vec.elementAt(1);
                statusVec = (Vector)vec.elementAt(2);
            }
            StringBuffer xml = new StringBuffer();

            if(dbMsg.msg_target_datetime != null)
                xml.append("<message target_datetime=\"").append(dbMsg.msg_target_datetime).append("\">").append(dbUtils.NEWL);
            else
                xml.append("<message target_datetime=\"\">").append(dbUtils.NEWL);
            for(int i=0; i<entIdVec.size(); i++) {
                xml.append("<recipient ent_id=\"").append(entIdVec.elementAt(i)).append("\" ");
                xml.append(" status=\"").append(statusVec.elementAt(i)).append("\"/>").append(dbUtils.NEWL);
            }
            xml.append("</message>").append(dbUtils.NEWL);
            return xml.toString();
        }






    //get recipient of ji message and the notification date
    public static Hashtable getSentDatetime(Connection con, long itm_id, String type)
        throws SQLException, cwException {

            long[] msgIds = DbMgItmSelectedMessage.getMessageIds(con, itm_id, type);
            if(msgIds != null)
                return DbMgView.getRecipientNotificationDate(con, msgIds, RECIPIENT, null, 0);
            else
                return null;

        }


    /**
    Get template id
    @param msg_type
    @param msg_subtype can be more than one value use '~' as the delimiter
    @return long array of template id
    */
    public void constructTemplateId(Connection con, String msg_type, String msg_subtype)
        throws SQLException{
        Vector idVec = new Vector();
        String[] subtype = dbUtils.split(msg_subtype.toUpperCase(), "~");

        String MESSAGE_GET_TEMPLATE_ID = " SELECT DISTINCT xtp_id FROM xslTemplate "
                                       + " WHERE xtp_type = ? AND ( xtp_subtype = ? ";

        for(int i=1; i<subtype.length; i++)
            MESSAGE_GET_TEMPLATE_ID += " OR xtp_subtype = ? ";

        MESSAGE_GET_TEMPLATE_ID += " ) ";
        int index = 1;
        PreparedStatement stmt = con.prepareStatement(MESSAGE_GET_TEMPLATE_ID);
        stmt.setString(index++, msg_type);
        stmt.setString(index, subtype[0]);
        for(int i=1; i<subtype.length; i++)
            stmt.setString(index+i, subtype[i]);

        ResultSet rs = stmt.executeQuery();
        while( rs.next() ){
        	idVec.addElement(new Long(rs.getLong("xtp_id")));
        }
        cwSQL.cleanUp(rs, stmt);
        xtp_id = new long[idVec.size()];
        for(int i=0; i<idVec.size(); i++)
            xtp_id[i] = ((Long)idVec.elementAt(i)).longValue();

        return;
    }




    public static String returnByUrl(String urlArgs, String enc)
        throws Exception {

            int index = urlArgs.indexOf("?");
            String url = urlArgs.substring(0,index);
            String args= urlArgs.substring(index+1, urlArgs.length());
            return returnByUrl(url, args, enc);

        }



    // call the url by http connection
    public static String returnByUrl(String url, String args, String enc) throws Exception {
        StringBuffer xml = new StringBuffer();

        args += "&";
        StringBuffer requestParam = new StringBuffer();
        int index = args.indexOf("&");
        while (index > 0) {
            String element = args.substring(0, index);
            int subIndex = element.indexOf("=");
            int strLength = element.length();
            requestParam.append(URLEncoder.encode(element.substring(0, subIndex)));
            requestParam.append("=");
            requestParam.append(URLEncoder.encode(element.substring(subIndex + 1, strLength)));
            requestParam.append("&");
            args = args.substring(index + 1);
            index = args.indexOf("&");
        }

        if (url != null && url.length() > 0) {
            xml.append(SendHttpRequest.sendUrl(url, requestParam.toString(), enc, null, null ));
        }

        return (xml.toString()).trim();
    }





    public static String returnByHttpApi(String urlArgs, String value)
        throws Exception {

            int index = urlArgs.indexOf("?");
            String url = urlArgs.substring(0,index);
            String args= urlArgs.substring(index+1, urlArgs.length());
            return returnByHttpApi(url, args, value);

        }

    public static String returnByHttpApi(String url, String args, String value) throws Exception {
        StringBuffer xml = new StringBuffer();
        args += "&";

        StringBuffer requestParam = new StringBuffer();
        int index = args.indexOf("&");
        while (index > 0) {
            String element = args.substring(0, index);
            int subIndex = element.indexOf("=");
            int strLength = element.length();
            requestParam.append(URLEncoder.encode(element.substring(0, subIndex)));
            requestParam.append("=");
            requestParam.append(URLEncoder.encode(element.substring(subIndex + 1, strLength)));
            requestParam.append("&");
            args = args.substring(index + 1);
            index = args.indexOf("&");
        }
        if (value != null && value.length() > 0)
            requestParam.insert(requestParam.length() - 1, URLEncoder.encode(value));
        if (url != null && url.length() > 0) {
            xml.append(SendHttpRequest.sendUrl(url, requestParam.toString(), null, null, null));
        }
        return (xml.toString()).trim();
    }













    /**
    Generate a XML of the message
    @param msg_id long value of the message id
    @return string of XML
    */
    public static String getMessage(Connection con, long msg_id)
        throws SQLException, cwException {

            StringBuffer xml = new StringBuffer();

            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_id = msg_id;
            dbMsg.get(con);

            String[] methods = DbMgView.getSendMethod(con, msg_id);

            xml.append("<message id=\"").append(dbMsg.msg_id).append("\" ");
            xml.append(" target_datetime=\"").append(dbMsg.msg_target_datetime).append("\"/>").append(dbUtils.NEWL);
            xml.append("<subject>").append(dbMsg.msg_subject).append("</subject>").append(dbUtils.NEWL);
            xml.append("<body>").append(dbMsg.msg_addition_note).append("</body>").append(dbUtils.NEWL);
                if(methods != null) {
                    xml.append("<sned>").append(dbUtils.NEWL);
                    for(int i=0; i<methods.length; i++)
                        xml.append("<by>").append(methods[i]).append("</by>").append(dbUtils.NEWL);
                    xml.append("</send>").append(dbUtils.NEWL);
                }
            xml.append("</message>").append(dbUtils.NEWL);

            return xml.toString();

        }

    // delete message
    public static void delMessage(Connection con, long msg_id)
        throws SQLException, cwException {

            DbMgItmSelectedMessage.delRecords(con, msg_id);
            DbMgRecHistory.delRecords(con, msg_id);
            DbMgRecipient.delRecords(con, msg_id);
            DbMgXslParamValue.delRecords(con, msg_id);
            DbXslMgSelectedTemplate.delRecords(con, msg_id);
            DbMgMessage.delRecords(con, msg_id);

            return;
        }


    public String getItmMsgStatus(Connection con, long itm_id, String[] entIds)
        throws SQLException {

            StringBuffer xml = new StringBuffer();
            StringBuffer xmlBody = new StringBuffer();
            DbMgView mgView = new DbMgView();
            DbMgView.ViewMgStatus[] rmdMgSts = mgView.getUserReminderStatus(con, itm_id, entIds);
            Timestamp send_datetime = null;
            for (int i = 0; i < rmdMgSts.length; i++) {
                send_datetime = rmdMgSts[i].msg_target_datetime;
                xmlBody.append("<usr ent_id=\"").append(rmdMgSts[i].rec_ent_id).append("\"/>").append(cwUtils.NEWL);
            }
            if( send_datetime != null )
                xml.append("<reminder_list send_datetime=\"").append(send_datetime).append("\">").append(cwUtils.NEWL);
            else
                xml.append("<reminder_list send_datetime=\"\">").append(cwUtils.NEWL);
            xml.append(xmlBody);
            xml.append("</reminder_list>").append(cwUtils.NEWL);
            xml.append("<ji_list>").append(cwUtils.NEWL);
            DbMgView.ViewMgStatus[] jiMgSts = mgView.getUserJIStatus(con, itm_id, entIds);
            for (int i = 0; i < jiMgSts.length; i++) {
                xml.append("<usr ent_id=\"").append(jiMgSts[i].rec_ent_id).append("\" ")
                   .append(" send_datetime=\"").append(jiMgSts[i].msg_target_datetime).append("\"/>")
                   .append(cwUtils.NEWL);
            }
            xml.append("</ji_list>").append(cwUtils.NEWL);

            return xml.toString();

        }



    public void updRecStatus(Connection con, Vector vec, String rec_type)
        throws SQLException {
            
            if( vec == null || vec.isEmpty() )
                return;
            Vector recIdVec = DbMgRecipient.getRecipientId(con, msg_id, rec_type, vec);
            DbMgRecHistory mghObj = new DbMgRecHistory();
            mghObj.mgh_status = "Y";
            mghObj.mgh_sent_datetime = cwSQL.getTime(con);
            mghObj.mgh_attempted = 0;
            mghObj.updRecStatus(con, recIdVec);
            return;
            
        }



    public static Vector getAllUserEntId(Connection con, Vector vec)
        throws SQLException, cwException{

            Vector allGroups = new Vector();
            allGroups.addAll(vec);
            allGroups.addAll(dbUserGroup.getAllSubGroupVec(con, vec));
            dbUtils.removeDuplicate(allGroups);
            
            Vector usrIdVec = new Vector();
            try{
                usrIdVec = dbUserGroup.getUserEntityIdVec(con, allGroups, true);
            }catch(qdbException e){
                throw new cwException(e.getMessage());
            }
            dbUtils.removeDuplicate(usrIdVec);
            return usrIdVec;
            
        }


    /**
    Insert a Message
    @param recipient entity id
    @param carbon copy user entity id
    @param template type
    @param template subtype
    @param class of dbMessage
    @param hashtable containing params name and param values
    */
    public void insNotify(Connection con, long[] ent_id, long[] cc_ent_id, String xtp_type, String[] xtp_subtype, DbMgMessage dbMsg, Vector params)
        throws SQLException , cwException {
            insNotify(con, ent_id, cc_ent_id, xtp_type, xtp_subtype, dbMsg, params, false);
            return;
        }

    public void insNotify(Connection con, long[] ent_id, long[] cc_ent_id, String xtp_type, String[] xtp_subtype, DbMgMessage dbMsg, Vector params, boolean wAttachment)
        throws SQLException , cwException {
            insNotify(con, ent_id, cc_ent_id, null, xtp_type, xtp_subtype, dbMsg, params, wAttachment, null);
            return;
        }

    public void insNotify(Connection con, long[] ent_id, long[] cc_ent_id, long[] bcc_ent_id, String xtp_type, String[] xtp_subtype, DbMgMessage dbMsg, Vector params, boolean wAttachment, long[] reply_ent_id)
        throws SQLException , cwException {

            Vector paramsName  = (Vector)params.elementAt(0);
            Vector paramsType  = (Vector)params.elementAt(1);
            Vector paramsValue = (Vector)params.elementAt(2);

            if( dbMsg.msg_subject == null || dbMsg.msg_subject.length() <= 0 ){
                dbMsg.msg_subject = subToken( DbXslTemplate.getTemplateTitle(con, xtp_type, xtp_subtype[0]), dbMsg.msg_subject_token);
            }

            dbMsg.ins(con);
            msg_id = dbMsg.msg_id;
            if( wAttachment ) {
                String[] subtype = { ATTACHMENT };
                long[] xtpIds = DbXslTemplate.getXtpIds(con, xtp_type, subtype);
                for(int i=0; i<xtpIds.length; i++) {
                    DbXslMgSelectedTemplate dbMst = new DbXslMgSelectedTemplate();
                    dbMst.mst_msg_id = msg_id;
                    dbMst.mst_xtp_id = xtpIds[i];
                    dbMst.mst_type = ATTACHMENT;
                    dbMst.ins(con);
                }
            }
            long[] xtpIds = DbXslTemplate.getXtpIds(con, xtp_type, xtp_subtype);
            //insert Selected Template Record
            for(int i=0; i<xtpIds.length; i++) {

                DbXslMgSelectedTemplate dbMst = new DbXslMgSelectedTemplate();
                dbMst.mst_msg_id = msg_id;
                dbMst.mst_xtp_id = xtpIds[i];
                dbMst.mst_type = MAIN;
                dbMst.ins(con);

                Hashtable paramsIdNameTable = DbXslParamName.getParamsName(con, xtpIds[i], true);
                DbMgXslParamValue dbParams = new DbMgXslParamValue();
                for(int j=0; j<paramsName.size(); j++) {
                    dbParams.xpv_mst_msg_id = msg_id;
                    dbParams.xpv_mst_xtp_id = xtpIds[i];
                    dbParams.xpv_xpn_id = ((Long)paramsIdNameTable.get((String)paramsName.elementAt(j))).longValue();
                    dbParams.xpv_type = (String)paramsType.elementAt(j);
                    dbParams.xpv_value = (String)paramsValue.elementAt(j);
                    dbParams.ins(con);
                }
            }
            if( ent_id != null && ent_id.length > 0  )
                for(int i=0; i<ent_id.length; i++)
                    insertRecipient(con, ent_id[i], xtpIds, RECIPIENT);
            if( cc_ent_id != null && cc_ent_id.length > 0 ) 
                for(int i=0; i<cc_ent_id.length; i++)
                    insertRecipient(con, cc_ent_id[i], xtpIds, CARBONCOPY);
            if( bcc_ent_id != null && bcc_ent_id.length > 0 ) 
                for(int i=0; i<bcc_ent_id.length; i++)
                    insertRecipient(con, bcc_ent_id[i], xtpIds, BLINDCARBONCOPY);
            if (reply_ent_id != null && reply_ent_id.length > 0)
            	for (int i = 0; i < reply_ent_id.length; i++)
            		insertRecipient(con, reply_ent_id[i], xtpIds, REPLYTO);
            return;
        }


     public void insRecipientForJI(Connection con, long ent_id, String xtp_type, String[] xtp_subtype, String status)
        throws SQLException , cwException {
            
            long[] xtpIds = DbXslTemplate.getXtpIds(con, xtp_type, xtp_subtype);
            DbMgRecipient dbRec = new DbMgRecipient();
            dbRec.rec_msg_id = msg_id;
            dbRec.rec_ent_id = ent_id;
            dbRec.rec_type = RECIPIENT;
            dbRec.ins(con);

            //insert Recipient History Record
            for(int j=0; j<xtpIds.length; j++) {
                DbMgRecHistory dbHis = new DbMgRecHistory();
                dbHis.mgh_rec_id = dbRec.rec_id;
                dbHis.mgh_mst_msg_id = msg_id;
                dbHis.mgh_mst_xtp_id = xtpIds[j];
                dbHis.ins(con, status);
            }
            return;
    }



    public void updateSender(Connection con, loginProfile prof)
        throws SQLException{
            DbMgView mgView = new DbMgView();
            mgView.updateSender(con, msg_id, prof.usr_id);
            return;
        }



    public static void errorToLog(Connection con, long xtp_id, long msg_id, String error, qdbEnv static_env)
        throws SQLException, cwException {

            String subtype = new String();
            String SQL = " SELECT xtp_subtype FROM xslTemplate WHERE xtp_id = ? ";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, xtp_id);
            ResultSet rs = stmt.executeQuery();
            if( rs.next() )
                subtype = rs.getString("xtp_subtype");

            stmt.close();
            
//            String logFolderStr = static_env.DOC_ROOT + dbUtils.SLASH + static_env.LOG_FOLDER;
            File logFolder = new File(qdbAction.static_env.logFolderPath);
            if( !logFolder.exists() )
                logFolder.mkdir();

            String content = new String();
            Timestamp curTime = cwSQL.getTime(con);
            content += curTime + "          [message id = " + msg_id + "]" + System.getProperty("line.separator");
            content += error + System.getProperty("line.separator") + System.getProperty("line.separator");

            try{
                File logFile = new File(logFolder, static_env.MAIL_NOTES_LOG);
                FileWriter fw = new FileWriter(logFile.toString() , true);
                fw.write(content);
                fw.flush();
                fw.close();
            }catch (IOException e) { throw new cwException(e.getMessage()); }

            return;
        }

    public static DbXslTemplate[] getAttachmentTemplate(Connection con, long msg_id)
        throws SQLException{
        DbMgView dbMg = new DbMgView();
        DbXslTemplate[] xslTpl = dbMg.getTemplate(con, msg_id, ATTACHMENT);
        return xslTpl;
    }


    /**
    * If sent on today, replace the hh:mm:ss of the timestamp to current time
    * It can prevent it from sending before confirmation message
    */
    public static Timestamp replaceToCurrentTime(Connection con, Timestamp timestamp, int second)
        throws SQLException{
            Timestamp current_timestamp = cwSQL.getTime(con);
            Date date = new Date(current_timestamp.getTime());
            int curYear = date.getYear();
            int curMon = date.getMonth();
            int curDate = date.getDate();
            int curHour = date.getHours();
            int curMin = date.getMinutes();
            int curSs = date.getSeconds();
                        
            date = new Date(timestamp.getTime());
            if( curYear == date.getYear() && curMon == date.getMonth() && curDate == date.getDate() ) {
                date.setHours(curHour);
                date.setMinutes(curMin);
                date.setSeconds(curSs + second);
            }
            return new Timestamp(date.getTime());

        }


    /**
    * Remove it's record from messaging table if the user is removed from the item
    */
    public void removeItemRecipient(Connection con, long itm_id, long usr_ent_id)
        throws SQLException {
            
            Vector recIdVec = DbMgView.getItemRecipient(con, itm_id, usr_ent_id);
            if(recIdVec.isEmpty())
                return;
            DbMgRecHistory dbRecHis = new DbMgRecHistory();
            dbRecHis.delByRecId(con, recIdVec);
            DbMgRecipient dbRec = new DbMgRecipient();
            dbRec.delByRecId(con, recIdVec);
            return;
            
        }

    public String subToken(String subject, Vector tokenVec)
        throws cwException {
            
            if( subject == null || subject.length() == 0 || tokenVec.isEmpty() )
                return subject;
            
            int index = subject.indexOf(DbMgMessage.subjectTokenTag);
            int cnt = 0;
            while( index != -1 && cnt < tokenVec.size() ){
                subject = subject.substring(0, index) 
                        + tokenVec.elementAt(cnt++)
                        + subject.substring(index+DbMgMessage.subjectTokenTag.length());
                index = subject.indexOf(DbMgMessage.subjectTokenTag);
            }
            return subject;
        }
        
    public static Vector getMsgAttachments(Connection con, long msg_id)
        throws SQLException, cwException {

            String SQL = " Select xpv_value from mgxslParamValue, xslParamName "
                + " Where xpv_mst_msg_id = ? and xpv_xpn_id = xpn_id and lower(xpn_name) like ? and xpv_value is not null ";

            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, msg_id);
            stmt.setString(2, "%attachment%");
            Vector fileVec = new Vector();
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                fileVec.addElement(rs.getString("xpv_value"));
            }
            stmt.close();
            
            return fileVec;
        }
}