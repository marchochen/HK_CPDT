package com.cwn.wizbank.wechat.model;

import com.cwn.wizbank.wechat.common.Static;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Base {
	@XStreamAlias("ToUserName")
	private String toUserName = Static.CHAR_Blank;//开发者微信号
	
	@XStreamAlias("FromUserName")
	private String fromUserName = Static.CHAR_Blank;//发送方帐号
	
	@XStreamAlias("CreateTime")
	private String createTime = Static.CHAR_Blank;//消息创建时间 （整型）
	
	@XStreamAlias("MsgType")
	private String msgType = Static.CHAR_Blank;//text
	
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}
