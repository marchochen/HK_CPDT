package com.cw.wizbank.km;

import java.sql.*;
import java.util.*;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.db.DbKmNodeSubscription;
import com.cw.wizbank.db.view.ViewKmNodeManager;
import com.cw.wizbank.db.view.ViewKmNodeActn;
import com.cwn.wizbank.utils.CommonLog;

public class KMScheduler extends Thread {
    
    private Connection con               =   null;
    private static cwSQL dbSource        =   null;
    
    private static long KM_REFRESH_TIME                 = 30*1000;   // 30 seconds
    private static long DAILY_MAIL_SEND_TIME            = 15*60*1000;   // 15 minutes
    private static int  WEEKLY_MAIL_SEND_TIME           = Calendar.THURSDAY; // Send the wekk mail every thursday
    private static int TODAY                            = 0;
    private static boolean TODAY_EXECUTED               = false;

    private static Vector actionVec                    = null;
    
    public void setRefreshTime(int inRefreshTime) {
        this.KM_REFRESH_TIME = inRefreshTime * 1000;
    }
    public void setDailyMailSendTime(int inDailyMailSendTime) {
        this.DAILY_MAIL_SEND_TIME = inDailyMailSendTime * 60 * 1000;
    }
    public void setWeeklyMailSendTime(int inWeeklyMailSendTime) {
        this.WEEKLY_MAIL_SEND_TIME = inWeeklyMailSendTime;
    }

    // Constructor of the thread
    public KMScheduler(cwSQL inCwSQL)
        throws cwException {
        this.actionVec = new Vector();
        this.dbSource = inCwSQL;
    }

    // Start the thread
    public void run() {
    	CommonLog.info("KM Subscription Notify Scheduler starts.");
        //System.out.println("KM Subscription Notify Scheduler starts.");
        try{
            // wait for a period of time in case other parts of servlet are not started yet
            sleep(this.KM_REFRESH_TIME);
        }catch( InterruptedException e ) {
        	CommonLog.error("Thread Interrupted : " + e.getMessage());
            this.stop();
        }

        while(true) {
            try{
            	CommonLog.info("KM Scheduler running...");
                con = dbSource.openDB(false);
                Timestamp curTime = dbSource.getTime(con);

                if (!actionVec.isEmpty()) {
                    notifyImmediateSub(con, curTime);
                }
                if (curTime.getDay()+1 != TODAY) {
                    TODAY = curTime.getDay()+1;
                    TODAY_EXECUTED = false;
                }

                if (!TODAY_EXECUTED && (timeFromToday(curTime) > DAILY_MAIL_SEND_TIME)) {
                	CommonLog.info("Sending day and week mail...");                    
                    notifyDayAndWeekSub(con, curTime);
                    TODAY_EXECUTED = true;
                }

                con.commit();
                if(con!=null && !con.isClosed())
                    con.close();
                sleep(KM_REFRESH_TIME);
                
            }catch( InterruptedException ie ) {
            	CommonLog.error("KMScheduler Interrupted : " + ie.getMessage());
            }catch( SQLException sqle ) {
            	CommonLog.error("SQLException : " + sqle.getMessage());
                if(this.con != null)
                    this.con = null;
            }catch( Exception e ) {
            	CommonLog.error("KMScheduler Error : " + e);
            } finally {
                try {
                    if (con != null &&!con.isClosed()) {
                        con.rollback();
                        con.close();
                    }
                } catch (Exception e) {

                }
            }
            
        }
    }

    /**
    Add new action to the stack, for immediate notification
    */
    public static  void addAction(ViewKmNodeActn action) {
    	CommonLog.debug("Add event to queue....");
        if (actionVec != null) {
            actionVec.addElement(action);
            CommonLog.debug("Done.");
        }else {
        	CommonLog.debug("KMScheduler is not enabled.");
        }
    }

    /**
    Add new action to the stack, for immediate notification
    */
    private static  ViewKmNodeActn popAction() {

        ViewKmNodeActn action = null;
        if (actionVec != null && !actionVec.isEmpty()) {
            action = (ViewKmNodeActn) actionVec.elementAt(0);
            actionVec.removeElementAt(0);
        }else {
        	CommonLog.debug("KMScheduler is not enabled.");
        }
        return action;
    }


    private static void notifyImmediateSub(Connection con, Timestamp curTime) throws cwSysMessage, cwException, SQLException {
        while (!actionVec.isEmpty()) {
            ViewKmNodeActn action = popAction();
            
            Vector immEntIdVec = ViewKmNodeManager.getUsersToNotify(con, DbKmNodeSubscription.EMAIL_SEND_TYPE_IMMD, curTime, action.node_id);
            KMNotify.insNotify(con, immEntIdVec, DbKmNodeSubscription.EMAIL_SEND_TYPE_IMMD, curTime, action.action_id);
        }
    }

    /**
    Send the daily email and weekly email
    */
    private static void notifyDayAndWeekSub(Connection con, Timestamp curTime) throws cwSysMessage, cwException, SQLException {

        if (TODAY == WEEKLY_MAIL_SEND_TIME) {
        	CommonLog.info("Sending week ...");
            Vector weekEntIdVec = ViewKmNodeManager.getUsersToNotify(con, DbKmNodeSubscription.EMAIL_SEND_TYPE_WEEK, curTime,0);
            KMNotify.insNotify(con, weekEntIdVec, DbKmNodeSubscription.EMAIL_SEND_TYPE_WEEK, curTime, 0);
        }

        CommonLog.info("Sending day...");
        Vector dayEntIdVec = ViewKmNodeManager.getUsersToNotify(con, DbKmNodeSubscription.EMAIL_SEND_TYPE_DAY, curTime,0);
        KMNotify.insNotify(con, dayEntIdVec, DbKmNodeSubscription.EMAIL_SEND_TYPE_DAY, curTime, 0);

    }

    /**
    Get the no. of milliseconds from today's 00:00am to current time
    */
    private static long timeFromToday(Timestamp curTime) {
        Timestamp today_start = new Timestamp(curTime.getYear(), curTime.getMonth(), curTime.getDate(), 0, 0, 0, 0);
        
        long timediff = curTime.getTime() - today_start.getTime();
        CommonLog.info("timediff = " + timediff);        
        return timediff;
    }
}