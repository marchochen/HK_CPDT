package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

/**
 * 动态
 * @author leon.li
 * 2014-8-7 下午5:47:04
 */
public class SnsDoing implements java.io.Serializable {

	private static final long serialVersionUID = -7156595788543332894L;
	/**
	 * pk
	 **/
	Long s_doi_id;
	/**
	 * 行为（干了什么产生的动态）
	 **/
	String s_doi_act;
	
	Long s_doi_act_id;
	/**
	 * 动态标题
	 **/
	String s_doi_title;
	/**
	 * 用户
	 **/
	Long s_doi_uid;
	/**
	 * 创建时间
	 **/
	Date s_doi_create_datetime;
	/**
	 * url
	 **/
	String s_doi_url;
	/**
	 * 模块
	 **/
	String s_doi_module;
	/**
	 * 动态对象（是什么产生的动态）
	 **/
	Long s_doi_target_id;
	
	/**
	 * 通知产生的记录，回复了谁
	 */
	long s_doi_reply_id;
	
	int is_user_like;
	
	String s_doi_act_str;
	
	String s_doi_target_type;
	
	long s_doi_operator_uid;
	
	RegUser user;
	
	RegUser operator;
	
	TcTrainingCenter tcenter;
	
	RegUserExtension userExtension;
	
	List<SnsComment> replies;

	SnsCount snsCount;
	
	SnsValuationLog userLike;
		
	List<ModuleTempFile> fileList;
	
	SnsShare snsShare;
	
	public Long getS_doi_id() {
		return s_doi_id;
	}
	public List<SnsComment> getReplies() {
		return replies;
	}
	public void setReplies(List<SnsComment> replies) {
		this.replies = replies;
	}
	public void setS_doi_id(Long s_doi_id) {
		this.s_doi_id = s_doi_id;
	}
	public String getS_doi_act() {
		return s_doi_act;
	}
	public void setS_doi_act(String s_doi_act) {
		this.s_doi_act = s_doi_act;
	}
	public String getS_doi_title() {
		return s_doi_title;
	}
	public void setS_doi_title(String s_doi_title) {
		this.s_doi_title = s_doi_title;
	}
	public Long getS_doi_uid() {
		return s_doi_uid;
	}
	public void setS_doi_uid(Long s_doi_uid) {
		this.s_doi_uid = s_doi_uid;
	}
	public Date getS_doi_create_datetime() {
		return s_doi_create_datetime;
	}
	public void setS_doi_create_datetime(Date s_doi_create_datetime) {
		this.s_doi_create_datetime = s_doi_create_datetime;
	}
	public String getS_doi_url() {
		return s_doi_url;
	}
	public void setS_doi_url(String s_doi_url) {
		this.s_doi_url = s_doi_url;
	}
	public String getS_doi_module() {
		return s_doi_module;
	}
	public void setS_doi_module(String s_doi_module) {
		this.s_doi_module = s_doi_module;
	}
	public Long getS_doi_target_id() {
		return s_doi_target_id;
	}
	public void setS_doi_target_id(Long s_doi_target_id) {
		this.s_doi_target_id = s_doi_target_id;
	}
	public RegUser getUser() {
		return user;
	}
	public void setUser(RegUser user) {
		this.user = user;
	}
	public TcTrainingCenter getTcenter() {
		return tcenter;
	}
	public void setTcenter(TcTrainingCenter tcenter) {
		this.tcenter = tcenter;
	}
	public RegUserExtension getUserExtension() {
		return userExtension;
	}
	public void setUserExtension(RegUserExtension userExtension) {
		this.userExtension = userExtension;
	}
	public SnsCount getSnsCount() {
		return snsCount;
	}
	public void setSnsCount(SnsCount snsCount) {
		this.snsCount = snsCount;
	}
	public SnsValuationLog getUserLike() {
		return userLike;
	}
	public void setUserLike(SnsValuationLog userLike) {
		this.userLike = userLike;
	}
	public List<ModuleTempFile> getFileList() {
		return fileList;
	}
	public void setFileList(List<ModuleTempFile> fileList) {
		this.fileList = fileList;
	}
	public Long getS_doi_act_id() {
		return s_doi_act_id;
	}
	public void setS_doi_act_id(Long s_doi_act_id) {
		this.s_doi_act_id = s_doi_act_id;
	}
	public int getIs_user_like() {
		return is_user_like;
	}
	public void setIs_user_like(int is_user_like) {
		this.is_user_like = is_user_like;
	}
	public String getS_doi_act_str() {
		return s_doi_act_str;
	}
	public void setS_doi_act_str(String s_doi_act_str) {
		this.s_doi_act_str = s_doi_act_str;
	}
	public long getS_doi_reply_id() {
		return s_doi_reply_id;
	}
	public void setS_doi_reply_id(long s_doi_reply_id) {
		this.s_doi_reply_id = s_doi_reply_id;
	}
	public String getS_doi_target_type() {
		return s_doi_target_type;
	}
	public void setS_doi_target_type(String s_doi_target_type) {
		this.s_doi_target_type = s_doi_target_type;
	}
	public long getS_doi_operator_uid() {
		return s_doi_operator_uid;
	}
	public void setS_doi_operator_uid(long s_doi_operator_uid) {
		this.s_doi_operator_uid = s_doi_operator_uid;
	}
	public RegUser getOperator() {
		return operator;
	}
	public void setOperator(RegUser operator) {
		this.operator = operator;
	}
	public SnsShare getSnsShare() {
		return snsShare;
	}
	public void setSnsShare(SnsShare snsShare) {
		this.snsShare = snsShare;
	}
	
	

}