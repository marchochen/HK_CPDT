package com.cw.wizbank.ae;

import java.sql.*;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

public class aeItemTreeNodePathScheduler extends Thread {
    
    private Connection con               =   null;
    private int CONNECTION_RETRY_TIME    =   60 * 1000;
    private static cwSQL dbSource        =   null;

    // Constructor of the thread
    public aeItemTreeNodePathScheduler(cwSQL inCwSQL)
        throws cwException {
        
        this.dbSource = inCwSQL;
        return;
    }
        
    // Start the thread
    public void run() {
    	CommonLog.info("Item Tree node path Scheduler starts.");
        try{
            // wait for a period of time in case other parts of servlet are not started yet
            sleep(10 * 1000);
        }catch( InterruptedException e ) {
        	CommonLog.error("Thread Interrupted : " + e.getMessage());
            this.stop();
        }

        while(true) {
            try{
                con = dbSource.openDB(false);
                Timestamp startTime = new Timestamp(System.currentTimeMillis());
                aeItemTreeNodePath treeNodePath = new aeItemTreeNodePath();
                treeNodePath.delAllPath(con);
                treeNodePath.updateAllPath(con);
                con.commit();
                long sleepTime = timeToNextRun(con);
                
                if(con!=null && !con.isClosed())
                    con.close();
                //daily
                CommonLog.info("aeItemTreeNodePathScheduler Scheduler running at " + startTime + " and used " + (System.currentTimeMillis() - startTime.getTime()) + " millseconds.");
                sleep(sleepTime);

            }catch( InterruptedException ie ) {
            	CommonLog.error("aeItemTreeNodePathScheduler Interrupted : " + ie.getMessage());
            }catch( Exception e ) {
            	CommonLog.error("aeItemTreeNodePathScheduler Error : " + e);
                try {
                    if(con!=null && !con.isClosed())
                        con.close();
                    sleep(CONNECTION_RETRY_TIME);
                }catch( InterruptedException ie2) {
                	CommonLog.error("Interrupted Exception: " + ie2.getMessage());
                }catch( Exception e2 ) {
                	CommonLog.error("Exception: " + e2.getMessage());
                }
            }
        }
    }
    
    public long timeToNextRun(Connection con)
        throws SQLException, cwException
    {
        Timestamp curTime = this.dbSource.getTime(con);
        int year = curTime.getYear();
        int month = curTime.getMonth();
        int date = curTime.getDate();
        Timestamp today = new Timestamp(year, month, date, 0, 0, 0, 0);
        Timestamp tomorrow = new Timestamp(today.getTime() + (24 * 60 * 60 * 1000));
        long timediff = tomorrow.getTime() - curTime.getTime();
        return timediff;
    }
}