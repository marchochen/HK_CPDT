package com.cw.wizbank.report;

import com.cw.wizbank.ae.*;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.DbTemplateView;
import com.cw.wizbank.ae.db.view.ViewCourseReport;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.view.ViewSurveyReport;
import com.cw.wizbank.ae.db.view.ViewSurveyReport.ViewProgressAttemptStat;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.course.ModuleSelect;
import com.cw.wizbank.db.DbObjectView;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.entity.Question;
import com.cwn.wizbank.entity.Resources;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.utils.LabelContent;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

// added for REPORT_VIEW template
public class SurveyReport extends ReportTemplate {

    private static final int SPLITTED_REPORT_SIZE = 750;

    static final int DEFAULT_PAGE_SIZE = 10;
    static final String DEFAULT_SORT_ORDER = "ASC";

    static final String[] DEFAULT_SORT_COL_ORDER = {"itm_title", "itm_version_code"};
    static final String TEMP_COL = "itm_type";

    static final String SESS_SURVEY_REPORT_DATA = "SESS_SURVEY_REPORT_DATA";
    static final String SESS_SURVEY_REPORT_PAGINATION = "SESS_SURVEY_REPORT_PAGINATION";
    static final String SESS_SURVEY_REPORT_RUN_DATA = "SESS_SURVEY_REPORT_RUN_DATA";

    static final String RTE_TYPE_SURVEY_PREFIX = "SURVEY_";
    static final String RTE_TYPE_SURVEY_COS_PREFIX = "SURVEY_COS_";
    static final String RTE_TYPE_SURVEY_IND = "SURVEY_IND";
    static final String RTE_TYPE_SURVEY_GRP = "SURVEY_GRP";
    static final String RTE_TYPE_SURVEY_COS_GRP = "SURVEY_COS_GRP";
    public static final String RTE_TYPE_SURVEY_QUE_GRP = "SURVEY_QUE_GRP";

    static final String SPEC_KEY_ENT_ID = "ent_id";
    static final String SPEC_KEY_UGR_ENT_ID = "ugr_ent_id_id";
    static final String SPEC_KEY_MOD_ID = "mod_id";
    static final String SPEC_KEY_ITM_ID = "itm_id";
    static final String SPEC_KEY_RUN_ID_LST = "run_id_lst";
    
    static final String SPEC_KEY_TND_ID = "tnd_id";
    static final String SPEC_KEY_ITM_TITLE = "itm_title";
    static final String SPEC_KEY_ITM_TITLE_PARTIAL_IND = "itm_title_partial_ind";
    static final String SPEC_KEY_ITM_TYPE = "itm_type";
    static final String SPEC_KEY_SHOW_RUN_IND = "show_run_ind";
    static final String SPEC_KEY_SHOW_OLD_VERSION = "show_old_version";
    static final String SPEC_KEY_START_DATETIME = "start_datetime";
    static final String SPEC_KEY_END_DATETIME = "end_datetime";
    static final String SPEC_KEY_DATETIME_RESTRICTION = "datetime_restriction";
    static final String SPEC_KEY_CONTENT_LST = "content_lst";
//    static final String SPEC_KEY_CONTENT_LST_ATTENDANCE = "attendance";
    static final String SPEC_KEY_ITM_CONTENT_LST = "itm_content_lst";
    static final String SPEC_KEY_RUN_CONTENT_LST = "run_content_lst";

    static final String SPEC_KEY_CONTENT_LST_VERSION = "version";
    static final String SPEC_KEY_CONTENT_LST_CATALOG = "catalog";
    static final String SPEC_KEY_CONTENT_LST_TYPE = "type";

//    static final String SPEC_KEY_CONTENT_LST_RUN_NUM = "run_num";
    static final String SPEC_KEY_CONTENT_LST_INSTRUCTOR = "instructor";
    static final String SPEC_KEY_CONTENT_LST_COS_MANAGER = "cos_manager";
    static final String SPEC_KEY_CONTENT_LST_PROJ_CONST = "proj_const";

    static final String V_DATA = "V_DATA";
    static final String H_COURSE_ACCESS = "H_COURSE_ACCESS";
//    static final String H_COURSE_ATTENDANCE = "H_COURSE_ATTENDANCE";
    static final String H_RUN_DATA = "H_RUN_DATA";
//    static final String H_RUN_ATTENDANCE = "H_RUN_ATTENDANCE";
    static final String H_RUN_ACCESS = "H_RUN_ACCESS";
//    static final String V_ATS_ID_ATTENDED_IND = "V_ATS_ID_ATTENDED_IND";
    static final int XLS_COL = 20;
    static final int COL_WIDTH = 6000;
    static final String XLS_NAME_REPORT = "report";
    static final String ITEM_TYPE_SELFSTUDY = "SELFSTUDY";
    static final String ITEM_TYPE_CLASSROOM = "CLASSROOM";
    
    private String rte_type = null;
    public SurveyReport(String rte_type){
        this.rte_type = rte_type;
    }
    
    public String[] getSurveyReport(Connection con, HttpServletRequest request,
                                    HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env,WizbiniLoader wizbini, 
                                    long rte_id, String[] spec_name, String[] spec_value,
                                    String rsp_title, boolean split_ind)
        throws SQLException, cwSysMessage, cwException, IOException, qdbException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;

        String[] reportXML = getSurveyReportHelper(con, sess, page, prof, env,wizbini, data, split_ind);
        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, 0, rte_id, null, reportXML[i], data.rsp_xml, this.rte_type, rsp_title);
        }
        return reportXML;
    }

    public String[] getSurveyReport(Connection con, HttpServletRequest request,
                                    HttpSession sess, cwPagination page,
                                    loginProfile prof,  qdbEnv env, WizbiniLoader wizbini, long rsp_id, 
                                    boolean split_ind)
        throws SQLException, cwSysMessage, cwException, IOException, qdbException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);

        String[] reportXML = getSurveyReportHelper(con, sess, page, prof, env, wizbini, data, split_ind);
        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, rsp_id, 0, null, reportXML[i], null, this.rte_type, null);
        }
        return reportXML;
    }

    public String[] getSurveyReportHelper(Connection con, HttpSession sess,
                                          cwPagination page, loginProfile prof, 
                                          qdbEnv env,WizbiniLoader wizbini,
                                          ViewReportSpec.Data data, 
                                          boolean split_ind)
        throws SQLException, cwSysMessage, cwException , qdbException {
        Hashtable spec_pairs = getSpecPairs(data.rsp_xml);

        long[] ent_id_lst = null;
        long[] ugr_ent_id_lst = null;
        String[] tnd_id_lst = null;
        String itm_title = null;
        int itm_title_partial_ind = 1;
        String[] itm_type = null;
        int show_run_ind = 1;
        int show_old_version = 1;
        String[] content = null;
        Timestamp start_datetime = null;
        Timestamp end_datetime = null;
        String datetime_restriction = null;
        long mod_id = 0;
        long[] itm_id_lst = null;
        long[] run_id_lst = null;

        Vector spec_values;

        boolean[] course_types = null;
        if(wizbini.cfgTcEnabled) {
            boolean all_cos_ind = false;
            boolean answer_for_course = false;
            boolean answer_for_lrn_course = false;

        	if(spec_pairs.containsKey("answer_for_course")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_course");
        		if(spec_values.get(0).equals("1")) {
        			answer_for_course  = true;
        		}
        	}
        	if(spec_pairs.containsKey("answer_for_lrn_course")) {
        		spec_values = (Vector)spec_pairs.get("answer_for_lrn_course");
        		if(spec_values.get(0).equals("1")) {
        			answer_for_lrn_course  = true;
        		}
    	    }
        	course_types = new boolean[2];
        	course_types[0] = answer_for_course;
        	course_types[1] = answer_for_lrn_course;
            if(!spec_pairs.containsKey("tnd_id") && !spec_pairs.containsKey("itm_id")) {
            	all_cos_ind = true;
            }
            if(all_cos_ind &&  spec_pairs.containsKey("answer_for_course") && spec_pairs.containsKey("answer_for_lrn_course")) {
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
        
        if (spec_pairs.containsKey(SPEC_KEY_ENT_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ENT_ID);

            ent_id_lst = new long[spec_values.size()];

            for (int i=0; i<spec_values.size(); i++) {
                ent_id_lst[i] = Long.parseLong((String)spec_values.elementAt(i));
            }
        }
        
        if (spec_pairs.containsKey(SPEC_KEY_UGR_ENT_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_UGR_ENT_ID);

            ugr_ent_id_lst = new long[spec_values.size()];

            for (int i=0; i<spec_values.size(); i++) {
                ugr_ent_id_lst[i] = Long.parseLong((String)spec_values.elementAt(i));
            }
        }

        if (spec_pairs.containsKey(SPEC_KEY_TND_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_TND_ID);

            tnd_id_lst = new String[spec_values.size()];

            for (int i=0; i<spec_values.size(); i++) {
                tnd_id_lst[i] = (String)spec_values.elementAt(i);
            }
        }

        if (spec_pairs.containsKey(SPEC_KEY_ITM_TITLE)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_TITLE);

            itm_title = (String)spec_values.elementAt(0);
        }

        if (spec_pairs.containsKey(SPEC_KEY_ITM_TITLE_PARTIAL_IND)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_TITLE_PARTIAL_IND);

            itm_title_partial_ind = ((new Integer((String)spec_values.elementAt(0)))).intValue();
        }

        if (spec_pairs.containsKey(SPEC_KEY_ITM_TYPE)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_TYPE);

            itm_type = new String[spec_values.size()];

            for (int i=0; i<spec_values.size(); i++) {
                itm_type[i] = (String)spec_values.elementAt(i);
            }
        }

        if (spec_pairs.containsKey(SPEC_KEY_SHOW_RUN_IND)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_SHOW_RUN_IND);

            show_run_ind = ((new Integer((String)spec_values.elementAt(0)))).intValue();
        }
        if (spec_pairs.containsKey(SPEC_KEY_SHOW_OLD_VERSION)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_SHOW_OLD_VERSION);
            show_old_version = ((new Integer((String)spec_values.elementAt(0)))).intValue();
        }

        if (spec_pairs.containsKey(SPEC_KEY_START_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_START_DATETIME);

            start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }

        if (spec_pairs.containsKey(SPEC_KEY_END_DATETIME)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_END_DATETIME);

            end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }
        if (spec_pairs.containsKey(SPEC_KEY_DATETIME_RESTRICTION)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_DATETIME_RESTRICTION);

            datetime_restriction = (String)spec_values.elementAt(0);
        }

        if (spec_pairs.containsKey(SPEC_KEY_MOD_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_MOD_ID);
            mod_id = Long.parseLong((String)spec_values.elementAt(0));
        }
        if (spec_pairs.containsKey(SPEC_KEY_ITM_ID)) {
            //spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_ID);
            //itm_id = Long.parseLong((String)spec_values.elementAt(0));
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ITM_ID);
            itm_id_lst = new long[spec_values.size()];
            for(int i=0; i<spec_values.size(); i++) {
                itm_id_lst[i] = Long.parseLong((String)spec_values.elementAt(i));
            }
/*
        }else{
            itm_id_lst = new long[1];
            itm_id_lst[0] = 0;
*/
        }
        
        
        if (spec_pairs.containsKey(SPEC_KEY_RUN_ID_LST)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_RUN_ID_LST);

            run_id_lst = new long[spec_values.size()];

            for (int i=0; i<spec_values.size(); i++) {
                run_id_lst[i] = Long.parseLong((String)spec_values.elementAt(i));
            }
        }


        String[] reportXML=null;
        //split_ind==true, then get Learning Report into a number of small XMLs
        //for tuning purpose when XML is too large (the case of download report)
        //else, just get report into a single XML
        if(split_ind) {
            //Get report data as XML into a Vector
            Vector v_reportData = getSplittedReport(con, sess, page, prof, env, rte_type, tnd_id_lst,
                                                    null, itm_title,
                                                    itm_title_partial_ind,
                                                    itm_type, show_run_ind,
                                                    show_old_version, content,
                                                    start_datetime, end_datetime, datetime_restriction,
                                                    mod_id, itm_id_lst, run_id_lst, ent_id_lst, ugr_ent_id_lst,
                                                    (Vector)spec_pairs.get(SPEC_KEY_CONTENT_LST),
                                                    (Vector)spec_pairs.get(SPEC_KEY_ITM_CONTENT_LST),
                                                    (Vector)spec_pairs.get(SPEC_KEY_RUN_CONTENT_LST),
                                                    null);

            //Construct the report data into a number of reportXML
            reportXML = new String[v_reportData.size()];
            for(int i=0; i<v_reportData.size(); i++) {
                reportXML[i] = (String)v_reportData.elementAt(i);
            }
        } else {
            reportXML = new String[1];
            reportXML[0] = getReport(con, sess, page, prof, env, rte_type, tnd_id_lst, null,
                                     itm_title, itm_title_partial_ind, itm_type,
                                     show_run_ind, show_old_version, content,
                                     start_datetime, end_datetime, datetime_restriction,
                                     mod_id, itm_id_lst, run_id_lst, ent_id_lst, ugr_ent_id_lst,
                                     (Vector)spec_pairs.get(SPEC_KEY_CONTENT_LST),
                                     (Vector)spec_pairs.get(SPEC_KEY_ITM_CONTENT_LST),
                                     (Vector)spec_pairs.get(SPEC_KEY_RUN_CONTENT_LST), course_types);
        }
        return reportXML;
     }

    /**
    Get Report into a number of small XMLs for tuning purpose
    when XML is too large
    */
    public Vector getSplittedReport(Connection con, HttpSession sess,
                                           cwPagination page, loginProfile prof,
                                           qdbEnv env,
                                           String rte_type, 
                                           String[] tnd_id_lst, 
                                           String itm_code, String itm_title,
                                           int itm_title_partial_ind,
                                           String[] itm_type, int show_run_ind,
                                           int show_old_version, String[] content,
                                           Timestamp start_datetime, Timestamp end_datetime, String datetime_restriction,
                                           long mod_id, long[] itm_id_lst, long[] run_id_lst, long[] ent_id_lst, long[] ugr_ent_id_lst, 
                                           Vector content_vec, Vector itm_content_vec, 
                                           Vector run_content_vec, boolean[] course_types)
        throws SQLException, cwException, cwSysMessage, qdbException {
        Vector v_reportData = new Vector();
        if(page.pageSize <= 0) {
            page.pageSize = SPLITTED_REPORT_SIZE;
        }
        page.curPage = 1;
        cwPagination sess_page = null;
        do {
            v_reportData.addElement(getReport(con, sess, page, prof, env, rte_type, tnd_id_lst,
                                              itm_code, itm_title, itm_title_partial_ind,
                                              itm_type, show_run_ind, show_old_version,
                                              content, start_datetime, end_datetime, datetime_restriction,
                                              mod_id, itm_id_lst, run_id_lst, ent_id_lst, ugr_ent_id_lst,
                                              content_vec, itm_content_vec, 
                                              run_content_vec, course_types));
            sess_page = (cwPagination)sess.getAttribute(SESS_SURVEY_REPORT_PAGINATION);
            page.ts = sess_page.ts;
            page.curPage++;
        }while (page.curPage <= sess_page.totalPage);
        return v_reportData;
    }

    public String getReport(Connection con, HttpSession sess, cwPagination page,
                                   loginProfile prof, qdbEnv env, String rte_type, String[] tnd_id_lst,
                                   String itm_code, String itm_title,
                                   int itm_title_partial_ind, String[] itm_type,
                                   int show_run_ind, int show_old_version,
                                   String[] content, Timestamp start_datetime,
                                   Timestamp end_datetime, String datetime_restriction,                                           
                                   long mod_id, long[] itm_id_lst, long[] run_id_lst, long[] ent_id_lst, long[] ugr_ent_id_lst, 
                                   Vector content_vec, Vector itm_content_vec, 
                                   Vector run_content_vec, boolean[] course_types)
        throws SQLException, cwException, cwSysMessage, qdbException {
            
        String xml = null;
        if (this.rte_type.equals(RTE_TYPE_SURVEY_IND)){
            if  (itm_id_lst == null){
                itm_id_lst = new long[1];
                itm_id_lst[0] = 0;
            }
            xml = getSurveyIndivReport(con, sess, prof, mod_id, itm_id_lst[0], run_id_lst, page, start_datetime, end_datetime, ent_id_lst, ugr_ent_id_lst);
        }else if (this.rte_type.equals(RTE_TYPE_SURVEY_GRP)){
            xml = getSurveyStat(con, prof, start_datetime, end_datetime, mod_id, ent_id_lst, ugr_ent_id_lst);
        }else if (this.rte_type.equals(RTE_TYPE_SURVEY_COS_GRP)){
            xml = getSurveyCourseReport(con, sess, page, prof, env, rte_type, tnd_id_lst,
                                              itm_code, itm_title, itm_title_partial_ind,
                                              itm_type, show_run_ind, show_old_version,
                                              content, start_datetime, end_datetime, datetime_restriction,
                                              mod_id, ent_id_lst, ugr_ent_id_lst,
                                              content_vec, itm_content_vec, 
                                              run_content_vec, itm_id_lst, course_types);
        }else{
            throw new cwException("Invalid survey report type:" + rte_type);
        }
        return xml;
    }

    private class QueData{
            long id;
            long order;
            long response_count;
            long response_score;
            StringBuffer response_xml = new StringBuffer();
    }

    public boolean checkAttempted(Connection con, Timestamp start_datetime, Timestamp end_datetime, long mod_id, long[] ent_id_lst, long[] ugr_ent_id_lst) throws SQLException{
        ViewSurveyReport.ViewProgressStat pgrStat = ViewSurveyReport.getProgressStat(con, start_datetime, end_datetime, mod_id, ent_id_lst, ugr_ent_id_lst);
        return pgrStat.total_cnt > 0;
    }

    public StringBuffer getSurveyAttemptStat(Connection con, Timestamp start_datetime, Timestamp end_datetime, long mod_id, long[] ent_id_lst, long[] ugr_ent_id_lst, boolean[] course_types) throws SQLException{
        ViewSurveyReport.ViewProgressStat pgrStat = ViewSurveyReport.getProgressStat(con, start_datetime, end_datetime, mod_id, ent_id_lst, ugr_ent_id_lst);
        ViewSurveyReport.ViewProgressAttemptStat[] pgrAtmStat = ViewSurveyReport.getAttemptStat(con, start_datetime, end_datetime, mod_id, ent_id_lst, ugr_ent_id_lst, course_types);

        StringBuffer xml = new StringBuffer();
        xml.append("<survey id=\"");
        xml.append(mod_id);
        xml.append("\" attempt_count=\"");
        xml.append(pgrStat.total_cnt);
        xml.append("\" avg_svy_score=\"");
        if (pgrStat.total_cnt > 0) {
            xml.append(pgrStat.total_score / pgrStat.total_cnt);
        }else{
            xml.append("0");            
        }            
        xml.append("\" >").append(cwUtils.NEWL);
        
        Hashtable htQueData = new Hashtable();
        
        for (int i = 0; i < pgrAtmStat.length; i++) {
            long que_id = pgrAtmStat[i].atm_int_res_id;
            QueData que = (QueData)htQueData.get(new Long(que_id));
            if (que==null){
                que = new QueData();
                que.id = que_id;
                que.order = pgrAtmStat[i].rcn_order;
            }            
            que.response_xml.append("<response id=\"").append(pgrAtmStat[i].atm_response_bil)
                .append("\" count=\"").append(pgrAtmStat[i].total_cnt)
                .append("\" total_score=\"").append(pgrAtmStat[i].total_score).append("\" />");
                                
            que.response_count += pgrAtmStat[i].total_cnt;
            que.response_score += pgrAtmStat[i].total_score;
            htQueData.put(new Long(que_id), que);
        }
        
        Enumeration enum_que = htQueData.keys();
        while (enum_que.hasMoreElements()) {
            QueData que = (QueData) htQueData.get(enum_que.nextElement());
            xml.append("<question id=\"").append(que.id)
                        .append("\" order=\"").append(que.order)
                        .append("\" response_count=\"").append(que.response_count)
                        .append("\" response_avg_score=\"").append((float)que.response_score/que.response_count)
                        .append("\" >");
            xml.append(que.response_xml);                        
            xml.append("</question>");                        
        }
        xml.append("</survey>").append(cwUtils.NEWL);
        
        return xml;
    }
    
    public String getMetaData(Connection con, loginProfile prof, boolean isStudyGroupMod,long sgp_id, WizbiniLoader wizbini) throws SQLException , cwException, cwSysMessage{
        StringBuffer result = new StringBuffer();
        result.append("<cur_time>").append(cwSQL.getTime(con)).append("</cur_time>").append(cwUtils.NEWL);
        result.append(dbUtils.getAllRoleAsXML(con, "role_list", prof.root_ent_id));
        String mod_type; 
        if (this.rte_type.equals(RTE_TYPE_SURVEY_COS_GRP) || this.rte_type.equals(RTE_TYPE_SURVEY_QUE_GRP)) {
            mod_type = dbModule.MOD_TYPE_SVY;
            result.append(aeItem.getHasQdbItemTypeTitleInOrg(con, prof.root_ent_id));
            result.append(CourseReport.getCatalogTitleList(con, prof,  wizbini));
        }else{
            mod_type = dbModule.MOD_TYPE_EVN;
        }
        ViewTrainingCenter tcView= new ViewTrainingCenter();
		List tcrLst = tcView.getTrainingCenterByOfficer(con, prof.usr_ent_id, prof.current_role, true);
		Vector modTcrIds = new Vector();
		if(null!= tcrLst && tcrLst.size()>0){
			for(int i = 0;i<tcrLst.size();i++){
				DbTrainingCenter tcr = (DbTrainingCenter)tcrLst.get(i);
				if(tcr.tcr_id > 0){
					modTcrIds.add(tcr.tcr_id);
				}
			}
		}

        Vector vtMod = dbModule.getPublicModLst(con, prof.root_ent_id, mod_type, false, isStudyGroupMod,sgp_id, modTcrIds);
        result.append(dbModule.getModLstAsXML(con, vtMod, null,0,0));
        return result.toString();
    }
    
    public static String getSurveyIndivReport(Connection con, HttpSession sess, loginProfile prof, long mod_id, long itm_id, long[] run_id_lst, cwPagination cwPage, Timestamp startDate, Timestamp endDate, long[] ent_id_lst, long[] ugr_ent_id_lst) 
        throws SQLException, cwException, qdbException,  cwSysMessage
    {
        StringBuffer result = new StringBuffer();
        boolean isLstByEnrollment = false;
        if (itm_id != 0 && ( ent_id_lst==null || ent_id_lst.length==0)){
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            itm.itm_run_ind = itm.getRunInd(con);
            itm.itm_create_run_ind = itm.getCreateRunInd(con);
            if (!itm.itm_run_ind && !itm.itm_create_run_ind)
                isLstByEnrollment = true;
            if (itm.itm_run_ind){
                ent_id_lst = aeApplication.getAdmittedUserList(con, itm_id);
            }else if (itm.itm_create_run_ind && run_id_lst != null && run_id_lst.length > 0){
                Vector vtAdmitUserInRuns = new Vector();
                for (int i=0; i<run_id_lst.length; i++){
                    long[] entId = aeApplication.getAdmittedUserList(con, run_id_lst[i]);
                    for (int j = 0; j < entId.length; j++){
                        Long tmpLong = new Long(entId[j]);
                        if (!vtAdmitUserInRuns.contains(tmpLong)){
                            vtAdmitUserInRuns.addElement(tmpLong);
                        }
                    }
                }
                if (vtAdmitUserInRuns.size()==0){
                    vtAdmitUserInRuns.addElement(new Long(0));
                }
                ent_id_lst = cwUtils.vec2longArray(vtAdmitUserInRuns);
            }
        }
        if (itm_id != 0){
            aeItem itm = new aeItem();
            itm.itm_id = itm_id;
            itm.get(con);
            result.append("<item id=\"").append(itm.itm_id)
                .append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(itm.itm_title)))
                .append("\" type=\"").append(cwUtils.escNull(itm.itm_type))
                .append("\" dummy_type=\"").append(cwUtils.escNull(itm.itm_dummy_type))
                    .append("\" eff_start_datetime=\"").append(aeUtils.escNull(itm.itm_eff_start_datetime))
                    .append("\" eff_end_datetime=\"").append(aeUtils.escNull(itm.itm_eff_end_datetime))
                    .append("\" cos_res_id=\"").append(itm.cos_res_id)
                    .append("\" />").append(cwUtils.NEWL);
        }
        result.append(dbModuleEvaluation.getByModuleAsXML(con, sess, prof, mod_id, cwPage, "C", startDate, endDate, ent_id_lst, ugr_ent_id_lst, isLstByEnrollment));
        return result.toString() ;
    }
    
    public String getSurveyStat(Connection con, loginProfile prof, Timestamp start_datetime, Timestamp end_datetime, long mod_id, long[] ent_id_lst, long[] ugr_ent_id_lst) 
        throws SQLException{
        StringBuffer result = new StringBuffer();
        result.append("<report_list>").append(cwUtils.NEWL);
        result.append("<item>").append(cwUtils.NEWL);
        if (mod_id!=0){
            result.append(getSurveyAttemptStat(con, start_datetime, end_datetime, mod_id, ent_id_lst, ugr_ent_id_lst, null)).append(cwUtils.NEWL);
        }
        result.append("</item>").append(cwUtils.NEWL);
        result.append("</report_list>").append(cwUtils.NEWL);
        return result.toString();
    }

    public String getSurveyCourseReport(Connection con, HttpSession sess, cwPagination page,
                                   loginProfile prof, qdbEnv env, String rte_type, String[] tnd_id_lst,
                                   String itm_code, String itm_title,
                                   int itm_title_partial_ind, String[] itm_type,
                                   int show_run_ind, int show_old_version,
                                   String[] content, Timestamp start_datetime,
                                   Timestamp end_datetime, String datetime_restriction,                                           
                                   long mod_id, long[] ent_id_lst, long[] ugr_ent_id_lst, 
                                   Vector content_vec, Vector itm_content_vec, 
                                   Vector run_content_vec, long[] itm_id_lst, boolean[] course_types)
        throws SQLException, cwException, cwSysMessage {
        StringBuffer result = new StringBuffer();

        cwPagination sess_page = (cwPagination)sess.getAttribute(SESS_SURVEY_REPORT_PAGINATION);
        ViewCourseReport report = new ViewCourseReport();
        Vector v_data = null;
        Hashtable h_course_access = null;
//        Hashtable h_course_attendance = null;
       // Hashtable h_run_data = null;
//        Hashtable h_run_attendance = null;
        //Hashtable h_run_access = null;
//        Vector v_ats_id_attended_ind = null;

        Hashtable htItmMod = null;
        
        if (page.pageSize == 0) {
            page.pageSize = DEFAULT_PAGE_SIZE;
        }

        if (page.curPage == 0) {
            page.curPage = 1;
        }

        if (sess_page != null && sess_page.ts.equals(page.ts)) {
            Hashtable sess_data = (Hashtable)sess.getAttribute(SESS_SURVEY_REPORT_DATA);
            v_data = (Vector)sess_data.get(V_DATA);
            h_course_access = (Hashtable)sess_data.get(H_COURSE_ACCESS);
//            h_course_attendance = (Hashtable)sess_data.get(H_COURSE_ATTENDANCE);
           // h_run_data = (Hashtable)sess_data.get(H_RUN_DATA);
//            h_run_attendance = (Hashtable)sess_data.get(H_RUN_ATTENDANCE);
            //h_run_access = (Hashtable)sess_data.get(H_RUN_ACCESS);
//            v_ats_id_attended_ind = (Vector)sess_data.get(V_ATS_ID_ATTENDED_IND);

            page.totalRec = sess_page.totalRec;

            if (page.pageSize == -1 && page.totalRec != 0) {
                page.pageSize = page.totalRec;
            }
            page.totalPage = page.totalRec/page.pageSize;

            if (page.totalRec%page.pageSize !=0) {
                page.totalPage++;
            }

            if (page.sortOrder == null) {
                page.sortOrder = DEFAULT_SORT_ORDER;
            }
            if (page.sortCol == null) {
                page.sortCol = DEFAULT_SORT_COL_ORDER[0];
            }
            if (sess_page.sortCol.equals(page.sortCol) && sess_page.sortOrder.equals(page.sortOrder)) {
                page.sortCol = cwPagination.esc4SortSql(sess_page.sortCol);
                page.sortOrder = cwPagination.esc4SortSql(sess_page.sortOrder);
            } else {
            }
        } else {
            String usr_lst = null;
            StringBuffer str_buf = new StringBuffer();
            Vector itm_lst_vec = null;
            if (tnd_id_lst != null) {
                itm_lst_vec = new Vector();
                aeTreeNode.getItemsFromNode(con, tnd_id_lst, itm_lst_vec);

                if (itm_lst_vec.size() == 0) {
                    itm_lst_vec.addElement(new Long(0));
                }
            } else if (itm_id_lst != null) {
                itm_lst_vec = new Vector();
                for(int i=0; i<itm_id_lst.length; i++){
                    itm_lst_vec.addElement(new Long(itm_id_lst[i]));
                }
            }
			

            if (mod_id != 0){
                htItmMod = ViewSurveyReport.searchItmMyModRoot(con, itm_lst_vec, mod_id,start_datetime,end_datetime);
                // reset itm_lst
                itm_lst_vec = new Vector();
                Enumeration enum_itm = htItmMod.keys();
                while (enum_itm.hasMoreElements()) {
                    itm_lst_vec.addElement(enum_itm.nextElement());
                }

            }else{
                throw new cwException("ERROR IN com.cw.wizbank.report.SurveyCourseReport: MOD ID NOT DEFINED.");
            }

            Vector v_itm_type = new Vector();

            if (itm_type != null) {
                for (int i=0; i<itm_type.length; i++) {
                    v_itm_type.addElement(itm_type[i]);
                }
            }

            if (page.sortOrder == null) {
                page.sortOrder = DEFAULT_SORT_ORDER;
            }

            str_buf = new StringBuffer();
            String str = null;

            for (int i=0; i<DEFAULT_SORT_COL_ORDER.length; i++) {
                if (DEFAULT_SORT_COL_ORDER[i].equals(page.sortCol)) {
                    str = page.sortCol + " " + page.sortOrder;
                } else {
                    str_buf.append(DEFAULT_SORT_COL_ORDER[i]).append(" ").append(page.sortOrder).append(", ");
                }
            }

            StringBuffer col_buf = new StringBuffer();
            col_buf.append(" ORDER BY ");

            if (str != null) {
                col_buf.append(str).append(", ");
            } else {
                page.sortCol = DEFAULT_SORT_COL_ORDER[0];
            }

            col_buf.append(str_buf.toString());
            col_buf.append(TEMP_COL).append(" ").append(page.sortOrder);

            Vector v_temp_data = report.search(con, page, prof.root_ent_id, itm_code, itm_lst_vec, itm_title, itm_title_partial_ind, itm_type, show_run_ind, show_old_version, content, col_buf.toString());
            // Vector v_has_run_itm_lst = null;
            //Vector v_no_run_itm_lst = null;
            h_course_access = new Hashtable();

            if (v_temp_data != null && v_temp_data.size() != 0) {
                // v_has_run_itm_lst = new Vector();
                //  v_no_run_itm_lst = new Vector();
                Vector itm_lst = new Vector();

                for (int i=0; i<v_temp_data.size(); i++) {
                    ViewCourseReport.Data d = (ViewCourseReport.Data)v_temp_data.elementAt(i);
                     /*
                    if (d.itm_create_run_ind == 1) {
                        v_has_run_itm_lst.addElement(new Long(d.itm_id));
                    } else {
                        v_no_run_itm_lst.addElement(new Long(d.itm_id));
                    }
                   */
                    itm_lst.addElement(new Long(d.itm_id));
                }

                if (content_vec != null && 
                    (content_vec.contains(SPEC_KEY_CONTENT_LST_COS_MANAGER) ||
                    content_vec.contains(SPEC_KEY_CONTENT_LST_PROJ_CONST))) {
                    h_course_access = report.searchItemAccess(con, itm_lst);
                }
            }

           


            v_data = new Vector();
           // h_run_access = new Hashtable();
            /*
            if (v_run_lst != null && v_run_lst.size() > 0 &&
                (content_vec != null && 
                (content_vec.contains(SPEC_KEY_CONTENT_LST_COS_MANAGER) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_PROJ_CONST)))) {
                    h_run_access = report.searchItemAccess(con, v_run_lst);
            }
             */
            // Filtering
            
            v_data = v_temp_data;
           

            page.totalRec = v_data.size();

            if (page.pageSize == -1 && page.totalRec != 0) {
                page.pageSize = page.totalRec;
            }

            page.totalPage = page.totalRec/page.pageSize;

            if (page.totalRec%page.pageSize !=0) {
                page.totalPage++;
            }

            page.ts = cwSQL.getTime(con);

            if (page != null) {
                sess.setAttribute(SESS_SURVEY_REPORT_PAGINATION, page);
            }

            Hashtable sess_data = new Hashtable();

            if (v_data != null) {
                sess_data.put(V_DATA, v_data);
            }
            if (h_course_access != null) {
                sess_data.put(H_COURSE_ACCESS, h_course_access);
            }
           // if (h_run_data != null) {
             //   sess_data.put(H_RUN_DATA, h_run_data); 
           // }
            //if (h_run_access != null) {
              //  sess_data.put(H_RUN_ACCESS, h_run_access);
           // }
            sess.setAttribute(SESS_SURVEY_REPORT_DATA, sess_data);
        }

        Vector itmVec = new Vector();
		Hashtable itm_parentId_Has=new Hashtable();
        for (int i=(page.curPage-1)*page.pageSize; i<v_data.size() && i<(page.curPage*page.pageSize); i++) {
            ViewCourseReport.Data d = (ViewCourseReport.Data)v_data.elementAt(i);
            if (!itmVec.contains(new Long(d.itm_id))) {
            	if(d.itm_run_ind == 1){
					aeItemRelation aeRelation = new aeItemRelation();
					aeRelation.ire_child_itm_id =d.itm_id;
					long parentId=aeRelation.getParentItemId(con);
					if(!itmVec.contains(new Long(parentId))){
						itmVec.addElement(new Long(parentId));
					}
					
					itm_parentId_Has.put(new Long(d.itm_id),new Long(parentId));
            	}else{
					if(!itmVec.contains(new Long(d.itm_id))){
						itmVec.addElement(new Long(d.itm_id));
					 }
					
            	}
                
            }
        }

        Hashtable itemCatalogXMLHash = new Hashtable();

        if (content_vec != null && content_vec.contains(SPEC_KEY_CONTENT_LST_CATALOG)) {
            itemCatalogXMLHash = aeItem.getCatalogXMLHash(con, itmVec);
        }

        Timestamp cur_time = cwSQL.getTime(con);
        aeTemplate itmTemplate = null;
        aeTemplate runTemplate = null;
        DbTemplateView itmReportView = null;
        DbTemplateView runReportView = null;
        CourseReport cosReport = new CourseReport();
        //get item tempalte view
        if(itm_content_vec != null && itm_content_vec.size() > 0) {
            DbObjectView itemView = 
                DbObjectView.getObjectView(con, prof.root_ent_id, 
                                           DbObjectView.OJV_TYPE_SURVEY_COURSE_REPORT, 
                                           DbObjectView.OJV_SUBTYPE_ITM);
            if(itemView != null) {
                itmReportView = cosReport.createTemplateView(itm_content_vec, cosReport.getItmResultView(itm_content_vec, itemView.getOptionXML()));
            }
        }
        //get run tempalte view
        if(run_content_vec != null && run_content_vec.size() > 0) {
            DbObjectView runView = 
                DbObjectView.getObjectView(con, prof.root_ent_id, 
                                        DbObjectView.OJV_TYPE_SURVEY_COURSE_REPORT, 
                                        DbObjectView.OJV_SUBTYPE_RUN);
            if(runView != null) {
                runReportView = cosReport.createTemplateView(run_content_vec, cosReport.getRunResultView(run_content_vec, runView.getOptionXML()));
            }
        }

        result.append("<report_list>").append(cwUtils.NEWL);

        for (int i=(page.curPage-1)*page.pageSize; i<v_data.size() && i<(page.curPage*page.pageSize); i++) {
			ViewCourseReport.Data d = (ViewCourseReport.Data)v_data.elementAt(i);
			if(d.itm_run_ind==1){
				result.append("<data>").append(cwUtils.NEWL);
				Vector temp=new Vector();
				temp.add(itm_parentId_Has.get(new Long(d.itm_id)));
				Vector temp_parent_itm=report.search(con, page, prof.root_ent_id, itm_code, temp, itm_title, itm_title_partial_ind, itm_type, show_run_ind, show_old_version, content,"");
				if(temp_parent_itm.size()>0){
					ViewCourseReport.Data parent = (ViewCourseReport.Data)temp_parent_itm.elementAt(0);
					result.append("<item id=\"").append(parent.itm_id)
						  .append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(parent.itm_code)))
						  .append("\" version=\"").append(cwUtils.esc4XML(aeUtils.escNull(parent.itm_version_code)))
						  .append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(parent.itm_title)))
						  .append("\" type=\"").append(cwUtils.escNull(parent.itm_type))
						  .append("\" dummy_type=\"").append(cwUtils.escNull(parent.itm_dummy_type))
						  .append("\" eff_start_datetime=\"").append(aeUtils.escNull(parent.itm_eff_start_datetime))
						  .append("\" eff_end_datetime=\"").append(aeUtils.escNull(parent.itm_eff_end_datetime))
						  .append("\" nature=\"").append(aeUtils.escNull(parent.itm_apply_method))
						  .append("\" unit=\"").append(parent.itm_unit)
						  .append("\" status=\"").append(aeUtils.escNull(parent.itm_status))
						  .append("\" life_status=\"").append(aeUtils.escNull(parent.itm_life_status))
						  .append("\" person_in_charge=\"").append(cwUtils.esc4XML(aeUtils.escNull(parent.itm_person_in_charge)))
						  .append("\" cos_res_id=\"").append(parent.cos_res_id)
						  .append("\">").append(cwUtils.NEWL);
					if(itm_content_vec!=null && itm_content_vec.size()>0 && env!=null) {
						aeItem item = new aeItem();
						item.itm_id = parent.itm_id;
						item.get(con);
						if(itmTemplate == null) {
							itmTemplate = item.getItemTemplate(con);
						}
						result.append(item.getValuedTemplate(con,itmTemplate,itmReportView,false,cur_time,env,prof.usr_ent_id));
					}
					result.append("</item>");

              	}
				result.append("<run id=\"").append(d.itm_id)
					  .append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_code)))
					  .append("\" version=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_version_code)))
					  .append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_title)))
					  .append("\" type=\"").append(cwUtils.escNull(d.itm_type))
					  .append("\" dummy_type=\"").append(cwUtils.escNull(d.itm_dummy_type))
					  .append("\" eff_start_datetime=\"").append(aeUtils.escNull(d.itm_eff_start_datetime))
					  .append("\" eff_end_datetime=\"").append(aeUtils.escNull(d.itm_eff_end_datetime))
					  .append("\" nature=\"").append(aeUtils.escNull(d.itm_apply_method))
					  .append("\" unit=\"").append(d.itm_unit)
					  .append("\" status=\"").append(aeUtils.escNull(d.itm_status))
					  .append("\" life_status=\"").append(aeUtils.escNull(d.itm_life_status))
					  .append("\" person_in_charge=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_person_in_charge)))
					  .append("\" cos_res_id=\"").append(d.cos_res_id)
					  .append("\">").append(cwUtils.NEWL);  
				if(run_content_vec!=null && run_content_vec.size()>0 && env!=null) {
					aeItem run = new aeItem();
					run.itm_id = d.itm_id;
					run.get(con);
					if(runTemplate == null) {
						runTemplate = run.getItemTemplate(con);
					}
					result.append(run.getValuedTemplate(con,runTemplate,runReportView,false,cur_time,env,prof.usr_ent_id));
				}
				result.append("</run>");
			}else{
				result.append("<data>").append(cwUtils.NEWL);
				result.append("<item id=\"").append(d.itm_id)
					  .append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_code)))
					  .append("\" version=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_version_code)))
					  .append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_title)))
					  .append("\" type=\"").append(cwUtils.escNull(d.itm_type))
					  .append("\" dummy_type=\"").append(cwUtils.escNull(d.itm_dummy_type))
					  .append("\" eff_start_datetime=\"").append(aeUtils.escNull(d.itm_eff_start_datetime))
					  .append("\" eff_end_datetime=\"").append(aeUtils.escNull(d.itm_eff_end_datetime))
					  .append("\" nature=\"").append(aeUtils.escNull(d.itm_apply_method))
					  .append("\" unit=\"").append(d.itm_unit)
					  .append("\" status=\"").append(aeUtils.escNull(d.itm_status))
					  .append("\" life_status=\"").append(aeUtils.escNull(d.itm_life_status))
					  .append("\" person_in_charge=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_person_in_charge)))
					  .append("\" cos_res_id=\"").append(d.cos_res_id)
					  .append("\">").append(cwUtils.NEWL);
				if(itm_content_vec!=null && itm_content_vec.size()>0 && env!=null) {
					aeItem item = new aeItem();
					item.itm_id = d.itm_id;
					item.get(con);
					if(itmTemplate == null) {
						itmTemplate = item.getItemTemplate(con);
					}
					result.append(item.getValuedTemplate(con,itmTemplate,itmReportView,false,cur_time,env,prof.usr_ent_id));
				}
				result.append("</item>");
			}

			/*
			result.append("<survey>").append(cwUtils.NEWL);
			if (content_vec != null && content_vec.contains(SPEC_KEY_CONTENT_LST_CATALOG)) {
				if(d.itm_run_ind == 1){
					if(itemCatalogXMLHash.get(itm_parentId_Has.get(new Long(d.itm_id)))!=null){
					result.append(itemCatalogXMLHash.get(itm_parentId_Has.get(new Long(d.itm_id)))).append(cwUtils.NEWL);
				}
				}else{
					if(itemCatalogXMLHash.get(new Long(d.itm_id))!=null){
						result.append(itemCatalogXMLHash.get(new Long(d.itm_id))).append(cwUtils.NEWL);	
					}
				}
			}
			*/
            long actual_participant = 0;
            Vector vtAdmitUserInRuns = new Vector();
           
			/*
            Vector access_lst = (Vector)h_course_access.get(new Long(d.itm_id));
            if (access_lst != null) {
                result.append("<item_access_list>").append(cwUtils.NEWL);

                for (int j=0; j<access_lst.size(); j++) {
                    ViewCourseReport.ItemAccess access = (ViewCourseReport.ItemAccess)access_lst.elementAt(j);

                    result.append("<item_access id=\"").append(access.usr_id).append("\" last_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(access.usr_last_name_bil))).append("\" first_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(access.usr_first_name_bil))).append("\" display_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(access.usr_display_bil))).append("\" ent_id=\"").append(access.usr_ent_id).append("\" role=\"").append(access.iac_access_id).append("\"/>").append(cwUtils.NEWL);
                }

                result.append("</item_access_list>").append(cwUtils.NEWL);
            }
			*/
            // for survey stat!!
            // for no_run_itm
            Timestamp scope_attempt_start;
            Timestamp scope_attempt_end;
                
            if (d.itm_eff_start_datetime!=null){
//                    scope_attempt_start = d.itm_eff_start_datetime;
                    scope_attempt_start = null;
            }else{
                    scope_attempt_start = start_datetime;
            }
            if (d.itm_eff_end_datetime!=null){
//                    scope_attempt_end = d.itm_eff_end_datetime;
                    scope_attempt_end = null;
            }else{
                    scope_attempt_end = end_datetime;
            }
            long[] tmp_ent_id_lst = null;
            if (d.itm_create_run_ind == 1){
                tmp_ent_id_lst = cwUtils.vec2longArray(vtAdmitUserInRuns);
            }
            result.append(getSurveyAttemptStat(con, scope_attempt_start, scope_attempt_end, ((Long)htItmMod.get(new Long(d.itm_id))).longValue(), tmp_ent_id_lst, null, course_types));
			/*result.append("</survey>").append(cwUtils.NEWL);*/
            result.append("</data>").append(cwUtils.NEWL);
            
        }

        result.append("</report_list>").append(cwUtils.NEWL);
        result.append(page.asXML()).append(cwUtils.NEWL);
        return result.toString();
    }

	public String getXlsFileBySpec(Connection con, HttpServletRequest request, HttpSession sess, cwPagination cwPage,
			loginProfile prof, qdbEnv static_env, WizbiniLoader wizbini, long rte_id, String[] spec_name,
			String[] spec_value, String rsp_title, boolean flag, String timeDir)
            throws cwException, NumberFormatException, SQLException, cwSysMessage, qdbException {
		Map<String, String> params = getValueByName(spec_name, spec_value);
		return getSurveyReportXls(con, wizbini, prof, timeDir, params);
	}

	private Map<String, String> getValueByName(String[] spec_name, String[] spec_value) {
		Map<String, String> params = null;
		if (spec_name != null && spec_value != null && spec_name.length == spec_value.length) {
			params = new HashMap<String, String>();
			for (int i=0; i<spec_name.length; i++) {
                params.put(spec_name[i], spec_value[i]);
            }
		}
		return params;
	}
	
	private String getSurveyReportXls(Connection con, WizbiniLoader wizbini, loginProfile prof, String timeDir,
			Map<String, String> params) throws NumberFormatException, cwException, SQLException, cwSysMessage, qdbException {

		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 通过工作簿创建工作单
		HSSFSheet sheet = wb.createSheet(XLS_NAME_REPORT);
		// 合并单元格
		CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 3);
		sheet.addMergedRegion(cra);
		// 设置单元格类型
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
		cellStyle.setWrapText(true);

		for (int i = 0; i < XLS_COL; i++) {
			sheet.setColumnWidth(i, COL_WIDTH);
			sheet.setDefaultColumnStyle(i, cellStyle);
		}
		/** 创建第一行作为标题行 */
		int index = 0;
		HSSFRow row = sheet.createRow(index++);
		row.setHeight((short)500);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management5"));
		row = sheet.createRow(index++);
		cell = row.createCell(0);
		if (params.get("tnd_id") != null && params.get("tnd_id").length() > 0) {
			cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management6") + " ： ");
			cell = row.createCell(1);
			String[] tnd_ids = params.get("tnd_id").split("~");
			StringBuffer tnd_id_lst = new StringBuffer();
			tnd_id_lst.append("(");
			for (int i = 0; i < tnd_ids.length; i++) {
				if (i != tnd_ids.length - 1) {
					tnd_id_lst.append(tnd_ids[i] + ",");
				} else {
					tnd_id_lst.append(tnd_ids[i]);
				}
			}
			tnd_id_lst.append(")");
			Hashtable tnd_hash = aeTreeNode.getDisplayName(con, tnd_id_lst.toString());
			String tnd_value = "";
			for (int i = 0; i < tnd_ids.length; i++) {
				if (tnd_hash.containsKey(tnd_ids[i])) {
					if (i != tnd_ids.length - 1) {
						tnd_value += tnd_hash.get(tnd_ids[i]) + ", ";
					} else {
						tnd_value += (String) tnd_hash.get(tnd_ids[i]);
					}
				}
			}
			cell.setCellValue(tnd_value);
		} else if (params.get("itm_id") != null && params.get("itm_id").length() > 0) {
			cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management7") + " ： ");
			cell = row.createCell(1);
			String[] itm_ids = params.get("itm_id").split("~");
			String itm_titles = "";
			for (int i = 0; i < itm_ids.length; i++) {
				if (i != itm_ids.length - 1) {
					itm_titles += aeItem.getItemTitle(con, Long.parseLong(itm_ids[i])) + ",";
				} else {
					itm_titles += aeItem.getItemTitle(con, Long.parseLong(itm_ids[i]));
				}
			}
			cell.setCellValue(itm_titles);
		} else {
			cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management8") + " ： ");
			cell = row.createCell(1);
			cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management9"));
		}
		if (params.get("itm_type") != null) {
			row = sheet.createRow(index++);
			cell = row.createCell(0);
			cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management10") + " ： ");
			cell = row.createCell(1);
			String itm_type = "";
			if (params.get("itm_type").indexOf("SELFSTUDY|-|COS") != -1) {
				itm_type += LabelContent.get(prof.cur_lan, "label_core_svy_management11") + ", ";
			}
			if (params.get("itm_type").indexOf("CLASSROOM|-|COS") != -1) {
				itm_type += LabelContent.get(prof.cur_lan, "label_core_svy_management12") + ", ";
			}
			cell.setCellValue(itm_type.substring(0, itm_type.length() - 2));
		}
		if (params.get("mod_id") != null && params.get("mod_id").length() > 0) {
			row = sheet.createRow(index++);
			cell = row.createCell(0);
			cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management13") + " ： ");
			cell = row.createCell(1);
			String[] mod_ids = params.get("mod_id").split("~");
			for (String mod_id : mod_ids) {
				cell.setCellValue(dbResource.getResTitle(con, Long.parseLong(mod_id)));
			}
		}
		// 查询结果
		List<Map<String, Object>> list = null;
		if (params.get("itm_id") != null && params.get("itm_id").length() > 0) {// 课程标题
			String[] tmp = params.get("itm_id").split("~");
			Long[] itmIds = new Long[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				itmIds[i] = Long.parseLong(tmp[i]);
			}
			list = aeItem.getReportByItmId(con, itmIds, null, null, Long.parseLong(params.get("mod_id")),prof.getUsr_ste_usr_id());
		} else if (params.get("tnd_id") != null && params.get("tnd_id").length() > 0 && params.get("itm_type") != null
				&& params.get("itm_type").length() > 0) { // 课程目录
			String[] tmp = params.get("tnd_id").split("~");
			String[] itm_types = params.get("itm_type").split("~");
			for (int j = 0; j < itm_types.length; j++) {
				if (itm_types[j].indexOf("SELFSTUDY|-|COS") != -1) {
					itm_types[j] = ITEM_TYPE_SELFSTUDY;
				}
				if (itm_types[j].indexOf("CLASSROOM|-|COS") != -1) {
					itm_types[j] = ITEM_TYPE_CLASSROOM;
				}
			}
			Long[] cat_ids = new Long[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				cat_ids[i] = Long.parseLong(tmp[i]);
			}
			list = aeItem.getReportByItmId(con, null, cat_ids, itm_types, Long.parseLong(params.get("mod_id")),prof.getUsr_ste_usr_id());
		} else if (params.get("itm_type") != null && params.get("itm_type").length() > 0) {
			String[] itm_types = params.get("itm_type").split("~");
			for (int j = 0; j < itm_types.length; j++) {
				if (itm_types[j].indexOf("SELFSTUDY|-|COS") != -1) {
					itm_types[j] = "SELFSTUDY";
				}
				if (itm_types[j].indexOf("CLASSROOM|-|COS") != -1) {
					itm_types[j] = "CLASSROOM";
				}
			}
			list = aeItem.getReportByItmId(con, null, null, itm_types, Long.parseLong(params.get("mod_id")),prof.getUsr_ste_usr_id());
		}
		
		String[] itm_contents = params.get("itm_content_lst").split("~");
		List<Resources> res_lst = null;
		if (params.get("content_lst") != null && params.get("content_lst").length() > 0 && list.size() > 0) {
			// 平均分
			if (params.get("content_lst").indexOf("question") != -1) {
				res_lst = ModuleSelect.getResourcesByModId(con, Long.parseLong(params.get("mod_id")));
				cra = new CellRangeAddress(index, index, itm_contents.length, itm_contents.length + res_lst.size() - 1);
				sheet.addMergedRegion(cra);
				row = sheet.createRow(index++);
				cell = row.createCell(itm_contents.length);
				cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management14"));
			}
		}
		
		if (list.size() > 0) {
			if (params.get("itm_content_lst") != null && params.get("itm_content_lst").length() > 0) {
				row = sheet.createRow(index++);
				int col = 0;
				for (String itm_content : itm_contents) {
					if ("field01".equals(itm_content)) {
						cell = row.createCell(col++);
						cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management15"));
					}
					if ("field02".equals(itm_content)) {
						cell = row.createCell(col++);
						cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management16"));
					}
					if ("itm_type".equals(itm_content)) {
						cell = row.createCell(col++);
						cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management17"));
					}
					if ("catalog".equals(itm_content)) {
						cell = row.createCell(col++);
						cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management18"));
					}
					if ("training_center".equals(itm_content)) {
						cell = row.createCell(col++);
						cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management19"));
					}
				}
				// 平均分 && 总平均
				if (params.get("content_lst") != null && params.get("content_lst").length() > 0) {
					if (params.get("content_lst").indexOf("question") != -1) {
						for (int i = 0; i < res_lst.size(); i++) {
							cell = row.createCell(col++);
							cell.setCellValue(res_lst.get(i).getRes_title());
						}
					}
					if (params.get("content_lst").indexOf("overall") != -1) {
						cell = row.createCell(col++);
						cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management20"));
					}
				}
			}
			// 列表详情
			if (params.get("itm_content_lst") != null && params.get("itm_content_lst").length() > 0) {
				for (int i = 0; i < list.size(); i++) {
					boolean[] course_types = new boolean[2];
					boolean answer_for_course = false;
					boolean answer_for_lrn_course = false;
					if (params.get("answer_for_course") != null && params.get("answer_for_course") != null) {
						if ("1".equals(params.get("answer_for_course")))
							answer_for_course = true;
						if ("1".equals(params.get("answer_for_lrn_course")))
							answer_for_lrn_course = true;
						course_types[0] = answer_for_course;
						course_types[1] = answer_for_lrn_course;
					}
					aeItem item = (aeItem) list.get(i).get("item");
					aeCatalog catalog = (aeCatalog) list.get(i).get("catalog");
					TcTrainingCenter trainingCenter = (TcTrainingCenter) list.get(i).get("trainingCenter");
					
					ViewSurveyReport.ViewProgressAttemptStat[] pgrAtmStat = ViewSurveyReport.getAttemptStat(con, null,
							null, Long.parseLong(list.get(i).get("res_id").toString()), null, null, course_types);
					index = createRowAndColVal(sheet, pgrAtmStat, params, prof, res_lst, item, catalog, trainingCenter, index, itm_contents);
				}
			}
		}
		String fileName = XLS_NAME_REPORT + ".xls";
		try {
			String basePath = wizbini.getFileUploadTmpDirAbs() + File.separator + timeDir + File.separator;
			File distPath = new File(basePath);
			if (!distPath.exists()) {
				distPath.mkdirs();
			}
			FileOutputStream fout = new FileOutputStream(basePath + fileName);
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	
	}

	private int createRowAndColVal(HSSFSheet sheet, ViewProgressAttemptStat[] pgrAtmStat,Map<String, String> params,  loginProfile prof, List<Resources> res_lst, aeItem item, aeCatalog catalog, TcTrainingCenter trainingCenter, int index, String[] itm_contents) {

		HSSFRow row = sheet.createRow(index++);
		HSSFCell cell = null;
		int col = 0;
		for (String itm_content : itm_contents) {
			if ("field01".equals(itm_content)) {
				cell = row.createCell(col++);
				cell.setCellValue(item.itm_code);
			}
			if ("field02".equals(itm_content)) {
				cell = row.createCell(col++);
				cell.setCellValue(item.itm_title);
			}
			if ("itm_type".equals(itm_content)) {
				cell = row.createCell(col++);
				if("SELFSTUDY".equals(item.itm_type))
					cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management11"));
				else if("CLASSROOM".equals(item.itm_type))
					cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management12"));
			}
			if ("catalog".equals(itm_content)) {
				cell = row.createCell(col++);
				cell.setCellValue(catalog.cat_title);
			}
			if ("training_center".equals(itm_content)) {
				cell = row.createCell(col++);
				cell.setCellValue(trainingCenter.getTcr_title());
			}
		}

		Map<Long, QueData> htQueData = new HashMap<Long, QueData>();
		List<Long> que_ids = new ArrayList<Long>();
		for (int j = 0; j < pgrAtmStat.length; j++) {
			long que_id = pgrAtmStat[j].atm_int_res_id;
			QueData que = htQueData.get(que_id);
			if (que == null) {
				que = new QueData();
				que.id = que_id;
				que.order = pgrAtmStat[j].rcn_order;
			}
			que.response_count += pgrAtmStat[j].total_cnt;
			que.response_score += pgrAtmStat[j].total_score;
			htQueData.put(que_id, que);
			boolean flag = true;
			for (int m = 0; m < que_ids.size(); m++) {
				if (que_id == que_ids.get(m)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				que_ids.add(que_id);
			}
		}
		
		float sum_score = 0;
		if (que_ids.size() > 0) {
			for (int n = 0; n < que_ids.size(); n++) {
				float avg_score = (float) htQueData.get(que_ids.get(n)).response_score
						/ htQueData.get(que_ids.get(n)).response_count;
				sum_score += avg_score;
				// 平均分
				if (params.get("content_lst").indexOf("question") != -1) {
					cell = row.createCell(col++);
					cell.setCellValue(
							new BigDecimal(avg_score).setScale(1, BigDecimal.ROUND_HALF_UP).toString());
				}
			}
			// 总平均
			if (params.get("content_lst").indexOf("overall") != -1) {
				cell = row.createCell(col++);
				cell.setCellValue(new BigDecimal(sum_score / res_lst.size())
						.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
			}
		} else {
			if (params.get("content_lst").indexOf("question") != -1) {
				for (int n = 0; n < res_lst.size(); n++) {
					cell = row.createCell(col++);
					cell.setCellValue("--");
				}
			}
			if (params.get("content_lst").indexOf("overall") != -1) {
				cell = row.createCell(col++);
				cell.setCellValue("--");
			}
		}
		
		return index;
	}

	public static File exportResQue(Connection con, loginProfile prof, long res_id, int sn, String session_dir,
			WizbiniLoader wizbini) throws qdbException, cwSysMessage, SQLException {

		dbResource db_res = new dbResource();
		db_res.res_id = res_id;
		db_res.get(con);
		List<Question> res_lst = db_res.getQuesByParentResId(con, res_id);

		// 创建工作簿
		HSSFWorkbook wb = new HSSFWorkbook();
		// 通过工作簿创建工作单
		HSSFSheet sheet = wb.createSheet("ans-" + res_id + "-" + sn);
		// 合并单元格
		CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 2);
		sheet.addMergedRegion(cra);
		// 设置单元格类型
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中

		for (int i = 0; i < XLS_COL; i++) {
			sheet.setColumnWidth(i, COL_WIDTH);
			sheet.setDefaultColumnStyle(i, cellStyle);
		}
		/** 创建第一行 */
		int index = 0;
		HSSFRow row = sheet.createRow(index++);
		row.setHeight((short)500);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(db_res.res_title);
		row = sheet.createRow(index++);
		cell = row.createCell(0);
		cell.setCellValue(LabelContent.get(prof.cur_lan, "label_core_svy_management1"));
		cell = row.createCell(1);
		cell.setCellValue(res_id + " - " + sn);
		row = sheet.createRow(index++);
		String[] head_str = { LabelContent.get(prof.cur_lan, "label_core_svy_management2"),
				LabelContent.get(prof.cur_lan, "label_core_svy_management3"),
				LabelContent.get(prof.cur_lan, "label_core_svy_management4") };
		for (int i = 0; i < head_str.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(head_str[i]);
		}
		String que_xml = null;
		for (int m = 0; m < res_lst.size(); m++) {
			String total_rst = "";
			String single_rst = "";
			String total_score = "";
			String single_score = "";
			que_xml = res_lst.get(m).getQue_xml();
			if (que_xml.length() > 0) {
				String[] tmp_str = que_xml.replaceAll("/", "").split("><");
				for (String str : tmp_str) {
					if (str.indexOf("score=") != -1 && str.indexOf("condition=") != -1) {
						String[] tmp_str2 = str.split(" ");
						for (String str2 : tmp_str2) {
							if (str2.indexOf("condition=\"") != -1) {
								single_rst = str2.substring(str2.indexOf("\"") + 1, str2.lastIndexOf("\""));
								if ("1".equals(single_rst))
									single_rst = "A";
								if ("2".equals(single_rst))
									single_rst = "B";
								if ("3".equals(single_rst))
									single_rst = "C";
								if ("4".equals(single_rst))
									single_rst = "D";
								if ("5".equals(single_rst))
									single_rst = "E";
								if ("6".equals(single_rst))
									single_rst = "F";
								if ("7".equals(single_rst))
									single_rst = "G";
								if ("8".equals(single_rst))
									single_rst = "H";
								if ("9".equals(single_rst))
									single_rst = "I";
								if ("10".equals(single_rst))
									single_rst = "J";
								total_rst += single_rst + ", ";
							}
							if (str2.indexOf("score=") != -1) {
								single_score = str2.substring(str2.indexOf("\"") + 1, str2.lastIndexOf("\""));
								total_score += single_score + ", ";
							}
						}
					}
				}
			}
			if (total_rst.length() > 0) {
				total_rst = total_rst.substring(0, total_rst.length() - 2);
			}
			if (total_score.length() > 0) {
				total_score = total_score.substring(0, total_score.length() - 2);
			}
			row = sheet.createRow(index++);
			for (int n = 0; n < 3; n++) {
				cell = row.createCell(n);
				if (n == 0)
					cell.setCellValue(m + 1);
				if (n == 1)
					cell.setCellValue(total_rst);
				if (n == 2)
					cell.setCellValue(total_score);
			}
		}
		File file = null;
		try {
			String basePath = session_dir + sn + File.separator + "ans-" + res_id + "-" + sn + ".xls";
			file = new File(basePath);
			FileOutputStream fout = new FileOutputStream(basePath);
			wb.write(fout);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

}