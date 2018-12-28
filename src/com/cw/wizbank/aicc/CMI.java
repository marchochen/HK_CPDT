package com.cw.wizbank.aicc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.accesscontrol.AcModule;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.course.CourseCriteria;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbModuleEvaluationHistory;
import com.cw.wizbank.qdb.dbObjective;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbResourceObjective;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.scorm.dm.*;
import com.cw.wizbank.scorm.util.CMIRequest;
import com.cw.wizbank.scorm.util.CMIResponse;
import com.cwn.wizbank.utils.CommonLog;

public class CMI extends HttpServlet
{
    public static Hashtable sess_hash = new Hashtable();

    // default output encoding
    public static final String defEncoding_ = "ISO-8859-1";
    public String clientEnc;

    private static final String SEPARATOR = ":_:_:";

    private static final String CORE = "[core]";
    private static final String CORE_LESSON = "[core_lesson]";
    private static final String OBJECTIVES_STATUS = "[objectives_status]";

    private static final char ASCII_10 = 10;
    private static final char ASCII_13 = 13;
    private static final String NEWL = "\n";
    private static final String CR = "<CR>";
    private static final String AICC_CREDIT_HASH = "aicc_credit_hash";
    private static final String AICC_TIME_HASH = "aicc_time_hash";
    private static final String LESSON_MODE_HASH = "lesson_mode_hash";
    private static final String AICC_VENDOR_HASH = "aicc_vendor_hash";

    // using xml in place of wizb.ini for system parameters
    private static WizbiniLoader wizbini = null;
    // logger for all debug messages
    private static Logger debugLogger = null;
    private static Object obj = new Object();
    
    public static final String SCORM_VERSION_2004 = "2004";
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
        	CommonLog.info("CMI.init() START...");
            wizbini = WizbiniLoader.getInstance(config);

            // initialize debug logger
            /*
            String logdir = wizbini.getAppnRoot() + dbUtils.SLASH + wizbini.cfgSysSetupadv.getLogDir().getName() + dbUtils.SLASH;
            File dir = new File(logdir);
            if (!dir.exists()) {
                dir.mkdir();
            }
           	System.setProperty("log_path", logdir);
            */
            System.setProperty("file_encoding", wizbini.cfgSysSetupadv.getEncoding());
            debugLogger = Logger.getLogger(this.getClass().getName() + ".log");
            PropertyConfigurator.configure(wizbini.getCfgFileLog4jDir());

            CommonLog.info("CMI.init() END");
        } catch (cwException e) {
            CommonLog.error("init() exception :" + e.getMessage(),e);
            throw new ServletException(e.getMessage());
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	boolean isScorm2004 = false;
    	String tmp = request.getParameter("scover");
    	//Scorm API request object, only used for scorm 2004
        CMIRequest scoApiReq = null;
        CMIResponse scoApiResp = null;
    	if (SCORM_VERSION_2004.equals(tmp)) {
    		isScorm2004 = true;
    		debugLogger.debug("-------------scorm 2004 request---------------");
    		debugLogger.debug("-------------begin to read request object---------------");
			scoApiReq = readObject(request);
			debugLogger.debug("-------------end to read request object---------------");
    	}
        // save the log for command=putlog
        String paramValue = request.getParameter("command");
        if (paramValue != null && paramValue.equals("putlog")) {
            String thisEnc = request.getCharacterEncoding();
            if (thisEnc == null || thisEnc.length() == 0) {
                thisEnc = defEncoding_;
            }
            String logValue = dbUtils.unicodeFrom(request.getParameter("log"), thisEnc, wizbini.cfgSysSetupadv.getEncoding());
            debugLogger.debug(logValue);
            return;
        }

debugLogger.debug("<><> AICC v2.0 !!!");
        HttpSession sess = request.getSession(false);
        clientEnc = request.getCharacterEncoding();

        // common http headers
        response.setHeader("Content-Language", "zh");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");

if (sess == null) {
    debugLogger.debug(">Session is null");
} else {
    if (sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE) == null) {
        debugLogger.debug(">Profile is null");
    }
}

        loginProfile prof = null;

        if (sess != null) {
            prof = (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
        }

        if (clientEnc == null) {
            clientEnc = dbUtils.ENC_ENG;
        }
        PrintWriter out = null;
        if (!isScorm2004) {
        	response.setContentType("text/plain; charset=" + wizbini.cfgSysSetupadv.getEncoding());
        	out = response.getWriter();
        }
        Connection con = null;

debugLogger.debug("======================");
Enumeration enumeration = request.getParameterNames();
String cmd_key = "command";

while (enumeration.hasMoreElements()) {
    String name = (String)enumeration.nextElement();
    debugLogger.debug(name + "=" + dbUtils.subsitute((String)request.getParameter(name), "\r\n", "{{br}}"));

    if (name.equalsIgnoreCase("command")) {
        cmd_key = name;
    }
}
debugLogger.debug("======================");

        String conType = request.getContentType();

        if ((conType != null && conType.toLowerCase().trim().startsWith("application/x-www-form-urlencoded")) || isScorm2004) {
            String sessIDCheck = isScorm2004 ? scoApiReq.aicc_sid : request.getParameter("session_id");
            if (sessIDCheck != null && (sessIDCheck.equalsIgnoreCase("VIEWONLY")||  sessIDCheck.trim().toUpperCase().startsWith("VIEWONLY"))) {
            	if (!isScorm2004) {
	                println(out, "error=0", null);
	                println(out, "aicc_data=[core]", null);
	                println(out, "student_id=", null);
	                println(out, "student_name=", null);
	                println(out, "lesson_location=", null);
	                println(out, "credit=", null);
	                println(out, "lesson_status=N", null);
	                println(out, "time=00:00:00", null);
	                println(out, "score=", null);
	                println(out, "lesson_mode=b", null);
	                println(out, "[core_lesson]", null);
	                println(out, "[core_vendor]", null);
            	} else {
            		CMIResponse  resp = new CMIResponse();
            		resp.mActivityData = new SCODataManager();
            		resp.mActivityData.addDM(DMFactory.DM_SCORM_2004);
//            		setValue2ScoDM("cmi.completion_status", "unknown", true, resp.mActivityData);
            		setValue2ScoDM("cmi.mode", "browse", resp.mActivityData);
            		ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
            		oos.writeObject(resp);
            		oos.close();
            	}
                return;
            }
            try {
                int error = 0;
                // get the database connection for this request
                try {
                    cwSQL sqlCon = new cwSQL();
                    sqlCon.setParam(wizbini);
                    con = sqlCon.openDB(false);
                } catch (Exception e) {
                    out =  response.getWriter();
                    out.println("<b><h3> Sorry, the server is too busy.</h3></b>");
                    return;
                }
                // try to convert null login profile with ent_id in the session_id parameter (2005-12-23 kawai)
                // errorCase 0: no error
                // errorCase 1: no session_id in request
                // errorCase 2: no ent_id in session_id
                // errorCase 3: NumberFormatException in ent_id
                // errorCase 4: no valid user for ent_id
                if (prof == null || prof.usr_id == null) {
                    long sessEntId = 0;
                    int errorCase = 1;
//                    Enumeration reqKey = request.getParameterNames();
//                    while (reqKey.hasMoreElements()) {
//                        String key = (String)reqKey.nextElement();
//                        if (key.equalsIgnoreCase("session_id")) {
                    // cause have got session_id before, no need to loop the request again, use sessIDCheck instead.
                    	  if (sessIDCheck != null && sessIDCheck.length() > 0) {
                            errorCase = 0;
                            String[] lst = dbUtils.split(sessIDCheck, SEPARATOR);
//                            String[] lst = dbUtils.split(request.getParameter(key), SEPARATOR);
                            if (lst != null && lst.length > 0 && lst[0] != null) {
                                try {
                                    sessEntId = Long.parseLong(lst[0]);
                                    dbRegUser userObj = new dbRegUser();
                                    userObj.usr_ent_id = sessEntId;
                                    userObj.getByEntId(con);
                                    if (userObj.usr_id != null) {
                                        if (prof == null) {
                                            prof = new loginProfile();
                                        }
                                        prof.usr_id         = userObj.usr_id;
                                        prof.usr_ent_id     = userObj.usr_ent_id;
                                        prof.current_role   = "NLRN_" + String.valueOf(userObj.usr_ste_ent_id);
                                        prof.root_ent_id    = userObj.usr_ste_ent_id;
                                    }
                                    else {
                                        errorCase = 4;
                                    }
                                } catch (NumberFormatException e) {
                                    errorCase = 3;
                                }
                            }
                            else {
                                errorCase = 2;
                            }
//                            break;
                        }
//                    }
                    debugLogger.debug("login profile converted with ent_id=" + sessEntId + "(errorCase=" + errorCase + ")");
                }
                Hashtable aiccData = new Hashtable();
debugLogger.debug("+++1");
                if ((error = unmarshalAiccData(con, request, aiccData, prof, isScorm2004, scoApiReq)) == 0) {
debugLogger.debug(" +++2");
                    error = checkAuPwd(con, ((Long)aiccData.get("mod_id")).longValue(), (String)aiccData.get("au_password"));
debugLogger.debug(" +++3");
                    Hashtable h_credit = getSessCredit(sess, ((Long)aiccData.get("ent_id")));
debugLogger.debug(" <><>"  + h_credit + "<><> ");
                    if (h_credit != null && h_credit.get((Long)aiccData.get("mod_id")) != null && aiccData != null) {
                        aiccData.put("credit", h_credit.get((Long)aiccData.get("mod_id")));
                    }
                }
debugLogger.debug(" +++4");

debugLogger.debug("***********");
Enumeration x = aiccData.keys();
while (x.hasMoreElements()) {
    String a = (String)x.nextElement();

    if (! a.equals("core") && ! a.equals("objectives_status")) {
        debugLogger.debug("[][]" + a + " = " + aiccData.get(a));
    }

    if (a.equals("core")) {
        debugLogger.debug("[][]" + a + " = ");
        Hashtable b = (Hashtable)aiccData.get(a);
        Enumeration c = b.keys();
        while (c.hasMoreElements()) {
            String d = (String)c.nextElement();
            debugLogger.debug(d + " = " + b.get(d));
        }
    }

    if (a.equals("objectives_status")) {
        debugLogger.debug("[][]" + a + " = ");
        Hashtable b = (Hashtable)aiccData.get(a);
        Enumeration c = b.keys();
        while (c.hasMoreElements()) {
            String d = (String)c.nextElement();
            debugLogger.debug(d + " = ");
            Hashtable e = (Hashtable)b.get(d);
            Enumeration f = e.keys();
            while (f.hasMoreElements()) {
                String g = (String)f.nextElement();
                debugLogger.debug(g + " = " + e.get(g));
            }
        }
    }
}
debugLogger.debug("***********");

                if (aiccData.get("command").equals("getparam") || isGetOrInitReq(isScorm2004, scoApiReq)) {
                    String last_mov_status = marshalAiccData(con, sess, prof, out, "getparam", error, (Long)aiccData.get("ent_id"), (Long)aiccData.get("cos_id"), (Long)aiccData.get("mod_id"), (String)aiccData.get("mod_vendor"), (Long) aiccData.get("tkh_id"), isScorm2004, scoApiReq);

                    if (error == 0 && incrementTotalAttempt(con, prof, ((Long)aiccData.get("ent_id")).longValue(), ((Long)aiccData.get("cos_id")).longValue(), ((Long)aiccData.get("mod_id")).longValue(), ((Long)aiccData.get("tkh_id")).longValue(), last_mov_status) == 1) {
                        con.commit();
                    }
                    if (isScorm2004) 
                    	SendScorm2004Response(response, scoApiReq, error);
                } else if (aiccData.get("command").equals("putparam") || (isScorm2004 && scoApiReq.mRequestType == CMIRequest.TYPE_SET)) {
                    if (error == 0) {
                        AcModule acModule = new AcModule(con);
                        if (prof == null || (Long)aiccData.get("tkh_id") > 0){
debugLogger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ normal mode");
                           // synchronized(obj){
                            //逻辑变更只在学习中的状态才记录学习时长
                            dbCourseEvaluation evaluation = new dbCourseEvaluation();
                            evaluation.cov_cos_id = Long.parseLong(aiccData.get("cos_id").toString());
                            evaluation.cov_ent_id = Long.parseLong(aiccData.get("ent_id").toString());
                            evaluation.cov_tkh_id = Long.parseLong(aiccData.get("tkh_id").toString());
                            evaluation.get(con);
                            if("I".equals(evaluation.cov_status)){
                                putAiccData2DB(con, sess, prof, aiccData);
                                con.commit();
                            }
                            //}
                        }
                        else {
debugLogger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ browse mode");
                            //do nothing
                            // not in learner role, therefore should be in browse mode
                        }
                    }

                    marshalAiccData(con, sess, prof, out, "putparam", error, (Long)aiccData.get("ent_id"), (Long)aiccData.get("cos_id"), (Long)aiccData.get("mod_id"), (String)aiccData.get("mod_vendor"), (Long)aiccData.get("tkh_id"), isScorm2004, scoApiReq);
					if (isScorm2004)
						SendScorm2004Response(response, scoApiReq, error);
                } else if (aiccData.get("command").equals("exitau")) {
                    clearSessTime(sess, (Long)aiccData.get("mod_id"), (String)aiccData.get("sess_id_time"), (Long)aiccData.get("ent_id"));
                    clearSessCredit(sess, (Long)aiccData.get("mod_id"), (Long)aiccData.get("ent_id"));
debugLogger.debug("exitau: clearSessTime ok");
                    marshalAiccData(con, sess, prof, out, "exitau", error, (Long)aiccData.get("ent_id"), (Long)aiccData.get("cos_id"), (Long)aiccData.get("mod_id"), (String)aiccData.get("mod_vendor"), (Long)aiccData.get("tkh_id"), isScorm2004, scoApiReq);
debugLogger.debug("exitau: marshal ok");
                    if (error == 0 && doLogout(con, ((Long)aiccData.get("ent_id")).longValue(), ((Long)aiccData.get("cos_id")).longValue(), ((Long)aiccData.get("mod_id")).longValue(),  ((Long)aiccData.get("tkh_id")).longValue()) && sess != null) {
                        if (sess != null) {
                            sess.invalidate();
                        }
debugLogger.debug("exitau: invalidate ok");
                        return;
                    }
                } else {
                    debugLogger.debug(" ### INVALID COMMAND!!");
                    marshalAiccData(con, sess, prof, out, null, 1, (Long)aiccData.get("ent_id"), (Long)aiccData.get("cos_id"), (Long)aiccData.get("mod_id"), (String)aiccData.get("mod_vendor"), (Long)aiccData.get("tkh_id"), isScorm2004, scoApiReq);
                }
            } catch (SQLException e) {
            	//e.printStackTrace();
            	CommonLog.error("SQL Error: " +e.getMessage(),e);
               // println(out, "SQL Error: " + e.getMessage(), null);
            } catch (qdbException e) {
            	CommonLog.error("Error: " +e.getMessage(),e);
                //println(out, "Error: " + e.getMessage(), null);
            } catch (qdbErrMessage e) {
            	CommonLog.error("Error: " +e.getMessage(),e);
                //println(out, "Error: " + e.getMessage(), null);
            } catch (cwException e) {
            	CommonLog.error("Error: " +e.getMessage(),e);
                //println(out, "Error: " + e.getMessage(), null);
            } catch (cwSysMessage e) {  // added by emily
                //e.printStackTrace();
                try {
                    con.rollback();
                    CommonLog.error("Error: " +e.getMessage(),e);
                    println(out, "Error: " + e.getMessage(), null);
                    //msgBox(qdbAction.MSG_STATUS, con, e, prof, urlp.url_failure, out);
                //} catch (cwException ce) {
                //   out.println("MSGBOX Server error: " + e.getMessage());
                } catch (SQLException se) {
                   out.println("MSGBOX SQL error: " + e.getMessage());
                   CommonLog.error("Error: " +se.getMessage(),se);
                }
            }

            finally {
                try {
                    con.commit();
                    if(con != null && !con.isClosed())
                        con.close();
                    if (out != null)
                    	out.close();
                }
                catch (SQLException sqle) {
                    out.println("Close connection error: " + sqle.getMessage());
                }
            }
        } else {
            out.println("<HR>");
            out.println("<A HREF=\"http://aicc.org/pages/sample.html?aicc_sid=" + 411 + SEPARATOR + 17664 + SEPARATOR + 17950 + SEPARATOR + "MOD_VENDOR" + SEPARATOR + "200101011234" + SEPARATOR + "&aicc_url=http%3a%2f%2f202.134.90.204:7001/servlet/cw.aicc.CMI\">Click here to CBT (202.134.90.204)</A><BR>");
            out.println("<A HREF=\"http://aicc.org/pages/sample.html?aicc_sid=" + 411 + SEPARATOR + 17664 + SEPARATOR + 17950 + SEPARATOR + "MOD_VENDOR" + SEPARATOR + "200101011234" + SEPARATOR + "&aicc_url=http%3a%2f%2fcw01:150/servlet/cw.aicc.CMI\">Click here to CBT (cw01)</A>");
            out.close();
            return;
        }

        return;
    }

    private String marshalAiccData(Connection con, HttpSession sess, loginProfile prof, PrintWriter out, String command, int error, Long l_ent_id, Long l_cos_id, Long l_mod_id, String sess_mod_vendor, Long l_tkh_id, boolean isScorm2004, CMIRequest scoApiReq)
        throws SQLException, cwException
    {
    	String last_mov_status = null;
debugLogger.debug("vvvvvvvvvv MARSHAL AICC DATA vvvvvvvvvv");
        Hashtable error_text = new Hashtable();
        error_text.put(new Long(0), "Successful");
        error_text.put(new Long(1), "Invalid Command");
        error_text.put(new Long(2), "Invalid AU-password");
        error_text.put(new Long(3), "Invalid Session ID");

        PreparedStatement stmt = null;
        ResultSet rs = null;
        long ent_id = 0;
        long cos_id = 0;
        long mod_id = 0;
        long tkh_id = -1;

        if (l_ent_id != null) {
            ent_id = l_ent_id.longValue();
        }

        if (l_cos_id != null) {
            cos_id = l_cos_id.longValue();
        }

        if (l_mod_id != null) {
            mod_id = l_mod_id.longValue();
        }

        if (l_tkh_id != null) {
            tkh_id = l_tkh_id.longValue();
        }else {
            tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, cos_id, prof.usr_ent_id);
        }

        if (!isScorm2004) {
        	println(out, "error=" + error, sess_mod_vendor);
        	println(out, "error_text=" + error_text.get(new Long(error)), sess_mod_vendor);
        }



        if (!isScorm2004) {
	        if (con == null) {
	            println(out, "version=", sess_mod_vendor);
	        } else {
	            stmt = con.prepareStatement("SELECT mod_aicc_version FROM Module "+ cwSQL.noLockTable() 
                    + " WHERE mod_res_id = ?");
	            stmt.setLong(1, mod_id);
	            rs = stmt.executeQuery();
	
	            if (rs.next()) {
	                if (rs.getString("mod_aicc_version") != null) {
	                    println(out, "version=" + rs.getString("mod_aicc_version"), sess_mod_vendor);
	                } else {
	                    println(out, "version=", sess_mod_vendor);
	                }
	            } else {
	                println(out, "version=", sess_mod_vendor);
	            }
	        }
	
	        print(out, "aicc_data=");

        }

        if (error == 0 && (command.equalsIgnoreCase("getparam") || isGetOrInitReq(isScorm2004, scoApiReq))) {
            StringBuffer sql = new StringBuffer();

//            stmt = con.prepareStatement("select usr_id, usr_last_name_bil, usr_first_name_bil, usr_city_bil, usr_country_bil, usr_display_bil, usr_state_bil, usr_address_bil, usr_tel_1, mod_max_score, mod_pass_score, mod_usr_id_instructor, mod_time_limit_action, mov_ele_loc, mov_total_time, mov_status, mov_status_flag, mov_score, mov_max_score, mov_min_score, mov_aicc_score, mov_total_attempt, res_duration From RegUser, Module, ModuleEvaluation, Resources Where usr_ent_id = mov_ent_id And mov_mod_id = mod_res_id And mov_cos_id = ? And usr_ent_id = ? And mod_res_id = ? AND res_id = mod_res_id union select usr_id, usr_last_name_bil, usr_first_name_bil, usr_city_bil, usr_country_bil, usr_display_bil, usr_state_bil, usr_address_bil, usr_tel_1, mod_max_score, mod_pass_score, mod_usr_id_instructor, mod_time_limit_action, null as mov_ele_loc, null as mov_total_time, null as mov_status, null as mov_status_flag, null as mov_score, null as mov_max_score, null as mov_min_score, null as mov_aicc_score, null as mov_total_attempt, res_duration From RegUser, Module, Resources Where usr_ent_id = ? And mod_res_id = ? And res_id = mod_res_id And NOT Exists(Select mov_cos_id From ModuleEvaluation Where mov_ent_id = usr_ent_id And mov_mod_id = mod_res_id And mov_cos_id = ?)");

            sql.append(" select usr_id, usr_ste_usr_id, usr_last_name_bil, usr_first_name_bil, usr_country_bil, usr_display_bil, usr_state_bil, usr_address_bil, usr_tel_1, mod_max_score, mod_pass_score, mod_usr_id_instructor, mod_time_limit_action, mov_ele_loc, mov_total_time, mov_status, mov_status_flag, mov_score, mov_max_score, mov_min_score, mov_aicc_score, mov_total_attempt, res_duration ");
            sql.append(" From RegUser "+ cwSQL.noLockTable() 
                    + ", Module "+ cwSQL.noLockTable() 
                    + ", ModuleEvaluation "+ cwSQL.noLockTable() 
                    + ", Resources "+ cwSQL.noLockTable() );
            sql.append(" Where usr_ent_id = mov_ent_id And mov_mod_id = mod_res_id And mov_cos_id = ? And usr_ent_id = ? And mod_res_id = ? AND res_id = mod_res_id AND mov_tkh_id = ?");
            sql.append(" union ");
            sql.append(" select usr_id, usr_ste_usr_id, usr_last_name_bil, usr_first_name_bil, usr_country_bil, usr_display_bil, usr_state_bil, usr_address_bil, usr_tel_1, mod_max_score, mod_pass_score, mod_usr_id_instructor, mod_time_limit_action, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)).append(" as mov_ele_loc, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_FLOAT)).append(" as mov_total_time, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)).append(" as mov_status, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)).append(" as mov_status_flag, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_DECIMAL)).append(" as mov_score, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_DECIMAL)).append(" as mov_max_score, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_DECIMAL)).append(" as mov_min_score, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_STRING)).append(" as mov_aicc_score, ").append(cwSQL.get_null_sql(cwSQL.COL_TYPE_INTEGER)).append(" as mov_total_attempt, res_duration ");
            sql.append(" From RegUser "+ cwSQL.noLockTable() 
                    + ", Module "+ cwSQL.noLockTable() 
                    + ", Resources "+ cwSQL.noLockTable() 
                    + " ");
            sql.append(" Where usr_ent_id = ? And mod_res_id = ? And res_id = mod_res_id And NOT Exists(Select mov_cos_id From ModuleEvaluation "+ cwSQL.noLockTable() 
                    + " Where mov_ent_id = usr_ent_id And mov_mod_id = mod_res_id And mov_cos_id = ? and mov_tkh_id = ?)");

            stmt = con.prepareStatement(sql.toString());
            stmt.setLong(1, cos_id);
            stmt.setLong(2, ent_id);
            stmt.setLong(3, mod_id);
            stmt.setLong(4, tkh_id);
            stmt.setLong(5, ent_id);
            stmt.setLong(6, mod_id);
            stmt.setLong(7, cos_id);
            stmt.setLong(8, tkh_id);
            rs = stmt.executeQuery();

            if (rs.next()) {
            	if (!isScorm2004) {
            		println(out, "[core]", sess_mod_vendor);
            		println(out, "student_ID=" + rs.getString("usr_ste_usr_id"), sess_mod_vendor);
            		println(out, "student_name=" + rs.getString("usr_display_bil"), sess_mod_vendor);
                //println(out, "output_file=");
            	} else {
            		setValue2ScoDM("cmi.learner_id", rs.getString("usr_ste_usr_id"), scoApiReq.mActivityData);
            		setValue2ScoDM("cmi.learner_name", rs.getString("usr_display_bil"), scoApiReq.mActivityData);
            	}

            	if (!isScorm2004) {
	                if (rs.getString("mov_ele_loc") != null) {
	                    println(out, "lesson_location=" + rs.getString("mov_ele_loc"), sess_mod_vendor);
	                } else {
	                    println(out, "lesson_location=", sess_mod_vendor);
	                }
                } else {
                	setValue2ScoDM("cmi.location", rs.getString("mov_ele_loc") != null ? rs.getString("mov_ele_loc") : "", scoApiReq.mActivityData);
                }

                String credit = checkCredit(con, ent_id, cos_id, mod_id, tkh_id);
                Hashtable h_credit = null;
debugLogger.debug("         <><><><><> " + credit);
                if ((h_credit = getSessCredit(sess, l_ent_id)) == null) {
                    h_credit = new Hashtable();
                }

                h_credit.put(new Long(mod_id), credit);
                putSessCredit(sess, h_credit, l_ent_id);
                if (!isScorm2004) {
                	println(out, "credit=" + credit, sess_mod_vendor);
                } else {
                	setValue2ScoDM("cmi.credit", (credit.equalsIgnoreCase("N") ? "no credit" : "credit"), scoApiReq.mActivityData);
                }

                if (!isScorm2004) {
	                AcModule acModule = new AcModule(con);
	                if (prof != null && tkh_id <= 0){
	debugLogger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ browse mode");
	                    // not in learner role
	                    println(out, "lesson_mode=Browse", sess_mod_vendor);
	                }
	                else {
	debugLogger.debug("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ normal mode");
	                    // do nothing
	                }
                }
                if (isScorm2004) {
                	setValue2ScoDM("cmi.mode", "normal", scoApiReq.mActivityData);
                }

                if (!isScorm2004) {
	                if (rs.getString("mov_status") != null) {
	                	last_mov_status = rs.getString("mov_status");
	                    print(out, "lesson_status=" + rs.getString("mov_status"));
	
	                    String flag = rs.getString("mov_status_flag");
	
	                    if (flag != null && flag.equalsIgnoreCase("S")) {
	                        println(out, ",R", sess_mod_vendor);
	                    } else {
	                        println(out, "", sess_mod_vendor);
	                    }
	                } else {
	                    println(out, "lesson_status=N,A", sess_mod_vendor);
	                }
                } else {
	                	last_mov_status = rs.getString("mov_status");
	                    setValue2ScoDM("cmi.completion_status", getFullStatus(last_mov_status), scoApiReq.mActivityData);
	                    if (last_mov_status == null || last_mov_status.length() == 0) {
	                    	setValue2ScoDM("cmi.entry", "ab-initio", scoApiReq.mActivityData);
	                    } else {
	                    	setValue2ScoDM("cmi.entry", "", scoApiReq.mActivityData);
	                    }
                }

                //out.println("path=");

                long total_time = (new Float(rs.getFloat("mov_total_time"))).longValue();
                String hr = (new Long(total_time/3600)).toString();
                String min = (new Long((total_time%3600)/60)).toString();
                String sec = (new Long((total_time%3600)%60)).toString();
                StringBuffer time = new StringBuffer();

                if (hr.equals("0")) {
                    hr = "00";
                } else if (hr.length() == 1) {
                    hr = "0" + hr;
                }

                time.append(hr).append(":");

                if (min.length() < 2) {
                    time.append("0");
                }

                time.append(min).append(":");

                if (sec.length() < 2) {
                    time.append("0");
                }

                time.append(sec);

                // append the demical second
                Float ftWithoutDecSec = new Float(Long.toString(total_time));
                Float ftWithDecSec = new Float(rs.getFloat("mov_total_time"));
                Float ftDecSec = new Float(ftWithDecSec.floatValue() - ftWithoutDecSec.floatValue());
                if (ftWithDecSec.floatValue() > ftWithoutDecSec.floatValue()) {
                    String strDecSec = ftDecSec.toString().substring((ftDecSec.toString()).indexOf('.'));
                    if (strDecSec.length() > 3) {
                        if (Integer.parseInt(strDecSec.substring(3,4)) <= 4) {
                            strDecSec = strDecSec.substring(0, 3);
                        }
                        else {
                            strDecSec = strDecSec.substring(0, 2) + Integer.toString(Integer.parseInt(strDecSec.substring(2,3)) + 1);
                            // do check to make sure there is only 2 decimal place (this will be useful when the 3rd decimal place is 0
                            strDecSec = strDecSec.substring(0, 3);
                        }
                    }
                    time.append(strDecSec);
                }

                if (!isScorm2004) {
                	println(out, "time=" + time.toString(), sess_mod_vendor);
                } else {
                	setValue2ScoDM("cmi.session_time", "PT" + new DecimalFormat("#.##").format(ftWithDecSec) + "S", scoApiReq.mActivityData);
                }

                if (!isScorm2004) {
	                print(out, "score=");
	
	                if (rs.getString("mov_aicc_score") != null) {
	                    println(out, rs.getString("mov_aicc_score"), sess_mod_vendor);
	                } else {
	                    println(out, "", sess_mod_vendor);
	                }
                } else {
                	String score = rs.getString("mov_aicc_score");
					if (score != null && score.indexOf(',') != -1) {
						setValue2ScoDM("cmi.score.raw", score.substring(0, score.indexOf(',')), scoApiReq.mActivityData);
						score = score.substring(score.indexOf(',') + 1);
						if (score.indexOf(',') != -1) {
							setValue2ScoDM("cmi.score.max", score.substring(0, score.indexOf(',')), scoApiReq.mActivityData);
							setValue2ScoDM("cmi.score.min", score.substring(score.indexOf(',') + 1), scoApiReq.mActivityData);
						} else {
							// do nothing
						}
					} else {
						// do nothing
					}
                }

                /*if (rs.getString("mov_score") != null) {
                    String score = rs.getString("mov_score");

                    if (score.endsWith(".0000")) {
                        print(out, score.substring(0, score.length()-5));
                    } else {
                        print(out, score);
                    }
                }

                if (rs.getString("mov_max_score") != null) {
                    String max_score = rs.getString("mov_max_score");

                    if (max_score.endsWith(".0000")) {
                        print(out, "," + max_score.substring(0, max_score.length()-5));
                    } else {
                        print(out, "," + max_score);
                    }
                }

                if (rs.getString("mov_min_score") != null) {
                    String min_score = rs.getString("mov_min_score");

                    if (min_score.endsWith(".0000")) {
                        print(out, "," + min_score.substring(0, min_score.length()-5));
                    } else {
                        print(out, "," + min_score);
                    }
                }*/

                if (!isScorm2004) {
	                println(out, "[student_data]", sess_mod_vendor);

	                if (rs.getString("mov_total_attempt") != null) {
	                    println(out, "attempt_number=" + rs.getString("mov_total_attempt"), sess_mod_vendor);
	                } else {
	                    println(out, "attempt_number=0", sess_mod_vendor);
	                }
	
	                if (rs.getString("mod_pass_score") != null) {
	                    String score = rs.getString("mod_pass_score");
	
	                    if (score.endsWith(".0000")) {
	                        println(out, "mastery_score=" + score.substring(0, score.length()-5), sess_mod_vendor);
	                    } else {
	                        println(out, "mastery_score=" + score, sess_mod_vendor);
	                    }
	                }

	                String[] res_duration = dbUtils.split(rs.getString("res_duration"), ".");
	                hr = "";
	                min = "";
	                sec = "00";
	
	                if (res_duration != null && res_duration.length > 0) {
	                    long l_min = Long.parseLong(res_duration[0]);
	
	                    hr = new Long(l_min/60).toString();
	                    min = new Long(l_min%60).toString();
	
	                    if (res_duration.length > 1) {
	                        sec = res_duration[1];
	                    }
	
	                    StringBuffer max_time = new StringBuffer();
	
	                    max_time.append(hr).append(":");
	
	                    if (min.length() < 2) {
	                        max_time.append("0");
	                    }
	
	                    max_time.append(min).append(":");
	
	                    if (sec.length() == 0) {
	                        max_time.append("00");
	                    } else {
	                        if (sec.length() == 1) {
	                            max_time.append("0").append(sec);
	                        } else {
	                            max_time.append(sec.substring(0, 2));
	                        }
	                    }
	
	                    println(out, "max_time_allowed=" + max_time.toString(), sess_mod_vendor);
	
		                if (rs.getString("mod_time_limit_action") != null && rs.getString("mod_time_limit_action").length() > 0) {
		                    println(out, "time_limit_action=" + rs.getString("mod_time_limit_action"), sess_mod_vendor);
		                }
	                }
	
	                println(out, "[student_demographics]", sess_mod_vendor);
	
	                //String city = rs.getString("usr_city_bil");
	                String city = "";
	                String country = rs.getString("usr_country_bil");
	                String familiar_name = rs.getString("usr_display_bil");
	                String instructor_name = rs.getString("mod_usr_id_instructor");
	                String state = rs.getString("usr_state_bil");
	                String street_address = rs.getString("usr_address_bil");
	                String tel = rs.getString("usr_tel_1");
	
	                if (city != null && city.length() > 0) {
	                    println(out, "city=" + city, sess_mod_vendor);
	                }
	
	                if (country != null && country.length() > 0) {
	                    println(out, "country=" + country, sess_mod_vendor);
	                }
	
	                if (familiar_name != null && familiar_name.length() > 0) {
	                    println(out, "familiar_name=" + familiar_name, sess_mod_vendor);
	                }
	
	                if (instructor_name != null && instructor_name.length() > 0) {
	                    println(out, "instructor_name=" + instructor_name, sess_mod_vendor);
	                }
	
	                if (state != null && state.length() > 0) {
	                    println(out, "state=" + state, sess_mod_vendor);
	                }
	
	                if (street_address != null && street_address.length() > 0) {
	                    println(out, "street_address=" + street_address, sess_mod_vendor);
	                }
	
	                if (tel != null && tel.length() > 0) {
	                    println(out, "telephone=" + tel, sess_mod_vendor);
	                }
                }
            } else {
                throw new cwException("Failed to retrieve record from Module/ModuleEvaluation/RegUser");
            }

            stmt = con.prepareStatement("SELECT mov_core_lesson FROM ModuleEvaluation "+ cwSQL.noLockTable() 
                    + " WHERE mov_cos_id = ? AND mov_ent_id = ? AND mov_mod_id = ? AND mov_tkh_id = ?");
            stmt.setLong(1, cos_id);
            stmt.setLong(2, ent_id);
            stmt.setLong(3, mod_id);
            stmt.setLong(4, tkh_id);
            rs = stmt.executeQuery();

            if (!isScorm2004) {
            	println(out, "[core_lesson]", sess_mod_vendor);
            }

            if (rs.next()) {
                String core_lesson = cwSQL.getClobValue(rs, "mov_core_lesson");

                if (core_lesson != null && core_lesson.length() > 0) {
                	if (!isScorm2004) {
                		println(out, core_lesson, sess_mod_vendor);
                	} else {
                		setValue2ScoDM("cmi.suspend_data", core_lesson, scoApiReq.mActivityData);
                	}
                }
            }

			if (!isScorm2004) {
				stmt = con.prepareStatement("SELECT mod_core_vendor FROM Module "+ cwSQL.noLockTable() 
                    + " WHERE mod_res_id = ?");
				stmt.setLong(1, mod_id);
				rs = stmt.executeQuery();


				if (rs.next()) {
					String core_vendor = cwSQL.getClobValue(rs, "mod_core_vendor");

					if (core_vendor != null) {
						String core_vendor_lst[] = dbUtils.split(core_vendor, CR);

						if (core_vendor_lst != null && core_vendor_lst.length != 0) {
							println(out, "[core_vendor]", sess_mod_vendor);

							for (int i = 0; i < core_vendor_lst.length; i++) {
								println(out, core_vendor_lst[i], sess_mod_vendor);
							}
						}
					}
				}
			}
	            stmt = con.prepareStatement("SELECT obj_developer_id, apm_aicc_score, apm_status FROM Objective, Accomplishment "+ cwSQL.noLockTable() 
                    + " WHERE apm_ent_id = ? AND apm_tkh_id = ? AND obj_id = apm_obj_id AND obj_developer_id IS NOT NULL AND obj_id IN (SELECT rob_obj_id FROM ResourceObjective "+ cwSQL.noLockTable() 
                    + " WHERE rob_res_id = ?) ORDER BY obj_developer_id, apm_id");
	            stmt.setLong(1, ent_id);
	            stmt.setLong(2, tkh_id);
	            stmt.setLong(3, mod_id);
	            rs = stmt.executeQuery();
	            Hashtable obj = new Hashtable();
	            Hashtable status_n_score = null;
	
	            while (rs.next()) {
	                String developer_id = rs.getString("obj_developer_id");
	
	                if (developer_id.length() != 0) {
	                    String status = rs.getString("apm_status");
	                    String score = rs.getString("apm_aicc_score");
	                    String prev_score = null;
	                    String prev_status = null;
	                    Hashtable temp = (Hashtable)obj.get(developer_id);
	
	                    if (temp != null) {
	                        prev_score = (String)temp.get("score");
	                        prev_status = (String)temp.get("status");
	                    }
	
	                    if ((status != null && status.length() != 0) || score != null) {
	                        status_n_score = new Hashtable();
	
	                        if (status != null && status.length() != 0) {
	                            if (status.equalsIgnoreCase("n") && prev_status != null && prev_status.length() != 0) {
	                                status_n_score.put("status", prev_status);
	                            } else {
	                                status_n_score.put("status", status);
	                            }
	                        } else if (prev_status != null && prev_status.length() != 0) {
	                            status_n_score.put("status", prev_status);
	                        }
	
	                        if (score != null) {
	//                            if (score.endsWith(".0000")) {
	//                                score = score.substring(0, score.length()-5);
	//                            }
	                            status_n_score.put("score", score);
	                        } else if (prev_score != null && prev_score.length() != 0) {
	                            status_n_score.put("score", prev_score);
	                        }
	
	                        obj.put(developer_id, status_n_score);
	                    }
	                }
	            }
	
	            if (obj != null) {
	                Enumeration enumeration = null;
	
	                if ((enumeration = obj.keys()) != null && obj.size() != 0) {
	                	if (!isScorm2004) {
	                		println(out, "[objectives_status]", sess_mod_vendor);
	                	}
	
	                    for (int count=1; enumeration.hasMoreElements(); count++) {
	                        String developer_id = (String)enumeration.nextElement();
	                        status_n_score = (Hashtable)obj.get(developer_id);
	                        if (!isScorm2004) {
	                        	println(out, "j_id." + count + "=" + developer_id, sess_mod_vendor);
	                        } else {
	                        	setValue2ScoDM("cmi.objectives." + count + ".id", developer_id, scoApiReq.mActivityData);
	                        }
	
	                        if (status_n_score.containsKey("status")) {
	                        	if (!isScorm2004) {
	                        		println(out, "j_status." + count + "=" + (String)status_n_score.get("status"), sess_mod_vendor);
	                        	} else {
	                        		setValue2ScoDM("cmi.objectives." + count + ".completion_status", (String)status_n_score.get("status"), scoApiReq.mActivityData);
//	                        		setValue2ScoDM("cmi.objectives." + count + ".success_status", (String)status_n_score.get("status"), scoApiReq.mActivityData);
	                        	}
	                        }
	
	                        if (status_n_score.containsKey("score")) {
	                        	if (!isScorm2004) {
	                        		println(out, "j_score." + count + "=" + (String)status_n_score.get("score"), sess_mod_vendor);
	                        	} else {
	                        		StringTokenizer st = new StringTokenizer((String)status_n_score.get("score"), ",");
	                                if (st.hasMoreTokens()) {
	                                	setValue2ScoDM("cmi.objectives." + count + ".score.raw", st.nextToken(), scoApiReq.mActivityData);
	                                }
	                                if (st.hasMoreTokens()) {
	                                	setValue2ScoDM("cmi.objectives." + count + ".score.max", st.nextToken(), scoApiReq.mActivityData);
	                                }
	                                if (st.hasMoreTokens()) {
	                                	setValue2ScoDM("cmi.objectives." + count + ".score.min", st.nextToken(), scoApiReq.mActivityData);
	                                }
	                        	}
	                        }
	                    }
	                }
	            }

        } else {
        	if (!isScorm2004) {
        		println(out, "", sess_mod_vendor);
        	}
        }

debugLogger.debug("^^^^^^^^^^ MARSHAL AICC DATA ^^^^^^^^^^");
        if (stmt != null ) stmt.close();
        return last_mov_status;
    }

    private int unmarshalAiccData(Connection con, HttpServletRequest request, Hashtable aiccData, loginProfile prof, boolean isScorm2004, CMIRequest scoApiReq)
    throws FileNotFoundException, IOException, cwException, cwSysMessage, SQLException {
    	if (isScorm2004) {
    		aiccData.put("command", "");
    		return unmarshalAiccData2004(con, request, aiccData, prof, scoApiReq);
    	} else {
    		return unmarshalAiccData(con, request, aiccData, prof);
    	}
    }
    private int unmarshalAiccData(Connection con, HttpServletRequest request, Hashtable aiccData, loginProfile prof)
        throws FileNotFoundException, IOException, cwException, cwSysMessage, SQLException
    {
        Enumeration enumeration = request.getParameterNames();
        int error = 0;
        Hashtable args = new Hashtable();
        String aiccData_key = "aicc_data";
debugLogger.debug("      unmar 1");
        if (enumeration == null) { 
            throw new cwException("Failed to get Request Parameters");
        }
debugLogger.debug("      unmar 2");
        while (enumeration.hasMoreElements()) {
            String key = (String)enumeration.nextElement();

debugLogger.debug("      unmar 3a");
            if (key.equalsIgnoreCase("session_id")) {
                String sessID = request.getParameter(key);
                String[] lst = null;
debugLogger.debug(" b");
                if (sessID != null) {
                    lst = dbUtils.split(sessID, SEPARATOR);
debugLogger.debug(" c");
                    if (lst != null && lst.length > 4) {
                        if (lst[0] == null || Long.parseLong(lst[0]) == 0 ||
                            lst[1] == null || Long.parseLong(lst[1]) == 0 ||
                            lst[2] == null || Long.parseLong(lst[2]) == 0 ||
                            lst[3] == null ||
                            lst[4] == null ) {
                            error = 3;
debugLogger.debug(" d");
                        } else {
                            aiccData.put("ent_id", new Long(lst[0]));
                            aiccData.put("cos_id", new Long(lst[1]));
                            aiccData.put("mod_id", new Long(lst[2]));
                            aiccData.put("mod_vendor", lst[3]);
                            aiccData.put("sess_id_time", lst[4]);
                            long tkh_id = -1;
                            if (lst.length < 6 || lst[5] == null) {
                                tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con,Long.parseLong(lst[1]),prof.usr_ent_id);
                            }else {
                                tkh_id = Long.parseLong(lst[5]);
                            }
                            aiccData.put("tkh_id", new Long(tkh_id));
                            if(DbTrackingHistory.getAppTrackingIDByCos(con, tkh_id, Long.parseLong(lst[0]), Long.parseLong(lst[1]), Long.parseLong(lst[2])) != 1){
                            	error = 3;
                            	debugLogger.debug("ent_id in session_id is not current user:" + sessID);
                            	throw new cwException("ent_id in session_id is not current user");
                            }
debugLogger.debug(" e");
                            if (prof != null && Long.parseLong(lst[0]) != prof.usr_ent_id ) {
                                error = 3;
                            }
debugLogger.debug(" f");
                        }
                    } else {
debugLogger.debug("SESSION PROBLEM!!!");
                        error = 3;
debugLogger.debug(" g");
                    }
                } else {
                    error = 3;
debugLogger.debug(" h");
                }
            } else {
                if (key.equalsIgnoreCase(aiccData_key)) {
                    aiccData_key = key;
                } else {
                    aiccData.put(key.toLowerCase().trim(), (String)request.getParameter(key).toLowerCase().trim());
                }
debugLogger.debug(" i");
            }
        }

        String core;

        if (aiccData.containsKey("command") && (((String)aiccData.get("command")).equalsIgnoreCase("putparam")) && (core = (String)request.getParameter(aiccData_key)) != null) {
            String[] data = null;
            String mod_vendor = (String)aiccData.get("mod_vendor");
debugLogger.debug("      unmar 4");
            if (mod_vendor != null && mod_vendor.equalsIgnoreCase("skillsoft")) {
                data = dbUtils.split(core, "\n");
            } else {
                data = dbUtils.split(core, new Character(ASCII_13).toString() + new Character(ASCII_10).toString());
            }
debugLogger.debug("      unmar 5");
            for (int i=0; i<data.length; i++) {
                if (data[i].toLowerCase().startsWith(CORE)) {
                    Hashtable core_data = new Hashtable();
                    String[] pair = null;
debugLogger.debug("      unmar 6");
    while (i < data.length-1 && (((pair = dbUtils.split(data[i+1], "=")) != null && pair.length > 1 && (pair[0].trim().equalsIgnoreCase("lesson_location") || pair[0].trim().equalsIgnoreCase("lesson_status") || pair[0].trim().equalsIgnoreCase("score") || pair[0].trim().equalsIgnoreCase("time"))) || ! data[i+1].startsWith("["))) {

                        if (pair != null && pair.length > 1) {
                            pair[0] = pair[0].trim();
                            pair[1] = pair[1].trim();
                            if (pair[0].equalsIgnoreCase("lesson_location")) {
                                core_data.put("lesson_location", pair[1]);
                            } else if (pair[0].equalsIgnoreCase("lesson_status")) {
                                String status = "n";
debugLogger.debug("      unmar 7");
                                if (pair[1].startsWith("p") || pair[1].startsWith("P") ||
                                    pair[1].startsWith("c") || pair[1].startsWith("C") ||
                                    pair[1].startsWith("f") || pair[1].startsWith("F") ||
                                    pair[1].startsWith("i") || pair[1].startsWith("I") ||
                                    pair[1].startsWith("b") || pair[1].startsWith("B") ||
                                    pair[1].startsWith("n") || pair[1].startsWith("N")) {
                                    status = pair[1].substring(0, 1);
                                }
debugLogger.debug("      unmar 8");
                                core_data.put("lesson_status", status.toUpperCase());
                                String[] status_n_flag = dbUtils.split(pair[1], ",");
debugLogger.debug("      unmar 9");
                                if (status_n_flag != null && status_n_flag.length > 1) {
                                    if (status_n_flag[1].startsWith("t") || status_n_flag[1].startsWith("T") ||
                                        status_n_flag[1].startsWith("s") || status_n_flag[1].startsWith("S") ||
                                        status_n_flag[1].startsWith("l") || status_n_flag[1].startsWith("L")) {
                                        String flag = status_n_flag[1].substring(0, 1);
                                        core_data.put("lesson_status_flag", flag.toUpperCase());
debugLogger.debug("      unmar 10");
                                    }
                                }
                            } else if (pair[0].equalsIgnoreCase("score")) {
                                core_data.put("aicc_score", pair[1]);

                                String[] score_lst = dbUtils.split(pair[1], ",");
debugLogger.debug("      unmar 11");
                                if (score_lst != null && score_lst.length > 0 && score_lst[0].length() > 0) {
                                    try {
                                        core_data.put("score", new Float(score_lst[0]));
                                    } catch (java.lang.NumberFormatException e) {
                                    }
                                }

                                if (score_lst != null && score_lst.length > 1 && score_lst[1].length() > 0) {
                                    try {
                                        core_data.put("max_score", new Float(score_lst[1]));
                                    } catch (java.lang.NumberFormatException e) {
                                    }
                                }

                                if (score_lst != null && score_lst.length > 2 && score_lst[2].length() > 0) {
                                    try {
                                        core_data.put("min_score", new Float(score_lst[2]));
                                    } catch (java.lang.NumberFormatException e) {
                                    }
                                }


debugLogger.debug("      unmar 12");
                            } else if (pair[0].equalsIgnoreCase("time")) {
                                core_data.put("time", new String(pair[1]));
debugLogger.debug("      unmar 13");
                            }
                        }

                        i++;
                        pair = null;
                    }
debugLogger.debug("      unmar 14");
                    aiccData.put("core", core_data);
                } else if (data[i].toLowerCase().startsWith(CORE_LESSON)) {
                    StringBuffer core_lesson = new StringBuffer();
                    int has_data = 0;
debugLogger.debug("      unmar 15");
                    while (i < data.length-1 && ! data[i+1].toLowerCase().startsWith(CORE) && ! data[i+1].toLowerCase().startsWith(OBJECTIVES_STATUS)) {
                        //if (has_data == 1) {
                        //    core_lesson.append(NEWL);
                        //}
debugLogger.debug("      unmar 16");
                        core_lesson.append(data[++i]).append(NEWL);
                        has_data = 1;
                    }
debugLogger.debug("      unmar 17");
                    if (has_data == 1) {
						aiccData.put("core_lesson", core_lesson.toString());
                    }
                } else if (data[i].toLowerCase().startsWith(OBJECTIVES_STATUS)) {
                    Hashtable obj_data = new Hashtable();
                    Hashtable h_id = new Hashtable();
                    Hashtable h_status = new Hashtable();
                    Hashtable h_score = new Hashtable();
                    String[] pair;
                    String[] temp;
debugLogger.debug("      unmar 18");
                    while (i < data.length-1 && (pair = dbUtils.split(data[i+1], "=")) != null && pair.length > 0 && (temp = dbUtils.split(pair[0], ".")) != null && temp.length > 1 && (temp[0].equalsIgnoreCase("j_id") || temp[0].equalsIgnoreCase("j_status") || temp[0].equalsIgnoreCase("j_score"))) {
                        if (temp[0].equalsIgnoreCase("j_id") && pair.length > 1) {
                            h_id.put(temp[1], pair[1]);
                        } else if (temp[0].equalsIgnoreCase("j_status") && pair.length > 1) {
                            h_status.put(temp[1], pair[1]);
                        } else if (temp[0].equalsIgnoreCase("j_score") && pair.length > 1) {
                            h_score.put(temp[1], pair[1]);
                        }
debugLogger.debug("      unmar 19");
                        i++;
                    }
debugLogger.debug("      unmar 20");
                    Enumeration id_lst = h_id.keys();

                    if (id_lst != null) {
                        while (id_lst.hasMoreElements()) {
                            Hashtable score_n_status = new Hashtable();
                            String id = (String)id_lst.nextElement();
debugLogger.debug("      unmar 21");
                            if (h_status.containsKey(id)) {
                                String status_char = "n";
                                String status = (String)h_status.get(id);
debugLogger.debug("      unmar 22");
                                if (status.startsWith("p") || status.startsWith("P") ||
                                    status.startsWith("c") || status.startsWith("C") ||
                                    status.startsWith("f") || status.startsWith("F") ||
                                    status.startsWith("i") || status.startsWith("I") ||
                                    status.startsWith("b") || status.startsWith("B") ||
                                    status.startsWith("n") || status.startsWith("N")) {
                                    status_char = status.substring(0, 1);
                                }
debugLogger.debug("      unmar 23");
                                score_n_status.put("status", status_char.toUpperCase());
                            }
debugLogger.debug("      unmar 24");
                            if (h_score.containsKey(id)) {
                                String score = (String)h_score.get(id);
                                String[] score_lst = dbUtils.split(score, ",");

                                score_n_status.put("aicc_score", score);

                                if (score_lst != null && score_lst.length > 0 && score_lst[0].length() > 0) {
                                    try {
                                        score_n_status.put("score", new Float(score_lst[0]));
                                    } catch (java.lang.NumberFormatException e) {
                                    }
                                }

                                if (score_lst != null && score_lst.length > 1 && score_lst[1].length() > 0) {
                                    try {
                                        score_n_status.put("max_score", new Float(score_lst[1]));
                                    } catch (java.lang.NumberFormatException e) {
                                    }
                                }

                                if (score_lst != null && score_lst.length > 2 && score_lst[2].length() > 0) {
                                    try {
                                        score_n_status.put("min_score", new Float(score_lst[2]));
                                    } catch (java.lang.NumberFormatException e) {
                                    }
                                }
                            }
debugLogger.debug("      unmar 26");
                            if (score_n_status != null && score_n_status.size() != 0) {
                                score_n_status.put("dev_id", (String)h_id.get(id));
                                obj_data.put(id, score_n_status);
                            }
                        }
                    }
debugLogger.debug("      unmar 27");
                    aiccData.put("objectives_status", obj_data);
                }
            }
        }

        return error;
    }

	private int unmarshalAiccData2004(Connection con, HttpServletRequest request, Hashtable aiccData, loginProfile prof, CMIRequest scoApiReq) throws FileNotFoundException, IOException, cwException, cwSysMessage,
			SQLException {
		int error = 0;
		String aiccData_key = "aicc_data";

		String sessID = scoApiReq.aicc_sid;
		String[] lst = null;
		if (sessID != null) {
			lst = dbUtils.split(sessID, SEPARATOR);
			if (lst != null && lst.length > 4) {
				if (lst[0] == null || Long.parseLong(lst[0]) == 0 || lst[1] == null || Long.parseLong(lst[1]) == 0 || lst[2] == null || Long.parseLong(lst[2]) == 0 || lst[3] == null || lst[4] == null) {
					error = 3;
					debugLogger.debug(" d");
				} else {
					aiccData.put("ent_id", new Long(lst[0]));
					aiccData.put("cos_id", new Long(lst[1]));
					aiccData.put("mod_id", new Long(lst[2]));
					aiccData.put("mod_vendor", lst[3]);
					aiccData.put("sess_id_time", lst[4]);
					long tkh_id = -1;
					if (lst.length < 6 || lst[5] == null) {
						tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, Long.parseLong(lst[1]), prof.usr_ent_id);
					} else {
						tkh_id = Long.parseLong(lst[5]);
					}
					aiccData.put("tkh_id", new Long(tkh_id));
					if (DbTrackingHistory.getAppTrackingIDByCos(con, tkh_id, Long.parseLong(lst[0]), Long.parseLong(lst[1]), Long.parseLong(lst[2])) != 1) {
						error = 3;
						debugLogger.debug("ent_id in session_id is not current user:" + sessID);
						throw new cwException("ent_id in session_id is not current user");
					}
					debugLogger.debug(" e");
					if (prof != null && Long.parseLong(lst[0]) != prof.usr_ent_id) {
						error = 3;
					}
					debugLogger.debug(" f");
				}
			} else {
				debugLogger.debug("SESSION PROBLEM!!!");
				error = 3;
				debugLogger.debug(" g");
			}
		} else {
			error = 3;
			debugLogger.debug(" h");
		}
		// should get data from scoApiReq.mActivityData
		/*
		 * for (int i = 0; i < scoApiReq.mActivityData.) if
		 * (key.equalsIgnoreCase(aiccData_key)) { aiccData_key = key; } else {
		 * aiccData.put(key.toLowerCase().trim(),
		 * (String)request.getParameter(key).toLowerCase().trim()); }
		 * debugLogger.debug(" i"); }
		 */

		if (scoApiReq.mRequestType == CMIRequest.TYPE_SET) {
			Hashtable core_data = new Hashtable();
			core_data.put("lesson_location", getDmValue("cmi.location", scoApiReq));
			String suspend_data = getDmValue("cmi.suspend_data", scoApiReq);
			if (suspend_data.length() > 0) {
				aiccData.put("core_lesson", suspend_data);
			}
			//
			String status1 = "n";
			String tmp_status = getDmValue("cmi.completion_status", scoApiReq);
			debugLogger.debug("      unmar 7");
			if (tmp_status.startsWith("p") || tmp_status.startsWith("P") 
					|| tmp_status.startsWith("c") || tmp_status.startsWith("C") 
					|| tmp_status.startsWith("f") || tmp_status.startsWith("F")
					|| tmp_status.startsWith("i") || tmp_status.startsWith("I") 
					|| tmp_status.startsWith("b") || tmp_status.startsWith("B") 
					|| tmp_status.startsWith("n") || tmp_status.startsWith("N")) {
				status1 = tmp_status.substring(0, 1);
			}
			debugLogger.debug("      unmar 8");
			// core_data.put("lesson_status", status.toUpperCase());
			core_data.put("lesson_status", status1.toUpperCase());
			String[] status_n_flag = dbUtils.split(tmp_status, ",");
			debugLogger.debug("      unmar 9");
			if (status_n_flag != null && status_n_flag.length > 1) {
				if (status_n_flag[1].startsWith("t") || status_n_flag[1].startsWith("T") 
						|| status_n_flag[1].startsWith("s") || status_n_flag[1].startsWith("S") 
						|| status_n_flag[1].startsWith("l")	|| status_n_flag[1].startsWith("L")) {
					String flag = status_n_flag[1].substring(0, 1);
					core_data.put("lesson_status_flag", flag.toUpperCase());
					debugLogger.debug("      unmar 10");
				}
			}

			// core_data.put("score", new Float(score_lst[0]));
			if (getDmValue("cmi.score.raw", scoApiReq) != "") {
				core_data.put("score", new Float(getDmValue("cmi.score.raw", scoApiReq)));
			}

			// core_data.put("max_score", new Float(score_lst[1]));
			if (getDmValue("cmi.score.max", scoApiReq) != "") {
				core_data.put("max_score", new Float(getDmValue("cmi.score.max", scoApiReq)));
			}

			// core_data.put("min_score", new Float(score_lst[2]));
			if (getDmValue("cmi.score.min", scoApiReq) != "") {
				core_data.put("min_score", new Float(getDmValue("cmi.score.min", scoApiReq)));
			}

			if (getDmValue("cmi.session_time", scoApiReq) != "" ) {
				core_data.put("time", dbUtils.transSco2004TimeInterval(getDmValue("cmi.session_time", scoApiReq)));
			}

			aiccData.put("core", core_data);
			if (getDmValue("cmi.objectives._count", scoApiReq) != "") {
				int count = new Integer(getDmValue("cmi.objectives._count", scoApiReq)).intValue();
				Hashtable obj_data = new Hashtable();
				if (count > 0) {
					int i = 0;
					while (i < count) {
						Hashtable score_n_status = new Hashtable();
						String id = getDmValue("cmi.objectives." + count + ".id", scoApiReq);
	
						String status_char = "n";
						String status = getDmValue("cmi.objectives." + count + ".completion_status", scoApiReq);
						if (status.startsWith("p") || status.startsWith("P") 
								|| status.startsWith("c") || status.startsWith("C") 
								|| status.startsWith("f") || status.startsWith("F")
								|| status.startsWith("i") || status.startsWith("I") 
								|| status.startsWith("b") || status.startsWith("B") 
								|| status.startsWith("n") || status.startsWith("N")) {
							status_char = status.substring(0, 1);
						}
						score_n_status.put("status", status_char.toUpperCase());
	
						score_n_status.put("aicc_score", getDmValue("cmi.objectives." + count + ".score.raw", scoApiReq) 
								+ "," + getDmValue("cmi.objectives." + count + ".score.max", scoApiReq) + ","
								+ getDmValue("cmi.objectives." + count + ".score.min", scoApiReq));
	
						try {
							score_n_status.put("score", new Float(getDmValue("cmi.objectives." + count + ".score.raw", scoApiReq)));
						} catch (java.lang.NumberFormatException e) {
						}
	
						try {
							score_n_status.put("max_score", new Float(getDmValue("cmi.objectives." + count + ".score.max", scoApiReq)));
						} catch (java.lang.NumberFormatException e) {
						}
	
						try {
							score_n_status.put("min_score", new Float(getDmValue("cmi.objectives." + count + ".score.min", scoApiReq)));
						} catch (java.lang.NumberFormatException e) {
						}
						if (score_n_status != null && score_n_status.size() != 0) {
							score_n_status.put("dev_id", id);
							obj_data.put(id, score_n_status);
						}
						i++;
					}
					aiccData.put("objectives_status", obj_data);
				}
			}
		}
		
		
	/*
		 * else if (data[i].toLowerCase().startsWith(CORE_LESSON)) {
		 * StringBuffer core_lesson = new StringBuffer(); int has_data = 0;
		 * debugLogger.debug("      unmar 15"); while (i < data.length-1 && !
		 * data[i+1].toLowerCase().startsWith(CORE) && !
		 * data[i+1].toLowerCase().startsWith(OBJECTIVES_STATUS)) { //if
		 * (has_data == 1) { // core_lesson.append(NEWL); //}
		 * debugLogger.debug("      unmar 16");
		 * core_lesson.append(data[++i]).append(NEWL); has_data = 1; }
		 * debugLogger.debug("      unmar 17"); if (has_data == 1) {
		 * aiccData.put("" + "", core_lesson.toString()); } }
		 */
		// }

		return error;
	}
    
    private  void putAiccData2DB(Connection con, HttpSession sess, loginProfile prof, Hashtable aiccData)
        throws SQLException, qdbException, cwException, cwSysMessage, qdbErrMessage // add cwSysMessage by emily
    {
debugLogger.debug("             ID 0");
        Timestamp cur_time = dbUtils.getTime(con);
        long tkh_id = ((Long)aiccData.get("tkh_id")).longValue();

        if (aiccData.get("core") == null) {
            String SQL = "SELECT mov_total_time, mov_status, mov_score, mod_vendor FROM ModuleEvaluation "+ cwSQL.noLockTable() 
                    + ", Module "+ cwSQL.noLockTable() 
                    + " WHERE mov_ent_id = ? AND mov_mod_id = ? AND mod_res_id = mov_mod_id AND mov_tkh_id = ?";
            PreparedStatement stmt = con.prepareStatement(SQL);
            stmt.setLong(1, ((Long)aiccData.get("ent_id")).longValue());
            stmt.setLong(2, ((Long)aiccData.get("mod_id")).longValue());
            stmt.setLong(3, tkh_id);
            ResultSet rs = stmt.executeQuery();

            if (! rs.next()) {
                // << BEGIN for oracle migration!
                //SQL = "INSERT INTO ModuleEvaluation (mov_cos_id, mov_ent_id, mov_mod_id, mov_last_acc_datetime, mov_total_time, mov_status, mov_credit, mov_core_lesson) VALUES (?, ?, ?, ?, ?, ?, ?, " + cwSQL.getClobNull(con) + ")";
                SQL = "INSERT INTO ModuleEvaluation "+ cwSQL.rowLockTable()
                    + " (mov_cos_id, mov_ent_id, mov_mod_id, mov_last_acc_datetime, mov_total_time, mov_status, mov_credit, mov_tkh_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                // >> END
debugLogger.debug("             ID 1");
                stmt = con.prepareStatement(SQL);
                stmt.setLong(1, ((Long)aiccData.get("cos_id")).longValue());
                stmt.setLong(2, ((Long)aiccData.get("ent_id")).longValue());
                stmt.setLong(3, ((Long)aiccData.get("mod_id")).longValue());
                stmt.setTimestamp(4, cur_time);
                stmt.setFloat(5, 0);
                stmt.setString(6, "N");
                stmt.setString(7, "C");
                stmt.setLong(8, tkh_id);

                if (stmt.executeUpdate() != 1) {
                    throw new cwException("Error: Failed to add record to ModuleEvaluation");
                }

                // insert module evaluation history
                dbModuleEvaluationHistory dbModEvalHist = new dbModuleEvaluationHistory();
                dbModEvalHist.mvh_ent_id = ((Long)aiccData.get("ent_id")).longValue();
                dbModEvalHist.mvh_mod_id = ((Long)aiccData.get("mod_id")).longValue();
                dbModEvalHist.mvh_last_acc_datetime = cur_time;
                dbModEvalHist.mvh_tkh_id = tkh_id;
                dbModEvalHist.mvh_ele_loc = null;
                dbModEvalHist.mvh_total_time = 0;
                dbModEvalHist.mvh_total_attempt = 0;
                dbModEvalHist.mvh_status = "N";
                dbModEvalHist.mvh_score = null;
                dbModEvalHist.mvh_create_usr_id = prof.usr_id;
                dbModEvalHist.mvh_create_timestamp = cur_time;
                dbModEvalHist.ins(con);

//                CourseCriteria.setAttendFromModule(con, prof, ((Long)aiccData.get("mod_id")).longValue(), ((Long)aiccData.get("cos_id")).longValue(), ((Long)aiccData.get("ent_id")).longValue(), tkh_id, "N");
                CourseCriteria.setAttendFromModule(con, prof, ((Long)aiccData.get("mod_id")).longValue(), ((Long)aiccData.get("cos_id")).longValue(),  ((Long)aiccData.get("ent_id")).longValue(),  tkh_id, "N", 0, cur_time, cur_time);

            }

            rs.close();
            stmt.close();
            return;
        }

        String SQL = "SELECT mov_total_time, mov_status, mov_score, mov_max_score, mov_min_score, mov_aicc_score, mod_vendor FROM ModuleEvaluation "+ cwSQL.noLockTable() 
                    + ", Module "+ cwSQL.noLockTable() 
                    + " WHERE mov_ent_id = ? AND mov_mod_id = ? AND mod_res_id = mov_mod_id AND mov_tkh_id = ?";
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        ResultSet rs;
        float total_time = 0;
        //String vendor = null;
        String sess_vendor = null;
debugLogger.debug("             ID 2");
        if (aiccData.get("mod_vendor") != null) {
            sess_vendor = (String)aiccData.get("mod_vendor");
        }
debugLogger.debug("             ID 3");
        stmt = con.prepareStatement(SQL);
        stmt.setLong(1, ((Long)aiccData.get("ent_id")).longValue());
        stmt.setLong(2, ((Long)aiccData.get("mod_id")).longValue());
        stmt.setLong(3, tkh_id);
        rs = stmt.executeQuery();
        boolean has_record = false;
        String prev_status = null;
        String prev_score = null;
        String prev_max_score = null;
        String prev_min_score = null;
        String prev_aicc_score = null;
        if(rs.next()){
            has_record = true;
            total_time = rs.getFloat("mov_total_time");
            prev_status = rs.getString("mov_status");
            prev_score = rs.getString("mov_score");
            prev_max_score = rs.getString("mov_max_score");
            prev_min_score = rs.getString("mov_min_score");
            prev_aicc_score = rs.getString("mov_aicc_score");
        }
        rs.close();
        stmt.close();
debugLogger.debug("             ID 4");
        String s_time = (String)((Hashtable)aiccData.get("core")).get("time");
        float prev_time = 0;
debugLogger.debug("             ID 5");
        if (s_time != null) {
        	
        	try{
        		prev_time  = new Float(s_time).floatValue();
        	}catch(Exception e){
	            String[] time_data = dbUtils.split(s_time, ":");
	            String hr = time_data[0];
	            String min = time_data[1];
	            String temp_sec = time_data[2];
	            String sec_data[] = dbUtils.split(temp_sec, ".");
	            String sec = sec_data[0];
	            // decimal seconds added for SCORM test suite (2 decimal place)
	            String decimal_sec = "0";
	            if (sec_data.length > 1) {
	                decimal_sec = "0." + sec_data[1];
	            }
	
	debugLogger.debug("             ID 6");
	
	            if (time_data != null && time_data.length == 3) {
	                prev_time = new Float(hr).floatValue()*3600 + new Float(min).floatValue()*60 + new Float(sec).floatValue();
	                if (sec_data.length > 1) {
	                    prev_time += new Float(decimal_sec).floatValue();
	                }
	            }
        	}
            
        }
debugLogger.debug("             ID 7");
        Hashtable sess_time_hash = getSessTime(sess, (Long)aiccData.get("ent_id"));
        float sess_time = 0;
        String s_sess_time = ((Long)aiccData.get("mod_id")).toString() + (String)aiccData.get("sess_id_time");
debugLogger.debug("             ID 8");
        if (sess_time_hash != null) {
debugLogger.debug(":_:_: GET_SESS_HASH!!!");
            Float temp = (Float)sess_time_hash.get(s_sess_time);
debugLogger.debug("             ID 9");
            if (temp != null) {
                sess_time = temp.floatValue();
debugLogger.debug("<><> GET_FROM_HASH = " + sess_time);
debugLogger.debug("             ID 10");
            }
        } else {
            sess_time_hash = new Hashtable();
        }
debugLogger.debug("             ID 11");
        sess_time_hash.put(s_sess_time, new Float(prev_time));
        putSessTime(sess, sess_time_hash, (Long)aiccData.get("ent_id"));
debugLogger.debug("             ID 12");
        boolean boolInsert = false;

        

        // object for insertion of module evaluation history
        dbModuleEvaluationHistory dbModEvalHist = new dbModuleEvaluationHistory();
		
        if (has_record) {
            boolInsert = false;
            
//            boolean hisExist = dbModuleEvaluationHistory.existHis(con,((Long)aiccData.get("mod_id")).longValue(),tkh_id);
            String everMaxScore = prev_score;//dbModuleEvaluationHistory.getEverMaxScore(con,((Long)aiccData.get("mod_id")).longValue(),tkh_id);
//dbModuleEvaluationHistory.everStatus(con,((Long)aiccData.get("mod_id")).longValue(),tkh_id,dbAiccPath.STATUS_PASSED,dbAiccPath.STATUS_COMPLETE);
            
            //vendor = rs.getString("mod_vendor");
debugLogger.debug("             ID 13");
            SQL = "UPDATE ModuleEvaluation "+ cwSQL.rowLockTable()
                    + " SET mov_last_acc_datetime = ?, mov_ele_loc = ?, mov_total_time = ?, mov_status = ?, mov_score = ?, mov_max_score = ?, mov_min_score = ?, mov_aicc_score = ?, mov_status_flag = ?, mov_credit = ? WHERE mov_cos_id = ? AND mov_ent_id = ? AND mov_mod_id = ? AND mov_tkh_id = ?";

            stmt1 = con.prepareStatement(SQL);

            stmt1.setTimestamp(1, cur_time);
            dbModEvalHist.mvh_last_acc_datetime = cur_time;

            stmt1.setString(2, (String)((Hashtable)aiccData.get("core")).get("lesson_location"));
            dbModEvalHist.mvh_ele_loc = (String)((Hashtable)aiccData.get("core")).get("lesson_location");

debugLogger.debug("             ID 14");



debugLogger.debug("&&& " + sess_time);
                    stmt1.setFloat(3, roundToDecimal(total_time + prev_time - sess_time, 2));
                    dbModEvalHist.mvh_total_time = roundToDecimal(prev_time - sess_time, 2);
debugLogger.debug("             ID 16");

debugLogger.debug("             ID 17");

            if (aiccData.get("credit") == null || ! aiccData.get("credit").equals("N")) {
				if(dbAiccPath.STATUS_COMPLETE.equalsIgnoreCase(prev_status) || dbAiccPath.STATUS_PASSED.equalsIgnoreCase(prev_status)){
					stmt1.setString(4, prev_status);
				}else{
					if(((Hashtable)aiccData.get("core")).get("lesson_status") == null){
						stmt1.setString(4, prev_status);
					}else{
						stmt1.setString(4, (String)((Hashtable)aiccData.get("core")).get("lesson_status"));
					}
				}
                dbModEvalHist.mvh_status = (String)((Hashtable)aiccData.get("core")).get("lesson_status");
debugLogger.debug("             ID 18");
            } else {
					stmt1.setString(4, prev_status);
                dbModEvalHist.mvh_status = prev_status;
debugLogger.debug("             ID 19");
            }
debugLogger.debug("             ID 20");
            if (aiccData.get("credit") == null || ! aiccData.get("credit").equals("N")) {


                Float haha = (Float)((Hashtable)aiccData.get("core")).get("score");
                String curScore = null;
                if (haha != null) {
                    curScore = haha.toString();
                }
				if( everMaxScore != null){
					curScore = dbModuleEvaluation.getMaxScore(curScore,prev_score,Float.valueOf(everMaxScore).floatValue());
					stmt1.setFloat(5,new Float(curScore).floatValue());
				}else{
					if(curScore != null){
						stmt1.setFloat(5,new Float(curScore).floatValue());
					}else{
						stmt1.setNull(5, java.sql.Types.FLOAT);
					}
				}



                if (((Hashtable)aiccData.get("core")).get("max_score") != null) {
                    stmt1.setFloat(6, ((Float)((Hashtable)aiccData.get("core")).get("max_score")).floatValue());
debugLogger.debug("             ID 21");
                } else {
                    stmt1.setNull(6, java.sql.Types.FLOAT);
debugLogger.debug("             ID 22");
                }

                if (((Hashtable)aiccData.get("core")).get("min_score") != null) {
                    stmt1.setFloat(7, ((Float)((Hashtable)aiccData.get("core")).get("min_score")).floatValue());
debugLogger.debug("             ID 21");
                } else {
                    stmt1.setNull(7, java.sql.Types.FLOAT);
debugLogger.debug("             ID 22");
                }
debugLogger.debug("                                 ID 22.a " + (String)((Hashtable)aiccData.get("core")).get("aicc_score"));
                stmt1.setString(8, ((String)((Hashtable)aiccData.get("core")).get("aicc_score")));
                dbModEvalHist.mvh_score = ((String)((Hashtable)aiccData.get("core")).get("aicc_score"));
                if (dbModEvalHist.mvh_score != null && dbModEvalHist.mvh_score.indexOf(",") != -1) {
                    dbModEvalHist.mvh_score = dbModEvalHist.mvh_score.substring(0, dbModEvalHist.mvh_score.indexOf(","));
                }
            } else {
				if(everMaxScore != null){
					String curScore = dbModuleEvaluation.getMaxScore(null,prev_score,Float.valueOf(everMaxScore).floatValue());
					stmt1.setFloat(5,new Float(curScore).floatValue());
				}else{
					if(prev_score != null){
						//this condition may never occur.
						stmt1.setFloat(5,new Float(prev_score).floatValue());
					}else{
						stmt1.setNull(5, java.sql.Types.FLOAT);
				  }
				}

                if (prev_max_score != null) {
                    stmt1.setFloat(6, (new Float(prev_max_score)).floatValue());
debugLogger.debug("             ID 23");
                } else {
                    stmt1.setNull(6, java.sql.Types.FLOAT);
debugLogger.debug("             ID 24");
                }

                if (prev_min_score != null) {
                    stmt1.setFloat(7, (new Float(prev_min_score)).floatValue());
debugLogger.debug("             ID 23");
                } else {
                    stmt1.setNull(7, java.sql.Types.FLOAT);
debugLogger.debug("             ID 24");
                }
debugLogger.debug("                                 ID 22.a " + prev_aicc_score);
                stmt1.setString(8, prev_aicc_score);
                dbModEvalHist.mvh_score = prev_aicc_score;
                if (dbModEvalHist.mvh_score != null && dbModEvalHist.mvh_score.indexOf(",") != -1) {
                    dbModEvalHist.mvh_score = dbModEvalHist.mvh_score.substring(0, dbModEvalHist.mvh_score.indexOf(","));
                }
            }
debugLogger.debug("             ID 25");
            stmt1.setString(9, (String)((Hashtable)aiccData.get("core")).get("lesson_status_flag"));

            if (aiccData.get("credit") == null || ! aiccData.get("credit").equals("N")) {
                stmt1.setString(10, "C");
            } else {
                stmt1.setString(10, "N");
            }

            stmt1.setLong(11, ((Long)aiccData.get("cos_id")).longValue());
            stmt1.setLong(12, ((Long)aiccData.get("ent_id")).longValue());
            dbModEvalHist.mvh_ent_id = ((Long)aiccData.get("ent_id")).longValue();
            stmt1.setLong(13, ((Long)aiccData.get("mod_id")).longValue());
            dbModEvalHist.mvh_mod_id = ((Long)aiccData.get("mod_id")).longValue();
            stmt1.setLong(14, tkh_id);
            dbModEvalHist.mvh_tkh_id = tkh_id;
            dbModEvalHist.mvh_total_attempt = 0;

debugLogger.debug("             ID 26");

        } else {
            boolInsert = true;
            SQL = "INSERT INTO ModuleEvaluation "+ cwSQL.rowLockTable() 
                    + " (mov_cos_id, mov_ent_id, mov_mod_id, mov_last_acc_datetime, mov_ele_loc, mov_total_time, mov_status, mov_score, mov_max_score, mov_min_score, mov_aicc_score, mov_status_flag, mov_total_attempt, mov_credit, mov_tkh_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
debugLogger.debug("             ID 27");
            stmt1 = con.prepareStatement(SQL);
            stmt1.setLong(1, ((Long)aiccData.get("cos_id")).longValue());
            stmt1.setLong(2, ((Long)aiccData.get("ent_id")).longValue());
            dbModEvalHist.mvh_ent_id = ((Long)aiccData.get("ent_id")).longValue();
            stmt1.setLong(3, ((Long)aiccData.get("mod_id")).longValue());
            dbModEvalHist.mvh_mod_id = ((Long)aiccData.get("mod_id")).longValue();
            stmt1.setTimestamp(4, cur_time);
            dbModEvalHist.mvh_last_acc_datetime = cur_time;
            stmt1.setString(5, (String)((Hashtable)aiccData.get("core")).get("lesson_location"));
            dbModEvalHist.mvh_ele_loc = (String)((Hashtable)aiccData.get("core")).get("lesson_location");
            stmt1.setFloat(6, roundToDecimal(total_time + prev_time, 2));
            dbModEvalHist.mvh_total_time = roundToDecimal(prev_time, 2);

debugLogger.debug("             ID 28");
            if (aiccData.get("credit") == null || ! aiccData.get("credit").equals("N")) {
                stmt1.setString(7, (String)((Hashtable)aiccData.get("core")).get("lesson_status"));
                dbModEvalHist.mvh_status = (String)((Hashtable)aiccData.get("core")).get("lesson_status");
debugLogger.debug("             ID 29");
            } else {
                stmt1.setString(7, "N");
                dbModEvalHist.mvh_status = "N";
debugLogger.debug("             ID 30");
            }
debugLogger.debug("             ID 31");
            if (aiccData.get("credit") == null || ! aiccData.get("credit").equals("N")) {
                if (((Hashtable)aiccData.get("core")).get("score") != null) {
                    stmt1.setFloat(8, ((Float)((Hashtable)aiccData.get("core")).get("score")).floatValue());
debugLogger.debug("             ID 32");
                } else {
                    stmt1.setNull(8, java.sql.Types.FLOAT);
debugLogger.debug("             ID 33");
                }

                if (((Hashtable)aiccData.get("core")).get("max_score") != null) {
                    stmt1.setFloat(9, ((Float)((Hashtable)aiccData.get("core")).get("max_score")).floatValue());
debugLogger.debug("             ID 32");
                } else {
                    stmt1.setNull(9, java.sql.Types.FLOAT);
debugLogger.debug("             ID 33");
                }

                if (((Hashtable)aiccData.get("core")).get("min_score") != null) {
                    stmt1.setFloat(10, ((Float)((Hashtable)aiccData.get("core")).get("min_score")).floatValue());
debugLogger.debug("             ID 32");
                } else {
                    stmt1.setNull(10, java.sql.Types.FLOAT);
debugLogger.debug("             ID 33");
                }
debugLogger.debug("                                 ID 33.a " + (String)((Hashtable)aiccData.get("core")).get("aicc_score"));
                stmt1.setString(11, ((String)((Hashtable)aiccData.get("core")).get("aicc_score")));
                dbModEvalHist.mvh_score = ((String)((Hashtable)aiccData.get("core")).get("aicc_score"));
                if (dbModEvalHist.mvh_score != null && dbModEvalHist.mvh_score.indexOf(",") != -1) {
                    dbModEvalHist.mvh_score = dbModEvalHist.mvh_score.substring(0, dbModEvalHist.mvh_score.indexOf(","));
                }
            } else {
debugLogger.debug("             ID 33.a");
                stmt1.setNull(8, java.sql.Types.FLOAT);
                stmt1.setNull(9, java.sql.Types.FLOAT);
                stmt1.setNull(10, java.sql.Types.FLOAT);
                stmt1.setString(11, null);
                dbModEvalHist.mvh_score = null;
            }

debugLogger.debug("             ID 34");
            stmt1.setString(12, (String)((Hashtable)aiccData.get("core")).get("lesson_status_flag"));
            stmt1.setLong(13, 1);
            dbModEvalHist.mvh_total_attempt = 1;

            if (aiccData.get("credit") == null || ! aiccData.get("credit").equals("N")) {
                stmt1.setString(14, "C");
            } else {
                stmt1.setString(14, "N");
            }

            stmt1.setLong(15,  tkh_id);
            dbModEvalHist.mvh_tkh_id = tkh_id;

debugLogger.debug("             ID 35");
        }

       
debugLogger.debug("             ID 36");
        if (stmt1.executeUpdate() != 1) {
            stmt1.close();
            throw new cwException("Error: Failed to add record to ModuleEvaluation");
        }
        stmt1.close();
        dbModEvalHist.mvh_create_usr_id = prof.usr_id;
        dbModEvalHist.mvh_create_timestamp = cur_time;
        dbModEvalHist.ins(con);
        
        // invoke course criteria API



debugLogger.debug("             ID 37");
        String condition = "mov_cos_id = " + ((Long)aiccData.get("cos_id")).longValue() +
                          " AND mov_ent_id = " + ((Long)aiccData.get("ent_id")).longValue() +
                          " AND mov_mod_id = " + ((Long)aiccData.get("mod_id")).longValue() +
                          " AND mov_tkh_id = " + tkh_id ;

        String[] columnName = new String[1];
        String[] columnValue = new String[1];
        columnName[0] = "mov_core_lesson";
        columnValue[0] = (String)aiccData.get("core_lesson");
        if(aiccData.get("core_lesson") != null && ((String)aiccData.get("core_lesson")).trim().length() > 0){
        	cwSQL.updateClobFields(con, "ModuleEvaluation  ", columnName, columnValue, condition);
        }
        // >> END
        Hashtable objectives = (Hashtable)aiccData.get("objectives_status");
        Enumeration enumeration = null;
debugLogger.debug("             ID 41");
        if (objectives != null) {
            enumeration = objectives.keys();
debugLogger.debug("             ID 42");
        }
debugLogger.debug("             ID 43");
        if (enumeration != null) {
debugLogger.debug("             ID 44");
            stmt1 = con.prepareStatement("INSERT INTO Accomplishment (apm_ent_id, apm_obj_id, apm_score, apm_max_score, apm_min_score, apm_aicc_score, apm_status, apm_tkh_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            boolean with_record = false;
            while (enumeration.hasMoreElements()) {
                String id = (String)enumeration.nextElement();
                Hashtable hash = (Hashtable)objectives.get(id);
                String developer_id = (String)hash.get("dev_id");
                developer_id = developer_id.trim();
                long obj_id = 0;

debugLogger.debug("???,,," + developer_id + ",,,???");
debugLogger.debug(",,," + ((Long)aiccData.get("mod_id")).longValue() + ",,,");

                stmt = con.prepareStatement("SELECT obj_id FROM Objective "+ cwSQL.noLockTable() 
                    + ", ResourceObjective "+ cwSQL.noLockTable() 
                    + " WHERE obj_developer_id = ? AND rob_res_id = ? AND rob_obj_id = obj_id");
                stmt.setString(1, developer_id);
                stmt.setLong(2, ((Long)aiccData.get("mod_id")).longValue());
                rs = stmt.executeQuery();
                if (rs.next()) {
debugLogger.debug("<><><>");
                    obj_id = rs.getLong("obj_id");
                }
                stmt.close();

                if (obj_id == 0) {
                    // special handling for SCORM, add the objective during run time
                    if (aiccData.get("type") != null && aiccData.get("type").equals("scorm") == true) {
                        // insert the objective
                        dbObjective dbObj = new dbObjective();
                        dbObj.obj_type = "AICC";
                        dbObj.obj_desc = developer_id;
                        dbObj.obj_title = developer_id;
                        dbObj.obj_developer_id = developer_id;
                        dbObj.obj_import_xml = null;
                        obj_id = dbObj.insAicc(con, prof);

                        // inssert the resource objective
                        long[] int_mod_id_list = new long[] {((Long) aiccData.get("mod_id")).longValue()};
                        long[] int_obj_id_list = new long[] { obj_id };
                        dbResourceObjective dbResObj = new dbResourceObjective();
                        dbResObj.insResObj(con, int_mod_id_list, int_obj_id_list);

                    }
                    // normal AICC case
                    else {
                        throw new cwException("Failed to retrieve obj_id from Objective.");
                    }
                }

               
                stmt1.setLong(1, ((Long)aiccData.get("ent_id")).longValue());
                stmt1.setLong(2, obj_id);

                if ((Float)hash.get("score") != null) {
                    stmt1.setFloat(3, ((Float)hash.get("score")).floatValue());
                } else {
                    stmt1.setNull(3, java.sql.Types.FLOAT);
                }

                if ((Float)hash.get("max_score") != null) {
                    stmt1.setFloat(4, ((Float)hash.get("max_score")).floatValue());
                } else {
                    stmt1.setNull(4, java.sql.Types.FLOAT);
                }

                if ((Float)hash.get("min_score") != null) {
                    stmt1.setFloat(5, ((Float)hash.get("min_score")).floatValue());
                } else {
                    stmt1.setNull(5, java.sql.Types.FLOAT);
                }

                stmt1.setString(6, (String)hash.get("aicc_score"));
                stmt1.setString(7, (String)hash.get("status"));
                stmt1.setLong(8, tkh_id);
                stmt1.addBatch();
                with_record = true;
            }
            if(with_record){
                stmt1.executeBatch();
            }
            stmt1.close();
        }
debugLogger.debug("             ID 45");
stmt.close();
stmt1.close();
con.commit();
Hashtable coreParam = (Hashtable) aiccData.get("core");
String temp_lesson_status  =(String) coreParam.get("lesson_status");
if (temp_lesson_status == null || temp_lesson_status.length() == 0) {
    temp_lesson_status = "N";
}
CourseCriteria.setAttendFromModule(con, prof, ((Long)aiccData.get("mod_id")).longValue(), ((Long)aiccData.get("cos_id")).longValue(),  ((Long)aiccData.get("ent_id")).longValue(),  tkh_id, temp_lesson_status, dbModEvalHist.mvh_total_time, cur_time, cur_time);

    }

    private int checkAuPwd(Connection con, long mod_id, String pwd)
        throws SQLException, cwException
    {
        int error = 0;

        PreparedStatement stmt = con.prepareStatement("SELECT mod_password FROM Module "+ cwSQL.noLockTable()
                    + " WHERE mod_res_id = ?");
        stmt.setLong(1, mod_id);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String au_pwd = rs.getString("mod_password");

            if (au_pwd != null && au_pwd.length() != 0 && (pwd == null || ! pwd.equalsIgnoreCase(au_pwd))) {
                error = 2;
            }
        } else {
            throw new cwException("Failed to retreive record from Module");
        }

        stmt.close();

        return error;
    }

    private int incrementTotalAttempt(Connection con, loginProfile prof, long ent_id, long cos_id, long mod_id, long tkh_id, String last_mov_status)
        throws SQLException, cwException
    {
        int update = 0;

        Timestamp cur_time = null;
        try {
            cur_time = dbUtils.getTime(con);
        } catch (qdbException e) {
            throw new cwException(e.toString());
        }

        PreparedStatement stmt = con.prepareStatement("SELECT mov_total_attempt FROM ModuleEvaluation "+ cwSQL.noLockTable() 
                    + " WHERE mov_cos_id = ? AND mov_ent_id = ? AND mov_mod_id = ? AND mov_tkh_id = ?");
        stmt.setLong(1, cos_id);
        stmt.setLong(2, ent_id);
        stmt.setLong(3, mod_id);
        stmt.setLong(4, tkh_id);
        ResultSet rs = stmt.executeQuery();
        boolean has_record = false;
        if(rs.next()){
            has_record = true;
        }
        if (has_record) {
            long total_attempt = rs.getLong("mov_total_attempt") + 1;

            stmt = con.prepareStatement("UPDATE ModuleEvaluation "+ cwSQL.rowLockTable()
                    + "  SET mov_total_attempt = ? WHERE mov_cos_id = ? AND mov_ent_id = ? AND mov_mod_id = ? AND mov_tkh_id = ?");
            stmt.setLong(1, total_attempt);
            stmt.setLong(2, cos_id);
            stmt.setLong(3, ent_id);
            stmt.setLong(4, mod_id);
            stmt.setLong(5, tkh_id);

            if (stmt.executeUpdate() != 1) {
                throw new cwException("Failed to increment mov_total_attempt INTO ModuleEvaluation");
            }

            update = 1;
         }

         stmt.close();

        // insert module evaluation history
        dbModuleEvaluationHistory dbModEvalHist = new dbModuleEvaluationHistory();
        dbModEvalHist.mvh_ent_id = ent_id;
        dbModEvalHist.mvh_mod_id = mod_id;
        dbModEvalHist.mvh_last_acc_datetime = cur_time;
        dbModEvalHist.mvh_tkh_id = tkh_id;
        dbModEvalHist.mvh_ele_loc = null;
        dbModEvalHist.mvh_total_time = 0;
        dbModEvalHist.mvh_total_attempt = 1;
        //dbModEvalHist.mvh_status = null;
        dbModEvalHist.mvh_status = last_mov_status;
        dbModEvalHist.mvh_score = null;
        dbModEvalHist.mvh_create_usr_id = prof.usr_id;
        dbModEvalHist.mvh_create_timestamp = cur_time;
        try {
            dbModEvalHist.ins(con);
        } catch (qdbException e) {
            throw new cwException(e.toString());
        }

        return update;
    }

    private boolean doLogout(Connection con, long ent_id, long cos_id, long mod_id, long tkh_id)
        throws SQLException, cwException
    {
        PreparedStatement stmt = con.prepareStatement("SELECT mov_status_flag FROM ModuleEvaluation "+ cwSQL.noLockTable() 
                    + " WHERE mov_cos_id = ? AND mov_ent_id = ? AND mov_mod_id = ? AND mov_tkh_id = ?");
        stmt.setLong(1, cos_id);
        stmt.setLong(2, ent_id);
        stmt.setLong(3, mod_id);
        stmt.setLong(4, tkh_id);
        ResultSet rs = stmt.executeQuery();
        boolean result;

        if (rs.next()) {
            if (rs.getString("mov_status_flag") != null && rs.getString("mov_status_flag").equalsIgnoreCase("l")) {
                result = true;
            } else {
                result = false;
            }
        } else {
            result = false;
//            throw new cwException("Failed to retrieve record from ModuleEvaluation");
        }

        stmt.close();
        return result;
    }

    public static String checkCredit(Connection con, long ent_id, long cos_id, long mod_id, long tkh_id)
        throws SQLException, cwException
    {
        PreparedStatement stmt = con.prepareStatement("SELECT cos_max_normal FROM Course "+ cwSQL.noLockTable()
                    + " WHERE cos_res_id = ?");
        stmt.setLong(1, cos_id);
        ResultSet rs = stmt.executeQuery();
        int max_normal = 0;
        int count = 0;
        boolean include = false;

        if (rs.next()) {
debugLogger.debug("       max_normal 1");
            max_normal = rs.getInt("cos_max_normal");
            stmt.close();
        } else {
debugLogger.debug("       max_normal 2");
            stmt.close();
            return "N";
        }
debugLogger.debug("       max_normal 3");
        if (max_normal == -1) {
debugLogger.debug("       max_normal 4");
            return "C";
        } else if (max_normal > 0) {
            stmt = con.prepareStatement("SELECT mov_mod_id FROM ModuleEvaluation "+ cwSQL.noLockTable()
                    + ", Module "+ cwSQL.noLockTable() 
                    + " WHERE mov_ent_id = ? AND mov_cos_id = ? AND mod_type = ?  AND mov_tkh_id = ? AND mod_res_id = mov_mod_id AND mov_status = 'I' AND mov_credit = 'C'");
            stmt.setLong(1, ent_id);
            stmt.setLong(2, cos_id);
            stmt.setString(3, "AICC_AU");
            stmt.setLong(4, tkh_id);

            rs = stmt.executeQuery();
debugLogger.debug("       max_normal 5");
            while (rs.next()) {
debugLogger.debug("!!! " + rs.getString("mov_mod_id"));
                if (mod_id == rs.getLong("mov_mod_id")) {
                    return "C";
                }

                count++;
            }
        } else {
            return "N";
        }
debugLogger.debug("       max_normal 6 -> max = " + max_normal + "; count = " + count);
        stmt.close();

        if (count >= max_normal) {
debugLogger.debug("       max_normal 7");
            return "N";
        } else {
debugLogger.debug("       max_normal 8");
            return "C";
        }
    }

    private void clearSessTime(HttpSession sess, Long mod_id, String sess_time, Long ent_id) {
debugLogger.debug("Clear time hash");
        String mod_time = mod_id.toString() + sess_time;
        Hashtable time_hash = getSessTime(sess, ent_id);

        if (time_hash == null) {
            time_hash = new Hashtable();
        }

        if (mod_id != null) {
            time_hash.remove(mod_time);
debugLogger.debug("time hash cleared!!!");
            putSessTime(sess, time_hash, ent_id);
        }
    }

    private void clearSessCredit(HttpSession sess, Long mod_id, Long ent_id) {
        Hashtable credit_hash = (Hashtable)getSessCredit(sess, ent_id);
debugLogger.debug("Clear credit hash: " + credit_hash);

        if (credit_hash == null) {
            credit_hash = new Hashtable();
        }

        if (mod_id != null) {
            credit_hash.remove(mod_id);
debugLogger.debug("credit hash cleared: " + credit_hash);
            putSessCredit(sess, credit_hash, ent_id);
        }
    }

    private void clearSessLessonMode(HttpSession sess, Long mod_id, Long ent_id) {
        Hashtable lesson_mode_hash = (Hashtable)getSessCredit(sess, ent_id);
debugLogger.debug("Clear lesson mode hash: " + lesson_mode_hash);

        if (lesson_mode_hash == null) {
            lesson_mode_hash = new Hashtable();
        }

        if (mod_id != null) {
            lesson_mode_hash.remove(mod_id);
debugLogger.debug("credit lesson mode cleared: " + lesson_mode_hash);
            putSessLessonMode(sess, lesson_mode_hash, ent_id);
        }
    }

    private void println(PrintWriter out, String str, String mod_vendor) {
        if (mod_vendor != null && mod_vendor.equalsIgnoreCase("skillsoft")) {
            out.println(str);
        } else {
            out.print(str + ASCII_13 + ASCII_10);
        }
debugLogger.debug(str);
    }

    private void print(PrintWriter out, String str) {
        out.print(str);
debugLogger.debug(str);
    }

    private Hashtable getSessCredit(HttpSession sess, Long ent_id) {
        if (sess != null) {
            return (Hashtable)sess.getAttribute(AICC_CREDIT_HASH);
        } else {
            Hashtable myHash;

            if ((myHash = (Hashtable)sess_hash.get(ent_id)) != null) {
                return (Hashtable)myHash.get(AICC_CREDIT_HASH);
            } else {
                return null;
            }
        }
    }

    private void putSessCredit(HttpSession sess, Hashtable credit_hash, Long ent_id) {
        if (sess != null) {
            sess.setAttribute(AICC_CREDIT_HASH, credit_hash);
        } else {
            Hashtable myHash = (Hashtable)sess_hash.get(ent_id);

            if (myHash != null) {
                myHash.put(AICC_CREDIT_HASH, credit_hash);
            } else {
                myHash = new Hashtable();
                myHash.put(AICC_CREDIT_HASH, credit_hash);
                sess_hash.put(ent_id, myHash);
            }
        }
    }

    private Hashtable getSessTime(HttpSession sess, Long ent_id) {
        if (sess != null) {
            return (Hashtable)sess.getAttribute(AICC_TIME_HASH);
        } else {
            Hashtable myHash;

            if ((myHash = (Hashtable)sess_hash.get(ent_id)) != null) {
                return (Hashtable)myHash.get(AICC_TIME_HASH);
            } else {
                return null;
            }
        }
    }

    private void putSessTime(HttpSession sess, Hashtable time_hash, Long ent_id) {
        if (sess != null) {
            sess.setAttribute(AICC_TIME_HASH, time_hash);
        } else {
            Hashtable myHash = (Hashtable)sess_hash.get(ent_id);

            if (myHash != null) {
                myHash.put(AICC_TIME_HASH, time_hash);
            } else {
                myHash = new Hashtable();
                myHash.put(AICC_TIME_HASH, time_hash);
                sess_hash.put(ent_id, myHash);
            }
        }
    }

    // used for preview course purpose
    private void putSessLessonMode(HttpSession sess, Hashtable lesson_mode_hash, Long ent_id) {
        if (sess != null) {
            sess.setAttribute(LESSON_MODE_HASH, lesson_mode_hash);
        } else {
            Hashtable myHash = (Hashtable)sess_hash.get(ent_id);

            if (myHash != null) {
                myHash.put(LESSON_MODE_HASH, lesson_mode_hash);
            } else {
                myHash = new Hashtable();
                myHash.put(LESSON_MODE_HASH, lesson_mode_hash);
                sess_hash.put(ent_id, myHash);
            }
        }
    }

    private Hashtable getSessLessonMode(HttpSession sess, Long ent_id) {
        if (sess != null) {
            return (Hashtable)sess.getAttribute(LESSON_MODE_HASH);
        } else {
            Hashtable myHash;

            if ((myHash = (Hashtable)sess_hash.get(ent_id)) != null) {
                return (Hashtable)myHash.get(LESSON_MODE_HASH);
            } else {
                return null;
            }
        }
    }


    private float roundToDecimal(float value, int decimalPlace) {
        String strValue = Float.toString(value);
        float returnValue = 0;
        if (strValue.indexOf('.') != -1) {
            int decimalIndex = strValue.indexOf('.');
            if (strValue.length() - decimalIndex - 1 <= decimalPlace) {
                returnValue = value;
            }
            else {
                String strRoundedValue = strValue.substring(0, decimalIndex+decimalPlace);
                if (strValue.charAt(decimalIndex+decimalPlace+1) <= '4') {
                    strRoundedValue = strRoundedValue + strValue.substring(decimalIndex+decimalPlace, decimalIndex+decimalPlace+1);
                }
                else {
                    strRoundedValue = strRoundedValue + Integer.toString(Integer.parseInt(strValue.substring(decimalIndex+decimalPlace, decimalIndex+decimalPlace+1)) + 1);
                }

                returnValue = Float.parseFloat(strRoundedValue);
            }
        }
        else {
            returnValue = value;
        }

        return returnValue;
    }
    
/*    *//**
     * initalize the scorm data manager for scorm2004
     * @param ent_id
     * @param cos_id
     * @param mod_id
     * @param tkh_id
     *//*
    private void initSco2004Dm(String sess_id) {
    	debugLogger.info("CMI Servlet Processing 'init' request");

        // create response object to return
    	scoApiResp = new CMIResponse();


//        SeqActivityTree mSeqActivityTree = new SeqActivityTree();
        // location of the stored data store, assuming there is one

//        RTEFileHandler fileHandler = new RTEFileHandler();
//        fileHandler.initializeStateFile(numAttempt, userID, userName, courseID, scoID, scoID);
        SCODataManager scoData = new SCODataManager();
        //  Add a SCORM 2004 Data Model
        scoData.addDM(DMFactory.DM_SCORM_2004);
//        out.writeObject(scoApiResp);
        debugLogger.info("init processed init");
    }
    */
    /**
     * Read {@link CMIRequest} from request object. 
     * @param req
     * @return
     * @throws IOException
     */
    public CMIRequest readObject(HttpServletRequest req) throws IOException {
    	ObjectInputStream oi = new ObjectInputStream(req.getInputStream());
    	CMIRequest request = null;
		try {
			request = (CMIRequest) oi.readObject();
			if (request.mRequestType == CMIRequest.TYPE_INIT){
				request.mActivityData = new SCODataManager();
				request.mActivityData.addDM(DMFactory.DM_SCORM_2004);
				setValue2ScoDM("cmi.session_time", "PT0S", request.mActivityData);
				setValue2ScoDM("cmi.score.raw", "0", request.mActivityData);
				setValue2ScoDM("cmi.score.max", "0", request.mActivityData);
				setValue2ScoDM("cmi.score.min", "0", request.mActivityData);
			}
			
		} catch (ClassNotFoundException e) {
			CommonLog.error(e.getMessage(),e);
		}
    	return request;
    }
    
    /**
     * Get dot-notation data model value from CMIRequest.
     * @param ireq dot-notation data model
     * @param scoApiReq CMIRequest object.
     * @return
     */
    public String getDmValue(String ireq, CMIRequest scoApiReq) {
    	DMProcessingInfo pi = new DMProcessingInfo();
    	DMInterface.processGetValue(ireq, true, true, scoApiReq.mActivityData, pi);
    	return pi.mValue != null ? pi.mValue : "";
    }
    
    /**
     * 
     * @return true if the request type is init or get for scorm2004. 
     */
    public boolean isGetOrInitReq(boolean isScorm2004, CMIRequest scoApiReq) {
    	return isScorm2004 && scoApiReq != null && (scoApiReq.mRequestType == CMIRequest.TYPE_INIT || scoApiReq.mRequestType == CMIRequest.TYPE_GET);
    }
    
    /**
     * Get full status specified in SCORM2004 API from mov_status.
     * @param status
     * @return
     */
    public String getFullStatus(String status) {
    	if ("C".equalsIgnoreCase(status)) {
    		return "completed";
    	} else if ("I".equalsIgnoreCase(status)) {
    		return "incompleted";
    	} else if ("N".equalsIgnoreCase(status)) {
    		return "not attempted";
    	} else if ("P".equalsIgnoreCase(status)) {
    		return "passed";
    	} else if ("F".equalsIgnoreCase(status)) {
    		return "failed";
    	} else {
    		return "unknown";
    	}
    }
    
    public void SendScorm2004Response(HttpServletResponse response, CMIRequest scoApiReq, int error) throws IOException {
    	CMIResponse resp = new CMIResponse();
    	resp.mError = "OK";
    	resp.mActivityData = scoApiReq.mActivityData;
		ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream());
		oos.writeObject(resp);
		oos.close();
    }
    
    public void setValue2ScoDM(String name, String value, SCODataManager scodata) {
    	int e = DMInterface.processSetValue(name, value, false, scodata);
    	CommonLog.debug("name:" + name + "|| value :" + value);
    	CommonLog.info("return code :" + e);
    }
}

