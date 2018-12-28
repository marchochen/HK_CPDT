package com.cw.wizbank.ae;

import java.sql.*;
import java.util.Vector;

import com.cw.wizbank.util.*;
import com.cw.wizbank.ae.db.DbItemMessage;
import com.cw.wizbank.newmessage.MessageDao;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.*;
import com.cwn.wizbank.utils.CommonLog;

public class aeItemMessage {
    
    private static final String TYPE_JI = "JI";
    private static final String TYPE_REMINDER = "REMINDER";
      
    public aeItemMessage() {
    }
    
    public static void insNotifyForJI(Connection con, loginProfile prof, long usrEntId, long itm_id, long app_id) throws SQLException, cwException, cwSysMessage, qdbException {
    	Timestamp[] ji_reminder_send_datetime = aeItem.getItmJiReminderSendTimestamp(con, itm_id);
		if(MessageTemplate.isActive(con, prof.my_top_tc_id, MessageTemplate.TYPE_JI) && ji_reminder_send_datetime[0] != null){
			MessageService msgService = new MessageService();
		    msgService.insItemJINotify(con, prof, itm_id, usrEntId, app_id, MessageTemplate.TYPE_JI, ji_reminder_send_datetime[0]);
			
		    //插入课程开课通知邮件信息
			aeItemMessage itmMsg = new aeItemMessage();
			itmMsg.ins(con, itm_id, DbItemMessage.TYPE_JI, prof.usr_id, app_id, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL);
			if(MessageTemplate.isMtpWebMessage(con, prof.my_top_tc_id, MessageTemplate.TYPE_JI)){
				itmMsg.ins(con, itm_id, DbItemMessage.TYPE_JI, prof.usr_id, app_id, MessageDao.MESSAGE_CONTENT_TYPE_PC);
			}
		}
		if(MessageTemplate.isActive(con, prof.my_top_tc_id, MessageTemplate.TYPE_REMINDER) && ji_reminder_send_datetime[1] != null){
			MessageService msgService = new MessageService();
			msgService.insItemJINotify(con, prof, itm_id, usrEntId, app_id, MessageTemplate.TYPE_REMINDER, ji_reminder_send_datetime[1]);
			
			//插入课程开课通知邮件信息
			aeItemMessage itmMsg = new aeItemMessage();
			itmMsg.ins(con, itm_id, DbItemMessage.TYPE_REMINDER, prof.usr_id, app_id, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL);
			if(MessageTemplate.isMtpWebMessage(con, prof.my_top_tc_id, MessageTemplate.TYPE_REMINDER)){
				itmMsg.ins(con, itm_id, DbItemMessage.TYPE_REMINDER, prof.usr_id, app_id, MessageDao.MESSAGE_CONTENT_TYPE_PC);
			}
		}
               
        return;
    }
        
    public static void insNotifyForOnlyJI(Connection con, loginProfile prof, long usrEntId, long itm_id, long app_id) throws SQLException, cwException, cwSysMessage, qdbException {
    	Timestamp[] ji_reminder_send_datetime = aeItem.getItmJiReminderSendTimestamp(con, itm_id);
		if(MessageTemplate.isActive(con, prof.my_top_tc_id, MessageTemplate.TYPE_JI)){
			MessageService msgService = new MessageService();
		    msgService.insItemJINotify(con, prof, itm_id, usrEntId, app_id, MessageTemplate.TYPE_JI, ji_reminder_send_datetime[0]);
			
		    //插入课程开课通知邮件信息
			aeItemMessage itmMsg = new aeItemMessage();
			itmMsg.ins(con, itm_id, DbItemMessage.TYPE_JI, prof.usr_id, app_id, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL);
			if(MessageTemplate.isMtpWebMessage(con, prof.my_top_tc_id, MessageTemplate.TYPE_JI)){
				itmMsg.ins(con, itm_id, DbItemMessage.TYPE_JI, prof.usr_id, app_id, MessageDao.MESSAGE_CONTENT_TYPE_PC);
			}
		}
		return;
    }
    
    public static void insNotifyForOnlyReminder(Connection con, loginProfile prof, long usrEntId, long itm_id, long app_id) throws SQLException, cwException, cwSysMessage, qdbException {
    	Timestamp[] ji_reminder_send_datetime = aeItem.getItmJiReminderSendTimestamp(con, itm_id);
    	if(MessageTemplate.isActive(con, prof.my_top_tc_id, MessageTemplate.TYPE_REMINDER)){
			MessageService msgService = new MessageService();
			msgService.insItemJINotify(con, prof, itm_id, usrEntId, app_id, MessageTemplate.TYPE_REMINDER, ji_reminder_send_datetime[1]);
			
			//插入课程开课通知邮件信息
			aeItemMessage itmMsg = new aeItemMessage();
			itmMsg.ins(con, itm_id, DbItemMessage.TYPE_REMINDER, prof.usr_id, app_id, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL);
			if(MessageTemplate.isMtpWebMessage(con, prof.my_top_tc_id, MessageTemplate.TYPE_REMINDER)){
				itmMsg.ins(con, itm_id, DbItemMessage.TYPE_REMINDER, prof.usr_id, app_id, MessageDao.MESSAGE_CONTENT_TYPE_PC);
			}
		}
    	return;
    }
    
    public static void removeNotifyForJI(Connection con, long itmId, long usrEntId, long appId,String mtp_type)
        throws SQLException, cwException{
           
    		CommonLog.debug("Remove JI Message.");
    		Vector<Long> emsg_id_vec = DbItemMessage.getMsgByAppId(con, appId, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL,mtp_type);
    		Vector<Long> wmsg_id_vec = DbItemMessage.getMsgByAppId(con, appId, MessageDao.MESSAGE_CONTENT_TYPE_PC,mtp_type);
    		MessageDao msgDao = new MessageDao();
        	
    		if(emsg_id_vec.size()>0){
    			msgDao.delMessage(con, emsg_id_vec, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL);
    			DbItemMessage.delByAppAndMtpType(con, emsg_id_vec,mtp_type);
    		}
    		if(wmsg_id_vec.size()>0){
    			msgDao.delMessage(con, wmsg_id_vec, MessageDao.MESSAGE_CONTENT_TYPE_PC);
    			DbItemMessage.delByAppAndMtpType(con, wmsg_id_vec,mtp_type);
    		}
    		
            return;
        }
    
    public static StringBuffer getAeItemMessage(Connection con, long img_itm_id) throws SQLException, cwException {
        
        StringBuffer xmlBuf = new StringBuffer();
        Timestamp[] ji_reminder_send_datetime = aeItem.getItmJiReminderSendTimestamp(con, img_itm_id);
        xmlBuf.append("<ji_message>");
        xmlBuf.append("<message")
        .append(" id=\"").append("0").append("\"")
        .append(" type=\"").append(TYPE_JI).append("\"")
        .append(" target_date=\"").append(cwUtils.escNull(ji_reminder_send_datetime[0])).append("\"")
        .append("/>");       
        xmlBuf.append("<message")
        .append(" id=\"").append("0").append("\"")
        .append(" type=\"").append(TYPE_REMINDER).append("\"")
        .append(" target_date=\"").append(cwUtils.escNull(ji_reminder_send_datetime[1])).append("\"")
        .append("/>");
               
        xmlBuf.append("</ji_message>");
        return xmlBuf;
        
    }
    
    public void ins(Connection con, long itm_id, String type, String usrId, long app_id, String msg_type) throws SQLException, cwException{
   
        DbItemMessage dbItmMsg = new DbItemMessage();
        dbItmMsg.img_itm_id = itm_id;
        dbItmMsg.img_mtp_type = type;
        dbItmMsg.img_create_usr_id = usrId;
        dbItmMsg.img_update_usr_id = usrId;
        dbItmMsg.img_app_id = app_id;
		if(msg_type.equalsIgnoreCase(MessageDao.MESSAGE_CONTENT_TYPE_EMAIL)){
		    dbItmMsg.img_msg_id = MessageDao.getEmailMessagMaxId(con);
		    dbItmMsg.img_msg_type = MessageDao.MESSAGE_CONTENT_TYPE_EMAIL;
		}else{
			dbItmMsg.img_msg_id = MessageDao.getWebMessagMaxId(con);
		    dbItmMsg.img_msg_type = MessageDao.MESSAGE_CONTENT_TYPE_PC;
		}
        dbItmMsg.ins(con);
    }
    
    public static void delByItem(Connection con, long itm_id) throws SQLException, cwException{
        
    	Vector<Long> emsg_id_vec = DbItemMessage.getMsgByItem(con, itm_id, DbItemMessage.TYPE_JI, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL);
		Vector<Long> wmsg_id_vec = DbItemMessage.getMsgByItem(con, itm_id, DbItemMessage.TYPE_REMINDER, MessageDao.MESSAGE_CONTENT_TYPE_PC);
		 
		MessageDao msgDao = new MessageDao();
    	
		if(emsg_id_vec.size()>0){
			msgDao.delMessage(con, emsg_id_vec, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL);
		}
		if(wmsg_id_vec.size()>0){
			msgDao.delMessage(con, wmsg_id_vec, MessageDao.MESSAGE_CONTENT_TYPE_PC);
		}

		DbItemMessage.delByApp(con, itm_id);
        return;
    }
    
	/**更改JI, Reminder发送时间
	 * @param con
	 * @param itm_id
	 * @param mtp_type
	 * @param send_target_datetime
	 * @throws SQLException
	 * @throws cwException
	 */
	public void updJiReminderSendTimestamp(Connection con, long itm_id, String mtp_type, Timestamp send_target_datetime) throws SQLException, cwException {
		Vector<Long> emsg_id_vec = DbItemMessage.getMsgByItem(con, itm_id, mtp_type, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL);
		Vector<Long> wmsg_id_vec = DbItemMessage.getMsgByItem(con, itm_id, mtp_type, MessageDao.MESSAGE_CONTENT_TYPE_PC);
		
		MessageDao mdao = new MessageDao();
		if(emsg_id_vec.size()>0){
			mdao.updMessageTargetTimestamp(con, send_target_datetime, emsg_id_vec, MessageDao.MESSAGE_CONTENT_TYPE_EMAIL, true);
		}
		if(wmsg_id_vec.size()>0){
			mdao.updMessageTargetTimestamp(con, send_target_datetime, wmsg_id_vec, MessageDao.MESSAGE_CONTENT_TYPE_PC, false);	
		}
	}
}