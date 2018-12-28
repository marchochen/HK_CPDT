package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

import com.cwn.wizbank.cpd.vo.CpdPeriodVO;

public class CpdType implements java.io.Serializable {
	
	private static final long serialVersionUID = 1598001027499313218L;
	
	public static final int AWARD_HOURS_TYPE_ONCE = 1;///每门课程只可获取一次时数
	
	public static final int AWARD_HOURS_TYPE_PERIOD_ONCE = 2;///每门课程在该评估周期内可获取一次时数
	
	public static final int AWARD_HOURS_TYPE_FINAL = 3;///每门课程完成就可以获得
	
	private Long ct_id ;
	private String ct_license_type ;
	private String ct_license_alias ;
	private Integer ct_starting_month ;
	private Integer ct_display_order ;
	private Integer ct_award_hours_type ;
	private Integer ct_cal_before_ind;
	private Integer ct_trigger_email_type ;
	private Integer ct_trigger_email_month_1 ;
	private Integer ct_trigger_email_date_1 ;
	private Integer ct_trigger_email_month_2 ;
	private Integer ct_trigger_email_date_2 ;
	private Integer ct_trigger_email_month_3 ;
	private Integer ct_trigger_email_date_3 ;
	private Integer ct_recover_hours_period ;
	private Date ct_last_email_send_time ;
	private Long ct_create_usr_ent_id ;
	private Date ct_create_datetime ;
	private Long ct_update_usr_ent_id ;
	private Date ct_update_datetime ;
	private String ct_status;
	
	CpdPeriodVO cpdPeriodVO;
	//额外添加的属性
 	private List<CpdGroup> cpdGrouplist;
	
	
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
	public Date getCt_last_email_send_time() {
		return ct_last_email_send_time;
	}
	public void setCt_last_email_send_time(Date ct_last_email_send_time) {
		this.ct_last_email_send_time = ct_last_email_send_time;
	}
    public CpdPeriodVO getCpdPeriodVO() {
        return cpdPeriodVO;
    }
    public void setCpdPeriodVO(CpdPeriodVO cpdPeriodVO) {
        this.cpdPeriodVO = cpdPeriodVO;
    }
	public List<CpdGroup> getCpdGrouplist() {
		return cpdGrouplist;
	}
	public void setCpdGrouplist(List<CpdGroup> cpdGrouplist) {
		this.cpdGrouplist = cpdGrouplist;
	}
	

	
}
