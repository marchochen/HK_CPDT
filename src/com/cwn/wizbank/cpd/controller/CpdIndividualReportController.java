package com.cwn.wizbank.cpd.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.CpdGroupService;
import com.cwn.wizbank.cpd.service.CpdIndividualReportService;
import com.cwn.wizbank.cpd.service.CpdRegistrationMgtService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.CpdGroupRegHoursHistory;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.LearningSituation;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.LearningSituationService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SnsAttentionService;
import com.cwn.wizbank.services.SnsSettingService;
import com.cwn.wizbank.services.SnsValuationLogService;
import com.cwn.wizbank.services.UserCreditsService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;

@RequestMapping("cpdIndividualReport")
@Controller
public class CpdIndividualReportController {
    
    @Autowired
    CpdIndividualReportService cpdIndividualReportService;
    
    @Autowired
    CpdRegistrationMgtService cpdRegistrationMgtService;
    
    @Autowired
    CpdUtilService cpdUtilService;
    
    @Autowired
    UserCreditsService userCreditsService;

    @Autowired
    RegUserService regUserService;
    
    @Autowired
    SnsAttentionService snsAttentionService;

    @Autowired
    ForCallOldAPIService forCallOldAPIService;
    
    @Autowired
    SnsValuationLogService snsValuationLogService;
    
	@Autowired
	CpdGroupService cpdGroupService;
	
	@Autowired
	AclService aclService;
	
	@Autowired
	SnsSettingService snsSettingService;
	
	@Autowired
	LearningSituationService learningSituationService;

	@RequestMapping(value = "cpdIndividualReport", method = RequestMethod.GET)
	public ModelAndView toIndividualReport(Model model,loginProfile prof,ModelAndView mav, 
			HttpServletRequest request) throws Exception {
        mav = new ModelAndView("personal/personalCpdIndividualReport");
        mav.addObject("command", "cpdIndividualReport");
        
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
	    
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int period = c.get(Calendar.YEAR);
        List<CpdType> cpdType =  cpdRegistrationMgtService.getCpdType();
        List<CpdGroupRegHoursHistory> cpdGroupRegHoursHistoriePeriods = cpdUtilService.getCpdGroupRegHoursHistoryPeriod();
        List cpdTypeList = new ArrayList();
        if(null!=cpdGroupRegHoursHistoriePeriods && cpdGroupRegHoursHistoriePeriods.size()>0){
            for(int i = 0;i<cpdType.size();i++){
                CpdPeriodVO cpdPeriodVO = cpdUtilService.getPeriodByYear(prof,cpdGroupRegHoursHistoriePeriods.get(0).getCghi_period(),cpdType.get(i).getCt_id()); 
                cpdType.get(i).setCpdPeriodVO(cpdPeriodVO);
            }
        }
        CpdGroupRegHoursHistory cpdGroupRegHoursHistory = new CpdGroupRegHoursHistory();
        cpdGroupRegHoursHistory.setCghi_period(period);
        
        boolean check_period = true;
        for(int i = 0; i < cpdGroupRegHoursHistoriePeriods.size(); i++){
			if(null != cpdGroupRegHoursHistoriePeriods.get(i).getCghi_period() && cpdGroupRegHoursHistoriePeriods.get(i).getCghi_period() == period){
				check_period = false;
			}
		}
		if(check_period){
			cpdGroupRegHoursHistoriePeriods.add(0, cpdGroupRegHoursHistory);//增加个当前年份的记录
		}
        
	    model.addAttribute("cpdType",cpdType);
        model.addAttribute("periods",cpdGroupRegHoursHistoriePeriods);
        model.addAttribute("cpdTypeList",cpdTypeList);
        return mav;
	}
	
	//评估年份修改时，变换大牌周期
    @RequestMapping(method = RequestMethod.POST, value = "getcpdTypeRegHistory")
    public String getcpdTypeRegHistory(Model model, loginProfile prof,
            @RequestParam(value = "period", required = false, defaultValue = "0") int period) throws Exception {
        List<CpdType> cpdType =  cpdRegistrationMgtService.getCpdType();
        for(int i = 0;i<cpdType.size();i++){
            CpdPeriodVO cpdPeriodVO = cpdUtilService.getPeriodByYear(prof,period,cpdType.get(i).getCt_id()); 
            cpdType.get(i).setCpdPeriodVO(cpdPeriodVO);
        }
        model.addAttribute("cpdType",cpdType);
        return JsonFormat.format(model);
    }
    
    //导出Excel
    @RequestMapping(method = RequestMethod.POST, value = "cpdIndividualReportPdf")
    public Model cpdIndividualReportPdf(Model model, loginProfile prof,WizbiniLoader wizbini,	
            @RequestParam(value = "period", required = false, defaultValue = "0") int period,
            @RequestParam(value = "exportUser", required = false, defaultValue = "0") int exportUser,
            @RequestParam(value = "cghiCtIdArray", required = false) Long[] cghiCtIdArray,
            @RequestParam(value = "usertIdArray", required = false) Long[] usertIdArray,
            @RequestParam(value = "formatType", required = false, defaultValue = "0") int formatType,
            @RequestParam(value = "sortType", required = false, defaultValue = "0") int sortType
    		) throws Exception {
    	long ct_id = 0;
    	if(null!=cghiCtIdArray && cghiCtIdArray.length>0){
    		ct_id = cghiCtIdArray[0];
    	}
    	List<RegUser> subordinates = cpdIndividualReportService.getSubordinates(usertIdArray, exportUser, wizbini.cfgSysSetupadv.isTcIndependent(), prof, period ,ct_id );
    	String download_pdf = cpdIndividualReportService.expor(subordinates, cghiCtIdArray, period, prof.cur_lan, wizbini, formatType, sortType,null,0);
    	if(null!=subordinates && subordinates.size()>0){
    		model.addAttribute("regUser",subordinates.size());
    	}else{
    		model.addAttribute("regUser",0);
    	}
    	model.addAttribute("fileUri","/temp/"+download_pdf);
        return model;
    }
    
	@RequestMapping(value = "personalIndividualReport/{encUsrEntId}", method = RequestMethod.GET)
	public ModelAndView learnerIndividualReport(Model model,loginProfile prof,ModelAndView mav, 
			HttpServletRequest request,
			@PathVariable(value = "encUsrEntId") String encUsrEntId) throws Exception {
		
		//long usrEntId = EncryptUtil.cwnDecrypt(encUsrEntId);
		long usrEntId = prof.usr_ent_id;
        mav = new ModelAndView("personal/learnerIndividualReport");
        mav.addObject("command", "cpdIndividualReport");
      //是否以培训管理员角色进入
  		boolean isTADM = aclService.hasAnyPermission(prof.current_role, new String []{AclFunction.FTN_AMD_Q_AND_A_MAIN});
  		mav.addObject("isTADM", isTADM);
  		if(isTADM){
			mav.addObject("isMeInd", false);
			mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, usrEntId));
		} else {
			//获取权限设置
			mav.addObject("snsSetting", snsSettingService.getSnsSetting(prof.usr_ent_id, usrEntId));
			mav.addObject("usrEntId", usrEntId);
			mav.addObject("encUsrEntId", EncryptUtil.cwnEncrypt(usrEntId));
			//是否是进入本人页面
			mav.addObject("isMeInd", prof.usr_ent_id == usrEntId);
			//总积分
			mav.addObject("total_credits", userCreditsService.getUserTotalCredits(usrEntId, "all"));
			//赞数
			mav.addObject("likes", snsValuationLogService.getUserLikeTotal(usrEntId));
			//关注数
			mav.addObject("attent", snsAttentionService.getSnsAttentiontotal(usrEntId, "attent"));
			//粉丝数
			mav.addObject("fans", snsAttentionService.getSnsAttentiontotal(usrEntId, "fans"));
			//获取用户信息
			mav.addObject("regUser", regUserService.getUserDetail(prof.usr_ent_id, usrEntId));
			//个人学分
		    mav.addObject("learn_credits", learningSituationService.getUserLearnCredits(prof.usr_ent_id));
		}
        
        
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int period = c.get(Calendar.YEAR);
        List<CpdType> cpdType =  cpdRegistrationMgtService.getCpdType();
        List<CpdGroupRegHoursHistory> cpdGroupRegHoursHistoriePeriods = cpdUtilService.getCpdGroupRegHoursHistoryPeriod();
        List cpdTypeList = new ArrayList();
        if(null!=cpdGroupRegHoursHistoriePeriods && cpdGroupRegHoursHistoriePeriods.size()>0){
            for(int i = 0;i<cpdType.size();i++){
                CpdPeriodVO cpdPeriodVO = cpdUtilService.getPeriodByYear(prof,cpdGroupRegHoursHistoriePeriods.get(0).getCghi_period(),cpdType.get(i).getCt_id()); 
                cpdType.get(i).setCpdPeriodVO(cpdPeriodVO);
            }
        }
        CpdGroupRegHoursHistory cpdGroupRegHoursHistory = new CpdGroupRegHoursHistory();
        cpdGroupRegHoursHistory.setCghi_period(period);
        boolean check_period = true;
        for(int i = 0; i < cpdGroupRegHoursHistoriePeriods.size(); i++){
			if(null != cpdGroupRegHoursHistoriePeriods.get(i).getCghi_period() && cpdGroupRegHoursHistoriePeriods.get(i).getCghi_period() == period){
				check_period = false;
			}
		}
		if(check_period){
			cpdGroupRegHoursHistoriePeriods.add(0, cpdGroupRegHoursHistory);//增加个当前年份的记录
		}
        
        model.addAttribute("cpdType",cpdType);
        model.addAttribute("periods",cpdGroupRegHoursHistoriePeriods);
        model.addAttribute("cpdTypeList",cpdTypeList);
        return mav;
	}
    
	
    //导出Excel
    @RequestMapping(method = RequestMethod.POST, value = "personalIndividualReportPdf")
    public Model personalIndividualReportPdf(Model model, loginProfile prof,WizbiniLoader wizbini,
            @RequestParam(value = "period", required = false, defaultValue = "0") int period,
            @RequestParam(value = "cghiCtIdArray", required = false) Long[] cghiCtIdArray,
            @RequestParam(value = "exportUserId", required = false) Long exportUserId,
            @RequestParam(value = "sortType", required = false, defaultValue = "0") int sortType
    		) throws Exception {
    	
    	List<RegUser>  userList = new ArrayList<RegUser>();
    	if(cpdIndividualReportService.isNotPastCpdReg(exportUserId, period, cghiCtIdArray)){
            userList.add(regUserService.get(exportUserId));
    	}
    	String download_pdf = cpdIndividualReportService.expor(userList, cghiCtIdArray, period, prof.cur_lan, wizbini
    			,CpdIndividualReportService.FORMAT_TYPE_IN_ONE , sortType,null,0);
        if(null!=userList && userList.size()>0){
            model.addAttribute("regUser",userList.size());
        }else{
            model.addAttribute("regUser",0);
        }
        model.addAttribute("fileUri","/temp/"+download_pdf);
        return model;
    }
    
}
