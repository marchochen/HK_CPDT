package com.cw.wizbank.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.db.DbRoleTargetEntity;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXSL;
import com.cwn.wizbank.utils.CommonLog;

public class ReportModule extends ServletModule {
    public final static String REPORT = "report";

    //System Message
    private final static String SMSG_INS_RPT_SPEC = "GEN004";

    //servlet API
    public final static String ECHO_SPEC_PARAM = "echo_spec_param";
    public final static String GET_RPT_LST = "get_rpt_lst";
    public final static String GET_RPT_TPL = "get_rpt_tpl";
    public final static String GET_RPT = "get_rpt";
    public final static String LRN_SOLN_HIST = "lrn_soln_hist";
    public final static String INS_RPT_SPEC = "ins_rpt_spec";
    public final static String UPD_RPT_SPEC = "upd_rpt_spec";
    public final static String DEL_RPT_SPEC = "del_rpt_spec";
    public final static String GET_MOD_LST = "get_mod_lst";
    public final static String GET_META_DATA_COURSE = "get_meta_data_course";
    public final static String GET_META_DATA_LEARNER = "get_meta_data_learner";
	public final static String SAVE_SESS_SPEC = "save_sess_spec";
    public final static String XML = "_XML";
    
    //session key
    public final static String SESS_SPEC_HASH = "REPORT_SESS_SPEC_HASH";

    public ReportModule() { ; }

    public void process()
        throws SQLException, IOException, cwException {

//        String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);

        if (prof == null) {
            response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            return;
//            response.sendRedirect(url_relogin);
        }

        PrintWriter out = response.getWriter();
        ReportReqParam urlp = new ReportReqParam(request, clientEnc, static_env.ENCODING);
        HttpSession sess = request.getSession(true);

        try {
            if(urlp.cmd == null) {
                throw new cwException("Invalid Command");
            } else if( urlp.cmd.equalsIgnoreCase(ECHO_SPEC_PARAM) ||
                        urlp.cmd.equalsIgnoreCase(ECHO_SPEC_PARAM + XML) ) {
                urlp.report(sess);
                ReportTemplate reportTemplate = new ReportTemplate();
                String xml = (urlp.rsp_id > 0) ?
                                reportTemplate.echoSearchParamAsXML(con, request,
                                                                    prof, urlp.rsp_id,
                                                                    urlp.rpt_type)
                             :  reportTemplate.echoSearchParamAsXML(con, request,
                                                                    prof, urlp.rpt_type,
                                                                    urlp.spec_name,
                                                                    urlp.spec_value);

                String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);


                xml = formatXML(xml, metaXML, REPORT);
                if (urlp.cmd.equalsIgnoreCase(ECHO_SPEC_PARAM + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }
			} else if( urlp.cmd.equalsIgnoreCase(SAVE_SESS_SPEC)) {
				urlp.save_sees_spec();
                
                if(urlp.window_name != null && urlp.str_spec_name != null && urlp.str_spec_value != null) {
                    Hashtable hSpec = (Hashtable) sess.getAttribute(SESS_SPEC_HASH);
                    if(hSpec == null) {
                        hSpec = new Hashtable();
                    }
                    Vector vSpec = (Vector) hSpec.get(urlp.window_name);
                    if(vSpec == null) {
                        vSpec = new Vector();
                    } else {
                        vSpec.clear();
                    }
                    
                    
                    String name_key = urlp.window_name;
                    String[] nameStrings= cwUtils.splitToString(urlp.window_name, "__AA__");
                    if(nameStrings!= null && nameStrings.length > 0){
                    	name_key = nameStrings[0];
                    }
                    
                    vSpec.addElement(urlp.str_spec_name);                
                    vSpec.addElement(urlp.str_spec_value);
                    hSpec.put(name_key, vSpec);
                    sess.setAttribute(SESS_SPEC_HASH, hSpec);
                }
                response.sendRedirect(urlp.url_success);
                
            } else if( urlp.cmd.equalsIgnoreCase(GET_RPT_TPL) ||
                        urlp.cmd.equalsIgnoreCase(GET_RPT_TPL + XML) ) {

                String xml = null;
                urlp.report(sess);

                try {
                    if (urlp.rpt_type.equalsIgnoreCase(Report.LEARNER)) {
                        urlp.lrn_report();
                        LearnerReport report = new LearnerReport();
                        xml = report.getReportTemplate(con, request, prof, urlp.usr_ent_id, urlp.rsp_id, urlp.rte_id);
                    }
                    else if(urlp.rpt_type.equalsIgnoreCase(Report.LEARNING_ACTIVITY_LRN)){
                        urlp.lrn_report();
                        LearnerReport report = new LearnerLrnReport();
                        xml=report.getReportTemplate(con, request, prof, urlp.usr_ent_id, urlp.rsp_id, urlp.rte_id);
                    }
                    else if (urlp.rpt_type.equalsIgnoreCase(Report.LEARNING_ACTIVITY_COS) || urlp.rpt_type.equalsIgnoreCase(Report.LEARNING_ACTIVITY_BY_COS)) {
                        urlp.lrn_report();
                        LearnerReport report = new LearnerCosReport();
                        xml = report.getReportTemplate(con, request, prof, urlp.usr_ent_id, urlp.rsp_id, urlp.rte_id);
					}else if (urlp.rpt_type.equalsIgnoreCase(Report.COURSE)) {
                        CourseReport report = new CourseReport();
                        xml = report.getReportTemplate(con, request, prof, urlp.rsp_id, urlp.rte_id);
                    } else if(urlp.rpt_type.equalsIgnoreCase(Report.TARGET_LEARNER)) {
                    	CommonLog.debug("Target Learner .....");
                        urlp.lrn_report();
                        TargetLearnerReport report = new TargetLearnerReport();
                        xml = report.getReportTemplate(con, request, prof, urlp.rsp_id, urlp.rte_id);
                    } else if (urlp.rpt_type.equalsIgnoreCase(Report.MODULE)) {
                        urlp.lrn_report();
                        LearningModuleReport modulereport = new LearningModuleReport();
                        xml=modulereport.getReportTemplate(con, request, prof,urlp.rsp_id, urlp.rte_id);
                        
                    } else if (urlp.rpt_type.toUpperCase().startsWith(Report.MODULE_PREFIX)) {
                        ModuleReport report = new ModuleReport();
                        xml = report.getReportTemplate(con, request, prof, urlp.rsp_id, urlp.rte_id);
                    //DENNIS: new code segment for get report templates in a generic manner
                    } else if (urlp.rpt_type.equalsIgnoreCase(Report.EXAM_PAPER_STAT)) {
                        urlp.lrn_report();
                        ExamPaperStatReport report = new ExamPaperStatReport();
                        xml = report.getReportTemplate(con, request, prof, urlp.usr_ent_id, urlp.rsp_id, urlp.rte_id);
                    } else if(urlp.rpt_type.equalsIgnoreCase(Report.TRAIN_FEE_STAT)) {
                    	urlp.lrn_report();
                    	TrainFeeStatReport report = new TrainFeeStatReport();
                    	xml = report.getReportTemplate(con, request, prof, urlp.rsp_id, urlp.rte_id);
                    } else if(urlp.rpt_type.equalsIgnoreCase(Report.TRAIN_COST_STAT)) {
                    	urlp.lrn_report();
                    	TrainCostStatReport report = new TrainCostStatReport();
                    	xml = report.getReportTemplate(con, request, prof, urlp.rsp_id, urlp.rte_id);
                    } else if (urlp.rpt_type.length() > 0){
                        ReportTemplate report = new ReportTemplate();
                        xml = report.getReport(con, request, prof, urlp.rsp_id, urlp.rte_id, null, null, null, urlp.rpt_type, null);
                    } else {
                        throw new cwException("No such report type: " + urlp.rpt_type);
                    }
                    String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);

                    xml = formatXML(xml, metaXML, REPORT);
                } catch(qdbException e) {
                    throw new cwException(e.getMessage());
                } catch(Exception e) {
                	CommonLog.error(e.getMessage(),e);
                    throw new cwException(e.getMessage());
                }

                if (urlp.cmd.equalsIgnoreCase(GET_RPT_TPL + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }
            } else if( urlp.cmd.equalsIgnoreCase(GET_RPT) ||
                        urlp.cmd.equalsIgnoreCase(GET_RPT + XML) ) {
                if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                    con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                }

                String xml = null;
                String[] reportXML = null;
                String reportFilePath = null;
                Vector vtXML = null;
                long[] cos_id_lst = null;
                urlp.report(sess);
                urlp.pagination();
                String rpt_filename = "report";

                if (urlp.download > 0) {
                    if (urlp.rpt_name != null && urlp.rpt_name.length() > 0) {
                        rpt_filename = urlp.rpt_name;
                    }
                }

                // download type = 1 csv, type = 2 zip, type = 3 xls in html format
                boolean flag = false;
                if (urlp.download == 1) {
                	response.setHeader("Cache-Control", ""); 
                	response.setHeader("Pragma", ""); 
                    response.setHeader("Content-Disposition", "attachment; filename=" + rpt_filename + ".csv;");
                    cwUtils.setContentType("application/vnd.ms-excel", response, wizbini);
                    flag = true;
                }else if (urlp.download == 2){
                    response.setHeader("Cache-Control", "public");
                    response.setHeader("Content-Disposition", "attachment; filename=" + rpt_filename + ".zip;");
                    cwUtils.setContentType("application/zip", response, wizbini);
                }else if (urlp.download == 3) {
                	response.setHeader("Cache-Control", ""); 
                	response.setHeader("Pragma", ""); 
                    response.setHeader("Content-Disposition", "attachment; filename=" + rpt_filename + ".xls;");
                    cwUtils.setContentType("text/html", response, wizbini);

                    flag = true;
                }
                try {
                    String metaXML = "";

                    if(prof != null && urlp.download != 4) {
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    }
                    metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);

                    if(urlp.rpt_type.equals(Report.TARGET_LEARNER)) {

                        urlp.lrn_report();
                        TargetLearnerReport report = new TargetLearnerReport();
                        if (urlp.rsp_id > 0) {
                            reportXML = report.getTargetLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, false);
                        } else {
                            reportXML = report.getTargetLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.rsp_title, false);
                        }
                    } else if (urlp.rpt_type.equalsIgnoreCase(Report.LEARNER)
                        || urlp.rpt_type.indexOf(Report.LEARNER) >= 0) {
                        urlp.lrn_report();
                        LearnerReport report = new LearnerReport();
                        report.is_realTime_view_rpt = wizbini.cfgSysSetupadv.isIsRealTimeViewRpt();
                        // for report export
                        ExportController controller = null;
                        LearnerRptExporter exporter = null;
                        boolean isInProgress = false;
                        String window_name = null;
                        if (urlp.download == 4) {
                            controller = new ExportController();
                            exporter = new LearnerRptExporter(con, controller);
                            exporter.is_realTime_view_rpt = wizbini.cfgSysSetupadv.isIsRealTimeViewRpt();
                            if (sess.getAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER) == null) {
                                sess.setAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
                            }else {
                                isInProgress = true;
                            }
                        }
                        if (urlp.rsp_id > 0) {
                            if (urlp.download == 4 ) {
                                if (!isInProgress) {
                                    exporter.getReportXlsByRsp(urlp.rsp_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                                reportXML = report.getLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, flag/*(urlp.download==1)*/, false, wizbini);
                            }
                        } else {
                            if (urlp.calendar_view == 1) {
                                reportXML = report.getLearningReport(con, sess, urlp.cwPage, prof, static_env, urlp.usr_ent_id, urlp.calendar_year, urlp.ent_id_lst, urlp.tnd_id_lst, urlp.ats_id_lst, urlp.att_create_start_datetime, urlp.att_create_end_datetime, flag/*(urlp.download==1)*/, false, urlp.rte_id, urlp.spec_name, urlp.spec_value, wizbini, urlp.isMyStaff,null);
                            } else {
                                if (urlp.download == 4) {
                                    if (!isInProgress) {
                                        exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                                    }
                                }else {
                                    reportXML = report.getLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag/*(urlp.download==1)*/, false, wizbini);
                                }
                            }
                        }
                    }
                    else if(urlp.rpt_type.equalsIgnoreCase(Report.LEARNING_ACTIVITY_LRN)){
                        urlp.lrn_report();
                        LearnerReport report = new LearnerLrnReport();
                        report.is_realTime_view_rpt = wizbini.cfgSysSetupadv.isIsRealTimeViewRpt();
                        // for report export
                        ExportController controller = null;
                        ReportLrnExporter exporter = null;
                        boolean isInProgress = false;
                        String window_name = null;
                        if (urlp.download == 4) {
                            controller = new ExportController();
                            exporter = new ReportLrnExporter(con, controller);
                            exporter.export_stat_only = urlp.export_stat_only;
                            exporter.is_realTime_view_rpt = wizbini.cfgSysSetupadv.isIsRealTimeViewRpt();
                            if (sess.getAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER) == null) {
                                sess.setAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
                            }else {
                                isInProgress = true;
                            }
                        }
                        if (urlp.rsp_id > 0) {
                            if (urlp.download == 4 ) {
                                if (!isInProgress) {
                                    exporter.getReportXlsByRsp(urlp.rsp_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                            report.v_ent_id = urlp.usr_ent_id;
                            reportXML = report.getLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, flag/*(urlp.download==1)*/, false, wizbini);
                            }
                        } else {
                            if (urlp.calendar_view == 1) {
                                reportXML = report.getLearningReport(con, sess, urlp.cwPage, prof, static_env, urlp.usr_ent_id, urlp.calendar_year, urlp.ent_id_lst, urlp.tnd_id_lst, urlp.ats_id_lst, urlp.att_create_start_datetime, urlp.att_create_end_datetime, flag/*(urlp.download==1)*/, false, urlp.rte_id, urlp.spec_name, urlp.spec_value, wizbini,urlp.isMyStaff,null);
                            } else {
                                if (urlp.download == 4) {
                                    if (!isInProgress) {
                                        exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                                    }
                                }else {
                                report.v_ent_id = urlp.usr_ent_id;
                                reportXML = report.getLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag/*(urlp.download==1)*/, false, wizbini);
                                }
                            }
                        }
                    }
                    else if (urlp.rpt_type.equalsIgnoreCase(Report.LEARNING_ACTIVITY_BY_COS)) {
                        urlp.lrn_report();
                        LearnerReport report = new LearnerByCosReport();
                        report.is_realTime_view_rpt = wizbini.cfgSysSetupadv.isIsRealTimeViewRpt();
                        // for report export
                        ExportController controller = null;
                        ReportByCosExporter exporter = null;
                        boolean isInProgress = false;
                        String window_name = null;
                        if (urlp.download == 4) {
                            controller = new ExportController();
                            exporter = new ReportByCosExporter(con, controller);
                            exporter.export_stat_only = urlp.export_stat_only;
                            exporter.is_realTime_view_rpt = wizbini.cfgSysSetupadv.isIsRealTimeViewRpt();
                            if (sess.getAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER) == null) {
                                sess.setAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
                            }else {
                                isInProgress = true;
                            }
                        }
                        if (urlp.rsp_id > 0) {
                            if (urlp.download == 4 ) {
                                if (!isInProgress) {
                                    exporter.getReportXlsByRsp(urlp.rsp_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                            report.v_itm_id = urlp.itm_id; 
                            reportXML = report.getLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, flag/*(urlp.download==1)*/, false, wizbini);
                            }
                        } else {
                            if (urlp.calendar_view == 1) {
                                reportXML = report.getLearningReport(con, sess, urlp.cwPage, prof, static_env, urlp.usr_ent_id, urlp.calendar_year, urlp.ent_id_lst, urlp.tnd_id_lst, urlp.ats_id_lst, urlp.att_create_start_datetime, urlp.att_create_end_datetime, flag/*(urlp.download==1)*/, false, urlp.rte_id, urlp.spec_name, urlp.spec_value, wizbini,urlp.isMyStaff,null);
                            } else {
                                if (urlp.download == 4) {
                                    if (!isInProgress) {
                                        exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                                    }
                                }else {
                            		report.v_itm_id = urlp.itm_id; 
                            		reportXML = report.getLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag, false, wizbini);
                                }
                            }
                        }
                    }
                    else if (urlp.rpt_type.equalsIgnoreCase(Report.LEARNING_ACTIVITY_COS)) {
                        urlp.lrn_report();
                        LearnerReport report = new LearnerCosReport();
                        // for report export
                        ExportController controller = null;
                        ReportCosExporter exporter = null;
                        boolean isInProgress = false;
                        String window_name = null;
                        if (urlp.download == 4) {
                            controller = new ExportController();
                            exporter = new ReportCosExporter(con, controller);
                            if (sess.getAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER) == null) {
                                sess.setAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
                            }else {
                                isInProgress = true;
                            }
                        }
                        if (urlp.rsp_id > 0) {
                            if (urlp.download == 4 ) {
                                if (!isInProgress) {
                                    exporter.getReportXlsByRsp(urlp.rsp_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                            reportXML = report.getLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, flag/*(urlp.download==1)*/, false, wizbini);
                            }
                        } else {
                            if (urlp.calendar_view == 1) {
                                reportXML = report.getLearningReport(con, sess, urlp.cwPage, prof, static_env, urlp.usr_ent_id, urlp.calendar_year, urlp.ent_id_lst, urlp.tnd_id_lst, urlp.ats_id_lst, urlp.att_create_start_datetime, urlp.att_create_end_datetime, flag/*(urlp.download==1)*/, false, urlp.rte_id, urlp.spec_name, urlp.spec_value, wizbini,urlp.isMyStaff,null);
                            } else {
                                if (urlp.download == 4) {
                                    if (!isInProgress) {
                                        exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                                    }
                                }else {
                                reportXML = report.getLearningReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag/*(urlp.download==1)*/, false, wizbini);
                                }
                            }
                        }
                    }
                     else if (urlp.rpt_type.equalsIgnoreCase(Report.COURSE)) {
                        CourseReport report = new CourseReport();

                        if (urlp.rsp_id > 0) {
                            reportXML = report.getCourseReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, null, flag/*(urlp.download==1)*/);
                        } else {
                            reportXML = report.getCourseReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.rsp_title, null, flag/*(urlp.download==1)*/);
                        }
                    } else if (urlp.rpt_type.endsWith(Report.MODULE)) {
                        urlp.lrn_report();
                        LearningModuleReport moduleReport = new LearningModuleReport();
                        ExportController controller = null;
                        ModuleRptExport exporter = null;
                        boolean isInProgress = false;
                        String window_name = null;
                        if (urlp.download == 4) {
                            controller = new ExportController();
                            exporter = new ModuleRptExport(con, controller);
                            if (sess.getAttribute(urlp.window_name + ModuleRptExport.EXPORT_CONTROLLER) == null) {
                                sess.setAttribute(urlp.window_name + ModuleRptExport.EXPORT_CONTROLLER, controller);
                            } else {
                                isInProgress = true;
                            }
                            if (!isInProgress) {
                                if (urlp.rsp_id > 0) {
                                    exporter.getReportXlsByRsp(urlp.rsp_id, prof, wizbini, urlp.window_name);
                                } else {
                                    exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                                }
                            }
                        } else {
                            reportXML = new String [1];
                            reportXML[0] = moduleReport.getReportXML(con, request, prof, urlp.rsp_id, urlp.rte_id, urlp.rsp_title, urlp.spec_name, urlp.spec_value, wizbini.cfgTcEnabled);
                        }
                    } else if (urlp.rpt_type.toUpperCase().startsWith(Report.SURVEY_PREFIX)) {
                    	                    	
                    	
                        SurveyReport report = new SurveyReport(urlp.rpt_type);
                        if (urlp.rpt_type.toUpperCase().equals("SURVEY_COS_GRP")) {
                        	urlp.que_report_by_no();
                            SurveyQueReport gsrd = new SurveyQueReport(con);
                            boolean isShowAllFBAns;
                            boolean isXlsFile;

                            if (urlp.que_id <= 0) {
                                urlp.que_id = 0;
                                isShowAllFBAns = false;
                                isXlsFile = false;
                                if (urlp.download == 4) {
                                    isShowAllFBAns = true;
                                    isXlsFile = true;
                                }
                            }
                            else {
                                isShowAllFBAns = true;
                                isXlsFile = false;
                            }
                            
                            if (urlp.download == 4) {
                                // get the Absolute path of xls file
                                StringBuffer xlsPath = new StringBuffer();
                                String timeDir = String.valueOf(System.currentTimeMillis());
                                xlsPath.append(wizbini.getFileUploadTmpDirAbs()).append(cwUtils.SLASH).append(timeDir);

                                // get the relative path of xls file
                                StringBuffer xlsFile = new StringBuffer();
                                String tempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
                                
                                String reportFileName = report.getXlsFileBySpec(con, request, sess, urlp.cwPage, prof, static_env,wizbini,  urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.rsp_title, flag, timeDir);
                                String filePath = ".." + cwUtils.SLASH + tempDirName + cwUtils.SLASH + timeDir + cwUtils.SLASH + reportFileName;
                                String newFilePath = cwUtils.replaceSlashToHttp(filePath);
                                xlsFile.append(newFilePath);
                                //xlsFile.append("..").append(cwUtils.SLASH).append(tempDirName).append(cwUtils.SLASH).append(timeDir).append(cwUtils.SLASH).append(reportFileName);
                                reportFilePath = xlsFile.toString();
                            }
                            else {
                            	if (urlp.rsp_id > 0) {
                                    reportXML = report.getSurveyReport(con, request, sess, urlp.cwPage, prof, static_env,wizbini, urlp.rsp_id, flag/*(urlp.download==1)*/);
                                }
                                else {
                                    reportXML = report.getSurveyReport(con, request, sess, urlp.cwPage, prof, static_env,wizbini,  urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.rsp_title, flag/*(urlp.download==1)*/);
                                }
                            }
                        }
                        else if(urlp.rpt_type.toUpperCase().equals("SURVEY_QUE_GRP")){
                            urlp.que_report_by_no();
                            SurveyQueReport gsrd = new SurveyQueReport(con);
                            boolean isShowAllFBAns;
                            boolean isXlsFile;

                            if (urlp.que_id <= 0) {
                                urlp.que_id = 0;
                                isShowAllFBAns = false;
                                isXlsFile = false;
                                if (urlp.download == 4) {
                                    isShowAllFBAns = true;
                                    isXlsFile = true;
                                }
                            }
                            else {
                                isShowAllFBAns = true;
                                isXlsFile = false;
                            }
                            
                            if (urlp.download == 4) {
                                // get the Absolute path of xls file
                                StringBuffer xlsPath = new StringBuffer();
                                String timeDir = String.valueOf(System.currentTimeMillis());
                                xlsPath.append(wizbini.getFileUploadTmpDirAbs()).append(cwUtils.SLASH).append(timeDir);

                                // get the relative path of xls file
                                StringBuffer xlsFile = new StringBuffer();
                                String tempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
                                
                                String reportFileName;
                                
                                if (urlp.rsp_id > 0) {
                                    reportFileName = gsrd.getXlsFileByRsp(prof, wizbini, urlp.rsp_id, report, urlp.que_id, isShowAllFBAns, isXlsFile, prof.cur_lan, xlsPath.toString(), wizbini.cfgSysSetupadv.getEncoding(), prof.root_ent_id);
                                }
                                else {
                                    reportFileName = gsrd.getXlsFileBySpec(prof, wizbini, urlp.spec_name, urlp.spec_value, urlp.que_id, isShowAllFBAns, isXlsFile, prof.cur_lan, xlsPath.toString(), wizbini.cfgSysSetupadv.getEncoding(), prof.root_ent_id);
                                }
                                String filePath = ".." + cwUtils.SLASH + tempDirName + cwUtils.SLASH + timeDir + cwUtils.SLASH + reportFileName;
                                String newFilePath = cwUtils.replaceSlashToHttp(filePath);
                                xlsFile.append(newFilePath);
                                //xlsFile.append("..").append(cwUtils.SLASH).append(tempDirName).append(cwUtils.SLASH).append(timeDir).append(cwUtils.SLASH).append(reportFileName);
                                reportFilePath = xlsFile.toString();
                            }
                            else {
                                if (urlp.rsp_id > 0) {
                                    reportXML = gsrd.getReportByRsp(prof, wizbini,urlp.rsp_id, report, urlp.que_id, isShowAllFBAns, isXlsFile, prof.cur_lan, wizbini.cfgSysSetupadv.getEncoding(), prof.root_ent_id);
                                }
                                else {
                                    reportXML = gsrd.getReportBySpec(prof, wizbini, urlp.spec_name, urlp.spec_value, urlp.que_id, isShowAllFBAns, isXlsFile, prof.cur_lan, wizbini.cfgSysSetupadv.getEncoding(), prof.root_ent_id);
                                }
                            }
                        } else if(urlp.rpt_type.toUpperCase().equals("SURVEY_EVN_QUE_GRP")) {
                        	EvnSurveyQueReport evnSurveyQueRpt = new EvnSurveyQueReport(con);
                        	boolean isShowAllFBAns = false;
                        	if (urlp.download == 4) {
                                // get the Absolute path of xls file
                                StringBuffer xlsPath = new StringBuffer();
                                String timeDir = String.valueOf(System.currentTimeMillis());
                                xlsPath.append(wizbini.getFileUploadTmpDirAbs()).append(cwUtils.SLASH).append(timeDir);

                                // get the relative path of xls file
                                StringBuffer xlsFile = new StringBuffer();
                                String tempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
                                String reportFileName = evnSurveyQueRpt.getXlsFileByRsp(prof, wizbini, urlp.spec_name, urlp.spec_value, isShowAllFBAns, prof.cur_lan, wizbini.cfgSysSetupadv.getEncoding(), prof.root_ent_id, xlsPath.toString());
                                
                                xlsFile.append("..").append(cwUtils.SLASH).append(tempDirName).append(cwUtils.SLASH).append(timeDir).append(cwUtils.SLASH).append(reportFileName);
                                reportFilePath = xlsFile.toString();
                            } else {
                            	reportXML = evnSurveyQueRpt.getReportBySpec(prof, wizbini, urlp.spec_name, urlp.spec_value, isShowAllFBAns, prof.cur_lan, wizbini.cfgSysSetupadv.getEncoding(), prof.root_ent_id);
                            }
                        } 
                    }else if(urlp.rpt_type.toUpperCase().equals(Report.ASSESSMENT_QUE_GRP)){
                        AssessmentQueReport report = new AssessmentQueReport();

                        if (urlp.download == 4) {
                            // get the Absolute path of xls file
                            StringBuffer xlsPath = new StringBuffer();
                            String timeDir = String.valueOf(System.currentTimeMillis());
                            xlsPath.append(wizbini.getFileUploadTmpDirAbs()).append(cwUtils.SLASH).append(timeDir);

                            // get the relative path of xls file
                            StringBuffer xlsFile = new StringBuffer();
                            String tempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
                            
                            String reportFileName;
                            
                            if (urlp.rsp_id > 0) {
                                reportFileName = report.getXlsFileByRsp(con,prof, wizbini, urlp.rsp_id, prof.cur_lan, xlsPath.toString(), wizbini.cfgSysSetupadv.getEncoding(),true);
                            }
                            else {
                                reportFileName = report.getXlsFileBySpec(con,prof, wizbini, urlp.rte_id, urlp.spec_name, urlp.spec_value, prof.cur_lan, xlsPath.toString(), wizbini.cfgSysSetupadv.getEncoding(),true);
                            }
                            String filePath = ".." + cwUtils.SLASH + tempDirName + cwUtils.SLASH + timeDir + cwUtils.SLASH + reportFileName;
                            String newFilePath = cwUtils.replaceSlashToHttp(filePath);
                            xlsFile.append(newFilePath);
                            //xlsFile.append("..").append(cwUtils.SLASH).append(tempDirName).append(cwUtils.SLASH).append(timeDir).append(cwUtils.SLASH).append(reportFileName);
                            reportFilePath = xlsFile.toString();
                            response.sendRedirect(cwUtils.replaceSlashToHttp(reportFilePath));
                        }
                        else {
                            if (urlp.rsp_id > 0) {
                                reportXML = report.getAssessmentReportByRsp(con, request, prof, urlp.rsp_id, wizbini,true);
                            }
                            else {
                                reportXML = report.getAssessmentReportBySpec(con, request, prof, urlp.rte_id, urlp.spec_name, urlp.spec_value,wizbini,true);
                            }
                        }
                    }else if(urlp.rpt_type.toUpperCase().equals(Report.QUE_STATISTIC)){
                    	
                    	AssessmentQueReport report = new AssessmentQueReport();
                    	
                        StringBuffer xlsPath = new StringBuffer();
                        String timeDir = String.valueOf(System.currentTimeMillis());
                        xlsPath.append(wizbini.getFileUploadTmpDirAbs()).append(cwUtils.SLASH).append(timeDir);

                        StringBuffer xlsFile = new StringBuffer();
                        String tempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
                        
                        String reportFileName;
                        
                        String itmId = request.getParameter("itm_id");
                        String modId = request.getParameter("mod_id"); 
                        
                        String[] spec_name = {"all_user_ind","mod_id","itm_id","answer_for_lrn", "answer_for_course_lrn","attempt_type","content_lst","group_by"};
                        String[] spec_value = {"1",modId,itmId,"1","1","NUMBERED~ALL","res_fdr~res_type~res_diff~attempt_cnt~correct~incorrect~partial_correct~not_graded~avg_sore~","QUE"};
                        
                        reportFileName = report.getXlsFileBySpec(con,prof, wizbini, 10, spec_name, spec_value, prof.cur_lan, xlsPath.toString(), wizbini.cfgSysSetupadv.getEncoding(),false);
                        
                        String filePath = ".." + cwUtils.SLASH + tempDirName + cwUtils.SLASH + timeDir + cwUtils.SLASH + reportFileName;
                        String newFilePath = cwUtils.replaceSlashToHttp(filePath);
                        xlsFile.append(newFilePath);
                        reportFilePath = xlsFile.toString();
                        response.sendRedirect(cwUtils.replaceSlashToHttp(reportFilePath));
                        
                    }else if (urlp.rpt_type.equalsIgnoreCase(Report.EXAM_PAPER_STAT)) {
                        urlp.lrn_report();
                        ExamPaperStatReport report = new ExamPaperStatReport();
                        // for report export
                        ExportController controller = null;
                        ExamPaperStatExporter exporter = null;
                        boolean isInProgress = false;
                        if (urlp.download == 4) {
                            controller = new ExportController();
                            exporter = new ExamPaperStatExporter(con, controller);
                            if (sess.getAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER) == null) {
                                sess.setAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
                            }else {
                                isInProgress = true;
                            }
                        }
                        if (urlp.rsp_id > 0) {
                            if (urlp.download == 4 ) {
                                if (!isInProgress) {
                                    exporter.getReportXlsByRsp(urlp.rsp_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                            	reportXML = report.getExamPaperStatReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, flag, false, wizbini);
                            }
                        } else {
                        	if (urlp.download == 4) {
                                if (!isInProgress) {
                                    exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                            	reportXML = report.getExamPaperStatReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag, false, wizbini);
                            }
                        }
                    } else if (urlp.rpt_type.equalsIgnoreCase(Report.TRAIN_FEE_STAT)) {
                        urlp.lrn_report();
                        TrainFeeStatReport report = new TrainFeeStatReport();
                        // for report export
                        ExportController controller = null;
                        TrainFeeStatExporter exporter = null;
                        boolean isInProgress = false;
                        if (urlp.download == 4) {
                            controller = new ExportController();
                            exporter = new TrainFeeStatExporter(con, controller);
                            if (sess.getAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER) == null) {
                                sess.setAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
                            }else {
                                isInProgress = true;
                            }
                        }
                        if (urlp.rsp_id > 0) {
                            if (urlp.download == 4 ) {
                                if (!isInProgress) {
                                    exporter.getReportXlsByRsp(urlp.rsp_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                            	reportXML = report.getReportView(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, flag, false, wizbini);
                            }
                        } else {
                        	if (urlp.download == 4) {
                                if (!isInProgress) {
                                    exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                            	reportXML = report.getReportView(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag, false, wizbini);
                            }
                        }
                    } else if (urlp.rpt_type.equalsIgnoreCase(Report.TRAIN_COST_STAT)) {
                        urlp.lrn_report();
                        TrainCostStatReport report = new TrainCostStatReport();
                        // for report export
                        ExportController controller = null;
                        TrainCostStatExporter exporter = null;
                        boolean isInProgress = false;
                        if (urlp.download == 4) {
                            controller = new ExportController();
                            exporter = new TrainCostStatExporter(con, controller);
                            if (sess.getAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER) == null) {
                                sess.setAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
                            }else {
                                isInProgress = true;
                            }
                        }
                        if (urlp.rsp_id > 0) {
                            if (urlp.download == 4 ) {
                                if (!isInProgress) {
                                    exporter.getReportXlsByRsp(urlp.rsp_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                            	reportXML = report.getReportView(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, flag, false, wizbini);
                            }
                        } else {
                        	if (urlp.download == 4) {
                                if (!isInProgress) {
                                    exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                                }
                            }else {
                            	reportXML = report.getReportView(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag, false, wizbini);
                            }
                        }
                    }else if(urlp.rpt_type.equals(Report.EXP_AEITEM_LESSON)) {

                        urlp.lrn_report();
                        ExportController controller = null;
                        controller = new ExportController();
                        if (sess.getAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER) == null) {
                        	sess.setAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
                           }
                        aeItemLessonExport evnSurveyQueRpt = new aeItemLessonExport(con,controller);
                        evnSurveyQueRpt.getReportXlsByRsp2ils(urlp.ils_id,urlp.ils_itm_id, prof, wizbini, urlp.window_name);
                        response.sendRedirect(cwUtils.replaceSlashToHttp(evnSurveyQueRpt.filePath + ".xls"));
//                    	StringBuffer xlsPath = new StringBuffer();
//                        String timeDir = String.valueOf(System.currentTimeMillis());
//                        xlsPath.append(wizbini.getFileUploadTmpDirAbs()).append(cwUtils.SLASH).append(timeDir);
//
//                        // get the relative path of xls file
//                        StringBuffer xlsFile = new StringBuffer();
//                        String tempDirName = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getName();
//                        String reportFileName = evnSurveyQueRpt.getXlsFile(prof, wizbini, urlp.ils_id, urlp.ils_itm_id, prof.cur_lan, wizbini.cfgSysSetupadv.getEncoding(), prof.root_ent_id, xlsPath.toString());
//                        
//                        xlsFile.append("..").append(cwUtils.SLASH).append(tempDirName).append(cwUtils.SLASH).append(timeDir).append(cwUtils.SLASH).append(reportFileName);
//                        reportFilePath = xlsFile.toString();
                    } else if (urlp.rpt_type.equalsIgnoreCase(Report.FM_FEE)) {
                        FMFeeReport report = new FMFeeReport();
                        // for report export
                        ExportController controller = null;
                        FMFeeExporter exporter = null;
                        boolean isInProgress = false;
                        if (urlp.download == 4) {
                            controller = new ExportController();
                            exporter = new FMFeeExporter(con, controller);
                            if (sess.getAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER) == null) {
                                sess.setAttribute(urlp.window_name + LearnerRptExporter.EXPORT_CONTROLLER, controller);
                            }else {
                                isInProgress = true;
                            }
                        }
                        if (urlp.download == 4) {
                            if (!isInProgress) {
                                exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                            }
                        }else {
                        	reportXML = report.getReportView(con, request, prof, urlp.spec_name, urlp.spec_value, wizbini);
                        }
                    }else if( urlp.rpt_type.toUpperCase().startsWith(Report.SELF) ) {

                        urlp.self_report();
                        //force the user can view self report only
                        for(int i=0; i<urlp.spec_name.length; i++) {
                            if( urlp.spec_name[i].equalsIgnoreCase("ent_id") )  {
                                urlp.spec_value[i] = Long.toString(prof.usr_ent_id);
                            } else if( urlp.spec_name[i].equalsIgnoreCase("show_itm_credit") ) {
                                urlp.spec_value[i] = "false";
                            }
                        }
                        reportXML = urlp.report.getLearningReport(con, request, sess, urlp.cwPage, prof, null, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag/*(urlp.download==1)*/, false, null);

                    }else if( urlp.rpt_type.toUpperCase().startsWith(Report.GROUP) ) {

                        //access control, not yet implement

                        urlp.group_report();
                        for(int i=0; i<urlp.spec_name.length; i++) {
                            if( urlp.spec_name[i].equalsIgnoreCase("ent_id") ){

                                DbRoleTargetEntity dbRte = new DbRoleTargetEntity();
                                dbRte.rte_usr_ent_id = prof.usr_ent_id;
                                dbRte.rte_rol_ext_id = prof.current_role;
                                Vector vec = dbRte.getRoleTargetEntityIds(con);
                                String id_lst = "";
                                for(int j=0; j<vec.size(); j++)
                                    id_lst += vec.elementAt(i) + "~";
                                long[] entIdLst = dbUserGroup.constructEntId(con, id_lst);

                                urlp.spec_value[i] = "";
                                for(int j=0; j<entIdLst.length; j++)
                                    urlp.spec_value[i] += Long.toString(entIdLst[j]) + "~";

                            } else if( urlp.spec_name[i].equalsIgnoreCase("show_itm_credit") ) {
                                urlp.spec_value[i] = "false";
                            }
                        }
                        reportXML = urlp.report.getLearningReport(con, request, sess, urlp.cwPage, prof, null, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag/*(urlp.download==1)*/, false, null);

                    } else if( urlp.rpt_type.toUpperCase().startsWith(Report.SELF_CPT) ) {

                        urlp.self_cpt_report();
                        //force the user can view self report only
                        for(int i=0; i<urlp.spec_name.length; i++) {
                            if( urlp.spec_name[i].equalsIgnoreCase("ent_id") )  {
                                urlp.spec_value[i] = Long.toString(prof.usr_ent_id);
                                break;
                            }
                        }
                        reportXML = urlp.report.getLearningReport(con, request, sess, urlp.cwPage, prof, null, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag/*(urlp.download==1)*/, false, null);

                    }else if( urlp.rpt_type.toUpperCase().startsWith(Report.GROUP_CPT) ) {

                        //access control, not yet implement

                        urlp.group_cpt_report();
                        for(int i=0; i<urlp.spec_name.length; i++) {
                            if( urlp.spec_name[i].equalsIgnoreCase("ent_id") ){

                                DbRoleTargetEntity dbRte = new DbRoleTargetEntity();
                                dbRte.rte_usr_ent_id = prof.usr_ent_id;
                                dbRte.rte_rol_ext_id = prof.current_role;
                                Vector vec = dbRte.getRoleTargetEntityIds(con);
                                String id_lst = "";
                                for(int j=0; j<vec.size(); j++)
                                    id_lst += vec.elementAt(i) + "~";
                                long[] entIdLst = dbUserGroup.constructEntId(con, id_lst);

                                urlp.spec_value[i] = "";
                                for(int j=0; j<entIdLst.length; j++)
                                    urlp.spec_value[i] += Long.toString(entIdLst[j]) + "~";
                                break;
                            }
                        }
                        reportXML = urlp.report.getLearningReport(con, request, sess, urlp.cwPage, prof, null, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.usr_ent_id, urlp.rsp_title, flag/*(urlp.download==1)*/, false, null);

                        }else if (urlp.rpt_type.startsWith(Report.MODULE_PREFIX)) {

                            ModuleReport report = new ModuleReport();
                            reportXML = report.getModuleReport(con, prof, urlp.rpt_type, urlp.rte_id, urlp.spec_name, urlp.spec_value);

                        } else if (urlp.rpt_type.toUpperCase().startsWith(Report.GLOBAL_ENROLLMENT)) {
                            GlobalEnrollmentReport report = new GlobalEnrollmentReport();

                            if (urlp.rsp_id > 0) {
                                //DENNIS
                                reportXML = report.getGlobalEnrollmentReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rsp_id, flag);
                            } else {
                                reportXML = report.getGlobalEnrollmentReport(con, request, sess, urlp.cwPage, prof, static_env, urlp.rte_id, urlp.spec_name, urlp.spec_value, urlp.rsp_title, flag);
                            }
                        } else if (urlp.rpt_type.toUpperCase().equals(Report.MODULE_EVN_OF_COS)) {
                        	urlp.getModuleEvnOfCosParams();
                        	// for report export
                            ExportController controller = new ExportController();;
							ModuleEvnOfCosExporter exporter = new ModuleEvnOfCosExporter(con, controller);
							boolean isInProgress = false;
							if (sess.getAttribute(urlp.window_name + ModuleEvnOfCosExporter.EXPORT_CONTROLLER) == null) {
								sess.setAttribute(urlp.window_name + ModuleEvnOfCosExporter.EXPORT_CONTROLLER, controller);
							} else {
								isInProgress = true;
							}
                        	
							if (!isInProgress) {
                                exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
                            }
                        }  else if (urlp.rpt_type.endsWith(Report.CREDIT)) {
                        	urlp.lrn_report();
							CreditReport creditRpt = new CreditReport();
							ExportController controller = null;
							CreditReportExporter exporter = null;
							boolean isInProgress = false;
							String window_name = null;
							if (urlp.download == 4) {
								controller = new ExportController();
								exporter = new CreditReportExporter(con, controller);
								if (sess.getAttribute(urlp.window_name + ModuleRptExport.EXPORT_CONTROLLER) == null) {
									sess.setAttribute(urlp.window_name + ModuleRptExport.EXPORT_CONTROLLER, controller);
								} else {
									isInProgress = true;
								}
								if (!isInProgress) {
									if (urlp.rsp_id > 0) {
										exporter.getReportXlsByRsp(urlp.rsp_id, prof, wizbini, urlp.window_name);
									} else {
										exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, urlp.rte_id, prof, wizbini, urlp.window_name);
									}
								}
	                        } else {
	                            reportXML = new String [1];
	                            reportXML[0] = creditRpt.getReportXML(con, request, prof, urlp.rsp_id, urlp.rte_id, urlp.rsp_title, urlp.spec_name, urlp.spec_value, wizbini.cfgTcEnabled);
	                        }
                        } else if (urlp.rpt_type.equalsIgnoreCase(Report.EXP_TARGET_LRN)) {
                            urlp.lrn_report();
                            // for report export
                            ExportController controller = null;
                            CourseTargetLrnExporter exporter = null;
                            boolean isInProgress = false;
                            if (urlp.download == 4) {
                                controller = new ExportController();
                                exporter = new CourseTargetLrnExporter(con, controller);
                                if (sess.getAttribute(urlp.window_name + CourseTargetLrnExporter.EXPORT_CONTROLLER) == null) {
                                    sess.setAttribute(urlp.window_name + CourseTargetLrnExporter.EXPORT_CONTROLLER, controller);
                                }else {
                                    isInProgress = true;
                                }
                            }
                   
                            if (!isInProgress) {
                                exporter.getReportXlsBySpec(urlp.spec_name, urlp.spec_value, 0, prof, wizbini, urlp.window_name);
                            } 
                        }
                        else {
                            throw new cwException("No such report type: " + urlp.rpt_type);
                        }
                    
                    
                    if (urlp.download == 2){
                        Timestamp curTime = cwSQL.getTime(con);
    //                    Calendar cal = Calendar.getInstance();
    //                    cal.setTime(curTime);
    //                    String curTimeString = cal.get(Calendar.YEAR) + "" + time2String(cal, Calendar.MONTH) + time2String(cal, Calendar.DAY_OF_MONTH) + "-" + time2String(cal, Calendar.HOUR_OF_DAY) + time2String(cal, Calendar.MINUTE) + time2String(cal, Calendar.SECOND);
                        deleteFiles(static_env.INI_DIR_UPLOAD_TMP, true);
                        int genFolder = (new Random()).nextInt();
                        if (genFolder < 0){
                            genFolder *= -1;
                        }
                        File workingFolder = new File(static_env.INI_DIR_UPLOAD_TMP, ""+genFolder);
                        workingFolder.mkdir();

                        Vector prodFiles = new Vector();
                        String[] arrFilenames = new String[vtXML.size()];
                        for (int i=0; i<vtXML.size(); i++){
                            aeItem itm = new aeItem();
                            itm.itm_id = dbCourse.getCosItemId(con, cos_id_lst[i]);
                            String code = itm.getItemCode(con);
                            String filename = code + ".csv";
                            xml = formatXML((String)vtXML.elementAt(i), metaXML, REPORT);
                            File xslFile = new File(prof.xsl_root, urlp.stylesheet);
                            xslFile = new File(static_env.DOC_ROOT, xslFile.getPath());
                            String csv = cwXSL.processFromFile(xml.toString(), xslFile.getPath());
                            File resultFile = new File(workingFolder, filename);
                            BufferedWriter outBuf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(resultFile), static_env.ENCODING));
                            outBuf.write(csv.toCharArray());
                            outBuf.flush();
                            outBuf.close();
                            prodFiles.addElement(resultFile);
                            arrFilenames[i] = filename;
                        }
                        File zipFile = new File(workingFolder, urlp.rpt_name + ".zip");
                        dbUtils.makeZip(workingFolder + dbUtils.SLASH + urlp.rpt_name + ".zip", workingFolder.toString(), arrFilenames, false);
                        String tmpPath = zipFile.getPath();
                        tmpPath = tmpPath.substring(static_env.DOC_ROOT.length());

                        response.sendRedirect(cwUtils.replaceSlashToHttp(".." + dbUtils.SLASH + tmpPath));
                        for (int i=0; i<prodFiles.size(); i++) {
                            File resFile = (File)prodFiles.elementAt(i);
                            resFile.delete();
                        }

                    } else if (urlp.download == 4 && urlp.rpt_type.toUpperCase().equals("SURVEY_QUE_GRP")) {
						response.sendRedirect(cwUtils.replaceSlashToHttp(reportFilePath));
					} else if (urlp.download == 4 && urlp.rpt_type.toUpperCase().equals("SURVEY_EVN_QUE_GRP")) {
						response.sendRedirect(cwUtils.replaceSlashToHttp(reportFilePath));
					} else if (urlp.download == 4 && urlp.rpt_type.toUpperCase().equals("SURVEY_COS_GRP")) {
						response.sendRedirect(cwUtils.replaceSlashToHttp(reportFilePath));
					} else if (urlp.download != 4) {
                        // reportXML==null, then it is a Course Report, report
						// is stored
                        //in variable xml.
                        //else, it is a Learning Report, report is stored into
                        //array reportXML since it may be splitted
                        if(reportXML==null) {
                            xml = formatXML(xml, metaXML, REPORT);
                            if (urlp.cmd.equalsIgnoreCase(GET_RPT + XML)) {
                                static_env.outputXML(out, xml);
                            }else {
                                generalAsHtml(xml.toString(), out , urlp.stylesheet);
                            }
                        } else {
                            long start_time = System.currentTimeMillis();
                            String tempReport = null;
                            String formattedXML = "";
                            if (urlp.cmd.equalsIgnoreCase(GET_RPT + XML)) {
                                StringBuffer strBuf = new StringBuffer(1024);
                                for(int i=0; i<reportXML.length; i++) {
                                    strBuf.append(reportXML[i]);
                                }
                                formattedXML = formatXML(strBuf.toString(), metaXML, REPORT);
                                static_env.outputXML(out, formattedXML);
                            } else {
                                int lastIdx = reportXML.length - 1;
                                for(int i=0; i<=lastIdx; i++) {
                                    formattedXML = formatXML(reportXML[i], metaXML, REPORT);
                                    //Use tempReportBuf to store the transformed result
                                    //and remove the 1st line, which is report header,
                                    //when i!=0
                                    tempReport = aeUtils.transformXML(formattedXML, urlp.stylesheet, static_env, prof.xsl_root);
                                    if (i != 0) {
                                        tempReport = tempReport.substring(tempReport.indexOf("<html>") + 6);
                                    }
                                    if (i != lastIdx) {
                                        tempReport = tempReport.substring(0, tempReport.lastIndexOf("</html>"));
                                    }
                                    out.print(tempReport);
                                    out.flush();
                                }
                                out.close();
                            }
                        }
                    }
                } catch(qdbException e) {
                    throw new cwException(e.getMessage());
                } catch (SQLException e) {
                    CommonLog.error(e.getMessage(),e);
                    throw new cwException(e.getMessage());
                } catch (IOException e) {
                    throw new cwException(e.getMessage());
                }
            } else if(urlp.cmd.equalsIgnoreCase(LRN_SOLN_HIST) ||urlp.cmd.equalsIgnoreCase(LRN_SOLN_HIST+XML)){
                String xml = null;
				String[] reportXML = null;
				urlp.report(sess);
				urlp.lrn_report();
				urlp.pagination();
				try {
					String metaXML = "";
					boolean flag = false;
					if (prof != null && urlp.download != 4) {
						AcPageVariant acPageVariant = new AcPageVariant(con);
						acPageVariant.ent_owner_ent_id = prof.root_ent_id;
						acPageVariant.ent_id = prof.usr_ent_id;
						acPageVariant.rol_ext_id = prof.current_role;
						if (xslQuestions == null) {
							xslQuestions = new Hashtable();
						}
						metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
					}
					metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
					urlp.lrn_report();
					LearnerReport report = new LearnerReport();
					// for report export
					reportXML = report.getLearningHistory(con, sess,
							urlp.cwPage, prof, static_env, urlp.usr_ent_id,
							urlp.calendar_year, urlp.ent_id_lst,
							urlp.tnd_id_lst, urlp.itm_title,
							urlp.itm_title_partial_ind, urlp.ats_id_lst,
							urlp.itm_start_datetime, urlp.itm_end_datetime,
							urlp.att_create_start_datetime,
							urlp.att_create_end_datetime,
							urlp.att_start_datetime, urlp.att_end_datetime,
							flag/* (urlp.download==1) */, false, urlp.rte_id,
							urlp.spec_name, urlp.spec_value, wizbini);
					if (reportXML == null) {
						xml = formatXML(xml, metaXML, REPORT);
						if (urlp.cmd.equalsIgnoreCase(LRN_SOLN_HIST + XML)) {
							static_env.outputXML(out, xml);
						} else {
							generalAsHtml(xml.toString(), out, urlp.stylesheet);
						}
					} else {
						String tempReport = null;
						String formattedXML = "";
						if (urlp.cmd.equalsIgnoreCase(LRN_SOLN_HIST + XML)) {
							StringBuffer strBuf = new StringBuffer(1024);
							for (int i = 0; i < reportXML.length; i++) {
								strBuf.append(reportXML[i]);
							}
							formattedXML = formatXML(strBuf.toString(), metaXML, REPORT);
							static_env.outputXML(out, formattedXML);
						} else {
							int lastIdx = reportXML.length - 1;
							for (int i = 0; i <= lastIdx; i++) {
								formattedXML = formatXML(reportXML[i], metaXML, REPORT);
								// Use tempReportBuf to store the transformed
								// result
								// and remove the 1st line, which is report
								// header,
								// when i!=0
								tempReport = aeUtils.transformXML(formattedXML, urlp.stylesheet, static_env, prof.xsl_root);
								if (i != 0) {
									tempReport = tempReport.substring(tempReport.indexOf("<html>") + 6);
								}
								if (i != lastIdx) {
									tempReport = tempReport.substring(0, tempReport.lastIndexOf("</html>"));
								}
								out.print(tempReport);
								out.flush();
							}
							out.close();
						}
					}
				} catch (qdbException e) {
					throw new cwException(e.getMessage());
				} catch (SQLException e) {
					CommonLog.error(e.getMessage(),e);
					throw new cwException(e.getMessage());
				} catch (IOException e) {
					throw new cwException(e.getMessage());
				}
            } else if( urlp.cmd.equalsIgnoreCase(GET_RPT_LST) ||
                        urlp.cmd.equalsIgnoreCase(GET_RPT_LST + XML) ) {

                String xml = null;
                urlp.report(sess);
                urlp.pagination();
                
            	if (wizbini.cfgTcEnabled && !ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id) && !wizbini.cfgSysSetupadv.isTcIndependent()) {
                    if(AccessControlWZB.isRoleTcInd(prof.current_role)){
	            		cwSysMessage sms = new cwSysMessage("TC010");
	                    msgBox(ServletModule.MSG_ERROR, sms, urlp.url_failure, out);
	                    return;
                    } 
            	
            	}

                xml = Report.getReportList(con, prof, urlp.rpt_type_lst, urlp.show_public);
                xml = formatXML(xml, null, REPORT);

                if (urlp.cmd.equalsIgnoreCase(GET_RPT_LST + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }
            } else if( urlp.cmd.equalsIgnoreCase(INS_RPT_SPEC)) {
                urlp.report(sess);

                Report.insUpdReportSpec(con, prof, urlp.usr_ent_id, urlp.rte_id, 0, urlp.rsp_title, urlp.spec_name, urlp.spec_value);
                con.commit();
                cwSysMessage sms = new cwSysMessage(SMSG_INS_RPT_SPEC);
                msgBox(ServletModule.MSG_STATUS, sms, urlp.url_success, out);

                return;
            } else if( urlp.cmd.equalsIgnoreCase(UPD_RPT_SPEC)) {
                urlp.report(sess);

                Report.insUpdReportSpec(con, prof, urlp.usr_ent_id, urlp.rte_id, urlp.rsp_id, urlp.rsp_title, urlp.spec_name, urlp.spec_value);
                con.commit();
                response.sendRedirect(urlp.url_success);

                return;
            } else if( urlp.cmd.equalsIgnoreCase(DEL_RPT_SPEC)) {
                urlp.report(sess);

                Report.delReportSpec(con, prof, urlp.rsp_id);
                con.commit();
                response.sendRedirect(urlp.url_success);

                return;
            } else if( urlp.cmd.equalsIgnoreCase(GET_MOD_LST) ||
                urlp.cmd.equalsIgnoreCase(GET_MOD_LST + XML)) {
                urlp.module();

                String xml = ModuleReport.getModuleListAsXML(con, urlp.cos_id_lst, urlp.mod_type_lst, urlp.rte_id);
                xml = formatXML(xml, null, REPORT);

                if (urlp.cmd.equalsIgnoreCase(GET_MOD_LST + XML)) {
                    static_env.outputXML(out, xml);
                }else {
                    generalAsHtml(xml.toString(), out , urlp.stylesheet);
                }

            } else if ( urlp.cmd.equalsIgnoreCase(GET_META_DATA_COURSE)) {
                out.println(CourseReport.getMetaData(con, prof, wizbini));
            } else if ( urlp.cmd.equalsIgnoreCase(GET_META_DATA_LEARNER)) {
                out.println(LearnerReport.getMetaData(con, prof,  wizbini));
            }
        } catch(qdbErrMessage e) {
            cwSysMessage sms = new cwSysMessage(e.getId());
            msgBox(ServletModule.MSG_ERROR, sms, urlp.url_failure, out);
        } catch(cwSysMessage se) {
            msgBox(ServletModule.MSG_ERROR, se, urlp.url_failure, out);
        }
    }

    public void deleteFiles(String reportFolderString, boolean onExpired){
        File reportFolder = new File(reportFolderString);
        String fileLst[] = reportFolder.list();
        for (int i=0; i<fileLst.length; i++){
            File file = new File(reportFolderString, fileLst[i]);
            if (!onExpired || (System.currentTimeMillis() - file.lastModified()) > 5*60*60*1000){
                if (file.isDirectory()){
                    deleteFiles(file.getPath(), false);
                    file.delete();
                }else{
                    file.delete();
                }
            }

        }
    }

}
