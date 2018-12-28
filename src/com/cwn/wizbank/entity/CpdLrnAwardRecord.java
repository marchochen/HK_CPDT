package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class CpdLrnAwardRecord implements java.io.Serializable{

	private static final long serialVersionUID = -4732197406983842986L;

	private Long clar_id ;
	private Long clar_usr_ent_id ;
	private Long clar_itm_id ;
	private Long clar_app_id ;
	private int clar_manul_ind ;
	private Long clar_ct_id ;
	private Long clar_cg_id ;
	private Long clar_acgi_id ;
	private Float clar_award_core_hours ;
	private Float clar_award_non_core_hours ;
	@DateTimeFormat(pattern="yyyy-MM-dd") 
	private Date clar_award_datetime ;
	private Long clar_create_usr_ent_id ;
	private Date clar_create_datetime ;
	private Long clar_update_usr_ent_id ;
	private Date clar_update_datetime ;
	
	/**格外添加的属性*/
	private List<CpdLrnAwardRecord> cpdLrnAwardRecordList;
	private String usr_display_bil;
	private String usr_ste_usr_id;
	private String usg_name;
	private String cg_alias; //小牌名称
	private Boolean show_award_core_hours; //课程设置核心时数大于0为true
	private Boolean show_award_non_core_hours; //课程设置非核心时数大于0 且小牌设置拥有非核心时数为true
	
	 private List exportUserIds; //选择的用户ID
 	 private List exportGroupIds; //选择的用户组ID
	 private List exportCpdTypeIds; //选择的大牌组ID
	 private String sortOrderName; //排序字段
	 private String sortOrderBy; //排序类型
	 private String upt_title;
	 private Integer period;  //周期
	 
	 private AeItem aeItem;
	 private AeItemCPDGourpItem aeItemCPDGourpItem;
	 
	public CpdLrnAwardRecord(){
		
	}
	
	public CpdLrnAwardRecord(Long clar_usr_ent_id , Long clar_itm_id ,Long clar_app_id ,int clar_manul_ind,
			Long clar_ct_id,Long clar_cg_id ,Long clar_acgi_id ,
			Float clar_award_core_hours , Float clar_award_non_core_hours ,Date clar_award_datetime ,
			Long clar_create_usr_ent_id ,Long clar_update_usr_ent_id ){
		this.clar_usr_ent_id = clar_usr_ent_id;
		this.clar_itm_id = clar_itm_id;
		this.clar_app_id = clar_app_id;
		this.clar_manul_ind = clar_manul_ind;
		this.clar_ct_id = clar_ct_id;
		this.clar_cg_id = clar_cg_id;
		this.clar_acgi_id = clar_acgi_id;
		this.clar_award_core_hours = clar_award_core_hours;
		this.clar_award_non_core_hours = clar_award_non_core_hours;
		this.clar_award_datetime = clar_award_datetime;
		this.clar_create_usr_ent_id = clar_create_usr_ent_id;
		this.clar_create_datetime = new Date();
		this.clar_update_usr_ent_id = clar_update_usr_ent_id;
		this.clar_update_datetime = this.clar_create_datetime;
	}
	
	public Long getClar_id() {
		return clar_id;
	}
	public void setClar_id(Long clar_id) {
		this.clar_id = clar_id;
	}
	public Long getClar_usr_ent_id() {
		return clar_usr_ent_id;
	}
	public void setClar_usr_ent_id(Long clar_usr_ent_id) {
		this.clar_usr_ent_id = clar_usr_ent_id;
	}
	public Long getClar_itm_id() {
		return clar_itm_id;
	}
	public void setClar_itm_id(Long clar_itm_id) {
		this.clar_itm_id = clar_itm_id;
	}
	public Long getClar_app_id() {
		return clar_app_id;
	}
	public void setClar_app_id(Long clar_app_id) {
		this.clar_app_id = clar_app_id;
	}
	public int getClar_manul_ind() {
		return clar_manul_ind;
	}
	public void setClar_manul_ind(int clar_manul_ind) {
		this.clar_manul_ind = clar_manul_ind;
	}

	public Long getClar_ct_id() {
		return clar_ct_id;
	}
	public void setClar_ct_id(Long clar_ct_id) {
		this.clar_ct_id = clar_ct_id;
	}
	public Long getClar_cg_id() {
		return clar_cg_id;
	}
	public void setClar_cg_id(Long clar_cg_id) {
		this.clar_cg_id = clar_cg_id;
	}
	public Float getClar_award_core_hours() {
		return clar_award_core_hours;
	}
	public void setClar_award_core_hours(Float clar_award_core_hours) {
		this.clar_award_core_hours = clar_award_core_hours;
	}
	public Float getClar_award_non_core_hours() {
		return clar_award_non_core_hours;
	}
	public void setClar_award_non_core_hours(Float clar_award_non_core_hours) {
		this.clar_award_non_core_hours = clar_award_non_core_hours;
	}
	public Date getClar_award_datetime() {
		return clar_award_datetime;
	}
	public void setClar_award_datetime(Date clar_award_datetime) {
		this.clar_award_datetime = clar_award_datetime;
	}
	public Long getClar_create_usr_ent_id() {
		return clar_create_usr_ent_id;
	}
	public void setClar_create_usr_ent_id(Long clar_create_usr_ent_id) {
		this.clar_create_usr_ent_id = clar_create_usr_ent_id;
	}
	public Date getClar_create_datetime() {
		return clar_create_datetime;
	}
	public void setClar_create_datetime(Date clar_create_datetime) {
		this.clar_create_datetime = clar_create_datetime;
	}
	public Long getClar_update_usr_ent_id() {
		return clar_update_usr_ent_id;
	}
	public void setClar_update_usr_ent_id(Long clar_update_usr_ent_id) {
		this.clar_update_usr_ent_id = clar_update_usr_ent_id;
	}
	public Date getClar_update_datetime() {
		return clar_update_datetime;
	}
	public void setClar_update_datetime(Date clar_update_datetime) {
		this.clar_update_datetime = clar_update_datetime;
	}
	public Long getClar_acgi_id() {
		return clar_acgi_id;
	}
	public void setClar_acgi_id(Long clar_acgi_id) {
		this.clar_acgi_id = clar_acgi_id;
	}

	public List<CpdLrnAwardRecord> getCpdLrnAwardRecordList() {
		return cpdLrnAwardRecordList;
	}

	public void setCpdLrnAwardRecordList(
			List<CpdLrnAwardRecord> cpdLrnAwardRecordList) {
		this.cpdLrnAwardRecordList = cpdLrnAwardRecordList;
	}

	public String getUsr_display_bil() {
		return usr_display_bil;
	}

	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}

	public String getUsr_ste_usr_id() {
		return usr_ste_usr_id;
	}

	public void setUsr_ste_usr_id(String usr_ste_usr_id) {
		this.usr_ste_usr_id = usr_ste_usr_id;
	}

	public String getUsg_name() {
		return usg_name;
	}

	public void setUsg_name(String usg_name) {
		this.usg_name = usg_name;
	}

	public String getCg_alias() {
		return cg_alias;
	}

	public void setCg_alias(String cg_alias) {
		this.cg_alias = cg_alias;
	}

	public Boolean getShow_award_core_hours() {
		return show_award_core_hours;
	}

	public void setShow_award_core_hours(Boolean show_award_core_hours) {
		this.show_award_core_hours = show_award_core_hours;
	}

	public Boolean getShow_award_non_core_hours() {
		return show_award_non_core_hours;
	}

	public void setShow_award_non_core_hours(Boolean show_award_non_core_hours) {
		this.show_award_non_core_hours = show_award_non_core_hours;
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

	public String getUpt_title() {
		return upt_title;
	}

	public void setUpt_title(String upt_title) {
		this.upt_title = upt_title;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public AeItem getAeItem() {
		return aeItem;
	}

	public void setAeItem(AeItem aeItem) {
		this.aeItem = aeItem;
	}

	public AeItemCPDGourpItem getAeItemCPDGourpItem() {
		return aeItemCPDGourpItem;
	}

	public void setAeItemCPDGourpItem(AeItemCPDGourpItem aeItemCPDGourpItem) {
		this.aeItemCPDGourpItem = aeItemCPDGourpItem;
	}
}
