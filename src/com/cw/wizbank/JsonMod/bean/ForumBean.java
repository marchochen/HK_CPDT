package com.cw.wizbank.JsonMod.bean;

import java.sql.Timestamp;

/**
 * 论坛Bean类
 * @author kimyu
 */
public class ForumBean {
	private long for_res_id;			// 论坛ID
	private String for_res_title;		// 论坛标题
	private long fto_total;				// 某个论坛中的主题数
	private long fmg_total;				// 论坛文章数
	private long fmg_unread_total;		// 未读的论坛文章数
	
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

	public long getFto_total() {
		return fto_total;
	}

	public void setFto_total(long fto_total) {
		this.fto_total = fto_total;
	}

	public long getFmg_total() {
		return fmg_total;
	}

	public void setFmg_total(long fmg_total) {
		this.fmg_total = fmg_total;
	}

	public long getFmg_unread_total() {
		return fmg_unread_total;
	}

	public void setFmg_unread_total(long fmg_unread_total) {
		this.fmg_unread_total = fmg_unread_total;
	}
	
}
