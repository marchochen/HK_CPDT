package com.cw.wizbank.netg;

import java.io.*;

import javax.servlet.http.*;

import java.sql.*;
import java.util.*;
import java.net.*;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.*;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.course.CourseCriteria;
import com.cwn.wizbank.utils.CommonLog;

public class NETgTrackingModule extends ServletModule
{

static Hashtable prevCookieList = null;    
    
    public NETgTrackingModule() {;}
    
    public void process() throws IOException, cwException, qdbErrMessage {
        //System.out.println("IN NETgTrackingModule");
        if (prof ==null)
        	CommonLog.info("login profile is null.");
        else 
        	CommonLog.info("loginProfile  > usr_id :" + prof.usr_id);
        // get output stream for normal content to client
        PrintWriter out = response.getWriter();
        
        NETgTrackingReqParam urlp = null;
        urlp = new NETgTrackingReqParam(bMultipart, request, multi, clientEnc, static_env.ENCODING);
        urlp.netg_tracking_info();

        // service processing starts here
        try {
            if (prof == null && urlp.cmd != null && !urlp.cmd.toUpperCase().startsWith("EXIT_COOKIE_COURSE")) {
                response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
            }
            else if (urlp.cmd == null) {
                throw new cwException("invalid command");

            }
            else if (urlp.cmd.toUpperCase().startsWith("LAUNCH_COOKIE_COURSE")) {
                if(urlp.cos_id > 0) {
	            	if (urlp.tkh_id <=0) {
	                    urlp.tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, urlp.cos_id, prof.usr_ent_id);
	                }
	    			if (urlp.tkh_id != 0 && urlp.tkh_id != DbTrackingHistory.TKH_ID_NOT_FOUND && DbTrackingHistory.getAppTrackingIDByCos(con, urlp.tkh_id, prof.usr_ent_id, urlp.cos_id, urlp.mod_id) != 1){
	    				msgBox(MSG_ERROR, new cwSysMessage("USR033"), urlp.url_failure, out);
	                    return;
	                }
                }
    			
                String domain = null;
                String home = null;
                String path = null;
                String contentServerName = null;
                
                boolean useIpSever = false;
                String  servername = request.getServerName();
                String serverIp = InetAddress.getByName(servername).getHostAddress();
                if(servername.equalsIgnoreCase(serverIp)){
                	useIpSever = true;
                }
                
                if (request.getServerName().indexOf('.') != -1 && !useIpSever) {
                    domain = request.getServerName().substring(request.getServerName().indexOf('.'));
                }
                
                
             
                //home = "http://" + request.getServerName() + ":" + request.getServerPort() + "/servlet/Dispatcher?env=wizb&module=netg.NETgTrackingModule&cmd=exit_cookie_course&mod_id=" + Long.toString(urlp.mod_id) + "&cos_id=" + Long.toString(urlp.cos_id);
                home = URLDecoder.decode(urlp.home);
                
                NETgTracking myNETgTrack = new NETgTracking();
                Cookie myCookie = myNETgTrack.getCookie(con, urlp.cos_id, urlp.mod_id, prof.usr_ent_id, domain, home, urlp.tkh_id, wizbini, prof);
                
                URL urlContent = new URL(urlp.cos_url);
                path = urlContent.getFile();
                for (int i=path.length()-1; i>=0; i--) {
                    if (path.charAt(i) == '/') {
                        path = path.substring(0, i);
                        break;
                    }
                }

                myCookie.setPath(path);
CommonLog.info("~~~~~~~~~~~~path:" + path);
                myCookie.setMaxAge(-1);
                
                response.addCookie(myCookie);
                response.sendRedirect(urlp.cos_url);                
            }            
            else if (urlp.cmd.toUpperCase().startsWith("EXIT_COOKIE_COURSE")) {

                NETgTracking myNETgTrack = new NETgTracking();
                String cookieName = myNETgTrack.getCookieName(con, urlp.mod_id);
                boolean boolSetCookie = false;
                StringBuffer contentBuf = new StringBuffer();

                if(urlp.usr_ent_id <= 0) {
                	CommonLog.info("Failed to get usr_ent_id from EXIT_COOKIE_COURSE");
                    response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
                }
                loginProfile myProf = new loginProfile();
                dbRegUser user = new dbRegUser();
                user.usr_ent_id = urlp.usr_ent_id;
                user.get(con);
                myProf.usr_ent_id = urlp.usr_ent_id;
                myProf.root_ent_id = user.usr_ste_ent_id;
                myProf.usr_id = user.usr_id;
                myProf.usr_ste_usr_id = user.usr_ste_usr_id;
                CommonLog.info("Netg EXIT_COOKIE_COURSE: use usr_ent_id from input parameter");
                CommonLog.info("usr_ent_id: " + urlp.usr_ent_id);
                CommonLog.info("root_ent_id: " + myProf.root_ent_id);
                CommonLog.info("usr_id: " + myProf.usr_id);
                CommonLog.info("usr_ste_usr_id: " + myProf.usr_ste_usr_id);

                if (urlp.tkh_id <=0) {
                    urlp.tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con, urlp.cos_id, myProf.usr_ent_id);
                }
    			if (urlp.tkh_id != 0 && urlp.tkh_id != DbTrackingHistory.TKH_ID_NOT_FOUND && DbTrackingHistory.getAppTrackingIDByCos(con, urlp.tkh_id, myProf.usr_ent_id, urlp.cos_id, urlp.mod_id) != 1){
    				msgBox(MSG_ERROR, new cwSysMessage("USR033"), urlp.url_failure, out);
                    return;
                }
                contentBuf.append("[NETg Request: cos_res_id:").append(urlp.cos_id)
                          .append(";mod_res_id:").append(urlp.mod_id)
                          .append(";usr_ent_id:").append(myProf.usr_ent_id)
                          .append(";tkh_id:").append(urlp.tkh_id)
                          .append("]");
                     
                dbModule dbMod = new dbModule();
                dbMod.mod_res_id = urlp.mod_id;
                dbMod.get(con);          
                int pass_score = new Float(dbMod.mod_pass_score).intValue();
                
                try {
                    Cookie[] myCookieList = request.getCookies();
                    prevCookieList = new Hashtable();
                    for (int i=0; i<myCookieList.length; i++) {
                        // only the first cookie is the valid cookie
                        if (myCookieList[i].getName().equalsIgnoreCase(cookieName) == true) {                        
                            contentBuf.append("  ").append(URLDecoder.decode(myCookieList[i].getValue()));
                            StringBuffer status = new StringBuffer();
                            //myNETgTrack.processCookie(con, urlp.cos_id, urlp.mod_id, myProf.usr_ent_id, URLDecoder.decode(myCookieList[i].getValue()), static_env.NETG_PASS_SCORE, urlp.tkh_id, status);
                            int duration = myNETgTrack.processCookie(con, urlp.cos_id, urlp.mod_id, myProf.usr_ent_id, URLDecoder.decode(myCookieList[i].getValue()), pass_score, urlp.tkh_id, status);
                            CourseCriteria.setAttendFromModule( con,  myProf, urlp.mod_id,  urlp.cos_id,  myProf.usr_ent_id,  urlp.tkh_id,  status.toString(),duration, cwSQL.getTime(con), cwSQL.getTime(con));
                            boolSetCookie = true;
                            break;
                        }
                    }
                } finally {
                    if (boolSetCookie) {
                        contentBuf.append(" SUCCESS").append(cwUtils.NEWL);
                        con.commit();
                    } else {
                        contentBuf.append(" FAILED").append(cwUtils.NEWL);
                    }
                    writeLog(contentBuf.toString());
                }
                
                if(!boolSetCookie) {
                    throw new cwSysMessage("PGR009","Unable to find cookie from request: cookie name="+cookieName);
                }
                
                Cookie myCookie = new Cookie(cookieName, "no-data");
                myCookie.setPath("/");
                myCookie.setMaxAge(0);
                response.addCookie(myCookie);
                response.sendRedirect(urlp.url_success);
            }
            else {
                // do nothing
            }
        } catch (cwSysMessage e) {
             try {
                 con.rollback();
                 writeLog(e.getSystemMessage("ISO-8859-1") + cwUtils.NEWL);
                 msgBox(ServletModule.MSG_ERROR, e, urlp.url_failure, out);
             } catch (cwException ce) {
                out.println("Server error: " + ce.getMessage());
             } catch (SQLException se) {
                out.println("SQL error: " + se.getMessage());
             }
        } catch (SQLException e) {
             try {
                 con.rollback();
                 writeLog(e.getMessage() + cwUtils.NEWL);
                 msgBox(ServletModule.MSG_ERROR, new cwSysMessage("PGR009",e.getMessage()), urlp.url_failure, out);
             } catch (cwException ce) {
                out.println("Server error: " + ce.getMessage());
             } catch (SQLException se) {
                out.println("SQL error: " + se.getMessage());
             }
        } catch (cwException e) {
             try {
                 con.rollback();
                 writeLog(e.getMessage() + cwUtils.NEWL);
                 msgBox(ServletModule.MSG_ERROR, new cwSysMessage("PGR009",e.getMessage()), urlp.url_failure, out);
             } catch (cwException ce) {
                out.println("Server error: " + ce.getMessage());
             } catch (SQLException se) {
                out.println("SQL error: " + se.getMessage());
             }
        } catch (qdbException e) {
             try {
                 con.rollback();
                 writeLog(e.getMessage() + cwUtils.NEWL);
                 msgBox(ServletModule.MSG_ERROR, new cwSysMessage("PGR009",e.getMessage()), urlp.url_failure, out);
             } catch (cwException ce) {
                out.println("Server error: " + ce.getMessage());
             } catch (SQLException se) {
                out.println("SQL error: " + se.getMessage());
             }
        }
    }

    public void writeLog(String content) throws IOException, SQLException {
        
        if(wizbini.cfgSysSetupadv.getNetgLog().isLogEnabled()) {
            Timestamp curTime = cwSQL.getTime(con);
            String logFolderStr = wizbini.getWebDocRoot() 
                                + cwUtils.SLASH 
                                + wizbini.cfgSysSetupadv.getLogDir().getName();
            String logFileName = wizbini.cfgSysSetupadv.getNetgLog().getLogFile();

            FileWriter fw = null;
            try {
                File logFolder = new File(logFolderStr);
                if( !logFolder.exists() ) {
                    logFolder.mkdir();
                }
                File logFile = new File(logFolder, logFileName);
                fw = new FileWriter(logFile.toString() , true);
                fw.write(curTime + "   " + content);
                fw.flush();
                fw.close();
            } finally {
                if(fw!=null) {fw.close();}
            }
        }
    }

}