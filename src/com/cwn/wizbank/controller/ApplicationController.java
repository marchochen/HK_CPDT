/**
 * 
 */
package com.cwn.wizbank.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;

/**
 * @author leon.li
 * 2014-9-9 下午2:57:35
 */
@RequestMapping("application")
@Controller
public class ApplicationController {

	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@RequestMapping("app")
	@ResponseBody
	public String insApp(HttpServletRequest request, loginProfile prof, Model model, 
			Params param,
			WizbiniLoader wizbini, @RequestParam(value = "itm_id", defaultValue = "0", required = false) long itm_id) throws Exception {
		forCallOldAPIService.insAppForPage(null, prof, model, prof.usr_ent_id, itm_id);
		return JsonFormat.format(param, model);
	}

	@RequestMapping("cancel")
	@ResponseBody
	public String cancelApp(HttpServletRequest request, WizbiniLoader wizbini, loginProfile prof, 
			Model model, 
			Params param,
			@RequestParam(value = "app_id", defaultValue = "0", required = false) long app_id) throws Exception {
		forCallOldAPIService.cancelApp(null, prof, model, app_id);
		return JsonFormat.format(param, model);
	}
	
}
