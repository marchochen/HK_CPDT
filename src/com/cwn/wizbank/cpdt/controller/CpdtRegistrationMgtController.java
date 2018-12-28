package com.cwn.wizbank.cpdt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpdt.service.CpdtRegistrationMgtService;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;

/**
 * cpdt牌照注册管理
 * @author jasper
 *
 */
@RequestMapping("admin/cpdtRegistrationMgt")
@Controller
public class CpdtRegistrationMgtController {
	
    @Autowired
    CpdtRegistrationMgtService cpdtRegistrationMgtService;
	
    @RequestMapping("list")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String index(loginProfile prof,@RequestParam(value = "ct_id", required = false, defaultValue = "0") long ct_id,Model model) throws MessageException {
        model.addAttribute("cpdTypeList",cpdtRegistrationMgtService.getCpdtType());
        return "admin/cpd/registration_mtg_new";
    }

}
