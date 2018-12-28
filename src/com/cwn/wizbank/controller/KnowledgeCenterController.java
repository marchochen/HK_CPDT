package com.cwn.wizbank.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
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

import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.KbAttachment;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.exception.AuthorityException;
import com.cwn.wizbank.exception.NotPublishException;
import com.cwn.wizbank.security.AclFunction;
import com.cwn.wizbank.security.annotation.HasPermission;
import com.cwn.wizbank.services.KbAttachmentService;
import com.cwn.wizbank.services.KbCatalogService;
import com.cwn.wizbank.services.KbItemService;
import com.cwn.wizbank.services.SnsCommentService;
import com.cwn.wizbank.services.SnsCountService;
import com.cwn.wizbank.utils.EncryptUtil;
import com.cwn.wizbank.utils.JsonFormat;
import com.cwn.wizbank.utils.LabelContent;
import com.cwn.wizbank.utils.Page;
import com.cwn.wizbank.utils.Params;
import com.cwn.wizbank.web.validation.KnowledgeCenterValidator;
import com.cwn.wizbank.web.validation.WzbValidationUtils;

/**
 * Knowledge Base Controller 知识库控制器
 */
@Controller
@RequestMapping("kb/center")
public class KnowledgeCenterController {

	@Autowired
	KbItemService kbItemService;

	@Autowired
	SnsCountService snsCountService;

	@Autowired
	SnsCommentService snsCommentService;

	@Autowired
	KbAttachmentService kbAttachmentService;
	
	@Autowired
	KbCatalogService KbCatalogService;

	@RequestMapping("index")
	@HasPermission(AclFunction.FTN_LRN_KB_VIEW)
	public String index() {
		return "/knowledge/index";
	}

	@RequestMapping("index/indexJson")
	@HasPermission(AclFunction.FTN_LRN_KB_VIEW)
	@ResponseBody
	public String indexJson(WizbiniLoader wizbini, Model model, loginProfile prof, Page<KbItem> page) {
		kbItemService.listStudyPage(wizbini, page, prof);
		return JsonFormat.format(model, page);
	}

	@RequestMapping("list")
	@HasPermission(AclFunction.FTN_LRN_KB_VIEW)
	public String list(Model model, @RequestParam(value = "tab", required = false, defaultValue = "") String tab) {
		model.addAttribute("tab", cwUtils.esc4JS(tab));
		return "/knowledge/list";
	}

	@RequestMapping("index/{type}")
	@HasPermission(AclFunction.FTN_LRN_KB_VIEW)
	@ResponseBody
	public String knowlegeRankJson(WizbiniLoader wizbini, Model model, loginProfile prof, Page<KbItem> page, @PathVariable(value = "type") String type) {
		kbItemService.knowledgeRank(wizbini, page, type, prof);
		return JsonFormat.format(model, page);
	}

	@RequestMapping("view")
	@HasPermission(AclFunction.FTN_LRN_KB_VIEW)
	public String view(WizbiniLoader wizbini, Model model, loginProfile prof, @ModelAttribute KbItem kbItem, @RequestParam(value = "source", required = false, defaultValue = "") String source,
			@RequestParam(value = "tab", required = false, defaultValue = "") String tab,String enc_kbi_id) throws NotPublishException, AuthorityException {
		
		if(enc_kbi_id!=null && kbItem.getKbi_id() == null){
			long kbi_id = EncryptUtil.cwnDecrypt(enc_kbi_id);
			kbItem.setKbi_id(kbi_id);
		}
		
		model.addAttribute("source", cwUtils.esc4JS(source));
		model.addAttribute("tab", cwUtils.esc4JS(tab));
		model.addAttribute("type", "center");
		kbItem = kbItemService.get(kbItem.getKbi_id());
		if (kbItem != null && kbItem.getKbi_id() != null) {
			if (!kbItemService.hashAuthority(kbItem.getKbi_id(), wizbini, prof)) {
				throw new AuthorityException();
			}
			if (!kbItem.getKbi_status().equals(KbItem.STATUS_ON) && !kbItem.getKbi_create_user_id().equals(prof.usr_id)) {
				throw new NotPublishException(LabelContent.get(prof.cur_lan, "lab_kb_knowledge_not_publish"));
			} else {
				if (kbItem.getKbi_status().equals(KbItem.STATUS_ON)) {
					kbItemService.view(prof, kbItem);
				}
				if (kbItem.getKbi_type().equals(KbItem.TYPE_DOCUMENT)) {
					String filename = kbItem.getAttachments().get(0).getKba_file();
					//使用I DOC VIEW插件不需要转换PDF
					//filename = filename.substring(0, filename.lastIndexOf(".") + 1) + "pdf";
					kbItem.getAttachments().get(0).setKba_filename(filename);
				}
				kbItem.setKbi_comment_count(snsCommentService.getCommentCount(kbItem.getKbi_id(), SNS.MODULE_KNOWLEDGE));
				model.addAttribute(kbItem);
				model.addAttribute(prof);
				return "/knowledge/view";
			}
		} else {
			throw new NotPublishException(LabelContent.get(prof.cur_lan, "lab_kb_knowledge_not_publish"));
		}
	}

	@RequestMapping("index/recentView")
	@HasPermission(AclFunction.FTN_LRN_KB_VIEW)
	@ResponseBody
	public String recentView(WizbiniLoader wizbini, Model model, loginProfile prof, Page<KbItem> page) {
		kbItemService.recentView(wizbini, page, prof);
		return JsonFormat.format(model, page);
	}

	@RequestMapping(method = RequestMethod.GET, value = "insert")
	@HasPermission(AclFunction.FTN_LRN_KB_VIEW)
	public String insert(Model model, @ModelAttribute KbItem kbItem, @RequestParam(value = "source", required = false, defaultValue = "") String source,
			@RequestParam(value = "tab", required = false, defaultValue = "") String tab) {
		model.addAttribute("source", cwUtils.esc4JS(source));
		model.addAttribute("tab", cwUtils.esc4JS(tab));
		if (kbItem.getKbi_id() != null && kbItem.getKbi_id() != 0) {
			kbItem = kbItemService.get(kbItem.getKbi_id());
		} else {
			kbItem.setKbi_id(0L);
		}
		kbItem.setKbi_type(cwUtils.esc4JS(kbItem.getKbi_type()));
		kbItem.setKbi_online(cwUtils.esc4JS(kbItem.getKbi_online()));
		model.addAttribute(kbItem);
		return "/knowledge/insert";
	}

	@RequestMapping(method = RequestMethod.POST, value = "insert")
	@HasPermission(AclFunction.FTN_LRN_KB_VIEW)
	public String insert(Model model, WizbiniLoader wizbini, loginProfile prof, HttpServletRequest request, @ModelAttribute KbItem kbItem, BindingResult result) {
		model.addAttribute("type", kbItem.getKbi_type());
		WzbValidationUtils.invokeValidator(new KnowledgeCenterValidator(prof), kbItem, result);
		KbAttachment kbAttachment = new KbAttachment(); 
		boolean pass = true;
		if (StringUtils.isBlank(kbItem.getKbi_image())) {
			if (StringUtils.isNotBlank(kbItem.getKbi_default_image())) {
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
			if (!kbItem.getKbi_type().equals(KbItem.TYPE_ARTICLE) && kbItem.getKbi_content() != null && !"".equals(kbItem.getKbi_content()) && !kbItem.getKbi_online().equals(KbItem.VIDEO_ONLINE)) {
				kbItem.setAttachments(kbAttachmentService.getAttachmentByContent(kbItem.getKbi_content()));
				kbItem.setKbi_content(null);
			}
			if (kbItem.getKbi_image() != null && !kbItem.getKbi_image().equals("")) {
				kbItem.setImageAttachment(kbAttachmentService.get(Long.valueOf(kbItem.getKbi_image())));
			}
			return "/knowledge/insert";
		}
		kbItemService.insertOrUpdateByShare(kbItem, prof);
		String url = "redirect:/app/kb/center/index";
		if (kbItem.getKbi_id() != null && kbItem.getKbi_id() != 0) {
			url = "redirect:/app/kb/center/list?tab=" + KbItem.APP_STATUS_PENDING;
		}
		return url;
	}

	@RequestMapping(method = RequestMethod.GET, value = "view/detailJson/{id}")
	@HasPermission({ AclFunction.FTN_LRN_KB_VIEW, AclFunction.FTN_AMD_KNOWLEDGE_MGT })
	@ResponseBody
	public String detailJson(Model model, loginProfile prof, @PathVariable(value = "id") long id) {
		model.addAttribute("snsCount", snsCountService.getByTargetInfo(id, SNS.MODULE_KNOWLEDGE));
		model.addAttribute("sns", snsCountService.getUserSnsDetail(id, prof.usr_ent_id, SNS.MODULE_KNOWLEDGE));
		return JsonFormat.format(model);
	}

	@RequestMapping("download/{id}")
	@HasPermission({ AclFunction.FTN_LRN_KB_VIEW, AclFunction.FTN_AMD_KNOWLEDGE_MGT })
	public void download(HttpServletResponse res, WizbiniLoader wizbini, @PathVariable(value = "id") long id) throws IOException {
		KbItem kbItem = kbItemService.get(id);
		String basePath = wizbini.getFileUploadAttachmentDirAbs() + File.separator + kbItem.getAttachments().get(0).getKba_id() + File.separator;
		OutputStream os = res.getOutputStream();
		String filename = kbItem.getAttachments().get(0).getKba_file();
		if(KbItem.TYPE_VEDIO.equals(kbItem.getKbi_type()) && (KbItem.VIDEO_OFFLINE.equals(kbItem.getKbi_online()) || kbItem.getKbi_online() == null || "".equals(kbItem.getKbi_online()))){
			if(!filename.toLowerCase().endsWith(".mp4")) {
				filename += ".mp4";
			}
		}
		try {
			res.reset();
			res.setHeader("Content-Disposition", "attachment; filename=" + filename);
			res.setContentType("application/octet-stream; charset=utf-8");
			File file = new File(basePath + kbItem.getAttachments().get(0).getKba_file());
			os.write(FileUtils.readFileToByteArray(file));
			os.flush();
		} finally {
			if (os != null) {
				os.close();
			}
		}
	}

	@RequestMapping("download_count/{id}")
	@HasPermission({ AclFunction.FTN_LRN_KB_VIEW, AclFunction.FTN_AMD_KNOWLEDGE_MGT })
	@ResponseBody
	public Map<String, Object> download_count(@PathVariable(value = "id") long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		KbItem kbItem = kbItemService.downloadCount(id);
		map.put("kbi_download_count", kbItem.getKbi_download_count());
		return map;
	}

	@RequestMapping("getDefaultImages")
	@HasPermission({ AclFunction.FTN_LRN_KB_VIEW, AclFunction.FTN_AMD_KNOWLEDGE_MGT, "KNOWLEDGE_STOREGE" })
	@ResponseBody
	public List<KbAttachment> getDefaultImages(WizbiniLoader wizbini) {
		String path = wizbini.getWebDocRoot() + cwUtils.SLASH  + "imglib" + cwUtils.SLASH + "knowledge";
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
        List<File> files = new ArrayList<File>();
		
		readFile(files,dir);
		
		List<KbAttachment> list = new ArrayList<KbAttachment>();
		for(File file : files)
		{
			String filename = file.getName();
			String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

			if (cwUtils.notEmpty(ext) && (ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png"))) {
				KbAttachment kbAttachment = new KbAttachment();
				String name = file.getName().substring(0, file.getName().lastIndexOf("."));
				String src = file.getParentFile().getName()+"/";
				kbAttachment.setKba_filename(name);
				kbAttachment.setKba_file(src+file.getName());
				list.add(kbAttachment);
			}
		}
		
		return list;
		/*File[] images = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				String filename = file.getName();
				String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

				if (cwUtils.notEmpty(ext) && (ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png"))) {
					return true;
				}
				return false;
			}
		});

		List<KbAttachment> list = new ArrayList<KbAttachment>();
		for (File file : images) {
			KbAttachment kbAttachment = new KbAttachment();
			String name = file.getName().substring(0, file.getName().lastIndexOf("."));
			kbAttachment.setKba_filename(name);
			kbAttachment.setKba_file(file.getName());
			list.add(kbAttachment);
		}
		return list; */
	}
	
	@RequestMapping("main/mobile/Json")
	@ResponseBody
	public String mainJson (Model model, WizbiniLoader wizbini, loginProfile prof, Params params){
		model.addAttribute(kbItemService.listGroupKnowledge(wizbini, prof));		
		return JsonFormat.format(params, model);
	}
	/**
	 * 获取详细页数据
	 */
	@RequestMapping("mobile/detailJson/{encId}")
	@ResponseBody
	public String detailJson(Model model, WizbiniLoader wizbini, loginProfile prof, Params params,
			@PathVariable(value = "encId") String encId) throws Exception{

		long id = EncryptUtil.cwnDecrypt(encId);

		KbItem kbItem = kbItemService.get(id);
		if (kbItem != null && kbItem.getKbi_id() != null) {
			if (!kbItemService.hashAuthority(kbItem.getKbi_id(), wizbini, prof)) {
				throw new AuthorityException();
			}
			if (!kbItem.getKbi_status().equals(KbItem.STATUS_ON) && !kbItem.getKbi_create_user_id().equals(prof.usr_id)) {
				throw new NotPublishException(LabelContent.get(prof.cur_lan, "lab_kb_knowledge_not_publish"));
			} else {
				if (kbItem.getKbi_status().equals(KbItem.STATUS_ON)) {
					kbItemService.view(prof, kbItem);
				}
				if (kbItem.getKbi_type().equals(KbItem.TYPE_DOCUMENT)) {
					String filename = kbItem.getAttachments().get(0).getKba_file();
					filename = filename.substring(0, filename.lastIndexOf(".") + 1) + "pdf";
					kbItem.getAttachments().get(0).setKba_filename(filename);
				}
				kbItem.setKbi_comment_count(snsCommentService.getCommentCount(kbItem.getKbi_id(), SNS.MODULE_KNOWLEDGE));
				kbItem.setSns(snsCountService.getUserSnsDetail(id, prof.usr_ent_id, SNS.MODULE_KNOWLEDGE));
				kbItem.setSnsCount(snsCountService.getByTargetInfo(id, SNS.MODULE_KNOWLEDGE));				
				model.addAttribute(kbItem);			
			}
		} else {
			throw new NotPublishException(LabelContent.get(prof.cur_lan, "lab_kb_knowledge_not_publish"));
		}
		return JsonFormat.format(params, model);
	}
	
	@RequestMapping("mobile/insert")
	@ResponseBody
	public String addKbItem(Model model, WizbiniLoader wizbini, HttpServletRequest request, loginProfile prof, Params params,
			@RequestParam(value = "json", required = false, defaultValue = "{}") String json,
			@RequestParam(value = "tag", required = false, defaultValue = "") String tag,
			@RequestParam(value = "catalog", required = false, defaultValue = "") String catalog){
		if(StringUtils.isNotBlank(json)){
			JSONObject jsons = JSONObject.fromObject(json); 
			KbItem kbItem = (KbItem)JSONObject.toBean(jsons, KbItem.class);
			if(StringUtils.isNotBlank(tag)){
				String[] tags = tag.split(","); 
				Long [] tagIds = new Long[tags.length]; 
				for(int i = 0; i < tags.length; i++){
					tagIds[i] = Long.parseLong(tags[i]);
				}
				kbItem.setKbi_tag_ids(tagIds);
			}
			if(StringUtils.isNotBlank(catalog)){
				String[] catalogs = catalog.split(","); 
				Long[] catalogIds = new Long[catalog.split(",").length];
				for(int i = 0; i < catalogs.length; i++){
					catalogIds[i] = Long.parseLong(catalogs[i]);
				}
				kbItem.setKbi_catalog_ids(catalogIds);
			}
 			if (StringUtils.isBlank(kbItem.getKbi_image())) {
				if (StringUtils.isNotBlank(kbItem.getKbi_default_image())) {
					String path = wizbini.getWebDocRoot() + kbItem.getKbi_default_image();
					File file = new File(path);
					KbAttachment kbAttachment = kbAttachmentService.upload(model, wizbini, prof, file, request);
					kbItem.setImageAttachment(kbAttachment);
					kbItem.setKbi_image(String.valueOf(kbAttachment.getKba_id()));
				}
			}
			kbItemService.insertOrUpdateByShare(kbItem, prof);
		}				
		return JsonFormat.format(params, model);
	}
	
	/**
	 *  读取文件夹中的所有文件
	 * @param files
	 * @param file
	 * @return
	 */
	private void readFile(List<File> files,File file)
	{
		if(file.isDirectory())
		{
			for(File directory : file.listFiles())
			{
				readFile(files,directory);
			}
		}
		else
		{
			files.add(file);
		}
	}
}
