package com.cwn.wizbank.scheduled;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.message.MessageScheduler;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.cpd.service.CpdManagementService;
import com.cwn.wizbank.cpd.service.CpdOutstandingEmailService;
import com.cwn.wizbank.cpd.service.CpdRegistrationMgtService;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.web.WzbApplicationContext;

public class CpdOutstandingHoursEmailScheduler extends ScheduledTask implements Job{
		
    
    CpdOutstandingEmailService cpdOutstandingEmailService = (CpdOutstandingEmailService) WzbApplicationContext.getBean("cpdOutstandingEmailService");
    
    CpdManagementService cpdManagementService = (CpdManagementService) WzbApplicationContext.getBean("cpdManagementService");
    
    CpdRegistrationMgtService cpdRegistrationMgtService = (CpdRegistrationMgtService) WzbApplicationContext.getBean("cpdRegistrationMgtService");
    
    public CpdOutstandingHoursEmailScheduler(){
        logger = Logger.getLogger(MessageScheduler.class);
    }
    
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void process() {
		try{
            wizbini = WizbiniLoader.getInstance();
            dbSource = new cwSQL();
            dbSource.setParam(wizbini);
		    con = dbSource.openDB(false);
            run(con);
            con.commit();
		}catch(Exception e){
			e.printStackTrace();
			CommonLog.error(e.getMessage(),e);
			e.printStackTrace();
           try {
               if (con != null && !con.isClosed()) {
                   con.rollback();
               }
           } catch (SQLException e1) {
        	   CommonLog.error(e.getMessage(),e);
           }
		} finally {
			if (this.con != null) {
				try {
					con.close();
				} catch (SQLException e) {
                    CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}
	
	

    public void run(Connection con) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        //获取当前月份（需要+1因为从0开始算）
        int currentMon = cal.get(Calendar.MONTH)+1;
        int currentDate = cal.get(Calendar.DAY_OF_MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        //如果开放了cpd功能
        try {
            if(AccessControlWZB.hasCPDFunction()){
                try {
                    loginProfile prof = new loginProfile();
                    dbRegUser sender =  new dbRegUser();
                    acSite site = new acSite();
                    site.ste_ent_id = 1;
                    sender.usr_ent_id = site.getSiteSysEntId(con);
                    sender.get(con);
                    prof.usr_id = sender.usr_id;
                    prof.my_top_tc_id=1;
                    prof.cur_lan = "en-us";
                    String sender_usr_id = sender.usr_id;
                    

                    //获取当天需要发送 CPT/D Outstanding Hours Email 邮件的大牌
                    List<CpdType> cpdTypes =  cpdManagementService.getCpdTypeOutStandingEmail(currentMon,currentDate);
                    
                    Timestamp sendTime = cwSQL.getTime(con);
                    for (int i = 0; i < cpdTypes.size(); i++) {
                        cpdOutstandingEmailService.addOutStandingEmailLearner( prof, cpdTypes.get(i).getCt_id(), con,sendTime,wizbini,sender_usr_id) ;//生成cpd未完成时数邮件（学员）
                        cpdOutstandingEmailService.addOutStandingEmailSupervisor( prof, cpdTypes.get(i).getCt_id(), con,sendTime,wizbini,sender_usr_id) ;//生成cpd未完成时数邮件（我的下属）
                        cpdManagementService.updLastEmailSendTime(sendTime,cpdTypes.get(i).getCt_id());//修改牌照最后一次发送邮件时间
                    }
                    
                    
                } catch (Exception e) {
                    CommonLog.error("CPT/D Outstanding Hours Email Alert  exception:"+e.getMessage(),e);
                }
                    
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        init();
        process();
    }
}
