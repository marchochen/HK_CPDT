package com.cwn.wizbank.controller.admin;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.UserPosition;
import com.cwn.wizbank.entity.UserPositionCatalog;
import com.cwn.wizbank.entity.UserPositionLrnItem;
import com.cwn.wizbank.entity.UserPositionLrnMap;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.UserPositionCatalogService;
import com.cwn.wizbank.services.UserPositionLrnItemService;
import com.cwn.wizbank.services.UserPositionLrnMapService;
import com.cwn.wizbank.services.UserPositionService;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * 岗位学习地图的controller
 *   
 * @author halo.pan
 */
@Controller("adminPoistionMapController")
@RequestMapping("admin/positionMap")
@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
public class PoistionMapController {
	@Autowired
	private UserPositionService userPositionService;
	@Autowired
	private UserPositionLrnItemService userPositionLrnItemService;
	@Autowired
	private UserPositionCatalogService userPositionCatalogService;
	@Autowired
	private UserPositionLrnMapService userPositionLrnMapService;
	/**
	 * 转发到列表首页
	 * 
	 * @return
	 */
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping("")
	public String page() {
		return "admin/positionMap/index";
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
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping("pageJson")
	@ResponseBody
	public String page(Page<UserPositionLrnMap> page, Model model,
			@RequestParam(value = "upm_id", required = false, defaultValue = "0") long upm_id,
			@RequestParam(value = "upt_id", required = false, defaultValue = "0") long upt_id,
			@RequestParam(value = "upc_id", required = false, defaultValue = "0") long upc_id,
			loginProfile profile,WizbiniLoader wizbini) throws ParseException {
	    if(wizbini.cfgSysSetupadv.isTcIndependent()){
            page.getParams().put("top_tcr_id", profile.my_top_tc_id);
            }
		userPositionLrnMapService.getPositionMapList(page, upm_id, upt_id, 
				upc_id);
		return JsonFormat.format(model, page);
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping("add")
	public String create(Model model) {
		model.addAttribute("type", "doadd");
		return "admin/positionMap/add";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping("catalog")
	public String catalog(Model model,WizbiniLoader wizbini,loginProfile prof) {
		List<UserPositionCatalog> catalogs = userPositionCatalogService.getList(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		model.addAttribute("catalogs", catalogs);
		return "admin/positionMap/catalog";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "doadd")
	public String add(WizbiniLoader wizbini, loginProfile prof, UserPositionLrnMap userPositionLrnMap,
			@RequestParam(value = "image", required = false) MultipartFile image,
			@RequestParam(value = "qid", required = false) String qid,
			@RequestParam(value = "imgurl", required = false) String imgurl,
			@RequestParam(value = "image_radio", required = false) int image_radio) throws Exception {

		userPositionService.addUserPositionMapAndItem(wizbini, prof, userPositionLrnMap, image, qid, imgurl, image_radio);
		return "redirect:/app/admin/positionMap";

	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "updatePage")
	public String updatePage(UserPosition userPosition, ModelMap map) {
		map.addAttribute("type", "update");
		userPosition = userPositionService.getByMapId(userPosition.getUpm_id());
		if (userPosition != null) {
			List<UserPositionLrnItem> items = userPositionLrnItemService.getItemByMapList(userPosition.getUpm_id());
			userPosition.setItems(items);
			map.addAttribute("itemSize", items.size());
		}
		map.addAttribute("position", userPosition);
		ImageUtil.combineImagePath(userPosition);
		return "admin/positionMap/add";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "update")
	public String update(WizbiniLoader wizbini, loginProfile prof, UserPositionLrnMap userPositionLrnMap,
			@RequestParam(value = "image", required = false) MultipartFile image,
			@RequestParam(value = "qid", required = false) String qid,
			@RequestParam(value = "imgurl", required = false) String imgurl,
			@RequestParam(value = "image_radio", required = false) int image_radio) throws Exception {
		userPositionService.updateUserPositionMapAndItem(wizbini, prof, userPositionLrnMap, image, qid, imgurl, image_radio);
		return "redirect:/app/admin/positionMap";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "publishAndCancel")
	public String publishAndCancel(UserPosition userPosition, loginProfile profile) {
		userPositionLrnMapService.publishAndCancel(userPosition.getUpm_id(), userPosition.getUpm_status());
		return "redirect:/app/admin/positionMap";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping(method = RequestMethod.GET, value = "delete")
	public String delete(UserPosition userPosition) {
		userPositionService.deleteUserPositionMapAndItem(userPosition.getUpm_id());
		return "redirect:/app/admin/positionMap";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "batDelete")
	public String batDelete(@RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		userPositionService.batDeleteUserPositionMapAndItem(ids);
		return "redirect:/app/admin/positionMap";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping("checkExistPosition")
	@ResponseBody
	public String checkExistPosition(Model model,WizbiniLoader wizbini,loginProfile prof,
			@RequestParam(value = "upt_id", required = false, defaultValue = "") String upt_id) {
		int i=userPositionLrnMapService.getCountById(upt_id,null,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		if(i>0){
			model.addAttribute("stauts", 1);
		}else{
			model.addAttribute("stauts", 0);
		}
		return JsonFormat.format(model);
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "batPublish")
	public String batPublished(@RequestParam(value = "ids", required = false, defaultValue = "") String ids, loginProfile profile) {
		userPositionLrnMapService.batPublishAndCancel(ids,1);
		return "redirect:/app/admin/positionMap";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAP_MAIN)
	@RequestMapping(method = RequestMethod.POST, value = "batCancelPublish")
	public String batCancelPublish(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,loginProfile profile) {
		userPositionLrnMapService.batPublishAndCancel(ids, 0);
		return "redirect:/app/admin/positionMap";
	}
	
}
