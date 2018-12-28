package com.cw.wizbank.qdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUtils;

import org.apache.commons.beanutils.ConvertUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.util.StringUtils;

import com.cw.wizbank.Application;
import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.JsonMod.eip.bean.EnterpriseInfoPortalBean;
import com.cw.wizbank.JsonMod.eip.dao.EnterpriseInfoPortalDao;
import com.cw.wizbank.JsonMod.exam.ExamController;
import com.cw.wizbank.JsonMod.exam.ExamModule;
import com.cw.wizbank.JsonMod.exam.ExamPassport;
import com.cw.wizbank.JsonMod.role.RoleManager;
import com.cw.wizbank.JsonMod.user.User;
import com.cw.wizbank.accesscontrol.AcCourse;
import com.cw.wizbank.accesscontrol.AcModule;
import com.cw.wizbank.accesscontrol.AcObjective;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AcRegUser;
import com.cw.wizbank.accesscontrol.AcResources;
import com.cw.wizbank.accesscontrol.AcRoleFunction;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AcUserGroup;
import com.cw.wizbank.accesscontrol.AcXslQuestion;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.accesscontrol.acSignonLink;
import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemCost;
import com.cw.wizbank.ae.aeItemRequirementScheduler;
import com.cw.wizbank.ae.aeItemTreeNodePathScheduler;
import com.cw.wizbank.ae.aeUtils;
import com.cw.wizbank.cache.wizbCacheManager;
import com.cw.wizbank.codetable.CodeTable;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.content.EvalAccess;
import com.cw.wizbank.content.ModuleTrainingCenter;
import com.cw.wizbank.course.CourseContentUtils;
import com.cw.wizbank.course.ModulePrerequisiteManagement;
import com.cw.wizbank.course.ModulePrerequisiteModule;
import com.cw.wizbank.course.loadTargetLrnCacheAndCourseEnrollScheduler;
import com.cw.wizbank.credit.view.ViewCreditsDAO;
import com.cw.wizbank.dao.SQLMapClientFactory;
import com.cw.wizbank.dao.SqlMapClientDataSource;
import com.cw.wizbank.db.DbAcRole;
import com.cw.wizbank.db.DbCmSkillSet;
import com.cw.wizbank.db.DbCtGlossary;
import com.cw.wizbank.db.DbCtReference;
import com.cw.wizbank.db.DbMgMessage;
import com.cw.wizbank.db.DbTable;
import com.cw.wizbank.db.DbTrackingHistory;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.DbUserGrade;
import com.cw.wizbank.db.view.ViewCmToTree;
import com.cw.wizbank.db.view.ViewEntityToTree;
import com.cw.wizbank.db.view.ViewKnowledgeObjectToTree;
import com.cw.wizbank.db.view.ViewObjectiveAccess;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.importcos.ImportCos;
import com.cw.wizbank.km.KMScheduler;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.personalization.PsnBiography;
import com.cw.wizbank.quebank.quecontainer.DynamicAssessment;
import com.cw.wizbank.quebank.quecontainer.DynamicScenarioQue;
import com.cw.wizbank.quebank.quecontainer.FixedScenarioQue;
import com.cw.wizbank.report.ExportController;
import com.cw.wizbank.report.LearnerRptExporter;
import com.cw.wizbank.report.SurveyReport;
import com.cw.wizbank.search.IndexFilesScheduler;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.upload.ImportTemplate;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.ScheduledTaskController;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXMLLabel;
import com.cw.wizbank.util.cwXSL;
import com.cw.wizbank.util.updXml;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.ContextPath;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.web.WzbApplicationContext;
import com.cwn.wizbank.wechat.service.WechatService;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.ParameterNotFoundException;
import com.oreilly.servlet.ServletUtils;
import com.oroinc.text.perl.Perl5Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class qdbAction extends HttpServlet {
	private static final boolean XSL_DISPLAY = true;

	// Initialize global variables
	Perl5Util perl = new Perl5Util();
	ServletUtils sutils = new ServletUtils();

	// WaiLun : the name of the cookie for storing the uploaded and converted
	// filename
	private static final String ORIGINAL_FILENAME = "ORIGINAL_FILENAME";
	private static final String NEW_FILENAME = "NEW_FILENAME";
	private static final String RENAME = "RENAME";

	static final String DEFAULT_ENC = dbUtils.ENC_ENG;

	static final String MSG_STATUS = "STATUS";
	static final String MSG_ERROR = "ERROR";
	static final String MSG_INFO = "INFORMATION";

	public static final String AUTH_LOGIN_PROFILE = "auth_login_profile";
	public static final String AUTH_LOGIN_SID = "auth_login_sid";

	static final String QUIZ_TIMESTAMP = "qdb_quiz_timestamp";
	
	// remember me cookieName
	public static final String REMEMBER_ME_USER_ID = "remember_user_id";
	// remember me Cookie valid for 30 days 
	public static final int COOKIE_MAX_AGE = 60*60*60*24*30;

	// session key for module objective id
	public static final String SESS_MOD_OBJ_ID = "qdb_sess_mod_obj_id";

	// session key for resource list
	public static final String SESS_RES_LIST_TS = "qdb_sess_res_list_ts";
	public static final String SESS_RES_LIST_RES_ID = "qdb_sess_res_list_res_id";
	public static final String VIEW_RES = "myFolder";

	// session key for xslQuestion (aclControl)
	public static final String SESS_ACL_CONTROL_XSL_QUES = "xsl_question";

	// session keys for loginProfile
	public static final String SESS_QDB_PROFILE_USR_ID = "prof_usr_id";
	public static final String SESS_QDB_PROFILE_USR_PWD = "prof_usr_pwd";
	public static final String SESS_QDB_PROFILE_USR_ENT_ID = "prof_usr_ent_id";
	public static final String SESS_QDB_PROFILE_USR_DISPLAY = "prof_usr_display_bil";
	public static final String SESS_QDB_PROFILE_USR_STUTAS = "prof_usr_status";
	public static final String SESS_QDB_PROFILE_USR_LAST_LOGIN_DATE = "prof_usr_last_login_date";
	public static final String SESS_QDB_PROFILE_ENV = "prof_env";
	public static final String SESS_QDB_PROFILE_ENCODING = "prof_encoding";
	public static final String SESS_QDB_PROFILE_LABEL_LAN = "prof_label_lan";
	public static final String SESS_QDB_PROFILE_CUR_LAN = "prof_cur_lan";
	public static final String SESS_QDB_PROFILE_ROOT_ENT_ID = "prof_root_ent_id";
	public static final String SESS_QDB_PROFILE_ROOT_CODE = "prof_root_code";
	public static final String SESS_QDB_PROFILE_ROOT_LEVEL = "prof_root_level";
	public static final String SESS_QDB_PROFILE_ROOT_DISPLAY = "prof_root_display";
	public static final String SESS_QDB_PROFILE_USR_STE_USR_ID = "prof_usr_ste_usr_id";
	public static final String SESS_QDB_PROFILE_SYS_ROLES = "prof_sysRoles";
	public static final String SESS_QDB_PROFILE_USR_GROUPS = "prof_usrGroups";
	public static final String SESS_QDB_PROFILE_IS_PUBLIC = "prof_isPublic";
	public static final String SESS_QDB_PROFILE_XSL_ROOT = "prof_xsl_root";
	public static final String SESS_QDB_PROFILE_SKIN_ROOT = "prof_skin_root";
	public static final String SESS_QDB_PROFILE_CURRENT_ROLE = "prof_current_role";
	public static final String SESS_QDB_PROFILE_CURRENT_ROLE_XML = "prof_current_role_xml";
	public static final String SESS_QDB_PROFILE_HOME_FTN_XML = "prof_home_ftn_xml";
	public static final String SESS_QDB_PROFILE_CURRENT_ROLE_SKIN_ROOT = "prof_current_role_skin_root";
	public static final String SESS_QDB_PROFILE_ROLE_URL_HOME = "prof_role_url_home";
	public static final String SESS_QDB_PROFILE_LAST_LOGIN_STATUS = "prof_last_login_status";
	public static final String SESS_QDB_PROFILE_CURRENT_LOGIN_STATUS_XML = "prof_current_login_status_xml";
	public static final String SESS_QDB_PROFILE_ACCOUNT_LOCKED = "prof_account_locked";
	public static final String SESS_QDB_PROFILE_FIRST_LOGIN = "prof_first_login";
	public static final String SESS_QDB_PROFILE_COMMON_ROLE_ID = "common_role_id";
	public static final String SESS_QDB_PROFILE_SHOW_ALERT_MSG_TYPE = "show_alert_msg_type";
	public static final String SESS_QDB_PROFILE_LOGINCREDIT = "loginCredit";
	public static final String SESS_QDB_PROFILE_USR_LOGIN_IP_ADDR = "prof_usr_login_ip_addr";
	public static final String SESS_QDB_CPD_ENABLE = "cpd_enable";

	// Get these variables at startup
	public static qdbEnv static_env = null;
	private static boolean debug = false;
	public static String trust_domain = null;

	private static String SQL_IN_procUploadedFiles = " SELECT res_src_link LINK,res_img_link IMG_LINK, res_src_type TYPE FROM Resources WHERE res_id = ? ";

	public static String AUTH_MOD_IDS = "AUTH_MOD_IDS";
	public static String AUTH_ASS_IDS = "AUTH_ASS_IDS";
	public static String AUTH_QUE_IDS = "AUTH_QUE_IDS";
	// Tim add
	public static String AUTH_EAS_IDS = "AUTH_EAS_IDS";
	// Tim add end
	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_PAGESIZE = 10;
	private static final String DEFAULT_SORT_BY = "ASC";

	// Lun:Message Scheduler thread
	public static MessageScheduler msgThread = null;

	// Lun:Message Scheduler thread
	public static loadTargetLrnCacheAndCourseEnrollScheduler enrollThread = null;

	// tim :item requirement Scheduler thread
	public static aeItemRequirementScheduler itmReqThread = null;

	// kim: item tree node path thread. rebuild all record in table
	// aeItemTreeNodePath
	public static aeItemTreeNodePathScheduler treeNodePathThread = null;

	// Clifford:Thread to delete temp directory
	public static TempDirCleaner tempDirCleanerThread = null;

	// Clifford:Thread to index the content for searching
	public static IndexFilesScheduler myIndexFilesScheduler = null;

	// Dennis:Page Variants
	public static Hashtable xslQuestions = null;

	// using xml in place of wizb.ini for system parameters
	public static WizbiniLoader wizbini = null;

	public static String HOME_PAGE_TAG = "home_page";

	// Robin:Test Memory
	public static HashMap tests_memory = new HashMap();
	public static Hashtable Ques_memory = new Hashtable();

	// scheduled task
	public static ScheduledTaskController taskController = null;

	public static String skinJsonStr = null;
	public static String domain = null;

	// Joyce: Credit rank for learner homepage.
	public static Map creditRankMap = new HashMap();

	private final SqlMapClientDataSource sqlMapClient = SQLMapClientFactory
			.getSqlMapClient();

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Connection con = null;
		try {
			CommonLog.info("qdbAction.init() START...");
			// WizbiniLoader类用于读取并存放系统的配置信息(配置信息存放入config/system/*.xml)
			wizbini = WizbiniLoader.getInstance(config);

			// initialize qdbEnv
			// String env = config.getInitParameter(";env")
			static_env = (qdbEnv) config.getServletContext().getAttribute(
					WizbiniLoader.SCXT_STATIC_ENV);
			if (static_env == null) {
				static_env = new qdbEnv();
				static_env.init(wizbini);
				config.getServletContext().setAttribute(
						WizbiniLoader.SCXT_STATIC_ENV, static_env);
				CommonLog.info("Encoding = " + static_env.ENCODING);
			}
			debug = wizbini.cfgSysSetupadv.isDebug();

			cwTree.enableAppletTree = wizbini.cfgSysSetupadv
					.isEnableAppletTree();
			// initialize the connection pooling
			cwSQL sqlCon = new cwSQL();
			sqlCon.setParam(wizbini);
			con = sqlCon.openDB(false);

			// 获取数据库连接后，初始Application读取数据库配置信息
			Application.init(con);

			// del redundant data from aeItemCost
			aeItemCost.delRedundantItemCost(con, wizbini);
			// reset user login count
			CurrentActiveUser.cleanAllCAU(con);

			// Dennis:Page Variants
			xslQuestions = AcXslQuestion.getQuestions();

			// precompile xsl stylesheet
			if (wizbini.cfgSysSetup.getXslStylesheet().isCacheEnabled()) {
				cwXSL xslp = new cwXSL();
				xslp.loadCache(wizbini.getWebDocRoot(), wizbini
						.getXslCacheFileAbs(), new File(wizbini.getAppnRoot(),
						wizbini.cfgSysSetup.getXslStylesheet().getCacheDir()));
			}

			if (wizbini.cfgSysSetupadv.getItemPrerequisiteThread().isEnabled()) {
				itmReqThread = new aeItemRequirementScheduler(sqlCon);
				itmReqThread.setRefreshPeriod(wizbini.cfgSysSetupadv
						.getItemPrerequisiteThread().getInterval());
				itmReqThread.start();
			}
			if (wizbini.cfgSysSetupadv.getItemTreenotePathThread().isEnabled()) {
				treeNodePathThread = new aeItemTreeNodePathScheduler(sqlCon);
				treeNodePathThread.start();
			}

			// init cwXMLLabel variable
			cwXMLLabel.DOC_ROOT = wizbini.getWebDocRoot();
			cwXMLLabel.INI_XSL_HOME = wizbini.cfgSysSetup.getXslStylesheet()
					.getHome();

			// ScheduledTaskController
			taskController = new ScheduledTaskController(wizbini, sqlCon,
					static_env);
			taskController.start();

			// threads of km
			if (wizbini.cfgSysSetupadv.getKmIndexingThread().isEnabled()) {
				myIndexFilesScheduler = new IndexFilesScheduler(
						wizbini.cfgSysSetupadv.getKmIndexingThread()
								.getInterval());
				myIndexFilesScheduler.startScheduler();
			}
			if (wizbini.cfgSysSetupadv.getKmSubscriptionNotifyThread()
					.isEnabled()) {
				KMScheduler kmThread = new KMScheduler(sqlCon);
				kmThread.setRefreshTime(wizbini.cfgSysSetupadv
						.getKmSubscriptionNotifyThread().getInterval());
				kmThread.setDailyMailSendTime(wizbini.cfgSysSetupadv
						.getKmSubscriptionNotifyThread().getDailyMailSendTime());
				kmThread.setWeeklyMailSendTime(wizbini.cfgSysSetupadv
						.getKmSubscriptionNotifyThread()
						.getWeeklyMailSendTime());
				kmThread.start();
			}

			acSite site = new acSite();
			site.ste_ent_id = 1;
			site.get(con);
			trust_domain = site.ste_domain;

			// openoffice服务启动
			/*
			 * openoffice弃用 if (Application.OPENOFFICE_ENABLED) { try { String
			 * command = Application.OPENOFFICE_PATH +
			 * " -headless -accept=\"socket,host=" + Application.OPENOFFICE_HOST
			 * + ",port=" + Application.OPENOFFICE_PORT +
			 * ";urp;\" -nofirststartwizard";
			 * 
			 * if (Application.OPENOFFICE_ENVIRONMENT.equalsIgnoreCase("linux"))
			 * { command = Application.OPENOFFICE_PATH +
			 * " \"-accept=socket,host=" + Application.OPENOFFICE_HOST +
			 * ",port=" + Application.OPENOFFICE_PORT +
			 * ";urp;StarOffice.ServiceManager\" -nologo -headless -nofirststartwizard &"
			 * ; } Runtime.getRuntime().exec(command); } catch (IOException e) {
			 * e.printStackTrace();
			 * System.out.println("****openoffice服务启动失败！****"); }
			 * wizbini.openOfficeConnection = new
			 * SocketOpenOfficeConnection(Application.OPENOFFICE_HOST,
			 * Application.OPENOFFICE_PORT); }
			 */
			dbPoster.getHeaderAndFooterSetting(con, wizbini);

			// init module type
			dbModuleType modType = new dbModuleType();

			// init langLabel
			LangLabel.init(new File(wizbini.getAppnRoot()),
					new File(wizbini.getWebDocRoot()),
					new File(wizbini.getWebConfigRoot()), null, con);

			// init UsgUgrFullPath
			EntityFullPath.getInstance(con);

			// 如果二级培训中心独（即LN模式，即开放课程指派功能，否则屏蔽）
			String[] ftn_ext_ids = { AclFunction.FTN_AMD_COURSE_ASSIGN,
					AclFunction.FTN_AMD_EIP_MAIN };

			if (wizbini.cfgSysSetupadv.isTcIndependent()) {
				AcRoleFunction.updFunctionStatus(con, ftn_ext_ids, 1);
			} else {
				AcRoleFunction.updFunctionStatus(con, ftn_ext_ids, 0);
			}

			// 以后这部分需求改到配置文件中
			// 如果需要屏蔽某功能，在这里指定功能的ID
			// 以后这部分需求改到配置文件中
			// 如果需要屏蔽某功能，在这里指定功能的ID
			String[] hidden_ftn_ext_ids = { AclFunction.FTN_AMD_FACILITY_MGT,
					AclFunction.FTN_AMD_FACILITY_BOOK_CREATE,
					AclFunction.FTN_AMD_FACILITY_BOOK_CALENDAR,
					AclFunction.FTN_AMD_FACILITY_BOOK_HISTORY,
					AclFunction.FTN_AMD_FACILITY_INFO };
			AcRoleFunction.updFunctionStatus(con, hidden_ftn_ext_ids, 0);

			// 如果某项功能原来屏蔽了，现在要开启，在这里指定功能的ID
			String[] show_ftn_ext_ids = {};
			AcRoleFunction.updFunctionStatus(con, show_ftn_ext_ids, 1);

			// get credit rank for learner homepage
			creditRankMap = ViewCreditsDAO.getTopCreditRank(con,
					User.HOME_GETDAT_CREDIT_RANK_RECORD_NUM);

			// update xml
			updXml.upd(con, wizbini.getWebInfRoot(),
					wizbini.getFileUpdXmlDirAbs());

			cwXSL.initGoldenManHtml(static_env);

			ConvertUtils.register(new JsonHelper.TimestampConverter(),
					java.sql.Timestamp.class);
			CommonLog.info("qdbAction.init() END");
		} catch (Exception e) {
			CommonLog.error(e.getMessage(), e);
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
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

	public void initDebug(ServletConfig config) throws ServletException {
		super.init(config);
		Connection con = null;
		try {
			CommonLog.info("qdbAction.init() START...");
			// WizbiniLoader类用于读取并存放系统的配置信息(配置信息存放入config/system/*.xml)
			wizbini = WizbiniLoader.getInstance(config);

			// initialize qdbEnv
			// String env = config.getInitParameter("env");
			static_env = (qdbEnv) config.getServletContext().getAttribute(
					WizbiniLoader.SCXT_STATIC_ENV);
			if (static_env == null) {
				static_env = new qdbEnv();
				static_env.init(wizbini);
				config.getServletContext().setAttribute(
						WizbiniLoader.SCXT_STATIC_ENV, static_env);
				CommonLog.info("Encoding = " + static_env.ENCODING);
			}
			debug = wizbini.cfgSysSetupadv.isDebug();

			cwTree.enableAppletTree = wizbini.cfgSysSetupadv
					.isEnableAppletTree();
			// initialize the connection pooling

			SqlSessionTemplate sstp = (SqlSessionTemplate) WzbApplicationContext
					.getBean("sqlSessionTemplate");

			con = sstp.getConnection();

			cwSQL sqlCon = new cwSQL();
			sqlCon.setParam(wizbini);

			// 获取数据库连接后，初始Application读取数据库配置信息
			// Application.init(con);

			// del redundant data from aeItemCost
			// aeItemCost.delRedundantItemCost(con, wizbini);
			// reset user login count
			// CurrentActiveUser.cleanAllCAU(con);

			// Dennis:Page Variants
			xslQuestions = AcXslQuestion.getQuestions();

			// precompile xsl stylesheet
			if (wizbini.cfgSysSetup.getXslStylesheet().isCacheEnabled()) {
				cwXSL xslp = new cwXSL();
				xslp.loadCache(wizbini.getWebDocRoot(), wizbini
						.getXslCacheFileAbs(), new File(wizbini.getAppnRoot(),
						wizbini.cfgSysSetup.getXslStylesheet().getCacheDir()));
			}

			/*
			 * if
			 * (wizbini.cfgSysSetupadv.getItemPrerequisiteThread().isEnabled())
			 * { itmReqThread = new aeItemRequirementScheduler(sqlCon);
			 * itmReqThread
			 * .setRefreshPeriod(wizbini.cfgSysSetupadv.getItemPrerequisiteThread
			 * ().getInterval()); itmReqThread.start(); } if
			 * (wizbini.cfgSysSetupadv.getItemTreenotePathThread().isEnabled())
			 * { treeNodePathThread = new aeItemTreeNodePathScheduler(sqlCon);
			 * treeNodePathThread.start(); }
			 */

			// init cwXMLLabel variable
			cwXMLLabel.DOC_ROOT = wizbini.getWebDocRoot();
			cwXMLLabel.INI_XSL_HOME = wizbini.cfgSysSetup.getXslStylesheet()
					.getHome();

			// ScheduledTaskController
			taskController = new ScheduledTaskController(wizbini, sqlCon,
					static_env);
			taskController.start();

			// threads of km
			if (wizbini.cfgSysSetupadv.getKmIndexingThread().isEnabled()) {
				myIndexFilesScheduler = new IndexFilesScheduler(
						wizbini.cfgSysSetupadv.getKmIndexingThread()
								.getInterval());
				myIndexFilesScheduler.startScheduler();
			}
			if (wizbini.cfgSysSetupadv.getKmSubscriptionNotifyThread()
					.isEnabled()) {
				KMScheduler kmThread = new KMScheduler(sqlCon);
				kmThread.setRefreshTime(wizbini.cfgSysSetupadv
						.getKmSubscriptionNotifyThread().getInterval());
				kmThread.setDailyMailSendTime(wizbini.cfgSysSetupadv
						.getKmSubscriptionNotifyThread().getDailyMailSendTime());
				kmThread.setWeeklyMailSendTime(wizbini.cfgSysSetupadv
						.getKmSubscriptionNotifyThread()
						.getWeeklyMailSendTime());
				kmThread.start();
			}

			// openoffice服务启动
			/*
			 * 弃用openoffice if (Application.OPENOFFICE_ENABLED) { try { String
			 * command = Application.OPENOFFICE_PATH +
			 * " -headless -accept=\"socket,host=" + Application.OPENOFFICE_HOST
			 * + ",port=" + Application.OPENOFFICE_PORT +
			 * ";urp;\" -nofirststartwizard";
			 * 
			 * if (Application.OPENOFFICE_ENVIRONMENT.equalsIgnoreCase("linux"))
			 * { command = Application.OPENOFFICE_PATH +
			 * " \"-accept=socket,host=" + Application.OPENOFFICE_HOST +
			 * ",port=" + Application.OPENOFFICE_PORT +
			 * ";urp;StarOffice.ServiceManager\" -nologo -headless -nofirststartwizard &"
			 * ; } Runtime.getRuntime().exec(command); } catch (IOException e) {
			 * e.printStackTrace();
			 * System.out.println("****openoffice服务启动失败！****"); }
			 * wizbini.openOfficeConnection = new
			 * SocketOpenOfficeConnection(Application.OPENOFFICE_HOST,
			 * Application.OPENOFFICE_PORT); }
			 */
			// init module type
			dbModuleType modType = new dbModuleType();

			// init langLabel
			LangLabel.init(new File(wizbini.getAppnRoot()),
					new File(wizbini.getWebDocRoot()),
					new File(wizbini.getWebConfigRoot()), null, con);

			// init UsgUgrFullPath
			EntityFullPath.getInstance(con);

			// get credit rank for learner homepage
			creditRankMap = ViewCreditsDAO.getTopCreditRank(con,
					User.HOME_GETDAT_CREDIT_RANK_RECORD_NUM);

			// update xml
			// updXml.upd(con, wizbini.getWebInfRoot(),
			// wizbini.getFileUpdXmlDirAbs());

			// cwXSL.initGoldenManHtml(static_env);

			// ConvertUtils.register(new JsonHelper.TimestampConverter(),
			// java.sql.Timestamp.class);
			// System.out.println("qdbAction.init() END");
		} catch (Exception e) {
			CommonLog.error(e.getMessage(), e);
		}
	}

	// to store param for obj list header
	/*
	 * CL : 2002-01-04 , No assessment objective anymore private class
	 * dataOListHdrPrm { long obj_id_syb; long obj_id_ass; dataOListHdrPrm () {
	 * obj_id_syb = 0; obj_id_ass = 0; } }
	 */

	public class dataObj {
		String tcId;//培训中心ID
		String group_cmd;////LN模式下培训中心操作类型
		
		String password;
		boolean ldap_auth;
		boolean create_new;
		String mode;
		String client_enc;
		String xsl_root;
		String session_upload_dir; // temporary upload dir within a sessin
		String tmpUploadDir; // temporary upload dir
		boolean bFileUpload;
		boolean bFileUploaded;
		String prm_ACTION;
		String encoding; // output encoding
		String label_lan; // Language of the label
		String style; // style preference
		String cur_stylesheet;
		boolean force_reload_xsl;
		String copy_media_from;
		String url_target;
		String url_login;
		String url_login_success;
		private String url_fail;
		public String url_fail1;
		String url_success;
		String domain;
		long site_id;
		String sort_order;
		long ent_id_parent;
		long begin_num;
		long end_num;
		long subId;
		int size;
		String sub;
		String template;
		String key;
		int cal_d = -100, cal_m = -100, cal_y = -100;
		Timestamp res_start_datetime, res_end_datetime;
		int num_of_mod; // Number of modules last visited show in the nevigator
		String rol_ext_id;
		String[] page_questions;
		String output_type;
		String tpl_subtype;
		// for assignment
		String viewRes;
		long step;
		String[] file_desc_lst;
		String[] file_order;
		String[] files;
		String ass_filename;
		String ass_comment;
		String[] type_lst;
		String[] download_lst;
		String email;
		Hashtable file_lst;
		String ass_queue;
		Timestamp ass_timestamp;
		long[] usr_ent_id_lst;
		long[] ent_id_lst;
		// String[] usr_id_lst;
		Timestamp[] usr_timestamp_lst;
		long[] usg_ent_id_lst;
		Timestamp[] usg_timestamp_lst;
		boolean upd_tst_que;
		boolean show_all;
		// search_user
		String user_code;
		// for Forum and Faq
		boolean isMaintain;
		boolean isExtend;
		boolean unthread;
		String phrase;
		String created_by;
		int search_type_topic;
		int search_type_msg;
		int phrase_cond;
		int created_by_cond;
		String[] msgLst;
		String[] topicLst;
		int page;
		Timestamp created_before;
		Timestamp created_after;
		int msg_length;

		// for Faq
		int commentOn;
		int search_que;
		int search_ans;
		int search_com;

		// for message
		String msg_title;
		String msg_body;
		int msg_id;
		int msgId;
		String msg_begin_date;
		String msg_end_date;
		String msg_method;
		Timestamp msg_send_time;
		long mod_id;
		String url_cmd;

		// For upload a zip file
		String uploaded_filename;

		// For course builder
		int lesson_count;
		int lesson_current;
		Hashtable wizard_data;
		int module_count;

		// For pagination and sorting
		int cur_page;
		int pagesize;
		String order_by;
		String sort_by;
		Timestamp pagetime;
		int page_size;
		String sortCol;
		String sortOrder;

		// For Objective
		long fr_obj_id, to_obj_id;

		loginProfile prof;

		String[] robs;
		String[] types;
		String[] subtypes;
		String[] res_id_lst;
		String[] que_id_lst;
		String[] que_order_lst;
		String[] que_score_lst;
		String[] que_multiplier_lst;
		String[] rpt_group_lst; // NEW
		String rpt_type;
		String rpt_date_type;
		Timestamp[] res_timestamp_lst;
		String rpt_search_full_name;

		// for AICC import
		// by cliff, 2001/4/18
		String aicc_crs_filename;
		String aicc_cst_filename;
		String aicc_des_filename;
		String aicc_au_filename;
		String aicc_ort_filename;

		// for NETg cdf file
		String netg_cdf_filename;

		// for scorm import
		String imsmanifestFileName;
		String cosUrlPrefix;

		// for course, mod eval
		String cur_grp_id;
		String cur_node;

		// for ins itm mod
		long itm_id;
		String itm_type;
		long[] itm_id_lst;
		boolean is_new_cos;

		// for messaging
		String ent_ids;
		String cc_ent_ids;
		String url_redirect;
		String xtp_type;
		String xtp_subtype;
		String sender_id;

		// for role target entities
		String[] rol_target_ext_ids;
		String[] rol_target_ent_groups;

		// for tree
		String node_id_lst;
		String node_type_lst;
		boolean self_group;

		// for file security (get permission for viewing the resource)
		boolean res_read_ind;
		// for module preview
		boolean res_preview_ind;

		// for multiple
		boolean multi_del;
		boolean multi_reactivate;

		// for signon
		long slk_id;
		Hashtable h_file_filename;

		// for tracking history
		long tkh_id;
		boolean use_tkh_ind;
		// dataOListHdrPrm olhPrm;

		// clone module information
		String[] sel_mod_id_list;
		long clo_src_itm_id;
		long clo_tag_cos_res_id;

		long src_res_id;
		// 公告的excape
		boolean is_upd;
		// System setting
		String threshold_warn;
		String threshold_block;
		String support_email;
		String multiple_login_ind;

		boolean isStudyGroupMod;
		long sgp_id;
		// new dbLib
		dbResource dbres;
		dbQuestion dbque;
		dbModule dbmod;
		dbCourse dbcos;
		dbUserGroup dbusg;
		dbAssignment dbass;
		dbForum dbforum;
		dbForumTopic dbforTopic;
		dbForumMessage dbforMsg;
		dbFaq dbfaq;
		dbFaqTopic dbfaqTopic;
		dbFaqMessage dbfaqMsg;
		qdbTestInstance tst;
		private dbRegUser usr;
		dbProgress dbpgr;
		dbSyllabus dbsyb;
		dbObjective dbobj;
		dbTemplate dbtpl;
		dbModuleSpec dbmsp;
		extendQue extque;
		dbSearchCond dbsearch;
		dbMessage dbmsg;
		dbDisplayOption dbdpo;
		dbEvent dbevt;
		dbChat dbchat;
		dbAiccPath dbacp;
		dbModuleEvaluation dbmov;
		dbCourseEvaluation dbcov;
		DbMgMessage dbMgMsg;
		dbThresholdSynLog dblog;
		dbPoster poster;
		// Vincent's code
		CodeTable dbcod;
		// Vincent's code

		// the training center of module
		ModuleTrainingCenter modTc;

		cwTree tree;
		cwPagination cwPage;

		public Vector vColName = new Vector();
		public Vector vColType = new Vector();
		public Vector vColValue = new Vector();
		public Vector vClobColName;
		public Vector vClobColValue;
		public Vector vExtColName = new Vector();
		public Vector vExtColType = new Vector();
		public Vector vExtColValue = new Vector();
		public Vector vExtClobColName;
		public Vector vExtClobColValue;

		UserGrade userGrade;
		String ugr_order;

		boolean is_start_test;
		boolean is_sso;
		boolean is_add_res;

		// dixson:for tree_frame
		String tree_frame_type;

		String window_name;
		String sso_refer;
		String sso_lms_url;
		// for thresholdlog,synlog
		String log_type;
		int last_days;
		Timestamp sys_log_starttime;
		Timestamp sys_log_endtime;
		boolean select_all;
		boolean remain_photo_ind;
		String option_lst;
		boolean del_que_media;
		Long usr_template_tcr_id;
		Long tcr_id;

		boolean isMobile;
		boolean isReceipt;

		/**
		 * @param url_fail
		 *            the url_fail to set
		 */
		public void setUrl_fail(String url_fail) {
			this.url_fail = url_fail;
		}

		/**
		 * @return the url_fail
		 */
		public String getUrl_fail() {
			return url_fail;
		}

		dataObj() {
			create_new = false;
			xsl_root = new String("");
			client_enc = new String("");

			tmpUploadDir = new String("");
			bFileUpload = false;
			bFileUploaded = false;
			prm_ACTION = new String("");
			label_lan = new String(dbUtils.ENC_ENG);
			encoding = new String(dbUtils.ENC_ENG);
			cur_stylesheet = new String("");
			force_reload_xsl = false;
			url_target = new String("");
			url_login_success = new String("");
			domain = new String("");
			site_id = 0;
			begin_num = 0;
			end_num = 0;
			upd_tst_que = false;
			show_all = false;
			pagetime = new Timestamp(0);
			h_file_filename = new Hashtable();
			user_code = null;
			// assignment
			step = 0;
			file_desc_lst = null;
			file_order = null;
			download_lst = null;
			files = null;
			ass_filename = null;
			ass_comment = null;
			email = null;
			file_lst = new Hashtable();
			ass_queue = null;
			ass_timestamp = null;
			use_tkh_ind = false;

			// forum and faq
			unthread = false;
			phrase = "";
			created_by = "";
			search_type_topic = 0;
			search_type_msg = 0;
			phrase_cond = 1;
			created_by_cond = 1;
			isExtend = false;
			msgLst = null;
			topicLst = null;
			page = 0;
			created_before = null;
			created_after = null;
			msg_length = 30;

			// For a upload zip file
			uploaded_filename = new String("");

			// faq
			commentOn = 0;
			search_que = 0;
			search_ans = 0;
			search_com = 0;

			// quiz
			subId = 0;
			sub = new String("");
			size = 0;
			template = new String("");
			key = new String("");
			robs = new String[30];
			types = new String[30];
			subtypes = new String[30];
			que_id_lst = new String[100];
			que_order_lst = new String[100];
			que_multiplier_lst = new String[100];
			rpt_group_lst = new String[100]; // NEW
			rpt_type = new String();
			rpt_date_type = new String();
			// olhPrm = new dataOListHdrPrm();
			prof = new loginProfile();

			// tree
			node_id_lst = null;
			node_type_lst = null;
			self_group = false;
			// course builder
			wizard_data = new Hashtable();

			// for AICC import
			// by cliff, 2001/4/18
			aicc_crs_filename = new String("");
			aicc_cst_filename = new String("");
			aicc_des_filename = new String("");
			aicc_au_filename = new String("");
			aicc_ort_filename = new String("");

			netg_cdf_filename = new String("");

			res_read_ind = false;
			res_preview_ind = false;

			multi_del = false;
			multi_reactivate = false;

			log_type = new String("");
			last_days = 0;
			sys_log_starttime = null;
			sys_log_endtime = null;
			select_all = false;

			isStudyGroupMod = false;
			sgp_id = 0;
			dbres = new dbResource();
			dbsyb = new dbSyllabus();
			dbque = new dbQuestion();
			dbmod = new dbModule();
			dbcos = new dbCourse();
			dbpgr = new dbProgress();
			dbobj = new dbObjective();
			tst = new qdbTestInstance();
			setUsr(new dbRegUser());
			dbusg = new dbUserGroup();
			dbtpl = new dbTemplate();
			dbmsp = new dbModuleSpec();
			dbsearch = new dbSearchCond();
			dbmsg = new dbMessage();
			dbass = new dbAssignment();
			dbforum = new dbForum();
			dbforTopic = new dbForumTopic();
			dbforMsg = new dbForumMessage();
			dbfaq = new dbFaq();
			dbfaqTopic = new dbFaqTopic();
			dbfaqMsg = new dbFaqMessage();
			dbdpo = new dbDisplayOption();
			dbevt = new dbEvent();
			dbchat = new dbChat();
			dbacp = new dbAiccPath();
			dbcov = new dbCourseEvaluation();
			dbmov = new dbModuleEvaluation();
			dbMgMsg = new DbMgMessage();
			userGrade = new UserGrade();
			poster = new dbPoster();
			// vincent
			dbcod = new CodeTable();
			// vincent

			// for training center of module
			modTc = new ModuleTrainingCenter();

			tree = new cwTree();
			cwPage = new cwPagination();
			dblog = new dbThresholdSynLog();
		}

		/**
		 * @param usr
		 *            the usr to set
		 */
		public void setUsr(dbRegUser usr) {
			this.usr = usr;
		}

		/**
		 * @return the usr
		 */
		public dbRegUser getUsr() {
			return usr;
		}

		// only for prototype
		public boolean goAdmin;

	}

	private class NoDataException extends Exception {
		NoDataException(String message) {
			super(message);
		}

		NoDataException() {
			super();
		}
	}

	private class InsertFailedException extends Exception {
		InsertFailedException(String message) {
			super(message);
		}

		InsertFailedException() {
			super();
		}
	}

	private class UpdateFailedException extends Exception {
		UpdateFailedException(String message) {
			super(message);
		}

		UpdateFailedException() {
			super();
		}
	}

	private class DeleteFailedException extends Exception {
		DeleteFailedException(String message) {
			super(message);
		}

		DeleteFailedException() {
			super();
		}
	}

	private class UploadedFileException extends Exception {
		UploadedFileException(String message) {
			super(message);
		}

		UploadedFileException() {
			super();
		}
	}

	private class IniFileException extends Exception {
		IniFileException(String message) {
			super(message);
		}

		IniFileException() {
			super();
		}
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long my_time = System.currentTimeMillis();
		int my_id = (int) (Math.random() * 1000);
		Date my_date = new Date(my_time);
		boolean passed = true;

		// variables for scope of a request
		dataObj dob = null;
		Connection con = null;
		PrintWriter out = null;
		HttpSession sess = request.getSession(true);

		String my_action = null;
		my_action = request.getParameter("cmd");
		// Performance Tunning
		CommonLog.info("[OPEN ] ID:" + my_id + "\t\t\t" + "[ACTION : "
				+ my_action + " ]\t" + my_date.toString());
		cwUtils.setUsedXsl2Sess(request, sess, false, null);

		// get the database connection for this request
		cwSQL sqlCon = new cwSQL();
		sqlCon.setParam(wizbini);
		try {
			con = sqlCon.openDB(false);
		} catch (Exception e) {
			out = response.getWriter();
			out.println("<b><h3> Sorry, the server is too busy.</h3></b>");
			return;
		}
		StringBuffer url = request.getRequestURL();
		domain = url
				.delete(url.length() - request.getRequestURI().length(),
						url.length()).append(request.getContextPath())
				.append("/").toString();

		// parse parameters from the client and get parameters from ini file
		// get system encoding
		String encoding = wizbini.cfgSysSetupadv.getEncoding();
		if (encoding == null || encoding.length() == 0) {
			encoding = DEFAULT_ENC;
		}
		// set up response header
		String lanCode = dbUtils.CharsetToLanguage(encoding);
		if (my_action != null && my_action.equalsIgnoreCase("get_used_xsl_lst")) {
			cwUtils.setContentType("text/xml", response, wizbini);
		} else {
			response.setContentType("text/html; charset=" + encoding);
		}
		response.setHeader("Content-Language", lanCode);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Expires", "Tue, 20 Aug 1996 00:00:00 GMT");

		out = response.getWriter();

		try {
			// handle the request and return to skip the lengthy DataObj
			String tempConType = request.getContentType();
			if (tempConType == null
					|| !tempConType.toLowerCase().startsWith(
							"multipart/form-data")) {

				String label_lan = null;
				label_lan = request.getParameter("label_lan");
				if (label_lan != null && !label_lan.equals("")) {
					label_lan = cwUtils.langToLabel(label_lan);
				} else if (wizbini.cfgSysSkinList.getDefaultLang() != null
						&& wizbini.cfgSysSkinList.getDefaultLang().length() > 0) {
					label_lan = cwUtils.langToLabel(wizbini.cfgSysSkinList
							.getDefaultLang());
				} else {
					label_lan = DEFAULT_ENC;
				}

				loginProfile prof = (loginProfile) sess
						.getAttribute(AUTH_LOGIN_PROFILE);

				if (my_action != null) {
					if (my_action.equalsIgnoreCase("START")
							|| my_action.equalsIgnoreCase("START_XML")) {

						if (sess.getAttribute("listener") != null) {
							sess.removeAttribute("listener");
						}
						sess.invalidate();
						String myStylesheet = request
								.getParameter("stylesheet");
						if (myStylesheet.indexOf("WEB-INF/") < 0) {
							acSite site = new acSite(encoding,
									wizbini.cfgSysSetupadv.getSkinHome());
							site.setWizbiniLoader(wizbini);
							String result = site.siteAsXML(con, label_lan);
							if (my_action.equalsIgnoreCase("START_XML")) {
								static_env.outputXML(out, result);
							} else {
								generalAsHtml(result, out, myStylesheet, null);
							}
							return;
						} else {
							throw new cwException("Param Error");
						}
					} else if (prof != null) {
						if (my_action.equalsIgnoreCase("GO_HOME")) {
							String url_home = prof.goHome(request);
							if (url_home != null && url_home.length() > 0) {
								response.sendRedirect(url_home);
							} else {
								response.sendRedirect(wizbini.cfgSysSetupadv
										.getLogin().getReloginUrl());
							}
							return;
						} else if (my_action.equalsIgnoreCase("HOME")
								|| my_action.equalsIgnoreCase("HOME_XML")) {
							if (prof != null
									&& prof.role_url_home != null
									&& prof.role_url_home.toLowerCase()
											.indexOf("app/") >= 0) {
								String url_home = prof.goHome(request);
								if (url_home != null && url_home.length() > 0) {
									response.sendRedirect(url_home);
								} else {
									response.sendRedirect(wizbini.cfgSysSetupadv
											.getLogin().getReloginUrl());
								}
							} else {
								String stylesheet = request
										.getParameter("stylesheet");
								String result = loginProfile.getHomeXML(con,
										sess, prof, stylesheet,
										wizbini.cfgTcEnabled);
								result = formatXML(result, null, HOME_PAGE_TAG,
										prof);

								if (my_action.equalsIgnoreCase("HOME_XML"))
									static_env.outputXML(out, result);
								else {
									generalAsHtml(result, out, stylesheet,
											prof.xsl_root);
								}
								return;
							}
						}
					}
				}
			}

			try {
				dob = parseParam(request);
			} catch (cwSysMessage e) {
				response.setHeader("Content-Language", "en");
				response.setContentType("text/html; charset=" + dbUtils.ENC_UTF);
				out = response.getWriter();
				try {
					dob = new dataObj();
					dob.prof = (loginProfile) sess
							.getAttribute(AUTH_LOGIN_PROFILE);
					dob.setUrl_fail("javascript:history.back()");
					msgBox(MSG_ERROR, con, e, dob, out);
				} catch (cwException ce) {
					out.println("MSGBOX Server error: "
							+ cwUtils.esc4JS(e.getMessage()));
				} catch (SQLException se) {
					out.println("MSGBOX SQL error: "
							+ cwUtils.esc4JS(e.getMessage()));
				}
				return;
			} catch (Exception e) {
				CommonLog.error(e.getMessage(), e);
				response.setHeader("Content-Language", "en");
				response.setContentType("text/html; charset=" + dbUtils.ENC_UTF);
				out = response.getWriter();
				// out.println("<b><h3> Server Error : </h3></b> " +
				// cwUtils.esc4Html(e.getMessage()));
				out.println("<b><h3> Server Error : </h3></b> Param Error");
				return;
			}

			// check login
			// check if current session has user id
			loginProfile p = null;

			dob.url_login = wizbini.cfgSysSetupadv.getLogin().getReloginUrl();

			if (!dob.prm_ACTION.equalsIgnoreCase("aff_auth")) {
				dob.url_success = cwUtils.getRealPath(request, dob.url_success);
				dob.setUrl_fail(cwUtils.getRealPath(request, dob.getUrl_fail()));
			}

			// --
			my_action = dob.prm_ACTION;

			// convert input usr_ent_id to usr_id
			try {
				if (dob.dbpgr.pgr_usr_id != null
						&& dob.dbpgr.pgr_usr_id.length() > 0)
					dob.dbpgr.pgr_usr_id = dbRegUser.usrEntId2UsrId(con,
							Long.parseLong(dob.dbpgr.pgr_usr_id));
				if (dob.tst.usr_id != null && dob.tst.usr_id.length() > 0)
					dob.tst.usr_id = dbRegUser.usrEntId2UsrId(con,
							Long.parseLong(dob.tst.usr_id));
			} catch (NumberFormatException nfe) {
				// do nothing
			}

			static_env.changeActiveReqNum(1);

			// get debug message for servlet connection
			if (dob.prm_ACTION.equals("get_servlet_info")) {
				static_env.dispDebugMesg(out,
						cwUtils.getRequestInfo(request, getServletConfig()));
				return;
			} else if (dob.prm_ACTION.equals("shutdown")) {
				static_env.sysShutdown(dob.password, out);
				return;
			} else if (dob.prm_ACTION.equals("startup")) {
				static_env.sysStartUp(dob.password, out);
				return;
			} else if (dob.prm_ACTION.equals("get_request_info")) {
				static_env.sysGetReqInfo(dob.password, out);
				return;
			} else if (static_env.shutdownMode) {
				response.sendRedirect(cwUtils.getRealPath(request,
						wizbini.cfgSysSetupadv.getMaintenance()
								.getRedirectUrl()));
				return;
			}
			// Reload all the cached xsl files
			else if (dob.prm_ACTION.equals("reload_xsl")) {
				if (wizbini.cfgSysSetup.getXslStylesheet().isCacheEnabled()) {
					cwXSL xslp = new cwXSL();
					File compiledHome = new File(wizbini.getAppnRoot(),
							wizbini.cfgSysSetup.getXslStylesheet()
									.getCacheDir());
					if (dob.cur_stylesheet != null
							&& dob.cur_stylesheet.length() > 0) {
						String stylesheet_folder = null;
						if (dob.cur_node != null && dob.cur_node.length() > 0) {
							stylesheet_folder = dob.cur_node;
						} else {
							stylesheet_folder = wizbini.cfgSysSetup
									.getXslStylesheet().getHome();
						}
						boolean saved = xslp.loadCacheSingle(
								wizbini.getWebDocRoot(), stylesheet_folder,
								dob.cur_stylesheet, compiledHome, out,
								dob.force_reload_xsl);
						// remove stylesheet from used_xsl_lst(sess)
						if (!saved) {
							cwUtils.removeUsedXslFromSess(sess,
									dob.cur_stylesheet);
						}
					} else {
						xslp.loadCache(wizbini.getWebDocRoot(),
								wizbini.getXslCacheFileAbs(), compiledHome);
					}
				}
				return;
			} else if (dob.prm_ACTION.equalsIgnoreCase("START")
					|| dob.prm_ACTION.equalsIgnoreCase("START_XML")) {
				if (sess != null)
					sess.invalidate();

				// String result = static_env.siteAsXML();

				acSite site = new acSite(dob.encoding,
						wizbini.cfgSysSetupadv.getSkinHome());
				site.setWizbiniLoader(wizbini);
				String result = site.siteAsXML(con, dob.label_lan);

				if (dob.prm_ACTION.equalsIgnoreCase("START_XML"))
					static_env.outputXML(out, result);
				else {
					generalAsHtml(result, out, dob);
				}
				return;
			} else if (dob.prm_ACTION.equalsIgnoreCase("AFF_TRASH_USR")) {
				try {
					String remoteHost = request.getRemoteHost();
					if (dbRegUser.aff_trashUsers(con, dob.getUsr().usr_id,
							dob.site_id, remoteHost)) {
						con.commit();
						response.sendRedirect(dob.url_success);
					} else {
						response.sendRedirect(dob.getUrl_fail());
					}
					return;
				} catch (Exception e) {
					con.rollback();
					response.sendRedirect(dob.getUrl_fail());
					return;
				}
			} else if (dob.prm_ACTION.equalsIgnoreCase("GET_SITE")) {

				response.setHeader("wizbank_encoding",
						wizbini.cfgSysSetupadv.getEncoding());
				String result = cwUtils.xmlHeader;
				result += "<site encoding=\""
						+ wizbini.cfgSysSetupadv.getEncoding() + "\"/>";

				if (dob.prm_ACTION.equalsIgnoreCase("GET_SITE_XML"))
					static_env.outputXML(out, result);
				else
					generalAsHtml(result, out, dob);

				return;
			} else if (dob.prm_ACTION.equalsIgnoreCase("reload_config")) {
				try {
					wizbini.reload();
					static_env.init(wizbini);
					// del redundant data from aeItemCost
					aeItemCost.delRedundantItemCost(con, wizbini);

					sqlCon.setParam(wizbini);
					ImportTemplate impTem = new ImportTemplate();
					impTem.rebuild(new File(wizbini.getAppnRoot()), new File(
							wizbini.getWebDocRoot()),
							new File(wizbini.getWebConfigRoot()), out, con);

					if (taskController != null) {
						taskController.reload(wizbini, out, sqlCon, static_env);
					}

					out.println("<br/>reload_config OK.");
				} catch (cwException ce) {
					out.println(ce.getMessage() + "<br>");
					out.println("reload_config failed.");
				}
				return;
			} else if (dob.prm_ACTION.equalsIgnoreCase("get_task_status")) {
				if (taskController != null) {
					taskController.printStatusMessage(out);
				}
				return;
			} else if (dob.prm_ACTION.equalsIgnoreCase("reload_task")) {
				if (taskController != null) {
					taskController.reload(wizbini, out, sqlCon, static_env);
				}
				return;
			} else if (dob.prm_ACTION.equalsIgnoreCase("reload_label")) {
				LangLabel.reload(new File(wizbini.getAppnRoot()), new File(
						wizbini.getWebDocRoot()),
						new File(wizbini.getWebConfigRoot()), out, con);

				String filePath = this.getServletConfig().getServletContext()
						.getRealPath("/");
				// System.out.println("filePath>>>>>>>>"+filePath);
				filePath += cwUtils.SLASH + "static" + cwUtils.SLASH + "js"
						+ cwUtils.SLASH + "i18n";
				// 初始化label
				try {
					new LabelContent(filePath);
				} catch (Exception e) {
					CommonLog.error(e.getMessage(), e);
				}

				return;
			} else if (dob.prm_ACTION.equalsIgnoreCase("get_used_xsl_lst")) {
				Vector xsl_lst = (Vector) sess
						.getAttribute(cwUtils.USED_XSL_LST);
				int lst_size = 0;
				Integer used_xsl_size = (Integer) sess
						.getAttribute(cwUtils.USED_XSL_SIZE);
				if (used_xsl_size != null) {
					lst_size = used_xsl_size.intValue();
				}
				String str = "<used_xsl_lst size=\"" + lst_size + "\">";
				if (xsl_lst != null && xsl_lst.size() > 0) {
					for (int i = 0; i < xsl_lst.size(); i++) {
						str += "<xsl>"
								+ cwUtils
										.esc4XML((String) xsl_lst.elementAt(i))
								+ "</xsl>";
					}
				}
				str += "</used_xsl_lst>";
				out.println(str);
				return;
			} else if (dob.prm_ACTION
					.equalsIgnoreCase("reload_entity_fullpath")) {
				EntityFullPath.getInstance(con).enclose(con, 0);
				out.print("reload_entity_fullpath completed!!");
				return;
			}
			// check login
			// check if current session has user id
			sess = request.getSession(true);
			// ClassCastException will be thrown if a new class loader is used
			// e.g. when a JSP is first loaded, a new class loader will be used
			// so use a wrapper class to read the loginProfile and put it back
			// to session
			try {

				p = (loginProfile) sess.getAttribute(AUTH_LOGIN_PROFILE);
			} catch (ClassCastException e) {

				p = new loginProfile();
				p.readSession(sess);
				sess.setAttribute(AUTH_LOGIN_PROFILE, p);
			}

			boolean validLogin = false;

			if (p != null && p.usr_id != null && p.usr_id.length() > 0) {
				dob.prof = p;

				if (Application.MULTIPLE_LOGIN
						|| p.login_date.equals(dbRegUser.getLastLoginDate(con,
								p.usr_id))) {
					validLogin = true;
					dob.xsl_root = p.xsl_root;
					dob.session_upload_dir = wizbini.getFileUploadTmpDirAbs()
							+ dbUtils.SLASH + sess.getAttribute(AUTH_LOGIN_SID);
				} else {
					sess.invalidate();
					dob.setUrl_fail(dob.url_login);
					cwSysMessage e = new cwSysMessage(dbRegUser.MSG_MULTI_LOGIN);
					request.setAttribute("sitemesh_parameter", "excludes");
					msgBox(MSG_ERROR, con, e, dob, out);
					return;
				}
				// if( cwUtils.checkUserRoleSkin(request, response, "/" +
				// (p.skin_root).trim() + "/" +
				// (p.current_role_skin_root).trim(), p.role_url_home) )
				// return;
			}

			// perform actions that do not need login
			// if an action is taken, just return
			if (doActionNotRequireLogin(con, dob, out)) {
				if (sess != null)
					sess.invalidate();
				return;
			}

			if (!validLogin) {
				if (sess != null)
					sess.invalidate();

				String url_login_success;

				if (dob.url_success == null || dob.url_success.length() <= 0) {
					url_login_success = getServReqURL(request);
				} else {
					url_login_success = dob.url_success;
				}
				/*
				 * // commented for not using this feature: // record the last
				 * accessed page and // go back to this page after relogin
				 * (2002.05.13 kawai) Cookie cookie_login_success = new
				 * Cookie("url_login_success", url_login_success);
				 * cookie_login_success.setPath("/");
				 * response.addCookie(cookie_login_success);
				 */
				response.sendRedirect(dob.url_login);
				return;
			}

			// validate input
			boolean bOk = validateInput(dob);
			if (!bOk) {
				static_env.dispDebugMesg(out, "Invalid input params.");
				return;
			}

			if (dob.prm_ACTION.equalsIgnoreCase("logout")) {
				sess = request.getSession(false);
				if (sess != null)
					sess.invalidate();

				acSite site = new acSite();
				site.ste_ent_id = dob.prof.root_ent_id;
				site.get(con);
				aeItem.usrTargetItmLstHS.clear();
				response.sendRedirect(site.ste_login_url);
				// response.sendRedirect(cwUtils.getRealPath(request,
				// site.ste_login_url));
				return;

			} else if (dob.prm_ACTION.equalsIgnoreCase("UNLOCK_ACCOUNT")) {

				dbRegUser.unlockAccount(con, dob.prof.usr_ent_id);

				response.sendRedirect(dob.url_success);
				return;
			}
			if (dob.prm_ACTION.equalsIgnoreCase("converData")) {
				aeItem itm = new aeItem();
				itm.convertItmData(con);

				cwSysMessage e = new cwSysMessage("ENT002");
				msgBox(MSG_STATUS, con, e, dob, out);
			} else if (dob.prm_ACTION
					.equalsIgnoreCase("converUserPositionData")) {
				DbCmSkillSet.converUserPositionData(con);

				cwSysMessage e = new cwSysMessage("ENT002");
				msgBox(MSG_STATUS, con, e, dob, out);
			}

			else if (dob.prm_ACTION.equalsIgnoreCase("INS_USR")) {
				try {

					AcUserGroup acusg = new AcUserGroup(con);
					boolean canMgtUsg = acusg.canManageGroup(dob.prof,
							dob.ent_id_parent, wizbini.cfgTcEnabled);

					if (!canMgtUsg) {
						dob.setUrl_fail(dob.url_fail1);
					}

					AcRegUser acusr = new AcRegUser(con);
					if (!acusr.hasInsPrivilege(dob.prof.usr_ent_id,
							dob.prof.root_ent_id, dob.prof.current_role,
							dob.getUsr().usr_roles)
							|| !canMgtUsg) {
						throw new qdbErrMessage("ACL002");
					}

					// 判断当前用户数是否已经达到上限
					String allowUserMaxSize = null;
					EnterpriseInfoPortalBean eipBean = null;
					if (wizbini.cfgSysSetupadv.isTcIndependent())
						eipBean = EnterpriseInfoPortalDao.getEipByTcrID(con,
								dob.prof.my_top_tc_id);
					if (eipBean == null
							|| dob.prof.current_role.startsWith("ADM")
							|| !wizbini.cfgSysSetupadv.isTcIndependent()
							|| dob.prof.my_top_tc_id == DbTrainingCenter
									.getSuperTcId(con, dob.prof.root_ent_id)) {
						allowUserMaxSize = wizbini.cfgSysSetup
								.getAllowUserMaxSize();
					} else {

						allowUserMaxSize = "" + eipBean.getEip_account_num();
					}

					if (!"".equals(allowUserMaxSize.trim())) {
						int maxSize = Integer.parseInt(allowUserMaxSize);
						// int totalNum = dbRegUser.getUserTotalNum(con);
						long totalNum = 0;//
						if (eipBean == null
								|| dob.prof.current_role.startsWith("ADM")
								|| !wizbini.cfgSysSetupadv.isTcIndependent()
								|| dob.prof.my_top_tc_id == DbTrainingCenter
										.getSuperTcId(con, dob.prof.root_ent_id)) {
							totalNum = dbRegUser.getUserTotalNum(con);
						} else {
							totalNum = eipBean.getAccount_used();
						}
						if (totalNum >= maxSize) {
							if (eipBean != null
									&& !dob.prof.current_role.startsWith("ADM")
									&& wizbini.cfgSysSetupadv.isTcIndependent()
									&& dob.prof.my_top_tc_id != DbTrainingCenter
											.getSuperTcId(con,
													dob.prof.root_ent_id)) {
								String cur_lan = dob.prof.cur_lan;
								String message = LangLabel.getValue(cur_lan,
										"LN005");
								message += "("
										+ LangLabel.getValue(cur_lan, "LN006");
								message += "/"
										+ LangLabel.getValue(cur_lan, "LN007");
								message += "):" + maxSize + "(" + totalNum
										+ "/" + (maxSize - totalNum) + ")";
								throw new cwSysMessage("LN327", message);
							} else {
								throw new qdbErrMessage("USG012",
										String.valueOf(totalNum));
							}

						}
					}
					// 检测选定的用户组是否存在
					String[] usr_attribute_relation_types = dob.getUsr().usr_attribute_relation_types;
					long[] usr_attribute_ent_ids = dob.getUsr().usr_attribute_ent_ids;
					if ((usr_attribute_relation_types != null || usr_attribute_ent_ids != null)
							&& !dbRegUser.isUsgExists(con,
									usr_attribute_relation_types,
									usr_attribute_ent_ids)) {
						throw new qdbErrMessage("USG011");
					}

					// dob.usr.ins(con,dob.prof,dob.ent_id_parent);

					dob.getUsr().insUser(con, dob.prof, dob.vColName,
							dob.vColType, dob.vColValue, dob.vClobColName,
							dob.vClobColValue, dob.vExtColName,
							dob.vExtColType, dob.vExtColValue,
							dob.vExtClobColName, dob.vExtClobColValue,
							dob.msg_title, wizbini.cfgTcEnabled);

					ObjectActionLog log = new ObjectActionLog(
							dob.getUsr().ent_id, dob.getUsr().usr_ste_usr_id,
							dob.getUsr().usr_display_bil,
							ObjectActionLog.OBJECT_TYPE_USR,
							ObjectActionLog.OBJECT_ACTION_ADD,
							ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
							dob.prof.getUsr_ent_id(),
							dob.prof.getUsr_last_login_date(), dob.prof.getIp());
					SystemLogContext.saveLog(log,
							SystemLogTypeEnum.OBJECT_ACTION_LOG);

					if ("use_default_image"
							.equals(dob.getUsr().extension_43_select)) {
						int slash = dob.getUsr().urx_extra_43.indexOf("\\");
						String slashType = "/";
						if (slash != -1) {
							slashType = "\\\\";
						}
						String[] originalPicDir = dob.getUsr().urx_extra_43
								.split(slashType);
						// String defaultPath = wizbini.getFileUploadUsrDirAbs()
						// + dbUtils.SLASH + "default" + dbUtils.SLASH +
						// dob.getUsr().urx_extra_43;
						String defaultPath = static_env.DOC_ROOT
								+ dbUtils.SLASH + static_env.DEFAULT_IMGLIG
								+ dbUtils.SLASH
								+ static_env.INI_INSTRUCTOR_DIR_UPLOAD_URL
								+ dbUtils.SLASH + dob.getUsr().urx_extra_43;
						String saveDirPath = wizbini.getFileUploadUsrDirAbs()
								+ dbUtils.SLASH + dob.getUsr().usr_ent_id
								+ dbUtils.SLASH + originalPicDir[1];
						dbUtils.copyFile(defaultPath, saveDirPath);
					} else if (dob.bFileUpload) {
						this.procUploadUsrFace(con, dob.tmpUploadDir,
								dob.getUsr().usr_ent_id, dob.remain_photo_ind,
								sess);
					}
					con.commit();
					cwSysMessage e = new cwSysMessage("USR006");
					msgBox(MSG_STATUS, con, e, dob, out);
					// response.sendRedirect(dob.url_success);
				} catch (qdbErrMessage e) {
					con.rollback();
					msgBox(MSG_ERROR, con, e, dob, out);
					return;
				}
			} else if (dob.prm_ACTION.equalsIgnoreCase("REGISTER_USR")) {
				try {
					// 判断当前用户数是否已经达到上限
					String allowUserMaxSize = null;
					EnterpriseInfoPortalBean eipBean = null;
					if (wizbini.cfgSysSetupadv.isTcIndependent()) {
						eipBean = EnterpriseInfoPortalDao.getEipByTcrID(con,
								dob.prof.my_top_tc_id);
					}
					if (eipBean == null
							|| dob.prof.current_role.startsWith("ADM")
							|| !wizbini.cfgSysSetupadv.isTcIndependent()
							|| dob.prof.my_top_tc_id == DbTrainingCenter
									.getSuperTcId(con, dob.prof.root_ent_id)) {
						allowUserMaxSize = wizbini.cfgSysSetup
								.getAllowUserMaxSize();
					} else {

						allowUserMaxSize = "" + eipBean.getEip_account_num();
					}

					if (!"".equals(allowUserMaxSize.trim())) {
						int maxSize = Integer.parseInt(allowUserMaxSize);
						long totalNum = 0;
						if (eipBean == null
								|| dob.prof.current_role.startsWith("ADM")
								|| !wizbini.cfgSysSetupadv.isTcIndependent()
								|| dob.prof.my_top_tc_id == DbTrainingCenter
										.getSuperTcId(con, dob.prof.root_ent_id)) {
							totalNum = dbRegUser.getUserTotalNum(con);
						} else {
							totalNum = eipBean.getAccount_used();
						}
						if (totalNum >= maxSize) {
							if (eipBean != null
									&& !dob.prof.current_role.startsWith("ADM")
									&& wizbini.cfgSysSetupadv.isTcIndependent()
									&& dob.prof.my_top_tc_id != DbTrainingCenter
											.getSuperTcId(con,
													dob.prof.root_ent_id)) {
								String cur_lan = dob.prof.cur_lan;
								String message = LangLabel.getValue(cur_lan,
										"LN005");
								message += "("
										+ LangLabel.getValue(cur_lan, "LN006");
								message += "/"
										+ LangLabel.getValue(cur_lan, "LN007");
								message += "):" + maxSize + "(" + totalNum
										+ "/" + (maxSize - totalNum) + ")";
								throw new cwSysMessage("LN327", message);
							} else {
								throw new qdbErrMessage("USG012",
										String.valueOf(totalNum));
							}

						}
					}
					dob.getUsr().registerUser(con, dob.prof, dob.vColName,
							dob.vColType, dob.vColValue, dob.vClobColName,
							dob.vClobColValue, dob.vExtColName,
							dob.vExtColType, dob.vExtColValue,
							dob.vExtClobColName, dob.vExtClobColValue,
							dob.itm_id_lst, wizbini.cfgTcEnabled);
					con.commit();
					// DENNIS: TODO: new system message
					cwSysMessage e = new cwSysMessage("USR012");
					msgBox(MSG_STATUS, con, e, dob, out);
				} catch (qdbErrMessage e) {
					con.rollback();
					msgBox(MSG_ERROR, con, e, dob, out);
					return;
				}
			} else if (dob.prm_ACTION.equalsIgnoreCase("GRADE_QUE")) {
				try {
					if (dbProgress.getLastAttemptNbr(con, dob.dbmod.mod_res_id,
							dob.tkh_id) > dob.dbpgr.pgr_attempt_nbr) {
						throw new qdbErrMessage(
								dbProgress.NEW_ATTEMPT_SUBMITTED_MSG);
					}

					dbProgress pgr = new dbProgress();
					pgr.pgr_usr_id = dob.dbpgr.pgr_usr_id;
					pgr.pgr_res_id = dob.dbmod.mod_res_id;
					pgr.pgr_tkh_id = dob.tkh_id;
					pgr.get(con, dob.dbpgr.pgr_attempt_nbr);
					pgr.updScore(con, dob.dbque.que_res_id,
							dob.dbque.que_score, dob.prof);
					con.commit();
					response.sendRedirect(dob.url_success);
				} catch (qdbErrMessage e) {
					con.rollback();
					msgBox(MSG_ERROR, con, e, dob, out);
					return;
				}
			} else if (dob.prm_ACTION.equalsIgnoreCase("GRADE_ALL_QUE")) {
				try {
					if (dbProgress.getLastAttemptNbr(con, dob.dbmod.mod_res_id,
							dob.tkh_id) > dob.dbpgr.pgr_attempt_nbr) {
						throw new qdbErrMessage(
								dbProgress.NEW_ATTEMPT_SUBMITTED_MSG);
					}
					dbProgress pgr = new dbProgress();
					pgr.pgr_usr_id = dob.dbpgr.pgr_usr_id;
					pgr.pgr_res_id = dob.dbmod.mod_res_id;
					pgr.pgr_tkh_id = dob.tkh_id;
					pgr.get(con, dob.dbpgr.pgr_attempt_nbr);
					for (int i = 0; i < dob.que_id_lst.length; i++) {
						dob.dbque.que_res_id = Long
								.parseLong(dob.que_id_lst[i]);
						dob.dbque.que_score = Integer
								.parseInt(dob.que_score_lst[i]);
						pgr.updScore(con, dob.dbque.que_res_id,
								dob.dbque.que_score, dob.prof);
					}
					con.commit();
					response.sendRedirect(dob.url_success);
				} catch (qdbErrMessage e) {
					con.rollback();
					msgBox(MSG_ERROR, con, e, dob, out);
					return;
				}
			} else {
				passed = false;
			}

			// Function for post message
			if (!passed) {
				service_cond(request, response, dob, con, out, sess, false);
			}
		} catch (cwSysMessage e) {
			CommonLog.error(e.getMessage(), e);
			try {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			} catch (cwException ce) {
				throw new ServletException(ce.getMessage());
			} catch (SQLException se) {
				throw new ServletException(se.getMessage());
			}

		} catch (SQLException e) {
			CommonLog.error(e.getMessage(), e);
			try {
				con.rollback();
				static_env.dispDebugMesg(out,
						"SQLException caught: " + e.getMessage());
			} catch (SQLException err) {
				throw new ServletException(err.getMessage());
			}

		} catch (NumberFormatException e) {
			CommonLog.error(e.getMessage(), e);
			try {
				con.rollback();
				static_env.dispDebugMesg(out,
						"Number Format Exception: " + e.getMessage());
			} catch (SQLException err) {
				CommonLog.error(err.getMessage(), err);
				throw new ServletException(err.getMessage());
			}

		} catch (UploadedFileException e) {
			CommonLog.error(e.getMessage(), e);
			try {
				con.rollback();
				static_env.dispDebugMesg(out,
						"Upload File Exception: " + e.getMessage());
			} catch (SQLException err) {
				throw new ServletException(err.getMessage());
			}

		} catch (UnsupportedEncodingException e) {
			CommonLog.error(e.getMessage(), e);
			try {
				con.rollback();
				static_env.dispDebugMesg(out,
						"Encoding conversion fails: " + e.getMessage());
			} catch (SQLException err) {
				throw new ServletException(err.getMessage());
			}

		} catch (Exception e) {
			out.println("Server error: " + cwUtils.esc4JS(e.getMessage()));
			CommonLog.error(e.getMessage(), e);
			try {
				con.rollback();
			} catch (SQLException re) {
				out.println("SQL rollback error: "
						+ cwUtils.esc4JS(re.getMessage()));
			}
		} finally {
			// always close db connection
			try {
				con.commit();
				if (con != null
						&& con.getTransactionIsolation() == Connection.TRANSACTION_READ_UNCOMMITTED
						&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL
								.getDbType())) {
					con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
				}
				if (con != null)
					con.close();
				CommonLog.info("[CLOSE] ID:" + my_id + "\t"
						+ (System.currentTimeMillis() - my_time) + "\t\t"
						+ "[ACTION : " + my_action + " ]");
				// System.out.println("[CLOSE] ID:" + my_id + "\t" +
				// "[ACTION : " + my_action + " ]" );
				if (out != null) {
					out.close();
				}
			} catch (SQLException ignored) {
			}
			static_env.changeActiveReqNum(-1);
		}
	}

	private void service_cond(HttpServletRequest request,
			HttpServletResponse response, dataObj dob, Connection con,
			PrintWriter out, HttpSession sess, boolean share_mode)
			throws cwSysMessage, cwException, SQLException, qdbException,
			IOException, UploadedFileException, qdbErrMessage,
			CloneNotSupportedException, Exception {

		boolean passed = true;

		if (dob.prm_ACTION.equalsIgnoreCase("REG_COMP")) {

			if (dob.xtp_type != null && dob.xtp_type.length() > 0
					&& dob.xtp_subtype != null && dob.xtp_subtype.length() > 0) {
				dob.dbMgMsg.msg_send_usr_id = dob.prof.usr_id;
				dob.dbMgMsg.msg_create_usr_id = dob.prof.usr_id;
				dob.dbMgMsg.msg_create_timestamp = cwSQL.getTime(con);
				dob.dbMgMsg.msg_target_datetime = dob.dbMgMsg.msg_create_timestamp;

				Vector params = new Vector();
				Vector paramsName = new Vector();
				Vector paramsType = new Vector();
				Vector paramsValue = new Vector();

				paramsName.addElement("ent_ids");
				paramsName.addElement("cmd");
				paramsName.addElement("url_redirect");
				paramsName.addElement("sender");

				paramsType.addElement("DYNAMIC");
				paramsType.addElement("STATIC");
				paramsType.addElement("STATIC");
				paramsType.addElement("STATIC");

				paramsValue.addElement("GET_ENT_ID");
				paramsValue.addElement("link_notify_xml");
				paramsValue.addElement(dob.url_redirect);
				paramsValue.addElement(dob.sender_id);

				params.addElement(paramsName);
				params.addElement(paramsType);
				params.addElement(paramsValue);

				long[] ent_ids = null;
				if (dob.ent_ids != null && dob.ent_ids.length() > 0)
					dbUtils.string2LongArray(dob.ent_ids, "~");
				else {
					acSite site = new acSite();
					site.ste_ent_id = dob.prof.root_ent_id;
					ent_ids = new long[1];
					ent_ids[0] = site.getSiteSysEntId(con);
				}
				String[] xtp_subtype = new String[1];
				xtp_subtype[0] = dob.xtp_subtype;
				Message msg = new Message();
				msg.insNotify(con, ent_ids, null, dob.xtp_type, xtp_subtype,
						dob.dbMgMsg, params);
				con.commit();
				if (qdbAction.msgThread != null)
					qdbAction.msgThread.addMessageIdToQueue(msg.msg_id);

			}

			response.sendRedirect(dob.url_success);

		}
		// this cmd is no longer being used (2005-08-18 kawai)
		// }else if (dob.prm_ACTION.equalsIgnoreCase("REG_USR")) {
		// /*
		// try {
		// dob.usr.regUser(con, dob.dbusg.usg_ent_id, dob.site_id);
		// con.commit();
		// response.sendRedirect(dob.url_success);
		// return;
		//
		// } catch(qdbErrMessage err) {
		// con.rollback();
		// response.sendRedirect(dob.url_fail);
		// return;
		// }
		// */
		// try {
		// //Access Control, Not Yet Implement
		// acSite site;
		// dob.usr.usr_attribute_ent_ids = new long[] {dob.ent_id_parent};
		// dob.usr.usr_attribute_relation_types = new String []
		// {dbEntityRelation.ERN_TYPE_USR_PARENT_USG};
		//
		// dob.usr.ins(con,dob.prof, null);
		//
		// /*
		// if( dob.xtp_type != null && dob.xtp_type.length() > 0
		// && dob.xtp_subtype != null && dob.xtp_subtype.length() > 0 ) {
		// dob.dbMgMsg.msg_send_usr_id = dob.prof.usr_id;
		// dob.dbMgMsg.msg_create_usr_id = dob.prof.usr_id;
		// dob.dbMgMsg.msg_create_timestamp = cwSQL.getTime(con);
		// dob.dbMgMsg.msg_target_datetime = dob.dbMgMsg.msg_create_timestamp;
		//
		// Vector params = new Vector();
		// Vector paramsName = new Vector();
		// Vector paramsType = new Vector();
		// Vector paramsValue = new Vector();
		//
		//
		// paramsName.addElement("ent_ids");
		// paramsName.addElement("cmd");
		// paramsName.addElement("url_redirect");
		// paramsName.addElement("sender_id");
		//
		// paramsType.addElement("DYNAMIC");
		// paramsType.addElement("STATIC");
		// paramsType.addElement("STATIC");
		// paramsType.addElement("STATIC");
		//
		// paramsValue.addElement("GET_ENT_ID");
		// paramsValue.addElement("link_notify_xml");
		// paramsValue.addElement(dob.url_redirect);
		// paramsValue.addElement(dob.sender_id);
		//
		// params.addElement(paramsName);
		// params.addElement(paramsType);
		// params.addElement(paramsValue);
		//
		// long[] ent_ids = null;
		// if( dob.ent_ids != null && dob.ent_ids.length() > 0 )
		// dbUtils.string2LongArray(dob.ent_ids, "~");
		// else {
		// site = new acSite();
		// site.ste_ent_id = dob.prof.root_ent_id;
		// ent_ids = new long[1];
		// ent_ids[0] = site.getSiteSysEntId(con);
		// }
		// String[] xtp_subtype = new String[1];
		// xtp_subtype[0] = dob.xtp_subtype;
		// Message msg = new Message();
		// msg.insNotify(con, ent_ids, null, dob.xtp_type, xtp_subtype,
		// dob.dbMgMsg, params);
		// con.commit();
		// if( qdbAction.msgThread != null )
		// qdbAction.msgThread.addMessageIdToQueue(msg.msg_id);
		//
		// }*/
		//
		// //Login
		// p = new loginProfile();
		// p.usr_id = dob.usr.usr_id;
		// p.usr_ste_usr_id = dob.usr.usr_ste_usr_id;
		// site = new acSite();
		// site.ste_ent_id = dob.prof.root_ent_id;
		// dbRegUser.login(con, p, dob.usr.usr_ste_usr_id, dob.usr.usr_pwd,
		// null, site, true, wizbini);
		// p.skin_root = wizbini.cfgSysSetupadv.getSkinHome();
		//
		// p.xsl_root = wizbini.cfgSysSetupadv.getSkinHome() + dbUtils.SLASH +
		// p.current_role_skin_root + dbUtils.SLASH +
		// wizbini.cfgSysSetup.getXslStylesheet().getHome();
		//
		// dob.xsl_root = p.xsl_root;
		//
		// p.label_lan = dob.prof.label_lan;
		// p.env = static_env.ENV;
		// p.encoding = dob.prof.encoding;
		//
		// sess.setAttribute(AUTH_LOGIN_PROFILE, p);
		// sess.setAttribute(SESS_ACL_CONTROL_XSL_QUES, xslQuestions);
		// String SID = p.usr_id + Math.round(Math.random() * 100000) ;
		// sess.setAttribute(AUTH_LOGIN_SID, SID);
		// //p.writeSession(sess);
		//
		// boolean login_to_home = false;
		// if (dob.url_success == null || dob.url_success.length() ==0) {
		// dob.url_success = p.goHome(request);
		// login_to_home = true;
		// }
		//
		// response.sendRedirect(dob.url_success);
		//
		// } catch(qdbErrMessage e) {
		// con.rollback();
		// msgBox(MSG_ERROR, con, e, dob, out);
		// return;
		// }
		//
		// }
		else if (dob.prm_ACTION.equalsIgnoreCase("GET_SUSPENSE_USR")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_SUSPENSE_USR_XML")) {

			try {
				if (AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
						AclFunction.FTN_AMD_USR_ACTIVATE)
						&& AccessControlWZB.isRoleTcInd(dob.prof.current_role)) {
					if (!ViewTrainingCenter.hasEffTc(con, dob.prof.usr_ent_id)) {
						cwSysMessage e = new cwSysMessage("TC016",
								Long.toString(dob.dbque.res_id));
						msgBox(MSG_ERROR, con, e, dob, out);
						return;
					}
				}

				if (wizbini.cfgSysSetupadv.isTcIndependent()
						&& AccessControlWZB.hasRolePrivilege(
								dob.prof.current_role,
								AclFunction.FTN_AMD_USR_ACTIVATE)
						&& AccessControlWZB.isRoleTcInd(dob.prof.current_role)) {
					Vector vec = dbUserGroup.getAllTargetGroupIdForOfficer(con,
							dob.prof.usr_ent_id);
					if (null == vec || vec.size() <= 0) {
						throw new qdbErrMessage("USG010");
					}
				}

				int max_trial = ((UserManagement) wizbini.cfgOrgUserManagement
						.get(dob.prof.root_id)).getAccountSuspension()
						.getMaxTrial();
				Vector usgId = null;
				if (AccessControlWZB.isRoleTcInd(dob.prof.current_role)) {
					usgId = dbUserGroup.getUsgEntIdByUser(con,
							dob.prof.usr_ent_id, dob.prof.current_role,
							wizbini.cfgTcEnabled);
				} else {
					usgId = dbUserGroup.getUsgEntIdByUser(con,
							dob.prof.root_ent_id, dob.prof.current_role,
							wizbini.cfgTcEnabled);
				}

				if (usgId != null && usgId.size() > 0) {
					dob.dbusg.s_usg_ent_id_lst = new String[usgId.size()];
					for (int i = 0; i < usgId.size(); i++) {
						dob.dbusg.s_usg_ent_id_lst[i] = String
								.valueOf((Long) usgId.elementAt(i));
					}
				}
				String result = dob.dbusg.getSuspenseUserXML(con, sess,
						dob.prof, dob.page, dob.pagesize, max_trial,
						dob.user_code);

				String metaXML = "";
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.getUsr().usr_ent_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				acPageVariant.root_id = dob.prof.root_id;
				acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
				acPageVariant.setWizbiniLoader(qdbAction.wizbini);
				metaXML = acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet));
				metaXML += dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id);
				result = formatXML(result, metaXML, "user_manager", dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("GET_SUSPENSE_USR_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_SUSPENSE_USR"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("UPD_MOD2")) {
			try {
				long modId;
				long cosId = dbModule.getCosId(con, dob.dbmod.mod_res_id);
				dob.dbcos.cos_res_id = cosId;
				dob.dbcos.res_id = cosId;

				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				String modTitle = new String();
				String modAction = "UPDATE";
				String inModStatus = dob.dbmod.res_status;
				String modType = dbResource.getResSubType(con,
						dob.dbmod.mod_res_id);
				dob.dbmod.mod_type = modType;
				dob.dbmod.res_subtype = modType;

				if (modType.equalsIgnoreCase("ASS")) {
					// set upd user
					dob.dbass.res_upd_user = dob.prof.usr_id;
					dob.dbass.upd2(con, dob.prof);
					modId = dob.dbass.mod_res_id;
					modTitle = dob.dbass.res_title;
				} else {
					// set upd user
					dob.dbmod.res_upd_user = dob.prof.usr_id;
					dob.dbmod.upd2(con, dob.prof);
					modId = dob.dbmod.mod_res_id;
					modTitle = dob.dbmod.res_title;
				}
				con.commit();

				String status = MSG_STATUS; 
				
				cwSysMessage e = null;
				if (inModStatus.equalsIgnoreCase(dbModule.RES_STATUS_ON)
						&& dob.dbmod.res_status
								.equalsIgnoreCase(dbModule.RES_STATUS_OFF)
						&& (modType.equalsIgnoreCase(dbModule.MOD_TYPE_TST) || modType
								.equalsIgnoreCase(dbModule.MOD_TYPE_DXT))) {
					// for a standard test or dynamic test
					// if the input status is online but finally it is saved as
					// offline
					// it must be because the test does not have any
					// question/criterion defined
					e = new cwSysMessage("MOD007");
					status = MSG_ERROR;
				} else {
					e = new cwSysMessage("GEN003");
				}

				if (!dob.dbmod.mod_is_public) {
					msgBox(status, con, e, dob, out, modId, modTitle,
							modType, modAction, cosId);
				} else {
					msgBox(status, con, e, dob, out);
				}
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_MOD_LST")) {
			try {
				dbModule dbMod = new dbModule();
				dbMod.res_upd_user = dob.prof.usr_id;
				long mod_res_id;
				EvalAccess evaAcc = new EvalAccess();
				List<ObjectActionLog> logList = new ArrayList<ObjectActionLog>();
				for (int i = 0; i < dob.res_id_lst.length; i++) {
					mod_res_id = Long.parseLong(dob.res_id_lst[i]);

					AcModule acMod = new AcModule(con);
					if (!AccessControlWZB
							.hasRolePrivilege(dob.prof.current_role,
									AclFunction.FTN_AMD_EVN_MAIN)
							&& AccessControlWZB
									.isRoleTcInd(dob.prof.current_role)) {
						throw new cwSysMessage(
								dbResourcePermission.NO_RIGHT_WRITE_MSG);
					}
					String modType = dbResource.getResSubType(con, mod_res_id);
					String modTitle = dbResource.getResTitle(con, mod_res_id);
					String modCode = dbResource.getResCode(con, mod_res_id);
					dbMod.mod_type = modType;
					dbMod.res_subtype = modType;
					dbMod.res_id = mod_res_id;
					dbMod.res_upd_date = dob.res_timestamp_lst[i];
					dbMod.mod_res_id = mod_res_id;
					dbMod.del(con, dob.prof);

					evaAcc.eac_res_id = dbMod.res_id;
					evaAcc.delEvalAccessByRes_ID(con);
					if (modType.equalsIgnoreCase("EVN")) {
						ObjectActionLog log = new ObjectActionLog(mod_res_id,
								modCode, modTitle,
								ObjectActionLog.OBJECT_TYPE_EL,
								ObjectActionLog.OBJECT_ACTION_DEL,
								ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
								dob.prof.getUsr_ent_id(),
								dob.prof.getUsr_last_login_date(),
								dob.prof.getIp());
						logList.add(log);
					}
				}

				con.commit();

				for (ObjectActionLog log : logList) {
					SystemLogContext.saveLog(log,
							SystemLogTypeEnum.OBJECT_ACTION_LOG);
				}

				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SEARCH")
				|| dob.prm_ACTION.equalsIgnoreCase("SEARCH_XML")) {
			// set upd user
			try {
				String result = dbSearchCond.searchAsXML(sess, con,
						dob.dbsearch, dob.page, dob.page_size, dob.prof,
						wizbini.cfgTcEnabled);
				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_QUE_BY_OBJ_PREP")
				|| dob.prm_ACTION
						.equalsIgnoreCase("SEARCH_QUE_BY_OBJ_PREP_XML")) {

			String result = formatXML("", "<search_sub_type>"
					+ dob.dbsearch.search_sub_type + "</search_sub_type>",
					"search_question", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_QUE_BY_OBJ_PREP_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_QUE_BY_OBJ_PREP"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_QUE_BY_OBJ")
				|| dob.prm_ACTION.equalsIgnoreCase("SEARCH_QUE_BY_OBJ_XML")) {
			String result = dbSearchCond.searchQueWDetails(con, dob.dbsearch,
					dob.prof, dob.cur_stylesheet).toString();
			result = formatXML(result, null, "search_question", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_QUE_BY_OBJ_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_QUE_BY_OBJ")) {
				response.setHeader("Cache-Control", "");
				response.setHeader("Pragma", "");
				response.setHeader("Content-Disposition",
						"attachment; filename=export_question" + ".xls;");
				response.setContentType("application/vnd.ms-excel; charset="
						+ static_env.ENCODING);
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SIMPLE_SEARCH")
				|| dob.prm_ACTION.equalsIgnoreCase("SIMPLE_SEARCH_XML")) {
			// set upd user
			try {
				String result = dbSearchCond.simple_searchAsXML(sess, con,
						dob.dbsearch, dob.page, dob.page_size, dob.begin_num,
						dob.end_num, dob.prof, wizbini.cfgTcEnabled);
				if (dob.prm_ACTION.equalsIgnoreCase("SIMPLE_SEARCH_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("SIMPLE_SEARCH"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_RESULT")
				|| dob.prm_ACTION.equalsIgnoreCase("SEARCH_RESULT_XML")) {
			// set upd user
			try {
				String result = dbSearchCond.search_result(sess, con,
						dob.dbsearch, dob.prof);
				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_RESULT_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_RESULT"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}
		/*
		 * else if (dob.prm_ACTION.equalsIgnoreCase("GET_ITM_TNA") ||
		 * dob.prm_ACTION.equalsIgnoreCase("GET_ITM_TNA_XML")) {
		 * 
		 * String result;
		 * 
		 * long modId = DbItemResources.getResId(con, dob.itm_id, "TNA");
		 * dob.dbmod.res_id = modId; dob.dbmod.mod_res_id = modId;
		 * dob.dbmod.get(con);
		 * 
		 * Survey mySvy = new Survey(dob.dbmod); mySvy.res_upd_user =
		 * dob.prof.usr_id; mySvy.getExtendXML(con); result = mySvy.asXML(con,
		 * dob.prof, dob.dbdpo.dpo_view);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_ITM_TNA_XML"))
		 * static_env.outputXML(out, result);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_ITM_TNA"))
		 * generalAsHtml(result, out, dob);
		 * 
		 * }
		 */

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_STATUS")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_MOD_STATUS_XML")) {
			String result = dbModuleEvaluation.getStatus(con, dob.prof,
					dob.dbmod.mod_res_id, dob.tkh_id, dob.dbcos.res_id);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_STATUS_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_STATUS"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_MOD_XML")) {
			// get sso_link info
			String ssoXml = dbSSOLink.ssoLinkAsXML(dob.prof.root_id, wizbini);

			if (dob.upd_tst_que) {
				try {
					dbModule mod = new dbModule();
					mod.res_id = dob.dbres.res_id;
					mod.mod_res_id = dob.dbres.res_id;
					mod.checkStat(con);
					if (wizbini.cfgTcEnabled) {
						if (dob.robs != null && dob.robs[0] != null) {

							Long objId = new Long(dob.robs[0]);
							String[] res = dbResourceContent
									.getResIdByContentId(con,
											dob.dbmod.mod_res_id);
							if (res != null) {
								if (res[1].equals(dbResource.RES_TYPE_COS)) {
									int share = dbObjective.getObjIsShare(con,
											objId.longValue());
									if (1 != share) {
										long itm_id = dbCourse.getCosItemId(
												con, Long.parseLong(res[0]));
										long itm_tcr_id = aeItem.getTcrId(con,
												itm_id, dob.prof.root_ent_id);
										long obj_tcr_id = dbObjective
												.getObjTcrId(con,
														objId.longValue());
										// ln模式修改抽题条件取消权限判断
										/*
										 * if (itm_tcr_id != obj_tcr_id) { //
										 * dob.url_fail = //
										 * "javascript:wb_utils_gen_home()";
										 * throw new qdbErrMessage("OBJ001"); }
										 */
									}
								}
							}
						}

					}
				} catch (qdbErrMessage e) {
					con.rollback();
					msgBox(MSG_ERROR, con, e, dob, out);
					return;
				}
			}

			if (dob.dbmod.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
				dob.dbmod.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con,dob.dbmod.res_id, dob.prof.usr_ent_id);
			}

			if (dob.dbmod.tkh_id != 0
					&& dob.dbmod.tkh_id != DbTrackingHistory.TKH_ID_NOT_FOUND
					&& DbTrackingHistory.getAppTrackingIDByCos(con,
							dob.dbmod.tkh_id, dob.prof.usr_ent_id,
							dbModule.getCosId(con, dob.dbmod.mod_res_id),
							dob.dbmod.mod_res_id) != 1) {
				msgBox(MSG_ERROR, con, new cwSysMessage("USR033"), dob, out);
				return;
			}
			AcModule acMod = new AcModule(con);
			// if (!acMod.checkModifyPermission(dob.prof,
			// dob.dbmod.mod_res_id)){
			// throw new qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
			// }
			if (!acMod.checkReadPermission(dob.prof, dob.dbmod.mod_res_id)) {
				throw new cwSysMessage(dbResourcePermission.NO_RIGHT_READ_MSG);
			}
			// dob.dbmod.checkReadPermission(con, dob.prof);

			String result;
			dob.dbmod.get(con);

			if (dob.dbmod.res_subtype.equals("ASS")) {
				// set upd user
				dob.dbass.res_upd_user = dob.prof.usr_id;
				dob.dbass.get(con);
				result = dob.dbass.asXML(con, dob.prof, dob.dbdpo.dpo_view,
						dob.dbmod.tkh_id, ssoXml);
			}
			/*
			 * else if (dob.dbmod.res_subtype.equals("EVN") ||
			 * dob.dbmod.res_subtype.equals("TNA")) { if
			 * ((dob.dbmod.mod_type).equalsIgnoreCase("TNA")) { AcTNA acTNA =
			 * new AcTNA(con); if(!acTNA.hasReadPrivilege(dob.dbmod.mod_res_id,
			 * dob.prof.usr_ent_id, dob.prof.current_role, dob.prof.usrGroups))
			 * {
			 * 
			 * throw new cwSysMessage(dbResourcePermission.NO_RIGHT_READ_MSG); }
			 * } // set upd user Survey mySvy = new Survey(dob.dbmod);
			 * mySvy.res_upd_user = dob.prof.usr_id; mySvy.getExtendXML(con);
			 * result = mySvy.asXML(con, dob.prof, dob.dbdpo.dpo_view); }
			 */
			else if (dob.dbmod.res_subtype.equals("FOR")
					|| dob.dbmod.res_subtype.equals("DIS")) {
				long cosId = dbModule.getCosId(con, dob.dbmod.mod_res_id);

				dob.dbforum = new dbForum(dob.dbmod);
				dob.dbforum.res_upd_user = dob.prof.usr_id;
				// dob.dbforum.get(con);
				if (wizbini.cfgSysSetupadv.getForum().isMarkMsg()) {
					dob.dbforum.MARK_MSG = 1;
				} else {
					dob.dbforum.MARK_MSG = 0;
				}
				if (dob.dbforTopic.fto_id == 0) {
					result = dob.dbforum.outputTopicsAsXML(con, sess, dob.prof,
							dob.dbdpo.dpo_view, cosId, dob.cwPage, ssoXml);
				} else {
					result = dob.dbforum.outputMsgsAsXML(con, sess, dob.prof,
							dob.dbdpo.dpo_view, dob.dbforTopic.fto_id,
							dob.unthread, dob.sort_order, dob.page,
							dob.page_size, dob.msg_length, cosId, ssoXml);
				}
				// answer page variants
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.dbmod.mod_res_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				acPageVariant.prof = dob.prof;
				String metaXML = acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet));
				if (metaXML == null || metaXML.length() == 0) {
					metaXML = acPageVariant
							.answerPageVariantAsXML(AcXslQuestion
									.getOneXslQuestions(dob.cur_stylesheet,
											xslQuestions));
				}
				if (metaXML != null && metaXML.length() > 0) {
					result = formatXML(
							result.substring(result.indexOf("?>") + 2),
							metaXML, "forum", dob.prof);
				}
				// result = dob.dbforum.asXML(con, dob.prof, dob.dbdpo.dpo_view,
				// dob.dbforTopic.fto_id,
				// dob.isExtend, dob.unthread, dob.sort_order);
			} else if (dob.dbmod.res_subtype.equals("FAQ")) {
				dob.dbfaq = new dbFaq(dob.dbass);
				dob.dbfaq.res_upd_user = dob.prof.usr_id;
				dob.dbfaq.get(con);

				/*
				 * if (static_env.INI_MARK_FORUM_MSG != null &&
				 * static_env.INI_MARK_FORUM_MSG.equalsIgnoreCase("YES")) {
				 * dob.dbfaq.MARK_MSG = 1; } else { dob.dbfaq.MARK_MSG = 0; }
				 */

				if (dob.dbfaqTopic.fto_id == 0) {
					result = dob.dbfaq.outputTopicsAsXML(con, sess, dob.prof,
							dob.dbdpo.dpo_view, dob.sort_order, dob.page,
							dob.page_size, ssoXml);
				} else {
					result = dob.dbfaq.outputMsgsAsXML(con, sess, dob.prof,
							dob.dbdpo.dpo_view, dob.dbfaqTopic.fto_id,
							dob.dbfaqMsg.fmg_id, dob.commentOn, dob.sort_order,
							dob.page, dob.page_size, ssoXml);
				}
			} else if (dob.dbmod.res_subtype.equals("CHT")
					|| dob.dbmod.res_subtype.equals("VCR")) {
				// set upd user
				dob.dbchat.res_upd_user = dob.prof.usr_id;
				dob.dbchat.get(con);
				result = dob.dbchat.asXML(con, dob.prof, dob.dbdpo.dpo_view,
						ssoXml);
			} else if (dbEvent.isEventType(dob.dbmod.res_subtype)) {
				// set upd user
				dob.dbevt.res_upd_user = dob.prof.usr_id;
				dob.dbevt.get(con);

				result = dob.dbevt.asXML(con, dob.prof, dob.dbdpo.dpo_view,
						ssoXml);
			} else {
				// set upd user
				dob.dbmod.res_upd_user = dob.prof.usr_id;
				dob.dbmod.get(con);
				EvalAccess evaAcc = new EvalAccess();
				evaAcc.eac_res_id = dob.dbmod.res_id;
				String other_xml = evaAcc.getTargetDisplayByRes_IDASXML(con);
				result = dob.dbmod.asXML(con, dob.prof, dob.dbdpo.dpo_view,
						other_xml, wizbini.cfgTcEnabled);
			}
			if (result != null) {
				String closed_tag = "</module>";
				int closed_tag_idx = result.lastIndexOf(closed_tag);
				if (closed_tag_idx > -1) {
					String end_tag = result.substring(closed_tag_idx
							+ closed_tag.length(), result.length());
					result = result.substring(0, closed_tag_idx);
					result += "<is_relation_com>"
							+ dob.dbmod.isModrelationCom(con,
									dob.dbmod.mod_res_id)
							+ "</is_relation_com>";
					result += "<res_dir_url>" +  cwUtils.getFileURL(qdbAction.static_env.INI_RES_DIR_URL) + "</res_dir_url>" + dbUtils.NEWL;
					result += "<itm_type>"
							+ aeItem.getItemType(con, dbModule.getCosId(con,
									dob.dbmod.mod_res_id)) + "</itm_type>";
					result += closed_tag;
					if (end_tag != null) {
						result += end_tag;
					}
				}
			}
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD"))
				generalAsHtml(result, out, dob);
			/*
			 * } else { response.sendRedirect(dob.url_fail); }
			 */

		} else if (dob.prm_ACTION.equalsIgnoreCase("get_sso_mod")
				|| dob.prm_ACTION.equalsIgnoreCase("get_sso_mod_xml")) {
			dbModule mod = new dbModule();
			mod.res_id = dob.dbres.res_id;
			mod.mod_res_id = dob.dbres.res_id;
			mod.get(con);

			if (!AccessControlWZB.hasRole(con, dob.prof.usr_ent_id,
					AccessControlWZB.ROL_EXT_ID_NLRN)) {
				throw new cwSysMessage("USR029");
			}

			// check mod status
			if (!mod.getModStatus(con).equals("ON")) {
				throw new cwSysMessage("MOD014");
			}

			if (!mod.isEffTime(con)) {
				throw new cwSysMessage("MOD014");
			}

			// get Module Prerequisite module information
			ModulePrerequisiteManagement modulemrerequisitemanagement = new ModulePrerequisiteManagement();
			if (!modulemrerequisitemanagement.hasCompletePreMod(con,
					dob.prof.usr_ent_id, dob.dbmod.tkh_id, dob.dbres.res_id)) {
				throw new cwSysMessage("MOD015");
			}

			// if the course contine the module is offline,throw a systemmessage
			if (!dbResource.getResStatus(con,
					dbModule.getCosId(con, mod.res_id)).equals(
					dbResource.RES_STATUS_ON)) {
				throw new cwSysMessage(dbResourcePermission.NO_RIGHT_READ_MSG);
			}

			AcModule acMod = new AcModule(con);
			if (!acMod.checkReadPermission(dob.prof, mod.mod_res_id)) {
				throw new cwSysMessage(dbResourcePermission.NO_RIGHT_READ_MSG);
			}

			String result = mod.getSSOXML(con, dob.prof, dob.sso_refer,
					dob.sso_lms_url);

			if (dob.prm_ACTION.equalsIgnoreCase("get_sso_mod_xml"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("get_sso_mod"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("get_sso_link_query")
				|| dob.prm_ACTION.equalsIgnoreCase("get_sso_link_query_xml")) {
			dbSSOLink sso = new dbSSOLink();
			String result = sso.getSSOHomeAsXML(dob.prof, wizbini);

			if (dob.prm_ACTION.equalsIgnoreCase("get_sso_link_query_xml"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("get_sso_link_query"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_BUILDER")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_MOD_BUILDER_XML")) {

			String result = dob.dbcos.getModBuilderXML(con, dob.prof,
					dob.lesson_count, dob.lesson_current, dob.types);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_BUILDER_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_BUILDER"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("SAVE_MOD_BUILDER")) {
			try {
				AcCourse acCos = new AcCourse(con);
				if (!acCos
						.checkModifyPermission(dob.prof, dob.dbcos.cos_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbcos
						.saveWizard(con, dob.prof, dob.wizard_data, static_env);

				con.commit();

				// response.sendRedirect(dob.url_success);
				cwSysMessage e = new cwSysMessage("GEN004");
				msgBox(MSG_STATUS, con, e, dob, out);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_RES_IMSCP")) {
			/*
			 * long cosId = dbModule.getCosId(con,dob.dbmod.mod_res_id); if
			 * (dob.prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN) ||
			 * dob.prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER) ||
			 * dbResourcePermission.hasPermission(con,cosId,
			 * dob.prof,dbResourcePermission.RIGHT_READ)) {
			 */
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod.checkReadPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				String xmlPath = wizbini.getFileUploadResDirAbs()
						+ cwUtils.SLASH + dob.dbmod.mod_res_id + cwUtils.SLASH
						+ "imsmanifest.xml";

				static_env.processFiles(xmlPath, dob.cur_stylesheet, out,
						dob.xsl_root);
				return;
			} catch (Exception e) {
				// do nothing
			}
			/* } */
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_EMAILS")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_EMAILS_XML")) {

			long[] ent_ids = dbUserGroup.constructEntId(con, dob.ent_ids);
			Vector emailVec = dbRegUser.getEmailVec(con, ent_ids,
					Application.MAIL_SERVER_ACCOUNT_TYPE);
			StringBuffer xml = new StringBuffer();
			// xml.append("<?xml version=\"1.0\" encoding=\"").append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>").append(dbUtils.NEWL);
			xml.append("<emails>").append(dbUtils.NEWL);
			for (int i = 0; i < emailVec.size(); i++)
				xml.append("<email>")
						.append(dbUtils.esc4XML((String) emailVec.elementAt(i)))
						.append("</email>").append(dbUtils.NEWL);
			xml.append("</emails>");
			xml.append("<input>").append(ent_ids.length).append("</input>");
			xml.append("<output>").append(emailVec.size()).append("</output>");

			String xmlString = formatXML(xml.toString(), null, "message",
					dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_EMAILS"))
				generalAsHtml(xmlString, out, dob);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_EMAILS_XML"))
				static_env.outputXML(out, xmlString);

		}

		/*
		 * else if (dob.prm_ACTION.equalsIgnoreCase("GET_ASS") ||
		 * dob.prm_ACTION.equalsIgnoreCase("GET_ASS_XML")) { // set upd user
		 * dob.dbass.res_upd_user = dob.prof.usr_id; try { long cosId =
		 * dob.dbass.getCosId(con,dob.dbass.ass_res_id);
		 * 
		 * if (dob.prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN) ||
		 * dob.prof.sysRoles.contains(dbUserGroup.USG_ROLE_TEACHER) ||
		 * dbResourcePermission.hasPermission(con,cosId,
		 * dob.prof,dbResourcePermission.RIGHT_READ)) {
		 * 
		 * dob.dbass.get(con);
		 * 
		 * String result = dob.dbass.asXML(con, dob.prof);
		 * 
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_ASS_XML"))
		 * static_env.outputXML(out, result);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_ASS")) generalAsHtml(result,
		 * out, dob); } else { response.sendRedirect(dob.url_fail); } }
		 * catch(qdbErrMessage e) { con.rollback(); msgBox(MSG_ERROR, con, e,
		 * dob, out); return; } }
		 */
		else if (dob.prm_ACTION.equalsIgnoreCase("UPD_MOD_STS")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbmod.updateStatus(con, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_MOD_OBJ")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}
				/*
				 * long objId = Long.parseLong(dob.robs[0]);
				 * dob.dbmod.insObj(con,objId, dob.prof); con.commit();
				 */
				Long objId = new Long(dob.robs[0]);
				sess.setAttribute(SESS_MOD_OBJ_ID, objId);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_MOD_OBJ")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;
			Vector clsModId = dob.dbmod.getChildModResId(con);
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}
				dob.dbmod.get(con);
				if (dob.dbmod.res_subtype
						.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)
						|| dob.dbmod.res_subtype
								.equalsIgnoreCase(dbModule.MOD_TYPE_STX)) {
					dob.dbmod.delMsp(con, dob.robs, dob.prof);
					dob.dbmod.updMaxScoreForChild(con);
				} else {
					for (int i = 0; i < clsModId.size(); i++) {
						dbModule dbmod = ((dbModule) clsModId.get(i));
						dbmod.get(con);
						dbmod.delObj(con, dob.robs, dob.prof, false);
					}
					dob.dbmod.delObj(con, dob.robs, dob.prof, true);
				}
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_MOD_QUE")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;
			Vector clsModId = dob.dbmod.getChildModResId(con);
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				// get the objective id from session
				// the objective id should be set by cmd "INS_MOD_OBJ"
				Long objId = (Long) sess.getAttribute(SESS_MOD_OBJ_ID);
				if (objId != null) {
					long obj_id = objId.longValue();
					if (obj_id > 0 && dob.que_id_lst.length > 0) {
						// if the objective id can be found from session
						// check if the questions are in the same objective
						// as the one in session
						// Vector vObjId = dbResourceObjective.getObjId(con,
						// Long.parseLong(dob.que_id_lst[0]));
						dbResourceObjective rob = new dbResourceObjective();
						rob.rob_res_id = Long.parseLong(dob.que_id_lst[0]);
						rob.rob_obj_id = obj_id;
						if (rob.doesExist(con)) {
							// insert the objective to database if the questions
							// are in the same objective as the one in session
							dob.dbmod.insObj(con, obj_id, dob.prof);
							for (int i = 0; i < clsModId.size(); i++) {
								dbModule dbmod = ((dbModule) clsModId.get(i));
								dbmod.get(con);
								dbmod.insObj(con, obj_id, dob.prof);
							}
						}
					}
					sess.removeAttribute(SESS_MOD_OBJ_ID);
				}
				dob.dbmod.insQ(con, dob.que_id_lst,
						wizbini.getFileUploadResDirAbs(), dob.prof, clsModId);

				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("ADD_MOD_QUE")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				long resId = dob.dbmod.addQ(con, dob.dbque, dob.robs, dob.prof);
				dob.dbque.res_id = resId;
				dob.dbque.que_res_id = resId;
				dob.dbque.get(con);

				if (dob.bFileUpload == true)
					procUploadedFiles(con, request, resId, dob.tmpUploadDir,
							dob);
				con.commit();

				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_STATUS, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_MOD_QUE")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;
			dob.dbmod.mod_mod_res_id_parent = dob.dbmod.mod_res_id;
			Vector clsModId = dob.dbmod.getChildModResId(con);
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}
				for (int i = 0; i < clsModId.size(); i++) {
					dbModule dbmod = ((dbModule) clsModId.get(i));
					dbmod.get(con);
					dbmod.delQ(con, dob.que_id_lst, dob.prof, false);
				}
				dob.dbmod.delQ(con, dob.que_id_lst, dob.prof, true);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("ORDER_MOD_QUE")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;
			Vector clsModId = dob.dbmod.getChildModResId(con);
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}
				for (int i = 0; i < clsModId.size(); i++) {
					dbModule dbmod = ((dbModule) clsModId.get(i));
					dbmod.get(con);
					dbmod.reorderQ(con, dob.que_id_lst, dob.que_order_lst,
							dob.prof);
				}
				dob.dbmod.reorderQ(con, dob.que_id_lst, dob.que_order_lst,
						dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SCORE_MOD_QUE")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbmod.assignScore(con, dob.que_id_lst,
						dob.que_multiplier_lst, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_QUIZ")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_QUIZ_XML")) {

			qdbQuiz qUiz = new qdbQuiz(con, dob.subId, dob.sub);

			dbQuestion dbque = new dbQuestion();

			if (dob.dbque.que_type != null && dob.dbque.que_type.length() > 0)
				dbque.que_type = dob.dbque.que_type;
			else
				dbque.que_type = dbQuestion.QUE_TYPE_MULTI;

			if (dob.dbque.res_privilege != null
					&& dob.dbque.res_privilege.length() > 0)
				dbque.res_privilege = dob.dbque.res_privilege;
			else
				dbque.res_privilege = dbResource.RES_PRIV_CW;

			dbque.que_media_ind = false;
			qUiz.setQOption(con, dbque);

			// shuffle questions
			long left = qUiz.getQLeftCount(con);
			if (left < dob.size)
				qUiz.recycleQ(con);

			Timestamp curTime = dbUtils.getTime(con);

			String result = "";
			result = qUiz.getNextQuizInXML(con, dob.prof, curTime, dob.size,
					dob.template);

			sess.setAttribute(QUIZ_TIMESTAMP, curTime);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_QUIZ_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_QUIZ"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_TST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_TST_XML")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;

			try {
				// access control
				AcModule acMod = new AcModule(con);
				if (!acMod.checkReadPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}

				float time_limit = dob.dbmod.res_duration;
				dob.dbmod.get(con);

				if (dob.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con,
							dob.dbmod.mod_res_id, dob.prof.usr_ent_id);
				}

				if (dob.dbmod.mod_eff_end_datetime.before(cwSQL.getTime(con))
						&& dob.prof.current_role.equalsIgnoreCase("NLRN_1")) {
					throw new qdbErrMessage("MOD022");
				}

				if (!dob.res_preview_ind
						&& dob.dbmod.mod_max_usr_attempt > 0
						&& !dob.prof.common_role_id.equalsIgnoreCase("TADM")
						&& dbModuleEvaluation.getTotalAttempt(con,
								dob.dbmod.mod_res_id, dob.prof.usr_ent_id,
								dob.tkh_id) >= dob.dbmod.mod_max_usr_attempt) {
					throw new qdbErrMessage("PGR005");
				}

				// for public evaluation, because of the evaluation doesn't have
				// tracking history.
				boolean isNotEvaluation = true;
				if (dbModule.MOD_TYPE_EVN.equalsIgnoreCase(dob.dbmod.mod_type)) {
					isNotEvaluation = false;
				}

				if (!dob.res_preview_ind
						&& isNotEvaluation
						&& DbTrackingHistory.getAppTrackingIDByCos(con,
								dob.tkh_id, dob.prof.usr_ent_id,
								dbModule.getCosId(con, dob.dbmod.mod_res_id),
								dob.dbmod.mod_res_id) != 1) {
					throw new qdbErrMessage("USR033");
				}

				if (!dob.res_preview_ind
						&& dob.dbmod.mod_sub_after_passed_ind == 0) {
					dbModuleEvaluation mov = new dbModuleEvaluation();
					mov.mov_ent_id = dob.prof.usr_ent_id;
					mov.mov_tkh_id = dob.tkh_id;
					mov.mov_mod_id = dob.dbmod.mod_res_id;
					if (mov.get(con)
							&& mov.mov_status
									.equals(dbModuleEvaluation.STATUS_PASSED)) {
						throw new qdbErrMessage("PGR008");
					}
				}

				if (dob.dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_DXT)
						|| dob.dbmod.mod_type
								.equalsIgnoreCase(dbModule.MOD_TYPE_TST)) {
					String pgr_status = dbModuleEvaluation.getStatus(con,
							dob.prof, dob.dbmod.mod_res_id, dob.dbcos.res_id);
					if (pgr_status != null && pgr_status.equalsIgnoreCase("I")) {
						throw new qdbErrMessage("PGR010");
					} else if (pgr_status != null
							&& pgr_status.equalsIgnoreCase("P")
							&& dob.dbmod.mod_sub_after_passed_ind == 0) {
						throw new qdbErrMessage("PGR011");
					}
				}
				// Contains the question ids in the test
				Vector queIdVec = new Vector();
				String result = dob.dbmod.genTestAsXML(con, dob.prof,
						dob.dbmsp, dob.robs, time_limit,
						wizbini.getFileUploadResDirAbs(), queIdVec, dob.tkh_id);
				StringBuffer resultBuf = new StringBuffer(result);
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				String metaXML = "<meta>"
						+ dob.prof.asXML()
						+ acPageVariant
								.answerPageVariantAsXML((String[]) xslQuestions
										.get(dob.cur_stylesheet)) + "</meta>";
				dbRegUser regUser = new dbRegUser();
				regUser.usr_ent_id = dob.prof.usr_ent_id;
				regUser.get(con);
				metaXML += "<user_photo>"
						+ wizbini.getUserPhotoPath(dob.prof,
								dob.prof.usr_ent_id, regUser.urx_extra_43)
						+ "</user_photo>";
				resultBuf.insert(result.indexOf("<key>"), metaXML);
				result = resultBuf.toString();

				// save progress tracking data
				if (!dob.res_preview_ind
						&& (dob.dbmod.mod_type
								.equalsIgnoreCase(dbModule.MOD_TYPE_TST) || dob.dbmod.mod_type
								.equalsIgnoreCase(dbModule.MOD_TYPE_DXT))) {
					dob.dbmod.saveTrackingDataAtLaunch(con, dob.prof,
							dob.tkh_id, dob.dbcos.cos_res_id);
				}

				// Append queIdVec to the session permission list
				Vector sessVec = (Vector) sess.getAttribute(AUTH_QUE_IDS);
				if (sessVec == null) {
					sessVec = queIdVec;
				} else if (queIdVec != null) {
					for (int i = 0; i < queIdVec.size(); i++) {
						if (!sessVec.contains(queIdVec.elementAt(i)))
							sessVec.addElement(queIdVec.elementAt(i));
					}
				}

				if (sessVec != null)
					sess.setAttribute(AUTH_QUE_IDS, sessVec);

				con.commit();

				if (dob.cur_stylesheet == null || dob.cur_stylesheet.equals("")) {
					String cleanXML = cwUtils.escNull(result);
					out.println(cleanXML);
					return;
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_TST_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_TST"))
					generalAsHtml(result, out, dob);
				/*
				 * } else { response.sendRedirect(dob.url_fail); }
				 */
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("CHECK_TST_TEST")
				|| dob.prm_ACTION.equalsIgnoreCase("CHECK_TST_TEST_XML")) {
			try {
				StringBuffer xml = new StringBuffer();
				if (dob.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con,
							dob.dbmod.mod_res_id, dob.prof.usr_ent_id);
				}

				if (!dob.res_preview_ind
						&& DbTrackingHistory.getAppTrackingIDByCos(con,
								dob.tkh_id, dob.prof.usr_ent_id,
								dbModule.getCosId(con, dob.dbmod.mod_res_id),
								dob.dbmod.mod_res_id) != 1) {
					throw new qdbErrMessage("USR033");
				}

				boolean isRestore = true;
				dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
				dbpas.pasTkhId = dob.tkh_id;
				dbpas.pasResId = dob.dbmod.mod_res_id;
				dob.dbmod.get(con);
				/*
				 * HashMap mod_cache =
				 * wizbCacheManager.getInstance().getCachedHashmap
				 * ("mod_check_test", true); if(mod_cache.get(new
				 * Long(dob.dbmod.mod_res_id))==null){ dob.dbmod.get(con);
				 * mod_cache.put(new Long(dob.dbmod.mod_res_id), dob.dbmod); }
				 * else { dob.dbmod = (dbModule)mod_cache.get(new
				 * Long(dob.dbmod.mod_res_id)); }
				 */
				dbModule dbmodule = new dbModule();
				dbModuleEvaluation mov = new dbModuleEvaluation();
				mov.mov_ent_id = dob.prof.usr_ent_id;
				mov.mov_tkh_id = dob.tkh_id;
				mov.mov_mod_id = dob.dbmod.mod_res_id;
				mov.get(con);
				/*
				 * dbTemplate dbtpl = new dbTemplate(); dbtpl.tpl_name =
				 * dob.dbmod.res_tpl_name; dbtpl.tpl_lan = dob.prof.label_lan;
				 * dbtpl.get(con);
				 */
				if (dbpas.chkforExist(con)) {
					isRestore = true;
				} else {
					isRestore = false;
				}
				xml.append("<header>");
				xml.append("<mod_id>").append(dob.dbmod.mod_res_id)
						.append("</mod_id>");
				xml.append("<mod_type>").append(dob.dbmod.mod_type)
						.append("</mod_type>");
				xml.append("<mod_tpl_name>").append(dob.cur_stylesheet)
						.append("</mod_tpl_name>");
				xml.append("<course_id>").append(dob.dbcos.cos_res_id)
						.append("</course_id>");
				xml.append("<tkh_id>").append(dob.tkh_id).append("</tkh_id>");
				xml.append("</header>");
				xml.append("<isRestore>").append(isRestore)
						.append("</isRestore>");
				xml.append("<isStartTest>").append(dob.is_start_test)
						.append("</isStartTest>");
				xml.append("<test_style>")
						.append(dbmodule.getTextStyle(con,
								(int) dob.dbmod.mod_res_id))
						.append("</test_style>");
				if (dob.dbmod.mod_managed_ind == 1) {
					if (dob.dbmod.mod_started_ind != 1) {
						xml.append("<not_started>").append(true)
								.append("</not_started>");
					}
				}
				if (dob.is_sso) {
					xml.append("<isSso>").append(dob.is_sso).append("</isSso>");
				}
				if (!isRestore && dob.is_start_test) {
					dob.dbmod.res_upd_user = dob.prof.usr_id;

					// dob.dbmod.get(con);

					if (!dob.res_preview_ind
							&& dob.dbmod.mod_max_usr_attempt > 0
							&& mov.mov_total_attempt >= dob.dbmod.mod_max_usr_attempt) {
						throw new qdbErrMessage("PGR005");
					}

					dbProgress pgr = new dbProgress();
					// long numEssayNotMarked = pgr.numEssayNotMarked(con,
					// dob.dbmod.mod_res_id, dob.tkh_id,
					// dbProgress.getLastAttemptNbr(con, dob.prof.usr_id,
					// dob.dbmod.mod_res_id, dob.tkh_id));
					if (mov.mov_not_mark_ind > 0) {
						throw new qdbErrMessage("ESS001");
					}

					String status = mov.mov_status;
					if (status == null
							|| !status.equals(dbModuleEvaluation.STATUS_PASSED)
							|| (status.equals(dbModuleEvaluation.STATUS_PASSED) && dob.dbmod.mod_sub_after_passed_ind == 1)) {
						xml.append("<begin_cal>").append(true)
								.append("</begin_cal>");
					}
					if (!dob.res_preview_ind
							&& dob.dbmod.mod_sub_after_passed_ind == 0) {
						if (dbModuleEvaluation.STATUS_PASSED
								.equals(mov.mov_status)) {
							throw new qdbErrMessage("PGR008");
						}
					}
				}
				String result = formatXML(xml.toString(), null, "applyeasy",
						dob.prof);
				if (dob.prm_ACTION.equalsIgnoreCase("CHECK_TST_TEST_XML")) {
					static_env.outputXML(out, result);
				} else {
					generalAsHtml(result, out, dob);
				}
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_TST_TEST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_TST_TEST_HTML")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_TST_TEST_XML")) {
			// set upd user
			dob.dbmod.res_upd_user = dob.prof.usr_id;
			HashMap test_mod_cache = wizbCacheManager.getInstance()
					.getCachedHashmap("TEST_MOD_CACHE_GET", true);
			boolean isExam = false;
			long parent_itm_id = 0;
			dob.dbmod.get(con);
			if (test_mod_cache.get(new Long(dob.dbmod.mod_res_id)) == null) {
				aeItem parent_item = aeItem.getItemByContentMod(con,
						dob.dbmod.mod_res_id);
				isExam = aeItem.isOnlineExam(parent_item);
				parent_itm_id = parent_item.itm_id;
				Object[] obj_cache = { new Boolean(isExam),
						new Long(parent_item.itm_id), dob.dbmod };
				test_mod_cache.put(new Long(dob.dbmod.mod_res_id), obj_cache);
			} else {
				Object[] obj_cache = (Object[]) test_mod_cache.get(new Long(
						dob.dbmod.mod_res_id));
				isExam = ((Boolean) obj_cache[0]).booleanValue();
				parent_itm_id = ((Long) obj_cache[1]).longValue();
				// dob.dbmod =(dbModule) obj_cache[2];
			}

			ExportController controller = new ExportController();
			try {

				float time_limit = dob.dbmod.res_duration;
				if (dob.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con,
							dob.dbmod.mod_res_id, dob.prof.usr_ent_id);
				}

				Timestamp curDate = cwSQL.getTime(con);
				if (!dob.res_preview_ind && dob.dbmod != null
						&& dob.dbmod.mod_eff_end_datetime != null) {
					// 在线模块学习结束时间已过
					long end_time = (dob.dbmod.mod_eff_end_datetime.getTime() - curDate
							.getTime()) / 1000;
					if (end_time <= 0) {
						throw new cwSysMessage("PGR013");
					}
				}

				if (!dob.res_preview_ind
						&& DbTrackingHistory.getAppTrackingIDByCos(con,
								dob.tkh_id, dob.prof.usr_ent_id,
								dbModule.getCosId(con, dob.dbmod.mod_res_id),
								dob.dbmod.mod_res_id) != 1) {
					throw new qdbErrMessage("USR033");
				}

				String result = "<?xml version=\"1.0\" encoding=\""
						+ dbUtils.ENC_UTF + "\" ?>" + dbUtils.NEWL;
				if (dob.prm_ACTION.equalsIgnoreCase("GET_TST_TEST")) {
					result += "<?xml:stylesheet type=\"text/xsl\" href=\"..\\xsl\\"
							+ dob.cur_stylesheet + "\"?>" + dbUtils.NEWL;
				}

				if (sess.getAttribute(dob.window_name
						+ LearnerRptExporter.EXPORT_CONTROLLER) != null) {
					sess.removeAttribute(dob.window_name
							+ LearnerRptExporter.EXPORT_CONTROLLER);
					sess.setAttribute(dob.window_name
							+ LearnerRptExporter.EXPORT_CONTROLLER, controller);
				} else {
					sess.setAttribute(dob.window_name
							+ LearnerRptExporter.EXPORT_CONTROLLER, controller);
				}

				// ----------------------------------
				dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
				dbpas.pasTkhId = dob.tkh_id;
				dbpas.pasResId = dob.dbmod.mod_res_id;
				dbpas.pasTimeLeft = ((long) time_limit) * 60;
				dbpas.pasFlag = null;
				dbpas.pas_create_usr_id = dob.prof.usr_id;
				dbpas.pas_update_usr_id = dob.prof.usr_id;
				// -----------------------------------

				if (!dob.res_preview_ind
						&& dob.dbmod.mod_max_usr_attempt > 0
						&& dbModuleEvaluation.getTotalAttempt(con,
								dob.dbmod.mod_res_id, dob.prof.usr_ent_id,
								dob.tkh_id) >= dob.dbmod.mod_max_usr_attempt
						&& !dbpas.chkforExist(con)) {
					throw new qdbErrMessage("PGR005");
				} else {

					Vector queIdVec = new Vector();

					result += dob.dbmod.genTestAsXML_test(con, dob.prof,
							dob.dbmsp, dob.robs, time_limit,
							wizbini.getFileUploadResDirAbs(), queIdVec,
							dob.tkh_id, false, true, controller, tests_memory,
							sess, 0);

					StringBuffer resultBuf = new StringBuffer(result);
					AcPageVariant acPageVariant = new AcPageVariant(con);
					acPageVariant.ent_id = dob.prof.usr_ent_id;
					acPageVariant.rol_ext_id = dob.prof.current_role;
					String metaXML = "<meta>"
							+ dob.prof.asXML()
							+ acPageVariant
									.answerPageVariantAsXML((String[]) xslQuestions
											.get(dob.cur_stylesheet))
							+ "</meta>";
					dbRegUser regUser = new dbRegUser();
					regUser.usr_ent_id = dob.prof.usr_ent_id;
					regUser.get(con);
					metaXML += "<user_photo>"
							+ wizbini.getUserPhotoPath(dob.prof,
									dob.prof.usr_ent_id, regUser.urx_extra_43)
							+ "</user_photo>";
					resultBuf.insert(result.indexOf("<key>"), metaXML);
					result = resultBuf.toString();

					// save progress tracking data
					if (!dob.res_preview_ind
							&& (dob.dbmod.mod_type
									.equalsIgnoreCase(dbModule.MOD_TYPE_TST) || dob.dbmod.mod_type
									.equalsIgnoreCase(dbModule.MOD_TYPE_DXT))) {
						if (dob.dbmod.mod_managed_ind == 1
								&& dob.dbmod.mod_started_ind == 0) {
							throw new cwSysMessage("GEN000");
						}
						dob.dbmod.saveTrackingDataAtLaunch(con, dob.prof,
								dob.tkh_id, dob.dbcos.cos_res_id);
					}

					if (!dbpas.chkforExist(con)) {
						dbpas.ins(con, cwSQL.getTime(con));
					}

					// Append queIdVec to the session permission list
					Vector sessVec = (Vector) sess.getAttribute(AUTH_QUE_IDS);
					if (sessVec == null) {
						sessVec = queIdVec;
					} else if (queIdVec != null) {
						for (int i = 0; i < queIdVec.size(); i++) {
							if (!sessVec.contains(queIdVec.elementAt(i)))
								sessVec.addElement(queIdVec.elementAt(i));
						}
					}

					if (sessVec != null)
						sess.setAttribute(AUTH_QUE_IDS, sessVec);

					con.commit();
					if (dob.cur_stylesheet == null
							|| dob.cur_stylesheet.equals("")) {
						String cleanXML = cwUtils.escNull(result);
						out.println(cleanXML);
						return;
					}

					// 判断是否“网上考试”或者“混合式考试”
					if (isExam) {
						ExamController.putExamInfo(con, dob.prof.usr_ent_id,
								parent_itm_id, dob.prof.usr_display_bil);
						ExamController.createExamSessListener(
								dob.prof.usr_ent_id, sess);
					}
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_TST_TEST")
						|| dob.prm_ACTION.equalsIgnoreCase("GET_TST_TEST_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_TST_TEST_HTML"))
					generalAsHtml(result, out, dob);

				// in order to make sure the progress reach 100% only when this
				// cmd ends
				// please refer to dbQuestion.getQueAsXML_test
				// (2007-03-12 kawai)
				controller.next();
				/*
				 * } else { response.sendRedirect(dob.url_fail); }
				 */
			} catch (qdbErrMessage e) {
				controller.setErrorMsg(e.getSystemMessage(dob.prof.label_lan));
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_TST_ENROL_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_TST_ENROL_LST_XML")) {

			String result = "";
			result = dob.dbcos.tstEnrolAsXML(con, dob.prof, sess,
					dob.dbmod.mod_res_id, dob.ent_id_parent, dob.cur_page,
					dob.pagesize);
			if (dob.cur_stylesheet == null || dob.cur_stylesheet.equals("")) {
				String cleanXML = cwUtils.escNull(result);
				out.println(cleanXML);
				return;
			}
			if (dob.prm_ACTION.equalsIgnoreCase("GET_TST_ENROL_LST_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_TST_ENROL_LST"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_ASS_ENROL_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_ASS_ENROL_LST_XML")) {
			String result = "";
			try {
				AcCourse acCos = new AcCourse(con);
				if (!acCos
						.checkModifyPermission(dob.prof, dob.dbcos.cos_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}
				String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id);
				String isFromIframe = request.getParameter("isFromIframe");
				metaXML += "<isFromIframe>";
				metaXML += isFromIframe;
				metaXML += "</isFromIframe>";

				if (dob.dbcos.cos_res_id > 0) {
					long itm_id = dbCourse.getCosItemId(con,
							dob.dbcos.cos_res_id);
					metaXML += aeItem
							.genItemActionNavXML(con, itm_id, dob.prof);

					aeItem itm = new aeItem();
					itm.itm_id = itm_id;
					itm.getItem(con);
					metaXML += itm.contentInfoAsXML(con);
				}

				result = dob.dbcos.assEnrolAsXML(con, sess, dob.prof,
						dob.dbmod.mod_res_id, dob.ass_queue, dob.page,
						dob.page_size, dob.ass_timestamp, metaXML, wizbini);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_ASS_ENROL_LST_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_ASS_ENROL_LST"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_SUBMISSION_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_SUBMISSION_LST_XML")) {
			String result = "";
			try {
				AcCourse acCos = new AcCourse(con);
				if (!acCos
						.checkModifyPermission(dob.prof, dob.dbcos.cos_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id);
				cwPagination cwPage = new cwPagination();
				cwPage.pageSize = dob.pagesize;
				cwPage.curPage = dob.page;

				cwPage.sortCol = cwPagination.esc4SortSql(dob.sortCol);
				cwPage.sortOrder = cwPagination.esc4SortSql(dob.sortOrder);

				result = dob.dbcos.submissionAsXML(con, sess, dob.prof,
						dob.dbmod.mod_res_id, dob.ass_queue, cwPage,
						dob.ass_timestamp, metaXML, dob.user_code);

				long itm_id = dbCourse.getCosItemId(con, dob.dbcos.cos_res_id);
				result += aeItem.genItemActionNavXML(con, itm_id, dob.prof);

				aeItem itm = new aeItem();
				itm.itm_id = itm_id;
				itm.getItem(con);
				result += itm.contentInfoAsXML(con);

				result = formatXML(result, "", "applyeasy", dob.prof);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_SUBMISSION_LST_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_SUBMISSION_LST"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_ENROL_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_ENROL_LST_XML")) {

			String result = "";
			result = dob.dbcos.enrolmentAsXML(con, dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_ENROL_LST_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_ENROL_LST"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("chg_user_mov_status")) {
			try {
				// Check Access Control
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmov.mov_mod_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_EXECUTE_MSG);
				}
				if (dob.dbmov.mov_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.dbmov.mov_tkh_id = DbTrackingHistory
							.getAppTrackingIDByMod(con, dob.dbmov.mov_mod_id,
									dob.dbmov.mov_ent_id);
				}

				if (dob.dbmov.mov_update_timestamp != null) {
					if (!dob.dbmov.checkUpdateTimestamp(con,
							dob.dbmov.mov_update_timestamp)) {
						if (dob.dbmov.mov_ent_id != 0)
							throw new qdbErrMessage("AEQM09",
									dbRegUser.getDisplayBil(con,
											dob.dbmov.mov_ent_id));
						else
							throw new qdbErrMessage("AEQM02");
					}
				}
				dob.dbmov.changeUserStatus(con, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SEND_TST_FILE")) {
			sess = request.getSession(true);
			// dob.prof.usr_id = dob.tst.usr_id;
			// dob.prof.usr_ent_id = dob.tst.usr_ent_id;
			long attempt_nbr = 0L;
			long mod_res_id = 0L;
			if (sess.getAttribute("send_tst_ts") != null
					&& dob.pagetime.equals((Timestamp) sess
							.getAttribute("send_tst_ts"))) {
				attempt_nbr = ((Long) sess.getAttribute("tst_attempt_nbr"))
						.longValue();
				mod_res_id = ((Long) sess.getAttribute("tst_mod_id"))
						.longValue();
			}
			dob.tst.procUploadedFiles(con, static_env, dob.tmpUploadDir,
					dob.h_file_filename, dob.dbque.que_res_id, dob.prof,
					mod_res_id, attempt_nbr);
			generalAsHtml(formatXML("", null, "file_submit", dob.prof), out,
					dob);
			return;
		} else if (dob.prm_ACTION.equalsIgnoreCase("SEND_TST_RESULT")) {

			// SVY module can be answered by other
			if (!dob.dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_SVY)) {
				// check if the current session is still valid
				if (!dob.tst.usr_id.equals(dob.prof.usr_id)) {
					// dob.url_fail = dob.url_login;
					msgBoxTst(MSG_ERROR, con, new qdbErrMessage("USR033"), dob,
							out);
					// out.println("Please login and try again.");
					return;
				}
			}

			try {
				/*
				 * Access Contorl, Svy can be answered by other it
				 * dob.tst.usr_id != prof.usr id that means the module is
				 * answered by other, then check prof have this permission
				 */
				if (!dob.prof.usr_id.equalsIgnoreCase(dob.tst.usr_id)
						&& dob.dbmod.mod_type
								.equalsIgnoreCase(dbModule.MOD_TYPE_SVY)) {
					// Check Access Control
					AcModule acMod = new AcModule(con);
					if (!acMod.checkModifyPermission(dob.prof,
							dob.tst.dbmod.mod_res_id)) {
						msgBox(MSG_ERROR, con, new qdbErrMessage("USR033"),
								dob, out);
						return;
					}
				}

				// For backward compatible, no update timestamp passed, no need
				// to check it
				if (dob.dbmov.mov_update_timestamp != null) {
					// Using the existing param passed by front end
					dob.dbmov.mov_mod_id = dob.tst.dbmod.mod_res_id;
					if (!dob.dbmov.checkUpdateTimestamp(con,
							dob.dbmov.mov_update_timestamp)) {
						if (dob.tst.usr_id != null
								&& dob.tst.usr_id.length() > 0) {
							msgBoxTst(
									MSG_ERROR,
									con,
									new qdbErrMessage("AEQM09", dbRegUser
											.getDisplayBil(con, dob.tst.usr_id)),
									dob, out);
							return;
						} else {
							msgBoxTst(MSG_ERROR, con, new qdbErrMessage(
									"AEQM02"), dob, out);
							return;
						}
					}
				}
				long attempt_nbr = dob.tst.markAndSave(con, dob.prof,
						dob.tkh_id);
				sess.setAttribute("send_tst_ts", dob.pagetime);
				sess.setAttribute("tst_attempt_nbr", new Long(attempt_nbr));
				sess.setAttribute("tst_mod_id", new Long(
						dob.tst.dbmod.mod_res_id));
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SEND_TST_RESULT_TEST")) {

			// delete the information of auto-save
			dbProgressAttemptSave dbpas = new dbProgressAttemptSave();
			dbpas.pasTkhId = dob.tkh_id;
			dbpas.pasResId = dob.tst.dbmod.mod_res_id;
			dbpas.del(con);
			dbProgressAttemptSaveAnswer dbpsa = new dbProgressAttemptSaveAnswer();
			dbpsa.psaTkhId = dob.tkh_id;
			dbpsa.psaPgrResId = dob.tst.dbmod.mod_res_id;
			dbpsa.delAll(con);
			dbResourceContent.DelForDxt(con, dob.tst.dbmod.mod_res_id,
					dob.tkh_id);

			// SVY module can be answered by other
			if (!dob.dbmod.mod_type.equalsIgnoreCase(dbModule.MOD_TYPE_SVY)) {
				// check if the current session is still valid
				if (!dob.tst.usr_id.equals(dob.prof.usr_id)) {
					// dob.url_fail = dob.url_login;
					msgBoxTst(MSG_ERROR, con, new qdbErrMessage("USR033"), dob,
							out);
					// out.println("Please login and try again.");
					return;
				}
			}
			boolean isExam = false;
			long parent_itm_id = 0;
			HashMap test_mod_cache = wizbCacheManager.getInstance()
					.getCachedHashmap("TEST_MOD_CACHE_SEND", true);
			if (test_mod_cache.get(new Long(dob.dbmod.mod_res_id)) == null) {
				aeItem parent_item = aeItem.getItemByContentMod(con,
						dob.tst.dbmod.mod_res_id);
				isExam = aeItem.isOnlineExam(parent_item);
				parent_itm_id = parent_item.itm_id;
				Object[] obj_cache = { new Boolean(isExam),
						new Long(parent_item.itm_id) };
				test_mod_cache.put(new Long(dob.dbmod.mod_res_id), obj_cache);
			} else {
				Object[] obj_cache = (Object[]) test_mod_cache.get(new Long(
						dob.dbmod.mod_res_id));
				isExam = ((Boolean) obj_cache[0]).booleanValue();
				parent_itm_id = ((Long) obj_cache[1]).longValue();
			}
 
			ExamPassport pass = null;
			// 判断是否“网上考试”或者“混合式考试”
			if (isExam) {
				// if test ended by admin, and score mark as zero
				if (dob.tst.isTerminateExam && dob.tst.is_score_mark_as_zero) {
					ExamController.examEndByLearner(parent_itm_id,
							dob.prof.usr_ent_id);
					msgBox(MSG_INFO, con, new qdbErrMessage(
							ExamModule.EXAM_SUBMIT_SUCC_AS_ZERO,
							dob.tst.terminate_exam_msg), dob, out);
					return;
				} else {
					pass = ExamController.getExamPassFromHash(parent_itm_id,
							dob.prof.usr_ent_id);
					if (pass != null && pass.terminate_as_zero) {
						ExamController.examEndByLearner(parent_itm_id,
								dob.prof.usr_ent_id);
						msgBox(MSG_INFO, con, new qdbErrMessage(
								ExamModule.EXAM_SUBMIT_SUCC_AS_ZERO,
								pass.terminate_msg), dob, out);
						return;
					}
				}
			}
			
			ExportController controller = new ExportController();
			try {
				/*
				 * Access Contorl, Svy can be answered by other it
				 * dob.tst.usr_id != prof.usr id that means the module is
				 * answered by other, then check prof have this permission
				 */
				if (!dob.prof.usr_id.equalsIgnoreCase(dob.tst.usr_id)
						&& dob.dbmod.mod_type
								.equalsIgnoreCase(dbModule.MOD_TYPE_SVY)) {
					// Check Access Control
					AcModule acMod = new AcModule(con);
					if (!acMod.checkModifyPermission(dob.prof,
							dob.tst.dbmod.mod_res_id)) {
						msgBox(MSG_ERROR, con, new qdbErrMessage("USR033"),
								dob, out);
						return;
					}
				} else {

				}
				// For backward compatible, no update timestamp passed, no need
				// to check it
				if (dob.dbmov.mov_update_timestamp != null) {
					// Using the existing param passed by front end
					dob.dbmov.mov_mod_id = dob.tst.dbmod.mod_res_id;
					if (!dob.dbmov.checkUpdateTimestamp(con,
							dob.dbmov.mov_update_timestamp)) {
						if (dob.tst.usr_id != null
								&& dob.tst.usr_id.length() > 0) {
							msgBoxTst(
									MSG_ERROR,
									con,
									new qdbErrMessage("AEQM09", dbRegUser
											.getDisplayBil(con, dob.tst.usr_id)),
									dob, out);
							return;
						} else {
							msgBoxTst(MSG_ERROR, con, new qdbErrMessage(
									"AEQM02"), dob, out);
							return;
						}
					}
				}
				if (sess.getAttribute(dob.window_name
						+ LearnerRptExporter.EXPORT_CONTROLLER) != null) {
					sess.removeAttribute(dob.window_name
							+ LearnerRptExporter.EXPORT_CONTROLLER);
					sess.setAttribute(dob.window_name
							+ LearnerRptExporter.EXPORT_CONTROLLER, controller);
				} else {
					sess.setAttribute(dob.window_name
							+ LearnerRptExporter.EXPORT_CONTROLLER, controller);
				}

				long attempt_nbr = dob.tst.markAndSave_test(con, dob.prof,
						dob.tkh_id, sess, controller);

				// 提交过完成后，强行把进度设置为100%
				controller.setCurrentRow(controller.getTotalRow());

				// 判断是否“网上考试”或者“混合式考试”
				if (isExam) {
					// if test ended by admin
					if (dob.tst.terminate_exam_msg != null
							&& dob.tst.terminate_exam_msg.length() > 0) {
						ExamController.examEndByLearner(parent_itm_id,
								dob.prof.usr_ent_id);
						msgBox(MSG_INFO, con, new qdbErrMessage(
								ExamModule.EXAM_SUBMIT_SUCC,
								dob.tst.terminate_exam_msg), dob, out);
						return;
					} else {
						if (pass != null && pass.isTerminate) {
							ExamController.examEndByLearner(parent_itm_id,
									dob.prof.usr_ent_id);
							msgBox(MSG_INFO, con, new qdbErrMessage(
									ExamModule.EXAM_SUBMIT_SUCC,
									dob.tst.terminate_exam_msg), dob, out);
							return;
						}
					}
					// 清除缓存
					ExamController.examEndByLearner(parent_itm_id,
							dob.prof.usr_ent_id);
				}

				sess.setAttribute("send_tst_ts", dob.pagetime);
				sess.setAttribute("tst_attempt_nbr", new Long(attempt_nbr));
				sess.setAttribute("tst_mod_id", new Long(
						dob.tst.dbmod.mod_res_id));
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				controller.setErrorMsg(e.getSystemMessage(dob.prof.label_lan));
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}
		// GET A WIZPACK REPORT WITH ITS MOD_ID by Kim
		else if (dob.prm_ACTION.equalsIgnoreCase("GET_PACK_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_PACK_RPT_XML")) {
			if (con != null
					&& con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED
					&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}

			// no access control

			String result = dob.dbacp.getElementRptAsXML(con, dob.prof,
					wizbini.getFileUploadResDirAbs());
			if (dob.prm_ACTION.equalsIgnoreCase("GET_PACK_RPT_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_PACK_RPT"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_PACK_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_LRN_PACK_RPT_XML")) {
			if (con != null
					&& con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED
					&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}

			// no access control

			String result = dob.dbacp.getLearnerElementRptAsXML(con, dob.prof,
					wizbini.getFileUploadResDirAbs());
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_PACK_RPT_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_PACK_RPT"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_MOD_RPT_XML")) {
			if (con != null
					&& con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED
					&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}

			// no access control

			String result = dob.dbmov.getModuleRptAsXML(con, dob.prof,
					dob.cur_node, dob.rpt_type);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_RPT_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_RPT"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_MOD_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_LRN_MOD_RPT_XML")) {
			if (con != null
					&& con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED
					&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}

			// access control
			AcCourse acCos = new AcCourse(con);

			if (!acCos.checkModifyPermission(dob.prof, dob.dbmov.mov_cos_id)
					&& dob.prof.usr_ent_id != dob.dbmov.mov_ent_id) {
				throw new qdbException("Permission denied.");
			}

			String result = dob.dbmov.getLearnerModuleRptAsXML(con, dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_MOD_RPT_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_MOD_RPT"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_SKILLSOFT_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_LRN_SKILLSOFT_RPT_XML")) {
			if (con != null
					&& con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED
					&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}

			// no access control

			String result = dob.dbmov
					.getLearnerSkillsoftRptAsXML(con, dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_SKILLSOFT_RPT_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_SKILLSOFT_RPT"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_AICC_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_LRN_AICC_RPT_XML")) {
			if (con != null
					&& con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED
					&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}
			String result = dob.dbmov.getLearnerAiccRptAsXML(con, dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_AICC_RPT_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_AICC_RPT"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_COS_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_LRN_COS_RPT_XML")) {
			if (con != null
					&& con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED
					&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}

			// no access control

			String result = dob.dbcov.getLearnerCourseRptAsXML(con, sess,
					dob.prof, dob.cur_grp_id, dob.cur_page, dob.pagesize);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_COS_RPT_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_COS_RPT"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_SINGLE_LRN_COS_RPT")
				|| dob.prm_ACTION
						.equalsIgnoreCase("GET_SINGLE_LRN_COS_RPT_XML")) {
			if (con != null
					&& con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED
					&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}

			// no access control

			String result = dob.dbcov.getSingleLearnerCourseRptAsXML(con,
					dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_SINGLE_LRN_COS_RPT_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_SINGLE_LRN_COS_RPT"))
				generalAsHtml(result, out, dob);
		}
		/*
		 * else if (dob.prm_ACTION.equalsIgnoreCase("UPD_SINGLE_LRN_COS_RPT") ||
		 * dob.prm_ACTION.equalsIgnoreCase("UPD_SINGLE_LRN_COS_RPT_XML")) {
		 * 
		 * //no access control
		 * 
		 * dob.dbcov.save(con); con.commit();
		 * response.sendRedirect(dob.url_success); }
		 */
		// Handle AICC PATH EVENT
		else if (dob.prm_ACTION.equalsIgnoreCase("GETPATH")
				|| dob.prm_ACTION.equalsIgnoreCase("GETPATH_XML")) {

			// no access control

			String result = dob.dbacp.getPathAsXML(con, dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GETPATH_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GETPATH"))
				generalAsHtml(result, out, dob);

		} else if (dob.prm_ACTION.equalsIgnoreCase("PUTPATH")) {

			// no access control

			dob.dbacp.save(con, dob.prof);
			con.commit();
			response.sendRedirect(dob.url_success);
		}
		// aicc path for module
		else if (dob.prm_ACTION.equalsIgnoreCase("GETPARAM")
				|| dob.prm_ACTION.equalsIgnoreCase("GETPARAM_XML")) {

			// no access control
			if (dob.dbmov.mov_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
				dob.dbmov.mov_tkh_id = DbTrackingHistory.getAppTrackingIDByMod(
						con, dob.dbmov.mov_mod_id, dob.prof.usr_ent_id);
			}

			String result = dob.dbmov.getParamAsXML(con, dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GETPARAM_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GETPARAM"))
				generalAsHtml(result, out, dob);

		}

		else if (dob.prm_ACTION.equalsIgnoreCase("PUTPARAM")) {

			// no access control
			if (dob.dbmov.mov_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
				dob.dbmov.mov_tkh_id = DbTrackingHistory.getAppTrackingIDByMod(
						con, dob.dbmov.mov_mod_id, dob.dbmov.mov_ent_id);
			}

			if (dob.dbmod.tkh_id != 0
					&& dob.dbmov.mov_tkh_id != DbTrackingHistory.TKH_ID_NOT_FOUND
					&& DbTrackingHistory.getAppTrackingIDByCos(con,
							dob.dbmov.mov_tkh_id, dob.prof.usr_ent_id,
							dbModule.getCosId(con, dob.dbmov.mov_mod_id),
							dob.dbmov.mov_mod_id) != 1) {
				msgBox(MSG_ERROR, con, new cwSysMessage("USR033"), dob, out);
				return;
			}

			if (dob.dbcos.is_hkib_vod) {
				dob.dbmov.mod_time = 0;
				dob.dbmov.save(con, dob.prof);
				// 添加视频学习记录
				dob.dbcos.addVodLearnRecord(con, dob.prof, dob.dbcos);
				con.commit();
			} else {
				if (cwUtils.checkHmac(String.valueOf(dob.dbmov.mov_start_time),
						dob.dbmov.mov_encrypted_start_time)
						|| "skip".equals(dob.dbmov.mov_encrypted_start_time)) {
					if (dob.dbmov.mov_start_time == null)
						dob.dbmov.mod_time = cwSQL.getTime(con).getTime() / 1000;
					else
						dob.dbmov.mod_time = (cwSQL.getTime(con).getTime() - dob.dbmov.mov_start_time
								.getTime()) / 1000;
					dob.dbmov.save(con, dob.prof);
					con.commit();
				}
			}
		}

		// export an AICC course
		else if (dob.prm_ACTION.equalsIgnoreCase("EXPORT_COS")) {
			// set upd user
			dob.dbcos.res_upd_user = dob.prof.usr_id;
			// dob.dbcos.res_usr_id_owner = dob.prof.usr_id;

			try {
				dob.dbcos.res_lan = "GB2312";

				/*
				 * if (!dbResourcePermission.hasPermission(con,
				 * dob.dbcos.cos_res_id, dob.prof,
				 * dbResourcePermission.RIGHT_WRITE)) { throw new
				 * qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG); }
				 */

				// access control
				AcCourse accos = new AcCourse(con);
				if (!accos
						.checkModifyPermission(dob.prof, dob.dbcos.cos_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				// String exportURL = dob.dbcos.exportAiccCos(con, dob.domain,
				// dob.prof, dob.tmpUploadDir);
				String resDir = wizbini.getFileUploadResDirAbs()
						+ dbUtils.SLASH + Long.toString(dob.dbcos.cos_res_id);
				File fileResDir = new File(resDir);
				if (fileResDir.exists() == false) {
					fileResDir.mkdir();
				}
				// String exportURL = dob.dbcos.exportAiccCos(con, dob.domain,
				// dob.prof, resDir);
				response.sendRedirect(dob.url_success);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		// import an AICC course
		// cliff, 2001/4/18
		else if (dob.prm_ACTION.equalsIgnoreCase("IMPORT_COS")) {
			// set upd user
			dob.dbcos.res_upd_user = dob.prof.usr_id;
			// dob.dbcos.res_usr_id_owner = dob.prof.usr_id;
			Vector vtCosResId = dob.dbcos.getChildCosResId(con);
			Vector vtParentObj = new Vector();
			Vector vtNewModId = new Vector();
			String isEnrollmentRelated = dob.dbcos.getIsEnrollmentRelated(con);
			long modId;

			try {
				dob.dbcos.res_lan = "GB2312";

				/*
				 * if (!dbResourcePermission.hasPermission(con,
				 * dob.dbcos.cos_res_id, dob.prof,
				 * dbResourcePermission.RIGHT_WRITE)) { throw new
				 * qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG); }
				 */
				// access control
				AcCourse accos = new AcCourse(con);
				if (!accos
						.checkModifyPermission(dob.prof, dob.dbcos.cos_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				if (dob.bFileUpload) {
					File aiccCrsFile = null;
					File aiccCstFile = null;
					File aiccDesFile = null;
					File aiccAuFile = null;
					File aiccOrtFile = null;

					File aiccFolder = null;

					if (dob.aicc_crs_filename.length() == 0
							|| dob.aicc_cst_filename.length() == 0
							|| dob.aicc_des_filename.length() == 0
							|| dob.aicc_au_filename.length() == 0) {
						msgBox(MSG_ERROR, con, new qdbErrMessage("AICC01"),
								dob, out);
						return;
					}

					aiccCrsFile = new File(dob.tmpUploadDir + dbUtils.SLASH
							+ dob.aicc_crs_filename);
					aiccCstFile = new File(dob.tmpUploadDir + dbUtils.SLASH
							+ dob.aicc_cst_filename);
					aiccDesFile = new File(dob.tmpUploadDir + dbUtils.SLASH
							+ dob.aicc_des_filename);
					aiccAuFile = new File(dob.tmpUploadDir + dbUtils.SLASH
							+ dob.aicc_au_filename);
					aiccOrtFile = new File(dob.tmpUploadDir + dbUtils.SLASH
							+ dob.aicc_ort_filename);

					if (aiccCrsFile.exists() == false
							|| aiccCstFile.exists() == false
							|| aiccDesFile.exists() == false
							|| aiccAuFile.exists() == false) {
						msgBox(MSG_ERROR, con, new qdbErrMessage("AICC01"),
								dob, out);
						/*
						 * if (aiccCrsFile.exists()) { aiccCrsFile.delete(); }
						 * if (aiccCstFile.exists()) { aiccCstFile.delete(); }
						 * if (aiccDesFile.exists()) { aiccDesFile.delete(); }
						 * if (aiccAuFile.exists()) { aiccAuFile.delete(); } if
						 * (aiccOrtFile.exists()) { aiccOrtFile.delete(); }
						 */

						aiccFolder = new File(dob.tmpUploadDir);
						if (aiccFolder.exists()) {
							aiccFolder.delete();
						}
						return;
					} else {
						String validMsg = CourseValidator.validAiccCourse(
								aiccCrsFile, aiccAuFile, aiccDesFile,
								aiccCstFile, aiccOrtFile).trim();
						if (!validMsg.equals("")) {
							String xml = dbUtils.xmlHeader
									+ "<errors>"
									+ ((dob.prof != null) ? dob.prof.asXML()
											: "") + validMsg + "</errors>";
							generalAsHtml(xml, out, "course_error.xsl", null);
							return;
						}

						if (aiccOrtFile.exists()) {
							modId = dob.dbcos.importAiccCos(con, dob.domain,
									dob.prof, dob.tmpUploadDir + dbUtils.SLASH
											+ dob.aicc_crs_filename,
									dob.tmpUploadDir + dbUtils.SLASH
											+ dob.aicc_cst_filename,
									dob.tmpUploadDir + dbUtils.SLASH
											+ dob.aicc_des_filename,
									dob.tmpUploadDir + dbUtils.SLASH
											+ dob.aicc_au_filename,
									dob.tmpUploadDir + dbUtils.SLASH
											+ dob.aicc_ort_filename,
									vtParentObj, null, isEnrollmentRelated);
						} else {
							modId = dob.dbcos.importAiccCos(con, dob.domain,
									dob.prof, dob.tmpUploadDir + dbUtils.SLASH
											+ dob.aicc_crs_filename,
									dob.tmpUploadDir + dbUtils.SLASH
											+ dob.aicc_cst_filename,
									dob.tmpUploadDir + dbUtils.SLASH
											+ dob.aicc_des_filename,
									dob.tmpUploadDir + dbUtils.SLASH
											+ dob.aicc_au_filename, null,
									vtParentObj, null, isEnrollmentRelated);
						}

						for (int i = 0; i < vtCosResId.size(); i++) {
							long clsModId;
							if (aiccOrtFile.exists()) {
								clsModId = ((dbCourse) vtCosResId.get(i))
										.importAiccCos(
												con,
												dob.domain,
												dob.prof,
												dob.tmpUploadDir
														+ dbUtils.SLASH
														+ dob.aicc_crs_filename,
												dob.tmpUploadDir
														+ dbUtils.SLASH
														+ dob.aicc_cst_filename,
												dob.tmpUploadDir
														+ dbUtils.SLASH
														+ dob.aicc_des_filename,
												dob.tmpUploadDir
														+ dbUtils.SLASH
														+ dob.aicc_au_filename,
												dob.tmpUploadDir
														+ dbUtils.SLASH
														+ dob.aicc_ort_filename,
												vtParentObj, new Long(modId),
												isEnrollmentRelated);
							} else {
								clsModId = ((dbCourse) vtCosResId.get(i))
										.importAiccCos(
												con,
												dob.domain,
												dob.prof,
												dob.tmpUploadDir
														+ dbUtils.SLASH
														+ dob.aicc_crs_filename,
												dob.tmpUploadDir
														+ dbUtils.SLASH
														+ dob.aicc_cst_filename,
												dob.tmpUploadDir
														+ dbUtils.SLASH
														+ dob.aicc_des_filename,
												dob.tmpUploadDir
														+ dbUtils.SLASH
														+ dob.aicc_au_filename,
												null, vtParentObj, new Long(
														modId),
												isEnrollmentRelated);
							}
							vtNewModId.addElement(new Long(clsModId));
						}
						procUploadedFiles(con, request, dob.dbres.res_id,
								dob.tmpUploadDir, dob);
						for (int i = 0; i < vtNewModId.size(); i++) {
							procUploadedFiles(con, request,
									((Long) vtNewModId.get(i)).longValue(),
									dob.tmpUploadDir, dob);
						}

						// create the SkillSoft specific info. file
						String courseName = dob.aicc_des_filename.substring(0,
								dob.aicc_des_filename.indexOf("."));
						dob.dbres.create_SSC_info_file(
								wizbini.getFileUploadResDirAbs(), courseName);
						/*
						 * if (aiccCrsFile.exists()) { aiccCrsFile.delete(); }
						 * if (aiccCstFile.exists()) { aiccCstFile.delete(); }
						 * if (aiccDesFile.exists()) { aiccDesFile.delete(); }
						 * if (aiccAuFile.exists()) { aiccAuFile.delete(); } if
						 * (aiccOrtFile.exists()) { aiccOrtFile.delete(); }
						 * 
						 * aiccFolder = new File(dob.tmpUploadDir); if
						 * (aiccFolder.exists()) { aiccFolder.delete(); }
						 */
					}
				}

				con.commit();

				// imported the course successfully
				msgBox(MSG_STATUS, con, new qdbErrMessage("AICC02"), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}
		/*
		 * else if (dob.prm_ACTION.equalsIgnoreCase("GET_SVY_RPT") ||
		 * dob.prm_ACTION.equalsIgnoreCase("GET_SVY_RPT_XML")) { String result =
		 * dob.dbmod.getSurveyReport(con, dob.prof);
		 * 
		 * if(dob.cur_stylesheet == null || dob.cur_stylesheet.equals("")) {
		 * String cleanXML = cwUtils.escNull(result); out.println(cleanXML);
		 * return; } if(dob.prm_ACTION.equalsIgnoreCase("GET_SVY_RPT_XML"))
		 * static_env.outputXML(out, result);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_SVY_RPT"))
		 * generalAsHtml(result, out, dob); }
		 */
		// GET Student Report
		else if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_RPT_XML")) {
			try {

				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {

					throw new qdbException("Permission denied.");
				}

				String result = dob.dbmod.getGroupReportFromList(con,
						dob.que_id_lst, dob.rpt_group_lst,
						dob.dbpgr.pgr_attempt_nbr, dob.prof);

				if (dob.cur_stylesheet == null || dob.cur_stylesheet.equals("")) {
					String cleanXML = cwUtils.escNull(result);
					out.println(cleanXML);
					return;
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT"))
					generalAsHtml(result, out, dob);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR_XML")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!dob.prof.usr_id.equals(dob.dbpgr.pgr_usr_id)
						&& !acmod.checkModifyPermission(dob.prof,
								dob.dbmod.mod_res_id)) {

					throw new qdbException("Permission denied.");
				}
				if (dob.dbmod.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.dbmod.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(
							con, dob.dbmod.res_id, dob.prof.usr_ent_id);
				}

				String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id);
				boolean flag = false;
				if (dob.dbcos.res_id > 0) {
					long itm_id = dbCourse.getCosItemId(con, dob.dbcos.res_id);
					metaXML += aeItem
							.genItemActionNavXML(con, itm_id, dob.prof);

					aeItem itm = new aeItem();
					itm.itm_id = itm_id;
					itm.getItem(con);
					metaXML += itm.contentInfoAsXML(con);
					flag = true;
				}
				metaXML += "<is_item>" + flag + "</is_item>";

				String result = dob.dbmod.getUserReport(con,
						dob.dbpgr.pgr_usr_id, dob.que_id_lst,
						dob.dbpgr.pgr_attempt_nbr, dob.prof, metaXML,
						static_env, 0);

				if (dob.cur_stylesheet == null || dob.cur_stylesheet.equals("")) {
					String cleanXML = cwUtils.escNull(result);
					out.println(cleanXML);
					return;
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR"))
					generalAsHtml(result, out, dob);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR_TEST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR_TEST_HTML")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR_TEST_XML")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!dob.prof.usr_id.equals(dob.dbpgr.pgr_usr_id)
						&& !acmod.checkModifyPermission(dob.prof,
								dob.dbmod.mod_res_id)) {

					throw new qdbException("Permission denied.");
				}
				if (dob.dbmod.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.dbmod.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(
							con, dob.dbmod.res_id, dob.prof.usr_ent_id);
				}
				StringBuffer result = new StringBuffer(1024);
				String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id);
				result.append(
						"<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF
								+ "\" ?>").append(dbUtils.NEWL);
				// if(dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR_TEST")){
				// result.append("<?xml:stylesheet type=\"text/xsl\" href=\"").append("..\\xsl\\"
				// + dob.cur_stylesheet).append("\"?>").append(dbUtils.NEWL);
				// }
				result.append(dob.dbmod.getUserReport_test(con,
						dob.dbpgr.pgr_usr_id, dob.que_id_lst,
						dob.dbpgr.pgr_attempt_nbr, dob.prof, metaXML,
						static_env));
				if (dob.cur_stylesheet == null || dob.cur_stylesheet.equals("")) {
					String cleanXML = cwUtils.escNull(result.toString());
					out.println(cleanXML);
					return;
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR_TEST_XML"))
					static_env.outputXML(out, result.toString());
				if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR_TEST_HTML")
						|| dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USR_TEST"))
					generalAsHtml(result.toString(), out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else {
			passed = false;
		}

		// Function for post message
		if (!passed) {
			service_cond_1(request, response, dob, con, out, sess, false);
		}
	}

	private void service_cond_1(HttpServletRequest request,
			HttpServletResponse response, dataObj dob, Connection con,
			PrintWriter out, HttpSession sess, boolean share_mode)
			throws cwSysMessage, cwException, SQLException, qdbException,
			IOException, UploadedFileException, qdbErrMessage,
			CloneNotSupportedException, Exception {

		/*
		 * if (dob.prm_ACTION.equalsIgnoreCase("POST_MSG")) {
		 * 
		 * boolean postOk = false;
		 * 
		 * //no access control
		 * 
		 * postOk = dob.dbmsg.postMsg(con, dob.encoding, dob.msg_title,
		 * dob.msg_body, dob.msg_begin_date, dob.msg_end_date, dob.client_enc);
		 * 
		 * if (postOk) { response.sendRedirect(dob.url_success); } else {
		 * response.sendRedirect(dob.url_fail); } }
		 */

		if (dob.prm_ACTION.equalsIgnoreCase("GET_ASS_RPT_USR")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_ASS_RPT_USR_XML")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!dob.prof.usr_id.equals(dob.dbpgr.pgr_usr_id)
						&& !acmod.checkModifyPermission(dob.prof,
								dob.dbmod.mod_res_id)) {

					throw new qdbException("Permission denied.");
				}
				String result = dob.dbass.getUserReport(con,
						dob.dbpgr.pgr_usr_id, dob.prof, dob.tkh_id);

				boolean is_item = false;
				if (dob.dbcos.cos_res_id > 0) {
					long itm_id = dbCourse.getCosItemId(con,
							dob.dbcos.cos_res_id);
					result += aeItem.genItemActionNavXML(con, itm_id, dob.prof);

					aeItem itm = new aeItem();
					itm.itm_id = itm_id;
					itm.getItem(con);
					result += itm.contentInfoAsXML(con);
					is_item = true;
				}
				result += "<is_item>" + is_item + "</is_item>";

				result = formatXML(result, "", "student_report", dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("GET_ASS_RPT_USR_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_ASS_RPT_USR"))
					generalAsHtml(result, out, dob);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("REACTIVATE_USR")) {
			try {
				AcRegUser acusr = new AcRegUser(con);
				if (!acusr.canMgtUser(dob.prof, dob.ent_id_lst[0],
						wizbini.cfgTcEnabled)) {
					throw new qdbErrMessage("ACL002");
				}
				// accessControl
				for (int i = 0; i < dob.ent_id_lst.length; i++) {
					dbRegUser.unlockAccount(con, dob.ent_id_lst[i]);
					dbRegUser activeUser = new dbRegUser();
					activeUser.usr_ent_id = dob.ent_id_lst[i];
					activeUser.getByEntId(con);

					ObjectActionLog log = new ObjectActionLog(
							activeUser.usr_ent_id, activeUser.usr_ste_usr_id,
							activeUser.usr_display_bil,
							ObjectActionLog.OBJECT_TYPE_USR,
							ObjectActionLog.OBJECT_ACTION_ACTIVE,
							ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
							dob.prof.getUsr_ent_id(),
							dob.prof.getUsr_last_login_date(), dob.prof.getIp());
					SystemLogContext.saveLog(log,
							SystemLogTypeEnum.OBJECT_ACTION_LOG);

				}
				// accessControl
				// dbRegUser.unlockAccount(con,dob.getUsr().usr_ent_id);
				/* dob.usr.resetInactive(con,dob.usr.usr_ent_id); */
				con.commit();

				if (dob.multi_reactivate) {
					response.sendRedirect(dob.url_success);
				} else {
					cwSysMessage e = new cwSysMessage("USR018");
					msgBox(MSG_STATUS, con, e, dob, out);
				}
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("RESTORE_USR")) {
			try {
				AcRegUser acusr = new AcRegUser(con);
				if (!acusr.hasDelPrivilege(dob.prof.usr_ent_id,
						dob.prof.root_ent_id, dob.prof.current_role,
						dob.ent_id_lst[0])
						|| !acusr.canMgtUser(dob.prof, dob.ent_id_lst[0],
								wizbini.cfgTcEnabled)) {
					throw new qdbErrMessage("USR004");
				}

				long[] list = dob.ent_id_lst;
				dbRegUser.userReduction(con,dob.prof,list);
				
				EnterpriseInfoPortalBean eipBean = null;
				if (qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()) {
					eipBean = EnterpriseInfoPortalDao.getEipByTcrID(con,
							dob.prof.my_top_tc_id);
					if (eipBean != null
							&& !dob.prof.current_role.startsWith("ADM")
							&& dob.prof.my_top_tc_id != DbTrainingCenter
									.getSuperTcId(con, dob.prof.root_ent_id)) {
						long totalNum = eipBean.getEip_account_num();
						long dSize = totalNum - eipBean.getAccount_used();
						if (dob.ent_id_lst != null
								&& dob.ent_id_lst.length > dSize) {
							throw new cwSysMessage("lab_user_full_on_restore");
						}
					}
				}

				for (int i = 0; i < dob.ent_id_lst.length; i++) {
					dob.getUsr().usr_ent_id = dob.ent_id_lst[i];
					dob.getUsr().restore(con, dob.prof);

					ObjectActionLog log = new ObjectActionLog(
							dob.getUsr().usr_ent_id,
							dob.getUsr().usr_ste_usr_id,
							dob.getUsr().usr_display_bil,
							ObjectActionLog.OBJECT_TYPE_USR,
							ObjectActionLog.OBJECT_ACTION_RESTORE,
							ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
							dob.prof.getUsr_ent_id(),
							dob.prof.getUsr_last_login_date(), dob.prof.getIp());
					SystemLogContext.saveLog(log,
							SystemLogTypeEnum.OBJECT_ACTION_LOG);
				}
				// 企业租用账户数是否已经达到上限
				// EnterpriseInfoPortal.checkEIPAccountLimited(con, 0,
				// dob.getUsr().usr_ent_id, -1, false);

				// 重名的判断：不同企业间可以重名
				// EnterpriseInfoPortal.checkEIPUsrId(con,
				// dob.getUsr().usr_ste_usr_id, 0, dob.getUsr().usr_ent_id,
				// dob.prof.root_ent_id);

				// dob.getUsr().restore(con,dob.prof);
				con.commit();
				cwSysMessage e = new cwSysMessage("USR019");
				msgBox(MSG_STATUS, con, e, dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_USR")) {
			try {
				AcRegUser acusr = new AcRegUser(con);
				boolean canMgtUser = acusr.canMgtUser(dob.prof,
						dob.getUsr().usr_ent_id, wizbini.cfgTcEnabled);
				if (!canMgtUser) {
					dob.setUrl_fail(dob.url_fail1);
				}
				if (!acusr.hasDelPrivilege(dob.prof.usr_ent_id,
						dob.prof.root_ent_id, dob.prof.current_role,
						dob.getUsr().usr_ent_id)
						|| !canMgtUser) {
					throw new qdbErrMessage("USR004");
				}
				dob.getUsr().get(con);

				ObjectActionLog log = new ObjectActionLog(dob.getUsr().ent_id,
						dob.getUsr().usr_ste_usr_id,
						dob.getUsr().usr_display_bil,
						ObjectActionLog.OBJECT_TYPE_USR,
						ObjectActionLog.OBJECT_ACTION_DEL,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				dob.getUsr().del(con, dob.prof, dob.ent_id_parent,
						dob.prof.label_lan);
				if (dob.multi_del) {
					response.sendRedirect(dob.url_success);
				} else {
					cwSysMessage e = new cwSysMessage("USR007");
					msgBox(MSG_STATUS, con, e, dob, out);
				}
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);
				// response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			} catch (cwSysMessage se) {
				con.rollback();
				msgBox(MSG_ERROR, con, se, dob, out);
				return;
			}
		}
		/*
		 * else if (dob.prm_ACTION.equalsIgnoreCase("TRASH_USR")) { try {
		 * AcRegUser acusr = new AcRegUser(con); int size =
		 * (dob.usr_ent_id_lst.length<dob.usr_timestamp_lst.length ?
		 * dob.usr_ent_id_lst.length : dob.usr_timestamp_lst.length); for(int
		 * i=0;i<size;i++) { if(!acusr.hasTrashPrivilege(dob.prof.usr_ent_id,
		 * dob.prof.current_role, dob.usr_ent_id_lst[i])) { throw new
		 * qdbErrMessage("USR004"); } }
		 * dbRegUser.trashUsers(con,dob.prof,dob.usr_ent_id_lst
		 * ,dob.usr_timestamp_lst); con.commit(); if( dob.multi_del ){
		 * response.sendRedirect(dob.url_success); }else { cwSysMessage e = new
		 * cwSysMessage("USR008"); msgBox(MSG_STATUS, con, e, dob, out); }
		 * //response.sendRedirect(dob.url_success); } catch(qdbErrMessage e) {
		 * con.rollback(); msgBox(MSG_ERROR, con, e, dob, out); return; } }
		 */
		else if (dob.prm_ACTION.equalsIgnoreCase("TRASH_USG")) {
			try {
				AcUserGroup acUsg = new AcUserGroup(con);
				if (!acUsg.hasManagePrivilege(dob.prof.current_role)) {
					throw new qdbErrMessage("USR004");
				}

				if (dob.usg_ent_id_lst != null
						&& dob.usg_ent_id_lst.length == 1) {
					if (!dbUserGroup.isUsgExists(con, dob.usg_ent_id_lst[0])) {
						throw new qdbErrMessage("USG011");
					}
				}

				dbUserGroup.trashGroups(con, dob.prof, dob.usg_ent_id_lst,
						dob.usg_timestamp_lst, wizbini.cfgTcEnabled, true,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
				con.commit();

				cwSysMessage e = new cwSysMessage("USG006");
				msgBox(MSG_STATUS, con, e, dob, out);
				// response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_USR")) {
			try {
				AcRegUser acusr = new AcRegUser(con);
				if (!acusr.hasUpdPrivilege(dob.prof.usr_ent_id,
						dob.prof.root_ent_id, dob.prof.current_role,
						dob.getUsr().usr_ent_id, dob.getUsr().usr_roles)) {
					throw new qdbErrMessage("USR003");
				}
				dob.getUsr().upd(con, dob.prof);
				/*
				 * dob.usr.saveRoleTargetEntity(con, dob.rol_target_ext_ids,
				 * dob.rol_target_ent_groups, dob.prof.usr_id);
				 */
				cwSysMessage e = new cwSysMessage("USR009");
				msgBox(MSG_STATUS, con, e, dob, out);
				// response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_USR_ON_DEMAND")) {
			try {
				AcRegUser acusr = new AcRegUser(con);
				if (AccessControlWZB.isRoleTcInd(dob.prof.current_role)) {
					if (!acusr.hasUpdPrivilege(dob.prof.usr_ent_id,
							dob.prof.root_ent_id, dob.prof.current_role,
							dob.getUsr().usr_ent_id, dob.getUsr().usr_roles)) {
						throw new qdbErrMessage("ACL002");
					}
				}

				if (dob.prof.usr_ent_id != dob.getUsr().usr_ent_id
						&& !acusr.canMgtUser(dob.prof, dob.getUsr().usr_ent_id,
								wizbini.cfgTcEnabled)) {
					dob.setUrl_fail(dob.url_fail1);
					throw new qdbErrMessage("USR004");
				}
				// 检测选定的下属部门是否存在
				long[] supervise_target_ent_ids = cwUtils
						.stringArray2LongArray(dob.getUsr().supervise_target_ent_ids);
				if (supervise_target_ent_ids != null
						&& supervise_target_ent_ids.length > 0
						&& !dbRegUser
								.isUsgExists(con, supervise_target_ent_ids)) {
					throw new qdbErrMessage("USG016");
				}
				String result = dob.getUsr().updUser(con, dob.prof,
						dob.vColName, dob.vColType, dob.vColValue,
						dob.vClobColName, dob.vClobColValue, dob.vExtColName,
						dob.vExtColType, dob.vExtColValue, dob.vExtClobColName,
						dob.vExtClobColValue, wizbini.cfgTcEnabled);
				dob.getUsr().get(con);
				ObjectActionLog log = new ObjectActionLog(dob.getUsr().ent_id,
						dob.getUsr().usr_ste_usr_id,
						dob.getUsr().usr_display_bil,
						ObjectActionLog.OBJECT_TYPE_USR,
						ObjectActionLog.OBJECT_ACTION_UPD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);

				if ("use_default_image"
						.equals(dob.getUsr().extension_43_select)) {
					String defaultPath = static_env.DOC_ROOT + dbUtils.SLASH
							+ static_env.DEFAULT_IMGLIG + dbUtils.SLASH
							+ static_env.INI_INSTRUCTOR_DIR_UPLOAD_URL
							+ dbUtils.SLASH + dob.getUsr().urx_extra_43;
					if (dob.getUsr().urx_extra_43 != null) {
						int slash = dob.getUsr().urx_extra_43.indexOf("\\");
						String slashType = "/";
						if (slash != -1) {
							slashType = "\\\\";
						}
						String[] originalPicDir = dob.getUsr().urx_extra_43
								.split(slashType);
						String saveDirPath = "";
						if (originalPicDir != null
								&& originalPicDir.length >= 3) {
							saveDirPath = wizbini.getFileUploadUsrDirAbs()
									+ dbUtils.SLASH + dob.getUsr().usr_ent_id
									+ dbUtils.SLASH + originalPicDir[1];
						} else {
							saveDirPath = wizbini.getFileUploadUsrDirAbs()
									+ dbUtils.SLASH + dob.getUsr().usr_ent_id;
						}

						dbUtils.delFiles(saveDirPath);
						dbUtils.copyFile(defaultPath, saveDirPath);
					}
				} else if (dob.bFileUpload) {
					this.procUploadUsrFace(con, dob.tmpUploadDir,
							dob.getUsr().usr_ent_id, dob.remain_photo_ind, sess);
				}

				if (result != null) {
					con.rollback();
					Vector resultVec = new Vector();
					resultVec.addElement(LangLabel.getValue(dob.prof.label_lan,
							AcObjective.LABEL_ROL_TADM));
					resultVec.addElement(result);
					cwSysMessage e = new cwSysMessage("USR030", resultVec);
					msgBox(MSG_ERROR, con, e, dob, out);
				} else {
					// update RegUserExtension
					// dob.usr.updExtension(con,dob.usr.usr_ent_id);
					con.commit();
					if (dob.getUsr().usr_ent_id == dob.prof.getUsr_ent_id()) {
						//if ("use_default_image".equals(dob.getUsr().extension_43_select)) {
							dob.prof.setUsr_photo(dbUtils.esc4XML(ContextPath
									.getContextPath()
									+ dbUtils.SLASH
									+ dbRegUser.getUsrPhotoDir(wizbini,
											dob.prof.root_id,
											dob.prof.usr_ent_id,
											dob.getUsr().urx_extra_43)));
						//}
					}
					cwSysMessage e = new cwSysMessage("USR009");
					msgBox(MSG_STATUS, con, e, dob, out);
				}
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("save_my_profile")) {
			try {
				// clientEnc

				PsnBiography psnBiography = new PsnBiography();
				psnBiography.saveBiography(con, dob.prof.usr_ent_id,
						dob.option_lst, "", dob.prof.usr_id);

				AcRegUser acusr = new AcRegUser(con);

				if (!acusr.canMgtUser(dob.prof, dob.getUsr().usr_ent_id,
						wizbini.cfgTcEnabled)) {
					dob.setUrl_fail(dob.url_fail1);
					throw new qdbErrMessage("USR004");
				}
				String result = dob.getUsr().updUser(con, dob.prof,
						dob.vColName, dob.vColType, dob.vColValue,
						dob.vClobColName, dob.vClobColValue, dob.vExtColName,
						dob.vExtColType, dob.vExtColValue, dob.vExtClobColName,
						dob.vExtClobColValue, wizbini.cfgTcEnabled);

				if (dob.bFileUpload)
					this.procUploadUsrFace(con, dob.tmpUploadDir,
							dob.prof.usr_ent_id, dob.remain_photo_ind, sess);
				if (result != null) {
					con.rollback();
					Vector resultVec = new Vector();
					resultVec.addElement(LangLabel.getValue(dob.prof.label_lan,
							AcObjective.LABEL_ROL_TADM));
					resultVec.addElement(result);
					cwSysMessage e = new cwSysMessage("USR030", resultVec);
					msgBox(MSG_ERROR, con, e, dob, out);
				} else {
					if (dob.prof.isLrnRole) {
						Credit credit = new Credit();
						credit.updUserCredits(con, Credit.SYS_UPD_MY_PROFILE,
								0, (int) dob.prof.usr_ent_id, dob.prof.usr_id,
								0, 0, 0, 0);
					}

					// 设置学员我的喜好中的主要培训中心
					if (dob.usr_template_tcr_id == null) {
						dob.usr_template_tcr_id = 0l;
					}
					sqlMapClient
							.executeUpdate(
									con,
									"update reguser set usr_choice_tcr_id = ? where usr_ent_id= ?",
									new Object[] { dob.usr_template_tcr_id,
											dob.prof.usr_ent_id });

					con.commit();
					cwSysMessage e = new cwSysMessage("USR009");
					msgBox(MSG_STATUS, con, e, dob, out);
				}

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("ADD_INSTR_ON_DEMAND")) {
			try {
				dob.getUsr().updateProfile(con, dob.prof, dob.vColName,
						dob.vColType, dob.vColValue, dob.vClobColName,
						dob.vClobColValue, dob.vExtColName, dob.vExtColType,
						dob.vExtColValue, dob.vExtClobColName,
						dob.vExtClobColValue);
				dob.getUsr().assignInstrRole(con, dob.prof.root_ent_id);
				con.commit();
				cwSysMessage e = new cwSysMessage("GEN004");
				msgBox(MSG_STATUS, con, e, dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_USR_PROFILE_ON_DEMAND")) {
			try {

				dob.getUsr().updateProfile(con, dob.prof, dob.vColName,
						dob.vColType, dob.vColValue, dob.vClobColName,
						dob.vClobColValue, dob.vExtColName, dob.vExtColType,
						dob.vExtColValue, dob.vExtClobColName,
						dob.vExtClobColValue);
				con.commit();
				cwSysMessage e = new cwSysMessage("GEN003");
				msgBox(MSG_STATUS, con, e, dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("RENAME_USR")) {
			try {
				AcRegUser acusr = new AcRegUser(con);
				if (!acusr.hasUpdPrivilege(dob.prof.usr_ent_id,
						dob.prof.root_ent_id, dob.prof.current_role,
						dob.getUsr().usr_ent_id, dob.getUsr().usr_roles))
					throw new qdbErrMessage("USR003");
				dob.getUsr().renameUser(con, dob.prof);
				con.commit();
				cwSysMessage e = new cwSysMessage("USR009");
				msgBox(MSG_STATUS, con, e, dob, out);

				ObjectActionLog log = new ObjectActionLog(
						dob.getUsr().usr_ent_id, dob.getUsr().usr_ste_usr_id,
						dob.getUsr().usr_display_bil,
						ObjectActionLog.OBJECT_TYPE_USR,
						ObjectActionLog.OBJECT_ACTION_UPD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("APPR_USR")) {
			try {

				AcRegUser acusr = new AcRegUser(con);
				if (!acusr.hasApprovalPrivilege(dob.prof,
						dob.getUsr().usr_ent_id, wizbini.cfgTcEnabled)) {
					// DENNIS: TODO: new SystemMessage
					throw new qdbErrMessage("USR003");
				}
				if (!acusr.canMgtUser(dob.prof, dob.getUsr().usr_ent_id,
						wizbini.cfgTcEnabled)) {
					throw new qdbErrMessage("ACL002");
				}

				// 检测选定的用户组是否存在
				String[] usr_attribute_relation_types = dob.getUsr().usr_attribute_relation_types;
				long[] usr_attribute_ent_ids = dob.getUsr().usr_attribute_ent_ids;
				if ((usr_attribute_relation_types != null || usr_attribute_ent_ids != null)
						&& !dbRegUser.isUsgExists(con,
								usr_attribute_relation_types,
								usr_attribute_ent_ids)) {
					throw new qdbErrMessage("USG011");
				}

				// 检测选定的下属部门是否存在
				long[] supervise_target_ent_ids = cwUtils
						.stringArray2LongArray(dob.getUsr().supervise_target_ent_ids);
				if (supervise_target_ent_ids != null
						&& supervise_target_ent_ids.length > 0
						&& !dbRegUser
								.isUsgExists(con, supervise_target_ent_ids)) {
					throw new qdbErrMessage("USG011");
				}
				// 检测最高报名审批用户组是否存在
				if (dob.getUsr().usr_app_approval_usg_ent_id != 0
						&& !dbUserGroup.isUsgExists(con,
								dob.getUsr().usr_app_approval_usg_ent_id)) {
					throw new qdbErrMessage("USG011");
				}

				dob.getUsr().apprRegisterUser(con, dob.prof, dob.vColName,
						dob.vColType, dob.vColValue, dob.vClobColName,
						dob.vClobColValue, dob.vExtColName, dob.vExtColType,
						dob.vExtColValue, dob.vExtClobColName,
						dob.vExtClobColValue, dob.msg_title,
						wizbini.cfgTcEnabled);
				if (dob.bFileUpload) {
					this.procUploadUsrFace(con, dob.tmpUploadDir,
							dob.getUsr().usr_ent_id, dob.remain_photo_ind, sess);
				}
				con.commit();
				cwSysMessage e = new cwSysMessage("USR013");
				msgBox(MSG_STATUS, con, e, dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DISAPPR_USR")) {
			try {

				AcRegUser acusr = new AcRegUser(con);
				if (!acusr.hasApprovalPrivilege(dob.prof,
						dob.getUsr().usr_ent_id, wizbini.cfgTcEnabled)) {
					// DENNIS: TODO: new SystemMessage
					throw new qdbErrMessage("USR003");
				}
				if (!acusr.canMgtUser(dob.prof, dob.getUsr().usr_ent_id,
						wizbini.cfgTcEnabled)) {
					throw new qdbErrMessage("ACL002");
				}

				dob.getUsr().disapprRegisterUser(con, dob.prof, dob.msg_title);
				con.commit();
				cwSysMessage e = new cwSysMessage("USR014");
				msgBox(MSG_STATUS, con, e, dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_EMAIL_PWD")) {
			// deprecated
			/*
			 * try { AcRegUser acusr = new AcRegUser(con);
			 * if(!acusr.hasUpdPrivilege(dob.prof.usr_ent_id,
			 * dob.prof.root_ent_id, dob.prof.current_role, dob.usr.usr_ent_id,
			 * dob.usr.usr_roles)) { throw new qdbErrMessage("USR003"); }
			 * dob.usr.updEmailNPwd(con); con.commit();
			 * response.sendRedirect(dob.url_success);
			 * 
			 * } catch(qdbErrMessage e) { con.rollback(); msgBox(MSG_ERROR, con,
			 * e, dob, out); return; }
			 */
		} else if (dob.prm_ACTION.equalsIgnoreCase("ASSIGN_USR_ROL")) {
			try {
				AcRegUser acusr = new AcRegUser(con);
				if (!acusr.hasAssignRolePrivilege(dob.prof.usr_ent_id,
						dob.prof.current_role, dob.getUsr().usr_ent_id,
						dob.getUsr().usr_roles)) {
					throw new qdbErrMessage("USR004");
				}
				dob.getUsr().assignUserRoles(con, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("PASTE_ENT")) {
			try {
				AcUserGroup acUsg = new AcUserGroup(con);
				if (!acUsg.hasManagePrivilege(dob.prof.current_role)) {
					throw new qdbErrMessage("USR002");
				}
				if (!acUsg.canManageGroup(dob.prof, dob.ent_id_parent,
						wizbini.cfgTcEnabled)) {
					throw new qdbErrMessage("ACL002");
				}

				EnterpriseInfoPortalBean eipBean = null;
				if (qdbAction.wizbini.cfgSysSetupadv.isTcIndependent()) {
					eipBean = EnterpriseInfoPortalDao.getEipByTcrID(con,
							dob.prof.my_top_tc_id);
					if (eipBean != null
							&& !dob.prof.current_role.startsWith("ADM")
							&& dob.prof.my_top_tc_id != DbTrainingCenter
									.getSuperTcId(con, dob.prof.root_ent_id)) {
						long totalNum = eipBean.getEip_account_num();
						long dSize = totalNum - eipBean.getAccount_used();
						if (dob.ent_id_lst != null
								&& dob.ent_id_lst.length > dSize) {
							throw new cwSysMessage("lab_user_full_on_paste");
						}
					}
				}

				// 检测用户组是否存在
				if (!dbUserGroup.isUsgExists(con, dob.ent_id_parent)) {
					throw new qdbErrMessage("USG011");
				}
				// 企业租用账户数是否已经达到上限
				// EnterpriseInfoPortal.checkEIPAccountLimited(con,
				// dob.ent_id_parent, 0, 0, false);

				for (int i = 0; i < dob.ent_id_lst.length; i++) {
					dob.dbusg.usg_ent_id = dob.ent_id_lst[i];
					dob.dbusg.ent_id = dob.ent_id_lst[i];
					dob.dbusg.paste(con, dob.prof, dob.ent_id_parent);
				}
				// dob.dbusg.paste(con,dob.prof, dob.ent_id_parent);
				con.commit();

				cwSysMessage e = new cwSysMessage("ENT004");
				msgBox(MSG_STATUS, con, e, dob, out);
				// response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}
		/*
		 * else if (dob.prm_ACTION.equalsIgnoreCase("GET_USR") ||
		 * dob.prm_ACTION.equalsIgnoreCase("GET_USR_XML")) {
		 * 
		 * AcRegUser acusr = new AcRegUser(con);
		 * if(!acusr.hasGetPrivilege(dob.prof.usr_ent_id,
		 * dob.prof.current_role)){ dob.usr.usr_ent_id = dob.prof.usr_ent_id; }
		 * 
		 * dob.usr.get(con); String result = dob.usr.asXML(con,dob.prof);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_USR_XML"))
		 * static_env.outputXML(out, result);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_USR")) generalAsHtml(result,
		 * out , dob);
		 * 
		 * }
		 */
		else if (dob.prm_ACTION.equalsIgnoreCase("UPD_USR_PWD")) {
			try {
				AcRegUser acusr = new AcRegUser(con);
				if (!acusr.hasUpdPrivilege(dob.prof.usr_ent_id,
						dob.prof.root_ent_id, dob.prof.current_role,
						dob.getUsr().usr_ent_id, dob.getUsr().usr_roles)) {
					throw new qdbErrMessage("USR003");
				}

				if (dob.getUsr().checkOldPwd(con)) {
					dob.getUsr().updPwd(con, dob.prof);
					dob.prof.bNeedToChangePwd = false;
					con.commit();
					cwSysMessage e = new cwSysMessage("USR016");
					msgBox(MSG_STATUS, con, e, dob, out);
				}
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("RESET_USR_PWD")) {
			try {
				String method = request.getMethod();
				if (acSite.getMethodCallBack(method)) {
					out.print(acSite.DOMAIN_REQUEST_ERROR);
					return;
				}
				AcRegUser acusr = new AcRegUser(con);
				if (!acusr.hasUpdPrivilege(dob.prof.usr_ent_id,
						dob.prof.root_ent_id, dob.prof.current_role,
						dob.getUsr().usr_ent_id, dob.getUsr().usr_roles)) {
					throw new qdbErrMessage("USR003");
				}
				if (!acusr.canMgtUser(dob.prof, dob.getUsr().usr_ent_id,
						wizbini.cfgTcEnabled)) {
					throw new qdbErrMessage("USR004");
				}

				dob.getUsr().updPwd(con, dob.getUsr().usr_pwd_need_change_ind,
						dob.prof);

				// 用户修改密码后，不管是学员自己修改，或管理员帮他修改。修改完后要自动解除微信绑定
				WechatService wechatService = (WechatService) WzbApplicationContext
						.getBean("wechatService");
				wechatService
						.unbindWizbankByUserSteEntId(dob.getUsr().usr_ste_usr_id);

				con.commit();
				dob.getUsr().get(con);
				ObjectActionLog log = new ObjectActionLog(dob.getUsr().ent_id,
						dob.getUsr().usr_ste_usr_id,
						dob.getUsr().usr_display_bil,
						ObjectActionLog.OBJECT_TYPE_USR,
						ObjectActionLog.OBJECT_ACTION_UPD_PWD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);
				cwSysMessage e = new cwSysMessage("USR016");
				msgBox(MSG_STATUS, con, e, dob, out);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_USR")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_USR_XML")) {
			/*
			 * if (!dob.prof.sysRoles.contains(dbUserGroup.USG_ROLE_ADMIN)) {
			 * dob.usr.usr_ent_id = dob.prof.usr_ent_id; }
			 */

			/*
			 * AcRegUser acusr = new AcRegUser(con);
			 * if(!acusr.hasGetPrivilege(dob.prof.usr_ent_id,
			 * dob.prof.current_role)){ dob.usr.usr_ent_id =
			 * dob.prof.usr_ent_id; }
			 */
			/*
			 * AccessControlWZB acl = new AccessControlWZB();
			 * if(!acl.hasUserPrivilege(con, dob.prof.usr_ent_id,
			 * dob.prof.current_role, AcRegUser.FTN_USR_MGT_ADMIN) &&
			 * !acl.hasUserPrivilege(con, dob.prof.usr_ent_id,
			 * dob.prof.current_role, AcRegUser.FTN_USR_MGT_NON_ADMIN)) {
			 * dob.usr.usr_ent_id = dob.prof.usr_ent_id; }
			 */
			// else
			// dob.usr.usr_ent_id = dob.usr.getEntId(con);
			// Default get the info of the login user
			try {
				if (dob.getUsr().usr_ent_id == 0) {
					dob.getUsr().usr_ent_id = dob.prof.usr_ent_id;
				}
				if (dob.cur_stylesheet != null
						&& dob.cur_stylesheet
								.equalsIgnoreCase("usr_detail_popup.xsl")) {
					// 简要查看用户信息
				} else {
					AcRegUser acUsr = new AcRegUser(con);
					if (!acUsr.canMgtUser(dob.prof, dob.getUsr().usr_ent_id,
							wizbini.cfgTcEnabled)) {
						throw new qdbErrMessage("ACL002");
					}
				}
				dob.getUsr().get(con);
				// dob.usr.getExtension(con);
				String result = dob.getUsr().getUserXML(con, dob.prof, wizbini);
				String metaXML;
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.getUsr().usr_ent_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				acPageVariant.ent_owner_ent_id = dob.prof.root_ent_id;
				acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
				metaXML = acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet));
				metaXML += dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id);
				// String metaXML = dbUtils.getAllRoleAsXML(con);
				result = formatXML(result, metaXML, "user_manager", dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("GET_USR_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_USR"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_ANCESTOR_USG")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_ANCESTOR_USG_XML")) {
			String xml = dob.dbusg.getAncestorUsgXML(con, dob.prof,
					wizbini.cfgTcEnabled);
			AcPageVariant acPageVariant = new AcPageVariant(con);
			acPageVariant.instance_id = dob.getUsr().usr_ent_id;
			acPageVariant.ent_id = dob.prof.usr_ent_id;
			acPageVariant.rol_ext_id = dob.prof.current_role;
			String metaXML = acPageVariant
					.answerPageVariantAsXML((String[]) xslQuestions
							.get(dob.cur_stylesheet));
			metaXML = metaXML
					+ dbRegUser.getUserAttributeInfoXML(wizbini,
							dob.prof.root_id);
			String result = formatXML(xml, metaXML, "user_group", dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_ANCESTOR_USG_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_ANCESTOR_USG"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_USG")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_USG_XML")) {
			dob.getUsr().get(con);
			String result = dob.dbusg.getUserGroupXML(con, dob.prof);
			String metaXML;
			AcPageVariant acPageVariant = new AcPageVariant(con);
			acPageVariant.instance_id = dob.getUsr().usr_ent_id;
			acPageVariant.ent_id = dob.prof.usr_ent_id;
			acPageVariant.rol_ext_id = dob.prof.current_role;
			metaXML = acPageVariant
					.answerPageVariantAsXML((String[]) xslQuestions
							.get(dob.cur_stylesheet));
			metaXML += dbRegUser.getUserAttributeInfoXML(wizbini,
					dob.prof.root_id);

			// String metaXML = dbUtils.getAllRoleAsXML(con);
			result = formatXML(result, metaXML, "user_group", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_USG_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_USG"))
				generalAsHtml(result, out, dob);

		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_PROF")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_PROF_XML")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_SELF_REG_FORM")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_SELF_REG_FORM_XML")) {

			dob.getUsr().usr_ent_id = dob.prof.usr_ent_id;
			// dob.getUsr().get(con);
			// String result = dob.usr.asXML(con,dob.prof);
			StringBuffer extXML = new StringBuffer();
			if (dob.prm_ACTION.equalsIgnoreCase("GET_SELF_REG_FORM")
					|| dob.prm_ACTION.equalsIgnoreCase("GET_SELF_REG_FORM_XML")) {
				extXML.append(dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id));
				extXML.append(DbUserGrade.getDefaultGradeAsXML(con,
						dob.prof.root_ent_id));
			}
			if (dob.dbcos.res_id > 0) {
				extXML.append("<ts_enable>")
						.append(wizbini.cfgSysSetup.getTspace().isEnabled())
						.append("</ts_enable>");
			}
			extXML.append("<tc_enabled>").append(wizbini.cfgTcEnabled)
					.append("</tc_enabled>");
			String is_inner = request.getParameter("is_inner");
			if (is_inner != null && is_inner.equals("true")) {
				extXML.append("<is_inner>").append(is_inner)
						.append("</is_inner>");
			}
			String result = dob.usr.insCosPrepXML(con, dob.prof,
					dob.dbdpo.dpo_view, extXML.toString(), dob.dbobj.obj_id,
					dob.cur_stylesheet, dob.dbcos.cos_res_id);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_PROF_XML")
					|| dob.prm_ACTION.equalsIgnoreCase("GET_SELF_REG_FORM_XML")) {
				static_env.outputXML(out, result);
			} else {
				generalAsHtml(result, out, dob);
			}
		}
		// Christ Qiu
		else if (dob.prm_ACTION.equalsIgnoreCase("GET_PROF_OBJ_PATH")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_PROF_OBJ_PATH_XML")) {
			dob.getUsr().usr_ent_id = dob.prof.usr_ent_id;
			dob.getUsr().get(con);
			String result = dob.getUsr().getProfObjPath(con, dob.prof,
					dob.dbobj.obj_id);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_PROF_OBJ_PATH_XML")) {
				static_env.outputXML(out, result);
			} else {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_SITE_LANG")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_SITE_LANG_XML")) {

			String result = getSiteLangAsXML(con, dob.prof,
					dob.prof.root_ent_id);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_SITE_LANG_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_SITE_LANG"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_TSERVER_INFO")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_TSERVER_INFO_XML")) {

			String result = static_env.getTserverInfoAsXML(dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_TSERVER_INFO_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_TSERVER_INFO"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("INS_USG")) {
			try {
				AcUserGroup acUsg = new AcUserGroup(con);
				if (!acUsg.canManageGroup(dob.prof, dob.ent_id_parent,
						wizbini.cfgTcEnabled)) {
					dob.setUrl_fail(dob.url_fail1);
					throw new qdbErrMessage("ACL002");
				}
				if (!acUsg.hasManagePrivilege(dob.prof.current_role)) {
					throw new qdbErrMessage("USR002");
				}

				dob.dbusg.ins(con, dob.prof, dob.ent_id_parent);
				ObjectActionLog log = new ObjectActionLog(dob.dbusg.usg_ent_id,
						dob.dbusg.ent_ste_uid, dob.dbusg.usg_display_bil,
						ObjectActionLog.OBJECT_TYPE_GRP,
						ObjectActionLog.OBJECT_ACTION_ADD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);
				con.commit();

				cwSysMessage e = new cwSysMessage("USG005");
				msgBox(MSG_STATUS, con, e, dob, out);
				// response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}

		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_USG")) {
			try {
				AcUserGroup acUsg = new AcUserGroup(con);
				if (!acUsg.hasManagePrivilege(dob.prof.current_role)) {
					throw new qdbErrMessage("USR002");
				}
				if (!acUsg.canManageGroup(dob.prof, dob.dbusg.usg_ent_id,
						wizbini.cfgTcEnabled)) {
					dob.setUrl_fail(dob.url_fail1);
					throw new qdbErrMessage("ACL002");
				}

				dob.dbusg.del(con, dob.prof, dob.ent_id_parent);
				con.commit();

				cwSysMessage e = new cwSysMessage("USG006");
				msgBox(MSG_STATUS, con, e, dob, out);
				// response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_USG")) {
			try {
				AcUserGroup acUsg = new AcUserGroup(con);
				if (!acUsg.canManageGroup(dob.prof, dob.dbusg.usg_ent_id,
						wizbini.cfgTcEnabled)) {
					dob.setUrl_fail(dob.url_fail1);
					throw new qdbErrMessage("ACL002");
				}
				if (!acUsg.hasManagePrivilege(dob.prof.current_role)) {
					throw new qdbErrMessage("USR002");
				}

				dob.dbusg.upd(con, dob.prof);
				con.commit();
				ObjectActionLog log = new ObjectActionLog(dob.dbusg.usg_ent_id,
						dob.dbusg.ent_ste_uid, dob.dbusg.usg_display_bil,
						ObjectActionLog.OBJECT_TYPE_GRP,
						ObjectActionLog.OBJECT_ACTION_UPD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);

				cwSysMessage e = new cwSysMessage("USG007");
				msgBox(MSG_STATUS, con, e, dob, out);
				// response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}

		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_OBJ")) {
			try {
				AcTrainingCenter acTc = new AcTrainingCenter(con);
				if (wizbini.cfgTcEnabled) {
					if (dob.dbobj.obj_obj_id_parent == 0) {

					} else {
						String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
								dob.dbobj.obj_obj_id_parent,
								dob.prof.current_role);
						if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
							throw new qdbErrMessage(code);
						}
					}
				}
				AcObjective acObj = new AcObjective(con);

				dob.dbobj.ins(con, dob.prof);
				dob.dbobj.insObjectiveAccess(con, dob.prof,
						wizbini.cfgTcEnabled);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_OBJ")) {
			try {
				dob.dbobj.get(con);
				AcTrainingCenter acTc = new AcTrainingCenter(con);
				if (wizbini.cfgTcEnabled) {
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						throw new qdbErrMessage(code);
					}
				}
				ViewObjectiveAccess voa = new ViewObjectiveAccess();
				AcObjective acObj = new AcObjective(con);
				if (!acObj.hasManagePrivilege(dob.prof.usr_ent_id,
						dob.dbobj.obj_id, dob.prof.current_role)) {
					throw new qdbErrMessage("OBJ001");
				}
				voa.delByObjId(con, dob.dbobj.obj_id);
				dob.dbobj.del(con, dob.prof, dob.types); // From objective
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("del_multi_obj")
				|| dob.prm_ACTION.equalsIgnoreCase("del_multi_obj_xml")) {
			try {
				dob.dbobj.DeleteObjList(con, dob.robs, dob.types, dob.prof);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}

		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_OBJ_ACCESS_CONTROL")) {
			try {
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						throw new qdbErrMessage(code);
					}
				}
				AcObjective acObj = new AcObjective(con);
				if (!acObj.hasAppointPrivilege(dob.prof.usr_ent_id,
						dob.dbobj.obj_id, dob.prof.current_role)) {
					throw new qdbErrMessage("OBJ001");
				}
				if (wizbini.cfgTcEnabled) {
					dob.dbobj.updObjTcrId(con);
				}
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						throw new qdbErrMessage(code);
					}
				}
				dob.dbobj.updateObjectiveAccess(con, dob.prof,
						wizbini.cfgTcEnabled);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_OBJ")) {
			try {
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						throw new qdbErrMessage(code);
					}
				}
				AcObjective acObj = new AcObjective(con);
				if (!acObj.hasManagePrivilege(dob.prof.usr_ent_id,
						dob.dbobj.obj_id, dob.prof.current_role)) {
					throw new qdbErrMessage("OBJ001");
				}
				dob.dbobj.upd(con, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("PASTE_OBJ")) {
			try {
				dob.dbobj.get(con);
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						throw new qdbErrMessage(code);
					}
				}
				AcObjective acObj = new AcObjective(con);
				if (!acObj.hasManagePrivilege(dob.prof.usr_ent_id,
						dob.dbobj.obj_id, dob.prof.current_role)) {
					throw new qdbErrMessage("OBJ001");
				}

				dob.dbobj.paste(con, dob.ent_id_parent);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_OBJ")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_XML")) {
			try {
				dob.dbobj.get(con);
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						if (dob.dbobj.obj_share_ind) {
							dob.dbobj.share_mode = true;
						} else {
							throw new qdbErrMessage(code);
						}
					}
				}
				String result = dob.dbobj.asXML(con, dob.prof, dob.viewRes,
						wizbini.cfgTcEnabled,
						wizbini.cfgSysSetupadv.isTcIndependent());
				if (dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_OBJ"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GEN_QUE")) {
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbmod.genQ(con, dob.dbmsp, dob.prof.usr_id,
						wizbini.getFileUploadResDirAbs(), dob.prof);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_MSP")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_MSP_XML")) {

			dob.dbmsp.get_from_id(con);
			dob.dbmsp.get(con);
			// dob.dbmsp.get(con);
			String result = dob.dbmsp.asXML(dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MSP_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MSP"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_THD_SYN_LOG")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_THD_SYN_LOG_XML")) {
			AccessControlWZB acWzb = new AccessControlWZB();

			if (!AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
					AclFunction.FTN_AMD_SYS_SETTING_MAIN)) {
				throw new cwSysMessage("ACL002");
			}
			String result = formatXML("", null, HOME_PAGE_TAG, dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_THD_SYN_LOG")) {
				generalAsHtml(result, out, dob);
			}
			if (dob.prm_ACTION.equalsIgnoreCase("GET_THD_SYN_LOG_XML")) {
				static_env.outputXML(out, result);
			}

		} else if (dob.prm_ACTION.equalsIgnoreCase("EXPORT_THD_SYN_LOG")
				|| dob.prm_ACTION.equalsIgnoreCase("EXPORT_THD_SYN_LOG_XML")) {
			String rpt_filename = null;
			if (dob.log_type
					.equalsIgnoreCase(dbThresholdSynLog.LOG_TYPE_THRESHOLD)) {
				rpt_filename = dbThresholdSynLog.LAB_LOG_TYPE_THRESHOLD;
			} else if (dob.log_type
					.equalsIgnoreCase(dbThresholdSynLog.LAB_LOG_USER_LOGIN)) {
				rpt_filename = dbThresholdSynLog.LAB_LOG_USER_LOGIN;
			} else if (dob.log_type
					.equalsIgnoreCase(dbThresholdSynLog.LAB_LOG_USER_OPERATION)) {
				rpt_filename = dbThresholdSynLog.LAB_LOG_USER_OPERATION;
			} else {
				rpt_filename = dbThresholdSynLog.LAB_LOG_TYPE_DATA_INTEGRATION;
			}
			response.setHeader("Cache-Control", "");
			response.setHeader("Pragma", "");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ rpt_filename + ".csv;");
			response.setContentType("application/vnd.ms-excel");
			// cwUtils.setContentType("application/vnd.ms-excel", response,
			// wizbini);

			if (dob.log_type
					.equalsIgnoreCase(dbThresholdSynLog.LOG_TYPE_THRESHOLD)) {
				dob.dblog.getThresholdLog(con, dob.last_days,
						dob.sys_log_starttime, dob.sys_log_endtime,
						dob.select_all, out, dob.prof.label_lan);
			} else if (dob.log_type
					.equalsIgnoreCase(dbThresholdSynLog.LAB_LOG_USER_LOGIN)) {
				// 用户登录日志
				dob.dblog.getUserLoginLog(con, dob.last_days,
						dob.sys_log_starttime, dob.sys_log_endtime,
						dob.select_all, out, dob.prof.cur_lan);
			} else if (dob.log_type
					.equalsIgnoreCase(dbThresholdSynLog.LAB_LOG_USER_OPERATION)) {
				// 重要操作对象日志
				dob.dblog.getUserOperationLog(con, dob.last_days,
						dob.sys_log_starttime, dob.sys_log_endtime,
						dob.select_all, out, dob.prof.cur_lan);
			} else {
				dob.dblog.getDataIntegrationLog(con, dob.last_days,
						dob.sys_log_starttime, dob.sys_log_endtime,
						dob.select_all, out, dob.prof.label_lan);
			}
			out.flush();
			out.close();
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_MSP")) {
			try {
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbmod.saveSpec(con, dob.dbmsp, dob.prof);
				dob.dbmod.updMaxScoreForChild(con);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_RES")) {
			try {
				// add the access control first
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						throw new qdbErrMessage(code);
					}
				}
				AcResources acRes = new AcResources(con);
				if (!acRes.hasManagePrivilege(dob.prof.usr_ent_id,
						dob.dbobj.obj_id, dob.prof.current_role)) {
					throw new qdbErrMessage("RES003");
				}
				// set upd user
				dob.dbres.res_upd_user = dob.prof.usr_id;
				dob.dbres.res_usr_id_owner = dob.prof.usr_id;
				if (dob.dbres.res_subtype.equalsIgnoreCase("WCT")) {
					if (dob.dbres.res_src_type != null
							&& dob.dbres.res_src_type
									.equalsIgnoreCase("ZIPFILE")) {
						// check if the index file exist in a zipfile
						ZipFile zipfile = new ZipFile(dob.tmpUploadDir
								+ dbUtils.SLASH + dob.uploaded_filename);
						if (zipfile.getEntry(dob.dbres.res_src_link) == null) {
							throw new qdbErrMessage("RES005");
						}
					}
				}

				if (dob.dbres.res_subtype.equalsIgnoreCase("SSC") == true) {
					// read the aicc files and get the required info. before
					// creating the resource
					if (dob.bFileUpload) {
						File aiccCrsFile = null;
						File aiccCstFile = null;
						File aiccDesFile = null;
						File aiccAuFile = null;
						File aiccOrtFile = null;

						File aiccFolder = null;

						File resFolder = null;
						File infoFile = null;

						String fileName = null;

						// when copy from resource manager
						if (dob.copy_media_from != null
								&& dob.aicc_crs_filename == null) {
							resFolder = new File(
									wizbini.getFileUploadResDirAbs()
											+ dbUtils.SLASH
											+ dob.copy_media_from);
							infoFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + "aicc_info.txt");
							BufferedReader in = null;
							try {
								in = new BufferedReader(new InputStreamReader(
										new FileInputStream(infoFile)));
							} catch (Exception e) {
								throw new IOException(e.toString());
							}
							fileName = in.readLine().trim();
							in.close();

							aiccCrsFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".crs");
							aiccCstFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".cst");
							aiccDesFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".des");
							aiccAuFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".au");
							aiccOrtFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".ort");
						} else {
							if (dob.aicc_crs_filename.length() == 0
									|| dob.aicc_cst_filename.length() == 0
									|| dob.aicc_des_filename.length() == 0
									|| dob.aicc_au_filename.length() == 0) {
								msgBox(MSG_ERROR, con, new qdbErrMessage(
										"AICC01"), dob, out);
								return;
							}
							aiccCrsFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_crs_filename);
							aiccCstFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_cst_filename);
							aiccDesFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_des_filename);
							aiccAuFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_au_filename);
							aiccOrtFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_ort_filename);
						}

						// for valid file
						String validMsg = CourseValidator.validAiccCourse(
								aiccCrsFile, aiccAuFile, aiccDesFile,
								aiccCstFile, aiccOrtFile).trim();
						if (!validMsg.equals("")) {
							String xml = dbUtils.xmlHeader
									+ "<errors>"
									+ ((dob.prof != null) ? dob.prof.asXML()
											: "") + validMsg + "</errors>";
							generalAsHtml(xml, out, "course_error.xsl", null);
							return;
						} else {
							dob.dbres.ins_ssc(con, dob.robs, dob.prof,
									aiccDesFile.getAbsolutePath(),
									aiccAuFile.getAbsolutePath());
						}
					}
				} else if (dob.dbres.res_subtype.equalsIgnoreCase("RES_SCO")) {
					// insert a scorm courseware as a resource
					String imsmanifestPath = dob.tmpUploadDir + dbUtils.SLASH
							+ dob.imsmanifestFileName;

					// valid scorm file
					String validMsg = CourseValidator.validScormFile(new File(
							imsmanifestPath));
					if (validMsg != null && !validMsg.equals("")) {
						String xml = dbUtils.xmlHeader + "<errors>"
								+ ((dob.prof != null) ? dob.prof.asXML() : "")
								+ validMsg + "</errors>";
						generalAsHtml(xml, out, "course_error.xsl", null);
						return;
					}

					dob.dbres.ins_res2(con, dob.robs, dob.prof);
					dob.dbres.ins_res_scorm(con, dob.prof, dob.dbres.res_id,
							imsmanifestPath, dob.cosUrlPrefix, false, wizbini);
				} else if (dob.dbres.res_subtype
						.equalsIgnoreCase("RES_NETG_COK")) {
					File netgCdfFile = new File(dob.tmpUploadDir
							+ dbUtils.SLASH + dob.netg_cdf_filename);

					// valid netg file
					String validMsg = CourseValidator
							.validNetgFile(netgCdfFile);
					if (validMsg != null && !validMsg.equals("")) {
						String xml = dbUtils.xmlHeader + "<errors>"
								+ ((dob.prof != null) ? dob.prof.asXML() : "")
								+ validMsg + "</errors>";
						generalAsHtml(xml, out, "course_error.xsl", null);
						return;
					}

					String cdfPath = netgCdfFile.getAbsolutePath();

					ImportCos myImportCos = new ImportCos();
					Vector vtParentObj = new Vector();
					myImportCos.importNETgCookie(con, dob.prof, dob.domain,
							dob.dbcos, dob.dbmod, cdfPath, vtParentObj, true);

					dob.dbres.res_src_link = dob.dbmod.res_src_link;
					dob.dbres.res_upd_user = dob.prof.usr_id;
					dob.dbres.res_usr_id_owner = dob.prof.usr_id;
					dob.dbres.ins_res2(con, dob.robs, dob.prof);
				} else {
					// insert the moudle
					dob.dbres.ins_res2(con, dob.robs, dob.prof);
				}

				if (dob.bFileUpload) {
					procUploadedFiles(con, request, dob.dbres.res_id,
							dob.tmpUploadDir, dob);
				}
				// create the SkillSoft info. file
				if (dob.dbres.res_subtype.equalsIgnoreCase("SSC") == true) {
					if (dob.copy_media_from != null
							&& dob.aicc_crs_filename == null) {
						// do nothing
					} else {
						String courseName = dob.aicc_des_filename.substring(0,
								dob.aicc_des_filename.indexOf("."));
						dob.dbres.create_SSC_info_file(
								wizbini.getFileUploadResDirAbs(), courseName);
					}
				}
				if (dob.dbres.res_subtype.equalsIgnoreCase("RES_SCO")) {
					if (dob.copy_media_from != null
							&& dob.aicc_crs_filename == null) {
						// do nothing
					} else {
						dob.dbres.create_SSC_info_file(
								wizbini.getFileUploadResDirAbs(),
								dob.imsmanifestFileName);
					}
				}
				if (dob.dbres.res_subtype.equalsIgnoreCase("RES_NETG_COK")) {
					if (dob.copy_media_from != null
							&& dob.aicc_crs_filename == null) {
						// do nothing
					} else {
						dob.dbres.create_SSC_info_file(
								wizbini.getFileUploadResDirAbs(),
								dob.netg_cdf_filename);
					}
				}
				con.commit();

				cwSysMessage e = new cwSysMessage("GEN001",
						Long.toString(dob.dbres.res_id));
				msgBox(MSG_STATUS, con, e, dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_RES")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_RES_XML")) {
			try {
				dob.dbres.res_in_subtype = dob.dbres.res_subtype;
				// Get the template info for the resource
				boolean bTemplate = false;
				if (dob.dbres.res_in_subtype != null
						&& dob.dbres.res_in_subtype.length() > 0)
					bTemplate = true;
				share_mode = false;
				dob.dbres.get(con);
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasResInMgtTc(dob.prof.usr_ent_id,
							dob.dbres.res_id, dob.prof.current_role);
					if (!code.equals(dbResource.CAN_MGT_RES)) {
						if (dbResource.checkResObjShared(con, dob.dbres.res_id)) {
							share_mode = true;
						} else {
							throw new qdbErrMessage(code);
						}
					}
				}
				String result = dob.dbres.asXML(con, bTemplate, dob.prof,
						dob.dbdpo.dpo_view, dob.cur_stylesheet, share_mode);

				if (dob.res_read_ind) {
					Vector mod_id_vec = (Vector) sess
							.getAttribute(AUTH_MOD_IDS);

					if (mod_id_vec == null) {
						mod_id_vec = new Vector();
					}

					mod_id_vec.addElement(new Long(dob.dbres.res_id));
					sess.setAttribute(AUTH_MOD_IDS, mod_id_vec);
				}

				if (dob.prm_ACTION.equalsIgnoreCase("GET_RES_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_RES"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("DEL_RES")) {
			// set upd user
			dob.dbres.res_upd_user = dob.prof.usr_id;
			try {
				AcResources acRes = new AcResources(con);
				long res_id1 = new Long(dob.res_id_lst[0]).longValue();
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasResInMgtTc(dob.prof.usr_ent_id,
							res_id1, dob.prof.current_role);
					if (!code.equals(dbResource.CAN_MGT_RES)) {
						throw new qdbErrMessage(code);
					}
				}
				if (!acRes.hasManagePrivilege(dob.prof.usr_ent_id, res_id1,
						dob.prof.current_role)) {
					throw new qdbErrMessage("RES002");
				}

				dob.dbres.removeResLst(con, dob.res_id_lst, dob.prof);
				con.commit();

				msgBox(MSG_STATUS, con, new qdbErrMessage("GEN002"), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_OBJ_RES")) {
			try {
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						throw new qdbErrMessage(code);
					}
				}
				AcResources acRes = new AcResources(con);
				if (!acRes.hasManagePrivilege(dob.prof.usr_ent_id,
						dob.dbobj.obj_id, dob.prof.current_role)) {
					throw new qdbErrMessage("RES002");
				}
				// Physical Delete the resources one by one
				for (int i = 0; i < dob.res_id_lst.length; i++) {
					dob.dbobj.delResources(con, wizbini,
							Long.parseLong(dob.res_id_lst[i].trim()));
				}
				con.commit();

				msgBox(MSG_STATUS, con, new qdbErrMessage("OBJ005"), dob, out);
				// msgBox(MSG_STATUS, con, new qdbErrMessage("OBJ007"), dob,
				// out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("TRASH_OBJ_RES")) {
			try {
				AcResources acRes = new AcResources(con);
				if (!acRes.checkResPermission(dob.prof, dob.res_id_lst)) {
					throw new qdbErrMessage("RES002");
				}

				dbObjective.trashObjRes(con, dob.prof, dob.res_id_lst);
				for (int i = 0; i < dob.res_id_lst.length; i++) {
					delUploadedFiles(Long.parseLong(dob.res_id_lst[i]));
				}
				con.commit();
				msgBox(MSG_STATUS, con, new qdbErrMessage("OBJ007"), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("CP_OBJ_RES_LST")) {

			try {
				AcResources acRes = new AcResources(con);
				if (!acRes.hasManagePrivilege(dob.prof.usr_ent_id,
						dob.to_obj_id, dob.prof.current_role)) {
					throw new qdbErrMessage("RES003");
				}

				dbObjective.copyPasteObjResLst(con, dob.prof, dob.res_id_lst,
						dob.to_obj_id, dob.dbres.res_privilege,
						wizbini.getFileUploadResDirAbs());
				con.commit();
				msgBox(MSG_STATUS, con, new qdbErrMessage("GEN004"), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("MV_OBJ_RES_LST")) {

			try {
				AcResources acRes = new AcResources(con);
				if (!acRes.checkResPermission(dob.prof, dob.res_id_lst)) {
					throw new qdbErrMessage("RES002");
				}

				dbObjective.cutPasteObjResLst(con, dob.prof, dob.res_id_lst,
						dob.fr_obj_id, dob.to_obj_id);
				con.commit();
				msgBox(MSG_STATUS, con, new qdbErrMessage("GEN004"), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("MV_OBJ_RES")) {
			try {
				String id = dbObjective.cutPasteObjRes(con, dob.prof,
						dob.dbres.res_id, dob.fr_obj_id, dob.to_obj_id);
				con.commit();
				msgBox(MSG_STATUS, con, new qdbErrMessage(id), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_RES_STS")) {
			// set upd user
			dob.dbres.res_upd_user = dob.prof.usr_id;
			try {
				AcResources acRes = new AcResources(con);
				long resId = (new Long(dob.res_id_lst[0])).longValue();
				if (!acRes.hasResPrivilege(dob.prof.usr_ent_id, resId,
						dob.prof.current_role)
						&& !acRes.checkResPermission(dob.prof, resId)) {
					throw new qdbErrMessage("RES002");
				}

				dbResource.updResStatus(con, dob.res_id_lst,
						dob.dbres.res_status, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_RES")) {
			// set upd user
			dob.dbres.res_upd_user = dob.prof.usr_id;
			try {

				AcResources acRes = new AcResources(con);
				long resId = 0;
				if (dob.dbres.res_id != 0) {
					resId = dob.dbres.res_id;
				}
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasResInMgtTc(dob.prof.usr_ent_id,
							resId, dob.prof.current_role);
					if (!code.equals(dbResource.CAN_MGT_RES)) {
						throw new qdbErrMessage(code);
					}
				}
				if (!acRes.hasResPrivilege(dob.prof.usr_ent_id, resId,
						dob.prof.current_role)) {
					throw new qdbErrMessage("RES002");
				}
				if (!dob.uploaded_filename.equals("")) {
					if (dob.dbres.res_type.equalsIgnoreCase("GEN")) {
						if (dob.dbres.res_subtype.equalsIgnoreCase("WCT")) {
							if (dob.dbres.res_src_type != null
									&& dob.dbres.res_src_type
											.equalsIgnoreCase("ZIPFILE")) {
								// check if the index file exist in a zipfile
								ZipFile zipfile = new ZipFile(dob.tmpUploadDir
										+ dbUtils.SLASH + dob.uploaded_filename);
								if (zipfile.getEntry(dob.dbres.res_src_link) == null) {
									throw new qdbErrMessage("RES005");
								}
							}
						}
					}
				}
				// else{
				// StringBuffer src_filename_buf = new StringBuffer();
				// src_filename_buf.append(wizbini.getFileUploadResDirAbs())
				// .append(dbUtils.SLASH)
				// .append(dob.dbres.res_id)
				// .append(dbUtils.SLASH)
				// .append(dob.dbres.res_src_link);
				// File src_file = new File(src_filename_buf.toString());
				// if(!src_file.exists()){
				// throw new qdbErrMessage("RES005");
				// }
				// TODO
				// }
				if (dob.dbres.res_subtype.equalsIgnoreCase("SSC") == true) {
					// when user upload the 5 descritpion files
					if (dob.aicc_crs_filename != null) {
						// valid
						String crsFile = dob.tmpUploadDir + dbUtils.SLASH
								+ dob.aicc_crs_filename;
						String auFile = dob.tmpUploadDir + dbUtils.SLASH
								+ dob.aicc_au_filename;
						String desFile = dob.tmpUploadDir + dbUtils.SLASH
								+ dob.aicc_des_filename;
						String cstFile = dob.tmpUploadDir + dbUtils.SLASH
								+ dob.aicc_cst_filename;
						String ortFile = null;
						if (dob.aicc_ort_filename != null
								&& !dob.aicc_ort_filename.equals("")) {
							ortFile = dob.tmpUploadDir + dbUtils.SLASH
									+ dob.aicc_ort_filename;
						}
						String validMsg = CourseValidator.validAiccCourse(
								crsFile, auFile, desFile, cstFile, ortFile)
								.trim();
						if (!validMsg.equals("")) {
							String xml = dbUtils.xmlHeader
									+ "<errors>"
									+ ((dob.prof != null) ? dob.prof.asXML()
											: "") + validMsg + "</errors>";
							generalAsHtml(xml, out, "course_error.xsl", null);
							return;
						}

						dob.dbres.upd_ssc(con, dob.prof, dob.tmpUploadDir
								+ dbUtils.SLASH + dob.aicc_des_filename,
								dob.tmpUploadDir + dbUtils.SLASH
										+ dob.aicc_au_filename);
					} else {
						dob.dbres.upd_res(con, dob.prof, true);
					}
				} else if (dob.dbres.res_subtype.equalsIgnoreCase("RES_SCO")) {
					if (dob.imsmanifestFileName != null) {
						String imsmanifestPath = dob.tmpUploadDir
								+ dbUtils.SLASH + dob.imsmanifestFileName;
						String validMsg = CourseValidator
								.validScormFile(new File(imsmanifestPath));
						if (validMsg != null && !validMsg.equals("")) {
							String xml = dbUtils.xmlHeader
									+ "<errors>"
									+ ((dob.prof != null) ? dob.prof.asXML()
											: "") + validMsg + "</errors>";
							generalAsHtml(xml, out, "course_error.xsl", null);
							return;
						}
						dob.dbres.upd_res_scorm(con, dob.prof,
								dob.dbres.res_id, imsmanifestPath,
								dob.cosUrlPrefix, false, wizbini);
					}
					dob.dbres.upd_res(con, dob.prof, false);
				} else if (dob.dbres.res_subtype
						.equalsIgnoreCase("RES_NETG_COK")) {
					if (dob.netg_cdf_filename != null) {
						File netgCdfFile = new File(dob.tmpUploadDir
								+ dbUtils.SLASH + dob.netg_cdf_filename);

						String validMsg = CourseValidator
								.validNetgFile(netgCdfFile);
						if (validMsg != null && !validMsg.equals("")) {
							String xml = dbUtils.xmlHeader
									+ "<errors>"
									+ ((dob.prof != null) ? dob.prof.asXML()
											: "") + validMsg + "</errors>";
							generalAsHtml(xml, out, "course_error.xsl", null);
							return;
						}

						String cdfPath = netgCdfFile.getAbsolutePath();

						ImportCos myImportCos = new ImportCos();
						Vector vtParentObj = new Vector();
						myImportCos.importNETgCookie(con, dob.prof, dob.domain,
								dob.dbcos, dob.dbmod, cdfPath, vtParentObj,
								true);
						dob.dbres.res_src_link = dob.dbmod.res_src_link;
					}
					dob.dbres.upd_res(con, dob.prof, false);
				} else {
					dob.dbres.upd_res(con, dob.prof, true);
				}
				con.commit();
				if (dob.bFileUpload) {
					procUploadedFiles(con, request, dob.dbres.res_id,
							dob.tmpUploadDir, dob);
				}

				// create the SkillSoft specific info. file
				if (dob.dbres.res_subtype.equalsIgnoreCase("SSC") == true) {
					// when ther user upload the 5 descritpion files
					if (dob.aicc_crs_filename != null) {
						String courseName = dob.aicc_des_filename.substring(0,
								dob.aicc_des_filename.indexOf("."));
						dob.dbres.create_SSC_info_file(
								wizbini.getFileUploadResDirAbs(), courseName);
					}
				}
				if (dob.dbres.res_subtype.equalsIgnoreCase("RES_SCO")) {
					if (dob.imsmanifestFileName != null) {
						dob.dbres.create_SSC_info_file(
								wizbini.getFileUploadResDirAbs(),
								dob.imsmanifestFileName);
					}
				}
				if (dob.dbres.res_subtype.equalsIgnoreCase("RES_NETG_COK")) {
					if (dob.netg_cdf_filename != null) {
						dob.dbres.create_SSC_info_file(
								wizbini.getFileUploadResDirAbs(),
								dob.netg_cdf_filename);
					}
				}

				// update successful
				msgBox(MSG_STATUS, con, new qdbErrMessage("GEN003"), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("INS_MOD")) {
			// set upd user
			dob.dbcos.res_upd_user = dob.prof.usr_id;
			// dob.dbcos.res_usr_id_owner = dob.prof.usr_id;
			try {
				// insert the moudle
				long modId;
				String modTitle = new String();
				String modAction = "INSERT";
				String modType = dob.dbmod.mod_type;
				String inModStatus = dob.dbmod.res_status;
				

				// EVN是公共调查问卷 SVY是课程调查问卷 新增选中为已发布时进入
				if ((modType.equals("EVN") || modType.equals("SVY"))
						&& inModStatus.equals("ON")) {
					// 没有题目不给发布
					throw new qdbErrMessage("1207");
				}

				/*
				 * if (dob.dbcos.cos_res_id == 0){ AcTNA acTNA = new AcTNA(con);
				 * if ((dob.dbmod.mod_type).equalsIgnoreCase("TNA")) {
				 * if(!acTNA.hasInsPrivilege(dob.prof.usr_ent_id,
				 * dob.prof.current_role)) {
				 * 
				 * throw new
				 * cwSysMessage(dbResourcePermission.NO_RIGHT_INS_MSG); } }
				 * dob.dbmod.res_upd_user = dob.prof.usr_id;
				 * dob.dbmod.res_usr_id_owner = dob.prof.usr_id;
				 * dob.dbmod.ins(con, dob.prof); modId = dob.dbmod.mod_res_id;
				 * modTitle = dob.dbmod.res_title; if
				 * ((dob.dbmod.mod_type).equalsIgnoreCase("TNA")) {
				 * acTNA.assignEntity(dob.dbmod.mod_res_id, dob.prof.usr_ent_id,
				 * dob.prof.current_role, true, dob.prof.usr_id, null); }
				 */
				// }else{
				if (modType.equalsIgnoreCase("ASS")) {

					if (dob.dbmod.res_src_type != null
							&& dob.dbmod.res_src_type
									.equalsIgnoreCase("ZIPFILE")
							&& dob.uploaded_filename.length() > 0) {
						// check if the index file exist in a zipfile
						ZipFile zipfile = new ZipFile(dob.tmpUploadDir
								+ dbUtils.SLASH + dob.uploaded_filename);
						if (zipfile.getEntry(dob.dbmod.res_src_link) == null) {
							throw new qdbErrMessage("RES005");
						}
					}
				}

				// 检测用户组是否存在
				if (!dob.dbmod.mod_type.equalsIgnoreCase("EVN")
						&& dob.usr_ent_id_lst != null
						&& dob.usr_ent_id_lst.length > 0) {
					long[] usgIds = dbRegUser.getUsgIdsFromEntIds(con,
							dob.usr_ent_id_lst);
					if (!dbRegUser.isUsgExists(con, usgIds)) {
						throw new qdbErrMessage("USG011");
					}
				} else if (dob.dbmod.mod_type.equalsIgnoreCase("EVN")
						&& dob.usr_ent_id_lst != null
						&& dob.usr_ent_id_lst.length > 0) {// 检查调查问卷的发布对象是否存在
					if (!dbRegUser.isUsgExists(con, dob.usr_ent_id_lst)) {
						throw new qdbErrMessage("859");
					}
				}

				// XSS 过滤字段res_desc
				if (null != dob.dbmod.res_desc) {
					dob.dbmod.res_desc = cwUtils.esc4Json(dob.dbmod.res_desc);
				}

				Vector vtCosResId = dob.dbcos.getChildCosResId(con);
				Vector vtNewModId = new Vector();
				long newModId;
				if (dob.dbmod.mod_is_public) {
					AcModule acModule = new AcModule(con);
					if (!AccessControlWZB.hasRolePrivilege(
							dob.prof.current_role,
							AclFunction.FTN_AMD_DEMAND_MGT)) {
						throw new cwSysMessage(
								dbResourcePermission.NO_RIGHT_WRITE_MSG);
					}
					dob.dbmod.res_upd_user = dob.prof.usr_id;
					dob.dbmod.res_usr_id_owner = dob.prof.usr_id;
					dob.dbmod.ins(con, dob.prof);
					if (dbModule.MOD_TYPE_EVN
							.equalsIgnoreCase(dob.dbmod.mod_type)) {
						if (!wizbini.cfgTcEnabled) {
							dob.modTc.mtc_tcr_id = DbTrainingCenter
									.getSuperTcId(con, dob.prof.root_ent_id);
						}
						dob.modTc.mtc_mod_id = dob.dbmod.mod_res_id;
						dob.modTc.mtc_create_usr_id = dob.prof.usr_id;
						dob.modTc.insert(con);
					}
					modId = dob.dbmod.mod_res_id;
					modTitle = dob.dbmod.res_title;
					EvalAccess evaAcc = new EvalAccess();
					evaAcc.batchAppendEvalAccess(con, dob.dbmod.mod_res_id,
							dob.usr_ent_id_lst, dob.prof.usr_id);
					if (dob.dbmod.mod_type.equalsIgnoreCase("EVN")) {
						ObjectActionLog log = new ObjectActionLog(modId, null,
								modTitle, ObjectActionLog.OBJECT_TYPE_EL,
								ObjectActionLog.OBJECT_ACTION_ADD,
								ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
								dob.prof.getUsr_ent_id(),
								dob.prof.getUsr_last_login_date(),
								dob.prof.getIp());
						SystemLogContext.saveLog(log,
								SystemLogTypeEnum.OBJECT_ACTION_LOG);
					}

				} else {
					AcCourse acCos = new AcCourse(con);

					dob.dbcos.res_id = dob.dbcos.cos_res_id;
					dob.dbcos.checkTimeStamp(con);
					if ((dob.dbmod.mod_type).equalsIgnoreCase("ASS")) {
						dob.dbass.res_upd_user = dob.prof.usr_id;
						dob.dbass.res_usr_id_owner = dob.prof.usr_id;

						dob.dbass.mod_mobile_ind = dob.dbmod.mod_mobile_ind;
						modId = dob.dbcos.insAssignment(con, dob.dbass,
								dob.domain, dob.prof, dob.file_desc_lst,
								dob.files);
						modTitle = dob.dbass.res_title;
						// copy the new module to every class
						dob.dbass.mod_mod_res_id_parent = modId;
						for (int i = 0; i < vtCosResId.size(); i++) {
							newModId = ((dbCourse) vtCosResId.get(i))
									.insAssignment(con, dob.dbass, dob.domain,
											dob.prof, dob.file_desc_lst,
											dob.files);
							vtNewModId.addElement(new Long(newModId));
						}
					} else if (dbEvent.isEventType(dob.dbmod.mod_type)) {
						dob.dbevt.res_upd_user = dob.prof.usr_id;
						dob.dbevt.res_usr_id_owner = dob.prof.usr_id;

						modId = dob.dbcos.insEvent(con, dob.dbevt, dob.domain,
								dob.prof);
						modTitle = dob.dbevt.res_title;
					} else if ((dob.dbmod.mod_type).equalsIgnoreCase("CHT")
							|| (dob.dbmod.mod_type).equalsIgnoreCase("VCR")) {
						dob.dbchat.res_upd_user = dob.prof.usr_id;
						dob.dbchat.res_usr_id_owner = dob.prof.usr_id;

						modId = dob.dbcos.insChat(con, dob.dbchat, dob.domain,
								dob.prof);
						modTitle = dob.dbchat.res_title;
						// copy the new module to every class
						dob.dbchat.mod_mod_res_id_parent = modId;
						for (int i = 0; i < vtCosResId.size(); i++) {
							newModId = ((dbCourse) vtCosResId.get(i)).insChat(
									con, dob.dbchat, dob.domain, dob.prof);
							vtNewModId.addElement(new Long(newModId));
						}
					}
					// for NETg
					else if ((dob.dbmod.mod_type).equalsIgnoreCase("NETG_COK")) {
						File netgCdfFile = null;
						String cdfPath = null;
						if (dob.copy_media_from != null) {
							File infoFile = new File(
									wizbini.getFileUploadResDirAbs()
											+ dbUtils.SLASH
											+ dob.copy_media_from
											+ cwUtils.SLASH + "aicc_info.txt");
							BufferedReader in = null;
							try {
								in = new BufferedReader(new InputStreamReader(
										new FileInputStream(infoFile)));
							} catch (Exception e) {
								throw new IOException(e.toString());
							}
							String fileName = in.readLine().trim();
							in.close();
							netgCdfFile = new File(
									wizbini.getFileUploadResDirAbs()
											+ dbUtils.SLASH
											+ dob.copy_media_from
											+ cwUtils.SLASH + fileName);
							cdfPath = netgCdfFile.getAbsolutePath();
						} else {
							netgCdfFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.netg_cdf_filename);
							cdfPath = netgCdfFile.getAbsolutePath();
						}

						String validMsg = CourseValidator
								.validNetgFile(netgCdfFile);
						if (validMsg != null && !validMsg.equals("")) {
							String xml = dbUtils.xmlHeader
									+ "<errors>"
									+ ((dob.prof != null) ? dob.prof.asXML()
											: "") + validMsg + "</errors>";
							generalAsHtml(xml, out, "course_error.xsl", null);
							return;
						}

						dob.dbmod.res_upd_user = dob.prof.usr_id;
						dob.dbmod.res_usr_id_owner = dob.prof.usr_id;

						ImportCos myImportCos = new ImportCos();
						Vector vtParentObj = new Vector();
						modId = myImportCos.importNETgCookie(con, dob.prof,
								dob.domain, dob.dbcos, dob.dbmod, cdfPath,
								vtParentObj, false);

						modTitle = dob.dbmod.res_title;
						// copy the new module to every class
						dob.dbmod.mod_mod_res_id_parent = modId;
						for (int i = 0; i < vtCosResId.size(); i++) {
							newModId = myImportCos.importNETgCookie(con,
									dob.prof, dob.domain,
									((dbCourse) vtCosResId.get(i)), dob.dbmod,
									cdfPath, vtParentObj, false);
							vtNewModId.addElement(new Long(newModId));
						}

						/*
						 * File tempFile = new File(cdfPath); if
						 * (tempFile.exists()) { tempFile.delete(); }
						 */
					} else if ((dob.dbmod.mod_type).equalsIgnoreCase("AICC_AU")) {
						String fileName = null;

						File aiccCrsFile = null;
						File aiccCstFile = null;
						File aiccDesFile = null;
						File aiccAuFile = null;
						File aiccOrtFile = null;

						File resFolder = null;
						File infoFile = null;

						File aiccFolder = null;

						// when copy from resource manager
						if (dob.copy_media_from != null) {
							resFolder = new File(
									wizbini.getFileUploadResDirAbs()
											+ dbUtils.SLASH
											+ dob.copy_media_from);
							infoFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + "aicc_info.txt");
							BufferedReader in = null;
							try {
								in = new BufferedReader(new InputStreamReader(
										new FileInputStream(infoFile)));
							} catch (Exception e) {
								throw new IOException(e.toString());
							}
							fileName = in.readLine().trim();
							in.close();

							aiccCrsFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".crs");
							aiccCstFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".cst");
							aiccDesFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".des");
							aiccAuFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".au");
							aiccOrtFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".ort");
						}
						// when ther user upload the 5 descritpion files
						else {
							if (dob.aicc_crs_filename == null
									|| dob.aicc_crs_filename.length() == 0
									|| dob.aicc_cst_filename == null
									|| dob.aicc_cst_filename.length() == 0
									|| dob.aicc_des_filename == null
									|| dob.aicc_des_filename.length() == 0
									|| dob.aicc_au_filename == null
									|| dob.aicc_au_filename.length() == 0) {
								msgBox(MSG_ERROR, con, new qdbErrMessage(
										"AICC01"), dob, out);
								return;
							}

							aiccCrsFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_crs_filename);
							aiccCstFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_cst_filename);
							aiccDesFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_des_filename);
							aiccAuFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_au_filename);
							aiccOrtFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_ort_filename);
						}

						// for valid file
						String validMsg = CourseValidator.validAiccCourse(
								aiccCrsFile, aiccAuFile, aiccDesFile,
								aiccCstFile, aiccOrtFile).trim();
						if (!validMsg.equals("")) {
							String xml = dbUtils.xmlHeader
									+ "<errors>"
									+ ((dob.prof != null) ? dob.prof.asXML()
											: "") + validMsg + "</errors>";
							generalAsHtml(xml, out, "course_error.xsl", null);
							return;
						} else {
							// set upd user
							dob.dbmod.res_upd_user = dob.prof.usr_id;
							dob.dbmod.res_usr_id_owner = dob.prof.usr_id;
							modTitle = dob.dbmod.res_title;
							Vector vtParentObj = new Vector();
							if (aiccOrtFile.exists()) {
								modId = dob.dbcos.insAiccAu(con, dob.dbmod,
										dob.domain, dob.prof,
										aiccCrsFile.getAbsolutePath(),
										aiccCstFile.getAbsolutePath(),
										aiccDesFile.getAbsolutePath(),
										aiccAuFile.getAbsolutePath(),
										aiccOrtFile.getAbsolutePath(),
										vtParentObj, false);
							} else {
								modId = dob.dbcos.insAiccAu(con, dob.dbmod,
										dob.domain, dob.prof,
										aiccCrsFile.getAbsolutePath(),
										aiccCstFile.getAbsolutePath(),
										aiccDesFile.getAbsolutePath(),
										aiccAuFile.getAbsolutePath(), null,
										vtParentObj, false);
							}

							dob.dbmod.mod_mod_res_id_parent = modId;
							for (int i = 0; i < vtCosResId.size(); i++) {
								long clsModId;
								if (aiccOrtFile.exists()) {
									clsModId = ((dbCourse) vtCosResId.get(i))
											.insAiccAu(con, dob.dbmod,
													dob.domain, dob.prof,
													aiccCrsFile
															.getAbsolutePath(),
													aiccCstFile
															.getAbsolutePath(),
													aiccDesFile
															.getAbsolutePath(),
													aiccAuFile
															.getAbsolutePath(),
													aiccOrtFile
															.getAbsolutePath(),
													vtParentObj, false);
								} else {
									clsModId = ((dbCourse) vtCosResId.get(i))
											.insAiccAu(con, dob.dbmod,
													dob.domain, dob.prof,
													aiccCrsFile
															.getAbsolutePath(),
													aiccCstFile
															.getAbsolutePath(),
													aiccDesFile
															.getAbsolutePath(),
													aiccAuFile
															.getAbsolutePath(),
													null, vtParentObj, false);
								}
								vtNewModId.addElement(new Long(clsModId));
							}

							/*
							 * // clean up the files for the case when the user
							 * upload the 5 files if (dob.copy_media_from ==
							 * null) { if (aiccCrsFile.exists()) {
							 * aiccCrsFile.delete(); } if (aiccCstFile.exists())
							 * { aiccCstFile.delete(); } if
							 * (aiccDesFile.exists()) { aiccDesFile.delete(); }
							 * if (aiccAuFile.exists()) { aiccAuFile.delete(); }
							 * if (aiccOrtFile.exists()) { aiccOrtFile.delete();
							 * }
							 * 
							 * aiccFolder = new File(dob.tmpUploadDir); if
							 * (aiccFolder.exists()) { aiccFolder.delete(); } }
							 */
						}
					} else if (dob.copy_media_from != null
							&& ((dob.dbmod.mod_type).equalsIgnoreCase("TST")
									|| (dob.dbmod.mod_type)
											.equalsIgnoreCase("DXT")
									|| (dob.dbmod.mod_type)
											.equalsIgnoreCase("STX") || (dob.dbmod.mod_type)
										.equalsIgnoreCase("EXC"))) {
						String asm_type = "";

						dbResource myDbResource = new dbResource();
						myDbResource.res_id = Long
								.parseLong(dob.copy_media_from);
						myDbResource.get(con);

						// for dynamic assessment
						if (myDbResource.res_subtype
								.equalsIgnoreCase(dbResource.RES_SUBTYPE_DAS)) {
							asm_type = dbResource.RES_SUBTYPE_DAS;
							if ((dob.dbmod.mod_type).equalsIgnoreCase("TST")
									|| (dob.dbmod.mod_type)
											.equalsIgnoreCase("DXT")) {
								dob.dbmod.mod_type = dbModule.MOD_TYPE_DXT;
								dob.dbmod.res_subtype = dbModule.MOD_TYPE_DXT;
							} else {
								dob.dbmod.mod_type = dbModule.MOD_TYPE_STX;
								dob.dbmod.res_subtype = dbModule.MOD_TYPE_STX;
							}
						} else {
							asm_type = dbResource.RES_SUBTYPE_FAS;
							if ((dob.dbmod.mod_type).equalsIgnoreCase("TST")
									|| (dob.dbmod.mod_type)
											.equalsIgnoreCase("DXT")) {
								dob.dbmod.res_subtype = dbModule.MOD_TYPE_TST;
								dob.dbmod.mod_type = dbModule.MOD_TYPE_TST;
							} else {
								dob.dbmod.res_subtype = dbModule.MOD_TYPE_EXC;
								dob.dbmod.mod_type = dbModule.MOD_TYPE_EXC;
							}
						}

						dob.dbmod.res_upd_user = dob.prof.usr_id;
						dob.dbmod.res_usr_id_owner = dob.prof.usr_id;

						modId = dob.dbcos.insModule(con, dob.dbmod, dob.domain,
								dob.prof);
						modTitle = dob.dbmod.res_title;

						// copy the new module to every class
						dob.dbmod.mod_mod_res_id_parent = modId;
						for (int i = 0; i < vtCosResId.size(); i++) {
							newModId = ((dbCourse) vtCosResId.get(i))
									.insModule(con, dob.dbmod, dob.domain,
											dob.prof);
							vtNewModId.addElement(new Long(newModId));
						}

						dob.dbmod.res_id = modId;
						dob.dbmod.mod_res_id = modId;
						dob.dbmod.selectAssessment(con, dob.prof,
								myDbResource.res_id, asm_type,
								static_env.INI_DIR_UPLOAD, vtNewModId);

						dob.dbmod.res_status = inModStatus;
						dob.dbmod.updateStatus(con, dob.prof);
					} else {
						dob.dbmod.res_upd_user = dob.prof.usr_id;
						dob.dbmod.res_usr_id_owner = dob.prof.usr_id;
						modId = dob.dbcos.insModule(con, dob.dbmod, dob.domain,
								dob.prof);
						modTitle = dob.dbmod.res_title;
						// copy the new module to every class
						dob.dbmod.mod_mod_res_id_parent = modId;
						for (int i = 0; i < vtCosResId.size(); i++) {
							newModId = ((dbCourse) vtCosResId.get(i))
									.insModule(con, dob.dbmod, dob.domain,
											dob.prof);
							vtNewModId.addElement(new Long(newModId));
						}
					}
					// insert successful
					dob.dbcos.cos_structure_xml = dbUtils.subsitute(
							dob.dbcos.cos_structure_xml, "$mod_id_",
							Long.toString(modId));
					dob.dbcos.cos_structure_xml = dbUtils.subsitute(
							dob.dbcos.cos_structure_xml, "$mod_title_",
							dbUtils.esc4XML(modTitle));
					dob.dbcos.cos_structure_xml = dbUtils.subsitute(
							dob.dbcos.cos_structure_xml, "$mod_type_", modType);
					dob.dbcos.cos_structure_json = static_env.transformXML(
							dob.dbcos.cos_structure_xml.replaceAll("&quot;",
									" "), "cos_structure_json_js.xsl", null);
					dob.dbcos.updCosStructure(con);

					for (int i = 0; i < vtCosResId.size(); i++) {
						dbCourse cosObj = (dbCourse) vtCosResId.get(i);
						cosObj.res_upd_user = dob.prof.usr_id;
						cosObj.updCosStructureFromParent(con,
								dob.dbcos.cos_structure_xml);
					}
				}

				// }

				/*
				 * if (dob.itm_id != 0){ DbItemResources dbItmRes = new
				 * DbItemResources(); dbItmRes.ire_itm_id = dob.itm_id;
				 * dbItmRes.ire_res_id = modId; dbItmRes.ire_type =
				 * dob.dbmod.mod_type; dbItmRes.ins(con); }
				 */
				if (dob.bFileUpload) {
					try {
						procUploadedFiles(con, request, modId,
								dob.tmpUploadDir, dob);
					} catch (cwSysMessage e) {
						msgBox(MSG_ERROR, con, e, dob, out);
						return;
					}
					for (int i = 0; i < vtNewModId.size(); i++) {
						try {
							procUploadedFiles(con, request,
									((Long) vtNewModId.get(i)).longValue(),
									dob.tmpUploadDir, dob);
						} catch (cwSysMessage e) {
							msgBox(MSG_ERROR, con, e, dob, out);
							return;
						}

					}
				}

				con.commit();

				cwSysMessage e = null;
				if (inModStatus.equalsIgnoreCase(dbModule.RES_STATUS_ON)
						&& dob.dbmod.res_status
								.equalsIgnoreCase(dbModule.RES_STATUS_OFF)
						&& (modType.equalsIgnoreCase(dbModule.MOD_TYPE_TST) || modType
								.equalsIgnoreCase(dbModule.MOD_TYPE_DXT))) {
					// for a standard test or dynamic test
					// if the input status is online but finally it is saved as
					// offline
					// it must be because the test does not have any
					// question/criterion defined
					e = new cwSysMessage("MOD012", Long.toString(modId));
				} else {
					e = new cwSysMessage("GEN001", Long.toString(modId));
				}
				if (dob.dbcos.cos_res_id == 0) {
					msgBox(MSG_STATUS, con, e, dob, out);
				} else {
					msgBox(MSG_STATUS, con, e, dob, out, modId, modTitle,
							modType, modAction, dob.dbcos.cos_res_id);
				}
				// response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				e.printStackTrace();
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}

		}

		else if (dob.prm_ACTION.equalsIgnoreCase("DEL_MOD")) {
			try {
				long modId;
				long cosId = dbModule.getCosId(con, dob.dbmod.mod_res_id);
				dob.dbcos.cos_res_id = cosId;
				dob.dbcos.res_id = cosId;

				String modTitle = new String();
				String modType = dbResource.getResSubType(con,
						dob.dbmod.mod_res_id);
				String modAction = "DELETE";
				AcModule acMod = new AcModule(con);
				ModulePrerequisiteManagement moduleprerequisitemanagement = new ModulePrerequisiteManagement();
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new cwSysMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}
				// if (cosId != 0){
				// AcCourse acCos = new AcCourse(con);
				// if (!acCos.hasWritePermission(dob.prof,
				// dob.dbcos.cos_res_id)){
				// throw new
				// qdbErrMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
				// }
				// }

				if (moduleprerequisitemanagement.delModPrerequisite(con,
						dob.dbmod.mod_res_id)) {
					// throw new
					// cwSysMessage(ModulePrerequisiteModule.SMSG_DEL_MSG);
					Vector clsModId = dob.dbmod.getChildModResId(con);
					// 如果班级已发布此模块，则不能删除
					if (dbModule.clsStaIsOn(con, clsModId)) {
						throw new cwSysMessage("MOD018");
					}
					if (modType.equalsIgnoreCase("ASS")) {
						// set upd user
						dob.dbass.res_upd_user = dob.prof.usr_id;
						// delete the child module of this parent module
						for (int i = 0; i < clsModId.size(); i++) {
							dbModule dbmod = ((dbModule) clsModId.get(i));
							dbmod.get(con);
							dbAssignment ass = new dbAssignment();
							ass.initialize(dbmod);
							ass.del(con, dob.prof);
						}
						dob.dbass.del(con, dob.prof);
						modId = dob.dbass.ass_res_id;

					} else if (modType.equalsIgnoreCase("FOR")) {
						dob.dbforum = new dbForum(dob.dbmod);
						// set upd user
						dob.dbforum.res_upd_user = dob.prof.usr_id;
						// delete the child module of this parent module
						for (int i = 0; i < clsModId.size(); i++) {
							dbModule dbmod = ((dbModule) clsModId.get(i));
							dbmod.get(con);
							dbForum forum = new dbForum(dbmod);
							forum.del(con, dob.prof);
						}
						dob.dbforum.del(con, dob.prof);
						modId = dob.dbforum.mod_res_id;
					} else if (modType.equalsIgnoreCase("FAQ")) {
						dob.dbfaq = new dbFaq(dob.dbmod);
						// set upd user
						dob.dbfaq.res_upd_user = dob.prof.usr_id;
						// delete the child module of this parent module
						for (int i = 0; i < clsModId.size(); i++) {
							dbModule dbmod = ((dbModule) clsModId.get(i));
							dbmod.get(con);
							dbFaq faq = new dbFaq(dbmod);
							faq.del(con, dob.prof);
						}
						dob.dbfaq.del(con, dob.prof);
						modId = dob.dbfaq.mod_res_id;
					} else if (dbEvent.isEventType(modType)) {
						// set upd user
						dob.dbevt.res_upd_user = dob.prof.usr_id;
						dob.dbevt.del(con, dob.prof);
						modId = dob.dbevt.evt_res_id;
					} else if (modType.equalsIgnoreCase("CHT")
							|| modType.equalsIgnoreCase("VCR")) {
						// set upd user
						dob.dbchat.res_upd_user = dob.prof.usr_id;
						// delete the child module of this parent module
						for (int i = 0; i < clsModId.size(); i++) {
							dbModule dbmod = ((dbModule) clsModId.get(i));
							dbmod.get(con);
							dbChat chat = new dbChat();
							chat.initialize(dbmod, wizbini.cfgSysSetup
									.getTspace().getHost(), wizbini.cfgSysSetup
									.getTspace().getPort(), wizbini.cfgSysSetup
									.getTspace().getRoomPort(),
									wizbini.cfgSysSetup.getTspace()
											.getWwwPort());
							chat.del(con, dob.prof);
						}
						dob.dbchat.del(con, dob.prof);
						modId = dob.dbchat.room_res_id;
					} else if (modType.equalsIgnoreCase("GLO")) {
						// set upd user
						DbCtGlossary glossary = new DbCtGlossary();
						dob.dbmod.res_upd_user = dob.prof.usr_id;
						// delete the child module of this parent module
						for (int i = 0; i < clsModId.size(); i++) {
							dbModule dbmod = ((dbModule) clsModId.get(i));
							dbmod.get(con);
							dbmod.del(con, dob.prof);
						}
						glossary.glo_res_id = dob.dbmod.mod_res_id;
						glossary.delByResId(con);
						dob.dbmod.del(con, dob.prof);
						modId = dob.dbmod.mod_res_id;
					} else if (modType.equalsIgnoreCase("REF")) {
						// set upd user
						dob.dbmod.res_upd_user = dob.prof.usr_id;
						// delete the child module of this parent module
						for (int i = 0; i < clsModId.size(); i++) {
							dbModule dbmod = ((dbModule) clsModId.get(i));
							dbmod.get(con);
							dbmod.del(con, dob.prof);
						}
						Vector vt = DbCtReference.getModuleReferenceList(con,
								(int) dob.dbmod.mod_res_id);
						for (int j = 0; j < vt.size(); j++) {
							DbCtReference dbreference = (DbCtReference) vt
									.get(j);
							dbreference.delete(con);
						}
						dob.dbmod.del(con, dob.prof);
						modId = dob.dbmod.mod_res_id;
					} else {
						// set upd user
						// AcTNA acTNA = new AcTNA(con);
						/*
						 * if ((modType).equalsIgnoreCase("TNA")) {
						 * if(!acTNA.hasModifyPrivilege(dob.dbmod.mod_res_id,
						 * dob.prof.usr_ent_id, dob.prof.current_role,
						 * dob.prof.usrGroups)) {
						 * 
						 * throw new
						 * cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG
						 * ); } acTNA.rmPrivilege(dob.dbmod.mod_res_id,
						 * AcTNA.ALL_ENTITIES, AcTNA.ALL_ROLES,
						 * AcTNA.ALL_FUNCTIONS, true); }
						 */
						dob.dbmod.res_upd_user = dob.prof.usr_id;
						// delete the child module of this parent module
						for (int i = 0; i < clsModId.size(); i++) {
							dbModule dbmod = ((dbModule) clsModId.get(i));
							dbmod.get(con);
							dbmod.del(con, dob.prof);
						}
						dob.dbmod.del(con, dob.prof);
						modId = dob.dbmod.mod_res_id;
					}
					if (!dob.dbmod.mod_is_public) {
						dob.dbcos.cos_res_id = cosId;
						dob.dbcos.cos_structure_json = static_env.transformXML(
								dob.dbcos.cos_structure_xml.replaceAll(
										"&quot;", " "),
								"cos_structure_json_js.xsl", null);
						dob.dbcos.updCosStructure(con);
						// update course structure xml of every class
						Vector vtClsId = dob.dbcos.getChildCosResId(con);
						for (int i = 0; i < vtClsId.size(); i++) {
							dbCourse cosObj = (dbCourse) vtClsId.get(i);
							cosObj.res_upd_user = dob.prof.usr_id;
							cosObj.updCosStructureFromParent(con,
									dob.dbcos.cos_structure_xml);
						}
					}

					/*
					 * if (dob.itm_id != 0){ DbItemResources dbItmRes = new
					 * DbItemResources(); dbItmRes.ire_itm_id = dob.itm_id;
					 * dbItmRes.ire_res_id = modId; dbItmRes.ire_type =
					 * dob.dbmod.mod_type; dbItmRes.del(con); }
					 */
					con.commit();
					cwSysMessage e = new cwSysMessage("GEN002");

					// delete the resource directory of the corresponding module
					dbUtils.delDir(wizbini.getFileUploadResDirAbs()
							+ dbUtils.SLASH + dob.dbmod.mod_res_id);
					for (int i = 0; i < clsModId.size(); i++) {
						dbUtils.delDir(wizbini.getFileUploadResDirAbs()
								+ dbUtils.SLASH
								+ ((dbModule) clsModId.get(i)).mod_res_id);
					}

					if (dob.dbmod.mod_is_public) {
						msgBox(MSG_STATUS, con, e, dob, out);
					} else {
						msgBox(MSG_STATUS, con, e, dob, out, modId, modTitle,
								modType, modAction, cosId);
					}
					// response.sendRedirect(dob.url_success);
				} else {
					con.rollback();
					cwSysMessage e = new cwSysMessage(
							ModulePrerequisiteModule.SMSG_DEL_MSG);
					msgBox(MSG_ERROR, con, e, dob, out);
					// return;
				}

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_MOD")) {
			try {
				long modId;
				long cosId = dbModule.getCosId(con, dob.dbmod.mod_res_id);
				dob.dbcos.cos_res_id = cosId;
				dob.dbcos.res_id = cosId;
				ObjectActionLog log = null;
				AcModule acMod = new AcModule(con);
				
				// 如果是课程内操作课程调查问卷，则不检查是否拥有课程调查问卷权限
				if (dob.dbcos.cos_res_id <= 0) {
					if (dob.dbmod.mod_type.equalsIgnoreCase("SVY")
							&& !AccessControlWZB.hasRolePrivilege(
									dob.prof.current_role,
									AclFunction.FTN_AMD_COS_EVN_MAIN)
							&& AccessControlWZB
									.isRoleTcInd(dob.prof.current_role)) {
						throw new qdbErrMessage(
								dbResourcePermission.NO_RIGHT_WRITE_MSG);
					}
				}
				if (!dob.uploaded_filename.equals("")) {
					if (dob.dbmod.mod_type.equalsIgnoreCase("ASS")
							|| dob.dbmod.mod_type.equalsIgnoreCase("RDG")) {
						if (dob.dbmod.res_src_type != null
								&& dob.dbmod.res_src_type
										.equalsIgnoreCase("ZIPFILE")) {
							// check if the index file exist in a zipfile
							ZipFile zipfile = new ZipFile(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.uploaded_filename);
							if (zipfile.getEntry(dob.dbmod.res_src_link) == null) {
								throw new qdbErrMessage("RES005");
							}
						}
					}
				}
				// XSS 过滤字段res_desc
				if (null != dob.dbmod.res_desc) {
					dob.dbmod.res_desc = cwUtils.esc4Json(dob.dbmod.res_desc);
				}

				// else{
				// StringBuffer src_filename_buf = new StringBuffer();
				// src_filename_buf.append(wizbini.getFileUploadResDirAbs())
				// .append(dbUtils.SLASH)
				// .append(dob.dbmod.mod_res_id)
				// .append(dbUtils.SLASH)
				// .append(dob.dbmod.res_src_link);
				// File src_file = new File(src_filename_buf.toString());
				// if(!src_file.exists()){
				// throw new qdbErrMessage("RES005");
				// }
				// TODO
				// }

				// 检测用户组是否存在
				if (!dob.dbmod.mod_type.equalsIgnoreCase("EVN")
						&& dob.usr_ent_id_lst != null
						&& dob.usr_ent_id_lst.length > 0) {
					long[] usgIds = dbRegUser.getUsgIdsFromEntIds(con,
							dob.usr_ent_id_lst);
					if (!dbRegUser.isUsgExists(con, usgIds)) {
						throw new qdbErrMessage("USG011");
					}
				} else if (dob.dbmod.mod_type.equalsIgnoreCase("EVN")
						&& dob.usr_ent_id_lst != null
						&& dob.usr_ent_id_lst.length > 0) {// 检查调查问卷的发布对象是否存在
					if (!dbRegUser.isUsgExists(con, dob.usr_ent_id_lst)) {
						throw new qdbErrMessage("859");
					}
				}

				// If the module was attempted, don't allow to update src_tye,
				// src_link and template
				boolean useNewFile = true;
				// boolean useNewFile = dob.dbmod.updSourceInfo(con);

				String modTitle = new String();
				String modAction = "UPDATE";
				String inModStatus = dob.dbmod.res_status;
				String modType = dbResource.getResSubType(con,
						dob.dbmod.mod_res_id);
				dob.dbmod.mod_type = modType;
				dob.dbmod.res_subtype = modType;

				Vector clsModId = dob.dbmod.getChildModResId(con);
				boolean isChangeDate = dob.dbmod.isChangeDate(con);
				if (modType.equalsIgnoreCase("ASS")) {
					// set upd user
					dob.dbass.res_upd_user = dob.prof.usr_id;
					dob.dbass.mod_mobile_ind = dob.dbmod.mod_mobile_ind;
					dob.dbass.upd(con, dob.prof, dob.file_desc_lst, dob.files,
							isChangeDate);
					modId = dob.dbass.mod_res_id;
					modTitle = dob.dbass.res_title;
					// update class module
					for (int i = 0; i < clsModId.size(); i++) {
						dbModule dbmod = ((dbModule) clsModId.get(i));
						dbmod.get(con);
						dbmod.initialize(dob.dbmod);
						dbAssignment ass = new dbAssignment();
						ass.initialize(dbmod);
						ass.ass_submission = dob.dbass.ass_submission;
						ass.upd(con, dob.prof, dob.file_desc_lst, dob.files,
								false);
					}
				} else if (dbEvent.isEventType(modType)) {
					dob.dbevt.res_upd_user = dob.prof.usr_id;
					dob.dbevt.upd(con, dob.prof);
					modId = dob.dbevt.mod_res_id;
					modTitle = dob.dbevt.res_title;
				} else if (modType.equalsIgnoreCase("CHT")
						|| modType.equalsIgnoreCase("VCR")) {
					// set upd user
					dob.dbchat.res_upd_user = dob.prof.usr_id;
					dob.dbchat.upd(con, dob.prof, isChangeDate);
					modId = dob.dbchat.room_res_id;
					modTitle = dob.dbchat.res_title;
					// update class module
					for (int i = 0; i < clsModId.size(); i++) {
						dbModule dbmod = ((dbModule) clsModId.get(i));
						dbmod.get(con);
						dbmod.initialize(dob.dbmod);
						dbChat chat = new dbChat();
						chat.initialize(dbmod, wizbini.cfgSysSetup.getTspace()
								.getHost(), wizbini.cfgSysSetup.getTspace()
								.getPort(), wizbini.cfgSysSetup.getTspace()
								.getRoomPort(), wizbini.cfgSysSetup.getTspace()
								.getWwwPort());
						chat.upd(con, dob.prof, false);
					}
				}
				// only if new resource is picked from resource manager OR the 5
				// aicc files are uploaded
				else if (modType.equalsIgnoreCase("AICC_AU")) {
					String fileName = null;

					File aiccCrsFile = null;
					File aiccCstFile = null;
					File aiccDesFile = null;
					File aiccAuFile = null;
					File aiccOrtFile = null;

					File resFolder = null;
					File infoFile = null;

					File aiccFolder = null;

					if (dob.copy_media_from != null
							|| dob.aicc_crs_filename != null) {
						// when copy from resource manager
						if (dob.copy_media_from != null) {
							resFolder = new File(
									wizbini.getFileUploadResDirAbs()
											+ dbUtils.SLASH
											+ dob.copy_media_from);
							infoFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + "aicc_info.txt");
							BufferedReader in = null;
							try {
								in = new BufferedReader(new InputStreamReader(
										new FileInputStream(infoFile)));
							} catch (Exception e) {
								throw new IOException(e.toString());
							}
							fileName = in.readLine().trim();
							in.close();

							aiccCrsFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".crs");
							aiccCstFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".cst");
							aiccDesFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".des");
							aiccAuFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".au");
							aiccOrtFile = new File(resFolder.getAbsolutePath()
									+ dbUtils.SLASH + fileName + ".ort");
						}
						// when ther user upload the 5 descritpion files
						else if (dob.aicc_crs_filename != null) {
							if (dob.aicc_crs_filename.length() == 0
									|| dob.aicc_cst_filename.length() == 0
									|| dob.aicc_des_filename.length() == 0
									|| dob.aicc_au_filename.length() == 0) {
								msgBox(MSG_ERROR, con, new qdbErrMessage(
										"AICC01"), dob, out);
								return;
							}

							aiccCrsFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_crs_filename);
							aiccCstFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_cst_filename);
							aiccDesFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_des_filename);
							aiccAuFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_au_filename);
							aiccOrtFile = new File(dob.tmpUploadDir
									+ dbUtils.SLASH + dob.aicc_ort_filename);
						}
						/*
						 * // use the original description files else {
						 * resFolder = new File(static_env.INI_DIR_UPLOAD +
						 * dbUtils.SLASH + dob.dbmod.mod_res_id); infoFile = new
						 * File(resFolder.getAbsolutePath() + dbUtils.SLASH +
						 * "aicc_info.txt"); BufferedReader in = null; try { in
						 * = new BufferedReader(new InputStreamReader(new
						 * FileInputStream(infoFile))); } catch(Exception e) {
						 * throw new IOException(e.toString()); } fileName =
						 * in.readLine().trim(); in.close();
						 * 
						 * aiccCrsFile = new File(resFolder.getAbsolutePath() +
						 * dbUtils.SLASH + fileName + ".crs"); aiccCstFile = new
						 * File(resFolder.getAbsolutePath() + dbUtils.SLASH +
						 * fileName + ".cst"); aiccDesFile = new
						 * File(resFolder.getAbsolutePath() + dbUtils.SLASH +
						 * fileName + ".des"); aiccAuFile = new
						 * File(resFolder.getAbsolutePath() + dbUtils.SLASH +
						 * fileName + ".au"); aiccOrtFile = new
						 * File(resFolder.getAbsolutePath() + dbUtils.SLASH +
						 * fileName + ".ort"); }
						 */

						if (aiccCrsFile.exists() == false
								|| aiccCstFile.exists() == false
								|| aiccDesFile.exists() == false
								|| aiccAuFile.exists() == false) {
							msgBox(MSG_ERROR, con, new qdbErrMessage("AICC01"),
									dob, out);
							/*
							 * if (aiccCrsFile.exists()) { aiccCrsFile.delete();
							 * } if (aiccCstFile.exists()) {
							 * aiccCstFile.delete(); } if (aiccDesFile.exists())
							 * { aiccDesFile.delete(); } if
							 * (aiccAuFile.exists()) { aiccAuFile.delete(); } if
							 * (aiccOrtFile.exists()) { aiccOrtFile.delete(); }
							 */

							return;
						} else {
							// set upd user
							dob.dbmod.res_upd_user = dob.prof.usr_id;
							modId = dob.dbmod.mod_res_id;
							modTitle = dob.dbmod.res_title;
							if (aiccOrtFile.exists()) {
								dob.dbmod.updAiccAu(con, dob.domain, dob.prof,
										aiccCrsFile.getAbsolutePath(),
										aiccCstFile.getAbsolutePath(),
										aiccDesFile.getAbsolutePath(),
										aiccAuFile.getAbsolutePath(),
										aiccOrtFile.getAbsolutePath());
							} else {
								dob.dbmod.updAiccAu(con, dob.domain, dob.prof,
										aiccCrsFile.getAbsolutePath(),
										aiccCstFile.getAbsolutePath(),
										aiccDesFile.getAbsolutePath(),
										aiccAuFile.getAbsolutePath(), null);
							}
							// update class module
							for (int i = 0; i < clsModId.size(); i++) {
								dbModule dbmod = ((dbModule) clsModId.get(i));
								dbmod.get(con);
								dbmod.initialize(dob.dbmod);
								if (aiccOrtFile.exists()) {
									dbmod.updAiccAu(con, dob.domain, dob.prof,
											aiccCrsFile.getAbsolutePath(),
											aiccCstFile.getAbsolutePath(),
											aiccDesFile.getAbsolutePath(),
											aiccAuFile.getAbsolutePath(),
											aiccOrtFile.getAbsolutePath());
								} else {
									dbmod.updAiccAu(con, dob.domain, dob.prof,
											aiccCrsFile.getAbsolutePath(),
											aiccCstFile.getAbsolutePath(),
											aiccDesFile.getAbsolutePath(),
											aiccAuFile.getAbsolutePath(), null);
								}
							}

							/*
							 * // clean up the files for the case when the user
							 * upload the 5 files if (dob.aicc_crs_filename !=
							 * null) { if (aiccCrsFile.exists()) {
							 * aiccCrsFile.delete(); } if (aiccCstFile.exists())
							 * { aiccCstFile.delete(); } if
							 * (aiccDesFile.exists()) { aiccDesFile.delete(); }
							 * if (aiccAuFile.exists()) { aiccAuFile.delete(); }
							 * if (aiccOrtFile.exists()) { aiccOrtFile.delete();
							 * }
							 * 
							 * aiccFolder = new File(dob.tmpUploadDir); if
							 * (aiccFolder.exists()) { aiccFolder.delete(); } }
							 */
						}
					}
					// use the original description files
					else {
						dob.dbmod.res_upd_user = dob.prof.usr_id;
						modId = dob.dbmod.mod_res_id;
						modTitle = dob.dbmod.res_title;
						dob.dbmod.updBasicInfo(con, dob.prof, isChangeDate);
						dob.dbmod.updateMobileInd(con);
						// update class module
						for (int i = 0; i < clsModId.size(); i++) {
							dbModule dbmod = ((dbModule) clsModId.get(i));
							dbmod.get(con);
							dbmod.initialize(dob.dbmod);
							dbmod.updBasicInfo(con, dob.prof, false);
						}
					}
				} else {
					// set upd user
					// 功能完善wizmobile2.5支持问答题
					// 如果是发布移动端需检查题目是否只有选择题
					// if(dob.modTc.mtc_mobile_ind &&
					// "ON".equalsIgnoreCase(dob.dbmod.res_status)){
					// List<dbResourceContent> ques =
					// dbResourceContent.getChildAss(con,dob.dbmod.res_id);
					// dbQuestion dbq = new dbQuestion();
					// // 处理题目信息
					// for(dbResourceContent content : ques){
					// dbq.res_id = dbq.que_res_id = content.rcn_res_id_content;
					// dbq.get(con);
					// if(!"MC".equals(dbq.que_type)){
					// throw new cwSysMessage("MOB001");
					// }
					// }
					// }

					dob.dbmod.res_upd_user = dob.prof.usr_id;
					dob.dbmod.upd(con, dob.prof, isChangeDate);
					modId = dob.dbmod.mod_res_id;
					modTitle = dob.dbmod.res_title;

					// EVN是公共调查问卷 SVY是课程调查问卷
					if ((modType.equals("EVN") || modType.equals("SVY"))
							&& inModStatus.equals("ON")) {
						Vector qArray = dbResourceContent.getChildAss(con,
								modId);
						if (qArray.size() == 0) { // 没有题目不给发布
							throw new qdbErrMessage("1207");
						}
					}

					// update the training center of module
					if (dbModule.MOD_TYPE_EVN
							.equalsIgnoreCase(dob.dbmod.mod_type)) {
						dob.modTc.mtc_mod_id = dob.dbmod.mod_res_id;
						dob.modTc.update(con);
					}

					EvalAccess evaAcc = new EvalAccess();
					evaAcc.eac_res_id = modId;
					evaAcc.delEvalAccessByRes_ID(con);
					evaAcc.batchAppendEvalAccess(con, dob.dbmod.mod_res_id,
							dob.usr_ent_id_lst, dob.prof.usr_id);

					// update class module
					for (int i = 0; i < clsModId.size(); i++) {
						dbModule dbmod = ((dbModule) clsModId.get(i));
						dbmod.get(con);
						dbmod.initialize(dob.dbmod);
						dbmod.upd(con, dob.prof, false);
					}
					if (dob.dbmod.mod_type.equalsIgnoreCase("EVN")) {
						log = new ObjectActionLog(modId, null, modTitle,
								ObjectActionLog.OBJECT_TYPE_EL,
								ObjectActionLog.OBJECT_ACTION_UPD,
								ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
								dob.prof.getUsr_ent_id(),
								dob.prof.getUsr_last_login_date(),
								dob.prof.getIp());
					}

				}
				if (!dob.dbmod.mod_is_public) {

					// if (!modType.equalsIgnoreCase("EVN") &&
					// !modType.equalsIgnoreCase("TNA")){
					dob.dbcos.cos_structure_xml = dbUtils.subsitute(
							dob.dbcos.cos_structure_xml, "$mod_title_",
							dbUtils.esc4XML(modTitle));
					dob.dbcos.cos_structure_json = static_env.transformXML(
							dob.dbcos.cos_structure_xml.replaceAll("&quot;",
									" "), "cos_structure_json_js.xsl", null);
					dob.dbcos.updCosStructure(con);
					Vector vtClsId = dob.dbcos.getChildCosResId(con);
					for (int i = 0; i < vtClsId.size(); i++) {
						dbCourse cosObj = (dbCourse) vtClsId.get(i);
						cosObj.res_upd_user = dob.prof.usr_id;
						cosObj.updCosStructureFromParent(con,
								dob.dbcos.cos_structure_xml);
					}
				}
				con.commit();
				if (null != log) {
					SystemLogContext.saveLog(log,
							SystemLogTypeEnum.OBJECT_ACTION_LOG);
				}
				if (dob.bFileUpload && useNewFile) {
					procUploadedFiles(con, request, dob.dbmod.mod_res_id,
							dob.tmpUploadDir, dob);
					for (int i = 0; i < clsModId.size(); i++) {
						procUploadedFiles(con, request,
								((dbModule) clsModId.get(i)).mod_res_id,
								dob.tmpUploadDir, dob);
					}
				}

				cwSysMessage e = null;
				String msg_status = MSG_STATUS;
				if (inModStatus.equalsIgnoreCase(dbModule.RES_STATUS_ON)
						&& dob.dbmod.res_status
								.equalsIgnoreCase(dbModule.RES_STATUS_OFF)
						&& (modType.equalsIgnoreCase(dbModule.MOD_TYPE_TST) || modType
								.equalsIgnoreCase(dbModule.MOD_TYPE_DXT))) {
					// for a standard test or dynamic test
					// if the input status is online but finally it is saved as
					// offline
					// it must be because the test does not have any
					// question/criterion defined
					e = new cwSysMessage("MOD007");
					msg_status = MSG_ERROR;
				} else {
					e = new cwSysMessage("GEN003");
				}

				if (!dob.dbmod.mod_is_public) {
					msgBox(msg_status, con, e, dob, out, modId, modTitle,
							modType, modAction, cosId);
				} else {
					msgBox(msg_status, con, e, dob, out);
				}

				// response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_TST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_RPT_TST_XML")) {

			// access control
			AcModule acmod = new AcModule(con);
			if (!acmod.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {

				throw new qdbException("Permission denied.");
			}

			String result = dob.dbmod.getTestReport(con, dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_TST_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_TST"))
				generalAsHtml(result, out, dob);

		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USG")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USG_XML")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {

					throw new qdbException("Permission denied.");
				}

				String result = dob.dbmod.getGroupReport(con, dob.que_id_lst,
						dob.ent_id_parent, dob.dbpgr.pgr_attempt_nbr, dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USG_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_RPT_USG"))
					generalAsHtml(result, out, dob);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("ENROL_ENT")) {
			// to be added

			// no access control

			dob.dbcos.cos_itm_id = dob.dbcos.getCosItemId(con,
					dob.dbcos.cos_res_id);
			dob.dbcos.enroll(con, dob.dbusg.usg_ent_id, dob.prof);
			con.commit();
			response.sendRedirect(dob.url_success);
		} else if (dob.prm_ACTION.equalsIgnoreCase("DROP_ENT")) {
			// to be added
			// no access control

			dob.dbcos.unenroll(con, dob.dbusg.usg_ent_id, dob.prof.root_ent_id);
			con.commit();
			response.sendRedirect(dob.url_success);
		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_COS")) {

			// no access control

			// set upd user
			dob.dbcos.res_upd_user = dob.prof.usr_id;
			dob.dbcos.res_usr_id_owner = dob.prof.usr_id;

			dob.dbcos.ins(con, dob.prof);

			con.commit();
			response.sendRedirect(dob.url_success);

		}

		else if (dob.prm_ACTION.equalsIgnoreCase("UPD_COS")) {

			try {
				// access control
				AcCourse accos = new AcCourse(con);
				if (!accos
						.checkModifyPermission(dob.prof, dob.dbcos.cos_res_id)) {
					throw new cwSysMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbcos.upd(con, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (cwSysMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SAVE_COS_STRUCT")) {
			try {
				// access control
				AcCourse accos = new AcCourse(con);
				if (!accos
						.checkModifyPermission(dob.prof, dob.dbcos.cos_res_id)) {
					throw new cwSysMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbcos.updCosStructure(con, dob.prof);
				Vector vtClsId = dob.dbcos.getChildCosResId(con);
				for (int i = 0; i < vtClsId.size(); i++) {
					dbCourse cosObj = (dbCourse) vtClsId.get(i);
					cosObj.res_upd_user = dob.prof.usr_id;
					cosObj.updCosStructureFromParent(con,
							dob.dbcos.cos_structure_xml);
				}
				con.commit();
				// response.sendRedirect(dob.url_success);
				// Get the updated timestamp
				dob.dbcos.get(con);
				response.setHeader("course_status", "TRUE");
				response.setHeader("course_timestamp",
						dob.dbcos.res_upd_date.toString());
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				String error = e.getSystemMessage("ISO-8859-1");
				if (error == null)
					error = "unknown";
				response.setHeader("course_status", "FALSE");
				response.setHeader("error_message", error);
				CommonLog.info("error_message=" + error);
				response.sendRedirect(dob.getUrl_fail());
				// msgBox(MSG_ERROR, con, e, dob, out);
				return;
			} catch (Exception e) {
				con.rollback();
				String error = e.getMessage();
				if (error == null)
					error = "unknown";
				response.setHeader("course_status", "FALSE");
				response.setHeader("error_message", error);
				CommonLog.error("error_message=" + error);
				response.sendRedirect(dob.getUrl_fail());
				// msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_COS")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_COS_XML")) {

			// no access control

			dob.dbcos.get(con);
			if (!cwTree.enableAppletTree && dob.tree.isCreateTree) {
				String treeXML = "<tree></tree>";
				if (dob.dbcos.cos_structure_xml != null) {
					treeXML = aeUtils.transformXML(dob.dbcos.cos_structure_xml,
							"cos_structure_xml_js.xsl", static_env, null);
				}
				cwUtils.setContentType("text/xml", response, wizbini);
				out = response.getWriter();
				out.println(treeXML);
				return;
			}

			/*
			 * if (dob.dbcos.cos_structure_xml != null || dob.url_redirect ==
			 * null || 如果是考试的话，第一次添加学习单元时，跳过“第一步”页面 dob.url_redirect.length() ==
			 * 0 || aeItem.isExam(con, dob.dbcos.cos_res_id)) {
			 */
			String result = dob.dbcos.asXML(con, dob.prof, dob.dbdpo.dpo_view,
					dob.itm_type, dob.is_new_cos);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_COS"))
				generalAsHtml(result, out, dob);
			/*
			 * } else { this is supposed to be the "Course Wizard" page when
			 * course content is not defined yet (2003-06-27 kawai)
			 * response.sendRedirect(dob.url_redirect); }
			 */
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("DEL_COS")) {

			try {
				// access control
				AcCourse accos = new AcCourse(con);
				if (!accos
						.checkModifyPermission(dob.prof, dob.dbcos.cos_res_id)) {
					throw new cwSysMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbcos.del(con, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_RPT_XML")) {

			dob.dbcos.get(con);
			if (dob.dbcos.cos_structure_xml != null || dob.url_redirect == null
					|| dob.url_redirect.length() == 0) {
				String result = dob.dbcos.getVodCosRptasXML(con, dob.prof);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_RPT")) {
					generalAsHtml(result, out, dob);
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_RPT_XML")) {
					static_env.outputXML(out, result);
				}
			} else {
				response.sendRedirect(dob.url_redirect);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_CONTENT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_CONTENT_XML")) {

			dob.dbcos.get(con);
			if (dob.dbcos.cos_structure_xml != null || dob.url_redirect == null
					|| dob.url_redirect.length() == 0) {
				String result = dob.dbcos.getVodCosContentasXML(con, dob.prof);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_CONTENT")) {
					generalAsHtml(result, out, dob);
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_CONTENT_XML")) {
					static_env.outputXML(out, result);
				}
			} else {
				response.sendRedirect(dob.url_redirect);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_XML")) {

			dob.dbcos.get(con);
			if (dob.dbcos.cos_structure_xml != null || dob.url_redirect == null
					|| dob.url_redirect.length() == 0) {
				String result = dob.dbcos.getVodCosasXML(con, dob.prof,
						dob.dbcos, true);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS")) {
					generalAsHtml(result, out, dob);
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_XML")) {
					static_env.outputXML(out, result);
				}
			} else {
				response.sendRedirect(dob.url_redirect);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_CHAPTER")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_CHAPTER_XML")) {

			dob.dbcos.get(con);
			if (dob.dbcos.cos_structure_xml != null || dob.url_redirect == null
					|| dob.url_redirect.length() == 0) {
				String result = dob.dbcos.getVodCosasXML(con, dob.prof,
						dob.dbcos, false);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_CHAPTER")) {
					generalAsHtml(result, out, dob);
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_VOD_COS_CHAPTER_XML")) {
					static_env.outputXML(out, result);
				}
			} else {
				response.sendRedirect(dob.url_redirect);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("vod_res_main_info")
				|| dob.prm_ACTION.equalsIgnoreCase("vod_res_main_info_xml")) {
			dob.dbres.res_id = dob.dbcos.vod_res_id;
			dob.dbres.get(con);
			String result = dob.dbres.getVodResMainInfo(con);

			AcPageVariant acPageVariant = new AcPageVariant(con);
			acPageVariant.instance_id = dob.dbusg.usg_ent_id;
			acPageVariant.ent_id = dob.prof.usr_ent_id;
			acPageVariant.rol_ext_id = dob.prof.current_role;
			acPageVariant.root_id = dob.prof.root_id;
			acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
			acPageVariant.setWizbiniLoader(qdbAction.wizbini);
			String metaXML = acPageVariant
					.answerPageVariantAsXML((String[]) xslQuestions
							.get(dob.cur_stylesheet));
			metaXML += dbRegUser.getUserAttributeInfoXML(wizbini,
					dob.prof.root_id);

			result = formatXML(result, metaXML, "res_point", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("vod_res_main_info")) {
				generalAsHtml(result, out, dob);
			}
			if (dob.prm_ACTION.equalsIgnoreCase("vod_res_main_info_xml")) {
				static_env.outputXML(out, result);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_HEADER")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_COS_HEADER_XML")) {

			// no access control

			dob.dbcos.getCosHeader(con);

			response.setHeader("course_timestamp",
					dob.dbcos.res_upd_date.toString());
			String result = cwUtils.xmlHeader;
			result += "<course id=\"" + dob.dbcos.cos_res_id
					+ "\" timestamp=\"" + dob.dbcos.res_upd_date.toString()
					+ "\"/>";

			if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_HEADER_XML"))
				static_env.outputXML(out, result);
			else
				generalAsHtml(result, out, dob);

			return;
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_COS_LST_XML")) {
			String result = "";

			// no access control

			// access control on filtering offline course
			AcCourse accos = new AcCourse(con);
			boolean checkStatus = (!accos.hasOffReadPrivilege(
					dob.prof.usr_ent_id, dob.prof.current_role));

			result = dbCourse.getCosListAsXML(con, dob.prof, dob.sort_order,
					dob.dbdpo.dpo_view, checkStatus, null);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_LST_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_LST"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_LRN_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_COS_LRN_LST_XML")) {

			// no access control
			String extXML = dbRegUser.getUserAttributeInfoXML(wizbini,
					dob.prof.root_id);

			String result = dob.dbcos.learnerEnrolAsXML(con, dob.prof, sess,
					dob.cur_page, dob.pagesize, dob.order_by, dob.sort_by,
					extXML, wizbini);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_LRN_LST_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_LRN_LST"))
				generalAsHtml(result, out, dob);

		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_NA_COS")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_NA_COS_XML")) {

			// no access control
			String tkh_id = request.getParameter("tkh_id");
			// String mod_id = request.getParameter("mod_id");
			long ent_id = dob.prof.usr_ent_id;
			dob.dbcos.get(con);
			if (!cwTree.enableAppletTree && dob.tree.isCreateTree) {
				String treeXML = "<tree></tree>";
				if (dob.dbcos.cos_structure_xml != null) {
					String xml = dbUtils.editxml(con,
							dob.dbcos.cos_structure_xml, ent_id,
							dob.dbcos.cos_res_id, tkh_id);
					// dob.dbcos.CosStrtItmIDAndRef(dob.dbcos.cos_structure_xml,"",
					// true);
					treeXML = aeUtils.transformXML(xml,
							"na_structure_xml_js.xsl", static_env, null);
				}
				response.setContentType("text/xml; charset=utf8");
				out = response.getWriter();
				out.println(treeXML);
				return;
			}
			if (dob.dbcos.cos_structure_xml != null || dob.url_redirect == null
					|| dob.url_redirect.length() == 0) {
				String result = dob.dbcos.asXML(con, dob.prof,
						dob.dbdpo.dpo_view, dob.itm_type, false);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_NA_COS_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_NA_COS"))
					generalAsHtml(result, out, dob);
			} else {
				// this is supposed to be the "Course Wizard" page
				// when course content is not defined yet (2003-06-27 kawai)
				response.sendRedirect(dob.url_redirect);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_DEL_ENT_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_DEL_ENT_LST_XML")) {
			try {
				String result = dob.dbusg.getDelMemberListAsXML(con, sess,
						dob.prof, dob.cur_page, dob.pagesize, dob.pagetime,
						false, wizbini.cfgTcEnabled);

				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.dbusg.usg_ent_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				acPageVariant.root_id = dob.prof.root_id;
				acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
				acPageVariant.setWizbiniLoader(qdbAction.wizbini);
				StringBuffer metaXMLBuf = new StringBuffer(
						acPageVariant
								.answerPageVariantAsXML((String[]) xslQuestions
										.get(dob.cur_stylesheet)));
				metaXMLBuf.append(dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id));

				result = formatXML(result, metaXMLBuf.toString(),
						"user_manager", dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("GET_DEL_ENT_LST"))
					generalAsHtml(result, out, dob);
				else
					static_env.outputXML(out, result);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_ENT_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_ENT_LST_XML")) {
			try {
				if (dob.dbusg.usg_ent_id != 0) {
					AcUserGroup ug = new AcUserGroup(con);
					if (!ug.canManageGroup(dob.prof, dob.dbusg.usg_ent_id,
							wizbini.cfgTcEnabled)) {
						throw new qdbErrMessage("USG002");
					}

					if (!dbUserGroup.isUsgExists(con, dob.dbusg.usg_ent_id)) {
						throw new qdbErrMessage("USG011");
					}
				}
				if ("undefined".equals(dob.sortOrder) || dob.sortOrder == null) {
					dob.sortOrder = "DESC";
				}
				dob.dbusg.s_order_by = dob.sortOrder;
				String result = dob.dbusg.getChildListAsXML(con, sess,
						dob.prof, dob.cur_page, dob.pagesize, dob.pagetime,
						false, wizbini);

				StringBuffer metaXMLBuf = new StringBuffer(1024);

				String allowUserMaxSize = null;
				EnterpriseInfoPortalBean eipBean = null;
				if (wizbini.cfgSysSetupadv.isTcIndependent())
					eipBean = EnterpriseInfoPortalDao.getEipByTcrID(con,
							dob.prof.my_top_tc_id);

				long totalNum = 0;
				if (eipBean == null
						|| dob.prof.current_role.startsWith("ADM")
						|| !wizbini.cfgSysSetupadv.isTcIndependent()
						|| dob.prof.my_top_tc_id == DbTrainingCenter
								.getSuperTcId(con, dob.prof.root_ent_id)) {
					allowUserMaxSize = wizbini.cfgSysSetup
							.getAllowUserMaxSize();
					totalNum = dbRegUser.getUserTotalNum(con);
				} else {
					allowUserMaxSize = "" + eipBean.getEip_account_num();
					totalNum = eipBean.getAccount_used();
				}
				if (!"".equals(allowUserMaxSize.trim())) {
					int maxSize = Integer.parseInt(allowUserMaxSize);
					metaXMLBuf.append("<allowUserSize>")
							.append(maxSize - totalNum)
							.append("</allowUserSize>");
				}
				// answer page variants
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.dbusg.usg_ent_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				acPageVariant.root_id = dob.prof.root_id;
				acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
				acPageVariant.ent_owner_ent_id = dob.prof.root_ent_id;
				acPageVariant.setWizbiniLoader(qdbAction.wizbini);
				metaXMLBuf.append(acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet)));
				metaXMLBuf.append(dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id));
				result = formatXML(result, metaXMLBuf.toString(),
						"user_manager", dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("GET_ENT_LST_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_ENT_LST"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("get_meta")
				|| dob.prm_ACTION.equalsIgnoreCase("get_meta_XML")) {
			if (con != null
					&& con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED
					&& cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			}

			StringBuffer metaXMLBuf = new StringBuffer(1024);

			// answer page variants
			AcPageVariant acPageVariant = new AcPageVariant(con);
			acPageVariant.instance_id = dob.dbusg.usg_ent_id;
			acPageVariant.ent_id = dob.prof.usr_ent_id;
			acPageVariant.rol_ext_id = dob.prof.current_role;
			acPageVariant.root_id = dob.prof.root_id;
			acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
			acPageVariant.ent_owner_ent_id = dob.prof.root_ent_id;
			acPageVariant.setWizbiniLoader(qdbAction.wizbini);
			metaXMLBuf.append(acPageVariant
					.answerPageVariantAsXML((String[]) xslQuestions
							.get(dob.cur_stylesheet)));
			metaXMLBuf.append(dbRegUser.getUserAttributeInfoXML(wizbini,
					dob.prof.root_id));
			StringBuffer data = new StringBuffer(1024);
			data.append("<group_member_list>");
			data.append(dbUtils.getAllRoleAsXML(con, "all_role_list",
					dob.prof.root_ent_id));
			data.append("</group_member_list>");

			data.append("<filter_user_group_ind>")
					.append(dob.dbusg.filter_user_group_ind)
					.append("</filter_user_group_ind>");
			String result = formatXML(data.toString(), metaXMLBuf.toString(),
					"user_manager", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("get_meta_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("get_meta"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("DL_ENT_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("DL_ENT_LST_XML")) {

			String result = dob.dbusg.getChildListAsXML(con, sess, dob.prof,
					dob.cur_page, dob.pagesize, dob.pagetime, true, wizbini);
			result = formatXML(result, null, "user_manager", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("DL_ENT_LST_XML")) {
				static_env.outputXML(out, result);
			}
			if (dob.prm_ACTION.equalsIgnoreCase("DL_ENT_LST")) {
				response.setHeader("Cache-Control", "");
				response.setHeader("Pragma", "");
				response.setHeader("Content-Disposition",
						"attachment; filename=userList" + ".txt;");
				cwUtils.setContentType("application/vnd.ms-excel", response,
						wizbini);
				generalAsHtml(result, out, dob);
			}
		}

		/*
		 * else if (dob.prm_ACTION.equalsIgnoreCase("GET_ENT_LST") ||
		 * dob.prm_ACTION.equalsIgnoreCase("GET_ENT_LST_XML")) {
		 * 
		 * //no access control
		 * 
		 * String result = ""; result = dob.dbusg.getMemberListAsXML(con, sess,
		 * dob.prof, dob.cur_page, dob.pagesize, dob.pagetime);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_ENT_LST_XML"))
		 * static_env.outputXML(out, result);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_ENT_LST"))
		 * generalAsHtml(result, out, dob); }
		 */
		/*
		 * else if (dob.prm_ACTION.equalsIgnoreCase("PICK_ENT_LST") ||
		 * dob.prm_ACTION.equalsIgnoreCase("PICK_ENT_LST_XML")) {
		 * 
		 * //for picking users in message if( dob.msg_title != null &&
		 * dob.msg_title.length() > 0 ) sess.setAttribute("MESSAGE_SUBJECT",
		 * dob.msg_title); else sess.setAttribute("MESSAGE_SUBJECT", "");
		 * 
		 * if( dob.msg_body != null && dob.msg_body.length() > 0 )
		 * sess.setAttribute("MESSAGE_CONTENT", dob.msg_body); else
		 * sess.setAttribute("MESSAGE_CONTENT", "");
		 * 
		 * 
		 * if(dob.msg_send_time != null) sess.setAttribute("MESSAGE_SEND_TIME",
		 * dob.msg_send_time); else sess.setAttribute("MESSAGE_SEND_TIME",
		 * "NOW");
		 * 
		 * if( dob.msg_method != null && dob.msg_method.length() > 0 )
		 * sess.setAttribute("MESSAGE_METHOD", dob.msg_method); else
		 * sess.setAttribute("MESSAGE_METHOD", ""); StringBuffer result = new
		 * StringBuffer(); result.append(dob.dbusg.getMemberListAsXML(con, sess,
		 * dob.prof, dob.cur_page, dob.pagesize, dob.pagetime));
		 * 
		 * StringBuffer xml = new StringBuffer().append(dbUtils.NEWL); String
		 * picked_ent = (String)sess.getAttribute("REC_ENT_ID"); Vector
		 * pickedVec = dbUtils.convert2Vec(picked_ent+"~","~");
		 * 
		 * xml.append("<picked_entity>").append(dbUtils.NEWL); for(int i=0;
		 * i<pickedVec.size(); i++) {
		 * xml.append("<entity id=\"").append(pickedVec
		 * .elementAt(i)).append("\"/>").append(dbUtils.NEWL); }
		 * xml.append("</picked_entity>").append(dbUtils.NEWL);
		 * 
		 * int index = (result.toString()).indexOf("</group_member_list>");
		 * result.insert(index, xml.toString());
		 * 
		 * if(dob.prm_ACTION.equalsIgnoreCase("PICK_ENT_LST_XML"))
		 * static_env.outputXML(out, result.toString());
		 * if(dob.prm_ACTION.equalsIgnoreCase("PICK_ENT_LST"))
		 * generalAsHtml(result.toString(), out, dob); }
		 */

		else if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_ENT_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("SEARCH_ENT_LST_XML")) {
			try {

				if (AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
						AclFunction.FTN_AMD_ARTICLE_MAIN)
						&& AccessControlWZB.isRoleTcInd(dob.prof.current_role)) {
					if (AccessControlWZB.isRoleTcInd(dob.prof.current_role)
							&& !ViewTrainingCenter.hasEffTc(con,
									dob.prof.usr_ent_id)) {
						cwSysMessage e = new cwSysMessage("TC015",
								Long.toString(dob.dbque.res_id));
						msgBox(MSG_ERROR, con, e, dob, out);
						return;
					}
				}

				String result = "";
				dob.dbusg.s_ext_col_names = dob.vColName;
				dob.dbusg.s_ext_col_types = dob.vColType;
				dob.dbusg.s_ext_col_values = dob.vColValue;
				dob.dbusg.tc_enabled = wizbini.cfgTcEnabled;
				if ((dob.dbusg.usg_ent_id == 0 && dob.dbusg.s_tcr_id == 0 && dob.dbusg.filter_user_group_ind)
						|| (dob.dbusg.filter_user_group_ind && dob.dbusg.usg_ent_id == dob.prof.root_ent_id)) {
					if (AccessControlWZB.isRoleTcInd(dob.prof.current_role)) {
						Vector usgId = dbUserGroup.getUsgEntIdByUser(con,
								dob.prof.usr_ent_id, dob.prof.current_role,
								wizbini.cfgTcEnabled);
						if (usgId != null && usgId.size() > 0) {
							dob.dbusg.s_usg_ent_id_lst = new String[usgId
									.size()];
							for (int i = 0; i < usgId.size(); i++) {
								dob.dbusg.s_usg_ent_id_lst[i] = String
										.valueOf((Long) usgId.elementAt(i));
							}
						} else if (wizbini.cfgTcEnabled
								&& dob.prof.common_role_id
										.equalsIgnoreCase("TADM")) {
							dob.dbusg.s_usg_ent_id_lst = new String[] { "0" };
						}
					} else {
						Vector usgId = dbUserGroup.getAllGroup(con,
								dob.prof.my_top_tc_id);
						if (usgId != null && usgId.size() > 0) {
							dob.dbusg.s_usg_ent_id_lst = new String[usgId
									.size()];
							for (int i = 0; i < usgId.size(); i++) {
								dob.dbusg.s_usg_ent_id_lst[i] = String
										.valueOf((Long) usgId.elementAt(i));
							}
						}
					}

				} else if (dob.dbusg.usg_ent_id > 0) {
					dob.dbusg.s_usg_ent_id_lst = new String[1];
					dob.dbusg.s_usg_ent_id_lst[0] = String
							.valueOf(dob.dbusg.usg_ent_id);
				}
				result = dob.dbusg.searchEntListAsXML(con, sess, dob.prof,
						dob.page, dob.page_size);
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.getUsr().usr_ent_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				acPageVariant.root_id = dob.prof.root_id;
				acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
				acPageVariant.setWizbiniLoader(qdbAction.wizbini);
				String metaXML = acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet));
				metaXML += dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id);
				String XML = formatXML(result, metaXML, "user_manager",
						dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_ENT_LST_XML"))
					static_env.outputXML(out, XML);
				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_ENT_LST"))
					generalAsHtml(XML, out, dob);
			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("MY_STAFF_ENT_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("MY_STAFF_ENT_LST_XML")) {
			try {

				// no access control

				String result = "";

				String ste_appr_staff_role = acSite.getSteApprStaffRole(con,
						dob.prof.root_ent_id);
				String xml = "<ste_appr_staff_role>"
						+ cwUtils.escNull(ste_appr_staff_role);
				xml += "</ste_appr_staff_role>" + dbUtils.NEWL;
				if (dob.getUsr().usr_ent_id != 0) {
					dob.getUsr().get(con);
					xml += "<cur_staff>";
					xml += dob.getUsr().getUserShortXML(con, false, true,
							false, true);
					xml += "</cur_staff>" + dbUtils.NEWL;
					loginProfile logPro = new loginProfile();
					logPro.usr_ent_id = dob.getUsr().usr_ent_id;
					logPro.root_ent_id = dob.prof.root_ent_id;
					if (ste_appr_staff_role == null
							|| ste_appr_staff_role.equals(""))
						logPro.current_role = dob.prof.current_role;
					else
						logPro.current_role = ste_appr_staff_role;
					result = dob.dbusg.searchEntListAsXML(con, sess, logPro,
							dob.page);
					// result = dob.dbusg.searchEntListAsXML(con, sess,
					// dob.usr.usr_ent_id,dob.prof.root_ent_id,
					// ste_appr_staff_role, dob.page,
					// dbUserGroup.USG_SEARCH_PAGE_SIZE);

				} else
					result = dob.dbusg.searchEntListAsXML(con, sess, dob.prof,
							dob.page);
				// result = dob.dbusg.searchEntListAsXML(con, sess,
				// dob.prof.usr_ent_id, dob.prof.root_ent_id,
				// ste_appr_staff_role, dob.page,
				// dbUserGroup.USG_SEARCH_PAGE_SIZE);

				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.getUsr().usr_ent_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				String metaXML = acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet));
				metaXML += dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id);
				String XML = formatXML(xml + result, metaXML, "user_manager",
						dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("MY_STAFF_ENT_LST_XML"))
					static_env.outputXML(out, XML);
				if (dob.prm_ACTION.equalsIgnoreCase("MY_STAFF_ENT_LST"))
					generalAsHtml(XML, out, dob);
			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_RES_CNT_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_RES_CNT_LST_XML")) {

			if (dob.dbres.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
				dob.dbres.tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con,
						dob.dbres.res_id, dob.prof.usr_ent_id);
			}

			String result = "";
			AcResources acres = new AcResources(con);
			AcModule acmod = new AcModule(con);
			boolean checkStatusRes = (!acres.hasOffReadPrivilege(
					dob.prof.usr_ent_id, dob.prof.current_role));
			boolean checkStatusMod = (!acmod.hasOffReadPrivilege(
					dob.prof.usr_ent_id, dob.prof.current_role));

			result = dob.dbres.getContentListAsXML(con, dob.prof,
					dob.sort_order, 0, dob.dbdpo.dpo_view, dob.cal_d,
					dob.cal_m, dob.cal_y, dob.res_start_datetime,
					dob.res_end_datetime, checkStatusMod, checkStatusRes);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_RES_CNT_LST_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_RES_CNT_LST"))
				generalAsHtml(result, out, dob);

		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_CNT_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_COS_CNT_LST_XML")) {

			// no access control

			// access control on filtering offline course
			AcCourse accos = new AcCourse(con);
			boolean checkStatus = (!accos.hasOffReadPrivilege(
					dob.prof.usr_ent_id, dob.prof.current_role));

			String result = "";
			result = dob.dbcos.getAllCosModAsXML(con, dob.prof,
					dob.dbdpo.dpo_view, dob.dbcos.res_id,
					dob.dbres.res_usr_id_owner,
					dob.dbres.mod_usr_id_instructor, dob.res_start_datetime,
					dob.res_end_datetime, checkStatus, null);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_CNT_LST_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_COS_CNT_LST"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_LAST_VISIT_MOD")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_LAST_VISIT_MOD_XML")) {

			// no access control

			// access control on filtering offline module
			AcModule acmod = new AcModule(con);
			boolean checkStatus = !(acmod.hasOffReadPrivilege(
					dob.prof.usr_ent_id, dob.prof.current_role));

			String result = "";
			if (dob.dbcos.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
				dob.dbcos.tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con,
						dob.dbcos.res_id, dob.prof.usr_ent_id);
			}
			result = dob.dbcos.getLastVisitModAsXML(con, dob.prof,
					dob.dbdpo.dpo_view, dob.dbcos.res_id, dob.num_of_mod,
					checkStatus, null);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LAST_VISIT_MOD_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_LAST_VISIT_MOD"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_RES_CNT_REC")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_RES_CNT_REC_XML")) {
			String result = "";

			if (dob.dbres.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
				dob.dbres.tkh_id = DbTrackingHistory.getAppTrackingIDByCos(con,
						dob.dbres.res_id, dob.prof.usr_ent_id);
			}

			// no access control

			// access control on filtering offline res/mod
			AcResources acres = new AcResources(con);
			AcModule acmod = new AcModule(con);
			boolean checkStatusRes = (!acres.hasOffReadPrivilege(
					dob.prof.usr_ent_id, dob.prof.current_role));
			boolean checkStatusMod = (!acmod.hasOffReadPrivilege(
					dob.prof.usr_ent_id, dob.prof.current_role));
			result = dob.dbres.getContentListAsXML(con, dob.prof,
					dob.sort_order, 1, dob.dbdpo.dpo_view, dob.cal_d,
					dob.cal_m, dob.cal_y, dob.res_start_datetime,
					dob.res_end_datetime, checkStatusMod, checkStatusRes);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_RES_CNT_REC_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_RES_CNT_REC"))
				generalAsHtml(result, out, dob);

		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_QUES")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_QUES_XML")) {
			StringBuffer xml = new StringBuffer();
			xml.append("<?xml version=\"1.0\" encoding=\"")
					.append(dbUtils.ENC_UTF).append("\" standalone=\"no\" ?>")
					.append(dbUtils.NEWL).append(dbUtils.NEWL);
			xml.append("<questions>").append(dbUtils.NEWL);
			for (int i = 0; i < dob.que_id_lst.length; i++) {
				dob.dbque.que_res_id = Long.parseLong(dob.que_id_lst[i]);
				dob.dbque.get(con);
				xml.append(dob.dbque.queAsXML(con, dob.prof,
						dob.dbmod.res_type, false, dob.cur_stylesheet));
			}
			xml.append("</questions>");
			if (dob.prm_ACTION.equalsIgnoreCase("GET_QUES_XML"))
				static_env.outputXML(out, xml.toString());
			if (dob.prm_ACTION.equalsIgnoreCase("GET_QUES"))
				generalAsHtml(xml.toString(), out, dob);

		}

		else if (dob.prm_ACTION.equalsIgnoreCase("UPLOAD_MEDIA")) {

			// no access control

			if (dob.bFileUpload == true) {
				// Copy all the uploaded files to a temp directory
				// The default temp direcoty is /resource/temp/sessionid
				String saveDirPath = dob.session_upload_dir;
				dbUtils.moveDir(dob.tmpUploadDir, saveDirPath);
			}
			mediaBox(dob, out);
		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_Q")) {
			try {
				// skip the access control checking because a question in a
				// FSC/DSC does not belong to any objective
				if (dob.robs[0] != null) {
					// now(2004-06-11),add the access control.
					AcResources acRes = new AcResources(con);
					long objId = (new Long(dob.robs[0])).longValue();
					if (wizbini.cfgTcEnabled) {
						AcTrainingCenter acTc = new AcTrainingCenter(con);
						String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
								objId, dob.prof.current_role);
						if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
							throw new qdbErrMessage(code);
						}
					}
					if (!acRes.hasManagePrivilege(dob.prof.usr_ent_id, objId,
							dob.prof.current_role)) {
						throw new qdbErrMessage("RES003");
					}
				}

				// set upd user
				dob.dbque.res_upd_user = dob.prof.usr_id;

				if (dob.dbque.res_usr_id_owner == null
						|| dob.dbque.res_usr_id_owner.length() == 0)
					dob.dbque.res_usr_id_owner = dob.prof.usr_id;

				if (dob.dbque.que_type.equals(dbResource.RES_SUBTYPE_FSC)) {
					// insert a fixed scenario question
					FixedScenarioQue fsc = new FixedScenarioQue();
					fsc.ins(con, dob.robs, dob.prof, dob.dbque);
				} else if (dob.dbque.que_type
						.equals(dbResource.RES_SUBTYPE_DSC)) {
					// insert a dynamic scenario question
					DynamicScenarioQue dsc = new DynamicScenarioQue();
					dsc.ins(con, dob.robs, dob.prof, dob.dbque);
				} else {
					// insert a question
					dob.dbque.ins(con, dob.robs, dbResource.RES_TYPE_QUE);
				}
				long queId = dob.dbque.res_id;

				if (dob.bFileUpload)
					procUploadedFiles(con, request, dob.dbque.res_id,
							dob.tmpUploadDir, dob);

				// TODO
				// dbQuestion.mirrorQue(con, dob.dbque, dob.robs,
				// static_env.INI_DIR_UPLOAD);

				con.commit();
				cwSysMessage e = new cwSysMessage("GEN001",
						Long.toString(queId));
				msgBox(MSG_STATUS, con, e, dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_SUQ")) {
			try {
				// set upd user
				dob.dbque.res_upd_user = dob.prof.usr_id;
				if (dob.dbque.res_usr_id_owner == null
						|| dob.dbque.res_usr_id_owner.length() == 0)
					dob.dbque.res_usr_id_owner = dob.prof.usr_id;
				/*
				 * AcModule acMod = new AcModule(con); if
				 * (!acMod.checkModifyPermission(dob.prof,
				 * Long.parseLong(dob.robs[0]))) { throw new
				 * cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG); }
				 */
				// access control
				/*
				 * AcSuq acSuq = new AcSuq(con);
				 * if(!acSuq.hasInsPrivilege(Long.parseLong(dob.robs[0]),
				 * dob.prof.usr_ent_id, dob.prof.current_role,
				 * dob.prof.usrGroups)) {
				 * 
				 * throw new
				 * cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG); }
				 */
				dob.dbque.ins(con, dob.robs, dob.prof, dbResource.RES_TYPE_SUQ);
				dbModule dbmod = new dbModule(Long.parseLong(dob.robs[0]));
				Vector clsModId = dbmod.getChildModResId(con);
				for (int i = 0; i < clsModId.size(); i++) {
					dbResourceContent resCon = new dbResourceContent();
					resCon.rcn_res_id = Long.parseLong(dob.robs[0]);
					resCon.rcn_res_id_content = dob.dbque.res_id;
					resCon.get(con);
					resCon.rcn_res_id = ((dbModule) clsModId.get(i)).mod_res_id;
					resCon.rcn_rcn_res_id_parent = Long.parseLong(dob.robs[0]);
					resCon.rcn_rcn_sub_nbr_parent = resCon.rcn_sub_nbr;
					if (!resCon.ins(con)) {
						con.rollback();
						throw new qdbException("Failed to add survey question.");
					}
				}
				con.commit();
				if (dob.bFileUpload)
					procUploadedFiles(con, request, dob.dbque.res_id,
							dob.tmpUploadDir, dob);

				cwSysMessage e = new cwSysMessage("GEN001",
						Long.toString(dob.dbque.res_id));
				msgBox(MSG_STATUS, con, e, dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}

		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_Q")) {

			try {
				// want to get the question res_type
				// it is used by access control only
				dbQuestion dbque = new dbQuestion();
				dbque.que_res_id = dob.dbque.que_res_id;
				dbque.get(con);

				// access control
				/*
				 * if(dbque.res_type != null &&
				 * dbque.res_type.equals(dbResource.RES_TYPE_SUQ)) {
				 * 
				 * //test access control for survey question AcSuq acSuq = new
				 * AcSuq(con);
				 * if(!acSuq.hasModifyPrivilege(dob.dbque.que_res_id,
				 * dob.prof.usr_ent_id, dob.prof.current_role,
				 * dob.prof.usrGroups)){
				 * 
				 * throw new
				 * cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG); } }
				 * else {
				 */
				if (dbque.res_mod_res_id_test > 0) {
					dbResource myDbResource = new dbResource();
					myDbResource.res_id = dbque.res_mod_res_id_test;
					myDbResource.get(con);

					if (myDbResource.res_type
							.equalsIgnoreCase(dbResource.RES_TYPE_MOD)) {
						// check User Right on the Module/Container
						AcModule acmod = new AcModule(con);
						AcResources acres = new AcResources(con);
						if (!acmod.checkModifyPermission(dob.prof,
								dbque.res_mod_res_id_test)
								&& !acres.checkResPermission(dob.prof,
										dbque.res_mod_res_id_test)) {
							throw new cwSysMessage(
									dbResourcePermission.NO_RIGHT_WRITE_MSG);
						}
					} else {
						// check User Right on the Container
						boolean needPermissionCheck = true;
						if (myDbResource.res_subtype
								.equals(dbResource.RES_SUBTYPE_FSC)
								|| myDbResource.res_subtype
										.equals(dbResource.RES_SUBTYPE_DSC)) {
							needPermissionCheck = !dbQuestion.isQueInTst(con,
									myDbResource.res_id);
						}

						if (needPermissionCheck) {
							AcResources acres = new AcResources(con);
							if (!acres.hasResPrivilege(dob.prof.usr_ent_id,
									dbque.res_mod_res_id_test,
									dob.prof.current_role)) {
								throw new cwSysMessage(
										dbResourcePermission.NO_RIGHT_WRITE_MSG);
							}
						}
					}
				} else {
					AcResources acRes = new AcResources(con);
					if (!acRes.hasResPrivilege(dob.prof.usr_ent_id,
							dob.dbque.res_id, dob.prof.current_role)) {
						throw new qdbErrMessage("RES002");
					}
				}
				// }

				if (dbque.que_type.equals(dbResource.RES_SUBTYPE_FSC)) {
					FixedScenarioQue fsc = new FixedScenarioQue();
					dob.dbque.que_type = dbque.que_type;
					fsc.upd(con, dob.robs, dob.prof, dob.dbque);
				} else if (dbque.que_type.equals(dbResource.RES_SUBTYPE_DSC)) {
					DynamicScenarioQue dsc = new DynamicScenarioQue();
					dob.dbque.que_type = dbque.que_type;
					dsc.upd(con, dob.robs, dob.prof, dob.dbque);
				} else {
					dob.dbque.upd(con, dob.robs, dob.prof);
				}

				if (dbque.res_mod_res_id_test > 0) {
					Timestamp curTime = dbUtils.getTime(con);
					dbResource.updResUpdDate(con, dbque.res_mod_res_id_test,
							curTime, dob.prof.usr_id);
				}

				if (dob.bFileUpload)
					procUploadedFiles(con, request, dob.dbque.res_id,
							dob.tmpUploadDir, dob);

				// TODO
				// dbQuestion.mirrorQue(con, dob.dbque, dob.robs,
				// static_env.INI_DIR_UPLOAD);
				con.commit();

				// out.println("UPDATE SUCCESSFUL.");
				msgBox(MSG_STATUS, con, new qdbErrMessage("GEN003"), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			} catch (cwSysMessage se) {
				con.rollback();
				msgBox(MSG_ERROR, con, se, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_Q")) {
			// set upd user
			dob.dbque.res_upd_user = dob.prof.usr_id;

			// want to get the question res_type
			// it is used by access control only
			dbQuestion dbque = new dbQuestion();
			dbque.que_res_id = dob.dbque.que_res_id;
			dbque.get(con);

			// access control
			if (dbque.res_type != null
					&& dbque.res_type.equals(dbResource.RES_TYPE_SUQ)) {

				if (dbque.res_mod_res_id_test > 0) {
					// check User Right on the Module
					AcModule acmod = new AcModule(con);
					if (!acmod.checkModifyPermission(dob.prof,
							dbque.res_mod_res_id_test)) {
						throw new cwSysMessage(
								dbResourcePermission.NO_RIGHT_WRITE_MSG);
					}
				}
				// test access control for survey question
				// AcSuq acSuq = new AcSuq(con);
				// if(!acSuq.hasModifyPrivilege(dob.dbque.que_res_id,
				// dob.prof.usr_ent_id,
				// dob.prof.current_role, dob.prof.usrGroups)){
				//
				// throw new
				// cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
				// }
			} else {
				// check User Right on the Question
				AcResources acres = new AcResources(con);
				if (!acres.checkResPermission(dob.prof, dbque.que_res_id)) {
					throw new cwSysMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}
			}

			try {
				dob.dbque.del(con, dob.prof);
				con.commit();
				// out.println("DELETE SUCCESSFUL.");
				msgBox(MSG_STATUS, con, new qdbErrMessage("GEN002"), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_QLST_STS")) {
			// set upd user
			try {
				// access control
				long que_res_id;
				AcResources acres = new AcResources(con);
				for (int i = 0; i < dob.que_id_lst.length; i++) {
					que_res_id = Long.parseLong(dob.que_id_lst[i]);
					if (!acres.checkResPermission(dob.prof, que_res_id)) {
						throw new cwSysMessage(
								dbResourcePermission.NO_RIGHT_WRITE_MSG);
					}
				}
				dbQuestion.updQueLstStatus(con, dob.prof, dob.que_id_lst,
						dob.dbque.res_status);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_QLST")) {
			// set upd user
			try {
				// access control
				long que_res_id;
				AcResources acres = new AcResources(con);
				for (int i = 0; i < dob.que_id_lst.length; i++) {
					que_res_id = Long.parseLong(dob.que_id_lst[i]);
					if (!acres.checkResPermission(dob.prof, que_res_id)) {
						throw new cwSysMessage(
								dbResourcePermission.NO_RIGHT_WRITE_MSG);
					}
				}
				dbQuestion.removeQueLst(con, dob.prof, dob.que_id_lst);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}
		/*
		 * else if (perl.match("#GET_SYB_LIST#i", dob.prm_ACTION)) {
		 * 
		 * //no access control
		 * 
		 * String result = ""; result = dbSyllabus.getSybListAsXML(con,
		 * dob.dbsyb.syl_locale, dob.prof);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_SYB_LIST_XML"))
		 * static_env.outputXML(out, result);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_SYB_LIST"))
		 * generalAsHtml(result, out, dob); } else if (perl.match("#INS_SYB#i",
		 * dob.prm_ACTION)) { try {
		 * 
		 * //access control AcSyllabus acsyb = new AcSyllabus(con);
		 * if(!acsyb.hasInsPrivilege(dob.prof.usr_ent_id,
		 * dob.prof.current_role)) { throw new
		 * qdbErrMessage(dbSyllabus.NO_RIGHT_MGT); }
		 * 
		 * dob.dbsyb.insSyllabus(con, dob.prof); con.commit();
		 * response.sendRedirect(dob.url_success); } catch(qdbErrMessage e) {
		 * con.rollback(); msgBox(MSG_ERROR, con, e, dob, out); return; } } else
		 * if (perl.match("#DEL_SYB#i", dob.prm_ACTION)) { try {
		 * 
		 * //access control AcSyllabus acsyb = new AcSyllabus(con);
		 * if(!acsyb.hasDelPrivilege(dob.prof.usr_ent_id,
		 * dob.prof.current_role)) { throw new
		 * qdbErrMessage(dbSyllabus.NO_RIGHT_MGT); }
		 * 
		 * dob.dbsyb.delSyllabus(con, dob.prof); con.commit();
		 * response.sendRedirect(dob.url_success); } catch(qdbErrMessage e) {
		 * con.rollback(); msgBox(MSG_ERROR, con, e, dob, out); return; } } else
		 * if (perl.match("#UPD_SYB#i", dob.prm_ACTION)) { try {
		 * 
		 * //access control AcSyllabus acsyb = new AcSyllabus(con);
		 * if(!acsyb.hasDelPrivilege(dob.prof.usr_ent_id,
		 * dob.prof.current_role)) { throw new
		 * qdbErrMessage(dbSyllabus.NO_RIGHT_MGT); }
		 * 
		 * dob.dbsyb.updSyllabus(con, dob.prof); con.commit();
		 * response.sendRedirect(dob.url_success); } catch(qdbErrMessage e) {
		 * con.rollback(); msgBox(MSG_ERROR, con, e, dob, out); return; } } else
		 * if (dob.prm_ACTION.equalsIgnoreCase("GET_SYB") ||
		 * dob.prm_ACTION.equalsIgnoreCase("GET_SYB_XML")) {
		 * 
		 * //no access control
		 * 
		 * String result = dob.dbsyb.getSybAsXML(con,dob.prof);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_SYB_XML"))
		 * static_env.outputXML(out, result);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_SYB")) generalAsHtml(result,
		 * out, dob); }
		 */

		/*
		 * else if (perl.match("#GET_OBJ_LST#i", dob.prm_ACTION)) {
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST") ||
		 * dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST_XML") ) {
		 * 
		 * //no access control
		 * 
		 * long objId = 0; if (dob.robs !=null && dob.robs[0]!=null &&
		 * dob.robs[0].length()>0 ) objId = Long.parseLong(dob.robs[0]);
		 * 
		 * String lang = dob.dbres.res_lan; String result =
		 * dbObjective.procGetOLAsXML(con, objId, dob.dbsyb.syl_id, lang,
		 * dob.types, dob.prof);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST"))
		 * generalAsHtml(result, out, dob); //olAsHtml(xmlOL, out, static_env);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST_XML"))
		 * static_env.outputXML(out, result); }
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST_HDR") ||
		 * dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST_HDR_XML") ) {
		 * 
		 * //no access control
		 * 
		 * String result = dbObjective.procGetOLHdrAsXML(con,
		 * dob.olhPrm.obj_id_syb, dob.olhPrm.obj_id_ass, 0, dob.prof, true);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST_HDR"))
		 * generalAsHtml(result, out, dob);
		 * if(dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST_HDR_XML"))
		 * static_env.outputXML(out, result); } }
		 */
		else if (perl.match("#GET_QUE_FRM#i", dob.prm_ACTION)) {

			// no access control

			long objId = (dob.robs != null && dob.robs[0] != null && dob.robs[0]
					.length() > 0) ? Long.parseLong(dob.robs[0]) : 0;
			String result = dob.dbque.passThruAsXML(con, dob.prof, objId,
					dob.dbmod.res_type);
			if (dob.prm_ACTION.equalsIgnoreCase("get_que_frm_xml"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("get_que_frm")) {
				if (!dob.is_add_res) {
					try {
						dbModule mod = new dbModule();
						mod.res_id = objId;
						mod.mod_res_id = objId;
						mod.checkStat(con);
					} catch (qdbErrMessage e) {
						con.rollback();
						msgBox(MSG_ERROR, con, e, dob, out);
						return;
					}
				}
				generalAsHtml(result, out, dob);
				// passThruAsHtml(xml, dob, out);
			}
		} else if (perl.match("#GET_QUE_LST#i", dob.prm_ACTION)) {

			// no access control
			// String xmlQL = procGetQLAsXML(con, dob, out);
			long objId = Long.parseLong(dob.robs[0]);
			if (wizbini.cfgTcEnabled) {
				AcTrainingCenter acTc = new AcTrainingCenter(con);
				String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id, objId,
						dob.prof.current_role);
				if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
					dob.dbobj.obj_id = objId;
					dob.dbobj.get(con);
					if (dob.dbobj.obj_share_ind) {
						dob.dbobj.share_mode = true;
					}
				}
			}
			String result = dob.dbres.getResListAsXML(con, sess, objId,
					dob.types, dob.subtypes, dob.prof, dob.cwPage,
					dob.cur_stylesheet, dob.dbobj.share_mode);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_QUE_LST"))
				generalAsHtml(result, out, dob);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_QUE_LST_XML"))
				static_env.outputXML(out, result);
		} else if (perl.match("#GET_SUQ_LST#i", dob.prm_ACTION)) {

			// no access control

			// String xmlQL = procGetQLAsXML(con, dob, out);
			long svyId = Long.parseLong(dob.robs[0]);
			String result = "";
			result = dob.dbres.getSuqListAsXML(con, svyId, dob.prof,
					dob.begin_num, dob.end_num);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_SUQ_LST"))
				generalAsHtml(result, out, dob);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_SUQ_LST_XML"))
				static_env.outputXML(out, result);
		} else if (perl.match("#GET_Q#i", dob.prm_ACTION)) {
			String result;
			if (dob.upd_tst_que) {

				if (dob.dbque.isQueAttempted(con)) {
					con.rollback();
					qdbErrMessage e = new qdbErrMessage(
							dbQuestion.SMESG_QUE_ATTEMPTED);
					msgBox(MSG_ERROR, con, e, dob, out);
					return;
				}

			}

			dob.dbque.get(con);
			if (dob.dbque.que_type.equals(dbResource.RES_SUBTYPE_FSC)) {
				FixedScenarioQue fsc = new FixedScenarioQue();
				fsc.res_id = dob.dbque.que_res_id;
				fsc.get(con, dob.dbque);
				result = fsc.queAsXML(con, dob.prof, dob.dbmod.res_type,
						dob.cur_stylesheet);
			} else if (dob.dbque.que_type.equals(dbResource.RES_SUBTYPE_DSC)) {
				DynamicScenarioQue dsc = new DynamicScenarioQue();
				dsc.res_id = dob.dbque.que_res_id;
				dsc.get(con, dob.dbque);
				result = dsc.queAsXML(con, dob.prof, dob.dbmod.res_type,
						dob.cur_stylesheet);
			} else {
				result = dob.dbque.queAsXML(con, dob.prof, dob.dbmod.res_type,
						dob.cur_stylesheet);
			}
			// 获取当前资源文件夹

			// 获取文件的子文件夹

			// 创建路径
			if (dob.prm_ACTION.equalsIgnoreCase("GET_Q_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_Q"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_PROF")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_PROF_XML")) {
			String result = dob.prof.profAsXML(dob.domain);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_PROF_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_PROF"))
				generalAsHtml(result, out, dob);
		}

		// Function for stat.net

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_MOD_LST_XML")) {

			// no access control

			String result = dob.dbcos.getModListAsXML(con, dob.domain,
					dob.dbmod.mod_type, dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_LST_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_LST"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("CLONE_MOD")) {
			if (dob.clo_src_itm_id == 0 || dob.clo_tag_cos_res_id == 0
					|| dob.sel_mod_id_list == null
					|| dob.sel_mod_id_list.length == 0) {
				cwSysMessage e = new cwSysMessage("GEN000");
				msgBox(MSG_ERROR, con, e, dob, out);
			} else {
				StringBuffer title = CourseContentUtils.cloneCourseContent(con,
						dob.clo_src_itm_id, dob.clo_tag_cos_res_id,
						dob.sel_mod_id_list, dob.prof,
						wizbini.getFileUploadResDirAbs());
				con.commit();
				cwSysMessage e;
				if (title.length() > 0) {
					e = new cwSysMessage("COS003", cwUtils.esc4XML(title
							.toString()));
				} else {
					e = new cwSysMessage("COS002");
				}
				msgBox(MSG_STATUS, con, e, dob, out);
			}
		}
		// Function for get message
		else if (dob.prm_ACTION.equalsIgnoreCase("GET_MSG")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_MSG_XML")) {

			// String result = dob.dbmsg.getMsgAsXML(con);
			try {
				if (!StringUtils.isEmpty(dob.dbmsg.encrypt_msg_id)) {
					dob.dbmsg.msg_id = EncryptUtil
							.cwnDecrypt(dob.dbmsg.encrypt_msg_id);
				}
				// access control on on/off filter
				boolean checkStatus = !(AccessControlWZB.hasRolePrivilege(
						dob.prof.current_role, AclFunction.FTN_AMD_MSG_MAIN));

				// check privilege of read.
				if (wizbini.cfgTcEnabled
						&& dob.dbmsg.msg_type
								.equalsIgnoreCase(dob.dbmsg.MSG_TYPE_SYS)) {
					if (dob.dbmsg.msg_readonly) {
						if (!dob.dbmsg.hasPrivilegeReadMsg(con, dob.prof)) {
							throw new qdbErrMessage(
									dbResourcePermission.NO_RIGHT_READ_MSG);
						}
					} else {
						AccessControlWZB acWZB = new AccessControlWZB();
						if (!dob.dbmsg.canEdit(con, dob.prof)
								&& !AccessControlWZB.hasRolePrivilege(
										dob.prof.current_role,
										AclFunction.FTN_AMD_MSG_MAIN)) {
							throw new qdbErrMessage(
									dbResourcePermission.NO_RIGHT_WRITE_MSG);
						}
					}
				}
				dob.dbmsg.msg_res_id = dob.dbres.res_id;
				String result = dob.dbmsg.msgAsXMLNoHeader(con, dob.prof, true,
						checkStatus, dob.dbmsg.msg_status, dob.page,
						dob.page_size, dob.sortCol, dob.sortOrder, dob.is_upd,
						"");
				if (dob.dbres.res_id > 0) {
					long itm_id = dbCourse.getCosItemId(con, dob.dbres.res_id);
					result += aeItem.genItemActionNavXML(con, itm_id, dob.prof);
				}
				result += "<msg_belong_exam_ind>"
						+ dob.dbmsg.msg_belong_exam_ind
						+ "</msg_belong_exam_ind>";
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.getUsr().usr_ent_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				acPageVariant.msg_type = dob.dbmsg.msg_type;
				String metaXML = acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet));
				metaXML += "<key>" + dob.key + "</key>";
				metaXML += String.format("<isMobile>%s</isMobile>",
						dob.isMobile);
				String XML = formatXML(result, metaXML, "announcement",
						dob.prof);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_MSG_XML"))
					static_env.outputXML(out, XML);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_MSG"))
					generalAsHtml(XML, out, dob);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_TPL")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_TPL_XML")) {
			String result = dob.dbtpl.tplListAsXML(con, dob.prof,
					dob.dbcos.cos_res_id, dob.dbdpo.dpo_view,
					dob.dbtpl.tpl_type, dob.tpl_subtype, null,
					wizbini.cfgTcEnabled, dob.itm_type,dob.tcr_id);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_TPL_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_TPL"))
				generalAsHtml(result, out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_ALL_MSG")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_ALL_MSG_XML")) {

			String title_code = "";
			if (request.getParameter("title_code") != null) {
				title_code = request.getParameter("title_code").toString()
						.trim();
			}
			;

			// String result = dob.dbmsg.getMsgAsXML(con);

			// access control on on/off filter
			boolean checkStatus;
			checkStatus = !(AccessControlWZB.hasRolePrivilege(
					dob.prof.current_role, AclFunction.FTN_AMD_MSG_MAIN));

			dob.dbmsg.msg_res_id = dob.dbres.res_id;

			String result = null;
			String nav_tree = null;
			cwTree tree = new cwTree();
			if (wizbini.cfgTcEnabled
					&& dob.dbmsg.msg_type
							.equalsIgnoreCase(dbMessage.MSG_TYPE_SYS)) {
				AccessControlWZB acWZB = new AccessControlWZB();
				if (!dob.dbmsg.msg_readonly
						&& AccessControlWZB.hasRolePrivilege(
								dob.prof.current_role,
								AclFunction.FTN_AMD_MSG_MAIN)
						&& AccessControlWZB.isRoleTcInd(dob.prof.current_role)) {
					// if admin has no effect training center. throw a system
					// message
					if (AccessControlWZB.isRoleTcInd(dob.prof.current_role)
							&& !ViewTrainingCenter.hasEffTc(con,
									dob.prof.usr_ent_id)) {
						qdbErrMessage e = new qdbErrMessage("TC009");
						msgBox(MSG_ERROR, con, e, dob, out);
						return;
					}
					// if admin visit message page.nav tree show the same as
					// learner.
					if (dob.dbmsg.msg_readonly) {
						nav_tree = tree.genNavTrainingCenterTree(con, dob.prof,
								true);
					}
				}
				if (nav_tree == null) {
					nav_tree = tree.genNavTrainingCenterTree(con, dob.prof,
							false);
				}
				result = dob.dbmsg.getAnnAsXml(con, dob.prof, dob.page,
						dob.page_size, dob.sortCol, dob.sortOrder, null,
						dob.isMobile, wizbini, title_code);
			} else {
				result = dob.dbmsg.msgAsXMLNoHeader(con, dob.prof, false,
						checkStatus, dob.dbmsg.msg_status, dob.page,
						dob.page_size, dob.sortCol, dob.sortOrder, false,
						title_code);
			}
			result += "<search_title>" + cwUtils.esc4XML(title_code)
					+ "</search_title>";
			result += "<read_only>" + dob.dbmsg.msg_readonly + "</read_only>";
			result += String.format("<isMobile>%s</isMobile>",
					String.valueOf(dob.isMobile));
			if (dob.dbres.res_id > 0) {
				long itm_id = dbCourse.getCosItemId(con, dob.dbres.res_id);
				result += aeItem.genItemActionNavXML(con, itm_id, dob.prof);
				// 路径缺失
				aeItem itm = new aeItem();
				itm.itm_id = itm_id;
				itm.getItem(con);
				result += itm.contentInfoAsXML(con);
			}
			AcPageVariant acPageVariant = new AcPageVariant(con);
			acPageVariant.instance_id = dob.getUsr().usr_ent_id;
			acPageVariant.ent_id = dob.prof.usr_ent_id;
			if (!AccessControlWZB.isRoleTcInd(dob.prof.current_role)) {
				acPageVariant.rol_ext_id = AcObjective.ADM;
			} else {
				acPageVariant.rol_ext_id = dob.prof.current_role;
			}

			acPageVariant.msg_type = dob.dbmsg.msg_type;
			String metaXML = acPageVariant
					.answerPageVariantAsXML((String[]) xslQuestions
							.get(dob.cur_stylesheet));
			if (nav_tree != null) {
				result += nav_tree;
			}
			String XML = formatXML(result, metaXML, "announcement", dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_ALL_MSG_XML"))
				static_env.outputXML(out, XML);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_ALL_MSG"))
				generalAsHtml(XML, out, dob);
		}

		// << BEGIN add new command
		else if (dob.prm_ACTION.equalsIgnoreCase("PREP_INS_MSG")
				|| dob.prm_ACTION.equalsIgnoreCase("PREP_INS_MSG_XML")) {
			Timestamp curTime = dbUtils.getTime(con);
			String result = dob.dbmsg.getMsgLevelLst(con);
			if (wizbini.cfgTcEnabled) {
				//long tcr_id = ViewTrainingCenter.getDefaultTcId(con, dob.prof);
				long tcr_id = dob.tcr_id;
				DbTrainingCenter objTc = DbTrainingCenter.getInstance(con,
						tcr_id);
				if (objTc != null) {
					StringBuffer xmlBuf = new StringBuffer();
					xmlBuf.append("<default_training_center id =\"")
							.append(objTc.getTcr_id()).append("\">");
					xmlBuf.append("<title>")
							.append(cwUtils.esc4XML(objTc.getTcr_title()))
							.append("</title>");
					xmlBuf.append("</default_training_center>");
					result += xmlBuf.toString();
				}
			}
			result += "<cur_time>" + curTime + "</cur_time>";
			result += String.format("<isMobile>%s</isMobile>",
					String.valueOf(dob.isMobile));
			result += "<msg_belong_exam_ind>" + dob.dbmsg.msg_belong_exam_ind
					+ "</msg_belong_exam_ind>";
			if (dob.dbres.res_id > 0) {
				long itm_id = dbCourse.getCosItemId(con, dob.dbres.res_id);
				result += aeItem.genItemActionNavXML(con, itm_id, dob.prof);
			}
			AcPageVariant acPageVariant = new AcPageVariant(con);
			acPageVariant.instance_id = dob.getUsr().usr_ent_id;
			acPageVariant.ent_id = dob.prof.usr_ent_id;
			acPageVariant.rol_ext_id = dob.prof.current_role;
			acPageVariant.msg_type = dob.dbmsg.msg_type;
			String metaXML = acPageVariant
					.answerPageVariantAsXML((String[]) xslQuestions
							.get(dob.cur_stylesheet));
			String XML = formatXML(result, metaXML, "announcement", dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("PREP_INS_MSG_XML"))
				static_env.outputXML(out, XML);
			if (dob.prm_ACTION.equalsIgnoreCase("PREP_INS_MSG"))
				generalAsHtml(XML, out, dob);
		}
		// >> END
		else if (dob.prm_ACTION.equalsIgnoreCase("INS_MSG")) {
			try {
				// 过滤字符
				if (null != dob.dbmsg.msg_body) {
					dob.dbmsg.msg_body = cwUtils.esc4Json(dob.dbmsg.msg_body);
				}
				if (null != dob.dbmsg.msg_title) {
					dob.dbmsg.msg_title = cwUtils.esc4Json(dob.dbmsg.msg_title);
				}

				dob.dbmsg.msg_res_id = dob.dbres.res_id;
				String msgIconName = (String) dob.h_file_filename
						.get("msg_icon_file");
				dob.dbmsg.msg_icon = msgIconName;

				// access control
				// AcMessage acmsg = new AcMessage(con);
				// if(!acmsg.hasMgtPrivilege(dob.prof, dob.dbmsg.msg_type,
				// dob.dbmsg.msg_res_id)) {
				// throw new qdbErrMessage(dbMessage.NO_RIGHT_INSERT_MSG);
				// }
				dob.dbmsg.msg_mobile_ind = dob.isMobile;
				dob.dbmsg.msg_receipt = dob.isReceipt;
				dob.dbmsg.insMsg(con, dob.prof);

				if (dob.bFileUpload && msgIconName != null) {
					dob.dbmsg
							.uploadedFile(dob.tmpUploadDir,
									static_env.INI_MSG_DIR_UPLOAD, wizbini,
									msgIconName);
				}

				con.commit();
				ObjectActionLog log = new ObjectActionLog(dob.dbmsg.msg_id,
						null, dob.dbmsg.msg_title,
						ObjectActionLog.OBJECT_TYPE_AN,
						ObjectActionLog.OBJECT_ACTION_ADD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);
				response.sendRedirect(dob.url_success);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("auto_login")) {
			response.sendRedirect(dob.url_success);
		}/*
		 * else if ( dob.prm_ACTION.equalsIgnoreCase("LINK_NOTIFY_XML") ) { try{
		 * StringBuffer xml = new StringBuffer(); if( dob.ent_ids != null &&
		 * dob.ent_ids.length() > 0 ) xml.append(dob.dbXmsg.getRecipientXml(con,
		 * Long.parseLong(dob.ent_ids)));
		 * xml.append(dob.dbXmsg.getSenderXml(con));
		 * 
		 * if( dob.dbXmsg.id_type.equalsIgnoreCase("ITEM") )
		 * xml.append(dob.dbXmsg.getItemXml(con)); else if(
		 * dob.dbXmsg.id_type.equalsIgnoreCase("RESOURCE") )
		 * xml.append(dob.dbXmsg.getReourceXml(con));
		 * 
		 * out.println((xml.toString()).trim()); }catch( Exception e ) {
		 * System.out.println("Failed to get message : " + e );
		 * out.println("FAILED"); } return;
		 * 
		 * } else if( dob.prm_ACTION.equalsIgnoreCase("INS_XMSG") ) {
		 * 
		 * //no access control
		 * 
		 * String returnMessage; long[] ent_ids =
		 * dbUserGroup.constructEntId(con, dob.ent_ids); long[] cc_ent_ids =
		 * dbUserGroup.constructEntId(con, dob.cc_ent_ids); dob.url_redirect =
		 * cwUtils.getRealPath(request, dob.url_redirect) + "&ent_ids=" +
		 * dbUtils.longArray2String(ent_ids,"~") + "&cc_ent_ids=" +
		 * dbUtils.longArray2String(cc_ent_ids,"~"); //+ "&usr_id=" +
		 * dob.prof.usr_id;
		 * 
		 * try{ //dbUtils.urlRedirect( "http://" +
		 * request.getServerName()+":"+request
		 * .getServerPort()+"/servlet/Dispatcher", dob.url_success, request );
		 * returnMessage = dbUtils.urlRedirect( dob.url_redirect, request );
		 * }catch(Exception e) { throw new
		 * cwException("Failed to get the message detials : " + e); }
		 * 
		 * if( returnMessage.equalsIgnoreCase("INSERT SUCCESS") ) {
		 * msgBox(MSG_STATUS, con, new cwSysMessage("XMG001"), dob, out);
		 * //response.sendRedirect(dob.url_success); }else {
		 * System.out.println("return message = " + returnMessage);
		 * msgBox(MSG_ERROR, con, new cwSysMessage("XMG002"), dob, out);
		 * //response.sendRedirect(dob.url_fail); }
		 * 
		 * } else if( dob.prm_ACTION.equalsIgnoreCase("init_msg") ||
		 * dob.prm_ACTION.equalsIgnoreCase("init_msg_xml") ) {
		 * 
		 * String[] default_recipient = {"admin"}; String xml =
		 * dob.dbXmsg.initMsg(con, dob.dbXmsg.msg_type, dob.dbXmsg.id,
		 * dob.dbXmsg.id_type, default_recipient, dob.prof);
		 * 
		 * 
		 * if( dob.prm_ACTION.equalsIgnoreCase("init_msg_xml") ) {
		 * static_env.outputXML(out, xml); } else { generalAsHtml(xml, out,
		 * dob); }
		 * 
		 * }
		 */

		else if (dob.prm_ACTION.equalsIgnoreCase("UPD_MSG")) {
			try {
				dob.dbmsg.msg_res_id = dob.dbres.res_id;
				String msgIconName = (String) dob.h_file_filename
						.get("msg_icon_file");
				dob.dbmsg.msg_icon = msgIconName;
				dob.dbmsg.msg_mobile_ind = dob.isMobile;
				dob.dbmsg.msg_receipt = dob.isReceipt;
				// access control
				// AcMessage acmsg = new AcMessage(con);
				// if(!acmsg.hasMgtPrivilege(dob.prof, dob.dbmsg.msg_type,
				// dob.dbmsg.msg_res_id)) {
				// throw new qdbErrMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
				// }
				if (AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
						AclFunction.FTN_AMD_MSG_MAIN)) {
					if (AccessControlWZB.isRoleTcInd(dob.prof.current_role)
							&& wizbini.cfgTcEnabled
							&& !dob.dbmsg.canEdit(con, dob.prof)) {
						throw new qdbErrMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
					}
				} else {
					throw new qdbErrMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
				}

				// 过滤字符
				if (null != dob.dbmsg.msg_body) {
					dob.dbmsg.msg_body = cwUtils.esc4Json(dob.dbmsg.msg_body);
				}
				if (null != dob.dbmsg.msg_title) {
					dob.dbmsg.msg_title = cwUtils.esc4Json(dob.dbmsg.msg_title);
				}

				if (dbMessage.MSG_ICON_UPD_DEL
						.equalsIgnoreCase(dob.dbmsg.msg_icon_result)) {
					dob.dbmsg.delFile(con, static_env.INI_MSG_DIR_UPLOAD);
					dob.dbmsg.msg_icon = "";
				} else if (dbMessage.MSG_ICON_UPD_CHANGE
						.equalsIgnoreCase(dob.dbmsg.msg_icon_result)) {
					dob.dbmsg.delFile(con, static_env.INI_MSG_DIR_UPLOAD);
					dob.dbmsg.msg_icon = msgIconName;
				}

				dob.dbmsg.updMsg(con, dob.prof);

				if (dob.bFileUpload && msgIconName != null) {
					dob.dbmsg
							.uploadedFile(dob.tmpUploadDir,
									static_env.INI_MSG_DIR_UPLOAD, wizbini,
									msgIconName);
				}

				con.commit();
				ObjectActionLog log = new ObjectActionLog(dob.dbmsg.msg_id,
						null, dob.dbmsg.msg_title,
						ObjectActionLog.OBJECT_TYPE_AN,
						ObjectActionLog.OBJECT_ACTION_UPD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);

				// msgBox(MSG_STATUS, con, new cwSysMessage("MSG006"), dob,
				// out);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("DEL_MSG")) {
			try {
				dob.dbmsg.msg_res_id = dob.dbres.res_id;

				// access control
				if (!AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
						AclFunction.FTN_AMD_MSG_MAIN)) {
					throw new qdbErrMessage(dbMessage.NO_RIGHT_DELETE_MSG);
				}

				AccessControlWZB acWZB = new AccessControlWZB();
				if (AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
						AclFunction.FTN_AMD_MSG_MAIN)) {
					if (AccessControlWZB.isRoleTcInd(dob.prof.current_role)
							&& wizbini.cfgTcEnabled
							&& !dob.dbmsg.canEdit(con, dob.prof)) {
						throw new qdbErrMessage(dbMessage.NO_RIGHT_DELETE_MSG);
					}
				}
				dob.dbmsg.delMsg(con, dob.prof);

				con.commit();

				// msgBox(MSG_STATUS, con, new cwSysMessage("MSG007"), dob,
				// out);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("DEL_MSG_LST")) {
			try {
				dob.dbmsg.msg_res_id = dob.dbres.res_id;

				// access control
				// AcMessage acmsg = new AcMessage(con);
				// if(!acmsg.hasMgtPrivilege(dob.prof, dob.dbmsg.msg_type,
				// dob.dbmsg.msg_res_id)) {
				// throw new qdbErrMessage(dbMessage.NO_RIGHT_DELETE_MSG);
				// }
				List<ObjectActionLog> msgLogList = new ArrayList<ObjectActionLog>();
				if (null != dob.msgLst && dob.msgLst.length > 0) {
					for (int i = 0; i < dob.msgLst.length; i++) {
						Long msg_id = Long.parseLong(dob.msgLst[i]);
						String title = dbMessage
								.getMsgTitleByMsgId(con, msg_id);
						ObjectActionLog log = new ObjectActionLog(msg_id, null,
								title, ObjectActionLog.OBJECT_TYPE_AN,
								ObjectActionLog.OBJECT_ACTION_DEL,
								ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
								dob.prof.getUsr_ent_id(),
								dob.prof.getUsr_last_login_date(),
								dob.prof.getIp());
						msgLogList.add(log);
					}
				}

				dob.dbmsg.delMsgList(con, wizbini, dob.prof, dob.msgLst);
				con.commit();
				for (ObjectActionLog log : msgLogList) {
					SystemLogContext.saveLog(log,
							SystemLogTypeEnum.OBJECT_ACTION_LOG);
				}
				// msgBox(MSG_STATUS, con, new cwSysMessage("MSG007"), dob,
				// out);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			} catch (cwSysMessage e) {
				msgBox(MSG_STATUS, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_TST_RES")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_LRN_TST_RES_XML")) {

			try {
				// access control//to be enhanced
				AcModule acMod = new AcModule(con);

				if (dob.dbmod.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.dbmod.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(
							con, dob.dbmod.mod_res_id, dob.getUsr().usr_ent_id);
				}
				dob.dbmod.get(con);
				dob.dbmod.usr_ent_id = dob.getUsr().usr_ent_id;// svy owner
																// entity id
				Vector queIdVec = new Vector();
				String result = dob.dbmod.genTestAsXML(con, dob.prof,
						dob.dbmsp, dob.robs, 0,
						wizbini.getFileUploadResDirAbs(), queIdVec, true,
						dob.dbmod.tkh_id, false, false);
				StringBuffer resultBuf = new StringBuffer(result);
				String metaXML = "<meta>" + dob.prof.asXML() + "</meta>";
				resultBuf.insert(result.indexOf("<key>"), metaXML);
				result = resultBuf.toString();

				if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_TST_RES_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_LRN_TST_RES"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SUBMIT_ASS")
				|| dob.prm_ACTION.equalsIgnoreCase("SUBMIT_ASS_XML")) {
			try {

				// access control
				AcModule acmod = new AcModule(con);

				if (DbTrackingHistory.getAppTrackingIDByCos(con, dob.tkh_id,
						dob.prof.usr_ent_id,
						dbModule.getCosId(con, dob.dbass.ass_res_id),
						dob.dbass.ass_res_id) != 1) {
					dob.setUrl_fail("javascript:window.close()");
					throw new qdbErrMessage("USR033");
				}

				String result = "";

				if (dob.dbass.ass_max_upload == -1
						&& dob.dbass.no_of_upload == 0) {
					dob.dbass.tmpUploadDir = dob.session_upload_dir;
				} else {
					dob.dbass.tmpUploadDir = dob.tmpUploadDir;
				}

				if (dob.tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.tkh_id = DbTrackingHistory.getAppTrackingIDByMod(con,
							dob.dbass.ass_res_id, dob.prof.usr_ent_id);
				}

				dob.dbass.submitAssignment(con, dob.prof, dob.file_lst,
						dob.step, dob.tkh_id);
				dob.dbass.get(con);

				// add by kim

				if (dob.step == 6) {
					dbModuleEvaluation dbmov = new dbModuleEvaluation();
					dbmov.mov_cos_id = dbModule.getCosId(con,
							dob.dbass.ass_res_id);
					dbmov.mov_ent_id = dob.prof.usr_ent_id;
					dbmov.mov_mod_id = dob.dbass.ass_res_id;
					dbmov.mov_status = dbAiccPath.STATUS_INCOMPLETE;
					dbmov.mov_tkh_id = dob.tkh_id;
					dbmov.save(con, dob.prof);
				}

				if (dob.step == 6) {
					con.commit();
					// notify student
					if (dob.dbass.ass_notify_ind.equals(dob.dbass.NOTIFY_ON)) {
						String cos_title = dbCourse.getCosTitle(con,
								dbModule.getCosId(con, dob.dbass.ass_res_id));
						// String[] contents = {dob.dbass.res_title, cos_title};
						String template;

						if (dob.prof.label_lan.equalsIgnoreCase("ISO-8859-1")) {
							template = static_env.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL;
						} else if (dob.prof.label_lan.equalsIgnoreCase("Big5")) {
							template = static_env.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_CH;
						} else {
							template = static_env.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_GB;
						}
						qdbMailSender sender = new qdbMailSender(
								Application.MAIL_SERVER_HOST,
								wizbini.cfgSysSetupadv.getEncoding(),
								qdbMailman.HTML);
						acSite ac_site = new acSite();
						ac_site.ste_ent_id = dob.prof.root_ent_id;
						dbRegUser dbusr = new dbRegUser();

						if (Application.MAIL_SERVER_ACCOUNT_TYPE
								.equalsIgnoreCase("NOTES")) {
							dbusr.usr_ent_id = ac_site.getSiteSysEntId(con);
							dbusr.get(con);
							String[] sender_ = { dbusr.usr_email_2,
									dbusr.usr_display_bil };
							String[] bcc_ = { dbusr.usr_email_2,
									dbusr.usr_display_bil };

							dbusr.usr_ent_id = dob.prof.usr_ent_id;
							dbusr.get(con);
							String[] receiver_ = { dbusr.usr_email_2,
									dbusr.usr_display_bil };
							String[] contents = { dob.dbass.res_title,
									cos_title, sender_[1] };
							try {
								sender.sendWithTemplate(sender_, receiver_,
										null, bcc_, null, template, contents,
										Application.MAIL_SERVER_USER,
										Application.MAIL_SERVER_PASSWORD,
										Application.MAIL_SERVER_AUTH_ENABLED);
							} catch (Exception e) {
								con.rollback();
								msgBox(MSG_ERROR, con, new cwSysMessage(
										"ASN001"), dob, out);
								return;
							}
						} else {
							dbusr.usr_ent_id = ac_site.getSiteSysEntId(con);
							dbusr.get(con);
							String[] sender_ = { dbusr.usr_email,
									dbusr.usr_display_bil };
							String[] bcc_ = { dbusr.usr_email,
									dbusr.usr_display_bil };

							dbusr.usr_ent_id = dob.prof.usr_ent_id;
							dbusr.get(con);
							String[] receiver_ = { dbusr.usr_email,
									dbusr.usr_display_bil };
							String[] contents = { dob.dbass.res_title,
									cos_title, sender_[1] };
							try {
								sender.sendWithTemplate(sender_, receiver_,
										null, bcc_, null, template, contents,
										Application.MAIL_SERVER_USER,
										Application.MAIL_SERVER_PASSWORD,
										Application.MAIL_SERVER_AUTH_ENABLED);
							} catch (Exception e) {
								con.rollback();
								msgBox(MSG_ERROR, con, new cwSysMessage(
										"ASN001"), dob, out);
								return;
							}
						}

					}
				}

				if (dob.step == 2) {
					dbProgressAttachment attach = new dbProgressAttachment(
							dob.prof.usr_id, dob.dbass.ass_res_id, 1,
							dob.tkh_id);
					Vector attachLst = null;

					attachLst = attach.get(con);
					result = dob.dbass.asXML(con, dob.prof, attachLst,
							dob.dbdpo.dpo_view, dob.tkh_id);
				} else {
					result = dob.dbass.asXML(con, dob.prof, dob.file_lst,
							dob.file_order, dob.dbdpo.dpo_view, dob.tkh_id);
				}

				if (dob.prm_ACTION.equalsIgnoreCase("SUBMIT_ASS_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("SUBMIT_ASS"))
					generalAsHtml(result, out, dob);

				// generalAsHtml(result, out, dob);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GRADE_ASS")) {
			try {

				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod
						.checkModifyPermission(dob.prof, dob.dbass.ass_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbass.tmpUploadDir = dob.tmpUploadDir;
				dob.dbass.grade(con, dob.prof, dob.dbpgr, dob.ass_filename,
						dob.ass_comment, dob.tkh_id);
				con.commit();

				// notify student
				dob.dbass.get(con);

				if (dob.email != null && dob.email.length() != 0) {
					/*
					 * String[] contents = {dob.dbpgr.pgr_usr_id,
					 * dob.dbass.res_title, dob.prof.usr_id}; String template;
					 * 
					 * if (dob.prof.label_lan.equalsIgnoreCase("ISO-8859-1")) {
					 * template = static_env.INI_NOTIFY_STUDENT_GRADE_TPL; }
					 * else if (dob.prof.label_lan.equalsIgnoreCase("Big5")) {
					 * template = static_env.INI_NOTIFY_STUDENT_GRADE_TPL_CH; }
					 * else { template =
					 * static_env.INI_NOTIFY_STUDENT_GRADE_TPL_GB; }
					 * 
					 * qdbMailSender sender = new
					 * qdbMailSender(static_env.INI_MAIL_SERVER);
					 * 
					 * sender.sendWithTemplate(dob.email,
					 * "do_not_reply@wizbank.com", template, contents);
					 */

					String cos_title = dbCourse.getCosTitle(con,
							dbModule.getCosId(con, dob.dbass.ass_res_id));
					String[] contents = { dob.dbass.res_title, cos_title };
					String template;

					if (dob.prof.label_lan.equalsIgnoreCase("ISO-8859-1")) {
						template = static_env.INI_NOTIFY_STUDENT_GRADE_TPL;
					} else if (dob.prof.label_lan.equalsIgnoreCase("Big5")) {
						template = static_env.INI_NOTIFY_STUDENT_GRADE_TPL_CH;
					} else {
						template = static_env.INI_NOTIFY_STUDENT_GRADE_TPL_GB;
					}
					qdbMailSender sender = new qdbMailSender(
							Application.MAIL_SERVER_HOST,
							wizbini.cfgSysSetupadv.getEncoding(),
							qdbMailman.HTML);
					acSite ac_site = new acSite();
					ac_site.ste_ent_id = dob.prof.root_ent_id;
					dbRegUser dbusr = new dbRegUser();

					if (Application.MAIL_SERVER_ACCOUNT_TYPE
							.equalsIgnoreCase("NOTES")) {
						String[] sender_ = null;
						dbusr.usr_ent_id = dob.prof.usr_ent_id;
						dbusr.get(con);
						if (dbusr.usr_email_2 != null
								&& dbusr.usr_email_2.length() > 0) {
							sender_ = new String[] { dbusr.usr_email_2,
									dbusr.usr_display_bil };
						}

						dbusr.usr_ent_id = ac_site.getSiteSysEntId(con);
						dbusr.get(con);
						String[] bcc_ = { dbusr.usr_email_2,
								dbusr.usr_display_bil };
						if (sender_ == null) {
							sender_ = new String[] { dbusr.usr_email_2,
									dbusr.usr_display_bil };
						}

						dbusr.usr_ent_id = dbRegUser.getEntId(con,
								dob.dbpgr.pgr_usr_id);
						dbusr.get(con);
						String[] receiver_ = { dbusr.usr_email_2,
								dbusr.usr_display_bil };

						sender.sendWithTemplate(sender_, receiver_, null, bcc_,
								null, template, contents,
								Application.MAIL_SERVER_USER,
								Application.MAIL_SERVER_PASSWORD,
								Application.MAIL_SERVER_AUTH_ENABLED);
					} else {
						String[] sender_ = null;
						dbusr.usr_ent_id = dob.prof.usr_ent_id;
						dbusr.get(con);
						if (dbusr.usr_email != null
								&& dbusr.usr_email.length() > 0) {
							sender_ = new String[] { dbusr.usr_email,
									dbusr.usr_display_bil };
						}

						dbusr.usr_ent_id = ac_site.getSiteSysEntId(con);
						dbusr.get(con);
						String[] bcc_ = { dbusr.usr_email,
								dbusr.usr_display_bil };
						if (sender_ == null) {
							sender_ = new String[] { dbusr.usr_email,
									dbusr.usr_display_bil };
						}

						dbusr.usr_ent_id = dbRegUser.getEntId(con,
								dob.dbpgr.pgr_usr_id);
						dbusr.get(con);
						String[] receiver_ = { dbusr.usr_email,
								dbusr.usr_display_bil };

						sender.sendWithTemplate(sender_, receiver_, null, bcc_,
								null, template, contents,
								Application.MAIL_SERVER_USER,
								Application.MAIL_SERVER_PASSWORD,
								Application.MAIL_SERVER_AUTH_ENABLED);
					}

				}

				msgBox(MSG_STATUS, con, new cwSysMessage("PGR006"), dob, out);
				// msgBox(MSG_STATUS, con, new qdbErrMessage("PGR006"), dob,
				// out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DOWNLOAD_ASS")) {
			// check User Right
			/*
			 * if (dbResourcePermission.hasPermission(con, dob.dbass.ass_res_id,
			 * dob.prof, dbResourcePermission.RIGHT_WRITE)) {
			 */
			// access control
			AcModule acmod = new AcModule(con);
			if (!acmod.checkModifyPermission(dob.prof, dob.dbass.ass_res_id)) {
				throw new cwSysMessage(dbResourcePermission.NO_RIGHT_WRITE_MSG);
			}

			try {
				String assignPath = wizbini.getFileUploadResDirAbs()
						+ dbUtils.SLASH + dob.dbass.ass_res_id + dbUtils.SLASH
						+ dbAssignment.SUBMIT_PATH;
				long current_time = System.currentTimeMillis();
				String zipFile = wizbini.getFileUploadTmpDirAbs()
						+ dbUtils.SLASH + dob.dbass.ass_res_id + dbUtils.SLASH
						+ current_time + dbUtils.SLASH + "assignments.zip";
				CommonLog.info(zipFile);

				if (dob.download_lst != null) {
					// download_lst is in the format "tkh_id1~tkh_id2~...."
					if (dob.use_tkh_ind) {
						dob.download_lst = dbAssignment
								.getFoloderLstByTrackingIDs(con,
										dob.download_lst);
					} else {
						// Backward compatiblity, download_lst is in the format
						// "entity1~entity2..."
						for (int i = 0; i < dob.download_lst.length; i++) {
							dob.download_lst[i] = dbRegUser.usrEntId2UsrId(con,
									Long.parseLong(dob.download_lst[i]));
						}
					}
				}

				dbUtils.makeZip(zipFile, assignPath, dob.download_lst, true);
				dob.url_success = "../"
						+ wizbini.cfgSysSetupadv.getFileUpload().getTmpDir()
								.getName() + "/" + dob.dbass.ass_res_id + "/"
						+ current_time + "/assignments.zip";
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
			} catch (ZipException e) {
				msgBox(MSG_ERROR, con, new cwSysMessage("RES004"), dob, out);
			} catch (FileNotFoundException e) {
				msgBox(MSG_ERROR, con, new qdbErrMessage(e.getMessage()), dob,
						out);
			}
			/* } */
		} else if (dob.prm_ACTION.equalsIgnoreCase("RESET_ASS")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod
						.checkModifyPermission(dob.prof, dob.dbass.ass_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbass.reset(con, dob.prof, dob.dbpgr, dob.tkh_id);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_FILE_ASS")) {
			try {

				// no access control

				dob.dbass.delFile(con, dob.prof, dob.ass_filename);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_PUBLIC_FORUM")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_PUBLIC_FORUM_XML")) {
			try {
				if (dob.isMaintain) {
					AcModule acModule = new AcModule(con);
					if (!AccessControlWZB
							.hasRolePrivilege(dob.prof.current_role,
									AclFunction.FTN_AMD_RES_MAIN)) {
						throw new qdbErrMessage(
								dbResourcePermission.NO_RIGHT_WRITE_MSG);
					}
				}
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				String metaXML = acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet));
				metaXML += "<isMaintain>" + dob.isMaintain + "</isMaintain>";
				String result = formatXML(dbForum.getPublicForumAsXML(con,
						dob.prof, dob.isMaintain, dob.isStudyGroupMod,
						dob.sgp_id), metaXML, "forumModule", dob.prof);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_PUBLIC_FORUM_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_PUBLIC_FORUM"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_PUBLIC_SURVEY_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_PUBLIC_SURVEY_LST_XML")) {
			// try{
			Vector modTcrIds = new Vector();
			if (dob.dbmod.mod_tcr_id <= 0) {
				long tcr_id = dbCourse
						.getCosItmTcrId(con, dob.dbcos.cos_res_id);
				modTcrIds = DbTrainingCenter.getChildTc(con, tcr_id);
				modTcrIds.add(tcr_id);
			} else {
				modTcrIds.add(dob.dbmod.mod_tcr_id);
			}
			Vector vtMod = dbModule.getPublicModLst(con, dob.prof.root_ent_id,
					dbModule.MOD_TYPE_SVY, true, dob.isStudyGroupMod,
					dob.sgp_id, modTcrIds);
			String result = formatXML(
					dbModule.getModLstAsXML(con, vtMod, null, 0, 0), null,
					"surveyModule", dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_PUBLIC_SURVEY_LST_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_PUBLIC_SURVEY_LST"))
				generalAsHtml(result, out, dob);
			// } catch (qdbErrMessage e) {
			// con.rollback();
			// msgBox(MSG_ERROR, con, e, dob, out);
			// }
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("PASTE_PUBLIC_SURVEY")) {
			try {
				Vector vtClsId = dob.dbcos.getChildCosResId(con);
				Vector vtModId = dob.dbcos.pastePublicSurvey(con,
						dob.dbmod.mod_res_id, wizbini.getFileUploadResDirAbs(),
						dob.prof, vtClsId);
				dbModule mod = new dbModule();
				mod.mod_res_id = ((Long) vtModId.get(0)).longValue();
				mod.res_id = ((Long) vtModId.get(0)).longValue();
				mod.get(con);
				String modTitle = mod.res_title;
				// insert successful
				dob.dbcos.cos_structure_xml = dbUtils.subsitute(
						dob.dbcos.cos_structure_xml, "$mod_id_",
						Long.toString(mod.mod_res_id));
				dob.dbcos.cos_structure_xml = dbUtils.subsitute(
						dob.dbcos.cos_structure_xml, "$mod_title_",
						dbUtils.esc4XML(modTitle));
				dob.dbcos.cos_structure_xml = dbUtils
						.subsitute(dob.dbcos.cos_structure_xml, "$mod_type_",
								mod.mod_type);
				dob.dbcos.cos_structure_json = static_env.transformXML(
						dob.dbcos.cos_structure_xml.replaceAll("&quot;", " "),
						"cos_structure_json_js.xsl", null);
				dob.dbcos.updCosStructure(con);

				for (int i = 0; i < vtClsId.size(); i++) {
					dbCourse cosObj = (dbCourse) vtClsId.get(i);
					cosObj.res_upd_user = dob.prof.usr_id;
					cosObj.updCosStructureFromParent(con,
							dob.dbcos.cos_structure_xml);
				}
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (cwSysMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("INS_FOR_TOPIC")) {
			try {

				dob.dbforTopic.fto_res_id = dob.dbmod.res_id;
				dob.dbforTopic.ins(con, dob.prof, dob.dbforMsg);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_FOR_TOPIC")) {
			try {

				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkModifyPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				if (dob.topicLst != null) {
					for (int index = 0; index < dob.topicLst.length; index++) {
						dob.dbforTopic = new dbForumTopic();
						dob.dbforTopic.fto_id = Long
								.parseLong(dob.topicLst[index]);
						dob.dbforTopic.fto_res_id = dob.dbmod.res_id;
						dob.dbforTopic.del(con, dob.prof);
					}

					con.commit();
				}
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_FOR_MSG")) {
			try {

				dob.dbforMsg.fmg_fto_id = dob.dbforTopic.fto_id;
				dob.dbforMsg.fmg_fto_res_id = dob.dbmod.res_id;
				dob.dbforMsg.directory_info_map = dbMessage
						.getDirectoryInfoMap(wizbini);
				dob.dbforMsg.ins(con, dob.prof);
				if (dob.prof.isLrnRole) {
					Credit credit = new Credit();
					boolean isCosFor = credit.isCosForum(con, dob.dbmod.res_id);
					if (!isCosFor) { // 不是课程里面的论坛
						if (dob.dbforMsg.fmg_fmg_id_parent == 0) {
							// 论坛发贴得分
							credit.updUserCredits(con, Credit.SYS_INS_TOPIC, 0,
									(int) dob.prof.usr_ent_id, dob.prof.usr_id,
									0, 0, 0, 0);
						} else {
							// 论坛回贴得分
							credit.updUserCredits(con, Credit.SYS_INS_MSG, 0,
									(int) dob.prof.usr_ent_id, dob.prof.usr_id,
									0, 0, 0, 0);
						}
						// 论坛共享资料得分
						credit.uploadResCredits(con, dob.prof.usr_ent_id,
								dob.prof.usr_id, dob.dbforMsg.fmg_msg);
					}
				}
				con.commit();

				// msgBox(MSG_STATUS, con, new qdbErrMessage("MSG005"), dob,
				// out);
				response.sendRedirect(dob.url_success);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_FOR_LST")) {
			try {
				dob.dbforum = new dbForum();
				dob.dbforum.res_upd_user = dob.prof.usr_id;
				dob.dbforum.mod_is_public = true;
				long mod_res_id;
				for (int i = 0; i < dob.res_id_lst.length; i++) {
					mod_res_id = Long.parseLong(dob.res_id_lst[i]);
					dob.dbforum.res_id = mod_res_id;
					dob.dbforum.res_upd_date = dob.res_timestamp_lst[i];
					dob.dbforum.mod_res_id = mod_res_id;
					dob.dbforum.del(con, dob.prof);
				}

				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("DEL_FOR_MSG")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkModifyPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				if (dob.msgLst != null) {
					for (int index = 0; index < dob.msgLst.length; index++) {
						dob.dbforMsg = new dbForumMessage();
						dob.dbforMsg.fmg_id = Long.parseLong(dob.msgLst[index]);
						dob.dbforMsg.fmg_fto_id = dob.dbforTopic.fto_id;
						dob.dbforMsg.fmg_fto_res_id = dob.dbmod.res_id;
						dob.dbforMsg.del(con, dob.prof);
					}
				}

				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("MARK_FOR_MSGS")) {
			try {

				/*
				 * // check User Right if
				 * (!dbResourcePermission.hasPermission(con, dob.dbmod.res_id,
				 * dob.prof, dbResourcePermission.RIGHT_READ)) { long cosId =
				 * dbModule.getCosId(con, dob.dbmod.res_id); if
				 * (!dbResourcePermission.hasPermission(con, cosId, dob.prof,
				 * dbResourcePermission.RIGHT_READ)) { throw new
				 * qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG); } }
				 */

				long msgId;
				String isRead;

				if (wizbini.cfgSysSetupadv.getForum().isMarkMsg()) {
					for (int i = 0; i < dob.msgLst.length; i++) {
						msgId = Long.parseLong(dob.msgLst[i]);
						isRead = dbForumMarkMsg.isMsgRead(con, dob.prof, msgId);

						if (isRead.equals("FALSE")) {
							dbForumMarkMsg.ins(con, dob.prof.usr_id,
									dob.dbmod.res_id, dob.dbforTopic.fto_id,
									msgId);
						}
					}

					con.commit();
				}

				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UNMARK_FOR_MSGS")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkReadPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				/*
				 * // check User Right if
				 * (!dbResourcePermission.hasPermission(con, dob.dbmod.res_id,
				 * dob.prof, dbResourcePermission.RIGHT_READ)) { long cosId =
				 * dbModule.getCosId(con, dob.dbmod.res_id); if
				 * (!dbResourcePermission.hasPermission(con, cosId, dob.prof,
				 * dbResourcePermission.RIGHT_READ)) { throw new
				 * qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG); } }
				 */

				if (wizbini.cfgSysSetupadv.getForum().isMarkMsg()) {
					long msgId;
					String isRead;

					for (int i = 0; i < dob.msgLst.length; i++) {
						msgId = Long.parseLong(dob.msgLst[i]);
						isRead = dbForumMarkMsg.isMsgRead(con, dob.prof, msgId);

						if (isRead.equals("TRUE")) {
							dbForumMarkMsg.del(con, dob.prof.usr_id, msgId);
						}
					}
				}

				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("VIEW_FOR_MSGS")
				|| dob.prm_ACTION.equalsIgnoreCase("VIEW_FOR_MSGS_XML")) {

			/*
			 * // check User Right if (!dbResourcePermission.hasPermission(con,
			 * dob.dbmod.res_id, dob.prof, dbResourcePermission.RIGHT_READ)) {
			 * long cosId = dbModule.getCosId(con, dob.dbmod.res_id); if
			 * (!dbResourcePermission.hasPermission(con, cosId, dob.prof,
			 * dbResourcePermission.RIGHT_READ)) { throw new
			 * qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG); } }
			 */
			dob.dbforum = new dbForum(dob.dbmod);
			dob.dbforum.res_upd_user = dob.prof.usr_id;
			dob.dbforum.get(con);
			long cosId = dbModule.getCosId(con, dob.dbmod.mod_res_id);

			if (wizbini.cfgSysSetupadv.getForum().isMarkMsg()) {
				dob.dbforum.MARK_MSG = 1;
			} else {
				dob.dbforum.MARK_MSG = 0;
			}

			String result = dob.dbforum.outputGivenMsgsAsXML(con, dob.prof,
					dob.dbdpo.dpo_view, dob.dbforTopic.fto_id, dob.msgLst,
					cosId);
			// answer page variants
			AcPageVariant acPageVariant = new AcPageVariant(con);
			acPageVariant.instance_id = dob.dbmod.mod_res_id;
			acPageVariant.ent_id = dob.prof.usr_ent_id;
			acPageVariant.rol_ext_id = dob.prof.current_role;
			acPageVariant.prof = dob.prof;
			String metaXML = acPageVariant
					.answerPageVariantAsXML((String[]) xslQuestions
							.get(dob.cur_stylesheet));
			result = formatXML(result.substring(result.indexOf("?>") + 2),
					metaXML, "forum", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("VIEW_FOR_MSGS_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("VIEW_FOR_MSGS"))
				generalAsHtml(result, out, dob);

		} else if (dob.prm_ACTION.equalsIgnoreCase("REPLY_FOR_MSGS")
				|| dob.prm_ACTION.equalsIgnoreCase("REPLY_FOR_MSGS_XML")) {
			try {

				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkReadPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				dob.dbforum = new dbForum(dob.dbmod);
				dob.dbforum.res_upd_user = dob.prof.usr_id;
				dob.dbforum.get(con);
				long cosId = dbModule.getCosId(con, dob.dbmod.mod_res_id);

				if (wizbini.cfgSysSetupadv.getForum().isMarkMsg()) {
					dob.dbforum.MARK_MSG = 1;
				} else {
					dob.dbforum.MARK_MSG = 0;
				}

				String result = dob.dbforum.outputGivenMsgsAsXML(con, dob.prof,
						dob.dbdpo.dpo_view, dob.dbforTopic.fto_id, dob.msgLst,
						cosId, true);
				// answer page variants
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.dbmod.mod_res_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				acPageVariant.prof = dob.prof;
				String metaXML = acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet));
				result = formatXML(result.substring(result.indexOf("?>") + 2),
						metaXML, "forum", dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("REPLY_FOR_MSGS")) {
					generalAsHtml(result, out, dob);
				} else if (dob.prm_ACTION
						.equalsIgnoreCase("REPLY_FOR_MSGS_XML")) {
					static_env.outputXML(out, result);
				}
			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_FOR_TOPICS")
				|| dob.prm_ACTION.equalsIgnoreCase("SEARCH_FOR_TOPICS_XML")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkReadPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				/*
				 * // check User Right if
				 * (!dbResourcePermission.hasPermission(con, dob.dbmod.res_id,
				 * dob.prof, dbResourcePermission.RIGHT_READ)) { long cosId =
				 * dbModule.getCosId(con, dob.dbmod.res_id); if
				 * (!dbResourcePermission.hasPermission(con, cosId, dob.prof,
				 * dbResourcePermission.RIGHT_READ)) { throw new
				 * qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG); } }
				 */
				dob.dbforum = new dbForum(dob.dbmod);
				dob.dbforum.res_upd_user = dob.prof.usr_id;
				dob.dbforum.get(con);

				if (wizbini.cfgSysSetupadv.getForum().isMarkMsg()) {
					dob.dbforum.MARK_MSG = 1;
				} else {
					dob.dbforum.MARK_MSG = 0;
				}

				long cosId = dbModule.getCosId(con, dob.dbmod.mod_res_id);
				String result = "";

				if (dob.search_type_topic == 1 && dob.search_type_msg == 0) {
					CommonLog.info("dob.dbforum.searchTopicsAsXML");
					result = dob.dbforum.searchTopicsAsXML(con, sess, dob.prof,
							dob.dbdpo.dpo_view, dob.phrase, dob.phrase_cond,
							dob.created_by, dob.created_by_cond,
							dob.created_after, dob.created_before,
							dob.sort_order, dob.page, cosId, dob.page_size);
				} else if (dob.search_type_msg == 1
						&& dob.search_type_topic == 0) {
					CommonLog.info("dob.dbforum.searchForumMsgsAsXML");
					result = dob.dbforum.searchForumMsgsAsXML(con, sess,
							dob.prof, dob.dbdpo.dpo_view, dob.phrase,
							dob.phrase_cond, dob.created_by,
							dob.created_by_cond, dob.created_after,
							dob.created_before, dob.sort_order, dob.page,
							cosId, dob.page_size);
				} else {
					CommonLog.info("dob.dbforum.searchForumAsXML");
					result = dob.dbforum.searchForumAsXML(con, sess, dob.prof,
							dob.dbdpo.dpo_view, dob.phrase, dob.phrase_cond,
							dob.created_by, dob.created_by_cond,
							dob.created_after, dob.created_before,
							dob.sort_order, dob.page, cosId, dob.page_size);
				}
				// answer page variants
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.instance_id = dob.dbmod.mod_res_id;
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				acPageVariant.prof = dob.prof;
				String metaXML = acPageVariant
						.answerPageVariantAsXML((String[]) xslQuestions
								.get(dob.cur_stylesheet));
				result = formatXML(result.substring(result.indexOf("?>") + 2),
						metaXML, "forum", dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_FOR_TOPICS_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_FOR_TOPICS"))
					generalAsHtml(result, out, dob);

			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_FOR_MSGS")
				|| dob.prm_ACTION.equalsIgnoreCase("SEARCH_FOR_MSGS_XML")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkReadPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				/*
				 * // check User Right if
				 * (!dbResourcePermission.hasPermission(con, dob.dbmod.res_id,
				 * dob.prof, dbResourcePermission.RIGHT_READ)) { long cosId =
				 * dbModule.getCosId(con, dob.dbmod.res_id); if
				 * (!dbResourcePermission.hasPermission(con, cosId, dob.prof,
				 * dbResourcePermission.RIGHT_READ)) { throw new
				 * qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG); } }
				 */
				long cosId = dbModule.getCosId(con, dob.dbmod.mod_res_id);

				dob.dbforum = new dbForum(dob.dbmod);
				dob.dbforum.res_upd_user = dob.prof.usr_id;
				dob.dbforum.get(con);

				if (wizbini.cfgSysSetupadv.getForum().isMarkMsg()) {
					dob.dbforum.MARK_MSG = 1;
				} else {
					dob.dbforum.MARK_MSG = 0;
				}

				String result = dob.dbforum.searchMsgsAsXML(con, sess,
						dob.prof, dob.dbdpo.dpo_view, dob.dbforTopic.fto_id,
						dob.phrase, dob.phrase_cond, dob.created_by,
						dob.created_by_cond, dob.created_after,
						dob.created_before, dob.sort_order, dob.page, cosId);

				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_FOR_MSGS_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_FOR_MSGS"))
					generalAsHtml(result, out, dob);

			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_FAQ_TOPIC")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkModifyPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				dob.dbfaqTopic.fto_res_id = dob.dbmod.res_id;
				dob.dbfaqTopic.ins(con, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_FAQ_TOPIC")) {
			try {

				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkModifyPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				if (dob.topicLst != null) {
					for (int index = 0; index < dob.topicLst.length; index++) {
						dob.dbfaqTopic = new dbFaqTopic();
						dob.dbfaqTopic.fto_id = Long
								.parseLong(dob.topicLst[index]);
						dob.dbfaqTopic.fto_res_id = dob.dbmod.res_id;
						dob.dbfaqTopic.del(con, dob.prof);
					}

					con.commit();
				}
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("INS_FAQ_MSG")) {
			try {

				// access control
				AcModule acmod = new AcModule(con);
				if (dob.dbfaqMsg.fmg_type.equals(dbFaqMessage.FAQ_ANSWER)) {
					if (!acmod
							.checkModifyPermission(dob.prof, dob.dbmod.res_id)) {
						throw new qdbErrMessage(
								dbResourcePermission.NO_RIGHT_WRITE_MSG);
					}
				} else if (dob.dbfaqMsg.fmg_type
						.equals(dbFaqMessage.FAQ_COMMENT)
						|| dob.dbfaqMsg.fmg_type
								.equals(dbFaqMessage.FAQ_QUESTION)) {

				} else {
					throw new qdbException("Wrong Faq Message Type.");
				}

				dob.dbfaqMsg.fmg_fto_id = dob.dbfaqTopic.fto_id;
				dob.dbfaqMsg.fmg_fto_res_id = dob.dbmod.res_id;
				dob.dbfaqMsg.ins(con, dob.prof);
				con.commit();

				// msgBox(MSG_STATUS, con, new qdbErrMessage("MSG005"), dob,
				// out);
				response.sendRedirect(dob.url_success);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_FAQ_MSG")) {
			try {

				// access control
				AcModule acmod = new AcModule(con);
				if (dob.dbfaqMsg.fmg_type.equals(dbFaqMessage.FAQ_ANSWER)) {
					if (!acmod
							.checkModifyPermission(dob.prof, dob.dbmod.res_id)) {
						throw new qdbErrMessage(
								dbResourcePermission.NO_RIGHT_WRITE_MSG);
					}
				} else {
					throw new qdbException("Wrong Faq Message Type.");
				}

				dob.dbfaqMsg.fmg_fto_id = dob.dbfaqTopic.fto_id;
				dob.dbfaqMsg.fmg_fto_res_id = dob.dbmod.res_id;
				dob.dbfaqMsg.updAns(con, dob.prof);
				con.commit();

				// msgBox(MSG_STATUS, con, new qdbErrMessage("MSG005"), dob,
				// out);
				response.sendRedirect(dob.url_success);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DEL_FAQ_MSG")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkModifyPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_WRITE_MSG);
				}

				if (dob.msgLst != null) {
					for (int index = 0; index < dob.msgLst.length; index++) {
						dob.dbfaqMsg = new dbFaqMessage();
						dob.dbfaqMsg.fmg_id = Long.parseLong(dob.msgLst[index]);
						dob.dbfaqMsg.fmg_fto_id = dob.dbfaqTopic.fto_id;
						dob.dbfaqMsg.fmg_fto_res_id = dob.dbmod.res_id;
						dob.dbfaqMsg.del(con, dob.prof);
					}
				}

				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_FAQ")
				|| dob.prm_ACTION.equalsIgnoreCase("SEARCH_FAQ_XML")) {
			try {
				// access control
				AcModule acmod = new AcModule(con);
				if (!acmod.checkReadPermission(dob.prof, dob.dbmod.res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				/*
				 * // check User Right if
				 * (!dbResourcePermission.hasPermission(con, dob.dbmod.res_id,
				 * dob.prof, dbResourcePermission.RIGHT_READ)) { long cosId =
				 * dbModule.getCosId(con, dob.dbmod.res_id); if
				 * (!dbResourcePermission.hasPermission(con, cosId, dob.prof,
				 * dbResourcePermission.RIGHT_READ)) { throw new
				 * qdbErrMessage(dbResourcePermission.NO_RIGHT_READ_MSG); } }
				 */
				/*
				 * if (static_env.INI_MARK_FORUM_MSG != null &&
				 * static_env.INI_MARK_FORUM_MSG.equalsIgnoreCase("YES")) {
				 * dob.dbfaq.MARK_MSG = 1; } else { dob.dbfaq.MARK_MSG = 0; }
				 */

				String result = "";

				dob.dbfaq = new dbFaq(dob.dbmod);
				dob.dbfaq.res_upd_user = dob.prof.usr_id;
				dob.dbfaq.get(con);

				if (dob.dbfaqTopic.fto_id == 0) {
					result = dob.dbfaq.searchInFaqAsXML(con, sess, dob.prof,
							dob.dbdpo.dpo_view, dob.phrase, dob.phrase_cond,
							dob.created_by, dob.created_by_cond,
							dob.created_after, dob.created_before,
							dob.sort_order, dob.page, dob.page_size,
							dob.search_que, dob.search_ans, dob.search_com);
				} else {
					result = dob.dbfaq.searchInTopicAsXML(con, sess, dob.prof,
							dob.dbdpo.dpo_view, dob.dbfaqTopic.fto_id,
							dob.phrase, dob.phrase_cond, dob.created_by,
							dob.created_by_cond, dob.created_after,
							dob.created_before, dob.sort_order, dob.page,
							dob.page_size, dob.search_que, dob.search_ans,
							dob.search_com);
				}

				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_FAQ_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_FAQ"))
					generalAsHtml(result, out, dob);

			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("DOWNLOAD_RES")) {
			if (dob.res_id_lst != null && dob.res_id_lst.length != 0) {
				Vector v_res_lst = new Vector();

				for (int i = 0; i < dob.res_id_lst.length; i++) {
					v_res_lst.addElement(new Long(dob.res_id_lst[i]));
				}

				// access control
				Vector permit_lst = dbResourcePermission.getResPermission(con,
						v_res_lst, dob.prof);
				dbResourcePermission dbrpm;
				int hasPermit = 1;

				for (int i = 0; i < permit_lst.size(); i++) {
					dbrpm = (dbResourcePermission) permit_lst.elementAt(i);
					if (!dbrpm.rpm_read && hasPermit == 1) {
						hasPermit = 0;
					}
				}

				if (hasPermit == 1) {
					try {
						String zipPath = wizbini.getFileUploadResDirAbs();
						String zipFile = wizbini.getFileUploadTmpDirAbs()
								+ dbUtils.SLASH + dob.prof.usr_id
								+ dbUtils.SLASH + "resources.zip";
						dbUtils.makeZip(zipFile, zipPath, dob.res_id_lst, true);
						response.sendRedirect(dob.url_success);
					} catch (qdbErrMessage e) {
						msgBox(MSG_ERROR, con, e, dob, out);
					} catch (ZipException e) {
						msgBox(MSG_ERROR, con, new cwSysMessage("RES004"), dob,
								out);
					} catch (FileNotFoundException e) {
						msgBox(MSG_ERROR, con,
								new qdbErrMessage(e.getMessage()), dob, out);
					}
				} else {
					msgBox(MSG_STATUS, con, new qdbErrMessage("RPM004"), dob,
							out);
				}
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("CHANGE_ROLE")) {
			acSite site = new acSite();
			site.ste_ent_id = dob.prof.root_ent_id;
			site.get(con);

			// no access control
			dob.getUsr().usr_ent_id = dob.prof.usr_ent_id;
			try {
				String url_home = dob.getUsr().changeRole(con, dob.rol_ext_id,
						dob.prof, sess, wizbini);
			} catch (cwException e) {
				RoleManager rf = new RoleManager();
				String role_title = rf.getRoleTitleByExtId(con, dob.rol_ext_id);
				msgBox(MSG_ERROR, con, new cwSysMessage("ROL011", role_title),
						dob, out);
				return;
			}

			dob.prof.xsl_root = wizbini.cfgSysSetupadv.getSkinHome()
					+ dbUtils.SLASH + dob.prof.current_role_skin_root
					+ dbUtils.SLASH
					+ wizbini.cfgSysSetup.getXslStylesheet().getHome();
			dob.xsl_root = dob.prof.xsl_root;

			con.commit();
			response.sendRedirect(dob.prof.goHome(request));
			// response.sendRedirect("/" + dob.prof.skin_root + "/" +
			// dob.prof.current_role_skin_root + "/" + url_home);
			// response.sendRedirect(url_home);
		}
		// Change language of the label
		// this cmd is no longer being used (2005-08-18 kawai)
		// else if (dob.prm_ACTION.equalsIgnoreCase("CHANGE_LAN")) {
		//
		// dob.prof.label_lan = dob.label_lan;
		//
		// //put profile in session
		// sess.setAttribute(AUTH_LOGIN_PROFILE, dob.prof);
		// sess.setAttribute(SESS_QDB_PROFILE_LABEL_LAN, dob.label_lan);
		//
		// response.sendRedirect(dob.url_success);
		//
		// }
		else if (dob.prm_ACTION.equalsIgnoreCase("GEN_TREE")
				|| dob.prm_ACTION.equalsIgnoreCase("GEN_TREE_XML")) {
			//
			//String tcId = request.getParameter("tc_id");
			if (dob.tree.tree_type.equalsIgnoreCase("user_group_and_user")
					&& dob.self_group) {
				dob.tree.node_id = ((Long) dob.prof.usrGroups.elementAt(0))
						.longValue();
			}
			dob.tree.hasGlobalCat = wizbini.cfgSysSetupadv.getOrganization()
					.isMultiple();
			dob.tree.tcEnableInd = wizbini.cfgTcEnabled;
			String treeXML = "";
			if (dob.tree.isCreateTree && !cwTree.enableAppletTree) {
				treeXML = dob.tree.create_treeXML(con, dob.prof);
				// dob.cur_stylesheet = "gen_tree_js.xsl";
			} else {
				String tcId = dob.tcId;
				dob.prof.tcId = tcId;
				dob.prof.group_cmd = dob.group_cmd;
				treeXML = dob.tree.treeXML(con, dob.prof,
						wizbini.cfgSysSetupadv.isTcIndependent());
				if (!dob.tree.enableAppletTree) {
					response.setContentType("text/xml; charset=UTF8");
					out = response.getWriter();
					out.println(treeXML);
					return;
				}
			}

			String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini,
					dob.prof.root_id);
			String result = formatXML(treeXML, metaXML, "tree", dob.prof);
			if (dob.tree.node_id == 0 || dob.tree.flag) {
				if (dob.prm_ACTION.equalsIgnoreCase("GEN_TREE_XML")) {
					static_env.outputXML(out, result);
				}

				if (dob.prm_ACTION.equalsIgnoreCase("GEN_TREE")) {
					generalAsHtml(result, out, dob);
				}
			} else {
				out.println(result);
			}

		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_TREE_DATA")) {
			String result = "";
			// System.out.println("tree type = " + dob.tree.tree_type);
			// System.out.println("node_id_lst = " + dob.node_id_lst);
			// System.out.println("node_type_lst = " + dob.node_type_lst);
			if (dob.tree.tree_type.equalsIgnoreCase(cwTree.USER_GROUP_AND_USER)) {
				result = ViewEntityToTree.getUsersFromGroup(con,
						dob.node_id_lst, dob.node_type_lst);
			} else if (dob.tree.tree_type.equalsIgnoreCase(cwTree.COMPETENCE)) {
				ViewCmToTree cmTree = new ViewCmToTree();
				result = cmTree.getSkills(con, dob.node_id_lst,
						dob.node_type_lst, dob.prof.root_ent_id);
			} else if (dob.tree.tree_type
					.equalsIgnoreCase(cwTree.KNOWLEDGE_OBJECT)) {
				ViewKnowledgeObjectToTree koTree = new ViewKnowledgeObjectToTree();
				result = koTree.getObjects(con, dob.node_id_lst,
						dob.node_type_lst, dob.prof.root_ent_id,
						dob.prof.usrGroupsList());
			}

			out.println(result);
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_SIGNON_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_SIGNON_LST_XML")) {

			String result = "";
			acSignonLink acLink = new acSignonLink();
			String contentXML = acLink.getSignonLinkLstAsXML(con,
					dob.prof.root_ent_id).toString();
			result = formatXML(contentXML, null, "signon", dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_SIGNON_LST_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("GET_SIGNON_LST")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("SIGNON")
				|| dob.prm_ACTION.equalsIgnoreCase("SIGNON_XML")) {

			String result = "";
			acSignonLink acLink = new acSignonLink();
			acLink.slk_id = dob.slk_id;
			acLink.get(con);
			String contentXML = acLink.asXML().toString();
			result = formatXML(contentXML, null, "signon", dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("SIGNON_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("SIGNON")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_USER_TK_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_USER_TK_RPT_XML")) {

			// no access control
			if (dob.cwPage.sortCol == null) {
				dob.cwPage.sortCol = "r_group";
			}
			if (dob.cwPage.sortOrder == null) {
				dob.cwPage.sortOrder = "asc";
			}

			String result = dob.dbcov.getUserTrackingRptAsXML(con, sess, dob.rpt_type, dob.rpt_date_type, dob.res_start_datetime, dob.res_end_datetime, dob.rpt_search_full_name,dob.cwPage, wizbini);

			long itm_id = dbCourse.getCosItemId(con, dob.dbcov.cov_cos_id);
			result += aeItem.genItemActionNavXML(con, itm_id, dob.prof);
			result += "<current_role>" + dob.prof.current_role
					+ "</current_role>";
			result +="<search_value>" +dob.rpt_search_full_name +"</search_value>";
			result = formatXML(result, null, "report", dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_USER_TK_RPT_XML"))
				static_env.outputXML(out, result);
			else
				generalAsHtml(result, out, dob);

			return;
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_RPT_IN_DATE")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_MOD_RPT_IN_DATE_XML")) {

			String result = dob.dbmov.getModuleRptAsXML(con, dob.prof,
					dob.cur_node, dob.rpt_type, dob.res_start_datetime,
					dob.res_end_datetime, dob.dbcos.cos_res_id);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_RPT_IN_DATE_XML"))
				static_env.outputXML(out, result);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_RPT_IN_DATE"))
				generalAsHtml(result, out, dob);
		}

		else if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_EVAL_HIST_RPT")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_MOD_EVAL_HIST_RPT_XML")) {

			// no access control
			String rpt_filename = "report";
			String result = dbModuleEvaluationHistory.getModEvalHistAsXML(con,
					dob.dbcos.res_id, dob.rpt_type, dob.res_start_datetime,
					dob.res_end_datetime, wizbini);
			result = formatXML(result, null, "report", dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_MOD_EVAL_HIST_RPT_XML")) {
				static_env.outputXML(out, result);
			} else {
				if (dob.output_type.equalsIgnoreCase("XLS")) {
					response.setHeader("Cache-Control", "");
					response.setHeader("Pragma", "");
					response.setHeader("Content-Disposition",
							"attachment; filename=" + rpt_filename + ".xls;");
					cwUtils.setContentType("application/vnd.ms-excel",
							response, wizbini);
					String tempReport = null;
					tempReport = aeUtils.transformXML(result,
							dob.cur_stylesheet, static_env, dob.prof.xsl_root);
					out.print(tempReport);
					out.flush();
					out.close();
				} else {
					generalAsHtml(result, out, dob);
				}
			}
			// Tim add begin
		} else if (dob.prm_ACTION.equalsIgnoreCase("get_user_mod_status_list")
				|| dob.prm_ACTION
						.equalsIgnoreCase("get_user_mod_status_list_xml")) {

			try {
				// Check Access Control
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmov.mov_mod_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini,
						dob.prof.root_id);

				if (dob.dbcos.cos_res_id > 0) {
					long itm_id = dbCourse.getCosItemId(con,
							dob.dbcos.cos_res_id);
					metaXML += aeItem
							.genItemActionNavXML(con, itm_id, dob.prof);

					aeItem itm = new aeItem();
					itm.itm_id = itm_id;
					itm.getItem(con);
					metaXML += itm.contentInfoAsXML(con);
				}

				String xml = formatXML(dbModuleEvaluation.getByModuleAsXML(con,
						sess, dob.prof, dob.dbmov.mov_mod_id, dob.cwPage,
						dob.dbmov.mov_status, null, null, null, null, true,
						wizbini), metaXML, "content", dob.prof);

				if (dob.prm_ACTION
						.equalsIgnoreCase("get_user_mod_status_list_xml")) {
					static_env.outputXML(out, xml);
				} else {
					generalAsHtml(xml, out, dob);
				}
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
			return;
		} else if (dob.prm_ACTION.equalsIgnoreCase("get_mod_eas_status_list")
				|| dob.prm_ACTION
						.equalsIgnoreCase("get_mod_eas_status_list_xml")) {

			try {
				// Check Access Control
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmov.mov_mod_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				String mode = request.getParameter("mode");
				String xml = formatXML(dbModuleEvaluation.getModuleByNameAsXML(
						"EAS", con, sess, dob.prof, dob.dbmov.mov_mod_id,
						dob.cwPage, dob.dbmov.mov_status, mode), null,
						"content", dob.prof);

				if (dob.prm_ACTION
						.equalsIgnoreCase("get_mod_eas_status_list_xml")) {
					static_env.outputXML(out, xml);
				} else {
					generalAsHtml(xml, out, dob);
				}
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
			return;
		} else if (dob.prm_ACTION.equalsIgnoreCase("get_mod_eas_individual")
				|| dob.prm_ACTION
						.equalsIgnoreCase("get_mod_eas_individual_xml")) {

			try {
				// Check Access Control
				AcModule acMod = new AcModule(con);
				if (!(dob.prof.usr_ent_id == dob.dbmov.mov_ent_id)
						&& !acMod.checkModifyPermission(dob.prof,
								dob.dbmov.mov_mod_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}

				if (dob.dbmov.mov_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.dbmov.mov_tkh_id = DbTrackingHistory
							.getAppTrackingIDByMod(con, dob.dbmov.mov_mod_id,
									dob.dbmov.mov_ent_id);
				}

				dbModuleEvaluation dbmov = new dbModuleEvaluation();
				dbmov.tmpUploadDir = dob.tmpUploadDir;
				dbmov.bFileUpload = dob.bFileUpload;
				dbmov.RES_FOLDER = static_env.RES_FOLDER;
				String xml = formatXML(dbmov.getEASResultByIndividualAsXML(con,
						dob.dbmov.mov_mod_id, dob.dbmov.mov_ent_id,
						dob.dbmov.mov_tkh_id, dob.prof), null, "content",
						dob.prof);

				if (dob.prm_ACTION
						.equalsIgnoreCase("get_mod_eas_individual_xml")) {
					static_env.outputXML(out, xml);
				} else {
					generalAsHtml(xml, out, dob);
				}
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
			return;
		} else if (dob.prm_ACTION.equalsIgnoreCase("mod_eas_grade_individual")
				|| dob.prm_ACTION
						.equalsIgnoreCase("mod_eas_grade_individual_xml")) {

			try {
				// Check Access Control
				// System.out.println("dob.dbmod.mod_res_id " +
				// dob.dbmod.mod_res_id);
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				dob.dbmov.mov_mod_id = dob.dbmod.mod_res_id;
				// System.out.println("dob.dbmov.mov_update_timestamp " +
				// dob.dbmov.mov_update_timestamp);
				// System.out.println("dob.dbmov.mov_mod_id " +
				// dob.dbmov.mov_mod_id);
				// System.out.println("dob.dbmov.mov_ent_id " +
				// dob.dbmov.mov_ent_id);
				if (dob.dbmov.mov_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.dbmov.mov_tkh_id = DbTrackingHistory
							.getAppTrackingIDByMod(con, dob.dbmov.mov_mod_id,
									dob.dbmov.mov_ent_id);
				}

				if (dob.dbmov.mov_update_timestamp != null) {
					if (!dob.dbmov.checkUpdateTimestamp(con,
							dob.dbmov.mov_update_timestamp)) {
						if (dob.dbmov.mov_ent_id != 0)
							throw new qdbErrMessage("AEQM09",
									dbRegUser.getDisplayBil(con,
											dob.dbmov.mov_ent_id));
						else
							throw new qdbErrMessage("AEQM02");
					}
				}
				dob.dbpgr.pgr_tkh_id = dob.dbmov.mov_tkh_id;
				// dbModuleEvaluation dbmov = new dbModuleEvaluation();

				dob.dbmov.tmpUploadDir = dob.tmpUploadDir;
				dob.dbmov.bFileUpload = dob.bFileUpload;
				dob.dbmov.INI_DIR_UPLOAD = wizbini.getFileUploadResDirAbs();
				/*
				 * System.out.println("ass_filename " + dob.ass_filename);
				 * System.out.println("dob.ass_comment " + dob.ass_comment);
				 * System.out.println("dob.dbmov.mov_max_score " +
				 * dob.dbmod.mod_max_score);
				 * System.out.println("dob.dbmov.mov_pass_score " +
				 * dob.dbmod.mod_pass_score);
				 * System.out.println("dbpgr.pgr_grade " + dob.dbpgr.pgr_grade);
				 * System.out.println("dbpgr.pgr_score " + dob.dbpgr.pgr_score);
				 */
				dob.dbpgr.pgr_res_id = dob.dbmod.mod_res_id;
				dob.dbmov.updateEASStatusWithComment(con, dob.prof, dob.dbpgr,
						dob.ass_filename, dob.ass_comment,
						dob.dbmod.mod_max_score, dob.dbmod.mod_pass_score);

				/*
				 * String xml = formatXML(ea.grade(con, dob.prof, dob.dbpgr,
				 * dob.ass_filename, dob.ass_comment, dob.dbmod.mod_max_score,
				 * dob.dbmod.mod_pass_score), null, "content", dob.prof);
				 */
				con.commit();
				msgBox(MSG_STATUS, con, new cwSysMessage("GEN003"), dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
			return;
		} else if (dob.prm_ACTION.equalsIgnoreCase("mod_eas_reset_individual")
				|| dob.prm_ACTION
						.equalsIgnoreCase("mod_eas_reset_individual_xml")) {

			try {
				// Check Access Control
				// System.out.println("dob.dbmov.mov_mod_id " +
				// dob.dbmov.mov_mod_id);
				// System.out.println("dob.dbmov.mov_ent_id " +
				// dob.dbmov.mov_ent_id);
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmov.mov_mod_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}

				if (dob.dbmov.mov_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.dbmov.mov_tkh_id = DbTrackingHistory
							.getAppTrackingIDByMod(con, dob.dbmov.mov_mod_id,
									dob.dbmov.mov_ent_id);
				}
				if (dob.dbmov.mov_update_timestamp != null) {
					if (!dob.dbmov.checkUpdateTimestamp(con,
							dob.dbmov.mov_update_timestamp)) {
						if (dob.dbmov.mov_ent_id != 0)
							throw new qdbErrMessage("AEQM09",
									dbRegUser.getDisplayBil(con,
											dob.dbmov.mov_ent_id));
						else
							throw new qdbErrMessage("AEQM02");
					}
				}

				dbModuleEvaluation dbmov = new dbModuleEvaluation();

				dbmov.tmpUploadDir = dob.tmpUploadDir;
				dbmov.INI_DIR_UPLOAD = wizbini.getFileUploadResDirAbs();
				dbmov.mov_mod_id = dob.dbmov.mov_mod_id;
				dbmov.mov_ent_id = dob.dbmov.mov_ent_id;
				dbmov.mov_tkh_id = dob.dbmov.mov_tkh_id;
				dbmov.delEAS(con, dob.prof);
				con.commit();
				response.sendRedirect(dob.url_success);

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
			return;
		} else if (dob.prm_ACTION.equalsIgnoreCase("mod_eas_mass_update_one")
				|| dob.prm_ACTION
						.equalsIgnoreCase("mod_eas_mass_update_one_xml")) {

			try {
				// Check Access Control
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				dob.dbmov.mov_mod_id = dob.dbmod.mod_res_id;
				/*
				 * System.out.println("dob.dbmov.mov_update_timestamp " +
				 * dob.dbmov.mov_update_timestamp);
				 * System.out.println("dob.dbmov.mov_mod_id " +
				 * dob.dbmov.mov_mod_id);
				 * System.out.println("dob.dbmov.mov_ent_id " +
				 * dob.dbmov.mov_ent_id);
				 */
				if (dob.dbmov.mov_tkh_id == DbTrackingHistory.TKH_ID_UNDEFINED) {
					dob.dbmov.mov_tkh_id = DbTrackingHistory
							.getAppTrackingIDByMod(con, dob.dbmov.mov_mod_id,
									dob.dbmov.mov_ent_id);
				}

				if (dob.dbmov.mov_update_timestamp != null) {
					if (!dob.dbmov.checkUpdateTimestamp(con,
							dob.dbmov.mov_update_timestamp)) {
						if (dob.dbmov.mov_ent_id != 0)
							throw new qdbErrMessage("AEQM09",
									dbRegUser.getDisplayBil(con,
											dob.dbmov.mov_ent_id));
						else
							throw new qdbErrMessage("AEQM02");
					}
				}
				dob.dbpgr.pgr_tkh_id = dob.dbmov.mov_tkh_id;
				// dbModuleEvaluation dbmov = new dbModuleEvaluation();

				dob.dbmov.tmpUploadDir = dob.tmpUploadDir;
				dob.dbmov.bFileUpload = dob.bFileUpload;
				dob.dbmov.INI_DIR_UPLOAD = wizbini.getFileUploadResDirAbs();
				/*
				 * System.out.println("ass_filename " + dob.ass_filename);
				 * System.out.println("dob.ass_comment " + dob.ass_comment);
				 * System.out.println("dob.dbmov.mov_max_score " +
				 * dob.dbmod.mod_max_score);
				 * System.out.println("dob.dbmov.mov_pass_score " +
				 * dob.dbmod.mod_pass_score);
				 * System.out.println("dbpgr.pgr_grade " + dob.dbpgr.pgr_grade);
				 * System.out.println("dbpgr.pgr_score " + dob.dbpgr.pgr_score);
				 */
				dob.dbpgr.pgr_res_id = dob.dbmod.mod_res_id;
				dob.dbmov.updateEASStatus(con, dob.prof, dob.dbpgr,
						dob.ass_filename, dob.ass_comment,
						dob.dbmod.mod_max_score, dob.dbmod.mod_pass_score);
				/*
				 * String xml = formatXML(ea.grade(con, dob.prof, dob.dbpgr,
				 * dob.ass_filename, dob.ass_comment, dob.dbmod.mod_max_score,
				 * dob.dbmod.mod_pass_score), null, "content", dob.prof);
				 */
				con.commit();
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
			return;
		} else if (dob.prm_ACTION.equalsIgnoreCase("get_user_mod_status_list")
				|| dob.prm_ACTION
						.equalsIgnoreCase("get_user_mod_status_list_xml")) {

			try {
				// Check Access Control
				AcModule acMod = new AcModule(con);
				if (!acMod
						.checkModifyPermission(dob.prof, dob.dbmov.mov_mod_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}

				String xml = formatXML(dbModuleEvaluation.getByModuleAsXML(con,
						sess, dob.prof, dob.dbmov.mov_mod_id, dob.cwPage,
						dob.dbmov.mov_status), null, "content", dob.prof);

				if (dob.prm_ACTION
						.equalsIgnoreCase("get_user_mod_status_list_xml")) {
					static_env.outputXML(out, xml);
				} else {
					generalAsHtml(xml, out, dob);
				}
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
			return;
			// Tim add end
		}
		/* NEW Knowelege Manager */
		// START
		else if (dob.prm_ACTION.equalsIgnoreCase("GET_SYB_OBJ")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_SYB_OBJ_XML")) {
			// no access control
			try {
				String lang = dob.dbres.res_lan;
				String result = "";
				AcObjective acObj = new AcObjective(con);
				if (acObj.hasAdminPrivilege(dob.prof.usr_ent_id,
						dob.prof.current_role)
						|| DbAcRole.isNormalRole(con, dob.prof.current_role)) {
					if (AccessControlWZB
							.hasRolePrivilege(dob.prof.current_role,
									AclFunction.FTN_AMD_RES_MAIN)
							&& AccessControlWZB
									.isRoleTcInd(dob.prof.current_role)) {
						if (!ViewTrainingCenter.hasEffTc(con,
								dob.prof.usr_ent_id)) {
							cwSysMessage e = new cwSysMessage("TC007",
									Long.toString(dob.dbque.res_id));
							msgBox(MSG_ERROR, con, e, dob, out);
							return;
						}
					}
					result = dbObjective.getRootSybObj(con,
							dob.dbsyb.syl_privilege, lang, dob.types,
							dob.subtypes, dob.prof, null, dob.show_all,
							wizbini.cfgTcEnabled, dob.dbobj.obj_tcr_id,
							dob.cwPage,
							wizbini.cfgSysSetupadv.isTcIndependent());
				} else {
					boolean isInstr = AccessControlWZB
							.isIstRole(dob.prof.current_role);
					if (wizbini.cfgTcEnabled
							&& !ViewTrainingCenter.hasEffTc(con,
									dob.prof.usr_ent_id) && !isInstr) {
						throw new qdbErrMessage("TC007");
					}

					if (dob.viewRes.equals(VIEW_RES)) {
						result = dbObjective.getMyRootSybObj(con,
								dob.dbsyb.syl_privilege, lang, dob.types,
								dob.subtypes, dob.prof, dob.viewRes,
								dob.show_all, wizbini.cfgTcEnabled,
								dob.dbobj.obj_tcr_id, dob.cwPage);
					} else {
						result = dbObjective.getRootSybObj(con,
								dob.dbsyb.syl_privilege, lang, dob.types,
								dob.subtypes, dob.prof, dob.viewRes,
								dob.show_all, wizbini.cfgTcEnabled,
								dob.dbobj.obj_tcr_id, dob.cwPage,
								wizbini.cfgSysSetupadv.isTcIndependent());
					}
				}
				if (dob.prm_ACTION.equalsIgnoreCase("GET_SYB_OBJ"))
					generalAsHtml(result, out, dob);
				if (dob.prm_ACTION.equalsIgnoreCase("GET_SYB_OBJ_XML"))
					static_env.outputXML(out, result);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}
		// else if (dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST"))
		else if (dob.prm_ACTION.equalsIgnoreCase("TEST_GET_OBJ_LST")
				|| dob.prm_ACTION.equalsIgnoreCase("TEST_GET_OBJ_LST_XML")) {
			try {
				if (wizbini.cfgTcEnabled) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						dob.dbobj.get(con);
						if (dob.dbobj.obj_share_ind) {
							dob.dbobj.share_mode = true;
						} else {
							throw new qdbErrMessage(code);
						}
					}
				}
				String lang = dob.dbres.res_lan;
				String result = dbObjective.getObjLstAsXML(con, dob.prof,
						dob.dbobj.obj_id, dob.dbsyb.syl_id, lang, dob.types,
						dob.subtypes, true, dob.viewRes, dob.cur_stylesheet,
						dob.show_all, wizbini.cfgTcEnabled,
						dob.dbobj.share_mode);
				if (dob.prm_ACTION.equalsIgnoreCase("TEST_GET_OBJ_LST"))
					generalAsHtml(result, out, dob);
				if (dob.prm_ACTION.equalsIgnoreCase("TEST_GET_OBJ_LST_XML"))
					static_env.outputXML(out, result);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}
		// else if (dob.prm_ACTION.equalsIgnoreCase("GET_OBJ_LST_HDR"))
		else if (dob.prm_ACTION.equalsIgnoreCase("TEST_GET_OBJ_LST_HDR")
				|| dob.prm_ACTION.equalsIgnoreCase("TEST_GET_OBJ_LST_HDR_XML")) {
			try {
				long objId = Long.parseLong(dob.robs[0]);
				String resultRes = null;
				if (wizbini.cfgTcEnabled && dob.dbobj.obj_id > 0) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							objId, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						dob.dbobj.obj_id = objId;
						dob.dbobj.get(con);
						if (dob.dbobj.obj_share_ind) {
							dob.dbobj.share_mode = true;
						}
					}
					resultRes = dob.dbres.getResListAsXML(con, sess, objId,
							dob.types, dob.subtypes, dob.prof, dob.cwPage,
							"res_lst.xsl", dob.dbobj.share_mode, false);
				}
				if (wizbini.cfgTcEnabled && dob.dbobj.obj_id > 0) {
					AcTrainingCenter acTc = new AcTrainingCenter(con);
					String code = acTc.hasObjInMgtTc(dob.prof.usr_ent_id,
							dob.dbobj.obj_id, dob.prof.current_role);
					if (!code.equals(dbObjective.CAN_MGT_OBJ)) {
						dob.dbobj.get(con);
						if (dob.dbobj.obj_share_ind) {
							dob.dbobj.share_mode = true;
						} else {
							throw new qdbErrMessage(code);
						}
					}
				}
				String lang = dob.dbres.res_lan;
				String result = dbObjective.getObjLstAsXML(con, dob.prof,
						dob.dbobj.obj_id, dob.dbsyb.syl_id, null, null, null,
						false, dob.viewRes, dob.cur_stylesheet, dob.show_all,
						wizbini.cfgTcEnabled, dob.dbobj.share_mode, dob.dbobj);
				result = result.replaceAll("</objective_list>", "");
				result += resultRes;
				result += "</objective_list>";
				if (dob.prm_ACTION.equalsIgnoreCase("TEST_GET_OBJ_LST_HDR"))
					generalAsHtml(result, out, dob);
				if (dob.prm_ACTION.equalsIgnoreCase("TEST_GET_OBJ_LST_HDR_XML"))
					static_env.outputXML(out, result);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		}
		// END
		else if (dob.prm_ACTION.equalsIgnoreCase("GET_UGR_TREE")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_UGR_TREE_XML")) {
			// boolean hasPrivilege = DbUserGrade.hasPrivilege(con, dob.prof);
			// boolean isSupTa = ViewTrainingCenter.isSuperTA(con,
			// dob.prof.root_ent_id, dob.prof.usr_ent_id,
			// dob.prof.current_role);
			/*
			 * if(AccessControlWZB.isRoleTcInd(dob.prof.current_role) ||
			 * (!AccessControlWZB.isRoleTcInd(dob.prof.current_role) &&
			 * !AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
			 * AclFunction.FTN_AMD_GRADE_MAIN))){ if (!hasPrivilege) { throw new
			 * cwSysMessage("UGR002"); } }
			 */
			String result = formatXML("", null, "tree", dob.prof);
			if (dob.prm_ACTION.equalsIgnoreCase("GET_UGR_TREE_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("GET_UGR_TREE")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_TOP_NAV")
				|| dob.prm_ACTION.equalsIgnoreCase("UGR_TOP_NAV_XML")) {

			// answer page variants
			StringBuffer metaXMLBuf = new StringBuffer(1024);
			AcPageVariant acPageVariant = new AcPageVariant(con);
			acPageVariant.instance_id = dob.dbusg.usg_ent_id;
			acPageVariant.ent_id = dob.prof.usr_ent_id;
			acPageVariant.rol_ext_id = dob.prof.current_role;
			acPageVariant.root_id = dob.prof.root_id;
			acPageVariant.setWizbiniLoader(qdbAction.wizbini);
			// metaXMLBuf.append(acPageVariant.answerPageVariantAsXML((String[])
			// xslQuestions.get(dob.cur_stylesheet)));
			metaXMLBuf.append(acPageVariant
					.answerPageVariantAsXML((String[]) xslQuestions
							.get("usr_manager.xsl")));
			metaXMLBuf.append(dbRegUser.getUserAttributeInfoXML(wizbini,
					dob.prof.root_id));
			String result = formatXML("", metaXMLBuf.toString(), "tree",
					dob.prof);

			// String result = formatXML("", null, "tree", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("UGR_TOP_NAV_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_TOP_NAV")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_NAV")
				|| dob.prm_ACTION.equalsIgnoreCase("UGR_NAV_XML")) {
			StringBuffer xml = UserGrade.getAllUserGradesAsXML(con, dob.prof);
			if (!cwTree.enableAppletTree && dob.tree.isCreateTree) {
				String treeXML = "<tree></tree>";
				if (xml != null) {
					treeXML = aeUtils.transformXML(xml.toString(),
							"ugr_structure_xml_js.xsl", static_env, null);
				}
				cwUtils.setContentType("text/xml", response, wizbini);
				out = response.getWriter();
				out.println(treeXML);
				return;
			}
			xml.append("<enableAppletTree>").append(cwTree.enableAppletTree)
					.append("</enableAppletTree>");
			String result = formatXML(xml.toString(), null, "tree", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("UGR_NAV_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_NAV")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_INFO")
				|| dob.prm_ACTION.equalsIgnoreCase("UGR_INFO_XML")) {
			String result = formatXML("", dbRegUser.getUserAttributeInfoXML(
					wizbini, dob.prof.root_id), "tree", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("UGR_INFO_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_INFO")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_UGR")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_UGR_XML")) {
			StringBuffer xml = dob.userGrade.asXML(con);
			String result = formatXML(xml.toString(), null, "tree", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_UGR_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("GET_UGR")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_INS_PREP")
				|| dob.prm_ACTION.equalsIgnoreCase("UGR_INS_PREP_XML")) {
			String result = formatXML("", dbRegUser.getUserAttributeInfoXML(
					wizbini, dob.prof.root_id), "tree", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("UGR_INS_PREP_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_INS_PREP")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_INS")) {
			try {
				dob.userGrade.ugr_ent_id_root = dob.prof.root_ent_id;
				dob.userGrade.ugr_code=request.getParameter("ugr_grade_code");
				dob.userGrade.ugr_tcr_id = dob.prof.my_top_tc_id;
				dob.userGrade.ins(con, dob.prof.usr_id);
				con.commit();
				dob.userGrade.get(con);
				ObjectActionLog log = new ObjectActionLog(
						dob.userGrade.ugr_ent_id, dob.userGrade.ugr_code,
						dob.userGrade.ugr_display_bil,
						ObjectActionLog.OBJECT_TYPE_UGR,
						ObjectActionLog.OBJECT_ACTION_ADD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);
				dob.url_success += "&ugr_ent_id=" + dob.userGrade.ugr_ent_id;
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_UPD_PREP")
				|| dob.prm_ACTION.equalsIgnoreCase("UGR_UPD_PREP_XML")) {
			StringBuffer xml = dob.userGrade.asXML(con);
			String result = formatXML(
					xml.toString(),
					dbRegUser
							.getUserAttributeInfoXML(wizbini, dob.prof.root_id),
					"tree", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("UGR_UPD_PREP_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_UPD_PREP")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_UPD")) {
			try {
				dob.userGrade.ugr_ent_id_root = dob.prof.root_ent_id;
				dob.userGrade.ugr_code=request.getParameter("ugr_grade_code");
				dob.userGrade.upd(con, dob.prof.usr_id);
				con.commit();
				dob.userGrade.get(con);
				ObjectActionLog log = new ObjectActionLog(
						dob.userGrade.ugr_ent_id, dob.userGrade.ugr_code,
						dob.userGrade.ugr_display_bil,
						ObjectActionLog.OBJECT_TYPE_UGR,
						ObjectActionLog.OBJECT_ACTION_UPD,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);
				response.sendRedirect(dob.url_success);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_DEL")
				|| dob.prm_ACTION.equalsIgnoreCase("UGR_DEL_XML")) {
			StringBuffer xml = new StringBuffer();
			xml.append(dob.userGrade.asXML(con))
					.append(dob.userGrade.getAffectedLrnSolListAsXML(con))
					.append(dob.userGrade.getAffectedUserListAsXML(con));
			String result = formatXML(
					xml.toString(),
					dbRegUser
							.getUserAttributeInfoXML(wizbini, dob.prof.root_id),
					"tree", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("UGR_DEL_XML")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_DEL")) {
				generalAsHtml(result, out, dob);
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_DEL_CONFIRM")) {
			try {
				dob.userGrade.ugr_ent_id_root = dob.prof.root_ent_id;
				dob.userGrade.get(con);
				ObjectActionLog log = new ObjectActionLog(
						dob.userGrade.ugr_ent_id, dob.userGrade.ugr_code,
						dob.userGrade.ugr_display_bil,
						ObjectActionLog.OBJECT_TYPE_UGR,
						ObjectActionLog.OBJECT_ACTION_DEL,
						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
						dob.prof.getUsr_ent_id(),
						dob.prof.getUsr_last_login_date(), dob.prof.getIp());
				dob.userGrade.del(con, dob.prof.usr_id);
				con.commit();
				SystemLogContext.saveLog(log,
						SystemLogTypeEnum.OBJECT_ACTION_LOG);
				cwSysMessage e = new cwSysMessage("GEN002");
				msgBox(MSG_STATUS, con, e, dob, out);
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("UGR_ORDER_UPD")) {
			// System.out.println(dob.ugr_order);
			dob.userGrade.updOrder(con, dob.ugr_order);
			con.commit();
			response.sendRedirect(dob.url_success);
		} else if (dob.prm_ACTION.equalsIgnoreCase("VALIDATE_DXT")) {
			dob.dbmod.get(con);
			boolean bOK = true;
			try {
				dob.dbmod.getDynamicQueAsXML(con, dob.prof.usr_id, dob.dbmsp,
						dob.robs, wizbini.getFileUploadResDirAbs(),
						new Vector(), true, false);
			} catch (qdbErrMessage e) {
				bOK = false;
			}
			if (bOK) {
				msgBox(MSG_STATUS, con, new cwSysMessage("ASM005"), dob, out,
						false);
			} else {
				msgBox(MSG_ERROR, con, new cwSysMessage("ASM006"), dob, out,
						false);
			}

		} else if (dob.prm_ACTION.equalsIgnoreCase("PREVIEW_ASM")
				|| dob.prm_ACTION.equalsIgnoreCase("PREVIEW_ASM_XML")
				|| dob.prm_ACTION.equalsIgnoreCase("PREVIEW_LEARNING_RES") 
				|| dob.prm_ACTION.equalsIgnoreCase("PREVIEW_LEARNING_RES_XML")) {
			try {
				String asm_type = "";

				dbResource myDbResource = new dbResource();
				myDbResource.res_id = dob.dbmod.res_id;
				myDbResource.get(con);

				// for dynamic assessment
				if (myDbResource.res_subtype
						.equalsIgnoreCase(dbResource.RES_SUBTYPE_DAS)) {
					asm_type = dbResource.RES_SUBTYPE_DAS;
					if ((dob.dbmod.mod_type).equalsIgnoreCase("TST")
							|| (dob.dbmod.mod_type).equalsIgnoreCase("DXT")) {
						dob.dbmod.mod_type = dbModule.MOD_TYPE_DXT;
						dob.dbmod.res_subtype = dbModule.MOD_TYPE_DXT;
					} else {
						dob.dbmod.mod_type = dbModule.MOD_TYPE_STX;
						dob.dbmod.res_subtype = dbModule.MOD_TYPE_STX;
					}
				} else {
					asm_type = dbResource.RES_SUBTYPE_FAS;
					if ((dob.dbmod.mod_type).equalsIgnoreCase("TST")
							|| (dob.dbmod.mod_type).equalsIgnoreCase("DXT")) {
						dob.dbmod.res_subtype = dbModule.MOD_TYPE_TST;
						dob.dbmod.mod_type = dbModule.MOD_TYPE_TST;
					} else {
						dob.dbmod.res_subtype = dbModule.MOD_TYPE_EXC;
						dob.dbmod.mod_type = dbModule.MOD_TYPE_EXC;
					}
				}

				dob.dbmod.res_title = myDbResource.res_title;
				dob.dbmod.res_desc = myDbResource.res_desc;
				dob.dbmod.mod_in_eff_start_datetime = Timestamp
						.valueOf(dbUtils.MIN_TIMESTAMP);
				dob.dbmod.mod_in_eff_end_datetime = Timestamp
						.valueOf(dbUtils.MAX_TIMESTAMP);
				dob.dbmod.mod_instructor_ent_id_lst = new long[1];
				dob.dbmod.mod_instructor_ent_id_lst[0] = dob.prof.usr_ent_id;

				dob.dbmod.res_upd_user = dob.prof.usr_id;
				dob.dbmod.res_usr_id_owner = dob.prof.usr_id;

				// overide the module's selection logic
				DynamicAssessment myDynamicAssessment = new DynamicAssessment();
				myDynamicAssessment.res_id = myDbResource.res_id;
				myDynamicAssessment.get(con);
				dob.dbmod.mod_logic = myDynamicAssessment.qct_select_logic;

				// modId = dob.dbcos.insModule(con, dob.dbmod, dob.domain,
				// dob.prof);
				dob.dbmod.res_status = dbResource.RES_STATUS_ON;
				dob.dbmod.res_upd_user = dob.prof.usr_id;
				dob.dbmod.res_usr_id_owner = dob.prof.usr_id;
				dob.dbmod.ins(con, dob.prof);

				dob.dbmod.selectAssessment(con, dob.prof, myDbResource.res_id,
						asm_type, static_env.INI_DIR_UPLOAD, true, null);

				float time_limit = dob.dbmod.res_duration;

				// Contains the question ids in the test
				Vector queIdVec = new Vector();
				String result = null;
				if (dob.prm_ACTION.equalsIgnoreCase("PREVIEW_ASM") || dob.prm_ACTION.equalsIgnoreCase("PREVIEW_ASM_XML")) {
					result = dob.dbmod.genTestAsXML(con, dob.prof, dob.dbmsp, dob.robs, time_limit,
							static_env.INI_DIR_UPLOAD, queIdVec, false, dob.tkh_id, true, true);
				} else {
					result = dob.dbmod.genLearningResAsXML(con, dob.prof, time_limit, 
							queIdVec, myDbResource.res_id, false, true);
				}
				StringBuffer resultBuf = new StringBuffer(result);
				AcPageVariant acPageVariant = new AcPageVariant(con);
				acPageVariant.ent_id = dob.prof.usr_ent_id;
				acPageVariant.rol_ext_id = dob.prof.current_role;
				String metaXML = "<meta>"
						+ dob.prof.asXML()
						+ acPageVariant
								.answerPageVariantAsXML((String[]) xslQuestions
										.get(dob.cur_stylesheet)) + "</meta>";

				dbRegUser regUser = new dbRegUser();
				regUser.usr_ent_id = dob.prof.usr_ent_id;
				regUser.get(con);
				metaXML += "<user_photo>"
						+ wizbini.getUserPhotoPath(dob.prof,
								dob.prof.usr_ent_id, regUser.urx_extra_43)
						+ "</user_photo>";

				resultBuf.insert(result.indexOf("<key>"), metaXML);
				result = resultBuf.toString();

				if (dob.prm_ACTION.equalsIgnoreCase("PREVIEW_ASM_XML") || dob.prm_ACTION.equalsIgnoreCase("PREVIEW_LEARNING_RES_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("PREVIEW_ASM") || dob.prm_ACTION.equalsIgnoreCase("PREVIEW_LEARNING_RES"))
					generalAsHtml(result, out, dob);

				// rollback to delete all related records created during the
				// assessment preview
				con.rollback();
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}

		} else if (dob.prm_ACTION.equalsIgnoreCase("EXPORT_TST")
				|| dob.prm_ACTION.equalsIgnoreCase("EXPORT_TST_XML")) {
			try {
				dbResource myDbResource = new dbResource();
				myDbResource.res_id = dob.dbmod.res_id;
				myDbResource.get(con);
				float time_limit;
				if (myDbResource.res_type.equals(dbResource.RES_TYPE_ASM)) {
					// for dynamic assessment
					String asm_type = "";
					if (myDbResource.res_subtype
							.equalsIgnoreCase(dbResource.RES_SUBTYPE_DAS)) {
						asm_type = dbResource.RES_SUBTYPE_DAS;
						if ((dob.dbmod.mod_type).equalsIgnoreCase("TST")
								|| (dob.dbmod.mod_type).equalsIgnoreCase("DXT")) {
							dob.dbmod.mod_type = dbModule.MOD_TYPE_DXT;
							dob.dbmod.res_subtype = dbModule.MOD_TYPE_DXT;
						} else {
							dob.dbmod.mod_type = dbModule.MOD_TYPE_STX;
							dob.dbmod.res_subtype = dbModule.MOD_TYPE_STX;
						}
					} else {
						asm_type = dbResource.RES_SUBTYPE_FAS;
						if ((dob.dbmod.mod_type).equalsIgnoreCase("TST")
								|| (dob.dbmod.mod_type).equalsIgnoreCase("DXT")) {
							dob.dbmod.res_subtype = dbModule.MOD_TYPE_TST;
							dob.dbmod.mod_type = dbModule.MOD_TYPE_TST;
						} else {
							dob.dbmod.res_subtype = dbModule.MOD_TYPE_EXC;
							dob.dbmod.mod_type = dbModule.MOD_TYPE_EXC;
						}
					}

					dob.dbmod.res_title = myDbResource.res_title;
					dob.dbmod.res_desc = myDbResource.res_desc;
					dob.dbmod.mod_in_eff_start_datetime = Timestamp
							.valueOf(dbUtils.MIN_TIMESTAMP);
					dob.dbmod.mod_in_eff_end_datetime = Timestamp
							.valueOf(dbUtils.MAX_TIMESTAMP);
					dob.dbmod.mod_instructor_ent_id_lst = new long[1];
					dob.dbmod.mod_instructor_ent_id_lst[0] = dob.prof.usr_ent_id;

					dob.dbmod.res_upd_user = dob.prof.usr_id;
					dob.dbmod.res_usr_id_owner = dob.prof.usr_id;

					// overide the module's selection logic
					DynamicAssessment myDynamicAssessment = new DynamicAssessment();
					myDynamicAssessment.res_id = myDbResource.res_id;
					myDynamicAssessment.get(con);
					dob.dbmod.mod_logic = myDynamicAssessment.qct_select_logic;

					// modId = dob.dbcos.insModule(con, dob.dbmod, dob.domain,
					// dob.prof);
					dob.dbmod.res_upd_user = dob.prof.usr_id;
					dob.dbmod.res_usr_id_owner = dob.prof.usr_id;
					dob.dbmod.ins(con, dob.prof);

					dob.dbmod.selectAssessment(con, dob.prof,
							myDbResource.res_id, asm_type,
							static_env.INI_DIR_UPLOAD, true, null);
					time_limit = dob.dbmod.res_duration;
				} else {
					// set upd user
					dob.dbmod.res_upd_user = dob.prof.usr_id;
					// access control
					AcModule acMod = new AcModule(con);
					if (!acMod.checkReadPermission(dob.prof,
							dob.dbmod.mod_res_id)) {
						throw new qdbErrMessage(
								dbResourcePermission.NO_RIGHT_READ_MSG);
					}
					time_limit = dob.dbmod.res_duration;
					dob.dbmod.get(con);
				}

				// Contains the question ids in the test
				Vector queIdVec = new Vector();
				String result = dob.dbmod.genTestAsXML(con, dob.prof,
						dob.dbmsp, dob.robs, time_limit,
						static_env.INI_DIR_UPLOAD, queIdVec, true, 0, true,
						true);
				int ins_index = result.indexOf("<key>");
				StringBuffer resultBuf = new StringBuffer(result);
				String metaXML = "<meta>";
				metaXML += "<export_mode>1</export_mode>";
				int sn = (int) (new Random().nextFloat() * 10000);
				metaXML += "<sn>" + myDbResource.res_id + "-" + sn + "</sn>";
				if (myDbResource.res_type.equals(dbResource.RES_TYPE_ASM)) {
					metaXML += "<mod_source_type subtype=\""
							+ myDbResource.res_subtype + "\">"
							+ myDbResource.res_type + "</mod_source_type>";
				}
				metaXML += dob.prof.asXML();
				metaXML += "</meta>";
				resultBuf.insert(ins_index, metaXML);

				if (dob.prm_ACTION.equalsIgnoreCase("EXPORT_TST_XML"))
					static_env.outputXML(out, resultBuf.toString());
				if (dob.prm_ACTION.equalsIgnoreCase("EXPORT_TST")) {
					// generalAsHtml(result, out, dob);
					response.setHeader("Cache-Control", "");
					response.setHeader("Pragma", "");
					File session_dir = new File(dob.session_upload_dir + sn);
					if (!session_dir.exists())
						session_dir.mkdirs();

					String[] files = new String[queIdVec.size() + 3];

					// file1
					File file1 = new File(session_dir, "test-" + myDbResource.res_id + "-" + sn + ".doc");
					PrintWriter fileout1 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file1),static_env.ENCODING));
					generalAsHtml(resultBuf.toString(), fileout1, dob);
					fileout1.close();
					files[0] = file1.getName();

					// file2
					File file2 = new File(session_dir, "test-ans-" + myDbResource.res_id + "-" + sn + ".doc");
					PrintWriter fileout2 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file2),static_env.ENCODING));
					generalAsHtml((resultBuf.replace(ins_index + 6, ins_index + 34,"<export_mode>2</export_mode>")).toString(),fileout2, dob);
					fileout2.close();
					files[1] = file2.getName();

					// file3
					File file3 = new File(session_dir, "ans-" + myDbResource.res_id + "-" + sn + ".xls");
					PrintWriter fileout3 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file3),static_env.ENCODING));
					generalAsHtml((resultBuf.replace(ins_index + 6, ins_index + 34,"<export_mode>3</export_mode>")).toString(),fileout3, dob);
					fileout3.close();
					files[2] = file3.getName();

					for (int i = 0; i < queIdVec.size(); i++) {
						String queId = ((Long) queIdVec.elementAt(i))
								.toString();
						dbUtils.copyDir(static_env.INI_DIR_UPLOAD
								+ cwUtils.SLASH + queId, dob.session_upload_dir
								+ sn + cwUtils.SLASH + queId);
						files[i + 3] = queId;
					}

					String zipPath = dob.session_upload_dir + sn;
					File zipFile = new File(session_dir, "test-"
							+ myDbResource.res_id + "-" + sn + ".zip");
					dbUtils.makeZip(zipFile.getPath(), zipPath, files, true);

					String tmpPath = zipFile.getPath();
					tmpPath = tmpPath.substring(static_env.DOC_ROOT.length());
					response.sendRedirect(".."
							+ cwUtils.replaceSlashToHttp(tmpPath));
				}
				// rollback to delete all related records created during the
				// assessment preview
				con.rollback();

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("EXPORT_EVN")
				|| dob.prm_ACTION.equalsIgnoreCase("EXPORT_EVN_XML")) {
			try {
				dbResource myDbResource = new dbResource();
				myDbResource.res_id = dob.dbmod.res_id;
				myDbResource.get(con);
				float time_limit;
				// set upd user
				dob.dbmod.res_upd_user = dob.prof.usr_id;
				// access control
				AcModule acMod = new AcModule(con);
				if (!acMod.checkReadPermission(dob.prof, dob.dbmod.mod_res_id)) {
					throw new qdbErrMessage(
							dbResourcePermission.NO_RIGHT_READ_MSG);
				}
				time_limit = dob.dbmod.res_duration;
				dob.dbmod.get(con);
				// Contains the question ids in the test
				Vector queIdVec = new Vector();
				String result = dob.dbmod.genTestAsXML(con, dob.prof,
						dob.dbmsp, dob.robs, time_limit,
						static_env.INI_DIR_UPLOAD, queIdVec, true, 0, true,
						true);
				int ins_index = result.indexOf("<key>");
				StringBuffer resultBuf = new StringBuffer(result);
				String metaXML = "<meta>";
				metaXML += "<export_mode>1</export_mode>";
				int sn = (int) (new Random().nextFloat() * 10000);
				metaXML += "<sn>" + myDbResource.res_id + "-" + sn + "</sn>";
				metaXML += dob.prof.asXML();
				metaXML += "</meta>";
				resultBuf.insert(ins_index, metaXML);

				if (dob.prm_ACTION.equalsIgnoreCase("EXPORT_EVN_XML"))
					static_env.outputXML(out, resultBuf.toString());
				if (dob.prm_ACTION.equalsIgnoreCase("EXPORT_EVN")) {
					response.setHeader("Cache-Control", "");
					response.setHeader("Pragma", "");
					File session_dir = new File(dob.session_upload_dir + sn);
					if (!session_dir.exists())
						session_dir.mkdirs();

					String[] files = new String[queIdVec.size() + 2];

					File file1 = new File(session_dir, "eval-"
							+ myDbResource.res_id + "-" + sn + ".doc");
					PrintWriter fileout1 = new PrintWriter(
							new OutputStreamWriter(new FileOutputStream(file1),
									static_env.ENCODING));
					generalAsHtml(resultBuf.toString(), fileout1, dob);
					fileout1.close();
					files[0] = file1.getName();

					File file2 = SurveyReport.exportResQue(con, dob.prof, myDbResource.res_id, sn,
							dob.session_upload_dir, wizbini);
					/*File file2 = new File(session_dir, "ans-"
							+ myDbResource.res_id + "-" + sn + ".xls");
					PrintWriter fileout2 = new PrintWriter(
							new OutputStreamWriter(new FileOutputStream(file2),
									static_env.ENCODING));
					generalAsHtml(
							(resultBuf.replace(ins_index + 6, ins_index + 34,
									"<export_mode>3</export_mode>")).toString(),
							fileout2, dob);
					fileout2.close();*/
					files[1] = file2.getName();

					for (int i = 0; i < queIdVec.size(); i++) {
						String queId = ((Long) queIdVec.elementAt(i))
								.toString();
						dbUtils.copyDir(static_env.INI_DIR_UPLOAD
								+ cwUtils.SLASH + queId, dob.session_upload_dir
								+ sn + cwUtils.SLASH + queId);
						files[i + 2] = queId;
					}

					String zipPath = dob.session_upload_dir + sn;
					File zipFile = new File(session_dir, "eval-"
							+ myDbResource.res_id + "-" + sn + ".zip");
					dbUtils.makeZip(zipFile.getPath(), zipPath, files, true);

					String tmpPath = zipFile.getPath();
					tmpPath = tmpPath.substring(static_env.DOC_ROOT.length());
					String tmp = tmpPath.replaceAll("\\\\", "/");
					response.sendRedirect(".." + tmp);
				}
				// rollback to delete all related records created during the
				// assessment preview
				con.rollback();
			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("heart_beat")) {
			// this is just for touching the session to prevent it from time-out
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_SYS_SETTING")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_SYS_SETTING_XML")) {
			if (!AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
					AclFunction.FTN_AMD_SYSTEM_SETTING_MGT)) {
				throw new cwSysMessage("ACL002");
			}

			String sysXml = SystemSetting.getSystemSettingXml(con);

			String result = formatXML(sysXml, "", "setting", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_SYS_SETTING_XML"))
				static_env.outputXML(out, result.toString());
			if (dob.prm_ACTION.equalsIgnoreCase("GET_SYS_SETTING"))
				generalAsHtml(result.toString(), out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPD_SYS_SETTING")) {

			AccessControlWZB acWzb = new AccessControlWZB();
			if (!AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
					AclFunction.FTN_AMD_SYSTEM_SETTING_MGT)) {
				throw new cwSysMessage("ACL002");
			}
			SystemSetting.updateSystemSetting(con, dob.prof.usr_id,
					dob.threshold_warn, dob.threshold_block, dob.support_email,
					dob.multiple_login_ind);
			msgBox(MSG_STATUS, con, new cwSysMessage("GEN003"), dob, out);

		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_CUR_ACT_USER")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_CUR_ACT_USER_XML")) {

			if (!AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
					AclFunction.FTN_AMD_SYS_SETTING_LOG)) {
				throw new cwSysMessage("ACL002");
			}
			String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini,
					dob.prof.root_id);
			String userListXml = CurrentActiveUser.getCurActUserXml(con,
					dob.prof, dob.page, dob.page_size, dob.sortCol,
					dob.sortOrder);
			String result = formatXML(userListXml, metaXML, "currentusers",
					dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_CUR_ACT_USER_XML"))
				static_env.outputXML(out, result.toString());
			if (dob.prm_ACTION.equalsIgnoreCase("GET_CUR_ACT_USER"))
				generalAsHtml(result.toString(), out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("GET_POSTER")
				|| dob.prm_ACTION.equalsIgnoreCase("GET_POSTER_XML")) {
			if (AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
					AclFunction.FTN_AMD_POSTER_MAIN)) {
				if (AccessControlWZB.isRoleTcInd(dob.prof.current_role)) {
					if (!ViewTrainingCenter.hasEffTc(con, dob.prof.usr_ent_id)) {
						cwSysMessage e = new cwSysMessage("TC022",
								Long.toString(dob.dbque.res_id));
						msgBox(MSG_ERROR, con, e, dob, out);
						return;
					}
				}
			}
			long tcr_id = dob.prof.root_ent_id;
			long ste_id = dob.prof.root_ent_id;
			// 如果是ln模式，并且用户所在二级培训中心已经跟企业关联
			if (wizbini.cfgSysSetupadv.isTcIndependent()
					&& EnterpriseInfoPortalDao.checkTcrOccupancy(con,
							dob.prof.my_top_tc_id)) {
				ste_id = tcr_id = dob.prof.my_top_tc_id;
			}
			// 二级培训中心没有独立数据时，读取顶层培训中心的宣传栏数据
			if (!dbPoster.checkPosterInd(con, ste_id, tcr_id, dob.rpt_type)) {
				ste_id = tcr_id = dob.prof.root_ent_id;
			}

			String metaXML = dbRegUser.getUserAttributeInfoXML(wizbini,
					dob.prof.root_id);
			String posterXml = dbPoster.getPosterInfoXml(con, ste_id,
					dob.rpt_type, tcr_id);
			String result = formatXML(posterXml, metaXML, "poster_info",
					dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("GET_POSTER_XML"))
				static_env.outputXML(out, result.toString());
			if (dob.prm_ACTION.equalsIgnoreCase("GET_POSTER"))
				generalAsHtml(result.toString(), out, dob);
		} else if (dob.prm_ACTION.equalsIgnoreCase("UPDATE_POSTER")) {
			AccessControlWZB acWzb = new AccessControlWZB();
			// 设置二级培训中心独立后，二级培训中心的培训管理员可以修改该培训中心的宣传栏
			if (!AccessControlWZB.hasRolePrivilege(dob.prof.current_role,
					new String[] { AclFunction.FTN_AMD_MOBILE_POSTER_MAIN,
							AclFunction.FTN_AMD_POSTER_MAIN })
					&& !wizbini.cfgSysSetupadv.isTcIndependent()) {
				throw new cwSysMessage("ACL002");
			}
			dob.poster.sp_ste_id = dob.prof.root_ent_id;
			dob.poster.sp_tcr_id = dob.prof.root_ent_id;
			// 如果是ln模式，并且用户所在二级培训中心已经跟企业关联
			if (wizbini.cfgSysSetupadv.isTcIndependent()
					&& EnterpriseInfoPortalDao.checkTcrOccupancy(con,
							dob.prof.my_top_tc_id)) {
				dob.poster.sp_tcr_id = dob.prof.my_top_tc_id;
				dob.poster.sp_ste_id = dob.poster.sp_tcr_id;
			}
			String saveDirPath = wizbini.getWebDocRoot() + dbUtils.SLASH
					+ dbPoster.SITE_POSTER_PATHNAME + dbUtils.SLASH
					+ dob.prof.root_id;
			// 登录背景图片储存地址
			String saveDirPath_loginBg = wizbini.getWebDocRoot()
					+ dbUtils.SLASH + dbPoster.SITE_POSTER_PATHNAME
					+ dbUtils.SLASH + dbPoster.SITE_POSTER_LOGINBG_PATHNAME
					+ dbUtils.SLASH + dob.prof.root_id;

			// app宣传图片的地址
			String saveDirPath_guide = wizbini.getWebDocRoot() + dbUtils.SLASH
					+ dbPoster.SITE_POSTER_PATHNAME + dbUtils.SLASH
					+ dbPoster.SITE_POSTER_GUIDE_PATHNAME + dbUtils.SLASH
					+ dob.prof.root_id;

			boolean flag = dob.prof.common_role_id.equalsIgnoreCase("ADM")
					|| dob.prof.common_role_id.equalsIgnoreCase("TADM");
			if (!dob.poster.isPosterExist(con, dob.rpt_type)) {
				dob.poster.ins(con, dob.rpt_type, flag);
			} else {
				dob.poster.upd(con, dob.rpt_type, flag);
			}
			if (dob.rpt_type.equalsIgnoreCase("FTN_AMD_POSTER_MAIN") && flag) {
				wizbini.show_all_footer_ind = dob.poster.sp_all_show_footer_ind;
				wizbini.show_login_header_ind = dob.poster.sp_login_show_header_ind;
			}
			if (!dob.poster.sp_keep_media) {
				dbUtils.delFiles(saveDirPath + dbUtils.SLASH
						+ dob.poster.sp_media_file);
			}
			if (!dob.poster.sp_keep_media1) {
				dbUtils.delFiles(saveDirPath + dbUtils.SLASH
						+ dob.poster.sp_media_file1);
			}
			if (!dob.poster.sp_keep_media2) {
				dbUtils.delFiles(saveDirPath + dbUtils.SLASH
						+ dob.poster.sp_media_file2);
			}
			if (!dob.poster.sp_keep_media3) {
				dbUtils.delFiles(saveDirPath + dbUtils.SLASH
						+ dob.poster.sp_media_file3);
			}
			if (!dob.poster.sp_keep_media4) {
				dbUtils.delFiles(saveDirPath + dbUtils.SLASH
						+ dob.poster.sp_media_file4);
			}
			if (!dob.poster.sp_keep_logo_cn) {
				dbUtils.delFiles(saveDirPath + dbUtils.SLASH
						+ dob.poster.sp_logo_file_cn);
			}
			if (!dob.poster.sp_keep_logo_hk) {
				dbUtils.delFiles(saveDirPath + dbUtils.SLASH
						+ dob.poster.sp_logo_file_hk);
			}
			if (!dob.poster.sp_keep_logo_us) {
				dbUtils.delFiles(saveDirPath + dbUtils.SLASH
						+ dob.poster.sp_logo_file_us);
			}
			if (!dob.poster.islogin_bg_file1) {
				dbUtils.delFiles(saveDirPath_loginBg + dbUtils.SLASH
						+ dob.poster.login_bg_file1);
			}
			if (!dob.poster.islogin_bg_file2) {
				dbUtils.delFiles(saveDirPath_loginBg + dbUtils.SLASH
						+ dob.poster.login_bg_file2);
			}
			if (!dob.poster.islogin_bg_file3) {
				dbUtils.delFiles(saveDirPath_loginBg + dbUtils.SLASH
						+ dob.poster.login_bg_file3);
			}
			if (!dob.poster.islogin_bg_file4) {
				dbUtils.delFiles(saveDirPath_loginBg + dbUtils.SLASH
						+ dob.poster.login_bg_file4);
			}
			if (!dob.poster.islogin_bg_file5) {
				dbUtils.delFiles(saveDirPath_loginBg + dbUtils.SLASH
						+ dob.poster.login_bg_file5);
			}
			if (!dob.poster.islogin_bg_video) {
				dbUtils.delFiles(saveDirPath_loginBg + dbUtils.SLASH
						+ dob.poster.login_bg_video);
			}
			if (!dob.poster.isGuide_file1) {
				dbUtils.delFiles(saveDirPath_guide + dbUtils.SLASH
						+ dob.poster.login_bg_file1);
			}
			if (!dob.poster.isGuide_file1) {
				dbUtils.delFiles(saveDirPath_guide + dbUtils.SLASH
						+ dob.poster.login_bg_file2);
			}
			if (!dob.poster.isGuide_file1) {
				dbUtils.delFiles(saveDirPath_guide + dbUtils.SLASH
						+ dob.poster.login_bg_file3);
			}
			dbUtils.moveDir(dob.tmpUploadDir, saveDirPath);
			dbUtils.moveFile(saveDirPath + dbUtils.SLASH
					+ dob.poster.login_bg_file1, saveDirPath_loginBg
					+ dbUtils.SLASH + dob.poster.login_bg_file1);
			dbUtils.moveFile(saveDirPath + dbUtils.SLASH
					+ dob.poster.login_bg_file2, saveDirPath_loginBg
					+ dbUtils.SLASH + dob.poster.login_bg_file2);
			dbUtils.moveFile(saveDirPath + dbUtils.SLASH
					+ dob.poster.login_bg_file3, saveDirPath_loginBg
					+ dbUtils.SLASH + dob.poster.login_bg_file3);
			dbUtils.moveFile(saveDirPath + dbUtils.SLASH
					+ dob.poster.login_bg_file4, saveDirPath_loginBg
					+ dbUtils.SLASH + dob.poster.login_bg_file4);
			dbUtils.moveFile(saveDirPath + dbUtils.SLASH
					+ dob.poster.login_bg_file5, saveDirPath_loginBg
					+ dbUtils.SLASH + dob.poster.login_bg_file5);
			dbUtils.moveFile(saveDirPath + dbUtils.SLASH
					+ dob.poster.login_bg_video, saveDirPath_loginBg
					+ dbUtils.SLASH + dob.poster.login_bg_video);
			dbUtils.moveFile(saveDirPath + dbUtils.SLASH
					+ dob.poster.guide_file1, saveDirPath_guide + dbUtils.SLASH
					+ dob.poster.guide_file1);
			dbUtils.moveFile(saveDirPath + dbUtils.SLASH
					+ dob.poster.guide_file2, saveDirPath_guide + dbUtils.SLASH
					+ dob.poster.guide_file2);
			dbUtils.moveFile(saveDirPath + dbUtils.SLASH
					+ dob.poster.guide_file3, saveDirPath_guide + dbUtils.SLASH
					+ dob.poster.guide_file3);
			con.commit();
			msgBox(MSG_STATUS, con, new cwSysMessage("GEN003"), dob, out);
		} else if (dob.prm_ACTION.equalsIgnoreCase("user_upd_batch_prep")
				|| dob.prm_ACTION.equalsIgnoreCase("user_upd_batch_prep_XML")) {
			try {
				String xml = "<user/>";
				if (dob.dbusg.usg_ent_id != 0) {
					AcUserGroup ug = new AcUserGroup(con);
					if (!ug.canManageGroup(dob.prof, dob.dbusg.usg_ent_id,
							wizbini.cfgTcEnabled)) {
						throw new qdbErrMessage("USG002");
					}

					if (!dbUserGroup.isUsgExists(con, dob.dbusg.usg_ent_id)) {
						throw new qdbErrMessage("USG011");
					}
					dob.dbusg.get(con);
					xml += "<group id=\" "
							+ dob.dbusg.usg_ent_id
							+ "\" title=\" "
							+ cwUtils.esc4XML(dob.dbusg.usg_display_bil)
							+ "\" parent_id=\""
							+ dbEntityRelation.getParentUserGroup(con,
									dob.dbusg.usg_ent_id).usg_ent_id + "\"/>";
					xml += DbUserGrade.getDefaultGradeAsXML(con,
							dob.prof.root_ent_id);
				}

				String result = formatXML(xml, "", "user_manager", dob.prof);

				if (dob.prm_ACTION.equalsIgnoreCase("user_upd_batch_prep_XML"))
					static_env.outputXML(out, result);
				if (dob.prm_ACTION.equalsIgnoreCase("user_upd_batch_prep"))
					generalAsHtml(result, out, dob);
			} catch (qdbErrMessage e) {
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("user_upd_batch_exec")) {
			try {
				AcRegUser acusr = new AcRegUser(con);
				dob.getUsr().usg_ent_id_lst = dob.usg_ent_id_lst;
				String result = dob.getUsr().updUserBatch(con, dob.prof,
						dob.vColName, dob.vColType, dob.vColValue,
						dob.vClobColName, dob.vClobColValue, dob.vExtColName,
						dob.vExtColType, dob.vExtColValue, dob.vExtClobColName,
						dob.vExtClobColValue, wizbini.cfgTcEnabled);

				dbRegUser user = new dbRegUser();
				for (int i = 0; i < dob.usg_ent_id_lst.length; i++) {
					user.usr_ent_id = dob.usg_ent_id_lst[i];
					user.getByEntId(con);
					ObjectActionLog log = new ObjectActionLog(user.usr_ent_id,
							user.usr_ste_usr_id, user.usr_display_bil,
							ObjectActionLog.OBJECT_TYPE_USR,
							ObjectActionLog.OBJECT_ACTION_UPD,
							ObjectActionLog.OBJECT_ACTION_TYPE_BATCH,
							dob.prof.getUsr_ent_id(),
							dob.prof.getUsr_last_login_date(), dob.prof.getIp());
					SystemLogContext.saveLog(log,
							SystemLogTypeEnum.OBJECT_ACTION_LOG);
				}
				if (dob.bFileUpload) {
					this.procUploadUsrFace(con, dob.tmpUploadDir,
							dob.getUsr().usr_ent_id, dob.remain_photo_ind);
				}
				if (result != null) {
					con.rollback();
					Vector resultVec = new Vector();
					resultVec.addElement(LangLabel.getValue(dob.prof.label_lan,
							AcObjective.LABEL_ROL_TADM));
					resultVec.addElement(result);
					cwSysMessage e = new cwSysMessage("USR030", resultVec);
					msgBox(MSG_ERROR, con, e, dob, out);
				} else {
					// update RegUserExtension
					// dob.usr.updExtension(con,dob.usr.usr_ent_id);
					con.commit();
					cwSysMessage e = new cwSysMessage("USR009");
					msgBox(MSG_STATUS, con, e, dob, out);
				}

			} catch (qdbErrMessage e) {
				con.rollback();
				msgBox(MSG_ERROR, con, e, dob, out);
				return;
			}
		} else if (dob.prm_ACTION.equalsIgnoreCase("itm_evaluation_report")
				|| dob.prm_ACTION.equalsIgnoreCase("itm_evaluation_report_xml")) {

			String result = dbModuleEvaluation.getItmEvaluationReportList(con,
					dob.dbmod.mod_res_id, dob.dbmod.res_type, dob.pagesize,
					dob.cur_page, dob.order_by, dob.sort_order);
			long itm_id = dbCourse.getCosItemId(con, dob.dbres.res_id);
			result += aeItem.genItemActionNavXML(con, itm_id, dob.prof);

			aeItem itm = new aeItem();
			itm.itm_id = itm_id;
			itm.getItem(con);
			result += itm.contentInfoAsXML(con);

			result = formatXML(result, "", "applyeasy", dob.prof);

			if (dob.prm_ACTION.equalsIgnoreCase("itm_evaluation_report_xml")) {
				static_env.outputXML(out, result);
			} else if (dob.prm_ACTION.equalsIgnoreCase("itm_evaluation_report")) {
				generalAsHtml(result, out, dob);
			}
		}
		/*
		 * else if(dob.prm_ACTION.equalsIgnoreCase("gen_tree0")) {
		 * out.println("<html>"); out.println("<head>");
		 * out.println("<TITLE>wizBank 3.1</TITLE>"); out.println(
		 * "<script src=\"../js/wb_course.js\" language=\"JavaScript\"></script><script src=\"../js/wb_utils.js\" language=\"JavaScript\"></script><script src=\"../js/ae_utils.js\" language=\"JavaScript\"></script><script src=\"../js/gen_utils.js\" language=\"JavaScript\"></script><script src=\"../js/wb_announcement.js\" language=\"JavaScript\"></script><script src=\"../js/ae_application.js\" language=\"JavaScript\"></script><script src=\"../js/ae_cata_lst.js\" language=\"JavaScript\"></script><SCRIPT TYPE=\"text/javascript\" LANGUAGE=\"JavaScript\">"
		 * ); out.println("</SCRIPT>"); out.println(
		 * "<meta content=\"text/html; charset=Big5\" http-equiv=\"Content-Type\">"
		 * ); out.println(
		 * "<link rel=\"stylesheet\" type=\"text/css\" href=\"../skin/cw/css/en_ist_gen_cw.css\">"
		 * ); out.println(
		 * "<link rel=\"stylesheet\" type=\"text/css\" href=\"../skin/cw/css/en_ist_course_cw.css\">"
		 * ); out.println(
		 * "<link rel=\"stylesheet\" type=\"text/css\" href=\"../skin/cw/css/en_ist_lstview_cw.css\">"
		 * ); out.println(
		 * "<link rel=\"stylesheet\" type=\"text/css\" href=\"../skin/cw/css/en_ist_home_cw.css\">"
		 * ); out.println("</head>"); out.println("<body>");
		 * out.println("<table><tr>"); out.println(
		 * "<td class=\"aeParInfTitleBg\" valign=\"top\" align=\"right\" width=\"25%\"><span class=\"aeParInfTitleText\"></span></td>"
		 * ); out.println(
		 * "<td valign=\"top\" class=\"aeParInfContentBg\" align=\"left\" width=\"75%\">"
		 * ); out.println("Category<br>"); out.println(
		 * "<select name=\"select4\" size=\"3\" class=\"wbGenSelFrmIE\" style=\"width:300\" multiple>"
		 * ); out.println("<option>AD1</option></select><br>"); out.println(
		 * "<span class=\"aeParInfcontentText\">[<a href=\"../servlet/qdb.qdbAction?cmd=gen_tree&tree_type=user_group_and_user&pick_method=4&stylesheet=gen_tree.xsl\" target=\"_blank\">Add</a>] [<a href=\"#\">Remove</a>]</span></td>"
		 * ); out.println("</tr>"); out.println("</body></html>"); }
		 * 
		 * else if (dob.prm_ACTION.equalsIgnoreCase("SEARCH_USER")) {
		 * out.println("<H1>Search User</H1>");
		 * out.println("<FORM METHOD=\"GET\" ACTION=\"../servlet/qdb.qdbAction\">"
		 * );
		 * out.println("<INPUT TYPE=\"HIDDEN\" NAME=\"env\" VALUE=\"wizb\">");
		 * out
		 * .println("<INPUT TYPE=\"HIDDEN\" NAME=\"cmd\" VALUE=\"search_ent_lst\">"
		 * ); out.println(
		 * "<INPUT TYPE=\"HIDDEN\" NAME=\"stylesheet\" VALUE=\"usr_search.xsl\">"
		 * ); out.println(
		 * "Ent ID: <INPUT TYPE=\"TEXT\" NAME=\"ent_id\" VALUE=\"\"><BR>");
		 * out.println
		 * ("User ID: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_id\" VALUE=\"\"><BR>");
		 * out.println(
		 * "User Email: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_email\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "User Last Name: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_last_name_bil\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "User First Name: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_first_name_bil\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "User Display Name: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_display_bil\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "User Gender: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_gender\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "User BDay From: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_bday_fr\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "User BDay To: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_bday_to\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "User HKID: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_hkid\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "User Tel: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_tel\" VALUE=\"\"><BR>");
		 * out.println(
		 * "User Fax: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_fax\" VALUE=\"\"><BR>");
		 * out.println(
		 * "User Address: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_address_bil\" NAME=\"\"><BR>"
		 * ); out.println(
		 * "User Postal Code: <INPUT TYPE=\"TEXT\" NAME=\"s_usr_postal_code_bil\" NAME=\"\"><BR>"
		 * ); out.println(
		 * "User Role Type: <INPUT TYPE=\"TEXT\" NAME=\"s_role_type\" NAME=\"\"> EADM | NADM | ECDN | NCDN | NIST | NLRN<BR>"
		 * );
		 * out.println("Page: <INPUT TYPE=\"TEXT\" NAME=\"page\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "Search Time: <INPUT TYPE=\"TEXT\" NAME=\"s_timestamp\" VALUE=\"\"><BR>"
		 * ); out.println(
		 * "Sort By: <INPUT TYPE=\"TEXT\" NAME=\"s_sort_by\" VALUE=\"\"> usr_display_bil | usg_display_bil<BR>"
		 * ); out.println(
		 * "Order By: <INPUT TYPE=\"TEXT\" NAME=\"s_order_by\" VALUE=\"\"> ASC | DESC<BR>"
		 * ); out.println("<INPUT TYPE=\"SUBMIT\">"); }
		 */

		else {
			static_env.dispDebugMesg(out, "No recognizable command specified: "
					+ cwUtils.esc4Html(dob.prm_ACTION));
		}
		return;
	}

	// Get Servlet information

	public String getServletInfo() {
		return "qdb.qdbAction Information";
	}

	private boolean validateInput(dataObj dob) {
		return true;
	}

	private void delUploadedFiles(long resId) throws cwException {
		String saveDirPath = wizbini.getFileUploadResDirAbs() + dbUtils.SLASH
				+ resId;
		dbUtils.delDir(saveDirPath);
	}

	private void procUploadUsrFace(Connection con, String tempSaveDirPath,
			long usr_ent_id, boolean remain_photo_ind, HttpSession sess)
			throws qdbException, SQLException, cwException, IOException {
		if (!remain_photo_ind) {
			String savaDirPath = wizbini.getFileUploadUsrDirAbs()
					+ dbUtils.SLASH + usr_ent_id;
			dbUtils.delFiles(savaDirPath);
			dbUtils.moveDir(tempSaveDirPath, savaDirPath);
			File Dir = new File(tempSaveDirPath);
			File[] files = Dir.listFiles();
			String filename = null;
			/*
			 * if(files != null && files.length>0) {
			 * filename=files[0].getName(); }
			 */
			Vector vec = (Vector) sess.getAttribute("NEW_FILENAME");
			if (vec != null && vec.size() > 0) {
				filename = (String) vec.get(0);
				sess.removeAttribute("NEW_FILENAME");
				sess.removeAttribute("ORIGINAL_FILENAME");
			}
			String saveFile = savaDirPath + dbUtils.SLASH + filename;
			cwUtils.resizeImage(saveFile, wizbini.getUsrImageWidth(),
					wizbini.getUsrImageHeight());
			String sql = "update regUserExtension set urx_extra_43= ? where urx_usr_ent_id= ? ";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, filename);
			stmt.setLong(2, usr_ent_id);
			stmt.execute();
			cwSQL.cleanUp(null, stmt);
		}
	}

	private void procUploadedFiles(Connection con, HttpServletRequest request,
			long resId, String tmpSaveDirPath, dataObj dob)
			throws UploadedFileException, IOException, cwException,
			qdbException, cwSysMessage {
		String saveDirPath = wizbini.getFileUploadResDirAbs() + dbUtils.SLASH
				+ resId;
		try {
			// session for stroe and get the converted filename
			HttpSession sess = request.getSession(true);

			// If the question is Multiple Choice or Matching,
			// copy the all the pre-uploaded files to the temporary directory
			String int_type = "";
			dbQuestion dbque = dob.dbque;
			if (dbque != null && dob.dbque.que_type != null) {
				int_type = dob.dbque.que_type;
			}

			if (int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MATCHING)
					|| int_type.equalsIgnoreCase(dbQuestion.QUE_TYPE_MULTI)) {
				dbUtils.moveDir(dob.session_upload_dir, tmpSaveDirPath);
			}

			// StringBuffer SQL = new StringBuffer();
			// SQL.append(" SELECT res_src_link LINK, res_src_type TYPE FROM ");
			// SQL.append(" Resources WHERE res_id = ? ");

			PreparedStatement stmt = con
					.prepareStatement(SQL_IN_procUploadedFiles);
			stmt.setLong(1, resId);
			ResultSet rs = stmt.executeQuery();

			String src_link = null;
			String src_type = null;
			String img_link = null;
			Vector self_lst = new Vector();
			if (rs.next()) {
				src_link = rs.getString("LINK");
				src_type = rs.getString("TYPE");
				img_link = rs.getString("IMG_LINK");
				if (src_link != null && src_link.length() > 0) {
					self_lst.add(src_link.trim());
				}
				if (img_link != null && img_link.length() > 0) {
					self_lst.add(img_link.trim());
				}
			}
			stmt.close();

			if (src_type != null) {
				// if(dob.uploaded_filename != null &&
				// dob.uploaded_filename.length() > 0)
				if (dob.bFileUploaded) {
					// dbUtils.delDir(saveDirPath);
					if (src_type.equalsIgnoreCase("WIZPACK")) {
						// unzip wizpack
						if (sess != null
								&& sess.getAttribute(ORIGINAL_FILENAME) != null)
							src_link = convertFilename(src_link, sess);
						dbUtils.unzip(
								tmpSaveDirPath + dbUtils.SLASH + src_link,
								saveDirPath);
					} else if (src_type.equalsIgnoreCase("ZIPFILE")) {
						// unzip file
						if (sess != null
								&& sess.getAttribute(ORIGINAL_FILENAME) != null)
							dob.uploaded_filename = convertFilename(
									dob.uploaded_filename, sess);
						dbUtils.delOtherFiles(saveDirPath, self_lst);
						dbUtils.unzip(tmpSaveDirPath + dbUtils.SLASH
								+ dob.uploaded_filename, saveDirPath);
					} else if (src_type.equalsIgnoreCase("FILE")) {
						// Copy all the files to the permanet directory
						// here we cannot use moveDir as the same file has to be
						// copied to class module in common content
						dbUtils.delOtherFiles(saveDirPath, self_lst);
						dbUtils.copyDir(tmpSaveDirPath, saveDirPath);
						String ext_str = "xlsx[|]xls[|]doc[|]docx[|]pptx[|]ppt";
						String file_ext = src_link != null && src_link != "" ? src_link
								.substring(src_link.indexOf(".") + 1,
										src_link.length()) : "";
						// 使用在线预览方式为：I DOC View 不再需要装换成pdf格式。
						/*
						 * if(Application.OPENOFFICE_ENABLED &&
						 * ext_str.contains(file_ext)){ try { PdfConverter
						 * pdfConverter = new PdfConverter(saveDirPath + "/" +
						 * src_link); pdfConverter.pdf2jsonPath =
						 * request.getSession
						 * ().getServletContext().getRealPath("/") +
						 * "pdf2json/pdf2json.exe";
						 * pdfConverter.file2pdf(wizbini.openOfficeConnection,
						 * Application.OPENOFFICE_HOST,
						 * Application.OPENOFFICE_PORT, true); dbResource
						 * resource = new dbResource();
						 * resource.updResSrcLink(con, resId,
						 * src_link.substring(0, src_link.indexOf(".")) +
						 * ".pdf"); } catch (Exception e) { // TODO
						 * Auto-generated catch block e.printStackTrace(); } }
						 * else if (file_ext.equalsIgnoreCase("pdf")) {
						 * PdfConverter pdfConverter = new
						 * PdfConverter(saveDirPath + "/" + src_link);
						 * pdfConverter.pdf2jsonPath =
						 * request.getSession().getServletContext
						 * ().getRealPath("/") + "pdf2json/pdf2json.exe";
						 * pdfConverter.pdf2json(); }
						 */
					} else if (src_type.equalsIgnoreCase("AICC_FILES")
							|| src_type.equalsIgnoreCase("SCORM_FILES")
							|| src_type.equalsIgnoreCase("NETGCOK_FILES")) {
						// Copy all the files to the permanet directory
						// here we cannot use moveDir as the same file has to be
						// copied to class module in common content
						dbUtils.delOtherFiles(saveDirPath, self_lst);
						dbUtils.copyDir(tmpSaveDirPath, saveDirPath);
					} else if (src_type.equalsIgnoreCase("URL")) {
						if (img_link != null) {
							dbUtils.copyDir(tmpSaveDirPath, saveDirPath);
						}
					}
				} else if (src_type.equalsIgnoreCase("URL")) {
					dbUtils.delOtherFiles(saveDirPath, self_lst);
					if (img_link != null) {
						dbUtils.copyDir(tmpSaveDirPath, saveDirPath);
					}
				}
			} else {
				if (dob.bFileUploaded
						|| dob.del_que_media
						|| (dob.dbass.mod_instruct != null && dob.dbass.mod_instruct
								.length() > 0)) {
					if (!int_type
							.equalsIgnoreCase(dbQuestion.QUE_TYPE_MATCHING)) {
						dbUtils.delOtherFiles(saveDirPath, self_lst);
					}
				}
				dbUtils.moveDir(tmpSaveDirPath, saveDirPath);
			}

			// if copy media from
			if (dob.copy_media_from != null && dob.copy_media_from.length() > 0) {
				String fromDirPath = wizbini.getFileUploadResDirAbs()
						+ dbUtils.SLASH + dob.copy_media_from;
				dbUtils.copyDir(fromDirPath, saveDirPath);
			}

			// WaiLun : Clear the cookie contains the uploaded filename
			if (sess.getAttribute(ORIGINAL_FILENAME) != null) {
				sess.setAttribute(ORIGINAL_FILENAME, new Vector());
				sess.setAttribute(NEW_FILENAME, new Vector());
				// sess.setAttribute(RENAME,new Vector());
			}

		} catch (qdbException e) {
			throw new cwException(e.getMessage());
		} catch (SQLException e) {
			throw new qdbException("SQL Error: " + e.getMessage());
		}
	}

	public dataObj parseParam(HttpServletRequest request)
			throws UploadedFileException, ParameterNotFoundException,
			NumberFormatException, IOException, UnsupportedEncodingException,
			cwException, cwSysMessage, qdbErrMessage {

		// If Websphere , get the client character encoding
		// Otherwise, use the default encoding
		// The client encoding is used in the converting unicode function
		String clientEnc = new String();
		ServletContext sc = getServletContext();
		if (sc.getServerInfo().toLowerCase()
				.indexOf(cwUtils.APP_SERVER_WEBSPHERE) > 0) {
			clientEnc = request.getCharacterEncoding();
		}
		if (clientEnc == null || clientEnc.length() == 0) {
			clientEnc = DEFAULT_ENC;
		}

		Enumeration keys;
		MultipartRequest multi = null;

		String key, val;

		dataObj dob = new dataObj();
		dob.client_enc = clientEnc;

		String prmNm;

		// static_env = static_env;

		if (wizbini.cfgSysSetupadv.getEncoding() != null
				&& wizbini.cfgSysSetupadv.getEncoding().length() > 0)
			dob.encoding = wizbini.cfgSysSetupadv.getEncoding();
		else
			dob.encoding = DEFAULT_ENC;

		// handle both content types
		boolean bMultiPart = false;
		String conType = request.getContentType();

		if (conType != null
				&& conType.toLowerCase().startsWith("multipart/form-data")) {
			bMultiPart = true;
		}

		// try to use utility class
		if (bMultiPart) {
			CommonLog.debug("df");

			dob.bFileUpload = true;
			java.util.Date ts = new java.util.Date();
			SimpleDateFormat fm = new SimpleDateFormat("SSSHHmmss");
			// dob.tmpUploadDir = "c:\\temp\\qdbData\\tmp\\" + ts.getYear() +
			// ts.getMonth() + ts.getDay() + ts.getHours() + ts.getMinutes() +
			// ts.getSeconds();

			dob.tmpUploadDir = wizbini.getFileUploadTmpDirAbs() + dbUtils.SLASH
					+ fm.format(ts);

			File tmpUploadDir = new File(dob.tmpUploadDir);
			boolean bOk = tmpUploadDir.mkdirs();
			if (!bOk)
				throw new UploadedFileException("Fails to create temp dir: "
						+ dob.tmpUploadDir);

			// add buffer for other page content
			int maxSize = (int) ((wizbini.cfgSysSetupadv.getFileUpload()
					.getMaxUploadSize() + 0.1) * 1024 * 1024);
			if (request.getContentLength() > maxSize) {
				request.getInputStream().skip(request.getContentLength());
				request.getInputStream().close();
				throw new cwSysMessage("GEN007", new Integer(
						static_env.INI_MAX_UPLOAD_SIZE).toString());
			} else {
				multi = new MultipartRequest(request, dob.tmpUploadDir,
						dob.encoding, maxSize);
				if (null != multi.getForBiddenFiles()
						&& !multi.getForBiddenFiles().isEmpty()) {
					throw new cwSysMessage("GEN011",
							Application.UPLOAD_FORBIDDEN);
				}
			}

			keys = multi.getParameterNames();
			// set flag on

			// check file uploaded or not....
			Enumeration files = multi.getFileNames();
			while (files.hasMoreElements()) {
				String name = (String) files.nextElement();
				String fileName = multi.getFilesystemName(name);
				if (fileName != null && fileName.length() > 0) {
					File fileUploaded = new File(dob.tmpUploadDir, fileName);
					FileInputStream fis = new FileInputStream(fileUploaded);
					if (fis.read() != -1) {
						dob.bFileUploaded = true;
						dob.h_file_filename.put(name, fileName);
					}
					fis.close();
				}
			}

		} else {
			keys = request.getParameterNames();
		}

		String tmpIntFldNm;
		int tmpIntCount;
		HttpSession sess = request.getSession(true); // WaiLun for filename
														// exchange
		// get the keys in the following sequence general arguments: env, cmd,
		// mode

		prmNm = "cmd";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.prm_ACTION = val;
			
		prmNm = "folders";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val == null)
			dob.viewRes = this.VIEW_RES;
		else
			dob.viewRes = val;

		prmNm = "password";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.password = val;

		prmNm = "ldap_auth";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("TRUE")) {
				dob.ldap_auth = true;
			} else {
				dob.ldap_auth = false;
			}
		} else {
			dob.ldap_auth = false;
		}

		prmNm = "create_new";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("TRUE")) {
				dob.create_new = true;
			} else {
				dob.create_new = false;
			}
		} else {
			dob.create_new = false;
		}

		prmNm = "mode";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.mode = val;
		}

		// for AICC import
		// by cliff, 2001/4/18
		prmNm = "aicc_crs_filename";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.aicc_crs_filename = val;
		else
			dob.aicc_crs_filename = null;

		prmNm = "aicc_cst_filename";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.aicc_cst_filename = val;
		else
			dob.aicc_cst_filename = null;

		prmNm = "aicc_des_filename";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.aicc_des_filename = val;
		else
			dob.aicc_des_filename = null;

		prmNm = "aicc_au_filename";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.aicc_au_filename = val;
		else
			dob.aicc_au_filename = null;

		prmNm = "aicc_ort_filename";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.aicc_ort_filename = val;
		else
			dob.aicc_ort_filename = null;

		// for NETG CDF file
		prmNm = "netg_cdf_filename";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.netg_cdf_filename = val;
		else
			dob.netg_cdf_filename = null;

		// Imsmanifest file name for SCORM 1.2
		prmNm = "imsmanifest_file_name";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			dob.imsmanifestFileName = val;
		} else {
			dob.imsmanifestFileName = null;
		}

		prmNm = "cos_url_prefix";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			dob.cosUrlPrefix = val;
		} else {
			dob.cosUrlPrefix = null;
		}

		// User / Usergroup Integration with other system
		prmNm = "action";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.prm_ACTION = val;

		prmNm = "site_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			try {
				dob.site_id = Long.parseLong(val);
			} catch (NumberFormatException e) {
				dob.site_id = 0;
			}
		}

		prmNm = "usr_competency";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_ske_id = Long.parseLong(val);
		}

		prmNm = "label_lan";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.label_lan = val;
		else {
			if (wizbini.cfgSysSkinList.getDefaultLang() != null
					&& wizbini.cfgSysSkinList.getDefaultLang().length() > 0) {
				dob.label_lan = cwUtils.langToLabel(wizbini.cfgSysSkinList
						.getDefaultLang());
			} else {
				dob.label_lan = DEFAULT_ENC;
			}
		}

		prmNm = "style";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.style = val;

		prmNm = "copy_media_from";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.copy_media_from = val;

		prmNm = "url_target";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.url_target = val;

		prmNm = "url_success";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.url_success = cwUtils.getUrlByisPhishing(cwUtils.esc4JS(val,
					true));

		prmNm = "url_failure";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.setUrl_fail(cwUtils.getUrlByisPhishing(cwUtils
					.esc4JS(val, true)));

		prmNm = "url_failure1";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.url_fail1 = cwUtils.getUrlByisPhishing(cwUtils
					.esc4JS(val, true));

		prmNm = "stylesheet";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && (val.indexOf("WEB-INF/") < 0)) {
			dob.cur_stylesheet = val;
		}
		if (this.XSL_DISPLAY)
			CommonLog.info("current stylesheet:\t\t" + dob.cur_stylesheet);

		prmNm = "force_reload_xsl";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.force_reload_xsl = Boolean.valueOf(val).booleanValue();

		prmNm = "cur_page";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.cur_page = Integer.parseInt(val);
		else
			dob.cur_page = DEFAULT_PAGE;

		prmNm = "pagesize";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.pagesize = Integer.parseInt(val);
		else
			dob.pagesize = DEFAULT_PAGESIZE;

		prmNm = "order_by";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.order_by = val;

		prmNm = "sort_by";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.sort_by = val;
		else
			dob.sort_by = DEFAULT_SORT_BY;

		prmNm = "timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.pagetime = Timestamp.valueOf(val);

		prmNm = "begin_num";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.begin_num = Long.parseLong(val);

		prmNm = "end_num";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.end_num = Long.parseLong(val);

		prmNm = "code";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.key = val;

		prmNm = "upd_tst_que";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.upd_tst_que = Boolean.valueOf(val.trim()).booleanValue();
		}
		prmNm = "show_all";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.show_all = Boolean.valueOf(val.trim()).booleanValue();
		}

		prmNm = "is_upd";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.is_upd = Boolean.valueOf(val.trim()).booleanValue();
		}

		prmNm = "isStudyGroupMod";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.isStudyGroupMod = Boolean.valueOf(val.trim()).booleanValue();
		}
		prmNm = "sgp_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.sgp_id = Long.parseLong(val);
		}

		// System setting
		prmNm = "threshold_block";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.threshold_block = val.trim();
		}
		prmNm = "threshold_warn";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.threshold_warn = val.trim();
		}
		prmNm = "support_email";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.support_email = val.trim();
		}
		prmNm = "multiple_login_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.multiple_login_ind = val.trim();
		}
		// Resource
		prmNm = "res_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbres.res_id = Long.parseLong(val);
			dob.dbmod.res_id = dob.dbres.res_id;
			dob.dbmod.mod_res_id = dob.dbres.res_id;
			dob.dbcos.cos_res_id = dob.dbres.res_id;
			dob.res_id_lst = sutils.split(val, "~");
		}

		prmNm = "res_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.res_id_lst = sutils.split(val, "~");

		prmNm = "res_timestamp_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.res_timestamp_lst = dbUtils.string2timestamp(sutils.split(val,
					"~"));
		}

		prmNm = "res_lan";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbres.res_lan = val;
			dob.dbque.res_lan = val;
			dob.dbcos.res_lan = val;
			dob.dbmod.res_lan = val;
		}

		prmNm = "res_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.types = sutils.split(val, "~");
			dob.dbres.res_type = dob.types[0];
			dob.dbque.res_type = dob.types[0];
		}

		prmNm = "res_subtype";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.subtypes = sutils.split(val, "~");
			dob.dbres.res_subtype = dob.subtypes[0];
		}
		
		prmNm = "tc_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if(val != null && val.length() != 0){
			dob.tcId = val;
		}

		prmNm = "groupType";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if(val != null && val.length() != 0){
			dob.group_cmd = val;
		}
		
		// Course Builder
		if ((dob.prm_ACTION.toUpperCase().indexOf("MOD_BUILDER")) >= 0) {
			prmNm = "mod_type_lst";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.types = sutils.split(val, "~");
			}

			prmNm = "lesson_count";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.lesson_count = Integer.parseInt(val);
			}

			prmNm = "lesson_current";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.lesson_current = Integer.parseInt(val);
			}

			prmNm = "mod_count";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.module_count = Integer.parseInt(val);
			}

			Vector typeVec = new Vector();
			Vector titleVec = new Vector();
			Vector descVec = new Vector();
			Vector startVec = new Vector();
			Vector endVec = new Vector();

			for (int i = 1; i <= dob.module_count; i++) {
				prmNm = "mod_type_" + i;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0) {
					typeVec.addElement(new String(val));
				}

				prmNm = "mod_title_" + i;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0) {
					String title_ = dbUtils.unicodeFrom(val, clientEnc,
							dob.encoding, bMultiPart);
					titleVec.addElement(new String(title_));
				} else {
					titleVec.addElement(new String());
				}

				prmNm = "mod_desc_" + i;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0) {
					String desc_ = dbUtils.unicodeFrom(val, clientEnc,
							dob.encoding, bMultiPart);
					descVec.addElement(new String(desc_));
				} else {
					descVec.addElement(new String());
				}

				prmNm = "mod_start_" + i;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0) {
					Timestamp start_ = new Timestamp(0);
					if (val.equalsIgnoreCase(dbUtils.IMMEDIATE))
						start_ = Timestamp.valueOf(dbUtils.MIN_TIMESTAMP);
					else
						start_ = Timestamp.valueOf(val);

					startVec.addElement(start_);
				} else {
					startVec.addElement(new Timestamp(0));
				}

				prmNm = "mod_end_" + i;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0) {
					Timestamp end_ = new Timestamp(0);
					if (val.equalsIgnoreCase(dbUtils.UNLIMITED))
						end_ = Timestamp.valueOf(dbUtils.MAX_TIMESTAMP);
					else
						end_ = Timestamp.valueOf(val);

					endVec.addElement(end_);
				} else {
					endVec.addElement(new Timestamp(0));
				}
			}

			dob.wizard_data.put("TYPE", typeVec);
			dob.wizard_data.put("TITLE", titleVec);
			dob.wizard_data.put("DESC", descVec);
			dob.wizard_data.put("START", startVec);
			dob.wizard_data.put("END", endVec);

		}

		// Dennis, store the resource content subtype that front end wants
		prmNm = "res_cnt_subtype";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_cnt_subtype = val;

		prmNm = "res_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_title = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "res_desc";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_desc = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "res_instructor_name";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbres.res_instructor_name = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
			dob.dbmod.res_instructor_name = dob.dbres.res_instructor_name;
			dob.dbcos.res_instructor_name = dob.dbres.res_instructor_name;
		}

		prmNm = "res_instructor_organization";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbres.res_instructor_organization = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
			dob.dbmod.res_instructor_organization = dob.dbres.res_instructor_organization;
			dob.dbcos.res_instructor_organization = dob.dbres.res_instructor_organization;
		}

		boolean annotation_html = false;
		String annotation = null;

		prmNm = "annotation_html";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("Y"))
				annotation_html = true;
		}

		prmNm = "res_annotation";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			annotation = val;
		}

		prmNm = "mod_annotation";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			annotation = val;
		}

		if (annotation != null && annotation.length() != 0) {
			annotation = dbUtils.unicodeFrom(annotation, clientEnc,
					dob.encoding, bMultiPart);
			annotation = dbUtils.esc4XML(annotation);
			if (annotation_html)
				annotation = "<html>" + annotation + "</html>";

			dob.dbres.res_annotation = annotation;
			dob.dbmod.res_annotation = annotation;
		}

		prmNm = "res_format";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_format = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "res_difficulty";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_difficulty = Integer.parseInt(val);

		prmNm = "res_duration";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_duration = Float.valueOf(val).floatValue();

		prmNm = "res_privilege";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_privilege = val;

		prmNm = "res_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_status = val;

		prmNm = "res_src_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_src_type = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		// WaiLun : get the name of the uploaded file from resource
		if (dob.dbres.res_src_type != null
				&& (dob.dbres.res_src_type).equalsIgnoreCase("FILE"))
			if (bMultiPart && dob.bFileUploaded
					&& multi.getFilesystemName("upload_file") != null)
				dob.dbres.res_src_link = multi.getFilesystemName("upload_file");
			else {
				prmNm = "res_src_link";
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0)
					dob.dbres.res_src_link = dbUtils.unicodeFrom(val,
							clientEnc, dob.encoding, bMultiPart);
			}

		if (dob.dbres.res_src_type != null
				&& (dob.dbres.res_src_type).equalsIgnoreCase("WIZPACK"))
			if (bMultiPart && dob.bFileUploaded
					&& multi.getFilesystemName("upload_wizpack") != null)
				dob.dbres.res_src_link = multi
						.getFilesystemName("upload_wizpack");
			else {
				prmNm = "res_src_link";
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0)
					dob.dbres.res_src_link = dbUtils.unicodeFrom(val,
							clientEnc, dob.encoding, bMultiPart);
			}
		if (dob.dbres.res_src_type != null
				&& ((dob.dbres.res_src_type).equalsIgnoreCase("URL") || (dob.dbres.res_src_type)
						.equalsIgnoreCase("AICC_FILES"))) {
			prmNm = "res_src_link";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.dbres.res_src_link = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);
			}
		}

		if (dob.dbres.res_src_type != null
				&& (dob.dbres.res_src_type).equalsIgnoreCase("ZIPFILE")) {
			if (bMultiPart && dob.bFileUploaded
					&& multi.getFilesystemName("upload_zipfile") != null)
				dob.uploaded_filename = multi
						.getFilesystemName("upload_zipfile");

			prmNm = "res_src_link";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.dbres.res_src_link = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);
			}
		}

		prmNm = "res_filename";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbres.res_src_link = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (bMultiPart && multi.getFilesystemName("file") != null)
			dob.uploaded_filename = multi.getFilesystemName("file");

		prmNm = "res_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_upd_date = Timestamp.valueOf(val);

		// For Calendar
		prmNm = "owner_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.res_usr_id_owner = val;

		prmNm = "instructor_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbres.mod_usr_id_instructor = val;

		// For upload Zip filename
		/*
		 * prmNm = "zip_filename"; val = (bMultiPart) ?
		 * multi.getParameter(prmNm) : request.getParameter(prmNm); if ( val!=
		 * null && val.length()!= 0 ) dob.uploaded_filename =
		 * dbUtils.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart);
		 */
		prmNm = "location";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbres.location = val;
		}

		prmNm = "ent_ids";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.ent_ids = val;

		prmNm = "sco_ver";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbres.res_sco_version = val;
		}
		/*
		 * //send message prmNm = "id"; val = (bMultiPart) ?
		 * multi.getParameter(prmNm) : request.getParameter(prmNm); if( val !=
		 * null && val.length() != 0 ) { dob.dbXmsg.id = Long.parseLong(val); }
		 * 
		 * prmNm = "id_type"; val = (bMultiPart) ? multi.getParameter(prmNm) :
		 * request.getParameter(prmNm); if( val != null && val.length() != 0 ) {
		 * dob.dbXmsg.id_type = val; }
		 * 
		 * prmNm = "ent_id"; val = (bMultiPart) ? multi.getParameter(prmNm) :
		 * request.getParameter(prmNm); if( val != null && val.length() != 0 ) {
		 * dob.dbXmsg.ent_id = Long.parseLong(val); }
		 * 
		 * prmNm = "sender_id"; val = (bMultiPart) ? multi.getParameter(prmNm) :
		 * request.getParameter(prmNm); if( val != null && val.length() != 0 )
		 * dob.dbXmsg.sender_id = val;
		 * 
		 * 
		 * prmNm = "ent_ids"; val = (bMultiPart) ? multi.getParameter(prmNm) :
		 * request.getParameter(prmNm); if( val != null && val.length() != 0 )
		 * dob.ent_ids = val;
		 * 
		 * prmNm = "cc_ent_ids"; val = (bMultiPart) ? multi.getParameter(prmNm)
		 * : request.getParameter(prmNm); if( val != null && val.length() != 0 )
		 * dob.cc_ent_ids = val;
		 * 
		 * prmNm = "url_redirect"; val = (bMultiPart) ?
		 * multi.getParameter(prmNm) : request.getParameter(prmNm); if( val !=
		 * null && val.length() != 0 ) dob.url_redirect =
		 * dbUtils.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart);
		 * 
		 * 
		 * prmNm = "msg_type"; val = (bMultiPart) ? multi.getParameter(prmNm) :
		 * request.getParameter(prmNm); if( val != null && val.length() != 0 )
		 * dob.dbXmsg.msg_type = val;
		 */

		/* assignment grading */
		prmNm = "pgr_usr_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbpgr.pgr_usr_id = val;

		prmNm = "pgr_usr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbpgr.pgr_usr_id = val;

		prmNm = "pgr_score";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbpgr.pgr_score = Float.valueOf(val).floatValue();

		prmNm = "pgr_grade";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbpgr.pgr_grade = val;

		/* test result */
		prmNm = "pgr_usr_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.tst.usr_id = val;
		}
		prmNm = "pgr_usr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tst.usr_id = val;

		prmNm = "pgr_mod_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tst.dbmod.mod_res_id = Long.parseLong(val);

		prmNm = "pgr_start_time";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tst.start_time = Timestamp.valueOf(val);

		prmNm = "pgr_used_time";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tst.used_time = Long.parseLong(val);

		prmNm = "pgr_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tst.pgr_status = val;

		prmNm = "terminate_exam_msg";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tst.terminate_exam_msg = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "isTerminateExam";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tst.isTerminateExam = Boolean.valueOf(val).booleanValue();

		prmNm = "exam_mark_as_zero";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tst.is_score_mark_as_zero = Boolean.valueOf(val).booleanValue();

		prmNm = "ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.usg_ent_id = Long.parseLong(val);
			dob.getUsr().usr_ent_id = Long.parseLong(val);
			dob.getUsr().ent_id = Long.parseLong(val);
		}

		prmNm = "group_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.usg_ent_id = Long.parseLong(val);
		}

		prmNm = "usr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.prof.usr_id = val;
			dob.getUsr().usr_id = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_ste_usr_id");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(val);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_nickname";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			dob.getUsr().usr_nickname = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
			dob.vColName.addElement("usr_nickname");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (dob.getUsr().usr_nickname.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_nickname);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "mov_usr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_id = val;
		}

		prmNm = "usr_ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			// val = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
			// bMultiPart);
			dob.usr_ent_id_lst = dbUtils.string2long(sutils.split(val, "~"));
		}

		prmNm = "ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			// val = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
			// bMultiPart);
			dob.ent_id_lst = dbUtils.string2long(sutils.split(val, "~"));
		}
		/*
		 * prmNm = "usr_id_lst"; val = (bMultiPart) ? multi.getParameter(prmNm)
		 * : request.getParameter(prmNm); if ( val!= null && val.length()!= 0 )
		 * { //val = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
		 * bMultiPart); dob.usr_id_lst = sutils.split(val, "~"); }
		 */

		prmNm = "usr_timestamp_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.usr_timestamp_lst = dbUtils.string2timestamp(sutils.split(val,
					"~"));
		}

		prmNm = "usg_ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			// val = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
			// bMultiPart);
			dob.usg_ent_id_lst = dbUtils.string2long(sutils.split(val, "~"));
		}

		prmNm = "usg_timestamp_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.usg_timestamp_lst = dbUtils.string2timestamp(sutils.split(val,
					"~"));
		}

		prmNm = "usr_group_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.getUsr().usr_group_id = Integer.valueOf(val.trim());

		prmNm = "usr_grade_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.getUsr().usr_grade_id = Integer.valueOf(val.trim());

		prmNm = "usr_old_pwd";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.getUsr().usr_old_pwd = val;

		prmNm = "usr_new_pwd";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.getUsr().usr_pwd = val;

		prmNm = "usr_pwd_need_change_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.getUsr().usr_pwd_need_change_ind = Boolean.valueOf(val.trim())
					.booleanValue();
		if (val != null) {
			dob.vColName.addElement("usr_pwd_need_change_ind");
			dob.vColType.addElement(DbTable.COL_TYPE_BOOLEAN);
			if (val.length() > 0) {
				dob.vColValue.addElement(new Boolean(val.trim()));
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_pwd";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.prof.usr_pwd = val;
			dob.getUsr().usr_pwd = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_pwd");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(val);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		// RegUser information
		// for INS/UPD/GET USR
		prmNm = "usr_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_ent_id = Long.parseLong(val);
			dob.getUsr().ent_id = Long.parseLong(val);
		}

		prmNm = "usr_email";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_email = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_email");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(val);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_email_2";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_email_2 = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_email_2");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(val);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_approve_reason";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_approve_reason = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_approve_reason");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_approve_reason);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_last_name_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_last_name_bil = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_last_name_bil");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_last_name_bil);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_first_name_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_first_name_bil = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_first_name_bil");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_first_name_bil);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_display_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_display_bil = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_display_bil");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_display_bil);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_initial_name_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_initial_name_bil = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_initial_name_bil");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_initial_name_bil);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_full_name_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_full_name_bil = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_full_name_bil");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_full_name_bil);
			} else {
				dob.vColValue.addElement(null);
			}
		}
		prmNm = "usr_app_approval_usg_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_app_approval_usg_ent_id = Long.parseLong(val);
		}
		if (val != null) {
			dob.vColName.addElement("usr_app_approval_usg_ent_id");
			dob.vColType.addElement(DbTable.COL_TYPE_LONG);
			if (dob.getUsr().usr_app_approval_usg_ent_id > 0) {
				dob.vColValue.addElement(new Long(
						dob.getUsr().usr_app_approval_usg_ent_id));
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_1";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_1 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_1");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_1);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_2";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_2 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_2");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_2);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_3";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_3 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_3");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_3);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_4";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_4 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_4");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_4);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_5";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_5 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_5");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_5);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_6";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_6 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_6");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_6);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_7";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_7 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_7");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_7);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_8";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_8 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_8");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_8);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_9";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_9 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_9");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_9);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_10";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_extra_10 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_extra_10");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_extra_10);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_11";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_11 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_11 = null;
				dob.getUsr().is_valid_usr_extra_datetime_11 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_11");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_11);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_12";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_12 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_12 = null;
				dob.getUsr().is_valid_usr_extra_datetime_12 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_12");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_12);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_13";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_13 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_13 = null;
				dob.getUsr().is_valid_usr_extra_datetime_13 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_13");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_13);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_14";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_14 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_14 = null;
				dob.getUsr().is_valid_usr_extra_datetime_14 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_14");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_14);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_15";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_15 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_15 = null;
				dob.getUsr().is_valid_usr_extra_datetime_15 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_15");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_15);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_16";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_16 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_16 = null;
				dob.getUsr().is_valid_usr_extra_datetime_16 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_16");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_16);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_17";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_17 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_17 = null;
				dob.getUsr().is_valid_usr_extra_datetime_17 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_17");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_17);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_18";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_18 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_18 = null;
				dob.getUsr().is_valid_usr_extra_datetime_18 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_18");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_18);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_19";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_19 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_19 = null;
				dob.getUsr().is_valid_usr_extra_datetime_19 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_19");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_19);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_extra_datetime_20";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			try {
				dob.getUsr().usr_extra_datetime_20 = Timestamp.valueOf(val);
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_extra_datetime_20 = null;
				dob.getUsr().is_valid_usr_extra_datetime_20 = false;
			}
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_datetime_20");
			dob.vExtColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().usr_extra_datetime_20);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_21";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf() to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_21 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_21 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_21");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_21);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_22";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_22 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_22 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_22");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_22);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_23";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_23 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_23 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_23");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_23);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_24";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_24 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_24 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_24");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_24);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_25";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_25 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_25 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_25");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_25);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_26";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_26 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_26 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_26");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_26);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_27";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_27 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_27 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_27");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_27);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_28";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_28 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_28 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_28");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_28);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_29";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_29 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_29 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_29");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_29);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_singleoption_30";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_singleoption_30 = val;
		} else {
			dob.getUsr().usr_extra_singleoption_30 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_singleoption_30");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_singleoption_30);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		// / add my canding ==========
		prmNm = "urx_extra_41";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().urx_extra_41 = val;
		} else {
			dob.getUsr().urx_extra_41 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_41");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().urx_extra_41);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "urx_extra_42";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			val = dbUtils.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart);
			dob.getUsr().urx_extra_42 = val;
		} else {
			dob.getUsr().urx_extra_42 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_42");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().urx_extra_42);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "urx_extra_43";
		val = (bMultiPart) ? multi.getFilesystemName(prmNm) : null;
		if (val != null && val.length() > 0) {
			dob.getUsr().urx_extra_43 = val;
		} else {
			dob.getUsr().urx_extra_43 = null;
		}

		prmNm = "extension_43_select";
		dob.getUsr().extension_43_select = (bMultiPart) ? multi
				.getParameter(prmNm) : request.getParameter(prmNm);
		if ("use_default_image"
				.equalsIgnoreCase(dob.getUsr().extension_43_select)) {
			prmNm = "default_image";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() > 0) {
				dob.getUsr().urx_extra_43 = val;
			}
		}

		if (dob.getUsr().urx_extra_43 != null) {
			dob.vExtColName.addElement("urx_extra_43");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (dob.getUsr().urx_extra_43.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().urx_extra_43);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "urx_extra_44";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			val = dbUtils.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart);
			dob.getUsr().urx_extra_44 = val;
		} else {
			dob.getUsr().urx_extra_44 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_44");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().urx_extra_44);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "urx_extra_45";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			val = dbUtils.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart);
			dob.getUsr().urx_extra_45 = val;
		} else {
			dob.getUsr().urx_extra_45 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_45");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue.addElement(dob.getUsr().urx_extra_45);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		// ===============================
		prmNm = "usr_extra_multipleoption_31_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_31 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_31 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_31");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_31);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_multipleoption_32_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_32 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_32 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_32");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_32);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_multipleoption_33_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_33 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_33 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_33");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_33);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_multipleoption_34_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_34 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_34 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_34");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_34);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_multipleoption_35_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_35 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_35 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_35");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_35);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_multipleoption_36_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_36 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_36 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_36");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_36);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_multipleoption_37_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_37 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_37 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_37");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_37);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_multipleoption_38_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_38 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_38 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_38");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_38);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_multipleoption_39_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_39 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_39 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_39");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_39);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}
		prmNm = "usr_extra_multipleoption_40_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			// Christ:use String.valueOf to avoid NullPointerException of
			// toString();
			dob.getUsr().usr_extra_multipleoption_40 = val;
		} else {
			dob.getUsr().usr_extra_multipleoption_40 = null;
		}
		if (val != null) {
			dob.vExtColName.addElement("urx_extra_multipleoption_40");
			dob.vExtColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vExtColValue
						.addElement(dob.getUsr().usr_extra_multipleoption_40);
			} else {
				dob.vExtColValue.addElement(null);
			}
		}

		prmNm = "usr_cost_center";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_cost_center = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_cost_center");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_cost_center);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		// selected role to login
		prmNm = "login_role";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().login_role = val;
		}

		prmNm = "page_question_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.page_questions = sutils.split(val, "~");
		}

		prmNm = "usr_role_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_roles = sutils.split(val, "~");
		}
		prmNm = "usr_role_start_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_roles_starts = sutils.split(val, "~");
		}

		prmNm = "usr_role_end_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_roles_ends = sutils.split(val, "~");
		}
		/*
		 * prmNm = "usr_attribute_ent_type_lst"; val = (bMultiPart) ?
		 * multi.getParameter(prmNm) : request.getParameter(prmNm); if ( val!=
		 * null && val.length()!= 0 ) { dob.usr.usr_attribute_ent_types =
		 * sutils.split(val, "~"); }
		 */
		prmNm = "usr_attribute_relation_type_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_attribute_relation_types = sutils.split(val, "~");
		}

		prmNm = "usr_attribute_ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_attribute_ent_ids = dbUtils.string2long(sutils
					.split(val, "~"));
		}

		prmNm = "appr_rol_ext_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		// e.g. val = "APPR~APPR~APPR";
		if (val != null && val.length() != 0) {
			dob.getUsr().appr_rol_ext_ids = sutils.split(val, "~");
		}

		prmNm = "appr_ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		// e.g. val = "4409,123~4468,123~1342,123";
		if (val != null && val.length() != 0) {
			dob.getUsr().appr_ent_ids = sutils.split(val, "~");
		}

		prmNm = "rol_target_ext_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		// e.g. val = "APPR~APPR~APPR";
		if (val != null && val.length() != 0) {
			dob.getUsr().rol_target_ext_ids = sutils.split(val, "~");
		}

		prmNm = "rol_target_ent_group_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		// e.g. val = "4409,123~4468,123~1342,123";
		if (val != null && val.length() != 0) {
			dob.getUsr().rol_target_ent_groups = sutils.split(val, "~");
		}
		prmNm = "rol_target_start_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().rol_target_starts = sutils.split(val, "~");
		}

		prmNm = "rol_target_end_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().rol_target_ends = sutils.split(val, "~");
		}
		prmNm = "usr_gender";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_gender = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_gender");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(val);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_bday";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			try {
				dob.getUsr().usr_bday = Timestamp.valueOf(val);
				dob.getUsr().is_valid_usr_bday = true;
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_bday = null;
				dob.getUsr().is_valid_usr_bday = false;
			}
		}
		if (val != null) {
			dob.vColName.addElement("usr_bday");
			dob.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_bday);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		// prmNm = "usr_bplace_bil";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if ( val!= null && val.length()!= 0 )
		// dob.usr.usr_bplace_bil = dbUtils.unicodeFrom(val, clientEnc,
		// dob.encoding, bMultiPart);

		prmNm = "usr_hkid";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_hkid = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_hkid");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_hkid);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_source";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_source = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_source");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_source);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_other_id_no";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_other_id_no = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_other_id_no");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_other_id_no);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_other_id_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_other_id_type = val;
		}
		if (val != null) {
			dob.vColName.addElement("usr_other_id_type");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_other_id_type);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_tel_1";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_tel_1 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_tel_1");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_tel_1);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_tel_2";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_tel_2 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_tel_2");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_tel_2);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_fax_1";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_fax_1 = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_fax_1");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_fax_1);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_country_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_country_bil = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_country_bil");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_country_bil);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_postal_code_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_postal_code_bil = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_postal_code_bil");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_postal_code_bil);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_state_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_state_bil = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_state_bil");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_state_bil);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		// prmNm = "usr_city_bil";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if ( val!= null && val.length()!= 0 )
		// dob.usr.usr_city_bil = dbUtils.unicodeFrom(val, clientEnc,
		// dob.encoding, bMultiPart);

		prmNm = "usr_address_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_address_bil = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_address_bil");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_address_bil);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		// prmNm = "usr_occupation_bil";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if ( val!= null && val.length()!= 0 )
		// dob.usr.usr_occupation_bil = dbUtils.unicodeFrom(val, clientEnc,
		// dob.encoding, bMultiPart);

		// prmNm = "usr_income_level";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if ( val!= null && val.length()!= 0 )
		// dob.usr.usr_income_level = val;

		// prmNm = "usr_edu_role";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if ( val!= null && val.length()!= 0 )
		// dob.usr.usr_edu_role = val;

		// prmNm = "usr_edu_level";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if ( val!= null && val.length()!= 0 )
		// dob.usr.usr_edu_level = val;

		// prmNm = "usr_school_bil";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if ( val!= null && val.length()!= 0 )
		// dob.usr.usr_school_bil = dbUtils.unicodeFrom(val, clientEnc,
		// dob.encoding, bMultiPart);

		prmNm = "usr_last_login_date";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_last_login_date = Timestamp.valueOf(val);
		}

		prmNm = "usr_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.getUsr().usr_status = val;

		prmNm = "usr_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_upd_date = Timestamp.valueOf(val);
			dob.getUsr().ent_upd_date = Timestamp.valueOf(val);
		}

		// user group
		prmNm = "ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.usg_ent_id = Long.parseLong(val);
			dob.dbusg.ent_id = Long.parseLong(val);
		}

		prmNm = "ent_id_parent";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.ent_id_parent = Long.parseLong(val);

		prmNm = "ent_id_root";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.usg_ent_id_root = Long.parseLong(val);

		prmNm = "ent_ste_uid";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.ent_ste_uid = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "ent_syn_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().ent_syn_ind = Boolean.valueOf(val.trim())
					.booleanValue();
			dob.dbusg.ent_syn_ind = Boolean.valueOf(val.trim()).booleanValue();
		} else {
			dob.getUsr().ent_syn_ind = true;
			dob.dbusg.ent_syn_ind = true;
		}
		// deprecated api, use usr_syn_rol_ind instead
		prmNm = "ent_syn_rol_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_syn_rol_ind = Boolean.valueOf(val.trim())
					.booleanValue();
			// dob.dbusg.ent_syn_rol_ind =
			// Boolean.valueOf(val.trim()).booleanValue();
		}

		prmNm = "usr_syn_rol_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_syn_rol_ind = Boolean.valueOf(val.trim())
					.booleanValue();
		}

		prmNm = "usr_not_syn_relation_type_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			dob.getUsr().usr_not_syn_gpm_type = val;
			dob.vColName.addElement("usr_not_syn_gpm_type");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(val);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		if (val != null && val.length() != 0) {
			dob.getUsr().usr_not_syn_gpm_type = val;
		}

		prmNm = "usr_join_date";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			try {
				dob.getUsr().usr_join_datetime = Timestamp.valueOf(val);
				dob.getUsr().is_valid_usr_join_datetime = true;
			} catch (IllegalArgumentException e) {
				dob.getUsr().usr_join_datetime = null;
				dob.getUsr().is_valid_usr_join_datetime = false;
			}
		}
		if (val != null) {
			dob.vColName.addElement("usr_join_datetime");
			dob.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_join_datetime);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "usr_job_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().usr_job_title = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}
		if (val != null) {
			dob.vColName.addElement("usr_job_title");
			dob.vColType.addElement(DbTable.COL_TYPE_STRING);
			if (val.length() > 0) {
				dob.vColValue.addElement(dob.getUsr().usr_job_title);
			} else {
				dob.vColValue.addElement(null);
			}
		}

		prmNm = "direct_supervisor_ent_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.getUsr().direct_supervisor_ent_ids = sutils.split(val, "~");
			} else {
				dob.getUsr().direct_supervisor_ent_ids = new String[0];
			}
		}

		prmNm = "direct_supervisor_start_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.getUsr().direct_supervisor_starts = sutils.split(val, "~");
			} else {
				dob.getUsr().direct_supervisor_starts = new String[0];
			}
		}

		prmNm = "direct_supervisor_end_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.getUsr().direct_supervisor_ends = sutils.split(val, "~");
			} else {
				dob.getUsr().direct_supervisor_ends = new String[0];
			}
		}

		prmNm = "supervise_target_ent_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.getUsr().supervise_target_ent_ids = sutils.split(val, "~");
			} else {
				dob.getUsr().supervise_target_ent_ids = new String[0];
			}
		}

		prmNm = "supervise_target_start_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.getUsr().supervise_target_starts = sutils.split(val, "~");
			} else {
				dob.getUsr().supervise_target_starts = new String[0];
			}
		}

		prmNm = "supervise_target_end_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.getUsr().supervise_target_ends = sutils.split(val, "~");
			} else {
				dob.getUsr().supervise_target_ends = new String[0];
			}
		}

		prmNm = "grade_code";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().grade_code = val;
		}

		prmNm = "group_code";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().group_code = val;
		}

		prmNm = "direct_supervisor_usr_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.getUsr().direct_supervisor_usr_lst = sutils.split(val, "~");
			} else {
				dob.getUsr().direct_supervisor_usr_lst = new String[0];
			}
		}

		prmNm = "supervise_target_group_code_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.getUsr().supervise_target_group_code_lst = sutils.split(
						val, "~");
			} else {
				dob.getUsr().supervise_target_group_code_lst = new String[0];
			}
		}

		prmNm = "usr_role_code_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.getUsr().role_code_lst = sutils.split(val, "~");
		}

		prmNm = "usg_display_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.usg_display_bil = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "usg_desc";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.usg_desc = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		// for department budget
		prmNm = "usg_budget";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.usg_budget = Integer.parseInt(val);

		prmNm = "usg_role";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.usg_role = val;

		prmNm = "usg_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.ent_upd_date = Timestamp.valueOf(val);

		// quiz
		prmNm = "sub";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.sub = val;

		prmNm = "template";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.template = val;

		prmNm = "subId";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.subId = Long.parseLong(val);

		prmNm = "size";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.size = Integer.parseInt(val);

		// specific arguments
		// to get template list
		prmNm = "tpl_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbtpl.tpl_type = val;

		prmNm = "tpl_subtype";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tpl_subtype = val;

		prmNm = "tpl_name";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbtpl.tpl_name = val;
			dob.dbmod.res_tpl_name = val;
		}

		// to get obj list hdr
		/*
		 * prmNm = "que_obj_id_syb"; val = (bMultiPart) ?
		 * multi.getParameter(prmNm) : request.getParameter(prmNm); if ( val!=
		 * null && val.length()!= 0 ) dob.olhPrm.obj_id_syb =
		 * Long.parseLong(val);
		 * 
		 * prmNm = "que_obj_id_ass"; val = (bMultiPart) ?
		 * multi.getParameter(prmNm) : request.getParameter(prmNm); if ( val!=
		 * null && val.length()!= 0 ) dob.olhPrm.obj_id_ass =
		 * Long.parseLong(val);
		 */

		// syb
		prmNm = "syb_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbsyb.syl_id = Long.parseLong(val);
			dob.dbobj.obj_syl_id = Long.parseLong(val);
		}

		prmNm = "syb_locale";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsyb.syl_locale = val;

		prmNm = "syb_privilege";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsyb.syl_privilege = val;

		prmNm = "syb_desc";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsyb.syl_desc = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		// objective
		prmNm = "syl_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbobj.obj_syl_id = Long.parseLong(val);

		prmNm = "obj_id_parent";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbobj.obj_obj_id_parent = Long.parseLong(val);

		prmNm = "obj_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbobj.obj_type = val;

		prmNm = "obj_desc";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbobj.obj_desc = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "obj_tcr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbobj.obj_tcr_id = Long.parseLong(val);

		prmNm = "obj_share_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbobj.obj_share_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		prmNm = "reader_ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbobj.reader_ent_id_lst = dbUtils.string2long(sutils.split(val,
					"~"));

		prmNm = "author_ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbobj.author_ent_id_lst = dbUtils.string2long(sutils.split(val,
					"~"));

		prmNm = "owner_ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbobj.owner_ent_id_lst = dbUtils.string2long(sutils.split(val,
					"~"));

		prmNm = "lrn_view_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbobj.lrn_can_view_ind = Boolean.valueOf(val.trim())
					.booleanValue();
		}

		// Survey && Login
		prmNm = "domain";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.domain = val;
		}

		// Module
		prmNm = "mod_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbmod.res_type = val;
			dob.dbmod.mod_type = val;
		}

		prmNm = "mod_subtype";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbmod.res_subtype = val;
			dob.dbmod.mod_type = val;
		}

		prmNm = "evt_datetime";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbevt.evt_datetime = Timestamp.valueOf(val);

		prmNm = "evt_venue";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbevt.evt_venue = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "mod_max_score";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			// dob.dbmod.mod_max_score = Float.valueOf(val);
			dob.dbmod.mod_max_score = Float.valueOf(val).floatValue();

		prmNm = "mod_pass_score";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			// dob.dbmod.mod_pass_score = Float.valueOf(val).floatValue();
			dob.dbmod.mod_pass_score = Float.valueOf(val).floatValue();

		prmNm = "mod_difficulty";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.res_difficulty = Integer.parseInt(val);

		prmNm = "mod_duration";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.res_duration = Float.valueOf(val).floatValue();

		prmNm = "mod_privilege";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.res_privilege = val;

		prmNm = "mod_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.res_status = val;

		prmNm = "mobile_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.modTc.mtc_mobile_ind = Boolean.parseBoolean(val);
			dob.dbmod.mod_mobile_ind = Boolean.parseBoolean(val) ? 1 : 0;
		}

		prmNm = "mod_logic";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_logic = val;

		prmNm = "mod_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		String mod_id = cwUtils.esc4JS(val);
		if (mod_id != null && mod_id.length() != 0) {
			dob.dbres.res_id = Long.parseLong(mod_id);
			dob.dbmod.res_id = Long.parseLong(mod_id);
			dob.dbmod.mod_res_id = Long.parseLong(mod_id);
			dob.dbmsp.msp_res_id = Long.parseLong(mod_id);
			dob.mod_id = Long.parseLong(mod_id);
		}

		prmNm = "mod_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.res_upd_date = Timestamp.valueOf(val);

		prmNm = "mod_src_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbmod.res_src_type = val;
		}

		// training center of module
		prmNm = "mtc_tcr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.modTc.mtc_tcr_id = Long.parseLong(val);

		prmNm = "msg_body";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbmod.res_vod_main = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		} else {
			dob.dbmod.res_vod_main = null;
		}

		prmNm = "mod_vod_duration";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.res_vod_duration = Integer.parseInt(val);

		if (bMultiPart && dob.bFileUploaded
				&& multi.getFilesystemName("mod_vod_img") != null) {
			dob.dbmod.res_img_link = multi.getFilesystemName("mod_vod_img");
		} else {
			prmNm = "mod_vod_img";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.dbmod.res_img_link = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);
		}

		// WaiLun : get the name of the uploaded file from module
		if (dob.dbmod.res_src_type != null
				&& (dob.dbmod.res_src_type).equalsIgnoreCase("FILE")) {
			if (bMultiPart && dob.bFileUploaded
					&& multi.getFilesystemName("mod_file") != null) {
				dob.dbmod.res_src_link = multi.getFilesystemName("mod_file");
				dob.dbmod.vodFile = multi.getFile("mod_file");
			} else {
				prmNm = "mod_src_link";
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0)
					dob.dbmod.res_src_link = dbUtils.unicodeFrom(val,
							clientEnc, dob.encoding, bMultiPart);
			}
		}

		if (dob.dbmod.res_src_type != null
				&& (dob.dbmod.res_src_type).equalsIgnoreCase("WIZPACK"))
			if (bMultiPart && dob.bFileUploaded
					&& multi.getFilesystemName("mod_wizpack") != null)
				dob.dbmod.res_src_link = multi.getFilesystemName("mod_wizpack");
			else {
				prmNm = "mod_src_link";
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0) {
					dob.dbmod.res_src_link = dbUtils.unicodeFrom(val,
							clientEnc, dob.encoding, bMultiPart);
				}
			}
		if (dob.dbmod.res_src_type != null
				&& (dob.dbmod.res_src_type).equalsIgnoreCase("URL")) {
			prmNm = "mod_src_link";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.dbmod.res_src_link = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);
			}
		}

		if (dob.dbmod.res_src_type != null
				&& (dob.dbmod.res_src_type).equalsIgnoreCase("AICC_FILES")) {
			prmNm = "mod_src_link";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.dbmod.res_src_link = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);
			}
		}

		if (dob.dbmod.res_src_type != null
				&& (dob.dbmod.res_src_type).equalsIgnoreCase("CDF")) {
			prmNm = "mod_src_link";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.dbmod.res_src_link = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);
			}
		}

		if (dob.dbmod.res_src_type != null
				&& (dob.dbmod.res_src_type).equalsIgnoreCase("MANIFEST_FILE")) {
			prmNm = "mod_src_link";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.dbmod.res_src_link = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);
			}
		}

		if (dob.dbmod.res_src_type != null
				&& (dob.dbmod.res_src_type).equalsIgnoreCase("ZIPFILE")) {
			if (bMultiPart && dob.bFileUploaded
					&& multi.getFilesystemName("mod_zipfilename") != null)
				dob.uploaded_filename = multi
						.getFilesystemName("mod_zipfilename");

			prmNm = "mod_src_link";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.dbmod.res_src_link = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);
			}
		}
		
		if (dob.dbmod.res_src_type != null && (dob.dbmod.res_src_type).indexOf("ONLINEVIDEO_") != -1) {
			prmNm = "mod_src_link";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				dob.dbmod.res_src_link = dbUtils.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart);
			}
		}

		prmNm = "mod_src_online_link";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request.getParameter(prmNm);
		if (val != null && val.length() > 0)
			dob.dbmod.res_src_online_link = val;
		
		prmNm = "multi_del";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0)
			dob.multi_del = Boolean.valueOf(val.trim()).booleanValue();

		prmNm = "multi_reactivate";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0)
			dob.multi_reactivate = Boolean.valueOf(val.trim()).booleanValue();

		prmNm = "mod_score_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_score_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		prmNm = "mod_score_reset";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_score_reset = Long.parseLong(val);

		prmNm = "mod_max_attempt";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_max_attempt = Long.parseLong(val);

		prmNm = "mod_max_usr_attempt";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_max_usr_attempt = Long.parseLong(val);

		prmNm = "mod_instructor";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_usr_id_instructor = val;

		prmNm = "mod_instructor_ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_instructor_ent_id_lst = cwUtils.splitToLong(val, "~");

		prmNm = "mod_has_rate_q";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_has_rate_q = Boolean.valueOf(val.trim())
					.booleanValue();

		prmNm = "mod_is_public";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_is_public = Boolean.valueOf(val.trim())
					.booleanValue();

		prmNm = "mod_show_answer_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_show_answer_ind = Integer.parseInt(val);

		prmNm = "mod_show_answer_after_passed_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_show_answer_after_passed_ind = Integer.parseInt(val);

		prmNm = "mod_sub_after_passed_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_sub_after_passed_ind = Integer.parseInt(val);

		prmNm = "mod_show_save_and_suspend_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_show_save_and_suspend_ind = Integer.parseInt(val);

		prmNm = "mod_managed_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_managed_ind = Integer.parseInt(val);

		prmNm = "mod_tcr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.mod_tcr_id = Long.parseLong(val);
		else
			dob.dbmod.mod_tcr_id = -1;

		prmNm = "num_of_mod";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.num_of_mod = Integer.parseInt(val);

		// Assignment
		prmNm = "ass_max_upload";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbass.ass_max_upload = Integer.parseInt(val);

		prmNm = "ass_email";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbass.ass_email = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		prmNm = "ass_submission";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbass.ass_submission = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "ass_notify_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbass.ass_notify_ind = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "ass_due_datetime";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			// added by Emily, 2002-06-14
			if (val.equalsIgnoreCase(dbUtils.UNLIMITED))
				dob.dbass.ass_due_datetime = Timestamp
						.valueOf(dbUtils.MAX_TIMESTAMP);
			else
				dob.dbass.ass_due_datetime = Timestamp.valueOf(val);

		prmNm = "ass_due_date_day";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbass.ass_due_date_day = Long.parseLong(val);

		/*
		 * prmNm = "del_file"; val = (bMultiPart) ? multi.getParameter(prmNm) :
		 * request.getParameter(prmNm); if ( val!= null && val.length()!= 0 ) {
		 * dob.ass_filename = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
		 * bMultiPart); }
		 */

		prmNm = "no_of_upload";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbass.no_of_upload = Long.parseLong(val.trim());

		prmNm = "file_desc_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			val = dbUtils.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart);
			dob.file_desc_lst = sutils.split(val, ":_:_:");
		}

		prmNm = "num_of_files";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			long numFile = Long.parseLong(val);
			String filename, comment;
			String order = "";

			for (int i = 1; i <= numFile; i++) {
				filename = null;
				prmNm = "file" + i;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0) {
					filename = dbUtils.unicodeFrom(val, clientEnc,
							dob.encoding, bMultiPart);
					if (sess != null
							&& sess.getAttribute(ORIGINAL_FILENAME) != null)
						filename = convertFilename(filename, sess);
				}

				comment = "";
				prmNm = "comment" + i;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0) {
					comment = val;
					// comment = dbUtils.esc4XML(dbUtils.unicodeFrom(val,
					// clientEnc, dob.encoding, bMultiPart));
				}

				if (filename != null) {
					dob.file_lst.put(filename, comment);
					order += filename + "~";
				} else {
					order += ".~";
				}
			}

			dob.file_order = sutils.split(order, "~");
		}

		prmNm = "ass_filename";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.ass_filename = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		prmNm = "ass_comment";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.ass_comment = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
					bMultiPart);
		}

		prmNm = "files";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.files = sutils.split(val, "~");

		/*
		 * prmNm = "type_lst"; val = (bMultiPart) ? multi.getParameter(prmNm) :
		 * request.getParameter(prmNm); if ( val!= null && val.length()!= 0 )
		 * dob.type_lst = sutils.split(val, "~");
		 */

		prmNm = "download_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.download_lst = sutils.split(val, "~");

		prmNm = "email";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.email = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
					bMultiPart);
		}

		prmNm = "step";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.step = Long.parseLong(val);

		prmNm = "ass_queue";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.ass_queue = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
					bMultiPart);
		}

		prmNm = "ass_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			dob.ass_timestamp = Timestamp.valueOf(val);
		}

		// Forum and Faq stuff
		prmNm = "ismaintain";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.isMaintain = Boolean.valueOf(val.trim()).booleanValue();
		}

		prmNm = "title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbforTopic.fto_title = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
			dob.dbforMsg.fmg_title = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
			dob.dbfaqTopic.fto_title = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		prmNm = "msg";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbforMsg.fmg_msg = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
			dob.dbfaqMsg.fmg_msg = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		prmNm = "image";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbforMsg.fmg_image = val;
			dob.dbfaqMsg.fmg_image = val;
		}

		prmNm = "topic_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbforTopic.fto_id = Long.parseLong(val);
			dob.dbfaqTopic.fto_id = Long.parseLong(val);
		}

		prmNm = "msg_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbforMsg.fmg_id = Long.parseLong(val);
			dob.dbfaqMsg.fmg_id = Long.parseLong(val);
		}

		prmNm = "msg_id_parent";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbforMsg.fmg_fmg_id_parent = Long.parseLong(val);
			dob.dbfaqMsg.fmg_fmg_id_parent = Long.parseLong(val);
		} else {
			dob.dbforMsg.fmg_fmg_id_parent = 0;
			dob.dbfaqMsg.fmg_fmg_id_parent = 0;
		}

		prmNm = "extend";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && (Long.parseLong(val) == 1))
			dob.isExtend = true;

		prmNm = "unthread";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && (Long.parseLong(val) == 1))
			dob.unthread = true;

		prmNm = "phrase";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.phrase = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
					bMultiPart);

		prmNm = "created_by";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.created_by = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
					bMultiPart);

		prmNm = "search_type_topic";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.search_type_topic = Integer.parseInt(val);

		prmNm = "search_type_msg";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.search_type_msg = Integer.parseInt(val);

		prmNm = "phrase_cond";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.phrase_cond = Integer.parseInt(val);

		prmNm = "created_by_cond";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.created_by_cond = Integer.parseInt(val);

		prmNm = "msg_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.msgLst = sutils.split(val, "~");

		prmNm = "topic_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.topicLst = sutils.split(val, "~");

		prmNm = "page";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.page = Integer.parseInt(val);
		} else {
			dob.page = dob.cur_page;
		}

		prmNm = "created_before";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.created_before = Timestamp.valueOf(val);

		prmNm = "created_after";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.created_after = Timestamp.valueOf(val);

		prmNm = "msg_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbforMsg.fmg_type = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
			dob.dbfaqMsg.fmg_type = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		// faq
		prmNm = "view_comment";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.commentOn = Integer.parseInt(val);

		prmNm = "search_que";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.search_que = Integer.parseInt(val);

		prmNm = "search_ans";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.search_ans = Integer.parseInt(val);

		prmNm = "search_com";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.search_com = Integer.parseInt(val);

		prmNm = "msg_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbfaqMsg.fmg_title = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		// Module Objective
		prmNm = "fr_obj_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.fr_obj_id = Long.parseLong(val);

		prmNm = "to_obj_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.to_obj_id = Long.parseLong(val);

		prmNm = "container_res_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbque.res_mod_res_id_test = Long.parseLong(val);
		}

		prmNm = "obj_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.robs = sutils.split(val, "~");
			dob.dbobj.obj_id = Long.parseLong(dob.robs[0]);
			dob.dbmsp.msp_obj_id = Long.parseLong(dob.robs[0]);
		}

		prmNm = "obj_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.robs = sutils.split(val, "~");
		}

		prmNm = "msp_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbmsp.msp_id = Long.parseLong(val);
		}

		// Course
		prmNm = "course_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbcos.res_id = Long.parseLong(val);
			dob.dbcos.cos_res_id = Long.parseLong(val);
		}

		prmNm = "cos_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.itm_type = val;

		prmNm = "is_new_cos";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.is_new_cos = Boolean.valueOf(val.trim()).booleanValue();
		;

		prmNm = "cos_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		String cos_id = cwUtils.esc4JS(val);
		if (cos_id != null && cos_id.length() != 0
				&& !cos_id.equalsIgnoreCase("undefined")) {
			dob.dbcos.res_id = Long.parseLong(cos_id);
			dob.dbcos.cos_res_id = Long.parseLong(cos_id);
		}

		prmNm = "course_privilege";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcos.res_privilege = val;

		prmNm = "course_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcos.res_status = val;

		prmNm = "course_desc";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcos.res_desc = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "course_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		// System.out.println("course_timestamp = " + val);
		if (val != null && val.length() != 0)
			dob.dbcos.res_upd_date = Timestamp.valueOf(val);

		prmNm = "course_struct_xml_cnt";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			int cnt = Integer.parseInt(val);
			StringBuffer xml = new StringBuffer();
			for (int i = 1; i <= cnt; i++) {

				prmNm = "course_struct_xml_" + i;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);

				xml.append(val);
			}

			dob.dbcos.cos_structure_xml = dbUtils.unicodeFrom(xml.toString(),
					clientEnc, dob.encoding, bMultiPart);
			// System.out.println(dob.dbcos.cos_structure_xml);
		}

		prmNm = "is_hkib_vod";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbcos.is_hkib_vod = Boolean.valueOf(val.trim()).booleanValue();
		} else {
			dob.dbcos.is_hkib_vod = false;
		}
		prmNm = "is_click_node";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbcos.is_click_node = Boolean.valueOf(val.trim())
					.booleanValue();
		} else {
			dob.dbcos.is_click_node = false;
		}

		prmNm = "cos_chapter_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcos.cos_chapter_id = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "cos_node_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcos.cos_node_id = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		prmNm = "vod_res_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0
				&& !val.equalsIgnoreCase("undefined")) {
			dob.dbcos.vod_res_id = Long.parseLong(val);
			dob.dbcos.vod_res_id = Long.parseLong(val);
		}

		// report
		prmNm = "rpt_usr_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbpgr.pgr_usr_id = val;

		prmNm = "rpt_usr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbpgr.pgr_usr_id = val;

		prmNm = "attempt_nbr";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbpgr.pgr_attempt_nbr = Long.parseLong(val);

		prmNm = "rpt_usg_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.ent_id_parent = Long.parseLong(val);

		// question
		prmNm = "que_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbque.que_res_id = Long.parseLong(val);
			dob.dbque.res_id = Long.parseLong(val);
		}

		prmNm = "que_difficulty";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbque.res_difficulty = Integer.parseInt(val);

		prmNm = "que_duration";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbque.res_duration = Float.valueOf(val).floatValue();

		prmNm = "que_privilege";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbque.res_privilege = val;

		prmNm = "que_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbque.res_status = val;

		prmNm = "que_score";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbque.que_score = Integer.parseInt(val);

		prmNm = "que_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbque.que_type = val;

		prmNm = "que_int_cnt";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbque.que_int_count = Integer.parseInt(val);

		prmNm = "que_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbque.res_upd_date = Timestamp.valueOf(val);

		prmNm = "que_owner";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbque.res_usr_id_owner = val;

		prmNm = "que_submit_file_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("y")) {
				dob.dbque.que_submit_file_ind = true;
			} else {
				dob.dbque.que_submit_file_ind = false;
			}
		}

		prmNm = "sc_que_shuffle";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("y")) {
				dob.dbque.que_sc_shuffle = true;
			} else {
				dob.dbque.que_sc_shuffle = false;
			}
		}
		// Module Spec
		prmNm = "msp_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsp.msp_type = val;

		prmNm = "msp_score";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsp.msp_score = Long.parseLong(val);

		prmNm = "msp_difficulty";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsp.msp_difficulty = Integer.parseInt(val);

		prmNm = "msp_privilege";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsp.msp_privilege = val;

		prmNm = "msp_duration";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbmsp.msp_duration = Float.valueOf(val).floatValue();
		} else {
			dob.dbmsp.msp_duration = -1;
		}
		prmNm = "msp_qcount";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsp.msp_qcount = Long.parseLong(val);

		prmNm = "msp_algorithm";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsp.msp_algorithm = Long.parseLong(val);

		prmNm = "time_limit";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmod.res_duration = Float.valueOf(val).floatValue();

		// others

		prmNm = "output_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.output_type = val;

		prmNm = "sort_order";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.sort_order = val;

		prmNm = "order";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.sort_order = val;

		if (dob.sort_order != null && dob.sort_order.length() != 0) {
			if (dob.sort_order.equalsIgnoreCase(dbUtils.SORT_BY_TITLE)) {
				dob.sort_order = "res_title";
			} else if (dob.sort_order.equalsIgnoreCase(dbUtils.SORT_BY_TYPE)) {
				dob.sort_order = "res_type";
			} else if (dob.sort_order.equalsIgnoreCase(dbUtils.SORT_BY_AUTHOR)) {
				dob.sort_order = "res_usr_id_owner";
			} else if (dob.sort_order.equalsIgnoreCase(dbUtils.SORT_BY_TIME)) {
				dob.sort_order = "res_upd_date";
			} else if (dob.sort_order.equalsIgnoreCase(dbUtils.SORT_BY_ID)) {
				dob.sort_order = "res_id";
			} else if (dob.sort_order.equalsIgnoreCase(dbUtils.SORT_BY_STATUS)) {
				dob.sort_order = "res_status";
			}
		}

		prmNm = "que_obj_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.robs = sutils.split(val, "~");

		prmNm = "que_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.que_id_lst = sutils.split(val, "~");

		prmNm = "que_score_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.que_score_lst = sutils.split(val, "~");

		prmNm = "que_order_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.que_order_lst = sutils.split(val, "~");

		prmNm = "que_multiplier_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.que_multiplier_lst = sutils.split(val, "~");

		prmNm = "cal_d";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.cal_d = Integer.parseInt(val);

		prmNm = "cal_m";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.cal_m = Integer.parseInt(val);

		prmNm = "cal_y";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.cal_y = Integer.parseInt(val);

		// Begin, Dennis, impl cos release control
		prmNm = "cos_eff_start_datetime";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			if (val.equalsIgnoreCase(dbUtils.IMMEDIATE))
				dob.dbcos.cos_eff_start_datetime = Timestamp
						.valueOf(dbUtils.MIN_TIMESTAMP);
			// cannot get the current time as DB connection not yet started
			else
				dob.dbcos.cos_eff_start_datetime = Timestamp.valueOf(val);

		// Begin, Dennis, impl cos release control
		prmNm = "cos_eff_end_datetime";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			if (val.equalsIgnoreCase(dbUtils.UNLIMITED))
				dob.dbcos.cos_eff_end_datetime = Timestamp
						.valueOf(dbUtils.MAX_TIMESTAMP);
			else
				dob.dbcos.cos_eff_end_datetime = Timestamp.valueOf(val);

		// Begin, Dennis, 2000-11-13, impl release control
		// get the request parameter mod_eff_start_datetime
		prmNm = "mod_eff_start_datetime";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		// System.out.println("mod_eff_start_datetime = " + val);
		if (val != null && val.length() != 0)
			if (val.equalsIgnoreCase(dbUtils.IMMEDIATE))
				dob.dbmod.mod_in_eff_start_datetime = Timestamp
						.valueOf(dbUtils.MIN_TIMESTAMP);
			// cannot get the current time as DB connection not yet started
			else
				dob.dbmod.mod_in_eff_start_datetime = Timestamp.valueOf(val);
		// System.out.println("mod_eff_start_datetime = " +
		// dob.dbmod.mod_in_eff_start_datetime);

		// Begin, Dennis, 2000-11-13, impl release control
		// get the request parameter mod_eff_end_datetime
		prmNm = "mod_eff_end_datetime";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		// System.out.println("mod_eff_end_datetime = " + val);
		if (val != null && val.length() != 0)
			if (val.equalsIgnoreCase(dbUtils.UNLIMITED))
				dob.dbmod.mod_in_eff_end_datetime = Timestamp
						.valueOf(dbUtils.MAX_TIMESTAMP);
			else
				dob.dbmod.mod_in_eff_end_datetime = Timestamp.valueOf(val);
		// System.out.println("mod_eff_end_datetime = " +
		// dob.dbmod.mod_in_eff_end_datetime);

		// Begin, Dennis, Display Option, store the VIEW that front end wants
		prmNm = "dpo_view";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_view = val;

		// Begin, Dennis, Display Option
		prmNm = "dpo_icon_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_icon_ind = Boolean.valueOf(val.trim()).booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_title_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_title_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_lan_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_lan_ind = Boolean.valueOf(val.trim()).booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_desc_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_desc_ind = Boolean.valueOf(val.trim()).booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_instruct_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_instruct_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_eff_start_datetime_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_eff_start_datetime_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_eff_end_datetime_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_eff_end_datetime_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_difficulty_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_difficulty_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_time_limit_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_time_limit_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_suggested_time_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_suggested_time_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_duration_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_duration_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_max_score_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_max_score_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_pass_score_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_pass_score_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_pgr_last_acc_datetime_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_pgr_last_acc_datetime_ind = Boolean.valueOf(
					val.trim()).booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_pgr_start_datetime_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_pgr_start_datetime_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_pgr_complete_datetime_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_pgr_complete_datetime_ind = Boolean.valueOf(
					val.trim()).booleanValue();

		// Begin, Dennis, Display Option
		prmNm = "dpo_pgr_attempt_nbr_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbdpo.dpo_pgr_attempt_nbr_ind = Boolean.valueOf(val.trim())
					.booleanValue();

		// NEW
		prmNm = "rpt_group_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.rpt_group_lst = sutils.split(val, "~");

		prmNm = "rpt_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.rpt_type = val;

		prmNm = "rpt_date_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.rpt_date_type = val;

		prmNm = "ugr_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.userGrade.ugr_ent_id = Long.parseLong(val);
		}

		prmNm = "ugr_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0) {
			dob.userGrade.ent_upd_date = Timestamp.valueOf(val);
		}

		prmNm = "ugr_display_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.userGrade.ugr_display_bil = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		prmNm = "ugr_tcr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.userGrade.ugr_tcr_id = Long.parseLong(val);
		} else {
			dob.userGrade.ugr_tcr_id = -1;
		}

		prmNm = "ugr_grade_code";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.userGrade.ent_ste_uid = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		prmNm = "ugr_order";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.ugr_order = val;
		}

		// process charset dependent fields
		String que_title, que_desc, que_xml;
		String mod_title, mod_desc, mod_instr;
		String cos_nm, cos_desc;
		String mod_required_time;
		String mod_download_ind;
		String mod_mobile_ind;
		String mod_text_style;
		File vodFile;

		prmNm = "mod_test_style";
		mod_text_style = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		prmNm = "que_title";
		que_title = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		prmNm = "que_desc";
		que_desc = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		prmNm = "que_xml";
		que_xml = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);

		prmNm = "mod_title";
		mod_title = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		prmNm = "mod_desc";
		mod_desc = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		prmNm = "mod_required_time";
		mod_required_time = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		prmNm = "mod_download_ind";
		mod_download_ind = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		prmNm = "mod_mobile_ind";
		mod_mobile_ind = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);

		prmNm = "mod_instr";
		mod_instr = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		prmNm = "course_nm";
		cos_nm = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);

		prmNm = "tkh_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			try {
				dob.tkh_id = Long.parseLong(val);
			} catch (Exception e) {
				dob.tkh_id = DbTrackingHistory.TKH_ID_UNDEFINED;
			}
		} else {
			dob.tkh_id = DbTrackingHistory.TKH_ID_UNDEFINED;
		}
		dob.dbmov.mov_tkh_id = dob.tkh_id;
		dob.dbres.tkh_id = dob.tkh_id;
		dob.dbcos.tkh_id = dob.tkh_id;
		dob.dbmod.tkh_id = dob.tkh_id;
		dob.dbpgr.pgr_tkh_id = dob.tkh_id;

		prmNm = "course_desc";
		cos_desc = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);

		dob.dbque.res_title = dbUtils.unicodeFrom(que_title, clientEnc,
				dob.encoding, bMultiPart);
		dob.dbque.res_desc = dbUtils.unicodeFrom(que_desc, clientEnc,
				dob.encoding, bMultiPart);
		dob.dbque.que_xml = dbUtils.unicodeFrom(que_xml, clientEnc,
				dob.encoding, bMultiPart);

		dob.dbmod.res_title = dbUtils.unicodeFrom(mod_title, clientEnc,
				dob.encoding, bMultiPart);
		dob.dbmod.mod_test_style = dbUtils.unicodeFrom(mod_text_style,
				clientEnc, dob.encoding, bMultiPart);
		dob.dbmod.res_desc = dbUtils.unicodeFrom(mod_desc, clientEnc,
				dob.encoding, bMultiPart);
		if (mod_required_time != null
				&& !"".equalsIgnoreCase(mod_required_time)) {
			dob.dbmod.mod_required_time = Integer.parseInt(mod_required_time);// dbUtils.unicodeFrom(mod_required_time,
																				// clientEnc,
																				// dob.encoding,
																				// bMultiPart);
		}

		if (mod_download_ind != null) {
			dob.dbmod.mod_download_ind = Integer.parseInt(mod_download_ind);
		}

		if (mod_mobile_ind != null) {
			dob.dbmod.mod_mobile_ind = Integer.parseInt(mod_mobile_ind);
		}
		dob.dbmod.mod_instruct = dbUtils.unicodeFrom(mod_instr, clientEnc,
				dob.encoding, bMultiPart);

		dob.dbcos.res_title = dbUtils.unicodeFrom(cos_nm, clientEnc,
				dob.encoding, bMultiPart);
		dob.dbcos.res_desc = dbUtils.unicodeFrom(cos_desc, clientEnc,
				dob.encoding, bMultiPart);

		dob.dbass.initialize(dob.dbmod);
		dob.dbass.RES_FOLDER = qdbEnv.RES_FOLDER;
		dob.dbass.bFileUpload = dob.bFileUpload;
		dob.dbass.INI_DIR_UPLOAD = wizbini.getFileUploadResDirAbs();

		// dob.dbass.INI_MAIL_SERVER = static_env. INI_MAIL_SERVER;
		// dob.dbass.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL =
		// static_env.INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL;
		// dob.dbass.tmpUploadDir = dob.session_upload_dir + dbUtils.SLASH +
		// sess.getId();

		dob.dbevt.initialize(dob.dbmod);
		dob.dbchat.initialize(dob.dbmod, wizbini.cfgSysSetup.getTspace()
				.getHost(), wizbini.cfgSysSetup.getTspace().getPort(),
				wizbini.cfgSysSetup.getTspace().getRoomPort(),
				wizbini.cfgSysSetup.getTspace().getWwwPort());
		// Searching.....................................................................
		String DELIMITER = " ";
		prmNm = "search_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0) // {
			// System.out.println("Search Title = " + val);
			// System.out.println("Search Title = " + dbUtils.unicodeFrom(val,
			// dob.encoding));
			dob.dbsearch.search_title = sutils.split(dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart), DELIMITER);
		// dob.dbsearch.search_for = dbUtils.unicodeFrom(val, clientEnc,
		// dob.encoding, bMultiPart);
		// }

		prmNm = "search_desc";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_desc = sutils.split(dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart), DELIMITER);

		prmNm = "search_dur_before";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_dur_before = Float.valueOf(val).floatValue();

		prmNm = "search_dur_after";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_dur_after = Float.valueOf(val).floatValue();

		prmNm = "search_diff_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_diff_list = dbUtils.string2int(dbUtils.split(
					val, "~"));

		prmNm = "search_diff_before";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_diff_before = Integer.parseInt(val);

		prmNm = "search_diff_after";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_diff_after = Integer.parseInt(val);

		prmNm = "search_start_index";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_start_index = Integer.parseInt(val);

		prmNm = "search_items_per_page";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_items_per_page = Integer.parseInt(val);

		prmNm = "search_id_before";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_id_before = Long.valueOf(val).longValue();

		prmNm = "search_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_id = Long.valueOf(val).longValue();

		prmNm = "search_id_after";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_id_after = Long.valueOf(val).longValue();

		prmNm = "search_owner";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_owner = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding);

		prmNm = "search_create_time_after";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_create_time_after = Timestamp.valueOf(val);

		prmNm = "search_create_time_before";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_create_time_before = Timestamp.valueOf(val);

		prmNm = "search_update_time_after";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_update_time_after = Timestamp.valueOf(val);

		prmNm = "search_update_time_before";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_update_time_before = Timestamp.valueOf(val);

		prmNm = "search_folder";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsearch.search_privilege = val;

		prmNm = "search_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsearch.search_status = val;

		prmNm = "search_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsearch.search_type = dbUtils.split(val, "~");

		prmNm = "search_sub_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsearch.search_sub_type = dbUtils.split(val, "~");

		prmNm = "search_key";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsearch.search_key = val;

		prmNm = "search_order";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsearch.search_order = val;

		prmNm = "search_lang";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbsearch.search_lang = val;

		prmNm = "search_owner_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_owner_ent_id = Long.valueOf(val).longValue();

		prmNm = "search_obj_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbsearch.search_obj_id = dbUtils.string2long(sutils.split(val,
					"~"));

		// =====================================================================================
		// Parameters for post message
		// << BEGIN add "level"
		prmNm = "msg_level";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0)
			dob.dbmsg.msg_level = val;
		else
			dob.dbmsg.msg_level = "";
		// >> END

		prmNm = "msg_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsg.msg_id = Long.parseLong(val);

		prmNm = "msg_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsg.msg_type = val;

		prmNm = "encrypt_msg_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsg.encrypt_msg_id = val;

		prmNm = "msg_belong_exam_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && !val.equals("")) {
			try {
				dob.dbmsg.msg_belong_exam_ind = Boolean.parseBoolean(val);
			} catch (Exception e) {
			}
		}

		prmNm = "msg_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsg.msg_title = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "msg_icon";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsg.msg_icon = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "msg_icon_result";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsg.msg_icon_result = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "msg_body";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsg.msg_body = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "msg_tcr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbmsg.msg_tcr_id = Long.parseLong(val);
		} else {
			dob.dbmsg.msg_tcr_id = -1;
		}

		prmNm = "msg_readonly";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbmsg.msg_readonly = Boolean.valueOf(val).booleanValue();
		}

		prmNm = "page";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.page = Integer.parseInt(val);
		}

		prmNm = "page_size";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.page_size = Integer.parseInt(val);
		}

		prmNm = "sortCol";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.sortCol = cwPagination.esc4SortSql(val);

		prmNm = "sortOrder";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.sortOrder = cwPagination.esc4SortSql(val);

		prmNm = "url_cmd";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.url_cmd = val;

		// messaging

		prmNm = "sender_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.sender_id = val;

		prmNm = "url_redirect";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.url_redirect = val;

		prmNm = "msg_subject";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbMgMsg.msg_subject = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "msg_body";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbMgMsg.msg_addition_note = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);

		prmNm = "xtp_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.xtp_type = val;

		prmNm = "xtp_subtype";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.xtp_subtype = val;

		prmNm = "msg_begin_date";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			if (val.equalsIgnoreCase(dbUtils.IMMEDIATE))
				dob.dbmsg.msg_begin_date = Timestamp
						.valueOf(dbUtils.MIN_TIMESTAMP);
			// cannot get the current time as DB connection not yet started
			else
				dob.dbmsg.msg_begin_date = Timestamp.valueOf(val);

		prmNm = "msg_end_date";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			if (val.equalsIgnoreCase(dbUtils.UNLIMITED))
				dob.dbmsg.msg_end_date = Timestamp
						.valueOf(dbUtils.MAX_TIMESTAMP);
			// cannot get the current time as DB connection not yet started
			else
				dob.dbmsg.msg_end_date = Timestamp.valueOf(val);

		prmNm = "msg_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsg.msg_status = val;

		prmNm = "msg_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmsg.msg_upd_date = Timestamp.valueOf(val);

		// for insert notify.......................
		prmNm = "msg_subject";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.msg_title = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
					bMultiPart);

		prmNm = "msg_body";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.msg_body = dbUtils.unicodeFrom(val, clientEnc, dob.encoding,
					bMultiPart);

		prmNm = "msg_subtype";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.msg_method = val;

		prmNm = "msg_datetime";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.msg_send_time = Timestamp.valueOf(val);

		// ............................................................................................
		// Dennis, used in dbResource.ContentListAsXML()
		prmNm = "start_datetime";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.res_start_datetime = Timestamp.valueOf(val);

		prmNm = "end_datetime";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.res_end_datetime = Timestamp.valueOf(val);
		
		prmNm = "rpt_search_full_name";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.rpt_search_full_name = val.trim();

		// Kim param for get course report (rpt_mod)
		prmNm = "itm_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.itm_id = Long.parseLong(val);

		prmNm = "itm_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.itm_id_lst = dbUtils.string2long(dbUtils.split(val, "~"));
		}

		// TODO
		prmNm = "ils_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.ils_id = Integer.parseInt(val);
		}
		prmNm = "itm_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.itm_id = Long.parseLong(val);
		}
		prmNm = "course_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_cos_id = Long.parseLong(val);

		prmNm = "student_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_ent_id = Long.parseLong(val);

		prmNm = "usr_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_ent_id = Long.parseLong(val);

		prmNm = "location";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null)
			dob.cur_node = val;

		// for putparam

		// course id got
		prmNm = "lesson_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_mod_id = Long.parseLong(val);

		prmNm = "lesson_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_status = val;

		prmNm = "lesson_location";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_ele_loc = val;

		// alert!! time is in {hh:mm:ss} format
		prmNm = "time";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_ele_time = val;

		prmNm = "start_time";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_start_time = Timestamp.valueOf(val);

		prmNm = "encrypted_start_time";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_encrypted_start_time = val;

		prmNm = "cur_grp_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null)
			dob.cur_grp_id = val;

		prmNm = "score";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_score = val;

		prmNm = "course_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcov.cov_cos_id = Long.parseLong(val);

		prmNm = "student_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcov.cov_ent_id = Long.parseLong(val);

		prmNm = "mov_last_upd_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbmov.mov_update_timestamp = Timestamp.valueOf(val);
		}

		prmNm = "score";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcov.cov_score = val;

		prmNm = "comment";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcov.cov_comment = val;

		prmNm = "status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbcov.cov_status = val;

		prmNm = "mov_mod_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_mod_id = Long.parseLong(val);

		prmNm = "mov_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbmov.mov_status = val;

		// Chris , parma for checking Aicc Path
		// course_id , parsed already
		// student_id

		prmNm = "course_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbacp.acp_cos_id = Long.parseLong(val);

		prmNm = "student_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbacp.acp_ent_id = Long.parseLong(val);

		prmNm = "lesson_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbacp.acp_mod_id = Long.parseLong(val);

		int PATH_MAX = 100;

		prmNm = "date";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			String[] date_ = new String[PATH_MAX];
			date_ = dbUtils.split(val, "~");
			dob.dbacp.paths.put("date", date_);
		}

		prmNm = "time";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			String[] time_ = new String[PATH_MAX];
			time_ = dbUtils.split(val, "~");
			dob.dbacp.paths.put("time", time_);
		}

		prmNm = "element_location";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			String[] element_location_ = new String[PATH_MAX];
			element_location_ = dbUtils.split(val, "~");
			dob.dbacp.paths.put("element_location", element_location_);
		}

		prmNm = "status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			String[] status_ = new String[PATH_MAX];
			status_ = dbUtils.split(val, "~");
			dob.dbacp.paths.put("status", status_);
		}

		prmNm = "why_left";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			String[] why_left_ = new String[PATH_MAX];
			why_left_ = dbUtils.split(val, "~");
			dob.dbacp.paths.put("why_left", why_left_);
		}

		prmNm = "time_in_element";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			String[] time_in_element_ = new String[PATH_MAX];
			time_in_element_ = dbUtils.split(val, "~");
			dob.dbacp.paths.put("time_in_element", time_in_element_);
		}

		// user search
		prmNm = "s_usr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_id = val;
		else
			dob.dbusg.s_usr_id = null;

		prmNm = "s_usr_email";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_email = val;
		else
			dob.dbusg.s_usr_email = null;

		prmNm = "s_usr_last_name_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_last_name_bil = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		else
			dob.dbusg.s_usr_last_name_bil = null;

		prmNm = "s_usr_first_name_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_first_name_bil = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
		else
			dob.dbusg.s_usr_first_name_bil = null;

		prmNm = "s_usr_display_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_display_bil = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		else
			dob.dbusg.s_usr_display_bil = null;

		prmNm = "s_usr_nickname";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_nickname = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		else
			dob.dbusg.s_usr_nickname = null;

		prmNm = "s_usr_id_display_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_id_display_bil = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
		else
			dob.dbusg.s_usr_id_display_bil = null;

		prmNm = "s_usr_gender";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_gender = val;
		else
			dob.dbusg.s_usr_gender = null;

		prmNm = "s_usr_bday_fr";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbusg.s_usr_bday_fr = Timestamp.valueOf(val);
		else
			dob.dbusg.s_usr_bday_fr = null;

		prmNm = "s_usr_job_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_job_title = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		else
			dob.dbusg.s_usr_job_title = null;

		// other_id and other_id_type was deleted
		// prmNm = "s_usr_other_id";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if (val != null && val.length() != 0)
		// dob.dbusg.s_usr_other_id = dbUtils.unicodeFrom(val, clientEnc,
		// dob.encoding, bMultiPart);
		// else
		// dob.dbusg.s_usr_other_id = null;
		//
		// prmNm = "s_usr_other_id_type";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if (val != null && val.length() != 0)
		// dob.dbusg.s_usr_other_id_type = dbUtils.unicodeFrom(val, clientEnc,
		// dob.encoding, bMultiPart);
		// else
		// dob.dbusg.s_usr_other_id_type = null;

		prmNm = "s_usr_source";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_source = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		else
			dob.dbusg.s_usr_source = null;

		prmNm = "s_usr_bday_to";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbusg.s_usr_bday_to = Timestamp.valueOf(val);
		else
			dob.dbusg.s_usr_bday_to = null;

		prmNm = "s_usr_jday_fr";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbusg.s_usr_jday_fr = Timestamp.valueOf(val);
		else
			dob.dbusg.s_usr_jday_fr = null;

		prmNm = "s_usr_jday_to";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbusg.s_usr_jday_to = Timestamp.valueOf(val);
		else
			dob.dbusg.s_usr_jday_to = null;

		// prmNm = "s_usr_bplace_bil";
		// val = (bMultiPart) ? multi.getParameter(prmNm) :
		// request.getParameter(prmNm);
		// if (val != null && val.length() != 0)
		// dob.dbusg.s_usr_bplace_bil = dbUtils.unicodeFrom(val, clientEnc,
		// dob.encoding, bMultiPart);
		// else
		// dob.dbusg.s_usr_bplace_bil = null;

		prmNm = "s_usr_hkid";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_hkid = val;
		else
			dob.dbusg.s_usr_hkid = null;

		prmNm = "s_usr_tel";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_tel = val;
		else
			dob.dbusg.s_usr_tel = null;

		prmNm = "s_usr_fax";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_fax = val;
		else
			dob.dbusg.s_usr_fax = null;

		prmNm = "s_usr_address_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_address_bil = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		else
			dob.dbusg.s_usr_address_bil = null;

		prmNm = "s_usr_postal_code_bil";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_usr_postal_code_bil = dbUtils.unicodeFrom(val,
					clientEnc, dob.encoding, bMultiPart);
		else
			dob.dbusg.s_usr_postal_code_bil = null;

		prmNm = "s_role_types";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.s_role_types = sutils.split(val, "~");
		}

		prmNm = "s_ftn_ext_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.s_ftn_ext_id = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		prmNm = "s_search_role";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && !val.matches("[0-9]+")) {
			dob.dbusg.s_rol_ext_id = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		prmNm = "s_instr";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.s_instr = dbUtils.unicodeFrom(val, clientEnc,
					dob.encoding, bMultiPart);
		}

		prmNm = "s_grade";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_grade = Long.parseLong(val);

		prmNm = "s_idc_int";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_idc_int = Long.parseLong(val);

		prmNm = "s_idc_fcs";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_idc_fcs = Long.parseLong(val);

		prmNm = "s_usg_ent_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.s_usg_ent_id_lst = sutils.split(val, "~");
		}

		prmNm = "s_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_status = sutils.split(val, "~");

		prmNm = "s_timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.trim().length() != 0)
			dob.dbusg.s_timestamp = Timestamp.valueOf(val);
		// else
		// dob.dbusg.s_timestamp = null;

		prmNm = "s_sort_by";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_sort_by = val;
		else
			dob.dbusg.s_sort_by = null;

		prmNm = "s_order_by";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_order_by = val;
		else
			dob.dbusg.s_order_by = null;

		prmNm = "s_itm_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_itm_id = Long.parseLong(val);

		prmNm = "s_search_enrolled";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_search_enrolled = val;
		else
			dob.dbusg.s_search_enrolled = null;

		prmNm = "s_target_usr_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_target_usr_ent_id = Long.parseLong(val);
		else
			dob.dbusg.s_target_usr_ent_id = 0;

		prmNm = "s_appr_ent_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_appr_ent_id = Long.parseLong(val);
		else
			dob.dbusg.s_appr_ent_id = 0;

		prmNm = "s_appr_rol_ext_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_appr_rol_ext_id = val;
		else
			dob.dbusg.s_appr_rol_ext_id = null;

		prmNm = "s_tcr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.dbusg.s_tcr_id = Long.parseLong(val);
		else
			dob.dbusg.s_tcr_id = 0;
		prmNm = "s_itm_code";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.s_itm_code = val;
		}
		prmNm = "s_itm_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			val = dbUtils.unicodeFrom(val, clientEnc, static_env.ENCODING,
					false);
			dob.dbusg.s_itm_title = val;
		}
		prmNm = "s_is_whole_word_match_itm_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.s_is_whole_word_match_itm_title = Boolean.valueOf(val)
					.booleanValue();
		}
		prmNm = "s_ils_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			val = dbUtils.unicodeFrom(val, clientEnc, static_env.ENCODING,
					false);
			dob.dbusg.s_ils_title = val;
		}
		prmNm = "s_is_whole_word_match_ils_title";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.s_is_whole_word_match_ils_title = Boolean.valueOf(val)
					.booleanValue();
		}

		Enumeration attr_names = request.getParameterNames();
		while (attr_names.hasMoreElements()) {
			String att_name = (String) attr_names.nextElement();
			if (att_name.startsWith("s_ext_")) {
				dob.vColName.addElement(att_name);
				if (att_name.endsWith("_text")) {
					dob.vColType.addElement(DbTable.COL_TYPE_STRING);
					dob.vColValue.addElement(dbUtils.unicodeFrom(
							request.getParameterValues(att_name)[0], clientEnc,
							dob.encoding, bMultiPart));
				}
				if (att_name.endsWith("_fr")) {
					dob.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
					if (request.getParameterValues(att_name)[0].length() > 0) {
						dob.vColValue.addElement(Timestamp.valueOf(request
								.getParameterValues(att_name)[0]));
					} else {
						dob.vColValue.addElement("");
					}
				}
				if (att_name.endsWith("_to")) {
					dob.vColType.addElement(DbTable.COL_TYPE_TIMESTAMP);
					if (request.getParameterValues(att_name)[0].length() > 0) {
						dob.vColValue.addElement(Timestamp.valueOf(request
								.getParameterValues(att_name)[0]));
					} else {
						dob.vColValue.addElement("");
					}
				}
				if (att_name.endsWith("_select")) {
					dob.vColType.addElement(DbTable.COL_TYPE_STRING);
					dob.vColValue.addElement(request
							.getParameterValues(att_name)[0]);
				}
				if (att_name.endsWith("_check")) {
					dob.vColType.addElement(DbTable.COL_TYPE_STRING);
					String[] checkValue = request.getParameterValues(att_name);
					dob.vColValue.addElement(checkValue);
				}
			}
		}

		prmNm = "remove_ils_instr";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.dbusg.remove_ils_instr = Boolean.valueOf(val).booleanValue();
		}
		prmNm = "rol_ext_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			val = dbUtils.unicodeFrom(val, clientEnc, static_env.ENCODING,
					false);
			dob.rol_ext_id = val;
		} else
			dob.rol_ext_id = null;

		// add for new user_search ---- start
		// by richard
		prmNm = "usr_srh_col_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.dbusg.usr_srh_col_lst = dbUtils.split(dbUtils.unicodeFrom(
						val, clientEnc, dob.encoding, bMultiPart), "~");
			} else {
				dob.dbusg.usr_srh_col_lst = new String[] {};
			}
		} else
			dob.dbusg.usr_srh_col_lst = null;

		prmNm = "usr_srh_value_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null) {
			if (val.length() != 0) {
				dob.dbusg.usr_srh_value_lst = dbUtils.split(dbUtils
						.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart),
						"~");
			} else {
				dob.dbusg.usr_srh_value_lst = new String[] {};
			}
		} else
			dob.dbusg.usr_srh_value_lst = null;
		// add for new user_search ---- end

		/* gen_tree param */
		prmNm = "create_tree";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.isCreateTree = true;
		else
			dob.tree.isCreateTree = false;

		prmNm = "global_cat";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && val.equalsIgnoreCase("true")) {
			dob.tree.hasGlobalCat_curTree = true;
		} else {
			dob.tree.hasGlobalCat_curTree = false;
		}

		prmNm = "tree_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.tree_type = val;
		else
			dob.tree.tree_type = null;

		prmNm = "show_bil_nickname";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equals("1")) {
				dob.tree.show_bil_nickname = true;
			} else {
				dob.tree.show_bil_nickname = false;
			}
		} else {
			dob.tree.show_bil_nickname = false;
		}

		// used in user classification
		prmNm = "tree_subtype";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.tree_subtype = val;
		else
			dob.tree.tree_subtype = null;

		prmNm = "flag";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("Y"))
				dob.tree.flag = true;
		}

		prmNm = "get_supervise_group";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equals("1")) {
				dob.tree.get_supervise_group = true;
			} else {
				dob.tree.get_supervise_group = false;
			}
		} else {
			dob.tree.get_supervise_group = false;
		}

		prmNm = "get_direct_supervise";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equals("1")) {
				dob.tree.get_direct_supervise = true;
			} else {
				dob.tree.get_direct_supervise = false;
			}
		} else {
			dob.tree.get_direct_supervise = false;
		}

		/*
		 * prmNm = "tree_sub_type_lst"; val = (bMultiPart) ?
		 * multi.getParameter(prmNm) : request.getParameter(prmNm); if (val !=
		 * null && val.length() != 0) dob.tree.tree_sub_type_lst =
		 * dbUtils.split(val, "~"); else dob.tree.tree_sub_type_lst = null;
		 */

		prmNm = "node_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			try {
				dob.tree.node_id = Long.parseLong(val);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				dob.tree.virtual_catalog = val;
			}

		prmNm = "sgp_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.tree.sgp_id = Long.parseLong(val);
		}

		prmNm = "itm_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.tree.itm_id = Long.parseLong(val);
		}

		prmNm = "pick_method";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.pick_method = Integer.parseInt(val);

		prmNm = "ftn_ext_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.ftn_ext_id = val;
		else
			dob.tree.ftn_ext_id = null;

		prmNm = "search_rol_ext_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.rol_ext_id = val;
		else
			dob.tree.rol_ext_id = null;

		prmNm = "auto_pick";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.auto_pick = Integer.parseInt(val);

		prmNm = "pick_leave";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.pick_leave = Integer.parseInt(val);

		prmNm = "pick_root";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.pick_root = Integer.parseInt(val);

		prmNm = "complusory_tree";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.complusory_tree = Integer.parseInt(val);

		prmNm = "self_group";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("true") || val.equalsIgnoreCase("yes"))
				dob.self_group = true;
		}

		prmNm = "res_read_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && (Long.parseLong(val) == 1))
			dob.res_read_ind = true;

		prmNm = "res_preview_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && (Long.parseLong(val) == 1))
			dob.res_preview_ind = true;

		prmNm = "node_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.node_type = val;
		else
			dob.tree.node_type = null;

		prmNm = "node_id_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.node_id_lst = val;
		else
			dob.node_id_lst = null;

		prmNm = "node_type_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.node_type_lst = val;
		else
			dob.node_type_lst = null;

		prmNm = "js";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.js = val;
		else
			dob.tree.js = null;

		prmNm = "item_type_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tree.catalogItemTypes = dbUtils.split(val, "~");
		else
			dob.tree.catalogItemTypes = null;

		prmNm = "parent_tcr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.tree.parent_tcr_id = Long.parseLong(val);
		}

		prmNm = "filter_user_group";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && (Long.parseLong(val) == 1))
			dob.tree.filter_user_group = true;

		prmNm = "slk_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.slk_id = Long.parseLong(val);

		prmNm = "use_tkh_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.use_tkh_ind = (new Boolean(val)).booleanValue();
		}

		// pagination
		prmNm = "cur_page";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			dob.cwPage.curPage = Integer.parseInt(val);
		} else
			dob.cwPage.curPage = 0;

		prmNm = "page_size";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			dob.cwPage.pageSize = Integer.parseInt(val);
		} else
			dob.cwPage.pageSize = 0;

		prmNm = "sort_col";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			dob.cwPage.sortCol = cwPagination.esc4SortSql(val);
		}

		prmNm = "sort_order";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			dob.cwPage.sortOrder = cwPagination.esc4SortSql(val);
		}

		prmNm = "timestamp";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() > 0) {
			dob.cwPage.ts = Timestamp.valueOf(val);
			dob.dbusg.s_timestamp = Timestamp.valueOf(val);
		} else
			dob.cwPage.ts = null;

		if (dob.cwPage.sortCol != null && dob.cwPage.sortCol.length() != 0) {
			if (dob.cwPage.sortCol.equalsIgnoreCase(dbUtils.SORT_BY_TITLE)) {
				dob.cwPage.sortCol = "res_title";
			} else if (dob.cwPage.sortCol
					.equalsIgnoreCase(dbUtils.SORT_BY_TYPE)) {
				dob.cwPage.sortCol = "res_type";
			} else if (dob.cwPage.sortCol
					.equalsIgnoreCase(dbUtils.SORT_BY_AUTHOR)) {
				dob.cwPage.sortCol = "res_usr_id_owner";
			} else if (dob.cwPage.sortCol
					.equalsIgnoreCase(dbUtils.SORT_BY_TIME)) {
				dob.cwPage.sortCol = "res_upd_date";
			} else if (dob.cwPage.sortCol.equalsIgnoreCase(dbUtils.SORT_BY_ID)) {
				dob.cwPage.sortCol = "res_id";
			} else if (dob.cwPage.sortCol
					.equalsIgnoreCase(dbUtils.SORT_BY_STATUS)) {
				dob.cwPage.sortCol = "res_status";
			}
		}

		prmNm = "sel_mod_id_list";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.sel_mod_id_list = dbUtils.split(val, "~");
		} else {
			dob.sel_mod_id_list = null;
		}

		prmNm = "clo_src_itm_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.clo_src_itm_id = Integer.parseInt(val);
		} else {
			dob.clo_src_itm_id = 0;
		}

		prmNm = "clo_tag_cos_res_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.clo_tag_cos_res_id = Integer.parseInt(val);
		} else {
			dob.clo_tag_cos_res_id = 0;
		}

		// poster management

		if (bMultiPart && multi.getFilesystemName("sp_media_file") != null)
			dob.poster.sp_media_file = multi.getFilesystemName("sp_media_file");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_sp_media01") != null)
			dob.poster.sp_media_file = multi
					.getFilesystemName("tmp_sp_media01");
		else {
			prmNm = "sp_media_file";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.sp_media_file = val.trim();
		}

		prmNm = "rdo_sp_media";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.sp_keep_media = true;
		}

		prmNm = "sp_url";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_url = val;

		prmNm = "sp_status";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_status = val;

		if (bMultiPart && multi.getFilesystemName("sp_media_file_1") != null)
			dob.poster.sp_media_file1 = multi
					.getFilesystemName("sp_media_file_1");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_sp_media01_1") != null)
			dob.poster.sp_media_file1 = multi
					.getFilesystemName("tmp_sp_media01_1");
		else {
			prmNm = "sp_media_file_1";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.sp_media_file1 = val.trim();
		}

		prmNm = "rdo_sp_media01_1";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.sp_keep_media1 = true;
		}

		prmNm = "sp_url_1";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_url1 = val;

		prmNm = "sp_status_1";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_status1 = val;

		if (bMultiPart && multi.getFilesystemName("sp_media_file_2") != null)
			dob.poster.sp_media_file2 = multi
					.getFilesystemName("sp_media_file_2");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_sp_media01_2") != null)
			dob.poster.sp_media_file2 = multi
					.getFilesystemName("tmp_sp_media01_2");
		else {
			prmNm = "sp_media_file_2";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.sp_media_file2 = val.trim();
		}

		prmNm = "rdo_sp_media01_2";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.sp_keep_media2 = true;
		}

		prmNm = "sp_url_2";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_url2 = val;

		prmNm = "sp_status_2";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_status2 = val;

		if (bMultiPart && multi.getFilesystemName("sp_media_file_3") != null)
			dob.poster.sp_media_file3 = multi
					.getFilesystemName("sp_media_file_3");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_sp_media01_3") != null)
			dob.poster.sp_media_file3 = multi
					.getFilesystemName("tmp_sp_media01_3");
		else {
			prmNm = "sp_media_file_3";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.sp_media_file3 = val.trim();
		}

		prmNm = "rdo_sp_media01_3";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.sp_keep_media3 = true;
		}

		prmNm = "sp_url_3";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_url3 = val;

		prmNm = "sp_status_3";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_status3 = val;

		if (bMultiPart && multi.getFilesystemName("sp_media_file_4") != null)
			dob.poster.sp_media_file4 = multi
					.getFilesystemName("sp_media_file_4");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_sp_media01_4") != null)
			dob.poster.sp_media_file4 = multi
					.getFilesystemName("tmp_sp_media01_4");
		else {
			prmNm = "sp_media_file_4";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.sp_media_file4 = val.trim();
		}

		prmNm = "rdo_sp_media01_4";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.sp_keep_media4 = true;
		}

		prmNm = "sp_url_4";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_url4 = val;

		prmNm = "sp_status_4";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		dob.poster.sp_status4 = val;
		// ===============================登录背景图片参数处理==================================
		prmNm = "show_login_header_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("true")) {
			dob.poster.sp_login_show_header_ind = true;
		} else {
			dob.poster.sp_login_show_header_ind = false;
		}

		prmNm = "show_all_footer_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("true")) {
			dob.poster.sp_all_show_footer_ind = true;
		} else {
			dob.poster.sp_all_show_footer_ind = false;
		}

		if (bMultiPart && multi.getFilesystemName("login_bg_file1") != null)
			dob.poster.login_bg_file1 = multi
					.getFilesystemName("login_bg_file1");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_login_bg_file1") != null)
			dob.poster.login_bg_file1 = multi
					.getFilesystemName("tmp_login_bg_file1");
		else {
			prmNm = "login_bg_file1";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.login_bg_file1 = val.trim();
		}
		prmNm = "rdo_login_bg1";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.islogin_bg_file1 = true;
		}

		if (bMultiPart && multi.getFilesystemName("login_bg_file2") != null)
			dob.poster.login_bg_file2 = multi
					.getFilesystemName("login_bg_file2");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_login_bg_file2") != null)
			dob.poster.login_bg_file2 = multi
					.getFilesystemName("tmp_login_bg_file2");
		else {
			prmNm = "login_bg_file2";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.login_bg_file2 = val.trim();
		}
		prmNm = "rdo_login_bg2";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.islogin_bg_file2 = true;
		}

		if (bMultiPart && multi.getFilesystemName("login_bg_file3") != null)
			dob.poster.login_bg_file3 = multi
					.getFilesystemName("login_bg_file3");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_login_bg_file3") != null)
			dob.poster.login_bg_file3 = multi
					.getFilesystemName("tmp_login_bg_file3");
		else {
			prmNm = "login_bg_file3";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.login_bg_file3 = val.trim();
		}
		prmNm = "rdo_login_bg3";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.islogin_bg_file3 = true;
		}

		if (bMultiPart && multi.getFilesystemName("login_bg_file4") != null)
			dob.poster.login_bg_file4 = multi
					.getFilesystemName("login_bg_file4");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_login_bg_file4") != null)
			dob.poster.login_bg_file4 = multi
					.getFilesystemName("tmp_login_bg_file4");
		else {
			prmNm = "login_bg_file4";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.login_bg_file4 = val.trim();
		}
		prmNm = "rdo_login_bg4";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.islogin_bg_file4 = true;
		}

		if (bMultiPart && multi.getFilesystemName("login_bg_file5") != null)
			dob.poster.login_bg_file5 = multi
					.getFilesystemName("login_bg_file5");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_login_bg_file5") != null)
			dob.poster.login_bg_file5 = multi
					.getFilesystemName("tmp_login_bg_file5");
		else {
			prmNm = "login_bg_file5";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.login_bg_file5 = val.trim();
		}
		prmNm = "rdo_login_bg5";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.islogin_bg_file5 = true;
		}
		// bill.lai 2016-01-27
		/*
		 * 获取登陆背景类型 pic：图片轮播 VOD：图片视频
		 */
		prmNm = "login_bg_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && !val.equals("")) {
			dob.poster.login_bg_type = val;
		} else {
			dob.poster.login_bg_type = "PIC";
		}

		if (bMultiPart && multi.getFilesystemName("login_bg_video") != null)
			dob.poster.login_bg_video = multi
					.getFilesystemName("login_bg_video");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_login_bg_video") != null)
			dob.poster.login_bg_video = multi
					.getFilesystemName("tmp_login_bg_video");
		else {
			prmNm = "login_bg_video";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.login_bg_video = val.trim();
		}
		prmNm = "rdo_login_video";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.islogin_bg_video = true;
		}

		// ========================== welcome_text
		// ======================================================

		if (bMultiPart && multi.getFilesystemName("sp_welcome_word") != null)
			dob.poster.sp_welcome_word = multi
					.getFilesystemName("sp_welcome_word");
		else {
			prmNm = "sp_welcome_word";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.sp_welcome_word = val.trim();
		}

		if (bMultiPart && multi.getFilesystemName("mb_welcome_word") != null)
			dob.poster.mb_welcome_word = multi
					.getFilesystemName("mb_welcome_word");
		else {
			prmNm = "mb_welcome_word";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.mb_welcome_word = val.trim();
		}

		// ===============================app指引图片参数处理==================================

		if (bMultiPart && multi.getFilesystemName("guide_file1") != null)
			dob.poster.guide_file1 = multi.getFilesystemName("guide_file1");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_guide_file1") != null)
			dob.poster.guide_file1 = multi.getFilesystemName("tmp_guide_file1");
		else {
			prmNm = "guide_file1";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.guide_file1 = val.trim();
		}
		prmNm = "rdo_guide_bg1";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.isGuide_file1 = true;
		}

		if (bMultiPart && multi.getFilesystemName("guide_file2") != null)
			dob.poster.guide_file2 = multi.getFilesystemName("guide_file2");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_guide_file2") != null)
			dob.poster.guide_file2 = multi.getFilesystemName("tmp_guide_file2");
		else {
			prmNm = "guide_file2";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.guide_file2 = val.trim();
		}
		prmNm = "rdo_guide_bg2";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.isGuide_file2 = true;
		}

		if (bMultiPart && multi.getFilesystemName("guide_file3") != null)
			dob.poster.guide_file3 = multi.getFilesystemName("guide_file3");
		else if (bMultiPart
				&& multi.getFilesystemName("tmp_guide_file3") != null)
			dob.poster.guide_file3 = multi.getFilesystemName("tmp_guide_file3");
		else {
			prmNm = "guide_file3";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.guide_file3 = val.trim();
		}
		prmNm = "rdo_guide_bg3";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.isGuide_file3 = true;
		}

		// ==========================================================================
		if (bMultiPart && multi.getFilesystemName("sp_logo_file_cn") != null) {
			dob.poster.sp_logo_file_cn = multi
					.getFilesystemName("sp_logo_file_cn");
		} else if (bMultiPart
				&& multi.getFilesystemName("tmp_sp_logo_cn") != null) {
			dob.poster.sp_logo_file_cn = multi
					.getFilesystemName("tmp_sp_logo_cn");
		} else {
			prmNm = "sp_logo_file_cn";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.sp_logo_file_cn = val.trim();
		}

		prmNm = "rdo_sp_logo_cn";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.sp_keep_logo_cn = true;
		}

		if (bMultiPart && multi.getFilesystemName("sp_logo_file_hk") != null) {
			dob.poster.sp_logo_file_hk = multi
					.getFilesystemName("sp_logo_file_hk");
		} else if (bMultiPart
				&& multi.getFilesystemName("tmp_sp_logo_hk") != null) {
			dob.poster.sp_logo_file_hk = multi
					.getFilesystemName("tmp_sp_logo_hk");
		} else {
			prmNm = "sp_logo_file_hk";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.sp_logo_file_hk = val.trim();
		}

		prmNm = "rdo_sp_logo_hk";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.sp_keep_logo_hk = true;
		}

		if (bMultiPart && multi.getFilesystemName("sp_logo_file_us") != null) {
			dob.poster.sp_logo_file_us = multi
					.getFilesystemName("sp_logo_file_us");
		} else if (bMultiPart
				&& multi.getFilesystemName("tmp_sp_logo_us") != null) {
			dob.poster.sp_logo_file_us = multi
					.getFilesystemName("tmp_sp_logo_us");
		} else {
			prmNm = "sp_logo_file_us";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.poster.sp_logo_file_us = val.trim();
		}

		prmNm = "rdo_sp_logo_us";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.equals("1")) {
			dob.poster.sp_keep_logo_us = true;
		}
		
		prmNm = "tcr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.tcr_id = Long.parseLong(val);
		else
			dob.tcr_id = 0L;

		/*
		 * prmNm = "completed"; val = (bMultiPart) ? multi.getParameter(prmNm) :
		 * request.getParameter(prmNm); if (val != null && val.length() != 0) {
		 * dob.dbacp.mod_status = val; }
		 * 
		 * prmNm = "unload"; val = (bMultiPart) ? multi.getParameter(prmNm) :
		 * request.getParameter(prmNm); if (val != null && val.length() != 0) {
		 * System.out.println("unload = " + val); if (val.equalsIgnoreCase("Y"))
		 * dob.dbacp.mod_unload = true; else dob.dbacp.mod_unload = false; }
		 */

		// for test results, loop to get all interactions, assume less than 1000
		String atm_int_res_id_n = "atm_int_res_id";
		String atm_flag_n = "atm_flag";
		String atm_int_order_n_m = "atm_int_order";
		String atm_response_n_m = "atm_response";
		String atm_response_ext_n_m = "atm_response_ext";
		qdbQueInstance q = null;
		dbInteraction intr = null;
		dbProgressAttempt atm = null;
		for (int n = 1; n <= 1000; n++) {
			prmNm = atm_int_res_id_n + "_" + n;
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val == null || val.length() == 0)
				break;
			q = new qdbQueInstance();
			q.dbque.que_res_id = Long.parseLong(val);
			q.dbque.res_id = q.dbque.que_res_id;
			// flag
			prmNm = atm_flag_n + "_" + n;
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() > 0) {
				if (val.equalsIgnoreCase("true"))
					q.flag_ind = true;
				else
					q.flag_ind = false;
			} else
				q.flag_ind = false;

			// n_m
			for (int m = 1; m <= 100; m++) {
				prmNm = atm_int_order_n_m + "_" + n + "_" + m;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val == null || val.length() == 0)
					break;

				intr = new dbInteraction();
				intr.int_res_id = q.dbque.que_res_id;
				intr.int_order = Integer.parseInt(val);
				q.dbque.ints.addElement(intr);

				prmNm = atm_response_n_m + "_" + n + "_" + m;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val == null)
					break;

				atm = new dbProgressAttempt();
				atm.atm_int_res_id = q.dbque.que_res_id;
				atm.atm_int_order = intr.int_order;

				atm.atm_response_bil = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);
				atm.atm_response_bil = atm.atm_response_bil.trim();

				// Matching type of response
				atm.atm_responses = dbUtils.split(val, "~");

				if (atm.atm_responses != null) {
					for (int k = 0; k < atm.atm_responses.length; k++) {
						atm.atm_responses[k] = dbUtils.unicodeFrom(
								atm.atm_responses[k], clientEnc, dob.encoding,
								bMultiPart);
						atm.atm_responses[k] = atm.atm_responses[k].trim();
					}
				}

				// response of MC in other option
				prmNm = atm_response_ext_n_m + "_" + n + "_" + m;
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() > 0) {
					atm.atm_response_bil_ext = dbUtils.unicodeFrom(val,
							clientEnc, dob.encoding, bMultiPart);
				}

				q.atms.addElement(atm);
			}

			dob.tst.qdbQues.addElement(q);
		}

		int MAX_OPT = 100;
		DELIMITER = "[|]";
		int inter_num_ = 0;
		int source_num_ = 0;
		int opt_num_ = 0;
		String[] optNum = null;

		prmNm = "inter_num_";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);

		if (val != null && val.length() != 0) {
			inter_num_ = Integer.parseInt(val);

			optNum = new String[MAX_OPT];
			prmNm = "inter_opt_num_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				optNum = dbUtils.split(val, DELIMITER);
			}

			for (int i = 0; i < optNum.length; i++) {
				int num = 0;
				if (optNum[i] != null && optNum[i].length() > 0)
					num = Integer.parseInt(optNum[i]);
				if (num > opt_num_)
					opt_num_ = num;
			}

			prmNm = "num_source_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				source_num_ = Integer.parseInt(val);
			else
				source_num_ = 0;

			if (source_num_ == 0)
				dob.extque = new extendQue(4, inter_num_, opt_num_, 0, 0);
			else
				dob.extque = new extendQue(4, inter_num_, opt_num_,
						source_num_, inter_num_);

			prmNm = "que_body_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.extque.cont = dbUtils.esc4XML(dbUtils.unicodeFrom(val,
						clientEnc, dob.encoding, bMultiPart));

			prmNm = "que_hint_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0 && val.equalsIgnoreCase("Y"))
				dob.extque.cont_hint = true;
			else
				dob.extque.cont_hint = false;

			/*
			 * prmNm = "que_media_"; val = (bMultiPart) ?
			 * multi.getParameter(prmNm) : request.getParameter(prmNm); if (
			 * val!= null && val.length()!= 0 ) dob.extque.cont_pic = val.trim()
			 * ;
			 */

			// WaiLun : Get the filename of the uploaded file
			if (bMultiPart && multi.getFilesystemName("que_media") != null)
				dob.extque.cont_pic = multi.getFilesystemName("que_media");
			else if (bMultiPart
					&& multi.getFilesystemName("tmp_que_media01") != null)
				dob.extque.cont_pic = multi
						.getFilesystemName("tmp_que_media01");
			else {
				prmNm = "que_media_";
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0)
					dob.extque.cont_pic = val.trim();
			}

			prmNm = "rdo_que_media01";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				if (!val.trim().equalsIgnoreCase("1")) {
					dob.del_que_media = true;
				}
			}
			// a question can has a maximum of 3 attachments
			dob.extque.vFiles = new Vector();
			if (bMultiPart && multi.getFilesystemName("que_att_1") != null) {
				dob.extque.vFiles.addElement(multi
						.getFilesystemName("que_att_1"));
			} else {
				prmNm = "tmp_que_att_1";
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0)
					dob.extque.vFiles.addElement(val.trim());
			}

			if (bMultiPart && multi.getFilesystemName("que_att_2") != null) {
				dob.extque.vFiles.addElement(multi
						.getFilesystemName("que_att_2"));
			} else {
				prmNm = "tmp_que_att_2";
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0)
					dob.extque.vFiles.addElement(val.trim());
			}

			if (bMultiPart && multi.getFilesystemName("que_att_3") != null) {
				dob.extque.vFiles.addElement(multi
						.getFilesystemName("que_att_3"));
			} else {
				prmNm = "tmp_que_att_3";
				val = (bMultiPart) ? multi.getParameter(prmNm) : request
						.getParameter(prmNm);
				if (val != null && val.length() != 0)
					dob.extque.vFiles.addElement(val.trim());
			}

			prmNm = "que_html_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0 && val.equalsIgnoreCase("Y"))
				dob.extque.cont_html = true;
			else
				dob.extque.cont_html = false;

			prmNm = "que_diff_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.extque.diff = Integer.parseInt(val);

			prmNm = "que_dur_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.extque.dur = Float.valueOf(val).floatValue();

			prmNm = "que_folder_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.extque.folder = val;

			prmNm = "que_status_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.extque.onoff = val;

			prmNm = "que_title_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.extque.title = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);

			prmNm = "que_desc_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				dob.extque.desc = dbUtils.unicodeFrom(val, clientEnc,
						dob.encoding, bMultiPart);

			String[] interType = new String[MAX_OPT];
			String[] interShuffle = new String[MAX_OPT];
			String[] interLength = new String[MAX_OPT];
			String[] interLogic = new String[MAX_OPT];
			String[] optBody = new String[MAX_OPT];
			String[] optMedia = new String[MAX_OPT];
			String[] optScore = new String[MAX_OPT];
			String[] optCond = new String[MAX_OPT];
			String[] optExp = new String[MAX_OPT];
			String[] optHtml = new String[MAX_OPT];
			String[] optCase = new String[MAX_OPT];
			String[] optSpace = new String[MAX_OPT];
			String[] optType = new String[MAX_OPT];

			// Matching specific
			String[] srcMedia = new String[MAX_OPT];
			String[] tgtMedia = new String[MAX_OPT];
			String[] srcText = new String[MAX_OPT];
			String[] tgtText = new String[MAX_OPT];
			int mediaW = 0;
			int mediaH = 0;
			// Choice specific
			boolean hasOtherOption = false;

			prmNm = "inter_type_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				interType = dbUtils.split(val, DELIMITER);

			prmNm = "inter_shuffle_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				interShuffle = dbUtils.split(val, DELIMITER);

			prmNm = "inter_logic_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				interLogic = dbUtils.split(val, DELIMITER);

			prmNm = "inter_opt_body_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				optBody = dbUtils.split(val, DELIMITER);
			}

			prmNm = "inter_opt_media_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				optMedia = dbUtils.split(val, DELIMITER);
				// WaiLun : if some filename is converted, check the original
				// filename and exchange
				if (sess != null
						&& sess.getAttribute(ORIGINAL_FILENAME) != null)
					for (int i = 0; i < optMedia.length; i++)
						optMedia[i] = convertFilename(optMedia[i], sess);

			}

			prmNm = "inter_opt_score_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				optScore = dbUtils.split(val, DELIMITER);

			prmNm = "inter_opt_cond_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				optCond = dbUtils.split(val, DELIMITER);

			prmNm = "inter_opt_exp_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				optExp = dbUtils.split(val, DELIMITER);
			}

			prmNm = "inter_opt_html_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				optHtml = dbUtils.split(val, DELIMITER);

			prmNm = "inter_opt_type_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				optType = dbUtils.split(val, DELIMITER);

			// Fill in the Blank specific
			prmNm = "inter_length_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				interLength = dbUtils.split(val, DELIMITER);

			prmNm = "inter_opt_case_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				optCase = dbUtils.split(val, DELIMITER);

			prmNm = "inter_opt_space_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				optSpace = dbUtils.split(val, DELIMITER);

			// Matching specific
			prmNm = "media_file_width_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				mediaW = Integer.parseInt(val);

			dob.extque.media_width = mediaW;

			prmNm = "media_file_height_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				mediaH = Integer.parseInt(val);

			dob.extque.media_height = mediaH;

			prmNm = "source_text_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				srcText = dbUtils.split(val, DELIMITER);
			else
				srcText = null;

			prmNm = "source_media_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				srcMedia = dbUtils.split(val, DELIMITER);
				// WaiLun : if some filename is converted, check the original
				// filename and exchange
				if (sess != null
						&& sess.getAttribute(ORIGINAL_FILENAME) != null)
					for (int i = 0; i < srcMedia.length; i++)
						srcMedia[i] = convertFilename(srcMedia[i], sess);
			} else
				srcMedia = null;

			prmNm = "target_text_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0)
				tgtText = dbUtils.split(val, DELIMITER);
			else
				tgtText = null;

			prmNm = "target_media_";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				tgtMedia = dbUtils.split(val, DELIMITER);
				// WaiLun : if some filename is converted, check the original
				// filename and exchange
				if (sess != null
						&& sess.getAttribute(ORIGINAL_FILENAME) != null)
					for (int i = 0; i < tgtMedia.length; i++)
						tgtMedia[i] = convertFilename(tgtMedia[i], sess);

			} else
				tgtMedia = null;

			prmNm = "has_other_option";
			val = (bMultiPart) ? multi.getParameter(prmNm) : request
					.getParameter(prmNm);
			if (val != null && val.length() != 0) {
				hasOtherOption = Boolean.valueOf(val).booleanValue();
			}

			if (srcText != null && srcText.length > 0
					&& (srcText[0].length() > 0 || srcMedia[0].length() > 0)) {
				for (int i = 0; i < srcText.length; i++) {

					if (srcText != null && srcText.length > i
							&& srcText[i] != null) {
						dob.extque.src_obj[i].text = dbUtils.esc4XML(dbUtils
								.unicodeFrom(srcText[i], clientEnc,
										dob.encoding, bMultiPart));
					}

					if (srcMedia != null && srcMedia.length > i
							&& srcMedia[i] != null) {
						dob.extque.src_obj[i].media = srcMedia[i];
					}
				}
			}

			if (tgtText != null && tgtText.length > 0
					&& (tgtText[0].length() > 0 || tgtMedia[0].length() > 0)) {
				for (int i = 0; i < tgtText.length; i++) {
					if (tgtText != null && tgtText.length > i
							&& tgtText[i] != null) {
						dob.extque.tgt_obj[i].text = dbUtils.esc4XML(dbUtils
								.unicodeFrom(tgtText[i], clientEnc,
										dob.encoding, bMultiPart));
					}

					if (tgtMedia != null && tgtMedia.length > i
							&& tgtMedia[i] != null) {
						dob.extque.tgt_obj[i].media = tgtMedia[i];
					}
				}
			}

			int i = 0, j = 0, k = 0;
			String interaction = "";

			// System.out.println(" Deal with iteraction ...");

			for (i = 0; i < inter_num_; i++) {
				interaction = interType[i];
				dob.extque.inter[i].type = interaction;

				if (interaction.equalsIgnoreCase(dbQuestion.QUE_TYPE_MULTI)
						|| interaction
								.equalsIgnoreCase(dbQuestion.QUE_TYPE_TRUEFALSE)) {
					dob.extque.inter[i].shuffle = interShuffle[i];
					dob.extque.inter[i].logic = interLogic[i];

				} else if (interaction
						.equalsIgnoreCase(dbQuestion.QUE_TYPE_MATCHING)) {

				} else if (interaction
						.equalsIgnoreCase(dbQuestion.QUE_TYPE_FILLBLANK)
						|| interaction
								.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)
						|| interaction
								.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
					if (interLength[i] != null)
						dob.extque.inter[i].att_len = Integer
								.parseInt(interLength[i]);
					else
						dob.extque.inter[i].att_len = 0;
				} else if (interaction
						.equalsIgnoreCase(dbQuestion.QUE_TYPE_TYPING)) {

				}

				int num = Integer.parseInt(optNum[i]);
				/*
				 * System.out.println("optBody  : " + optBody.length);
				 * System.out.println("optMedia  : " + optMedia.length);
				 * System.out.println("optCase  : " + optCase.length);
				 * System.out.println("optSpace  : " + optSpace.length);
				 * System.out.println("optExp : " + optExp.length);
				 * System.out.println("optCond  : " + optCond.length);
				 * 
				 * System.out.println("option number  : " + num);
				 */
				for (j = 0; j < num; j++) {
					if (interaction.equalsIgnoreCase(dbQuestion.QUE_TYPE_MULTI)) {
						if (optHtml[k] != null
								&& optHtml[k].equalsIgnoreCase("Y"))
							dob.extque.inter[i].opt[j].cont_html = true;
						else
							dob.extque.inter[i].opt[j].cont_html = false;

						if (optMedia != null && k < optMedia.length
								&& optMedia[k] != null) {
							dob.extque.inter[i].opt[j].cont_pic = optMedia[k]
									.trim();
						}
					} else if (interaction
							.equalsIgnoreCase(dbQuestion.QUE_TYPE_MATCHING)) {

					} else if (interaction
							.equalsIgnoreCase(dbQuestion.QUE_TYPE_FILLBLANK)) {
						if (optCase != null & k < optCase.length
								&& optCase[k] != null)
							dob.extque.inter[i].opt[j].case_sense = optCase[k]
									.trim();
						if (optSpace != null & k < optSpace.length
								&& optSpace[k] != null)
							dob.extque.inter[i].opt[j].spc_sense = optSpace[k]
									.trim();
						if (optType != null & k < optType.length
								&& optType[k] != null)
							dob.extque.inter[i].opt[j].type = optType[k].trim();
						if (optCond != null & k < optCond.length
								&& optCond[k] != null)
							dob.extque.inter[i].opt[j].cond = optCond[k].trim();
					} else if (interaction
							.equalsIgnoreCase(dbQuestion.QUE_TYPE_TYPING)) {

					} else if (interaction
							.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY)
							|| interaction
									.equalsIgnoreCase(dbQuestion.QUE_TYPE_ESSAY_2)) {
						if (optType != null & k < optType.length
								&& optType[k] != null)
							dob.extque.inter[i].opt[j].type = optType[k].trim();
						if (optCond != null & k < optCond.length
								&& optCond[k] != null)
							dob.extque.inter[i].opt[j].cond = optCond[k].trim();
					}

					dob.extque.inter[i].opt[j].cont = dbUtils.esc4XML(dbUtils
							.unicodeFrom(optBody[k], clientEnc, dob.encoding,
									bMultiPart));
					dob.extque.inter[i].has_other_option = hasOtherOption;

					if (optScore != null & k < optScore.length
							&& optScore[k] != null)
						dob.extque.inter[i].opt[j].score = Integer
								.parseInt(optScore[k]);
					else
						dob.extque.inter[i].opt[j].score = 0;

					if (optExp != null && k < optExp.length
							&& optExp[k] != null)
						dob.extque.inter[i].opt[j].exp = dbUtils
								.esc4XML(dbUtils.unicodeFrom(optExp[k],
										clientEnc, dob.encoding, bMultiPart));

					// System.out.println("optBody : " + optBody[k]);

					k++;
				}
			}

			// System.out.println("Assigning variables ....");
			// -- Assign all the fields to dob.dbque
			dob.dbque.res_title = dob.extque.title;
			// dob.dbque.res_desc = dob.extque.getDesc();
			if (dob.dbque.res_desc == null) {
				dob.dbque.res_desc = dob.extque.desc;
			}
			dob.dbque.res_type = dbResource.RES_TYPE_QUE;
			dob.dbque.res_duration = dob.extque.dur;
			dob.dbque.res_privilege = dob.extque.folder;
			dob.dbque.res_status = dob.extque.onoff;
			if (dob.extque.diff > 0) {
				dob.dbque.res_difficulty = dob.extque.diff;
			}
			if (dob.extque.inter != null && dob.extque.inter.length > 0) {
				dob.dbque.que_type = dob.extque.inter[0].type;
			}
			dob.dbque.que_int_count = inter_num_;
			dob.dbque.que_score = dob.extque.getScore();
			dob.dbque.que_xml = dob.extque.getBody();

			// System.out.println("assigning interaction....");

			// initilize the question interaction vector
			for (i = 1; i <= dob.dbque.que_int_count; i++) {
				dbInteraction intObj = new dbInteraction();

				// if (i > 0) {
				intObj.int_order = i;
				String xmlO = dob.extque.inter[i - 1].getOutcome();
				String xmlE = dob.extque.inter[i - 1].getExplanation();

				intObj.int_xml_outcome = xmlO;
				intObj.int_xml_explain = xmlE;
				// }

				dob.dbque.ints.addElement(intObj);
			}

		} else {
			// initilize the question interaction vector
			for (int i = 1; i <= dob.dbque.que_int_count; i++) {
				dbInteraction intObj = new dbInteraction();
				dob.dbque.ints.addElement(intObj);
			}
		}

		prmNm = "is_start_test";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("TRUE")) {
				dob.is_start_test = true;
			} else {
				dob.is_start_test = false;
			}
		} else {
			dob.is_start_test = false;
		}

		prmNm = "is_sso";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("TRUE")) {
				dob.is_sso = true;
			} else {
				dob.is_sso = false;
			}
		} else {
			dob.is_sso = false;
		}

		prmNm = "is_add_res";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("TRUE")) {
				dob.is_add_res = true;
			} else {
				dob.is_add_res = false;
			}
		} else {
			dob.is_add_res = false;
		}

		prmNm = "filter_user_group";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && (Long.parseLong(val) == 1)) {
			dob.dbusg.filter_user_group_ind = true;
		}

		// iterate the keys to process multiple values
		/*
		 * while (keys.hasMoreElements()) { key = (String) keys.nextElement();
		 * val = (bMultiPart) ? multi.getParameter(key) :
		 * request.getParameter(key);
		 * 
		 * if ( perl.match("#que_int_[a-z]+[0-9]{2}#i", key) ) { if ( val!=null
		 * ) { tmpIntFldNm =
		 * perl.substitute("s#que_int_([a-z]+)([0-9]{2})#$1#i", key);
		 * tmpIntCount =
		 * Integer.parseInt(perl.substitute("s#que_int_([a-z]+)([0-9]{2})#$2#i",
		 * key)); // Vector
		 * 
		 * 
		 * if ( tmpIntFldNm.startsWith("xml") && tmpIntCount <=
		 * dob.dbque.que_int_count ) {
		 * 
		 * dbInteraction tmpIntObj = (dbInteraction)
		 * dob.dbque.ints.elementAt(tmpIntCount);
		 * 
		 * if (tmpIntFldNm.equalsIgnoreCase("xmlo")) tmpIntObj.int_xml_outcome =
		 * dbUtils.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart); if
		 * (tmpIntFldNm.equalsIgnoreCase("xmle")) tmpIntObj.int_xml_explain =
		 * dbUtils.unicodeFrom(val, clientEnc, dob.encoding, bMultiPart); }
		 * 
		 * if ( tmpIntFldNm.equalsIgnoreCase("order") && tmpIntCount <=
		 * dob.dbque.que_int_count) { dbInteraction tmpIntObj = (dbInteraction)
		 * dob.dbque.ints.elementAt(tmpIntCount); tmpIntObj.int_order =
		 * Integer.parseInt(val); continue; }
		 * 
		 * if ( tmpIntFldNm.equalsIgnoreCase("label") && tmpIntCount <=
		 * dob.dbque.que_int_count) { dbInteraction tmpIntObj = (dbInteraction)
		 * dob.dbque.ints.elementAt(tmpIntCount); tmpIntObj.int_label = val;
		 * continue; } } // if val !=null } // if perl match interaction } //
		 * while
		 */

		prmNm = "window_name";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.window_name = val;

		prmNm = "sso_refer";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.sso_refer = val;

		prmNm = "sso_lms_url";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.sso_lms_url = val;

		prmNm = "log_type";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.log_type = val;
		else
			dob.log_type = null;

		prmNm = "last_days";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			dob.last_days = Integer.parseInt(val);
		else
			dob.last_days = 0;

		prmNm = "sys_log_start_date";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && !val.equals("0")) {

			dob.sys_log_starttime = Timestamp.valueOf(val);
		} else {
			dob.sys_log_starttime = null;
		}
		prmNm = "sys_log_end_date";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0 && !val.equals("0"))
			dob.sys_log_endtime = Timestamp.valueOf(val);
		else
			dob.sys_log_endtime = null;

		prmNm = "select_all";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0)
			if (val.equalsIgnoreCase("ALL")) {
				dob.select_all = true;
			} else {
				dob.select_all = false;
			}

		prmNm = "option_lst";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			dob.option_lst = val;
		} else {
			dob.option_lst = null;
		}

		prmNm = "remain_photo_ind";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && val.length() != 0) {
			if (val.equalsIgnoreCase("TRUE")) {
				dob.remain_photo_ind = true;
			} else {
				dob.remain_photo_ind = false;
			}
		} else {
			dob.remain_photo_ind = false;
		}

		prmNm = "usr_template_tcr_id";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && !val.equals("")) {
			dob.usr_template_tcr_id = Long.valueOf(val);
		}

		prmNm = "isMobile";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && !val.equals("")) {
			try {
				dob.isMobile = Boolean.parseBoolean(val);
			} catch (Exception e) {
			}
		}
		prmNm = "isReceipt";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && !val.equals("")) {
			try {
				dob.isReceipt = Boolean.parseBoolean(val);
			} catch (Exception e) {
			}
		}
		prmNm = "user_code";
		val = (bMultiPart) ? multi.getParameter(prmNm) : request
				.getParameter(prmNm);
		if (val != null && !val.equals(""))
			dob.user_code = val;
		else
			dob.user_code = null;
		CommonLog.info("+++++++++++++++++++++++++++++" + dob.user_code
				+ "++++++++++++++++++++++++++");

		return dob;
	}

	private void generalAsHtml(String xmlOL, PrintWriter out, dataObj dob)
			throws IOException, cwException {
		static_env.procXSLFile(xmlOL, dob.cur_stylesheet, out, dob.xsl_root);
	}

	private void generalAsHtml(String xmlOL, PrintWriter out,
			String stylesheet, String xsl_root) throws IOException, cwException {
		static_env.procXSLFile(xmlOL, stylesheet, out, xsl_root);
	}

	// message box for new module only.
	// Add module id and module title in the xml;
	private void msgBox(String title, Connection con, cwSysMessage e,
			dataObj dob, PrintWriter out, long modId, String modTitle,
			String subtype, String action, long cosId) throws IOException,
			cwException, SQLException, qdbException, cwSysMessage {
		String msg = e.getSystemMessage(dob.prof.label_lan);
		String modXML = new String();
		dbCourse dbcos = new dbCourse();
		dbcos.cos_res_id = cosId;
		dbcos.get(con);
		modXML = "<course id=\"" + cosId + "\" timestamp=\""
				+ dbcos.res_upd_date + "\"/>" + dbUtils.NEWL;
		modXML += "<module id=\"" + modId + "\" subtype=\"" + subtype;
		modXML += "\" action=\"" + action + "\">";
		modXML += dbUtils.esc4XML(modTitle) + "</module>" + dbUtils.NEWL;
		genMsgBox(title, msg, dob, out, modXML);
	}

	private void msgBox(String title, Connection con, cwSysMessage e,
			dataObj dob, PrintWriter out) throws IOException, cwException,
			SQLException {
		String msg = e.getSystemMessage(dob.prof.label_lan);
		genMsgBox(title, msg, dob, out, null);
	}

	private void msgBox(String title, Connection con, cwSysMessage e,
			dataObj dob, PrintWriter out, boolean tag) throws IOException,
			cwException, SQLException {
		String msg = e.getSystemMessage(dob.prof.label_lan);

		String url_next = "";
		if (title.equalsIgnoreCase(MSG_STATUS))
			url_next = dob.url_success;
		else
			url_next = dob.getUrl_fail();

		String xml = "";
		// qdbEnv qe = static_env;

		xml = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF
				+ "\" standalone=\"no\" ?>" + dbUtils.NEWL;
		xml += "<message>" + dbUtils.NEWL;

		if (dob.prof != null) {
			xml += dob.prof.asXML() + dbUtils.NEWL;
		}

		xml += "<title>" + title + "</title>" + dbUtils.NEWL;
		xml += "<body>" + dbUtils.NEWL;
		xml += "<text>" + dbUtils.esc4XML(msg) + "</text>" + dbUtils.NEWL;
		xml += "<button url=\"" + dbUtils.esc4XML(url_next) + "\"";
		if (dob.url_target != null && dob.url_target.length() > 0)
			xml += " target=\"" + dob.url_target + "\"";
		xml += " tag = '" + tag + "' "; // by nick 因为wb_msg_box 公用的 有的地方需要用定时器
										// 所以在这个地方添加标志来做判断
		xml += ">OK</button>" + dbUtils.NEWL;
		xml += "</body>" + dbUtils.NEWL;
		xml += "</message>" + dbUtils.NEWL;

		// File fXsl = new File(qe.INI_XSL_MSGBOX);
		// String xslFile = fXsl.getAbsolutePath();
		static_env.procXSLFile(xml, wizbini.cfgSysSetup.getXslStylesheet()
				.getMsgboxFile(), out, dob.xsl_root);

	}

	private void msgBox(String title, Connection con, qdbErrMessage e,
			dataObj dob, PrintWriter out) throws IOException, cwException,
			SQLException {
		String msg = e.getSystemMessage(dob.prof.label_lan);
		genMsgBox(title, msg, dob, out, null);
	}

	private void genMsgBox(String title, String msg, dataObj dob,
			PrintWriter out, String modXML) throws IOException, cwException {
		String url_next = "";
		if (title.equalsIgnoreCase(MSG_STATUS))
			url_next = dob.url_success;
		else
			url_next = dob.getUrl_fail();

		String xml = "";
		// qdbEnv qe = static_env;

		xml = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF
				+ "\" standalone=\"no\" ?>" + dbUtils.NEWL;
		xml += "<message>" + dbUtils.NEWL;

		if (dob.prof != null) {
			xml += dob.prof.asXML() + dbUtils.NEWL;
		}

		if (modXML != null)
			xml += modXML;

		xml += "<title>" + title + "</title>" + dbUtils.NEWL;
		xml += "<body>" + dbUtils.NEWL;
		xml += "<text>" + dbUtils.esc4XML(msg) + "</text>" + dbUtils.NEWL;
		xml += "<button url=\"" + dbUtils.esc4XML(url_next) + "\"";
		if (dob.url_target != null && dob.url_target.length() > 0)
			xml += " target=\"" + dob.url_target + "\"";
		xml += ">OK</button>" + dbUtils.NEWL;
		xml += "</body>" + dbUtils.NEWL;
		xml += "</message>" + dbUtils.NEWL;

		// File fXsl = new File(qe.INI_XSL_MSGBOX);
		// String xslFile = fXsl.getAbsolutePath();
		static_env.procXSLFile(xml, wizbini.cfgSysSetup.getXslStylesheet()
				.getMsgboxFile(), out, dob.xsl_root);
	}

	private void mediaBox(dataObj dob, PrintWriter out) throws IOException,
			cwException {
		// try {
		String xml = "";

		xml = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF
				+ "\" standalone=\"no\" ?>" + dbUtils.NEWL;
		xml += "<media_upload>" + dbUtils.NEWL;
		xml += dob.prof.asXML() + dbUtils.NEWL;
		xml += "<filename>" + dbUtils.esc4XML(dob.dbres.res_src_link)
				+ "</filename>" + dbUtils.NEWL;
		xml += "<converted_filename>" + dob.uploaded_filename
				+ "</converted_filename>" + dbUtils.NEWL;
		xml += "</media_upload>" + dbUtils.NEWL;

		static_env.procXSLFile(xml, wizbini.cfgSysSetup.getXslStylesheet()
				.getMediaboxFile(), out, dob.xsl_root);
		// }catch (qdbException e) {
		// throw new cwException(e.getMessage());
		// }
	}

	// return the full URL of the request to this servlet
	private String getServReqURL(HttpServletRequest req) {
		boolean paraExist = false;
		String paraname = null;
		String paravalue = null;
		// should replace HttpUtils.getRequestURL with
		// HttpServletRequest.getRequestURL in servlet API 2.3
		StringBuffer query = HttpUtils.getRequestURL(req);
		Enumeration paranames = req.getParameterNames();
		while (paranames.hasMoreElements()) {
			if (paraExist) {
				query.append("&");
			} else {
				query.append("?");
			}
			paraname = (String) paranames.nextElement();
			paravalue = req.getParameter(paraname);
			query.append(paraname).append("=").append(paravalue);
			paraExist = true;
		}
		return (query.toString());
	}

	// return the full URL for re-logging in
	// inSuccessURL: indicates the URL to be redirected to after successful
	// relogin
	private String getReLoginURL(String url, String inSuccessURL) {
		return (url + "=" + java.net.URLEncoder.encode(inSuccessURL));
	}

	// WaiLun : convert the uploaded chinese filename to the converted new
	// filename
	public String convertFilename(String mediaFile, HttpSession sess) {
		Vector original_filename = (Vector) sess
				.getAttribute("ORIGINAL_FILENAME");
		Vector new_filename = (Vector) sess.getAttribute("NEW_FILENAME");
		int filenameIndex = original_filename.indexOf(mediaFile);
		if (filenameIndex != -1)
			return (String) new_filename.elementAt(filenameIndex);
		return mediaFile;
	}

	/**
	 * Include a module specific open-end tag and user profile xml with the
	 * input data XML
	 * 
	 * @param dataXML
	 *            input data XML
	 * @param metaXML
	 *            meta XML
	 * @param moduleName
	 *            start, end root tag (e.g. "user_manager")
	 * @return an XML contain <cur_usr> and the input data XML
	 */
	private String formatXML(String dataXML, String metaXML, String moduleName,
			loginProfile prof) {
		StringBuffer outBuf = new StringBuffer(2500);

		outBuf.append(dbUtils.xmlHeader).append(dbUtils.NEWL);
		outBuf.append("<").append(moduleName).append(">").append(dbUtils.NEWL);
		outBuf.append("<meta>").append(dbUtils.NEWL);
		if (prof != null)
			outBuf.append(prof.asXML()).append(dbUtils.NEWL);
		if (metaXML != null)
			outBuf.append(metaXML).append(dbUtils.NEWL);
		outBuf.append("<tc_enabled>").append(wizbini.cfgTcEnabled)
				.append("</tc_enabled>");
		outBuf.append("<tc_independent>")
				.append(wizbini.cfgSysSetupadv.isTcIndependent())
				.append("</tc_independent>");
		outBuf.append("<default_usr_icon>")
				.append(cwUtils.esc4XmlJs(dbRegUser.getDefaultUsrPhotoDir(
						wizbini, prof.root_id))).append("</default_usr_icon>");
		outBuf.append("</meta>").append(dbUtils.NEWL);
		outBuf.append(dataXML).append(dbUtils.NEWL);
		outBuf.append("</").append(moduleName).append(">");

		String out = new String(outBuf);
		return out;
	}

	private static String extractHostNameFrURL(String inURL) {
		int protocolLen = 7;
		int colonIdx = inURL.indexOf(':', protocolLen);
		int slashIdx = inURL.indexOf('/', protocolLen);
		int endIdx = inURL.length();

		if (colonIdx > 0 && slashIdx > 0) {
			if (colonIdx < slashIdx)
				endIdx = colonIdx;
			else
				endIdx = slashIdx;
		} else if (!(colonIdx < 0 && slashIdx < 0)) {
			if (colonIdx > slashIdx)
				endIdx = colonIdx;
			else
				endIdx = slashIdx;
		}

		return inURL.substring(protocolLen, endIdx);
	}

	public String getSiteLangAsXML(Connection con, loginProfile prof,
			long site_id) throws SQLException, qdbException {

		acSite site = new acSite();
		site.ste_ent_id = site_id;
		site.get(con);

		// format xml
		StringBuffer xmlBuf = new StringBuffer();
		xmlBuf.append(cwUtils.xmlHeader);
		xmlBuf.append("<site id=\"").append(site_id).append("\">")
				.append(cwUtils.NEWL);
		xmlBuf.append(prof.asXML());
		// get system time
		xmlBuf.append("<cur_time>").append(dbUtils.getTime(con))
				.append("</cur_time>").append(cwUtils.NEWL);
		xmlBuf.append("<language>").append(site.ste_lan_xml)
				.append("</language>").append(cwUtils.NEWL);
		xmlBuf.append("</site>").append(cwUtils.NEWL);
		return xmlBuf.toString();
	}

	/**
	 * do servlet cmd that do not need login
	 * 
	 * @param con
	 *            Connection to databse
	 * @param dob
	 *            dataObj stores the input parameters
	 * @param out
	 *            output writer of servlet
	 * @return true if an action is taken, false if not
	 */
	private boolean doActionNotRequireLogin(Connection con, dataObj dob,
			PrintWriter out) throws SQLException, qdbException, cwException {

		boolean actionTaken = false; // store if an action is taken in this
										// method

		if (dob.prm_ACTION.equalsIgnoreCase("USR_REG_APPROVE_XML")) {
			actionTaken = true;
			StringBuffer xmlBuf = new StringBuffer(1024);
			qdbXMessage msgWorker = new qdbXMessage();
			dob.getUsr().get(con);
			xmlBuf.append(msgWorker.getSenderXml(con,
					dob.getUsr().usr_approve_usr_id,
					Application.MAIL_SERVER_ACCOUNT_TYPE));
			xmlBuf.append(msgWorker.getRecipientXml(con,
					dob.getUsr().usr_ent_id,
					Application.MAIL_SERVER_ACCOUNT_TYPE,
					wizbini.cfgSysSetupadv.getDesKey()));
			xmlBuf.append(dob.getUsr().getUserRegApproveXML(con, dob.prof));
			static_env.outputXML(out, xmlBuf.toString());
		} else if (dob.prm_ACTION.equalsIgnoreCase("USR_REG_DISAPPROVE_XML")) {
			actionTaken = true;
			StringBuffer xmlBuf = new StringBuffer(1024);
			qdbXMessage msgWorker = new qdbXMessage();
			dob.getUsr().get(con);
			xmlBuf.append(msgWorker.getSenderXml(con,
					dob.getUsr().usr_approve_usr_id,
					Application.MAIL_SERVER_ACCOUNT_TYPE));
			xmlBuf.append(msgWorker.getRecipientXml(con,
					dob.getUsr().usr_ent_id,
					Application.MAIL_SERVER_ACCOUNT_TYPE,
					wizbini.cfgSysSetupadv.getDesKey()));
			xmlBuf.append(dob.getUsr().getUserXML(con, dob.prof));
			static_env.outputXML(out, xmlBuf.toString());
		} else if (dob.prm_ACTION.equalsIgnoreCase("USR_CREATION_XML")) {
			actionTaken = true;
			StringBuffer xmlBuf = new StringBuffer(1024);
			qdbXMessage msgWorker = new qdbXMessage();
			dob.getUsr().get(con);
			xmlBuf.append(msgWorker.getSenderXml(con, dob.sender_id,
					Application.MAIL_SERVER_ACCOUNT_TYPE));
			xmlBuf.append(msgWorker.getRecipientXml(con,
					dob.getUsr().usr_ent_id,
					Application.MAIL_SERVER_ACCOUNT_TYPE,
					wizbini.cfgSysSetupadv.getDesKey()));
			xmlBuf.append(dob.getUsr().getUserXML(con, dob.prof));
			static_env.outputXML(out, xmlBuf.toString());
		}
		return actionTaken;
	}

	public static WizbiniLoader getWizbini() {
		return wizbini;
	}

	// message box only for process error when learner submit test.
	private void msgBoxTst(String title, Connection con, qdbErrMessage e,
			dataObj dob, PrintWriter out) throws IOException, cwException,
			SQLException {
		String msg = e.getSystemMessage(dob.prof.label_lan);
		genMsgBoxTst(title, msg, dob, out);
	}

	private void genMsgBoxTst(String title, String msg, dataObj dob,
			PrintWriter out) throws IOException, cwException {
		String xml = "";
		String url_next = "";
		if (title.equalsIgnoreCase(MSG_STATUS))
			url_next = dob.url_success;
		else
			url_next = dob.getUrl_fail();

		xml = "<?xml version=\"1.0\" encoding=\"" + dbUtils.ENC_UTF
				+ "\" standalone=\"no\" ?>";
		xml += "<message>";
		xml += "<is_tst_submit_err>true</is_tst_submit_err>";

		if (dob.prof != null) {
			xml += dob.prof.asXML();
		}

		xml += "<title>" + title + "</title>";
		xml += "<body>";
		xml += "<text>" + dbUtils.esc4XML(msg) + "</text>";
		xml += "<button url=\"" + dbUtils.esc4XML(url_next) + "\"";
		xml += ">OK</button>";
		xml += "</body>";
		xml += "</message>";

		static_env.procXSLFile(xml, wizbini.cfgSysSetup.getXslStylesheet()
				.getMsgboxFile(), out, dob.xsl_root);
	}

	public void destroy() {
		if (taskController != null)
			taskController.stopController();
		super.destroy();
		CommonLog.info("qdbAction.destory() End.");
	}

	private void procUploadUsrFace(Connection con, String tempSaveDirPath,
			long usr_ent_id, boolean remain_photo_ind) throws qdbException,
			SQLException, cwException, IOException {
		if (!remain_photo_ind) {
			String savaDirPath = wizbini.getFileUploadUsrDirAbs()
					+ dbUtils.SLASH + usr_ent_id;
			dbUtils.delFiles(savaDirPath);
			dbUtils.moveDir(tempSaveDirPath, savaDirPath);
			File Dir = new File(tempSaveDirPath);
			File[] files = Dir.listFiles();
			String filename = null;
			if (files != null && files.length > 0) {
				filename = files[0].getName();
			}
			String saveFile = savaDirPath + dbUtils.SLASH + filename;
			cwUtils.resizeImage(saveFile, wizbini.getUsrImageWidth(),
					wizbini.getUsrImageHeight());
			String sql = "update regUserExtension set urx_extra_43= ? where urx_usr_ent_id= ? ";
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, filename);
			stmt.setLong(2, usr_ent_id);
			stmt.execute();
			cwSQL.cleanUp(null, stmt);
		}
	}

}
