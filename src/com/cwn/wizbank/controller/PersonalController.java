package com.cwn.wizbank.controller;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.Application;
import com.cw.wizbank.config.organization.usermanagement.UserManagement;
import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.dbRegUser;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.LearningSituation;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.entity.SitePoster;
import com.cwn.wizbank.entity.SnsSetting;
import com.cwn.wizbank.entity.UserPasswordHistory;
import com.cwn.wizbank.entity.vo.LikeMsgVo;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AcRoleFunctionService;
import com.cwn.wizbank.services.CertificateService;
import com.cwn.wizbank.services.CreditsTypeService;
import com.cwn.wizbank.services.EnterpriseInfoPortalService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.KbItemService;
import com.cwn.wizbank.services.LearningSituationService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.ResourcesService;
import com.cwn.wizbank.services.SitePosterService;
import com.cwn.wizbank.services.SnsAttentionService;
import com.cwn.wizbank.services.SnsCollectService;
import com.cwn.wizbank.services.SnsDoingService;
import com.cwn.wizbank.services.SnsGroupService;
import com.cwn.wizbank.services.SnsSettingService;
import com.cwn.wizbank.services.SnsValuationLogService;
import com.cwn.wizbank.services.SuperviseTargetEntityService;
import com.cwn.wizbank.services.UserCreditsService;
import com.cwn.wizbank.services.WebMessageService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;

/**
 *
 * @author lance 个人中心
 */
@Controller
@RequestMapping("personal")
public class PersonalController {
	@Autowired
	RegUserService regUserService;

	@Autowired
	SnsSettingService snsSettingService;

	@Autowired
	SnsAttentionService snsAttentionService;

	@Autowired
	SnsCollectService snsCollectService;

	@Autowired
	UserCreditsService userCreditsService;

	@Autowired
	SnsDoingService snsDoingService;

	@Autowired
	SnsGroupService snsGroupService;

	@Autowired
	LearningSituationService learningSituationService;

	@Autowired
	SnsValuationLogService snsValuationLogService;

	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	@Autowired
	AcRoleFunctionService acRoleFunctionService;

	@Autowired
	CertificateService certificateService;

	@Autowired
	ResourcesService resourcesService;

	@Autowired
	SuperviseTargetEntityService superviseTargetEntityService;

	@Autowired
	KbItemService kbItemService;

	@Autowired
	AclService aclService;

	@Autowired
	WebMessageService webMessageService;
	
	@Autowired
	CreditsTypeService creditsTypeService;
	
	@Autowired
	SitePosterService sitePosterService;
	
	@Autowired
	EnterpriseInfoPortalService eipService; 
	
	/**
	 * 用户入口
	 * @param prof
	 * @param usrEntId
	 * @param mav
	 * @return
	 * @throws AuthorityException
	 */
	@RequestMapping(value = "{usrEntId}", method = RequestMethod.GET)
	public String userCenter(loginProfile prof, WizbiniLoader wizbini, @PathVariable(value = "usrEntId") long usrEntId, ModelAndView mav) throws AuthorityException{
		SnsSetting setting = snsSettingService.getSnsSetting(prof.usr_ent_id, usrEntId);
		String comment = "";
		//if(usrEntId == 0){
			usrEntId = prof.usr_ent_id;
		//}
		String encUsrEntId = EncryptUtil.cwnEncrypt(usrEntId);
		if(wizbini.cfgSysSetupadv.isSnsEnabled()){
			if(usrEntId == prof.usr_ent_id || setting == null || setting.getS_set_doing() == null || setting.getS_set_doing() == 0 || (setting.getS_set_doing() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "personalDetail";
			} else if(setting.getS_set_group() == null || setting.getS_set_group() == 0 || (setting.getS_set_group() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "personalGroup";
			} else if(setting.getS_set_my_fans() == null || setting.getS_set_my_fans() == 0 || (setting.getS_set_my_fans() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "personalUserList/fans";
			} else if(setting.getS_set_my_follow() == null || setting.getS_set_my_follow() == 0 || (setting.getS_set_my_follow() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "personalUserList/attention";
			} else if(setting.getS_set_like() == null || setting.getS_set_like() == 0 || (setting.getS_set_like() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "personalValuation";
			}
		}
		if(comment.equalsIgnoreCase("")){
			if(usrEntId == prof.usr_ent_id || setting == null || (setting.getSpt_source_usr_ent_id() != null && setting.getSpt_source_usr_ent_id() > 0) || setting.getS_set_my_learning_record() == null || setting.getS_set_my_learning_record() == 0 || (setting.getS_set_my_learning_record() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "personalHistory";
			} else if(setting.getS_set_my_collection() == null || setting.getS_set_my_collection() == 0 || (setting.getS_set_my_collection() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "personalCollect";
			}  else if(setting.getS_set_my_files() == null || setting.getS_set_my_files() == 0 || (setting.getS_set_my_files() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "personalDetail";
			} else if(setting.getS_set_my_credit() == null || setting.getS_set_my_credit() == 0 || (setting.getS_set_my_credit() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "personalCredits";
			} else if((setting.getSpt_source_usr_ent_id() != null && setting.getSpt_source_usr_ent_id() > 0) || setting.getS_set_my_learning_situation() == null || setting.getS_set_my_learning_situation() == 0 || (setting.getS_set_my_learning_situation() == 1 && setting.getSnsAttention() != null && setting.getSnsAttention().getS_att_id() > 0)){
				comment = "learningSituation";
			}
		}
		if(comment==null||comment.equals(""))
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		return "redirect:/app/personal/" + comment + "/" + encUsrEntId;
	}

	/**
	 * 个人中心页面
	 * @param command
	 * 				: personalPrivacySet 隐私设置
	 * 				: personalDoing 动态页面
	 * 				: personalCredits 积分页面
	 * 				: personalDetail 档案信息页面
	 * 				: personalCollect 收藏页面
	 * 				: personalGroup 群组页面
	 * 				: personalCertificate 证书页面
	 * 				: personalHistory 学习记录
	 * 				: learningSituation 学习概况
	 * @return
	 */
	@RequestMapping(value = "{command}/{encUsrEntId}", method = RequestMethod.GET)
	public ModelAndView toPage(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini,
			@PathVariable(value = "command") String command,
			@PathVariable(value = "encUsrEntId") String encUsrEntId,HttpServletRequest request) {
		
		long usrEntId = EncryptUtil.cwnDecrypt(encUsrEntId);
		usrEntId = prof.usr_ent_id;
		
		mav = new ModelAndView("personal/" + command);

		//是否以培训管理员角色进入
		boolean isTADM = aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_Q_AND_A_MAIN});
		mav.addObject("isTADM", isTADM);
		if(isTADM){
			mav.addObject("isMeInd", false);
			mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, usrEntId));
		} else {
			mav.addObject("command", command);

			//获取权限设置
			mav.addObject("snsSetting", snsSettingService.getSnsSetting(prof.usr_ent_id, usrEntId));

			getPersonalDetail(mav, prof, usrEntId);

			if(command.equalsIgnoreCase("personalCredits")){
				//活动积分
				mav.addObject("activity_credits", userCreditsService.getUserTotalCredits(usrEntId, "activity"));
				//培训积分
				mav.addObject("train_credits", userCreditsService.getUserTotalCredits(usrEntId, "train"));
			} else if(command.equalsIgnoreCase("learningSituation")){
				//个人学习概况
				long tcrId = 0;
				if(wizbini.cfgSysSetupadv.isTcIndependent()){
					tcrId = prof.my_top_tc_id;
				}
				Page<LearningSituation> page = new Page<LearningSituation>();
				page.getParams().put("tcrId", tcrId);
				page.getParams().put("usr_ent_id", usrEntId);
				page.getParams().put("learningSituation", true);
				LearningSituation ls = learningSituationService.getLearningRankDetail(page);
				if(ls != null){
					long lds = ls.getLs_learn_duration()%60;
					long ldm = ls.getLs_learn_duration()/60%60;
					long ldh = ls.getLs_learn_duration()/60/60%24;
					long ldd = ls.getLs_learn_duration()/60/60/24;
					DecimalFormat df = new DecimalFormat("00");
					ls.setLs_learn_duration_str(ldd +" "+ LabelContent.get(prof.cur_lan, "time_day") + " " + df.format(ldh) + ":" + df.format(ldm) + ":" + df.format(lds));
					Calendar c = Calendar.getInstance(); 
					c.add(Calendar.DAY_OF_WEEK, -1); // 目前的時間减去1天    
					ls.setLs_update_time(c.getTime());
				}
				mav.addObject("learningSituation", ls);
			} else if(command.equalsIgnoreCase("personalDetail")){
				mav.addObject("mySupervise", superviseTargetEntityService.getMySupervise(usrEntId));
			}
			
		}
		
		return mav;
	}

	/**
	 * 修改个人档案
	 * @param regUser
	 * @param birthday 出生日期
	 * @param join_datetime 加入公司日期
	 * @param image 个人头像
	 * @return
	 */
	@RequestMapping(value = "personalDetail/update/userDetail", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView updateUserDetail(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini, @Param(value="regUser") RegUser regUser,
			@Param(value="birthday") String birthday, @Param(value="join_datetime") String join_datetime,
			@RequestParam(value="image", required = false) MultipartFile image) throws Exception {
		regUserService.updateUserDetail(wizbini, regUser, prof.usr_ent_id, birthday, join_datetime, image);
		forCallOldAPIService.updUserCredits(null, Credit.SYS_UPD_MY_PROFILE, prof.usr_ent_id, prof.usr_id, 0, 0);
		mav = new ModelAndView("personal/personalDetail");
		RegUser usr = regUserService.getUserDetail(prof.usr_ent_id, prof.usr_ent_id);
		prof.setUsr_photo(usr.getUsr_photo());
		mav.addObject("regUser", usr);
		mav.addObject("command", "personalDetail");
		mav.addObject("message", "update_ok");
		//是否以培训管理员角色进入
		mav.addObject("isTADM", false);
		getPersonalDetail(mav, prof, prof.usr_ent_id);
		 return new ModelAndView("redirect:/app/personal/personalDetail/"+prof.usr_ent_id);
	}

	/**
	 * 修改个人档案(移动端使用)
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "personalDetail/update/userDetailMobile")
	@ResponseBody
	public String updateUserDetail(Model model, loginProfile prof, WizbiniLoader wizbini, Page page, @RequestParam(value="file", required = false) MultipartFile image) throws Exception{
		if(page.getParams().containsKey("json")){
			String jsonStr = (String) page.getParams().get("json");
			JSONObject jsons = JSONObject.fromObject(jsonStr);
			RegUser regUser=(RegUser) JSONObject.toBean(jsons, RegUser.class);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(page.getParams().get("joinTime") != null && !page.getParams().get("joinTime").equals("")){
				regUser.setUsr_join_datetime(sdf.parse((String) page.getParams().get("joinTime")));
			}
			if(page.getParams().get("birthday") != null && !page.getParams().get("birthday").equals("")){
				regUser.setUsr_bday(sdf.parse((String) page.getParams().get("birthday")));
			}
			regUserService.updateUserDetail(wizbini, regUser, prof.usr_ent_id, image);
			model.addAttribute("status", "success");
		}
		return JsonFormat.format(model, page);
	}
	
	/**
	 * 隐私设置
	 * @param snsSetting
	 * @return
	 */
	@RequestMapping(value = "personalPrivacySet/update/changePrivacySet", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView changePrivacySet(ModelAndView mav, loginProfile prof, @Param(value="snsSetting") SnsSetting snsSetting) throws Exception {
		mav = new ModelAndView("personal/personalPrivacySet");
		snsSettingService.setMyPrivacySet(prof.usr_ent_id, snsSetting);
		mav.addObject("snsSetting", snsSetting);
		mav.addObject("result_msg", "update_ok");
		getPersonalDetail(mav, prof, prof.usr_ent_id);
		return mav;
	}
	@RequestMapping("personalPrivacySet/getSnsSetting/{usrEntId}")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String snsSettingMsg(Model model, loginProfile prof, WizbiniLoader wizbini,Page page,
			@PathVariable(value = "usrEntId") long usrEntId){
		model.addAttribute("snsSetting", snsSettingService.getSnsSetting(prof.usr_ent_id, usrEntId));
		return JsonFormat.format(model, page);
	}

	@RequestMapping("personalPrivacySet/changePrivacySetMobile")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String changePrivacySetMobile(Model model, loginProfile prof, Page page) throws Exception {
		snsSettingService.setMyPrivacySet(prof.usr_ent_id, page);
		model.addAttribute("message", "update_ok");
		return JsonFormat.format(model, page);
	}
	/**
	 * 用户列表
	 * @param type 	attention : 关注; fans : 粉丝; find : 找人
	 * @param encUsrEntId 用户id // 加密过的
	 * @return
	 */
	@RequestMapping(value = "personalUserList/{type}/{encUsrEntId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getUserList(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini,
			@PathVariable(value = "type") String type, @PathVariable(value = "encUsrEntId") String encUsrEntId) {
		
		long usrEntId;
		if("0".equals(encUsrEntId)){
			usrEntId = prof.usr_ent_id;
		}else{
			usrEntId = EncryptUtil.cwnDecrypt(encUsrEntId);
		}
		
		mav = new ModelAndView("personal/personalUserList");
		mav.addObject("command", type);
		mav.addObject("snsSetting", snsSettingService.getSnsSetting(prof.usr_ent_id, usrEntId));
		getPersonalDetail(mav, prof, usrEntId);
		return mav;
	}

	/**
	 * 获取用户列表信息
	 * @param type {attention : 关注; fans : 粉丝; find : 找人}
	 * @param encUsrEntId 用户id
	 * @param searchContent 搜索内容
	 * @return
	 */
	@RequestMapping(value = "getUserList/{type}/{encUsrEntId}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getUserList(Model model, Page page, WizbiniLoader wizbini, loginProfile prof, @PathVariable(value = "type") String type,
			@PathVariable(value = "encUsrEntId") String encUsrEntId, @RequestParam(value="searchContent", defaultValue="", required = false) String searchContent,
			@RequestParam(value="notEqualGroup", defaultValue="false", required = false) boolean notEqualGroup) {
		long usrEntId = EncryptUtil.cwnDecrypt(encUsrEntId);
		
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			page.getParams().put("tcr_id", prof.my_top_tc_id);
		}
		
		String groupId = "";
		if(!notEqualGroup && prof.usrGroups.size() > 0){
			groupId = prof.usrGroups.get(prof.usrGroups.size()-1).toString();
		}
		
		snsAttentionService.getUserList(page, type, usrEntId, prof.my_top_tc_id, prof.usr_ent_id, searchContent, groupId);
		return JsonFormat.format(model, page);
	}

	/**
	 * 新增关注
	 * @param s_att_target_uid 被关注用户id
	 * @return
	 */
	@RequestMapping(value = "addAttention/{s_att_target_uid}")
	@ResponseBody
	public void addAttention(loginProfile prof, @PathVariable(value = "s_att_target_uid") String s_att_target_uid) {
	    //解密
		long uid = EncryptUtil.cwnDecrypt(s_att_target_uid);
		snsAttentionService.addAttention(prof.usr_ent_id, uid);
	}

	/**
	 * 取消关注
	 * @param s_att_target_uid 被关注用户id
	 * @return
	 */
	@RequestMapping(value = "cancelAttention/{s_att_target_uid}")
	@ResponseBody
	public void cancelAttention(loginProfile prof, @PathVariable(value = "s_att_target_uid") String s_att_target_uid) {
		//解密
		long uid = EncryptUtil.cwnDecrypt(s_att_target_uid);
		snsAttentionService.cancelAttention(prof.usr_ent_id, uid);
	}

	/**
	 * 获取个人收藏列表
	 * @param s_clt_module Course ： 课程      Article : 文章
	 * @param encUsrEntId 用户id
	 * @return
	 */
	@RequestMapping(value = "getCollectList/{s_clt_module}/{encUsrEntId}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getCollectList(Model model, Page page, @PathVariable(value = "encUsrEntId") String encUsrEntId,  @PathVariable(value = "s_clt_module") String s_clt_module,@RequestParam(value = "isPC",defaultValue="false") boolean isPC) {
		long usr_ent_id = EncryptUtil.cwnDecrypt(encUsrEntId);
		if(s_clt_module.equalsIgnoreCase("Course")){
			String sql =  " and itm.itm_ref_ind = 0";
			if(isPC == true){
				sql =  " and (itm.itm_ref_ind = 0 or itm.itm_ref_ind = 1) ";
			}
			snsCollectService.getCollectItemList(page, usr_ent_id, s_clt_module,sql);
		} else if(s_clt_module.equalsIgnoreCase("Knowledge")){
			snsCollectService.getCollectKnowledgeList(page, usr_ent_id, s_clt_module);
		} else if(s_clt_module.equalsIgnoreCase("OpenCourse")){
			snsCollectService.getCollectItemList(page, usr_ent_id, "Course"," and itm.itm_ref_ind = 1");
		} else {
			snsCollectService.getCollectArticleList(page, usr_ent_id, s_clt_module);
		}
		model.addAttribute("s_clt_module", s_clt_module);
		return JsonFormat.format(model, page);
	}
	@RequestMapping(value = "getLikeList/{encUsrEntId}")
	@ResponseBody
	public String LikeList(Model model, Page<LikeMsgVo> page, WizbiniLoader wizbini, loginProfile prof,
			@PathVariable(value="encUsrEntId") String encUsrEntId) {
		long usrEntId = EncryptUtil.cwnDecrypt(encUsrEntId);
		snsValuationLogService.getLikeList(page, wizbini, usrEntId, prof.cur_lan);
		return JsonFormat.format(model, page);
	}
	/**
	 * 取消收藏
	 * @param s_clt_target_id 所收藏东西id 已加密的
	 * @param s_clt_module Course ： 课程
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "cancelCollect/{s_clt_target_id}/{s_clt_module}")
	@ResponseBody
	public void cancelCollect(Model model,loginProfile prof, @PathVariable(value = "s_clt_module") String s_clt_module, @PathVariable(value = "s_clt_target_id") String s_clt_target_id) throws Exception {
		snsCollectService.cancel(model, EncryptUtil.cwnDecrypt(s_clt_target_id), s_clt_module, prof.usr_ent_id);
	}

	/**
	 * 获取个人积分详情
	 * @param usr_ent_id 用户id
	 * @return
	 */
	@RequestMapping(value = "getCreditDetail/{encUsrEntId}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getCreditDetail(Model model, Page page, @PathVariable(value = "encUsrEntId") String encUsrEntId) {
		long usrEntId = EncryptUtil.cwnDecrypt(encUsrEntId);
		userCreditsService.getUserCreditsDetailList(page, usrEntId);
		return JsonFormat.format(model, page);
	}

	/**
	 * 获取个人动态列表
	 * @param type
	 * @param usr_ent_id 用户id
	 * @return
	 */
	@RequestMapping(value = "getDoingList/{type}/{usr_ent_id}", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getDoingList(Model model,
			loginProfile prof,
			Page page, @PathVariable(value = "usr_ent_id") long usr_ent_id,  @PathVariable(value = "type") String type) {
		//snsDoingService.getPersonalDoingList(page, usr_ent_id, type, prof.cur_lan);
		return JsonFormat.format(model, page);
	}

	/**
	 * 获取个人群组列表
	 * @param encUsrEntId 用户id
	 * @return
	 */
	@RequestMapping("getPersonalGroupList/{encUsrEntId}")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String getPersonalGroupList(Model model, Page page, loginProfile prof, @PathVariable(value = "encUsrEntId") String encUsrEntId) {
		long usr_ent_id = EncryptUtil.cwnDecrypt(encUsrEntId);
		snsGroupService.getPersonalGroupList(page, usr_ent_id, prof.usr_ent_id == usr_ent_id, prof.my_top_tc_id);
		return JsonFormat.format(model, page);
	}

	/**
	 * 证书列表
	 *
	 * @return
	 */
	@RequestMapping(value = "certificateList", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String certificateList(Model model, Page page, loginProfile prof){
		certificateService.getCertificateList(page, prof.usr_ent_id);
		return JsonFormat.format(model, page);
	}

	/**
	 * 调查问卷列表
	 *
	 * @return
	 */
	@RequestMapping(value = "evaluationList")
	@ResponseBody
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String evaluationList(Model model, Page page,
			@RequestParam(value = "status", defaultValue = "0") int status,
			loginProfile prof){
		resourcesService.getMyEvaluation(prof.usr_ent_id, status, page);
		return JsonFormat.format(model, page);
	}

	/**
	 * 切换语言报错问题处理
	 * @param command
	 * @return
	 */
	@RequestMapping(value = "{command}/update/{operation}", method = RequestMethod.GET)
	public String message(loginProfile prof, @PathVariable(value = "command") String command){
		return "redirect:/app/personal/" + command + "/" + prof.usr_ent_id;
	}

	public ModelAndView getPersonalDetail(ModelAndView mav, loginProfile prof, long usr_ent_id){
		mav.addObject("usrEntId", usr_ent_id);
		mav.addObject("encUsrEntId", EncryptUtil.cwnEncrypt(usr_ent_id));
		//是否是进入本人页面
		mav.addObject("isMeInd", prof.usr_ent_id == usr_ent_id);
		//总积分
		mav.addObject("total_credits", userCreditsService.getUserTotalCredits(usr_ent_id, "all"));
		//赞数
		mav.addObject("likes", snsValuationLogService.getUserLikeTotal(usr_ent_id));
		//关注数
		mav.addObject("attent", snsAttentionService.getSnsAttentiontotal(usr_ent_id, "attent"));
		//粉丝数
		mav.addObject("fans", snsAttentionService.getSnsAttentiontotal(usr_ent_id, "fans"));
		//获取用户信息
		mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, usr_ent_id));
		//个人学分
		mav.addObject("learn_credits", learningSituationService.getUserLearnCredits(prof.usr_ent_id));
		return mav;
	}

	@RequestMapping("home/json/{encUsrEntId}")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String homePageMobile(Model model, Page page, loginProfile prof, WizbiniLoader wizbini, @PathVariable(value = "encUsrEntId") String encUsrEntId) throws Exception{
		long usrEntId = EncryptUtil.cwnDecrypt(encUsrEntId);
		model.addAttribute("snsSetting", snsSettingService.getSnsSetting(prof.usr_ent_id, usrEntId));
		getPersonalDetail(model, prof, usrEntId);
		return JsonFormat.format(model, page);
	}
	@RequestMapping("learningSituation/json/{encUsrEntId}")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String learningSituation(Model model, Page page, loginProfile prof, WizbiniLoader wizbini, @PathVariable(value = "encUsrEntId") String encUsrEntId) throws Exception{
		long usrEntId = EncryptUtil.cwnDecrypt(encUsrEntId);
		getPersonalDetail(model,prof,usrEntId);
		LearningSituation ls = null;
		if(page.getParams().get("isMobile")!=null && page.getParams().get("isMobile").equals("true")){
			//移动端修改成获取实时的数据
			ls = learningSituationService.getDynamicLearningSituation(usrEntId);
			//由于个人学习概况线程更新的总积分数据是从userCredits表查询的，而该表也是通过线程更新的，故总积分需要额外实时查询
			ls.setLs_total_integral((long)userCreditsService.getUserTotalCredits(usrEntId, "all"));
		} else {	
			ls = learningSituationService.getLearningSituation(usrEntId);
		}

		if(ls != null){
			long lds = ls.getLs_learn_duration()%60;
			long ldm = ls.getLs_learn_duration()/60%60;
			long ldh = ls.getLs_learn_duration()/60/60%24;
			long ldd = ls.getLs_learn_duration()/60/60/24;
			DecimalFormat df = new DecimalFormat("00");
			ls.setLs_learn_duration_str(ldd +" "+ LabelContent.get(prof.cur_lan, "time_day") + " " + df.format(ldh) + ":" + df.format(ldm) + ":" + df.format(lds));
			if(page.getParams().get("isMobile")!=null && page.getParams().get("isMobile").equals("true")){
				ls.setLs_learn_duration_html("<span class=\"panel-num-4\">"+ldd +"</span>"+ LabelContent.get(prof.cur_lan, "time_day") + " <span class=\"panel-num-6\">" + df.format(ldh) + ":" + df.format(ldm) + ":" + df.format(lds) + "</span>");
				Calendar c = Calendar.getInstance(); 
				c.add(Calendar.DAY_OF_WEEK, -1); // 目前的時間减去1天    
				ls.setLs_update_time(c.getTime());
			}
		}
		model.addAttribute("learningSituation", ls);
		return JsonFormat.format(model, page);
	}



	public Model getPersonalDetail(Model model, loginProfile prof, long usr_ent_id) throws Exception{
		model.addAttribute("usrEntId", usr_ent_id);
		//是否是进入本人页面
		model.addAttribute("isMeInd", prof.usr_ent_id == usr_ent_id);
		//总积分
		model.addAttribute("total_credits", userCreditsService.getUserTotalCredits(usr_ent_id, "all"));
		//活动积分
		model.addAttribute("activity_credits", userCreditsService.getUserTotalCredits(usr_ent_id, "activity"));
		//培训积分
		model.addAttribute("train_credits", userCreditsService.getUserTotalCredits(usr_ent_id, "train"));
		//赞数
		model.addAttribute("likes", snsValuationLogService.getUserLikeTotal(usr_ent_id));
		//关注数
		model.addAttribute("attent", snsAttentionService.getSnsAttentiontotal(usr_ent_id, "attent"));
		//粉丝数
		model.addAttribute("fans", snsAttentionService.getSnsAttentiontotal(usr_ent_id, "fans"));
		//获取用户信息
		model.addAttribute("regUser", regUserService.getUserDetail(prof.usr_ent_id, usr_ent_id));

		model.addAttribute("mySupervise", superviseTargetEntityService.getMySupervise(usr_ent_id));
		
		//用户未读站内信
		model.addAttribute("messageCount", webMessageService.getUserNotReadCount(usr_ent_id));
		
		//是否有下属
		model.addAttribute("hasStaff", forCallOldAPIService.hasStaff(null, prof));

		return model;
	}

	@RequestMapping("credits/json")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String credits(Model model, Page page, loginProfile prof, WizbiniLoader wizbini ){
		//总积分
		model.addAttribute("total_credits", userCreditsService.getUserTotalCredits(prof.usr_ent_id, "all"));
		//活动积分
		model.addAttribute("activity_credits", userCreditsService.getUserTotalCredits(prof.usr_ent_id, "activity"));
		//培训积分
		model.addAttribute("train_credits", userCreditsService.getUserTotalCredits(prof.usr_ent_id, "train"));
		return JsonFormat.format(model, page);
	}

	/**
	 * 获取用户学习足迹
	 * @param model
	 * @param prof
	 * @param wizbini
	 * @return
	 */
	@RequestMapping("learningHistory/json")
	@ResponseBody
	public String StringlearningSituation(Model model,loginProfile prof, WizbiniLoader wizbini){
		model.addAttribute("learningHistoryJson", learningSituationService.getlearningHistoryForMobile(prof.usr_ent_id));
		return JsonFormat.format(model);
	}
	
	/**
	 * 获取用户总积分
	 * @param model
	 * @param prof
	 * @param eip_code 企业标识---暂时没有用，为了以后拓展
	 * @return
	 */
	@RequestMapping(value = "credits",method = RequestMethod.POST)
	@ResponseBody
	public String totalCredits(Model model,loginProfile prof,@RequestParam(value = "eip_code", required = false) String eip_code){
		try{
			//总积分
			model.addAttribute("total_credits", userCreditsService.getUserTotalCredits(prof.usr_ent_id, "all"));
			model.addAttribute("status", "success");
			model.addAttribute("code", "200");
		}catch(Exception e){
			CommonLog.error(e.getMessage(),e);
			model.addAttribute("status", "error");
			model.addAttribute("msg", e.getMessage());
			model.addAttribute("code", "500");
		}
		return JsonFormat.format(model);
	}
	
	/**
	 * 消除积分/新增积分
	 * @param model
	 * @param prof
	 * @param consumeCredits 削减的积分
	 * @param eip_code 企业标识---暂时没有用，为了以后拓展
	 * @return
	 */
	@RequestMapping(value = "credits/updateCredits", method = RequestMethod.POST)
	@ResponseBody
	public String updateCredits(Model model,loginProfile prof, @RequestParam(value = "consumeCredits", required = false) long consumeCredits,
			@RequestParam(value = "eip_code", required = false) String eip_code){
		
		try {	
			Pattern pattern = Pattern.compile("[0-9]+");
            if( !pattern.matcher(Long.toString(consumeCredits)).matches()){
            	model.addAttribute("status", "fail");
            	model.addAttribute("msg", "consumeCredits参数有误!请检查是否是一个正整数。");
            	model.addAttribute("code", "0");
            }else{
            	
            	double credits =  userCreditsService.getUserTotalCredits(prof.usr_ent_id, "all");
            	if(credits < consumeCredits){
            		model.addAttribute("status", "fail");
                	model.addAttribute("msg", "所消费的积分大于该用户的总积分数！");
                	model.addAttribute("code", "1");
            	}else{
            		
            		if(!creditsTypeService.hasCreditsTypeByCode(Credit.API_UPDATE_CREDITS)){
            			model.addAttribute("status", "fail");
                    	model.addAttribute("msg", "系统不存在该消费积分类型，请联系相关技术人员。");
                    	model.addAttribute("code", "2");
            		}else{
            			forCallOldAPIService.updUserCredits(null, Credit.API_UPDATE_CREDITS, prof.usr_ent_id, prof.usr_id, 0, consumeCredits, 0);
            			model.addAttribute("status", "success");
            			model.addAttribute("msg", "消费成功！");
            			model.addAttribute("code", "200");
            		}
            	}
            }
			
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			model.addAttribute("status", "error");
			model.addAttribute("msg", e.getMessage());
			model.addAttribute("code", "500");
		}
		return JsonFormat.format(model);
	}
	
	/**
	 * 修改密码页面
	 * @param fromlogin true表示是用户登录之后，被强制引导到密码修改页面的，false表示用户自己点击个人主页密码按钮进入密码修改页面的
	 * @param prof
	 * @param wizbini
	 * @return
	 */
	@RequestMapping(value = "passwordModify", method = RequestMethod.GET)
	public ModelAndView passwordModify(@RequestParam(value = "fromlogin", required = false) boolean fromlogin,loginProfile prof,WizbiniLoader wizbini,HttpServletRequest request){
		
		ModelAndView mav = new ModelAndView("personal/passwordModify");
		
		//获取密码策略的相关信息
		mav.addAllObjects(getPasswordPolicyInfoModel(prof,wizbini));
		
		//修改完成需要跳转的页面
		mav.addObject("userIndexUrl",prof.goHome(request));
		
		//是否隐藏页面header，如果fromlogin为true，则隐藏
		mav.addObject("hideHeaderInd", fromlogin);
		
		if(!fromlogin){//如果是用户手动去到密码修改页面的，则需要显示侧边栏和用户信息，所以需要获取用户信息
			getPersonalDetail(mav, prof, prof.usr_ent_id);
		}else{//页面只要logo即可
			List<SitePoster> list = null;
			//如果是ln模式，并且用户所在二级培训中心已经跟企业关联
			if (wizbini.cfgSysSetupadv.isTcIndependent() && eipService.checkTcrOccupancy(prof.my_top_tc_id)) {
				list = sitePosterService.getPoster(prof.my_top_tc_id, 0);
			}
			
			if(list==null || list.size()==0){
				list = sitePosterService.getPoster(prof.root_ent_id, 0);
			}
			
			mav.addObject("logo", list.get(0));
		}
		
		//添加到数据模型，页面需要根据这个参数进行显示
		mav.addObject("fromlogin", fromlogin);
		
		return mav;
	}
	
	/**
	 * 处理更新用户密码
	 * @param old_password 旧密码
	 * @param new_password 新密码
	 * @param prof
	 * @param model
	 * @param wizbini
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "updatePassword")
	@ResponseBody
	public String updatePassword(@Param(value="old_password") String old_password, @Param(value="new_password") String new_password, @Param(value="developer")String developer,loginProfile prof, Model model, WizbiniLoader wizbini ,HttpServletRequest request) throws Exception{
		
		if(StringUtils.isEmpty(developer)){
			developer = UserPasswordHistory.UPH_CLIENT_TYPE_PC;
		}
		String result = regUserService.updateUserPwd(prof.usr_ent_id, old_password, new_password, prof.usr_ste_usr_id ,developer);
		
		model.addAttribute("result", result);
		
		/*
		 * 以下是提示客户端信息
		*/
		if(result.equalsIgnoreCase("update_ok")){
			
			prof.bNeedToChangePwd = false;
			
			//修改成功需要跳转的页面，只针对PC端
			model.addAttribute("success_url", prof.goHome(request));
			String label = LabelContent.get(prof.cur_lan, "login_update_pwd_ok");
			//修改之后提示信息
			model.addAttribute("message", label);
			//记录重要功能日志——修改密码
			ObjectActionLog log = new ObjectActionLog(prof.getUsr_ent_id(), 
					prof.usr_ste_usr_id,
					prof.usr_display_bil,
					ObjectActionLog.OBJECT_TYPE_USR,
					ObjectActionLog.OBJECT_ACTION_UPD_PWD,
					ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
					prof.getUsr_ent_id(),
					prof.getUsr_last_login_date(),
					prof.getIp()
			);
			SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		} else if(result.equalsIgnoreCase("usr_password_has_existed")){
			String label = LabelContent.get(prof.cur_lan, "label_ss.label_core_system_setting_158");
			label = label.replace("{{N}}", Application.PASSWORD_POLICY_COMPARE_COUNT);
			model.addAttribute("message", label);
		}else if(result.equalsIgnoreCase("usr_old_password_error")){
			String label = LabelContent.get(prof.cur_lan, "usr_old_password_error");
			model.addAttribute("message", label);
		}else if(result.equalsIgnoreCase("old_and_new_similar")){
			String label = LabelContent.get(prof.cur_lan, "label_cm.label_core_community_management_213");
			model.addAttribute("message", label);
		}
		return JsonFormat.format(model);
	}
	
	/**
	 * 获取密码策略的相关信息Model
	 * @param prof
	 * @param wizbini
	 * @return
	 */
	private ModelMap getPasswordPolicyInfoModel(loginProfile prof,WizbiniLoader wizbini){
		ModelMap m = new ModelMap();
		
		//密码最大长度
		int maxLength=((UserManagement)wizbini.cfgOrgUserManagement.get(prof.root_id)).getUserProfile().getProfileAttributes().getPassword().getMaxLength();
		//密码最小长度
		int minLength=((UserManagement)wizbini.cfgOrgUserManagement.get(prof.root_id)).getUserProfile().getProfileAttributes().getPassword().getMinLength();
		m.addAttribute("maxLength", maxLength);
		m.addAttribute("minLength", minLength);
		
		//是否允许强制修改
		boolean flag = Application.PASSWORD_POLICY_PERIOD_FORCE!=null && "1".equals(Application.PASSWORD_POLICY_PERIOD_FORCE);
		//管理员强制 用户必须在下一次登录时修改密码
		if(forCallOldAPIService.isNeedChangePwd(null, prof.usr_ste_usr_id) && !flag) {
			flag = true;
		}
		m.addAttribute("forceChange", flag);
		
		//是否允许强制修改 
		m.addAttribute("forceChange", prof.bNeedToChangePwd && (Application.PASSWORD_POLICY_PERIOD_FORCE!=null && "1".equals(Application.PASSWORD_POLICY_PERIOD_FORCE)));
		//和历史密码比对的次数
		m.addAttribute("policyCompareCount", Application.PASSWORD_POLICY_COMPARE_COUNT);
		
		Timestamp usrPwdUpdTimestamp = regUserService.usrPwdUpdTimestamp(prof.usr_ent_id);
		if(usrPwdUpdTimestamp != null){
			/*
			 * 如果用户超过了设置的天数没改密码，则页面需要提示用户
			 */
			long dTime = System.currentTimeMillis() - usrPwdUpdTimestamp.getTime();
			if(!StringUtils.isEmpty(Application.PASSWORD_POLICY_PERIOD) && dTime > Long.parseLong(Application.PASSWORD_POLICY_PERIOD) * 24 * 60 * 60 * 1000){//如果需要修改密码，则页面需要提示
				long days = dTime/1000/60/60/24;
				m.addAttribute("notChangePwdDays", days);
			}
		}
		
		return m;
	}
	
	/**
	 * 
	 * @return 获取密码策略的相关信息
	 */
	@RequestMapping(value = "passwordPolicyInfo")
	@ResponseBody
	public String passwordPolicyInfo(loginProfile prof,WizbiniLoader wizbini){
		return JsonFormat.format(getPasswordPolicyInfoModel(prof,wizbini));
	}

	/**
	 * 用户是否需要修改密码
	 * @param prof
	 * @return
	 */
	@RequestMapping(value = "pwdNeedChangeInd")
	@ResponseBody
	public boolean pwdNeedChangeInd(loginProfile prof){
		Timestamp usrPwdUpdTimestamp = regUserService.usrPwdUpdTimestamp(prof.usr_ent_id);
		long dTime = System.currentTimeMillis() - usrPwdUpdTimestamp.getTime();
		if(prof.bNeedToChangePwd){
			return true;
		}
		if(!StringUtils.isEmpty(Application.PASSWORD_POLICY_PERIOD) && !"0".equals(Application.PASSWORD_POLICY_PERIOD) && dTime > Long.parseLong(Application.PASSWORD_POLICY_PERIOD) * 24 * 60 * 60 * 1000){
			return true;
		}
		
		//管理员强制 用户必须在下一次登录时修改密码
		if(forCallOldAPIService.isNeedChangePwd(null, prof.usr_ste_usr_id)) {
			return true;
		}
		
		return false;
	}
	
}
