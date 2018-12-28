package com.cwn.wizbank.entity.vo;

import java.util.Date;

/**
 * 课程VO halo
 */
public class AeItemVo {
	Long itm_id;
	String itm_desc;// 课程简介
	boolean itm_status_off;
	String itm_title;// 课程名称
	String itm_icon;// 课程图片
	Date itm_publish_timestamp;// 创建时间
	int is_user_like;
	Long itm_exam_ind;
	String itm_type;
	Long cnt_comment_count;
	int itm_canread;// 当前登录用户是否可读
	int itd_compulsory_ind;// 选修或必修
	String itm_mobile_ind;//是否发布到移动端的标志

	public Long getItm_id() {
		return itm_id;
	}

	public void setItm_id(Long itm_id) {
		this.itm_id = itm_id;
	}

	public String getItm_desc() {
		return itm_desc;
	}

	public void setItm_desc(String itm_desc) {
		this.itm_desc = itm_desc;
	}

	public boolean isItm_status_off() {
		return itm_status_off;
	}

	public void setItm_status_off(boolean itm_status_off) {
		this.itm_status_off = itm_status_off;
	}

	public String getItm_title() {
		return itm_title;
	}

	public void setItm_title(String itm_title) {
		this.itm_title = itm_title;
	}

	public String getItm_icon() {
		return itm_icon;
	}

	public void setItm_icon(String itm_icon) {
		this.itm_icon = itm_icon;
	}


	public Date getItm_publish_timestamp() {
		return itm_publish_timestamp;
	}

	public void setItm_publish_timestamp(Date itm_publish_timestamp) {
		this.itm_publish_timestamp = itm_publish_timestamp;
	}

	public int getIs_user_like() {
		return is_user_like;
	}

	public void setIs_user_like(int is_user_like) {
		this.is_user_like = is_user_like;
	}

	public Long getItm_exam_ind() {
		return itm_exam_ind;
	}

	public void setItm_exam_ind(Long itm_exam_ind) {
		this.itm_exam_ind = itm_exam_ind;
	}

	public String getItm_type() {
		return itm_type;
	}

	public void setItm_type(String itm_type) {
		this.itm_type = itm_type;
	}

	public Long getCnt_comment_count() {
		return cnt_comment_count;
	}

	public void setCnt_comment_count(Long cnt_comment_count) {
		this.cnt_comment_count = cnt_comment_count;
	}

	public int getItm_canread() {
		return itm_canread;
	}

	public void setItm_canread(int itm_canread) {
		this.itm_canread = itm_canread;
	}

	public int getItd_compulsory_ind() {
		return itd_compulsory_ind;
	}

	public void setItd_compulsory_ind(int itd_compulsory_ind) {
		this.itd_compulsory_ind = itd_compulsory_ind;
	}

	public String getItm_mobile_ind() {
		return itm_mobile_ind;
	}

	public void setItm_mobile_ind(String itm_mobile_ind) {
		this.itm_mobile_ind = itm_mobile_ind;
	}

}