package com.cw.wizbank.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.util.cwSQL;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SynlrnActivityReportScheduler extends ScheduledTask implements Job{

	public SynlrnActivityReportScheduler(){
		logger = Logger.getLogger(MessageScheduler.class);
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void process() {
		try {
			wizbini = WizbiniLoader.getInstance();
			dbSource = new cwSQL();
			dbSource.setParam(wizbini);
			con = dbSource.openDB(false);
			synLrarnRecord(con);
			con.commit();
		}
		catch (Exception e) {
			logger.debug("error in SynlrnActivityReportScheduler process()");
			logger.debug(e);
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
				logger.debug("error in SynlrnActivityReportScheduler()");
				logger.debug(e);
			}
		}
	}
	public void synLrarnRecord(Connection con) throws SQLException{
		//删除表数据
		delLrnActivityReport(con);
		//添加表数据
		String sql = "insert into lrnActivityReport (lar_c_itm_id,lar_p_itm_id,lar_usr_ent_id,lar_app_id,lar_tkh_id,lar_att_ats_id,lar_cov_score,lar_cov_total_time,lar_attempts_user,lar_total_attempt,";
		sql += " lar_app_create_timestamp,lar_app_status,lar_app_process_status,lar_att_timestamp,lar_att_create_timestamp,lar_att_remark,lar_att_rate,lar_cov_cos_id,lar_cov_commence_datetime, lar_cov_last_acc_datetime)";
		sql += " select lar_c_itm_id,lar_p_itm_id,lar_usr_ent_id,lar_app_id,lar_tkh_id,lar_att_ats_id,lar_cov_score,lar_cov_total_time,lar_attempts_user,lar_total_attempt, ";
		sql += " lar_app_create_timestamp,lar_app_status,lar_app_process_status,lar_att_timestamp,lar_att_create_timestamp,lar_att_remark,lar_att_rate,lar_cov_cos_id,lar_cov_commence_datetime, lar_cov_last_acc_datetime";
		sql += " from view_lrn_activity_group";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.executeUpdate();
		if(stmt != null){
			stmt.close();
		}
	}
	
	public void delLrnActivityReport(Connection con) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement("truncate table lrnActivityReport");
			stmt.executeUpdate();
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		init();
		process();
	}
}
