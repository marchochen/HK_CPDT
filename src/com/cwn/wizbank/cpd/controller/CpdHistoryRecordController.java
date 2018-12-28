package com.cwn.wizbank.cpd.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.CpdGroupRegHoursHistoryService;
import com.cwn.wizbank.cpd.service.CpdOutstandingReportService;
import com.cwn.wizbank.cpd.service.CpdRegistrationMgtService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.entity.CpdGroupRegHoursHistory;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * cpd历史保存记录
 * 
 * @author Archer
 *
 */
@RequestMapping("admin/cpdHistoryRecord")
@Controller
public class CpdHistoryRecordController {

    @Autowired
    CpdRegistrationMgtService cpdRegistrationMgtService;
    
    @Autowired
    CpdUtilService cpdUtilService;

    @Autowired
    CpdGroupRegHoursHistoryService cpdGroupRegHoursHistoryService;
    

    @Autowired
    CpdOutstandingReportService cpdOutstandingReportService;
    

    @RequestMapping("index")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String index(loginProfile prof,@RequestParam(value = "ct_id", required = false, defaultValue = "0") long ct_id,Model model) throws MessageException {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int period = c.get(Calendar.YEAR);
        
        List<CpdGroupRegHoursHistory> cpdGroupRegHoursHistoriePeriods = cpdOutstandingReportService.getCpdGroupRegHoursHistoryPeriod();

        model.addAttribute("periods",cpdGroupRegHoursHistoriePeriods);
        model.addAttribute("cpdTypeList",cpdRegistrationMgtService.getCpdType());
        return "admin/cpd/cpd_history_record";
    }
    
    @RequestMapping("listJson")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String listJson(loginProfile prof, Model model, Page<CpdGroupRegHoursHistory> page,
            @RequestParam(value = "ct_id", required = false, defaultValue = "0") long ct_id,
            @RequestParam(value = "period", required = false, defaultValue = "0") int period,
            @RequestParam(value = "searchText", required = false)String searchText) {
        cpdGroupRegHoursHistoryService.getHistoryView(page, searchText, ct_id, period);
        return JsonFormat.format(model, page);
    }



}
