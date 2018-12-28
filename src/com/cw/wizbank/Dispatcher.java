package com.cw.wizbank;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;




// Utils classes
import com.oreilly.servlet.*;
import com.cw.wizbank.JsonMod.tcrTemplate.TcrTemplateLogic;
import com.cw.wizbank.accesscontrol.AcXslQuestion;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.trunkinterface.DeveloperConfig;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.exception.UploadException;
import com.cwn.wizbank.utils.CommonLog;

public class Dispatcher extends HttpServlet
{
    // static env object for all requests
    private static qdbEnv static_env = null;
    private static final String WB_PACKAGE = "com.cw.wizbank.";
    private final String init_tcr_module_path = "init_tcr_module.xml";
    public static String siteType = null;//will be init in acSite.initSteType
    public static Hashtable bereavedFunc = null;//will be init in acSite.initSteType
    // using xml in place of wizb.ini for system parameters
    private static WizbiniLoader wizbini = null;

    public static Logger creditsLogger = null;

    public static DeveloperConfig developer = null;

    public static WizbiniLoader getWizbini() {
    	return wizbini;
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Connection con = null;
        try {
        	CommonLog.info("Dispatcher.init() START...");
            wizbini = WizbiniLoader.getInstance(config);

            // initialize qdbEnv
            //String env = config.getInitParameter("env");
			static_env = (qdbEnv) config.getServletContext().getAttribute(WizbiniLoader.SCXT_STATIC_ENV);
			if( static_env == null ) {
	            static_env = new qdbEnv();
    	        static_env.init(wizbini);
    	        config.getServletContext().setAttribute(WizbiniLoader.SCXT_STATIC_ENV, static_env);
			}
			/*
			String creditsLogdir = wizbini.getAppnRoot() + dbUtils.SLASH + wizbini.cfgSysSetupadv.getLogDir().getName() + dbUtils.SLASH
								 + "credits" + dbUtils.SLASH;
            File dir = new File(creditsLogdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            System.setProperty("credits_log_path", creditsLogdir);
            */
            System.setProperty("file_encoding", wizbini.cfgSysSetupadv.getEncoding());
			creditsLogger = Logger.getLogger(this.getClass().getName() + ".creditslog.log");
			PropertyConfigurator.configure(wizbini.getCfgFileLog4jDir());
			cwSQL sqlCon = new cwSQL();
            sqlCon.setParam(wizbini);
            con = sqlCon.openDB(false);
            
            // 获取数据库连接后，初始Application读取数据库配置信息
            Application.init(con);

            acSite.initSteType(con);//init the siteType & bereavedFunc;

			developer = new DeveloperConfig();
			developer.init(con, wizbini.getCfgFileDeveloperDir());
            
            /*
             * init tcr module
             * Author:wrren
             */
            String tcr_module_config_path = wizbini.getWebConfigRoot() + cwUtils.SLASH + init_tcr_module_path;
            TcrTemplateLogic.getInstance().init_all(con, tcr_module_config_path);


            CommonLog.info("Dispatcher.init() END...");
        } catch (Exception e) {
			try {
				if (con != null && !con.isClosed()) {
					con.rollback();
					con.close();
				}
			} catch (SQLException e1) {
				throw new ServletException(e1);
			}
			CommonLog.error("init() exception :" + e.getMessage(),e);
			throw new ServletException(e.getMessage());
		}
        try {
			if (con != null && !con.isClosed()) {
				con.commit();
				con.close();
			}
		} catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession sess = request.getSession(false);

        // If Websphere , get the client character encoding
        // Otherwise, use the default encoding
        // The client encoding is used in the converting unicode function
        String clientEnc = cwUtils.getCharacterEncoding(getServletContext(), request.getCharacterEncoding());

        // common http headers
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "Tue, 20 Aug 1996 00:00:00 GMT");

        // get output stream for normal content to client
        response.setContentType("text/html; charset=" + wizbini.cfgSysSetupadv.getEncoding());
        PrintWriter out = response.getWriter();

        Connection con = null;
        loginProfile prof = null;
        Hashtable xslQuestions = null;

        // get the database connection for this request
        try {
            cwSQL sqlCon = new cwSQL();
            sqlCon.setParam(wizbini);
            con = sqlCon.openDB(false);
        } catch (Exception e) {
            out.println("<b><h3> Sorry, the server is too busy.</h3></b>");
            return;
        }

        if (sess !=null) {
            //prof = (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);

            //ClassCastException will be thrown if a new class loader is used
            //e.g. when a JSP is first loaded, a new class loader will be used
            //so use a wrapper class to read the loginProfile and put it back to session
            try {
                prof = (loginProfile)sess.getAttribute(qdbAction.AUTH_LOGIN_PROFILE);
                xslQuestions = (Hashtable)sess.getAttribute(qdbAction.SESS_ACL_CONTROL_XSL_QUES);
            }
            catch(ClassCastException e) {
                prof = new loginProfile();
                prof.readSession(sess);
                sess.setAttribute(qdbAction.AUTH_LOGIN_PROFILE, prof);

                xslQuestions = new Hashtable();
                xslQuestions = AcXslQuestion.getQuestions();
                sess.setAttribute(qdbAction.SESS_ACL_CONTROL_XSL_QUES, xslQuestions);
            }
        }

        ServletModule mm = null;
        MultipartRequest multi = null;
        String tmpUploadPath = new String();
        boolean bMultiPart = false;

// for debugging
long my_time = 0;
int my_id = 0;
String my_action = null;
String mod = null;
String module =null;

        // service processing starts here
        try {

            try {
                String conType = request.getContentType();

                if( conType != null && conType.toLowerCase().startsWith("multipart/form-data") ) {
                    bMultiPart = true;
                }

                // try to use utility class
                if( bMultiPart)
                {
                    try {
                        java.util.Date ts = new java.util.Date();
                        SimpleDateFormat fm = new SimpleDateFormat("SSSHHmmss");

                        tmpUploadPath = wizbini.getFileUploadTmpDirAbs() + cwUtils.SLASH + fm.format(ts);

                        File tmpUploadDir = new File(tmpUploadPath);
                        boolean bOk = tmpUploadDir.mkdirs();
                        if (!bOk)
                            throw new IOException ("Fails to create temp dir: " + tmpUploadDir);

                        int maxSize = (int) ((wizbini.cfgSysSetupadv.getFileUpload().getMaxUploadSize() + 0.1 ) * 1024 * 1024);
                        CommonLog.info("maxSize: " + maxSize);

                        if (request.getContentLength() > maxSize){

							request.getInputStream().skip(request.getContentLength());
							request.getInputStream().close();
							//special process for upload files using wb_upload_utils.js
							if (request.getParameter("upload_listener") != null) {
							    UploadListener listener = new UploadListener(0);
						        listener.error("GEN007");
						        HttpSession session = request.getSession();
						        session.setAttribute("FILE_UPLOAD_LISTENER", listener);
						        session.setAttribute("FILE_UPLOAD_STATS", listener.getFileUploadStats());
                            }
                            cwSysMessage msg = new cwSysMessage("GEN007", new Integer(static_env.INI_MAX_UPLOAD_SIZE).toString());
                            try{
                                msgBox(request, mm.MSG_ERROR, con, msg, prof, "javascript:history.back()", out);
                                return;
                            } catch (cwException ce) {
                                out.println("MSGBOX Server error: " + cwUtils.esc4Html(ce.getMessage()));
                            } catch (SQLException se) {
                                out.println("MSGBOX SQL error: " +  cwUtils.esc4Html(se.getMessage()));
                            }
                        }else{
                            multi = new MultipartRequest( request, tmpUploadPath, wizbini.cfgSysSetupadv.getEncoding(), maxSize);
                            if(null!=multi.getForBiddenFiles() && !multi.getForBiddenFiles().isEmpty()){
                              	cwSysMessage msg = new cwSysMessage("GEN011", Application.UPLOAD_FORBIDDEN);
                            	msgBox(request, mm.MSG_ERROR, con, msg, prof, "exedjs_function:history.back();", out);
                            	return;
                            }
                        }

                    }catch (IOException e) {
                    	CommonLog.error(e.getMessage(),e);
                        // MultipartRequest failed to process, treat the request as normal content
                        bMultiPart = false;
                    }
                }

                String prmNm = "module";
                module = (bMultiPart) ? multi.getParameter(prmNm) : request.getParameter(prmNm);
                String module_js = cwUtils.esc4JS(module);
                // 根据页面相关的请求生成相应的处理该页面的类名，并创建相应的类对象
                // (例如：当登陆时WB_PACKAGE + module的值是"com.cw.wizbank. " + "login.LoginModule")
                // 生成com.cw.wizbank.LoginModule类

                Class c = Class.forName(WB_PACKAGE + module_js);
                 // (注：每个形如 ***Module 的类都是继承自ServletModule类，都是用于处理页面的，并由Dispatcher来转发)
                 mm = (ServletModule) c.newInstance();
            }catch (Exception ex) {
            	CommonLog.error("Exception : " + ex.getMessage());
                out.println("Excpetion : Param Error");
                return;
            }

			my_time = System.currentTimeMillis();
			my_id = (int)(Math.random() * 1000);
			Date my_date = new Date(my_time);
			my_action = (bMultiPart) ? multi.getParameter("cmd") : request.getParameter("cmd");
			mod = (bMultiPart) ? multi.getParameter("module") : request.getParameter("module");
			CommonLog.debug("[" + mod + " OPEN] ID:" + my_id  + "\t\t\t\t" + my_date.toString());
			cwUtils.setUsedXsl2Sess(request, sess, bMultiPart, multi);

            // pass request parameters to the appropriate
            // module and go
            mm.setParams(request, response, prof, con, static_env, clientEnc, xslQuestions, wizbini);
            if(bMultiPart) {
                mm.setMultipartParams(multi, tmpUploadPath);
            }
            // set request type: ajax or normal request
            mm.setRequestType();

            if(mm.param != null){
            	String pageName=getPageName(bMultiPart,request,multi);//获取当前页面的名称
            	mm.setJsonConfig(pageName);//初始化jsonconfig、获取当前page的label、初始化MataMap（页面的meta信息）

                mm.param.setBMultiPart(bMultiPart);
                mm.param.setClientEnc(clientEnc);
                mm.param.setEncoding(static_env.ENCODING);
                mm.param.setStatic_env(static_env);
                try {
                	if(bMultiPart){
                		Enumeration Enum=multi.parameters.keys();
                		Map paramMap =new HashMap();
                		while(Enum.hasMoreElements()){
                			String paramName=(String)Enum.nextElement();
                			Vector paramValue=(Vector)multi.parameters.get(paramName);
            				String[] temp = new  String[paramValue.size()];
            				for(int i=0 ;i<paramValue.size();i++){
            					 temp[i] =(String)paramValue.elementAt(i);
            				}
            				paramMap.put(paramName,temp );
                		}
                		BeanUtils.populate(mm.param,paramMap);
                	}else{
                		BeanUtils.populate(mm.param, request.getParameterMap());
                	}
					mm.param.common();
				} catch (IllegalAccessException e) {
					CommonLog.error(e.getMessage(),e);
				} catch (InvocationTargetException e) {
					CommonLog.error(e.getMessage(),e);
				}
				mm.param.setCur_time(cwSQL.getTime(con));
            }
            // If already login successfully, check if multiple login is permitted
            // and invalidate any session if necessary
            // Not allow multiple login and session login time != database login time
            if (prof != null && prof.usr_id != null && prof.usr_id.length() > 0)
            {
                if (!Application.MULTIPLE_LOGIN &&
                        !prof.login_date.equals(dbRegUser.getLastLoginDate(con, prof.usr_id))) {
                    sess.invalidate();
                    request.setAttribute("sitemesh_parameter", "excludes");
                    cwSysMessage e = new cwSysMessage(dbRegUser.MSG_MULTI_LOGIN);
                    if (mm.param != null) {
                        mm.sysMsg = mm.getErrorMsg(dbRegUser.MSG_MULTI_LOGIN, wizbini.cfgSysSetupadv.getLogin().getReloginUrl());
                        mm.sysMsg.setStatus(mm.MSG_ERROR);
                        mm.dispatchDirection();
                    } else {
                        mm.msgBox(mm.MSG_ERROR, e, wizbini.cfgSysSetupadv.getLogin().getReloginUrl(), out);
                    }
                    return;
                }
            }

            //if( cwUtils.checkUserRoleSkin(request, response, "/" + (prof.skin_root).trim() + "/" + (prof.current_role_skin_root).trim(), prof.role_url_home ) )
            //    return;
            mm.process();

            if(mm.param != null) {
            	mm.dispatchDirection();
            }

        }
        catch (SQLException e) {
            out.println("SQL error: " + cwUtils.esc4JS(e.getMessage()));
            CommonLog.error(e.getMessage(),e);
            sendError(response, request, e, mm);
            try {
                con.rollback();
            } catch (SQLException re) {
            	CommonLog.error(re.getMessage(),re);
                out.println("SQL rollback error: " + cwUtils.esc4JS(re.getMessage()));
            }
        }
        catch (cwException e) {
         //   out.println("Server error: " + e.getMessage());
            CommonLog.error(e.getMessage(),e);
            sendError(response, request, e, mm);
            try {
                con.rollback();
            } catch (SQLException re) {
            	CommonLog.error(re.getMessage(),re);
                out.println("SQL rollback error: " + cwUtils.esc4JS(re.getMessage()));
            }
        }
        catch(Exception e){
			out.println("Server error: " + cwUtils.esc4JS(e.getMessage()));
			CommonLog.error(e.getMessage(),e);
			sendError(response, request, e, mm);
			try {
				con.rollback();
			} catch (SQLException re) {
				CommonLog.error(re.getMessage(),re);
				out.println("SQL rollback error: " + cwUtils.esc4JS(re.getMessage()));
			}
        }

        finally {
CommonLog.info("[" + mod + " CLOSE] ID:" + my_id + "\t" + (System.currentTimeMillis() - my_time) + "\t\t" + "[ACTION : " + my_action + " ]");//  + "\t\t" + "[sess_id = " + sess.getId() + "]");
            try {
                con.commit();
                if (con != null && con.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                    con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                }
                if(con != null && !con.isClosed())
                    con.close();
                out.close();
            }
            catch (SQLException sqle) {
                out.println("Close connection error: " + cwUtils.esc4JS(sqle.getMessage()));
            }
        }
    }
    
  private void msgBox(HttpServletRequest request, String title, Connection con, cwSysMessage e, loginProfile prof, String url, PrintWriter out)
    throws IOException, cwException, SQLException
  {
        String encoding;
        if(prof == null)
            encoding = static_env.ENCODING;
        else
            encoding = prof.label_lan;
        String msg = e.getSystemMessage(encoding);

        genMsgBox(request, title, msg, prof, url, out);
  }

  private void genMsgBox(HttpServletRequest request, String title, String msg, loginProfile prof, String url, PrintWriter out)
    throws IOException, cwException
  {
        String xml = "";
        
		boolean script = false;
		
		if (url != null) {
			url = cwUtils.esc4Html(url);
		}
		
		url = cwUtils.getRealPath(request, cwUtils.esc4XML(url));
		if (url != null && url.trim().toLowerCase().startsWith("javascript:")) {
			url = url.trim().substring("javascript:".length());
			script = true;
		}

        xml = "<?xml version=\"1.0\" encoding=\"" + cwUtils.ENC_UTF + "\" standalone=\"no\" ?>" + cwUtils.NEWL;
        xml += "<message>" + cwUtils.NEWL;
        if(prof!=null)
            xml += prof.asXML() + cwUtils.NEWL ;
        xml += "<title>" + title + "</title>" + cwUtils.NEWL;
        xml += "<body>" + cwUtils.NEWL;
        xml += "<text>" + cwUtils.esc4XML(msg) + "</text>" + cwUtils.NEWL;
        
        if(script){
            xml += "<button url=\"" +  url+ "\"";
            xml += " script=\"" + script + "\"";
        }else{
            xml += "<button url=\"" +  url+ "\"";
        }
        
        xml += ">OK</button>" + cwUtils.NEWL;
        xml += "</body>" + cwUtils.NEWL;
        xml += "</message>" + cwUtils.NEWL;

        //File fXsl = new File(static_env.INI_XSL_MSGBOX);
        //String xslFile = fXsl.getAbsolutePath();
        //cwXSL.procAbsoluteXSLFile(xml, xslFile, out, static_env.DEBUG, static_env.COMP_XSL);
        static_env.procXSLFile(xml, static_env.INI_XSL_MSGBOX, out, null);
  }
  private String getPageName(boolean bMultiPart,HttpServletRequest request,MultipartRequest multi){
		String pageName="";

		String prmNm = "REFERER";
		String url = request.getHeader(prmNm);
		if (url == null) {
			prmNm = "pagename";
			url = (bMultiPart) ? multi.getParameter(prmNm) : request.getParameter(prmNm);
		}
		if (url != null) {
			url = url.substring(url.lastIndexOf("/") + 1);
			if (url.lastIndexOf(".") >= 0) {
				url = url.substring(0, url.lastIndexOf("."));
			}
		}
		pageName = url;
		return pageName;
	}

	private void sendError(HttpServletResponse response, HttpServletRequest request, Exception e, ServletModule mm) throws IOException {
		String err_msg = e.getMessage();
		err_msg = cwUtils.esc4JS(err_msg);
		if(mm.is_ajax_request) {
			if(err_msg != null && err_msg.equals(cwUtils.MESSAGE_SESSION_TIMEOUT)) {
				String cur_lan = wizbini.cfgSysSkinList.getDefaultLang();
				String message_label_name = "session_timeout_message";
				response.addHeader(cwUtils.RESPONSE_HEADER_SESSION_TIMEOUT, URLEncoder.encode(LangLabel.getValue(cur_lan, message_label_name), cwUtils.ENC_UTF));
				response.addHeader(cwUtils.RESPONSE_HEADER_MESSAGE_TITLE, URLEncoder.encode(LangLabel.getValue(cur_lan, "646"), cwUtils.ENC_UTF));
				response.addHeader(cwUtils.RESPONSE_HEADER_MESSAGE_BUTTON, URLEncoder.encode(LangLabel.getValue(cur_lan, "329"), cwUtils.ENC_UTF));
				response.addHeader(cwUtils.RESPONSE_HEADER_RELOGIN_URL, wizbini.cfgSysSetupadv.getLogin().getReloginUrl());
			}
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, err_msg);
		} else if(err_msg != null && err_msg.equals(cwUtils.MESSAGE_SESSION_TIMEOUT)) {
        	response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
        }
	}
}