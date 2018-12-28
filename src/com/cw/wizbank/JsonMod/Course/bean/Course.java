package com.cw.wizbank.JsonMod.Course.bean;

public class Course {
	// 课程基本信息
	private long itm_id;
	private String itm_code;
	private String itm_title;
	private String itm_icon;
	private int itm_fee;
	private String itm_type;
	private long itm_tcr_id;
	private String tnd_title;

	// 课程统计信息
	private long iti_itm_id;
	private int iti_collect_count; // 收藏人数
	private int iti_share_count; //分享人数
	private int iti_star_count; // 评分人数
	private int iti_like_count; // 赞的人数
	private int iti_cmt_count; // 评论数
	private int iti_score; // 评分
	private float iti_cos_score; // 课程推荐百分比

	public Course(int iti_collect_count, int iti_share_count, int iti_star_count, int iti_like_count, int iti_cmt_count, int iti_score, float iti_cos_score) {
		super();
		this.iti_collect_count = iti_collect_count;
		this.iti_share_count = iti_share_count;
		this.iti_star_count = iti_star_count;
		this.iti_like_count = iti_like_count;
		this.iti_cmt_count = iti_cmt_count;
		this.iti_score = iti_score;
		this.iti_cos_score = iti_cos_score;
	}

	public Course() {
		super();
	}

	public long getItm_id() {
		return itm_id;
	}

	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}

	public String getItm_code() {
		return itm_code;
	}

	public void setItm_code(String itm_code) {
		this.itm_code = itm_code;
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

	public int getItm_fee() {
		return itm_fee;
	}

	public void setItm_fee(int itm_fee) {
		this.itm_fee = itm_fee;
	}

	public String getItm_type() {
		return itm_type;
	}

	public void setItm_type(String itm_type) {
		this.itm_type = itm_type;
	}

	public long getItm_tcr_id() {
		return itm_tcr_id;
	}

	public void setItm_tcr_id(long itm_tcr_id) {
		this.itm_tcr_id = itm_tcr_id;
	}

	public String getTnd_title() {
		return tnd_title;
	}

	public void setTnd_title(String tnd_title) {
		this.tnd_title = tnd_title;
	}

	public long getIti_itm_id() {
		return iti_itm_id;
	}

	public void setIti_itm_id(long iti_itm_id) {
		this.iti_itm_id = iti_itm_id;
	}

	public int getIti_collect_count() {
		return iti_collect_count;
	}

	public void setIti_collect_count(int iti_collect_count) {
		this.iti_collect_count = iti_collect_count;
	}

	public int getIti_share_count() {
		return iti_share_count;
	}

	public void setIti_share_count(int iti_share_count) {
		this.iti_share_count = iti_share_count;
	}

	public int getIti_star_count() {
		return iti_star_count;
	}

	public void setIti_star_count(int iti_star_count) {
		this.iti_star_count = iti_star_count;
	}

	public int getIti_like_count() {
		return iti_like_count;
	}

	public void setIti_like_count(int iti_like_count) {
		this.iti_like_count = iti_like_count;
	}

	public int getIti_score() {
		return iti_score;
	}

	public void setIti_score(int iti_score) {
		this.iti_score = iti_score;
	}

	public int getIti_cmt_count() {
		return iti_cmt_count;
	}

	public void setIti_cmt_count(int iti_cmt_count) {
		this.iti_cmt_count = iti_cmt_count;
	}

	public float getIti_cos_score() {
		return iti_cos_score;
	}

	public void setIti_cos_score(float iti_cos_score) {
		this.iti_cos_score = iti_cos_score;
	}

}