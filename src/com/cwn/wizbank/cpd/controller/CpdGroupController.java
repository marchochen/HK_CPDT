package com.cwn.wizbank.cpd.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.CpdGroupService;
import com.cwn.wizbank.cpd.service.CpdManagementService;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;


/**
 * CPT/D牌照组別（小牌）控制器
 * @author Nat
 *
 */
@RequestMapping("admin/cpdGroup")
@Controller
public class CpdGroupController {

	@Autowired
	CpdManagementService cpdManagementService;
	
	@Autowired
	CpdGroupService CpdGroupService;
	
	
	
	@RequestMapping("index")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	public String index(loginProfile prof) throws MessageException {
		
		
		return "admin/cpd/cpd_mgt_admin";
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "insert")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	public String insert(Model model, @RequestParam(required=false , value = "cg_id") String cg_id,
			@RequestParam(required=false , value = "ct_id") String ct_id) {
		
		if(null != ct_id ){
			long encryp_ctId=(long)EncryptUtil.cwnDecrypt(ct_id);
			model.addAttribute("cpd", cpdManagementService.get(encryp_ctId));
		}
		CpdGroup cpdGroup = new CpdGroup();
		if (null != cg_id) {
			long encryp_cgId=(long)EncryptUtil.cwnDecrypt(cg_id);
			model.addAttribute("type", "update");
			cpdGroup = CpdGroupService.get(encryp_cgId);
			if(null != cpdGroup.getCg_ct_id()){
				CpdType cpdType = cpdManagementService.get(cpdGroup.getCg_ct_id());
				model.addAttribute("cpd",cpdType);
			}
		} else {
			model.addAttribute("type", "add");
		}
		model.addAttribute("cpdGroup",cpdGroup);
		return "admin/cpd/cpd_group_add";
	}
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String save(Model model, loginProfile prof,CpdGroup cpdGroup) throws Exception {
		
		if (cpdGroup != null && cpdGroup.getCg_id() != null) {
			model.addAttribute("type", "update");
		} else {
			model.addAttribute("type", "add");
		}
	    // 该牌照类别已经存在  ct_license_type
		if (CpdGroupService.isExistForType(cpdGroup)) {
			model.addAttribute("success",false);
		}else{
			CpdGroupService.saveOrUpdate(cpdGroup, prof);
		    model.addAttribute("success",true);
		}
		
		return JSON.toJSONString(model);
	}

	@RequestMapping("listJson")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String listJson(loginProfile prof, Model model, Page<CpdGroup> page, long cg_ct_id) {
		CpdGroupService.searchAll(page,cg_ct_id);
		return JsonFormat.format(model, page);
	}

	@RequestMapping("detail/{ct_id}")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	public String detail(Model model, @PathVariable(value = "ct_id") long ct_id) {
		model.addAttribute("cpd", cpdManagementService.get(ct_id));
		model.addAttribute("type", "detail");
		return "admin/cpd/cpd_type_detail";
	}
	
	@RequestMapping(value = "detele")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String detele(Model model, loginProfile prof,CpdGroup cpdGroup) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
	    // 该牌照类别已有学员注册   不能删除
		if(CpdGroupService.getCountByCgID(cpdGroup.getCg_id())) { 
			map.put("type", "1");
			map.put("success",false);
		}else if(CpdGroupService.getCountItemByCgID(cpdGroup.getCg_id())) { 
			// 该牌照类别已有课程关联  不能删除
			map.put("type", "2");
			map.put("success",false);
		}else{
			//删除成功
			CpdGroupService.delete(cpdGroup,prof);
			map.put("success",true);
		}
		
		return JSON.toJSONString(map);
	}
	
	@RequestMapping(value = "infoSort")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String infoSort(Model model, loginProfile prof,String cg_display_order,long cg_ct_id) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
	    
		CpdGroupService.infoSort(cg_display_order,cg_ct_id,prof);
		
		return JSON.toJSONString(map);
	}
	
	
	@RequestMapping(value = "groupPeriod")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	public String groupPeriod(Model model, loginProfile prof, @RequestParam(required=true , value = "cg_id") String cg_id ) throws Exception {
	    
		long encryp_cgId=(long)EncryptUtil.cwnDecrypt(cg_id);
		
		CpdGroup cpdGroup = CpdGroupService.get(encryp_cgId);
		model.addAttribute("cpdGroup", cpdGroup);
		return "admin/cpd/cpd_group_period";
		
	}
	
	
}
