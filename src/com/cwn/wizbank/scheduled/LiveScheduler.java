package com.cwn.wizbank.scheduled;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.entity.LiveItem;
import com.cwn.wizbank.services.LiveItemService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.web.WzbApplicationContext;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LiveScheduler extends ScheduledTask implements Job{

	protected static List<LiveItem> liveItemList = null;
	
	static LiveItemService  liveItemService = (LiveItemService) WzbApplicationContext.getBean("liveItemService");

	public LiveScheduler(){
		logger = Logger.getLogger(MessageScheduler.class);
	}

	public void init() {
		
	}

	protected void process() {
		this.setPriority(Thread.MIN_PRIORITY);
		try {
			//检查活动是否结束
			updateLiveStatus();
			//检查修改当前时间已大于结束时间的直播活动
			liveItemService.checkLiveTimeout();
			//展示互动获取回放信息
			liveItemService.getRecordInfo();
		} catch (Exception e) {
			logger.debug("LiveScheduler.process() error" + e);
			CommonLog.error(e.getMessage(),e);
            try {
                if (con != null && !con.isClosed()) {
                    con.rollback();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
		} finally {
			if (this.con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.debug("LiveScheduler.process() error" + e);
                    CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}      
        
    public void updateLiveStatus(){
    	if(liveItemList != null && liveItemList.size() > 0){
            try {
            	boolean flag = false;
            	for (LiveItem liveItem : liveItemList) {
            		flag = liveItemService.setLiveState(liveItem);
            		if(flag){
            			liveItemList.remove(liveItem);
            		}
				}
            } catch (Exception e) {
                logger.debug("too much error occoured when calling send_msg, stopped for next run");
            }
    	}
    	
    }
    
    public static void addLiveItemList(LiveItem liveItem){
    	if(liveItemList == null){
    		liveItemList = new ArrayList<LiveItem>();
    	}
    	liveItemList.add(liveItem);
    }

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		init();
		process();
	}
}