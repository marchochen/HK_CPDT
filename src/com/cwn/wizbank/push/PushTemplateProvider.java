package com.cwn.wizbank.push;

import java.util.Map;

import com.cwn.wizbank.entity.AppPushMessage;
import com.cwn.wizbank.utils.JsonFormat;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

/**
 * 
 * @author andrew.xiao
 * 
 * 推送模板提供者
 *
 */
public class PushTemplateProvider {

	
		/**
	    * 通知打开应用模板
	    * @return
	    */
	   public static NotificationTemplate notificationTemplate(String appId,String appKey,AppPushMessage appPushMessage) {
	       NotificationTemplate template = new NotificationTemplate();
	       template.setAppId(appId);
	       template.setAppkey(appKey);
	       // 设置通知栏标题与内容
	       template.setTitle(appPushMessage.getTitle());
	       template.setText(appPushMessage.getContent());
	       // 设置通知是否响铃，震动，或者可清除
	       template.setIsRing(true);
	       template.setIsVibrate(true);
	       template.setIsClearable(true);
	       // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	       template.setTransmissionType(1);
	       
	       String payLoad = "{}";
	       if(appPushMessage.getParams() != null){
	       	payLoad = JsonFormat.format(appPushMessage.getParams());
	       }
	       template.setTransmissionContent(payLoad);
	       
	       //为IOS设置
	       template.setAPNInfo(getAPNInfo(appPushMessage));
	       
	       return template;
	   }
	
		/**
	    * 透传消息模版
	    * @return
	    */
	   public static TransmissionTemplate transmissionTemplate(String appId,String appKey,AppPushMessage appPushMessage) {
	       TransmissionTemplate template = new TransmissionTemplate();
	       template.setAppId(appId);
	       template.setAppkey(appKey);
	       // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	       template.setTransmissionType(1);
	       String payLoad = "{}";
	       if(appPushMessage.getParams() != null){
	       	payLoad = JsonFormat.format(appPushMessage.getParams());
	       }
	       template.setTransmissionContent("{'title':'"+appPushMessage.getTitle()+"','content':'"+appPushMessage.getContent()+"','payload':"+payLoad+"}");
	       //为IOS设置
	       template.setAPNInfo(getAPNInfo(appPushMessage));
	       return template;
	   }
	
	   private static APNPayload getAPNInfo(AppPushMessage appPushMessage){
	   	 	APNPayload payload = new APNPayload();
	   	 	if(appPushMessage != null && appPushMessage.getParams() != null){
		   	 	for (Map.Entry<String,Object> entry : appPushMessage.getParams().entrySet()) {
			   	    String key = entry.getKey().toString();
			   	    Object value = entry.getValue();
			   	    payload.addCustomMsg(key, value);
		   	    }
	   	 	}
	        payload.setSound("default");
	        payload.setAlertMsg(getDictionaryAlertMsg(appPushMessage));
	        return payload;
	   }
	   
	   private static APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(AppPushMessage appPushMessage){
	       APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
	       alertMsg.setTitle(appPushMessage.getTitle());
	       alertMsg.setBody(appPushMessage.getContent());
	       return alertMsg;
	   }
}
