package com.cwn.wizbank.controller.admin;

import com.alibaba.fastjson.JSON;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.WebMessage;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.WebMessageService;
import com.cwn.wizbank.services.WebMsgReadHistoryService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Role Manage Controller 角色管理控制器
 */
@Controller("adminMessageController")
@RequestMapping("admin/message")
public class MessageController {
	
	@Autowired
	WebMessageService webMessageService;
	
	@Autowired
	WebMsgReadHistoryService webMsgReadHistoryService;
	@Autowired
	RegUserService regUserService;
	
	@RequestMapping(value="{command}",method=RequestMethod.GET)
	public ModelAndView main(ModelAndView mav,loginProfile prof,@PathVariable(value="command")String command){
		mav = new ModelAndView("admin/message/" + command);
		if(command.equalsIgnoreCase("main")){
			Map<String, Long> map = webMessageService.getWebMessageTypeCount(prof.usr_ent_id);
			mav.addObject("wmsg_person_total", map.get("PERSON"));
			mav.addObject("wmsg_sys_total", map.get("SYS"));
		}
		return mav;
	}
	
	
	/**
	 * 站内消息列表
	 * @param model
	 * @param page
	 * @param prof
	 * @param command
	 * @return
	 */
	@RequestMapping(value="getMyWebMessage/{command}")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getMyWebMessage(Model model,Page page,loginProfile prof,@PathVariable(value="command")String command){
		if(command != null && !"".equals(command) ){
			if(command.equals("rec_message")){
				//查询收件箱
				webMessageService.getMessageList(page, prof.usr_ent_id);
			}else if(command.equals("send_message")){
				//查询发件箱
				webMessageService.getSendMessageList(page, prof.usr_ent_id);
			}else{
				webMessageService.getWebMessageList(page, prof.usr_ent_id, command);
			}
		}
		//ID字段加密
		for(Object bean : page.getResults()){
			WebMessage message = (WebMessage)bean;
			if(message.getWmsg_id() != null){
				String idStr = new String(Base64.encodeBase64(message.getWmsg_id().getBytes()));
				message.setWmsg_id(idStr);
			}
		}
		
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 第一次点击站内消息
	 * @param wmsg_id 站内消息id
	 * @return
	 */
	@RequestMapping(value="readWebMessage/{wmsg_id}")
	@ResponseBody
	public String readWebMessage(Model model,Params params,
			@RequestParam(value="message_type")String message_type,
		 @PathVariable(value = "wmsg_id") String wmsg_id){
		wmsg_id = new String(Base64.decodeBase64(wmsg_id.getBytes()));
		/**如果是在发件箱点击消息则不标记已读*/
		if(message_type != null && message_type.equals("rec_message")){
			if(webMsgReadHistoryService.checkMyReadHistory(Long.parseLong(wmsg_id))){
				webMsgReadHistoryService.insertWebMsgReadHistory(Long.parseLong(wmsg_id));
				model.addAttribute("message", "ok");
			} else {
				model.addAttribute("message", "error");
			}
		}
		return JsonFormat.format(params, model);
	}
	

	/**
	 * 站内消息详情
	 * @param wmsg_id 站内消息id
	 * @return
	 */
	@RequestMapping(value="getWebMessageDetail/{wmsg_id}")
	@ResponseBody
	public String getWebMessageDetail(Model model,loginProfile prof,
			Params params,
			@PathVariable(value = "wmsg_id") String wmsg_id){
		wmsg_id = new String(Base64.decodeBase64(wmsg_id.getBytes()));
		model.addAttribute(webMessageService.getWebMessageDetail(Long.parseLong(wmsg_id)));
		return JsonFormat.format(params, model);
	}
	
	/**删除站内信*/
	@RequestMapping(value="delById")
	@ResponseBody
	public void delById(Model model,@RequestParam(value="id")String id,@RequestParam(value="message_type")String message_type){
		id = new String(Base64.decodeBase64(id.getBytes()));
		webMessageService.delById(Long.parseLong(id),message_type);
	}
	
	/**发送消息*/
	@RequestMapping(value="sendMessage")
	@ResponseBody
	public void sendMessage(Model model,loginProfile prof,@RequestParam(value="usr_ent_id_str")String usr_ent_id_str,@RequestParam(value="wmsg_subject")String wmsg_subject,
								@RequestParam(value="wmsg_content_pc")String wmsg_content_pc){
		webMessageService.sendMessage(prof.getUsr_ent_id(),usr_ent_id_str,wmsg_subject,wmsg_content_pc);
	}
	
	/**发送站内信获取用户列表*/
	@RequestMapping("findUserList")
	@ResponseBody
	public String findUserList(Model model, Page<RegUser> page,WizbiniLoader wizbini, loginProfile prof,@RequestParam("search_name")String search_name){
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			long tcp_id = prof.my_top_tc_id;//改成获取当前培训中心
			page.getParams().put("cfgTcEnabled", true);
			page.getParams().put("tcp_id", tcp_id);
		}
		regUserService.findUserList(page,search_name);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 检查标题是否存在
	 * @param model
	 * @param prof
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "checkExistTitle", method = RequestMethod.POST)
	@ResponseBody
	public String checkExistTitle(Model model, WizbiniLoader wizbini, loginProfile prof,
			@RequestParam(value = "title", required = false, defaultValue = "") String title,
			@RequestParam(value = "id", required = false, defaultValue = "0") long id,
			@RequestParam(value = "msg_tcr_id", required = false, defaultValue = "0") long msg_tcr_id) throws Exception {
		
		
 		boolean isExistTitle = webMessageService.isExistTitle(title, id,msg_tcr_id);
		
		model.addAttribute("success",isExistTitle);
		
		return JSON.toJSONString(model);
	}
	
}
