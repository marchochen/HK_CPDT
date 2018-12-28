package com.cwn.wizbank.services;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cw.wizbank.Application;
import com.cw.wizbank.accesscontrol.AccessControlWZB;
import com.cw.wizbank.ae.aeAction;
import com.cw.wizbank.config.WizbiniLoader;
import com.cw.wizbank.qdb.loginProfile;
import com.cw.wizbank.util.LangLabel;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.common.SNS;
import com.cwn.wizbank.entity.AcRole;
import com.cwn.wizbank.entity.KbAttachment;
import com.cwn.wizbank.entity.KbCatalog;
import com.cwn.wizbank.entity.KbItem;
import com.cwn.wizbank.entity.KbItemAttachment;
import com.cwn.wizbank.entity.KbItemCat;
import com.cwn.wizbank.entity.KbItemTag;
import com.cwn.wizbank.entity.KbItemView;
import com.cwn.wizbank.entity.Tag;
import com.cwn.wizbank.persistence.KbItemAttachmentMapper;
import com.cwn.wizbank.persistence.KbItemCatMapper;
import com.cwn.wizbank.persistence.KbItemMapper;
import com.cwn.wizbank.persistence.KbItemTagMapper;
import com.cwn.wizbank.persistence.KbItemViewMapper;
import com.cwn.wizbank.utils.IdocViewUtils;
import com.cwn.wizbank.utils.ImageUtil;
import com.cwn.wizbank.utils.Page;

/**
 * service 实现
 */
@Service
public class KbItemService extends BaseService<KbItem> {

	@Autowired
	KbItemMapper kbItemMapper;

	@Autowired
	KbItemCatMapper kbItemCatMapper;

	@Autowired
	KbItemTagMapper kbItemTagMapper;

	@Autowired
	KbItemViewMapper kbItemViewMapper;

	@Autowired
	SnsCommentService snsCommentService;

	@Autowired
	SnsCountService snsCountService;

	@Autowired
	SnsValuationLogService snsValuationLogService;

	@Autowired
	KbCatalogService kbCatalogService;

	@Autowired
	KbItemAttachmentMapper kbItemAttachmentMapper;

	@Autowired
	KbAttachmentService kbAttachmentService;
	
	public KbItem get(long id){
		KbItem kbItem = super.get(id);
		
		if(kbItem != null && KbItem.TYPE_DOCUMENT.equals(kbItem.getKbi_type())){
			
			kbItem.setDocType(getDocType(kbItem));
			
			KbAttachment ka = null;
			if(kbItem.getAttachments()!=null && kbItem.getAttachments().size()>0){
				ka = kbItem.getAttachments().get(0);
			}
			if(ka!=null){
				WizbiniLoader wizbini = aeAction.wizbini;
				String previewUrl = wizbini.cfgSysSetupadv.getFileUpload().getAttachmentDir().getUrl() + "/" + ka.getKba_id() + "/" + ka.getKba_file();
				kbItem.setPreviewUrl(IdocViewUtils.getViewPath(Application.I_DOC_VIEW_PREVIEW_HOST+previewUrl));
			}
		}
			
		return kbItem;
	}

	public void saveOrUpdate(KbItem kbItem, loginProfile prof) {
		kbItem.setKbi_title(kbItem.getKbi_title().trim());
		Timestamp nowDate = super.getDate();
		if (kbItem.getKbi_id() == null || kbItem.getKbi_id().equals("")) {
			kbItem.setKbi_create_datetime(nowDate);
			kbItem.setKbi_create_user_id(prof.usr_id);
			kbItem.setKbi_app_status(KbItem.APP_STATUS_APPROVED);
			kbItem.setKbi_access_count(0L);
			kbItem.setKbi_status(KbItem.STATUS_OFF);
			kbItem.setKbi_download_count(0L);
			super.add(kbItem);
		} else {
			kbItem.setKbi_update_datetime(nowDate);
			kbItem.setKbi_update_user_id(prof.usr_id);
			super.update(kbItem);
		}
		kbItemAttachmentMapper.delete(kbItem.getKbi_id());
		if (!kbItem.getKbi_type().equals(KbItem.TYPE_ARTICLE) && !kbItem.getKbi_online().equals(KbItem.VIDEO_ONLINE)) {
			insertAttachment(kbItem, prof, nowDate);
		}
		insertCatalogAndTag(kbItem, prof, nowDate);
	}

	public Page<KbItem> listPage(Page<KbItem> page, loginProfile prof) {
		if(!AccessControlWZB.isRoleTcInd(prof.current_role)){
			page.getParams().put("usr_ent_id", prof.root_ent_id);
			page.getParams().put("current_role", AcRole.ROLE_ADM_1);
		}else{
			page.getParams().put("usr_ent_id", prof.usr_ent_id);
			page.getParams().put("current_role", prof.current_role);
		}

		kbItemMapper.selectPage(page);
		return page;
	}

	public void delete(long id, WizbiniLoader wizbini) {
		kbItemCatMapper.delete(id);
		kbItemTagMapper.delete(id);
		kbItemViewMapper.delete(id);
		kbItemAttachmentMapper.delete(id);
		kbAttachmentService.delInvalidAttachment(wizbini);
		super.delete(id);
	}

	private void insertCatalogAndTag(KbItem kbItem, loginProfile prof, Timestamp nowDate) {
		KbCatalog kbCatalog = new KbCatalog();
		KbItemCat kbItemCat = new KbItemCat();
		KbItemTag kbItemTag = new KbItemTag();
		Tag tag = new Tag();
		kbItemCatMapper.delete(kbItem.getKbi_id());
		kbItemTagMapper.delete(kbItem.getKbi_id());
		if (kbItem.getKbi_catalog_ids() != null && kbItem.getKbi_catalog_ids().length > 0) {
			for (int i = 0; i < kbItem.getKbi_catalog_ids().length; i++) {
				kbCatalog.setKbc_id(kbItem.getKbi_catalog_ids()[i]);
				kbItemCat.setKbItem(kbItem);
				kbItemCat.setKbCatalog(kbCatalog);
				kbItemCat.setKic_create_datetime(nowDate);
				kbItemCat.setKic_create_user_id(prof.usr_id);
				kbItemCatMapper.insert(kbItemCat);
			}
		}
		if (kbItem.getKbi_tag_ids() != null && kbItem.getKbi_tag_ids().length > 0) {
			for (int i = 0; i < kbItem.getKbi_tag_ids().length; i++) {
				tag.setTag_id(kbItem.getKbi_tag_ids()[i]);
				kbItemTag.setKbItem(kbItem);
				kbItemTag.setTag(tag);
				kbItemTag.setKit_create_datetime(nowDate);
				kbItemTag.setKit_create_user_id(prof.usr_id);
				kbItemTagMapper.insert(kbItemTag);
			}
		}
	}

	private void insertAttachment(KbItem kbItem, loginProfile prof, Timestamp nowDate) {
		KbAttachment kbAttachment = new KbAttachment();
		KbItemAttachment kbItemAttachment = new KbItemAttachment();
		String[] fileIds = kbItem.getKbi_content().split(",");
		if (fileIds != null && fileIds.length > 0) {
			for (int i = 0; i < fileIds.length; i++) {
				kbAttachment.setKba_id(Long.valueOf(fileIds[i]));
				kbItemAttachment.setKbItem(kbItem);
				kbItemAttachment.setKbAttachment(kbAttachment);
				kbItemAttachment.setKia_create_datetime(nowDate);
				kbItemAttachment.setKia_create_user_id(prof.usr_id);
				kbItemAttachmentMapper.insert(kbItemAttachment);
			}
		}
	}

	public void knowledgeRank(WizbiniLoader wizbini, Page<KbItem> page, String type, loginProfile prof) {
		page.getParams().put("type", type);
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		page.getParams().put("usr_id", prof.usr_id);
		page.getParams().put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
		page.getParams().put("tcr_id", prof.my_top_tc_id);
		kbItemMapper.selectKbItemWithView(page);
		for (KbItem kbItem : page.getResults()) {
			ImageUtil.combineImagePath(kbItem.getImageAttachment());
			kbItem.setDocType(getDocType(kbItem));
		}
	}
	
	/**
	 * 获取kbItem的文档类型
	 * @param kbItem
	 * @return
	 */
	public String getDocType(KbItem kbItem){
		
		String type="";
		if(kbItem != null && kbItem.getKbi_type().equalsIgnoreCase("DOCUMENT")&& kbItem.getKbi_content()!=null&&!kbItem.getKbi_content().equals("")){
			String fileName = kbAttachmentService.get(Long.parseLong(kbItem.getKbi_content())).getKba_filename();
			type = getDocType(fileName);
		}
		
		return type;
	}
	
	/**
	 * 获取的fileName文档类型
	 * @param kbItem
	 * @return
	 */
	public static String getDocType(String fileName){
		String type="";
		if(StringUtils.isNotBlank(fileName)){
			String fileType = fileName.substring(fileName.lastIndexOf('.')+1);
			if(fileType.equalsIgnoreCase("doc") || fileType.equalsIgnoreCase("docx")){
				type = "DOC";
			}else if(fileType.equalsIgnoreCase("xls") || fileType.equalsIgnoreCase("xlsx")){
				type = "XLS";
			}else if(fileType.equalsIgnoreCase("ppt") || fileType.equalsIgnoreCase("pptx")){
				type = "PPT";
			}else if(fileType.equalsIgnoreCase("pdf")){
				type="PDF";
			}
		}
		return type;
	}
	
	public void view(loginProfile prof, KbItem kbItem) {
		Timestamp nowDate = super.getDate();
		KbItemView kbItemView = new KbItemView();
		kbItem.setKbi_access_count(kbItem.getKbi_access_count() + 1);
		kbItemMapper.updateAccessCount(kbItem);
		kbItemView.setKbItem(kbItem);
		kbItemView.setKiv_usr_ent_id(prof.usr_ent_id);
		if (kbItemViewMapper.get(kbItemView) > 0) {
			kbItemView.setKiv_update_datetime(nowDate);
			kbItemViewMapper.update(kbItemView);
		} else {
			kbItemView.setKiv_create_datetime(nowDate);
			kbItemView.setKiv_update_datetime(nowDate);
			kbItemViewMapper.insert(kbItemView);
		}
	}

	public void publish(KbItem kbItem, loginProfile prof) {
		if (kbItem != null && kbItem.getKbi_id() != null) {
			kbItem.setKbi_publish_datetime(super.getDate());
			kbItem.setKbi_publish_user_id(prof.usr_id);
			kbItemMapper.publish(kbItem);
		}
	}

	public void approval(KbItem kbItem, loginProfile prof) {
		kbItem.setKbi_approve_datetime(super.getDate());
		kbItem.setKbi_approve_user_id(prof.usr_id);
		kbItemMapper.approval(kbItem);
	}

	public void recentView(WizbiniLoader wizbini, Page<KbItem> page, loginProfile prof) {
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		page.getParams().put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
		page.getParams().put("tcr_id", prof.my_top_tc_id);
		kbItemMapper.selectKbItemRecentView(page);
	}

	public void listStudyPage(WizbiniLoader wizbini, Page<KbItem> page, loginProfile prof) {
		page.getParams().put("kbi_status", KbItem.STATUS_ON);
		page.getParams().put("usr_ent_id", prof.usr_ent_id);
		page.getParams().put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
		page.getParams().put("tcr_id", prof.my_top_tc_id);
		kbItemMapper.listStudyPage(page);
		for (KbItem kbItem : page.getResults()) {
			ImageUtil.combineImagePath(kbItem.getImageAttachment());
			kbItem.setUserLike(snsValuationLogService.getByUserId(kbItem.getKbi_id(), prof.usr_ent_id, SNS.MODULE_KNOWLEDGE, 0));
			kbItem.setSnsCount(snsCountService.getByTargetInfo(kbItem.getKbi_id(), SNS.MODULE_KNOWLEDGE));
			kbItem.setKbi_comment_count(snsCommentService.getCommentCount((long) kbItem.getKbi_id(), SNS.MODULE_KNOWLEDGE));
			kbItem.setSns(snsCountService.getUserSnsDetail(kbItem.getKbi_id(), prof.usr_ent_id, SNS.MODULE_KNOWLEDGE));
			if(kbItem.getKbi_approve_user_id() == null || "".equalsIgnoreCase(kbItem.getKbi_approve_user_id())){
				kbItem.setUsr_display_bil(LangLabel.getValue(prof.cur_lan, "lab_training"));
			}
			kbItem.setDocType(getDocType(kbItem));
		}
	}

	public Map<String, List<KbItem>>  listGroupKnowledge(WizbiniLoader wizbini, loginProfile prof){
		Map<String, List<KbItem>> reMap = new HashMap<String, List<KbItem>>();
		String [] types = new String[]{"ARTICLE", "DOCUMENT", "VEDIO", "IMAGE"};		
		for(String type : types){
			Page<KbItem> page = new Page<KbItem>();
		    page.setPageSize(2);
			page.getParams().put("sortname", "kbi_access_count");
			page.getParams().put("sortorder", "desc");
			page.getParams().put("kbi_type", type);	
			listStudyPage(wizbini, page, prof);
			reMap.put(type, page.getResults());
		}
		return reMap;
	}
	public void insertOrUpdateByShare(KbItem kbItem, loginProfile prof) {
		kbItem.setKbi_title(cwUtils.esc4JS(kbItem.getKbi_title().trim()));
		Timestamp nowDate = super.getDate();
		if (kbItem.getKbi_id() == null || kbItem.getKbi_id() == 0) {
			kbItem.setKbi_create_datetime(nowDate);
			kbItem.setKbi_create_user_id(prof.usr_id);
			kbItem.setKbi_app_status(KbItem.APP_STATUS_PENDING);
			kbItem.setKbi_status(KbItem.STATUS_OFF);
			kbItem.setKbi_access_count(0L);
			kbItem.setKbi_download_count(0L);
			if (kbItem.getKbi_type().equals(KbItem.TYPE_DOCUMENT) || kbItem.getKbi_type().equals(KbItem.TYPE_VEDIO)) {
				kbItem.setKbi_download(KbItem.DOWNLOAD_ALLOW);
			}
			super.add(kbItem);
		} else {
			kbItem.setKbi_app_status(KbItem.APP_STATUS_PENDING);
			kbItem.setKbi_status(KbItem.STATUS_OFF);
			kbItem.setKbi_update_datetime(nowDate);
			kbItem.setKbi_update_user_id(prof.usr_id);
			kbItemMapper.updateByShare(kbItem);
		}

		if (kbItem.getKbi_catalog_ids() == null || kbItem.getKbi_catalog_ids().length < 1) {
			Long[] catalog_ids = new Long[1];
			Long catalog_id;
			catalog_id = kbCatalogService.getTmpCatalogByTopTcr(prof.my_top_tc_id);
			if (catalog_id == null) {
				catalog_ids[0] = kbCatalogService.insertTmpCatalogInTopTcr(prof);
			} else {
				catalog_ids[0] = catalog_id;
			}
			kbItem.setKbi_catalog_ids(catalog_ids);
		}
		kbItemAttachmentMapper.delete(kbItem.getKbi_id());
		if (!kbItem.getKbi_type().equals(KbItem.TYPE_ARTICLE) && !kbItem.getKbi_online().equals(KbItem.VIDEO_ONLINE)) {
			insertAttachment(kbItem, prof, nowDate);
		}
		insertCatalogAndTag(kbItem, prof, nowDate);
	}

	public KbItem downloadCount(long id) {
		KbItem kbItem = kbItemMapper.get(id);
		kbItem.setKbi_download_count(kbItem.getKbi_download_count() + 1);
		kbItemMapper.updateDownloadCount(kbItem);
		return kbItem;
	}

	public boolean hashAuthority(Long kbi_id, WizbiniLoader wizbini, loginProfile prof) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tc_independent", wizbini.cfgSysSetupadv.isTcIndependent());
		map.put("tcr_id", prof.my_top_tc_id);
		map.put("usr_ent_id", prof.usr_ent_id);
		map.put("kbi_id", kbi_id);
		return kbItemMapper.hashAuthority(map);
	}

	/**
	 * 为kb_item表添加了知识文件类型的参数，提供了这个方法，更新旧数据
	 * @return
	 */
	public void updateOldData() {
		List<KbItem> list = kbItemMapper.getKbItemListForUpdateOldData();
		Map<String, Object> params;
		for(KbItem k : list){
			params = new HashMap<String,Object>();
			params.put("kbi_id", k.getKbi_id());
			String fileType = k.getKbi_type();
			if(KbItem.TYPE_DOCUMENT.equals(k.getKbi_type())){
				k.setDocType(getDocType(k));
			}
			params.put("filetype",fileType);
			kbItemMapper.updateKbi_filetype(params);
		}
	}

	/**
	 * 获取培训中心【tcrId】（注：包括子中心）下管理员分享的知识数量
	 * @param tcrID
	 * @return
	 */
	public long getAdminShareCountByTcrIdAndChild(long tcrId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tcrId", tcrId);
		params.put("type","admin");
		return this.kbItemMapper.getkbItemCountByRootTcrId(params);
	}

	/**
	 * 获取培训中心【tcrId】（注：包括子中心）下管理员分享的知识数量
	 * @param tcrID
	 * @return
	 */
	public long getLearnerShareCountByTcrIdAndChild(long tcrId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tcrId", tcrId);
		params.put("type","learner");
		return this.kbItemMapper.getkbItemCountByRootTcrId(params);
	}

	/**
	 * 获取待审批的知识数量
	 * @param usr_ent_id
	 * @return
	 */
	public long selectWaitAppCount(long usr_ent_id, String current_role) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!AccessControlWZB.isRoleTcInd(current_role)){
			map.put("usr_ent_id", usr_ent_id);
			map.put("current_role", AcRole.ROLE_ADM_1);
		}else{
			map.put("usr_ent_id", usr_ent_id);
			map.put("current_role", current_role);
		}
		return this.kbItemMapper.selectWaitAppCount(map);
	}
	
	public String getKbTitle(long kbi_id){
		return this.kbItemMapper.getTitle(kbi_id);
	}
	
}