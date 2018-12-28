package com.cw.wizbank.JsonMod.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import com.cw.wizbank.JsonMod.Ann.Ann;
import com.cw.wizbank.JsonMod.Ann.AnnModuleParam;
import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.Course.CourseModuleParam;
import com.cw.wizbank.JsonMod.commonBean.TCBean;
import com.cw.wizbank.JsonMod.know.Know;
import com.cw.wizbank.JsonMod.know.KnowModuleParam;
import com.cw.wizbank.JsonMod.links.dao.FriendshipLinkDAO;
import com.cw.wizbank.JsonMod.study.Study;
import com.cw.wizbank.JsonMod.study.StudyModuleParam;
import com.cw.wizbank.JsonMod.studyMaterial.StudyMaterial;
import com.cw.wizbank.JsonMod.studyMaterial.StudyMaterialModuleParam;
import com.cw.wizbank.JsonMod.supervise.Supervise;
import com.cw.wizbank.JsonMod.supervise.SuperviseModuleParam;
import com.cw.wizbank.JsonMod.tcrCommon.TcrLogic;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeItemDummyType;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.config.organization.usermanagement.impl.ProfileAttributesTypeImpl;
import com.cw.wizbank.config.organization.usermanagement.impl.UserAttributeTypeImpl;
import com.cw.wizbank.config.organization.usermanagement.impl.UserExtAttributeTypeImpl;
import com.cw.wizbank.config.organization.usermanagement.impl.UserIdTypeImpl;
import com.cw.wizbank.content.Evaluation;
import com.cw.wizbank.credit.view.ViewCreditsDAO;
import com.cw.wizbank.dao.SQLMapClientFactory;
import com.cw.wizbank.dao.SqlMapClientDataSource;
import com.cw.wizbank.db.DbAcRole;
import com.cw.wizbank.db.DbPsnBiography;
import com.cw.wizbank.db.DbSitePoster;
import com.cw.wizbank.db.view.ViewCmAssessment;
import com.cw.wizbank.know.view.ViewUserCreditsUserDAO;
import com.cw.wizbank.qdb.CurrentActiveUser;
import com.cw.wizbank.qdb.SystemSetting;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbPoster;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.studyGroup.StudyGroupDAO;
import com.cw.wizbank.util.JsonHelper;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cw.wizbank.qdb.SitePoster;

public class User {
	private final SqlMapClientDataSource sqlMapClient = SQLMapClientFactory.getSqlMapClient();
	private final TcrLogic tcrLogic = TcrLogic.getInstance();
	private static final String UserID="usr_ste_usr_id";
	private static final String NAME="usr_display_bil";
	private static final String GENDER="usr_gender";
	private static final String DateOfBirth="usr_bday";
	private static final String EMAIL="usr_email";
	private static final String PHONE="usr_tel_1";
	private static final String FAX="usr_fax";
	private static final String JOBTITLE="usr_job_title";
	private static final String JOINDATE="usr_join_date";

	private static final String GROUP="usr_group";

	private static final String GRADE="usr_grade";

    private static final String NICKNAME="usr_nickname";
	private static final String EXTENTSION41="urx_extra_41";
	private static final String EXTENTSION42="urx_extra_42";
	private static final String EXTENTSION43="urx_extra_43";
	private static final String EXTENTSION44="urx_extra_44";
	private static final String EXTENTSION45="urx_extra_45";

	private static final String UPD_DATE="upd_date";

	//for output json
	public static final String JSON_USER_INFO = "usr_info";
	public static final String JSON_COMPETENCE_LIST = "competence_list";
	public static final String JSON_LEARNING_CATALOG = "learning_catalog";
	public static final String JSON_TCR_LST = "tcr_lst";
	public static final String JSON_TND_LST = "tnd_lst";
	public static final String JSON_LEARNING_CENTER = "learning_center";
	public static final String JSON_ITM_LST = "itm_lst";
	public static final String JSON_POP_ITEM = "pop_item";
	public static final String JSON_ESTIMATED_ITEM = "estimated_item";
	public static final String JSON_NEWEST_ITEM = "newest_item";
	public static final String JSON_EXAM_CENTER = "exam_center";
	public static final String JSON_INTEGRATED_LEARNING = "integrated_learning";
	public static final String JSON_ANNOUNCEMENTS = "announcements";
	public static final String JSON_ANN_LST = "ann_lst";
	public static final String JSON_MY_SGP = "my_sgp";
	public static final String JSON_SGP_LST = "sgp_lst";
	public static final String JSON_PENDING_WAITING_ITEM = "pending_waiting_item";
	public static final String JSON_PUBLIC_EVAL = "public_eval";
	public static final String JSON_EVAL_LST = "eval_lst";
	public static final String JSON_FORUM = "forum";
	public static final String JSON_FOR_TOPIC_LST = "for_topic_lst";
	public static final String JSON_SKILL_EVAL = "skill_eval";
	public static final String JSON_ASSESSMENT_LST = "assessment_lst";
	private static final String JSON_SUPERVISE_APP_PEND = "supervise_app_pend";
	private static final String JSON_MATERIAL_CATALOG = "material_cat";
	private static final String JSON_SITE_POSTER = "ste_poster";
	private static final String JSON_FRIENDSHIP_LINK = "fs_link";
	private static final String JSON_CREDIT_RANK = "credit_rank";
	private static final String DEFINED_PROJECT = "DEFINED_PROJECT_";
	private static final String COMPANY_QQ_DATA = "companyQQData";
	private static final String COMPANY_QQ_LIST = "company_qq_list";
	private static final String DEFI_PROJ_LINK = "defi_proj_link";
	private static final String LEFT = "LEFT";
	private static final String CENTRE = "CENTRE";
	private static final String RIGHT = "RIGHT";
	private static final String JSON_TEMPLATE = "template_infor";
	private static final String JSON_USER_CHOICE_TCR_INFOR = "user_choice_tcr_infor";
	private static final String JSON_MY_PROFILE = "my_profile";
	private static final String JSON_MAIN_TCR = "extension30";
	private static final String jSON_DESC_MAIN_TCR = "extension20";

	//for home page tab
	private static final String TAB_QUE_UNANS_LST = "que_unans_lst";
	private static final String TAB_QUE_ANSED_LST = "que_ansed_lst";
	private static final String TAB_QUE_POPULAR_LST = "que_popular_lst";
	private static final String TAB_QUE_FAQ_LST = "que_faq_lst";

	private static final String TAB_POP_ITEM = "pop_item";
	private static final String TAB_ESTIMATED_ITEM = "estimated_item";
	private static final String TAB_NEWEST_ITEM = "newest_item";

	private static final String TAB_MY_SGP = "my_sgp";


	// config the record number of gadget
	private static final int HOME_GETDAT_STUDY_CENTER_RECORD_NUM = 4;
	private static final int HOME_GETDAT_EXAM_CENTER_RECORD_NUM = 4;
	private static final int HOME_GETDAT_TRAIN_CHART_RECORD_NUM = 5;
	private static final int HOME_GETDAT_KNOW_QUE_RECORD_NUM = 5;
	private static final int HOME_GETDAT_ANN_RECORD_NUM = 3;
	private static final int HOME_GETDAT_PEND_WATI_COS_RECORD_NUM = 3;
	private static final int HOME_SUPERVISE_APP_PEND_NUM =200;
	private static final int HOME_GETDAT_MY_STUDY_GROUP_RECORD_NUM = 5;
	public static final int HOME_GETDAT_CREDIT_RANK_RECORD_NUM = 5;

	public HashMap user_detail = new HashMap();


	public Object getUserProfile(Connection conn, long usr_id, WizbiniLoader wizbini, String root_id, JsonConfig defJsonConfig, loginProfile prof) throws qdbException, SQLException
	{

		dbRegUser regUser=new dbRegUser();
		regUser.usr_ent_id=usr_id;
		regUser.get(conn);


		DbPsnBiography dbpbg = new DbPsnBiography();
		dbpbg.getBiographyByEntId(conn, regUser.usr_ent_id);

		String options=dbpbg.pbg_option;

		ProfileAttributesTypeImpl attr=new ProfileAttributesTypeImpl();

		//用户id

		UserIdTypeImpl userid=new UserIdTypeImpl();
		if(options!=null&&options.indexOf(UserID)!=-1)
			userid.setPublicInd(true);
		else
			userid.setPublicInd(false);

		userid.setValue(regUser.usr_ste_usr_id);
		attr.setUserId(userid);



		//用户全名

		UserAttributeTypeImpl fullname=new UserAttributeTypeImpl();
		if(options!=null&&options.indexOf(NAME)!=-1)
			fullname.setPublicInd(true);
		else
			fullname.setPublicInd(false);

		fullname.setValue(regUser.usr_display_bil!=null ?regUser.usr_display_bil:"");
		attr.setName(fullname);


		//用户性别

		UserAttributeTypeImpl  gender=new UserAttributeTypeImpl();
		if(options!=null&&options.indexOf(GENDER)!=-1)
			gender.setPublicInd(true);
		else
			gender.setPublicInd(false);
		gender.setValue(regUser.usr_gender!=null ?regUser.usr_gender:"" );
		attr.setGender(gender);



		//用户出生日期

		UserAttributeTypeImpl  dateBirth=new UserAttributeTypeImpl();
	    dateBirth.setValue(regUser.usr_bday!=null ? JsonHelper.toJsonDateFormat(regUser.usr_bday):"");

		if(options!=null&&options.indexOf(DateOfBirth)!=-1)
			dateBirth.setPublicInd(true);
		else
			dateBirth.setPublicInd(false);

		attr.setDateOfBirth(dateBirth);


		//用户Email

		UserAttributeTypeImpl  email=new UserAttributeTypeImpl();
		email.setValue(regUser.usr_email!=null? regUser.usr_email:"");
		if(options!=null&&options.indexOf(EMAIL)!=-1)
			email.setPublicInd(true);
		else
			email.setPublicInd(false);

		attr.setEmail(email);


		//用户电话

		UserAttributeTypeImpl phone=new UserAttributeTypeImpl();
		phone.setValue(regUser.usr_tel_1!=null ?regUser.usr_tel_1:"");
		if(options!=null&&options.indexOf(PHONE)!=-1)
			phone.setPublicInd(true);
		else
			phone.setPublicInd(false);



		attr.setPhone(phone);


		//用户Fax

		UserAttributeTypeImpl usr_fax=new UserAttributeTypeImpl();
		if(options!=null&&options.indexOf(FAX)!=-1)
			usr_fax.setPublicInd(true);
		else
			usr_fax.setPublicInd(false);
		usr_fax.setValue(regUser.usr_fax_1!=null ?regUser.usr_fax_1:"");
		attr.setFax(usr_fax);

		//用户jobTitle

		UserAttributeTypeImpl jobTitile=new UserAttributeTypeImpl();

		if(options!=null&&options.indexOf(JOBTITLE)!=-1)
			jobTitile.setPublicInd(true);
		else
			jobTitile.setPublicInd(false);
		jobTitile.setValue(regUser.usr_job_title!=null?regUser.usr_job_title:"");

        attr.setJobTitle(jobTitile);


        //用户join_date

        UserAttributeTypeImpl join_date=new UserAttributeTypeImpl();
    	if(options!=null&&options.indexOf(JOINDATE)!=-1)
    		join_date.setPublicInd(true);
		else
			join_date.setPublicInd(false);

    	join_date.setValue(regUser.usr_join_datetime!=null ? JsonHelper.toJsonDateFormat(regUser.usr_join_datetime):"");


        attr.setJoinDate(join_date);

        //group
        UserAttributeTypeImpl Usr_group=new UserAttributeTypeImpl();


        Usr_group.setValue(dbEntityRelation.getFullPath(conn, regUser.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_PARENT_USG));
        if(options!=null&&options.indexOf(GROUP)!=-1)
    		Usr_group.setPublicInd(true);
		else
			Usr_group.setPublicInd(false);
    	attr.setGroup(Usr_group);

		//grade
        UserAttributeTypeImpl Usr_grade=new UserAttributeTypeImpl();
        Usr_grade.setValue(dbEntityRelation.getFullPath(conn, regUser.usr_ent_id, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR));
        if(options!=null&&options.indexOf(GRADE)!=-1)
        	Usr_grade.setPublicInd(true);
		else
			Usr_grade.setPublicInd(false);

        attr.setGrade(Usr_grade);




        //extension4

        UserExtAttributeTypeImpl extension41=new  UserExtAttributeTypeImpl();
        extension41.setValue(regUser.urx_extra_41!=null?regUser.urx_extra_41:"");
        if(options!=null&&options.indexOf(EXTENTSION41)!=-1)
        	extension41.setPublicInd(true);
		else
			extension41.setPublicInd(false);

        attr.setExtension41(extension41);
      //extension5

        UserExtAttributeTypeImpl extension42=new  UserExtAttributeTypeImpl();
        extension42.setValue(regUser.urx_extra_42!=null?regUser.urx_extra_42:"");
        if(options!=null&&options.indexOf(EXTENTSION42)!=-1)
        	extension42.setPublicInd(true);
		else
			extension42.setPublicInd(false);


        attr.setExtension42(extension42);



        UserExtAttributeTypeImpl extension43=new  UserExtAttributeTypeImpl();


        if(regUser.urx_extra_43==null||regUser.urx_extra_43.equalsIgnoreCase(""))
        {
        	UserManagement usr=(UserManagement)wizbini.cfgOrgUserManagement.get(root_id);
        	String defaultUserPhoto=wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getName()+"/"+usr.getUserProfile().getProfileAttributes().getExtension43().getValue();
        	extension43.setValue(defaultUserPhoto);
        }
        else
        {
        	String photopath=wizbini.cfgSysSetupadv.getFileUpload().getUsrDir().getName()+"/"+regUser.usr_ent_id+"/"+regUser.urx_extra_43;
        	extension43.setValue(photopath);
        }

        if(options!=null&&options.indexOf(EXTENTSION43)!=-1)
        	extension43.setPublicInd(true);
		else
			extension43.setPublicInd(false);

        attr.setExtension43(extension43);


        UserExtAttributeTypeImpl extension44=new  UserExtAttributeTypeImpl();
        extension44.setValue(regUser.urx_extra_44!=null?regUser.urx_extra_44:"");

        if(options!=null&&options.indexOf(EXTENTSION44)!=-1)
        	extension44.setPublicInd(true);
		else
			extension44.setPublicInd(false);
       attr.setExtension44(extension44);

        UserExtAttributeTypeImpl extension45=new  UserExtAttributeTypeImpl();
        extension45.setValue(regUser.urx_extra_45!=null?regUser.urx_extra_45:"");
        if(options!=null&&options.indexOf(EXTENTSION45)!=-1)
        	extension45.setPublicInd(true);
		else
			extension45.setPublicInd(false);

        attr.setExtension45(extension45);


        UserAttributeTypeImpl nickname=new  UserAttributeTypeImpl();
        nickname.setValue(regUser.nickname!=null?regUser.nickname:"");

        if(options!=null&&options.indexOf(NICKNAME)!=-1)
        	nickname.setPublicInd(true);
		else
			nickname.setPublicInd(false);
        attr.setNickname(nickname);



        //用户扩展区域
        this.user_detail.put(UPD_DATE,regUser.usr_upd_date!=null?  JsonHelper.toJsonDateFormat(regUser.usr_upd_date):"");

        defJsonConfig.setRootClass(UserAttributeTypeImpl.class);

        defJsonConfig.setJsonPropertyFilter(new PropertyFilter() {
          public boolean apply(Object source, String name, Object value) {
            boolean resultSymbol = false;
            boolean isPrimaryInterface = name.startsWith("primaryInterface");
            boolean isProfileAttr = source.getClass().toString().endsWith("ProfileAttributesTypeImpl");
            boolean tmp = source.getClass().toString().startsWith("class com.cw.wizbank.config.organization.usermanagement.impl.User");

            if(isPrimaryInterface) {
            	resultSymbol = true;
            } else if(isProfileAttr) {
              resultSymbol = false;
            } else {
              boolean isAttrValue = "value".equalsIgnoreCase(name);
              boolean isAttrPublicInd = "publicind".equalsIgnoreCase(name);
              if(isAttrValue || isAttrPublicInd){
                resultSymbol = false;
              } else if (tmp) {
            	  resultSymbol = true;
              }
            }
            if (value == null) {
            	resultSymbol = true;
            }
            return resultSymbol;
          }
        });

        UserExtAttributeTypeImpl extension_30 = new UserExtAttributeTypeImpl();
        String user_in_tcr_id = (String) sqlMapClient.getsingleColumn(conn, "select usr_choice_tcr_id from reguser where usr_ent_id = ?",
				new Object[]{new Long(usr_id)}, String.class);
		if(user_in_tcr_id == null || user_in_tcr_id.equals("0")){
			user_in_tcr_id = String.valueOf(tcrLogic.getUsrInTcrInfor(conn, usr_id).get("tcr_id"));
		}
        extension_30.setValue(user_in_tcr_id);
        attr.setExtension30(extension_30);

        UserExtAttributeTypeImpl extension_20 = new UserExtAttributeTypeImpl();
        extension_20.setValue(LangLabel.getValue(prof.cur_lan, "lab_main_tcr_desc_content"));
        attr.setExtension20(extension_20);

        return attr;


		/*        JSONObject jsObj = (JSONObject) JSONObject.fromObject(attr, defJsonConfig);

		 return  JSONObject.toBean(jsObj);*/

	}

	public void getHome(Connection con, loginProfile prof, WizbiniLoader wizbini, UserModuleParam modParam, HashMap resultMap) throws SQLException, qdbException, cwException {
		getUserInfo(con, prof, wizbini, resultMap);
		getSysWarn(con, resultMap);
		getCosSelectionCenter(con, prof, resultMap);
		getStudyCenter(con, prof, wizbini, resultMap);
		getCommentCourseChart(con, prof, resultMap, wizbini);
		getExamCenter(con, prof, wizbini, resultMap);
		getIntegratedCenter(con, prof, wizbini, resultMap);
		getAnnouncement(con, prof, modParam, resultMap,  wizbini);
		getPendingWaitingCos(con, prof, wizbini, resultMap);
		getEvaluationSurvey(con, prof, resultMap,  wizbini);
		getUnsolvedQueList(con, prof, resultMap);
		getMySkillEvaluation(con, prof, resultMap);
		getSupervisePendApp(con, prof, resultMap);
	
		getStudyMaterial(con, prof, wizbini, resultMap);
	
		getSitePoster(con, prof, wizbini, resultMap);
		getFriendshipLink(con, resultMap,5);
		getCreditRank(con, resultMap);
	}

	/**
	 * 首页的加载数据(lrn_home.jsp)
	 */
	public void getHomeNew(Connection con, WizbiniLoader wizbini, loginProfile prof, HashMap resultMap, HttpSession sess, String isPreview) throws SQLException, qdbException, cwException {
		// 系统警告
		getSysWarn(con, resultMap);

		// 调查问卷
		getEvaluationSurvey(con, prof, resultMap,  wizbini);

		// 获取用户模版
		// getModuleCode(con, sess, prof, resultMap, isPreview);

		// 获取 等待审批 的数据
		getSupervisePendApp(con, prof, resultMap);

		// 选课中心
		// getCosSelectionCenter(con, prof, resultMap);

		// 改为jsp页面加载，此处不加载
		// getStudyCenter(con, prof, wizbini, resultMap);
		// getExamCenter(con, prof, wizbini, resultMap);
		// getIntegratedCenter(con, prof, wizbini, resultMap);

		// 积分排行榜
		getCreditRank(con, resultMap);
		// 宣传栏
		getSitePoster(con, prof, wizbini, resultMap);
		// 友情链接
		getFriendshipLink(con, resultMap,5);

		// 我的学习小组
		// getMyStudyGroup(con, prof, resultMap);

		// 课程排行榜
		// 热门课程
		getHotCourseChart(con, prof, resultMap, wizbini);
		// 最佳课程
		getCommentCourseChart(con, prof, resultMap, wizbini);
		// 最新培训
		getNewestCourseChart(con, prof, resultMap, wizbini);

		// 在线问答
		// 待解决
		getUnsolvedQueList(con, prof, resultMap);
		// 已解决
		getSolvedQueList(con, prof, resultMap);
		// 精选
		getPopularQueList(con, prof, resultMap);
		// FAQ
		getFaqList(con, prof, resultMap);
	}

	public void getHomeGadget(Connection con, loginProfile prof, String tabType, HashMap resultMap, WizbiniLoader wizbini) throws SQLException, qdbException, cwException {
		if(TAB_POP_ITEM.equalsIgnoreCase(tabType)) {
			getHotCourseChart(con, prof, resultMap, wizbini);
		} else if(TAB_ESTIMATED_ITEM.equalsIgnoreCase(tabType)) {
			getCommentCourseChart(con, prof, resultMap, wizbini);
		} else if(TAB_NEWEST_ITEM.equalsIgnoreCase(tabType)) {
			getNewestCourseChart(con, prof, resultMap,wizbini);
		} else if(TAB_QUE_UNANS_LST.equalsIgnoreCase(tabType)) {
			getUnsolvedQueList(con, prof, resultMap);
		} else if(TAB_QUE_ANSED_LST.equalsIgnoreCase(tabType)) {
			getSolvedQueList(con, prof, resultMap);
		} else if(TAB_QUE_POPULAR_LST.equalsIgnoreCase(tabType)) {
			getPopularQueList(con, prof, resultMap);
		} else if(TAB_QUE_FAQ_LST.equalsIgnoreCase(tabType)) {
			getFaqList(con, prof, resultMap);
		} else if(TAB_MY_SGP.equalsIgnoreCase(tabType)) {
			getMyStudyGroup(con, prof, resultMap);
		}
	}

	/*
	 * personal information
	 */
	private void getUserInfo(Connection con, loginProfile prof,  WizbiniLoader wizbini, HashMap resultMap) throws qdbException, SQLException {
		HashMap userInfoMap = new HashMap();
		userInfoMap.put("usr_display_bil", prof.usr_display_bil);
		dbRegUser regUser = new dbRegUser();
		regUser.usr_ent_id = prof.usr_ent_id;
		regUser.get(con);
		String userPhotoPath = wizbini.getUserPhotoPath(prof, prof.usr_ent_id, regUser.urx_extra_43);
		userInfoMap.put("usr_avatar", userPhotoPath);
		if(!prof.first_login){
			userInfoMap.put("last_login_timestamp", prof.usr_last_login_date);
			userInfoMap.put("last_login_status", new Boolean(prof.usr_last_login_success));
		}
		userInfoMap.put("my_credit", new Float(ViewUserCreditsUserDAO.getTotalByEntId(con, prof.usr_ent_id)));

		resultMap.put(JSON_USER_INFO, userInfoMap);
	}

	/**
	 *
	 * @param con
	 * @param resultMap
	 * @throws SQLException
	 */
	private void getSysWarn(Connection con,HashMap resultMap) throws SQLException{
		   Hashtable curSysSet = SystemSetting.getCurSystemSetting(con);
	        long blockThreshold = 0;
			long warnThreshold = 0;
			if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString().length() > 0){
				blockThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_BLOCK).toString());
			}
			if(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString().length() > 0){
				warnThreshold = Long.parseLong(curSysSet.get(SystemSetting.SYS_CFG_TYPE_WARN).toString());
			}
	        long loginUserCount = CurrentActiveUser.getcurActiveUserCount(con);
	        boolean warn = false;
	        if((blockThreshold > 0 && loginUserCount >= blockThreshold)
	        		|| (warnThreshold > 0 && loginUserCount >= warnThreshold)) {
	        	warn = true;
	        }
	        resultMap.put("sys_warning", new Boolean(warn));
	}

	/*
	 * course selections
	 */
	private void getCosSelectionCenter(Connection con, loginProfile prof, HashMap resultMap) throws SQLException {
		//choose classes center
		HashMap courseCenterMap = new HashMap();
		Course course = new Course();
		CourseModuleParam param = new CourseModuleParam();
		Vector myTrainingCenterVec = course.getMyTrainingCenter(con, prof.usr_ent_id, param);
		courseCenterMap.put(JSON_TCR_LST, myTrainingCenterVec);

		long nearTcrId = course.getMyNearTrainingCenter(con, prof.usr_ent_id, param);
		Vector tndVec = null;
		if(nearTcrId > 0) {
			tndVec = course.getCatalogForHome(con, prof.usr_ent_id, nearTcrId, 2);
		}
		courseCenterMap.put(JSON_TND_LST, tndVec);

		// get competence list by post of current user
		Vector mySkillBaseVector = course.getMySkillBases(con, prof.usr_ent_id);	// ???????λ???????
		courseCenterMap.put(JSON_COMPETENCE_LIST, mySkillBaseVector);
		resultMap.put(JSON_LEARNING_CATALOG, courseCenterMap);
	}

	/*
	 * study center
	 */
	private void getStudyCenter(Connection con, loginProfile prof, WizbiniLoader wizbini, HashMap resultMap) throws SQLException {
		Study study = new Study();
		StudyModuleParam param = new StudyModuleParam();
		param.setType(aeItemDummyType.ITEM_DUMMY_TYPE_COS);
		param.setStart(0);
		param.setLimit(HOME_GETDAT_STUDY_CENTER_RECORD_NUM);
		String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
		Vector studyingCourseVec = study.getCurrentCourses(con, prof.usr_ent_id, prof.cur_lan, param, itmDir);
		HashMap courseMap = new HashMap();
		courseMap.put(JSON_ITM_LST, studyingCourseVec);
		resultMap.put(JSON_LEARNING_CENTER, courseMap);
	}

	/*
	 * chart of host course
	 */
	private void getHotCourseChart(Connection con, loginProfile prof, HashMap resultMap, WizbiniLoader wizbini) throws SQLException, qdbException {
		Course course = new Course();
		CourseModuleParam param = new CourseModuleParam();
		param.setStart(0);
		param.setLimit(HOME_GETDAT_TRAIN_CHART_RECORD_NUM);
		Vector courseVec = course.getHotCourse(con, param, prof, wizbini);
		HashMap courseMap = new HashMap();
		courseMap.put(JSON_ITM_LST, courseVec);
		resultMap.put(JSON_POP_ITEM, courseMap);
	}

	/*
	 * chart of comment course
	 */
	private void getCommentCourseChart(Connection con, loginProfile prof, HashMap resultMap, WizbiniLoader wizbini) throws SQLException, qdbException {
		Course course = new Course();
		CourseModuleParam param = new CourseModuleParam();
		param.setStart(0);
		param.setLimit(HOME_GETDAT_TRAIN_CHART_RECORD_NUM);
		Vector courseVec = course.getSorCourses(con, param, prof,wizbini);
		HashMap courseMap = new HashMap();
		courseMap.put(JSON_ITM_LST, courseVec);
		resultMap.put(JSON_ESTIMATED_ITEM, courseMap);
	}

	/*
	 * chart of newest course
	 */
	private void getNewestCourseChart(Connection con, loginProfile prof, HashMap resultMap, WizbiniLoader wizbini) throws SQLException, qdbException {
		Course course = new Course();
		CourseModuleParam param = new CourseModuleParam();
		param.setStart(0);
		param.setLimit(HOME_GETDAT_TRAIN_CHART_RECORD_NUM);
		Vector courseVec = course.getLastedCourses(con, param, prof, wizbini);
		HashMap courseMap = new HashMap();
		courseMap.put(JSON_ITM_LST, courseVec);
		resultMap.put(JSON_NEWEST_ITEM, courseMap);
	}

	/*
	 * unsolved question list
	 */
	private void getUnsolvedQueList(Connection con, loginProfile prof, HashMap resultMap) throws SQLException, qdbException, cwException {
		KnowModuleParam param = new KnowModuleParam();
		param.setStart(0);
		param.setLimit(HOME_GETDAT_KNOW_QUE_RECORD_NUM);
		param.setSort("que_create_timestamp");
		param.setDir("desc");
		Know know = new Know(param, prof);
		know.getUnsolvedQue(con, resultMap);
	}

	/*
	 * solved question list
	 */
	private void getSolvedQueList(Connection con, loginProfile prof, HashMap resultMap) throws SQLException, qdbException, cwException {
		KnowModuleParam param = new KnowModuleParam();
		param.setStart(0);
		param.setLimit(HOME_GETDAT_KNOW_QUE_RECORD_NUM);
		param.setSort("que_create_timestamp");
		param.setDir("desc");
		Know know = new Know(param, prof);
		know.getSolvedQue(con, resultMap);
	}

	/*
	 * FAQ question list
	 */
	private void getFaqList(Connection con, loginProfile prof, HashMap resultMap) throws SQLException, qdbException, cwException {
		KnowModuleParam param = new KnowModuleParam();
		param.setStart(0);
		param.setLimit(HOME_GETDAT_KNOW_QUE_RECORD_NUM);
		param.setSort("que_create_timestamp");
		param.setDir("desc");
		Know know = new Know(param, prof);
		know.getFaq(con, resultMap);
	}

	/*
	 * popular question list
	 */
	private void getPopularQueList(Connection con, loginProfile prof, HashMap resultMap) throws SQLException, qdbException, cwException {
		KnowModuleParam param = new KnowModuleParam();
		param.setStart(0);
		param.setLimit(HOME_GETDAT_KNOW_QUE_RECORD_NUM);
		param.setSort("ans_vote_for");
		param.setDir("desc");
		Know know = new Know(param, prof);
		know.getPopulardQue(con, resultMap);
	}

	/*
	 * exam center
	 */
	private void getExamCenter(Connection con, loginProfile prof, WizbiniLoader wizbini, HashMap resultMap) throws SQLException {
		StudyModuleParam param = new StudyModuleParam();
		param.setType(aeItemDummyType.ITEM_DUMMY_TYPE_EXAM);
		param.setStart(0);
		param.setLimit(HOME_GETDAT_EXAM_CENTER_RECORD_NUM);
		Study study = new Study();
		String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
		Vector examVec = study.getCurrentCourses(con, prof.usr_ent_id, prof.cur_lan, param, itmDir);
		HashMap examMap = new HashMap();
		examMap.put(JSON_ITM_LST, examVec);
		resultMap.put(JSON_EXAM_CENTER, examMap);
	}

	/*
	 * integrated center
	 */
	private void getIntegratedCenter(Connection con, loginProfile prof, WizbiniLoader wizbini, HashMap resultMap) throws SQLException {
		StudyModuleParam param = new StudyModuleParam();
		param.setType(aeItemDummyType.ITEM_DUMMY_TYPE_INTEGRATED);
		param.setStart(0);
		param.setLimit(HOME_GETDAT_EXAM_CENTER_RECORD_NUM);
		Study study = new Study();
		String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
		Vector vec = study.getCurrentCourses(con, prof.usr_ent_id, prof.cur_lan, param, itmDir);
		HashMap map = new HashMap();
		map.put(JSON_ITM_LST, vec);
		resultMap.put(JSON_INTEGRATED_LEARNING, map);
	}

	/*
	 * announcement
	 */
	private void getAnnouncement(Connection con, loginProfile prof, UserModuleParam modParam, HashMap resultMap, WizbiniLoader wizbini) throws SQLException, qdbException {
		AnnModuleParam param = new AnnModuleParam();
		param.setStart(0);
		param.setLimit(HOME_GETDAT_ANN_RECORD_NUM);
		param.setSort("msg_begin_date");
		param.setDir("desc");
		param.setCur_time(modParam.getCur_time());
		Ann ann = new Ann();
		Vector annVec = ann.getAnnByTc_ID(con, prof, param, param.getTcr_id(), true,  wizbini);
		HashMap annMap = new HashMap();
		annMap.put(JSON_ANN_LST, annVec);
		resultMap.put(JSON_ANNOUNCEMENTS, annMap);
	}

	/*
	 * pending or waiting course list
	 */
	private void getPendingWaitingCos(Connection con, loginProfile prof, WizbiniLoader wizbini, HashMap resultMap) throws SQLException {
		StudyModuleParam param = new StudyModuleParam();
		param.setStart(0);
		param.setLimit(HOME_GETDAT_PEND_WATI_COS_RECORD_NUM);
		param.setSort("appn.app_upd_timestamp");
		param.setDir("desc");
		Study study = new Study();
		String itmDir = wizbini.cfgSysSetupadv.getFileUpload().getItmDir().getName();
		Vector pendWaitCosVec = study.getMyPendingCourses(con, prof.usr_ent_id, prof.cur_lan, param, itmDir);
		HashMap pendWaitCosMap = new HashMap();
		pendWaitCosMap.put(JSON_ITM_LST, pendWaitCosVec);
		resultMap.put(JSON_PENDING_WAITING_ITEM, pendWaitCosMap);
	}

	/*
	 * evaluation survey
	 */
	public void getEvaluationSurvey(Connection con, loginProfile prof, HashMap resultMap, WizbiniLoader wizbini) throws SQLException {
		Evaluation evaluation = new Evaluation();
		Vector evaluationModVec = evaluation.getModJsonVec(con, prof.root_ent_id, true, prof.usr_id, prof.usr_ent_id, 0, 0, 0,  wizbini);
		HashMap evaluationModMap = new HashMap();
		evaluationModMap.put(JSON_EVAL_LST, evaluationModVec);
		resultMap.put(JSON_PUBLIC_EVAL, evaluationModMap);
	}

	public Vector getPublicEvaluationSurvey(Connection con, loginProfile prof, WizbiniLoader wizbini) throws SQLException {
		Evaluation evaluation = new Evaluation();
		return evaluation.getModJsonVec(con, prof.root_ent_id, true, prof.usr_id, prof.usr_ent_id, 0, 0, 0,  wizbini);
	}

	/*
	 * study group of my
	 */
	private void getMyStudyGroup(Connection con, loginProfile prof, HashMap resultMap) throws SQLException {
		StudyGroupDAO studyGroupDao = new StudyGroupDAO();
		Vector studyGroupVec = studyGroupDao.getMyStudyGroup(con, prof.usr_ent_id, 0, HOME_GETDAT_MY_STUDY_GROUP_RECORD_NUM);
		HashMap studyGroupMap = new HashMap();
		studyGroupMap.put(JSON_SGP_LST, studyGroupVec);
		resultMap.put(JSON_MY_SGP, studyGroupMap);
	}

	/*
	 * skill evaluation
	 */
	private void getMySkillEvaluation(Connection con, loginProfile prof, HashMap resultMap) throws SQLException {
		Vector skillEvaluationVec = ViewCmAssessment.getMySkillEvaluation(con, prof.usr_ent_id, false);
		HashMap skillEvaluationMap = new HashMap();
		skillEvaluationMap.put(JSON_ASSESSMENT_LST, skillEvaluationVec);
		resultMap.put(JSON_SKILL_EVAL, skillEvaluationMap);
	}

	private void getSupervisePendApp(Connection con, loginProfile prof, HashMap resultMap) throws SQLException {
		Supervise sup = new Supervise();
		Vector appVc = new Vector();
		SuperviseModuleParam param =new SuperviseModuleParam();
		param.setStart(0);
		param.setLimit(HOME_SUPERVISE_APP_PEND_NUM);
		sup.getApp_pend(con, prof, param, appVc);
		resultMap.put(JSON_SUPERVISE_APP_PEND, appVc);
	}

	private void getStudyMaterial(Connection con, loginProfile prof, WizbiniLoader wizbini, HashMap resultMap) throws SQLException {
		Course cos = new Course();
		Vector tcrVec = cos.getTrainingCenterByTargetUser(con, prof.usr_ent_id);
		long tcrId = 0;
		if(tcrVec.size() > 0) {
			TCBean tcBean = (TCBean)tcrVec.get(0);
			tcrId = tcBean.getTcr_id();
		}
		StudyMaterialModuleParam modParam = new StudyMaterialModuleParam();
		modParam.setTcr_id(tcrId);
		modParam.setSort("obj_desc");
		StudyMaterial material = new StudyMaterial(wizbini, prof, con, modParam);
		Vector catVec = material.getCatListOfHome() == null ? new Vector() : material.getCatListOfHome();
		resultMap.put(JSON_MATERIAL_CATALOG, catVec);
	}

	private void getSitePoster(Connection con, loginProfile prof, WizbiniLoader wizbini, HashMap resultMap) throws SQLException {
		dbPoster poster = new dbPoster();
		poster.sp_ste_id = prof.root_ent_id;

		Vector<SitePoster> vector = poster.getPoster(con);
		for (SitePoster p : vector) {
			String saveDirPath = cwUtils.getFileURL(qdbAction.static_env.INI_POSTER_DIR_URL) + "/" + prof.root_id + "/" + p.getSp_media_file();
			p.setSp_media_file(saveDirPath);
		}
		resultMap.put(JSON_SITE_POSTER, vector);
	}

	private void getFriendshipLink(Connection con, HashMap resultMap,int max_size) throws SQLException {
		resultMap.put(JSON_FRIENDSHIP_LINK, FriendshipLinkDAO.getLinkList(con,max_size));
	}

	private void getCreditRank(Connection con, HashMap resultMap) throws SQLException {
		Map creditRankMap = qdbAction.creditRankMap;
		resultMap.put(JSON_CREDIT_RANK, creditRankMap.get(ViewCreditsDAO.MAP_KEY_LIST));
	}

	public void getDefinedProject(Connection con, HashMap resultJson, Long tcr_id){
		Map defiProjsonData = new HashMap();
		List definedProjectLst = sqlMapClient.getObjectList(con, "select dpt_id, dpt_code, dpt_title from DefinedProject where dpt_status = ? and dpt_tcr_id = ? order by dpt_id asc", new Object[]{"ON" ,tcr_id});
		List dpt_id_lst = new ArrayList();
		for (Iterator iterator = definedProjectLst.iterator(); iterator.hasNext();) {
			Map definedProjectMap = (Map) iterator.next();
			dpt_id_lst.add(Long.valueOf(String.valueOf(definedProjectMap.get("dpt_id"))));
		}
		List projectLinkLst = sqlMapClient.getObjectList(con, "select pjl_dpt_id, pjl_code, pjl_title, pjl_url from projectLink where pjl_status = 'ON' and pjl_dpt_id in (#) order by pjl_dpt_id asc", new Object[]{dpt_id_lst});

		int n = 0;
		for (Iterator iterator = definedProjectLst.iterator(); iterator.hasNext();) {
			Map definedProjectMap = (Map) iterator.next();
			Long dpt_id = Long.valueOf(String.valueOf(definedProjectMap.get("dpt_id")));
			List pro_link_lst = new ArrayList();
			definedProjectMap.put(DEFI_PROJ_LINK, pro_link_lst);
			while(n < projectLinkLst.size()){
				Map projectLinkMap = (Map)projectLinkLst.get(n);
				Long pjl_dpt_id = Long.valueOf(String.valueOf(projectLinkMap.get("pjl_dpt_id")));
				if(dpt_id.longValue() == pjl_dpt_id.longValue()){
					pro_link_lst.add(projectLinkMap);
				}else{
					defiProjsonData.put(DEFINED_PROJECT + definedProjectMap.get("dpt_code"), definedProjectMap);
					break;
				}
				if((n + 1) == projectLinkLst.size()){
					defiProjsonData.put(DEFINED_PROJECT + definedProjectMap.get("dpt_code"), definedProjectMap);
				}
				++n;
			}
		}
		resultJson.put(DEFINED_PROJECT, defiProjsonData);
	}

	public void getCompanyQQInfor(Connection con, HashMap resultJson, Long tcr_id){
		Map companyQQInfors = new HashMap();
		companyQQInfors.put(COMPANY_QQ_LIST, sqlMapClient.getObjectList(con, "select cpq_title, cpq_number, cpq_desc from companyQQ where cpq_status = 'ON' order by cpq_code asc", new Object[]{}));
		resultJson.put(COMPANY_QQ_DATA, companyQQInfors);
	}

	private final String getmoduleCodeSql = new StringBuffer()
	.append("select tm_code from tcrModule")
	.append(" inner join TcrTemplateModule on (ttm_tm_id = tm_id)")
	.append(" inner join TcrTemplate on (ttm_tt_id = tt_id)")
	.append(" where tt_tcr_id = ? and ttm_mod_status = ? and ttm_mod_x_value = ? order by ttm_mod_y_value asc").toString();
	public Long getModuleCode(Connection con, UserModuleParam modParam, HttpSession session, loginProfile prof, HashMap resultJson){
		if(modParam.getIsPreView().equals("true")){
			Map tempInfoMap = (Map) session.getAttribute(JSON_TEMPLATE);
			resultJson.put(JSON_TEMPLATE, tempInfoMap);
			//session.removeAttribute(JSON_TEMPLATE);
			return new Long(modParam.getTcr_id());
		}else{
			Long tcr_id = (Long) sqlMapClient.getsingleColumn(con, "select usr_choice_tcr_id from reguser,tctrainingcenter  tcr_id=usr_choice_tcr_id and  where usr_ent_id = ?", new Object[]{new Long(prof.usr_ent_id)}, Long.class);
			if(tcr_id == null || tcr_id.longValue() <=0){
				TcrLogic tcrLogic = TcrLogic.getInstance();
				tcr_id = Long.valueOf(String.valueOf(tcrLogic.getUsrInTcrInfor(con, prof.usr_ent_id).get("tcr_id")));
			}
			Map tempInfoMap = new HashMap();
			tempInfoMap.put(LEFT, sqlMapClient.getsingleColumnList(con, getmoduleCodeSql, new Object[]{tcr_id, new Integer(1), new Integer(0)}, String.class));
			tempInfoMap.put(CENTRE, sqlMapClient.getsingleColumnList(con, getmoduleCodeSql, new Object[]{tcr_id, new Integer(1), new Integer(1)}, String.class));
			tempInfoMap.put(RIGHT, sqlMapClient.getsingleColumnList(con, getmoduleCodeSql, new Object[]{tcr_id, new Integer(1), new Integer(2)}, String.class));
			tempInfoMap.put("dis_fun_ind", sqlMapClient.getsingleColumn(con,
					"select tt_dis_fun_navigation_ind from TcrTemplate where tt_tcr_id = ?", new Object[]{tcr_id}, Integer.class));
			resultJson.put(JSON_TEMPLATE, tempInfoMap);
			return tcr_id;
		}

	}

	/**
	 * 获取用户首页显示模版
	 * @param con
	 * @param session 如果是预览的，则取出模版参数（位置和顺序）
	 * @param prof
	 * @param resultMap 返回结果，包括4个信息（1、左边模块 2、中间模块 3、右边模块 4、是否显示功能模块）
	 * @param isPreview 是否预览，在培训管理员中的培训中心管理中可以设置培训中心的模版；
	 * 点击 预览 时取session中参数，而不从数据库中查表获取模版位置和顺序
	 */
	public void getModuleCode(Connection con, HttpSession session, loginProfile prof, HashMap resultMap, String isPreview) throws SQLException {
		if("true".equals(isPreview)){
			Map tempInfoMap = (Map) session.getAttribute(JSON_TEMPLATE);
			resultMap.put(JSON_TEMPLATE, tempInfoMap);
		}else{
			String sSQL = new StringBuffer().append("select tm_code,tt_dis_fun_navigation_ind,ttm_mod_x_value ")
					.append(" from tcrModule,TcrTemplateModule,TcrTemplate ")
					.append(" where ttm_tm_id=tm_id and ttm_tt_id=tt_id and ttm_mod_status='1' ")
					.append(" and tt_tcr_id ='" + prof.my_top_tc_id + "' ")
					.append(" order by ttm_mod_x_value,ttm_mod_y_value ").toString();

			PreparedStatement stmt = null;
			stmt=con.prepareStatement(sSQL);
			ResultSet rs = stmt.executeQuery();
			List all = new ArrayList();
			List alm = new ArrayList();
			List alr = new ArrayList();
			String sTem = "";
			while(rs.next()){
				if("0".equals(rs.getString("ttm_mod_x_value"))){ // left side
					all.add(rs.getString("tm_code"));
				} else if("1".equals(rs.getString("ttm_mod_x_value"))){ // middle
					alm.add(rs.getString("tm_code"));
				} else { // right side
					alr.add(rs.getString("tm_code"));
				}
				sTem = rs.getString("tt_dis_fun_navigation_ind"); // 是否显示功能导航模块，如果为 1 则显示。
			}

			Map tempInfoMap = new HashMap();
			tempInfoMap.put(LEFT, all);
			tempInfoMap.put(CENTRE, alm);
			tempInfoMap.put(RIGHT, alr);
			tempInfoMap.put("dis_fun_ind", sTem);
			resultMap.put(JSON_TEMPLATE, tempInfoMap);
		}

	}

	public void setUserTcrId(Connection con, HashMap resultJson, loginProfile prof){
		Map tcrInforMap = new HashMap();
		tcrInforMap.put("name", JSON_MY_PROFILE);
		List tcr_infor_lst = new ArrayList();

		Map choice_tcr_map = new HashMap();
		choice_tcr_map.put("searchable", "div");
		choice_tcr_map.put("group", JSON_MY_PROFILE);
		choice_tcr_map.put("publicity", "0");
		choice_tcr_map.put("readonly", "false");
		choice_tcr_map.put("fieldname", "usr_template_tcr_id");
		choice_tcr_map.put("registration", "true");
		choice_tcr_map.put("type", "comboBox");
		choice_tcr_map.put("label", LangLabel.getValue(prof.cur_lan, "lab_main_tcr"));
		choice_tcr_map.put("name", JSON_MAIN_TCR);
		List option_tcr_lst = new ArrayList();
		choice_tcr_map.put("option_lst", option_tcr_lst);
		tcr_infor_lst.add(choice_tcr_map);

		Map desc_map = new HashMap();
		desc_map.put("searchable", "all");
		desc_map.put("group", JSON_MY_PROFILE);
		desc_map.put("publicity", "0");
		desc_map.put("readonly", "true");
		desc_map.put("fieldname", "usr_desc_infor");
		desc_map.put("registration", "true");
		desc_map.put("label", LangLabel.getValue(prof.cur_lan, "wb_imp_tem_explanation"));
		desc_map.put("name", jSON_DESC_MAIN_TCR);
		tcr_infor_lst.add(desc_map);

		tcrInforMap.put("attr_lst", tcr_infor_lst);
		List user_in_tcr_is_lst = tcrLogic.getUsrInTcrInforList(con, prof.usr_ent_id);
		for (Iterator iterator = user_in_tcr_is_lst.iterator(); iterator.hasNext();) {
			Map tcrMap = (Map) iterator.next();
			Long tcr_id =  Long.valueOf(String.valueOf(tcrMap.get("tcr_id")));
			String tcr_title = String.valueOf(tcrMap.get("tcr_title"));
			Map optionMap = new HashMap();
			optionMap.put("value", tcr_id);
			optionMap.put("label", tcr_title);
			option_tcr_lst.add(optionMap);
		}
		resultJson.put(JSON_USER_CHOICE_TCR_INFOR, tcrInforMap);
	}

	/*
     * get the roles that the user has for json
     */
    public List getUserRolesJsonCanLogin(Connection con, long usr_ent_id, Timestamp cur_time) throws SQLException {
        AccessControlWZB acl = new AccessControlWZB();
        List roleArray = DbAcRole.getRolesCanLogin(con, usr_ent_id, cur_time);
        Vector rolVec = null;
        if(roleArray != null && roleArray.size() > 0) {
            rolVec = new Vector();
            for(int roleIndex = 0; roleIndex < roleArray.size(); roleIndex++) {
                HashMap roleMap = new HashMap();
                DbAcRole role = (DbAcRole)roleArray.get(roleIndex);
                roleMap.put("id", role.rol_ext_id);
                if (DbAcRole.ROL_TYPE_NORMAL.equalsIgnoreCase(role.getRol_type())) {
                    roleMap.put("title", role.getRolTitle());
                }
                rolVec.addElement(roleMap);
            }
        }
        return rolVec;
    }
}
