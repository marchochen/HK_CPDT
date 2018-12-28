package com.cw.wizbank.JsonMod.bean;

import java.sql.Timestamp;

/**
 * 论坛主题Bean类
 * @author kimyu
 */
public class ForumTopicBean {
	private long for_res_id;					// 论坛id
	private String for_res_title;				// 论坛标题
	private long fto_id;						// 标题id
	private String fto_title;					// 标题
	private String fto_create_user;				// 创建人
	private Timestamp fto_create_datetime;		// 创建时间
	private Timestamp fto_last_post_datetime;	// 最后发表时间
	private String fto_last_post_user;			// 最后发表人
	private long fto_msg_cnt;					// 回复数
	
	public long getFor_res_id() {
		return for_res_id;
	}

	public void setFor_res_id(long for_res_id) {
		this.for_res_id = for_res_id;
	}

	public String getFor_res_title() {
		return for_res_title;
	}

	public void setFor_res_title(String for_res_title) {
		this.for_res_title = for_res_title;
	}

	public String getFto_title() {
		return fto_title;
	}

	public void setFto_title(String fto_title) {
		this.fto_title = fto_title;
	}

	public String getFto_create_user() {
		return fto_create_user;
	}

	public void setFto_create_user(String fto_create_user) {
		this.fto_create_user = fto_create_user;
	}

	public Timestamp getFto_create_datetime() {
		return fto_create_datetime;
	}

	public void setFto_create_datetime(Timestamp fto_create_datetime) {
		this.fto_create_datetime = fto_create_datetime;
	}

	public Timestamp getFto_last_post_datetime() {
		return fto_last_post_datetime;
	}

	public void setFto_last_post_datetime(Timestamp fto_last_post_datetime) {
		this.fto_last_post_datetime = fto_last_post_datetime;
	}

	public String getFto_last_post_user() {
		return fto_last_post_user;
	}

	public void setFto_last_post_user(String fto_last_post_user) {
		this.fto_last_post_user = fto_last_post_user;
	}

	public void setFto_id(long fto_id) {
		this.fto_id = fto_id;
	}

	public long getFto_msg_cnt() {
		return fto_msg_cnt;
	}

	public void setFto_msg_cnt(long fto_msg_cnt) {
		this.fto_msg_cnt = fto_msg_cnt;
	}

	public long getFto_id() {
		return fto_id;
	}
	
}
