package com.cwn.wizbank.wechat.model.music;

import com.cwn.wizbank.wechat.model.Base;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 微信音乐消息发送
 */
@XStreamAlias("xml")
public class OutputMusic extends Base {
	@XStreamAlias("FuncFlag")
	private String funcFlag = "0";// x0001被标志时，星标刚收到的消息

	@XStreamAlias("Music")
	private OutputMusicItem musicItem;

	public OutputMusicItem getMusicItem() {
		return musicItem;
	}

	public void setMusicItem(OutputMusicItem musicItem) {
		this.musicItem = musicItem;
	}

	public String getFuncFlag() {
		return funcFlag;
	}

	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}
}
