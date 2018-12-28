package com.cwn.wizbank.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Resources;
import com.cwn.wizbank.services.ResourcesService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

@Controller("adminModuleController")
@RequestMapping("/admin/module")
public class ModuleController {

	@Autowired
	ResourcesService resourcesService;
	
	/**
	 * 作业
	 * @return
	 */
	@RequestMapping("ass")
	public String toPage(Page<Resources> page, Model model, loginProfile prof){
		return "admin/module/assList";
	}
	/**
	 * 作业
	 * @return
	 */
	@RequestMapping("assPage")
	@ResponseBody
	public String pageJson(Page<Resources> page, Model model, loginProfile prof){
		resourcesService.getModulePage(page, prof.usr_ent_id, "ASS", prof.current_role);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 作业
	 * @return
	 */
	@RequestMapping("test")
	public String toTestPage(Page<Resources> page, Model model, loginProfile prof){
		return "admin/module/testList";
	}
	/**
	 * 作业
	 * @return
	 */
	@RequestMapping("testPage")
	@ResponseBody
	public String pageTestJson(Page<Resources> page, Model model, loginProfile prof){
		resourcesService.getModulePage(page, prof.usr_ent_id, "DXT,TST", prof.current_role);
		return JsonFormat.format(model, page);
	}
	
	
	
	
}
