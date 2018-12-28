/**
 * 
 */
package com.cwn.wizbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.JsonMod.eip.dao.EnterpriseInfoPortalDao;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.SitePoster;
import com.cwn.wizbank.services.EnterpriseInfoPortalService;
import com.cwn.wizbank.services.SitePosterService;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Params;

/**
 * @author leon.li
 *
 */
@Controller
@RequestMapping("poster")
public class PosterController {
	
	@Autowired
	SitePosterService sitePosterService;
	
	@Autowired
	EnterpriseInfoPortalService eipService; 
	
	@RequestMapping("Json")
	@ResponseBody
	public String poster(Model model, loginProfile prof, WizbiniLoader wizbini,
			@RequestParam(value="isMobile", required=false, defaultValue="0") int isMobile,
			Params params){
		
		long tcrId = prof.root_ent_id;
		//如果是ln模式，并且用户所在二级培训中心已经跟企业关联
		if (wizbini.cfgSysSetupadv.isTcIndependent() && eipService.checkTcrOccupancy(prof.my_top_tc_id)) {
			tcrId = prof.my_top_tc_id;
		}
		List<SitePoster> list = sitePosterService.getPoster(tcrId, isMobile);	
		if (list == null || list.size() == 0) {
			list = sitePosterService.getPoster(prof.root_ent_id, isMobile);
		}
		if(list != null && list.size() > 0){
			for(SitePoster pst : list){
				ImageUtil.combineImagePath(pst);
			}
		}
		model.addAttribute("poster", list);
		return JsonFormat.format(params, model);
	}
	
	@RequestMapping("logoJson")
	@ResponseBody
	public String logo(Model model, loginProfile prof, WizbiniLoader wizbini, Params params){
		
		long tcrId = prof.root_ent_id;
		//如果是ln模式，并且用户所在二级培训中心已经跟企业关联
		if (wizbini.cfgSysSetupadv.isTcIndependent() && eipService.checkTcrOccupancy(prof.my_top_tc_id)) {
			tcrId = prof.my_top_tc_id;
		}
		List<SitePoster> list = sitePosterService.getLogo(tcrId, 0);		
		if (list == null || list.size() == 0) {
			list = sitePosterService.getLogo(prof.root_ent_id, 0);
		}
		if(list != null && list.size() > 0){
			for(SitePoster pst : list){
				ImageUtil.combineImagePath(pst);
			}
		}
		model.addAttribute("logo", list);
		return JsonFormat.format(params, model);
	}
	@RequestMapping("welText")
	@ResponseBody
	public String welcomeText(Model model, loginProfile prof, WizbiniLoader wizbini,
			@RequestParam(value="isMobile", required=false, defaultValue="0") int isMobile,
			Params params){
		
		long tcrId = prof.root_ent_id;
		//如果是ln模式，并且用户所在二级培训中心已经跟企业关联
		if (wizbini.cfgSysSetupadv.isTcIndependent() && eipService.checkTcrOccupancy(prof.my_top_tc_id)) {
			tcrId = prof.my_top_tc_id;
		}
		
		List<SitePoster> list = sitePosterService.getPoster(tcrId, isMobile);	
		if (list == null || list.size() == 0) {
			list = sitePosterService.getPoster(prof.root_ent_id, isMobile);
		}
		model.addAttribute("welcomeText", list.get(0).getSp_welcome_word());
		return JsonFormat.format(params, model);
	}
}
