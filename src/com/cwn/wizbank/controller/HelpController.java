package com.cwn.wizbank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.HelpQuestion;
import com.cwn.wizbank.entity.HelpQuestionType;
import com.cwn.wizbank.entity.KbCatalog;
import com.cwn.wizbank.entity.vo.HelpQuestionTypeVo;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.Anonymous;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.HelpQuestionService;
import com.cwn.wizbank.services.HelpQuestionTypeService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

@Controller
@RequestMapping("help")
public class HelpController {
	
	private static final HelpQuestion Integer = null;

	Logger logger = LoggerFactory.getLogger(HelpController.class);
	
	@Autowired
	HelpQuestionService helpQuestionService;
	
	@Autowired
	HelpQuestionTypeService helpQuestionTypeService;
	
	@RequestMapping("")
	@Anonymous
	public ModelAndView help(Model model,loginProfile prof,ModelAndView mav, HttpServletRequest request,Params params) {
		mav = new ModelAndView("help/help");
		Map<String, Object> map = new HashMap<String,Object>();
		mav.addObject("is_admin", false);
		if(null==prof){
			map.put("hqt_is_publish", 1);
			map.put("hqt_pid", 0);
			params.setParams(map);
		}else{
			if(!AccessControlWZB.isSysAdminRole(prof.current_role)){
				map.put("hqt_is_publish", 1);
				map.put("hqt_pid", 0);
				params.setParams(map);
			}else{
				mav.addObject("is_admin", true);
			}
		}
		List<HelpQuestionTypeVo> typeList = helpQuestionTypeService.getQuestinTypes(params);
		mav.addObject("type_list", typeList);
		//是否隐藏页面header，如果fromlogin为true，则隐藏
		mav.addObject("hideHeaderInd", "true");
		return mav;
	}
	
	@RequestMapping("admin/indexJson")
	@ResponseBody
	public String indexJson(Model model, loginProfile prof, Page<HelpQuestionTypeVo> page) {
		helpQuestionTypeService.listPage(page,prof);
		return JsonFormat.format(model, page);
	}
	
	
	@RequestMapping("questionType/save")
	@ResponseBody
	public String questionTypeSave(HttpSession session,
			ModelAndView mav, HttpServletRequest request,@ModelAttribute HelpQuestionType helpQuestionType) {
		ModelMap model = new ModelMap();
		if(helpQuestionType != null )
		{
			helpQuestionType.setHqt_language("zh-cn");
			helpQuestionType.setHqt_is_publish(1);
		}
		try{
			helpQuestionTypeService.save(helpQuestionType);
			model.addAttribute("success",true);
			model.addAttribute("data", helpQuestionType);
		}catch(Exception ex){
			model.addAttribute("success",false);
		}
		return JsonFormat.format(model);
	}
	
	@RequestMapping("questionType/{id}")
	@ResponseBody
	@Anonymous
	public String questionType(@PathVariable(value="id") int id,
			Model model) {
		return JsonFormat.format(helpQuestionTypeService.getHelpQuestionType(id));
	}
	
	@RequestMapping("questionType/update/{id}")
	@ResponseBody
	public String questionTypeUpdate(HttpSession session,
			ModelAndView mav, HttpServletRequest request,
			@ModelAttribute HelpQuestionType helpQuestionType) {
		ModelMap model = new ModelMap();
		try{
			helpQuestionType.setHqt_is_publish(0);
			helpQuestionType.setHqt_pid(0);
			helpQuestionType.setHqt_language("zh-cn");
			helpQuestionTypeService.update(helpQuestionType);
			model.addAttribute("success",true);
			model.addAttribute("data", helpQuestionType);
		}catch(Exception ex){
			model.addAttribute("success",false);
		}
		return JsonFormat.format(model);
	}
	
	/**
	 * 检测编号
	 * @param helpQuestion
	 * @return
	 */
	@RequestMapping("question/check_number")
	@HasPermission(value = {AclFunction.FTN_AMD_SNS_MGT, AclFunction.FTN_AMD_SYS_SETTING_MAIN})
	@ResponseBody
	public String checkNumber(@ModelAttribute HelpQuestion helpQuestion,@RequestParam (value="question_act")String question_act) {
		ModelMap model = new ModelMap();
		try{
			helpQuestion.setHqt_number(helpQuestion.getHqt_number().trim());
			if(null!=question_act && question_act.equals("save")){
				helpQuestion.setHq_id(null);
			}
			boolean flg = true;
			int count = helpQuestionService.checkNumber(helpQuestion);
			if(count > 0)
			{
				flg = false;
			}
			model.addAttribute("success",flg);
		}catch(Exception ex){
			ex.printStackTrace();
			model.addAttribute("success",false);
		}
		return JsonFormat.format(model);
	}
	
	@RequestMapping(value="questionType/{id}",method = RequestMethod.DELETE)
	@ResponseBody
	@HasPermission(value = {AclFunction.FTN_AMD_SNS_MGT, AclFunction.FTN_AMD_SYS_SETTING_MAIN})
	public String questionTypeDelete(
			@PathVariable(value="id") int id) {
		ModelMap model = new ModelMap();
		try{
			helpQuestionTypeService.deleteHelpQuestionType(id);
			model.addAttribute("success",true);
		}catch(Exception ex){
			ex.printStackTrace();
			model.addAttribute("success",false);
		}
		return JsonFormat.format(model);
	}
	
	@RequestMapping(value="questions")
	@ResponseBody
	public String getQuestions(Model model, Page<HelpQuestion> page,@RequestParam (value="isAdmin") boolean isAdmin ,
			@RequestParam (value="typeId") Integer typeId ,
			@RequestParam (value="searchContext",required=false) String searchContext ,
			Params params) {
		Map<String, Object> map = new HashMap<String,Object>();
		if(null!=typeId && typeId!=0){
			page.getParams().put("hq_type_id", typeId);
		}
		if(!cwUtils.isEmpty(searchContext)){
			page.getParams().put("hq_title", searchContext);
		}
		params.setParams(map);
	    helpQuestionService.getQuestionList(page);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping(value="question/{id}",method = RequestMethod.GET)
	@Anonymous
	@ResponseBody
	public String getQuestion(@PathVariable(value="id") String id) {
		HelpQuestion que = new HelpQuestion();
		que.setHqt_number(id);
		return JsonFormat.format(helpQuestionService.getQuestion(que));
	}
	
	@RequestMapping(value="question/publish/{id}",method = RequestMethod.POST)
	@HasPermission(value = {AclFunction.FTN_AMD_SNS_MGT, AclFunction.FTN_AMD_SYS_SETTING_MAIN})
	@ResponseBody
	public String questionPublish(@PathVariable(value="id") int id,
			@RequestParam (value="publishAct") Integer publishAct ) {
		ModelMap model = new ModelMap();
		try{
			helpQuestionService.publishQuestion(id, publishAct);
			model.addAttribute("success",true);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("[com.cwn.wizbank.controller.HelpController questionPublish] err : " ,ex.getMessage());
			model.addAttribute("success",false);
		}
		return JsonFormat.format(model); 
	}
	
	@RequestMapping("question/save")
	@HasPermission(value = {AclFunction.FTN_AMD_SNS_MGT, AclFunction.FTN_AMD_SYS_SETTING_MAIN})
	@ResponseBody
	public String questionSave(@ModelAttribute HelpQuestion helpQuestion) {
		ModelMap model = new ModelMap();
		try{
			helpQuestion.setHqt_number(helpQuestion.getHqt_number().trim());
			helpQuestionService.save(helpQuestion);
			model.addAttribute("success",true);
			model.addAttribute("data", helpQuestion);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			logger.error("[com.cwn.wizbank.controller.HelpController questionSave] err : " ,ex.getMessage());
			model.addAttribute("success",false);
		}
		return JsonFormat.format(model);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "admin/deleteByIds")
	@ResponseBody
	public void delete(@RequestParam(value = "ids", required = false, defaultValue = "") String ids, WizbiniLoader wizbini) {
		if (ids != null && !"".equals(ids)) {
			String[] arrId = ids.split(",");
			if (arrId != null && arrId.length > 0) {
				for (int i = 0; i < arrId.length; i++) {
					int id = (int) EncryptUtil.cwnDecrypt(arrId[i]);
					helpQuestionService.delHelpQuestion(id);
				}
			}
		}

	}
	
	@RequestMapping("question/update/{id}")
	@ResponseBody
	public String questionUpdate(@PathVariable(value="id") Integer id, @ModelAttribute HelpQuestion helpQuestionVo) {
		ModelMap model = new ModelMap();
		try{
			helpQuestionVo.setHqt_number(helpQuestionVo.getHqt_number().trim());
			model.addAttribute("data", helpQuestionService.updateQuestion( id , helpQuestionVo));
			model.addAttribute("success",true);
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			logger.error("[com.cwn.wizbank.controller.HelpController questionUpdate] err : " ,ex.getMessage());
			model.addAttribute("success",false);
		}
		return JsonFormat.format(model);
	}
	
	@RequestMapping(value="question/{id}",method = RequestMethod.DELETE)
	@ResponseBody
	@HasPermission(value = {AclFunction.FTN_AMD_SNS_MGT, AclFunction.FTN_AMD_SYS_SETTING_MAIN})
	public String questionDelete(
			@PathVariable(value="id") int id) {
		ModelMap model = new ModelMap();
		try{
			helpQuestionService.delHelpQuestion(id);
			model.addAttribute("success",true);
		}catch(Exception ex){
			ex.printStackTrace();
			model.addAttribute("success",false);
		}
		return JsonFormat.format(model);
	}
	
	/**
	 * 
	 * @param model
	 * @param prof
	 * @param mav
	 * @param request
	 * @param params
	 * @return
	 */
	@RequestMapping("vip")
	@Anonymous
	public ModelAndView vip(Model model,loginProfile prof,ModelAndView mav, HttpServletRequest request,Params params) {
		mav = new ModelAndView("help/vip");
		return mav;
	}
	
}