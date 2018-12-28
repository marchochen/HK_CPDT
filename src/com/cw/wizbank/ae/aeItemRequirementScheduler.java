package com.cw.wizbank.ae;

import java.sql.*;

import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

public class aeItemRequirementScheduler extends Thread {
    
    private int refresh_period           =   30;
    private Connection con               =   null;
    private static cwSQL dbSource        =   null;

    public void setRefreshPeriod(int inRefreshPeriod) {
        this.refresh_period = inRefreshPeriod;
    }
    
    // Constructor of the thread
    public aeItemRequirementScheduler(cwSQL inCwSQL)
        throws cwException {
        
        this.dbSource = inCwSQL;
        return;
    }
        

    // Start the thread
    public void run() {
    	CommonLog.info("Item Requirement Scheduler starts.");
        try{
            // wait for a period of time in case other parts of servlet are not started yet
            sleep(1000 * 30);
        }catch( InterruptedException e ) {
        	CommonLog.error("Thread Interrupted : " + e.getMessage(),e);
            this.stop();
        }

        while(true) {
            try{
                con = dbSource.openDB(false);
                aeItemRequirement.doAction(con, null);
                con.commit();
                if(con!=null && !con.isClosed())                    
                con.close();                
                sleep(1000 * refresh_period * 60);

            }catch( InterruptedException ie ) {
                CommonLog.error("aeItemRequirementscheduler Interrupted : " + ie.getMessage(),ie);
            }catch( SQLException sqle ) {
                try{
                    sleep(1000 * 3 * 60);
                    if(this.con != null && !this.con.isClosed()) {
                        con.close();
                    }
                }catch( InterruptedException iie ) {
                	CommonLog.error("aeItemRequirementscheduler Interrupted : " + iie.getMessage(),iie);
                }catch( SQLException sqlee){                    
                    if(this.con != null) {
                        this.con = null;
                    }
                }
            }catch( Exception e ) {
            	CommonLog.error("aeItemRequirementscheduler Error : " + e,e);
            }
            
        }
    }        
}