package com.cwn.wizbank.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Creating to 2016-1-5 12:12
 * @author mt-201
 *
 */
public class LiveItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 0
	 */
	public static final int LIVE_UNPUBLISH = 0;
	/**
	 * 直播进行中, 参加者可以进入观看直播
	 */
	public static final int LIVE_IN = 1;
	/**
	 * 预约中 , 活动预约中,尚未开始
	 */
	public static final int LIVE_BOOKING = 2;
	/**
	 * 结束 , 活动已结束
	 */
	public static final int LIVE_OVER = 3;
	/**
	 * 录播已上线, 参加者可以观看录播回放
	 */
	public static final int LIVE_ONLINE = 4;
	/**
	 * 发布
	 */
	public static final String PUBLISH = "publish";
	/**
	 * 已发布
	 */
	public static final String STATUS_ON = "ON";
	/**
	 * 未发布
	 */
	public static final String STATUS_OFF = "OFF";
	/**
	 * 微吼
	 */
	public static final String LIVE_MODE_TYPE_VHALL = "VHALL";
	/**
	 * 腾讯云
	 */
	public static final String LIVE_MODE_TYPE_QCLOUD = "QCLOUD";
	/**
	 * 展示互动
	 */
	public static final String LIVE_MODE_TYPE_GENSEE = "GENSEE";
	/**
	 * 其他
	 */
	public static final String LIVE_MODE_TYPE_OTHER = "OTHER";
	
	/**
	 * PK
	 */
	private long lv_id; 
	/**
	 * 標題
	 */
	private String lv_title;
	/**
	 * 直播简介
	 */
	private String lv_desc;
	/**
	 * 创建时间
	 */
	private Date lv_create_datetime; 
	/**
	 * 创建人ID
	 */
	private String lv_create_usr_id;
	/**
	 * 更新时间
	 */
	private Date lv_upd_datetime;
	/**
	 * 更新人ID
	 */
	private String lv_upd_usr_id;	
	/**
	 * 直播开始时间
	 */
	private Date lv_start_datetime;
	/**
	 * 直播结束时间
	 */
	private Date lv_end_datetime;
	/**
	 * 直播室播放地址
	 */
	private String lv_url;
	/**
	 * 直播密码
	 */
	private String lv_pwd;	
	/**
	 * 活动ID(重要)
	 */
	private long lv_webinar_id;
	/**
	 * 回放ID
	 */
	private long lv_record_id;
	/**
	 * 图片
	 */
	private String lv_image;
	/**
	 * 状态  on=已发布。off=未发布
	 */
	private String lv_status;
	/**
	 * 状态  2=未开始（预告）。1=正在直播。3-已结束
	 */
	private int lv_type;
	/**
	 * 直播开始时间
	 */
	private Date lv_real_start_datetime;
	/**
	 * 是否需要密码才能查看
	 */
	private boolean lv_need_pwd;
	/**
	 * 是否直播过
	 */
	private boolean lv_had_live;
	/**
	 * 培训中心ID
	 */
	private long lv_tcr_id;
	/**
	 * 限制的人数
	 */
	private int lv_people_num;
	/**
	 * 供应商类型
	 * 微吼：VHALL
	 * 腾讯云：QCLOUD
	 */
	private String lv_mode_type;
	/** 
	 * 推流地址
	 */
	private String lv_upstream_address;
	/**
	 *  hls观看地址
	 */
	 private String lv_hls_downstream_address;
	 /** 
	  *  rtmp观看地址
	  */
	 private String lv_rtmp_downstream_address;
	/** 
	 *  flv观看地址
	 */
	 private String lv_flv_downstream_address;
	 /**
	  * 腾讯云id
	  */
	 private String lv_channel_id;
	 
	 /**
	  * Web 端学员口令
	  */
	 private String lv_student_token;
	 /**
	  * 老师口令
	  */
	 private String lv_teacher_token; 
	 /**
	  * 学生客户端口令
	  */
	 private String lv_student_client_token;
	 /**
	  * 老师和助教加入URL
	  */
	 private String lv_teacher_join_url;
	 /**
	  * 学员加入URL
	  */
	 private String lv_student_join_url;
	 /**
	  * 展示互动在线人数
	  */
	 private int lv_gensee_online_user;
	 /**
	  * 展示互动真正的结束时间
	  */
	 private Date lv_real_end_datetime;
	 /**
	  * 展示互动回放路径
	  */
	 private String lv_gensee_record_url;
	
	//非数据库字段
	/**
	 * 在线人数
	 */
	private int lv_onlineNum;
	
	private String lv_enc_id;
	
	private String lv_image_path;
	
	public String getLv_image_path() {
		return lv_image_path;
	}

	public void setLv_image_path(String lv_image_path) {
		this.lv_image_path = lv_image_path;
	}

	public String getLv_enc_id() {
		return lv_enc_id;
	}

	public void setLv_enc_id(String lv_enc_id) {
		this.lv_enc_id = lv_enc_id;
	}

	public long getLv_id() {
		return lv_id;
	}

	public void setLv_id(long lv_id) {
		this.lv_id = lv_id;
	}
	
	public String getLv_title() {
		return lv_title;
	}
	
	public void setLv_title(String lv_title) {
		this.lv_title = lv_title;
	}
	
	public Date getLv_start_datetime() {
		return lv_start_datetime;
	}
	
	public void setLv_start_datetime(Date lv_start_datetime) {
		this.lv_start_datetime = lv_start_datetime;
	}
	
	public Date getLv_end_datetime() {
		return lv_end_datetime;
	}
	
	public void setLv_end_datetime(Date lv_end_datetime) {
		this.lv_end_datetime = lv_end_datetime;
	}
	
	public String getLv_create_usr_id() {
		return lv_create_usr_id;
	}
	
	public void setLv_create_usr_id(String lv_create_usr_id) {
		this.lv_create_usr_id = lv_create_usr_id;
	}
	
	public String getLv_upd_usr_id() {
		return lv_upd_usr_id;
	}
	
	public void setLv_upd_usr_id(String lv_upd_usr_id) {
		this.lv_upd_usr_id = lv_upd_usr_id;
	}
	
	public String getLv_url() {
		return lv_url;
	}
	
	public void setLv_url(String lv_url) {
		this.lv_url = lv_url;
	}
	
	public String getLv_desc() {
		return lv_desc;
	}

	public void setLv_desc(String lv_desc) {
		this.lv_desc = lv_desc;
	}

	public Date getLv_create_datetime() {
		return lv_create_datetime;
	}

	public void setLv_create_datetime(Date lv_create_datetime) {
		this.lv_create_datetime = lv_create_datetime;
	}

	public Date getLv_upd_datetime() {
		return lv_upd_datetime;
	}

	public void setLv_upd_datetime(Date lv_upd_datetime) {
		this.lv_upd_datetime = lv_upd_datetime;
	}

	public long getLv_webinar_id() {
		return lv_webinar_id;
	}

	public void setLv_webinar_id(long lv_webinar_id) {
		this.lv_webinar_id = lv_webinar_id;
	}

	public String getLv_image() {
		return lv_image;
	}

	public void setLv_image(String lv_image) {
		this.lv_image = lv_image;
	}

	public String getLv_status() {
		return lv_status;
	}

	public void setLv_status(String lv_status) {
		this.lv_status = lv_status;
	}

	public String getLv_pwd() {
		return lv_pwd;
	}

	public void setLv_pwd(String lv_pwd) {
		this.lv_pwd = lv_pwd;
	}

	public long getLv_record_id() {
		return lv_record_id;
	}

	public void setLv_record_id(long lv_record_id) {
		this.lv_record_id = lv_record_id;
	}

	public int getLv_onlineNum() {
		return lv_onlineNum;
	}

	public void setLv_onlineNum(int lv_onlineNum) {
		this.lv_onlineNum = lv_onlineNum;
	}

	public Date getLv_real_start_datetime() {
		return lv_real_start_datetime;
	}

	public void setLv_real_start_datetime(Date lv_real_start_datetime) {
		this.lv_real_start_datetime = lv_real_start_datetime;
	}

	public long getLv_tcr_id() {
		return lv_tcr_id;
	}

	public void setLv_tcr_id(long lv_tcr_id) {
		this.lv_tcr_id = lv_tcr_id;
	}

	public int getLv_people_num() {
		return lv_people_num;
	}

	public void setLv_people_num(int lv_people_num) {
		this.lv_people_num = lv_people_num;
	}

	public int getLv_type() {
		return lv_type;
	}

	public void setLv_type(int lv_type) {
		this.lv_type = lv_type;
	}

	public boolean getLv_need_pwd() {
		return lv_need_pwd;
	}

	public void setLv_need_pwd(boolean lv_need_pwd) {
		this.lv_need_pwd = lv_need_pwd;
	}

	public boolean getLv_had_live() {
		return lv_had_live;
	}

	public void setLv_had_live(boolean lv_had_live) {
		this.lv_had_live = lv_had_live;
	}

	public String getLv_mode_type() {
		return lv_mode_type;
	}

	public void setLv_mode_type(String lv_mode_type) {
		this.lv_mode_type = lv_mode_type;
	}

	public String getLv_upstream_address() {
		return lv_upstream_address;
	}

	public void setLv_upstream_address(String lv_upstream_address) {
		this.lv_upstream_address = lv_upstream_address;
	}

	public String getLv_hls_downstream_address() {
		return lv_hls_downstream_address;
	}

	public void setLv_hls_downstream_address(String lv_hls_downstream_address) {
		this.lv_hls_downstream_address = lv_hls_downstream_address;
	}

	public String getLv_rtmp_downstream_address() {
		return lv_rtmp_downstream_address;
	}

	public void setLv_rtmp_downstream_address(String lv_rtmp_downstream_address) {
		this.lv_rtmp_downstream_address = lv_rtmp_downstream_address;
	}

	public String getLv_flv_downstream_address() {
		return lv_flv_downstream_address;
	}

	public void setLv_flv_downstream_address(String lv_flv_downstream_address) {
		this.lv_flv_downstream_address = lv_flv_downstream_address;
	}

	public String getLv_channel_id() {
		return lv_channel_id;
	}

	public void setLv_channel_id(String lv_channel_id) {
		this.lv_channel_id = lv_channel_id;
	}

	public String getLv_student_token() {
		return lv_student_token;
	}

	public void setLv_student_token(String lv_student_token) {
		this.lv_student_token = lv_student_token;
	}

	public String getLv_teacher_token() {
		return lv_teacher_token;
	}

	public void setLv_teacher_token(String lv_teacher_token) {
		this.lv_teacher_token = lv_teacher_token;
	}

	public String getLv_student_client_token() {
		return lv_student_client_token;
	}

	public void setLv_student_client_token(String lv_student_client_token) {
		this.lv_student_client_token = lv_student_client_token;
	}

	public String getLv_teacher_join_url() {
		return lv_teacher_join_url;
	}

	public void setLv_teacher_join_url(String lv_teacher_join_url) {
		this.lv_teacher_join_url = lv_teacher_join_url;
	}

	public String getLv_student_join_url() {
		return lv_student_join_url;
	}

	public void setLv_student_join_url(String lv_student_join_url) {
		this.lv_student_join_url = lv_student_join_url;
	}

	public int getLv_gensee_online_user() {
		return lv_gensee_online_user;
	}

	public void setLv_gensee_online_user(int lv_gensee_online_user) {
		this.lv_gensee_online_user = lv_gensee_online_user;
	}

	public Date getLv_real_end_datetime() {
		return lv_real_end_datetime;
	}

	public void setLv_real_end_datetime(Date lv_real_end_datetime) {
		this.lv_real_end_datetime = lv_real_end_datetime;
	}

	public String getLv_gensee_record_url() {
		return lv_gensee_record_url;
	}

	public void setLv_gensee_record_url(String lv_gensee_record_url) {
		this.lv_gensee_record_url = lv_gensee_record_url;
	}
	
}
