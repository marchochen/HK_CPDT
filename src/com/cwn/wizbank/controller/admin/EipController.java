package com.cwn.wizbank.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.EnterpriseInfoPortal;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.services.EnterpriseInfoPortalService;
import com.cwn.wizbank.utils.RequestStatus;

@Controller("adminEipController")
@RequestMapping("admin/eip")
public class EipController {
	
	@Autowired
	EnterpriseInfoPortalService enterpriseInfoPortalService;
	
	@ResponseBody
	@RequestMapping("addEip")
	public Model addEip(Model model, loginProfile prof, 
			@ModelAttribute("eip") EnterpriseInfoPortal eip, BindingResult result){
		
		if (result.hasErrors()) {
			model.addAttribute(RequestStatus.STATUS, RequestStatus.ERROR);
			model.addAttribute(RequestStatus.MSG, result.getAllErrors().get(0).getDefaultMessage());
		} else {
			model.addAttribute("usg_list", enterpriseInfoPortalService.addEip(eip,  prof));
			model.addAttribute(RequestStatus.STATUS,  RequestStatus.SUCCESS);
		}
	
		return model;
	}
	
	
	@ResponseBody
	@RequestMapping("editEip")
	public Model editEip(Model model, loginProfile prof, 
			@ModelAttribute("eip") EnterpriseInfoPortal eip, BindingResult result) throws DataNotFoundException{
		
		if (result.hasErrors()) {
			model.addAttribute(RequestStatus.STATUS, RequestStatus.ERROR);
			model.addAttribute(RequestStatus.MSG, result.getAllErrors().get(0).getDefaultMessage());
		} else {
			enterpriseInfoPortalService.editEip(eip, prof.usr_ent_id);
			model.addAttribute(RequestStatus.STATUS,  RequestStatus.SUCCESS);
		}
	
		return model;
	}

}
