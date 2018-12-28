package com.cw.wizbank.report;

import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.IOException;

import javax.servlet.http.*;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.view.ViewCourseReport;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.accesscontrol.AcCatalog;
// added for REPORT_VIEW template
import com.cw.wizbank.ae.aeTemplate;
import com.cw.wizbank.ae.db.DbTemplateView;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbObjectView;
import com.cwn.wizbank.utils.CommonLog;

public class CourseReport extends ReportTemplate {

    private static final int SPLITTED_REPORT_SIZE = 750;

    static final int DEFAULT_PAGE_SIZE = 10;
    static final String DEFAULT_SORT_ORDER = "ASC";
    static final String[] DEFAULT_SORT_COL_ORDER = {"itm_title", "itm_version_code"};
    static final String TEMP_COL = "itm_type";

    static final String SESS_COURSE_REPORT_DATA = "SESS_COURSE_REPORT_DATA";
    static final String SESS_COURSE_REPORT_PAGINATION = "SESS_COURSE_REPORT_PAGINATION";
    static final String SESS_COURSE_REPORT_RUN_DATA = "SESS_COURSE_REPORT_RUN_DATA";

    static final String RTE_TYPE_COURSE = "COURSE";

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
    static final String SPEC_KEY_ITM_CONTENT_LST = "itm_content_lst";
    static final String SPEC_KEY_RUN_CONTENT_LST = "run_content_lst";
    static final String SPEC_KEY_CONTENT_LST_VERSION = "version";
    static final String SPEC_KEY_CONTENT_LST_CATALOG = "catalog";
    static final String SPEC_KEY_CONTENT_LST_TYPE = "type";
    static final String SPEC_KEY_CONTENT_LST_TAKE_UP = "take_up";
    static final String SPEC_KEY_CONTENT_LST_ATTENDANCE = "attendance";
    static final String SPEC_KEY_CONTENT_LST_CPE_HRS = "cpe_hrs";
    static final String SPEC_KEY_CONTENT_LST_AVG_RATING = "avg_rating";
    static final String SPEC_KEY_CONTENT_LST_BUDGET = "budget";
    static final String SPEC_KEY_CONTENT_LST_RUN_NUM = "run_num";
    static final String SPEC_KEY_CONTENT_LST_MOTE = "mote";
    static final String SPEC_KEY_CONTENT_LST_MOTE_STATUS = "mote_status";
    static final String SPEC_KEY_CONTENT_LST_INSTRUCTOR = "instructor";
    static final String SPEC_KEY_CONTENT_LST_COS_MANAGER = "cos_manager";
    static final String SPEC_KEY_CONTENT_LST_PROJ_CONST = "proj_const";

    static final String V_DATA = "V_DATA";
    static final String H_COURSE_ACCESS = "H_COURSE_ACCESS";
    static final String H_COURSE_ATTENDANCE = "H_COURSE_ATTENDANCE";
    static final String H_COURSE_MOTE = "H_COURSE_MOTE";
    static final String H_COURSE_WITH_RUN_MOTE = "H_COURSE_WITH_RUN_MOTE";
    static final String H_RUN_DATA = "H_RUN_DATA";
    static final String H_RUN_ATTENDANCE = "H_RUN_ATTENDANCE";
    static final String H_RUN_ACCESS = "H_RUN_ACCESS";
    static final String V_ATS_ID_ATTENDED_IND = "V_ATS_ID_ATTENDED_IND";

    public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long rsp_id, long rte_id) throws cwException, SQLException, cwException, Exception, IOException {
println("getReportTemplate");
        return super.getReport(con, request, prof, rsp_id, rte_id, null, null, null, RTE_TYPE_COURSE, null);
    }

    
    /**
    Old API keep for backward compatibility
    @deprecated
    */
    public String[] getCourseReport(Connection con, HttpServletRequest request,
                                    HttpSession sess, cwPagination page, loginProfile prof,
                                    long rte_id, String[] spec_name, String[] spec_value,
                                    String rsp_title, String tvw_id, boolean split_ind)
        throws SQLException, cwSysMessage, cwException, IOException {

        return getCourseReport(con, request,
                               sess, page, prof,
                               null, rte_id, spec_name, spec_value,
                               rsp_title, tvw_id, split_ind);
    }
    
    public String[] getCourseReport(Connection con, HttpServletRequest request,
                                    HttpSession sess, cwPagination page, loginProfile prof,
                                    qdbEnv env, long rte_id, String[] spec_name, String[] spec_value,
                                    String rsp_title, String tvw_id, boolean split_ind)
        throws SQLException, cwSysMessage, cwException, IOException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;

        String[] reportXML = getCourseReportHelper(con, sess, page, prof, env, data, tvw_id, split_ind);
        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, 0, rte_id, null, reportXML[i], data.rsp_xml, RTE_TYPE_COURSE, rsp_title);
        }
        return reportXML;
        /*
        String xml_suffix = getCourseReportHelper(con, sess, page, prof, data);
        return super.getReport(con, request, prof, 0, rte_id, null, xml_suffix, data.rsp_xml, RTE_TYPE_COURSE, rsp_title);
        */
    }


    /**
    Old API keep for backward compatibility
    */
    public String[] getCourseReport(Connection con, HttpServletRequest request,
                                    HttpSession sess, cwPagination page,
                                    loginProfile prof, long rsp_id,
                                    String tvw_id, boolean split_ind)
        throws SQLException, cwSysMessage, cwException, IOException {

        return getCourseReport(con, request,
                               sess, page,
                               prof, null, rsp_id,
                               tvw_id, split_ind);
    }
    
    public String[] getCourseReport(Connection con, HttpServletRequest request,
                                    HttpSession sess, cwPagination page,
                                    loginProfile prof, qdbEnv env, long rsp_id,
                                    String tvw_id, boolean split_ind)
        throws SQLException, cwSysMessage, cwException, IOException {
println("getCourseReport from rsp_id");
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);
println("1");

        String[] reportXML = getCourseReportHelper(con, sess, page, prof, env, data, tvw_id, split_ind);
        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, rsp_id, 0, null, reportXML[i], null, RTE_TYPE_COURSE, null);
        }
        return reportXML;
        /*
        String xml_suffix = getCourseReportHelper(con, sess, page, prof, data);
        return super.getReport(con, request, prof, rsp_id, 0, null, xml_suffix, null, RTE_TYPE_COURSE);
        */
    }

    public String[] getCourseReportHelper(Connection con, HttpSession sess,
                                          cwPagination page, loginProfile prof,
                                          qdbEnv env, ViewReportSpec.Data data, 
                                          String tvw_id, boolean split_ind)
        throws SQLException, cwSysMessage, cwException {
        Hashtable spec_pairs = getSpecPairs(data.rsp_xml);

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
        Vector spec_values;

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

println("getCourseReport");
        String[] reportXML=null;
        //split_ind==true, then get Learning Report into a number of small XMLs
        //for tuning purpose when XML is too large (the case of download report)
        //else, just get report into a single XML
        if(split_ind) {
            //Get report data as XML into a Vector
            Vector v_reportData = getSplittedReport(con, sess, page, prof, env, 
                                                    tnd_id_lst, tvw_id, null, 
                                                    itm_title, itm_title_partial_ind,
                                                    itm_type, show_run_ind,
                                                    show_old_version, content,
                                                    start_datetime, end_datetime, datetime_restriction,
                                                    (Vector)spec_pairs.get(SPEC_KEY_CONTENT_LST),
                                                    (Vector)spec_pairs.get(SPEC_KEY_ITM_CONTENT_LST),
                                                    (Vector)spec_pairs.get(SPEC_KEY_RUN_CONTENT_LST));

            //Construct the report data into a number of reportXML
            reportXML = new String[v_reportData.size()];
            for(int i=0; i<v_reportData.size(); i++) {
                reportXML[i] = (String)v_reportData.elementAt(i);
            }
        } else {
            reportXML = new String[1];
            reportXML[0] = getReport(con, sess, page, prof, env, tnd_id_lst, tvw_id, null,
                                     itm_title, itm_title_partial_ind, itm_type,
                                     show_run_ind, show_old_version, content,
                                     start_datetime, end_datetime, datetime_restriction,
                                     (Vector)spec_pairs.get(SPEC_KEY_CONTENT_LST),
                                     (Vector)spec_pairs.get(SPEC_KEY_ITM_CONTENT_LST),
                                     (Vector)spec_pairs.get(SPEC_KEY_RUN_CONTENT_LST));
        }
        return reportXML;
        /*
        return getReport(con, sess, page, prof, tnd_id_lst, null, itm_title, itm_title_partial_ind, itm_type, show_run_ind, show_old_version, content, start_datetime, end_datetime, (Vector)spec_pairs.get(SPEC_KEY_CONTENT_LST));
        */
     }

    /**
    Old API keep for backward compatibility
    @deprecated
    */
    public static Vector getSplittedReport(Connection con, HttpSession sess,
                                           cwPagination page, loginProfile prof,
                                           String[] tnd_id_lst, String tvw_id,
                                           String itm_code, String itm_title,
                                           int itm_title_partial_ind,
                                           String[] itm_type, int show_run_ind,
                                           int show_old_version, String[] content,
                                           Timestamp start_datetime, Timestamp end_datetime,
                                           Vector content_vec)
        throws SQLException, cwException, cwSysMessage {

        return getSplittedReport(con, sess,
                                 page, prof, null,
                                 tnd_id_lst, tvw_id,
                                 itm_code, itm_title,
                                 itm_title_partial_ind,
                                 itm_type, show_run_ind,
                                 show_old_version, content,
                                 start_datetime, end_datetime, null,
                                 content_vec, null, null);
    }

    /**
    Get Report into a number of small XMLs for tuning purpose
    when XML is too large
    */
    public static Vector getSplittedReport(Connection con, HttpSession sess,
                                           cwPagination page, loginProfile prof,
                                           qdbEnv env, 
                                           String[] tnd_id_lst, String tvw_id,
                                           String itm_code, String itm_title,
                                           int itm_title_partial_ind,
                                           String[] itm_type, int show_run_ind,
                                           int show_old_version, String[] content,
                                           Timestamp start_datetime, Timestamp end_datetime,
                                           String datetime_restriction,
                                           Vector content_vec, Vector itm_content_vec, 
                                           Vector run_content_vec)
        throws SQLException, cwException, cwSysMessage {
        Vector v_reportData = new Vector();
        if(page.pageSize <= 0) {
            page.pageSize = SPLITTED_REPORT_SIZE;
        }
        page.curPage = 1;
        cwPagination sess_page = null;
        do {
            v_reportData.addElement(getReport(con, sess, page, prof, env, tnd_id_lst,
                                              tvw_id, itm_code, itm_title, itm_title_partial_ind,
                                              itm_type, show_run_ind, show_old_version,
                                              content, start_datetime, end_datetime,
                                              datetime_restriction, 
                                              content_vec, itm_content_vec, run_content_vec));
            sess_page = (cwPagination)sess.getAttribute(SESS_COURSE_REPORT_PAGINATION);
            page.ts = sess_page.ts;
            page.curPage++;
        }while (page.curPage <= sess_page.totalPage);
        return v_reportData;
    }

    /**
    Old API keep for backward compatibility
    @deprecated
    */
    public static String getReport(Connection con, HttpSession sess, cwPagination page,
                                   loginProfile prof, String[] tnd_id_lst,
                                   String tvw_id, String itm_code, String itm_title,
                                   int itm_title_partial_ind, String[] itm_type,
                                   int show_run_ind, int show_old_version,
                                   String[] content, Timestamp start_datetime,
                                   Timestamp end_datetime, Vector content_vec)
        throws SQLException, cwException, cwSysMessage {

        return getReport(con, sess, page,
                         prof, null, tnd_id_lst,
                         tvw_id, itm_code, itm_title,
                         itm_title_partial_ind, itm_type,
                         show_run_ind, show_old_version,
                         content, start_datetime,
                         end_datetime, null, content_vec, null, null);
    }
    
    public static String getReport(Connection con, HttpSession sess, cwPagination page,
                                   loginProfile prof, qdbEnv env, String[] tnd_id_lst,
                                   String tvw_id, String itm_code, String itm_title,
                                   int itm_title_partial_ind, String[] itm_type,
                                   int show_run_ind, int show_old_version,
                                   String[] content, Timestamp start_datetime,
                                   Timestamp end_datetime, String datetime_restriction,
                                   Vector content_vec, Vector itm_content_vec, 
                                   Vector run_content_vec)
        throws SQLException, cwException, cwSysMessage {
        StringBuffer result = new StringBuffer();

//long my_time = System.currentTimeMillis();
//Date my_date = new Date(my_time);
//System.out.println(">>>> TIME (1) = " + my_date.toString());
//println("getReport");
        cwPagination sess_page = (cwPagination)sess.getAttribute(SESS_COURSE_REPORT_PAGINATION);
println("getReport 1");
        ViewCourseReport report = new ViewCourseReport();
        Vector v_data = null;
        Hashtable h_course_access = null;
        Hashtable h_course_attendance = null;
        Hashtable h_course_mote = null;
        Hashtable h_course_with_run_mote = null;
        Hashtable h_run_data = null;
        Hashtable h_run_attendance = null;
        Hashtable h_run_access = null;
        Vector v_ats_id_attended_ind = null;

        if (page.pageSize == 0) {
            page.pageSize = DEFAULT_PAGE_SIZE;
        }

        if (page.curPage == 0) {
            page.curPage = 1;
        }

        if (sess_page != null && sess_page.ts.equals(page.ts)) {
println("getReport 4");
            Hashtable sess_data = (Hashtable)sess.getAttribute(SESS_COURSE_REPORT_DATA);
println("getReport 4.1");
            v_data = (Vector)sess_data.get(V_DATA);
println("getReport 4.2");
            h_course_access = (Hashtable)sess_data.get(H_COURSE_ACCESS);
println("getReport 4.3");
            h_course_attendance = (Hashtable)sess_data.get(H_COURSE_ATTENDANCE);
println("getReport 4.4");
            h_course_mote = (Hashtable)sess_data.get(H_COURSE_MOTE);
println("getReport 4.5");
            h_course_with_run_mote = (Hashtable)sess_data.get(H_COURSE_WITH_RUN_MOTE);
println("getReport 4.6");
            h_run_data = (Hashtable)sess_data.get(H_RUN_DATA);
println("getReport 4.7");
            h_run_attendance = (Hashtable)sess_data.get(H_RUN_ATTENDANCE);
println("getReport 4.8");
            h_run_access = (Hashtable)sess_data.get(H_RUN_ACCESS);
println("getReport 4.9");
            v_ats_id_attended_ind = (Vector)sess_data.get(V_ATS_ID_ATTENDED_IND);
println("getReport 4.10");

            page.totalRec = sess_page.totalRec;

            if (page.pageSize == -1 && page.totalRec != 0) {
                page.pageSize = page.totalRec;
            }
println("getReport 4.11");
            page.totalPage = page.totalRec/page.pageSize;

            if (page.totalRec%page.pageSize !=0) {
                page.totalPage++;
            }

            if (page.sortOrder == null) {
                page.sortOrder = cwPagination.esc4SortSql(DEFAULT_SORT_ORDER);
            }
println("getReport 4.12");
            if (page.sortCol == null) {
    			
                page.sortCol = cwPagination.esc4SortSql(DEFAULT_SORT_COL_ORDER[0]);
            }
println("getReport 4.13");
            if (sess_page.sortCol.equals(page.sortCol) && sess_page.sortOrder.equals(page.sortOrder)) {
                page.sortCol = cwPagination.esc4SortSql(sess_page.sortCol);
                page.sortOrder = cwPagination.esc4SortSql(sess_page.sortOrder);

                
println(">>> DATA FROM SESSION!!!");
            } else {
/* not ready
                StringBuffer str_buf = new StringBuffer();
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

                report = new ViewCourseReport();
                v_data = report.reorder(con, page, v_data, col_buf.toString());

                page.curPage = 1;

                sess_data.put(V_DATA, v_data);
                sess.setAttribute(SESS_COURSE_REPORT_DATA, sess_data);
                sess.setAttribute(SESS_COURSE_REPORT_PAGINATION, page);
println(">>> DATA FROM SESSION AND REORDER!!!");*/
            }
        } else {
println("getReport 5");
            String usr_lst = null;
            StringBuffer str_buf = new StringBuffer();
            Vector itm_lst_vec = null;
//System.out.println(">>>> TIME (2) = " + (System.currentTimeMillis() - my_time));
            if (tnd_id_lst != null) {
                itm_lst_vec = new Vector();
                aeTreeNode.getItemsFromNode(con, tnd_id_lst, itm_lst_vec);

                if (itm_lst_vec.size() == 0) {
                    itm_lst_vec.addElement(new Long(0));
                }
            }
//System.out.println(">>>> TIME (3) = " + (System.currentTimeMillis() - my_time));
println("itm_title = " + itm_title);
println("itm_title_partial_ind = " + itm_title_partial_ind);

            Vector v_itm_type = new Vector();

            if (itm_type != null) {
                for (int i=0; i<itm_type.length; i++) {
                    v_itm_type.addElement(itm_type[i]);
                }
            }

            if (page.sortOrder == null) {
                page.sortOrder = cwPagination.esc4SortSql(DEFAULT_SORT_ORDER);
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
                page.sortCol = cwPagination.esc4SortSql(DEFAULT_SORT_COL_ORDER[0]);
            }

            col_buf.append(str_buf.toString());
            col_buf.append(TEMP_COL).append(" ").append(page.sortOrder);

println("a");
//System.out.println(">>>> TIME (3) = " + (System.currentTimeMillis() - my_time));
            v_ats_id_attended_ind = report.getAttendedIndAtsId(con, prof.root_ent_id);
for (int i=0; i<v_ats_id_attended_ind.size(); i++) {
	CommonLog.debug(">> " + v_ats_id_attended_ind.elementAt(i));   
}
//System.out.println(">>>> TIME (4) = " + (System.currentTimeMillis() - my_time));
println("b");
            Vector v_temp_data = report.search(con, page, prof.root_ent_id, itm_code, itm_lst_vec, itm_title, itm_title_partial_ind, itm_type, show_run_ind, show_old_version, content, col_buf.toString());
//System.out.println(">>>> TIME (5) = " + (System.currentTimeMillis() - my_time));
println("c");
            Vector v_has_run_itm_lst = null;
            Vector v_no_run_itm_lst = null;
            h_course_access = new Hashtable();
            h_course_attendance = new Hashtable();

            if (v_temp_data != null && v_temp_data.size() != 0) {
                v_has_run_itm_lst = new Vector();
                v_no_run_itm_lst = new Vector();
                Vector itm_lst = new Vector();

                for (int i=0; i<v_temp_data.size(); i++) {
                    ViewCourseReport.Data d = (ViewCourseReport.Data)v_temp_data.elementAt(i);

                    if (d.itm_create_run_ind == 1) {
                        v_has_run_itm_lst.addElement(new Long(d.itm_id));
                    } else {
                        v_no_run_itm_lst.addElement(new Long(d.itm_id));
                    }

                    itm_lst.addElement(new Long(d.itm_id));
                }

                if (content_vec != null &&
                    (content_vec.contains(SPEC_KEY_CONTENT_LST_COS_MANAGER) ||
                    content_vec.contains(SPEC_KEY_CONTENT_LST_PROJ_CONST))) {
                    h_course_access = report.searchItemAccess(con, itm_lst);
                }
            }
//System.out.println(">>>> TIME (6) = " + (System.currentTimeMillis() - my_time));
            // ITEM WITHOUT RUN
            if (v_no_run_itm_lst != null && v_no_run_itm_lst.size() > 0 /*&&
                (content_vec.contains(SPEC_KEY_CONTENT_LST_TAKE_UP) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_ATTENDANCE) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_CPE_HRS))*/) {
                    h_course_attendance = report.searchAttendance(con, v_no_run_itm_lst, start_datetime, end_datetime, datetime_restriction, v_ats_id_attended_ind);
            }
//System.out.println(">>>> TIME (6.1) = " + (System.currentTimeMillis() - my_time));

            if (v_no_run_itm_lst != null && v_no_run_itm_lst.size() > 0 &&
                content_vec != null && 
                (content_vec.contains(SPEC_KEY_CONTENT_LST_MOTE) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_MOTE_STATUS) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_TAKE_UP) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_AVG_RATING) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_BUDGET))) {
                h_course_mote = report.searchMoteWithoutRun(con, v_no_run_itm_lst);
            }
//System.out.println(">>>> TIME (6.2) = " + (System.currentTimeMillis() - my_time));

            // ITEM WITH RUN
            h_run_data = new Hashtable();
            Vector v_run_lst = new Vector();

            if (v_has_run_itm_lst != null && v_has_run_itm_lst.size() > 0) {
                v_run_lst = report.searchRun(con, v_has_run_itm_lst, start_datetime, end_datetime, h_run_data);
//System.out.println(">>>> TIME (7.1) = " + (System.currentTimeMillis() - my_time));
                if (content_vec != null && (
                    content_vec.contains(SPEC_KEY_CONTENT_LST_MOTE) ||
                    content_vec.contains(SPEC_KEY_CONTENT_LST_MOTE_STATUS) ||
                    content_vec.contains(SPEC_KEY_CONTENT_LST_TAKE_UP) ||
                    content_vec.contains(SPEC_KEY_CONTENT_LST_AVG_RATING) ||
                    content_vec.contains(SPEC_KEY_CONTENT_LST_BUDGET))) {
                    h_course_with_run_mote = report.searchMoteWithRun(con, v_has_run_itm_lst);
                }
//System.out.println(">>>> TIME (7.2) = " + (System.currentTimeMillis() - my_time));
            }

            v_data = new Vector();
            h_run_attendance = new Hashtable();
            h_run_access = new Hashtable();

            if (v_run_lst != null && v_run_lst.size() > 0 /*&&
                (content_vec.contains(SPEC_KEY_CONTENT_LST_TAKE_UP) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_ATTENDANCE) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_CPE_HRS))*/) {
                h_run_attendance = report.searchAttendance(con, v_run_lst, null, null, null, v_ats_id_attended_ind);
             }
//System.out.println(">>>> TIME (8) = " + (System.currentTimeMillis() - my_time));
            if (v_run_lst != null && v_run_lst.size() > 0 &&
                content_vec != null && (content_vec.contains(SPEC_KEY_CONTENT_LST_COS_MANAGER) ||
                 content_vec.contains(SPEC_KEY_CONTENT_LST_PROJ_CONST))) {
                    h_run_access = report.searchItemAccess(con, v_run_lst);
            }

//System.out.println(">>>> TIME (9) = " + (System.currentTimeMillis() - my_time));

            // Filtering
            if (start_datetime != null || end_datetime != null) {
                if (v_temp_data != null && v_temp_data.size() != 0) {
                    for (int i=0; i<v_temp_data.size(); i++) {
                        ViewCourseReport.Data d = (ViewCourseReport.Data)v_temp_data.elementAt(i);

                        if (d.itm_create_run_ind == 1) {
                            Vector run_lst = (Vector)h_run_data.get(new Long(d.itm_id));

                            if (run_lst != null && run_lst.size() != 0) {
                                for (int j=0; j<run_lst.size(); j++) {
                                    ViewCourseReport.Data run = (ViewCourseReport.Data)run_lst.elementAt(j);
println("start datetime = " + start_datetime);
println("end datetime = " + end_datetime);
println("itm eff start datetime = " + run.itm_eff_start_datetime);
println("itm eff end datetime = " + run.itm_eff_end_datetime);
                                    if (run.itm_eff_start_datetime != null) {
                                        if ((start_datetime != null && end_datetime == null && (run.itm_eff_start_datetime.after(start_datetime) || run.itm_eff_start_datetime.equals(start_datetime))) ||
                                            (start_datetime == null && end_datetime != null && (run.itm_eff_start_datetime.before(end_datetime) || run.itm_eff_start_datetime.equals(end_datetime))) ||
                                            (start_datetime != null && end_datetime != null && (run.itm_eff_start_datetime.after(start_datetime) || run.itm_eff_start_datetime.equals(start_datetime)) && (run.itm_eff_start_datetime.before(end_datetime) || run.itm_eff_start_datetime.equals(end_datetime)))) {
                                            v_data.addElement(d);
println(" ~~~~~~~~ BINGO ~~~~~~~~");
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            ViewCourseReport.Attendance att = (ViewCourseReport.Attendance)h_course_attendance.get(new Long(d.itm_id));

                            if (att != null) {
                            v_data.addElement(d);
                            }
                        }
                    }
                }
            } else {
                v_data = v_temp_data;
            }

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
                sess.setAttribute(SESS_COURSE_REPORT_PAGINATION, page);
            }

            Hashtable sess_data = new Hashtable();

            if (v_data != null) {
                sess_data.put(V_DATA, v_data);
            }

            if (h_course_access != null) {
                sess_data.put(H_COURSE_ACCESS, h_course_access);
            }

            if (h_course_attendance != null) {
                sess_data.put(H_COURSE_ATTENDANCE, h_course_attendance);
            }

            if (h_course_mote != null) {
                sess_data.put(H_COURSE_MOTE, h_course_mote);
            }

            if (h_course_with_run_mote != null) {
                sess_data.put(H_COURSE_WITH_RUN_MOTE, h_course_with_run_mote);
            }

            if (h_run_data != null) {
                sess_data.put(H_RUN_DATA, h_run_data);
            }

            if (h_run_attendance != null) {
                sess_data.put(H_RUN_ATTENDANCE, h_run_attendance);
            }

            if (h_run_access != null) {
                sess_data.put(H_RUN_ACCESS, h_run_access);
            }

            if (v_ats_id_attended_ind != null) {
                sess_data.put(V_ATS_ID_ATTENDED_IND, v_ats_id_attended_ind);
            }

            sess.setAttribute(SESS_COURSE_REPORT_DATA, sess_data);
        }

        Vector itmVec = new Vector();
        for (int i=(page.curPage-1)*page.pageSize; i<v_data.size() && i<(page.curPage*page.pageSize); i++) {
            ViewCourseReport.Data d = (ViewCourseReport.Data)v_data.elementAt(i);
            if (!itmVec.contains(new Long(d.itm_id))) {
                itmVec.addElement(new Long(d.itm_id));
            }
        }

        Hashtable itemCatalogXMLHash = new Hashtable();

        if (content_vec != null && content_vec.contains(SPEC_KEY_CONTENT_LST_CATALOG)) {
            itemCatalogXMLHash = aeItem.getCatalogXMLHash(con, itmVec);
        }
//System.out.println(">>>> TIME (10) = " + (System.currentTimeMillis() - my_time));

        Timestamp cur_time = cwSQL.getTime(con);
        DbTemplateView itmReportView = null;
        DbTemplateView runReportView = null;
        CourseReport cosReport = new CourseReport();
        //get item tempalte view
        if(itm_content_vec != null && itm_content_vec.size() > 0) {
            DbObjectView itemView = 
                DbObjectView.getObjectView(con, prof.root_ent_id, 
                                        DbObjectView.OJV_TYPE_COURSE_REPORT, 
                                        DbObjectView.OJV_SUBTYPE_ITM);
            if(itemView != null) {
                itmReportView = cosReport.createTemplateView(itm_content_vec, cosReport.getItmResultView(itm_content_vec, itemView.getOptionXML()));
            }
        }
        //get run tempalte view
        if(run_content_vec != null && run_content_vec.size() > 0) {
            DbObjectView runView = 
                DbObjectView.getObjectView(con, prof.root_ent_id, 
                                        DbObjectView.OJV_TYPE_COURSE_REPORT, 
                                        DbObjectView.OJV_SUBTYPE_RUN);
            if(runView != null) {
                runReportView = cosReport.createTemplateView(run_content_vec, cosReport.getRunResultView(run_content_vec, runView.getOptionXML()));
            }
        }

        result.append("<report_list>").append(cwUtils.NEWL);

        for (int i=(page.curPage-1)*page.pageSize; i<v_data.size() && i<(page.curPage*page.pageSize); i++) {
            ViewCourseReport.Data d = (ViewCourseReport.Data)v_data.elementAt(i);
            
            result.append("<item id=\"").append(d.itm_id)
                    .append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_code)))
                    .append("\" version=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_version_code)))
                    .append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_title)))
                    .append("\" type=\"").append(cwUtils.escNull(d.itm_type))
                    .append("\" eff_start_datetime=\"").append(aeUtils.escNull(d.itm_eff_start_datetime))
                    .append("\" eff_end_datetime=\"").append(aeUtils.escNull(d.itm_eff_end_datetime))
                    .append("\" nature=\"").append(aeUtils.escNull(d.itm_apply_method))
                    .append("\" unit=\"").append(d.itm_unit)
                    .append("\" status=\"").append(aeUtils.escNull(d.itm_status))
                    .append("\" life_status=\"").append(aeUtils.escNull(d.itm_life_status))
                    .append("\" person_in_charge=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_person_in_charge)))
                    .append("\" cos_res_id=\"").append(d.cos_res_id)
                    .append("\">").append(cwUtils.NEWL);

            //get item details
            if(itm_content_vec!=null && itm_content_vec.size()>0 && env!=null) {
                aeItem item = new aeItem();
                item.itm_id = d.itm_id;
                item.get(con);
                result.append(item.getValuedTemplate(con,item.getItemTemplate(con),itmReportView,false,cur_time,env,prof.usr_ent_id));
            }

            if (content_vec != null && content_vec.contains(SPEC_KEY_CONTENT_LST_CATALOG)) {
                result.append(itemCatalogXMLHash.get(new Long(d.itm_id))).append(cwUtils.NEWL);
            }

            long actual_participant = 0;
            Hashtable attendance = new Hashtable();
            long num_of_run = -1;

            if (d.itm_create_run_ind == 1) {
                num_of_run = 0;
                Vector run_lst = (Vector)h_run_data.get(new Long(d.itm_id));

                if (run_lst != null) {
                    for (int j=0; j<run_lst.size(); j++) {
                        ViewCourseReport.Data run = (ViewCourseReport.Data)run_lst.elementAt(j);

                        if (run.itm_eff_start_datetime != null &&
                            ((start_datetime == null && end_datetime == null) ||
                            (start_datetime != null && end_datetime == null && (run.itm_eff_start_datetime.after(start_datetime) || run.itm_eff_start_datetime.equals(start_datetime))) ||
                            (start_datetime == null && end_datetime != null && (run.itm_eff_start_datetime.before(end_datetime) || run.itm_eff_start_datetime.equals(end_datetime))) ||
                            ((run.itm_eff_start_datetime.after(start_datetime) || run.itm_eff_start_datetime.equals(start_datetime)) && (run.itm_eff_start_datetime.before(end_datetime) || run.itm_eff_start_datetime.equals(end_datetime))))) {

                        ViewCourseReport.Attendance att = (ViewCourseReport.Attendance)h_run_attendance.get(new Long(run.itm_id));
println(0.1);
                        Vector access_lst = (Vector)h_run_access.get(new Long(run.itm_id));
                        num_of_run++;
println(0.2);

                        result.append("<item id=\"").append(run.itm_id).append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(run.itm_code))).append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(run.itm_title))).append("\" type=\"").append(cwUtils.escNull(run.itm_type)).append("\" eff_start_datetime=\"").append(aeUtils.escNull(run.itm_eff_start_datetime)).append("\" eff_end_datetime=\"").append(aeUtils.escNull(run.itm_eff_end_datetime)).append("\" nature=\"").append(aeUtils.escNull(run.itm_apply_method)).append("\" unit=\"").append(run.itm_unit).append("\" status=\"").append(aeUtils.escNull(run.itm_status)).append("\" life_status=\"").append(aeUtils.escNull(run.itm_life_status));

                        if (run.itm_cancellation_type != null) {
                            result.append("\" cancellation_type=\"").append(cwUtils.esc4XML(run.itm_cancellation_type));
                        }

/*                        result.append("\" within_period=\"");
println(0.3);
println("start_datetime = " + start_datetime);
println("end_datetime = " + end_datetime);
println("itm_eff_start_datetime = " + run.itm_eff_start_datetime);
                        if (run.itm_eff_start_datetime != null &&
                            ((start_datetime == null && end_datetime == null) ||
                            (start_datetime != null && end_datetime == null && (run.itm_eff_start_datetime.after(start_datetime) || run.itm_eff_start_datetime.equals(start_datetime))) ||
                            (start_datetime == null && end_datetime != null && (run.itm_eff_start_datetime.before(end_datetime) || run.itm_eff_start_datetime.equals(end_datetime))) ||
                            ((run.itm_eff_start_datetime.after(start_datetime) || run.itm_eff_start_datetime.equals(start_datetime)) && (run.itm_eff_start_datetime.before(end_datetime) || run.itm_eff_start_datetime.equals(end_datetime))))) {
                            result.append("YES");
println(0.4);
                        } else {
                            result.append("NO");
println(0.5);
                        }
println(1.1);*/
                            result.append("\" person_in_charge=\"").append(cwUtils.esc4XML(aeUtils.escNull(run.itm_person_in_charge))).append("\">").append(cwUtils.NEWL);


                        //get run details
                        if(run_content_vec!=null && run_content_vec.size()>0 && env!=null) {
                            aeItem runItem = new aeItem();
                            runItem.itm_id = run.itm_id;
                            runItem.get(con);
                            result.append(runItem.getValuedTemplate(con,runItem.getItemTemplate(con),runReportView,false,cur_time,env,prof.usr_ent_id));
                        }

                        long num_of_attended = 0;
                        Hashtable ats_lst = null;

                        if (att != null) {
                            ats_lst = (Hashtable)att.ats_attendance;
                        }

                        if (ats_lst != null) {
                            Enumeration enum_ats_lst = ats_lst.keys();
                            result.append("<attendance_list>").append(cwUtils.NEWL);
println(1.2);
                            while (enum_ats_lst.hasMoreElements()) {
                                Long ats_id = (Long)enum_ats_lst.nextElement();
                                long num = ((Long)ats_lst.get(ats_id)).longValue();

                                result.append("<attendance id=\"").append(ats_id.toString()).append("\" count=\"").append(num).append("\"/>").append(cwUtils.NEWL);

                                if (v_ats_id_attended_ind.contains(ats_id)) {
                                    num_of_attended += num;
                                }

                                Long course_ats = (Long)attendance.get(ats_id);
                                long temp = 0;

                                if (course_ats != null) {
                                    temp = course_ats.longValue();
                                }

                                temp += num;
                                attendance.put(ats_id, new Long(temp));
                            }

                            actual_participant += num_of_attended;
                            result.append("</attendance_list>").append(cwUtils.NEWL);
println(1.3);
                        }

println(1.4);
                        if (access_lst != null) {
                            result.append("<item_access_list>").append(cwUtils.NEWL);
println(1.5);
                            for (int k=0; k<access_lst.size(); k++) {
                                ViewCourseReport.ItemAccess access = (ViewCourseReport.ItemAccess)access_lst.elementAt(k);

                                result.append("<item_access id=\"").append(access.usr_id).append("\" last_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(access.usr_last_name_bil))).append("\" first_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(access.usr_first_name_bil))).append("\" display_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(access.usr_display_bil))).append("\" ent_id=\"").append(access.usr_ent_id).append("\" role=\"").append(access.iac_access_id).append("\"/>").append(cwUtils.NEWL);
                            }
println(1.6);
                            result.append("</item_access_list>").append(cwUtils.NEWL);
                        }
println(1.7);
                        result.append("</item>").append(cwUtils.NEWL);
                    }
                }
                }
            } else {
println(">>>> I AM HERE 2 = " + d.itm_id);
                ViewCourseReport.Attendance att = (ViewCourseReport.Attendance)h_course_attendance.get(new Long(d.itm_id));
                Hashtable ats_lst = null;

                if (att != null) {
                    ats_lst = (Hashtable)att.ats_attendance;
                }
println(">>1");
                if (ats_lst != null) {
println(">>2");
                    Enumeration enum_ats_lst = ats_lst.keys();

                    while (enum_ats_lst.hasMoreElements()) {
                        Long ats_id = (Long)enum_ats_lst.nextElement();
                        long num = ((Long)ats_lst.get(ats_id)).longValue();

                        if (v_ats_id_attended_ind.contains(ats_id)) {
                            actual_participant += num;
                        }

                        attendance.put(ats_id, new Long(num));
                    }
                }
            }

            if (attendance.size() != 0) {
println(2.1);
                result.append("<attendance_list>").append(cwUtils.NEWL);

                Enumeration enum_attendance = attendance.keys();
println(2.2);
                while (enum_attendance.hasMoreElements()) {
println(2.3);
                    Long ats_id = (Long)enum_attendance.nextElement();
                    Long num = (Long)attendance.get(ats_id);

                    result.append("<attendance id=\"").append(ats_id).append("\" count=\"").append(num).append("\" attend_ind=\"");
            
                    if (v_ats_id_attended_ind.contains(ats_id)) {
                        result.append("1");
                    } else {
                        result.append("0");
                    }
                    
                    result.append("\"/>");
                }
println(2.4);
                result.append("</attendance_list>").append(cwUtils.NEWL);
                result.append("<duration actual=\"").append(d.itm_unit*actual_participant).append("\"/>").append(cwUtils.NEWL);
println(2.5);
            }

println(2.6);
            if (d.itm_create_run_ind == 1) {
                if (h_course_with_run_mote != null) {
                    ViewCourseReport.Mote m = (ViewCourseReport.Mote)h_course_with_run_mote.get(new Long(d.itm_id));
println(2.7);
                    if (m != null) {
                        result.append("<mote status=\"").append(cwUtils.esc4XML(aeUtils.escNull(m.imt_status))).append("\">").append(cwUtils.NEWL);
                        result.append("<level id=\"1\" included=\"").append(m.imt_level1_ind).append("\"/>").append(cwUtils.NEWL);
                        result.append("<level id=\"2\" included=\"").append(m.imt_level2_ind).append("\"/>").append(cwUtils.NEWL);
                        result.append("<level id=\"3\" included=\"").append(m.imt_level3_ind).append("\"/>").append(cwUtils.NEWL);
                        result.append("<level id=\"4\" included=\"").append(m.imt_level4_ind).append("\"/>").append(cwUtils.NEWL);
                        result.append("</mote>").append(cwUtils.NEWL);
println(2.71);
                        result.append("<rating>").append(cwUtils.NEWL);
                        result.append("<target>").append(m.imt_rating_target).append("</target>").append(cwUtils.NEWL);

                        if (m.imt_rating_actual_xml != null) {
                            result.append(m.imt_rating_actual_xml).append(cwUtils.NEWL);
                        }
println(2.72);
                        result.append("</rating>").append(cwUtils.NEWL);
                        result.append("<budget target=\"").append(m.imt_cost_target).append("\" actual=\"").append(m.imt_cost_actual).append("\"/>").append(cwUtils.NEWL);
                        result.append("<participant target=\"").append(cwUtils.escNull(m.imt_participant_target)).append("\" actual=\"").append(actual_participant).append("\" take_up=\"");

                        if (m.imt_participant_target != null) {
                            result.append(CourseReport.average((new Long(m.imt_participant_target)).longValue(), actual_participant));
                        }

                        result.append("\"/>").append(cwUtils.NEWL);
println(2.73);
                    }
                }
            } else {
println(2.8);
                if (h_course_mote != null) {
                    ViewCourseReport.MoteDefault m = (ViewCourseReport.MoteDefault)h_course_mote.get(new Long(d.itm_id));

                    if (m != null) {
                        result.append("<mote>").append(cwUtils.NEWL);
                        result.append("<level id=\"1\" included=\"").append(m.imd_level1_ind).append("\"/>").append(cwUtils.NEWL);
                        result.append("<level id=\"2\" included=\"").append(m.imd_level2_ind).append("\"/>").append(cwUtils.NEWL);
                        result.append("<level id=\"3\" included=\"").append(m.imd_level3_ind).append("\"/>").append(cwUtils.NEWL);
                        result.append("<level id=\"4\" included=\"").append(m.imd_level4_ind).append("\"/>").append(cwUtils.NEWL);
                        result.append("</mote>").append(cwUtils.NEWL);
                        result.append("<participant actual=\"").append(actual_participant).append("\"/>").append(cwUtils.NEWL);
                    }
                }
            }

println(2.9);
            if (num_of_run != -1) {
                result.append("<num_of_run count=\"").append(num_of_run).append("\"/>").append(cwUtils.NEWL);
            }

            Vector access_lst = (Vector)h_course_access.get(new Long(d.itm_id));
println(2.10);
            if (access_lst != null) {
                result.append("<item_access_list>").append(cwUtils.NEWL);

                for (int j=0; j<access_lst.size(); j++) {
                    ViewCourseReport.ItemAccess access = (ViewCourseReport.ItemAccess)access_lst.elementAt(j);

                    result.append("<item_access id=\"").append(access.usr_id).append("\" last_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(access.usr_last_name_bil))).append("\" first_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(access.usr_first_name_bil))).append("\" display_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(access.usr_display_bil))).append("\" ent_id=\"").append(access.usr_ent_id).append("\" role=\"").append(access.iac_access_id).append("\"/>").append(cwUtils.NEWL);
                }

                result.append("</item_access_list>").append(cwUtils.NEWL);
            }

            // added for REPORT_VIEW template
            // retrieve the template & template_view
            if (tvw_id != null && tvw_id.length() != 0) {
                aeItem itm = new aeItem(con);
                itm.itm_id = d.itm_id;
                // template
                aeTemplate tpl = itm.getItemTemplate(con);
                // template view: REPORT_VIEW
                DbTemplateView dbTplView = new DbTemplateView();
                dbTplView.tvw_tpl_id = tpl.tpl_id;
                dbTplView.tvw_id = tvw_id;
                dbTplView.get(con);

                result.append(dbTplView.tvw_xml).append(cwUtils.NEWL);
                result.append(tpl.tpl_xml).append(cwUtils.NEWL);
                // item detail
                result.append(d.itm_xml).append(cwUtils.NEWL);
            }

            result.append("</item>").append(cwUtils.NEWL);
        }

        result.append("</report_list>").append(cwUtils.NEWL);
        result.append(page.asXML()).append(cwUtils.NEWL);
//System.out.println(">>>> TIME (11) = " + (System.currentTimeMillis() - my_time));
        return result.toString();
    }

    public static String average(long target, long actual) {
        String score = "0.00";

        if (target != 0) {
            float percent = (new Float(actual)).floatValue()/(new Float(target)).floatValue();
            float temp = (new Float(percent * 10000)).longValue();
            float temp2 = temp/(new Float(100)).floatValue();

            score = (new Float(temp2)).toString();

            if ((new Long((new Float(temp)).longValue())).toString().endsWith("0")) {
                score += "0";
            }
        }
println("Average=" + score + "; target=" + target + "; actual=" + actual);
        return score;
    }

    public static String getMetaData(Connection con, loginProfile prof, WizbiniLoader wizbini) throws SQLException, cwSysMessage {
        StringBuffer result = new StringBuffer();

        result.append("<cur_time>").append(cwSQL.getTime(con)).append("</cur_time>").append(cwUtils.NEWL);
        //result.append(aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id));
        result.append(aeItem.getApplicableItemTypeTitleInOrg(con, prof.root_ent_id));
        result.append(dbUtils.getAllRoleAsXML(con, "role_list", prof.root_ent_id));
        result.append(getAttendanceStatusList(con, prof.root_ent_id));
        result.append(getCatalogTitleList(con, prof,  wizbini));

        return result.toString();
    }

    /** 
    * Construct the catalog list as xml    
    */
    public static String getCatalogTitleList(Connection con, loginProfile prof, WizbiniLoader wizbini)
        throws SQLException, cwSysMessage{
            boolean checkStatus = false;
            aeCatalog cat = new aeCatalog();
            String xml = null;
            try{
                xml = cat.getTitleListAsXML(con, prof, checkStatus, null,  wizbini);
            }catch(qdbException e){
                throw new SQLException(e.getMessage());
            }
            return xml;
        }

}