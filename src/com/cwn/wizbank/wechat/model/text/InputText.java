package com.cwn.wizbank.wechat.model.text;


import com.cwn.wizbank.wechat.common.Static;
import com.cwn.wizbank.wechat.model.Base;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 微信文本消息接收
 */
@XStreamAlias("xml")
public class InputText extends Base{
	@XStreamAlias("Content")
	private String content = Static.CHAR_Blank;//文本消息内容
	
	@XStreamAlias("MsgId")
	private String msgId = Static.CHAR_Blank;//消息id
	
	@XStreamAlias("URL")
	private String URL = Static.CHAR_Blank;

	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	
	
}
