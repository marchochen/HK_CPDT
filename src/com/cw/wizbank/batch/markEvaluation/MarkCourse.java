package com.cw.wizbank.batch.markEvaluation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.batch.batchUtil.BatchUtils;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.impl.ParamTypeImpl;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.view.ViewCourseModuleCriteria;
import com.cw.wizbank.integratedlrn.IntegratedLrn;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwIniFile;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cwn.wizbank.utils.CommonLog;

import org.apache.log4j.*;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MarkCourse extends ScheduledTask implements Job{

	private String wbid ="sysadmin";;

	private String wbpsd = null;

	private boolean finalizeCourse = false;

	private Vector site = null;

	public MarkCourse(){
		wizbini = WizbiniLoader.getInstance();
		logger = Logger.getLogger(MessageScheduler.class);
		dbSource = new cwSQL();
		dbSource.setParam(wizbini);
	}
	public void init() {
		if (this.param != null) {
			for (int i = 0; i < this.param.size(); i++) {
				ParamType paramType = (ParamType) this.param.get(i);
				if (paramType.getName().equals("finalize_course")) {
					if (paramType.getValue().trim().equals("true") || paramType.getValue().trim().equals("yes")) {
						this.finalizeCourse = true;
					}
				}
				if (paramType.getName().equals("site")) {
					String value = paramType.getValue();
					if (value.trim().length() > 0) {
						this.site = cwUtils.splitToVec(value, "~");
					}
				}
			}
		}
	}

	protected void process() {
		try {
			this.con = this.dbSource.openDB(false);
			if (site == null) {
				site = new Vector();
				PreparedStatement stmt = acSite.getSite(con);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					site.addElement(new Long(rs.getLong("ste_ent_id")));
				}
				stmt.close();
			}
			for (int k = 0; k < site.size(); k++) {
				long siteId = ((Long) site.elementAt(k)).longValue();
				loginProfile prof = new loginProfile();
				acSite s = new acSite();
				s.ste_ent_id = siteId;
				s.ste_trusted = true;
				String code;
				try {
					code = dbRegUser.mark_login(con, prof, wbid, wbpsd, s, true, wizbini);
					if (code.equals(dbRegUser.CODE_LOGIN_SUCCESS)) {
					}
					else {
						System.err.println("login failure");
						throw new cwException("login failure");
					}
				}
				catch (qdbException e) {
					 CommonLog.error(e.getMessage(),e);
				}
				if (finalizeCourse)
					finalizeCourse(logger, con, siteId, prof);
				
				 con.commit();
                 //final the Integrated Learning course
                 finalizeIntegratedCourse(logger, con, siteId, prof);
                 con.commit();
			}
			con.commit();

		}
		catch (Exception e) {
			logger.debug("MarkCourse.process() error", e);
			CommonLog.error(e.getMessage(),e);
            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                }
            } catch (SQLException e1) {
            	CommonLog.error(e1.getMessage(),e1);
            }
		}
		finally {
			if (this.con != null) {
				try {
					con.close();
				}
				catch (SQLException e) {
					logger.debug("MarkCourse.process() error", e);
					logger.debug("奥运圣火今天（2008年5月7日）下午五点左右从我们办公室楼下经过！北京奥运，中国加油！");
					 CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}

    public static void main(String argv[]) {
        try{
            Vector site = null;
//            boolean setAttend = false;
            boolean finalizeCourse = false;
            for (int i = 0; i < argv.length; i++) {
                System.out.println(argv[i]);
                // if it is an option argument
                if (argv[i].charAt(0) == '-') {
                    if (argv[i].equals("-s")) 
                        site = cwUtils.splitToVec(argv[++i], "~");
                    
//                    else if (argv[i].equals("-a")) 
//                        setAttend = true;
                    
                    else if (argv[i].equals("-f")) 
                        finalizeCourse = true;

                }else{
                    String inifile = argv[i++];
                    WizbiniLoader wizbini = new WizbiniLoader(argv[i]);
                    cwSQL sqlCon = new cwSQL();
                    sqlCon.setParam(wizbini);
                    Connection con = sqlCon.openDB(true);
                    if (site == null){
                        site = new Vector();
                        PreparedStatement stmt = acSite.getSite(con);
                        ResultSet rs = stmt.executeQuery();
                        while(rs.next()){
                            site.addElement(new Long(rs.getLong("ste_ent_id")));    
                        }
                        stmt.close();
                    }
                    for (int k=0; k<site.size(); k++){
                        long siteId = ((Long)site.elementAt(k)).longValue();
                        loginProfile prof = BatchUtils.getProf(con, siteId, new cwIniFile(inifile), wizbini);  

//                        if (setAttend)
//                            setAttendanceStatus(con, siteId, prof);
                        if (finalizeCourse)
							finalizeCourse(null, con, siteId, prof);
                        
                        con.commit();
                        //final the Integrated Learning course
                        finalizeIntegratedCourse(null, con, siteId, prof);
                        con.commit();
                    }
                    con.commit(); 
                }
            }
        }catch(Exception e){
            CommonLog.error(e.getMessage(),e);
            System.err.println("Error:" + e.getMessage());    
        }
    }        

    /*
    *   when the course is finished
    */
    public static void finalizeCourse(Logger logger, Connection con, long root_ent_id, loginProfile prof) throws cwException, SQLException, cwSysMessage   , qdbException, qdbErrMessage{
        ViewCourseModuleCriteria.ViewAttendDate[] modCrit = ViewCourseModuleCriteria.getExpiredList(con, root_ent_id, DbCourseCriteria.TYPE_COMPLETION,false);
        setAttendNReScore(logger, con, prof, modCrit, true);
    }
    
    public static void finalizeIntegratedCourse(Logger logger, Connection con, long root_ent_id, loginProfile prof) throws cwException, SQLException, cwSysMessage {
        Vector app_lst = ViewCourseModuleCriteria.getProgressAppList(con, root_ent_id,true);
        IntegratedLrn intg_lrn = new IntegratedLrn();
        intg_lrn.markCourseApp( logger,  con,  prof,  app_lst,root_ent_id);
    }
/*    
    public static void setAttendanceStatus(Connection con, long root_ent_id, loginProfile prof) throws SQLException, cwException, cwSysMessage{
        ResultSet rs = null;
        rs = ViewCourseModuleCriteria.getActiveList(con, root_ent_id, DbCourseCriteria.TYPE_COMPLETION);
        setAttendNReScore(con, root_ent_id, prof, rs, false);
    }
*/
    
    /*
    *   set to incomplete only in finalize
    */
    
    public static void setAttendNReScore(Logger logger, Connection con, loginProfile prof, ViewCourseModuleCriteria.ViewAttendDate[] modCrit, boolean setFinal) throws SQLException, cwException, cwSysMessage   , qdbException, qdbErrMessage{
        for (int i = 0; i < modCrit.length; i++) {
//            boolean itmCreateSession = modCrit[i].itm_create_session_ind;
//            if (itmCreateSession){
//                return;
//            }
            DbCourseCriteria ccr = new DbCourseCriteria();
            ccr.ccr_id = modCrit[i].ccr_id;        
            ccr.ccr_all_cond_ind = modCrit[i].ccr_all_cond_ind;
            ccr.ccr_pass_ind = modCrit[i].ccr_pass_ind;
            ccr.ccr_pass_score = (int)modCrit[i].ccr_pass_score;
            ccr.ccr_pass_score_f = modCrit[i].ccr_pass_score;
            ccr.ccr_attendance_rate = modCrit[i].ccr_attendance_rate;
            ccr.ccr_itm_id = modCrit[i].ccr_itm_id;
            ccr.ccr_type = modCrit[i].ccr_type;
            ccr.ccr_upd_method = modCrit[i].ccr_upd_method;

            int duration = modCrit[i].itm_content_eff_duration;
            /*
            Timestamp itmContentStartDate = modCrit[i].itm_content_eff_start_datetime;
            Timestamp itmContentEndDate = modCrit[i].itm_content_eff_end_datetime;
            
            Timestamp startDate;
            Timestamp endDate;
            if (itmContentStartDate!=null && itmContentEndDate!=null){
                startDate = itmContentStartDate;
                endDate = itmContentEndDate;
            }else if (duration !=0){
                startDate = modCrit[i].att_create_timestamp;
                endDate = new Timestamp(startDate.getTime() + (long)duration*24*60*60*1000);
            }else{
                startDate = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
                endDate = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
            }
            */
			if (logger != null) {
				logger.debug("++++++ reEvaluation Date: " + cwSQL.getTime(con));
			}
//            CourseCriteria.setAttend(con, prof, ccr, modCrit[i].cov_ent_id, modCrit[i].cos_res_id, /*startDate, endDate, */
//                modCrit[i].cov_status, modCrit[i].cov_score, modCrit[i].app_itm_id, modCrit[i].app_id, modCrit[i].cov_tkh_id, true, false, setFinal); 
//            
            CourseCriteria.setAttendOhter( con,  prof, modCrit[i].cos_res_id, modCrit[i].cov_ent_id, modCrit[i].cov_tkh_id, modCrit[i].app_id, true,  setFinal,
                    false, false);
        }
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