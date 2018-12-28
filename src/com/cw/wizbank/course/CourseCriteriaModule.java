package com.cw.wizbank.course;

import java.io.*;
import java.sql.*;
import java.util.Vector;

import javax.servlet.http.*;

import com.oreilly.servlet.*;
import com.cw.wizbank.util.*;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbCourse;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.qdb.dbMessage;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.ae.aeAction;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.*;
import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.db.DbCourseCriteria;
import com.cw.wizbank.db.DbCourseMeasurement;
import com.cw.wizbank.db.view.ViewCourseMeasurement;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;

public class CourseCriteriaModule extends ServletModule {
    
    public static final String moduleName = "CourseCriteria";
	private final static String SMSG_DEL_MSG = "GEN002";
	private final static String SMSG_UPD_MSG = "GEN003";
	private final static String SMSG_INS_MSG = "GEN004";
	private final static String SMSG_UPDED_MSG = "GEN006";
    ServletUtils sutils = new ServletUtils();
    public void process() throws SQLException, IOException, cwException, qdbErrMessage{
//        String url_relogin = cwUtils.getRealPath(request, static_env.URL_RELOGIN);

        CourseCriteriaReqParam urlp = null;

        urlp = new CourseCriteriaReqParam(request, clientEnc, static_env.ENCODING);
		HttpSession session = request.getSession(false);
        if (bMultipart) {
            urlp.setMultiPart(multi);
        }
        
        urlp.common();
        
        PrintWriter out = response.getWriter();

        try {
            // if all command need authorized users
            if (urlp.cmd.equalsIgnoreCase("get_notify_xml") ){
                urlp.notification(clientEnc, static_env.ENCODING);
                String xml = CourseCriteria.getNotifyAsXML(con, urlp.ent_id, urlp.sender_id).toString();
                out.println((xml).trim());
            }else if (urlp.cmd.equalsIgnoreCase("COURSE_COMPLETION_XML")) {
                urlp.course_completion();
                StringBuffer xml = new StringBuffer();
                xml.append(urlp.aeXmsg.getSenderXml(con, static_env.MAIL_ACCOUNT));
                xml.append(urlp.aeXmsg.getRecipientXml(con, urlp.aeXmsg.ent_id, static_env.MAIL_ACCOUNT, static_env.DES_KEY));
                xml.append(CourseCriteria.getCourseXml(con, urlp.aeXmsg.id, urlp.tkh_id));
                out.println((xml.toString()).trim());
            }else{
                if (prof == null) {
                    response.sendRedirect(static_env.URL_SESSION_TIME_OUT);
        //            response.sendRedirect(url_relogin);        
                }
                else if (urlp.cmd.equalsIgnoreCase("get_criteria_lst") || urlp.cmd.equalsIgnoreCase("get_criteria_lst_xml")){
                	
                	
                        urlp.criteria();
                        urlp.pagination();
                        HttpSession sess = request.getSession(true);
                        
                        if (urlp.type == null){
                            throw new cwException("please input a ccr_type");    
                        }
                        if (urlp.itm_id==0){
                            urlp.itm_id = dbCourse.getCosItemId(con, urlp.cos_id);                                                        
                        }
                        if (!CourseCriteria.checkExist(con, urlp.itm_id, urlp.type)){
                            CourseCriteria.insCriteria(con, urlp.itm_id, urlp.type, urlp.upd_method, urlp.attendance_rate, 
                                urlp.pass_score, urlp.must_pass, urlp.must_meet_all_cond, prof.usr_id,null, false); 
                            con.commit();
                        } 
                        AcPageVariant acPageVariant = new AcPageVariant(con);
                        acPageVariant.prof = prof;
                        acPageVariant.instance_id = urlp.itm_id;
                        acPageVariant.ent_owner_ent_id = prof.root_ent_id;
                        acPageVariant.ent_id = prof.usr_ent_id;
                        acPageVariant.rol_ext_id = prof.current_role;
                        String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(urlp.stylesheet));
                        StringBuffer result = new StringBuffer(formatXML(CourseCriteria.getCriteriaByItmAsXML(con, urlp.itm_id, urlp.type, urlp.del_cmr_id ).toString(), metaXML,"CourseCriteria"));

                        if(urlp.cmd.equalsIgnoreCase("get_criteria_lst_xml"))
                        static_env.outputXML(out, result.toString());
                        if(urlp.cmd.equalsIgnoreCase("get_criteria_lst")){
                            generalAsHtml(result.toString(), out, urlp.stylesheet);
                        }
                }
                else if (urlp.cmd.equalsIgnoreCase("upd_criteria")){
                        urlp.criteria();
                        if (urlp.itm_id==0){
                            urlp.itm_id = dbCourse.getCosItemId(con, urlp.cos_id);                                                        
                        }

                        AcItem acitm = new AcItem(con);
                        if(!acitm.hasMaintainPrivilege(urlp.itm_id, prof.usr_ent_id,
                                                  prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }

                        if (urlp.has_duration && urlp.type.equalsIgnoreCase(DbCourseCriteria.TYPE_COMPLETION)){
                            aeItem itm = new aeItem();
                            itm.itm_id = urlp.itm_id;
                            itm.itm_content_eff_duration = urlp.duration;
                            itm.updItemContentEffDuration(con, prof.usr_id);
                            urlp.duration = 0;
                        }
                        CourseCriteria ccr = new CourseCriteria();
                        ccr.updCourseCriteria(con, urlp.ccr_id, urlp.upd_method, urlp.duration, urlp.attendance_rate, 
                            urlp.pass_score, urlp.must_pass, urlp.must_meet_all_cond, prof.usr_id); 
                
//                        aeAttendance.autoUpdateAttendance(con, prof.usr_id, prof.root_ent_id, urlp.itm_id, 0, true, false, false); 

                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equalsIgnoreCase("del_criteria_module")){
                        urlp.criteria();
                        if (urlp.itm_id==0){
                            urlp.itm_id = dbCourse.getCosItemId(con, urlp.cos_id);                                                        
                        }

                        AcItem acitm = new AcItem(con);
                        if(!acitm.hasMaintainPrivilege(urlp.itm_id, prof.usr_ent_id,
                                                  prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }
                        CourseCriteria ccr = new CourseCriteria();
                        ccr.delCriteriaModule(con, urlp.cmr_lst); 
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }
                else if (urlp.cmd.equalsIgnoreCase("soft_del_criteria_module")){
                        urlp.criteria();
                        if (urlp.itm_id==0){
                            urlp.itm_id = dbCourse.getCosItemId(con, urlp.cos_id);                                                        
                        }

                        AcItem acitm = new AcItem(con);
                        if(!acitm.hasMaintainPrivilege(urlp.itm_id, prof.usr_ent_id,
                                                  prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }
                        CourseCriteria ccr = new CourseCriteria();
                        ccr.softDelCriteriaModule(con, urlp.cmr_lst, prof.usr_id); 
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }

                else if (urlp.cmd.equalsIgnoreCase("pick_module")){
                        urlp.criteria();
                        if (urlp.itm_id==0){
                            urlp.itm_id = dbCourse.getCosItemId(con, urlp.cos_id);                                                        
                        }

                        AcItem acitm = new AcItem(con);
                        if(!acitm.hasMaintainPrivilege(urlp.itm_id, prof.usr_ent_id,
                                                  prof.current_role, prof.root_ent_id)) {
                            throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
                        }

                        CourseCriteria ccr = new CourseCriteria();
                        ccr.pickModule(con, urlp.ccr_id, urlp.mod_id, urlp.status, urlp.rate, urlp.is_contri_by_score, prof.usr_id);
                        con.commit();
                        response.sendRedirect(urlp.url_success);
                }
				else if (urlp.cmd.equalsIgnoreCase("upd_multi_status")) {
					urlp.criteria();
					if (urlp.itm_id == 0) {
						urlp.itm_id = dbCourse.getCosItemId(con, urlp.cos_id);
					}

					AcItem acitm = new AcItem(con);
					if (!acitm
						.hasMaintainPrivilege(
							urlp.itm_id,
							prof.usr_ent_id,
							prof.current_role,
							prof.root_ent_id)) {
						throw new cwSysMessage(dbMessage.NO_RIGHT_UPDATE_MSG);
					}
					CourseCriteria ccr = new CourseCriteria();

					if (urlp.del_cmr_id > 0) {
						ccr.softDelCriteriaModule(
							con,
							new long[] { urlp.del_cmr_id },
							prof.usr_id);
					}
					ccr.updMultiStatus(
						con,
						prof.usr_id,
						urlp.cmr_lst,
						urlp.status_lst,
						urlp.contri_rate_lst,
						urlp.is_contri_by_score_lst);
					con.commit();
					response.sendRedirect(urlp.url_success);
				}
				//get score scheme list
				else if (urlp.cmd.equalsIgnoreCase("get_cmt_lst")
						|| urlp.cmd.equalsIgnoreCase("get_cmt_lst_xml")) {
					StringBuffer resultBuf = new StringBuffer(2000);
					urlp.attendance(clientEnc, static_env.ENCODING);
					aeItem itm = new aeItem();
					itm.itm_id = urlp.itm_id;
					itm.getItem(con);
					resultBuf.append(itm.contentInfoAsXML(con));
					dbModule dbm = new dbModule();
					CourseCriteria ccr = new CourseCriteria();
					CourseMeasurement cmt = new CourseMeasurement();
					long ccr_id = ccr.getCcrIdByItmNType(con,itm.itm_id);
					resultBuf.append(cmt.getCmtLstAsXML(con,ccr_id,urlp.cmt_id_del,session, itm));
					resultBuf.append(dbm.getAvailableOnLineWithScoreListAsXml(con,urlp.cos_res_id,ccr_id));
					
					resultBuf.append(aeItem.genItemActionNavXML(con, urlp.itm_id, prof));
					resultBuf.append("<current_role>").append(prof.current_role).append("</current_role>");
					String result = formatXML(resultBuf.toString(), moduleName);
					if (urlp.cmd.equals("get_cmt_lst_xml")) {
						out.println(result);
					} else {
						generalAsHtml(result, out, urlp.stylesheet);
					}
				}	
				else if (urlp.cmd.equalsIgnoreCase("upd_cmt")
						|| urlp.cmd.equalsIgnoreCase("upd_cmt_xml")) {
					StringBuffer resultBuf = new StringBuffer(2000);
					CourseMeasurement cmt = new CourseMeasurement();
					dbModule dbm = new dbModule();
					urlp.attendance(clientEnc, static_env.ENCODING);
					aeItem itm = new aeItem();
					itm.itm_id = urlp.itm_id;
					itm.getItem(con);					
					resultBuf.append(itm.contentInfoAsXML(con));
					resultBuf.append(cmt.getCmtAsXml(con, urlp.cmt_id));
					resultBuf.append(dbm.getAvailableOnLineWithScoreListAsXml(con,urlp.cos_res_id,urlp.ccr_id));
					
					resultBuf.append(aeItem.genItemActionNavXML(con, urlp.itm_id, prof));	
					resultBuf.append("<current_role>").append(prof.current_role).append("</current_role>");
					String result = formatXML(resultBuf.toString(), moduleName);
					if (urlp.cmd.equals("upd_cmt_xml")) {
						out.println(result);
					} else {
						generalAsHtml(result, out, urlp.stylesheet);
					}
				}
				else if (urlp.cmd.equalsIgnoreCase("upd_cmt_exec")
						|| urlp.cmd.equalsIgnoreCase("upd_cmt_exec_xml")) {
                    CourseMeasurement cmt = new CourseMeasurement();
                    dbModule dbm = new dbModule();
                    urlp.attendance(clientEnc, static_env.ENCODING);
                    //DbCourseMeasurement dbCmt = new DbCourseMeasurement();
                    //float oldPassScore = dbCmt.getByCmtId(con,urlp.cmt_id).getCmt_pass_score();
                    if(cmt.updCmt(con,urlp.cmt_id,urlp.ccr_id,urlp.cmt_title,urlp.mod_res_id,urlp.cmt_max_score,urlp.cmt_pass_score,urlp.upd_timestamp,prof)){                          
                            ViewCourseMeasurement.updLrnStatus(con,dbAiccPath.STATUS_PASSED,urlp.cmt_id,urlp.cmt_pass_score);
                            ViewCourseMeasurement.updLrnStatus(con,dbAiccPath.STATUS_FAILED,urlp.cmt_id,urlp.cmt_pass_score);
                        
                        if (urlp.re_evaluate_ind){
                        		CommonLog.info("==================================WizBank will Re_score the relate learners!!!!!");
                          
                                CourseCriteria reScore = new CourseCriteria();
                                reScore.setAttDate(urlp.upd_comp_date);
                                
                                reScore.setFromMarkingSchema(con,prof,urlp.ccr_id,true,false,true,urlp.upd_comp_date);
                        }
                        con.commit();
                        aeItem itm = new aeItem();
                        itm.itm_id = urlp.itm_id;
                        itm.get(con);
                        long parent_itm_id = 0;
    				    ObjectActionLog	log = new ObjectActionLog(itm.itm_id, 
    				    		itm.itm_code,
    				    		itm.itm_title,
    							ObjectActionLog.OBJECT_TYPE_CREDITS,
    							ObjectActionLog.OBJECT_ACTION_UPD,
    							ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
    							prof.getUsr_ent_id(),
    							prof.getUsr_last_login_date(),
								prof.getIp()
    					);
                        if(itm.itm_run_ind || itm.itm_session_ind) {
                            Long temp = (Long)sess.getAttribute(aeAction.SESS_PARENT_ITM_ID);
                            if(temp != null) {
                                parent_itm_id = temp.longValue();
                                itm.parent_itm_id = parent_itm_id;
                            }else{
                            	CommonLog.info("temp is null");
                            }
                        }
        				if(aeItem.ITM_TYPE_CLASSROOM.equalsIgnoreCase(itm.itm_type)){
            				if(itm.parent_itm_id!=0){
    							Vector parent = new Vector();
    							itm.getParentsInfo(con, itm.parent_itm_id, parent);
    							if(parent.size()>0){
    								log.setObjectTitle(((aeItem)parent.get(0)).itm_title + " > " +itm.itm_title);
    							}
    						}
        				}
    				    SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
        
                        cwSysMessage sms = new cwSysMessage(SMSG_UPD_MSG);
                        msgBox(MSG_STATUS, sms, urlp.url_success, out);
                    }
                }
				else if (urlp.cmd.equalsIgnoreCase("add_new_cmt")
						|| urlp.cmd.equalsIgnoreCase("add_new_cmt_xml")) {
					StringBuffer resultBuf = new StringBuffer(2000);
					urlp.attendance(clientEnc, static_env.ENCODING);
					aeItem itm = new aeItem();
					itm.itm_id = urlp.itm_id;
					itm.getItem(con);					
					resultBuf.append(itm.contentInfoAsXML(con));
					session = request.getSession(true);
                    if(urlp.cmt_title != null) {
                        session.setAttribute("cmt_title", urlp.cmt_title);
                        session.setAttribute("cmt_max_score",new Float(urlp.cmt_max_score));
                        session.setAttribute("cmt_pass_score",new Float(urlp.cmt_pass_score));
                    }
					if (urlp.mod_res_id != 0) {
						session.setAttribute("mod_res_id",new Long(urlp.mod_res_id));
					}
					CourseMeasurement cmt = new CourseMeasurement();
					if (!cmt.isTitleExist(con, urlp.ccr_id, urlp.cmt_title, session))
						resultBuf.append(cmt.getCmtLstAsXML(con, urlp.ccr_id, urlp.cmt_id_del, session, itm));
					
					resultBuf.append(aeItem.genItemActionNavXML(con, urlp.itm_id, prof));
					resultBuf.append("<current_role>").append(prof.current_role).append("</current_role>");
					String result = formatXML(resultBuf.toString(), moduleName);
					if (urlp.cmd.equals("add_new_cmt_xml")) {
						out.println(result);
					} else {
						generalAsHtml(result, out, urlp.stylesheet);
					}
				} else if (urlp.cmd.equalsIgnoreCase("all_cmt_oper_final")
						|| urlp.cmd.equalsIgnoreCase("all_cmt_oper_final_xml")) {
                    cwSysMessage sms = new cwSysMessage(SMSG_UPD_MSG);
                    StringBuffer resultBuf = new StringBuffer(2000);
                    urlp.attendance(clientEnc, static_env.ENCODING);    
                    CourseMeasurement cmt = new CourseMeasurement();
                    ObjectActionLog log = null;
                    cmt.modCmtPercent(con,urlp.cmt_id_list,urlp.cmt_id_percent_list,urlp.upd_timestamp_list,urlp.cmt_id_del,prof,urlp.itm_id);
                    aeItem itm = new aeItem();
                    itm.itm_id = urlp.itm_id;
                    itm.get(con);
                    long parent_itm_id = 0;
                    for (int i = 0; i < urlp.cmt_id_list.length; i++) {
                        if (urlp.cmt_id_list[i] == 0) {
                            Long newId = cmt.insScoreCmt(con,urlp.mod_res_id,urlp.ccr_id,urlp.cmt_title,urlp.itm_id,urlp.cmt_id_percent_list[i],urlp.cmt_max_score,urlp.cmt_pass_score,prof);
                            sms = new cwSysMessage(SMSG_INS_MSG);
        					log = new ObjectActionLog(itm.itm_id, 
        							itm.itm_code,
        							itm.itm_title,
        							ObjectActionLog.OBJECT_TYPE_CREDITS,
        							ObjectActionLog.OBJECT_ACTION_ADD,
        							ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
        							prof.getUsr_ent_id(),
        							prof.getUsr_last_login_date(),
									prof.getIp()
        					);
                            if(itm.itm_run_ind || itm.itm_session_ind) {
                                Long temp = (Long)sess.getAttribute(aeAction.SESS_PARENT_ITM_ID);
                                if(temp != null) {
                                    parent_itm_id = temp.longValue();
                                    itm.parent_itm_id = parent_itm_id;
                                }else{
                                	CommonLog.info("temp is null");
                                }
                            }
            				if(aeItem.ITM_TYPE_CLASSROOM.equalsIgnoreCase(itm.itm_type)){
                				if(itm.parent_itm_id!=0){
        							Vector parent = new Vector();
        							itm.getParentsInfo(con, itm.parent_itm_id, parent);
        							if(parent.size()>0){
        								log.setObjectTitle(((aeItem)parent.get(0)).itm_title + " > " +itm.itm_title);
        							}
        						}
            				}
                        }
                    }                   
                    if (urlp.cmt_id_del != 0){
                    	String title = cmt.getTitleByCmtId(con,urlp.cmt_id_del );
                        cmt.delCmt(con, urlp.cmt_id_del, prof);
                        sms = new cwSysMessage(SMSG_DEL_MSG);
                        ObjectActionLog	delLog = new ObjectActionLog(itm.itm_id, 
                        			itm.itm_code,
                        			itm.itm_title,
        							ObjectActionLog.OBJECT_TYPE_CREDITS,
        							ObjectActionLog.OBJECT_ACTION_DEL,
        							ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
        							prof.getUsr_ent_id(),
        							prof.getUsr_last_login_date(),
									prof.getIp()
        					);
                        if(itm.itm_run_ind || itm.itm_session_ind) {
                            Long temp = (Long)sess.getAttribute(aeAction.SESS_PARENT_ITM_ID);
                            if(temp != null) {
                                parent_itm_id = temp.longValue();
                                itm.parent_itm_id = parent_itm_id;
                            }else{
                            	CommonLog.info("temp is null");
                            }
                        }
        				if(aeItem.ITM_TYPE_CLASSROOM.equalsIgnoreCase(itm.itm_type)){
            				if(itm.parent_itm_id!=0){
    							Vector parent = new Vector();
    							itm.getParentsInfo(con, itm.parent_itm_id, parent);
    							if(parent.size()>0){
    								log.setObjectTitle(((aeItem)parent.get(0)).itm_title + " > " +itm.itm_title);
    							}
    						}
        				}
                        SystemLogContext.saveLog(delLog, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                    }
                    if (urlp.re_evaluate_ind){
                    	  CommonLog.info("==================================WizBank will Re_score the relate learners!!!!!");
                          
                          CourseCriteria reScore = new CourseCriteria();
                          reScore.setFromMarkingSchema(con,prof,urlp.ccr_id,true,false,true,urlp.upd_comp_date);
                    }
                    con.commit();        
                    if(null!=log){
                    	SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                    }
                    msgBox(MSG_STATUS, sms, urlp.url_success, out);
                }
				else if (urlp.cmd.equalsIgnoreCase("get_criteria_cond_lst")
						|| urlp.cmd.equalsIgnoreCase("get_criteria_cond_lst_xml")) {
                    urlp.getItemId();
					urlp.getIsNewCos();
					urlp.getRedirectUrl();

                    CourseMeasurement cmt = new CourseMeasurement();
                    String xml = "";
                    String metaXML = "";
                    if(urlp.itm_id != 0){
                        aeItem itm = new aeItem();
                        itm.itm_id = urlp.itm_id;
                        itm.get(con);

                        long cos_id = dbCourse.getCosResId(con, itm.itm_id);
                        dbCourse cos = new dbCourse();
                        cos.cos_res_id = cos_id;
                        cos.get(con);
                        
                        long ccr_id = DbCourseCriteria.getCcrIdByItmID(con, itm.itm_id, DbCourseCriteria.TYPE_COMPLETION);
                        if(urlp.is_new_cos){
                        	dbModule dbMod = new dbModule();
                        	Vector onlineScoreModVec = dbMod.getAvailableOnLineWithScoreVec(con, cos_id, ccr_id);
                        	if(cos.cos_structure_xml == null){
                        		//跳到发布范围
                        		response.sendRedirect(urlp.redirect_url);
                        		return;
                        	}else if(onlineScoreModVec.size()>0){
                    			dbModule mod = null;
                        		for(int i=0; i<onlineScoreModVec.size(); i++){
                        			mod = (dbModule)onlineScoreModVec.get(i);
                        			String cmt_title = mod.mod_res_title+mod.mod_res_id;
                        			Float cmt_rate = (float) ((int)100/onlineScoreModVec.size());
                        			cmt.insScoreCmt(con,mod.mod_res_id, ccr_id, cmt_title, itm.itm_id, cmt_rate, mod.mod_max_score, mod.mod_pass_score, prof);
                        		}	
                        	}
                        }
                        metaXML = itm.contentInfoAsXML(con);
                        xml = cmt.getCondLstAsXML(con, urlp.itm_id);
                        /*
                        if(itm.itm_create_run_ind){
                            CourseMeasurement cm = new CourseMeasurement();
                            xml = cm.getCosCondLstAsXML(con, urlp.itm_id);
                            
                        }else{
                            CourseMeasurement cm = new CourseMeasurement();
                            xml = cm.getRunCondLstAsXML(con, urlp.itm_id);
                        }
                        */
                    }
                    
                    xml += aeItem.genItemActionNavXML(con, urlp.itm_id, prof);
                    xml +="<is_new_cos>"+urlp.is_new_cos+"</is_new_cos>";
                    xml +="<current_role>"+prof.current_role+"</current_role>";
                    StringBuffer result = new StringBuffer(formatXML(xml, metaXML, "CourseCriteria", urlp.stylesheet));
                    if(urlp.cmd.equalsIgnoreCase("get_criteria_cond_lst_xml"))
                        static_env.outputXML(out, result.toString());
                    if(urlp.cmd.equalsIgnoreCase("get_criteria_cond_lst")){
                        generalAsHtml(result.toString(), out, urlp.stylesheet);
                    }
			}
				else if (urlp.cmd.equalsIgnoreCase("mod_cos_criteria_cond")
                        || urlp.cmd
                                .equalsIgnoreCase("mod_cos_criteria_cond_xml")) {
                    urlp.getItemId();
                    // urlp.getCosCondRequest();
                      urlp.getRunCondRequest();
                      CourseMeasurement cm = new CourseMeasurement();
                      boolean result = false;
                      //result = cm.modCourseCriteriaCond(con, prof, urlp.ccr_id, urlp.itm_id,
                      result = cm.modCriteriaCond(con, prof, urlp.ccr_id, urlp.itm_id,
                              urlp.pass_status, urlp.attend_status,
                              urlp.ccr_pass_score, urlp.ccr_attendance_rate,
                              urlp.cmtOnlineVec, urlp.cmtScoringVec, urlp.offline_cond, urlp.ccr_update_timestamp);
                      if(result){
                          //re
                          if (urlp.recal.equalsIgnoreCase("on")){
                              CourseCriteria reScore = new CourseCriteria();
                              boolean reset_date = false;
                              if(urlp.recal_date.equalsIgnoreCase("on")){
                                  reScore.setAttDate(true);
                                  reset_date = true;
                              }
                              reScore.setFromMarkingSchema(con, prof, urlp.ccr_id, true,false,true,reset_date);
                           }
                          con.commit();
                          cwSysMessage msg = new cwSysMessage("CND001");
                          msgBox(ServletModule.MSG_STATUS, msg, urlp.url_success, out);
                      }else{
                          cwSysMessage msg = new cwSysMessage("CND002");
                          msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
                      }
                  }
				else if (urlp.cmd.equalsIgnoreCase("mod_run_criteria_cond")
                        || urlp.cmd.equalsIgnoreCase("mod_run_criteria_cond_xml")) {
                    urlp.getItemId();
                    urlp.getRunCondRequest();
                    CourseMeasurement cm = new CourseMeasurement();
                    boolean result = false;
                    result = cm.modCriteriaCond(con, prof, urlp.ccr_id, urlp.itm_id,
                            urlp.pass_status, urlp.attend_status,
                            urlp.ccr_pass_score, urlp.ccr_attendance_rate,
                            urlp.cmtOnlineVec, urlp.cmtScoringVec, urlp.offline_cond, urlp.ccr_update_timestamp);
                    if(result){
                    	
                        if (urlp.recal.equalsIgnoreCase("on")){
                        	CourseCriteria reScore = new CourseCriteria();
                            boolean reset_date = false;
                            if(urlp.recal_date.equalsIgnoreCase("on")){
                                reset_date = true;
                                reScore.setAttDate(true);
                            }
                            
                            reScore.setFromMarkingSchema(con, prof, urlp.ccr_id, true,false,true,reset_date);
                         }
                        con.commit();
                        cwSysMessage msg = new cwSysMessage("GEN003");
                        aeItem itm = new aeItem();
                        itm.itm_id=urlp.itm_id;
                        itm.get(con);
        				ObjectActionLog log = new ObjectActionLog(itm.itm_id, 
        						itm.itm_code,
        						itm.itm_title,
        						ObjectActionLog.OBJECT_TYPE_CC,
        						ObjectActionLog.OBJECT_ACTION_UPD,
        						ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
        						prof.getUsr_ent_id(),
        						prof.getUsr_last_login_date(),
								prof.getIp()
        				);
                        long parent_itm_id = 0;
                        //if it is a run, get the parent item id from session
                        if(itm.itm_run_ind || itm.itm_session_ind) {
                            Long temp = (Long)sess.getAttribute(aeAction.SESS_PARENT_ITM_ID);
                            if(temp != null) {
                                parent_itm_id = temp.longValue();
                                itm.parent_itm_id = parent_itm_id;
                            }else{
                            	CommonLog.info("temp is null");
                            }
                        }
        				if(aeItem.ITM_TYPE_CLASSROOM.equalsIgnoreCase(itm.itm_type)){
            				if(itm.parent_itm_id!=0){
    							Vector parent = new Vector();
    							itm.getParentsInfo(con, itm.parent_itm_id, parent);
    							if(parent.size()>0){
    								log.setObjectTitle(((aeItem)parent.get(0)).itm_title + " > " +itm.itm_title);
    							}
    						}
        				}
        				SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                        
                        msgBox(ServletModule.MSG_STATUS, msg, urlp.url_success, out);
                    }else{
                        cwSysMessage msg = new cwSysMessage("CND002");
                        msgBox(ServletModule.MSG_ERROR, msg, urlp.url_failure, out);
                    }
                }
                else {
                    throw new cwException("Invalid Command");
                }
            }
        }catch (qdbException e) {
            con.rollback();
            out.println("QDB error: " + e.getMessage());
        }catch (cwSysMessage se) {
            try {
                 con.rollback();
                 msgBox(ServletModule.MSG_ERROR, se, urlp.url_failure, out);
             } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
             }
        }
        
    }
}

