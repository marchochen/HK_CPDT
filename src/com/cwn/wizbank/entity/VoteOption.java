package com.cwn.wizbank.entity;


/**
 * 投票问题选项
 * 
 * @author Andrew 2015-6-6 下午2:00
 */
public class VoteOption implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4206579731898046099L;

	/**
	 * 自增长ID
	 */
	private Long vto_id;

	/**
	 * 相关联的题目ID
	 */
	private Long vto_vtq_id;

	/**
	 * 选项内容
	 */
	private String vto_desc;

	/**
	 * 选项排序
	 */
	private Long vto_order;

	public Long getVto_id() {
		return vto_id;
	}

	public void setVto_id(Long vto_id) {
		this.vto_id = vto_id;
	}

	public Long getVto_vtq_id() {
		return vto_vtq_id;
	}

	public void setVto_vtq_id(Long vto_vtq_id) {
		this.vto_vtq_id = vto_vtq_id;
	}

	public String getVto_desc() {
		return vto_desc;
	}

	public void setVto_desc(String vto_desc) {
		this.vto_desc = vto_desc;
	}

	public Long getVto_order() {
		return vto_order;
	}

	public void setVto_order(Long vto_order) {
		this.vto_order = vto_order;
	}

}