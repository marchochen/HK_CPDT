package com.cwn.wizbank.cpd.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.CpdOutstandingReportService;
import com.cwn.wizbank.cpd.service.CpdRegistrationMgtService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.CpdGroupRegHoursHistory;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.LearningSituationService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SnsAttentionService;
import com.cwn.wizbank.services.SnsValuationLogService;
import com.cwn.wizbank.services.UserCreditsService;
import com.cwn.wizbank.utils.JsonFormat;

import icepdf.in;

@RequestMapping("cpdOutstandingReport")
@Controller
public class CpdOutstandingReportController {
    
    @Autowired
    CpdOutstandingReportService cpdOutstandingReportService;
    
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
	LearningSituationService learningSituationService;
    
    
    //进入CPT/D Outstanding Hours Report 页面
	@RequestMapping(value = "cpdOutstandingReport", method = RequestMethod.GET)
	public ModelAndView toSp(Model model,loginProfile prof,ModelAndView mav, 
			HttpServletRequest request) throws Exception {
        mav = new ModelAndView("personal/personalCpdOutstandingReport");
        mav.addObject("command", "cpdOutstandingReport");
        
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
	    //List<CpdGroupRegHoursHistory> cpdGroupRegHoursHistories = cpdOutstandingReportService.getCpdGroupRegHoursHistory(prof.usr_ent_id,period);
        List<CpdGroupRegHoursHistory> cpdGroupRegHoursHistoriePeriods = cpdOutstandingReportService.getCpdGroupRegHoursHistoryPeriod();
        
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
        
        List cpdTypeList = new ArrayList();
        if(null!=cpdGroupRegHoursHistoriePeriods && cpdGroupRegHoursHistoriePeriods.size()>0){
            for(int i = 0;i<cpdType.size();i++){
                CpdPeriodVO cpdPeriodVO = cpdUtilService.getPeriodByYear(prof,cpdGroupRegHoursHistoriePeriods.get(0).getCghi_period(),cpdType.get(i).getCt_id()); 
                cpdType.get(i).setCpdPeriodVO(cpdPeriodVO);
            }
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
    @RequestMapping(method = RequestMethod.POST, value = "cpdOutstandingReportExcel")
    public Model cpdOutstandingReportExcel(Model model, loginProfile prof,WizbiniLoader wizbini,
            @RequestParam(value = "period", required = false, defaultValue = "0") int period,
            @RequestParam(value = "exportUser", required = false, defaultValue = "0") int exportUser,
            @RequestParam(value = "cghiCtIdArray", required = false) long[] cghiCtIdArray,
            @RequestParam(value = "usertIdArray", required = false) long[] usertIdArray) throws Exception {
        long[] usr_ent_id = cpdOutstandingReportService.getUserForExcel(usertIdArray, exportUser, wizbini, prof);
        Map<Integer, List> map =  cpdOutstandingReportService.excelData(usr_ent_id, cghiCtIdArray, exportUser, period,wizbini,prof);
        String fileName = cpdOutstandingReportService.expor(prof,wizbini,map,cghiCtIdArray,period,null,0);
        model.addAttribute("fileUri","/temp/"+fileName);
        return model;
    }
    
    
}
