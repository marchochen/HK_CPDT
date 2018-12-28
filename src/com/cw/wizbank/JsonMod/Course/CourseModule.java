package com.cw.wizbank.JsonMod.Course;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONArray;

import org.apache.commons.beanutils.BeanUtils;

import com.cw.wizbank.ServletModule;
import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.JsonMod.ErrorMsg;
import com.cw.wizbank.JsonMod.Course.bean.CatalogBean;
import com.cw.wizbank.JsonMod.Course.bean.CompetenceNode;
import com.cw.wizbank.JsonMod.Course.bean.CosCommentBean;
import com.cw.wizbank.JsonMod.Course.bean.NodeNavigationBar;
import com.cw.wizbank.JsonMod.study.Study;
import com.cw.wizbank.JsonMod.study.Utilities;
import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.studyGroup.StudyGroupDAO;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;

/**
 * 处理选课中心有关信息
 * @author kimyu
 */
public class CourseModule extends ServletModule {
	CourseModuleParam  modParam;
	
	public CourseModule() {
		super();
		modParam = new CourseModuleParam();
		param = modParam;
	}

	public void process() throws SQLException, IOException, cwException {
		
		try {
				if (this.prof == null || this.prof.usr_ent_id == 0)// 若还是未登录
				{
					throw new cwException(cwUtils.MESSAGE_SESSION_TIMEOUT);
				} else {// 若已经登陆

					// 进入选课中心"浏览目录"页面
					if ("get_lrn_center".equalsIgnoreCase(modParam.getCmd())
							&& (modParam.getSrh_key() == null || "".equals((modParam.getSrh_key())[0]))
							&& (modParam.getSke_id_lst() == null || "".equals(modParam.getSke_id_lst()))) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						Course course = new Course();
						course.getLrnCenter(con, modParam, prof, wizbini, resultJson);
					}
					
					// 在选课中心按能力分类筛选课程
					else if ("get_lrn_center".equalsIgnoreCase(modParam.getCmd())
							&& (modParam.getSrh_key() == null || "".equals((modParam.getSrh_key())[0]))
							&& (modParam.getSke_id_lst() != null && !"".equals(modParam.getSke_id_lst()))) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						Course course = new Course();
						course.filterCosBySkill(con, modParam, prof, wizbini, resultJson);
					}
					
					// 简单搜索(在选课中心"浏览目录"页面中进行搜索时，前台要求刷新整个页面)
					else if ("get_lrn_center".equalsIgnoreCase(modParam.getCmd())
							&& modParam.getSrh_key() != null && !"".equals((modParam.getSrh_key())[0])) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						Course course = new Course();
						course.simpleSearchInLrnCenter(con, modParam, prof, wizbini, resultJson);
					}
					
					// 浏览给定的培训中心下的所有顶层目录及其一级子目录(遍历时不包括子课程)
					// 或浏览顶层目录下的所有已发布的一级子目录为根结点的目录树(遍历时不包括子课程)
					else if ("get_tnd_detail_lst".equalsIgnoreCase(modParam.getCmd())) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						Course course = new Course();
						course.browseCatalog(con, modParam, prof, wizbini, resultJson);
					}
					
					// 在选课中心"浏览目录"页面的"缩小搜索范围"栏搜索符合条件的课程
					// 或高级搜索
					else if ("srh_itm".equalsIgnoreCase(modParam.getCmd()) && modParam.isNot_planed() == false) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						Course course = new Course();
						course.searchCoursesInLrnCenter(con, modParam, sess, prof, wizbini, resultJson);
					}
					
					// 在"我的培训计划"页面搜索，此时要求过滤掉已添加到计划的培训
					else if ("srh_itm".equalsIgnoreCase(modParam.getCmd()) && modParam.isNot_planed() == true) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						Course course = new Course();
						course.searchInLrnPlan(con, modParam, prof, wizbini, resultJson);
					}
					
//					// 从高级搜索中搜索符合条件的课程
//					else if ("srh_itm".equalsIgnoreCase(courseModuleParam.getCmd()) && "adv_itm_srh".equalsIgnoreCase(courseModuleParam.getFrom())) {
//						
//					}
									
					else if (modParam.getCmd().equalsIgnoreCase("get_course_rank_lst")) {
						this.doProcess(modParam.getActivetab(), resultJson, modParam,wizbini);
					}
					else if(modParam.getCmd().equalsIgnoreCase("get_cos_content")){
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
				              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				          }
						Course cos =new Course();
						cos.getCosContent(con, prof, modParam, resultJson, defJsonConfig,wizbini);
						JsonHelper.disableEsc4Json(com.cw.wizbank.JsonMod.Course.bean.ModBean.class, "title", defJsonConfig);
						JsonHelper.disableEsc4Json("app_note", defJsonConfig);
					}
					else if(modParam.getCmd().equalsIgnoreCase("get_cos_lessons")){
						Course cos =new Course();
						cos.getLessons(con, prof, modParam, resultJson);
					}
					else if(modParam.getCmd().equalsIgnoreCase("get_cos_sgp_res")){
						StudyGroupDAO sgpDao =new StudyGroupDAO();
						Vector resVc=sgpDao.getStudyGroupResLst(con,  modParam.getSgp_id());
						resultJson.put("res_lst", resVc);
						hasMetaAndSkin=false;
					}
					else if(modParam.getCmd().equalsIgnoreCase("get_cos_sgp_forum")){
						StudyGroupDAO sgpDao =new StudyGroupDAO();
						Vector topicVc=sgpDao.getSgpForumTopic(con, modParam.getSgp_id(),null);
						resultJson.put("topicLst", topicVc);
						hasMetaAndSkin=false;
					}

					else if(modParam.getCmd().equalsIgnoreCase("upd_cos_note"))
					{
						Course cos =new Course();
						cos.updCosNote(con, modParam.getTkh_id(), prof.usr_ent_id, modParam.getItm_id(), modParam.getNote());
						hasMetaAndSkin=false;
					}
					else if(modParam.getCmd().equalsIgnoreCase("add_cos_comment")){
						Course cos =new Course();																							
						if(cos.isExistComment(con, modParam.getItm_id(), prof.usr_ent_id, modParam.getTkh_id())){
							return;							
						}else{
							Timestamp cur_time = cwSQL.getTime(con);
							//cos.addCosComment(con, modParam.getItm_id(), prof, modParam.getTkh_id(),
							//		modParam.getScore(), modParam.getComment(), cur_time);							
							Vector levelMsg=cos.getCommentLevelDisplayMsg();
							resultJson.put("comment_level", levelMsg);
							CosCommentBean cosCom=cos.getCosCommentInfo(con, modParam.getItm_id(), prof.usr_ent_id, modParam.getTkh_id());
							resultJson.put("course_comment", cosCom);
							resultJson.put("success", new Boolean(true));
						}
					}
					else if(modParam.getCmd().equalsIgnoreCase("get_cos_comment_info")){
						Course cos =new Course();
						Vector comDetail=cos.getCommentDetail(con);
						resultJson.put("comment_level", comDetail);
					} else if (modParam.getCmd().equalsIgnoreCase("get_itm")) {
					    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
                            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
                        }
					    // access control
					    long itmId = modParam.getItm_id();
					    boolean isUsrLrnRole = dbUtils.isUserLrnRole(con, prof.usr_ent_id, prof.current_role);
					    boolean hasTargetedLrnPrivilege = aeItem.hasTargetedLrnPrivilege(con, prof.root_ent_id, prof.usr_ent_id, itmId);
	                    if(isUsrLrnRole && 
	                            (!hasTargetedLrnPrivilege && !aeItem.isItemShared(con, itmId)
	                                    && !aeApplication.hasEffectiveAppId(con, itmId, prof.usr_ent_id))){
	                        cwSysMessage sms = new cwSysMessage("AEIT28");
	                        msgBox(MSG_STATUS, sms, modParam.getUrl_failure(), out);
	                        return;
	                    }
	                    AcItem acitm = new AcItem(con);
	            		aeItem itm = new aeItem();
	            		itm.itm_id = itmId;
	            		itm.getItem(con);
	            		boolean checkStatus = (!acitm.hasOffReadPrivilege(prof.usr_ent_id, prof.current_role));
	        			if(!acitm.hasReadPrivilege(itm, prof.usr_ent_id, prof.current_role, checkStatus)){
	        				throw new cwSysMessage("AEIT27");
	        			}
					    Course cos = new Course();
					    cos.getCourseInfo(con, wizbini, xslQuestions, prof, sess, modParam, resultJson);
					    JsonHelper.disableEsc4Json("course_detail", defJsonConfig);
					} else if(modParam.getCmd().equalsIgnoreCase("adv_srh_itm_prep")) {	// 进入高级搜索页面
						Course course = new Course();
					    AccessControlWZB acl = new AccessControlWZB();

					    //for learner
					    if(AccessControlWZB.hasRole(con, prof.usr_ent_id, AccessControlWZB.ROL_EXT_ID_NLRN)) {
					    	
					    	// 获取系统中的课程类型
//							Vector courseTypeVector = (new Study()).getCourseTypes(con);	// Wizbank5.0以前版本
							Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, null, prof.cur_lan);
							
							HashMap courseTypeMap = new HashMap();
							courseTypeMap.put("itm_type_lst", courseTypeVector);
							
							// 加入课程类型信息
							resultJson.put("srh_meta", courseTypeMap);
					    	
//					    	// 生成能力树(动态树)
//					    	cwTree tree = new cwTree();
//					    	tree.appendUsrSkills = true;	// 追加学员岗位上的能力分类
//					    	tree.isJsonTree = true;
//					    	tree.node_id = 0;
//					    	tree.tree_type = cwTree.COMPETENCE_PROFILE_AND_SKILL;
//					    	String competence_tree = tree.treeXML(con, prof);
					    	
					    	// 生成能力树(整个能力树)
					    	Vector competence_tree = course.buildSkillSetCat(con, prof.usr_ent_id, prof.cur_lan);
					    	
					        //get training center list to which user belong.(生成培训中心目录树)
					        ViewTrainingCenter viewTCR = new ViewTrainingCenter();
					        List tcrList = viewTCR.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
					        Vector treeBeanVec = Course.getTreeBeanByTcrLst(tcrList);
					        
//					        resultJson.put("competence_tree", JSONArray.fromObject(competence_tree));
					        resultJson.put("competence_tree", competence_tree);	// 装入能力树
					        resultJson.put("tc_cat_lst", treeBeanVec);			// 装入目录树
			            }
					} else if(modParam.getCmd().equalsIgnoreCase("get_tc_catalog_tree")) {
                        cwTree tree = new cwTree();
                        tree.isJsonTree = true;
                        tree.node_id = modParam.getNode_id();
                        tree.tree_type = cwTree.TC_CATALOG;
                        tree.node_type = modParam.getNode_type();

//                        String tcrList = tree.tcItemCatalog2TreeXML(con, prof, null);
//                        JSONArray array = JSONArray.fromObject(tcrList);
//                        out.print(array.toString());
//                        out.println(tcrList);

                        String nodeList = null;
                        if(cwTree.NODE_TYPE_CATALOG.equals(tree.node_type)) {
                        	nodeList = tree.tcItemCatalog2TreeXML(con, prof, null,0);
                        } else {
                        	long [] entIdList = Course.usrGroups(prof.usr_ent_id, prof.usrGroups);
                        	Vector catVec = aeCatalog.catalogListAsVector(con, entIdList, true, null, tree.node_id, null, null, null,  wizbini, prof);
                        	Vector nodeVec = Course.getTreeBeanByCatVec(catVec);
                        	nodeList = JSONArray.fromObject(nodeVec).toString();
                        }
                        JSONArray array = JSONArray.fromObject(nodeList);
                        out.print(array.toString());
					} else {
						throw new cwException(cwUtils.MESSAGE_NO_RECOGNIZABLE_CMD + modParam.getCmd());
					}
				}
		} catch (qdbException e) {
			throw new cwException(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			throw new cwException(e.getMessage(),e);
		} catch (InvocationTargetException e) {
			throw new cwException(e.getMessage(),e);
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

	private void doProcess(String cmdArgument, HashMap resultMap, BaseParam param ,WizbiniLoader wizbini )
		throws SQLException, IOException, cwException, cwSysMessage {
		
		CourseModuleParam courseModuleParam = (CourseModuleParam)param;
		Course course = new Course();
		Vector coursesVc;

		try {
			//热门课程排行
			if (cmdArgument.equalsIgnoreCase("hot_course")) {
			    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
		              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		          }
				coursesVc = course.getHotCourse(con, courseModuleParam,this.prof,wizbini);
				Map courseMap = new HashMap();
				courseMap.put("hot_course_lst", coursesVc);
				courseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
				resultJson.put("hot_course", courseMap);
				return;
			}
			//课程评分排名 
			if (cmdArgument.equalsIgnoreCase("comments_course")) {
			    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
		              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		          }
				coursesVc = course.getSorCourses(con, courseModuleParam,this.prof, wizbini);
				Map courseMap = new HashMap();
				courseMap.put("comments_course_lst", coursesVc);
				courseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
				resultJson.put("comments_course", courseMap);
				return;
			}
            //最新课程
			if (cmdArgument.equalsIgnoreCase("new_course")) {
			    if (con != null && con.getTransactionIsolation() != Connection.TRANSACTION_READ_UNCOMMITTED && cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())) {
		              con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		          }
				coursesVc = course.getLastedCourses(con, courseModuleParam, this.prof , wizbini);
				Map courseMap = new HashMap();
				courseMap.put("new_course_lst", coursesVc);
				courseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
				resultJson.put("new_course", courseMap);
				return;

			}

		} catch (qdbException qdbex) {
			qdbex.printStackTrace();
		}

	}
}
