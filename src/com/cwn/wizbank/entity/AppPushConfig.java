package com.cwn.wizbank.entity;

/**
 * App推送信息配置
 * 
 * @author andrew.xiao 2016/8/9
 *
 */
public class AppPushConfig {

	private String appId;
	private String appKey;
	private String masterSecret;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getMasterSecret() {
		return masterSecret;
	}

	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

}
