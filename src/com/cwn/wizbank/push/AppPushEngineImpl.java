package com.cwn.wizbank.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.cw.wizbank.dao.Log4jFactory;
import com.cwn.wizbank.entity.AppClient;
import com.cwn.wizbank.entity.AppPushConfig;
import com.cwn.wizbank.entity.AppPushMessage;
import com.cwn.wizbank.utils.CharUtils;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;

/**
 * app推送引擎接口默认的实现，底层基于个推：http://www.getui.com/
 * 
 * @author andrew.xiao 2016/8/9
 * 
 * 支持对多个 app应用进行推送
 *
 */
public class AppPushEngineImpl implements AppPushEngine{

	private static final Logger logger = Log4jFactory.createLogger(AppPushEngineImpl.class);
	
	public static final int titleLimitLength = 40;//20 个字40个字符
	public static final int contentLimitLength = 200;//100 个字200个字符
	
	/**
	 * 个推公众平台推送url
	 */
	private static final String url = "http://sdk.open.api.igexin.com/apiex.htm";
	
	private List<AppPushConfig> appPushConfigList;
	private Map<String,IGtPush> appPusherContainner;
	private Map<String,AppPushConfig> appPushConfigMap;
	
	/**
	 * init
	 * @param appPushConfigList
	 */
	public AppPushEngineImpl(List<AppPushConfig> appPushConfigList){
		
		this.appPushConfigList = appPushConfigList;
		
		if(appPushConfigList!=null && appPushConfigList.size() > 0){
			
			this.appPusherContainner = new HashMap<String, IGtPush>();
			this.appPushConfigMap = new HashMap<String, AppPushConfig>();
			
			for(AppPushConfig aps : appPushConfigList){
				IGtPush push = new IGtPush(url, aps.getAppKey(),aps.getMasterSecret());
				this.appPusherContainner.put(aps.getAppId(), push);
				this.appPushConfigMap.put(aps.getAppId(),aps);
			}
			
		}
	}

	/**
	 * 单独推送个一个用户
	 * @param client 用户客户端实体
	 * @param appPushMessage 推送消息体
	 */
	public void pushMessage2Client(AppClient client,AppPushMessage appPushMessage){
		push2Single(client, appPushMessage);
	}

	/**
	 * 推送给一组用户
	 * @param clientList 用户客户端集合
	 * @param appPushMessage 推送消息体
	 */
	public void pushMessage2ClientList(List<AppClient> clientList,AppPushMessage appPushMessage){
		
		if(!appConfigValidate()){
			return;
		};
		
		if(clientList==null || clientList.size() == 0){
			return;
		}
		
		// 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
		for(int i=0;i<clientList.size();i++){
			AppClient appClient = clientList.get(i);
			push2Single(appClient, appPushMessage);
		}
		
		return;
	}
	
	/**
	 * 推送给app所有用户
	 * @param appPushMessage 推送消息体
	 */
	public void pushMessage2App(AppPushMessage appPushMessage){
		
		if(!appConfigValidate()){
			return;
		};
		
		for(int i=0;i<appPusherContainner.size();i++){
			
			AppPushConfig apc = appPushConfigList.get(i);
			IGtPush pusher = appPusherContainner.get(apc.getAppId());
			NotificationTemplate template = PushTemplateProvider.notificationTemplate(apc.getAppId(),apc.getAppKey(),appPushMessage);
	        List<String> appIds = new ArrayList<String>();
	        appIds.add(apc.getAppId());

	        // 定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
	        AppMessage message = new AppMessage();
	        message.setData(template);
	        message.setAppIdList(appIds);
	        message.setOffline(true);
	        message.setOfflineExpireTime(1000 * 600);

	        IPushResult ret = null;
	        try{
	        	ret = pusher.pushMessageToApp(message);
	        	if (ret != null) {
	            	logger.info("pushMessage2App:"+apc.getAppId()+" - "+ret.getResponse().toString());
	            } else {
	            	logger.error("pushMessage2App:"+apc.getAppId()+" - Server Response Exception");
	            }
	        }catch(Exception e){
	        	logger.error("pushMessage2App:"+apc.getAppId() + " - "+e.getMessage());
	        }
		}
		
		return;
	}
	
    /**
     * 推送给单个app用户
     * @param client appClient
     * @param appPushMessage 推送消息实体
     * @return
     */
    private void push2Single(AppClient client,
			AppPushMessage appPushMessage) {
		
		if(!appConfigValidate()){
			return;
		};
		
		/**
		 * 获取AppClient对应的推送器和推送配置
		 */
		AppPushConfig apc = appPushConfigMap.get(client.getAppId());
		IGtPush pusher = appPusherContainner.get(client.getAppId());
		
		/**
		 * 如果请求推送的客户端对应的appId没有推送配置，或者客户端处于离线状态，则不予推送
		 */
		if(apc == null || pusher == null || client == null || AppClient.STATUS_OFFLINE.equals(client.getStatus())){
			return;
		}
		
		//过滤掉信息
		filterMessage(appPushMessage);
		
		NotificationTemplate template = PushTemplateProvider.notificationTemplate(apc.getAppId(),apc.getAppKey(),appPushMessage);
		
		SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0); 
        Target target = new Target();
        target.setAppId(apc.getAppId());
        target.setClientId(client.getClientId());
        IPushResult ret = null;
        try {
        	if(AppClient.MOBILE_IND_IOS.equals(client.getMobileInd())){
        		if(!StringUtils.isEmpty(client.getClientId())){
        			ret = pusher.pushAPNMessageToSingle(apc.getAppId(), client.getClientId(), message);
        		}
        	}else{
        		ret = pusher.pushMessageToSingle(message, target);
        	}
            
            if (ret != null) {
            	logger.info("push2Single:"+client + " - " + ret.getResponse().toString());
            } else {
            	logger.error("push2Single:"+client+" - Server Response Exception");
            }
        } catch (RequestException e) {
        	logger.error("push2Single:"+client + " - "+ e.getMessage());
            ret = pusher.pushMessageToSingle(message, target, e.getRequestId());
        }catch (Exception e) {
        	logger.error("push2Single:"+client + " - "+ e.getMessage());
        }
	}
    
    private void filterMessage(AppPushMessage appPushMessage) {
		
    	String title = appPushMessage.getTitle();
	    String content = appPushMessage.getContent();
	   
		if(title!=null){ 
			title = CharUtils.subStringWithDecoretor(title, titleLimitLength, "...");
		}
		
	    if(content != null){
	       content = filterHTML(content).trim();//过滤掉html
		   content = CharUtils.subStringWithDecoretor(content, contentLimitLength, "...");
	    } 
	    
	    appPushMessage.setTitle(title);
	    appPushMessage.setContent(content);
	}
    
    
    private String filterHTML(String con) {
		String content = ""; 
		content=con.replaceAll("</?[^>]+>","");
		content=content.replace("&nbsp;",""); 
		content=content.replace("\n",""); 
		content=content.replace("\r","");
		content=content.replace("\"","‘");
		content=content.replace("'","‘");
		return content;
    }

	/**
	 * 检查是否有正确的推送配置
	 * @return
	 */
	private boolean appConfigValidate() {
		if(appPushConfigList == null || appPushConfigList.size() == 0){
			return false;
		}
		return true;
	}
	
}
