package com.cwn.wizbank.cpd.vo;

import java.util.Date;
import java.util.List;

public class IndividualReportVo implements java.io.Serializable{

	private static final long serialVersionUID = -6416015149656752315L;

	private Long itm_id;
	
	private String itm_title;
	
	private String accreditation_code;
	
	private Date award_datetime;
	
	private Long app_id;
	
	private Integer period;
	
	private Long usr_ent_id;
	
	private Long ct_id;
	
	private Long cghi_id;
	
	private Long cr_id;
	
	private List<CpdGroupSumVo> cpd_group_list;

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

	public List<CpdGroupSumVo> getCpd_group_list() {
		return cpd_group_list;
	}

	public void setCpd_group_list(List<CpdGroupSumVo> cpd_group_list) {
		this.cpd_group_list = cpd_group_list;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Long getUsr_ent_id() {
		return usr_ent_id;
	}

	public void setUsr_ent_id(Long usr_ent_id) {
		this.usr_ent_id = usr_ent_id;
	}

	public Long getCt_id() {
		return ct_id;
	}

	public void setCt_id(Long ct_id) {
		this.ct_id = ct_id;
	}

	public String getAccreditation_code() {
		return accreditation_code;
	}

	public void setAccreditation_code(String accreditation_code) {
		this.accreditation_code = accreditation_code;
	}

	public Date getAward_datetime() {
		return award_datetime;
	}

	public void setAward_datetime(Date award_datetime) {
		this.award_datetime = award_datetime;
	}

	public Long getApp_id() {
		return app_id;
	}

	public void setApp_id(Long app_id) {
		this.app_id = app_id;
	}

	public Long getCghi_id() {
		return cghi_id;
	}

	public void setCghi_id(Long cghi_id) {
		this.cghi_id = cghi_id;
	}

	public Long getCr_id() {
		return cr_id;
	}

	public void setCr_id(Long cr_id) {
		this.cr_id = cr_id;
	}

}


