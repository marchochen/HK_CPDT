package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

/**
 * 群组
 * @author leon.li
 * 2014-8-7 下午5:35:32
 */
public class SnsGroup implements java.io.Serializable {
	private static final long serialVersionUID = -8354241976184561382L;
		/**
		 * pk
		 **/
		Long s_grp_id;
		/**
		 * 创建人id
		 **/
		Long s_grp_uid;
		/**
		 * 群组title
		 **/
		String s_grp_title;
		/**
		 * 群组简介
		 **/
		String s_grp_desc;
		/**
		 * 是否公开
		 **/
		Long s_grp_private;
		/**
		 * 创建人id
		 **/
		Long s_grp_create_uid;
		/**
		 * 创建时间
		 **/
		Date s_grp_create_datetime;
		/**
		 * 修改人
		 **/
		Long s_grp_update_uid;
		/**
		 * 修改时间
		 **/
		Date s_grp_update_datetime;
		/**
		 * 培训中心id
		 **/
		Long s_grp_tcr_id;
		/**
		 * 群组名片
		 **/
		String s_grp_card;
		/**
		 * 群组状态
		 **/
		String s_grp_status;
		/**
		 * 总成员数
		 **/
		Long member_total;
		/**
		 * 总信息数
		 **/
		Long message_total;
		/**
		 * 是否为培训管理员
		 */
		boolean s_grp_admin;
		
		/**
		 * 待审批
		 */
		long member_wait;
		
		String card_actual_path;
		
		SnsGroupMember s_gpm;
		
		RegUser user;
		
		List<SnsGroupMember> goupMemberList;
		
		public SnsGroup(){
		}

		public Long getS_grp_id() {
			return s_grp_id;
		}

		public void setS_grp_id(Long s_grp_id) {
			this.s_grp_id = s_grp_id;
		}

		public Long getS_grp_uid() {
			return s_grp_uid;
		}

		public void setS_grp_uid(Long s_grp_uid) {
			this.s_grp_uid = s_grp_uid;
		}

		public String getS_grp_title() {
			return s_grp_title;
		}

		public void setS_grp_title(String s_grp_title) {
			this.s_grp_title = s_grp_title;
		}

		public String getS_grp_desc() {
			return s_grp_desc;
		}

		public void setS_grp_desc(String s_grp_desc) {
			this.s_grp_desc = s_grp_desc;
		}

		public Long getS_grp_private() {
			return s_grp_private;
		}

		public void setS_grp_private(Long s_grp_private) {
			this.s_grp_private = s_grp_private;
		}

		public Long getS_grp_create_uid() {
			return s_grp_create_uid;
		}

		public void setS_grp_create_uid(Long s_grp_create_uid) {
			this.s_grp_create_uid = s_grp_create_uid;
		}

		public Date getS_grp_create_datetime() {
			return s_grp_create_datetime;
		}

		public void setS_grp_create_datetime(Date s_grp_create_datetime) {
			this.s_grp_create_datetime = s_grp_create_datetime;
		}

		public Long getS_grp_update_uid() {
			return s_grp_update_uid;
		}

		public void setS_grp_update_uid(Long s_grp_update_uid) {
			this.s_grp_update_uid = s_grp_update_uid;
		}

		public Date getS_grp_update_datetime() {
			return s_grp_update_datetime;
		}

		public void setS_grp_update_datetime(Date s_grp_update_datetime) {
			this.s_grp_update_datetime = s_grp_update_datetime;
		}

		public Long getS_grp_tcr_id() {
			return s_grp_tcr_id;
		}

		public void setS_grp_tcr_id(Long s_grp_tcr_id) {
			this.s_grp_tcr_id = s_grp_tcr_id;
		}

		public String getS_grp_card() {
			return s_grp_card;
		}

		public void setS_grp_card(String s_grp_card) {
			this.s_grp_card = s_grp_card;
		}

		public boolean isS_grp_admin() {
			return s_grp_admin;
		}

		public void setS_grp_admin(boolean s_grp_admin) {
			this.s_grp_admin = s_grp_admin;
		}

		public String getS_grp_status() {
			return s_grp_status;
		}

		public void setS_grp_status(String s_grp_status) {
			this.s_grp_status = s_grp_status;
		}

		public Long getMember_total() {
			return member_total;
		}

		public void setMember_total(Long member_total) {
			this.member_total = member_total;
		}

		public Long getMessage_total() {
			return message_total;
		}

		public void setMessage_total(Long message_total) {
			this.message_total = message_total;
		}

		public SnsGroupMember getS_gpm() {
			return s_gpm;
		}

		public void setS_gpm(SnsGroupMember s_gpm) {
			this.s_gpm = s_gpm;
		}

		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public String getCard_actual_path() {
			return card_actual_path;
		}

		public void setCard_actual_path(String card_actual_path) {
			this.card_actual_path = card_actual_path;
		}

		public List<SnsGroupMember> getGoupMemberList() {
			return goupMemberList;
		}

		public void setGoupMemberList(List<SnsGroupMember> goupMemberList) {
			this.goupMemberList = goupMemberList;
		}

		public Long getMember_wait() {
			return member_wait;
		}

		public void setMember_wait(Long member_wait) {
			this.member_wait = member_wait;
		}

		
		
		
	
}