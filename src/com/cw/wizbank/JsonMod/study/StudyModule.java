
package com.cw.wizbank.JsonMod.study;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.aeLearningPlan;
import com.cw.wizbank.ae.aeLearningSoln;
import com.cw.wizbank.config.organization.learningplan.LearningPlan;
import com.cw.wizbank.db.view.ViewSuperviseTargetEntity;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
 * 处理与学习、考试有关的信息
 * @author kimyu
 */
public class StudyModule extends ServletModule {
	
	StudyModuleParam modParam;
	public StudyModule() {
		super();
		modParam = new StudyModuleParam();
		param = modParam;
	}
	
	public void process() throws SQLException, IOException, cwException {
		
		try {
				if(this.prof == null || this.prof.usr_ent_id == 0) {	// 若还是未登录
					throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
				} else {// 若已经登陆

					// 查询与该学员相关的待审批课程
					if (modParam.getCmd().equalsIgnoreCase("get_pending_waiting_cos")) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                        }
						Study study = new Study();
						
						long usr_ent_id = modParam.getUsr_ent_id();
						if (usr_ent_id == 0) {
							usr_ent_id = this.prof.usr_ent_id;
						}
						
						// 获取该学员的待审批课程的信息
						String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
						Vector pendingCourseVector = study.getMyPendingCourses(con, usr_ent_id, prof.cur_lan, modParam, itmDir);
						
						// 等待审批课程栏Map
						HashMap pendingCourseMap = new HashMap();
						pendingCourseMap.put("total", new Integer(modParam.getTotal_rec()));// 加入总记录数
						pendingCourseMap.put("itm_lst", pendingCourseVector);						// 加入待审批的课程列表
						
						// 获取系统中的课程类型
						ArrayList groupType = new ArrayList();
						if ("COS".equalsIgnoreCase(modParam.getType())) {
							groupType.add(aeItemDummyType.ITEM_DUMMY_TYPE_COS);
						} else if ("EXAM".equalsIgnoreCase(modParam.getType())) {
							groupType.add(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM);
						}
						Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, groupType, prof.cur_lan);
						
						// 课程类型栏Map
						HashMap courseTypeMap = new HashMap();
						courseTypeMap.put("cos_type", courseTypeVector);
						
						resultJson.put("cos_pend", pendingCourseMap);
						resultJson.put("cos_type", courseTypeMap);
					}
					
					// 查询某位学员已报名正在学习中的课程
					// 查询接下来90天内应出席的课程(按课程期限列出所有后续90天学员可以学习的网上课程和离线课程)
					// 查询与学员相关的在30天内即将结束的课程
					else if (modParam.getCmd().equalsIgnoreCase("get_my_studying_cos")) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                        }
						Study study = new Study();
						
						long usr_ent_id = modParam.getUsr_ent_id();
						if (usr_ent_id == 0) {
							usr_ent_id = this.prof.usr_ent_id;
						}
						
						// 获取学员已报名正在学习中的课程信息
						String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
						Vector currentCourseVector = study.getCurrentCourses(con, usr_ent_id, prof.cur_lan, modParam, itmDir);
						
						HashMap currentCourseMap = new HashMap();
						currentCourseMap.put("total", new Integer(modParam.getTotal_rec()));// 加入总记录数
						currentCourseMap.put("itm_lst", currentCourseVector);						// 加入学习中的课程列表
						
//						// 获取接下来三个月应出席的课程(按课程期限列出所有后续三个月学员可以学习的网上课程和离线课程)
//						Vector attendCourseVector = study.getAttendCourses(con, usr_ent_id, prof.cur_lan, 90, studyModuleParam);
//						
//						HashMap attendCourseMap = new HashMap();
//						attendCourseMap.put("total", new Integer(studyModuleParam.getTotal_rec()));
//						attendCourseMap.put("itm_lst", attendCourseVector);
						
//						// 获取与学员相关的在30天内即将结束的课程
//						Vector shouldFinishedCourseVector = study.getShouldFinishedCourses(con, usr_ent_id, prof.cur_lan, 30, studyModuleParam);
//						
//						HashMap shouldFinishedCourseMap = new HashMap();
//						shouldFinishedCourseMap.put("total", new Integer(studyModuleParam.getTotal_rec()));
//						shouldFinishedCourseMap.put("itm_lst", shouldFinishedCourseVector);
						
//						// 获取系统中的课程类型
//						Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, null, prof.cur_lan);
						
						// 获取系统中的课程类型
						ArrayList groupType = new ArrayList();
						if ("COS".equalsIgnoreCase(modParam.getType())) {
							groupType.add(aeItemDummyType.ITEM_DUMMY_TYPE_COS);
						} else if ("EXAM".equalsIgnoreCase(modParam.getType())) {
							groupType.add(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM);
						}
						Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, groupType, prof.cur_lan);
						
						HashMap courseTypeMap = new HashMap();
						courseTypeMap.put("itm_type_lst", courseTypeVector);
						
						// 获取未报名推荐课程
//						if (modParam.getStart() == 0) {
						Vector unSignUpCommendedCosVector = study.getUnSignUpRecommendedCos(con, usr_ent_id);
//						}
						HashMap unSignUpCommendedCosMap = new HashMap();
						unSignUpCommendedCosMap.put("itm_lst", unSignUpCommendedCosVector);
						

						resultJson.put("cos_study", currentCourseMap);
//						resultJson.put("cos_att", attendCourseMap);
//						resultJson.put("cos_remind", shouldFinishedCourseMap);
						resultJson.put("srh_meta", courseTypeMap);
						resultJson.put("recommended_itm_lst", unSignUpCommendedCosVector);

					}
					
					// 查询与学员相关的所有课程(状态：可以是正在学习中的课程、或已完成的课程、或未完成的、或已放弃的课程)
					else if (modParam.getCmd().equalsIgnoreCase("get_all_my_cos")) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                        }
						Study study = new Study();
						
						long usr_ent_id = modParam.getUsr_ent_id();
						if (usr_ent_id == 0) {
							usr_ent_id = this.prof.usr_ent_id;
						}
						
						// 获取正在等待中、学习中、已结束3个状态下的课程信息
						String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
						Vector allMyCourseVector = study.getAllMyCourses(con, usr_ent_id, prof.cur_lan, modParam, itmDir);
						
						HashMap allMyCourseMap = new HashMap();
						allMyCourseMap.put("total", new Integer(modParam.getTotal_rec()));	// 加入总记录数
						allMyCourseMap.put("itm_lst", allMyCourseVector);							// 加入与用户相关的所有课程
						
//						// 获取系统中的课程类型
//						Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, null, prof.cur_lan);
						
						// 获取系统中的课程类型
						ArrayList groupType = new ArrayList();
						if ("COS".equalsIgnoreCase(modParam.getType())) {
							groupType.add(aeItemDummyType.ITEM_DUMMY_TYPE_COS);
						} else if ("EXAM".equalsIgnoreCase(modParam.getType())) {
							groupType.add(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM);
						}
						Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, groupType, prof.cur_lan);
						
						HashMap courseTypeMap = new HashMap();
						courseTypeMap.put("itm_type_lst", courseTypeVector);
						
						resultJson.put("my_cos", allMyCourseMap);
						resultJson.put("srh_meta", courseTypeMap);
					}
					
					// 查询学员已结束的课程列表(即查询学员已学完课程的历史记录)
					else if (modParam.getCmd().equalsIgnoreCase("get_my_hist_cos")) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                        }
						Study study = new Study();
						
						long usr_ent_id = modParam.getUsr_ent_id();
						if (usr_ent_id == 0) {
							usr_ent_id = this.prof.usr_ent_id;
						}
						
						// 获取与学员相关的已结束的课程
						String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
						Vector historyCourseVector = study.getMyHistoryCourses(con, usr_ent_id, prof.cur_lan, modParam, itmDir);
						
						HashMap historyCourseMap = new HashMap();
						historyCourseMap.put("total", new Integer(modParam.getTotal_rec()));	// 加入总记录数
						historyCourseMap.put("itm_lst", historyCourseVector);
						
//						// 获取系统中的课程类型
//						Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, null, prof.cur_lan);
						
						// 获取系统中的课程类型
						ArrayList groupType = new ArrayList();
						if ("COS".equalsIgnoreCase(modParam.getType())) {
							groupType.add(aeItemDummyType.ITEM_DUMMY_TYPE_COS);
						} else if ("EXAM".equalsIgnoreCase(modParam.getType())) {
							groupType.add(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM);
						} else if ("INTEGRATED".equalsIgnoreCase(modParam.getType())) {
							groupType.add(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM);
						}
						Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, groupType, prof.cur_lan);
						
						HashMap courseTypeMap = new HashMap();
						courseTypeMap.put("itm_type_lst", courseTypeVector);
						
						resultJson.put("cos_hist", historyCourseMap);
						resultJson.put("srh_meta", courseTypeMap);
					}
					
					// 搜索符合条件的课程
					else if (modParam.getCmd().equalsIgnoreCase("srh_my_cos")) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                        }
						Study study = new Study(); //searchCourseVector
						
						long usr_ent_id = modParam.getUsr_ent_id();
						if (usr_ent_id == 0) {
							usr_ent_id = this.prof.usr_ent_id;
						}
						
						// 获取与学员相关的符合条件的课程
						String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
						Vector searchCourseVector = study.searchCourses(con, usr_ent_id, prof.cur_lan, modParam, itmDir);
						
						HashMap searchCourseMap = new HashMap();
						searchCourseMap.put("total", new Integer(modParam.getTotal_rec()));	// 加入总记录数
						searchCourseMap.put("itm_lst", searchCourseVector);
						
						// 分辨是从我的全部课程页搜索还是从我的历史课程搜索(可选值："ALL","HIS")
						String srh_range = modParam.getSrh_range();
						if ("ALL".equalsIgnoreCase(srh_range)) {		// 若是从我的全部课程页面搜索
							resultJson.put("my_cos", searchCourseMap);
						} else if ("HIS".equalsIgnoreCase(srh_range)) {	// 若是从我的历史课程页面搜索
							resultJson.put("cos_hist", searchCourseMap);
						}
					}
					
					else if(modParam.getCmd().equalsIgnoreCase("get_my_cos"))	// 查询适合我的培训
					{
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				        }
						aeLearningSoln soln = new aeLearningSoln();
						
						dbRegUser user = new dbRegUser();
						user.usr_ent_id = modParam.getUsr_ent_id();
						if (user.usr_ent_id == 0 || user.usr_ent_id == prof.usr_ent_id) {
							user.usr_ent_id = prof.usr_ent_id;
							user.usr_display_bil = prof.usr_display_bil;
							user.usr_ste_ent_id = prof.root_ent_id;
						} else if(user.usr_ent_id != prof.usr_ent_id) {
							if (!new ViewSuperviseTargetEntity().isMyStaff(con, prof.usr_ent_id, user.usr_ent_id)) {
								throw new cwSysMessage("AELS03");
							}
							user.getByEntId(con);	// 由usr_ent_id获取此用户的一些个人信息
			            }
						
			            String xmlResult="";
			            
						String groupAncesterSql = dbEntityRelation.getAncestorListSql(user.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
						String gradeAncesterSql = dbEntityRelation.getAncestorListSql(user.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
						boolean isSelfInitiatedEnabled = ((LearningPlan)wizbini.cfgOrgLearningPlan.get(prof.root_id)).getSelfInitiated().isEnabled();
						String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getUrl();

						
						Vector corVC = aeLearningPlan.getLearningPlan(con, isSelfInitiatedEnabled,
								user.usr_ste_ent_id, user.usr_ent_id, user.usr_display_bil,
								groupAncesterSql, gradeAncesterSql, aeLearningPlan.RECOMMENDED,
								aeLearningPlan.JSON_FORMAT, xmlResult, modParam.isPending_plan(), null, null, null, prof.cur_lan, itmDir, null, null);
						
						resultJson.put("recommended_itm_lst", corVC);
					
					}
					
					else if(modParam.getCmd().equalsIgnoreCase("get_my_plan"))	// 查询我的培训计划
					{
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				         }
						aeLearningSoln soln = new aeLearningSoln();
				        
						dbRegUser user = new dbRegUser();
				        user.usr_ent_id = modParam.getUsr_ent_id();
						if (user.usr_ent_id == 0  || user.usr_ent_id == prof.usr_ent_id) {
							user.usr_ent_id = prof.usr_ent_id;
							user.usr_display_bil = prof.usr_display_bil;
							user.usr_ste_ent_id = prof.root_ent_id;
						} else if(user.usr_ent_id != prof.usr_ent_id) {
							if (!new ViewSuperviseTargetEntity().isMyStaff(con, prof.usr_ent_id, user.usr_ent_id)) {
								throw new cwSysMessage("AELS03");
							}
							user.getByEntId(con);	// 由usr_ent_id获取此用户的一些个人信息
			            }
				         
			            String xmlResult="";			            
						String groupAncesterSql = dbEntityRelation.getAncestorListSql(user.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
						String gradeAncesterSql = dbEntityRelation.getAncestorListSql(user.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
						boolean isSelfInitiatedEnabled = ((LearningPlan)wizbini.cfgOrgLearningPlan.get(prof.root_id)).getSelfInitiated().isEnabled();
						String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
						
//						Vector corVC=aeLearningPlan.getLearningPlan(con,isSelfInitiatedEnabled,prof,groupAncesterSql,gradeAncesterSql,aeLearningPlan.MYPLAN,aeLearningPlan.JSON_FORMAT,xmlResult,
//								studyModuleParam.isPending_plan(),studyModuleParam.getPlan_status(),studyModuleParam.getStart_time(),studyModuleParam.getEnd_time(), prof.cur_lan, itmDir);
						
						Vector corVC = aeLearningPlan.getLearningPlan(con,isSelfInitiatedEnabled,
								user.usr_ste_ent_id, user.usr_ent_id, user.usr_display_bil, 
								groupAncesterSql, gradeAncesterSql, aeLearningPlan.MYPLAN, aeLearningPlan.JSON_FORMAT, xmlResult,
								modParam.isPending_plan(), modParam.getPlan_status(), modParam.getStart_time(), modParam.getEnd_time(), prof.cur_lan, itmDir, null, null);

						resultJson.put("lrn_plan_lst", corVC);
						
						Vector VC = aeLearningPlan.getSimplePlan(con, prof, groupAncesterSql, gradeAncesterSql);
						
						if(VC != null && VC.size() > 0) {
							HashMap recommended = new HashMap();
							
							recommended.put("total", new Long(VC.size()));
							recommended.put("recommended_itm_lst", cwUtils.getSpecifiedNumOfVec(VC, modParam.getRec_num()));
							
							resultJson.put("recommended", recommended);
						} else {
							HashMap recommended = new HashMap();
							
							recommended.put("total", new Long(0));
							recommended.put("recommended_itm_lst", new Vector());
							
							resultJson.put("recommended", recommended);
						}
						
					}
					
					else if(modParam.getCmd().equalsIgnoreCase("ins_lrn_soln"))
					{
						
						long usr_ent_id=0;
						
							if(modParam.getUsr_ent_id()!=0)
							{
								aeLearningSoln.insSoln(con, prof, usr_ent_id,modParam.getItm_id() , 0);
							}else
							{
								aeLearningSoln.insSoln(con, prof, this.prof.usr_ent_id,modParam.getItm_id() , 0);
							}
							redirectUrl = modParam.getUrl_success();
						
					}
					else if(modParam.getCmd().equalsIgnoreCase("del_lrn_soln"))
					{
										
							aeLearningSoln.delSoln(con, modParam.getItm_id(),this.prof.usr_ent_id);
							redirectUrl = modParam.getUrl_success();
						
					}
					else if(modParam.getCmd().equalsIgnoreCase("save_plan_date"))
					{
						try
						{
							aeLearningSoln.updSingleSolnDate(con, prof.usr_ent_id,modParam.getItm_id(),modParam.getLsn_start_datetime(),modParam.getLsn_end_datetime());
							redirectUrl = modParam.getUrl_success();
						}
						catch(SQLException ex)
						{
							redirectUrl = modParam.getUrl_failure();

						}
					} else {
						throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
					}
				}
		} catch (qdbException e) {
			throw new cwException(e.getMessage(), e);
		} catch (cwSysMessage e) {
			try {
                con.rollback();
                msgBox(ServletModule.MSG_STATUS, e, modParam.getUrl_failure(),
                        out);
            } catch (SQLException sqle) {
                out.println("SQL error: " + sqle.getMessage());
            }
		}
	}
	
}

