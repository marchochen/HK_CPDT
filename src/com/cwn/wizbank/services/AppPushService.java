package com.cwn.wizbank.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cwn.wizbank.entity.AppClient;
import com.cwn.wizbank.entity.AppPushMessage;
import com.cwn.wizbank.push.AppPushEngine;

/**
 * 
 * @author andrew.xiao 2016/8/9
 * 
 * app推送服务
 *
 */
@Service
public class AppPushService {

	@Autowired
	private AppClientService appClientService;
	
	@Autowired
	private AppPushEngine appPushEngine;
	
	public AppClientService getAppClientService() {
		return appClientService;
	}

	public void setAppClientService(AppClientService appClientService) {
		this.appClientService = appClientService;
	}

	/**
	 * 单独推送个一个用户
	 * @param usrEntId 用户id
	 * @param appPushMessage 推送消息体
	 */
	public void pushMessage2User(long usrEntId,AppPushMessage appPushMessage){
		
		List<AppClient> appClients = appClientService.getValidRecodeByUsrEntId(usrEntId);
		Push2Client(appPushMessage, appClients);
	}

	/**
	 * 推送给一组用户
	 * @param usrEntIdList 用户Id集合
	 * @param appPushMessage 推送消息体
	 */
	public void pushMessage2UserList(List<Long> usrEntIdList,AppPushMessage appPushMessage){
		
		List<AppClient> appClients = appClientService.getValidRecodeByUsrEntIdList(usrEntIdList);
		Push2Client(appPushMessage, appClients);
	}
	
	/**
	 * 推送给app所有用户
	 * @param appPushMessage 推送消息体
	 * @return
	 */
	public void pushMessage2App(AppPushMessage appPushMessage){
		appPushEngine.pushMessage2App(appPushMessage);
	}
	
	private void Push2Client(AppPushMessage appPushMessage,
			List<AppClient> appClients) {
		if(appClients==null || appClients.size() == 0){
			return;
		}
		if(appClients.size() == 1){
			appPushEngine.pushMessage2Client(appClients.get(0), appPushMessage);
		}else{
			appPushEngine.pushMessage2ClientList(appClients, appPushMessage);
		}
	}
}
