package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

import com.cwn.wizbank.entity.vo.LearningMapVo;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 课程
 * 2014-8-7 下午5:56:49
 */
public class AeItem implements java.io.Serializable {
	private static final long serialVersionUID = -1167414941389856129L;
	
	public final static String SELFSTUDY = "SELFSTUDY";	//网上课程
	public final static String CLASSROOM = "CLASSROOM";	//离线课程
	public final static String INTEGRATED = "INTEGRATED"; //继承培训
	public final static String AUDIOVIDEO = "AUDIOVIDEO"; //公开课
	
		/**
		 * pk
		 * 
		 **/
		Long itm_id;
		/**
		 * 标题
		 **/
		String itm_title;
		/**
		 * 
		 **/
		String itm_code;
		/**
		 * 
		 **/
		String itm_version_code;
		/**
		 * 
		 **/
		String itm_type;
		/**
		 * 
		 **/
		Long itm_capacity;
		/**
		 * 
		 **/
		Long itm_min_capacity;
		/**
		 * 
		 **/
		Double itm_unit;
		/**
		 * 
		 **/
		String itm_fee_ccy;
		/**
		 * 
		 **/
		Float itm_fee;
		/**
		 * 
		 **/
		Date itm_appn_start_datetime;
		/**
		 * 
		 **/
		Date itm_appn_end_datetime;
		/**
		 * 
		 **/
		Date itm_eff_start_datetime;
		/**
		 * 
		 **/
		Date itm_eff_end_datetime;
		/**
		 * 
		 **/
		String itm_xml;
		/**
		 * 
		 **/
		String itm_status;
		/**
		 * 
		 **/
		Long itm_owner_ent_id;
		/**
		 * 
		 **/
		Date itm_create_timestamp;
		/**
		 * 
		 **/
		String itm_create_usr_id;
		/**
		 * 
		 **/
		Date itm_upd_timestamp;
		/**
		 * 
		 **/
		String itm_upd_usr_id;
		/**
		 * 
		 **/
		String itm_ext1;
		/**
		 * 
		 **/
		Long itm_create_run_ind;
		/**
		 * 
		 **/
		Long itm_run_ind;
		/**
		 * 
		 **/
		Long itm_apply_ind;
		/**
		 * 
		 **/
		Long itm_deprecated_ind;
		/**
		 * 
		 **/
		Long itm_qdb_ind;
		/**
		 * 
		 **/
		Long itm_auto_enrol_qdb_ind;
		/**
		 * 
		 **/
		String itm_life_status;
		/**
		 * 
		 **/
		String itm_apply_method;
		/**
		 * 
		 **/
		Long itm_imd_id;
		/**
		 * 
		 **/
		String itm_person_in_charge;
		/**
		 * 
		 **/
		Long itm_rsv_id;
		/**
		 * 
		 **/
		String itm_cancellation_reason;
		/**
		 * 
		 **/
		String itm_cancellation_type;
		/**
		 * 
		 **/
		Long itm_ctf_id;
		/**
		 * 
		 **/
		Date itm_syn_timestamp;
		/**
		 * 
		 **/
		Long itm_create_session_ind;
		/**
		 * 
		 **/
		Long itm_session_ind;
		/**
		 * 
		 **/
		Long itm_has_attendance_ind;
		/**
		 * 
		 **/
		Long itm_ji_ind;
		/**
		 * 
		 **/
		Long itm_completion_criteria_ind;
		/**
		 * 
		 **/
		Double itm_fee_1;
		/**
		 * 
		 **/
		Long itm_can_cancel_ind;
		/**
		 * 
		 **/
		Date itm_content_eff_start_time;
		/**
		 * 
		 **/
		Date itm_content_eff_end_time;
		/**
		 * 
		 **/
		Long itm_content_eff_duration;
		/**
		 * 
		 **/
		Long itm_can_qr_ind;
		/**
		 * 
		 **/
		Long itm_retake_ind;
		/**
		 * 
		 **/
		String itm_approval_status;
		/**
		 * 
		 **/
		String itm_approval_action;
		/**
		 * 
		 **/
		Date itm_approve_timestamp;
		/**
		 * 
		 **/
		String itm_approve_usr_id;
		/**
		 * 
		 **/
		String itm_submit_action;
		/**
		 * 
		 **/
		Date itm_submit_timestamp;
		/**
		 * 
		 **/
		String itm_submit_usr_id;
		/**
		 * 
		 **/
		String itm_app_approval_type;
		/**
		 * 
		 **/
		Long itm_tcr_id;
		/**
		 * 
		 **/
		String itm_content_def;
		/**
		 * 
		 **/
		String itm_enroll_type;
		/**
		 * 
		 **/
		Long itm_send_enroll_email_ind;
		/**
		 * 
		 **/
		Date itm_qte_notify_timestamp;
		/**
		 * 
		 **/
		String itm_access_type;
		/**
		 * 
		 **/
		Long itm_mark_buffer_day;
		/**
		 * 
		 **/
		Long itm_notify_days;
		/**
		 * 
		 **/
		String itm_notify_email;
		/**
		 * 
		 **/
		Long itm_not_allow_waitlist_ind;
		/**
		 * 
		 **/
		String itm_target_enrol_type;
		/**
		 * 
		 **/
		Double itm_comment_avg_score;
		/**
		 * 
		 **/
		Long itm_comment_total_count;
		/**
		 * 
		 **/
		Long itm_comment_total_score;
		/**
		 * 
		 **/
		Date itm_publish_timestamp;
		/**
		 * 
		 **/
		String itm_srh_content;
		/**
		 * 课程介绍
		 **/
		String itm_desc;
		/**
		 * 
		 **/
		String itm_plan_code;
		/**
		 * 
		 **/
		String itm_icon;
		/**
		 * 
		 **/
		Long itm_exam_ind;
		/**
		 * 
		 **/
		Long itm_blend_ind;
		/**
		 * 
		 **/
		Long itm_ref_ind;
		/**
		 * 
		 **/
		Long itm_bonus_ind;
		/**
		 * 
		 **/
		Double itm_diff_factor;
		/**
		 * 
		 **/
		Long itm_integrated_ind;
		/**
		 * 
		 **/
		Long itm_share_ind;
		/**
		 * 
		 **/
		String itm_offline_pkg;
		/**
		 * 
		 **/
		String itm_offline_pkg_file;
		/**
		 * 
		 **/
		Long itm_cfc_id;
		/**
		 * 
		 **/
		String itm_inst_type;
		/**
		 * 
		 **/
		Long itm_parent_id;
		/**
		 * 被赞数
		 **/
		Long s_cnt_like_count;
		/**
		 * 排名
		 **/
		Long rownum;
		/**
		 * 课程收藏时间
		 **/
		Date s_clt_create_datetime;
		/**
		 * 访问量
		 */
		Integer ies_access_count;
		/**
		 * 证书
		 */
		String itm_cfc_title;
		/**
		 * 最后访问时间
		 */
		Date last_access_time;
		/**
		 * 用户是否赞过
		 */
		int is_user_like;
		/**
		 * 报名数量
		 **/
		Long cnt_app_count;
		/**
		 * 赞总数
		 */
		Long cnt_comment_count;
		
		Long cos_res_id;
		
		/**
		 * 是否发布到移动端,no or yes
		 */
		String itm_mobile_ind;
		
		/**
		 * 是否可看
		 */
		int itm_canread = 0;

		/**
		 * 报名期限状态 	0 -> 无限期		1 -> 有具体时间		2 -> XX天后
		 */
		int itm_online_content_period = 0;
		
		/**
		 * 是否已报名		0 -> 未报名		1 -> 已报名
		 */
		int itm_is_enrol = 0;
//Object
		AeItemExtension ies;
		
		TcTrainingCenter tcr;
		
		CourseEvaluation cov;
		
		AeItem parent;
		
		List<AeItem> childrens;
	
		ItemTargetLrnDetail itd;
		
		AeApplication app;
				
		IntegCompleteCondition icd;

		AeTreeNode aeTreeNode;
		
		SnsCount snsCount;
		
		SnsValuationLog userLike;
		
		LearningMapVo mapVo;
		
		Course course;
		
		//未发布，且未过期
		public boolean itm_status_off;
		
		public boolean itm_has_lesson;
		
		public Long getCos_res_id() {
			return cos_res_id;
		}

		public void setCos_res_id(Long cos_res_id) {
			this.cos_res_id = cos_res_id;
		}
		
		public SnsCount getSnsCount() {
			return snsCount;
		}

		public void setSnsCount(SnsCount snsCount) {
			this.snsCount = snsCount;
		}

		public AeItem(){
		}

		public Long getItm_id() {
			return itm_id;
		}

		public void setItm_id(Long itm_id) {
			this.itm_id = itm_id;
		}

		public String getItm_title() {
			return itm_title;
		}

		public void setItm_title(String itm_title) {
			this.itm_title = itm_title;
		}

		public String getItm_code() {
			return itm_code;
		}

		public void setItm_code(String itm_code) {
			this.itm_code = itm_code;
		}

		public String getItm_version_code() {
			return itm_version_code;
		}

		public void setItm_version_code(String itm_version_code) {
			this.itm_version_code = itm_version_code;
		}

		public String getItm_type() {
			return itm_type;
		}

		public void setItm_type(String itm_type) {
			this.itm_type = itm_type;
		}

		public Long getItm_capacity() {
			return itm_capacity;
		}

		public void setItm_capacity(Long itm_capacity) {
			this.itm_capacity = itm_capacity;
		}

		public Long getItm_min_capacity() {
			return itm_min_capacity;
		}

		public void setItm_min_capacity(Long itm_min_capacity) {
			this.itm_min_capacity = itm_min_capacity;
		}

		public Double getItm_unit() {
			return itm_unit;
		}

		public void setItm_unit(Double itm_unit) {
			this.itm_unit = itm_unit;
		}

		public String getItm_fee_ccy() {
			return itm_fee_ccy;
		}

		public void setItm_fee_ccy(String itm_fee_ccy) {
			this.itm_fee_ccy = itm_fee_ccy;
		}

		public Float getItm_fee() {
			return itm_fee;
		}

		public void setItm_fee(Float itm_fee) {
			this.itm_fee = itm_fee;
		}

		public Date getItm_appn_start_datetime() {
			return itm_appn_start_datetime;
		}

		public void setItm_appn_start_datetime(Date itm_appn_start_datetime) {
			this.itm_appn_start_datetime = itm_appn_start_datetime;
		}

		public Date getItm_appn_end_datetime() {
			return itm_appn_end_datetime;
		}

		public void setItm_appn_end_datetime(Date itm_appn_end_datetime) {
			this.itm_appn_end_datetime = itm_appn_end_datetime;
		}

		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
		public Date getItm_eff_start_datetime() {
			return itm_eff_start_datetime;
		}

		public void setItm_eff_start_datetime(Date itm_eff_start_datetime) {
			this.itm_eff_start_datetime = itm_eff_start_datetime;
		}

		@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
		public Date getItm_eff_end_datetime() {
			return itm_eff_end_datetime;
		}

		public void setItm_eff_end_datetime(Date itm_eff_end_datetime) {
			this.itm_eff_end_datetime = itm_eff_end_datetime;
		}

		public String getItm_xml() {
			return itm_xml;
		}

		public void setItm_xml(String itm_xml) {
			this.itm_xml = itm_xml;
		}

		public String getItm_status() {
			return itm_status;
		}

		public void setItm_status(String itm_status) {
			this.itm_status = itm_status;
		}

		public Long getItm_owner_ent_id() {
			return itm_owner_ent_id;
		}

		public void setItm_owner_ent_id(Long itm_owner_ent_id) {
			this.itm_owner_ent_id = itm_owner_ent_id;
		}

		public Date getItm_create_timestamp() {
			return itm_create_timestamp;
		}

		public void setItm_create_timestamp(Date itm_create_timestamp) {
			this.itm_create_timestamp = itm_create_timestamp;
		}

		public String getItm_create_usr_id() {
			return itm_create_usr_id;
		}

		public void setItm_create_usr_id(String itm_create_usr_id) {
			this.itm_create_usr_id = itm_create_usr_id;
		}

		public Date getItm_upd_timestamp() {
			return itm_upd_timestamp;
		}

		public void setItm_upd_timestamp(Date itm_upd_timestamp) {
			this.itm_upd_timestamp = itm_upd_timestamp;
		}

		public String getItm_upd_usr_id() {
			return itm_upd_usr_id;
		}

		public void setItm_upd_usr_id(String itm_upd_usr_id) {
			this.itm_upd_usr_id = itm_upd_usr_id;
		}

		public String getItm_ext1() {
			return itm_ext1;
		}

		public void setItm_ext1(String itm_ext1) {
			this.itm_ext1 = itm_ext1;
		}

		public Long getItm_create_run_ind() {
			return itm_create_run_ind;
		}

		public void setItm_create_run_ind(Long itm_create_run_ind) {
			this.itm_create_run_ind = itm_create_run_ind;
		}

		public Long getItm_run_ind() {
			return itm_run_ind;
		}

		public void setItm_run_ind(Long itm_run_ind) {
			this.itm_run_ind = itm_run_ind;
		}

		public Long getItm_apply_ind() {
			return itm_apply_ind;
		}

		public void setItm_apply_ind(Long itm_apply_ind) {
			this.itm_apply_ind = itm_apply_ind;
		}

		public Long getItm_deprecated_ind() {
			return itm_deprecated_ind;
		}

		public void setItm_deprecated_ind(Long itm_deprecated_ind) {
			this.itm_deprecated_ind = itm_deprecated_ind;
		}

		public Long getItm_qdb_ind() {
			return itm_qdb_ind;
		}

		public void setItm_qdb_ind(Long itm_qdb_ind) {
			this.itm_qdb_ind = itm_qdb_ind;
		}

		public Long getItm_auto_enrol_qdb_ind() {
			return itm_auto_enrol_qdb_ind;
		}

		public void setItm_auto_enrol_qdb_ind(Long itm_auto_enrol_qdb_ind) {
			this.itm_auto_enrol_qdb_ind = itm_auto_enrol_qdb_ind;
		}

		public String getItm_life_status() {
			return itm_life_status;
		}

		public void setItm_life_status(String itm_life_status) {
			this.itm_life_status = itm_life_status;
		}

		public String getItm_apply_method() {
			return itm_apply_method;
		}

		public void setItm_apply_method(String itm_apply_method) {
			this.itm_apply_method = itm_apply_method;
		}

		public Long getItm_imd_id() {
			return itm_imd_id;
		}

		public void setItm_imd_id(Long itm_imd_id) {
			this.itm_imd_id = itm_imd_id;
		}

		public String getItm_person_in_charge() {
			return itm_person_in_charge;
		}

		public void setItm_person_in_charge(String itm_person_in_charge) {
			this.itm_person_in_charge = itm_person_in_charge;
		}

		public Long getItm_rsv_id() {
			return itm_rsv_id;
		}

		public void setItm_rsv_id(Long itm_rsv_id) {
			this.itm_rsv_id = itm_rsv_id;
		}

		public String getItm_cancellation_reason() {
			return itm_cancellation_reason;
		}

		public void setItm_cancellation_reason(String itm_cancellation_reason) {
			this.itm_cancellation_reason = itm_cancellation_reason;
		}

		public String getItm_cancellation_type() {
			return itm_cancellation_type;
		}

		public void setItm_cancellation_type(String itm_cancellation_type) {
			this.itm_cancellation_type = itm_cancellation_type;
		}

		public Long getItm_ctf_id() {
			return itm_ctf_id;
		}

		public void setItm_ctf_id(Long itm_ctf_id) {
			this.itm_ctf_id = itm_ctf_id;
		}

		public Date getItm_syn_timestamp() {
			return itm_syn_timestamp;
		}

		public void setItm_syn_timestamp(Date itm_syn_timestamp) {
			this.itm_syn_timestamp = itm_syn_timestamp;
		}

		public Long getItm_create_session_ind() {
			return itm_create_session_ind;
		}

		public void setItm_create_session_ind(Long itm_create_session_ind) {
			this.itm_create_session_ind = itm_create_session_ind;
		}

		public Long getItm_session_ind() {
			return itm_session_ind;
		}

		public void setItm_session_ind(Long itm_session_ind) {
			this.itm_session_ind = itm_session_ind;
		}

		public Long getItm_has_attendance_ind() {
			return itm_has_attendance_ind;
		}

		public void setItm_has_attendance_ind(Long itm_has_attendance_ind) {
			this.itm_has_attendance_ind = itm_has_attendance_ind;
		}

		public Long getItm_ji_ind() {
			return itm_ji_ind;
		}

		public void setItm_ji_ind(Long itm_ji_ind) {
			this.itm_ji_ind = itm_ji_ind;
		}

		public Long getItm_completion_criteria_ind() {
			return itm_completion_criteria_ind;
		}

		public void setItm_completion_criteria_ind(Long itm_completion_criteria_ind) {
			this.itm_completion_criteria_ind = itm_completion_criteria_ind;
		}

		public Double getItm_fee_1() {
			return itm_fee_1;
		}

		public void setItm_fee_1(Double itm_fee_1) {
			this.itm_fee_1 = itm_fee_1;
		}

		public Long getItm_can_cancel_ind() {
			return itm_can_cancel_ind;
		}

		public void setItm_can_cancel_ind(Long itm_can_cancel_ind) {
			this.itm_can_cancel_ind = itm_can_cancel_ind;
		}

		public Long getItm_content_eff_duration() {
			return itm_content_eff_duration;
		}

		public void setItm_content_eff_duration(Long itm_content_eff_duration) {
			this.itm_content_eff_duration = itm_content_eff_duration;
		}

		public Long getItm_can_qr_ind() {
			return itm_can_qr_ind;
		}

		public void setItm_can_qr_ind(Long itm_can_qr_ind) {
			this.itm_can_qr_ind = itm_can_qr_ind;
		}

		public Long getItm_retake_ind() {
			return itm_retake_ind;
		}

		public void setItm_retake_ind(Long itm_retake_ind) {
			this.itm_retake_ind = itm_retake_ind;
		}

		public String getItm_approval_status() {
			return itm_approval_status;
		}

		public void setItm_approval_status(String itm_approval_status) {
			this.itm_approval_status = itm_approval_status;
		}

		public String getItm_approval_action() {
			return itm_approval_action;
		}

		public void setItm_approval_action(String itm_approval_action) {
			this.itm_approval_action = itm_approval_action;
		}

		public Date getItm_approve_timestamp() {
			return itm_approve_timestamp;
		}

		public void setItm_approve_timestamp(Date itm_approve_timestamp) {
			this.itm_approve_timestamp = itm_approve_timestamp;
		}

		public String getItm_approve_usr_id() {
			return itm_approve_usr_id;
		}

		public void setItm_approve_usr_id(String itm_approve_usr_id) {
			this.itm_approve_usr_id = itm_approve_usr_id;
		}

		public String getItm_submit_action() {
			return itm_submit_action;
		}

		public void setItm_submit_action(String itm_submit_action) {
			this.itm_submit_action = itm_submit_action;
		}

		public Date getItm_submit_timestamp() {
			return itm_submit_timestamp;
		}

		public void setItm_submit_timestamp(Date itm_submit_timestamp) {
			this.itm_submit_timestamp = itm_submit_timestamp;
		}

		public String getItm_submit_usr_id() {
			return itm_submit_usr_id;
		}

		public void setItm_submit_usr_id(String itm_submit_usr_id) {
			this.itm_submit_usr_id = itm_submit_usr_id;
		}

		public String getItm_app_approval_type() {
			return itm_app_approval_type;
		}

		public void setItm_app_approval_type(String itm_app_approval_type) {
			this.itm_app_approval_type = itm_app_approval_type;
		}

		public Long getItm_tcr_id() {
			return itm_tcr_id;
		}

		public void setItm_tcr_id(Long itm_tcr_id) {
			this.itm_tcr_id = itm_tcr_id;
		}

		public String getItm_content_def() {
			return itm_content_def;
		}

		public void setItm_content_def(String itm_content_def) {
			this.itm_content_def = itm_content_def;
		}

		public String getItm_enroll_type() {
			return itm_enroll_type;
		}

		public void setItm_enroll_type(String itm_enroll_type) {
			this.itm_enroll_type = itm_enroll_type;
		}

		public Long getItm_send_enroll_email_ind() {
			return itm_send_enroll_email_ind;
		}

		public void setItm_send_enroll_email_ind(Long itm_send_enroll_email_ind) {
			this.itm_send_enroll_email_ind = itm_send_enroll_email_ind;
		}

		public Date getItm_qte_notify_timestamp() {
			return itm_qte_notify_timestamp;
		}

		public void setItm_qte_notify_timestamp(Date itm_qte_notify_timestamp) {
			this.itm_qte_notify_timestamp = itm_qte_notify_timestamp;
		}

		public String getItm_access_type() {
			return itm_access_type;
		}

		public void setItm_access_type(String itm_access_type) {
			this.itm_access_type = itm_access_type;
		}

		public Long getItm_mark_buffer_day() {
			return itm_mark_buffer_day;
		}

		public void setItm_mark_buffer_day(Long itm_mark_buffer_day) {
			this.itm_mark_buffer_day = itm_mark_buffer_day;
		}

		public Long getItm_notify_days() {
			return itm_notify_days;
		}

		public void setItm_notify_days(Long itm_notify_days) {
			this.itm_notify_days = itm_notify_days;
		}

		public String getItm_notify_email() {
			return itm_notify_email;
		}

		public void setItm_notify_email(String itm_notify_email) {
			this.itm_notify_email = itm_notify_email;
		}

		public Long getItm_not_allow_waitlist_ind() {
			return itm_not_allow_waitlist_ind;
		}

		public void setItm_not_allow_waitlist_ind(Long itm_not_allow_waitlist_ind) {
			this.itm_not_allow_waitlist_ind = itm_not_allow_waitlist_ind;
		}

		public String getItm_target_enrol_type() {
			return itm_target_enrol_type;
		}

		public void setItm_target_enrol_type(String itm_target_enrol_type) {
			this.itm_target_enrol_type = itm_target_enrol_type;
		}

		public Double getItm_comment_avg_score() {
			return itm_comment_avg_score;
		}

		public void setItm_comment_avg_score(Double itm_comment_avg_score) {
			this.itm_comment_avg_score = itm_comment_avg_score;
		}

		public Long getItm_comment_total_count() {
			return itm_comment_total_count;
		}

		public void setItm_comment_total_count(Long itm_comment_total_count) {
			this.itm_comment_total_count = itm_comment_total_count;
		}

		public Long getItm_comment_total_score() {
			return itm_comment_total_score;
		}

		public void setItm_comment_total_score(Long itm_comment_total_score) {
			this.itm_comment_total_score = itm_comment_total_score;
		}

		public Date getItm_publish_timestamp() {
			return itm_publish_timestamp;
		}

		public void setItm_publish_timestamp(Date itm_publish_timestamp) {
			this.itm_publish_timestamp = itm_publish_timestamp;
		}

		public String getItm_srh_content() {
			return itm_srh_content;
		}

		public void setItm_srh_content(String itm_srh_content) {
			this.itm_srh_content = itm_srh_content;
		}

		public String getItm_desc() {
			return itm_desc;
		}

		public void setItm_desc(String itm_desc) {
			this.itm_desc = itm_desc;
		}

		public String getItm_plan_code() {
			return itm_plan_code;
		}

		public void setItm_plan_code(String itm_plan_code) {
			this.itm_plan_code = itm_plan_code;
		}

		public String getItm_icon() {
			return itm_icon;
		}

		public void setItm_icon(String itm_icon) {
			this.itm_icon = itm_icon;
		}

		public Long getItm_exam_ind() {
			return itm_exam_ind;
		}

		public void setItm_exam_ind(Long itm_exam_ind) {
			this.itm_exam_ind = itm_exam_ind;
		}

		public Long getItm_blend_ind() {
			return itm_blend_ind;
		}

		public void setItm_blend_ind(Long itm_blend_ind) {
			this.itm_blend_ind = itm_blend_ind;
		}

		public Long getItm_ref_ind() {
			return itm_ref_ind;
		}

		public void setItm_ref_ind(Long itm_ref_ind) {
			this.itm_ref_ind = itm_ref_ind;
		}

		public Long getItm_bonus_ind() {
			return itm_bonus_ind;
		}

		public void setItm_bonus_ind(Long itm_bonus_ind) {
			this.itm_bonus_ind = itm_bonus_ind;
		}

		public Double getItm_diff_factor() {
			return itm_diff_factor;
		}

		public void setItm_diff_factor(Double itm_diff_factor) {
			this.itm_diff_factor = itm_diff_factor;
		}

		public Long getItm_integrated_ind() {
			return itm_integrated_ind;
		}

		public void setItm_integrated_ind(Long itm_integrated_ind) {
			this.itm_integrated_ind = itm_integrated_ind;
		}

		public Long getItm_share_ind() {
			return itm_share_ind;
		}

		public void setItm_share_ind(Long itm_share_ind) {
			this.itm_share_ind = itm_share_ind;
		}

		public String getItm_offline_pkg() {
			return itm_offline_pkg;
		}

		public void setItm_offline_pkg(String itm_offline_pkg) {
			this.itm_offline_pkg = itm_offline_pkg;
		}

		public String getItm_offline_pkg_file() {
			return itm_offline_pkg_file;
		}

		public void setItm_offline_pkg_file(String itm_offline_pkg_file) {
			this.itm_offline_pkg_file = itm_offline_pkg_file;
		}

		public Long getItm_cfc_id() {
			return itm_cfc_id;
		}

		public void setItm_cfc_id(Long itm_cfc_id) {
			this.itm_cfc_id = itm_cfc_id;
		}

		public String getItm_inst_type() {
			return itm_inst_type;
		}

		public void setItm_inst_type(String itm_inst_type) {
			this.itm_inst_type = itm_inst_type;
		}

		public Long getItm_parent_id() {
			return itm_parent_id;
		}

		public void setItm_parent_id(Long itm_parent_id) {
			this.itm_parent_id = itm_parent_id;
		}

		public AeItemExtension getIes() {
			return ies;
		}

		public void setIes(AeItemExtension ies) {
			this.ies = ies;
		}

		public TcTrainingCenter getTcr() {
			return tcr;
		}

		public void setTcr(TcTrainingCenter tcr) {
			this.tcr = tcr;
		}

		public CourseEvaluation getCov() {
			return cov;
		}

		public void setCov(CourseEvaluation cov) {
			this.cov = cov;
		}

		public AeItem getParent() {
			return parent;
		}

		public void setParent(AeItem parent) {
			this.parent = parent;
		}

		public List<AeItem> getChildrens() {
			return childrens;
		}

		public void setChildrens(List<AeItem> childrens) {
			this.childrens = childrens;
		}

		public ItemTargetLrnDetail getItd() {
			return itd;
		}

		public void setItd(ItemTargetLrnDetail itd) {
			this.itd = itd;
		}

		public AeApplication getApp() {
			return app;
		}

		public void setApp(AeApplication app) {
			this.app = app;
		}

		public Long getS_cnt_like_count() {
			return s_cnt_like_count;
		}

		public void setS_cnt_like_count(Long s_cnt_like_count) {
			this.s_cnt_like_count = s_cnt_like_count;
		}

		public Long getRownum() {
			return rownum;
		}

		public void setRownum(Long rownum) {
			this.rownum = rownum;
		}

		public IntegCompleteCondition getIcd() {
			return icd;
		}

		public void setIcd(IntegCompleteCondition icd) {
			this.icd = icd;
		}

		public Date getS_clt_create_datetime() {
			return s_clt_create_datetime;
		}

		public void setS_clt_create_datetime(Date s_clt_create_datetime) {
			this.s_clt_create_datetime = s_clt_create_datetime;
		}

		public AeTreeNode getAeTreeNode() {
			return aeTreeNode;
		}

		public void setAeTreeNode(AeTreeNode aeTreeNode) {
			this.aeTreeNode = aeTreeNode;
		}

		public Date getItm_content_eff_start_time() {
			return itm_content_eff_start_time;
		}

		public void setItm_content_eff_start_time(Date itm_content_eff_start_time) {
			this.itm_content_eff_start_time = itm_content_eff_start_time;
		}

		public Date getItm_content_eff_end_time() {
			return itm_content_eff_end_time;
		}

		public void setItm_content_eff_end_time(Date itm_content_eff_end_time) {
			this.itm_content_eff_end_time = itm_content_eff_end_time;
		}
		
		public Integer getIes_access_count() {
			return ies_access_count;
		}

		public void setIes_access_count(Integer ies_access_count) {
			this.ies_access_count = ies_access_count;
		}

		public SnsValuationLog getUserLike() {
			return userLike;
		}

		public void setUserLike(SnsValuationLog userLike) {
			this.userLike = userLike;
		}

		public LearningMapVo getMapVo() {
			return mapVo;
		}

		public void setMapVo(LearningMapVo mapVo) {
			this.mapVo = mapVo;
		}

		public Date getLast_access_time() {
			return last_access_time;
		}

		public void setLast_access_time(Date last_access_time) {
			this.last_access_time = last_access_time;
		}

		public int getIs_user_like() {
			return is_user_like;
		}

		public void setIs_user_like(int is_user_like) {
			this.is_user_like = is_user_like;
		}
		
		public int getItm_online_content_period() {
			return itm_online_content_period;
		}
		
		public void setItm_online_content_period(int itm_online_content_period) {
			this.itm_online_content_period = itm_online_content_period;
		}
		
		public int getItm_is_enrol() {
			return itm_is_enrol;
		}
		
		public void setItm_is_enrol(int itm_is_enrol) {
			this.itm_is_enrol = itm_is_enrol;
		}
		
		public Long getCnt_app_count() {
			return cnt_app_count;
		}

		public void setCnt_app_count(Long cnt_app_count) {
			this.cnt_app_count = cnt_app_count;
		}

		public Long getCnt_comment_count() {
			return cnt_comment_count;
		}

		public void setCnt_comment_count(Long cnt_comment_count) {
			this.cnt_comment_count = cnt_comment_count;
		}

		public String getItm_cfc_title() {
			return itm_cfc_title;
		}

		public void setItm_cfc_title(String itm_cfc_title) {
			this.itm_cfc_title = itm_cfc_title;
		}

		public Course getCourse() {
			return course;
		}

		public void setCourse(Course course) {
			this.course = course;
		}

		public boolean isItm_status_off() {
			return itm_status_off;
		}

		public void setItm_status_off(boolean itm_status_off) {
			this.itm_status_off = itm_status_off;
		}

		public int getItm_canread() {
			return itm_canread;
		}

		public void setItm_canread(int itm_canread) {
			this.itm_canread = itm_canread;
		}

		public String getItm_mobile_ind() {
			return itm_mobile_ind;
		}

		public void setItm_mobile_ind(String itm_mobile_ind) {
			this.itm_mobile_ind = itm_mobile_ind;
		}

		public boolean isItm_has_lesson() {
			return itm_has_lesson;
		}

		public void setItm_has_lesson(boolean itm_has_lesson) {
			this.itm_has_lesson = itm_has_lesson;
		}
		
}