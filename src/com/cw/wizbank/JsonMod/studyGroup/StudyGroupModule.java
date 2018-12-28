package com.cw.wizbank.JsonMod.studyGroup;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupBean;
import com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupResBean;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeXMessage;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbErrMessage;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.studyGroup.StudyGroupDAO;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXSL;
import com.cwn.wizbank.security.AclFunction;

/**
 * 用于处理与学习小组有关的信息
 */
public class StudyGroupModule extends ServletModule {

	StudyGroupModuleParam modParam;
	
	public StudyGroupModule() {
		super();
		modParam = new StudyGroupModuleParam();
		param = modParam;
	}

	public void process() throws SQLException, IOException, cwException {
		
		try {
				if(this.prof == null || this.prof.usr_ent_id == 0){	
					if(modParam.getCmd().equalsIgnoreCase("GET_STUDYGROUP_REMINDER_XML")){
						StudyGroup sgp= new StudyGroup();
						aeXMessage aeXmsg = new aeXMessage();
						aeXmsg.sender_id = modParam.getSender_id();
						StringBuffer xml = new StringBuffer();
		                xml.append(aeXmsg.getSenderXml(con, static_env.MAIL_ACCOUNT));                    
		                xml.append(aeXmsg.getEmailsAsXml(con, modParam.getEnt_ids(), null, null, static_env.MAIL_ACCOUNT));
		                xml.append(sgp.getEmailContentXML(con, modParam.getSgp_id()));
	                    out.println((xml.toString()).trim());  
	                    return;
					}else{
						throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
					}
				} else {

					if (modParam.getCmd().equalsIgnoreCase("get_std_grp")) {	
						StudyGroup sgrp =new StudyGroup();
						if(wizbini.cfgTcEnabled) {
							Course cos = new Course();
							// 获取用户相关的培训中心	
							Vector tc_lst = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
							String tcr_id_lst=sgrp.getTcrIdLst(tc_lst);						
							modParam.setTcr_id_lst(tcr_id_lst.toString());
							sgrp.getSgpLst(con, prof, modParam, resultJson ,true);	
						}else{
							sgrp.getSgpLst(con, prof, modParam, resultJson ,false);	
						}
						
					} else if (modParam.getCmd().equalsIgnoreCase("search_std_grp")) {
						StudyGroup sgrp =new StudyGroup();
						Vector sgpVc =new Vector();
						if(wizbini.cfgTcEnabled) {
							Course cos = new Course();
							Vector tc_lst = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
							String tcr_id_lst=sgrp.getTcrIdLst(tc_lst);
							modParam.setTcr_id_lst(tcr_id_lst);
							sgpVc=sgrp.searchSgpResult(con, prof, modParam,true);
						}else{
							sgpVc=sgrp.searchSgpResult(con, prof, modParam,false);
						}
						HashMap srhMap =new HashMap(); 
						srhMap.put("sgp_lst", sgpVc);
						srhMap.put("total", new Integer(modParam.getTotal_rec()));
						resultJson.put("srh_sgp", srhMap);
					}else if (modParam.getCmd().equalsIgnoreCase("search_sgp_lst")) {
						StudyGroup sgrp =new StudyGroup();
						Vector sgpVc =new Vector();
						if(wizbini.cfgTcEnabled) {
							Course cos = new Course();
							Vector tc_lst = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
							String tcr_id_lst=sgrp.getTcrIdLst(tc_lst);
							modParam.setTcr_id_lst(tcr_id_lst);
							sgpVc=sgrp.searchSgpResult(con, prof, modParam,true);
						}else{
							sgpVc=sgrp.searchSgpResult(con, prof, modParam,false);
						}
						//resultMap.put("sgp_lst", sgpVc);
						HashMap srhMap =new HashMap(); 
						srhMap.put("sgp_lst", sgpVc);
						srhMap.put("total", new Integer(modParam.getTotal_rec()));
						resultJson.put("srh_sgp", srhMap);
						hasMetaAndSkin=false;
					}else if (modParam.getCmd().equalsIgnoreCase("get_mgt_sgp")) {
						StudyGroup sgrp =new StudyGroup();			
						if(wizbini.cfgTcEnabled) {
							Course cos = new Course();
							Vector tc_lst = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
							String tcr_id_lst=sgrp.getTcrIdLst(tc_lst);						
							modParam.setTcr_id_lst(tcr_id_lst.toString());
							sgrp.getMgtSgpLst(con, prof, modParam, resultJson, true);			
						}else{
							sgrp.getMgtSgpLst(con, prof, modParam, resultJson, false);	
						}
						hasMetaAndSkin=false;
						
					}else if (modParam.getCmd().equalsIgnoreCase("get_join_sgp")) {
						StudyGroup sgrp =new StudyGroup();
						if(wizbini.cfgTcEnabled) {
							Course cos = new Course();
							Vector tc_lst = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
							String tcr_id_lst=sgrp.getTcrIdLst(tc_lst);						
							modParam.setTcr_id_lst(tcr_id_lst.toString());
							sgrp.getJoinSgpLst(con, prof, modParam, resultJson, true);
						}else{
							sgrp.getJoinSgpLst(con, prof, modParam, resultJson, false);
						}
						hasMetaAndSkin=false;
						
					}else if (modParam.getCmd().equalsIgnoreCase("get_other_sgp")) {
						StudyGroup sgrp =new StudyGroup();			
						if(wizbini.cfgTcEnabled) {		
							Course cos = new Course();
							Vector tc_lst = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
							String tcr_id_lst=sgrp.getTcrIdLst(tc_lst);						
							modParam.setTcr_id_lst(tcr_id_lst.toString());
							sgrp.getOtherSgpLst(con, prof, modParam, resultJson, true);							
						}else{
							sgrp.getOtherSgpLst(con, prof, modParam, resultJson, false);
						}
						hasMetaAndSkin=false;
						
					}else if (modParam.getCmd().equalsIgnoreCase("get_sgp_info")) {
						StudyGroup sgrp =new StudyGroup();
						StudyGroupDAO sgpDao= new StudyGroupDAO();
						AccessControlWZB acWZB = new AccessControlWZB();
						boolean isMem=sgpDao.isUsrSgpMem(con, modParam.getSgp_id(), prof.usr_ent_id);
//						if(!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)&&!isMem 
//								&& !sgpDao.isCurUsrMgt(con, prof, modParam.getSgp_id()) ){
//			            	throw new cwSysMessage("SGP004"); 
//						}
						StudyGroupBean sgp=sgpDao.getStudyGroupDetail(con, modParam.getSgp_id());
						if(sgp.getSgp_id()==0){
			            	throw new cwSysMessage("SGP001"); 
						}
						sgrp.getSgpInfo(con, prof, modParam, resultJson,wizbini,sgp);
						JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupBean.class, "sgp_desc_noescape", defJsonConfig);
						JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupBean.class, "sgp_title_noescape", defJsonConfig);
						JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupResBean.class, "sgs_title_noescape", defJsonConfig);
						JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupResBean.class, "sgs_content_noescape", defJsonConfig);
						JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.studyGroup.bean.StudyGroupResBean.class, "sgs_desc_noescape", defJsonConfig);
					}else if (modParam.getCmd().equalsIgnoreCase("get_mem_lst")) {
						StudyGroupDAO sgpDAO= new StudyGroupDAO();
						Vector memVc = sgpDAO.getStudyGroupMemLst(con,prof, modParam,wizbini);
						HashMap usrMap = new HashMap();
						usrMap.put("total", new Integer(modParam.getTotal_rec()));
						usrMap.put("usr_lst", memVc);
						resultJson.put("sgp_usr", usrMap);
						hasMetaAndSkin=false;
						
					}else if (modParam.getCmd().equalsIgnoreCase("get_res_lst")) {
						StudyGroupDAO sgpDAO= new StudyGroupDAO();
						Vector resVc =sgpDAO.getStudyGroupResLst(con,prof, modParam,wizbini);
						HashMap resMap = new HashMap();
						resMap.put("total", new Integer(modParam.getTotal_rec()));
						resMap.put("res_lst", resVc);
						resultJson.put("sgp_res", resMap);
						hasMetaAndSkin=false;
					}else if (modParam.getCmd().equalsIgnoreCase("upd_sgp_info")) {
						StudyGroup sgrp =new StudyGroup();
						StudyGroupDAO sgpDAO= new StudyGroupDAO();
						AccessControlWZB acWZB = new AccessControlWZB();
//						if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)
//								&& !sgpDAO.isCurUsrMgt(con, prof, modParam.getSgp_id())) {
//			            	throw new cwSysMessage("ACL002");
//			            } 
						sgrp.updSgpInfo(con,  modParam);	
						
					}else if (modParam.getCmd().equalsIgnoreCase("ins_sgp_res")) {
						StudyGroup sgrp =new StudyGroup();
						String fileName="";
						String tempfile="";
						if(request.getSession().getAttribute("NEW_FILENAME")!=null){
							Vector fileVc=(Vector)request.getSession().getAttribute("NEW_FILENAME");
							fileName =(String)fileVc.lastElement();
							tempfile=tmpUploadPath+"\\"+fileName;
						}					
						sgrp.addSgpRes(con, prof, modParam,fileName, tempfile,wizbini);		
						redirectUrl = modParam.getUrl_success();
						
					}else if (modParam.getCmd().equalsIgnoreCase("get_sgp_res_info")) {
						StudyGroupDAO sgpDAO= new StudyGroupDAO();					
						StudyGroupResBean res=sgpDAO.getStudyGroupRes(con, modParam.getSgs_id());
						resultJson.put("sgp_res", res);
						
					}else if (modParam.getCmd().equalsIgnoreCase("upd_sgp_res")) {
						StudyGroup sgrp =new StudyGroup();
						StudyGroupDAO sgpDao= new StudyGroupDAO();
						AccessControlWZB acWZB = new AccessControlWZB();
//						if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)
//								&& !sgpDao.isCurUsrMgt(con, prof, modParam.getSgp_id()) 
//								&& !sgpDao.isUsrSgpResCreator(con, modParam.getSgs_id(), prof.usr_ste_usr_id)) {
//			            	throw new cwSysMessage("ACL002");
//
//			            } 
						boolean sgsExist=sgpDao.isSgsExist(con, modParam.getSgs_id(), modParam.getSgs_upd_timestamp());
						if(!sgsExist){
							throw new cwSysMessage("SGS001");
						}
						String fileName="";
						String tempfile="";
						if(request.getSession().getAttribute("NEW_FILENAME")!=null){
							Vector fileVc=(Vector)request.getSession().getAttribute("NEW_FILENAME");
							fileName =(String)fileVc.lastElement();
							tempfile=tmpUploadPath+"\\"+fileName;
						}		
						sgrp.updSgpRes(con, prof, modParam,fileName,tempfile,wizbini);
						response.sendRedirect(param.getUrl_success());
						
					}else if (modParam.getCmd().equalsIgnoreCase("del_sgp_res")) {
						StudyGroup sgrp =new StudyGroup();
						StudyGroupDAO sgpDao= new StudyGroupDAO();
						AccessControlWZB acWZB = new AccessControlWZB();
//						if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)
//								&& !sgpDao.isCurUsrMgt(con, prof, modParam.getSgp_id()) 
//								&& !sgpDao.isUsrSgpResCreator(con, modParam.getSgs_id(), prof.usr_ste_usr_id)) {
//			            	throw new cwSysMessage("ACL002");
//
//			            }
						boolean sgsExist=sgpDao.isSgsExist(con, modParam.getSgs_id(), modParam.getSgs_upd_timestamp());
						if(!sgsExist){
							throw new cwSysMessage("SGS002");
						}
						sgrp.delSgpRes(con,  modParam ,wizbini);
						
					}else if (modParam.getCmd().equalsIgnoreCase("set_cust_sgp_mem")) {
						StudyGroup sgrp =new StudyGroup();
						sgrp.addCustSgpMem(con, prof, modParam);
						
					}else if(modParam.getCmd().equalsIgnoreCase("set_auto_sgp_mem")){
						StudyGroup sgrp =new StudyGroup();
						
						// 检测用户组是否存在
						String sgm_ent_id_usg = modParam.getSgm_ent_id_usg();
						long[] usgIds = null;
						if(sgm_ent_id_usg != null && !"".equals(sgm_ent_id_usg) && sgm_ent_id_usg.length() > 0) {
							usgIds = cwUtils.splitToLong(sgm_ent_id_usg, "~");
						
							if (usgIds != null && usgIds.length > 0 && !dbRegUser.isUsgExists(con, usgIds)) {
								throw new cwSysMessage("USG011");
							}
						}
						
						sgrp.addUsgSgpMem(con, prof, modParam);
						if(modParam.isTa()){
							sgrp.addItmSgpMem(con, prof, modParam);
						}
						
					}else if (modParam.getCmd().equalsIgnoreCase("get_cust_sgp_mem")) {
						//手动添加成员页面
						StudyGroup sgrp =new StudyGroup();
						sgrp.getCustSgpMem(con, modParam, resultJson);	
						cwXSL.getGoldenManHtml(prof.label_lan, modParam.getGoldenman_param(), resultJson);
					}else if (modParam.getCmd().equalsIgnoreCase("get_auto_sgp_mem")) {	
						//自动添加成员页面
						cwXSL.getGoldenManHtml(prof.label_lan, modParam.getGoldenman_param(), resultJson);
						StudyGroup sgrp =new StudyGroup();
						sgrp.getAutoSgpMem(con,prof, modParam, resultJson);
					}else if(modParam.getCmd().equalsIgnoreCase("get_sgp_lst") ||modParam.getCmd().equalsIgnoreCase("get_sgp_lst_xml")){
					//	AcMessage acmsg = new AcMessage(con);			           
			            String result=null;
			            String nav_tree=null;
			        	cwTree tree = new cwTree();
			        	StudyGroup sgrp =new StudyGroup();
			            AccessControlWZB acWZB = new AccessControlWZB();
//			            if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)) {
//			            	throw new cwSysMessage("ACL002");
//			            } 
			            if (wizbini.cfgTcEnabled) {
			            	if (!ViewTrainingCenter.hasEffTc(con, prof.usr_ent_id)) {
			            		throw new cwSysMessage("TC011");
				            }
			                nav_tree = tree.genNavTrainingCenterTree(con, prof, false);      			                
			            	result = sgrp.getMyMgtSgpXml(con, prof, modParam, false);
			            } else {
			            	result =sgrp.getMyMgtSgpXml(con, prof, modParam, true);
			            }
			            AcPageVariant acPageVariant = new AcPageVariant(con);
			            acPageVariant.ent_id = prof.usr_ent_id;
			            acPageVariant.rol_ext_id = prof.current_role;
			            
			            String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(modParam.getStylesheet()));
			            if (nav_tree != null) {
			              	result += nav_tree;
			            }
			            resultXml = formatXML(result, metaXML, "studygroup", "");
						
					}else if(modParam.getCmd().equalsIgnoreCase("get_sgp_prep")||modParam.getCmd().equalsIgnoreCase("get_sgp_prep_xml")){
						 AcPageVariant acPageVariant = new AcPageVariant(con);
						 acPageVariant.ent_id = prof.usr_ent_id;
						 acPageVariant.rol_ext_id = prof.current_role;
						 String metaXML = acPageVariant.answerPageVariantAsXML((String[]) xslQuestions.get(modParam.getStylesheet()));
						 StudyGroup sgp= new StudyGroup();
						 String result="";						
						 if(modParam.getSgp_id() !=0){
							 result= sgp.getSgpInfoXml(con, prof, modParam);
						 }else{
							 result="<sgp/>";
						 }
						 if(wizbini.cfgTcEnabled) {
							   long tcr_id = ViewTrainingCenter.getDefaultTcId(con, prof);
				                DbTrainingCenter objTc = DbTrainingCenter.getInstance(con,tcr_id);
				                if(objTc != null && modParam.getSgp_id() ==0) {
				                	StringBuffer xmlBuf = new StringBuffer();
				                    xmlBuf.append("<default_training_center id =\"").append(objTc.getTcr_id()).append("\">");
				                    xmlBuf.append("<title>").append(cwUtils.esc4XML(objTc.getTcr_title())).append("</title>");
				                    xmlBuf.append("</default_training_center>");
				                    result += xmlBuf.toString();
				                }
						 }
						 resultXml = formatXML(result, metaXML, "studygroup", "");
						 
					}else if(modParam.getCmd().equalsIgnoreCase("ins_sgp")){
						StudyGroup sgp= new StudyGroup();
						StudyGroupDAO sgpDao= new StudyGroupDAO();
						AccessControlWZB acWZB = new AccessControlWZB();
//						if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)) {
//							throw new cwSysMessage("ACL002");
//			            } 

						if(sgpDao.isSgpTitleExist(con, modParam.getSgp_title())){
							throw new cwSysMessage("SGP005");
						}
						
						sgp.insSgp(con, prof, modParam);		
						redirectUrl = modParam.getUrl_success();


					}else if(modParam.getCmd().equalsIgnoreCase("upd_sgp")){
						StudyGroup sgp= new StudyGroup();
						StudyGroupDAO sgpDao= new StudyGroupDAO();
						AccessControlWZB acWZB = new AccessControlWZB();
//						if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)
//								||!sgpDao.isTaMgtSgpTc(con, modParam.getSgp_id(), prof)	) {
//							throw new cwSysMessage("ACL002");
//			            } 
						boolean sgpExist=sgpDao.isSgpIdExist(con, modParam.getSgp_id(), modParam.getSgp_upd_timestamp());
						if(!sgpExist){
							throw new cwSysMessage("SGP002");
						}
						sgp.updSgp(con, prof, modParam);
						redirectUrl = modParam.getUrl_success();
					}else if(modParam.getCmd().equalsIgnoreCase("del_sgp")){
						StudyGroup sgp= new StudyGroup();
						StudyGroupDAO sgpDao= new StudyGroupDAO();
						AccessControlWZB acWZB = new AccessControlWZB();
//						if (!acWZB.hasUserPrivilege(prof.current_role, AclFunction.FTN_TEMP)
//								||!sgpDao.isTaMgtSgpTc(con, modParam.getSgp_id(), prof)) {
//							throw new cwSysMessage("ACL002");
//			            } 
						boolean sgpExist=sgpDao.isSgpIdExist(con, modParam.getSgp_id(), modParam.getSgp_upd_timestamp());
						if(!sgpExist){
							throw new cwSysMessage("SGP003");
						}
						sgp.delSgp(con, prof, modParam.getSgp_id());
						redirectUrl = modParam.getUrl_success();
					}else {
						throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
					}
				}
		} catch (qdbException e) {
			throw new cwException(e.getMessage(), e);
		} catch (cwSysMessage e) {
			try {
                con.rollback();
                msgBox(ServletModule.MSG_STATUS, e, modParam.getUrl_failure(),out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
            }
		} catch (qdbErrMessage e) {
			throw new cwException(e.getMessage(), e);
		}
	}	
}
