package com.cwn.wizbank.cpd.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;

import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwSysMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.cw.wizbank.batch.eLibBatch.utils;
import com.cwn.wizbank.cpd.service.CpdRegistrationMgtService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.cpd.utils.CpdUtils;
import com.cwn.wizbank.cpd.vo.CpdPeriodVO;
import com.cwn.wizbank.entity.CpdGroup;
import com.cwn.wizbank.entity.CpdGroupPeriod;
import com.cwn.wizbank.entity.CpdGroupRegistration;
import com.cwn.wizbank.entity.CpdRegistration;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.ImsLog;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.DateUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;
import com.google.common.collect.Lists;
import com.ibm.db2.jcc.a.c;
import com.opensymphony.module.sitemesh.tapestry.Util;
import com.cw.wizbank.config.WizbiniLoader;


/**
 * cpd牌照管理控制器
 * 
 * @author Nat
 *
 */
@RequestMapping("admin/cpdRegistrationMgt")
@Controller
public class CpdRegistrationMgtController {

    @Autowired
    CpdRegistrationMgtService cpdRegistrationMgtService;
    
    @Autowired
    CpdUtilService cpdUtilService;
    
    @Autowired
    RegUserService regUserService;
    

    @RequestMapping("list")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String index(loginProfile prof,@RequestParam(value = "ct_id", required = false, defaultValue = "0") long ct_id,Model model) throws MessageException {
        model.addAttribute("cpdTypeList",cpdRegistrationMgtService.getCpdType());
        return "admin/cpd/registration_mtg_new";
    }

    @RequestMapping("listJson")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String listJson(loginProfile prof, Model model, Page<CpdRegistration> page,
            @RequestParam(value = "ct_id", required = false, defaultValue = "0") long ct_id,
            @RequestParam(value = "searchText", required = false)String searchText) {
        cpdRegistrationMgtService.searchAll(page, prof,ct_id,searchText);
        return JsonFormat.format(model, page);
    }

    @RequestMapping(method = RequestMethod.GET, value = "insert")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String insert(Model model, @ModelAttribute CpdRegistration cpdRegistration) {
        List<CpdGroup> cpdGroupss =null;
        List<CpdType> cpdTypes = cpdRegistrationMgtService.getCpdType();
        if(cpdTypes!=null ){
            if(cpdTypes.size()>0){
                cpdGroupss = cpdRegistrationMgtService.getCpdGroupMap(cpdTypes.get(0).getCt_id());
            }
        }
        model.addAttribute("cpdTypeList",cpdTypes);
        model.addAttribute("cpdGroupList",cpdGroupss);
        return "admin/cpd/cpd_registration_add";
    }
    
    @RequestMapping(value = "importCPDRegistration")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String importCPDRegistration(Model model, @ModelAttribute CpdRegistration cpdRegistration) {
    	return "admin/cpd/cpd_registratioin_import";
    }
    
    @RequestMapping(value = "filesUpload")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String filesUpload(Model model,@RequestParam(value = "src_filename", required = true) MultipartFile file, 
    		@ModelAttribute CpdRegistration cpdRegistration,WizbiniLoader wizbini,loginProfile prof,qdbEnv static_env) throws cwSysMessage, IOException{
    	cpdRegistrationMgtService.cpdRegistrationMgtService(model,file,wizbini,prof,static_env,cpdRegistration);
    	return "admin/cpd/cpd_registratioin_import_record";
    }
    
    @RequestMapping(value = "upload_result")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String upload_result(Model model, @ModelAttribute CpdRegistration cpdRegistration,WizbiniLoader wizbini,loginProfile prof,qdbEnv static_env) throws IOException, cwException, NamingException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Map<String, Object> map = cpdRegistrationMgtService.upload_result(model,wizbini,prof,static_env,cpdRegistration);
        
        model.addAttribute("success_total", map.get("success_total"));
        model.addAttribute("unsuccess_total", map.get("unsuccess_total"));
        model.addAttribute("success_href", map.get("success_href"));
        
    	return "admin/cpd/cpd_registratioin_import_record_result";
    }
    
    @RequestMapping(value = "upload_model")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String upload_model(Model model, @ModelAttribute CpdRegistration cpdRegistration,WizbiniLoader wizbini,loginProfile prof,qdbEnv static_env) {
    	cpdRegistrationMgtService.upload_model(model,wizbini,prof,static_env,cpdRegistration);
    	return "admin/cpd/cpd_registratioin_import_model";
    }
    
    @RequestMapping(value = "upload_check")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String upload_check(Model model, @ModelAttribute CpdRegistration cpdRegistration,WizbiniLoader wizbini,loginProfile prof,qdbEnv static_env) {
//    	cpdRegistrationMgtService.upload_check(model,wizbini,prof,static_env,cpdRegistration);
    	return "admin/cpd/cpd_registration_import_log";
    }
    /**
     * 导入用户获得CPT/D时数日志页面
     * @param model
     * @return
     */
    @RequestMapping(value = "cpdRegistrationImportLog")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    public ModelAndView cpdHoursAwaedImportLog(ModelAndView mav,Model model,WizbiniLoader wizbini,loginProfile prof) {
        mav = new ModelAndView("/admin/cpd/cpd_registration_import_log");
        return mav;
    }
    
    /**
     * 导入用户获得CPT/D时数日志数据
     * @param model
     * @return
     */
    @RequestMapping("listJsonLog")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    @ResponseBody
    public String listJson(loginProfile prof, Model model, Page<ImsLog> page,qdbEnv static_env) {
    	cpdRegistrationMgtService.searchAll(page, prof,static_env);
        return JsonFormat.format(model, page);
    }
    
    @RequestMapping(value = "down_file")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String down_file(Model model, @ModelAttribute CpdRegistration cpdRegistration,WizbiniLoader wizbini,loginProfile prof,qdbEnv static_env) {
    	cpdRegistrationMgtService.upload_model(model,wizbini,prof,static_env,cpdRegistration);
    	return "admin/cpd/cpd_registratioin_import_check";
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "getCpdGroupAndType")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String getCpdGroupAndType(Model model, loginProfile prof,
            @RequestParam(value = "ct_id", required = false, defaultValue = "0") long ct_id) throws Exception {
        List<CpdGroup> cpdGroupss =null;
        cpdGroupss = cpdRegistrationMgtService.getCpdGroupMap(ct_id);
        CpdType cpdType = cpdRegistrationMgtService.getCpdTypeByid(ct_id);
        model.addAttribute("cpdGroupList",cpdGroupss);
        model.addAttribute("cpdType",cpdType);
        return JsonFormat.format(model);
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = "info")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String info(Model model, @ModelAttribute CpdRegistration cpdRegistration,
            @RequestParam(value = "cr_id", required = false, defaultValue = "0") long cr_id) {
        CpdRegistration cpdRegistration2 = cpdRegistrationMgtService.getDetail(cr_id);
        CpdPeriodVO  cpdPeriodVO=cpdUtilService.getCurrentPeriod(cpdRegistration2.getCr_ct_id());
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");  
        cpdPeriodVO.setEndDate(sdf.format(cpdPeriodVO.getEndTime()));
        model.addAttribute("detail",cpdRegistration2);
        model.addAttribute("endDate",cpdPeriodVO.getEndDate());
        return "admin/cpd/cpd_registration_info";
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = "update")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String update(Model model, @ModelAttribute CpdRegistration cpdRegistration,
            @RequestParam(value = "cr_id", required = false, defaultValue = "0") long cr_id,
            @RequestParam(value = "ct_id", required = false, defaultValue = "0") long ct_id,
            @RequestParam(value = "usr_ent_id", required = false, defaultValue = "0") long usr_ent_id) {
        List<CpdGroup> cpdGroupss =null;
        cpdGroupss = cpdRegistrationMgtService.getCpdGroupMap(ct_id);
        model.addAttribute("detail",cpdRegistrationMgtService.getDetail(cr_id));
        model.addAttribute("cList",cpdRegistrationMgtService.getUserGroupRegi(ct_id,cr_id,usr_ent_id));
        return "admin/cpd/cpd_registration_upd";
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = "updCpdRegistration")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String updCpdRegistration(Model model,loginProfile prof,
            @ModelAttribute CpdRegistration cpdRegistration) {
        
            boolean isExistForRegistration = false;
            boolean isInfoRegistrationBydate = false;
            isExistForRegistration = cpdRegistrationMgtService.isExistForRegistration(prof,cpdRegistration);//检查大牌报名记录是否重复
            if(!isExistForRegistration){
                isInfoRegistrationBydate = cpdRegistrationMgtService.isInfoRegistrationBydate(prof,cpdRegistration);//检查大牌报名记录时间是否重叠
                if(!isInfoRegistrationBydate){
                    cpdRegistrationMgtService.update(prof, cpdRegistration);  
                    model.addAttribute("cr_id", cpdRegistration.getCr_id());
                    model.addAttribute("status", 1);//1."更新大牌记录成功"
                    
                    try {
                        ObjectActionLog log = new ObjectActionLog();
                        log.setObjectId(cpdRegistration.getCr_ct_id());//大牌ID
                        log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
                        RegUser regUser = regUserService.getUserDetail(cpdRegistration.getCr_usr_ent_id());
                        CpdType cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
                        log.setObjectTitle(regUser.getUsr_ste_usr_id()+"-"+cpdType.getCt_license_alias());//标题、全名
                        log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
                        log.setObjectAction(ObjectActionLog.OBJECT_ACTION_UPD);
                        log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
                        log.setObjectOptUserId(prof.getUsr_ent_id());
                        log.setObjectActionTime(DateUtil.getCurrentTime());
                        log.setObjectOptUserLoginTime(prof.login_date);
                        log.setObjectOptUserLoginIp(prof.ip);
                        SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                    } catch (Exception e) {
                        CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
                        // TODO: handle exception
                    }
                    
                    
                    
                }else{
                    model.addAttribute("status", 3);//3."时间重叠"
                }
            }else{
                model.addAttribute("status", 2);//2."数据重复"
            }
        return  JSON.toJSONString(model);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "updCpdGroupRegistration" ,consumes="application/json")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String updCpdGroupRegistration(loginProfile prof,Model model, 
            @RequestBody List<CpdGroupRegistration> cpdGroupRegistrations) {
        long cgr_cr_id =  0;
        long cgr_cg_id =  0;
        List<CpdGroupRegistration> cpdGroupRegistrationList = null;//查询大牌下的小牌注册记录
        if(null!=cpdGroupRegistrations && cpdGroupRegistrations.size()>0){
            cgr_cr_id = cpdGroupRegistrations.get(0).getCgr_cr_id();
            cpdGroupRegistrationList =  cpdRegistrationMgtService.getGroupRegiByCrId(0,cgr_cr_id);
            for (int i = 0; i < cpdGroupRegistrations.size(); i++) {
                List<CpdGroupRegistration> cpdGroupRegistrationList1 = null;//查询大牌下的小牌注册记录（根据小牌ID）
                cgr_cg_id = cpdGroupRegistrations.get(i).getCgr_cg_id();
                cpdGroupRegistrationList1=  cpdRegistrationMgtService.getGroupRegiByCrId(cgr_cg_id,cgr_cr_id);
                if(cpdGroupRegistrationList1.size()==0){//该小牌没注册
                    //增加小牌注册记录
                    int cgr_first_ind = 0;
                    CpdGroupRegistration cpdGroupRegistration = cpdGroupRegistrations.get(i);
                    if(cpdUtilService.cpdIsFirstInd(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date())){
                        cgr_first_ind = 1;//是首次挂牌
                    }
                    //大牌注册信息
                    CpdRegistration cpdRegistration = new CpdRegistration();    
                    cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
                    //大牌信息
                    CpdType cpdType = new CpdType();
                    cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
                    //小牌信息
                    CpdGroup  cpdGroup = new CpdGroup();
                    cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
                    
                    CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
                    
                    
                    //上一个周期
                    CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistration(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date());
                    //实际开始时间
                    cpdGroupRegistration.setCgr_actual_date(cpdUtilService.getCgrActualDate(cpdGroupRegistration, preCpdGroupRegistration, cpdType));
                    
                    cpdGroupRegistration.setCgr_first_ind(cgr_first_ind);
                    cpdGroupRegistration.setCgr_create_usr_ent_id(prof.getUsr_ent_id());
                    cpdGroupRegistration.setCgr_create_datetime(cpdRegistrationMgtService.getDate());
                    cpdGroupRegistration.setCgr_update_usr_ent_id(prof.getUsr_ent_id());
                    cpdGroupRegistration.setCgr_update_datetime(cpdRegistrationMgtService.getDate());
                    cpdGroupRegistration.setCgr_status("OK");     
                    
                  //获取周期信息
                    CpdPeriodVO prePeriod = null;
                    if(preCpdGroupRegistration!=null){
                        prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
                    }
                    
                    //增加小牌注册记录
                    cpdRegistrationMgtService.insertGroupRegi( cpdGroupRegistration);

                    if(req_period!=null){//CpdGroupPeriod为null时不进行重算
                        //中间表记录变动计算方法
                        cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_ADD, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
                    }
                    
                    try {
                        ObjectActionLog log = new ObjectActionLog();
                        log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
                        log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
                        log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
                                +"-->"+cpdGroup.getCg_alias());//标题、全名
                        log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
                        log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
                        log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
                        log.setObjectOptUserId(prof.getUsr_ent_id());
                        log.setObjectActionTime(DateUtil.getCurrentTime());
                        log.setObjectOptUserLoginTime(prof.login_date);
                        log.setObjectOptUserLoginIp(prof.ip);
                        SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                    } catch (Exception e) {
                        CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
                        // TODO: handle exception
                    }
                    
                    }
            }
            
            List<Long> list = new ArrayList();//获取被删除的小牌cpd_cg_id
            List<Long> list1 = new ArrayList();//获取被修改或者新增的小牌
            if(!cpdGroupRegistrations.containsAll(cpdGroupRegistrationList)){//存在已注册的小牌被删除
                for (int i = 0; i < cpdGroupRegistrationList.size(); i++) {
                    CpdGroupRegistration cpdGroupRegistration =  cpdGroupRegistrationList.get(i);
                    boolean isRegi = true;//判断该小牌是否被删除
                    for (int j = 0; j < cpdGroupRegistrations.size(); j++) {
                        if(cpdGroupRegistrations.get(j).getCgr_cg_id().equals(cpdGroupRegistration.getCgr_cg_id())){
                            isRegi = false;//没被删除，进行修改
                            cpdGroupRegistration.setCgr_initial_date(cpdGroupRegistrations.get(j).getCgr_initial_date());
                            cpdGroupRegistration.setCgr_expiry_date(cpdGroupRegistrations.get(j).getCgr_expiry_date());
                            

                            //大牌注册信息
                            CpdRegistration cpdRegistration = new CpdRegistration();    
                            cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
                            //大牌信息
                            CpdType cpdType = new CpdType();
                            cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
                            //小牌信息
                            CpdGroup  cpdGroup = new CpdGroup();
                            cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
                            CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
                            
                            //上一个周期                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
                            CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistrationNoId(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date(),cpdGroupRegistration.getCgr_id());
                            //实际开始时间
                            cpdGroupRegistration.setCgr_actual_date(cpdUtilService.getCgrActualDate(cpdGroupRegistration, preCpdGroupRegistration, cpdType));
                            //获取周期信息
                            CpdPeriodVO prePeriod = null;
                            if(preCpdGroupRegistration!=null){
                                prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
                            }

                            //修改小牌注册信息
                            cpdRegistrationMgtService.updCpdGroupRegistration(prof,cpdGroupRegistration);
                            
                            if(req_period!=null){//CpdGroupPeriod为null时不进行重算
                                //中间表记录变动计算方法
                                cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_UPD, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
                            }
                            try {
                                ObjectActionLog log = new ObjectActionLog();
                                log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
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
                                CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
                                // TODO: handle exception
                            }
                           
                            break;
                        }
                    }
                    if(isRegi){//进行删除
                        //大牌注册信息
                        CpdRegistration cpdRegistration = new CpdRegistration();    
                        cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
                        //大牌信息
                        CpdType cpdType = new CpdType();
                        cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
                        //小牌信息
                        CpdGroup  cpdGroup = new CpdGroup();
                        cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
                        CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
                        //上一个周期
                        CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistration(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date());
                       //获取周期信息
                        CpdPeriodVO prePeriod = null;
                        if(preCpdGroupRegistration!=null){
                            prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
                        }

                        //删除注册信息
                        cpdRegistrationMgtService.delCpdGroupRegistration(prof, cpdGroupRegistration.getCgr_id(),0);
                        
                        //中间表记录变动计算方法
                        if(req_period!=null){//CpdGroupPeriod为null时不进行重算
                            cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_DEL, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
                        }
                        try {
                            ObjectActionLog log = new ObjectActionLog();
                            log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
                            log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
                            log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
                                    +"-->"+cpdGroup.getCg_alias());//标题、全名
                            log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
                            log.setObjectAction(ObjectActionLog.OBJECT_ACTION_DEL);
                            log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
                            log.setObjectOptUserId(prof.getUsr_ent_id());
                            log.setObjectActionTime(DateUtil.getCurrentTime());
                            log.setObjectOptUserLoginTime(prof.login_date);
                            log.setObjectOptUserLoginIp(prof.ip);
                            SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                        } catch (Exception e) {
                            CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
                            // TODO: handle exception
                        }
                        
                        list.add(cpdGroupRegistration.getCgr_cg_id());//被删除的小牌Id
                    }else{
                        list1.add(cpdGroupRegistration.getCgr_cg_id());//需要修改的小牌id
                    }
                }
            }else{//不存在被删除的小牌
                for (int i = 0; i < cpdGroupRegistrationList.size(); i++) {
                    CpdGroupRegistration cpdGroupRegistration =  cpdGroupRegistrationList.get(i);
                    for (int j = 0; j < cpdGroupRegistrations.size(); j++) {
                        if(cpdGroupRegistrations.get(j).getCgr_cg_id().equals(cpdGroupRegistration.getCgr_cg_id())){
                            cpdGroupRegistrations.get(j).setCgr_id(cpdGroupRegistration.getCgr_id());
                            cpdRegistrationMgtService.updCpdGroupRegistration(prof,cpdGroupRegistrations.get(j));
                            break;
                        }
                    }
                }
            }

            model.addAttribute("status", 1);
        }
        return JSON.toJSONString(model);
    }
    
    
    @RequestMapping(value = "delGroupRegistration", method = RequestMethod.POST)
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String delGroupRegistration(Model model, loginProfile prof,
            @RequestParam(value = "cgr_cr_id", required = false, defaultValue = "0") long cgrCrId  ) throws Exception {
        List<CpdGroupRegistration> CpdGroupRegistrationList = cpdRegistrationMgtService.getGroupRegiByCrId(0, cgrCrId);
        
        cpdRegistrationMgtService.delCpdGroupRegistration(prof, 0, cgrCrId);
        for (int i = 0; i < CpdGroupRegistrationList.size(); i++) {
            CpdGroupRegistration cpdGroupRegistration = CpdGroupRegistrationList.get(i);
          //大牌注册信息
            CpdRegistration cpdRegistration = new CpdRegistration();    
            cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
            //大牌信息
            CpdType cpdType = new CpdType();
            cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
            //小牌信息
            CpdGroup  cpdGroup = new CpdGroup();
            cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
            CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
            //上一个周期
            CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistration(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date());
           //获取周期信息
            CpdPeriodVO prePeriod = null;
            if(preCpdGroupRegistration!=null){
                prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
            }
            if(req_period!=null){//CpdGroupPeriod为null时不进行重算
              //中间表记录变动计算方法
                cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_DEL, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
            }
            try {
                ObjectActionLog log = new ObjectActionLog();
                log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
                log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
                log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
                        +"-->"+cpdGroup.getCg_alias());//标题、全名
                log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
                log.setObjectAction(ObjectActionLog.OBJECT_ACTION_DEL);
                log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
                log.setObjectOptUserId(prof.getUsr_ent_id());
                log.setObjectActionTime(DateUtil.getCurrentTime());
                log.setObjectOptUserLoginTime(prof.login_date);
                log.setObjectOptUserLoginIp(prof.ip);
                SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
            } catch (Exception e) {
                CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
                // TODO: handle exception
            }
            
            
        }
        
        return JSON.toJSONString(model);
    }
    
    
    @RequestMapping("delRegistration")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    public String delRegistration(loginProfile prof, Model model, 
            @RequestParam(value = "cr_id", required = false, defaultValue = "0") long cgrCrId) {
        CpdRegistration cpdRegistration1 = cpdRegistrationMgtService.getDetail(cgrCrId);
        cpdRegistrationMgtService.delCpdRegistration(prof,cgrCrId);
        try {
            ObjectActionLog log = new ObjectActionLog();
            log.setObjectId(cpdRegistration1.getCr_ct_id());//大牌ID
            log.setObjectCode(cpdRegistration1.getCr_reg_number());//注册号码
            log.setObjectTitle(cpdRegistration1.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration1.getCpdType().getCt_license_alias());//标题、全名
            log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
            log.setObjectAction(ObjectActionLog.OBJECT_ACTION_DEL);
            log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
            log.setObjectOptUserId(prof.getUsr_ent_id());
            log.setObjectActionTime(DateUtil.getCurrentTime());
            log.setObjectOptUserLoginTime(prof.login_date);
            log.setObjectOptUserLoginIp(prof.ip);
            SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
        } catch (Exception e) {
            CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
            // TODO: handle exception
        }
        
        List<CpdGroupRegistration> CpdGroupRegistrationList = cpdRegistrationMgtService.getGroupRegiByCrId(0, cgrCrId);
        
        cpdRegistrationMgtService.delCpdGroupRegistration(prof, 0, cgrCrId);
        for (int i = 0; i < CpdGroupRegistrationList.size(); i++) {
            CpdGroupRegistration cpdGroupRegistration = CpdGroupRegistrationList.get(i);
          //大牌注册信息
            CpdRegistration cpdRegistration = new CpdRegistration();    
            cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
            //大牌信息
            CpdType cpdType = new CpdType();
            if(cpdRegistration != null){
            	cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
            }
            //小牌信息
            CpdGroup  cpdGroup = new CpdGroup();
            cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
            CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
            //上一个周期
            CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistration(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date());
           //获取周期信息
            CpdPeriodVO prePeriod = null;
            if(preCpdGroupRegistration!=null){
                prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
            }
            if(req_period!=null){//CpdGroupPeriod为null时不进行重算
              //中间表记录变动计算方法
                cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_DEL, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
            }
            
            try {
                ObjectActionLog log = new ObjectActionLog();
                log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
                log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
                log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
                        +"-->"+cpdGroup.getCg_alias());//标题、全名
                log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
                log.setObjectAction(ObjectActionLog.OBJECT_ACTION_DEL);
                log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
                log.setObjectOptUserId(prof.getUsr_ent_id());
                log.setObjectActionTime(DateUtil.getCurrentTime());
                log.setObjectOptUserLoginTime(prof.login_date);
                log.setObjectOptUserLoginIp(prof.ip);
                SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
            } catch (Exception e) {
                CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
                // TODO: handle exception
            }
            
            
        }

        model.addAttribute("cpdTypeList",cpdRegistrationMgtService.getCpdType());
        return "admin/cpd/registration_mtg_new";
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = "hasUpdInitialDate" ,consumes="application/json")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String hasUpdInitialDate(loginProfile prof,Model model, 
            @RequestBody List<CpdGroupRegistration> cpdGroupRegistrations) {
        long cgr_cr_id =  0;
        List<CpdGroupRegistration> cpdGroupRegistrationList = null;
        String hasUpd = "";
        if(null!=cpdGroupRegistrations && cpdGroupRegistrations.size()>0){
            cgr_cr_id = cpdGroupRegistrations.get(0).getCgr_cr_id();
            cpdGroupRegistrationList =  cpdRegistrationMgtService.getGroupRegiByCrId(0, cgr_cr_id);
            List<Long> list = new ArrayList();
            List<Long> list1 = new ArrayList();
            List<String> listName = new ArrayList();//
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < cpdGroupRegistrationList.size(); i++) {
                CpdGroupRegistration cpdGroupRegistration =  cpdGroupRegistrationList.get(i);
                for (int j = 0; j < cpdGroupRegistrations.size(); j++) {
                    if(cpdGroupRegistrations.get(j).getCgr_cg_id().equals(cpdGroupRegistration.getCgr_cg_id())){
                        String initDate1 = format.format(cpdGroupRegistrations.get(j).getCgr_initial_date());
                        String initDate2 = format.format(cpdGroupRegistration.getCgr_initial_date());
                        if(initDate1.equals(initDate2)){
                            list.add(cpdGroupRegistrations.get(j).getCgr_cg_id()); //同一个小牌注册记录的开始时间相同，没修改，取得小牌id
                        }else{
                            list1.add(cpdGroupRegistrations.get(j).getCgr_cg_id());//同一个小牌注册记录的开始时间不相同，已修改，取得小牌id
                        }
                    }
                }
            }
            if(list1.size()>0){
                for (int i = 0; i < list1.size(); i++) {
                    hasUpd += cpdRegistrationMgtService.getCpdGroupById(list1.get(i)).getCg_alias() +"、";
                }
                if(hasUpd.length()>0){
                    hasUpd = hasUpd.substring(0, hasUpd.length()-1);
                    model.addAttribute("hasUpd", hasUpd);//开始日期有变动的小牌别名
                }
            }
        }
        return hasUpd;
        //return JSON.toJSONString(model);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "hasUpdExpiryDate" ,consumes="application/json")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String hasUpdExpiryDate(loginProfile prof,Model model, 
            @RequestBody List<CpdGroupRegistration> cpdGroupRegistrations) {
        long cgr_cr_id =  0;
        List<CpdGroupRegistration> cpdGroupRegistrationList = null;
        String hasUpd = "";
        if(null!=cpdGroupRegistrations && cpdGroupRegistrations.size()>0){
            cgr_cr_id = cpdGroupRegistrations.get(0).getCgr_cr_id();
            cpdGroupRegistrationList =  cpdRegistrationMgtService.getGroupRegiByCrId(0, cgr_cr_id);
            List<Long> list = new ArrayList();
            List<Long> list1 = new ArrayList();
            List<String> listName = new ArrayList();//
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < cpdGroupRegistrationList.size(); i++) {
                CpdGroupRegistration cpdGroupRegistration =  cpdGroupRegistrationList.get(i);
                for (int j = 0; j < cpdGroupRegistrations.size(); j++) {
                    if(cpdGroupRegistrations.get(j).getCgr_cg_id().equals(cpdGroupRegistration.getCgr_cg_id())){
                        if(cpdGroupRegistration.getCgr_expiry_date()!=null){
                            if(cpdGroupRegistrations.get(j).getCgr_expiry_date()==null){
                                list1.add(cpdGroupRegistrations.get(j).getCgr_cg_id());
                            }else{
                                String initDate1 = format.format(cpdGroupRegistrations.get(j).getCgr_expiry_date());
                                String initDate2 = format.format(cpdGroupRegistration.getCgr_expiry_date());
                                if(initDate1.equals(initDate2)){
                                    list.add(cpdGroupRegistrations.get(j).getCgr_cg_id()); //同一个小牌注册记录的开始时间相同，没修改，取得小牌id
                                }else{
                                    list1.add(cpdGroupRegistrations.get(j).getCgr_cg_id());//同一个小牌注册记录的开始时间不相同，已修改，取得小牌id
                                }
                            }
                            
                        }else{
                            if(cpdGroupRegistrations.get(j).getCgr_expiry_date()!=null){
                                list1.add(cpdGroupRegistrations.get(j).getCgr_cg_id());
                            }
                        }
                    }
                }
            }
            if(list1.size()>0){
                for (int i = 0; i < list1.size(); i++) {
                    hasUpd += cpdRegistrationMgtService.getCpdGroupById(list1.get(i)).getCg_alias() +"、";
                }
                if(hasUpd.length()>0){
                    hasUpd = hasUpd.substring(0, hasUpd.length()-1);
                    model.addAttribute("hasUpd", hasUpd);//开始日期有变动的小牌别名
                }
            }
        }
        return hasUpd;
        //return JSON.toJSONString(model);
    }
    
    @RequestMapping(method = RequestMethod.POST, value = "hasDelRegi" ,consumes="application/json")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String hasDelRegi(loginProfile prof,Model model, 
            @RequestBody List<CpdGroupRegistration> cpdGroupRegistrations,
            @RequestParam(value = "cgr_cr_ids", required = false, defaultValue = "0") long cgr_cr_ids) {
        long cgr_cr_id =  0;
        List<CpdGroupRegistration> cpdGroupRegistrationList = null;
        String hasUpd = "";
        if(null!=cpdGroupRegistrations && cpdGroupRegistrations.size()>0){
            cgr_cr_id = cpdGroupRegistrations.get(0).getCgr_cr_id();
            cpdGroupRegistrationList =  cpdRegistrationMgtService.getGroupRegiByCrId(0, cgr_cr_id);
            List<Long> list = new ArrayList();//获取被删除的小牌cpd_cg_id
            if(!cpdGroupRegistrations.containsAll(cpdGroupRegistrationList)){//存在已注册的小牌被删除
                for (int i = 0; i < cpdGroupRegistrationList.size(); i++) {
                    CpdGroupRegistration cpdGroupRegistration =  cpdGroupRegistrationList.get(i);
                    boolean isRegi = true;//判断该小牌是否被删除
                    for (int j = 0; j < cpdGroupRegistrations.size(); j++) {
                        if(cpdGroupRegistrations.get(j).getCgr_cg_id().equals(cpdGroupRegistration.getCgr_cg_id())){
                            isRegi = false;//没被删除，进行修改
                            break;
                        }
                    }
                    if(isRegi){
                        list.add(cpdGroupRegistration.getCgr_cg_id());
                    }
                }
            }  
            if(list.size()>0){
                for (int i = 0; i < list.size(); i++) {
                    hasUpd += cpdRegistrationMgtService.getCpdGroupById(list.get(i)).getCg_alias() +"、";
                }
                if(hasUpd.length()>0){
                    hasUpd = hasUpd.substring(0, hasUpd.length()-1);
                    model.addAttribute("hasUpd", hasUpd);//被删除的小牌别名
                }
            }
        }else if(null!=cpdGroupRegistrations&& cpdGroupRegistrations.size()==0){
            List<CpdGroupRegistration> cpdGroupRegistrations2 = cpdRegistrationMgtService.getCpdCrGroupExist(prof, cgr_cr_ids);
            if(null != cpdGroupRegistrations2){
                if(cpdGroupRegistrations2.size()>0){
                    for (int i = 0; i < cpdGroupRegistrations2.size(); i++) {
                        hasUpd +=cpdGroupRegistrations2.get(i).getCpdGroup().getCg_alias() +"、";
                    }
                    if(hasUpd.length()>0){
                        hasUpd = hasUpd.substring(0, hasUpd.length()-1);
                        model.addAttribute("hasUpd", hasUpd);//被删除的小牌别名
                    }
                }
            }
            
        }
        return hasUpd;
        //return JSON.toJSONString(model);
    }
    

    @RequestMapping(value = "save", method = RequestMethod.POST)
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String save(Model model, loginProfile prof,@ModelAttribute CpdRegistration cpdRegistration,
            @ModelAttribute CpdGroupRegistration cpdGroupRegistration) throws Exception {
        if (cpdRegistration != null && cpdRegistration.getCr_id() != null) {
            model.addAttribute("type", "update");
        } else {
            model.addAttribute("type", "add");
        }
        boolean isExistForRegistration = false;
        boolean isInfoRegistrationBydate = false;
        isExistForRegistration = cpdRegistrationMgtService.isExistForRegistration(prof,cpdRegistration);//检查大牌报名记录是否重复
        if(!isExistForRegistration){
            isInfoRegistrationBydate = cpdRegistrationMgtService.isInfoRegistrationBydate(prof,cpdRegistration);//检查大牌报名记录时间是否重叠
            if(!isInfoRegistrationBydate){
                cpdRegistrationMgtService.insert(prof,cpdRegistration);   
                model.addAttribute("status", 1);//1."添加记录成功"
                model.addAttribute("cr_id", cpdRegistration.getCr_id());
                
                try {
                    ObjectActionLog log = new ObjectActionLog();
                    log.setObjectId(cpdRegistration.getCr_ct_id());//大牌ID
                    log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
                    RegUser regUser = regUserService.getUserDetail(cpdRegistration.getCr_usr_ent_id());
                    CpdType cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
                    log.setObjectTitle(regUser.getUsr_ste_usr_id()+"-"+cpdType.getCt_license_alias());//标题、全名
                    log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
                    log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
                    log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
                    log.setObjectOptUserId(prof.getUsr_ent_id());
                    log.setObjectActionTime(DateUtil.getCurrentTime());
                    log.setObjectOptUserLoginTime(prof.login_date);
                    log.setObjectOptUserLoginIp(prof.ip);
                    SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
                } catch (Exception e) {
                    CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
                    // TODO: handle exception
                }
                
                
            }else{
                model.addAttribute("status", 3);//3."时间重叠"
            }
        }else{
            model.addAttribute("status", 2);//2."数据重复"
        }
        
        return JSON.toJSONString(model);
    }
    
    
    @RequestMapping(value = "saveCpdGroupRegi", method = RequestMethod.POST ,consumes="application/json")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_LICENSE_LIST)
    @ResponseBody
    public String saveCpdGroupRegi(Model model, loginProfile prof,
            @RequestBody List<CpdGroupRegistration> cpdGroupRegistrations  ) throws Exception {
        int cgr_first_ind = 0;
        CpdRegistration cpdRegistration = null;
        CpdType cpdType = null;
        CpdGroup cpdGroup = null;
        for(int i = 0;i<cpdGroupRegistrations.size();i++){
            cgr_first_ind = 0;
            CpdGroupRegistration cpdGroupRegistration = cpdGroupRegistrations.get(i);
            if(cpdUtilService.cpdIsFirstInd(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date())){
                cgr_first_ind = 1;//是首次挂牌
            }
            cpdGroupRegistration.setCgr_first_ind(cgr_first_ind);
            cpdGroupRegistration.setCgr_create_usr_ent_id(prof.getUsr_ent_id());
            cpdGroupRegistration.setCgr_create_datetime(cpdRegistrationMgtService.getDate());
            cpdGroupRegistration.setCgr_update_usr_ent_id(prof.getUsr_ent_id());
            cpdGroupRegistration.setCgr_update_datetime(cpdRegistrationMgtService.getDate());
            cpdGroupRegistration.setCgr_status(CpdUtils.STATUS_OK);        
            
            //大牌注册信息
            cpdRegistration = new CpdRegistration();    
            cpdRegistration = cpdRegistrationMgtService.getDetail(cpdGroupRegistration.getCgr_cr_id());
            //大牌信息
            cpdType = new CpdType();
            cpdType = cpdRegistrationMgtService.getCpdTypeByid(cpdRegistration.getCr_ct_id());
            //小牌信息
            cpdGroup = new CpdGroup();
            cpdGroup = cpdRegistrationMgtService.getCpdGroupById(cpdGroupRegistration.getCgr_cg_id());
            
            CpdGroupPeriod req_period = cpdUtilService.getReqHoursList(cpdGroup.getCg_id());
            
            
            //上一个周期
            CpdGroupRegistration preCpdGroupRegistration =  cpdUtilService.getPreGroupRegistration(cpdGroupRegistration.getCgr_cg_id(), cpdGroupRegistration.getCgr_usr_ent_id(), cpdGroupRegistration.getCgr_initial_date());
            //实际开始时间
            cpdGroupRegistration.setCgr_actual_date(cpdUtilService.getCgrActualDate(cpdGroupRegistration, preCpdGroupRegistration, cpdType));
            

            //获取周期信息
            CpdPeriodVO prePeriod = null;
            if(preCpdGroupRegistration!=null){
                prePeriod = cpdUtilService.getPeriod(preCpdGroupRegistration.getCgr_initial_date(), cpdType.getCt_id());
            }
            
            //增加小牌注册记录
            cpdRegistrationMgtService.insertGroupRegi( cpdGroupRegistration);


            if(req_period!=null){//CpdGroupPeriod为null时不进行重算
              //中间表记录变动计算方法
                cpdUtilService.calReqHours(cpdGroupRegistration, cpdType, cpdGroup, req_period, CpdUtils.REQUIRE_HOURS_ACTION_ADD, prePeriod, cpdGroupRegistration.getCgr_usr_ent_id());
            }
            
            try {
                ObjectActionLog log = new ObjectActionLog();
                log.setObjectId(cpdGroupRegistration.getCgr_cg_id());//小牌ID
                log.setObjectCode(cpdRegistration.getCr_reg_number());//注册号码
                log.setObjectTitle(cpdRegistration.getUser().getUsr_ste_usr_id()+"-"+cpdRegistration.getCpdType().getCt_license_alias()
                        +"-->"+cpdGroup.getCg_alias());//标题、全名
                log.setObjectType(ObjectActionLog.OBJECT_TYPE_CPD_REG);
                log.setObjectAction(ObjectActionLog.OBJECT_ACTION_ADD);
                log.setObjectActionType(ObjectActionLog.OBJECT_ACTION_TYPE_WEB);
                log.setObjectOptUserId(prof.getUsr_ent_id());
                log.setObjectActionTime(DateUtil.getCurrentTime());
                log.setObjectOptUserLoginTime(prof.login_date);
                log.setObjectOptUserLoginIp(prof.ip);
                SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
            } catch (Exception e) {
                CommonLog.error("CPT/D Registration  exception:"+e.getMessage(),e);
                // TODO: handle exception
            }
            
            
            
        }
        model.addAttribute("status", 1);//1."添加记录成功"
        return JSON.toJSONString(model);
    }

}
