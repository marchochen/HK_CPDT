package com.cwn.wizbank.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.KbCatalog;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.KbCatalogService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.web.validation.KbCatalogValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;

/**
 * Knowledge Base Controller 知识库控制器
 */
@Controller("adminKbCatalogController")
@RequestMapping("admin/kbCatalog")
@HasPermission(value = {AclFunction.FTN_AMD_KNOWLEDGE_STOREGE,AclFunction.FTN_AMD_KNOWLEDEG_CATALOG,AclFunction.FTN_AMD_KNOWLEDEG_TAG,AclFunction.FTN_AMD_KNOWLEDEG_APP})
public class KbCatalogController {

	@Autowired
	KbCatalogService kbCatalogService;

	@Autowired
	TcTrainingCenterService tcTrainingCenterService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	@RequestMapping("index")
	public String index(loginProfile prof) throws Exception {
		if (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_KNOWLEDEG_CATALOG) 
				&& AccessControlWZB.isRoleTcInd(prof.current_role)) {
			if (!forCallOldAPIService.hasEffTc(null, prof.usr_ent_id)) {
				throw new MessageException("label_core_requirements_management_62");
			}
		}
		return "admin/kbCatalog/admin";
	}

	@RequestMapping("indexJson")
	@ResponseBody
	public String indexJson(Model model, loginProfile prof, Page<KbCatalog> page) {
		kbCatalogService.listPage(page, prof);
		return JsonFormat.format(model, page);
	}

	@RequestMapping(method = RequestMethod.GET, value = "insert")
	public String insert(Model model, @ModelAttribute KbCatalog kbCatalog,
			@RequestParam(value = "tcr_id", required = false, defaultValue = "0")Long tcr_id) {
		if(null!=kbCatalog && !StringUtils.isEmpty(kbCatalog.getEncrypt_kbc_id())){
			long kbcId = EncryptUtil.cwnDecrypt(kbCatalog.getEncrypt_kbc_id());
			kbCatalog.setKbc_id(kbcId);
		}
		if (kbCatalog != null && kbCatalog.getKbc_id() != null) {
			model.addAttribute("type", "update");
			kbCatalog = kbCatalogService.get(kbCatalog.getKbc_id());
		} else {
			model.addAttribute("tcr_id", tcr_id);
			if(tcr_id > 0)
				model.addAttribute("tcr_title", tcTrainingCenterService.get(tcr_id).getTcr_title());
		}
		model.addAttribute(kbCatalog);
		return "admin/kbCatalog/insert";
	}

	@RequestMapping(method = RequestMethod.POST, value = "insert")
	public String insert(Model model, WizbiniLoader wizbini, loginProfile prof, @ModelAttribute KbCatalog kbCatalog, BindingResult result) {
		if (kbCatalog != null && kbCatalog.getTcTrainingCenter() != null && kbCatalog.getTcTrainingCenter().getTcr_id() != null && !kbCatalog.getTcTrainingCenter().getTcr_id().equals("")) {
			TcTrainingCenter tcTrainingCenter = tcTrainingCenterService.get(kbCatalog.getTcTrainingCenter().getTcr_id());
			kbCatalog.setTcTrainingCenter(tcTrainingCenter);
		}
		WzbValidationUtils.invokeValidator(new KbCatalogValidator(prof), kbCatalog, result);
		if (kbCatalog != null && kbCatalog.getKbc_id() != null) {
			model.addAttribute("type", "update");
		} else {
			model.addAttribute("type", "add");
		}
		if (result.hasErrors()) {
			return "admin/kbCatalog/insert";
		} else {
			if (kbCatalogService.isExist(kbCatalog)) {
				result.rejectValue("kbc_title", null, null, LabelContent.get(prof.cur_lan, "lab_eist_catalog"));
				return "admin/kbCatalog/insert";
			}
		}
		kbCatalogService.saveOrUpdate(kbCatalog, prof);
		return "redirect:/app/admin/kbCatalog/index";
	}

	@RequestMapping(method = RequestMethod.GET, value = "delete")
	@ResponseBody
	public Map<String, Object> delete(@ModelAttribute KbCatalog kbCatalog, loginProfile prof) {
		if(null!=kbCatalog && !StringUtils.isEmpty(kbCatalog.getEncrypt_kbc_id())){
			long kbcId = EncryptUtil.cwnDecrypt(kbCatalog.getEncrypt_kbc_id());
			kbCatalog.setKbc_id(kbcId);
		}
		return kbCatalogService.delete(kbCatalog,prof);
	}

	// 知识中心学员端用到
	@RequestMapping("getCatalogJson")
	@ResponseBody
	public List<KbCatalog> getCatalogByTcr(WizbiniLoader wizbini, Model model, loginProfile prof, @RequestParam(value = "kbi_id", required = false, defaultValue = "") String kbi_id) {
		return kbCatalogService.getCatalogByTcr(wizbini, prof, KbItem.STATUS_ON, kbi_id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "publish")
	@ResponseBody
	public void publish(@ModelAttribute KbCatalog kbCatalog, loginProfile prof) {
		if(null!=kbCatalog && !StringUtils.isEmpty(kbCatalog.getEncrypt_kbc_id())){
			long kbcId = EncryptUtil.cwnDecrypt(kbCatalog.getEncrypt_kbc_id());
			kbCatalog.setKbc_id(kbcId);
		}
		kbCatalogService.publish(kbCatalog, prof);
	}
}
