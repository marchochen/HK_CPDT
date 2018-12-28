package com.cw.wizbank.qdb;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.config.system.scheduledtask.impl.ParamTypeImpl;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ScheduledTask;
import com.cwn.wizbank.utils.CommonLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CheckActiveUserScheduler extends ScheduledTask implements Job{

	private Connection con = null;
	
	private static long send_mail_interval = 0;
	
	public static Timestamp last_warning_time;
	
	public static Timestamp last_blocking_time;

	public CheckActiveUserScheduler(){

		logger = Logger.getLogger(MessageScheduler.class);
		
	}

	public void init() {
		if (this.param != null) {
			for (int i = 0; i < this.param.size(); i++) {
				ParamType paramType = (ParamType) this.param.get(i);
				if (paramType.getName().equals("send_mail_interval")) {					
					try {
						send_mail_interval = Long.valueOf(paramType.getValue()).longValue();	
					} 
					catch (Exception e) {	
						send_mail_interval = 0;
					}
				}
			}
		}
		send_mail_interval *=  60 * 60 * 1000;
	}

	protected void process() {
		this.setPriority(Thread.MIN_PRIORITY);
		try {
			wizbini = WizbiniLoader.getInstance();
			dbSource = new cwSQL();
			dbSource.setParam(wizbini);
			con = dbSource.openDB(false);
			checkActiveUser();
			con.commit();
		} catch (Exception e) {
			logger.debug("CheckActiveUserScheduler.process() error", e);
			CommonLog.error(e.getMessage(),e);
            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
		} finally {
			if (this.con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.debug("CheckActiveUserScheduler.process() error", e);
                    logger.debug("奥运圣火今天（2008年5月7日）下午五点左右从我们办公室楼下经过！北京奥运，中国加油！");
                    CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}

	public void checkActiveUser() throws SQLException, qdbException, IOException, cwSysMessage, cwException {
		Timestamp cur_time = cwSQL.getTime(con);
		long active_user = CurrentActiveUser.getcurActiveUserCount(con);
		Hashtable curSysSet = SystemSetting.getCurSystemSetting(con);
		long warning_user = 0;
		long blocking_user = 0;
		if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString().length() > 0){
			blocking_user = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString());
		}
		if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString().length() > 0){
			warning_user = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString());
		}
		String email = (String) curSysSet.get(SystemSetting.SYS_CFG_TYPE_EMAIL);
		if (email != null && email.length() > 0) {
			if (blocking_user > 0 && active_user >= blocking_user) {
				if (last_blocking_time == null || (cur_time.getTime() - last_blocking_time.getTime()) > send_mail_interval ) {
					String subject = "Alert: LMS maximum active user has been reached";
					sendSystemPerformanceNotify(subject, active_user, warning_user, blocking_user, cur_time);
					last_blocking_time = cur_time;
				}
			} else if (warning_user > 0 && active_user >= warning_user) {
				if (last_warning_time == null || (cur_time.getTime() - last_warning_time.getTime()) > send_mail_interval ) {
					String subject = "Alert: LMS performance warning level has been reached";
					sendSystemPerformanceNotify(subject, active_user, warning_user, blocking_user, cur_time);
					last_warning_time = cur_time;
				}
			}
		}
		return;
	}
	
	private void sendSystemPerformanceNotify(String subject, long active_user, long warning_user, long blocking_user, Timestamp curTime) throws qdbException, SQLException, cwException {
		String admin_usr_id = acSite.getSysUsrId(con, 1);
		long admin_usr_ent_id = dbRegUser.getEntId(con, admin_usr_id);

		long top_tc_id = ViewTrainingCenter.getTopTc(con, admin_usr_ent_id, false);
		if(MessageTemplate.isActive(con, top_tc_id, "SYS_PERFORMANCE_NOTIFY")){
          //插入邮件及邮件内容
            MessageService msgService = new MessageService();
            Timestamp sendTime = cwSQL.getTime(con);
            
    		MessageTemplate mtp = new MessageTemplate();
    		mtp.setMtp_tcr_id(top_tc_id);
    		mtp.setMtp_type("SYS_PERFORMANCE_NOTIFY");
    		mtp.getByTcr(con);
    		mtp.setMtp_subject(subject);
            String[] contents = msgService.getSysPerfMsgContent(con, mtp, active_user, warning_user, blocking_user, sendTime);
            msgService.insMessage(con, mtp, admin_usr_id,  new long[] {admin_usr_ent_id}, new long[0], sendTime, contents,0);
            
            con.commit();
            CommonLog.info("# Notificaion email created. ");
        }
	}
	
	public static String getQuotaExceedNotifyXML(Connection con, String sender_id, long active_user, long warning_user, long blocking_user, Timestamp cur_time) throws SQLException, cwException, qdbException, cwSysMessage {
		StringBuffer xml = new StringBuffer();
		Hashtable curSysSet = SystemSetting.getCurSystemSetting(con);
		String rec_email = (String) curSysSet.get(SystemSetting.SYS_CFG_TYPE_EMAIL);
        if(sender_id == null || sender_id.length() == 0)
            return "";
        dbRegUser sender = new dbRegUser();
        sender.usr_id = sender_id;
        sender.usr_ent_id = sender.getEntId(con);
        sender.get(con);
        xml.append("<sender display_name=\"").append(dbUtils.esc4XML(sender.usr_display_bil)).append("\" ")
           .append(" usr_id=\"").append(dbUtils.esc4XML(sender.usr_id)).append("\" ")
           .append(" ste_usr_id=\"").append(dbUtils.esc4XML(sender.usr_ste_usr_id)).append("\" ")
           .append(" email=\"").append(dbUtils.esc4XML(sender.usr_email)).append("\" />").append(cwUtils.NEWL);
        
        String admin_usr_id = acSite.getSysUsrId(con, 1);
        long admin_usr_ent_id = dbRegUser.getEntId(con, admin_usr_id);
        if (rec_email != null && rec_email.length() > 0) {
        	xml.append("<recipient>").append(cwUtils.NEWL);
                xml.append("<entity ent_id=\"").append(admin_usr_ent_id).append("\" ")
                   .append(" usr_id=\"").append("").append("\" ")
                   .append(" display_name=\"").append("").append("\" ")
                   .append(" email=\"").append(dbUtils.esc4XML(rec_email)).append("\" ")
                   .append(" ste_usr_id=\"").append("").append("\"/>");
        	xml.append("</recipient>").append(cwUtils.NEWL);
        }

        xml.append("<sys_setting active_user=\"").append(active_user)
	    	.append("\" warning_user=\"").append(warning_user)
	    	.append("\" blocking_user=\"").append(blocking_user)
	    	.append("\" gen_time=\"").append(cur_time)
	    	.append("\"/>");
		return xml.toString();
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Map<String, Object> params = jobExecutionContext.getMergedJobDataMap();
		if(param == null){
			param = new ArrayList();
		}
		for(String key : params.keySet()){
			ParamType paramType = new ParamTypeImpl();
			paramType.setName(key);
			paramType.setValue(params.get(key).toString());
			param.add(paramType);
		}
		init();
		process();
	}
}
