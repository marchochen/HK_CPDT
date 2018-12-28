package com.cwn.wizbank.cpd.controller;

import java.io.File;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.dataMigrate.imp.Imp;
import com.cw.wizbank.db.view.ViewIMSLog.ViewUsrIMSLog;
import com.cw.wizbank.enterprise.IMSLog;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.cpd.service.AeItemCPDItemService;
import com.cwn.wizbank.cpd.service.CpdGroupService;
import com.cwn.wizbank.cpd.service.CpdImportAwardedHoursService;
import com.cwn.wizbank.cpd.service.CpdLrnAwardRecordService;
import com.cwn.wizbank.cpd.service.CpdUtilService;
import com.cwn.wizbank.entity.CpdType;
import com.cwn.wizbank.entity.ImsLog;
import com.cwn.wizbank.scheduled.ImportCpdAwardHoursFromFile;
import com.cwn.wizbank.scheduled.ImportUserFromFile;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.security.service.AclService;
import com.cwn.wizbank.services.AeItemService;
import com.cwn.wizbank.services.CourseTabsRemindService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
 * cpd导入学员获得时数
 *
 */
//@RequestMapping("admin/cpdImportAwardedHours")
//@Controller
public class CpdImportAwardedHoursController {

    @Autowired
    CpdLrnAwardRecordService cpdLrnAwardRecordService;

    @Autowired
    AeItemCPDItemService aeItemCPDItemService;

    @Autowired
    CpdGroupService cpdGroupService;

    @Autowired
    CpdUtilService cpdUtilService;

    @Autowired
    AclService aclService;

    @Autowired
    AeItemService aeItemService;

    @Autowired
    CourseTabsRemindService courseTabsRemindService;

    @Autowired
    CpdImportAwardedHoursService cpdImportAwardedHoursService;

    /**
     * 导入用户获得CPT/D时数
     * 
     * @param mav
     * @param prof
     * @return
     */
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    @RequestMapping(value = "toCPDHoursAwaededImport", method = RequestMethod.GET)
    public ModelAndView toCPDHoursAwaededImport(ModelAndView mav, loginProfile prof,WizbiniLoader wizbini,Model model) {
        int maxUploadCount = wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount();
        model.addAttribute("maxUploadCount", maxUploadCount);
        mav = new ModelAndView("/admin/cpd/cpdt_hours_awarded_import");
        return mav;
    }

    /**
     * 导入用户获得CPT/D时数日志页面
     * @param model
     * @return
     */
    @RequestMapping(value = "cpdHoursAwaedImportLog")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    public ModelAndView cpdHoursAwaedImportLog(ModelAndView mav,Model model,WizbiniLoader wizbini,loginProfile prof) {
        mav = new ModelAndView("/admin/cpd/cpd_hours_awarded_import_log");
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
        cpdImportAwardedHoursService.searchAll(page, prof,static_env);
        return JsonFormat.format(model, page);
    }
    
    
    
    /**
     * 导入用户获得CPT/D时数模板说明
     * @param model
     * @return
     */
    @RequestMapping(value = "toCPDHoursAwaededImportInstr")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    public String toCPDHoursAwaededImportInstr(Model model) {
        return "admin/cpd/cpd_hours_awarded_import_model";
    }
    

    /**
     * 上传文件
     *
     * @param files
     * @param request
     * @return
     */
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    @RequestMapping(value = "filesUpload", method = RequestMethod.POST)
    public ModelAndView filesUpload(@RequestParam("src_filename") MultipartFile file,@RequestParam("upload_desc")String upload_desc,
            ModelAndView mav, Model model,WizbiniLoader wizbini,qdbEnv static_env,loginProfile prof) {
        // 保存文件
        Map<String, Object> map = cpdImportAwardedHoursService.saveFile( file,wizbini,static_env,prof);
        mav = new ModelAndView("/admin/cpd/cpd_hours_awarded_import_comfirm");
        
        model.addAttribute("list", map.get("list"));
        model.addAttribute("errorFile", map.get("errorFile"));
        model.addAttribute("filePath", map.get("filePath"));
        model.addAttribute("msg", map.get("msg"));
        model.addAttribute("successCount", map.get("successNumber"));
        model.addAttribute("failCount",map.get("errorNumber"));
        model.addAttribute("sumCount", map.get("totalNumber"));
        model.addAttribute("upload_desc", upload_desc);
        
        if(map.get("filePath")!=null){
            File srcFile = new File (map.get("filePath").toString());
            model.addAttribute("filePathSource", cwUtils.esc4XML(Imp.getFilePath(srcFile)));
        }
        return mav;
    }

    /**
     * 确认导入
     *
     * @param files
     * @param request
     * @return
     */
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    @RequestMapping(value = "comfirmUpload", method = RequestMethod.POST)
    public ModelAndView filesUpload(@RequestParam(value = "filePath", required = false) String filePath,@RequestParam("upload_desc")String upload_desc,
            loginProfile prof, ModelAndView mav, Model model,WizbiniLoader wizbini,qdbEnv static_env) {
        mav = new ModelAndView("/admin/cpd/cpd_hours_awarded_import_end");
        try {
            //设置参数到同步线程
            File file = new File(filePath);
            cpdImportAwardedHoursService.insFile(wizbini, prof, filePath, upload_desc, static_env);
            Map<String, Object> map = cpdImportAwardedHoursService.comfirmUpload(file.getName(),upload_desc, prof,static_env,wizbini);
            
            model.addAttribute("success_total", map.get("success_total"));
            model.addAttribute("unsuccess_total", map.get("unsuccess_total"));
            model.addAttribute("success_href", map.get("success_href"));
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return mav;
    }

}
