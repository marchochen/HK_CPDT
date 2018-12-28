package com.cwn.wizbank.entity;

/**
 * 
 * 记录用户app客户端情况，用于app推送
 * 
 * @author andrew.xiao 2016/8/9
 * 
 *    desc：在用户使用APP登录的时候，记录信息
 */
public class AppClient {

	public static final String MOBILE_IND_ANDROID = "ANDROID";
	public static final String MOBILE_IND_IOS = "IOS";
	
	public static final String STATUS_ONLINE = "online";
	public static final String STATUS_OFFLINE = "offline";
	
	/**
	 * 对应平台的用户id
	 */
	private long usrEntId;
	
	/**
	 * 手机标识，Android or IOS
	 */
	private String mobileInd;
	
	/**
	 * 手机app对应的id，每台手机每个应用都对应一个clientId
	 */
	private String clientId;
	
	/**
	 * 整个应用的标识(注意：不是打包时的appId，是推送配置的appId)
	 */
	private String appId;
	
	/**
	 * app登录状态，当用户退出app时，为【offline】，登录时，为【online】
	 */
	private String status;
	

	public long getUsrEntId() {
		return usrEntId;
	}

	public void setUsrEntId(long usrEntId) {
		this.usrEntId = usrEntId;
	}

	public String getMobileInd() {
		return mobileInd;
	}

	public void setMobileInd(String mobileInd) {
		this.mobileInd = mobileInd;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * for print object
	 */
	@Override
	public String toString() {
		return "usrEntId:"+this.usrEntId+";mobileInd:"+this.mobileInd+";clientId:"+this.clientId+";appId:"+this.appId+";"+"status:"+this.status;
	}
	
}
