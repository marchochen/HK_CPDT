package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;
/**
 * 群组成员
 * @author leon.li
 * 2014-8-7 下午5:36:45
 */
public class SnsGroupMember implements java.io.Serializable {
	private static final long serialVersionUID = -4353194645659431495L;

	
	Long s_gpm_id;
	/**
	 * 群组id
	 **/
	Long s_gpm_grp_id;
	/**
	 * 群组成员id
	 **/
	Long s_gpm_usr_id;
	/**
	 * 加入日期
	 **/
	Date s_gpm_join_datetime;
	/**
	 * 状态  0 等待审批 1 审批通过  3 审批拒绝
	 **/
	Long s_gpm_status; 
	/**
	 * 类型 2是普通成员 1是群主
	 **/
	Long s_gpm_type;
	/**
	 * 申请时间
	 **/
	Date s_gpm_apply_datetime;
	/**
	 * 审批时间
	 **/
	Date s_gpm_check_datetime;
	/**
	 * 审批人
	 **/
	Long s_gpm_check_user;
	
	List<Long> s_gpm_id_list;
	
	RegUser user;
	
	public Long getS_gpm_id() {
		return s_gpm_id;
	}
	public void setS_gpm_id(Long s_gpm_id) {
		this.s_gpm_id = s_gpm_id;
	}
	public Long getS_gpm_grp_id() {
		return s_gpm_grp_id;
	}
	public void setS_gpm_grp_id(Long s_gpm_grp_id) {
		this.s_gpm_grp_id = s_gpm_grp_id;
	}
	public Long getS_gpm_usr_id() {
		return s_gpm_usr_id;
	}
	public void setS_gpm_usr_id(Long s_gpm_usr_id) {
		this.s_gpm_usr_id = s_gpm_usr_id;
	}
	public Date getS_gpm_join_datetime() {
		return s_gpm_join_datetime;
	}
	public void setS_gpm_join_datetime(Date s_gpm_join_datetime) {
		this.s_gpm_join_datetime = s_gpm_join_datetime;
	}
	public Long getS_gpm_status() {
		return s_gpm_status;
	}
	public void setS_gpm_status(Long s_gpm_status) {
		this.s_gpm_status = s_gpm_status;
	}
	public Long getS_gpm_type() {
		return s_gpm_type;
	}
	public void setS_gpm_type(Long s_gpm_type) {
		this.s_gpm_type = s_gpm_type;
	}
	public Date getS_gpm_apply_datetime() {
		return s_gpm_apply_datetime;
	}
	public void setS_gpm_apply_datetime(Date s_gpm_apply_datetime) {
		this.s_gpm_apply_datetime = s_gpm_apply_datetime;
	}
	public Date getS_gpm_check_datetime() {
		return s_gpm_check_datetime;
	}
	public void setS_gpm_check_datetime(Date s_gpm_check_datetime) {
		this.s_gpm_check_datetime = s_gpm_check_datetime;
	}
	public Long getS_gpm_check_user() {
		return s_gpm_check_user;
	}
	public void setS_gpm_check_user(Long s_gpm_check_user) {
		this.s_gpm_check_user = s_gpm_check_user;
	}
	public List<Long> getS_gpm_id_list() {
		return s_gpm_id_list;
	}
	public void setS_gpm_id_list(List<Long> s_gpm_id_list) {
		this.s_gpm_id_list = s_gpm_id_list;
	}
	public RegUser getUser() {
		return user;
	}
	public void setUser(RegUser user) {
		this.user = user;
	}

}