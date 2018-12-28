package com.cwn.wizbank.controller;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.WebMessage;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.ErrorException;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.WebMessageService;
import com.cwn.wizbank.services.WebMsgReadHistoryService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
/**
 * 
 * @author lance 积分
 */
@Controller
@RequestMapping("message")
public class MessageController {
	
	@Autowired
	WebMessageService webMessageService;
	
	@Autowired
	WebMsgReadHistoryService webMsgReadHistoryService;
	
	@Autowired
	RegUserService regUserService;	
	
	/**
	 * 站内消息页面
	 *  @param command : writeMessage 发送消息
	 * @return
	 */
	@RequestMapping(value = "{command}", method = RequestMethod.GET)
	public ModelAndView message(ModelAndView mav,loginProfile prof, @PathVariable(value = "command") String command) {
		mav = new ModelAndView("message/" + command);
		mav.addObject("command",command);
		
		if(command.equalsIgnoreCase("webMessage")){
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
	 * @throws AuthorityException 
	 */
	@RequestMapping(value = "getMyWebMessage/{command}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getMyWebMessage(Model model, Page page, loginProfile prof,@PathVariable(value="command")String command) throws ErrorException {
		if(command != null && !"".equals(command) ){
			if(command.equals("rec_message")){
				// 解密ID字段
				Object obj_wmsgIdValue = page.getParams().get("wmsgIdValue");
				if(obj_wmsgIdValue != null) {
					String wmsgIdValue = obj_wmsgIdValue.toString();
					wmsgIdValue = new String(new Base64().decode(wmsgIdValue.getBytes()));
					if(!StringUtils.isNumeric(wmsgIdValue)){
						wmsgIdValue = "0";
					}
					page.getParams().put("wmsgIdValue", wmsgIdValue);
				}
				//查询收件箱
				webMessageService.getMessageList(page, prof.usr_ent_id);
			}else if(command.equals("send_message")){
				//查询发件箱
				webMessageService.getSendMessageList(page, prof.usr_ent_id);
			}else{
				//
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
	 * 站内消息详情
	 * @param wmsg_id 站内消息id
	 * @return
	 * @throws AuthorityException 
	 */
	@RequestMapping(value = "getWebMessageDetail/{wmsg_id}")
	@ResponseBody
	public String getWebMessageDetail(Model model, loginProfile prof,
			Params params,@PathVariable(value = "wmsg_id") String wmsg_id) throws AuthorityException{
		wmsg_id = new String(Base64.decodeBase64(wmsg_id.getBytes()));
		WebMessage webMessage = webMessageService.getWebMessageDetail(Long.parseLong(wmsg_id));
		if(!(prof.usr_ent_id == webMessage.getWmsg_rec_ent_id() || prof.usr_ent_id == webMessage.getWmsg_send_ent_id())){
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		model.addAttribute(webMessage);
		return JsonFormat.format(params, model);
	}
	
	/**
	 * 第一次点击站内消息
	 * @param wmsg_id 站内消息id
	 * @return
	 */
	@RequestMapping(value = "readWebMessage/{wmsg_id}")
	@ResponseBody
	public String readWebMessage(Model model,
			Params params,@RequestParam(value="message_type")String message_type,
			@PathVariable(value = "wmsg_id") String wmsg_id) {
		wmsg_id = new String(Base64.decodeBase64(wmsg_id.getBytes()));
		/**如果是在发件箱点击消息则不标记*/
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
	
	@RequestMapping(value = "myWebMessageTotal", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String myWebMessageTotal(Model model, Page page, loginProfile prof,
			Params params) {
		if(prof != null){
			webMessageService.getWebMessageList(page, prof.usr_ent_id, "MY");
			model.addAttribute("total", page.getTotalRecord());
		}
		return JsonFormat.format(params, model);
	}
	
	/**删除站内信*/
	@RequestMapping(value="delById")
	@ResponseBody
	public void delById(Model model,@RequestParam(value="id")String id,@RequestParam(value="message_type")String message_type){
		id = new String(Base64.decodeBase64(id.getBytes()));
		webMessageService.delById(Long.parseLong(id), message_type);
	}
	
	/**发送消息*/
	@RequestMapping(value="sendMessage")
	@ResponseBody
	public void sendMessage(Model model,loginProfile prof,@RequestParam(value="usr_ent_id_str")String usr_ent_id_str,@RequestParam(value="wmsg_subject")String wmsg_subject,
								@RequestParam(value="wmsg_content_pc")String wmsg_content_pc){
		webMessageService.sendMessage(prof.getUsr_ent_id(),usr_ent_id_str,wmsg_subject,wmsg_content_pc);
	}
	
	/**发送站内信获取用户列表
	 *
	 */
	@RequestMapping("findUserList")
	@ResponseBody
	public String findUserList(Model model, Page<RegUser> page, loginProfile prof,WizbiniLoader wizbini,@RequestParam("search_name")String search_name){
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			long tcp_id = prof.my_top_tc_id;//改成获取当前培训中心
			page.getParams().put("cfgTcEnabled", true);
			page.getParams().put("tcp_id", tcp_id);
		}
		regUserService.findUserList(page,search_name);
		return JsonFormat.format(model, page);
	}
	

	/**
	 * 获取当前用户未读信息个数
	 * @param prof
	 * @return
	 */
	@RequestMapping("getUserNotReadCount")
	@ResponseBody
	public int getUserNotReadCount(loginProfile prof){
		return webMessageService.getUserNotReadCount(prof.getUsr_ent_id());
	}
}
