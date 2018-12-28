package com.cw.wizbank.report;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeTemplate;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.DbTemplateView;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbObjectView;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class LearnerCosReport extends LearnerReport{
	public static final String RTE_TYPE_LEARNER_COS = "LEARNING_ACTIVITY_COS";
	static final String T_TITLE = "t_title";
	public static String DEFAULT_SORT_COL_ORDER_2 = "t_id";
	
	public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long usr_ent_id, long rsp_id, long rte_id) throws SQLException, qdbException, cwException, IOException, MalformedURLException, cwSysMessage {
		String xml_prefix = null;

		if (usr_ent_id > 0) {
			xml_prefix = getUserInfo(con, usr_ent_id);
		}

		return super.getReport(con, request, prof, rsp_id, rte_id, xml_prefix, null, null, RTE_TYPE_LEARNER_COS, null);
	 }
	 
	public String[] getLearningReportHelper(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, ViewReportSpec.Data data, long usr_ent_id, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException {

		   Hashtable spec_pairs = getSpecPairs(data.rsp_xml);

		   String[] ent_id_lst = null;
		   String[] s_usg_ent_id_lst = null;
		   String[] ugr_ent_id_lst = null;
		   String[] app_status_lst = null;
		   String[] tnd_id_lst = null;
		   String itm_title = null;
		   int itm_title_partial_ind = 1;
		   String[] ats_id_lst = null;
		   long[] itm_id_lst = null;
		   Timestamp itm_start_datetime = null;
		   Timestamp itm_end_datetime = null;
		   Timestamp att_create_start_datetime = null;
		   Timestamp att_create_end_datetime = null;
		   Timestamp att_start_datetime = null;
		   Timestamp att_end_datetime = null;
		   boolean show_itm_credit = false;
           boolean include_no_record = true;
           boolean show_stat_only = false;
           String lrn_scope_sql = null;
		   Vector spec_values;
           if(wizbini.cfgTcEnabled) {
        	   boolean all_user_ind = false;
        	   boolean all_cos_ind = false;
        	   boolean answer_for_lrn = false;
        	   boolean answer_for_course_lrn = false;
        	   boolean answer_for_course = false;
        	   boolean answer_for_lrn_course = false;
        	   if(spec_pairs.containsKey("all_user_ind")) {
        		   spec_values = (Vector)spec_pairs.get("all_user_ind");
        		   if(((String)spec_values.get(0)).equals("1")) {
        			   all_user_ind  = true;
        		   }
        	   }
        	   if(spec_pairs.containsKey("answer_for_lrn")) {
        		   spec_values = (Vector)spec_pairs.get("answer_for_lrn");
        		   if(((String)spec_values.get(0)).equals("1")) {
        			   answer_for_lrn  = true;
        		   }
        	   }
        	   if(spec_pairs.containsKey("answer_for_course_lrn")) {
        		   spec_values = (Vector)spec_pairs.get("answer_for_course_lrn");
        		   if(((String)spec_values.get(0)).equals("1")) {
        			   answer_for_course_lrn  = true;
        		   }
        	   }
        	   if(spec_pairs.containsKey("answer_for_course")) {
        		   spec_values = (Vector)spec_pairs.get("answer_for_course");
        		   if(((String)spec_values.get(0)).equals("1")) {
        			   answer_for_course  = true;
        		   }
        	   }
        	   if(spec_pairs.containsKey("answer_for_lrn_course")) {
        		   spec_values = (Vector)spec_pairs.get("answer_for_lrn_course");
        		   if(((String)spec_values.get(0)).equals("1")) {
        			   answer_for_lrn_course  = true;
        		   }
        	   }
        	   if(all_user_ind) {
//        		   Vector vec = new Vector();
        		   if((answer_for_lrn && answer_for_course_lrn )||(!answer_for_lrn && !answer_for_course_lrn )) {
//        			   vec = ViewLearnerReport.getAllLrn(con, prof.usr_ent_id, prof.root_ent_id);
        			   lrn_scope_sql = ViewLearnerReport.getAllLrnSql(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
        		   } else if (answer_for_lrn && !answer_for_course_lrn) {
//        			   vec = ViewLearnerReport.getMyRspLrn(con, prof.usr_ent_id, prof.root_ent_id);
        			   lrn_scope_sql = ViewLearnerReport.getMyRspLrnSql(Report.COLNAME_TMP_USR_ENT_ID ,prof.usr_ent_id, prof.root_ent_id);
        		   } else if (!answer_for_lrn && answer_for_course_lrn) {
//        			   vec = ViewLearnerReport.getMyRspCosEnrollLrn(con, prof.usr_ent_id, prof.root_ent_id);
        			   lrn_scope_sql = ViewLearnerReport.getMyRspCosEnrollLrn(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
        		   } 
/*        		   if(vec.size() == 0) {
        			   ent_id_lst = new String[1];
        			   ent_id_lst[0] = "0";
        		   } else {
            		   ent_id_lst = new String[vec.size()];
            		   for (int i=0; i<vec.size(); i++) {
            			   Long ent_id = (Long)vec.elementAt(i);
            			   ent_id_lst[i] = ent_id.longValue() + "";
            		   }        			   
        		   }*/
        	   }
        	   if(!spec_pairs.containsKey("tnd_id") && !spec_pairs.containsKey("itm_id")) {
        		   all_cos_ind = true;
        	   }
        	   if(all_cos_ind) {
        		   Vector vec = new Vector();
        		   if((answer_for_course && answer_for_lrn_course )||(!answer_for_course && !answer_for_lrn_course )) {
        			   vec = ViewLearnerReport.getAllCos(con, prof.usr_ent_id, prof.root_ent_id);
        		   } else if (answer_for_course && !answer_for_lrn_course) {
        			   vec = ViewLearnerReport.getMyRspCos(con, prof.usr_ent_id, prof.root_ent_id);
        		   } else if (!answer_for_course && answer_for_lrn_course) {
        			   vec = ViewLearnerReport.getMyRspLrnEnrollCos(con, prof.usr_ent_id, prof.root_ent_id);
        		   }
        		   if(vec.size() == 0) {
        			   itm_id_lst = new long[1];
        			   itm_id_lst[0] = 0;
        		   } else {
        			   itm_id_lst = new long[vec.size()];
        			   for(int i=0; i<vec.size(); i++) {
        				   itm_id_lst[i] = ((Long)vec.elementAt(i)).longValue();
        			   }
        		   }
        	   }
           }
		   
           if ((spec_values = (Vector)spec_pairs.get("include_no_record")) != null) {
               include_no_record = new Boolean((String)spec_values.get(0)).booleanValue();
           }

           if ((spec_values = (Vector)spec_pairs.get("show_stat_only")) != null) {
               show_stat_only = new Boolean((String)spec_values.get(0)).booleanValue();
           }

		   if (spec_pairs.containsKey("usr_ent_id")||spec_pairs.containsKey("usg_ent_id")) {
			if((spec_values=(Vector)spec_pairs.get("usr_ent_id"))==null){
				spec_values = (Vector)spec_pairs.get("usg_ent_id");
			 }

			   ent_id_lst = new String[spec_values.size()];

			   for (int i=0; i<spec_values.size(); i++) {
				   ent_id_lst[i] = (String)spec_values.elementAt(i);
			   }
		   }

		   if (spec_pairs.containsKey("s_usg_ent_id_lst")) {
			   spec_values = (Vector)spec_pairs.get("s_usg_ent_id_lst");

			   s_usg_ent_id_lst  = new String[spec_values.size()];
			   Supervisor sup = new Supervisor(con, prof.usr_ent_id);
			   Vector  v_ent_id  =  new  Vector();
            
			   for (int i=0; i<spec_values.size(); i++) {
				   s_usg_ent_id_lst[i] = (String)spec_values.elementAt(i);
				   if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_STAFF)) {
					   Vector vStaff = sup.getStaffEntIdVector(con);
					   for (int k=0; k<vStaff.size(); k++){
						   v_ent_id.addElement(vStaff.elementAt(k)); 
					   }
				   } else if(s_usg_ent_id_lst[i].equals(Supervisor.SEARCH_MY_DIRECT_STAFF)) {
					   Vector vStaff = sup.getDirectStaffEntIdVector(con);
					   for (int k=0; k<vStaff.size(); k++){
						   v_ent_id.addElement(vStaff.elementAt(k)); 
					   }
				   }else{
					   v_ent_id.addElement(Long.valueOf(s_usg_ent_id_lst[i]));
				   }
			   }
			   ent_id_lst = new String[v_ent_id.size()];
			   for(int i=0; i<v_ent_id.size(); i++) {
				   ent_id_lst[i] = (v_ent_id.elementAt(i).toString());
			   }
		   }

		   if (spec_pairs.containsKey("tnd_id")) {
			   spec_values = (Vector)spec_pairs.get("tnd_id");

			   tnd_id_lst = new String[spec_values.size()];

			   for (int i=0; i<spec_values.size(); i++) {
				   tnd_id_lst[i] = (String)spec_values.elementAt(i);
			   }
		   }

		   if (spec_pairs.containsKey("ugr_ent_id")) {
			   spec_values = (Vector)spec_pairs.get("ugr_ent_id");

			   ugr_ent_id_lst = new String[spec_values.size()];

			   for (int i=0; i<spec_values.size(); i++) {
				   ugr_ent_id_lst[i] = (String)spec_values.elementAt(i);
			   }
		   }

		   if (spec_pairs.containsKey("itm_title")) {
			   spec_values = (Vector)spec_pairs.get("itm_title");

			   itm_title = (String)spec_values.elementAt(0);
		   }

		   if (spec_pairs.containsKey("itm_title_partial_ind")) {
			   spec_values = (Vector)spec_pairs.get("itm_title_partial_ind");

			   itm_title_partial_ind = ((new Integer((String)spec_values.elementAt(0)))).intValue();
		   }

		   if (spec_pairs.containsKey("ats_id")) {
			   spec_values = (Vector)spec_pairs.get("ats_id");
			   // determine "ALL", donald update at 18:00,04,16,2004
			   if(((String)spec_values.elementAt(0)).equals("0")){
				   
				   Vector vID = aeAttendanceStatus.getAllStatusIdByRoot(con, prof.root_ent_id);
				   ats_id_lst=new String[vID.size()];
				   try{
					  for(int i=0;i<vID.size();i++){
						 ats_id_lst[i]=((Integer)vID.get(i)).toString();;
						 //System.out.println("========="+ats_id_lst[i]);
					  }
				   }catch(NumberFormatException  e){
					   CommonLog.error(e.getMessage(),e);
				   }
			   }
			   else{
				 ats_id_lst = new String[spec_values.size()];
				 for (int i=0; i<spec_values.size(); i++) {
				   ats_id_lst[i] = (String)spec_values.elementAt(i);
				 }
			   }
		   }

		   if( spec_pairs.containsKey("app_status_lst") ) {
			   spec_values = (Vector)spec_pairs.get("app_status_lst");
			   app_status_lst = new String[spec_values.size()];
			   for(int i=0; i<spec_values.size(); i++)
				   app_status_lst[i] = (String)spec_values.elementAt(i);
		   }

		   if( spec_pairs.containsKey("show_itm_credit") ) {
			   spec_values = (Vector)spec_pairs.get("show_itm_credit");
			   if( ((String)spec_values.elementAt(0)).equalsIgnoreCase("true") )
				   show_itm_credit = true;
		   }

		   if (spec_pairs.containsKey("att_create_start_datetime")) {

			   spec_values = (Vector)spec_pairs.get("att_create_start_datetime");
			   CommonLog.debug("DENNIS: att_create = " + att_create_start_datetime);
			   att_create_start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
		   }
		   if (spec_pairs.containsKey("att_create_end_datetime")) {
			   spec_values = (Vector)spec_pairs.get("att_create_end_datetime");

			   att_create_end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
		   }
		   if (spec_pairs.containsKey("att_start_datetime")) {
			   spec_values = (Vector)spec_pairs.get("att_start_datetime");
			   CommonLog.debug("DENNIS: att = " + att_create_start_datetime);

			   att_start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
		   }

		   if (spec_pairs.containsKey("att_end_datetime")) {
			   spec_values = (Vector)spec_pairs.get("att_end_datetime");

			   att_end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
		   }

		   if (spec_pairs.containsKey("itm_id")) {
			   spec_values = (Vector)spec_pairs.get("itm_id");
			   itm_id_lst = new long[spec_values.size()];
			   for(int i=0; i<spec_values.size(); i++) {
				   itm_id_lst[i] = Long.parseLong((String)spec_values.elementAt(i));
			   }
		   }
		   //System.out.println("entId:" + cwUtils.array2list(ent_id_lst));
		   //Donald update at 16:30,04-15-2004
		   //System.out.println("-------------Start processing the page parameter----------");
		   Vector v=null;
		   if((v=(Vector)spec_pairs.get("sort_col"))!=null){
			   String col=(String)v.elementAt(0);
			   if(col.equals("itm_code")||col.equals("itm_title")){
				   col="t"+col.substring(col.indexOf("_"));
			   }
			   page.sortCol=cwPagination.esc4SortSql(col);
			   //System.out.println("===============sortCol=="+page.sortCol);
			   v=null;
		   }
		   if((v=(Vector)spec_pairs.get("sort_col1"))!=null){
			   String col = (String) v.elementAt(0);
			   if(col.equals("itm_code")||col.equals("itm_title")){
				   col="t"+col.substring(col.indexOf("_"));
			   }
			   page.sortCol1 = cwPagination.esc4SortSql(col);
			   //System.out.println("================sortCol1=="+page.sortCol1);
			   v=null;
		   }
		   if((v=(Vector)spec_pairs.get("sort_order"))!=null){
			   page.sortOrder=cwPagination.esc4SortSql((String)v.elementAt(0));
			   //System.out.println("===============sortOrder="+page.sortOrder);
			   v=null;
		   }
		   if((v=(Vector)spec_pairs.get("page_size"))!=null){
			   if(page.pageSize==0){
				   page.pageSize=Integer.parseInt((String)v.elementAt(0));
			   }		
			   //System.out.println("===============pageSize="+page.pageSize);
			   v=null;
		   }
		   String[] reportXML=null;
		   //split_ind==true, then get Learning Report into a number of small XMLs
		   //for tuning purpose when XML is too large (the case of download report)
		   //else, just get report into a single XML
		   if(split_ind) {
			   //Get report data as XML into a Vector
			   Vector v_reportData = getSplittedReport(con, sess, page, prof, env, ent_id_lst, ugr_ent_id_lst, itm_id_lst, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, att_create_start_datetime, att_create_end_datetime, att_start_datetime, att_end_datetime, app_status_lst, (Vector)spec_pairs.get("content_lst"), (Vector)spec_pairs.get("usr_content_lst"), (Vector)spec_pairs.get("itm_content_lst"), (Vector)spec_pairs.get("run_content_lst"), show_itm_credit, override_appr_usg, wizbini, include_no_record, show_stat_only, lrn_scope_sql);

			   //Construct the report data into a number of reportXML
			   reportXML = new String[v_reportData.size()];
			   for(int i=0; i<v_reportData.size(); i++) {
				   reportXML[i] = (String)v_reportData.elementAt(i);
			   }
		   } else {
			   reportXML = new String[1];
			   reportXML[0] = getReport(con, sess, page, prof, env, ent_id_lst, ugr_ent_id_lst, itm_id_lst, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, att_create_start_datetime, att_create_end_datetime, att_start_datetime, att_end_datetime, app_status_lst, (Vector)spec_pairs.get("content_lst"), (Vector)spec_pairs.get("usr_content_lst"), (Vector)spec_pairs.get("itm_content_lst"), (Vector)spec_pairs.get("run_content_lst"), show_itm_credit, override_appr_usg, wizbini, include_no_record, show_stat_only, lrn_scope_sql);
		   }
		   return reportXML;
	   } 
	 
	 
    public Vector getSplittedReport(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, String[] ent_id_lst, String[] ugr_ent_id_lst, long[] itm_id_lst, String[] tnd_id_lst, String itm_title, int itm_title_partial_ind, String[] ats_id_lst, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, Timestamp att_start_datetime, Timestamp att_end_datetime, String[] app_status_lst, Vector content_vec, Vector usr_content_vec, Vector itm_content_vec, Vector run_content_vec, boolean show_itm_credit, boolean override_appr_usg, WizbiniLoader wizbini, boolean include_no_record, boolean show_stat_only, String lrn_scope_sql) throws SQLException, qdbException, cwSysMessage, cwException, IOException {
        Vector v_reportData = new Vector();
        if (page.pageSize <= 0) {
            page.pageSize = LearnerReport.SPLITTED_REPORT_SIZE;
        }
        page.curPage = 1;
        cwPagination sess_page = null;
        do {
            v_reportData.addElement(getReport(con, sess, page, prof, env, ent_id_lst, ugr_ent_id_lst, itm_id_lst, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, att_create_start_datetime, att_create_end_datetime, att_start_datetime, att_end_datetime, app_status_lst, content_vec, usr_content_vec, itm_content_vec, run_content_vec, show_itm_credit, override_appr_usg, wizbini, include_no_record, show_stat_only, lrn_scope_sql));
            sess_page = (cwPagination)sess.getAttribute(SESS_LEARNER_REPORT_PAGINATION);
            page.ts = sess_page.ts;
            page.curPage++;
        }
        while (page.curPage <= sess_page.totalPage);
        return v_reportData;
    }

	public String[] getLearningReport(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long rte_id, String[] spec_name, String[] spec_value, long usr_ent_id, String rsp_title, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
		ViewReportSpec spec = new ViewReportSpec();
		ViewReportSpec.Data data = spec.getNewData();
		DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

		data.rte_id = rte_id;
		data.rsp_xml = rpt_spec.rsp_xml;
        
		StringBuffer prefixBuf = new StringBuffer(512);
		String xml_prefix = null;
		String xml_suffix = null;

		//if only one user is selected in this search
		//append an XML showing the user's current profile
		if (usr_ent_id > 0) {

			prefixBuf.append(getUserInfo(con, usr_ent_id));
			dbRegUser user = new dbRegUser();
			user.usr_ent_id = usr_ent_id;
			user.get(con);
			prefixBuf.append(user.getUserXML(con, prof, wizbini));
			xml_prefix = prefixBuf.toString();
		} else {
			Hashtable spec_pairs = getSpecPairs(data.rsp_xml);
			if (spec_pairs.containsKey("ent_id")) {
				Vector spec_values = (Vector)spec_pairs.get("ent_id");
				if(spec_values.size() == 1) {
					long data_ent_id = Long.parseLong((String)spec_values.elementAt(0));
					try {
						dbRegUser user = new dbRegUser();
						user.usr_ent_id = data_ent_id;
						user.get(con);
						xml_prefix = user.getUserXML(con, prof, wizbini);
					} catch(qdbException e) {
						//assume it is user not found exception
						//do nothing, just process
					}
				}
			}            
		}
        
		String[] reportXML = getLearningReportHelper(con, sess, page, prof, env, data, usr_ent_id, split_ind, override_appr_usg, wizbini); 

		for(int i=0; i<reportXML.length; i++) {
			reportXML[i] = super.getReport(con, request, prof, 0, rte_id, xml_prefix, reportXML[i], data.rsp_xml, RTE_TYPE_LEARNER_COS, rsp_title);
		}
		return reportXML;
	}
	
	public String getReport(
		Connection con,
		HttpSession sess,
		cwPagination page,
		loginProfile prof,
		qdbEnv env,
		String[] ent_id_lst,
		String[] ugr_ent_id_lst,
		long[] itm_id_lst,
		String[] tnd_id_lst,
		String itm_title,
		int itm_title_partial_ind,
		String[] ats_id_lst,
		Timestamp itm_start_datetime,
		Timestamp itm_end_datetime,
		Timestamp att_create_start_datetime,
		Timestamp att_create_end_datetime,
		Timestamp att_start_datetime,
		Timestamp att_end_datetime,
		String[] app_status_lst,
		Vector content_vec,
		Vector usr_content_vec,
		Vector itm_content_vec,
		Vector run_content_vec,
		boolean show_itm_credit,
		boolean override_appr_usg,
		WizbiniLoader wizbini, 
        boolean include_no_record, boolean show_stat_only, String lrn_scope_sql)
		throws SQLException, qdbException, cwSysMessage, cwException, IOException {
		StringBuffer result = new StringBuffer();
		if (!override_appr_usg
			&& (ent_id_lst == null || ent_id_lst.length == 0)
			&& dbUtils.isUserApprRole(con, prof.usr_ent_id, prof.current_role)) {
			ent_id_lst = getApprGroups(con, prof);
		}

		
		HashMap userData = null;
		Vector statusVec = null;
		Vector keyVec = null;
		StatisticBean reportSumm=null;
		Long dataKey=null;
			StringBuffer str_buf = new StringBuffer();
			Vector itm_id_vec = null;
			if (itm_id_lst != null) {
				itm_id_vec = new Vector();
				aeItem itm = null;
				Vector childItm;
				for (int i = 0; i < itm_id_lst.length; i++) {
					itm = new aeItem();
					itm.itm_id = itm_id_lst[i];
					if (itm.getCreateRunInd(con)) {
						childItm =
							aeItemRelation.getChildItemId(con, itm_id_lst[i]);
						for (int k = 0; k < childItm.size(); k++) {
							itm_id_vec.addElement((Long) childItm.elementAt(k));
						}
					}
					itm_id_vec.addElement(new Long(itm_id_lst[i]));
				}
			} else if (tnd_id_lst != null) {
				itm_id_vec = new Vector();
				aeTreeNode.getItemsFromNode(con, tnd_id_lst, itm_id_vec);
			}

			Vector ats_lst = null;
			if (ats_id_lst != null) {
				ats_lst = new Vector();
				for (int i = 0; i < ats_id_lst.length; i++) {
					ats_lst.addElement(ats_id_lst[i]);
				}
			}
//		Vector ats_attend_vec = aeAttendanceStatus.getAttendIdsVec(con, prof.root_ent_id);
			if (page.sortOrder == null) {
				page.sortOrder = DEFAULT_SORT_ORDER;
			}
			StringBuffer col_buf = new StringBuffer();
			col_buf.append(" ORDER BY ");
		col_buf.append(page.sortCol).append(" ").append(page.sortOrder).append(" , ").append(DEFAULT_SORT_COL_ORDER_2).append(" ").append(page.sortOrder);
//			ViewLearnerReport report = new ViewLearnerReport();
		userData = LearnerCosStatisticInfo.getStatisticInfo(con, ent_id_lst,
						itm_id_vec, ats_lst, att_create_start_datetime,
						att_create_end_datetime, col_buf.toString(), prof.usr_ent_id, prof.root_ent_id,
						include_no_record, show_stat_only, wizbini.cfgTcEnabled, lrn_scope_sql);
			
			statusVec = (Vector)userData.get("status");
			keyVec = (Vector) userData.get("keyVec");
			reportSumm=(StatisticBean)userData.get("reportSummary");
	
		Vector idVec = new Vector();
		Vector itmVec = new Vector();

		Hashtable itemCatalogXMLHash = aeItem.getCatalogXMLHash(con, keyVec);
		HashMap groupXMLHash = new HashMap();
		Hashtable recordXMLHash = null;
		HashMap idXMLHash = new HashMap();
		Vector groupVec = new Vector();
		Vector recordVec = null;
		Hashtable appHash = null;

		Timestamp cur_time = cwSQL.getTime(con);
		aeTemplate itmTemplate = null;
		aeTemplate runTemplate = null;
		DbTemplateView itmReportView = null;
		DbTemplateView runReportView = null;
		//get item tempalte view
		if (itm_content_vec != null && itm_content_vec.size() > 0) {
			DbObjectView itemView = DbObjectView.getObjectView(con,prof.root_ent_id, DbObjectView.OJV_TYPE_LEARNER_REPORT,DbObjectView.OJV_SUBTYPE_ITM);
			if (itemView != null) {
				itmReportView = createTemplateView(itm_content_vec,getItmResultView(itm_content_vec, itemView.getOptionXML()));
			}
		}
		//get run tempalte view
		if (run_content_vec != null && run_content_vec.size() > 0) {
			DbObjectView runView =
				DbObjectView.getObjectView(
					con,
					prof.root_ent_id,
					DbObjectView.OJV_TYPE_LEARNER_REPORT,
					DbObjectView.OJV_SUBTYPE_RUN);
			if (runView != null) {
				runReportView = createTemplateView(run_content_vec, getRunResultView(run_content_vec, runView.getOptionXML()));
			}
		}
		StringBuffer recordXML = null;
		StringBuffer statisInfo = null;
		Hashtable curUsrProfXML = null;
		int keySize = keyVec.size();
		for (int i = 0; i < keySize; i++) {
			recordXMLHash = new Hashtable();
			recordVec = new Vector();
			appHash = new Hashtable();

			dataKey = (Long) keyVec.elementAt(i);
			List dataList = (ArrayList) userData.get(dataKey);
			int size = dataList.size();
			//get the statistic info of the group
			StatisticBean sb = (StatisticBean) dataList.get(0);
			statisInfo = new StringBuffer();
			statisInfo.append(StatisticBean.getSummary(env.INI_DIR_UPLOAD_TMP,wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName(), prof.cur_lan, sb, statusVec,1,show_stat_only));
			for (int j = 1; j < size; j++) {
				ViewLearnerReport.Data d = (ViewLearnerReport.Data) dataList.get(j);
				idVec.addElement(new Long(d.itm_id));
				recordXML = new StringBuffer();
				//get application xml columns 
				//as they cannot be got from ViewLearnerReport.search
				//it is because xml column cannot be Union
				aeApplication app = null;
				if(d.app_id!=0){
				if ((usr_content_vec != null && usr_content_vec.size() > 0)
							|| (content_vec != null && content_vec
									.indexOf("app_ext4") > -1)) {
					app = new aeApplication();
					app.app_id = d.app_id;
					app.get(con);
				}
				}

				//get historical user profile XML.
				if (d.usr_ent_id!=0 && d.att_ats_id!=0 && d.att_create_timestamp!=null&&usr_content_vec != null && usr_content_vec.size() > 0) {
					if (app != null && app.app_usr_prof_xml != null) {
						recordXML.append(app.app_usr_prof_xml);
					} else {
						//if cannot find historial user profile XML
						//get the current user profile XML
						if (curUsrProfXML == null) {
							curUsrProfXML = new Hashtable();
						}
						Long key = new Long(d.usr_ent_id);
						String profXML = (String) curUsrProfXML.get(key);
						if (profXML == null) {
							dbRegUser applicant = new dbRegUser();
							applicant.usr_ent_id = d.usr_ent_id;
							applicant.get(con);
							profXML = applicant.getUserXML(con, prof, wizbini);
							curUsrProfXML.put(key, profXML);
						}
						recordXML.append(profXML);
					}
				}

				//get item details
				if (itm_content_vec != null
					&& itm_content_vec.size() > 0
					&& env != null) {
					recordXML.append("<item>");
					aeItem item = new aeItem();
					item.itm_id = d.t_id;
					item.get(con);
					if (itmTemplate == null) {
						itmTemplate = item.getItemTemplate(con);
					}
					recordXML.append(item.getValuedTemplate(con, itmTemplate,
							itmReportView, false, cur_time, env,
							prof.usr_ent_id));
					recordXML.append("</item>");
				}

				//get run details
				if (run_content_vec != null
					&& run_content_vec.size() > 0
					&& env != null
					&& d.t_id != d.itm_id) {
					recordXML.append("<run>");
					aeItem run = new aeItem();
					run.itm_id = d.itm_id;
					run.get(con);
					if (runTemplate == null) {
						runTemplate = run.getItemTemplate(con);
					}
					recordXML.append(run.getValuedTemplate(con, runTemplate,
							runReportView, false, cur_time, env,
							prof.usr_ent_id));
					recordXML.append("</run>");
				}
                if(d.usr_ent_id!=0 && d.att_ats_id!=0 && d.att_create_timestamp!=null){
				recordXML
					.append("<student id=\"")
					.append(d.usr_ste_usr_id)
					.append("\" last_name=\"")
					.append(
						cwUtils.esc4XML(aeUtils.escNull(d.usr_last_name_bil)))
					.append("\" first_name=\"")
					.append(
						cwUtils.esc4XML(aeUtils.escNull(d.usr_first_name_bil)))
					.append("\" display_name=\"")
					.append(cwUtils.esc4XML(aeUtils.escNull(d.usr_display_bil)))
					.append("\" ent_id=\"")
					.append(d.usr_ent_id)
					.append("\"/>")
					.append(cwUtils.NEWL);
                }
				recordXML
					.append("<item id=\"")
					.append(d.t_id)
					.append("\" code=\"")
					.append(cwUtils.esc4XML(aeUtils.escNull(d.t_code)))
					.append("\" title=\"")
					.append(cwUtils.esc4XML(aeUtils.escNull(d.t_title)))
					.append("\" type=\"")
					.append(cwUtils.escNull(d.t_type))
					.append("\" course_mod_cnt=\"")
					.append(0)
					.append("\" course_id=\"")
					.append(0)
					.append("\" eff_start_datetime=\"")
					.append(aeUtils.escNull(d.t_eff_start_datetime))
					.append("\" eff_ent_datetime=\"")
					.append(aeUtils.escNull(d.t_eff_end_datetime))
					.append("\" nature=\"")
					.append(aeUtils.escNull(d.t_apply_method))
					.append("\" unit=\"")
					.append(d.t_unit)
					.append("\" ");

				recordXML.append(">").append(cwUtils.NEWL);

				aeItem item = new aeItem();
				item.itm_id = d.t_id;
				recordXML.append(
					itemCatalogXMLHash.get(new Long(d.t_id))).append(
					cwUtils.NEWL);
				recordXML.append("</item>").append(cwUtils.NEWL);

				// added for certificate
				recordXML.append(
					checkCertificateStatus(con, item, d.usr_ent_id));

				//get application details
				if(d.usr_ent_id!=0 && d.att_ats_id!=0 && d.att_create_timestamp!=null){
				recordXML
					.append("<application id=\"")
					.append(d.app_id)
					.append("\" app_tkh_id=\"")
					.append(d.app_tkh_id)
					.append("\" datetime=\"")
					.append(aeUtils.escNull(d.app_create_timestamp))
					.append("\" process_status=\"")
					.append(
						cwUtils.esc4XML(aeUtils.escNull(d.app_process_status)))
					.append("\" queue_status=\"")
					.append(cwUtils.esc4XML(aeUtils.escNull(d.app_status)))
					.append("\" app_itm_id=\"")
					.append(d.itm_id)
					.append("\" itm_eff_start_datetime=\"")
					.append(aeUtils.escNull(d.itm_eff_start_datetime))
					.append("\" itm_eff_end_datetime=\"")
					.append(aeUtils.escNull(d.itm_eff_end_datetime))
					.append("\" itm_title=\"")
					.append(cwUtils.esc4XML(aeUtils.escNull(d.itm_title)))
					.append("\">");
				if (content_vec != null
					&& content_vec.indexOf("app_ext4") > -1) {
					recordXML.append("<app_ext4>").append(
						cwUtils.escNull(app.app_ext4)).append(
						"</app_ext4>");
				}
				recordXML.append("</application>");

				String total_time = "";

				if (d.cov_total_time != 0) {
					total_time = dbAiccPath.getTime(d.cov_total_time);
				}

				String score = null;

				if (d.cov_score != null) {
					if (d.cov_max_score == null) {
						d.cov_max_score = "100";
					}

					//round the score to 2 decimal place
					float percent =
						(new Float(d.cov_score)).floatValue()
							/ (new Float(d.cov_max_score)).floatValue();
					float temp = (new Float(percent * 10000)).longValue();
					float temp2 = temp / (new Float(100)).floatValue();
					score = (new Float(temp2)).toString();
				}

				recordXML
					.append("<aicc_data course_id=\"")
					.append(d.cos_res_id)
					.append("\" student_id=\"")
					.append(d.usr_ent_id)
					.append("\" last_acc_datetime=\"")
					.append(aeUtils.escNull(d.cov_last_acc_datetime))
					.append("\" commence_datetime=\"")
					.append(aeUtils.escNull(d.cov_commence_datetime))
					.append("\" used_time=\"")
					.append(total_time)
					.append("\" attempt=\"")
					.append(d.totalAttempt)
					.append("\" status=\"")
					.append(aeUtils.escNull(d.cov_status))
					.append("\" score=\"")
					.append(aeUtils.escNull(score))
					.append("\" comment=\"")
					.append(aeUtils.escNull(d.cov_comment))
					.append("\" completion_datetime=\"")
					.append(aeUtils.escNull(d.cov_complete_datetime))
					.append("\"/>")
					.append(cwUtils.NEWL);
				recordXML.append("<attendance datetime=\"").append(
					aeUtils.escNull(d.att_timestamp));
				recordXML.append("\" create_date=\"").append(
					aeUtils.escNull(d.att_create_timestamp));
				recordXML.append("\" status=\"");

				if (d.att_ats_id != 0) {
					recordXML.append(d.att_ats_id);
				}

				recordXML.append("\" remark=\"").append(
					cwUtils.esc4XML(cwUtils.escNull(d.att_remark)));
				recordXML.append("\" rate=\"").append(
					cwUtils.esc4XML(cwUtils.escNull(d.att_rate)));
				recordXML.append("\"/>").append(cwUtils.NEWL);
				recordXML.append("<group_list>");
				recordXML.append("<group>").append(cwUtils.esc4XML(cwUtils.escNull(d.group_name)))
				  		 .append("</group>");
				recordXML.append("</group_list>");
				recordXML.append("<grade_list>");
				recordXML.append("<group>").append(cwUtils.esc4XML(cwUtils.escNull(d.grade_name)))
				  		 .append("</group>");
				recordXML.append("</grade_list>");
				recordVec.addElement(new Long(d.app_id));
				recordXMLHash.put(new Long(d.app_id), recordXML);
				appHash.put(new Long(d.app_id), new Long(d.usr_ent_id));
				}
			}
			
			// end of a section of a record, assemble xml
			recordXMLHash.put("statisInfo", statisInfo.toString());
			groupXMLHash.put(dataKey, recordXMLHash);
			idXMLHash.put(dataKey, recordVec);
			groupVec.addElement(dataKey);
		}
		result.append("<report_list>");
	    result.append(StatisticBean.getSummary(env.INI_DIR_UPLOAD_TMP,wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName(), prof.cur_lan, reportSumm,statusVec,2,show_stat_only));

			ViewLearnerReport.Data rData;
			String t_code ;
			String t_title;
			try{
			for (int groupIndex = 0; groupIndex < groupVec.size(); groupIndex++) {
				Long groupKey = (Long) groupVec.elementAt(groupIndex);
				Hashtable records = (Hashtable) groupXMLHash.get(groupKey);
				Vector app_ids = (Vector) idXMLHash.get(groupKey);
				//start a group
				result.append("<report_group>").append(cwUtils.NEWL);
				StatisticBean sb = (StatisticBean)((ArrayList)userData.get(groupKey)).get(0);
				t_code = sb.t_code;
				t_title = sb.t_title;
				result.append("<item code=\"").append(cwUtils.esc4XML(t_code)).append("\" >");
				result.append("<title>").append(cwUtils.esc4XML(t_title)).append("</title>");
				result.append("</item>").append(cwUtils.NEWL);
				result.append((String) records.get("statisInfo"));
				int recordSize = app_ids.size();
                if (!show_stat_only) {
                    for (int index = 0; index < recordSize; index++) {
                        Long appID = (Long) app_ids.elementAt(index);
                        result.append("<record>").append(cwUtils.NEWL);
                        result.append(records.get(appID));
                        result.append("</record>");
                    }
                }
				//end of a group 
				result.append("</report_group>");
			}
			}catch(Exception e){
				CommonLog.error(e.getMessage(),e);
			}
		result.append("</report_list>");
		result.append("<item_credits>");
		StringBuffer xml = new StringBuffer();
		if (show_itm_credit) {
			StringBuffer itmXml = new StringBuffer();
			long prv_itm_id = 0;
			long cur_itm_id = 0;
			if (itmXml.length() > 0) {
				xml.append(itmXml).append(cwUtils.NEWL);
				xml.append("</item>").append(cwUtils.NEWL);
			}
		}
		result.append(xml).append(cwUtils.NEWL);
		result.append("</item_credits>").append(cwUtils.NEWL);
		return result.toString();
	}
	public static String getUserInfo(Connection con, long usr_ent_id)
		throws SQLException, qdbException {
		StringBuffer result = new StringBuffer();
		dbRegUser user = new dbRegUser();

		user.usr_ent_id = usr_ent_id;
		user.get(con);
		result
			.append("<student id=\"")
			.append(user.usr_ste_usr_id)
			.append("\" last_name=\"")
			.append(cwUtils.esc4XML(aeUtils.escNull(user.usr_last_name_bil)))
			.append("\" first_name=\"")
			.append(cwUtils.esc4XML(aeUtils.escNull(user.usr_first_name_bil)))
			.append("\" display_bil=\"")
			.append(cwUtils.esc4XML(aeUtils.escNull(user.usr_display_bil)))
			.append("\" ent_id=\"")
			.append(usr_ent_id)
			.append("\"/>")
			.append(cwUtils.NEWL);

		return result.toString();
	}

	public static String getMetaData(Connection con, loginProfile prof, WizbiniLoader wizbini)
		throws SQLException, cwSysMessage {
		StringBuffer result = new StringBuffer();

		result
			.append("<cur_time>")
			.append(cwSQL.getTime(con))
			.append("</cur_time>")
			.append(cwUtils.NEWL);
		result.append(getAttendanceStatusList(con, prof.root_ent_id));

		// added for certificate
		result.append(getCertificateStatusList(con));

		result.append(getCatalogTitleList(con, prof,  wizbini));

		// added for item types
		result.append(aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id));

		// added for role labels
		result.append(
			dbUtils.getAllRoleAsXML(con, "role_list", prof.root_ent_id));

		return result.toString();
	}
 }
