package com.cw.wizbank.JsonMod.study.bean;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

import com.cw.wizbank.JsonMod.Course.Course;
import com.cw.wizbank.JsonMod.Course.bean.CCRBean;
import com.cw.wizbank.JsonMod.Course.bean.CompletionCriteriaBean;

/**
 * 已审批的课程(每个学员对已审批的课程可以有不同的学习状态)
 * @author kimyu
 */
public class ApprovalCourseBean extends CourseBean {
	private Timestamp app_upd_timestamp;	// 报名被成功录取的时间
	private String ats_type;				// 该门课程的学习状态(状态：可以是正在学习中的课程、或已完成的课程、或未完成的、或已放弃的课程)
//	private double cov_total_time; 			// 总计学习时长
	private String cov_total_time;			// 总计学习时长(形如："00:03:22")
	private double cov_score; 				// 分数
	private Timestamp cov_last_acc_datetime;// 学员上次访问课程内容时间
	private Timestamp att_timestamp; 		// 考勤日期
	private double cov_progress;			// 进度
	private long cos_res_id;				// 对应Course表的cos_res_id
	private long cov_tkh_id;				// 对应CourseEvaluation表的cov_tkh_id
	private String itm_icon;
	private String itm_dummy_type;
	private String content_status;			// 学员学习该门课程的当前状态是已结束课程
	private CompletionCriteriaBean comp_criteria;

	public Timestamp getApp_upd_timestamp() {
		return app_upd_timestamp;
	}

	public void setApp_upd_timestamp(Timestamp app_upd_timestamp) {
		this.app_upd_timestamp = app_upd_timestamp;
	}

	public String getAts_type() {
		return ats_type;
	}

	public void setAts_type(String ats_type) {
		this.ats_type = ats_type;
	}

	public String getCov_total_time() {
		return cov_total_time;
	}

	public void setCov_total_time(String cov_total_time) {
		this.cov_total_time = cov_total_time;
	}

	public double getCov_score() {
		return cov_score;
	}

	public void setCov_score(double cov_score) {
		this.cov_score = cov_score;
	}

	public Timestamp getCov_last_acc_datetime() {
		return cov_last_acc_datetime;
	}

	public void setCov_last_acc_datetime(Timestamp cov_last_acc_datetime) {
		this.cov_last_acc_datetime = cov_last_acc_datetime;
	}

	public Timestamp getAtt_timestamp() {
		return att_timestamp;
	}

	public void setAtt_timestamp(Timestamp att_timestamp) {
		this.att_timestamp = att_timestamp;
	}

	public double getCov_progress() {
		return cov_progress;
	}

	public void setCov_progress(double cov_progress) {
		this.cov_progress = cov_progress;
	}

	public long getCos_res_id() {
		return cos_res_id;
	}

	public void setCos_res_id(long cos_res_id) {
		this.cos_res_id = cos_res_id;
	}

	public long getCov_tkh_id() {
		return cov_tkh_id;
	}

	public void setCov_tkh_id(long cov_tkh_id) {
		this.cov_tkh_id = cov_tkh_id;
	}
	
	public String getItm_icon() {
		return itm_icon;
	}

	public void setItm_icon(String itm_icon) {
		this.itm_icon = itm_icon;
	}

	public String getItm_dummy_type() {
		return itm_dummy_type;
	}

	public void setItm_dummy_type(String itm_dummy_type) {
		this.itm_dummy_type = itm_dummy_type;
	}

	public String getContent_status() {
		return content_status;
	}

	public void setContent_status(String content_status) {
		this.content_status = content_status;
	}

	public CompletionCriteriaBean getComp_criteria() {
		return comp_criteria;
	}

	public void setComp_criteria(Connection con, long tkh_id, long res_id) throws SQLException {
		Hashtable onLineStatusHs =new Hashtable();
		Course course = new Course();
		CCRBean ccr = course.getCCR_id(con, tkh_id, res_id);
		Vector offAndCom = course.getCMTAndOfflineRs(con, tkh_id, res_id, ccr.getCcr_id());
		CompletionCriteriaBean comCrt = course.getCompletionCriteria(offAndCom, ccr, onLineStatusHs);
		this.comp_criteria = comCrt;
	}
	
}
