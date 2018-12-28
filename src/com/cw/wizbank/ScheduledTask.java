package com.cw.wizbank;

import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;
import com.sun.management.OperatingSystemMXBean;

public abstract class ScheduledTask extends Thread {

	public final static String Repeat_Type_Minute = "minute";

	public final static String Repeat_Type_Hour = "hour";

	protected Logger logger = null;

	protected cwSQL dbSource = null;

	protected Connection con = null;

	public qdbEnv static_env = null;

	public WizbiniLoader wizbini = null;

	protected List param = null;

	protected ScheduledStatus scheduledStatus = null;
	
	public static int gcCount = 0 ;

	public ScheduledTask() {
	}

	public void run() {
	    try {
	    	
	    	/*2017-03-20 代码屏蔽 By Kenry.Xian
	    	该GC代码逻辑不可控，在系统繁忙时执行GC会对服务造成大量压力,容易造成系统缓慢

	    	gcCount++ ;
	    	if(gcCount==60){
	    		
	    		CommonLog.info(">>>>>>>>>>>>>>>  beg gc <<<<<<<<<<<<<<<<<");
	    		gcCount = 0 ;
	    		System.gc() ;
	    		systemView() ;

	    		CommonLog.info(">>>>>>>>>>>>>>>  end gc <<<<<<<<<<<<<<<<<");
	    	}*/
    		logger.debug(scheduledStatus.getClassName() + " run start.");
    		init();
    		scheduledStatus.setStartTime(Calendar.getInstance());
    		scheduledStatus.setFinishStatus(false);
    		process();
    		scheduledStatus.setEndTime(Calendar.getInstance());
    		scheduledStatus.setFinishStatus(true);
    		scheduledStatus.addRunCount();
    		logger.debug(scheduledStatus.getClassName() + " run finish.");
    	} finally {
            try {
                if (con != null && !con.isClosed()) {
                	con.commit();
                	con.close();
                }
            } catch (SQLException sqle) {
                CommonLog.error(sqle.getMessage(),sqle);
            }
        }
	}

	protected abstract void init();

	protected abstract void process();

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setDbSource(cwSQL dbSource) {
		this.dbSource = dbSource;
	}

	public void setCon(Connection con) {
		this.con = con;
	}

	public void setStatic_env(qdbEnv static_env) {
		this.static_env = static_env;
	}

	public void setWizbini(WizbiniLoader wizbini) {
		this.wizbini = wizbini;
	}

	public void setScheduledStatus(ScheduledStatus scheduledStatus) {
		this.scheduledStatus = scheduledStatus;
	}

	public void setParam(List param) {
		this.param = param;
	}

	
	public void systemView(){
		//free和use和total均为KB   
		long free=0;   
		long use=0;   
		long total=0;   
		int kb=1024;   
		Runtime rt=Runtime.getRuntime();   
		total=rt.totalMemory();   
		free=rt.freeMemory();   
		use=total-free;   
	//	System.out.println("系统内存已用的空间为："+use/kb+" MB");   
	//	System.out.println("系统内存的空闲空间为："+free/kb+" MB");   
	//	System.out.println("系统总内存空间为："+total/kb+" MB");   
		OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();   
		long physicalFree=osmxb.getFreePhysicalMemorySize()/kb;   
		long physicalTotal=osmxb.getTotalPhysicalMemorySize()/kb;   
		long physicalUse=physicalTotal-physicalFree;   
	//	String os=System.getProperty("os.name");   
	//	System.out.println("操作系统的版本："+os);   
	//	System.out.println("系统物理内存已用的空间为："+physicalFree+" MB");   
	//	System.out.println("系统物理内存的空闲空间为："+physicalUse+" MB");   
	//	System.out.println("总物理内存："+physicalTotal+" MB");         
		    // 获得线程总数     
		ThreadGroup parentThread;        
		for (parentThread = Thread.currentThread().getThreadGroup(); parentThread.getParent() != null; parentThread = parentThread.getParent()){
			int totalThread = parentThread.activeCount();        
		//	System.out.println("获得线程总数:"+totalThread);
		}
	}
	
}
