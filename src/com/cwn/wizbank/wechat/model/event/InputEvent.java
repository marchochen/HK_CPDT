package com.cwn.wizbank.wechat.model.event;

import com.cwn.wizbank.wechat.common.Static;
import com.cwn.wizbank.wechat.model.Base;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ΢���¼���Ϣ����
 * 
 * @author Administrator
 */
@XStreamAlias("xml")
public class InputEvent extends Base {

	@XStreamAlias("Event")
	private String event = Static.CHAR_Blank;// 事件类型，subscribe(订阅)、unsubscribe(取消订阅)、CLICK(自定义菜单点击事

	@XStreamAlias("EventKey")
	private String eventKey = Static.CHAR_Blank;// 事件KEY值，与自定义菜单接口中KEY值对

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

}
