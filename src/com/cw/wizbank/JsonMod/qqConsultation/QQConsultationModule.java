package com.cw.wizbank.JsonMod.qqConsultation;

import java.io.IOException;
import java.sql.SQLException;

import com.cw.wizbank.JsonMod.tcrCommon.TcrModule;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.security.AclFunction;

public class QQConsultationModule extends TcrModule {
	private static final String xslRootElemtnStr = "qqConsultation";
	private static final String ADD = "ADD";
	private static final String UPD = "UPD";
	private static final String DEL = "DEL";
	private static final String ON = "ON";
	private static final String OFF = "OFF";
	private static final QQConsultationLogic qqConsultationLogic = QQConsultationLogic.getInstance();
	private QQConsultationParam qqParam = null;

	public QQConsultationModule() {
		super(AclFunction.FTN_TEMP, QQConsultationParam.class, new String[]{},
				new String[]{});
		qqParam = (QQConsultationParam)tcrParam;
		isTtcModule = false;
	}
	
	public void getCompanyQQLst(){
		try {
			resultXml = formatXML(qqConsultationLogic.getCompanyQQLst(con), xslRootElemtnStr);
		} catch (Exception e) {
			logger.error("method getCompanyQQLst() excuete error.", e);
			throw new RuntimeException("method getCompanyQQLst() execute error.");
		}
	}
	
	public void disOperatingPre(){
		try {
			resultXml = formatXML(qqConsultationLogic.disOperatingCompanyQQPre(con, qqParam, prof), xslRootElemtnStr);
		} catch (Exception e) {
			logger.error("method disOperatingPre() excuete error.", e);
			throw new RuntimeException("method disOperatingPre() execute error.");
		}
	}
	
	public void operatingComnanyQQExe() throws IOException, cwException, SQLException{
		String operating = qqParam.getOperating();
		cwSysMessage suc_message = null;
		cwSysMessage fail_message = null;
		try{
			 if(operating.equals(ADD)){
				 Long cpqId = (Long)sqlMapClient.getsingleColumn(con, "select cpq_id from companyQQ where cpq_code = ?", 
						 new Object[]{qqParam.getCpq_code()}, Long.class);
				 if(cpqId != null && cpqId.longValue() > 0){
					 fail_message = new cwSysMessage("de_pro_08", new String[]{qqParam.getCpq_code()});
					 msgBox(MSG_STATUS, fail_message, qqParam.getUrl_failure(), out);
					 return;
				 }
				 suc_message = new cwSysMessage("cqq_01");
			 }else if(operating.equals(UPD)){
				 Long cpqId = (Long)sqlMapClient.getsingleColumn(con, "select cpq_id from companyQQ where cpq_code = ? and cpq_id != ?", 
						 new Object[]{qqParam.getCpq_code(), new Long(qqParam.getCpq_id())}, Long.class);
				 if(cpqId != null && cpqId.longValue() > 0){
					 fail_message = new cwSysMessage("de_pro_08", new String[]{qqParam.getCpq_code()});
					 msgBox(MSG_STATUS, fail_message, qqParam.getUrl_failure(), out);
					 return;
				 }
				 suc_message = new cwSysMessage("cqq_03");
			 }else if(operating.equals(DEL)){
				 suc_message = new cwSysMessage("cqq_05");	
			 }
			 qqConsultationLogic.OperatingCompanyQQExe(con, qqParam, prof);
			 
			 msgBox(MSG_STATUS, suc_message, qqParam.getUrl_success(), out);
		}catch(Exception e){
			isTransactionException = true;
			logger.error("method operatingComnanyQQExe() execute error.\n", e);
			if(operating.equals(ADD)){
				 fail_message = new cwSysMessage("cqq_02");	
			 }else if(operating.equals(UPD)){
				 fail_message = new cwSysMessage("cqq_04");
			 }else if(operating.equals(DEL)){
				 fail_message = new cwSysMessage("cqq_06");
			 }
			 msgBox(MSG_STATUS, fail_message, qqParam.getUrl_failure(), out);
		}
	}
	
	public void updateQQStatus() throws IOException , cwException , SQLException{
		String operating = qqParam.getOperating();
		cwSysMessage suc_message = null;
		cwSysMessage fail_message = null;
		try{
			if(operating.equals(ON)){
				qqConsultationLogic.updateQQStatus(con, qqParam);
				suc_message =new cwSysMessage("cqq_enable_sucess");
			}else if(operating.equals(OFF)){
				qqConsultationLogic.updateQQStatus(con, qqParam);
				suc_message =new cwSysMessage("cqq_disable_sucess");
			}
			msgBox(MSG_STATUS, suc_message, qqParam.getUrl_success(), out);
		}catch(Exception e){
			isTransactionException = true;
			logger.error("method operatingComnanyQQExe() execute error.\n", e);
			if(operating.equals(ON)){
				fail_message = new cwSysMessage("cqq_enable_fail");
			}else if(operating.equals(OFF)){
				fail_message = new cwSysMessage("cqq_disable_fail");
			}
			msgBox(MSG_STATUS, fail_message, qqParam.getUrl_failure(), out);
		}
		
	}

}
