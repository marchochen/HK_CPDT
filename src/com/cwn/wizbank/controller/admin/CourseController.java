package com.cwn.wizbank.controller.admin;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AcRoleService;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

@Controller("adminCourseController")
@RequestMapping("admin/course")
public class CourseController {
	
	@Autowired
	AeItemService aeItemService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@Autowired
	AclService aclService;
	
	@Autowired
	AcRoleService acRoleService;
	
	@RequestMapping("")
	public String page(Model model, 
			@RequestParam(value="type",required=false, defaultValue="course") String type,
			@RequestParam(value="cos_type",required=false, defaultValue="") String cos_type,
			loginProfile prof
			) throws Exception{
		if(acRoleService.isRoleTcInd(prof.current_role)){
			if("exam".equalsIgnoreCase(type)){
				if(aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_EXAM_MGT)
					&& !forCallOldAPIService.hasEffTc(null, prof.usr_ent_id)) {
						throw new MessageException("label_core_requirements_management_58");
					}
			}else if("open".equalsIgnoreCase(type)){
				if(aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_OPEN_COS_MAIN)
						&& !forCallOldAPIService.hasEffTc(null, prof.usr_ent_id)) {
						throw new MessageException("label_core_requirements_management_60");
					}
			}else{
				if(aclService.hasAnyPermission(prof.current_role, AclFunction.FTN_AMD_ITM_COS_MAIN)
						&& !forCallOldAPIService.hasEffTc(null, prof.usr_ent_id)) {
						throw new MessageException("label_core_requirements_management_59");
					}
			}
		}

		model.addAttribute("type", type);
		model.addAttribute("cos_type", cos_type);
		return "admin/course/index";
	}
	
	@RequestMapping("pageJson")
	@ResponseBody
	public String page(Page<AeItem> page, loginProfile prof, Model model) throws ParseException{
		
		aeItemService.pageAdmin(page, prof.usr_ent_id, prof.current_role);
		
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping(value = "parseOnlineAPIJson", method = RequestMethod.POST)
	@ResponseBody
	public String parseOnlineAPIJson(Model model, @RequestParam(value = "url") String url) {
		
		aeItemService.parseOnlineAPI(model, url);
		
		return JsonFormat.format(model);
	}
	
	@RequestMapping("pageCourseJson")
	@ResponseBody
	public String pageCourseJson(Page<AeItem> page, loginProfile prof, Model model,
			@RequestParam(value="itm_type",required=false, defaultValue="course") String type) throws ParseException{
		
		aeItemService.pageAdminCourse(page, prof.usr_ent_id, prof.current_role,type);
		
		return JsonFormat.format(model, page);
	}
	

}
