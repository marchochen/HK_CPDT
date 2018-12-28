package com.cw.wizbank.enterprise;

import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.db.DbIMSLog;
import com.cw.wizbank.db.view.ViewIMSLog;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.*;
import com.cwn.wizbank.utils.CommonLog;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

public class IMSLog{
    
    //Log filename
    public final static String[] FILENAME_LIST = { IMSUtils.SUCCESS_FILE, IMSUtils.FAILURE_FILE, IMSUtils.ERROR_FILE};
    public final static String[] LOG_LIST = {"SUCCESS", "UNSUCCESS", "ERROR" };
    
    //Process 
    public final static String IMSLOG_PROCESS_IMPORT = "IMPORT";
    public final static String IMSLOG_PROCESS_DELETE = "DELETE";
    public final static String IMSLOG_PROCESS_EXPORT = "EXPORT";
    
    //Type
    public final static String IMSLOG_TYPE_USER = "USER";
    public final static String IMSLOG_TYPE_DEL_USER = "DEL";
    public final static String IMSLOG_TYPE_CPD = "CPD";
    public final static String IMSLOG_TYPE_COURSE = "COURSE";
    public final static String IMSLOG_TYPE_ENROLLMENT = "ENROLLMENT";
    public final static String IMSLOG_TYPE_CREDIT = "CREDIT";
    public final static String IMSLOG_TYPE_CPDAWARDHOURS = "CPDAWARDHOURS";
    
    public final static String URI_PATH_SEPARATOR = "/";
    
    public final static String ACTION_TRIGGERED_BY_UI = "UI";
    public final static String ACTION_TRIGGERED_BY_BATCH = "BATCH";
    
    //Log Sub-Folder
    public final static String FOLDER_IMSLOG = "ims";
    public final static String FOLDER_DEL_IMSLOG = "del";

    public final static String CPD_FOLDER_IMSLOG = "cpd";
    
    public final static String FOLDER_CPDLOG = "cpd";
    
    // for clp import enrollment]
    public final static String ITM = "ITM_";
    
    public static boolean CPD = false;
    public static boolean DEL = false;

    
    //History Session Key
    private static final String SESS_LOG_HISTORY_LOG_ID = "SESS_LOG_HISTORY_LOG_ID";
    private static final String SESS_LOG_HISTORY_TIMESTAMP = "SESS_LOG_HISTORY_TIMESTAMP";
    private static final String SESS_LOG_HISTORY_TOTAL_REC = "SESS_LOG_HISTORY_TOTAL_REC";
    private static final String SESS_LOG_HISTORY_TOTAL_PAGE = "SESS_LOG_HISTORY_TOTAL_PAGE";
    
    /**
    * Insert a record to database
    @param con db connection 
    @param prof login profile
    @param dbIlg Object to be inset to db
    */
    public void ins(Connection con, loginProfile prof, DbIMSLog dbIlg)
        throws SQLException {
            
            dbIlg.ilg_create_usr_id = prof.usr_id;
            dbIlg.ilg_create_timestamp = cwSQL.getTime(con);
            dbIlg.ilg_tcr_id=prof.my_top_tc_id;
            DbIMSLog.ins(con, dbIlg);
    
            return;
        }
    
    
    /**
    * Retrun a hashtable of filename as a key and file uri as the value
    @param ilg_id record id
    @param static_env qdb static environment
    */
    public static Hashtable<String,String> getLogFilesURI(long ilg_id, qdbEnv static_env)
        throws cwException {
    	
            Hashtable<String,String> h_file_uri = new Hashtable<String,String>();
            String uri = "";
            File logFileFolder = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
            if( logFileFolder.exists() ){
                	if(CPD){
            		logFileFolder = new File(logFileFolder, CPD_FOLDER_IMSLOG);
            	}else if(DEL){
            		logFileFolder = new File(logFileFolder, FOLDER_DEL_IMSLOG);
            	}else{
                    logFileFolder = new File(logFileFolder, FOLDER_IMSLOG);
                }
                if( logFileFolder.exists() ) {
                    logFileFolder = new File(logFileFolder, Long.toString(ilg_id));
                    if( logFileFolder.exists() ){
                        for(int i=0; i<3; i++){
                            File logFile = new File(logFileFolder, FILENAME_LIST[i]);
                            if( logFile.exists() ){
                            	//获取日志文件，如果日志文件中没有记录，将不显示
                            	int size;
                                InputStream f1;
								try {
									f1 = new FileInputStream(logFile.getAbsolutePath());
									size = f1.available();
									f1.close();
									if(size!=0){
	                                uri = URI_PATH_SEPARATOR + static_env.LOG_FOLDER
	                                    + URI_PATH_SEPARATOR + (DEL? FOLDER_DEL_IMSLOG:CPD ? CPD_FOLDER_IMSLOG : FOLDER_IMSLOG)
	                                    + URI_PATH_SEPARATOR + ilg_id
	                                    + URI_PATH_SEPARATOR + FILENAME_LIST[i];
									}else{
										uri="";
									}
                                        
                                h_file_uri.put(FILENAME_LIST[i], uri);
								} catch (Exception e) {
									CommonLog.error(e.getMessage(),e);
								}      
                            } else {

                                h_file_uri.put(FILENAME_LIST[i], "");

                            }
                        }
                    }
                }
            }
            
            return h_file_uri;
            
        }

    
    /**
    * Return the uri of the uploaded file
    @param ilg_id record id
    */
    public static String getUploadedFileURI(long ilg_id, String ilg_filename, qdbEnv static_env)
        throws cwException {
            String uri = "";
            // 文件特殊字符已经转译过，需要重新转译
            ilg_filename = dbUtils.unEscXML(ilg_filename);
            File uploadedFile = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
            if( uploadedFile.exists() ) {
            	if(CPD){
            		uploadedFile = new File(uploadedFile, CPD_FOLDER_IMSLOG);
            	}else if(DEL){
            		uploadedFile = new File(uploadedFile, FOLDER_DEL_IMSLOG);
            	}else {
                    uploadedFile = new File(uploadedFile, FOLDER_IMSLOG);
                }
                if( uploadedFile.exists() ) {
                    uploadedFile = new File(uploadedFile, Long.toString(ilg_id));
                    if( uploadedFile.exists() ) {
                        uploadedFile = new File(uploadedFile, ilg_filename);
                        if( uploadedFile.exists() ) {
                        	// 需要把已经编译过的ilg_filename,重新反编译
                            uri = URI_PATH_SEPARATOR + static_env.LOG_FOLDER
                                + URI_PATH_SEPARATOR + (DEL? FOLDER_DEL_IMSLOG:CPD ? CPD_FOLDER_IMSLOG : FOLDER_IMSLOG)
                                + URI_PATH_SEPARATOR + ilg_id
                                + URI_PATH_SEPARATOR + dbUtils.esc4XML(ilg_filename);
                        }
                    }
                }
            }
            return uri;

        }


    public static String readErrorLogContent(qdbEnv static_env, long ilg_id)
        throws IOException {
            
            return readLogContent(static_env, ilg_id, IMSUtils.ERROR_FILE);
            
        }


    public static String readSuccessLogContent(qdbEnv static_env, long ilg_id)
        throws IOException {
            
            return readLogContent(static_env, ilg_id, IMSUtils.SUCCESS_FILE);
            
        }


    private static String readLogContent(qdbEnv static_env, long ilg_id, String filename)
        throws IOException {
            
            File logFile = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
            if( logFile.exists() ){
                logFile = new File(logFile, CPD ? CPD_FOLDER_IMSLOG : FOLDER_IMSLOG);
                if( logFile.exists() ) {
                    logFile = new File(logFile, Long.toString(ilg_id));
                    if( logFile.exists() ){
                        logFile = new File(logFile, filename);
                        if( logFile.exists() )
                            return (IMSUtils.readFile(logFile)).toString();
                    }
                }
            }
            return null;
        }


    /**
    * Return string as xml format of the export history
    */
    public String exportHistoryXML(Connection con, HttpSession sess, qdbEnv static_env, String ilg_type, loginProfile prof, cwPagination cwPage)
        throws SQLException, cwException ,  cwSysMessage{
            
            String[] ilg_process = {IMSLOG_PROCESS_EXPORT};
            return getHistoryXML(con, sess, static_env, ilg_process, ilg_type,  cwPage, 0, null);

        }

    /**
    * Return string as xml format of the import history
    */
    public String importHistoryXML(Connection con, HttpSession sess, qdbEnv static_env, String ilg_type, loginProfile prof, cwPagination cwPage)
        throws SQLException, cwException ,  cwSysMessage{
            
            String[] ilg_process = {IMSLOG_PROCESS_IMPORT};
            return getHistoryXML(con, sess, static_env, ilg_process, ilg_type, cwPage, 0, null);
            
        }

    /**
    * Return string as xml format of the export/import history
     * @param prof 
    */
    public String getHistoryXML(Connection con, HttpSession sess, qdbEnv static_env, String[] ilg_process, String ilg_type,  cwPagination cwPage, long itm_id, loginProfile prof)
        throws SQLException, cwException , cwSysMessage{
            
            StringBuffer xml = new StringBuffer(1024);
            xml.append("<ims_log>");
            boolean useSess = false;
            
            if( cwPage.curPage == 0 ) cwPage.curPage = 1;
            if( cwPage.pageSize == 0 ) cwPage.pageSize = 10;
            
            int start = (cwPage.curPage - 1) * cwPage.pageSize + 1;
            int end = start + cwPage.pageSize - 1;
            
            Vector v_ilg_id = null;
            if( sess.getAttribute(SESS_LOG_HISTORY_TIMESTAMP) != null && ((Timestamp)sess.getAttribute(SESS_LOG_HISTORY_TIMESTAMP)).equals(cwPage.ts) ) {
                Vector v_id = (Vector)sess.getAttribute(SESS_LOG_HISTORY_LOG_ID);
                v_ilg_id = new Vector();
                for(int i=start-1; i<end && i<v_id.size(); i++)
                    v_ilg_id.addElement(v_id.elementAt(i));
                cwPage.totalPage = ((Integer)sess.getAttribute(SESS_LOG_HISTORY_TOTAL_PAGE)).intValue();
                cwPage.totalRec = ((Integer)sess.getAttribute(SESS_LOG_HISTORY_TOTAL_REC)).intValue();
                useSess = true;
            } else {
                cwPage.ts = cwSQL.getTime(con);
                sess.setAttribute(SESS_LOG_HISTORY_TIMESTAMP, cwPage.ts);
            }
            //获取
            ViewIMSLog.ViewUsrIMSLog[] logs = ViewIMSLog.getHistory(con, ilg_process, ilg_type, v_ilg_id, prof, cwPage.sortCol, cwPage.sortOrder, itm_id);

            v_ilg_id = new Vector();
            int count = 0;
            for (int i=0; i<logs.length; i++){
                count++;
                if( !useSess ) {
                    v_ilg_id.addElement(new Long(logs[i].dbIMSLog.ilg_id));
                    if( count < start || count > end ){
                        continue;
                    }
                }
                xml.append("<record ")
                   .append(" id=\"").append(logs[i].dbIMSLog.ilg_id).append("\" ")
                   .append(" timestamp=\"").append(logs[i].dbIMSLog.ilg_create_timestamp).append("\" ")
                   .append(" performer=\"").append(logs[i].usr_display_bil).append("\" ")
                   .append(" type=\"").append(logs[i].dbIMSLog.ilg_type).append("\" ")
                   .append(" method=\"").append(logs[i].dbIMSLog.ilg_method).append("\" ")
                   .append(" process=\"").append(logs[i].dbIMSLog.ilg_process).append("\">");
                
                xml.append("<desc>").append(logs[i].dbIMSLog.ilg_desc).append("</desc>");

                xml.append("<uploaded_file>")
                   .append("<filename>").append(logs[i].dbIMSLog.ilg_filename).append("</filename>") 
                   .append("<uri>").append(getUploadedFileURI(logs[i].dbIMSLog.ilg_id, logs[i].dbIMSLog.ilg_filename, static_env)).append("</uri>")
                   .append("</uploaded_file>");

                xml.append("<log_file_list>");
                Hashtable h_file_uri = getLogFilesURI(logs[i].dbIMSLog.ilg_id, static_env);
                for(int k=0; k<3; k++){
                	
                    xml.append("<log_file ")
                       .append(" type=\"").append(LOG_LIST[k]).append("\">")
                       .append("<filename>").append(FILENAME_LIST[k]).append("</filename>")
                       .append("<uri>").append((String)h_file_uri.get(FILENAME_LIST[k])).append("</uri>")
                       .append("</log_file>");
                }
                xml.append("</log_file_list>");

                xml.append("</record>");
            }
            
            if( !useSess ) {
                sess.setAttribute(SESS_LOG_HISTORY_LOG_ID, v_ilg_id);
                cwPage.totalRec = v_ilg_id.size();
                cwPage.totalPage = (int)Math.ceil( (float)v_ilg_id.size() / cwPage.pageSize );
                sess.setAttribute(SESS_LOG_HISTORY_TOTAL_PAGE, new Integer(cwPage.totalPage));
                sess.setAttribute(SESS_LOG_HISTORY_TOTAL_REC, new Integer(cwPage.totalRec));
            }
            
            xml.append(cwPage.asXML());
            if(itm_id >0){
            	xml.append(aeItem.genItemActionNavXML(con, itm_id, prof));
            }
            xml.append("</ims_log>");
            xml.append(aeItem.getItemXMLForNav ( con, itm_id));
            return xml.toString();
            
        }



    public void saveUploadedFile(qdbEnv static_env, long ilg_id, File srcFile)
        throws IOException {

            File logFolder = new File(static_env.DOC_ROOT, static_env.LOG_FOLDER);
            if( !logFolder.exists() ) 
                logFolder.mkdir();                
            logFolder = new File(logFolder, CPD ? CPD_FOLDER_IMSLOG : FOLDER_IMSLOG);
            if( !logFolder.exists() ) 
                logFolder.mkdir();
            logFolder = new File(logFolder, Long.toString(ilg_id));
            if( !logFolder.exists() )
                logFolder.mkdir();
            IMSUtils.fileSaveAs(srcFile, logFolder);
            return;
        }
        
        
    public static String getResultXML(qdbEnv static_env, long ilg_id, Hashtable h_status_count)
        throws cwException, IOException {
            
            StringBuffer xml = new StringBuffer(1024);
            String logFolderUri = IMSLog.URI_PATH_SEPARATOR
                                + static_env.LOG_FOLDER
                                + IMSLog.URI_PATH_SEPARATOR
                                + (CPD ? CPD_FOLDER_IMSLOG : FOLDER_IMSLOG)
                                + IMSLog.URI_PATH_SEPARATOR
                                + ilg_id
                                + IMSLog.URI_PATH_SEPARATOR;
                                
            xml.append("<data_import id=\"").append(ilg_id).append("\">");
            
            xml.append("<success_entity>")
               .append("<total>").append(h_status_count.get(IMSEnterprise.STATUS_SUCCESS)).append("</total>")
               .append("<log_file>")
               .append("<filename>").append(IMSUtils.SUCCESS_FILE).append("</filename>")
               .append("<uri>").append(logFolderUri + IMSUtils.SUCCESS_FILE).append("</uri>")
               .append("</log_file>")
               .append("</success_entity>");
            
            xml.append("<unsuccess_entity>")
               .append("<total>").append(h_status_count.get(IMSEnterprise.STATUS_UNSUCCESS)).append("</total>")
               .append("<log_file>")
               .append("<filename>").append(IMSUtils.FAILURE_FILE).append("</filename>")
               .append("<uri>").append(logFolderUri + IMSUtils.FAILURE_FILE).append("</uri>")
               .append("</log_file>")
               .append("</unsuccess_entity>");
            
            xml.append("<error>")
               .append("<log_file>")
               .append("<filename>").append(IMSUtils.ERROR_FILE).append("</filename>")
               .append("<uri>").append(logFolderUri + IMSUtils.ERROR_FILE).append("</uri>")
               .append("</log_file>")
               .append("<reason>").append(cwUtils.esc4XML(IMSLog.readErrorLogContent(static_env, ilg_id))).append("</reason>")
               .append("</error>");

            xml.append("</data_import>");
            
            return xml.toString();            
        }
        
}