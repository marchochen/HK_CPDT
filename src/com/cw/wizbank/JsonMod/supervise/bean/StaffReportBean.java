package com.cw.wizbank.JsonMod.supervise.bean;

import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.commonBean.GoldenManOptionBean;

public class StaffReportBean {
	private int rsp_id;
	private String rsp_title;
	private String rsp_title_noescape;
	private String rte_type;
	private Timestamp rsp_upd_timestamp;
	private String rte_exe_xsl;	
	
	private String s_usg_ent_id_lst;
	
	private GoldenManOptionBean gmUsrOption;
	private GoldenManOptionBean gmItmOption;
	private GoldenManOptionBean gmTndOption;
	private Timestamp att_create_start_datetime;
	private Timestamp att_create_end_datetime;
	private int ats_id;

	
	public Timestamp getRsp_upd_timestamp() {
		return rsp_upd_timestamp;
	}
	public void setRsp_upd_timestamp(Timestamp rsp_upd_timestamp) {
		this.rsp_upd_timestamp = rsp_upd_timestamp;
	}
	public String getRte_type() {
		return rte_type;
	}
	public void setRte_type(String rte_type) {
		this.rte_type = rte_type;
	}

	public int getAts_id() {
		return ats_id;
	}
	public void setAts_id(int ats_id) {
		this.ats_id = ats_id;
	}

	public Timestamp getAtt_create_end_datetime() {
		return att_create_end_datetime;
	}
	public void setAtt_create_end_datetime(Timestamp att_create_end_datetime) {
		this.att_create_end_datetime = att_create_end_datetime;
	}

	public Timestamp getAtt_create_start_datetime() {
		return att_create_start_datetime;
	}
	public void setAtt_create_start_datetime(Timestamp att_create_start_datetime) {
		this.att_create_start_datetime = att_create_start_datetime;
	}

	public int getRsp_id() {
		return rsp_id;
	}
	public void setRsp_id(int rsp_id) {
		this.rsp_id = rsp_id;
	}
	public String getRsp_title() {
		return rsp_title;
	}
	public void setRsp_title(String rsp_title) {
		this.rsp_title = rsp_title;
	}
	public String getRsp_title_noescape() {
		return rsp_title_noescape;
	}
	public void setRsp_title_noescape(String rsp_title_noescape) {
		this.rsp_title_noescape = rsp_title_noescape;
	}
	public String getRte_exe_xsl() {
		return rte_exe_xsl;
	}
	public void setRte_exe_xsl(String rte_exe_xsl) {
		this.rte_exe_xsl = rte_exe_xsl;
	}
	public GoldenManOptionBean getGmItmOption() {
		return gmItmOption;
	}
	public void setGmItmOption(GoldenManOptionBean gmItmOption) {
		this.gmItmOption = gmItmOption;
	}
	public GoldenManOptionBean getGmTndOption() {
		return gmTndOption;
	}
	public void setGmTndOption(GoldenManOptionBean gmTndOption) {
		this.gmTndOption = gmTndOption;
	}
	public GoldenManOptionBean getGmUsrOption() {
		return gmUsrOption;
	}
	public void setGmUsrOption(GoldenManOptionBean gmUsrOption) {
		this.gmUsrOption = gmUsrOption;
	}
	public String getS_usg_ent_id_lst() {
		return s_usg_ent_id_lst;
	}
	public void setS_usg_ent_id_lst(String s_usg_ent_id_lst) {
		this.s_usg_ent_id_lst = s_usg_ent_id_lst;
	}


}
