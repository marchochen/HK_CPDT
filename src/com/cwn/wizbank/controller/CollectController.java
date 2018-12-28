/**
 * 
 */
package com.cwn.wizbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.services.SnsCollectService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;

/**
 * 讲师评分，评论
 * @author leon.li
 * 2014-9-5 上午11:12:31
 */
@Controller
@RequestMapping("collect")
public class CollectController {

	@Autowired
	SnsCollectService snsCollectService;

	@RequestMapping("add/{module}/{targetId}")
	@ResponseBody
	public String praise(loginProfile prof, WizbiniLoader wizbini, Model model,
			Params param,
			@PathVariable("targetId") String targetId,
			@PathVariable("module") String module) throws Exception {
		snsCollectService.add(model, EncryptUtil.cwnDecrypt(targetId), prof.usr_ent_id, module);
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping("cancel/{module}/{id}")
	@ResponseBody
	public String cancel(loginProfile prof, WizbiniLoader wizbini, Model model,
			Params param,
			@PathVariable("id") String id,
			@PathVariable("module") String module) throws Exception {
		snsCollectService.cancel(model, EncryptUtil.cwnDecrypt(id), module, prof.usr_ent_id);
		return JsonFormat.format(param, model);
	}


	
}
