package com.cwn.wizbank.cpd.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.CpdGroupPeriodService;
import com.cwn.wizbank.cpd.service.CpdGroupService;
import com.cwn.wizbank.cpd.service.CpdManagementService;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupPeriod;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;


/**
 * CPT/D牌照组別（小牌）周期
 * @author Nat
 *
 */
@RequestMapping("admin/cpdGroupPeriod")
@Controller
public class CpdGroupPeriodController {

	@Autowired
	CpdManagementService cpdManagementService;
	
	@Autowired
	CpdGroupService CpdGroupService;
	
	@Autowired
	CpdGroupPeriodService cpdGroupPeriodService;
	
	
	
	@RequestMapping(method = RequestMethod.GET, value = "insert")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	public String insert(Model model,  @RequestParam(required=false , value = "cgp_id") String cgp_id,  
			@RequestParam(required=false , value = "cg_id") String cg_id) {
		
		if(null != cg_id ){
			long encryp_cgId=(long)EncryptUtil.cwnDecrypt(cg_id);
			model.addAttribute("cpdGroup", CpdGroupService.get(encryp_cgId));
		}
		CpdGroupPeriod cpdGroupPeriod = new CpdGroupPeriod();
		if (cgp_id != null ) {
			long encryp_cgp_id=(long)EncryptUtil.cwnDecrypt(cgp_id);
			model.addAttribute("type", "update");
			cpdGroupPeriod = cpdGroupPeriodService.getByCgpId(encryp_cgp_id);
			if(null != cpdGroupPeriod.getCgp_cg_id()){
				CpdType cpdType = cpdManagementService.get(cpdGroupPeriod.getCgp_ct_id());
				CpdGroup cpdGroup = CpdGroupService.get(cpdGroupPeriod.getCgp_cg_id());
				model.addAttribute("cpd",cpdType);
				model.addAttribute("cpdGroup",cpdGroup);
				//时间只需要年
				model.addAttribute("cgp_effective_time",DateUtil.getInstance().getDateYear(cpdGroupPeriod.getCgp_effective_time()));
			}
		} else {
			model.addAttribute("type", "add");
		}
		model.addAttribute("cpdGroupPeriod",cpdGroupPeriod);
		return "admin/cpd/cpd_group_period_add";
	}
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String save(Model model, loginProfile prof,CpdGroupPeriod cpdGroupPeriod,int statrData ,int statrMonth) throws Exception {
		
		if (cpdGroupPeriod != null && cpdGroupPeriod.getCgp_id() != null) {
			model.addAttribute("type", "update");
		} else {
			model.addAttribute("type", "add");
		}
	    // 该日期已经存在  
		if (cpdGroupPeriodService.isExistForTime(cpdGroupPeriod,statrData,statrMonth)) {
			model.addAttribute("success",false);
		}else{
			cpdGroupPeriodService.saveOrUpdate(cpdGroupPeriod, prof, statrData, statrMonth);
		    model.addAttribute("success",true);
		}
		
		return JSON.toJSONString(model);
	}

	@RequestMapping("listJson")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String listJson(loginProfile prof, Model model, Page<CpdGroupPeriod> page, long cg_id) {
		cpdGroupPeriodService.searchAll(page,cg_id);
		return JsonFormat.format(model, page);
	}

	@RequestMapping(value = "detele")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String detele(Model model, loginProfile prof,CpdGroupPeriod cpdGroupPeriod) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
	   
		//删除成功
		cpdGroupPeriodService.delete(cpdGroupPeriod,prof);
		map.put("success",true);
		
		
		return JSON.toJSONString(map);
	}
	
	
	
}
