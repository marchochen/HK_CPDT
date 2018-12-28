package com.cwn.wizbank.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.entity.Tag;
import com.cwn.wizbank.entity.TcTrainingCenter;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.persistence.TcTrainingCenterMapper;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.TagService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.web.validation.TagValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;

/**
 * Tag Controller 标签控制器
 */
@Controller
@RequestMapping("admin/kbTag")
public class KbTagController {

	@Autowired
	TagService tagService;
	
	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	@Autowired
	TcTrainingCenterMapper tcTrainingCenterMapper;

	@RequestMapping("index")
	@HasPermission(AclFunction.FTN_AMD_KNOWLEDEG_TAG)
	public String index(loginProfile prof) throws Exception {
		if (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_KNOWLEDEG_TAG) 
				&& AccessControlWZB.isRoleTcInd(prof.current_role)) {
			if (!forCallOldAPIService.hasEffTc(null, prof.usr_ent_id)) {
				throw new MessageException("label_core_requirements_management_63");
			}
		}
		return "admin/kbTag/admin";
	}

	@RequestMapping("listJson")
	@HasPermission(AclFunction.FTN_AMD_KNOWLEDEG_TAG)
	@ResponseBody
	public String listJson(loginProfile prof, Model model, Page<Tag> page) {
		tagService.searchAll(page, prof);
		return JsonFormat.format(model, page);
	}

	@RequestMapping(method = RequestMethod.GET, value = "insert")
	@HasPermission(AclFunction.FTN_AMD_KNOWLEDEG_TAG)
	public String insert(Model model, @ModelAttribute Tag tag) {
		if(null!=tag && !StringUtils.isEmpty(tag.getEncrypt_tag_id())){
			long tagId = EncryptUtil.cwnDecrypt(tag.getEncrypt_tag_id());
			tag.setTag_id(tagId);
		}
		if (tag != null && tag.getTag_id() != null) {
			model.addAttribute("type", "update");
			tag = tagService.get(tag.getTag_id());
		} else {
			model.addAttribute("type", "add");
		}
		model.addAttribute(tag);
		return "admin/kbTag/add";
	}

	@RequestMapping("detail/{tag_id}")
	@HasPermission(AclFunction.FTN_AMD_KNOWLEDEG_TAG)
	public String detail(Model model, @PathVariable(value = "tag_id") long tag_id) {
		model.addAttribute("tag", tagService.get(tag_id));
		model.addAttribute("type", "detail");
		return "admin/kbTag/detail";
	}

	@RequestMapping(method = RequestMethod.GET, value = "detele")
	@HasPermission(AclFunction.FTN_AMD_KNOWLEDEG_TAG)
	@ResponseBody
	public void detele(@ModelAttribute Tag tag) {
		if(null!=tag && !StringUtils.isEmpty(tag.getEncrypt_tag_id())){
			long tagId = EncryptUtil.cwnDecrypt(tag.getEncrypt_tag_id());
			tag.setTag_id(tagId);
		}
		tagService.delete(tag);
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	@HasPermission(AclFunction.FTN_AMD_KNOWLEDEG_TAG)
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
			return "admin/kbTag/add";
		} else { // 该标签已经存在
			if (tagService.isExistForTitle(tag)) {
				result.rejectValue("tag_title", null, null, LabelContent.get(prof.cur_lan, "lab_eist_tag"));
				return "admin/kbTag/add";
			}
		}
		tagService.saveOrUpdate(tag, prof);
		return "redirect:/app/admin/kbTag/index";
	}

	@RequestMapping("getTagJson")
	@ResponseBody
	public List<Tag> getTagByTcr(WizbiniLoader wizbini, loginProfile prof, @RequestParam(value = "kbi_id", required = false, defaultValue = "") String kbi_id) {
		return tagService.getTagByTcr(wizbini, prof, kbi_id);
	}
}
