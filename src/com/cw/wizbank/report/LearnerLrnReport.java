package com.cw.wizbank.report;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author donaldl
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LearnerLrnReport extends LearnerReport {
	public static final String RTE_TYPE_LEARNER_LRN = "LEARNING_ACTIVITY_LRN";
	public static final String DEFAULT_SORT_COL_ORDER_2 ="usr_ent_id";
	public String getReportTemplate(
		Connection con,
		HttpServletRequest request,
		loginProfile prof,
		long usr_ent_id,
		long rsp_id,
		long rte_id)
		throws
			SQLException,
			qdbException,
			cwException,
			IOException,
			MalformedURLException,
			cwSysMessage {
		String xml_prefix = null;

		if (usr_ent_id > 0) {
			xml_prefix = getUserInfo(con, usr_ent_id);
		}

		return super.getReport(
			con,
			request,
			prof,
			rsp_id,
			rte_id,
			xml_prefix,
			null,
			null,
			RTE_TYPE_LEARNER_LRN,
			null);
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
	    		   if(((String)spec_values.get(0)).equals("1") && v_ent_id <= 0) {
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
//					Vector vec = new Vector();
					if((answer_for_lrn && answer_for_course_lrn )||(!answer_for_lrn && !answer_for_course_lrn )) {
//						vec = ViewLearnerReport.getAllLrn(con, prof.usr_ent_id, prof.root_ent_id);
						lrn_scope_sql = ViewLearnerReport.getAllLrnSql(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
					} else if (answer_for_lrn && !answer_for_course_lrn) {
//						vec = ViewLearnerReport.getMyRspLrn(con, prof.usr_ent_id, prof.root_ent_id);
						lrn_scope_sql = ViewLearnerReport.getMyRspLrnSql(Report.COLNAME_TMP_USR_ENT_ID ,prof.usr_ent_id, prof.root_ent_id);
					} else if (!answer_for_lrn && answer_for_course_lrn) {
//						vec = ViewLearnerReport.getMyRspCosEnrollLrn(con, prof.usr_ent_id, prof.root_ent_id);
						lrn_scope_sql = ViewLearnerReport.getMyRspCosEnrollLrn(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
					}
				}
				if (!spec_pairs.containsKey("tnd_id") && !spec_pairs.containsKey("itm_id")) {
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
	        if(v_ent_id > 0){
	        	show_stat_only = false;
	        }else {
	        	show_stat_only = true;
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

			if(v_ent_id > 0){
				ent_id_lst = new String[1];
				ent_id_lst[0] = String.valueOf(v_ent_id);
			  }
			
			
			if (spec_pairs.containsKey("tnd_id")) {
				spec_values = (Vector)spec_pairs.get("tnd_id");

				tnd_id_lst = new String[spec_values.size()];

				for (int i=0; i<spec_values.size(); i++) {
					tnd_id_lst[i] = (String)spec_values.elementAt(i);
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
				// determine "ALL"
				if(((String)spec_values.elementAt(0)).equals("0")){
					   Vector vID = aeAttendanceStatus.getAllStatusIdByRoot(con, prof.root_ent_id);
					   ats_id_lst=new String[vID.size()];
					try{
						  for(int i=0;i<vID.size();i++){
							 ats_id_lst[i]=((Integer)vID.get(i)).toString();;
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

			if (spec_pairs.containsKey("att_create_start_datetime")) {
				spec_values = (Vector)spec_pairs.get("att_create_start_datetime");
				att_create_start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
			}
			if (spec_pairs.containsKey("att_create_end_datetime")) {
				spec_values = (Vector)spec_pairs.get("att_create_end_datetime");

				att_create_end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
			}
			if (spec_pairs.containsKey("att_start_datetime")) {
				spec_values = (Vector)spec_pairs.get("att_start_datetime");
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
			Vector v=null;
			if((v=(Vector)spec_pairs.get("sort_col"))!=null){
				String col=(String)v.elementAt(0);
				if(col.equals("itm_code")||col.equals("itm_title")){
					col="t"+col.substring(col.indexOf("_"));
				}
				page.sortCol=cwPagination.esc4SortSql(col);
				v=null;
			}
			if((v=(Vector)spec_pairs.get("sort_col1"))!=null){
				String col = (String) v.elementAt(0);
				if(col.equals("itm_code")||col.equals("itm_title")){
					col="t"+col.substring(col.indexOf("_"));
				}
				page.sortCol1 = cwPagination.esc4SortSql(col);
				v=null;
			}
			if((v=(Vector)spec_pairs.get("sort_order"))!=null){
				page.sortOrder=cwPagination.esc4SortSql((String)v.elementAt(0));
				v=null;
			}
			if((v=(Vector)spec_pairs.get("page_size"))!=null){
				if(page.pageSize==0){
					page.pageSize=Integer.parseInt((String)v.elementAt(0));
				}		
				v=null;
			}
			String[] reportXML=null;
			if(split_ind) {
				//Get report data as XML into a Vector
				Vector v_reportData = getSplittedReport(con, sess, page, prof, env, ent_id_lst, ugr_ent_id_lst, itm_id_lst, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, att_create_start_datetime, att_create_end_datetime, att_start_datetime, att_end_datetime, app_status_lst, (Vector)spec_pairs.get("content_lst"), (Vector)spec_pairs.get("usr_content_lst"), (Vector)spec_pairs.get("itm_content_lst"), (Vector)spec_pairs.get("run_content_lst"), show_itm_credit, override_appr_usg, wizbini, include_no_record, show_stat_only,lrn_scope_sql);

				//Construct the report data into a number of reportXML
				reportXML = new String[v_reportData.size()];
				for(int i=0; i<v_reportData.size(); i++) {
					reportXML[i] = (String)v_reportData.elementAt(i);
				}
			} else {
				reportXML = new String[1];
				reportXML[0] = getReport(con, sess, page, prof, env, ent_id_lst, itm_id_lst, tnd_id_lst, ats_id_lst, att_create_start_datetime, att_create_end_datetime,override_appr_usg, wizbini, include_no_record, show_stat_only, lrn_scope_sql);
			}
			return reportXML;
		}

        public Vector getSplittedReport(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, String[] ent_id_lst, String[] ugr_ent_id_lst, long[] itm_id_lst, String[] tnd_id_lst, String itm_title, int itm_title_partial_ind, String[] ats_id_lst, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, Timestamp att_start_datetime, Timestamp att_end_datetime, String[] app_status_lst, Vector content_vec, Vector usr_content_vec, Vector itm_content_vec, Vector run_content_vec, boolean show_itm_credit, boolean override_appr_usg, WizbiniLoader wizbini, boolean include_no_record, boolean show_stat_only,String lrn_scope_sql) throws SQLException, qdbException, cwSysMessage, cwException, IOException {
            Vector v_reportData = new Vector();
            if (page.pageSize <= 0) {
                page.pageSize = LearnerReport.SPLITTED_REPORT_SIZE;
            }
            page.curPage = 1;
            cwPagination sess_page = null;
            do {
                v_reportData.addElement(getReport(con, sess, page, prof, env, ent_id_lst, itm_id_lst, tnd_id_lst, ats_id_lst, att_create_start_datetime, att_create_end_datetime,override_appr_usg, wizbini, include_no_record, show_stat_only,lrn_scope_sql));
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
					reportXML[i] = super.getReport(con, request, prof, 0, rte_id, xml_prefix, reportXML[i], data.rsp_xml, RTE_TYPE_LEARNER_LRN, rsp_title);
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
		long[] itm_id_lst,
		String[] tnd_id_lst,
		String[] ats_id_lst,
		Timestamp att_create_start_datetime,
		Timestamp att_create_end_datetime,
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
		
		StringBuffer str_buf = new StringBuffer();
		str_buf = new StringBuffer();
		Vector itm_id_vec = null;
		if (itm_id_lst != null) {
			itm_id_vec = new Vector();
			aeItem itm = null;
			Vector childItm;
			for (int i = 0; i < itm_id_lst.length; i++) {
				itm = new aeItem();
				itm.itm_id = itm_id_lst[i];
				if (itm.getCreateRunInd(con)) {
					childItm = aeItemRelation.getChildItemId(con, itm_id_lst[i]);
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

		if (page.sortOrder == null) {
			page.sortOrder = DEFAULT_SORT_ORDER;
		}
		StringBuffer col_buf = new StringBuffer();
		col_buf.append(" ORDER BY ");
		col_buf.append(page.sortCol).append(" ").append(page.sortOrder).append(" , ").append(DEFAULT_SORT_COL_ORDER_2).append(" ").append(page.sortOrder);
		userData = LearnerLrnStatisticInfo.getStatisticInfo(con, ent_id_lst,itm_id_vec, ats_lst, att_create_start_datetime,
				att_create_end_datetime, col_buf.toString(),prof.usr_ent_id, prof.root_ent_id,
				include_no_record, show_stat_only, wizbini.cfgTcEnabled, lrn_scope_sql,v_ent_id,is_realTime_view_rpt);
			
		statusVec = (Vector)userData.get("status");
		keyVec = (Vector) userData.get("keyVec");
		reportSumm=(StatisticBean)userData.get("reportSummary");

		result.append("<report_list>");
		result.append("<show_stat_only>").append(show_stat_only).append("</show_stat_only>").append(cwUtils.NEWL);
		result.append(StatisticBean.getSummary(env.INI_DIR_UPLOAD_TMP, wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName(),prof.label_lan, reportSumm,statusVec,2,show_stat_only));
		try{
			for (int i = 0; i < keyVec.size(); i++) {
				Long usr_ent_id = (Long) keyVec.elementAt(i);
				List dataList = (ArrayList) userData.get(usr_ent_id);
				
				//start a group
				StatisticBean stb=(StatisticBean)((ArrayList) userData.get(usr_ent_id)).get(0);
				result.append("<report_group>");
				String usr_ste_usr_id =stb.u_login_id;
				String usr_displayname = stb.u_display;
				result.append("<user id=\"").append(cwUtils.esc4XML(usr_ste_usr_id)).append("\" usr_ent_id=\"").append(usr_ent_id).append("\" >").append(cwUtils.NEWL);;
				result.append("<display_name>").append(cwUtils.esc4XML(usr_displayname)).append("</display_name>").append(cwUtils.NEWL);
				result.append("</user>").append(cwUtils.NEWL);
				
				StatisticBean sb = (StatisticBean) dataList.get(0);
				result.append(StatisticBean.getSummary(env.INI_DIR_UPLOAD_TMP, wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName(), prof.label_lan, sb,statusVec,1,show_stat_only));
				
                if (!show_stat_only) {
                    for (int index = 1; index < dataList.size(); index++) {
                    	ViewLearnerReport.Data d = (ViewLearnerReport.Data) dataList.get(index);
                        result.append("<record>");
                        
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
        					float percent =	(new Float(d.cov_score)).floatValue()/ (new Float(d.cov_max_score)).floatValue();
        					float temp = (new Float(percent * 10000)).longValue();
        					float temp2 = temp / (new Float(100)).floatValue();
        					score = (new Float(temp2)).toString();
        				}
        				Vector itmVec = new Vector();
        				itmVec.add(d.t_id);
        				Hashtable itemCatalogXMLHash = aeItem.getCatalogXMLHash(con, itmVec);
                        result
    					.append("<item id=\"").append(d.t_id)
    					.append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.t_code)))
    					.append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.t_title)))
    					.append("\" type=\"").append(cwUtils.escNull(d.t_type))
    					.append("\" dummy_type=\"").append(cwUtils.escNull(d.itm_dummy_type))
    					.append("\" tcr_title=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.tcr_title)))
    					.append("\" att_create_timestamp=\"").append(aeUtils.escNull(d.att_create_timestamp))
    					.append("\" att_timestamp=\"").append(aeUtils.escNull(d.att_timestamp))
    					.append("\" cov_commence_datetime=\"").append(aeUtils.escNull(d.cov_commence_datetime))
    					.append("\" cov_last_acc_datetime=\"").append(aeUtils.escNull(d.cov_last_acc_datetime))
    					.append("\" attempt=\"").append(d.totalAttempt)
    					.append("\" att_ats_id=\"").append(d.att_ats_id)
    					.append("\" used_time=\"").append(cwUtils.esc4XML(aeUtils.escNull(total_time)))
    					.append("\" score=\"").append(aeUtils.escNull(score))
    					.append("\"/>")
    					.append(cwUtils.NEWL);
                        result.append(itemCatalogXMLHash.get(new Long(d.t_id))).append(cwUtils.NEWL);
                        result.append("</record>");
                    }
                }
				result.append("</report_group>");
			}
			}catch(Exception e){
				CommonLog.error(e.getMessage(),e);
			}
		result.append("</report_list>");
		result.append(page.asXML()).append(cwUtils.NEWL);

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
			.append("\"/>");

		return result.toString();
	}
}
