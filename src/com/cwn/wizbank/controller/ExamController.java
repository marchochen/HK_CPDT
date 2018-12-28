package com.cwn.wizbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.AeApplicationService;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.ItemTargetLrnDetailService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

/**
 * 
 * @author leon.li 课程
 */
@Controller
@RequestMapping("exam")
public class ExamController {

	@Autowired
	ItemTargetLrnDetailService itemTargetLrnDetailService;
	@Autowired
	AeApplicationService aeApplicationService;
	@Autowired
	AeItemService aeItemService;
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	/**
	 * 课程页面
	 * 
	 * @param command
	 *            recommend 推荐课程页面 ，signup 已报名页面, courseCatalog 课程目录
	 * @return
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value = "courseCatalog", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_EXAM_CATALOG)
	public ModelAndView toCourseCatalog(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini){
		mav = new ModelAndView("exam/courseCatalog");
		List<TcTrainingCenter> myTrainingCenter = tcTrainingCenterService.getMyTrainingCenter(wizbini, prof.usr_ent_id);
		mav.addObject("myTrainingCenter",myTrainingCenter);
		mav.addObject("command", "courseCatalog");
		return mav;
	}
	@RequestMapping(value = "recommend", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_EXAM_RECOMMEND)
	public ModelAndView toRecommend(ModelAndView mav, loginProfile prof){
		mav = new ModelAndView("exam/recommend");
		mav.addObject("command", "recommend");
		return mav;
	}
	@RequestMapping(value = "signup", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_EXAM_SIGNUP)
	public ModelAndView toSignup(ModelAndView mav, loginProfile prof){
		mav = new ModelAndView("exam/signup");
		mav.addObject("command", "signup");
		return mav;
	}
	
	/**
	 * 获取推荐课程的数据
	 * 
	 * @param command
	 * 			 recommend 推荐课程 ，signup 已报名课程 ，open 公开课
	 * @param isCompulsory
	 * @param itemType
	 * @return
	 */
	@RequestMapping(value = "recommendJson")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_EXAM_RECOMMEND)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String recommendJson(Model model, Page page, loginProfile prof){
		page.getParams().put("isExam", 1);
		itemTargetLrnDetailService.recommend(prof.usr_ent_id, prof.cur_lan, page);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping(value = "signupJson")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_EXAM_SIGNUP)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String signupJson(Model model, Page page, loginProfile prof){
		page.getParams().put("isExam", 1);
		aeApplicationService.signup(prof.usr_ent_id, prof.cur_lan, page);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping(value = "catalogCourseJson")
	@ResponseBody
	@HasPermission(AclFunction.FTN_LRN_EXAM_CATALOG)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String catalogCourseJson(Model model, Page page, loginProfile prof, WizbiniLoader wizbini){
		page.getParams().put("isExam", 1);
		long tcrId = 0;
		if(page.getParams().get("tcrId") == null || Integer.parseInt((String)page.getParams().get("tcrId")) < 1){
			if (wizbini.cfgSysSetupadv.isTcIndependent()) {
				tcrId = prof.my_top_tc_id;
			}
			page.getParams().put("tcrId", tcrId);
		}
		aeItemService.getCatalogCourse(prof.usr_ent_id, prof.cur_lan, page);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 课程详细页面获取数据
	 * 
	 * @param itm_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "detailJson/{encItmId}")
	@ResponseBody
	@HasPermission(value = {AclFunction.FTN_LRN_EXAM_RECOMMEND, AclFunction.FTN_LRN_EXAM_SIGNUP, AclFunction.FTN_LRN_EXAM_CATALOG})
	public String detailJson(Model model,
			Params param,
			@PathVariable(value = "encItmId") String encItmId,
			@RequestParam(value = "tkhId", required = false, defaultValue = "0") long tkhId,
			@RequestParam(value = "appId", required = false, defaultValue = "0") long appId,
			@RequestParam(value = "resId", required = false, defaultValue = "0") long resId,
			@RequestParam(value = "mobileInd", required = false, defaultValue = "0") int mobileInd,
			loginProfile prof) throws Exception {
		
		long itmId = EncryptUtil.cwnDecrypt(encItmId);
		
		aeItemService.getDetial(model,prof.my_top_tc_id, itmId, tkhId, prof.usr_ent_id, prof.cur_lan, prof.hasStaff, mobileInd, prof.usr_id);
		return JsonFormat.format(param, model);
	}
	
	@RequestMapping(value = "getMyExam")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getMyExam(Model model, Page page,loginProfile prof,
			WizbiniLoader wizbini) {
		page.getParams().put("isExam", 1);
		long tcrId = 0;
		if(page.getParams().get("tcrId") == null || Integer.parseInt((String)page.getParams().get("tcrId")) < 1){
			if (wizbini.cfgSysSetupadv.isTcIndependent()) {
				tcrId = prof.my_top_tc_id;
			}
			page.getParams().put("tcrId", tcrId);
		}
		aeApplicationService.getMyCourse(prof.usr_ent_id, prof.cur_lan, page);
		return JsonFormat.format(model, page);
	}
	
}
