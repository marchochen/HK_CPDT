package com.cwn.wizbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Tag;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.persistence.TcTrainingCenterMapper;
import com.cwn.wizbank.services.TagService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.web.validation.TagValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;

/**
 * Tag Controller 标签控制器
 */
@Controller
@RequestMapping("kb/tag")
public class TagController {

	@Autowired
	TagService tagService;

	@Autowired
	TcTrainingCenterMapper tcTrainingCenterMapper;

	@RequestMapping("admin/index")
	public String index() {
		return "tag/admin";
	}

	@RequestMapping("admin/listJson")
	@ResponseBody
	public String listJson(loginProfile prof, Model model, Page<Tag> page) {
		tagService.searchAll(page, prof);
		return JsonFormat.format(model, page);
	}

	@RequestMapping(method = RequestMethod.GET, value = "admin/insert")
	public String insert(Model model, @ModelAttribute Tag tag) {
		if (tag != null && tag.getTag_id() != null) {
			model.addAttribute("type", "update");
			tag = tagService.get(tag.getTag_id());
		} else {
			model.addAttribute("type", "add");
		}
		model.addAttribute(tag);
		return "/tag/add";
	}

	@RequestMapping("admin/detail/{tag_id}")
	public String detail(Model model, @PathVariable(value = "tag_id") long tag_id) {
		model.addAttribute("tag", tagService.get(tag_id));
		model.addAttribute("type", "detail");
		return "tag/detail";
	}

	@RequestMapping(method = RequestMethod.GET, value = "admin/detele")
	@ResponseBody
	public void detele(@ModelAttribute Tag tag) {
		tagService.delete(tag);
	}

	@RequestMapping(value = "admin/save", method = RequestMethod.POST)
	public String save(Model model, loginProfile prof, @ModelAttribute Tag tag, BindingResult result, RedirectAttributes redirectAttributes) throws Exception {
		if (tag != null && tag.getTcTrainingCenter() != null && tag.getTcTrainingCenter().getTcr_id() != null && !tag.getTcTrainingCenter().getTcr_id().equals("")) {
			TcTrainingCenter tcTrainingCenter = tcTrainingCenterMapper.get(tag.getTcTrainingCenter().getTcr_id());
			tag.setTcTrainingCenter(tcTrainingCenter);
		}
		WzbValidationUtils.invokeValidator(new TagValidator(prof.cur_lan), tag, result);
		if (tag != null && tag.getTag_id() != null) {
			model.addAttribute("type", "update");
		} else {
			model.addAttribute("type", "add");
		}
		if (result.hasErrors()) {
			return "tag/add";
		} else { // 该标签已经存在
			if (tagService.isExistForTitle(tag)) {
				result.rejectValue("tag_title", null, null, LabelContent.get(prof.cur_lan, "lab_eist_tag"));
				return "tag/add";
			}
		}
		tagService.saveOrUpdate(tag, prof);
		return "redirect:/app/kb/tag/admin/index";
	}

	@RequestMapping("admin/getTagJson")
	@ResponseBody
	public List<Tag> getTagByTcr(WizbiniLoader wizbini, loginProfile prof, @RequestParam(value = "kbi_id", required = false, defaultValue = "") String kbi_id) {
		return tagService.getTagByTcr(wizbini, prof, kbi_id);
	}
}
