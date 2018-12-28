package com.cwn.wizbank.cpd.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.cpd.service.CpdGroupRegistrationService;
import com.cwn.wizbank.cpd.service.CpdRegistrationMgtService;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * cpd小牌注册记录
 * 
 * @author Nat
 *
 */
@RequestMapping("admin/cpdGroupRegistration")
@Controller
public class CpdGroupRegistrationController {

    @Autowired
    CpdGroupRegistrationService cpdGroupRegistrationService;
    
    @Autowired
    CpdRegistrationMgtService cpdRegistrationMgtService;

    
    @RequestMapping("listJson")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String listJson(loginProfile prof, Model model, Page<CpdGroupRegistration> page,
            @RequestParam(value = "cgr_usr_ent_id", required = false, defaultValue = "0") long cgr_usr_ent_id,
            @RequestParam(value = "ct_id", required = false, defaultValue = "0") long ct_id,
            @RequestParam(value = "cgr_cr_id", required = false, defaultValue = "0")long cgr_cr_id) {
    	
    	cpdGroupRegistrationService.getByUsrEntIdAndCrId(page,cgr_usr_ent_id,cgr_cr_id,ct_id);
        return JsonFormat.format(model, page);
    }
    
    @RequestMapping(value = "updateGroupRegHours")
	@HasPermission(AclFunction.FTN_AMD_CPT_D_LIST)
	@ResponseBody
	public String updateGroupRegHours(Model model, loginProfile prof,
			 @RequestParam(value = "core_hours", required = false, defaultValue = "0") Float core_hours,
	         @RequestParam(value = "non_core_hours", required = false, defaultValue = "0")Float non_core_hours,
	         @RequestParam(value = "cgrh_id", required = true )long cgrh_id,
	         @RequestParam(value = "cg_id", required = false, defaultValue = "0" )long cg_id,
	         @RequestParam(value = "cr_id", required = false, defaultValue = "0" )long cr_id ) throws Exception {
    	
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			cpdGroupRegistrationService.updateGroupRegHours(prof,core_hours,non_core_hours,cgrh_id);
			map.put("success",true);
			
            try {
                CpdRegistration cpdRegistration = cpdRegistrationMgtService.getDetail(cr_id);
                CpdGroup cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cg_id);
                ObjectActionLog log = new ObjectActionLog();
                log.setObjectId(cg_id);//小牌ID
                log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
                log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
                        +"-->"+cpdGroup.getCg_alias());//标题、全名
                log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
                log.setObjectAction(ObjectActionLog.OBJECT_ACTION_UPD);
                log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
                log.setObjectOptUserId(prof.getUsr_ent_id());
                log.setObjectActionTime(DateUtil.getCurrentTime());
                log.setObjectOptUserLoginTime(prof.login_date);
                log.setObjectOptUserLoginIp(prof.ip);
                SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
            } catch (Exception e) {
                CommonLog.error("CPT/D Group Registration  exception:"+e.getMessage(),e);
                // TODO: handle exception
            }
            
		} catch (Exception e) {
			map.put("success",false);
		}
		
		return JSON.toJSONString(map);
	}
    

}
