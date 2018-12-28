package com.cwn.wizbank.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AeTreeNode;
import com.cwn.wizbank.entity.vo.AeTreeNodeVo;
import com.cwn.wizbank.services.AeTreeNodeService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;

/**
 * 目录
 * 
 * @author leon.li 2014-8-20 上午10:39:44
 */
@Controller
@RequestMapping("catalog")
public class CatalogController {

	@Autowired
	AeTreeNodeService aeTreeNodeService;

	@RequestMapping("treeJson")
	@ResponseBody
	public String treeJson(
			loginProfile prof,
			WizbiniLoader wizbini,
			@RequestParam(value = "tcr_id", required = false, defaultValue = "0") long tcrId,
			@RequestParam(value = "cos_type", required = false) String cos_type ,
			Params param,
			Model model) {
		long userTopTcrId = 1;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			userTopTcrId = prof.my_top_tc_id;
		}
		List<AeTreeNodeVo> list = aeTreeNodeService.getTraingCenterCatalog(userTopTcrId, tcrId, prof.usr_ent_id, cos_type, wizbini);
		model.addAttribute(list);
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping("adminTreeJson")
	@ResponseBody
	public List<AeTreeNodeVo> adminTreeJson(
			loginProfile prof,
			WizbiniLoader wizbini,
			@RequestParam(value = "tcrId", required = false, defaultValue = "0") long tcrId,
			@RequestParam(value = "cosType", required = false, defaultValue = "course") String cosType,
			Params param,
			Model model) {
		
		return  aeTreeNodeService.getAdminTraingCenterCatalog(tcrId, prof.usr_ent_id, cosType, prof.current_role);
	}
	
	@RequestMapping(value = "getTreeInfo/{tcrId}", method = RequestMethod.GET)
	@ResponseBody
	public String getTreeInfo(
			loginProfile prof,
			WizbiniLoader wizbini,
			@PathVariable(value = "tcrId") long tcrId,
			Model model) {
		model.addAttribute("tcrId", tcrId);
		return JsonFormat.format(model);
	}
	
	@RequestMapping("openJson")
	@ResponseBody
	public String openJson(
			WizbiniLoader wizbini,
			loginProfile prof,
			@RequestParam(value = "tcrId", required = false, defaultValue = "0") long tcrId,
			@RequestParam(value = "tndId", required = false, defaultValue = "0") long tndId,
			@RequestParam(value = "cos_type", required = false) String cos_type ,
			Params param,
			Model model) {
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		Map<String,Object> map = aeTreeNodeService.getOpenCatalog(tcrId, tndId, prof.usr_ent_id,  cos_type);
		model.addAttribute("catalog", map);
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping("mobileJson")
	@ResponseBody
	public String mobileJson(
			WizbiniLoader wizbini,
			loginProfile prof,
			@RequestParam(value = "tcrId", required = false, defaultValue = "0") long tcrId,
			@RequestParam(value = "tndId", required = false, defaultValue = "0") long tndId,
			@RequestParam(value = "cos_type", required = false) String cos_type ,
			Params param,
			Model model) {
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		Map<String,Object> map = aeTreeNodeService.getMobileCatalog(tcrId, tndId, prof.usr_ent_id,  cos_type);
		model.addAttribute("catalog", map);
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping("getJson")
	@ResponseBody
	public String get(
			@RequestParam(value = "tndId", required = false, defaultValue = "0") long tndId,
			Params param,
			Model model) {
		model.addAttribute("catalog", aeTreeNodeService.get(tndId));
		return JsonFormat.format(param, model);
	}
	
	/**
	 * 获取指定目录下的子目录
	 * @param wizbini
	 * @param prof
	 * @param tcrId
	 * @param tndId 指定目录ID
	 * @param cos_type 课程类型
	 * @param param
	 * @param model
	 * @return
	 */
	@RequestMapping("getSubCtg")
	@ResponseBody
	public String getSubCtg(
			WizbiniLoader wizbini,
			loginProfile prof,
			@RequestParam(value = "tcrId", required = false, defaultValue = "0") long tcrId,
			@RequestParam(value = "tndId", required = false, defaultValue = "0") long tndId,
			@RequestParam(value = "cos_type", required = false) String cos_type ,
			Params param,
			Model model) {
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		AeTreeNode target = aeTreeNodeService.get(tndId);
		List<AeTreeNode> nodeList = aeTreeNodeService.getSubCatalog(tcrId, tndId, prof.usr_ent_id, cos_type);
		model.addAttribute("catalog", nodeList).addAttribute("target",target);
		return JsonFormat.format(param, model);
	}
	@RequestMapping("adminCourseTreeJson")
	@ResponseBody
	public List<AeTreeNodeVo> adminCourseTreeJson(
			loginProfile prof,
			WizbiniLoader wizbini,
			@RequestParam(value = "tcrId", required = false, defaultValue = "0") long tcrId,
			@RequestParam(value = "cosType", required = false, defaultValue = "course") String cosType,
			Params param,
			Model model) {
		return  aeTreeNodeService.getAdminTraingCenterCourse(tcrId, prof.usr_ent_id, cosType, prof.current_role);
	}
}
