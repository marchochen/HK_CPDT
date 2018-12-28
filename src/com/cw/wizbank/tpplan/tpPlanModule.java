package com.cw.wizbank.tpplan;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AcTrainingCenter;
import com.cw.wizbank.accesscontrol.AcTrainingPlan;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.tpplan.db.dbTpTrainingPlan;
import com.cw.wizbank.tpplan.db.dbTpYearPlan;
import com.cw.wizbank.tpplan.db.dbTpYearSetting;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
 * @author jackyx
 * @date 2007-10-15
 */
public class tpPlanModule extends ServletModule {
	public static final String moduleName = "tptrainingplan";
	public static final String criteriamoduleName = "Class_Criteria";
	public static final String recruitregistmoduleName = "Recruit_Regist";
	public static final String LRN_EVALUATE = "lrn_evaluate";
	public static final String CERTIFICATE = "Certificate";
	
	public static final String SESS_UPLOAD_YEAR_PLAN = "SESS_UPLOAD_YEAR_PLAN";
	public static final String SESS_UPLOAD_ENROLLMENT1 = "SESS_UPLOAD_ENROLLMENT1";
	public static final String SESS_UPLOAD_ENROLLMENT2 = "SESS_UPLOAD_ENROLLMENT2";
	public static final String SESS_UPLOAD_ENROLLMENT3 = "SESS_UPLOAD_ENROLLMENT3";
	public static final String SESS_UPLOAD_OTHER = "SESS_UPLOAD_OTHER";
	public static final String SESS_UPLOAD_CLS_EVA = "SESS_UPLOAD_CLS_EVA";
	public static final String SESS_UPLOAD_CLS_SUM = "SESS_UPLOAD_CLS_SUM";
	
	public static final String SESS_UPLOAD_CRITERIA1 = "SESS_UPLOAD_CRITERIA1";
	public static final String SESS_UPLOAD_CRITERIA2 = "SESS_UPLOAD_CRITERIA2";
	public static final String SESS_UPLOAD_CRITERIA_COL_INFO = "SESS_UPLOAD_CRITERIA_COL_INFO";

    public tpPlanModule() { ; }
    
    private static String YEAR_SETTING = "year_setting";

	public void process() throws IOException, cwException, SQLException{
		if (prof == null) {
			response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
		}

		PrintWriter out = response.getWriter();
		tpPlanReqParam urlp = new tpPlanReqParam(request, clientEnc,static_env.ENCODING);
        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        urlp.common();
		HttpSession sess = request.getSession(true);
        urlp.pagination();
		try {
        	if(urlp.cmd == null) {
        		throw new cwException("Invalid Command");
        	} 
        	else if (urlp.cmd.equalsIgnoreCase("get_training_plan_lst") || urlp.cmd.equalsIgnoreCase("get_training_plan_lst_xml")) {
				urlp.plan();
				urlp.getPageParam();
				
				AcTrainingPlan actp = new AcTrainingPlan(con);
                if (!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
                	throw new cwSysMessage("ACL002");
				}
              	if (wizbini.cfgTcEnabled && !ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id) && !wizbini.cfgSysSetupadv.isTcIndependent()) {
                    cwSysMessage sms = new cwSysMessage("TC013");
                    msgBox(ServletModule.MSG_ERROR, sms, urlp.url_failure, out);
                    return;
            	}
				tpPlanManagement tpm = new tpPlanManagement();
				String contentXML = tpm.getOrgTrainingPlanXML(con, wizbini, prof, urlp.tpn_status, urlp.tpn_status_lst, urlp.tpn_type, urlp.tpn_date_year, urlp.tpn_date_month, urlp.is_makeup, urlp.cwPage);
				String result = formatXML(contentXML, moduleName);
				if (urlp.cmd.equals("get_training_plan_lst_xml")) {
					out.println(result);
				} else {
					generalAsHtml(result, out, urlp.stylesheet);
				}
			} 
			else if (urlp.cmd.equalsIgnoreCase("refer_markup_plan")){
				urlp.plan();
				AcTrainingPlan actp = new AcTrainingPlan(con);
                if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
				  throw new cwSysMessage("ACL002");
				}
	        	AcTrainingCenter actc = new AcTrainingCenter(con);
				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
					throw new cwSysMessage("ACL002");
				}
                tpPlanManagement tp = new tpPlanManagement();
                tp.referMarkupPlan(con, urlp.tpn_id, urlp.tpn_update_timestamp, prof);
                con.commit();
	            cwSysMessage msg = new cwSysMessage("TPN001");
                msgBox(ServletModule.MSG_STATUS, msg, urlp.url_success, out);
			}
			else if (urlp.cmd.equalsIgnoreCase("del_markup_plan")){
				urlp.plan();
				AcTrainingPlan actp = new AcTrainingPlan(con);
                if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
				  throw new cwSysMessage("ACL002");
				}
	        	AcTrainingCenter actc = new AcTrainingCenter(con);
				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
					throw new cwSysMessage("ACL002");
				}
	        	
                dbTpTrainingPlan tpn = new dbTpTrainingPlan();
                tpn.tpn_update_timestamp = urlp.tpn_update_timestamp;
                tpn.tpn_id = urlp.tpn_id;
                tpn.checkTimeStamp(con);

                tpn.del(con);
                con.commit();
	            cwSysMessage msg = new cwSysMessage("TPN002");
                msgBox(ServletModule.MSG_STATUS, msg, urlp.url_success, out);
			}
			else if(urlp.cmd.equalsIgnoreCase("set_year_config_prepare") || urlp.cmd.equalsIgnoreCase("set_year_config_prepare_xml")) {
	        	urlp.tpPlanConfig();
				AcTrainingPlan actp = new AcTrainingPlan(con);
                if (!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
                	throw new cwSysMessage("ACL002");
				}
				String dataXML = null;			
				dataXML = ViewYearSetting.getYearSettingXML(con, prof, wizbini, urlp.year, urlp.tcr_id);
				dataXML = formatXML(dataXML, null, YEAR_SETTING);
				if(urlp.cmd.equalsIgnoreCase("set_year_config_prepare_xml")){
					static_env.outputXML((PrintWriter)out, dataXML);
				}else{
					generalAsHtml(dataXML, (PrintWriter)out, urlp.stylesheet);
				}        	
	        } 
			else if(urlp.cmd.equalsIgnoreCase("save_year_config")) {
	        	urlp.tpPlanConfig();
	        	if(urlp.tys.ysg_tcr_id > 0) {
	        		AcTrainingCenter actc = new AcTrainingCenter(con);
	        		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
		        		if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tys.ysg_tcr_id)) {
		        			throw new cwSysMessage("ACL002");
		        		}
	        		}
	        	}
	        	Timestamp cur_time = cwSQL.getTime(con);
	        	dbTpYearSetting dbtys = dbTpYearSetting.get(con, urlp.tcr_id, urlp.year);
	        	urlp.tys.ysg_update_timestamp = cur_time;
	        	urlp.tys.ysg_update_usr_id = prof.usr_id;
	        	
	        	if(dbtys != null) {
	        		if(urlp.upd_timestamp != null ) {
//	    	            Timestamp tTmp = dbtys.ysg_update_timestamp;
//	    	            tTmp.setNanos(urlp.upd_timestamp.getNanos());
//	    	            if(tTmp.equals(urlp.upd_timestamp))
	    	            	urlp.tys.upd(con);
//	    	            else
//	    	            	throw new cwSysMessage("GEN006");
	        		}
	        		
	        	} else {
	        		urlp.tys.ysg_create_timestamp = cur_time;
	        		urlp.tys.ysg_create_usr_id = prof.usr_id;
	        		urlp.tys.ins(con);
	        	}
	            con.commit();
			    cwSysMessage sms = new cwSysMessage("GEN003");
			    msgBox(MSG_STATUS, sms, urlp.url_success, out);
	        }
	        else if(urlp.cmd.equalsIgnoreCase("submit_year_plan")) {
	        	urlp.yearPlan();
				AcTrainingPlan actp = new AcTrainingPlan(con);
                if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
				  throw new cwSysMessage("ACL002");
				}
                if(AccessControlWZB.isRoleTcInd(prof.current_role)){
    	        	AcTrainingCenter actc = new AcTrainingCenter(con);
    				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
    					throw new cwSysMessage("ACL002");
    				}
                }

                tpPlanManagement tp =  new tpPlanManagement();
                tp.submitYearPlan(con, urlp.year, urlp.ypn_tcr_id, urlp.upd_timestamp, prof, wizbini);
	            con.commit();
			    cwSysMessage sms = new cwSysMessage("TPN001");
			    msgBox(MSG_STATUS, sms, urlp.url_success, out);
	        }
	        else if (urlp.cmd.equalsIgnoreCase("del_year_plan")) {
	        	urlp.yearPlan();
				AcTrainingPlan actp = new AcTrainingPlan(con);
                if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
				  throw new cwSysMessage("ACL002");
				}
                if(AccessControlWZB.isRoleTcInd(prof.current_role)){
    	        	AcTrainingCenter actc = new AcTrainingCenter(con);
    				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
    					throw new cwSysMessage("ACL002");
    				}
                }

                tpPlanManagement tp =  new tpPlanManagement();
                tp.delYearPlan(con, wizbini, urlp.year, urlp.ypn_tcr_id, urlp.upd_timestamp);
                con.commit();
	            cwSysMessage msg = new cwSysMessage("TPN002");
                msgBox(ServletModule.MSG_STATUS, msg, urlp.url_success, out);

	        } 
	        else if (urlp.cmd.equalsIgnoreCase("add_training_plan_pre") || urlp.cmd.equalsIgnoreCase("add_training_plan_pre_xml")) {
        		urlp.getAddtpTrainingPlan();
				AcTrainingPlan actp = new AcTrainingPlan(con);
                if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
				  throw new cwSysMessage("ACL002");
				}
              	if (wizbini.cfgTcEnabled && !ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id)) {
                    cwSysMessage sms = new cwSysMessage("TC013");
                    msgBox(ServletModule.MSG_ERROR, sms, urlp.url_failure, out);
                    return;
            	}
        		tpPlanManagement tpPM = new tpPlanManagement();
        		StringBuffer metaXML = new StringBuffer();
        		metaXML.append("<current_timestamp>").append(cwSQL.getTime(con)).append("</current_timestamp>");
        		metaXML.append("<cur_training_center id=\"").append(urlp.tcr_id).append("\">");
        		metaXML.append("<title>").append(cwUtils.esc4XML("Training Center Name")).append("</title>");
        		metaXML.append("</cur_training_center>");
        		String xml =formatXML(tpPM.getTraingingPlanPreviewXML(con, prof), metaXML.toString(), moduleName) ;
        		if(urlp.cmd.equalsIgnoreCase("add_training_plan_pre_xml")) {
        			out.println(xml);
        		} else {
        			generalAsHtml(xml, out, urlp.stylesheet);
        		}
        	} 
	        else if (urlp.cmd.equalsIgnoreCase("add_training_plan_exe")) {
        		try{
        			//check user have add new training plan
        			urlp.getTrainingPlan();
    				AcTrainingPlan actp = new AcTrainingPlan(con);
                    if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
    				  throw new cwSysMessage("ACL002");
    				}
    	        	AcTrainingCenter actc = new AcTrainingCenter(con);
    				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tpTp.tpn_tcr_id)) {
    					throw new cwSysMessage("ACL002");
    				}
        			tpPlanManagement tpPM = new tpPlanManagement();
        			tpPM.insTrainingPlan(con, prof, urlp.tpTp);
        			con.commit();
        			cwSysMessage e = new cwSysMessage(tpPlanManagement.TP_INS_SUCCESS);
        			msgBox(MSG_STATUS, e, urlp.url_success, out);
        		} catch(cwSysMessage e) {
        			con.rollback();
        			msgBox(MSG_ERROR, e, urlp.url_failure, out);
        		}
        	} 
	        else if (urlp.cmd.equalsIgnoreCase("upd_training_plan_exe")) {
        		try {
        			urlp.getTrainingPlan();
        			AcTrainingPlan actp = new AcTrainingPlan(con);
                    if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
    				  throw new cwSysMessage("ACL002");
    				}
    	        	AcTrainingCenter actc = new AcTrainingCenter(con);
    				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
    					throw new cwSysMessage("ACL002");
    				}
        			tpPlanManagement tpPM = new tpPlanManagement();
        			//check user have modified the training plan
        			tpPM.updTrainingPlan(con, prof, urlp.tpTp);
        			con.commit();
        			cwSysMessage e = new cwSysMessage("TPN004");
        			msgBox(MSG_STATUS, e, urlp.url_success, out);
        		} catch(cwSysMessage e) {
        			con.rollback();
        			msgBox(MSG_ERROR, e, urlp.url_failure, out);
        		}
        	} 
	        else if (urlp.cmd.equalsIgnoreCase("get_training_plan") || urlp.cmd.equalsIgnoreCase("get_training_plan_xml")) {
        		urlp.getTrainingPlan();
        		//check user have read the training plan
				AcTrainingPlan actp = new AcTrainingPlan(con);
                if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
				  throw new cwSysMessage("ACL002");
				}
	        	AcTrainingCenter actc = new AcTrainingCenter(con);
				if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
					throw new cwSysMessage("ACL002");
				}
        		tpPlanManagement pm = new tpPlanManagement();
        		StringBuffer metaXML = new StringBuffer();
        		metaXML.append("<current_timestamp>").append(cwSQL.getTime(con)).append("</current_timestamp>");
        		metaXML.append("<cur_training_center id=\"").append(urlp.tcr_id).append("\">");
        		metaXML.append("</cur_training_center>");
                AcPageVariant acPageVariant = new AcPageVariant(con);
                acPageVariant.prof = prof;
                acPageVariant.instance_id = urlp.tpn_id;
                acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                acPageVariant.ent_id = prof.usr_ent_id;
                acPageVariant.rol_ext_id = prof.current_role;
                acPageVariant.tcr_id = urlp.tcr_id;
                metaXML.append(acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet)));
                metaXML.append("<entrance>").append(urlp.entrance).append("</entrance>");
        		String xml = pm.getTrainingPlanInfoAsXML(con, prof, urlp.tpn_id);
        		xml = formatXML(xml, metaXML.toString(), moduleName);
        		if(urlp.cmd.equalsIgnoreCase("get_training_plan_xml")) {
        			out.println(xml);
        		} else {
        			generalAsHtml(xml, out, urlp.stylesheet);
        		}
	        } else if (urlp.cmd.equalsIgnoreCase("upload_year_plan_prep") || urlp.cmd.equalsIgnoreCase("upload_year_plan_prep_xml")) {
	        	try{
	        		urlp.getUpLoadYearPrep();
	        		AcTrainingPlan actp = new AcTrainingPlan(con);
	        		if (!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
	        			throw new cwSysMessage("ACL002");
	        		}
	        		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
		        		AcTrainingCenter actc = new AcTrainingCenter(con);
		        		if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
		        			throw new cwSysMessage("ACL002");
		        		}
	        		}

	        		String metaXML = "";
        			dbTpYearPlan ypn = new dbTpYearPlan();
        			ypn.ypn_year = urlp.year;
        			ypn.ypn_tcr_id = urlp.tcr_id;
        			if (urlp.upd_timestamp != null) {
        				ypn.ypn_update_timestamp = urlp.upd_timestamp;
        				ypn.checkTimeStamp(con);
        				metaXML += "<upd_timestamp>" + ypn.ypn_update_timestamp + "</upd_timestamp>";
        			}else if(dbTpYearPlan.isExistYearPlan(con, urlp.year, urlp.tcr_id)) {
        				 throw new cwSysMessage("GEN006");
        			}
        			tpPlanManagement tpMgt = new tpPlanManagement();
        			String xml = tpMgt.getUploadPlanPrepXML(con, urlp.tcr_id, urlp.year, urlp.ypn_year);
	        		xml =formatXML(xml, metaXML, moduleName);
	        		if (urlp.cmd.equalsIgnoreCase("upload_year_plan_prep_xml")) {
	        			out.println(xml);
	        		} else {
	        			generalAsHtml(xml, out, urlp.stylesheet);
	        		}
	        	} catch (cwSysMessage e) {
        			urlp.url_failure ="javascript:window.close();";
        			msgBox(MSG_ERROR, e, urlp.url_failure, out);
	        	}
	        } else if(urlp.cmd.equalsIgnoreCase("upload_year_plan_confirm") || urlp.cmd.equalsIgnoreCase("upload_year_plan_confirm_xml")) {
	        	try {
	        		urlp.getUploadYearConfirm();
	        		AcTrainingPlan actp = new AcTrainingPlan(con);
	        		if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
	        			throw new cwSysMessage("ACL002");
	        		}
	        		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
		        		AcTrainingCenter actc = new AcTrainingCenter(con);
		        		if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
		        			throw new cwSysMessage("ACL002");
		        		}
	        		}

	        		String newFileName = null;
	        		File srcFile = null ;
	        		String  xml = null;
	        		String metaXML = null;
	        		metaXML = "<plan_prep_info>";
	        		metaXML +="<year>"+urlp.year+"</year>";
	        		metaXML +="<tcr_id>"+urlp.tcr_id+"</tcr_id>";
	        		metaXML +="<ypn_year>"+urlp.ypn_year+"</ypn_year>";
	        		metaXML +="</plan_prep_info>";
	        		Enumeration files = multi.getFileNames();
	        		if(files != null ) {
	        			while(files.hasMoreElements()){
	        				String name = (String)files.nextElement();
	        				newFileName = multi.getFilesystemName(name);
	        				if( newFileName != null && newFileName.length() > 0)  {
	        					srcFile = new File(tmpUploadPath, newFileName);
	        					break;
	        				}
	        			}
	        			dbTpYearPlan ypn = new dbTpYearPlan();
	        			ypn.ypn_year = urlp.year;
	        			ypn.ypn_tcr_id = urlp.tcr_id;
	        			if(urlp.upd_timestamp != null) {
	        				ypn.ypn_update_timestamp = urlp.upd_timestamp;
	        				ypn.checkTimeStamp(con);
	        				ypn.get(con);
	        				metaXML += "<upd_timestamp>" + ypn.ypn_update_timestamp + "</upd_timestamp>";
	        			} else if(dbTpYearPlan.isExistYearPlan(con, urlp.year, urlp.tcr_id)) {
        					ypn.get(con);
        					metaXML += "<upd_timestamp>" + ypn.ypn_update_timestamp + "</upd_timestamp>";
        				}
	        			tpPlanUpLoad plan = new tpPlanUpLoad(con, prof.label_lan, urlp.tcr_id, urlp.year);
	        			//Vector plan_vec = new Vector(); 
	        			xml = plan.uploadPlan(srcFile, wizbini, false, prof, urlp.year, urlp.tcr_id);
	        			//save upload file
	        			if(plan.passed) {
	        				ypn.ypn_file_name = newFileName;
	        				sess.setAttribute(SESS_UPLOAD_YEAR_PLAN + "-" + urlp.tcr_id +"-" + urlp.year, ypn);
	        				sess.setAttribute(newFileName, tmpUploadPath);
	        			}
	        		} else {
	        			xml = "<invalid_file>" + "" + "</invalid_file>";
	        		}
	        		xml = formatXML(xml, metaXML, moduleName);
	        		if(urlp.cmd.equalsIgnoreCase("upload_year_plan_confirm_xml")) {
	        			out.println(xml);
	        		} else {
	        			generalAsHtml(xml, out, urlp.stylesheet);
	        		}
	        	} catch(cwSysMessage e) {
        			urlp.url_failure ="javascript:window.close();";
        			msgBox(MSG_ERROR, e, urlp.url_failure, out);
	        	}
	        } else if(urlp.cmd.equalsIgnoreCase("upload_year_plan_exec")) {
	        	try {
	        		urlp.getUploadYearConfirm();
					AcTrainingPlan actp = new AcTrainingPlan(con);
	                if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
	                	throw new cwSysMessage("ACL002");
					}
	                if(AccessControlWZB.isRoleTcInd(prof.current_role)){
			        	AcTrainingCenter actc = new AcTrainingCenter(con);
						if(!actc.canMgtTc(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
							throw new cwSysMessage("ACL002");
						}
	                }

	        		dbTpYearPlan ypn = (dbTpYearPlan)sess.getAttribute(SESS_UPLOAD_YEAR_PLAN + "-" + urlp.tcr_id +"-" + urlp.year);
	        		tmpUploadPath = (String)sess.getAttribute(ypn.ypn_file_name);
	        		boolean pass = ypn.saveTpYearPlan(con, prof, wizbini, tmpUploadPath, ypn, urlp.upd_timestamp);
	        		if(pass) {
	        			con.commit();
	        			urlp.url_success ="javascript:window.opener.document.location.reload();window.close();";
	        			cwSysMessage e = new cwSysMessage("CND004");
	        			msgBox(MSG_STATUS, e, urlp.url_success, out);
	        		} else {
	        			con.rollback();
	        			cwSysMessage e = new cwSysMessage("FMT001");
	        			msgBox(MSG_STATUS, e, urlp.url_success, out);
	        		}
	        	} catch(cwSysMessage e) {
        			con.rollback();
        			urlp.url_failure ="javascript:window.close();";
        			msgBox(MSG_ERROR, e, urlp.url_failure, out);
	        	}
	        } else if(urlp.cmd.equalsIgnoreCase("get_year_plan_lst")
	        		|| urlp.cmd.equalsIgnoreCase("get_year_plan_lst_xml")) {
	        	urlp.yearPlan();
              	if (wizbini.cfgTcEnabled && !ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id) && !wizbini.cfgSysSetupadv.isTcIndependent()) {
                    cwSysMessage sms = new cwSysMessage("TC013");
                    msgBox(ServletModule.MSG_ERROR, sms, urlp.url_failure, out);
                    return;
            	}
				String dataXML = null;
				dataXML = tpPlanManagement.getYearPlanXML(con, wizbini, prof, urlp.cwPage);
				dataXML = formatXML(dataXML, null, moduleName);
				if(urlp.cmd.equalsIgnoreCase("get_year_plan_lst_xml")){
					static_env.outputXML((PrintWriter)out, dataXML);
				}else{
					generalAsHtml(dataXML, (PrintWriter)out, urlp.stylesheet);
				}
	        } 
	     else if(urlp.cmd.equalsIgnoreCase("get_out_training_plan_lst") || urlp.cmd.equalsIgnoreCase("get_out_training_plan_lst_xml")){
	        urlp.tpPlanSearch();
	        urlp.getPageParam();
	        
			AcTrainingPlan actp = new AcTrainingPlan(con);
            if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
			  throw new cwSysMessage("ACL002");
			}
          	if (wizbini.cfgTcEnabled && !ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id) && !wizbini.cfgSysSetupadv.isTcIndependent()) {
                cwSysMessage sms = new cwSysMessage("TC013");
                msgBox(ServletModule.MSG_ERROR, sms, urlp.url_failure, out);
                return;
        	}
			StringBuffer xml = new StringBuffer();
			tpPlanManagement tpPlanMgt = new tpPlanManagement();
			xml.append(tpPlanMgt.getMakeupplanXml(con, prof, urlp.status, urlp.cwPage));
            String result = formatXML(xml.toString(),moduleName);
            if(urlp.cmd.equalsIgnoreCase("get_out_training_plan_lst_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet);
            }                         
		}else if (urlp.cmd.equalsIgnoreCase("upd_status_exec")){
			urlp.tpUpdStaParam();
	    	AcTrainingCenter actc = new AcTrainingCenter(con);
	    	if(urlp.tcr_id > 0) {
	    		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
	        		if(!actc.canMgtTcForTp(prof.usr_ent_id, prof.current_role, prof.root_ent_id, urlp.tcr_id)) {
	        			throw new cwSysMessage("ACL002");
	        		}    
	    		}
	    	}
	    	if(urlp.sel_tcr_id !=null && urlp.tcr_id_list.length > 0) {
	    		if(AccessControlWZB.isRoleTcInd(prof.current_role)){
		    		for(int i = 0; i<urlp.tcr_id_list.length; i++) {
		    			long this_tcr_id = urlp.tcr_id_list[i];
		    			if(!actc.canMgtTcForTp(prof.usr_ent_id, prof.current_role, prof.root_ent_id, this_tcr_id)) {
		        			throw new cwSysMessage("ACL002");
		        		}    
		    		}
	    		}
	    	}
			tpPlanManagement tpPlanMgt = new tpPlanManagement();
			tpPlanMgt.auditingPlan(con, urlp.status, urlp.tpn_id_list, urlp.update_timestamp, urlp.plantype, wizbini, prof, urlp.tcr_id_list);
            con.commit();
            response.sendRedirect(urlp.url_success);
        }else if(urlp.cmd.equalsIgnoreCase("get_auditing_yearPlan_lst") || urlp.cmd.equalsIgnoreCase( "get_auditing_yearPlan_lst_xml")){
        	urlp.tpPlanSearch();
			AcTrainingPlan actp = new AcTrainingPlan(con);        	
            if(!actp.hasMaintainPrivilege(prof.usr_ent_id, prof.current_role)) {
  			  throw new cwSysMessage("ACL002");
  			}
          	if (wizbini.cfgTcEnabled && !ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id) && !wizbini.cfgSysSetupadv.isTcIndependent()) {
                cwSysMessage sms = new cwSysMessage("TC013");
                msgBox(ServletModule.MSG_ERROR, sms, urlp.url_failure, out);
                return;
        	}
			tpPlanManagement tpPlanMgt = new tpPlanManagement();
			String xml = tpPlanMgt.getYearplanXml(con, prof, urlp.status, urlp.cwPage);
			String result = formatXML(xml, moduleName);
            if(urlp.cmd.equalsIgnoreCase("get_auditing_yearPlan_lst_xml")) {
                out.println(result.toString());
            } else {
                generalAsHtml(result.toString(), out, urlp.stylesheet);
            }                                     
		} 
		else {
				throw new cwException("Invalid Command");
			}
		}  catch (cwSysMessage se) {
			try {
				con.rollback();
				msgBox(ServletModule.MSG_ERROR, se, urlp.url_failure, out);
			} catch (SQLException sqle) {
				out.println("SQL error: " + sqle.getMessage());
			}
		} catch(qdbException qdbe) {
			con.rollback();
			out.println("SQL error: " + qdbe.getMessage());
		}
	        
	}
}
