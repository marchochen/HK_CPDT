package com.cwn.wizbank.cpd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.CpdReportRemarkService;
import com.cwn.wizbank.cpd.vo.CpdReportRemarkVO;
import com.cwn.wizbank.entity.CpdReportRemark;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;

@RequestMapping("admin/cpdReportRemark")
@Controller
public class CpdReportRemarkController {
	
	@Autowired
	CpdReportRemarkService cpdReportRemarkService;

    @RequestMapping("list")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_NOTE)
    public ModelAndView index(loginProfile prof,ModelAndView mav) throws MessageException {
    	mav = new ModelAndView("admin/cpd/cpd_report_remark_list");
    	List<CpdReportRemark> list = cpdReportRemarkService.findAll();
    	if(null!=list){
    		for(CpdReportRemark remark : list){
    			if(CpdReportRemark.AWARDED_REMARK_CODE.equalsIgnoreCase(remark.getCrpm_report_code())){
    				mav.addObject(CpdReportRemark.AWARDED_REMARK_CODE, remark.getCrpm_report_remark());
    			}else if(CpdReportRemark.INDIVIDUAL_REMARK_CODE.equalsIgnoreCase(remark.getCrpm_report_code())){
    				mav.addObject(CpdReportRemark.INDIVIDUAL_REMARK_CODE, remark.getCrpm_report_remark());
    			}else if(CpdReportRemark.LICENSE_REGISTRATION_CODE.equalsIgnoreCase(remark.getCrpm_report_code())){
    				mav.addObject(CpdReportRemark.LICENSE_REGISTRATION_CODE, remark.getCrpm_report_remark());
    			}else if(CpdReportRemark.OUTSTANDING_REMARK_CODE.equalsIgnoreCase(remark.getCrpm_report_code())){
    				mav.addObject(CpdReportRemark.OUTSTANDING_REMARK_CODE, remark.getCrpm_report_remark());
    			}
    		}
    	}
        return mav;
    }
    
    @RequestMapping("save")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_NOTE)
    public String save(loginProfile prof,Model model,CpdReportRemarkVO remarks) throws MessageException {
    	cpdReportRemarkService.saveFromCpdReportRemarkVO(remarks,prof.usr_ent_id);
        return "redirect:list";
    }
    
}
