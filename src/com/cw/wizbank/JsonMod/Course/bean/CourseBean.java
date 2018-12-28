package com.cw.wizbank.JsonMod.Course.bean;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * 课程类
 * 
 * @author kimyu
 */
public class CourseBean {
	private long itm_id; // 课程ID
	private String itm_title; // 课程名
	private String itm_type; // 课程类型
	private String itm_desc; // 课程描述
	private int itm_comment_avg_score; // 课程评分
	private int avg_score; // 平均分
	private int total_count;
	private int total_score;
	private int app_count;
	private HashMap recent_start_classes; // 离线课程(混合课程)对应的班级的开课时间
	private Timestamp itm_appn_start_datetime; // 报名开始时间
	private Timestamp itm_appn_end_datetime; // 报名结束时间
	private Vector start_timestamp = new Vector();
	private Timestamp itm_publish_timestamp; // 课程发布时间
	private String lab_itm_type;// 用于前台显示的课程类型标签
	private String itm_dummy_type;
	private String itm_icon; // 课程图标
	private float itm_fee;
	private int order;
	private long btn;
	private long app_id;
	private long app_ent_id;
	private long app_tkh_id;
	private long app_cos_id;
	private Course course;
	private String cov_status;  //学习状态

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CourseBean)) {
			return false;
		}

		CourseBean c = (CourseBean) obj;

		return new EqualsBuilder().append(this.getItm_id(), c.getItm_id()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.getItm_id()).toHashCode();
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public long getItm_id() {
		return itm_id;
	}

	public void setItm_id(long itm_id) {
		this.itm_id = itm_id;
	}

	public String getItm_title() {
		return itm_title;
	}

	public void setItm_title(String itm_title) {
		this.itm_title = itm_title;
	}

	public String getItm_type() {
		return itm_type;
	}

	public void setItm_type(String itm_type) {
		this.itm_type = itm_type;
	}

	public int getAvg_score() {
		return avg_score;
	}

	public void setAvg_score(int avg_score) {
		this.avg_score = avg_score;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getTotal_score() {
		return total_score;
	}

	public void setTotal_score(int total_score) {
		this.total_score = total_score;
	}

	public int getApp_count() {
		return app_count;
	}

	public void setApp_count(int app_count) {
		this.app_count = app_count;
	}

	public String getItm_desc() {
		return itm_desc;
	}

	public void setItm_desc(String itm_desc) {
		this.itm_desc = itm_desc;
	}

	public HashMap getRecent_start_classes() {
		return recent_start_classes;
	}

	public void setRecent_start_classes(HashMap recent_start_classes) {
		this.recent_start_classes = recent_start_classes;
	}

	public Timestamp getItm_appn_start_datetime() {
		return itm_appn_start_datetime;
	}

	public void setItm_appn_start_datetime(Timestamp itm_appn_start_datetime) {
		this.itm_appn_start_datetime = itm_appn_start_datetime;
	}

	public Timestamp getItm_appn_end_datetime() {
		return itm_appn_end_datetime;
	}

	public void setItm_appn_end_datetime(Timestamp itm_appn_end_datetime) {
		this.itm_appn_end_datetime = itm_appn_end_datetime;
	}

	public Timestamp getItm_publish_timestamp() {
		return itm_publish_timestamp;
	}

	public void setItm_publish_timestamp(Timestamp itm_publish_timestamp) {
		this.itm_publish_timestamp = itm_publish_timestamp;
	}

	public int getItm_comment_avg_score() {
		return itm_comment_avg_score;
	}

	public void setItm_comment_avg_score(int itm_comment_avg_score) {
		this.itm_comment_avg_score = itm_comment_avg_score;
	}

	public Vector getStart_timestamp() {
		return start_timestamp;
	}

	public void setStart_timestamp(Vector start_timestamp) {
		this.start_timestamp = start_timestamp;
	}

	public String getLab_itm_type() {
		return lab_itm_type;
	}

	public void setLab_itm_type(String lab_itm_type) {
		this.lab_itm_type = lab_itm_type;
	}

	public String getItm_dummy_type() {
		return itm_dummy_type;
	}

	public void setItm_dummy_type(String itm_dummy_type) {
		this.itm_dummy_type = itm_dummy_type;
	}

	public String getItm_icon() {
		return itm_icon;
	}

	public void setItm_icon(String itm_icon) {
		this.itm_icon = itm_icon;
	}

	public float getItm_fee() {
		return itm_fee;
	}

	public void setItm_fee(float itm_fee) {
		this.itm_fee = itm_fee;
	}

	public long getBtn() {
		return btn;
	}

	public void setBtn(long btn) {
		this.btn = btn;
	}

	public long getApp_id() {
		return app_id;
	}

	public void setApp_id(long app_id) {
		this.app_id = app_id;
	}

	public long getApp_ent_id() {
		return app_ent_id;
	}

	public void setApp_ent_id(long app_ent_id) {
		this.app_ent_id = app_ent_id;
	}

	public long getApp_tkh_id() {
		return app_tkh_id;
	}

	public void setApp_tkh_id(long app_tkh_id) {
		this.app_tkh_id = app_tkh_id;
	}

	public long getApp_cos_id() {
		return app_cos_id;
	}

	public void setApp_cos_id(long app_cos_id) {
		this.app_cos_id = app_cos_id;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getCov_status() {
		return cov_status;
	}

	public void setCov_status(String cov_status) {
		this.cov_status = cov_status;
	}
}