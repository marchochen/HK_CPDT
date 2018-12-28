package com.cwn.wizbank.controller.admin;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.UserSpecialExpert;
import com.cwn.wizbank.entity.UserSpecialItem;
import com.cwn.wizbank.entity.UserSpecialTopic;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.UserSpecialExpertService;
import com.cwn.wizbank.services.UserSpecialItemService;
import com.cwn.wizbank.services.UserSpecialTopicService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.StringUtils;

/**
 * 专题培训controller
 *   
 * @author halo.pan
 */
@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
@Controller("adminSpecialTopicController")
@RequestMapping("admin/specialTopic")
public class SpecialTopicController {
	@Autowired
	private UserSpecialTopicService userSpecialTopicService;
	@Autowired
	private UserSpecialItemService userSpecialItemService;
	@Autowired
	private UserSpecialExpertService userSpecialExpertService;
	/**
	 * 转发到列表首页
	 * 
	 * @return
	 */
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping("")
	public String page(Model model) {
		return "admin/specialTopic/index";
	}

	/**
	 * 获取json列表
	 * 
	 * @param page
	 * @param prof
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping("pageJson")
	@ResponseBody
	public String page(WizbiniLoader wizbini, loginProfile prof,Page<UserSpecialTopic> page, Model model) throws ParseException {
	    if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", prof.my_top_tc_id);
            }
		userSpecialTopicService.getSpecialTopicList( page);
		return JsonFormat.format(model, page);
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping("create")
	public String create(Model model) {
		model.addAttribute("type", "doadd");
		return "admin/specialTopic/add";
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping("doadd")
	public String add(WizbiniLoader wizbini,UserSpecialTopic specialTopic, loginProfile profile,
			@RequestParam(value = "image", required = false) MultipartFile image,
			@RequestParam(value = "qid", required = false) String qid,
			@RequestParam(value = "expert_ids", required = false) String expert_ids,
			@RequestParam(value = "imgurl", required = false) String imgurl,
			@RequestParam(value = "image_radio", required = false) int image_radio) {
		userSpecialTopicService.addSpecialTopic(wizbini, profile, specialTopic, image, qid, expert_ids, imgurl, image_radio);
		return "redirect:/app/admin/specialTopic";
	}

	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping("updatePage")
	public String updatePage(WizbiniLoader wizbini,loginProfile profile,Model model, UserSpecialTopic specialTopic) {
		if(null!=specialTopic && !StringUtils.isEmpty(specialTopic.getEncrypt_ust_id())){
			Long ust_id = EncryptUtil.cwnDecrypt(specialTopic.getEncrypt_ust_id());
			specialTopic.setUst_id(ust_id);
		}
		specialTopic=userSpecialTopicService.get(specialTopic.getUst_id());
		if (specialTopic != null) {
			List<UserSpecialItem> items = userSpecialItemService.getItemByUstId(specialTopic.getUst_id());
			List<String> itemStrs=new ArrayList<String>();
			for (UserSpecialItem userSpecialItem : items) {
				itemStrs.add(String.valueOf(userSpecialItem.getUsi_itm_id()));
			}
			String itemStr=StringUtils.listToString(itemStrs,"~");
			model.addAttribute("itemStr", itemStr);
			specialTopic.setItems(items);
			model.addAttribute("itemSize", items.size());
	      
			List<UserSpecialExpert> experts=userSpecialExpertService.getExpertsByUstId(wizbini,specialTopic.getUst_id(),profile.my_top_tc_id);
			List<String> expertStrs=new ArrayList<String>();
		  	for (UserSpecialExpert userSpecialExpert : experts) {
		  		expertStrs.add(String.valueOf(userSpecialExpert.getUse_ust_id()));
		  	}
		  	String expertStr=StringUtils.listToString(expertStrs,",");
		  	model.addAttribute("expertStr", expertStr);
		  	specialTopic.setExperts(experts);
		  	model.addAttribute("expertSize", experts.size());
		}
		ImageUtil.combineImagePath(specialTopic);
		if(specialTopic.getUst_title()!=null&&specialTopic.getUst_title()!=""){
			specialTopic.setUst_title(specialTopic.getUst_title().trim());
		}
		if(specialTopic.getUst_summary()!=null&&specialTopic.getUst_summary()!=""){
			specialTopic.setUst_summary(specialTopic.getUst_summary().trim());
		}
		model.addAttribute("specialTopic", specialTopic);
		model.addAttribute("type", "update");
		return "admin/specialTopic/add";
	}

	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping("update")
	public String update(WizbiniLoader wizbini,UserSpecialTopic specialTopic, loginProfile profile,
			@RequestParam(value = "image", required = false) MultipartFile image,
			@RequestParam(value = "qid", required = false) String qid,
			@RequestParam(value = "expert_ids", required = false) String expert_ids,
			@RequestParam(value = "imgurl", required = false) String imgurl,
			@RequestParam(value = "image_radio", required = false) int image_radio) {
		userSpecialTopicService.updateSpecialTopic(wizbini, profile, specialTopic, image, qid, expert_ids, imgurl, image_radio);
		return "redirect:/app/admin/specialTopic";
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping("expertsPage")
	public String expertPage(Model model,loginProfile prof){
		return "admin/pop/expertsPage";
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "publish")
	public String published(UserSpecialTopic specialTopic, loginProfile profile) {
		userSpecialTopicService.publishAndCancel(specialTopic.getUst_id(),1);
		return "redirect:/app/admin/specialTopic";
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "cancelPublish")
	public String cancelPublish(UserSpecialTopic specialTopic, loginProfile profile) {
		userSpecialTopicService.publishAndCancel(specialTopic.getUst_id(),0);
		return "redirect:/app/admin/specialTopic";
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "delete")
	public String delete(UserSpecialTopic specialTopic) {
		if(null!=specialTopic && !StringUtils.isEmpty(specialTopic.getEncrypt_ust_id())){
			Long ust_id = EncryptUtil.cwnDecrypt(specialTopic.getEncrypt_ust_id());
			specialTopic.setUst_id(ust_id);
		}
		userSpecialTopicService.deleteSpecialTopic(specialTopic.getUst_id());
		return "redirect:/app/admin/specialTopic";
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping("viewResult")
	public String viewResult(Model model,UserSpecialTopic specialTopic) {
		model.addAttribute("ust_id", specialTopic.getUst_id());
		return "admin/specialTopic/viewResult";
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "batDelete")
	public String batDelete(@RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		userSpecialTopicService.batDeleteSpecialTopic(ids);
		return "redirect:/app/admin/specialTopic";
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "batPublish")
	public String batPublished(@RequestParam(value = "ids", required = false, defaultValue = "") String ids, loginProfile profile) {
		userSpecialTopicService.batPublishAndCancel(ids,1);
		return "redirect:/app/admin/specialTopic";
	}
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "batCancelPublish")
	public String batCancelPublish(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,loginProfile profile) {
		userSpecialTopicService.batPublishAndCancel(ids, 0);
		return "redirect:/app/admin/specialTopic";
	}
	
	@HasPermission(AclFunction.FTN_AMD_SPECIALTOPIC_MAIN)
	@RequestMapping("checkExistTitle")
	@ResponseBody
	public String checkExistTitle(Model model,WizbiniLoader wizbini, loginProfile prof,
			@RequestParam(value = "ust_title", required = false, defaultValue = "") String ust_title,@RequestParam(value = "old_value", required = false, defaultValue = "") String old_value) {
		if(userSpecialTopicService.isExistTitle(ust_title, old_value,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id  )){
			model.addAttribute("stauts", 1);
		}else{
			model.addAttribute("stauts", 0);
		}
		return JsonFormat.format(model);
	}
	
}
