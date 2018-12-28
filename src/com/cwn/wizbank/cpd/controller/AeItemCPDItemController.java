package com.cwn.wizbank.cpd.controller;


import java.util.ArrayList;
import java.util.HashMap;
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
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.entity.AeItemCPDGourpItem;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.utils.DateUtil;


/**
 * CPT/D牌照与课程 控制器
 * @author Nat
 *
 */
@RequestMapping("admin/aeItemCPDItem")
@Controller
public class AeItemCPDItemController {

	@Autowired
	AeItemCPDItemService aeItemCPDItemService;
	
	@Autowired
	CpdGroupService cpdGroupService;
	
	/**
	 * 课程 CPT/D获得时数
	 * @param prof
	 * @param model
	 * @param itm_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("listJson")
	@HasPermission(AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW)
	@ResponseBody
	public String listJson(loginProfile prof, Model model, long itm_id) throws Exception  {
		
		//获取到当前所有生效小牌牌照
		List<CpdGroup> list = cpdGroupService.getAllOrder(0);
		Map awardCPDGourpItemMap = aeItemCPDItemService.getCPDGourpItemMap(itm_id);
		List<Map> tableColumn = null;
		if(null!=list && list.size()>0){
			tableColumn = new ArrayList<Map>();
			for(CpdGroup cg : list){
				//如果该课程注册了小牌且需要时数不为0才显示
				if(awardCPDGourpItemMap.containsKey(cg.getCg_id())){
					AeItemCPDGourpItem acgi =  (AeItemCPDGourpItem)awardCPDGourpItemMap.get(cg.getCg_id());
					if(null!=acgi.getAcgi_award_core_hours() && acgi.getAcgi_award_core_hours()>0f){
						String coreColumn = cg.getCg_alias() + " Core Hours("+ acgi.getAcgi_award_core_hours() +")";
						Map<String,String> map =new HashMap<String,String>();
						map.put("columnText", coreColumn);
						map.put("columnKey", cg.getCg_id()+CpdUtils.CPD_CORE_HEADER_KEY);
						tableColumn.add(map);
					}
					if(cg.getCg_contain_non_core_ind()==1){
						if(null!=acgi.getAcgi_award_non_core_hours() && acgi.getAcgi_award_non_core_hours()>0f){
							String nonCoreColumn = cg.getCg_alias() + " Non-core Hours("+ acgi.getAcgi_award_non_core_hours() +")";
							//tableColumn.add(nonCoreColumn);
							Map<String,String> map =new HashMap<String,String>();
							map.put("columnText", nonCoreColumn);
							map.put("columnKey", cg.getCg_id()+CpdUtils.CPD_NON_CORE_HEADER_KEY);
							tableColumn.add(map);
						}
					}
				}

			}
		}
		model.addAttribute("tableColumn", tableColumn) ;
		return JSON.toJSONString(model);
	}	
	
	
	/**
	 * 保存/修改课程对应牌照的所需时数
	 * @param model
	 * @param prof
	 * @param aeItemCPDItem
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@HasPermission(AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW)
	@ResponseBody
	public String save(Model model, loginProfile prof,AeItemCPDItem aeItemCPDItem) throws Exception {
		
		if (aeItemCPDItem != null && aeItemCPDItem.getAci_id() != null) {
			model.addAttribute("type", "update");
		} else {
			model.addAttribute("type", "add");
		}
	  
		try {
			Map<String, Object> map = aeItemCPDItemService.saveOrUpdate(aeItemCPDItem, prof);
			model.addAttribute("map",map);
		    model.addAttribute("success",true);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("success",false);
		}
		
		
		return JSON.toJSONString(model);
	}
	
	/**
	 * 获取当前课程与对应牌照设置的所需时数
	 * @param prof
	 * @param model
	 * @param itm_id
	 * @param parent_itm_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getHoursListJson")
	@HasPermission(AclFunction.FTN_AMD_ITM_COS_MAIN_VIEW)
	@ResponseBody
	public String listJson(loginProfile prof, Model model, long itm_id, long parent_itm_id) throws Exception  {
		
		AeItemCPDItem aeItemCPDItem = aeItemCPDItemService.getByItmId(itm_id);
		List<CpdGroup> list = cpdGroupService.getAllOrder(0);
		model.addAttribute("cpdGroupList",list);
		model.addAttribute("aeItemCPDItem",aeItemCPDItem);
		String end_date = "";
		if(null != aeItemCPDItem && null != aeItemCPDItem.getAci_hours_end_date()){
		  end_date =DateUtil.getInstance().formateDate(aeItemCPDItem.getAci_hours_end_date());
		}
		model.addAttribute("end_date",end_date);
		return JSON.toJSONString(model);
	}
	
	
}
