package com.cwn.wizbank.cpd.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.cwn.wizbank.cpd.service.CpdGroupService;
import com.cwn.wizbank.cpd.service.CpdIndividualReportService;
import com.cwn.wizbank.cpd.service.CpdRegistrationMgtService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.CpdGroupRegHoursHistory;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SnsValuationLogService;

@RequestMapping("admin/cpdIndividualReport")
@Controller
public class CpdIndividualReportTAController {
    
    @Autowired
    CpdIndividualReportService cpdIndividualReportService;
    
    @Autowired
    CpdRegistrationMgtService cpdRegistrationMgtService;
    
    @Autowired
    CpdUtilService cpdUtilService;

    @Autowired
    RegUserService regUserService;


    @Autowired
    ForCallOldAPIService forCallOldAPIService;
    
    @Autowired
    SnsValuationLogService snsValuationLogService;
    
	@Autowired
	CpdGroupService cpdGroupService;

    
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView index(Model model,loginProfile prof,ModelAndView mav, 
			HttpServletRequest request) throws Exception {
        mav = new ModelAndView("admin/cpd/cpd_individual_report");
        
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
    @RequestMapping(method = RequestMethod.POST, value = "cpdIndividualReportPdf")
    public Model exoprtReportPdf(Model model, loginProfile prof,WizbiniLoader wizbini,
            @RequestParam(value = "period", required = false, defaultValue = "0") int period,
            @RequestParam(value = "exportUser", required = false, defaultValue = "0") int exportUser,
            @RequestParam(value = "cghiCtIdArray", required = false) Long[] cghiCtIdArray,
            @RequestParam(value = "userId", required = false) Long[] userId,
            @RequestParam(value = "groupId", required = false) Long[] groupId,
            @RequestParam(value = "formatType", required = false, defaultValue = "0") int formatType,
            @RequestParam(value = "sortType", required = false, defaultValue = "0") int sortType
    		) throws Exception {

    	List<RegUser> userList = regUserService.getUserListForCpdTa(exportUser,userId,groupId,prof.getUsr_ent_id()
    			,prof.getCurrent_role(),prof.my_top_tc_id,cghiCtIdArray[0],period);
    	if(null!=userList && userList.size()>0){
    		model.addAttribute("regUser",userList.size());
    	}else{
    		model.addAttribute("regUser",0);
    	}
    	String download_pdf = cpdIndividualReportService.expor(userList, cghiCtIdArray, period, prof.cur_lan, wizbini, formatType, sortType,null,0);
        model.addAttribute("fileUri","/temp/"+download_pdf);
        return model;
    }
}
