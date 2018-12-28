package com.cwn.wizbank.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.InstructorCos;
import com.cwn.wizbank.entity.InstructorInf;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.InstructorInfService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

@Controller
@RequestMapping("instr")
public class InstructorController {
	
	@Autowired
	InstructorInfService instructorInfService;
	@Autowired
	AeItemService aeItemService;

	@RequestMapping("")
	public String toPage(){
		
		return "instr/index";
	}
	
	@RequestMapping("detail/{encId}")
	public String detail(@PathVariable("encId") String encId, loginProfile prof, Model model) {
		
		long id = EncryptUtil.cwnDecrypt(encId);
		
		model.addAttribute("id", id);
		InstructorInf inst = instructorInfService.getInstructorDetail(id, prof.usr_ent_id);
		if(null!=inst.getIti_expertise_areas()&&inst.getIti_expertise_areas().length()>150)
		{
			model.addAttribute("isT", false);
			model.addAttribute("iti_expertise_areas", inst.getIti_expertise_areas().substring(0, 150)+"......");
		}else{
			model.addAttribute("isT", true);
		}
		if(null!=inst.getIti_main_course())
		{
			inst.setIti_main_course(inst.getIti_main_course().replaceAll("\r\n", "<br/>"));
		}
		if(null!=inst.getIti_expertise_areas())
		{
			inst.setIti_expertise_areas(inst.getIti_expertise_areas().replaceAll("\r\n", "<br/>"));
		}
		if(null!=inst.getIti_good_industry())
		{
			inst.setIti_good_industry(inst.getIti_good_industry().replaceAll("\r\n", "<br/>"));
		}
		if(null!=inst.getIti_introduction())
		{
			inst.setIti_introduction(inst.getIti_introduction().replaceAll("\r\n", "<br/>"));
		}
		model.addAttribute("instr", inst);
		return "instr/detail";
	}
	
	@RequestMapping("detailJson/{encId}")
	@ResponseBody
	public String detailJson(@PathVariable("encId") String encId, loginProfile prof, Model model) {
		long id = EncryptUtil.cwnDecrypt(encId);
		InstructorInf instructorDetail = instructorInfService.getInstructorDetail(id, prof.usr_ent_id);
		model.addAttribute(instructorDetail);
		return JsonFormat.format(model);
	}
	
	@RequestMapping("page")
	@ResponseBody
	public String instrPage(loginProfile prof, Model model, Page<InstructorInf> page,WizbiniLoader wizbini) {
		//判断是否LN模式
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			Map<String, Object>params = page.getParams();
			if(null!=params){
				params.put("top_tc_id", prof.my_top_tc_id);
			}else{
				params = new HashMap<String, Object>();
				params.put("top_tc_id", prof.my_top_tc_id);
				page.setParams(params);
			}
		}
		instructorInfService.page(page);
		return JsonFormat.format(model, page);
	}

	@RequestMapping("pageItemCos/{encId}")
	@ResponseBody
	public String instrItemCos(@PathVariable("encId") String encId ,loginProfile prof, Model model, Page<AeItem> page){
		long instrId = EncryptUtil.cwnDecrypt(encId);
		aeItemService.getInstructorItemCosPage(page, instrId, prof.usr_ent_id);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping("pageCos/{id}")
	@ResponseBody
	public String instrCos(@PathVariable("id") long instrId ,loginProfile prof, Model model, Page<InstructorCos> page){
		instructorInfService.pageCos(page, instrId);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping("instructor/{id}")
	@ResponseBody
	public String instructorInfo(Model model, loginProfile prof, 
			@PathVariable("id") long id,
			Params params) throws DataNotFoundException{
		model.addAttribute(instructorInfService.getInstructorDetail(id, prof.usr_ent_id));
		return JsonFormat.format(params, model);
	}
	@RequestMapping("getInstructors")
	@ResponseBody
	public String getInstructors(loginProfile prof, Model model, Page<InstructorInf> page,WizbiniLoader wizbini) {
		//判断是否LN模式
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			Map<String, Object>params = page.getParams();
			if(null!=params){
				params.put("top_tc_id", prof.my_top_tc_id);
			}else{
				params = new HashMap<String, Object>();
				params.put("top_tc_id", prof.my_top_tc_id);
				page.setParams(params);
			}
		}
		instructorInfService.getInstructors(page);
		return JsonFormat.format(model, page);
	}
}
