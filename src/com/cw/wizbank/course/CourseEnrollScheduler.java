package com.cw.wizbank.course;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemAccess;
import com.cw.wizbank.ae.aeItemRequirement;
import com.cw.wizbank.ae.aeQueueManagerAutoEnrol;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.db.DbMgMessage;
import com.cw.wizbank.db.view.ViewEntityToTree;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.ScheduledTask;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.utils.CommonLog;

public class CourseEnrollScheduler extends ScheduledTask {

	public static long Auto_Enroll_Interval = 0;
	
	public static String ENROLL_ACTION = "AUTO";

	public static String[] PROCESS_STATUS_LST = new String[] { aeApplication.ADMITTED, aeApplication.PENDING, aeApplication.WAITING };

//	private Connection con = null;

	private static Hashtable ste_lst;
	
	private long quota_exceed_notify_interval = 0;
	
	private long auto_enroll_interval = 0;

	public void init() {
		CourseEnrollScheduler.ste_lst = new Hashtable();
		if (this.param != null) {
			for (int i = 0; i < this.param.size(); i++) {
				ParamType paramType = (ParamType) this.param.get(i);
				if (paramType.getName().equals("quota_exceed_notify_interval")) {					
					try {
						this.quota_exceed_notify_interval = Long.valueOf(paramType.getValue()).longValue();	
					} 
					catch (Exception e) {	
						this.quota_exceed_notify_interval = 0;	
					}
				}
			}
		}
		this.auto_enroll_interval = (this.scheduledStatus.getPeriod()) / 60 / 1000;
	}

	protected void process() {
		this.setPriority(Thread.MIN_PRIORITY);
		try {
			con = dbSource.openDB(false);
			doEnrollment();
			con.commit();
		} catch (Exception e) {
			logger.debug("CourseEnrollScheduler.process() error", e);
			CommonLog.error("CourseEnrollScheduler.process() error"+e.getMessage(),e);
			
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
                    logger.debug("奥运圣火今天（2008年5月7日）下午五点左右从我们办公室楼下经过！北京奥运，中国加油！");
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
				pstmt.setString(index++, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
				if(hasUsrGroup) {
					pstmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
				}
				if(hasUsrGrade) {
					pstmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
				}
				pstmt.setLong(index++, site.ste_ent_id);
				pstmt.setString(index++, dbRegUser.USR_STATUS_OK);
				pstmt.setTimestamp(index++, curTime);
				pstmt.setLong(index++, lrn_rol_id);
				pstmt.setTimestamp(index++, curTime);
				pstmt.setString(index++, aeItem.ITM_STATUS_ON);
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
						 if(itm.itm_app_approval_type == null || itm.itm_app_approval_type.length() == 0 || qm.auto_enroll_ind) {
							 qm.insAppNoWorkflow(con, null, usr_ent_id, itm_id, null, ENROLL_ACTION, prof, itm);
						 } else {
							 qm.insApplication(con, null, usr_ent_id,
									 itm_id, prof, 0, 0, 0, 0, null, null, false, 0, null, ENROLL_ACTION);
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
		sql.append(" select distinct itm_id,itm_enroll_type,usr_ent_id ")
			.append(" from aeItem")
			.append(" inner join aeItemTargetRuleDetail on(itm_id = ird_itm_id and ird_type = ?)");
		if(hasUsrGroup) {
			sql.append(" inner join EntityRelation r1 on (ird_group_id = r1.ern_ancestor_ent_id and r1.ern_type = ?)");
			temp_ern_child_ent_id = "r1.ern_child_ent_id";
		}
		if(hasUsrGrade) {
			sql.append(" inner join EntityRelation r2 on (ird_grade_id = r2.ern_ancestor_ent_id and r2.ern_type = ?");
			temp_ern_child_ent_id = "r2.ern_child_ent_id";
		}
		if(hasUsrGroup && hasUsrGrade) {
			sql.append(" and r2.ern_child_ent_id = r1.ern_child_ent_id");
		}
		sql.append(")")
			.append(" left join RegUser on (").append(temp_ern_child_ent_id).append(" = usr_ent_id and usr_ste_ent_id = ? and usr_status = ?)")
			.append(" left join aeApplication on ( itm_id = app_itm_id and usr_ent_id = app_ent_id) ")
			.append(" left join acEntityRole on ( usr_ent_id = erl_ent_id and ? between erl_eff_start_datetime and erl_eff_end_datetime)")
			.append(" where erl_rol_id = ?")
			.append(" and app_id is null")
			.append(" and usr_ent_id is not null")
			.append(" and erl_ent_id is not null")
			.append(" and ? between itm_appn_start_datetime and itm_appn_end_datetime")
			.append(" and itm_status = ?")
			.append(" and itm_enroll_type is not null")
			.append(" and itm_enroll_type in (?, ?) and itm_owner_ent_id = ?")
			.append(" and (ird_skill_id = -1  or ( ird_skill_id > 0 and usr_ent_id in (")
			.append(" select uss_ent_id from RegUserSkillSet where ird_skill_id = uss_ske_id")
			.append(" union")
			.append(" select uss_ent_id from cmSkillBase, cmSkillSetCoverage, cmSkillSet, RegUserSkillSet")
			.append(" where ird_skill_id = skb_ske_id  and skb_id = ssc_skb_id")
			.append(" and ssc_sks_skb_id = sks_skb_id   and sks_ske_id = uss_ske_id )))")
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

}
