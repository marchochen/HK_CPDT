package com.cwn.wizbank.push;

import java.util.List;

import com.cwn.wizbank.entity.AppClient;
import com.cwn.wizbank.entity.AppPushMessage;


/**
 * app推送引擎接口
 * @author andrew.xiao 2016/8/9
 *
 */
public interface AppPushEngine {

	/**
	 * 单独推送个一个用户
	 * @param client 用户客户端实体
	 * @param appPushMessage 推送消息体
	 */
	public void pushMessage2Client(AppClient client,AppPushMessage appPushMessage);
	
	/**
	 * 推送给一组用户
	 * @param clientList 用户客户端实体集合
	 * @param appPushMessage 推送消息体
	 */
	public void pushMessage2ClientList(List<AppClient> clientList,AppPushMessage appPushMessage);
	
	/**
	 * 推送给app所有用户
	 * @param appPushMessage 推送消息体
	 * @return
	 */
	public void pushMessage2App(AppPushMessage appPushMessage);
	
}
