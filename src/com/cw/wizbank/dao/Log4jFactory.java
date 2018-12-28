package com.cw.wizbank.dao;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.qdb.dbUtils;

public class Log4jFactory {
	public static Logger createLogger(Class className){
		Logger logger = null;
		try {
			/*
			String logdir = Dispatcher.getWizbini().getSystemLogDirAbs() + dbUtils.SLASH;
			File dir = new File(logdir);
			if (!dir.exists()) {
				dir.mkdir();
			}
			System.setProperty("log_path", logdir);
			*/
			System.setProperty("file_encoding", Dispatcher.getWizbini().cfgSysSetupadv.getEncoding());
			logger = Logger.getLogger(className.getName() + ".log");
			PropertyConfigurator.configure(Dispatcher.getWizbini().getCfgFileLog4jDir());
		} catch (RuntimeException e) {
			logger = Logger.getLogger(className);
		}
		return logger;
	}
	
}
