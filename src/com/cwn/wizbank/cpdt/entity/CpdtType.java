package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Desc : 牌照(大牌)
 */
public class CpdtType implements Serializable{
	
	private static final long serialVersionUID = -5826477064988998669L;

	public static final int AWARD_HOURS_TYPE_ONCE = 1;///每门课程只可获取一次时数

	public static final int AWARD_HOURS_TYPE_PERIOD_ONCE = 2;///每门课程在该评估周期内可获取一次时数

	public static final int AWARD_HOURS_TYPE_FINAL = 3;///每门课程完成就可以获得
	
	public static final int CT_CAL_BEFORE_IND1 = 1;//在用户要求项目的开始日期前不追认学分时数

	public static final int CT_CAL_BEFORE_IND2 = 2;//2、追认用户在要求项目的开始日期于当前结算年度的学分时数。

	public static final int CT_TRIGGER_EMAIL_TYPE1 = 1;//1、手动(默认)

	public static final int CT_TRIGGER_EMAIL_TYPE2 = 2;//2、自动

	public static final int CT_SEND_REPORTS_SUPERVISORS1 = 1;//1、是

	public static final int CT_SEND_REPORTS_SUPERVISORS2 = 0;//0、否
	
	public static final int CT_WEB_COURSE1 = 1;//1、只要完成网上课程就可以获得完整的 CPD/T学分。

	public static final int CT_WEB_COURSE2 = 2;//2、获得 CPD/T 学分需要完成网上课程时长的 xx%根据这个规则，系统将按照网上课程需要学习的时间每 0.5 个 CPD/T 小时计算，并且根据此原则获得最多 CPD/T 学分。
	
	
	/**
	 * 主键
	 */
	private Long ct_id ;
	/**
	 * 牌照类别 
	 * (
	 * 牌照的唯一标识。每条记录都必须唯一，在添加修改时，
	 * 如果发现与其它记录有重复，则提示“牌照类别重复”。适用于“编号”的长度规则。
	 * )
	 */
	private String ct_license_type ;
	/**
	 * 别名
	 * (
	 * 牌照的名称。每条记录都必须唯一，在课程及报表相关页面都是显示这个别名。
	 * 在添加修改时，如果发现与其它记录有重复，则提示“别名重复”。适用于“标题”的长度规则。
	 * )
	 */
	private String ct_license_alias ;
	/**
	 * 周期的开始月份。提供1到12月份供用户选。默认选中1月。
	 */
	private Integer ct_starting_month ;
	/**
	 * 显示顺序
	 */
	private Integer ct_display_order ;
	/**
	 * 获得CPT/D时数类型
	 * (
	 * 1、每门课程只可获取一次时数  
	 * 2、每门课程在该评估周期内可获取一次时数  
	 * 3、每当完成本门课程都可获取时数
	 * )
	 */
	private Integer ct_award_hours_type ;
	/**
	 * 新挂牌学员学分追认
	 * 1、在用户要求项目的开始日期前不追认学分时数
	 * 2、追认用户在要求项目的开始日期于当前结算年度的学分时数。
	 */
	private Integer ct_cal_before_ind;
	/**
	 * CPT/D未完成时数提示邮件触发方式
	 * (
	 * 1、手动(默认)
	 * 2、自动
	 * )
	 */
	private Integer ct_trigger_email_type ;
	/**
	 * 当CPT/D未完成时数提示邮件触发方式选择自动时，自动发送的第一个月份(1-12)
	 */
	private Integer ct_trigger_email_month_1 ;
	/**
	 * 当CPT/D未完成时数提示邮件触发方式选择自动时，自动发送的第一个日期(1-31)
	 */
	private Integer ct_trigger_email_date_1 ;
	/**
	 * 当CPT/D未完成时数提示邮件触发方式选择自动时，自动发送的第二个月份(1-12)
	 */
	private Integer ct_trigger_email_month_2 ;
	/**
	 * 当CPT/D未完成时数提示邮件触发方式选择自动时，自动发送的第二个日期(1-31)
	 */
	private Integer ct_trigger_email_date_2 ;
	/**
	 * 当CPT/D未完成时数提示邮件触发方式选择自动时，自动发送的第三个月份(1-12)
	 */
	private Integer ct_trigger_email_month_3 ;
	/**
	 * 当CPT/D未完成时数提示邮件触发方式选择自动时，自动发送的第三个日期(1-31)
	 */
	private Integer ct_trigger_email_date_3 ;
	/**
	 * 番生指数(天数)
	 * (
	 * 当牌照重新挂牌时，追溯上一次除牌相隔日数上限。
	 * 这里可只可以输入小于等于999的正整数。(hksi目前貌似还没用到这个字段)
	 * )
	 */
	private Integer ct_recover_hours_period ;
	/**
	 * 上次邮件发送时间
	 */
	private Date ct_last_email_send_time ;
	/**
	 * 创建用户ID reguser.usr_ent_id
	 */
	private Long ct_create_usr_ent_id ;
	/**
	 * 创建时间
	 */
	private Date ct_create_datetime ;
	/**
	 * 更新用户ID reguser.usr_ent_id
	 */
	private Long ct_update_usr_ent_id ;
	/**
	 * 更新时间
	 */
	private Date ct_update_datetime ;
	/**
	 * 状态 (OK : 正常 DEL：已删除)
	 */
	private String ct_status;
	/**
	 * 同时发送提示给相关学员的直属上司
	 * 1、是
	 * 0、否
	 */
	private Long ct_send_reports_supervisors;
	/**
	 * 网上课程学习时间
	 * 1、只要完成网上课程就可以获得完整的 CPD/T学分。
	 * 2、获得 CPD/T 学分需要完成网上课程时长的 xx%根据这个规则，
	 * 系统将按照网上课程需要学习的时间每 0.5 个 CPD/T 小时计算，并且根据此原则获得最多 CPD/T 学分。	
	 */
	private Long ct_web_course;
	/**
	 * 网上课程学习时间时长(1-100%)
	 */
	private Long ct_web_reached;
	
	public Long getCt_id() {
		return ct_id;
	}
	public void setCt_id(Long ct_id) {
		this.ct_id = ct_id;
	}
	public String getCt_license_type() {
		return ct_license_type;
	}
	public void setCt_license_type(String ct_license_type) {
		this.ct_license_type = ct_license_type;
	}
	public String getCt_license_alias() {
		return ct_license_alias;
	}
	public void setCt_license_alias(String ct_license_alias) {
		this.ct_license_alias = ct_license_alias;
	}
	public Integer getCt_starting_month() {
		return ct_starting_month;
	}
	public void setCt_starting_month(Integer ct_starting_month) {
		this.ct_starting_month = ct_starting_month;
	}
	public Integer getCt_display_order() {
		return ct_display_order;
	}
	public void setCt_display_order(Integer ct_display_order) {
		this.ct_display_order = ct_display_order;
	}
	public Integer getCt_award_hours_type() {
		return ct_award_hours_type;
	}
	public void setCt_award_hours_type(Integer ct_award_hours_type) {
		this.ct_award_hours_type = ct_award_hours_type;
	}
	public Integer getCt_cal_before_ind() {
		return ct_cal_before_ind;
	}
	public void setCt_cal_before_ind(Integer ct_cal_before_ind) {
		this.ct_cal_before_ind = ct_cal_before_ind;
	}
	public Integer getCt_trigger_email_type() {
		return ct_trigger_email_type;
	}
	public void setCt_trigger_email_type(Integer ct_trigger_email_type) {
		this.ct_trigger_email_type = ct_trigger_email_type;
	}
	public Integer getCt_trigger_email_month_1() {
		return ct_trigger_email_month_1;
	}
	public void setCt_trigger_email_month_1(Integer ct_trigger_email_month_1) {
		this.ct_trigger_email_month_1 = ct_trigger_email_month_1;
	}
	public Integer getCt_trigger_email_date_1() {
		return ct_trigger_email_date_1;
	}
	public void setCt_trigger_email_date_1(Integer ct_trigger_email_date_1) {
		this.ct_trigger_email_date_1 = ct_trigger_email_date_1;
	}
	public Integer getCt_trigger_email_month_2() {
		return ct_trigger_email_month_2;
	}
	public void setCt_trigger_email_month_2(Integer ct_trigger_email_month_2) {
		this.ct_trigger_email_month_2 = ct_trigger_email_month_2;
	}
	public Integer getCt_trigger_email_date_2() {
		return ct_trigger_email_date_2;
	}
	public void setCt_trigger_email_date_2(Integer ct_trigger_email_date_2) {
		this.ct_trigger_email_date_2 = ct_trigger_email_date_2;
	}
	public Integer getCt_trigger_email_month_3() {
		return ct_trigger_email_month_3;
	}
	public void setCt_trigger_email_month_3(Integer ct_trigger_email_month_3) {
		this.ct_trigger_email_month_3 = ct_trigger_email_month_3;
	}
	public Integer getCt_trigger_email_date_3() {
		return ct_trigger_email_date_3;
	}
	public void setCt_trigger_email_date_3(Integer ct_trigger_email_date_3) {
		this.ct_trigger_email_date_3 = ct_trigger_email_date_3;
	}
	public Integer getCt_recover_hours_period() {
		return ct_recover_hours_period;
	}
	public void setCt_recover_hours_period(Integer ct_recover_hours_period) {
		this.ct_recover_hours_period = ct_recover_hours_period;
	}
	public Date getCt_last_email_send_time() {
		return ct_last_email_send_time;
	}
	public void setCt_last_email_send_time(Date ct_last_email_send_time) {
		this.ct_last_email_send_time = ct_last_email_send_time;
	}
	public Long getCt_create_usr_ent_id() {
		return ct_create_usr_ent_id;
	}
	public void setCt_create_usr_ent_id(Long ct_create_usr_ent_id) {
		this.ct_create_usr_ent_id = ct_create_usr_ent_id;
	}
	public Date getCt_create_datetime() {
		return ct_create_datetime;
	}
	public void setCt_create_datetime(Date ct_create_datetime) {
		this.ct_create_datetime = ct_create_datetime;
	}
	public Long getCt_update_usr_ent_id() {
		return ct_update_usr_ent_id;
	}
	public void setCt_update_usr_ent_id(Long ct_update_usr_ent_id) {
		this.ct_update_usr_ent_id = ct_update_usr_ent_id;
	}
	public Date getCt_update_datetime() {
		return ct_update_datetime;
	}
	public void setCt_update_datetime(Date ct_update_datetime) {
		this.ct_update_datetime = ct_update_datetime;
	}
	public String getCt_status() {
		return ct_status;
	}
	public void setCt_status(String ct_status) {
		this.ct_status = ct_status;
	}
	public Long getCt_send_reports_supervisors() {
		return ct_send_reports_supervisors;
	}
	public void setCt_send_reports_supervisors(Long ct_send_reports_supervisors) {
		this.ct_send_reports_supervisors = ct_send_reports_supervisors;
	}
	public Long getCt_web_course() {
		return ct_web_course;
	}
	public void setCt_web_course(Long ct_web_course) {
		this.ct_web_course = ct_web_course;
	}
	public Long getCt_web_reached() {
		return ct_web_reached;
	}
	public void setCt_web_reached(Long ct_web_reached) {
		this.ct_web_reached = ct_web_reached;
	}

	@Override
	public String toString() {
		return "CpdType [ct_id=" + ct_id + ", ct_license_type="
				+ ct_license_type + ", ct_license_alias=" + ct_license_alias
				+ ", ct_starting_month=" + ct_starting_month
				+ ", ct_display_order=" + ct_display_order
				+ ", ct_award_hours_type=" + ct_award_hours_type
				+ ", ct_cal_before_ind=" + ct_cal_before_ind
				+ ", ct_trigger_email_type=" + ct_trigger_email_type
				+ ", ct_trigger_email_month_1=" + ct_trigger_email_month_1
				+ ", ct_trigger_email_date_1=" + ct_trigger_email_date_1
				+ ", ct_trigger_email_month_2=" + ct_trigger_email_month_2
				+ ", ct_trigger_email_date_2=" + ct_trigger_email_date_2
				+ ", ct_trigger_email_month_3=" + ct_trigger_email_month_3
				+ ", ct_trigger_email_date_3=" + ct_trigger_email_date_3
				+ ", ct_recover_hours_period=" + ct_recover_hours_period
				+ ", ct_last_email_send_time=" + ct_last_email_send_time
				+ ", ct_create_usr_ent_id=" + ct_create_usr_ent_id
				+ ", ct_create_datetime=" + ct_create_datetime
				+ ", ct_update_usr_ent_id=" + ct_update_usr_ent_id
				+ ", ct_update_datetime=" + ct_update_datetime + ", ct_status="
				+ ct_status + ", ct_send_reports_supervisors="
				+ ct_send_reports_supervisors + ", ct_web_course="
				+ ct_web_course + ", ct_web_reached=" + ct_web_reached
			    + "]";
	}
	
	

	
	
}
