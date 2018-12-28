package com.cwn.wizbank.controller.admin;

import java.io.File;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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

import com.cw.wizbank.JsonMod.credit.Credit;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.KbAttachment;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.entity.ObjectActionLog;
import com.cwn.wizbank.entity.RegUser;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.EncryptException;
import com.cwn.wizbank.exception.MessageException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.ForCallOldAPIService;
import com.cwn.wizbank.services.KbAttachmentService;
import com.cwn.wizbank.services.KbCatalogService;
import com.cwn.wizbank.services.KbItemService;
import com.cwn.wizbank.services.RegUserService;
import com.cwn.wizbank.services.SnsCommentService;
import com.cwn.wizbank.services.SnsCountService;
import com.cwn.wizbank.services.TagService;
import com.cwn.wizbank.systemLog.SystemLogContext;
import com.cwn.wizbank.systemLog.SystemLogTypeEnum;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.web.validation.KbItemValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;

/**
 * Knowledge Base Controller 知识库控制器
 */
@Controller("adminKnowledgeBaseController")
@RequestMapping("admin/kb")
@HasPermission(value = {AclFunction.FTN_AMD_KNOWLEDGE_STOREGE,AclFunction.FTN_AMD_KNOWLEDEG_CATALOG,AclFunction.FTN_AMD_KNOWLEDEG_TAG,AclFunction.FTN_AMD_KNOWLEDEG_APP})
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
	
	@Autowired
	SnsCountService snsCountService;

	@RequestMapping("storege")
	public String index(Model model, @RequestParam(value = "kbc_id", required = false, defaultValue = "0") long kbc_id,loginProfile prof) throws Exception {
		model.addAttribute("kbc_id", kbc_id);
		
		if (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_KNOWLEDGE_STOREGE) 
				&& AccessControlWZB.isRoleTcInd(prof.current_role)) {
			if (!forCallOldAPIService.hasEffTc(null, prof.usr_ent_id)) {
				throw new MessageException("label_core_requirements_management_61");
			}
		}
		return "admin/kb/storege";
	}

	@RequestMapping("indexJson")
	@ResponseBody
	public String indexJson(loginProfile prof, Model model, Page<KbItem> page) {
		kbItemService.listPage(page, prof);
		return JsonFormat.format(model, page);
	}

	@RequestMapping(method = RequestMethod.GET, value = "insert")
	public String insert(Model model, @ModelAttribute KbItem kbItem, @RequestParam(value = "source", required = false, defaultValue = "") String source) {
		if(!StringUtils.isEmpty(kbItem.getEncrypt_kbi_id())){
			long kbi_id = EncryptUtil.cwnDecrypt(kbItem.getEncrypt_kbi_id());
			kbItem.setKbi_id(kbi_id);
		}
		if (kbItem != null && kbItem.getKbi_id() != null) {
			model.addAttribute("type", "update");
			kbItem = kbItemService.get(kbItem.getKbi_id());
		} else {
			model.addAttribute("type", "add");
		}
		model.addAttribute("source", source);
		model.addAttribute(kbItem);
		return "admin/kb/insert";
	}

	@RequestMapping(method = RequestMethod.POST, value = "insert")
	public String insert(Model model, WizbiniLoader wizbini, loginProfile prof, HttpServletRequest request, @ModelAttribute KbItem kbItem, BindingResult result,
			@RequestParam(value = "source", required = false, defaultValue = "") String source) {
		
		ObjectActionLog log = new ObjectActionLog(kbItem.getKbi_id(), 
				null,
				kbItem.getKbi_title(),
				ObjectActionLog.OBJECT_TYPE_KB,
				ObjectActionLog.OBJECT_ACTION_ADD,
				ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
				prof.getUsr_ent_id(),
				prof.getUsr_last_login_date(),
				prof.getIp()
		);
		
		if (kbItem != null && kbItem.getKbi_id() != null) {
			model.addAttribute("type", "update");
			log.setObjectAction(ObjectActionLog.OBJECT_ACTION_UPD);
		} else {
			model.addAttribute("type", "add");
		}
		
		if(kbItem.getKbi_content() != null){
			String content=kbItem.getKbi_content().replaceAll("&nbsp;", "").trim();
			if(content.equals(""))
			{
				kbItem.setKbi_content("");
			}
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
			return "admin/kb/insert";
		}
		String url = "redirect:/app/admin/kb/storege";
		if (source != null && source.equals("approval")) {
			url = "redirect:/app/admin/kb/approval";
		}
		kbItemService.saveOrUpdate(kbItem, prof);
		if (kbItem != null && kbItem.getKbi_id() != null) {
			log.setObjectId(kbItem.getKbi_id());
		}
		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		return url;
	}

	@RequestMapping(method = RequestMethod.GET, value = "delete")
	@ResponseBody
	public void delete(@ModelAttribute KbItem kbItem, WizbiniLoader wizbini,loginProfile prof) {
		if(!StringUtils.isEmpty(kbItem.getEncrypt_kbi_id())){
			long kbi_id = EncryptUtil.cwnDecrypt(kbItem.getEncrypt_kbi_id());
			kbItem.setKbi_id(kbi_id);
		}
		if (kbItem != null && kbItem.getKbi_id() != null) {
			kbItem = kbItemService.get(kbItem.getKbi_id());
			ObjectActionLog log = new ObjectActionLog(kbItem.getKbi_id(), 
					null,
					kbItem.getKbi_title(),
					ObjectActionLog.OBJECT_TYPE_KB,
					ObjectActionLog.OBJECT_ACTION_DEL,
					ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
					prof.getUsr_ent_id(),
					prof.getUsr_last_login_date(),
					prof.getIp()
			);
			kbItemService.delete(kbItem.getKbi_id(), wizbini);
			SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "publish")
	@ResponseBody
	public void publish(@ModelAttribute KbItem kbItem, loginProfile prof) throws EncryptException{
		if(!StringUtils.isEmpty(kbItem.getEncrypt_kbi_id())){
			long kbi_id = EncryptUtil.cwnDecrypt(kbItem.getEncrypt_kbi_id());
			kbItem.setKbi_id(kbi_id);
		}
		kbItemService.publish(kbItem, prof);
		ObjectActionLog log = new ObjectActionLog(kbItem.getKbi_id(), 
				null,
				kbItemService.getKbTitle(kbItem.getKbi_id()),
				ObjectActionLog.OBJECT_TYPE_KB,
				ObjectActionLog.OBJECT_ACTION_PUB,
				ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
				prof.getUsr_ent_id(),
				prof.getUsr_last_login_date(),
				prof.getIp()
		);
		if(KbItem.STATUS_OFF.equals(kbItem.getKbi_status())){
			log.setObjectAction(ObjectActionLog.OBJECT_ACTION_CANCLE_PUB);
		}
		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
	}

	@RequestMapping(method = RequestMethod.GET, value = "approval")
	public String approval(loginProfile prof) throws MessageException, SQLException {
		if (AccessControlWZB.hasRolePrivilege(prof.current_role, AclFunction.FTN_AMD_KNOWLEDEG_APP) 
				&& AccessControlWZB.isRoleTcInd(prof.current_role)) {
			if (!forCallOldAPIService.hasEffTc(null, prof.usr_ent_id)) {
				throw new MessageException("label_core_requirements_management_64");
			}
		}
		return "admin/kb/approval";
	}

	@RequestMapping(method = RequestMethod.POST, value = "approval")
	public String approval(@ModelAttribute KbItem kbItem, loginProfile prof) throws Exception {
		kbItemService.approval(kbItem, prof);
		kbItem = kbItemService.get(kbItem.getKbi_id());
		RegUser regUser = regUserService.getUserDetailByUserId(kbItem.getKbi_create_user_id());
		forCallOldAPIService.updUserCredits(null, Credit.KB_SHARE_KNOWLEDGE, regUser.getUsr_ent_id(), regUser.getUsr_id(), 0, 0);
		ObjectActionLog log = new ObjectActionLog(kbItem.getKbi_id(), 
				null,
				kbItem.getKbi_title(),
				ObjectActionLog.OBJECT_TYPE_KB,
				ObjectActionLog.OBJECT_ACTION_APPR,
				ObjectActionLog.OBJECT_ACTION_TYPE_WEB,
				prof.getUsr_ent_id(),
				prof.getUsr_last_login_date(),
				prof.getIp()
		);
		if(KbItem.APP_STATUS_REAPPROVL.equals(kbItem.getKbi_app_status())){
			log.setObjectAction(ObjectActionLog.OBJECT_ACTION_CANCEL_APPR);
		}
		SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
		return "admin/kb/approval";
	}

	@RequestMapping(method = RequestMethod.POST, value = "deleteByIds")
	@ResponseBody
	public void delete(@RequestParam(value = "ids", required = false, defaultValue = "") String ids, 
			WizbiniLoader wizbini , loginProfile prof) {
		if (ids != null && !"".equals(ids)) {
			String[] arrId = ids.split(",");
			if (arrId != null && arrId.length > 0) {
				for (int i = 0; i < arrId.length; i++) {
					Long id = EncryptUtil.cwnDecrypt(arrId[i]);
					KbItem kbItem = kbItemService.get(id);
					ObjectActionLog log = new ObjectActionLog(kbItem.getKbi_id(),
							null,
							kbItem.getKbi_title(),
							ObjectActionLog.OBJECT_TYPE_KB,
							ObjectActionLog.OBJECT_ACTION_DEL,
							ObjectActionLog.OBJECT_ACTION_TYPE_BATCH,
							prof.getUsr_ent_id(),
							prof.getUsr_last_login_date(),
							prof.getIp()
					);
					kbItemService.delete(id, wizbini);
					SystemLogContext.saveLog(log, SystemLogTypeEnum.OBJECT_ACTION_LOG);
				}
			}
		}

	}

	@RequestMapping("view")
	public String view(Model model,loginProfile prof, @RequestParam(value = "kbi_id", required = true ) String kbi_id
			, @RequestParam(value = "source", required = false, defaultValue = "") String source) throws AuthorityException,EncryptException {
		Long id = EncryptUtil.cwnDecrypt(kbi_id);
		KbItem kbItem = kbItemService.get(id);
		if (kbItem.getKbi_type().equals(KbItem.TYPE_DOCUMENT)) {
			String filename = kbItem.getAttachments().get(0).getKba_file();
			//使用I DOC VIEW插件不需要转换PDF
			//filename = filename.substring(0, filename.lastIndexOf(".") + 1) + "pdf";
			kbItem.getAttachments().get(0).setKba_filename(filename);
		}
		kbItem.setKbi_comment_count(snsCommentService.getCommentCount(kbItem.getKbi_id(), SNS.MODULE_KNOWLEDGE));
		model.addAttribute(kbItem);
		model.addAttribute(prof);
		model.addAttribute("source", source);
		model.addAttribute("type", "admin");
		return "admin/kb/view";
	}
	
	@RequestMapping("detailJson/{id}")
	@ResponseBody
	public String detail(loginProfile prof,
			@PathVariable(value="id") long id,
			Params param,
			Model model){
		
		KbItem kbItem = kbItemService.get(id);
		kbItem.setKbi_comment_count(snsCommentService.getCommentCount(id, SNS.MODULE_KNOWLEDGE));
		model.addAttribute(kbItem);
		model.addAttribute("snsCount", snsCountService.getByTargetInfo(id, SNS.MODULE_KNOWLEDGE));
		model.addAttribute("sns", snsCountService.getUserSnsDetail(id, prof.usr_ent_id, SNS.MODULE_KNOWLEDGE));
		return JsonFormat.format(param, model) ;
	}
}
