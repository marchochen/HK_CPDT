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
import com.cwn.wizbank.services.SnsShareService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;

/**
 * @author leon.li
 * 2014-9-9 上午10:40:08
 */
@RequestMapping("share")
@Controller
public class ShareController {

	@Autowired
	SnsShareService snsShareService;
	
	@RequestMapping("add/{module}/{targetId}")
	@ResponseBody
	public String share(Model model,loginProfile prof, WizbiniLoader wizbini,
			Params params,
			@PathVariable(value="module") String module,
			@PathVariable(value="targetId") long targetId,
			@RequestParam(value="note", required=false, defaultValue="") String note,
			@RequestParam(value="tkhId", required=false, defaultValue="0") long tkhId) throws Exception{
		snsShareService.share(model, prof.usr_ent_id, wizbini, targetId, module, note, tkhId, prof.cur_lan);
		return JsonFormat.format(params, model);
	}
	
}
