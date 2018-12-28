package com.cwn.wizbank.controller.admin;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cw.wizbank.JsonMod.eip.bean.EnterpriseInfoPortalBean;
import com.cw.wizbank.JsonMod.eip.dao.EnterpriseInfoPortalDao;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.entity.AcFunction;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.EnterpriseInfoPortal;
import com.cwn.wizbank.entity.MessageTemplate;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.AcFunctionService;
import com.cwn.wizbank.services.AcRoleFunctionService;
import com.cwn.wizbank.services.AcRoleService;
import com.cwn.wizbank.services.EnterpriseInfoPortalService;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.MessageTemplateService;
import com.cwn.wizbank.utils.CommonLog;
import com.cwn.wizbank.utils.CwnUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.web.validation.AcRoleValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;

/**
 * Role Manage Controller 角色管理控制器
 */
@Controller("adminRoleController")
@RequestMapping("admin/role")
public class RoleController {
	
	@Autowired
	AcRoleService roleService;

	@Autowired
	AcRoleFunctionService acRoleFunctionService;
	@Autowired
	AcFunctionService acFunctionService;
	@Autowired
	MessageTemplateService messageTemplateService;
	
	@Autowired
	private EnterpriseInfoPortalService eipService; 
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;
	
	@HasPermission(value = {AclFunction.FTN_AMD_SYS_ROLE_MAIN})
	@RequestMapping("list")
	public String index(){
		
		return "admin/role/list";
	}
	
	/**角色列表数据*/
	@HasPermission(value = {AclFunction.FTN_AMD_SYS_ROLE_MAIN})
	@RequestMapping("indexJson")
	@ResponseBody
	public String indexJson(Model model,loginProfile prof,Page<AcRole> page){
		roleService.getList(page,prof);
		return JsonFormat.format(model, page);
	}
	
	/**新增角色页面*/
	@HasPermission(value = {AclFunction.FTN_AMD_SYS_ROLE_MAIN})
	@RequestMapping("addRole")
	public String addRole(Model model,@ModelAttribute AcRole acRole){
		model.addAttribute("type", "add");
		model.addAttribute("acRole",acRole);
		return "admin/role/add";
	}
	
	/**修改角色*/
	@HasPermission(value = {AclFunction.FTN_AMD_SYS_ROLE_MAIN})
	@RequestMapping("update")
	public String update(Model model,@ModelAttribute AcRole acRole,@RequestParam(value="role_id")long role_id,loginProfile prof){
		model.addAttribute("type", "update");
		acRole = roleService.get(role_id);
		if(acRole!=null){
			String roleTitle = "";
			if("SYSTEM".equals(acRole.getRol_type())){
				roleTitle = LabelContent.get(prof.cur_lan, "lab_rol_" + acRole.getRol_ste_uid());
			}else{
				roleTitle = acRole.getRol_title();
			}
			acRole.setRol_title(roleTitle);
		}
		model.addAttribute("acRole",acRole);
		return "admin/role/add";
	}
	
	/**添加新角色*/
	@HasPermission(value = {AclFunction.FTN_AMD_SYS_ROLE_MAIN})
	@RequestMapping(value = "save",method=RequestMethod.POST)
	public String save(Model model,loginProfile prof,@ModelAttribute AcRole acRole,
						BindingResult result,RedirectAttributes attributes,
						@RequestParam(value="type")String type){
		WzbValidationUtils.invokeValidator(new AcRoleValidator(prof.cur_lan), acRole, result);
		if(result.hasErrors()){
			model.addAttribute("type", type);
			model.addAttribute("acRole",acRole);
			return "admin/role/add";
		}
		try {
			roleService.save(acRole,prof,type);
		} catch (MessageException e) {	
			model.addAttribute("type", type);
			model.addAttribute("acRole",acRole);	
			//e.printStackTrace();
			model.addAttribute("exist","true");
			result.rejectValue("rol_title", null, null, LabelContent.get(prof.cur_lan, "label_core_system_setting_128"));
			return "admin/role/add";
		}
		return "redirect:/app/admin/role/list";
	}
	
	/**删除角色*/
	@HasPermission(value = {AclFunction.FTN_AMD_SYS_ROLE_MAIN})
	@RequestMapping(value="delById",method=RequestMethod.POST)
	@ResponseBody
	public void delById(@RequestParam(value="ids",required=false,defaultValue="")String ids){
		if(ids != null && !"".equals(ids)){
			String[] rol_id = ids.split(",");
			if(rol_id != null && rol_id.length > 0){
				for(int i = 0;i<rol_id.length;i++){
					roleService.delById(Long.valueOf(rol_id[i]));
				}
			}
		}
	}
	
	/**
	 * 获取当前角色的权限
	 * @param current_role
	 * @return
	 */
	@RequestMapping(value="getCurrentRoleFunctions",method=RequestMethod.POST)
	@ResponseBody
	public List<AcFunction> getCurrentRoleFunctions(loginProfile prof){
		return acRoleFunctionService.getFunctions(prof.current_role);
	}
	
	
	/**初始化角色权限
	 * 升级旧系统，或者是把所有角色的权限恢复到最初始状态时使用
	 * 这些请求只是供升级系弘临时例用，不要用于常规功能。
	 * */
	//@HasPermission(value = {AclFunction.FTN_AMD_SYS_ROLE_MAIN})
	@RequestMapping("initRolePermission")
	@ResponseBody
	public String initRolePermission(@RequestParam(value="dirPath",required=true)String dirPath){
		try {
			//清除acRoleFunction表
			acRoleFunctionService.getAcRoleFunctionMapper().truncate();
			//清除acFunction表
			acFunctionService.getAcFunctionMapper().deleteAll();
	
			acFunctionService.initFunction(dirPath + "acfunction.xlsx");
			//学员
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionLearnning.xlsx");
			//培训管理员
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionTaAdmin.xlsx");
			//系统管理员
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionSysAdmin.xlsx");
			//讲师
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionInstructor.xlsx");
			//考试监考员
			acRoleFunctionService.initRoleFunction(dirPath + "acfunctionInvigilator.xlsx");
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			
			return "error!error!error!error!" +  cwUtils.esc4JS(e.getMessage());
		}
		return "finish!finish!finish!finish!finish!";
	}
	
	/**初始化邮件内容
	 * 升级旧系统，或者是把所有邮件内容恢复到最初始状态时使用
	 * 这些请求只是供升级系弘临时例用，不要用于常规功能。
	 * */
	@HasPermission(value = {AclFunction.FTN_AMD_SYS_ROLE_MAIN})
	@RequestMapping("initEmail")
	@ResponseBody
	public String initEmail(@RequestParam(value="dirPath",required=true)String dirPath){
		try{
			List<String> fileNames = new ArrayList<String>();
			File dir = new File(dirPath);
			if(dir.isDirectory()) {
				File[] listFiles = dir.listFiles();
				if(listFiles != null && listFiles.length > 0) {
					for(File f : listFiles) {
						String name = f.getName();
						String content = CwnUtil.file2String(f).replaceAll("[\\t\\n\\r]", " ");
						name = name.substring(2).replace(".html", "");
						fileNames.add(name);
						String[] name_arr= CwnUtil.splitToString(name,"[--]");
						String subject = null;
						if(name_arr != null && name_arr.length > 1){
							name = name_arr[0];
							subject= name_arr[1];
						}
						
						//
						Map<String,Object> param = new HashMap<String,Object>();
						param.put("tpl_type", name);
						param.put("tcr_id", 1);
						MessageTemplate temp = messageTemplateService.getByType(param);
						temp.setMtp_content(content);
						if(subject != null){
							temp.setMtp_subject(subject);
						}
						messageTemplateService.update(temp);
						CommonLog.debug(name + "    " + content);
					}
				}
			}
			
			List<EnterpriseInfoPortal> enterpriseInfoPortalList =  eipService.getEnterpriseInfoPortalList();
			if(enterpriseInfoPortalList != null && enterpriseInfoPortalList.size() > 0){
				for(EnterpriseInfoPortal e : enterpriseInfoPortalList){
					if(e.getEip_tcr_id() != 1){
						forCallOldAPIService.coptyTemplates(null, e.getEip_tcr_id());
					}
				}
			}
		} catch (Exception e) {
			CommonLog.error(e.getMessage(),e);
			return "error!error!error!error!" +  cwUtils.esc4JS(e.getMessage());
		}
		return "finish!finish!finish!finish!finish!";
	}
}
