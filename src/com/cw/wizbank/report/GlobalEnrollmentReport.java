package com.cw.wizbank.report;

import java.sql.*;
import java.util.*;
import java.io.*;
import javax.servlet.http.*;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.view.ViewGlobalEnrollmentReport;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwPagination;

public class GlobalEnrollmentReport extends ReportTemplate {

    private static final int SPLITTED_REPORT_SIZE = 750;
    static final int DEFAULT_PAGE_SIZE = 100;
    static final String DEFAULT_SORT_ORDER = "ASC";
	static final int VIEW_REPORT_BY_COURSE = 2;
	static final int VIEW_REPORT_BY_COURSE_CODE = 3;

    static final String SPEC_KEY_ENT_ID = "ent_id";
    static final String SPEC_KEY_ALL_ORG_IND = "all_org_ind";
    static final String SPEC_KEY_START_DATE = "start_datetime";
    static final String SPEC_KEY_END_DATE = "end_datetime";
    static final String SPEC_KEY_DATE_SELECTION = "date_selection";
    static final String SPEC_KEY_REPORT_VIEW = "report_view";
    static final String SPEC_KEY_COURSE_CODE = "course_code";
  
    private static final String YESTERDAY = "yesterday";
    private static final String TODAY = "today";
    private static final String LASTWEEK = "lastweek";
    private static final String THISWEEK = "thisweek";
    private static final String LASTMONTH = "lastmonth";
    private static final String THISMONTH = "thismonth";
    private static final String LAST7DAYS = "last7days";
    private static final String LAST30DAYS = "last30days";
    private static final String LAST60DAYS = "last60days";
    private static final String LAST90DAYS = "last90days";
    private static final String LAST120DAYS = "last120days";

    private static final String SESS_GLOBAL_ENROLLMENT_PAGINATION = "SESS_GLOBAL_ENROLLMENT_PAGINATION";
    private static final String SESS_GLOBAL_ENROLLMENT_DATA = "SESS_GLOBAL_ENROLLMENT_DATA";
    
    public static String getMetaData(Connection con, loginProfile prof) throws SQLException {
        StringBuffer result = new StringBuffer(512);
        result.append("<cur_time>").append(cwSQL.getTime(con)).append("</cur_time>").append(cwUtils.NEWL);
        Vector vSites = acSite.getActiveSites(con);
        result.append("<site_list>");
        for(int i=0; i<vSites.size(); i++) {
            acSite site = (acSite) vSites.elementAt(i);
            result.append("<site ent_id=\"").append(site.ste_ent_id).append("\">")
                  .append("<title>").append(cwUtils.esc4XML(site.ste_name)).append("</title>")
                  .append("</site>");
        }
        result.append("</site_list>");
        return result.toString();
    }

    public String[] getGlobalEnrollmentReport(Connection con, HttpServletRequest request,
                                              HttpSession sess, cwPagination page, 
                                              loginProfile prof, qdbEnv env, 
                                              long rte_id, String[] spec_name, 
                                              String[] spec_value, String rsp_title, 
                                              boolean split_ind)
        throws SQLException, cwSysMessage, cwException, IOException, qdbException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getNewData();
        DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

        data.rte_id = rte_id;
        data.rsp_xml = rpt_spec.rsp_xml;

        String[] reportXML = getGlobalEnrollmentReportHelper(con, sess, page, prof, env, data, split_ind);
        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, 0, rte_id, null, reportXML[i], data.rsp_xml, Report.GLOBAL_ENROLLMENT, rsp_title);
        }
        return reportXML;
    }

    public String[] getGlobalEnrollmentReport(Connection con, HttpServletRequest request,
                                              HttpSession sess, cwPagination page,
                                              loginProfile prof, qdbEnv env, long rsp_id, 
                                              boolean split_ind)
        throws SQLException, cwSysMessage, cwException, IOException, qdbException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);

        String[] reportXML = getGlobalEnrollmentReportHelper(con, sess, page, prof, env, data, split_ind);
        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, rsp_id, 0, null, reportXML[i], null, Report.GLOBAL_ENROLLMENT, null);
        }
        return reportXML;
    }

    public String[] getGlobalEnrollmentReportHelper(Connection con, HttpSession sess,
                                                    cwPagination page, loginProfile prof, 
                                                    qdbEnv env, ViewReportSpec.Data data, 
                                                    boolean split_ind)
        throws SQLException, cwSysMessage, cwException , qdbException {
        Hashtable spec_pairs = getSpecPairs(data.rsp_xml);
        long[] ent_id_lst = null;
        int all_org_ind = 1;
        Timestamp start_date = null;
        Timestamp end_date = null;
        String date_selection = null;
        int report_view = 1;
        String course_code = null;
        Vector spec_values;

		if( spec_pairs.containsKey(SPEC_KEY_REPORT_VIEW) ) {
			spec_values = (Vector)spec_pairs.get(SPEC_KEY_REPORT_VIEW);
			report_view = Integer.parseInt((String) spec_values.elementAt(0));
		}

		if( spec_pairs.containsKey(SPEC_KEY_COURSE_CODE) ) {
			spec_values = (Vector) spec_pairs.get(SPEC_KEY_COURSE_CODE);
			course_code = (String) spec_values.elementAt(0);
		}

        if (spec_pairs.containsKey(SPEC_KEY_ENT_ID)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ENT_ID);
            ent_id_lst = new long[spec_values.size()];
            for (int i=0; i<spec_values.size(); i++) {
                ent_id_lst[i] = Long.parseLong((String)spec_values.elementAt(i));
            }
        }

        if (spec_pairs.containsKey(SPEC_KEY_ALL_ORG_IND)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_ALL_ORG_IND);
            all_org_ind = ((new Integer((String)spec_values.elementAt(0)))).intValue();
        }
        
        if (spec_pairs.containsKey(SPEC_KEY_DATE_SELECTION)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_DATE_SELECTION);
            date_selection = (String)spec_values.elementAt(0);
        }

        if (spec_pairs.containsKey(SPEC_KEY_START_DATE)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_START_DATE);
            start_date = Timestamp.valueOf((String)spec_values.elementAt(0));
        }

        if (spec_pairs.containsKey(SPEC_KEY_END_DATE)) {
            spec_values = (Vector)spec_pairs.get(SPEC_KEY_END_DATE);
            end_date = Timestamp.valueOf((String)spec_values.elementAt(0));
        }

        String[] reportXML=null;
        //split_ind==true, then get Learning Report into a number of small XMLs
        //for tuning purpose when XML is too large (the case of download report)
        //else, just get report into a single XML
        if(split_ind) {
            //Get report data as XML into a Vector
            Vector v_reportData = getSplittedReport(con, sess, page, prof, env, all_org_ind, ent_id_lst, start_date, end_date, date_selection, report_view, course_code);

            //Construct the report data into a number of reportXML
            reportXML = new String[v_reportData.size()];
            for(int i=0; i<v_reportData.size(); i++) {
                reportXML[i] = (String)v_reportData.elementAt(i);
            }
        } else {
            reportXML = new String[1];
            reportXML[0] = getReport(con, sess, page, prof, env, all_org_ind, ent_id_lst, start_date, end_date, date_selection, report_view, course_code);
        }
        return reportXML;
     }

    /**
    Get Report into a number of small XMLs for tuning purpose
    when XML is too large
    */
    public Vector getSplittedReport(Connection con, HttpSession sess, cwPagination page, 
                                    loginProfile prof, qdbEnv env,
                                    int all_org_ind, long[] ent_id_lst, 
                                    Timestamp start_date, Timestamp end_date,
                                    String date_selection, int report_view, String course_code)
        throws SQLException, cwException, cwSysMessage, qdbException {
        Vector v_reportData = new Vector();
        if(page.pageSize <= 0) {
            page.pageSize = SPLITTED_REPORT_SIZE;
        }
        page.curPage = 1;
        cwPagination sess_page = null;
        do {
            v_reportData.addElement(getReport(con, sess, page, prof, env, all_org_ind, ent_id_lst, start_date, end_date, date_selection, report_view, course_code));
            sess_page = (cwPagination)sess.getAttribute(SESS_GLOBAL_ENROLLMENT_PAGINATION);
            page.ts = sess_page.ts;
            page.curPage++;
        }while (page.curPage <= sess_page.totalPage);
        return v_reportData;
    }

    public String getReport(Connection con, HttpSession sess, cwPagination page,
                            loginProfile prof, qdbEnv env, 
                            int all_org_ind, long[] ent_id_lst, 
                            Timestamp start_date, Timestamp end_date,
                            String date_selection, int report_view, String course_code)
        throws SQLException, cwException, cwSysMessage, qdbException {
            
        StringBuffer xmlBuf = new StringBuffer(1024);
        Vector reportData = null;
        Timestamp curTime = cwSQL.getTime(con);
        cwPagination sess_page = (cwPagination)sess.getAttribute(SESS_GLOBAL_ENROLLMENT_PAGINATION);
        if (sess_page != null && sess_page.ts.equals(page.ts)) {
            reportData = (Vector) sess.getAttribute(SESS_GLOBAL_ENROLLMENT_DATA);
            sess_page.curPage = page.curPage;
            sess_page.pageSize = page.pageSize;
            page = sess_page;
        } else {
            //convert date selection into dates
            if(date_selection != null) {
                Timestamp[] beginEndDates = null;
                if(date_selection.equals(TODAY)) {
                    beginEndDates = cwUtils.getTodayBeginEnd(curTime);
                } else if(date_selection.equals(YESTERDAY)) {
                    beginEndDates = cwUtils.getYesterdayBeginEnd(curTime);
                } else if(date_selection.equals(THISWEEK)) {
                    beginEndDates = cwUtils.getWeekBeginEnd(curTime);
                } else if(date_selection.equals(LASTWEEK)) {
                    beginEndDates = cwUtils.getLastWeekBeginEnd(curTime);
                } else if(date_selection.equals(THISMONTH)) {
                    beginEndDates = cwUtils.getMonthBeginEnd(curTime);
                } else if(date_selection.equals(LASTMONTH)) {
                    beginEndDates = cwUtils.getLastMonthBeginEnd(curTime);
                } else if(date_selection.equals(LAST30DAYS)) {
                    beginEndDates = cwUtils.getLastNDaysBeginEnd(curTime, 30);
                } else if(date_selection.equals(LAST60DAYS)) {
                    beginEndDates = cwUtils.getLastNDaysBeginEnd(curTime, 60);
                } else if(date_selection.equals(LAST90DAYS)) {
                    beginEndDates = cwUtils.getLastNDaysBeginEnd(curTime, 90);
                } else if(date_selection.equals(LAST120DAYS)) {
                    beginEndDates = cwUtils.getLastNDaysBeginEnd(curTime, 120);
                }
                if(beginEndDates != null) {
                    start_date = beginEndDates[0];
                    end_date = beginEndDates[1];
                }
            }

            //use ViewGlobalEnrollmentReport to get Report
            reportData = ViewGlobalEnrollmentReport.generateReport(con, all_org_ind, ent_id_lst, start_date, end_date);
            //put data in session
            page.ts = curTime;
            page.totalRec = reportData.size();
            if (page.pageSize == -1 && page.totalRec != 0) {
                page.pageSize = page.totalRec;
            }
            if (page.pageSize == 0) {
                page.pageSize = DEFAULT_PAGE_SIZE;
            }

            if (page.curPage == 0) {
                page.curPage = 1;
            }
            page.totalPage = page.totalRec/page.pageSize;
            if (page.totalRec%page.pageSize !=0) {
                page.totalPage++;
            }
        }
        sess.setAttribute(SESS_GLOBAL_ENROLLMENT_DATA, reportData);
        sess.setAttribute(SESS_GLOBAL_ENROLLMENT_PAGINATION, page);
        xmlBuf.append("<report_list>");
        int start = (page.curPage-1)*page.pageSize; 
        int end = (page.curPage*page.pageSize);
        
        Hashtable h_ste_itm = null;
        if( report_view == VIEW_REPORT_BY_COURSE || report_view == VIEW_REPORT_BY_COURSE_CODE ) {
        	Vector v_site_id = new Vector();
			for(int i=start; i<reportData.size() && i<end; i++) {
				ViewGlobalEnrollmentReport data = (ViewGlobalEnrollmentReport) reportData.elementAt(i);
				v_site_id.addElement(new Long(data.getSiteEntId()));
			}
			h_ste_itm = ViewGlobalEnrollmentReport.getItemEnrollmentCount(con, v_site_id, start_date, end_date, course_code);
        }
        
        long enrollmentCount = 0;
        for(int i=start; i<reportData.size() && i<end; i++) {
            ViewGlobalEnrollmentReport data = (ViewGlobalEnrollmentReport) reportData.elementAt(i);
            Long steId = new Long(data.getSiteEntId());
        	/*
        	if( report_view == VIEW_REPORT_BY_COURSE_CODE && !h_ste_itm.containsKey(steId)) {
        		continue;
        	} 
        	*/    
            xmlBuf.append("<record>")
                  .append("<site ent_id=\"").append(data.getSiteEntId()).append("\">")
                  .append("<title>").append(cwUtils.esc4XML(data.getSiteName())).append("</title>")
                  .append("</site>");
                  //.append("<enrollment_count>").append(data.getEnrollmentCount()).append("</enrollment_count>");
            enrollmentCount = data.getEnrollmentCount();
            if( report_view == VIEW_REPORT_BY_COURSE || report_view == VIEW_REPORT_BY_COURSE_CODE ) {
            	enrollmentCount = 0;
	            if( h_ste_itm.containsKey(steId) ) {
					xmlBuf.append("<course_list>");
	            	Vector v_itm = (Vector) h_ste_itm.get(steId);	            	
	            	for(int j=0; j<v_itm.size(); j++){
						ViewGlobalEnrollmentReport itmData = (ViewGlobalEnrollmentReport) v_itm.elementAt(j);
						xmlBuf.append("<course>")
							   .append("<title>").append(cwUtils.esc4XML(itmData.getItemTitle())).append("</title>")
							   .append("<code>").append(cwUtils.esc4XML(itmData.getItemCode())).append("</code>")
						 	   .append("<enrollment_count>").append(itmData.getEnrollmentCount()).append("</enrollment_count>")
							   .append("</course>");
						enrollmentCount += itmData.getEnrollmentCount();
	            	}
					xmlBuf.append("</course_list>");
	            }
            }
            xmlBuf.append("<enrollment_count>").append(enrollmentCount).append("</enrollment_count>");
            xmlBuf.append("</record>");
        }
        xmlBuf.append("</report_list>");
        return xmlBuf.toString();
    }
    
    
    
}