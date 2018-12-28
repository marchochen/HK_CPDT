package com.cwn.wizbank.entity;

import java.util.Date;


/**
 * 投票结果
 * 
 * @author Andrew 2015-6-6 下午2:00
 */
public class VoteResponse implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6168729987369142162L;

	/**
	 * 用户ID
	 */
	private Long vrp_usr_ent_id;

	/**
	 * 投票活动ID
	 */
	private Long vrp_vot_id;

	/**
	 * 题目ID
	 */
	private Long vrp_vtq_id;

	/**
	 * 所选选项
	 */
	private Long vrp_vto_id;

	/**
	 * 投票时间
	 */
	private Date vrp_respone_time;

	public Long getVrp_usr_ent_id() {
		return vrp_usr_ent_id;
	}

	public void setVrp_usr_ent_id(Long vrp_usr_ent_id) {
		this.vrp_usr_ent_id = vrp_usr_ent_id;
	}

	public Long getVrp_vot_id() {
		return vrp_vot_id;
	}

	public void setVrp_vot_id(Long vrp_vot_id) {
		this.vrp_vot_id = vrp_vot_id;
	}

	public Long getVrp_vtq_id() {
		return vrp_vtq_id;
	}

	public void setVrp_vtq_id(Long vrp_vtq_id) {
		this.vrp_vtq_id = vrp_vtq_id;
	}

	public Long getVrp_vto_id() {
		return vrp_vto_id;
	}

	public void setVrp_vto_id(Long vrp_vto_id) {
		this.vrp_vto_id = vrp_vto_id;
	}

	public Date getVrp_respone_time() {
		return vrp_respone_time;
	}

	public void setVrp_respone_time(Date vrp_respone_time) {
		this.vrp_respone_time = vrp_respone_time;
	}

}