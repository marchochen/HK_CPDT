package com.cwn.wizbank.scheduled;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.dataMigrate.imp.Imp;
import com.cw.wizbank.dataMigrate.imp.ImpCPDRegistration;
import com.cw.wizbank.dataMigrate.imp.ImpUser;
import com.cw.wizbank.db.DbIMSLog;
import com.cw.wizbank.enterprise.IMSLog;
import com.cw.wizbank.newmessage.MessageService;
import com.cw.wizbank.newmessage.entity.MessageTemplate;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class ImportCPDFromFile extends ScheduledTask {

	// loginprofile 保存上传文件的用户，String 保存上传文件的文件名称
	public static HashMap<loginProfile, String> uploadFile = new HashMap<loginProfile, String>();
	// loginprofile 保存上传文件的用户，String 保存上传文件的說明
	public static Map<loginProfile, String> uploadDesc= new HashMap<loginProfile, String>();
	
    public static Map<String, Object> resultData= new HashMap<String, Object>();


	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void process() {
		try {
			con = dbSource.openDB(false);
			run(con);
			con.commit();
//			con.rollback();
			logger.debug("ImportCPDFromFile.process() start");
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			logger.debug("ImportCPDFromFile.process() error", e);
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
					logger.debug("ImportCPDFromFile.process() error", e);
					CommonLog.error(e.getMessage(),e);
				}
			}
		}

	}

	public void run(Connection con) {
		try {
			
			/*loginProfile profile = new loginProfile();
			profile.usr_id = "s1u3";
			profile.my_top_tc_id = 1;
			profile.cur_lan = "zh-cn";
			profile.usr_ent_id = 3;
	    	ImportCPDFromFile.uploadFile.put(profile,"wb_import_cpd_profile_template-zh-cn.xls");
	    	if(false){
	    		return;
	    	}*/
			String dir =static_env.DOC_ROOT+cwUtils.SLASH+static_env.LOG_FOLDER+cwUtils.SLASH+IMSLog.FOLDER_CPDLOG+cwUtils.SLASH;
			Map<loginProfile, String> uploadFileClone = null;
			uploadFileClone = (HashMap<loginProfile, String>)uploadFile.clone();
			uploadFile.clear();
			Set<loginProfile> key = uploadFileClone.keySet();
			for (loginProfile prof : key) {
				try{
					String file_path = dir + uploadFileClone.get(prof);
					File srcFile=new File(file_path);
					//向数据库记录此次导入文件，日志信息
					DbIMSLog dbIlg=new DbIMSLog();
					dbIlg.ilg_type = IMSLog.IMSLOG_TYPE_CPD;
					dbIlg.ilg_process = IMSLog.IMSLOG_PROCESS_IMPORT;
					dbIlg.ilg_create_usr_id = prof.usr_id;
		            dbIlg.ilg_create_timestamp = cwSQL.getTime(con);
		            dbIlg.ilg_method=IMSLog.ACTION_TRIGGERED_BY_UI;
		            dbIlg.ilg_desc=uploadDesc.get(prof);
		            dbIlg.ilg_filename=srcFile.getName();
		            dbIlg.ilg_tcr_id=prof.my_top_tc_id;
					long logid=DbIMSLog.ins(con, dbIlg);
					con.commit();
					File logFile=new File(dir+logid);
					//如果目录存在就创建
					if(!logFile.exists()){
						logFile.mkdir();
					}
					//开始导入用户
					long startTime = System.currentTimeMillis();
					dbRegUser reguser = new dbRegUser();
					reguser.ent_id = prof.usr_ent_id;
					reguser.usr_ent_id = prof.usr_ent_id;
					reguser.get(con);
					Imp imp = new ImpCPDRegistration(con, wizbini, logFile.getAbsolutePath()+cwUtils.SLASH, file_path);
					Imp.ImportStatus importStatus= imp.doImp(prof, false);
					long endTime = System.currentTimeMillis();
					
					//导入CPD之后给导入人发送邮件
					String success_total =String.valueOf(importStatus.cnt_success);//插入成功的条数
					String unsuccess_total =String.valueOf(importStatus.cnt_error);//插入失败的条数
					
					resultData.put("success_total", String.valueOf(importStatus.cnt_success));
					resultData.put("unsuccess_total", String.valueOf(importStatus.cnt_error));

					IMSLog iLog = new IMSLog();
					iLog.CPD=true;
                    Hashtable h_file_uri = iLog.getLogFilesURI(dbIlg.ilg_id, static_env);
                    resultData.put("success_href", (String)h_file_uri.get("success.txt"));
                    
					
					if(MessageTemplate.isActive(con, prof.my_top_tc_id, "CPD_REGISTRATION_IMPORT_SUCCESS")){
			          //插入邮件及邮件内容
			            MessageService msgService = new MessageService();
			            Timestamp sendTime = cwSQL.getTime(con);
			    		MessageTemplate mtp = new MessageTemplate();
			    		mtp.setMtp_tcr_id(prof.my_top_tc_id);
			    		mtp.setMtp_type("CPD_REGISTRATION_IMPORT_SUCCESS");
			    		mtp.getByTcr(con);
			            String[] contents = msgService.getImportMsgContent(con, mtp, prof.usr_ent_id, srcFile.getName(), startTime, endTime, success_total, unsuccess_total);
			            msgService.insMessage(con, mtp, "s1u3",  new long[] {prof.usr_ent_id}, new long[0], sendTime, contents,0);
			           
			        }
				}catch(Exception ex){
					ex.printStackTrace();
//					logger.debug("ImportCPDFromFile.process() error", ex);
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
//			logger.debug("ImportCPDFromFile.process() error", e);
		}
	}
}
