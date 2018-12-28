package com.cwn.wizbank.entity;

import java.util.Map;

/**
 * app 推送信息实体
 * @author andrew.xiao 2016/8/9
 *
 */
public class AppPushMessage{

	/**
	 * 推送的标题
	 */
	private String title;
	
	/**
	 * 推送的内容
	 */
	private String content;
	
	/**
	 * 透传消息参数
	 */
	private Map<String, Object> params;

	public AppPushMessage() {
	}

	public AppPushMessage(String title, String content,
			Map<String, Object> params) {
		this.title = title;
		this.content = content;
		this.params = params;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
}
