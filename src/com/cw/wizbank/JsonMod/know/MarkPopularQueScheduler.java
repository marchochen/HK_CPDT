package com.cw.wizbank.JsonMod.know;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.ScheduledStatus;
import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.config.system.scheduledtask.TaskType;
import com.cw.wizbank.config.system.scheduledtask.impl.ParamTypeImpl;
import com.cw.wizbank.config.system.scheduledtask.impl.TaskTypeImpl;
import com.cw.wizbank.know.dao.KnowAnswerDAO;
import com.cw.wizbank.know.dao.KnowQuestionDAO;
import com.cw.wizbank.know.view.ViewKnowQueAnsDAO;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author DeanChen
 * 
 */
public class MarkPopularQueScheduler extends ScheduledTask implements Job{

    private int available_question_days;
    private int popular_question_count;
    private int available_vote_threshold;
    
    private Timestamp current_start_timestamp;
    private Timestamp next_start_timestamp;

    public MarkPopularQueScheduler(){
        logger = Logger.getLogger(MessageScheduler.class);
        TaskType taskype = new TaskTypeImpl();
        scheduledStatus = new ScheduledStatus(taskype, Calendar.getInstance());
    }

    public void init() {
        if (this.param != null) {
            for (int i = 0; i < this.param.size(); i++) {
                ParamType paramType = (ParamType) this.param.get(i);
                String val = null;
                if (paramType.getName().equals("available_question_days")) {
                    val = paramType.getValue();
                    this.available_question_days = Integer.parseInt(val);
                } else if (paramType.getName().equals("popular_question_count")) {
                    val = paramType.getValue();
                    this.popular_question_count = Integer.parseInt(val);
                } else if (paramType.getName().equals("available_vote_threshold")) {
                    val = paramType.getValue();
                    this.available_vote_threshold = Integer.parseInt(val);
                }
            }
        }
    }

    protected void process() {
        this.setPriority(Thread.MIN_PRIORITY);
        try {
            wizbini = WizbiniLoader.getInstance();
            dbSource = new cwSQL();
            dbSource.setParam(wizbini);
            con = dbSource.openDB(false);
            initTime(con);
            markPopular(con);
            con.commit();
        } catch (Exception e) {
            logger.debug("MarkPopularQueScheduler.process() error", e);
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
                    logger.debug("MarkPopularQueScheduler.process() error", e);
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
    }
    
    private void initTime(Connection con) throws SQLException {
    	current_start_timestamp = cwSQL.getTime(con);
    	next_start_timestamp = new Timestamp(current_start_timestamp.getTime() +  scheduledStatus.getPeriod());
    }

    private void markPopular(Connection con) throws SQLException {
        // to delete popular mark of all popular questions
        KnowQuestionDAO.delDelAllPopularMark(con);
        
        // to clear overdue temporary vote
        clearOverdueTempVote(con);
        
        // to mark popular question
        Vector queIdVec = ViewKnowQueAnsDAO.getFuturePopularQueIds(con,
                popular_question_count, available_vote_threshold);
        if (queIdVec.size() > 0) {
            String queIdStr = cwUtils.vector2list(queIdVec);
            KnowQuestionDAO.MarkPopularQue(con, queIdStr);
        }
        
        // clear temporary vote when one period end.
        clearLastPeriodTempVote(con);
    }
    
    /**
     * to clear overdue temporary vote. 
     * @param con
     * @throws SQLException
     */
    private void clearOverdueTempVote(Connection con) throws SQLException {
        Vector ansIdVec = ViewKnowQueAnsDAO.getQueIdsWithOverdueVote(con,this.wizbini.zdVoteDuration);
        if (ansIdVec != null && ansIdVec.size() > 0) {
            String ansIdsStr = cwUtils.vector2list(ansIdVec);
            KnowAnswerDAO.clearTempVote(con, ansIdsStr);
        }
    }

    /**
     * to clear temporary vote when one period end.
     * @param con
     * @throws SQLException
     */
    private void clearLastPeriodTempVote(Connection con) throws SQLException {
        Vector ansIdVec = ViewKnowQueAnsDAO.getQueIdsAtMarkPeriodEnd(con, current_start_timestamp, next_start_timestamp, wizbini.zdVoteDuration, available_question_days);
        if (ansIdVec != null && ansIdVec.size() > 0) {
            String ansIdsStr = cwUtils.vector2list(ansIdVec);
            KnowAnswerDAO.clearTempVote(con, ansIdsStr);
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