package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

public class KnowQuestion implements java.io.Serializable {
	private static final long serialVersionUID = -7119341781995764881L;

	public static final String TYPE_UNSOLVED = "UNSOLVED";
	public static final String TYPE_FAQ = "FAQ";
	public static final String TYPE_SOLVED = "SOLVED";
	public static final String TYPE_POPULAR = "POPULAR";

	/**
	 * pk null
	 **/
	Long que_id;
	/**
	 * null
	 **/
	String que_title;
	/**
	 * null
	 **/
	Date que_answered_timestamp;
	/**
	 * null
	 **/
	Integer que_popular_ind;
	/**
	 * null
	 **/
	Date que_popular_timestamp;
	/**
	 * null
	 **/
	Integer que_reward_credits;
	/**
	 * null
	 **/
	String que_status;
	/**
	 * null
	 **/
	Long que_create_ent_id;
	/**
	 * null
	 **/
	Date que_create_timestamp;
	/**
	 * null
	 **/
	Long que_update_ent_id;
	/**
	 * null
	 **/
	Date que_update_timestamp;
	/**
	 * null
	 **/
	String que_content;
	/**
	 * null
	 **/
	String que_type;
	/**
	 * 回答数
	 **/
	Long ask_num;
	/**
	 * 问题数量
	 **/
	Long que_num;
	/**
	 * 悬赏积分数
	 **/
	Double que_bounty;
	/**
	 * 搜索内容
	 **/
	String searchContent;
	/**
	 * 邀请的讲师id
	 */
	String que_ask_ent_ids;
	
	/**
	 * 回答问题时邮箱类型
	 */
	boolean type_sys;
	
	KnowCatalog knowCatalog;

	KnowVoteDetail knowVoteDetail;

	Knowanswer knowAnswer;

	KnowCatalogRelation knowCatalogRelation;

	RegUser user;
	
	/**
	 * 文件列表
	 */
	List<ModuleTempFile> fileList;

	public KnowQuestion() {
	}

	public Long getQue_id() {
		return this.que_id;
	}

	public void setQue_id(Long que_id) {
		this.que_id = que_id;
	}

	public String getQue_title() {
		return this.que_title;
	}

	public void setQue_title(String que_title) {
		this.que_title = que_title;
	}

	public Date getQue_answered_timestamp() {
		return this.que_answered_timestamp;
	}

	public void setQue_answered_timestamp(Date que_answered_timestamp) {
		this.que_answered_timestamp = que_answered_timestamp;
	}

	public Integer getQue_popular_ind() {
		return this.que_popular_ind;
	}

	public void setQue_popular_ind(Integer que_popular_ind) {
		this.que_popular_ind = que_popular_ind;
	}

	public Date getQue_popular_timestamp() {
		return this.que_popular_timestamp;
	}

	public void setQue_popular_timestamp(Date que_popular_timestamp) {
		this.que_popular_timestamp = que_popular_timestamp;
	}

	public Integer getQue_reward_credits() {
		return this.que_reward_credits;
	}

	public void setQue_reward_credits(Integer que_reward_credits) {
		this.que_reward_credits = que_reward_credits;
	}

	public String getQue_status() {
		return this.que_status;
	}

	public void setQue_status(String que_status) {
		this.que_status = que_status;
	}

	public Long getQue_create_ent_id() {
		return this.que_create_ent_id;
	}

	public void setQue_create_ent_id(Long que_create_ent_id) {
		this.que_create_ent_id = que_create_ent_id;
	}

	public Date getQue_create_timestamp() {
		return this.que_create_timestamp;
	}

	public void setQue_create_timestamp(Date que_create_timestamp) {
		this.que_create_timestamp = que_create_timestamp;
	}

	public Long getQue_update_ent_id() {
		return this.que_update_ent_id;
	}

	public void setQue_update_ent_id(Long que_update_ent_id) {
		this.que_update_ent_id = que_update_ent_id;
	}

	public Date getQue_update_timestamp() {
		return this.que_update_timestamp;
	}

	public void setQue_update_timestamp(Date que_update_timestamp) {
		this.que_update_timestamp = que_update_timestamp;
	}

	public String getQue_content() {
		return this.que_content;
	}

	public void setQue_content(String que_content) {
		this.que_content = que_content;
	}

	public String getQue_type() {
		return this.que_type;
	}

	public void setQue_type(String que_type) {
		this.que_type = que_type;
	}

	public KnowCatalog getKnowCatalog() {
		return knowCatalog;
	}

	public void setKnowCatalog(KnowCatalog knowCatalog) {
		this.knowCatalog = knowCatalog;
	}

	public Long getAsk_num() {
		return ask_num;
	}

	public void setAsk_num(Long ask_num) {
		this.ask_num = ask_num;
	}

	public Long getQue_num() {
		return que_num;
	}

	public void setQue_num(Long que_num) {
		this.que_num = que_num;
	}

	public Double getQue_bounty() {
		return que_bounty;
	}

	public void setQue_bounty(Double que_bounty) {
		this.que_bounty = que_bounty;
	}

	public String getSearchContent() {
		return searchContent;
	}

	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}

	public KnowVoteDetail getKnowVoteDetail() {
		return knowVoteDetail;
	}

	public void setKnowVoteDetail(KnowVoteDetail knowVoteDetail) {
		this.knowVoteDetail = knowVoteDetail;
	}

	public Knowanswer getKnowAnswer() {
		return knowAnswer;
	}

	public void setKnowAnswer(Knowanswer knowAnswer) {
		this.knowAnswer = knowAnswer;
	}

	public KnowCatalogRelation getKnowCatalogRelation() {
		return knowCatalogRelation;
	}

	public void setKnowCatalogRelation(KnowCatalogRelation knowCatalogRelation) {
		this.knowCatalogRelation = knowCatalogRelation;
	}

	public RegUser getRegUser() {
		return user;
	}

	public void setRegUser(RegUser user) {
		this.user = user;
	}

	public String getQue_ask_ent_ids() {
		return que_ask_ent_ids;
	}

	public void setQue_ask_ent_ids(String que_ask_ent_ids) {
		this.que_ask_ent_ids = que_ask_ent_ids;
	}

	public RegUser getUser() {
		return user;
	}

	public void setUser(RegUser user) {
		this.user = user;
	}

	public void setQue_bounty(double que_bounty) {
		this.que_bounty = que_bounty;
	}

	public boolean isType_sys() {
		return type_sys;
	}

	public void setType_sys(boolean type_sys) {
		this.type_sys = type_sys;
	}

	public List<ModuleTempFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<ModuleTempFile> fileList) {
		this.fileList = fileList;
	}
	
	

}