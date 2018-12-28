package com.cwn.wizbank.wechat.model.news;

import java.util.List;

import com.cwn.wizbank.wechat.model.Base;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 微信图文消息发song
 */
@XStreamAlias("xml")
public class OutputNews extends Base{
	@XStreamAlias("ArticleCount")
	private int articleCount;//图文消息个数，限制为10条以上
	
	@XStreamAlias("Articles")
	private List<OutputNewsItem> articleList;
	
	@XStreamAlias("FuncFlag")
	private String funcFlag = "1";//x0001被标志时，星标刚收到的消息

	public int getArticleCount() {
		return articleCount;
	}

	public void setArticleCount(int articleCount) {
		this.articleCount = articleCount;
	}

	public String getFuncFlag() {
		return funcFlag;
	}

	public void setFuncFlag(String funcFlag) {
		this.funcFlag = funcFlag;
	}

	public List<OutputNewsItem> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<OutputNewsItem> articleList) {
		this.articleList = articleList;
	}
}
