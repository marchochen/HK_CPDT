package com.cwn.wizbank.wechat.model.news;

import com.cwn.wizbank.wechat.common.Static;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 微信图文消息发
 */
@XStreamAlias("item")
public class OutputNewsItem {
	@XStreamAlias("Title")
	private String title = Static.CHAR_Blank;// 图文消息标题

	@XStreamAlias("Description")
	private String description = Static.CHAR_Blank;// 图文消息描述

	@XStreamAlias("PicUrl")
	private String picUrl = Static.CHAR_Blank;// 图片链接，支持JPG、PNG格式，较好的效果为大240*320，小0*80

	@XStreamAlias("Url")
	private String url = Static.CHAR_Blank;// 点击图文消息跳转链接

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
