package com.cwn.wizbank.scheduled;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.cw.wizbank.ScheduledTask;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.message.MessageScheduler;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwSQL;
import com.cwn.wizbank.entity.LearningSituation;
import com.cwn.wizbank.services.SystemStatisticsService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.web.WzbApplicationContext;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class LearningSituationScheduled extends ScheduledTask implements Job{

	//学员角色ID，用于仅统计学员的学分
	public static final long LEARNER_ROL_ID = 5;
	public static final String COV_STATUS = "C";
	public static final String APP_STATUS = "Admitted";
	
	public LearningSituationScheduled(){
		logger = Logger.getLogger(MessageScheduler.class);
		static_env = qdbAction.static_env;
	}

	 public void init() {

	 }
	// 统计个人学习根况
    protected void process() {
        try {
			wizbini = WizbiniLoader.getInstance();
			dbSource = new cwSQL();
			dbSource.setParam(wizbini);
            con = dbSource.openDB(false);
            updateLearningSituation(con);
            
            con.commit();
        } catch (Exception e) {
            logger.debug("LearningSituationScheduled.process() error", e);
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
                    logger.debug("LearningSituationScheduled.process() error", e);
                    CommonLog.error(e.getMessage(),e);
                }
            }
        }
    }
    
    public void updateLearningSituation(Connection con) throws SQLException{
    	Map<Long, LearningSituation> map = new HashMap<Long, LearningSituation>();
    	//获取个人概况总览
    	getLearnSituation(con, map);
    	//获取个人课程学习概况
    	getAeItemSituation(con, map, false);
    	//获取个人考试概况
    	getAeItemSituation(con, map, true);
    	//获取个人关注概况
    	getAttentionSituation(con, map);
    	//获取个人赞概况
    	getPraiseSituation(con, map);
    	//获取个人收藏和分享概况
    	getCollectAndShareSituation(con, map);
    	//获取个人群组概况
    	getGroupSituation(con, map);
    	//获取个人问答概况
    	getKnowSituation(con, map);
    	
    	//获取第一个回答我问题的人
    	getKnowSituationFirs( con, map); 
    	
    	//获取个人知识分享及浏览概况
    	getKnowledgeSituation(con, map);
    	for(Entry<Long,LearningSituation> learningSituations : map.entrySet()){
    		if(checkLearningSituationInd(learningSituations.getKey())){
    			updateLearningSituation(learningSituations.getValue());
    		} else {
    			insertLearningSituation(learningSituations.getValue());
    		}
    	}
    }
    
    public boolean checkLearningSituationInd(long ls_ent_id) throws SQLException{
    	boolean ls_ind = false;
    	String sql = " select * from LearningSituation where ls_ent_id = ? ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	stmt.setLong(1, ls_ent_id);
        	rs = stmt.executeQuery();
        	if(rs.next()){
        		ls_ind = true;
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
        return ls_ind;
    }
    
    public void insertLearningSituation(LearningSituation learningSituation) throws SQLException{
    	String sql = " insert into LearningSituation(ls_ent_id, ls_learn_duration, ls_learn_credit, ls_total_integral, "
    			+ " ls_total_courses, ls_course_completed_num, ls_course_fail_num, ls_course_inprogress_num, ls_course_pending_num, "
    			+ " ls_total_exams, ls_exam_completed_num, ls_exam_fail_num, ls_exam_inprogress_num, ls_exam_pending_num, ls_fans_num, "
    			+ " ls_attention_num, ls_praised_num, ls_praise_others_num, ls_collect_num, ls_share_num, ls_create_group_num, "
    			+ " ls_join_group_num, ls_group_speech_num, ls_question_num, ls_answer_num, ls_share_count, ls_access_count, ls_update_time, "
    			+ " Is_signup_date, Is_first_course_date, Is_first_praised_date, Is_first_fans_usr, Is_first_que_date, Is_first_fans_date, "
    			+ " Is_first_helper_usr, Is_first_share_date, Is_first_praised_usr) "
    			+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    	PreparedStatement stmt = null;
    	Timestamp curTime = cwSQL.getTime(con);
    	int index = 1;
    	try{
	    	stmt = con.prepareStatement(sql);
	    	stmt.setLong(index++, learningSituation.getLs_ent_id());
	    	stmt.setLong(index++, learningSituation.getLs_learn_duration() == null? 0 : learningSituation.getLs_learn_duration());
	    	stmt.setFloat(index++, learningSituation.getLs_learn_credit() == null? 0 : learningSituation.getLs_learn_credit());
	    	stmt.setLong(index++, learningSituation.getLs_total_integral() == null? 0 : learningSituation.getLs_total_integral());
	    	stmt.setLong(index++, learningSituation.getLs_total_courses() == null? 0 : learningSituation.getLs_total_courses());
	    	stmt.setLong(index++, learningSituation.getLs_course_completed_num() == null? 0 : learningSituation.getLs_course_completed_num());
	    	stmt.setLong(index++, learningSituation.getLs_course_fail_num() == null? 0 : learningSituation.getLs_course_fail_num());
	    	stmt.setLong(index++, learningSituation.getLs_course_inprogress_num() == null? 0 : learningSituation.getLs_course_inprogress_num());
	    	stmt.setLong(index++, learningSituation.getLs_course_pending_num() == null? 0 : learningSituation.getLs_course_pending_num());
	    	stmt.setLong(index++, learningSituation.getLs_total_exams() == null? 0 : learningSituation.getLs_total_exams());
	    	stmt.setLong(index++, learningSituation.getLs_exam_completed_num() == null? 0 : learningSituation.getLs_exam_completed_num());
	    	stmt.setLong(index++, learningSituation.getLs_exam_fail_num() == null? 0 : learningSituation.getLs_exam_fail_num());
	    	stmt.setLong(index++, learningSituation.getLs_exam_inprogress_num() == null? 0 : learningSituation.getLs_exam_inprogress_num());
	    	stmt.setLong(index++, learningSituation.getLs_exam_pending_num() == null? 0 : learningSituation.getLs_exam_pending_num());
	    	stmt.setLong(index++, learningSituation.getLs_fans_num() == null? 0 : learningSituation.getLs_fans_num());
	    	stmt.setLong(index++, learningSituation.getLs_attention_num() == null? 0 : learningSituation.getLs_attention_num());
	    	stmt.setLong(index++, learningSituation.getLs_praised_num() == null? 0 : learningSituation.getLs_praised_num());
	    	stmt.setLong(index++, learningSituation.getLs_praise_others_num() == null? 0 : learningSituation.getLs_praise_others_num());
	    	stmt.setLong(index++, learningSituation.getLs_collect_num() == null? 0 : learningSituation.getLs_collect_num());
	    	stmt.setLong(index++, learningSituation.getLs_share_num() == null? 0 : learningSituation.getLs_share_num());
	    	stmt.setLong(index++, learningSituation.getLs_create_group_num() == null? 0 : learningSituation.getLs_create_group_num());
	    	stmt.setLong(index++, learningSituation.getLs_join_group_num() == null? 0 : learningSituation.getLs_join_group_num());
	    	stmt.setLong(index++, learningSituation.getLs_group_speech_num() == null? 0 : learningSituation.getLs_group_speech_num());
	    	stmt.setLong(index++, learningSituation.getLs_question_num() == null? 0 : learningSituation.getLs_question_num());
	    	stmt.setLong(index++, learningSituation.getLs_answer_num() == null? 0 : learningSituation.getLs_answer_num());
	    	stmt.setLong(index++, learningSituation.getLs_share_count() == null? 0 : learningSituation.getLs_share_count());
	    	stmt.setLong(index++, learningSituation.getLs_access_count() == null? 0 : learningSituation.getLs_access_count());
	    	stmt.setTimestamp(index++, curTime);
	    	stmt.setTimestamp(index++, learningSituation.getIs_signup_date());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_course_date());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_praised_date());
	    	stmt.setString(index++, learningSituation.getIs_first_fans_usr());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_que_date());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_fans_date());
	    	stmt.setString(index++, learningSituation.getIs_first_helper_usr());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_share_date());
	    	stmt.setString(index++, learningSituation.getIs_first_praised_usr());
	    	
	    	stmt.executeUpdate();
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}
    }
    
    public void updateLearningSituation(LearningSituation learningSituation) throws SQLException{
    	String sql = " update LearningSituation set ls_learn_duration = ?, ls_learn_credit = ?, ls_total_integral = ?, ls_total_courses = ?,"
    			+ " ls_course_completed_num = ?, ls_course_fail_num = ?, ls_course_inprogress_num = ?, ls_course_pending_num = ?, ls_total_exams = ?, "
    			+ " ls_exam_completed_num = ?, ls_exam_fail_num = ?, ls_exam_inprogress_num = ?, ls_exam_pending_num = ?, ls_fans_num = ?, "
    			+ " ls_attention_num = ?, ls_praised_num = ?, ls_praise_others_num = ?, ls_collect_num = ?, ls_share_num = ?, ls_create_group_num = ?, "
    			+ " ls_join_group_num = ?, ls_group_speech_num = ?, ls_question_num = ?, ls_answer_num = ?, ls_share_count = ?, ls_access_count = ?, "
    			+ " ls_update_time = ? , Is_signup_date = ?,Is_first_course_date = ? , Is_first_praised_date = ? , Is_first_fans_usr = ?, "
    			+ " Is_first_que_date = ? , Is_first_fans_date = ? ,Is_first_helper_usr = ? ,Is_first_share_date = ? , Is_first_praised_usr = ? "
    			+ " where ls_ent_id = ? ";
    	PreparedStatement stmt = null;
    	Timestamp curTime = cwSQL.getTime(con);
    	int index = 1;
    	try{
	    	stmt = con.prepareStatement(sql);
	    	stmt.setLong(index++, learningSituation.getLs_learn_duration() == null? 0 : learningSituation.getLs_learn_duration());
	    	stmt.setFloat(index++, learningSituation.getLs_learn_credit() == null? 0 : learningSituation.getLs_learn_credit());
	    	stmt.setLong(index++, learningSituation.getLs_total_integral() == null? 0 : learningSituation.getLs_total_integral());
	    	stmt.setLong(index++, learningSituation.getLs_total_courses() == null? 0 : learningSituation.getLs_total_courses());
	    	stmt.setLong(index++, learningSituation.getLs_course_completed_num() == null? 0 : learningSituation.getLs_course_completed_num());
	    	stmt.setLong(index++, learningSituation.getLs_course_fail_num() == null? 0 : learningSituation.getLs_course_fail_num());
	    	stmt.setLong(index++, learningSituation.getLs_course_inprogress_num() == null? 0 : learningSituation.getLs_course_inprogress_num());
	    	stmt.setLong(index++, learningSituation.getLs_course_pending_num() == null? 0 : learningSituation.getLs_course_pending_num());
	    	stmt.setLong(index++, learningSituation.getLs_total_exams() == null? 0 : learningSituation.getLs_total_exams());
	    	stmt.setLong(index++, learningSituation.getLs_exam_completed_num() == null? 0 : learningSituation.getLs_exam_completed_num());
	    	stmt.setLong(index++, learningSituation.getLs_exam_fail_num() == null? 0 : learningSituation.getLs_exam_fail_num());
	    	stmt.setLong(index++, learningSituation.getLs_exam_inprogress_num() == null? 0 : learningSituation.getLs_exam_inprogress_num());
	    	stmt.setLong(index++, learningSituation.getLs_exam_pending_num() == null? 0 : learningSituation.getLs_exam_pending_num());
	    	stmt.setLong(index++, learningSituation.getLs_fans_num() == null? 0 : learningSituation.getLs_fans_num());
	    	stmt.setLong(index++, learningSituation.getLs_attention_num() == null? 0 : learningSituation.getLs_attention_num());
	    	stmt.setLong(index++, learningSituation.getLs_praised_num() == null? 0 : learningSituation.getLs_praised_num());
	    	stmt.setLong(index++, learningSituation.getLs_praise_others_num() == null? 0 : learningSituation.getLs_praise_others_num());
	    	stmt.setLong(index++, learningSituation.getLs_collect_num() == null? 0 : learningSituation.getLs_collect_num());
	    	stmt.setLong(index++, learningSituation.getLs_share_num() == null? 0 : learningSituation.getLs_share_num());
	    	stmt.setLong(index++, learningSituation.getLs_create_group_num() == null? 0 : learningSituation.getLs_create_group_num());
	    	stmt.setLong(index++, learningSituation.getLs_join_group_num() == null? 0 : learningSituation.getLs_join_group_num());
	    	stmt.setLong(index++, learningSituation.getLs_group_speech_num() == null? 0 : learningSituation.getLs_group_speech_num());
	    	stmt.setLong(index++, learningSituation.getLs_question_num() == null? 0 : learningSituation.getLs_question_num());
	    	stmt.setLong(index++, learningSituation.getLs_answer_num() == null? 0 : learningSituation.getLs_answer_num());
	    	stmt.setLong(index++, learningSituation.getLs_share_count() == null? 0 : learningSituation.getLs_share_count());
	    	stmt.setLong(index++, learningSituation.getLs_access_count() == null? 0 : learningSituation.getLs_access_count());
	    	stmt.setTimestamp(index++, curTime);
	    	stmt.setTimestamp(index++, learningSituation.getIs_signup_date());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_course_date());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_praised_date());
	    	stmt.setString(index++, learningSituation.getIs_first_fans_usr());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_que_date());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_fans_date());
	    	stmt.setString(index++, learningSituation.getIs_first_helper_usr());
	    	stmt.setTimestamp(index++, learningSituation.getIs_first_share_date());
	    	stmt.setString(index++, learningSituation.getIs_first_praised_usr());
	    	
	    	stmt.setLong(index++, learningSituation.getLs_ent_id());
	    	stmt.executeUpdate();
    	} finally {
    		cwSQL.closePreparedStatement(stmt);
    	}
    }
    
    public void getLearnSituation(Connection con, Map<Long, LearningSituation> map) throws SQLException{
    	String sql = " select usr_ent_id, sum(cov_total_time) ls_learn_duration, sum(case when erl_rol_id = ? then ies_credit else 0 end) ls_learn_credit, uct_total,usr_signup_date "
    			+ " from regUser "
    			+ " inner join acEntityRole on erl_ent_id = usr_ent_id "
    			+ " inner join acRole on rol_id = erl_rol_id "
    			+ " left join aeApplication on app_ent_id = usr_ent_id "
    			+ " left join CourseEvaluation on cov_tkh_id = app_tkh_id and cov_tkh_id in "
    			+ " (select MAX(cov_tkh_id) from CourseEvaluation where cov_status = ? group by cov_cos_id,cov_ent_id) "
    			+ " left join aeItem on itm_id = app_itm_id "
    			+ " left join aeItemExtension on ies_itm_id = itm_id and cov_status = ? and app_status = ? "
    			+ " left join userCredits on uct_ent_id = usr_ent_id "
    			+ " group by usr_ent_id,usr_signup_date, uct_total order by usr_ent_id ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        int index = 1;
        try{
        	stmt = con.prepareStatement(sql);
        	stmt.setLong(index++, LEARNER_ROL_ID);
        	stmt.setString(index++, COV_STATUS);
        	stmt.setString(index++, COV_STATUS);
        	stmt.setString(index++, APP_STATUS);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		LearningSituation ls = map.get(rs.getLong("usr_ent_id"));
        		if(ls == null){
        			ls = new LearningSituation();
        			ls.setLs_ent_id(rs.getLong("usr_ent_id"));
            		ls.setLs_learn_duration(rs.getLong("ls_learn_duration"));
            		ls.setLs_learn_credit(rs.getFloat("ls_learn_credit"));
            		ls.setLs_total_integral(rs.getLong("uct_total"));
            		//用户注册时间
            		ls.setIs_signup_date(rs.getTimestamp("usr_signup_date"));
            		map.put(rs.getLong("usr_ent_id"), ls);
        		} else {
            		ls.setLs_learn_duration(rs.getLong("ls_learn_duration"));
            		ls.setLs_learn_credit(rs.getFloat("ls_learn_credit"));
            		ls.setLs_total_integral(rs.getLong("uct_total"));
            		//用户注册时间
            		ls.setIs_signup_date(rs.getTimestamp("usr_signup_date"));
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    public void getAeItemSituation(Connection con, Map<Long, LearningSituation> map, boolean itm_exam_ind) throws SQLException{
    	String sql = " select allApp.app_ent_id, count(itm_id) total_num, count(distinct C_course.cov_tkh_id) completed_num, "
				+ " count(distinct F_course.cov_tkh_id) fail_num, count(distinct I_course.cov_tkh_id) inprogress_num, count(distinct Pending.app_id) pending_num , min(allApp.app_create_timestamp) first_app_datetime"
				+ " from aeApplication allApp left join aeItem on itm_id = allApp.app_itm_id  "
				+ " left join CourseEvaluation C_course on C_course.cov_tkh_id = allApp.app_tkh_id and C_course.cov_status = 'C' "
				+ " left join CourseEvaluation F_course on F_course.cov_tkh_id = allApp.app_tkh_id and (F_course.cov_status = 'F' or F_course.cov_status = 'W') "
				+ " left join CourseEvaluation I_course on I_course.cov_tkh_id = allApp.app_tkh_id and I_course.cov_status = 'I' "
				+ " left join aeApplication Pending on Pending.app_id = allApp.app_id and (Pending.app_status = 'Pending' or Pending.app_status = 'Waiting')"
				+ " where itm_exam_ind = ? and allApp.app_Status != 'Withdrawn' group by allApp.app_ent_id order by allApp.app_ent_id ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	stmt.setBoolean(1, itm_exam_ind);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		LearningSituation ls = map.get(rs.getLong("app_ent_id"));
        		if(ls == null){
        			ls = new LearningSituation();
        			if(itm_exam_ind){
	        			ls.setLs_ent_id(rs.getLong("app_ent_id"));
	            		ls.setLs_total_exams(rs.getLong("total_num"));
	            		ls.setLs_exam_completed_num(rs.getLong("completed_num"));
	            		ls.setLs_exam_fail_num(rs.getLong("fail_num"));
	            		ls.setLs_exam_inprogress_num(rs.getLong("inprogress_num"));
	            		ls.setLs_exam_pending_num(rs.getLong("pending_num"));
        			} else {
        				ls.setLs_ent_id(rs.getLong("app_ent_id"));
	            		ls.setLs_total_courses(rs.getLong("total_num"));
	            		ls.setLs_course_completed_num(rs.getLong("completed_num"));
	            		ls.setLs_course_fail_num(rs.getLong("fail_num"));
	            		ls.setLs_course_inprogress_num(rs.getLong("inprogress_num"));
	            		ls.setLs_course_pending_num(rs.getLong("pending_num"));
	            		// 第一次报名时间
	            		ls.setIs_first_course_date(rs.getTimestamp("first_app_datetime"));
        			}
            		map.put(rs.getLong("app_ent_id"), ls);
        		} else {
        			if(itm_exam_ind){
	            		ls.setLs_total_exams(rs.getLong("total_num"));
	            		ls.setLs_exam_completed_num(rs.getLong("completed_num"));
	            		ls.setLs_exam_fail_num(rs.getLong("fail_num"));
	            		ls.setLs_exam_inprogress_num(rs.getLong("inprogress_num"));
	            		ls.setLs_exam_pending_num(rs.getLong("pending_num"));
        			} else {
	            		ls.setLs_total_courses(rs.getLong("total_num"));
	            		ls.setLs_course_completed_num(rs.getLong("completed_num"));
	            		ls.setLs_course_fail_num(rs.getLong("fail_num"));
	            		ls.setLs_course_inprogress_num(rs.getLong("inprogress_num"));
	            		ls.setLs_course_pending_num(rs.getLong("pending_num"));
	            		// 第一次报名时间
	            		ls.setIs_first_course_date(rs.getTimestamp("first_app_datetime"));
        			}
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    public void getGroupSituation(Connection con, Map<Long, LearningSituation> map) throws SQLException{
    	String sql = " select usr_ent_id, count(distinct s_grp_id) ls_create_group_num, count(distinct s_gpm_id) ls_join_group_num, "
    			+ " count(distinct s_doi_id) + count(distinct s_cmt_id) ls_group_speech_num from regUser "
    			+ " inner join acEntityRole on erl_ent_id = usr_ent_id "
    			+ " inner join acRole on rol_id = erl_rol_id "
    			+ " left join sns_group on s_grp_create_uid = usr_ent_id "
    			+ " left join ( "
				+ "		select s_gpm_id, s_gpm_usr_id from sns_group_member "
				+ "	  	left join sns_group on s_gpm_grp_id = s_grp_id "
				+ "	  	where s_gpm_status = 1 and s_grp_status = 'OK' "
				+ "	) t on s_gpm_usr_id = usr_ent_id "
    			+ " left join sns_doing on s_doi_uid = usr_ent_id and Lower(s_doi_act) = 'group' "
    			+ " left join sns_comment on s_cmt_uid = usr_ent_id and s_cmt_module = 'Group' "
    			+ " group by usr_ent_id order by usr_ent_id ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		LearningSituation ls = map.get(rs.getLong("usr_ent_id"));
        		if(ls == null){
        			ls = new LearningSituation();
        			ls.setLs_ent_id(rs.getLong("usr_ent_id"));
            		ls.setLs_create_group_num(rs.getLong("ls_create_group_num"));
            		ls.setLs_join_group_num(rs.getLong("ls_join_group_num"));
            		ls.setLs_group_speech_num(rs.getLong("ls_group_speech_num"));
            		map.put(rs.getLong("usr_ent_id"), ls);
        		} else {
        			ls.setLs_create_group_num(rs.getLong("ls_create_group_num"));
            		ls.setLs_join_group_num(rs.getLong("ls_join_group_num"));
            		ls.setLs_group_speech_num(rs.getLong("ls_group_speech_num"));
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    /*
     * 问答数据统计
     */
    public void getKnowSituation(Connection con, Map<Long, LearningSituation> map) throws SQLException{
    	String sql = " select usr_ent_id, count(distinct que_id) ls_question_num, min(que_create_timestamp) first_share_datetime, count(distinct ans_id) ls_answer_num from regUser "
    			+ " inner join acEntityRole on erl_ent_id = usr_ent_id "
    			+ " inner join acRole on rol_id = erl_rol_id "
    			+ " left join knowQuestion on que_create_ent_id = usr_ent_id and que_status = 'OK' "
    			+ " left join knowAnswer on ans_create_ent_id = usr_ent_id and ans_status = 'OK' "
    			+ " group by usr_ent_id order by usr_ent_id ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		LearningSituation ls = map.get(rs.getLong("usr_ent_id"));
        		if(ls == null){
        			ls = new LearningSituation();
        			ls.setLs_ent_id(rs.getLong("usr_ent_id"));
            		ls.setLs_question_num(rs.getLong("ls_question_num"));
            		ls.setLs_answer_num(rs.getLong("ls_answer_num"));
            		//取到第一次提问题的时间
            		ls.setIs_first_que_date(rs.getTimestamp("first_share_datetime"));
            		map.put(rs.getLong("usr_ent_id"), ls);
        		} else {
        			ls.setLs_question_num(rs.getLong("ls_question_num"));
            		ls.setLs_answer_num(rs.getLong("ls_answer_num"));
            		//取到第一次提问题的时间
            		ls.setIs_first_que_date(rs.getTimestamp("first_share_datetime"));
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    /*
     * 取到第一次回答我问题的人
     */
    public void getKnowSituationFirs(Connection con, Map<Long, LearningSituation> map) throws SQLException{
    	String sql = 
    		" select u.usr_ent_id, rg.usr_display_bil as ans_create_usr from knowAnswer inner join ( "
    		   + " select usr_ent_id, min(ans_id) min_and_id from regUser "
    			+ " inner join acEntityRole on erl_ent_id = usr_ent_id "
    			+ " inner join acRole on rol_id = erl_rol_id "
    			+ " inner join knowQuestion on que_create_ent_id = usr_ent_id and que_status = 'OK' "
    			+ " inner join knowAnswer on que_id = ans_que_id and ans_status = 'OK' "
    			+ " where  que_create_ent_id != ans_create_ent_id"
    			+ " group by usr_ent_id " 
    			+ " ) u on (ans_id = u.min_and_id) "
    		 + "inner join regUser rg on rg.usr_ent_id = ans_create_ent_id";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		LearningSituation ls = map.get(rs.getLong("usr_ent_id"));
        		if(ls == null){
        			ls = new LearningSituation();
        			ls.setLs_ent_id(rs.getLong("usr_ent_id"));
        			ls.setIs_first_helper_usr(rs.getString("ans_create_usr"));
            		map.put(rs.getLong("usr_ent_id"), ls);
        		} else {
            		ls.setIs_first_helper_usr(rs.getString("ans_create_usr"));

        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    
    public void getKnowledgeSituation(Connection con, Map<Long, LearningSituation> map) throws SQLException{
    	String sql = " select usr_ent_id,COUNT(distinct kbi_id) ls_share_count, min(kbi_create_datetime) first_share_datetime,COUNT(distinct kiv_kbi_id) ls_access_count from RegUser "
    			+ " inner join acEntityRole on erl_ent_id = usr_ent_id "
    			+ " inner join acRole on rol_id = erl_rol_id  "
    			+ " left join kb_item on usr_id = kbi_create_user_id and "
    			+ " ((kbi_app_status='APPROVED' and kbi_approve_user_id is not null) or kbi_app_status != 'APPROVED') "
    			+ " left join kb_item_view on usr_ent_id = kiv_usr_ent_id "
    			+ " group by usr_ent_id ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		LearningSituation ls = map.get(rs.getLong("usr_ent_id"));
        		if(ls == null){
        			ls = new LearningSituation();
        			ls.setLs_ent_id(rs.getLong("usr_ent_id"));
            		ls.setLs_share_count(rs.getLong("ls_share_count"));
            		ls.setLs_access_count(rs.getLong("ls_access_count"));
            		//第一次分享知识时间
            		ls.setIs_first_share_date(rs.getTimestamp("first_share_datetime"));
            		map.put(rs.getLong("usr_ent_id"), ls);
        		} else {
        			ls.setLs_share_count(rs.getLong("ls_share_count"));
            		ls.setLs_access_count(rs.getLong("ls_access_count"));
            		//第一次分享知识时间
            		ls.setIs_first_share_date(rs.getTimestamp("first_share_datetime"));
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    public void getAttentionSituation(Connection con, Map<Long, LearningSituation> map) throws SQLException{
    	
    	String sql = " select usr.usr_ent_id source_id ,count(distinct my_att.s_att_target_uid) ls_attention_num,count(distinct other_att.s_att_source_uid) ls_fans_num, "
    			+ " min(other_att.s_att_id) firt_fans_id from RegUser usr "
    			+ " inner join acEntityRole on erl_ent_id = usr_ent_id "
    			+ " inner join acRole on rol_id = erl_rol_id  "
    			+ " left join sns_attention my_att on my_att.s_att_source_uid = usr.usr_ent_id "
    			+ " left join sns_attention other_att on other_att.s_att_target_uid = usr.usr_ent_id "
    			+ " group by usr.usr_ent_id order by usr.usr_ent_id ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		LearningSituation ls = map.get(rs.getLong("source_id"));
        		if(ls == null){
        			ls = new LearningSituation();
        			ls.setLs_ent_id(rs.getLong("source_id"));
            		ls.setLs_attention_num(rs.getLong("ls_attention_num"));
            		ls.setLs_fans_num(rs.getLong("ls_fans_num"));
            		  //我的第一个粉丝
            		if(rs.getLong("ls_fans_num") > 0){
            			getFistFansdData( con,  rs.getLong("firt_fans_id") ,  ls);
            		}
            		map.put(rs.getLong("s_att_source_uid"), ls);
        		} else {
        			ls.setLs_attention_num(rs.getLong("ls_attention_num"));
            		ls.setLs_fans_num(rs.getLong("ls_fans_num"));
            	   
            		  //我的第一个粉丝
            		if(rs.getLong("firt_fans_id") > 0){
            			getFistFansdData( con,  rs.getLong("firt_fans_id") ,  ls);
            		}
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    public void getCollectAndShareSituation(Connection con, Map<Long, LearningSituation> map) throws SQLException{
    	String sql = " select usr_ent_id, count(distinct s_clt_id) ls_collect_num, count(distinct s_sha_id) ls_share_num, min(s_sha_create_datetime) Is_first_share_date"
    			+ " from regUser "
    			+ " inner join acEntityRole on erl_ent_id = usr_ent_id "
    			+ " inner join acRole on rol_id = erl_rol_id  "
    			+ " left join sns_collect on s_clt_uid = usr_ent_id "
    			+ " left join sns_share on s_sha_uid = usr_ent_id "
    			+ " group by usr_ent_id order by usr_ent_id ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		LearningSituation ls = map.get(rs.getLong("usr_ent_id"));
        		if(ls == null){
        			ls = new LearningSituation();
        			ls.setLs_ent_id(rs.getLong("usr_ent_id"));
            		ls.setLs_collect_num(rs.getLong("ls_collect_num"));
            		ls.setLs_share_num(rs.getLong("ls_share_num"));
//            		ls.setIs_first_share_date(rs.getTimestamp("Is_first_share_date"));
            		map.put(rs.getLong("usr_ent_id"), ls);
        		} else {
        			ls.setLs_collect_num(rs.getLong("ls_collect_num"));
            		ls.setLs_share_num(rs.getLong("ls_share_num"));
//            		ls.setIs_first_share_date(rs.getTimestamp("Is_first_share_date"));
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    public void getPraiseSituation(Connection con, Map<Long, LearningSituation> map) throws SQLException{
    	String sql = " select usr_ent_id, count(distinct praise_other.s_vtl_target_id) ls_praise_others_num, "
    			+ " count(distinct praised.s_vtl_log_id) ls_praised_num , min(praised.s_vtl_log_id)  min_vtl_log_id from regUser "
    			+ " inner join acEntityRole on erl_ent_id = usr_ent_id "
    			+ " inner join acRole on rol_id = erl_rol_id  "
    			+ " left join sns_valuation_log praise_other on praise_other.s_vtl_uid = usr_ent_id and Lower(praise_other.s_vtl_type) = 'like'  and (Lower(praise_other.s_vtl_module) != 'course' or s_vtl_is_comment = 1) "
    			+ " left join ( "
    			+ " 	select s_cmt_uid,s_vtl_log_id from sns_comment "
    			+ " 	inner join sns_valuation_log on Lower(s_vtl_type) = 'like' and s_cmt_id = s_vtl_target_id and Lower(s_vtl_module) in ('doing','group','course') and s_vtl_is_comment = 1 "
    			+ "		inner join sns_doing on s_doi_id = s_cmt_reply_to_id "
    			+ " 	union ( "
    			+ "			select s_doi_uid,s_vtl_log_id from sns_doing "
    			+ "			inner join sns_valuation_log on Lower(s_vtl_type) = 'like' and s_doi_id = s_vtl_target_id and Lower(s_vtl_module) in ('doing','group') and s_vtl_is_comment = 0 "
    			+ "		) union ( "
    			+ "			select ans_create_ent_id,s_vtl_log_id from knowAnswer "
    			+ "			inner join sns_valuation_log on Lower(s_vtl_type) = 'like' and ans_id = s_vtl_target_id and Lower(s_vtl_module) = 'answer') "
    			+ "	) praised on  praised.s_cmt_uid = usr_ent_id "
    			+ " group by usr_ent_id order by usr_ent_id ";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	rs = stmt.executeQuery();
        	while(rs.next()){
        		LearningSituation ls = map.get(rs.getLong("usr_ent_id"));
        		if(ls == null){
        			ls = new LearningSituation();
        			ls.setLs_ent_id(rs.getLong("usr_ent_id"));
            		ls.setLs_praised_num(rs.getLong("ls_praised_num"));
            		ls.setLs_praise_others_num(rs.getLong("ls_praise_others_num"));
            		//第一次给个点赞的人及时间
            		if(rs.getLong("min_vtl_log_id") > 0){
            			getFistPraisedData( con,  rs.getLong("min_vtl_log_id") ,  ls);
            		}
            		map.put(rs.getLong("usr_ent_id"), ls);
        		} else {
        			ls.setLs_praised_num(rs.getLong("ls_praised_num"));
            		ls.setLs_praise_others_num(rs.getLong("ls_praise_others_num"));
            		if(rs.getLong("min_vtl_log_id") > 0){
            			/*
            		     * 第一次给个点赞的人及时间
            		     */
            			getFistPraisedData( con,  rs.getLong("min_vtl_log_id") ,  ls);
            		}
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
    /*
     * 第一次给个点赞的人及时间
     */
    public void getFistPraisedData(Connection con, Long s_vtl_log_id, LearningSituation ls) throws SQLException{
    	String sql = " select usr_display_bil,s_vtl_create_datetime from sns_valuation_log join RegUser on s_vtl_uid = usr_ent_id where s_vtl_log_id = ?";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	stmt.setLong(1, s_vtl_log_id);
        	rs = stmt.executeQuery();
        	if(rs.next()){
        		if(ls != null){
        			ls.setIs_first_praised_usr(rs.getString("usr_display_bil"));
            		ls.setIs_first_praised_date(rs.getTimestamp("s_vtl_create_datetime"));
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }
    
   
    public void getFistFansdData(Connection con, Long s_att_id, LearningSituation ls) throws SQLException{
    	String sql = " select usr_display_bil, s_att_create_datetime from sns_attention join RegUser on s_att_source_uid = usr_ent_id where s_att_id = ?";
    	PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
        	stmt = con.prepareStatement(sql);
        	stmt.setLong(1, s_att_id);
        	rs = stmt.executeQuery();
        	if(rs.next()){
        		if(ls != null){
        			ls.setIs_first_fans_usr(rs.getString("usr_display_bil"));
            		ls.setIs_first_fans_date(rs.getTimestamp("s_att_create_datetime"));
        		}
        	}
        } finally {
        	cwSQL.cleanUp(rs, stmt);
        }
    }

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		init();
		process();
	}
}
