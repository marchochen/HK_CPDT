package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;


/**
 * 投票问题
 * 
 * @author Andrew 2015-6-6 下午2:00
 */
public class VoteQuestion implements java.io.Serializable {

	public static final String TYPE_SINGLE = "MC_S";
	public static final String TYPE_MUTIPLE = "MC_M";
	public static final String TYPE_DEFAULT = "MC_C";
	public static final String STATUS_ON = "ON";
	public static final String STATUS_DEL = "DEL";
	/**
	 * 
	 */
	private static final long serialVersionUID = -4561708364532877424L;
	/**
	 * 自增长id
	 */
	private Long vtq_id;

	/**
	 * 相关联投票的id
	 */
	private Long vtq_vot_id;

	/**
	 * 题目标题
	 */
	private String vtq_title;

	/**
	 * 题目内容
	 */
	private String vtq_contnet;

	/**
	 * 题目类型
	 */
	private String vtq_type = TYPE_DEFAULT;

	/**
	 * 状态
	 */
	private String vtq_status = STATUS_ON;

	/**
	 * 题目排序
	 */
	private Integer vtq_order = 1;

	/**
	 * 创建时间
	 */
	private Date vtq_create_timestamp;

	/**
	 * 创建人
	 */
	private String vtq_create_usr_id;

	/**
	 * 修改时间
	 */
	private Date vtq_update_timestamp;

	/**
	 * 修改人
	 */
	private String vtq_update_usr_id;
	
	private List<VoteOption> voteOptions; 
	

	public Long getVtq_id() {
		return vtq_id;
	}

	public void setVtq_id(Long vtq_id) {
		this.vtq_id = vtq_id;
	}

	public Long getVtq_vot_id() {
		return vtq_vot_id;
	}

	public void setVtq_vot_id(Long vtq_vot_id) {
		this.vtq_vot_id = vtq_vot_id;
	}

	public String getVtq_title() {
		return vtq_title;
	}

	public void setVtq_title(String vtq_title) {
		this.vtq_title = vtq_title;
	}

	public String getVtq_contnet() {
		return vtq_contnet;
	}

	public void setVtq_contnet(String vtq_contnet) {
		this.vtq_contnet = vtq_contnet;
	}

	public String getVtq_type() {
		return vtq_type;
	}

	public void setVtq_type(String vtq_type) {
		this.vtq_type = vtq_type;
	}

	public String getVtq_status() {
		return vtq_status;
	}

	public void setVtq_status(String vtq_status) {
		this.vtq_status = vtq_status;
	}

	public Integer getVtq_order() {
		return vtq_order;
	}

	public void setVtq_order(Integer vtq_order) {
		this.vtq_order = vtq_order;
	}

	public Date getVtq_create_timestamp() {
		return vtq_create_timestamp;
	}

	public void setVtq_create_timestamp(Date vtq_create_timestamp) {
		this.vtq_create_timestamp = vtq_create_timestamp;
	}

	public String getVtq_create_usr_id() {
		return vtq_create_usr_id;
	}

	public void setVtq_create_usr_id(String vtq_create_usr_id) {
		this.vtq_create_usr_id = vtq_create_usr_id;
	}

	public Date getVtq_update_timestamp() {
		return vtq_update_timestamp;
	}

	public void setVtq_update_timestamp(Date vtq_update_timestamp) {
		this.vtq_update_timestamp = vtq_update_timestamp;
	}

	public String getVtq_update_usr_id() {
		return vtq_update_usr_id;
	}

	public void setVtq_update_usr_id(String vtq_update_usr_id) {
		this.vtq_update_usr_id = vtq_update_usr_id;
	}

	public List<VoteOption> getVoteOptions() {
		return voteOptions;
	}

	public void setVoteOptions(List<VoteOption> voteOptions) {
		this.voteOptions = voteOptions;
	}

}