/*
 * Created on May 4, 2004
 *
 */
package com.cw.wizbank.upload;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;

import com.cw.wizbank.accesscontrol.acSite;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dataMigrate.imp.Imp;
import com.cw.wizbank.dataMigrate.imp.ImpUser;
import com.cw.wizbank.db.DbIMSLog;
import com.cw.wizbank.db.DbMgMessage;
import com.cw.wizbank.enterprise.IMSEnterprise;
import com.cw.wizbank.enterprise.IMSLog;
import com.cw.wizbank.message.Message;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author kelvin
 *
 */
public class UploadUserThread extends Thread {
	private String admin_usr_id = "s1u3";
	private long admin_usr_ent_id = 3;

//	private Connection con;
	private WizbiniLoader wizbini;
	private HttpSession sess;
	private qdbEnv static_env;
	private loginProfile prof;
	private File srcFile;
	private File tmpFile;
	private Vector v_used_col;
	
	public UploadUserThread(WizbiniLoader wizbini,
							HttpSession sess, qdbEnv static_env, loginProfile prof,
								File srcFile, File tmpFile, Vector v_used_col, Connection con)
									throws cwException, SQLException {

		this.wizbini = wizbini;
		this.sess = sess;
		this.static_env = static_env;
		this.prof = prof;
		this.srcFile = srcFile;
		this.tmpFile = tmpFile;
		this.v_used_col = v_used_col;
		
		this.setPriority(Thread.MIN_PRIORITY);
		
		acSite ste = new acSite();
		ste.ste_ent_id = prof.root_ent_id;
		this.admin_usr_ent_id = ste.getSiteSysEntId(con);
		this.admin_usr_id = "s"+prof.root_ent_id+"u"+this.admin_usr_ent_id;
		
		CommonLog.debug("@"+this.admin_usr_id+"@");
	}
	
	public StringBuffer getXML() {
		StringBuffer xml = new StringBuffer(64);
		xml.append("<data_import src_file=\"").append(srcFile.getName()).append("\">");
		xml.append("</data_import>");
		return xml;
	}
	
	public void run() {
	    
	   Connection con = null;
	   try {
	    // Get a database connection for this thread object
       try {
            cwSQL sqlCon = new cwSQL();
            sqlCon.setParam(wizbini);
            con = sqlCon.openDB(false);
        } catch (Exception e) {
            CommonLog.error("UploadUserThread failed to get databaes connection");
            return;
        }
		///
		long startTime = System.currentTimeMillis();
		CommonLog.info("# UploadUserThread started. Processing " + tmpFile + " ...");
		///

		DbIMSLog dbIlg = (DbIMSLog)sess.getAttribute(UploadModule.SESS_UPLOAD_LOG_RECORD);
		dbIlg.ilg_type = IMSLog.IMSLOG_TYPE_USER;
		dbIlg.ilg_process = IMSLog.IMSLOG_PROCESS_IMPORT;
		IMSLog ilg = new IMSLog();
		try {
			ilg.ins(con, prof, dbIlg);
//			ilg.saveUploadedFile(static_env, dbIlg.ilg_id, (File)sess.getAttribute(UploadModule.SESS_UPLOADED_SRC_FILE));
		} catch (SQLException e) {
			CommonLog.error(e.getMessage(),e);
			return;
		} 
		
		UploadUser uploadUser = null;

		try {
			uploadUser = new UploadUser(con, static_env.ENCODING, static_env.INI_PATH, prof, static_env.DEFAULT_GRADE_ID, static_env.UPLOAD_USER_PWD_ENABLE, !dbIlg.ilg_dup_data_update_ind);
			String xml = uploadUser.cookUser(tmpFile.getAbsolutePath(), static_env, dbIlg.ilg_id, v_used_col, wizbini, prof.usr_id);
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			return;
		} 
        con.commit();

		///
		long endTime = System.currentTimeMillis();
		CommonLog.info("# Import initiator = " + prof.usr_ent_id + ":" + prof.usr_id + ":" + prof.usr_ste_usr_id);
		CommonLog.info("# Total cook time = " + (endTime - startTime));
		CommonLog.info("# UploadUserThread ended. " + tmpFile + " imported.");
		///
		
		CommonLog.info("# Creating notification email ... ");
		
		Vector paramsName  = new Vector();
		Vector paramsType  = new Vector();
		Vector paramsValue = new Vector();
		Vector params = new Vector();

//		Hashtable h_status_count = uploadUser.h_status_count;
		CommonLog.info("     Success entries : ");
		CommonLog.info("Unsuccessful entries : ");
		 
		String success_total =String.valueOf(0);
		String unsuccess_total =String.valueOf(0);
		
		if(MessageTemplate.isActive(con, prof.my_top_tc_id, "USER_IMPORT_SUCCESS")){
          //插入邮件及邮件内容
            MessageService msgService = new MessageService();
            Timestamp sendTime = cwSQL.getTime(con);
            
    		MessageTemplate mtp = new MessageTemplate();
    		mtp.setMtp_tcr_id(prof.my_top_tc_id);
    		mtp.setMtp_type("USER_IMPORT_SUCCESS");
    		mtp.getByTcr(con);
    		
            String[] contents = msgService.getImportMsgContent(con, mtp, prof.usr_ent_id, srcFile.getName(), startTime, endTime, success_total, unsuccess_total);
            msgService.insMessage(con, mtp, admin_usr_id,  new long[] {prof.usr_ent_id}, new long[0], sendTime, contents,0);
            
            con.commit();
            CommonLog.info("# Notificaion email created. ");
        }
		
	   } catch(Exception e) {
		   CommonLog.error(e.getMessage(),e);
            try {
                if (con != null)
                    con.rollback();
            } catch (SQLException e1) {
            	CommonLog.error(e1.getMessage(),e);
            }
	   } finally {
	       try {
               if(con != null && !con.isClosed()) con.close();
           } catch (SQLException e) {
        	   CommonLog.error("UploadUserThread failed to close connection");
           }
	   }
	}
}
