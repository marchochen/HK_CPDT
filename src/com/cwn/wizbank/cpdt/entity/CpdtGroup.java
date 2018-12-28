package com.cwn.wizbank.cpdt.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * Desc : CPT/D牌照组別（小牌）
 */
public class CpdtGroup implements Serializable{
	
	private static final long serialVersionUID = 7632954346784134710L;
	/**
	 * 主键
	 */
	private Long cg_id ;
	/**
	 * 组别编号(组别的唯一标识)
	 */
	private String cg_code ;
	/**
	 * 组别的名称
	 */
	private String cg_alias;
	/**
	 * 显示次序(最大输入两位正整数)
	 */
	private Integer cg_display_order;
	/**
	 * 是否拥有核心活动和非核心活动时数(是：该小牌同时要求学员要获得核心时数及非核心时数。在时数要求设置页面，课程CPD时数设置页面，
	 * “非核心时数”对应的输入框可以编辑。 否：该小牌不用要求非核心时数。在时数要求设置页面，课程CPD时数设置页面，
	 * “非核心时数”对应的输入框不可以编辑。)
	 * (1、是    2、否(默认))
	 */
	private Integer cg_contain_non_core_ind;
	/**
	 * 是否在报表显示(1、是(默认) 2、否)
	 */
	private Integer cg_display_in_report_ind;
	/**
	 * 所属于大牌的ID cpdType.ct_id
	 */
	private Long cg_ct_id;
	/**
	 * 创建用户ID reguser.usr_ent_id
	 */
	private Long cg_create_usr_ent_id;
	/**
	 * 创建时间
	 */
	private Date cg_create_datetime;
	/**
	 * 更新用户ID reguser.usr_ent_id
	 */
	private Long cg_update_usr_ent_id;
	/**
	 * 更新时间
	 */
	private Date cg_update_datetime;
	/**
	 *  状态(OK : 正常 DEL：已删除)
	 */
	private String cg_status;

	public Long getCg_id() {
		return cg_id;
	}
	public void setCg_id(Long cg_id) {
		this.cg_id = cg_id;
	}
	public String getCg_code() {
		return cg_code;
	}
	public void setCg_code(String cg_code) {
		this.cg_code = cg_code;
	}
	public String getCg_alias() {
		return cg_alias;
	}
	public void setCg_alias(String cg_alias) {
		this.cg_alias = cg_alias;
	}
	public Integer getCg_display_order() {
		return cg_display_order;
	}
	public void setCg_display_order(Integer cg_display_order) {
		this.cg_display_order = cg_display_order;
	}
	public Integer getCg_contain_non_core_ind() {
		return cg_contain_non_core_ind;
	}
	public void setCg_contain_non_core_ind(Integer cg_contain_non_core_ind) {
		this.cg_contain_non_core_ind = cg_contain_non_core_ind;
	}
	public Integer getCg_display_in_report_ind() {
		return cg_display_in_report_ind;
	}
	public void setCg_display_in_report_ind(Integer cg_display_in_report_ind) {
		this.cg_display_in_report_ind = cg_display_in_report_ind;
	}
	public Long getCg_ct_id() {
		return cg_ct_id;
	}
	public void setCg_ct_id(Long cg_ct_id) {
		this.cg_ct_id = cg_ct_id;
	}
	public Long getCg_create_usr_ent_id() {
		return cg_create_usr_ent_id;
	}
	public void setCg_create_usr_ent_id(Long cg_create_usr_ent_id) {
		this.cg_create_usr_ent_id = cg_create_usr_ent_id;
	}
	public Date getCg_create_datetime() {
		return cg_create_datetime;
	}
	public void setCg_create_datetime(Date cg_create_datetime) {
		this.cg_create_datetime = cg_create_datetime;
	}
	public Long getCg_update_usr_ent_id() {
		return cg_update_usr_ent_id;
	}
	public void setCg_update_usr_ent_id(Long cg_update_usr_ent_id) {
		this.cg_update_usr_ent_id = cg_update_usr_ent_id;
	}
	public Date getCg_update_datetime() {
		return cg_update_datetime;
	}
	public void setCg_update_datetime(Date cg_update_datetime) {
		this.cg_update_datetime = cg_update_datetime;
	}
	public String getCg_status() {
		return cg_status;
	}
	public void setCg_status(String cg_status) {
		this.cg_status = cg_status;
	}

}
