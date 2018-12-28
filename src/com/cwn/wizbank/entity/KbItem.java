package com.cwn.wizbank.entity;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 知识中心 - 知识
 */
public class KbItem {
	// 类型
	public final static String TYPE_ARTICLE = "ARTICLE"; // 文章
	public final static String TYPE_DOCUMENT = "DOCUMENT"; // 文档
	public final static String TYPE_VEDIO = "VEDIO"; // 视频
	public final static String TYPE_IMAGE = "IMAGE"; // 图片

	// 状态
	public final static String STATUS_ON = "ON"; // 已发布
	public final static String STATUS_OFF = "OFF"; // 未发布

	// 审批状态
	public final static String APP_STATUS_PENDING = "PENDING"; // 待审批
	public final static String APP_STATUS_APPROVED = "APPROVED"; // 已审批
	public final static String APP_STATUS_REAPPROVL = "REAPPROVAL"; // 拒绝审批

	// 下载控制
	public final static String DOWNLOAD_ALLOW = "ALLOW"; // 允许下载
	public final static String DOWNLOAD_INTERDICT = "INTERDICT"; // 禁止下载
	
	public final static String VIDEO_ONLINE = "ONLINE"; //在线视频
	public final static String VIDEO_OFFLINE = "OFFLINE"; //线下视频

	private Long kbi_id;
	private String kbi_title;
	private String kbi_desc;
	private String kbi_image;
	private String kbi_content;
	private String kbi_status;
	private String kbi_type;
	private String kbi_app_status;
	private Timestamp kbi_approve_datetime;
	private String kbi_approve_user_id;
	private Timestamp kbi_publish_datetime;
	private String kbi_publish_user_id;
	private Timestamp kbi_create_datetime;
	private String kbi_create_user_id;
	private Timestamp kbi_update_datetime;
	private String kbi_update_user_id;
	private Long[] kbi_tag_ids;
	private Long[] kbi_catalog_ids;
	private String kbi_download;
	private Long kbi_download_count;
	private String kbi_online;
	private String kbi_filetype;

	/**
	 * 文档预览地址，该属性只对文档类型的知识有效
	 */
	private String previewUrl;
	
	//文档类型
	private String docType;
	// 知识所属目录
	private List<KbCatalog> catalogues = new LinkedList<KbCatalog>();
	// 知识拥有的附件
	private List<KbAttachment> attachments = new LinkedList<KbAttachment>();
	// 知识对应的标签
	private List<Tag> tags = new LinkedList<Tag>();
	// 添加用户
	private String usr_display_bil;
	// 审批人
	private String kbi_approve_usr_display_bil;
	// 评论数
	private Long kbi_comment_count;

	private Long kbi_access_count;

	private String kbi_default_image;

	// 知识拥有的附件
	KbAttachment imageAttachment = new KbAttachment();

	SnsCount snsCount;
	// 点赞数
	Long s_cnt_like_count;

	SnsValuationLog userLike;

	Map<String, Object> sns;
	
	private String encrypt_kbi_id; 

	public SnsCollect getCollect() {
		return collect;
	}

	public void setCollect(SnsCollect collect) {
		this.collect = collect;
	}

	SnsCollect collect;

	public Long getKbi_id() {
		return kbi_id;
	}

	public void setKbi_id(Long kbi_id) {
		this.kbi_id = kbi_id;
	}

	public String getKbi_title() {
		return kbi_title;
	}

	public void setKbi_title(String kbi_title) {
		this.kbi_title = kbi_title;
	}

	public String getKbi_desc() {
		return kbi_desc;
	}

	public void setKbi_desc(String kbi_desc) {
		this.kbi_desc = kbi_desc;
	}

	public String getKbi_image() {
		return kbi_image;
	}

	public void setKbi_image(String kbi_image) {
		this.kbi_image = kbi_image;
	}

	public String getKbi_content() {
		return kbi_content;
	}

	public void setKbi_content(String kbi_content) {
		this.kbi_content = kbi_content;
	}

	public String getKbi_status() {
		return kbi_status;
	}

	public void setKbi_status(String kbi_status) {
		this.kbi_status = kbi_status;
	}

	public String getKbi_type() {
		return kbi_type;
	}

	public void setKbi_type(String kbi_type) {
		this.kbi_type = kbi_type;
	}

	public String getKbi_app_status() {
		return kbi_app_status;
	}

	public void setKbi_app_status(String kbi_app_status) {
		this.kbi_app_status = kbi_app_status;
	}

	public Timestamp getKbi_approve_datetime() {
		return kbi_approve_datetime;
	}

	public void setKbi_approve_datetime(Timestamp kbi_approve_datetime) {
		this.kbi_approve_datetime = kbi_approve_datetime;
	}

	public Timestamp getKbi_publish_datetime() {
		return kbi_publish_datetime;
	}

	public void setKbi_publish_datetime(Timestamp kbi_publish_datetime) {
		this.kbi_publish_datetime = kbi_publish_datetime;
	}

	public Timestamp getKbi_create_datetime() {
		return kbi_create_datetime;
	}

	public void setKbi_create_datetime(Timestamp kbi_create_datetime) {
		this.kbi_create_datetime = kbi_create_datetime;
	}

	public Timestamp getKbi_update_datetime() {
		return kbi_update_datetime;
	}

	public void setKbi_update_datetime(Timestamp kbi_update_datetime) {
		this.kbi_update_datetime = kbi_update_datetime;
	}

	public Long[] getKbi_tag_ids() {
		return kbi_tag_ids;
	}

	public void setKbi_tag_ids(Long[] kbi_tag_ids) {
		this.kbi_tag_ids = kbi_tag_ids;
	}

	public Long[] getKbi_catalog_ids() {
		return kbi_catalog_ids;
	}

	public void setKbi_catalog_ids(Long[] kbi_catalog_ids) {
		this.kbi_catalog_ids = kbi_catalog_ids;
	}

	public List<KbCatalog> getCatalogues() {
		return catalogues;
	}

	public void setCatalogues(List<KbCatalog> catalogues) {
		this.catalogues = catalogues;
	}

	public List<KbAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<KbAttachment> attachments) {
		this.attachments = attachments;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getUsr_display_bil() {
		return usr_display_bil;
	}

	public void setUsr_display_bil(String usr_display_bil) {
		this.usr_display_bil = usr_display_bil;
	}

	public String getKbi_approve_usr_display_bil() {
		return kbi_approve_usr_display_bil;
	}

	public void setKbi_approve_usr_display_bil(String kbi_approve_usr_display_bil) {
		this.kbi_approve_usr_display_bil = kbi_approve_usr_display_bil;
	}

	public Long getKbi_comment_count() {
		return kbi_comment_count;
	}

	public void setKbi_comment_count(Long kbi_comment_count) {
		this.kbi_comment_count = kbi_comment_count;
	}

	public Long getKbi_access_count() {
		return kbi_access_count;
	}

	public void setKbi_access_count(Long kbi_access_count) {
		this.kbi_access_count = kbi_access_count;
	}

	public SnsCount getSnsCount() {
		return snsCount;
	}

	public void setSnsCount(SnsCount snsCount) {
		this.snsCount = snsCount;
	}

	public KbAttachment getImageAttachment() {
		return imageAttachment;
	}

	public void setImageAttachment(KbAttachment imageAttachment) {
		this.imageAttachment = imageAttachment;
	}

	public Long getS_cnt_like_count() {
		return s_cnt_like_count;
	}

	public void setS_cnt_like_count(Long s_cnt_like_count) {
		this.s_cnt_like_count = s_cnt_like_count;
	}

	public SnsValuationLog getUserLike() {
		return userLike;
	}

	public void setUserLike(SnsValuationLog userLike) {
		this.userLike = userLike;
	}

	public Map<String, Object> getSns() {
		return sns;
	}

	public void setSns(Map<String, Object> sns) {
		this.sns = sns;
	}

	public String getKbi_download() {
		return kbi_download;
	}

	public void setKbi_download(String kbi_download) {
		this.kbi_download = kbi_download;
	}

	public Long getKbi_download_count() {
		return kbi_download_count;
	}

	public void setKbi_download_count(Long kbi_download_count) {
		this.kbi_download_count = kbi_download_count;
	}

	public String getKbi_default_image() {
		return kbi_default_image;
	}

	public void setKbi_default_image(String kbi_default_image) {
		this.kbi_default_image = kbi_default_image;
	}

	public String getKbi_approve_user_id() {
		return kbi_approve_user_id;
	}

	public void setKbi_approve_user_id(String kbi_approve_user_id) {
		this.kbi_approve_user_id = kbi_approve_user_id;
	}

	public String getKbi_publish_user_id() {
		return kbi_publish_user_id;
	}

	public void setKbi_publish_user_id(String kbi_publish_user_id) {
		this.kbi_publish_user_id = kbi_publish_user_id;
	}

	public String getKbi_create_user_id() {
		return kbi_create_user_id;
	}

	public void setKbi_create_user_id(String kbi_create_user_id) {
		this.kbi_create_user_id = kbi_create_user_id;
	}

	public String getKbi_update_user_id() {
		return kbi_update_user_id;
	}

	public void setKbi_update_user_id(String kbi_update_user_id) {
		this.kbi_update_user_id = kbi_update_user_id;
	}

	public String getKbi_online() {
		return kbi_online;
	}

	public void setKbi_online(String kbi_online) {
		this.kbi_online = kbi_online;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getKbi_filetype() {
		return kbi_filetype;
	}

	public void setKbi_filetype(String kbi_filetype) {
		this.kbi_filetype = kbi_filetype;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	public String getEncrypt_kbi_id() {
		return encrypt_kbi_id;
	}

	public void setEncrypt_kbi_id(String encrypt_kbi_id) {
		this.encrypt_kbi_id = encrypt_kbi_id;
	}
	
}
