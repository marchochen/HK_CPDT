package com.cw.wizbank.ae;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.Application;
import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.accesscontrol.AcCatalog;
import com.cw.wizbank.accesscontrol.AcCourse;
import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.accesscontrol.AcItemPageVariant;
import com.cw.wizbank.accesscontrol.AcModule;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AcQueueManager;
import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AcTreeNode;
import com.cw.wizbank.accesscontrol.AcXslQuestion;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.db.DbItemMessage;
import com.cw.wizbank.ae.db.DbItemRequirement;
import com.cw.wizbank.ae.db.view.ViewItemTemplate;
import com.cw.wizbank.aicc.AICCScriptWriter;
import com.cw.wizbank.cert.Certificate;
import com.cw.wizbank.codetable.CodeTable;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.learningplan.LearningPlan;
//for get score scheme
import com.cw.wizbank.course.CourseContentUtils;
import com.cw.wizbank.course.CourseEnrollScheduler;
import com.cw.wizbank.course.ModulePrerequisiteManagement;
import com.cw.wizbank.course.loadTargetLrnCacheAndCourseEnrollScheduler;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewRoleTargetGroup;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.CheckActiveUserScheduler;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbDisplayOption;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbMessage;
import com.cw.wizbank.qdb.dbProgressAttempt;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbResourcePermission;
import com.cw.wizbank.qdb.dbSSOLink;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tpplan.tpPlanManagement;
import com.cw.wizbank.tpplan.db.dbTpTrainingPlan;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXMLLabel;
import com.cwn.wizbank.cpd.service.CpdDbUtilsForOld;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.oreilly.servlet.MultipartRequest;

public class aeAction extends HttpServlet
{

    //Session Keys
    public static final String SESS_PARENT_ITM_ID = "PARENT_ITM_ID";
    public static final String SESS_ITM_APP_APPROVAL_TYPE = "ITM_APP_APPROVAL_TYPE";
	private final static String SMSG_UPD_MSG = "GEN003";
	private final static String SMSG_CHANGED_MSG = "GEN006";
	private final static String SMSG_CONFLICT_MSG = "AEQM10";

    // default output encoding
    public static final String defEncoding_ = "ISO-8859-1";
    static final String MSG_STATUS    = "STATUS";
    static final String MSG_ERROR     = "ERROR";
    // static env object for all requests
    private static qdbEnv static_env = null;
    

    private static String APPLYEASY = "applyeasy";

    //Dennis:Page Variants
    private static Hashtable xslQuestions = null;

    // using xml in place of wizb.ini for system parameters
    public static WizbiniLoader wizbini = null;
    // load displayOption table dpo_view is lrn_read record
    public static Hashtable diplayOption_hash = null;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Connection con = null;
        try {
        	CommonLog.info("aeAction.init() START...");
            wizbini = WizbiniLoader.getInstance(config);

            // initialize qdbEnv
			//String env = config.getInitParameter("env");
			 static_env = (qdbEnv) config.getServletContext().getAttribute(WizbiniLoader.SCXT_STATIC_ENV);
			 if( static_env == null ) {
				 static_env = new qdbEnv();
				 static_env.init(wizbini);
				 config.getServletContext().setAttribute(WizbiniLoader.SCXT_STATIC_ENV, static_env);
			 }

            //Dennis:Page Variants
            xslQuestions = AcXslQuestion.getQuestions();

            cwSQL sqlCon = new cwSQL();
            sqlCon.setParam(wizbini);
            con = sqlCon.openDB(false);
            
            // 获取数据库连接后，初始Application读取数据库配置信息
            Application.init(con);
            
            /**
             * added to cache the workflow & status list - Emily, 20020906
             */
            aeWorkFlowCache workFlow = new aeWorkFlowCache(con);
            diplayOption_hash = dbDisplayOption.getDisplayOption(con); 

            CommonLog.info("aeAction.init() END");
        } catch (Exception e) {
            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                    con.close();
                }
            } catch (SQLException e1) {
                throw new ServletException(e1);
            }
            throw new ServletException(e);
        }
        try {
            if (con != null && !con.isClosed()) {
                con.commit();
                con.close();
            }
        }
        catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sess = request.getSession(false);

        // If Websphere , get the client character encoding
        // Otherwise, use the default encoding
        // The client encoding is used in the converting unicode function
        String clientEnc = new String();
        ServletContext sc = getServletContext();
        if (sc.getServerInfo().toLowerCase().indexOf(cwUtils.APP_SERVER_WEBSPHERE) > 0) {
            clientEnc = request.getCharacterEncoding();
        }
        if (clientEnc == null || clientEnc.length()==0) {
            clientEnc = dbUtils.ENC_ENG;
        }


        // common http headers
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "Tue, 20 Aug 1996 00:00:00 GMT");

        if (xslQuestions == null) {
            if (sess != null) {
                xslQuestions = (Hashtable)sess.getAttribute(qdbAction.SESS_ACL_CONTROL_XSL_QUES);
            } else {
                xslQuestions = AcXslQuestion.getQuestions();
            }
        }


        if (clientEnc == null) clientEnc = dbUtils.ENC_ENG;
        // get output stream for normal content to client
        response.setContentType("text/html; charset=" + static_env.ENCODING);
        PrintWriter out = response.getWriter();
        Connection con = null;
        loginProfile prof = null;

// get the database connection for this request
            try {
                cwSQL sqlCon = new cwSQL();
                sqlCon.setParam(wizbini);
                con = sqlCon.openDB(false);
            } catch (Exception e) {
                out.println("<b><h3> Sorry, the server is too busy.</h3></b>");
                return;
            }

        // test Multipart content type
        String conType = request.getContentType();
        boolean bMultiPart = false;
        String tmpUploadPath = null;
        MultipartRequest multi = null;
        boolean bFileUpload = false;

        if( conType != null && conType.toLowerCase().startsWith("multipart/form-data") ) {
            bMultiPart = true;
        }
        // try to use utility class
        if( bMultiPart)
        {
                bFileUpload = true;
                java.util.Date ts = new java.util.Date();
                SimpleDateFormat fm = new SimpleDateFormat("SSSHHmmss");
                tmpUploadPath = static_env.INI_ITM_DIR_UPLOAD_TMP + cwUtils.SLASH + fm.format(ts);
                File tmpUploadDir = new File(tmpUploadPath);
                boolean bOk = tmpUploadDir.mkdirs();
                if (!bOk)
                    throw new IOException ("Fails to create temp dir: " + tmpUploadDir);

                // add buffer for other page content
                int maxSize = (int) ((static_env.INI_MAX_UPLOAD_SIZE + 0.1 ) * 1024 * 1024);
                if (request.getContentLength() > maxSize){
					request.getInputStream().skip(request.getContentLength());
					request.getInputStream().close();
                    cwSysMessage msg = new cwSysMessage("GEN007", new Integer(static_env.INI_MAX_UPLOAD_SIZE).toString());
                    try{
                        msgBox(MSG_ERROR, con, msg, (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE), "javascript:history.back()", out);
                    } catch (cwException ce) {
                        out.println("MSGBOX Server error: " + cwUtils.esc4Html(ce.getMessage()));
                     } catch (SQLException se) {
                        out.println("MSGBOX SQL error: " + cwUtils.esc4Html(se.getMessage()));
                     }
                    return;
                }else{
                	
                	multi = new MultipartRequest( request, tmpUploadPath, static_env.ENCODING, maxSize);
                    if(null!=multi.getForBiddenFiles() && !multi.getForBiddenFiles().isEmpty()){
                      	cwSysMessage msg = new cwSysMessage("GEN011", Application.UPLOAD_FORBIDDEN);
                      	try {
							msgBox(MSG_ERROR, con, msg, (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE), "exedjs_function:history.back()", out);
						} catch (cwException ce) {
	                        out.println("MSGBOX Server error: " + cwUtils.esc4Html(ce.getMessage()));
	                     } catch (SQLException se) {
	                        out.println("MSGBOX SQL error: " + cwUtils.esc4Html(se.getMessage()));
	                     }
                    	return;
                    }
                }
        }
        aeReqParam urlp = new aeReqParam(request, bMultiPart, multi);
/*
		String val = (bMultiPart) ? multi.getParameter("save_submitted_params") : request.getParameter("save_submitted_params");
		if( val != null && val.equalsIgnoreCase("true") ) {
			Enumeration enumeration = (bMultiPart) ? multi.getParameterNames() : request.getParameterNames();
			StringBuffer params_xml = new StringBuffer(512);
			String param_name = null;
			String[] param_value = null;
			params_xml.append("<submitted_params_list>");
			while(enumeration.hasMoreElements()){
				param_name = (String) enumeration.nextElement();
				params_xml.append("<param name=\"").append(param_name).append("\">");
				param_value = (bMultiPart) ? multi.getParameterValues(param_name) : request.getParameterValues(param_name);
				for(int i=0; i<param_value.length; i++){
					params_xml.append("<value>")
								.append(cwUtils.esc4XML(param_value[i]))
								.append("</value>");
				}
				params_xml.append("</param>");
			}
			params_xml.append("</submitted_params_list>");
			sess.setAttribute(SUBMITTED_PARAMETERS_AS_XML, params_xml.toString());
		}


		val = (bMultiPart) ? multi.getParameter("get_submitted_params") : request.getParameter("get_submitted_params");
		submitted_params_xml = null;
		if( val != null && val.equalsIgnoreCase("true")) {
			submitted_params_xml = (String) sess.getAttribute(SUBMITTED_PARAMETERS_AS_XML);
		}
*/

long my_time = System.currentTimeMillis();
int my_id = (int)(Math.random() * 1000);
Date my_date = new Date(my_time);
String my_action = urlp.cmd;
CommonLog.debug("[aeAction OPEN] ID:" + my_id  + "\t\t\t\t" + my_date.toString());
cwUtils.setUsedXsl2Sess(request, sess, bMultiPart, multi);

        // service processing starts here
        try{
            AccessControlWZB acl = new AccessControlWZB();
            static_env.changeActiveReqNum(1);
            /**
             * added to cache the workflow & status list - Emily, 20020906
             */
            aeWorkFlowCache workFlow = new aeWorkFlowCache(con);
//System.out.println("aeWorkFlowCache.cachedWorkFlowXML: "+aeWorkFlowCache.cachedWorkFlowXML);
//System.out.println("aeWorkFlowCache.cachedAppStatusList: "+aeWorkFlowCache.cachedAppStatusList);

            //System.out.println("cmd = " + urlp.cmd);
            if (urlp.cmd == null) {
                throw new qdbException("invalid command");
            }
            else if (urlp.cmd.equals("shutdown")) {
                urlp.system();
                static_env.sysShutdown(urlp.password, out);
            }
            else if (urlp.cmd.equals("startup")) {
                urlp.system();
                static_env.sysStartUp(urlp.password, out);
            }
            else if (urlp.cmd.equals("get_request_info")) {
                urlp.system();
                static_env.sysGetReqInfo(urlp.password, out);
            }
            // Don't accept any other command if in shutdown mode
            else if (static_env.shutdownMode)  {
                response.sendRedirect(cwUtils.getRealPath(request, static_env.URL_MAINTENANCE));
            }
			else if ( urlp.cmd.equalsIgnoreCase("USER_IMPORT_NOTIFY_XML") ) {
				CommonLog.debug("########## USER_IMPORT_NOTIFY_XML ##########");
				try{
					urlp.notify(clientEnc, static_env.ENCODING);
					StringBuffer xml = new StringBuffer();
					xml.append(urlp.aeXmsg.getSenderXml(con, static_env.MAIL_ACCOUNT));
					xml.append(urlp.aeXmsg.getReplyToXml(con, static_env.MAIL_ACCOUNT));
					xml.append(urlp.aeXmsg.getEmailsAsXml(con, urlp.ent_ids, urlp.cc_ent_ids, urlp.bcc_ent_ids, static_env.MAIL_ACCOUNT));
					xml.append(urlp.aeXmsg.getActionTakerXml(con, static_env.MAIL_ACCOUNT));

					out.println((xml.toString()).trim());
					CommonLog.debug((xml.toString()).trim());
        
				}catch( Exception e ) {
					CommonLog.error(e.getMessage(),e);
				}
				CommonLog.debug("########## END ##########");
				return;
			} 
            else if ( urlp.cmd.equalsIgnoreCase("ENROLLMENT_IMPORT_NOTIFY_XML") ) {
            	CommonLog.debug("########## ENROLLMENT_IMPORT_NOTIFY_XML ##########");
                try{
                    urlp.notify(clientEnc, static_env.ENCODING);
                    StringBuffer xml = new StringBuffer();
                    xml.append(urlp.aeXmsg.getSenderXml(con, static_env.MAIL_ACCOUNT));
                    xml.append(urlp.aeXmsg.getReplyToXml(con, static_env.MAIL_ACCOUNT));
                    xml.append(urlp.aeXmsg.getEmailsAsXml(con, urlp.ent_ids, urlp.cc_ent_ids, urlp.bcc_ent_ids, static_env.MAIL_ACCOUNT));
                    xml.append(urlp.aeXmsg.getActionTakerXml(con, static_env.MAIL_ACCOUNT));

                    out.println((xml.toString()).trim());
                    CommonLog.debug((xml.toString()).trim());
        
                }catch( Exception e ) {
                	CommonLog.error(e.getMessage(),e);
                }
                CommonLog.debug("########## END ##########");
                return;
            } 
            /*else if (urlp.cmd.equalsIgnoreCase("COURSE_NOTIFY")) {
                System.out.println("########## COURSE_NOTIFY ##########");
                try {
                    urlp.course_notify(clientEnc, static_env.ENCODING);
                    StringBuffer xml = CourseNotifyScheduler.getCourseNotifyEmailAsXML(con, urlp.aeXmsg.app_id);
                    out.println((xml.toString()).trim());
                } catch (Exception e) {
                    out.println("FAILED");
                    System.err.println(e.getMessage());
                }
                System.out.println("########## END ##########");
                return;
            }*/
            //get message
            else if ( urlp.cmd.equalsIgnoreCase("NOTIFY_XML") ) {
                try{
                    urlp.notify(clientEnc, static_env.ENCODING);
                    StringBuffer xml = new StringBuffer();

                    xml.append(urlp.aeXmsg.getSenderXml(con, static_env.MAIL_ACCOUNT));
                    xml.append(urlp.aeXmsg.getReplyToXml(con, static_env.MAIL_ACCOUNT));
                    xml.append(urlp.aeXmsg.getEmailsAsXml(con, urlp.ent_ids, urlp.cc_ent_ids, urlp.bcc_ent_ids, static_env.MAIL_ACCOUNT));
                    xml.append(urlp.aeXmsg.getActionTakerXml(con, static_env.MAIL_ACCOUNT));
                    if (urlp.aeXmsg.app_id > 0) {
                        xml.append(urlp.aeXmsg.getApplicationXml(con));
                    }
                    if (urlp.aeXmsg.ent_id > 0) {
                        xml.append(urlp.aeXmsg.getEntXml(con));
                    }

                    if( urlp.aeXmsg.id_type != null && urlp.aeXmsg.id > 0 ) {
                        if( urlp.aeXmsg.id_type.equalsIgnoreCase("ITEM") || urlp.aeXmsg.id_type.equalsIgnoreCase("COURSE")) {
                            //xml.append(urlp.aeXmsg.getItemXml(con));
                            aeItem itm = new aeItem();
                            itm.itm_id = urlp.aeXmsg.id;
                            itm.get(con);
                            itm.setQRInd(con, prof);
                            xml.append(itm.ItemDetailByRunAsXML(con, static_env, false, false, null, 0, false, false, true));
                        } else if( urlp.aeXmsg.id_type.equalsIgnoreCase("RESOURCE") ) {
                            xml.append(urlp.aeXmsg.getReourceXml(con));
                        }
                    }
                    out.println((xml.toString()).trim());
                    
                }catch( Exception e ) {
                    out.println("FAILED");
                    System.err.println(e.getMessage());
                }
                return;

            } else if ( urlp.cmd.equalsIgnoreCase("LINK_NOTIFY_XML") ) {
                try{
                    urlp.notify(clientEnc, static_env.ENCODING);
                    StringBuffer xml = new StringBuffer();
                    xml.append(urlp.aeXmsg.getSenderXml(con, static_env.MAIL_ACCOUNT));
                    if (urlp.url_link_ind == false)  {
                        xml.append(urlp.aeXmsg.getEmailsAsXml(con, urlp.ent_ids, urlp.cc_ent_ids, null, static_env.MAIL_ACCOUNT));
                    }else if( urlp.ent_ids != null && urlp.ent_ids.length() > 0 ) {
                        xml.append(urlp.aeXmsg.getRecipientXml(con, Long.parseLong(urlp.ent_ids), static_env.MAIL_ACCOUNT, static_env.DES_KEY));
                        xml.append(urlp.aeXmsg.getEmailsAsXml(con, null, urlp.cc_ent_ids, null, static_env.MAIL_ACCOUNT));
                    }

                    if( urlp.aeXmsg.id_type != null && urlp.aeXmsg.id > 0 ) {
                        if( urlp.aeXmsg.id_type.equalsIgnoreCase("ITEM") || urlp.aeXmsg.id_type.equalsIgnoreCase("COURSE")) {
//                            xml.append(urlp.aeXmsg.getItemXml(con));
                            aeItem itm = new aeItem();
                            itm.itm_id = urlp.aeXmsg.id;
                            itm.get(con);
                            itm.setQRInd(con, prof);
                            xml.append(itm.ItemDetailByRunAsXML(con, static_env, false, false, null, 0, false, false, true));
                            //xml.append(
                        } else if( urlp.aeXmsg.id_type.equalsIgnoreCase("RESOURCE") )
                            xml.append(urlp.aeXmsg.getReourceXml(con));
                    }

                    // added to get the application, by Emily, 2002-10-13
                    if (urlp.aeXmsg.app_id > 0) {
                        aeApplication application = new aeApplication();
                        application.app_id = urlp.aeXmsg.app_id;
                        application.get(con);
                        xml.append(application.contentAsXML(con, sess, false, false, false));
                    }

                    if (urlp.show_prereq_ind || urlp.aeXmsg.app_id > 0) {
                        // added to get the item requirement by Chris, 2003-01-29
                        aeItemRequirement aeItmReq = new aeItemRequirement();
                        xml.append("<item_req>");
                        xml.append(aeItmReq.asXML(con, urlp.aeXmsg.id));
                        xml.append("</item_req>");
                    }

                    out.println((xml.toString()).trim());

                }catch( Exception e ) {
                    System.err.println(e.getMessage());
                    out.println("FAILED");
                }
                return;
            }

            else if(urlp.cmd.equalsIgnoreCase("ae_notify_xml")) {
                try{
                    //aeXMessage aeXmsg = new aeXMessage();
                    urlp.get_notify(clientEnc, static_env.ENCODING);
                    StringBuffer xml = new StringBuffer();
                    xml.append(urlp.aeXmsg.getRecipientXml(con, static_env.MAIL_ACCOUNT, static_env.DES_KEY));
                    xml.append(urlp.aeXmsg.getSenderXml(con, static_env.MAIL_ACCOUNT));
                    urlp.itm.get(con);
                    urlp.itm.setQRInd(con, prof);
                    xml.append(urlp.itm.ItemDetailByRunAsXML(con,static_env, false, false, urlp.tvw_id, 0, false,false, urlp.show_session_ind));
                    //xml.append(boolean)
                   // getChildItemAsXML(Connection con, boolean checkStatus, boolean show_attendance_ind, boolean xmlInDetails, cwPagination page, String boundListXMLTag)
                    out.println(xml.toString());
                }catch( Exception e ) {
                    System.err.println("Failed to get message : " + e.getMessage() );
                    out.println("FAILED");
                }
                return;
            }
            else if(urlp.cmd.equalsIgnoreCase("sys_performance_notify_xml")) {
                try{
                	urlp.get_sys_performance_notify(clientEnc, static_env.ENCODING);
                    out.println(CheckActiveUserScheduler.getQuotaExceedNotifyXML(con, urlp.sender_id, urlp.active_user, urlp.warning_user, urlp.blocking_user, urlp.gen_time));
                }catch( Exception e ) {
                    System.err.println("Failed to get message xml : " + e.getMessage() );
                    out.println("FAILED");
                }
                return;
            } else if(urlp.cmd.equalsIgnoreCase("quota_exceed_notify_xml")) {
                try{
                	urlp.get_quota_exceed_notify(clientEnc, static_env.ENCODING);
                    out.println(loadTargetLrnCacheAndCourseEnrollScheduler.getQuotaExceedNotifyXML(con, urlp.sender_id, urlp.rec_ent_ids, urlp.itm, urlp.appn_wait_count));
                }catch( Exception e ) {
                    System.err.println("Failed to get message xml : " + e.getMessage() );
                    out.println("FAILED");
                }
                return;
            } else if(urlp.cmd.equalsIgnoreCase("ji_notify_xml")) {
            	CommonLog.debug("ji_notify_xml" );
                try{
                    //aeXMessage aeXmsg = new aeXMessage();
                    urlp.ji_get_notify(clientEnc, static_env.ENCODING);
                    StringBuffer xml = new StringBuffer();
                    xml.append(urlp.aeXmsg.getSenderXml(con, static_env.MAIL_ACCOUNT));
                    Vector ccEntIdVec = new Vector();
                    long[] ent_ids;
                    long[] bcc_ent_ids;
                    if( urlp.ent_ids == null || urlp.ent_ids.length() == 0 )
                        ent_ids = new long[0];
                    else
                        ent_ids = dbUtils.string2LongArray(urlp.ent_ids, "~");

                     String cc_to_approver_rol_ext_id[];
                     CommonLog.debug("urlp.cc_to_approver_ind" + urlp.cc_to_approver_ind);
                     if(urlp.cc_to_approver_ind){
                            //System.out.println("urlp.cc_to_approver_rol_ext_id.length" + urlp.cc_to_approver_rol_ext_id.length);
                            if(urlp.cc_to_approver_rol_ext_id == null || urlp.cc_to_approver_rol_ext_id.length == 0 ||urlp.cc_to_approver_rol_ext_id[0].equals("null")){
                                cc_to_approver_rol_ext_id = dbRegUser.getApproverRolExtId(con);
                            }
                            else
                                cc_to_approver_rol_ext_id = urlp.cc_to_approver_rol_ext_id;
                        }

                    long[] cc_ent_ids = new long[ccEntIdVec.size()];
                    for(int i=0; i<cc_ent_ids.length; i++){
                        cc_ent_ids[i] = ((Long)ccEntIdVec.elementAt(i)).longValue();
                        CommonLog.debug("cc_ent_ids[i] " + cc_ent_ids[i]);
                    }

                    CommonLog.debug(urlp.bcc_ent_ids);
                    if( urlp.bcc_ent_ids == null || urlp.bcc_ent_ids.length() == 0 || urlp.bcc_ent_ids.equals("null"))
                        bcc_ent_ids = new long[0];
                    else
                        bcc_ent_ids = dbUtils.string2LongArray(urlp.bcc_ent_ids, "~");

                    xml.append(urlp.aeXmsg.getEmails(con, ent_ids, cc_ent_ids, bcc_ent_ids, static_env.MAIL_ACCOUNT));
                    urlp.itm.get(con);
                    urlp.itm.setQRInd(con, prof);
                    if(urlp.show_session_ind == false){

                        urlp.show_session_ind = true;
                    }
                    xml.append(urlp.itm.JIItemDetailByRunAsXML(con,static_env, false, false, urlp.tvw_id, 0, false,false, urlp.show_session_ind));
                    out.println(xml.toString());

                }catch( Exception e ) {
                    CommonLog.error(e.getMessage(),e);
                }
                return;
            }
            // function called by bank
            else if(urlp.cmd.equalsIgnoreCase("commit_online_payment") || urlp.cmd.equalsIgnoreCase("commit_online_payment_xml")) {
                urlp.onlinePayment(clientEnc, static_env.ENCODING);
                aeAccountTransaction axn = new aeAccountTransaction();
                StringBuffer privateKey = new StringBuffer();
                privateKey.append("Succeed=");
                if(urlp.Succeed)
                    privateKey.append("Y&");
                else
                    privateKey.append("N&");
                privateKey.append("BillNo=").append(urlp.BillNo);
                privateKey.append("&Amount=").append(urlp.Amount);
                privateKey.append("&Date=").append(urlp.Date);
                privateKey.append("&Msg=").append(urlp.Msg);
                privateKey.append("&Signature=").append(urlp.Signature);
                //System.out.println(privateKey.toString());
                //Convert Msg to unicode
                //System.out.println("MSG : " + urlp.Msg);
                //System.out.println("ClientEnc : " + clientEnc);
                //System.out.println("static_env : " + static_env.ENCODING);
                urlp.Msg = dbUtils.unicodeFrom(urlp.Msg,clientEnc, static_env.ENCODING);
                //System.out.println("MSG : " + urlp.Msg);
                byte[] baMsg = (privateKey.toString()).getBytes(defEncoding_);
                boolean bRet = false;

                if(static_env.CHECK_KEY) {
                    try {
                        //System.out.println(static_env.PUBLICKEY_PATH + "\\public.key");
// commented as CMB is not longer used (2003-11-19 kawai)
//                        cmb.netpayment.Security pay = new cmb.netpayment.Security((static_env.PUBLICKEY_PATH).getAbsolutePath());
//                        bRet = pay.checkInfoFromBank(baMsg);
                    }catch(Exception e) {
                        System.err.println("testPay exception: "+e.getMessage());
                    }

                    if(!bRet)
                        throw new cwException("Error : private key not match.");
                }

                if(urlp.Succeed) {
                    StringBuffer paymentStringBuffer = new StringBuffer();
                    axn.getEntId(con,urlp.axn_id);

                    paymentStringBuffer.append("<cur_usr ent_id=\"").append(axn.ent_id).append("\" ");
                    paymentStringBuffer.append("host=\"").append("http://" + request.getServerName() + request.getServletPath()).append("\" ");
                    paymentStringBuffer.append("encoding=\"").append(static_env.ENCODING).append("\" ");
                    paymentStringBuffer.append("style=\"").append(static_env.STYLE).append("\" ");
                    paymentStringBuffer.append("label=\"").append(axn.label_lan).append("\" />").append(dbUtils.NEWL);

                    axn.updateOnlinePayment(con,urlp.axn_id,aeReqParam.to2decPt(urlp.Amount),urlp.Msg,paymentStringBuffer,true);
                    StringBuffer result = new StringBuffer(formatXML(paymentStringBuffer.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("commit_online_payment_xml"))
                        out.println(result.toString());
                    else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result.toString(), out, urlp.stylesheet_success,xsl_root);
                    }
                }else {
                    //axn.updateOnlinePayment(con,urlp.olp_axn_id,result,false);

                    StringBuffer payment_result = new StringBuffer();
                    axn.getEntId(con,urlp.axn_id);

                    payment_result.append("<cur_usr ent_id=\"").append(axn.ent_id).append("\" ");
                    payment_result.append("encoding=\"").append(static_env.ENCODING).append("\" ");
                    payment_result.append("style=\"").append(static_env.STYLE).append("\" ");
                    payment_result.append("label=\"").append(axn.label_lan).append("\" />").append(dbUtils.NEWL);

                    payment_result.append("<transaction_failure msg=\"").append(urlp.Msg).append("\"/>").append(dbUtils.NEWL);
                    StringBuffer result = new StringBuffer(formatXML(payment_result.toString(),prof));
                    aeAccountTransaction.updateTransactionDesc(con,urlp.axn_id,urlp.Msg);
                    if(urlp.cmd.equalsIgnoreCase("commit_online_payment_xml"))
                        out.println(result.toString());
                    else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result.toString(), out, urlp.stylesheet_fail,xsl_root);
                    }
                }
                //System.out.println("Commit online Payment");
                con.commit();
            }

            else if(sess == null) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            }
            else {
                //prof = (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
                //ClassCastException will be thrown if a new class loader is used
                //e.g. when a JSP is first loaded, a new class loader will be used
                //so use a wrapper class to read the loginProfile and put it back to session
                try {
                    prof = (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
                }
                catch(ClassCastException e) {
                    prof = new loginProfile();
                    prof.readSession(sess);
                    sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
                }

                if (prof ==null || prof.usr_id == null) {
                    response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
                    return;
                }

                //if( cwUtils.checkUserRoleSkin(request, response, "/" + prof.skin_root + "/" + prof.current_role_skin_root, prof.role_url_home) )
                //    return;


                // Not allow multiple login and session login time != database login time
                else if (!Application.MULTIPLE_LOGIN &&
                     !prof.login_date.equals(dbRegUser.getLastLoginDate(con, prof.usr_id))) {
                    sess.invalidate();
                    request.setAttribute("sitemesh_parameter", "excludes");
                    cwSysMessage e = new cwSysMessage(dbRegUser.MSG_MULTI_LOGIN);
                    msgBox(MSG_ERROR, con, e, prof, static_env.URL_RELOGIN, out);
                    return;
                }
                else if (urlp.cmd.equals("ae_get_cat_lst") || urlp.cmd.equals("ae_get_cat_lst_xml")) {
                	urlp.pagination();
                	urlp.catalog(clientEnc, static_env.ENCODING);
                    boolean checkStatus;
                    long[] usr_ent_ids = {0};

                    if(prof == null) {
                        checkStatus = true;
                    }
                    else {
                        //if( cwUtils.checkUserRoleSkin(request, response, "/" + prof.skin_root + "/" + prof.current_role_skin_root, prof.role_url_home) )
                        //    return;
                        usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        checkStatus = false;
                    }

                    //no access control
                    String filter_type = null;
                    if (wizbini.cfgTcEnabled) {
                    	if(AccessControlWZB.isLrnRole(prof.current_role)) {
                    		filter_type = "lrn_filter";
                    	} else {
                    		filter_type = "admin_filter";
                    		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
                        		//if admin has no effect training center. throw a system message
                            	if (!ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id)) {
                                    cwSysMessage e = new cwSysMessage("TC008");
                                    msgBox(MSG_ERROR, con, e, prof, urlp.url_success, out);
                                    return;
                            	}
                    		}else{
//                    			if(wizbini.cfgSysSetupadv.isTcIndependent()){
//                    				filter_type = "lrn_filter";
//                    			}else{
//                    				filter_type = "admin_filter";
//                    			}
                    		}
                    	}
                    	
                    }
                    
                    StringBuffer resultBuf = new StringBuffer(aeCatalog.catalogListAsXML(con, usr_ent_ids, checkStatus, urlp.cat.cat_tcr_id, urlp.cwPage.curPage, 
                    											urlp.cwPage.pageSize, urlp.cwPage.sortCol, urlp.cwPage.sortOrder, wizbini.cfgTcEnabled, 
                    											filter_type, prof,  wizbini));
                    String metaXML = null;
                    if(prof != null) {
                        //Page Variant Object to answer Xsl Questions
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    }
                  	cwTree tree = new cwTree();
                    resultBuf.append(tree.genNavTrainingCenterTree(con, prof, false));
                    String result = formatXML(resultBuf.toString(),metaXML, APPLYEASY, prof);

                    if (urlp.cmd.equals("ae_get_cat_lst_xml")) {
                        out.println(result);
                    } else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result, out, urlp.stylesheet, xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_get_glb_cat_lst") || urlp.cmd.equals("ae_get_glb_cat_lst_xml")) {
                    boolean checkStatus;

                    if(prof == null) {
                        checkStatus = true;
                    }
                    else {
                        checkStatus = false;
                    }

                    //no access control
                    StringBuffer resultBuf = new StringBuffer(aeCatalog.globalCatalogListAsXML(con, checkStatus));
                    String metaXML = null;
                    if(prof != null) {
                        //Page Variant Object to answer Xsl Questions
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    }
                    String result = formatXML(resultBuf.toString(),metaXML, APPLYEASY, prof);

                    if (urlp.cmd.equals("ae_get_glb_cat_lst_xml")) {
                        out.println(result);
                    } else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result, out, urlp.stylesheet, xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_get_tnd_cnt_lst") || urlp.cmd.equals("ae_get_tnd_cnt_lst_xml")) {
                    boolean checkStatusCat;
                    boolean checkStatusItm;
                    boolean isUsrLrnRole;
                    long[] usr_ent_ids = {0};
                    AcTreeNode actnd = new AcTreeNode(con);
                    AcItem acitm = new AcItem(con);
                    Vector itmLst = null;
                    long cat_tcr_id = 0;
                    if(request.getParameter("cat_tcr_id") != null){
                    	cat_tcr_id = Long.parseLong(request.getParameter("cat_tcr_id"));
                    }
                    if(cat_tcr_id < 0){
                    	cat_tcr_id = 0;
                    }
                    if(prof == null) {
                        checkStatusCat = true;
                        checkStatusItm = true;
                        isUsrLrnRole = true;
                    }
                    else {
                        //if( cwUtils.checkUserRoleSkin(request, response, "/" + prof.skin_root + "/" + prof.current_role_skin_root, prof.role_url_home) )
                        //    return;

                        usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/

                        checkStatusCat = false;
                        checkStatusItm = false;
						isUsrLrnRole = dbUtils.isUserLrnRole(con, prof.usr_ent_id, prof.current_role);
						
						if (wizbini.cfgSysSetupadv.getLearningSolution().isCatalogShowOnlyTargetedLs()&& isUsrLrnRole){
                            dbRegUser usr = new dbRegUser();
                            usr.usr_ent_id = prof.usr_ent_id;
                            itmLst = usr.getTargetedItmLst(con);
                        }
                    }

                    urlp.treenode(clientEnc, static_env.ENCODING);
                    urlp.pagination();

                    //for sso check
                    urlp.getSsoParam();
                    
                    aeTreeNode tnd = urlp.tnd;
                    
                    //access control
                    tnd.tnd_cat_id = tnd.getCatalogId(con);
                    if(prof != null) {
                        if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role,
                                                    tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
                            throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                        }
                    }
                    if (wizbini.cfgTcEnabled && AccessControlWZB.isRoleTcInd(prof.current_role) ){
                        if (prof.isLrnRole) {
                        	if (!aeCatalog.canReadByLrn(con, tnd.tnd_cat_id, prof.root_ent_id, prof.usr_ent_id,  wizbini)) {
                                throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                        	}
                        } else if (!aeCatalog.canEditByTcadmin(con, tnd.tnd_cat_id, prof.usr_ent_id)) {
                            throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                        }
                    }

					tnd.get(con);
					long owner_ent_id = (prof == null) ? 0 : prof.root_ent_id;
                    StringBuffer resultBuf = new StringBuffer(tnd.getNodeAndChildrenAsXML(con, owner_ent_id, usr_ent_ids, checkStatusCat, checkStatusItm, itmLst, urlp.list, urlp.cwPage, prof));
                    resultBuf.append(dbSSOLink.ssoLinkAsXML(prof.root_id, wizbini));
                    resultBuf.append("<cat_tcr_id>").append(cat_tcr_id).append("</cat_tcr_id>");
                    String metaXML = null;
                    if(prof != null) {
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        acPageVariant.instance_id = tnd.tnd_cat_id;
                        metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    }
                    String result = formatXML(resultBuf.toString(), metaXML, APPLYEASY, prof);
                    if (urlp.cmd.equals("ae_get_tnd_cnt_lst_xml")) {
                        out.println(result);
                    } else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result, out, urlp.stylesheet, xsl_root);
                    }
                }
                else if(urlp.cmd.equals("ae_get_sim_itm") || urlp.cmd.equals("ae_get_sim_itm_xml")) {
                    AcItem acitm = new AcItem(con);
                    urlp.item(clientEnc, static_env.ENCODING);
                    aeItem itm = urlp.itm;
                    itm.get(con);
                    itm.setQRInd(con, prof);
                    //access control on item
                    if(!acitm.hasReadPrivilege(itm.itm_id, prof.usr_ent_id, prof.current_role)){
                        throw new cwSysMessage(aeItem.ITM_OFFLINE_MSG);
                    }
                    StringBuffer resultBuf = new StringBuffer(2500);
                    resultBuf.append(itm.itemAttributeAsXML());
                    String result = formatXML(resultBuf.toString(),null, APPLYEASY, prof);

                    if (urlp.cmd.equals("ae_get_sim_itm_xml")) {
                        out.println(result.toString());
                    } else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result.toString(), out, urlp.stylesheet, xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_get_itm") || urlp.cmd.equals("ae_get_itm_xml")) {//获取课程详细信息

					urlp.item(clientEnc, static_env.ENCODING);//获取参数（服务器编码格式，系统配置文件默认编码格式）
					urlp.saveGetItemSubmittedParams(clientEnc, static_env.ENCODING);
                    urlp.getSsoParam();
					
//					boolean isUsrLrnRole = dbUtils.isUserLrnRole(con, prof.usr_ent_id, prof.current_role);
//					boolean hasTargetedLrnPrivilege = aeItem.hasTargetedLrnPrivilege(con, prof.root_ent_id, prof.usr_ent_id, urlp.itm.itm_id);
//					if(isUsrLrnRole && 
//							(!hasTargetedLrnPrivilege
//									&& !aeApplication.hasEffectiveAppId(con, urlp.itm.itm_id, prof.usr_ent_id))){
//						cwSysMessage sms = new cwSysMessage("AEIT28");
//						msgBox(MSG_STATUS, con, sms, prof, urlp.url_failure, out);
//						return;
//					}
//                  
					String metaXML = null;
					if(prof != null) {
						AcPageVariant acPageVariant = new AcPageVariant(con);
						acPageVariant.prof = prof;
						acPageVariant.instance_id = urlp.itm.itm_id;
						acPageVariant.ent_owner_ent_id = prof.root_ent_id;
						acPageVariant.ent_id = prof.usr_ent_id;
						acPageVariant.rol_ext_id = prof.current_role;
						if(AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN)){
							acPageVariant.rol_ext_id = AcObjective.TADM;
						}
						acPageVariant.root_id = prof.root_id;
						acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
						acPageVariant.setWizbiniLoader(wizbini);

						metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
					}
                    
                    //add sso xml for sso query
                    metaXML += dbSSOLink.ssoLinkAsXML(prof.root_id, wizbini);
					urlp.itm.auto_enroll_interval = loadTargetLrnCacheAndCourseEnrollScheduler.Auto_Enroll_Interval;
					String result = getItemXML(con, urlp, prof) + aeItem.genItemActionNavXML(con, urlp.itm.itm_id, prof);
					result = result.replaceAll("&#[0-9]*;", "");//处理乱码导致的页码不显示
					result = formatXML(result, metaXML, APPLYEASY, prof, urlp.stylesheet);
				//	System.out.println("<pre>"+result+"</pre>");
                    if (urlp.cmd.equals("ae_get_itm_xml")) {
                    	
                    	//result = result.replace("<", "&lt;");
                    	//result = result.replace(">", "&gt;");

                        out.println(result);
                    } else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result, out, urlp.stylesheet, xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_get_itm_comment_lst") || urlp.cmd.equals("ae_get_itm_comment_lst_xml")) {
					urlp.item(clientEnc, static_env.ENCODING);
					
					// aeItem item = new aeItem();
					aeItem itm = urlp.itm;
					
					//String metaXML = itm.getItemCostAttribute(wizbini, prof.root_id);
					
					String mainXml = aeItem.getItemCommentLstPageXML(con, urlp);
					
					String result = formatXML(mainXml, "", APPLYEASY, prof, urlp.stylesheet);
					
					if (urlp.cmd.equals("ae_get_itm_comment_lst_xml")) {
						out.println(result);
					} else {
						String xsl_root = (prof == null) ? null : prof.xsl_root;
						generalAsHtml(result, out, urlp.stylesheet, xsl_root);
					}
				}
                else if (urlp.cmd.equals("ae_del_sns")) {
                    urlp.getSnsParam();
                    Course cos = new Course();
                    //cos.delCosComment(con, urlp.sns_type, urlp.s_cmt_id, urlp.ent_id);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
				else if (urlp.cmd.equals("ae_get_itm_cost") || urlp.cmd.equals("ae_get_itm_cost_xml")) {
					urlp.item(clientEnc, static_env.ENCODING);
					aeItemCost itemCost = new aeItemCost();
					//aeItem item = new aeItem();
					aeItem item1 = urlp.itm;
					String metaXML = item1.getItemCostAttribute(wizbini,prof.root_id);
					String mainXml = itemCost.getItemCostPageXml(con,item1);
					mainXml += aeItem.genItemActionNavXML(con, urlp.itm.itm_id, prof);
					String result = formatXML(mainXml,metaXML,APPLYEASY,prof,urlp.stylesheet);
					if(urlp.cmd.equals("ae_get_itm_cost_xml")){
						out.println(result);
					}else{
						String xsl_root = (prof == null) ? null:prof.xsl_root;
						generalAsHtml(result, out, urlp.stylesheet, xsl_root);
					}
				}
				else if(urlp.cmd.equals("commit_item_cost") || urlp.cmd.equals("commit_item_cost_xml")){
					aeItemCost itemCost = new aeItemCost();
					List itemCosts = urlp.getParamWithItemCost();
					urlp.item(clientEnc, static_env.ENCODING);
					aeItem item = urlp.itm;
					boolean flag = itemCost.commitCostData(con,itemCosts,item,prof);
					con.commit();
					if(!flag){
						//other body have changed the data
						cwSysMessage sms = new cwSysMessage(SMSG_CHANGED_MSG);
						msgBox(MSG_STATUS, con, sms, prof, urlp.url_failure, out);
					}else{
						cwSysMessage sms = new cwSysMessage(SMSG_UPD_MSG);
						msgBox(MSG_STATUS, con, sms, prof, urlp.url_success, out);
					}
				}
                else if (urlp.cmd.equals("ae_get_itm_preview") || urlp.cmd.equals("ae_get_itm_preview_xml")) {

                    boolean checkStatus;
                    boolean cos_mgt_ind;
                    long[] usr_ent_ids = {0};
                    AcCourse accos = new AcCourse(con);
                    AcItem acitm = new AcItem(con);
                    urlp.item(clientEnc, static_env.ENCODING);
                    if (urlp.get_last == 1) {
                        urlp.itm.itm_id = aeItem.getLastVersion(con, urlp.itm.itm_id);
                    }
                    aeItem itm = urlp.itm;
                    itm.get(con, urlp.tnd_id);
                    itm.setQRInd(con, prof);

                    if(prof == null) {
                        checkStatus = true;
                        cos_mgt_ind = false;
                    }
                    else {
                        usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                        checkStatus = false;
                        cos_mgt_ind = AccessControlWZB.hasRolePrivilege( prof.current_role, new String [] {AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN});
                    }
                    if(prof != null) {
                        if(urlp.tnd_id != 0) {
                            //access control on treenode
                            AcTreeNode actnd = new AcTreeNode(con);
                            aeTreeNode tnd = new aeTreeNode();
                            tnd.tnd_id = urlp.tnd_id;
                            tnd.tnd_cat_id = tnd.getCatalogId(con);
                            if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role,
                                                        tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
                                throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                            }
                        }
                        //access control on item
                        if(!acitm.hasReadPrivilege(itm, prof.usr_ent_id, prof.current_role, checkStatus)){
                            throw new cwSysMessage(aeItem.ITM_OFFLINE_MSG);
                        }
                    }

                    StringBuffer resultBuf = new StringBuffer(2500);
                    String metaXML = null;
                    long usr_ent_id = (prof == null) ? 0 : prof.usr_ent_id;
                    resultBuf.append(itm.ItemPreviewAsXML(con));
                    if(prof != null) {
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.prof = prof;
                        acPageVariant.instance_id = itm.itm_id;
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    }
                    String result = formatXML(resultBuf.toString(),metaXML, APPLYEASY, prof, urlp.stylesheet);

                    if (urlp.cmd.equals("ae_get_itm_preview_xml")) {
                        out.println(result.toString());
                    } else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result.toString(), out, urlp.stylesheet, xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_upd_itm_preview")) {
                    if(sess != null) {
                        try {
                            prof = (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
                        }
                        catch(ClassCastException e) {
                            prof = new loginProfile();
                            prof.readSession(sess);
                            sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
                        }
                    }
                    urlp.item(clientEnc, static_env.ENCODING);
                    urlp.mote(clientEnc, static_env.ENCODING);

                    aeItem itm = urlp.itm;
                    Timestamp itm_upd_timestamp = urlp.itm_in_upd_timestamp;

                    //access control
                    AcItem acitm = new AcItem(con);
                    if(prof != null) {
                        if(!acitm.hasUpdPrivilege(itm.itm_id, prof.usr_ent_id,
                                                    prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }
                    }
                    StringBuffer resultBuf = new StringBuffer(1024);
                    itm.updItemCanQrInd(con, itm_upd_timestamp);

                    con.commit();
                    cwSysMessage e = new cwSysMessage(aeItem.ITM_QR_UPDATE_OK);
                    msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
    //                response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_search_itm") || urlp.cmd.equals("ae_search_itm_xml")) {

                    boolean checkStatusCat;
                    boolean checkStatusItm;
                    long[] usr_ent_ids = {0};

                    if(prof == null) {
                        checkStatusCat = true;
                        checkStatusItm = true;
                    }
                    else {
                        //if( cwUtils.checkUserRoleSkin(request, response, "/" + prof.skin_root + "/" + prof.current_role_skin_root, prof.role_url_home) )
                        //    return;


                        usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        
                        checkStatusCat = (! AccessControlWZB.hasRolePrivilege( prof.current_role, AclFunction.FTN_AMD_CAT_MAIN));
                        AcItem acitm = new AcItem(con);
                        checkStatusItm =(! AccessControlWZB.hasRolePrivilege( prof.current_role, new String [] {AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN}));
                    }

                    urlp.treeNodeSearch(clientEnc, static_env.ENCODING);
                    aeTreeNode treeNode = new aeTreeNode();
                    treeNode.tnd_id = urlp.tnd_id;

                    //if not search for all catalogs, begin with a TreeNode
                    if(!urlp.all_ind)
                        treeNode.get(con);
                        //treeNode.get(con, usr_ent_ids, checkStatus);

					String result = formatXML(treeNode.searchNodeAsXML(con, sess, urlp.search_timestamp, urlp.page, urlp.phrase, urlp.exact, urlp.code, null, urlp.types, urlp.appn_from, urlp.appn_to, urlp.eff_from, urlp.eff_to, urlp.status, prof, urlp.all_ind, checkStatusCat, checkStatusItm,  wizbini), prof);

                    if (urlp.cmd.equals("ae_search_itm_xml")) {
                        out.println(result);
                    } else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result, out, urlp.stylesheet, xsl_root);
                    }
                }

                else if (urlp.cmd.equalsIgnoreCase("ae_get_prof") ||
                        urlp.cmd.equalsIgnoreCase("ae_get_prof_xml")) {

                        urlp.navigator();
                        dbRegUser usr = new dbRegUser();
                        usr.usr_id = prof.usr_id;
                        usr.usr_ent_id = prof.usr_ent_id;
                        usr.get(con);
                        String navXML = "";
                        if(urlp.nav_type != null && urlp.nav_type.length() > 0 && urlp.nav_type.equals(aeTreeNode.NAV_CATALOG)) {
                            aeTreeNode tnd = new aeTreeNode();
                            tnd.tnd_id = urlp.nav_id;
                            navXML = tnd.getNavigatorAsXML(con);
                        }
    //                    String result = usr.asXML(con,prof);
                        String result = usr.insCosPrepXML(con, prof, null, navXML,0,"",0);
                        if(urlp.cmd.equalsIgnoreCase("ae_get_prof_xml"))
                            out.println(result);
                        if(urlp.cmd.equalsIgnoreCase("ae_get_prof")) {
                            generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                        }
                }
                else if (urlp.cmd.equals("ae_pending_items") || urlp.cmd.equals("ae_pending_items_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.pendingItems(con, sess, prof.root_ent_id, urlp.page, urlp.sort_by, urlp.order_by, urlp.history);
                    result = formatXML(result, prof);
                    if (urlp.cmd.equals("ae_pending_items_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_pending_appns") || urlp.cmd.equals("ae_pending_appns_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.pendingApplications(con, sess, prof.root_ent_id, urlp.page, urlp.sort_by, urlp.order_by, urlp.history);
                    result = formatXML(result, prof);

                    if (urlp.cmd.equals("ae_pending_appns_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_process_queue") || urlp.cmd.equals("ae_process_queue_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.processQueue(con, sess, prof.root_ent_id, urlp.itm_id, urlp.queue_type, urlp.page, urlp.sort_by, urlp.order_by);
                    result = formatXML(result, prof);

                    if (urlp.cmd.equals("ae_process_queue_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_notify_status") || urlp.cmd.equals("ae_notify_status_xml")) {
                    //no access control
                    urlp.pagination();
                    urlp.notifyStatus();
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.getNotifyStatus(con, request, prof.root_ent_id, urlp.itm_id, urlp.cwPage /*page_size, urlp.sort_by, urlp.cur_page, urlp.pagetime, urlp.order_by*/);

                    result = formatXML(result, prof);

                    if (urlp.cmd.equals("ae_notify_status_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                } else if( urlp.cmd.equalsIgnoreCase("init_msg") || urlp.cmd.equalsIgnoreCase("init_msg_xml") ) {

                    urlp.notify(clientEnc, static_env.ENCODING);


                    //String[] default_recipient = {"admin"};
                    String xml = urlp.aeXmsg.initMsg(con, urlp.aeXmsg.msg_type, urlp.aeXmsg.id, urlp.aeXmsg.id_type, urlp.ent_id_lst, prof, urlp.target_group, urlp.app_process_status);

                    if( urlp.cmd.equalsIgnoreCase("init_msg_xml") ) {
                        out.println(xml);
                    }
                    else {
                        generalAsHtml(xml, out, urlp.stylesheet, prof.xsl_root);
                    }

                } else if( urlp.cmd.equalsIgnoreCase("INS_XMSG") ) {

                    urlp.notify(clientEnc, static_env.ENCODING);
                   
                    //send message
                    long[] ent_ids = dbUserGroup.constructEntId(con, urlp.ent_ids);
                    long[] cc_ent_ids = dbUserGroup.constructEntId(con, urlp.cc_ent_ids);
                    String cc_email_address = urlp.cc_email_address;

			        //插入邮件及邮件内容
					Timestamp send_time = cwSQL.getTime(con);
			        MessageService msgService = new MessageService();
			        String mtp_type = "EMPTY_TYPE";
			        
					MessageTemplate mtp = new MessageTemplate();
					mtp.setMtp_tcr_id(prof.my_top_tc_id);
					mtp.setMtp_type(mtp_type);
					mtp.getByTcr(con);
					
					mtp.setMtp_subject(urlp.msg_subject);
					String[] contents = {urlp.msg_body, urlp.msg_body, urlp.msg_body, urlp.msg_body};
			        msgService.insMessage(con, mtp, prof.usr_id, ent_ids, cc_ent_ids, send_time, contents, 0, cc_email_address);
			        con.commit();
			        msgBox(MSG_STATUS, con, new cwSysMessage("XMG001"), prof, urlp.url_success, out);
     
                } else if (urlp.cmd.equals("ae_export_queue")) {
                    // no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.exportQueue(con, sess, prof.root_ent_id, urlp.itm_id, urlp.queue_type, urlp.sort_by);

                    response.setHeader("Content-Disposition", "attachment; filename=participants_list.txt");
                    cwUtils.setContentType("text/plain", response, wizbini);
                    out.print(result);
                }
                else if (urlp.cmd.equals("ae_process_appn") || urlp.cmd.equals("ae_process_appn_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.processApplication(con, sess, urlp.app_id, urlp.tvw_id, urlp.app_tvw_id, static_env, prof.root_ent_id, prof);
                    result = formatXML(result, prof, urlp.stylesheet);

                    if (urlp.cmd.equals("ae_process_appn_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_get_appn_form") || urlp.cmd.equals("ae_get_appn_form_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    String result;
                    aeQueueManager qm = new aeQueueManager();
                    if(urlp.ent_id == 0) {
                        urlp.ent_id = prof.usr_ent_id;
                    }
                    result = qm.getApplicationForm(con, sess, urlp.itm_id, urlp.ent_id, prof, static_env, urlp.tnd_id, urlp.tvw_id, urlp.app_tvw_id);

                    //Page Variant Object to answer Xsl Questions
                    AcPageVariant acPageVariant = new AcPageVariant(con);
                    acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                    acPageVariant.ent_id = prof.usr_ent_id;
                    acPageVariant.rol_ext_id = prof.current_role;
                    String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));

                    result = formatXML(result, metaXML, APPLYEASY, prof);

                    if (urlp.cmd.equals("ae_get_appn_form_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_confirm_appn") || urlp.cmd.equals("ae_confirm_appn_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    //long ent_id = dbRegUser.getEntId(con, prof.usr_id);
                    if(urlp.ent_id == 0) {
                        urlp.ent_id = prof.usr_ent_id;
                    }
                    String result = qm.confirmApplication(con, sess, urlp.itm_id, urlp.ent_id, urlp.app_xml, prof, static_env, urlp.tnd_id, urlp.tvw_id, urlp.app_tvw_id);

                    //Page Variant Object to answer Xsl Questions
                    AcPageVariant acPageVariant = new AcPageVariant(con);
                    acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                    acPageVariant.ent_id = prof.usr_ent_id;
                    acPageVariant.rol_ext_id = prof.current_role;
                    String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    result = formatXML(result, metaXML, APPLYEASY, prof);

                    if (urlp.cmd.equals("ae_confirm_appn_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_view_appn") || urlp.cmd.equals("ae_view_appn_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.viewApplication(con, urlp.app_id, static_env, urlp.tvw_id, urlp.app_tvw_id);
                    result = formatXML(result, prof);

                    if (urlp.cmd.equals("ae_view_appn_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_ins_appn") || urlp.cmd.equals("ae_ins_appn_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    //long ent_id = dbRegUser.getEntId(con, prof.usr_id);
                    qm.insApplication(con, sess, urlp.ent_id, urlp.itm_id, prof.usr_id, urlp.tnd_id, prof);
//                    aeLearningSoln.insSoln(con, prof, urlp.ent_id, urlp.itm_id, 0);


                    //Update Resources Permission in the session
                    String status = aeApplication.getUserItemStatus(con, urlp.itm_id, urlp.ent_id);
//System.out.println("Status = " + status);
                    if( status != null && status.equalsIgnoreCase(aeApplication.ADMITTED) &&
                        urlp.ent_id == prof.usr_ent_id ){ //Status is ADMITTED and Self Enrol
                        Vector modIdVec = new Vector();
                        Vector assIdVec = new Vector();
                        dbCourse.getAllEnrolledModIds(con, prof, modIdVec, assIdVec);
                        Vector queIdVec = dbProgressAttempt.getAttemptedResIds(con, prof.usr_id);
//System.out.println("modIdVec = " + modIdVec);
//System.out.println("assIdVec = " + assIdVec);
//System.out.println("queIdVec = " + queIdVec);
                        if (modIdVec != null)
                            sess.setAttribute(qdbAction.AUTH_MOD_IDS, modIdVec);
                        if (assIdVec != null)
                            sess.setAttribute(qdbAction.AUTH_ASS_IDS, assIdVec);
                        if (queIdVec != null)
                            sess.setAttribute(qdbAction.AUTH_QUE_IDS, queIdVec);
                    }


                    con.commit();
                    cwSysMessage e = new cwSysMessage(aeQueueManager.QM_APPN_SUCCESS);
                    msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                    /*response.sendRedirect(urlp.url_success);*/
                    /*result = formatXML(result, prof);*/

                    /*
                    if (urlp.cmd.equals("ae_ins_appn_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                    */
                }
                else if (urlp.cmd.equals("ae_direct_ins_appn")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    //qm.insApplication(con, sess, urlp.ent_id, urlp.itm_id, prof.usr_id, urlp.tnd_id, prof);
                    if(urlp.ent_id == 0) {
                        urlp.ent_id = prof.usr_ent_id;
                    }
                    /*AcQueueManager acqm = new AcQueueManager(con);
					if(qm.checkAppnConflictAsXML(con, urlp.itm_id , urlp.ent_id, false) != null) {
						//System.out.println(acqm.hasAdminPrivilege(prof.usr_ent_id, prof.current_role));
                   		if (!acqm.hasAdminPrivilege(prof.usr_ent_id, prof.current_role)){
                   			throw new cwSysMessage(SMSG_CONFLICT_MSG);
                   		}
                    }*/            
                    aeItem item = new aeItem();
                    item.itm_id = urlp.itm_id;
                    item.getItem(con);
                    int enrol_cnt = aeApplication.countItemAppn(con, item.itm_id, false);
                    if(item.itm_not_allow_waitlist_ind && (item.itm_capacity <= enrol_cnt)) {
                    	throw new cwSysMessage("AEQM11");
                    }
					AcQueueManager acqm = new AcQueueManager(con);
					String conflict = null;
					if ((conflict = qm.checkAppnConflictAsXML(con, urlp.itm_id , urlp.ent_id, true)) != null) {
						if (!acqm.hasAdminPrivilege(prof.usr_ent_id, prof.current_role)){
							conflict += "<learner usr_ent_id=\"" + urlp.ent_id + "\"/>";
							String result = formatXML(conflict, null, APPLYEASY, prof);
							generalAsHtml(result, out, "appn_conflict.xsl", prof.xsl_root);
							return;
						}
					}
					aeApplication aeApp = null;
	                if(item.itm_app_approval_type == null || item.itm_app_approval_type.length() == 0) {
						aeApp = qm.insAppNoWorkflow(con, urlp.app_xml, urlp.ent_id, urlp.itm_id, urlp.comment, null, prof, item);
					} else{
						aeApp = qm.insApplication(con, urlp.app_xml, urlp.ent_id, urlp.itm_id, prof, 0, 0, 0, 0, null, null, true, 0, urlp.comment, null);
					}
                    if( urlp.entIdRole != null ) {
                        aeAppnTargetEntity aeAte = new aeAppnTargetEntity();
                        aeAte.ins(con, aeApp.app_id, urlp.entIdRole, prof);
                    }

                    con.commit();
                    if(urlp.from_core5){
	                    cwSysMessage e = new cwSysMessage(aeQueueManager.QM_APPN_SUCCESS);
	                    msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                    } else {
	                    urlp.url_success = cwUtils.substituteURL(urlp.url_success, aeApp.app_id);
                    	response.sendRedirect(urlp.url_success);
                    }
                }
                else if (urlp.cmd.equals("ae_ins_actn")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    qm.insAppnAction(con, sess, prof.usr_id, urlp.app_id, urlp.process_id, urlp.fr, urlp.to,
                                    urlp.action_verb, urlp.action_id, urlp.status_id,
                                    prof.usr_ent_id, prof.common_role_id);
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_ins_multi_actn")
                        || urlp.cmd.equals("ae_ins_multi_actn_xml") ) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.insMultiAppnAction(con,
                                                          sess,
                                                          prof.usr_id,
                                                          urlp.app_id_lst,
                                                          urlp.process_id,
                                                          urlp.fr,
                                                          urlp.to,
                                                          urlp.action_verb,
                                                          urlp.action_id,
                                                          urlp.status_id, prof);

                    result = formatXML(result, prof);
                    if (urlp.cmd.equals("ae_ins_multi_actn_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_ins_multi_appn")
                        || urlp.cmd.equals("ae_ins_multi_appn_xml") ) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    //application conflict
                    
                    aeQueueManager qm = new aeQueueManager();
					if(urlp.ent_id == 0) {
						urlp.ent_id = prof.usr_ent_id;
					}
                    if (urlp.auto_enroll_ind.equalsIgnoreCase("false"))
						qm.auto_enroll_ind = false;
					
					
					AcQueueManager acqm = new AcQueueManager(con);	
									
					String conflict = null;
					if ((urlp.back_confirm == null) || (!urlp.back_confirm.equalsIgnoreCase("true"))) 
						if ((conflict = qm.checkAppnConflictAsXML(con, urlp.itm_id , urlp.ent_id_lst[0], true)) != null) {
							String result = formatXML(conflict, null, APPLYEASY, prof);
							generalAsHtml(result, out, "appn_conflict.xsl", prof.xsl_root);
							return;
						}
                    //Connection con, long itm_id, long[] ent_id_lst, loginProfile prof
                    qm.insMultiAppn(con, urlp.itm_id, urlp.ent_id_lst, prof);
                    con.commit();
                 
                    if(urlp.url_success != null && !"".equalsIgnoreCase(urlp.url_success)){
                    	response.sendRedirect(urlp.url_success);
                    }
                }
                //for appn_conflict
				else if (urlp.cmd.equals("appn_conflict")
						|| urlp.cmd.equals("appn_conflict_xml") ) {
					//no access control
					if (urlp.back_confirm == null){
						urlp.queueManagement(clientEnc, static_env.ENCODING);
						StringBuffer xmlBuf = new StringBuffer(2500);
						xmlBuf.append("<application_conflict>").append(dbUtils.NEWL);
						xmlBuf.append("</application_conflict>").append(dbUtils.NEWL);
						StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
						generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
					}
				}                                
                else if (urlp.cmd.equals("ae_del_appn")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    qm.delAppn(con, urlp.app_id_lst, urlp.app_upd_timestamp_lst,prof.root_ent_id);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_rm_appn")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    qm.makeMultiRemoveActn(con, prof, urlp.app_id_lst, urlp.app_upd_timestamp_lst);
                    aeItemLessonQianDao ilsQd = new aeItemLessonQianDao();
                    ilsQd.removeSigninRecord(con, urlp.app_id_lst);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_ins_comm")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    qm.insAppnComment(con, prof.usr_id, urlp.app_id, urlp.aah_id, urlp.content);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_upd_comm")) {
                    //no access control

                    urlp.aeUpdComm(clientEnc, static_env.ENCODING);
                    urlp.ach.ach_upd_usr_id = prof.usr_id;
                    urlp.ach.upd(con);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_get_comm") || urlp.cmd.equals("ae_get_comm_xml")) {
                    //no access control
                    urlp.aeGetComm();
                    String result = formatXML(urlp.ach.getAppnCommentAsXML(con), prof,"ae_get_comm_xml");

                    if (urlp.cmd.equals("ae_get_comm_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }                }
                else if (urlp.cmd.equals("ae_ins_history")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    qm.insHistory2DB(con, sess, prof, urlp.app_id, urlp.content, urlp.upd_timestamp, false);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_ins_multi_history")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    qm.insMultiHistory2DB(con, sess, prof, urlp.app_id_lst, urlp.comment_lst,
                                     urlp.app_upd_timestamp_lst);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_make_multi_actn")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
//long startTime;
//long endTime;
//startTime = System.currentTimeMillis();
//urlp.app_priority_lst = new String[1];
//urlp.app_priority_lst[0] = "2";
                    qm.makeMultiActn(con, prof,
                              urlp.app_id_lst, urlp.content,
                              urlp.app_upd_timestamp_lst, urlp.process_id,
                              urlp.fr, urlp.to, urlp.action_verb, urlp.action_id,
                              urlp.status_id, urlp.app_priority_lst);
//endTime = System.currentTimeMillis();
//System.out.println("makeMultiActn = " + (endTime - startTime));
//System.out.println("================================================");

                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_app_move_queue")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();
                    qm.queueAction(con, sess, prof.usr_id, urlp.app_id, urlp.to);
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_cancel_history")) {
                    //no access control

                    aeQueueManager qm = new aeQueueManager();
                    qm.cancelHistorySession(sess);
//                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_cancel_appn")) {
                    //no access control

                    aeQueueManager qm = new aeQueueManager();
                    qm.cancelAppn(sess);
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_get_all_enrol") || urlp.cmd.equals("ae_get_all_enrol_xml")) {
                    //no access control

                    long ent_id = dbRegUser.getEntId(con, prof.usr_id);
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.getEnrolledItems(con, ent_id);
                    result = formatXML(result, prof);

                    if (urlp.cmd.equals("ae_get_all_enrol_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_get_pgm_enrol") || urlp.cmd.equals("ae_get_pgm_enrol_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    long ent_id = dbRegUser.getEntId(con, prof.usr_id);
                    aeQueueManager qm = new aeQueueManager();
                    String result = qm.getEnrolledItemsFromPgm(con, ent_id, urlp.itm_id, static_env);
                    result = formatXML(result, prof);

                    if (urlp.cmd.equals("ae_get_pgm_enrol_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equals("ae_ins_cat") || urlp.cmd.equals("ae_ins_cat_xml")) {
                        urlp.catalog(clientEnc, static_env.ENCODING);
                        urlp.pagination();
                        
                        if (urlp.cat.cat_tcr_id <= 0) {
                        	urlp.cat.cat_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
                        }
                        //access control
                        
                        if(!AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CAT_MAIN)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                        }
//                        AcCatalog accat = new AcCatalog(con);
//                        if(urlp.cat.cat_public_ind) {
//                            if(!accat.hasGlbInsPrivilege(prof.usr_ent_id, prof.current_role)) {
//                                throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
//                            }
//                        } else {
//                            if(!accat.hasInsPrivilege(prof.usr_ent_id, prof.current_role)) {
//                                throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
//                            }
//                        }
                        aeCatalog cat = urlp.cat;
                        
                        if(!cat.validCatalogName(con, 0)) {
                        	throw new cwSysMessage(aeCatalog.CAT_NAME_EXISTED);
                        }
                        
                        cat.cat_owner_ent_id = prof.root_ent_id;
                        // kim: allow duplicate catalog name
                        /*
                        if( !cat.validCatalogName(con, 0) ){
                            throw new cwSysMessage(aeCatalog.CAT_NAME_EXISTED);
                        }
                        */
                        //if current tc admin can not modify the trainingcenter with cat_tcr_id,throw error.
                        AcTrainingCenter acTc = new AcTrainingCenter(con);
                        if(AccessControlWZB.isRoleTcInd(prof.current_role)){
                            if (wizbini.cfgTcEnabled && !acTc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, cat.cat_tcr_id)) {
                                throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                            }
                        }

                        long cat_id = cat.ins(con, prof.root_ent_id, prof.usr_id, urlp.cat_acc_ent_id_list, urlp.ity_id_lst);

                        long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                        boolean checkStatus = false;;
                      

                        cat.cat_treenode.get(con, usr_ent_ids, checkStatus);
						boolean isUsrLrnRole = dbUtils.isUserLrnRole(con, prof.usr_ent_id, prof.current_role);
					    StringBuffer result = new StringBuffer(formatXML(cat.cat_treenode.getNodeAndChildrenAsXML(con, prof.root_ent_id, usr_ent_ids, checkStatus, false, aeTreeNode.TND_LIST_USR, urlp.cwPage, prof),prof));
                        con.commit();
                        urlp.url_success = cwUtils.substituteURL(urlp.url_success, cat.cat_treenode.tnd_id);
                        String sysMsg="lab_catalog_add_success";
        	            cwSysMessage e = new cwSysMessage(sysMsg);
        	            msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                }
                /*
                else if (urlp.cmd.equals("ae_get_new_cat_id") || urlp.cmd.equals("ae_get_new_cat_id_xml")) {
                    aeCatalog cat = new aeCatalog();
                    cat.cat_id = ((Long) sess.getAttribute(aeCatalog.CAT_ID)).longValue();
                    cat.cat_create_timestamp = (Timestamp) sess.getAttribute(aeCatalog.CAT_CREATE_TIMESTAMP);
                    String result = cat.catIdAsXML();
                    result = formatXML(result, prof);

                    if (urlp.cmd.equals("ae_get_new_cat_id_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet);
                    }
                }
                */
                else if (urlp.cmd.equals("ae_upd_cat_acc")) {
                        urlp.catalog(clientEnc, static_env.ENCODING);
                        aeCatalog cat = urlp.cat;

                        //access control
                 
                        if(!AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CAT_MAIN) ) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }

                        cat.updAccess(con, prof.root_ent_id, prof.usr_id, urlp.cat_in_upd_timestamp,
                                urlp.cat_acc_ent_id_list);
                        con.commit();
                        //System.out.println(urlp.url_success);
                        response.sendRedirect(urlp.url_success);
                }

                else if (urlp.cmd.equals("ae_upd_cat")) {
                        urlp.catalog(clientEnc, static_env.ENCODING);
                        aeCatalog cat = urlp.cat;

                        //access control
                 
                        if(!AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CAT_MAIN)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }
                        
                        if(!cat.validCatalogName(con, cat.cat_id)) {
                        	throw new cwSysMessage(aeCatalog.CAT_NAME_EXISTED);
                        }

                        cat.cat_owner_ent_id = prof.root_ent_id;
                        // kim: allow duplicate catalog name
                        /*
                        if( !cat.validCatalogName(con, cat.cat_id) ){
                            throw new cwSysMessage(aeCatalog.CAT_NAME_EXISTED);
                        }
                        */
                        AcTrainingCenter acTc = new AcTrainingCenter(con);
                        if (!wizbini.cfgTcEnabled) {
                        	cat.cat_tcr_id = 1;
                        } else if (AccessControlWZB.isRoleTcInd(prof.current_role) && !acTc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, cat.cat_tcr_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }
                        //if catalog tcr_id is changed,need to put all items which under it to share folder. 
                        if (wizbini.cfgTcEnabled) {
                        	long old_tcr_id = cat.getTcrByCatId(con, prof.root_ent_id);
                        	if (cat.cat_tcr_id != old_tcr_id) {
                        		aeTreeNodeRelation.delTnrByCata(con,  prof.root_ent_id, cat.cat_id);
                        		aeTreeNode.updateItemToShareByCatId(con, prof.root_ent_id, cat.cat_id);
                        	}
                        }
                        cat.upd(con, prof.root_ent_id, prof.usr_id, urlp.cat_in_upd_timestamp,
                                urlp.cat_acc_ent_id_list);
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }

                else if (urlp.cmd.equals("ae_get_cat") || urlp.cmd.equals("ae_get_cat_xml")) {
                        urlp.catalog(clientEnc, static_env.ENCODING);

                        aeCatalog cat = urlp.cat;

                        //long[] usr_ent_ids = {prof.usr_ent_id};
                        long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);

                        //access control
                      
                        if(!AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_CAT_MAIN) ) {
                            throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                        }
                        boolean checkStatus = false;
                       

                        cat.get(con, usr_ent_ids, checkStatus);
                        AcTrainingCenter acTc = new AcTrainingCenter(con);
                        //修改只与角色相关的功能才判断是否有操作权限
                        if(AccessControlWZB.isRoleTcInd(prof.current_role)){
                            if (wizbini.cfgTcEnabled && !acTc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, cat.cat_tcr_id)) {
                                throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                            }
                        }
                        StringBuffer result = new StringBuffer(formatXML(cat.prepUpdAsXML(con, prof.root_ent_id),prof));

                        if (urlp.cmd.equals("ae_get_cat_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }
                }
                else if (urlp.cmd.equals("ae_prep_ins_cat") || urlp.cmd.equals("ae_prep_ins_cat_xml")) {
                        //no access control
                		urlp.treenode(clientEnc, static_env.ENCODING);

                		String cat_tcr_id_str = request.getParameter("cat_tcr_id");
                		
                        StringBuffer result = new StringBuffer(formatXML(aeCatalog.blankContentAsXML(con, prof, urlp.show_all,cat_tcr_id_str),prof));
                        if (urlp.cmd.equals("ae_prep_ins_cat_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }

                }
                else if (urlp.cmd.equals("ae_prep_ins_glb_cat") || urlp.cmd.equals("ae_prep_ins_glb_cat_xml")) {
                        //no access control

                        StringBuffer result = new StringBuffer(formatXML(aeCatalog.prepInsGlbCatalog(con, prof.root_ent_id),prof));
                        if (urlp.cmd.equals("ae_prep_ins_glb_cat_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }

                }
                else if (urlp.cmd.equals("ae_del_cat")) {
                    urlp.catalog(clientEnc, static_env.ENCODING);
                    aeCatalog cat = urlp.cat;

                    //access control
                    AcCatalog accat = new AcCatalog(con);
                    if(!accat.hasUpdPrivilege(cat.cat_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                        throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                    }
                    if(AccessControlWZB.isRoleTcInd(prof.current_role)){
                        if (wizbini.cfgTcEnabled && !aeCatalog.canEditByTcadmin(con, cat.cat_id, prof.usr_ent_id)) {
                            throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                        }
                    }

                    cat.del(con, prof.root_ent_id, urlp.cat_in_upd_timestamp);
                    con.commit();
                    msgBox(MSG_STATUS, con, new cwSysMessage(aeCatalog.DEL_SUCCESS), prof, urlp.url_success, out);
                    //response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_upd_mobile_cat_status")) {
                	urlp.catalog(clientEnc, static_env.ENCODING);
                	aeCatalog cat = urlp.cat;
                	
                	//access control
                	AcCatalog accat = new AcCatalog(con);
                	if(!accat.hasUpdPrivilege(cat.cat_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                		throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                	}
                	if (wizbini.cfgTcEnabled && !aeCatalog.canEditByTcadmin(con, cat.cat_id, prof.usr_ent_id)) {
                		throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                	}
                	
                	cat.updMobileCatStatus(con, prof.root_ent_id, urlp.cat_in_upd_timestamp);
                	con.commit();
                	response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_multi_del_cat")) {
                    urlp.catalog(clientEnc, static_env.ENCODING);

                    long[] cat_id_lst = urlp.cat_id_lst;
                    Timestamp[] cat_in_upd_timestamp_lst = urlp.cat_in_upd_timestamp_lst;
                    try {
                        aeCatalog cat = new aeCatalog();
                        aeCatalog.delMultCata(con, wizbini.cfgTcEnabled, cat_id_lst, cat_in_upd_timestamp_lst, prof);
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                    } catch (cwSysMessage e) {
                        msgBox(MSG_ERROR, con, e, prof, urlp.url_success, out);
                        return;
                    }
                }
                else if (urlp.cmd.equals("ae_get_tnd") || urlp.cmd.equals("ae_get_tnd_xml")) {
                        boolean checkStatus;
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        AcCatalog accat = new AcCatalog(con);
                        if(accat.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                            checkStatus = false;
                        else
                            checkStatus = true;

                        urlp.treenode(clientEnc, static_env.ENCODING);
                        aeTreeNode tnd = urlp.tnd;
                        //long[] usr_ent_ids = {prof.usr_ent_id};
                        long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);

                        //access control
                        tnd.tnd_cat_id = tnd.getCatalogId(con);
                        AcTreeNode actnd = new AcTreeNode(con);
                        if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role,
                                                    tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
                            throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                        }

                        tnd.get(con, usr_ent_ids, checkStatus);

                        StringBuffer result = new StringBuffer(tnd.prepUpdAsXML(con, prof.root_ent_id));

                        if(urlp.ctb_type != null && urlp.ctb_type.length() > 0)
                            result.append(dbUtils.NEWL).append(CodeTable.lookUp(con, null, urlp.ctb_type, null, null, null, null, true,CodeTable.CTB_ALL_PAGES, null, false));

                        if (wizbini.cfgTcEnabled) {
                        	result.append("<from_show_all>").append(urlp.show_all).append("</from_show_all>");
                        }
                        result = new StringBuffer(formatXML(result.toString(), prof));

                        if (urlp.cmd.equals("ae_get_tnd_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }
                }

                else if (urlp.cmd.equals("ae_ins_tnd")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);
                        aeTreeNode tnd = urlp.tnd;
                        long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);

                        //access control
                        AcTreeNode actnd = new AcTreeNode(con);
                        if(!actnd.hasInsPrivilege(tnd.tnd_parent_tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                        }

                        if( !tnd.validCategoryName(con, 0) )
                            throw new cwSysMessage(aeTreeNode.TND_ITEM_NAME_EXISTED);

                        tnd.ins(con, prof.root_ent_id, prof.usr_id);
                        con.commit();
                        String sysMsg="lab_sub_catalog_add_success";
        	            cwSysMessage e = new cwSysMessage(sysMsg);
        	            msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);

                }

                else if (urlp.cmd.equals("ae_upd_tnd")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);
                        aeTreeNode tnd = urlp.tnd;

                        //access control
                        AcTreeNode actnd = new AcTreeNode(con);
                        if(!actnd.hasUpdPrivilege(tnd.tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }

                        if( tnd.tnd_parent_tnd_id != 0 ) { //For Backward Compatible
                            if( !tnd.validCategoryName(con, tnd.tnd_id) )
                                throw new cwSysMessage(aeTreeNode.TND_ITEM_NAME_EXISTED);
                        }

                        tnd.upd(con, prof.root_ent_id, prof.usr_id, urlp.tnd_in_upd_timestamp);
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }

                else if (urlp.cmd.equals("ae_upd_tnd_order")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);

                        //access control
                        AcCatalog accat = new AcCatalog(con);
                        if(!accat.hasInsPrivilege(prof.usr_ent_id, prof.current_role)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                        }

                        aeTreeNode.updOrder(con, urlp.tnd_id_lst);
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }

                else if (urlp.cmd.equals("ae_prep_ins_tnd") || urlp.cmd.equals("ae_prep_ins_tnd_xml")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);
                        aeTreeNode tnd = urlp.tnd;

                        //access control
                        AcTreeNode actnd = new AcTreeNode(con);
                        if(!actnd.hasInsPrivilege(tnd.tnd_parent_tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                        }

                        StringBuffer result = new StringBuffer(aeTreeNode.blankContentAsXML(con, tnd.tnd_parent_tnd_id, tnd.tnd_title, tnd.tnd_link_tnd_id, tnd.tnd_status, prof.root_ent_id));

                        if(urlp.ctb_type != null && urlp.ctb_type.length() > 0)
                            result.append(dbUtils.NEWL).append(CodeTable.lookUp(con, null, urlp.ctb_type, null, null, null, null, true,CodeTable.CTB_ALL_PAGES,null,false));

                        result = new StringBuffer(formatXML(result.toString(), prof));
                        if (urlp.cmd.equals("ae_prep_ins_tnd_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }

                }
                else if (urlp.cmd.equals("ae_del_multi_tnd")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);
                        aeTreeNode tnd = urlp.tnd;

                        long[] tnd_id_lst = urlp.tnd_id_lst;
                        Timestamp[] tnd_in_upd_timestamp_lst = urlp.tnd_in_upd_timestamp_lst;

                        for(int i=0;i<tnd_id_lst.length;i++) {
                            tnd.tnd_id = tnd_id_lst[i];

                            //access control
                            AcTreeNode actnd = new AcTreeNode(con);
                            if(!actnd.hasUpdPrivilege(tnd.tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                            }
                            AcTrainingCenter acTc = new AcTrainingCenter(con);
                            long tcr_id = aeTreeNode.getTcrIdByTndId(con, tnd.tnd_id, prof.root_ent_id);
                            if (wizbini.cfgTcEnabled && !acTc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, tcr_id)) {
                                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                            }

                            tnd.del(con, prof.root_ent_id, urlp.tnd_in_upd_timestamp_lst[i]);
                        }

                        con.commit();
                        msgBox(MSG_STATUS, con, new cwSysMessage(aeCatalog.DEL_SUCCESS), prof, urlp.url_success, out);
                }
                else if (urlp.cmd.equals("ae_del_tnd")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);

                        aeTreeNode tnd = urlp.tnd;

                        //access control
                        AcTreeNode actnd = new AcTreeNode(con);
                        if(!actnd.hasUpdPrivilege(tnd.tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }
                      	aeTreeNodeRelation tnr= new aeTreeNodeRelation();
                    	tnr.delTnr(con, aeTreeNodeRelation.TNR_TYPE_TND_PARENT_TND, tnd.tnd_id);
                        tnd.del(con, prof.root_ent_id, urlp.tnd_in_upd_timestamp);
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_del_itm_tnd")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);

                        aeTreeNode tnd = urlp.tnd;

                        //access control
                        AcTreeNode actnd = new AcTreeNode(con);
                        if(!actnd.hasUpdPrivilege(tnd.tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }

                        boolean isOrphan = tnd.delItem(con, prof.root_ent_id, urlp.tnd_in_upd_timestamp);
                        con.commit();
                        if(isOrphan) {
                            cwSysMessage e = new cwSysMessage(aeTreeNode.TND_PUT_ITM_ORPHAN);
                            msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                        }
                        else {
                            cwSysMessage e = new cwSysMessage(aeTreeNode.TND_NOT_PUT_ITM_ORPHAN);
                            msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                        }
                }/*
                else if (urlp.cmd.equals("ae_change_tnd_parent")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);

                        aeTreeNode tnd = urlp.tnd;
                        long new_parent_tnd_id = tnd.tnd_parent_tnd_id;

                        tnd.changeNodeParent(con, prof.root_ent_id, prof.usr_id, urlp.tnd_in_upd_timestamp, new_parent_tnd_id);
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }*/
                else if (urlp.cmd.equals("ae_get_all_itm") || urlp.cmd.equals("ae_get_all_itm_xml")) {
                        //urlp.treenode(clientEnc, static_env.ENCODING);
                        urlp.item(clientEnc, static_env.ENCODING);
                        boolean checkStatus;
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        AcItem acitm = new AcItem(con);
                        if(acitm.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                            checkStatus = false;
                        else
                            checkStatus = true;

                        //no access control

                        StringBuffer result = new StringBuffer(formatXML(aeItem.getOwnerItems(con, prof.root_ent_id, checkStatus, aeItem.ITM_ALL, urlp.orderBy, urlp.sortOrder,wizbini.cfgTcEnabled,prof),prof));
                        if (urlp.cmd.equals("ae_get_all_itm_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }

                }
                else if (urlp.cmd.equals("ae_get_orphan_itm") || urlp.cmd.equals("ae_get_orphan_itm_xml")) {
                        //urlp.treenode(clientEnc, static_env.ENCODING);
	                    urlp.item(clientEnc, static_env.ENCODING);
	                    urlp.getTcrId();
                        boolean checkStatus;
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        AcItem acitm = new AcItem(con);
                        if(acitm.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                            checkStatus = false;
                        else
                            checkStatus = true;

                        //no access control
                        StringBuffer resultBuf = new StringBuffer();
                        if (wizbini.cfgTcEnabled) {
                        	resultBuf.append(aeItem.getOwnerItems(con, prof.root_ent_id, checkStatus, aeItem.ITM_ORPHAN, urlp.orderBy, urlp.sortOrder, wizbini.cfgTcEnabled, prof, urlp.tcr_id));
                        } else {
                        	resultBuf.append(aeItem.getOwnerItems(con, prof.root_ent_id, checkStatus, aeItem.ITM_ORPHAN, urlp.orderBy, urlp.sortOrder, wizbini.cfgTcEnabled, prof));
                        }
                        
                        long tcr_id = 0;
                        if(request.getParameter("tcr_id") != null){
                        	tcr_id = Long.parseLong(request.getParameter("tcr_id"));
                        }
                        if(tcr_id < 0){
                        	tcr_id = 0;
                        }
                        resultBuf.append("<cat_tcr_id>").append(tcr_id).append("</cat_tcr_id>");
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                        String result = formatXML(resultBuf.toString(), metaXML, APPLYEASY, prof);
                        if (urlp.cmd.equals("ae_get_orphan_itm_xml")) {
                            out.println(result);
                        } else {
                            generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                        }

                }
                else if (urlp.cmd.equals("ae_pick_tnd_itm") || urlp.cmd.equals("ae_pick_tnd_itm_xml")) {
                        urlp.item(clientEnc, static_env.ENCODING);
                        aeItem itm = urlp.itm;
                        sess.setAttribute(aeTreeNode.TND_PARENT_TND_ID, new Long(urlp.tnd_parent_tnd_id));

                        boolean checkStatus;
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        AcCatalog accat = new AcCatalog(con);
                        if(accat.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                            checkStatus = false;
                        else
                            checkStatus = true;
                        long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);

                        //no access control

                        StringBuffer result = new StringBuffer(formatXML(aeCatalog.catalogListAsXML(con, usr_ent_ids, checkStatus, prof,  wizbini),prof));

                        if (urlp.cmd.equals("ae_pick_tnd_itm_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }
                }
                else if (urlp.cmd.equals("ae_pick_tnd_itm_done") || urlp.cmd.equals("ae_pick_tnd_itm_done_xml")) {
                        urlp.item(clientEnc, static_env.ENCODING);
                        urlp.pagination();

                        long l;
                        long[] itm_id_lst = urlp.itm_id_lst;
                        aeTreeNode tnd = new aeTreeNode();
                        // get the id of the tree node for the items to be added to
                        // if specified through url, use it, otherwise get from session
                        if (urlp.tnd_parent_tnd_id > 0) {
                            l = urlp.tnd_parent_tnd_id;
                        } else {
                            Long temp = (Long) sess.getAttribute(aeTreeNode.TND_PARENT_TND_ID);
                            if(temp != null)
                                l = temp.longValue();
                            else
                                l = 0;
                        }
                        tnd.tnd_parent_tnd_id = l;
                        long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);

                        for(int i=0;i<itm_id_lst.length;i++) {
                            tnd.tnd_itm_id = itm_id_lst[i];

                            //access control
                            AcTreeNode actnd = new AcTreeNode(con);
                            if(!actnd.hasInsPrivilege(tnd.tnd_parent_tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                                throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                            }

                            tnd.ins(con, prof.root_ent_id, prof.usr_id);
                        }

                        // for the response, if url_success if specified, use it
                        // otherwise generate tree node details of the specified tree node
                        if (urlp.url_success == null) {
                            //same as cmd=ae_get_tnd_cnt_lst
                            boolean checkStatusCat;
                            boolean checkStatusItm;
                            /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                            AcCatalog accat = new AcCatalog(con);
                            if(accat.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                                checkStatusCat = false;
                            else
                                checkStatusCat = true;

                            AcItem acitm = new AcItem(con);
                            if(acitm.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                                checkStatusItm = false;
                            else
                                checkStatusItm = true;

                            tnd = new aeTreeNode();
                            tnd.tnd_id = l;
                            //long[] usr_ent_ids = {prof.usr_ent_id};
                            tnd.get(con, usr_ent_ids, checkStatusCat);
							
                            StringBuffer result = new StringBuffer(formatXML(tnd.getNodeAndChildrenAsXML(con, prof.root_ent_id, usr_ent_ids, checkStatusCat, checkStatusItm, aeTreeNode.TND_LIST_ITM, urlp.cwPage, prof),prof));
                            con.commit();
                            if (urlp.cmd.equals("ae_pick_tnd_itm_done_xml")) {
                                out.println(result.toString());
                            } else {
                                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                            }
                        } else {
                            con.commit();
                            response.sendRedirect(urlp.url_success);
                        }
                }
                else if (urlp.cmd.equals("ae_paste_itm")) {
                        urlp.item(clientEnc, static_env.ENCODING);

                        long[] itm_id_lst = urlp.itm_id_lst;
                        aeTreeNode tnd = new aeTreeNode();

                        tnd.tnd_parent_tnd_id = urlp.tnd_parent_tnd_id;

                        for(int i=0;i<itm_id_lst.length;i++) {
                            tnd.tnd_itm_id = itm_id_lst[i];

                            //access control
                            AcTreeNode actnd = new AcTreeNode(con);
                            if(!actnd.hasInsPrivilege(tnd.tnd_parent_tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                                throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                            }

                            tnd.ins(con, prof.root_ent_id, prof.usr_id);
                        }

                        con.commit();
                        response.sendRedirect(urlp.url_success);

                }
                else if (urlp.cmd.equals("ae_pick_tnd_itm_cancel") || urlp.cmd.equals("ae_pick_tnd_itm_cancel_xml")) {
                        urlp.item(clientEnc, static_env.ENCODING);

                        long l;
                        long[] itm_id_lst = urlp.itm_id_lst;
                        aeTreeNode tnd = new aeTreeNode();

                        //get Session Values/////
                        Long temp = (Long) sess.getAttribute(aeTreeNode.TND_PARENT_TND_ID);
                        if(temp != null)
                            l = temp.longValue();
                        else
                            l = 0;
                        /////////////////////////
                        tnd.tnd_parent_tnd_id = l;
                        sess.removeAttribute(aeTreeNode.TND_PARENT_TND_ID);

                        //same as cmd=ae_prep_ins_itm
                        /*
                        long itm_tpl_id = getTplId(con, sess, aeTemplate.ITEM, prof.root_ent_id);
                        long wrk_tpl_id = getTplId(con, sess, aeTemplate.WORKFLOW, prof.root_ent_id);
                        long app_tpl_id = getTplId(con, sess, aeTemplate.APPNFORM, prof.root_ent_id);

                        sess.setAttribute(aeTemplate.ITEM, new Long(itm_tpl_id));
                        sess.setAttribute(aeTemplate.WORKFLOW, new Long(wrk_tpl_id));
                        sess.setAttribute(aeTemplate.APPNFORM, new Long(app_tpl_id));
                        */
                        long tnd_id = tnd.tnd_parent_tnd_id;

                        //access control
                        AcItem acitm = new AcItem(con);
                        if(!acitm.hasInsPrivilege(prof.usr_ent_id, prof.current_role, tnd_id, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }

                        /*StringBuffer result = new StringBuffer(formatXML(aeItem.prep_ins(con, tnd_id, prof.root_ent_id, itm_tpl_id, wrk_tpl_id, app_tpl_id),prof));*/
                        StringBuffer result = new StringBuffer(formatXML(aeItem.prep_ins(con, tnd_id, prof.root_ent_id),prof));
                        if (urlp.cmd.equals("ae_pick_tnd_itm_cancel_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }
                }
                else if (urlp.cmd.equals("ae_pick_tnd") || urlp.cmd.equals("ae_pick_tnd_xml")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);
                        aeTreeNode tnd = urlp.tnd;

                        boolean checkStatus;
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        AcCatalog accat = new AcCatalog(con);
                        if(accat.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                            checkStatus = false;
                        else
                            checkStatus = true;

                        if(tnd.tnd_title == null)
                            tnd.tnd_title = "";
                        if(tnd.tnd_status == null)
                            tnd.tnd_status = "";

                        sess.setAttribute(aeTreeNode.TND_TITLE, tnd.tnd_title);
                        sess.setAttribute(aeTreeNode.TND_STATUS, tnd.tnd_status);
                        sess.setAttribute(aeTreeNode.TND_PARENT_TND_ID, new Long(tnd.tnd_parent_tnd_id));
                        sess.setAttribute(aeTreeNode.TND_ID, new Long(tnd.tnd_id));
                        sess.setAttribute(aeTreeNode.TND_LINK_TND_ID, new Long(tnd.tnd_link_tnd_id));

                        long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);

                        //no access control

                        StringBuffer result = new StringBuffer(formatXML(aeCatalog.catalogListAsXML(con, usr_ent_ids, checkStatus, prof,  wizbini),prof));
                        if (urlp.cmd.equals("ae_pick_tnd_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }

                }
                else if (urlp.cmd.equals("ae_pick_tnd_done") || urlp.cmd.equals("ae_pick_tnd_done_xml")) {
                        urlp.treenode(clientEnc, static_env.ENCODING);
                        aeTreeNode tnd = urlp.tnd;

                        //get Session Values/////
                        tnd.tnd_title = (String)sess.getAttribute(aeTreeNode.TND_TITLE);
                        tnd.tnd_status = (String)sess.getAttribute(aeTreeNode.TND_STATUS);
                        Long temp = (Long) sess.getAttribute(aeTreeNode.TND_PARENT_TND_ID);
                        if(temp != null)
                            tnd.tnd_parent_tnd_id = temp.longValue();
                        else
                            tnd.tnd_parent_tnd_id = 0;

                        temp = (Long)sess.getAttribute(aeTreeNode.TND_ID);
                        if(temp != null)
                            tnd.tnd_id = temp.longValue();
                        else
                            tnd.tnd_id = 0;

                        if(tnd.tnd_link_tnd_id == 0) {
                            temp = (Long)sess.getAttribute(aeTreeNode.TND_LINK_TND_ID);
                            if(temp != null)
                                tnd.tnd_link_tnd_id = temp.longValue();
                            else
                                tnd.tnd_link_tnd_id = 0;
                        }
                        /////////////////////////
                        long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                        StringBuffer result;
                        if(tnd.tnd_id == 0) {
                            //an ins command

                            //access control
                            AcTreeNode actnd = new AcTreeNode(con);
                            if(!actnd.hasInsPrivilege(tnd.tnd_parent_tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                                throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                            }

                            result = new StringBuffer(formatXML(aeTreeNode.blankContentAsXML(con, tnd.tnd_parent_tnd_id, tnd.tnd_title, tnd.tnd_link_tnd_id, tnd.tnd_status, prof.root_ent_id),prof));
                        }
                        else {
                            //an upd command
                            String temp_title = tnd.tnd_title;
                            String temp_status = tnd.tnd_status;
                            long temp_link_tnd_id = tnd.tnd_link_tnd_id;

                            boolean checkStatus;
                            /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                            AcItem acitm = new AcItem(con);
                            if(acitm.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                                checkStatus = false;
                            else
                                checkStatus = true;

                            //access control
                            AcTreeNode actnd = new AcTreeNode(con);
                            if(!actnd.hasUpdPrivilege(tnd.tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                            }

                            tnd.get(con, usr_ent_ids, checkStatus);

                            tnd.tnd_title = temp_title;
                            tnd.tnd_status = temp_status;
                            tnd.tnd_link_tnd_id = temp_link_tnd_id;

                            result = new StringBuffer(formatXML(tnd.contentAsXML(con),prof));
                        }

                        if (urlp.cmd.equals("ae_pick_tnd_done_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }

                }
                else if (urlp.cmd.equals("ae_prep_ins_itm") || urlp.cmd.equals("ae_prep_ins_itm_xml")) {
                        urlp.item(clientEnc, static_env.ENCODING);

                        /*
                        long itm_tpl_id = urlp.itm_tpl_id;
                        long wrk_tpl_id = urlp.wrk_tpl_id;
                        long app_tpl_id = urlp.app_tpl_id;
                        Long temp;


                        if(itm_tpl_id == 0) {
                            itm_tpl_id = getTplId(con, sess, aeTemplate.ITEM, prof.root_ent_id);
                        }
                        if(wrk_tpl_id == 0) {
                            wrk_tpl_id = getTplId(con, sess, aeTemplate.WORKFLOW, prof.root_ent_id);
                        }
                        if(app_tpl_id == 0) {
                            app_tpl_id = getTplId(con, sess, aeTemplate.APPNFORM, prof.root_ent_id);
                        }

                        sess.setAttribute(aeTemplate.ITEM, new Long(itm_tpl_id));
                        sess.setAttribute(aeTemplate.WORKFLOW, new Long(wrk_tpl_id));
                        sess.setAttribute(aeTemplate.APPNFORM, new Long(app_tpl_id));
                        */
                        long tnd_id = urlp.tnd_parent_tnd_id;

                        //access control
                        AcItem acitm = new AcItem(con);
                        if(!acitm.hasInsPrivilege(prof.usr_ent_id, prof.current_role, tnd_id, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }

                        /*StringBuffer result = new StringBuffer(formatXML(aeItem.prep_ins(con, tnd_id, prof.root_ent_id, itm_tpl_id, wrk_tpl_id, app_tpl_id),prof));*/
                        StringBuffer result = new StringBuffer(formatXML(aeItem.prep_ins(con, tnd_id, prof.root_ent_id),prof));
                        if (urlp.cmd.equals("ae_prep_ins_itm_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }
                }
                else if ( urlp.cmd.equalsIgnoreCase("ae_get_ent_lst") || urlp.cmd.equalsIgnoreCase("ae_get_ent_lst_xml") ){

                        urlp.get_ent_lst(clientEnc, static_env.ENCODING);
                        StringBuffer xml = new StringBuffer();
                        xml.append(urlp.dbUsg.getMemberListXML(con, sess, prof, urlp.cur_page, urlp.pagesize, urlp.timestamp));

                        long[] usr_ent_ids = {0};
                        boolean checkStatus;
                        if(prof == null) {
                            checkStatus = true;
                        }
                        else {
                            usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                            /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                            AcCatalog accat = new AcCatalog(con);
                            if(accat.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                                checkStatus = false;
                            else
                                checkStatus = true;
                        }

                        aeTreeNode tnd = new aeTreeNode();
                        tnd.tnd_id = urlp.tnd_id;

                        //access control
                        AcTreeNode actnd = new AcTreeNode(con);
                        tnd.tnd_cat_id = tnd.getCatalogId(con);
                        if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role,
                                                    tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
                            throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                        }

                        tnd.get(con, usr_ent_ids, checkStatus);

                        aeCatalogAccess cac = new aeCatalogAccess();
                        cac.cac_cat_id = tnd.tnd_cat_id;
                        xml.append("<picked_entity>").append(dbUtils.NEWL);
                        xml.append(cac.getAssignEntityAsXML(con, prof.root_ent_id));
                        xml.append("</picked_entity>");
                        if (urlp.cmd.equals("ae_get_ent_lst_xml")) {
                            out.println(formatXML(xml.toString(),prof));
                        } else {
                            generalAsHtml(formatXML(xml.toString(),prof), out, urlp.stylesheet, prof.xsl_root);
                        }


                }
                else if (urlp.cmd.equals("ae_change_tpl") || urlp.cmd.equals("ae_change_tpl_xml")) {

                        urlp.item(clientEnc, static_env.ENCODING);
                        long tnd_id = urlp.tnd_parent_tnd_id;
                        Long temp;
                        long tpl_id=0;
                        String ttp_title=urlp.tpl_type;

                        if(urlp.cur_tpl_id != 0)
                            tpl_id = urlp.cur_tpl_id;
                        else if(ttp_title.equals(aeTemplate.ITEM)) {
                            tpl_id = getTplId(con, sess, ttp_title, prof.root_ent_id);
                        }
                        else if(ttp_title.equals(aeTemplate.WORKFLOW)) {
                            tpl_id = getTplId(con, sess, ttp_title, prof.root_ent_id);
                        }
                        else if(ttp_title.equals(aeTemplate.APPNFORM)) {
                            tpl_id = getTplId(con, sess, ttp_title, prof.root_ent_id);
                        }

                        //no access control

                        StringBuffer result = new StringBuffer(formatXML(aeTemplate.getAllTemplateAsXML(con, prof.root_ent_id, tnd_id, tpl_id, ttp_title, static_env),prof));

                        if (urlp.cmd.equals("ae_change_tpl_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }
                }
                else if (urlp.cmd.equals("ae_form_ins_itm") || urlp.cmd.equals("ae_form_ins_itm_xml")) {
                	
                		Vector tcIds = ViewTrainingCenter.getAllTcByOfficer(con, prof.usr_ent_id);
                		
                		if(tcIds == null || tcIds.size() == 0){//如果没有可以管理的培训中心，提示
                			throw new cwSysMessage("TC002");
                		}
                	
                		urlp.item(clientEnc, static_env.ENCODING);
						urlp.saveGetItemSubmittedParams(clientEnc, static_env.ENCODING);
						//自动报名间隔
						urlp.itm.auto_enroll_interval =  loadTargetLrnCacheAndCourseEnrollScheduler.Auto_Enroll_Interval;
                        String metaXml="";
                        String itmXml="";
                        //如果是培训计划
                        if(urlp.training_plan){
                        	if(urlp.tpn_itm_run_ind){
                        		urlp.itm.itm_run_ind=true;
                        	}
                        	dbTpTrainingPlan tpTp = new dbTpTrainingPlan();
                        	tpTp.tpn_id = urlp.plan_id;
                        	if(!tpTp.isLastUpd(con, urlp.tpn_update_timestamp)) {
                        		throw new cwSysMessage("TPN007");
                        	}                  
                        	tpTp.get(con);
                        	itmXml = tpPlanManagement.getPlanDetailsAsXML(con, tpTp);  
                        	metaXml="<training_plan>true</training_plan>";
                        }else{
                        	metaXml="<training_plan>false</training_plan>";
                        }
                       //如果是考试
              		  if(urlp.tpn_itm_run_ind) {
            			  aeReqParam sessUrlp = new aeReqParam(request, bMultiPart, multi);
            			  if(sess.getAttribute("PLAN_ITM_SESS")!=null){                   	
                         	 sessUrlp.getGetItemSubmittedParams("PLAN_ITM_SESS");
            			  }
            			  urlp.itm.itm_blend_ind = sessUrlp.itm.itm_blend_ind;
            			  urlp.itm.itm_exam_ind = sessUrlp.itm.itm_exam_ind;
            			  urlp.itm.itm_ref_ind = sessUrlp.itm.itm_ref_ind;
            			  urlp.itm.itm_dummy_type = sessUrlp.itm.itm_dummy_type;
            		  }
                        itmXml+=getFormInsItmXML(con, sess, urlp, prof);
                        metaXml+="<training_type>"+urlp.training_type+"</training_type>";
                        metaXml+="<itm_integrated_ind>"+urlp.itm.itm_integrated_ind+"</itm_integrated_ind>";
                        String result = formatXML(itmXml, metaXml, APPLYEASY,prof, urlp.stylesheet);
                        if (urlp.cmd.equals("ae_form_ins_itm_xml")) {
                            out.println(result);
                        } else {
                            generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                        }
                }
                else if (urlp.cmd.equals("ae_ins_itm") || urlp.cmd.equals("ae_ins_itm_xml")) {
                	    int acgi_aci_id = 0;  //班级，copy课程设置的所需时数，保存的id。
                        urlp.item(clientEnc, static_env.ENCODING);                      
                        urlp.mote(clientEnc, static_env.ENCODING);
                        long itm_id_ = 0;                     
                        String dummy_type = urlp.itm.itm_dummy_type;
                        if(urlp.training_plan && urlp.tpn_itm_run_ind){
                        	 urlp.itm.itm_run_ind=urlp.tpn_itm_run_ind;
                        	 if(urlp.vColName.contains("itm_run_ind")){
                        		 int index=urlp.vColName.indexOf("itm_run_ind");
                        		 urlp.vColName.remove(index);
                        		 urlp.vColType.remove(index);
                        		 urlp.vColValue.remove(index);
                        		 urlp.vColName.addElement("itm_run_ind");
                        		 urlp.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
                        		 urlp.vColValue.addElement(new Boolean(urlp.itm.itm_run_ind));
                        	 }
                        	 aeReqParam sessUrlp = new aeReqParam(request, bMultiPart, multi);
                        	 if(sess.getAttribute("PLAN_ITM_SESS")!=null){                   	
	                        	 sessUrlp.getGetItemSubmittedParams("PLAN_ITM_SESS");
	                        	 try{	               
	     	                        sessUrlp.itm.itm_owner_ent_id = prof.root_ent_id;
	     	                        sessUrlp.itm.getItemTypeInd(con);
	     	                        long tnd_id = sessUrlp.tnd_parent_tnd_id;
	     	                        //get template types
	     	                        ViewItemTemplate viItmTpl = new ViewItemTemplate();
	     	                        viItmTpl.ownerEntId = prof.root_ent_id;
	     	                        viItmTpl.itemType = sessUrlp.itm.itm_type;
	     	                        viItmTpl.runInd = sessUrlp.itm.itm_run_ind;
	     	                        viItmTpl.sessionInd = sessUrlp.itm.itm_session_ind;
	     	                        String[] templateTypes = viItmTpl.getItemTypeTemplates(con);
	     	                        
	     	                        if(!wizbini.cfgTcEnabled) {
	     	                        	long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
	     	                        	sessUrlp.itm.itm_tcr_id = sup_tcr_id;
	     	                        }
	     	                       
	
	     	                        //find out the template id
	     	                        long[] tpl_ids;
	     	                        // get own template for non-run item (for session, no need to add parent template)
	 	                            tpl_ids = getTplIds(con, sess, sessUrlp.itm.itm_type,
	 	                                                templateTypes, sessUrlp.itm.itm_session_ind, prof.root_ent_id);
	 	                            sessUrlp.itm.itm_app_approval_type = (String)sess.getAttribute(SESS_ITM_APP_APPROVAL_TYPE);
	
	     	                        aeItem itm = sessUrlp.itm;
	     	                        //access control
	     	                        AcItem acitm = new AcItem(con);
	     	                        if(!acitm.hasInsPrivilege(prof.usr_ent_id, prof.current_role, tnd_id, prof.root_ent_id)) {
	     	                            throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
	     	                        }
//	     	                        if (!itm.itm_run_ind && !itm.itm_session_ind){
//	     	                            if (acitm.isItmMgtAssistant(prof.usr_ent_id, prof.current_role)){
//	     	                                itm.itm_approval_status = aeItem.ITM_APPROVAL_STATUS_PREAPPROVE;
//	     	                                sessUrlp.vColName.addElement("itm_approval_status");
//	     	                                sessUrlp.vColType.addElement(DbTable.COL_TYPE_STRING);
//	     	                                sessUrlp.vColValue.addElement(itm.itm_approval_status);
//	     	                            }    	        
//	     	                        }
	                                 if(itm.itm_qdb_ind) {
	                                     dbCourse cos = new dbCourse();
	                                     cos.res_lan = prof.label_lan;
	                                     cos.res_title = itm.itm_title;
	                                     cos.res_usr_id_owner = prof.usr_id;
	                                     cos.res_upd_user = prof.usr_id;
	                                     itm.insWZBCourse(con, prof, cos, tnd_id,
	                                             templateTypes, tpl_ids,
	                                             sessUrlp.vColName, sessUrlp.vColType, sessUrlp.vColValue,
	                                             sessUrlp.vClobColName, sessUrlp.vClobColValue);
	                                 }
	                                 else {
	                                     itm.ins(con, prof.root_ent_id, prof.usr_id, tnd_id,
	                                         templateTypes, tpl_ids, prof,
	                                         sessUrlp.vColName, sessUrlp.vColType, sessUrlp.vColValue,
	                                         sessUrlp.vClobColName, sessUrlp.vClobColValue);
	                                 }
	                                 if(urlp.iac_id_lst == null && prof.current_role.equals(AcObjective.TADM)){  
	                                  	urlp.iac_id_lst = new String[1];
	                                  	urlp.iac_id_lst[0]  = "TADM_1~"+prof.usr_ent_id;
	                                  }else if(urlp.iac_id_lst[0].equals("TADM_1")&& prof.current_role.equals(AcObjective.TADM)){
	                                  	urlp.iac_id_lst = new String[1];
	                                  	urlp.iac_id_lst[0]  = "TADM_1~"+prof.usr_ent_id;
	                                  }
	                                 itm.insExternalInfo(con,
	                                                     sessUrlp.tnd_id_lst,
	     												 sessUrlp.tnd_id_lst_value,
	                                                     sessUrlp.target_ent_group_lst,
	                                                     sessUrlp.comp_target_ent_group_lst,
	                                                     sessUrlp.cost_center_group_lst,
	                                                     sessUrlp.iac_id_lst,
	                                                     sessUrlp.mote,
	                                                     sessUrlp.cm_lst,
	                                                     sessUrlp.fgt_id_vec,
	                                                     sessUrlp.fig_val_vec,
	                                                     prof.usr_id,
	                                                     prof.usr_ent_id,
	                                                     prof.root_ent_id,
	                                                     wizbini.cfgSysSetupadv.getDefaultTaId(),
	                                                     wizbini.cfgTcEnabled);
	     	                        //save upload file
	     	                      //  if(bFileUpload) {
	     	                        	uploadedFiles(itm.itm_id);
	     	                      //  }
	     	                       aeItemExtension itmExtension = sessUrlp.itmExtension;
	     	                       itmExtension.ins(con, itm.itm_id, sessUrlp.vExtensionColName, sessUrlp.vExtensionColType, sessUrlp.vExtensionColValue);
	     	                        //try to apply for the newly created course
	     	                        //if prof.current_role is a learner role
	     	                        if(sessUrlp.apply_now_ind) {
	     	                            String[] lrn_role = {AccessControlWZB.ROL_EXT_ID_NLRN};
	     	                            if(lrn_role != null && lrn_role.length > 0) {
	     	                                for(int i=0; i<lrn_role.length; i++) {
	     	                                    if(prof.current_role.equalsIgnoreCase(lrn_role[i])) {
	     	                                        aeQueueManager qm = new aeQueueManager();
	     	                                        qm.insApplication(con, null, prof.usr_ent_id,
	     	                                                        itm.itm_id, prof, 0);
	     	                                        break;
	     	                                    }
	     	                                }
	     	                            }
	     	                        }
	     	                        itm_id_ = itm.itm_id;   
	     	                        sess.setAttribute("PLAN_ITM_SESS_TEMP", sess.getAttribute("PLAN_ITM_SESS"));
	     	                        sess.removeAttribute("PLAN_ITM_SESS");
	     	                        sess.setAttribute(SESS_PARENT_ITM_ID,new Long(itm_id_));
	     	                    }catch(cwSysMessage e) {
	     	                    	con.rollback();
	     	                    	sess.setAttribute("PLAN_ITM_SESS", sess.getAttribute("PLAN_ITM_SESS_TEMP"));
	     	                    	sessUrlp.getGetItemSubmittedParams("");
	     							StringBuffer metaXML = new StringBuffer();
	     							metaXML.append(e.getErrorMessageXML(prof.label_lan));
	     							metaXML.append(sessUrlp.getSubmittedParamsListXML());
	     							metaXML.append("<training_plan>true</training_plan>");
	                            	dbTpTrainingPlan tpTp = new dbTpTrainingPlan();
	                            	tpTp.tpn_id = sessUrlp.plan_id;
	                            	tpTp.get(con);
	                            	tpTp.tpn_update_timestamp = sessUrlp.tpn_update_timestamp;
	                            	String xml_tp = tpPlanManagement.getPlanDetailsAsXML(con, tpTp); 
	                            	
	     							String result = formatXML(xml_tp + getFormInsItmXML(con, sess, sessUrlp, prof), metaXML.toString(), APPLYEASY, prof, sessUrlp.stylesheet);
	     							generalAsHtml(result.toString(), out, sessUrlp.stylesheet, prof.xsl_root);
	     							return;
	                             }
                        	 } 
                        }
	                    try{	               
	                        urlp.itm.itm_owner_ent_id = prof.root_ent_id;
	                        urlp.itm.getItemTypeInd(con);

	                        long tnd_id = urlp.tnd_parent_tnd_id;
	                        long parent_itm_id = 0;
	                        //if it is a run, get the parent item id from session
	                        if(urlp.itm.itm_run_ind || urlp.itm.itm_session_ind) {
	                            Long temp = (Long)sess.getAttribute(SESS_PARENT_ITM_ID);
	                            
	                            if(temp != null) {
	                                parent_itm_id = temp.longValue();
	                                urlp.itm.parent_itm_id = parent_itm_id;
	                            }else{
	                            	CommonLog.info("temp is null");
	                            }
	                        }
	                        //get template types
	                        ViewItemTemplate viItmTpl = new ViewItemTemplate();
	                        viItmTpl.ownerEntId = prof.root_ent_id;
	                        viItmTpl.itemType = urlp.itm.itm_type;
	                        viItmTpl.runInd = urlp.itm.itm_run_ind;
	                        viItmTpl.sessionInd = urlp.itm.itm_session_ind;
	                        String[] templateTypes = viItmTpl.getItemTypeTemplates(con);
	                        
	                        if(!wizbini.cfgTcEnabled) {
	                        	long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
	                        	urlp.itm.itm_tcr_id = sup_tcr_id;
	                        }


	                        //find out the template id
	                        long[] tpl_ids;
	                        // get own template for non-run item (for session, no need to add parent template)
	                        if(!urlp.itm.itm_run_ind) {
	                            tpl_ids = getTplIds(con, sess, urlp.itm.itm_type,
	                                                templateTypes, urlp.itm.itm_session_ind, prof.root_ent_id);
	                            urlp.itm.itm_app_approval_type = (String)sess.getAttribute(SESS_ITM_APP_APPROVAL_TYPE);
	                        }
	                        else {
	                            tpl_ids = getRunTplIds(con, sess, urlp.itm.itm_type,
	                                                    templateTypes, prof.root_ent_id,
	                                                    parent_itm_id);

	                            aeItem parent = new aeItem();
	                            parent.itm_id = parent_itm_id;
	                            parent.getItem(con);
	                            urlp.itm.itm_app_approval_type = parent.itm_app_approval_type;
	                            // for itm_content_def, inherit the value from parent item
								urlp.vColName.addElement("itm_content_def");
								urlp.vColType.addElement(DbTable.COL_TYPE_STRING);
								urlp.vColValue.addElement(parent.itm_content_def);
								
	                            // for itm_content_def, inherit the value from parent item
								urlp.vColName.addElement("itm_target_enrol_type");
								urlp.vColType.addElement(DbTable.COL_TYPE_STRING);
								urlp.vColValue.addElement(aeItem.ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER);
								
								//继承证书
								for (int i = 0; i <urlp.vColName.size(); i++) {
									if("itm_cfc_id".equals((String)urlp.vColName.get(i))){
										urlp.vColValue.set(i,parent.itm_cfc_id);
									}
								}
								
	                        }

	                        aeItem itm = urlp.itm;
	                        aeItemExtension itmExtension = urlp.itmExtension;
	                        //access control
	                        AcItem acitm = new AcItem(con);
	                        if(!acitm.hasInsPrivilege(prof.usr_ent_id, prof.current_role, tnd_id, prof.root_ent_id)) {
	                            throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
	                        }
	                        if (!itm.itm_run_ind && !itm.itm_session_ind){
//	                            if (acitm.isItmMgtAssistant(prof.usr_ent_id, prof.current_role)){
//	                                itm.itm_approval_status = aeItem.ITM_APPROVAL_STATUS_PREAPPROVE;
//	                                urlp.vColName.addElement("itm_approval_status");
//	                                urlp.vColType.addElement(DbTable.COL_TYPE_STRING);
//	                                urlp.vColValue.addElement(itm.itm_approval_status);
//	                            }
	                        	if(urlp.training_plan && urlp.tpn_create_run_ind){	                        		
	                        		if (itm.itm_code!=null && itm.isItemCodeExist(con, 0)){
	                        			throw new cwSysMessage(aeItem.ITM_CODE_EXIST_MSG, null, "itm_code");
	                        		}
	                        		urlp.saveGetItemSubmittedParams(clientEnc, static_env.ENCODING,"PLAN_ITM_SESS");
	                                if(bFileUpload) {
	                                	if("use_default_image".equalsIgnoreCase(multi.getParameter("field99__select"))){
	    	                        		String defaultPath = static_env.DOC_ROOT+ dbUtils.SLASH +static_env.DEFAULT_IMGLIG + dbUtils.SLASH +static_env.INI_ITM_DIR_UPLOAD_URL+ dbUtils.SLASH + multi.getParameter("default_image");
	    	                        		String saveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + itm.itm_id;
	    	                        		dbUtils.copyFile(defaultPath, saveDirPath);
	    	                        	} else {
		                                    String itm_icon = multi.getFilesystemName("itm_icon");
		    	                            procUploadedFiles(itm.itm_id, tmpUploadPath,itm_icon);
	    	                        	}
	    	                            
	    								String ies_top_icon = multi.getFilesystemName("ies_top_icon");
	    								procIesIconUploadedFiles(itm.itm_id, tmpUploadPath, ies_top_icon);
	    	                        }
	                        		response.sendRedirect(urlp.url_success);
	                        		return;
	                        	}
	                        }
                            if(itm.itm_qdb_ind) {
                                dbCourse cos = new dbCourse();
                                cos.res_lan = prof.label_lan;
                                cos.res_title = itm.itm_title;
                                cos.res_usr_id_owner = prof.usr_id;
                                cos.res_upd_user = prof.usr_id;
                                itm.insWZBCourse(con, prof, cos, tnd_id,
                                        templateTypes, tpl_ids,
                                        urlp.vColName, urlp.vColType, urlp.vColValue,
                                        urlp.vClobColName, urlp.vClobColValue);
                            }
                            else {
                                itm.ins(con, prof.root_ent_id, prof.usr_id, tnd_id,
                                    templateTypes, tpl_ids, prof,
                                    urlp.vColName, urlp.vColType, urlp.vColValue,
                                    urlp.vClobColName, urlp.vClobColValue);
                            }
                            if(urlp.iac_id_lst == null && prof.current_role.equals(AcObjective.TADM)){  
                              	urlp.iac_id_lst = new String[1];
                              	urlp.iac_id_lst[0]  = "TADM_1~"+prof.usr_ent_id;
                              }else if(urlp.iac_id_lst[0].equals("TADM_1")&& prof.current_role.equals(AcObjective.TADM)){
                              	urlp.iac_id_lst = new String[1];
                              	urlp.iac_id_lst[0]  = "TADM_1~"+prof.usr_ent_id;
                              }
                            itm.insExternalInfo(con,
                                                urlp.tnd_id_lst,
												urlp.tnd_id_lst_value,
                                                urlp.target_ent_group_lst,
                                                urlp.comp_target_ent_group_lst,
                                                urlp.cost_center_group_lst,
                                                urlp.iac_id_lst,
                                                urlp.mote,
                                                urlp.cm_lst,
                                                urlp.fgt_id_vec,
                                                urlp.fig_val_vec,
                                                prof.usr_id,
                                                prof.usr_ent_id,
                                                prof.root_ent_id,
                                                wizbini.cfgSysSetupadv.getDefaultTaId(),
                                                wizbini.cfgTcEnabled);
                            
                            itmExtension.ins(con, itm.itm_id, urlp.vExtensionColName, urlp.vExtensionColType, urlp.vExtensionColValue);
                            
	                        //save upload file
	                        if(bFileUpload) {
	                        	if("use_default_image".equalsIgnoreCase(multi.getParameter("field99__select"))){
	                        		String defaultPath = static_env.DOC_ROOT+ dbUtils.SLASH +static_env.DEFAULT_IMGLIG + dbUtils.SLASH +static_env.INI_ITM_DIR_UPLOAD_URL+ dbUtils.SLASH + multi.getParameter("default_image");
	                        		String saveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + itm.itm_id;
	                        		dbUtils.copyFile(defaultPath, saveDirPath);
	                        	} else {
	                        		String itm_icon = multi.getFilesystemName("itm_icon");
	                        		procUploadedFiles(itm.itm_id, tmpUploadPath,itm_icon);
	                        	}
	                            
								String ies_top_icon = multi.getFilesystemName("ies_top_icon");
								procIesIconUploadedFiles(itm.itm_id, tmpUploadPath, ies_top_icon);
	                        }
	                        //try to apply for the newly created course
	                        //if prof.current_role is a learner role
	                        if(urlp.apply_now_ind) {
	                            String[] lrn_role = {AccessControlWZB.ROL_EXT_ID_NLRN};
	                            if(lrn_role != null && lrn_role.length > 0) {
	                                for(int i=0; i<lrn_role.length; i++) {
	                                    if(prof.current_role.equalsIgnoreCase(lrn_role[i])) {
	                                        aeQueueManager qm = new aeQueueManager();
	                                        qm.insApplication(con, null, prof.usr_ent_id,
	                                                        itm.itm_id, prof, 0);
	                                        break;
	                                    }
	                                }
	                            }
	                        }
	                        itm_id_ = itm.itm_id;
	                        
	                        //如果是班级的话，复制课程的CPD时数设置给班级
	                        if(parent_itm_id!=0 && urlp.itm.itm_run_ind){
	                        	List<AeItemCPDItem> aeCpdList = CpdDbUtilsForOld.getAeItemCPDItem(parent_itm_id,con);
	                        	if(null!=aeCpdList && aeCpdList.size()>0){
	                        		AeItemCPDItem acpdItem = aeCpdList.get(0);
	                        		AeItemCPDItem newItem = new AeItemCPDItem();
	                        		newItem.setAci_accreditation_code(acpdItem.getAci_accreditation_code());
	                        		newItem.setAci_create_datetime(acpdItem.getAci_create_datetime());
	                        		newItem.setAci_create_usr_ent_id(acpdItem.getAci_create_usr_ent_id());
	                        		//newItem.setAci_ct_id(acpdItem.getAci_ct_id());
	                        		newItem.setAci_hours_end_date(acpdItem.getAci_hours_end_date());
	                        		newItem.setAci_itm_id(itm.itm_id);
	                        		newItem.setAci_update_datetime(acpdItem.getAci_update_datetime());
	                        		newItem.setAci_update_usr_ent_id(acpdItem.getAci_update_usr_ent_id());
	                        	    acgi_aci_id = CpdDbUtilsForOld.insertAeItemCPDItem(newItem,con);
	                        		List<AeItemCPDGourpItem> gItems = CpdDbUtilsForOld.getAeItemCPDGourpItem(acpdItem.getAci_itm_id(),acpdItem.getAci_id(),con);
	                        		for(AeItemCPDGourpItem gItem :gItems){
	                        			AeItemCPDGourpItem newgItem = new AeItemCPDGourpItem();
	                        			newgItem.setAcgi_cg_id(gItem.getAcgi_cg_id());
	                        			newgItem.setAcgi_aci_id((long) acgi_aci_id);
	                        			newgItem.setAcgi_itm_id(itm.itm_id);
	                        			newgItem.setAcgi_award_core_hours(gItem.getAcgi_award_core_hours());
	                        			newgItem.setAcgi_award_non_core_hours(gItem.getAcgi_award_non_core_hours());
	                        			newgItem.setAcgi_create_datetime(gItem.getAcgi_create_datetime());
	                        			newgItem.setAcgi_create_usr_ent_id(gItem.getAcgi_create_usr_ent_id());
	                        			newgItem.setAcgi_update_datetime(gItem.getAcgi_update_datetime());
	                        			newgItem.setAcgi_update_usr_ent_id(gItem.getAcgi_update_usr_ent_id());
	                        			CpdDbUtilsForOld.insertAeItemCPDGourpItem(newgItem,con);
	                        		}
	                        		
	                        	}
	                        }
                            // duplicate course content to the newly added class
                            if (urlp.itm.itm_run_ind) {
                                CourseContentUtils.propagateCourseContent(con, parent_itm_id, itm_id_, prof, false, wizbini.getFileUploadResDirAbs());
                            }
                            if(urlp.training_plan){
                            	dbTpTrainingPlan tpTp = new dbTpTrainingPlan();
                            	tpTp.tpn_id = urlp.plan_id;
                            	if (!tpTp.isLastUpd(con, urlp.tpn_update_timestamp)) {
                            		cwSysMessage e =  new cwSysMessage("TPN007");
                            		msgBox(MSG_ERROR, con, e, prof, urlp.url_failure, out);
                            		return;
                            	}    
                            	tpPlanManagement.updaePlanStatus(con, urlp.plan_id, prof);
                            }
	                    }catch(cwSysMessage e) {
	                    	con.rollback();	                    
							urlp.getGetItemSubmittedParams("");
							urlp.itm.itm_dummy_type = dummy_type;
							StringBuffer metaXML = new StringBuffer();
							metaXML.append(e.getErrorMessageXML(prof.label_lan));
							metaXML.append(urlp.getSubmittedParamsListXML());
							String xml_tp = "";
							if(urlp.training_plan) {
								sess.setAttribute("PLAN_ITM_SESS", sess.getAttribute("PLAN_ITM_SESS_TEMP"));
                            	dbTpTrainingPlan tpTp = new dbTpTrainingPlan();
                            	tpTp.tpn_id = urlp.plan_id;
                            	tpTp.get(con);
                            	tpTp.tpn_update_timestamp = urlp.tpn_update_timestamp;
                            	xml_tp = tpPlanManagement.getPlanDetailsAsXML(con, tpTp); 
                            	metaXML.append("<training_plan>true</training_plan>");
                            	if(urlp.itm.itm_run_ind || urlp.itm.itm_session_ind) {
	                            	Long temp2 = (Long)sess.getAttribute(SESS_PARENT_ITM_ID);
	                            	sess.removeAttribute(SESS_PARENT_ITM_ID);
	                            	sess.setAttribute(SESS_PARENT_ITM_ID, new Long(temp2.longValue()));
                            	}
							}
 							metaXML.append("<training_type>").append(urlp.training_type).append("</training_type>");
 							metaXML.append("<itm_integrated_ind>").append(urlp.itm.itm_integrated_ind).append("</itm_integrated_ind>");
							String result = formatXML(xml_tp + getFormInsItmXML(con, sess, urlp, prof), metaXML.toString(), APPLYEASY, prof, urlp.stylesheet);
							generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
						
							return;
                        }
                        urlp.clearGetItemSubmittedParams();
                        urlp.url_success = cwUtils.substituteURL(urlp.url_success, itm_id_);
                        String sysMsg="";
                        if(urlp.training_plan){
                        	sysMsg="TPN006";
                        }else{
                        	sysMsg=aeItem.ITM_ADD_OK;
                        }
                        con.commit();
                        cwSysMessage e = new cwSysMessage(sysMsg);
						aeItem itm = urlp.itm;
						
						ObjectActionLog log = new ObjectActionLog();
						log.setObjectId(itm.itm_id);
						log.setObjectCode(itm.itm_code);
						log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
						log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
						log.setObjectOptUserId(prof.getUsr_ent_id());
						log.setObjectActionTime(DateUtil.getCurrentTime());
						this.setItemLogTitle(itm, log, con);
						log.setObjectOptUserLoginTime(prof.login_date);
		                log.setObjectOptUserLoginIp(prof.ip);
						SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
						
						//如果是班级的话，复制课程的CPD时数设置给班级
                        if(itm.parent_itm_id!=0 && urlp.itm.itm_run_ind){
                        	List<AeItemCPDItem> aeCpdList = CpdDbUtilsForOld.getAeItemCPDItem(itm.parent_itm_id,con);
                        	if(null!=aeCpdList && aeCpdList.size()>0){
                        		//记录重要功能操作日志 copyCpt/d时数设置
                        		aeItem parentCourse = aeItem.getCourseByClassId(con, itm.itm_id);
                        		ObjectActionLog cpd_log = new ObjectActionLog();
                        		cpd_log.setObjectId((long) acgi_aci_id);
                        		cpd_log.setObjectCode(itm.itm_code);
                        		cpd_log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
                        		cpd_log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
                        		cpd_log.setObjectOptUserId(prof.getUsr_ent_id());
                        		cpd_log.setObjectActionTime(DateUtil.getCurrentTime());
                        		cpd_log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_COURSE_HOURS);
                        		cpd_log.setObjectOptUserLoginTime(prof.login_date);
                        		cpd_log.setObjectOptUserLoginIp(prof.ip);
                        		cpd_log.setObjectTitle(parentCourse.itm_title + "> " + itm.itm_title);
                        		SystemLogContext.saveLog(cpd_log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
        						
                        	}
                        }
//                        if (itm.itm_run_ind || urlp.training_plan){
							msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
//                        }
//                        else{
//                        	StringBuffer extMsg = new StringBuffer();
//                        	extMsg.append("<training_type>").append(urlp.training_type).append("</training_type>");
//                        	genMsgBox(MSG_STATUS, con, e, prof, urlp.url_success, out,urlp.stylesheet, extMsg.toString());
//                        }
                        //response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_upd_itm_rsv")) {
                        urlp.updItmRsv();

                        //access control
                        AcItem acitm = new AcItem(con);
                        if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                                  prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }
                        DbTable dbTab = new DbTable(con);
                        dbTab.upd("aeItem", urlp.vColName, urlp.vColType, urlp.vColValue,
                                  urlp.itm.itm_id, "itm_id");
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_upd_itm_workflow")) {
	                urlp.item(clientEnc, static_env.ENCODING);

	                //check access control
	                AcItem acitm = new AcItem(con);
	                if(wizbini.cfgTcEnabled && !acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
	                                            prof.current_role, prof.root_ent_id)) {
	                    throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
	                }

	                //update item workflow
	                urlp.itm.itm_upd_timestamp = urlp.itm_in_upd_timestamp;
	                urlp.itm.updAppApprovalType(con);
	                con.commit();
                    cwSysMessage e = new cwSysMessage(aeItem.ITM_CHANGE_WRK_OK);
                    msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                } else if (urlp.cmd.equals("ae_get_itm_batch") || urlp.cmd.equals("ae_get_itm_batch_xml")) {
					urlp.item(clientEnc, static_env.ENCODING);
					urlp.saveGetItemSubmittedParams(clientEnc, static_env.ENCODING);
					String metaXML = null;
					if (prof != null) {
						AcPageVariant acPageVariant = new AcPageVariant(con);
						acPageVariant.prof = prof;
						acPageVariant.instance_id = urlp.itm.itm_id;
						acPageVariant.ent_owner_ent_id = prof.root_ent_id;
						acPageVariant.ent_id = prof.usr_ent_id;
						acPageVariant.rol_ext_id = prof.current_role;
						acPageVariant.root_id = prof.root_id;
						acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
						acPageVariant.setWizbiniLoader(wizbini);

						metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
					}

					// add sso xml for sso query
					// metaXML += dbSSOLink.ssoLinkAsXML(prof.root_id, wizbini);
					urlp.itm.auto_enroll_interval = CourseEnrollScheduler.Auto_Enroll_Interval;
					String xml = "<item></item>";
					xml += "<training_type>COS</training_type>";
					xml += "<itm_exam_ind>"+ urlp.itm.itm_exam_ind +"</itm_exam_ind>";
					String result = formatXML(xml, metaXML, APPLYEASY, prof, urlp.stylesheet);
					if (urlp.cmd.equals("ae_get_itm_batch_xml")) {
						out.println(result);
					} else {
						String xsl_root = (prof == null) ? null : prof.xsl_root;
						generalAsHtml(result, out, urlp.stylesheet, xsl_root);
					}
				}else if (urlp.cmd.equals("ae_upd_itm_batch")) {
					urlp.item(clientEnc, static_env.ENCODING);
					aeItem itm = urlp.itm;
					// 从前台获取重读和自动积分是否做修改的信息
					String itm_bonus_ind = urlp.getParam("itm_bonus_ind");
					String itm_retake_ind = urlp.getParam("itm_retake_ind");
					String iscatalog = urlp.getParam("iscatalog");
					String itm_status = urlp.getParam("itm_status");
					String itm_access_type = urlp.getParam("itm_access_type");
					itm.upd_batch(con, prof, itm_bonus_ind, itm_retake_ind, iscatalog,urlp.tnd_ids_change_lst,itm_status,itm_access_type);

					con.commit();
					urlp.clearGetItemSubmittedParams();
					if (bMultiPart) {
						if (itm.itm_run_ind) {
							cwSysMessage e = new cwSysMessage(aeItem.ITM_UPDATE_OK2);
							msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
						} else {
							cwSysMessage e = new cwSysMessage(aeItem.ITM_UPDATE_OK);
							msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
						}
					} else {
						response.sendRedirect(urlp.url_success);
					}
				} 
                else if (urlp.cmd.equals("ae_upd_itm")) {
                        urlp.item(clientEnc, static_env.ENCODING);
                        urlp.mote(clientEnc, static_env.ENCODING);
                        urlp.itm.itm_owner_ent_id = prof.root_ent_id;
                        urlp.itm.getItemTypeInd(con);
                        //System.out.println("urlp.comp_target_ent_group_lst:" + urlp.comp_target_ent_group_lst);
                        aeItem itm = urlp.itm;
                        aeItemExtension itmExtension = urlp.itmExtension;
                        Timestamp itm_upd_timestamp = urlp.itm_in_upd_timestamp;
                        if(!wizbini.cfgTcEnabled) {
                        	//-1: if TC Management is disabled, it will not maintain itm_tcr_id in aeItem.
                        	urlp.itm.itm_tcr_id = -1;
                        }

                        //if item's training center changed,clear all Item Requirement
                        long old_itm_tcr_id = aeItem.getTcrId(con, urlp.itm.itm_id, prof.root_ent_id);
                        if ((old_itm_tcr_id != urlp.itm.itm_tcr_id) && wizbini.cfgTcEnabled) {
                            // del itemRequirement
                            DbItemRequirement itmReq = new DbItemRequirement();
                            itmReq.itrItmId = urlp.itm.itm_id;
                            itmReq.delByItmId(con);
                        }
                        //access control
	                    try{
	                        AcItem acitm = new AcItem(con);
							
							if(urlp.upd_itm_content_ind) {
								if(!acitm.hasMaintainPrivilege(itm.itm_id, prof.usr_ent_id,prof.current_role, prof.root_ent_id)) {
									throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
								}
							} else {
								if(wizbini.cfgTcEnabled && !acitm.hasUpdPrivilege(itm.itm_id, prof.usr_ent_id,
														  prof.current_role, prof.root_ent_id)) {
									throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
								}
							}
							
                            if(itm.itm_qdb_ind) {
                                dbCourse cos = new dbCourse();
                                cos.res_lan = prof.label_lan;
                                cos.res_title = itm.itm_title;
                                cos.res_usr_id_owner = prof.usr_id;
                                cos.res_upd_user = prof.usr_id;
                                cos.cos_itm_id = itm.itm_id;
                                cos.res_desc = urlp.cos_desc;
                                cos.cos_lic_key = urlp.cos_lic_key;
								if( !itm.isLastUpd(con, itm_upd_timestamp) ) {
									throw new cwSysMessage(aeItem.ITM_INVALID_TIMESTAMP, null, "invalid_timestamp");
								}
                                itm.updWZBCourse(con, prof, cos, itm_upd_timestamp,
                                                 urlp.vColName, urlp.vColType, urlp.vColValue,
                                                 urlp.vClobColName, urlp.vClobColValue);
                                if(urlp.iac_id_lst == null && prof.current_role.equals(AcObjective.TADM)){  
                                	urlp.iac_id_lst = new String[1];
                                	urlp.iac_id_lst[0]  = "TADM_1~"+prof.usr_ent_id;
                                }else if(urlp.iac_id_lst[0].equals("TADM_1")&& prof.current_role.equals(AcObjective.TADM)){
                                	urlp.iac_id_lst = new String[1];
                                	urlp.iac_id_lst[0]  = "TADM_1~"+prof.usr_ent_id;
                                }
                                itm.saveExternalInfo(con,
                                                     urlp.tnd_id_lst,
													 urlp.tnd_id_lst_value,
                                                     urlp.target_ent_group_lst,
                                                     urlp.comp_target_ent_group_lst,
                                                     urlp.cost_center_group_lst,
                                                     urlp.iac_id_lst,
                                                     urlp.mote,
                                                     urlp.cm_lst,
                                                     urlp.fgt_id_vec,
                                                     urlp.fig_val_vec,
                                                     prof.usr_id,
                                                     prof.usr_ent_id,
                                                     prof.root_ent_id,
                                                     wizbini.cfgSysSetupadv.getDefaultTaId(),
                                                     wizbini.cfgTcEnabled);
                                itmExtension.upd(con, itm.itm_id, urlp.vExtensionColName, urlp.vExtensionColType, urlp.vExtensionColValue);
                            }
                            else {
								if( !itm.isLastUpd(con, itm_upd_timestamp) ) {
									throw new cwSysMessage(aeItem.ITM_INVALID_TIMESTAMP, null, "invalid_timestamp");
								}
                                itm.upd(con, prof.root_ent_id, prof.usr_id, itm_upd_timestamp,
                                        urlp.vColName, urlp.vColType, urlp.vColValue,
                                        urlp.vClobColName, urlp.vClobColValue);
                                if(urlp.iac_id_lst == null && prof.current_role.equals(AcObjective.TADM)){  
                                 	urlp.iac_id_lst = new String[1];
                                 	urlp.iac_id_lst[0]  = "TADM_1~"+prof.usr_ent_id;
                                 }else if(urlp.iac_id_lst[0].equals("TADM_1")&& prof.current_role.equals(AcObjective.TADM)){
                                 	urlp.iac_id_lst = new String[1];
                                 	urlp.iac_id_lst[0]  = "TADM_1~"+prof.usr_ent_id;
                                 }
                                itm.saveExternalInfo(con,
                                                     urlp.tnd_id_lst,
													 urlp.tnd_id_lst_value,
                                                     urlp.target_ent_group_lst,
                                                     urlp.comp_target_ent_group_lst,
                                                     urlp.cost_center_group_lst,
                                                     urlp.iac_id_lst,
                                                     urlp.mote,
                                                     urlp.cm_lst,
                                                     urlp.fgt_id_vec,
                                                     urlp.fig_val_vec,
                                                     prof.usr_id,
                                                     prof.usr_ent_id,
                                                     prof.root_ent_id,
                                                     wizbini.cfgSysSetupadv.getDefaultTaId(),
                                                     wizbini.cfgTcEnabled);                                                     
                                itmExtension.upd(con, itm.itm_id, urlp.vExtensionColName, urlp.vExtensionColType, urlp.vExtensionColValue);
                            }
	                        if(bFileUpload) {
	                        	delOldFiles(itm.itm_id,urlp.vFileName);
	                        	if("use_default_image".equalsIgnoreCase(multi.getParameter("field99__select"))){
	                        		String defaultPath = static_env.DOC_ROOT+ dbUtils.SLASH +static_env.DEFAULT_IMGLIG + dbUtils.SLASH +static_env.INI_ITM_DIR_UPLOAD_URL+ dbUtils.SLASH + multi.getParameter("default_image");
	                        		String saveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + itm.itm_id;
	                        		dbUtils.copyFile(defaultPath, saveDirPath);
	                        	} else {
	                        		String itm_icon = multi.getFilesystemName("itm_icon");
	                        		procUploadedFiles(itm.itm_id, tmpUploadPath,itm_icon);
	                        	}
	
								String ies_top_icon = multi.getFilesystemName("ies_top_icon");
								procIesIconUploadedFiles(itm.itm_id, tmpUploadPath, ies_top_icon);
	                        }
						}catch(cwSysMessage e) {
							con.rollback();
							urlp.getGetItemSubmittedParams("");
							StringBuffer metaXML = new StringBuffer();
							if(prof != null && urlp.stylesheet != null) {
								AcPageVariant acPageVariant = new AcPageVariant(con);
								acPageVariant.prof = prof;
								acPageVariant.instance_id = urlp.itm.itm_id;
								acPageVariant.ent_owner_ent_id = prof.root_ent_id;
								acPageVariant.ent_id = prof.usr_ent_id;
								acPageVariant.rol_ext_id = prof.current_role;
								acPageVariant.root_id = prof.root_id;
								acPageVariant.setWizbiniLoader(wizbini);
								metaXML.append(acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet)));
							}
							metaXML.append(e.getErrorMessageXML(prof.label_lan));
							metaXML.append(urlp.getSubmittedParamsListXML());
							String result = formatXML(getItemXML(con, urlp, prof)+ aeItem.genItemActionNavXML(con, urlp.itm.itm_id, prof), metaXML.toString(), APPLYEASY, prof, urlp.stylesheet);
							generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
							return;
                        }
                        con.commit();
						urlp.clearGetItemSubmittedParams();
						
						ObjectActionLog log = new ObjectActionLog();
						log.setObjectId(itm.itm_id);
						log.setObjectCode(itm.itm_code);
						log.setObjectAction(ObjectActionLog.OBJECT_ACTION_UPD);
						log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
						log.setObjectOptUserId(prof.getUsr_ent_id());
						log.setObjectActionTime(DateUtil.getCurrentTime());
						this.setItemLogTitle(itm, log, con);
						log.setObjectOptUserLoginTime(prof.login_date);
		                log.setObjectOptUserLoginIp(prof.ip);
						SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);

                        if(bMultiPart) {
                        	boolean hasLesson = false;
        					aeItemLesson lesson = new aeItemLesson();
        					long rowCount = lesson.getLessonCountByItmId(con ,itm.itm_id);
        					if(rowCount>0){
        						hasLesson = true;
        					}
                        	if(hasLesson){
                                cwSysMessage e = new cwSysMessage(aeItem.ITM_UPDATE_OK2);
                                msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                            } else {
                                cwSysMessage e = new cwSysMessage(aeItem.ITM_UPDATE_OK);
                                msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                            }
                        }
                        else {
                            response.sendRedirect(urlp.url_success);
                        }
                } else if (urlp.cmd.equals("share_itm")) {
                    urlp.item(clientEnc, static_env.ENCODING);
                    AcItem acitm = new AcItem(con);
                    if(wizbini.cfgTcEnabled && !acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                            prof.current_role, prof.root_ent_id)) {
                        throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                    }
                    urlp.itm.setItemShareStatus(con);
                    response.sendRedirect(urlp.url_success);
//                    cwSysMessage e = new cwSysMessage("GEN003");
//                    msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                } else if (urlp.cmd.equals("ae_del_itm")) {
                        urlp.item(clientEnc, static_env.ENCODING);
                        long[] itm_id_lst = urlp.itm_id_lst;
                        Timestamp[] itm_upd_timestamp_lst = urlp.itm_in_upd_timestamp_lst;
                        aeItem itm = urlp.itm;

                        for(int i=0; i<itm_id_lst.length; i++) {
                            itm.itm_id = itm_id_lst[i];

                            //access control
                            AcItem acitm = new AcItem(con);
                            if(wizbini.cfgTcEnabled && !acitm.hasUpdPrivilege(itm.itm_id, prof.usr_ent_id,
                                                    prof.current_role, prof.root_ent_id)) {
                                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                            }
                            ModulePrerequisiteManagement itm_pre_mod = new ModulePrerequisiteManagement();
                            itm_pre_mod.delCosModPrerequisite(con,itm.itm_id);
                            itm.itm_type = itm.getItemType(con);
                            String plan_code = itm.getItemPlanCode(con);
                            if (plan_code != null) {
                            	dbTpTrainingPlan dbTp = new dbTpTrainingPlan();
                            	dbTp.tpn_code = plan_code;
                            	long tpn_id = dbTp.getTpPlanIdByCode(con);
                            	dbTp.tpn_id = tpn_id;
                            	dbTp.get(con);
                            	dbTp.tpn_status = dbTpTrainingPlan.TPN_STATUS_APPROVED;
                            	dbTp.tpn_update_timestamp = cwSQL.getTime(con);
                            	dbTp.tpn_update_usr_id = prof.usr_id;
                            	dbTp.upd_status(con);
                            }
                            itm.getItem(con);
                            
        					ObjectActionLog log = new ObjectActionLog();
        					log.setObjectId(itm.itm_id);
        					log.setObjectCode(itm.itm_code);
                            log.setObjectAction(ObjectActionLog.OBJECT_ACTION_DEL);
        					log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
        					log.setObjectOptUserId(prof.getUsr_ent_id());
        					log.setObjectActionTime(DateUtil.getCurrentTime());
        					this.setItemLogTitle(itm, log, con);
        					log.setObjectOptUserLoginTime(prof.login_date);
        	                log.setObjectOptUserLoginIp(prof.ip);
                            if(itm.getQdbInd(con)) {
                                dbCourse cos = new dbCourse();
                                cos.cos_itm_id = itm.itm_id;
                                itm.delWZBCourse(con, prof, cos, itm_upd_timestamp_lst[i]);
                            }
                            else {
                                itm.delItem(con, prof.root_ent_id, itm_upd_timestamp_lst[i], prof);
                            }
                            //如果开启了cpd功能 会删除课程相关的CPD数据
                    		if(AccessControlWZB.hasCPDFunction()){
                    			CpdDbUtilsForOld.delCourseCpd(con, itm.itm_id);
                    		}
                            SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                        }
                        con.commit();
                       
                        for(int i=0; i<itm_id_lst.length; i++) {
                            dbUtils.delDir(static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + Long.toString(itm_id_lst[i]));
                        }

                        response.sendRedirect(urlp.url_success);
                }
                else if( urlp.cmd.equalsIgnoreCase("ae_get_itm_credit") || urlp.cmd.equalsIgnoreCase("ae_get_itm_credit_xml") ) {

                    urlp.itmCredit(clientEnc, static_env.ENCODING);
                    aeItemFigure ifg = new aeItemFigure();
                    StringBuffer xml = new StringBuffer();
                    xml.append("<item id=\"").append(urlp.itm_id).append("\">");
                    xml.append(aeItem.getNavAsXML(con, urlp.itm_id));
                    /*
                    xml.append(aeFigureType.getAllAsXML(con, prof.root_ent_id));
                    xml.append(ifg.getDetailAsXML(con, urlp.itm_id, urlp.credit_type, urlp.credit_subtype, urlp.sort_by, urlp.order_by));
                    */
                    xml.append(ifg.getAllFigure(con, prof.root_ent_id, urlp.itm_id));
                    xml.append("</item>");
                    String result = formatXML(xml.toString(), prof);

                    if (urlp.cmd.equals("ae_get_itm_credit_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }

                }
                else if( urlp.cmd.equalsIgnoreCase("ae_get_itm_credit_value") || urlp.cmd.equalsIgnoreCase("ae_get_itm_credit_value_xml") ) {

                    urlp.itmCreditValue(clientEnc, static_env.ENCODING);
                    aeItemFigure ifg = new aeItemFigure();
                    String xml = aeFigureType.getAllAsXML(con, prof.root_ent_id);
                           xml += ifg.asXML(con, urlp.dbFig.fig_id);
                    xml = formatXML(xml, prof);

                    if (urlp.cmd.equals("ae_get_itm_credit_value_xml")) {
                        out.println(xml);
                    } else {
                        generalAsHtml(xml, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if( urlp.cmd.equalsIgnoreCase("ae_ins_itm_credit_value") ) {

                    urlp.itmCreditValue(clientEnc, static_env.ENCODING);
                    aeItemFigure ifg = new aeItemFigure();
                    ifg.ins(con, prof.usr_id, urlp.itm_id, urlp.dbFig);
                    ifg.insChildFigure(con, prof, urlp.itm_id, urlp.dbFig);
                    con.commit();
                    response.sendRedirect(urlp.url_success);

                }
                else if( urlp.cmd.equalsIgnoreCase("ae_upd_itm_credit_value") ) {

                    urlp.itmCreditValue(clientEnc, static_env.ENCODING);
                    aeItemFigure ifg = new aeItemFigure();
                    ifg.updItemFigure(con, prof, urlp.itm_id, urlp.ict_id_vec, urlp.icv_val_vec);
                    con.commit();
                    response.sendRedirect(urlp.url_success);

                }
                else if( urlp.cmd.equalsIgnoreCase("ae_upd_itm_run_credit_value") ) {

                    urlp.itmCreditValue(clientEnc, static_env.ENCODING);
                    aeItemFigure ifg = new aeItemFigure();
                    ifg.updItemRunFigure(con, prof, urlp.itm_id, urlp.ict_id_vec, urlp.icv_val_vec);
                    con.commit();
                    response.sendRedirect(urlp.url_success);

                }
                /*
                else if( urlp.cmd.equalsIgnoreCase("ae_del_itm_credit_value") ) {

                    urlp.itmCreditValue(clientEnc, static_env.ENCODING);
                    aeItemAccreditation iad = new aeItemAccreditation();
                    iad.delRelatedItemAccreditation(con, urlp.itm_id, urlp.icv_id_vec);
                    iad.del(con, urlp.itm_id, urlp.icv_id_vec);
                    con.commit();
                    response.sendRedirect(urlp.url_success);

                }
                */
                else if( urlp.cmd.equalsIgnoreCase("ae_get_all_itm_credits") || urlp.cmd.equalsIgnoreCase("ae_get_all_itm_credits_xml") ) {

                    String xml = formatXML( aeFigureType.getAllAsXML(con, prof.root_ent_id), prof );
                    if (urlp.cmd.equals("ae_get_all_itm_credits_xml")) {
                            out.println(xml);
                        } else {
                            generalAsHtml(xml, out, urlp.stylesheet, prof.xsl_root);
                        }
                }
                else if (urlp.cmd.equals("ae_upd_itm_status")) {
                    urlp.item(clientEnc, static_env.ENCODING);
                    aeItem itm = urlp.itm;
                    String[] itm_status_lst = urlp.itm_status_lst;
                    String[] itm_mobile_ind_lst = urlp.itm_mobile_ind_lst;
                    long[] itm_id_lst = urlp.itm_id_lst;
                    Timestamp[] itm_upd_timestamp_lst  = urlp.itm_in_upd_timestamp_lst;

                    //access control
                    for(int i=0; i<itm_id_lst.length; i++) {
                        AcItem acitm = new AcItem(con);
                        if(wizbini.cfgTcEnabled && !acitm.hasUpdPrivilege(itm_id_lst[i], prof.usr_ent_id,
                                                  prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }
                    }
                    itm.updItemStatus(con, itm_id_lst, itm_status_lst, itm_upd_timestamp_lst,
                                          prof.root_ent_id, prof.usr_id,prof.usr_ent_id,itm_mobile_ind_lst);
                    con.commit();
                    itm.get(con);
                    
					ObjectActionLog log = new ObjectActionLog();
					log.setObjectId(itm.itm_id);
					log.setObjectCode(itm.itm_code);
					if(itm.itm_status.equalsIgnoreCase(itm.ITM_STATUS_OFF)){
                    	log.setObjectAction(ObjectActionLog.OBJECT_ACTION_CANCLE_PUB);
                    }else{
                    	log.setObjectAction(ObjectActionLog.OBJECT_ACTION_PUB);
                    }
					log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
					log.setObjectOptUserId(prof.getUsr_ent_id());
					log.setObjectActionTime(DateUtil.getCurrentTime());
					this.setItemLogTitle(itm, log, con);
					log.setObjectOptUserLoginTime(prof.login_date);
	                log.setObjectOptUserLoginIp(prof.ip);
					SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                    
                    if(urlp.show_sys_msg) {
                        cwSysMessage e = new cwSysMessage("GEN003");
                        msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                    } else {
                    	response.sendRedirect(urlp.url_success);
                    }
                } 
                else if (urlp.cmd.equals("ae_upd_auto_enrol_type")) {
                    urlp.item(clientEnc, static_env.ENCODING);
                    aeItem itm = urlp.itm;

                    //access control
                    AcItem acitm = new AcItem(con);
                    if(wizbini.cfgTcEnabled && !acitm.hasUpdPrivilege(itm.itm_id, prof.usr_ent_id,
                                              prof.current_role, prof.root_ent_id)) {
                        throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                    }

                    itm.updAutoEnrolType(con, urlp.itm_in_upd_timestamp, prof.usr_id);
                    con.commit();
                    cwSysMessage e = new cwSysMessage("GEN003");
                    msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                }
                else if (urlp.cmd.equals("ae_get_itm_acc") || urlp.cmd.equals("ae_get_itm_acc_xml")) {
                        StringBuffer result = new StringBuffer();
                        urlp.itemAccess();

                        //no access control

                        result.append(formatXML(urlp.iac.getEntityAsXML(con, urlp.iac_acc_id_lst, urlp.tnd_id, prof.root_ent_id),prof));
                        if (urlp.cmd.equals("ae_get_itm_acc_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                        }
                }
                // before inserting a run of an item, get all the templates of this item
                // and put them into the session for later use
                else if (urlp.cmd.equals("ae_prep_ins_run")) {
                    urlp.item(clientEnc, static_env.ENCODING);

                    //no access control
                    /*
                    aeItemTemplate tplUsage = new aeItemTemplate(con, urlp.itm.itm_id);
                    Hashtable tplList = tplUsage.getUsage();
                    */
                    ViewItemTemplate viItmTpl = new ViewItemTemplate();
                    viItmTpl.itemId = urlp.itm.itm_id;
                    Hashtable tplList = viItmTpl.getUsage(con);
                    Enumeration tplTypeList = tplList.keys();
                    while (tplTypeList.hasMoreElements()) {
                        String tplType = (String)tplTypeList.nextElement();
                        sess.setAttribute(tplType, tplList.get(tplType));
                    }

                    response.sendRedirect(urlp.url_success);
                }/*
                else if (urlp.cmd.equals("ae_save_itm_role_acc")) {
                        urlp.itemAccess();

                        //no access control

                        urlp.iac.saveByRole(con, urlp.iac_ecdn_lst, urlp.iac_ncdn_lst, urlp.iac_nist_lst);
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }*/
                else if (urlp.cmd.equals("ae_search_acc") || urlp.cmd.equals("ae_search_acc_xml")) {
                        sess = request.getSession(true);
                        urlp.getSearchItem(clientEnc, static_env.ENCODING);
                        aeSearch aes = urlp.aes;

                        //no access control

                        StringBuffer search_result = new StringBuffer();
                        boolean flag = aes.getSearchResultAsXML(con,sess,search_result,prof);
                        StringBuffer result = new StringBuffer(formatXML(search_result.toString(),prof));
                        if (urlp.cmd.equals("ae_search_acc_xml")) {
                            out.println(result.toString());
                        } else {
                            if(flag)
                                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                            else
                                generalAsHtml(result.toString(), out, urlp.no_result_stylesheet, prof.xsl_root);
                        }
                }
                else if (urlp.cmd.equalsIgnoreCase("get_os_oi") || urlp.cmd.equalsIgnoreCase("get_os_oi_xml") ) {
                    sess = request.getSession(true);
                    urlp.acnTransaction(clientEnc, static_env.ENCODING);
                    //convert input usr_ent_id to usr_id
                    try {
                        if(urlp.axn.axn_create_usr_id != null && urlp.axn.axn_create_usr_id.length() >0 )
                            urlp.axn.axn_create_usr_id = dbRegUser.usrEntId2UsrId(con, Long.parseLong(urlp.axn.axn_create_usr_id));
                    }
                    catch(NumberFormatException e) {
                        //do nothing
                    }

                    //no access control

                    StringBuffer result = new StringBuffer(formatXML(aeOpenItem.getOSOpenItem(con,sess,urlp.ent_id,urlp.acn_type),prof));
                    if(urlp.cmd.equalsIgnoreCase("get_os_oi_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equalsIgnoreCase("get_back_os_oi") || urlp.cmd.equalsIgnoreCase("get_back_os_oi_xml") ) {
                    sess = request.getSession(true);
                    //urlp.acnTransaction(clientEnc, static_env.ENCODING);

                    //no access control

                    StringBuffer result = new StringBuffer(formatXML(aeOpenItem.getBackOSOpenItem(con,sess),prof));
                    if(urlp.cmd.equalsIgnoreCase("get_back_os_oi_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equalsIgnoreCase("get_oi_axn") || urlp.cmd.equalsIgnoreCase("get_oi_axn_xml") ) {

                    urlp.acnTransaction(clientEnc, static_env.ENCODING);
                    //convert input usr_ent_id to usr_id
                    try {
                        if(urlp.axn.axn_create_usr_id != null && urlp.axn.axn_create_usr_id.length()>0 )
                            urlp.axn.axn_create_usr_id = dbRegUser.usrEntId2UsrId(con, Long.parseLong(urlp.axn.axn_create_usr_id));
                    }
                    catch(NumberFormatException e) {
                        //do nothing
                    }

                    //no access control

                    StringBuffer result = new StringBuffer(formatXML(aeOpenItem.getOSOpenItem(con,null,urlp.ent_id,urlp.acn_type)+aeOpenItem.checkTransactionHistory(con,urlp.ent_id,urlp.acn_type),prof));
                    if(urlp.cmd.equalsIgnoreCase("get_oi_axn_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("chk_axn") || urlp.cmd.equalsIgnoreCase("chk_axn_xml") ) {
                    sess = request.getSession(true);
                    urlp.acnTransaction(clientEnc, static_env.ENCODING);

                

                    //convert input usr_ent_id to usr_id
                    try {
                        if(urlp.axn.axn_create_usr_id != null && urlp.axn.axn_create_usr_id.length() >0 )
                            urlp.axn.axn_create_usr_id = dbRegUser.usrEntId2UsrId(con, Long.parseLong(urlp.axn.axn_create_usr_id));
                    }
                    catch(NumberFormatException e) {
                        //do nothing
                    }

                    urlp.axn.setSessionValue(con,sess,prof);
                    StringBuffer paymentStringBuffer = new StringBuffer();
                    urlp.axn.paymentValidation(con,prof,paymentStringBuffer,false);
                    StringBuffer result = new StringBuffer(formatXML(paymentStringBuffer.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("chk_axn_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("cmt_axn") || urlp.cmd.equalsIgnoreCase("cmt_axn_xml")) {
                    sess = request.getSession(true);
                    urlp.acnTransaction(clientEnc, static_env.ENCODING);

                 

                    //convert input usr_ent_id to usr_id
                    try {
                        if(urlp.axn.axn_create_usr_id != null && urlp.axn.axn_create_usr_id.length() >0 )
                            urlp.axn.axn_create_usr_id = dbRegUser.usrEntId2UsrId(con, Long.parseLong(urlp.axn.axn_create_usr_id));
                    }
                    catch(NumberFormatException e) {
                        //do nothing
                    }

                    aeAccountTransaction axn = new aeAccountTransaction();
                    urlp.axn.getSessionValue(sess);
                    StringBuffer paymentStringBuffer = new StringBuffer();
                    boolean flag = urlp.axn.commitOfflinePayment(con,prof,paymentStringBuffer);
                    StringBuffer result = new StringBuffer(formatXML(paymentStringBuffer.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("cmt_axn_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                    if(flag)
                        con.commit();
                    else
                        con.rollback();
                }
                else if(urlp.cmd.equalsIgnoreCase("cmt_on_axn") || urlp.cmd.equalsIgnoreCase("cmt_on_axn_xml")) {
                    sess = request.getSession(true);
                    urlp.acnTransaction(clientEnc, static_env.ENCODING);

                 

                    //convert input usr_ent_id to usr_id
                    try {
                        if(urlp.axn.axn_create_usr_id != null && urlp.axn.axn_create_usr_id.length() >0 )
                            urlp.axn.axn_create_usr_id = dbRegUser.usrEntId2UsrId(con, Long.parseLong(urlp.axn.axn_create_usr_id));
                    }
                    catch(NumberFormatException e) {
                        //do nothing
                    }

                    aeAccountTransaction axn = new aeAccountTransaction();
                    urlp.axn.setSessionValue(con,sess,prof);
                    urlp.axn.getSessionValue(sess);
                    StringBuffer paymentStringBuffer = new StringBuffer();
                    boolean flag = urlp.axn.commitOnlinePayment(con,prof,paymentStringBuffer);
                    if(!flag) {
                        StringBuffer result = new StringBuffer(formatXML(paymentStringBuffer.toString(),prof));
                        if(urlp.cmd.equalsIgnoreCase("cmt_on_axn_xml")) {
                            out.println(result.toString());
                        } else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet_fail, prof.xsl_root);
                        }
                    } else {
                        StringBuffer result = new StringBuffer(formatXML(urlp.axn.formData(con,static_env.CoNo),prof));
                        if(urlp.cmd.equalsIgnoreCase("cmt_on_axn_xml")) {
                            out.println(result.toString());
                        }else {
                            generalAsHtml(result.toString(), out, urlp.stylesheet_success, prof.xsl_root);
                        }
                    }
                    con.commit();
                } else if(urlp.cmd.equalsIgnoreCase("ae_send_notify")){
                    urlp.send_notify(clientEnc, static_env.ENCODING);
                    //long[] ent_ids = dbUserGroup.constructEntId(con, urlp.rec_ent_ids);
                    //long[] cc_ent_ids = dbUserGroup.constructEntId(con, urlp.cc_ent_ids);
                    //long[] ent_ids = dbUtils.string2LongArray(urlp.rec_ent_ids, "~");
                    //long[] cc_ent_ids = dbUtils.string2LongArray(urlp.cc_ent_ids, "~");
                    long[] ent_ids;
                    long[] cc_ent_ids;

                    if( urlp.rec_ent_ids == null || urlp.rec_ent_ids.length() == 0 )
                        ent_ids = new long[0];
                    else
                        ent_ids = dbUtils.string2LongArray(urlp.rec_ent_ids, "~");

                    if( urlp.cc_ent_ids == null || urlp.cc_ent_ids.length() == 0 )
                        cc_ent_ids = new long[0];
                    else
                        cc_ent_ids = dbUtils.string2LongArray(urlp.cc_ent_ids, "~");

                    Hashtable argsTable = new Hashtable();
                    argsTable.put("ent_ids", dbUtils.longArray2String(ent_ids,"~"));
                    argsTable.put("cc_ent_ids", dbUtils.longArray2String(cc_ent_ids,"~"));
                    argsTable.put("itm_id", Long.toString(urlp.itm_id));
                    argsTable.put("ji_status", Integer.toString(urlp.notify_status));
                    argsTable.put("msg_datetime", urlp.msg_datetime.toString());
                    urlp.url_redirect = cwUtils.getRealPath(request, urlp.url_redirect);
                    //urlp.url_redirect += "&ent_ids=" + dbUtils.longArray2String(ent_ids,"~")
                    //                  + "&cc_ent_ids=" + dbUtils.longArray2String(cc_ent_ids,"~")
                    //                  + "&ji_status=" + urlp.notify_status
                    //                  + "&itm_id=" + urlp.itm_id
                    //                  + "&msg_datetime=" + urlp.msg_datetime;

                    aeApplication.updNotifyStatus(con, urlp.itm_id, ent_ids, urlp.notify_status, urlp.msg_datetime);

                    String returnMessage;
                    try{
                        returnMessage = dbUtils.urlRedirect( urlp.url_redirect, request, argsTable, wizbini.cfgSysSetupadv.getEncoding());
                    }catch(Exception e) {
                        throw new cwException("Failed to insert recipient : " + e);
                    }

                    if( (returnMessage.trim()).equalsIgnoreCase("INSERT SUCCESS") ) {
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                    } else {
                        con.rollback();
                        cwSysMessage e = new cwSysMessage("XMG002");
                        msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                    }

                    return;

                } else if( urlp.cmd.equalsIgnoreCase("ae_notify_page")
                       ||  urlp.cmd.equalsIgnoreCase("ae_notify_page_xml") ){
                    int ji_status = 0;
                    int reminder_status = 0;
                    long itm_id = 0;
                    String rec_ent_id_str;
                    String cc_ent_id_str;
                    Timestamp send_date = null;
                    Vector pickedRecVec = new Vector();
                    Vector pickedCCVec  = new Vector();
                    urlp.notify_page(clientEnc, static_env.ENCODING);

                    //new access control

                    if( !urlp.flag ) {
                        // clear session
                        sess.setAttribute("REC_ENT_ID", "");
                        sess.setAttribute("CC_ENT_ID", "");
                        sess.setAttribute("JI_STATUS", new Integer(0));
                        sess.setAttribute("REMINDER_STATUS", new Integer(0));
                        sess.setAttribute("SEND_DATE", "");
                        sess.setAttribute("ITEM_ID", new Long(0));
                        itm_id = urlp.itm_id;
                    } else {
                        // get from session
                        rec_ent_id_str = (String)sess.getAttribute("REC_ENT_ID");
                        pickedRecVec   = dbUtils.convert2Vec(rec_ent_id_str + "~", "~");

                        cc_ent_id_str  = (String)sess.getAttribute("CC_ENT_ID");
                        pickedCCVec    = dbUtils.convert2Vec(cc_ent_id_str + "~", "~");

                        try{
                            ji_status = ((Integer)sess.getAttribute("JI_STATUS")).intValue();
                        }catch(NumberFormatException e) {
                            ji_status = 0;
                        }

                        try{
                            reminder_status = ((Integer)sess.getAttribute("REMINDER")).intValue();
                        }catch(NumberFormatException e) {
                            reminder_status = 0;
                        }

                        try{
                            send_date = (Timestamp)sess.getAttribute("SEND_DATE");
                        }catch(ClassCastException e) {
                            send_date = null;
                        }

                        try{
                            itm_id = ((Long)sess.getAttribute("ITEM_ID")).longValue();
                        }catch(NumberFormatException e) {
                            itm_id = 0;
                        }
                    }
                    StringBuffer xml = new StringBuffer();
                    xml.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>").append(dbUtils.NEWL);
                    xml.append("<item id=\"").append(itm_id).append("\">").append(dbUtils.NEWL);
                    xml.append("<JI_STATUS>").append(ji_status).append("</JI_STATUS>").append(dbUtils.NEWL);
                    xml.append("<REMINDER_STATUS>").append(reminder_status).append("</REMINDER_STATUS>").append(dbUtils.NEWL);
                    xml.append("<SEND_DATE>");
                    if(send_date != null)
                        xml.append(send_date);
                    xml.append("</SEND_DATE>").append(dbUtils.NEWL);
                    xml.append("<PICKED_RECIPIENT>").append(dbUtils.NEWL);
                        xml.append(dbUserGroup.getEntityXml(con, pickedRecVec));
                    xml.append("</PICKED_RECIPIENT>").append(dbUtils.NEWL);
                    xml.append("<PICKED_CC>").append(dbUtils.NEWL);
                        xml.append(dbUserGroup.getEntityXml(con, pickedCCVec));
                    xml.append("</PICKED_CC>").append(dbUtils.NEWL);
                    xml.append("</item>");

                    if(urlp.cmd.equalsIgnoreCase("ae_notify_page_xml")) {
                        out.println(xml.toString());
                    } else {
                        generalAsHtml(xml.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }

                } else if(urlp.cmd.equalsIgnoreCase("ae_ins_ctb")) {
                    urlp.codeTable(clientEnc, static_env.ENCODING);

                    //no access control
					
                    //urlp.ctb.ins(con, prof.usr_id);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_del_ctb")) {
                    urlp.codeTable(clientEnc, static_env.ENCODING);

                    //no access control

                    for(int i=0; i<urlp.ctb_id_lst.length; i++) {
                        urlp.ctb.ctb_id = urlp.ctb_id_lst[i];
                        urlp.ctb.del(con, urlp.ctb_upd_timestamp_lst[i]);
                    }
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                /*else if(urlp.cmd.equalsIgnoreCase("ae_upd_ctb")) {
                    urlp.codeTable(clientEnc, static_env.ENCODING);

                    //no access control

                    urlp.ctb.upd(con, prof.usr_id, urlp.ctb_in_upd_timestamp);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }*/
 				//auto_enroll_ind
				else if(urlp.cmd.equalsIgnoreCase("auto_enroll_ind") || urlp.cmd.equalsIgnoreCase("auto_enroll_ind_xml")) {
					StringBuffer xmlBuf = new StringBuffer(2500);
					xmlBuf.append("<auto_enroll_ind>").append(dbUtils.NEWL);
					xmlBuf.append("</auto_enroll_ind>").append(dbUtils.NEWL);
					StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
					if(urlp.cmd.equalsIgnoreCase("auto_enroll_ind_xml")) {
						out.println(result.toString());
					} else {
						generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
					}					
					
				}
				//auto_enroll_ind				               
                else if(urlp.cmd.equalsIgnoreCase("ae_prep_ins_ctb") || urlp.cmd.equalsIgnoreCase("ae_prep_ins_ctb_xml")) {
                    urlp.codeTable(clientEnc, static_env.ENCODING);
                    StringBuffer xmlBuf = new StringBuffer(2500);

                    //no access control

                    xmlBuf.append("<codes>").append(dbUtils.NEWL);
                    xmlBuf.append("</codes>").append(dbUtils.NEWL);
                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_prep_ins_ctb_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_get_ctb") || urlp.cmd.equalsIgnoreCase("ae_get_ctb_xml")) {
                    urlp.codeTable(clientEnc, static_env.ENCODING);

                    //get access control

                    urlp.ctb.get(con);
                    StringBuffer xmlBuf = new StringBuffer(2500);
                    xmlBuf.append("<codes>").append(dbUtils.NEWL);
                    xmlBuf.append(urlp.ctb.asXML(con, true)).append(dbUtils.NEWL);
                    xmlBuf.append("</codes>").append(dbUtils.NEWL);
                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_get_ctb_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_lookup_ctb") || urlp.cmd.equalsIgnoreCase("ae_lookup_ctb_xml")) {
                    urlp.codeTable(clientEnc, static_env.ENCODING);

                    //no access control

                    StringBuffer xmlBuf = new StringBuffer(2500);
                    xmlBuf.append(CodeTable.lookUp(con,sess,urlp.ctb.ctb_type,urlp.ctb.ctb_id,urlp.ctb.ctb_title,urlp.orderBy,urlp.sortOrder,urlp.exact, urlp.page, urlp.search_timestamp, true)).append(dbUtils.NEWL);
                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_lookup_ctb_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                // get all code types available in the system (2001.07.27 wai)
                else if(urlp.cmd.equalsIgnoreCase("ae_get_codetype") || urlp.cmd.equalsIgnoreCase("ae_get_codetype_xml")) {
                    StringBuffer xmlBuf = new StringBuffer(500);

                    //no access control

                    xmlBuf.append(CodeTable.getTypeListAsXML(con));
                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_get_codetype_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_lookup_itm") || urlp.cmd.equalsIgnoreCase("ae_lookup_itm_xml")) {
                    urlp.searchItem(clientEnc, static_env.ENCODING);
                    StringBuffer xmlBuf = new StringBuffer(2500);

                    boolean checkStatus;
                    boolean checkLifeStatus;
                    boolean responAllItem;
                    boolean checkIsTargetedItem;
                    long[] usr_ent_ids = {0};
                    if(prof == null) {
                        checkStatus = true;
                        checkLifeStatus = true;
                        responAllItem = false;
                        checkIsTargetedItem = false;
                    }
                    else {
                        if (wizbini.cfgTcEnabled && !ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id) && !prof.isLrnRole) {
                        	cwSysMessage e = new cwSysMessage("TC014");
                            msgBox(MSG_ERROR, con, e, prof, urlp.url_success, out);
                            return;
                    	}
                        usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        AcItem acitm = new AcItem(con);
                        if(acitm.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                            checkStatus = false;
                        else
                            checkStatus = true;
                        
                            checkLifeStatus = true;
                        if(acitm.hasResponAllPrivilege(prof.usr_ent_id, prof.current_role))
                            responAllItem = true;
                        else
                            responAllItem = false;

						//David 7th June
						//acitm.hasNonTargetReadPrivilege   Deprecate
						/**
						 * Following is to chech whether show targeted solutions or not
						 * If CatalogShowOnlyTargetedLs is true and current user is Learner   -----true
						 * otherwise 													     -----false
						 */
						if (wizbini.cfgSysSetupadv.getLearningSolution().isCatalogShowOnlyTargetedLs()&& prof.isLrnRole){
							 checkIsTargetedItem = true;
						 }else{
							 checkIsTargetedItem = false;
						 }
					 }

                    //no access control
                    xmlBuf.append(aeItem.searchItemAsXML(con, sess, urlp.searchItemParam, checkStatus, checkLifeStatus, responAllItem, checkIsTargetedItem, usr_ent_ids, prof.root_ent_id, static_env, prof.usr_ent_id, null, prof.current_role,prof, wizbini, urlp.stylesheet, wizbini.cfgTcEnabled)).append(dbUtils.NEWL);
                    xmlBuf.append("<adv_srh_ind>").append(urlp.searchItemParam.get(aeItem.ADV_SRH_IND)).append("</adv_srh_ind>");
                    StringBuffer resultBuf = new StringBuffer(xmlBuf.toString());
                    String metaXML = null;
                    if(prof != null) {
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;

                        String[] itm_types = (String[])urlp.searchItemParam.get(aeItem.ITM_TYPES);
                        Hashtable param = null;

                        if (itm_types == null) {
                            param = aeItem.getSearchSessionHash(con, sess, urlp.searchItemParam);
                        } else {
                            param = urlp.searchItemParam;
                        }

                        acPageVariant.itm_types = (String[])param.get(aeItem.ITM_TYPES);
//                        acPageVariant.itm_show_run_ind = ((Boolean)param.get(aeItem.ITM_SHOW_RUN_IND)).booleanValue();
                        acPageVariant.itm_show_run_ind = true;
                        metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    }
                    cwTree tree = new cwTree();
                    resultBuf.append(tree.genNavTrainingCenterTree(con, prof, false));
                    String result = formatXML(resultBuf.toString(), metaXML, APPLYEASY, prof);
                    if(urlp.cmd.equalsIgnoreCase("ae_lookup_itm_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
				else if(urlp.cmd.equalsIgnoreCase("my_responsible_itm")
						|| urlp.cmd.equalsIgnoreCase("my_responsible_itm_xml")) {

					urlp.pagination();
					StringBuffer result = new StringBuffer(formatXML(aeItemAccess.getResponsibleItemAsXML(con, prof.usr_ent_id, prof.current_role, prof.root_ent_id, wizbini.cfgTcEnabled,urlp.cwPage), null, APPLYEASY, prof));

					if(urlp.cmd.equalsIgnoreCase("my_responsible_itm_xml")) {
						out.println(result.toString());
					} else {
						generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
					}
				}
                else if(urlp.cmd.equalsIgnoreCase("ae_get_appn_history")
                        || urlp.cmd.equalsIgnoreCase("ae_get_appn_history_xml")) {
                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    StringBuffer xmlBuf = new StringBuffer(2500);
                    //no access control
                    aeQueueManager qm = new aeQueueManager();
                    xmlBuf.append(qm.getAppnHistoryAsXML(con,
                                                         prof.usr_ent_id,
                                                         urlp.itm_id,
                                                         urlp.page,
                                                         urlp.page_size));
                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_get_appn_history_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_ist_home") || urlp.cmd.equalsIgnoreCase("ae_ist_home_xml")) {

                    //access control on filtering offline res/mod
                    AcResources acres = new AcResources(con);
                    AcModule acmod = new AcModule(con);
                    AcCourse accos = new AcCourse(con);
                    boolean checkStatusCos = (!accos.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));
                    boolean checkStatusRes = (!acres.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));
                    boolean checkStatusMod = (!acmod.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));

                    StringBuffer xmlBuf = new StringBuffer(2500);
                    xmlBuf.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
                    xmlBuf.append("<home>").append(dbUtils.NEWL);
                    xmlBuf.append(prof.asXML()).append(dbUtils.NEWL);
                    xmlBuf.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>").append(dbUtils.NEWL);

                    Vector v_cos_res_id
                        = dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_COS, prof);
                    //course list(new course, course in development, course on live
                    xmlBuf.append(dbCourse.getCosListAsXMLNoHeader(con, prof, null, null, checkStatusCos, v_cos_res_id)).append(dbUtils.NEWL);

                    //announcements
                    dbMessage dbmsg = new dbMessage();
                    dbmsg.msg_type = dbMessage.MSG_TYPE_SYS;
                    //access control on on/off filter

                    boolean checkStatusMsg = AccessControlWZB.hasRolePrivilege( prof.current_role,AclFunction.FTN_AMD_MSG_MAIN) ;
    
                    xmlBuf.append(dbmsg.msgAsXMLNoHeader(con, prof, true, checkStatusMsg, 0, 0, null, null, false)).append(dbUtils.NEWL);

                    //calendar
                    Timestamp curTime = dbUtils.getTime(con);
                    Timestamp[] t = aeUtils.getMonthBeginEnd(curTime);
                    dbCourse dbcos = new dbCourse();
                    urlp.calendar();
                    xmlBuf.append("<calendar>").append(dbUtils.NEWL);
                    xmlBuf.append(dbcos.getCalendarAsXMLNoHeader(con, prof, 0, null, null, t[0], t[1], v_cos_res_id)).append(dbUtils.NEWL);
                    xmlBuf.append("</calendar>").append(dbUtils.NEWL);

                    //catalog list
                    boolean checkStatus=true;
                    long[] usr_ent_ids={0};
                    if(prof == null) {
                        checkStatus = true;
                    }
                    else {
                        usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        AcCatalog accat = new AcCatalog(con);
                        if(accat.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                            checkStatus = false;
                        else
                            checkStatus = true;
                    }
                    xmlBuf.append(aeCatalog.catalogListAsXML(con, usr_ent_ids, checkStatus, prof,  wizbini));

                    xmlBuf.append("</home>");

                    //StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_ist_home_xml")) {
                        out.println(xmlBuf.toString());
                    } else {
                        generalAsHtml(xmlBuf.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_lrn_home") || urlp.cmd.equalsIgnoreCase("ae_lrn_home_xml")) {
                    //access control on filtering offline res/mod
                    AcResources acres = new AcResources(con);
                    AcModule acmod = new AcModule(con);
                    AcCourse accos = new AcCourse(con);
                    boolean checkStatusCos = (!accos.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));
                    boolean checkStatusRes = (!acres.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));
                    boolean checkStatusMod = (!acmod.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));

                    StringBuffer xmlBuf = new StringBuffer(2500);
                    xmlBuf.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
                    xmlBuf.append("<home>").append(dbUtils.NEWL);
                    xmlBuf.append(prof.asXML()).append(dbUtils.NEWL);
                    xmlBuf.append("<cur_time>").append(dbUtils.getTime(con)).append("</cur_time>").append(dbUtils.NEWL);

                    Vector v_cos_res_id
                        = dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_COS, prof);

                    //courses, statistics
                    long ent_id = dbRegUser.getEntId(con, prof.usr_id);
                    aeQueueManager qm = new aeQueueManager();
                    xmlBuf.append("<course_list>").append(dbUtils.NEWL);
                    xmlBuf.append(qm.getEnrolledItems(con, ent_id)).append(dbUtils.NEWL);
                    xmlBuf.append("</course_list>").append(dbUtils.NEWL);

                    //announcements
                    dbMessage dbmsg = new dbMessage();
                    dbmsg.msg_type = dbMessage.MSG_TYPE_SYS;
                   
                    boolean checkStatusMsg;
                    checkStatusMsg =true;
                    xmlBuf.append(dbmsg.msgAsXMLNoHeader(con, prof, true, checkStatusMsg, 0, 0, null, null, false)).append(dbUtils.NEWL);

                    //calendar
                    Timestamp curTime = dbUtils.getTime(con);
                    Timestamp[] t = aeUtils.getMonthBeginEnd(curTime);
                    dbCourse dbcos = new dbCourse();
                    urlp.calendar();
                    xmlBuf.append("<calendar>").append(dbUtils.NEWL);
                    xmlBuf.append(dbcos.getCalendarAsXMLNoHeader(con, prof, 0, null, null, t[0], t[1], v_cos_res_id)).append(dbUtils.NEWL);
                    xmlBuf.append("</calendar>").append(dbUtils.NEWL);

                    //!!!!!!! not support tracking history
                    //navigation center
                    dbcos.tkh_id = 0;
                    xmlBuf.append("<nav_center>").append(dbUtils.NEWL);
                    xmlBuf.append(dbcos.getLastVisitModAsXMLNoHeader(con, prof, null, 0, 10, checkStatusMod, v_cos_res_id));
                    xmlBuf.append("</nav_center>").append(dbUtils.NEWL);

                    //open item
                    xmlBuf.append("<payment>").append(dbUtils.NEWL);
                    xmlBuf.append(aeOpenItem.getOSOpenItem(con,null,prof.usr_ent_id,"applyeasy")+aeOpenItem.checkTransactionHistory(con,prof.usr_ent_id,"applyeasy"));
                    xmlBuf.append("</payment>").append(dbUtils.NEWL);

                    //catalog list
                    boolean checkStatus=true;
                    long[] usr_ent_ids={0};
                    if(prof == null) {
                        checkStatus = true;
                    }
                    else {
                        usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
                        /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                        AcCatalog accat = new AcCatalog(con);
                        if(accat.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                            checkStatus = false;
                        else
                            checkStatus = true;
                    }
                    xmlBuf.append(aeCatalog.catalogListAsXML(con, usr_ent_ids, checkStatus, prof,  wizbini));

                    xmlBuf.append("</home>");

                    //StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_lrn_home_xml")) {
                        out.println(xmlBuf.toString());
                    } else {
                        generalAsHtml(xmlBuf.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_get_appn_lst") || urlp.cmd.equalsIgnoreCase("ae_get_appn_lst_xml")) {
                    urlp.template();
                    urlp.applicationList();
                    StringBuffer xmlBuf = new StringBuffer(2500);
                    aeQueueManager qm = new aeQueueManager();
                    boolean showAll;

                    /*if(prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN))*/
                    AcQueueManager acqm = new AcQueueManager(con);
                    if(acqm.hasAdminPrivilege(prof.usr_ent_id, prof.current_role))
                        showAll = true;
                    else
                        showAll = false;

                    //no access control

                    xmlBuf.append(qm.applicationList(con, prof.root_ent_id, prof.usr_ent_id, showAll, urlp.app_lst_param,sess)).append(dbUtils.NEWL);
                    xmlBuf.append(urlp.tpl.getRawTemplate(con, urlp.itm_type, urlp.ttp_title, prof.root_ent_id));

                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_get_appn_lst_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_get_appr_appn_lst") || urlp.cmd.equalsIgnoreCase("ae_get_appr_appn_lst_xml")) {
                    StringBuffer xmlBuf = new StringBuffer(2500);
                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    aeQueueManager qm = new aeQueueManager();

                    //access control
                    /*
                    AcEntity acEnt = new AcEntity(con);
                    if(!acEnt.hasApproverPrivilege(prof.usr_ent_id, prof.current_role)) {
                        throw new cwSysMessage("GEN005", "You are NOT an approver");
                    }
                    */
                    AcPageVariant acPageVariant = new AcPageVariant(con);
                    acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                    acPageVariant.ent_id = prof.usr_ent_id;
                    acPageVariant.rol_ext_id = prof.current_role;
                    String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);

                    xmlBuf.append(qm.getApprAppnListAsXML(con, prof.root_ent_id,
                                       urlp.app_process_status, urlp.page_size,
                                       urlp.page, prof,
                                       urlp.sort_by, urlp.order_by,
                                       urlp.download, wizbini));

                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(), metaXML, APPLYEASY, prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_get_appr_appn_lst_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                /**
                 * added for the application list of approver
                 * Emily, 20020830
                 */
                else if(urlp.cmd.equalsIgnoreCase("ae_get_workflow_lst") ||
                        urlp.cmd.equalsIgnoreCase("ae_get_workflow_lst_xml")) {
                    // retrieve workflow list
                    StringBuffer result = new StringBuffer(2500);
                    result = new StringBuffer(formatXML(workFlow.getWorkFlowXML(prof, aeWorkFlowCache.cachedAppStatusList),
                                                        null, APPLYEASY, prof));

//                    StringBuffer xmlBuf = new StringBuffer(2500);
//                    xmlBuf.append(workFlow.getWorkFlowXML(con, prof, this.cachedAppStatusList));
//
//                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(), null, APPLYEASY, prof));
                    if (urlp.cmd.equalsIgnoreCase("ae_get_workflow_lst_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_get_approver_appn_lst") ||
                        urlp.cmd.equalsIgnoreCase("ae_get_approver_appn_lst_xml")) {
                    StringBuffer xmlBuf = new StringBuffer(2500);
                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                   // System.out.println("urlp.itm_id: " + urlp.itm_id);
                    aeQueueManager qm = new aeQueueManager();

                    //access control
                    /*
                    AcEntity acEnt = new AcEntity(con);
                    if(!acEnt.hasApproverPrivilege(prof.usr_ent_id, prof.current_role)) {
                        throw new cwSysMessage("GEN005", "You are NOT an approver");
                    }
                    */
                    AcPageVariant acPageVariant = new AcPageVariant(con);
                    acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                    acPageVariant.ent_id = prof.usr_ent_id;
                    acPageVariant.rol_ext_id = prof.current_role;
                    String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);

                    //System.out.println("urlp.app_process_status: " + urlp.app_process_status);
                    //System.out.println("urlp.download: " + urlp.download);
                    /*
     public String getAppnListAsXML(Connection con, long owner_ent_id,
                                   String process_status, long itm_id,
                                   long page_size, long page_num,
                                   String orderBy, String sortOrder,
                                   boolean download)
                                   throws SQLException, cwSysMessage, cwException, IOException {
                          */
                    xmlBuf.append(qm.getApproverAppnListAsXML(con, prof.root_ent_id, prof.usr_ent_id,
                                                              prof.current_role, urlp.app_process_status, urlp.tpl_id,
                                                              aeWorkFlowCache.cachedAppStatusList,
                                                              urlp.page_size,urlp.page, urlp.order_by, urlp.sort_by,
                                                              urlp.download, urlp.itm_id, urlp.show_process_status));

                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(), metaXML, APPLYEASY, prof));
                    if (urlp.cmd.equalsIgnoreCase("ae_get_approver_appn_lst_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_get_approver_nom_appn_lst") ||
                        urlp.cmd.equalsIgnoreCase("ae_get_approver_nom_appn_lst_xml")) {
                    StringBuffer xmlBuf = new StringBuffer(2500);
                    urlp.queueManagement(clientEnc, static_env.ENCODING);
//System.out.println("urlp.itm_id: " + urlp.itm_id);
                    aeQueueManager qm = new aeQueueManager();

//System.out.println("urlp.app_process_status_lst: " + urlp.app_process_status_lst);

                    xmlBuf.append(qm.getApproverNomAppnListAsXML(con, prof.root_ent_id, prof.usr_ent_id,
                                                              prof.current_role, urlp.app_process_status_lst,
                                                              urlp.page_size,urlp.page, urlp.order_by, urlp.sort_by,
                                                              urlp.itm_id, urlp.show_process_status));

                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(), null, APPLYEASY, prof));
                    if (urlp.cmd.equalsIgnoreCase("ae_get_approver_nom_appn_lst_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_get_approver_appn_cos_lst") ||
                        urlp.cmd.equalsIgnoreCase("ae_get_approver_appn_cos_lst_xml")) {
                    StringBuffer xmlBuf = new StringBuffer(2500);
                    urlp.pagination();
                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                   // System.out.println("urlp.itm_id: " + urlp.itm_id);
                    aeQueueManager qm = new aeQueueManager();

                   //System.out.println("page: " + urlp.cwPage.asXML());

                    AcPageVariant acPageVariant = new AcPageVariant(con);
                    acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                    acPageVariant.ent_id = prof.usr_ent_id;
                    acPageVariant.rol_ext_id = prof.current_role;
                    String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);

                    //System.out.println("urlp.app_process_status: " + urlp.app_process_status);
                    //System.out.println("urlp.download: " + urlp.download);

                    //HttpSession sess = request.getSession(true);
                    xmlBuf.append(qm.getApproverAppnCourseListAsXML(con, prof.root_ent_id, prof.usr_ent_id,
                                                              prof.current_role, urlp.app_process_status, urlp.tpl_id,
                                                              aeWorkFlowCache.cachedAppStatusList,
                                                              urlp.cwPage, sess,
                                                              urlp.download, urlp.show_process_status));
                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(), metaXML, APPLYEASY, prof));
                    if (urlp.cmd.equalsIgnoreCase("ae_get_approver_appn_cos_lst_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_get_appn_ps_lst") || urlp.cmd.equalsIgnoreCase("ae_get_appn_ps_lst_xml")) {
                    StringBuffer xmlBuf = new StringBuffer(2500);
                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    if(urlp.clear_session){
                        sess.removeAttribute("filter_value");
                        sess.removeAttribute("appn_process_status");
                        sess.removeAttribute("appn_filter");
                        sess.removeAttribute("filter_type");
                    }
                    aeQueueManager qm = new aeQueueManager();
                    
                    //no access control

                    xmlBuf.append(qm.getAppnListAsXML(con, 
                                       urlp.app_process_status, urlp.itm_id,
                                       urlp.app_lst_page_size, urlp.app_lst_page,
                                       urlp.sort_by, urlp.order_by,
                                       urlp.download, urlp.app_id_lst, urlp.ent_id_lst,
                                       urlp.show_approval_ent_only,
                                       wizbini, sess, prof));
                    xmlBuf.append(aeItem.genItemActionNavXML(con, urlp.itm_id, prof));
                    String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);

                    StringBuffer result = new StringBuffer(1024);
                    if (urlp.download) {
                        //result.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
                        //result.append(xmlBuf.toString());
                        result.append(formatXML(xmlBuf.toString(),metaXML,APPLYEASY, prof, "application_lst.xsl"));
                        response.setHeader("Cache-Control", ""); 
                        response.setHeader("Pragma", ""); 
                        response.setHeader("Content-Disposition", "attachment; filename=report.xls;");
                        cwUtils.setContentType("application/vnd.ms-excel", response, wizbini);
                    }
                    else {
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet)) + metaXML;
                        result.append(formatXML(xmlBuf.toString(),metaXML, APPLYEASY, prof, urlp.stylesheet));
                    }
                    if(urlp.cmd.equalsIgnoreCase("ae_get_appn_ps_lst_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if(urlp.cmd.equalsIgnoreCase("ae_set_appn_filter")) {
                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    dbUserGroup dbusg = new dbUserGroup();
                    if (urlp.filter_type.equalsIgnoreCase("advanced_filter")) {
                        urlp.getFilterAppn(clientEnc, static_env.ENCODING);
                        dbusg = urlp.dbUsg;
                    }
                    aeQueueManager qm = new aeQueueManager();

                    qm.setAppnFilterSession(sess, urlp.filter_value, urlp.filter_type,
                                            urlp.app_process_status, urlp.appn_upd_fr, urlp.appn_upd_to, urlp.dbUsg);
                    response.sendRedirect(urlp.url_success);
                }
                //get the raw template from aeTemplate
                else if(urlp.cmd.equalsIgnoreCase("ae_get_tpl") || urlp.cmd.equalsIgnoreCase("ae_get_tpl_xml")) {
                    urlp.template();
                    StringBuffer xmlBuf = new StringBuffer(2500);

                    //no access control

                    xmlBuf.append(urlp.tpl.getRawTemplate(con, urlp.itm_type, urlp.ttp_title, prof.root_ent_id));

                    StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
                    if(urlp.cmd.equalsIgnoreCase("ae_get_tpl_xml")) {
                        out.println(result.toString());
                    } else {
                        generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                //update multiple application status
                else if(urlp.cmd.equalsIgnoreCase("ae_upd_appn_status")) {
                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    StringBuffer xmlBuf = new StringBuffer(2500);
                    aeQueueManager qm = new aeQueueManager();

                    //no access control

                    qm.updApplicationStatus(con, urlp.app_id_lst, urlp.comment_lst, urlp.process_id, urlp.status_id, urlp.action_id, urlp.fr, urlp.to, urlp.action_verb, urlp.app_upd_timestamp_lst, prof.usr_id);
                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equals("ae_ins_new_itm") || urlp.cmd.equals("ae_ins_new_itm_xml")) {
                        urlp.item(clientEnc, static_env.ENCODING);
                        urlp.mote(clientEnc, static_env.ENCODING);
                        long tnd_id = urlp.tnd_parent_tnd_id;

                        aeItem itm = urlp.itm;
                        //access control
                        AcItem acitm = new AcItem(con);
                        if(!acitm.hasInsPrivilege(prof.usr_ent_id, prof.current_role, tnd_id, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                        }

                        dbCourse cos = null;
                        if(itm.itm_qdb_ind) {
                            cos = new dbCourse();
                            cos.res_lan = prof.label_lan;
                            cos.res_title = itm.itm_title;
                            cos.res_desc = urlp.cos_desc;
                            cos.cos_lic_key = urlp.cos_lic_key;
                            cos.res_usr_id_owner = prof.usr_id;
                            cos.res_upd_user = prof.usr_id;
                        }
                        itm.insAsNewVersion(con,
                                            urlp.old_itm_id,
                                            urlp.itm_in_upd_timestamp,
                                            null,
                                            prof.usr_id,
                                            static_env.INI_ITM_DIR_UPLOAD,
                                            cos,
                                            prof,
                                            urlp.vColName,
                                            urlp.vColType,
                                            urlp.vColValue,
                                            urlp.vClobColName,
                                            urlp.vClobColValue);
                        itm.insExternalInfo(con,
                                            urlp.tnd_id_lst,
											urlp.tnd_id_lst_value,
                                            urlp.target_ent_group_lst,
                                            urlp.comp_target_ent_group_lst,
                                            urlp.cost_center_group_lst,
                                            urlp.iac_id_lst,
                                            urlp.mote,
                                            urlp.cm_lst,
                                            urlp.fgt_id_vec,
                                            urlp.fig_val_vec,
                                            prof.usr_id,
                                            prof.usr_ent_id,
                                            prof.root_ent_id,
                                            wizbini.cfgSysSetupadv.getDefaultTaId(),
                                            wizbini.cfgTcEnabled);
                        //save upload file
                        if(bFileUpload) {
                        	if("use_default_image".equalsIgnoreCase(multi.getParameter("field99__select"))){
                        		String defaultPath = static_env.DOC_ROOT+ dbUtils.SLASH +static_env.DEFAULT_IMGLIG + dbUtils.SLASH +static_env.INI_ITM_DIR_UPLOAD_URL+ dbUtils.SLASH + multi.getParameter("default_image");
                        		String saveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + itm.itm_id;
                        		dbUtils.copyFile(defaultPath, saveDirPath);
                        	} else {
	                    	    String itm_icon = multi.getFilesystemName("itm_icon");
	                            procUploadedFiles(itm.itm_id, tmpUploadPath, itm_icon);
                        	}
                            
							String ies_top_icon = multi.getFilesystemName("ies_top_icon");
							procIesIconUploadedFiles(itm.itm_id, tmpUploadPath, ies_top_icon);
                        }
                        con.commit();
                        cwSysMessage e = new cwSysMessage(aeItem.ITM_NEW_VERSION_OK);
                        msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                        //response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equalsIgnoreCase("get_itm_sch")||urlp.cmd.equalsIgnoreCase("get_itm_sch_xml")){
					aeItemSchedule itmSch=new aeItemSchedule(con);
					Calendar today=Calendar.getInstance();
					int	year=today.getTime().getYear()+1900;
					int	month=today.getTime().getMonth()+1;
					String stylesheet = new String(request.getParameter("stylesheet"));
					try{
					year=Integer.parseInt(request.getParameter("year"));
					month=Integer.parseInt(request.getParameter("month"));
					}
					catch(Exception e){}
					String outXML=itmSch.getSchduleXML(year,month,prof, wizbini.cfgTcEnabled,  wizbini);
					if	(urlp.cmd.equals("get_itm_sch_xml")) {
						out.println(formatXML(outXML,prof));
					} else {
						if(stylesheet.equalsIgnoreCase("ae_export_schedule.xsl")){
						cwUtils.setContentType("text/html", response, wizbini);
                        response.setHeader("Cache-Control", ""); 
                        response.setHeader("Pragma", ""); 
						response.setHeader("Content-Disposition", "attachment; filename=training_calendar.xls;");
						}
						generalAsHtml(formatXML(outXML,prof), out, urlp.stylesheet, prof.xsl_root);
					}
                }
				else if (urlp.cmd.equals("test_cmd") || urlp.cmd.equals("test_cmd_xml")) {
					
				} 
				else if (urlp.cmd.equals("ae_itm_publish_target") || urlp.cmd.equals("ae_itm_publish_target_xml")) {
					urlp.item(clientEnc, static_env.ENCODING);
                    String xml = aeItem.getPublishTargetXML(urlp);
                    xml = formatXML(xml, prof);
                    if (urlp.cmd.equals("ae_itm_publish_target_xml")) {
                        out.println(xml);
                    } else {
                        generalAsHtml(xml, out, urlp.stylesheet, prof.xsl_root);
                    }
				}
                /*
                else if (urlp.cmd.equalsIgnoreCase("ae_new_itm_version") ||
                            urlp.cmd.equalsIgnoreCase("ae_new_itm_version_xml")) {

                    urlp.item(clientEnc, static_env.ENCODING);
                    //access control
                    AcItem acitm = new AcItem(con);
                    if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                              prof.current_role, prof.root_ent_id)) {
                        throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                    }
                    aeItem newItem = urlp.itm.newVersion(con, urlp.itm_in_upd_timestamp,
                                                            null, prof.usr_id,
                                                            static_env.INI_ITM_DIR_UPLOAD,
                                                            prof);
                    AcPageVariant acPageVariant = new AcPageVariant(con);
                    acPageVariant.instance_id = newItem.itm_id;
                    acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                    acPageVariant.ent_id = prof.usr_ent_id;
                    acPageVariant.rol_ext_id = prof.current_role;
                    String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    String result = formatXML(newItem.ItemDetailAsXML(con,static_env,false,urlp.prev_version_ind, urlp.tvw_id, urlp.show_run_ind, 0, false), metaXML, APPLYEASY, prof);
                    con.commit();
                    if (urlp.cmd.equals("ae_new_itm_version_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                */
                else {
                    service_cond1(request, response, con, prof, out, clientEnc, urlp, sess);
                }
            }
        }
        catch (qdbException e) {
            out.println("Server error: " + cwUtils.esc4JS(e.getMessage()));
            CommonLog.error(e.getMessage(),e);
            //e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException re) {
            	CommonLog.error("SQL rollback error: " +re.getMessage(),re);
                //out.println("SQL rollback error: " + cwUtils.esc4JS(re.getMessage()));
            }
        }
        catch (SQLException e) {
            out.println("SQL error: " +cwUtils.esc4JS( e.getMessage()));
           // e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException re) {
                out.println("SQL rollback error: " + cwUtils.esc4JS(re.getMessage()));
            }
        }
        catch (cwException e) {
            out.println("Server error: " + cwUtils.esc4JS(e.getMessage()));
            CommonLog.error(e.getMessage(),e);
           // e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException re) {
                out.println("SQL rollback error: " + cwUtils.esc4JS(re.getMessage()));
            }
        }
        catch (cwSysMessage e) {
             //e.printStackTrace();
             try {
                 con.rollback();
                 msgBox(MSG_ERROR, con, e, prof, urlp.url_failure, out);

             } catch (cwException ce) {
                out.println("MSGBOX Server error: " + cwUtils.esc4JS(e.getMessage()));
             } catch (SQLException se) {
                out.println("MSGBOX SQL error: " +cwUtils.esc4JS( e.getMessage()));
             }
        }
        catch(Exception e){	 
        	e.printStackTrace();
            CommonLog.error(e.getMessage(),e);
			out.println("Server error: " + cwUtils.esc4JS(e.getMessage()));
			//e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException re) {
				 CommonLog.error(re.getMessage(),re);
				out.println("SQL rollback error: " + cwUtils.esc4JS(re.getMessage()));
			}
        }
        finally {
        	CommonLog.info("[aeAction CLOSE] ID:" + my_id + "\t" + (System.currentTimeMillis() - my_time) + "\t\t" + "[ACTION : " + my_action + " ]");// + "\t\t" + "[sess_id = " + sess.getId() + "]");
            try {
                con.commit();
                if (con != null && con.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                    con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                }
                if(con != null && !con.isClosed()) {
                    con.close();
                }
                out.close();
            }
            catch (SQLException sqle) {
                CommonLog.error("Close connection error: " + cwUtils.esc4JS(sqle.getMessage()),sqle);
            }
            static_env.changeActiveReqNum(-1);

        }
    }

    private void service_cond1(HttpServletRequest request, HttpServletResponse response, Connection con, loginProfile prof, PrintWriter out, String clientEnc, aeReqParam urlp, HttpSession sess) throws cwException, qdbException, UnsupportedEncodingException, SQLException, IOException, cwSysMessage, qdbErrMessage, CloneNotSupportedException {
        // prepare application form for multiple booking of slots
        if (urlp.cmd.equals("ae_book_slot_prep") || urlp.cmd.equals("ae_book_slot_prep_xml")) {
            // retrieve url parameters
            urlp.bookSlot(clientEnc, static_env.ENCODING);
            long ent_id = dbRegUser.getEntId(con, prof.usr_id);

            //no access control

            // object for processing time slots booking
            aeBooking bman = new aeBooking(con, urlp.slot_item_id, urlp.slot_start_time, urlp.slot_parent_node_id, urlp.app_xml);
            // processing the slots booked
            bman.validateSlots(prof.usr_id);
            bman.checkDupSlots((int)ent_id);
            bman.storeInSession(sess);
            // generate the xml
            StringBuffer xml = new StringBuffer(4096);
            xml.append(aeUtils.valueTemplate(bman.getFirstTemplate(aeTemplate.APPNFORM), bman.getAppXML(), static_env))
                .append(bman.getSlotsAsXML());
            String outXML = formatXML(xml.toString(), prof);
            if (urlp.cmd.equals("ae_book_slot_prep"))
                generalAsHtml(outXML, out, urlp.stylesheet, prof.xsl_root);
            else
                out.println(outXML);
        }
        // receive application form details for multiple booking of slots
        else if (urlp.cmd.equals("ae_book_slot_appn") || urlp.cmd.equals("ae_book_slot_appn_xml")) {
            // retrieve url parameters
            urlp.bookSlot(clientEnc, static_env.ENCODING);
            long ent_id = dbRegUser.getEntId(con, prof.usr_id);

            //no access control

            // object for processing time slots booking
            aeBooking bman = new aeBooking(con, sess);
            // application XML provided?
            if (urlp.app_xml != null) bman.setAppXML(urlp.app_xml);
            // extension column 1
            if (urlp.app_ext1 != null) bman.setAppExt1(urlp.app_ext1);
            // extension column 3
            if (urlp.app_ext3 != null) bman.setAppExt3(urlp.app_ext3);
            // processing the slots booked
            bman.validateSlots(prof.usr_id);
            bman.checkDupSlots((int)ent_id);
            bman.storeInSession(sess);
            // generate the xml
            StringBuffer xml = new StringBuffer(4096);
            xml.append(bman.checkAppAsXML())
                .append(aeUtils.valueTemplate(bman.getFirstTemplate(aeTemplate.APPNFORM), bman.getAppXML(), static_env))
                .append(bman.getSlotsAsXML());
            String outXML = formatXML(xml.toString(), prof);
            if (urlp.cmd.equals("ae_book_slot_appn"))
                generalAsHtml(outXML, out, urlp.stylesheet, prof.xsl_root);
            else
                out.println(outXML);
        }
        // make the booking of slots
        else if (urlp.cmd.equals("ae_book_slot_rslt") || urlp.cmd.equals("ae_book_slot_rslt_xml")) {
            // retrieve url parameters
            urlp.bookSlot(clientEnc, static_env.ENCODING);
            long ent_id = dbRegUser.getEntId(con, prof.usr_id);

            //no access control

            // object for processing time slots booking
            aeBooking bman = new aeBooking(con, sess);
            // processing the slots booked and generate the XML
            StringBuffer xml = new StringBuffer(4096);
            xml.append(bman.make((int)ent_id, prof.usr_id)).append(bman.getSlotsAsXML());
            con.commit();
            String outXML = formatXML(xml.toString(), prof);
            if (urlp.cmd.equals("ae_book_slot_rslt"))
                generalAsHtml(outXML, out, urlp.stylesheet, prof.xsl_root);
            else
                out.println(outXML);
        }
        //get Application Form detail and history
        else if(urlp.cmd.equalsIgnoreCase("ae_get_appn_detail") || urlp.cmd.equalsIgnoreCase("ae_get_appn_detail_xml")) {
            urlp.queueManagement(clientEnc, static_env.ENCODING);
            StringBuffer xmlBuf = new StringBuffer(2500);

            aeQueueManager qm = new aeQueueManager();

            //no access control

            xmlBuf.append(qm.viewApplication(con, urlp.app_id, static_env, urlp.tvw_id, urlp.app_tvw_id)).append(dbUtils.NEWL);
            aeApplication app = new aeApplication();
            app.app_id = urlp.app_id;
            xmlBuf.append(app.appnHistoryAsXML(con));

            StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
            if(urlp.cmd.equalsIgnoreCase("ae_get_appn_detail_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
        }
        // update application form for booking slots
        else if(urlp.cmd.equals("ae_upd_appn_detail")) {
            urlp.queueManagement(clientEnc, static_env.ENCODING);
            urlp.bookSlot(clientEnc, static_env.ENCODING);

            //no access control

            aeBooking bman = new aeBooking(con);
            bman.setAppXML(urlp.app_xml);
            bman.setAppExt3(urlp.app_ext3);
            bman.updAppXML((int)urlp.app_id, urlp.upd_timestamp, prof.usr_id);
            con.commit();
            response.sendRedirect(urlp.url_success);
        }
        else if(urlp.cmd.equalsIgnoreCase("ae_get_enrol_pgm") || urlp.cmd.equalsIgnoreCase("ae_get_enrol_pgm_xml")) {

            //access control on filtering offline res/mod
            AcResources acres = new AcResources(con);
            AcModule acmod = new AcModule(con);
            AcCourse accos = new AcCourse(con);
            boolean checkStatusCos = true;
            boolean checkStatusRes = true;
            boolean checkStatusMod = true;

            StringBuffer xmlBuf = new StringBuffer(2500);
            Timestamp curTime = dbUtils.getTime(con);
            Timestamp[] t = aeUtils.getMonthBeginEnd(curTime);

            Vector v_cos_res_id
                = dbResourcePermission.getResPermission(con, dbResource.RES_TYPE_COS, prof);

            //get all enrolled program and course
            long ent_id = dbRegUser.getEntId(con, prof.usr_id);
            aeQueueManager qm = new aeQueueManager();
            xmlBuf.append(qm.getEnrolledItems(con, ent_id));

            //calendar
            dbCourse dbcos = new dbCourse();
            urlp.calendar();
            xmlBuf.append("<calendar>").append(dbUtils.NEWL);
            if(urlp.cal_start_datetime == null)
                urlp.cal_start_datetime = t[0];
            if(urlp.cal_end_datetime == null)
                urlp.cal_end_datetime = t[1];
            xmlBuf.append(dbcos.getCalendarAsXMLNoHeader(con, prof, urlp.res_id, null, null, urlp.cal_start_datetime, urlp.cal_end_datetime, v_cos_res_id)).append(dbUtils.NEWL);
            xmlBuf.append("</calendar>").append(dbUtils.NEWL);

            //navigation center
            //!!!!!!! not support tracking history
            urlp.nav_center();
            dbcos.tkh_id = 0;
            xmlBuf.append("<nav_center>").append(dbUtils.NEWL);
            xmlBuf.append(dbcos.getLastVisitModAsXMLNoHeader(con, prof, null, urlp.res_id, urlp.nav_num_of_mod, checkStatusMod, v_cos_res_id));
            xmlBuf.append("</nav_center>").append(dbUtils.NEWL);

            //announcements
            urlp.message();
            dbMessage dbmsg = new dbMessage();
            if(urlp.msg_begin_date == null)
                urlp.msg_begin_date = curTime;
            dbmsg.msg_type = urlp.msg_type;
            dbmsg.msg_begin_date = urlp.msg_begin_date;
            dbmsg.msg_res_id = urlp.res_id;
            //access control on on/off filter
       
            boolean checkStatusMsg;
            checkStatusMsg = true;
            xmlBuf.append(dbmsg.msgAsXMLNoHeader(con, prof, true, checkStatusMsg, 0, 0, null, null, false)).append(dbUtils.NEWL);

            StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
            if(urlp.cmd.equalsIgnoreCase("ae_get_enrol_pgm_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
        }
        else if(urlp.cmd.equalsIgnoreCase("ae_auto_enrol_cos") || urlp.cmd.equalsIgnoreCase("ae_auto_enrol_cos_xml")) {
            StringBuffer xmlBuf = new StringBuffer(2048);
            Timestamp curTime = dbUtils.getTime(con);
            Timestamp[] t = aeUtils.getMonthBeginEnd(curTime);

            //no access control

            //auto enrol cos
            urlp.item(clientEnc, static_env.ENCODING);
            urlp.itm.autoEnrolWZBCourse(con, prof.usr_ent_id, prof.usr_id, prof.root_ent_id);
            con.commit();
            response.sendRedirect(urlp.url_success);
        }
        else if(urlp.cmd.equalsIgnoreCase("ae_get_enrol_cos") || urlp.cmd.equalsIgnoreCase("ae_get_enrol_cos_xml")) {
        	StringBuffer xmlBuf = new StringBuffer(2500);
            dbResource dbres = new dbResource();
            urlp.resContentList();
            dbres.res_id = urlp.res_id;
            long tkh_id = 0;
            if (urlp.tkh_id > 0) {
                tkh_id = urlp.tkh_id;
            }else if (urlp.qr_ind == false){
            	CommonLog.debug("!!!!!!!!!!!!!get tracking id in aeAction ae_get_enrol_cos");
                tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, urlp.res_id, prof.usr_ent_id);
            }else {
                // Quick Reference
                if (dbCourse.canQuickReference(con, urlp.res_id)) {
                    tkh_id = DbTrackingHistory.getQRTrackingIDByCos(con, urlp.res_id, prof.usr_ent_id);
                    if (tkh_id <= 0) {
                        DbTrackingHistory dbtkh = new DbTrackingHistory();
                        dbtkh.tkh_usr_ent_id = prof.usr_ent_id;
                        dbtkh.tkh_cos_res_id = urlp.res_id;
                        dbtkh.tkh_type = DbTrackingHistory.TKH_TYPE_QUICK_REFERENCE;
                        dbtkh.ins(con);
                        tkh_id = dbtkh.tkh_id;
                        con.commit();
                    }
                }else {
                    // No Permission
                    cwSysMessage e = new cwSysMessage("ACL002");
                    msgBox(MSG_ERROR, con, e, prof, urlp.url_failure, out);
                    return;
                }
            }
            dbres.location = urlp.location;
            dbres.tkh_id = tkh_id;
            String res_content = dbres.getCosContentListAsXML(con, prof, null, 0, urlp.dpo_view, urlp.cal_d, urlp.cal_m, urlp.cal_y, null, null, true);
            xmlBuf.append(res_content.substring(res_content.indexOf("?>")+2));
            //announcements
            urlp.message();
            dbMessage dbmsg = new dbMessage();
            dbmsg.msg_type = urlp.msg_type;
            dbmsg.msg_begin_date = urlp.msg_begin_date;
            dbmsg.msg_res_id = urlp.res_id;
            xmlBuf.append(dbmsg.msgAsXMLNoHeader(con, prof, true, true, 0, 0, null, null, false)).append(dbUtils.NEWL);
            StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
            if(urlp.cmd.equalsIgnoreCase("ae_get_enrol_cos_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
        }
        else if(urlp.cmd.equalsIgnoreCase("ae_cal_and_msg") || urlp.cmd.equalsIgnoreCase("ae_cal_and_msg_xml")) {
            StringBuffer xmlBuf = new StringBuffer(2500);
            Timestamp curTime = dbUtils.getTime(con);
            Timestamp[] t = aeUtils.getMonthBeginEnd(curTime);

            // no access control

            //access control on filtering offline res/mod
            AcCourse accos = new AcCourse(con);
            boolean checkStatusCos = false;

            //get user profile
            dbRegUser usr = new dbRegUser();
            usr.usr_ent_id = prof.usr_ent_id;
            usr.get(con);
            xmlBuf.append(usr.insCosPrepXMLNoHeader(con, prof, null, ""));

            //calendar
            dbCourse dbcos = new dbCourse();
            urlp.calendar();
            if(urlp.cal_start_datetime == null)
                urlp.cal_start_datetime = t[0];
            if(urlp.cal_end_datetime == null)
                urlp.cal_end_datetime = t[1];
            xmlBuf.append("<calendar>").append(dbUtils.NEWL);
            xmlBuf.append(dbcos.getCalendarAsXMLNoHeader(con, prof, urlp.res_id, null, null, urlp.cal_start_datetime, urlp.cal_end_datetime, null)).append(dbUtils.NEWL);
            xmlBuf.append("</calendar>").append(dbUtils.NEWL);
            //result = formatXML(result, prof);

            //announcements
            urlp.message();
            dbMessage dbmsg = new dbMessage();
            dbmsg.msg_type = urlp.msg_type;
            if(urlp.msg_begin_date == null)
                urlp.msg_begin_date = curTime;
            dbmsg.msg_begin_date = urlp.msg_begin_date;
            dbmsg.msg_res_id = urlp.res_id;
            //access control on on/off filter
            boolean checkStatusMsg;
            checkStatusMsg = false;
            xmlBuf.append(dbmsg.msgAsXMLNoHeader(con, prof, true, checkStatusMsg, 0, 0, null, null, false)).append(dbUtils.NEWL);

            StringBuffer result = new StringBuffer(formatXML(xmlBuf.toString(),prof));
            if(urlp.cmd.equalsIgnoreCase("ae_cal_and_msg_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_get_rating_q") || urlp.cmd.equalsIgnoreCase("ae_get_rating_q_xml")){
            urlp.rating(clientEnc, static_env.ENCODING);

            String result = formatXML(aeItemRating.getRatingQ(con, prof, urlp.mod_id), prof);
            if(urlp.cmd.equalsIgnoreCase("ae_get_rating_q_xml"))
                    static_env.outputXML(out, result);
            if(urlp.cmd.equalsIgnoreCase("ae_get_rating_q"))
                    generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_save_rating")){
            urlp.rating(clientEnc, static_env.ENCODING);
            aeItemRating.saveRating(con, prof, urlp.itm_id, urlp.rate, urlp.ent_id);
            con.commit();
            response.sendRedirect(urlp.url_success);
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_get_rating_defination") || urlp.cmd.equalsIgnoreCase("ae_get_rating_defination_xml")){
            urlp.rating(clientEnc, static_env.ENCODING);

            String result = formatXML(aeItemRating.getRateDefinationAsXML(con, prof.root_ent_id).toString(), prof);
            if(urlp.cmd.equalsIgnoreCase("ae_get_rating_defination_xml"))
                    static_env.outputXML(out, result);
            if(urlp.cmd.equalsIgnoreCase("ae_get_rating_defination"))
                    generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_save_rating_defination")){
            urlp.rating(clientEnc, static_env.ENCODING);
            aeItemRating.saveRatingDefination(con, prof.root_ent_id, prof.usr_id, urlp.rate_range_xml, urlp.rate_q_xml);
            con.commit();
            response.sendRedirect(urlp.url_success);
        }

        else if(urlp.cmd.equalsIgnoreCase("ae_get_all_ity")
                || urlp.cmd.equalsIgnoreCase("ae_get_all_ity_xml")) {

            StringBuffer result = new StringBuffer(formatXML(
                aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id),
                "",APPLYEASY,prof));

            if(urlp.cmd.equalsIgnoreCase("ae_get_all_ity_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
            con.commit();
        }

        else if(urlp.cmd.equalsIgnoreCase("ae_get_all_ity_form")
                || urlp.cmd.equalsIgnoreCase("ae_get_all_ity_form_xml")) {
        	urlp.searchItem(clientEnc,  static_env.ENCODING);//参数赋值
        	Hashtable param = urlp.searchItemParam;
        	String itm_dummy_type="";//需要创建课程类型
        	String itm_type = "";
        	if(param.get(aeItem.TRAINING_TYPE) != null && param.get(aeItem.TRAINING_TYPE).equals("COS_ONLINE")){
        		//网上课程
        		itm_type = "SELFSTUDY";
        		itm_dummy_type="SELFSTUDY|-|COS";
        		param.put(aeItem.TRAINING_TYPE, "COS");
        	}else if(param.get(aeItem.TRAINING_TYPE) != null && param.get(aeItem.TRAINING_TYPE).equals("COS_OFF_ONLINE")){
        		//离线课程
        		itm_type = "CLASSROOM";
        		itm_dummy_type="CLASSROOM|-|COS";
        		param.put(aeItem.TRAINING_TYPE, "COS");
        	}else if(param.get(aeItem.TRAINING_TYPE) != null && param.get(aeItem.TRAINING_TYPE).equals("EXAM_ONLINE")){
        		//在线考试
        		itm_type="SELFSTUDY";
        		itm_dummy_type="SELFSTUDY|-|EXAM";
        		param.put(aeItem.TRAINING_TYPE, "EXAM");
        	}else if(param.get(aeItem.TRAINING_TYPE) != null && param.get(aeItem.TRAINING_TYPE).equals("EXAM_OFF_ONLINE")){
        		//离线考试
        		itm_type="CLASSROOM";
        		itm_dummy_type="CLASSROOM|-|EXAM";
        		param.put(aeItem.TRAINING_TYPE, "EXAM");
        	} else if(param.get(aeItem.TRAINING_TYPE) != null && param.get(aeItem.TRAINING_TYPE).equals("INTEGRATED")){
        		//项目式培训
        		itm_type="INTEGRATED";
        		itm_dummy_type="INTEGRATED";
        	}
        	itm_dummy_type="<itm_dummy_type>"+itm_dummy_type+"</itm_dummy_type>";
        	itm_type="<itm_type>"+itm_type+"</itm_type>";
        	//培训计划
        	boolean training_plan=((Boolean)param.get(aeItem.TRAINING_PLAN)).booleanValue();
        	String ityStr= aeItem.getAllItemTypeDetailInOrgAsXML(con, prof.root_ent_id)+
        		"<cur_training_type>"+param.get(aeItem.TRAINING_TYPE)+"</cur_training_type>";
        	ityStr+="<plan id=\""+(Long)param.get(aeItem.PLAN_ID)+"\" tcr_id=\""+(Long)param.get(aeItem.TPN_TCR_ID)+"\" tpn_upd_timestamp=\""
        			+param.get(aeItem.TPN_UPDATE_TIMESTAMP)+"\" entrance=\""+param.get(aeItem.TPN_ENTRANCE)+"\"/>";
        	if(training_plan){
        		Timestamp tpn_upd_timestamp=(Timestamp)param.get(aeItem.TPN_UPDATE_TIMESTAMP);
        		Long tpn_id_Long=(Long)param.get(aeItem.PLAN_ID);
        		long tpn_id=tpn_id_Long.longValue();
        		dbTpTrainingPlan tpn =new dbTpTrainingPlan();
        		tpn.tpn_id=tpn_id;
        		if(!tpn.isLastUpd(con, tpn_upd_timestamp)){
        			throw new cwSysMessage("TPN007");
        		}
        		tpn.get(con);
        		ityStr+="<tpn_cos_type>"+tpn.tpn_cos_type+"</tpn_cos_type>";
        	}
        	ityStr+=itm_dummy_type;
        	ityStr+=itm_type;
            StringBuffer result = new StringBuffer(formatXML(ityStr                                   
                                    , "", APPLYEASY, prof, urlp.stylesheet));

            if(urlp.cmd.equalsIgnoreCase("ae_get_all_ity_form_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
            
            sess.removeAttribute("PLAN_ITM_SESS");
            sess.removeAttribute("PLAN_ITM_SESS_TEMP");
            con.commit();
        }

        else if(urlp.cmd.equalsIgnoreCase("get_itm_ref_data")
                || urlp.cmd.equalsIgnoreCase("get_itm_ref_data_xml")) {

            urlp.catalog(clientEnc, static_env.ENCODING);
            if(urlp.training_type==null ||urlp.training_type.length()==0){
            	urlp.training_type="ALL";
            }         
            StringBuffer xml = new StringBuffer();   
            xml.append("<cur_training_type>").append(urlp.training_type).append("</cur_training_type>");
            xml.append("<item_reference_data>")
                .append(aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id,urlp.training_type));
           
            AcCatalog accat = new AcCatalog(con);
            boolean checkStatus = (!accat.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));
            String filter_type = null;
            if (wizbini.cfgTcEnabled) {
            	filter_type = "admin_filter";
            }

            xml.append(urlp.cat.getTitleListAsXML(con, prof, checkStatus, filter_type,  wizbini));
            
            xml.append(aeItem.getDefaultTCR4Search(con, prof.usr_ent_id, prof.current_role));
            xml.append("</item_reference_data>");
            String result = formatXML(xml.toString(),"",APPLYEASY,prof);
            if(urlp.cmd.equalsIgnoreCase("get_itm_ref_data_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
            con.commit();
        }
        else if(urlp.cmd.equalsIgnoreCase("get_run_ref_data")
                || urlp.cmd.equalsIgnoreCase("get_run_ref_data_xml")) {

            urlp.catalog(clientEnc, static_env.ENCODING);
            StringBuffer xml = new StringBuffer();
            xml.append("<cur_training_type>").append(urlp.training_type).append("</cur_training_type>");
            xml.append("<run_reference_data>")
                .append(aeItem.getAllItemTypeTitleInOrg(con, prof.root_ent_id,urlp.training_type));

            xml.append(aeItem.getDefaultTCR4Search(con, prof.usr_ent_id, prof.current_role));

            xml.append("</run_reference_data>");
            String result = formatXML(xml.toString(),"",APPLYEASY,prof);
            if(urlp.cmd.equalsIgnoreCase("get_run_ref_data_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
            con.commit();
        }

        else if (urlp.cmd.equalsIgnoreCase("ae_get_num_targeted_lrn") ||
                    urlp.cmd.equalsIgnoreCase("ae_get_num_targeted_lrn_xml")) {

            urlp.item(clientEnc, static_env.ENCODING);
/*                    //access control
            AcItem acitm = new AcItem(con);
            if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                        prof.current_role, prof.root_ent_id)) {
                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
            }*/
            urlp.itm.get(con);
            urlp.itm.setQRInd(con, prof);

            String result = formatXML(urlp.itm.getNumOfTargetLrnAsXML(con, urlp.apply_method), prof);
            if (urlp.cmd.equals("ae_get_num_targeted_lrn_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }
        else if (urlp.cmd.equals("ae_maintain_attendance") || urlp.cmd.equals("ae_maintain_attendance_xml")) {

            urlp.pagination();
            urlp.attendance(clientEnc, static_env.ENCODING);
            if (urlp.download){
                urlp.cwPage.pageSize = Integer.MAX_VALUE;
            }
            String result = aeAttendance.processStatus(con, sess, prof.root_ent_id, urlp.itm_id, urlp.att_status, urlp.cwPage, urlp.show_approval_ent_only, prof.usr_ent_id, prof.current_role, wizbini, urlp.app_id_lst,true,urlp.user_code);
            String metaXML = "";
            if(prof != null) {
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.instance_id = urlp.itm_id;
                acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                acPageVariant.ent_id = prof.usr_ent_id;
                acPageVariant.rol_ext_id = prof.current_role;
                metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
            }
            metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);

            result += aeItem.genItemActionNavXML(con, urlp.itm_id, prof);
            result+="<current_role>"+prof.current_role+"</current_role>";
            result = formatXML(cwUtils.escNull(result), metaXML, APPLYEASY, prof, urlp.stylesheet);

            if (urlp.cmd.equals("ae_maintain_attendance_xml")) {
                out.println(result);
            } else {
                if (urlp.download){
                    response.setHeader("Cache-Control", ""); 
                    response.setHeader("Pragma", ""); 
                    response.setHeader("Content-Disposition", "attachment; filename=attendance_report.xls;");
                    cwUtils.setContentType("application/vnd.ms-excel", response, wizbini);
                }
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }else if(urlp.cmd.equalsIgnoreCase("ae_get_attendance_record")||urlp.cmd.equalsIgnoreCase("ae_get_attendance_record_xml")){
				urlp.attendance(clientEnc, static_env.ENCODING);
        		aeAttendance att =new aeAttendance();
        		String xml = att.usr_attendance_record(con,urlp.itm_id,urlp.app_id, prof.root_ent_id,urlp.att_status);

        		xml += aeItem.genItemActionNavXML(con, urlp.itm_id, prof);
        		String result = formatXML(cwUtils.escNull(xml), dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id), "applyeasy", prof);
        		if(urlp.cmd.equalsIgnoreCase("ae_get_attendance_record_xml")){
					out.println(result);
        		}else{
					generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
        		}
        }
        else if (urlp.cmd.equals("ae_get_att") || urlp.cmd.equals("ae_get_att_xml")) {

            urlp.attendance(clientEnc, static_env.ENCODING);
            aeAttendance att = new aeAttendance();
            att.att_app_id = urlp.app_id;
            String result = att.getSingleAsXML(con, sess, prof.root_ent_id, urlp.itm_id, urlp.att_status).toString();
            aeUserFigure aeUfg = new aeUserFigure();
            result += aeUfg.getDetailAsXML(con, urlp.itm_id, urlp.app_id);
            result = formatXML(cwUtils.escNull(result), dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id), "applyeasy", prof);
            if (urlp.cmd.equals("ae_get_att_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }
        else if (urlp.cmd.equals("ae_upd_att")) {

            urlp.attendance(clientEnc, static_env.ENCODING);
            aeAttendance att = new aeAttendance();
            att.att_app_id = urlp.app_id;
            att.att_ats_id = urlp.att_status;
            att.att_remark = urlp.remark;
            att.att_itm_id = urlp.itm_id;
            att.att_update_usr_id = prof.usr_id;
            //for concurrence
            att.att_last_update_timestamp = urlp.att_update_timestamp;
			aeAttendance.msgSubject[0] = urlp.msg_subject_1;
			aeAttendance.msgSubject[1] = urlp.msg_subject_2;
			aeAttendance.msgSubject[2] = urlp.msg_subject_3;
            att.save(con, prof.usr_id);
            att.invalidateSess(sess);
            //Update user item accreditation
            aeUserFigure aeUfg = new aeUserFigure();
            aeUfg.updUserFigure(con, prof, att.att_app_id, urlp.ict_id_list, urlp.icv_value_list);

            con.commit();
            response.sendRedirect(urlp.url_success);
        }
    	else if (urlp.cmd.equalsIgnoreCase("ae_upd_att_timestamp")){
			urlp.attendance(clientEnc, static_env.ENCODING);
			aeAttendance att = new aeAttendance();
			att.att_app_id = urlp.app_id;
			att.att_remark = urlp.remark;
			att.att_itm_id = urlp.itm_id;
			att.att_timestamp = urlp.att_timestamp;
			att.updAttTimestampWithRemark(con,prof.usr_id);
			con.commit();
			response.sendRedirect(urlp.url_success);
    	}
        //add for att_remark
        else if (urlp.cmd.equals("ae_upd_att_remark")) {
            urlp.attendance(clientEnc, static_env.ENCODING);
            aeAttendance att = new aeAttendance();
            att.att_update_usr_id = prof.usr_id;
            //for concurrence
            att.att_update_timestamp_lst = urlp.att_update_timestamp_lst;
            att.updAttMultiRemark(con, urlp.app_id_lst, urlp.att_remark_lst);
            con.commit();
            cwSysMessage e = new cwSysMessage("ENT002");
            msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
//                    response.sendRedirect(urlp.url_success);        
        }
        else if (urlp.cmd.equals("ae_upd_multi_att_status")) {
            urlp.attendance(clientEnc, static_env.ENCODING);
            aeAttendance att = new aeAttendance();
			aeAttendance.msgSubject[0] = urlp.msg_subject_1;
			aeAttendance.msgSubject[1] = urlp.msg_subject_2;
			aeAttendance.msgSubject[2] = urlp.msg_subject_3;
            att.updMultiStatus(con, prof.usr_id, urlp.app_id_lst, urlp.att_status, urlp.itm_id);

            aeUserFigure aeUfg = new aeUserFigure();
            aeUfg.updMultiUserFigure(con, prof, urlp.itm_id, urlp.att_status, urlp.app_id_lst);

            att.invalidateSess(sess);
            con.commit();
            response.sendRedirect(urlp.url_success);
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_cancel_itm")) {

                    urlp.item(clientEnc, static_env.ENCODING);


                    //access control
                    AcItem acitm = new AcItem(con);
                    if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                              prof.current_role, prof.root_ent_id)) {
                        throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                    }
                    aeXMessage aeXmsg = new aeXMessage();
                    aeXmsg.itemCancellationNotify(con, prof, urlp.itm.itm_id, urlp.itm.itm_cancellation_reason, urlp.msg_subject,urlp.sender_ent_id,urlp.cc_to_approver_ind, urlp.cc_to_approver_rol_ext_id );

                    urlp.itm.cancelItem(con, prof, urlp.itm_in_upd_timestamp, urlp.rsv_upd_timestamp, null);

                    Vector childItm = urlp.itm.getChildItemRecursive(con);
                    for(int i =0; i < childItm.size(); i++){
                        aeItem childItem = (aeItem) childItm.elementAt(i);
                        // System.out.println("cancel :id " + childItem.itm_id);
                        if(childItem.itm_life_status == null){
                            childItem.itm_cancellation_reason = urlp.itm.itm_cancellation_reason;
                            childItem.itm_cancellation_type = urlp.itm.itm_cancellation_type;
                            aeXmsg.itemCancellationNotify(con, prof, childItem.itm_id, childItem.itm_cancellation_reason, urlp.msg_subject,urlp.sender_ent_id, urlp.cc_to_approver_ind, urlp.cc_to_approver_rol_ext_id );
                            childItem.cancelItem(con, prof, childItem.itm_upd_timestamp, urlp.rsv_upd_timestamp, null);
                        }
                    }

                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equalsIgnoreCase("ae_prep_cancel_itm") ||
                         urlp.cmd.equalsIgnoreCase("ae_prep_cancel_itm_xml")) {

                    urlp.item(clientEnc, static_env.ENCODING);
                    //access control
                    AcItem acitm = new AcItem(con);
                    if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                              prof.current_role, prof.root_ent_id)) {
                        throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                    }
                    urlp.itm.get(con);
                    urlp.itm.setQRInd(con, prof);
                    String result = formatXML(urlp.itm.prepareCancelItem(con), prof);
                    if (urlp.cmd.equals("ae_prep_cancel_itm_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_discontinue_itm")) {

            urlp.item(clientEnc, static_env.ENCODING);
            //access control
            AcItem acitm = new AcItem(con);
            if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                        prof.current_role, prof.root_ent_id)) {
                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
            }
            urlp.itm.get(con);
            urlp.itm.setQRInd(con, prof);
            urlp.itm.discontinueVersion(con, prof.usr_id, urlp.itm_in_upd_timestamp);

            con.commit();
            response.sendRedirect(urlp.url_success);
        }else if (urlp.cmd.equalsIgnoreCase("ae_get_run_lst") ||
                            urlp.cmd.equalsIgnoreCase("ae_get_run_lst_xml") ||
                            urlp.cmd.equalsIgnoreCase("ae_get_child_lst") ||
                            urlp.cmd.equalsIgnoreCase("ae_get_child_lst_xml") )
        {

                    urlp.item(clientEnc, static_env.ENCODING);
                    urlp.pagination();

                    //access control on viewing offline items
                    boolean checkStatus;
                    AcItem acitm = new AcItem(con);
                    if(acitm.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                        checkStatus = false;
                    else
                        checkStatus = true;

                    urlp.itm.get(con);
                    urlp.itm.setQRInd(con, prof);
                    if(checkStatus && urlp.itm.isItemOff(con)) {
                        throw new cwSysMessage(aeItem.ITM_OFFLINE_MSG);
                    }
                    String boundListXMLTag = null;
                    if (urlp.cmd.equalsIgnoreCase("ae_get_run_lst") ||
                            urlp.cmd.equalsIgnoreCase("ae_get_run_lst_xml") ){
                        boundListXMLTag = "run_item_list";
                    }
                    String result = urlp.itm.getChildItemAsXML(con, checkStatus, urlp.show_attendance_ind, true, urlp.cwPage, boundListXMLTag);
                    //Page Variant Object to answer Xsl Questions
                    AcPageVariant acPageVariant = new AcPageVariant(con);
                    acPageVariant.instance_id = urlp.itm.itm_id;
                    acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                    acPageVariant.ent_id = prof.usr_ent_id;
                    acPageVariant.rol_ext_id = prof.current_role;
                    String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    if (urlp.show_attendance_ind){
                        metaXML += aeAttendanceStatus.getAllStatusAsXML(con, prof.root_ent_id);
                    }
        			result += aeItem.genItemActionNavXML(con, urlp.itm.itm_id, prof);
        			
        			/**
        			 * 权限控制
        			 */
        			//课程维护权限
        			boolean has_ftn_amd_itm_cos_main_view = AccessControlWZB.hasRolePrivilege(acPageVariant.rol_ext_id, AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW);
        			//处理课程报名权限
        			boolean has_ftn_amd_itm_cos_main_application = AccessControlWZB.hasRolePrivilege(acPageVariant.rol_ext_id, AclFunction.FTN_AMD_ITM_COS_MAIN_APPLICATION);
        			////课程成绩权限
        			boolean has_ftn_amd_itm_cos_main_performance = AccessControlWZB.hasRolePrivilege(acPageVariant.rol_ext_id, AclFunction.FTN_AMD_ITM_COS_MAIN_PERFORMANCE);
        			
        			result += "<permission "
        					+ "has_ftn_amd_itm_cos_main_view='"+has_ftn_amd_itm_cos_main_view+"'"
        					+ " has_ftn_amd_itm_cos_main_application='"+has_ftn_amd_itm_cos_main_application+"'"
        					+ " has_ftn_amd_itm_cos_main_performance='"+has_ftn_amd_itm_cos_main_performance+"'"
        					+ "></permission>";
        			
                    result = formatXML(result, metaXML, APPLYEASY, prof);

                    if (urlp.cmd.equalsIgnoreCase("ae_get_run_lst_xml")
                    || urlp.cmd.equalsIgnoreCase("ae_get_child_lst_xml") ) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
                }
                else if (urlp.cmd.equalsIgnoreCase("target_lrn_test")) {
                    urlp.item(clientEnc, static_env.ENCODING);
                    Vector v = urlp.itm.getTargetLrn(con);
                    out.println("target_lrn num = " + v.size());
                    for(int i=0; i<v.size(); i++) {
                        out.println("<BR>");
                        out.println("usr_ent_id = " + v.elementAt(i));
                    }
                }
        else if (urlp.cmd.equalsIgnoreCase("ae_lrn_soln") ||
                    urlp.cmd.equalsIgnoreCase("ae_lrn_soln_xml")) {
            urlp.LearningSoln();
            aeLearningSoln soln = new aeLearningSoln();

            String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
            metaXML += aeLearningSoln.getLearningPlanConfigXML(wizbini, prof.root_id);
            //access control on viewing other's learning solution
            if(urlp.usr_ent_id == 0) {
                urlp.usr_ent_id = prof.usr_ent_id;
            } else if(urlp.usr_ent_id != prof.usr_ent_id) {
                if(urlp.viewer_ent_id == 0){ //case for current user to see
                    Vector v_subordinates = ViewRoleTargetGroup.getTargetGroupsLrn(con, prof.usr_ent_id, prof.current_role, false);
                    if(!v_subordinates.contains(new Long(urlp.usr_ent_id))) {
                        throw new cwSysMessage("AELS03");
                    }
                } else{
                     Vector v_subordinates = ViewRoleTargetGroup.getTargetGroupsLrn(con, urlp.viewer_ent_id, urlp.viewer_role, false);
                    if(!v_subordinates.contains(new Long(urlp.usr_ent_id))) {
                        throw new cwSysMessage("AELS03");
                    }
                }

            }
            //access control end
			String groupAncesterSql = dbEntityRelation.getAncestorListSql(prof.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			String gradeAncesterSql = dbEntityRelation.getAncestorListSql(prof.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			boolean isSelfInitiatedEnabled = ((LearningPlan)wizbini.cfgOrgLearningPlan.get(prof.root_id)).getSelfInitiated().isEnabled();
			String result = aeLearningPlan.getMyLearningPlanXML(con, isSelfInitiatedEnabled, prof, groupAncesterSql, gradeAncesterSql);
            
            result = formatXML(result, metaXML, APPLYEASY, prof);

            if (urlp.cmd.equals("ae_lrn_soln_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_ins_lrn_soln")) {
            urlp.LearningSoln();
            int result = aeLearningSoln.insSoln(con, prof, urlp.usr_ent_id, urlp.itm_id, urlp.period_id);

            if (result == 1) {
                con.commit();
//                cwSysMessage e = new cwSysMessage("AELS01");
//                msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
                response.sendRedirect(urlp.url_success);
            } else {
                cwSysMessage e = new cwSysMessage("AELS02");
                msgBox(MSG_ERROR, con, e, prof, urlp.url_success, out);
            }
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_upd_lrn_soln")) {
            urlp.LearningSoln();
            aeLearningSoln.updSoln(con, prof, urlp.usr_ent_id, urlp.v_itm_lst, urlp.v_period_lst);
            con.commit();
            response.sendRedirect(urlp.url_success);
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_lrn_plan") ||
            urlp.cmd.equalsIgnoreCase("ae_lrn_plan_xml")) {
            urlp.LearningSoln();
            aeLearningPlan plan = new aeLearningPlan();
            String result = plan.getMyCoursesAsXML(con, prof.root_ent_id, prof.usr_ent_id, urlp.v_itm_type);
            result = formatXML(result, prof);

            if (urlp.cmd.equals("ae_lrn_plan_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }


        else if (urlp.cmd.equals("ae_get_appn_form_step2") || urlp.cmd.equals("ae_get_appn_form_step2_xml")) {
            //no access control

            urlp.queueManagement(clientEnc, static_env.ENCODING);
            String result;
            aeQueueManager qm = new aeQueueManager();
            if(urlp.ent_id == 0) {
                urlp.ent_id = prof.usr_ent_id;
            }
            result = qm.getApplicationForm(con, sess, urlp.itm_id, urlp.ent_id, prof, static_env, urlp.tnd_id, urlp.tvw_id, urlp.app_tvw_id);

            //Page Variant Object to answer Xsl Questions
            AcPageVariant acPageVariant = new AcPageVariant(con);
            acPageVariant.ent_owner_ent_id = prof.root_ent_id;
            acPageVariant.ent_id = prof.usr_ent_id;
            acPageVariant.rol_ext_id = prof.current_role;
            String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
            dbRegUser dbUsr = new dbRegUser();
            if( urlp.entIdRole != null ){
                result = formatXML(result + dbUsr.getUserListAsXml(con, urlp.entIdRole), metaXML, APPLYEASY, prof);
            }else{
                result = formatXML(result, metaXML, APPLYEASY, prof);
            }
            if (urlp.cmd.equals("ae_get_appn_form_step2_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        } else if (urlp.cmd.equals("ae_get_multi_appn_form") || urlp.cmd.equals("ae_get_multi_appn_form_xml")) {
                    //no access control

                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                    String usr_ent_n_rol_id_lst = request.getParameter("usr_ent_n_rol_id_lst");
                    String app_xml = request.getParameter("app_xml");

                    //System.out.println("urlp.itm_id:" + urlp.itm_id);
                    //System.out.println("urlp.itm_id:" + urlp.itm_id);
                    //System.out.println("usr_ent_n_rol_id_lst:" + request.getParameter("usr_ent_n_rol_id_lst"));
                    //System.out.println("app_xml:" + request.getParameter("app_xml"));

                    String result;
                    aeQueueManager qm = new aeQueueManager();


                    result = qm.getMultiApplicationForm(con, sess, urlp.itm_id, urlp.ent_id_lst, urlp.entIdRole, urlp.app_xml);

                    //Page Variant Object to answer Xsl Questions
                    AcPageVariant acPageVariant = new AcPageVariant(con);
                    acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                    acPageVariant.ent_id = prof.usr_ent_id;
                    acPageVariant.rol_ext_id = prof.current_role;
                    String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));

                    result = formatXML(result, metaXML, APPLYEASY, prof);

                    if (urlp.cmd.equals("ae_get_multi_appn_form_xml")) {
                        out.println(result);
                    } else {
                        generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
                    }
        }
        else if (urlp.cmd.equals("ae_mass_ins_appn")) {
                    //no access control


                    urlp.queueManagement(clientEnc, static_env.ENCODING);
                  /*
                    System.out.println("urlp.app_xml: " + urlp.app_xml);
                    System.out.println("urlp.ent_id: " + urlp.ent_id);
                    System.out.println("urlp.itm_id: " + urlp.itm_id);
                    System.out.println("urlp.entIdRole: " + urlp.entIdRole);*/
                    //if(urlp.app_xml == null || urlp.app_xml.equals(""))
                        urlp.app_xml = (String)sess.getAttribute(aeQueueManager.APP_XML);
                    //if(urlp.entIdRole == null || urlp.entIdRole.size() == 0)
                        urlp.entIdRole = (Hashtable) sess.getAttribute(aeQueueManager.USR_APPROVER_LST);


                     CommonLog.debug("urlp.app_xml: " + urlp.app_xml);
                     CommonLog.debug("urlp.entIdRole: " + urlp.entIdRole);



                    aeQueueManager qm = new aeQueueManager();
                    //qm.insApplication(con, sess, urlp.ent_id, urlp.itm_id, prof.usr_id, urlp.tnd_id, prof);
                    if(urlp.ent_id == 0) {
                        urlp.ent_id = prof.usr_ent_id;
                    }
                    aeApplication aeApp = qm.insApplication(con, urlp.app_xml, urlp.ent_id, urlp.itm_id, prof, 0);
                    if( urlp.entIdRole != null ) {
                        aeAppnTargetEntity aeAte = new aeAppnTargetEntity();
                        aeAte.ins(con, aeApp.app_id, urlp.entIdRole, prof);
                    }


                    //Update Resources Permission in the session
                    String status = aeApplication.getUserItemStatus(con, urlp.itm_id, urlp.ent_id);

                    if( status != null && status.equalsIgnoreCase(aeApplication.ADMITTED) &&
                        urlp.ent_id == prof.usr_ent_id ){ //Status is ADMITTED and Self Enrol
                        Vector modIdVec = new Vector();
                        Vector assIdVec = new Vector();
                        dbCourse.getAllEnrolledModIds(con, prof, modIdVec, assIdVec);
                        Vector queIdVec = dbProgressAttempt.getAttemptedResIds(con, prof.usr_id);
                        if (modIdVec != null)
                            sess.setAttribute(qdbAction.AUTH_MOD_IDS, modIdVec);
                        if (assIdVec != null)
                            sess.setAttribute(qdbAction.AUTH_ASS_IDS, assIdVec);
                        if (queIdVec != null)
                            sess.setAttribute(qdbAction.AUTH_QUE_IDS, queIdVec);
                    }


                    con.commit();
                    response.sendRedirect(urlp.url_success);

        }
        else {
            service_cond2(request, response, con, prof, out, clientEnc, urlp, sess);
        }
    }

    private void service_cond2(HttpServletRequest request, HttpServletResponse response, Connection con, loginProfile prof, PrintWriter out, String clientEnc, aeReqParam urlp, HttpSession sess) throws cwException, qdbException, UnsupportedEncodingException, SQLException, IOException, cwSysMessage, qdbErrMessage, CloneNotSupportedException {
        if (urlp.cmd.equalsIgnoreCase("ae_req_appr_itm")) {

                    urlp.item(clientEnc, static_env.ENCODING);
                    urlp.message();
                    //access control
                    AcItem acitm = new AcItem(con);
                    if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                              prof.current_role, prof.root_ent_id)) {
                        throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                    }
                    urlp.itm.itm_life_status = aeItem.ITM_LIFE_STATUS_REQUEST_APPROVAL;
                    urlp.itm.setLifeStatus(con, prof.usr_id, urlp.itm_in_upd_timestamp);

                    //send request approval request
                    urlp.messaging(clientEnc, static_env.ENCODING);
                    //long[] ent_ids = dbUserGroup.constructEntId(con, urlp.ent_ids);
                    //long[] cc_ent_ids = dbUserGroup.constructEntId(con, urlp.cc_ent_ids);
                    //long[] ent_ids = dbUtils.string2LongArray(urlp.ent_ids, "~");
                    //long[] cc_ent_ids = dbUtils.string2LongArray(urlp.cc_ent_ids, "~");
                    long[] ent_ids;
                    long[] cc_ent_ids;

                    if( urlp.ent_ids == null || urlp.ent_ids.length() == 0 )
                        ent_ids = new long[0];
                    else
                        ent_ids = dbUtils.string2LongArray(urlp.ent_ids, "~");

                    if( urlp.cc_ent_ids == null || urlp.cc_ent_ids.length() == 0 )
                        cc_ent_ids = new long[0];
                    else
                        cc_ent_ids = dbUtils.string2LongArray(urlp.cc_ent_ids, "~");


                    urlp.url_redirect = cwUtils.getRealPath(request, urlp.url_redirect);
                    urlp.url_redirect_param += "&ent_id=" + dbUtils.longArray2String(ent_ids,"~")
                                            + "&cc_ent_id="+dbUtils.longArray2String(cc_ent_ids,"~")
                                            + "&usr_id="+prof.usr_id;

                    String returnMessage;
                    try{
                        returnMessage = aeUtils.urlRedirect( urlp.url_redirect_param, request );
                    }catch(Exception e) {
                        throw new cwException("Failed to get the message detials : " + e);
                    }
                    //System.out.println("Insert Message Success !!!");

                    con.commit();
                    response.sendRedirect(urlp.url_success);
                }
        else if (urlp.cmd.equalsIgnoreCase("ae_get_approved_version") ||
            urlp.cmd.equalsIgnoreCase("ae_get_approved_version_xml")) {
            urlp.item(clientEnc, static_env.ENCODING);
            long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
            if(urlp.tnd_id != 0) {
                //access control on treenode
                AcTreeNode actnd = new AcTreeNode(con);
                aeTreeNode tnd = new aeTreeNode();
                tnd.tnd_id = urlp.tnd_id;
                tnd.tnd_cat_id = tnd.getCatalogId(con);
                if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role,
                                            tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
                    throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
                }
            }
            //access control on item
            AcItem acitm = new AcItem(con);
            if(!acitm.hasReadPrivilege(urlp.itm.itm_id, prof.usr_ent_id, prof.current_role)){
                throw new cwSysMessage(aeItem.ITM_OFFLINE_MSG);
            }
            boolean checkStatus;
            if(acitm.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role))
                checkStatus = false;
            else
                checkStatus = true;

            String result =
                urlp.itm.getApprovedVersionAsXML(con, static_env, urlp.tnd_id,
                                    checkStatus, urlp.prev_version_ind,
                                    urlp.tvw_id);
            result = formatXML(result, prof);

            if (urlp.cmd.equals("ae_get_approved_version_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_get_itm_raw_tpl") ||
            urlp.cmd.equalsIgnoreCase("ae_get_itm_raw_tpl_xml")) {
            urlp.item(clientEnc, static_env.ENCODING);
            StringBuffer xmlBuf = new StringBuffer(2500);
            aeTemplate tpl = new aeTemplate();
            tpl.tpl_id = urlp.itm.getTemplateId(con, urlp.tpl_type);
            tpl.get(con);
            xmlBuf.append(tpl.tpl_xml);
            String xml = formatXML(xmlBuf.toString(), prof);
            if (urlp.cmd.equals("ae_get_itm_raw_tpl_xml")) {
                out.println(xmlBuf.toString());
            } else {
                generalAsHtml(xmlBuf.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_do_enrol_assign")) {
            urlp.queueManagement(clientEnc, static_env.ENCODING);
            aeQueueManager qm = new aeQueueManager();
			aeQueueManager.doEnrolAssignment(con, urlp.itm_id_lst, urlp.ent_id_lst,
                                    urlp.app_id_lst, urlp.app_upd_timestamp_lst,
                                    prof);
            con.commit();
            response.sendRedirect(urlp.url_success);
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_get_itm_target_ent") ||
            urlp.cmd.equalsIgnoreCase("ae_get_itm_target_ent_xml")) {
            urlp.item(clientEnc, static_env.ENCODING);

            String result = urlp.itm.getTargetGroupsAsXML(con, urlp.apply_method);
            String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
            result = formatXML(result, metaXML, APPLYEASY, prof);

            if (urlp.cmd.equalsIgnoreCase("ae_get_itm_target_ent_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }
        else if (urlp.cmd.equalsIgnoreCase("ae_notify_target_ent") ||
                 urlp.cmd.equalsIgnoreCase("ae_notify_target_ent_xml")) {
            urlp.item(clientEnc, static_env.ENCODING);
            String result = "aaa";

            if (urlp.cmd.equalsIgnoreCase("ae_notify_target_ent_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }
            else if(urlp.cmd.equalsIgnoreCase("get_itm_wrk_tpl") ||
                urlp.cmd.equalsIgnoreCase("get_itm_wrk_tpl_xml") ) {
                urlp.item(clientEnc, static_env.ENCODING);
                ViewItemTemplate viewIt = new ViewItemTemplate();
                viewIt.itemId = urlp.itm.itm_id;
                urlp.itm.get(con);
                urlp.itm.setQRInd(con, prof);
                viewIt.templateType = "WORKFLOW";
                viewIt.ownerEntId = prof.root_ent_id;
                StringBuffer result = new StringBuffer(
                                        formatXML(
                                        viewIt.getItemSupportedTemplateAsXML(con) + urlp.itm.shortInfoAsXML(con),
                                        "", APPLYEASY, prof, urlp.stylesheet));
            if(urlp.cmd.equalsIgnoreCase("get_itm_wrk_tpl_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);
            }
        }

        else if(urlp.cmd.equalsIgnoreCase("upd_itm_wrk_tpl") ||
                urlp.cmd.equalsIgnoreCase("upd_itm_wrk_tpl_xml") ) {
                urlp.item(clientEnc, static_env.ENCODING);
                urlp.itm.get(con);
                urlp.itm.setQRInd(con, prof);
                urlp.itm.updCascadeItemWrkTpl(con, urlp.wrk_tpl_id);
                con.commit();
                response.sendRedirect(urlp.url_success);
                return;
        }
        else if(urlp.cmd.equalsIgnoreCase("ae_get_content_info") || urlp.cmd.equalsIgnoreCase("ae_get_content_info_xml")) {
                    urlp.item(clientEnc, static_env.ENCODING);
                    // no access control
                    aeItem itm = urlp.itm;
                    itm.get(con, urlp.tnd_id);
                    itm.setQRInd(con, prof);

                    StringBuffer resultBuf = new StringBuffer(2500);
                    StringBuffer extXML = new StringBuffer();
                    String metaXML = null;
                    resultBuf.append(itm.contentInfoAsXML(con));
                    if(prof != null) {
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.instance_id = itm.itm_id;
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    }
                    if(urlp.adv_filter){
                        metaXML += dbRegUser.getUserAttributeInfoXML(wizbini, prof.root_id);
                        metaXML += itm.getRawTemplate(con, aeTemplate.WORKFLOW);
                    }
                    if(urlp.training_type!=null){
                    	metaXML +="<training_type>"+urlp.training_type+"</training_type>";
                    }
                    
                    extXML.append("<max_upload_count>" + wizbini.cfgSysSetupadv.getEnrollmentBatchUpload().getMaxUploadCount() + "</max_upload_count>");
                    extXML.append("<spawn_threshold>" + wizbini.cfgSysSetupadv.getEnrollmentBatchUpload().getSpawnThreshold() + "</spawn_threshold>");
                    resultBuf.append(extXML.toString());
                    resultBuf.append(cwXMLLabel.get("application_lst.xsl", prof.xsl_root, prof.label_lan));
                    resultBuf.append(aeQueueManager.getExportColsXML(wizbini, prof.root_id, aeQueueManager.EXPORT_ENROLLMENT));

                    resultBuf.append("<is_new_cos>").append(urlp.itm.is_new_cos).append("</is_new_cos>");
                    resultBuf.append(aeItem.genItemActionNavXML(con, itm.itm_id, prof));
                    resultBuf.append("<current_role>").append(prof.current_role).append("</current_role>");
                    String result = formatXML(resultBuf.toString(),metaXML, APPLYEASY, prof, urlp.stylesheet);
                    if (urlp.cmd.equals("ae_get_content_info_xml")) {
                        out.println(result.toString());
                    } else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result.toString(), out, urlp.stylesheet, xsl_root);
                    }

        }
         else if(urlp.cmd.equalsIgnoreCase("ae_get_ji_info") || urlp.cmd.equalsIgnoreCase("ae_get_ji_info_xml")) {
                    urlp.item(clientEnc, static_env.ENCODING);
                    // no access control
                    aeItem itm = urlp.itm;
                    itm.get(con, urlp.tnd_id);
                    itm.setQRInd(con, prof);

                    StringBuffer resultBuf = new StringBuffer(2500);
                    String metaXML = null;
                    resultBuf.append(itm.JIInfoAsXML(con));
                    if(prof != null) {
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.instance_id = itm.itm_id;
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                    }

                    resultBuf.append(aeItem.genItemActionNavXML(con, itm.itm_id, prof));
                    String result = formatXML(resultBuf.toString(),metaXML, APPLYEASY, prof, urlp.stylesheet);
                    if (urlp.cmd.equals("ae_get_ji_info_xml")) {
                        out.println(result.toString());
                    } else {
                        String xsl_root = (prof == null) ? null:prof.xsl_root;
                        generalAsHtml(result.toString(), out, urlp.stylesheet, xsl_root);
                    }

        }
         else if(urlp.cmd.equalsIgnoreCase("ae_upd_ji") || urlp.cmd.equalsIgnoreCase("ae_upd_ji_xml")) {

			urlp.item(clientEnc, static_env.ENCODING);
			urlp.jiMsg();
            //access control
            AcItem acitm = new AcItem(con);
            if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                        prof.current_role, prof.root_ent_id)) {
                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
            }

            //获取当前课程所有报名学员
			aeQueueManager qm = new aeQueueManager();
			Vector v_app = qm.getEnrollVector(con, prof.root_ent_id, urlp.itm.itm_id);
			if(urlp.ji_no_change.equalsIgnoreCase("new")) {
				
				//清空已报名的学员 开课提醒邮件
				for (int i=0; i<v_app.size(); i++) {
					  aeApplication app;
	                app = (aeApplication) v_app.elementAt(i);
	                aeItemMessage.removeNotifyForJI(con,urlp.itm.itm_id,app.app_ent_id,app.app_id,"JI");
	            }
				
				aeItemMessage itmMsg = new aeItemMessage();
//				itmMsg.updJiReminderSendTimestamp(con, urlp.itm.itm_id, DbItemMessage.TYPE_JI, urlp.ji_target_datetime);
				aeItem.updateItemJiSendTimestamp(con, urlp.itm.itm_id, urlp.ji_target_datetime);
				//对已报名用户插入JI邮件
				for (int i=0; i<v_app.size(); i++) {
					  aeApplication app;
	                  app = (aeApplication) v_app.elementAt(i);
	                  aeItemMessage.insNotifyForOnlyJI(con,prof,app.app_ent_id,urlp.itm.itm_id,app.app_id);
	             }
				
			}else if(urlp.ji_no_change.equalsIgnoreCase("never")){
				
				//清空已报名的学员 开课提醒邮件
				for (int i=0; i<v_app.size(); i++) {
					  aeApplication app;
	                app = (aeApplication) v_app.elementAt(i);
	                aeItemMessage.removeNotifyForJI(con,urlp.itm.itm_id,app.app_ent_id,app.app_id,"JI");
	            }
				
		    	aeItemMessage itmMsg = new aeItemMessage();
//				itmMsg.updJiReminderSendTimestamp(con, urlp.itm.itm_id, DbItemMessage.TYPE_JI,null);
				aeItem.updateItemJiSendTimestamp(con, urlp.itm.itm_id, null);
		    }
			if(urlp.reminder_no_change.equalsIgnoreCase("new")) {
				
				//清空已报名的学员 开课提醒邮件
				for (int i=0; i<v_app.size(); i++) {
					  aeApplication app;
	                app = (aeApplication) v_app.elementAt(i);
	                aeItemMessage.removeNotifyForJI(con,urlp.itm.itm_id,app.app_ent_id,app.app_id,"REMINDER");
	            }
				
				aeItemMessage itmMsg = new aeItemMessage();
//				itmMsg.updJiReminderSendTimestamp(con, urlp.itm.itm_id, DbItemMessage.TYPE_REMINDER, urlp.ji_reminder_target_datetime);
				aeItem.updateItemReminderSendTimestamp(con, urlp.itm.itm_id, urlp.ji_reminder_target_datetime);
				//对已报名用户插入Reminder邮件
				for (int i=0; i<v_app.size(); i++) {
					  aeApplication app;
	                  app = (aeApplication) v_app.elementAt(i);
	                  aeItemMessage.insNotifyForOnlyReminder(con,prof,app.app_ent_id,urlp.itm.itm_id,app.app_id);
	             }
			}else if(urlp.reminder_no_change.equalsIgnoreCase("never")){
				
				//清空已报名的学员 开课提醒邮件
				for (int i=0; i<v_app.size(); i++) {
					  aeApplication app;
	                app = (aeApplication) v_app.elementAt(i);
	                aeItemMessage.removeNotifyForJI(con,urlp.itm.itm_id,app.app_ent_id,app.app_id,"REMINDER");
	            }
				
		    	aeItemMessage itmMsg = new aeItemMessage();
//				itmMsg.updJiReminderSendTimestamp(con, urlp.itm.itm_id, DbItemMessage.TYPE_REMINDER, null);
				aeItem.updateItemReminderSendTimestamp(con, urlp.itm.itm_id, null);
		    }
		    con.commit();
			response.sendRedirect(urlp.url_success);

        }else if(urlp.cmd.equalsIgnoreCase("ae_auto_upd_attendance") ) {
            urlp.item(clientEnc, static_env.ENCODING);
            AcItem acitm = new AcItem(con);
            if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                        prof.current_role, prof.root_ent_id)) {
                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
            }

            aeAttendance.autoUpdateAttendance(con, prof.usr_id, prof.root_ent_id, urlp.itm.itm_id, true, false);
            con.commit();
            response.sendRedirect(urlp.url_success);

        } else if (urlp.cmd.equalsIgnoreCase("AE_PREP_ITM_REQ") ||
                urlp.cmd.equalsIgnoreCase("AE_PREP_ITM_REQ_XML")) {
            // no access control
            urlp.itmRequirement(clientEnc, static_env.ENCODING);
            StringBuffer xmlBuffer = new StringBuffer();
            aeItem itm = new aeItem();
            itm.itm_id = urlp.itrItmId;
            itm.getItem(con);

            xmlBuffer.append("<item id=\"").append(itm.itm_id).append("\"")
                     .append(" title=\"").append(cwUtils.esc4XML(cwUtils.escNull(itm.itm_title))).append("\"")
                     .append(" itm_tcr_id=\"").append(itm.itm_tcr_id).append("\"")
                     .append(" create_run_ind=\"").append(itm.itm_create_run_ind).append("\"/>");

            xmlBuffer.append("<requirement order=\"").append(urlp.itrOrder)
                            .append("\" type=\"").append(cwUtils.escNull(urlp.itrRequirementType))
                            .append("\" subtype=\"").append(cwUtils.escNull(urlp.itrRequirementSubtype))
                            .append("\" />");

            String result = xmlBuffer.toString();

            result += aeItem.genItemActionNavXML(con, itm.itm_id, prof);
            result = formatXML(result, prof);

            if (urlp.cmd.equalsIgnoreCase("AE_PREP_ITM_REQ_XML")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        } else if (urlp.cmd.equalsIgnoreCase("AE_INS_ITM_REQ")) {
           
            urlp.itmRequirement(clientEnc, static_env.ENCODING);
            //access control
		    AcItem acitm = new AcItem(con);
		    if(wizbini.cfgTcEnabled && !acitm.hasUpdPrivilege(urlp.itrItmId, prof.usr_ent_id,prof.current_role, prof.root_ent_id)) {
			       throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
		    }
            aeItemRequirement aeItmReq = new aeItemRequirement();
            String conditionRule = "";
            if(urlp.itrConditionType.equalsIgnoreCase(aeItemRequirement.COND_TYPE_AICC_SCRIPT)) {
                AICCScriptWriter aiccWriter = new AICCScriptWriter();
                String [] element;
                if (urlp.itrRequirementSubtype.equalsIgnoreCase(aeItemRequirement.REQ_SUBTYPE_USER)){
                    element = urlp.reqEntLst;
                }else{
                    element = aiccWriter.convertToElement(AICCScriptWriter.ELEMENT_TYPE_ITEM, urlp.reqItmId);
                }

                if(urlp.reqOperator.equalsIgnoreCase("AND")) {
                    conditionRule = aiccWriter.writeAllOf(element);
                } else if(urlp.reqOperator.equalsIgnoreCase("OR")) {
                    conditionRule = aiccWriter.writeAnyOf(element);
                }
            } else if(urlp.itrConditionType.equalsIgnoreCase(aeItemRequirement.COND_TYPE_STATEMENT)) {
                conditionRule = urlp.itrConditionRule;
            }

            aeItmReq.ins(con, urlp.itrItmId, urlp.itrOrder, urlp.itrRequirementType,
                    urlp.itrRequirementSubtype, urlp.itrRequirementRestriction,
                    urlp.itrRequirementDueDate, urlp.itrAppnFootnoteInd,
                    urlp.itrConditionType, conditionRule,
                    urlp.posIaaToAttStatus, urlp.negIaaToAttStatus,
                    urlp.itrProcExecuteTimestamp,prof.usr_id);
            con.commit();
            cwSysMessage e = new cwSysMessage("GEN004");
            msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
        } else if (urlp.cmd.equalsIgnoreCase("AE_UPD_ITM_REQ")) {
			urlp.itmRequirement(clientEnc, static_env.ENCODING);
           //access control
		   AcItem acitm = new AcItem(con);
		   if(wizbini.cfgTcEnabled && !acitm.hasUpdPrivilege(urlp.itrItmId, prof.usr_ent_id,prof.current_role, prof.root_ent_id)) {
			   throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
		   }
            
            aeItemRequirement aeItmReq = new aeItemRequirement();
            String conditionRule = "";
            if(urlp.itrConditionType.equalsIgnoreCase(aeItemRequirement.COND_TYPE_AICC_SCRIPT)) {
                AICCScriptWriter aiccWriter = new AICCScriptWriter();
                String [] element;
                if (urlp.itrRequirementSubtype.equalsIgnoreCase(aeItemRequirement.REQ_SUBTYPE_USER)){
                    element = urlp.reqEntLst;
                }else{
                    element = aiccWriter.convertToElement(AICCScriptWriter.ELEMENT_TYPE_ITEM, urlp.reqItmId);
                }

                if(urlp.reqOperator.equalsIgnoreCase("AND")) {
                    conditionRule = aiccWriter.writeAllOf(element);
                } else if(urlp.reqOperator.equalsIgnoreCase("OR")) {
                    conditionRule = aiccWriter.writeAnyOf(element);
                }
            } else if(urlp.itrConditionType.equalsIgnoreCase(aeItemRequirement.COND_TYPE_STATEMENT)) {
                conditionRule = urlp.itrConditionRule;
            }

            boolean flag = aeItmReq.upd(con, urlp.itrItmId, urlp.itrOrder, urlp.itrRequirementType,
                    urlp.itrRequirementSubtype, urlp.itrRequirementRestriction,
                    urlp.itrRequirementDueDate, urlp.itrAppnFootnoteInd,
                    urlp.itrConditionType, conditionRule,
                    urlp.posIaaToAttStatus, urlp.negIaaToAttStatus,
                    urlp.itrProcExecuteTimestamp,prof.usr_id,urlp.lastUpdTime);
            if(!flag){
				cwSysMessage e = new cwSysMessage(SMSG_CHANGED_MSG);
				msgBox(MSG_STATUS, con, e, prof, urlp.url_failure, out);
            }else{
				con.commit();
				cwSysMessage e = new cwSysMessage("GEN003");
				msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
            }
            

        } else if (urlp.cmd.equalsIgnoreCase("AE_SAVE_ITM_REQ_DUE_DATE")) {
            // no access control
            urlp.itmRequirement(clientEnc, static_env.ENCODING);

            aeItemRequirement aeItmReq = new aeItemRequirement();
            aeItmReq.saveDueDate(con, urlp.itrItmId, urlp.itrOrder
                                ,urlp.itrRequirementDueDate);
            con.commit();
            cwSysMessage e = new cwSysMessage("GEN003");
            msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);

        } else if (urlp.cmd.equalsIgnoreCase("AE_DEL_ITM_REQ")) {
            // no access control
            urlp.itmRequirement(clientEnc, static_env.ENCODING);
            //access control
		    AcItem acitm = new AcItem(con);
		    if(wizbini.cfgTcEnabled && !acitm.hasUpdPrivilege(urlp.itrItmId, prof.usr_ent_id,
									 prof.current_role, prof.root_ent_id)) {
			   throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
		    }
            aeItemRequirement aeItmReq = new aeItemRequirement();
            aeItmReq.del(con, urlp.itrItmId, urlp.itrOrder);
            con.commit();
            cwSysMessage e = new cwSysMessage("GEN002");
            msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
        } else if (urlp.cmd.equalsIgnoreCase("AE_GET_ITM_REQ") ||
                urlp.cmd.equalsIgnoreCase("AE_GET_ITM_REQ_XML")) {
            // no access control
            urlp.itmRequirement(clientEnc, static_env.ENCODING);
            aeItemRequirement aeItmReq = new aeItemRequirement();
            String result = "";
           
            if(urlp.itrOrder != 0) {
                result = aeItmReq.asXML(con, urlp.itrItmId, urlp.itrOrder);
            } else {
                result = aeItmReq.asXML(con, urlp.itrItmId);
            }
            
            result += aeItem.genItemActionNavXML(con, urlp.itrItmId, prof);
            result = formatXML(result, prof);

            if (urlp.cmd.equalsIgnoreCase("AE_GET_ITM_REQ_XML")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        } else if (urlp.cmd.equalsIgnoreCase("AE_GET_APPN_STATUS") ||
                urlp.cmd.equalsIgnoreCase("AE_GET_APPN_STATUS_XML")) {
            // no access control
            urlp.queueManagement(clientEnc, static_env.ENCODING);
            StringBuffer xml = aeApplication.getApplicationStatusAsXML(con, urlp.app_id);
            //Page Variant Object to answer Xsl Questions
            AcPageVariant acPageVariant = new AcPageVariant(con);
            acPageVariant.ent_owner_ent_id = prof.root_ent_id;
            acPageVariant.ent_id = prof.usr_ent_id;
            acPageVariant.rol_ext_id = prof.current_role;
            String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));

            String result = formatXML(xml.toString(), metaXML, APPLYEASY, prof);

            if (urlp.cmd.equalsIgnoreCase("AE_GET_APPN_STATUS_XML")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }else if (urlp.cmd.equalsIgnoreCase("ae_make_itm_approval_actn")) {
            urlp.item(clientEnc, static_env.ENCODING);

            urlp.itm.getItem(con);
            AcItem acitm = new AcItem(con);
            if(!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id,
                                        prof.current_role, prof.root_ent_id)) {
                throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
            }

            AcItemPageVariant acItmPV = new AcItemPageVariant(con);

           

            urlp.itm.makeApprovalActn(con, urlp.approval_action, prof.usr_id);
            con.commit();
            response.sendRedirect(urlp.url_success);

        } else if (urlp.cmd.equalsIgnoreCase("AE_GET_MY_APPN_APPROVAL_LST") ||
                urlp.cmd.equalsIgnoreCase("AE_GET_MY_APPN_APPROVAL_LST_XML")) {
            // no access control
            urlp.queueManagement(clientEnc, static_env.ENCODING);
            urlp.pagination();

            aeQueueManager qm = new aeQueueManager();

            StringBuffer xml = qm.getMyAppnApprovalLstAsXML(con, prof, urlp.aal_status, false, urlp.cwPage);

            String result = formatXML(xml.toString(), null, APPLYEASY, prof);

            if (urlp.cmd.equalsIgnoreCase("AE_GET_MY_APPN_APPROVAL_LST_XML")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        } else if(urlp.cmd.equalsIgnoreCase("SETTING_INSTR")||urlp.cmd.equalsIgnoreCase("REMOVE_INSTR")){
            int[] ili_usr_ent_id_lst = urlp.getInstrLst4aeItemLesson();
            if(ili_usr_ent_id_lst.length > 0){
                aeItemLessonInstructor ili = urlp.getParam2aeItemLessonInstructor(con);
                if(ili.ili_ils_id > 0){
                    for(int i=0;i<ili_usr_ent_id_lst.length;i++){
                        if(ili_usr_ent_id_lst[i]!=0){
                            ili.ili_usr_ent_id = ili_usr_ent_id_lst[i];
                            if(urlp.cmd.equalsIgnoreCase("REMOVE_INSTR"))
                            {
                                ili.del(con);
                            }else{
                                ili.ins(con);
                            }
                        }
                    }
                }
                con.commit();
                if(urlp.cmd.equalsIgnoreCase("REMOVE_INSTR")){
                    msgBox(MSG_STATUS, con, new cwSysMessage("GEN002"), (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE), urlp.url_success, out);
                }else{
                    msgBox(MSG_STATUS, con, new cwSysMessage("GEN003"), (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE), urlp.url_success, out);    
                }
            }
        } else if (urlp.cmd.equalsIgnoreCase("AE_GET_LESSON_INFO") || 
                   urlp.cmd.equalsIgnoreCase("AE_GET_LESSON_INFO_XML")) {
            urlp.item(clientEnc, static_env.ENCODING);
            // no access control
            aeItem itm = urlp.itm;
            itm.get(con, urlp.tnd_id);
            //itm.setQRInd(con, prof);
            StringBuffer resultBuf = new StringBuffer();
            String metaXML = null;
            resultBuf.append(itm.contentInfoAsXML(con));
            if (prof != null) {
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.instance_id = itm.itm_id;
                acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                acPageVariant.ent_id = prof.usr_ent_id;
                acPageVariant.rol_ext_id = prof.current_role;
                metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
            }
            aeItemLesson lesson = new aeItemLesson();
            if (urlp.itm.itm_create_run_ind) {
                resultBuf.append(lesson.getListAsXML(con, itm.itm_id));
            } else {
                resultBuf.append(lesson.getRunListAsXML(con, itm.itm_id));
            }
            resultBuf.append(aeItem.genItemActionNavXML(con, itm.itm_id, prof));
            String result = formatXML(resultBuf.toString(), metaXML, APPLYEASY, prof, urlp.stylesheet);
            if (urlp.cmd.equalsIgnoreCase("AE_GET_LESSON_INFO_XML")) {
                out.println(result);
            }
            else {
                String xsl_root = (prof == null) ? null : prof.xsl_root;
                generalAsHtml(result, out, urlp.stylesheet, xsl_root);
            }

        } else if (urlp.cmd.equalsIgnoreCase("AE_UPD_LESSON") 
                    || urlp.cmd.equalsIgnoreCase("AE_UPD_LESSON_XML")) {
            urlp.ilsRequest(clientEnc, static_env.ENCODING);
            urlp.item(clientEnc, static_env.ENCODING);
    
            // no access control
            aeItem itm = urlp.itm;
            itm.get(con, urlp.tnd_id);
            AcItem acitm = new AcItem(con);
            StringBuffer resultBuf = new StringBuffer();
            String metaXML = null;
            aeItemLesson lesson = urlp.itmLessonReq;
            resultBuf.append(itm.contentInfoAsXML(con)).append("<act_type ").append("maxday=\"").append(lesson.getMaxDay(con)).append("\" >" + urlp.ils_act_type + "</act_type>");
            cwSysMessage message = null;
            String title = "";
            if (urlp.ils_act_type.indexOf("edit") > 0) {
                if (!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                    throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                }
            } else if (urlp.ils_act_type.indexOf("save") > 0) {
                if (!acitm.hasUpdPrivilege(urlp.itm.itm_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                    throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
                }
            } else if (urlp.ils_act_type.indexOf("delete") > 0) {
                if (!acitm.hasMaintainPrivilege(urlp.itm.itm_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id)) {
                    throw new cwSysMessage(dbMessage.NO_RIGHT_DELETE_MSG);
                }
            }
            if (urlp.ils_act_type.equalsIgnoreCase("edit")) {
                //enter unit edit page
                resultBuf.append(lesson.getSingleAsXML(con, urlp.ils_id));
            } else if (urlp.ils_act_type.equalsIgnoreCase("new")) {
                ;
            } else if (urlp.ils_act_type.equalsIgnoreCase("save")) {
                if (urlp.ils_id != 0) {
                    //update training unit
                    if (!lesson.checkTimePeriod(con, lesson.ils_id)) {
                        lesson.ils_update_usr_id = prof.usr_id;
                        if (lesson.checkConflict(con, urlp.ils_id, lesson.ils_update_timestamp)) {
                            if (lesson.upd(con)) {
                                if(lesson.checkTimeConflictByItem(con)){
                                	 con.rollback();
                                    message = new cwSysMessage("ILS011");
                                    msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                                }else {
                                    con.commit();
                                    message = new cwSysMessage("GEN003");
                                    msgBox(MSG_STATUS, con, message, prof, urlp.url_success, out);
                                }
                            } else {
                            	con.rollback();
                                message = new cwSysMessage("GEN006");
                                msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                            }
                        } else {
                        	con.rollback();
                            message = new cwSysMessage("GEN006");
                            msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                        }
                    } else {
                        message = new cwSysMessage("ILS007");
                        msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                    }
                } else {
                    //insert training unit
                    if (!urlp.itmLessonReq.checkTimePeriod(con, lesson.ils_id)) {
                        lesson.ils_create_usr_id = prof.usr_id;
                        lesson.ils_update_usr_id = prof.usr_id;
                        if (urlp.itmLessonReq.ins(con)) {
                            con.commit();
                            message = new cwSysMessage("GEN001");
                            msgBox(MSG_STATUS, con, message, prof, urlp.url_success, out);
                        } else {
                        	con.rollback();
                            message = new cwSysMessage("ILS003");
                            msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                        }
                    } else {
                        message = new cwSysMessage("ILS007");
                        msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                    }
                }
            } else if (urlp.ils_act_type.equalsIgnoreCase("delete")) {
                //delete training unit
                if(lesson.checkConflict(con, urlp.ils_id, lesson.ils_update_timestamp)){
                    aeItemLessonInstructor.delByLesson(con, urlp.ils_id);
                    if (lesson.del(con, urlp.ils_id)) {
                        con.commit();
                        message = new cwSysMessage("GEN002");
                        msgBox(MSG_STATUS, con, message, prof, urlp.url_failure, out);
                    } else {
                    	con.rollback();
                        message = new cwSysMessage("ILS006");
                        msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                    }
                }else{
                    message = new cwSysMessage("ILS006");
                    msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                }
            } else if (urlp.ils_act_type.equalsIgnoreCase("set_date")) {
                //enter set date page
                resultBuf.append(lesson.getDateListAsXML(con, urlp.itm_id));
            } else if (urlp.ils_act_type.equalsIgnoreCase("set_date_save")) {
                //save unit date
                if (lesson.setDate(con, urlp.itm_id, urlp.ils_day_date_Req)) {
                    if(lesson.checkTimeConflictByItem(con)){
                        message = new cwSysMessage("ILS010");
                        msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                    } else {
                        con.commit();
                        message = new cwSysMessage("ILS008");
                        msgBox(MSG_STATUS, con, message, prof, urlp.url_success, out);
                    }
                } else {
                	con.rollback();
                    message = new cwSysMessage("ILS009");
                    msgBox(MSG_ERROR, con, message, prof, urlp.url_failure, out);
                }
            } 
            if (message == null && title.length() == 0) {
            	resultBuf.append(aeItem.genItemActionNavXML(con, urlp.itm_id, prof));
            	resultBuf.append(aeItem.genItemActionNavXML(con, urlp.itm_id, prof));
                String result = formatXML(resultBuf.toString(), metaXML, APPLYEASY, prof);
                if (urlp.cmd.equalsIgnoreCase("AE_UPD_LESSON_XML")) {
                    out.println(result);
                } else {
                    String xsl_root = (prof == null) ? null : prof.xsl_root;
                    generalAsHtml(result.toString(), out, urlp.stylesheet, xsl_root);
                }
            }
            
        }
        else if (urlp.cmd.equals("ae_set_content_def")) {
            urlp.contentDef();
            urlp.itm.setContentDef(con);
            //更新class 的ccr_ccr_id_parent
            DbCourseCriteria.updClsCcrParent(con, urlp.itm.itm_id);
            con.commit();
            response.sendRedirect(urlp.url_success);
        }
        //简易添加课程
        else if (urlp.cmd.equals("ae_simple_ins_itm_perp") || urlp.cmd.equals("ae_simple_ins_itm_perp_xml")) {               		

    		urlp.item(clientEnc, static_env.ENCODING);
    		//自动报名
			urlp.itm.auto_enroll_interval =  loadTargetLrnCacheAndCourseEnrollScheduler.Auto_Enroll_Interval;
            String metaXml="";
            String itmXml="";
            itmXml+=getFormInsItmXML(con, sess, urlp, prof);
            metaXml+="<itm_content_def>"+cwUtils.escNull(urlp.itm.itm_content_def)+"</itm_content_def>";
            metaXml+="<training_type>"+urlp.training_type+"</training_type>";
            metaXml+="<itm_integrated_ind>"+urlp.itm.itm_integrated_ind+"</itm_integrated_ind>";
            metaXml+="<cur_time>"+cwSQL.getTime(con)+"</cur_time>";
            String result = formatXML(itmXml, metaXml, APPLYEASY,prof, urlp.stylesheet);
            //System.out.println(result);
            if (urlp.cmd.equals("ae_form_ins_itm_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }
        //简易添加课程--面授课程第二步，设置内容模式
        else if (urlp.cmd.equals("ae_set_content_info") || urlp.cmd.equals("ae_set_content_info_xml")) {               		

    		urlp.item(clientEnc, static_env.ENCODING);
    		//自动报名
            String metaXml="";
            metaXml+="<itm_dummy_type>"+urlp.itm.itm_dummy_type+"</itm_dummy_type>";
            metaXml+="<wrk_tpl_id>"+urlp.wrk_tpl_id+"</wrk_tpl_id>";
            metaXml+="<ity_id>"+urlp.ity_id+"</ity_id>";
            metaXml+="<itm_app_approval_type>"+urlp.itm.itm_app_approval_type+"</itm_app_approval_type>";
            metaXml+="<tvw_id>"+urlp.tvw_id+"</tvw_id>";
            metaXml+="<training_type>"+urlp.training_type+"</training_type>";
            metaXml+="<itm_integrated_ind>"+urlp.itm.itm_integrated_ind+"</itm_integrated_ind>";
            metaXml+="<cur_time>"+cwSQL.getTime(con)+"</cur_time>";
            String result = formatXML(null, metaXml, APPLYEASY,prof, urlp.stylesheet);
            //System.out.println(result);
            if (urlp.cmd.equals("ae_set_content_info_xml")) {
                out.println(result);
            } else {
                generalAsHtml(result, out, urlp.stylesheet, prof.xsl_root);
            }
        }
	    else if (urlp.cmd.equals("ae_simple_ins_itm") || urlp.cmd.equals("ae_simple_ins_itm_xml")) {
	            urlp.item(clientEnc, static_env.ENCODING);
	            long itm_id_ = 0;  
	            long cos_id_ = 0;
	            String itm_content_def_ = "";
	            String dummy_type = urlp.itm.itm_dummy_type;
	            Timestamp cur_time = cwSQL.getTime(con);
	            //if(urlp.training_plan && urlp.tpn_itm_run_ind){}
	            try{	               
	                urlp.itm.itm_owner_ent_id = prof.root_ent_id;
	                urlp.itm.getItemTypeInd(con);

                	//重读：允许
                	urlp.itm.itm_retake_ind = true;
                	if(urlp.vColName.contains("itm_retake_ind")){
                		int code_index = urlp.vColName.indexOf("itm_retake_ind");
                        urlp.vColValue.setElementAt(urlp.itm.itm_retake_ind, code_index);
                	}else{
	                	urlp.vColName.addElement("itm_retake_ind");
						urlp.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
						urlp.vColValue.addElement(urlp.itm.itm_retake_ind);
                	}
                	
                	//图标
                	urlp.itm.itm_icon = "1.png";
                	if(urlp.vColName.contains("itm_icon")){
                		int code_index = urlp.vColName.indexOf("itm_icon");
                        urlp.vColValue.setElementAt(urlp.itm.itm_icon, code_index);
                	}else{
	                	urlp.vColName.addElement("itm_icon");
						urlp.vColType.addElement(DbTable.COL_TYPE_STRING);
						urlp.vColValue.addElement(urlp.itm.itm_icon);
                	}
                	
	                if(urlp.itm.itm_type.equalsIgnoreCase(aeItem.ITM_TYPE_SELFSTUDY) || urlp.itm.itm_type.equalsIgnoreCase(aeItem.ITM_TYPE_INTEGRATED)){
	                	urlp.itm.itm_appn_start_datetime = cur_time;
	                	if(urlp.vColName.contains("itm_appn_start_datetime")){
	                		int code_index = urlp.vColName.indexOf("itm_appn_start_datetime");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_appn_start_datetime, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_appn_start_datetime");
							urlp.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
							urlp.vColValue.addElement(urlp.itm.itm_appn_start_datetime);
	                	}

	                	urlp.itm.itm_appn_end_datetime = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
	                	if(urlp.vColName.contains("itm_appn_end_datetime")){
	                		int code_index = urlp.vColName.indexOf("itm_appn_end_datetime");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_appn_end_datetime, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_appn_end_datetime");
							urlp.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
							urlp.vColValue.addElement(urlp.itm.itm_appn_end_datetime);
	                	}

	                	//itm_content_eff_duration
	                	urlp.itm.itm_content_eff_duration = 0;
	                	if(urlp.vColName.contains("itm_content_eff_duration")){
	                		int code_index = urlp.vColName.indexOf("itm_content_eff_duration");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_content_eff_duration, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_content_eff_duration");
							urlp.vColType.addElement(DbTable.COL_TYPE_INT);
							urlp.vColValue.addElement(urlp.itm.itm_content_eff_duration);
	                	}
	                	
	                	//itm_notify_days
	                	urlp.itm.itm_notify_days = -1;
	                	if(urlp.vColName.contains("itm_notify_days")){
	                		int code_index = urlp.vColName.indexOf("itm_notify_days");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_notify_days, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_notify_days");
							urlp.vColType.addElement(DbTable.COL_TYPE_LONG);
							urlp.vColValue.addElement(urlp.itm.itm_notify_days);
	                	}

	                	urlp.itm.itm_inst_type = "IN";
	                	if(urlp.vColName.contains("itm_inst_type")){
	                		int code_index = urlp.vColName.indexOf("itm_inst_type");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_inst_type, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_inst_type");
							urlp.vColType.addElement(DbTable.COL_TYPE_STRING);
							urlp.vColValue.addElement(urlp.itm.itm_inst_type);
	                	}
	                	
	                	//发送报名通知：是
	                	urlp.itm.itm_send_enroll_email_ind = 1;
	                	if(urlp.vColName.contains("itm_send_enroll_email_ind")){
	                		int code_index = urlp.vColName.indexOf("itm_send_enroll_email_ind");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_send_enroll_email_ind, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_send_enroll_email_ind");
							urlp.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
							urlp.vColValue.addElement(urlp.itm.itm_send_enroll_email_ind);
	                	}
	                	
	                	//自动积分：否
	                	urlp.itm.itm_bonus_ind=false;
	                	if(urlp.vColName.contains("itm_bonus_ind")){
	                		int code_index = urlp.vColName.indexOf("itm_bonus_ind");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_bonus_ind, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_bonus_ind");
							urlp.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
							urlp.vColValue.addElement(urlp.itm.itm_bonus_ind);
	                	}
	                }else if(urlp.itm.itm_run_ind){
	                	//itm_notify_days
	                	urlp.itm.itm_notify_days = -1;
	                	if(urlp.vColName.contains("itm_notify_days")){
	                		int code_index = urlp.vColName.indexOf("itm_notify_days");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_notify_days, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_notify_days");
							urlp.vColType.addElement(DbTable.COL_TYPE_LONG);
							urlp.vColValue.addElement(urlp.itm.itm_notify_days);
	                	}

	                	urlp.itm.itm_inst_type = "IN";
	                	if(urlp.vColName.contains("itm_inst_type")){
	                		int code_index = urlp.vColName.indexOf("itm_inst_type");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_inst_type, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_inst_type");
							urlp.vColType.addElement(DbTable.COL_TYPE_STRING);
							urlp.vColValue.addElement(urlp.itm.itm_inst_type);
	                	}
	                	
	                	//发送报名通知：是
	                	urlp.itm.itm_send_enroll_email_ind = 1;
	                	if(urlp.vColName.contains("itm_send_enroll_email_ind")){
	                		int code_index = urlp.vColName.indexOf("itm_send_enroll_email_ind");
	                        urlp.vColValue.setElementAt(urlp.itm.itm_send_enroll_email_ind, code_index);
	                	}else{
		                	urlp.vColName.addElement("itm_send_enroll_email_ind");
							urlp.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
							urlp.vColValue.addElement(urlp.itm.itm_send_enroll_email_ind);
	                	}
	                	
	                }else{
	                    // for itm_content_def, inherit the value from parent item
						urlp.vColName.addElement("itm_content_def");
						urlp.vColType.addElement(DbTable.COL_TYPE_STRING);
						urlp.vColValue.addElement(urlp.itm.itm_content_def);
	                }
	                
	                long tnd_id = urlp.tnd_parent_tnd_id;
	                long parent_itm_id = 0;
	                //if it is a run, get the parent item id from session
	                if(urlp.itm.itm_run_ind || urlp.itm.itm_session_ind) {
	                    Long temp = (Long)sess.getAttribute(SESS_PARENT_ITM_ID);
	                    
	                    if(temp != null) {
	                        parent_itm_id = temp.longValue();
	                        urlp.itm.parent_itm_id = parent_itm_id;
	                    }else{
	                    	CommonLog.info("temp is null");
	                    }
	                }
	                //get template types
	                ViewItemTemplate viItmTpl = new ViewItemTemplate();
	                viItmTpl.ownerEntId = prof.root_ent_id;
	                viItmTpl.itemType = urlp.itm.itm_type;
	                viItmTpl.runInd = urlp.itm.itm_run_ind;
	                viItmTpl.sessionInd = urlp.itm.itm_session_ind;
	                String[] templateTypes = viItmTpl.getItemTypeTemplates(con);
	                
	                if(!wizbini.cfgTcEnabled) {
	                	long sup_tcr_id = DbTrainingCenter.getSuperTcId(con, prof.root_ent_id);
	                	urlp.itm.itm_tcr_id = sup_tcr_id;
	                }
	
	
	                //find out the template id
	                long[] tpl_ids;
	                // get own template for non-run item (for session, no need to add parent template)
	                if(!urlp.itm.itm_run_ind) {
	                    tpl_ids = getTplIds(con, sess, urlp.itm.itm_type,
	                                        templateTypes, urlp.itm.itm_session_ind, prof.root_ent_id);
	                    urlp.itm.itm_app_approval_type = (String)sess.getAttribute(SESS_ITM_APP_APPROVAL_TYPE);
	                }
	                else {
	                    tpl_ids = getRunTplIds(con, sess, urlp.itm.itm_type,
	                                            templateTypes, prof.root_ent_id,
	                                            parent_itm_id);
	
	                    aeItem parent = new aeItem();
	                    parent.itm_id = parent_itm_id;
	                    parent.getItem(con);
		                itm_content_def_ = parent.itm_content_def;
	                    urlp.itm.itm_app_approval_type = parent.itm_app_approval_type;
	                    // for itm_content_def, inherit the value from parent item
						urlp.vColName.addElement("itm_content_def");
						urlp.vColType.addElement(DbTable.COL_TYPE_STRING);
						urlp.vColValue.addElement(parent.itm_content_def);
						
	                    // for itm_content_def, inherit the value from parent item
						urlp.vColName.addElement("itm_target_enrol_type");
						urlp.vColType.addElement(DbTable.COL_TYPE_STRING);
						urlp.vColValue.addElement(aeItem.ITM_TARGET_ENROLL_TYPE_TARGET_LEARNER);
						
						//继承证书
						for (int i = 0; i <urlp.vColName.size(); i++) {
							if("itm_cfc_id".equals((String)urlp.vColName.get(i))){
								urlp.vColValue.set(i,parent.itm_cfc_id);
							}
						}
						
	                }
	
	                aeItem itm = urlp.itm;
	                aeItemExtension itmExtension = urlp.itmExtension;
	                //access control
	                AcItem acitm = new AcItem(con);
	                if(!acitm.hasInsPrivilege(prof.usr_ent_id, prof.current_role, tnd_id, prof.root_ent_id)) {
	                    throw new cwSysMessage(dbMessage.NO_RIGHT_INSERT_MSG);
	                }

	                Hashtable data = new Hashtable();
	                for(int i=0; i< urlp.vColName.size(); i++){
	                	data.put((String)urlp.vColName.get(i), ""+urlp.vColValue.get(i));
	                }
	                itm.generateItemXML(con, data);
	                if(urlp.vClobColName != null && urlp.vClobColName.indexOf("itm_xml") == -1 ){
	                	urlp.vClobColName.addElement("itm_xml");
	                	urlp.vClobColValue.addElement(itm.itm_xml);
	                }
	                
                    dbCourse cos = new dbCourse();
                    cos.res_lan = prof.label_lan;
                    cos.res_title = itm.itm_title;
                    cos.res_usr_id_owner = prof.usr_id;
                    cos.res_upd_user = prof.usr_id;
                    itm.insWZBCourse(con, prof, cos, tnd_id,
                            templateTypes, tpl_ids,
                            urlp.vColName, urlp.vColType, urlp.vColValue,
                            urlp.vClobColName, urlp.vClobColValue);

                    if(urlp.iac_id_lst == null){
                    	urlp.iac_id_lst = new String[1];
                    	urlp.iac_id_lst[0]  = "TADM_1~"+prof.usr_ent_id;
                    }
	                itm.insExternalInfo(con,
	                                    urlp.tnd_id_lst,
										urlp.tnd_id_lst_value,
	                                    urlp.target_ent_group_lst,
	                                    urlp.comp_target_ent_group_lst,
	                                    urlp.cost_center_group_lst,
	                                    urlp.iac_id_lst,
	                                    urlp.mote,
	                                    urlp.cm_lst,
	                                    urlp.fgt_id_vec,
	                                    urlp.fig_val_vec,
	                                    prof.usr_id,
	                                    prof.usr_ent_id,
	                                    prof.root_ent_id,
	                                    wizbini.cfgSysSetupadv.getDefaultTaId(),
	                                    wizbini.cfgTcEnabled);
	                
	                itmExtension.ins(con, itm.itm_id, urlp.vExtensionColName, urlp.vExtensionColType, urlp.vExtensionColValue);

		            // saveDirPath
	                String saveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + itm.itm_id;
	                String defaultDirPath = static_env.DOC_ROOT+ dbUtils.SLASH +static_env.DEFAULT_IMGLIG + dbUtils.SLASH +static_env.INI_ITM_DIR_UPLOAD_URL+ dbUtils.SLASH + itm.itm_icon;
	                dbUtils.copyFile(defaultDirPath, saveDirPath);
	                
	                itm_id_ = itm.itm_id;
	                cos_id_ = cos.cos_res_id;
	                // duplicate course content to the newly added class
	                if (urlp.itm.itm_run_ind) {
	                    CourseContentUtils.propagateCourseContent(con, parent_itm_id, itm_id_, prof, false, wizbini.getFileUploadResDirAbs());
	                }
	            }catch(cwSysMessage e) {
	            	con.rollback();
	            	msgBox(MSG_ERROR, con, e, prof, urlp.url_failure, out);
					/*urlp.getGetItemSubmittedParams("");
					urlp.itm.itm_dummy_type = dummy_type;
					StringBuffer metaXML = new StringBuffer();
					metaXML.append(e.getErrorMessageXML(prof.label_lan));
					metaXML.append(urlp.getSubmittedParamsListXML());
					String xml_tp = "";
					if(urlp.training_plan) {
						sess.setAttribute("PLAN_ITM_SESS", sess.getAttribute("PLAN_ITM_SESS_TEMP"));
	                	dbTpTrainingPlan tpTp = new dbTpTrainingPlan();
	                	tpTp.tpn_id = urlp.plan_id;
	                	tpTp.get(con);
	                	tpTp.tpn_update_timestamp = urlp.tpn_update_timestamp;
	                	xml_tp = tpPlanManagement.getPlanDetailsAsXML(con, tpTp); 
	                	metaXML.append("<training_plan>true</training_plan>");
	                	if(urlp.itm.itm_run_ind || urlp.itm.itm_session_ind) {
	                    	Long temp2 = (Long)sess.getAttribute(SESS_PARENT_ITM_ID);
	                    	sess.removeAttribute(SESS_PARENT_ITM_ID);
	                    	sess.setAttribute(SESS_PARENT_ITM_ID, new Long(temp2.longValue()));
	                	}
					}
						metaXML.append("<training_type>").append(urlp.training_type).append("</training_type>");
						metaXML.append("<itm_integrated_ind>").append(urlp.itm.itm_integrated_ind).append("</itm_integrated_ind>");
					String result = formatXML(xml_tp + getFormInsItmXML(con, sess, urlp, prof), metaXML.toString(), APPLYEASY, prof, urlp.stylesheet);
					generalAsHtml(result.toString(), out, urlp.stylesheet, prof.xsl_root);*/
				
					return;
	            }
	            if((urlp.itm.itm_type.equalsIgnoreCase(aeItem.ITM_TYPE_CLASSROOM) && ("".equalsIgnoreCase(itm_content_def_) || "PARENT".equalsIgnoreCase( itm_content_def_)))
	            	|| urlp.itm.itm_type.equals(aeItem.ITM_TYPE_INTEGRATED)
	            		){
		            urlp.url_success = cwUtils.substituteURL(urlp.url_success, itm_id_);
	            }else{
		            urlp.url_success = cwUtils.substituteURL(urlp.url_success, itm_id_);
	            }
	            String sysMsg=aeItem.ITM_ADD_OK;
	            con.commit();
	            cwSysMessage e = new cwSysMessage(sysMsg);
/*				aeItem itm = urlp.itm;
	            if (itm.itm_run_ind || urlp.training_plan){
					msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
	            }
	            else{
	            	StringBuffer extMsg = new StringBuffer();
	            	extMsg.append("<training_type>").append(urlp.training_type).append("</training_type>");
	            	genMsgBox(MSG_STATUS, con, e, prof, urlp.url_success, out,urlp.stylesheet, extMsg.toString());
	            }*/
	            msgBox(MSG_STATUS, con, e, prof, urlp.url_success, out);
	            //response.sendRedirect(urlp.url_success);
	    }
        else {
            throw new qdbException("unknown command " + cwUtils.esc4Html(urlp.cmd));
        }

    }

  private void generalAsHtml(String xmlOL, PrintWriter out, String stylesheet, String xsl_root)
    throws cwException
  {
      if(stylesheet == null || stylesheet.length() == 0)
        throw new cwException("Invalid stylesheet");

        static_env.procXSLFile(xmlOL, stylesheet, out, xsl_root);
  }


  public static long[] usrGroups(long usr_ent_id, Vector v) {
    long[] list = new long[v.size() + 1];
    list[0] = usr_ent_id;
    for(int i=0;i<v.size();i++)
        list[i+1] = ((Long) v.elementAt(i)).longValue();

    return list;
  }



  private String formatXML(String in, loginProfile prof) throws IOException{

        return formatXML(in, prof, null);
  }

  private String formatXML(String in, loginProfile prof, String stylesheet) throws IOException {

    StringBuffer outBuf = new StringBuffer(2500);

    outBuf.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
    outBuf.append("<applyeasy>").append(dbUtils.NEWL);

    outBuf.append("<meta>").append(dbUtils.NEWL);
	if(prof!=null){
    	outBuf.append(prof.asXML()).append(dbUtils.NEWL);
	}
	if( stylesheet != null && stylesheet.length() > 0 ) {
		outBuf.append(cwXMLLabel.get(stylesheet, prof.xsl_root, prof.label_lan)).append(dbUtils.NEWL);
	}
	outBuf.append("<tc_enabled>").append(wizbini.cfgTcEnabled).append("</tc_enabled>");
 
    outBuf.append("</meta>").append(dbUtils.NEWL);
    outBuf.append(in).append(dbUtils.NEWL);
    outBuf.append("</applyeasy>");

    String out = new String(outBuf);
    return out;
  }

    /**
    Include a module specific open-end tag and user profile xml with the input data XML
    @param dataXML input data XML
    @param metaXML meta XML
    @param moduleName start, end root tag (e.g. "applyeasy")
    @param stylesheet used to get the related xml label
    @return an XML contain <cur_usr> and the input data XML
    */
    private String formatXML(String dataXML, String metaXML, String moduleName, loginProfile prof) throws IOException{
        return formatXML(dataXML, metaXML, moduleName, prof, null);
    }

    public String formatXML(String dataXML, String metaXML, String moduleName, loginProfile prof, String stylesheet) throws IOException{

        StringBuffer outBuf = new StringBuffer(2500);

        outBuf.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
        outBuf.append("<").append(moduleName).append(">").append(dbUtils.NEWL);
        outBuf.append("<meta>").append(dbUtils.NEWL);
        if(prof!=null) {
            outBuf.append(prof.asXML()).append(dbUtils.NEWL);
        }
        if(metaXML != null) {
            outBuf.append(metaXML).append(dbUtils.NEWL);
        }
        if( stylesheet != null && stylesheet.length() > 0 ) {
            outBuf.append(cwXMLLabel.get(stylesheet, prof.xsl_root, prof.label_lan)).append(dbUtils.NEWL);
        }
        outBuf.append("<tc_enabled>").append(wizbini.cfgTcEnabled).append("</tc_enabled>");
        outBuf.append("</meta>").append(dbUtils.NEWL);
        outBuf.append(dataXML).append(dbUtils.NEWL);
        outBuf.append("</").append(moduleName).append(">");

        String out = new String(outBuf);
        return out;
    }

  private static long getTplId(Connection con, HttpSession sess, String tpl_type, long owner_ent_id) throws SQLException, qdbException{
    Long temp = null;
    long tpl_id;

    if (sess != null) {
        temp = (Long)sess.getAttribute(tpl_type);
    }

    if(temp != null)
        tpl_id = temp.longValue();
    else
        tpl_id = aeTemplate.getFirstTplId(con, tpl_type, owner_ent_id, "asc");

    return tpl_id;
  }
    // retired
  private static long getTplId(Connection con, HttpSession sess, String itemType,
                        String templateType, boolean runInd, long ownerEntId)
                        throws SQLException, qdbException{
    return getTplId(con, sess, itemType, templateType, runInd, false, ownerEntId);
    }
  //get the tpl_id of (itm_type + tpl_type from session)
  //if not found, get the 1st tpl in database and put the tpl_id into session

  private static long getTplId(Connection con, HttpSession sess, String itemType,
                        String templateType, boolean runInd, boolean sessionInd, long ownerEntId)
                        throws SQLException, qdbException{
    Long temp = null;
    long tpl_id;

//System.out.println("sess name == " + itemType + templateType + runInd + sessionInd);
    if (sess != null) {
        temp = (Long)sess.getAttribute(itemType + templateType + runInd + sessionInd);
    }

    if(temp != null){

        tpl_id = temp.longValue();
    }
    else {
        ViewItemTemplate viItmTpl = new ViewItemTemplate();
        viItmTpl.ownerEntId = ownerEntId;
        viItmTpl.itemType = itemType;
        viItmTpl.templateType = templateType;
        viItmTpl.runInd = runInd;
        viItmTpl.sessionInd = sessionInd;
        tpl_id = viItmTpl.getFirstTplId(con);
    }
    if(sess != null && tpl_id != 0) {
        sess.setAttribute(itemType + templateType + runInd + sessionInd, new Long(tpl_id));
    }
    return tpl_id;
  }
    /** retired
    */
    public static long[] getTplIds(Connection con, HttpSession sess, String itemType,
                        String[] templateTypes, long ownerEntId)
                        throws SQLException, qdbException, cwException{
        return getTplIds(con, sess, itemType, templateTypes, false, ownerEntId);
    }
    /**
    Get template id for the given itemType + templatTypes
    if it is a run, inherit the templates from it's parent
    if it is not a run, just get the template from datatbase
    */

    public static long[] getTplIds(Connection con, HttpSession sess, String itemType,
                        String[] templateTypes, boolean session_ind, long ownerEntId)
                        throws SQLException, qdbException, cwException{

        long[] tpl_ids = new long[templateTypes.length];
        //for each template type, check if the tpl_id is in session.
        //if not found, get the 1st tpl and put it into session
        for(int i=0; i<templateTypes.length; i++) {
            tpl_ids[i] = getTplId(con, sess, itemType,
                            templateTypes[i], false, session_ind,
                            ownerEntId);

            if(tpl_ids[i] == 0) {
                throw new cwException("Cannot found template for " + templateTypes[i]);
            }
        }
        return tpl_ids;
    }

    public static long[] getRunTplIds(Connection con, HttpSession sess, String itemType,
                        String[] templateTypes, long ownerEntId, long pItemId)
                        throws SQLException, qdbException, cwException {

        long[] tpl_ids = new long[templateTypes.length];
        //get the template from session or database
        //if not found, get from it's parent
        for(int i=0; i<templateTypes.length; i++) {
            tpl_ids[i] = getTplId(con, sess, itemType,
                            templateTypes[i], true, false,
                            ownerEntId);
            if(tpl_ids[i] == 0 ) {
                aeItem pItm = new aeItem();
                pItm.itm_id = pItemId;
                tpl_ids[i] = pItm.getTemplateId(con, templateTypes[i]);
            }
            if(tpl_ids[i] == 0) {
                throw new cwException("Cannot found template for " + templateTypes[i]);
            }
        }
        return tpl_ids;
    }

  private void delUploadedFiles(long itmId) throws cwException {
    String saveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + itmId;
    dbUtils.delDir(saveDirPath);
  }
  
	private void procUploadedFiles(long itmId, String tmpSaveDirPath, String icon_name) throws cwException, IOException {
		procUploadedFiles(itmId, tmpSaveDirPath, icon_name, wizbini.getItmImageWidth(), wizbini.getItmImageHeight(), true);
	}

	private void procIesIconUploadedFiles(long itmId, String tmpSaveDirPath, String icon_name) throws cwException, IOException {
		procUploadedFiles(itmId, tmpSaveDirPath, icon_name, 200, 150, false);
	}

	private void procUploadedFiles(long itmId, String tmpSaveDirPath, String icon_name, int width, int height, boolean resize) throws cwException, IOException {
    String saveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + itmId;
    String itm_icon = saveDirPath + dbUtils.SLASH + icon_name;
    try {
        dbUtils.moveDir(tmpSaveDirPath, saveDirPath);
			if (resize) {
        cwUtils.resizeImage(itm_icon, wizbini.getItmImageWidth(), wizbini.getItmImageHeight());
			}
    } catch(qdbException e) {
        throw new cwException(e.getMessage());
    }
  }

  private void delOldFiles(long itmId, Vector vFileName) {
	  String saveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + itmId;
	  if(vFileName != null && vFileName.size() >0) {
  		for(int i=0;i<vFileName.size();i++) {
  			String fileName = (String)vFileName.elementAt(i);
  			File file = new File(saveDirPath + dbUtils.SLASH + fileName);
  			file.delete();
  		}
  	}
  }

	private void uploadedFiles(long itmId) throws cwException {
	  String tmpSaveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + 0;  
	  String saveDirPath = static_env.INI_ITM_DIR_UPLOAD + dbUtils.SLASH + itmId;
    try {
        dbUtils.moveDir(tmpSaveDirPath, saveDirPath);
    } catch(qdbException e) {
        throw new cwException(e.getMessage());
    }
  }

	private void msgBox(String title, Connection con, cwSysMessage e, loginProfile prof, String url, PrintWriter out) throws IOException, cwException, SQLException {
	String xsl= static_env.INI_XSL_MSGBOX;
	genMsgBox(title,con,e, prof, url, out,xsl, null);

  }

  private void genMsgBox(String title, Connection con, cwSysMessage e, loginProfile prof, String url, PrintWriter out,String xsl, String extMsg)
    throws IOException, cwException,SQLException
  {
		String encoding;
	  if(prof == null)
		  encoding = static_env.ENCODING;
	  else
		  encoding = prof.label_lan;
	  String msg = e.getSystemMessage(encoding);

		if (url != null) {
			url = cwUtils.esc4Html(url);
		}

		boolean script = false;
		if (url != null && url.trim().toLowerCase().startsWith("javascript:")) {
			url = url.trim().substring("javascript:".length());
			script = true;
		}

        String xml = "";

        xml = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF + "\" standalone=\"no\" ?>" + dbUtils.NEWL;
        xml += "<message>" + dbUtils.NEWL;
        if(prof!=null)
            xml += prof.asXML() + dbUtils.NEWL ;
        xml += "<title>" + title + "</title>" + dbUtils.NEWL;
        if(extMsg != null && !extMsg.equals("")) {
        	xml += extMsg;
        }
        xml += "<body>" + dbUtils.NEWL;
        xml += "<text>" + dbUtils.esc4XML(msg) + "</text>" + dbUtils.NEWL;
		if (script) {
        xml += "<button url=\"" +  dbUtils.esc4XML(url)+ "\"";
			xml += " script=\"" + script + "\"";
		} else {
			xml += "<button url=\"" + dbUtils.esc4XML(url) + "\"";
		}
        xml += ">OK</button>" + dbUtils.NEWL;
        xml += "</body>" + dbUtils.NEWL;
        xml += "</message>" + dbUtils.NEWL;
		static_env.procXSLFile(xml, xsl, out, null);

        //File fXsl = new File(static_env.INI_XSL_MSGBOX);
        //String xslFile = fXsl.getAbsolutePath();
        //cwXSL.procAbsoluteXSLFile(xml, xslFile, out, static_env.DEBUG, static_env.COMP_XSL);
        
  }
    
  

	private String getItemXML(Connection con, aeReqParam urlp, loginProfile prof)
		throws SQLException, cwSysMessage, qdbException, cwException, IOException {
		boolean checkStatus;
		boolean cos_mgt_ind;
		long[] usr_ent_ids = {0};
		AcCourse accos = new AcCourse(con);
		AcItem acitm = new AcItem(con);
		if (urlp.get_last == 1) {
			urlp.itm.itm_id = aeItem.getLastVersion(con, urlp.itm.itm_id);
		}
		aeItem itm = urlp.itm;
		itm.get(con, urlp.tnd_id);
		//itm.setQRInd(con, prof);

		if(prof == null) {
			checkStatus = true;
			cos_mgt_ind = false;
		}
		else {
			//if( cwUtils.checkUserRoleSkin(request, response, "/" + prof.skin_root + "/" + prof.current_role_skin_root, prof.role_url_home) )
			//    return;

			usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
			checkStatus = (!acitm.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));
            cos_mgt_ind = acitm.hasMaintainPrivilege(itm, prof.usr_ent_id, prof.current_role, prof.root_ent_id);
			//cos_mgt_ind = accos.checkModifyPermission(prof, dbCourse.getCosResId(con, urlp.itm.itm_id));
		}
		if(prof != null) {
			if(urlp.tnd_id != 0) {
				//access control on treenode
				AcTreeNode actnd = new AcTreeNode(con);
				aeTreeNode tnd = new aeTreeNode();
				tnd.tnd_id = urlp.tnd_id;
				tnd.tnd_cat_id = tnd.getCatalogId(con);
				if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role,
											tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
					throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
				}
			}
			//access control on item
			if(!acitm.hasReadPrivilege(itm, prof.usr_ent_id, prof.current_role, checkStatus)){
				throw new cwSysMessage("AEIT27");
			}
		}

        StringBuffer resultBuf = new StringBuffer(2500);
        String metaXML = null;
        long usr_ent_id;
        if (urlp.usr_ent_id!=0) {
            usr_ent_id = urlp.usr_ent_id ;
        }
        else {
            usr_ent_id = (prof == null) ? 0 : prof.usr_ent_id;
        }
        itm.app_rol_ext_id = (prof == null) ? null : prof.current_role;
        if( urlp.ji_view ) {
            resultBuf.append(urlp.itm.ItemDetailByRunAsXML(con,static_env, false, false, urlp.tvw_id, 0, false,false, urlp.show_session_ind));
        } else {
            resultBuf.append(itm.ItemDetailAsXML(con,static_env,checkStatus, urlp.prev_version_ind, urlp.tvw_id, urlp.show_run_ind, urlp.show_session_ind, 0, usr_ent_id,cos_mgt_ind,false,urlp.show_respon_run_ind,prof.current_role, prof, false));
        }
      //  if(cos_mgt_ind && !itm.itm_run_ind && !itm.itm_create_run_ind && itm.itm_apply_ind){
            Certificate cert =  new Certificate();
            resultBuf.append(cert.getAllCertXml(con,itm.itm_cfc_id));
      //  }
        return resultBuf.toString();

    }

    private String getFormInsItmXML(Connection con, HttpSession sess, aeReqParam urlp, loginProfile prof)
		throws SQLException, cwSysMessage, qdbException, cwException, IOException {
		  long tnd_id = urlp.tnd_parent_tnd_id;
		  long tpl_id=0;

		  //access control
		  AcItem acitm = new AcItem(con);
		  if(!acitm.hasInsPrivilege(prof.usr_ent_id, prof.current_role, tnd_id, prof.root_ent_id)) {
			  throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
		  }

		  //if urlp.itm.itm_id is given, we are going to insert a run/session
		  //save this itm_id to http-session
		  if(urlp.itm.itm_id != 0 || urlp.tpn_itm_run_ind) {
			  sess.setAttribute(SESS_PARENT_ITM_ID, new Long(urlp.itm.itm_id));
			  if(urlp.tpn_itm_run_ind){
				  urlp.itm.itm_run_ind = urlp.tpn_itm_run_ind;
			  }else{				  
				  urlp.itm.itm_run_ind = !urlp.itm.itm_session_ind;
			  }
//							urlp.itm.itm_run_ind = true;
		  }
		  else {
			  sess.removeAttribute(SESS_PARENT_ITM_ID);
			  urlp.itm.itm_run_ind = false;
			  urlp.itm.itm_session_ind = false;
			  //If work flow template id is passed, save it into session
			  if(urlp.wrk_tpl_id != 0) {
				  sess.setAttribute(urlp.ity_id + "WORKFLOW" + urlp.itm.itm_run_ind + urlp.itm.itm_session_ind, new Long(urlp.wrk_tpl_id));
			  }
			  //Save the approval type into session
			  if(urlp.itm.itm_app_approval_type != null) {
				  sess.setAttribute(SESS_ITM_APP_APPROVAL_TYPE, urlp.itm.itm_app_approval_type);
			  } else {
				  sess.removeAttribute(SESS_ITM_APP_APPROVAL_TYPE);
			  }
//System.out.println("Sess name = " + urlp.ity_id + "WORKFLOW" + urlp.itm.itm_run_ind + urlp.itm.itm_session_ind);
		  }

		  //get template types
		  ViewItemTemplate viItmTpl = new ViewItemTemplate();
		  viItmTpl.ownerEntId = prof.root_ent_id;
		  viItmTpl.itemType = urlp.ity_id;
		  viItmTpl.runInd = urlp.itm.itm_run_ind;
		  viItmTpl.sessionInd = urlp.itm.itm_session_ind;
		  String[] templateTypes = viItmTpl.getItemTypeTemplates(con);

		  if(!urlp.itm.itm_run_ind) {
			  getTplIds(con, sess, urlp.ity_id, templateTypes, urlp.itm.itm_session_ind, prof.root_ent_id);
		  }
		  else {
			  if(!urlp.tpn_itm_run_ind){
				  getRunTplIds(con, sess, urlp.ity_id, templateTypes, prof.root_ent_id, urlp.itm.itm_id);
			  }
		  }

		  //get the itm_tpl_id
		  tpl_id = getTplId(con, sess, urlp.ity_id, aeTemplate.ITEM,
							  urlp.itm.itm_run_ind, urlp.itm.itm_session_ind, prof.root_ent_id);
		  if(tpl_id == 0) {
			  throw new cwException("No Item Template Can Be Found");
		  }

		  //generate valued template
		  StringBuffer resultBuf = new StringBuffer(2500);
		  urlp.itm.itm_type = urlp.ity_id;		 
		  resultBuf.append(urlp.itm.prep_ins_form(con, tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id, tpl_id, urlp.tvw_id, static_env, urlp.wrk_tpl_id, prof.my_top_tc_id, urlp.itm_tcr_id));
		  resultBuf.append(aeItem.genItemActionNavXML(con, urlp.itm_id, prof, urlp.itm.itm_type, urlp));
		  return resultBuf.toString();
	}
    
    private String getSimpleInsItmXML(Connection con, HttpSession sess, aeReqParam urlp, loginProfile prof)
    		throws SQLException, cwSysMessage, qdbException, cwException, IOException {
    		  long tnd_id = urlp.tnd_parent_tnd_id;
    		  long tpl_id=0;

    		  //access control（权限控制）
    		  AcItem acitm = new AcItem(con);
    		  if(!acitm.hasInsPrivilege(prof.usr_ent_id, prof.current_role, tnd_id, prof.root_ent_id)) {
    			  throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
    		  }

    		  //if urlp.itm.itm_id is given, we are going to insert a run/session
    		  //save this itm_id to http-session（如果课程ID存在，）
    		  if(urlp.itm.itm_id != 0 || urlp.tpn_itm_run_ind) {
    			  sess.setAttribute(SESS_PARENT_ITM_ID, new Long(urlp.itm.itm_id));
    			  urlp.itm.itm_run_ind = true;
    		  }
    		  else {
    			  sess.removeAttribute(SESS_PARENT_ITM_ID);
    			  urlp.itm.itm_run_ind = false;
    			  urlp.itm.itm_session_ind = false;
    			  //If work flow template id is passed, save it into session
    			  if(urlp.wrk_tpl_id != 0) {
    				  sess.setAttribute(urlp.ity_id + "WORKFLOW" + urlp.itm.itm_run_ind + urlp.itm.itm_session_ind, new Long(urlp.wrk_tpl_id));
    			  }
    			  //Save the approval type into session
    			  if(urlp.itm.itm_app_approval_type != null) {
    				  sess.setAttribute(SESS_ITM_APP_APPROVAL_TYPE, urlp.itm.itm_app_approval_type);
    			  } else {
    				  sess.removeAttribute(SESS_ITM_APP_APPROVAL_TYPE);
    			  }
    		  }

    		  //generate valued template
    		  StringBuffer resultBuf = new StringBuffer(2500);
    		  urlp.itm.itm_type = urlp.ity_id;		 
    		  resultBuf.append(urlp.itm.getDefaultTrainingCenterXML(con, prof));
    		  resultBuf.append(urlp.itm.prep_ins_simple_form(con, tnd_id, prof.usr_ent_id, prof.current_role, prof.root_ent_id, tpl_id, urlp.tvw_id, static_env, urlp.wrk_tpl_id));
    		  resultBuf.append(aeItem.genItemActionNavXML(con, urlp.itm_id, prof, urlp.itm.itm_type, urlp));
    		  return resultBuf.toString();
    	}

    public void setItemLogTitle(aeItem itm ,ObjectActionLog log,Connection con ){
		aeItem parentCourse = aeItem.getCourseByClassId(con, itm.itm_id);
		String objectTitle = null;
		String objectType = null;
		if(itm.itm_dummy_type.equalsIgnoreCase("SELFSTUDY|-|COS")){//网上课程
			objectTitle = itm.itm_title;
			objectType = ObjectActionLog.OBJECT_TYPE_COS;
		}else if(itm.itm_dummy_type.equalsIgnoreCase("SELFSTUDY|-|EXAM")){ //在线考试
			objectTitle = itm.itm_title;
			objectType = ObjectActionLog.OBJECT_TYPE_ONLINE_EXAM;
		}else if(itm.itm_dummy_type.equalsIgnoreCase("CLASSROOM|-|COS")){//离线课程
			objectTitle = itm.itm_title;
			if(null!=parentCourse){
					objectTitle = parentCourse.itm_title + " > " +objectTitle;
					objectType = ObjectActionLog.OBJECT_TYPE_CLASS;
			}else{
				objectType = ObjectActionLog.OBJECT_TYPE_COS;
			}
		}else if(itm.itm_dummy_type.equalsIgnoreCase("CLASSROOM|-|EXAM")){ //离线考试
			objectTitle = itm.itm_title;
			objectType = ObjectActionLog.OBJECT_TYPE_OFFLINE_EXAM;
			if(null!=parentCourse){
					objectTitle = parentCourse.itm_title + " > " +objectTitle;
			}else{
				objectType = ObjectActionLog.OBJECT_TYPE_ONLINE_EXAM;
			}
		}else if(itm.itm_dummy_type.equalsIgnoreCase("INTEGRATED")){//项目式培训
			objectTitle = itm.itm_title;
			objectType = ObjectActionLog.OBJECT_TYPE_COS;
		}else{
			objectTitle = itm.itm_title;
			objectType = ObjectActionLog.OBJECT_TYPE_COS;
		}
		log.setObjectTitle(objectTitle);
		log.setObjectType(objectType);
    }
}