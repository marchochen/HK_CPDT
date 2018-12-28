package com.cw.wizbank.dao.pagination;

public class NowPage {
	private String url = null;
	private long nowPath;

	public NowPage() {
	}

	public NowPage(String url, long nowPath) {
		this.url = url;
		this.nowPath = nowPath;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getNowPath() {
		return nowPath;
	}

	public void setNowPath(long nowPath) {
		this.nowPath = nowPath;
	}

}
