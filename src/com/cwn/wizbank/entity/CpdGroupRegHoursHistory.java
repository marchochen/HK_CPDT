package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

public class CpdGroupRegHoursHistory implements java.io.Serializable{

	private static final long serialVersionUID = -8000888203711089564L;
	
	private Long cghi_id ;
	private Long cghi_usr_ent_id ;
	private Long cghi_ct_id ;
	private Long cghi_cg_id ;
	private String cghi_license_type ;
	private String cghi_license_alias ;
	private Integer cghi_cal_before_ind ;
	private Integer cghi_recover_hours_period ;
	private String cghi_code ;
	private String cghi_alias;
	private Date cghi_initial_date ;
	private Date cghi_expiry_date;
	private Date cghi_cr_reg_date ;
	private Date cghi_cr_de_reg_date ;
	private Integer cghi_period ;
	private Integer cghi_first_ind ;
	private Date cghi_actual_date ;
	private Integer cghi_ct_starting_month ;
	private Date cghi_cgp_effective_time ;
	private Date cghi_cal_start_date;
	private Date cghi_cal_end_date ;
	private Float cghi_manul_core_hours;
	private Float cghi_manul_non_core_hours ;
	private Integer cghi_manul_ind ;
	private Float cghi_req_core_hours;
	private Float cghi_req_non_core_hours;
	private Float cghi_execute_core_hours ;
	private Float cghi_execute_non_core_hours;
	private Float cghi_award_core_hours;
	private Float cghi_award_non_core_hours ;
	private Date cghi_create_datetime ;
	private Date cghi_update_datetime ;
	private int cghi_cal_month;
	private String cghi_cr_reg_number;
	private Long cghi_cgp_id;
	private int cghi_cg_contain_non_core_ind;
	private Long cghi_cgr_id;
	
	//其他所需属性
    private List exportUserIds; //选择的用户ID
	private List exportGroupIds; //选择的用户组ID
	private List exportCpdTypeIds; //选择的大牌组ID
	private String sortOrderName; //排序字段
	private String sortOrderBy; //排序类型
	
	private List<CpdGroupRegHoursHistory> cpdGroupRegHoursHistoryList;
	private String usr_display_bil;
    private String usr_ste_usr_id;
	private String usg_name;
	private String upt_title;
	
	public Long getCghi_id() {
		return cghi_id;
	}
	public void setCghi_id(Long cghi_id) {
		this.cghi_id = cghi_id;
	}
	public Long getCghi_usr_ent_id() {
		return cghi_usr_ent_id;
	}
	public void setCghi_usr_ent_id(Long cghi_usr_ent_id) {
		this.cghi_usr_ent_id = cghi_usr_ent_id;
	}
	public Long getCghi_ct_id() {
		return cghi_ct_id;
	}
	public void setCghi_ct_id(Long cghi_ct_id) {
		this.cghi_ct_id = cghi_ct_id;
	}
	public Long getCghi_cg_id() {
		return cghi_cg_id;
	}
	public void setCghi_cg_id(Long cghi_cg_id) {
		this.cghi_cg_id = cghi_cg_id;
	}
	public String getCghi_license_type() {
		return cghi_license_type;
	}
	public void setCghi_license_type(String cghi_license_type) {
		this.cghi_license_type = cghi_license_type;
	}
	public String getCghi_license_alias() {
		return cghi_license_alias;
	}
	public void setCghi_license_alias(String cghi_license_alias) {
		this.cghi_license_alias = cghi_license_alias;
	}
	public Integer getCghi_cal_before_ind() {
		return cghi_cal_before_ind;
	}
	public void setCghi_cal_before_ind(Integer cghi_cal_before_ind) {
		this.cghi_cal_before_ind = cghi_cal_before_ind;
	}
	public Integer getCghi_recover_hours_period() {
		return cghi_recover_hours_period;
	}
	public void setCghi_recover_hours_period(Integer cghi_recover_hours_period) {
		this.cghi_recover_hours_period = cghi_recover_hours_period;
	}
	public String getCghi_code() {
		return cghi_code;
	}
	public void setCghi_code(String cghi_code) {
		this.cghi_code = cghi_code;
	}
	public String getCghi_alias() {
		return cghi_alias;
	}
	public void setCghi_alias(String cghi_alias) {
		this.cghi_alias = cghi_alias;
	}
	public Date getCghi_initial_date() {
		return cghi_initial_date;
	}
	public void setCghi_initial_date(Date cghi_initial_date) {
		this.cghi_initial_date = cghi_initial_date;
	}
	public Date getCghi_expiry_date() {
		return cghi_expiry_date;
	}
	public void setCghi_expiry_date(Date cghi_expiry_date) {
		this.cghi_expiry_date = cghi_expiry_date;
	}
	public Date getCghi_cr_reg_date() {
		return cghi_cr_reg_date;
	}
	public void setCghi_cr_reg_date(Date cghi_cr_reg_date) {
		this.cghi_cr_reg_date = cghi_cr_reg_date;
	}
	public Date getCghi_cr_de_reg_date() {
		return cghi_cr_de_reg_date;
	}
	public void setCghi_cr_de_reg_date(Date cghi_cr_de_reg_date) {
		this.cghi_cr_de_reg_date = cghi_cr_de_reg_date;
	}
	public Integer getCghi_period() {
		return cghi_period;
	}
	public void setCghi_period(Integer cghi_period) {
		this.cghi_period = cghi_period;
	}
	public Integer getCghi_first_ind() {
		return cghi_first_ind;
	}
	public void setCghi_first_ind(Integer cghi_first_ind) {
		this.cghi_first_ind = cghi_first_ind;
	}
	public Date getCghi_actual_date() {
		return cghi_actual_date;
	}
	public void setCghi_actual_date(Date cghi_actual_date) {
		this.cghi_actual_date = cghi_actual_date;
	}
	public Integer getCghi_ct_starting_month() {
		return cghi_ct_starting_month;
	}
	public void setCghi_ct_starting_month(Integer cghi_ct_starting_month) {
		this.cghi_ct_starting_month = cghi_ct_starting_month;
	}
	public Date getCghi_cgp_effective_time() {
		return cghi_cgp_effective_time;
	}
	public void setCghi_cgp_effective_time(Date cghi_cgp_effective_time) {
		this.cghi_cgp_effective_time = cghi_cgp_effective_time;
	}
	public Date getCghi_cal_start_date() {
		return cghi_cal_start_date;
	}
	public void setCghi_cal_start_date(Date cghi_cal_start_date) {
		this.cghi_cal_start_date = cghi_cal_start_date;
	}
	public Date getCghi_cal_end_date() {
		return cghi_cal_end_date;
	}
	public void setCghi_cal_end_date(Date cghi_cal_end_date) {
		this.cghi_cal_end_date = cghi_cal_end_date;
	}
	public Float getCghi_manul_core_hours() {
		return cghi_manul_core_hours;
	}
	public void setCghi_manul_core_hours(Float cghi_manul_core_hours) {
		this.cghi_manul_core_hours = cghi_manul_core_hours;
	}
	public Float getCghi_manul_non_core_hours() {
		return cghi_manul_non_core_hours;
	}
	public void setCghi_manul_non_core_hours(Float cghi_manul_non_core_hours) {
		this.cghi_manul_non_core_hours = cghi_manul_non_core_hours;
	}
	public Integer getCghi_manul_ind() {
		return cghi_manul_ind;
	}
	public void setCghi_manul_ind(Integer cghi_manul_ind) {
		this.cghi_manul_ind = cghi_manul_ind;
	}
	public Float getCghi_req_core_hours() {
		return cghi_req_core_hours;
	}
	public void setCghi_req_core_hours(Float cghi_req_core_hours) {
		this.cghi_req_core_hours = cghi_req_core_hours;
	}
	public Float getCghi_req_non_core_hours() {
		return cghi_req_non_core_hours;
	}
	public void setCghi_req_non_core_hours(Float cghi_req_non_core_hours) {
		this.cghi_req_non_core_hours = cghi_req_non_core_hours;
	}
	public Float getCghi_execute_core_hours() {
		return cghi_execute_core_hours;
	}
	public void setCghi_execute_core_hours(Float cghi_execute_core_hours) {
		this.cghi_execute_core_hours = cghi_execute_core_hours;
	}
	public Float getCghi_execute_non_core_hours() {
		return cghi_execute_non_core_hours;
	}
	public void setCghi_execute_non_core_hours(Float cghi_execute_non_core_hours) {
		this.cghi_execute_non_core_hours = cghi_execute_non_core_hours;
	}
	public Float getCghi_award_core_hours() {
		return cghi_award_core_hours;
	}
	public void setCghi_award_core_hours(Float cghi_award_core_hours) {
		this.cghi_award_core_hours = cghi_award_core_hours;
	}

	public Float getCghi_award_non_core_hours() {
		return cghi_award_non_core_hours;
	}
	public void setCghi_award_non_core_hours(Float cghi_award_non_core_hours) {
		this.cghi_award_non_core_hours = cghi_award_non_core_hours;
	}
	public Date getCghi_create_datetime() {
		return cghi_create_datetime;
	}
	public void setCghi_create_datetime(Date cghi_create_datetime) {
		this.cghi_create_datetime = cghi_create_datetime;
	}
	public Date getCghi_update_datetime() {
		return cghi_update_datetime;
	}
	public void setCghi_update_datetime(Date cghi_update_datetime) {
		this.cghi_update_datetime = cghi_update_datetime;
	}
	public int getCghi_cal_month() {
		return cghi_cal_month;
	}
	public void setCghi_cal_month(int cghi_cal_month) {
		this.cghi_cal_month = cghi_cal_month;
	}
	public String getCghi_cr_reg_number() {
		return cghi_cr_reg_number;
	}
	public void setCghi_cr_reg_number(String cghi_cr_reg_number) {
		this.cghi_cr_reg_number = cghi_cr_reg_number;
	}
	public Long getCghi_cgp_id() {
		return cghi_cgp_id;
	}
	public void setCghi_cgp_id(Long cghi_cgp_id) {
		this.cghi_cgp_id = cghi_cgp_id;
	}
	public int getCghi_cg_contain_non_core_ind() {
		return cghi_cg_contain_non_core_ind;
	}
	public void setCghi_cg_contain_non_core_ind(int cghi_cg_contain_non_core_ind) {
		this.cghi_cg_contain_non_core_ind = cghi_cg_contain_non_core_ind;
	}
	public Long getCghi_cgr_id() {
		return cghi_cgr_id;
	}
	public void setCghi_cgr_id(Long cghi_cgr_id) {
		this.cghi_cgr_id = cghi_cgr_id;
	}
	public List getExportUserIds() {
		return exportUserIds;
	}
	public void setExportUserIds(List exportUserIds) {
		this.exportUserIds = exportUserIds;
	}
	public List getExportGroupIds() {
		return exportGroupIds;
	}
	public void setExportGroupIds(List exportGroupIds) {
		this.exportGroupIds = exportGroupIds;
	}
	public List getExportCpdTypeIds() {
		return exportCpdTypeIds;
	}
	public void setExportCpdTypeIds(List exportCpdTypeIds) {
		this.exportCpdTypeIds = exportCpdTypeIds;
	}
	public String getSortOrderName() {
		return sortOrderName;
	}
	public void setSortOrderName(String sortOrderName) {
		this.sortOrderName = sortOrderName;
	}
	public String getSortOrderBy() {
		return sortOrderBy;
	}
	public void setSortOrderBy(String sortOrderBy) {
		this.sortOrderBy = sortOrderBy;
	}
	public List<CpdGroupRegHoursHistory> getCpdGroupRegHoursHistoryList() {
		return cpdGroupRegHoursHistoryList;
	}
	public void setCpdGroupRegHoursHistoryList(
			List<CpdGroupRegHoursHistory> cpdGroupRegHoursHistoryList) {
		this.cpdGroupRegHoursHistoryList = cpdGroupRegHoursHistoryList;
	}
	public String getUsr_display_bil() {
		return usr_display_bil;
	}
	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}
	public String getUsg_name() {
		return usg_name;
	}
	public void setUsg_name(String usg_name) {
		this.usg_name = usg_name;
	}
	public String getUpt_title() {
		return upt_title;
	}
	public void setUpt_title(String upt_title) {
		this.upt_title = upt_title;
	}
    public String getUsr_ste_usr_id() {
        return usr_ste_usr_id;
    }
    public void setUsr_ste_usr_id(String usr_ste_usr_id) {
        this.usr_ste_usr_id = usr_ste_usr_id;
    }
	
}
