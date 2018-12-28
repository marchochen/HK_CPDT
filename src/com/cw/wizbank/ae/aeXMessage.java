package com.cw.wizbank.ae;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
//import com.cw.wizbank.utils.rsa.*;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.message.*;
import com.cw.wizbank.db.DbMgMessage;
import com.cwn.wizbank.utils.CommonLog;

import java.net.URLEncoder;



public class aeXMessage {

    public final static String COMMA            =   ",";
    public final static String STATIC           =   "STATIC";
    public final static String DYNAMIC          =   "DYNAMIC";



    // recipient entity id
    public long ent_id;

    //sender user id
    public String sender_id;

    public String msg_type;
    public long id;
    public String id_type;
    public String reply_to;

    // added to get the application, by Emily, 2002-10-13
    public long app_id;
    
    public long action_taker_ent_id;    //usr_ent_id of the user who trigger this message

    public String getReplyToXml(Connection con, String mail_account)
        throws cwException {

            boolean isNum = true;
            long entId = 0;
            try{
                entId = Long.parseLong(reply_to);
            }catch( NumberFormatException e ) {
                isNum = false;
            }
            if( isNum ) {
                dbRegUser dbUser = new dbRegUser();
                dbUser.usr_ent_id = entId;
                try{
                    dbUser.get(con);
                }catch( qdbException e ) {
                    throw new cwException("Failed to get Recipient detail, id=" + ent_id + " : " + e);
                }
                StringBuffer xml = new StringBuffer();
                String userMail = dbUser.usr_email;
                if(mail_account.equalsIgnoreCase("NOTES"))
                    userMail = dbUser.usr_email_2;
                xml.append("<reply_to>").append(cwUtils.NEWL)
                   .append("<entity ")
                   .append(" ent_id=\"").append(entId).append("\" ")
                   .append(" display_name=\"").append(dbUser.usr_display_bil).append("\" ")
                   .append(" email=\"").append(dbUtils.esc4XML(userMail)).append("\" ")
                   .append(" />").append(cwUtils.NEWL);
                xml.append("</reply_to>").append(cwUtils.NEWL);
                return xml.toString();
            } else {
                return new String();
            }

        }

    public String getRecipientXml(Connection con, long ent_id, String mail_account, long DES_KEY)
        throws cwException {
            this.ent_id = ent_id;
            return getRecipientXml(con, mail_account, DES_KEY);
        }

    public String getRecipientXml(Connection con, String mail_account, long DES_KEY)
        throws cwException {


            if(ent_id == 0)
                return new String();

            StringBuffer xml = new StringBuffer();
            dbRegUser dbRecip = new dbRegUser();
            dbRecip.usr_ent_id = ent_id;

            try{
                dbRecip.get(con);
            }catch( qdbException e ) {
                throw new cwException("Failed to get Recipient detail, id=" + ent_id + " : " + e);
            }

            String userMail = dbRecip.usr_email;
            if(mail_account.equalsIgnoreCase("NOTES"))
                userMail = dbRecip.usr_email_2;

            xml.append("<recipient>").append(cwUtils.NEWL)
               .append("<entity ent_id=\"").append(dbRecip.usr_ent_id).append("\" ")
               .append(" usr_id=\"").append(dbUtils.esc4XML(dbRecip.usr_id)).append("\" ")
               .append(" display_name=\"").append(dbUtils.esc4XML(dbRecip.usr_display_bil)).append("\" ")
               .append(" email=\"").append(dbUtils.esc4XML(userMail)).append("\" ")
               .append(" ste_usr_id=\"").append(dbUtils.esc4XML(dbRecip.usr_ste_usr_id)).append("\" ");

            if( dbRecip.usr_pwd != null && dbRecip.usr_pwd.length() > 0 ) {
                //RSA_instance r = new RSA_instance();
                //RSA encoder = new RSA(dbRecip.usr_pwd, r, true);
                //xml.append(" usr_pwd=\"").append((encoder.encodeString()).trim()).append("\" ");
                MessageCryptography msgCrypto = new MessageCryptography(DES_KEY);
                xml.append(" usr_pwd=\"").append(URLEncoder.encode(msgCrypto.encrypt(dbRecip.usr_pwd))).append("\" ");
            } else {
                xml.append(" usr_pwd=\"\" ");
            }
            xml.append("/>");
            xml.append("</recipient>").append(cwUtils.NEWL);

            return xml.toString();

        }
    
    public String getEntXml(Connection con)
    throws cwException {


        if(ent_id == 0)
            return new String();

        StringBuffer xml = new StringBuffer();
        dbRegUser dbRecip = new dbRegUser();
        dbRecip.usr_ent_id = ent_id;

        try{
            dbRecip.get(con);
        }catch( qdbException e ) {
            throw new cwException("Failed to get Recipient detail, id=" + ent_id + " : " + e);
        }

            xml.append("<entity ent_id=\"").append(dbRecip.usr_ent_id).append("\" ")
           .append(" usr_id=\"").append(dbUtils.esc4XML(dbRecip.usr_id)).append("\" ")
           .append(" display_name=\"").append(dbUtils.esc4XML(dbRecip.usr_display_bil)).append("\" ")
           .append(" ste_usr_id=\"").append(dbUtils.esc4XML(dbRecip.usr_ste_usr_id)).append("\" ")
           .append("/>");
       

        return xml.toString();

    }


    public String getSenderXml(Connection con, String sender_id, String mail_account)
        throws cwException {
            this.sender_id = sender_id;
            return getSenderXml(con, mail_account);
        }

    public String getSenderXml(Connection con, String mail_account)
        throws cwException {

            if(sender_id == null || sender_id.length() == 0)
                return "";

            StringBuffer xml = new StringBuffer();
            dbRegUser dbSender = new dbRegUser();
            dbSender.usr_id = sender_id;

            try{
                dbSender.usr_ent_id = dbSender.getEntId(con);
                dbSender.get(con);
            }catch( qdbException e ) {
                throw new cwException("Failed to get Sender detail, id = " + sender_id + " : " + e);
            }

            String userMail = dbSender.usr_email;
            if(mail_account.equalsIgnoreCase("NOTES"))
                userMail = dbSender.usr_email_2;

            xml.append("<sender display_name=\"").append(dbUtils.esc4XML(dbSender.usr_display_bil)).append("\" ")
               .append(" usr_id=\"").append(dbUtils.esc4XML(dbSender.usr_id)).append("\" ")
               .append(" ste_usr_id=\"").append(dbUtils.esc4XML(dbSender.usr_ste_usr_id)).append("\" ")
               .append(" email=\"").append(dbUtils.esc4XML(userMail)).append("\" />").append(cwUtils.NEWL);

            return xml.toString();

        }

    public String getActionTakerXml(Connection con, String mail_account)
        throws cwException {

            if(action_taker_ent_id == 0)
                return "";

            StringBuffer xml = new StringBuffer();
            dbRegUser dbActionTaker = new dbRegUser();
            dbActionTaker.usr_ent_id = action_taker_ent_id;
            dbActionTaker.ent_id = action_taker_ent_id;

            try{
                dbActionTaker.get(con);
            }catch( qdbException e ) {
                throw new cwException("Failed to get Action Taker detail, id = " + sender_id + " : " + e);
            }

            String userMail = dbActionTaker.usr_email;
            if(mail_account.equalsIgnoreCase("NOTES"))
                userMail = dbActionTaker.usr_email_2;

            xml.append("<action_taker display_name=\"").append(dbUtils.esc4XML(dbActionTaker.usr_display_bil)).append("\" ")
               .append(" usr_id=\"").append(dbUtils.esc4XML(dbActionTaker.usr_id)).append("\" ")
               .append(" ste_usr_id=\"").append(dbUtils.esc4XML(dbActionTaker.usr_ste_usr_id)).append("\" ")
               .append(" email=\"").append(dbUtils.esc4XML(userMail)).append("\" />").append(cwUtils.NEWL);

            return xml.toString();

        }

    public String getReourceXml(Connection con)
        throws cwException {

            StringBuffer xml = new StringBuffer();
            dbResource dbRes = new dbResource();
            dbRes.res_id = id;

            try{
                dbRes.get(con);
            }catch( Exception e) {
                throw new cwException("Failed to get the Resource detail, id = " + id + " : " + e);
            }

            xml.append("<resource id=\"").append(dbRes.res_id).append("\" ");
            xml.append(" type=\"").append(dbRes.res_type).append("\" ");
            xml.append(" subtype=\"").append(dbRes.res_subtype).append("\">").append(cwUtils.NEWL);
            xml.append("<title>").append(dbRes.res_title).append("</title>").append(cwUtils.NEWL);
            xml.append("<desc>").append(dbRes.res_desc).append("</desc>").append(cwUtils.NEWL);
            xml.append("</resource>").append(cwUtils.NEWL);

            return xml.toString();

        }

    public String getApplicationXml(Connection con) throws SQLException, qdbException {
        aeApplication app = new aeApplication();
        app.app_id = app_id;
        app.get(con);
        return app.contentAsXML(con, null, false, false, false);
    }


    public String getItemXml(Connection con)
        throws cwException, SQLException {

            StringBuffer xml = new StringBuffer();

            long parent_id = 0;
            String parent_title = null;
            aeItem aeItm = new aeItem();
            aeItm.itm_id = id;

//            if( id_type.equalsIgnoreCase("ITEM") ) {
                try{
                    aeItm.getItem(con);
                    aeItemRelation aeIre = new aeItemRelation();
                    aeIre.ire_child_itm_id = id;
                    aeItem ireParentItm = aeIre.getParentInfo(con);
                    if (ireParentItm != null) {
                        parent_id = ireParentItm.itm_id;
                        parent_title = ireParentItm.itm_title;
                    }
                }catch( Exception e) {
                    throw new cwException("Failed to get the Item detail, id = " + id + " : " + e);
                }
//            }

            xml.append("<item id=\"").append(aeItm.itm_id).append("\" ")
               .append(" type=\"").append(aeItm.itm_type).append("\" ")
               .append(" itm_cap=\"").append(aeItm.itm_capacity).append("\" ")
               .append(" itm_fee_ccy=\"").append(aeItm.itm_fee_ccy).append("\" ")
               .append(" itm_fee=\"").append(aeItm.itm_fee).append("\" ");
               if( aeItm.itm_eff_start_datetime == null )
                    xml.append(" eff_start_datetime=\"\" ");
               else
                    xml.append(" eff_start_datetime=\"").append(aeItm.itm_eff_start_datetime).append("\" ");
               if( aeItm.itm_eff_end_datetime == null )
                    xml.append(" eff_end_datetime=\"\" >");
               else
                    xml.append(" eff_end_datetime=\"").append(aeItm.itm_eff_end_datetime).append("\" >");

               xml.append(cwUtils.NEWL)
                  .append("<self_title>").append(dbUtils.esc4XML(aeItm.itm_title)).append("</self_title>").append(cwUtils.NEWL)
                  .append("<self_code>").append(dbUtils.esc4XML(aeItm.itm_code)).append("</self_code>").append(cwUtils.NEWL);
               if( parent_id != 0 && parent_title != null && parent_title.length() > 0 )
                   xml.append("<parent_title>").append(dbUtils.esc4XML(parent_title)).append("</parent_title>").append(cwUtils.NEWL);

            aeItemAccess iac = new aeItemAccess();
            iac.iac_itm_id = aeItm.itm_id;
            xml.append(iac.getAssignedRoleList(con));

            xml.append("</item>").append(cwUtils.NEWL);

            return xml.toString();

        }

    public String getEmailsAsXml(Connection con, String ent_ids_str, String cc_ent_ids_str, String bcc_ent_ids_str, String mail_account)
        throws cwException, SQLException
    {
        long[] ent_ids = null;
        long[] cc_ent_ids = null;
        long[] bcc_ent_ids = null;

        if( ent_ids_str != null && ent_ids_str.length() > 0)
            ent_ids = dbUtils.string2LongArray(ent_ids_str, "~");

        if( cc_ent_ids_str != null && cc_ent_ids_str.length() > 0 )
            cc_ent_ids = dbUtils.string2LongArray(cc_ent_ids_str, "~");

        if( bcc_ent_ids_str != null && bcc_ent_ids_str.length() > 0 )
            bcc_ent_ids = dbUtils.string2LongArray(bcc_ent_ids_str, "~");

        return getEmails(con, ent_ids, cc_ent_ids, bcc_ent_ids, mail_account);

    }

    public String getEmails(Connection con, long[] ent_ids, long[] cc_ent_ids, String mail_account)
        throws SQLException, cwException {
            return getEmails(con, ent_ids, cc_ent_ids, null, mail_account);
        }


    public String getEmails(Connection con, long[] ent_ids, long[] cc_ent_ids, long[] bcc_ent_ids, String mail_account)
        throws SQLException, cwException {

            StringBuffer xml = new StringBuffer();

            String dbXMessage_GET_USR_EMAIL = " SELECT usr_ent_id, usr_ste_usr_id, usr_display_bil, usr_email USERMAIL, usr_id FROM RegUser WHERE usr_ent_id IN ";

            if(mail_account.equalsIgnoreCase("NOTES"))
                dbXMessage_GET_USR_EMAIL = " SELECT usr_ent_id, usr_ste_usr_id, usr_display_bil, usr_email_2 USERMAIL, usr_id FROM RegUser WHERE usr_ent_id IN ";

            PreparedStatement stmt = null;
            ResultSet rs = null;

            if (ent_ids != null && ent_ids.length > 0) {
                StringBuffer entStr = new StringBuffer().append("(0");
                for(int i=0; i<ent_ids.length; i++)
                    entStr.append(COMMA).append(ent_ids[i]);
                entStr.append(")");

                xml.append("<recipient>").append(cwUtils.NEWL);
                stmt = con.prepareStatement(dbXMessage_GET_USR_EMAIL + entStr.toString());
    //System.out.println("SQL + " + dbXMessage_GET_USR_EMAIL + entStr.toString());
                rs = stmt.executeQuery();
                while( rs.next() ) {
                    xml.append("<entity display_name=\"").append(dbUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                    .append(" ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
                    .append(" usr_id=\"").append(rs.getString("usr_id")).append("\" ")
                    .append(" ste_usr_id=\"").append(dbUtils.esc4XML(rs.getString("usr_ste_usr_id"))).append("\" ")
                    .append(" email=\"").append(dbUtils.esc4XML(rs.getString("USERMAIL"))).append("\" />").append(cwUtils.NEWL);
                }
                stmt.close();
                xml.append("</recipient>").append(cwUtils.NEWL);
            }

            if (cc_ent_ids != null && cc_ent_ids.length > 0) {
                StringBuffer ccEntStr = new StringBuffer().append("(0");
                for(int i=0; i<cc_ent_ids.length; i++)
                    ccEntStr.append(COMMA).append(cc_ent_ids[i]);
                ccEntStr.append(")");

                xml.append("<carboncopy>").append(cwUtils.NEWL);
                stmt = con.prepareStatement(dbXMessage_GET_USR_EMAIL + ccEntStr.toString());
                rs = stmt.executeQuery();
                while( rs.next() ) {
                    xml.append("<entity display_name=\"").append(dbUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                    .append(" ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
                    //.append(" usr_ste_usr_id=\"").append(dbUtils.esc4XML(rs.getString("usr_ste_usr_id"))).append("\" ")
                    .append(" email=\"").append(dbUtils.esc4XML(rs.getString("USERMAIL"))).append("\" />").append(cwUtils.NEWL);
                }
                stmt.close();
                xml.append("</carboncopy>").append(cwUtils.NEWL);
            }

            if (bcc_ent_ids != null && bcc_ent_ids.length > 0) {
                StringBuffer bccEntStr = new StringBuffer().append("(0");
                for(int i=0; i<bcc_ent_ids.length; i++)
                    bccEntStr.append(COMMA).append(bcc_ent_ids[i]);
                bccEntStr.append(")");

                xml.append("<blindcarboncopy>").append(cwUtils.NEWL);
                stmt = con.prepareStatement(dbXMessage_GET_USR_EMAIL + bccEntStr.toString());
    //System.out.println(">>>>>>> sql = " + dbXMessage_GET_USR_EMAIL + bccEntStr.toString());
                rs = stmt.executeQuery();
                while( rs.next() ) {
                    xml.append("<entity display_name=\"").append(dbUtils.esc4XML(rs.getString("usr_display_bil"))).append("\" ")
                    .append(" ent_id=\"").append(rs.getLong("usr_ent_id")).append("\" ")
                    //.append(" usr_ste_usr_id=\"").append(dbUtils.esc4XML(rs.getString("usr_ste_usr_id"))).append("\" ")
                    .append(" email=\"").append(dbUtils.esc4XML(rs.getString("USERMAIL"))).append("\" />").append(cwUtils.NEWL);
                }
                stmt.close();
                xml.append("</blindcarboncopy>").append(cwUtils.NEWL);
            }

            return xml.toString();

        }




    public String initMsg(Connection con, String msg_type, long id, String id_type, long[] usr_ent_id, loginProfile prof, String[] groups, String process_status)
        throws SQLException, cwException {
//System.out.println("message type = " + msg_type);

            StringBuffer xmlBody = new StringBuffer();
            xmlBody.append("<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>").append(cwUtils.NEWL);


            xmlBody.append("<message type=\"").append(msg_type).append("\" ");

            Timestamp curTime;
            try{
                curTime = dbUtils.getTime(con);
            }catch( qdbException e ) {
                throw new cwException( e.getMessage() );
            }
            xmlBody.append(" timestamp=\"").append(curTime).append("\" ");

            if( msg_type.equalsIgnoreCase("JI") ) {
                aeItem aeItm = new aeItem();
                aeItm.itm_id = id;
                try{
                    aeItm.getItem(con);
                }catch( Exception e) {
                    throw new cwException("Failed to get the Item detail, id = " + id + " : " + e);
                }
                xmlBody.append(" send_datetime=\"").append(aeItm.itm_eff_start_datetime).append("\" ");
            }



            if( id_type != null && id_type.length() > 0 )
                xmlBody.append(" id_type=\"").append(id_type).append("\" ");


            if( id_type != null && id_type.length() > 0 && id > 0 ) {

                if( id_type.equalsIgnoreCase("ITEM") ) {
                    aeItem aeItm = new aeItem();
                    aeItm.itm_id = id;
                    try{
                        aeItm.getItem(con);
                    }catch( Exception e) {
                        throw new cwException("Failed to get the Item detail, id = " + id + " : " + e);
                    }
                    xmlBody.append(" title=\"").append(dbUtils.esc4XML(aeItm.itm_title)).append("\" ");
                    xmlBody.append(" send_datetime=\"").append(aeItm.itm_eff_start_datetime).append("\" ");

                } else if( id_type.equalsIgnoreCase("RESOURCE") ) {

                    dbResource dbRes = new dbResource();
                    dbRes.res_id = id;
                    try{
                        dbRes.get(con);
                    }catch( Exception e) {
                        throw new cwException("Failed to get the Resource detail, id = " + id + " : " + e);
                    }
                    xmlBody.append(" title=\"").append(dbUtils.esc4XML(dbRes.res_title)).append("\" ");

                }
            }

            if( id > 0 )
                xmlBody.append(" id=\"").append(id).append("\">").append(cwUtils.NEWL);
            else
                xmlBody.append(">").append(cwUtils.NEWL);

            xmlBody.append(prof.asXML()).append(cwUtils.NEWL);



            if( msg_type.equalsIgnoreCase("LINK_NOTIFY") ) {
                xmlBody.append("<message_body>").append(cwUtils.NEWL)
                       .append("<subject/>").append(cwUtils.NEWL)
                       .append("<body/>").append(cwUtils.NEWL)
                       .append("</message_body>").append(cwUtils.NEWL);


            } else if ( msg_type.equalsIgnoreCase("NOTIFY") ) {
                xmlBody.append("<message_body>").append(cwUtils.NEWL)
                       .append("<subject/>").append(cwUtils.NEWL)
                       .append("<body/>").append(cwUtils.NEWL)
                       .append("</message_body>").append(cwUtils.NEWL);


            } else if( msg_type.equalsIgnoreCase("ENROLLMENT_NOTIFY")) {
                xmlBody.append("<message_body>").append(cwUtils.NEWL)
                       .append("<subject/>").append(cwUtils.NEWL)
                       .append("<body/>").append(cwUtils.NEWL)
                       .append("</message_body>").append(cwUtils.NEWL);

                    xmlBody.append("<default_recipients>").append(cwUtils.NEWL);
                    
                    /*
                    aeQueueManager qm = new aeQueueManager();
                    aeApplication.ViewAppnUser[] appnUser = null;
                    try{
                        appnUser = qm.getEnrollmentUser(con, id, process_status, prof.root_ent_id);
                    }catch(IOException e){
                        throw new cwException(e.getMessage());
                    }
                    for (int i = 0; i < appnUser.length; i++) {
                        xmlBody.append("<recipient usr_id=\"").append(cwUtils.esc4XML(appnUser[i].usr_id)).append("\" ")
                               .append(" ent_id=\"").append(appnUser[i].usr_ent_id).append("\" ")
                               .append(" display_bil=\"").append(cwUtils.esc4XML(appnUser[i].usr_display_bil)).append("\"/>");
                    }
                    */
                    if(usr_ent_id != null) {
                        for(int i=0; i<usr_ent_id.length; i++) {
                            dbRegUser dbRecip = new dbRegUser();
                            try{
                                dbRecip.usr_ent_id = usr_ent_id[i];
                                dbRecip.get(con);
                            }catch( qdbException e ) {
                                throw new cwException("Failed to get Default Recipient detail, id = " + dbRecip.usr_id + " : " + e);
                            }
                            xmlBody.append("<recipient usr_id=\"").append(cwUtils.esc4XML(dbRecip.usr_id)).append("\" ")
                                .append(" ent_id=\"").append(dbRecip.usr_ent_id).append("\" ")
                                .append(" display_bil=\"").append(cwUtils.esc4XML(dbRecip.usr_display_bil)).append("\"/>");
                        }
                    }
                    xmlBody.append("</default_recipients>").append(cwUtils.NEWL);

            } else if( msg_type.equalsIgnoreCase("INVITE_TARGET_LEARNER")) {
            	CommonLog.debug("Invite target learner!");
                xmlBody.append("<message_body>").append(cwUtils.NEWL)
                       .append("<subject/>").append(cwUtils.NEWL)
                       .append("<body/>").append(cwUtils.NEWL)
                       .append("</message_body>").append(cwUtils.NEWL);

                aeItem aeItm = new aeItem();
                aeItm.itm_id = id;
                if( groups != null) {

                    xmlBody.append("<default_recipients>").append(cwUtils.NEWL);
                    aeItem.ViewNotifyUser[] ntfyUser = aeItm.inviteMsgInfo(con, groups);
                    for (int i = 0; i < ntfyUser.length; i++) {
                        xmlBody.append("<recipient usr_id=\"").append(cwUtils.esc4XML(ntfyUser[i].usr_id)).append("\" ")
                               .append(" ent_id=\"").append(ntfyUser[i].usr_ent_id).append("\" ")
                               .append(" display_bil=\"").append(cwUtils.esc4XML(ntfyUser[i].usr_display_bil)).append("\"/>");
                    }
                    xmlBody.append("</default_recipients>").append(cwUtils.NEWL);
                }

            } else if( msg_type.equalsIgnoreCase("COURSE_APPROVAL_REQUEST")) {

                aeItem aeItm = new aeItem();
                aeItm.itm_id = id;
                try{
                    aeItm.getItem(con);
                }catch( Exception e) {
                        throw new cwException("Failed to get the Resource detail, id = " + id + " : " + e);
                }

                xmlBody.append("<message_body>").append(cwUtils.NEWL)
                       .append("<subject>From e-Learning Centre - Approval of Executive Summary</subject>").append(cwUtils.NEWL)
                       .append("<body/>").append(cwUtils.NEWL)
                       .append("</message_body>").append(cwUtils.NEWL);

                xmlBody.append("<default_recipients>").append(cwUtils.NEWL);
//                aeItem.ViewNotifyUser[] ntfyUser = aeItm.requestApprovalInfo(con, prof.root_ent_id);
//                for (int i = 0; i < ntfyUser.length; i++) {
//                    xmlBody.append("<recipient usr_id=\"").append(cwUtils.esc4XML(ntfyUser[i].usr_id)).append("\" ")
//                           .append(" ent_id=\"").append(ntfyUser[i].usr_ent_id).append("\" ")
//                           .append(" display_bil=\"").append(cwUtils.esc4XML(ntfyUser[i].usr_display_bil)).append("\"/>");
//                }
                xmlBody.append("</default_recipients>").append(cwUtils.NEWL);

            } else if( msg_type.equalsIgnoreCase("JI") ) {

                xmlBody.append("<message_body>").append(cwUtils.NEWL)
                       .append("<subject/>").append(cwUtils.NEWL)
                       .append("<body/>").append(cwUtils.NEWL)
                       .append("</message_body>").append(cwUtils.NEWL);


                xmlBody.append("<default_recipients>").append(cwUtils.NEWL);
                aeApplication aeAppn = new aeApplication();
                aeApplication.ViewAppnUser[] appnUser = aeAppn.getAdmittedUser(con, id, 0);
                for (int i = 0; i < appnUser.length; i++) {
                    xmlBody.append("<recipient usr_id=\"").append(cwUtils.esc4XML(appnUser[i].usr_id)).append("\" ")
                           .append(" ent_id=\"").append(appnUser[i].usr_ent_id).append("\" ")
                           .append(" display_bil=\"").append(cwUtils.esc4XML(appnUser[i].usr_display_bil)).append("\"/>");
                }
                xmlBody.append("</default_recipients>").append(cwUtils.NEWL);


            } else if( msg_type.equalsIgnoreCase("COMMENT") ){


                xmlBody.append("<message_body>").append(cwUtils.NEWL)
                       .append("<subject/>").append(cwUtils.NEWL)
                       .append("<body/>").append(cwUtils.NEWL)
                       .append("</message_body>").append(cwUtils.NEWL);


                xmlBody.append("<default_recipients>").append(cwUtils.NEWL);
                dbRegUser dbRecip = new dbRegUser();

                //for(int i=0; i<usr_id.length; i++) {
                    dbRecip.usr_id = getSysUserEntId(con,prof);//usr_id[i];
                    try{
                        dbRecip.usr_ent_id = dbRecip.getEntId(con);
                        dbRecip.get(con);
                    }catch( qdbException e ) {
                        throw new cwException("Failed to get Default Recipient detail, id = " + dbRecip.usr_id + " : " + e);
                    }
                    xmlBody.append("<recipient usr_id=\"").append(cwUtils.esc4XML(dbRecip.usr_id)).append("\" ")
                           .append(" ent_id=\"").append(dbRecip.usr_ent_id).append("\" ")
                           .append(" display_bil=\"").append(cwUtils.esc4XML(dbRecip.usr_display_bil)).append("\"/>");
                //}
                xmlBody.append("</default_recipients>").append(cwUtils.NEWL);
            }

            xmlBody.append("</message>");
            return xmlBody.toString();
        }







    public void noShowNotify(Connection con, String sender_id, long usr_ent_id, int count, long itm_id, String[] msgSubject)
        throws SQLException, cwException {
            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_send_usr_id = sender_id;
            dbMsg.msg_create_usr_id = sender_id;
            dbMsg.msg_create_timestamp = cwSQL.getTime(con);
            dbMsg.msg_target_datetime = dbMsg.msg_create_timestamp;

            String[] xtp_subtype = null;
            String xtp_type = null;
            /*
            dbCourse dbCos = new dbCourse();
            dbCos.cos_res_id = cos_id;
            try{
                dbCos.get(con);
            } catch( qdbException e ) {
                throw new cwException(e.getMessage());
            } catch( cwSysMessage e ) {
                throw new cwException(e.getMessage());
            }
            */
            Vector vec = new Vector();

            if( count == 1 ) {

                xtp_subtype = new String[1];
                xtp_subtype[0] = "HTML";

                xtp_type = "LEARNER_NO_SHOW_FIRST_TIME";

                if(msgSubject[0] != null)
                    dbMsg.msg_subject = msgSubject[0];
                else
                    dbMsg.msg_subject = "1st NO SHOW";
                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
                String value[] = {sender_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", Long.toString(itm_id), "ITEM"};
                vec = notifyParams(name, type, value);

                //vec = notifyParams(sender_id, "notify_xml", Long.toString(dbCos.cos_itm_id) , "ITEM");


            } else if( count == 2 ) {

                xtp_subtype = new String[1];
                xtp_subtype[0] = "HTML";

                xtp_type = "LEARNER_NO_SHOW_SECOND_TIME";

                if( msgSubject[1] != null )
                    dbMsg.msg_subject = msgSubject[1];
                else
                    dbMsg.msg_subject = "2nd NO SHOW";

                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
                String value[] = {sender_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", Long.toString(itm_id), "ITEM"};
                vec = notifyParams(name, type, value);

            }  else {

                xtp_subtype = new String[1];
                xtp_subtype[0] = "HTML";

                xtp_type = "LEARNER_NO_SHOW";

                if( msgSubject[1] != null )
                    dbMsg.msg_subject = msgSubject[2];
                else
                    dbMsg.msg_subject = "NO SHOW";

                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
                String value[] = {sender_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", Long.toString(itm_id), "ITEM"};
                vec = notifyParams(name, type, value);

            }

            Message msg = new Message();
            long[] ent_id = { usr_ent_id };
            dbMsg.msg_bcc_sys_ind = true;
            msg.insNotify(con, ent_id, new long[0], xtp_type, xtp_subtype, dbMsg, vec);
        }




    public void itemCancellationNotify(Connection con, loginProfile prof, long itm_id, String reason, String msg_subject, long sender_ent_id, boolean cc_to_approver_ind, String[] cc_to_approver_rol_ext_id)
        throws SQLException, cwException, cwSysMessage, qdbException {
            String[] status = { "Admitted", "Pending", "Waiting" };
            Hashtable param = new Hashtable();
            String sender_usr_id;
            Vector ccEntIdVec = new Vector();
            
            if( msg_subject == null )
                param.put("subject", "Course Cancelled");
            else
                param.put("subject", msg_subject);
            param.put("template_type", "ITEM_CANCELLATION");
            param.put("template_subtype", "HTML");
            param.put("itm_id", new Long(itm_id));
            if( reason != null )
                param.put("body", reason);

            long[] ent_id = aeApplication.getEnrollUser(con, itm_id, status);

            CommonLog.debug("sender_ent_id:" + sender_ent_id);
            if(sender_ent_id == 0){
                sender_usr_id = prof.usr_id;  
            } else {
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = sender_ent_id;
                usr.getByEntId(con);
                sender_usr_id = usr.usr_id;                  
            }
            //check if need to cc mail to approver 
            CommonLog.debug("cc_to_approver_ind:" + cc_to_approver_ind);
            //
            if(cc_to_approver_ind){
                if(cc_to_approver_rol_ext_id == null || cc_to_approver_rol_ext_id.length == 0){
                    cc_to_approver_rol_ext_id = dbRegUser.getApproverRolExtId(con);
                    CommonLog.debug("cc_to_approver_rol_ext_id.size()" + cc_to_approver_rol_ext_id.length);
                }
                else{
                	CommonLog.debug("cc_to_approver_rol_ext_id != null");   
                }
                CommonLog.debug("after cc_to_approver_rol_ext_id.size()" + cc_to_approver_rol_ext_id.length);
            }
            
            long[] cc_ent_id = new long[ccEntIdVec.size()];
            for(int i=0; i<cc_ent_id.length; i++){
                cc_ent_id[i] = ((Long)ccEntIdVec.elementAt(i)).longValue();
               // System.out.println("cc_ent_id[i] " + cc_ent_id[i]);
            }

            if (ent_id.length > 0) {
                insNotify(con, prof, sender_usr_id, ent_id, cc_ent_id, cwSQL.getTime(con), param);
            }

            return;
        }
        
    public void itemJINotify(Connection con, loginProfile prof, long itm_id, String msg_subject, long sender_ent_id, String templateType, Timestamp sendTime)
        throws SQLException, cwException, cwSysMessage, qdbException {
            String[] status = { "Admitted", "Pending", "Waiting" };
            Hashtable param = new Hashtable();
            String sender_usr_id;
            Vector ccEntIdVec = new Vector();
            
            if( msg_subject == null ){
                param.put("subject", "Joining Instruction");
                if(templateType.equals("REMINDER"))
                    param.put("subject", "Reminder");
            }else
                param.put("subject", msg_subject);
            param.put("template_type", templateType);
            param.put("template_subtype", "HTML");
            param.put("itm_id", new Long(itm_id));
          
           // System.out.println("sender_ent_id:" + sender_ent_id);
            if(sender_ent_id == 0){
                sender_usr_id = prof.usr_id;  
            } else {
                dbRegUser usr = new dbRegUser();
                usr.usr_ent_id = sender_ent_id;
                usr.getByEntId(con);
                sender_usr_id = usr.usr_id;                  
            }
          
            insNotify(con, prof, sender_usr_id, new long[0], new long[0], sendTime, param);
            return;
    }

    public void insNotify(Connection con, loginProfile prof, String sender_usr_id, long ent_id[], long[] cc_ent_id, Timestamp sendTime, Hashtable params)
        throws SQLException, cwException, cwSysMessage {

        insNotify(con, prof, sender_usr_id, ent_id, cc_ent_id, null, sendTime, params, null);    
    }

    public void insNotify(Connection con, loginProfile prof, String sender_usr_id, long ent_id[], long[] cc_ent_id, long[] bcc_ent_id, Timestamp sendTime, Hashtable params, long[] reply_ent_id)
        throws SQLException, cwException, cwSysMessage {

            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_send_usr_id = sender_usr_id;
            dbMsg.msg_create_usr_id = prof.usr_id;
            dbMsg.msg_create_timestamp = cwSQL.getTime(con);
            dbMsg.msg_target_datetime = sendTime;
            dbMsg.msg_subject = (String)params.get("subject");
            dbMsg.msg_subject_token = (Vector)params.get("subject_token");
            dbMsg.msg_addition_note = (String)params.get("body");

            boolean wAttachment;
            if( params.containsKey("with_attachment") )
                wAttachment = (Boolean.valueOf((String) params.get("with_attachment"))).booleanValue();
            else
                wAttachment = false;

            String[] xtp_subtype = null;
            try{
                xtp_subtype = dbUtils.split((String) params.get("template_subtype"), "~");
            }catch(Exception e){
                CommonLog.error(e.getMessage(),e);
                xtp_subtype = new String[1];
                xtp_subtype[0] = "HTML";
            }

            Vector vec = new Vector();
            String url_redirect = null;
            String xtp_type = (String)params.get("template_type");
            if( xtp_type == null )
                throw new cwException("Message Template not found!");


            if( xtp_type.equalsIgnoreCase("ENROLLMENT_NEW") ) {

                // added to get the application, by Emily, 2002-10-13
                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID"};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_CONFIRMED_REMINDER") ) {

                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID"};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_REMOVED_REMINDER") ) {

                String name[] = {"ent_ids", "sender_id", "cmd", "cc_ent_ids", "id", "id_type","ent_id"};
                String type[] = {DYNAMIC, STATIC, STATIC, DYNAMIC, STATIC, STATIC, STATIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM", ((Long)params.get("ent_id")).toString()};
                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_WITHDRAWAL_REMINDER") ) {

                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID"};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_APPROVED_REMINDER") ) {

                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids", "action_taker_ent_id"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC, STATIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID", ((Long)params.get("action_taker_ent_id")).toString()};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_NEXT_APPROVERS") ) {
                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID"};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_NO_SUPERVISOR") ) {

                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID"};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_APPROVED") ) {


                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
                String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM"};
                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_WAITLISTED") ) {


                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
                String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM"};
                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_NOT_APPROVED") ) {


                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type", "app_id"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC, STATIC};
                String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM", ((Long)params.get("app_id")).toString()};
                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_CONFIRMED") ) {


                // added to get the application, by Emily, 2002-10-13
                aeItem aeItm = new aeItem();
                aeItm.itm_id = ((Long)params.get("itm_id")).longValue();
                aeItm.getItem(con);
                String time = (String)params.get("time");
                if( time != null ) {
                    long longTime = (aeItm.itm_eff_end_datetime).getTime();
                    longTime += Long.parseLong(time) * 24 * 60 * 60 * 1000;
                    dbMsg.msg_target_datetime = new Timestamp(longTime);
                }
                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID"};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_NOT_CONFIRMED") ) {


                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids", "action_taker_ent_id"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC, STATIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID", ((Long)params.get("action_taker_ent_id")).toString()};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_WITHDRAWAL_REQUEST") ) {


                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "cc_ent_ids", "app_id"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC, STATIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", "GET_CC_ENT_ID", ((Long)params.get("app_id")).toString()};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_WITHDRAWAL_APPROVED") ) {


                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type", "bcc_ent_ids"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC, DYNAMIC};
                String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM", "GET_BCC_ENT_ID"};
                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_WITHDRAWAL_NOT_APPROVED") ) {


                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type", "bcc_ent_ids"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC, DYNAMIC};
                String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM", "GET_BCC_ENT_ID"};
                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ITEM_CANCELLATION") ) {


                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
                String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM"};
                vec = notifyParams(name, type, value);

            } else if ( xtp_type.equalsIgnoreCase("ENROLLMENT_LATE_WITHDRAWAL_REQUEST") ) {
                String cmd = "link_notify_xml";
                String cmdParam = (String)params.get("msg_cmd");

                if (cmdParam != null && cmdParam.equalsIgnoreCase("notify_xml")) {
                    cmd = cmdParam;
                }

                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "cc_ent_ids", "app_id"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC, STATIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, cmd, ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", "GET_CC_ENT_ID", ((Long)params.get("app_id")).toString()};

                vec = notifyParams(name, type, value);
                
            } else if ( xtp_type.equalsIgnoreCase("USER_ACCOUNT_SUSPENSION_NOTIFICATION") ) {

                String name[] = {"ent_ids", "sender_id", "cmd", "site_id", "style", "id", "id_type"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "link_notify_xml", Long.toString(prof.root_ent_id), prof.skin_root, (String)params.get("usr_ent_id"), "USER"};

                vec = notifyParams(name, type, value);
                
            } else if ( xtp_type.equalsIgnoreCase("USER_ACCOUNT_SUSPENSION_SELF_NOTIFICATION") ) {

                String name[] = {"ent_ids", "sender_id", "cmd"};
                String type[] = {DYNAMIC, STATIC, STATIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml"};

                vec = notifyParams(name, type, value);
                
            } else if( xtp_type.equalsIgnoreCase("JI") ) {
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC};
                String name[] = {"ent_ids", "sender_id", "cmd", "intro", "env", "label_lan", "site_id", "style", "url_redirect", "itm_id", "tvw_id", "cc_to_approver_ind", "cc_to_approver_ex_id", "bcc_ent_ids"};
                String value[] = {"GET_ENT_ID", sender_usr_id, "ji_notify_xml", "", "wizb", "", "", "", "", ((Long)params.get("itm_id")).toString(),"JI_VIEW", "", "", ""};

                //String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
                //String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
                //String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM"};
                vec = notifyParams(name, type, value);
            } else if( xtp_type.equalsIgnoreCase("REMINDER") ) {
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC};
                String name[] = {"ent_ids", "sender_id", "cmd", "intro", "env", "label_lan", "site_id", "style", "url_redirect", "itm_id", "tvw_id", "cc_to_approver_ind", "cc_to_approver_ex_id", "bcc_ent_ids"};
                String value[] = {"GET_ENT_ID", sender_usr_id, "ji_notify_xml", "", "wizb", "", "", "", "", ((Long)params.get("itm_id")).toString(),"JI_VIEW", "", "", ""};
                /*
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC};
                String name[] = {"ent_ids", "sender_id", "cmd", "intro", "env", "label_lan", "site_id", "style", "url_redirect", "itm_id", "tvw_id"};
                String value[] = {"GET_ENT_ID", sender_usr_id, "ae_notify_xml", "", "wizb", "", "", "", "", ((Long)params.get("itm_id")).toString(),"JI_VIEW"};
                
                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
                String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM"};
                */
                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_REMOVED") ) {


                String name[] = {"ent_ids", "sender_id", "cmd", "cc_ent_ids", "id", "id_type","ent_id"};
                String type[] = {DYNAMIC, STATIC, STATIC, DYNAMIC, STATIC, STATIC, STATIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM", ((Long)params.get("ent_id")).toString()};
                vec = notifyParams(name, type, value);

            // the following XXX_AUTO templates are copied from corresponding XXX templates
            // (2003-07-08 kawai)
            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_WAITLISTED_AUTO") ) {


                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
                String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM"};
                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_CONFIRMED_AUTO") ) {


                // added to get the application, by Emily, 2002-10-13
                aeItem aeItm = new aeItem();
                aeItm.itm_id = ((Long)params.get("itm_id")).longValue();
                aeItm.getItem(con);
                String time = (String)params.get("time");
                if( time != null ) {
                    long longTime = (aeItm.itm_eff_end_datetime).getTime();
                    longTime += Long.parseLong(time) * 24 * 60 * 60 * 1000;
                    dbMsg.msg_target_datetime = new Timestamp(longTime);
                }
                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID"};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_NOT_CONFIRMED_AUTO") ) {


                String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type", "app_id", "cc_ent_ids"};
                String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, DYNAMIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", ((Long)params.get("itm_id")).toString(), prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, "url_redirect", "ITEM", ((Long)params.get("app_id")).toString(), "GET_CC_ENT_ID"};

                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_WITHDRAWAL_APPROVED_AUTO") ) {


                String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type", "bcc_ent_ids"};
                String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC, DYNAMIC};
                String value[] = {sender_usr_id, "notify_xml", "GET_ENT_ID", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM", "GET_BCC_ENT_ID"};
                vec = notifyParams(name, type, value);

            } else if( xtp_type.equalsIgnoreCase("ENROLLMENT_REMOVED_AUTO") ) {


                String name[] = {"ent_ids", "sender_id", "cmd", "cc_ent_ids", "id", "id_type"};
                String type[] = {DYNAMIC, STATIC, STATIC, DYNAMIC, STATIC, STATIC};
                String value[] = {"GET_ENT_ID", sender_usr_id, "notify_xml", "GET_CC_ENT_ID", ((Long)params.get("itm_id")).toString(), "ITEM"};
                vec = notifyParams(name, type, value);

            }else
                throw new cwException("Message Template not found!");


            Message msg = new Message();
            // please do not turn on this flag to bcc emails to sys
            dbMsg.msg_bcc_sys_ind = false;
            //msg.insNotify(con, ent_id, cc_ent_id, xtp_type, xtp_subtype, dbMsg, vec);
            msg.insNotify(con, ent_id, cc_ent_id, bcc_ent_id, xtp_type, xtp_subtype, dbMsg, vec, wAttachment, reply_ent_id);

            return;
        }

    public void addParam(Vector vec, String paramName, String paramType, String paramValue) {

        Vector paramsName = (Vector)vec.elementAt(0);
        Vector paramsType = (Vector)vec.elementAt(1);
        Vector paramsValue = (Vector)vec.elementAt(2);
        paramsName.addElement(paramName);
        paramsType.addElement(paramType);
        paramsValue.addElement(paramValue);
        vec.removeAllElements();
        vec.addElement(paramsName);
        vec.addElement(paramsType);
        vec.addElement(paramsValue);
        return;

    }


/*
    public Vector linkNotifyParams(loginProfile prof, String usr_id, String cmd, String id, String id_type, String url_redirect) {

        Vector params = new Vector();
        Vector paramsName = new Vector();
        Vector paramsType = new Vector();
        Vector paramsValue = new Vector();

        String name[] = {"ent_ids", "sender_id", "cmd", "id", "label_lan", "site_id", "style", "url_redirect", "id_type"};
        String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC};
        String value[] = {"GET_ENT_ID", usr_id, cmd, id, prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root, url_redirect, id_type};

        for(int i=0; i<name.length; i++) {
            paramsName.addElement(name[i]);
            paramsType.addElement(type[i]);
            paramsValue.addElement(value[i]);
        }

        params.addElement(paramsName);
        params.addElement(paramsType);
        params.addElement(paramsValue);
        return params;

    }

    public Vector notifyParams(String usr_id, String cmd, String id, String id_type) {

        Vector params = new Vector();
        Vector paramsName = new Vector();
        Vector paramsType = new Vector();
        Vector paramsValue = new Vector();

        String name[] = {"sender_id", "cmd", "ent_ids", "cc_ent_ids", "id", "id_type"};
        String type[] = {STATIC, STATIC, DYNAMIC, DYNAMIC, STATIC, STATIC};
        String value[] = {usr_id, cmd, "GET_ENT_ID", "GET_CC_ENT_ID", id, id_type};

        for(int i=0; i<name.length; i++) {
            paramsName.addElement(name[i]);
            paramsType.addElement(type[i]);
            paramsValue.addElement(value[i]);
        }

        params.addElement(paramsName);
        params.addElement(paramsType);
        params.addElement(paramsValue);

        return params;

    }
*/



    public Vector notifyParams(String[] name, String[] type, String[] value) {

        Vector params = new Vector();
        Vector paramsName = new Vector();
        Vector paramsType = new Vector();
        Vector paramsValue = new Vector();

        for(int i=0; i<name.length; i++) {
            paramsName.addElement(name[i]);
            paramsType.addElement(type[i]);
            paramsValue.addElement(value[i]);
        }

        params.addElement(paramsName);
        params.addElement(paramsType);
        params.addElement(paramsValue);

        return params;

    }



    public String getSysUserEntId(Connection con, loginProfile prof)
        throws SQLException, cwException {

            PreparedStatement stmt = con.prepareStatement(
                                     " SELECT ste_default_sys_ent_id FROM acSite WHERE ste_ent_id = ? ");

            stmt.setLong(1, prof.root_ent_id);
            ResultSet rs = stmt.executeQuery();
            long entId = 0;
            if( rs.next() )
                entId = rs.getLong("ste_default_sys_ent_id");
            stmt.close();

            dbRegUser dbusr = new dbRegUser();
            dbusr.usr_ent_id = entId;
            try{
                dbusr.get(con);
            }catch(qdbException e) {
                throw new cwException(e.getMessage());
            }
            return dbusr.usr_id;

        }

}