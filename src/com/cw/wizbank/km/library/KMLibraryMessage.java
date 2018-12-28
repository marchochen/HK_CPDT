package com.cw.wizbank.km.library;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
//import com.cw.wizbank.utils.rsa.*;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.message.*;
import com.cw.wizbank.db.DbMgMessage;
import com.cwn.wizbank.utils.CommonLog;

import java.net.URLEncoder;



public class KMLibraryMessage {

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

    public long app_id;

    public void insNotify(Connection con, loginProfile prof, String senderUsrId, long[] entIds, long[] ccEntIds, long[] bccEntIds, Timestamp sendTime, Hashtable params)
        throws SQLException, cwException, cwSysMessage {

            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_send_usr_id = senderUsrId;
            dbMsg.msg_create_usr_id = prof.usr_id;
            dbMsg.msg_create_timestamp = cwSQL.getTime(con);
            dbMsg.msg_target_datetime = sendTime;
            if( params.containsKey("subject") )
                dbMsg.msg_subject = (String)params.get("subject");
            if( params.containsKey("subject_token") )
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


            if( xtp_type.equalsIgnoreCase("RESERVE_NOTIFICATION") ) {

                String name[] = {"cmd", "usr_ent_id", "sender_id", "lob_id", "site_id"};
                String type[] = {STATIC, STATIC, STATIC, STATIC, STATIC};
                String value[] = {"reserve_notify_xml", (new Long(entIds[0])).toString(), senderUsrId, ((Long)params.get("lobId")).toString(), Long.toString(prof.root_ent_id)};

                vec = notifyParams(name, type, value);
            } else if( xtp_type.equalsIgnoreCase("BORROW_NOTIFICATION") ) {

                String name[] = {"cmd", "usr_ent_id", "sender_id", "lob_id", "site_id"};
                String type[] = {STATIC, STATIC, STATIC, STATIC, STATIC};
                String value[] = {"borrow_notify_xml", (new Long(entIds[0])).toString(), senderUsrId, ((Long)params.get("lobId")).toString(), Long.toString(prof.root_ent_id)};

                vec = notifyParams(name, type, value);
            } else if( xtp_type.equalsIgnoreCase("CHECKOUT_NOTIFICATION") ) {

                String name[] = {"cmd", "usr_ent_id", "sender_id", "lob_id", "site_id"};
                String type[] = {STATIC, STATIC, STATIC, STATIC, STATIC};
                String value[] = {"checkout_notify_xml", (new Long(entIds[0])).toString(), senderUsrId, ((Long)params.get("lobId")).toString(), Long.toString(prof.root_ent_id)};

                vec = notifyParams(name, type, value);
            } else
                throw new cwException("Message Template not found!");


            Message msg = new Message();
            dbMsg.msg_bcc_sys_ind = true;
            msg.insNotify(con, entIds, ccEntIds, bccEntIds, xtp_type, xtp_subtype, dbMsg, vec, wAttachment, null);

            return;
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
            	CommonLog.debug("DES_KEY = " + DES_KEY);
                MessageCryptography msgCrypto = new MessageCryptography(DES_KEY);
                xml.append(" usr_pwd=\"").append(URLEncoder.encode(msgCrypto.encrypt(dbRecip.usr_pwd))).append("\" ");
            } else {
                xml.append(" usr_pwd=\"\" ");
            }
            xml.append("/>");
            xml.append("</recipient>").append(cwUtils.NEWL);            
            
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