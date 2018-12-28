package com.cw.wizbank.km;

import java.sql.*;
import java.util.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.aeXMessage;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.db.DbMgMessage;
import com.cw.wizbank.db.DbKmNodeSubscription;
import com.cw.wizbank.db.view.ViewKmNodeManager;
import com.cw.wizbank.qdb.dbRegUser;

public class KMNotify {
    
    private static final String NOTIFICATION_SUBJECT        = "Knowlege Portal Subscription Notification";
    private static final String NOTIFICATION_TYPE           = "KM_NODE_ACTION_NOTIFICATION";
    private static final String NOTIFICATION_SUBTYPE        = "HTML";

        
    /**
    Insert subscription notification record to the mgMessage table 
    */
    public static void insNotify(Connection con, Vector entIdVec, String email_send_type, Timestamp curTime, long action_id)
        throws SQLException, cwSysMessage, cwException {
            
        for (int i=0;i<entIdVec.size();i++) {
            long usr_ent_id = ((Long) entIdVec.elementAt(i)).longValue();
            String sender_usr_id = ViewKmNodeManager.getSysUsrId(con, usr_ent_id);
            
            DbMgMessage dbMgMsg = new DbMgMessage();
            dbMgMsg.msg_send_usr_id = sender_usr_id;
            dbMgMsg.msg_create_usr_id = sender_usr_id;
            dbMgMsg.msg_create_timestamp = curTime;
            dbMgMsg.msg_target_datetime = curTime;
            dbMgMsg.msg_subject = NOTIFICATION_SUBJECT;

            Vector params = new Vector();
            Vector paramsName = new Vector();
            Vector paramsType = new Vector();
            Vector paramsValue = new Vector();

            paramsName.addElement("cmd");
            paramsName.addElement("usr_ent_id");
            paramsName.addElement("email_send_type");
            paramsName.addElement("email_send_timestamp");
            paramsName.addElement("action_id");
            paramsName.addElement("sender_usr_id");

            paramsType.addElement("STATIC");
            paramsType.addElement("STATIC");
            paramsType.addElement("STATIC");
            paramsType.addElement("STATIC");
            paramsType.addElement("STATIC");
            paramsType.addElement("STATIC");
            
            paramsValue.addElement(KMModule.NOTIFICATION_CMD);
            paramsValue.addElement(Long.toString(usr_ent_id));
            paramsValue.addElement(email_send_type);
            paramsValue.addElement(curTime.toString());
            paramsValue.addElement(Long.toString(action_id));
            paramsValue.addElement(sender_usr_id);
            
            params.addElement(paramsName);
            params.addElement(paramsType);
            params.addElement(paramsValue);

            long[] ent_ids = new long[1];
            ent_ids[0] = usr_ent_id;

            String[] subtype = new String[1];
            subtype[0] = NOTIFICATION_SUBTYPE;
            Message msg = new Message();
            msg.insNotify(con, ent_ids, null, NOTIFICATION_TYPE, subtype, dbMgMsg, params);
        }

    }

    /**
    Get the xml for sending subscription email
    */
    public static String getNotifyXML(Connection con, long usr_ent_id, String email_send_type, Timestamp email_send_timestamp, long action_id, String sender_usr_id, String mail_account, long DES_KEY) 
        throws cwException, SQLException
    {
        StringBuffer xml = new StringBuffer(1024);
        aeXMessage xmsg = new aeXMessage();
		xml.append(xmsg.getSenderXml(con, sender_usr_id, mail_account));
		xml.append(getRecipientXml(con, usr_ent_id, mail_account, DES_KEY));
		Hashtable subscriptionHash = new Hashtable();
        Hashtable actionHash = new Hashtable();
		
        if (email_send_type.equals(DbKmNodeSubscription.EMAIL_SEND_TYPE_IMMD)) {
            ViewKmNodeManager.getSubscriptionByActionID(con, usr_ent_id, action_id, actionHash, subscriptionHash);
            
        }else {
            ViewKmNodeManager.getSubscriptionByEmailSendType(con, usr_ent_id, email_send_type, email_send_timestamp, actionHash, subscriptionHash);
            DbKmNodeSubscription.updEmailFromTime(con, usr_ent_id, email_send_type, email_send_timestamp);
        }
        xml.append(KMSubscriptionManager.formatSubscriptionXML(con, actionHash, subscriptionHash));
        return xml.toString();
    }
    
    public static String getRecipientXml(Connection con, long usr_ent_id, String mail_account, long DES_KEY) 
        throws cwException {

        if(usr_ent_id == 0)
            return new String();

        StringBuffer xml = new StringBuffer();
        dbRegUser dbRecip = new dbRegUser();
        dbRecip.usr_ent_id = usr_ent_id;

        try{
            dbRecip.get(con);
        }catch( Exception e ) {
            throw new cwException("Failed to get Recipient detail, id=" + usr_ent_id + " : " + e);
        }

        String userMail = dbRecip.usr_email;
        if(mail_account.equalsIgnoreCase("NOTES"))
            userMail = dbRecip.usr_email_2;
    
        xml.append("<recipient>").append(cwUtils.NEWL)
            .append("<entity ent_id=\"").append(dbRecip.usr_ent_id).append("\" ")
            .append(" usr_id=\"").append(dbRecip.usr_id).append("\" ")
            .append(" display_name=\"").append(cwUtils.esc4XML(dbRecip.usr_display_bil)).append("\" ")
            .append(" email=\"").append(userMail).append("\" ")
            .append(" ste_usr_id=\"").append(dbRecip.usr_ste_usr_id).append("\" ");
            
        //if( dbRecip.usr_pwd != null && dbRecip.usr_pwd.length() > 0 ) {
            //RSA_instance r = new RSA_instance();
            //RSA encoder = new RSA(dbRecip.usr_pwd, r, true);
            //xml.append(" usr_pwd=\"").append((encoder.encodeString()).trim()).append("\" ");                
            //MessageCryptography msgCrypto = new MessageCryptography(DES_KEY);
            //xml.append(" usr_pwd=\"").append(URLEncoder.encode(msgCrypto.encrypt(dbRecip.usr_pwd))).append("\" ");
        //} else {
            xml.append(" usr_pwd=\"\" ");
        //}
        xml.append("/>");
        xml.append("</recipient>").append(cwUtils.NEWL);            
            
        return xml.toString();
            
    }


}