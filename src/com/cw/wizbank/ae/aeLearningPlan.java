package com.cw.wizbank.ae;

import javax.servlet.http.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbEntity;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.db.DbIndustryCode;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.Course.CourseModuleParam;
import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.ae.db.view.ViewLearningSoln;
import com.cw.wizbank.ae.db.DbItemTargetRuleDetail;
import com.cw.wizbank.ae.db.DbItemType;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.accesscontrol.acSite;
// ADDED for BJMU
import com.cw.wizbank.db.view.ViewCourseModuleCriteria;
import com.cw.wizbank.db.view.ViewCfCertificate;
import com.cw.wizbank.db.view.ViewEntityToTree;
import com.cwn.wizbank.utils.CommonLog;

public class aeLearningPlan {
	private static Log logger  = LogFactory.getLog("COMMON_LOG");
    public static final String EFF_DATE_UNLIMITED = "UNLIMITED";
    private static final boolean debug = false;

	public static final String SESS_LEARNING_PLAN_HASH = "SESS_LEARNING_PLAN_HASH";
	public static final String LRNPLAN_A_TARGET = "A_Target";	// 适合我的培训
	public static final String LRNPLAN_B_SOLN = "B_Soln";		// 我的培训计划
	public static final String LRNPLAN_REMARK_HAS_COMPLETED = "has_completed";
	public static final String LRNPLAN_REMARK_HAS_ENROLL = "has_enroll";
	public static final String LRNPLAN_REMARK_CAN_NOT_ENROLL = "can_not_enroll";
	public static final String LRNPLAN_REMARK_OPEN_ENROLL = "open_enroll";
	public static final String LRNPLAN_REMARK_NEXT_ENROLL = "next_enroll";

	public  static  final String RECOMMENDED="RECOMMENDED";// 适合我的培训
	public static  final String MYPLAN="MYPLAN";			// 我的培训计划
	public static  final String RECOMMENDED_MYPLAN="RECOMMENDED_MYPLAN";
	public static String JSON_FORMAT="JSON";
	private static String XML_FORMAT="XML";

	//过滤学习计划参数
	public static final String NULL_NOTENROLL="NOTENROLL";
	public static final String I_PROGRESS="PROGRESS";
	public static final String C_ATTEND="ATTEND";
	public static final String F_INCOMPLETE="INCOMPLETE";
	public static final String W_WITHDRAWN="WITHDRAWN";
	public static final String NOT_SET="NOT_SET";
	public static final String SevenDAY="LAST_7_DAY";
	public static final String OneMONTH="LAST_1_MONTH";

    public static class myLearningPlan {
		String type;
    	String pitm_type;//
    	long pitm_id;//
    	String pitm_title;//
    	public long pitm_tcr_id;//
    	public String pitm_tcr_title;//
    	String status;//
    	Timestamp appn_start_Timestamp;//
    	Timestamp appn_end_Timestamp;//
    	Timestamp maxEnd_timestamp;//
		Timestamp minStart_timestamp = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);//
    	public  Timestamp att_timestamp;//
    	public  String itm_retake_ind;//
		public String ats_cov_status;//
		public Timestamp lsn_start_datetime;//
	    Timestamp lsn_end_datetime;//

    	String app_status;
    	String ats_type;
    	long app_id;
    	long itm_create_run_ind;
		long itm_can_not_enroll_ind;
    	long citm_id;
    	long app_tkh_id;
		long cos_res_id;
		String pitm_dummy_type;
		String pitm_icon;

		String pitm_desc;
		double pitm_comment_avg_score;
		HashMap recent_start_classes;

		//whether the course has been added my learning plan
		boolean plan_ind;

		boolean pitm_ref_ind;	// 是否为参考
		boolean pitm_exam_ind;

		public String getPitm_icon() {
			return pitm_icon;
		}
		public void setPitm_icon(String pitm_icon) {
			this.pitm_icon = pitm_icon;
		}
		public String getPitm_dummy_type() {
			return pitm_dummy_type;
		}
		public void setPitm_dummy_type(String pitm_dummy_type) {
			this.pitm_dummy_type = pitm_dummy_type;
		}
		public String getLabel_pitm_type() {
			return label_pitm_type;
		}
		public void setLabel_pitm_type(String label_pitm_type) {
			this.label_pitm_type = label_pitm_type;
		}
		String label_pitm_type;

		public long getCos_res_id() {
			return cos_res_id;
		}
		public void setCos_res_id(long cos_res_id) {
			this.cos_res_id = cos_res_id;
		}
		public long getCitm_id() {
			return citm_id;
		}
		public void setCitm_id(long citm_id) {
			this.citm_id = citm_id;
		}
		public long getApp_tkh_id() {
			return app_tkh_id;
		}
		public void setApp_tkh_id(long app_tkh_id) {
			this.app_tkh_id = app_tkh_id;
		}
		Timestamp eff_end;

		public long itm_tcr_id;
		public String itm_tcr_title;


	    public String getPitm_type() {
			return pitm_type;
		}
		public void setPitm_type(String pitm_type) {
			this.pitm_type = pitm_type;
		}
		public long getPitm_id() {
			return pitm_id;
		}
		public void setPitm_id(long pitm_id) {
			this.pitm_id = pitm_id;
		}
		public String getPitm_title() {
			return pitm_title;
		}
		public void setPitm_title(String pitm_title) {
			this.pitm_title = pitm_title;
		}
		public long getPitm_tcr_id() {
			return pitm_tcr_id;
		}
		public void setPitm_tcr_id(long pitm_tcr_id) {
			this.pitm_tcr_id = pitm_tcr_id;
		}
		public String getPitm_tcr_title() {
			return pitm_tcr_title;
		}
		public void setPitm_tcr_title(String pitm_tcr_title) {
			this.pitm_tcr_title = pitm_tcr_title;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Timestamp getAppn_start_Timestamp() {
			return appn_start_Timestamp;
		}
		public void setAppn_start_Timestamp(Timestamp appn_start_Timestamp) {
			this.appn_start_Timestamp = appn_start_Timestamp;
		}
		public Timestamp getAppn_end_Timestamp() {
			return appn_end_Timestamp;
		}
		public void setAppn_end_Timestamp(Timestamp appn_end_Timestamp) {
			this.appn_end_Timestamp = appn_end_Timestamp;
		}
		public Timestamp getMaxEnd_timestamp() {
			return maxEnd_timestamp;
		}
		public void setMaxEnd_timestamp(Timestamp maxEnd_timestamp) {
			this.maxEnd_timestamp = maxEnd_timestamp;
		}
		public Timestamp getMinStart_timestamp() {
			return minStart_timestamp;
		}
		public void setMinStart_timestamp(Timestamp minStart_timestamp) {
			this.minStart_timestamp = minStart_timestamp;
		}
		public Timestamp getAtt_timestamp() {
			return att_timestamp;
		}
		public void setAtt_timestamp(Timestamp att_timestamp) {
			this.att_timestamp = att_timestamp;
		}
		public String getItm_retake_ind() {
			return itm_retake_ind;
		}
		public void setItm_retake_ind(String itm_retake_ind) {
			this.itm_retake_ind = itm_retake_ind;
		}
		public String getAts_cov_status() {
			return ats_cov_status;
		}
		public void setAts_cov_status(String ats_cov_status) {
			this.ats_cov_status = ats_cov_status;
		}
		public Timestamp getLsn_start_datetime() {
			return lsn_start_datetime;
		}
		public void setLsn_start_datetime(Timestamp lsn_start_datetime) {
			this.lsn_start_datetime = lsn_start_datetime;
		}
		public Timestamp getLsn_end_datetime() {
			return lsn_end_datetime;
		}
		public void setLsn_end_datetime(Timestamp lsn_end_datetime) {
			this.lsn_end_datetime= lsn_end_datetime;
		}
		public long getItm_tcr_id() {
			return itm_tcr_id;
		}
		public void setItm_tcr_id(long itm_tcr_id) {
			this.itm_tcr_id = itm_tcr_id;
		}
		public String getItm_tcr_title() {
			return itm_tcr_title;
		}
		public void setItm_tcr_title(String itm_tcr_title) {
			this.itm_tcr_title = itm_tcr_title;
		}
		public boolean isPlan_ind() {
			return plan_ind;
		}
		public void setPlan_ind(boolean plan_ind) {
			this.plan_ind = plan_ind;
		}
		public String getPitm_desc() {
			return pitm_desc;
		}
		public void setPitm_desc(String pitm_desc) {
			this.pitm_desc = pitm_desc;
		}
		public double getPitm_comment_avg_score() {
			return pitm_comment_avg_score;
		}
		public void setPitm_comment_avg_score(double pitm_comment_avg_score) {
			this.pitm_comment_avg_score = pitm_comment_avg_score;
		}
		public HashMap getRecent_start_classes() {
			return recent_start_classes;
		}
		public void setRecent_start_classes(HashMap recent_start_classes) {
			this.recent_start_classes = recent_start_classes;
		}
		public boolean isPitm_ref_ind() {
			return pitm_ref_ind;
		}
		public void setPitm_ref_ind(boolean pitm_ref_ind) {
			this.pitm_ref_ind = pitm_ref_ind;
		}
		public boolean isPitm_exam_ind() {
			return pitm_exam_ind;
		}
		public void setPitm_exam_ind(boolean pitm_exam_ind) {
			this.pitm_exam_ind = pitm_exam_ind;
		}
	}

    public String myLearningPlan(Connection con, HttpSession sess, loginProfile prof, long usr_ent_id, Vector item_type, long pgm_id, long pgm_run_id, String[] targeted_item_apply_method_lst)
        throws SQLException, qdbException, cwException, cwSysMessage {
        long grade_ent_id = 0;

        Hashtable target_soln = null; //store courses targeted
        Hashtable other_soln = null; //store courses planned
        Vector itm_lst = new Vector();

        if (item_type == null || item_type.size() == 0) {
            item_type = DbItemType.getApplicableItemType(con, prof.root_ent_id);
        }

        target_soln = new Hashtable();
        other_soln = new Hashtable();

        itm_lst = new Vector();

        Hashtable app_info = ViewLearningSoln.getApplicationAndAttendance(con, usr_ent_id);

        Enumeration enum_app_info = app_info.keys();

        while (enum_app_info.hasMoreElements()) {
            Long itm_id = (Long)enum_app_info.nextElement();

            if (!itm_lst.contains(itm_id)) {
                itm_lst.addElement(itm_id);
            }
        }

        Enumeration enum_target_soln = target_soln.keys();
        Enumeration enum_other_soln = other_soln.keys();

        // get itm_id according to period_id
        Hashtable temp_info = new Hashtable();
        Vector temp_vector = new Vector();
        Vector temp_lst = new Vector();

        Enumeration enum_temp_soln = target_soln.keys();

        while (enum_temp_soln.hasMoreElements()) {
            temp_lst.addElement((Long)enum_temp_soln.nextElement());
        }

        ViewLearningSoln.getItemInfo(con, temp_lst, temp_info, temp_vector);

        if (grade_ent_id != 0) {
            // get my period id
            String period_xml = ViewLearningSoln.getPeriodXML(con, prof.root_ent_id);

            if (period_xml != null && period_xml.length() > 0) {
                aeLearningSoln soln = new aeLearningSoln();
                Vector grade_lst = new Vector();
                grade_lst.addElement(new Long(grade_ent_id));
                Vector my_period_lst = soln.getPeriodList(grade_lst, period_xml);

                while (enum_target_soln.hasMoreElements()) {
                    Long itm_id = (Long)enum_target_soln.nextElement();
                    Vector period_lst = soln.getPeriodList((Vector)target_soln.get(itm_id), period_xml);
                    aeItem item = (aeItem)temp_info.get(itm_id);

                    for (int i=0; i<period_lst.size(); i++) {
                        Long period_id = (Long)period_lst.elementAt(i);
                        if (! period_id.equals(new Long(0)) &&
                            my_period_lst.contains(period_id) &&
                            item.itm_apply_method != null && item.itm_apply_method.equals(aeItem.ITM_APPLY_METHOD_COMPULSORY)) {
                            if (!itm_lst.contains(itm_id)) {
                                itm_lst.addElement(itm_id);
                            }

                            break;
                        }
                    }
                }

                while (enum_other_soln.hasMoreElements()) {
                    Long itm_id = (Long)enum_other_soln.nextElement();
                    Long period_id = (Long)other_soln.get(itm_id);

                    if (!itm_lst.contains(itm_id) && my_period_lst.contains(period_id)) {
                        itm_lst.addElement(itm_id);
                    }
                }
            }
        } else {
            while (enum_target_soln.hasMoreElements()) {
                Long itm_id = (Long)enum_target_soln.nextElement();
                aeItem item = (aeItem)temp_info.get(itm_id);

                if (item.itm_apply_method != null && item.itm_apply_method.equals(aeItem.ITM_APPLY_METHOD_COMPULSORY) && !itm_lst.contains(itm_id)) {
                    itm_lst.addElement(itm_id);
                }
            }

            while (enum_other_soln.hasMoreElements()) {
                Long itm_id = (Long)enum_other_soln.nextElement();

                if (!itm_lst.contains(itm_id)) {
                    itm_lst.addElement(itm_id);
                }
            }
        }

        // generate itm_lst and get itm info
        Hashtable item_info = new Hashtable();
        Vector item_vector = new Vector();
        ViewLearningSoln.getItemInfo(con, itm_lst, item_info, item_vector);
        Hashtable item_evaluation = dbCourseEvaluation.getCourseEvaluation4Plan(con, usr_ent_id, itm_lst);
        Hashtable xml_hash = new Hashtable();
        Timestamp cur_time = cwSQL.getTime(con);

        for (int i=0; i<item_type.size(); i++) {
            StringBuffer xml = new StringBuffer();
            String type = (String)item_type.elementAt(i);
            //add the "" to one type of item
            xml_hash.put(type.toUpperCase(), xml);
        }

        AcItem acitm = new AcItem(con);
        boolean hasPreApprReadPrivilege = true;
        boolean hasOffReadPrivilege = true;

        for (int i=0; i<item_vector.size(); i++) {
            Long itm_id = (Long)item_vector.elementAt(i);
            String is_targeted = "NO";
            aeItem item = (aeItem)item_info.get(itm_id);

            if (xml_hash.containsKey(item.itm_type.toUpperCase()) &&
                (hasOffReadPrivilege || item.itm_status.equals(aeItem.ITM_STATUS_ON)) &&
                (hasPreApprReadPrivilege || item.itm_life_status == null) &&
                !item.itm_run_ind) {
                StringBuffer xml_temp = new StringBuffer();

                if (target_soln.containsKey(itm_id)) {
                    is_targeted = "YES";
                }

				xml_temp.append(toItemXML(con, prof, item, is_targeted, (Vector)app_info.get(itm_id), item_evaluation));

                Vector app_lst = (Vector)app_info.get(new Long(item.itm_id));

                    //Tim start

                 /*
                    boolean launch_ind = false;
                    Hashtable htInfo = new Hashtable();
                    Timestamp createdTimestamp = cwSQL.getTime(con);
                    if (app_lst != null && app_lst.size() > 0) {
                        System.out.println("createdTimestamp" + createdTimestamp);
                        htInfo = (Hashtable)app_lst.elementAt(0);
                        createdTimestamp = (Timestamp)htInfo.get(ViewLearningSoln.ATTN_CREATE_TIMESTAMP);
                    }
                    if(createdTimestamp == null)
                        createdTimestamp = cwSQL.getTime(con);

                        if(item.itm_content_eff_duration == 0 &&
                                item.itm_content_eff_start_datetime != null &&
                                item.itm_content_eff_start_datetime.before(cur_time) &&
                                item.itm_content_eff_end_datetime != null &&
                                item.itm_content_eff_end_datetime.after(cur_time)) {
                                launch_ind = true;
                            }
                            if(item.itm_content_eff_duration !=0){
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(createdTimestamp);
                                calendar.add(Calendar.DAY_OF_MONTH, item.itm_content_eff_duration) ;
                                if(calendar.getTime().after(cur_time))
                                launch_ind = true;
                            }
 	                    if(item.itm_content_eff_duration ==0 && item.itm_content_eff_start_datetime == null && item.itm_content_eff_end_datetime == null){
                                launch_ind = true;
                            }
	           */
	           	//boolean launch_ind = true;
		        Hashtable htInfo = new Hashtable();
		        Timestamp createdTimestamp = cur_time;
		        Timestamp itm_content_eff_start_datetime= cur_time;
		        Timestamp itm_content_eff_end_datetime = cur_time;
		        int itm_content_eff_duration = 0;
		        if (app_lst != null && app_lst.size() > 0) {
		            htInfo = (Hashtable)app_lst.elementAt(0);
		            if(htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_START_DATETIME) != null)
		            	itm_content_eff_start_datetime = (Timestamp)htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_START_DATETIME);
		            if(htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_END_DATETIME) != null)
		            	itm_content_eff_end_datetime = (Timestamp)htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_END_DATETIME);
		            itm_content_eff_duration = ((Integer)htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_DURATION)).intValue();
		            if(htInfo.get(ViewLearningSoln.ATTN_CREATE_TIMESTAMP) != null)
		            	createdTimestamp = (Timestamp)htInfo.get(ViewLearningSoln.ATTN_CREATE_TIMESTAMP);
		        }

		        // Tim add
		        //set a flag for display 'launch button'
		        /*
		        if(itm_content_eff_duration == 0 &&
		            itm_content_eff_start_datetime != null &&
		            itm_content_eff_start_datetime.before(cur_time) &&
		            itm_content_eff_end_datetime != null &&
		            itm_content_eff_end_datetime.after(cur_time)) {
		            launch_ind = true;
		        }
		        if(itm_content_eff_duration !=0){
		            Calendar calendar = new GregorianCalendar();
		            calendar.setTime(createdTimestamp);
		            calendar.add(Calendar.DAY_OF_MONTH, itm_content_eff_duration) ;
		            if(calendar.getTime().after(cur_time))
		               launch_ind = true;
		        }
		 	   if(itm_content_eff_duration ==0 && itm_content_eff_start_datetime == cur_time && itm_content_eff_end_datetime == cur_time){
		            launch_ind = true;
		        }
               if(launch_ind == true && !item.hasToc(con)) {
				    launch_ind = false;
                }
	            */


                    //Tim end
                if(xml_temp != null && xml_temp.length() > 0) {
                    if (app_lst != null && app_lst.size() != 0) {
                            StringBuffer xml = (StringBuffer)xml_hash.get(item.itm_type.toUpperCase());
                            xml.append(xml_temp.toString());
                            xml_hash.put(item.itm_type.toUpperCase(), xml);
                     } else {
                         if (!aeApplication.hasCompletedCourse(con, usr_ent_id, item.itm_id)) {
                             StringBuffer xml = (StringBuffer)xml_hash.get(item.itm_type.toUpperCase());
                             xml.append(xml_temp.toString());
                             xml_hash.put(item.itm_type.toUpperCase(), xml);
                         }
                     }
                }
            }
        }

        StringBuffer result = new StringBuffer();

        result.append("<learning_plan>").append(cwUtils.NEWL);

        dbRegUser user = new dbRegUser();
        user.usr_ent_id = usr_ent_id;
        user.get(con);

        result.append("<student id=\"").append(user.usr_ste_usr_id).append("\" last_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(user.usr_last_name_bil))).append("\" first_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(user.usr_first_name_bil))).append("\" display_bil=\"").append(cwUtils.esc4XML(aeUtils.escNull(user.usr_display_bil))).append("\" ent_id=\"").append(usr_ent_id).append("\"/>").append(cwUtils.NEWL);
        result.append("<cur_time>").append(cur_time).append("</cur_time>");
        result.append(aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id));
        result.append("<attendance_status_list>").append(cwUtils.NEWL);
        result.append(aeAttendanceStatus.statusAsXML(con, prof.root_ent_id));
        result.append("</attendance_status_list>").append(cwUtils.NEWL);
        // added for certificate
        ViewCfCertificate certification = new ViewCfCertificate(con);
        result.append(certification.getCertificationLstAsXML());

        for (int i=0; i<item_type.size(); i++) {
            String type = (String)item_type.elementAt(i);
            StringBuffer temp_xml = (StringBuffer)xml_hash.get(type.toUpperCase());
            result.append("<item_list type=\"").append(type.toUpperCase()).append("\">").append(cwUtils.NEWL);
            result.append(temp_xml.toString());
            result.append("</item_list>").append(cwUtils.NEWL);
        }

        result.append("</learning_plan>");


        return result.toString();
    }

    public String getMyCoursesAsXML(Connection con, long root_ent_id, long usr_ent_id, Vector item_type)
	throws SQLException, qdbException, cwException, cwSysMessage {
		if (item_type == null || item_type.size() == 0) {
			item_type = DbItemType.getApplicableItemType(con, root_ent_id);
		}
    	StringBuffer result = new StringBuffer();
		Timestamp cur_time = cwSQL.getTime(con);
		Vector mycourse = ViewLearningSoln.getMyCourses(con, usr_ent_id, item_type);
		result.append("<learning_plan>");

		result.append("<cur_time>").append(cur_time).append("</cur_time>");
		result.append(aeItem.getAllItemTypeTitleInOrg(con, root_ent_id));
		result.append("<attendance_status_list>");
		result.append(aeAttendanceStatus.statusAsXML(con, root_ent_id));
		result.append("</attendance_status_list>");
		if (item_type != null) {
			for (int i=0; i<item_type.size(); i++) {
				String type = (String)item_type.elementAt(i);
				result.append("<item_list type=\"").append(type.toUpperCase()).append("\">");
				result.append(getMyCoursesDetailAsXML(con, mycourse, type, cur_time));
				result.append("</item_list>");
			}
		}

		result.append("</learning_plan>");
    	return result.toString();
    }
    

    private static List getLearningPlanByCmd(Connection con, boolean isSelfInitiatedEnabled, long root_ent_id, long usr_ent_id, String groupAncester,
    					String gradeAncester, Timestamp cur_time,String soln_type,boolean isPending_Plan, String lang, String itmDir, CourseModuleParam param)
    	throws SQLException, cwException {

		List lst = new ArrayList();

		StringBuffer sql = new StringBuffer();
		boolean hasUsrGroup = false;
		boolean hasUsrGrade = false;
		String[] acsite_targeted_entity = ViewEntityToTree.getTargetEntity(con, root_ent_id);
		int usr_dimension = getUsrDimensionByUsrEntId(con, usr_ent_id);

		if (acsite_targeted_entity.length <= usr_dimension || isSelfInitiatedEnabled) {

			sql.append("select distinct itm.type type, parent.itm_type pitm_type,parent.itm_id pitm_id, parent.itm_icon pitm_icon, parent.itm_title pitm_title, parent.itm_type pitm_type,att_update_timestamp, parent.itm_retake_ind,ats_cov_status, app_tkh_id," );

			if(soln_type.equalsIgnoreCase(MYPLAN) || soln_type.equalsIgnoreCase(RECOMMENDED_MYPLAN))
			{
				sql.append("lsn_start_datetime,lsn_end_datetime,");
			}

			sql.append(cwSQL.replaceNull("child.itm_appn_start_datetime", "parent.itm_appn_start_datetime")).append(" appn_start, ")
			   .append(cwSQL.replaceNull("child.itm_appn_end_datetime", "parent.itm_appn_end_datetime")).append(" appn_end, ")
			   .append(cwSQL.replaceNull("child.itm_content_eff_end_datetime", "parent.itm_content_eff_end_datetime")).append(" eff_end,")
			   .append(" app_status, ").append(cwSQL.replaceNull("app_id", "-1")).append(" app_id, ats_type")
			   .append(" ,parent.itm_tcr_id pitm_tcr_id, child.itm_tcr_id itm_tcr_id, ")
			   .append(cwSQL.replaceNull("ptc.tcr_title", "tc.tcr_title"))
			   .append(" pitm_tcr_title, tc.tcr_title itm_tcr_title, ")
			   .append(" app_itm_id citm_id, app_tkh_id, cos_res_id, ")
			   .append(" parent.itm_desc pitm_desc, parent.itm_comment_avg_score pitm_comment_avg_score, ")
			   .append(" parent.itm_create_run_ind, parent.itm_exam_ind, parent.itm_blend_ind, parent.itm_ref_ind ")	// 是否可以创建班级、是否是考试、是否是混合、是否是参考
			   .append(" from (");

			if (acsite_targeted_entity.length <= usr_dimension) {
				for (int i=0; i<acsite_targeted_entity.length; i++) {
					if (acsite_targeted_entity[i].equals("user_group")) {
						hasUsrGroup = true;
					}
					if (acsite_targeted_entity[i].equals("grade")) {
						hasUsrGrade = true;
					}
				} // end for
				if(soln_type.equalsIgnoreCase(RECOMMENDED) || soln_type.equalsIgnoreCase(RECOMMENDED_MYPLAN))
				{
					if (hasUsrGroup) {
						sql.append(" Select '")
							.append(LRNPLAN_A_TARGET)
							.append("' as type,ird_itm_id as itm_id from aeItemTargetRuleDetail,aeItem ")
							.append(" where itm_id = ird_itm_id and itm_access_type is not null ");
						sql.append(" and ird_type = ? and ird_group_id in (").append(groupAncester).append(")");
					}

					if (hasUsrGrade) {
						sql.append(" and ird_grade_id in (").append(gradeAncester).append(")");
					}

					//target post or target skill
//					sql.append(" and (ird_upt_id = -1 or ird_upt_id in (");
//					sql.append(" select uss_ske_id from RegUserSkillSet where uss_ent_id = ?");
//					sql.append(" union");
//					sql.append(" select ske_id From cmSkillSet");
//					sql.append(" inner join cmSkillSetCoverage on (ssc_sks_skb_id = sks_skb_id)");
//					sql.append(" inner join cmSkillBase on (ssc_skb_id = skb_id and skb_delete_timestamp is null)");
//					sql.append(" inner join cmSkillEntity on (skb_ske_id = ske_id)");
//					sql.append(" inner join RegUserSkillSet on (uss_ske_id = sks_ske_id and uss_ent_id = ?)");
//					sql.append(" ))");

					//target post or target skill
					sql.append(" and (ird_upt_id = -1 or ird_upt_id = ?)");

					if(isPending_Plan)	// 若要查询待计划的课程
					{
						sql.append("and not exists ( select * from aeLearningSoln where lsn_itm_id = ird_itm_id and lsn_ent_id = ?) ")
							.append("and not exists ( select * from aeApplication  where app_ent_id = ? ")
							.append("and (app_itm_id = ird_itm_id or  app_itm_id in (select ire_child_itm_id from aeItemRelation where ire_parent_itm_id = ird_itm_id))) ");
					}
				}
			}

			if(soln_type.equalsIgnoreCase(RECOMMENDED_MYPLAN))
			{
				if (acsite_targeted_entity.length <= usr_dimension && isSelfInitiatedEnabled) {
					sql.append(" union ");
				}

			}
			if(soln_type.equalsIgnoreCase(MYPLAN))	// 若要查询我的培训计划
			{

				if (isSelfInitiatedEnabled&&acsite_targeted_entity.length <= usr_dimension) {
					sql.append("select '").append(LRNPLAN_B_SOLN).append("' as type,lsn_itm_id as itm_id,lsn_start_datetime,lsn_end_datetime from aeLearningSoln where lsn_status_ind = 1 and lsn_ent_id = ?");
				}
			}
			sql.append(" ) itm ");
			sql.append(" inner join aeItem parent on (itm.itm_id = parent.itm_id and parent.itm_status = ?)")
			   .append(" left join aeItemRelation on (parent.itm_id = ire_parent_itm_id)")
			   .append(" left join aeItem child on (ire_child_itm_id = child.itm_id)")
			   .append(" left join aeApplication on (app_ent_id = ? and (parent.itm_id = app_itm_id or child.itm_id = app_itm_id) and app_status in (?, ?, ?))")
			   .append(" left join Course on (app_itm_id = cos_itm_id)")
			   .append(" left join aeAttendance on (app_id = att_app_id)")
			   .append(" left join aeAttendanceStatus on (att_ats_id = ats_id)")
			   .append(" LEFT JOIN tcTrainingCenter ptc ON parent.itm_tcr_id = ptc.tcr_id ")
    		   .append(" LEFT JOIN tcTrainingCenter tc ON child.itm_tcr_id = tc.tcr_id ")
			   .append(" where (child.itm_status is null or child.itm_status = ?)");
			if(param != null && param.isMobile()){ // 添加移动端
				sql.append("and parent.itm_type = 'MOBILE' ");
				if(param.getComment() != null && !"".equals(param.getComment().trim())){
					sql.append("and parent.itm_title like ? ");
				}
				if(param.getTnd_id() > 0){
					sql.append("and parent.itm_id in (SELECT chile.tnd_itm_id FROM aeTreeNode chile where chile.tnd_parent_tnd_id = ? and chile.tnd_type='ITEM') ");
				}else{
					sql.append("and itm.itm_id in (SELECT chile.tnd_itm_id FROM aeTreeNode chile ");
					sql.append("inner join (select tnd_id from aeTreeNode WHERE tnd_parent_tnd_id IN ");
					sql.append("(SELECT tnd.tnd_id FROM aeTreeNode tnd INNER JOIN aeCatalog cat ");
					sql.append("ON (cat.cat_id = tnd.tnd_cat_id) WHERE tnd.tnd_parent_tnd_id IS NULL ");
					sql.append("AND cat.cat_mobile_ind = 1 AND cat.cat_status='ON') ");
					sql.append("AND tnd_type = 'NORMAL' ) parent on parent.tnd_id = chile.tnd_parent_tnd_id and chile.tnd_type='ITEM') ");
				}
			}
			sql.append(" order by itm.type, parent.itm_type desc, parent.itm_id asc, app_id desc");
			PreparedStatement stmt;
			ResultSet rs;

			stmt = con.prepareStatement(sql.toString());

			int index = 1;
			if(soln_type.equalsIgnoreCase(RECOMMENDED) || soln_type.equalsIgnoreCase(RECOMMENDED_MYPLAN))
			{
				if (hasUsrGroup) {
					stmt.setString(index++, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
					if(isPending_Plan)
					{
						stmt.setLong(index++, usr_ent_id);
//						stmt.setLong(index++, usr_ent_id);

					}
				}
				stmt.setLong(index++, usr_ent_id);
				stmt.setLong(index++, usr_ent_id);
			}

			if(soln_type.equalsIgnoreCase(MYPLAN) || soln_type.equalsIgnoreCase(RECOMMENDED_MYPLAN))
			{
				if (isSelfInitiatedEnabled) {
					stmt.setLong(index++, usr_ent_id);
				}
			}

			stmt.setString(index++, aeItem.ITM_STATUS_ON);
			stmt.setLong(index++, usr_ent_id);
			stmt.setString(index++, aeApplication.WAITING);
			stmt.setString(index++, aeApplication.PENDING);
			stmt.setString(index++, aeApplication.ADMITTED);
			stmt.setString(index++, aeItem.ITM_STATUS_ON);
			if(param != null && param.isMobile()){
				if(param.getComment() != null && !"".equals(param.getComment().trim())){
					stmt.setObject(index++, "%" + param.getComment() + "%");
				}
				if(param.getTnd_id() > 0){
					stmt.setLong(index++, param.getTnd_id());
				}
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				myLearningPlan myLrnplan = new myLearningPlan();
				myLrnplan.type = rs.getString("type");

				myLrnplan.setPitm_type(rs.getString("pitm_type"));

				boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
				boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
				boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
				boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");

				String dummyType = aeItemDummyType.getDummyItemType(rs.getString("pitm_type"), itm_blend_ind, itm_exam_ind, itm_ref_ind);
				myLrnplan.setPitm_dummy_type(dummyType);
				myLrnplan.setLabel_pitm_type(aeItemDummyType.getItemLabelByDummyType(lang, dummyType));

				if (itm_create_run_ind) {
					if (!itm_blend_ind) {	// 若是离线课程或离线考试，获取其对应的班级的开课时间
						myLrnplan.setRecent_start_classes((new Course()).getStartDateOfClass(con, rs.getLong("pitm_id")));
					} else {	// 若是混合课程或混合考试，获取其对应的班级的开课时间
						myLrnplan.setRecent_start_classes((new Course()).getStartDateofBlendItem(con, rs.getLong("pitm_id")));
					}
				}

				myLrnplan.setPitm_id(rs.getLong("pitm_id"));
				myLrnplan.setPitm_ref_ind(itm_ref_ind);	// 设置是否是参考
				myLrnplan.setPitm_exam_ind(itm_exam_ind);

				String pItmIcon = rs.getString("pitm_icon");
				String itmIconPath = Course.getItmIconPath(itmDir, rs.getLong("pitm_id"), pItmIcon);
				myLrnplan.setPitm_icon(itmIconPath);

				myLrnplan.setAppn_end_Timestamp(rs.getTimestamp("appn_end"));
				myLrnplan.setAppn_start_Timestamp(rs.getTimestamp("appn_start"));

				myLrnplan.setItm_tcr_id(rs.getLong("itm_tcr_id"));
				myLrnplan.setItm_tcr_title(rs.getString("itm_tcr_title"));
				myLrnplan.setAppn_end_Timestamp(rs.getTimestamp("appn_end"));
				myLrnplan.setAppn_start_Timestamp(rs.getTimestamp("appn_start"));

//				myLrnplan.setStatus(rs.getString("app_status"));
				myLrnplan.setPitm_title(rs.getString("pitm_title"));
				myLrnplan.setPitm_tcr_title(rs.getString("pitm_tcr_title"));
				myLrnplan.eff_end = rs.getTimestamp("eff_end");
				myLrnplan.app_status = rs.getString("app_status");	// 审批状态
				myLrnplan.app_id = rs.getLong("app_id");
				myLrnplan.ats_type = rs.getString("ats_type");
				myLrnplan.setCitm_id(rs.getLong("citm_id"));
				myLrnplan.setApp_tkh_id(rs.getLong("app_tkh_id"));

				myLrnplan.setPitm_desc(rs.getString("pitm_desc"));
				myLrnplan.setPitm_comment_avg_score(rs.getDouble("pitm_comment_avg_score"));

				if(rs.getString("ats_cov_status") != null && !rs.getString("ats_cov_status").equalsIgnoreCase(""))
				{
					myLrnplan.setAts_cov_status(rs.getString("ats_cov_status"));
				}

				if(rs.getString("att_update_timestamp")!=null)
					myLrnplan.setAtt_timestamp(rs.getTimestamp("att_update_timestamp"));
				else
					myLrnplan.setAtt_timestamp(null);
				myLrnplan.setItm_retake_ind(rs.getString("itm_retake_ind"));
				if(soln_type.equalsIgnoreCase(MYPLAN)||soln_type.equalsIgnoreCase(RECOMMENDED_MYPLAN))
				{
					myLrnplan.setLsn_end_datetime(rs.getTimestamp("lsn_end_datetime"));
					myLrnplan.setLsn_start_datetime(rs.getTimestamp("lsn_start_datetime"));
				}
				lst.add(myLrnplan);
			}
			stmt.close();
		}
		return lst;
	}
    public static Vector getLearningPlan(Connection con,
			boolean isSelfInitiatedEnabled, long root_ent_id, long usr_ent_id,
			String usr_display_bil, String groupAncester, String gradeAncester,
			String lrn_soln_type, String out_format, String outResult,
			boolean isPending_plan, String plan_status, String start_time,
			String end_time, String lang, String itmDir, String sort_name,
			String sort_order) throws SQLException,
			cwException, qdbException {
    	return getLearningPlan(con,
    			isSelfInitiatedEnabled, root_ent_id, usr_ent_id,
    			usr_display_bil, groupAncester, gradeAncester,
    			lrn_soln_type, out_format, outResult,
    			isPending_plan, plan_status, start_time,
    			end_time, lang, itmDir, sort_name,
    			sort_order, null);
    }

	public static Vector getLearningPlan(Connection con,
			boolean isSelfInitiatedEnabled, long root_ent_id, long usr_ent_id,
			String usr_display_bil, String groupAncester, String gradeAncester,
			String lrn_soln_type, String out_format, String outResult,
			boolean isPending_plan, String plan_status, String start_time,
			String end_time, String lang, String itmDir, String sort_name,
			String sort_order, CourseModuleParam param) throws SQLException,
			cwException, qdbException {
    		StringBuffer result = new StringBuffer();
    		Timestamp curTime = cwSQL.getTime(con);

    		// 获取用户所有相关培训课程（查询出来的课程有可能形如：2, 2, 6, 8, 9, 9...）
    		List lst = getLearningPlanByCmd(con, isSelfInitiatedEnabled,
    				root_ent_id, usr_ent_id, groupAncester, gradeAncester, curTime, lrn_soln_type, isPending_plan, lang, itmDir, param);

	    	Vector vec_elective = new Vector();
			Vector vec_other = new Vector();
			Vector target_itm_id = new Vector();

			myLearningPlan prevRec = new myLearningPlan();	// 当前课程的先前课程
			myLearningPlan currRec = null;					// 当前待筛选的课程
			boolean hasTargetedLrnPrivilege = false;		// 是否具有访问ID为itm_id该门课程的权限

			if (lst != null && lst.size() != 0) {
				Iterator itr = lst.iterator();

				while(itr.hasNext()) {
					currRec = (myLearningPlan)itr.next();

					// 判定当前待筛选的课程，用户是否具有访问权限
					hasTargetedLrnPrivilege = aeItem.hasTargetedLrnPrivilege(con, root_ent_id, usr_ent_id, currRec.pitm_id);

					if (prevRec.pitm_id == 0) {
						currRec.maxEnd_timestamp = currRec.appn_end_Timestamp;
						prevRec.maxEnd_timestamp = currRec.maxEnd_timestamp;
						prevRec = currRec;
					}

					// 若该门课程属于"适合我的培训"
					if (!target_itm_id.contains(new Long(currRec.pitm_id)) && currRec.type.equals(LRNPLAN_A_TARGET)) {
						target_itm_id.add(new Long(currRec.pitm_id));
					}

					if (currRec.pitm_id != prevRec.pitm_id) {
						if (prevRec.status == null) {
							if (curTime.after(prevRec.maxEnd_timestamp)) {	// 若当前时间大于课程结束时间
								prevRec.status = LRNPLAN_REMARK_CAN_NOT_ENROLL;	// 标记该门课程的status为"can_not_enroll"
							} else {
								prevRec.status = LRNPLAN_REMARK_NEXT_ENROLL;	// 标记该门课程的status为"next_enroll"
							}
						}

						if (prevRec.status != null /*&& !prevRec.status.equals(LRNPLAN_REMARK_HAS_COMPLETED)*/) {
							if (prevRec.type.equals(LRNPLAN_A_TARGET)) {// 若该门课程属于"适合我的培训"
								vec_elective.add(prevRec);
							} else if (prevRec.type.equals(LRNPLAN_B_SOLN) && !target_itm_id.contains(new Long(prevRec.pitm_id))
										&& hasTargetedLrnPrivilege){	// 若该门课程属于"我的培训计划"
//								vec_other.add(prevRec);
								if(filtratePlan(prevRec, plan_status, start_time, end_time, curTime))
								{
									vec_other.add(prevRec);
								}
							}
						}

						currRec.maxEnd_timestamp = currRec.appn_end_Timestamp;
						prevRec.maxEnd_timestamp = currRec.maxEnd_timestamp;
						prevRec = currRec;
					}

					if (prevRec.status == null) {

						if (currRec.app_id > 0) {	// 若该门课程存在该用户的报名记录
							boolean applicable = aeItem.isItemEnrollable(con, currRec.pitm_id, usr_ent_id);

							if (currRec.app_status.equals(aeApplication.ADMITTED)) {	// 若该用户已被审批了此门课程

								if (currRec.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_ATTEND)) {	// 若已完成学习
									currRec.status = LRNPLAN_REMARK_HAS_COMPLETED;	// 标记该门课程的status为"has_completed"
									prevRec.status = currRec.status;
								} else if (currRec.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_PROGRESS)) {	// 若正在学习中
									if (!applicable){
										currRec.status = LRNPLAN_REMARK_HAS_ENROLL;	// 标记该门课程的status为"has_enroll"
										prevRec.status = currRec.status;
									}
								}
							} else if (currRec.app_status.equals(aeApplication.PENDING)
										|| currRec.app_status.equals(aeApplication.WAITING)) {	// 若该用户还在等待此门课程的审批
								if (!applicable) {
									currRec.status = LRNPLAN_REMARK_HAS_ENROLL;	// 标记该门课程的status为"has_enroll"
									prevRec.status = currRec.status;
								}
							}
						}

						if (currRec.status == null) {
							if (currRec.appn_start_Timestamp== null && currRec.appn_end_Timestamp== null) {
								currRec.status = LRNPLAN_REMARK_CAN_NOT_ENROLL;
								prevRec.status = currRec.status;
							}else if (curTime.after(currRec.appn_start_Timestamp) && curTime.before(currRec.appn_end_Timestamp)) {
								currRec.status = LRNPLAN_REMARK_OPEN_ENROLL;
								prevRec.status = currRec.status;
							}else{
								if (currRec.eff_end == null) {
									prevRec.maxEnd_timestamp = currRec.appn_end_Timestamp;
									prevRec.minStart_timestamp = currRec.appn_start_Timestamp;
								} else if (curTime.before(currRec.appn_end_Timestamp)){
									currRec.maxEnd_timestamp = currRec.appn_end_Timestamp;
									currRec.minStart_timestamp = currRec.appn_start_Timestamp;
									if (currRec.maxEnd_timestamp.after(prevRec.maxEnd_timestamp)) {
										prevRec.maxEnd_timestamp = currRec.maxEnd_timestamp;
									}
									if (currRec.minStart_timestamp.before(prevRec.minStart_timestamp)) {
										prevRec.minStart_timestamp = currRec.minStart_timestamp;
									}
								}
							}
						}

						if(prevRec.pitm_id != 0 && lrn_soln_type.equalsIgnoreCase(RECOMMENDED)) {
							prevRec.setPlan_ind(aeLearningSoln.existSoln(con, usr_ent_id, prevRec.pitm_id, root_ent_id));
						}
					}

				} // end while

				if (prevRec.status == null) {
					if (curTime.after(prevRec.maxEnd_timestamp)) {
						prevRec.status = LRNPLAN_REMARK_CAN_NOT_ENROLL;
					} else {
						prevRec.status = LRNPLAN_REMARK_NEXT_ENROLL;
					}
				}

				if (!prevRec.status.equals(LRNPLAN_REMARK_HAS_COMPLETED) && (prevRec.status != null)) {

					if (prevRec.type.equals(LRNPLAN_A_TARGET)) {

						vec_elective.add(prevRec);
					}else if (prevRec.type.equals(LRNPLAN_B_SOLN) && !target_itm_id.contains(new Long(prevRec.pitm_id)) && hasTargetedLrnPrivilege){
						if(filtratePlan(prevRec, plan_status, start_time, end_time, curTime))
						{
							vec_other.add(prevRec);
						}
					}
				}
			} // end outer if

			if(out_format.equalsIgnoreCase("XML_FORMAT")) {
				result.append("<learning_soln>");
				result.append("<student display_bil=\"").append(cwUtils.esc4XML(aeUtils.escNull(usr_display_bil))).append("\" ent_id=\"").append(usr_ent_id).append("\"/>");
				result.append(aeItem.getAllItemTypeTitleInOrg(con, root_ent_id));
				result.append("<item_list type=\"").append(aeLearningSoln.ELECTIVE).append("\">");
				result.append(toXML(con, vec_elective, usr_ent_id));
				result.append("</item_list>");
				result.append("<item_list type=\"").append(aeLearningSoln.OTHER).append("\">");
				result.append(toXML(con, vec_other, usr_ent_id));
				result.append("</item_list>");
				result.append("</learning_soln>");
				return null;
			}

		if(lrn_soln_type.equalsIgnoreCase(RECOMMENDED)) {
			return vec_elective;
		} else if(lrn_soln_type.equalsIgnoreCase(MYPLAN)) {
			return vec_other;
		}

		return null;
		//return result.toString();
    }

    /**
     * 我的培训计划按“状态”、“日期”过滤
     * @param plan
     * @param plan_status
     * @param start_time
     * @param end_time
     * @param cur_time
     * @return
     */
    private static boolean filtratePlan(myLearningPlan plan,
    		String plan_status, String start_time, String end_time, Timestamp cur_time) {
    	boolean b_status = false;
    	boolean b_time = false;
    	if( plan_status != null && plan_status.length() > 0) {
	    	if(plan_status.equalsIgnoreCase(NULL_NOTENROLL)) {
	    		if(plan.ats_cov_status == null){
	    			b_status = true;
	    		}
	    	}else if(plan_status.equalsIgnoreCase(I_PROGRESS)) {
	    		if("I".equalsIgnoreCase(plan.ats_cov_status)){
	    			b_status = true;
	    		}
	    	}else if(plan_status.equalsIgnoreCase(C_ATTEND)) {
	    		if("C".equalsIgnoreCase(plan.ats_cov_status)){
	    			b_status = true;
	    		}
	    	}else if(plan_status.equalsIgnoreCase(F_INCOMPLETE)) {
	    		if("F".equalsIgnoreCase(plan.ats_cov_status)){
	    			b_status = true;
	    		}
	    	}else if(plan_status.equalsIgnoreCase(W_WITHDRAWN)) {
	    		if("W".equalsIgnoreCase(plan.ats_cov_status)){
	    			b_status = true;
	    		}
	    	}
    	}else{//选择“状态”里的“所有”
    		b_status = true;
    	}

    	Calendar cal_1 = Calendar.getInstance();
    	Calendar cal_2 = Calendar.getInstance();
    	Calendar cal_today = Calendar.getInstance();
    	cal_today.setTime(cur_time);

    	if((start_time != null && start_time.length() >0)|| (end_time != null && end_time.length() >0)) {

	    	if(NOT_SET.equalsIgnoreCase(start_time) && NOT_SET.equalsIgnoreCase(end_time)) {// 未编排

	    		if(plan.lsn_end_datetime == null && plan.lsn_start_datetime==null) {
	    			b_time = true;
	    		}
	    	}

	    	if(start_time != null && end_time == null) {
	    		if (plan.lsn_start_datetime != null) {
		    		cal_1.setTime(plan.lsn_start_datetime);	// 开始日
		    		if(SevenDAY.equalsIgnoreCase(start_time)) {	// 最近7日开始(今天<=到期日<今天+一个星期)
		    			cal_2.add(Calendar.DATE, 7);
		    			cal_today.add(Calendar.DATE, -1);
		    			if (cal_1.after(cal_today) && cal_1.before(cal_2)) {
			    			b_time = true;
			    		}
		    		}
		    		if(OneMONTH.equalsIgnoreCase(start_time)) {	// 最近一个月开始(今天<=到期日<今天+一个月)
		    			cal_2.add(Calendar.MONTH, 1);

		    			cal_today.add(Calendar.DATE, -1);
		    			if (cal_1.after(cal_today) && cal_1.before(cal_2))  {
			    			b_time = true;
			    		}
		    		}
	    		}
	    	} else if (end_time != null && start_time == null){
	    		if (plan.lsn_end_datetime != null) {
		    		cal_1.setTime(plan.lsn_end_datetime);	// 到期日
			    	if(SevenDAY.equalsIgnoreCase(end_time)) {	// 最近7日到期(今天<=到期日<今天+一个星期)
			    		cal_2.add(Calendar.DATE, 7);
			    		cal_today.add(Calendar.DATE, -1);
		    			if (cal_1.after(cal_today) && cal_1.before(cal_2))  {
			    			b_time = true;
			    		}
			    	}
			    	if(OneMONTH.equalsIgnoreCase(end_time)) {	// 最近一个月到期(今天<=到期日<今天+一个月)
			    		cal_2.add(Calendar.MONTH, 1);

			    		cal_today.add(Calendar.DATE, -1);
		    			if (cal_1.after(cal_today) && cal_1.before(cal_2))  {
			    			b_time = true;
			    		}
			    	}
	    		}
	    	}
    	}else{//选择“日期”里的“所有”
    		b_time = true;
    	}

    	if((b_status == true && b_time == true)){
    		return true;
    	}
    	return false;
    }

	public static String getMyLearningPlanXML(Connection con, boolean isSelfInitiatedEnabled, loginProfile prof, String groupAncester, String gradeAncester) throws SQLException, cwException, qdbException  {
		/*
		StringBuffer result = new StringBuffer();
		Timestamp curTime = cwSQL.getTime(con);
		List lst = getMyLearningPlan(con, isSelfInitiatedEnabled, prof.root_ent_id, prof.usr_ent_id, groupAncester, gradeAncester, curTime);
		Vector vec_elective = new Vector();
		Vector vec_other = new Vector();
		Vector target_itm_id = new Vector();
		myLearningPlan prevRec = new myLearningPlan();
		myLearningPlan currRec = null;
		boolean hasTargetedLrnPrivilege = false;
		if (lst != null && lst.size() != 0) {
			Iterator itr = lst.iterator();
			while(itr.hasNext()) {
				currRec = (myLearningPlan)itr.next();
				hasTargetedLrnPrivilege = aeItem.hasTargetedLrnPrivilege(con, prof.root_ent_id, prof.usr_ent_id, currRec.pitm_id);
				if (prevRec.pitm_id == 0) {
					currRec.maxEnd_timestamp = currRec.appn_end_Timestamp;
					prevRec.maxEnd_timestamp = currRec.maxEnd_timestamp;
					prevRec = currRec;
				}
				if (!target_itm_id.contains(new Long(currRec.pitm_id)) && currRec.type.equals(LRNPLAN_A_TARGET)) {
					target_itm_id.add(new Long(currRec.pitm_id));
				}
				if (currRec.pitm_id != prevRec.pitm_id) {
					if (prevRec.status == null) {
						if (curTime.after(prevRec.maxEnd_timestamp)) {
							prevRec.status = LRNPLAN_REMARK_CAN_NOT_ENROLL;
						} else {
							prevRec.status = LRNPLAN_REMARK_NEXT_ENROLL;
						}
					}
					if (!prevRec.status.equals(LRNPLAN_REMARK_HAS_COMPLETED) && (prevRec.status != null)) {
						if (prevRec.type.equals(LRNPLAN_A_TARGET)) {
							vec_elective.add(prevRec);
						}else if (prevRec.type.equals(LRNPLAN_B_SOLN) && !target_itm_id.contains(new Long(prevRec.pitm_id)) && hasTargetedLrnPrivilege){
							vec_other.add(prevRec);
						}
					}
					currRec.maxEnd_timestamp = currRec.appn_end_Timestamp;
					prevRec.maxEnd_timestamp = currRec.maxEnd_timestamp;
					prevRec = currRec;
				}
				if (prevRec.status == null) {
					if (currRec.app_id > 0) {
						boolean applicable = aeItem.isItemEnrollable(con, currRec.pitm_id, prof.usr_ent_id);
						if (currRec.app_status.equals(aeApplication.ADMITTED)) {
							if (currRec.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_ATTEND)) {
								currRec.status = LRNPLAN_REMARK_HAS_COMPLETED;
								prevRec.status = currRec.status;
							} else if (currRec.ats_type.equals(aeAttendanceStatus.STATUS_TYPE_PROGRESS)) {
								if (!applicable){
									currRec.status = LRNPLAN_REMARK_HAS_ENROLL;
									prevRec.status = currRec.status;
								}
							}
						} else if (currRec.app_status.equals(aeApplication.PENDING) || currRec.app_status.equals(aeApplication.WAITING)) {
							if (!applicable) {
								currRec.status = LRNPLAN_REMARK_HAS_ENROLL;
								prevRec.status = currRec.status;
							}
						}
					}
					if (currRec.status == null) {
						if (currRec.appn_start_Timestamp== null && currRec.appn_end_Timestamp== null) {
							currRec.status = LRNPLAN_REMARK_CAN_NOT_ENROLL;
							prevRec.status = currRec.status;
						}else if (curTime.after(currRec.appn_start_Timestamp) && curTime.before(currRec.appn_end_Timestamp)) {
							currRec.status = LRNPLAN_REMARK_OPEN_ENROLL;
							prevRec.status = currRec.status;
						}else{
							if (currRec.eff_end == null) {
								prevRec.maxEnd_timestamp = currRec.appn_end_Timestamp;
								prevRec.minStart_timestamp = currRec.appn_start_Timestamp;
							} else if (curTime.before(currRec.appn_end_Timestamp)){
								currRec.maxEnd_timestamp = currRec.appn_end_Timestamp;
								currRec.minStart_timestamp = currRec.appn_start_Timestamp;
								if (currRec.maxEnd_timestamp.after(prevRec.maxEnd_timestamp)) {
									prevRec.maxEnd_timestamp = currRec.maxEnd_timestamp;
								}
								if (currRec.minStart_timestamp.before(prevRec.minStart_timestamp)) {
									prevRec.minStart_timestamp = currRec.minStart_timestamp;
								}
							}
						}
					}
				}
			}
			if (prevRec.status == null) {
				if (curTime.after(prevRec.maxEnd_timestamp)) {
					prevRec.status = LRNPLAN_REMARK_CAN_NOT_ENROLL;
				} else {
					prevRec.status = LRNPLAN_REMARK_NEXT_ENROLL;
				}
			}
			if (!prevRec.status.equals(LRNPLAN_REMARK_HAS_COMPLETED) && (prevRec.status != null)) {
				if (prevRec.type.equals(LRNPLAN_A_TARGET)) {
					vec_elective.add(prevRec);
				}else if (prevRec.type.equals(LRNPLAN_B_SOLN) && !target_itm_id.contains(new Long(prevRec.pitm_id)) && hasTargetedLrnPrivilege){
					vec_other.add(prevRec);
				}
			}
		}
		result.append("<learning_soln>");
		result.append("<student display_bil=\"").append(cwUtils.esc4XML(aeUtils.escNull(prof.usr_display_bil))).append("\" ent_id=\"").append(prof.usr_ent_id).append("\"/>");
		result.append(aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id));
		result.append("<item_list type=\"").append(aeLearningSoln.ELECTIVE).append("\">");
		result.append(toXML(con, vec_elective,prof.usr_ent_id));
		result.append("</item_list>");
		result.append("<item_list type=\"").append(aeLearningSoln.OTHER).append("\">");
		result.append(toXML(con, vec_other,prof.usr_ent_id));
		result.append("</item_list>");
		result.append("</learning_soln>");
		return result.toString();
		*/

		return "";
	}

	private static String toXML(Connection con, Vector vec, long usr_ent_id) throws SQLException, cwException {
		StringBuffer temp = new StringBuffer();
		myLearningPlan myLrnplan = new myLearningPlan();
		for (int i=0; i<vec.size(); i++) {
			myLrnplan = (myLearningPlan)vec.elementAt(i);
			temp.append("<item id=\"").append(myLrnplan.pitm_id).append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(myLrnplan.pitm_title))).append("\" type=\"").append(aeUtils.escNull(myLrnplan.pitm_type)).append("\" remark_ind=\"").append(myLrnplan.status);
			if (myLrnplan.type.equals(LRNPLAN_B_SOLN)) {
				temp.append("\" self_plan=\"").append("true").append("\" >");
			}else {
				temp.append("\">");
			}
			temp.append("<history>");
			if (myLrnplan.app_status != null) {
				temp.append("<record itm_id=\"").append(myLrnplan.pitm_id).append("\">");
				temp.append("<application id=\"").append(myLrnplan.app_id).append("\" queue_status=\"").append(aeUtils.escNull(myLrnplan.app_status)).append("\"/>");
				temp.append("<attendance attended=\"").append("NO").append("\"/>");
				temp.append("</record>");
			}
			temp.append("</history>");
			temp.append("<training_center id=\"").append(myLrnplan.itm_tcr_id).append("\">").append(cwUtils.esc4XML(cwUtils.escNull(myLrnplan.pitm_tcr_title))).append("</training_center>");
			temp.append("<enrolment_info itm_id=\"").append(myLrnplan.pitm_id);
			if (myLrnplan.minStart_timestamp != null) {
				temp.append("\" start_datetime=\"").append(myLrnplan.minStart_timestamp);
			}
			temp.append("\"/>");
			temp.append("</item>");
		}
		return temp.toString();
	}

	private static List getMyLearningPlan(Connection con, boolean isSelfInitiatedEnabled, long root_ent_id, long usr_ent_id, String groupAncester, String gradeAncester, Timestamp cur_time) throws SQLException, cwException {
		List lst = new ArrayList();
		/*
		StringBuffer sql = new StringBuffer();
		boolean hasUsrGroup = false;
		boolean hasUsrGrade = false;
		String[] acsite_targeted_entity = ViewEntityToTree.getTargetEntity(con, root_ent_id);
		int usr_dimension = getUsrDimensionByUsrEntId(con, usr_ent_id);
		if (acsite_targeted_entity.length <= usr_dimension || isSelfInitiatedEnabled) {
			sql.append("select itm.type type, parent.itm_id pitm_id, parent.itm_title pitm_title, parent.itm_type pitm_type, ")
			   .append(cwSQL.replaceNull(con, "child.itm_appn_start_datetime", "parent.itm_appn_start_datetime")).append(" appn_start, ")
			   .append(cwSQL.replaceNull(con, "child.itm_appn_end_datetime", "parent.itm_appn_end_datetime")).append(" appn_end, ")
			   .append(cwSQL.replaceNull(con, "child.itm_content_eff_end_datetime", "parent.itm_content_eff_end_datetime")).append(" eff_end,")
			   .append(" app_status, ").append(cwSQL.replaceNull(con, "app_id", "-1")).append(" app_id, ats_type")
			   .append(" ,parent.itm_tcr_id pitm_tcr_id, child.itm_tcr_id itm_tcr_id, ")
			   .append(cwSQL.replaceNull(con, "ptc.tcr_title", "tc.tcr_title"))
			   .append(" pitm_tcr_title, tc.tcr_title itm_tcr_title ")
			   .append(" from (");
			if (acsite_targeted_entity.length <= usr_dimension) {
				for (int i=0; i<acsite_targeted_entity.length; i++) {
					if (acsite_targeted_entity[i].equals("user_group")) {
						hasUsrGroup = true;
					}
					if (acsite_targeted_entity[i].equals("grade")) {
						hasUsrGrade = true;
					}
				}
				if (hasUsrGroup) {
					sql.append(" Select '").append(LRNPLAN_A_TARGET).append("' as type,ird_itm_id as itm_id from aeItemTargetRuleDetail,aeItem ")
					.append(" where itm_id = ird_itm_id and itm_access_type is not null ");
					sql.append(" and ird_type = ? and ird_group_id in (").append(groupAncester).append(")");
				}
				if (hasUsrGrade) {
					sql.append(" and ird_grade_id in (").append(gradeAncester).append(")");
				}
			}
			if (acsite_targeted_entity.length <= usr_dimension && isSelfInitiatedEnabled) {
				sql.append(" union ");
			}
			if (isSelfInitiatedEnabled) {
				sql.append("select '").append(LRNPLAN_B_SOLN).append("' as type,lsn_itm_id as itm_id from aeLearningSoln where lsn_status_ind = 1 and lsn_ent_id = ?");
			}
			sql.append(" ) itm ");
			sql.append(" inner join aeItem parent on (itm.itm_id = parent.itm_id and parent.itm_status = ?)")
			   .append(" left join aeItemRelation on (parent.itm_id = ire_parent_itm_id)")
			   .append(" left join aeItem child on (ire_child_itm_id = child.itm_id)")
			   .append(" left join aeApplication on (app_ent_id = ? and (parent.itm_id = app_itm_id or child.itm_id = app_itm_id) and app_status in (?, ?, ?))")
			   .append(" left join aeAttendance on (app_id = att_app_id)")
			   .append(" left join aeAttendanceStatus on (att_ats_id = ats_id)")
			   .append(" LEFT JOIN tcTrainingCenter ptc ON parent.itm_tcr_id = ptc.tcr_id ")
    		   .append(" LEFT JOIN tcTrainingCenter tc ON child.itm_tcr_id = tc.tcr_id ")
			   .append(" where (child.itm_status is null or child.itm_status = ?)")
			   .append(" order by itm.type, parent.itm_type desc, parent.itm_id asc, app_id desc");
			PreparedStatement stmt;
			ResultSet rs;
			stmt = con.prepareStatement(sql.toString());
			int index = 1;
			if (hasUsrGroup) {
				//stmt.setString(index++, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
				stmt.setString(index++, DbItemTargetRuleDetail.IRD_TYPE_TARGET_LEARNER);
			}
			if (isSelfInitiatedEnabled) {
				stmt.setLong(index++, usr_ent_id);
			}
			stmt.setString(index++, aeItem.ITM_STATUS_ON);
			stmt.setLong(index++, usr_ent_id);
			stmt.setString(index++, aeApplication.WAITING);
			stmt.setString(index++, aeApplication.PENDING);
			stmt.setString(index++, aeApplication.ADMITTED);
			stmt.setString(index++, aeItem.ITM_STATUS_ON);
			rs = stmt.executeQuery();
			while (rs.next()) {
			/*
				myLearningPlan myLrnplan = new myLearningPlan();
				myLrnplan.type = rs.getString("type");
				myLrnplan.pitm_type = rs.getString("pitm_type");
				myLrnplan.pitm_id = rs.getLong("pitm_id");
				myLrnplan.pitm_title = rs.getString("pitm_title");
			//	myLrnplan.appn_start = rs.getTimestamp("appn_start");
			//	myLrnplan.appn_end = rs.getTimestamp("appn_end");
				myLrnplan.eff_end = rs.getTimestamp("eff_end");
				myLrnplan.app_status = rs.getString("app_status");
				myLrnplan.app_id = rs.getLong("app_id");
				myLrnplan.ats_type = rs.getString("ats_type");
				myLrnplan.pitm_tcr_id = rs.getLong("pitm_tcr_id");
				myLrnplan.itm_tcr_id = rs.getLong("itm_tcr_id");
				myLrnplan.pitm_tcr_title = rs.getString("pitm_tcr_title");
				myLrnplan.itm_tcr_title = rs.getString("itm_tcr_title");

				lst.add(myLrnplan);
			}
			stmt.close();
		}
		*/
		return lst;
	}


	public static int getUsrDimensionByUsrEntId (Connection con, long usr_ent_id) throws SQLException, cwException {
		int entityCnt = 0;
		Vector user_group_ent_ids = new Vector();
		Vector user_industry_ent_ids = new Vector();
		Vector user_grade_ent_ids = new Vector();
		user_grade_ent_ids = DbUserGrade.getusrGradeEntIds(con, usr_ent_id);
		user_group_ent_ids = dbUserGroup.getUserParentEntIds(con, usr_ent_id);
		user_industry_ent_ids = DbIndustryCode.getIndCodeEntIds(con, usr_ent_id);
		Hashtable h_v_entity = new Hashtable();
		if (user_group_ent_ids != null && user_group_ent_ids.size() > 0) {
			entityCnt++;
		}
		if (user_industry_ent_ids != null && user_industry_ent_ids.size() > 0) {
			entityCnt++;
		}
		if (user_grade_ent_ids != null && user_grade_ent_ids.size() > 0) {
			entityCnt++;
		}
		return entityCnt;
	}

    private String getMyCoursesDetailAsXML(Connection con, Vector vec, String type, Timestamp cur_time) throws SQLException, cwSysMessage {
    	StringBuffer xml = new StringBuffer();
		Timestamp personal_content_end = null;
		//boolean itm_title_shown = false;
		ViewLearningSoln.MyCourses mc = new ViewLearningSoln.MyCourses();
		if (vec != null && vec.size() > 0) {
			for (int i=0; i<vec.size(); i++) {
				String unlimited = "";
				mc = (ViewLearningSoln.MyCourses)vec.elementAt(i);
				if (type.equals(mc.itm_type)) {
					if (mc.att_create_timestamp == null) {
						personal_content_end = null;
					} else if (mc.itm_content_eff_end_datetime != null) {
						personal_content_end = mc.itm_content_eff_end_datetime;
					} else if (mc.itm_content_eff_duration != 0){
						personal_content_end = new Timestamp((mc.att_create_timestamp).getTime() + (long)(mc.itm_content_eff_duration)*24*60*60*1000);
					} else {
						unlimited = EFF_DATE_UNLIMITED;
					}
					if (mc.ats_type != null && (mc.ats_type).equals(aeAttendanceStatus.STATUS_TYPE_ATTEND)&& !unlimited.equals(EFF_DATE_UNLIMITED) && cur_time.after(personal_content_end)) {
						continue;
					}
					xml.append("<item id=\"").append(mc.pitm_id)
					 .append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(mc.pitm_title)))
					 .append("\" type=\"").append(aeUtils.escNull(mc.itm_type))
					 .append("\" course_id=\"").append(mc.cov_cos_id).append("\">");
				   xml.append("<application tkh_id=\"").append(mc.app_tkh_id)
					 .append("\" start_date=\"").append(mc.att_create_timestamp);
				   if (unlimited.equals(EFF_DATE_UNLIMITED)) {
					  xml.append("\" end_date=\"").append(unlimited);
				   } else {
				      xml.append("\" end_date=\"").append(personal_content_end);
				   }
				   xml.append("\" process_status=\"").append(mc.app_process_status).append("\" queue_status=\"").append(aeUtils.escNull(mc.app_status)).append("\">");
				   xml.append("<attendance status_id=\"").append(aeUtils.escNull(mc.att_ats_id)).append("\"/>");
				   xml.append("<application_item app_itm_id=\"").append(mc.itm_id).append("\" itm_title=\"").append(cwUtils.esc4XML(aeUtils.escNull(mc.itm_title))).append("\"/>");
				   xml.append("<training_center id=\"").append(mc.itm_tcr_id).append("\">").append(cwUtils.esc4XML(cwUtils.escNull(mc.itm_tcr_title))).append("</training_center>");
				   xml.append("</application>");
                   String total_time = "";
				   if (mc.cov_total_time != 0) {
				   	 total_time = dbAiccPath.getTime(mc.cov_total_time);
				   }

				   xml.append("<aicc_data course_id=\"").append(mc.cov_cos_id)
					 .append("\" tkh_id=\"").append(mc.app_tkh_id)
					 .append("\" last_acc_datetime=\"").append(aeUtils.escNull(mc.cov_last_acc_datetime))
					 .append("\" used_time=\"").append(total_time)
					 .append("\"/>");
				xml.append("</item>");
				}
			}
		}
    	return xml.toString();
    }

    private static String toItemXML(Connection con, loginProfile prof,
                                    aeItem item, String is_targeted,
                                    Vector app_lst, Hashtable evals)
        throws SQLException, cwSysMessage {
        StringBuffer result = new StringBuffer();
        aeItemCompetency aeItc = new aeItemCompetency();

        Timestamp cur_time = cwSQL.getTime(con);
        //boolean launch_ind = false;
        Hashtable htInfo = null;
        Timestamp createdTimestamp = null;
        Timestamp itm_content_eff_start_datetime= null;
        Timestamp itm_content_eff_end_datetime = null;
        String personal_content_start = null;
        String personal_content_end = null;
        int itm_content_eff_duration = 0;
        boolean itm_title_shown = false;

        if (app_lst != null && app_lst.size() > 0) {
            for(int i=0; i < app_lst.size(); i++) {
                boolean show_ind = false;
                htInfo = (Hashtable)app_lst.elementAt(i);
                if(htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_START_DATETIME) != null) {
                    itm_content_eff_start_datetime = (Timestamp)htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_START_DATETIME);
                }
                if(htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_END_DATETIME) != null) {
                    itm_content_eff_end_datetime = (Timestamp)htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_END_DATETIME);
                }
                itm_content_eff_duration = ((Integer)htInfo.get(ViewLearningSoln.ITEM_CONTENT_EFF_DURATION)).intValue();
                if(htInfo.get(ViewLearningSoln.ATTN_CREATE_TIMESTAMP) != null) {
                    createdTimestamp = (Timestamp)htInfo.get(ViewLearningSoln.ATTN_CREATE_TIMESTAMP);
                }
                if (createdTimestamp==null){
                    personal_content_start = null;
                    personal_content_end = null;
                }else{
                    if (itm_content_eff_duration != 0){
                        personal_content_start = createdTimestamp.toString();
                        personal_content_end = new Timestamp(createdTimestamp.getTime() + (long)itm_content_eff_duration*24*60*60*1000).toString();
                    }else{
                        if (itm_content_eff_end_datetime != null){
                            //personal_content_start = itm_content_eff_start_datetime.toString();
                            personal_content_start = createdTimestamp.toString();
                            personal_content_end = itm_content_eff_end_datetime.toString();
                        }else{
                            personal_content_start = createdTimestamp.toString();
                            personal_content_end = EFF_DATE_UNLIMITED;
                        }
                    }
                }
                Timestamp personal_content_end_ts = (personal_content_end == null || personal_content_end.equals(EFF_DATE_UNLIMITED))
                                                    ? null
                                                    : Timestamp.valueOf(personal_content_end);
                String queue_status = (String)htInfo.get(ViewLearningSoln.APPN_STATUS);
                String ats_type = (String)htInfo.get(ViewLearningSoln.ATTN_TYPE);
                //the item is shown in my course if
                //if the appn is waiting or pending or (admitted and the attendance is in progress or completed)
                //and if the attendance is completed, the course end date should not be passed
                if ((queue_status != null && queue_status.equals(aeApplication.WAITING)) ||  // the appn is waiting
                   (queue_status != null && queue_status.equals(aeApplication.PENDING)) ||   // or the appn is pending
                   (queue_status != null && queue_status.equals(aeApplication.ADMITTED) &&   // or
                     (ats_type == null || ats_type.equals(aeAttendanceStatus.STATUS_TYPE_PROGRESS) || (ats_type.equals(aeAttendanceStatus.STATUS_TYPE_ATTEND) && (personal_content_end_ts == null || personal_content_end_ts.after(cur_time))))
                      )) {
                            show_ind = true;
                      }

                if(show_ind) {
                    if(!itm_title_shown) {
                        Timestamp end_date = null;
                        try {
                            end_date = ViewCourseModuleCriteria.getCourseEndDate(con, item.itm_id, prof.usr_ent_id);
                        } catch (cwException cwEx) {
                            //cwEx.printStackTrace();
                        }

                        result.append("<item id=\"").append(item.itm_id)
                              .append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(item.itm_code)))
                              .append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(item.itm_title)))
                              .append("\" type=\"").append(aeUtils.escNull(item.itm_type))
                              .append("\" course_id=\"").append(item.cos_res_id)
                              .append("\" cos_eff_start_datetime=\"").append(aeUtils.escNull(item.itm_content_eff_start_datetime))
                              .append("\" cos_eff_end_datetime=\"").append(aeUtils.escNull(item.itm_content_eff_end_datetime))
                              .append("\" eff_start_datetime=\"").append(aeUtils.escNull(item.itm_eff_start_datetime))
                              .append("\" eff_end_datetime=\"").append(aeUtils.escNull(item.itm_eff_end_datetime))
                              .append("\" nature=\"").append(aeUtils.escNull(item.itm_apply_method))
                              .append("\" unit=\"").append(item.itm_unit)
                              .append("\" is_targeted=\"").append(is_targeted)
                              // ADDED
                              .append("\" end_date=\"").append(aeUtils.escEmptyDate(end_date))
                //              .append("\" launch_ind=\"").append(launch_ind)
                              .append("\" tob_existence=\"").append(item.hasToc(con))
                              .append("\" create_run_ind=\"").append(item.itm_create_run_ind)
                              .append("\">").append(cwUtils.NEWL);

                        // added for certificate
                        result.append(checkCertificateStatus(con, item, prof.usr_ent_id));
                        result.append(item.getAssignedNodeAsXML(con)).append(cwUtils.NEWL);
                        result.append(aeItc.asXML(con, item.itm_id, "ASC", "skb_title"));

                        itm_title_shown = true;
                    }

                    Hashtable info = (Hashtable)app_lst.elementAt(i);
                    Long run_itm_id = (Long)info.get(ViewLearningSoln.ITEM_ID);
                    Long app_tkh_id = (Long)info.get(ViewLearningSoln.APPN_TKH_ID);
                    dbCourseEvaluation eval = (evals==null || app_tkh_id == null) ? null : (dbCourseEvaluation)evals.get(app_tkh_id);
                    result.append("<application id=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.APPN_ID)))
                          .append("\" tkh_id=\"").append(eval==null?"":new Long(eval.cov_tkh_id).toString())
                          .append("\" datetime=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.APPN_TIMESTAMP)))
                          .append("\" start_date=\"").append(personal_content_start)
                          .append("\" end_date=\"").append(personal_content_end)
                          .append("\" process_status=\"").append(cwUtils.esc4XML(aeUtils.escNull(info.get(ViewLearningSoln.APPN_PROCESS_STATUS)))).append("\" queue_status=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.APPN_STATUS))).append("\">").append(cwUtils.NEWL);

                    result.append("<attendance datetime=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.ATTN_TIMESTAMP))).append("\" attended=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.ATTN_ATTEND_IND))).append("\" status_id=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.ATTN_ATTEND_STATUS_ID))).append("\"/>").append(cwUtils.NEWL);
                    result.append("<application_item app_itm_id=\"").append(run_itm_id.longValue()).append("\" itm_title=\"").append(cwUtils.esc4XML(aeUtils.escNull(info.get(ViewLearningSoln.ITEM_TITLE)))).append("\" itm_eff_start_datetime=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.ITEM_EFF_START_DATETIME))).append("\" itm_eff_end_datetime=\"").append(aeUtils.escNull(info.get(ViewLearningSoln.ITEM_EFF_END_DATETIME))).append("\"/>").append(cwUtils.NEWL);
                    if(run_itm_id != null){
                    	long itm_tcr_id = aeItem.getTcrId(con, run_itm_id.longValue(),prof.root_ent_id);
                    	DbTrainingCenter dbTc = DbTrainingCenter.getInstance(con, itm_tcr_id);
                    	if(dbTc !=null) {
                    		result.append("<training_center id=\"").append(dbTc.tcr_id).append("\">")
                    		.append(cwUtils.esc4XML(cwUtils.escNull(dbTc.tcr_title))).append("</training_center>");
                    	}
                    }
                    result.append("</application>").append(cwUtils.NEWL);
                    if(eval != null /*&& eval.cov_status.equalsIgnoreCase("I")*/) {
                       String total_time = "";

                       if (eval.cov_total_time != 0) {
                        total_time = dbAiccPath.getTime(eval.cov_total_time);
                       }

                       result.append("<aicc_data course_id=\"").append(eval.cov_cos_id)
                          .append("\" student_id=\"").append(eval.cov_ent_id)
                          .append("\" tkh_id=\"").append(eval.cov_tkh_id)
                          .append("\" last_acc_datetime=\"").append(aeUtils.escNull(eval.cov_last_acc_datetime))
                          .append("\" used_time=\"").append(total_time)
                          .append("\" status=\"").append(aeUtils.escNull(eval.cov_status))
                          .append("\" score=\"").append(aeUtils.escNull(eval.cov_score))
                          .append("\" comment=\"").append(aeUtils.escNull(eval.cov_comment))
                          .append("\"/>").append(cwUtils.NEWL);
                    }
                }
                if(itm_title_shown && i == app_lst.size()-1) {
                    result.append("</item>").append(cwUtils.NEWL);
                }
            }
        }
        return result.toString();
    }

    private static void println(String str) {
        if (debug) {
            System.out.println(str);
        }
    }

    private static void println(long l) {
        if (debug) {
        	logger.debug(l);
           // System.out.println(l);
        }
    }

    private static void print(String str) {
        if (debug) {
        	logger.debug(str);
            //System.out.print(str);
        }
    }

    private static void print(long l) {
        if (debug) {
        	logger.debug(l);
            //System.out.print(l);
        }
    }

    private static String checkCertificateStatus(Connection con, aeItem item, long usr_ent_id) {
        StringBuffer result = new StringBuffer(1024);
        try {
            ViewCfCertificate cert = new ViewCfCertificate(con);

            // get the certificate id
            int ctf_id = item.getCertificateId(con);

//          result.append(cert.getUserCertificationAsXML(ctf_id, usr_ent_id));

            // get the qualified status of certificate
            boolean qualification_status = cert.getQualificationInd(ctf_id, (int)usr_ent_id);
            // get the issued status of certificate
            String certification_status = cert.getCertificationStatus(ctf_id, (int)usr_ent_id);

            // generate the xml
            result.append("<certificate id=\"").append(ctf_id).append("\">");
            result.append("<completion_status>").append(qualification_status).append("</completion_status>");
            result.append("<issue_status>").append(aeUtils.escNull(certification_status)).append("</issue_status>");
            result.append("</certificate>");
        } catch (Exception e) {
            //e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
        }
        return result.toString();
    }

	public static Vector getSimplePlan(Connection con, loginProfile prof,String groupAncester, String gradeAncester)throws SQLException,qdbException
	{
		StringBuffer sql=new StringBuffer();
		sql.append("select distinct itm_id, itm_title from aeItemTargetRuleDetail,aeItem   where ird_group_id in ("+groupAncester+") and ird_grade_id in ("+gradeAncester+") ")
			.append("and ird_itm_id = itm_id ")
			.append("and itm_status = 'ON' " )
			.append("and not exists ( select * from aeLearningSoln where lsn_itm_id = ird_itm_id and lsn_ent_id = ?) ")
			.append("and not exists ( select * from aeApplication " )
			.append("where app_ent_id = ? ")
			.append("and (app_itm_id = ird_itm_id or  app_itm_id in (select ire_child_itm_id from aeItemRelation where ire_parent_itm_id = ird_itm_id)))");

		String appendSql =
			  " 			and (ird_upt_id = -1 "
			+ "			   		 or ird_upt_id in ( "
			+ "							select upr_upt_id ske_id "
			+ "							from UserPositionRelation "
			+ "							where upr_usr_ent_id = ? "	// 传入：学员ID(RegUser表的usr_ent_id)
			+ "					 ) "
			+ "				) "
			+ " 			and ird_type = 'TARGET_LEARNER'";

		PreparedStatement stmt=null;
		stmt=con.prepareStatement(sql.toString() + appendSql);
		stmt.setLong(1, prof.usr_ent_id);
		stmt.setLong(2, prof.usr_ent_id);
		stmt.setLong(3, prof.usr_ent_id);

		ResultSet rs=stmt.executeQuery();
		Vector recVc=new Vector();

		while(rs.next())
		{
			myLearningPlan myLrnplan = new myLearningPlan();
			myLrnplan.setPitm_id(rs.getLong("itm_id"));
			myLrnplan.setPitm_title(rs.getString("itm_title"));
			recVc.add(myLrnplan);
		}

		if (stmt != null) {
			stmt.close();
			stmt = null;
		}

		return recVc;
	}
}
/*
String ViewCfCertificate.getCertificationStatus(int cfn_ctf_id, int cfn_ent_id)
*/