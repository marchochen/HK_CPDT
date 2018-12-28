/**
 * 
 */
package com.cwn.wizbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;

/**
 * 用户
 */
@RequestMapping("userInfo")
@Controller
public class UserInfoController {
	
	@Autowired
	RegUserService regUserService;
	
	@RequestMapping("detail")
	@ResponseBody
	public String userInfo(Model model, loginProfile prof, 
			Params params){
		model.addAttribute(regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		return JsonFormat.format(params, model);

	}
	
}
