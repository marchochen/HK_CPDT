package com.cwn.wizbank.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.KbCatalog;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.services.KbCatalogService;
import com.cwn.wizbank.services.TcTrainingCenterService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.web.validation.KbCatalogValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;

/**
 * Knowledge Base Controller 知识库控制器
 */
@Controller
@RequestMapping("kb/catalog")
public class KbCatalogController {

	@Autowired
	KbCatalogService kbCatalogService;

	@Autowired
	TcTrainingCenterService tcTrainingCenterService;

	@RequestMapping("admin/index")
	public String index() {
		return "/kbCatalog/admin";
	}

	@RequestMapping("admin/indexJson")
	@ResponseBody
	public String indexJson(Model model, loginProfile prof, Page<KbCatalog> page) {
		kbCatalogService.listPage(page, prof);
		return JsonFormat.format(model, page);
	}

	@RequestMapping(method = RequestMethod.GET, value = "admin/insert")
	public String insert(Model model, @ModelAttribute KbCatalog kbCatalog) {
		if (kbCatalog != null && kbCatalog.getKbc_id() != null) {
			model.addAttribute("type", "update");
			kbCatalog = kbCatalogService.get(kbCatalog.getKbc_id());
		} else {
			model.addAttribute("type", "add");
		}
		model.addAttribute(kbCatalog);
		return "/kbCatalog/insert";
	}

	@RequestMapping(method = RequestMethod.POST, value = "admin/insert")
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
			return "/kbCatalog/insert";
		} else {
			if (kbCatalogService.isExist(kbCatalog)) {
				result.rejectValue("kbc_title", null, null, LabelContent.get(prof.cur_lan, "lab_eist_catalog"));
				return "/kbCatalog/insert";
			}
		}
		kbCatalogService.saveOrUpdate(kbCatalog, prof);
		return "redirect:/app/kb/catalog/admin/index";
	}

	@RequestMapping(method = RequestMethod.GET, value = "admin/delete")
	@ResponseBody
	public Map<String, Object> delete(@ModelAttribute KbCatalog kbCatalog, loginProfile prof) {
		return kbCatalogService.delete(kbCatalog, prof);
	}

	// 知识中心学员端用到
	@RequestMapping("admin/getCatalogJson")
	@ResponseBody
	public List<KbCatalog> getCatalogByTcr(WizbiniLoader wizbini, Model model, loginProfile prof, @RequestParam(value = "kbi_id", required = false, defaultValue = "") String kbi_id) {
		return kbCatalogService.getCatalogByTcr(wizbini, prof, KbItem.STATUS_ON, kbi_id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "admin/publish")
	@ResponseBody
	public void publish(@ModelAttribute KbCatalog kbCatalog, loginProfile prof) {
		kbCatalogService.publish(kbCatalog, prof);
	}
}
