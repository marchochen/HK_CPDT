package com.cwn.wizbank.wechat.service;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.wechat.common.Static;
import com.cwn.wizbank.wechat.model.Base;
import com.cwn.wizbank.wechat.model.music.OutputMusic;
import com.cwn.wizbank.wechat.model.music.OutputMusicItem;
import com.cwn.wizbank.wechat.model.news.OutputNews;
import com.cwn.wizbank.wechat.model.news.OutputNewsItem;
import com.cwn.wizbank.wechat.model.text.OutputText;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

@Service
public class WechatResponceService {

/*	private static final Logger logger = Logger
			.getLogger(WechatResponceService.class);
*/
	
	String emptyShow = "暂无相关信息！";
	
	
    /** 
     * 扩展xstream，使其支持CDATA块 
     *  
     * @date 2013-05-19 
     */  
    private static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  
                // 对所有xml节点的转换都增加CDATA标记  
                boolean cdata = true;  
  
                public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {  
                    super.startNode(name, clazz);  
                }  
  
                protected void writeText(QuickWriter writer, String text) {  
                    if (cdata) {  
                        writer.write("<![CDATA[");  
                        writer.write(text);  
                        writer.write("]]>");  
                    } else {  
                        writer.write(text);  
                    }  
                }  
            };  
        }  
    });
	
	/**
	 * 构文本消息xml
	 * 
	 * @param infoInput
	 * @param content
	 * @return
	 */
	public String buildTextInfoXml(Base infoInput, String content) {
		String textXml = null;

		if (null != infoInput && StringUtils.isNotBlank(content)) {
			String toUserName = infoInput.getFromUserName();// 接收方帐号（收到的OpenID?
			String fromUserName = infoInput.getToUserName();// 开发者微信号
			String createTime = infoInput.getCreateTime();// 消息创建时间
			
			//XStream ouputXstream = new XStream(new DomDriver());
			xstream.processAnnotations(OutputText.class);
			OutputText textInfoOutput = new OutputText();
			textInfoOutput.setToUserName(toUserName);
			textInfoOutput.setFromUserName(fromUserName);
			textInfoOutput.setCreateTime(createTime);
			textInfoOutput.setMsgType(Static.MSG_TYPE_TEXT);
			textInfoOutput.setContent(content);
			textXml = xstream.toXML(textInfoOutput);
		}

		return textXml;
	}

	/**
	 * 构造音乐消息xml
	 * 
	 * @param infoInput
	 * @param musicUrl
	 * @param hqMusicUrl
	 * @return
	 */
	public String buildMusicInfoXml(Base infoInput, OutputMusicItem musicItem) {
		String textXml = null;

		if (null != musicItem) {
			String toUserName = infoInput.getFromUserName();// 接收方帐号（收到的OpenID?
			String fromUserName = infoInput.getToUserName();// 开发者微信号
			String createTime = infoInput.getCreateTime();// 消息创建时间

			//XStream ouputXstream = new XStream(new DomDriver());
			xstream.processAnnotations(OutputMusic.class);

			OutputMusic music = new OutputMusic();
			music.setToUserName(toUserName);
			music.setFromUserName(fromUserName);
			music.setCreateTime(createTime);
			music.setMsgType(Static.MSG_TYPE_MUSIC);

			music.setMusicItem(musicItem);

			textXml = xstream.toXML(music);
		}

		return textXml;
	}

	/**
	 * 构?图文消息xml
	 * 
	 * @param infoInput
	 * @param newItemList
	 * @return
	 */
	public String buildNewsInfoXml(Base infoInput,
			List<OutputNewsItem> newItemList) {
		String newsXml = null;
		if (null != infoInput) {
			String toUserName = infoInput.getFromUserName();// 接收方帐号（收到的OpenID?
			String fromUserName = infoInput.getToUserName();// 开发者微信号
			String createTime = infoInput.getCreateTime();// 消息创建时间

			//XStream ouputXstream = new XStream(new DomDriver());
			xstream.processAnnotations(OutputNews.class);
			if (null != newItemList && newItemList.size() > 0) {
				OutputNews newsInfoOutput = new OutputNews();
				newsInfoOutput.setToUserName(toUserName);
				newsInfoOutput.setFromUserName(fromUserName);
				newsInfoOutput.setCreateTime(createTime);
				newsInfoOutput.setMsgType(Static.MSG_TYPE_NEWS);
				newsInfoOutput.setArticleList(newItemList);
				newsInfoOutput.setArticleCount(newItemList.size());
				newsXml = xstream.toXML(newsInfoOutput);
			} else {
				newsXml = buildTextInfoXml(infoInput, emptyShow);
			}
		}
		return newsXml;
	}

	/**
	 * 构造图文消息xml
	 * 
	 * @param infoInput
	 * @param newsItem
	 * @return
	 */
	public String buildNewsInfoXml(Base infoInput, OutputNewsItem newsItem) {
		String newsXml = null;
		if (null != infoInput && null != newsItem) {
			List<OutputNewsItem> newItemList = new ArrayList<OutputNewsItem>();
			newItemList.add(newsItem);
			newsXml = buildNewsInfoXml(infoInput, newItemList);
		}
		return newsXml;
	}
}
