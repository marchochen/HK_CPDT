/**
 * 
 */
package com.cwn.wizbank.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Message;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.MessageService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

/**
 * @author leon.li 2014-8-1 上午9:45:56 公告
 */
@Controller
@RequestMapping("announce")
@HasPermission(AclFunction.FTN_LRN_ANN_MGT)
public class AnnounceController {

	@Autowired
	MessageService messageService;
	
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	@RequestMapping("")
	public ModelAndView announce(ModelAndView mav,
			@RequestParam(value = "id", required = false, defaultValue = "0") String id,
			loginProfile prof)
			throws DataNotFoundException {
		
		if(!"0".equals(id)){
			long real_id = EncryptUtil.cwnDecrypt(id);
			
			if (real_id > 0) {
				if (messageService.get(real_id) == null) {
					throw new DataNotFoundException(LabelContent.get(prof.cur_lan, "error_data_not_found"));
				}
			}
		}
		
		mav = new ModelAndView("announce/announce");
		return mav;
	}

	@RequestMapping("pageJson/{tcr_id}")
	@ResponseBody
	public String pageMessage(@PathVariable(value = "tcr_id") long tcr_id,
			@RequestParam(value = "isMobile", defaultValue="false") boolean isMobile,
			loginProfile prof,
			Page<Message> page, Model model, WizbiniLoader wizbini) {
		List<Long> tcr_id_list = new ArrayList<Long>();
		if (tcr_id > 0) {
			tcr_id_list.add(tcr_id);
		} else {
			List<TcTrainingCenter> list = tcTrainingCenterService.getTcrIdList(prof.usr_ent_id, prof.my_top_tc_id);
			for(int i=0;i<list.size();i++){
				tcr_id_list.add(list.get(i).getTcr_id());
			}
		}
		messageService.pageMessage(prof.getUsr_ent_id(),tcr_id_list, 1l, isMobile, page, wizbini.cfgSysSetupadv.getNewestDuration());
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping("detailJson/{encId}")
	@ResponseBody
	public String detail(@PathVariable(value="encId") String encId,
			loginProfile prof,
			Params param,
			Model model) {
		long id = EncryptUtil.cwnDecrypt(encId);
		model.addAttribute("detail", messageService.get(id));
		model.addAttribute("isReceipted", messageService.getReceipt(id, prof.usr_ent_id));
		//IsReturn为参数识别，返回当前公告是否已阅读 true为已阅读，false为未阅读（返回为String）
		model.addAttribute("isNotRead",checkRead(id, prof, param, model,"IsReturn"));
		return JsonFormat.format(param, model);
	}
	
	/**
	 * 检查公告是否已阅读
	 * @param id
	 * @param prof
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping("CheckRead/{id}")
	@ResponseBody
	public String checkRead(@PathVariable(value="id") long id,
			loginProfile prof,
			Params param,
			Model model,String type) {
		boolean isNotRead = true;
		//判断该条公告是否需要回执，不需要回执，则进入
			if(messageService.getIsOrNotReceipt(id)){
			try {
				//判断该条不需要回执公告是否已阅读 ，true(已阅读)则不插入，false（未阅读）则插入
				if("IsReturn".equals(type)){//当单击单条公告detail时
					if(!messageService.getNotReceipt(id, prof.usr_ent_id)){
							messageService.insReceipt(prof.usr_ent_id,id);
					}
				}
					if(!messageService.getNotReceipt(id, prof.usr_ent_id))isNotRead = false ;
			} catch (ParseException e) {
				CommonLog.error(e.getMessage(),e);
			}
		}else{
			if(!messageService.getReceipt(id, prof.usr_ent_id)) isNotRead = false;
		}
			model.addAttribute("detail", messageService.get(id));
			model.addAttribute("isNotRead",isNotRead);
		if("IsReturn".equals(type)){
			return String.valueOf(isNotRead);
		}else{
			return JsonFormat.format(param, model);
		}
	}
	
	@RequestMapping("courseMessageJson/{itmId}")
	@ResponseBody
	public String courseMessages(@PathVariable(value="itmId") long itmId,
			@RequestParam(value="resId",required=false, defaultValue="0") long resId,
			Params param,
			Model model) {
		model.addAttribute("messages", messageService.getMessageList(itmId, resId));
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping("getTcrIdList")
	@ResponseBody
	public String getTcrIdList(Model model, loginProfile prof,
			Params param
			) {
		model.addAttribute("tcr_id_list", tcTrainingCenterService.getTcrIdList(prof.usr_ent_id, prof.my_top_tc_id));
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping("insReceipt/{msgId}")
	@ResponseBody
	public void insReceipt(@PathVariable(value="msgId")long msgId,loginProfile prof) {
		try {
			if(!messageService.getReceipt(msgId, prof.usr_ent_id)){
				messageService.insReceipt(prof.usr_ent_id,msgId);
			}
		} catch (ParseException e) {
			CommonLog.error(e.getMessage(),e);
		}
	}
}
