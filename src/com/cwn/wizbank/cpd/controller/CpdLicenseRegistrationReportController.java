package com.cwn.wizbank.cpd.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.CpdLicenseRegistrationReportService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.CpdType;


/**
 * CPT/D报表 (学员牌照注册报表) License Registration Report
 * @author 
 *
 */
@RequestMapping("admin/cpdLicenseRegistrationReport")
@Controller
public class CpdLicenseRegistrationReportController {
    
	@Autowired
	CpdUtilService cpdUtilService;
	
    @Autowired
    CpdLicenseRegistrationReportService cpdLicenseRegistrationReportService;
    
    
	/**
	 * 进入报表条件页面
	 * @param model
	 * @param prof
	 * @param mav
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "")
	public ModelAndView toSp(Model model,loginProfile prof,ModelAndView mav, 
			HttpServletRequest request) throws Exception {
       
		mav = new ModelAndView("admin/cpd/cpd_license_registration_report");
	    //List<Integer> yearlist = cpdUtilService.getReportYear();
	    List<CpdType> cpdTypelist =  cpdUtilService.getReportTypeByNowDate(prof);
		
	    //mav.addObject("year", yearlist);
	    mav.addObject("typelist", cpdTypelist);
        return mav;
	}
    
	/**
	 * 导出报表
	 * @param prof
	 * @param cghi
	 * @param wizbini
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("export")
	@ResponseBody
	public Model export(loginProfile prof, CpdRegistration cr, WizbiniLoader wizbini
			,Model model)throws Exception{
		String fileName = cpdLicenseRegistrationReportService.export(prof,cr,wizbini);
		model.addAttribute("fileUri","/temp/"+fileName);
		return model;
	}
	
	
	/**
	 * 通过周期获得当前groupType
	 * @param prof
	 * @param period
	 * @param wizbini
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getGroupTypeByPeriod")
	@ResponseBody
	public Model getGroupTypeByPeriod(loginProfile prof,Integer period, WizbiniLoader wizbini,Model model)throws Exception{
		
		List<CpdType> cpdTypelist =  cpdUtilService.getReportType(prof, period);
		model.addAttribute("cpdTypelist", cpdTypelist);
	        
		return model;
	}
	
}
