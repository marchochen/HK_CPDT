package com.cw.wizbank.enterprise;

import java.lang.Long;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;


import com.cw.wizbank.config.system.scheduledtask.impl.ParamTypeImpl;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.util.*;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.qdb.TempDirCleaner;
import com.cwn.wizbank.utils.CommonLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author DeanChen 
 * create at 2008-03-26
 */
public class IMSEnterpriseScheduler extends ScheduledTask implements Job{
	protected IMSEnterpriseApp app = null;
	private boolean imsInvalid = false; 
	private String err_msg = "";

	public IMSEnterpriseScheduler(){
		wizbini = WizbiniLoader.getInstance();
		logger = Logger.getLogger(MessageScheduler.class);
		dbSource = new cwSQL();
		dbSource.setParam(wizbini);
	}
	
	protected void init() {
		this.app = new IMSEnterpriseApp();
		try{
			if (this.param != null) {
				for (int i = 0; i < this.param.size(); i++) {
					ParamType paramType = (ParamType) this.param.get(i);
					if (paramType.getName().equalsIgnoreCase("init_file")) {
						app.iniFile = getFilePathOfCurrentDir(paramType.getValue());
						app.cwIni = new cwIniFile(app.iniFile); 
					}else if (paramType.getName().equalsIgnoreCase("action")) {
						app.action = paramType.getValue();
					}else if (paramType.getName().equalsIgnoreCase("type")) {
						app.type = paramType.getValue();
					}else if (paramType.getName().equalsIgnoreCase("file")) {
						app.xmlFilename = getFilePathOfCurrentDir(paramType.getValue());
					}else if (paramType.getName().equalsIgnoreCase("action_rule")) {
						app.actionRule = paramType.getValue();
					}else if (paramType.getName().equalsIgnoreCase("method")) {
						String actionMethod = paramType.getValue();
						if("eventdriven".equalsIgnoreCase(actionMethod)) {
							app.cleanUp = false;
						} 
					}else if (paramType.getName().equalsIgnoreCase("log_dir")) {
						app.rootLogDir = getFilePathOfCurrentDir(paramType.getValue());
					}else if (paramType.getName().equalsIgnoreCase("destination_log_dir")) {
						app.destinLogDir = getFilePathOfCurrentDir(paramType.getValue());
					}else if (paramType.getName().equalsIgnoreCase("start_date")) {
						try{
                            app.startTime = IMSUtils.constructTimestamp(paramType.getValue());                            
                        }catch(Exception e){
                        	imsInvalid = true;
                        	err_msg += "Start time incorrect foramt : YYYY-MM-DD\n";
                        	logger.debug("Start time incorrect foramt : YYYY-MM-DD");
                        }
					}else if (paramType.getName().equalsIgnoreCase("end_date")) {
						try{
                            app.endTime = IMSUtils.constructTimestamp(paramType.getValue());                            
                        }catch(Exception e){
                        	imsInvalid = true;
                        	err_msg += "End time incorrect foramt : YYYY-MM-DD\n";
                        	logger.debug("End time incorrect foramt : YYYY-MM-DD");
                        }
					}else{
						err_msg += "Invalid argument:" + paramType.getName() + "\n";
	                    throw new IllegalArgumentException("Invalid argument:" + paramType.getName());
	                }
				}
			} 
			app.appnRoot = wizbini.getAppnRoot();
			app.wizbini = wizbini;		
			if (app.action == null || app.iniFile == null ){
				err_msg += "Invalid argument: action or init_file.\n";
	            throw new IllegalArgumentException("Invalid argument: action or init_file.");
	        }
		}catch (Exception e){
			imsInvalid = true;
			CommonLog.error(e.getMessage());
        	logger.debug(e.getMessage());
        }
		try{
			con = dbSource.openDB(false);
			app.con = con;
			if(imsInvalid) {
				if (app.destinLogDir==null){
		            IMSUtils.getLogDir(app.rootLogDir, cwSQL.getTime(con));
		        }else{
		            IMSUtils.setLogDir(app.destinLogDir);
		        }
		        String sDebug = app.cwIni.getValue("DEBUG");
		        if (sDebug!=null){
		            IMSUtils.setDebug(Boolean.valueOf(sDebug).booleanValue());
		        }
		        return;
			} else {
				app.init();
			}
        }catch (Exception e){
        	imsInvalid = true;
        	CommonLog.error("ERROR IN INIT EnterpriseApp." + e.getMessage());
            logger.debug("ERROR IN INIT EnterpriseApp." + e.getMessage());
        }
		
//		 cleanup log dir
		if (app.rootLogDir != null) {
			String sExpireDuration = app.cwIni
					.getValue("LOG_FILE_TIME_TO_LIVE");
			if (sExpireDuration != null) {
				long expireDuration = Long.parseLong(app.cwIni
						.getValue("LOG_FILE_TIME_TO_LIVE"));
				if (expireDuration > 0) {
					TempDirCleaner cleaner = new TempDirCleaner();
					cleaner.addTargetDir(app.rootLogDir,
							expireDuration * 24 * 60 * 60);
					cleaner.cleanTempDir();
				}
			}
		}
		 //set the path of email template file
		app.template = app.cwIni.getParentFile()+ cwUtils.SLASH + app.cwIni.getValue("MAIL_NOTIFY_INTEGRATION_TPL");
	}

	protected void process() {
		try {
			if(!imsInvalid) {
				if (app.action.equals(IMSEnterprise.ACTION_IMPORT)) {
					app.importXML();
				} else if (app.action.equals(IMSEnterprise.ACTION_EXPORT)) {
					app.exportSource = app.cwIni.getValue("DATA_SOURCE");
					app.exportXML();
				} else {
					throw new IllegalArgumentException("invalid action.");
				}
				con.commit();
			}
		}
		catch (Exception e) {
			imsInvalid = true;
			logger.debug("error in IMSEnterpriseScheduler.process()");
			logger.debug(e);
            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
		}
		finally {
			if (app.action.equals(IMSEnterprise.ACTION_IMPORT)) {
				app.moveXML(true);
			} else if (app.action.equals(IMSEnterprise.ACTION_EXPORT)) {
				app.moveXML(false);
			}
			if (app.checkEmailCondition()) {
				if (app.recipientList != null && app.recipientList.length > 0) {
					app.sendNotify();
				}
			}
			if(imsInvalid) {
				writeErrLog();
			}
			try {
				if (con != null) {
					con.close();
				}
			}
			catch (Exception e) {
				logger.debug("error in IMSEnterpriseScheduler.process()");
				logger.debug(e);
			}
		}
	}
	
	protected String getFilePathOfCurrentDir(String filePath) {
		filePath = this.wizbini.getAppnRoot() + cwUtils.SLASH + WizbiniLoader.dirPathCfg + cwUtils.SLASH + WizbiniLoader.dirPathSys + cwUtils.SLASH + filePath;
		return filePath;
	}
	
	private void writeErrLog() {
        if(err_msg != null && !"".equals(err_msg)) {
        	err_msg = err_msg.substring(0, err_msg.lastIndexOf("\n"));
        	IMSUtils.writeLog(IMSUtils.FAILURE_FILE, err_msg);
        }
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		Map<String, Object> params = jobExecutionContext.getMergedJobDataMap();
		if(param == null){
			param = new ArrayList();
		}
		for(String key : params.keySet()){
			ParamType paramType = new ParamTypeImpl();
			paramType.setName(key);
			paramType.setValue(params.get(key).toString());
			param.add(paramType);
		}
		init();
		process();
	}
}
