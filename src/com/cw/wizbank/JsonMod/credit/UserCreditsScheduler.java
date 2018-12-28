package com.cw.wizbank.JsonMod.credit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.JsonMod.user.User;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.system.scheduledtask.ParamType;
import com.cw.wizbank.config.system.scheduledtask.impl.ParamTypeImpl;
import com.cw.wizbank.credit.dao.UserCreditsDAO;
import com.cw.wizbank.credit.db.UserCreditsDB;
import com.cw.wizbank.credit.view.ViewCreditsDAO;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author DeanChen
 * 
 */
public class UserCreditsScheduler extends ScheduledTask implements Job{

    public UserCreditsScheduler(){
        logger = Logger.getLogger(MessageScheduler.class);
    }

    protected void init() {
        ;
    }

    protected void process() {
        this.setPriority(Thread.MIN_PRIORITY);
        try {
            wizbini = WizbiniLoader.getInstance();
            dbSource = new cwSQL();
            dbSource.setParam(wizbini);
            con = dbSource.openDB(false);
            updUserCredits(con);
            con.commit();
            qdbAction.creditRankMap = ViewCreditsDAO.getTopCreditRank(con, User.HOME_GETDAT_CREDIT_RANK_RECORD_NUM);
            con.commit();
        } catch (Exception e) {
            logger.debug("UserCreditsScheduler.process() error", e);
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
                    logger.debug("UserCreditsScheduler.process() error", e);
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
    }

    /**
     * to synchronize the credits of all user. 
     * @param con
     * @throws SQLException
     */
    private void updUserCredits(Connection con) throws SQLException {
		Vector creditsVec = ViewCreditsDAO.getUserCreditsList(con);
		for (Iterator iter = (Iterator) creditsVec.iterator(); iter.hasNext();) {
			UserCreditsDB creditsDb = (UserCreditsDB) iter.next();
			boolean isExist = UserCreditsDAO.isExistUct(con, creditsDb.getUct_ent_id());
			UserCreditsDAO creditsDao = new UserCreditsDAO();
			if (isExist) {
				creditsDao.upd(con, creditsDb);
			} else {
				creditsDao.ins(con, creditsDb);
			}
		}
	}

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        init();
        process();
    }
}
