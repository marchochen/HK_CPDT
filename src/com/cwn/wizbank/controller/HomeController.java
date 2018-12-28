package com.cwn.wizbank.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.SessionListener;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbAction;
import com.cw.wizbank.util.cwException;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.ItemTargetLrnDetail;
import com.cwn.wizbank.entity.SitePoster;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.AeApplicationMapper;
import com.cwn.wizbank.security.annotation.Anonymous;
import com.cwn.wizbank.services.AeApplicationService;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.EnterpriseInfoPortalService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.HomeService;
import com.cwn.wizbank.services.LiveItemService;
import com.cwn.wizbank.services.LoginService;
import com.cwn.wizbank.services.MessageService;
import com.cwn.wizbank.services.ProfessionService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.ResourcesService;
import com.cwn.wizbank.services.SitePosterService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.services.UserCreditsService;
import com.cwn.wizbank.services.UserPositionLrnMapService;
import com.cwn.wizbank.services.UserSpecialTopicService;
import com.cwn.wizbank.services.VotingService;
import com.cwn.wizbank.services.WebMessageService;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;

/**
 * 
 * @author leon.li
 * 2014-7-24 下午6:11:34
 */
@Controller
@RequestMapping("home")
//@HasPermission(AclFunction.FTN_LRN_HOME_VIEW)
public class HomeController {
	
	@Autowired
	HomeService homeService;
	
	@Autowired
	RegUserService regUserService;
	
	@Autowired
	UserCreditsService userCreditsService;
	
	@Autowired
	AeItemService aeItemService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	WebMessageService webMessageService;
	
	@Autowired
	AeApplicationService aeApplicationService;
	
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	@Autowired
	ResourcesService resourcesService;
	
	@Autowired
	SitePosterService sitePosterService;
	
	@Autowired
	AeApplicationMapper aeApplicationMapper;
	
	@Autowired
	VotingService votingService;
	
	@Autowired
	UserPositionLrnMapService userPositionLrnMapService;
	@Autowired
	UserSpecialTopicService specialTopicService; 
	@Autowired
	ProfessionService professionService;
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@Autowired 
	LiveItemService liveService;
	
	@Autowired
	EnterpriseInfoPortalService eipService;
	
	@Autowired
	LoginService loginService;
	
	@RequestMapping("")
	public ModelAndView home(ModelAndView mav, HttpServletRequest request,HttpServletResponse response, HttpSession session, loginProfile prof, WizbiniLoader wizbini,Model model) throws SQLException ,IOException{
		mav = new ModelAndView("home/index");
		if(prof!= null && !prof.isLrnRole){
			String home_url = prof.goHome(request);
			response.sendRedirect(home_url);
			return null;
		}
		
		long tcr_id = prof.root_ent_id;
		//如果是ln模式，并且用户所在二级培训中心已经跟企业关联
		if (wizbini.cfgSysSetupadv.isTcIndependent() && eipService.checkTcrOccupancy(prof.my_top_tc_id)) {
			tcr_id = prof.my_top_tc_id;
		}
		List<SitePoster> list = sitePosterService.getPoster(tcr_id, 0);	
		
		if (list == null || list.size() == 0) {
			list = sitePosterService.getPoster(prof.root_ent_id, 0);
		}
		if(list != null && list.size() > 0){
			for(SitePoster pst : list){
				ImageUtil.combineImagePath(pst);
			}
		}
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		session.setAttribute("logo", list.get(0));
		session.setAttribute("welcomeText", list.get(0).getSp_welcome_word());
		int posicount=userPositionLrnMapService.getCountById(null,"1",wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id);
		session.setAttribute("positionMapCount",posicount);
		int specount=specialTopicService.getCountByStatus(wizbini,prof,1);
		session.setAttribute("specialTopicCount",specount);
		int livecount=liveService.getCount(wizbini, prof);
		session.setAttribute("liveCount",livecount);
		int profecount=professionService.getAllFront(wizbini.cfgSysSetupadv.isTcIndependent(),prof.my_top_tc_id).size();
		session.setAttribute("professionCount",profecount);
		int cnt=posicount+profecount;
		session.setAttribute("learningMapCount",cnt);
		model.addAttribute("showTaskOnPage", true);//页面header是否显示任务的标记
		
		//注入我的任务数据  （当前培训中心以及下面所有的子培训中心）
		injectTaskCountModel(model,prof,0,0);
		
		//是否达到用户警告值
		model.addAttribute("sys_warning", forCallOldAPIService.getWarn(null));
		
		return mav;
	}
	
	
	@RequestMapping(value = "openJson")
	@ResponseBody
	public String openJson(Model model, Page<AeItem> page,
			loginProfile prof,
			WizbiniLoader wizbini) {
		page.getParams().put("isExam", 0);
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		homeService.getOpenList(page, tcrId, prof.usr_ent_id);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping(value = "signupJson")
	@ResponseBody
	public String signupJson(Model model, Page<AeApplication> page,
			loginProfile prof,
			WizbiniLoader wizbini) {
		page.getParams().put("isExam", 0);
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		homeService.getSignupList(page, tcrId, prof.usr_ent_id, prof.cur_lan);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping(value = "recommendJson")
	@ResponseBody
	public String recommendJson(Model model, Page<ItemTargetLrnDetail> page,
			loginProfile prof,
			WizbiniLoader wizbini) {
		page.getParams().put("isExam", 0);
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		homeService.getRecommendList(page, tcrId, prof.usr_ent_id, prof.cur_lan);
		return JsonFormat.format(model, page);
	}
	
	
	@RequestMapping(value = "hotCourseJson")
	@ResponseBody
	public String hotCourseJson(Model model, Page<AeItem> page,
			loginProfile prof,
			WizbiniLoader wizbini) {
		page.getParams().put("isExam", 0);
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		homeService.getHotList(page, tcrId, prof.usr_ent_id, prof.cur_lan);
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping(value = "newCourseJson")
	@ResponseBody
	public String newCourseJson(Model model, Page<AeItem> page,
			loginProfile prof,
			WizbiniLoader wizbini) {
		page.getParams().put("isExam", 0);
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		homeService.getNewList(page, tcrId, prof.usr_ent_id, prof.cur_lan);
		return JsonFormat.format(model, page);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "count")
	@ResponseBody
	public String newMessageCount(Model model, WizbiniLoader wizbini, 
			loginProfile prof,
			@RequestParam(value="tcrId", defaultValue="0") long tcrId,
			@RequestParam(value="isMobile", defaultValue="0") int isMobile) throws Exception{
		
		injectTaskCountModel(model,prof,tcrId,isMobile);
		
		//是否拥有下属
		model.addAttribute("hasStaff", forCallOldAPIService.hasStaff(null, prof));
		
		return JsonFormat.format(model);
	}
	
	/**
	 * 为model注入相关任务数量
	 * @param model
	 */
	private void injectTaskCountModel(Model model, loginProfile prof ,long tcrId,int isMobile){
		
		List<Long> tcrIdList = new ArrayList<Long>();
		if (tcrId > 0) {
			tcrIdList.add(tcrId);
		} else {
			List<TcTrainingCenter> list = tcTrainingCenterService.getTcrIdList(prof.usr_ent_id, prof.my_top_tc_id);
			for(int i=0;i<list.size();i++){
				tcrIdList.add(list.get(i).getTcr_id());
			}
		}
		int count = messageService.getUserNotReadCount(tcrIdList,prof,0);
		model.addAttribute("announceCount" , count);
		count = 0;
		count = webMessageService.getUserNotReadCount(prof.usr_ent_id);
		model.addAttribute("messageCount" , count);
		count = 0;
		count = aeApplicationService.getSubordinateApprovalDetail(new Page(), prof.usr_ent_id, "PENDING").getTotalRecord();
		model.addAttribute("approvalCount", count);
		count = 0;
		count = resourcesService.getMyEvaluationCount(prof.usr_ent_id, 0, isMobile);
		model.addAttribute("evaluationCount", count);
		count = 0;
		count = (int)votingService.getInProgressCount(prof.usr_ent_id);
		model.addAttribute("votingCount", count);
	}
	
	/**
	 * 移动端清除session
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "mobile/sessionClear")
	@ResponseBody
	@Anonymous
	public String mobileAppClearSession(HttpServletRequest request){
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		return "success";
	}

	/**
	 * 移动端初始化页面，请求应答测试
	 * @return
	 * @throws MessageException 
	 * @throws cwException 
	 * @throws SQLException
	 */
	@RequestMapping(value = "mobile/replyTest")
	@ResponseBody
	public String mobileReplyTest(loginProfile prof,HttpServletRequest request,WizbiniLoader wizbini) throws MessageException, cwException, SQLException{
		
		if(prof != null){
			//如prof不为null，需要判断登录用户数是否超出设置的值
			if(forCallOldAPIService.loginUserExceedLimit(null,prof,wizbini)){
				String message = LabelContent.get(prof.cur_lan, "error_lab_login_fail_08");
				throw new MessageException(message);
			}
		}
		
		return "success";
	}
}

