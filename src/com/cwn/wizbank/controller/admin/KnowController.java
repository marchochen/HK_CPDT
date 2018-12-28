package com.cwn.wizbank.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.KnowCatalog;
import com.cwn.wizbank.entity.KnowQuestion;
import com.cwn.wizbank.entity.Knowanswer;
import com.cwn.wizbank.entity.ModuleTempFile;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.entity.vo.AeTreeNodeVo;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.exception.ErrorException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AcEntityRoleService;
import com.cwn.wizbank.services.AcRoleFunctionService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.KnowCatalogRelationService;
import com.cwn.wizbank.services.KnowCatalogService;
import com.cwn.wizbank.services.KnowQuestionService;
import com.cwn.wizbank.services.KnowVoteDetailService;
import com.cwn.wizbank.services.KnowanswerService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.services.UserCreditsService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.FileUtils;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.utils.RequestStatus;
import com.cwn.wizbank.web.validation.KnowQuestionValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;
//import com.cwn.wizbank.utils.Param;

/**
 * 
 * @author lance 问答
 */
@Controller("adminKnowController")
@RequestMapping("admin/know")
@HasPermission({AclFunction.FTN_AMD_Q_AND_A_VIEW,AclFunction.FTN_AMD_Q_AND_A_MAIN})
public class KnowController {
	@Autowired
	KnowQuestionService knowQuestionService;
	
	@Autowired
	KnowCatalogService knowCatalogService;
	
	@Autowired
	KnowCatalogRelationService knowCatalogRelationService;
	
	@Autowired
	KnowanswerService knowAnswerService;
	
	@Autowired
	KnowVoteDetailService knowVoteDetailService;
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@Autowired
	AcRoleFunctionService acRoleFunctionService;
	
	@Autowired
	AclService aclService;
	
	@Autowired
	AcEntityRoleService acEntityRoleService;
	
	@Autowired
	UserCreditsService userCreditsService;
	
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	@RequestMapping("detail/{id}")
	public String knowDetail(@PathVariable(value="id") String id,loginProfile prof,@RequestParam(value = "ftn_type",required=false,defaultValue="FTN_AMD_Q_AND_A_MAIN") String ftn_type) throws ErrorException{
		long kqId = EncryptUtil.cwnDecrypt(id);
		KnowQuestion kq = knowQuestionService.get(kqId);
		if(kq != null){
			return "redirect:/app/admin/know/detail/" + kq.getQue_type() + "/"
					+ id +"?ftn_type="+ftn_type;
		}else{
			throw new ErrorException(LabelContent.get(prof.cur_lan, "know_record_invalid"));
		}
	}
		
	/**
	 * 问答页面
	 * 
	 * @param command
	 *            : allKnow 所有问题 
	 *            : knowHelp 帮助
	 *            : myKnow 我的问题
	 *            : AskKnow 我要提问
	 * @return
	 */
	@RequestMapping(value = "{command}", method = RequestMethod.GET)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public ModelAndView knowHomePage(ModelAndView mav, WizbiniLoader wizbini, loginProfile prof, Page page, @PathVariable(value = "command") String command,@RequestParam(value = "ftn_type",required=false,defaultValue="FTN_AMD_Q_AND_A_MAIN") String ftn_type,@RequestParam(value="isView", required=false, defaultValue="false") boolean isView) {
		mav = new ModelAndView("admin/know/" + command);
		mav.addObject("command", command);
		mav.addObject("isView", isView);
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		double userCredits=userCreditsService.getUserTotalCredits( prof.usr_ent_id, "all");
		if(command.equalsIgnoreCase("myKnow")){
			knowQuestionService.getKnowQuestionList(page, "", 0, "my_question", prof.usr_ent_id, 0, 0, "", 0);
			mav.addObject("solved_num", page.getTotalRecord());
			knowQuestionService.getKnowQuestionList(page, "", 0, "my_answer", prof.usr_ent_id, 0, 0, "", 0);
			mav.addObject("unsolved_num", page.getTotalRecord());
		} else {
			knowQuestionService.getKnowSolveTotal(mav, tcrId);
		}
		//获取用户信息
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		//用户积分数
		mav.addObject("credits", userCredits);
		
		if(command.equalsIgnoreCase("allknow")){
			mav.addObject("ftn_type",ftn_type);
		}
		
		return mav;
	}
	
	/**
	 * 根据问题类型获取问题信息
	 * 
	 * @param queType 问题类型
	 * @param kcaId 分类id
	 * @return
	 */
	@RequestMapping("allKnow/que/{queType}/{kcaId}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getKnowQuestion(Model model, Page page, WizbiniLoader wizbini, loginProfile prof, @PathVariable(value = "queType") String queType,
			@PathVariable(value = "kcaId") long kcaId, @RequestParam(value="searchContent", defaultValue="", required = false) String searchContent) {
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent() && kcaId == 0){
			tcrId = prof.my_top_tc_id;
		}
		knowQuestionService.getKnowQuestionList(page, queType, kcaId, "", 0, 0, 0, searchContent, tcrId);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 问题分类
	 * 
	 * @param kcaType 分类类型
	 * @param kcaId 分类id
	 * @return
	 */
	@RequestMapping("allKnow/kca/{kcaType}/{kcaId}")
	@ResponseBody
	public String getknowCatalog(Model model, WizbiniLoader wizbini, loginProfile prof, 
			Params params,
			@PathVariable(value = "kcaType") String kcaType, 
			@PathVariable(value = "kcaId") long kcaId) {
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}else{
			TcTrainingCenter rootTc =  tcTrainingCenterService.getRootTrainingCenter();
			tcrId = rootTc.getTcr_id();
		}
		model.addAttribute("kca", knowCatalogService.getKnowCatalogLlist(kcaType, kcaId, tcrId, prof, false));
		return JsonFormat.format(params, model);
	}
	
	
	/**
	 * 分类
	 * @param model
	 * @param page
	 * @param wizbini
	 * @param prof
	 * @return
	 */
	@RequestMapping("catalogPage")
	@ResponseBody
	public String page(Model model, Page<KnowCatalog> page, WizbiniLoader wizbini, loginProfile prof) {
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		knowCatalogService.page(page, tcrId);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping("catalog/add")
	@ResponseBody
	public String addCatalog(Model model, @ModelAttribute("catalog") KnowCatalog catalog, WizbiniLoader wizbini, loginProfile prof) {
		long tcrId = 1;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		
		knowCatalogService.saveOrUpdate(catalog, prof.usr_ent_id, tcrId);
		//model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		return "";//JsonFormat.format(model);
	}
	
	@RequestMapping("catalog/checkCatalogName")
	@ResponseBody
	public int checkCatalogName(Model model, @ModelAttribute("catalog") KnowCatalog catalog) {
		model.addAttribute("catalog", knowCatalogService.checkCatalogName(catalog));
		return knowCatalogService.checkCatalogName(catalog);
	}
	@RequestMapping("catalog/detail/{id}")
	@ResponseBody
	public String getCatalog(Model model, @PathVariable("id") long id) {
		model.addAttribute("catalog", knowCatalogService.get(id));
		return JsonFormat.format(model);
	}
	
	
	@RequestMapping("catalog/del/{id}")
	@ResponseBody
	@HasPermission(AclFunction.FTN_AMD_Q_AND_A_MAIN)
	public String delCatalog(Model model, loginProfile prof, @PathVariable("id") long id, 
			@RequestParam(value="type", defaultValue="parent") String type) {
		int result = this.knowCatalogService.del(id, type);
		if(result < 1) {
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		} else {
			model.addAttribute(RequestStatus.STATUS, RequestStatus.ERROR);
			model.addAttribute(RequestStatus.MSG, LabelContent.get(prof.cur_lan, "label_core_community_management_185"));
		}
		return JsonFormat.format(model);
	}
	
	@RequestMapping("catalog/publish/{id}")
	@HasPermission(AclFunction.FTN_AMD_Q_AND_A_MAIN)
	@ResponseBody
	public String updateCatalogStatus(Model model, loginProfile prof, @PathVariable("id") long id, 
			@RequestParam(value="type", defaultValue="0") String type) {
		if(!"1".equals(type) && !"0".equals(type)) {
			model.addAttribute(RequestStatus.STATUS, RequestStatus.ERROR);
		} else {
			this.knowCatalogService.updateStatus(id, type);
			model.addAttribute(RequestStatus.STATUS, RequestStatus.SUCCESS);
		}
		return JsonFormat.format(model);
	}
	
	
	@RequestMapping("add")
	public ModelAndView add(@RequestParam(value="type", required=false) String type,
			@RequestParam(value="id", required=false) String encryptId,@RequestParam(value="isView", required=false, defaultValue="false") boolean isView,loginProfile prof) throws DataNotFoundException{
		ModelAndView mav = new ModelAndView("admin/know/add");
		Long id = null;
		if(!StringUtils.isEmpty(encryptId)){
			 id = EncryptUtil.cwnDecrypt(encryptId);
		}
		KnowQuestion question = null;
		if(id != null && id > 0) {
			question= this.knowQuestionService.get(id);
			if(question == null) {
				throw new DataNotFoundException();
			} else {
				if(KnowQuestion.TYPE_FAQ.equals(question.getQue_type())) {
					question.setKnowAnswer(this.knowAnswerService.getByQueId(id));
				}
				List<Long> parentIds = knowCatalogRelationService.getKnowCatalogRelationMapper().selectParents(question.getQue_id());
				mav.addObject("parentIds", parentIds);
				mav.addObject("users", question.getQue_ask_ent_ids()!=null?regUserService.getRegUserMapper().getUserByIds(Arrays.asList(question.getQue_ask_ent_ids().split(","))):null);
			}
		} else {
			question = new KnowQuestion();
		}
		double userCredits=userCreditsService.getUserTotalCredits( prof.usr_ent_id, "all");
		mav.addObject("question", question);
		mav.addObject("type", type);
		mav.addObject("id", id);
		mav.addObject("isView", isView);
		mav.addObject("credits", userCredits);
		return mav;
	}
	
	

	/**
	 * 添加问题
	 * @param queTitle 问题标题
	 * @param queContent 问题内容
	 * @param kcaIdOne 一级分类id
	 * @param kcaIdTwo 二级分类id
	 * @return
	 */
	@RequestMapping(value = "addQuestion", method = RequestMethod.POST)
	public String addKnowQuestion(Model model, RedirectAttributes attr, WizbiniLoader wizbini, loginProfile prof, 
			@RequestParam(value = "kcaIdOne") int kcaIdOne,
			@RequestParam(value = "kcaIdTwo", required = false, defaultValue="0") int kcaIdTwo,
			@ModelAttribute("question") KnowQuestion question,
			BindingResult result, @RequestParam(value="isView", required=false, defaultValue="false") boolean isView) throws Exception {
		
		WzbValidationUtils.invokeValidator(new KnowQuestionValidator(prof), question, result);
		if (result.hasErrors()) {
			//model.addAttribute("users", regUserService.getRegUserMapper().getUserByIds(Arrays.asList(question.getQue_ask_ent_ids().split(","))));
			attr.addAttribute("type",question.getQue_type());
			return "redirect:add?isView="+isView;
		} else { 		
			double userCredits=userCreditsService.getUserTotalCredits( prof.usr_ent_id, "all");
			if(null !=question && null !=question.getQue_bounty() &&  userCredits < question.getQue_bounty()){
				throw new DataNotFoundException(LabelContent.get(prof.cur_lan, "know_inputBouty_tip2"));
			}
			knowQuestionService.saveOrUpdateQuestion(question, kcaIdOne, kcaIdTwo, prof.usr_ent_id, prof.usr_id, prof.usr_display_bil, prof);
		}
		if(KnowQuestion.TYPE_FAQ.equals(question.getQue_type())) {
			attr.addAttribute("tab", 4);
		} else if(KnowQuestion.TYPE_UNSOLVED.equals(question.getQue_type())){
			attr.addAttribute("tab", 1);
		} else if(KnowQuestion.TYPE_SOLVED.equals(question.getQue_type())){
			attr.addAttribute("tab", 2);
		} else if(KnowQuestion.TYPE_POPULAR.equals(question.getQue_type())){
			attr.addAttribute("tab", 3);
		}
		
		String ftn_type = AclFunction.FTN_AMD_Q_AND_A_MAIN;
		if(isView){
			ftn_type = AclFunction.FTN_AMD_Q_AND_A_VIEW;
		}
		return "redirect:allKnow?isView="+isView+"&ftn_type="+ftn_type;
	}
	

	/**
	 * 问题列表
	 * @param command 
	 * 				my_question : 我的提问;  my_answer : 我的回答;
	 * 
	 * @return
	 */
	@RequestMapping("myKnow/{command}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getMyknow(Model model, Page page, loginProfile prof, @PathVariable(value = "command") String command,
			@RequestParam(value="searchContent", defaultValue="", required = false) String searchContent) {
		knowQuestionService.getKnowQuestionList(page, "", 0, command, prof.usr_ent_id, 0, 0, searchContent, 0);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 问题详细信息
	 * @param queId 问题id
	 * @param queType 问题类型
	 * @return
	 */
	@RequestMapping("detail/{queType}/{queId}")
	@SuppressWarnings({"rawtypes", "unchecked"})
	public ModelAndView getKnowDetail(ModelAndView mav, WizbiniLoader wizbini, Page page, loginProfile prof, 
			@PathVariable(value = "queType") String queType, @PathVariable(value = "queId") String encryptQueId
			,@RequestParam(value = "ftn_type",required=false,defaultValue="FTN_AMD_Q_AND_A_MAIN") String ftn_type) {
		long queId = EncryptUtil.cwnDecrypt(encryptQueId);
		mav = new ModelAndView("admin/know/detail");
		
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		knowQuestionService.getKnowSolveTotal(mav, tcrId);
		//获取用户信息
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));

		Page<KnowQuestion> knowQuestionPage = knowQuestionService.getKnowQuestionList(page, queType, 0, "", 0, queId, 0, "", 0);
		List<KnowQuestion> list = knowQuestionPage.getResults();
		if(list != null && list.size() > 0){
			if(list.get(0).getQue_content()!= null){
				list.get(0).setQue_content(list.get(0).getQue_content().replaceAll("\r\n", "<br/>"));
			}
			if(StringUtils.isNotEmpty(list.get(0).getQue_ask_ent_ids())) {
				mav.addObject("users", regUserService.getRegUserMapper().getUserByIds(Arrays.asList(list.get(0).getQue_ask_ent_ids().split(","))));
			}
			
			//动态的附件
			String urlPath =  "/"+ wizbini.cfgSysSetupadv.getFileUpload().getEditorDir().getUrl();
			List<ModuleTempFile> fileList = list.get(0).getFileList();
			if(fileList != null){
				for(ModuleTempFile mtf : fileList) {
					if(!"url".equals(mtf.getMtf_file_type())){
						mtf.setMtf_url(	urlPath + "/" + FileUtils.SNS_TEMP_DIR + "/" + mtf.getMtf_module() + "/" + mtf.getMtf_usr_id());
					}
				}
			}
			mav.addObject("know_detail", list.get(0));
			mav.addObject("my_usr_id", prof.usr_ent_id);
			if(list.get(0).getQue_type().equalsIgnoreCase("SOLVED")){
				Page<Knowanswer> knowanswerPage = knowAnswerService.getKnowAnswerList(page, prof.usr_ent_id, queId, 1,wizbini);
				Knowanswer bestAnswer = knowanswerPage.getResults().get(0);
				if(bestAnswer.getAns_content()!=null)
				{
					bestAnswer.setAns_content(bestAnswer.getAns_content().replaceAll("\r\n", "<br/>").replaceAll("\n", "<br/>"));
				}
				mav.addObject("best_answer", bestAnswer);
				if(bestAnswer.getAns_vote_total() == null || bestAnswer.getAns_vote_total() == 0){
					mav.addObject("good_rate", "0%");
					mav.addObject("not_good_rate", "0%");
				} else {
					mav.addObject("good_rate", knowQuestionService.CountRate(bestAnswer.getAns_vote_for(), bestAnswer.getAns_vote_total()));
					mav.addObject("not_good_rate", knowQuestionService.CountRate(bestAnswer.getAns_vote_down(), bestAnswer.getAns_vote_total()));
				}
			} else if(list.get(0).getQue_type().equalsIgnoreCase("FAQ")){
				Page<Knowanswer> knowanswerPage = knowAnswerService.getKnowAnswerList(page, prof.usr_ent_id, queId, 1,wizbini);
				if(knowanswerPage != null && knowanswerPage.getResults() != null && knowanswerPage.getResults().size() > 0) {
				Knowanswer answer = knowanswerPage.getResults().get(0);
				mav.addObject("answer", answer);
				}
			}
		}
		//是否以培训管理员角色进入
		boolean isTADM = aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_Q_AND_A_MAIN});
		if(!isTADM){
			List<KnowCatalog> catalogList = knowCatalogService.getCatalogSituation(queId);
			mav.addObject("parent_catalog", catalogList.get(0));
			//是否在二级分类里
			if(catalogList.size() > 1){
				mav.addObject("child_catalog", catalogList.get(1));
			}
		}
		mav.addObject("isTADM", isTADM);
		mav.addObject("ftn_type",ftn_type);
		return mav;
	}
	/**
	 * 问题详细信息(手机端)
	 * @param queId 问题id
	 * @param queType 问题类型
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("detail/json/{queType}/{queId}")
	@ResponseBody
	public String getKnowDetail(Model model, WizbiniLoader wizbini, Page page, loginProfile prof,
			@PathVariable(value = "queType") String queType, @PathVariable(value = "queId") long queId){
		if(queType.equals("all")){
			KnowQuestion kq = knowQuestionService.get(queId);
			queType = kq.getQue_type();
		}
		Page<KnowQuestion> knowQuestionPage = knowQuestionService.getKnowQuestionList(page, queType, 0, "", 0, queId, 0, "", 0);
		List<KnowQuestion> list = knowQuestionPage.getResults();
		if(list != null && list.size() > 0){
			//动态的附件
			String urlPath =  "/"+ wizbini.cfgSysSetupadv.getFileUpload().getEditorDir().getUrl();
			List<ModuleTempFile> fileList = list.get(0).getFileList();
			if(fileList != null){
				for(ModuleTempFile mtf : fileList) {
					if(!"url".equals(mtf.getMtf_file_type())){
						mtf.setMtf_url(	urlPath + "/" + FileUtils.SNS_TEMP_DIR + "/" + mtf.getMtf_module() + "/" + mtf.getMtf_usr_id());
					}
				}
			}
			model.addAttribute("know_detail", list.get(0));
			model.addAttribute("my_usr_id", prof.usr_ent_id);
			if(list.get(0).getQue_type().equalsIgnoreCase("SOLVED")){
				Page<Knowanswer> knowanswerPage =knowAnswerService.getKnowAnswerList(page, prof.usr_ent_id, queId, 1,wizbini);
				Knowanswer bestAnswer = knowanswerPage.getResults().get(0);
				model.addAttribute("best_answer", bestAnswer);
				if(bestAnswer.getAns_vote_total() == null || bestAnswer.getAns_vote_total() == 0){
					model.addAttribute("good_rate", "0");
					model.addAttribute("not_good_rate", "0");
				} else {
					model.addAttribute("good_rate", knowQuestionService.getRate(bestAnswer.getAns_vote_for(), bestAnswer.getAns_vote_total()));
					model.addAttribute("not_good_rate", knowQuestionService.getRate(bestAnswer.getAns_vote_down(), bestAnswer.getAns_vote_total()));
				}
			} else if(list.get(0).getQue_type().equalsIgnoreCase("FAQ")){
				Page<Knowanswer> knowanswerPage =knowAnswerService.getKnowAnswerList(page, prof.usr_ent_id, queId, 1,wizbini);
				Knowanswer answer = knowanswerPage.getResults().get(0);
				model.addAttribute("answer", answer);
			}
		}
		return JsonFormat.format(model, page);
	}
	/**
	 * 回答列表
	 * @param queId 问题id
	 * @return
	 */
	@RequestMapping("answer/{queId}/{listSize}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getknowAnswer(Model model, Page page, loginProfile prof,  
			Params params,WizbiniLoader wizbini,
			@PathVariable(value = "queId") long queId,  @PathVariable(value = "listSize") int listSize) {
		page.setPageSize(listSize);
		model.addAttribute("ask_list", knowAnswerService.getKnowAnswerList(page, prof.usr_ent_id, queId, 0,wizbini));
		return JsonFormat.format(params, model);
	}
		
	
	/**
	 * 相关问题
	 * @param queId 问题id
	 * @param queType 问题类型
	 * @param kcaId 问题分类id
	 * @return
	 */
	@RequestMapping("relevantKnow/{queId}/{queType}/{kcaId}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getRelevantKnow(Model model, Page page, @PathVariable(value = "queId") long queId,
			Params params,
			@PathVariable(value = "queType") String queType, @PathVariable(value = "kcaId") long kcaId) {
		page.setPageSize(4);
		model.addAttribute("know_list", knowQuestionService.getKnowQuestionList(page, queType, 0, "", 0, queId, kcaId, "", 0));
		return JsonFormat.format(params, model);
	}
	
	/**
	 * 回答列表(手机端使用)
	 * @param queId 问题id
	 * @return
	 */
	@RequestMapping("answers/{queId}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getknowAnswers(Model model, Page page, loginProfile prof,WizbiniLoader wizbini,
			@PathVariable(value = "queId") long queId) {		
		knowAnswerService.getKnowAnswerList(page, prof.usr_ent_id, queId, 0,wizbini);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 相关问题(手机端使用)
	 * @param queId 问题id
	 * @param queType 问题类型
	 * @param kcaId 问题分类id
	 * @return
	 */
	@RequestMapping("relevantKnow/mobile/{queId}/{queType}/{kcaId}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getRelevantKnows(Model model, Page page, @PathVariable(value = "queId") long queId,
			@PathVariable(value = "queType") String queType, @PathVariable(value = "kcaId") long kcaId) {
		knowQuestionService.getKnowQuestionList(page, queType, 0, "", 0, queId, kcaId, "", 0);
		return JsonFormat.format(model, page);
	}
	
	
	/**
	 * 回答问题
	 * @param queId 问题id
	 * @param ansContent 回答内容
	 * @param ansReferContent 参考资料
	 * @return
	 */
	@RequestMapping("addKnowAnswer/{queId}")
	@ResponseBody
	public void addKnowAnswer(loginProfile prof, @PathVariable(value = "queId") long queId, 
			@RequestParam(value="ansContent", defaultValue="", required=false) String ansContent,
			@RequestParam(value="ansReferContent", defaultValue="", required=false) String ansReferContent) throws Exception {
		knowAnswerService.addKnowAnswer(queId, prof.usr_ent_id, ansContent, ansReferContent,0);
		forCallOldAPIService.updUserCredits(null, Credit.ZD_COMMIT_ANS, prof.usr_ent_id, prof.usr_id, queId, 0);
	}
	
	/**
	 * 取消提问
	 * @param queId 问题id
	 * @return
	 */
	@RequestMapping("delQuestion/{que_id_str}")
	@ResponseBody
	public void delKnowQuestion(loginProfile prof, @PathVariable(value = "que_id_str") String que_id_str) throws Exception {
		String[] que_id_lst = que_id_str.split("~");
		for(String que_id : que_id_lst){ 
//			knowQuestionService.delKnowQuestion(prof.usr_ent_id, Long.parseLong(que_id));
			forCallOldAPIService.deleteQusetion(null, Long.parseLong(que_id));
		}
		if(!aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_Q_AND_A_MAIN})){
			forCallOldAPIService.updUserCredits(null, Credit.ZD_CANCEL_QUE, prof.usr_ent_id, prof.usr_id, 0, 0);
		}
	}
	
	/**
	 * 问题补充修改
	 * @param queId 问题id
	 * @param queContent 问题补充内容
	 * @return
	 */
	@RequestMapping("changeQueContent/{queId}")
	@ResponseBody
	public void changeQueContent(@PathVariable(value = "queId") long queId, loginProfile prof,
			@RequestParam(value="queContent", defaultValue="", required=false) String queContent){
		knowQuestionService.changeQueContent(prof.usr_ent_id, queId, queContent);
	}
	
	/**
	 * 设为最佳回答
	 * @param ansId 回答id
	 * @param queId 问题id
	 * @return
	 */
	@RequestMapping("setBestAnswer/{ansId}/{ansEntId}/{queId}")
	@ResponseBody
	public void setBestAnswer(loginProfile prof, @PathVariable(value = "ansId") long ansId, @PathVariable(value = "queId") long queId,
			@PathVariable(value = "ansEntId") long ansEntId) throws Exception {
		knowAnswerService.updateKnowAnswer(ansId, queId, prof.usr_ent_id);
		forCallOldAPIService.updUserCredits(null, Credit.ZD_RIGHT_ANS, ansEntId, prof.usr_id, queId, 0);	
		double bounty=knowQuestionService.getQueBounty(queId);
		if(bounty>0){
			forCallOldAPIService.updUserCredits(null, Credit.SYS_ANWSER_BOUNTY, ansEntId, prof.usr_id, queId, bounty, 0);
			forCallOldAPIService.updUserCredits(null, Credit.SYS_QUESTION_BOUNTY, prof.usr_ent_id,  prof.usr_id, queId, bounty, 0);
		}
	}
	
	/**
	 * 删除该条回答
	 * @param ansId 回答id
	 * @param queId 问题id
	 * @return
	 */
	@RequestMapping("delThisAnswer/{ansId}/{ansEntId}/{queId}")
	@ResponseBody
	public void delThisAnswer(loginProfile prof, @PathVariable(value = "ansId") long ansId, @PathVariable(value = "queId") long queId,
			@PathVariable(value = "ansEntId") long ansEntId) throws Exception {
		forCallOldAPIService.deleteAnswer(null, Long.parseLong(String.valueOf(queId)), ansEntId);//先执行
		knowAnswerService.deleteKnowAnswer(ansId, queId);//后执行
//		forCallOldAPIService.updUserCredits(null, Credit.ZD_DELETE_ANS, prof.usr_ent_id, prof.usr_id, queId, 0);
	}
	
	
	/**
	 * 回答评价
	 * @param queId 问题id
	 * @param goodInd 是否评价为好
	 * @return
	 */
	@RequestMapping("changeAnsVote/{queId}/{goodInd}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String changeAnsVote(Model model, Page page, loginProfile prof, 
			Params params,WizbiniLoader wizbini,
			@PathVariable(value = "queId") long queId, 
			@PathVariable(value = "goodInd") boolean goodInd){
		boolean evaluationInd = knowVoteDetailService.hasKnowVoteDetail(queId, prof.usr_ent_id);
		if(!evaluationInd){
			Page<Knowanswer> knowanswerPage = knowAnswerService.getKnowAnswerList(page, prof.usr_ent_id, queId, 1,wizbini);
			Knowanswer bestAnswer = knowanswerPage.getResults().get(0);
			knowAnswerService.updateAnsVote(bestAnswer, queId, goodInd, prof.usr_ent_id, prof.usr_id);

			Page<Knowanswer> bestAnswerPage = knowAnswerService.getKnowAnswerList(page, prof.usr_ent_id, queId, 1,wizbini);
			bestAnswer = bestAnswerPage.getResults().get(0);
			model.addAttribute("answerSituation", bestAnswer);
			if(bestAnswer.getAns_vote_total() == 0){
				model.addAttribute("good_rate", "0%");
				model.addAttribute("not_good_rate", "0%");
			} else {
				model.addAttribute("good_rate", knowQuestionService.CountRate(bestAnswer.getAns_vote_for(), bestAnswer.getAns_vote_total()));
				model.addAttribute("not_good_rate", knowQuestionService.CountRate(bestAnswer.getAns_vote_down(), bestAnswer.getAns_vote_total()));
			}
		}
		model.addAttribute("evaluationInd", evaluationInd);
		return JsonFormat.format(params, model);
	}
	
	/**
	 * 通过分类导航返回问答页面
	 * 
	 * @param parent_catalog_id 一级分类id
	 * @param child_catalog_id 二级分类id
	 * @return
	 */
	@RequestMapping(value = "allKnow/{parent_catalog_id}/{child_catalog_id}", method = RequestMethod.GET)
	public ModelAndView returnKnowHomePage(ModelAndView mav, WizbiniLoader wizbini, loginProfile prof, @PathVariable(value = "parent_catalog_id") long parent_catalog_id,
			@PathVariable(value = "child_catalog_id") long child_catalog_id) {
		mav = new ModelAndView("know/allKnow");
		mav.addObject("command", "allKnow");
		
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		knowQuestionService.getKnowSolveTotal(mav, tcrId);
		//获取用户信息
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		
		mav.addObject("parent_catalog_id", parent_catalog_id);
		mav.addObject("child_catalog_id", child_catalog_id);
		return mav;
	}
	
	/**
	 * 问题分类页面
	 * @return
	 */
	@RequestMapping(value = "knowCatalogTree", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView knowCatalogTree(ModelAndView mav, @RequestParam(value = "que_id_lst", defaultValue = "", required = false) String que_id_lst) {
		mav = new ModelAndView("admin/know/knowCatalogTree");
		mav.addObject("que_id_lst", que_id_lst);
		return mav;
	}
	
	/**
	 * 问题分类树
	 * @return
	 */
	@RequestMapping("getknowCatalogTree")
	@ResponseBody
	public String getknowCatalogTree(loginProfile prof, Model model,
			Params params) {
		Long tcr_id = prof.my_top_tc_id;
		if(prof.current_role.equalsIgnoreCase("ADM_1")){
			tcr_id = 1l;
		}
		List<AeTreeNodeVo> list = knowCatalogService.getKnowCatalogTree(tcr_id);
		model.addAttribute(list);
		return JsonFormat.format(params, model);
	}
	
	/**
	 * 改变问题分类
	 * @param que_id_lst 问题id
	 * @param kcaIdOne 一级分类id
	 * @param kcaIdTwo 二级分类id
	 * @return
	 */
	@RequestMapping("changeKnowCatalog/{que_id_lst}/{kcaIdOne}/{kcaIdTwo}")
	@ResponseBody
	public void changeKnowCatalog(loginProfile prof, @PathVariable(value = "que_id_lst") String que_id_lst,
			@PathVariable(value = "kcaIdOne") int kcaIdOne, @PathVariable(value = "kcaIdTwo") int kcaIdTwo) throws Exception {
		if(aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_Q_AND_A_MAIN})){
			for(String que_id : que_id_lst.split("~")){
				knowCatalogService.updateKcaQueCountByQueId(Long.parseLong(que_id), -1, prof.usr_id);
				knowCatalogRelationService.deleteKnowCatalogRelation(Long.parseLong(que_id));
				knowCatalogRelationService.insertKnowCatalogRelation(Long.parseLong(que_id), kcaIdOne, kcaIdTwo, prof.usr_id);
				knowCatalogService.updateKcaQueCountByKcaId(kcaIdOne, 1, prof.usr_id);
				if(kcaIdTwo > 0){
					knowCatalogService.updateKcaQueCountByKcaId(kcaIdTwo, 1, prof.usr_id);
				}
			}
		} else {
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
	}
	
	/**
	 * 通过左边已解决问题或待解决问题按钮返回问答页面
	 * @param type 问题类型
	 * @return
	 */
	@RequestMapping(value = "allKnow/{type}", method = RequestMethod.GET)
	public ModelAndView allKnowType(ModelAndView mav, WizbiniLoader wizbini, loginProfile prof, @PathVariable(value = "type") String type) {
		mav = new ModelAndView("admin/know/allKnow");
		mav.addObject("command", "allKnow");
		
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		knowQuestionService.getKnowSolveTotal(mav, tcrId);
		//获取用户信息
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		mav.addObject("type", type);
		
		return mav;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("main/json")
	@ResponseBody
	public String mainJson(Model model, WizbiniLoader wizbini, loginProfile prof, Page page){
		long tcrId = 0;
		if(wizbini.cfgSysSetupadv.isTcIndependent()){
			tcrId = prof.my_top_tc_id;
		}
		knowQuestionService.getKnowSolveTotal(model, tcrId);
		knowQuestionService.getKnowQuestionList(page, "POPULAR", 0, "", 0, 0, 0, "", tcrId);
		model.addAttribute("popularNum", page.getTotalRecord());
		knowQuestionService.getKnowQuestionList(page, "FAQ", 0, "", 0, 0, 0, "", tcrId);
		model.addAttribute("faqNum", page.getTotalRecord());
		return JsonFormat.format(model, page);
		
	}
	
	/**
	 * 切换语言报错问题处理
	 * @param command 
	 * @return
	 */
	@RequestMapping(value = "addKnowQuestion", method = RequestMethod.GET)
	public String message(){
		return "redirect:/app/admin/know/myKnow";
	}
	
	
	/**
	 * 维护问答管理旧的数据关系
	 * (修复旧数据可能在二级分类下，在关系表中只有一条记录，不能关联到一级分类。)
	 * 限指拥有问答管理权限的用户进行维护
	 * @param prof
	 * @return
	 */
	@HasPermission(value = {AclFunction.FTN_AMD_Q_AND_A_MAIN})
	@RequestMapping("maintenance")
	@ResponseBody
	public String maintenance(loginProfile prof){
         
		String result = "Success...";
		
		List<KnowCatalog>  catalogListOne =  knowCatalogService.getKnowCatalogLlist("CATALOG",0,0,prof,false);
		if(catalogListOne.size() > 0){
			for(int i=0; i < catalogListOne.size(); i++)
			{
				//int kcaIdOne, kcaIdTwo =0;  
				int kcaIdOne = catalogListOne.get(i).getKca_id().intValue();  //一级分类id
				List<KnowCatalog>  catalogListTwo =  knowCatalogService.getKnowCatalogLlist("NORMAL",kcaIdOne,0,prof,false); 
				if(catalogListTwo.size() > 0){ //获取2级分类下 问题内容
					for(int y=0; y < catalogListTwo.size(); y++){
						int kcaIdTwo = catalogListTwo.get(y).getKca_id().intValue();  //二级分类id
						
						try {
							checkInfo(kcaIdOne,kcaIdTwo,catalogListTwo.get(y).getKca_id(),KnowQuestion.TYPE_UNSOLVED,prof);
							checkInfo(kcaIdOne,kcaIdTwo,catalogListTwo.get(y).getKca_id(),KnowQuestion.TYPE_FAQ,prof);
							checkInfo(kcaIdOne,kcaIdTwo,catalogListTwo.get(y).getKca_id(),KnowQuestion.TYPE_POPULAR,prof);
							checkInfo(kcaIdOne,kcaIdTwo,catalogListTwo.get(y).getKca_id(),KnowQuestion.TYPE_SOLVED,prof);
						} catch (Exception e) {
							result = "failure...";
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}
			}
			
		}
		
		return result;
	}
	
	private void checkInfo(int kcaIdOne,int kcaIdTwo,Long kcaId,String typeName,loginProfile prof) throws Exception{
		Page<KnowQuestion> page = new Page<KnowQuestion>();
	    knowQuestionService.getKnowQuestionList(page,typeName,kcaId,"",0,0,0,"",0);
		    
		    List<KnowQuestion> qlistU = page.getResults();
		    for(int s=0;s<qlistU.size();s++){
		    	KnowQuestion question = qlistU.get(s);
					knowQuestionService.saveOrUpdateQuestion(question, kcaIdOne, kcaIdTwo, prof.usr_ent_id, prof.usr_id, prof.usr_display_bil, prof);
		    }
	}
	
	
}
