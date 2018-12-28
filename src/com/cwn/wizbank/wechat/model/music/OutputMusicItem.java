package com.cwn.wizbank.wechat.model.music;

import com.cwn.wizbank.wechat.common.Static;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 微信音乐消息发送
 * @author Administrator
 */
@XStreamAlias("Music")
public class OutputMusicItem{
	
	@XStreamAlias("Title")
	private String title = Static.CHAR_Blank;//图文消息标题
	
	@XStreamAlias("Description")
	private String description = Static.CHAR_Blank;//图文消息描述
	
	@XStreamAlias("MusicUrl")
	private String musicUrl = Static.CHAR_Blank;//音乐链接
	
	@XStreamAlias("HQMusicUrl")
	private String hqMusicUrl = Static.CHAR_Blank;//高质量音乐链接，WIFI环境优先使用该链接播放音
	
	public String getMusicUrl() {
		return musicUrl;
	}
	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}
	public String getHqMusicUrl() {
		return hqMusicUrl;
	}
	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}
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
}
