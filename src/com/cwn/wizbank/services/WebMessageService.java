package com.cwn.wizbank.services;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.message.MessagePushScheduler;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.AppPushMessage;
import com.cwn.wizbank.entity.WebMessage;
import com.cwn.wizbank.persistence.RegUserMapper;
import com.cwn.wizbank.persistence.WebMessageMapper;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.wechat.service.WechatService;
/**
 *  service 实现
 */
@Service
public class WebMessageService extends BaseService<WebMessage> {

	@Autowired
	WebMessageMapper webMessageMapper;
	
	@Autowired
	RegUserMapper regUserMapper;
	
	@Autowired
	APITokenService apiTokenService;
	
	@Autowired
	WechatService wechatService;
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	AppPushService appPushService;

	public void setWebMessageMapper(WebMessageMapper webMessageMapper){
		this.webMessageMapper = webMessageMapper;
	}
	
	/**
	 * 添加提醒消息
	 * @param usr_ent_id 用户id
	 * @param usr_ent_id_str 用户id串
	 * @param wmsg_subject 消息主题
	 * @param wmsg_content 消息内容
	 * @param type 发送类型
	 * @return
	 */
	public void addWebMessage(long usr_ent_id, String usr_ent_id_str, String wmsg_subject, String wmsg_content, String type, String... contents){
		WebMessage webMessage = new WebMessage();
		webMessage.setWmsg_send_ent_id(usr_ent_id);
		webMessage.setWmsg_type(type);
		webMessage.setWmsg_subject(wmsg_subject);
		webMessage.setWmsg_content_pc(wmsg_content);
		if (contents != null) {
			if(contents.length > 0){
				webMessage.setWmsg_content_mobile(contents[3]);
			}else{
				webMessage.setWmsg_content_mobile(wmsg_content);
			}
		}
		webMessage.setWmsg_create_ent_id(usr_ent_id);
		webMessage.setWmsg_create_timestamp(getDate());
		String[] usr_ent_id_list = usr_ent_id_str.split("~");
		
		List<String> pushMsgList = null;		
		MessagePushScheduler msgPushThread = new MessagePushScheduler();
		
		for(int i=0;i<usr_ent_id_list.length;i++){
			webMessage.setWmsg_rec_ent_id(Long.parseLong(usr_ent_id_list[i]));
			webMessageMapper.insert(webMessage);
			
			pushMsgList = new ArrayList<String>();
			pushMsgList.add(Long.toString(usr_ent_id));
			pushMsgList.add(usr_ent_id_list[i]);
			pushMsgList.add(webMessage.getWmsg_id());
			pushMsgList.add(wmsg_subject);
			pushMsgList.add(wmsg_content);
			msgPushThread.addPushMessage(pushMsgList);
			
		}
	}
	
	/**
	 * 推送站内信到APP
	 * @param parseLong
	 * @param webMessage
	 */
	@SuppressWarnings("unused")
	private void pushToApp(long usrEntId, WebMessage webMessage) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type","message");
		param.put("msg_id", webMessage.getWmsg_id());
		
		AppPushMessage apm = new AppPushMessage(webMessage.getWmsg_subject(), webMessage.getWmsg_content_pc(), param);
		appPushService.pushMessage2User(usrEntId, apm);
	}

	/**
	 * 获取站内消息列表
	 * @param usr_ent_id 用户id
	 * @param type 消息类型
	 * @return
	 */
	public Page<WebMessage> getWebMessageList(Page<WebMessage> page, long usr_ent_id, String type){
		page.getParams().put("usr_ent_id", usr_ent_id);
		page.getParams().put("type", type);
		SimpleDateFormat format = new SimpleDateFormat(cwUtils.DEFAULT_DATETIME_FORMAT_ymdhms);
		Date date=new Date(System.currentTimeMillis());
		page.getParams().put("now", format.format(date));
		webMessageMapper.selectWebMessageList(page);
		return page;
	}
	
	/**
	 * 获取站内消息详情
	 * @param wmsg_id 消息id
	 * @return
	 */
	public WebMessage getWebMessageDetail(long wmsg_id){
		WebMessage message= webMessageMapper.get(wmsg_id);
		if(message.getWmsg_type().equals("SYS")){
			if(message.getWmsg_admin_content_pc() != null){
				message.setWmsg_admin_content_pc(message.getWmsg_admin_content_pc().replaceAll("[\\t\\n\\r]", " "));
			}
			if(message.getWmsg_content_mobile() != null){
				message.setWmsg_content_mobile(message.getWmsg_content_mobile().replaceAll("[\\t\\n\\r]", " "));
			}
			if(message.getWmsg_content_pc() != null){
				message.setWmsg_content_pc(message.getWmsg_content_pc().replaceAll("[\\t\\n\\r]", " "));
			}
		}
		return message;
	}

	public Page<WebMessage> getMessageList(Page<WebMessage> page, long usr_ent_id) {
		page.getParams().put("usr_ent_id", usr_ent_id);
		webMessageMapper.selectMessageList(page);
		return page;
	}

	public void delById(long id,String message_type) {
		/**删除信息关联表
		webMessageMapper.delReadHistory(id);*/
		if(message_type.equals("send_message")){
			//修改发件箱消息状态
			webMessageMapper.updateSendMessage(id);
		}else{
			//修改收件箱消息状态
			webMessageMapper.updateRecMessage(id);
		}
		
	}
	
	
	/**发送消息*/
	public void sendMessage(long usr_ent_id,String usr_ent_id_str, String wmsg_subject,
			String wmsg_content_pc) {
		WebMessage webMessage = new WebMessage();
		webMessage.setWmsg_send_ent_id(usr_ent_id);
		webMessage.setWmsg_content_pc(wmsg_content_pc);
		webMessage.setWmsg_content_mobile(wmsg_content_pc);
		webMessage.setWmsg_subject(wmsg_subject);
		webMessage.setWmsg_type("PERSON");
		webMessage.setWmsg_create_timestamp(getDate());
		webMessage.setWmsg_create_ent_id(usr_ent_id);
		String[] usr_ent_id_list = usr_ent_id_str.split("~");
		
		List<String> pushMsgList = null;		
		MessagePushScheduler msgPushThread = new MessagePushScheduler();
		
		for(int i = 0 ;i<usr_ent_id_list.length;i++){
			webMessage.setWmsg_rec_ent_id(Long.parseLong(usr_ent_id_list[i]));
			webMessageMapper.insert(webMessage);
			
			pushMsgList = new ArrayList<String>();
			pushMsgList.add(Long.toString(usr_ent_id));
			pushMsgList.add(usr_ent_id_list[i]);
			pushMsgList.add(webMessage.getWmsg_id());
			pushMsgList.add(wmsg_subject);
			pushMsgList.add(wmsg_content_pc);
			msgPushThread.addPushMessage(pushMsgList);
			
		}
	}
	
	/**查询发件箱消息*/
	public Page<WebMessage> getSendMessageList(Page<WebMessage> page, long usr_ent_id) {
		page.getParams().put("usr_ent_id", usr_ent_id);
		webMessageMapper.selectSendMessageList(page);
		return page;
		
	}
	
	/**
	 * 获取用户未读的站内信个数
	 * @param usr_ent_id
	 * @return
	 */
	public int getUserNotReadCount(long usr_ent_id){
		Page<WebMessage> page = new Page<WebMessage>();
		getWebMessageList(page, usr_ent_id, "MY");
		return page.getTotalRecord();
	}
	
	/**
	 * 获取站内信各类型总数
	 * @param usr_ent_id
	 * @return
	 */
	public Map<String, Long> getWebMessageTypeCount(long usr_ent_id){
		List<WebMessage> list = webMessageMapper.getWebMessageTypeCount(usr_ent_id);
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("PERSON", (long) 0);
		map.put("SYS", (long) 0);
		if(list != null){
			for (WebMessage webMessage : list) {
				if("PERSON".equalsIgnoreCase(webMessage.getWmsg_type())){
					map.put("PERSON", webMessage.getWmsg_type_total());
				}else if("SYS".equalsIgnoreCase(webMessage.getWmsg_type())){
					map.put("SYS", webMessage.getWmsg_type_total());
				}
			}
		}
		return map;
	}
	
	/**
	 * 将站内信持久化
	 */
	public WebMessage insertMessage(long usr_ent_id,long rec_usr_ent_id, String wmsg_subject,
			String wmsg_content_pc,String wmsg_type){
		WebMessage webMessage = new WebMessage();
		webMessage.setWmsg_send_ent_id(usr_ent_id);
		webMessage.setWmsg_rec_ent_id(rec_usr_ent_id);
		webMessage.setWmsg_content_pc(wmsg_content_pc);
		webMessage.setWmsg_content_mobile(wmsg_content_pc);
		webMessage.setWmsg_subject(wmsg_subject);
		webMessage.setWmsg_type(wmsg_type);
		webMessage.setWmsg_create_timestamp(getDate());
		webMessage.setWmsg_create_ent_id(usr_ent_id);
		
		this.webMessageMapper.insert(webMessage);
		 
		return webMessage;
	}
	
	/**
	 * 检查标题是否存在
	 * @param title
	 * @param id
	 * @param isTcIndependent
	 * @param top_tcr_id
	 * @return
	 */
	public boolean isExistTitle(String title,long id,long msg_tcr_id){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("title", title);
		map.put("id", id);
        map.put("msg_tcr_id", msg_tcr_id);
		
		boolean isExist = false;
		if(this.webMessageMapper.isExistFormMessage(map) > 0){
			isExist = true;
		}
		return isExist;
	}
	
	
}