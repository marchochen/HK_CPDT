package com.cw.wizbank.qdb;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

public class SessionListener implements HttpSessionBindingListener {

    public String remoteHost = "";
    public loginProfile loginprof;
    private WizbiniLoader wizbini = null;
    private Connection con = null;
    public String sess_id = null;
    public String login_type = null;
    
    //initialize
    public SessionListener(WizbiniLoader wizbini, loginProfile prof, String info, String sess_id,String login_type) throws cwException {
    	
    	this.remoteHost = info;
        this.loginprof = new loginProfile();
        this.wizbini = wizbini;
        this.sess_id = sess_id;
        if(prof!=null){
            this.loginprof = prof;
            this.login_type = login_type;
        }else{
            throw new cwException("The loginProfile is null!");
        }
       
    }
    
    private void getCon(){
    	try {
            if(this.con==null || this.con.isClosed()) {
                cwSQL sqlCon = new cwSQL();
                sqlCon.setParam(this.wizbini);
                this.con = sqlCon.openDB(false);
            }
        } catch (Exception e) {
        	CommonLog.error("<b><h3> Sorry, the server is too busy.</h3></b>");
        }
        return;
    }
    private void closeCon() {
        try {
            if(this.con!=null && !this.con.isClosed()) {
                this.con.close();
            }
        } catch (SQLException sqle) {
        	CommonLog.error(sqle.getMessage(),sqle);
        }
    }
    
	public void valueBound(HttpSessionBindingEvent arg0) {
		try{
			getCon();
			CurrentActiveUser.addCAU(con, loginprof.usr_ent_id, sess_id,loginprof.my_top_tc_id,login_type);
			con.commit();
		}catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException re) {
            	CommonLog.error("SQL rollback error: " + re.getMessage());
            }
		} finally {
		    closeCon();
		}
    }
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		try{
			getCon();
			CurrentActiveUser.subCAU(con, sess_id);
			con.commit();
		}catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException re) {
            	CommonLog.error("SQL rollback error: " + re.getMessage());
            }
		} finally {
		    closeCon();
		}
		
	}
}
