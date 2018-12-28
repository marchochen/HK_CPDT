package com.cwn.wizbank.entity.vo;

import java.util.Date;

import com.cwn.wizbank.entity.RegUser;

public class LikeMsgVo {
	private long id;
	
	private String module;
	
	private String title;
	
	private String titleTcr;
	
	private String url;
	
	private Date  crtTime;
	
	private Long targetId; 
	
	private RegUser user;
	
	private RegUser operator;
	
	private int isComment;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleTcr() {
		return titleTcr;
	}

	public void setTitleTcr(String titleTcr) {
		this.titleTcr = titleTcr;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	

	public Date getCrtTime() {
		return crtTime;
	}

	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}

	
	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public RegUser getUser() {
		return user;
	}

	public void setUser(RegUser user) {
		this.user = user;
	}

	public RegUser getOperator() {
		return operator;
	}

	public void setOperator(RegUser operator) {
		this.operator = operator;
	}

	public int getIsComment() {
		return isComment;
	}

	public void setIsComment(int isComment) {
		this.isComment = isComment;
	}
	
	
	
}
