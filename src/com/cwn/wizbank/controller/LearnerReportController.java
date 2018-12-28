package com.cwn.wizbank.controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.junit.runner.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.db.DbTrainingCenter;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.report.ReportTemplate;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.ReportSpec;
import com.cwn.wizbank.entity.vo.LearnerDetailReportVo;
import com.cwn.wizbank.entity.vo.LearnerReportParamVo;
import com.cwn.wizbank.exception.DataNotFoundException;
import com.cwn.wizbank.persistence.ReportSpecMapper;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.LearnerReportService;
import com.cwn.wizbank.services.ReportSpecService;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.StringUtils;

@Controller
@RequestMapping("/admin/learnerReport")
public class LearnerReportController {

	@Autowired
	LearnerReportService learnerReportService;

	@Autowired
	ReportSpecMapper reportSpecMapper;

	@Autowired
	ReportSpecService reportSpecService;

	private final String CHOSE_CONDITION_SESSION_KEY = "LEARNER_REPORT_CHOSE_CONDITION";

	@RequestMapping("")
	@HasPermission(AclFunction.FTN_AMD_TRAINING_REPORT_MGT)
	public ModelAndView announce(ModelAndView mav, loginProfile prof)
			throws DataNotFoundException {
		mav.addObject("language", prof.getCur_lan());
		mav = new ModelAndView("admin/learnerReport/index");
		return mav;
	}

	@RequestMapping("getLearnerReportByUser")
	@HasPermission(AclFunction.FTN_AMD_TRAINING_REPORT_MGT)
	public ModelAndView getLearnerReportByUser(
			ModelAndView mav,
			loginProfile prof,
			LearnerReportParamVo vo,
			WizbiniLoader wizbini,
			HttpSession session,
			@RequestParam(value = "pageExportUser", required = false) String pageExportUser,
			@RequestParam(value = "pageExportUserIdsText", required = false) String pageExportUserIdsText,
			@RequestParam(value = "pageExportCourse", required = false) String pageExportCourse,
			@RequestParam(value = "pageExportCourseIdsText", required = false) String pageExportCourseIdsText,
			@RequestParam(value = "pageCourseType", required = false) String pageCourseType,
			@RequestParam(value = "pageAppnStartDatetime", required = false) String pageAppnStartDatetime,
			@RequestParam(value = "pageAppnEndDatetime", required = false) String pageAppnEndDatetime,
			@RequestParam(value = "pageAttStartTime", required = false) String pageAttStartTime,
			@RequestParam(value = "pageAttEndTime", required = false) String pageAttEndTime,
			@RequestParam(value = "pageAppStatus", required = false) String pageAppStatus,
			@RequestParam(value = "pageCourseStatus", required = false) String pageCourseStatus,
			@RequestParam(value = "pageResultDataStatistic", required = false) String pageResultDataStatistic,
			@RequestParam(value = "pageIsExportDetail", required = false) String pageIsExportDetail,
			@RequestParam(value = "pageUserInfo", required = false) String pageUserInfo,
			@RequestParam(value = "pageCourseInfo", required = false) String pageCourseInfo,
			@RequestParam(value = "pageOtherInfo", required = false) String pageOtherInfo)
			throws DataNotFoundException, SQLException {
		if (null == vo.getLanguage()) { // 这个判断主要用于切换语言 之前的选择条件为空
			vo = (LearnerReportParamVo) session
					.getAttribute(CHOSE_CONDITION_SESSION_KEY);
		}
		mav = new ModelAndView("admin/learnerReport/userLearnerReport");
		vo.setRoleTcInd(AccessControlWZB.isRoleTcInd(prof.current_role));
		vo.setUserRole(prof.current_role);
		vo.setMyTopTcId(prof.my_top_tc_id);
		vo.setUserEntId(prof.usr_ent_id);
		vo.setLanguage(prof.cur_lan);
		vo.setAppnStartDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnStartDatetime(), DateUtil.patternA));
		vo.setAppnEndDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnEndDatetime(), DateUtil.patternA));
		vo.setAttStartDispalyTime(DateUtil.getInstance().dateToString(
				vo.getAttStartTime(), DateUtil.patternA));
		vo.setAttEndDisplayTime(DateUtil.getInstance().dateToString(
				vo.getAttEndTime(), DateUtil.patternA));
		vo.setAnalysisType(1);
		setEndDate(vo);
		Map data = learnerReportService.getLearnerReportByUser(vo);
		mav.addObject("reportData", data);
		mav.addObject("choseCondition", vo);
		
		if(pageExportCourseIdsText != null){		
			pageExportCourseIdsText = StringUtils.escapeUtils(pageExportCourseIdsText); 
		}
		if(pageExportUserIdsText != null){		
			pageExportUserIdsText = StringUtils.escapeUtils(pageExportUserIdsText); 
		}
		
		mav.addObject("pageExportUser", pageExportUser);
		mav.addObject("pageExportUserIdsText", pageExportUserIdsText);
		mav.addObject("pageExportCourse", pageExportCourse);
		mav.addObject("pageExportCourseIdsText", pageExportCourseIdsText);
		mav.addObject("pageCourseType", pageCourseType);
		mav.addObject("pageAppnStartDatetime", pageAppnStartDatetime);
		mav.addObject("pageAppnEndDatetime", pageAppnEndDatetime);
		mav.addObject("pageAttStartTime", pageAttStartTime);
		mav.addObject("pageAttEndTime", pageAttEndTime);
		mav.addObject("pageAppStatus", pageAppStatus);
		mav.addObject("pageCourseStatus", pageCourseStatus);
		mav.addObject("pageResultDataStatistic", pageResultDataStatistic);
		mav.addObject("pageIsExportDetail", pageIsExportDetail);
		mav.addObject("pageUserInfo", pageUserInfo);
		mav.addObject("pageCourseInfo", pageCourseInfo);
		mav.addObject("pageOtherInfo", pageOtherInfo);

		session.setAttribute(CHOSE_CONDITION_SESSION_KEY, vo);
		String voJsonString = JSON.toJSONString(vo);
		if(voJsonString != null){			
			voJsonString = StringUtils.escapeJsonUtils(voJsonString);
		}
		mav.addObject("voJsonString", voJsonString);
		return mav;
	}

	@RequestMapping("generateLearnerReportByUser")
	@HasPermission(AclFunction.FTN_AMD_TRAINING_REPORT_MGT)
	@ResponseBody
	public String generateLearnerReportByUser(ModelAndView mav,
			loginProfile prof, LearnerReportParamVo vo, WizbiniLoader wizbini)
			throws DataNotFoundException, SQLException {
		vo.setRoleTcInd(AccessControlWZB.isRoleTcInd(prof.current_role));
		vo.setUserRole(prof.current_role);
		vo.setMyTopTcId(prof.my_top_tc_id);
		vo.setUserEntId(prof.usr_ent_id);
		setEndDate(vo);
		Map data = learnerReportService.getLearnerReportByUser(vo);

		return null;
	}

	@RequestMapping("getLearnerReportByCourse")
	@HasPermission(AclFunction.FTN_AMD_TRAINING_REPORT_MGT)
	public ModelAndView getLearnerReportByCourse(
			ModelAndView mav,
			loginProfile prof,
			LearnerReportParamVo vo,
			WizbiniLoader wizbini,
			HttpSession session,
			@RequestParam(value = "pageExportUser", required = false) String pageExportUser,
			@RequestParam(value = "pageExportUserIdsText", required = false) String pageExportUserIdsText,
			@RequestParam(value = "pageExportCourse", required = false) String pageExportCourse,
			@RequestParam(value = "pageExportCourseIdsText", required = false) String pageExportCourseIdsText,
			@RequestParam(value = "pageCourseType", required = false) String pageCourseType,
			@RequestParam(value = "pageAppnStartDatetime", required = false) String pageAppnStartDatetime,
			@RequestParam(value = "pageAppnEndDatetime", required = false) String pageAppnEndDatetime,
			@RequestParam(value = "pageAttStartTime", required = false) String pageAttStartTime,
			@RequestParam(value = "pageAttEndTime", required = false) String pageAttEndTime,
			@RequestParam(value = "pageAppStatus", required = false) String pageAppStatus,
			@RequestParam(value = "pageCourseStatus", required = false) String pageCourseStatus,
			@RequestParam(value = "pageResultDataStatistic", required = false) String pageResultDataStatistic,
			@RequestParam(value = "pageIsExportDetail", required = false) String pageIsExportDetail,
			@RequestParam(value = "pageUserInfo", required = false) String pageUserInfo,
			@RequestParam(value = "pageCourseInfo", required = false) String pageCourseInfo,
			@RequestParam(value = "pageOtherInfo", required = false) String pageOtherInfo)
			throws DataNotFoundException, SQLException {
		mav = new ModelAndView("admin/learnerReport/courseLearnerReport");
		if (null == vo.getLanguage()) { // 这个判断主要用于切换语言 之前的选择条件为空
			vo = (LearnerReportParamVo) session
					.getAttribute(CHOSE_CONDITION_SESSION_KEY);
		}
		vo.setRoleTcInd(AccessControlWZB.isRoleTcInd(prof.current_role));

		if(pageExportCourseIdsText != null){		
			pageExportCourseIdsText = StringUtils.escapeUtils(pageExportCourseIdsText); 
		}
		if(pageExportUserIdsText != null){
			pageExportUserIdsText = StringUtils.escapeUtils(pageExportUserIdsText); 
		}

		vo.setUserRole(prof.current_role);
		vo.setMyTopTcId(prof.my_top_tc_id);
		vo.setUserEntId(prof.usr_ent_id);
		vo.setAppnStartDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnStartDatetime(), DateUtil.patternA));
		vo.setAppnEndDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnEndDatetime(), DateUtil.patternA));
		vo.setAttStartDispalyTime(DateUtil.getInstance().dateToString(
				vo.getAttStartTime(), DateUtil.patternA));
		vo.setAttEndDisplayTime(DateUtil.getInstance().dateToString(
				vo.getAttEndTime(), DateUtil.patternA));
		vo.setAnalysisType(0);
		vo.setLanguage(prof.cur_lan);
		setEndDate(vo);
		Map data = learnerReportService.getLearnerReportByCourse(vo);
		mav.addObject("reportData", data);
		mav.addObject("choseCondition", vo);
		mav.addObject("pageExportUser", pageExportUser);
		mav.addObject("pageExportUserIdsText", pageExportUserIdsText);
		mav.addObject("pageExportCourse", pageExportCourse);
		mav.addObject("pageExportCourseIdsText", pageExportCourseIdsText);
		mav.addObject("pageCourseType", pageCourseType);
		mav.addObject("pageAppnStartDatetime", pageAppnStartDatetime);
		mav.addObject("pageAppnEndDatetime", pageAppnEndDatetime);
		mav.addObject("pageAttStartTime", pageAttStartTime);
		mav.addObject("pageAttEndTime", pageAttEndTime);
		mav.addObject("pageAppStatus", pageAppStatus);
		mav.addObject("pageCourseStatus", pageCourseStatus);
		mav.addObject("pageResultDataStatistic", pageResultDataStatistic);
		mav.addObject("pageIsExportDetail", pageIsExportDetail);
		mav.addObject("pageUserInfo", pageUserInfo);
		mav.addObject("pageCourseInfo", pageCourseInfo);
		mav.addObject("pageOtherInfo", pageOtherInfo);

		session.setAttribute(CHOSE_CONDITION_SESSION_KEY, vo);
		String voJsonString = JSON.toJSONString(vo);
		if(voJsonString != null){		
			voJsonString = StringUtils.escapeJsonUtils(voJsonString);
		}
		mav.addObject("voJsonString", voJsonString);
		return mav;
	}

	/**
	 * 导出报表
	 * 
	 * @param prof
	 * @param vo
	 * @param wizbini
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("expor")
	@HasPermission(AclFunction.FTN_AMD_TRAINING_REPORT_MGT)
	@ResponseBody
	public Model expor(loginProfile prof, LearnerReportParamVo vo, WizbiniLoader wizbini
			,Model model,String voJsonString,@RequestParam(value = "pageUserInfo", required = false)String pageUserInfo)throws Exception{
		if(null!=voJsonString && voJsonString.length() > 0){
			//转义'
			if(voJsonString != null){				
				voJsonString = StringUtils.escapeJsonUtils(voJsonString);
			}
			vo = JSON.parseObject(voJsonString, LearnerReportParamVo.class);
		}
		model.addAttribute("pageUserInfo", pageUserInfo);
		vo.setRoleTcInd(AccessControlWZB.isRoleTcInd(prof.current_role));
		vo.setUserRole(prof.current_role);
		vo.setMyTopTcId(prof.my_top_tc_id);
		vo.setUserEntId(prof.usr_ent_id);
		vo.setLanguage(prof.cur_lan);
		vo.setAppnStartDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnStartDatetime(), DateUtil.patternA));
		vo.setAppnEndDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnEndDatetime(), DateUtil.patternA));
		vo.setAttStartDispalyTime(DateUtil.getInstance().dateToString(
				vo.getAttStartTime(), DateUtil.patternA));
		vo.setAttEndDisplayTime(DateUtil.getInstance().dateToString(
				vo.getAttEndTime(), DateUtil.patternA));
		setEndDate(vo);
		String fileName = learnerReportService.expor(prof, vo, wizbini);
		model.addAttribute("fileUri", "/temp/" + fileName);
		return model;
	}

	private void setEndDate(LearnerReportParamVo vo) {
		if (null != vo) {
			if (null != vo.getAppnEndDatetime()) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(vo.getAppnEndDatetime());
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				vo.setAppnEndDatetime(cal.getTime());
			}
			if (null != vo.getAttEndTime()) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(vo.getAttEndTime());
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				vo.setAttEndTime(cal.getTime());
			}
		}
	}

	/**
	 * 报表详情页面带条件返回条件页面
	 * 
	 * @param mav
	 * @param prof
	 * @param session
	 * @return
	 * @throws DataNotFoundException
	 */
	@RequestMapping("back")
	@HasPermission(AclFunction.FTN_AMD_TRAINING_REPORT_MGT)
	public ModelAndView back(
			ModelAndView mav,
			loginProfile prof,
			HttpSession session,
			@RequestParam(value = "pageExportUser", required = false) String pageExportUser,
			@RequestParam(value = "pageExportUserIdsText", required = false) String pageExportUserIdsText,
			@RequestParam(value = "pageExportCourse", required = false) String pageExportCourse,
			@RequestParam(value = "pageExportCourseIdsText", required = false) String pageExportCourseIdsText,
			@RequestParam(value = "pageCourseType", required = false) String pageCourseType,
			@RequestParam(value = "pageAppnStartDatetime", required = false) String pageAppnStartDatetime,
			@RequestParam(value = "pageAppnEndDatetime", required = false) String pageAppnEndDatetime,
			@RequestParam(value = "pageAttStartTime", required = false) String pageAttStartTime,
			@RequestParam(value = "pageAttEndTime", required = false) String pageAttEndTime,
			@RequestParam(value = "pageAppStatus", required = false) String pageAppStatus,
			@RequestParam(value = "pageCourseStatus", required = false) String pageCourseStatus,
			@RequestParam(value = "pageResultDataStatistic", required = false) String pageResultDataStatistic,
			@RequestParam(value = "pageIsExportDetail", required = false) String pageIsExportDetail,
			@RequestParam(value = "pageUserInfo", required = false) String pageUserInfo,
			@RequestParam(value = "pageCourseInfo", required = false) String pageCourseInfo,
			@RequestParam(value = "pageOtherInfo", required = false) String pageOtherInfo)
			throws DataNotFoundException {
		mav.addObject("language", prof.getCur_lan());
		mav = new ModelAndView("admin/learnerReport/index");
		LearnerReportParamVo vo = (LearnerReportParamVo) session
				.getAttribute(CHOSE_CONDITION_SESSION_KEY);
		String voJsonString = JSON.toJSONString(vo);
		
		//转义'
		if(voJsonString != null){		
			voJsonString = StringUtils.escapeJsonUtils(voJsonString);
		}
		if(pageExportCourseIdsText != null){		
			pageExportCourseIdsText = StringUtils.escapeUtils(pageExportCourseIdsText);
		}
		if(pageExportUserIdsText != null){
			pageExportUserIdsText = StringUtils.escapeUtils(pageExportUserIdsText); 
		}
		
		mav.addObject("pageExportUser", pageExportUser);
		mav.addObject("pageExportUserIdsText", pageExportUserIdsText);
		mav.addObject("pageExportCourse", pageExportCourse);
		mav.addObject("pageExportCourseIdsText", pageExportCourseIdsText);
		mav.addObject("pageCourseType", pageCourseType);
		mav.addObject("pageAppnStartDatetime", pageAppnStartDatetime);
		mav.addObject("pageAppnEndDatetime", pageAppnEndDatetime);
		mav.addObject("pageAttStartTime", pageAttStartTime);
		mav.addObject("pageAttEndTime", pageAttEndTime);
		mav.addObject("pageAppStatus", pageAppStatus);
		mav.addObject("pageCourseStatus", pageCourseStatus);
		mav.addObject("pageResultDataStatistic", pageResultDataStatistic);
		mav.addObject("pageIsExportDetail", pageIsExportDetail);
		mav.addObject("pageUserInfo", pageUserInfo);
		mav.addObject("pageCourseInfo", pageCourseInfo);
		mav.addObject("pageOtherInfo", pageOtherInfo);

		mav.addObject("voJsonString", voJsonString);
		return mav;
	}

	@RequestMapping("getLearnerReport")
	@HasPermission(AclFunction.FTN_AMD_TRAINING_REPORT_MGT)
	public ModelAndView getLearnerReport(ModelAndView mav, loginProfile prof,
			LearnerReportParamVo vo, WizbiniLoader wizbini,
			HttpSession session, Long rsp_id) throws DataNotFoundException,
			SQLException, ParseException {
		ReportTemplate reportTemplate = new ReportTemplate();
		ReportSpec reportSpec = reportSpecService.selectReportSpecId(rsp_id);
		Hashtable spec_pairs = reportTemplate.getSpecPairs(reportSpec
				.getRsp_xml());
		Map infoData =learnerReportService.loadInfo(  vo,spec_pairs);
		
		if(vo.getResultDataStatistic() == 0){
	        mav = new ModelAndView("admin/learnerReport/courseLearnerReport");//"统计数据以课程为主";
	        vo.setAnalysisType(0);
       }else{
           mav = new ModelAndView("admin/learnerReport/userLearnerReport");//"统计数据以学员为主";
           vo.setAnalysisType(1);
       }

		//转义'
		Object pageExportCourseIdsText =  infoData.get("pageExportCourseIdsText") == null ? null : StringUtils.escapeUtils(infoData.get("pageExportCourseIdsText").toString());
		Object pageExportUserIdsText = infoData.get("pageExportUserIdsText") == null ? null : StringUtils.escapeUtils(infoData.get("pageExportUserIdsText").toString());

        mav.addObject("pageResultDataStatistic", infoData.get("pageResultDataStatistic"));
        mav.addObject("pageAppnStartDatetime", infoData.get("pageAppnStartDatetime"));
        mav.addObject("pageAppnEndDatetime",  infoData.get("pageAppnEndDatetime"));
        mav.addObject("pageAttStartTime",  infoData.get("pageAttStartTime"));
        mav.addObject("pageAttEndTime",  infoData.get("pageAttEndTime"));
        mav.addObject("pageCourseType",  infoData.get("pageCourseType"));
        mav.addObject("pageExportCourse",  infoData.get("pageExportCourse"));
        mav.addObject("pageExportCourseIds",  infoData.get("pageExportCourseIds"));
        mav.addObject("pageExportCourseIdsText",  pageExportCourseIdsText);
        mav.addObject("pageExportUser",  infoData.get("pageExportUser"));
        mav.addObject("pageExportUserIds",  infoData.get("pageExportUserIds"));
        mav.addObject("pageExportUserIdsText",  pageExportUserIdsText);
        mav.addObject("pageIsExportDetail",  infoData.get("pageIsExportDetail"));
        mav.addObject("pageUserInfo",  infoData.get("pageUserInfo"));
        mav.addObject("pageCourseInfo",  infoData.get("pageCourseInfo"));
        mav.addObject("pageOtherInfo",  infoData.get("pageOtherInfo"));
        mav.addObject("pageAppStatus",  infoData.get("pageAppStatus"));
        mav.addObject("pageCourseStatus",  infoData.get("pageCourseStatus"));
        mav.addObject("pageReturnRptAll",  true);
        mav.addObject("reportSpec", reportSpec);

		vo.setRoleTcInd(AccessControlWZB.isRoleTcInd(prof.current_role));
		vo.setUserRole(prof.current_role);
		vo.setMyTopTcId(prof.my_top_tc_id);
		vo.setUserEntId(prof.usr_ent_id);
		vo.setAppnStartDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnStartDatetime(), DateUtil.patternA));
		vo.setAppnEndDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnEndDatetime(), DateUtil.patternA));
		vo.setAttStartDispalyTime(DateUtil.getInstance().dateToString(
				vo.getAttStartTime(), DateUtil.patternA));
		vo.setAttEndDisplayTime(DateUtil.getInstance().dateToString(
				vo.getAttEndTime(), DateUtil.patternA));
        vo.setLanguage(prof.cur_lan);
		setEndDate(vo);

        Map data = new HashMap<String,Object>();
        if(vo.getResultDataStatistic() == 0){
            data = learnerReportService.getLearnerReportByCourse(vo);//"统计数据以课程为主";
       }else{
           data = learnerReportService.getLearnerReportByUser(vo);//"统计数据以学员为主";
       }
		mav.addObject("reportData", data);
		mav.addObject("choseCondition", vo);

		session.setAttribute(CHOSE_CONDITION_SESSION_KEY, vo);
		String voJsonString = JSON.toJSONString(vo);
		if(voJsonString != null){		
			voJsonString = StringUtils.escapeJsonUtils(voJsonString);
		}
		mav.addObject("voJsonString", voJsonString);
		return mav;
	}

	/**
	 * jsp数据导出报表
	 * 
	 * @param prof
	 * @param vo
	 * @param wizbini
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("exporJsp")
	@HasPermission(AclFunction.FTN_AMD_TRAINING_REPORT_MGT)
	@ResponseBody
	public Model exporJsp(loginProfile prof, LearnerReportParamVo vo,
			WizbiniLoader wizbini, Model model, String voJsonString, Long rsp_id)
			throws Exception {
		if (null != voJsonString && voJsonString.length() > 0) {
			vo = JSON.parseObject(voJsonString, LearnerReportParamVo.class);
		}
		ReportTemplate reportTemplate = new ReportTemplate();
		ReportSpec reportSpec = reportSpecService.selectReportSpecId(rsp_id);
		Hashtable spec_pairs = reportTemplate.getSpecPairs(reportSpec
				.getRsp_xml());
		learnerReportService.loadInfo( vo,spec_pairs);

		vo.setRoleTcInd(AccessControlWZB.isRoleTcInd(prof.current_role));
		vo.setUserRole(prof.current_role);
		vo.setMyTopTcId(prof.my_top_tc_id);
		vo.setUserEntId(prof.usr_ent_id);
		vo.setLanguage(prof.cur_lan);
		vo.setAppnStartDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnStartDatetime(), DateUtil.patternA));
		vo.setAppnEndDisplayDatetime(DateUtil.getInstance().dateToString(
				vo.getAppnEndDatetime(), DateUtil.patternA));
		vo.setAttStartDispalyTime(DateUtil.getInstance().dateToString(
				vo.getAttStartTime(), DateUtil.patternA));
		vo.setAttEndDisplayTime(DateUtil.getInstance().dateToString(
				vo.getAttEndTime(), DateUtil.patternA));
		setEndDate(vo);
		String fileName = learnerReportService.expor(prof, vo, wizbini);
		model.addAttribute("fileUri", "/temp/" + fileName);
		return model;
        
	}

	private void setEndDates(LearnerReportParamVo vo) {
		if (null != vo) {
			if (null != vo.getAppnEndDatetime()) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(vo.getAppnEndDatetime());
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				vo.setAppnEndDatetime(cal.getTime());
			}
			if (null != vo.getAttEndTime()) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(vo.getAttEndTime());
				cal.set(Calendar.HOUR_OF_DAY, 23);
				cal.set(Calendar.MINUTE, 59);
				cal.set(Calendar.SECOND, 59);
				vo.setAttEndTime(cal.getTime());
			}
		}
	}

	@RequestMapping("update")
	@HasPermission(AclFunction.FTN_AMD_TRAINING_REPORT_MGT)
	public ModelAndView update(ModelAndView mav,loginProfile prof,
			HttpSession session, Long rsp_id)
			throws DataNotFoundException, ParseException {
		mav.addObject("language", prof.getCur_lan());
		mav = new ModelAndView("admin/learnerReport/UpdateIndex");
		//LearnerReportParamVo vo = (LearnerReportParamVo) session.getAttribute(CHOSE_CONDITION_SESSION_KEY);
        LearnerReportParamVo vo = new LearnerReportParamVo();
		String voJsonString = JSON.toJSONString(vo);
		
		ReportTemplate reportTemplate = new ReportTemplate();
		ReportSpec reportSpec = reportSpecService.selectReportSpecId(rsp_id);
		Hashtable spec_pairs = reportTemplate.getSpecPairs(reportSpec.getRsp_xml());
		Map infoData =learnerReportService.loadInfo(vo,spec_pairs);
       
		//转义'
		Object pageExportCourseIdsText = infoData.get("pageExportCourseIdsText") == null ? null : StringUtils.escapeUtils(infoData.get("pageExportCourseIdsText").toString());
		Object pageExportUserIdsText = infoData.get("pageExportUserIdsText") == null ? null : StringUtils.escapeUtils(infoData.get("pageExportUserIdsText").toString());

        mav.addObject("pageResultDataStatistic", infoData.get("pageResultDataStatistic"));
        mav.addObject("pageAppnStartDatetime", infoData.get("pageAppnStartDatetime"));
        mav.addObject("pageAppnEndDatetime",  infoData.get("pageAppnEndDatetime"));
        mav.addObject("pageAttStartTime",  infoData.get("pageAttStartTime"));
        mav.addObject("pageAttEndTime",  infoData.get("pageAttEndTime"));
        mav.addObject("pageCourseType",  infoData.get("pageCourseType"));
        mav.addObject("pageExportCourse",  infoData.get("pageExportCourse"));
        mav.addObject("pageExportCourseIds",  infoData.get("pageExportCourseIds"));
        mav.addObject("pageExportCourseIdsText",  pageExportCourseIdsText);
        mav.addObject("pageExportUser",  infoData.get("pageExportUser"));
        mav.addObject("pageExportUserIds",  infoData.get("pageExportUserIds"));
        mav.addObject("pageExportUserIdsText",  pageExportUserIdsText);
        mav.addObject("pageIsExportDetail",  infoData.get("pageIsExportDetail"));
        mav.addObject("pageUserInfo",  infoData.get("pageUserInfo"));
        mav.addObject("pageCourseInfo",  infoData.get("pageCourseInfo"));
        mav.addObject("pageOtherInfo",  infoData.get("pageOtherInfo"));
        mav.addObject("pageAppStatus",  infoData.get("pageAppStatus"));
        mav.addObject("pageCourseStatus",  infoData.get("pageCourseStatus"));
        mav.addObject("reportSpec", reportSpec);
		mav.addObject("voJsonString", voJsonString);
		return mav;
	}

	/**
	 * form表单提交 Date类型数据绑定
	 * 
	 * @param binder
	 * @see [类、类#方法、类#成员]
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

}
