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
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.config.WizbiniLoader;
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
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author Randy
 *
 */
public class UploadEnrollmentThread extends Thread {
    private String admin_usr_id = "s1u3";
    private long admin_usr_ent_id = 3;

//    private Connection con;
    private WizbiniLoader wizbini;
    private HttpSession sess;
    private qdbEnv static_env;
    private loginProfile prof;
    private File srcFile;
    private File tmpFile;
    private long itm_id;
    private String  upload_type;
    
    public UploadEnrollmentThread(WizbiniLoader wizbini,
                            HttpSession sess, qdbEnv static_env, loginProfile prof,
                                File srcFile, File tmpFile, long itm_id, String upload_type, Connection con)
                                    throws cwException, SQLException {
        this.wizbini = wizbini;
        this.sess = sess;
        this.static_env = static_env;
        this.prof = prof;
        this.srcFile = srcFile;
        this.tmpFile = tmpFile;
        this.itm_id = itm_id;
        this.upload_type = upload_type;
        
        this.setPriority(Thread.MIN_PRIORITY);
        
        acSite ste = new acSite();
        ste.ste_ent_id = prof.root_ent_id;
        this.admin_usr_ent_id = ste.getSiteSysEntId(con);
        this.admin_usr_id = "s"+prof.root_ent_id+"u"+this.admin_usr_ent_id;
        
        CommonLog.debug("@"+this.admin_usr_id+"@");
    }
    
    public StringBuffer getXML(Connection con) throws SQLException,  cwSysMessage{
        StringBuffer xml = new StringBuffer(64);
        xml.append("<data_import src_file=\"").append(srcFile.getName()).append("\">");
        xml.append("</data_import>");
        xml.append(aeItem.getItemXMLForNav (con,itm_id));
        return xml;
    }
    
    public void run() {
        Connection con = null;
        try {
	        cwSQL sqlCon = new cwSQL();
	        sqlCon.setParam(wizbini);
	        try{
	            con = sqlCon.openDB(false);
	        } catch (Exception e) {
	            CommonLog.error("UploadEnrollmentThread: failed to open database connection");
	            return;
	        }
	        ///
	        long startTime = System.currentTimeMillis();
	        CommonLog.info("# UploadEnrollmentThread started. Processing " + tmpFile + " ...");
	        ///
	        
	        /////////////////////////////////////////
	        
	
	       // UploadEnrollment uploadEnrollment = new UploadEnrollment(con, static_env.ENCODING, static_env.INI_PATH, static_env.INI_DIR_UPLOAD_TMP, prof, upload_type); 
	       // String xml = uploadEnrollment.cookEnrollment(tmpFile.getAbsolutePath(), static_env, dbIlg.ilg_id, wizbini, itm_id);
	  
	            
	        ///////////////////////////////////
	        Hashtable h_status_count = new Hashtable();
	        DbIMSLog dbIlg = (DbIMSLog)sess.getAttribute(UploadModule.SESS_UPLOAD_LOG_RECORD);
	        dbIlg.ilg_type = IMSLog.IMSLOG_TYPE_ENROLLMENT;
	        dbIlg.ilg_process = IMSLog.IMSLOG_PROCESS_IMPORT;
	        dbIlg.ilg_target_id = IMSLog.ITM + itm_id;
	        IMSLog ilg = new IMSLog();
	        try {
	            ilg.ins(con, prof, dbIlg);
	            ilg.saveUploadedFile(static_env, dbIlg.ilg_id, (File)sess.getAttribute(UploadModule.SESS_UPLOADED_SRC_FILE));
	        //----------------------
	        } catch (SQLException e) {
	        	CommonLog.error(e.getMessage(),e);
	            return;
	        } catch (IOException e) {
	        	CommonLog.error(e.getMessage(),e);
	            return;
	        }
	        
	        try {
	            UploadEnrollment uploadEnrollment = new UploadEnrollment(con, static_env.ENCODING, static_env.INI_PATH, static_env.INI_DIR_UPLOAD_TMP, prof, upload_type); 
	            String xml = uploadEnrollment.cookEnrollment(tmpFile.getAbsolutePath(), static_env, dbIlg.ilg_id, wizbini, itm_id,prof);
	            h_status_count = uploadEnrollment.h_status_count; 
	        } catch (cwException e) {
	        	CommonLog.error(e.getMessage(),e);
	            return;
	        } catch (SQLException e) {
	        	CommonLog.error(e.getMessage(),e);
	            return;
	        } catch (IOException e) {
	        	CommonLog.error(e.getMessage(),e);
	            return;
	        } catch (JAXBException e) {
	        	CommonLog.error(e.getMessage(),e);
	            return;
	        }catch (cwSysMessage e) {
	        	CommonLog.error(e.getMessage(),e);
	            return;
	        }
	        con.commit();
	        ///
	        long endTime = System.currentTimeMillis();
	        CommonLog.info("# Import initiator = " + prof.usr_ent_id + ":" + prof.usr_id + ":" + prof.usr_ste_usr_id);
	        CommonLog.info("# Total cook time = " + (endTime - startTime));
	        CommonLog.info("# UploadEnrollmentThread ended. " + tmpFile + " imported.");
	        ///
	        
	        CommonLog.info("# Creating notification email ... ");
	        
	        Vector paramsName  = new Vector();
	        Vector paramsType  = new Vector();
	        Vector paramsValue = new Vector();
	        Vector params = new Vector();
	
	        CommonLog.info("     Success entries : "+(Integer)h_status_count.get(IMSEnterprise.STATUS_SUCCESS));
	        CommonLog.info("Unsuccessful entries : "+(Integer)h_status_count.get(IMSEnterprise.STATUS_UNSUCCESS));
	         
	        String success_total = ((Integer)h_status_count.get(IMSEnterprise.STATUS_SUCCESS)).toString();
	        String unsuccess_total = ((Integer)h_status_count.get(IMSEnterprise.STATUS_UNSUCCESS)).toString();

			if(MessageTemplate.isActive(con, prof.my_top_tc_id, "ENROLLMENT_IMPORT_SUCCESS")){
	          //插入邮件及邮件内容
	            MessageService msgService = new MessageService();
	            Timestamp sendTime = cwSQL.getTime(con);
	            
	    		MessageTemplate mtp = new MessageTemplate();
	    		mtp.setMtp_tcr_id(prof.my_top_tc_id);
	    		mtp.setMtp_type("ENROLLMENT_IMPORT_SUCCESS");
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
            	CommonLog.error(e1.getMessage(),e1);
            }
        }finally {
            try {
                if(con != null && !con.isClosed()) con.close();
            }
            catch (SQLException e) {
            	CommonLog.error("UplodEnrollmentThread: failed to close connection");
            	CommonLog.error(e.getMessage(),e);
            }
        }
    }
}
