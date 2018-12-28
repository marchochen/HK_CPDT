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
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;


/**
 * cpd牌照管理控制器
 * @author Nat
 *
 */
@RequestMapping("admin/cpdManagement")
@Controller
public class CpdManagementController {

	@Autowired
	CpdManagementService cpdManagementService;
	
	@Autowired
	CpdGroupService	cpdGroupService;
	
	@RequestMapping("index")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	public String index(loginProfile prof) throws MessageException {
		
		
		return "admin/cpd/cpd_mgt_admin";
	}
	
	
	@RequestMapping(method = RequestMethod.GET, value = "insert")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	public String insert(Model model,  @RequestParam(required=false , value = "ct_id") String ct_id) {
		
		CpdType cpdType = new CpdType();
 		
		if (ct_id != null ) {
			long encryp_ctId=(long)EncryptUtil.cwnDecrypt(ct_id);
			model.addAttribute("type", "update");
			cpdType = cpdManagementService.get(encryp_ctId);
		} else {
			model.addAttribute("type", "add");
		}
		model.addAttribute(cpdType);
		return "admin/cpd/cpd_type_add";
	}
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String save(Model model, loginProfile prof,CpdType cpdType) throws Exception {
		
		if (cpdType != null && cpdType.getCt_id() != null) {
			model.addAttribute("type", "update");
			//修改 成功之后跳转到详情页面
			model.addAttribute("ct_id", cpdType.getCt_id());
		} else {
			model.addAttribute("type", "add");
			model.addAttribute("ct_id", 0);
		}
	    // 该牌照类别已经存在  ct_license_type
		if (cpdManagementService.isExistForType(cpdType)) {
			model.addAttribute("success",false);
		}else{
			cpdManagementService.saveOrUpdate(cpdType, prof);
		    model.addAttribute("success",true);
		}
		
		return JSON.toJSONString(model);
	}

	@RequestMapping("listJson")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String listJson(loginProfile prof, Model model, Page<CpdType> page) {
		cpdManagementService.searchAll(page);
		return JsonFormat.format(model, page);
	}

	@RequestMapping("detail/{ct_id}")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	public String detail(Model model, @PathVariable(value = "ct_id") String ct_id) {
		
		long encryp_ctId=(long)EncryptUtil.cwnDecrypt(ct_id);
		
		model.addAttribute("cpd", cpdManagementService.get(encryp_ctId));
		long cg_count = cpdGroupService.getCountGroupByCtId(encryp_ctId);
		model.addAttribute("cg_count", cg_count);
		model.addAttribute("type", "detail");
		return "admin/cpd/cpd_type_detail";
	}
	
	@RequestMapping(value = "detele")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String detele(Model model, loginProfile prof,CpdType cpdType) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
	    // 该牌照类别已有学员注册   不能删除
		if(cpdManagementService.getCountByCtID(cpdType.getCt_id())) {  
			map.put("type", "1");
			map.put("success",false);
		}else if(cpdManagementService.getCountItemByCtID(cpdType.getCt_id())) { 
			// 该牌照类别已有课程关联  不能删除
			map.put("type", "2");
			map.put("success",false);
		}else{
			//删除成功
			cpdManagementService.delete(cpdType,prof);
			map.put("success",true);
		}
		
		return JSON.toJSONString(map);
	}
	
	@RequestMapping(value = "infoSort")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String infoSort(Model model, loginProfile prof,String ct_type_Sort) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
	    
		cpdManagementService.infoSort(ct_type_Sort,prof);
		
		return JSON.toJSONString(map);
	}
	
}
