package com.cw.wizbank.itemtarget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.db.DbItemTargetRule;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.db.DbItemTargetLrnDetail;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.DbUserPosition;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class ManageItemTarget {
	
	public static class TargetUser {
		long usr_ent_id;
		String usr_ste_usr_id;
		String usr_display_bil;
		String usr_usg_display_bil;
		String usr_ugr_display_bil;
	}
	//时间格式化
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private static SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	public static String getItemTargetRuleLstXML(Connection con, ItemTargetReqParam urlp, long root_ent_id) throws SQLException, cwSysMessage {
		StringBuffer xml = new StringBuffer(512);
		aeItem itm = new aeItem();
		itm.itm_id = urlp.itm_id;
		itm.getItem(con);
        xml.append(aeItem.getNavAsXML(con, itm.itm_id));
		xml.append("<item id=\"").append(urlp.itm_id).append("\"")
			.append(" create_run_ind=\"").append(itm.itm_create_run_ind).append("\"")
			.append(" type=\"").append(itm.itm_type).append("\"");
		if(itm.itm_run_ind) {
			if(itm.itm_target_enrol_type == null) {
				itm.itm_target_enrol_type = aeItem.ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER;
			}
			xml.append(" target_enrol_type=\"").append(itm.itm_target_enrol_type).append("\"");
		}
		xml.append("/>");
		long itm_id = urlp.itm_id;
		String target_type = urlp.itm_target_type;
		if(itm.itm_run_ind && itm.itm_target_enrol_type.equalsIgnoreCase(aeItem.ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER)) {
			target_type = DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER;
            aeItemRelation aeIre = new aeItemRelation();
            aeIre.ire_child_itm_id = itm_id;
            aeIre.getParentItemId(con);
            itm_id = aeIre.ire_parent_itm_id;
		}
		xml.append("<target_rule_lst type=\"").append(urlp.itm_target_type).append("\"")
			.append(" last_upd_time=\"").append(DbItemTargetRule.getLastTimestamp(con, itm_id, target_type)).append("\">");//last_upd_time:only for target_enrollment_rule
		Vector vec = DbItemTargetRule.get(con, itm_id, target_type);
		for(int i=0; i<vec.size(); i++) {
			DbItemTargetRule itr = (DbItemTargetRule)vec.elementAt(i);
			xml.append("<target_rule id=\"").append(itr.itr_id).append("\"")
				.append(" modified_date=\"").append(itr.itr_update_timestamp).append("\"")
				.append(" modified_by=\"").append(cwUtils.esc4XML(dbRegUser.getUserName(con, itr.itr_update_usr_id))).append("\">");

			xml.append(getTargetGroupXML(con, itr.itr_group_id, root_ent_id));
			xml.append(getTargetGradeXML(con, itr.itr_grade_id, root_ent_id));
			xml.append(getTargetSkillXml(con, itr.itr_upt_id));
			xml.append("</target_rule>");
		}
		xml.append("</target_rule_lst>");
		return xml.toString();
	}
	
	public static String setItemTargetRulePrevXML(Connection con, ItemTargetReqParam urlp, long root_ent_id) throws SQLException, cwSysMessage {
		StringBuffer xml = new StringBuffer(512);
		//xml.append("<item id=\"").append(urlp.itm_id).append("\"/>");
		xml.append("<item id=\"").append(urlp.itm_id).append("\"")
		   .append(" tcr_id=\"").append(aeItem.getTcrId(con, urlp.itm_id, root_ent_id)).append("\"/>")
		   .append("<target_rule type=\"").append(urlp.itm_target_type).append("\"");
		if(urlp.rule_id != 0) {
			DbItemTargetRule itr = DbItemTargetRule.getById(con, urlp.rule_id);
			String updateTime="";
			if(itr.itr_update_timestamp.toString()!= null && itr.itr_update_timestamp.toString().length() > 0){
				updateTime=format.format(itr.itr_update_timestamp);
			}
			xml.append(" id=\"").append(urlp.rule_id).append("\"")
				.append(" timestamp=\"").append(updateTime).append("\">");
			xml.append(getTargetGroupXML(con, itr.itr_group_id, root_ent_id));
			xml.append(getTargetGradeXML(con, itr.itr_grade_id, root_ent_id));
			xml.append(getTargetSkillXml(con, itr.itr_upt_id));
			xml.append("<itr_group_ind>").append(itr.itr_group_ind).append("</itr_group_ind>");
			xml.append("<itr_grade_ind>").append(itr.itr_grade_ind).append("</itr_grade_ind>");
			xml.append("<itr_skill_ind>").append(itr.itr_position_ind).append("</itr_skill_ind>");
			xml.append("<itr_compulsory_ind>").append(itr.itr_compulsory_ind).append("</itr_compulsory_ind>");
			
		} else {
			xml.append(">");
		}
		xml.append("</target_rule>");
		return xml.toString();
	}

	public static void saveTargetRule (Connection con, ItemTargetReqParam urlp, loginProfile prof) throws SQLException {
		Timestamp cur_time = cwSQL.getTime(con);
		DbItemTargetRule itr = new DbItemTargetRule();
		
		Vector target_group = cwUtils.splitToVec(urlp.target_group_lst, ",");
		Long root_group = new Long(prof.root_ent_id);
		if(target_group.contains(root_group)) {
			target_group.clear();
			target_group.addElement(root_group);
			itr.itr_group_id = String.valueOf(prof.root_ent_id);
		} else {
			itr.itr_group_id = urlp.target_group_lst;
		}
		Vector target_grade = cwUtils.splitToVec(urlp.target_grade_lst, ",");
		long grade_root = DbUserGrade.getGradeRoot(con, prof.root_ent_id);
		Long root_grade = new Long(grade_root);
		if(target_grade.contains(root_grade)) {
			target_grade.clear();
			target_grade.addElement(root_grade);
			itr.itr_grade_id = String.valueOf(grade_root);
		} else {
			itr.itr_grade_id = urlp.target_grade_lst;
		}
		
		if(urlp.target_skill_lst==null){
			urlp.target_skill_lst ="-1";
		}
		Vector target_skill = cwUtils.splitToVec(urlp.target_skill_lst, ",");
		itr.itr_upt_id =urlp.target_skill_lst;
		
		itr.itr_update_usr_id = prof.usr_id;
		itr.itr_update_timestamp = cur_time;

		itr.itr_group_ind = urlp.itr_group_ind;
		itr.itr_grade_ind = urlp.itr_grade_ind;
		itr.itr_position_ind = urlp.itr_skill_ind;
		itr.itr_compulsory_ind = urlp.itr_compulsory_ind;
		
		DbItemTargetRuleDetail ird = new DbItemTargetRuleDetail();
		if(urlp.rule_id > 0) {
			ird.ird_itr_id = urlp.rule_id;
			ird.del(con);

			itr.itr_id = urlp.rule_id;
			itr.upd(con);
		} else {
			itr.itr_itm_id = urlp.itm_id;
			itr.itr_type = urlp.itm_target_type;
			itr.itr_create_usr_id = prof.usr_id;
			itr.itr_create_timestamp = cur_time;
			itr.ins(con);
		}

		ird.ird_itm_id = urlp.itm_id;
		ird.ird_itr_id = itr.itr_id;
		ird.ird_type = urlp.itm_target_type;
		ird.ird_create_usr_id = prof.usr_id;
		ird.ird_create_timestamp = cur_time;
		ird.ird_update_usr_id = prof.usr_id;
		ird.ird_update_timestamp = cur_time;
		for (int i=0; i<target_group.size(); i++) {
			ird.ird_group_id = ((Long)target_group.elementAt(i)).longValue();  
			for(int j=0; j<target_grade.size(); j++) {
				ird.ird_grade_id = ((Long)target_grade.elementAt(j)).longValue();
				for(int n=0; n<target_skill.size(); n++){
					ird.ird_upt_id =((Long)target_skill.elementAt(n)).longValue();
					ird.ins(con);
				}
			}
		}
	}
	
	public static void delTargetRuleById(Connection con, long rule_id) throws SQLException {
		DbItemTargetRuleDetail ird = new DbItemTargetRuleDetail();
		ird.ird_itr_id = rule_id;
		ird.del(con);

		DbItemTargetRule itr = new DbItemTargetRule();
		itr.itr_id = rule_id;
		itr.del(con);
	}
	
	private static Vector getTargetUserLst (Connection con, ItemTargetReqParam urlp) throws SQLException {
		Vector vec = new Vector();
		if(urlp.target_group_lst != null && urlp.target_grade_lst != null) {
			if(urlp.cwPage.sortCol == null) {
				urlp.cwPage.sortCol = "usr_ste_usr_id";
			}
			if(urlp.cwPage.sortOrder == null) {
				urlp.cwPage.sortOrder = "asc";
			}
			StringBuffer sql = new StringBuffer(512);
			sql.append(" select distinct usr_ent_id, usr_ste_usr_id, usr_display_bil, usg_display_bil, ugr_display_bil")
			.append(" from userGroup, userGrade, reguser")
			.append(" inner join EntityRelation r1 on(r1.ern_ancestor_ent_id in (") 
			.append(urlp.target_group_lst)
			.append(") and r1.ern_type = ?)");
			if(urlp.target_grade_lst != null && !urlp.target_grade_lst.equals("-1")) {
				sql.append(" inner join EntityRelation r2 on(r2.ern_ancestor_ent_id in (")
				.append(urlp.target_grade_lst)
				.append(") and r2.ern_type = ? and r2.ern_child_ent_id=r1.ern_child_ent_id)");
			} else {
				sql.append(" inner join EntityRelation r2 on (r2.ern_child_ent_id = usr_ent_id and r2.ern_type = ?)");
			}
//			if(urlp.target_skill_lst !=null ){
//				sql.append(" inner join RegUserSkillSet on(usr_ent_id =uss_ent_id)");
//			}
			sql.append(" where usr_status = ?")
			.append(" and usr_ent_id = r1.ern_child_ent_id")
			.append(" and usg_ent_id = (")
			.append(" select ern_ancestor_ent_id ")
			.append(" from EntityRelation")
			.append(" where ern_parent_ind = ?")
			.append(" and ern_child_ent_id = usr_ent_id")
			.append(" and ern_type = ? )");
//			if(urlp.target_grade_lst != null && !urlp.target_grade_lst.equals("-1")) {
				sql.append(" and ugr_ent_id = (")
				.append(" select ern_ancestor_ent_id from EntityRelation")
				.append(" where ern_parent_ind = ?")
				.append(" and ern_child_ent_id = usr_ent_id")
				.append(" and ern_type = ?)");
//			}
			if (urlp.target_skill_lst !=null&& !urlp.target_skill_lst.equals("-1")) {

				sql.append(" and usr_ent_id in (select upr_usr_ent_id from UserPositionRelation where upr_upt_id in(").append(urlp.target_skill_lst).append("))");
			}
			sql.append(" order by ").append(urlp.cwPage.sortCol).append(" ").append(urlp.cwPage.sortOrder);
			PreparedStatement stmt = con.prepareStatement(sql.toString());
			int index = 1;
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			stmt.setString(index++, dbRegUser.USR_STATUS_OK);
			stmt.setBoolean(index++, true);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setBoolean(index++, true);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				TargetUser tu = new TargetUser();
				tu.usr_ent_id = rs.getLong("usr_ent_id");
				tu.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
				tu.usr_display_bil = rs.getString("usr_display_bil");
				tu.usr_usg_display_bil = rs.getString("usg_display_bil");
				tu.usr_ugr_display_bil = rs.getString("ugr_display_bil");
				vec.addElement(tu);
			}
			rs.close();
			stmt.close();
		}
		return vec;
	}
	
	public static String getTargetUserLstXML (Connection con, ItemTargetReqParam urlp) throws SQLException {
		StringBuffer xml = new StringBuffer(1024);
        if(urlp.cwPage.curPage == 0){
        	urlp.cwPage.curPage = 1;
        }
        if(urlp.cwPage.pageSize == 0){
        	urlp.cwPage.pageSize = 10;
        }
        Vector vec = getTargetUserLst(con, urlp);
        int start = urlp.cwPage.pageSize * (urlp.cwPage.curPage-1);
        int total_count = vec.size();
        int total_index = (total_count > start+urlp.cwPage.pageSize) ? (start+urlp.cwPage.pageSize) : total_count;
		xml.append("<page page_size=\"").append(urlp.cwPage.pageSize).append("\"")
			.append(" cur_page=\"").append(urlp.cwPage.curPage).append("\"")
			.append(" total_search=\"").append(total_count).append("\"")
			.append(" orderby=\"").append(urlp.cwPage.sortCol).append("\"")
			.append(" sortorder=\"").append(urlp.cwPage.sortOrder).append("\"/>");
		xml.append("<target_user_lst>");
		for(int i=start; i<total_index; i++) {
			TargetUser tu = (TargetUser)vec.elementAt(i);
			xml.append("<user id=\"").append(tu.usr_ent_id).append("\">")
				.append("<usr_ste_usr_id>").append(cwUtils.esc4XML(tu.usr_ste_usr_id)).append("</usr_ste_usr_id>")
				.append("<usr_display_bil>").append(cwUtils.esc4XML(tu.usr_display_bil)).append("</usr_display_bil>")
				.append("<usg_display_bil>").append(cwUtils.esc4XML(tu.usr_usg_display_bil)).append("</usg_display_bil>")
				.append("<ugr_display_bil>").append(cwUtils.esc4XML(tu.usr_ugr_display_bil)).append("</ugr_display_bil>")
				.append("</user>");
		}
		xml.append("</target_user_lst>");
		return xml.toString();
	}

	private static String getTargetGroupXML(Connection con, String group_id, long root_ent_id) throws SQLException {
		StringBuffer xml = new StringBuffer(512);
		String group_id_lst = "(" + group_id + ")";
		Hashtable has_group = dbUserGroup.getDisplayName(con, group_id_lst);
		Enumeration enu_group = has_group.keys();
		xml.append("<group_lst>");
		while(enu_group.hasMoreElements()) {
			String groupId = (String)enu_group.nextElement();
			xml.append("<group id=\"").append(groupId).append("\"")
				.append(" name=\"").append(cwUtils.esc4XML((String)has_group.get(groupId))).append("\"");
			if(String.valueOf(root_ent_id).equals(groupId)) {
				xml.append(" is_root_group=\"").append(true).append("\"");
			}
			xml.append("/>");
		}
		xml.append("</group_lst>");
		return xml.toString();
	}

	private static String getTargetGradeXML(Connection con, String grade_id, long root_ent_id) throws SQLException {
		StringBuffer xml = new StringBuffer(512);
		String grade_id_lst = "(" + grade_id + ")";
		Hashtable has_grade = DbUserGrade.getDisplayName(con, grade_id_lst);
		Enumeration enu_grade = has_grade.keys();
		xml.append("<grade_lst>");
		long grade_root = DbUserGrade.getGradeRoot(con, root_ent_id);
		while(enu_grade.hasMoreElements()) {
			String gradeId = (String)enu_grade.nextElement();
			xml.append("<grade id=\"").append(gradeId).append("\"")
			.append(" name=\"").append(cwUtils.esc4XML((String)has_grade.get(gradeId))).append("\"");
			if(String.valueOf(grade_root).equals(gradeId)) {
				xml.append(" is_root_grade=\"").append(true).append("\"");
			}
			xml.append("/>");
		}
		xml.append("</grade_lst>");
		return xml.toString();
	}
	
	private static String getTargetSkillXml(Connection con, String itr_upt_id) throws SQLException {
		StringBuffer xml = new StringBuffer(512);
		Hashtable has_skill=DbUserPosition.getDisplayNames(con, itr_upt_id);
		Enumeration enu_skill = has_skill.keys();
		xml.append("<skill_lst>");
//		long grade_root = 0;//DbUserGrade.getGradeRoot(con, root_ent_id);
		while(enu_skill.hasMoreElements()) {
			Long skillId = (Long)enu_skill.nextElement();
			xml.append("<skill id=\"").append(skillId).append("\"")
			.append(" name=\"").append(cwUtils.esc4XML((String)has_skill.get(skillId))).append("\"");
			xml.append("/>");
		}
		xml.append("</skill_lst>");
		return xml.toString();
	}

	public static boolean checkUpdTimestamp (Connection con, long rule_id, Timestamp timestamp) throws SQLException {
		boolean result = false;
		String sql = "select * from aeItemTargetRule where itr_id = ? and itr_update_timestamp = ?";
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, rule_id);
		stmt.setTimestamp(index++, timestamp);
		ResultSet rs = stmt.executeQuery();
		if(rs.next()) {
			result = true;
		}
        rs.close();
        stmt.close();
        return result;
	}
	
	public static boolean checkLastUpdTimestamp (Connection con, ItemTargetReqParam urlp) throws SQLException, cwSysMessage {
		boolean result = false;
		long itm_id = urlp.itm_id;
		String target_type = urlp.itm_target_type;
		aeItem itm = new aeItem();
		itm.itm_id = urlp.itm_id;
		itm.getItem(con);
		if(itm.itm_target_enrol_type != null) {
			if(itm.itm_target_enrol_type.equalsIgnoreCase(aeItem.ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER)) {
	            aeItemRelation aeIre = new aeItemRelation();
	            aeIre.ire_child_itm_id = urlp.itm_id;
	            aeIre.getParentItemId(con);
	            itm_id = aeIre.ire_parent_itm_id;
	            target_type = DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER;
			}
			String sql = "select itr_update_timestamp from aeItemTargetRule where itr_update_timestamp = (select max(itr_update_timestamp) from aeItemTargetRule where itr_itm_id = ? and itr_type = ?)";
			PreparedStatement stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, itm_id);
			stmt.setString(index++, target_type);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				if(urlp.last_upd_time != null && urlp.last_upd_time.equals(rs.getTimestamp(1))) {
					result = true;
				}
			} else if(urlp.last_upd_time == null){
				result = true;
			}
	        rs.close();
	        stmt.close();
		} else {
			result = true;
		}
		return result;
	}
	
	public static boolean checkTargetEnrolType (Connection con, ItemTargetReqParam urlp) throws SQLException, cwSysMessage {
		boolean result = false;
		aeItem itm = new aeItem();
		itm.itm_id = urlp.itm_id;
		itm.getItem(con);
		if(itm.itm_target_enrol_type != null) {
			if(itm.itm_target_enrol_type.equalsIgnoreCase(urlp.last_target_enrol_type)) {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}

	public static void chkPermission(Connection con, long itm_id, String rule_type) throws SQLException, cwSysMessage {
		if(rule_type != null) {
			boolean isRun = aeItem.getRunInd(con, itm_id);
			if(rule_type.equalsIgnoreCase(DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER)) {
				if(isRun) {
					throw new cwSysMessage("XMG002");
				}
			} else if(!isRun){
				throw new cwSysMessage("XMG002");
			}
		}
	}

	public static void changeTargetEnrolRule(Connection con, ItemTargetReqParam urlp, loginProfile prof) throws SQLException {
		aeItem itm = new aeItem();
		itm.itm_id = urlp.itm_id;
		itm.itm_target_enrol_type = urlp.target_enrol_type;
		itm.updTargetEnrolType(con, prof);

		if(urlp.is_del_all || (urlp.target_enrol_type != null  && urlp.target_enrol_type.equalsIgnoreCase(aeItem.ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER))) {
			DbItemTargetRuleDetail ird = new DbItemTargetRuleDetail();
			ird.ird_itm_id = urlp.itm_id;
			ird.ird_type = urlp.itm_target_type;
			ird.delByItem(con);
			
			DbItemTargetRule itr = new DbItemTargetRule();
			itr.itr_itm_id = urlp.itm_id;
			itr.itr_type = urlp.itm_target_type;
			itr.delByItem(con);
		}
	}
	
	
	public static Vector getTargetUserLst(Connection con, long itm_id)throws SQLException, cwSysMessage {
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.get(con);
        Vector vec = new Vector();
        Vector rules = new Vector();
        if (itm.itm_run_ind
                && itm.itm_target_enrol_type != null
                && itm.itm_target_enrol_type
                        .equalsIgnoreCase(aeItem.ITM_TARGET_ENROLL_TYPE_ASSIGNED)) {

            rules = DbItemTargetRule.get(con, itm_id,
                    DbItemTargetRuleDetail.IRD_TYPE_TARGET_ENROLLMENT);
        } else {
            if (itm.itm_run_ind) {
                aeItemRelation irl = new aeItemRelation();
                irl.ire_child_itm_id = itm_id;
                long parent_itm_id = irl.getParentItemId(con);
                if (parent_itm_id > 0) {
                    rules = DbItemTargetRule.get(con, parent_itm_id,
                            DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
                } else {
                    rules = DbItemTargetRule.get(con, itm_id,
                            DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
                }
            } else {
                rules = DbItemTargetRule.get(con, itm_id,
                        DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
            }

        }
        if (rules != null && rules.size() > 0) {
            StringBuffer sql = new StringBuffer(512);
            for (int i = 0; i < rules.size(); i++) {
                DbItemTargetRule itr = (DbItemTargetRule) rules.get(i);
                if (i > 0) {
                    sql.append(" union ");
                }

                sql
                        .append( " select distinct usr_ent_id,usr_email, usr_ste_usr_id, usr_display_bil, usg_display_bil, ugr_display_bil, upt_title")
                        .append(" from userGroup, userGrade, reguser")
                        .append(" inner join EntityRelation r1 on(r1.ern_ancestor_ent_id in (")
                        .append(itr.itr_group_id)
                        .append(") and r1.ern_type = ?)");
                        
                        
//                        .append(" inner join EntityRelation r2 on(r2.ern_ancestor_ent_id in (")
//                        .append(itr.itr_grade_id)
//                        .append(") and r2.ern_type = ? and r2.ern_child_ent_id=r1.ern_child_ent_id)");
                
                if(itr.itr_grade_id != null && !itr.itr_grade_id.equals("-1")) {
                    sql.append(" inner join EntityRelation r2 on(r2.ern_ancestor_ent_id in (")
                    .append(itr.itr_grade_id)
                    .append(") and r2.ern_type = ? and r2.ern_child_ent_id=r1.ern_child_ent_id)");
                } else {
                    sql.append(" inner join EntityRelation r2 on (r2.ern_child_ent_id = usr_ent_id and r2.ern_type = ?)");
                }
                
                
               
                        sql .append(" left join (select upr_usr_ent_id,upt_id,upt_title from UserPosition,UserPositionRelation where upt_id =upr_upt_id) Position on(upr_usr_ent_id = usr_ent_id)");
                
                sql.append(" where usr_status = ?")
                    .append(" and usr_ent_id = r1.ern_child_ent_id")
                    .append(" and usg_ent_id = (")
                    .append(" select ern_ancestor_ent_id ")
                    .append(" from EntityRelation")
                    .append(" where ern_parent_ind = ?")
                    .append(" and ern_child_ent_id = usr_ent_id")
                    .append(" and ern_type = ? )")
                    .append(" and ugr_ent_id = (")
                    .append(" select ern_ancestor_ent_id from EntityRelation")
                    .append(" where ern_parent_ind = ?").append(
                            " and ern_child_ent_id = usr_ent_id").append(
                            " and ern_type = ?)");
                if (itr.itr_upt_id != null&& !itr.itr_upt_id.equalsIgnoreCase("-1")&& itr.itr_upt_id.length() > 0) {
                    sql.append(" and upt_id in(")
                        .append(itr.itr_upt_id)
                        .append(")");
                }
                    
            }

            sql.append(" order by usr_ste_usr_id ");
            PreparedStatement stmt = con.prepareStatement(sql.toString());
            int index = 1;
            for (int i = 0; i < rules.size(); i++) {
                stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
                stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
                stmt.setString(index++, dbRegUser.USR_STATUS_OK);
                stmt.setBoolean(index++, true);
                stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
                stmt.setBoolean(index++, true);
                stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Vector tu = new Vector();
                tu.add(new Long(rs.getLong("usr_ent_id")));
                tu.add(rs.getString("usr_ste_usr_id"));
                tu.add(rs.getString("usr_display_bil"));
                tu.add(rs.getString("usr_email"));
                tu.add(rs.getString("usg_display_bil"));
                tu.add(rs.getString("ugr_display_bil"));
                tu.add(rs.getString("upt_title"));
                vec.addElement(tu);
            }
            rs.close();
            stmt.close();
        }
        return vec;
    }
	
	/*
	 * 把课程/班级的目标学员预加载到中间表中, 在发布课程 ,添加/修改/删除目标学员时调用
	 */
    public static void setTargetCache(Connection con, long itm_id, boolean del_first) throws SQLException, cwSysMessage {
        aeItem itm = new aeItem();
        itm.itm_id = itm_id;
        itm.get(con);
//        Vector vec = new Vector();
        Vector rules = new Vector();
//        HashMap<Long, DbItemTargetRule> allRules = new HashMap<Long, DbItemTargetRule>();
        
        //先删除原来的记录
        if(del_first){
            DbItemTargetLrnDetail.delByItem( con, itm_id) ;
        }
        
        //根据课程目标学员规则读取课程信息 damon
        //同一课程可能有不同的推荐维度
        if (itm.itm_run_ind && itm.itm_target_enrol_type != null && itm.itm_target_enrol_type.equalsIgnoreCase(aeItem.ITM_TARGET_ENROLL_TYPE_ASSIGNED)) {
            rules = DbItemTargetRule.get(con, itm_id, DbItemTargetRuleDetail.IRD_TYPE_TARGET_ENROLLMENT);
        } else {
            if (itm.itm_run_ind) {
                aeItemRelation irl = new aeItemRelation();
                irl.ire_child_itm_id = itm_id;
                long parent_itm_id = irl.getParentItemId(con);
                if (parent_itm_id > 0) {
                    rules = DbItemTargetRule.get(con, parent_itm_id, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
                } else {
                    rules = DbItemTargetRule.get(con, itm_id, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
                }
            } else {
                rules = DbItemTargetRule.get(con, itm_id, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
            }

        }

        //从岗位学习地图处读取课程信息 damon0331
        rules.addAll(DbItemTargetRule.getByPst(con, itm_id));
        
        //从职级学习地图处读取课程信息 后续待补充
        rules.addAll(DbItemTargetRule.getByProf(con, itm_id));
        
        if (rules != null && rules.size() > 0) {
//        if (allRules != null && allRules.size() > 0) {
            StringBuffer sql = new StringBuffer(512);
            
            /*
             * 取到该课程的所有规则,然后拼装SQL
             */
//            int i = 0;
            for (int i = 0; i < rules.size(); i++) {
//            for (DbItemTargetRule itr : allRules.values()) {
                DbItemTargetRule itr = (DbItemTargetRule) rules.get(i);
//            	System.out.println("读取到的规则信息："+JSON.toJSONString(itr));
                if (i > 0) {
                    sql.append(" union ");
                }

                if(itr.itr_id > 0){//来自于课程目标学员的规则
                	sql.append(" select usr_ent_id,itr_group_ind,itr_grade_ind,itr_position_ind,itr_compulsory_ind,");
                	sql.append("'" + itr.rulesource + "' roleresource")
                	.append(" from  aeitemtargetrule, reguser  ");
//                	.append(" where usr_status = '").append(dbRegUser.USR_STATUS_OK).append("' ");
                }else{//来自学习地图的规则
                	sql.append(" select usr_ent_id,-1 as itr_group_ind,-1 as itr_grade_ind,-1 as itr_position_ind,0 as itr_compulsory_ind,");
                	sql.append("'" + itr.rulesource + "' roleresource")
                	.append(" from reguser ");
                }
                sql.append(" where usr_status = '").append(dbRegUser.USR_STATUS_OK).append("' ");
                	
            	//根据用户组寻找用户
            	if(itr.itr_group_id != null && !itr.itr_group_id.equals("-1")){
            		sql.append(" and usr_ent_id in (").append(" select ern_child_ent_id ").append(
            				" from EntityRelation").append(" where ern_ancestor_ent_id in(").append(itr.itr_group_id)
            		.append(") and ern_type = '").append(dbEntityRelation.ERN_TYPE_USR_PARENT_USG).append("')");
            	}
            	
            	//根据职级寻找用户
            	if (itr.itr_grade_id != null && !itr.itr_grade_id.equals("-1")) {
            		sql.append(" and usr_ent_id in (").append(" select ern_child_ent_id ")
            		.append(" from EntityRelation").append(" where ern_ancestor_ent_id in(").append(itr.itr_grade_id)
            		.append(") and ern_type = '").append(dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR).append("')");
            	}
            	
            	//根据岗位寻找用户
            	if (itr.itr_upt_id != null && !itr.itr_upt_id.equalsIgnoreCase("-1") && itr.itr_upt_id.length() > 0) {
            		sql.append(" and usr_ent_id in (select upr_usr_ent_id from UserPositionRelation where upr_upt_id in(").append(itr.itr_upt_id).append("))");
            	}
            	
            	if(itr.itr_id > 0)
                	sql.append(" and itr_id = ").append(itr.itr_id).append(" ");
                	
//                i++;
                
            }
            sql.append(" order by usr_ent_id ");
            
//            System.out.println("要执行的sql："+sql.toString());

            PreparedStatement stmt = con.prepareStatement(sql.toString());
            
            ResultSet rs = stmt.executeQuery();
            Map<String,DbItemTargetLrnDetail> lst = new HashMap<String,DbItemTargetLrnDetail>();
            DbItemTargetLrnDetail detail = new DbItemTargetLrnDetail();
//            long pre_usr_ent_id = 0;
            long cur_usr_ent_id = 0;
            while (rs.next()) {
        		cur_usr_ent_id = rs.getLong("usr_ent_id");
        		
        		String key = cur_usr_ent_id+","+itm_id;
        		
        		detail = lst.get(key);
                
        		if(detail == null)
                	detail = new DbItemTargetLrnDetail();
                
                detail.itd_usr_ent_id = cur_usr_ent_id;
                detail.itd_itm_id = itm_id;
                
                
                String resource = rs.getString("roleresource");
                int group_ind = rs.getInt("itr_group_ind");
                int grade_ind = rs.getInt("itr_grade_ind");
                int position_ind = rs.getInt("itr_position_ind");
                
                if(resource.indexOf("1") > -1){
                	if(detail.itd_group_ind < 1){
                		detail.itd_group_ind = group_ind;
                	}
                	if(detail.itd_grade_ind < 1){
                		detail.itd_grade_ind = grade_ind;
                	}
                	if(detail.itd_position_ind < 1){
                		detail.itd_position_ind = position_ind;
                	}
                }
                if(resource.indexOf("2") > -1){
                	if(position_ind == 1)
                		detail.itd_fromposition = 2;
                	else{
                		detail.itd_position_ind = 1;
                		detail.itd_fromposition = 1;
                	}                		
                }

                if(resource.indexOf("3") > -1){
                	if(grade_ind == 1)
                		detail.itd_fromprofession = 2;
                	else{
                		detail.itd_grade_ind = 1;
                		detail.itd_fromprofession = 1;
                	}                		
                }

            	if(detail.itd_compulsory_ind < 1){
            		detail.itd_compulsory_ind = rs.getInt("itr_compulsory_ind");
            	}
                
//                pre_usr_ent_id = cur_usr_ent_id;
            	lst.put(key, detail);
            }
            rs.close();
            stmt.close();
            
//            System.out.println("要添加的课程id为："+itm_id+"，课程名称为："+itm.itm_title+"，记录集合为："+JSON.toJSONString(lst));
            
            /*
             * 插入中间表
             */
            detail.AddBatch( con, lst );
        }

    }
}

