/**
 * 
 */
package com.cwn.wizbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.vo.SearchResultVo;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.SearchService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

/**
 * @author leon.li
 *
 */
@Controller
@RequestMapping("search")
@HasPermission(value = {AclFunction.FTN_LRN_LEARNING_CATALOG,
		AclFunction.FTN_LRN_EXAM_CATALOG,
		AclFunction.FTN_LRN_LEARNING_OPEN,
		AclFunction.FTN_LRN_ANN_MGT,
		AclFunction.FTN_LRN_GROUP_MGT,
		AclFunction.FTN_LRN_KNOW_MGT,
		AclFunction.FTN_LRN_KB_VIEW,
		AclFunction.FTN_LRN_DOING_MGT})
public class SearchController {
	
	@Autowired
	SearchService searchService;
	
	@RequestMapping("toPage")
	public ModelAndView toPage(WizbiniLoader wizbini ,loginProfile prof,ModelAndView mav,
			@RequestParam(value="searchText", defaultValue="") String searchText){
		mav = new ModelAndView("home/search");
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		if(searchText != null && !"".equals(searchText)){
			searchText = searchText.trim();
		}
		searchText = cwUtils.esc4JS(searchText);
		mav.addObject("searchText", searchText);
		if(searchText != null && !"".equals(searchText)){
			searchText = "%" + searchText.toLowerCase() + "%";
		}
		mav.addObject("searchResult", searchService.searchResultMsg(searchText,  prof.usr_ent_id, tcrId));
		return mav;
	}
	
	/**
	 * 
	 * @param wizbini
	 * @param prof
	 * @param model
	 * @param page
	 * @param type
	 * @param showMobileOnly 该参数对课程类型才有效，表示是否只显示发布到移动端的课程
	 * @param searchValue
	 * @return
	 */
	@RequestMapping("Json")
	@ResponseBody
	public String searchJson(WizbiniLoader wizbini ,loginProfile prof, Model model,
			Page<SearchResultVo> page,
			@RequestParam(value="type", defaultValue="", required=false) String type,
			@RequestParam(value = "showMobileOnly", defaultValue = "false", required = false) boolean showMobileOnly,
			@RequestParam(value="searchValue", defaultValue="", required=false) String searchValue){
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		if(searchValue != null && !"".equals(searchValue)){
			searchValue = "%" + searchValue + "%";
		}
		searchService.search(page, type, searchValue, prof.usr_ent_id, tcrId,showMobileOnly);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 
	 * @param wizbini
	 * @param prof
	 * @param mav
	 * @param params
	 * @param searchText
	 * @param showMobileOnly 该参数对课程类型才有效，表示是否只显示发布到移动端的课程
	 * @param isMobile
	 * @return
	 */
	@RequestMapping("searchCount")
	@ResponseBody
	public String searchCount(WizbiniLoader wizbini, loginProfile prof, Model mav,
			Params params,
			@RequestParam(value = "searchText", defaultValue="") String searchText,
			@RequestParam(value = "showMobileOnly", defaultValue = "false", required = false) boolean showMobileOnly,
			@RequestParam(value = "isMobile", defaultValue = "0", required = false) long isMobile){
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		if(searchText != null && !"".equals(searchText)){
			searchText = "%" + searchText + "%";
		}
		mav.addAttribute(searchService.searchGroupResultMsg(searchText,  prof.usr_ent_id, tcrId, isMobile,showMobileOnly));
		return JsonFormat.format(params, mav);
	}

}
