package com.cwn.wizbank.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 评论
 * @author leon.li
 * 2014-8-7 下午5:44:58
 */
public class SnsComment implements java.io.Serializable {
	private static final long serialVersionUID = -1554164584174455769L;


		Long s_cmt_id;
		/**
		 *  评论人
		 **/
		Long s_cmt_uid;
		/**
		 * 评论内容
		 **/
		String s_cmt_content;
		/**
		 * 是否是回复
		 **/
		boolean s_cmt_is_reply;
		/**
		 * 回复给ID
		 **/
		Long s_cmt_reply_to_id;
		/**
		 * 评论时间
		 **/
		Date s_cmt_create_datetime;
		/**
		 * 是否匿名
		 **/
		Long s_cmt_anonymous;
		/**
		 * 评论模块
		 **/
		String s_cmt_module;
		/**
		 * 评论对象
		 **/
		Long s_cmt_target_id;
		
		/**
		 * 回复人Id
		 */
		Long s_cmt_reply_to_uid;
		
		/**
		 * 管理权限
		 */
		boolean isManage;
		
		/**
		 * 共有的管理权限
		 */
		boolean isCommonManage;
		
		List<SnsComment> replies = new ArrayList<SnsComment>();
		
		RegUser user;
	
		RegUser toUser;
		
		SnsCount snsCount;
		
		SnsValuationLog valuation;
		
		int is_user_like;
		
		public int getIs_user_like() {
			return is_user_like;
		}

		public void setIs_user_like(int is_user_like) {
			this.is_user_like = is_user_like;
		}

		public SnsValuationLog getValuation() {
			return valuation;
		}

		public void setValuation(SnsValuationLog valuation) {
			this.valuation = valuation;
		}

		public SnsCount getSnsCount() {
			return snsCount;
		}

		public void setSnsCount(SnsCount snsCount) {
			this.snsCount = snsCount;
		}

		public SnsComment(){
		}

		public Long getS_cmt_id() {
			return s_cmt_id;
		}

		public void setS_cmt_id(Long s_cmt_id) {
			this.s_cmt_id = s_cmt_id;
		}

		public Long getS_cmt_uid() {
			return s_cmt_uid;
		}

		public void setS_cmt_uid(Long s_cmt_uid) {
			this.s_cmt_uid = s_cmt_uid;
		}

		public String getS_cmt_content() {
			return s_cmt_content;
		}

		public void setS_cmt_content(String s_cmt_content) {
			this.s_cmt_content = s_cmt_content;
		}

		public boolean getS_cmt_is_reply() {
			return s_cmt_is_reply;
		}

		public void setS_cmt_is_reply(boolean s_cmt_is_reply) {
			this.s_cmt_is_reply = s_cmt_is_reply;
		}

		public Long getS_cmt_reply_to_id() {
			return s_cmt_reply_to_id;
		}

		public void setS_cmt_reply_to_id(Long s_cmt_reply_to_id) {
			this.s_cmt_reply_to_id = s_cmt_reply_to_id;
		}

		public Date getS_cmt_create_datetime() {
			return s_cmt_create_datetime;
		}

		public void setS_cmt_create_datetime(Date s_cmt_create_datetime) {
			this.s_cmt_create_datetime = s_cmt_create_datetime;
		}

		public Long getS_cmt_anonymous() {
			return s_cmt_anonymous;
		}

		public void setS_cmt_anonymous(Long s_cmt_anonymous) {
			this.s_cmt_anonymous = s_cmt_anonymous;
		}

		public String getS_cmt_module() {
			return s_cmt_module;
		}

		public void setS_cmt_module(String s_cmt_module) {
			this.s_cmt_module = s_cmt_module;
		}

		public Long getS_cmt_target_id() {
			return s_cmt_target_id;
		}

		public void setS_cmt_target_id(Long s_cmt_target_id) {
			this.s_cmt_target_id = s_cmt_target_id;
		}

		public List<SnsComment> getReplies() {
			return replies;
		}

		public void setReplies(List<SnsComment> replies) {
			this.replies = replies;
		}

		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public Long getS_cmt_reply_to_uid() {
			return s_cmt_reply_to_uid;
		}

		public void setS_cmt_reply_to_uid(Long s_cmt_reply_to_uid) {
			this.s_cmt_reply_to_uid = s_cmt_reply_to_uid;
		}

		public RegUser getToUser() {
			return toUser;
		}

		public void setToUser(RegUser toUser) {
			this.toUser = toUser;
		}

		public boolean isManage() {
			return isManage;
		}

		public void setManage(boolean isManage) {
			this.isManage = isManage;
		}

		public boolean isCommonManage() {
			return isCommonManage;
		}

		public void setCommonManage(boolean isCommonManage) {
			this.isCommonManage = isCommonManage;
		}

}