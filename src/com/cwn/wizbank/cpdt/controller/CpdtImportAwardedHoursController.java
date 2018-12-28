package com.cwn.wizbank.cpdt.controller;

import java.io.File;
import java.util.Map;

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
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.qdb.qdbEnv;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.cpdt.service.CpdtImportAwardedHoursService;
import com.cwn.wizbank.entity.ImsLog;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.Page;

/**
* Title: CpdtImportAwardedHoursController.java 
* Description: CPDT导入学员获得时数
* @author Jaren
* @date 2018年11月20日
 */
@Controller
@RequestMapping("admin/cpdtImportAwardedHours")
public class CpdtImportAwardedHoursController {
	
	@Autowired
    CpdtImportAwardedHoursService cpdtImportAwardedHoursService;
	
	
	/** 导入用户获得CPD/CPT时数 - 主页 **/
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    @RequestMapping(value = "toCpdtHoursAwaededImport", method = RequestMethod.GET)
    public ModelAndView toCPDHoursAwaededImport(ModelAndView mav, loginProfile prof,WizbiniLoader wizbini,Model model) {
    	// 最大上传记录数
        int maxUploadCount = wizbini.cfgSysSetupadv.getUserBatchUpload().getMaxUploadCount();
        model.addAttribute("maxUploadCount", maxUploadCount);
        mav = new ModelAndView("/admin/cpdt/cpdt_hours_awarded_import");
        return mav;
    }
    
    
    
    /** 导入用户获得CPD/CPT时数 - 日志页面 **/
    @RequestMapping(value = "cpdtHoursAwaedImportLog")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    public ModelAndView cpdHoursAwaedImportLog(ModelAndView mav,Model model,WizbiniLoader wizbini,loginProfile prof) {
        mav = new ModelAndView("/admin/cpdt/cpdt_hours_awarded_import_log");
        return mav;
    }
    
    
    /** 导入用户获得CPT/D时数 - 日志数据 **/
    @RequestMapping("listJsonLog")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    @ResponseBody
    public String listJson(loginProfile prof, Model model, Page<ImsLog> page,qdbEnv static_env) {
    	page = cpdtImportAwardedHoursService.searchAll(page, prof,static_env);
        return JsonFormat.format(model, page);
    }
    
    
    
   /** 上传文件 **/
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    @RequestMapping(value = "filesUpload", method = RequestMethod.POST)
    public ModelAndView filesUpload(ModelAndView mav, Model model,WizbiniLoader wizbini,qdbEnv static_env,loginProfile prof,
    		@RequestParam("src_filename") MultipartFile file,@RequestParam("upload_desc")String upload_desc) {
        // 保存文件
        Map<String, Object> map = cpdtImportAwardedHoursService.checkAndSaveFile(file,wizbini,static_env,prof);
        model.addAttribute("errorMsg", map.get("errorMsg")); // 错误信息具体内容
        model.addAttribute("errorFile", map.get("errorFile"));
        model.addAttribute("filePath", map.get("filePath")); // 源文件相对路径
        model.addAttribute("msg", map.get("msg")); 
        model.addAttribute("successCount", map.get("successNumber")); // 成功记录总数
        model.addAttribute("failCount",map.get("errorNumber")); // 失败记录总数
        model.addAttribute("sumCount", map.get("totalNumber")); // 记录总数
        model.addAttribute("upload_desc", upload_desc);
        
        if(map.get("filePath") != null){
        	// 源文件的绝对路径
            File srcFile = new File (map.get("filePath").toString());
            model.addAttribute("filePathSource", cwUtils.esc4XML(Imp.getFilePath(srcFile)));
        }
        
        mav = new ModelAndView("/admin/cpdt/cpdt_hours_awarded_import_comfirm");
        return mav;
    }
    
    /** 确认导入 **/
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    @RequestMapping(value = "comfirmUpload", method = RequestMethod.POST)
    public ModelAndView filesUpload(@RequestParam(value = "filePath", required = false) String filePath,@RequestParam("upload_desc")String upload_desc,
            loginProfile prof, ModelAndView mav, Model model,WizbiniLoader wizbini,qdbEnv static_env) {
            Map<String, Object> map = cpdtImportAwardedHoursService.comfirmUpload(filePath,upload_desc, prof,static_env,wizbini);
            model.addAttribute("success_total", map.get("success_total"));
            model.addAttribute("unsuccess_total", map.get("unsuccess_total"));
            model.addAttribute("success_href", map.get("success_href"));
        mav = new ModelAndView("/admin/cpdt/cpdt_hours_awarded_import_end");
        return mav;
    }
    
    
    /** 导入用户获得CPT/D时数模板说明 **/
    @RequestMapping(value = "toCpdtHoursAwaededImportInstr")
    @HasPermission(AclFunction.FTN_AMD_CPT_D_MGT)
    public String toCPDHoursAwaededImportInstr(Model model) {
        return "admin/cpdt/cpdt_hours_awarded_import_model";
    }
    
    
    
}
