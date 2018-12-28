package com.cwn.wizbank.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Voting;
import com.cwn.wizbank.entity.VotingResponseResult;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AcRoleFunctionService;
import com.cwn.wizbank.services.LearningSituationService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SnsAttentionService;
import com.cwn.wizbank.services.SnsSettingService;
import com.cwn.wizbank.services.SnsValuationLogService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.services.UserCreditsService;
import com.cwn.wizbank.services.VotingService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * 投票的controller:学员端
 *
 * @author andrew.xiao 2015/6/11 下午5：15
 *
 */
@Controller("votingController")
@RequestMapping("voting")
public class VotingController {

	@Autowired
	private VotingService votingService;

	@Autowired
	private RegUserService regUserService;

	@Autowired
	private TcTrainingCenterService tcTrainingCenterService;

	@Autowired
	private SnsSettingService snsSettingService;

	@Autowired
	private AcRoleFunctionService acRoleFunctionService;

	@Autowired
	private UserCreditsService userCreditsService;

	@Autowired
	private SnsValuationLogService snsValuationLogService;

	@Autowired
	private SnsAttentionService snsAttentionService;

	@Autowired
	AclService aclService;
	
	@Autowired
	LearningSituationService learningSituationService;

	/**
	 * 到投票活动列表首页
	 *
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView page(loginProfile prof, WizbiniLoader wizbini){
		
		ModelAndView mav = new ModelAndView("voting/index");
		setPersonalInfo(mav,prof,prof.usr_ent_id);

		mav.addObject("command", "voting");
		return mav;
	}

	/**
	 * 获取进行中的投票活动json列表
	 *
	 * @param page
	 * @param prof
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("pageJsonForIng")
	@ResponseBody
	public String pageForIng(Page<Voting> page, loginProfile prof, Model model)
			throws ParseException {

		page.getParams().put("type", "ing");
		votingService.pageFront(page, prof.usr_ent_id);

		return JsonFormat.format(model, page);
	}

	/**
	 * 获取已结束的投票活动json列表
	 *
	 * @param page
	 * @param prof
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("pageJsonForEd")
	@ResponseBody
	public String pageForEd(Page<Voting> page, loginProfile prof, Model model)
			throws ParseException {

		page.getParams().put("type", "ed");
		votingService.pageFront(page, prof.usr_ent_id);

		return JsonFormat.format(model, page);
	}

	/**
	 * 去往投票活动页面
	 * @param enc_vot_id 已加密的id
	 * @param prof
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "toVotingPage")
	public ModelAndView toVotingPage(String enc_vot_id, loginProfile prof) {
		long vot_id = EncryptUtil.cwnDecrypt(enc_vot_id);
		ModelAndView mav = new ModelAndView("voting/vote");
		setPersonalInfo(mav, prof, prof.usr_ent_id);
		Voting voting = votingService.get(vot_id);
		mav.addObject("command", "voting");
		mav.addObject("v", voting);
		mav.addObject("enc_vot_id",enc_vot_id);
		return mav;
	}

	/**
	 * 参与投票
	 *
	 * @param voting
	 *            参与的投票活动
	 * @param prof
	 *            登陆用户的信息
	 * @param answers
	 *            答案
	 * @return
	 */
	@RequestMapping(value = "vote")
	public String vote(Voting voting, loginProfile prof, Long[] answers) {
		votingService.vote(voting, prof.usr_ent_id, answers);
		return "redirect:/app/voting";
	}

	/**
	 * 查看投票结果
	 *
	 * @param enc_vot_id 已加密的ID
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, value = "viewResult")
	public ModelAndView viewResult(String enc_vot_id, loginProfile prof,ModelMap map) {
		
		long vot_id = EncryptUtil.cwnDecrypt(enc_vot_id);
		
		ModelAndView mav = new ModelAndView("voting/viewResult");
		setPersonalInfo(mav,prof,prof.usr_ent_id);
		List<VotingResponseResult> result = votingService
				.getVotingResponseResult(vot_id);
		Voting voting = votingService.get(vot_id);
		mav.addObject("command", "voting");
		mav.addObject("result", result)
		.addObject("voting", voting);
		return mav;
	}

	private void setPersonalInfo(ModelAndView mav, loginProfile prof,
			long usr_ent_id) {
		// 是否以培训管理员角色进入
		boolean isTADM = aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_Q_AND_A_MAIN});
		mav.addObject("isTADM", isTADM);
		mav.addObject("encUsrEntId",EncryptUtil.cwnEncrypt(usr_ent_id));
		if (isTADM) {
			mav.addObject("isMeInd", false);
			mav.addObject("regUser",
					regUserService.getUserDetail(prof.usr_ent_id, usr_ent_id));
		} else {

			// 获取权限设置
			mav.addObject("snsSetting", snsSettingService.getSnsSetting(
					prof.usr_ent_id, usr_ent_id));
			mav.addObject("usrEntId", usr_ent_id);
			// 是否是进入本人页面
			mav.addObject("isMeInd", prof.usr_ent_id == usr_ent_id);
			// 总积分
			mav.addObject("total_credits",
					userCreditsService.getUserTotalCredits(usr_ent_id, "all"));
			// 赞数
			mav.addObject("likes",
					snsValuationLogService.getUserLikeTotal(usr_ent_id));
			// 关注数
			mav.addObject("attent", snsAttentionService.getSnsAttentiontotal(
					usr_ent_id, "attent"));
			// 粉丝数
			mav.addObject("fans", snsAttentionService.getSnsAttentiontotal(
					usr_ent_id, "fans"));
			// 获取用户信息
			mav.addObject("regUser",regUserService.getUserDetail(prof.usr_ent_id, usr_ent_id));
			
			//个人学分
		    mav.addObject("learn_credits", learningSituationService.getUserLearnCredits(prof.usr_ent_id));
		}
	}

	/**
	 * 手机端获取投票结果信息
	 * @param enc_vot_id 已加密的id
	 * @param prof
	 * @return
	 */
	@RequestMapping(value="viewResultForMobile")
	@ResponseBody
	public Map<String, Object> viewResultForMobile(String enc_vot_id,loginProfile prof){
		
		long vot_id = EncryptUtil.cwnDecrypt(enc_vot_id);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<VotingResponseResult> responseResult = votingService
				.getVotingResponseResult(vot_id);
		Voting voting = votingService.get(vot_id);
		resultMap.put("voting", voting);
		resultMap.put("responseResult", responseResult);
		return resultMap;
	}

	/**
	 * 
	 * @param enc_vot_id 已加密的id
	 * @param prof
	 * @return
	 */
	@RequestMapping(value="getVoting")
	@ResponseBody
	public Voting getVoting(String enc_vot_id,loginProfile prof){
		long vot_id = EncryptUtil.cwnDecrypt(enc_vot_id);
		Voting voting = votingService.get(vot_id);
		return voting;
	}

	@RequestMapping(value = "voteForMobile")
	@ResponseBody
	public String voteForMobile(Voting voting, loginProfile prof, Long[] answers) {
		String errorCode = votingService.vote(voting, prof.usr_ent_id, answers);
		return errorCode;
	}

}
