package com.cw.wizbank.enterprise;
import java.io.*;
import java.lang.Long;

import javax.naming.NamingException;
import javax.xml.bind.*;

import java.sql.*;
import java.util.Hashtable;

import org.imsglobal.enterprise.*;

import com.cw.wizbank.util.*;
import com.cw.wizbank.batch.batchUtil.BatchUtils;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbThresholdSynLog;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.TempDirCleaner;
import com.cwn.wizbank.utils.CommonLog;

public class IMSEnterpriseApp {
    public static final String allSchemaContext = "org.imsglobal.enterprise"
                                                 + ":org.imsglobal.enterprise.cwn"
                                                 ;
    
    private static final String MSG_INVALID_INPUT_ARGV = "Usage: EnterpriseApp <-inifile filename> <-configpath configpath> <-import|-export option filename> [-logdir <directory name>] ";

    protected String xmlFilename;
    protected String iniFile;
    protected cwIniFile cwIni;
    protected String appnRoot;
    protected WizbiniLoader wizbini;
    protected IMSEnterprise imsEnterprise; 
    protected Enterprise _enterprise;
    protected String action;
    protected String actionRule;
    protected String type;
    protected String rootLogDir;
    protected String destinLogDir;
    protected String[] recipientList = null;
    protected Connection con;
    protected long siteId;
    protected Timestamp startTime;
    protected Timestamp endTime;
    protected boolean cleanUp = true;
    protected String template;
    
    private String syn_type;
    private int syn_success;
    private int syn_unsucces;
    private Timestamp syn_start_time;
    private Timestamp syn_end_time;
    private String syn_remark;
    
    static String exportSource = "wizBank";
    static String importSource = "wizBank";

    public static void main(String[] argv) {
        IMSEnterpriseApp app = new IMSEnterpriseApp();
//        try{
            try{
                for (int i = 0; i < argv.length; i++) {
                    if (argv[i].charAt(0) == '-') {
                        if (argv[i].equals("-inifile")){
                            app.iniFile = argv[++i];
                            app.cwIni = new cwIniFile(app.iniFile);
                        }else if (argv[i].equals("-configpath")){
                            app.appnRoot = argv[++i];
                            app.wizbini = new WizbiniLoader(app.appnRoot);
                        }else if (argv[i].equals("-import")){
                            app.action = IMSEnterprise.ACTION_IMPORT;
                            app.type = argv[++i];
                            app.xmlFilename = argv[++i];
                        }else if (argv[i].equals("-export")){
                            app.action = IMSEnterprise.ACTION_EXPORT;
                            app.type = argv[++i];
                            app.xmlFilename = argv[++i];
                        }else if (argv[i].equals("-rule")){
                            // action role only make sense on upload application
                            app.actionRule = argv[++i];
                        }else if (argv[i].equals("-start")){
                            try{
                                app.startTime = IMSUtils.constructTimestamp(argv[++i]);                            
                            }catch(Exception e){
                                System.err.println("Start time incorrect foramt : YYYY-MM-DD");
                                System.exit(1);
                            }
                        }else if (argv[i].equals("-end")){
                            try{
                                app.endTime = IMSUtils.constructTimestamp(argv[++i]);                            
                            }catch(Exception e){
                                System.err.println("End time incorrect foramt : YYYY-MM-DD");
                                System.exit(1);
                            }
                        }else if (argv[i].equals("-eventdriven")){
                            app.cleanUp = false;
                        }else if (argv[i].equals("-logdir")){
                            app.rootLogDir = argv[++i];
                        }else if (argv[i].equals("-destinlogdir")){
                            app.destinLogDir = argv[++i];
                        }else{                  
                            throw new IllegalArgumentException("Invalid argument:" +  argv[i] + cwUtils.NEWL + MSG_INVALID_INPUT_ARGV);
                        }
                    }else{ 
                        throw new IllegalArgumentException("Invalid argument:" +  argv[i] + cwUtils.NEWL + MSG_INVALID_INPUT_ARGV);
                    }
                }
            }catch (Exception e){
            	CommonLog.error(MSG_INVALID_INPUT_ARGV);
            	CommonLog.error(e.getMessage());
    			System.exit(1);
            }

            if (app.action == null || app.appnRoot == null || app.iniFile == null ){
            	CommonLog.error(MSG_INVALID_INPUT_ARGV);
    			System.exit(1);
            }
            
            try{
            	app.con = BatchUtils.openDB(app.appnRoot);
                app.init();    
            }catch (Exception e){
            	CommonLog.error("ERROR IN INIT EnterpriseApp." + e.getMessage());
    			System.exit(1);
            }
            
            // cleanup log dir
            if (app.rootLogDir!=null){
                String sExpireDuration = app.cwIni.getValue("LOG_FILE_TIME_TO_LIVE");
                if (sExpireDuration!=null){
                    long expireDuration = Long.parseLong(app.cwIni.getValue("LOG_FILE_TIME_TO_LIVE"));
                    if (expireDuration>0){
                        TempDirCleaner cleaner = new TempDirCleaner();  
        		        cleaner.addTargetDir(app.rootLogDir,expireDuration*24*60*60);
        		        cleaner.cleanTempDir();
        		    }
        		}
		    }
            try{
                if (app.action.equals(IMSEnterprise.ACTION_IMPORT)){
                    app.importXML();                
//                    app.moveXML();
                }else if (app.action.equals(IMSEnterprise.ACTION_EXPORT)){
                    exportSource = app.cwIni.getValue("DATA_SOURCE"); 
                    app.exportXML();                        
                }else{
                    throw new IllegalArgumentException("invalid action.");            
                }
            }finally{
                if (app.action.equals(IMSEnterprise.ACTION_IMPORT)){
                    app.moveXML(true);
                }else if (app.action.equals(IMSEnterprise.ACTION_EXPORT)){
                    app.moveXML(false);
                }
                if(app.checkEmailCondition()){
	                if (app.recipientList!=null && app.recipientList.length>0){
	                    app.sendNotify();    
	                }
                }
            }
    }
    /**
    initialize value before real work
    initialize Log Directory in IMSUtil
     * @throws ClassNotFoundException 
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     * @throws NamingException 
    */
    protected void init() throws cwException, IOException, SQLException, NamingException, InstantiationException, IllegalAccessException, ClassNotFoundException{
        this.syn_start_time= new   Timestamp(System.currentTimeMillis()); 
        siteId = BatchUtils.getSiteId(iniFile); 
        if (destinLogDir==null){
            IMSUtils.getLogDir(rootLogDir, cwSQL.getTime(con));
        }else{
            IMSUtils.setLogDir(destinLogDir);
        }
        
        
        qdbEnv env = new qdbEnv();
        env.init(wizbini); 
        
        String sDebug = cwIni.getValue("DEBUG");
        if (sDebug!=null){
            IMSUtils.setDebug(Boolean.valueOf(sDebug).booleanValue());
        }

        String bMailNotify = cwIni.getValue("MAIL_NOTIFY");
        if (bMailNotify!=null && Boolean.valueOf(bMailNotify).booleanValue()){
            String strAddress = cwIni.getValue("MAIL_NOTIFY_LIST");
            if (strAddress!=null){
                recipientList = cwUtils.splitToString(strAddress , "~");
            }
        }
    }
    
    /**********************************************
    /   for import
    /
    **********************************************/
    /**
    to read and save data from XML to db
    pre-defined type, xmlFile
    */
    protected void importXML(){
        boolean bContinue = true;
        if (checkImportType() && checkActionRule()){
            try{
                buildTreesFromXML();
                importSource = _enterprise.getProperties().getDatasource();
            }catch (Exception e){
                bContinue = false;
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "Error in parsing xml.", e);
            }
            if (bContinue){
                // determine whether it is needed to encrypt the password in the xml
                String pwdEncryptedIndc = cwIni.getValue("PWD_ENCRYPTED");
                // by default, no need to do any password encryption
                boolean encryptPwd = false;
                if (pwdEncryptedIndc != null) {
                    pwdEncryptedIndc = pwdEncryptedIndc.trim().toUpperCase();
                    if (pwdEncryptedIndc.equals("NO")) {
                        encryptPwd = true;
                    }
                }
                
                try{
                    loginProfile wbProfile = BatchUtils.getProf(con, siteId, cwIni, wizbini);
                    imsEnterprise = new IMSEnterprise(_enterprise);
                   	dbThresholdSynLog dblog=new dbThresholdSynLog();
    		        Hashtable htb=imsEnterprise.updDB(con, cwIni, wbProfile, siteId, type, actionRule, encryptPwd, null);
    		        this.syn_end_time= new   Timestamp(System.currentTimeMillis()); 
    		        this.syn_type="IMS_"+this.type;
    		        if(this.type.equalsIgnoreCase(IMSEnterprise.TYPE_USER)||this.type.equalsIgnoreCase(IMSEnterprise.TYPE_USER_GROUP)){
    		        	this.syn_remark=IMSEnterprise.TYPE_REMARK_USER;
    		        }else if(this.type.equalsIgnoreCase(IMSEnterprise.TYPE_USER_COURSE)||this.type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE_ENROLLMENT)
    		        		||this.type.equalsIgnoreCase(IMSEnterprise.TYPE_RESULT)){
    		        	this.syn_remark=IMSEnterprise.TYPE_REMARK_ENROLL;
    		        }else if (this.type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE)) {
    		        	this.syn_remark=IMSEnterprise.TYPE_REMARK_COURCE;
    		        }
    		        if (this.type.equalsIgnoreCase(IMSEnterprise.TYPE_USER_GROUP)) {
    		        	this.syn_success=((Integer)((Hashtable)htb.get(IMSEnterprise.TYPE_USER)).get(IMSEnterprise.STATUS_SUCCESS)).intValue();
    		        	this.syn_unsucces=((Integer)((Hashtable)htb.get(IMSEnterprise.TYPE_USER)).get(IMSEnterprise.STATUS_UNSUCCESS)).intValue();
    		        	dblog.insDataIntegrationLog(con, this.syn_type, this.syn_start_time, this.syn_end_time, this.syn_success, this.syn_unsucces, this.syn_remark);
    		        }else if(this.type.equalsIgnoreCase(IMSEnterprise.TYPE_USER_COURSE)||this.type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE_ENROLLMENT)
    		        		||this.type.equalsIgnoreCase(IMSEnterprise.TYPE_RESULT)){
    		        	this.syn_success=((Integer)((Hashtable)htb.get(IMSEnterprise.TYPE_USER_COURSE)).get(IMSEnterprise.STATUS_SUCCESS)).intValue();
    		        	this.syn_unsucces=((Integer)((Hashtable)htb.get(IMSEnterprise.TYPE_USER_COURSE)).get(IMSEnterprise.STATUS_UNSUCCESS)).intValue();
    		        	dblog.insDataIntegrationLog(con, this.syn_type, this.syn_start_time, this.syn_end_time, this.syn_success, this.syn_unsucces, this.syn_remark);
    		        }else if (this.type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE)) {
    		        	this.syn_success=((Integer)((Hashtable)htb.get(IMSEnterprise.TYPE_USER_GROUP)).get(IMSEnterprise.STATUS_SUCCESS)).intValue();
    		        	this.syn_unsucces=((Integer)((Hashtable)htb.get(IMSEnterprise.TYPE_USER_GROUP)).get(IMSEnterprise.STATUS_UNSUCCESS)).intValue();
    		        	dblog.insDataIntegrationLog(con, this.syn_type, this.syn_start_time, this.syn_end_time, this.syn_success, this.syn_unsucces, this.syn_remark);
    		        }
                    if (cleanUp){
        		        imsEnterprise.cleanUp(con, cwIni, wbProfile, siteId);
    		        }
                }catch (Exception e){
                    IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "Error in import data.", e);
                }                    
            }
        }else{
            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "Invalid import type.");
        }
    }
    /**
    to export Learning Result to xmlFile
    pre-defined xmlFile
    */
    protected void exportXML(){
        if (checkExportType()){
            try{
                buildTreesFromDB();
                buildXML();
            }catch (Exception e){
                CommonLog.error(e.getMessage(),e);
                IMSUtils.writeLog(IMSUtils.FAILURE_FILE, e);   
            }
        }else{
            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "Invalid export type.");
        }
    }
    /**
    to check valid import type
    pre-defined type
    */
    private boolean checkImportType(){
        if (type!=null){
            if (type.equalsIgnoreCase(IMSEnterprise.TYPE_USER_GROUP) 
                || type.equalsIgnoreCase(IMSEnterprise.TYPE_USER_COURSE)
                || type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE_ENROLLMENT)
                || type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE) 
                || type.equalsIgnoreCase(IMSEnterprise.TYPE_RESULT))
            {   
                return true;
            }else{
                return false;    
            }
        }else{
            return false;    
        }
    }
    
    /**
    to check valid export type
    pre-defined type
    */
    private boolean checkExportType(){
        if (type!=null){
            if (type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE) 
                || type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE_RESULT) 
                || type.equalsIgnoreCase(IMSEnterprise.TYPE_RESULT) 
                || type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE_ENROLLMENT)
                || type.equalsIgnoreCase(IMSEnterprise.TYPE_USER_GROUP))
            {   
                return true;
            }else{
                return false;    
            }
        }else{
            return false;    
        }
    }
    
    // action rule only make sense on import application
    private boolean checkActionRule(){
        boolean bValid;
        if (actionRule==null){
            bValid = true;
        }else{
            if (type.equalsIgnoreCase(IMSEnterprise.TYPE_USER_COURSE) || type.equalsIgnoreCase(IMSEnterprise.TYPE_RESULT) || type.equalsIgnoreCase(IMSEnterprise.TYPE_COURSE_ENROLLMENT)){
                bValid = IMSApplication.validateActionRule(actionRule);
            }else{
                bValid = false;
            }
        }
        return bValid;
    }
    /**
    to send log file and integration result to the list of user
    pre-defined cwInifile, recipientList
    */
    protected void sendNotify(){
        try{
            if (imsEnterprise==null){
                imsEnterprise = new IMSEnterprise();
            }
        }catch (JAXBException e){
            System.err.println("Error in IMSEnterpriseApp.sendNotify: " + e.getMessage());
        }
        imsEnterprise.sendNotify(cwIni, action, recipientList, template);
    }
    
    /**
    for import , open XML and build a content tree 
    */
    private void buildTreesFromXML() throws Exception {
        JAXBContext jc = JAXBContext.newInstance(this.allSchemaContext, this.getClass().getClassLoader());
        Unmarshaller unmar = jc.createUnmarshaller();
        unmar.setValidating(true);
            
	    // Unmarshal the xml file to a imsEnterprise object
        _enterprise = (Enterprise)unmar.unmarshal(new File(xmlFilename));
    }

    /**
    for export. get data from db and put into the content tree in imsEnterprise object
    */
    private void buildTreesFromDB() throws cwException, IOException, SQLException, qdbException, JAXBException{
        loginProfile wbProfile = BatchUtils.getProf(con, siteId, cwIni, wizbini);       
        imsEnterprise = new IMSEnterprise();
        imsEnterprise.get(con, wbProfile, cwIni, startTime, endTime, type);
        if (imsEnterprise==null){
            System.err.println("imsEnterprise==null");
        }
        
    }

    /**
    Create output xml from the content trees
    */
    private void buildXML() throws IOException, FileNotFoundException, cwException {
    	    // Marshal the content trees to new XML documents
            
            OutputStream fOut = new FileOutputStream(xmlFilename);

	        try {
                JAXBContext jc = JAXBContext.newInstance(this.allSchemaContext, this.getClass().getClassLoader());
                Marshaller m = jc.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                m.marshal(imsEnterprise.getEnterprise(), fOut);
            } catch (JAXBException e) {
                CommonLog.error(e.getMessage(),e);
                throw new cwException("Error in reconstructing xml from unmarshalled object:" + e.getMessage());
	        } finally {
	            fOut.close();
	        }	
    }

    protected void moveXML(boolean bDeleteFile){
        try{
            File xmlFile = new File(xmlFilename);
            File destinFile = new File(IMSUtils.logDir, xmlFile.getName());
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(xmlFile));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destinFile));

            int inByte;
                
            while ((inByte = in.read()) != -1) {
                out.write(inByte);
            }
            in.close();
            out.flush();
            out.close();
            
            if (bDeleteFile){
                xmlFile.delete();
            }
        }catch(FileNotFoundException fnE){
            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "File not found in move xml to log directory:" + xmlFilename , fnE);
        }catch(IOException fnE){
            IMSUtils.writeLog(IMSUtils.FAILURE_FILE, "IOException in move xml to log directory.", fnE);
        }
    }
    protected boolean checkEmailCondition(){
    	boolean sendEmail = false;
    	String logTypeStr = this.cwIni.getValue("SEND_MAIL_CONDITION");
    	String []logType = cwUtils.splitToString(logTypeStr, "~");
    	
    	for(int i = 0; i < logType.length; i++){
    		if(logType[i] != null && logType[i].equalsIgnoreCase("SUCCESS")){
	    		File successLog = new File(IMSUtils.logDir + File.separator + IMSUtils.SUCCESS_FILE);
	            if (successLog.exists()){
	            	sendEmail = true;
	            	break;
	            }
    		} else if(logType[i] != null && logType[i].equalsIgnoreCase("FAILURE")){
	    		File failureLog = new File(IMSUtils.logDir + File.separator + IMSUtils.FAILURE_FILE);
	            if (failureLog.exists()){
	            	sendEmail = true;
	            	break;
	            }
    		} else if(logType[i] != null && logType[i].equalsIgnoreCase("ERROR")){
	    		File errorLog = new File(IMSUtils.logDir + File.separator + IMSUtils.ERROR_FILE);
	            if (errorLog.exists()){
	            	sendEmail = true;
	            	break;
	            }
    		} else if(logType[i] != null && logType[i].equalsIgnoreCase("CLEAN")){
	    		File cleanLog = new File(IMSUtils.logDir + File.separator + IMSUtils.CLEAN_FILE);
	            if (cleanLog.exists()){
	            	sendEmail = true;
	            	break;
	            }
    		}
    	}
    	return sendEmail;
    }
    
}
