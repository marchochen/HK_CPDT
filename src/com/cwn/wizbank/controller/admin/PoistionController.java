package com.cwn.wizbank.controller.admin;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.UserPosition;
import com.cwn.wizbank.entity.UserPositionCatalog;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.UserPositionCatalogService;
import com.cwn.wizbank.services.UserPositionLrnMapService;
import com.cwn.wizbank.services.UserPositionService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.RequestStatus;
import com.cwn.wizbank.utils.StringUtils;

/**
 * 岗位controller
 *  
 * @author halo.pan
 */
@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
@Controller("adminPoistionController")
@RequestMapping("admin/position")
public class PoistionController {
	@Autowired
	private UserPositionService userPositionService;
	@Autowired
	private UserPositionCatalogService userPositionCatalogService;
	@Autowired
	private RegUserService userService;
	@Autowired
	private UserPositionLrnMapService userPositionLrnMapService;
	/**
	 * 转发到列表首页
	 * 
	 * @return
	 */
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("")
	public String page(Model model,loginProfile prof,WizbiniLoader wizbini,@RequestParam(value="active", required=false, defaultValue="")String active) {
		List<UserPositionCatalog> catalogs = userPositionCatalogService.getList(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		model.addAttribute("active", active);
		model.addAttribute("catalogs", catalogs);
		return "admin/position/index";
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
	@HasPermission(value={AclFunction.FTN_AMD_POSITION_MAIN,AclFunction.FTN_AMD_POSITION_MAP_MAIN})
	@RequestMapping("pageJson")
	@ResponseBody
	public String page(Page<UserPosition> page, Model model,
			@RequestParam(value = "upt_id", required = false, defaultValue = "0") long upt_id,
			@RequestParam(value = "upc_id", required = false, defaultValue = "0") long upc_id,
			loginProfile profile,WizbiniLoader wizbini) throws ParseException {
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			page.getParams().put("top_tcr_id", profile.my_top_tc_id);
			}
		userPositionService.getPositionList(page, upt_id, upc_id);
		return JsonFormat.format(model, page);
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
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("mapPageJson")
	@ResponseBody
	public String mapPageJson(WizbiniLoader wizbini, loginProfile prof,Page<UserPosition> page, Model model,
			@RequestParam(value = "upt_id", required = false, defaultValue = "0") long upt_id,
			@RequestParam(value = "upc_id", required = false, defaultValue = "0") long upc_id) throws ParseException {
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			page.getParams().put("top_tcr_id", prof.my_top_tc_id);
			}
		userPositionService.getPositionMapPage(page, upt_id, upc_id);
		return JsonFormat.format(model, page);
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
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("catalogPageJson")
	@ResponseBody
	public String catalogPage(Page<UserPositionCatalog> page, Model model,
			@RequestParam(value = "upc_id", required = false, defaultValue = "0") long upc_id,
			loginProfile prof,WizbiniLoader wizbini
			) throws ParseException {
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			page.getParams().put("top_tcr_id", prof.my_top_tc_id);
			}
		userPositionCatalogService.getPageList(page, upc_id);
		return JsonFormat.format(model, page);
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("create")
	public String create(Model model,loginProfile prof,WizbiniLoader wizbini) {
		List<UserPositionCatalog> catalogs = userPositionCatalogService.getList(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("type", "add");
		return "admin/position/add";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("createCatalog")
	public String createCatalog(Model model) {
		model.addAttribute("type", "addCatalog");
		return "admin/position/addCatalog";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("add")
	public String add(UserPosition userPosition, loginProfile profile) {
		userPosition.setUpt_tcr_id(profile.my_top_tc_id);
			userPositionService.add(userPosition);
			ObjectActionLog log = new ObjectActionLog(userPosition.getUpt_id(), 
					userPosition.getUpt_code(),
					userPosition.getUpt_title(),
					ObjectActionLog.OBJECT_TYPE_UPT,
					ObjectActionLog.OBJECT_ACTION_ADD,
					ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
					profile.getUsr_ent_id(),
					profile.getUsr_last_login_date(),
					profile.getIp()
			);
			SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		return "redirect:/app/admin/position";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("addCatalog")
	public String addCatalog(UserPositionCatalog userPositionCatalog, loginProfile profile) {
		userPositionCatalog.setUpc_tcr_id(profile.my_top_tc_id);
		userPositionCatalog.setUpc_create_user_id(profile.usr_ent_id);
		userPositionCatalog.setUpc_update_user_id(profile.usr_ent_id);
		userPositionCatalogService.add(userPositionCatalog);
		return "redirect:/app/admin/position?active=c";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("updatePage")
	public String updatePage(Model model, UserPosition userPosition,loginProfile prof,WizbiniLoader wizbini) {
		List<UserPositionCatalog> catalogs = userPositionCatalogService.getList(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		userPosition = userPositionService.get(userPosition.getUpt_id());
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("position", userPosition);
		model.addAttribute("type", "update");
		return "admin/position/add";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("updatePageCatalog")
	public String updatePageCatalog(Model model, UserPositionCatalog userPositionCatalog) {
		userPositionCatalog = userPositionCatalogService.get(userPositionCatalog.getUpc_id());
		model.addAttribute("type", "update");
		model.addAttribute("positionCatalog", userPositionCatalog);
		return "admin/position/addCatalog";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("update")
	public String update(Model model, UserPosition userPosition, loginProfile prof) {
		userPositionService.update(userPosition, prof.usr_ent_id);
		ObjectActionLog log = new ObjectActionLog(userPosition.getUpt_id(), 
				userPosition.getUpt_code(),
				userPosition.getUpt_title(),
				ObjectActionLog.OBJECT_TYPE_UPT,
				ObjectActionLog.OBJECT_ACTION_UPD,
				ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
				prof.getUsr_ent_id(),
				prof.getUsr_last_login_date(),
				prof.getIp()
		);
		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		return "redirect:/app/admin/position";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("updateCatalog")
	public String updateCatalog(Model model, UserPositionCatalog userPositionCatalog, loginProfile prof) {
		UserPositionCatalog userPositionCata = userPositionCatalogService.get(userPositionCatalog.getUpc_id());
		userPositionCata.setUpc_title(userPositionCatalog.getUpc_title());
		userPositionCatalogService.updateCatalog(userPositionCata, prof.usr_ent_id);
		return "redirect:/app/admin/position?active=c";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("batchdel")
	@ResponseBody
	public String batchDel(Model model, WizbiniLoader wizbini,loginProfile prof,
			@RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		
			
			boolean flag=false;
			String[] uptIds=ids.split(",");
			for (String uptId : uptIds) {
			int cnt=userPositionLrnMapService.getCountById(uptId,null,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
			if(cnt>0){
				flag=true;
			}
			}
			if(flag){
				//存在关键岗位
				model.addAttribute(RequestStatus.STATUS, 2);
			}else{
					model.addAttribute("ids", ids);
					model.addAttribute(RequestStatus.STATUS, 1);
			}
		return JsonFormat.format(model);
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("batchdelCatalog")
	@ResponseBody
	public String batchDelCatalog(Model model, loginProfile prof,
			@RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		int err = 0;
		String[] arr = ids.split(",");
		for (String upt_upc_id : arr) {
			int cnt = userPositionService.getCountById(upt_upc_id);
			if (cnt > 0) {
				err++;
			}
		}
		if (err == 0) {

			int cns = userPositionCatalogService.batchdelete(ids);
			if (cns > 0) {
				model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
			} else {
				model.addAttribute(RequestStatus.STATUS, RequestStatus.ERROR);
			}
		} else {
			model.addAttribute(RequestStatus.ERROR, 94);
		}
		return JsonFormat.format(model);
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("checkExistTitle")
	@ResponseBody
	public String checkExistTitle(Model model, WizbiniLoader wizbini, loginProfile prof,
			@RequestParam(value = "upt_title", required = false, defaultValue = "") String upt_title,@RequestParam(value = "old_id", required = false, defaultValue = "0") long old_id) {
		if(userPositionService.isExistTitle(upt_title, old_id,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id)){
			model.addAttribute("stauts", 1);
		}else{
			model.addAttribute("stauts", 0);
		}
		return JsonFormat.format(model);
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("checkExistCode")
	@ResponseBody
	public String checkExistCode(Model model,WizbiniLoader wizbini, loginProfile prof,
			@RequestParam(value = "upt_code", required = false, defaultValue = "") String upt_code,@RequestParam(value = "old_id", required = false, defaultValue = "0") long old_id) {
		if(userPositionService.isExistCode(upt_code, old_id,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id)){
			model.addAttribute("stauts", 1);
		}else{
			model.addAttribute("stauts", 0);
		}
		return JsonFormat.format(model);
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("checkCatalogExistTitle")
	@ResponseBody
	public String checkCatalogExistTitle(Model model,WizbiniLoader wizbini, loginProfile prof,
			@RequestParam(value = "upc_title", required = false, defaultValue = "") String upc_title,@RequestParam(value = "old_id", required = false, defaultValue = "0")long old_id) {
		if(userPositionCatalogService.isExistTitle(upc_title, old_id,wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id)){
			model.addAttribute("stauts", 1);
		}else{
			model.addAttribute("stauts", 0);
		}
		return JsonFormat.format(model);
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("getEffectuser")
	public String getEffectuser(Model model, loginProfile prof,@RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		List<RegUser> list=userService.getDelUptAffectUsr(ids);
		
		List<String> positionTitles=new ArrayList<String>();
		
		String[] uptIds=ids.split(",");
		
		for (String uptId : uptIds) {
			UserPosition position=userPositionService.get(Long.valueOf(uptId));
			positionTitles.add(position.getUpt_title());
		}
		
		String positionTitle=StringUtils.listToString(positionTitles, ",");
		model.addAttribute("list", list);
		model.addAttribute("positionTitle", positionTitle);
		model.addAttribute("ids", ids);
		return "admin/position/effectuser";
	}
	@HasPermission(AclFunction.FTN_AMD_POSITION_MAIN)
	@RequestMapping("delUptaffectuser")
	public String updateus(Model model, loginProfile prof,@RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
		
		String[] idList = ids.split(",");
		List<UserPosition> logList = new ArrayList<UserPosition>();
		for(String val : idList){
			UserPosition userPosition = userPositionService.get(Long.valueOf(val));
			logList.add(userPosition);
		}
		userPositionService.batchDeleteAndRelation(ids);
		for(UserPosition up : logList){
			ObjectActionLog log = new ObjectActionLog(up.getUpt_id(), 
					up.getUpt_code(),
					up.getUpt_title(),
					ObjectActionLog.OBJECT_TYPE_UPT,
					ObjectActionLog.OBJECT_ACTION_DEL,
					ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
					prof.getUsr_ent_id(),
					prof.getUsr_last_login_date(),
					prof.getIp()
			);
			SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		}
		return "redirect:/app/admin/position";
	}
	}
