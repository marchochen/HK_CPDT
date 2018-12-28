package com.cwn.wizbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.LearningSituationService;
import com.cwn.wizbank.services.UserCreditsService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
/**
 * 
 * @author lance 积分
 */
@Controller
@RequestMapping("rank")
public class RankController {
	@Autowired
	UserCreditsService userCreditsService;
	
	@Autowired
	AeItemService aeItemService;
	
	@Autowired
	LearningSituationService learningSituationService;
	/**
	 * 排行榜页面
	 * 
	 * @param command
	 *            ： credit_rank 积分排行榜
	 *            ： course_rank 课程排行榜
	 * @return
	 */
	@RequestMapping(value = "courseRank", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_RANK_COURSE)
	public ModelAndView toCourseRank(ModelAndView mav) {
		mav = new ModelAndView("rank/courseRank");
		return mav;
	}
	
	@RequestMapping(value = "learningRank", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_RANK_LEARNING)
	public ModelAndView toLearningRank(ModelAndView mav) {
		mav = new ModelAndView("rank/learningRank");
		return mav;
	}
	
	@RequestMapping(value = "creditRank", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_RANK_CREDIT)
	public ModelAndView toCreditRank(ModelAndView mav) {
		mav = new ModelAndView("rank/creditRank");
		return mav;
	}
	
	@RequestMapping(value = "courseRank/{sort}", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_RANK_COURSE)
	public ModelAndView toCourseRank(ModelAndView mav,
			@PathVariable(value = "sort") String sort) {
		mav.addObject("sort", sort);
		mav = new ModelAndView("rank/courseRank");
		return mav;
	}
	
	@RequestMapping(value = "learningRank/{sort}", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_RANK_LEARNING)
	public ModelAndView toLearningRank(ModelAndView mav,
			@PathVariable(value = "sort") String sort) {
		mav.addObject("sort", sort);
		mav = new ModelAndView("rank/learningRank");
		return mav;
	}
	
	@RequestMapping(value = "creditRank/{sort}", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_RANK_CREDIT)
	public ModelAndView toCreditRank(ModelAndView mav,
			@PathVariable(value = "sort") String sort) {
		mav.addObject("sort", sort);
		mav = new ModelAndView("rank/creditRank");
		return mav;
	}
	
	/**
	 * 积分排行榜
	 * 
	 * @return
	 */
	@RequestMapping(value = "credit_rank")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	@HasPermission(AclFunction.FTN_LRN_RANK_CREDIT)
	public String getCreditsRank(Model model, Page page, WizbiniLoader wizbini, loginProfile prof){
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		userCreditsService.getUserCreditsRankList(page, tcrId);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 积分排行榜页面获取我的积分信息
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("my_credit")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_RANK_CREDIT)
	public String getMyCreditsDetail(loginProfile prof, Model model,
			Page page,
			WizbiniLoader wizbini){
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		if(!dbRegUser.USR_STATUS_SYS.equalsIgnoreCase(prof.usr_status)){
			model.addAttribute("my_credit_detail", userCreditsService.getUserCreditAndRank(prof.usr_ent_id, tcrId));
		}
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 课程排行榜
	 * 
	 * @return
	 */	
	@RequestMapping(value = "course_rank")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_RANK_COURSE)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String courseRank(Model model, Page page, WizbiniLoader wizbini, loginProfile prof) {
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		aeItemService.getAeitemRank(page, tcrId, prof.usr_ent_id);
		return JsonFormat.format(model, page);
	}
	@RequestMapping("myLearningSituation")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_RANK_LEARNING)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String myLearningSituation(loginProfile prof, Model model, Page page, WizbiniLoader wizbini){
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		page.getParams().put("tcrId", tcrId);
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		if(!prof.usr_status.equalsIgnoreCase("sys")){
			model.addAttribute("learnDetail", learningSituationService.getLearningRankDetail(page));
		}
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping("learningSituationRank")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_RANK_LEARNING)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String learningSituationRank(loginProfile prof, Model model, Page page, WizbiniLoader wizbini){
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		page.getParams().put("tcrId", tcrId);
		learningSituationService.getLearningRankList(page);
		return JsonFormat.format(model, page);
	}
	/**
	 * 手机端主页数据
	 * @param model
	 * @param page
	 * @param wizbini
	 * @param prof
	 * @return
	 */
	@RequestMapping("mobile/main/json")
	@ResponseBody
	@HasPermission(value = {AclFunction.FTN_LRN_RANK_COURSE, AclFunction.FTN_LRN_RANK_CREDIT, AclFunction.FTN_LRN_RANK_LEARNING})
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String mainJson(Model model, Page page, WizbiniLoader wizbini, loginProfile prof){
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}		
		page.getParams().put("tcrId", tcrId);
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		model.addAttribute("creditDetail", userCreditsService.getUserCreditAndRank(prof.usr_ent_id, tcrId));
		model.addAttribute("learnDetail", learningSituationService.getLearningRankDetail(page));
		return JsonFormat.format(model, page);
	}	
}
