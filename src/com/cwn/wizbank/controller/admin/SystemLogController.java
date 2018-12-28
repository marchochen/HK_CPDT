package com.cwn.wizbank.controller.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.services.SystemLogService;

/**
 * 系统运作日志
 * @author Leaf
 *
 */
@Controller("SystemLogController")
@RequestMapping("admin/system")
public class SystemLogController {

	@Autowired
	SystemLogService systemLogService;
	
	
	/**
	 * 导出登录日志/重要功能操作日志/用量警告日志
	 * @param model
	 * @param prof
	 * @param wizbini
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "exporOperationOrLoginLog")
	@ResponseBody
	public Model exporOperationOrLoginLog(String type,String lastdays,String starttime,
			String endtime,Model model, loginProfile prof, WizbiniLoader wizbini) throws Exception{
		 int day = 0;
		 if(null !=lastdays && !lastdays.equals("")){
			 day = Integer.parseInt(lastdays);
		 }
		 String fileName = systemLogService.expor(prof,wizbini,day,starttime,endtime,type);
		 model.addAttribute("fileUri", "/temp/"+fileName);
		 return model;
	}
	
	
}
