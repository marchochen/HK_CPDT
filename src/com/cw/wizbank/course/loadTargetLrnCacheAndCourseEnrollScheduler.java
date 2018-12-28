package com.cw.wizbank.course;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import com.cw.wizbank.ScheduledStatus;
import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.ae.aeItemRequirement;
import com.cw.wizbank.ae.aeQueueManagerAutoEnrol;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.config.system.scheduledtask.TaskType;
import com.cw.wizbank.config.system.scheduledtask.impl.ParamTypeImpl;
import com.cw.wizbank.config.system.scheduledtask.impl.TaskTypeImpl;
import com.cw.wizbank.db.DbItemTargetLrnDetail;
import com.cw.wizbank.db.DbMgMessage;
import com.cw.wizbank.db.view.ViewEntityToTree;
import com.cw.wizbank.itemtarget.ManageItemTarget;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ScheduledStatus;
import com.cw.wizbank.ScheduledTask;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class loadTargetLrnCacheAndCourseEnrollScheduler extends ScheduledTask implements Job{

	public static long Auto_Enroll_Interval = 0;
	
	
	public static String ENROLL_ACTION = "AUTO";

	public static String[] PROCESS_STATUS_LST = new String[] { aeApplication.ADMITTED, aeApplication.PENDING, aeApplication.WAITING };

//	private Connection con = null;

	private static Hashtable ste_lst;
	
	
	private long auto_enroll_interval = 0;

	public loadTargetLrnCacheAndCourseEnrollScheduler(){
		logger = Logger.getLogger(MessageScheduler.class);
		TaskType taskype = new TaskTypeImpl();
		scheduledStatus = new ScheduledStatus(taskype, Calendar.getInstance());
	}

	public void init() {
		loadTargetLrnCacheAndCourseEnrollScheduler.ste_lst = new Hashtable();
		if (this.param != null) {
			for (int i = 0; i < this.param.size(); i++) {
				ParamType paramType = (ParamType) this.param.get(i);
			}
		}
		this.auto_enroll_interval = (this.scheduledStatus.getPeriod()) / 60 / 1000;
	}

	protected void process() {
		this.setPriority(Thread.MIN_PRIORITY);
		try {
			wizbini = WizbiniLoader.getInstance();
			dbSource = new cwSQL();
			dbSource.setParam(wizbini);
			con = dbSource.openDB(false);
			loadTargetLrnCache() ; // 把目标规则学员预加载到一张中间中
			doEnrollment(); // 执行报名动作
			con.commit();
		} catch (Exception e) {
			logger.debug("CourseEnrollScheduler.process() error", e);
			CommonLog.error(e.getMessage(),e);
            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                CommonLog.error(e1.getMessage(),e1);
            }
		} finally {
			if (this.con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.debug("CourseEnrollScheduler.process() error", e);
					CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}

	public void doEnrollment() throws SQLException, qdbException, IOException, cwSysMessage, cwException {
		Timestamp curTime = cwSQL.getTime(con);

		String sql_get_all_site = "select ste_ent_id,ste_id, ste_default_sys_ent_id from acSite where ste_status=?";
		PreparedStatement pstmt_ste = con.prepareStatement(sql_get_all_site);
		pstmt_ste.setString(1, acSite.STATUS_OK);

		ResultSet rs_ste = pstmt_ste.executeQuery();

		while (rs_ste.next()) {
			boolean do_enroll_flag = false;
			Long ste_ent_id = new Long(rs_ste.getLong("ste_ent_id"));
			String ste_id = rs_ste.getString("ste_id");
			long ste_default_sys_ent_id = rs_ste.getLong("ste_default_sys_ent_id");
			Timestamp last_executed = (Timestamp) ste_lst.get(ste_ent_id);
			if (last_executed == null) {
				// never executed before
				do_enroll_flag = true;
			} else {
				long diff = (curTime.getTime() - last_executed.getTime()) / (1000 * 60);
				if (diff >= auto_enroll_interval)
					do_enroll_flag = true;
			}
			if (do_enroll_flag) {
				loginProfile prof = new loginProfile();
				acSite site = new acSite();
				site.ste_ent_id = ste_ent_id.longValue();
		        prof.usr_ent_id = ste_default_sys_ent_id;
		        prof.root_ent_id = site.ste_ent_id;
		        prof.root_id = ste_id;
		        prof.current_role = AccessControlWZB.ROL_STE_UID_TADM + "_" + ste_ent_id;
		        dbRegUser usr = new dbRegUser();
		        usr.usr_ent_id = prof.usr_ent_id;
		        try {
					usr.get(con);
				} catch (qdbException e1) {
					e1.printStackTrace();
				}
				prof.usr_id = usr.usr_id;
				boolean hasUsrGroup = false;
				boolean hasUsrGrade = false;
				String[] acsite_targeted_entity = ViewEntityToTree.getTargetEntity(con, site.ste_ent_id);
				for (int i=0; i<acsite_targeted_entity.length; i++) {
					if (acsite_targeted_entity[i].equals("user_group")) {
						hasUsrGroup = true;
					}
					if (acsite_targeted_entity[i].equals("grade")) {
						hasUsrGrade = true;
					}
				}
				long lrn_rol_id = getLrnRolId(con, site.ste_ent_id);
				String SQL = getAutoEnrolItemAndTargetLrnSQL(con, acsite_targeted_entity);
				PreparedStatement pstmt = con.prepareStatement(SQL);
				int index = 1;
	            pstmt.setString(index++, dbRegUser.USR_STATUS_OK);
				pstmt.setTimestamp(index++, curTime);

				//pstmt.setString(index++, aeItem.ITM_STATUS_ON);
				pstmt.setString(index++, aeItem.ITM_AUTO_ENROLL_TYPE_AUTO_CONFIRM);
				pstmt.setString(index++, aeItem.ITM_AUTO_ENROLL_TYPE_AUTO_ENROLL);
				pstmt.setLong(index++, site.ste_ent_id);
				ResultSet rs = pstmt.executeQuery();
				long itm_id = 0;
				aeItem itm = new aeItem();
				while (rs.next()) {
					aeQueueManagerAutoEnrol qm = new aeQueueManagerAutoEnrol();
					itm_id = rs.getLong("itm_id");
					if(itm_id !=0 && itm.itm_id != itm_id){
						itm.itm_id = itm_id;
						itm.getItem(con);
					}
					long usr_ent_id = rs.getLong("usr_ent_id");
					String enroll_type = rs.getString("itm_enroll_type");
					if (enroll_type != null && enroll_type.equalsIgnoreCase(aeItem.ITM_AUTO_ENROLL_TYPE_AUTO_CONFIRM)){
						qm.auto_enroll_ind = true;
					} else{
						qm.auto_enroll_ind = false;
					}
					if (!hasAutoEnrolAppnConflict(con, itm_id, usr_ent_id)) {
						 if(qm.auto_enroll_ind || itm.itm_app_approval_type == null || itm.itm_app_approval_type.length() == 0 || qm.auto_enroll_ind) {
							 qm.insAppNoWorkflow(con, null, usr_ent_id, itm_id, null, ENROLL_ACTION, prof, itm);
						 } else {
							 qm.insApplication(con, null, usr_ent_id, itm_id, prof, 0, 0, 0, 0, null, null, false, 0, null, ENROLL_ACTION);
						 }
					}
					con.commit();
				}
				rs.close();
				pstmt.close();
				ste_lst.put(ste_ent_id, cwSQL.getTime(con));
				logger.debug("AutoEnroll for ste_ent_id=" + ste_ent_id + ":Completed");
			}			
		}
		rs_ste.close();
		pstmt_ste.close();
		return;
	}
	
	
	public void loadTargetLrnCache() throws SQLException, cwSysMessage {

        //先清空所有数据
        DbItemTargetLrnDetail.delAll(con) ;
        
        StringBuffer sql_get_all_itm = new StringBuffer();
        sql_get_all_itm.append("select distinct itm_id from aeItem, aeitemtargetrule where itm_id = itr_itm_id and itm_life_status is null")
        .append(" union ")
        .append("select distinct itm_id from aeitem,UserPositionLrnItem where itm_id = upi_itm_id and itm_life_status is null")
        .append(" union ")
        .append("select distinct itm_id from aeitem,ProfessionLrnItem where itm_id = psi_itm_id and itm_life_status is null");
                
        PreparedStatement pstmt = con.prepareStatement(sql_get_all_itm.toString());
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
           long itm_id = rs.getLong("itm_id");
           ManageItemTarget.setTargetCache(con,  itm_id, false) ;  
        }
        rs.close();
        pstmt.close();
        con.commit();
        logger.debug("loadTargetLrnCache Completed");     
        return;
    }
	
	
	public static String getQuotaExceedNotifyXML(Connection con, String sender_id, String ent_ids, aeItem itm, long appn_wait_count) throws SQLException, cwException, qdbException, cwSysMessage {
		StringBuffer xml = new StringBuffer();
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
        
        if (ent_ids != null && ent_ids.length() > 0) {
        	long[] rec_ent_id_lst = cwUtils.splitToLong(ent_ids, "~");
        	xml.append("<recipient>").append(cwUtils.NEWL);
        	for (int i = 0; i < rec_ent_id_lst.length; i++) {
                dbRegUser dbRecip = new dbRegUser();
                dbRecip.usr_ent_id = rec_ent_id_lst[i];
                dbRecip.get(con);
                xml.append("<entity ent_id=\"").append(dbRecip.usr_ent_id).append("\" ")
                   .append(" usr_id=\"").append(dbUtils.esc4XML(dbRecip.usr_id)).append("\" ")
                   .append(" display_name=\"").append(dbUtils.esc4XML(dbRecip.usr_display_bil)).append("\" ")
                   .append(" email=\"").append(dbUtils.esc4XML(dbRecip.usr_email)).append("\" ")
                   .append(" ste_usr_id=\"").append(dbUtils.esc4XML(dbRecip.usr_ste_usr_id)).append("\"/>");
        	}
        	xml.append("</recipient>").append(cwUtils.NEWL);
        }

        itm.get(con);
        xml.append("<item code=\"").append(cwUtils.esc4XML(itm.itm_code))
	    	.append("\" title=\"").append(cwUtils.esc4XML(itm.itm_title))
	    	.append("\" quota=\"").append(itm.itm_capacity)
	    	.append("\" wait_count=\"").append(appn_wait_count)
	    	.append("\"/>");
		return xml.toString();
	}
	
	public boolean hasAutoEnrolAppnConflict(Connection con, long itm_id, long usr_ent_id) 
	throws cwException, SQLException, qdbException, cwSysMessage {		
		//check if the learner passes prerequisite criteria
		aeItemRequirement itm_req = new aeItemRequirement();
		Hashtable requirement = itm_req.checkPrerequisiteAndGetConditionRuleList(con, usr_ent_id, itm_id, aeItemRequirement.REQ_SUBTYPE_ENROLLMENT, 1);
		if(requirement.containsKey("false")) {
			return true;
		} 
		return false;
	}

	// if target learner dimension of the organization is neither one nor two, the following sql should be modified
	// to enhance in the future
	private String getAutoEnrolItemAndTargetLrnSQL(Connection con, String[] acsite_targeted_entity) throws SQLException{
		boolean hasUsrGroup = false;
		boolean hasUsrGrade = false;
		for (int i=0; i<acsite_targeted_entity.length; i++) {
			if (acsite_targeted_entity[i].equals("user_group")) {
				hasUsrGroup = true;
			}
			if (acsite_targeted_entity[i].equals("grade")) {
				hasUsrGrade = true;
			}
		}
		StringBuffer sql = new StringBuffer();
		String temp_ern_child_ent_id = null;
		sql.append(" select distinct itm_id,itm_enroll_type,itm_app_approval_type,usr_ent_id from aeItem, itemTargetLrnDetail,reguser ")
			.append(" where itm_id = itd_itm_id and  usr_ent_id = itd_usr_ent_id and usr_status=?")
			.append("  and not exists(select app_id from aeApplication where app_itm_id =itm_id and app_ent_id = usr_ent_id)")
		
			.append(" and ? between itm_appn_start_datetime and itm_appn_end_datetime")
			//.append(" and itm_status = ?")
			.append(" and itm_enroll_type is not null")
			.append(" and itm_enroll_type in (?, ?) and itm_owner_ent_id = ?")
		
			.append(" order by itm_id");
		return sql.toString();
	}
	
	private long getLrnRolId(Connection con, long rol_ste_ent_id) throws SQLException {
		long lrn_rol_id = 0;
		String sql = "select rol_id from acFunction, acRoleFunction, acRole where ftn_ext_id = ? and ftn_id = rfn_ftn_id and rfn_rol_id = rol_id and rol_ste_ent_id = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, AclFunction.FTN_LRN_LEARNING_SIGNUP);
		stmt.setLong(2, rol_ste_ent_id);
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			lrn_rol_id = rs.getLong("rol_id");
		}
		stmt.close();
		return lrn_rol_id;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Map<String,Object> params = jobExecutionContext.getMergedJobDataMap();
		if (params == null){
			param = new ArrayList();
		}
		for (String key : params.keySet()){
			ParamType paramType = new ParamTypeImpl();
			paramType.setName(key);
			paramType.setValue(params.get(key).toString());
			param.add(paramType);
		}
		init();
		process();
	}
}
