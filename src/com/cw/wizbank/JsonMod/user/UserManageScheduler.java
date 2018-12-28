package com.cw.wizbank.JsonMod.user;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class UserManageScheduler  extends ScheduledTask implements Job{

	public UserManageScheduler(){
		logger = Logger.getLogger(MessageScheduler.class);
	}
	
	protected void init() {
		
	}

	protected void process() {
		try {
			wizbini = WizbiniLoader.getInstance();
			dbSource = new cwSQL();
			dbSource.setParam(wizbini);
			con = dbSource.openDB(false);
			delExpiredUser();
			con.commit();
		}
		catch (Exception e) {
			logger.debug("error in UserManageScheduler process()");
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
				logger.debug("error in UserManageScheduler()");
				logger.debug(e);
			}
		}
	}
	
	
	//删除过期用户
	public void delExpiredUser() throws SQLException,qdbException, qdbErrMessage, cwSysMessage{
		
		//构造loginProfile信息
		String sql_get_all_site = "select ste_ent_id,ste_id, ste_default_sys_ent_id from acSite where ste_status=?";
		PreparedStatement pstmt_ste = con.prepareStatement(sql_get_all_site);
		pstmt_ste.setString(1, acSite.STATUS_OK);
		ResultSet rs_ste = pstmt_ste.executeQuery();
		loginProfile prof = null;
		dbRegUser usr  = null;
		while (rs_ste.next()) 
		{
			Long ste_ent_id = new Long(rs_ste.getLong("ste_ent_id"));
			long ste_default_sys_ent_id = rs_ste.getLong("ste_default_sys_ent_id");
			prof = new loginProfile();
	        prof.usr_ent_id = ste_default_sys_ent_id;
	        prof.root_ent_id = ste_ent_id.longValue();
	        prof.current_role = AccessControlWZB.ROL_STE_UID_TADM + "_" + ste_ent_id;
	        usr = new dbRegUser();
	        usr.usr_ent_id = prof.usr_ent_id;
	        try {
	        prof.usr_id = usr.getUserId(con);
			} catch (qdbException e1) {
				e1.printStackTrace();
			}
			
		}
		cwSQL.cleanUp(rs_ste,pstmt_ste);
		List usrList = dbRegUser.getAllUser(con);
		for(int i = 0 ;i < usrList.size() ;i++)
		{
			dbRegUser reguser = (dbRegUser)usrList.get(i);
			try
			{
				ObjectActionLog log = new ObjectActionLog(reguser.usr_ent_id, 
						reguser.usr_ste_usr_id,
						reguser.usr_display_bil,
						ObjectActionLog.OBJECT_TYPE_USR,
						ObjectActionLog.OBJECT_ACTION_EXPIRE,
						ObjectActionLog.OBJECT_ACTION_TYPE_BATCH,
						prof.getUsr_ent_id(),
						prof.getUsr_last_login_date(),
						prof.getIp()
				);
				reguser.del(con,prof,reguser.usr_parent_usg_id,null);	
				SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
			}
			catch(qdbErrMessage ex)
			{
				ex.printStackTrace();
				continue;
			}
			catch(cwSysMessage ex)
			{
				ex.printStackTrace();
				continue;
			}
			
		 }
		
	}


	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		init();
		process();
	}
}
