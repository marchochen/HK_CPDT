package com.cwn.wizbank.entity;

import java.util.Date;


public class LoginLog  extends SystemLog{
	
	public static final int MODE_PC = 1;        //pc登陆
	public static final int MODE_APP = 2;       //APP登陆
	public static final int MODE_WECHAT  = 3;   //微信登陆
	
	public static final String USR_LOGIN_STATUS_FAIL = "fail";  //登录失败
	public static final String USR_LOGIN_STATUS_SUCCESS = "success";  //登录失败
	
	int  entId;             //登录用户的ent_id
	String usrDisplayBil;   //用户登录名
	String usrFullNameBil;  //全名
	int loginMode;       //登录入口
	String loginIP;         //登录IP
	Date loginTime;         //登录时间
	String tableName;       //表名
	int grpId;              //用户组id
	int ugrId;              //职级id
	int uptId;              //岗位id
	int year;               //年份
	int month;              //月份
	int totleLoginNumber;   //登录人次
	String usrLoginStatus;  //登录状态  成功/失败
	String login_failure_code; //登录失败code
	
	public LoginLog(){}
	
	/**
	 * 
	 * @param entId  用户id
	 * @param usrDisplayBil  用户登录名
	 * @param usrFullNameBil 全名
	 * @param loginMode  登录方式
	 * @param loginIP    登录ip
	 * @param loginTime  登录时间
	 * @param usrLoginStatus  登录状态
	 * @param loginFailureCode  登录失败code
	 */
	public LoginLog(int entId,String usrDisplayBil,	String usrFullNameBil,int loginMode,String loginIP,
			        Date loginTime,String usrLoginStatus,String loginFailureCode){
		this.entId = entId;
		this.usrDisplayBil = usrDisplayBil;
		this.usrFullNameBil = usrFullNameBil;
		this.loginMode = loginMode;
		this.loginIP = loginIP;
		this.loginTime = loginTime;
		this.usrLoginStatus = usrLoginStatus;
		this.login_failure_code = loginFailureCode;
	}
	
	
	
	public int getEntId() {
		return entId;
	}
	public void setEntId(int entId) {
		this.entId = entId;
	}
	public String getUsrDisplayBil() {
		return usrDisplayBil;
	}
	public void setUsrDisplayBil(String usrDisplayBil) {
		this.usrDisplayBil = usrDisplayBil;
	}
	public String getUsrFullNameBil() {
		return usrFullNameBil;
	}
	public void setUsrFullNameBil(String usrFullNameBil) {
		this.usrFullNameBil = usrFullNameBil;
	}
	public int getLoginMode() {
		return loginMode;
	}
	public void setLoginMode(int loginMode) {
		this.loginMode = loginMode;
	}
	public String getLoginIP() {
		return loginIP;
	}
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getGrpId() {
		return grpId;
	}
	public void setGrpId(int grpId) {
		this.grpId = grpId;
	}
	public int getUgrId() {
		return ugrId;
	}
	public void setUgrId(int ugrId) {
		this.ugrId = ugrId;
	}
	public int getUptId() {
		return uptId;
	}
	public void setUptId(int uptId) {
		this.uptId = uptId;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getTotleLoginNumber() {
		return totleLoginNumber;
	}
	public void setTotleLoginNumber(int totleLoginNumber) {
		this.totleLoginNumber = totleLoginNumber;
	}

	public String getUsrLoginStatus() {
		return usrLoginStatus;
	}

	public void setUsrLoginStatus(String usrLoginStatus) {
		this.usrLoginStatus = usrLoginStatus;
	}

	public String getLogin_failure_code() {
		return login_failure_code;
	}

	public void setLogin_failure_code(String login_failure_code) {
		this.login_failure_code = login_failure_code;
	}
	
	
	
}
