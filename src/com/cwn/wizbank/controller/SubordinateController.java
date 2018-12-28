package com.cwn.wizbank.controller;

import java.sql.Timestamp;

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
import com.cw.wizbank.config.organization.personalization.Personalization;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cwn.wizbank.services.AeApplicationService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.LearningSituationService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.ReportSpecService;
import com.cwn.wizbank.services.SnsAttentionService;
import com.cwn.wizbank.services.SnsValuationLogService;
import com.cwn.wizbank.services.SuperviseTargetEntityService;
import com.cwn.wizbank.services.UserCreditsService;
import com.cwn.wizbank.services.WebMessageService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

/**
 * 
 * @author lance 我的下属
 */
@Controller
@RequestMapping("subordinate")
public class SubordinateController {
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	SnsAttentionService snsAttentionService;
	
	@Autowired
	UserCreditsService userCreditsService;
	
	@Autowired
	SuperviseTargetEntityService superviseTargetEntityService;
	
	@Autowired
	ReportSpecService reportSpecService;
	
	@Autowired
	AeApplicationService aeApplicationService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@Autowired
	WebMessageService webMessageService;
	
	@Autowired
	SnsValuationLogService snsValuationLogService;
	
	@Autowired
	LearningSituationService learningSituationService;
	
	/**
	 * 我的下属页面
	 * @param command
	 * 				: subordinateList 下属列表
	 * 				: subordinateReport 查看报表
	 * 				: subordinateApproval 审批报名
	 * 				: subordinateMessage 发送消息
	 * @return
	 */
	@RequestMapping(value = "{command}", method = RequestMethod.GET)
	public ModelAndView toPage(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini,
			@PathVariable(value = "command") String command) throws Exception {
		mav = new ModelAndView("subordinate/" + command);
		mav.addObject("command", command);

		//总积分
		mav.addObject("total_credits", userCreditsService.getUserTotalCredits(prof.usr_ent_id, "all"));
		//赞数
		mav.addObject("likes", snsValuationLogService.getUserLikeTotal(prof.usr_ent_id));
		//关注数
		mav.addObject("attent", snsAttentionService.getSnsAttentiontotal(prof.usr_ent_id, "attent"));
		//粉丝数
		mav.addObject("fans", snsAttentionService.getSnsAttentiontotal(prof.usr_ent_id, "fans"));
		//获取用户信息
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id));
		
		mav.addObject("workflowData", forCallOldAPIService.getworkflow(null, prof));
		
		//个人学分
		mav.addObject("learn_credits", learningSituationService.getUserLearnCredits(prof.usr_ent_id));

		return mav;
	}
	
	/**
	 * 下属列表
	 * @param type 类型
	 * @param searchContent 搜索内容
	 * @return
	 */
	@RequestMapping(value = "getSubordinateList/{type}", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getSubordinateList(Model model, loginProfile prof, Page page, @PathVariable(value = "type") String type,
			@RequestParam(value="searchContent", defaultValue="", required=false) String searchContent,
			@RequestParam(value="itmId", defaultValue="0", required=false) String itmId){
		
		if(searchContent != null && !"".equals(searchContent)){
			searchContent = "%" + searchContent.toLowerCase() + "%";
		}
		
		superviseTargetEntityService.getSubordinateList(page, prof.usr_ent_id, type, searchContent, EncryptUtil.cwnDecrypt(itmId));
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 报表列表
	 * @param type 报表类型
	 * @return
	 */
	@RequestMapping(value = "getReportSpecList/{type}", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getReportSpecList(Model model, loginProfile prof, Page page, @PathVariable(value = "type") String type){
		reportSpecService.getReportSpecList(page, prof.usr_ent_id, 0, type);
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 删除报表
	 * @param type 报表类型
	 * @param rsp_id 报表id
	 * @return
	 */
	@RequestMapping(value = "delReportSpec/{type}/{rsp_id}", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String delReportSpec(Model model, loginProfile prof, Page page, 
			Params params,
			@PathVariable(value = "rsp_id") long rsp_id, @PathVariable(value = "type") String type){
		reportSpecService.getReportSpecList(page, prof.usr_ent_id, rsp_id, type);
		if(page.getResults().size() > 0){
			reportSpecService.delReportSpec(rsp_id);
		} else {
			model.addAttribute("error", "subordinate_del_error");
		}
		return JsonFormat.format(params, model);
	}
	
	/**
	 * 审批列表
	 * @param type 审批类型
	 * @return
	 */
	@RequestMapping(value = "getApprovalList/{type}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getSubordinateApprovalDetail(Model model, loginProfile prof, Page page, @PathVariable(value = "type") String type){
		aeApplicationService.getSubordinateApprovalDetail(page, prof.usr_ent_id, type);
		return JsonFormat.format(model,page);
	}
	
	/**
	 * 发送消息
	 * @param usr_ent_id_str 用户id串
	 * @param wmsg_subject 消息标题
	 * @param wmsg_content 消息内容
	 * @return
	 */
	@RequestMapping("sendWebMessage/{usr_ent_id_str}")
	@ResponseBody
	public void sendWebMessage(Model model, loginProfile prof, @PathVariable(value = "usr_ent_id_str") String usr_ent_id_str,
			@RequestParam(value="wmsg_subject", defaultValue="", required=false) String wmsg_subject,
			@RequestParam(value="wmsg_content", defaultValue="", required=false) String wmsg_content){
		webMessageService.addWebMessage(prof.usr_ent_id, usr_ent_id_str, wmsg_subject, wmsg_content, "PERSON");
	}
	
	/**
	 * 报名审批操作
	 * @return
	 */
	@RequestMapping(value = "registrationApproval/{app_id}")
	@ResponseBody
	public void registrationApproval(loginProfile prof, @PathVariable(value = "app_id") long app_id,
			@RequestParam(value="upd_time", defaultValue="", required=false) Timestamp upd_time,
			@RequestParam(value="process_id", defaultValue="", required=false) long process_id,
			@RequestParam(value="fr", defaultValue="", required=false) String fr,
			@RequestParam(value="to", defaultValue="", required=false) String to,
			@RequestParam(value="verb", defaultValue="", required=false) String verb,
			@RequestParam(value="action_id", defaultValue="", required=false) long action_id,
			@RequestParam(value="status_id", defaultValue="", required=false) long status_id) throws Exception{
		//修改成动态获取默认语言
		String work_flow_lan = ((Personalization)qdbAction.wizbini.cfgOrgPersonalization.get(prof.root_id)).getSkinList().getDefaultLang();
		fr = LabelContent.get(work_flow_lan, fr);
		to = LabelContent.get(work_flow_lan, to);
		verb = LabelContent.get(work_flow_lan, verb);
		forCallOldAPIService.doAppnActn(null, prof, app_id, upd_time, process_id, fr, to, verb, action_id, status_id);
	}
	
}
