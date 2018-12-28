package com.cwn.wizbank.entity;

import java.util.Date;


public class Module implements java.io.Serializable {
	private static final long serialVersionUID = -4788521338241994578L;
		/**
		 * pk
		 * null
		 **/
		Long mod_res_id;
		/**
		 * null
		 **/
		String mod_type;
		/**
		 * null
		 **/
		Double mod_max_score;
		/**
		 * null
		 **/
		Double mod_pass_score;
		/**
		 * null
		 **/
		String mod_instruct;
		/**
		 * null
		 **/
		Long mod_max_attempt;
		/**
		 * null
		 **/
		Long mod_max_usr_attempt;
		/**
		 * null
		 **/
		Long mod_score_ind;
		/**
		 * null
		 **/
		Long mod_score_reset;
		/**
		 * null
		 **/
		String mod_logic;
		/**
		 * null
		 **/
		Date mod_eff_start_datetime;
		/**
		 * null
		 **/
		Date mod_eff_end_datetime;
		/**
		 * null
		 **/
		String mod_usr_id_instructor;
		/**
		 * null
		 **/
		String mod_core_vendor;
		/**
		 * null
		 **/
		String mod_password;
		/**
		 * null
		 **/
		String mod_import_xml;
		/**
		 * null
		 **/
		Date mod_import_datetime;
		/**
		 * null
		 **/
		String mod_time_limit_action;
		/**
		 * null
		 **/
		String mod_web_launch;
		/**
		 * null
		 **/
		String mod_vendor;
		/**
		 * null
		 **/
		String mod_aicc_version;
		/**
		 * null
		 **/
		Long mod_has_rate_q;
		/**
		 * null
		 **/
		Long mod_is_public;
		/**
		 * null
		 **/
		Long mod_public_need_enrol;
		/**
		 * null
		 **/
		Long mod_mod_id_root;
		/**
		 * null
		 **/
		Long mod_show_answer_ind;
		/**
		 * null
		 **/
		Long mod_sub_after_passed_ind;
		/**
		 * null
		 **/
		Long mod_mod_res_id_parent;
		/**
		 * null
		 **/
		Long mod_auto_save_ind;
		/**
		 * null
		 **/
		Long mod_sgp_ind;
		/**
		 * null
		 **/
		Long mod_managed_ind;
		/**
		 * null
		 **/
		Long mod_started_ind;
		/**
		 * null
		 **/
		Long mod_tcr_id;
		/**
		 * null
		 **/
		Long mod_show_answer_after_passed_ind;
		/**
		 * null
		 **/
		Long mod_show_a_a_passed_ind;
		/**
		 * null
		 **/
		Long mod_required_time;
		/**
		 * null
		 **/
		Long mod_download_ind;
		
		int mod_mobile_ind;
		
				
		/**
		 * 提示作业期限,报名成功多少天后
		 **/
		Integer ass_due_date_day;
		
		/**
		 * 提示作业期限, 指定具体日期
		 **/
		Date ass_due_datetime;
		
		/**
		 * 所指定的模板
		 **/
		String tpl_stylesheet;
		
		/**
		 * 先修模块的ID
		 **/
		Long rrq_req_res_id;

		/**
		 * 先修模块的标题
		 **/
		String pre_res_title ;
		/**
		 * 先修模块的类型
		 **/
		String rrq_res_type;
		/**
		 * 先修模块所要求的状态
		 **/
		String rrq_status;
		/**
		 * 先修模块已获得状态
		 **/
		String pre_mod_mov_status ;
		/**
		 * 参考总人数
		 **/
		Long exam_total;
		/**
		 * 题目总数
		 **/
		Long topic_total;
		
        /**
		 * 是否已完成先修模块
		 **/
        boolean pre_mod_had_completed;
        
        String mod_test_style;
        
        Resources res;


		public Module(){
		}

		public Long getMod_res_id() {
			return mod_res_id;
		}

		public void setMod_res_id(Long mod_res_id) {
			this.mod_res_id = mod_res_id;
		}

		public String getMod_type() {
			return mod_type;
		}

		public void setMod_type(String mod_type) {
			this.mod_type = mod_type;
		}

		public Double getMod_max_score() {
			return mod_max_score;
		}

		public void setMod_max_score(Double mod_max_score) {
			this.mod_max_score = mod_max_score;
		}

		public Double getMod_pass_score() {
			return mod_pass_score;
		}

		public void setMod_pass_score(Double mod_pass_score) {
			this.mod_pass_score = mod_pass_score;
		}

		public String getMod_instruct() {
			return mod_instruct;
		}

		public void setMod_instruct(String mod_instruct) {
			this.mod_instruct = mod_instruct;
		}

		public Long getMod_max_attempt() {
			return mod_max_attempt;
		}

		public void setMod_max_attempt(Long mod_max_attempt) {
			this.mod_max_attempt = mod_max_attempt;
		}

		public Long getMod_max_usr_attempt() {
			return mod_max_usr_attempt;
		}

		public void setMod_max_usr_attempt(Long mod_max_usr_attempt) {
			this.mod_max_usr_attempt = mod_max_usr_attempt;
		}

		public Long getMod_score_ind() {
			return mod_score_ind;
		}

		public void setMod_score_ind(Long mod_score_ind) {
			this.mod_score_ind = mod_score_ind;
		}

		public Long getMod_score_reset() {
			return mod_score_reset;
		}

		public void setMod_score_reset(Long mod_score_reset) {
			this.mod_score_reset = mod_score_reset;
		}

		public String getMod_logic() {
			return mod_logic;
		}

		public void setMod_logic(String mod_logic) {
			this.mod_logic = mod_logic;
		}

		public Date getMod_eff_start_datetime() {
			return mod_eff_start_datetime;
		}

		public void setMod_eff_start_datetime(Date mod_eff_start_datetime) {
			this.mod_eff_start_datetime = mod_eff_start_datetime;
		}

		public Date getMod_eff_end_datetime() {
			return mod_eff_end_datetime;
		}

		public void setMod_eff_end_datetime(Date mod_eff_end_datetime) {
			this.mod_eff_end_datetime = mod_eff_end_datetime;
		}

		public String getMod_usr_id_instructor() {
			return mod_usr_id_instructor;
		}

		public void setMod_usr_id_instructor(String mod_usr_id_instructor) {
			this.mod_usr_id_instructor = mod_usr_id_instructor;
		}

		public String getMod_core_vendor() {
			return mod_core_vendor;
		}

		public void setMod_core_vendor(String mod_core_vendor) {
			this.mod_core_vendor = mod_core_vendor;
		}

		public String getMod_password() {
			return mod_password;
		}

		public void setMod_password(String mod_password) {
			this.mod_password = mod_password;
		}

		public String getMod_import_xml() {
			return mod_import_xml;
		}

		public void setMod_import_xml(String mod_import_xml) {
			this.mod_import_xml = mod_import_xml;
		}

		public Date getMod_import_datetime() {
			return mod_import_datetime;
		}

		public void setMod_import_datetime(Date mod_import_datetime) {
			this.mod_import_datetime = mod_import_datetime;
		}

		public String getMod_time_limit_action() {
			return mod_time_limit_action;
		}

		public void setMod_time_limit_action(String mod_time_limit_action) {
			this.mod_time_limit_action = mod_time_limit_action;
		}

		public String getMod_web_launch() {
			return mod_web_launch;
		}

		public void setMod_web_launch(String mod_web_launch) {
			this.mod_web_launch = mod_web_launch;
		}

		public String getMod_vendor() {
			return mod_vendor;
		}

		public void setMod_vendor(String mod_vendor) {
			this.mod_vendor = mod_vendor;
		}

		public String getMod_aicc_version() {
			return mod_aicc_version;
		}

		public void setMod_aicc_version(String mod_aicc_version) {
			this.mod_aicc_version = mod_aicc_version;
		}

		public Long getMod_has_rate_q() {
			return mod_has_rate_q;
		}

		public void setMod_has_rate_q(Long mod_has_rate_q) {
			this.mod_has_rate_q = mod_has_rate_q;
		}

		public Long getMod_is_public() {
			return mod_is_public;
		}

		public void setMod_is_public(Long mod_is_public) {
			this.mod_is_public = mod_is_public;
		}

		public Long getMod_public_need_enrol() {
			return mod_public_need_enrol;
		}

		public void setMod_public_need_enrol(Long mod_public_need_enrol) {
			this.mod_public_need_enrol = mod_public_need_enrol;
		}

		public Long getMod_mod_id_root() {
			return mod_mod_id_root;
		}

		public void setMod_mod_id_root(Long mod_mod_id_root) {
			this.mod_mod_id_root = mod_mod_id_root;
		}

		public Long getMod_show_answer_ind() {
			return mod_show_answer_ind;
		}

		public void setMod_show_answer_ind(Long mod_show_answer_ind) {
			this.mod_show_answer_ind = mod_show_answer_ind;
		}

		public Long getMod_sub_after_passed_ind() {
			return mod_sub_after_passed_ind;
		}

		public void setMod_sub_after_passed_ind(Long mod_sub_after_passed_ind) {
			this.mod_sub_after_passed_ind = mod_sub_after_passed_ind;
		}

		public Long getMod_mod_res_id_parent() {
			return mod_mod_res_id_parent;
		}

		public void setMod_mod_res_id_parent(Long mod_mod_res_id_parent) {
			this.mod_mod_res_id_parent = mod_mod_res_id_parent;
		}

		public Long getMod_auto_save_ind() {
			return mod_auto_save_ind;
		}

		public void setMod_auto_save_ind(Long mod_auto_save_ind) {
			this.mod_auto_save_ind = mod_auto_save_ind;
		}

		public Long getMod_sgp_ind() {
			return mod_sgp_ind;
		}

		public void setMod_sgp_ind(Long mod_sgp_ind) {
			this.mod_sgp_ind = mod_sgp_ind;
		}

		public Long getMod_managed_ind() {
			return mod_managed_ind;
		}

		public void setMod_managed_ind(Long mod_managed_ind) {
			this.mod_managed_ind = mod_managed_ind;
		}

		public Long getMod_started_ind() {
			return mod_started_ind;
		}

		public void setMod_started_ind(Long mod_started_ind) {
			this.mod_started_ind = mod_started_ind;
		}

		public Long getMod_tcr_id() {
			return mod_tcr_id;
		}

		public void setMod_tcr_id(Long mod_tcr_id) {
			this.mod_tcr_id = mod_tcr_id;
		}

		public Long getMod_show_answer_after_passed_ind() {
			return mod_show_answer_after_passed_ind;
		}

		public void setMod_show_answer_after_passed_ind(Long mod_show_answer_after_passed_ind) {
			this.mod_show_answer_after_passed_ind = mod_show_answer_after_passed_ind;
		}

		public Long getMod_show_a_a_passed_ind() {
			return mod_show_a_a_passed_ind;
		}

		public void setMod_show_a_a_passed_ind(Long mod_show_a_a_passed_ind) {
			this.mod_show_a_a_passed_ind = mod_show_a_a_passed_ind;
		}

		public Long getMod_required_time() {
			return mod_required_time;
		}

		public void setMod_required_time(Long mod_required_time) {
			this.mod_required_time = mod_required_time;
		}

		public Long getMod_download_ind() {
			return mod_download_ind;
		}

		public void setMod_download_ind(Long mod_download_ind) {
			this.mod_download_ind = mod_download_ind;
		}
		

		public Integer getAss_due_date_day() {
			return ass_due_date_day;
		}

		public void setAss_due_date_day(Integer assDueDateDay) {
			ass_due_date_day = assDueDateDay;
		}

		public Date getAss_due_datetime() {
			return ass_due_datetime;
		}

		public void setAss_due_datetime(Date assDueDatetime) {
			ass_due_datetime = assDueDatetime;
		}

		public String getTpl_stylesheet() {
			return tpl_stylesheet;
		}

		public void setTpl_stylesheet(String tplStylesheet) {
			tpl_stylesheet = tplStylesheet;
		}

		public Long getRrq_req_res_id() {
			return rrq_req_res_id;
		}

		public void setRrq_req_res_id(Long rrqReqResId) {
			rrq_req_res_id = rrqReqResId;
		}

		public String getPre_res_title() {
			return pre_res_title;
		}

		public void setPre_res_title(String preResTitle) {
			pre_res_title = preResTitle;
		}

		public String getRrq_status() {
			return rrq_status;
		}

		public void setRrq_status(String rrqStatus) {
			rrq_status = rrqStatus;
		}

		public String getPre_mod_mov_status() {
			return pre_mod_mov_status;
		}

		public void setPre_mod_mov_status(String preModMovStatus) {
			pre_mod_mov_status = preModMovStatus;
		}

		public Long getExam_total() {
			return exam_total;
		}

		public void setExam_total(Long exam_total) {
			this.exam_total = exam_total;
		}

		public Long getTopic_total() {
			return topic_total;
		}

		public void setTopic_total(Long topic_total) {
			this.topic_total = topic_total;
		}

		public boolean isPre_mod_had_completed() {
			return pre_mod_had_completed;
		}

		public void setPre_mod_had_completed(boolean preModHadCompleted) {
			pre_mod_had_completed = preModHadCompleted;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public Resources getRes() {
			return res;
		}

		public void setRes(Resources res) {
			this.res = res;
		}

		public int getMod_mobile_ind() {
			return mod_mobile_ind;
		}

		public void setMod_mobile_ind(int mod_mobile_ind) {
			this.mod_mobile_ind = mod_mobile_ind;
		}

		public String getRrq_res_type() {
			return rrq_res_type;
		}

		public void setRrq_res_type(String rrq_res_type) {
			this.rrq_res_type = rrq_res_type;
		}

		public String getMod_test_style() {
			return mod_test_style;
		}

		public void setMod_test_style(String mod_test_style) {
			this.mod_test_style = mod_test_style;
		}
	
}