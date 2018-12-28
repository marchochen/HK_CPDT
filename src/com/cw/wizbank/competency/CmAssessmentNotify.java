package com.cw.wizbank.competency;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.ae.aeXMessage;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.db.*;


public class CmAssessmentNotify{
    
    private final static String ASSESSMENT_NOTIFICATION = "ASSESSMENT_NOTIFICATION";
    private final static String ASSESSMENT_COLLECTION_NOTIFICATION = "ASSESSMENT_COLLECTION_NOTIFICATION";

    private final static String DYNAMIC = "DYNAMIC";
    private final static String STATIC = "STATIC";
    
    private void insNotification(Connection con, DbMgMessage dbMsg
                                ,String msg_type, boolean bccFlag
                                ,long asm_id, String asu_type
                                ,String[] name, String[] type, String[] value)
        throws SQLException, cwException, cwSysMessage {

        String[] xtp_subtype = new String[1];
        xtp_subtype[0] = "HTML";
        Vector vec = notifyParams(name, type, value);               
    
        Message msg = new Message();
        dbMsg.msg_bcc_sys_ind = bccFlag;
        msg.insNotify(con, new long[0], new long[0], msg_type + "_" + asu_type, xtp_subtype, dbMsg, vec);
            
        DbCmAssessmentNotify dbAssNot = new DbCmAssessmentNotify();
        dbAssNot.asn_asm_id = asm_id;
        dbAssNot.asn_msg_id = msg.msg_id;
        dbAssNot.asn_asu_type = asu_type;
        if(msg_type.equals(ASSESSMENT_NOTIFICATION)) {
            dbAssNot.asn_type = DbCmAssessmentNotify.NOTIFICATION;
        } else if (msg_type.equals(ASSESSMENT_COLLECTION_NOTIFICATION)) {
            dbAssNot.asn_type = DbCmAssessmentNotify.COLLECTION;
        } 
        dbAssNot.ins(con);
        return;
    }
    
    void insNotification(Connection con, long asm_id, String asu_type, String sender_usr_id, Timestamp send_datetime, String msg_subject, boolean bccFlag, loginProfile prof)
        throws SQLException, cwException, cwSysMessage {

            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_send_usr_id = sender_usr_id;
            dbMsg.msg_create_usr_id = prof.usr_id;
            dbMsg.msg_create_timestamp = cwSQL.getTime(con);
            dbMsg.msg_target_datetime = send_datetime;
            dbMsg.msg_subject = msg_subject;
            
            String name[] = {"ent_ids", "sender_id", "cmd", "asm_id", "label_lan", "site_id", "style"};
            String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC};
            String value[] = {"GET_ENT_ID", sender_usr_id, "assessment_notify_xml", (new Long(asm_id)).toString() , prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root};
    
            insNotification(con, dbMsg, ASSESSMENT_NOTIFICATION, bccFlag, asm_id, asu_type, name, type, value);
            return;
    }
    
    void insCollectionNotification(Connection con, long asm_id, String asu_type, String sender_usr_id, Timestamp send_datetime, String msg_subject, boolean bccFlag, loginProfile prof)
        throws SQLException, cwException, cwSysMessage {

            DbMgMessage dbMsg = new DbMgMessage();
            dbMsg.msg_send_usr_id = sender_usr_id;
            dbMsg.msg_create_usr_id = prof.usr_id;
            dbMsg.msg_create_timestamp = cwSQL.getTime(con);
            dbMsg.msg_target_datetime = send_datetime;
            dbMsg.msg_subject = msg_subject;

            String name[] = {"ent_ids", "sender_id", "cmd", "asm_id", "label_lan", "site_id", "style"};
            String type[] = {DYNAMIC, STATIC, STATIC, STATIC, STATIC, STATIC, STATIC};
            String value[] = {"GET_ENT_ID", sender_usr_id, "assessment_notify_xml", (new Long(asm_id)).toString() , prof.label_lan, Long.toString(prof.root_ent_id), prof.skin_root};
            
            insNotification(con, dbMsg, ASSESSMENT_COLLECTION_NOTIFICATION, bccFlag, asm_id, asu_type, name, type, value);
            return;
    }    
    
    void insNotificationRecipient(Connection con, Hashtable msgXtpTable, long usr_ent_id)
        throws cwException, SQLException {        
        
        Message msg = new Message();
        Enumeration enumeration = msgXtpTable.keys();
        Vector vec = null;
        long[] xtp_id_list = null;
        while(enumeration.hasMoreElements()){
            msg.msg_id = ((Long)enumeration.nextElement()).longValue();
            vec = (Vector)msgXtpTable.get(new Long(msg.msg_id));
            xtp_id_list = new long[vec.size()];
            for(int i=0; i<vec.size(); i++)
                xtp_id_list[i] = ((Long)vec.elementAt(i)).longValue();
            msg.insertRecipient(con, usr_ent_id, xtp_id_list, msg.RECIPIENT);
        }
        return;
    }
    
    /*
    void insNotificationRecipientNotInList(Connection con, long asm_id, String asu_type, long[] ids) 
        throws SQLException {
        //get the msg_id of the input assessment and assessment unit type
        long[] msg_id_list = DbCmAssessmentNotify.getMessageIdList(con, asm_id, asu_type);
        if( msg_id_list != null && msg_id_list.length > 0 ) {
            Hashtable hvMsgRecEntId = new Hashtable();
            int index = msg_id_list.length;
            for(int i=0; i<index; i++) {
                //get message recipient list
                Vector vMsgRecEntId = new Vector();
                Long msgId = new Long(msg_id_list[i]);
                hvMsgRecEntId.put(msgId, vMsgRecEntId);
            }
            Enumeration eMsgId = hvMsgRecEntId.keys();
            while(eMsgId.hasMoreElements()){
                Long msgId = (Long)eMsgId.nextElement();
                Vector vMsgRecEntId = (Vector)hvMsgRecEntId.get(msgId);
                for(int i=0; i<ids.length; i++) {
                    if(!vMsgRecEntId.contains(new Long(ids[i]))) {
                        //insert mgRecHistory and mgRecipient
                    }
                }
            }
        }
        return;
    }
    */
    
    void delNotificationRecipientNotInListByAsuType(Connection con, long asm_id, String asu_type, long[] ids )
        throws cwException, SQLException {
        
            long[] msg_id_list = DbCmAssessmentNotify.getMessageIdListByAsuType(con, asm_id, asu_type);
            if( msg_id_list == null || msg_id_list.length == 0 )
                return;
            DbMgRecHistory.delNotInList(con, msg_id_list, ids);
            DbMgRecipient.delNotInList(con, msg_id_list, ids);            
            return;
    }

    void delNotificationRecipientNotInList(Connection con, long asm_id, long[] ids )
        throws cwException, SQLException {
        
            long[] msg_id_list = DbCmAssessmentNotify.getAssessorMessageIdList(con, asm_id);
            if( msg_id_list == null || msg_id_list.length == 0 )
                return;
            DbMgRecHistory.delNotInList(con, msg_id_list, ids);
            DbMgRecipient.delNotInList(con, msg_id_list, ids);            
            return;
    }
    
    void delNotification(Connection con, long asm_id)
        throws cwException, SQLException {
            long[] msg_id_list = DbCmAssessmentNotify.getMessageIdList(con, asm_id);
            if( msg_id_list == null || msg_id_list.length == 0 )
                return;
            DbCmAssessmentNotify.delRecords(con, msg_id_list);
            DbMgRecHistory.delRecords(con, msg_id_list);
            DbMgRecipient.delRecords(con, msg_id_list);
            DbMgXslParamValue.delRecords(con, msg_id_list);
            DbXslMgSelectedTemplate.delRecords(con, msg_id_list);
            DbMgMessage.delRecords(con, msg_id_list);
            return;
    }
    
    void updNotification(Connection con, long asm_id, String asu_type, Timestamp notification_timestamp, Timestamp collection_timestamp, loginProfile prof)
        throws SQLException, cwException {
            DbMgMessage dbMsg = new DbMgMessage();
            
            dbMsg.msg_id = DbCmAssessmentNotify.getMessageId(con, asm_id, DbCmAssessmentNotify.NOTIFICATION, asu_type);
            dbMsg.msg_target_datetime = notification_timestamp;
            dbMsg.updTimestamp(con, prof.usr_id);
            
            dbMsg.msg_id = DbCmAssessmentNotify.getMessageId(con, asm_id, DbCmAssessmentNotify.COLLECTION, asu_type);
            dbMsg.msg_target_datetime = collection_timestamp;
            dbMsg.updTimestamp(con, prof.usr_id);
            return;
    }
    
    
    private Vector notifyParams(String[] name, String[] type, String[] value) {
                
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
 
	public static String getAssessmentNotifyXML(Connection con, String sender_usr_id, long usr_ent_id, long asm_id, String mail_account, long DES_KEY)
		throws SQLException, cwException, cwSysMessage {
			
			StringBuffer xml = new StringBuffer();
			aeXMessage xmsg = new aeXMessage();
			xml.append(xmsg.getRecipientXml(con, usr_ent_id, mail_account, DES_KEY));			
			xml.append(xmsg.getSenderXml(con, sender_usr_id, mail_account));
			CmAssessmentManager assMgr = new CmAssessmentManager();
			assMgr.asm_id = asm_id;
			xml.append(assMgr.getAssessmentXML(con));
			return xml.toString();
	}    
}