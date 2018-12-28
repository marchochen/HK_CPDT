package com.cwn.wizbank.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbException;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;
import com.cwn.wizbank.cpd.service.AeItemCPDItemService;
import com.cwn.wizbank.cpd.service.CpdGroupService;
import com.cwn.wizbank.cpd.service.CpdLrnAwardRecordService;
import com.cwn.wizbank.entity.AeApplication;
import com.cwn.wizbank.entity.AeItem;
import com.cwn.wizbank.entity.AeItemAccess;
import com.cwn.wizbank.entity.AeItemCPDItem;
import com.cwn.wizbank.entity.CourseRecord;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AcRoleFunctionService;
import com.cwn.wizbank.services.AeApplicationService;
import com.cwn.wizbank.services.AeItemAccessService;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.CourseRecordService;
import com.cwn.wizbank.services.CourseTabsRemindService;
import com.cwn.wizbank.services.ItemTargetLrnDetailService;
import com.cwn.wizbank.services.ModuleManagementService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;

/**
 * 
 * @author leon.li 课程
 */
@Controller
@RequestMapping("course")
public class CourseController {

	@Autowired
	ItemTargetLrnDetailService itemTargetLrnDetailService;
	@Autowired
	CourseRecordService courseRecordService;
	@Autowired
	AeApplicationService aeApplicationService;
	@Autowired
	AcRoleFunctionService acRoleFunctionService;
	@Autowired
	AeItemService aeItemService;
	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	@Autowired
	AclService aclService;
	@Autowired
	CourseTabsRemindService courseTabsRemindService;
	@Autowired
	AeItemAccessService aeItemAccessService;
	
	@Autowired
	ModuleManagementService moduleManagementService;
	@Autowired
	AeItemCPDItemService aeItemCPDItemService;
	@Autowired
	CpdGroupService cpdGroupService;
	@Autowired
	CpdLrnAwardRecordService cpdLrnAwardRecordService;

	@RequestMapping(value = "detail/{encItmId}", method = RequestMethod.GET)
//	@HasPermission(AclFunction.FTN_LRN_LEARNING_COURSEVIEW)
	public ModelAndView toPage(ModelAndView mav, HttpServletRequest request,
			@PathVariable(value = "encItmId") String encItmId,
			@RequestParam(value = "tkhId", required = false, defaultValue="0") long tkhId,
			loginProfile prof,
			Page<AeItem> page)
			throws AuthorityException, qdbException, cwSysMessage {
		
		long itmId = EncryptUtil.cwnDecrypt(encItmId);
		
		if(tkhId > 0){
			//指定报名记录
			AeApplication app = aeApplicationService.getAeApplicationMapper().getByTkhId(tkhId);
			if(app == null || app.getApp_ent_id() != prof.usr_ent_id || app.getApp_itm_id() != itmId){
				if(app == null){
					// 防止通过修改URL上参数，进入查看没有权限看的课程
					throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
				}else{
					AeItem parent = aeItemService.getParent(app.getApp_itm_id());
					if(parent != null && parent.getItm_id() == itmId){
						//如果离线课程，则判断是否在班级中报了名
						tkhId = 0;
					}else{
						// 防止通过修改URL上参数，进入查看没有权限看的课程
						throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
					}
				}
			}
		}
		
		AeItem item = aeItemService.getAeItem(page, itmId, prof.usr_ent_id, prof.my_top_tc_id);
		if(item == null && tkhId > 0 && aeApplicationService.getItemId(tkhId) == itmId){
			item = aeItemService.get(itmId);
		} else {
			AeApplication app = aeApplicationService.getMaxAppByUser(prof.usr_ent_id, itmId);
			if(app != null) {
				item = aeItemService.get(app.getApp_itm_id());
			}
		}
		mav = new ModelAndView("course/detail");
		mav.addObject("tkhId", tkhId);
		if(request.getParameter("isIntergrated") != null){
			   Boolean isIntergrated=Boolean.parseBoolean((request.getParameter("isIntergrated")));
			   mav.addObject("isIntergrated",isIntergrated);
			   if(isIntergrated) {
				   AeItem itemForItg = aeItemService.get(Long.parseLong(request.getParameter("itgItemId")));
				   mav.addObject("itgItemId",request.getParameter("itgItemId"));
				   mav.addObject("itgTkhId",request.getParameter("itgTkhId"));
				   mav.addObject("itgItmTitle", itemForItg.getItm_title());
			  }		
		}
		if (item == null) {
			//或是没有权限
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		} else if (1 == item.getItm_ref_ind()) {
			mav = new ModelAndView("course/openDetail");
		} else if (1 == item.getItm_exam_ind()) {
			mav = new ModelAndView("exam/detail");
		}
		mav.addObject("itmId", itmId);
		//讲师
		List<AeItemAccess> instrucotrs  = aeItemAccessService.getInstructorsByItmId(itmId);
		boolean hasInstrucotrs =false;
		if(instrucotrs!=null && instrucotrs.size()>0){
			hasInstrucotrs = true;
		}
		mav.addObject("hasInstrucotrs", hasInstrucotrs);
		try {
			boolean hasCPDFunctions =  AccessControlWZB.hasCPDFunction();
			List<String> list = cpdLrnAwardRecordService.getTableColumn(itmId);
			if(hasCPDFunctions &&( null == list || list.size() == 0)){
				hasCPDFunctions = false;
			}
            mav.addObject("hasCPDFunctions", hasCPDFunctions);
        } catch (SQLException e ) {
            e.printStackTrace();
        }
		return mav;
	}
	
	@RequestMapping(value="admin/mgt_comments/{itmId}",method = RequestMethod.GET)
	public ModelAndView toPageReply(ModelAndView mav,
			@PathVariable(value = "itmId") String itmId,
			@RequestParam(value = "tkhId", required = false, defaultValue="0") long tkhId,
			loginProfile prof)
			throws DataNotFoundException, AuthorityException, SQLException{
		long encryp_itmId=(int)EncryptUtil.cwnDecrypt(itmId);
		boolean isManage = false;
		isManage = aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN});
		if(isManage){
		//mav = new ModelAndView("course/mgt_cos_comment");
		mav = new ModelAndView("/admin/course/mgt_cos_comment");
		AeItem aeItem = aeItemService.get(encryp_itmId);
		mav.addObject("item", aeItem) ;
		mav.addObject("hasRole", aeItemService.getHasRolePrivilege(aeItem,prof.current_role));
		mav.addObject("courseTabsRemind", courseTabsRemindService.getCourseTabsRemind(encryp_itmId));
		}else{
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		
		return mav;
	}

	/**
	 * 课程页面
	 * 
	 * @param command
	 *            recommend 推荐课程页面 ，signup 已报名页面, courseCatalog 课程目录
	 * @return
	 * @throws SQLException 
	 * @throws DataNotFoundException
	 */
	@RequestMapping(value = "courseCatalog", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_LEARNING_CATALOG)
	public ModelAndView toCourseCatalog(ModelAndView mav, loginProfile prof, WizbiniLoader wizbini , String isT) throws SQLException{
		mav = new ModelAndView("course/courseCatalog");
		List<TcTrainingCenter> myTrainingCenter = tcTrainingCenterService.getMyTrainingCenter(wizbini, prof.usr_ent_id);
		mav.addObject("myTrainingCenter",myTrainingCenter);
		//如果当期角色有管理CPD功能就显示CPD筛选条件
		mav.addObject("cpd_search_ind",AccessControlWZB.hasCPDFunction());
		return mav;
	}
	
	@RequestMapping(value = "recommend", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_LEARNING_RECOMMEND)
	public ModelAndView toRecommend(ModelAndView mav, loginProfile prof){
		mav = new ModelAndView("course/recommend");
		return mav;
	}
	
	@RequestMapping(value = "signup", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_LEARNING_SIGNUP)
	public ModelAndView toSignup(ModelAndView mav, loginProfile prof){
		mav = new ModelAndView("course/signup");
		return mav;
	}
	
	@RequestMapping(value = "open", method = RequestMethod.GET)
	@HasPermission(AclFunction.FTN_LRN_LEARNING_OPEN)
	public ModelAndView toOpen(ModelAndView mav, loginProfile prof){
		mav = new ModelAndView("course/open");
		return mav;
	}
	
	/**
	 * 获取相关课程的JSON
	 * 
	 * @param command
	 *            recommend 推荐课程 ，signup 已报名课程 ，open 公开课
	 * @return
	 */
	@RequestMapping(value = "{command}Json")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String courseJson(Model model, Page page,
			@PathVariable(value = "command") String command, loginProfile prof,
			WizbiniLoader wizbini) {
		page.getParams().put("isExam", 0);
		long tcrId = 0;
		if(page.getParams().get("tcrId") == null || Integer.parseInt((String)page.getParams().get("tcrId")) < 1){
			if (wizbini.cfgSysSetupadv.isTcIndependent()) {
				tcrId = prof.my_top_tc_id;
			}
			page.getParams().put("tcrId", tcrId);
		}

		if ("recommend".equals(command)) {
			itemTargetLrnDetailService.recommend(prof.usr_ent_id, prof.cur_lan, page);
		} else if ("signup".equals(command)) {
			aeApplicationService.signup(prof.usr_ent_id, prof.cur_lan, page);
		} else if ("catalogCourse".equals(command)) {
			aeItemService.getCatalogCourse(prof.usr_ent_id, prof.cur_lan, page);
		} else if ("open".equals(command)) {
			aeItemService.getOpens(page, tcrId, prof.usr_ent_id);
		}
		return JsonFormat.format(model, page);
	}
	
	@RequestMapping(value = "historyJson/{encUsrEntId}")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public String courseHistory(Model model, Page page,
			@PathVariable(value = "encUsrEntId") String encUsrEntId,
			loginProfile prof,
			WizbiniLoader wizbini){
		long usrEntId = EncryptUtil.cwnDecrypt(encUsrEntId);
		if(usrEntId < 1) {
			usrEntId = prof.usr_ent_id;
		}
		aeApplicationService.getMyCourse(usrEntId, prof.cur_lan, page);
		return JsonFormat.format(model, page);

	}

	/**
	 * 课程详细页面获取数据
	 * 
	 * @param itm_id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "openDetailJson/{encItmId}")
	@ResponseBody
	public String openDetailJson(
			Model model,
			WizbiniLoader wizbini,
			Params param,
			loginProfile prof,
			@PathVariable(value = "encItmId") String encItmId,
			@RequestParam(value = "tkhId", required = false, defaultValue = "0") long tkhId,
			@RequestParam(value = "appId", required = false, defaultValue = "0") long appId,
			@RequestParam(value = "resId", required = false, defaultValue = "0") long resId,
			@RequestParam(value = "isMobile", required = false, defaultValue = "false")boolean isMobile
			) throws Exception {
		
		long itmId = EncryptUtil.cwnDecrypt(encItmId);
		
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		aeItemService.getOpenDetial(model, tcrId, itmId, prof.usr_ent_id, prof.cur_lan, prof.hasStaff, 
				aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_OPEN_COS_MAIN}), isMobile ? 1 : 0, prof.usr_id);
		return JsonFormat.format(param, model);
	}
	/**
	 * 记录公开课播放的时间
	 * 
	 * @param model
	 * @param userId
	 * @param courseId
	 * @param courseNum
	 * @param currTime
	 * @return
	 */
	@RequestMapping("openTimeRecord")
	@ResponseBody
	public String openPlayTime(
			Model model,
			Params param,
			@RequestParam(value = "userId",required = true, defaultValue = "0") int userId,
			@RequestParam(value = "courseId",required = true, defaultValue = "0") int courseId,
			@RequestParam(value = "courseNum",required = true, defaultValue = "0") int courseNum,
			@RequestParam(value = "duration",required = true, defaultValue = "0") String currTime){
		model.addAttribute("msg", "");
		long pcr_duration = Long.parseLong(currTime);
		CourseRecord courseRecord = new CourseRecord(userId,courseId,courseNum,pcr_duration);
		courseRecordService.insertRecord(courseRecord);
		return JsonFormat.format(param, model);
	}
	
	/**
	 * 记录公开课的某个视频的笔记
	 * 
	 * @param model
	 * @param userId
	 * @param courseId
	 * @param courseNum
	 * @param pcrNote
	 * @return
	 */
	@RequestMapping(value="noteCourseRecord",method=RequestMethod.POST)
	@ResponseBody
	public String recordCourse(
			Model model,
			Params param,
			@RequestParam(value = "userId",required = true, defaultValue = "0") int userId,
			@RequestParam(value = "courseId",required = true, defaultValue = "0") int courseId,
			@RequestParam(value = "courseNum",required = true, defaultValue = "0") int courseNum,
			@RequestParam(value = "pcrNote",required = true, defaultValue = "") String pcrNote){
		model.addAttribute("msg", "");
		CourseRecord courseRecord = new CourseRecord(userId,courseId,courseNum,pcrNote);
		courseRecordService.insertRecord(courseRecord);
		return JsonFormat.format(param, model);
	}
	/**
	 * 获取当前视频笔记
	 */
	@RequestMapping("getCourseRecord")
	@ResponseBody
	public String getCourseRecord(
			Model model,
			Page<?> page,
			Params param,
			@RequestParam(value = "userId",required = true, defaultValue = "0") int userId,
			@RequestParam(value = "courseId",required = true, defaultValue = "0") int courseId,
			@RequestParam(value = "courseNum",required = true, defaultValue = "0") int courseNum
			){
		CourseRecord backCourseRecord = null;
		CourseRecord courseRecord = new CourseRecord(userId,courseId,courseNum);
		backCourseRecord = courseRecordService.getCourseRecord(courseRecord);
		if(backCourseRecord != null)
		model.addAttribute("note", backCourseRecord.getPcr_note());
		return JsonFormat.format(param, model);
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
	public String detailJson(
			Model model,
			WizbiniLoader wizbini,
			Params param,
			@PathVariable(value = "encItmId") String encItmId,
			@RequestParam(value = "tkhId", required = false, defaultValue = "0") long tkhId,
			@RequestParam(value = "appId", required = false, defaultValue = "0") long appId,
			@RequestParam(value = "resId", required = false, defaultValue = "0") long resId,
			@RequestParam(value = "mobileInd", required = false, defaultValue = "0") int mobileInd,
			loginProfile prof) throws Exception {
		/*
		 * 此处代码是为了暂时解决公共调查问卷会出错的问题，带APP新版本上线后（现在版本为3.6.70922）。请务必删掉
		 */
		if(encItmId == null || encItmId.equalsIgnoreCase("null")){
			AeItem item = new AeItem();
			item.setItm_title("null");
			model.addAttribute("item", item);
			return JsonFormat.format(param, model);
		}
		/*
		 * 暂时解决公共调查问卷会出错代码结束
		 */
		
		long itmId = EncryptUtil.cwnDecrypt(encItmId);
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		
		
        model.addAttribute("hasCPDFunction", AccessControlWZB.hasCPDFunction());
        model.addAttribute("hasCPDColumn", cpdLrnAwardRecordService.getTableColumn(itmId));
		aeItemService.getDetial(model, tcrId, itmId, tkhId, prof.usr_ent_id,prof.cur_lan, prof.hasStaff, mobileInd, prof.usr_id);
		return JsonFormat.format(param, model);
	}
			
	@RequestMapping("return/{tkh_id}")
	public String isExam(@PathVariable(value = "tkh_id") long tkh_id){
		return "redirect:/app/course/detail/" + aeApplicationService.getItemId(tkh_id) + "?isCont=true&tkhId=" + tkh_id;
	}
	
	@RequestMapping("lesson/{id}")
	@ResponseBody
	public String itemLesson(Model model, @PathVariable(value="id") long id){
		model.addAttribute("lessons", aeItemService.getAeItemLessonMapper().getList(id));
		return JsonFormat.format(model);
	}
	
	@RequestMapping("message/{id}")
	@ResponseBody
	public String itemMessage(Model model, @PathVariable(value="id") long id){
		model.addAttribute("messages", aeItemService.getMessagesByItmId(id));
		return JsonFormat.format(model);
	}
	
	@RequestMapping("signup/count")
	@ResponseBody
	public String signupCount(Model model, loginProfile prof, WizbiniLoader wizbini, 
			@RequestParam(value="isExam", defaultValue="0") int isExam) {
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		model.addAttribute("count", aeApplicationService.getSignupCount(
				prof.usr_ent_id, wizbini.cfgSysSetupadv.getNewestDuration(),
				isExam, tcrId));
		return JsonFormat.format(model);
	}
	
	@RequestMapping("catalog/count")
	@ResponseBody
	public String catalogCount(Model model, loginProfile prof, WizbiniLoader wizbini, 
			@RequestParam(value="isExam", defaultValue="0") int isExam) {
		long tcrId = 0;
		if (wizbini.cfgSysSetupadv.isTcIndependent()) {
			tcrId = prof.my_top_tc_id;
		}
		model.addAttribute("count", aeItemService.getCatalogCount(prof.usr_ent_id, wizbini.cfgSysSetupadv.getNewestDuration(), isExam, tcrId));
		return JsonFormat.format(model);
	}
	
	@RequestMapping(value = "getMyCourse")
	@ResponseBody
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getMyCourse(Model model, Page page,loginProfile prof,
			WizbiniLoader wizbini) {
		page.getParams().put("isExam", 0);
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
	
	@RequestMapping(value = "aeInsMultiAppn")
	@ResponseBody
	@SuppressWarnings("rawtypes")
	public String aeInsMultiAppn(Model model, Page page,loginProfile prof, 
			@RequestParam(value="auto_enroll_ind", defaultValue="false", required=false) boolean auto_enroll_ind,
			@RequestParam(value="ent_id_lst", defaultValue="", required=false) String ent_ids,
			@RequestParam(value="itm_id", defaultValue="", required=false) long itm_id) throws cwException, SQLException, qdbException, cwSysMessage, IOException{
		
		model.addAttribute(moduleManagementService.aeInsMultiAppn(null, prof, itm_id, auto_enroll_ind, ent_ids));
		
		return JsonFormat.format(model, page);
	}
	

	
	/**
	 * 去到学习日程表页面
	 * @return
	 */
	@RequestMapping("schedule")
	public String schedule(){
		return "course/schedule";
	}
	
	/**
	 * 获取日程表课程列表
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @param itemType 课程类型
	 * @return
	 */
	@RequestMapping("scheduleItems")
	@ResponseBody
	public List<AeItem> scheduleItemList(String startDate,String endDate,Model model,loginProfile prof,String itemType){
		List<AeItem> itemList = this.aeItemService.scheduleItemList(startDate,endDate,prof.usr_ent_id,itemType);
		return itemList;
	}

	/**
	 * 设置CPT/D所需时数
	 * @param mav
	 * @param itmId
	 * @param prof
	 * @return
	 * @throws DataNotFoundException
	 * @throws AuthorityException
	 * @throws SQLException 
	 */
	@RequestMapping(value="admin/mgt_cpd_set_hours/{itmId}",method = RequestMethod.GET)
	public ModelAndView toCPDHoursSet(ModelAndView mav,
			@PathVariable(value = "itmId") String itmId,
			loginProfile prof)
			throws DataNotFoundException, AuthorityException, SQLException{
		long encryp_itmId=(int)EncryptUtil.cwnDecrypt(itmId);
		boolean isManage = false;
		isManage = aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN});
		if(isManage){
		mav = new ModelAndView("/admin/course/mgt_cpd_set_hours");
		AeItem aeItem = aeItemService.get(encryp_itmId);
		mav.addObject("item", aeItem) ;
		mav.addObject("hasRole", aeItemService.getHasRolePrivilege(aeItem,prof.current_role));
		mav.addObject("courseTabsRemind", courseTabsRemindService.getCourseTabsRemind(encryp_itmId));
		}else{
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		
		return mav;
	}
	
	
	/**
	 * 设置CPT/D所需时数
	 * @param mav
	 * @param itmId
	 * @param prof
	 * @return
	 * @throws DataNotFoundException
	 * @throws AuthorityException
	 */
	@RequestMapping(value="admin/update_cpd_hours/{itmId}",method = RequestMethod.GET)
	public ModelAndView toAddCPDHoursSet(ModelAndView mav,
			@PathVariable(value = "itmId") String itmId,
			loginProfile prof)
			throws DataNotFoundException, AuthorityException, SQLException, qdbException, cwSysMessage{
		long encryp_itmId=(int)EncryptUtil.cwnDecrypt(itmId);
		boolean isManage = false;
		isManage = aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN});
		if(isManage){
		mav = new ModelAndView("/admin/course/add_mgt_cpd_set_hours");
		AeItem aeItem = aeItemService.get(encryp_itmId);
		mav.addObject("item", aeItem) ;
		mav.addObject("hasRole", aeItemService.getHasRolePrivilege(aeItem,prof.current_role));
		mav.addObject("courseTabsRemind", courseTabsRemindService.getCourseTabsRemind(encryp_itmId));
		
		AeItemCPDItem aeItemCPDItem =  aeItemCPDItemService.getByItmId(encryp_itmId);
		//当未找到该牌照设置时数，判断是否有父级id   使用父级设置的cpd时数
		/*if(null == aeItemCPDItem.getAci_id()){
			if(null != aeItem.getParent() && null != aeItem.getParent().getItm_id()){
				aeItemCPDItem =  aeItemCPDItemService.getByItmId(aeItem.getParent().getItm_id());
			}
		}*/
		//获取当前生效的所有小牌牌照
		List<CpdGroup> list = cpdGroupService.getAllOrder(0);
		mav.addObject("cpdGroupList",list);
		
		mav.addObject("aeItemCPDItem",aeItemCPDItem);
		}else{
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		
		return mav;
	}
	
	
	/**
	 * CPT/D获得时数记录
	 * @param mav
	 * @param itmId
	 * @param prof
	 * @return
	 * @throws DataNotFoundException
	 * @throws AuthorityException
	 * @throws cwSysMessage 
	 * @throws qdbException 
	 * @throws SQLException 
	 */
	@RequestMapping(value="admin/mgt_cpd_hours_award/{itmId}",method = RequestMethod.GET)
	public ModelAndView toCPDHoursAwaeded(ModelAndView mav,
			@PathVariable(value = "itmId") String itmId,
			loginProfile prof)
			throws DataNotFoundException, AuthorityException, SQLException, qdbException, cwSysMessage{
		long encryp_itmId=(int)EncryptUtil.cwnDecrypt(itmId);
		boolean isManage = false;
		isManage = aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_EXAM_MGT,AclFunction.FTN_AMD_ITM_COS_MAIN,AclFunction.FTN_AMD_OPEN_COS_MAIN});
		if(isManage){
		mav = new ModelAndView("/admin/course/cpd_hours_awarded");
		AeItem aeItem = aeItemService.get(encryp_itmId);
		mav.addObject("item", aeItem) ;
		mav.addObject("hasRole", aeItemService.getHasRolePrivilege(aeItem,prof.current_role));
		mav.addObject("courseTabsRemind", courseTabsRemindService.getCourseTabsRemind(encryp_itmId));
		}else{
			throw new AuthorityException(LabelContent.get(prof.cur_lan, "error_no_authority"));
		}
		return mav;
	}
	

}
