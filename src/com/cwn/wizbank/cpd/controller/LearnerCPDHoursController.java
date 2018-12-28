package com.cwn.wizbank.cpd.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.AeItemCPDItemService;
import com.cwn.wizbank.cpd.service.CpdGroupService;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.utils.DateUtil;


/**
 * 学员端CPT/D时数
 * @author Nat
 *
 */
@RequestMapping("admin/learnerCPDHours")
@Controller
public class LearnerCPDHoursController {

	@Autowired
	AeItemCPDItemService aeItemCPDItemService;
	@Autowired
	CpdGroupService cpdGroupService;
	
	
	@RequestMapping("listJson")
	@ResponseBody
	public String listJson(loginProfile prof, Model model, long itm_id, long parent_itm_id) throws Exception  {
		
		AeItemCPDItem aeItemCPDItem = aeItemCPDItemService.getByItmId(itm_id);
		//当未找到该牌照设置时数，判断是否有父级id   使用父级设置的cpd时数
		/*if(null == aeItemCPDItem.getAci_id()){
			if(0 != parent_itm_id){
				aeItemCPDItem =  aeItemCPDItemService.getByItmId(parent_itm_id);
			}
		}*/
		//获取到当前所有生效小牌牌照
		List<CpdGroup> list = cpdGroupService.getAllOrder(0);
		String end_date = "";
		
		if(null != aeItemCPDItem && null != aeItemCPDItem.getAci_hours_end_date()){
	          end_date =DateUtil.getInstance().formateDate(aeItemCPDItem.getAci_hours_end_date());
	        }
	        model.addAttribute("end_date",end_date);
	        
		model.addAttribute("cpdGroupList",list);
   
		model.addAttribute("aeItemCPDItem",aeItemCPDItem);
		
		return JSON.toJSONString(model);
	}
	
	
}
