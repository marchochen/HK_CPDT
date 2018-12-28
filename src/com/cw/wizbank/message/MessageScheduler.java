package com.cw.wizbank.message;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

import com.cw.wizbank.Application;
import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.config.system.scheduledtask.impl.ParamTypeImpl;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.WebMessage;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MessageScheduler extends ScheduledTask implements Job{

	protected static Vector<Long> messageQueue = null;

	public static String site_domain = "";

	private static boolean flag = false;

	private int attempt = 3;

	private int error_count = 0;

	public MessageScheduler(){
		logger = Logger.getLogger(MessageScheduler.class);
		static_env = qdbAction.static_env;
	}

	public void init() {
		if (this.param != null) {
			for (int i = 0; i < this.param.size(); i++) {
				ParamType paramType = (ParamType) this.param.get(i);
				if (paramType.getName().equals("retry_attempt")) {
					this.attempt = Integer.valueOf(paramType.getValue()).intValue();
				}
				if (paramType.getName().equals("site_domain")) {
					site_domain = Application.MAIL_SCHEDULER_DOMAIN;
				}
				if (paramType.getName().equals("notes_log_file")) {
					this.static_env.MAIL_NOTES_LOG = paramType.getValue();
				}
			}
		}
		messageQueue = new Vector<Long>();
	}

	protected void process() {
		this.setPriority(Thread.MIN_PRIORITY);
		try {
			wizbini = WizbiniLoader.getInstance();
			dbSource = new cwSQL();
			dbSource.setParam(wizbini);
			con = dbSource.openDB(false);
			callSendMessage();
			con.commit();
		} catch (Exception e) {
			logger.error("MessageScheduler.process() error" + e, e);
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
        
    //Send Message by Calling URLs
    public void callSendMessage() 
        throws SQLException {
            
            flag = true;        //  sending message
            Hashtable<Long, Long> htXtpId = new Hashtable<Long, Long>();
            Timestamp curTime = this.dbSource.getTime(this.con);
            
            String MessageScheduler_GET_MESSAGE_ID  = "SELECT emsg_id, emsg_mtp_id FROM emailMessage, emailMsgRecHistory " 
            										+ " WHERE emsg_id = emrh_emsg_id "
            										+ " AND emrh_status = ? "
            										+ " AND emsg_target_datetime < ? "
            										+ " AND ( emrh_attempted < ?) "
            										+ " ORDER BY emsg_id ASC";
            PreparedStatement stmt = con.prepareStatement(MessageScheduler_GET_MESSAGE_ID);

            stmt.setString(1, "N");
            stmt.setTimestamp(2, curTime);
            stmt.setLong(3, attempt);
            ResultSet rs = stmt.executeQuery();

            while( rs.next() ){
                messageQueue.addElement(new Long(rs.getLong("emsg_id")));
                htXtpId.put(new Long(rs.getLong("emsg_id")), new Long(rs.getLong("emsg_mtp_id")));
            }
            stmt.close();
            
            while( !messageQueue.isEmpty() && error_count < 10) {

                StringBuffer args = new StringBuffer();
                Long curMsgId = (Long)messageQueue.elementAt(0);
                try {
                    args.append("&msg_id=").append(curMsgId);
                    messageQueue.removeElementAt(0);
                    
                    long attempted = MessageService.sendMessage(con, curMsgId, static_env);
                    
                    //推送消息到微信和app客户端
                    if(attempted == 1){//为了避免多次推送消息
                    	pushMsgToAppAndWechat(con,curMsgId);
                    }
                    
                } catch (Exception e) {
                     if (e instanceof UnknownHostException || 
                             e instanceof MalformedURLException ||
                             e instanceof ConnectException) {
                         error_count ++;
                     }
                    Long xtpId = (Long)htXtpId.get(curMsgId);
                    logger.debug("Thread Error: " + ((xtpId==null) ? 0 : xtpId.longValue()), e);
                    logger.debug("too much error occoured when calling send_msg, stopped for next run");
                }
                
            }
            
            flag = false;
            error_count = 0;
            return;
        }
    
    private void pushMsgToAppAndWechat(Connection con,long emsgId) throws SQLException {
    	
    	List<WebMessage> msgList = MessageService.getWebMessageListByEmsgId(con,emsgId);
    	
    	for(WebMessage wm : msgList){
    		List<String> msg = new ArrayList<String>();
    		msg.add(wm.getWmsg_send_ent_id()+"");
    		msg.add(wm.getWmsg_rec_ent_id()+"");
    		msg.add(wm.getWmsg_id()+"");
    		msg.add(wm.getWmsg_subject());
    		msg.add(wm.getWmsg_content_mobile());
    		MessageService.pushMsgToAppOrWechat(msg);
    	}
	}

	public void addMessageIdToQueue(long msg_id){
        
        if(flag)
            messageQueue.addElement(new Long(msg_id));
        else {
        	logger.debug("interrupt the thread");
            interrupt();
        }
        return;
        
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