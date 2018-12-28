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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.report.ViewExamPaperStatReport.ModInfo;
import com.cw.wizbank.report.ViewExamPaperStatReport.SummaryStat;
import com.cw.wizbank.report.ViewExamPaperStatReport.TestInfo;
import com.cw.wizbank.report.ViewExamPaperStatReport.UsrInfo;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

public class ExamPaperStatReport extends ReportTemplate{
	public static final String RTE_TYPE_EXAM_PAPER_STAT = "EXAM_PAPER_STAT";
	static final String DEFAULT_SORT_ORDER = "DESC";
	
	public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long usr_ent_id, long rsp_id, long rte_id) throws SQLException, qdbException, cwException, IOException, MalformedURLException, cwSysMessage {
		String xml_prefix = null;

		if (usr_ent_id > 0) {
			xml_prefix = getUserInfo(con, usr_ent_id);
		}

		return super.getReport(con, request, prof, rsp_id, rte_id, xml_prefix, null, null, RTE_TYPE_EXAM_PAPER_STAT, null);
	 }
	 
	public String[] getExamPaperStatReportHelper(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, ViewReportSpec.Data data, long usr_ent_id, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException {
        		
	   Hashtable spec_pairs = getSpecPairs(data.rsp_xml);

	   String[] ent_id_lst = null;
	   String[] s_usg_ent_id_lst = null;
	   String[] tnd_id_lst = null;
	   long[] itm_id_lst = null;
	   String[] mod_id_lst = null;
	   Timestamp att_start_datetime = null;
	   Timestamp att_end_datetime = null;
	   boolean show_stat_only = false;
	   Vector spec_values;
	   String lrn_scope_sql = null;
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
//    		   Vector vec = new Vector();
    		   if((answer_for_lrn && answer_for_course_lrn )||(!answer_for_lrn && !answer_for_course_lrn )) {
//    			   vec = ViewLearnerReport.getAllLrn(con, prof.usr_ent_id, prof.root_ent_id);
    			   lrn_scope_sql = ViewLearnerReport.getAllLrnSql(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
    		   } else if (answer_for_lrn && !answer_for_course_lrn) {
//    			   vec = ViewLearnerReport.getMyRspLrn(con, prof.usr_ent_id, prof.root_ent_id);
    			   lrn_scope_sql = ViewLearnerReport.getMyRspLrnSql(Report.COLNAME_TMP_USR_ENT_ID ,prof.usr_ent_id, prof.root_ent_id);
    		   } else if (!answer_for_lrn && answer_for_course_lrn) {
//    			   vec = ViewLearnerReport.getMyRspCosEnrollLrn(con, prof.usr_ent_id, prof.root_ent_id);
    			   lrn_scope_sql = ViewLearnerReport.getMyRspCosEnrollLrn(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
    		   } 
/*    		   if(vec.size() == 0) {
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
	   
	   if (spec_pairs.containsKey("mod_id")) {
		   spec_values = (Vector)spec_pairs.get("mod_id");
		   mod_id_lst = new String[spec_values.size()];
		   for(int i=0; i<spec_values.size(); i++) {
			   mod_id_lst[i] = (String)spec_values.elementAt(i);
		   }
	   }

	   Vector v=null;
	   if((v=(Vector)spec_pairs.get("sort_col"))!=null){
		   String col=(String)v.elementAt(0);
		   if(col.equals("itm_code")||col.equals("itm_title")){
			   col="t"+col.substring(col.indexOf("_"));
		   }
		   page.sortCol=col;
		   v=null;
	   }
	   if((v=(Vector)spec_pairs.get("sort_order"))!=null){
		   page.sortOrder=(String)v.elementAt(0);
		   v=null;
	   }

	   String[] reportXML=null;
	   reportXML = new String[1];
	   reportXML[0] = getReport(con, sess, page, prof, env, ent_id_lst, itm_id_lst, tnd_id_lst, mod_id_lst,  
			   att_start_datetime, att_end_datetime, (Vector)spec_pairs.get("content_lst"), (Vector)spec_pairs.get("usr_content_lst"), 
			   (Vector)spec_pairs.get("itm_content_lst"), (Vector)spec_pairs.get("run_content_lst"), wizbini, show_stat_only, lrn_scope_sql);
	   return reportXML;
   } 
	 
    public String[] getExamPaperStatReport(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long rte_id, String[] spec_name, String[] spec_value, long usr_ent_id, String rsp_title, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;
        
        StringBuffer prefixBuf = new StringBuffer(512);
        String xml_prefix = null;

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
        
        String[] reportXML = getExamPaperStatReportHelper(con, sess, page, prof, env, data, usr_ent_id, split_ind, override_appr_usg, wizbini); 

        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, 0, rte_id, xml_prefix, reportXML[i], data.rsp_xml, RTE_TYPE_EXAM_PAPER_STAT, rsp_title);
        }
        return reportXML;
    }
    
    public String[] getExamPaperStatReport(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long rsp_id, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);

        //if only one user is selected in this search
        //append an XML showing the user's current profile
        Hashtable spec_pairs = getSpecPairs(data.rsp_xml);
        if (spec_pairs.containsKey("ent_id")) {
            Vector spec_values = (Vector)spec_pairs.get("ent_id");
            if(spec_values.size() == 1) {
                long data_ent_id = Long.parseLong((String)spec_values.elementAt(0));
                try {
                    dbRegUser user = new dbRegUser();
                    user.usr_ent_id = data_ent_id;
                    user.get(con);
                } catch(qdbException e) {
                    //assume it is user not found exception
                    //do nothing, just process
                }
            }
        }            
        
        String[] reportXML = getExamPaperStatReportHelper(con, sess, page, prof, env, data, 0, split_ind, override_appr_usg, wizbini);

        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, rsp_id, 0, null, reportXML[i], null, RTE_TYPE_EXAM_PAPER_STAT, null);
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
		String[] mod_id_lst,
		Timestamp att_start_datetime,
		Timestamp att_end_datetime,
		Vector content_vec,
		Vector usr_content_vec,
		Vector itm_content_vec,
		Vector run_content_vec,
		WizbiniLoader wizbini, 
        boolean show_stat_only, String lrn_scope_sql)
		throws SQLException, qdbException, cwSysMessage, cwException, IOException {
		StringBuffer result = new StringBuffer();
		
		Map modHash = setModIdToList(con, itm_id_lst, tnd_id_lst, mod_id_lst, prof);
		List modIds = (List)modHash.get("modIds");
		int itmCnt = ((Integer)modHash.get("itmCnt")).intValue();
		
		if (page.sortOrder == null) {
			page.sortOrder = DEFAULT_SORT_ORDER;
		}
		StringBuffer col_buf = new StringBuffer();
		col_buf.append(" ORDER BY ");
		col_buf.append(page.sortCol).append(" ").append(page.sortOrder);
		
		ViewExamPaperStatReport viewExam = new ViewExamPaperStatReport();
		HashMap testData = viewExam.getTestData(con, ent_id_lst, itmCnt, modIds, att_start_datetime, att_end_datetime, 
								col_buf.toString(), show_stat_only, true, null, lrn_scope_sql);
		
		if (testData != null) {
			setRptXml(testData, show_stat_only, env.INI_DIR_UPLOAD_TMP, wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName(), 
						prof.cur_lan, result, viewExam);
		}
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
	
	//return mod id list and parent itm id count
	public static Map setModIdToList(Connection con, long[] itm_id_lst, String[] tnd_id_lst, String[] mod_id_lst, loginProfile prof) throws NumberFormatException, SQLException {
		Map modHash = new HashMap();
		List itmIds = null;
		List modIds = null;
		int i, k, itmCnt;
		if (itm_id_lst != null) {
			itmIds= new ArrayList();
			aeItem itm = new aeItem();
			Vector childItm;
			for (i = 0; i < itm_id_lst.length; i++) {
				itm.itm_id = itm_id_lst[i];
				if (itm.getCreateRunInd(con)) {
					childItm = aeItemRelation.getChildItemId(con, itm_id_lst[i]);
					for (k = 0; k < childItm.size(); k++) {
						itmIds.add((Long) childItm.get(k));
					}
				}
				itmIds.add(new Long(itm_id_lst[i]));
			}
		} else if (tnd_id_lst != null) {
			itmIds = new ArrayList();
			aeTreeNode.getItemsFromNode(con, tnd_id_lst, itmIds);
		}
		
		if (mod_id_lst != null) {
			modIds = cwUtils.string2ArrayList(mod_id_lst);
			//指定测验条件只能选择一个课程中的模块
			itmCnt = 1;
		} else {
			String[] res_type = new String[]{dbModule.MOD_TYPE_TST, dbModule.MOD_TYPE_DXT};
			modIds = dbResource.getResIdByItmIds(con, itmIds, res_type);
			//统计课程数
			itmCnt = aeItem.getCourseCnt(con, itmIds);
		}
		modHash.put("itmCnt", new Integer(itmCnt));
		modHash.put("modIds", modIds);
		return modHash;
	}
	
	private void setRptXml(HashMap testData, boolean show_stat_only, String chartImgSavedPath, String tempPathName, String cur_lan, 
							StringBuffer result, ViewExamPaperStatReport viewExam) throws IOException {
		Map tstHash = (Map)testData.get(ViewExamPaperStatReport.TST_HASH);
		Map usrHash = (Map) testData.get(ViewExamPaperStatReport.USR_HASH);
		Map viewHash = (Map)testData.get(ViewExamPaperStatReport.VIEW_HASH);
		List orderLst = (List)testData.get(ViewExamPaperStatReport.ORDER_LST);
		SummaryStat sumStat =(SummaryStat)testData.get(ViewExamPaperStatReport.SUMMARY_STAT);
		
		Long modIdObj = null;
		List usrIds = null;
		Long usrIdObj = null;
		TestInfo tstInfo = null;
		UsrInfo usrInfo = null;
		ModInfo modInfo = null;

		result.append("<report_list>").append(cwUtils.NEWL);
		result.append("<report_summary>").append(cwUtils.NEWL);
		result.append("<itmCnt>").append(sumStat.itmCnt).append("</itmCnt>")
			  .append("<tstCnt>").append(sumStat.tstCnt).append("</tstCnt>")
			  .append("<usrCnt>").append(sumStat.usrCnt).append("</usrCnt>")
			  .append("<examineeCnt>").append(sumStat.examineeCnt).append("</examineeCnt>");
		result.append("</report_summary>").append(cwUtils.NEWL);
		int usr_cnt, i, j;
		int pageSize = viewHash.size();
		for (j = 0; j < pageSize && j < orderLst.size(); j++) {
			modIdObj = (Long)orderLst.get(j);
			usrIds = (List)viewHash.get(modIdObj);
			tstInfo = (TestInfo)tstHash.get(modIdObj);
			usr_cnt = tstInfo.usrLst.size();
			
			result.append("<report_group>").append(cwUtils.NEWL);
			result.append("<itm_title>").append(cwUtils.esc4XML(tstInfo.itm_title)).append("</itm_title>")
				  .append("<res_title>").append(cwUtils.esc4XML(tstInfo.res_title)).append("</res_title>");
			
			if (usr_cnt > 0) {
				result.append("<group_summary>").append(cwUtils.NEWL)
					  .append("<mod_max_score>").append(tstInfo.mod_max_score).append("</mod_max_score>")
					  .append("<average_score>").append(cwUtils.formatNumber(tstInfo.usr_total_score / (float)usr_cnt, 1)).append("</average_score>")
					  .append("<pass_rate>").append(cwUtils.formatNumber(tstInfo.usr_pass_cnt / (float)usr_cnt * 100, 1)).append("%").append("</pass_rate>")
					  .append("<max_score>").append(tstInfo.usr_max_score).append("</max_score>")
					  .append("<least_score>").append(tstInfo.usr_least_score).append("</least_score>")
					  .append("<pass_cnt>").append(tstInfo.usr_pass_cnt).append("</pass_cnt>")
					  .append("<examineeCnt>").append(usr_cnt).append("</examineeCnt>")
					  .append("<score_90_100 percentage=\"").append(cwUtils.formatNumber(tstInfo.score_90_100_cnt / (float)usr_cnt * 100, 1)).append("\">")
					  .append(tstInfo.score_90_100_cnt).append("</score_90_100>")
					  .append("<score_80_90 percentage=\"").append(cwUtils.formatNumber(tstInfo.score_80_90_cnt / (float)usr_cnt * 100, 1)).append("\">")
					  .append(tstInfo.score_80_90_cnt).append("</score_80_90>")
					  .append("<score_70_80 percentage=\"").append(cwUtils.formatNumber(tstInfo.score_70_80_cnt / (float)usr_cnt * 100, 1)).append("\">")
					  .append(tstInfo.score_70_80_cnt).append("</score_70_80>")
					  .append("<score_60_70 percentage=\"").append(cwUtils.formatNumber(tstInfo.score_60_70_cnt / (float)usr_cnt * 100, 1)).append("\">")
					  .append(tstInfo.score_60_70_cnt).append("</score_60_70>")
					  .append("<score_0_60 percentage=\"").append(cwUtils.formatNumber(tstInfo.score_0_60_cnt / (float)usr_cnt * 100, 1)).append("\">")
					  .append(tstInfo.score_0_60_cnt).append("</score_0_60>")
					  .append("<stat_image_path>")
					  .append(viewExam.getStatImage(chartImgSavedPath, tempPathName, cur_lan, 
											  tstInfo.score_90_100_cnt, tstInfo.score_80_90_cnt, tstInfo.score_70_80_cnt, 
											  tstInfo.score_60_70_cnt, tstInfo.score_0_60_cnt))
					  .append("</stat_image_path>")
					  .append("</group_summary>");
				
				if (!show_stat_only) {
					for (i = 0; i < usrIds.size(); i++) {
						usrIdObj = (Long)usrIds.get(i);
						usrInfo = (UsrInfo)usrHash.get(usrIdObj);
						for(Entry<String, ModInfo> mod : usrInfo.modHash.entrySet()){
							modInfo = mod.getValue();
							if(modInfo.mod_res_id == tstInfo.res_id){
								result.append("<record>").append(cwUtils.NEWL);
								result.append("<user>")
									  .append("<usr_ent_id>").append(usrInfo.usr_ent_id).append("</usr_ent_id>")
									  .append("<usr_ste_usr_id>").append(cwUtils.esc4XML(usrInfo.usr_ste_usr_id)).append("</usr_ste_usr_id>")
									  .append("<usr_display_bil>").append(cwUtils.esc4XML(usrInfo.usr_display_bil)).append("</usr_display_bil>")
									  .append("<usr_email>").append(cwUtils.esc4XML(usrInfo.usr_email)).append("</usr_email>")
									  .append("<usr_tel_1>").append(cwUtils.esc4XML(usrInfo.usr_tel_1)).append("</usr_tel_1>")
									  .append("<usr_extra_2>").append(cwUtils.esc4XML(usrInfo.usr_extra_2)).append("</usr_extra_2>")
									  .append("<usg_display_bil>").append(cwUtils.esc4XML(usrInfo.usg_display_bil)).append("</usg_display_bil>")
									  .append("<ugr_display_bil>").append(cwUtils.esc4XML(usrInfo.ugr_display_bil)).append("</ugr_display_bil>")
									  .append("<sks_title>").append(cwUtils.esc4XML(usrInfo.sks_title)).append("</sks_title>")
									  .append("<start_visit_time>").append(modInfo.start_visit_time).append("</start_visit_time>")
									  .append("<last_visit_time>").append(modInfo.last_visit_time).append("</last_visit_time>")
									  .append("<mov_total_attempt>").append(modInfo.mov_total_attempt).append("</mov_total_attempt>")
									  .append("<is_pass>").append(modInfo.is_pass).append("</is_pass>")
									  .append("<mov_score>").append(modInfo.mov_score).append("</mov_score>")
									  .append("</user>");
								result.append("</record>").append(cwUtils.NEWL);
							}
						}
					}
				}
			}
			result.append("</report_group>").append(cwUtils.NEWL);
		}

		result.append("</report_list>");
	}
 }
