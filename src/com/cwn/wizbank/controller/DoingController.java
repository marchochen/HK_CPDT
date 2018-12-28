/**
 *
 */
package com.cwn.wizbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.SnsDoing;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SnsDoingService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.utils.RequestStatus;

/**
 * @author leon.li 动态信息
 * @see
 */
@RequestMapping("doing")
@Controller
@HasPermission(AclFunction.FTN_LRN_DOING_MGT)
public class DoingController {

	@Autowired
	SnsDoingService snsDoingService;

	@Autowired
	AeItemService aeItemService;

	@Autowired
	RegUserService regUserService;


	@RequestMapping("dynamic/{type}")
	public String dynamic(Model model, Page<SnsDoing> page,
			WizbiniLoader wizbini,
			@PathVariable(value="type") String type,
			@RequestParam(value = "tcr_id", defaultValue="0") long tcr_id,
			loginProfile prof){
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcr_id = prof.my_top_tc_id;
		}
		//snsDoingService.listAll(prof.usr_ent_id, page, wizbini, tcr_id, prof.cur_lan, type);
		model.addAttribute("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		return "dynamic/dynamic";
	}

	@RequestMapping("dynamicJson/{type}")
	@ResponseBody
	public String dynamicJson(Model model, Page<SnsDoing> page,
			WizbiniLoader wizbini,
			@PathVariable(value="type") String type,
			@RequestParam(value = "tcr_id", defaultValue="0") long tcr_id,
			loginProfile prof){
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcr_id = prof.my_top_tc_id;
		}
		//snsDoingService.listAll(prof.usr_ent_id, page, wizbini, tcr_id, prof.cur_lan, type);
		return JsonFormat.format(model, page);
	}
	@HasPermission(value = {AclFunction.FTN_LRN_GROUP_MGT, AclFunction.FTN_AMD_SNS_GROUP_MAIN})
	@RequestMapping("pageJson")
	@ResponseBody
	public String page(Model model, Page<SnsDoing> page, WizbiniLoader wizbini,
			@RequestParam(value = "tcr_id", defaultValue="0") long tcr_id, loginProfile prof) {
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcr_id = prof.my_top_tc_id;
		}
		//snsDoingService.listIndex(prof.usr_ent_id, page, wizbini, tcr_id, prof.cur_lan, null);
		return JsonFormat.format(model, page);
	}
	@HasPermission(value = {AclFunction.FTN_LRN_GROUP_MGT, AclFunction.FTN_AMD_SNS_GROUP_MAIN,AclFunction.FTN_AMD_SNS_GROUP_VIEW})
	@RequestMapping("user/json/{id}/{module}/{encTargetId}")
	@ResponseBody
	public String page(Model model, Page<SnsDoing> page, WizbiniLoader wizbini, loginProfile prof,
			@PathVariable(value="id") long uid,
			@PathVariable(value="module") String module,
			@PathVariable(value="encTargetId") String encTargetId) {
		long targetId = EncryptUtil.cwnDecrypt(encTargetId);
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		//当时群组评论时，放行。             bill/2016-04-19
		if(module != null && module.equals("Group"))
			snsDoingService.listUserAll(prof.usr_ent_id, page, wizbini, uid, tcrId, prof.cur_lan, module, targetId);
		return JsonFormat.format(model, page);
	}

	@HasPermission(value = {AclFunction.FTN_LRN_GROUP_MGT, AclFunction.FTN_AMD_SNS_GROUP_MAIN,AclFunction.FTN_AMD_SNS_GROUP_VIEW})
	@RequestMapping("add")
	@ResponseBody
	public String publish(Model model,
			loginProfile prof,
			Params param,
			@RequestParam(value="note", required = true, defaultValue="") String note,
			@RequestParam(value="action", required = true, defaultValue="") String action,
			@RequestParam(value="module", required = true, defaultValue="") String module,
			@RequestParam(value="targetId", required = true, defaultValue="") String targetId){	  
		 note = cwUtils.esc4JS(cwUtils.esc4Json(note));
		if(module != null && module.equals("Group"))
			snsDoingService.publish(model, EncryptUtil.cwnDecrypt(targetId), 0, prof.usr_ent_id, 0, action, 0, module, note, 0);
		return JsonFormat.format(param, model);
	}

	@HasPermission(value = {AclFunction.FTN_LRN_GROUP_MGT, AclFunction.FTN_AMD_SNS_GROUP_MAIN,AclFunction.FTN_AMD_SNS_GROUP_VIEW})
	@RequestMapping("del/{id}")
	@ResponseBody
	public String delete(Model model,
			loginProfile prof,
			Params param,
			@PathVariable(value="id") long id) throws MessageException{
		snsDoingService.delete(id, prof.usr_ent_id, SNS.MODULE_DOING, prof.cur_lan);
		model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return JsonFormat.format(param, model);
	}

}
