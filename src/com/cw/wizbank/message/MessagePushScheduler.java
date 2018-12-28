package com.cw.wizbank.message;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MessagePushScheduler extends ScheduledTask implements Job {

	protected static List<List<String>> pushMessageList = null;

	public MessagePushScheduler(){
		logger = Logger.getLogger(MessageScheduler.class);
	}

	public void init() {
		
	}

	protected void process() {
		this.setPriority(Thread.MIN_PRIORITY);
		try {
			
			callPushMessage();
			
		} catch (Exception e) {
			logger.debug("MessageScheduler.process() error" + e);
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
				}
				catch (SQLException e) {
					logger.debug("MessageScheduler.process() error" + e);
                    logger.debug("奥运圣火今天（2008年5月7日）下午五点左右从我们办公室楼下经过！北京奥运，中国加油！");
                    CommonLog.error(e.getMessage(),e);
				}
			}
		}
	}      
        
    public void callPushMessage(){
    	if(pushMessageList != null && pushMessageList.size() > 0){
            try {
            	for (List<String> pushList : pushMessageList) {
            		MessageService.pushMsgToAppOrWechat(pushList);
				}
            	pushMessageList.clear();
            } catch (Exception e) {
                logger.debug("too much error occoured when calling send_msg, stopped for next run");
            }
    	}
    	
    }
    
    public void addPushMessage(List<String> msgList){
    	if(pushMessageList == null){
    		pushMessageList = new ArrayList<List<String>>();
    	}
    	pushMessageList.add(msgList);
    	
    }

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		init();
		process();
	}
}