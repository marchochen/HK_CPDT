package com.cw.wizbank.JsonMod.commonBean;

/**
 * 课程的类型(类型：网上课程、离线课程、书本、影音、网站)
 * @author kimyu
 */
public class ItemTypeBean {

	/**
	 * 是否是一个分组类型，比如COS：课程，EXAM：测试等...
	 */
	private boolean is_group_type;
	private String itm_type; // 课程或班级的类型标识
	/**
	 * 一个用来替代原来的课程类型的字符串，根据ity_id,ity_blend_ind,ity_exam_ind,ity_ref_id 生成
	 */
	private String itm_dummy_type;
	
	private boolean itm_blend_ind;
	
	private boolean itm_create_run_ind;
	private boolean itm_exam_ind;
	private boolean itm_integ_ind;
	private boolean itm_ref_ind;
	/**
	 * label for corrsponding itm_type
	 */
	private String label;
	
	public boolean isIs_group_type() {
		return is_group_type;
	}
	public void setIs_group_type(boolean is_group_type) {
		this.is_group_type = is_group_type;
	}
	public String getItm_type() {
		return itm_type;
	}
	public void setItm_type(String itm_type) {
		this.itm_type = itm_type;
	}
	public String getItm_dummy_type() {
		return itm_dummy_type;
	}
	public void setItm_dummy_type(String itm_dummy_type) {
		this.itm_dummy_type = itm_dummy_type;
	}
	public boolean isItm_blend_ind() {
		return itm_blend_ind;
	}
	public void setItm_blend_ind(boolean itm_blend_ind) {
		this.itm_blend_ind = itm_blend_ind;
	}
	public boolean isItm_create_run_ind() {
		return itm_create_run_ind;
	}
	public void setItm_create_run_ind(boolean itm_create_run_ind) {
		this.itm_create_run_ind = itm_create_run_ind;
	}
	public boolean isItm_exam_ind() {
		return itm_exam_ind;
	}
	public void setItm_exam_ind(boolean itm_exam_ind) {
		this.itm_exam_ind = itm_exam_ind;
	}
	public boolean isItm_ref_ind() {
		return itm_ref_ind;
	}
	public void setItm_ref_ind(boolean itm_ref_ind) {
		this.itm_ref_ind = itm_ref_ind;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public boolean isItm_integ_ind() {
		return itm_integ_ind;
	}
	public void setItm_integ_ind(boolean itmIntegInd) {
		itm_integ_ind = itmIntegInd;
	}

}
