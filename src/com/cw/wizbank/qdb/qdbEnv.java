/*
[ Public Constructors ]
_____________________________________________________________________

qdbEnv()
    Create a new qdbEnv containing no data.

[ Public Methods ]
_____________________________________________________________________

init(HttpServletRequest, String)
Connection openDB()
procXSLFile(String, String, PrintWriter)
procAbsoluteXSLFile(String, String, PrintWriter, boolean, boolean)
public static String getRequestInfo(HttpServletRequest)
_____________________________________________________________________

public void init(HttpServletRequest req, String env)

    Parse an ini file and initialize the envrionment
    Parameters :
        req -- servlet request
        env -- filename of the ini file
               e.g. {Document Root}\config\{env}.ini

public Connection openDB()

    Create a database connection using the parameters specified in the ini file
    Return :
        a database connection

public void procXSLFile(String xml, String xslFile, PrintWriter resultWriter, String xslRoot)

    Process XSL transfromation combining with the XML stream
    Parameters :
        xml -- string containing the xml
        xslFile -- filename of the xsl file (NOT included the full path)
        resultWriter -- printwriter that output the generated HTM
        xslRoot -- directory relative to WWW_ROOT storing the XSL files

public static void processFiles(String absoluteXMLFile, String xslFile, PrintWriter resultWriter, String xslRoot)

    Process XSL transfromation combining with the XML stream
    Parameters :
        absoluteXMLFile -- the absolute path of the xsl file
        absoluteXSLFile -- the absolute path of the xsl file
        resultWriter -- printwriter that output the generated HTM
        xslRoot -- directory relative to WWW_ROOT storing the XSL files

public static String getRequestInfo(HttpServletRequest req)

    Get the servlet request information
    Parameters :
        req -- servlet request
    Return :
        string containing all the request information

*/
package com.cw.wizbank.qdb;

import com.cw.wizbank.Application;
import com.cw.wizbank.util.*;

//import javax.servlet.*;
import java.io.*;
import java.sql.*;

import com.cw.wizbank.config.WizbiniLoader;
import com.cwn.wizbank.utils.CommonLog;

public class qdbEnv {
    public static final String CONFIG_FOLDER = "wb_config";
    public static final String SYSTEM_FOLDER = "system";
    public static String RES_FOLDER    = "resource";
    public static final String EMAIL_TPL_FOLDER    = "template";
    public static final String ITM_FOLDER    = "item";
    public static final String OBJ_FOLDER = "object";
    public static final String XSL_LOG_FOLDER = "log";
    public static final String DEFAULT_STYLE = "cw";
    public static final String DEFAULT_ENV   = "wizb";
    public final String DEFAULT_IMGLIG = "imglib";
 //   private static final String DEFAULT_SEARCH_DB_DIR = "search";
    // folder to place the facility thumbnail
    public static final String FM_FOLDER    = "facility";

    public boolean shutdownMode = false;
    public int activeRequestNum = 0;
    public long last_req_time = 0;

    public String ENV;
    public String DOC_ROOT;
    public String INI_PATH;
    public String INI_DIR_UPLOAD_TMP;
    public String INI_DIR_UPLOAD_TMP_URL;
    public String INI_DIR_UPLOAD;
    public String INI_ITM_DIR_UPLOAD_TMP;
    public String INI_ITM_DIR_UPLOAD;
    public String INI_MSG_DIR_UPLOAD_TMP;
    public String INI_MSG_DIR_UPLOAD;
    public String INI_ITM_DIR_UPLOAD_URL;
    public String INI_OBJ_DIR_UPLOAD;
    public String INI_RES_DIR_URL;
    public String INI_FAC_DIR_URL;
    public String INI_POSTER_DIR_URL;
    public String INI_PLAN_DIR_URL;
    public String INI_LOG_DIR_URL;
    public String INI_ARC_DIR_UPLOAD_URL;
    public String INI_INSTRUCTOR_DIR_UPLOAD_URL;
    // folder to place the facility thumbnail
    public String INI_FM_DIR_UPLOAD_TMP;
    public String INI_FM_DIR_UPLOAD;
    public int INI_MAX_UPLOAD_SIZE;
    public String INI_SYSTEM_PASSWORD;

    public String INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL;
    public String INI_NOTIFY_TEACHER_TPL;
    public String INI_NOTIFY_STUDENT_GRADE_TPL;
    public String INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_CH;
    public String INI_NOTIFY_TEACHER_TPL_CH;
    public String INI_NOTIFY_STUDENT_GRADE_TPL_CH;
    public String INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_GB;
    public String INI_NOTIFY_TEACHER_TPL_GB;
    public String INI_NOTIFY_STUDENT_GRADE_TPL_GB;

    public boolean INI_MARK_FORUM_MSG;

    public String INI_FILE_SERVLET_IGNORE;
    public String INI_MEDIA_SERVER_LICENSE;
    public String INI_LOGIN_REQUIRED_FOLDERS;
    public String INI_HIDDEN_FOLDERS;

    public String ENCODING;
    public String STYLE;


    public String URL_RELOGIN;
    public String URL_SESSION_TIME_OUT;
    public String URL_LOGIN_FAILURE;
    public String URL_MAINTENANCE;

    public String INI_XSL_HOME;
    public String INI_XSL_CACHE_FILE;
    public String INI_XSL_MSGBOX;
    public String INI_XSL_MEDIABOX;
    public String INI_XSL_VALTPL;
    public String INI_XSL_LOG;
    public boolean INI_XSL_LOG_ENABLED;

    // DB PARAM
    private String DBVENDOR;
    private String DBHOST;
    private String DBPORT;
    private String DBNAME;
    private String DBUSER;
    private String DBPASS;
    private int DBPOOL_SIZE;

    //  TS Server
    public boolean TSENABLED;   // Enable/Disable TS Server
    public String TSHOST;       //Host Name of TS Server
    public int TSPORT;       //TS Server Port of Lobby
    public int ROOMPORT;     //TS Server Port of Room
    public int WWWPORT;      //WEB Server Port for applet

    //For online payment
    public String CoNo;
    public File PUBLICKEY_PATH;
    public boolean CHECK_KEY;
    public String KEY_FILENAME;

    //For wizCase
    public String WIZCASE_XML_HOME;
    public String WIZCASE_TEMP_DIR;

    public boolean DEBUG;
    public boolean COMP_XSL;

    // for AICC only
    public String AICC_URL;

    // for NETG only
    public int NETG_PASS_SCORE;

    // duration for cleaning the temp directory
    public int TEMP_DIR_CLEAN_DUR;

    // how frequent to call the "temp" directory cleaner (in seconds)
    public int TEMP_DIR_CLEAN_FREQUENCY;

    // search, real time content indexing or not
    public boolean SEARCH_CONTENT_INDEXING;

    // Search, search index builder thread sleep time in second
    public int SEARCH_INDEX_BUILDER_SLEEP_TIME;

    //foe message scheduler
    public String SEND_MAIL_URL;        //url of the message scheduler to call for sending email
    public int MAIL_REFRESH;            //refresh rate of the message scheduler to send email
    public int MAIL_ATTEMPT;
    public String MAIL_NOTES_LOG;
    public String LOG_FOLDER;
    public String MAIL_ACCOUNT;
    public String MAIL_SCHEDULER_ENABLE;

    //for PREREQUISITE REFRESH
    public int ITEM_REQUIREMENT_REFRESH;
    public String ITEM_REQUIREMENT_SCHEDULER_ENABLE;

    //for TREENODEPATH REFRESH
    public String ITEM_TREENODE_PATH_SCHEDULER_ENABLE;

    //public boolean CRITERIA_ENABLED;            //enable the criteria scheduler
    //public int CRITERIA_REFRESH;            //refresh rate of the criteria scheduer to set Attendance status

    public long DES_KEY;

    //for search engine
    public String SEARCH_DB_DIR;

    //for KM scheduler
    public boolean  KM_SCHEDULER_ENABLE;
    public int      KM_REFRESH_TIME;
    public int      KM_DAILY_MAIL_SEND_TIME;
    public int      KM_WEEKLY_MAIL_SEND_TIME;

    // for entreprise manager
    public int DEFAULT_GRADE_ID;

    public boolean UPLOAD_USER_PWD_ENABLE;
    public boolean UPLOAD_USER_DISTINCT_CHECK_ENABLE;

    // for km-library policy
    public int KM_LIB_BORROW_LIMIT;
    public int KM_LIB_OVERDUE_LIMIT;
    public int KM_LIB_RENEW_LIMIT;
    public int KM_LIB_DUE_DAY;

    public boolean logDirIsRelative;
    public String logFolderPath;

    // for Group Member Full Path Separator
    public static String GPM_FULL_PATH_SEPARATOR;

    public qdbEnv() {
/*
        DOC_ROOT = "";
        INI_SYSTEM_PASSWORD = "";
        INI_MAX_UPLOAD_SIZE = 0;
        INI_DIR_UPLOAD = "";
        INI_DIR_UPLOAD_TMP = "";
        INI_XSL_HOME = "";
        INI_XSL_CACHE_FILE = "";
        INI_XSL_MSGBOX = "";
        INI_XSL_MEDIABOX = "";
        INI_XSL_VALTPL = "";

        INI_MAIL_SERVER = "";

        INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL = "";
        INI_NOTIFY_TEACHER_TPL = "";
        INI_NOTIFY_STUDENT_GRADE_TPL = "";
        INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_CH = "";
        INI_NOTIFY_TEACHER_TPL_CH = "";
        INI_NOTIFY_STUDENT_GRADE_TPL_CH = "";
        INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_GB = "";
        INI_NOTIFY_TEACHER_TPL_GB = "";
        INI_NOTIFY_STUDENT_GRADE_TPL_GB = "";

        INI_MARK_FORUM_MSG = true;

        INI_FILE_SERVLET_IGNORE = "";
        INI_MEDIA_SERVER_LICENSE = "";
        INI_LOGIN_REQUIRED_FOLDERS = "";
        INI_HIDDEN_FOLDERS = "";

        ENCODING = "";
        STYLE = "";


        URL_RELOGIN = "";
        URL_LOGIN_FAILURE = "";
        URL_MAINTENANCE = "";


        DBVENDOR = "";
        DBHOST = "";
        DBPORT = "";
        DBNAME = "";
        DBUSER = "";
        DBPASS = "";

        TSENABLED = false;
        TSHOST = "";
        TSPORT = 7800;
        ROOMPORT = 7900;
        WWWPORT = 80;

        CoNo = "";
        CHECK_KEY = false;
        KEY_FILENAME = "";

        MULTI_LOGIN = false;
        DEBUG = false;
        COMP_XSL = false;

        // for AICC only
        AICC_URL = "";

        // for NETg course only
        NETG_PASS_SCORE = 0;

        // duration for cleaning the temp directory
        TEMP_DIR_CLEAN_DUR = 3600;

        TEMP_DIR_CLEAN_FREQUENCY = 3600;

        SEARCH_CONTENT_INDEXING = false;

        SEARCH_INDEX_BUILDER_SLEEP_TIME = 10;

        //message
        SEND_MAIL_URL = "";
        MAIL_REFRESH = 10;
        MAIL_ATTEMPT = 3;
        MAIL_NOTES_LOG = "";
        LOG_FOLDER = "";
        MAIL_ACCOUNT = "EMAIL";
        MAIL_SCHEDULER_ENABLE = "true";

        // PREREQUISITE
        ITEM_REQUIREMENT_REFRESH = 3600;
        ITEM_REQUIREMENT_SCHEDULER_ENABLE = "true";

        ITEM_TREENODE_PATH_SCHEDULER_ENABLE = "true";

        CRITERIA_REFRESH = 10;
        CRITERIA_ENABLED = false;

        WIZCASE_XML_HOME = "";
        WIZCASE_TEMP_DIR = "";

        DES_KEY = 1;

        KM_SCHEDULER_ENABLE = false;
        KM_REFRESH_TIME = 30;               // 30 seconds
        KM_DAILY_MAIL_SEND_TIME = 15;       // 15 minutes
        KM_WEEKLY_MAIL_SEND_TIME = 1;       // Sunday

        DEFAULT_GRADE_ID = -1;

        UPLOAD_USER_PWD_ENABLE = true;
        UPLOAD_USER_DISTINCT_CHECK_ENABLE = true;

        KM_LIB_BORROW_LIMIT = 3;
        KM_LIB_OVERDUE_LIMIT = 4;
        KM_LIB_RENEW_LIMIT = 3;
        KM_LIB_DUE_DAY = 14;

        GPM_FULL_PATH_SEPARATOR = "/";
*/
    }

    public void init(WizbiniLoader wizbini/*, String env*/)
        throws cwException
    {

		RES_FOLDER = cwUtils.getFileURL(wizbini.cfgSysSetupadv.getFileUpload().getResDir().getName());
    	/*
        if (env == null || env.length() == 0)
            ENV = DEFAULT_ENV;
        else
            ENV = env;
		*/
		ENV = DEFAULT_ENV;
        DOC_ROOT = wizbini.getWebDocRoot();
        File fConfigRoot = new File(DOC_ROOT, CONFIG_FOLDER);
        File fIniFile = new File(fConfigRoot, ENV + ".ini");
        // check existence of files
//        if (!fIniFile.exists())
//            throw new cwException("Env ini file not found: ENV=" + ENV);
        if(fIniFile.exists())
            INI_PATH = fIniFile.getAbsolutePath();





            //cwIniFile ini = new cwIniFile(INI_PATH);

            INI_MAX_UPLOAD_SIZE = wizbini.cfgSysSetupadv.getFileUpload().getMaxUploadSize();
            /*
            String val = ini.getValue("MAX_UPLOAD_SIZE");
            if (val == null || val.length()==0)
                INI_MAX_UPLOAD_SIZE = 5;        // Default 5 Meg.
            else
                INI_MAX_UPLOAD_SIZE = Integer.parseInt(val);
            */


            INI_DIR_UPLOAD = wizbini.getFileUploadResDirAbs();
            /*
            val = ini.getValue("RES_PATH");
            if (val == null || val.length() ==0) {
                File resFolder = new File(DOC_ROOT, RES_FOLDER);
                INI_DIR_UPLOAD = resFolder.getAbsolutePath();
            }else
                INI_DIR_UPLOAD = val;
            */


			INI_DIR_UPLOAD_TMP = wizbini.getFileUploadTmpDirAbs();
			INI_DIR_UPLOAD_TMP_URL = wizbini.cfgSysSetupadv.getFileUpload().getTmpDir().getUrl();
			INI_ITM_DIR_UPLOAD_URL = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getUrl();
			INI_RES_DIR_URL = wizbini.cfgSysSetupadv.getFileUpload().getResDir().getUrl();
			INI_FAC_DIR_URL = wizbini.cfgSysSetupadv.getFileUpload().getFacDir().getUrl();
			INI_POSTER_DIR_URL = wizbini.cfgSysSetupadv.getFileUpload().getPosterDir().getUrl();
			INI_LOG_DIR_URL = wizbini.cfgSysSetupadv.getLogDir().getUrl();
			INI_ARC_DIR_UPLOAD_URL =  wizbini.cfgSysSetupadv.getFileUpload().getArticleDir().getUrl();
			INI_INSTRUCTOR_DIR_UPLOAD_URL = wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getUrl();
            /*
            File fTmpRoot = new File(INI_DIR_UPLOAD, "temp");
            INI_DIR_UPLOAD_TMP = fTmpRoot.getAbsolutePath();
			*/


			INI_ITM_DIR_UPLOAD = wizbini.getFileUploadItmDirAbs();
			/*
            val = ini.getValue("ITM_PATH");
            if (val == null || val.length() ==0) {
                File itmFolder = new File(DOC_ROOT, ITM_FOLDER);
                INI_ITM_DIR_UPLOAD = itmFolder.getAbsolutePath();
            }else
                INI_ITM_DIR_UPLOAD = val;
			*/

			INI_MSG_DIR_UPLOAD = wizbini.getFileUploadMsgDirAbs();

			INI_OBJ_DIR_UPLOAD = wizbini.getFileUploadObjDirAbs();
			/*
            val = ini.getValue("OBJ_PATH");
            if (val == null || val.length() ==0) {
                File itmFolder = new File(DOC_ROOT, OBJ_FOLDER);
                INI_OBJ_DIR_UPLOAD = itmFolder.getAbsolutePath();
            }else
                INI_OBJ_DIR_UPLOAD = val;
			*/


            // intialize the facility thumbnail folder
            INI_FM_DIR_UPLOAD = wizbini.getFileUploadFacDirAbs();
            /*
            val = ini.getValue("FM_PATH");
            if (val == null || val.length() == 0) {
                File fmFolder = new File(DOC_ROOT, FM_FOLDER);
                this.INI_FM_DIR_UPLOAD = fmFolder.getAbsolutePath();
            } else {
                this.INI_FM_DIR_UPLOAD = val;
            }
            */

			INI_FM_DIR_UPLOAD_TMP = wizbini.getFileUploadTmpDirAbs();
            // INI_FM_DIR_UPLOAD_TMP not in use because INI_DIR_UPLOAD_TMP is used in Dispatcher
            /*
            File fFacilityTmpRoot = new File(this.INI_FM_DIR_UPLOAD, "temp");
            this.INI_FM_DIR_UPLOAD_TMP = fFacilityTmpRoot.getAbsolutePath();
			*/


			SEARCH_DB_DIR = wizbini.getKmSearchDirAbs();
			/*
            val = ini.getValue("SEARCH_DB_DIR");
            if (val == null || val.length() ==0) {
                File searchDbDir = new File(DOC_ROOT, DEFAULT_SEARCH_DB_DIR);
                SEARCH_DB_DIR = searchDbDir.getAbsolutePath();
            }else
                SEARCH_DB_DIR = val;
			*/


			INI_XSL_LOG = wizbini.getSystemLogDirAbs() + dbUtils.SLASH + wizbini.cfgSysSetup.getXslStylesheet().getLogFile();
			INI_XSL_LOG_ENABLED = wizbini.cfgSysSetup.getXslStylesheet().isLogEnabled();
			/*
            val = ini.getValue("XSL_LOG");
            if (val == null || val.length() ==0) {
                INI_XSL_LOG = null;
            }else if(val.indexOf(File.pathSeparator)>0) {
                INI_XSL_LOG = val;
            }else {
                String root = DOC_ROOT + File.separator + XSL_LOG_FOLDER ;
                File xslLogFile = new File(root, val);
                INI_XSL_LOG = xslLogFile.getAbsolutePath();
            }
            */


            INI_ITM_DIR_UPLOAD_TMP = wizbini.getFileUploadTmpDirAbs();
			/*
            File fItmTmpRoot = new File(INI_ITM_DIR_UPLOAD, "temp");
            INI_ITM_DIR_UPLOAD_TMP = fItmTmpRoot.getAbsolutePath();
            */

            INI_MSG_DIR_UPLOAD_TMP = wizbini.getFileUploadTmpDirAbs();
            

            DEBUG = wizbini.cfgSysSetupadv.isDebug();
            /*
            val = ini.getValue("DEBUG");
            if (val != null && val.equalsIgnoreCase("YES"))
                DEBUG = true;
            else
                DEBUG = false;
            */


            INI_XSL_HOME = wizbini.cfgSysSetup.getXslStylesheet().getHome();
            /*
            String xslPath = "";
            val = ini.getValue("COMP_XSL");
            if (val != null && val.equalsIgnoreCase("YES")) {
                COMP_XSL = true;
                xslPath = ini.getValue("XSL_COMP_HOME");
            }else {
                COMP_XSL = false;
                xslPath = ini.getValue("XSL_HOME");
            }
            INI_XSL_HOME = xslPath;
            */

			INI_XSL_CACHE_FILE = wizbini.cfgSysSetup.getXslStylesheet().getCacheFile();
            INI_XSL_MSGBOX = wizbini.cfgSysSetup.getXslStylesheet().getMsgboxFile();
            INI_XSL_MEDIABOX = wizbini.cfgSysSetup.getXslStylesheet().getMediaboxFile();
            INI_XSL_VALTPL = wizbini.cfgSysSetup.getXslStylesheet().getValueTemplateFile();
            /*
            INI_XSL_CACHE_FILE = ini.getValue("XSL_CACHE_FILE");
            INI_XSL_MSGBOX = ini.getValue("XSL_MSGBOX");
            INI_XSL_MEDIABOX = ini.getValue("XSL_MEDIABOX");
            INI_XSL_VALTPL = ini.getValue("XSL_VALTPL");
			*/


			INI_SYSTEM_PASSWORD = wizbini.cfgSysSetupadv.getMaintenance().getPassword();
			ENCODING = wizbini.cfgSysSetupadv.getEncoding();//设置默认编码格式
			STYLE = wizbini.cfgSysSetupadv.getSkinHome();

			/*
            INI_SYSTEM_PASSWORD = ini.getValue("PASSWORD");
            ENCODING = ini.getValue("ENCODING");
            STYLE = ini.getValue("STYLE");
            if (STYLE ==null)
                STYLE = DEFAULT_STYLE;
            */


            URL_RELOGIN = wizbini.cfgSysSetupadv.getLogin().getReloginUrl();
			URL_LOGIN_FAILURE = wizbini.cfgSysSetupadv.getLogin().getLoginFailureUrl();
			URL_MAINTENANCE = wizbini.cfgSysSetupadv.getMaintenance().getRedirectUrl();
			URL_SESSION_TIME_OUT=wizbini.cfgSysSetupadv.getLogin().getSessionTimeOut();
			/*
            URL_RELOGIN = ini.getValue("URL_RELOGIN");
            URL_LOGIN_FAILURE = ini.getValue("URL_LOGIN_FAILURE");
            URL_MAINTENANCE = ini.getValue("URL_MAINTENANCE");
            INI_MAIL_SERVER = ini.getValue("MAILSERVER");
			*/

			String configFolderAbs = DOC_ROOT + dbUtils.SLASH + CONFIG_FOLDER + dbUtils.SLASH + EMAIL_TPL_FOLDER + dbUtils.SLASH;
			INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL = configFolderAbs + wizbini.cfgSysSetupadv.getAssEmailTemplate().getSuccessTemplateEnUs();
			INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_GB = configFolderAbs + wizbini.cfgSysSetupadv.getAssEmailTemplate().getSuccessTemplateZhCn();
			INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_CH = configFolderAbs + wizbini.cfgSysSetupadv.getAssEmailTemplate().getSuccessTemplateZhHk();
			INI_NOTIFY_TEACHER_TPL = configFolderAbs + wizbini.cfgSysSetupadv.getAssEmailTemplate().getSubmitTeacherTemplateEnUs();
			INI_NOTIFY_TEACHER_TPL_GB = configFolderAbs + wizbini.cfgSysSetupadv.getAssEmailTemplate().getSubmitTeacherTemplateZhCn();
			INI_NOTIFY_TEACHER_TPL_CH = configFolderAbs + wizbini.cfgSysSetupadv.getAssEmailTemplate().getSubmitTeacherTemplateZhHk();
			INI_NOTIFY_STUDENT_GRADE_TPL = configFolderAbs + wizbini.cfgSysSetupadv.getAssEmailTemplate().getGradeTemplateEnUs();
			INI_NOTIFY_STUDENT_GRADE_TPL_GB = configFolderAbs + wizbini.cfgSysSetupadv.getAssEmailTemplate().getGradeTemplateZhCn();
			INI_NOTIFY_STUDENT_GRADE_TPL_CH = configFolderAbs + wizbini.cfgSysSetupadv.getAssEmailTemplate().getGradeTemplateZhHk();

/*
            String FULL_TPL_FOLDER = fConfigRoot + dbUtils.SLASH + EMAIL_TPL_FOLDER;
            INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL = FULL_TPL_FOLDER + dbUtils.SLASH + ini.getValue("MAIL_ASSIGNMENT_SUCCESS_TPL");
            INI_NOTIFY_TEACHER_TPL = FULL_TPL_FOLDER + dbUtils.SLASH + ini.getValue("MAIL_NOTIFY_SUBMIT_TPL");
            INI_NOTIFY_STUDENT_GRADE_TPL = FULL_TPL_FOLDER + dbUtils.SLASH + ini.getValue("MAIL_ASSIGNMENT_GRADE_TPL");
            INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_CH = FULL_TPL_FOLDER + dbUtils.SLASH + ini.getValue("MAIL_CH_ASSIGNMENT_SUCCESS_TPL");
            INI_NOTIFY_TEACHER_TPL_CH = FULL_TPL_FOLDER + dbUtils.SLASH + ini.getValue("MAIL_CH_NOTIFY_SUBMIT_TPL");
            INI_NOTIFY_STUDENT_GRADE_TPL_CH = FULL_TPL_FOLDER + dbUtils.SLASH + ini.getValue("MAIL_CH_ASSIGNMENT_GRADE_TPL");
            INI_NOTIFY_ASSIGNMENT_SUCCESS_TPL_GB = FULL_TPL_FOLDER + dbUtils.SLASH + ini.getValue("MAIL_GB_ASSIGNMENT_SUCCESS_TPL");
            INI_NOTIFY_TEACHER_TPL_GB = FULL_TPL_FOLDER + dbUtils.SLASH + ini.getValue("MAIL_GB_NOTIFY_SUBMIT_TPL");
            INI_NOTIFY_STUDENT_GRADE_TPL_GB = FULL_TPL_FOLDER + dbUtils.SLASH + ini.getValue("MAIL_GB_ASSIGNMENT_GRADE_TPL");
*/



			INI_MARK_FORUM_MSG = wizbini.cfgSysSetupadv.getForum().isMarkMsg();
			INI_FILE_SERVLET_IGNORE = wizbini.cfgSysSetupadv.getSecurityFileServlet().getIgnoreMime();
			INI_MEDIA_SERVER_LICENSE = wizbini.cfgSysSetupadv.getSecurityFileServlet().getMediaServerLicense();
			INI_LOGIN_REQUIRED_FOLDERS = wizbini.cfgSysSetupadv.getSecurityFileServlet().getLoginRequiredFolder();
			INI_HIDDEN_FOLDERS = wizbini.cfgSysSetupadv.getSecurityFileServlet().getHiddenFolder();
			/*
            INI_MARK_FORUM_MSG = ini.getValue("MARK_FORUM_MSG");
            INI_FILE_SERVLET_IGNORE = ini.getValue("FILE_SERVLET_IGNORE_MIME");
            INI_MEDIA_SERVER_LICENSE = ini.getValue("MEDIA_SERVER_LICENSE");
            INI_LOGIN_REQUIRED_FOLDERS = ini.getValue("LOGIN_REQUIRED_FOLDERS");
            INI_HIDDEN_FOLDERS = ini.getValue("HIDDEN_FOLDERS");
            */

			DBVENDOR = wizbini.cfgSysDb.getDbConnect().getDbvendor();
			DBHOST = wizbini.cfgSysDb.getDbConnect().getHost();
			DBPORT = Integer.toString(wizbini.cfgSysDb.getDbConnect().getPort());
			DBNAME = wizbini.cfgSysDb.getDbConnect().getName();
			DBUSER = wizbini.cfgSysDb.getDbConnect().getUser();
			DBPASS = wizbini.cfgSysDb.getDbConnect().getPass();
			/*
            DBVENDOR = ini.getValue("DBVENDOR");
            DBHOST = ini.getValue("DBHOST");
            DBPORT = ini.getValue("DBPORT");
            DBNAME = ini.getValue("DBNAME");
            DBUSER = ini.getValue("DBUSER");
            DBPASS = ini.getValue("DBPASS");
            try {
                DBPOOL_SIZE = Integer.parseInt(ini.getValue("DBPOOL_SIZE"));
            }catch ( Exception e) {
                DBPOOL_SIZE = cwSQL.DEFAULT_POOL_SIZE;
            }
            */



            //  For TSserver
            TSENABLED = wizbini.cfgSysSetup.getTspace().isEnabled();
            TSHOST = wizbini.cfgSysSetup.getTspace().getHost();
            TSPORT = wizbini.cfgSysSetup.getTspace().getPort();
            ROOMPORT = wizbini.cfgSysSetup.getTspace().getRoomPort();
            WWWPORT = wizbini.cfgSysSetup.getTspace().getWwwPort();
            /*
            val = ini.getValue("TSENABLED");
            if (val != null && val.equalsIgnoreCase("YES"))
                TSENABLED = true;
            else
                TSENABLED = false;

            TSHOST = ini.getValue("TSHOST");
            try {
                TSPORT = Integer.parseInt(ini.getValue("TSPORT"));
                ROOMPORT = Integer.parseInt(ini.getValue("ROOMPORT"));
                WWWPORT = Integer.parseInt(ini.getValue("WWWPORT"));
            }catch ( Exception e) {
                TSPORT = 0;
                ROOMPORT = 0;
                WWWPORT = 0;
            }
            */



            CoNo = wizbini.cfgSysSetupadv.getOnlinePayment().getCono();
            CHECK_KEY = wizbini.cfgSysSetupadv.getOnlinePayment().isCheckKey();
            KEY_FILENAME = wizbini.cfgSysSetupadv.getOnlinePayment().getKeyFile();

            /*
            CoNo = ini.getValue("CoNo");
            CHECK_KEY = ini.getValue("CHECK_KEY");
            KEY_FILENAME = ini.getValue("KEY_FILENAME");
            */

            //PUBLICKEY_PATH = new File(fConfigRoot,  KEY_FILENAME);
			PUBLICKEY_PATH = null;	//The function using this parameter is not reachable, but the code stil reference it, so set it to null for compile purpose.



            // for AICC only
            //AICC_URL = ini.getValue("AICC_URL");

            // for NETg only
            /*
            if (ini.getValue("NETG_PASS_SCORE") != null && ini.getValue("NETG_PASS_SCORE").equalsIgnoreCase("") == false) {
                NETG_PASS_SCORE = Integer.parseInt(ini.getValue("NETG_PASS_SCORE"));
            }
            else {
                NETG_PASS_SCORE = 50;
            }
            */



            // duration for cleaning the temp directory
            /*
            if (ini.getValue("TEMP_DIR_CLEAN_DUR") != null && ini.getValue("TEMP_DIR_CLEAN_DUR").equalsIgnoreCase("") == false) {
                TEMP_DIR_CLEAN_DUR = Integer.parseInt(ini.getValue("TEMP_DIR_CLEAN_DUR"));
            }
            else {
                TEMP_DIR_CLEAN_DUR = 3600;
            }

            if (ini.getValue("TEMP_DIR_CLEAN_FREQUENCY") != null && ini.getValue("TEMP_DIR_CLEAN_FREQUENCY").equalsIgnoreCase("") == false) {
                TEMP_DIR_CLEAN_FREQUENCY = Integer.parseInt(ini.getValue("TEMP_DIR_CLEAN_FREQUENCY"));
            }
            else {
                TEMP_DIR_CLEAN_FREQUENCY = 3600;
            }
			*/



			SEARCH_CONTENT_INDEXING = wizbini.cfgSysSetupadv.getKmIndexingThread().isEnabled();
			SEARCH_INDEX_BUILDER_SLEEP_TIME = wizbini.cfgSysSetupadv.getKmIndexingThread().getInterval();
			/*
            val = ini.getValue("SEARCH_CONTENT_INDEXING");
            if (val != null && val.equalsIgnoreCase("YES")) {
                SEARCH_CONTENT_INDEXING = true;
            }
            else {
                SEARCH_CONTENT_INDEXING = false;
            }
            if (ini.getValue("SEARCH_INDEX_BUILDER_SLEEP_TIME") != null && ini.getValue("SEARCH_INDEX_BUILDER_SLEEP_TIME").equalsIgnoreCase("") == false) {
                SEARCH_INDEX_BUILDER_SLEEP_TIME = Integer.parseInt(ini.getValue("SEARCH_INDEX_BUILDER_SLEEP_TIME"));
            }
            else {
                SEARCH_INDEX_BUILDER_SLEEP_TIME = 10;
            }
			*/


			//LOG_FOLDER = wizbini.getSystemLogDirAbs();
			LOG_FOLDER = wizbini.cfgSysSetupadv.getLogDir().getName();
			logDirIsRelative = wizbini.cfgSysSetupadv.getLogDir().isRelative();
			if (logDirIsRelative) {
				logFolderPath = DOC_ROOT + dbUtils.SLASH + LOG_FOLDER;
			} else {
				logFolderPath = LOG_FOLDER;
			}

            MAIL_ACCOUNT = Application.MAIL_SERVER_ACCOUNT_TYPE;
            /*
            SEND_MAIL_URL = ini.getValue("SEND_MAIL_URL");
            MAIL_NOTES_LOG = ini.getValue("MAIL_NOTES_LOG");
            if( ( MAIL_SCHEDULER_ENABLE = ini.getValue("MAIL_SCHEDULER_ENABLE") ) == null )
                MAIL_SCHEDULER_ENABLE = "true";
            LOG_FOLDER = ini.getValue("LOG_FOLDER");
            MAIL_ACCOUNT = ini.getValue("MAIL_ACCOUNT");
            try{
                MAIL_REFRESH = Integer.parseInt(ini.getValue("MAIL_REFRESH"));
            }catch(NumberFormatException e) {
                MAIL_REFRESH = 10;
            }
            try{
                MAIL_ATTEMPT = Integer.parseInt(ini.getValue("MAIL_ATTEMPT"));
            }catch(NumberFormatException e) {
                MAIL_ATTEMPT = 3;
            }
            */



            /*
            if( ( ITEM_REQUIREMENT_SCHEDULER_ENABLE = ini.getValue("ITEM_REQUIREMENT_SCHEDULER_ENABLE") ) == null )
                ITEM_REQUIREMENT_SCHEDULER_ENABLE = "true";
            try{
                ITEM_REQUIREMENT_REFRESH = Integer.parseInt(ini.getValue("ITEM_REQUIREMENT_REFRESH"));
            }catch(NumberFormatException e) {
                ITEM_REQUIREMENT_REFRESH = 3600;
            }
            if( ( ITEM_TREENODE_PATH_SCHEDULER_ENABLE = ini.getValue("ITEM_TREENODE_PATH_SCHEDULER_ENABLE") ) == null )
                ITEM_TREENODE_PATH_SCHEDULER_ENABLE = "true";
            try{
                CRITERIA_REFRESH = Integer.parseInt(ini.getValue("CRITERIA_REFRESH"));
            }catch(NumberFormatException e) {
                CRITERIA_REFRESH = 10;
            }
            val = ini.getValue("CRITERIA_ENABLED");
            if (val != null && val.equalsIgnoreCase("YES"))
                CRITERIA_ENABLED = true;
            else
                CRITERIA_ENABLED = false;
			*/


            WIZCASE_XML_HOME = wizbini.getWizcaseHomeDirAbs();
            WIZCASE_TEMP_DIR = wizbini.getWizcaseTempAbs();
            /*
            WIZCASE_XML_HOME = ini.getValue("WIZCASE_XML_HOME");
            WIZCASE_TEMP_DIR = ini.getValue("WIZCASE_TEMP_DIR");
            */



            DES_KEY = wizbini.cfgSysSetupadv.getDesKey();
            /*
            try{
                DES_KEY = Long.parseLong(ini.getValue("DES_KEY"));
            }catch(Exception e){
                DES_KEY = 1;
            }
			*/

			//KM related
            KM_REFRESH_TIME = wizbini.cfgSysSetupadv.getKmIndexingThread().getInterval();
            KM_DAILY_MAIL_SEND_TIME = wizbini.cfgSysSetupadv.getKmSubscriptionNotifyThread().getDailyMailSendTime();
            KM_WEEKLY_MAIL_SEND_TIME = wizbini.cfgSysSetupadv.getKmSubscriptionNotifyThread().getWeeklyMailSendTime();
            /*
            val = ini.getValue("KM_SCHEDULER_ENABLE");
            if (val != null && val.equalsIgnoreCase("TRUE"))
                KM_SCHEDULER_ENABLE = true;
            try{
                KM_REFRESH_TIME = Integer.parseInt(ini.getValue("KM_REFRESH_TIME"));
            }catch(NumberFormatException e) {
                // do nothing;
            }
            try{
                KM_DAILY_MAIL_SEND_TIME = Integer.parseInt(ini.getValue("KM_DAILY_MAIL_SEND_TIME"));
            }catch(NumberFormatException e) {
                // do nothing;
            }
            try{
                KM_WEEKLY_MAIL_SEND_TIME = Integer.parseInt(ini.getValue("KM_WEEKLY_MAIL_SEND_TIME"));
            }catch(NumberFormatException e) {
                // do nothing;
            }
            */



			DEFAULT_GRADE_ID = wizbini.cfgSysSetupadv.getEnterpriseManager().getDefaultGradeId();
			/*
            try{
                DEFAULT_GRADE_ID = Integer.parseInt(ini.getValue("DEFAULT_GRADE_ID"));
            }catch(Exception e){
                DEFAULT_GRADE_ID = -1;
            }
			*/
			UPLOAD_USER_PWD_ENABLE = wizbini.cfgSysSetupadv.getUserBatchUpload().isGeneratePassword();
            /*
            val = ini.getValue("UPLOAD_USER_PWD_ENABLE");
            if (val != null && val.equalsIgnoreCase("YES"))
                UPLOAD_USER_PWD_ENABLE = true;
            else
                UPLOAD_USER_PWD_ENABLE = false;
            */

            UPLOAD_USER_DISTINCT_CHECK_ENABLE = wizbini.cfgSysSetupadv.getUserBatchUpload().isCheckDistinctUser();
            /*
            val = ini.getValue("UPLOAD_USER_DISTINCT_CHECK_ENABLE");
            if (val != null && val.equalsIgnoreCase("YES"))
                UPLOAD_USER_DISTINCT_CHECK_ENABLE = true;
            else
                UPLOAD_USER_DISTINCT_CHECK_ENABLE = false;
            */


            // KM library policy
            KM_LIB_BORROW_LIMIT = wizbini.cfgSysSetupadv.getKmLibrary().getBorrowLimit();
            KM_LIB_OVERDUE_LIMIT = wizbini.cfgSysSetupadv.getKmLibrary().getOverdueLimit();
            KM_LIB_RENEW_LIMIT = wizbini.cfgSysSetupadv.getKmLibrary().getRenewLimit();
            KM_LIB_DUE_DAY = wizbini.cfgSysSetupadv.getKmLibrary().getDueDay();
            /*
            try{
                KM_LIB_BORROW_LIMIT = Integer.parseInt(ini.getValue("KM_LIB_BORROW_LIMIT"));
            }catch(NumberFormatException e) {
                // do nothing;
            }
            try{
                KM_LIB_OVERDUE_LIMIT = Integer.parseInt(ini.getValue("KM_LIB_OVERDUE_LIMIT"));
            }catch(NumberFormatException e) {
                // do nothing;
            }
            try{
                KM_LIB_RENEW_LIMIT = Integer.parseInt(ini.getValue("KM_LIB_RENEW_LIMIT"));
            }catch(NumberFormatException e) {
                // do nothing;
            }
            try{
                KM_LIB_DUE_DAY = Integer.parseInt(ini.getValue("KM_LIB_DUE_DAY"));
            }catch(NumberFormatException e) {
                // do nothing;
            }
            */

            GPM_FULL_PATH_SEPARATOR = wizbini.cfgSysSetupadv.getGroupMemberFullPathSeparator();
            /*
            val = ini.getValue("GPM_FULL_PATH_SEPARATOR");
            if(val != null)
                GPM_FULL_PATH_SEPARATOR = val;
			*/


    }

    public String getAbsoluteXSLFile(String xsl_root, String xslFile)
            throws cwException
    {
            String absoluteXSLFile = null;

            //if xsl_root is valid, check if the xslFile is a valid file
            if (xsl_root != null && xsl_root.length() !=0) {

                //absoluteXSLFile = DOC_ROOT + dbUtils.SLASH + xsl_root + dbUtils.SLASH + xslFile;
                File fXSLPath = new File(DOC_ROOT, xsl_root);
                File file = new File(fXSLPath, xslFile);
                absoluteXSLFile = file.getAbsolutePath();
                //File file = new File(absoluteXSLFile);
                if (file.exists()) {
                    return absoluteXSLFile;
                }
            }
            //if the xsl_root is not valid, get the stylesheet from INI_XSL_HOME
            xsl_root = INI_XSL_HOME;
            absoluteXSLFile = DOC_ROOT + dbUtils.SLASH + xsl_root + dbUtils.SLASH + xslFile;
            return (absoluteXSLFile);
    }

    public void processFiles(String absoluteXMLFile, String xslFile, PrintWriter resultWriter, String xsl_root)
            throws cwException
    {
          String absoluteXSLFile = getAbsoluteXSLFile(xsl_root, xslFile);

          cwXSL.processFiles(absoluteXMLFile, absoluteXSLFile, resultWriter, ENCODING, DEBUG, COMP_XSL, INI_XSL_LOG, INI_XSL_LOG_ENABLED);
    }

    public void procXSLFile(String xml, String xslFile, Writer resultWriter, String xsl_root)
            throws cwException
    {
          String absoluteXSLFile = getAbsoluteXSLFile(xsl_root, xslFile);

          cwXSL.procAbsoluteXSLFile(xml, absoluteXSLFile, resultWriter, ENCODING, DEBUG, COMP_XSL, INI_XSL_LOG, INI_XSL_LOG_ENABLED);

    }

    public String siteAsXML()
    {
        StringBuffer xml = new StringBuffer();
        xml.append(dbUtils.xmlHeader);
        xml.append("<site encoding=\"").append(ENCODING).append("\" style=\"")
           .append(STYLE).append("\">").append(dbUtils.NEWL);
        xml.append("<desc></desc>").append(dbUtils.NEWL);
        xml.append("</site>").append(dbUtils.NEWL);

        return xml.toString();

    }

    public void outputXML (PrintWriter out , String xml)
    {

        String cleanXML = cwUtils.escNull(xml);

        if (DEBUG)
            out.println(cleanXML);
        else
            out.println("<h2><b>Sorry, the page cannot be found.</b></h2>");

        return;
    }

    public void dispDebugMesg (PrintWriter out , String mesg)
    {
        if (DEBUG) {
            out.println("<b>Error Message :</b><br>");
            out.println(cwUtils.esc4JS(mesg));
        }else {
            out.println("Sorry, your request cannot be processed.");
        }
    }


    public String getTserverInfoAsXML(loginProfile prof)
    {
        StringBuffer xml = new StringBuffer();

        xml.append(cwUtils.xmlHeader)
           .append("<tserver enabled=\"").append(TSENABLED).append("\">").append(cwUtils.NEWL)
           .append(prof.asXML()).append(cwUtils.NEWL)
           .append("</tserver>").append(cwUtils.NEWL);

        return xml.toString();
    }

    public void changeActiveReqNum(int val){
        activeRequestNum  += val;
        // for a new active request
        if (val == 1) {
            last_req_time = System.currentTimeMillis();
        }
    }

    public void sysShutdown(String password, PrintWriter out) {

        if (password != null && INI_SYSTEM_PASSWORD !=null
            && password.equals(INI_SYSTEM_PASSWORD)) {
            shutdownMode = true;
            out.println("System changed to shutdown mode.");
        }else {
            out.println("Sorry, you don't have permission.");
        }
    }

    public  void sysStartUp(String password, PrintWriter out) {

        if (password != null && INI_SYSTEM_PASSWORD !=null
            && password.equals(INI_SYSTEM_PASSWORD)) {
            shutdownMode = false;
            out.println("System changed to normal mode.");
        }else {
            out.println("Sorry, you don't have permission.");
        }
    }

    public void sysGetReqInfo(String password, PrintWriter out) {

        if (password != null && INI_SYSTEM_PASSWORD !=null
            && password.equals(INI_SYSTEM_PASSWORD)) {
            if (shutdownMode) {
                out.println("System in shutdown mode. <br/>");
            }else {
                out.println("System in normal mode. <br/>");
            }
            out.println("Number of active requests = " + activeRequestNum + "</br>");
            Timestamp last_req_date = new Timestamp(last_req_time);
            out.println("Time of the last request = " + last_req_date.toString());

        }else {
            out.println("Sorry, you don't have permission.");
        }
    }

    public String transformXML(String inXML, String inXSLFilename, String xsl_root) throws cwException {
        try {
            if (inXSLFilename == null || inXSLFilename.length() == 0)
                throw new cwException("Invalid stylesheet for transform");

            StringWriter outXML = new StringWriter(2048);
            procXSLFile(inXML, inXSLFilename, outXML, xsl_root);
            outXML.close();
            return outXML.toString();
        } catch (IOException e) {
        	CommonLog.error("error_xml ------inXSLFilename=" +inXSLFilename);
        	CommonLog.error(inXML);
            throw new cwException(e.getMessage());
        }
    }
}