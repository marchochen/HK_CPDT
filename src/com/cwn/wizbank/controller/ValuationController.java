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
import com.cwn.wizbank.services.SnsValuationService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;

/**
 * 评价 【赞】
 * 
 * @author leon.li 2014-8-7 下午5:30:04
 */
@Controller
@RequestMapping("valuation")
public class ValuationController {

	@Autowired
	SnsValuationService snsValuationService;

	@RequestMapping("praise/{module}/{targetId}")
	@ResponseBody
	public String praise(loginProfile prof, WizbiniLoader wizbini, Model model,
			Params params,
			@PathVariable("targetId") String targetId,
			@PathVariable("module") String module,
			@RequestParam(value="isComment",required=false, defaultValue="0") int isComment,
			@RequestParam(value="tkhId",required=false, defaultValue="0") long tkhId) throws Exception {
		snsValuationService.praise(model, EncryptUtil.cwnDecrypt(targetId), prof.usr_ent_id, module, isComment, tkhId, prof.cur_lan, prof.usr_id);
		return JsonFormat.format(params, model);
	}
	
	@RequestMapping("cancel/{module}/{id}")
	@ResponseBody
	public String cancel(loginProfile prof, WizbiniLoader wizbini, Model model,
			Params params,
			@PathVariable("id") String id,
			@PathVariable("module") String module,
			@RequestParam(value="isComment",required=false, defaultValue="0") int isComment) throws Exception {
		snsValuationService.cancel(model, EncryptUtil.cwnDecrypt(id), module, prof.usr_ent_id, isComment);
		return JsonFormat.format(params, model);
	}

}
