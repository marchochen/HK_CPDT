package com.cw.wizbank.login;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Vector;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcXslQuestion;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.qdb.SessionListener;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.dbProgressAttempt;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.supervise.Supervisor;
import com.cw.wizbank.util.HttpRequestDeviceUtils;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.entity.LoginLog;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.ContextPath;
import com.cwn.wizbank.utils.CookieUtil;

import com.cwn.wizbank.utils.CwnUtil;


public class LoginModule extends ServletModule {

	@SuppressWarnings("rawtypes")
	public void process() throws SQLException, cwException, IOException {
		PrintWriter out = response.getWriter();
		String method = request.getMethod();
		LoginReqParam urlp = new LoginReqParam(request, clientEnc, static_env.ENCODING);
		HttpSession sess = request.getSession(true);
		if(sess!=null){//让cookie过期

			sess.invalidate();

			Cookie[] cookieList = request.getCookies(); //获取cookie
			if(null != cookieList && cookieList.length > 0){
				for(Cookie cookie : cookieList){
					cookie.setMaxAge(0);//让cookie过期
				}
			}
			
	       /* Cookie cookie = request.getCookies()[0];//获取cookie

	        cookie.setMaxAge(0);//让cookie过期*/

		}
		sess = request.getSession(true);
		
	    String encoding = wizbini.cfgSysSetupadv.getEncoding();
        xslQuestions = AcXslQuestion.getQuestions();
		try {
			ContextPath.setContextPath(request.getContextPath());
			sess.setAttribute("globalPageSize", new com.cwn.wizbank.utils.Page().getPageSize());
			if (urlp.cmd == null) {
				throw new cwException("Invalid Command");
			} else if (urlp.cmd.equalsIgnoreCase("auth") ||           //Manual Login
						urlp.cmd.equalsIgnoreCase("guest_login") ||   //Guest Account Login
						urlp.cmd.equalsIgnoreCase("ad_login") ||   //ad Login
						urlp.cmd.equalsIgnoreCase("aff_auth")) {      //Single SignOn Login
				urlp.getLoginInfo();
                prof = new loginProfile();
                acSite site = new acSite();
                site.ste_ent_id = urlp.site_id;
                String code = "";
                if(dbRegUser.checkSiteUsrIdCount(con,urlp.usr_ste_usr_id, 1)){
                	code = dbRegUser.CODE_USER_SYSTEM_ISSUE;
            		CookieUtil.addCookie(response, dbRegUser.LOGIN_LAN, urlp.login_lan, dbRegUser.LOGIN_LAN_AGE);
                } else if (urlp.cmd.equalsIgnoreCase("auth")){
                    code = dbRegUser.auth_login(con, request, response, prof, urlp.usr_ste_usr_id, urlp.usr_pwd, urlp.login_role, site, true, wizbini, urlp.label_lan, request.getServerName(), urlp.login_lan);
                } if (urlp.cmd.equalsIgnoreCase("ad_login")){
                    code = dbRegUser.ad_login(con, prof, urlp.usr_ste_usr_id, urlp.usr_pwd, urlp.login_role, site, true, wizbini, urlp.label_lan);
                }else if(urlp.cmd.equalsIgnoreCase("guest_login")) {
                    code = dbRegUser.guest_login(con, prof, site, true, wizbini);
                } else if(urlp.cmd.equalsIgnoreCase("aff_auth")){
                	urlp.getAffLoginUsrInfo();
                    // parms : usr_id
                    String remoteHost = request.getRemoteHost();
                    String referer  = request.getHeader("REFERER");
                    urlp.usr.usr_ste_usr_id = urlp.usr_ste_usr_id;
                    urlp.usr.usr_id = urlp.usr_ste_usr_id;
                    urlp.usr.login_role = urlp.login_role;
                    code = urlp.usr.aff_login(con, prof, site, remoteHost, referer, urlp.create_new, true, urlp.mode, wizbini);
                    // set current login is Single SignOn Login
                    prof.sso_login = true;
                }
                con.commit();
                String key = request.getParameter("user-checked");
                // 记住user id，存入cookie
                if("1".equals(key) && "LGS01".equals(code)) {
                	CookieUtil.addCookie(response, qdbAction.REMEMBER_ME_USER_ID, urlp.usr_ste_usr_id, qdbAction.COOKIE_MAX_AGE);
                }
                sess.setAttribute("code", code);
                String userID = request.getParameter("userID");
                String type = request.getParameter("type");
                String appID = request.getParameter("appID");
                String action_ent_id = request.getParameter("action_ent_id");
                String acc_ent_id = request.getParameter("acc_ent_id");
                
                if(prof!= null && prof.usr_id!=null && prof.usr_id.length() > 0 ) {
                	Supervisor sup = new Supervisor(con, prof.usr_ent_id);
                    if (sup.hasStaff(con)) {
                        prof.hasStaff = true;
                    } else {
                        prof.hasStaff = false;
                    }
                	prof.isMobileDeviceClien = HttpRequestDeviceUtils.isMobileDevice( request) ;
                	if(!(prof.usr_status != null && prof.usr_status.equals(dbRegUser.USR_STATUS_SYS))){
	                	SessionListener sessionListener=new SessionListener(wizbini, prof, request.getRemoteAddr(), sess.getId(),null);  
		                sess.setAttribute("listener", sessionListener);
                	}
                	prof.skin_root = wizbini.cfgSysSetupadv.getSkinHome();
                    prof.xsl_root = wizbini.cfgSysSetupadv.getSkinHome() + dbUtils.SLASH + prof.current_role_skin_root + dbUtils.SLASH + wizbini.cfgSysSetup.getXslStylesheet().getHome();
                    prof.encoding  = encoding;
                    sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);
                    sess.setAttribute(qdbAction.SESS_ACL_CONTROL_XSL_QUES, xslQuestions);
                    String SID = prof.usr_id + Math.round(Math.random() * 100000) ;
                    sess.setAttribute(qdbAction.AUTH_LOGIN_SID, SID);
                    prof.setIp(CwnUtil.getIpAddr(request));//获取客户端IP
                    prof.writeSession(sess);
                    if(wizbini.cfgSysSetupadv.getSecurityFileServlet().isEnabled() && AccessControlWZB.isLrnRole(prof.current_role)) {
                    	CommonLog.info("load security file servlet");                                                  
                        // if student , create a res id permission list
                        // 1. Get all module that the student can access
                        Vector modIdVec = new Vector();
                        Vector assIdVec = new Vector();
                        Vector easIdVec = new Vector();  
                        dbCourse.getAccessibleModIds(con, prof, modIdVec, assIdVec, easIdVec);

                        // 2. Get all the question id that the student attempted before
                        Vector queIdVec = dbProgressAttempt.getAttemptedResIds(con, prof.usr_id);

                        if (modIdVec != null) {
                        	sess.setAttribute(qdbAction.AUTH_MOD_IDS, modIdVec);
                        }
                        if (assIdVec != null) {
                        	sess.setAttribute(qdbAction.AUTH_ASS_IDS, assIdVec);
                        }
                        if (easIdVec != null) {
                        	sess.setAttribute(qdbAction.AUTH_EAS_IDS, easIdVec);
                        }
                        if (queIdVec != null) {
                        	sess.setAttribute(qdbAction.AUTH_QUE_IDS, queIdVec);
                        }
                    }

                    // do bypass_pwd_change here to improve performance
                    boolean emailUrl = false;
                    if(urlp.courseID > 0){
                        if(userID != null && userID.length() > 0){
                            emailUrl = true;
                        }
                    }
					if (prof.bNeedToChangePwd) {
					    if(emailUrl){
                            urlp.url_success = urlp.url_change_pwd+ prof.usr_ent_id + "?courseID=" + urlp.courseID + "&userID=" + userID + "&appID=" + appID + "&action_ent_id=" + action_ent_id + "&acc_ent_id=" + acc_ent_id + "&type=" + type;
                        }else{
                            urlp.url_success = urlp.url_change_pwd;
                        }
					}
                    else if(urlp.courseID > 0){
                        if(emailUrl){
                            dbRegUser user = new dbRegUser();
                            user.usr_ent_id = prof.usr_ent_id;
                            if("enroll".equals(type)){
                                urlp.url_success = "/app/course/detail/"+ urlp.courseID;
                            }else{
                                boolean direct_approval = dbRegUser.isDirectApproval(con,prof,urlp.courseID,userID,appID,action_ent_id,type,wizbini,sess);
                                if(direct_approval){
                                    urlp.url_success = "/app/subordinate/subordinateApproval";
                                    user.changeRole(con, "NLRN_1", prof, sess, wizbini);
                                }else{
                                    urlp.url_success = "/servlet/aeAction?env=wizb&cmd=ae_process_appn&app_id="+appID+"&tvw_id=APPLY_VIEW&app_tvw_id=DETAIL_VIEW&frmAppr=false&stylesheet=enrol_approval.xsl";
                                    user.changeRole(con, "TADM_1", prof, sess, wizbini);
                                }
                            }
                        }
                    }
					// Default : using the url success > rol_home_url > home.xsl
					else if (urlp.url_success == null || urlp.url_success.length() == 0) {
						urlp.url_success = prof.goHome(request);
					}
					
                    if (urlp.cmd.equalsIgnoreCase("auth") || urlp.cmd.equalsIgnoreCase("guest_login")){
                        if(urlp.url_success != null && urlp.url_success.length() > 0) {
                        	// 登陆成功跳转至成功页面
                            response.sendRedirect(urlp.url_success);
                            //记录登录日志  成功
                            LoginLog loginLog = new LoginLog((int)prof.getUsr_ent_id(),prof.usr_ste_usr_id,prof.usr_display_bil,LoginLog.MODE_PC,request.getRemoteAddr(),prof.login_date,LoginLog.USR_LOGIN_STATUS_SUCCESS,null);
                            SystemLogContext.saveLog(loginLog, SystemLogTypeEnum.LOGIN_ACTION_LOG);
                        } else {
                            String stylesheet = request.getParameter("stylesheet");
                            if(stylesheet == null || stylesheet.length() == 0) {
                                stylesheet = "home.xsl";
                            }
                            // 获取页面需要的XML
                            String result = loginProfile.getHomeXML(con, sess, prof, stylesheet, wizbini.cfgTcEnabled);
                            // 格式化该XML
                            result = formatXML(result, null, qdbAction.HOME_PAGE_TAG);
                            // 生成相关的HTML
                            generalAsHtml(result, out, stylesheet);
                        }
                    }else if(urlp.cmd.equalsIgnoreCase("aff_auth")){
                        // redirect a url that don't contain session id.
                        if (urlp.url_success.indexOf("?") > 0 ){
                            if (urlp.url_success.endsWith("?")){
                            	urlp.url_success += "wzbcode=" + code;
                            }else{
                            	urlp.url_success += "&wzbcode=" + code;
                            }
                        }else{
                        	urlp.url_success += "?wzbcode=" + code;
                        }
                        response.sendRedirect(urlp.url_success);
                    }
                } else {
                	//记录登录日志   失败
                	Timestamp curTime = cwSQL.getTime(con);
                    LoginLog loginLog = new LoginLog((int)prof.getUsr_ent_id(),urlp.usr_ste_usr_id,prof.usr_display_bil,LoginLog.MODE_PC,request.getRemoteAddr(),curTime,LoginLog.USR_LOGIN_STATUS_FAIL,code);
                    SystemLogContext.saveLog(loginLog, SystemLogTypeEnum.LOGIN_ACTION_LOG);
                	
                    if(code != null && !dbRegUser.CODE_LOGIN_SUCCESS.equals(code)){
                      	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                      }
                	 if(sess != null) {
                     	sess.invalidate();
                     }
                	
                	if (urlp.url_failure == null || urlp.url_failure.length() == 0) {
 						urlp.url_failure = wizbini.cfgSysSetupadv.getLogin().getLoginFailureUrl();
 					}
 					if (urlp.url_failure.indexOf("?") > 0) {
 						if (urlp.url_failure.endsWith("?")) {
 							urlp.url_failure += "wzbcode=" + code;
 						} else {
 							urlp.url_failure += "&wzbcode=" + code;
 						}
 					} 
 					else if(urlp.url_failure.indexOf("$") > 0) {
 						urlp.url_failure += code;
 					}else{
 						urlp.url_failure += "?wzbcode=" + code;
 					}
                	if(code.equals(dbRegUser.CODE_MAX_LOGIN_USER_EXCEED_LIMIT)) {
                        cwSysMessage e = new cwSysMessage(code);
                        prof.xsl_root = wizbini.cfgSysSetupadv.getSkinHome() + dbUtils.SLASH + prof.current_role_skin_root + dbUtils.SLASH + wizbini.cfgSysSetup.getXslStylesheet().getHome();
                        prof.encoding  = wizbini.cfgSysSetupadv.getEncoding();
                        prof.skin_root = wizbini.cfgSysSetupadv.getSkinHome();
                        
                        msgBox(MSG_ERROR, e, urlp.url_failure, out);
                   
                        if(sess != null && request.getSession(false)!=null) {
                        	sess.invalidate();
                        }
                       
                        return;
                	}
                	if(urlp.courseID > 0){
                        if(userID != null && userID.length() > 0){
                            urlp.url_failure += "?course=" + urlp.courseID + "&userID=" + userID + "&appID=" + appID + "&action_ent_id=" + action_ent_id + "&type=" + type;
                        }   
                    }
  					 if(!acSite.getOpenRedirect(wizbini.cfgSysSetupadv.getOpenRedirectWhiteList().getRedirect(),urlp.url_failure)){
  						 	response.sendRedirect(urlp.url_failure);
  		                }else{
  		                	out.print(acSite.DOMAIN_FORBIDDEN);
  		                }
                }
                return;
            } 
		} catch (qdbException qdbe) {
			con.rollback();
			CommonLog.error(qdbe.getMessage(),qdbe);
		} catch (cwSysMessage cwe) {
			con.rollback();
			CommonLog.error(cwe.getMessage(),cwe);
		} catch (qdbErrMessage em) {
			con.rollback();
			CommonLog.error(em.getMessage(),em);
		}
	}
	
}
