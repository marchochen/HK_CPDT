package com.cw.wizbank.JsonMod.credit;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.cw.wizbank.Dispatcher;
import com.cw.wizbank.JsonMod.know.bean.CreditsTypeBean;
import com.cw.wizbank.ae.aeItem;
import com.cw.wizbank.credit.dao.CreditsTypeDAO;
import com.cw.wizbank.credit.dao.UserCreditsDAO;
import com.cw.wizbank.credit.dao.UserCreditsDetailDAO;
import com.cw.wizbank.credit.dao.UserCreditsDetailLogDAO;
import com.cw.wizbank.credit.db.CreditsTypeDB;
import com.cw.wizbank.credit.db.UserCreditsDB;
import com.cw.wizbank.credit.db.UserCreditsDetailDB;
import com.cw.wizbank.credit.db.UserCreditsDetailLogDB;
import com.cw.wizbank.credit.view.ViewCreditsDAO;
import com.cw.wizbank.db.view.ViewEntityRelation;
import com.cw.wizbank.db.view.ViewTrainingCenter;
import com.cw.wizbank.know.dao.KnowQuestionDAO;
import com.cw.wizbank.know.db.KnowQuestionDB;
import com.cw.wizbank.qdb.dbAiccPath;
import com.cw.wizbank.qdb.dbCourseEvaluation;
import com.cw.wizbank.qdb.dbEntityRelation;
import com.cw.wizbank.qdb.dbModule;
import com.cw.wizbank.qdb.dbModuleEvaluation;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.dbUserGroup;
import com.cw.wizbank.qdb.dbUtils;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.report.LearnerRptExporter;
import com.cw.wizbank.upload.UploadUtils;
import com.cw.wizbank.util.EntityFullPath;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.MYSQLDbHelper;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwPagination;
import com.cw.wizbank.util.cwSQL;
import com.cw.wizbank.util.cwSysMessage;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

/**
 * @author DeanChen
 *
 */
public class Credit {

	/**
     * the type of user credit
     */
	
	// 创建群组
    public static final String CREATE_GROUP = "SYS_CREATE_GROUP";
	//参与群组
	public static final String SYS_JION_GROUP = "SYS_JION_GROUP";
	//被点赞
	public static final String SYS_GET_LIKE = "SYS_GET_LIKE";
	//参与课程评论
	public static final String SYS_COS_COMMENT = "SYS_COS_COMMENT";
	//点赞
	public static final String SYS_CLICK_LIKE = "SYS_CLICK_LIKE";
	
	
    // 新用户首次登录
    public static final String ZD_INIT = "ZD_INIT";

    // 提交回答
    public static final String ZD_COMMIT_ANS = "ZD_COMMIT_ANS";

    // 回答被采纳为最佳答案
    public static final String ZD_RIGHT_ANS = "ZD_RIGHT_ANS";

    // 选择放弃问题
    public static final String ZD_CANCEL_QUE = "ZD_CANCEL_QUE";

    // 采纳最佳答案
    public static final String ZD_CHOOSE_ANS = "ZD_CHOOSE_ANS";

    // 提出问题
    public static final String ZD_NEW_QUE = "ZD_NEW_QUE";

    // 用户非首次登录
    public static final String SYS_NORMAL_LOGIN = "SYS_NORMAL_LOGIN";

    // 更改个人资料
    public static final String SYS_UPD_MY_PROFILE = "SYS_UPD_MY_PROFILE";

    // 参加公共调查问卷
    public static final String SYS_SUBMIT_SVY = "SYS_SUBMIT_SVY";

    // 论坛发贴得分
    public static final String SYS_INS_TOPIC = "SYS_INS_TOPIC";

    // 论坛回贴得分
    public static final String SYS_INS_MSG = "SYS_INS_MSG";

    // 论坛共享资料得分
    public static final String SYS_MSG_UPLOAD_RES = "SYS_MSG_UPLOAD_RES";

    // 成功报读培训
    public static final String ITM_ENROLLED = "ITM_ENROLLED";

    // 成绩达到60分或以上
    public static final String ITM_SCORE_PAST_60 = "ITM_SCORE_PAST_60";

    // 成绩达到70分或以上
    public static final String ITM_SCORE_PAST_70 = "ITM_SCORE_PAST_70";

    // 成绩达到80分或以上
    public static final String ITM_SCORE_PAST_80 = "ITM_SCORE_PAST_80";

    // 成绩达到90分或以上
    public static final String ITM_SCORE_PAST_90 = "ITM_SCORE_PAST_90";

    // 测验已通过
    public static final String ITM_PAST_TEST = "ITM_PAST_TEST";

    // 作业已提交
    public static final String ITM_SUBMIT_ASS = "ITM_SUBMIT_ASS";

    // 调查问卷已提交
    public static final String ITM_SUBMIT_SVY = "ITM_SUBMIT_SVY";

    // 教材已浏览
    public static final String ITM_VIEW_RDG = "ITM_VIEW_RDG";

    // “课件”已浏览
    public static final String ITM_VIEW_COURSEWARE = "ITM_VIEW_COURSEWARE";

    // “课件”已完成
    public static final String ITM_PAST_COURSEWARE = "ITM_PAST_COURSEWARE";

    // 参考已浏览
    public static final String ITM_VIEW_REF = "ITM_VIEW_REF";

    // 视频点播已浏览
    public static final String ITM_VIEW_VOD = "ITM_VIEW_VOD";

    // 答疑栏已参与
    public static final String ITM_VIEW_FAQ = "ITM_VIEW_FAQ";

    // 培训论坛发贴得分
    public static final String ITM_INS_TOPIC = "ITM_INS_TOPIC";

    // 论坛回贴得分
    public static final String ITM_INS_MSG = "ITM_INS_MSG";

    // 论坛共享资料得分
    public static final String ITM_MSG_UPLOAD_RES = "ITM_MSG_UPLOAD_RES";

    // 导入课程积分
    public static final String ITM_IMPORT_CREDIT = "ITM_IMPORT_CREDIT";
    //问答，提问积分
    public static final String SYS_QUESTION_BOUNTY="SYS_QUESTION_BOUNTY";
    //问答，回答积分
    public static final String SYS_ANWSER_BOUNTY="SYS_ANWSER_BOUNTY";
    //分享知识分
    public static final String KB_SHARE_KNOWLEDGE="KB_SHARE_KNOWLEDGE";
    
    //API手工设置积分类型
    public static final String API_UPDATE_CREDITS = "API_UPDATE_CREDITS";
    
    //清空培训积分
    public static final String ITM_INTEGRAL_EMPTY = "ITM_INTEGRAL_EMPTY";
    
    /**
     * the source type of credits
     */
    public static final String CTY_KNOW = "ZD";

    public static final String CTY_COURSE = "COS";

    public static final String INTEGRAL_EMPTY = "INTEGRAL_EMPTY";

    //for report
    public static final int RPT_PAGE_COUNT = 20;
    public static final String ACTIVITY_SCORE = "A";
	public static final String TRAINNING_SCORE = "T";

	public static final String FTN_CREDIT_SETTING_MAIN="CREDIT_SETTING_MAIN";

	public static final String FTN_CREDIT_OTHER_MAIN="CREDIT_OTHER_MAIN";

	public static final String AddTYPE = "A";
	public static final String DELTYPE = "D";
	public static final String EMPTY_SCORE = "E";
	private class timeCntClass {
		int timeCnt = 1;
	}

    /**
     * to update the credits of user by event type.
     *
     * @param con
     * @param creditsType
     * @param sourceId
     *            : question id or item id
     * @param userEntId
     *            : the user id of credit
     * @param userId
     * @param manualScore 手工积分
     * @param app_id 与课程相关的学员app_id
     * @param cty_id 积分项的ID,如果是自动积分,这个参数值为0
     * @throws SQLException
     * @throws cwException
     * @throws cwSysMessage
     */
    public float updUserCredits(Connection con, String creditsType, long sourceId, int userEntId, String userId,
    		float manualScore, long app_id, float itm_diff_factor, long cty_id) throws SQLException, cwException {
        boolean isTcIndependent = qdbAction.wizbini.cfgSysSetupadv.isTcIndependent();//二级培训中心是否独立
    	long tcr_id=0;//设置培训ID值
    	float credits = 0;
    	//如果是清空积分的就获取顶层的培训中心id，因为默认的清空操作为顶层培训中心共享所有子培训中心公用
        if(Credit.INTEGRAL_EMPTY.equals(creditsType) || Credit.ITM_IMPORT_CREDIT.equals(creditsType)  ){//清空积分类型
        	tcr_id=ViewTrainingCenter.getRootTcId(con);
        }else{            
            tcr_id= ViewTrainingCenter.getTopTc( con, userEntId, isTcIndependent);//如果不是清空积分，则获取相关的培训中心
        }
        
     // Bug 17880 - 学员已经首次登陆但是没有记录到，故重新判断一次状态
        if(Credit.ZD_INIT.equalsIgnoreCase(creditsType)){
        	if(UserCreditsDetailLogDAO.hasUserCreditsDetailLog(con, userEntId,Credit.ZD_INIT, tcr_id)) {
        		creditsType = Credit.SYS_NORMAL_LOGIN;
        	}
        }
        
    	// get relation credits by type of credit
        CreditsTypeDB creditsTypeDb = new CreditsTypeDB();//积分类型类
        creditsTypeDb.setCty_code(creditsType);//设置积分类型
        creditsTypeDb.setCty_id((int)cty_id);//设置ID
        creditsTypeDb.setCty_tcr_id(tcr_id);//设置培训中心
        CreditsTypeDAO creditsTypeDao = new CreditsTypeDAO();//积分数据库层类
        if(cty_id > 0){//如果积分ID不为空，就获取积分类型
            creditsTypeDao.getById(con, creditsTypeDb);
        }else{
            creditsTypeDao.getByCode(con, creditsTypeDb);//如果积分ID为空给，则按照标题和培训中获取级分类型详细信息
            if(creditsTypeDb.getCty_id() < 1){//如果级分类性所属的培训中小于1，则获取跟培训中心。
                creditsTypeDb.setCty_tcr_id(ViewTrainingCenter.getRootTcId(con));
                creditsTypeDao.getByCode(con, creditsTypeDb);
            }
            if(creditsTypeDb.getCty_id() < 1){//如果积分类型I小于1，则抱异常
                Dispatcher.creditsLogger.info(" Failed to get a record of userCreditsDetail by code: " + creditsTypeDb.getCty_code());
                return credits;
            }
        }
        
        

        //如果积分类型存在下能做以下的操作
        if (creditsTypeDb.getCty_id() > 0) {//如果积分类型大于0
	        // get detail of user credit
	        UserCreditsDetailDB creditsDetailsDb = new UserCreditsDetailDB();//用户积分详细数据库类
	        //设定积分类型和被积分的用户
	        creditsDetailsDb.setUcd_cty_id(creditsTypeDb.getCty_id());//设置用户详细数据库类的积分类型ID
	        creditsDetailsDb.setUcd_ent_id(userEntId);//设置用户线I昂系数据库类的积分类型用户ID
	        if (CreditsTypeDAO.CTY_COURSE.equalsIgnoreCase(creditsTypeDb.getCty_relation_type())) {//如果积分类关系是COS
	        	creditsDetailsDb.setUcd_itm_id(sourceId);//设置用户详细数据库类的课程ID
	        	creditsDetailsDb.setUcd_app_id(app_id);//设置用户详细数据库类的报名ID
	        }
	        //判断积分的用户的积分记录是否存在
	        UserCreditsDetailDAO creditsDetailsDao = new UserCreditsDetailDAO();
	        boolean isExistCreditDetail = creditsDetailsDao.get(con, creditsDetailsDb);
	        
	        // the condition of add credit，判断是否更新积分
	        boolean isUpdateCredit = isUpdateCredits(con, creditsType, creditsDetailsDb, creditsTypeDb, (int)sourceId, userId);
	       //获取应该修改的积分
        	float a = getCredits(creditsTypeDb.isCty_default_credits_ind(), creditsTypeDb.isCty_deduction_ind(), 
					creditsTypeDb.getCty_default_credits(), manualScore, itm_diff_factor);
	        if (isUpdateCredit) {
	        	//所得积分
	        	
	        	//如果是清空积分，首先要做的就是把积分明细的所有分数加起来得到一个负分，然后再添加一条记录到积分明细表中，清空以后算总积分的时候就可以与该负分与之前的分数相抵
	        	if(Credit.INTEGRAL_EMPTY.equals(creditsType)){
	        		//获取需要清空的活动积分
	        		credits=getCredits(con,userEntId,"activity");
	        		
	        	}else{
	        		credits = getCredits(creditsTypeDb.isCty_default_credits_ind(), creditsTypeDb.isCty_deduction_ind(), 
							creditsTypeDb.getCty_default_credits(), manualScore, itm_diff_factor);
	        	}
	        	
	            // compute data for update database
	        	setCreditsDetail(con, creditsDetailsDb, userId, creditsTypeDb.getCty_period(), creditsTypeDb.getCty_hit(), credits, creditsTypeDb.getCty_code());
	        	
	            // update user credits
	            if (isExistCreditDetail) {
	            	//如果存在积分记录，并且是清空用户积分
	            	if(Credit.INTEGRAL_EMPTY.equals(creditsType)){
	            		 //插入一条积分明细信息，当前的积分为原来分数的负数
	            		 //插入的记录只 清除  活动积分 
	            		creditsDetailsDao.ins(con, creditsDetailsDb);
	            		 //插入的记录只 清除 培训积分 
	            		delItmIntegralEmpty(con,userEntId,userId,tcr_id,sourceId,app_id);
	            		 //设定当前用户的积分为0
	            		 UserCreditsDB creditsDb=new UserCreditsDB();
	            		 creditsDb.setUct_ent_id(userEntId);
	            		 creditsDb.setUct_total(0);
	            		 creditsDb.setUct_update_timestmp(cwSQL.getTime(con));
	            		 //清空用户积分就是把用户的积分修改为0，实时看到数据更改，在线程启动的时候仍然会统计，因为插入了与原来分数相等的负分，因此会对消积分同时修改为0
	            		 UserCreditsDAO userCreditsDAO=new UserCreditsDAO();
	            		 userCreditsDAO.upd(con, creditsDb);
	            	}else{
	            		//用户积分存在，只是更新当前的积分
	            		creditsDetailsDao.upd(con, creditsDetailsDb);
	            	}
	            } else {
	                creditsDetailsDao.ins(con, creditsDetailsDb);
	                if(Credit.INTEGRAL_EMPTY.equals(creditsType)){
	                	//插入的记录只 清除 培训积分 
	            		delItmIntegralEmpty(con,userEntId,userId,tcr_id,sourceId,app_id);
	                }
	            }
	            // insert log of credits operation
	            UserCreditsDetailLogDB creditsLogDb = new UserCreditsDetailLogDB();
	            UserCreditsDetailLogDAO creditsLogDao = new UserCreditsDetailLogDAO();
	            setCreditsOperLog(creditsTypeDb.getCty_relation_type(), creditsDetailsDb, creditsLogDb, sourceId, credits, app_id);
	            if(isExistCreditDetail && creditsTypeDb.getCty_code().equalsIgnoreCase(ITM_IMPORT_CREDIT)){
	            	creditsLogDao.del(con, creditsLogDb, true);
	            	Dispatcher.creditsLogger.info("===UPDATE ITEM ATTEND CREDIT===");
	            }
	        	creditsLogDao.ins(con, creditsLogDb);
	            Dispatcher.creditsLogger.info("usr_ent_id: " + userEntId + "\t" + "cty_id: " + creditsTypeDb.getCty_id() + "\t" + "ucl_point: " + credits
						 + "\t" + "ucl_relation_type: " + creditsTypeDb.getCty_relation_type() + "\t"+ "ucl_source_id: " + sourceId);
	        }
        }
        return credits;
    }

    private void delItmIntegralEmpty(Connection con,int userEntId,String userId,long tcr_id,long sourceId,long app_id) throws SQLException {
	    	UserCreditsDetailDAO creditsDetailsDao = new UserCreditsDetailDAO();
	    	
	    	CreditsTypeDB creditsTypeDb = new CreditsTypeDB();//积分类型类
	    	creditsTypeDb.setCty_code(ITM_INTEGRAL_EMPTY);//设置积分类型  清除培训积分
	     	//creditsTypeDb.setCty_id((int)198);//设置ID
	     	creditsTypeDb.setCty_tcr_id(tcr_id);//设置培训中心
	     	CreditsTypeDAO creditsTypeDao = new CreditsTypeDAO();//积分数据库层类
	        //通过code 获取积分类型
			creditsTypeDao.getById(con, creditsTypeDb);
	     	
	    	UserCreditsDetailDB creditsDetailsDb = new UserCreditsDetailDB();//用户积分详细数据库类
	        //设定积分类型和被积分的用户
	        creditsDetailsDb.setUcd_cty_id(creditsTypeDb.getCty_id());//设置用户详细数据库类的积分类型ID
	        creditsDetailsDb.setUcd_ent_id(userEntId);//设置用户  据库类的积分类型  用户ID
	        
	        //获取需要清空的培训积分
	        float credits = getCredits(con,userEntId,"train");
			
			setCreditsDetail(con, creditsDetailsDb, userId, creditsTypeDb.getCty_period(), creditsTypeDb.getCty_hit(), credits, creditsTypeDb.getCty_code());
	        
	    	//插入的记录只 清除 培训积分 
			creditsDetailsDao.ins(con, creditsDetailsDb);
			
			// insert log of credits operation
	        UserCreditsDetailLogDB creditsLogDb = new UserCreditsDetailLogDB();
	        UserCreditsDetailLogDAO creditsLogDao = new UserCreditsDetailLogDAO();
	        setCreditsOperLog(creditsTypeDb.getCty_relation_type(), creditsDetailsDb, creditsLogDb, sourceId, credits, app_id);
	       
	    	creditsLogDao.ins(con, creditsLogDb);
	        Dispatcher.creditsLogger.info("usr_ent_id: " + userEntId + "\t" + "cty_id: " + creditsTypeDb.getCty_id() + "\t" + "ucl_point: " + credits
					 + "\t" + "ucl_relation_type: " + creditsTypeDb.getCty_relation_type() + "\t"+ "ucl_source_id: " + sourceId);
    }
    
    private void setCreditsDetail(Connection con, UserCreditsDetailDB creditsDetailsDb,
    		String userId, String cty_period, int cty_hit, float credits, String cty_code)
            throws SQLException {
    	float ucdTotal = 0;
    	int ucdHit = 0;
    	int ucdHitTemp = 0;
    	if(cty_code.equalsIgnoreCase(ITM_IMPORT_CREDIT)){
    		ucdTotal = credits;
    	}else if(cty_code.equals(INTEGRAL_EMPTY)){
    		ucdTotal = credits;
    	}else{
    		ucdTotal = creditsDetailsDb.getUcd_total();
            ucdHit = creditsDetailsDb.getUcd_hit();
            ucdHitTemp = creditsDetailsDb.getUcd_hit_temp();

            // compute data
            ucdTotal += credits;
            ucdHit += 1;
            ucdHitTemp = getHitTemp(creditsDetailsDb.getUcd_update_timestamp(), ucdHitTemp, cty_period, cty_hit);
    	}

        Timestamp curTime = cwSQL.getTime(con);
        creditsDetailsDb.setUcd_total(ucdTotal);
        creditsDetailsDb.setUcd_hit(ucdHit);
        creditsDetailsDb.setUcd_hit_temp(ucdHitTemp);
        creditsDetailsDb.setUcd_create_timestamp(curTime);
        creditsDetailsDb.setUcd_create_usr_id(userId);
        creditsDetailsDb.setUcd_update_timestamp(curTime);
        creditsDetailsDb.setUcd_update_usr_id(userId);
    }

    /**
     * the logic of to get credit.
     *
     * @param creditsTypeDb
     * @return
     */
    private float getCredits(boolean cty_default_credits_ind, boolean cty_deduction_ind, float cty_default_credits, float manualScore, float itm_diff_factor) {
        float credits = 0;
        if (cty_default_credits_ind) {
        	//如果是获取积分的有两种情况，一种是默认积分，一种是倍数积分
            credits = cty_default_credits;
            if (itm_diff_factor > 0) {
            	credits *= itm_diff_factor;
            }
        } else {
        	//如果被扣除的就直接扣除，不扣除的就是默认
        	if (cty_deduction_ind) {
        		credits -= manualScore;
        	} else {
        		credits = manualScore;
        	}
        }
        return credits;
    }
    
    private float getCredits(Connection con,long usr_ent_id,String type) throws SQLException {
    	
    	return  -ViewCreditsDAO.getUserCredits(con,usr_ent_id,type);
    }
    
    private void setCreditsOperLog(String cty_relation_type, UserCreditsDetailDB creditsDetailsDb,
            UserCreditsDetailLogDB creditsLogDb, long sourceId, float credits, long app_id) {
        creditsLogDb.setUcl_usr_ent_id(creditsDetailsDb.getUcd_ent_id());
        creditsLogDb.setUcl_bpt_id(creditsDetailsDb.getUcd_cty_id());
        creditsLogDb.setUcl_relation_type(cty_relation_type);

        creditsLogDb.setUcl_source_id(sourceId);
        creditsLogDb.setUcl_point(credits);
        creditsLogDb.setUcl_create_timestamp(creditsDetailsDb
                .getUcd_update_timestamp());
        creditsLogDb.setUcl_create_usr_id(creditsDetailsDb
                .getUcd_update_usr_id());
        creditsLogDb.setUcl_app_id(app_id);
    }

    /**
     * to validate whether current credit event can operate. 判断当前的学分事件,是否执行学分操作
     *
     * @param con
     * @param creditType
     * @param creditsDetailsDb
     * @param queId
     * @return
     * @throws SQLException
     * @throws cwSysMessage
     * @throws qdbException
     */
    private boolean isUpdateCredits(Connection con, String creditType,
            UserCreditsDetailDB creditsDetailsDb, CreditsTypeDB creditsTypeDb, int sourceId, String userId)
            throws SQLException, cwException {
        boolean isUpdateCredit = true;
        if (!creditsTypeDb.isCty_default_credits_ind()) { // 手工积分
        	isUpdateCredit = true;
        } else if (Credit.ZD_INIT.equalsIgnoreCase(creditType)) {
            // one user can get credit of current type only one time.
            // 新用户首次登录时,能够获取学分
            int hitTotal = creditsDetailsDb.getUcd_hit();
            if (hitTotal <= 0) {
                isUpdateCredit = true;
            } else {
                isUpdateCredit = false;
            }
        } else if (Credit.ZD_COMMIT_ANS.equalsIgnoreCase(creditType)) {
            if (sourceId <= 0) {
                throw new cwException(" The question id " + sourceId + " is invaild.");
            }

            // It will not add credit, when the user who answer question is the
            // same the user who submits question.
            // 用户回答自己提出的问题时,不加分
            KnowQuestionDB knowQueDb = new KnowQuestionDB();
            knowQueDb.setQue_id(sourceId);
            KnowQuestionDAO queDao = new KnowQuestionDAO();
            queDao.getQueByQueId(con, knowQueDb);
            int queCreatorEntId = knowQueDb.getQue_create_ent_id();

            if (queCreatorEntId == creditsDetailsDb.getUcd_ent_id()) {
                isUpdateCredit = false;
            }

            // The user can get credit of current type only one time, when the
            // questions that user have answered are same.
            // 用户多次回答同一个问题时,只有第一次回答时加分
            if (isUpdateCredit) {
				boolean hasUpdate = UserCreditsDetailLogDAO.hasAnswerQue(con, creditsDetailsDb.getUcd_ent_id(), sourceId);
				if (hasUpdate) {
					isUpdateCredit = false;
				}
			}
            
            if (creditsTypeDb.getCty_default_credits() == 0) {
        		isUpdateCredit = false;
        	} else if (!checkPeriod(creditsDetailsDb.getUcd_update_timestamp(), creditsDetailsDb.getUcd_hit_temp(),
        			creditsTypeDb.getCty_period(), creditsTypeDb.getCty_hit())) { //判断积分周期
        		isUpdateCredit = false;
        	}
        } else if (Credit.ZD_RIGHT_ANS.equalsIgnoreCase(creditType)) {
            // It will not add credit, when the user who answer question is the
            // same the user who submits question.
            // 用户采纳自己的回答为最佳答案,不给回答者加分
            KnowQuestionDB knowQueDb = new KnowQuestionDB();
            knowQueDb.setQue_id(sourceId);
            KnowQuestionDAO queDao = new KnowQuestionDAO();
            queDao.getQueByQueId(con, knowQueDb);
            int queCreatorEntId = knowQueDb.getQue_create_ent_id();

            if (queCreatorEntId == creditsDetailsDb.getUcd_ent_id()) {
                isUpdateCredit = false;
            } else {
                isUpdateCredit = true;
            }
            
            if (creditsTypeDb.getCty_default_credits() == 0) {
        		isUpdateCredit = false;
        	} else if (!checkPeriod(creditsDetailsDb.getUcd_update_timestamp(), creditsDetailsDb.getUcd_hit_temp(),
        			creditsTypeDb.getCty_period(), creditsTypeDb.getCty_hit())) { //判断积分周期
        		isUpdateCredit = false;
        	}
        } else if (Credit.SYS_GET_LIKE.equalsIgnoreCase(creditType)) {
        	//每个人只能为一样东西点赞加一次分，重复点赞不加分
        	if(UserCreditsDetailLogDAO.hasUserCreditsDetailLog(con, creditsDetailsDb.getUcd_ent_id(), creditsDetailsDb.getUcd_cty_id(), sourceId, userId)){
        		isUpdateCredit = false;
        	}
        	
        	if (creditsTypeDb.getCty_default_credits() == 0) {
        		isUpdateCredit = false;
        	} else if (!checkPeriod(creditsDetailsDb.getUcd_update_timestamp(), creditsDetailsDb.getUcd_hit_temp(),
        			creditsTypeDb.getCty_period(), creditsTypeDb.getCty_hit())) { //判断积分周期
        		isUpdateCredit = false;
        	}
        	
        } else{
        	
        	if (creditsTypeDb.getCty_default_credits() == 0) {
        		isUpdateCredit = false;
        	} else if (!checkPeriod(creditsDetailsDb.getUcd_update_timestamp(), creditsDetailsDb.getUcd_hit_temp(),
        			creditsTypeDb.getCty_period(), creditsTypeDb.getCty_hit())) { //判断积分周期
        		isUpdateCredit = false;
        	}
        }
       

        return isUpdateCredit;
    }

    private boolean checkPeriod(Timestamp ucd_update_timestamp, int ucd_hit_temp, String cty_period, int cty_hit) {
    	boolean isUpdateCredit = false;
    	if (cty_period == null || ucd_update_timestamp == null) { //没有积分周期 或者 第一次插入
    		isUpdateCredit = true;
    	} else if (cty_hit > 0) {
			Calendar curCal = Calendar.getInstance();
			Calendar updCal = Calendar.getInstance();

			formatData(curCal, updCal, ucd_update_timestamp, cty_period);

			if (curCal.after(updCal)) { //到另一个周期了
				isUpdateCredit = true;
			} else {
				if (ucd_hit_temp < cty_hit) { //还没达到周期最大次数
					isUpdateCredit = true;
				}
			}
    	}
    	
    	//没有周期次数限制
    	if(cty_hit == 0 && null != cty_period){
    		isUpdateCredit = false;
		}
    	
    	return isUpdateCredit;
    }

    private int getHitTemp(Timestamp ucd_update_timestamp, int ucd_hit_temp, String cty_period, int cty_hit) {
    	int hit_temp = 0;
    	if (cty_period == null || ucd_update_timestamp == null) { //没有积分周期 或者 第一次插入
    		hit_temp = ucd_hit_temp + 1;
    	} else if (cty_hit > 0) {
			Calendar curCal = Calendar.getInstance();
			Calendar updCal = Calendar.getInstance();

			formatData(curCal, updCal, ucd_update_timestamp, cty_period);

			if (curCal.after(updCal)) { //到另一个周期了
				hit_temp = 1;
			} else {
				if (ucd_hit_temp < cty_hit) { //还没达到周期最大次数
					hit_temp = ucd_hit_temp + 1;
				}
			}
    	}
    	return hit_temp;
    }

    private void formatData(Calendar curCal, Calendar updCal, Timestamp ucd_update_timestamp, String cty_period) {
    	updCal.setTime(new Date(ucd_update_timestamp.getTime()));

		curCal.set(Calendar.HOUR_OF_DAY, 0);
		curCal.set(Calendar.MINUTE, 0);
		curCal.set(Calendar.SECOND, 0);
		curCal.set(Calendar.MILLISECOND, 0);

		updCal.set(Calendar.HOUR_OF_DAY, 0);
		updCal.set(Calendar.MINUTE, 0);
		updCal.set(Calendar.SECOND, 0);
		updCal.set(Calendar.MILLISECOND, 0);

		if (cty_period.equalsIgnoreCase(CreditsTypeDAO.CTY_PERIOD_MONTH)) { //一月一个周期
			curCal.set(Calendar.DAY_OF_MONTH, 1);
			updCal.set(Calendar.DAY_OF_MONTH, 1);
		} else if (cty_period.equalsIgnoreCase(CreditsTypeDAO.CTY_PERIOD_YEAR)) { //一年一个周期
			curCal.set(Calendar.DAY_OF_MONTH, 1);
			updCal.set(Calendar.DAY_OF_MONTH, 1);
			curCal.set(Calendar.MONTH, 1);
			updCal.set(Calendar.MONTH, 1);
		}
    }

    public static void autoUpdUserCreditsByCos(Connection con, String original_status, long app_itm_id, long app_id, long usr_ent_id,
    		String upd_usr_id, dbCourseEvaluation cov) throws SQLException, cwException, cwSysMessage {
    	if (!dbAiccPath.STATUS_COMPLETE.equalsIgnoreCase(original_status) && dbAiccPath.STATUS_COMPLETE.equalsIgnoreCase(cov.cov_status)) {
		    boolean flag = aeItem.getItmBonusIndByItmId(con, app_itm_id);
		    if (flag) {
		    	//删除该课程学员旧的积分
	    		deleteOldCreditsByCos(con, app_itm_id, app_id, usr_ent_id);
		    	//add log for bonus points
			    Dispatcher.creditsLogger.debug("=============AUTO_BONUS_TYPE_COURSE_COMP============================start=");
			    Dispatcher.creditsLogger.debug("学员名 = " + dbRegUser.getDisplayBil(con, usr_ent_id));
			    Dispatcher.creditsLogger.debug("更新的用户名 = " + dbRegUser.getDisplayBil(con, upd_usr_id));
			    Dispatcher.creditsLogger.debug("课程状态 = " + cov.cov_status);
			    Dispatcher.creditsLogger.debug("课程分数 = " + cov.cov_score);
			    Dispatcher.creditsLogger.debug("课程名 = " + aeItem.getItemTitle(con, app_itm_id));
			    Dispatcher.creditsLogger.debug("itm_id = " + app_itm_id);
			    Dispatcher.creditsLogger.debug("app_id = " + app_id);

		    	float itm_diff_factor = aeItem.getItmDiffFactorByItmId(con, app_itm_id);
		    	Dispatcher.creditsLogger.debug("itm_diff_factor = " + itm_diff_factor);

		    	Credit credit = new Credit();
		    	//成功报读培训
		    	credit.updUserCredits(con, Credit.ITM_ENROLLED, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);

		    	//成绩
		    	credit.cosScoreCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, cov.cov_score, itm_diff_factor);

			    //测验已通过
		    	credit.cosTestPassCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//作业已提交
		    	credit.cosSubmitAssCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//调查问卷已提交
		    	credit.cosSubmitSvyCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//教材已浏览
		    	credit.cosViewRdgCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//“课件”已浏览
		    	credit.cosViewCoursewareCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//“课件”已完成
		    	credit.cosPastCoursewareCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//参考已浏览
		    	credit.cosViewRefCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//视频点播已浏览
		    	credit.cosViewVodCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//答疑栏已参与
		    	//credit.cosViewFaqCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//培训论坛发贴得分
		    	credit.cosInsTopicCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//论坛回贴得分
		    	credit.cosInsMsgCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);

		    	//论坛共享资料得分
		    	credit.cosUploadResCredits(con, app_itm_id, usr_ent_id, app_id, upd_usr_id, itm_diff_factor);
		    }
	        Dispatcher.creditsLogger.debug("=============AUTO_BONUS_TYPE_COURSE_COMP============================end=");
    	}
    }

    public static void deleteOldCreditsByCos(Connection con, long app_itm_id, long app_id, long usr_ent_id) throws SQLException {
    	UserCreditsDetailDB creditsDetailsDb = new UserCreditsDetailDB();
        creditsDetailsDb.setUcd_ent_id((int)usr_ent_id);
    	creditsDetailsDb.setUcd_itm_id(app_itm_id);
    	creditsDetailsDb.setUcd_app_id(app_id);
        UserCreditsDetailDAO creditsDetailsDao = new UserCreditsDetailDAO();
        creditsDetailsDao.delete(con, creditsDetailsDb);

        //delete log of credits operation
        UserCreditsDetailLogDB creditsLogDb = new UserCreditsDetailLogDB();
        UserCreditsDetailLogDAO creditsLogDao = new UserCreditsDetailLogDAO();
        creditsLogDb.setUcl_usr_ent_id((int)usr_ent_id);
        creditsLogDb.setUcl_source_id(app_itm_id);
        creditsLogDb.setUcl_app_id(app_id);
        creditsLogDao.del(con, creditsLogDb, false);
    }

    private void cosScoreCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, String cov_score, float itm_diff_factor) throws SQLException, cwException {
    	try {
	    	String type = null;
		    if (cov_score != null) {
			    float score = Float.parseFloat(cov_score);
			    if (score >= 60 && score < 70) {
			    	type = Credit.ITM_SCORE_PAST_60;
			    } else if (score >= 70 && score < 80) {
			    	type = Credit.ITM_SCORE_PAST_70;
			    } else if (score >= 80 && score < 90){
			    	type = Credit.ITM_SCORE_PAST_80;
			    } else if (score >= 90) {
			    	type = Credit.ITM_SCORE_PAST_90;
			    }

			    if (score >= 60) {
			    	updUserCredits(con, type, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
			    }
		    }
	    } catch (NumberFormatException e) {
		    Dispatcher.creditsLogger.debug("error in format cov.cov_score.");
		    CommonLog.error(e.getMessage(),e);
 	    }
    }

    private void cosTestPassCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, dbModule.MOD_TYPE_TST, dbModuleEvaluation.STATUS_PASSED);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_PAST_TEST, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    	
    	cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, dbModule.MOD_TYPE_DXT, dbModuleEvaluation.STATUS_PASSED);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_PAST_TEST, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosSubmitAssCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, dbModule.MOD_TYPE_ASS, null);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_SUBMIT_ASS, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosSubmitSvyCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, dbModule.MOD_TYPE_SVY, null);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_SUBMIT_SVY, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosViewRdgCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, dbModule.MOD_TYPE_RDG, null);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_VIEW_RDG, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosViewCoursewareCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, new String[]{dbModule.MOD_TYPE_AICC_U, dbModule.MOD_TYPE_SCO, dbModule.MOD_TYPE_NETG_COK},
    								new String[]{dbAiccPath.STATUS_INCOMPLETE, dbAiccPath.STATUS_FAILED});
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_VIEW_COURSEWARE, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosPastCoursewareCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, new String[]{dbModule.MOD_TYPE_AICC_U, dbModule.MOD_TYPE_SCO, dbModule.MOD_TYPE_NETG_COK},
    								new String[]{dbAiccPath.STATUS_COMPLETE, dbAiccPath.STATUS_PASSED});
		for (int i = 0; i < cnt; i++) {
			updUserCredits(con, Credit.ITM_PAST_COURSEWARE, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
		}
    }

    private void cosViewRefCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, dbModule.MOD_TYPE_REF, null);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_VIEW_REF, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosViewVodCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, dbModule.MOD_TYPE_VOD, null);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_VIEW_VOD, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosViewFaqCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrModLearnCnt(con, app_itm_id, usr_ent_id, app_id, dbModule.MOD_TYPE_FAQ, null);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_VIEW_FAQ, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosInsTopicCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrForCnt(con, app_itm_id, usr_ent_id, Credit.ITM_INS_TOPIC);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_INS_TOPIC, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosInsMsgCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrForCnt(con, app_itm_id, usr_ent_id, Credit.ITM_INS_MSG);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_INS_MSG, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    private void cosUploadResCredits(Connection con, long app_itm_id, long usr_ent_id, long app_id, String upd_usr_id, float itm_diff_factor) throws SQLException, cwException {
    	int cnt = getUsrForUploadResCnt(con, app_itm_id, usr_ent_id);
    	for (int i = 0; i < cnt; i++) {
    		updUserCredits(con, Credit.ITM_MSG_UPLOAD_RES, app_itm_id, (int)usr_ent_id, upd_usr_id, 0, app_id, itm_diff_factor,0);
    	}
    }

    public void uploadResCredits(Connection con, long usr_ent_id, String upd_usr_id, String fmg_msg) throws SQLException, cwException {
    	int uploadResCnt = getSearchUploadResCnt(fmg_msg);
	    for (int i = 0; i < uploadResCnt; i++) {
	        updUserCredits(con, Credit.SYS_MSG_UPLOAD_RES, 0, (int)usr_ent_id, upd_usr_id, 0, 0, 0,0);
	  	}
    }

    /**
     * 获取学员在该课程里对应的模块类型所达标(浏览/完成)的次数
     */
    private int getUsrModLearnCnt(Connection con, long itm_id, long usr_ent_id, long app_id, String res_subtype, String mov_status) throws SQLException {
    	int cnt = 0;
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT count(cos_res_id) ")
    	   .append(" FROM Course ")
    	   .append(" INNER JOIN resourcecontent ON (rcn_res_id = cos_res_id) ")
    	   .append(" INNER JOIN module ON (mod_res_id = rcn_res_id_content AND mod_type = ?) ")
    	   .append(" INNER JOIN aeApplication ON (app_itm_id = cos_itm_id AND app_id = ?) ");
    	if (dbModule.MOD_TYPE_ASS.equalsIgnoreCase(res_subtype)) {
	    	sql.append(" INNER JOIN reguser ON (usr_ent_id = ?) ")
	    	   .append(" INNER JOIN progress ON (pgr_usr_id = usr_id AND pgr_res_id = mod_res_id AND pgr_tkh_id = app_tkh_id) ");
    	} else {
    		sql.append(" INNER JOIN moduleEvaluation ON (mov_ent_id = ? AND mov_mod_id = mod_res_id AND mov_tkh_id = app_tkh_id ");
	    	if (mov_status != null) {
	    		sql.append(" AND mov_status = ? ");
	    	}
	    	sql.append(" ) ");
    	}
    	sql.append(" WHERE cos_itm_id = ? ");


    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql.toString());
    		int index = 1;
    		stmt.setString(index++, res_subtype);
    		stmt.setLong(index++, app_id);
    		stmt.setLong(index++, usr_ent_id);
    		if (mov_status != null && !dbModule.MOD_TYPE_ASS.equalsIgnoreCase(res_subtype)) {
    			stmt.setString(index++, mov_status);
    		}
    		stmt.setLong(index++, itm_id);
    		rs = stmt.executeQuery();
    		if (rs.next()) {
    			cnt = rs.getInt(1);
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return cnt;
    }

    private int getUsrModLearnCnt(Connection con, long itm_id, long usr_ent_id, long app_id, String[] mod_type, String[] mov_status) throws SQLException {
    	int cnt = 0;
    	int i;
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT count(cos_res_id) ")
    	   .append(" FROM Course ")
    	   .append(" INNER JOIN resourcecontent ON (rcn_res_id = cos_res_id) ")
    	   .append(" INNER JOIN module ON (mod_res_id = rcn_res_id_content AND (mod_type = '").append(mod_type[0]).append("'");
    	for (i = 1; i < mod_type.length; i++) {
    		sql.append(" OR mod_type = '").append(mod_type[i]).append("'");
    	}
    	sql.append(" )) ")
    	   .append(" INNER JOIN aeApplication ON (app_itm_id = cos_itm_id AND app_id = ?) ")
    	   .append(" INNER JOIN moduleEvaluation ON (mov_ent_id = ? AND mov_mod_id = mod_res_id AND mov_tkh_id = app_tkh_id ");
    	if (mov_status != null) {
    		sql.append(" AND (");
    		for (i = 0; i < mov_status.length; i++ ) {
    			if (i > 0) {
    				sql.append(" OR ");
    			}
    			sql.append(" mov_status = '").append(mov_status[i]).append("'");
    		}
    		sql.append(" ) ");
    	}
    	sql.append(" ) ")
    	   .append(" WHERE cos_itm_id = ? ");


    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql.toString());
    		int index = 1;
    		stmt.setLong(index++, app_id);
    		stmt.setLong(index++, usr_ent_id);
    		stmt.setLong(index++, itm_id);
    		rs = stmt.executeQuery();
    		if (rs.next()) {
    			cnt = rs.getInt(1);
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return cnt;
    }

    /**
     * 统计学员在课程里的论坛发表文章或回复的次数
     * @param isReply 是否回复
     */
    private int getUsrForCnt(Connection con, long itm_id, long usr_ent_id, String creditsType) throws SQLException {
    	int cnt = 0;
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT fmg_create_datetime ")
    	   .append(" FROM Course ")
    	   .append(" INNER JOIN resourcecontent ON (rcn_res_id = cos_res_id) ")
    	   .append(" INNER JOIN module ON (mod_res_id = rcn_res_id_content AND mod_type = ?) ")
    	   .append(" INNER JOIN reguser ON (usr_ent_id = ?) ")
    	   .append(" INNER JOIN ForumMessage ON (fmg_fto_res_id = mod_res_id AND fmg_usr_id = usr_id ");
    	if (Credit.ITM_INS_MSG.equalsIgnoreCase(creditsType)) { //回复
    		sql.append(" AND fmg_fmg_id_parent IS NOT NULL ");
    	} else { //发表文章
    		sql.append(" AND fmg_fmg_id_parent IS NULL ");
    	}
    	sql.append(" ) ")
    	   .append(" WHERE cos_itm_id = ? ");


    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql.toString());
    		int index = 1;
    		stmt.setString(index++, dbModule.MOD_TYPE_FOR);
    		stmt.setLong(index++, usr_ent_id);
    		stmt.setLong(index++, itm_id);
    		rs = stmt.executeQuery();
    		Calendar cal = null;
    		timeCntClass timeClass = null;
    		Map timeHash = new HashMap();
    		CreditsTypeDB creditsTypeDb = new CreditsTypeDB();
            creditsTypeDb.setCty_code(creditsType);
            CreditsTypeDAO creditsTypeDao = new CreditsTypeDAO();
            creditsTypeDao.getByCode(con, creditsTypeDb);

    		while (rs.next()) {
    			cal = getFormatCal(rs.getTimestamp(1), creditsTypeDb.getCty_period());
    			if (timeHash.containsKey(cal)) {
    				timeClass = (timeCntClass)timeHash.get(cal);
    				timeClass.timeCnt++;
    			} else {
    				timeHash.put(cal, new timeCntClass());
    			}
    		}

    		Set set = timeHash.entrySet();
    		Iterator iter = set.iterator();
    		Map.Entry entry = null;
    		while (iter.hasNext()) {
    			//统计每个周期的发帖或回帖数
    			entry = (Map.Entry)iter.next();
    			timeClass = (timeCntClass)entry.getValue();
    			if (timeClass.timeCnt > creditsTypeDb.getCty_hit()) {
    				cnt += creditsTypeDb.getCty_hit();
    			} else {
    				cnt += timeClass.timeCnt;
    			}
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return cnt;
    }

    private Calendar getFormatCal(Timestamp time, String cty_period) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date(time.getTime()));
    	cal.set(Calendar.HOUR_OF_DAY, 0);
    	cal.set(Calendar.MINUTE, 0);
    	cal.set(Calendar.SECOND, 0);
    	cal.set(Calendar.MILLISECOND, 0);

		if (cty_period.equalsIgnoreCase(CreditsTypeDAO.CTY_PERIOD_MONTH)) { //一月一个周期
			cal.set(Calendar.DAY_OF_MONTH, 1);
		} else if (cty_period.equalsIgnoreCase(CreditsTypeDAO.CTY_PERIOD_YEAR)) { //一年一个周期
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.MONTH, 1);
		}
    	return cal;
    }

    /**
     * 统计学员在课程里的论坛共享资料的数量
     */
    private int getUsrForUploadResCnt(Connection con, long itm_id, long usr_ent_id) throws SQLException {
    	int cnt = 0;
    	StringBuffer sql = new StringBuffer();
    	sql.append(" SELECT fmg_msg ")
    	   .append(" FROM Course ")
    	   .append(" INNER JOIN resourcecontent ON (rcn_res_id = cos_res_id) ")
    	   .append(" INNER JOIN module ON (mod_res_id = rcn_res_id_content AND mod_type = ?) ")
    	   .append(" INNER JOIN reguser ON (usr_ent_id = ?) ")
    	   .append(" INNER JOIN ForumMessage ON (fmg_fto_res_id = mod_res_id AND fmg_usr_id = usr_id) ")
    	   .append(" WHERE cos_itm_id = ? ");

    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql.toString());
    		int index = 1;
    		stmt.setString(index++, dbModule.MOD_TYPE_FOR);
    		stmt.setLong(index++, usr_ent_id);
    		stmt.setLong(index++, itm_id);
    		rs = stmt.executeQuery();
    		while (rs.next()) {
    			/*
    			 * 帖子里面的内容都是保存整个html的，所以用搜索 '<A target=' 出现的次数来判断附件的数量
    			 * 会有不准确的情况出现的: 1. 插入的连接也是以'<A target='开头；  2. 手动在帖子内容里输入 '<A target='
    			 */
    			cnt += getSearchUploadResCnt(rs.getString(1));
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return cnt;
    }

    public int getSearchUploadResCnt(String fmg_msg) {
    	int cnt = 0;
    	Pattern p = Pattern.compile("<A target=");
    	Matcher m = p.matcher(fmg_msg);
    	while (m.find()) {
    		cnt++;
    	}
    	return cnt;
    }

    public boolean isCosForum(Connection con, long res_id) throws SQLException {
    	boolean result = false;
    	String sql = " SELECT rcn_res_id FROM resourcecontent WHERE rcn_res_id_content = ? ";

    	PreparedStatement stmt = null;
    	ResultSet rs = null;
    	try {
    		stmt = con.prepareStatement(sql);
    		int index = 1;
    		stmt.setLong(index++, res_id);
    		rs = stmt.executeQuery();
    		if (rs.next()) {
    			result = true;
    		}
    	} finally {
    		cwSQL.cleanUp(rs, stmt);
    	}
    	return result;
    }

	/**
	 * Get auto credit point types as xml.
	 * @param con Db connection.
	 * @return Auto credit point type xml.
	 * @throws SQLException
	 */
	public String getAutoPointSettingAsXml(Connection con, long top_tcr_id) throws SQLException {
	    StringBuffer resultXml = new StringBuffer();
		CreditsTypeDAO ctyDao = new CreditsTypeDAO();
		Vector bonus_lst = ctyDao.getAllAutoCredit(con,top_tcr_id);
		if(bonus_lst == null || bonus_lst.size() < 1){
		    bonus_lst = ctyDao.getAllAutoCredit(con,ViewTrainingCenter.getRootTcId( con));
		}
		resultXml.append("<auto_cty_lst>");
		for(int i=0; i<bonus_lst.size(); i++){
			CreditsTypeBean creditsTypeBean = new CreditsTypeBean();
			creditsTypeBean = (CreditsTypeBean)bonus_lst.get(i);
			resultXml.append("<cty id=\"").append(creditsTypeBean.getCty_id()).append("\" relation_type=\"").append(creditsTypeBean.getCty_relation_type()).append("\">");
			resultXml.append("<code>").append(cwUtils.esc4XML(creditsTypeBean.getCty_code())).append("</code>");
			resultXml.append("<lower_code>").append(cwUtils.esc4XML(creditsTypeBean.getCty_code().toLowerCase())).append("</lower_code>");
			resultXml.append("<default_credits>").append(creditsTypeBean.getCty_default_credits()).append("</default_credits>");
			resultXml.append("<period>").append(cwUtils.escNull(creditsTypeBean.getCty_period())).append("</period>");
			resultXml.append("<hit>").append(creditsTypeBean.getCty_hit()).append("</hit>");
			resultXml.append("<update_timestamp>").append(creditsTypeBean.getCty_update_timestamp()).append("</update_timestamp>");
			resultXml.append("</cty>");
		}
		resultXml.append("</auto_cty_lst>");
	    return resultXml.toString();
	}

    public void updateAutoPointSetting(loginProfile prof, CreditModuleParam modParam, Connection con, long my_top_tc_id) throws SQLException {
		Timestamp cur_time = modParam.getCur_time();
		String upd_usr_id = prof.usr_id;
		CreditsTypeDAO.updAutoCreditType(con, ZD_INIT, upd_usr_id, cur_time, modParam.getScore_zd_init(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, SYS_NORMAL_LOGIN, upd_usr_id, cur_time, modParam.getScore_sys_normal_login(), modParam.getHit_sys_normal_login(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, SYS_UPD_MY_PROFILE, upd_usr_id, cur_time, modParam.getScore_sys_upd_my_profile(), modParam.getHit_sys_upd_my_profile(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, SYS_SUBMIT_SVY, upd_usr_id, cur_time, modParam.getScore_sys_submit_svy(), modParam.getHit_sys_submit_svy(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, SYS_INS_TOPIC, upd_usr_id, cur_time, modParam.getScore_sys_ins_topic(), modParam.getHit_sys_ins_topic(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, SYS_INS_MSG, upd_usr_id, cur_time, modParam.getScore_sys_ins_msg(), modParam.getHit_sys_ins_msg(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, SYS_MSG_UPLOAD_RES, upd_usr_id, cur_time, modParam.getScore_sys_msg_upload_res(), modParam.getHit_sys_msg_upload_res(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ZD_NEW_QUE, upd_usr_id, cur_time, modParam.getScore_zd_new_que(), modParam.getHit_zd_new_que(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ZD_COMMIT_ANS, upd_usr_id, cur_time, modParam.getScore_zd_commit_ans(), modParam.getHit_zd_commit_ans(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ZD_RIGHT_ANS, upd_usr_id, cur_time, modParam.getScore_zd_right_ans(), modParam.getHit_zd_right_ans(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ZD_CANCEL_QUE, upd_usr_id, cur_time, modParam.getScore_zd_cancel_que(), modParam.getHit_zd_cancel_que(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_ENROLLED, upd_usr_id, cur_time, modParam.getScore_itm_enrolled(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_SCORE_PAST_60, upd_usr_id, cur_time, modParam.getScore_itm_score_past_60(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_SCORE_PAST_70, upd_usr_id, cur_time, modParam.getScore_itm_score_past_70(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_SCORE_PAST_80, upd_usr_id, cur_time, modParam.getScore_itm_score_past_80(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_SCORE_PAST_90, upd_usr_id, cur_time, modParam.getScore_itm_score_past_90(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_PAST_TEST, upd_usr_id, cur_time, modParam.getScore_itm_past_test(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_SUBMIT_ASS, upd_usr_id, cur_time, modParam.getScore_itm_submit_ass(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_SUBMIT_SVY, upd_usr_id, cur_time, modParam.getScore_itm_submit_svy(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_VIEW_RDG, upd_usr_id, cur_time, modParam.getScore_itm_view_rdg(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_VIEW_COURSEWARE, upd_usr_id, cur_time, modParam.getScore_itm_view_courseware(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_PAST_COURSEWARE, upd_usr_id, cur_time, modParam.getScore_itm_past_courseware(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_VIEW_REF, upd_usr_id, cur_time, modParam.getScore_itm_view_ref(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_VIEW_VOD, upd_usr_id, cur_time, modParam.getScore_itm_view_vod(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_VIEW_FAQ, upd_usr_id, cur_time, modParam.getScore_itm_view_faq(), 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_INS_TOPIC, upd_usr_id, cur_time, modParam.getScore_itm_ins_topic(), modParam.getHit_itm_ins_topic(),my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, ITM_INS_MSG, upd_usr_id, cur_time, modParam.getScore_itm_ins_msg(), modParam.getHit_itm_ins_msg(),my_top_tc_id);
	    CreditsTypeDAO.updAutoCreditType(con, ITM_MSG_UPLOAD_RES, upd_usr_id, cur_time, modParam.getScore_itm_msg_upload_res(), modParam.getHit_itm_msg_upload_res(),my_top_tc_id);	    
	    CreditsTypeDAO.updAutoCreditType(con, CREATE_GROUP, upd_usr_id, cur_time, modParam.getScore_sys_create_group(), modParam.getHit_sys_create_group(),my_top_tc_id);
	    CreditsTypeDAO.updAutoCreditType(con, SYS_JION_GROUP, upd_usr_id, cur_time, modParam.getScore_sys_jion_group(), modParam.getHit_sys_jion_group(),my_top_tc_id);
	    CreditsTypeDAO.updAutoCreditType(con, SYS_GET_LIKE, upd_usr_id, cur_time, modParam.getScore_sys_get_like(), modParam.getHit_sys_get_like(),my_top_tc_id);
	    CreditsTypeDAO.updAutoCreditType(con, SYS_CLICK_LIKE, upd_usr_id, cur_time, modParam.getScore_sys_click_like(), modParam.getHit_sys_click_like(),my_top_tc_id);
	    CreditsTypeDAO.updAutoCreditType(con, SYS_COS_COMMENT, upd_usr_id, cur_time, modParam.getScore_sys_cos_comment(), modParam.getHit_sys_cos_comment(),my_top_tc_id);
	    CreditsTypeDAO.updAutoCreditType(con, SYS_QUESTION_BOUNTY, upd_usr_id, cur_time, 0, 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, SYS_ANWSER_BOUNTY, upd_usr_id, cur_time, 0, 0,my_top_tc_id);
		CreditsTypeDAO.updAutoCreditType(con, KB_SHARE_KNOWLEDGE, upd_usr_id, cur_time, modParam.getScore_kb_share_knowledge(), modParam.getHit_kb_share_knowledge(),my_top_tc_id);

    }

		public class resultData {
		public resultData() {
			;
		}

		public long usr_ent_id;
		public String usr_ste_usr_id;
		public String usr_display_bil;
		public String group_name;
		public String grade_name;
		public float trainscore;
		public float activityscore;
		public int act_ind;
		public long totalRecode;
		public String jifen_type;
		public String cty_code;
		//for Detail
		public String jifen_source;
		public boolean jifen_manual_ind;
		public Timestamp ucl_create_timestamp;
	}

	public class dataByGroup {
		public dataByGroup() {
			;
		}

		public String group_name;
		public long usrcount;
		public float trainscore;
		public float activityscore;
		public float totalscore;
		public long totalRecode;
	}

	/**
	 * 报告前台xml数据
	 * @param con
	 * @param ent_id_lst
	 * @param isDetail
	 * @param containDel
	 * @param isGroupAndUser
	 * @param start_time
	 * @param end_time
	 * @return
	 * @throws SQLException
	 */
	public String getRptXml(Connection con, Hashtable spec_pairs, boolean tc_enabled, long root_ent_id, long usr_ent_id) throws SQLException {
		Vector ent_id_vec = null;
		String[] ent_id_lst = null;
		boolean all_usg_ind = false;// 所有用户组，如果tc_enabled为true，则为所有负责的用户组
		boolean containDel = false;// 是否包含已删除用户
		boolean show_usg_only = false;// 只显示用户组积分统计
		boolean isDetail = false;// 是否是明细
		Timestamp start_time = null;
		Timestamp end_time = null;

		if (spec_pairs.containsKey("all_usg_ind")) {
			int ind = Integer.parseInt((String) ((Vector) spec_pairs.get("all_usg_ind")).get(0));
			if (ind == 1)
				all_usg_ind = true;
		}
		if (spec_pairs.containsKey("usg_ent_id")) {
			ent_id_vec = (Vector) spec_pairs.get("usg_ent_id");
		}
		if (spec_pairs.containsKey("is_detail_ind")) {
			int ind = Integer.parseInt((String) ((Vector) spec_pairs.get("is_detail_ind")).get(0));
			if (ind == 1)
				isDetail = true;
		}
		if (spec_pairs.containsKey("include_del_usr_ind")) {
			int ind = Integer.parseInt((String) ((Vector) spec_pairs.get("include_del_usr_ind")).get(0));
			if (ind == 1)
				containDel = true;
		}
		if (spec_pairs.containsKey("show_usg_only")) {
			show_usg_only = new Boolean((String) ((Vector) spec_pairs.get("show_usg_only")).get(0)).booleanValue();
		}
		if (spec_pairs.containsKey("att_create_start_datetime")) {
			start_time = Timestamp.valueOf((String) ((Vector) spec_pairs.get("att_create_start_datetime")).get(0));
		}
		if (spec_pairs.containsKey("att_create_end_datetime")) {
			end_time = Timestamp.valueOf((String) ((Vector) spec_pairs.get("att_create_end_datetime")).get(0));
		}

		if (all_usg_ind) {
			if (tc_enabled) {
				// 所有我为officer的用户组
				ent_id_vec = dbUserGroup.getAllTargetGroupIdForOfficer(con, usr_ent_id);
			} else {
				// 所有用户组
				ent_id_vec = dbUserGroup.getAllGroup(con, root_ent_id);
			}
		}
		ent_id_lst = new String[ent_id_vec.size()];
		for (int i = 0; i < ent_id_vec.size(); i++) {
			ent_id_lst[i] = ((Object) ent_id_vec.elementAt(i)).toString();
		}

		StringBuffer xml = new StringBuffer();
		Hashtable hash = null;
		Vector vtdata = null;
		resultData data = null;
		dataByGroup groupdata = null;
		cwPagination pageination = new cwPagination();
		xml.append("<report_list ");
		if (isDetail) {// 显示积分明细
			xml.append("type=\"usr_detail\">");
			vtdata = creditDetailOfUser(con, ent_id_lst, containDel, start_time, end_time);
			int size = vtdata.size();
			if (size > RPT_PAGE_COUNT) {
				size = RPT_PAGE_COUNT;
			}
			for (int i = 0; i < size; i++) {
				data = (resultData) vtdata.get(i);
				xml.append("<record>").append("<usr usr_ent_id =\"").append(data.usr_ent_id).append("\" usr_ste_usr_id=\"").append(
						dbUtils.esc4XML(data.usr_ste_usr_id));
				xml.append("\" displaybil=\"").append(dbUtils.esc4XML(data.usr_display_bil)).append("\" group=\"").append(
						dbUtils.esc4XML(data.group_name)).append("\" grade=\"").append(dbUtils.esc4XML(data.grade_name));
				xml.append("\" email=\"").append("\" act_ind=\"").append(data.act_ind).append("\" jf_manual=\"").append(data.jifen_manual_ind);
				xml.append("\" jf_create_timestamp=\"").append(data.ucl_create_timestamp).append("\" jf_source=\"").append(
						dbUtils.esc4XML(data.jifen_source)).append("\" jifen_type=\"");
				if (data.jifen_type.equals(ACTIVITY_SCORE)) {
					xml.append(ACTIVITY_SCORE).append("\" jifen=\"").append(data.activityscore).append("\" jf_name=\"").append(
							dbUtils.esc4XML(data.cty_code)).append("\"/>").append("</record>");
				} else {
					xml.append(TRAINNING_SCORE).append("\" jifen=\"").append(data.trainscore).append("\" jf_name=\"").append(
							dbUtils.esc4XML(data.cty_code)).append("\"/>").append("</record>");
				}
			}
			pageination.totalRec = vtdata.size();
			pageination.pageSize = RPT_PAGE_COUNT;
			pageination.curPage = 1;
			xml.append(pageination.asXML());
		} else { //不显示积分明细
			if (show_usg_only) {
				xml.append("type=\"usg\">");
			} else {
				xml.append("type=\"usg_n_usr\">");
			}
			hash = getScoreData(con, ent_id_lst, containDel, start_time, end_time);
			Vector[] vtlit = new Vector[2];
			Vector vtgroupdata = null;
			Vector vtresdata = null;
			int count = 1;
			Enumeration enumeration = hash.keys();
			while (enumeration.hasMoreElements()) {
				if (count > RPT_PAGE_COUNT)
					break;
				vtlit = (Vector[]) hash.get((Long) enumeration.nextElement());
				vtresdata = vtlit[0];
				vtgroupdata = vtlit[1];
				groupdata = (dataByGroup) vtgroupdata.get(0);
				if (show_usg_only) {
					xml.append("<record>").append("<group name=\"").append(dbUtils.esc4XML(groupdata.group_name)).append("\" count=\"").append(
							groupdata.usrcount);
					xml.append("\" train_jifen=\"").append(groupdata.trainscore).append("\" activity_jifen=\"").append(groupdata.activityscore)
							.append("\" total_jifen=\"").append(groupdata.totalscore).append("\"/>");
					xml.append("</record>");
				} else {
					xml.append("<record>").append("<group name=\"").append(dbUtils.esc4XML(groupdata.group_name)).append("\" count=\"").append(
							groupdata.usrcount);
					xml.append("\" train_jifen=\"").append(groupdata.trainscore).append("\" activity_jifen=\"").append(groupdata.activityscore)
							.append("\" total_jifen=\"").append(groupdata.totalscore).append("\"/>");

					int size = vtresdata.size();
					if (size > RPT_PAGE_COUNT) {
						size = RPT_PAGE_COUNT;
					}
					for (int j = 0; j < size; j++) {
						data = (resultData) vtresdata.get(j);
						xml.append("<usr usr_ent_id =\"").append(data.usr_ent_id).append("\" usr_ste_usr_id=\"").append(
								dbUtils.esc4XML(data.usr_ste_usr_id));
						xml.append("\" displaybil=\"").append(dbUtils.esc4XML(data.usr_display_bil)).append("\" group=\"").append(
								dbUtils.esc4XML(data.group_name)).append("\" grade=\"").append(dbUtils.esc4XML(data.grade_name));
						xml.append("\" email=\"").append("\" phone=\"").append("\" train_jifen=\"").append(data.trainscore).append(
								"\" activity_jifen=\"").append(data.activityscore)
								.append("\" total_jifen=\"").append(data.trainscore + data.activityscore).append("\"/>");
					}
					pageination.totalRec = vtresdata.size();
					pageination.pageSize = RPT_PAGE_COUNT;
					pageination.curPage = 1;
					xml.append(pageination.asXML());
					xml.append("</record>");
				}
				count++;
			}
			pageination.totalRec = hash.size();
			pageination.pageSize = RPT_PAGE_COUNT;
			pageination.curPage = 1;
			xml.append(pageination.asXML());
		}
		xml.append("</report_list>");
		return xml.toString();
	}

	/**
	 * 用户积分明细数据
	 *
	 * @param con
	 * @param ent_id_lst
	 *            用户id
	 * @param containDel
	 *            是否包含已删除
	 * @param start_time
	 *            开始时间
	 * @param end_time
	 *            结束时间
	 * @return
	 * @throws SQLException
	 */
	public Vector creditDetailOfUser(Connection con, String[] ent_id_lst, boolean containDel, Timestamp start_time, Timestamp end_time)
			throws SQLException {
		String tableName = null;
		String sql = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Vector vtdata = new Vector();
		long[] entIds = cwUtils.stringArray2LongArray(ent_id_lst);
		Vector vtEntId = ViewEntityRelation.getUserIdsByUsgEntIds(con, entIds, containDel,true);

		if (vtEntId != null && vtEntId.size() > 0) {
			tableName = cwSQL.createSimpleTemptable(con, "tmp_ent_id", cwSQL.COL_TYPE_LONG, 0);
			cwSQL.insertSimpleTempTable(con, tableName, vtEntId, cwSQL.COL_TYPE_LONG);
			
		} else {
			return vtdata;
		}
		
		 MYSQLDbHelper mysqlDbHelper = null;
 		 boolean isMysql = false;
 			if(cwSQL.DBVENDOR_MYSQL.equalsIgnoreCase(cwSQL.getDbType())){
 				mysqlDbHelper = new MYSQLDbHelper();
 				isMysql = true;
 			}
 			 String physicalTableName = null;
 			if(isMysql){
 				physicalTableName = mysqlDbHelper.tempTable2Physical(con, tableName);
			}
		
		try{
			sql = "select usr_ent_id,usr_ste_usr_id,usr_display_bil,usg.ern_ancestor_ent_id group_id,ugr.ern_ancestor_ent_id grade_id,"
					+ " cty_code,cty_deduction_ind,cty_manual_ind,ucl_point,ucl_relation_type,itm_title,ucl_create_timestamp"
					+ " from userCreditsDetailLog" + " inner join reguser on (ucl_usr_ent_id = usr_ent_id)"
					+ " inner join creditsType on (ucl_bpt_id = cty_id and cty_relation_total_ind = 1 and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0))"
					+ " inner join entityRelation usg on (usg.ern_child_ent_id = usr_ent_id and usg.ern_type = ? and usg.ern_parent_ind = ?)"
					+ " left join entityRelation ugr on (ugr.ern_child_ent_id = usr_ent_id and ugr.ern_type = ? and ugr.ern_parent_ind = ?)"
					+ " inner join " + (isMysql==true?physicalTableName:tableName) + " on (usr_ent_id  = tmp_ent_id) "
					+ " left join aeItem on (itm_id = ucl_source_id and ucl_relation_type = ?)";

			String conditionSql = "";
			if (start_time != null && end_time == null) {
				conditionSql += " where ucl_create_timestamp >= ?";
			} else if (start_time == null && end_time != null) {
				conditionSql += " where ucl_create_timestamp <= ?";
			} else if (start_time != null && end_time != null) {
				conditionSql += " where ucl_create_timestamp between ? and ?";
			}
			sql += conditionSql;

			if (containDel) {
				sql += " union " + " select usr_ent_id,usr_ste_usr_id,usr_display_bil,usg.erh_ancestor_ent_id group_id,ugr.erh_ancestor_ent_id grade_id,"
						+ " cty_code,cty_deduction_ind,cty_manual_ind,ucl_point,ucl_relation_type,itm_title,ucl_create_timestamp"
						+ " from userCreditsDetailLog" + " inner join reguser on (ucl_usr_ent_id = usr_ent_id and usr_status = 'DELETED')"
						+ " inner join creditsType on (ucl_bpt_id = cty_id and cty_relation_total_ind = 1 and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0))"
						+ " inner join entityRelationHistory usg on (usg.erh_child_ent_id = usr_ent_id and usr_status = 'DELETED' and usg.erh_type = ? and usg.erh_parent_ind = ?)"
						+ " left join entityRelationHistory ugr on (ugr.erh_child_ent_id = usr_ent_id and usr_status = 'DELETED' and ugr.erh_type = ? and ugr.erh_parent_ind = ?)"
						+ " inner join " +  (isMysql==true?physicalTableName:tableName)  + " on (usr_ent_id  = tmp_ent_id) "
						+ " left join aeItem on (itm_id = ucl_source_id and ucl_relation_type = ?)";
				sql += conditionSql;
			}

			sql += " order by usr_ent_id asc, ucl_create_timestamp desc";

			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
			stmt.setBoolean(index++, true);
			stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
			stmt.setBoolean(index++, true);
			stmt.setString(index++, CTY_COURSE);
			if (start_time != null) {
				stmt.setTimestamp(index++, start_time);
			}
			if (end_time != null) {
				stmt.setTimestamp(index++, end_time);
			}
			if (containDel) {
				stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
				stmt.setBoolean(index++, true);
				stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
				stmt.setBoolean(index++, true);
				stmt.setString(index++, CTY_COURSE);
				if (start_time != null) {
					stmt.setTimestamp(index++, start_time);
				}
				if (end_time != null) {
					stmt.setTimestamp(index++, end_time);
				}
			}

			EntityFullPath entityFullPath = EntityFullPath.getInstance(con);
			rs = stmt.executeQuery();
			while (rs.next()) {
				resultData data = new resultData();
				data.usr_ent_id = rs.getLong("usr_ent_id");
				data.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
				data.usr_display_bil = rs.getString("usr_display_bil");
				data.group_name = entityFullPath.getFullPath(con, rs.getLong("group_id"));
				data.grade_name = entityFullPath.getEntityName(con, rs.getLong("grade_id"));
				data.cty_code = rs.getString("cty_code");
				if (CTY_COURSE.equalsIgnoreCase(rs.getString("ucl_relation_type"))) {
					data.jifen_type = TRAINNING_SCORE;
					data.trainscore = rs.getFloat("ucl_point");
				} else {
					data.jifen_type = ACTIVITY_SCORE;
					data.activityscore = rs.getFloat("ucl_point");
				}
				if(INTEGRAL_EMPTY.equals(data.cty_code)) {
					data.act_ind =2;
				}else{	
					data.act_ind = rs.getInt("cty_deduction_ind");
				}
				//data.act_ind = rs.getInt("cty_deduction_ind");
				data.jifen_manual_ind = rs.getBoolean("cty_manual_ind");
				data.jifen_source = rs.getString("itm_title");
				data.ucl_create_timestamp = rs.getTimestamp("ucl_create_timestamp");
				vtdata.add(data);
			}
		}finally {
			if (tableName != null) {
				cwSQL.dropTempTable(con, tableName);
				if(isMysql){
					mysqlDbHelper.dropTable(con, physicalTableName);
				}
			}
			cwSQL.cleanUp(rs, stmt);
		}
		return vtdata;
	}

	/**
	 * @param groupId	所选用户组
	 * @param containDel	是否包含已删除
	 * @param start_time	开始时间
	 * @param end_time		结束时间
	 */
	public Hashtable getScoreData(Connection con, String[] group_Id_lst, boolean containDel, Timestamp start_time, Timestamp end_time)
	throws SQLException {
		Hashtable hash = new Hashtable();
		long id = 0;
		String tableName = null;
		String sql = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long cur_ent_id = 0;
		long back_ent_id = 0;
		Vector vtdata=null;
		Vector vtgroupdata=null;
		Vector[] vt_data=null;
		Vector vtId=null;
		String inId="";
		EntityFullPath entityFullPath = EntityFullPath.getInstance(con);
		tableName = cwSQL.createSimpleTemptable(con, "tmp_ent_id", cwSQL.COL_TYPE_LONG, 0);		//创建一个临时表，用于当需要In的数据大于1000时使用
		for (int i = 0; i < group_Id_lst.length; i++) {
			vtdata = new Vector();
			vtgroupdata = new Vector();
			vt_data = new Vector[2];
			float grp_train_score = 0;
			float grp_activity_score = 0;
			long count = 0;
			String type = null;
			id = Long.parseLong(group_Id_lst[i]);
			vtId = ViewEntityRelation.getUserIdsByUsgEntIds(con, new long[] { id }, containDel,false);
			vtId.add(new Long(id));
			if (vtId != null && vtId.size() > 0 &&vtId.size()>=1000) {
				cwSQL.insertSimpleTempTable(con, tableName, vtId, cwSQL.COL_TYPE_LONG);
			}else{
				inId=cwUtils.vector2list(vtId);
			}
			try {
				if (start_time == null && end_time == null) {
					sql = "select usr_ent_id, usr_ste_usr_id,usr_display_bil, usg.ern_ancestor_ent_id group_id,ugr.ern_ancestor_ent_id grade_id, cty_relation_type, ucd_total, ucd_cty_id, ucd_itm_id, ucd_app_id"
							+ " from userCreditsDetail inner join reguser on (ucd_ent_id = usr_ent_id)"
							+ " inner join creditsType on (cty_id = ucd_cty_id and cty_relation_total_ind = 1 and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0))"
							+ " inner join entityRelation usg on (usg.ern_child_ent_id = usr_ent_id and usg.ern_type = ? and usg.ern_parent_ind = ?)"
							+ " inner join entityRelation ugr on (ugr.ern_child_ent_id = usr_ent_id and ugr.ern_type = ? and ugr.ern_parent_ind = ?)";
							if(vtId != null && vtId.size() > 0 &&vtId.size()>=1000){
								sql+= " inner join " + tableName + " on (usr_ent_id = tmp_ent_id)";
							}else{
								sql+="and usr_ent_id in"+inId;
							}
					if (containDel) {
						sql += " union "
								+ " select usr_ent_id, usr_ste_usr_id,usr_display_bil, usg.erh_ancestor_ent_id group_id,ugr.erh_ancestor_ent_id grade_id, cty_relation_type, ucd_total, ucd_cty_id, ucd_itm_id, ucd_app_id"
								+ " from userCreditsDetail inner join reguser on (ucd_ent_id = usr_ent_id)"
								+ " inner join creditsType on (cty_id = ucd_cty_id and cty_relation_total_ind = 1 and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0))"
								+ " inner join entityRelationHistory usg on (usg.erh_child_ent_id = usr_ent_id and usg.erh_type = ? and usg.erh_parent_ind = ?)"
								+ " inner join entityRelationHistory ugr on (ugr.erh_child_ent_id = usr_ent_id and ugr.erh_type = ? and ugr.erh_parent_ind = ?)";
								if(vtId != null && vtId.size() > 0 &&vtId.size()>=1000){
									sql+= " inner join " + tableName + " on (usr_ent_id = tmp_ent_id)";
								}else{
									sql+="and usr_ent_id in"+inId;
								}
					}
					sql += " order by usr_ent_id";
				} else {
					sql = "select usr_ent_id, usr_ste_usr_id,usr_display_bil, usg.ern_ancestor_ent_id group_id,ugr.ern_ancestor_ent_id grade_id,cty_deduction_ind,cty_relation_type,ucl_point,ucl_create_timestamp,ucl_bpt_id"
							+ " from userCreditsDetailLog"
							+ " inner join reguser on (ucl_usr_ent_id = usr_ent_id)"
							+ " inner join creditsType on (ucl_bpt_id = cty_id and cty_relation_total_ind = 1 and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0))"
							+ " inner join entityRelation usg on (usg.ern_child_ent_id = usr_ent_id and usg.ern_type = ? and usg.ern_parent_ind = ?)"
							+ " inner join entityRelation ugr on (ugr.ern_child_ent_id = usr_ent_id and ugr.ern_type = ? and ugr.ern_parent_ind = ?)";
							if(vtId != null && vtId.size() > 0 &&vtId.size()>=1000){
								sql+= " inner join " + tableName + " on (usr_ent_id = tmp_ent_id)";
							}else{
								sql+="and usr_ent_id in"+inId;
							}
					String conditionSql = "";
					if (start_time != null && end_time == null) {
						conditionSql += " where ucl_create_timestamp >= ? ";
					} else if (start_time == null && end_time != null) {
						conditionSql += " where ucl_create_timestamp <= ? ";
					} else if (start_time != null && end_time != null) {
						conditionSql += " where ucl_create_timestamp between ? and ? ";
					}
					sql += conditionSql;
					if (containDel) {
						sql += " union select usr_ent_id, usr_ste_usr_id,usr_display_bil, usg.erh_ancestor_ent_id group_id,ugr.erh_ancestor_ent_id grade_id,cty_deduction_ind,cty_relation_type,ucl_point,ucl_create_timestamp,ucl_bpt_id"
								+ " from userCreditsDetailLog"
								+ " inner join reguser on (ucl_usr_ent_id = usr_ent_id)"
								+ " inner join creditsType on (ucl_bpt_id = cty_id and cty_relation_total_ind = 1 and ((cty_manual_ind = 1 and cty_deleted_ind = 1) or cty_deleted_ind = 0))"
								+ " inner join entityRelationHistory usg on (usg.erh_child_ent_id = usr_ent_id and usg.erh_type = ? and usg.erh_parent_ind = ?)"
								+ " inner join entityRelationHistory ugr on (ugr.erh_child_ent_id = usr_ent_id and ugr.erh_type = ? and ugr.erh_parent_ind = ?)";
								if(vtId != null && vtId.size() > 0 &&vtId.size()>=1000){
									sql+= " inner join " + tableName + " on (usr_ent_id = tmp_ent_id)";
								}else{
									sql+=" and usr_ent_id in"+inId;
								}
						sql += conditionSql;
					}
					sql += " order by usr_ent_id, ucl_create_timestamp desc ";
				}
				stmt = con.prepareStatement(sql);
				int index = 1;
				stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
				stmt.setBoolean(index++, true);
				stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
				stmt.setBoolean(index++, true);
				if (start_time != null) {
					stmt.setTimestamp(index++, start_time);
				}
				if (end_time != null) {
					stmt.setTimestamp(index++, end_time);
				}
				if (containDel) {
					stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_PARENT_USG);
					stmt.setBoolean(index++, true);
					stmt.setString(index++, dbEntityRelation.ERN_TYPE_USR_CURRENT_UGR);
					stmt.setBoolean(index++, true);
					if (start_time != null) {
						stmt.setTimestamp(index++, start_time);
					}
					if (end_time != null) {
						stmt.setTimestamp(index++, end_time);
					}
				}
				rs = stmt.executeQuery();
				float activityscore = 0;
				float trainscore = 0;
				float score = 0;
				int act_ind = 0;
				if (start_time != null || end_time != null) {
					resultData resdata = null;
					while (rs.next()) {
						cur_ent_id = rs.getLong("usr_ent_id");
						type = rs.getString("cty_relation_type");
						score = rs.getFloat("ucl_point");
						act_ind = rs.getInt("cty_deduction_ind");
						if (back_ent_id != cur_ent_id) {
							resultData backdata = resdata;// 保存前一个data
							if (back_ent_id != 0 && backdata != null) {// 第一条记录不保存
								backdata.activityscore = cwUtils.roundingFloat(activityscore, 2);;
								backdata.trainscore = cwUtils.roundingFloat(trainscore, 2);;
		
								activityscore = 0;
								trainscore = 0;
								count++;
								backdata.totalRecode = 1;
								vtdata.add(backdata);
		
								// 累加用户组积分
								grp_train_score += backdata.trainscore;
								grp_activity_score += backdata.activityscore;
							}
							resdata = new resultData();
							resdata.usr_ent_id = rs.getLong("usr_ent_id");
							resdata.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
							resdata.usr_display_bil = rs.getString("usr_display_bil");
							resdata.group_name = entityFullPath.getFullPath(con, rs.getLong("group_id"));
							resdata.grade_name = entityFullPath.getEntityName(con, rs.getLong("grade_id"));
						}
		
						if (CTY_COURSE.equalsIgnoreCase(type)) {
							if (act_ind > 0) {
								trainscore += score;
							} else {
								trainscore += score;
							}
						} else {
							if (act_ind > 0) {
								activityscore += score;
							} else {
								activityscore += score;
							}
						}
						back_ent_id = cur_ent_id;
					}
					if (resdata != null) {
						resultData backdatalast = resdata;// 保存最后一个data
						backdatalast.activityscore = activityscore;
						backdatalast.trainscore = trainscore;
		
						count++;
						backdatalast.totalRecode = 1;
						vtdata.add(backdatalast);
		
						// 累加用户组积分
						grp_train_score += backdatalast.trainscore;
						grp_activity_score += backdatalast.activityscore;
					}
				} else {
					resultData resdata = null;
					while (rs.next()) {
						cur_ent_id = rs.getLong("usr_ent_id");
		
						if (back_ent_id != cur_ent_id) {
							resultData backdata = resdata;// 保存前一个data
							if (back_ent_id != 0 && backdata != null) {// 第一条记录不保存
								backdata.activityscore = cwUtils.roundingFloat(activityscore,2);
								backdata.trainscore = cwUtils.roundingFloat(trainscore,2);
		
								grp_train_score += backdata.trainscore;
								grp_activity_score += backdata.activityscore;
								count++;
								backdata.totalRecode = 1;
								vtdata.add(backdata);
							}
							resdata = new resultData();
							resdata.usr_ent_id = cur_ent_id;
							resdata.usr_ste_usr_id = rs.getString("usr_ste_usr_id");
							resdata.usr_display_bil = rs.getString("usr_display_bil");
							resdata.group_name = entityFullPath.getFullPath(con, rs.getLong("group_id"));
							resdata.grade_name = entityFullPath.getEntityName(con, rs.getLong("grade_id"));
		
							activityscore = 0;
							trainscore = 0;
						}
						type = rs.getString("cty_relation_type");
						if (CTY_COURSE.equalsIgnoreCase(type)) {
							trainscore += rs.getFloat("ucd_total");
						} else {
							activityscore += rs.getFloat("ucd_total");
						}
		
						back_ent_id = cur_ent_id;
					}
					if (resdata != null) {
						resultData lastdata = resdata;// 保存最后一个data
						lastdata.activityscore = cwUtils.roundingFloat(activityscore, 2);;
						lastdata.trainscore = cwUtils.roundingFloat(trainscore, 2);
		
						grp_train_score += lastdata.trainscore;
						grp_activity_score += lastdata.activityscore;
						count++;
						lastdata.totalRecode = 1;
						vtdata.add(lastdata);
					}
				}
				dataByGroup groupdata = new dataByGroup();
				groupdata.group_name = entityFullPath.getEntityName(con, id);
				groupdata.usrcount = count;
				groupdata.activityscore = cwUtils.roundingFloat(grp_activity_score, 2);
				groupdata.trainscore = cwUtils.roundingFloat(grp_train_score, 2);
				groupdata.totalscore = groupdata.activityscore + groupdata.trainscore;
				groupdata.totalRecode = 1;
				vtgroupdata.add(groupdata);
				vt_data[0] = vtdata;
				vt_data[1] = vtgroupdata;
				hash.put(new Long(id), vt_data);
			} finally {
				if (vtId != null && vtId.size() > 0 &&vtId.size()>=1000) {
					cwSQL.delTempTable(con, tableName); 		//删除临时表中的所有记录
				}
				cwSQL.cleanUp(rs, stmt);
			}
		}
		if(tableName!=null){
			cwSQL.dropTempTable(con, tableName);				//删除临时表
		}
		return hash;
	}
 	/**
	 * Get manual bonus point as xml.
	 * @param deduction_ind  0: add point; 1: deduct point
	 * @return
	 */
	public String getManualBonusXMl(Connection con, boolean deduction_ind, long tcr_id,String sort_order,String... cty_set_type) throws SQLException {
		StringBuffer xml = new StringBuffer();
		
		xml.append("<sort sort_order=\"").append(sort_order).append("\" >");
		xml.append("</sort>");
		xml.append("<manual_cyt_lst deduction_ind=\"").append(deduction_ind).append("\" >");
		CreditsTypeDAO ctyDao = new CreditsTypeDAO();
		
		String cty_set_type_str = "";
		//如果为积分清空
		if (cty_set_type != null) {
			if(cty_set_type.length > 0){
				for (int i = 0; i < cty_set_type.length; i++) {
					cty_set_type_str = cty_set_type[i];
				}
			}
		}
		List cty_lst = null;
		if(cty_set_type_str != null && Credit.INTEGRAL_EMPTY.equals(cty_set_type_str)){
			cty_lst = ctyDao.getManualBonusList(con, deduction_ind, tcr_id,sort_order,true);
		}else{
			cty_lst = ctyDao.getManualBonusList(con, deduction_ind, tcr_id,sort_order);
		}
		Iterator it = cty_lst.iterator();
		while (it.hasNext()) {
			CreditsTypeBean creditsTypeBean = (CreditsTypeBean) it.next();
			xml.append("<manual_cyt id=\"" + creditsTypeBean.getCty_id() + "\">");
			xml.append("<code>").append(dbUtils.esc4XML(creditsTypeBean.getCty_code())).append("</code>");
			xml.append("</manual_cyt>");
		}
		xml.append("</manual_cyt_lst>");
		return xml.toString();
	}

	public void insManualPoint(Connection con, String usr_id, CreditModuleParam modParam, long tcr_id) throws SQLException {
		CreditsTypeDAO ctyDao = new CreditsTypeDAO();
		CreditsTypeBean ctyBean = new CreditsTypeBean();
        ctyBean.setCty_code(modParam.getCty_code());
        ctyBean.setCty_title(modParam.getCty_code());
        ctyBean.setCty_deduction_ind(modParam.getCty_deduction_ind());
        ctyBean.setCty_manual_ind(true);
        ctyBean.setCty_deleted_ind(false);
        ctyBean.setCty_relation_total_ind(true);
		ctyBean.setCty_default_credits_ind(false);
		ctyBean.setCty_default_credits(0);
        ctyBean.setCty_create_usr_id(usr_id);
        ctyBean.setCty_create_timestamp(modParam.getCur_time());
        ctyBean.setCty_update_usr_id(usr_id);
        ctyBean.setCty_update_timestamp(modParam.getCur_time());
        ctyBean.setCty_tcr_id(tcr_id);
        ctyDao.insManualPoint(con, ctyBean);
	}

	public void softDelManualPoint(Connection con, String usr_id, CreditModuleParam modParam) throws SQLException {
		CreditsTypeDAO ctyDao = new CreditsTypeDAO();
		CreditsTypeBean ctyBean = new CreditsTypeBean();
        ctyBean.setCty_id(modParam.getCty_id());
        ctyBean.setCty_deleted_ind(true);
        ctyBean.setCty_update_usr_id(usr_id);
        ctyBean.setCty_update_timestamp(modParam.getCur_time());
        ctyDao.softDelPoint(con, ctyBean);
	}

	// 手工设置学员积分
	public void setLrnjifen(Connection con, CreditModuleParam modParam, String usr_id) throws SQLException, cwException  {
		String usr_n_usg_id_lst = modParam.getUsr_n_usg_id_lst();
		long[] ent_id_lst = cwUtils.splitToLong(usr_n_usg_id_lst, "~");
		if(ent_id_lst == null || ent_id_lst.length == 0) return;
		//取用户
		Vector in_ent_ids = new Vector();
        for (int i = 0;i < ent_id_lst.length;i++) {
            in_ent_ids.addElement(new Long(ent_id_lst[i]));
        }
      //把用户组和用户区分，并且把用户组当中的用户拿去出来
        Vector usr_ent_id_lst = new Vector();
        usr_ent_id_lst = LearnerRptExporter.getUserEntId(con, usr_ent_id_lst, in_ent_ids);
		for(int i = 0; i < usr_ent_id_lst.size(); i++){
			long usr_ent_id = ((Long)usr_ent_id_lst.get(i)).longValue();
			if (usr_ent_id == 0) {
				continue;
			}

			CreditsTypeDAO creditsTypeDao = new CreditsTypeDAO();
			//获取积分的类型,如果是清空积分就使用INTEGRAL_EMPTY
			//modParam.getCty_code() 由积分清空页面 默认值 回传
			String cty_code = null;
			if(modParam.getCty_code()!=null && modParam.getCty_code().equals("INTEGRAL_EMPTY_DEL")){
				cty_code ="INTEGRAL_EMPTY";
			}else{
				cty_code = creditsTypeDao.getCode(con, modParam.getCty_id()); 
			}
			updUserCredits(con, cty_code, 0, (int)usr_ent_id, usr_id, modParam.getInput_point(), 0, 0, modParam.getCty_id());
		}
	}

	public void getCreditImpTpl(Connection con, String tplFileName, String tpl_path, String tempDir, String tempDirName, long usr_ent_id, String cur_lang, long tcr_id) throws IOException, cwException, SQLException {
		Workbook wbFile = null;
    	WritableWorkbook wwbFile = null;

    	File fIn = new File(tpl_path);
    	File fOut = new File(tempDir + cwUtils.SLASH + usr_ent_id);
    	if (!fOut.exists()) {
    		fOut.mkdirs();
    	}
    	try {
        	wbFile = Workbook.getWorkbook(fIn);
        	wwbFile = Workbook.createWorkbook(new File(fOut, tplFileName), wbFile);
        	WritableSheet sheet = wwbFile.createSheet(LangLabel.getValue(cur_lang, "842"), 1);
        	CreditsTypeDAO ctyDao = new CreditsTypeDAO();
        	List pointList = ctyDao.getManualBonusList(con, false,tcr_id,"asc");
        	int i = 0;
        	CreditsTypeBean creditsTypeBean = null;
        	for (i = 0; i < pointList.size(); i++) {
        		creditsTypeBean = (CreditsTypeBean)pointList.get(i);
        		setCellContent(sheet, creditsTypeBean.getCty_code(), (short)i);
        	}

        	sheet = wwbFile.createSheet(LangLabel.getValue(cur_lang, "843"), 2);
        	pointList = ctyDao.getManualBonusList(con, true,tcr_id,"asc");
        	for (i = 0; i < pointList.size(); i++) {
        		creditsTypeBean = (CreditsTypeBean)pointList.get(i);
        		setCellContent(sheet, creditsTypeBean.getCty_code(), (short)i);
        	}
        	wwbFile.write();
		} catch (BiffException e) {
			throw new cwException(e.getMessage());
		} catch (WriteException e) {
			throw new cwException(e.getMessage());
		} finally {
			try {
				wwbFile.close();
			} catch (WriteException e) {
				throw new cwException(e.getMessage());
			}
        	wbFile.close();
    	}
	}

	private void setCellContent(WritableSheet sheet, String content, short rowNum) throws WriteException {
        Label label = new Label(0, rowNum, content);
        sheet.addCell(label);
    }

	public Map upLoadBonusPoint(Connection con, String sourceFile, int max_row, String usr_id, long adm_ent_id, long site_id)
		    throws SQLException, cwSysMessage, cwException {
		Map result = new HashMap();
		StringBuffer result_xml = new StringBuffer();
		int succ_count = 0;
		Workbook w = null;
		try {
		    int line_col = 4;
		    List fail_line = new ArrayList();
		    File inputWorkbook = new File(sourceFile);
		    w = Workbook.getWorkbook(inputWorkbook);
		    Sheet sheet = w.getSheet(0);;

		    long usr_ent_id ;
		    int cty_id;
		    float change_point;

		    String acttype = "";
		    boolean cty_deduction_ind = false;
		    boolean isChargeUser = false;
		    String cty_code = null;
		    if (sheet != null) {
		        int row = sheet.getRows();
		        if(row > max_row && max_row >0){
		            throw new cwSysMessage(UploadUtils.SMSG_ULG_UPLOAD_COUNT_EXCEED_LIMIT, new Integer(max_row).toString());
		        }

		        CreditsTypeDAO ctDao = new CreditsTypeDAO();
		        for (int i = 1; i < row; i++) {
		            Cell[] cell = sheet.getRow(i);
		            boolean is_null_line = true;
		            for (int colidx = 0; colidx < cell.length; colidx++) {
		                String temp_value = cell[colidx].getContents();
		                if(temp_value != null && temp_value.length() > 0){
		                    is_null_line = false;
		                    break;
		                }
		            }
		            if(!is_null_line){
		                if(cell.length < line_col){
		                    fail_line.add(cell);
		                    continue;
		                }
		                 // get user login id
		                if(cell[0].getContents()== null ||cell[0].getContents().trim().length() == 0){
		                    fail_line.add(cell);
		                    continue;
		                }
		                usr_ent_id = dbRegUser.getEntIdStatusOk( con, cell[0].getContents().trim(), site_id);
		                if(usr_ent_id == 0){
		                    fail_line.add(cell);
		                    continue;
		                }

		                //判断导入的学员是否该培训管理员所负责的学员
		                isChargeUser = ViewEntityRelation.isChargeUser(con, adm_ent_id, usr_ent_id);
		                if(!isChargeUser){
		                    fail_line.add(cell);
		                    continue;
		                }

		                if(cell[1].getContents() == null ||cell[1].getContents().trim().length() == 0){
		                    fail_line.add(cell);
		                    continue;
		                }
		                acttype = cell[1].getContents().trim();
		                if (!(acttype.equals(AddTYPE)||acttype.equals(DELTYPE))){
		                    fail_line.add(cell);
		                    continue;
		                }else if(acttype.equals(DELTYPE)){
		                	cty_deduction_ind = true;
		                } else {
		                	cty_deduction_ind = false;
		                }
		                //get bonustitle
		                if(cell[2].getContents()== null ||cell[2].getContents().trim().length() == 0){
		                    fail_line.add(cell);
		                    continue;
		                }

		                cty_code = cell[2].getContents().trim();
		                cty_id = ctDao.getManualIdByCode(con, cty_code, cty_deduction_ind);
		                if(cty_id == 0){
		                    fail_line.add(cell);
		                    continue;
		                }

		                //get point
		                if(cell[3].getContents()== null ||cell[3].getContents().trim().length() == 0){
		                    fail_line.add(cell);
		                    continue;
		                }
		                try{
		                    change_point = Float.parseFloat(cell[3].getContents().trim());
		                }catch(Exception e) {
		                    change_point = 0;
		                }
		                if(change_point <= 0){
		                    fail_line.add(cell);
		                    continue;
		                }
		                // set bonus
		                try{
		                	updUserCredits(con, cty_code, 0, (int)usr_ent_id, usr_id, change_point, 0, 0, 0);
		                    succ_count ++;
		                    con.commit();
		                }catch(Exception e) {
		                    fail_line.add(cell);
		                    con.rollback();
		                    continue;
		                }
		            }
		        }
		        result_xml.append("<succeed_count>").append(succ_count).append("</succeed_count>").append(cwUtils.NEWL);
		        result_xml.append("<fail_list count = \"").append(fail_line.size()).append("\">");
		        Cell cell[] =null;
		        for(int i = 0; i < fail_line.size(); i++){
		            cell = (Cell[])fail_line.get(i);
		            result_xml.append("<record>");
		            if(cell.length >0 && cell[0].getContents()!= null){
		                result_xml.append("<usr_id>").append(dbUtils.esc4XML(cell[0].getContents())).append("</usr_id>").append(cwUtils.NEWL);
		            }
		            if(cell.length >1 && cell[1].getContents()!= null){
		                result_xml.append("<usr_name>").append(dbUtils.esc4XML(cell[1].getContents())).append("</usr_name>").append(cwUtils.NEWL);
		            }
		            if(cell.length >2 && cell[2].getContents()!= null){
		                result_xml.append("<bonus_name>").append(dbUtils.esc4XML(cell[2].getContents())).append("</bonus_name>").append(cwUtils.NEWL);
		            }
		            if(cell.length >3 && cell[3].getContents()!= null){
		                result_xml.append("<score>").append(dbUtils.esc4XML(cell[3].getContents())).append("</score>").append(cwUtils.NEWL);
		            }
		            result_xml.append("</record>");
		        }
		        result_xml.append("</fail_list>");
		    } else{
		        throw new cwSysMessage("GEN009");
		    }

		    result.put("xml", result_xml.toString());
		    result.put("succ_count", new Integer(succ_count));
		    return result;
		} catch (BiffException e) {
		    throw new cwSysMessage("GEN009");
		} catch (IOException e) {
		    throw new cwException("read file error:" + e.getMessage());
		} catch (qdbException e) {
		    throw new cwException(e.getMessage());
		} finally {
			if (w != null) {
				w.close();
			}
		}
	}

	//设置课程积分查询
	public String getAttendUser(Connection con, CreditModuleParam modParam) throws SQLException {
		StringBuffer xml = new StringBuffer();
        Vector rec_vec = new Vector();
        int recTotal = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		cwPagination page = modParam.getCwPage();
        if (page == null) {
            page = new cwPagination();
            page.curPage = 1;
            page.pageSize = 50;
        }
		String sql ="select usr_ent_id, usr_ste_usr_id, usr_display_bil, app_id ";
		if(modParam.getUcd_itm_status().equalsIgnoreCase("NO")){//如果课程未发布
			sql = sql + " ,NULL as ucd_total ";
		}else{
			sql = sql + " ,ucd_total ";
		}
		sql = sql +" from aeApplication"
			+" inner join aeAttendance on(att_app_id = app_id and att_itm_id =?) "
			+" inner join RegUser on (usr_ent_id = app_ent_id ) ";
		if(modParam.getUcd_itm_status().equalsIgnoreCase("ALL")){
			sql = sql + " left join userCreditsDetail on (app_id = ucd_app_id and ucd_cty_id in(SELECT cty_id FROM creditsType WHERE cty_code = ? ))";
		}else if(modParam.getUcd_itm_status().equalsIgnoreCase("YES")){
			sql = sql + " inner join userCreditsDetail on (app_id = ucd_app_id and ucd_cty_id  in(SELECT cty_id FROM creditsType WHERE cty_code = ? ) )";
		}
		sql = sql+" where app_id in (select max(app_id) from aeApplication, aeAttendance where att_app_id = app_id and app_itm_id = ? group by app_ent_id)  and att_ats_id = ? ";
		if(modParam.getUsr_steid_or_diplaybil()!=null && modParam.getUsr_steid_or_diplaybil().length()>0){
			sql = sql + " and (lower(usr_ste_usr_id) like ? or lower(usr_display_bil) like ?) ";
		}
		if(modParam.getUcd_itm_status().equalsIgnoreCase("NO")){
			sql = sql + " and app_id not in (select ucd_app_id from userCreditsDetail where ucd_itm_id =? and ucd_cty_id in(SELECT cty_id FROM creditsType WHERE cty_code = ? )) ";
		}
		try {
			stmt = con.prepareStatement(sql);
			int index = 1;
			stmt.setLong(index++, modParam.getItm_id());
			if(!modParam.getUcd_itm_status().equalsIgnoreCase("NO")){
				stmt.setString(index++, Credit.ITM_IMPORT_CREDIT);
			}
			stmt.setLong(index++, modParam.getItm_id());
			stmt.setLong(index++, 1);//只查找已完成的学员
			if(modParam.getUsr_steid_or_diplaybil()!=null && modParam.getUsr_steid_or_diplaybil().length()>0){
				stmt.setString(index++, '%' + modParam.getUsr_steid_or_diplaybil().toLowerCase() + '%');
                stmt.setString(index++, '%' + modParam.getUsr_steid_or_diplaybil().toLowerCase() + '%');
			}
			if(modParam.getUcd_itm_status().equalsIgnoreCase("NO")){
				stmt.setLong(index++, modParam.getItm_id());
				stmt.setString(index++, Credit.ITM_IMPORT_CREDIT);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				 recTotal++;
				 //if (recTotal >=modParam.getStart()+1 && recTotal <= modParam.getStart() + page.pageSize) {
					Vector rec = new Vector();
					rec.addElement(new Long(rs.getLong("usr_ent_id")));
					rec.addElement(rs.getString("usr_ste_usr_id"));
					rec.addElement(rs.getString("usr_display_bil"));
					rec.addElement(new Long(rs.getLong("app_id")));
					rec.addElement(new Float(rs.getFloat("ucd_total")));
					rec_vec.add(rec);
				 //}
			}
		}finally {
			cwSQL.cleanUp(rs, stmt);
		}

        page.totalRec = recTotal;
		page.totalPage = page.totalRec / page.pageSize;
		if (page.totalRec % page.pageSize != 0) {
			page.totalPage++;
		}
		xml.append("<user_lst>");
		for (int i=0; i<rec_vec.size(); i++) {
			Vector rec = (Vector)rec_vec.elementAt(i);
			StringBuffer usrStr = new StringBuffer();
			usrStr.append("<user id=\"").append(((Long)rec.get(0)).longValue()).append("\"");
			usrStr.append(" usr_id=\"").append(cwUtils.esc4XML((String)rec.get(1))).append("\"");
			usrStr.append(" display_bil=\"").append(cwUtils.esc4XML((String)rec.get(2))).append("\"");
			usrStr.append(" app_id=\"").append(((Long)rec.get(3)).longValue()).append("\">");
			Float score  = ((Float)rec.get(4)).floatValue();
		    if(score != 0){
		    	usrStr.append("<itm_manual_credit>");
		    	usrStr.append(score);
			    usrStr.append("</itm_manual_credit>");
		    }
			usrStr.append("</user>");
			xml.append(usrStr);
		}
		xml.append("</user_lst>");
		xml.append("<item id=\"").append(modParam.getItm_id()).append("\">");
		xml.append("</item>");
		xml.append(page.asXML());
		return xml.toString();
	}

	//设置学员课程积分
	public void setAttendUserCredit(Connection con, CreditModuleParam modParam, String usr_id) throws SQLException, cwException {
		long[] app_id_list = cwUtils.splitToLong(modParam.getSel_app_id_list(), "~");//报名ID
		long[] usr_ent_id_list = cwUtils.splitToLong(modParam.getSel_usr_ent_id_list(), "~");//用户ID
		long itm_id = modParam.getItm_id();//课程ID
		float score =  modParam.getInput_point();//设置的分数
		String cty_code = ITM_IMPORT_CREDIT;//设置分数的类型
		for(int i = 0; i < app_id_list.length; i++){//循环报名ID
			long usr_ent_id = usr_ent_id_list[i];
			long app_id = app_id_list[i];
	        updUserCredits(con, cty_code, itm_id, (int)usr_ent_id, usr_id, score, app_id, 0,0);//更新用户积分
		}
	}
	


}
