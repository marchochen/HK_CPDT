package com.cwn.wizbank.controller;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.KbAttachment;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.KbAttachmentService;
import com.cwn.wizbank.services.KbCatalogService;
import com.cwn.wizbank.services.KbItemService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SnsCommentService;
import com.cwn.wizbank.services.TagService;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.web.validation.KbItemValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;

/**
 * Knowledge Base Controller 知识库控制器
 */
@Controller
@RequestMapping("kb")
public class KnowledgeBaseController {

	@Autowired
	KbItemService kbItemService;

	@Autowired
	KbCatalogService kbCatalogService;

	@Autowired
	TagService tagService;

	@Autowired
	KbAttachmentService kbAttachmentService;

	@Autowired
	RegUserService regUserService;

	@Autowired
	ForCallOldAPIService forCallOldAPIService;

	@Autowired
	SnsCommentService snsCommentService;

	@RequestMapping("admin/index")
	public String index(Model model, @RequestParam(value = "kbc_id", required = false, defaultValue = "0") long kbc_id) {
		model.addAttribute("kbc_id", kbc_id);
		return "/kb/admin";
	}

	@RequestMapping("admin/indexJson")
	@ResponseBody
	public String indexJson(loginProfile prof, Model model, Page<KbItem> page) {
		kbItemService.listPage(page, prof);
		return JsonFormat.format(model, page);
	}

	@RequestMapping(method = RequestMethod.GET, value = "admin/insert")
	public String insert(Model model, @ModelAttribute KbItem kbItem, @RequestParam(value = "source", required = false, defaultValue = "") String source) {
		if (kbItem != null && kbItem.getKbi_id() != null) {
			model.addAttribute("type", "update");
			kbItem = kbItemService.get(kbItem.getKbi_id());
		} else {
			model.addAttribute("type", "add");
		}
		model.addAttribute("source", source);
		model.addAttribute(kbItem);
		return "/kb/insert";
	}

	@RequestMapping(method = RequestMethod.POST, value = "admin/insert")
	public String insert(Model model, WizbiniLoader wizbini, loginProfile prof, HttpServletRequest request, @ModelAttribute KbItem kbItem, BindingResult result,
			@RequestParam(value = "source", required = false, defaultValue = "") String source) {
		if (kbItem != null && kbItem.getKbi_id() != null) {
			model.addAttribute("type", "update");
		} else {
			model.addAttribute("type", "add");
		}
		model.addAttribute("source", source);
		WzbValidationUtils.invokeValidator(new KbItemValidator(prof), kbItem, result);
		KbAttachment kbAttachment = new KbAttachment();
		boolean pass = true;
		if (kbItem.getKbi_image() == null || "".equals(kbItem.getKbi_image())) {
			if (kbItem.getKbi_default_image() != null && !(kbItem.getKbi_default_image().equals(""))) {
				String path = wizbini.getWebDocRoot() + kbItem.getKbi_default_image();
				File file = new File(path);
				kbAttachment = kbAttachmentService.upload(model, wizbini, prof, file, request);
				kbItem.setImageAttachment(kbAttachment);
				kbItem.setKbi_image(String.valueOf(kbAttachment.getKba_id()));
			} else {
				result.rejectValue("kbi_image", null, null, LabelContent.get(prof.cur_lan, "lab_kb_image_error"));
				pass = false;
			}
		}
		if (kbItem.getKbi_type().equals(KbItem.TYPE_VEDIO) && kbItem.getKbi_online().equals(KbItem.VIDEO_ONLINE) && !kbItem.getKbi_content().equals("")) {
			String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
			Pattern p = Pattern.compile(regex);
			Matcher matcher = p.matcher(kbItem.getKbi_content());
			if (matcher.matches()) {
				int length = kbItem.getKbi_content().length();
				if (length > 7) {
					String suffix = kbItem.getKbi_content().substring(length - 4, length);
					if (!suffix.equalsIgnoreCase(".mp4")) {
						result.rejectValue("kbi_content", null, null, LabelContent.get(prof.cur_lan, "lab_kb_video_url_error"));
						pass = false;
					}
				} else {
					result.rejectValue("kbi_content", null, null, LabelContent.get(prof.cur_lan, "lab_kb_video_url_error"));
					pass = false;
				}
			} else {
				result.rejectValue("kbi_content", null, null, LabelContent.get(prof.cur_lan, "lab_kb_video_url_error"));
				pass = false;
			}
		}
		if(kbItem.getKbi_type().equals(KbItem.TYPE_VEDIO)){
			kbItem.setKbi_filetype(KbItem.TYPE_VEDIO);
		}else if(kbItem.getKbi_type().equals(KbItem.TYPE_IMAGE)){
			kbItem.setKbi_filetype(KbItem.TYPE_IMAGE);
		}else if(kbItem.getKbi_type().equals(KbItem.TYPE_ARTICLE)){
			kbItem.setKbi_filetype(KbItem.TYPE_ARTICLE);
		}else if(kbItem.getKbi_type().equals(KbItem.TYPE_DOCUMENT)){
			kbItem.setKbi_filetype("DOCUMENT");
			KbAttachment ka = kbAttachmentService.get(Long.parseLong(kbItem.getKbi_content()));
			if(!ka.getKba_filename().equals("") && ka.getKba_filename()!=null){
				String[]  filenamelist = ka.getKba_filename().split("\\.");
				int size = filenamelist.length - 1;
				if(size < 1){
					result.rejectValue("kbi_content", null, null, LabelContent.get(prof.cur_lan, "lab_kb_video_url_error"));
					pass = false;
				}
				String suffix = filenamelist[size];
				
				if(suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx")){
					kbItem.setKbi_filetype("XLS");
				}else if(suffix.equalsIgnoreCase("doc") || suffix.equalsIgnoreCase("docx")){
					kbItem.setKbi_filetype("DOC");
				}else if(suffix.equalsIgnoreCase("PPT") || suffix.equalsIgnoreCase("PPTX")){
					kbItem.setKbi_filetype("PPT");
				}else if(suffix.equalsIgnoreCase("PDF")){
					kbItem.setKbi_filetype("PDF");
				}
			}
		}
		if (result.hasErrors() || !pass) {
			kbItem.setCatalogues(kbCatalogService.getCatalogByIds(kbItem.getKbi_catalog_ids()));
			kbItem.setTags(tagService.getTagByIds(kbItem.getKbi_tag_ids()));
			if (!kbItem.getKbi_type().equals(KbItem.TYPE_ARTICLE) && kbItem.getKbi_content() != null && !"".equals(kbItem.getKbi_content()) && !kbItem.getKbi_online().equals(KbItem.VIDEO_ONLINE)) {
				kbItem.setAttachments(kbAttachmentService.getAttachmentByContent(kbItem.getKbi_content()));
				kbItem.setKbi_content(null);
			}
			kbItem.setKbi_catalog_ids(null);
			kbItem.setKbi_tag_ids(null);
			if (kbItem.getKbi_image() != null && !kbItem.getKbi_image().equals("")) {
				kbItem.setImageAttachment(kbAttachmentService.get(Long.valueOf(kbItem.getKbi_image())));
			}
			return "/kb/insert";
		}
		String url = "redirect:/app/kb/admin/index";
		if (source != null && source.equals("approval")) {
			url = "redirect:/app/kb/admin/approval";
		}
		kbItemService.saveOrUpdate(kbItem, prof);
		return url;
	}

	@RequestMapping(method = RequestMethod.GET, value = "admin/delete")
	@ResponseBody
	public void delete(@ModelAttribute KbItem kbItem, WizbiniLoader wizbini) {
		if (kbItem != null && kbItem.getKbi_id() != null) {
			kbItemService.delete(kbItem.getKbi_id(), wizbini);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "admin/publish")
	@ResponseBody
	public void publish(@ModelAttribute KbItem kbItem, loginProfile prof) {
		kbItemService.publish(kbItem, prof);
	}

	@RequestMapping(method = RequestMethod.GET, value = "admin/approval")
	public String approval() {
		return "/kb/approval";
	}

	@RequestMapping(method = RequestMethod.POST, value = "admin/approval")
	public String approval(@ModelAttribute KbItem kbItem, loginProfile prof) throws Exception {
		kbItemService.approval(kbItem, prof);
		kbItem = kbItemService.get(kbItem.getKbi_id());
		RegUser regUser = regUserService.getUserDetailByUserId(kbItem.getKbi_create_user_id());
		forCallOldAPIService.updUserCredits(null, Credit.KB_SHARE_KNOWLEDGE, regUser.getUsr_ent_id(), regUser.getUsr_id(), 0, 0);
		return "/kb/approval";
	}

	@RequestMapping(method = RequestMethod.POST, value = "admin/deleteByIds")
	@ResponseBody
	public void delete(@RequestParam(value = "ids", required = false, defaultValue = "") String ids, WizbiniLoader wizbini) {
		if (ids != null && !"".equals(ids)) {
			String[] arrId = ids.split(",");
			if (arrId != null && arrId.length > 0) {
				for (int i = 0; i < arrId.length; i++) {
					kbItemService.delete(Long.valueOf(arrId[i]), wizbini);
				}
			}
		}

	}

	@RequestMapping("admin/view")
	public String view(Model model, @ModelAttribute KbItem kbItem, @RequestParam(value = "source", required = false, defaultValue = "") String source) throws AuthorityException {
		kbItem = kbItemService.get(kbItem.getKbi_id());
		if (kbItem.getKbi_type().equals(KbItem.TYPE_DOCUMENT)) {
			String filename = kbItem.getAttachments().get(0).getKba_file();
			filename = filename.substring(0, filename.lastIndexOf(".") + 1) + "pdf";
			kbItem.getAttachments().get(0).setKba_filename(filename);
		}
		kbItem.setKbi_comment_count(snsCommentService.getCommentCount(kbItem.getKbi_id(), SNS.MODULE_KNOWLEDGE));
		model.addAttribute(kbItem);
		model.addAttribute("source", source);
		model.addAttribute("type", "admin");
		return "/knowledge/view";
	}
	
	/**
	 * 为kb_item表添加了知识文件类型的参数，提供了这个方法，更新旧数据
	 * @return
	 */
	@RequestMapping("admin/updateOldData")
	@ResponseBody
	public String updateOldData(){
		kbItemService.updateOldData();
		return "success";
	}
}
