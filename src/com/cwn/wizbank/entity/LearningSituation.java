package com.cwn.wizbank.entity;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 学习概况
 * 
 **/
public class LearningSituation {
	/**
	 * 学习概况所有者
	 * 
	 **/
	public Long ls_ent_id;

	/**
	 * 学习总时长
	 * 
	 **/
	public Long ls_learn_duration;

	/**
	 * 总学分
	 * 
	 **/
	public Float ls_learn_credit;

	/**
	 * 总积分
	 * 
	 **/
	public Long ls_total_integral;

	/**
	 * 课程总数
	 * 
	 **/
	public Long ls_total_courses;

	/**
	 * 已完成课程数
	 * 
	 **/
	public Long ls_course_completed_num;

	/**
	 * 未完成课程数
	 * 
	 **/
	public Long ls_course_fail_num;

	/**
	 * 进行中课程数
	 * 
	 **/
	public Long ls_course_inprogress_num;

	/**
	 * 审批中课程数
	 * 
	 **/
	public Long ls_course_pending_num;

	/**
	 * 考试总数
	 * 
	 **/
	public Long ls_total_exams;

	/**
	 * 已完成考试数
	 * 
	 **/
	public Long ls_exam_completed_num;

	/**
	 * 未完成考试数
	 * 
	 **/
	public Long ls_exam_fail_num;

	/**
	 * 进行中考试数
	 * 
	 **/
	public Long ls_exam_inprogress_num;

	/**
	 * 审批中考试数
	 * 
	 **/
	public Long ls_exam_pending_num;

	/**
	 * 粉丝数
	 * 
	 **/
	public Long ls_fans_num;

	/**
	 * 关注数
	 * 
	 **/
	public Long ls_attention_num;

	/**
	 * 被赞数
	 * 
	 **/
	public Long ls_praised_num;

	/**
	 * 赞他人数
	 * 
	 **/
	public Long ls_praise_others_num;

	/**
	 * 收藏数
	 * 
	 **/
	public Long ls_collect_num;

	/**
	 * 分享数
	 * 
	 **/
	public Long ls_share_num;

	/**
	 * 创建群组数
	 * 
	 **/
	public Long ls_create_group_num;

	/**
	 * 参与群组数
	 * 
	 **/
	public Long ls_join_group_num;

	/**
	 * 群组发言数
	 * 
	 **/
	public Long ls_group_speech_num;

	/**
	 * 提问数
	 * 
	 **/
	public Long ls_question_num;

	/**
	 * 回答数
	 * 
	 **/
	public Long ls_answer_num;

	/**
	 * 更新时间
	 * 
	 **/
	public Date ls_update_time;

	/**
	 * 学习总时长格式化
	 * 
	 **/
	public String ls_learn_duration_str;

	/**
	 * 学习总时长格式化(html)
	 * 
	 **/
	public String ls_learn_duration_html;
	/**
	 * 是否是考试
	 * 
	 **/
	public Long itm_exam_ind;

	/**
	 * 排名
	 **/
	private Long rownum;

	/**
	 * 我的分享
	 **/
	private Long ls_share_count;

	/**
	 * 我的浏览
	 **/
	private Long ls_access_count;
	
	/**
	 * 报名第一个课程的时间
	 **/
	private Timestamp Is_first_course_date	;
	
	/**
	 * 第一个给我点赞的人
	 **/
	private String  Is_first_praised_usr	;
	
	/**
	 * 第一次收到赞的时间
	 **/
	private Timestamp Is_first_praised_date	;
	
	/**
	 * 第一个粉丝
	 **/
	private String  Is_first_fans_usr	;
	
	/**
	 * 第一个收到粉丝的时间
	 **/
	private Timestamp  Is_first_fans_date	;
	
	/**
	 * 第一次分享知识的问题
	 **/
	private Timestamp  Is_first_share_date	;
	
	/**
	 * 第一次在问答中提问的时间
	 **/
	private Timestamp  Is_first_que_date	;
	
	/**
	 * 第一个回答我问题的人
	 **/
	private String  Is_first_helper_usr	;
	
	/**
	 * 用户注册到平台的时间
	 **/
	private Timestamp  Is_signup_date;
	

	/**
	 * 合并属性值
	 * @param learningSituation
	 * @param learningSituationValue
	 * @return
	 */
	public LearningSituation setLearningSituationValue(LearningSituation learningSituation,LearningSituation learningSituationValue){
		
		if(learningSituation != null && learningSituationValue != null){			
			if(learningSituation.ls_ent_id == null && learningSituationValue.ls_ent_id != null) learningSituation.ls_ent_id = learningSituationValue.ls_ent_id;
			if(learningSituation.ls_learn_duration == null && learningSituationValue.ls_learn_duration != null) learningSituation.ls_learn_duration = learningSituationValue.ls_learn_duration;
			if(learningSituation.ls_learn_credit == null && learningSituationValue.ls_learn_credit != null) learningSituation.ls_learn_credit = learningSituationValue.ls_learn_credit;
			if(learningSituation.ls_total_integral == null && learningSituationValue.ls_total_integral != null) learningSituation.ls_total_integral = learningSituationValue.ls_total_integral;
			if(learningSituation.ls_total_courses == null && learningSituationValue.ls_total_courses != null) learningSituation.ls_total_courses = learningSituationValue.ls_total_courses;
			if(learningSituation.ls_course_completed_num == null && learningSituationValue.ls_course_completed_num != null) learningSituation.ls_course_completed_num = learningSituationValue.ls_course_completed_num;
			if(learningSituation.ls_course_fail_num == null && learningSituationValue.ls_course_fail_num != null) learningSituation.ls_course_fail_num = learningSituationValue.ls_course_fail_num;
			if(learningSituation.ls_course_inprogress_num == null && learningSituationValue.ls_course_inprogress_num != null) learningSituation.ls_course_inprogress_num = learningSituationValue.ls_course_inprogress_num;
			if(learningSituation.ls_course_pending_num == null && learningSituationValue.ls_course_pending_num != null) learningSituation.ls_course_pending_num = learningSituationValue.ls_course_pending_num;
			if(learningSituation.ls_total_exams == null && learningSituationValue.ls_total_exams != null) learningSituation.ls_total_exams = learningSituationValue.ls_total_exams;
			if(learningSituation.ls_exam_completed_num == null && learningSituationValue.ls_exam_completed_num != null) learningSituation.ls_exam_completed_num = learningSituationValue.ls_exam_completed_num;
			if(learningSituation.ls_exam_fail_num == null && learningSituationValue.ls_exam_fail_num != null) learningSituation.ls_exam_fail_num = learningSituationValue.ls_exam_fail_num;
			if(learningSituation.ls_exam_inprogress_num == null && learningSituationValue.ls_exam_inprogress_num != null) learningSituation.ls_exam_inprogress_num = learningSituationValue.ls_exam_inprogress_num;
			if(learningSituation.ls_exam_pending_num == null && learningSituationValue.ls_exam_pending_num != null) learningSituation.ls_exam_pending_num = learningSituationValue.ls_exam_pending_num;
			if(learningSituation.ls_fans_num == null && learningSituationValue.ls_fans_num != null) learningSituation.ls_fans_num = learningSituationValue.ls_fans_num;
			if(learningSituation.ls_attention_num == null && learningSituationValue.ls_attention_num != null) learningSituation.ls_attention_num = learningSituationValue.ls_attention_num;
			if(learningSituation.ls_praised_num == null && learningSituationValue.ls_praised_num != null) learningSituation.ls_praised_num = learningSituationValue.ls_praised_num;
			if(learningSituation.ls_praise_others_num == null && learningSituationValue.ls_praise_others_num != null) learningSituation.ls_praise_others_num = learningSituationValue.ls_praise_others_num;
			if(learningSituation.ls_collect_num == null && learningSituationValue.ls_collect_num != null) learningSituation.ls_collect_num = learningSituationValue.ls_collect_num;
			if(learningSituation.ls_share_num == null && learningSituationValue.ls_share_num != null) learningSituation.ls_share_num = learningSituationValue.ls_share_num;
			if(learningSituation.ls_create_group_num == null && learningSituationValue.ls_create_group_num != null) learningSituation.ls_create_group_num = learningSituationValue.ls_create_group_num;
			if(learningSituation.ls_join_group_num == null && learningSituationValue.ls_join_group_num != null) learningSituation.ls_join_group_num = learningSituationValue.ls_join_group_num;
			if(learningSituation.ls_group_speech_num == null && learningSituationValue.ls_group_speech_num != null) learningSituation.ls_group_speech_num = learningSituationValue.ls_group_speech_num;
			if(learningSituation.ls_question_num == null && learningSituationValue.ls_question_num != null) learningSituation.ls_question_num = learningSituationValue.ls_question_num;
			if(learningSituation.ls_answer_num == null && learningSituationValue.ls_answer_num != null) learningSituation.ls_answer_num = learningSituationValue.ls_answer_num;
			if(learningSituation.ls_update_time == null && learningSituationValue.ls_update_time != null) learningSituation.ls_update_time = learningSituationValue.ls_update_time;
			if(learningSituation.ls_learn_duration_str == null && learningSituationValue.ls_learn_duration_str != null) learningSituation.ls_learn_duration_str = learningSituationValue.ls_learn_duration_str;
			if(learningSituation.ls_learn_duration_html == null && learningSituationValue.ls_learn_duration_html != null) learningSituation.ls_learn_duration_html = learningSituationValue.ls_learn_duration_html;
			if(learningSituation.itm_exam_ind == null && learningSituationValue.itm_exam_ind != null) learningSituation.itm_exam_ind = learningSituationValue.itm_exam_ind;
			if(learningSituation.rownum == null && learningSituationValue.rownum != null) learningSituation.rownum = learningSituationValue.rownum;
			if(learningSituation.ls_share_count == null && learningSituationValue.ls_share_count != null) learningSituation.ls_share_count = learningSituationValue.ls_share_count;
			if(learningSituation.ls_access_count == null && learningSituationValue.ls_access_count != null) learningSituation.ls_access_count = learningSituationValue.ls_access_count;
			if(learningSituation.Is_first_course_date == null && learningSituationValue.Is_first_course_date != null) learningSituation.Is_first_course_date = learningSituationValue.Is_first_course_date;
			if(learningSituation.Is_first_praised_usr == null && learningSituationValue.Is_first_praised_usr != null) learningSituation.Is_first_praised_usr = learningSituationValue.Is_first_praised_usr;
			if(learningSituation.Is_first_praised_date == null && learningSituationValue.Is_first_praised_date != null) learningSituation.Is_first_praised_date = learningSituationValue.Is_first_praised_date;
			if(learningSituation.Is_first_fans_usr == null && learningSituationValue.Is_first_fans_usr != null) learningSituation.Is_first_fans_usr = learningSituationValue.Is_first_fans_usr;
			if(learningSituation.Is_first_fans_date == null && learningSituationValue.Is_first_fans_date != null) learningSituation.Is_first_fans_date = learningSituationValue.Is_first_fans_date;
			if(learningSituation.Is_first_share_date == null && learningSituationValue.Is_first_share_date != null) learningSituation.Is_first_share_date = learningSituationValue.Is_first_share_date;
			if(learningSituation.Is_first_que_date == null && learningSituationValue.Is_first_que_date != null) learningSituation.Is_first_que_date = learningSituationValue.Is_first_que_date;
			if(learningSituation.Is_first_helper_usr == null && learningSituationValue.Is_first_helper_usr != null) learningSituation.Is_first_helper_usr = learningSituationValue.Is_first_helper_usr;
			if(learningSituation.Is_signup_date == null && learningSituationValue.Is_signup_date != null) learningSituation.Is_signup_date = learningSituationValue.Is_signup_date;		
		}
		
		return learningSituation;
	}
/**
 * 将null值置0
 * @param learningSituation
 * @return
 */
	public LearningSituation setDefaultZero(LearningSituation learningSituation){
		if(learningSituation != null){			
			if(learningSituation.getLs_learn_duration() == null) learningSituation.setLs_learn_duration(0l);
			if(learningSituation.getLs_learn_credit() == null) learningSituation.setLs_learn_credit((float) 0);
			if(learningSituation.getLs_total_integral() == null) learningSituation.setLs_total_integral(0l);
			if(learningSituation.getLs_total_courses() == null) learningSituation.setLs_total_courses(0l);
			if(learningSituation.getLs_course_completed_num() == null) learningSituation.setLs_course_completed_num(0l);
			if(learningSituation.getLs_course_fail_num() == null) learningSituation.setLs_course_fail_num(0l);
			if(learningSituation.getLs_course_inprogress_num() == null) learningSituation.setLs_course_inprogress_num(0l);
			if(learningSituation.getLs_course_pending_num() == null) learningSituation.setLs_course_pending_num(0l);
			if(learningSituation.getLs_total_exams() == null) learningSituation.setLs_total_exams(0l);
			if(learningSituation.getLs_exam_completed_num() == null) learningSituation.setLs_exam_completed_num(0l);
			if(learningSituation.getLs_exam_fail_num() == null) learningSituation.setLs_exam_fail_num(0l);
			if(learningSituation.getLs_exam_inprogress_num() == null) learningSituation.setLs_exam_inprogress_num(0l);
			if(learningSituation.getLs_exam_pending_num() == null) learningSituation.setLs_exam_pending_num(0l);
			if(learningSituation.getLs_fans_num() == null) learningSituation.setLs_fans_num(0l);
			if(learningSituation.getLs_attention_num() == null) learningSituation.setLs_attention_num(0l);
			if(learningSituation.getLs_praised_num() == null) learningSituation.setLs_praised_num(0l);
			if(learningSituation.getLs_praise_others_num() == null) learningSituation.setLs_praise_others_num(0l);
			if(learningSituation.getLs_collect_num() == null) learningSituation.setLs_collect_num(0l);
			if(learningSituation.getLs_share_num() == null) learningSituation.setLs_share_num(0l);
			if(learningSituation.getLs_create_group_num() == null) learningSituation.setLs_create_group_num(0l);
			if(learningSituation.getLs_join_group_num() == null) learningSituation.setLs_join_group_num(0l);
			if(learningSituation.getLs_group_speech_num() == null) learningSituation.setLs_group_speech_num(0l);
			if(learningSituation.getLs_question_num() == null) learningSituation.setLs_question_num(0l);
			if(learningSituation.getLs_answer_num() == null) learningSituation.setLs_answer_num(0l);
			if(learningSituation.getLs_share_count() == null) learningSituation.setLs_share_count(0l);
			if(learningSituation.getLs_access_count() == null) learningSituation.setLs_access_count(0l);
		}

		return learningSituation;
	}
	/**
	 * 用户组
	 */
	private String usg_name;

	private RegUser user;

	public Long getLs_ent_id() {
		return ls_ent_id;
	}

	public void setLs_ent_id(Long ls_ent_id) {
		this.ls_ent_id = ls_ent_id;
	}

	public Long getLs_learn_duration() {
		return ls_learn_duration;
	}

	public void setLs_learn_duration(Long ls_learn_duration) {
		this.ls_learn_duration = ls_learn_duration;
	}

	public Float getLs_learn_credit() {
		return ls_learn_credit;
	}

	public void setLs_learn_credit(Float ls_learn_credit) {
		this.ls_learn_credit = ls_learn_credit;
	}

	public Long getLs_total_integral() {
		return ls_total_integral;
	}

	public void setLs_total_integral(Long ls_total_integral) {
		this.ls_total_integral = ls_total_integral;
	}

	public Long getLs_total_courses() {
		return ls_total_courses;
	}

	public void setLs_total_courses(Long ls_total_courses) {
		this.ls_total_courses = ls_total_courses;
	}

	public Long getLs_course_completed_num() {
		return ls_course_completed_num;
	}

	public void setLs_course_completed_num(Long ls_course_completed_num) {
		this.ls_course_completed_num = ls_course_completed_num;
	}

	public Long getLs_course_fail_num() {
		return ls_course_fail_num;
	}

	public void setLs_course_fail_num(Long ls_course_fail_num) {
		this.ls_course_fail_num = ls_course_fail_num;
	}

	public Long getLs_course_inprogress_num() {
		return ls_course_inprogress_num;
	}

	public void setLs_course_inprogress_num(Long ls_course_inprogress_num) {
		this.ls_course_inprogress_num = ls_course_inprogress_num;
	}

	public Long getLs_course_pending_num() {
		return ls_course_pending_num;
	}

	public void setLs_course_pending_num(Long ls_course_pending_num) {
		this.ls_course_pending_num = ls_course_pending_num;
	}

	public Long getLs_total_exams() {
		return ls_total_exams;
	}

	public void setLs_total_exams(Long ls_total_exams) {
		this.ls_total_exams = ls_total_exams;
	}

	public Long getLs_exam_completed_num() {
		return ls_exam_completed_num;
	}

	public void setLs_exam_completed_num(Long ls_exam_completed_num) {
		this.ls_exam_completed_num = ls_exam_completed_num;
	}

	public Long getLs_exam_fail_num() {
		return ls_exam_fail_num;
	}

	public void setLs_exam_fail_num(Long ls_exam_fail_num) {
		this.ls_exam_fail_num = ls_exam_fail_num;
	}

	public Long getLs_exam_inprogress_num() {
		return ls_exam_inprogress_num;
	}

	public void setLs_exam_inprogress_num(Long ls_exam_inprogress_num) {
		this.ls_exam_inprogress_num = ls_exam_inprogress_num;
	}

	public Long getLs_exam_pending_num() {
		return ls_exam_pending_num;
	}

	public void setLs_exam_pending_num(Long ls_exam_pending_num) {
		this.ls_exam_pending_num = ls_exam_pending_num;
	}

	public Long getLs_fans_num() {
		return ls_fans_num;
	}

	public void setLs_fans_num(Long ls_fans_num) {
		this.ls_fans_num = ls_fans_num;
	}

	public Long getLs_attention_num() {
		return ls_attention_num;
	}

	public void setLs_attention_num(Long ls_attention_num) {
		this.ls_attention_num = ls_attention_num;
	}

	public Long getLs_praised_num() {
		return ls_praised_num;
	}

	public void setLs_praised_num(Long ls_praised_num) {
		this.ls_praised_num = ls_praised_num;
	}

	public Long getLs_praise_others_num() {
		return ls_praise_others_num;
	}

	public void setLs_praise_others_num(Long ls_praise_others_num) {
		this.ls_praise_others_num = ls_praise_others_num;
	}

	public Long getLs_collect_num() {
		return ls_collect_num;
	}

	public void setLs_collect_num(Long ls_collect_num) {
		this.ls_collect_num = ls_collect_num;
	}

	public Long getLs_share_num() {
		return ls_share_num;
	}

	public void setLs_share_num(Long ls_share_num) {
		this.ls_share_num = ls_share_num;
	}

	public Long getLs_create_group_num() {
		return ls_create_group_num;
	}

	public void setLs_create_group_num(Long ls_create_group_num) {
		this.ls_create_group_num = ls_create_group_num;
	}

	public Long getLs_join_group_num() {
		return ls_join_group_num;
	}

	public void setLs_join_group_num(Long ls_join_group_num) {
		this.ls_join_group_num = ls_join_group_num;
	}

	public Long getLs_group_speech_num() {
		return ls_group_speech_num;
	}

	public void setLs_group_speech_num(Long ls_group_speech_num) {
		this.ls_group_speech_num = ls_group_speech_num;
	}

	public Long getLs_question_num() {
		return ls_question_num;
	}

	public void setLs_question_num(Long ls_question_num) {
		this.ls_question_num = ls_question_num;
	}

	public Long getLs_answer_num() {
		return ls_answer_num;
	}

	public void setLs_answer_num(Long ls_answer_num) {
		this.ls_answer_num = ls_answer_num;
	}

	public Date getLs_update_time() {
		return ls_update_time;
	}

	public void setLs_update_time(Date ls_update_time) {
		this.ls_update_time = ls_update_time;
	}

	public Long getItm_exam_ind() {
		return itm_exam_ind;
	}

	public void setItm_exam_ind(Long itm_exam_ind) {
		this.itm_exam_ind = itm_exam_ind;
	}

	public String getLs_learn_duration_str() {
		return ls_learn_duration_str;
	}

	public void setLs_learn_duration_str(String ls_learn_duration_str) {
		this.ls_learn_duration_str = ls_learn_duration_str;
	}

	public String getLs_learn_duration_html() {
		return ls_learn_duration_html;
	}

	public void setLs_learn_duration_html(String ls_learn_duration_html) {
		this.ls_learn_duration_html = ls_learn_duration_html;
	}

	public Long getRownum() {
		return rownum;
	}

	public void setRownum(Long rownum) {
		this.rownum = rownum;
	}

	public RegUser getUser() {
		return user;
	}

	public void setUser(RegUser user) {
		this.user = user;
	}

	public Long getLs_share_count() {
		return ls_share_count;
	}

	public void setLs_share_count(Long ls_share_count) {
		this.ls_share_count = ls_share_count;
	}

	public Long getLs_access_count() {
		return ls_access_count;
	}

	public void setLs_access_count(Long ls_access_count) {
		this.ls_access_count = ls_access_count;
	}

	public String getUsg_name() {
		return usg_name;
	}

	public void setUsg_name(String usg_name) {
		this.usg_name = usg_name;
	}

	public Timestamp getIs_first_course_date() {
		return Is_first_course_date;
	}

	public void setIs_first_course_date(Timestamp isFirstCourseDate) {
		Is_first_course_date = isFirstCourseDate;
	}

	public String getIs_first_praised_usr() {
		return Is_first_praised_usr;
	}

	public void setIs_first_praised_usr(String isFirstPraisedEntId) {
		Is_first_praised_usr = isFirstPraisedEntId;
	}

	public Timestamp getIs_first_praised_date() {
		return Is_first_praised_date;
	}

	public void setIs_first_praised_date(Timestamp isFirstPraisedDate) {
		Is_first_praised_date = isFirstPraisedDate;
	}

	public String getIs_first_fans_usr() {
		return Is_first_fans_usr;
	}

	public void setIs_first_fans_usr(String isFirstFansEntId) {
		Is_first_fans_usr = isFirstFansEntId;
	}

	public Timestamp getIs_first_fans_date() {
		return Is_first_fans_date;
	}

	public void setIs_first_fans_date(Timestamp isFirstFansDate) {
		Is_first_fans_date = isFirstFansDate;
	}

	public Timestamp getIs_first_share_date() {
		return Is_first_share_date;
	}

	public void setIs_first_share_date(Timestamp isFirstShareDate) {
		Is_first_share_date = isFirstShareDate;
	}

	public Timestamp getIs_first_que_date() {
		return Is_first_que_date;
	}

	public void setIs_first_que_date(Timestamp isFirstQueDate) {
		Is_first_que_date = isFirstQueDate;
	}

	public String getIs_first_helper_usr() {
		return Is_first_helper_usr;
	}

	public void setIs_first_helper_usr(String isFirstHelperEntId) {
		Is_first_helper_usr = isFirstHelperEntId;
	}

	public Timestamp getIs_signup_date() {
		return Is_signup_date;
	}

	public void setIs_signup_date(Timestamp isSignupDate) {
		Is_signup_date = isSignupDate;
	}
	
}
