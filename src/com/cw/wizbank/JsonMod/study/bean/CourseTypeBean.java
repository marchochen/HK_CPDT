package com.cw.wizbank.JsonMod.study.bean;

/**
 * 课程的类型(类型：网上课程、离线课程、书本、影音、网站)
 * @author kimyu
 */
public class CourseTypeBean {

	// 以下三字段对应aeItemType表的主键
	private int ity_owner_ent_id;	// 参照Entity表的ent_id
	private String ity_id;			// 课程或班级的类型标识
	private int ity_run_ind; 		// 是否为班级，用于区分课程还是班级(可选值：0 否，1 是)

	public int getIty_owner_ent_id() {
		return ity_owner_ent_id;
	}

	public void setIty_owner_ent_id(int ity_owner_ent_id) {
		this.ity_owner_ent_id = ity_owner_ent_id;
	}

	public String getIty_id() {
		return ity_id;
	}

	public void setIty_id(String ity_id) {
		this.ity_id = ity_id;
	}

	public int getIty_run_ind() {
		return ity_run_ind;
	}

	public void setIty_run_ind(int ity_run_ind) {
		this.ity_run_ind = ity_run_ind;
	}

}
