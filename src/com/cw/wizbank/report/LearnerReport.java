package com.cw.wizbank.report;

import java.net.*;
import java.io.IOException;
import java.sql.*;
import java.util.Vector;
import java.util.Hashtable;

import com.cw.wizbank.qdb.dbAiccPath;

import javax.servlet.http.*;

import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.ae.aeTemplate;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.db.view.ViewLearnerReport;
import com.cw.wizbank.ae.db.view.ViewReportSpec;
import com.cw.wizbank.ae.db.DbReportSpec;
import com.cw.wizbank.ae.db.DbTemplateView;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.db.view.ViewCfCertificate;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.db.DbObjectView;
import com.cw.wizbank.accesscontrol.AcCatalog;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.config.WizbiniLoader;
import com.cwn.wizbank.utils.CommonLog;

public class LearnerReport extends ReportTemplate {
    /**
    When download learner report, it's too slow to perform transformation if the XML is
    big, say 10k records. Use this value to split one big transformation to a number of
    smaller transformation to improve efficency
    */
    static final int SPLITTED_REPORT_SIZE = 750;

    static final String YEAR_FIRST_DAY = "-01-01 00:00:00.0";
    static final String YEAR_LAST_DAY = "-12-31 23:59:59.99999999";
    static final String DELIMITER = "~";

    static final String SESS_LEARNER_REPORT_DATA = "SESS_LEARNER_REPORT_DATA";
    static final String SESS_LEARNER_REPORT_TOTAL_UNITS = "SESS_LEARNER_REPORT_TOTAL_UNITS";
    static final String SESS_LEARNER_REPORT_PAGINATION = "SESS_LEARNER_REPORT_PAGINATION";
    static final String SESS_LEARNER_REPORT_MODULE_COUNT = "SESS_LEARNER_REPORT_MODULE_COUNT";

    static final String RTE_TYPE_LEARNER = "LEARNER";

    static final int DEFAULT_PAGE_SIZE = 100;
    static final String DEFAULT_SORT_ORDER = "DESC";
    static final String TEMP_COL = "app_id";
    public long v_itm_id = 0;
    public long v_ent_id = 0;
    public boolean is_realTime_view_rpt;
      public String[] DEFAULT_SORT_COL_ORDER = {"att_timestamp", "usr_ste_usr_id","usr_display_bil", "att_create_timestamp", "att_ats_id","t_code","t_title"};

    public String getReportTemplate(Connection con, HttpServletRequest request, loginProfile prof, long usr_ent_id, long rsp_id, long rte_id) throws SQLException, qdbException, cwException, IOException, MalformedURLException, Exception {
        String xml_prefix = null;

        if (usr_ent_id > 0) {
            xml_prefix = getUserInfo(con, usr_ent_id);
        }

        return super.getReport(con, request, prof, rsp_id, rte_id, xml_prefix, null, null, RTE_TYPE_LEARNER, null);
     }

    /**
    Old API keep for backward compatibility
    @deprecated
    KIM 20030710 : wanna remove 
    */    
    /*
    public String[] getLearningReport(Connection con, HttpSession sess, cwPagination page, loginProfile prof, long usr_ent_id, String calendar_year, String[] ent_id_lst, String[] tnd_id_lst, String itm_title, int itm_title_partial_ind, String[] ats_id_lst, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Timestamp att_start_datetime, Timestamp att_end_datetime, boolean split_ind) throws SQLException, qdbException, cwSysMessage {
        try {
            return getLearningReport(con, sess, page, prof, null, usr_ent_id, calendar_year, ent_id_lst, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, att_start_datetime, att_end_datetime, split_ind, false);

        } catch(cwException e) {
            throw new qdbException(e.getMessage());
        }
    }
    */

    /**
    Return Learning report XML. If split_ind == true, will split the XML into
    an array of small XML for performance tuning (in the case of downloading report)
    KIM 20030710 : wanna remove 
    */
    /*
    public String[] getLearningReport(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long usr_ent_id, String calendar_year, String[] ent_id_lst, String[] tnd_id_lst, String itm_title, int itm_title_partial_ind, String[] ats_id_lst, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Timestamp att_start_datetime, Timestamp att_end_datetime, boolean split_ind, boolean override_appr_usg) throws SQLException, qdbException, cwSysMessage, cwException {
        return getLearningReport(con, sess, page, prof, env, usr_ent_id, calendar_year, ent_id_lst, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, null, null, att_start_datetime, att_end_datetime, split_ind, override_appr_usg, -1L, null, null);
    }
    */

//overload to accept spec field
    public String[] getLearningReport(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long usr_ent_id, String calendar_year, String[] ent_id_lst, String[] tnd_id_lst, String[] ats_id_lst, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, boolean split_ind, boolean override_appr_usg, long rte_id, String[] spec_name, String[] spec_value, WizbiniLoader wizbini,boolean isMyStaff, String lrn_scope_sql) throws SQLException, qdbException, cwSysMessage, cwException {
// newly added
        Hashtable spec_pairs = new Hashtable();
        if(spec_name != null && spec_value != null) {
            ViewReportSpec spec = new ViewReportSpec();
            ViewReportSpec.Data data = spec.getNewData();
            DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

            data.rte_id = rte_id;
            data.rsp_xml = rpt_spec.rsp_xml;
            spec_pairs = getSpecPairs(data.rsp_xml);
        }

        StringBuffer xmlHeader = new StringBuffer();
        String userXML = null;

        if (usr_ent_id > 0) {
            ent_id_lst = new String[1];
            ent_id_lst[0] = (new Long(usr_ent_id)).toString();

            xmlHeader.append(getUserInfo(con, usr_ent_id));
            /*
            if (calendar_year == null && att_start_datetime == null && att_end_datetime == null && att_create_start_datetime == null && att_create_end_datetime == null ) {
                aeTimeField time =  new aeTimeField(cwSQL.getTime(con));
                calendar_year = (new Integer(time.getYear())).toString();
				att_start_datetime = Timestamp.valueOf(calendar_year + YEAR_FIRST_DAY);
				att_end_datetime = Timestamp.valueOf(calendar_year + YEAR_LAST_DAY);                
            }
            xmlHeader.append("<calendar year=\"").append(calendar_year).append("\"/>");
            if( calendar_year != null && calendar_year.equals("0")){
                att_start_datetime = null;
                att_end_datetime = null;
            }*/
            //get the user's current profile
            dbRegUser user = new dbRegUser();
            user.usr_ent_id = usr_ent_id;
            user.get(con);
            userXML = user.getUserXML(con, prof, wizbini);
        }

        String[] reportXML=null;
        //split_ind==true, then get Learning Report into a number of small XMLs
        //for tuning purpose when XML is too large (the case of download report)
        //else, just get report into a single XML
        if(split_ind) {
            //Get report data as XML into a Vector
            Vector v_reportData = new Vector();
            if(spec_name != null && spec_value != null) {
                v_reportData = getSplittedReport(con, sess, page, prof, env, ent_id_lst, null, tnd_id_lst, ats_id_lst, att_create_start_datetime, att_create_end_datetime, null, null, (Vector)spec_pairs.get("itm_content_lst"), null, override_appr_usg, wizbini,isMyStaff,lrn_scope_sql);
            } else {
                v_reportData = getSplittedReport(con, sess, page, prof, env, ent_id_lst, null, tnd_id_lst, ats_id_lst, att_create_start_datetime, att_create_end_datetime, null, null, null, null, override_appr_usg, wizbini, isMyStaff,lrn_scope_sql);
            }

            //Construct the report data into a number of reportXML
            reportXML = new String[v_reportData.size()];
            for(int i=0; i<v_reportData.size(); i++) {
                StringBuffer tempBuf = new StringBuffer(xmlHeader.toString());
                tempBuf.append((String)v_reportData.elementAt(i))
                       .append(cwUtils.NEWL);
                reportXML[i] = tempBuf.toString();
            }
        } else {
            if(spec_name != null && spec_value != null) {
                xmlHeader.append(getReport(con, sess, page, prof, env, ent_id_lst, null, tnd_id_lst, ats_id_lst, att_create_start_datetime, att_create_end_datetime, null, null, (Vector)spec_pairs.get("itm_content_lst"), null, override_appr_usg, null,isMyStaff,lrn_scope_sql));
            } else {
                xmlHeader.append(getReport(con, sess, page, prof, env, ent_id_lst, null, tnd_id_lst, ats_id_lst, att_create_start_datetime, att_create_end_datetime, null, null, null, null, override_appr_usg, null,isMyStaff,lrn_scope_sql));
            }
            xmlHeader.append(cwUtils.NEWL);
            reportXML = new String[1];
            reportXML[0] = xmlHeader.toString();
        }
        
        String[] spec_name1 = {"ent_id", "att_create_start_datetime", "att_create_end_datetime"};
        String[] spec_value1 = {toList(ent_id_lst, "~"), "", ""};
        if(att_create_start_datetime != null){
			spec_value1[1] = att_create_start_datetime.toString();
        }
       	if(att_create_end_datetime != null){
            spec_value1[2] = att_create_end_datetime.toString();
        }
        DbReportSpec spec = Report.toSpec(0, null, spec_name1, spec_value1);
        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, null, prof, 0, 0, userXML, reportXML[i], spec.rsp_xml, RTE_TYPE_LEARNER, null);
        }
        return reportXML;
    }

    /**
    Convert the input String array in to a String list separated by the input separator
    @param in String array to be converted into list
    @param separator separator used to separate values in String array
    */
    private static String toList(String[] in, String separator) {
        StringBuffer out = new StringBuffer();
        if(in!=null) {
            for(int i=0; i<in.length; i++) {
                out.append(in[i]);
                if(i>0) {
                    out.append(separator).append(in[i]);
                }
            }
        }
        return out.toString();
    }

    /**
    Old API keep for backward compatibility
    @deprecated
    KIM 20030710 : wanna remove 
    */
    /*
    public String[] getLearningReport(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, long rte_id, String[] spec_name, String[] spec_value, long usr_ent_id, String rsp_title, boolean split_ind) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        return getLearningReport(con, request, sess, page, prof, null, rte_id, spec_name, spec_value, usr_ent_id, rsp_title, split_ind, false);
    }*/
    
    /**
    Return Learning report XML. If split_ind == true, will split the XML into
    an array of small XML for performance tuning (in the case of downloading report)
    */
    public String[] getLearningReport(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long rte_id, String[] spec_name, String[] spec_value, long usr_ent_id, String rsp_title, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
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
        
        String[] reportXML = getLearningReportHelper(con, sess, page, prof, env, data, usr_ent_id, split_ind, override_appr_usg, wizbini); 

        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, 0, rte_id, xml_prefix, reportXML[i], data.rsp_xml, RTE_TYPE_LEARNER, rsp_title);
        }
        return reportXML;
    }

    /**
    Old API keep for backward compatibility
    @deprecated
    KIM 20030710 : wanna remove 
    */
    /*
    public String[] getLearningReport(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, long rsp_id, boolean split_ind) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        return getLearningReport(con, request, sess, page, prof, rsp_id, split_ind);
    }
    */
    
    /**
    Return Learning report XML. If split_ind == true, will split the XML into
    an array of small XML for performance tuning (in the case of downloading report)
    */
    public String[] getLearningReport(Connection con, HttpServletRequest request, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long rsp_id, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException, IOException, MalformedURLException {
        ViewReportSpec spec = new ViewReportSpec();
        ViewReportSpec.Data data = spec.getTemplateAndSpec(con, rsp_id);

        String[] reportXML = getLearningReportHelper(con, sess, page, prof, env, data, 0, split_ind, override_appr_usg, wizbini);

        for(int i=0; i<reportXML.length; i++) {
            reportXML[i] = super.getReport(con, request, prof, rsp_id, 0, null, reportXML[i], null, RTE_TYPE_LEARNER, null);
        }
        return reportXML;
    }

    public String[] getLearningReportHelper(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, ViewReportSpec.Data data, long usr_ent_id, boolean split_ind, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException,IOException {

        Hashtable spec_pairs = getSpecPairs(data.rsp_xml);

        String[] ent_id_lst = null;
        String[] s_usg_ent_id_lst = null;
        String[] tnd_id_lst = null;
        String[] ats_id_lst = null;
        long[] itm_id_lst = null;
        Timestamp att_create_start_datetime = null;
        Timestamp att_create_end_datetime = null;
        boolean isMyStaff = false;
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
            if(all_user_ind && spec_pairs.containsKey("answer_for_lrn") && spec_pairs.containsKey("answer_for_course_lrn")) {
            	if((answer_for_lrn && answer_for_course_lrn )||(!answer_for_lrn && !answer_for_course_lrn )) {
            	    lrn_scope_sql = ViewLearnerReport.getAllLrnSql(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
            	} else if (answer_for_lrn && !answer_for_course_lrn) {
            	    lrn_scope_sql = ViewLearnerReport.getMyRspLrnSql(Report.COLNAME_TMP_USR_ENT_ID ,prof.usr_ent_id, prof.root_ent_id);
            	} else if (!answer_for_lrn && answer_for_course_lrn) {
            	    lrn_scope_sql = ViewLearnerReport.getMyRspCosEnrollLrn(Report.COLNAME_TMP_USR_ENT_ID, prof.usr_ent_id, prof.root_ent_id);
            	}
            }
            if(!spec_pairs.containsKey("tnd_id") && !spec_pairs.containsKey("itm_id")) {
            	all_cos_ind = true;
            }
            if(all_cos_ind &&  spec_pairs.containsKey("answer_for_course") && spec_pairs.containsKey("answer_for_lrn_course")) {
            	Vector vec = null;
            	if((answer_for_course && answer_for_lrn_course )||(!answer_for_course && !answer_for_lrn_course )) {
            		vec = ViewLearnerReport.getAllCos(con, prof.usr_ent_id, prof.root_ent_id);
            	} else if (answer_for_course && !answer_for_lrn_course) {
            		vec = ViewLearnerReport.getMyRspCos(con, prof.usr_ent_id, prof.root_ent_id);
            	} else if (!answer_for_course && answer_for_lrn_course) {
            		vec = ViewLearnerReport.getMyRspLrnEnrollCos(con, prof.usr_ent_id, prof.root_ent_id);
            	}
            	if(vec == null || vec.size() == 0) {
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
    	if(spec_pairs.containsKey("is_my_staff")) {
    		spec_values = (Vector)spec_pairs.get("is_my_staff");
    		if(((String)spec_values.get(0)).equals("true")) {
    			isMyStaff  = true;
    		}
	    }
        if (spec_pairs.containsKey("ent_id")) {
            spec_values = (Vector)spec_pairs.get("ent_id");

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
			// in case there is no direct subordinate
            v_ent_id.add(new Long(0));
            
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

		if (spec_pairs.containsKey("ats_id")) {
		   spec_values = (Vector)spec_pairs.get("ats_id");
		   // determine "ALL", donald update at 18:00,04,16,2004
		   if(((String)spec_values.elementAt(0)).equals("0")){
			   ats_id_lst=new String[10];
			   try{
				  for(int i=0;i<4;i++){
					 ats_id_lst[i]=Integer.toString(i+1);
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

        if (spec_pairs.containsKey("att_create_start_datetime")) {

            spec_values = (Vector)spec_pairs.get("att_create_start_datetime");
            CommonLog.debug("DENNIS: att_create = " + att_create_start_datetime);
            //报表统计开始时间
            att_create_start_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
        }
        if (spec_pairs.containsKey("att_create_end_datetime")) {
            spec_values = (Vector)spec_pairs.get("att_create_end_datetime");
            //报表统计结束时间
            att_create_end_datetime = Timestamp.valueOf((String)spec_values.elementAt(0));
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
			page.sortCol=col;
			v=null;
		}
		if((v=(Vector)spec_pairs.get("sort_order"))!=null){
			page.sortOrder=(String)v.elementAt(0);
			v=null;
		}
		
		String[] reportXML=null;
        if(split_ind) {
            //Get report data as XML into a Vector
            Vector v_reportData = getSplittedReport(con, sess, page, prof, env, ent_id_lst, itm_id_lst, tnd_id_lst, ats_id_lst, att_create_start_datetime, att_create_end_datetime, (Vector)spec_pairs.get("content_lst"), (Vector)spec_pairs.get("usr_content_lst"), (Vector)spec_pairs.get("itm_content_lst"), (Vector)spec_pairs.get("run_content_lst"), override_appr_usg, wizbini,isMyStaff,lrn_scope_sql);

            //Construct the report data into a number of reportXML
            reportXML = new String[v_reportData.size()];
            for(int i=0; i<v_reportData.size(); i++) {
                reportXML[i] = (String)v_reportData.elementAt(i);
            }
        } else {
            reportXML = new String[1];
            reportXML[0] = getReport(con, sess, page, prof, env, ent_id_lst, itm_id_lst, tnd_id_lst, ats_id_lst, att_create_start_datetime, att_create_end_datetime, (Vector)spec_pairs.get("content_lst"), (Vector)spec_pairs.get("usr_content_lst"), (Vector)spec_pairs.get("itm_content_lst"), (Vector)spec_pairs.get("run_content_lst"), override_appr_usg, wizbini,isMyStaff,lrn_scope_sql);
        }
        return reportXML;
    }

    /**
    Old API keep for backward compatibility
    @deprecated
    KIM 20030710 : wanna remove 
    */
    /*
    public Vector getSplittedReport(Connection con, HttpSession sess, cwPagination page, loginProfile prof, String[] ent_id_lst, String[] ugr_ent_id_lst, String[] tnd_id_lst, String itm_title, int itm_title_partial_ind, String[] ats_id_lst, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Timestamp att_start_datetime, Timestamp att_end_datetime, String[] app_status_lst, boolean show_itm_credit) throws SQLException, qdbException, cwSysMessage {
        try {
            return getSplittedReport(con, sess, page, prof, null, ent_id_lst, ugr_ent_id_lst, null, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, null, null, att_start_datetime, att_end_datetime, app_status_lst, null, null, null, null, show_itm_credit, false);
        } catch (cwException e) {
            throw new qdbException(e.getMessage());
        }
    }
    */
    
    /**
    Get Report into a number of small XMLs for tuning purpose
    when XML is too large
    */
    public Vector getSplittedReport(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, String[] ent_id_lst, long[] itm_id_lst, String[] tnd_id_lst, String[] ats_id_lst, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, Vector content_vec, Vector usr_content_vec, Vector itm_content_vec, Vector run_content_vec, boolean override_appr_usg, WizbiniLoader wizbini,boolean isMyStaff,String lrn_scope_sql) throws SQLException, qdbException, cwSysMessage, cwException {
        Vector v_reportData = new Vector();
        if(page.pageSize <= 0) {
            page.pageSize = SPLITTED_REPORT_SIZE;
        }
        page.curPage = 1;
        cwPagination sess_page = null;
        do {
            v_reportData.addElement(getReport(con, sess, page, prof, env, ent_id_lst, itm_id_lst, tnd_id_lst, ats_id_lst, att_create_start_datetime, att_create_end_datetime, content_vec, usr_content_vec, itm_content_vec, run_content_vec, override_appr_usg, wizbini,isMyStaff,lrn_scope_sql));
            sess_page = (cwPagination)sess.getAttribute(SESS_LEARNER_REPORT_PAGINATION);
            page.ts = sess_page.ts;
            page.curPage++;
        }while (page.curPage <= sess_page.totalPage);
        return v_reportData;
    }

    public String[] getApprGroups(Connection con, loginProfile prof) throws SQLException {
        //get prof's approval groups
        ViewRoleTargetGroup[] apprGroups = 
            ViewRoleTargetGroup.getTargetGroups(con, prof.usr_ent_id, prof.current_role, true, true);
        String[] appr_group = new String[apprGroups.length];
        for(int i=0; i < appr_group.length; i++) {
            appr_group[i] = "" + apprGroups[i].targetEntIds[0];
        }
        return appr_group;
    }


    /**
    Old API kept for backward compatibility
    @deprecated
    KIM 20030710 : wanna remove 
    */
    /*
    public String getReport(Connection con, HttpSession sess, cwPagination page, loginProfile prof, String[] ent_id_lst, String[] ugr_ent_id_lst, String[] tnd_id_lst, String itm_title, int itm_title_partial_ind, String[] ats_id_lst, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Timestamp att_start_datetime, Timestamp att_end_datetime, String[] app_status_lst, boolean show_itm_credit) throws SQLException, qdbException, cwSysMessage {
        try {
            return getReport(con, sess, page, prof, null, ent_id_lst, ugr_ent_id_lst, null, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, null, null, att_start_datetime, att_end_datetime, app_status_lst, null, null, null, null, show_itm_credit, false);
        } catch (cwException e) {
            throw new qdbException(e.getMessage());
        }
    }
    */
    
    public String getReport(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, String[] ent_id_lst, long[] itm_id_lst, String[] tnd_id_lst, String[] ats_id_lst, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, Vector content_vec, Vector usr_content_vec, Vector itm_content_vec, Vector run_content_vec, boolean override_appr_usg, WizbiniLoader wizbini,boolean isMyStaff, String lrn_scope_sql) throws SQLException, qdbException, cwSysMessage, cwException {
        StringBuffer result = new StringBuffer();
        //get accessible user groups if prof is approver
        if(!override_appr_usg && (ent_id_lst == null || ent_id_lst.length == 0)
            && dbUtils.isUserApprRole(con, prof.usr_ent_id, prof.current_role)) {
            ent_id_lst = getApprGroups(con, prof);
        }
        
        Vector v_data = null;
        if (page.pageSize == 0) {
            page.pageSize = DEFAULT_PAGE_SIZE;
        }

        if (page.curPage == 0) {
            page.curPage = 1;
        }

        StringBuffer str_buf = new StringBuffer();
        Vector itm_id_vec = null;

        if (itm_id_lst!=null){
            itm_id_vec = new Vector();
            aeItem itm = null;
            Vector childItm;
            for (int i=0; i<itm_id_lst.length; i++){
                itm = new aeItem();
                itm.itm_id = itm_id_lst[i];
                if (itm.getCreateRunInd(con)){
                    childItm = aeItemRelation.getChildItemId(con, itm_id_lst[i]); 
                    for (int k=0; k<childItm.size(); k++){
                        itm_id_vec.addElement((Long)childItm.elementAt(k));
                    }
                } 
                itm_id_vec.addElement(new Long(itm_id_lst[i]));
            }
        }else if (tnd_id_lst != null) {
            itm_id_vec = new Vector();
            aeTreeNode.getItemsFromNode(con, tnd_id_lst, itm_id_vec);
        }

        Vector ats_lst = null;

        if (ats_id_lst != null) {
            ats_lst = new Vector();

            for (int i=0; i<ats_id_lst.length; i++) {
            	if (ats_id_lst[i] != null && ats_id_lst[i].length() > 0) {
                    ats_lst.addElement(ats_id_lst[i]);
            	}
            }
        }

        Vector ats_attend_vec = aeAttendanceStatus.getAttendIdsVec(con, prof.root_ent_id);    
        
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
            page.sortCol = cwPagination.esc4SortSql(DEFAULT_SORT_COL_ORDER[0]);
        }

        col_buf.append(str_buf.toString());
        col_buf.append(TEMP_COL).append(" ").append(page.sortOrder);
        ViewLearnerReport report = new ViewLearnerReport();
        Hashtable data_hash = report.search(con, page, ent_id_lst, itm_id_vec, ats_lst, ats_attend_vec, att_create_start_datetime, att_create_end_datetime, col_buf.toString(), prof.usr_ent_id,prof.root_ent_id, wizbini.cfgTcEnabled, isMyStaff, lrn_scope_sql,is_realTime_view_rpt);
        v_data = (Vector)data_hash.get(ViewLearnerReport.VEC_DATA);

        page.totalRec = v_data.size();
        if (page.pageSize == -1 && page.totalRec != 0) {
            page.pageSize = page.totalRec;
        }
        page.totalPage = page.totalRec/page.pageSize;
        if (page.totalRec%page.pageSize !=0) {
            page.totalPage++;
        }
        page.ts = cwSQL.getTime(con);

        Vector usr_ent_vec = new Vector();
        Vector idVec = new Vector();
        Vector itmVec = new Vector();
        for (int i=(page.curPage-1)*page.pageSize; i<v_data.size() && i<(page.curPage*page.pageSize); i++) {
            ViewLearnerReport.Data d = (ViewLearnerReport.Data)v_data.elementAt(i);
            if (!itmVec.contains(new Long(d.t_id))) {
                itmVec.addElement(new Long(d.t_id));
            }
        }
        Hashtable recordXMLHash = new Hashtable();
        Vector recordVec = new Vector();
        Hashtable appHash = new Hashtable();

        StringBuffer recordXML = null;
        for (int i=(page.curPage-1)*page.pageSize; i<v_data.size() && i<(page.curPage*page.pageSize); i++) {
            ViewLearnerReport.Data d = (ViewLearnerReport.Data)v_data.elementAt(i);
            if (!usr_ent_vec.contains(new Long(d.usr_ent_id))) {
                usr_ent_vec.addElement(new Long(d.usr_ent_id));
            }
            idVec.addElement(new Long(d.itm_id));
            recordXML = new StringBuffer();
            
            recordXML.append("<user id=\"").append(d.usr_ste_usr_id).append("\" last_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.usr_last_name_bil))).append("\" first_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.usr_first_name_bil))).append("\" display_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.usr_display_bil))).append("\" email_1=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.usr_email))).append("\" tel_1=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.usr_tel_1))).append("\" ent_id=\"").append(d.usr_ent_id).append("\"/>").append(cwUtils.NEWL);
            recordXML.append("<item id=\"").append(d.t_id).append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.t_code))).append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.t_title))).append("\" type=\"").append(cwUtils.escNull(d.t_type)).append("\" dummy_type=\"").append(aeItemDummyType.getDummyItemType(d.t_type, d.itm_blend_ind, d.itm_exam_ind, d.itm_ref_ind)).append("\" course_id=\"").append(d.cos_res_id).append("\" eff_start_datetime=\"").append(aeUtils.escNull(d.t_eff_start_datetime)).append("\" eff_ent_datetime=\"").append(aeUtils.escNull(d.t_eff_end_datetime)).append("\" nature=\"").append(aeUtils.escNull(d.t_apply_method)).append("\" unit=\"").append(d.t_unit).append("\" ");
            if(wizbini.cfgTcEnabled) {
            	recordXML.append(" itm_tcr_title=\"").append(cwUtils.esc4XML(d.tcr_title)).append("\" ");
            }
            recordXML.append(">").append(cwUtils.NEWL);
            
            Vector cat_vec = ViewLearnerReport.getCatVec(con, d.t_id);
            if(cat_vec != null && cat_vec.size() > 0) {
            	recordXML.append("<catalog>");
            	for(int j = 0; j < cat_vec.size(); j++) {
            		recordXML.append("<cat>").append(cwUtils.esc4XML((String)cat_vec.get(j))).append("</cat>");
            	}
            	recordXML.append("</catalog>");
            }
            
            recordXML.append("</item>").append(cwUtils.NEWL);

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
                float percent = (new Float(d.cov_score)).floatValue()/(new Float(d.cov_max_score)).floatValue();
                float temp = (new Float(percent * 10000)).longValue();
                float temp2 = temp/(new Float(100)).floatValue();
                score = (new Float(temp2)).toString();
            }

            recordXML.append("<aicc_data course_id=\"").append(d.cos_res_id).append("\" student_id=\"").append(d.usr_ent_id).append("\" last_acc_datetime=\"").append(aeUtils.escNull(d.cov_last_acc_datetime)).append("\" commence_datetime=\"").append(aeUtils.escNull(d.cov_commence_datetime)).append("\" used_time=\"").append(total_time).append("\" attempt=\"").append(dbModuleEvaluation.getTotalAttemptByTkhId(con, d.app_tkh_id)).append("\" status=\"").append(aeUtils.escNull(d.cov_status)).append("\" score=\"").append(aeUtils.escNull(score)).append("\" comment=\"").append(aeUtils.escNull(d.cov_comment)).append("\" completion_datetime=\"").append(aeUtils.escNull(d.cov_complete_datetime)).append("\"/>").append(cwUtils.NEWL);
            recordXML.append("<attendance datetime=\"").append(aeUtils.escNull(d.att_timestamp));
            recordXML.append("\" create_date=\"").append(aeUtils.escNull(d.att_create_timestamp));
            recordXML.append("\" status=\"");

            if (d.att_ats_id != 0) {
                recordXML.append(d.att_ats_id);
            }

            recordXML.append("\" remark=\"").append(cwUtils.esc4XML(cwUtils.escNull(d.att_remark)));
            recordXML.append("\" rate=\"").append(cwUtils.esc4XML(cwUtils.escNull(d.att_rate)));
            recordXML.append("\"/>").append(cwUtils.NEWL);
            recordVec.addElement(new Long(d.app_id));
            recordXMLHash.put(new Long(d.app_id), recordXML);
            appHash.put(new Long(d.app_id), new Long(d.usr_ent_id));
        }

        result.append("<report_list>").append(cwUtils.NEWL);

        if (usr_ent_vec.size() != 0) {
            // Use temp table instead of using IN (1,2,3,.....,XXXX)
            String colName = "tmp_usr_ent_id";
            String tableName = cwSQL.createSimpleTemptable(con, colName, cwSQL.COL_TYPE_LONG, 0);
            cwSQL.insertSimpleTempTable(con, tableName, usr_ent_vec, cwSQL.COL_TYPE_LONG);
            Hashtable usr_group_hash = dbEntityRelation.getGroupEntIdRelation(con, tableName, colName);
            Hashtable usr_grade_hash = dbEntityRelation.getGradeRelation(con, tableName, colName);
            cwSQL.dropTempTable(con, tableName);
            EntityFullPath full_path = EntityFullPath.getInstance(con);

            for (int index=0; index<recordVec.size(); index++) {
                Long appID = (Long)recordVec.elementAt(index);
                Long usr_ent_id = (Long) appHash.get(appID);

                result.append("<record>").append(cwUtils.NEWL);
                result.append(recordXMLHash.get(appID));
                result.append("<group_list>");
                long usg_ent_id = (Long)usr_group_hash.get(usr_ent_id);
                result.append("<group>").append(cwUtils.esc4XML(full_path.getFullPath(con, usg_ent_id))).append("</group>");
                result.append("</group_list>");
                
                result.append("<grade_list>");
                result.append("<group>").append(cwUtils.esc4XML((String)usr_grade_hash.get(usr_ent_id))).append("</group>");
                result.append("</grade_list>");
                result.append("</record>").append(cwUtils.NEWL);
            }
        }
        result.append("</report_list>").append(cwUtils.NEWL);
        result.append(page.asXML()).append(cwUtils.NEWL);
        return result.toString();
    }
    public String[] getLearningHistory(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, long usr_ent_id, String calendar_year, String[] ent_id_lst, String[] tnd_id_lst, String itm_title, int itm_title_partial_ind, String[] ats_id_lst, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, Timestamp att_start_datetime, Timestamp att_end_datetime, boolean split_ind, boolean override_appr_usg, long rte_id, String[] spec_name, String[] spec_value, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException {
//    	long start =0, end =0;
//    	start = System.currentTimeMillis();
    	        Hashtable spec_pairs = new Hashtable();
    	        if(spec_name != null && spec_value != null) {
    	            ViewReportSpec spec = new ViewReportSpec();
    	            ViewReportSpec.Data data = spec.getNewData();
    	            DbReportSpec rpt_spec = Report.toSpec(rte_id, null, spec_name, spec_value);

    	            data.rte_id = rte_id;
    	            data.rsp_xml = rpt_spec.rsp_xml;
    	            spec_pairs = getSpecPairs(data.rsp_xml);
    	        }
//    	end = System.currentTimeMillis();
//    	System.out.println("getLearningHistory 01 = " + (end -start));
//    	start = System.currentTimeMillis();
    	        StringBuffer xmlHeader = new StringBuffer();
    	    	xmlHeader.append("<report_body>").append(cwUtils.NEWL);
    	        if (usr_ent_id > 0) {
    	            ent_id_lst = new String[1];
    	            ent_id_lst[0] = (new Long(usr_ent_id)).toString();
    	            xmlHeader.append(getUserInfo(con, usr_ent_id));

    	        }
//    	end = System.currentTimeMillis();
//    	System.out.println("getLearningHistory 02 = " + (end -start));
//    	start = System.currentTimeMillis();
    	        String[] reportXML=null;
    	    	xmlHeader.append("<meta>").append(cwUtils.NEWL);
    	    	xmlHeader.append(LearnerReport.getMetaData(con, prof,  wizbini));
    	    	xmlHeader.append("</meta>").append(cwUtils.NEWL);
    	    	xmlHeader.append("");
    	        if(spec_name != null && spec_value != null) {
    	            xmlHeader.append(getLrnSolnHistory(con, sess, page, prof, env, ent_id_lst, null, null, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, att_create_start_datetime, att_create_end_datetime, att_start_datetime, att_end_datetime, null, null, null, (Vector)spec_pairs.get("itm_content_lst"), null, false, override_appr_usg, null));
    	        } else {
    	            xmlHeader.append(getLrnSolnHistory(con, sess, page, prof, env, ent_id_lst, null, null, tnd_id_lst, itm_title, itm_title_partial_ind, ats_id_lst, itm_start_datetime, itm_end_datetime, att_create_start_datetime, att_create_end_datetime, att_start_datetime, att_end_datetime, null, null, null, null, null, false, override_appr_usg, null));
    	        }
    	        String[] spec_name1 = {"ent_id", "att_start_datetime", "att_end_datetime", "att_create_start_datetime", "att_create_end_datetime"};
    	        String[] spec_value1 = {toList(ent_id_lst, "~"),"", "", "", ""};
    	        if(att_start_datetime != null){
    				spec_value1[1] = att_start_datetime.toString();
    	        }
    	       	if(att_end_datetime != null){
    	            spec_value1[2] = att_end_datetime.toString();
    	        }
    	        if(att_create_start_datetime != null){
    				spec_value1[3] = att_create_start_datetime.toString();
    	        }
    	       	if(att_create_end_datetime != null){
    	            spec_value1[4] = att_create_end_datetime.toString();
    	        }
    	        DbReportSpec spec = Report.toSpec(0, null, spec_name1, spec_value1);
    	        xmlHeader.append("<spec>");
    	        xmlHeader.append(spec.rsp_xml);
    	        xmlHeader.append("</spec>");
    	        xmlHeader.append("</report_body>").append(cwUtils.NEWL);
    	        reportXML = new String[1];
    	        reportXML[0] = xmlHeader.toString();
//    	end = System.currentTimeMillis();
//    	System.out.println("getLearningHistory 03 = " + (end -start));
    	        return reportXML;
    }
    public String getLrnSolnHistory(Connection con, HttpSession sess, cwPagination page, loginProfile prof, qdbEnv env, String[] ent_id_lst, String[] ugr_ent_id_lst, long[] itm_id_lst, String[] tnd_id_lst, String itm_title, int itm_title_partial_ind, String[] ats_id_lst, Timestamp itm_start_datetime, Timestamp itm_end_datetime, Timestamp att_create_start_datetime, Timestamp att_create_end_datetime, Timestamp att_start_datetime, Timestamp att_end_datetime, String[] app_status_lst, Vector content_vec, Vector usr_content_vec, Vector itm_content_vec, Vector run_content_vec, boolean show_itm_credit, boolean override_appr_usg, WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException {

	        StringBuffer result = new StringBuffer();
	        if(!override_appr_usg && (ent_id_lst == null || ent_id_lst.length == 0)
	            && dbUtils.isUserApprRole(con, prof.usr_ent_id, prof.current_role)) {
	            ent_id_lst = getApprGroups(con, prof);
	        }
	    	long start=0, end =0;
	    	cwPagination sess_page  =null;
	    	if(att_create_start_datetime ==null && att_create_end_datetime==null){
	            sess_page = (cwPagination)sess.getAttribute(SESS_LEARNER_REPORT_PAGINATION);    		
	    	}

	        Vector v_data = null;
	        Hashtable modCount = null;
	        if (page.pageSize == 0) {
	            page.pageSize = DEFAULT_PAGE_SIZE;
	        }

	        if (page.curPage == 0) {
	            page.curPage = 1;
	        }

	        if (sess_page != null && sess_page.ts.equals(page.ts)) {
	            v_data = (Vector)sess.getAttribute(SESS_LEARNER_REPORT_DATA);
	            modCount = (Hashtable)sess.getAttribute(SESS_LEARNER_REPORT_MODULE_COUNT);            
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
	            }
	        } else {
		        StringBuffer str_buf = new StringBuffer();
		
		        str_buf = new StringBuffer();
		        Vector itm_id_vec = null;
		
		        if (itm_id_lst!=null){
		            itm_id_vec = new Vector();
		            aeItem itm = null;
		            Vector childItm;
		            for (int i=0; i<itm_id_lst.length; i++){
		                itm = new aeItem();
		                itm.itm_id = itm_id_lst[i];
		                if (itm.getCreateRunInd(con)){
		                    childItm = aeItemRelation.getChildItemId(con, itm_id_lst[i]); 
		                    for (int k=0; k<childItm.size(); k++){
		                        itm_id_vec.addElement((Long)childItm.elementAt(k));
		                    }
		                } 
		                itm_id_vec.addElement(new Long(itm_id_lst[i]));
		            }
		        }else if (tnd_id_lst != null) {
		            itm_id_vec = new Vector();
	                aeTreeNode.getItemsFromNode(con, tnd_id_lst, itm_id_vec);
		        }
		
		        Vector ats_lst = null;
		        if (ats_id_lst != null) {
		            ats_lst = new Vector();
		
		            for (int i=0; i<ats_id_lst.length; i++) {
		                ats_lst.addElement(ats_id_lst[i]);
		            }
		        }
		        Vector ats_attend_vec = aeAttendanceStatus.getAttendIdsVec(con, prof.root_ent_id);            
		        if (page.sortOrder == null) {
		            page.sortOrder = DEFAULT_SORT_ORDER;
		        }
		
		        str_buf = new StringBuffer();
		        String str = null;
		        for (int i=0; i<DEFAULT_SORT_COL_ORDER.length; i++) {
		            if (DEFAULT_SORT_COL_ORDER[i].equals(page.sortCol)) {
		                str = page.sortCol + " " + page.sortOrder;
		            } else {
		                if(!DEFAULT_SORT_COL_ORDER[i].startsWith("usr_extra_") && !DEFAULT_SORT_COL_ORDER[i].startsWith("urx_extra_")) {
		                    str_buf.append(DEFAULT_SORT_COL_ORDER[i]).append(" ").append(page.sortOrder).append(", ");
		                }
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
		        ViewLearnerReport report = new ViewLearnerReport();
		        Hashtable data_hash = report.searchLrnSolnHistory(con, page, ent_id_lst, ugr_ent_id_lst, itm_id_vec, itm_title, itm_title_partial_ind, ats_lst, ats_attend_vec, itm_start_datetime, itm_end_datetime, att_create_start_datetime, att_create_end_datetime, att_start_datetime, att_end_datetime, col_buf.toString(), app_status_lst, prof.root_ent_id);
		    	v_data = (Vector)data_hash.get(ViewLearnerReport.VEC_DATA);
		    	modCount = (Hashtable)data_hash.get(ViewLearnerReport.MODULE_COUNT);
		        page.totalRec = v_data.size();
		        if (page.pageSize == -1 && page.totalRec != 0) {
		            page.pageSize = page.totalRec;
		        }
		        page.totalPage = page.totalRec/page.pageSize;
		        if (page.totalRec%page.pageSize !=0) {
		            page.totalPage++;
		        }
		        page.ts = cwSQL.getTime(con);
		        if (v_data != null && page != null) {
		            sess.setAttribute(SESS_LEARNER_REPORT_DATA, v_data);
		            sess.setAttribute(SESS_LEARNER_REPORT_PAGINATION, page);
		            sess.setAttribute(SESS_LEARNER_REPORT_MODULE_COUNT, modCount);
		        }
		    }
        Vector usr_ent_vec = new Vector();
        Vector idVec = new Vector();

        Vector itmVec = new Vector();
        for (int i=(page.curPage-1)*page.pageSize; i<v_data.size() && i<(page.curPage*page.pageSize); i++) {
            ViewLearnerReport.Data d = (ViewLearnerReport.Data)v_data.elementAt(i);
            if (!itmVec.contains(new Long(d.t_id))) {
                itmVec.addElement(new Long(d.t_id));
            }
        }
        Hashtable itemCatalogXMLHash = aeItem.getCatalogXMLHash(con, itmVec);
        Hashtable recordXMLHash = new Hashtable();
        Vector recordVec = new Vector();
        Hashtable appHash = new Hashtable();
        
        StringBuffer recordXML = null;
        Hashtable curUsrProfXML = null;
        for (int i=(page.curPage-1)*page.pageSize; i<v_data.size() && i<(page.curPage*page.pageSize); i++) {
            ViewLearnerReport.Data d = (ViewLearnerReport.Data)v_data.elementAt(i);
            if (!usr_ent_vec.contains(new Long(d.usr_ent_id))) {
                usr_ent_vec.addElement(new Long(d.usr_ent_id));
            }
            idVec.addElement(new Long(d.itm_id));
            recordXML = new StringBuffer();
            
            //get application xml columns 
            //as they cannot be got from ViewLearnerReport.search
            //it is because xml column cannot be Union
            aeApplication app = null;
            if((usr_content_vec!=null && usr_content_vec.size()>0) || 
               (content_vec!=null && content_vec.indexOf("app_ext4")>-1)) {
                app = new aeApplication();
                app.app_id = d.app_id;
                app.get(con);
            } 
            //get historical user profile XML
            if(usr_content_vec!=null && usr_content_vec.size()>0) {
                if(app!=null && app.app_usr_prof_xml!=null) {
                    recordXML.append(app.app_usr_prof_xml);
                } else {
                    //if cannot find historial user profile XML
                    //get the current user profile XML
                    if(curUsrProfXML == null) {
                        curUsrProfXML = new Hashtable();
                    }
                    Long key = new Long(d.usr_ent_id);
                    String profXML = (String)curUsrProfXML.get(key);
                    if(profXML == null) {
                        dbRegUser applicant = new dbRegUser();
                        applicant.usr_ent_id = d.usr_ent_id;
                        applicant.get(con);
                        profXML = applicant.getUserXML(con, prof, wizbini);
                        curUsrProfXML.put(key, profXML);
                    }
                    recordXML.append(profXML);
                }
            } 
            recordXML.append("<student id=\"").append(d.usr_ste_usr_id).append("\" last_name=\"")
            		 .append(cwUtils.esc4XML(aeUtils.escNull(d.usr_last_name_bil)))
            		 .append("\" first_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.usr_first_name_bil)))
            		 .append("\" display_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.usr_display_bil)))
            		 .append("\" ent_id=\"").append(d.usr_ent_id).append("\"/>").append(cwUtils.NEWL);
            recordXML.append("<item id=\"").append(d.t_id)
            		 .append("\" code=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.t_code)))
            		 .append("\" title=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.t_title)))
            		 .append("\" type=\"").append(cwUtils.escNull(d.t_type))
            		 .append("\" dummy_type=\"").append(cwUtils.escNull(aeItemDummyType.getDummyItemType(d.t_type, d.itm_blend_ind, d.itm_exam_ind, d.itm_ref_ind)))
            		 .append("\" course_mod_cnt=\"").append((Long)modCount.get(new Long(d.cos_res_id)))
            		 .append("\" course_id=\"").append(d.cos_res_id)
            		 .append("\" eff_start_datetime=\"").append(aeUtils.escNull(d.t_eff_start_datetime))
            		 .append("\" eff_ent_datetime=\"").append(aeUtils.escNull(d.t_eff_end_datetime))
            		 .append("\" nature=\"").append(aeUtils.escNull(d.t_apply_method))
            		 .append("\" unit=\"").append(d.t_unit).append("\">").append(cwUtils.NEWL);
            aeItem item = new aeItem();
            item.itm_id = d.t_id;   
            recordXML.append(itemCatalogXMLHash.get(new Long(d.t_id))).append(cwUtils.NEWL);
            recordXML.append("</item>").append(cwUtils.NEWL);
            //get application details
            recordXML.append("<application id=\"").append(d.app_id)
            		 .append("\" app_tkh_id=\"").append(d.app_tkh_id)
            		 .append("\" datetime=\"").append(aeUtils.escNull(d.app_create_timestamp))
            		 .append("\" process_status=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.app_process_status)))
            		 .append("\" queue_status=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.app_status)))
            		 .append("\" app_itm_id=\"").append(d.itm_id)
            		 .append("\" itm_eff_start_datetime=\"").append(aeUtils.escNull(d.itm_eff_start_datetime))
            		 .append("\" itm_eff_end_datetime=\"").append(aeUtils.escNull(d.itm_eff_end_datetime))
            		 .append("\" itm_title=\"").append(cwUtils.esc4XML(aeUtils.escNull(d.itm_title)))
            		 .append("\">").append(cwUtils.NEWL);
            if(content_vec!=null && content_vec.indexOf("app_ext4")>-1) {
                recordXML.append("<app_ext4>").append(cwUtils.escNull(app.app_ext4)).append("</app_ext4>");
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
                float percent = (new Float(d.cov_score)).floatValue()/(new Float(d.cov_max_score)).floatValue();
                float temp = (new Float(percent * 10000)).longValue();
                float temp2 = temp/(new Float(100)).floatValue();
                score = (new Float(temp2)).toString();
            }
            recordXML.append("<aicc_data course_id=\"").append(d.cos_res_id).append("\" student_id=\"").append(d.usr_ent_id).append("\" last_acc_datetime=\"").append(aeUtils.escNull(d.cov_last_acc_datetime)).append("\" commence_datetime=\"").append(aeUtils.escNull(d.cov_commence_datetime)).append("\" used_time=\"").append(total_time).append("\" attempt=\"").append("").append("\" status=\"").append(aeUtils.escNull(d.cov_status)).append("\" score=\"").append(aeUtils.escNull(score)).append("\" comment=\"").append(aeUtils.escNull(d.cov_comment)).append("\" completion_datetime=\"").append(aeUtils.escNull(d.cov_complete_datetime)).append("\"/>").append(cwUtils.NEWL);
            recordXML.append("<attendance datetime=\"").append(aeUtils.escNull(d.att_timestamp));
            recordXML.append("\" create_date=\"").append(aeUtils.escNull(d.att_create_timestamp));
            recordXML.append("\" status=\"");
            if (d.att_ats_id != 0) {
                recordXML.append(d.att_ats_id);
            }
	        recordXML.append("\" remark=\"").append(cwUtils.esc4XML(cwUtils.escNull(d.att_remark)));
	        recordXML.append("\" rate=\"").append(cwUtils.esc4XML(cwUtils.escNull(d.att_rate)));
	        recordXML.append("\"/>").append(cwUtils.NEWL);
	        recordXML.append("<training_center id=\"").append(cwUtils.escZero(d.tcr_id)).append("\">")
	        		 .append(cwUtils.esc4XML(cwUtils.escNull(d.tcr_title))).append("</training_center>");
	        recordVec.addElement(new Long(d.app_id));
	        recordXMLHash.put(new Long(d.app_id), recordXML);
	        appHash.put(new Long(d.app_id), new Long(d.usr_ent_id));
        }
        
        result.append("<report_list>").append(cwUtils.NEWL);
        if (usr_ent_vec.size() != 0) {
            for (int index=0; index<recordVec.size(); index++) {
                Long appID = (Long)recordVec.elementAt(index);
                result.append("<record>").append(cwUtils.NEWL);
                result.append(recordXMLHash.get(appID));
                result.append("</record>").append(cwUtils.NEWL);
            }
        }
        result.append("</report_list>").append(cwUtils.NEWL);

        result.append("<item_credits>").append(cwUtils.NEWL);
        StringBuffer xml = new StringBuffer();
        result.append(xml).append(cwUtils.NEWL);
        result.append("</item_credits>").append(cwUtils.NEWL);

        result.append(page.asXML()).append(cwUtils.NEWL);
        return result.toString();
    	    	    
    }
    public static String getUserInfo(Connection con, long usr_ent_id) throws SQLException, qdbException {
        StringBuffer result = new StringBuffer();
        dbRegUser user = new dbRegUser();

        user.usr_ent_id = usr_ent_id;
        user.get(con);
        result.append("<student id=\"").append(user.usr_ste_usr_id).append("\" last_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(user.usr_last_name_bil))).append("\" first_name=\"").append(cwUtils.esc4XML(aeUtils.escNull(user.usr_first_name_bil))).append("\" display_bil=\"").append(cwUtils.esc4XML(aeUtils.escNull(user.usr_display_bil))).append("\" ent_id=\"").append(usr_ent_id).append("\"/>").append(cwUtils.NEWL);

        return result.toString();
    }

    public static String getMetaData(Connection con, loginProfile prof, WizbiniLoader wizbini) throws SQLException, cwSysMessage {
        StringBuffer result = new StringBuffer();

        result.append("<cur_time>").append(cwSQL.getTime(con)).append("</cur_time>").append(cwUtils.NEWL);
        result.append(getAttendanceStatusList(con, prof.root_ent_id));

        // added for certificate
        result.append(getCertificateStatusList(con));
        
        result.append(getCatalogTitleList(con, prof,  wizbini));

        // added for item types
        result.append(aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id));
        
        // added for role labels
        result.append(dbUtils.getAllRoleAsXML(con, "role_list", prof.root_ent_id));

        return result.toString();
    }

    /** 
    * Construct the catalog list as xml    
    */
    public static String getCatalogTitleList(Connection con, loginProfile prof, WizbiniLoader wizbini)
        throws SQLException, cwSysMessage{
            boolean checkStatus =false;
            aeCatalog cat = new aeCatalog();
            String xml = null;
            try{
                xml = cat.getTitleListAsXML(con, prof, checkStatus, null,  wizbini);
            }catch(qdbException e){
                throw new SQLException(e.getMessage());
            }
            return xml;
        }

    // for certificate
    public static String checkCertificateStatus(Connection con, aeItem item, long usr_ent_id) {
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
            CommonLog.error(e.getMessage(),e);
        }
        return result.toString();
    }
}