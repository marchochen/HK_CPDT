package com.cw.wizbank.JsonMod.study;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;

/**
 * URL请求参数信息
 * 
 * @author kimyu
 */
public class StudyModuleParam extends BaseParam {

	private long usr_ent_id;
	private long itm_id;
	private String type; // 判别是学习中心还是考试中心(可选值："COS"、"EXAM")

	// 更新数据表后跳转页面URL
	private String url_success;
	private String url_failure;

	// 学些计划过滤参数
	private String plan_status;
	private String start_time;
	private String end_time;

	// 以下是搜索参数
	private String srh_key; // 搜索关键词
	private String srh_key_type; // 是否全文搜索
	private String[] srh_itm_type_lst; // 课程类型选项
	private String[] srh_status_lst; // 课程状态选项
	private String[] srh_att_status_lst;// 结训状态
	private String srh_range; // 分辨是从我的全部课程页搜索还是从我的历史课程搜索(可选值："ALL","HIS")

	// 以下是时间期限搜索参数的可选值：
	// 即时开始 IMMEDIATE、最近一周 LAST_1_WEEK、最近两周 LAST_2_WEEK
	// 最的一个月 LAST_1_MONTH、最的两个月 LAST_2_MONTH、不限 UNLIMITED
	private String srh_last_acc_period; // 上次访问参与期限
	private String srh_admitted_period; // 录取时间的期限
	private String srh_end_period; // 课程结束时间的期限

	// update学习计划的时间。
	// private Date start_datetime;
	// private Date end_datetime;
	private String lsn_start_datetime;
	private String lsn_end_datetime;

	private boolean pending_plan = false; // 是否要查询待计划的推荐培训

	private int rec_num = 5; // "培训计划"页面显示待计划的推荐培训的数目

	public String getPlan_status() {
		return plan_status;
	}

	public void setPlan_status(String plan_status) {
		this.plan_status = plan_status;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		if ("".equals(start_time)) {
			this.start_time = null;
		} else {
			this.start_time = start_time;
		}
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		if ("".equals(end_time)) {
			this.end_time = null;
		} else {
			this.end_time = end_time;
		}
	}

	public String getUrl_success() {
		return url_success;
	}

	public void setUrl_success(String url_success) {
		this.url_success = url_success;
	}

	public String getUrl_failure() {
		return url_failure;
	}

	public void setUrl_failure(String url_failure) {
		this.url_failure = url_failure;
	}

	public long getItm_id() {
		return itm_id;
	}

	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}

	public long getUsr_ent_id() {
		return usr_ent_id;
	}

	public void setUsr_ent_id(long usr_ent_id) {

		this.usr_ent_id = usr_ent_id;
	}

	public boolean isPending_plan() {
		return pending_plan;
	}

	public void setPending_plan(boolean pending_plan) {
		this.pending_plan = pending_plan;
	}

	public String getSrh_key() {
		return srh_key;
	}

	public void setSrh_key(String srh_key) throws cwException {
		this.srh_key = srh_key;
	}

	public String getSrh_key_type() {
		return srh_key_type;
	}

	public void setSrh_key_type(String srh_key_type) {
		this.srh_key_type = srh_key_type;
	}

	public String[] getSrh_itm_type_lst() {
		return srh_itm_type_lst;
	}

	public void setSrh_itm_type_lst(String[] srh_itm_type_lst) {
		this.srh_itm_type_lst = srh_itm_type_lst;
	}

	public String[] getSrh_status_lst() {
		return srh_status_lst;
	}

	public void setSrh_status_lst(String[] srh_status_lst) {
		this.srh_status_lst = srh_status_lst;
	}

	public String[] getSrh_att_status_lst() {
		return srh_att_status_lst;
	}

	public void setSrh_att_status_lst(String[] srh_att_status_lst) {
		this.srh_att_status_lst = srh_att_status_lst;
	}

	public String getSrh_last_acc_period() {
		return srh_last_acc_period;
	}

	public void setSrh_last_acc_period(String srh_last_acc_period) {
		this.srh_last_acc_period = srh_last_acc_period;
	}

	public String getSrh_admitted_period() {
		return srh_admitted_period;
	}

	public void setSrh_admitted_period(String srh_admitted_period) {
		this.srh_admitted_period = srh_admitted_period;
	}

	public String getSrh_end_period() {
		return srh_end_period;
	}

	public void setSrh_end_period(String srh_end_period) {
		this.srh_end_period = srh_end_period;
	}

	public String getSrh_range() {
		return srh_range;
	}

	public void setSrh_range(String srh_range) {
		this.srh_range = srh_range;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLsn_start_datetime() {
		return lsn_start_datetime;
	}

	public void setLsn_start_datetime(String lsn_start_datetime) {

		if (lsn_start_datetime != null && lsn_start_datetime.length() > 0 && lsn_start_datetime.trim().indexOf(" ") == -1) {
			lsn_start_datetime += " 00:00:00.0";
		} else {
			lsn_start_datetime = null;
		}

		this.lsn_start_datetime = lsn_start_datetime;
	}

	public String getLsn_end_datetime() {
		return lsn_end_datetime;
	}

	public void setLsn_end_datetime(String lsn_end_datetime) {

		if (lsn_end_datetime != null && lsn_end_datetime.length() > 0 && lsn_end_datetime.trim().indexOf(" ") == -1) {
			lsn_end_datetime += " 00:00:00.0";
		} else {
			lsn_end_datetime = null;
		}

		this.lsn_end_datetime = lsn_end_datetime;
	}

	public int getRec_num() {
		return rec_num;
	}

	public void setRec_num(int rec_num) {
		this.rec_num = rec_num;
	}

}
