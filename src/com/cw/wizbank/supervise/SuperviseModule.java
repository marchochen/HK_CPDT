package com.cw.wizbank.supervise;

import java.io.*;
import java.sql.*;

import javax.servlet.http.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.ServletModule;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.ae.aeLearningPlan;
import com.cw.wizbank.ae.aeLearningSoln;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.report.LearnerReport;
import com.cwn.wizbank.utils.CommonLog;

public class SuperviseModule extends ServletModule {
    
    private final static String GET_STAFF_COUNT     = "get_staff_count";
    private final static String SEARCH_ENT_LST      = "search_ent_lst";
    private final static String GET_USR             = "get_usr";
    private final static String GET_META            = "get_meta";
    private final static String AE_LRN_PLAN         = "ae_lrn_plan";
    private final static String AE_LRN_SOLN         = "ae_lrn_soln";
    private final static String GET_RPT             = "get_rpt";
    private final static String XML                 = "_xml";

    private final static String MSG_SPT001          = "SPT001";
        
    private final static String moduleName = "supervise_module";
    
    public SuperviseModule() { ; 		//$$ superviseModule1.move(0,0);
}
    
    public void process()
        throws SQLException, IOException, cwException {

        if (prof == null) {
            response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
        }
        
        PrintWriter out = response.getWriter();
        SuperviseReqParam urlp = new SuperviseReqParam(request, clientEnc, static_env.ENCODING);
        urlp.myCommon();
        HttpSession sess = request.getSession(true);
        try {
            if(urlp.cmd == null) {
                 
                throw new cwException("Invalid Command");
            } else if(urlp.cmd.equalsIgnoreCase(GET_STAFF_COUNT)
                    || urlp.cmd.equalsIgnoreCase(GET_STAFF_COUNT + XML)) {
                Supervisor sup = new Supervisor(con, prof.usr_ent_id);
                String xml = sup.getStaffCountAsXML(con);
                String result = formatXML(xml,moduleName);
                if(urlp.cmd.equalsIgnoreCase(GET_STAFF_COUNT + XML)) {
                    out.println(result.toString());
                } else {
                    generalAsHtml(result.toString(), out, urlp.stylesheet);
                }                                                                                                
            } else if(urlp.cmd.equalsIgnoreCase(SEARCH_ENT_LST)
                    || urlp.cmd.equalsIgnoreCase(SEARCH_ENT_LST + XML)) {

                urlp.searchEntLst();
                StringBuffer xmlBuf = new StringBuffer(1024);
                xmlBuf.append("<reference_data>");
                if(urlp.usg.s_usg_ent_id_lst != null && urlp.usg.s_usg_ent_id_lst.length > 0) {
                    xmlBuf.append(urlp.usg.s_usg_ent_id_lst[0]);
                } else {
                    if (urlp.usg.usg_ent_id <= 0) {
                    		urlp.usg.s_usg_ent_id_lst = new String[] {Supervisor.SEARCH_MY_STAFF};
                    }
                }

                xmlBuf.append("</reference_data>");
                xmlBuf.append("<search_in_mystaff>").append(urlp.searchInMyStaff).append("</search_in_mystaff>");
                if(!urlp.searchEmptyInd) {
                    xmlBuf.append(urlp.usg.searchEntListAsXML(con, sess, prof, urlp.page, urlp.page_size));
                }                
                StringBuffer metaXMLBuf = new StringBuffer(1024);
                metaXMLBuf.append(dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id));
                
                String result = formatXML(xmlBuf.toString(), metaXMLBuf.toString(), moduleName);
                if(urlp.cmd.equalsIgnoreCase(SEARCH_ENT_LST + XML)) {
                    out.println(result);
                } else {
                    generalAsHtml(result, out, urlp.stylesheet);
                }                                                                                                
            } else if(urlp.cmd.equalsIgnoreCase(GET_USR)
                    || urlp.cmd.equalsIgnoreCase(GET_USR + XML)) {

                urlp.getUsr();
                Supervisor sup = new Supervisor(con, prof.usr_ent_id);
                dbRegUser staff = new dbRegUser();
                staff.usr_ent_id = urlp.usr_ent_id;
                staff.get(con);
                if(!sup.isMyStaff(con, urlp.usr_ent_id)) {
                    throw new cwSysMessage(MSG_SPT001, staff.usr_display_bil);
                } else {
                    StringBuffer xmlBuf = new StringBuffer(1024);
                    xmlBuf.append(staff.getUserXML(con,prof));
                    StringBuffer metaXMLBuf = new StringBuffer(1024);
                    metaXMLBuf.append(dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id));
                    String result = formatXML(xmlBuf.toString(), metaXMLBuf.toString(), moduleName);
                    if(urlp.cmd.equalsIgnoreCase(GET_USR + XML)) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet);
                    }                                                                                                
                }
            } else if (urlp.cmd.equalsIgnoreCase(AE_LRN_PLAN) ||
                urlp.cmd.equalsIgnoreCase(AE_LRN_PLAN + XML)) {
                
                urlp.aeLrnPlan();
                Supervisor sup = new Supervisor(con, prof.usr_ent_id);
                dbRegUser staff = new dbRegUser();
                staff.usr_ent_id = urlp.usr_ent_id;
                staff.get(con);
                if(!sup.isMyStaff(con, urlp.usr_ent_id)) {
                    throw new cwSysMessage(MSG_SPT001, staff.usr_display_bil);
                } else {
                    aeLearningPlan plan = new aeLearningPlan();
                    String result = plan.myLearningPlan(con, sess, prof, urlp.usr_ent_id, urlp.v_itm_type, urlp.pgm_id, urlp.pgm_run_id, urlp.targeted_itm_apply_method_lst);
                    result = formatXML(result, moduleName);

                    if (urlp.cmd.equals(AE_LRN_PLAN + XML)) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet);
                    }
                }
            } else if (urlp.cmd.equalsIgnoreCase(GET_META) ||
                urlp.cmd.equalsIgnoreCase(GET_META + XML)) {
            	
                StringBuffer metaXMLBuf = new StringBuffer(1024);
                metaXMLBuf.append(dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id));
                
                String result = formatXML("", metaXMLBuf.toString(), moduleName);

                if (urlp.cmd.equals(GET_META + XML)) {
                    out.println(result);
                } else {
                    generalAsHtml(result, out, urlp.stylesheet);
                }
            } else if (urlp.cmd.equalsIgnoreCase(AE_LRN_SOLN) ||
                urlp.cmd.equalsIgnoreCase(AE_LRN_SOLN + XML)) {
                
                urlp.aeLrnSoln();
                Supervisor sup = new Supervisor(con, prof.usr_ent_id);
                dbRegUser staff = new dbRegUser();
                staff.usr_ent_id = urlp.usr_ent_id;
                staff.get(con);
                if(!sup.isMyStaff(con, urlp.usr_ent_id)) {
                    throw new cwSysMessage(MSG_SPT001, staff.usr_display_bil);
                } else {
                    aeLearningSoln soln = new aeLearningSoln();
                    String result = soln.myLearningSoln(con, sess, prof, urlp.usr_ent_id, urlp.v_soln_type, urlp.v_itm_type, urlp.targeted_itm_apply_method_lst, urlp.all_ind, urlp.order_bys, urlp.sort_bys);
                    String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
                    metaXML += aeLearningSoln.getLearningPlanConfigXML(wizbini, prof.root_id);

                    result = formatXML(result, metaXML, moduleName);

                    if (urlp.cmd.equals(AE_LRN_SOLN + XML)) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet);
                    }
                }
            } else if (urlp.cmd.equalsIgnoreCase(GET_RPT) ||
                urlp.cmd.equalsIgnoreCase(GET_RPT + XML)) {
                
                urlp.getRpt();
                urlp.pagination();
                boolean flag = false;
                String[] reportXML = null;
                String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);

                urlp.getLrnRpt();

                Supervisor sup = new Supervisor(con, prof.usr_ent_id);
                dbRegUser staff = new dbRegUser();
                staff.usr_ent_id = urlp.usr_ent_id;
                staff.get(con);
                if(!sup.isMyStaff(con, urlp.usr_ent_id)) {
                    throw new cwSysMessage(MSG_SPT001, staff.usr_display_bil);
                } else {
                
                    LearnerReport report = new LearnerReport();
                    reportXML = report.getLearningReport(con, sess, urlp.cwPage, prof, static_env, urlp.usr_ent_id, urlp.calendar_year, urlp.ent_id_lst, urlp.tnd_id_lst, urlp.ats_id_lst, null, null, flag, false, urlp.rte_id, urlp.spec_name, urlp.spec_value, wizbini,true,null);

                    String tempReport = null;
                    String formattedXML = "";
                    if (urlp.cmd.equalsIgnoreCase(GET_RPT + XML)) {
                        StringBuffer strBuf = new StringBuffer(1024);
                        for(int i=0; i<reportXML.length; i++) {
                            strBuf.append(reportXML[i]);
                        }
                        formattedXML = formatXML(strBuf.toString(), metaXML, moduleName);
                        static_env.outputXML(out, formattedXML);
                    } else {
                        for(int i=0; i<reportXML.length; i++) {
                            formattedXML = formatXML(reportXML[i], metaXML, moduleName);
                            //Use tempReportBuf to store the transformed result
                            //and remove the 1st line, which is report header,
                            //when i!=0
                            tempReport = aeUtils.transformXML(formattedXML, urlp.stylesheet, static_env, prof.xsl_root);
                            if(i!=0) {
                                tempReport = tempReport.substring(tempReport.indexOf('\r')+1);
                            }
                            out.print(tempReport);
                            out.flush();
                        }
                        out.close();
                    }
                }
            }
        } catch (cwSysMessage se) {
            try {
                con.rollback();
                msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
            }
        } catch (qdbErrMessage err) {
            try {
                con.rollback();
                cwSysMessage se = new cwSysMessage(err.getId(), err.getData());
                msgBox(ServletModule.MSG_STATUS, se, urlp.url_failure, out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
            }
        } catch (qdbException qdbe) {
            CommonLog.error(qdbe.getMessage(),qdbe);
            throw new cwException (qdbe.getMessage());
        } 
    }
	//{{DECLARE_CONTROLS
	//}}
}