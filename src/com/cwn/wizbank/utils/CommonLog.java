package com.cwn.wizbank.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//公用记录logger
public class CommonLog {

	private static Logger logger  = LoggerFactory.getLogger(CommonLog.class);
	
	public CommonLog (){
       
	};
	
	public static final void debug(String msg){
		logger.debug(msg);
	}
	
	public static final void debug(String msg , Throwable t){
		logger.debug(msg, t);
	}
	
	public static final void info(String msg){
		logger.info(msg);
	}
	
	public static final void info(String msg , Throwable t){
		logger.info(msg, t);
	}
	
	public static final void error(String msg){
		System.out.println(msg);
		logger.error(msg);
	}
	
	public static final void error(String msg , Throwable t){
		t.printStackTrace();
		logger.error(msg,t);
	}
}
