package com.cw.wizbank.course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.config.system.scheduledtask.impl.ParamTypeImpl;
import com.cw.wizbank.db.DbRegUser;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CourseNotifyScheduler extends ScheduledTask implements Job{

	protected long send_mail_interval;

	public CourseNotifyScheduler(){
		logger = Logger.getLogger(MessageScheduler.class);
	}

	protected void init() {
		if (this.param != null) {
			for (int i = 0; i < this.param.size(); i++) {
				ParamType paramType = (ParamType) this.param.get(i);
				if (paramType.getName().equals("send_mail_interval")) {
					this.send_mail_interval = Long.valueOf(paramType.getValue()).longValue();
				}
			}
		}
	}

	protected void process() {
		try {
			wizbini = WizbiniLoader.getInstance();
			dbSource = new cwSQL();
			dbSource.setParam(wizbini);
			con = dbSource.openDB(false);
			callSendNotification();
			con.commit();
		}
		catch (Exception e) {
			logger.debug("error in CourseNotifyScheduler.process()");
			logger.debug(e);
            CommonLog.error(e.getMessage(),e);
            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
		}
		finally {
			try {
				if (con != null) {
					con.close();
				}
			}
			catch (Exception e) {
				logger.debug("error in CourseNotifyScheduler.process()");
                logger.debug("奥运圣火今天（2008年5月7日）下午五点左右从我们办公室楼下经过！北京奥运，中国加油！");
				logger.debug(e);
			}
		}
	}

	public void callSendNotification() throws SQLException, cwException, qdbException, cwSysMessage {
		List ste_lst = new ArrayList();
		String sql_get_all_site = "select ste_ent_id from acSite where ste_status = ? ";
		PreparedStatement stmt = con.prepareStatement(sql_get_all_site);
		stmt.setString(1, acSite.STATUS_OK);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			ste_lst.add(new Long(rs.getLong("ste_ent_id")));
		}
		rs.close();
		stmt.close();

		String COURS_ALERT_SQL = " Select app_id, app_itm_id, app_ent_id, itm_notify_days,itm_content_eff_duration,itm_content_eff_end_datetime,att_create_timestamp" 
			    + " From aeApplication "
				+ " inner join aeAttendance on (att_app_id = app_id) inner join aeItem on (app_itm_id = itm_id) Where att_ats_id = 2 "
				+ " and itm_owner_ent_id = ? "
				+ " and itm_notify_days > 0 and att_timestamp is null ";
		for (int i = 0; i < ste_lst.size(); i++) {
			long ste_id = ((Long) ste_lst.get(i)).longValue();
			dbRegUser admin = getSiteDefauleAdmin(con, ste_id);
			String send_usr_id = admin.usr_id;

			long top_tcr_id = ViewTrainingCenter.getTopTc(con, admin.usr_ent_id, false);
			if(MessageTemplate.isActive(con, top_tcr_id, "COURSE_NOTIFY")){
				PreparedStatement pstmt = con.prepareStatement(COURS_ALERT_SQL);
				pstmt.setLong(1, ste_id);
				ResultSet rs2 = pstmt.executeQuery();
				while (rs2.next()) {
					Timestamp cur_time = cwSQL.getTime(con);
					long app_id = rs2.getLong("app_id");
					long app_ent_id = rs2.getLong("app_ent_id");
					long app_itm_id = rs2.getLong("app_itm_id");
					Timestamp itm_content_eff_end_datetime = rs2.getTimestamp("itm_content_eff_end_datetime");
					int itm_notify_days = rs2.getInt("itm_notify_days");
					int itm_content_eff_duration = rs2.getInt("itm_content_eff_duration");
					Timestamp att_create_timestamp = rs2.getTimestamp("att_create_timestamp");
					int diff = 0;
					if(itm_content_eff_end_datetime != null) {
						diff = cwUtils.dateDiff(cur_time,itm_content_eff_end_datetime);
					}else if(itm_content_eff_duration > 0){
						Timestamp endTime = cwUtils.dateAdd(att_create_timestamp, itm_content_eff_duration);
						diff = cwUtils.dateDiff(cur_time, endTime);
					}
					if(diff == itm_notify_days) {
				        //插入邮件及邮件内容
						Timestamp send_time = cwSQL.getTime(con);
				        MessageService msgService = new MessageService();
				        String mtp_type = "COURSE_NOTIFY";
				        
						MessageTemplate mtp = new MessageTemplate();
						mtp.setMtp_tcr_id(top_tcr_id);
						mtp.setMtp_type(mtp_type);
						mtp.getByTcr(con);
	
						long[] ent_ids = new long[] {app_ent_id};
						String[] contents = msgService.getMsgContent(con, mtp, app_ent_id, admin.usr_ent_id, app_itm_id, app_id,ent_ids);
				        msgService.insMessage(con, mtp, send_usr_id, ent_ids, new long[0], send_time, contents,app_itm_id);
					}
				}
				pstmt.close();
			}
		}
		return;
	}

	private static DbRegUser getSiteDefauleAdmin(Connection con, long ste_id) throws SQLException, qdbException {
		DbRegUser sysadmin = new DbRegUser();
		acSite site = new acSite();
		site.ste_ent_id = ste_id;
		sysadmin.usr_ent_id = site.getSiteSysEntId(con);
		sysadmin.get(con);
		return sysadmin;
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
