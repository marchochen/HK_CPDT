package com.cw.wizbank.JsonMod.Course;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import net.sf.json.JsonConfig;

import org.apache.commons.beanutils.BeanUtils;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.JsonMod.Ann.Ann;
import com.cw.wizbank.JsonMod.Course.bean.AttBean;
import com.cw.wizbank.JsonMod.Course.bean.CCRBean;
import com.cw.wizbank.JsonMod.Course.bean.CatalogBean;
import com.cw.wizbank.JsonMod.Course.bean.CommentDetailBean;
import com.cw.wizbank.JsonMod.Course.bean.CommentLevelBean;
import com.cw.wizbank.JsonMod.Course.bean.CompetenceNode;
import com.cw.wizbank.JsonMod.Course.bean.CompletionCriteriaBean;
import com.cw.wizbank.JsonMod.Course.bean.CosCommentBean;
import com.cw.wizbank.JsonMod.Course.bean.CourseBean;
import com.cw.wizbank.JsonMod.Course.bean.CourseInfoBean;
import com.cw.wizbank.JsonMod.Course.bean.LessionBean;
import com.cw.wizbank.JsonMod.Course.bean.MessageBean;
import com.cw.wizbank.JsonMod.Course.bean.NodeBean;
import com.cw.wizbank.JsonMod.Course.bean.NodeNavigationBar;
import com.cw.wizbank.JsonMod.Course.bean.OfflineAndCompletionBean;
import com.cw.wizbank.JsonMod.Course.bean.OfflineMeasureBean;
import com.cw.wizbank.JsonMod.Course.bean.ScoreBean;
import com.cw.wizbank.JsonMod.Course.bean.ScoreMeasureBean;
import com.cw.wizbank.JsonMod.Course.bean.StudentMeasureBean;
import com.cw.wizbank.JsonMod.Course.bean.TeacherBean;
import com.cw.wizbank.JsonMod.Course.bean.TrainingCenterBean;
import com.cw.wizbank.JsonMod.commonBean.JsonTreeBean;
import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.JsonMod.study.Study;
import com.cw.wizbank.accesscontrol.AcItem;
import com.cw.wizbank.accesscontrol.AcPageVariant;
import com.cw.wizbank.accesscontrol.AcTreeNode;
import com.cw.wizbank.ae.aeAction;
import com.cw.wizbank.ae.aeApplication;
import com.cw.wizbank.ae.aeAttendanceStatus;
import com.cw.wizbank.ae.aeCatalog;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.ae.aeItemRelation;
import com.cw.wizbank.ae.aeLearningSoln;
import com.cw.wizbank.ae.aeReqParam;
import com.cw.wizbank.ae.aeTreeNode;
import com.cw.wizbank.ae.aeTreeNodeRelation;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.course.loadTargetLrnCacheAndCourseEnrollScheduler;
import com.cw.wizbank.course.view.ViewAeTreeNodeDAO;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbResource;
import com.cw.wizbank.qdb.dbSSOLink;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.studyGroup.StudyGroupDAO;
import com.cw.wizbank.trainingcenter.TrainingCenter;
import com.cw.wizbank.tree.cwTree;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.util.cwXSL;
//import com.cw.wizbank.JsonMod.Course.bean.Comments;
//import com.cw.wizbank.JsonMod.Course.bean.Valuation;
//import com.cw.wizbank.JsonMod.Course.bean.ValuationLog;
import com.cwn.wizbank.utils.CommonLog;


/**
 * 处理选课模块、课程内容模块
 * @author kimyu
 */
public class Course {
    private long countId = 0;

    public static final String ITM_TYPE_SELFSTUDY = "SELFSTUDY";
    
    public static final String ITM_TYPE_VIDEO = "VIDEO";

	public static int[] COMMENT_LEVEL = {1,2,3,4,5};
	public static int[] COMMENT_LABEL={101,102,103,104,105};

	public static final String SEPARATOR_GREATER_THAN_SIGN = ">";

	public static final String NODE_TYPE_SKP="SKP";	// 以岗位分类标识
	public static final String NODE_TYPE_CSP="CSP";	// 以复合能力分类标识

	public static final String SKE_TYPE_SKP = "SKP";// 岗位标识
	public static final String SKE_TYPE_COMPOSITE_SKILL = "COMPOSITE_SKILL";	// 能力标识

	public static final String LAB_USR_ALL_SKILLS = "lab_usr_all_skills";

	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

	public long getNextId() {
		return ++countId;
	}

	public static final String SQL_GET_TC_COURSE_BY_USER = "select distinct itm_id From tcTrainingCenterTargetEntity, aeItem, EntityRelation" +
			" where tce_tcr_id = itm_tcr_id" +
			" and tce_ent_id = ern_ancestor_ent_id" +
			" and ern_type = 'USR_PARENT_USG'" +
			" and ern_child_ent_id = ?";

	public static final String SQL_GET_TC_COURSE_BY_USER_EXISTS = " AND EXISTS (SELECT 'X'  From tcTrainingCenterTargetEntity, aeItem, EntityRelation" +
	" WHERE tce_tcr_id = itm_tcr_id" +
	" AND tce_ent_id = ern_ancestor_ent_id" +
	" AND ern_type = 'USR_PARENT_USG'" +
	" AND ern_child_ent_id = ?" +
	" AND tab.itm_id = itm_id )";

	/**
	 * 选课中心浏览目录
	 * @param con
	 * @param modParam
	 * @param prof
	 * @param wizbini
	 * @param resultJson
	 * @throws SQLException
	 * @throws cwSysMessage
	 */
	public void getLrnCenter(Connection con, CourseModuleParam courseModuleParam, loginProfile prof,
			WizbiniLoader wizbini, HashMap resultJson) throws SQLException, cwSysMessage {
		long tnd_id = courseModuleParam.getTnd_id();	// 从客户端获取当前目录ID

		if(tnd_id > 0) {
			AcTreeNode actnd = new AcTreeNode(con);
			long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
			aeTreeNode tnd = new aeTreeNode();
			tnd.tnd_id = tnd_id;
			tnd.tnd_cat_id = tnd.getCatalogId(con);
			if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role, tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
				throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
			}
        	if (!aeCatalog.canReadByLrn(con, tnd.tnd_cat_id, prof.root_ent_id, prof.usr_ent_id,  wizbini)) {
                throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
        	}
		}

		// 获取该用户相关的培训中心的信息
		Vector myTrainingCenterVector = this.getMyTrainingCenter(con, prof.usr_ent_id, courseModuleParam);

		long tcr_id = courseModuleParam.getTcr_id();// 从客户端获取当前培训中心ID

		// 类别栏Map
		HashMap tndMap = new HashMap();

		Vector catalogVector = null;		// 当前培训中心所有顶层目录的集合(或某一目录下所有已发布的一级子目录的集合)
		Vector myViewCourseVector = null;	// 当前培训中心所有顶层目录(包括子目录)下的用户可见课程的集合(或某一目录(包括子目录)下用户可见课程集合)

		// 课程列表栏Map
		HashMap myViewCourseMap = new HashMap();

		// 注：当tnd_id的值为0时，在类别栏中放入当前培训中心下的所有顶层目录；当为其他值时，在类别栏中放入当前目录的所有已发布的一级子目录
		if (tnd_id == 0) {	// 当用户点击选课中心的"浏览目录"(或在选课中心下选中某个"培训中心")时，在类别栏默认显示当前培训中心下的所有顶层目录

			// 第一次访问选课中心(即tcr_id的初始值为0)，此时当前培训中心默认为用户最近培训中心
			if (tcr_id == 0) {
				tcr_id = this.getMyNearTrainingCenter(con, prof.usr_ent_id, courseModuleParam);// 查找用户最近培训中心所对应的tcr_id值
			}

			// 在类别栏中构造一个当前目录的父目录(可以设置该父目录的tnd_id值为0、tnd_title值为""，以便于前台统一输出)
			CatalogBean parentCatBean = new CatalogBean();
			parentCatBean.setTnd_id(0);
			parentCatBean.setTnd_title("");
			parentCatBean.setTcr_id(tcr_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入当前培训中心下的所有顶层目录
			catalogVector = this.getTopCatalogBeans(con, prof.usr_ent_id, tcr_id, false, false, 0);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 在课程列表栏中放入学员在当前培训中心下的所有顶层目录(包括子目录)中的所有可见课程
			String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
			myViewCourseVector = this.getMyViewCoursesByPagination(con, prof.usr_ent_id, tcr_id, prof.cur_lan, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);
		} else {	// 当用户点击进入某个目录时，在类别栏放入已发布的一级目录
			// 在类别栏中构造当前目录的父目录
			CatalogBean parentCatBean = this.getParentCatBean(con, tnd_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中生成导航条
			NodeNavigationBar nodeNavBar = this.buildNavigationBar(con, tnd_id);
			resultJson.put("nav_link", nodeNavBar);

			// 在"类别栏"中放入当前目录的所有已发布的一级子目录
			catalogVector = this.getFirstFloorCatalogs(con, prof.usr_ent_id, tnd_id, false, false);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 在"课程列表栏"中放入待查看目录(包括子目录)中的所有可见课程
			String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
			myViewCourseVector = this.getViewCosOfOneCatByPagination(con, prof.usr_ent_id, tnd_id, courseModuleParam, prof.cur_lan, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);

			//单点登录时，只传入了tnd_id，需要获取对应的tcr_id
			if(tcr_id == 0) {
				tcr_id = aeCatalog.getTcrIdByTndId(con, tnd_id);
			}
		}

		// 获取系统中的课程类型
//		Vector courseTypeVector = (new Study()).getCourseTypes(con);	// Wizbank5.0以前版本
		Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, null, prof.cur_lan);

		// 搜索栏Map
		HashMap courseTypeMap = new HashMap();
		courseTypeMap.put("itm_type_lst", courseTypeVector);	// 在搜索栏加入课程类型信息
		resultJson.put("srh_meta", courseTypeMap);				// 装入搜索栏

		// 能力分类栏
		Vector buildSkillBaseVector = this.buildMySkillBaseGadget(con, prof.usr_ent_id, prof.cur_lan);
		resultJson.put("competence_list", buildSkillBaseVector);// 装入能力分类栏

		resultJson.put("tcr_lst", myTrainingCenterVector);		// 装入所有培训中心ID
		resultJson.put("tnd", tndMap);							// 装入类别栏
		resultJson.put("items", myViewCourseMap);				// 装入课程列表栏
		resultJson.put("cur_tcr", this.getTCById(con, tcr_id));	// 装入当前培训中心ID
	}

	/**
	 * 选课中心按能力分类筛选课程
	 * @param con
	 * @param courseModuleParam
	 * @param prof
	 * @param wizbini
	 * @param resultJson
	 * @throws SQLException
	 * @throws cwException 
	 */
	public void filterCosBySkill(Connection con, CourseModuleParam courseModuleParam, loginProfile prof,
			WizbiniLoader wizbini, HashMap resultJson) throws SQLException, cwException {

		long tcr_id = courseModuleParam.getTcr_id();// 从客户端获取当前培训中心ID

		if (tcr_id == 0) {	// 若是从学员首页按能力访问，此时当前培训中心默认为用户最近培训中心
			tcr_id = this.getMyNearTrainingCenter(con, prof.usr_ent_id, courseModuleParam);// 查找用户最近培训中心所对应的tcr_id值

			// 获取该用户相关的培训中心的信息
			Vector myTrainingCenterVector = this.getMyTrainingCenter(con, prof.usr_ent_id, courseModuleParam);
			resultJson.put("tcr_lst", myTrainingCenterVector);		// 装入所有培训中心ID

			Vector buildSkillBaseVector = this.buildMySkillBaseGadget(con, prof.usr_ent_id, prof.cur_lan);
			resultJson.put("competence_list", buildSkillBaseVector);	// 装入能力分类栏
		}

		// 从客户端获取按能力分类的参数
		String ske_id_lst = courseModuleParam.getSke_id_lst();	// 形如""、"0"、"1,2,3,..."或"1"

		// 将搜索条件装入conditionMap中
		HashMap conditionMap = new HashMap();
		conditionMap.put("tcr_id", new Long(tcr_id));
		long tnd_id = courseModuleParam.getTnd_id();
		conditionMap.put("tnd_id_lst", new long[]{tnd_id});
		conditionMap.put("ske_id_lst", ske_id_lst);
		Vector tcrIdVec = null;
		if(tcr_id > 0) {
			tcrIdVec = new Vector();
			tcrIdVec.addElement(new Long(tcr_id));
		}
		conditionMap.put("tcr_id_lst", tcrIdVec);

		// 类别栏Map
		HashMap tndMap = new HashMap();

		Vector catalogVector = null;		// 当前培训中心所有顶层目录的集合(或某一目录下所有已发布的一级子目录的集合)，注：顶层目录中必须存在符合搜索条件的用户可见课程
		Vector myViewCourseVector = null;	// 当前培训中心所有顶层目录(包括子目录)下的用户可见课程的集合(或某一目录(包括子目录)下用户可见课程集合)

		// 课程列表栏Map
		HashMap myViewCourseMap = new HashMap();

		String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();

		// 注：当tnd_id的值为0时，在类别栏中放入当前培训中心下的所有顶层目录(顶层目录中必须存在符合能力筛选条件的用户可见课程)；当为其他值时，在类别栏中放入当前目录的所有已发布的一级子目录(一级子目录中必须存在符合能力筛选条件的用户可见课程)
		if (tcr_id != 0 && tnd_id == 0) {	// 当用户第一次点击选课中心的"能力分类"时，在类别栏显示当前培训中心下的所有顶层目录(顶层目录中必须存在符合能力筛选条件的用户可见课程)
			// 在类别栏中构造一个所有顶层目录的父目录(由于所有顶层目录的父目录不存在，可以设置该父目录的tnd_id值为0、tnd_title值为""，以便于前台统一输出)
			CatalogBean parentCatBean = new CatalogBean();
			parentCatBean.setTnd_id(0);
			parentCatBean.setTnd_title("");
			parentCatBean.setTcr_id(tcr_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入当前培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)
			catalogVector = this.getTopCatBeansForSrhCondition(con, wizbini, prof.usr_ent_id, false, false, conditionMap, false, false);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 在课程列表栏中放入学员在当前培训中心下的所有符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan,conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);

			// 能力分类栏
			Vector buildSkillBaseVector = this.buildMySkillBaseGadget(con, prof.usr_ent_id, prof.cur_lan);
			resultJson.put("competence_list", buildSkillBaseVector);	// 装入能力分类栏

			// 获取系统中的课程类型
			Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, null, prof.cur_lan);

			// 搜索栏Map
			HashMap courseTypeMap = new HashMap();
			courseTypeMap.put("itm_type_lst", courseTypeVector);	// 在搜索栏加入课程类型信息
			resultJson.put("srh_meta", courseTypeMap);				// 装入搜索栏
		} else if (tcr_id != 0 && tnd_id != 0) {
			// 在类别栏中构造所有一级目录的父目录
			CatalogBean parentCatBean = this.getParentCatBean(con, tnd_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入待查看的目录下的所有已发布的一级子目录(一级子目录中必须存在符合搜索条件的用户可见课程)
			catalogVector = this.getFirstFloorCatBeansForSrhCondition(con, prof.usr_ent_id, tnd_id, tcr_id, false, false, conditionMap, false);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 当用户进入某个目录时，在类别栏中生成导航条
			NodeNavigationBar nodeNavBar = this.buildNavigationBar(con, tnd_id);
			resultJson.put("nav_link", nodeNavBar);

			// 在课程列表栏中放入学员在当前培训中心下的所有符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);
		}

		resultJson.put("srh_criteria", conditionMap);			// 装入条件
		resultJson.put("tnd", tndMap);							// 装入类别栏
		resultJson.put("cur_tcr", this.getTCById(con, tcr_id));	// 装入当前培训中心ID
		resultJson.put("items", myViewCourseMap);				// 装入课程列表栏
	}

	/**
	 * 简单搜索(在选课中心"浏览目录"页面中进行搜索，同时刷新整个页面)
	 * @param con
	 * @param courseModuleParam
	 * @param prof
	 * @param wizbini
	 * @param resultJson
	 * @throws SQLException
	 * @throws cwException 
	 */
	public void simpleSearchInLrnCenter(Connection con, CourseModuleParam courseModuleParam, loginProfile prof,
			WizbiniLoader wizbini, HashMap resultJson) throws SQLException, cwException {

		// 获取该用户相关的培训中心的信息
		Vector myTrainingCenterVector = this.getMyTrainingCenter(con, prof.usr_ent_id, courseModuleParam);

		long tcr_id = courseModuleParam.getTcr_id();		// 从客户端获取当前培训中心ID

		// 从客户端获取搜索参数
		String[] srh_key = courseModuleParam.getSrh_key();	// 关键词

		long tnd_id = courseModuleParam.getTnd_id();		// 从客户端获取当前目录ID

		// 将搜索条件装入conditionMap中
		HashMap conditionMap = new HashMap();
		conditionMap.put("tcr_id", new Long(tcr_id));
		conditionMap.put("srh_key", srh_key);
		conditionMap.put("srh_key_type", "TITLE");			// 非全文搜索

		// 培训中心ID
		Vector tcrIdVec = null;
		if(tcr_id > 0) {
			tcrIdVec = new Vector();
			tcrIdVec.addElement(new Long(tcr_id));
		}
		conditionMap.put("tcr_id_lst", tcrIdVec);

		// 目录ID
		long[] tnd_id_lst = null;
		if(tnd_id > 0) {
			tnd_id_lst = new long[]{tnd_id};
		}
		conditionMap.put("tnd_id_lst", tnd_id_lst);

		// 类别栏Map
		HashMap tndMap = new HashMap();

		Vector catalogVector = null;		// 当前培训中心所有顶层目录的集合(或某一目录下所有已发布的一级子目录的集合)，注：顶层目录中必须存在符合搜索条件的用户可见课程
		Vector myViewCourseVector = null;	// 当前培训中心所有顶层目录(包括子目录)下的用户可见课程的集合(或某一目录(包括子目录)下用户可见课程集合)

		// 课程列表栏Map
		HashMap myViewCourseMap = new HashMap();

		String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();

		// 注：当tnd_id_lst的值为0时，在类别栏中放入当前培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)；当为其他值时，在类别栏中放入当前目录的所有已发布的一级子目录(一级子目录中必须存在符合搜索条件的用户可见课程)
		if (tcr_id != 0 && tnd_id == 0) {	// 当用户第一次在选课中心"浏览目录"页面中进行搜索时，在类别栏显示当前培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)
			// 在类别栏中构造一个当前目录的父目录(可以设置该父目录的tnd_id值为0、tnd_title值为""，以便于前台统一输出)
			CatalogBean parentCatBean = new CatalogBean();
			parentCatBean.setTnd_id(0);
			parentCatBean.setTnd_title("");
			parentCatBean.setTcr_id(tcr_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入当前培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)
			catalogVector = this.getTopCatBeansForSrhCondition(con, wizbini, prof.usr_ent_id, false, false, conditionMap, false, false);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 在课程列表栏中放入学员在当前培训中心下的所有符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);

			resultJson.put("tcr_lst", myTrainingCenterVector);	// 装入所有培训中心ID

			// 获取系统中的课程类型
			Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, null, prof.cur_lan);

			// 搜索栏Map
			HashMap courseTypeMap = new HashMap();
			// 在搜索栏加入课程类型信息
			courseTypeMap.put("itm_type_lst", courseTypeVector);
			resultJson.put("srh_meta", courseTypeMap);			// 装入搜索栏

			// 能力分类栏
			Vector buildSkillBaseVector = this.buildMySkillBaseGadget(con, prof.usr_ent_id, prof.cur_lan);
			resultJson.put("competence_list", buildSkillBaseVector);	// 装入能力分类栏
		} else if (tcr_id != 0 && tnd_id != 0) {	// 进行搜索后，当再次点击进入某个目录时，在类别栏放入已发布的一级目录
			// 在类别栏中构造当前目录的父目录
			CatalogBean parentCatBean = this.getParentCatBean(con, tnd_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入待查看的目录下的所有已发布的一级子目录(一级子目录中必须存在符合搜索条件的用户可见课程)
			catalogVector = this.getFirstFloorCatBeansForSrhCondition(con, prof.usr_ent_id, tnd_id, tcr_id, false, false, conditionMap, false);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 当用户进入某个目录时，在类别栏中生成导航条
			NodeNavigationBar nodeNavBar = this.buildNavigationBar(con, tnd_id);
			resultJson.put("nav_link", nodeNavBar);

			// 在课程列表栏中放入学员在当前培训中心下的所有符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);
		}

		resultJson.put("srh_criteria", conditionMap);				// 装入条件
		resultJson.put("tnd", tndMap);								// 装入类别栏
		resultJson.put("cur_tcr", this.getTCById(con, tcr_id));		// 装入当前培训中心ID
		resultJson.put("items", myViewCourseMap);					// 装入课程列表栏
	}

	/**
	 * 浏览给定的培训中心下的所有顶层目录及其一级子目录(遍历时不包括子课程)
 	 * 或浏览顶层目录下的所有已发布的一级子目录为根结点的目录树(遍历时不包括子课程)
	 * @param con
	 * @param courseModuleParam
	 * @param prof
	 * @param wizbini
	 * @param resultJson
	 * @throws SQLException
	 */
	public void browseCatalog(Connection con, CourseModuleParam courseModuleParam, loginProfile prof,
			WizbiniLoader wizbini, HashMap resultJson) throws SQLException {

		// 获取该用户相关的培训中心的信息
		Vector myTrainingCenterVector = this.getMyTrainingCenter(con, prof.usr_ent_id, courseModuleParam);

		long tcr_id = courseModuleParam.getTcr_id();		// 从客户端获取当前培训中心ID

		long tnd_id = courseModuleParam.getTnd_id();		// 从客户端获取该顶层目录的ID(对应aeTreeNode表的tnd_id)
		int floor_num = 2;									// 要显示的目录树层数(当floor_num的值为0时，则遍历所有层次的子目录)

		// 注：当tnd_id的值为0时，默认为输出某个培训中心下所有顶层节点目录树；当为其他值时，输出该节点目录树
		if (tnd_id == 0) {	// 当用户点击选课中心"详细分类"时，浏览给定的培训中心下的所有顶层目录及其一级子目录(遍历时不包括子课程)
			resultJson.put("tnd_lst", this.getTopCatalogGroupingVector(con, prof.usr_ent_id, tcr_id, floor_num));
		} else {
			CatalogBean topCatBean = this.getCatalogById(con, prof.usr_ent_id, false, tnd_id, true);	// 根据前台传递的顶层目录tnd_id构造一个顶层目录对象

			floor_num = 0;	// 要显示的目录树层数(当floor_num的值为0时，则遍历所有层次的子目录)
			resultJson.put("top_cat", topCatBean);
			resultJson.put("tnd_lst", this.getSubCatalogGroupingVector(con, prof.usr_ent_id, tnd_id, floor_num));
		}

		resultJson.put("tcr_lst", myTrainingCenterVector);		// 装入所有培训中心ID
		resultJson.put("cur_tcr", this.getTCById(con, tcr_id));	// 装入当前培训中心ID
	}

	/**
	 * 在选课中心"浏览目录"页面的"缩小搜索范围"栏搜索符合条件的课程或高级搜索
	 * @param con
	 * @param courseModuleParam
	 * @param sess
	 * @param prof
	 * @param wizbini
	 * @param resultJson
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws cwException
	 * @throws cwSysMessage
	 */
	public void searchCoursesInLrnCenter(Connection con, CourseModuleParam courseModuleParam, HttpSession sess,
			loginProfile prof, WizbiniLoader wizbini, HashMap resultJson) throws SQLException, IllegalAccessException, InvocationTargetException, cwException, cwSysMessage {

		boolean isNotNullFromSess = false;
		if ("adv_itm_srh".equalsIgnoreCase(courseModuleParam.getFrom())
				&& sess.getAttribute(courseModuleParam.getSearch_id()) != null) {

			// 从Session中获取搜索条件Map
			HashMap conditionMapFromSess = (HashMap)sess.getAttribute(courseModuleParam.getSearch_id());

			long[] tnd_id_lst = courseModuleParam.getTnd_id_lst();
			if (tnd_id_lst == null) {		// 第一次高级搜索
				BeanUtils.populate(courseModuleParam, conditionMapFromSess);	// 将搜索条件Map派发至modParam
			} else if (tnd_id_lst[0] != 0){	// 当高级搜索完成后，在导航栏或目录分类栏点击某个目录
				BeanUtils.populate(courseModuleParam, conditionMapFromSess);	// 将搜索条件Map派发至modParam
				courseModuleParam.setTnd_id_lst(tnd_id_lst);
				courseModuleParam.setTcr_id(this.getTcByTndId(con, tnd_id_lst[0]).getTcr_id());
			} else if (tnd_id_lst[0] == 0 && courseModuleParam.getTcr_id() != 0){// 当高级搜索完成后，在导航栏点击培训中心
				long tcr_id_temp = courseModuleParam.getTcr_id();
				BeanUtils.populate(courseModuleParam, conditionMapFromSess);	// 将搜索条件Map派发至modParam
				courseModuleParam.setTnd_id_lst(tnd_id_lst);
				courseModuleParam.setTcr_id(tcr_id_temp);
			} else if (tnd_id_lst[0] == 0 && courseModuleParam.getTcr_id() == 0) {// 所有类别
				BeanUtils.populate(courseModuleParam, conditionMapFromSess);
				courseModuleParam.setTcr_id(0);
			}

			isNotNullFromSess = true;
		} else if ("adv_itm_srh".equalsIgnoreCase(courseModuleParam.getFrom())
				&& sess.getAttribute(courseModuleParam.getSearch_id()) == null) {
			throw new cwException("Condition in Session is Null!");
		}

//		boolean isNotNullFromSess = true;	// 用于在无Session下测试
		long tcr_id = courseModuleParam.getTcr_id();	// 从客户端获取当前培训中心ID

		// 从客户端获取搜索参数
		String[] srh_key = courseModuleParam.getSrh_key();					// 关键词
		String srh_key_type = courseModuleParam.getSrh_key_type();			// 是否全文搜索(可选值："TITLE"、"FULLTEXT")
		long[] tnd_id_lst = courseModuleParam.getTnd_id_lst();				// 目录ID集合
		String ske_id_lst = courseModuleParam.getSke_id_lst();				// 能力，形如""、"0"、"1,2,3,..."或"1"
		String srh_itm_type_lst = courseModuleParam.getSrh_itm_type_lst();	// 课程类型选项
		String srh_start_period	= courseModuleParam.getSrh_start_period();	// 开课日期时间期限

		// 将搜索条件装入conditionMap中
		HashMap conditionMap = new HashMap();
		conditionMap.put("tcr_id", new Long(tcr_id));
		conditionMap.put("srh_key", srh_key);
		conditionMap.put("srh_key_type", srh_key_type);
		conditionMap.put("tnd_id_lst", tnd_id_lst);
		conditionMap.put("ske_id_lst", ske_id_lst);
		conditionMap.put("srh_itm_type_lst", srh_itm_type_lst);
		conditionMap.put("srh_start_period", srh_start_period);

		Vector tcrIdVec = null;
		if(tcr_id > 0) {
			tcrIdVec = new Vector();
			tcrIdVec.addElement(new Long(tcr_id));
		} else if (tcr_id == 0) {
			tcrIdVec = this.getTcrIdVecByTargetUser(con, prof.usr_ent_id);	// 获取用户相关所有培训中心ID集
		}
		conditionMap.put("tcr_id_lst", tcrIdVec);

		String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();

		// 高级搜索中新增的条件装入到conditionMap
		if (isNotNullFromSess == true) {
			String itm_code = courseModuleParam.getItm_code();	// 课程编号
			String srh_appn_start_datetime = courseModuleParam.getSrh_appn_start_datetime();// 报名日期的开始搜索时间
			String srh_appn_end_datetime = courseModuleParam.getSrh_appn_end_datetime();	// 报名日期的结束搜索时间

			conditionMap.put("itm_code", itm_code);
			conditionMap.put("srh_appn_start_datetime", srh_appn_start_datetime);
			conditionMap.put("srh_appn_end_datetime", srh_appn_end_datetime);
		}

		// 类别栏Map
		HashMap tndMap = new HashMap();

		Vector catalogVector = null;		// 当前培训中心所有顶层目录的集合(或某一目录下所有已发布的一级子目录的集合)，注：顶层目录中必须存在符合搜索条件的用户可见课程
		Vector myViewCourseVector = null;	// 当前培训中心所有顶层目录(包括子目录)下的用户可见课程的集合(或某一目录(包括子目录)下用户可见课程集合)

		// 课程列表栏Map
		HashMap myViewCourseMap = new HashMap();

		long tnd_id = 0;	// 目录ID
		if (tnd_id_lst != null && tnd_id_lst.length > 0) {
			tnd_id = (courseModuleParam.getTnd_id_lst())[0];
		}

		if(tnd_id > 0) {
			AcTreeNode actnd = new AcTreeNode(con);
			long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
			aeTreeNode tnd = new aeTreeNode();
			tnd.tnd_id = tnd_id;
			tnd.tnd_cat_id = tnd.getCatalogId(con);
			if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role, tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
				throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
			}
        	if (!aeCatalog.canReadByLrn(con, tnd.tnd_cat_id, prof.root_ent_id, prof.usr_ent_id,  wizbini)) {
                throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
        	}
		}

		if (tcr_id == -1) {
		    //search for shared item
            myViewCourseVector = getSharedCourses(con, courseModuleParam, conditionMap, prof.cur_lan, itmDir);
            myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
            myViewCourseMap.put("itm_lst", myViewCourseVector);
        } else if (tcr_id != 0 && tnd_id == 0) {	// 当用户第一次点击选课中心的"筛选课程"时，在类别栏显示当前培训中心下的所有顶层目录(顶层目录中可以存在也可以不存在符合搜索条件的用户可见课程)
            // 注：当tnd_id的值为0时，在类别栏中放入当前培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)；当为其他值时，在类别栏中放入当前目录的所有已发布的一级子目录(一级子目录中必须存在符合搜索条件的用户可见课程)
			// 在类别栏中构造一个所有顶层目录的父目录(由于所有顶层目录的父目录不存在，可以设置该父目录的tnd_id值为0、tnd_title值为""，以便于前台统一输出)
			CatalogBean parentCatBean = new CatalogBean();
			parentCatBean.setTnd_id(0);
			parentCatBean.setTnd_title("");
			parentCatBean.setTcr_id(tcr_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入当前培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)
			catalogVector = this.getTopCatBeansForSrhCondition(con, wizbini, prof.usr_ent_id, false, false, conditionMap, false, false);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 在课程列表栏中放入学员在当前培训中心下的所有符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);

			resultJson.put("cur_tcr", this.getTCById(con, tcr_id));// 装入当前培训中心ID

			// 获取系统中的课程类型
			Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, null, prof.cur_lan);

			// 搜索栏Map
			HashMap courseTypeMap = new HashMap();
			// 在搜索栏加入课程类型信息
			courseTypeMap.put("itm_type_lst", courseTypeVector);

			resultJson.put("srh_meta", courseTypeMap);			// 装入搜索栏
		} else if ((tcr_id != 0 && tnd_id != 0) || (tcr_id == 0 && tnd_id != 0)) {	// 当进行搜索后，再次点击进入某个目录时，在类别栏放入已发布的一级目录
			// 在类别栏中构造所有一级目录的父目录
			CatalogBean parentCatBean = this.getParentCatBean(con, tnd_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入待查看的目录下的所有已发布的一级子目录(一级子目录中必须可以存在也可以不存在符合搜索条件的用户可见课程)
			catalogVector = this.getFirstFloorCatBeansForSrhCondition(con, prof.usr_ent_id, tnd_id, tcr_id, false, false, conditionMap, false);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 当用户进入某个目录时，在类别栏中生成导航条
			NodeNavigationBar nodeNavBar = this.buildNavigationBar(con, tnd_id);
			resultJson.put("nav_link", nodeNavBar);

			// 装入当前培训中心ID
			if (tcr_id != 0) {
				resultJson.put("cur_tcr", this.getTCById(con, tcr_id));
			} else {
				resultJson.put("cur_tcr", this.getTcByTndId(con, tnd_id));
			}

			// 在课程列表栏中放入学员在当前培训中心下的所有符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);
		} else if (tcr_id == 0 && tnd_id == 0) {	// 在高级搜索中选择所有目录，前两个if子句为"缩小搜索范围"栏搜索和"高级搜索"共用
			// 在类别栏中构造一个所有顶层目录的父目录(由于所有顶层目录的父目录不存在，可以设置该父目录的tnd_id值为0、tnd_title值为""，以便于前台统一输出)
			CatalogBean parentCatBean = new CatalogBean();
			parentCatBean.setTnd_id(0);
			parentCatBean.setTnd_title("");
			parentCatBean.setTcr_id(tcr_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入所有培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)
			catalogVector = this.getTopCatBeansForSrhCondition(con, wizbini, prof.usr_ent_id, false, false, conditionMap, false, true);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 在课程列表栏中放入学员在所有培训中心下的所有顶层目录中符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);
		}

		resultJson.put("srh_criteria", conditionMap);
		resultJson.put("tnd", tndMap);
		resultJson.put("items", myViewCourseMap);
	}

	/**
	 * 在选课中心"浏览目录"页面的"缩小搜索范围"栏搜索符合条件的课程或高级搜索
	 * @param con
	 * @param courseModuleParam
	 * @param prof
	 * @param wizbini
	 * @param resultJson
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws cwException
	 * @throws cwSysMessage
	 */
	public void searchCoursesInLrnCenter(Connection con, CourseModuleParam courseModuleParam,
			loginProfile prof, WizbiniLoader wizbini, HashMap resultJson) throws SQLException, IllegalAccessException, InvocationTargetException, cwException, cwSysMessage {
		long tcr_id = courseModuleParam.getTcr_id();	// 从客户端获取当前培训中心ID

		// 从客户端获取搜索参数
		String[] srh_key = courseModuleParam.getSrh_key(); 									// 关键词
		String srh_key_type = courseModuleParam.getSrh_key_type(); 							// 是否全文搜索(可选值："TITLE"、"FULLTEXT")
		long[] tnd_id_lst = courseModuleParam.getTnd_id_lst(); 								// 目录ID集合
		String ske_id_lst = courseModuleParam.getSke_id_lst(); 								// 能力，形如""、"0"、"1,2,3,..."或"1"
		String srh_itm_type_lst = courseModuleParam.getSrh_itm_type_lst(); 					// 课程类型选项
		String srh_start_period = courseModuleParam.getSrh_start_period(); 					// 开课日期时间期限
		String itm_code = courseModuleParam.getItm_code(); 									// 课程编号
		String srh_appn_start_datetime = courseModuleParam.getSrh_appn_start_datetime();	// 报名日期的开始搜索时间
		String srh_appn_end_datetime = courseModuleParam.getSrh_appn_end_datetime(); 		// 报名日期的结束搜索时间

		// 将搜索条件装入conditionMap中
		HashMap conditionMap = new HashMap();
		conditionMap.put("tcr_id", new Long(tcr_id));
		conditionMap.put("srh_key", srh_key);
		conditionMap.put("srh_key_type", srh_key_type);
		conditionMap.put("tnd_id_lst", tnd_id_lst);
		conditionMap.put("ske_id_lst", ske_id_lst);
		conditionMap.put("srh_itm_type_lst", srh_itm_type_lst);
		conditionMap.put("srh_start_period", srh_start_period);
		conditionMap.put("itm_code", itm_code);
		conditionMap.put("srh_appn_start_datetime", srh_appn_start_datetime);
		conditionMap.put("srh_appn_end_datetime", srh_appn_end_datetime);

		Vector tcrIdVec = null;
		if(tcr_id > 0) {
			tcrIdVec = new Vector();
			tcrIdVec.addElement(new Long(tcr_id));
		} else if (tcr_id == 0) {
			tcrIdVec = this.getTcrIdVecByTargetUser(con, prof.usr_ent_id);	// 获取用户相关所有培训中心ID集
		}
		conditionMap.put("tcr_id_lst", tcrIdVec);

		String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getUrl();

		// 类别栏Map
		HashMap tndMap = new HashMap();

		Vector catalogVector = null;		// 当前培训中心所有顶层目录的集合(或某一目录下所有已发布的一级子目录的集合)，注：顶层目录中必须存在符合搜索条件的用户可见课程
		Vector myViewCourseVector = null;	// 当前培训中心所有顶层目录(包括子目录)下的用户可见课程的集合(或某一目录(包括子目录)下用户可见课程集合)

		// 课程列表栏Map
		HashMap myViewCourseMap = new HashMap();

		long tnd_id = 0;	// 目录ID
		if (tnd_id_lst != null && tnd_id_lst.length > 0) {
			tnd_id = (courseModuleParam.getTnd_id_lst())[0];
		}

		if(tnd_id > 0) {
			AcTreeNode actnd = new AcTreeNode(con);
			long[] usr_ent_ids = usrGroups(prof.usr_ent_id, prof.usrGroups);
			aeTreeNode tnd = new aeTreeNode();
			tnd.tnd_id = tnd_id;
			tnd.tnd_cat_id = tnd.getCatalogId(con);
			if(!actnd.hasReadPrivilege(prof.usr_ent_id, prof.current_role, tnd.tnd_id, tnd.tnd_cat_id, usr_ent_ids)){
				throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
			}
        	if (!aeCatalog.canReadByLrn(con, tnd.tnd_cat_id, prof.root_ent_id, prof.usr_ent_id,  wizbini)) {
                throw new cwSysMessage(aeCatalog.NO_ACCESS_RIGHT);
        	}
		}

		if (tcr_id == -1) {
		    //search for shared item
            myViewCourseVector = getSharedCourses(con, courseModuleParam, conditionMap, prof.cur_lan, itmDir);
            myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
            myViewCourseMap.put("itm_lst", myViewCourseVector);
        } else if (tcr_id != 0 && tnd_id == 0) {	// 当用户第一次点击选课中心的"筛选课程"时，在类别栏显示当前培训中心下的所有顶层目录(顶层目录中可以存在也可以不存在符合搜索条件的用户可见课程)
            // 注：当tnd_id的值为0时，在类别栏中放入当前培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)；当为其他值时，在类别栏中放入当前目录的所有已发布的一级子目录(一级子目录中必须存在符合搜索条件的用户可见课程)
			// 在类别栏中构造一个所有顶层目录的父目录(由于所有顶层目录的父目录不存在，可以设置该父目录的tnd_id值为0、tnd_title值为""，以便于前台统一输出)
			CatalogBean parentCatBean = new CatalogBean();
			parentCatBean.setTnd_id(0);
			parentCatBean.setTnd_title("");
			parentCatBean.setTcr_id(tcr_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入当前培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)
			catalogVector = this.getTopCatBeansForSrhCondition(con, wizbini, prof.usr_ent_id, false, false, conditionMap, false, false);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 在课程列表栏中放入学员在当前培训中心下的所有符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);

			resultJson.put("cur_tcr", this.getTCById(con, tcr_id));// 装入当前培训中心ID

			// 获取系统中的课程类型
			Vector courseTypeVector = Study.getItemTypes(con, prof.root_ent_id, null, prof.cur_lan);

			// 搜索栏Map
			HashMap courseTypeMap = new HashMap();
			// 在搜索栏加入课程类型信息
			courseTypeMap.put("itm_type_lst", courseTypeVector);

			resultJson.put("srh_meta", courseTypeMap);			// 装入搜索栏
		} else if ((tcr_id != 0 && tnd_id != 0) || (tcr_id == 0 && tnd_id != 0)) {	// 当进行搜索后，再次点击进入某个目录时，在类别栏放入已发布的一级目录
			// 在类别栏中构造所有一级目录的父目录
			CatalogBean parentCatBean = this.getParentCatBean(con, tnd_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入待查看的目录下的所有已发布的一级子目录(一级子目录中必须可以存在也可以不存在符合搜索条件的用户可见课程)
			catalogVector = this.getFirstFloorCatBeansForSrhCondition(con, prof.usr_ent_id, tnd_id, tcr_id, false, false, conditionMap, false);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 当用户进入某个目录时，在类别栏中生成导航条
			NodeNavigationBar nodeNavBar = this.buildNavigationBar(con, tnd_id);
			resultJson.put("nav_link", nodeNavBar);

			// 装入当前培训中心ID
			if (tcr_id != 0) {
				resultJson.put("cur_tcr", this.getTCById(con, tcr_id));
			} else {
				resultJson.put("cur_tcr", this.getTcByTndId(con, tnd_id));
			}

			// 在课程列表栏中放入学员在当前培训中心下的所有符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);
		} else if (tcr_id == 0 && tnd_id == 0) {	// 在高级搜索中选择所有目录，前两个if子句为"缩小搜索范围"栏搜索和"高级搜索"共用
			// 在类别栏中构造一个所有顶层目录的父目录(由于所有顶层目录的父目录不存在，可以设置该父目录的tnd_id值为0、tnd_title值为""，以便于前台统一输出)
			CatalogBean parentCatBean = new CatalogBean();
			parentCatBean.setTnd_id(0);
			parentCatBean.setTnd_title("");
			parentCatBean.setTcr_id(tcr_id);
			tndMap.put("parent_node", parentCatBean);

			// 在类别栏中放入所有培训中心下的所有顶层目录(顶层目录中必须存在符合搜索条件的用户可见课程)
			catalogVector = this.getTopCatBeansForSrhCondition(con, wizbini, prof.usr_ent_id, false, false, conditionMap, false, true);
			tndMap.put("sub_tnd_lst", catalogVector);

			// 在课程列表栏中放入学员在所有培训中心下的所有顶层目录中符合搜索条件的用户可见课程
			myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
			myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
			myViewCourseMap.put("itm_lst", myViewCourseVector);
		}

		resultJson.put("srh_criteria", conditionMap);
		resultJson.put("tnd", tndMap);
		resultJson.put("items", myViewCourseMap);
	}

	/**
	 * 在"我的培训计划"页面搜索，同时过滤掉已添加到计划的培训
	 * @param con
	 * @param courseModuleParam
	 * @param prof
	 * @param wizbini
	 * @param resultJson
	 * @throws SQLException
	 * @throws cwException 
	 */
	public void searchInLrnPlan(Connection con, CourseModuleParam courseModuleParam,
			loginProfile prof, WizbiniLoader wizbini, HashMap resultJson) throws SQLException, cwException {

		// 从客户端获取搜索参数
		String[] srh_key = courseModuleParam.getSrh_key();	// 关键词
		Vector tcrIdVec = this.getTcrIdVecByTargetUser(con, prof.usr_ent_id);	// 获取用户相关所有培训中心ID集

		// 将搜索条件装入conditionMap中
		HashMap conditionMap = new HashMap();
		conditionMap.put("srh_key", srh_key);
		conditionMap.put("srh_key_type", "TITLE");		// 非全文搜索
		conditionMap.put("tcr_id_lst", tcrIdVec);

		// 课程列表栏Map
		HashMap myViewCourseMap = new HashMap();

		String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();

		// 在课程列表栏中放入学员在所有培训中心下的所有顶层目录中符合搜索条件的用户可见课程
		Vector myViewCourseVector = this.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, courseModuleParam, itmDir);
		myViewCourseMap.put("total", new Integer(courseModuleParam.getTotal_rec()));
		myViewCourseMap.put("itm_lst", myViewCourseVector);

		resultJson.put("items", myViewCourseMap);
	}

	public Vector getHotCourse(Connection con, CourseModuleParam param, loginProfile prof, WizbiniLoader wizbini) throws SQLException, qdbException {
		// 过滤：只显示当前用户关联的二级及子培训中心
		String tcStr = "";
		if (wizbini != null && !wizbini.cfgSysSetupadv.isLrnShowHeaderTc()) {
			TrainingCenter tc = new TrainingCenter();
			Vector relatedTc = tc.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
			
			Vector tcIds = new Vector();
			for (int i = 0; i < relatedTc.size(); i++) {
				TCBean tcBean = (TCBean) relatedTc.get(i);
				
				if (tcBean.getTcr_id() == 1) {
					continue;
				}
				tcIds.add(tcBean.getTcr_id());
			}
			if (tcIds != null && tcIds.size() > 0) {
				tcStr = " and itm_tcr_id in " + cwUtils.vector2list(tcIds);
			}
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select itm.itm_id,itm.itm_title,itm.itm_type ,tab.app_count ,itm_comment_avg_score,itm_comment_total_count,itm_comment_total_score, itm_desc,itm_blend_ind, itm_create_run_ind,itm_exam_ind,itm_ref_ind,itm_icon ");
		sql.append(" From aeItem itm ");
		sql.append(",( ");
		sql.append(" select ire_parent_itm_id itm_id ,count(*) app_count From aeItemRelation , aeItem , aeApplication ");
		sql.append(" where itm_id = ire_child_itm_id ");
		sql.append(" and itm_id = app_itm_id");
		sql.append(" and app_status = ? ");
		sql.append(" group by ire_parent_itm_id ");
		sql.append(" union ");
		sql.append(" select itm_id,count(*) app_count from aeItem,aeApplication ");
		sql.append(" where itm_id = app_itm_id ");
		sql.append(" and itm_create_run_ind = 0 ");
		sql.append(" and itm_run_ind = 0 ");
		sql.append(" and app_status = ? ");
		sql.append(tcStr);
		sql.append(" group by itm_id ");
		sql.append(" ) tab ");
		sql.append(" where itm.itm_id = tab.itm_id ");
		sql.append(" and itm.itm_status = ? ");
		sql.append(aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_COS, "itm", true));
		sql.append(SQL_GET_TC_COURSE_BY_USER_EXISTS);
		sql.append("order by tab.app_count desc,itm_title asc ");
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql.toString());
		int parameterIndex=1;
		stmt.setString(parameterIndex++, aeApplication.ADMITTED);
		stmt.setString(parameterIndex++, aeApplication.ADMITTED);
		stmt.setString(parameterIndex++, aeItem.ITM_STATUS_ON);
		stmt.setLong(parameterIndex,prof.usr_ent_id);
		ResultSet rs=stmt.executeQuery();
		Vector hotCourseVc=new Vector();

		CourseBean course=null;
		int count =0;

		while (rs.next()) {
			if (count >= param.getStart() && count < (param.getLimit() + param.getStart())) {
				course = new CourseBean();
				course.setOrder(count);
				course.setItm_id(rs.getLong("itm_id"));
				course.setItm_title(rs.getString("itm_title"));
				course.setItm_type(rs.getString("itm_type"));
				course.setTotal_score(rs.getInt("itm_comment_total_score"));
				course.setTotal_count(rs.getInt("itm_comment_total_count"));
				float avg_score = rs.getFloat("itm_comment_avg_score");
				course.setItm_comment_avg_score(Math.round(avg_score));
				course.setItm_desc(rs.getString("itm_desc"));
				course.setApp_count(rs.getInt("app_count"));
				boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
				boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
				boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
				boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
				String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
				String itmIcon = rs.getString("itm_icon");
				if (itmIcon != null && itmIcon.length() > 0) {
					long item_id = rs.getLong("itm_id");
					String itm_icon = itmDir + "/" + item_id + "/" + itmIcon;
					course.setItm_icon(itm_icon);
				}
				String dummy_type = aeItemDummyType.getDummyItemType(course.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
				course.setItm_dummy_type(dummy_type);

				course.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(prof.cur_lan, aeItemDummyType.getDummyItemType(course.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind)));
				if (itm_create_run_ind) {
					if (!itm_blend_ind) { // 若是离线课程或离线考试，获取其对应的班级的开课时间
						course.setRecent_start_classes(this.getStartDateOfClass(con, course.getItm_id()));
					} else { // 若是混合课程或混合考试，获取其对应的班级的开课时间
						course.setRecent_start_classes(this.getStartDateofBlendItem(con, course.getItm_id()));
					}
				}
				hotCourseVc.add(course);
			}
			count++;

		}
		param.setTotal_rec(count);

		if (count > 0) {
			// 设置课程的开始时间
			courseStartDatetimeByItmID(con, hotCourseVc);
		}

		if (stmt != null)
			stmt.close();
		return hotCourseVc;
	}
	
	public Vector getSorCourses(Connection con, CourseModuleParam param, loginProfile prof, WizbiniLoader wizbini) throws SQLException, qdbException {
		// 过滤：只显示当前用户关联的二级及子培训中心
		String tcStr = "";
		if (wizbini != null && !wizbini.cfgSysSetupadv.isLrnShowHeaderTc()) {
			TrainingCenter tc = new TrainingCenter();
			Vector relatedTc = tc.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
			
			Vector tcIds = new Vector();
			for (int i = 0; i < relatedTc.size(); i++) {
				TCBean tcBean = (TCBean) relatedTc.get(i);
				
				if (tcBean.getTcr_id() == 1) {
					continue;
				}
				tcIds.add(tcBean.getTcr_id());
			}
			if (tcIds != null && tcIds.size() > 0) {
				tcStr = " and itm_tcr_id in " + cwUtils.vector2list(tcIds);
			}
		}

		StringBuffer sql = new StringBuffer();
		sql.append("select itm.itm_id,itm.itm_title,itm.itm_type,tab.app_count ,itm.itm_publish_timestamp, itm.itm_comment_avg_score, itm.itm_comment_total_count, itm.itm_comment_total_score, itm.itm_desc ,itm.itm_blend_ind ,itm.itm_create_run_ind,itm.itm_exam_ind,itm.itm_ref_ind, itm_icon ");
		sql.append(" From aeItem  itm ");
		sql.append(",( ");
		sql.append(" select ire_parent_itm_id itm_id ,count(*) app_count From aeItemRelation , aeItem , aeApplication ");
		sql.append(" where itm_id = ire_child_itm_id ");
		sql.append(" and itm_id = app_itm_id");
		sql.append(" and app_status = ? ");
		sql.append(" group by ire_parent_itm_id ");
		sql.append(" union ");
		sql.append(" select itm_id,count(*) app_count from aeItem,aeApplication ");
		sql.append(" where itm_id = app_itm_id ");
		sql.append(" and itm_create_run_ind = 0 ");
		sql.append(" and itm_run_ind = 0 ");
		sql.append(" and app_status = ? ");
		sql.append(tcStr);
		sql.append(" group by itm_id ");
		sql.append(" ) tab ");
		sql.append(" where itm.itm_id = tab.itm_id ");
		sql.append(" and itm.itm_status = ? ");
		sql.append(" and itm.itm_comment_total_count > 0 "); // Lee:过滤未投票的课程
		sql.append(SQL_GET_TC_COURSE_BY_USER_EXISTS);
		sql.append(aeItemDummyType.genSqlByItemDummyType(aeItemDummyType.ITEM_DUMMY_TYPE_COS, "itm", true));
		if(param.isMobile()){ // 添加移动端
			sql.append("and itm.itm_type = 'MOBILE' ");
			if(param.getTnd_id() > 0){
				sql.append("and itm.itm_id in (SELECT chile.tnd_itm_id FROM aeTreeNode chile where chile.tnd_parent_tnd_id = ? and chile.tnd_type='ITEM') ");
			}else{
				sql.append("and itm.itm_id in (SELECT chile.tnd_itm_id FROM aeTreeNode chile ");
				sql.append("inner join (select tnd_id from aeTreeNode WHERE tnd_parent_tnd_id IN ");
				sql.append("(SELECT tnd.tnd_id FROM aeTreeNode tnd INNER JOIN aeCatalog cat ");
				sql.append("ON (cat.cat_id = tnd.tnd_cat_id) WHERE tnd.tnd_parent_tnd_id IS NULL ");
				sql.append("AND cat.cat_mobile_ind = 1 AND cat.cat_status='ON') ");
				sql.append("AND tnd_type = 'NORMAL' ) parent on parent.tnd_id = chile.tnd_parent_tnd_id and chile.tnd_type='ITEM') ");
			}
			if(param.getComment() != null && !"".equals(param.getComment().trim())){
				sql.append("and itm.itm_title like ? ");
			}
		}
		sql.append(" order by itm.itm_comment_avg_score desc ,itm.itm_comment_total_count desc , itm.itm_title asc ");

		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql.toString());

		int index = 1;
		stmt.setString(index++, aeApplication.ADMITTED);
		stmt.setString(index++, aeApplication.ADMITTED);
		stmt.setString(index++, aeItem.ITM_STATUS_ON);
		stmt.setLong(index++, prof.usr_ent_id);
		if(param.isMobile()){
			if(param.getTnd_id() > 0){
				stmt.setLong(index++, param.getTnd_id());
			}
			if(param.getComment() != null && !"".equals(param.getComment().trim())){
				stmt.setString(index++, "%"+param.getComment()+"%");
			}
		}
		ResultSet rs = stmt.executeQuery();
		Vector vCourse = new Vector();
		CourseBean course = null;
		int count = 0;

		while (rs.next()) {
			if (count >= param.getStart() && count < (param.getLimit() + param.getStart())) {
				course = new CourseBean();
				course.setOrder(count);
				course.setItm_id(rs.getInt("itm_id"));
				course.setItm_title(rs.getString("itm_title"));
				course.setItm_type(rs.getString("itm_type"));
				course.setTotal_score(rs.getInt("itm_comment_total_score"));
				course.setTotal_count(rs.getInt("itm_comment_total_count"));
				float avg_score = rs.getFloat("itm_comment_avg_score");
				course.setItm_comment_avg_score(Math.round(avg_score));
				course.setItm_desc(rs.getString("itm_desc"));
				course.setApp_count(rs.getInt("app_count"));
				boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
				boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
				boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
				boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
				String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
				String itmIcon = rs.getString("itm_icon");
				if (itmIcon != null && itmIcon.length() > 0) {
					long item_id = rs.getLong("itm_id");
					String itm_icon = itmDir + "/" + item_id + "/" + itmIcon;
					course.setItm_icon(itm_icon);
				}
				String dummy_type = aeItemDummyType.getDummyItemType(course.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
				course.setItm_dummy_type(dummy_type);
				course.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(prof.cur_lan, aeItemDummyType.getDummyItemType(course.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind)));
				if (itm_create_run_ind) {
					if (!itm_blend_ind) { // 若是离线课程或离线考试，获取其对应的班级的开课时间
						course.setRecent_start_classes(this.getStartDateOfClass(con, course.getItm_id()));
					} else { // 若是混合课程或混合考试，获取其对应的班级的开课时间
						course.setRecent_start_classes(this.getStartDateofBlendItem(con, course.getItm_id()));
					}
				}
				vCourse.add(course);
			}
			count++;
		}
		param.setTotal_rec(count);

		if (vCourse.size() > 0) {
			courseStartDatetimeByItmID(con, vCourse);
		}

		if (stmt != null)
			stmt.close();
		return vCourse;
	}

	public Vector getLastedCourses(Connection con, CourseModuleParam param, loginProfile prof, WizbiniLoader wizbini) throws SQLException, qdbException {
		// 过滤：只显示当前用户关联的二级及子培训中心
		String tcStr = "";
		if (wizbini != null && !wizbini.cfgSysSetupadv.isLrnShowHeaderTc()) {
			TrainingCenter tc = new TrainingCenter();
			Vector relatedTc = tc.getTrainingCenterByTargetUser(con, prof.usr_ent_id);

			Vector tcIds = new Vector();
			for (int i = 0; i < relatedTc.size(); i++) {
				TCBean tcBean = (TCBean) relatedTc.get(i);

				if (tcBean.getTcr_id() == 1) {
					continue;
				}
				tcIds.add(tcBean.getTcr_id());
			}
			if (tcIds != null && tcIds.size() > 0) {
				tcStr = " and itm_tcr_id in " + cwUtils.vector2list(tcIds);
			}
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("select itm_id,itm_title,itm_type,itm_publish_timestamp, itm_comment_avg_score,itm_desc, itm_comment_total_count, itm_comment_total_score, itm_publish_timestamp, itm_desc ,itm_blend_ind ,itm_create_run_ind,itm_exam_ind,itm_ref_ind,itm_icon ");
		sql.append(" From aeItem itm ");
		sql.append(" where ( (itm.itm_create_run_ind = 1 and itm.itm_run_ind = 0) ");
		sql.append(" or (itm.itm_create_run_ind = 0 and itm.itm_run_ind = 0) )");
		sql.append(" and itm.itm_status = ? ");
		sql.append(tcStr);
		sql.append(" and itm.itm_id in(");
		sql.append(SQL_GET_TC_COURSE_BY_USER);
		sql.append(") ");
		if(param.isMobile()){ // 添加移动端
			sql.append("and itm.itm_type = 'MOBILE' ");
			if(param.getTnd_id() > 0){
				sql.append("and itm.itm_id in (SELECT chile.tnd_itm_id FROM aeTreeNode chile where chile.tnd_parent_tnd_id = ? and chile.tnd_type='ITEM') ");
			}else{
				sql.append("and itm.itm_id in (SELECT chile.tnd_itm_id FROM aeTreeNode chile ");
				sql.append("inner join (select tnd_id from aeTreeNode WHERE tnd_parent_tnd_id IN ");
				sql.append("(SELECT tnd.tnd_id FROM aeTreeNode tnd INNER JOIN aeCatalog cat ");
				sql.append("ON (cat.cat_id = tnd.tnd_cat_id) WHERE tnd.tnd_parent_tnd_id IS NULL ");
				sql.append("AND cat.cat_mobile_ind = 1 AND cat.cat_status='ON') ");
				sql.append("AND tnd_type = 'NORMAL' ) parent on parent.tnd_id = chile.tnd_parent_tnd_id and chile.tnd_type='ITEM') ");
			}
			if(param.getComment() != null && !"".equals(param.getComment().trim())){
				sql.append("and itm.itm_title like ? ");
			}
		}
		sql.append(" order by itm.itm_publish_timestamp desc, itm.itm_title asc ");

		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql.toString());
		int index = 1;
		stmt.setString(index++, aeItem.ITM_STATUS_ON);
		stmt.setLong(index++, prof.usr_ent_id);
		if(param.isMobile()){
			if(param.getTnd_id() > 0){
				stmt.setLong(index++, param.getTnd_id());
			}
			if(param.getComment() != null && !"".equals(param.getComment().trim())){
				stmt.setString(index++, "%" + param.getComment() + "%");
			}
		}
		ResultSet rs = stmt.executeQuery();
		Vector vCourse = new Vector();
		CourseBean course = null;
		int count = 0;
		while (rs.next()) {
			if (count >= param.getStart() && count < (param.getLimit() + param.getStart())) {
				course = new CourseBean();
				course.setOrder(count);
				course.setItm_id(rs.getInt("itm_id"));
				course.setItm_title(rs.getString("itm_title"));
				course.setItm_type(rs.getString("itm_type"));
				course.setTotal_score(rs.getInt("itm_comment_total_score"));
				course.setTotal_count(rs.getInt("itm_comment_total_count"));
				float avg_score = rs.getFloat("itm_comment_avg_score");
				course.setItm_comment_avg_score(Math.round(avg_score));
				course.setItm_desc(rs.getString("itm_desc"));
				course.setItm_publish_timestamp(rs.getTimestamp("itm_publish_timestamp"));
				boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
				boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
				boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
				boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
				String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
				String itmIcon = rs.getString("itm_icon");
				if (itmIcon != null && itmIcon.length() > 0) {
					long item_id = rs.getLong("itm_id");
					String itm_icon = itmDir + "/" + item_id + "/" + itmIcon;
					course.setItm_icon(itm_icon);
				}
				String dummy_type = aeItemDummyType.getDummyItemType(course.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
				course.setItm_dummy_type(dummy_type);
				course.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(prof.cur_lan, aeItemDummyType.getDummyItemType(course.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind)));
				if (itm_create_run_ind) {
					if (!itm_blend_ind) { // 若是离线课程或离线考试，获取其对应的班级的开课时间
						course.setRecent_start_classes(this.getStartDateOfClass(con, course.getItm_id()));
					} else { // 若是混合课程或混合考试，获取其对应的班级的开课时间
						course.setRecent_start_classes(this.getStartDateofBlendItem(con, course.getItm_id()));
					}
				}
				vCourse.add(course);
			}
			count++;
		}
		param.setTotal_rec(count);

		if (vCourse.size() > 0) {
			courseStartDatetimeByItmID(con, vCourse);
		}

		if (stmt != null)
			stmt.close();
		return vCourse;
	}

    /**
     * 根据传入的课程集合,去查找集合中课程的(班级)开始时间
     * @param con
     * @param vCourse
     * @throws SQLException
     */
	public void courseStartDatetimeByItmID(Connection con, Vector vCourse) throws SQLException{
		HashMap hashCourse = new HashMap();

		String itm_id_lst = "";
		for(int i=0;i<vCourse.size();i++){
			CourseBean course = (CourseBean) vCourse.get(i);
			if(i==0){
				itm_id_lst = ""+course.getItm_id();
			}else{
				itm_id_lst = itm_id_lst + "," + course.getItm_id();
			}
			hashCourse.put(""+course.getItm_id(),course);
		}
		//根据上面产生的课程ID,去查找课程下班级的开始时间
		StringBuffer sql_cls_start_datetime = new StringBuffer();
		sql_cls_start_datetime.append("select ire_parent_itm_id,itm_id cls_itm_id, ")
		   .append(cwSQL.minDate("itm_eff_start_datetime", "itm_content_eff_start_datetime", "itm_eff_start_datetime", con.getMetaData().getDatabaseMajorVersion()))
		   .append("from aeItemRelation,aeItem where ire_child_itm_id = itm_id ")
		   .append("and itm_status = ? ")
		   .append("and ire_parent_itm_id in ("+itm_id_lst+") ")
		   .append("and itm_eff_start_datetime > ? ")
		   .append("order by itm_eff_start_datetime asc ");

		PreparedStatement tempStmt = con.prepareStatement(sql_cls_start_datetime.toString());
		int index = 1;
		tempStmt.setString(index++, aeItem.ITM_STATUS_ON);
		tempStmt.setTimestamp(index++, cwSQL.getTime(con));

		ResultSet tempRs = tempStmt.executeQuery();
		while(tempRs.next()){
			((CourseBean)hashCourse.get(""+tempRs.getLong("ire_parent_itm_id"))).getStart_timestamp().add(tempRs.getTimestamp("itm_eff_start_datetime"));
		}


		if(tempStmt!=null) tempStmt.close();
	}

	/**
	 * 当用户点击浏览"详细分类"时，浏览给定的培训中心下的所有顶层目录及其一级子目录(遍历时不包括目录中的子课程)
	 * @param course 选课中心服务对象
	 * @param tcr_id 培训中心ID
	 * @param floor_num 要显示的目录树层数
	 * @return 返回用于分组显示某个培训中心的所有顶层目录和其一级子目录组成的目录树的集合
	 * @throws SQLException
	 */
	public Vector getTopCatalogGroupingVector(Connection con, long usr_ent_id, long tcr_id, int floor_num) throws SQLException {

//		CourseModuleParam courseModuleParam = (CourseModuleParam)param;

		// 当用户点击浏览"详细分类"时，输出给定的某个培训中心下的所有顶层目录下的一级子目录
//		long tcr_id = courseModuleParam.getTcr_id();	// 从客户端获取培训中心ID

		// 获取学员在某一培训中心下的所有已发布的顶层目录
		Vector topCatBeanVector = this.getTopCatalogBeans(con, usr_ent_id, tcr_id, true, true, 0);

		// 页面显示时是分三列来显示所有顶层目录，需要将当前的所有的顶层目录topCatalogVector分为三个数组来放
		Vector topCatalogGroupingVector = new Vector();
		Vector subGroupingVector = null;

		int sizeOfTopCatalogs = topCatBeanVector.size();	// 顶层目录的个数
//		int floor_num = courseModuleParam.getFloor_num();	// 从客户端获取要显示的目录树层数

		if (sizeOfTopCatalogs <= 3) {	// 若顶层目录的个数少于或等于3个
			for (int i = 0; i < sizeOfTopCatalogs; i++) {
				subGroupingVector = new Vector();

				long tnd_id = ((CatalogBean)topCatBeanVector.get(i)).getId();// 获取每一个顶层目录ID
				CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, true, tnd_id, floor_num, 1, true);	// 获取该目录树
				subGroupingVector.add(catalogBean);

				topCatalogGroupingVector.add(subGroupingVector);
			}
		} else {	// 若顶层目录的个数多于3个

			// 前两个用于显示的顶层目录组中的目录个数
			int firstOrSecondCatGroupSize = sizeOfTopCatalogs / 3;
			if (sizeOfTopCatalogs % 3 != 0) {
				firstOrSecondCatGroupSize += 1;
			}

			// 最后一个用于显示的顶层目录组的目录个数
			int endCatGroupSize = sizeOfTopCatalogs - firstOrSecondCatGroupSize * 2;

			int count = 0;	// 顶层目录的计数器
			for (int i = 0; i < 3; i++) {

				// 装入到第一个用于显示的顶层目录组
				if (count < firstOrSecondCatGroupSize) {
					subGroupingVector = new Vector();

					for(; count < firstOrSecondCatGroupSize; count++) {
						long tnd_id = ((CatalogBean)topCatBeanVector.get(count)).getId();	// 获取每一个顶层目录ID
						CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, true, tnd_id, floor_num, 1, true);// 生成该目录树对象
						subGroupingVector.add(catalogBean);
					}

					topCatalogGroupingVector.add(subGroupingVector);
					continue;
				}

				// 装入到第二个用于显示的顶层目录组
				if (count < firstOrSecondCatGroupSize * 2) {
					subGroupingVector = new Vector();

					for(; count < firstOrSecondCatGroupSize * 2; count++) {
						long tnd_id = ((CatalogBean)topCatBeanVector.get(count)).getId();	// 获取每一个顶层目录ID
						CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, true, tnd_id, floor_num, 1, true);// 获取该目录树
						subGroupingVector.add(catalogBean);
					}

					topCatalogGroupingVector.add(subGroupingVector);
					continue;
				}

				// 装入到第三个用于显示的顶层目录组
				if (endCatGroupSize > 0 && count < sizeOfTopCatalogs) {
					subGroupingVector = new Vector();

					for(; count < sizeOfTopCatalogs; count++) {
						long tnd_id = ((CatalogBean)topCatBeanVector.get(count)).getId();	// 获取每一个顶层目录ID
						CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, true, tnd_id, floor_num, 1, true);// 获取该目录树
						subGroupingVector.add(catalogBean);
					}

					topCatalogGroupingVector.add(subGroupingVector);
					continue;
				}

			} // end for

		} // end else of if...else

		return topCatalogGroupingVector;
	}

	/**
	 * 当用户进入"详细分类"并浏览某个顶层目录时，获取该顶层目录下的所有已发布的一级子目录为根结点的目录树(遍历时不包括目录中的子课程)
	 * @param course 选课中心服务对象
	 * @param tnd_id 目录ID
	 * @param floor_num 要显示的目录树层数
	 * @return 返回用于分组显示某个顶层目录下的所有已发布的一级子目录为根结点的目录树的集合
	 * @throws SQLException
	 */
	public Vector getSubCatalogGroupingVector(Connection con, long usr_ent_id, long tnd_id, int floor_num) throws SQLException {

//		CourseModuleParam courseModuleParam = (CourseModuleParam)param;

//		long tnd_id = courseModuleParam.getTnd_id();	// 从客户端获取该顶层目录的ID(对应aeTreeNode表的tnd_id)

		// 获取该顶层目录下所有已发布的一级子目录
		Vector firstFloorSubCatVector = this.getFirstFloorCatalogs(con, usr_ent_id, tnd_id, true, true);

		// 页面显示时是分三列来显示所有一级子目录，需要将当前的所有的一级子目录firstFloorSubCatGroupingVector分为三个数组来放
		Vector firstFloorSubCatGroupingVector = new Vector();
		Vector subGroupingVector = null;

		int sizeOfFirstFloorSubCats = firstFloorSubCatVector.size();// 一级子目录的个数
//		int floor_num = courseModuleParam.getFloor_num();			// 从客户端获取要显示的目录树层数(当floor_num的值为0时，则遍历所有层次的子目录)

		if (sizeOfFirstFloorSubCats <= 3) {	// 若一级子目录的个数少于或等于3个
			for (int i = 0; i < sizeOfFirstFloorSubCats; i++) {
				subGroupingVector = new Vector();

				long firstFloorTndId = ((CatalogBean)firstFloorSubCatVector.get(i)).getId();// 获取每个一级子目录ID
				CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, false, firstFloorTndId, floor_num, 1, true,true);	// 生成该目录树对象
				subGroupingVector.add(catalogBean);

				firstFloorSubCatGroupingVector.add(subGroupingVector);
			}
		} else {
			subGroupingVector = new Vector();
			for(int i=0 ; i< firstFloorSubCatVector.size(); i++){
				long firstFloorTndId = ((CatalogBean)firstFloorSubCatVector.get(i)).getId();// 获取每一个顶层目录ID
				CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, false, firstFloorTndId, floor_num, 1, true,true);// 获取该目录树
				subGroupingVector.add(catalogBean);
			}
			firstFloorSubCatGroupingVector.add(subGroupingVector);

			/*	// 若一级子目录的个数多于3个

			// 前两个用于显示的一级子目录组中的目录个数
			int firstOrSecondCatGroupSize = sizeOfFirstFloorSubCats / 3;
			if (sizeOfFirstFloorSubCats % 3 != 0) {
				firstOrSecondCatGroupSize += 1;
			}

			// 最后一个用于显示的一级子目录组的目录个数
			int endCatGroupSize = sizeOfFirstFloorSubCats - firstOrSecondCatGroupSize * 2;

			int count = 0;	// 一级子目录的计数器
			for (int i = 0; i < 3; i++) {

				// 装入到第一个用于显示的一级子目录组
				if (count < firstOrSecondCatGroupSize) {
					subGroupingVector = new Vector();

					for(; count < firstOrSecondCatGroupSize; count++) {
						long firstFloorTndId = ((CatalogBean)firstFloorSubCatVector.get(i)).getId();// 获取每一个顶层目录ID
						CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, false, firstFloorTndId, floor_num, 1, true,true);// 获取该目录树
						subGroupingVector.add(catalogBean);
					}

					firstFloorSubCatGroupingVector.add(subGroupingVector);
					continue;
				}

				// 装入到第二个用于显示的顶层目录组
				if (count < firstOrSecondCatGroupSize * 2) {
					subGroupingVector = new Vector();

					for(; count < firstOrSecondCatGroupSize * 2; count++) {
						long firstFloorTndId = ((CatalogBean)firstFloorSubCatVector.get(i)).getId();	// 获取每一个顶层目录ID
						CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, false, firstFloorTndId, floor_num, 1, true,true);// 获取该目录树
						subGroupingVector.add(catalogBean);
					}

					firstFloorSubCatGroupingVector.add(subGroupingVector);
					continue;
				}

				// 装入到第三个用于显示的顶层目录组
				if (endCatGroupSize > 0 && count < sizeOfFirstFloorSubCats) {
					subGroupingVector = new Vector();

					for(; count < sizeOfFirstFloorSubCats; count++) {
						long firstFloorTndId = ((CatalogBean)firstFloorSubCatVector.get(i)).getId();	// 获取每一个顶层目录ID
						CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, false, firstFloorTndId, floor_num, 1, true,true);// 获取该目录树
						subGroupingVector.add(catalogBean);
					}

					firstFloorSubCatGroupingVector.add(subGroupingVector);
					continue;
				}

			} // end for

		*/} // end else of if...else

		return firstFloorSubCatGroupingVector;
	}

	/**
	 * 查询与用户相关的培训中心，并按最近用户组排序
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @return 与用户相关的培训中心并按最近用户组排序
	 * @throws SQLException
	 */
	public Vector getTrainingCenterByTargetUser(Connection con, long usr_ent_id) throws SQLException {
		String sql = " select distinct tcr_id, tcr_title, tcr_ste_ent_id, tcr_status, tcr_create_timestamp, "
			+ " tcr_create_usr_id, tcr_update_usr_id, tcr_update_timestamp,tcr_parent_tcr_id, "
			+ " ern_order "
			+ " from EntityRelation, tcTrainingCenterTargetEntity, tcTrainingCenter "
			+ " where ern_ancestor_ent_id = tce_ent_id "
			+ " 	and ern_child_ent_id = ? "
			+ " 	and ern_type = 'USR_PARENT_USG' "
			+ " 	and tce_tcr_id = tcr_id	"
			+ " 	order by ern_order desc, tcr_title ";

		Vector tcVector = new Vector();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, usr_ent_id);
            rs = pstmt.executeQuery();
            Vector v = new Vector();
            while(rs.next()) {
            	TCBean tc = new TCBean();
            	long tcr_id = rs.getLong("tcr_id");
            	if (!v.contains(new Long(tcr_id))) {
            		tc.setTcr_id(tcr_id);
            		tc.setTcr_title(rs.getString("tcr_title"));
            		tc.setTcr_parent_tcr_id(rs.getInt("tcr_parent_tcr_id"));
            		tcVector.add(tc);
            		v.add(new Long(tcr_id));
            	} else {
            		v.add(new Long(tcr_id));
            	}
            }
        } finally {
        	// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
        }

        return tcVector;
    }

	/**
	 * 查询与用户相关的培训中心的ID集，并按最近用户组排序
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @return 与用户相关的培训中心并按最近用户组排序
	 * @throws SQLException
	 */
	public Vector getTcrIdVecByTargetUser(Connection con, long usr_ent_id) throws SQLException {
		String sql = " select distinct tcr_id, tcr_title, tcr_ste_ent_id, tcr_status, tcr_create_timestamp, "
			+ " tcr_create_usr_id, tcr_update_usr_id, tcr_update_timestamp,tcr_parent_tcr_id, "
			+ " ern_order "
			+ " from EntityRelation, tcTrainingCenterTargetEntity, tcTrainingCenter "
			+ " where ern_ancestor_ent_id = tce_ent_id "
			+ " 	and ern_child_ent_id = ? "
			+ " 	and ern_type = 'USR_PARENT_USG' "
			+ " 	and tce_tcr_id = tcr_id	"
			+ " 	order by ern_order desc, tcr_title ";

		Vector tcrIdVec = new Vector();

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, usr_ent_id);
            rs = pstmt.executeQuery();

            while(rs.next()) {
            	tcrIdVec.add(new Long(rs.getLong("tcr_id")));
            }
        } finally {
        	// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
        }

        return tcrIdVec;
    }

	/**
	 * 根据目录的tcr_id获取培训中心Bean
	 * @param con 数据库连接器
	 * @param tcr_id 培训中心ID
	 * @return 培训中心Bean
	 * @throws SQLException
	 */
	public TrainingCenterBean getTCById(Connection con, long tcr_id) throws SQLException {

		// 查询某一培训中心的信息
		String sql = "select tcr_id, tcr_title from tcTrainingCenter where tcr_id=? ";	// 传入：培训中心ID

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		TrainingCenterBean tcBean = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tcr_id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				tcBean = new TrainingCenterBean();

				tcBean.setTcr_id(rs.getLong("tcr_id"));
				tcBean.setTcr_title(rs.getString("tcr_title"));
			}

		} catch (SQLException se) {
			System.err.println("[Get_TC_By_Id_Exception]: " + se.getMessage());
			CommonLog.error(se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return tcBean;
	}

	/**
	 * 获取与用户相关的一个(或多个)培训中心
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param param URL参数信息
	 * @return 与用户相关的培训中心信息
	 * @throws SQLException
	 */
	public Vector getMyTrainingCenter(Connection con, long usr_ent_id, BaseParam param)
		throws SQLException {

		CourseModuleParam courseModuleParam = (CourseModuleParam)param;

		Vector trainingCenterVector = null;
		try {
			// 获取与用户相关的培训中心集合
			trainingCenterVector = this.getTrainingCenterByTargetUser(con, usr_ent_id);

			courseModuleParam.setTotal_rec(trainingCenterVector.size());	// 设置总记录数
		} catch(SQLException se) {
			System.err.println("[Get_My_Training_Center_Exception]: " + se.getMessage());
			CommonLog.error("[Get_My_Training_Center_Exception]: " + se.getMessage(),se);
			throw se;
		}

		return trainingCenterVector;
	}

	/**
	 * 分页提取学员在某一培训中心下的所有已发布的顶层目录(包括子目录)中的可见课程
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param tcr_id 培训中心ID(对应tcTrainingCenter表中的tcr_id)
	 * 		  在此方法中约定：若传入的tcr_id值为0，则为默认为用户最近的培训中心；若为其他值，则为该培训中心本身的tcr_id
	 * @param param URL参数信息
	 * @param itmDir upload path of item
	 * @return 学员在某一培训中心下的某一目录(包括子目录)中的所有可见课程的集合
	 * @throws SQLException
	 */
	public Vector getMyViewCoursesByPagination(Connection con, long usr_ent_id, long tcr_id,
			String inLang, BaseParam param, String itmDir)
		throws SQLException {

		// 获取学员在某一培训中心下的所有已发布的顶层目录(包括子目录)中的所有可见课程
	    Vector myViewCoursesByPagination = new Vector();
		Vector myAllViewCourseVector = null;
		if (tcr_id == -1) {
		    myAllViewCourseVector = getAllMySharedCourses(con, inLang, param, itmDir);
		    return myAllViewCourseVector;
		} else {
		    myAllViewCourseVector = getAllMyViewCourses(con, usr_ent_id, tcr_id, inLang, param, false, itmDir);
		    CourseModuleParam courseModuleParam = (CourseModuleParam)param;

	        int size = 0;
	        if (myAllViewCourseVector != null &&  myAllViewCourseVector.size() > 0) {
	            size = myAllViewCourseVector.size();        // 所有可见课程的总数

	            for (int count = 0; count < size; count++) {
	                int end = (courseModuleParam.getStart() + courseModuleParam.getLimit() - 1);

	                // 采用假分页技术(所有数据库都通用)
	                if(count >= courseModuleParam.getStart() && count <= end) {
	                    myViewCoursesByPagination.add(myAllViewCourseVector.get(count));
	                }
	            } // end for
	            courseModuleParam.setTotal_rec(size);   // 设置总记录数
	        }
	        return myViewCoursesByPagination;
		}
	}

	/**
	 * 获取学员在某一培训中心下的所有已发布的顶层目录(包括子目录)中的所有可见课程
	 * 实现思路：通过用户ID->培训中心->培训中心下的顶层目录->顶层目录在aeTreeNode表中的tnd_id字段值
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param tcr_id 培训中心ID(对应tcTrainingCenter表中的tcr_id)
	 * 		  在此方法中约定：若传入的tcr_id值为0，则为默认为用户最近的培训中心；若为其他值，则为该培训中心本身的tcr_id
	 * @param param URL参数信息
	 * @param isShowAmount 是否在目录名后显示可见课程的数量，以用于前台显示(true：追加，false：不追加)
	 * @param itmDir upload path of item
	 * @return 学员在某一培训中心下的某一目录(包括子目录)中的所有可见课程的集合
	 * @throws SQLException
	 */
	public Vector getAllMyViewCourses(Connection con, long usr_ent_id, long tcr_id,
			String inLang, BaseParam param, boolean isShowAmount, String itmDir)
		throws SQLException {

		Vector topCatBeanVector = null;		// 顶层目录集

		Vector courseVector = new Vector();	// 课程集

		// 若默认为用户最近培训中心(即若传入的tcr_id值为0)
		if (tcr_id == 0) {
			tcr_id = getMyNearTrainingCenter(con, usr_ent_id, param);	// 查找用户最近的培训中心所对应的tcr_id值
		}

		// 获取与该培训中心相关的所有已发布的顶层目录的集合
		if (isShowAmount) {
			topCatBeanVector = getTopCatalogBeans(con, usr_ent_id, tcr_id, false, true, 0);
		} else {
			topCatBeanVector = getTopCatalogBeans(con, usr_ent_id, tcr_id, false, false, 0);
		}

		// 依次获取每一个顶层目录下(包括该顶层目录的子目录)的所有用户可见的课程

		if(topCatBeanVector.size() > 0){
			long[] tnd_id_array = new long[topCatBeanVector.size()];
			for (int i = 0; i < topCatBeanVector.size(); i++) {
				tnd_id_array[i] = ((CatalogBean)topCatBeanVector.get(i)).getId();	// 每一个顶层目录对应的aeTreeNode表中的tnd_id字段
			}
			courseVector = this.getViewCoursesOfOneCatalog(con, usr_ent_id, inLang, itmDir, tnd_id_array);
		}
		return courseVector;
	}

   public Vector getAllMySharedCourses(Connection con, String inLang, BaseParam param, String itmDir)
        throws SQLException {
/*        int cnt = getSharedCoursesCnt(con, inLang, itmDir);
        param.setTotal_rec(cnt);*/
        Vector cos_vec = getSharedCourses(con, param, null, inLang, itmDir);
        return cos_vec;
    }
	/**
	 * 查找与用户相关的总培训中心ID号
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param param URL参数信息
	 * @return 总培训中心ID号
	 * @throws SQLException
	 */
	public long getMyHQTrainingCenter(Connection con, long usr_ent_id, BaseParam param)
		throws SQLException {

		Vector myTrainingCenterVector = this.getMyTrainingCenter(con, usr_ent_id, param);// 获取与用户相关的培训中心集合

		long tcr_id = 0;

		// 查找总培训中心所对应的tcr_id值
		for (int i = 0; i < myTrainingCenterVector.size(); i++) {
			TCBean tcBean = (TCBean)myTrainingCenterVector.get(i);
			if (tcBean.getTcr_parent_tcr_id() == 0)	{	// 若为总培训中心
				tcr_id = tcBean.getTcr_id();	// 则记下总培训中心的tcr_id
				break;
			}
		}

		return tcr_id;
	}

	/**
	 * 查找与用户相关的最近的培训中心ID号
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param param URL参数信息
	 * @return 总培训中心ID号
	 * @throws SQLException
	 */
	public long getMyNearTrainingCenter(Connection con, long usr_ent_id, BaseParam param)
		throws SQLException {

		Vector myTrainingCenterVector = this.getMyTrainingCenter(con, usr_ent_id, param);// 获取与用户相关的培训中心集合

		long tcr_id = 0;

		if (myTrainingCenterVector.size() > 0) {
			TCBean tcBean = (TCBean)myTrainingCenterVector.get(0);
			tcr_id = tcBean.getTcr_id();
		}

		return tcr_id;
	}

	/**
	 * 按要求获取某个给定的目录的目录树(注：子目录是已发布的，遍历时不包括目录中的子课程)对象
	 * @param con 数据库连接器
	 * @param long usr_ent_id 学员ID
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @param floor_num 要显示的目录树层数(当为0时，则遍历所有层次的子目录)
	 * @param level 层次(约定初始值一般为1)
	 * @param isShowAmount 是否在目录名后显示可见课程的数量，以用于前台显示(true：追加，false：不追加)
	 * @return 以该目录为根结点的目录树(不包括子课程)对象
	 * @throws SQLException
	 */
	public CatalogBean viewCatalogs(Connection con, long usr_ent_id, boolean needTcrId, long tnd_id, int floor_num, int level, boolean isShowAmount)
		throws SQLException {

		// 获取该目录结点对象
		CatalogBean catalogBean = this.getCatalogById(con, usr_ent_id, needTcrId, tnd_id, isShowAmount);

		// 生成以该目录为根结点的目录树对象
		if (catalogBean != null) {
			this.treeCatalogs(con, usr_ent_id, tnd_id, catalogBean, floor_num, level, isShowAmount);
		}

		return catalogBean;
	}

	public CatalogBean viewCatalogs(Connection con ,long usr_ent_id, boolean needTcrId, long tnd_id,int floor_num, int level,boolean isShowAmount, boolean allSubCata) throws SQLException{
		CatalogBean catalogBean = this.getCatalogById(con, usr_ent_id, needTcrId, tnd_id, isShowAmount);
		if (catalogBean != null) {
			Vector allSubVc= new Vector();
			this.treeCatalogs(con, usr_ent_id, tnd_id, catalogBean, floor_num, level, isShowAmount,allSubCata,allSubVc);
			catalogBean.setChildren(allSubVc);
		}
		return catalogBean;

	}

	/**
	 * 根据目录的tnd_id获取该目录结点对象
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @return 目录Bean
	 * @throws SQLException
	 */
	public CatalogBean getCatalogById(Connection con, long tnd_id) throws SQLException {

		// 获取该目录的一些信息SQL
		String sql = "select tnd_id, tnd_title from aeTreeNode where tnd_id=?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		CatalogBean catalogBean = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tnd_id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				catalogBean = new CatalogBean();

				catalogBean.setTnd_id(rs.getLong("tnd_id"));
				catalogBean.setId(rs.getLong("tnd_id"));
				catalogBean.setTnd_title(rs.getString("tnd_title"));
				catalogBean.setText(rs.getString("tnd_title"));
			}

		} catch (SQLException se) {
			System.err.println("[Get_Catalog_By_Id_Exception]: " + se.getMessage());
			CommonLog.error("[Get_Catalog_By_Id_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return catalogBean;
	}

	/**
	 * 根据目录的tnd_id获取该目录结点对象
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param needTcrId 是否需要在结点中加入相关的培训中心ID信息
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @param isShowAmount 是否在目录名后显示可见课程的数量，以用于前台显示(true：追加，false：不追加)
	 * @return 目录Bean
	 * @throws SQLException
	 */
	public CatalogBean getCatalogById(Connection con, long usr_ent_id, boolean needTcrId, long tnd_id, boolean isShowAmount) throws SQLException {

		// 获取该目录的一些信息SQL
		String sql = "select tnd_id, tnd_title from aeTreeNode where tnd_id=?";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		CatalogBean catalogBean = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tnd_id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				catalogBean = new CatalogBean();

				if (needTcrId == true) {
					catalogBean.setTcr_id(this.getTcByTndId(con, tnd_id).getTcr_id());					// 培训中心ID
				}
				catalogBean.setTnd_id(rs.getLong("tnd_id"));	// 目录ID(对应aeTreeNode表中的tnd_id)
				catalogBean.setId(rs.getLong("tnd_id"));		// 目录ID(对应aeTreeNode表中的tnd_id)，用于前台展现
				catalogBean.setText(rs.getString("tnd_title"));	// 目录名
				catalogBean.setTnd_title(rs.getString("tnd_title"));	// 目录名，用于前台展现
				catalogBean.setCount(this.getViewCosAmountOfOneCat(con, usr_ent_id, tnd_id));	// 该目录下(包括其子孙目录)的所有用户可见课程的数量

				if (isShowAmount == true) {
					if (catalogBean.isAlreadyShowed() == false) {
						catalogBean.setText(catalogBean.getText() /*+ "  (" + catalogBean.getCount() + ")"*/);
						catalogBean.setAlreadyShowed(true);
					}
				}

				if (this.isLeaf(con, tnd_id)) {	// 若是叶子结点(注：这里的叶子结点的概念，即某一目录下只要不存在已发布的子目录(该目录可以包括子课程也可以不包括子课程)，则该目录就是叶子结点)
					catalogBean.setLeafage(true);
				} else {// 若非叶子结点
					catalogBean.setLeafage(false);
				}
			}

		} catch (SQLException se) {
			System.err.println("[Get_Catalog_By_Id_Exception]: " + se.getMessage());
			CommonLog.error("[Get_Catalog_By_Id_Exception]: " +se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return catalogBean;
	}

	/**
	 * 根据目录的tnd_id获取培训中心Bean
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID
	 * @return 培训中心Bean
	 * @throws SQLException
	 */
	public TrainingCenterBean getTcByTndId(Connection con, long tnd_id) throws SQLException {

		// 查询某一目录所属的培训中心SQL
		String sql = "select tcr_id, tcr_code, tcr_title "
			+ " from aeTreeNode, "	// 目录课程结点表
			+ " aeCatalog, "		// 顶层目录表
			+ " tcTrainingCenter "	// 培训中心表
			+ " where tnd_id=? "	// 传入：某一目录ID
			+ " and tnd_cat_id=cat_id "
			+ " and cat_tcr_id=tcr_id ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		TrainingCenterBean tcBean = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tnd_id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				tcBean = new TrainingCenterBean();

				tcBean.setTcr_id(rs.getLong("tcr_id"));
				tcBean.setTcr_title(rs.getString("tcr_title"));
			}

		} catch (SQLException se) {
			System.err.println("[Get_TC_By_TndId_Exception]: " + se.getMessage());
			CommonLog.error("[Get_TC_By_TndId_Exception]: " +se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return tcBean;
	}

	/**
	 * 递归遍历给定的某个目录的目录树(注：子目录是已发布的，遍历时不包括目录中的子课程)
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param tcr_id 培训中心ID
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @param catalogBean 目录Bean，以供JSON输出(注：该变量用于存放遍历后的结点树，因此，在传入时必须有初始化的对象被指定)
	 * @param floor_num 要显示的目录树层数(当为0时，则遍历所有层次的子目录)
	 * @param level 层次，可以用于前台显示的缩进(约定初始值一般为1)
	 * @param isShowAmount 是否在目录名后显示可见课程的数量，以用于前台显示(true：追加，false：不追加)
	 * @throws SQLException
	 */
	public void treeCatalogs(Connection con, long usr_ent_id, long tnd_id,
			CatalogBean catalogBean, final int floor_num, int level,
			boolean isShowAmount) throws SQLException {

//		if (limitLevel != 0) {	// 若非遍历所有层次的子目录
//			if (level < limitLevel) {
//				// 继续以下流程
//			} else {
//				// 返回
//			}
//		}

		if (floor_num != 0) {	// 若非遍历所有层次的子目录
			if (level >= floor_num) {
				return;
			}
		}

		// 查询某一目录下的所有已发布的一级子目录SQL
		String sql = "select treeNode.tnd_id, treeNode.tnd_title "	// 目录在aeTreeNode结点树表中的tnd_id，目录名
			+ " from aeTreeNodeRelation treeNodeR, "	// 用来记录目录与目录、目录与课程的关系表
			+ " 	 aeTreeNode treeNode "			// 课程与目录的关系结点树
			+ " where treeNodeR.tnr_ancestor_tnd_id=? "	// 传入：某一目录ID(aeTreeNode表的tnd_id字段)
			+ " 	  and treeNodeR.tnr_child_tnd_id=treeNode.tnd_id "
			+ " 	  and tnr_parent_ind=1 "
			+ " 	  and treeNodeR.tnr_type='TND_PARENT_TND' "
			+ " 	  and treeNode.tnd_status='ON' "				// 该目录是已发布的;
			+ " 	  order by treeNode.tnd_title ";


		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tnd_id);

			// 查询所有父结点都是tnd_id的所有子结点
			rs = pstmt.executeQuery();

			// 遍历
			while (rs.next()) {
				if (catalogBean.getChildren() == null) {
					catalogBean.setChildren(new Vector());
					catalogBean.setExpanded(true);	// 若该目录只要有children，则将该目录设置为可以展开
				}

				// 创建该子结点对象
				CatalogBean subCatalogBean = new CatalogBean();
				subCatalogBean.setId(rs.getLong("tnd_id"));		// 目录ID(对应aeTreeNode表的tnd_id)，用于前台展现
				subCatalogBean.setTnd_id(rs.getLong("tnd_id"));	// 目录ID(对应aeTreeNode表的tnd_id)
				subCatalogBean.setText(rs.getString("tnd_title"));		// 目录名，用于前台展现
				subCatalogBean.setTnd_title(rs.getString("tnd_title"));	// 目录名
				subCatalogBean.setTcr_id(catalogBean.getTcr_id());		// 培训中心ID
				subCatalogBean.setCount(this.getViewCosAmountOfOneCat(con, usr_ent_id, subCatalogBean.getId()));// 该目录下(包括其子孙目录)的所有用户可见课程的数量

				if (isShowAmount == true) {
					if (subCatalogBean.isAlreadyShowed() == false) {	// 若该目录名后已设置数量
						subCatalogBean.setText(subCatalogBean.getText()/* + "  (" + subCatalogBean.getCount() + ")"*/);
						subCatalogBean.setAlreadyShowed(true);
					}
				}

				// 将该子结点加入到父结点的子结点集合中
				catalogBean.getChildren().add(subCatalogBean);

				// 判断该子结点是否是叶子结点(注：这里的叶子结点的概念，即某一目录下只要不存在已发布的子目录(该目录可以包括子课程也可以不包括子课程)，则该目录就是叶子结点)
				if (isLeaf(con, subCatalogBean.getId())) {	// 若是叶子结点
					subCatalogBean.setLeafage(true);
				} else {// 若非叶子结点，则继续遍历
					subCatalogBean.setLeafage(false);

					treeCatalogs(con, usr_ent_id, subCatalogBean.getId(), subCatalogBean, floor_num, level + 1, isShowAmount);
				}

			} // end while
		} catch(SQLException se) {
			System.err.println("[Tree_Catalogs_Exception]: " + se.getMessage());
			CommonLog.error("[Tree_Catalogs_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

	}

	public void treeCatalogs(Connection con, long usr_ent_id, long tnd_id,
			CatalogBean catalogBean, final int floor_num, int level,
			boolean isShowAmount,boolean allSubCata, Vector allSubVc) throws SQLException {
		String sql = "select treeNode.tnd_id, treeNode.tnd_title "	// 目录在aeTreeNode结点树表中的tnd_id，目录名
			+ " from aeTreeNodeRelation treeNodeR, "	// 用来记录目录与目录、目录与课程的关系表
			+ " 	 aeTreeNode treeNode "			// 课程与目录的关系结点树
			+ " where treeNodeR.tnr_ancestor_tnd_id=? "	// 传入：某一目录ID(aeTreeNode表的tnd_id字段)
			+ " 	  and treeNodeR.tnr_child_tnd_id=treeNode.tnd_id "
			+ " 	  and tnr_parent_ind=1 "
			+ " 	  and treeNodeR.tnr_type='TND_PARENT_TND' "
			+ " 	  and treeNode.tnd_status='ON' "				// 该目录是已发布的;
			+ " 	  order by treeNode.tnd_title ";


		PreparedStatement pstmt = null;
		ResultSet rs = null;
		pstmt = con.prepareStatement(sql);
		pstmt.setLong(1, tnd_id);
		// 查询所有父结点都是tnd_id的所有子结点
		rs = pstmt.executeQuery();
		// 遍历
		while (rs.next()) {
			// 创建该子结点对象
			CatalogBean subCatalogBean = new CatalogBean();
			subCatalogBean.setId(rs.getLong("tnd_id"));		// 目录ID(对应aeTreeNode表的tnd_id)，用于前台展现
			subCatalogBean.setTnd_id(rs.getLong("tnd_id"));	// 目录ID(对应aeTreeNode表的tnd_id)
			subCatalogBean.setText(rs.getString("tnd_title"));		// 目录名，用于前台展现
			subCatalogBean.setTnd_title(rs.getString("tnd_title"));	// 目录名
			subCatalogBean.setTcr_id(catalogBean.getTcr_id());		// 培训中心ID
			subCatalogBean.setCount(this.getViewCosAmountOfOneCat(con, usr_ent_id, subCatalogBean.getId()));// 该目录下(包括其子孙目录)的所有用户可见课程的数量
			if (isShowAmount == true) {
				if (subCatalogBean.isAlreadyShowed() == false) {	// 若该目录名后已设置数量
					subCatalogBean.setText(subCatalogBean.getText()/* + "  (" + subCatalogBean.getCount() + ")"*/);
					subCatalogBean.setAlreadyShowed(true);
				}
			}

			// 将该子结点加入到父结点的子结点集合中
			//catalogBean.getChildren().add(subCatalogBean);

			// 判断该子结点是否是叶子结点(注：这里的叶子结点的概念，即某一目录下只要不存在已发布的子目录(该目录可以包括子课程也可以不包括子课程)，则该目录就是叶子结点)

			allSubVc.add(subCatalogBean);
			if (isLeaf(con, subCatalogBean.getId())) {	// 若是叶子结点
				subCatalogBean.setLeafage(true);
			} else {// 若非叶子结点，则继续遍历
				subCatalogBean.setLeafage(false);
				treeCatalogs(con, usr_ent_id, subCatalogBean.getId(), subCatalogBean, floor_num, level + 1, isShowAmount,true,allSubVc);
			}

		} 
		cwSQL.cleanUp(rs, pstmt);
		// end while
	}

	/**
	 * 判断某个目录是否是叶子结点(即某一目录下只要不存在已发布的子目录(该目录可以包括子课程也可以不包括子课程)，则该目录就是叶子结点)
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @return 是否是叶子结点
	 * @throws SQLException
	 */
	public boolean isLeaf(Connection con, long tnd_id) throws SQLException {

		// 查询某一目录下的所有已发布的一级子目录的数量SQL，用以判断该目录是否是叶子结点
		// 注，这里的叶子结点的概念是：目录下只要不存在已发布的子目录(该目录可以包括子课程也可以不包括子课程)，则该目录就是叶子结点
		String sql = "select count(*) "
			+ " from aeTreeNodeRelation treeNodeR, "	// 用来记录目录与目录、目录与课程的关系表
			+ " 	 aeTreeNode treeNode "			// 课程与目录的关系结点树
			+ " where treeNodeR.tnr_ancestor_tnd_id=? "	// 传入：某一目录ID(aeTreeNode表的tnd_id字段)
			+ " 	  and treeNodeR.tnr_child_tnd_id=treeNode.tnd_id "
			+ " 	  and tnr_parent_ind=1 "
			+ " 	  and treeNodeR.tnr_type='TND_PARENT_TND' "
			+ " 	  and treeNode.tnd_status='ON' ";	// 该目录是已发布的

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int count = 0;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tnd_id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}

		} catch (SQLException se) {
			System.err.println("[IS_Leaf_Exception]: " + se.getMessage());
			CommonLog.error("[IS_Leaf_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return (count > 0 ? false : true);
	}

	/**
	 * 获取某一培训中心下的所有已发布的顶层目录(注：这个(些)目录是已发布的)集合
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param tcr_id 培训中心ID(对应tcTrainingCenter表中的tcr_id)
	 * @param limit TODO
	 * @param boolean isLazy 是否设置每个顶层目录下的所有可见课程数(true：不设置，false：设置)
	 * @param boolean isShowAmount 是否在目录名后显示可见课程的数量，以用于前台显示(true：追加，false：不追加)
	 * @return 封装了与某一培训中心相关的所有已发布的顶层目录对应的aeTreeNode表的tnd_id值的集合
	 * @throws SQLException
	 */
	public Vector getTopCatalogBeans(Connection con, long usr_ent_id, long tcr_id, boolean isLazy, boolean isShowAmount, long limit) throws SQLException {
		// 查询某一培训中心下的所有顶层目录(注：这个(些)目录是已发布的)在aeTreeNode表中对应的tnd_id的SQL
		String sql = " select cat.cat_id, cat.cat_tcr_id, treeNode.tnd_id, treeNode.tnd_title ";
		sql += " from tcTrainingCenter tCenter, ";
		sql += "		 aeCatalog cat, ";
		sql += " 	 aeTreeNode treeNode ";
		sql += " where cat.cat_tcr_id=? "; // 传入：培训中心tcTrainingCenter表的主键tcr_id
		sql += "	  and tCenter.tcr_id=cat.cat_tcr_id ";
		sql += " 	  and cat_status='ON' ";
		sql += " 	  and treeNode.tnd_parent_tnd_id is null ";
		sql += "	  and treeNode.tnd_cat_id=cat.cat_id ";
		sql += " order by tnd_title ";

		Vector topCatalogBeanVector = new Vector();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tcr_id);

			rs = pstmt.executeQuery();
			CatalogBean catBean = null;
			long count =0;
			while (rs.next()) {
				catBean = new CatalogBean();
				catBean.setId(rs.getLong("tnd_id"));			// 目录ID
				catBean.setTnd_id(rs.getLong("tnd_id"));		// 目录ID(用于前台目录树展现)
				catBean.setText(rs.getString("tnd_title"));		// 目录名
				catBean.setTnd_title(rs.getString("tnd_title"));// 目录名(用于前台目录树展现)
				catBean.setTcr_id(rs.getLong("cat_tcr_id"));	// 目录所属的培训中心ID
				if (isLazy == false) {
					// 设置该目录下的所有可见课程数
					catBean.setCount(this.getViewCosAmountOfOneCat(con, usr_ent_id, catBean.getId()));

					if (isShowAmount == true) {
						if (catBean.isAlreadyShowed() == false) {
							catBean.setText(catBean.getText() /*+ "  (" + catBean.getCount() + ")"*/);
							catBean.setAlreadyShowed(true);
						}
					}
				}
				topCatalogBeanVector.add(catBean);
				count++;
				if (limit > 0 && count >= limit)
				    break;
			} // end while

		} catch(SQLException se) {
			System.err.println("[Get_Top_CatalogBeans_Exception]: " + se.getMessage());
			CommonLog.error("[Get_Top_CatalogBeans_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return topCatalogBeanVector;
	}

	/**
	 * 获取某一目录下(包括其子孙目录)的所有用户可见的课程
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @param usr_ent_id 用户ID
	 * @param param URL参数信息
	 * @param itmDir upload path of item
	 * @return 某一目录下(包括其子孙目录)的所有用户的可见课程集
	 * @throws SQLException
	 */
	public Vector getViewCoursesOfOneCatalog(Connection con, long usr_ent_id, String inLang, String itmDir, long[] tnd_id_array)
		throws SQLException {
		String sql = getViewCoursesOfOneCatalogSql(false, true, tnd_id_array);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Vector courseVector = new Vector();
		try {
			pstmt = con.prepareStatement(sql);
			int index =1;
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);

			rs = pstmt.executeQuery();
			while (rs.next()) {
			    transDatatoBean(con, rs, courseVector, itmDir,inLang);
			}
			return courseVector;
		} catch(SQLException se) {
			System.err.println("[Get_View_Courses_Of_One_Catalog_Exception]: " + se.getMessage());
			CommonLog.error("[Get_View_Courses_Of_One_Catalog_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			if (pstmt != null) pstmt.close();
		}
	}

	/**
	 * get shared item with required information in vector
	 * @param con
	 * @param inLang
	 * @param itmDir
	 * @return
	 * @throws SQLException
	 */
	public Vector getSharedCourses(Connection con, BaseParam param, HashMap srh, String inLang, String itmDir)
    throws SQLException {
        String sql = getSharedCoursesSql(false, srh);
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Vector courseVector = new Vector();
        try {
            pstmt = con.prepareStatement(sql);
            int idx = 1;
            pstmt.setBoolean(idx++, true);
            pstmt.setString(idx++, aeItem.ITM_STATUS_ON);
            if (srh != null) {
                if (srh.get("srh_key")!= null && ((String[])srh.get("srh_key")).length > 0
                        && ((String[])srh.get("srh_key"))[0].length() > 0) {
                    pstmt.setString(idx++, "%"+((String[])srh.get("srh_key"))[0]+"%");
                }
                String srh_start_period = (String)srh.get("srh_start_period");
                if (IMMEDIATE.equalsIgnoreCase(srh_start_period)){
                    pstmt.setString(idx++, aeItem.ITM_STATUS_ON);
                    pstmt.setTimestamp(idx++, param.getCur_time());
                    pstmt.setTimestamp(idx++, param.getCur_time());
                } else {
                    Timestamp[] start_end_datetime = getsrhStartEndTime(srh, param.getCur_time());
                    Timestamp start_datetime = start_end_datetime[0];
                    Timestamp end_datetime = start_end_datetime[1];

                    if (start_datetime != null || end_datetime != null) {
                        pstmt.setString(idx++, aeItem.ITM_STATUS_ON);
                        pstmt.setTimestamp(idx++, start_datetime);
                        pstmt.setTimestamp(idx++, end_datetime);
                        pstmt.setTimestamp(idx++, start_datetime);
                        pstmt.setTimestamp(idx++, end_datetime);
                    }
                }
            }
            rs = pstmt.executeQuery();
            int count = 0;
            int end = (param.getStart() + param.getLimit() - 1);
            while(rs.next()) {
                if(count >= param.getStart() && count <= end) {
                    transDatatoBean(con, rs, courseVector, itmDir,inLang);
                }
                count++;
            }
            param.setTotal_rec(count);
            return courseVector;
        } catch(SQLException se) {
            CommonLog.error( se.getMessage(),se);
            throw se;
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

	public void transDatatoBean(Connection con, ResultSet rs, Vector outdata,String itmDir, String inLang) throws SQLException {
        CourseBean courseBean = new CourseBean();

        courseBean.setItm_id(rs.getLong("itm_id"));         // 课程ID
        courseBean.setItm_title(rs.getString("itm_title")); // 课程名
        courseBean.setItm_type(rs.getString("itm_type"));   // 课程类型
        courseBean.setItm_desc(rs.getString("itm_desc"));

        String itmIcon = rs.getString("itm_icon");
        String itmIconPath = Course.getItmIconPath(itmDir, rs.getLong("itm_id"), itmIcon);
        courseBean.setItm_icon(itmIconPath);
        float vag_score = rs.getFloat("itm_comment_avg_score");
        courseBean.setItm_comment_avg_score(Math.round(vag_score));
        boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
        boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
        boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
        boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");

        String itmDummyType = aeItemDummyType.getDummyItemType(courseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
        courseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, itmDummyType));
        courseBean.setItm_dummy_type(itmDummyType);

        if (itm_create_run_ind) {
            if (!itm_blend_ind) {   // 若是离线课程或离线考试，获取其对应的班级的开课时间
                courseBean.setRecent_start_classes(this.getStartDateOfClass(con, courseBean.getItm_id()));
            } else {    // 若是混合课程或混合考试，获取其对应的班级的开课时间
                courseBean.setRecent_start_classes(this.getStartDateofBlendItem(con, courseBean.getItm_id()));
            }
        }
        outdata.add(courseBean);
	}
	/**
	 * 查询某一目录下(包括其子孙目录)的所有用户的可见课程的SQL
	 * @param isCalcAmount 是否要计算某一目录下(包括其子孙目录)的所有用户的可见课程的数量
	 * @return 某一目录下(包括其子孙目录)的所有用户的可见课程的SQL语句
	 */
	public String getViewCoursesOfOneCatalogSql(boolean isCalcAmount, boolean isAppend, long[] tnd_id_array) {
		String appDate = null;
		String order = null;
		if(isAppend){
			appDate = " ,citm.itm_create_timestamp ";
			order = " order by citm.itm_create_timestamp desc ";
		}
		// 若不计算该目录下的可见课程的数量，则不能有treeNode.tnd_id，
		// 这是由于前台在课程列表栏显示某个目录(包括其子目录)下的课程时，不能出现重复的课程(即使是同一门课程发布该目录下的不同子目录中时的情况)
		String subSql = " select distinct ";

		// 若要计算该目录下的可见课程的数量
		// 可能出现的情况是，在某个目录下的多个子目录中发布有相同的课程，就不能注释掉treeNode.tnd_id
		if (isCalcAmount == true) {
			subSql = " select distinct treeNode.tnd_id, ";
		}

		// 查询某一目录下(包括其子孙目录)的所有用户的可见课程的SQL
		String sql = subSql + " citm.itm_id, citm.itm_title, citm.itm_type, citm.itm_icon, "	// 课程所在结点树的ID(去掉)，课程ID，课程名，课程类型，课程图标
			+ " citm.itm_desc, citm.itm_comment_avg_score, "	// 课程描述，课程评分
			+ " citm.itm_create_run_ind, citm.itm_exam_ind, citm.itm_blend_ind, citm.itm_ref_ind "	// 是否可以创建班级、是否是考试、是否是混合、是否是参考
			+ " from aeTreeNodeRelation treeNodeR "		// 用来记录目录与目录、目录与课程的关系表
			+ " inner join aeTreeNode treeNode on treeNodeR.tnr_child_tnd_id=treeNode.tnd_id "	// 课程与目录的关系结点树
			+ " inner join aeItem citm on treeNode.tnd_itm_id=citm.itm_id "	// 课程(或班级)表
//			// 加入班级与离线课程的关系表，以方便查找离线课程对应的班级的开课时间
//			+ " left join aeItemRelation citmR on citm.itm_id=citmR.ire_parent_itm_id "
//			+ " left join aeItem citm2 on citm2.itm_id=citmR.ire_child_itm_id "
			// 加入顶层目录表
			+ " left join aeCatalog cat on treeNode.tnd_cat_id=cat.cat_id "
			+ " left join aeTreeNode treeNode2 on ( "
			+ " 		treeNode2.tnd_parent_tnd_id is null "
			+ " 		and cat.cat_id = treeNode2.tnd_cat_id "
			+ " ) "
			// 加入目录表
			+ " left join aeTreeNode treeNode3 on treeNodeR.tnr_ancestor_tnd_id = treeNode3.tnd_id "
			+ " where treeNodeR.tnr_ancestor_tnd_id IN  " + cwUtils.vector2list(cwUtils.long2vector(tnd_id_array)) 	// 传入：某一或多个目录ID(aeTreeNode表的tnd_id字段)
			+ " 	  and treeNodeR.tnr_type='ITEM_PARENT_TND' "
//			+ " 	  and citm.itm_status='ON' "	// 课程是发布的
			+ " 	  and citm.itm_status='ON' and cat.cat_status = 'ON' and ((treeNode3.tnd_type = 'NORMAL' and treeNode3.tnd_status = 'ON') or (treeNode3.tnd_type = 'CATALOG' and treeNode3.tnd_status is null)) "// 课程是发布的、且其顶层目录是发布的、且其所在的目录及上层目录是发布的
			// 加入课程是对所有学员发布的或者是对目标学员发布的(若是对目标学员发布的，则要判断目标学员所属的用户组或职务是否符合规则)
			+ " and (citm.itm_access_type='ALL' "
			+ "	     or (citm.itm_access_type='TARGET_LEARNER' "
			+ "	     	 and exists (select * "
			+ " 	 			   	 from aeItemTargetRuleDetail "
															// 精确查找学员所在的用户组ID
		    + "            	   		 where ird_group_id in (select ern_ancestor_ent_id "// 用户组ID(对应Entity表的ent_id)
			+ "										  		from EntityRelation	"		// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
			+ "										  		where ern_child_ent_id=? "	// 传入：学员的ID(RegUser表中的usr_ent_id)
//			+ "										  	    	  and ern_parent_ind=1 "// 限定只查找学员所属的父组
			+ "											    	  and ern_type='USR_PARENT_USG' "
		    + "            	 					     	   ) "
		    													// 精确查找学员所属的职务ID
		    + "                 	       and ird_grade_id in (select ern_ancestor_ent_id "// 职务ID(对应Entity表的ent_id)
			+ "						 					  	  from EntityRelation "			// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
			+ "											  	  where ern_child_ent_id=? "	// 传入：学员的ID(RegUser表中的usr_ent_id)
//			+ "												    	and ern_parent_ind=1 "	// 限定只查找学员所属的职务
			+ "												   	 	and ern_type='USR_CURRENT_UGR' "
			+ "										     	 ) "
		    + "                 	       and ird_itm_id = citm.itm_id "
//		    + "                  	       and citm.itm_access_type='TARGET_LEARNER' "
		    + " 						   and (ird_upt_id = -1 "
			+ "									or ird_upt_id in ( "
			+ "										select upr_upt_id "
			+ "										from UserPositionRelation "
			+ "										where upr_usr_ent_id = ? "

			+ "									) "
			+ " 						  ) "
		    + "	         		    ) "
			+ " 	  	) "
			+ "  	) ";

		return sql;
	}

    public String getSharedCoursesSql(boolean cnt, HashMap srh) {
        StringBuffer sql = new StringBuffer();
        if (cnt == true) {
            sql.append("select count(*) ");
        } else {
            sql.append("select pitm.itm_id, pitm.itm_title, pitm.itm_type, pitm.itm_icon, ")
            .append(" pitm.itm_desc, pitm.itm_comment_avg_score, ")
            .append(" pitm.itm_create_run_ind, pitm.itm_exam_ind, pitm.itm_blend_ind, pitm.itm_ref_ind ");
        }
        sql.append(" from aeItem pitm where pitm.itm_share_ind = ? and pitm.itm_status = ?");
        if (srh != null) {
            String[] srh_key = null;
            if (srh.get("srh_key")!= null && ((String[])srh.get("srh_key")).length > 0
                    && ((String[])srh.get("srh_key"))[0].length() > 0) {    // 获取关键词
                srh_key = (String[])srh.get("srh_key");
            }
            String srh_itm_type_lst = (String)srh.get("srh_itm_type_lst");    // 课程类型选项
            String srh_start_period = (String)srh.get("srh_start_period"); // 开课日期时间期限
            String srh_key_type = "TITLE";      // 默认为非全文搜索
            if (srh.get("srh_key_type") != null) { // 获取全文搜索(可选值："TITLE"、"FULLTEXT")
                srh_key_type = (String)srh.get("srh_key_type");
            }
            if (srh_key != null && srh_key.length == 1) {
                 if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {       // 若是全文搜索
                     sql.append(" and pitm.itm_srh_content like ? ");
                 } else if ("TITLE".equalsIgnoreCase(srh_key_type)) {   // 若非全文搜索
                     sql.append(" and pitm.itm_title like ? ");
                 }
            } else if (srh_key != null && srh_key.length > 1) {
                sql.append(" and ( ");
                int keyCount = 1;
                for (int i = 0; i < srh_key.length; i++) {
                    // 加入第i个key
                    if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {        // 若是全文搜索
                        sql.append(" pitm.itm_srh_content like ? ");
                    } else if ("TITLE".equalsIgnoreCase(srh_key_type)) {    // 若非全文搜索
                        sql.append(" pitm.itm_title like ? ");
                    }
                    keyCount++;
                    if (keyCount <= srh_key.length) {
                        sql.append(" or ");
                    }
                } // end for
                sql.append( " ) ");
            }

            // 加入课程类型的判断 and (pitm.itm_type=... or pitm.itm_type=...)
            if (srh_itm_type_lst != null && !"".equals(srh_itm_type_lst)) {
                sql.append( aeItemDummyType.genSqlByItemDummyType(srh_itm_type_lst, "pitm", true));
            }
            String tmpsql = "";
            tmpsql += ")";
            // 加入开始日期的搜索判断
            if (IMMEDIATE.equalsIgnoreCase(srh_start_period)) {
    //        String sub1 = " (? between citm2.itm_eff_start_datetime and citm.itm_eff_end_datetime or ? between citm.itm_content_eff_start_datetime and citm.itm_content_eff_end_datetime) ";
                String sub1 =
                      " (? between case when citm.itm_eff_start_datetime < citm.itm_content_eff_start_datetime then citm.itm_eff_start_datetime "
                    + "                 else citm.itm_content_eff_start_datetime "
                    + "            end "
                    + "    and case when citm.itm_eff_end_datetime > citm.itm_content_eff_end_datetime then citm.itm_eff_end_datetime "
                    + "             else citm.itm_content_eff_end_datetime "
                    + "        end"
                    + " ) ";
                // 面授课程(考试)
                String sub2 = " ? between citm.itm_eff_start_datetime and citm.itm_eff_end_datetime ";
                // 混合课程(考试)的处理
                sql.append(" and (exists(select * from aeItemRelation, aeItem citm where ire_parent_itm_id = pitm.itm_id and ire_child_itm_id = citm.itm_id and citm.itm_status=?");
                sql.append(" and ( (citm.itm_blend_ind = 1 and citm.itm_run_ind = 1 and " + sub1 + ") ");
                // 面授课程(考试)的处理
                sql.append( " or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 1 and " + sub2 + ")) ");
                sql.append(")");
                // 网上课程(考试)的处理,选择即时开始时可以搜索出网上课程
                sql.append( " or (pitm.itm_blend_ind = 0 and pitm.itm_run_ind = 0 and pitm.itm_create_run_ind = 0 and pitm.itm_ref_ind = 0 )");
                sql.append(")");
            } else if ((srh_start_period != null && srh_start_period.length() > 0 && !UNLIMITED.equalsIgnoreCase(srh_start_period))) {
                // 混合课程(考试)开课日期的条件查询子句
                String sub1 =
                      " (case when citm.itm_eff_start_datetime < citm.itm_content_eff_start_datetime then citm.itm_eff_start_datetime "
                    + "       else citm.itm_content_eff_start_datetime "
                    + "  end "
                    + "  between ? and ? "
                    + " ) ";
                // 面授课程(考试)
                String sub2 = " citm.itm_eff_start_datetime between ? and ? ";

                sql.append(" and exists(select * from aeItemRelation, aeItem citm where ire_parent_itm_id = pitm.itm_id and ire_child_itm_id = citm.itm_id and citm.itm_status=?");
                // 混合课程(考试)的处理
                sql.append( " and ((citm.itm_blend_ind = 1 and citm.itm_run_ind = 1 and " + sub1 + ") ");
                // 面授课程(考试)的处理
                sql.append( " or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 1 and " + sub2 + ")) ");
                sql.append(")");
            }
        }
        return sql.toString();
    }

	/**
	 * 分页搜索符合条件的所有的用户可见课程(课程是已发布的)
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param conditionMap 条件集
	 * @param itmDir upload path of item
	 * @return 符合条件的所有的用户可见课程(课程是已发布的)集
	 * @throws SQLException
	 * @throws cwException 
	 * @throws ParseException
	 */
	public Vector searchCourses(Connection con, WizbiniLoader wizbini, long usr_ent_id, long root_ent_id, String inLang, HashMap conditionMap, BaseParam param, String itmDir) throws SQLException, cwException {
		CourseModuleParam courseModuleParam = (CourseModuleParam) param;
		
		long[] tnd_id_lst = null;  // 目录ID
        String[] s_app = null;
        if (conditionMap.get("tnd_id_lst") != null) {
            tnd_id_lst = (long[]) conditionMap.get("tnd_id_lst");
            Vector tnd_id_vec = new Vector();
         
            
            if (tnd_id_lst != null && tnd_id_lst.length == 1 && tnd_id_lst[0] != 0) {
                tnd_id_vec.add(new Long(tnd_id_lst[0]));
            } else if (tnd_id_lst != null && tnd_id_lst.length > 1 && tnd_id_lst[0] != 0) {
                
                for (int i = 0; i < tnd_id_lst.length; i++) {
                    tnd_id_vec.add(new Long(tnd_id_lst[i]));
                }
            }
            
            if(tnd_id_vec.size() > 0){
                s_app = cwSQL.getSQLClause(con,"tmp_id",cwSQL.COL_TYPE_LONG,tnd_id_vec,0);
                conditionMap.put("tnd_id_lst_SQL", s_app[0]);
            }
        }
        
		String sql = this.getSrhCourseSql(con, conditionMap, false);

		// 获取搜索参数
		String[] srh_key = null;
		if (conditionMap.get("srh_key") != null && ((String[]) conditionMap.get("srh_key")).length > 0) { // 获取关键词
			srh_key = (String[]) conditionMap.get("srh_key");
		}

		String srh_key_type = "TITLE"; // 默认为非全文搜索
		if (conditionMap.get("srh_key_type") != null) { // 获取全文搜索(可选值："TITLE"、"FULLTEXT")
			srh_key_type = (String) conditionMap.get("srh_key_type");
		}

		String itm_code = null; // 获取课程编号
		if (conditionMap.get("itm_code") != null) {
			itm_code = (String) conditionMap.get("itm_code");
		}

		String ske_id_lst = null; // 能力
		if (conditionMap.get("ske_id_lst") != null) {
			ske_id_lst = (String) conditionMap.get("ske_id_lst");
		}

		Vector courseVector = new Vector();
		Timestamp cur_time = cwSQL.getTime(con);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int index = 1;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);

			
			// 加入搜索条件 and citm.itm_title like '%关键字%' 或 and (citm.itm_title like '%关键字1%' or citm.itm_title like '%关键字2%' or ...)
			if (srh_key != null && srh_key.length == 1) {
				 if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {		// 若是全文搜索

					 if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
					     pstmt.setString(index++, "%" + srh_key[0].toLowerCase() + "%");
                     }else{
                         pstmt.setString(index++, "%" + srh_key[0] + "%");
                     }
				 } else if ("TITLE".equalsIgnoreCase(srh_key_type)) {	// 若非全文搜索
					 pstmt.setString(index++, "%" + srh_key[0].toLowerCase() + "%");
				 }
			} else if (srh_key != null && srh_key.length > 1) {
				for (int i = 0; i < srh_key.length; i++) {
					String key = srh_key[i];

					// 加入第i个key
					if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {		// 若是全文搜索
					    if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
					        pstmt.setString(index++, "%" + key.toLowerCase() + "%");
					    }else{
					        pstmt.setString(index++, "%" + key + "%");
					    }
						
					} else if ("TITLE".equalsIgnoreCase(srh_key_type)) {	// 若非全文搜索
						pstmt.setString(index++, "%" + key.toLowerCase() + "%");
					}
				} // end for
			} // end if...else

			// 加入课程编号的判断
			if (itm_code != null && !"".equalsIgnoreCase(itm_code)) {
				pstmt.setString(index++, "%" + itm_code.toLowerCase() + "%");
			}
			if (IMMEDIATE.equalsIgnoreCase((String)conditionMap.get("srh_start_period"))) {
				pstmt.setTimestamp(index++, cur_time);
				pstmt.setTimestamp(index++, cur_time);
			} else {
				Timestamp[] start_end_datetime = getsrhStartEndTime(conditionMap, cur_time);

				Timestamp start_datetime = start_end_datetime[0];
				Timestamp end_datetime = start_end_datetime[1];

				if (start_datetime != null || end_datetime != null) {
					pstmt.setTimestamp(index++, start_datetime);
					pstmt.setTimestamp(index++, end_datetime);
					pstmt.setTimestamp(index++, start_datetime);
					pstmt.setTimestamp(index++, end_datetime);
				}
			}

			int count=0;
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int end = (courseModuleParam.getStart() + courseModuleParam.getLimit() - 1);// 每页所显示记录的最大序号

				// 采用假分页技术(所有数据库都通用)
				if (!courseModuleParam.isPaging_ind() || (count >= courseModuleParam.getStart() && count <= end)) {
					CourseBean courseBean = new CourseBean();

					courseBean.setItm_id(rs.getLong("itm_id")); // 课程ID
					courseBean.setItm_title(rs.getString("itm_title")); // 课程名
					courseBean.setItm_type(rs.getString("itm_type")); // 课程类型
					courseBean.setItm_desc(rs.getString("itm_desc")); // 课程描述
					float avg_score = rs.getFloat("itm_comment_avg_score");
					courseBean.setItm_comment_avg_score(Math.round(avg_score)); // 课程评分
					courseBean.setItm_publish_timestamp(rs.getTimestamp("itm_publish_timestamp"));
					courseBean.setItm_appn_start_datetime(rs.getTimestamp("itm_appn_start_datetime"));
					courseBean.setItm_appn_end_datetime(rs.getTimestamp("itm_appn_end_datetime"));
					courseBean.setItm_fee(rs.getFloat("itm_fee"));

					String itmIcon = rs.getString("itm_icon"); // 课程图标
					String itmIconPath = Course.getItmIconPath(itmDir, rs.getLong("itm_id"), itmIcon);
					courseBean.setItm_icon(itmIconPath);
					
					courseBean.setBtn(aeItem.itmButtonCon(con, rs.getLong("itm_id"), usr_ent_id, courseBean, 0));
					aeApplication.getApplication(con, rs.getLong("itm_id"), usr_ent_id, courseBean);

					boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
					boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
					boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
					boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");

					String itmDummyType = aeItemDummyType.getDummyItemType(courseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
					courseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, itmDummyType));
					courseBean.setItm_dummy_type(itmDummyType);

					if (itm_create_run_ind) {
						if (!itm_blend_ind) { // 若是离线课程或离线考试，获取其对应的班级的开课时间
							courseBean.setRecent_start_classes(this.getStartDateOfClass(con, courseBean.getItm_id()));
						} else { // 若是混合课程或混合考试，获取其对应的班级的开课时间
							courseBean.setRecent_start_classes(this.getStartDateofBlendItem(con, courseBean.getItm_id()));
						}
					}

					if (courseModuleParam.isNot_planed() == true) {	// 是否过滤学习计划(true:过滤 false:不过滤)
						boolean hasAddedPlan = aeLearningSoln.existSoln(con, usr_ent_id, courseBean.getItm_id(), root_ent_id);

						if (hasAddedPlan == false) {
							courseVector.add(courseBean);
						}
					} else {
						courseVector.add(courseBean);
					}
				}

				count++;
			} // end while
			courseModuleParam.setTotal_rec(count); // 设置总记录数
		} catch (SQLException se) {
			System.err.println("[Search_Courses_Exception]: " + se.getMessage());
			CommonLog.error("[Search_Courses_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			cwSQL.cleanUp(rs, pstmt);
			if(s_app != null && s_app[1] != null){
                cwSQL.dropTempTable(con,s_app[1]);       
            } 
		}
		return courseVector;
	}

	/**
	 * 获取某一培训中心下的所有已发布的顶层目录(注：这个(些)目录是已发布的)集合，同时该顶层目录(包括子目录)中可以存在也可以不存在符合搜索条件的课程
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param tcr_id 培训中心ID(对应tcTrainingCenter表中的tcr_id)
	 * @param boolean isLazy 是否设置每个顶层目录下的所有符合搜索条件的可见课程数(true：不设置，false：设置)
	 * @param boolean isShowAmount 是否在目录名后显示符合搜索条件的可见课程的数量，以用于前台显示(true：追加，false：不追加)
	 * @param conditionMap 条件集
	 * @param isShowZero 当该目录下的课程数为零时，指定是否显示此目录
	 * @param isShowTcrTitle 是否显示培训中心名
	 * @return 封装了与某一培训中心相关的所有已发布的顶层目录(该顶层目录(包括子目录)中存在搜索条件的课程)的集合
	 * @throws SQLException
	 * @throws ParseException
	 */
	public Vector getTopCatBeansForSrhCondition(Connection con, WizbiniLoader wizbini, long usr_ent_id, boolean isLazy, 
			boolean isShowAmount, HashMap conditionMap, boolean isShowZero, boolean isShowTcrTitle)
			throws SQLException {
		return getTopCatBeansForSrhCondition(con, wizbini, usr_ent_id, isLazy, isShowAmount, conditionMap, isShowZero, isShowTcrTitle, 0);
	}

	public Vector getTopCatBeansForSrhCondition(Connection con, WizbiniLoader wizbini, long usr_ent_id, boolean isLazy, 
			boolean isShowAmount, HashMap conditionMap, boolean isShowZero, boolean isShowTcrTitle, int cat_type)
			throws SQLException {
		HashMap copyConditionMap = new HashMap();
		copyConditionMap.putAll(conditionMap);	// 复制Map
		long tcr_id = ((Long)copyConditionMap.get("tcr_id")).longValue();	// 培训中心ID

		long[] tnd_id_lst = (long[])copyConditionMap.get("tnd_id_lst");	// 目录ID集
		Vector tndIdVec = null;
		if (tnd_id_lst != null && tnd_id_lst.length > 0 && tnd_id_lst[0] != 0) {
			tndIdVec = cwUtils.long2vector(tnd_id_lst);
		}

		// 查询某一培训中心下的所有顶层目录(注：这个(些)目录是已发布的)在aeTreeNode表中对应的tnd_id的SQL
		String sql = " select cat.cat_id, cat.cat_tcr_id, tCenter.tcr_title, treeNode.tnd_id, treeNode.tnd_title ";
		sql += " from tcTrainingCenter tCenter, ";
		sql += "		 aeCatalog cat, ";
		sql += " 	 aeTreeNode treeNode ";
		// 若tcr_id的值为0，则可以查询出所有培训中心下的顶层目录；值为非0则可以查询出该培训中心下的所有顶层目录
		sql += (tcr_id != 0 ? " where cat.cat_tcr_id = ? " : "where cat.cat_tcr_id in " + cwUtils.vector2list((Vector) copyConditionMap.get("tcr_id_lst")) + " "); // 传入：培训中心tcTrainingCenter表的主键tcr_id
		sql += " 	and tCenter.tcr_id = cat.cat_tcr_id ";
		sql += " 	and cat_status = 'ON' ";
		sql += " 	and treeNode.tnd_parent_tnd_id is null ";
		sql += "		and treeNode.tnd_cat_id = cat.cat_id ";
		sql += (tndIdVec != null && tndIdVec.size() > 0 ? "and treeNode.tnd_id in " + cwUtils.vector2list(tndIdVec) : "");
		if (!wizbini.cfgSysSetupadv.isLrnShowHeaderTc()) {
			sql += " 	and cat.cat_tcr_id <> 1";
		}
		if (cat_type > 0) {
			if (cat_type == 1) {
				sql += " and ( cat.cat_online_continue_educ <> '" + aeCatalog.CAT_STATUS_ON + "' or cat.cat_online_continue_educ is null)";
			} else if (cat_type == 2) {
				sql += " and cat.cat_online_continue_educ = '" + aeCatalog.CAT_STATUS_ON + "' ";
			}
		}
		sql += " order by tnd_title ";

		Vector topCatalogBeanVector = new Vector();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			if (tcr_id != 0) {
				pstmt.setLong(1, tcr_id);
			}

			rs = pstmt.executeQuery();
			CatalogBean catBean = null;
			while (rs.next()) {
				int amountOfSrhCourses = 0;

				copyConditionMap.put("tnd_id_lst", new long[]{rs.getLong("tnd_id")});

				// 获取某一顶层目录下符合搜索条件的课程数
				amountOfSrhCourses = this.getAmountOfSrhCourses(con, usr_ent_id, copyConditionMap);

				catBean = new CatalogBean();
				catBean.setId(rs.getLong("tnd_id"));			// 目录ID
				catBean.setTnd_id(rs.getLong("tnd_id"));		// 目录ID
				catBean.setText(rs.getString("tnd_title"));		// 目录名
				catBean.setTnd_title(rs.getString("tnd_title"));// 目录名
				catBean.setTcr_id(rs.getLong("cat_tcr_id"));	// 目录所属的培训中心ID
				if (isShowTcrTitle) {
					catBean.setTcr_title(rs.getString("tcr_title"));// 培训中心名
				}

				if (isLazy == false) {
					// 设置该目录下的所有符合搜索条件的用户可见课程数
					catBean.setCount(amountOfSrhCourses);

					if (isShowAmount == true) {
						if (catBean.isAlreadyShowed() == false) {
							catBean.setText(catBean.getText()/* + "  (" + catBean.getCount() + ")"*/);
							catBean.setAlreadyShowed(true);
						}
					}
				}
				topCatalogBeanVector.add(catBean);
			} // end while
		} catch(SQLException se) {
			System.err.println("[Get_Top_CatBeans_For_Srh_Condition_Exception]: " + se.getMessage());
			CommonLog.error("[Get_Top_CatBeans_For_Srh_Condition_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return topCatalogBeanVector;
	}

	/**
	 * 获取某一目录下所有已发布的一级子目录，同时该一级子目录(包括其子目录)中可以存在也可以不存在符合搜索条件的课程
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @param boolean isLazy 是否设置每个一级子目录下的所有符合搜索条件的可见课程数量(true：不设置，false：设置)
	 * @param boolean isShowAmount 是否在目录名后追加符合搜索条件的可见课程的数量，以用于前台显示(true：追加，false：不追加)
	 * @param conditionMap 条件集
	 * @param isShowZero 当该目录下的课程数为零时，指定是否显示此目录
	 * @return 某一目录下所有已发布的一级子目录(该一级子目录(包括子目录)中可以存在也可以不存在搜索条件的课程)的集合
	 * @throws SQLException
	 * @throws ParseException
	 */
	public Vector getFirstFloorCatBeansForSrhCondition(Connection con, long usr_ent_id,
			long tnd_id, long tcr_id, boolean isLazy, boolean isShowAmount, HashMap conditionMap,
			boolean isShowZero) throws SQLException {

		// 查询某一目录下的所有已发布的一级子目录的SQL
		String sql = "select treeNode.tnd_id, treeNode.tnd_title"	// 目录在aeTreeNode结点树表中的tnd_id，目录名
			+ " from aeTreeNodeRelation treeNodeR, "				// 用来记录目录与目录、目录与课程的关系表
			+ " 	 aeTreeNode treeNode "						// 课程与目录的关系结点树
			+ " where treeNodeR.tnr_ancestor_tnd_id=? "				// 传入：某一目录ID(aeTreeNode表的tnd_id字段)
			+ " 	  and treeNodeR.tnr_child_tnd_id=treeNode.tnd_id "
			+ " 	  and tnr_parent_ind=1 "
			+ " 	  and treeNodeR.tnr_type='TND_PARENT_TND' "
			+ " 	  and treeNode.tnd_status='ON' "				// 该目录是已发布的;
			+ " 	  order by treeNode.tnd_title ";

		Vector firstFloorCatalogVector = new Vector();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tnd_id);

			rs = pstmt.executeQuery();
			CatalogBean catBean = null;
			while (rs.next()) {
				HashMap copyConditionMap = new HashMap();
				copyConditionMap.putAll(conditionMap);	// 复制Map
				copyConditionMap.put("tnd_id_lst", new long[]{rs.getLong("tnd_id")});

				int amountOfSrhCourses = 0;

				// 获取该一级子目录下符合搜索条件的课程数
//				amountOfSrhCourses = this.getAmountOfSrhCourses(con, usr_ent_id, copyConditionMap);
//				if (amountOfSrhCourses == 0 && isShowZero == false) {// 若该顶层目录下不存在符合搜索条件的课程且指定不显示此目录，则不构建此一级子目录
//					continue;
//				}

				catBean = new CatalogBean();

				catBean.setId(rs.getLong("tnd_id"));
				catBean.setTnd_id(rs.getLong("tnd_id"));
				catBean.setText(rs.getString("tnd_title"));
				catBean.setTnd_title(rs.getString("tnd_title"));
				catBean.setTcr_id(tcr_id);
				if (isLazy == false) {
					// 设置该目录下的所有符合搜索条件的用户可见课程数
					catBean.setCount(amountOfSrhCourses);

					if (isShowAmount) {
						if (catBean.isAlreadyShowed() == false) {
							catBean.setText(catBean.getText()/* + "  (" + catBean.getCount() + ")"*/);
							catBean.setAlreadyShowed(true);
						}
					}
				}

				firstFloorCatalogVector.add(catBean);
			} // end while

		} catch(SQLException se) {
			System.err.println("[Get_First_Floor_CatBeans_For_Srh_Condition_Exception]: " + se.getMessage());
			CommonLog.error("[Get_First_Floor_CatBeans_For_Srh_Condition_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return firstFloorCatalogVector;
	}

	/**
	 * 获取符合搜索条件的所有的用户可见课程(课程是已发布的)数量
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param conditionMap 条件集
	 * @return 符合搜索条件的所有的用户可见课程(课程是已发布的)数量
	 * @throws SQLException
	 * @throws ParseException
	 */
	public int getAmountOfSrhCourses(Connection con, long usr_ent_id, HashMap conditionMap)
		throws SQLException {
		long[] tnd_id_lst = null; // 目录ID
		String[] s_app = null;
		if (conditionMap.get("tnd_id_lst") != null) {
			tnd_id_lst = (long[]) conditionMap.get("tnd_id_lst");
			Vector tnd_id_vec = new Vector();

			if (tnd_id_lst != null && tnd_id_lst.length == 1 && tnd_id_lst[0] != 0) {
				tnd_id_vec.add(new Long(tnd_id_lst[0]));
			} else if (tnd_id_lst != null && tnd_id_lst.length > 1 && tnd_id_lst[0] != 0) {

				for (int i = 0; i < tnd_id_lst.length; i++) {
					tnd_id_vec.add(new Long(tnd_id_lst[i]));
				}
			}

			if (tnd_id_vec.size() > 0)
				s_app = cwSQL.getSQLClause(con, "tmp_id", cwSQL.COL_TYPE_LONG, tnd_id_vec, 0);
			conditionMap.put("tnd_id_lst_SQL", s_app[0]);
		}
	        
		String sql = this.getSrhCourseSql(con, conditionMap, true);

		// 将以上SQL查询做为一个内联视图用以统计
		sql = "select count(*) from ( " + sql + " ) t";

		// 获取搜索参数
		String[] srh_key = null;
		if (conditionMap.get("srh_key") != null && ((String[])conditionMap.get("srh_key")).length > 0) {	// 获取关键词
//			srh_key = Utilities.filter((String)conditionMap.get("srh_key"));
			srh_key = (String[])conditionMap.get("srh_key");
		}

		String srh_key_type = "TITLE";		// 默认为非全文搜索
		if (conditionMap.get("srh_key_type") != null) {	// 获取全文搜索(可选值："TITLE"、"FULLTEXT")
			srh_key_type = (String)conditionMap.get("srh_key_type");
		}

		String itm_code = null;	// 获取课程编号
		if (conditionMap.get("itm_code") != null) {
			itm_code = (String)conditionMap.get("itm_code");
		}

		String ske_id_lst = null;	// 能力
		if (conditionMap.get("ske_id_lst") != null) {
			ske_id_lst = (String)conditionMap.get("ske_id_lst");
		}

		Timestamp cur_time = cwSQL.getTime(con);
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int amount = 0;
		int index = 1;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);

			pstmt.setLong(index++, usr_ent_id);


			// 加入搜索条件 and citm.itm_title like '%关键字%' 或 and (citm.itm_title like '%关键字1%' or citm.itm_title like '%关键字2%' or ...)
			if (srh_key != null && srh_key.length == 1) {
				 if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {		// 若是全文搜索
//					 appendCondition += " and citm.itm_srh_content like N'%" + srh_key[0] + "%' ";
					 pstmt.setString(index++, "%" + srh_key[0].toLowerCase() + "%");
				 } else if ("TITLE".equalsIgnoreCase(srh_key_type)) {	// 若非全文搜索
//					 appendCondition += " and citm.itm_title like '%" + srh_key[0] + "%' ";
					 pstmt.setString(index++, "%" + srh_key[0].toLowerCase() + "%");
				 }
			} else if (srh_key != null && srh_key.length > 1) {
//				appendCondition += " and ( ";

//				int keyCount = 1;
				for (int i = 0; i < srh_key.length; i++) {
					String key = srh_key[i];

					// 加入第i个key
					if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {		// 若是全文搜索
//						appendCondition += " citm.itm_srh_content like N'%" + key + "%' ";
						pstmt.setString(index++, "%" + key.toLowerCase() + "%");
					} else if ("TITLE".equalsIgnoreCase(srh_key_type)) {	// 若非全文搜索
//						appendCondition += " citm.itm_title like '%" + key + "%' ";
						pstmt.setString(index++, "%" + key.toLowerCase() + "%");
					}

//					keyCount++;
//					if (keyCount <= srh_key.length) {
//						appendCondition += " or ";
//					}
				} // end for

//				appendCondition += " ) ";
			}

			// 加入课程编号的判断
			if (itm_code != null && !"".equalsIgnoreCase(itm_code)) {
				pstmt.setString(index++, "%" + itm_code.toLowerCase() + "%");
			}
			if (IMMEDIATE.equalsIgnoreCase((String)conditionMap.get("srh_start_period"))) {
				pstmt.setTimestamp(index++, cur_time);
				pstmt.setTimestamp(index++, cur_time);
			} else {
				Timestamp start_datetime = null;
				Timestamp end_datetime = null;
				Timestamp[] start_end_datetime = getsrhStartEndTime(conditionMap, cur_time);
				start_datetime = start_end_datetime[0];
				end_datetime = start_end_datetime[1];
				if (start_datetime != null || end_datetime != null) {
					pstmt.setTimestamp(index++, start_datetime);
					pstmt.setTimestamp(index++, end_datetime);
					pstmt.setTimestamp(index++, start_datetime);
					pstmt.setTimestamp(index++, end_datetime);
				}
			}

			rs = pstmt.executeQuery();
			if (rs.next()) {
				amount = rs.getInt(1);

			} // end if

		} catch(SQLException se) {
			System.err.println("[Get_Amount_Of_Srh_Courses_Exception]: " + se.getMessage());
			CommonLog.error("[Get_Amount_Of_Srh_Courses_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}

			if(s_app != null && s_app[1] != null){
	            cwSQL.dropTempTable(con,s_app[1]);       
	        } 
		}

		return amount;
	}

	/**
	 * 获取按搜索条件的SQL
	 * @param con 数据库连接器
	 * @param conditionMap 条件集
	 * @param isCalcAmount 是否要计算某一目录下(包括其子孙目录)的所有用户的可见课程的数量
	 * @return 按搜索条件的SQL
	 * @throws SQLException
	 * @throws ParseException
	 */
	public String getSrhCourseSql(Connection con, HashMap conditionMap, boolean isCalcAmount) throws SQLException {
		String dbproduct = cwSQL.getDbProductName();
		// 获取搜索参数
		String[] srh_key = null;
		if (conditionMap.get("srh_key") != null && ((String[])conditionMap.get("srh_key")).length > 0) {	// 获取关键词
//			srh_key = Utilities.filter((String)conditionMap.get("srh_key"));
			srh_key = (String[])conditionMap.get("srh_key");
		}

		String srh_key_type = "TITLE";		// 默认为非全文搜索
		if (conditionMap.get("srh_key_type") != null) {	// 获取全文搜索(可选值："TITLE"、"FULLTEXT")
			srh_key_type = (String)conditionMap.get("srh_key_type");
		}

		Vector tcrIdVec = null;// 培训中心ID
		if (conditionMap.get("tcr_id_lst") != null) {
			tcrIdVec = (Vector)conditionMap.get("tcr_id_lst");
		}

		long[] tnd_id_lst = null;	// 目录ID
		if (conditionMap.get("tnd_id_lst") != null) {
			tnd_id_lst = (long[]) conditionMap.get("tnd_id_lst");
		}

		String srh_itm_type_lst = (String)conditionMap.get("srh_itm_type_lst");	// 课程类型选项
		String srh_start_period	= (String)conditionMap.get("srh_start_period");	// 开课日期时间期限

		long[] exclude_itm_id_lst = null;	// 获取exclude_itm_id_lst，用于指定不包含某些课程ID
		if (conditionMap.get("exclude_itm_id_lst") != null) {
			exclude_itm_id_lst = (long[]) conditionMap.get("exclude_itm_id_lst");
		}

		String itm_code = null;	// 获取课程编号
		if (conditionMap.get("itm_code") != null) {
			itm_code = (String)conditionMap.get("itm_code");
		}

		String srh_appn_start_datetime = null;	// 报名日期的开始搜索时间
		if (conditionMap.get("srh_appn_start_datetime") != null) {
			srh_appn_start_datetime = (String)conditionMap.get("srh_appn_start_datetime");
		}

		String srh_appn_end_datetime = null;	// 报名日期的结束搜索时间
		if (conditionMap.get("srh_appn_end_datetime") != null) {
			srh_appn_end_datetime = (String)conditionMap.get("srh_appn_end_datetime");
		}

		String ske_id_lst = null;// 能力ID(对应cmSkillEntity表的ske_id)集的字符串形式(形如""、"0"、"1"或"1,2,3"，其中""则认为不对能力进行筛选，"0"则认为目标课程的所有能力)
		if (conditionMap.get("ske_id_lst") != null) {
			ske_id_lst = (String)conditionMap.get("ske_id_lst");
		}

		// 若不计算该目录下的可见课程的数量，则不能有treeNode.tnd_id，
		// 这是由于前台在课程列表栏显示某个目录(包括其子目录)下的课程时，不能出现重复的课程(即使是同一门课程发布该目录下的不同子目录中时的情况)
		String subSql = " select distinct ";

		// 若要计算该目录下的可见课程的数量
		// 可能出现的情况是，在某个目录下的多个子目录中发布有相同的课程，就不能注释掉treeNode.tnd_id
		if (isCalcAmount == true) {
			subSql = " select distinct treeNode.tnd_id, ";
		}

		// 搜索符合条件的所有培训中心下(或某一目录下)的用户可见课程(课程是已发布的)，注：此方式能够查询某一目录下的所有可见课程SQL
		// 将以下的treeNode.tnd_id(课程所在结点树的ID)注释掉，以配合distinct使用，这样当一个课程发布在多个目录中时，可以确保该门课程的唯一
		String sql = subSql + " citm.itm_id, citm.itm_tcr_id, citm.itm_title, citm.itm_type, "	// 课程ID，课程名，课程类型
			   + " citm.itm_desc, citm.itm_fee, citm.itm_comment_avg_score, citm.itm_publish_timestamp,"	// 课程描述，课程评分
			   // 开课时间(去掉select子句中的离线课程对应班级的开课时间itm_eff_start_datetime字段，加入distinct，来限制搜索到的离线课程的惟一性，因为一个离线课程可能对应多个班级
			   // 若不加入distinct,可能搜索到的结果中存在多个相同的离线课程+不同的开课时间组成的行)
//			   + " isnull(citm.itm_eff_start_datetime, citm2.itm_eff_start_datetime) itm_eff_start_datetime, "
//			   + cwSQL.replaceNull(con, "citm.itm_eff_start_datetime", "citm2.itm_eff_start_datetime") + " itm_eff_start_datetime, "// 开课日期
			   // 课程时长
//			   + " isnull(citm.itm_content_eff_duration, datediff(day, citm2.itm_eff_start_datetime, citm2.itm_eff_end_datetime)) itm_content_eff_duration, "
//			   + cwSQL.replaceNull(con, "citm.itm_content_eff_duration", cwSQL.datediff(con, "citm2.itm_eff_start_datetime", "citm2.itm_eff_end_datetime")) + " itm_content_eff_duration, "	// 课程时长
			   + " citm.itm_appn_start_datetime, citm.itm_appn_end_datetime, "	// 报名开始时间，报名结束时间
			   // 该课程所属的顶层目录的cat_id，该课程所属的顶层目录在aeTreeNode表中的tnd_id，该目录所属的顶层目录名
			   // 将下行注释，以配合distinct使用，这样当一个课程发布在多个目录(多个目录可能在不同的顶层目录中)中时，可以确保该门课程的唯一
			   // " cat.cat_id, treeNode2.tnd_id cat_tnd_id, treeNode2.tnd_title cat_tnd_title, "
			   + " tCenter.tcr_id, tCenter.tcr_title, "		// 课程所属的培训中心ID，课程所属于的培训中心名
			   + " citm.itm_icon, "				// 课程图标
			   + " citm.itm_create_run_ind, citm.itm_exam_ind, citm.itm_blend_ind, citm.itm_ref_ind, "	// 是否是考试、是否是混合、是否是参考
			   + " citm.itm_create_timestamp "	// 课程创建时间
			   + " from aeTreeNodeRelation treeNodeR "	// 用来记录目录与目录、目录与课程的关系表
			   + " inner join aeTreeNode treeNode on treeNodeR.tnr_child_tnd_id = treeNode.tnd_id "// 课程与目录的关系结点树
			   + " inner join aeItem citm on treeNode.tnd_itm_id = citm.itm_id	"	// 课程表
			   // 加入班级表，以查找离线课程对应的班级的开课时间
			   + " left join aeItemRelation citmR on citm.itm_id = citmR.ire_parent_itm_id "
			   + " left join aeItem citm2 on citm2.itm_id = citmR.ire_child_itm_id "
			   // 加入网上课程报名表，以查找学员报读了网上课程的开课程时间
			   + " left join aeApplication appn on ( "
			   + " 		citm.itm_id = appn.app_itm_id "
			   + "		and appn.app_ent_id = ? "	// 传入：学员的ID(RegUser表中的usr_ent_id)
			   + " ) "
			   // 加入顶层目录表
			   + " left join aeCatalog cat on treeNode.tnd_cat_id=cat.cat_id "
			   + " left join aeTreeNode treeNode2 on ( "
			   + " 		treeNode2.tnd_parent_tnd_id is null "
			   + " 		and cat.cat_id = treeNode2.tnd_cat_id "
			   + " ) "
			   // 加入目录表
			   + " left join aeTreeNode treeNode3 on treeNodeR.tnr_ancestor_tnd_id = treeNode3.tnd_id "
			   // 加入培训中心表
			   + " left join tcTrainingCenter tCenter on cat.cat_tcr_id = tCenter.tcr_id "
			   // 加入查询某一培训中心的判断(若去掉此判断，则可以查询出所有培训中心下的符合搜索条件的用户可见课程)
			   + ((tcrIdVec != null && tcrIdVec.size() >= 1) ? " where tCenter.tcr_id in " + cwUtils.vector2list(tcrIdVec) + " " : "")
			   + ((tcrIdVec == null || tcrIdVec.size() == 0)  ? " where treeNodeR.tnr_type = '" + aeTreeNodeRelation.TNR_TYPE_ITEM_PARENT_TND + "' " : " and treeNodeR.tnr_type = '" + aeTreeNodeRelation.TNR_TYPE_ITEM_PARENT_TND + "' ")
			   // 课程是发布的、且其顶层目录是发布的、且其所在的目录及上层目录是发布的
			   + " and citm.itm_status='ON' and cat.cat_status = 'ON' and ((treeNode3.tnd_type = 'NORMAL' and treeNode3.tnd_status = 'ON') or (treeNode3.tnd_type = 'CATALOG' and treeNode3.tnd_status is null)) ";

   String skillSubSql_1 = "	   and exists (select * "
		   + " 			   			from aeItemTargetRuleDetail citmRD "
		   							// 对于aeItemTargetRuleDetail表中的ird_upt_id字段是岗位的情况，找到其对应的能力
		   + " 						left join cmSkillEntity cmEnt on (citmRD.ird_upt_id= cmEnt.ske_id and cmEnt.ske_type = 'SKP') "
		   + "						left join cmSkillSet cmSet on (cmEnt.ske_id = cmSet.sks_ske_id) "
		   + "						left join cmSkillSetCoverage cmCov on (cmSet.sks_skb_id = cmCov.ssc_sks_skb_id) "
		   + " 						left join cmSkillBase cmBase on (cmCov.ssc_skb_id = cmBase.skb_id) "
			 	 			 							// 精确查找学员所在的用户组ID
		   + "             			where ird_group_id in (select ern_ancestor_ent_id "	// 用户组ID(对应Entity表的ent_id)
		   + "			   						  		   from EntityRelation "		// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
		   + " 			   						  		   where ern_child_ent_id = ?	"	// 传入：学员的ID(RegUser表中的usr_ent_id)
//		   + " 			   		 				  		   and ern_parent_ind = 1 "		// 限定只查找学员所属的父组
		   + "					 				  		   and ern_type = '" + dbEntityRelation.ERN_TYPE_USR_PARENT_USG + "' "
		   + " 			  						 		  ) "
		                	 					   		// 精确查找学员所属的职务ID
		   + "            		 		  and ird_grade_id in (select ern_ancestor_ent_id "	// 职务ID(对应Entity表的ent_id)
		   + "										  		   from EntityRelation "		// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
		   + " 										  		   where ern_child_ent_id = ? "	// 传入：学员的ID(RegUser表中的usr_ent_id)
//		   + " 										  				 and ern_parent_ind = 1 "	// 限定只查找学员所属的职务
		   + " 												   		 and ern_type = '" + dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR + "' "
		   + " 										 		  ) "
		   + "					 		  and citm.itm_id = ird_itm_id ";

   String skillSubSql_1_1 = "  and exists (select * "
	       + " 			   			from aeItemTargetRuleDetail citmRD "
		 	 			 								// 精确查找学员所在的用户组ID
	       + "             			where ird_group_id in (select ern_ancestor_ent_id "	// 用户组ID(对应Entity表的ent_id)
	       + "			   						  		   from EntityRelation "		// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
	       + " 			   						  		   where ern_child_ent_id = ?	"	// 传入：学员的ID(RegUser表中的usr_ent_id)
//	       + " 			   		 				  		   and ern_parent_ind = 1 "		// 限定只查找学员所属的父组
	       + "					 				  		   and ern_type = '" + dbEntityRelation.ERN_TYPE_USR_PARENT_USG + "' "
	       + " 			  						 		  ) "
	                	 					   			// 精确查找学员所属的职务ID
	       + "            		 		  and ird_grade_id in (select ern_ancestor_ent_id "	// 职务ID(对应Entity表的ent_id)
	       + "										  		   from EntityRelation "		// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
	       + " 										  		   where ern_child_ent_id = ? "	// 传入：学员的ID(RegUser表中的usr_ent_id)
//	       + " 										  				 and ern_parent_ind = 1 "	// 限定只查找学员所属的职务
	       + " 												   		 and ern_type = '" + dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR + "' "
	       + " 										 		  ) "
	       + "					 		  and citm.itm_id = ird_itm_id ";

   String skillSubSql_2 =
		     " 						   	  and (ird_upt_id = -1 "
		   + "								   or ird_upt_id in ( "
           + "                                     select upr_upt_id "
           + "                                     from UserPositionRelation "
           + "                                     where upr_usr_ent_id = ? "
		   + "								   ) "
		   + " 						      ) ";

   String skillSubSql_3 =
	   		 " 							  and ird_upt_id > 0 ";
   String skillSubSql_4 =
	   		 " 							  and " + cwSQL.replaceNull("cmBase.skb_ske_id", "citmRD.ird_upt_id") + " in (" + ske_id_lst + ") ";

	// 加入课程是对所有学员发布的或者是对目标学员发布的(若是对目标学员发布的，则要判断目标学员所属的用户组或职务是否符合规则)和对能力的判断
	if (ske_id_lst == null || "".equals(ske_id_lst) || "0".equals(ske_id_lst)) {

	  if (!"0".equals(ske_id_lst)) {// 若前台传入的ske_id_lst为空或""，则不对能力进行筛选
		  sql += " and (citm.itm_access_type='ALL' "
			   + "	    or (citm.itm_access_type='TARGET_LEARNER' "
			   + skillSubSql_1
		  	   + skillSubSql_2;
	  } else {						// 若前台传入的ske_id_lst为"0"，则列出所有和岗位能力关联的培训，不管该培训是否和当前用户的岗位能力关联
		  sql += " and ( "
		  	   + "		(citm.itm_access_type='ALL' "	// 对所有学员发布的情况
			   + " 		 and exists ( "
			   + " 				select * from aeItemTargetRuleDetail "
			   + " 				where citm.itm_id = ird_itm_id and ird_upt_id > 0 "
			   + " 		 ) "// 匹配"and exists ("
			   + " 		) "	// 匹配"(citm.itm_access_type='ALL'"
			   + "	    or (citm.itm_access_type='TARGET_LEARNER' "
			   + skillSubSql_1
			   + skillSubSql_2
		  	   + skillSubSql_3;
	  }
	  sql +=
			" 		) "	// 匹配"and exists ("
			+ "  ) "
			+ " ) ";
	} else {	// 否则，对能力进行筛选
		// 对传入的ske_id_lst分组成岗位集和能力集
		HashMap skillMap = this.groupSkillSetAndSkillBase(con, ske_id_lst);
		Vector skillSetVec = (Vector)skillMap.get("skillSet");
		Vector skillBaseVec = (Vector)skillMap.get("skillBase");

		boolean hasSkillSet = skillSetVec.size() > 0 ? true : false;
		boolean hasSkillBase = skillBaseVec.size() > 0 ? true : false;

	    sql +=
	    	  " and ( "
	    	+ " 		(citm.itm_access_type = 'ALL'"	// 对所有学员发布的情况
	    	+ " 		 and exists ( "
	    	+ " 				select * from aeItemTargetRuleDetail "
	    	+ " 				where citm.itm_id = ird_itm_id and ird_upt_id > 0 ";
	    // 若前台传入的ske_id_lst是按岗位进行搜索
	    if (hasSkillSet == true && hasSkillBase == false) {
			sql += "				  and (ird_upt_id in " + cwUtils.vector2list(skillSetVec)
				+ " 					   or ird_upt_id in ( "	// 或者匹配该岗位下的能力
				+ " 								select distinct skb_ske_id from cmSkillSet, cmSkillSetCoverage, cmSkillBase "
				+ " 								where sks_ske_id in " + cwUtils.vector2list(skillSetVec) + " and sks_skb_id = ssc_sks_skb_id and ssc_skb_id = skb_id and skb_delete_timestamp is null "
				+ " 					   ) "	// 匹配"or ird_upt_id in ("
				+ "                   ) " 		// 匹配"and (ird_upt_id in"
				+ "		 ) "	// 匹配" and exists ( "
				+ " 	) ";	// 匹配" (citm.itm_access_type = 'ALL' "
		}
	    // 若前台传入的ske_id_lst是按能力进行搜索，则ird_skill_id匹配ske_id_lst并ske_id_lst对应的岗位
	    if (hasSkillBase == true && hasSkillSet == false) {
			sql += "				  and (ird_upt_id in " + cwUtils.vector2list(skillBaseVec)
				+ "			 		  	   or ird_upt_id in ( "	// 或者匹配该能力对应的岗位
				+ "									select distinct sks_ske_id from cmSkillBase, cmSkillSetCoverage, cmSkillSet "
				+ "									where skb_ske_id in " + cwUtils.vector2list(skillBaseVec) + " and skb_id = ssc_skb_id and ssc_sks_skb_id = sks_skb_id and skb_delete_timestamp is null "
				+ " 		 			   ) " 	// 匹配"or ird_upt_id in ("
				+ " 				  ) "		// 匹配"and (ird_upt_id in"
				+ " 	 ) "	// 匹配"and exists ("
				+ "		) ";	// 匹配"(citm.itm_access_type = 'ALL'"
		}
	    // 若前台传入的ske_id_lst中既包含岗位又包含能力
	    if (hasSkillSet == true && hasSkillBase == true) {

	    }

	    // 对目标学员发布的情况
	    sql += " 	    or (citm.itm_access_type = 'TARGET_LEARNER' "
	    	+ skillSubSql_1_1 + skillSubSql_2 + skillSubSql_3;
	    // 若前台传入的ske_id_lst是按岗位进行搜索
	    if (hasSkillSet == true && hasSkillBase == false) {
			sql += "		and (ird_upt_id in " + cwUtils.vector2list(skillSetVec)
				+ " 			 or ird_upt_id in ( "	// 或者匹配该岗位下的能力
				+ " 					select distinct skb_ske_id from cmSkillSet, cmSkillSetCoverage, cmSkillBase "
				+ " 					where sks_ske_id in " + cwUtils.vector2list(skillSetVec) + " and sks_skb_id = ssc_sks_skb_id and ssc_skb_id = skb_id and skb_delete_timestamp is null "
				+ " 			 ) "	// 匹配"or ird_upt_id in ("
				+ " 			) "
				+ " 	)"	// 匹配"and exists ("
				+ "    )";	// 匹配"or (citm.itm_access_type = 'TARGET_LEARNER'";
		}
	    // 若前台传入的ske_id_lst是按能力进行搜索，则ird_skill_id匹配ske_id_lst并ske_id_lst对应的岗位
	    if (hasSkillBase == true && hasSkillSet == false) {
			sql += "		and (ird_upt_id in " + cwUtils.vector2list(skillBaseVec)
				+ "			 	 or ird_upt_id in ( "	// 或者匹配该能力对应的岗位
				+ "						select distinct sks_ske_id from cmSkillBase, cmSkillSetCoverage, cmSkillSet "
				+ "						where skb_ske_id in " + cwUtils.vector2list(skillBaseVec) + " and skb_id = ssc_skb_id and ssc_sks_skb_id = sks_skb_id and skb_delete_timestamp is null "
				+ " 		 	 ) "
				+ " 		) "
				+ " 	)"	// 匹配"and exists ("
				+ "    )";	// 匹配"or (citm.itm_access_type = 'TARGET_LEARNER'"
		}
	    // 若前台传入的ske_id_lst中既包含岗位又包含能力
	    if (hasSkillSet == true && hasSkillBase == true) {

	    }

		sql += " ) ";	// 匹配"and ("
	} // end if

		// 以下为新加入的用于搜索符合要求课程的条件
		String appendCondition = "";

		// 加入查询某一(些)目录下的所有课程的判断
		// 若无此限定值(约定：tnd_id_lst[0]为0，则无此限定值)和无tcr_id的限定，则可以查询出所有培训中心下所有符合条件的课程
		// 若无此限定值(约定：tnd_id_lst[0]为0，则无此限定值)但有tcr_id的限定，则可以查询出某一培训中心下的所有符合条件的课程
		// and treeNodeR.tnr_ancestor_tnd_id in (...)  某一(些)目录ID(aeTreeNode表的tnd_id字段)
		if (tnd_id_lst != null && tnd_id_lst.length >= 1 && tnd_id_lst[0] != 0) {
			appendCondition += " and treeNodeR.tnr_ancestor_tnd_id in "+(String)conditionMap.get("tnd_id_lst_SQL") +" ";
//			appendCondition += " and treeNodeR.tnr_ancestor_tnd_id in (";
//
//			int tndIdCount = 1;
//			for (int i = 0; i < tnd_id_lst.length; i++) {
//				long tndId = tnd_id_lst[i];
//
//				// 加入第i个tndId
//				appendCondition += tndId;
//
//				tndIdCount++;
//				if (tndIdCount <= tnd_id_lst.length) {
//					appendCondition += ", ";
//				}
//			} // end for
//
//			appendCondition += ") ";
		}

		// 加入搜索条件 and citm.itm_title like '%关键字%' 或 and (citm.itm_title like '%关键字1%' or citm.itm_title like '%关键字2%' or ...)
		if (srh_key != null && srh_key.length == 1) {
			 if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {		// 若是全文搜索
				 
				 if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
				     appendCondition += " and lower(citm.itm_srh_content) like ? ";
                 }else{
                     appendCondition += " and citm.itm_srh_content like ? ";
                 }
				 

			 } else if ("TITLE".equalsIgnoreCase(srh_key_type)) {	// 若非全文搜索
				 appendCondition += " and lower(citm.itm_title) like ? ";
			 }
		} else if (srh_key != null && srh_key.length > 1) {
			appendCondition += " and ( ";

			int keyCount = 1;
			for (int i = 0; i < srh_key.length; i++) {

				// 加入第i个key
				if ("FULLTEXT".equalsIgnoreCase(srh_key_type)) {		// 若是全文搜索
					
					
					if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
					    appendCondition += " lower(citm.itm_srh_content) like ? ";
	                 }else{
	                     appendCondition += " citm.itm_srh_content like ? ";
	                 }
					

				} else if ("TITLE".equalsIgnoreCase(srh_key_type)) {	// 若非全文搜索
					appendCondition += " lower(citm.itm_title) like ? ";
				}

				keyCount++;
				if (keyCount <= srh_key.length) {
					appendCondition += " or ";
				}
			} // end for

			appendCondition += " ) ";
		}

		// 加入课程类型的判断 and (citm.itm_type=... or citm.itm_type=...)
		if (srh_itm_type_lst != null && !"".equals(srh_itm_type_lst)) {
//			appendCondition += "and citm.itm_type='" + srh_itm_type_lst + "' ";
			appendCondition += aeItemDummyType.genSqlByItemDummyType(srh_itm_type_lst, "citm", true);
		}

		// 加入排除在某一(些)课程外的判断
		// and citm.itm_id not in (...)  某一(些)目录ID(aeTreeNode表的tnd_id字段)
		if (exclude_itm_id_lst != null && exclude_itm_id_lst.length == 1 && exclude_itm_id_lst[0] != 0) {
			appendCondition += " and citm.itm_id <> " + exclude_itm_id_lst[0] + " ";
		} else if (exclude_itm_id_lst != null && exclude_itm_id_lst.length > 1 && exclude_itm_id_lst[0] != 0) {
			appendCondition += " and citm.itm_id not in (";

			int itmIdCount = 1;
			for (int i = 0; i < exclude_itm_id_lst.length; i++) {
				long tndId = exclude_itm_id_lst[i];

				// 加入第i个tndId
				appendCondition += tndId;

				itmIdCount++;
				if (itmIdCount <= exclude_itm_id_lst.length) {
					appendCondition += ", ";
				}
			} // end for

			appendCondition += ") ";
		}

		// 加入课程编号的判断
		if (itm_code != null && !"".equalsIgnoreCase(itm_code)) {
			appendCondition += " and lower(citm.itm_code) like ?";
		}

		// 加入开始日期的搜索判断
		if (IMMEDIATE.equalsIgnoreCase(srh_start_period)) {
//			String sub1 = " (? between citm2.itm_eff_start_datetime and citm2.itm_eff_end_datetime or ? between citm2.itm_content_eff_start_datetime and citm2.itm_content_eff_end_datetime) ";
			String sub1 =
				  " (? between case when citm2.itm_eff_start_datetime < citm2.itm_content_eff_start_datetime then citm2.itm_eff_start_datetime "
				+ "					else citm2.itm_content_eff_start_datetime "
				+ "			   end "
				+ "	   and case when citm2.itm_eff_end_datetime > citm2.itm_content_eff_end_datetime then citm2.itm_eff_end_datetime "
				+ "		   	    else citm2.itm_content_eff_end_datetime "
				+ "		   end"
				+ " ) ";
			// 面授课程(考试)
			String sub2 = " ? between citm2.itm_eff_start_datetime and citm2.itm_eff_end_datetime ";
			// 混合课程(考试)的处理
			appendCondition += " and ( (citm2.itm_blend_ind = 1 and citm2.itm_run_ind = 1 and " + sub1 + ") ";
			// 网上课程(考试)的处理,选择即时开始时可以搜索出网上课程
			appendCondition += " or (citm.itm_blend_ind = 0 and citm.itm_run_ind = 0 and citm.itm_create_run_ind = 0 and citm.itm_ref_ind = 0 )";
			// 面授课程(考试)的处理
		    appendCondition += " or (citm2.itm_blend_ind = 0 and citm2.itm_run_ind = 1 and " + sub2 + ")) ";
		} else if ((srh_start_period != null && srh_start_period.length() > 0 && !UNLIMITED.equalsIgnoreCase(srh_start_period))
				|| (srh_appn_start_datetime != null && !"".equals(srh_appn_start_datetime))
				|| (srh_appn_end_datetime != null && !"".equals(srh_appn_end_datetime))) {
			// 混合课程(考试)开课日期的条件查询子句
//			String sub1 = " (citm2.itm_eff_start_datetime between ? and ?  or citm2.itm_content_eff_start_datetime between ? and ?) ";
			String sub1 =
				  " (case when citm2.itm_eff_start_datetime < citm2.itm_content_eff_start_datetime then citm2.itm_eff_start_datetime "
				+ "		  else citm2.itm_content_eff_start_datetime "
				+ "	 end "
				+ "  between ? and ? "
				+ " ) ";
			// 面授课程(考试)
			String sub2 = " citm2.itm_eff_start_datetime between ? and ? ";
			// 混合课程(考试)的处理
			appendCondition += " and ((citm2.itm_blend_ind = 1 and citm2.itm_run_ind = 1 and " + sub1 + ") ";
			// 面授课程(考试)的处理
		    appendCondition += " or (citm2.itm_blend_ind = 0 and citm2.itm_run_ind = 1 and " + sub2 + ")) ";
		}
		
		if (conditionMap.get("srh_fee") != null) {
			String srh_fee = (String) conditionMap.get("srh_fee"); // 排序字段
			if (cwUtils.notEmpty(srh_fee) && srh_fee.trim().equalsIgnoreCase("1")) {
				appendCondition += " and citm.itm_fee > 0 ";
			} else if (cwUtils.notEmpty(srh_fee) && srh_fee.trim().equalsIgnoreCase("2")) {
				appendCondition += " and (citm.itm_fee is null or citm.itm_fee = 0) ";
			}
		}
		if (conditionMap.get("cat_type") != null) {
			int srh_type = (Integer) conditionMap.get("cat_type"); // 排序字段
			if (srh_type == 1) {
				appendCondition += " and ( cat.cat_online_continue_educ is null or cat.cat_online_continue_educ <> '" + aeCatalog.CAT_STATUS_ON + "') ";
			} else if (srh_type == 2) {
				appendCondition += " and cat.cat_online_continue_educ = '" + aeCatalog.CAT_STATUS_ON + "' ";
			}
		}
		
		String sort = (String)conditionMap.get("sort");	// 排序字段
		String dir = (String)conditionMap.get("dir");	// 排序字段
		if(sort != null && sort.length() > 0) {
			if(sort.equalsIgnoreCase("msg_upd_date") || sort.equalsIgnoreCase("msg_begin_date")){
				appendCondition += " order by " + sort + " " + dir;
			}
		}

		sql += appendCondition;

		return sql;
	}

	/**
	 * 对传入的ske_id_lst分组成岗位集和能力集，并包含在Map中
	 * @param con 数据库连接器
	 * @param ske_id_lst 形如"1"、"3, 6, 8"
	 * @return 返回HashMap，并包含岗位集和能力集
	 * @throws SQLException
	 */
	public HashMap groupSkillSetAndSkillBase(Connection con, String ske_id_lst) throws SQLException {
		String sql = "select ske_id, ske_type from cmSkillEntity where ske_id in (" + ske_id_lst + ") order by ske_type";

		Vector skillSetVec = new Vector();	// 岗位集
		Vector skillBaseVec = new Vector();	// 能力集

		HashMap skillMap = new HashMap();
		skillMap.put("skillSet", skillSetVec);
		skillMap.put("skillBase", skillBaseVec);

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				long ske_id = rs.getLong("ske_id");
				String ske_type = rs.getString("ske_type");

				if (Course.SKE_TYPE_SKP.equalsIgnoreCase(ske_type)) {
					skillSetVec.addElement(new Long(ske_id));
				} else if (Course.SKE_TYPE_COMPOSITE_SKILL.equalsIgnoreCase(ske_type)) {
					skillBaseVec.addElement(new Long(ske_id));
				}
			}
		} catch(SQLException se) {
			System.err.println("[Group_SkillSet_And_SkillBase_Exception]: " + se.getMessage());
			CommonLog.error("[Group_SkillSet_And_SkillBase_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return skillMap;
	}

	/**
	 * 根据传入的ske_id来判断此ID为岗位还是能力
	 * @param con 数据库连接器
	 * @param ske_id 对应cmSkillEntity表的ske_id
	 * @return 返回判断后的标识("SKP" 岗位、"COMPOSITE_SKILL" 能力、null 空)
	 * @throws SQLException
	 */
	public String isSkillSetOrSkillBase(Connection con, long ske_id) throws SQLException {
		String sql = "select ske_type from cmSkillEntity where ske_id = ?";

		String str = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, ske_id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				str = rs.getString(1);
			}

		} catch(SQLException se) {
			System.err.println("[Is_SkillSet_Or_SkillBase_Exception]: " + se.getMessage());
			CommonLog.error("[Is_SkillSet_Or_SkillBase_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return str;
	}

	/**
	 * 分页提取某一目录下(包括其子孙目录)的所有用户可见的课程
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @param usr_ent_id 用户ID
	 * @param param URL参数信息
	 * @param inLang 语言
	 * @param itmDir upload path of item
	 * @return 某一目录下(包括其子孙目录)的所有用户的可见课程集
	 * @throws SQLException
	 */
	public Vector getViewCosOfOneCatByPagination(Connection con, long usr_ent_id, long tnd_id, BaseParam param, String inLang, String itmDir)
		throws SQLException {

		CourseModuleParam courseModuleParam = (CourseModuleParam)param;

		// 查询某一目录下(包括其子孙目录)的所有用户的可见课程的SQL
		String sql = this.getViewCoursesOfOneCatalogSql(false, true, new long[]{tnd_id});

		Vector courseVector = new Vector();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			int index =1;
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);

			int count = 0;
			rs = pstmt.executeQuery();
			while (rs.next()) {
				int end = courseModuleParam.getStart() + courseModuleParam.getLimit() - 1;// 每页所显示记录的最大序号

				// 采用假分页技术(所有数据库都通用)
				if(count >= courseModuleParam.getStart() && count <= end) {
					CourseBean courseBean = new CourseBean();

					courseBean.setItm_id(rs.getLong("itm_id"));			// 课程ID
					courseBean.setItm_title(rs.getString("itm_title"));	// 课程名
					courseBean.setItm_type(rs.getString("itm_type"));	// 课程类型
					courseBean.setItm_desc(rs.getString("itm_desc"));	// 课程描述
					float avg_score = rs.getFloat("itm_comment_avg_score");
					courseBean.setItm_comment_avg_score(Math.round(avg_score)); // 课程评分
					
					String itmIcon = rs.getString("itm_icon");
					String itmIconPath = Course.getItmIconPath(itmDir, rs.getLong("itm_id"), itmIcon);
				    courseBean.setItm_icon(itmIconPath);

					boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
					boolean itm_exam_ind = rs.getBoolean("itm_exam_ind");
					boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
					boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");

					String itmDummyType = aeItemDummyType.getDummyItemType(courseBean.getItm_type(), itm_blend_ind, itm_exam_ind, itm_ref_ind);
					courseBean.setLab_itm_type(aeItemDummyType.getItemLabelByDummyType(inLang, itmDummyType));
					courseBean.setItm_dummy_type(itmDummyType);

					if (itm_create_run_ind) {
						if (!itm_blend_ind) {	// 若是离线课程或离线考试，获取其对应的班级的开课时间
							courseBean.setRecent_start_classes(this.getStartDateOfClass(con, courseBean.getItm_id()));
						} else {	// 若是混合课程或混合考试，获取其对应的班级的开课时间
							courseBean.setRecent_start_classes(this.getStartDateofBlendItem(con, courseBean.getItm_id()));
						}
					}

					courseVector.add(courseBean);
				}

				count++;
			} // end while


			courseModuleParam.setTotal_rec(count);	// 设置总记录数
		} catch(SQLException se) {
			System.err.println("[Get_View_Cos_Of_One_Cat_By_Pagination_Exception]: " + se.getMessage());
			CommonLog.error("[Get_View_Cos_Of_One_Cat_By_Pagination_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return courseVector;
	}

	/**
	 * 获取某一目录下(包括其子孙目录)的所有用户可见的课程的数量
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @param usr_ent_id 用户ID
	 * @return 某一目录下(包括其子孙目录)的所有用户可见的课程的数量
	 * @throws SQLException
	 */
	public int getViewCosAmountOfOneCat(Connection con, long usr_ent_id, long tnd_id)
		throws SQLException {

		// 由于要计算某一目录下(包括其子孙目录)的所有用户可见课程的数量，
		// 可能出现的情况是，在某个目录下的多个子目录发布有相同的课程，于是就不能注释掉treeNode.tnd_id
		String subSql = "select distinct treeNode.tnd_id, citm.itm_id, citm.itm_title, citm.itm_type, citm.itm_icon, "	// 课程所在结点树的ID(去掉)，课程ID，课程名，课程类型，课程图标
			+ " citm.itm_desc, citm.itm_comment_avg_score, "	// 课程描述，课程评分
			+ " citm.itm_create_run_ind, citm.itm_exam_ind, citm.itm_blend_ind, citm.itm_ref_ind "	// 是否可以创建班级、是否是考试、是否是混合、是否是参考
			+ " from aeTreeNodeRelation treeNodeR "		// 用来记录目录与目录、目录与课程的关系表
			+ " inner join aeTreeNode treeNode on treeNodeR.tnr_child_tnd_id=treeNode.tnd_id "	// 课程与目录的关系结点树
			+ " inner join aeItem citm on treeNode.tnd_itm_id=citm.itm_id "	// 课程(或班级)表
//			// 加入班级与离线课程的关系表，以方便查找离线课程对应的班级的开课时间
//			+ " left join aeItemRelation citmR on citm.itm_id=citmR.ire_parent_itm_id "
//			+ " left join aeItem citm2 on citm2.itm_id=citmR.ire_child_itm_id "
			// 加入顶层目录表
			+ " left join aeCatalog cat on treeNode.tnd_cat_id=cat.cat_id "
			+ " left join aeTreeNode treeNode2 on ( "
			+ " 		treeNode2.tnd_parent_tnd_id is null "
			+ " 		and cat.cat_id = treeNode2.tnd_cat_id "
			+ " ) "
			// 加入目录表
			+ " left join aeTreeNode treeNode3 on treeNodeR.tnr_ancestor_tnd_id = treeNode3.tnd_id "
			+ " where treeNodeR.tnr_ancestor_tnd_id=? "	// 传入：某一目录ID(aeTreeNode表的tnd_id字段)
			+ " 	  and treeNodeR.tnr_type='ITEM_PARENT_TND' "
//			+ " 	  and citm.itm_status='ON' "	// 课程是发布的
			+ " 	  and citm.itm_status='ON' and cat.cat_status = 'ON' and ((treeNode3.tnd_type = 'NORMAL' and treeNode3.tnd_status = 'ON') or (treeNode3.tnd_type = 'CATALOG' and treeNode3.tnd_status is null)) "// 课程是发布的、且其顶层目录是发布的、且其所在的目录及上层目录是发布的
			// 加入课程是对所有学员发布的或者是对目标学员发布的(若是对目标学员发布的，则要判断目标学员所属的用户组或职务是否符合规则)
			+ " and (citm.itm_access_type='ALL' "
			+ "	     or (citm.itm_access_type='TARGET_LEARNER' "
			+ "	     	 and exists (select null "
			+ " 	 			   	 from aeItemTargetRuleDetail "
															// 精确查找学员所在的用户组ID
		    + "            	   		 where ird_group_id in (select ern_ancestor_ent_id "// 用户组ID(对应Entity表的ent_id)
			+ "										  		from EntityRelation	"		// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
			+ "										  		where ern_child_ent_id=? "	// 传入：学员的ID(RegUser表中的usr_ent_id)
//			+ "										  	    	  and ern_parent_ind=1 "// 限定只查找学员所属的父组
			+ "											    	  and ern_type='USR_PARENT_USG' "
		    + "            	 					     	   ) "
		    													// 精确查找学员所属的职务ID
		    + "                 	       and ird_grade_id in (select ern_ancestor_ent_id "// 职务ID(对应Entity表的ent_id)
			+ "						 					  	  from EntityRelation "			// 用户与用户组、用户组与用户组、用户与职务、职务与职务的关系表
			+ "											  	  where ern_child_ent_id=? "	// 传入：学员的ID(RegUser表中的usr_ent_id)
//			+ "												    	and ern_parent_ind=1 "	// 限定只查找学员所属的职务
			+ "												   	 	and ern_type='USR_CURRENT_UGR' "
			+ "										     	 ) "
		    + "                 	       and ird_itm_id = citm.itm_id "
//		    + "                  	       and citm.itm_access_type='TARGET_LEARNER' "
		    + " 						   and (ird_upt_id = -1 "
			+ "									or ird_upt_id in ( "
			+ "										select uss_ske_id "
			+ "										from RegUserSkillSet "
			+ "										where uss_ent_id = ? "
			+ "										union "
			+ " 									select ske_id "
			+ " 									from cmSkillSet "
			+ " 									inner join cmSkillSetCoverage on (ssc_sks_skb_id = sks_skb_id) "
			+ "										inner join cmSkillBase on (ssc_skb_id = skb_id and skb_delete_timestamp is null) "
			+ " 									inner join cmSkillEntity on (skb_ske_id = ske_id) "
			+ " 									inner join RegUserSkillSet on (uss_ske_id = sks_ske_id and uss_ent_id = ?) "
			+ "									) "
			+ " 						  ) "
		    + "	         		    ) "
			+ " 	  	) "
			+ "  	) ";

		String sql = " select count(*) from (" + getViewCoursesOfOneCatalogSql(true, false, new long[]{tnd_id}) + ") t ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int amount = 0;
		try {
			pstmt = con.prepareStatement(sql);
			int index = 1;
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);
			pstmt.setLong(index++, usr_ent_id);


			rs = pstmt.executeQuery();
			if (rs.next()) {
				amount = rs.getInt(1);
			} // end if
		} catch (SQLException se) {
			System.err.println("[Get_View_Cos_Amount_Of_One_Cat_Exception]: " + se.getMessage());
			CommonLog.error("[Get_View_Cos_Amount_Of_One_Cat_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			cwSQL.cleanUp(rs, pstmt);
		}
		return amount;
	}

	/**
	 * 获取某一目录下所有已发布的一级子目录
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @param boolean isLazy 是否设置每个一级子目录下的所有可见课程数(true：不设置，false：设置)
	 * @param boolean isShowAmount 是否在目录名后追加可见课程的数量，以用于前台显示(true：追加，false：不追加)
	 * @return 某一目录下所有已发布的一级子目录的集合
	 * @throws SQLException
	 */
	public Vector getFirstFloorCatalogs(Connection con, long usr_ent_id, long tnd_id, /*long tcr_id,*/ boolean isLazy, boolean isShowAmount)
		throws SQLException {

		// 查询某一目录下的所有已发布的一级子目录的SQL
		String sql = "select treeNode.tnd_id, treeNode.tnd_title"	// 目录在aeTreeNode结点树表中的tnd_id，目录名
			+ " from aeTreeNodeRelation treeNodeR, "				// 用来记录目录与目录、目录与课程的关系表
			+ " 	 aeTreeNode treeNode "							// 课程与目录的关系结点树
			+ " where treeNodeR.tnr_ancestor_tnd_id=? "				// 传入：某一目录ID(aeTreeNode表的tnd_id字段)
			+ " 	  and treeNodeR.tnr_child_tnd_id=treeNode.tnd_id "
			+ " 	  and tnr_parent_ind=1 "
			+ " 	  and treeNodeR.tnr_type='TND_PARENT_TND' "
			+ " 	  and treeNode.tnd_status='ON' "				// 该目录是已发布的;
			+ " 	  order by treeNode.tnd_title ";

		Vector firstFloorCatalogVector = new Vector();

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tnd_id);

			rs = pstmt.executeQuery();
			CatalogBean catBean = null;
			while (rs.next()) {
				catBean = new CatalogBean();

				catBean.setId(rs.getLong("tnd_id"));		// 目录ID，用于前台展现
				catBean.setTnd_id(rs.getLong("tnd_id"));	// 目录ID

				catBean.setText(rs.getString("tnd_title"));			// 目录名，用于前台展现
				catBean.setTnd_title(rs.getString("tnd_title"));	// 目录名
//				catBean.setTcr_id(tcr_id);
				if (isLazy == false) {
					// 设置该目录下的所有可见课程数
					catBean.setCount(this.getViewCosAmountOfOneCat(con, usr_ent_id, catBean.getId()));

					if (isShowAmount) {
						if (catBean.isAlreadyShowed() == false) {
							catBean.setText(catBean.getText()/* + "  (" + catBean.getCount() + ")"*/);
							catBean.setAlreadyShowed(true);
						}
					}
				}

				firstFloorCatalogVector.add(catBean);
			} // end while

		} catch(SQLException se) {
			System.err.println("[Get_First_Floor_Catalogs_Exception]: " + se.getMessage());
			CommonLog.error("[Get_First_Floor_Catalogs_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return firstFloorCatalogVector;
	}

	/**
	 * 获取某一目录的父目录
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID
	 * @return 父目录Bean
	 * @throws SQLException
	 */
	public CatalogBean getParentCatBean(Connection con, long tnd_id) throws SQLException {

		// 查询某一目录的父目录SQL
		String sql = "select tnd_id, tnd_title "
			+ " from aeTreeNodeRelation, aeTreeNode "
			+ " where tnr_child_tnd_id=? "	// 传入：某一目录ID
			+ " 	  and tnr_type='TND_PARENT_TND' "
			+ " 	  and tnr_parent_ind=1 "
			+ " 	  and tnr_ancestor_tnd_id=tnd_id ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		CatalogBean catBean = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tnd_id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				catBean = new CatalogBean();

				catBean.setTnd_id(rs.getLong("tnd_id"));
				catBean.setTnd_title(rs.getString("tnd_title"));
			} // end if

		} catch(SQLException se) {
			System.err.println("[Get_Parent_CatBean_Exception]: " + se.getMessage());
			CommonLog.error("[Get_Parent_CatBean_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return catBean;
	}

	/**
	 * 根据当前结点生成目录导航条
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @param tnd_title 目录名
	 * @return 当前结点的导航条
	 * @throws SQLException
	 */
	public NodeNavigationBar buildNavigationBar(Connection con, long tnd_id)
		throws SQLException {

		// 当前结点
		NodeBean currentNodeBean = new NodeBean();
		currentNodeBean.setTnd_id(tnd_id);
//		currentNodeBean.setTnd_title(tnd_title);
		currentNodeBean.setTnd_title(this.getCatalogById(con, tnd_id).getTnd_title());


		// 根据当前结点创建导航条
		NodeNavigationBar catNavBar = new NodeNavigationBar();
		catNavBar.setCur_node(currentNodeBean);	// 在导航条中加入当前结点

		// 查询当前结点的所有祖先结点
		Vector ancetorVector = this.getAncetorsOfOneNode(con, tnd_id);
		if (ancetorVector != null && ancetorVector.size() > 0) {
			catNavBar.setParent_nav(ancetorVector);
		}

		return catNavBar;
	}

	/**
	 * 查询某一目录结点(或课程结点)的所有祖先结点，并按层次排列
	 * @param con 数据库连接器
	 * @param tnd_id 目录ID(对应aeTreeNode表中的tnd_id)
	 * @return 该结点的所有祖先结点集合，并按层次排列
	 * @throws SQLException
	 */
	public Vector getAncetorsOfOneNode(Connection con, long tnd_id)
		throws SQLException {

		// 查询某一目录结点(或课程结点)的所有祖先结点SQL
		String sql = "select treeNode.tnd_id, treeNode.tnd_title, treeNodeR.tnr_order "
			+ " from aeTreeNodeRelation treeNodeR, "		// 用来记录目录与目录、目录与课程的关系表
			+ "		 aeTreeNode	 treeNode "				// 课程与目录的关系结点树
			+ " where treeNodeR.tnr_child_tnd_id=? "		// 传入：某个目录结点的ID(对应aeTreeNode表的tnd_id)
			+ "		  and treeNodeR.tnr_ancestor_tnd_id=treeNode.tnd_id "
			+ " order by tnr_order asc ";

		Vector ancetorVector = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, tnd_id);

			rs = pstmt.executeQuery();
			NodeBean nodeBean = null;
			while (rs.next()) {
				// 创建祖先结点集合
				if (ancetorVector == null) {
					ancetorVector = new Vector();
				}

				nodeBean = new NodeBean();
				nodeBean.setTnd_id(rs.getLong("tnd_id"));			// 结点ID
				nodeBean.setTnd_title(rs.getString("tnd_title"));	// 结点名
				nodeBean.setOrder(rs.getInt("tnr_order"));			// 结点序号

				ancetorVector.add(nodeBean);
			} // end while

		} catch(SQLException se) {
			System.err.println("[Get_Ancetors_Of_One_Node_Exception]: " + se.getMessage());
			CommonLog.error("[Get_Ancetors_Of_One_Node_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return ancetorVector;
	}

	/**
	 * 获取离线课程对应班级的开课时间，取最早开课时间的前两个班级
	 * @param con 数据库连接器
	 * @param itm_id 离线课程ID
	 * @return 离线课程对应班级的开课时间，取最早开课时间的前两个班级
	 * @throws SQLException
	 */
	public HashMap getStartDateOfClass(Connection con, long itm_id) throws SQLException {

		// 获取离线课程对应班级的开课时间SQL
		String sql = "select citm.itm_id, citm.itm_title, "	// 离线课程对应的班级ID，离线课程对应的班级名
			+ " citm.itm_eff_start_datetime "				// 离线课程对应班级的开课日期
			+ " from aeItemRelation citmR "
			+ " inner join aeItem citm on citmR.ire_child_itm_id = citm.itm_id "
			+ " where citmR.ire_parent_itm_id = ? "			// 传入：离线课程ID(对应aeItem表的itm_id)
			+ " 	  and citm.itm_status = 'ON' "
//			+ "       and citm.itm_eff_start_datetime >= getdate() "
			+ " order by itm_eff_start_datetime desc ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		HashMap startDateofClassMap = new HashMap();
		Vector startDateOfClassVec = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, itm_id);

			rs = pstmt.executeQuery();

			int count = 0;
			while (rs.next()) {
				if (startDateOfClassVec == null) {
					startDateOfClassVec = new Vector();
				}

				if (count < 2) {	// 取两个
					HashMap runMap = new HashMap();
					runMap.put("itm_title", rs.getString("itm_title"));
					runMap.put("start_date",rs.getTimestamp("itm_eff_start_datetime"));
					startDateOfClassVec.add(runMap);
				}

				count++;
			} // end while

			startDateofClassMap.put("start_classes", startDateOfClassVec);
			startDateofClassMap.put("class_count", new Integer(count));

		} catch(SQLException se) {
			System.err.println("[Get_Start_Date_Of_Class_Exception]: " + se.getMessage());
			CommonLog.error("[Get_Start_Date_Of_Class_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return startDateofClassMap;
	}

	/**
	 * 获取混合课程对应班级的开课时间，取最早开课时间的前两个班级
	 * @param con 数据库连接器
	 * @param itm_id 混合课程ID
	 * @return 混合课程对应班级的开课时间，取最早开课时间的前两个班级
	 */
	public HashMap getStartDateofBlendItem(Connection con, long itm_id) throws SQLException {

		// 获取混合课程对应班级的开课时间SQL
		// 取班级开始时间与网上内容开始时间之间的最小值
		String sql = "select citm.itm_id, citm.itm_title, "	// 混合课程对应的班级ID，混合课程对应的班级名
			+ " case when itm_content_eff_start_datetime is null or itm_eff_start_datetime < itm_content_eff_start_datetime then itm_eff_start_datetime "
			+ " 	 else itm_content_eff_start_datetime "
			+ " end itm_eff_start_datetime "
			+ " from aeItemRelation citmR "
			+ " inner join aeItem citm on citmR.ire_child_itm_id = citm.itm_id "
			+ " where citmR.ire_parent_itm_id = ?	"	// 传入：混合课程ID(对应aeItem表的itm_id)
			+ " 	  and citm.itm_status = 'ON' "
//			+ " 	  and citm.itm_eff_start_datetime >= getdate() "
			+ " order by itm_eff_start_datetime desc ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		HashMap startDateofBlendItemMap = new HashMap();
		Vector startDateofBlendItemVec = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, itm_id);

			rs = pstmt.executeQuery();

			int count = 0;
			while (rs.next()) {
				if (startDateofBlendItemVec == null) {
					startDateofBlendItemVec = new Vector();
				}

				if (count < 2) {	// 取两个
					HashMap runMap = new HashMap();
					runMap.put("itm_title", rs.getString("itm_title"));
					runMap.put("start_date",rs.getTimestamp("itm_eff_start_datetime"));
					startDateofBlendItemVec.add(runMap);
				}

				count++;
			} // end while

			startDateofBlendItemMap.put("start_classes", startDateofBlendItemVec);
			startDateofBlendItemMap.put("class_count", new Integer(count));
		} catch(SQLException se) {
			System.err.println("[Get_Start_Date_Of_Blend_Item_Exception]: " + se.getMessage());
			CommonLog.error("[Get_Start_Date_Of_Blend_Item_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return startDateofBlendItemMap;

	}

	/**
	 * 构建我的能力分类栏
	 * @param con 数据库连接哭
	 * @param usr_ent_id 用户ID
	 * @param cur_lan 语言
	 * @return 我的能力分类集
	 * @throws SQLException
	 */
	public Vector buildMySkillBaseGadget(Connection con, long usr_ent_id, String cur_lan)
		throws SQLException {

		// 能力分类栏
		Vector buildSkillBaseVector = new Vector();

		Vector mySkillBaseVector = this.getMySkillBases(con, usr_ent_id);	// 获取学员岗位上的能力
		if (mySkillBaseVector != null && mySkillBaseVector.size() > 0) {

			// 构建形如{id:'11,12,14,15',   		//当前学员具有的所有能力ID(skb_ske_id)，多个时用逗号","分隔
		    //  	   text:'--我岗位上的能力--',  //由后台读取label产生，要实现多语言
		    // 		  }
			buildSkillBaseVector.add((CompetenceNode)this.buildMyAllSkillBaseBean(mySkillBaseVector, cur_lan));

			// 将学员岗位上的能力加入Vector，以供前台JSon输出
			buildSkillBaseVector.addAll(mySkillBaseVector);
		}

		return buildSkillBaseVector;
	}

	/**
	 * 构建能力分类目录树
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @param inLang 语言
	 * @return 按能力分类目录树
	 * @throws SQLException
	 */
	public Vector buildSkillSetCat(Connection con, long usr_ent_id, String inLang) throws SQLException {
		Vector skillVector = new Vector();

		Vector mySkillBaseVector = this.getMySkillBases(con, usr_ent_id);	// 获取学员岗位能力集合
		if (mySkillBaseVector != null) {

			// 构建一个"按岗位分类"的顶层目录
			CompetenceNode setNode = new CompetenceNode();
			setNode.setId(this.getNextId());
			setNode.setSke_id(Course.NODE_TYPE_SKP);
			setNode.setText(LangLabel.getValue(inLang, "lab_skillSet"));	// lab_skillSet=以岗位分类
			setNode.setChoice(false);
			setNode.setChildren(this.buildSkillsBySet(con));

			skillVector.addElement(setNode);

			// 构建一个"按复合能力分类"的顶层目录
			CompetenceNode skillNode = new CompetenceNode();
			setNode.setId(this.getNextId());
			skillNode.setSke_id(Course.NODE_TYPE_CSP);
			skillNode.setText(LangLabel.getValue(inLang, "lab_skillGroup"));// lab_skillGroup=以复合能力分类
			skillNode.setChoice(false);
			skillNode.setChildren(this.buildSkillsByComplex(con));

			skillVector.addElement(skillNode);

			// 构建一个"按学员岗位上的能力"的顶层目录
			CompetenceNode mySkillNode = this.buildMyAllSkillBaseBean(mySkillBaseVector, inLang);
			if (mySkillNode == null) {
				mySkillNode = new CompetenceNode();

				mySkillNode.setId(this.getNextId());
				mySkillNode.setSke_id("");
				mySkillNode.setText(LangLabel.getValue(inLang, "lab_usr_all_skills"));	// lab_usr_all_skills=--我岗位上的能力--
			}
			mySkillNode.setChildren(mySkillBaseVector);

			skillVector.addElement(mySkillNode);
		}

		return skillVector;
	}

	/**
	 * 按岗位分类的能力目录树
	 * @param con 数据库连接器
	 * @return 按岗位分类的目录树
	 * @throws SQLException
	 */
	public Vector buildSkillsBySet(Connection con) throws SQLException {

		// 按岗位分类的能力目录树SQL
		String sql = "select sks_ske_id, sks_title, "	// 岗位在cmSkillEntity表(岗位能力实体表)的ske_id，岗位名
			+ " parent.skb_ske_id parent_skb_ske_id, parent.skb_title parent_skb_title, "	// 岗位中的某个能力所属的复合能力在cmSkillEntity表(岗位能力实体表)的ske_id，岗位中的某个能力所属的复合能力名
			+ " child.skb_ske_id, child.skb_title "		// 岗位中的能力在cmSkillEntity表(岗位能力实体表)的ske_id，岗位中能力名
			+ " from  cmSkillSet, "		// 岗位表
			+ " cmSkillSetCoverage, "	// 岗位与能力的关系表
			+ " cmSkillBase child, "	// 能力表
			+ " cmSkillBase parent "	// 用于查询能力所属的复合能力表
			+ " where sks_skb_id = ssc_sks_skb_id "
			+ " 	  and ssc_skb_id = child.skb_id "
			+ " 	  and child.skb_parent_skb_id = parent.skb_id "
			+ "		  and child.skb_delete_timestamp is null and parent.skb_delete_timestamp is null "
			+ " order by sks_ske_id, parent.skb_ske_id, child.skb_ske_id ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Vector skillSetVector = new Vector();	// 岗位结点集合

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			long skillSetIdByGrouping = 0;		// 能力所属的岗位ID(记载按岗位分组的ID)
			CompetenceNode skillSetNode = null;	// 岗位结点
			while (rs.next()) {
				long sks_ske_id = rs.getLong("sks_ske_id");

				if (skillSetIdByGrouping != sks_ske_id) {

					// 创建岗位结点
					skillSetNode = new CompetenceNode();

					skillSetNode.setId(this.getNextId());
					skillSetNode.setSke_id(new Long(sks_ske_id).toString());	// 岗位ID
					skillSetNode.setText(rs.getString("sks_title"));			// 岗位名
					if (skillSetNode.getChildren() == null) {
						skillSetNode.setChildren(new Vector());
					}

					skillSetVector.addElement(skillSetNode);	// 将新的岗位加入到岗位目录树中

					skillSetIdByGrouping = sks_ske_id;	// 记下该岗位ID，以备创建新的不同的岗位结点
				}

				// 加入岗位下的能力结点到该岗位中
				CompetenceNode skillNode = new CompetenceNode();
				skillNode.setId(this.getNextId());
				skillNode.setSke_id(rs.getString("skb_ske_id"));
				skillNode.setText(rs.getString("parent_skb_title") + "/" + rs.getString("skb_title"));

				if (skillSetNode != null) {
					skillSetNode.getChildren().addElement(skillNode);
				}

			} // end while

		} catch(SQLException se) {
			System.err.println("[Get_SkillSets_Exception]: " + se.getMessage());
			CommonLog.error("[Get_SkillSets_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return skillSetVector;
	}

	/**
	 * 按复合能力分类的能力目录树
	 * @param con 数据库连接器
	 * @return 按复合能力分类的能力目录树
	 * @throws SQLException
	 */
	public Vector buildSkillsByComplex(Connection con) throws SQLException {

		// 按复合能力分类的能力目录树SQL
		String sql = "select parent.skb_id parent_skb_id, parent.skb_ske_id parent_skb_ske_id, parent.skb_title parent_skb_title, "	// 复合能力在cmSkillEntity表(岗位能力实体表)的ske_id，复合能力名
			+ " child.skb_ske_id, child.skb_title "	// 能力在cmSkillEntity表(岗位能力实体表)的ske_id，能力名
			+ " from cmSkillBase parent "			// 复合能力表
			+ " left join cmSkillBase child on parent.skb_id=child.skb_parent_skb_id "	// 用于查询复合能力下的能力的表
			+ " where parent.skb_type='GROUP' and parent.skb_delete_timestamp is null and child.skb_delete_timestamp is null "
			+ " order by parent.skb_id, child.skb_ske_id ";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Vector complexSkillVector = new Vector();	// 复合能力结点集合

//		try {
//			pstmt = con.prepareStatement(sql);
//			rs = pstmt.executeQuery();
//
//			long complexSkillIdByGrouping = 0;		// 能力所属的复合能力ID(记载按复合能力分组的ID)
//			CompetenceNode complexSkillNode = null;	// 复合能力结点
//			while (rs.next()) {
//				long parent_skb_id = rs.getLong("parent_skb_id");
//
//				if (complexSkillIdByGrouping != parent_skb_id) {
//
//					// 创建复合能力结点
//					complexSkillNode = new CompetenceNode();
//
//					complexSkillNode.setId(this.getNextId());
//					complexSkillNode.setSke_id(new Long(parent_skb_id).toString());	// 复合能力结点ID
//					complexSkillNode.setText(rs.getString("parent_skb_title"));	// 复合能力结点名
//					if (complexSkillNode.getChildren() == null) {
//						complexSkillNode.setChildren(new Vector());
//					}
//
//					complexSkillVector.addElement(complexSkillNode);// 将新的复合能力结点加入到复合能力目录树中
//
//					complexSkillIdByGrouping = parent_skb_id;		// 记下该复合能力结点ID，以备创建新的不同的复合能力结点
//				}
//
//				// 加入复合能力结点下的能力结点到该复合能力结点中
//				CompetenceNode skillNode = new CompetenceNode();
//				skillNode.setId(rs.getString("skb_ske_id"));
////				System.out.println("String: " + rs.getString("skb_ske_id"));// 测试从表的整形字段中获取为NULL的String值，结果："null"
////				System.out.println("Long: " + rs.getLong("skb_ske_id"));	// 测试从表的整形字段中获取为NULL的long值，结果：0
//				skillNode.setText(rs.getString("parent_skb_title") + "/" + rs.getString("skb_title"));
//
//				if (complexSkillNode != null && rs.getLong("skb_ske_id") > 0) {
//					complexSkillNode.getChildren().addElement(skillNode);
//				}
//
//			} // end while
//
//		} catch(SQLException se) {
//			System.err.println("[Get_SkillSets_Exception]: " + se.getMessage());
//			throw se;
//		} finally {
//			// 关闭相关的数据库操作
//			if (rs != null) {
//				rs.close();
//				rs = null;
//			}
//
//			if (pstmt != null) {
//				pstmt.close();
//				pstmt = null;
//			}
//		}

		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				CompetenceNode skillNode = new CompetenceNode();
				skillNode.setId(this.getNextId());
				skillNode.setSke_id(rs.getString("skb_ske_id"));
//				System.out.println("String: " + rs.getString("skb_ske_id"));// 测试从表的整形字段中获取为NULL的String值，结果："null"
//				System.out.println("Long: " + rs.getLong("skb_ske_id"));	// 测试从表的整形字段中获取为NULL的long值，结果：0
				skillNode.setText(rs.getString("parent_skb_title") + "/" + rs.getString("skb_title"));

				complexSkillVector.add(skillNode);
			} // end while

		} catch(SQLException se) {
			System.err.println("[Get_SkillSets_Exception]: " + se.getMessage());
			CommonLog.error("[Get_SkillSets_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return complexSkillVector;
	}


	/**
	 * 获取指定学员岗位上的能力
	 * @param con 数据库连接器
	 * @param usr_ent_id 用户ID
	 * @return 学员岗位上能力的集合
	 * @throws SQLException
	 */
	public Vector getMySkillBases(Connection con, long usr_ent_id) throws SQLException {

		// 查看学员所属岗位上的能力SQL(思路：用户ID->岗位->能力)
		String sql = "select skillBase.skb_ske_id, skillBase.skb_id, skillBase.skb_title, "	// 能力在cmSkillEntity表(岗位能力实体表)的ske_id，能力ID，能力名
			+ " skillSet.sks_ske_id, skillSet.sks_skb_id, sks_title, "		// 岗位在cmSkillEntity表(岗位能力实体表)的ske_id，岗位ID，岗位名
			+ " skillBaseParent.skb_ske_id parent_skb_ske_id, skillBaseParent.skb_title parent_skb_title "	// 所属的复合能力在cmSkillEntity表(岗位能力实体表)的ske_id，所属的复合能力名
			+ " from RegUserSkillSet uSkillSetR,	"	// 用户与岗位的关系表
			+ " 	 cmSkillSet	skillSet, "				// 岗位表
			+ " 	 cmSkillSetCoverage	ssC,	"		// 岗位与能力关系表
			+ " 	 cmSkillBase skillBase, "			// 能力表
			+ "		 cmSkillBase skillBaseParent	"	// 能力表(再次连接此表可以查询出能力所属的复合能力)
			+ " where uSkillSetR.uss_ent_id=? "	// 传入：学员ID(RegUser表的usr_ent_id)
			+ "  	  and uSkillSetR.uss_ske_id=skillSet.sks_ske_id "
			+ "		  and skillSet.sks_type='SKP' "
			+ "		  and ssC.ssc_sks_skb_id=skillSet.sks_skb_id "
			+ "		  and ssC.ssc_skb_id=skillBase.skb_id "
			+ "  	  and skillBase.skb_parent_skb_id=skillBaseParent.skb_id "
			+ " 	  and skillBase.skb_delete_timestamp is null and skillBaseParent.skb_delete_timestamp is null "
			+ " order by parent_skb_ske_id";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Vector mySkillBaseVector = null;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, usr_ent_id);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				if(mySkillBaseVector == null) {
					mySkillBaseVector = new Vector();
				}

				CompetenceNode cNode = new CompetenceNode();

				cNode.setId(this.getNextId());
				cNode.setSke_id(rs.getString("skb_ske_id"));	// 能力ID
				cNode.setText(rs.getString("parent_skb_title") + "/" + rs.getString("skb_title"));// 能力名

				mySkillBaseVector.add(cNode);

			} // end while

		} catch(SQLException se) {
			System.err.println("[Get_My_SkillBases_Exception]: " + se.getMessage());
			CommonLog.error("[Get_My_SkillBases_Exception]: " + se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}

			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}

		return mySkillBaseVector;
	}

	/**
	 * 构建学员岗位上的能力的Bean(其中该Bean的id值形如"1,2,3")
	 * @param cNodeVector 学员所具有的所有能力的集合
	 * @param inLang 语言，通常传入prof.cur_lan
	 * @return 学员岗位上的能力的Bean
	 * @throws SQLException
	 */
	public CompetenceNode buildMyAllSkillBaseBean(Vector cNodeVector, String inLang)
		throws SQLException {

		CompetenceNode cNode = null;

		int size = (cNodeVector != null) ? cNodeVector.size() : 0;
		if (cNodeVector != null && size > 0) {
			cNode = new CompetenceNode();

			String ske_id = "";
			cNode.setText(LangLabel.getValue(inLang, Course.LAB_USR_ALL_SKILLS));

			int count = 1;
			for (Iterator iter = cNodeVector.iterator(); iter.hasNext();) {
				String idStr = ((CompetenceNode)iter.next()).getSke_id();

				// 加入第count个
				if (!idStr.equals("")) {
					ske_id += idStr;
				}

				count++;
				if (count <= size) {
					ske_id += ",";
				}

			} // end for

			cNode.setId(this.getNextId());
			cNode.setSke_id(ske_id);
		}

		return cNode;
	}



	/**
	 * 获取面授课程安排
	 * @param con
	 * @param param
	 * @param lesnVc
	 * @return
	 * @throws SQLException
	 */
	public String getLessonLst(Connection con,CourseModuleParam param ,Vector lesnVc) throws SQLException{
		String sql="Select"
			+ " ils_id,ils_itm_id,"
			+ " ils_title,"
			+ " ils_day,"
			+ " ils_start_time,"
			+ " ils_end_time,"
			+ " ils_create_timestamp,"
			+ " ils_create_usr_id,"
			+ " ils_update_timestamp,"
			+ " ils_update_usr_id,"
			+ " ils_place "
			+ " from aeItemLesson where ils_itm_id=?";
		if(param.getSort()!=null){
			sql=sql+" order by "+param.getSort()+" "+param.getDir();
		}else{
			sql=sql+" order by ils_day,ils_start_time asc";
		}
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setLong(1, param.getItm_id());
		ResultSet rs=stmt.executeQuery();
		StringBuffer idBuf =new StringBuffer();
		int count=0;
		while(rs.next()){
			/*if(count>=param.getStart() &&count<=(param.getLimit()+param.getStart())){ */
				LessionBean lesn= new LessionBean();
				lesn.setIls_id(rs.getLong("ils_id"));
				idBuf.append(",").append(lesn.getIls_id());
				lesn.setIls_title(rs.getString("ils_title"));
				lesn.setIls_day(rs.getInt("ils_day"));
				lesn.setIls_start_time(rs.getTimestamp("ils_start_time"));
				lesn.setIls_end_time(rs.getTimestamp("ils_end_time"));
				lesn.setIls_place(rs.getString("ils_place"));
				lesnVc.add(lesn);
			/*}*/
			count++;
		}
		if(stmt !=null) stmt.close();
		param.setTotal_rec(count);
		if(idBuf.length()>0){
			idBuf.deleteCharAt(0);
		}
		return idBuf.toString();
	}
	/**
	 * 获取讲师
	 * @param con
	 * @param ils_id_str
	 * @param tchHs
	 * @throws SQLException
	 */
	public void getTeacherHs(Connection con, String ils_id_str ,Hashtable tchHs) throws SQLException{
		String sql="select ili_ils_id,usr_ent_id,usr_display_bil" +
				" from reguser,aeItemLessonInstructor" +
				" where ili_ils_id in ( " +ils_id_str+" )"+
				" and ili_usr_ent_id=usr_ent_id";
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs=stmt.executeQuery();
		while(rs.next()){
			TeacherBean tch =new TeacherBean();
			tch.setUsr_ent_id(rs.getLong("usr_ent_id"));
			tch.setUsr_display_bil(rs.getString("usr_display_bil"));
			Long idObj= new Long( rs.getLong("ili_ils_id"));
			Hashtable recHs =new Hashtable();
			int resCut=1;
			if(tchHs.containsKey(idObj)){
				recHs=(Hashtable)tchHs.get(idObj);
				resCut=recHs.size();
				recHs.put(new Integer(resCut+1), tch);
				tchHs.put(idObj, recHs);
			}else{
				recHs.put(new Integer(resCut), tch);
				tchHs.put(idObj, recHs);
			}
		}
		if(stmt !=null) stmt.close();
	}

	/**
	 * 查询完成准则中设置基本信息、完成率、出席率等
	 * @param con
	 * @param tkh_id
	 * @param res_id
	 * @return
	 * @throws SQLException
	 */
	public CCRBean getCCR_id(Connection con,long tkh_id ,long res_id) throws SQLException{
		String sql="select ccr_id,ccr_pass_ind,ccr_pass_score,att_rate,ccr_attendance_rate,ccr_offline_condition " +
			"	From CourseCriteria" +
			"	inner join course on (ccr_itm_id = cos_itm_id and cos_res_id = ?)" +
			" 	left join (" +
			" 		select app_itm_id, att_rate From aeApplication ,aeAttendance   " +
			"		where app_id = att_app_id and app_tkh_id = ? " +
			"	) att on (ccr_itm_id = app_itm_id ) " ;
		PreparedStatement stmt = con.prepareStatement(sql);
		int index = 1;
		stmt.setLong(index++, res_id);
		stmt.setLong(index++, tkh_id);
		ResultSet rs=stmt.executeQuery();
		CCRBean ccr= new CCRBean();
		if(rs.next()){
			ccr.setCcr_id(rs.getLong("ccr_id"));
			ccr.setCcr_pass_score(rs.getFloat("ccr_pass_score"));
			ccr.setCcr_pass_ind(rs.getBoolean("ccr_pass_ind"));
			ccr.setAtt_rate(rs.getInt("att_rate"));
			ccr.setCcr_attendance_rate(rs.getInt("ccr_attendance_rate"));
			ccr.setCcr_offline_condition(rs.getString("ccr_offline_condition"));
		}
		if(stmt !=null)stmt.close();
		return ccr;
	}
	/**
	 * 根据完成准则ID查询设置的计分项，完成条件等
	 * @param con
	 * @param tkh_id
	 * @param res_id
	 * @param ccr_id
	 * @return Vector
	 * @throws SQLException
	 */
	public Vector getCMTAndOfflineRs(Connection con,long tkh_id ,long res_id,long ccr_id) throws SQLException{
		String sql="select cmt_is_contri_by_score,cmt_id,cmt_title,cmt_max_score,cmt_contri_rate,cmt_pass_score,cmt_duration, cmt_place," +
				" cmt_status,cmt_status_desc_option,mtv_status,mtv_score,res_status,res_id,res_title,res_subtype,mov_status,cmr.* From CourseMeasurement " +
				" left join (" +
				"  select cmr_ccr_id ,cmr_id,cmr_contri_rate, cmr_status,cmr_status_desc_option,res_id,res_type,res_title,res_status,res_subtype," +
				" res_desc,mod_max_score,mod_pass_score,mov_score,mov_status,mov_tkh_id" +
				" From CourseModuleCriteria, Resources, Module left join ModuleEvaluation on (mov_mod_id = mod_res_id and mov_tkh_id =? )" +
				" where cmr_del_timestamp is null    and cmr_res_id = res_id and res_id = mod_res_id  " +
				" ) cmr on (cmt_cmr_id = cmr.cmr_id)" +
				" left join measurementEvaluation on (cmt_id = mtv_cmt_id and mtv_tkh_id = ?)" +
				" where cmt_delete_timestamp is null and cmt_ccr_id = ?  ";
		
		String sql_sp = "{call SP_getCMTAndOfflineRs(?,?)}";
		PreparedStatement stmt = null;
	    CallableStatement proc = null;
	    ResultSet rs = null;
		if(cwSQL.DBVENDOR_MSSQL.equalsIgnoreCase(cwSQL.getDbType())){
    	    stmt = con.prepareStatement(sql_sp);
    		stmt.setLong(1, tkh_id);
    		stmt.setLong(2, ccr_id);
    		rs=stmt.executeQuery();
		}
//		else if(cwSQL.DBVENDOR_ORACLE.equalsIgnoreCase(cwSQL.getDbType())){
//		    proc = con.prepareCall(sql_sp);
//		    proc.setInt(1, (int)tkh_id);
//		    proc.setInt(2,(int)ccr_id);
//		    proc.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
//            proc.execute();
//            rs = (ResultSet) proc.getObject(3);
//		}
		else{
		    stmt = con.prepareStatement(sql);
	        stmt.setLong(1, tkh_id);
	        stmt.setLong(2, tkh_id);
	        stmt.setLong(3, ccr_id);
	        rs=stmt.executeQuery();
		}
		Vector reVc= new Vector();
		while(rs.next()){
			OfflineAndCompletionBean offAndCom = new OfflineAndCompletionBean();
			offAndCom.setCmr_contri_rate(rs.getInt("cmr_contri_rate"));
			offAndCom.setCmr_id(rs.getLong("cmr_id"));
			offAndCom.setCmr_status(rs.getString("cmr_status"));
			offAndCom.setCmr_status_desc_option(rs.getString("cmr_status_desc_option"));
			offAndCom.setCmt_contri_rate(rs.getInt("cmt_contri_rate"));
			offAndCom.setCmt_duration(rs.getInt("cmt_duration"));
			offAndCom.setCmt_id(rs.getLong("cmt_id"));
			offAndCom.setCmt_is_contri_by_score(rs.getInt("cmt_is_contri_by_score"));
			offAndCom.setCmt_max_score(rs.getFloat("cmt_max_score"));
			offAndCom.setCmt_pass_score(rs.getInt("cmt_pass_score"));
			offAndCom.setCmt_place(rs.getString("cmt_place"));
			offAndCom.setCmt_status(rs.getString("cmt_status"));
			offAndCom.setCmt_status_desc_option(rs.getString("cmt_status_desc_option"));
			offAndCom.setCmt_title(rs.getString("cmt_title"));
			offAndCom.setMod_max_score(rs.getFloat("mod_max_score"));
			offAndCom.setMod_pass_score(rs.getInt("mod_pass_score"));
			offAndCom.setMov_score(rs.getFloat("mov_score"));
			offAndCom.setMtv_score(rs.getFloat("mtv_score"));
			offAndCom.setMtv_status(rs.getString("mtv_status"));
			offAndCom.setRes_status(rs.getString("res_status"));
			offAndCom.setRes_id(rs.getLong("res_id"));
			offAndCom.setRes_title(rs.getString("res_title"));
			offAndCom.setRes_subtype(rs.getString("res_subtype"));
			offAndCom.setMov_status(rs.getString("mov_status"));
			reVc.add(offAndCom);
		}
		if(stmt !=null) stmt.close();
		if (proc != null) {
            proc.close();
        }
		return reVc;
	}
	/**
	 * 查询离线部分
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public Vector getOfflineMeasure(Vector  offAndCom)throws SQLException{
		Vector offVc =new Vector();
		if(offAndCom !=null){
			Iterator iter=offAndCom.iterator();
			while(iter.hasNext()){
				OfflineAndCompletionBean res =(OfflineAndCompletionBean)iter.next();
				if(res.getCmr_id()==0 && !"OFF".equalsIgnoreCase(res.getRes_status())){
					OfflineMeasureBean off =new OfflineMeasureBean();
					off.setCmt_id(res.getCmt_id());
					off.setCmt_title(res.getCmt_title());
					off.setCmt_duration(res.getCmt_duration());
					off.setScore(res.getMtv_score());
					off.setStatus(res.getMtv_status());
					off.setCmt_place(res.getCmt_place());
					off.setCmt_status(res.getCmt_status());
					offVc.add(off);
				}
			}
		}
		return offVc;
	}


	/**查询完成准则
	 * @param rs
	 * @param ccr
	 * @return
	 * @throws SQLException
	 */
	public CompletionCriteriaBean getCompletionCriteria(Vector  offAndCom ,CCRBean ccr, Hashtable onLineStatusHs)throws SQLException{
		Vector scoreVc =new Vector();
		Vector stuVc =new Vector();
		float total_max_score=0.00f;
		float total_score=0.00f;
		boolean is_available=false;
		int noOptionCnt=0;
		if(offAndCom!=null){
			Iterator iter=offAndCom.iterator();
			while(iter.hasNext()){
				OfflineAndCompletionBean res =(OfflineAndCompletionBean)iter.next();
				//if(!"OFF".equalsIgnoreCase(res.getRes_status())){//模块状态
				StudentMeasureBean stu =new StudentMeasureBean();
				if(res.getCmt_is_contri_by_score()==1){
					//计分项目
					ScoreBean score =new ScoreBean();
					score.setRes_status(res.getRes_status());
					score.setId(res.getCmt_id());
					if(res.getCmr_id()==0){
						score.setTitle(res.getCmt_title());
						score.setContri_rate(res.getCmt_contri_rate());
						score.setMax_score(res.getCmt_max_score());
						score.setPass_score_rate(res.getCmt_pass_score());
						score.setScore(res.getMtv_score());
					}else{
						score.setTitle(res.getRes_title());
						score.setContri_rate(res.getCmr_contri_rate());
						score.setMax_score(res.getMod_max_score());
						score.setPass_score_rate(res.getMod_pass_score());
						score.setScore(res.getMov_score());
					}
					total_max_score=total_max_score+(score.getMax_score()*score.getContri_rate())/100;
					if(score.getMax_score()>0){
						total_score=total_score+((score.getScore()*100/score.getMax_score())* score.getContri_rate())/100;
					}else if(score.getScore()<=100){
						total_score=total_score+((score.getScore()*100/100)* score.getContri_rate())/100;
					}else{
						total_score=total_score+0;
					}
					scoreVc.add(score);
					stu.setContri_by_score(true);
				}else{
					stu.setContri_by_score(false);
				}

				stu.setRes_status(res.getRes_status());
				stu.setId(res.getCmt_id());
				stu.setTitle(res.getCmt_title());
				stu.setRes_id(res.getRes_id());
				stu.setRes_title(res.getRes_title());
				stu.setRes_subtype(res.getRes_subtype());
				if(res.getCmr_id()==0){
					stu.setOffline(true);
					stu.setMov_status(res.getMtv_status());
				}else{
					stu.setOffline(false);
					stu.setMov_status(res.getMov_status());
					Long res_id_Long= new Long(res.getRes_id());
					if(res.getCmr_status()!=null){
						onLineStatusHs.put(res_id_Long, res.getCmr_status());
					}
				}
				if(res.getCmt_is_contri_by_score()==1 && res.getCmr_id()==0){
					stu.setStatus(res.getCmt_status());
					stu.setStatus_desc_option(res.getCmt_status_desc_option());
				}else{
					stu.setStatus(res.getCmr_status());
					stu.setStatus_desc_option(res.getCmr_status_desc_option());
				}
				if(stu.getStatus_desc_option()==null){
					noOptionCnt++;
				}
				stuVc.add(stu);
				//}
			}
		}
		float total_pass_score_rate = ccr.getCcr_pass_score();
		float total_pass_score = total_max_score * ccr.getCcr_pass_score()/100;
		ScoreMeasureBean scoreM = new ScoreMeasureBean();
		scoreM.setTotal_max_score(total_max_score);
		scoreM.setTotal_pass_score(total_pass_score);
		scoreM.setTotal_pass_score_rate(total_pass_score_rate);
		String score_str = new java.text.DecimalFormat("#.0").format(total_score);
		total_score =  Float.parseFloat(score_str);
		scoreM.setTotal_score(total_score);
		scoreM.setMeasurement_lst(scoreVc);

		if(total_pass_score_rate>0 ||ccr.getCcr_attendance_rate()>0 ||noOptionCnt!=offAndCom.size()){
			is_available=true;
		}

		AttBean att =new AttBean();
		att.setAttendance_rate(ccr.getAtt_rate());
		att.setPass_attendance_rate(ccr.getCcr_attendance_rate());

		CompletionCriteriaBean comCrt =new CompletionCriteriaBean();
		comCrt.setCondition(ccr.getCcr_offline_condition());
		comCrt.setIs_available(is_available);
		comCrt.setScore_measurement(scoreM);
		comCrt.setStudent_measurement(stuVc);
		comCrt.setAttendance(att);
		return comCrt;
	}

	/**
	 * 查询课程公告
	 * @param con
	 * @param res_id
	 * @return
	 * @throws SQLException
	 */
	public Vector getCosMsg(Connection con , String res_id_str) throws SQLException{
		String sql="SELECT msg_id, msg_usr_id, msg_type, msg_level,  msg_title, msg_body, msg_res_id, msg_status,  msg_type," +
				" msg_begin_date, msg_end_date, msg_upd_date, usr_display_bil " +
				" FROM Message , RegUser" +
				" WHERE msg_type = 'RES' AND msg_usr_id = usr_id AND msg_res_id in " +res_id_str+
				"  ORDER BY msg_upd_date DESC" ;
		PreparedStatement stmt = con.prepareStatement(sql);
		ResultSet rs=stmt.executeQuery();
		Vector msgVc =new Vector();
		while(rs.next()){
			MessageBean msg =new MessageBean();
			msg.setMsg_id(rs.getLong("msg_id"));
			msg.setMsg_title(rs.getString("msg_title"));
			msg.setMsg_type(rs.getString("msg_type"));
			String msg_body=cwSQL.getClobValue(rs, "msg_body");

			String msgDesc = cwUtils.getContentFromHtmlStr(msg_body);
			if (msgDesc!=null && msgDesc.length() > Ann.ANNOUNCEMENT_SHOW_BODY_LENGTH) {
				msgDesc = msgDesc.substring(0,  Ann.ANNOUNCEMENT_SHOW_BODY_LENGTH) + "...";
			}
			msg.setMsg_body(msgDesc);
			msg.setMsg_begin_date(rs.getTimestamp("msg_begin_date"));
			msg.setMsg_update_date(rs.getTimestamp("msg_upd_date"));
			msg.setMsg_end_date(rs.getTimestamp("msg_end_date"));
			msgVc.add(msg);
		}
		if(stmt !=null)stmt.close();
		return msgVc;
	}


	/**
	 * 生成评分的页面显示信息
	 * @return
	 */
	public Vector getCommentLevelDisplayMsg(){
		Vector levelVc =new Vector();
		for(int i=0; i<COMMENT_LEVEL.length; i++){
			CommentLevelBean comLevel =new CommentLevelBean();
			comLevel.setLevel(i+1);
			comLevel.setScore(COMMENT_LEVEL[i]);
			comLevel.setLabel(COMMENT_LABEL[i]);
			levelVc.add(comLevel);
		}
		return levelVc;
	}
	/**
	 * 查询当前学员是否已对课程进行了评论
	 * @param con
	 * @param itm_id
	 * @param usr_ent_id
	 * @param tkh_id
	 * @return
	 * @throws SQLException
	 */
	public boolean hasMyComments(Connection con, long itm_id ,long usr_ent_id ,long tkh_id) throws SQLException{
		String sql=" select count(*) ComTimes from aeItemComments" +
				" where ict_itm_id = ? and ict_ent_id = ? and ict_tkh_id =?";
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		stmt.setLong(2, usr_ent_id);
		stmt.setLong(3, tkh_id);
		ResultSet rs=stmt.executeQuery();
		int count=0;
		if(rs.next()){
			count =rs.getInt("ComTimes");
		}
		if(stmt !=null)stmt.close();
		if(count==0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 获得评分人数及总评分
	 * @param con
	 * @param itm_id
	 * @param usr_ent_id
	 * @param tkh_id
	 * @return
	 * @throws SQLException
	 */
	public CosCommentBean getCosCommentInfo(Connection con, long itm_id ,long usr_ent_id ,long tkh_id) throws SQLException{
		String sql=" select itm_comment_avg_score, itm_comment_total_count, itm_comment_total_score,itm_cfc_id  " +
				"from aeItem where itm_id =?";
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		ResultSet rs=stmt.executeQuery();
		CosCommentBean cosCom= new CosCommentBean();
		if(rs.next()){
			cosCom.setItm_id(itm_id);
			cosCom.setTotal_score(rs.getLong("itm_comment_total_score"));
			cosCom.setTotal_count(rs.getLong("itm_comment_total_count"));
			float avg_score = rs.getFloat("itm_comment_avg_score");
			cosCom.setAvg_score(Math.round(avg_score));
			cosCom.setItm_cfc_id(rs.getLong("itm_cfc_id"));
			boolean hasCommented=hasMyComments(con, itm_id, usr_ent_id, tkh_id);
			cosCom.setHasCommented(hasCommented);
		}
		if(stmt !=null)stmt.close();
		return cosCom;
	}
	
	/**
	 * 根据课程的itm_id获取课程评分的统计信息
	 * @param con
	 * @param itm_id
	 * @return
	 * @throws SQLException
	 */
	public CosCommentBean getCosCommentInfoByItmId(Connection con, long itm_id) throws SQLException {
		//String sql = "select itm_comment_avg_score, itm_comment_total_count, itm_comment_total_score from aeItem where itm_id = ?";
		
		String sql = "select sum(ict_score) total_score,count(ict_id) total_count from aeItemComments where  ict_itm_id = ? and ict_score >0 group by ict_itm_id ";
		
		PreparedStatement stmt = null;
		stmt = con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		ResultSet rs = stmt.executeQuery();
		CosCommentBean cosCom = new CosCommentBean();
		if (rs.next()) {
			cosCom.setItm_id(itm_id);
			//cosCom.setTotal_score(rs.getLong("itm_comment_total_score"));
			//cosCom.setTotal_count(rs.getLong("itm_comment_total_count"));
			//cosCom.setAvg_score(rs.getLong("itm_comment_avg_score"));
			cosCom.setTotal_score(rs.getLong("total_score"));
			cosCom.setTotal_count(rs.getLong("total_count"));
			
			 float favg = 0f;
			 try {
				 BigDecimal score= new BigDecimal(cosCom.getTotal_score());
				 BigDecimal count = new BigDecimal(cosCom.getTotal_count());
				 BigDecimal avg = score.divide(count, 2, BigDecimal.ROUND_HALF_UP);
				 favg = avg.floatValue();
			 } catch(Exception e) {
				 CommonLog.error( e.getMessage(),e);
			 }
			 cosCom.setAvg_score(favg);
		}
		if (stmt != null)
			stmt.close();
		return cosCom;
	}
	
	/**
	 * 获得评分人数及总评分
	 * @param con
	 * @param itm_id
	 * @param usr_ent_id
	 * @param tkh_id
	 * @return
	 * @throws SQLException
	 */
	public CosCommentBean getCosCommentInfoByUser(Connection con, long itm_id ,long usr_ent_id ,long tkh_id) throws SQLException{
		String sql=" select ict_id, ict_itm_id,ict_ent_id, ict_tkh_id ,ict_score,ict_comment,ict_create_timestamp  " +
				"from aeItemComments where ict_itm_id = ? and ict_ent_id = ? and ict_tkh_id = ? order by ict_create_timestamp desc ";
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		stmt.setLong(2, usr_ent_id);
		stmt.setLong(3, tkh_id);
		ResultSet rs=stmt.executeQuery();
		CosCommentBean cosCom= new CosCommentBean();
		if(rs.next()){
			cosCom.setItm_id(itm_id);
			cosCom.setIct_id(rs.getLong("ict_id"));
			cosCom.setIct_itm_id(itm_id);
			cosCom.setIct_ent_id(rs.getLong("ict_ent_id"));
			cosCom.setIct_tkh_id(rs.getLong("ict_tkh_id"));
			cosCom.setIct_score(rs.getLong("ict_score"));
			cosCom.setIct_comment(rs.getString("ict_comment"));
		}
		if(stmt !=null)stmt.close();
		return cosCom;
	}
	
	/**
	 * 获得该课程的所有评分
	 * @param con
	 * @param itm_id
	 * @param usr_ent_id
	 * @param tkh_id
	 * @return
	 * @throws SQLException
	 */
	public List<CosCommentBean> getCosCommentInfoByUserList(Connection con, long itm_id) throws SQLException{
		String sql=" select ict_id, ict_itm_id,ict_ent_id, ict_tkh_id ,ict_score,ict_comment,ict_create_timestamp  " +
				"from aeItemComments where ict_itm_id = ? order by ict_create_timestamp desc ";
		List<CosCommentBean> result = new ArrayList<CosCommentBean>();
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		ResultSet rs=stmt.executeQuery();
		while(rs.next()){
			CosCommentBean cosCom= new CosCommentBean();
			cosCom.setItm_id(itm_id);
			cosCom.setIct_id(rs.getLong("ict_id"));
			cosCom.setIct_itm_id(itm_id);
			cosCom.setIct_ent_id(rs.getLong("ict_ent_id"));
			cosCom.setIct_tkh_id(rs.getLong("ict_tkh_id"));
			cosCom.setIct_score(rs.getLong("ict_score"));
			cosCom.setIct_comment(rs.getString("ict_comment"));
			cosCom.setIct_create_timestamp(rs.getTimestamp("ict_create_timestamp"));
			result.add(cosCom);
		}
		if(stmt !=null)stmt.close();
		return result;
	}
	/**
	 * 获得该课程的所有评分
	 * @param con
	 * @param itm_id
	 * @param usr_ent_id
	 * @param tkh_id
	 * @return
	 * @throws SQLException
	 */
	public List<CosCommentBean> getCosCommentItmId(Connection con, long itm_id) throws SQLException{
/*		String sql = " select  s_cmt_uid, s_cmt_content, s_cmt_is_reply, s_cmt_reply_to_id,";
		sql += " s_cmt_create_datetime, s_cmt_anonymous, s_cmt_module, s_cmt_target_id ";
		sql += " from sns_comment,v_user where s_cmt_uid = u_id and s_cmt_target_id = ? and s_cmt_module = ? order by s_cmt_create_datetime asc ";
*/
		String sql = " select  s_cmt_uid, s_cmt_content,";
			sql += " s_cmt_create_datetime, s_cmt_target_id,s_cmt_anonymous score from ( ";
			sql += " select s_cmt_uid, s_cmt_content,";
			sql += " s_cmt_create_datetime,s_cmt_target_id,s_cmt_anonymous from sns_comment,v_user where s_cmt_uid = u_id and s_cmt_module = 'Course'";
			sql += " union all ";
			sql += " select ict_ent_id,ict_comment,ict_create_timestamp,ict_itm_id,ict_score from aeItemComments";
			sql += " ) t where s_cmt_target_id = ?";
			sql += " order by s_cmt_create_datetime asc,score desc";
		List<CosCommentBean> result = new ArrayList<CosCommentBean>();
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		ResultSet rs=stmt.executeQuery();
		while(rs.next()){
			CosCommentBean cosCom= new CosCommentBean();
			cosCom.setItm_id(itm_id);
			//cosCom.setIct_id(rs.getLong("ict_id"));
			cosCom.setIct_itm_id(itm_id);
			cosCom.setIct_ent_id(rs.getLong("s_cmt_uid"));
			//cosCom.setIct_tkh_id(rs.getLong("ict_tkh_id"));
			cosCom.setIct_score(rs.getLong("score"));
			cosCom.setIct_comment(rs.getString("s_cmt_content"));
			cosCom.setIct_create_timestamp(rs.getTimestamp("s_cmt_create_datetime"));
			result.add(cosCom);
		}
		if(stmt !=null)stmt.close();
		return result;
	}
	/**
	 * 获得评分人数及总评分
	 * @param con
	 * @param itm_id
	 * @param usr_ent_id
	 * @param tkh_id
	 * @return
	 * @throws SQLException
	 */
	public CosCommentBean getCosCommentInfo(Connection con, long itm_id ,long usr_ent_id ,long tkh_id,long itm_cfc_id) throws SQLException{
		String sql=" select itm_comment_avg_score, itm_comment_total_count, itm_comment_total_score  " +
				"from aeItem where itm_id =?";
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		ResultSet rs=stmt.executeQuery();
		CosCommentBean cosCom= new CosCommentBean();
		if(rs.next()){
			cosCom.setItm_id(itm_id);
			cosCom.setTotal_score(rs.getLong("itm_comment_total_score"));
			cosCom.setTotal_count(rs.getLong("itm_comment_total_count"));
			float avg_score = rs.getFloat("itm_comment_avg_score");

			cosCom.setAvg_score(Math.round(avg_score));
			cosCom.setItm_cfc_id(itm_cfc_id);
			boolean hasCommented=hasMyComments(con, itm_id, usr_ent_id, tkh_id);
			cosCom.setHasCommented(hasCommented);
		}
		if(stmt !=null)stmt.close();
		return cosCom;
	}
	/**
	 * 课程是否已经评分
	 * @param con
	 * @param itm_id
	 * @param usr_ent_id
	 * @param tkh_id
	 * @return
	 * @throws SQLException
	 */
	public boolean isExistComment(Connection con, long itm_id,long usr_ent_id,long tkh_id) throws SQLException{
		String sql = "select * from aeItemComments where ict_itm_id = ? and ict_ent_id =? and ict_tkh_id=?";
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		stmt.setLong(1, itm_id);
		stmt.setLong(2, usr_ent_id);
		stmt.setLong(3, tkh_id);
		ResultSet rs=stmt.executeQuery();
		boolean exist = false;
		if(rs.next()){
			exist = true;
		}
		if(stmt !=null) stmt.close();
		return exist;
	}

	/**
	 * 添加课程评论
	 * @return
	 * @throws SQLException
	 */
	public  boolean  addCourseComment(Connection con, long itm_id,loginProfile prof,long tkh_id,long score,String comment) throws SQLException{
		boolean flag;
		//检测评分 评分>0 ，则不可以再评。 感觉去改表结构关联很不好，所以用时间关联
		Timestamp cur_time = cwSQL.getTime(con);
		if(getCountScore(con, itm_id, prof.usr_ent_id, tkh_id) <= 0 && tkh_id >0) {
			flag = addCosScore(con, itm_id, prof, tkh_id, score, comment, cur_time);
			// 发布评价
			ValuationLog val = new ValuationLog();
			val.setS_vtl_create_datetime(cwSQL.getTime(con));
			val.setS_vtl_uid(prof.usr_ent_id);
			val.setS_vtl_module("Course");
			val.setS_vtl_type("Star");
			val.setS_vtl_score(Integer.parseInt(score+""));
			val.setS_vtl_target_id(itm_id);
			insValuationLog(con, val);
			
			calcTotal(con, "Star", itm_id);
			
		}
		flag = addCosComment(con, itm_id, prof, tkh_id, score, comment, cur_time);
		
		return flag;
	}
	
	public void calcTotal(Connection con, String type, long itm_id) throws SQLException {
		String sql = " delete from sns_valuation where s_vlt_type = ? and s_vlt_module = ? and s_vlt_target_id = ?";

		PreparedStatement ps = null;
		try{
			ps = con.prepareStatement(sql);
			int index = 1;
			ps.setString(index++, type);
			ps.setString(index++, "Course");
			ps.setLong(index++, itm_id);
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
		calcStarTotal(con, type, itm_id);
		
	}
	
	// 评价的总分是统计星星评价的平均分
	public void calcStarTotal(Connection con, String type, long itm_id) throws SQLException {
		String sql = " insert into sns_valuation (s_vlt_type, s_vlt_score, s_vlt_module, s_vlt_target_id) ";
		sql += " select ?, sum(s_vtl_score) / count(s_vtl_log_id), ?, ? from sns_valuation_log ";
		sql += " where s_vtl_type = ? and s_vtl_module = ? and s_vtl_target_id = ? ";

		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sql);
			int index = 1;
			ps.setString(index++, type);
			ps.setString(index++, "Course");
			ps.setLong(index++, itm_id);
			ps.setString(index++, type);
			ps.setString(index++, "Course");
			ps.setLong(index++, itm_id);
			ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
	}
	
	public void insValuationLog(Connection con, ValuationLog val) throws SQLException {
		String sql = "insert into sns_valuation_log (s_vtl_type, s_vtl_score, s_vtl_create_datetime, s_vtl_uid, s_vtl_module, s_vtl_target_id) values (?, ?, ?, ?, ?, ?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(sql);
			int index = 1;
	
			ps.setString(index++, val.getS_vtl_type());
			ps.setLong(index++, val.getS_vtl_score());
			ps.setTimestamp(index++, val.getS_vtl_create_datetime());
			ps.setLong(index++, val.getS_vtl_uid());
			ps.setString(index++, val.getS_vtl_module());
			ps.setLong(index++, val.getS_vtl_target_id());
			ps.executeUpdate();
		}finally{
			if (ps != null)
				ps.close();
		}
	}

	
	public Long getCountScore(Connection con, long itm_id, Long usr_ent_id, long tkh_id) throws SQLException {
		String sql = "select max(ict_score) max_score from aeItemComments where ict_itm_id = ? and ict_ent_id = ? ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long max_score = null;
		try {
			int index = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(index++, itm_id);
			stmt.setLong(index++, usr_ent_id);
			//stmt.setLong(index++, tkh_id);
			rs = stmt.executeQuery();
			
			if(rs.next()) {
				max_score = rs.getLong("max_score");
			}
		} finally {
			if(stmt!=null)
				stmt.close();
		}
		return max_score;
	}
	
	public Long getCountScoreById(Connection con, long itm_id, long usr_ent_id, Timestamp timestamp) throws SQLException {
		String sql = "select ict_score from aeItemComments where ict_itm_id = ?  and ict_ent_id =? and ict_create_timestamp = ?";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long ict_score = 0l;
		try {
			int index = 1;
			stmt = con.prepareStatement(sql);
			stmt.setLong(index++, itm_id);
			stmt.setLong(index++, usr_ent_id);
			stmt.setTimestamp(index++, timestamp);
			rs = stmt.executeQuery();
			if(rs.next()) {
				ict_score = rs.getLong("ict_score");
			}
		} finally {
			if(stmt!=null)
				stmt.close();
		}
		return ict_score;
	}
	/**
	 * 添加评分
	 * @param con
	 * @param itm_id
	 * @param total_score
	 * @param score
	 * @param total_count
	 * @return
	 * @throws SQLException
	 */
	public  boolean  addCosScore(Connection con, long itm_id,loginProfile prof,long tkh_id,long score,String comment, Timestamp cur_time) throws SQLException{
		String sql1="insert into aeItemComments (ict_itm_id ,ict_ent_id ,ict_tkh_id ,ict_score ,ict_comment,ict_create_timestamp )" +
				" values(?,?,?,?,?,?)";
		boolean addOk=false;
		PreparedStatement stmt1 = null;
		stmt1=con.prepareStatement(sql1);
		int index=1;
		stmt1.setLong(index++, itm_id);
		stmt1.setLong(index++, prof.usr_ent_id);
		stmt1.setLong(index++, tkh_id);
		stmt1.setLong(index++, score);
		stmt1.setString(index++, comment);
		stmt1.setTimestamp(index++, cur_time);
		int cnt1=stmt1.executeUpdate();
		if(cnt1!=1){
			stmt1.close();
			con.rollback();
		}else{
				String sql="update aeItem set itm_comment_total_score = "+cwSQL.replaceNull("itm_comment_total_score", "0")+" + ?" +
						", itm_comment_total_count = "+cwSQL.replaceNull("itm_comment_total_count", "0")+" + 1" +
						", itm_comment_avg_score = ("+cwSQL.replaceNull("itm_comment_total_score", "0")+"  + ?) / ("+cwSQL.replaceNull("itm_comment_total_count", "0")+" + 1) " +
				" where itm_id=?";
				PreparedStatement stmt = null;
				stmt=con.prepareStatement(sql);
				stmt.setLong(1, score);
				stmt.setLong(2, score);
				stmt.setLong(3, itm_id);
				int cnt=stmt.executeUpdate();
				if(stmt !=null)stmt.close();
				if(cnt!=1){
					addOk= false;
				}else{
					addOk= true;
				}
		}
		return addOk;
	}
	
	public  boolean  addCosComment(Connection con, long itm_id,loginProfile prof,long tkh_id,long score,String comment ,Timestamp cur_time) throws SQLException{
		final String sql = " insert into sns_comment (s_cmt_content, s_cmt_is_reply, s_cmt_reply_to_id, s_cmt_uid, s_cmt_create_datetime, s_cmt_anonymous, s_cmt_module, s_cmt_target_id) values (?, ?, ?, ?, ?, ?, ?, ?) ";

		boolean addOk=false;
		PreparedStatement ps = null;
		ps=con.prepareStatement(sql);
		int index=1;
		ps.setString(index++, comment);
		ps.setBoolean(index++, false);
		ps.setLong(index++, 0);
		ps.setLong(index++, prof.usr_ent_id);
		ps.setTimestamp(index++, cur_time);
		ps.setBoolean(index++, false);
		ps.setString(index++, "Course");
		ps.setLong(index++, itm_id);
		int cnt1 = ps.executeUpdate();
		if(ps !=null)ps.close();
		if(cnt1>0)
		return addOk;
		return !addOk;
	}
	
	/**
	 * 评分统计信息
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public Vector getCommentDetail(Connection con)throws SQLException{
		String sql="select ict_score, count(*) comments_count from aeItemComments " +
				" where ict_itm_id = ? group by ict_score order by ict_score asc" ;
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		ResultSet rs=stmt.executeQuery();
		int i=0;
		Vector comVc =new Vector();
		long total=0;
		while(rs.next()){
			i++;
			CommentDetailBean comDet= new CommentDetailBean();
			comDet.setLevel(i);
			comDet.setCount(rs.getLong("comments_count"));
			total=total+comDet.getCount();
			comVc.add(comDet);
		}
		if(stmt !=null)stmt.close();
		if(!comVc.isEmpty()){
			Iterator iter=comVc.iterator();
			while(iter.hasNext()){
				CommentDetailBean comDet=(CommentDetailBean)iter.next();
				int rate=(int) ((comDet.getCount()*100)/total);
				comDet.setRate(rate);
			}
		}
		return comVc;
	}
	
	/**
	 * 分页获取一门课程评分数据列表
	 * @param con
	 * @param urlp
	 * @return
	 * @throws SQLException
	 */
	@Deprecated
	public static Vector getItemCommentLst(Connection con, aeReqParam urlp) throws SQLException {
		Vector comLst = new Vector();
		
		String sql = "SELECT ict.*, usr_ent_id, usr_display_bil FROM aeItemComments ict INNER JOIN regUser usr ON (usr.usr_ent_id = ict_ent_id) WHERE ict.ict_itm_id = ? ORDER BY ict.ict_create_timestamp";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int page = urlp.page;
		int pageSize = urlp.pageSize;
		
		int start = (page - 1) * pageSize;
		int total = 0;
		
		try {
			ps = con.prepareStatement(sql);
			ps.setLong(1, urlp.itm.itm_id);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				if (total >= start && total < (start + pageSize) ) {
					CosCommentBean cosCom= new CosCommentBean();
					cosCom.setItm_id(urlp.itm_id);
					cosCom.setIct_ent_id(rs.getLong("ict_ent_id"));
					cosCom.setIct_ent_name(rs.getString("usr_display_bil"));
					cosCom.setIct_score(rs.getLong("ict_score"));
					cosCom.setIct_comment(rs.getString("ict_comment"));
					cosCom.setIct_create_timestamp(rs.getTimestamp("ict_create_timestamp"));
					comLst.add(cosCom);
				}
				total++;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			cwSQL.cleanUp(rs, ps);
		}
		
		urlp.total = total;
		
		return comLst;
	}
	
	/**
	 * 分页获取一门课程评分数据列表
	 * @param con
	 * @param urlp
	 * @return
	 * @throws SQLException
	 */
	public static Vector getCosCommentLst(Connection con, aeReqParam urlp) throws SQLException {
		Vector comLst = new Vector();
		Vector<CosCommentBean> repeat = new Vector<CosCommentBean>();

		String sql = " select u.usr_display_bil, s_cmt_uid, s_cmt_content,";
		sql += " s_cmt_create_datetime,  s_cmt_target_id,s_cmt_anonymous score,id,type from ( ";
		sql += " select s_cmt_uid, s_cmt_content,";
		sql += " s_cmt_create_datetime, s_cmt_target_id,s_cmt_anonymous, s_cmt_id id,'sns' type from sns_comment,v_user where s_cmt_uid = u_id and s_cmt_module = ?";
		sql += " union all ";
		sql += " select ict_ent_id,ict_comment,ict_create_timestamp,ict_itm_id,ict_score,ict_id id,'ict' type from aeItemComments";
		sql += " ) t inner join regUser u on (u.usr_ent_id = s_cmt_uid) where s_cmt_target_id = ? ";
		sql += " order by s_cmt_create_datetime asc,score desc";
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		int page = urlp.page;
		int pageSize = urlp.pageSize;
		
		int start = (page - 1) * pageSize;
		int total = 0;
		long itm_id = urlp.itm.itm_id;
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, "Course");
			ps.setLong(2, itm_id);
			
			rs = ps.executeQuery();
			
			while (rs.next()) {
				//if (total >= start && total < (start + pageSize) ) {
					CosCommentBean cosCom= new CosCommentBean();
					cosCom.setItm_id(itm_id);
					cosCom.setIct_id(rs.getLong("id"));
					cosCom.setIct_itm_id(itm_id);
					cosCom.setIct_ent_id(rs.getLong("s_cmt_uid"));
					cosCom.setIct_ent_name(rs.getString("usr_display_bil"));
					//cosCom.setIct_tkh_id(rs.getLong("ict_tkh_id"));
					cosCom.setIct_score(rs.getLong("score"));
					cosCom.setIct_comment(rs.getString("s_cmt_content"));
					cosCom.setIct_create_timestamp(rs.getTimestamp("s_cmt_create_datetime"));
					cosCom.setType(rs.getString("type"));
					comLst.add(cosCom);
				//}
				//total++;
			}
			/** 以下分类的目的：手机端评分同时打了星星的一起显示，pc只点击了星星的单独显示**/
			outsite:for(int i=0; i<comLst.size(); i++) {
				CosCommentBean ccb = (CosCommentBean) comLst.elementAt(i);
				for(int j=i+1;j<comLst.size();j++) {
					if(ccb.getIct_create_timestamp().equals(((CosCommentBean) comLst.elementAt(j)).getIct_create_timestamp())) {
						repeat.add((CosCommentBean)comLst.elementAt(j));
						continue outsite;
					}
				}
			}
			comLst.removeAll(repeat);
			
			repeat.clear();
			for(int i=0; i<comLst.size(); i++ ) {
				if (total >= start && total < (start + pageSize) ) {
					repeat.add((CosCommentBean) comLst.elementAt(i));
				}
				total++;
			}
			
		} catch (SQLException e) {
			throw e;
		} finally {
			cwSQL.cleanUp(rs, ps);
		}
		
		urlp.total = total;
		
		return repeat;
	}
/*	
	public void delCosComment(Connection con,String type,long id, long ent_id) throws SQLException{
		PreparedStatement ps = null;
		String sql = "";
		if("sns".equalsIgnoreCase(type)){
			long itm_id = 0 ;
			Timestamp times = null;
			sql = " select s_cmt_create_datetime,s_cmt_target_id from sns_comment where s_cmt_id = ?";
			ps = null;
			ps=con.prepareStatement(sql);
			ps.setLong(1, id);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				times = rs.getTimestamp("");
				itm_id = rs.getLong("ict_itm_id");
			}
			if(ps !=null)ps.close();
			
			// 删除评论
			sql = " delete from sns_comment where s_cmt_id = ? ";
			ps = null;
			int deled = 0;
			try{
				ps = con.prepareStatement(sql);
				int index = 1;
				ps.setLong(index++, id);
				deled = ps.executeUpdate();
			} finally {
				if (ps != null)
					ps.close();
			}
			//删除了评论，也删除评分
			sql = " delete from aeItemComments where ict_itm_id = ? and ict_create_timestamp = ?";
			ps = null;
			if( deled > 0 && itm_id > 0 && times != null){
				try{
					ps = con.prepareStatement(sql);
					int index = 1;
					ps.setLong(index++, itm_id);
					ps.setTimestamp(index++, times);
					ps.executeUpdate();
				} finally {
					if (ps != null)
						ps.close();
				}	
			}
					
		}else if ("ict".equalsIgnoreCase(type)) {
			//查询需要删除的评分分数
			long score = 0;
			long itm_id = 0;
			Timestamp times = null;
			sql="select ict_score,ict_itm_id,ict_create_timestamp from aeItemComments where ict_id=?";
			ps = null;
			ps=con.prepareStatement(sql);
			ps.setLong(1, id);
			ResultSet rs=ps.executeQuery();
			if(rs.next()){
				score = rs.getLong("ict_score");
				itm_id = rs.getLong("ict_itm_id");
				times = rs.getTimestamp("ict_create_timestamp");
			}
			if(ps !=null)ps.close();
			
			//删除评分
			int deled = 0;
			sql = " delete from aeItemComments where ict_id = ? ";
			ps = null;
			try{
				ps = con.prepareStatement(sql);
				int index = 1;
				ps.setLong(index++, id);
				deled = ps.executeUpdate();
			} finally {
				if (ps != null)
					ps.close();
			}
			
			//删除了评分，也删除评论
			if(deled > 0 && times != null) {
				// 删除评论
				sql = " delete from sns_comment where s_cmt_target_id = ? and s_cmt_create_datetime = ?";
				ps = null;
				try{
					ps = con.prepareStatement(sql);
					int index = 1;
					ps.setLong(index++, itm_id);
					ps.setTimestamp(index++, times);
					ps.executeUpdate();
				} finally {
					if (ps != null)
						ps.close();
				}
			}
			
			//更新评分人数/分数/平均分
			sql="update aeItem set itm_comment_total_score = "+cwSQL.replaceNull("itm_comment_total_score", "0")+" - ?" +
					", itm_comment_total_count = "+cwSQL.replaceNull("itm_comment_total_count", "0")+" - 1" +
					", itm_comment_avg_score = case when itm_comment_total_count = 1 then 0 else ("+cwSQL.replaceNull("itm_comment_total_score", "0")+"  - ?) / ("+cwSQL.replaceNull("itm_comment_total_count", "0")+" - 1) end" +
			" where itm_id=?";
			ps = null;
			ps=con.prepareStatement(sql);
			ps.setLong(1, score);
			ps.setLong(2, score);
			ps.setLong(3, itm_id);
			int cnt=ps.executeUpdate();
			if(ps !=null)ps.close();
			
			//删除 评价的总分是统计星星评价的平均分
			sql = " delete from sns_valuation where s_vlt_type = ? and s_vlt_module = ? and s_vlt_target_id = ?";
			ps = null;
			ps=con.prepareStatement(sql);
			ps.setString(1, Valuation.VALUATION_TYPE_STAR);
			ps.setString(2, Valuation.MODULE_COURSE);
			ps.setLong(3, itm_id);
			ps.executeUpdate();
			if(ps !=null)ps.close();
			
			//删除评论的分数 注意。位置不能挪动
			sql = " delete from sns_valuation_log where s_vtl_type = ? and s_vtl_module = ? and s_vtl_target_id = ? and s_vtl_uid = ?";
			ps = null;
			ps=con.prepareStatement(sql);
			ps.setString(1, Valuation.VALUATION_TYPE_STAR);
			ps.setString(2, Valuation.MODULE_COURSE);
			ps.setLong(3, itm_id);
			ps.setLong(4, ent_id);
			ps.executeUpdate();
			if(ps !=null)ps.close();
			
			//评价的总分是统计星星评价的平均分
			sql = " insert into sns_valuation (s_vlt_type, s_vlt_score, s_vlt_module, s_vlt_target_id) ";
			sql += " select ?, sum(s_vtl_score) / count(s_vtl_log_id), ?, ? from sns_valuation_log ";
			sql += " where s_vtl_type = ? and s_vtl_module = ? and s_vtl_target_id = ? ";
			ps = null;
			ps=con.prepareStatement(sql);
			ps.setString(1, Valuation.VALUATION_TYPE_STAR);
			ps.setString(2, Valuation.MODULE_COURSE);
			ps.setLong(3, itm_id);
			ps.setString(4, Valuation.VALUATION_TYPE_STAR);
			ps.setString(5, Valuation.MODULE_COURSE);
			ps.setLong(6, itm_id);
			ps.executeUpdate();
			if(ps !=null)ps.close();
			

		}
	}
	*/
	/**
	 * 获取课程笔记
	 * @param con
	 * @param tkh_id
	 * @return
	 * @throws SQLException
	 */
	public String getCosNote(Connection con, long tkh_id,long usr_ent_id, long itm_id) throws SQLException{
		String sql="select app_note from aeApplication where app_tkh_id=? and app_ent_id=? and app_itm_id=?";
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		stmt.setLong(1, tkh_id);
		stmt.setLong(2, usr_ent_id);
		stmt.setLong(3, itm_id);
		ResultSet rs=stmt.executeQuery();
		String resultStr=null;
		if(rs.next()){
			resultStr= cwSQL.getClobValue(rs, "app_note");
		}
		if(stmt !=null)stmt.close();
		return resultStr;
	}
	/**
	 * 更新笔记
	 * @param con
	 * @param tkh_id
	 * @param note
	 * @throws SQLException
	 */
	public boolean updCosNote(Connection con, long tkh_id,long usr_ent_id, long itm_id,String note) throws SQLException{
		String sql="update aeApplication set app_note=?  where app_tkh_id=? and app_ent_id=? and app_itm_id=?";
		PreparedStatement stmt = null;
		stmt=con.prepareStatement(sql);
		stmt.setString(1, note);
		stmt.setLong(2, tkh_id);
		stmt.setLong(3, usr_ent_id);
		stmt.setLong(4, itm_id);
		int cnt=stmt.executeUpdate();
		if(stmt !=null)stmt.close();
		if(cnt!=1){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 获取学习进度
	 * @param con
	 * @param tkh_id
	 * @param itm_id
	 * @param usr_ent_id
	 * @return
	 * @throws SQLException
	 */
	public float getCovProgress(Connection con, long tkh_id,long itm_id, long usr_ent_id, boolean isIntegratedLearning) throws SQLException{
		float progress = 0.00f;

		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			String sql = "select cov_progress from CourseEvaluation, course";
			sql += " where cos_res_id=cov_cos_id and  cos_itm_id=?";
			sql += " and cov_ent_id=?";
			sql += " and cov_tkh_id=?";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			stmt.setLong(2, usr_ent_id);
			stmt.setLong(3, tkh_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				progress = rs.getFloat("cov_progress");
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return progress;
	}
	/**
	 *
	 * 获取日程表
	 * @param con
	 * @param prof
	 * @param param
	 * @param resultMap
	 * @throws SQLException
	 */
	public void getLessons(Connection con,loginProfile prof, CourseModuleParam param, HashMap resultMap) throws SQLException{
		Vector lesnVc=new Vector();
		String idStr=getLessonLst(con, param, lesnVc);
		Hashtable tchHs =new Hashtable();
		if(idStr!=null && idStr.length()>0)
			getTeacherHs(con, idStr, tchHs);
		if(!lesnVc.isEmpty()){
			Iterator iter=lesnVc.iterator();
			while(iter.hasNext()){
				LessionBean lesn= (LessionBean)iter.next();
				Long idObj= new Long(lesn.getIls_id());
				if(tchHs.containsKey(idObj)){
					Vector tchVc =new Vector();
					Hashtable recHs =new Hashtable();
					recHs=(Hashtable)tchHs.get(idObj);
					if(recHs.size()>0){
						Enumeration Enum=recHs.elements();
						while(Enum.hasMoreElements()){
							TeacherBean tch =(TeacherBean)Enum.nextElement();
							tchVc.add(tch);
						}
					}
					lesn.setTeachers(tchVc);
				}
			}
		}
		resultMap.put("itm_id", new Long(param.getItm_id()));
		resultMap.put("lessons_lst", lesnVc);
	}
	/**
	 * 获取课程内容页面的信息
	 * @param con
	 * @param prof
	 * @param param
	 * @param resultMap
	 * @param defJsonConfig
	 * @throws SQLException
	 * @throws Exception
	 */
	public boolean isIntegratedLearning(Connection con, long tkh_id) throws SQLException {
		boolean isIntegratedLearning = false;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = " select itm_id, itm_integrated_ind ";
			sql += "  from aeapplication, aeitem  ";
			sql += "  where itm_id = app_itm_id ";
			sql += "  and app_tkh_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, tkh_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getBoolean("itm_integrated_ind")) {
					isIntegratedLearning = true;
				}
			}
			rs.close();
			stmt.close();
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return isIntegratedLearning;
	}

	public void getCosContent(Connection con,loginProfile prof, CourseModuleParam param, HashMap resultMap,JsonConfig defJsonConfig,WizbiniLoader wizbini) throws SQLException, qdbException, cwSysMessage, cwException{

		dbResource resDb= new dbResource();
		long tkh_id=param.getTkh_id();
		long res_id=param.getRes_id();
		resDb.res_id=param.getRes_id();
		resDb.tkh_id=param.getTkh_id();
		Hashtable onLineStatusHs =new Hashtable();

		String cov_status =dbCourseEvaluation.getCovStatus(con,tkh_id);
		//日程
		getLessons(con, prof, param, resultMap);

		boolean isIntegratedLearning = isIntegratedLearning(con, resDb.tkh_id);
		resultMap.put("is_integ_learning", Boolean.valueOf(isIntegratedLearning));
		if (isIntegratedLearning) { // 如果是集成培训
			// 论坛信息
			resultMap.put("forum", getCosForumAsJson(con, res_id));
			// 完成准则
			resultMap.put("completion_requirements", getCosCompeletionRequirement(con, resDb.tkh_id));
			// 课程信息
			resultMap.put("integ_criteria", getIntegCriteria(con,param.getItm_id(),prof.usr_ent_id));
			// 学习进度
			getIntegProgressAsJson(con, resDb.tkh_id, resultMap);
		} else { // 如果是课程或考试
			// 离线部分
			CCRBean ccr = getCCR_id(con, resDb.tkh_id, resDb.res_id);
			Vector offAndCom = getCMTAndOfflineRs(con, tkh_id, res_id, ccr.getCcr_id());
			Vector offM = getOfflineMeasure(offAndCom);
			resultMap.put("offline_measurement", offM);
			// 完成准则
			CompletionCriteriaBean comCrt = getCompletionCriteria(offAndCom, ccr, onLineStatusHs);
			resultMap.put("completion_criteria", comCrt);
		}
		// 网上内容
		resDb.getCosContentListAsJson(con, prof, "", true, resultMap, defJsonConfig, onLineStatusHs, wizbini, cov_status);

		//公告
		aeItem itm =new aeItem();
		long cos_res_id=itm.getResIdByRunId(con,param.getItm_id());
		String res_id_str="("+cos_res_id+","+res_id+")";
		Vector msgVc=getCosMsg(con, res_id_str);
		resultMap.put("msg_lst", msgVc);
		//评分
		aeItemRelation itmRlt = new aeItemRelation();
		long itm_id=0;
		itmRlt.ire_child_itm_id= param.getItm_id();
		itm_id=itmRlt.getParentItemId(con);
		if(itm_id==0){
			itm_id = param.getItm_id();
		}
		Vector levelMsg=getCommentLevelDisplayMsg();
		resultMap.put("comment_level", levelMsg);
		CosCommentBean cosCom=getCosCommentInfo(con, itm_id, prof.usr_ent_id, tkh_id);
		resultMap.put("course_comment", cosCom);
		//学习小组
		StudyGroupDAO sgpDao= new StudyGroupDAO();
		Vector sgpVc=sgpDao.getItmStudyGroups(con, param.getItm_id());
		resultMap.put("sgp_lst", sgpVc);
		//讨论区
		Vector sgpForum=sgpDao.getItmAllDis(con, param.getItm_id());
		resultMap.put("all_forums", sgpForum);
		//笔记
		String note=getCosNote(con, tkh_id,prof.usr_ent_id ,param.getItm_id());
		resultMap.put("app_note", note);
		//进度
		float progress=getCovProgress(con, tkh_id, param.getItm_id(), prof.usr_ent_id, isIntegratedLearning);
		resultMap.put("cov_progress", new Float(progress));
		//更新访问时间
		updCosLastAccTime(con, resDb.res_id, resDb.tkh_id, prof.usr_ent_id);
	}

	// 获取集成培训的学习进度和学习状态
	public void getIntegProgressAsJson(Connection con, long tkh_id, Map resultMap) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String cov_status = null;
		float cov_progress = 0.00f;
		float cov_score = 0.00f;
		try {
			String sql = " select cov_status, cov_progress,cov_score from CourseEvaluation where cov_tkh_id = ? ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, tkh_id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				cov_status = rs.getString("cov_status");
				cov_progress = rs.getFloat("cov_progress");
				cov_score = rs.getFloat("cov_score");
			}
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		resultMap.put("integ_cov_status", cov_status);
		resultMap.put("integ_cov_progress", new Float(cov_progress));
		resultMap.put("integ_cov_score", new Float(cov_score));
	}

	public Vector getIntegCriteria(Connection con, long icc_itm_id, long usr_ent_id) throws SQLException {
		Vector vec = new Vector();

		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		try {
			String sql = "";
			sql += " select icc_id, icc_itm_id, icc_completed_elective_count,  icd_completed_item_count, icd_type, icd_id, ";
			sql += " 	itm_id, itm_code, itm_title, itm_status,itm_type, itm_content_eff_duration, itm_content_eff_end_datetime, ";
			sql += " 	itm_integrated_ind, itm_exam_ind, itm_exam_ind, itm_blend_ind, itm_create_run_ind, itm_ref_ind, itm_run_ind,";
			// 网上课程的开始时间
			sql += " 	case when appn.app_status = 'Admitted' then "+cwSQL.replaceNull("itm_content_eff_start_datetime", "appn.app_upd_timestamp");
			sql += " 	when appn.app_status = 'Pending' or appn.app_status = 'Waiting' then itm_content_eff_start_datetime ";
			sql += " 	else null end content_eff_start_datetime, ";
			// 网上课程的结束时间
			sql += " 	case when appn.app_status = 'Admitted' then " +cwSQL.replaceNull("itm_content_eff_end_datetime",cwSQL.dateadd("appn.app_upd_timestamp","itm_content_eff_duration" )) ;
			sql += " 	when appn.app_status = 'Pending' or appn.app_status = 'Waiting' then itm_content_eff_end_datetime ";
			sql += " 	else null end content_eff_end_datetime, ";
			sql += " 	itm_eff_end_datetime, itm_eff_start_datetime, ";
			sql += " 	appn.app_id, appn.app_status, appn.app_tkh_id, appn.app_create_timestamp, appn.app_upd_timestamp, ";
			sql += " 	cos_res_id, ats_type, att_create_timestamp, ";
			sql += " 	case when appn.app_status = 'Admitted' then appn.app_upd_timestamp else null end app_upd_timestamp ";
			sql += " from IntegCourseCriteria ";
			sql += " 	inner join IntegCompleteCondition on (icc_id = icd_icc_id) ";
			sql += " 	inner join IntegRelationItem on (icd_id = iri_icd_id) ";
			sql += " 	inner join aeItem on (iri_relative_itm_id = itm_id )   ";
			sql += " 	inner join Course on (itm_id = cos_itm_id)  ";
			sql += " 	left join aeapplication appn on (cos_itm_id = appn.app_itm_id and appn.app_ent_id = ? ";
			sql += " 		and not exists (  ";
			sql += " 			select 1 from aeapplication app where appn.app_itm_id = app.app_itm_id ";
			sql += " 			and appn.app_ent_id = app.app_ent_id and appn.app_id < app.app_id ";
			sql += " 		)    ";
			sql += " 	)    ";
			sql += "	left join aeattendance on (appn.app_id = att_app_id) ";
			sql += " 	left join aeattendancestatus on (att_ats_id = ats_id) ";
			sql += "	where icc_itm_id = ? ";
			sql += " 	order by itm_code, itm_title  ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, usr_ent_id);
			stmt.setLong(2, icc_itm_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				long icd_id = rs.getLong("icd_id");
				int icd_completed_item_count = rs.getInt("icd_completed_item_count");
				String icd_type = rs.getString("icd_type");

				long itm_id = rs.getLong("itm_id"); // 课程考试信息
				String itm_code = rs.getString("itm_code");
				String itm_title = rs.getString("itm_title");
				String itm_status = rs.getString("itm_status");
				String itm_type = rs.getString("itm_type");

				long app_id = rs.getLong("app_id");
				long app_tkh_id = rs.getLong("app_tkh_id");
				long cos_res_id = rs.getLong("cos_res_id");
				String app_status = rs.getString("app_status");
				String ats_type = rs.getString("ats_type");

				Map data = new HashMap();
				data.put("icd_id", new Long(icd_id));
				data.put("icd_type", icd_type);
				data.put("icd_completed_item_count", new Integer(icd_completed_item_count));
				
				data.put("itm_id", new Long(itm_id));
				data.put("itm_code", itm_code);
				data.put("itm_title", itm_title);
				data.put("itm_status", itm_status);
				data.put("itm_type", itm_type);
				

				data.put("app_id", new Long(app_id));
				data.put("app_tkh_id", new Long(app_tkh_id));
				data.put("app_status", app_status);
				data.put("cos_res_id", new Long(cos_res_id));
				data.put("ats_type", ats_type);

				boolean itm_blend_ind = rs.getBoolean("itm_blend_ind");
				boolean itm_ref_ind = rs.getBoolean("itm_ref_ind");
				boolean itm_create_run_ind = rs.getBoolean("itm_create_run_ind");
				boolean itm_run_ind = rs.getBoolean("itm_run_ind");
				if (app_id > 0) {
					int itm_content_eff_duration = rs.getInt("itm_content_eff_duration");
					Timestamp att_create_timestamp = rs.getTimestamp("att_create_timestamp");
					Timestamp itm_eff_end_datetime = rs.getTimestamp("itm_eff_end_datetime");
					Timestamp itm_content_eff_end_datetime = rs.getTimestamp("itm_content_eff_end_datetime");

					// 混合课程的网上内容(或网上课程)结束时间
					Timestamp content_eff_end_datetime = null;
					if (itm_content_eff_end_datetime == null && itm_content_eff_duration == 0) {
						// 若网上课程结束日期设定为不限
						content_eff_end_datetime = null;
					} else {
						content_eff_end_datetime = rs.getTimestamp("content_eff_end_datetime");
					}

					String content_status = null;
					if (aeApplication.ADMITTED.equals(app_status)) {
						if (new Study().isCosOfHistory(con, itm_blend_ind, itm_run_ind, itm_create_run_ind, itm_ref_ind,
								itm_eff_end_datetime, content_eff_end_datetime, att_create_timestamp, itm_content_eff_duration,ats_type)) {
							content_status = "end";
						}

						// 若是'未完成'、'已放弃'课程，不论课程结束时间是否已到期，也视为已结束课程
						if (aeAttendanceStatus.STATUS_TYPE_INCOMPLETE.equalsIgnoreCase(ats_type)
							|| aeAttendanceStatus.STATUS_TYPE_WITHDRAWN.equalsIgnoreCase(ats_type)) {
							content_status = "end";
						}
					}
					data.put("content_status", content_status);
				}
				data.put("itm_create_run_ind", Boolean.valueOf(itm_create_run_ind));
				data.put("itm_blend_ind", Boolean.valueOf(itm_blend_ind));
				// 如果是混合课程或者离线课程，假如下属班级信息
				if ((itm_blend_ind == false && itm_run_ind == false && itm_create_run_ind == true && itm_ref_ind == false) /* 离线课程 */||
					(itm_blend_ind == true) /* 混合课程 */ ) {
					if (itm_status != null && itm_status.equals(aeItem.ITM_STATUS_ON)) {
						String sql2 = "";
						sql2 += " select parent.itm_id p_itm_id, ";
						sql2 += " 	child.itm_id c_itm_id, child.itm_code c_itm_code, child.itm_title c_itm_title, child.itm_status c_itm_status, ";
						sql2 += " 	child.itm_id itm_run_ind, ";
						sql2 += " 	appn.app_id, appn.app_status, appn.app_tkh_id, appn.app_create_timestamp, appn.app_upd_timestamp, ";
						sql2 += " 	cos_res_id, ats_type, att_create_timestamp, ";
						sql2 += " 	case when appn.app_status = 'Admitted' then "+cwSQL.replaceNull("child.itm_content_eff_start_datetime", "appn.app_upd_timestamp") ;
						sql2 += " 	when appn.app_status = 'Pending' or appn.app_status = 'Waiting' then child.itm_content_eff_start_datetime  ";
						sql2 += " 		else null end content_eff_start_datetime, ";
						sql2 += " 	case when appn.app_status = 'Admitted' then "+cwSQL.replaceNull("child.itm_content_eff_end_datetime", cwSQL.dateadd( "appn.app_upd_timestamp","child.itm_content_eff_duration" ));
						sql2 += " 	when appn.app_status = 'Pending' or appn.app_status = 'Waiting' then child.itm_content_eff_end_datetime ";
						sql2 += " 		else null end content_eff_end_datetime,  ";
						sql2 += " 	child.itm_content_eff_end_datetime, child.itm_content_eff_duration, ";
						sql2 += " 	child.itm_eff_start_datetime, child.itm_eff_end_datetime,  ";
						sql2 += " 	case when appn.app_status = 'Admitted' then appn.app_upd_timestamp else null end app_upd_timestamp ";
						sql2 += " 	from aeitem child ";
						sql2 += " 	inner join Course on (child.itm_id = cos_itm_id) ";
						sql2 += " 	inner join aeItemRelation on (ire_child_itm_id = itm_id) ";
						sql2 += " 	inner join aeItem parent on (ire_parent_itm_id = parent.itm_id) ";
						sql2 += " 	inner join aeapplication appn on (cos_itm_id = appn.app_itm_id and appn.app_status in ('Admitted', 'Pending') and appn.app_ent_id = ? ";
						sql2 += " 		and not exists (  ";
						sql2 += " 			select 1 from aeapplication app where appn.app_itm_id = app.app_itm_id ";
						sql2 += " 				and appn.app_ent_id = app.app_ent_id and appn.app_status in ('Admitted', 'Pending') and appn.app_id < app.app_id ";
						sql2 += " 		) ";
						sql2 += " 	)    ";
						sql2 += " 	left join aeattendance on (appn.app_id = att_app_id) ";
						sql2 += " 	left join aeattendancestatus on (att_ats_id = ats_id) ";
						sql2 += " 	where parent.itm_id = ? ";
						sql2 += " 	order by parent.itm_code, child.itm_code, child.itm_title  ";

						stmt2 = con.prepareStatement(sql2);
						stmt2.setLong(1, usr_ent_id);
						stmt2.setLong(2, itm_id);
						rs2 = stmt2.executeQuery();

						Vector items = new Vector();
						while (rs2.next()) {
							long p_itm_id = rs2.getLong("p_itm_id"); // 课程考试信息
							long c_itm_id = rs2.getLong("c_itm_id"); // 课程考试信息
							String c_itm_code = rs2.getString("c_itm_code");
							String c_itm_title = rs2.getString("c_itm_title");
							String c_itm_status = rs2.getString("c_itm_status");
							//float c_itm_score = rs2.getFloat("c_itm_score");

							long c_app_id = rs2.getLong("app_id");
							long c_app_tkh_id = rs2.getLong("app_tkh_id");
							long c_cos_res_id = rs2.getLong("cos_res_id");
							String c_app_status = rs2.getString("app_status");
							String c_ats_type = rs2.getString("ats_type");

							Map c_data = new HashMap();
							c_data.put("p_itm_id", new Long(p_itm_id));
							c_data.put("itm_id", new Long(c_itm_id));
							c_data.put("itm_code", c_itm_code);
							c_data.put("itm_title", c_itm_title);
							c_data.put("itm_status", c_itm_status);
							//c_data.put("itm_score", new Float(c_itm_score));

							c_data.put("app_id", new Long(c_app_id));
							c_data.put("app_tkh_id", new Long(c_app_tkh_id));
							c_data.put("cos_res_id", new Long(c_cos_res_id));
							c_data.put("app_status", c_app_status);
							c_data.put("ats_type", c_ats_type);

							Timestamp itm_content_eff_end_datetime = rs2.getTimestamp("itm_content_eff_end_datetime");
							int itm_content_eff_duration = rs2.getInt("itm_content_eff_duration");

							// 离线课程(或混合课程)的结束时间
							Timestamp itm_eff_end_datetime = rs2.getTimestamp("itm_eff_end_datetime");
							Timestamp att_create_timestamp = rs2.getTimestamp("att_create_timestamp");

							String content_status = null;
							if (aeApplication.ADMITTED.equals(c_app_status)) {
								if (new Study().isCosOfHistory(con, itm_blend_ind,
									itm_run_ind, itm_create_run_ind, itm_ref_ind,
									itm_eff_end_datetime, itm_content_eff_end_datetime,
									att_create_timestamp, itm_content_eff_duration,c_ats_type)) {

									content_status = "end";
								}

								// 若是'未完成'、'已放弃'课程，不论课程结束时间是否已到期，也视为已结束课程
								if (aeAttendanceStatus.STATUS_TYPE_INCOMPLETE.equalsIgnoreCase(c_ats_type)
									|| aeAttendanceStatus.STATUS_TYPE_WITHDRAWN.equalsIgnoreCase(c_ats_type)) {
									content_status = "end";
								}
							}
							c_data.put("content_status", content_status);
							items.add(c_data);
						}
						data.put("items", items);
					}
				}
				vec.add(data);
			}
		}finally {
			if (stmt != null) {
				stmt.close();
			}
			if (stmt2 != null) {
				stmt2.close();
			}
		}
		return vec;
	}

	// 取得集成培训必修条件的课程数
	private Map getIntegCriteriaComlulsoryCosCount(Connection con, long itm_id) throws SQLException {
		Map data = new HashMap();

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "";
			sql += " select icd_id, count(iri_relative_itm_id) as count ";
			sql += " from IntegCourseCriteria, IntegCompleteCondition, IntegRelationItem ";
			sql += "  where icc_id = icd_icc_id and icd_id = iri_icd_id ";
			sql += "	and  icd_type = 'COMPULSORY' ";
			sql += "	and  icc_itm_id = ? ";
			sql += "  group by icd_id ";

			stmt = con.prepareStatement(sql);
			stmt.setLong(1, itm_id);
			rs = stmt.executeQuery();

			while (rs.next()) {
				long icd_id = rs.getLong("icd_id");
				int count = rs.getInt("count");
				data.put(new Long(icd_id), new Integer(count));
			}
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
		return data;
	}

	// 取得完成条件的选修条件完成数 -- 集成培训
	public Map getCosCompeletionRequirement(Connection con, long tkh_id) throws SQLException {
		Map data = new HashMap();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = " select icc_completed_elective_count, itm_id ";
			sql += "  from aeapplication, aeitem, IntegCourseCriteria  ";
			sql += "  where itm_id = app_itm_id and icc_itm_id = itm_id ";
			sql += "  and app_tkh_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, tkh_id);
			rs = stmt.executeQuery();
			long icc_completed_elective_count = 0;
			float icc_completed_score = 0;
			if (rs.next()) {
				icc_completed_elective_count = rs.getInt("icc_completed_elective_count");
				//icc_completed_score = rs.getFloat("icc_completed_score");
                
			}
			data.put("elective_count", new Long(icc_completed_elective_count));
			//data.put("icc_completed_score", new Float(icc_completed_score));
			rs.close();
			stmt.close();
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return data;
	}

	// 获取集成培训的论坛信息（包括最近3个主题）
	public Map getCosForumAsJson(Connection con, long res_id) throws SQLException {
		Map data = new HashMap();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = " select rcn_res_id, res_id, res_title, res_type, res_subtype, res_status ";
			sql += "	from resourcecontent, resources  ";
			sql += "	where res_id = rcn_res_id_content ";
			sql += " 	and res_type = 'MOD' ";
			sql += " 	and res_subtype = 'FOR' ";
			sql += " 	and rcn_res_id = ? ";
			stmt = con.prepareStatement(sql);
			stmt.setLong(1, res_id);
			rs = stmt.executeQuery();

			long forum_res_id = 0;
			if (rs.next()) {
				data.put("rcn_res_id", rs.getString("rcn_res_id"));
				data.put("res_id", rs.getString("res_id"));
				data.put("title", rs.getString("res_title"));
				data.put("status", rs.getString("res_status"));
				data.put("type", rs.getString("res_type"));
				data.put("subtype", rs.getString("res_subtype"));

				forum_res_id = rs.getLong("res_id");
			}
			rs.close();
			stmt.close();

			// forum topics
			if (forum_res_id > 0) {
				sql = " select fto_id, fto_res_id, fto_title from forumTopic ";
				sql += " where fto_res_id = ? ";
				sql += " order by fto_create_datetime desc ";
				stmt = con.prepareStatement(sql);
				stmt.setLong(1, forum_res_id);

				rs = stmt.executeQuery();

				int i = 0;
				Vector topicVec = new Vector();
				while (rs.next()) {
					if (i >= 3) {
						break;
					}
					Map topic = new HashMap();
					topic.put("fto_id", rs.getString("fto_id"));
					topic.put("fto_res_id", rs.getString("fto_res_id"));
					topic.put("fto_title", rs.getString("fto_title"));
					topicVec.add(topic);
					i++;
				}
				rs.close();
				stmt.close();
				data.put("topics", topicVec);
			}
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return data;
	}

	/**
	 * to get json of course info.
	 * @param con
	 * @param wizbini
	 * @param xslQuestions
	 * @param prof
	 * @param sess
	 * @param param
	 * @param resultMap
	 * @throws cwException
	 * @throws SQLException
	 * @throws qdbException
	 * @throws cwSysMessage
	 * @throws IOException
	 */
	public void getCourseInfo(Connection con, WizbiniLoader wizbini,
			Hashtable xslQuestions, loginProfile prof, HttpSession sess,
			CourseModuleParam param, HashMap resultMap) throws cwException, SQLException, qdbException, cwSysMessage, IOException {
	    int itmId = (int)param.getItm_id();
	    //get course detail information
        setCourseInfo(con, prof, wizbini, xslQuestions, param, resultMap);

        aeItem itm = new aeItem();
        itm.itm_id = param.getItm_id();
        itm.get(con);

		// get boolean value whether the course has been added to learning plan
		boolean hasAddedPlan = aeLearningSoln.existSoln(con, prof.usr_ent_id, itmId, prof.root_ent_id);
		String itmDir = cwUtils.getFileURL(wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getUrl());
		CourseInfoBean cosInfoBean = getCourseInfoBean(itm, itmDir, hasAddedPlan);
		resultMap.put("itm", cosInfoBean);

		// get catalog list that the course belongs to.
		HashMap cosCatMap = ViewAeTreeNodeDAO.getCosCatalogLst(con, itmId, (int) prof.usr_ent_id);
		Vector cosCatVec = (Vector) cosCatMap.get(ViewAeTreeNodeDAO.COS_CAT_LST);
		Vector cosCatIdVec = (Vector) cosCatMap.get(ViewAeTreeNodeDAO.COS_CAT_ID_LST);
		Vector catFullPathVec = getCatFullPathVec(con, cosCatVec, cosCatIdVec);
		resultMap.put("tnd_lst", catFullPathVec);

		// get relate course list of catalogs that the course belongs to.
		Vector relateCosVec = getRelateCosBean(con, wizbini, prof, sess, cosCatIdVec, param, itmDir);
		resultMap.put("itm_lst", cwUtils.getSpecifiedNumOfVec(relateCosVec, param.getRec_num()));
	}

	/**
	 * to get the basic information of course.
	 * @param itm
	 * @param itmDir upload path of item
	 * @param hasAddedPlan
	 * @return
	 */
	private CourseInfoBean getCourseInfoBean(aeItem itm, String itmDir, boolean hasAddedPlan) {
		CourseInfoBean cosInfoBean = new CourseInfoBean();
		int itmId = (int) itm.itm_id;
		cosInfoBean.setItm_id(itmId);
		cosInfoBean.setItm_title(itm.itm_title);
		cosInfoBean.setItm_type(itm.itm_type);
		cosInfoBean.setItm_dummy_type(itm.itm_dummy_type);
		String itmIconPath = null;
		if (itm.itm_icon != null && !"".equals(itm.itm_icon)) {
			itmIconPath = itmDir + itmId + "/" + itm.itm_icon;
		}
		cosInfoBean.setItm_icon(itmIconPath);
		cosInfoBean.setPlanned(hasAddedPlan);
		return cosInfoBean;
	}

	/**
	 * to get relate coure list.
	 * @param con
	 * @param wizbini
	 * @param xslQuestions
	 * @param prof
	 * @param sess
	 * @param param
	 * @param resultMap
	 * @param itmDir upload path of item
	 * @throws SQLException
	 * @throws cwException 
	 */
	public void getRelateCourse(Connection con, WizbiniLoader wizbini, Hashtable xslQuestions,
			loginProfile prof, HttpSession sess, CourseModuleParam param,HashMap resultMap, String itmDir) throws SQLException, cwException {
	    //get search criteria and get result
	    Vector relateCosVec = getRelateCosBean(con, wizbini, prof, sess, null, param, itmDir);
	    //set json
	    resultMap.put("itm_lst", relateCosVec);
	}

	/**
	 * to get full path of course catalog.
	 * @param con
	 * @param cosCatVec
	 * @param cosCatIdVec
	 * @return
	 * @throws SQLException
	 */
	public Vector getCatFullPathVec(Connection con, Vector cosCatVec, Vector cosCatIdVec) throws SQLException {
	    Vector catFullPathVec = null;
	    if(cosCatVec != null) {
	        //get tnd_title of parent
	        HashMap parentTndLstMap = ViewAeTreeNodeDAO.getParentTndTitle(con, cosCatIdVec);
	        catFullPathVec = new Vector();

	        //compose current catalog to  catalog list of parent
	        for(Iterator iter = cosCatVec.iterator(); iter.hasNext();) {
	            HashMap catMap = (HashMap) iter.next();
	            Long tndId = (Long) catMap.get(ViewAeTreeNodeDAO.MAP_KEY_TND_ID);
	            String tndTitle = (String) catMap.get(ViewAeTreeNodeDAO.MAP_KEY_TND_TITLE);
	            if(parentTndLstMap.get(tndId) != null) {
	                Vector parentTndTitleVec = (Vector) parentTndLstMap.get(tndId);
	                String parentTndTitle = dbUtils.vec2String(parentTndTitleVec, " " + SEPARATOR_GREATER_THAN_SIGN + " ");
	                tndTitle = parentTndTitle + " " + SEPARATOR_GREATER_THAN_SIGN + " " + tndTitle;
	                catMap.put(ViewAeTreeNodeDAO.MAP_KEY_TND_TITLE, tndTitle);
	            }
	            catFullPathVec.addElement(catMap);
	        }
	    }

	    return catFullPathVec;
	}

	/**
	 * to get json bean of relate courses.
	 * @param con
	 * @param prof
	 * @param sess
	 * @param catIdVec
	 * @param param
	 * @param itmDir
	 * @return
	 * @throws SQLException
	 * @throws cwException 
	 */
	public Vector getRelateCosBean(Connection con, WizbiniLoader wizbini, loginProfile prof, HttpSession sess,
			Vector catIdVec, CourseModuleParam param, String itmDir) throws SQLException, cwException {
        Vector relateCosVec = null;
        if(catIdVec != null) {
            // set search condition
            HashMap conditionMap = new HashMap();
            long itmId = param.getItm_id();
            String sessKey = "relate_cos_of_itm_" + itmId;
            if (sess == null || sess.getAttribute(sessKey) == null) {
                long[] catIdArr = cwUtils.vec2longArray(catIdVec);
                long[] itmIdArr = new long[1];
                itmIdArr[0] = itmId;

                conditionMap = new HashMap();
                conditionMap.put("tnd_id_lst", catIdArr);
                conditionMap.put("exclude_itm_id_lst", itmIdArr);
            } else {
                conditionMap = (HashMap) sess.getAttribute(sessKey);
            }

            // get relate course
            Course cos = new Course();
            conditionMap.put("sort", "citm.itm_create_timestamp");
            conditionMap.put("dir", "desc");
            relateCosVec = cos.searchCourses(con, wizbini, prof.usr_ent_id, prof.root_ent_id, prof.cur_lan, conditionMap, param, itmDir);
        }
        return relateCosVec;
	}

	/**
	 * get json of the course by xml of course.
	 * @param con
	 * @param prof
	 * @param wizbini
	 * @param xslQuestions
	 * @param param
	 * @param resultMap
	 * @throws SQLException
	 * @throws cwException
	 * @throws qdbException
	 * @throws cwSysMessage
	 * @throws IOException
	 */
	private void setCourseInfo(Connection con, loginProfile prof, WizbiniLoader wizbini, Hashtable xslQuestions, CourseModuleParam param, HashMap resultMap) throws SQLException, cwException, qdbException, cwSysMessage, IOException  {
	    String itm_tpl_xml = getItmTplXml(con, prof, param, wizbini, xslQuestions);
        StringWriter resultWriter = new StringWriter();
        qdbEnv env = param.getStatic_env();
        String stylesheetAbsolutePath = env.DOC_ROOT + dbUtils.SLASH + env.INI_XSL_HOME + dbUtils.SLASH + param.getStylesheet();
        cwXSL.procAbsoluteXSLFile(itm_tpl_xml, stylesheetAbsolutePath, resultWriter, env.ENCODING, env.DEBUG, env.COMP_XSL, env.INI_XSL_LOG, env.INI_XSL_LOG_ENABLED);
        String html = resultWriter.toString();
        resultMap.put("course_detail", html);
	}

	/**
	 * get xml of item template.
	 * @param con
	 * @param prof
	 * @param param
	 * @param wizbini
	 * @param xslQuestions
	 * @return
	 * @throws SQLException
	 * @throws cwException
	 * @throws qdbException
	 * @throws cwSysMessage
	 * @throws IOException
	 */
	private String getItmTplXml(Connection con, loginProfile prof,CourseModuleParam param, WizbiniLoader wizbini, Hashtable xslQuestions) throws SQLException,
            cwException, qdbException, cwSysMessage, IOException {
        aeItem itm = new aeItem();
        itm.itm_id = param.getItm_id();
        itm.auto_enroll_interval = loadTargetLrnCacheAndCourseEnrollScheduler.Auto_Enroll_Interval;
        itm.get(con, 0);
        itm.setQRInd(con, prof);

        String metaXML = null;
        if (prof != null) {
            AcPageVariant acPageVariant = new AcPageVariant(con);
            acPageVariant.prof = prof;
            acPageVariant.instance_id = itm.itm_id;
            acPageVariant.ent_owner_ent_id = prof.root_ent_id;
            acPageVariant.ent_id = prof.usr_ent_id;
            acPageVariant.rol_ext_id = prof.current_role;
            acPageVariant.root_id = prof.root_id;
            acPageVariant.tc_enable_ind = wizbini.cfgTcEnabled;
            acPageVariant.setWizbiniLoader(wizbini);
            metaXML = acPageVariant.answerPageVariantAsXML(xslQuestions != null ?(String[]) xslQuestions.get(param.getStylesheet()): null);
        }

        metaXML += dbSSOLink.ssoLinkAsXML(prof.root_id, wizbini);
        AcItem acitm = new AcItem(con);
        boolean checkStatus = (!acitm.hasOffReadPrivilege(prof.usr_ent_id,prof.current_role));
        boolean cos_mgt_ind = acitm.hasMaintainPrivilege(itm, prof.usr_ent_id,prof.current_role, prof.root_ent_id);
        itm.app_rol_ext_id = (prof == null) ? null : prof.current_role;
        String itmXml = itm.ItemDetailAsXML(con, param.getStatic_env(), checkStatus,false, param.getTvw_id(), param.isShow_run_ind(), param.isShow_session_ind(), 0, prof.usr_ent_id,cos_mgt_ind, false, false, prof.current_role, prof, false);
        itmXml += "<page_readonly>" + param.isPage_readonly() + "</page_readonly>";
        aeAction aeAct = new aeAction();
        String result = aeAct.formatXML(itmXml, metaXML, "applyeasy", prof, param.getStylesheet());
        return result;
    }

	public static Vector getTreeBeanByTcrLst(List tcrList) {
	    Vector tcrTreeBeanVec = null;
	    if(tcrList != null) {
	        for(Iterator iter = tcrList.iterator(); iter.hasNext();) {
	            if(tcrTreeBeanVec == null) {
	                tcrTreeBeanVec = new Vector();
	            }
	            DbTrainingCenter tcr = (DbTrainingCenter) iter.next();
	            JsonTreeBean jNode = new JsonTreeBean();
	            jNode.setId(tcr.getTcr_id());
	            jNode.setText(tcr.getTcr_title());
	            jNode.setType(cwTree.NODE_TYPE_TC);
	            tcrTreeBeanVec.addElement(jNode);
	        }
	    }

	    return tcrTreeBeanVec;
	}

	public static Vector getTreeBeanByCatVec(Vector catVec) {
		Vector catTreeBeanVec = null;
	    if(catVec != null) {
	        for(Iterator iter = catVec.iterator(); iter.hasNext();) {
	            if(catTreeBeanVec == null) {
	            	catTreeBeanVec = new Vector();
	            }
	            aeCatalog catalog = (aeCatalog) iter.next();
	            JsonTreeBean jNode = new JsonTreeBean();
	            jNode.setId(catalog.cat_treenode.tnd_id);
	            jNode.setText(catalog.cat_title);
	            jNode.setType(cwTree.NODE_TYPE_CATALOG);
	            catTreeBeanVec.addElement(jNode);
	        }
	    }

	    return catTreeBeanVec;
	}


	public static long[] usrGroups(long usr_ent_id, Vector v) {
		long[] list = new long[v.size() + 1];
		list[0] = usr_ent_id;
		for (int i = 0; i < v.size(); i++)
			list[i + 1] = ((Long) v.elementAt(i)).longValue();

		return list;
	}

	/*
	 * get catalog structure for home page of learner.
	 */
	public Vector getCatalogForHome(Connection con, long usr_ent_id, long tcr_id, int floor_num) throws SQLException{
		Vector topCatalogGroupingVector = null;

		Vector topCatBeanVector = getTopCatalogBeans(con, usr_ent_id, tcr_id, true, true, 5);
		if(topCatBeanVector != null) {
			topCatalogGroupingVector = new Vector();
			for (int i = 0; i < topCatBeanVector.size(); i++) {
				long tnd_id = ((CatalogBean)topCatBeanVector.get(i)).getId();// 获取每一个顶层目录ID
				CatalogBean catalogBean = this.viewCatalogs(con, usr_ent_id, true, tnd_id, floor_num, 1, true);	// 获取该目录树
				topCatalogGroupingVector.add(catalogBean);
			}
		}

		return topCatalogGroupingVector;
	}
	/*
	 * get item's icon path
	 */
	public static String getItmIconPath(String itmDir, long itmId, String itmIcon) {
		String itmIconPath = null;
		if (itmIcon != null && !"".equals(itmIcon)) {
			itmIconPath = itmDir + "/" + itmId + "/" + itmIcon;
		}
		return itmIconPath;
	}

	public void updCosLastAccTime(Connection con,long cov_cos_id, long cov_tkh_id, long cov_ent_id) throws cwException, SQLException{
		PreparedStatement stmt=null;
		try{
		    
		    Timestamp cur_time=cwSQL.getTime(con);
            stmt = con.prepareStatement(
                    " UPDATE CourseEvaluation SET "
                    + " cov_commence_datetime = ? "
                    + " WHERE "
                    + " cov_cos_id = ? AND "
                    + " cov_ent_id = ? AND "
                    + " cov_tkh_id = ? and (cov_commence_datetime is null or cov_commence_datetime > ?)" );
            stmt.setTimestamp(1, cur_time);
            stmt.setLong(2, cov_cos_id);
            stmt.setLong(3, cov_ent_id);
            stmt.setLong(4, cov_tkh_id);
            stmt.setTimestamp(5, cur_time);
            stmt.executeUpdate();
            stmt.close();
            
			stmt = con.prepareStatement(
	                " UPDATE CourseEvaluation SET "
	                + " cov_last_acc_datetime = ? "
	                + " WHERE "
	                + " cov_cos_id = ? AND "
	                + " cov_ent_id = ? AND "
	                + " cov_tkh_id = ? " );
			stmt.setTimestamp(1, cur_time);
	        stmt.setLong(2, cov_cos_id);
	        stmt.setLong(3, cov_ent_id);
	        stmt.setLong(4, cov_tkh_id);
	        if (stmt.executeUpdate()!=1){
	            throw new cwException("Failed to update course evaluation .");
	        }
			}  catch(SQLException e) {
	            throw new cwException("SQL Error: " + e.getMessage());
	        } finally{
	        	stmt.close();
	        }
	}

	static final String IMMEDIATE = "IMMEDIATE";
	static final String UNLIMITED = "UNLIMITED";
	static final String[] periods = {IMMEDIATE,
			"LAST_1_WEEK", "LAST_2_WEEK",
			"LAST_1_MONTH", "LAST_2_MONTH",
			UNLIMITED};
	private Timestamp[] getsrhStartEndTime(HashMap conditionMap, Timestamp cur_time) {
		// 以下为时间期限搜索参数，可选值如下：
		// 即时开始 IMMEDIATE、 最近一周 LAST_1_WEEK、 最近两周 LAST_2_WEEK
		// 最近的一个月 LAST_1_MONTH、 最近的两个月 LAST_2_MONTH、 不限 UNLIMITED

		String srh_start_period	= (String) conditionMap.get("srh_start_period");
		Timestamp start_datetime = null;
		Timestamp end_datetime = null;
		if (srh_start_period != null && srh_start_period.length() > 0) {
			// 开始时间段的判断，选课中心的开始日期范围
			boolean flag = false;
			for (int i = 0; i < periods.length && !flag; i++) {
				String elementOfPeriods = periods[i];

				if (srh_start_period != null && !"".equals(srh_start_period)
						&& elementOfPeriods.equalsIgnoreCase(srh_start_period)) { // 若从客户端传来的值srh_start_period不为空且能匹配periods中的值

					if (IMMEDIATE.equalsIgnoreCase(elementOfPeriods)) {
						start_datetime = cur_time;
						end_datetime = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
						flag = true;
					}

					if (elementOfPeriods.endsWith("WEEK")) {
						// 记下第一次出现"_"的索引位置
						int firstUnderlineIndex = elementOfPeriods.indexOf("_");
						// 截取从数字出现到末尾的子串(即"数字_WEEK")
						String subStr = elementOfPeriods.substring(firstUnderlineIndex + 1);
						// 截取数字字符串并转换成数字
						int weekPeriod = Integer.parseInt((subStr.substring(0, subStr.indexOf("_"))));

						Calendar cal = Calendar.getInstance();
						cal.setTime(cur_time);
						cal.add(Calendar.WEEK_OF_MONTH, weekPeriod);
						start_datetime = cur_time;
						end_datetime = new Timestamp(cal.getTime().getTime());
						flag = true;
					}

					if (elementOfPeriods.endsWith("MONTH")) {
						// 记下第一次出现"_"的索引位置
						int firstUnderlineIndex = elementOfPeriods.indexOf("_");
						// 截取从数字出现到末尾的子串(即"数字_MONTH")
						String subStr = elementOfPeriods.substring(firstUnderlineIndex + 1);
						// 截取数字字符串并转换成数字
						int monthPeriod = Integer.parseInt((subStr.substring(0, subStr.indexOf("_"))));

						Calendar cal = Calendar.getInstance();
						cal.setTime(cur_time);
						cal.add(Calendar.MONTH, monthPeriod);
						start_datetime = cur_time;
						end_datetime = new Timestamp(cal.getTime().getTime());
						flag = true;
					}
				} // end outer if
			} // end for
		}

		// 报名日期的开始搜索时间
		String srh_appn_start_datetime = (String) conditionMap.get("srh_appn_start_datetime");
		if ( srh_appn_start_datetime != null && srh_appn_start_datetime.length() > 0) {
			try {
				start_datetime = new Timestamp(dateformat.parse(srh_appn_start_datetime).getTime());
			} catch(ParseException e) {
				CommonLog.error( e.getMessage(),e);
				start_datetime = null;
			}
		}
		// 报名日期的结束搜索时间
		String srh_appn_end_datetime = (String)conditionMap.get("srh_appn_end_datetime");
		if (srh_appn_end_datetime != null && srh_appn_end_datetime.length() > 0) {
			try {
				end_datetime = new Timestamp(dateformat.parse(srh_appn_end_datetime).getTime());
			} catch(ParseException e) {
				CommonLog.error( e.getMessage(),e);
				end_datetime = null;
			}
		}
		if (start_datetime != null && end_datetime == null) {
			end_datetime = Timestamp.valueOf(cwUtils.MAX_TIMESTAMP);
		}
		if (start_datetime == null && end_datetime != null) {
			start_datetime = Timestamp.valueOf(cwUtils.MIN_TIMESTAMP);
		}
		if (start_datetime != null) {
			Calendar cur_cal = Calendar.getInstance();
			cur_cal.setTime(start_datetime);
			cur_cal.set(Calendar.HOUR_OF_DAY, 0);
			cur_cal.set(Calendar.MINUTE,0);
			cur_cal.set(Calendar.SECOND, 0);
			cur_cal.set(Calendar.MILLISECOND,0);
			start_datetime = new Timestamp(cur_cal.getTime().getTime());
		}
		return new Timestamp[]{start_datetime, end_datetime};
	}
	
	public long getTcrId(Connection con , String eip_code)throws SQLException{
		long tcr_id = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select eip_tcr_id from enterpriseInfoPortal where eip_code=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, eip_code);
			rs = pstmt.executeQuery();
			while(rs.next()){
				tcr_id = rs.getLong(1);
			}
		} catch(SQLException se) {
			System.err.println("[GetTcrID According to eip_code]: " + se.getMessage());
			CommonLog.error( se.getMessage(),se);
			throw se;
		} finally {
			// 关闭相关的数据库操作
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pstmt != null) {
				pstmt.close();
				pstmt = null;
			}
		}
		return tcr_id;
	}
	
}
