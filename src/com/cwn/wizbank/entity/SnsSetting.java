package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 
 * 0所有人可见，2所有不可见，1我关注的人可见
 */
public class SnsSetting implements java.io.Serializable {
	private static final long serialVersionUID = -1361534713616814658L;

		Long s_set_id;
		/**
		 * 
		 **/
		Long s_set_uid;
		/**
		 *  评论
		 **/
		Long s_set_comment;
		/**
		 *  群组
		 **/
		Long s_set_group;
		/**
		 * 我的粉丝
		 **/
		Long s_set_my_follow;
		/**
		 * 我的关注
		 **/
		Long s_set_my_fans;
		/**
		 * 我的分享
		 **/
		Long s_set_share;
		/**
		 * 我的评价
		 **/
		Long s_set_valuation;
		/**
		 * 我的赞
		 **/
		Long s_set_like;
		/**
		 * 我的动态
		 **/
		Long s_set_doing;
		/**
		 * 全名
		 **/
		Long s_set_prof_fullname;
		/**
		 * 出生日期
		 **/
		Long s_set_prof_bday;
		/**
		 * 物业管理师证书所在地
		 **/
		Long s_set_prof_cert_location;
		/**
		 * 政治面貌
		 **/
		Long s_set_prof_pol_land;
		/**
		 * 邮箱
		 **/
		Long s_set_prof_email;
		/**
		 * 电话
		 **/
		Long s_set_prof_tel;
		/**
		 * 传真
		 **/
		Long s_set_prof_fax;
		/**
		 * 所在企业
		 **/
		Long s_set_prof_company;
		/**
		 * 昵称
		 **/
		Long s_set_prof_nickname;
		/**
		 * 加入公司日期
		 **/
		Long s_set_prof_join_date;
		/**
		 * 职务
		 **/
		Long s_set_prof_grade;
		/**
		 * msn
		 **/
		Long s_set_prof_msn;
		/**
		 * 我的档案
		 **/
		Long s_set_my_files;
		/**
		 * 我的收藏
		 **/
		Long s_set_my_collection;
		/**
		 * 我的积分
		 **/
		Long s_set_my_credit;
		/**
		 * 我的学习记录
		 **/
		Long s_set_my_learning_record;
		/**
		 * 我的学习概况
		 **/
		Long s_set_my_learning_situation;
		
		Long s_set_voting;
		
		
		/**
		 * 上司id
		 **/
		Long spt_source_usr_ent_id;
		
		Long s_set_create_uid;
		Date s_set_create_datetime;
		Long s_set_update_uid;
		Date s_set_update_datetime;
		
		SnsAttention snsAttention;
		
		public SnsSetting(){
		}

		public Long getS_set_id() {
			return s_set_id;
		}

		public void setS_set_id(Long s_set_id) {
			this.s_set_id = s_set_id;
		}

		public Long getS_set_uid() {
			return s_set_uid;
		}

		public void setS_set_uid(Long s_set_uid) {
			this.s_set_uid = s_set_uid;
		}

		public Long getS_set_comment() {
			return s_set_comment;
		}

		public void setS_set_comment(Long s_set_comment) {
			this.s_set_comment = s_set_comment;
		}

		public Long getS_set_group() {
			return s_set_group;
		}

		public void setS_set_group(Long s_set_group) {
			this.s_set_group = s_set_group;
		}

		public Long getS_set_my_follow() {
			return s_set_my_follow;
		}

		public void setS_set_my_follow(Long s_set_my_follow) {
			this.s_set_my_follow = s_set_my_follow;
		}

		public Long getS_set_my_fans() {
			return s_set_my_fans;
		}

		public void setS_set_my_fans(Long s_set_my_fans) {
			this.s_set_my_fans = s_set_my_fans;
		}

		public Long getS_set_share() {
			return s_set_share;
		}

		public void setS_set_share(Long s_set_share) {
			this.s_set_share = s_set_share;
		}

		public Long getS_set_valuation() {
			return s_set_valuation;
		}

		public void setS_set_valuation(Long s_set_valuation) {
			this.s_set_valuation = s_set_valuation;
		}

		public Long getS_set_like() {
			return s_set_like;
		}

		public void setS_set_like(Long s_set_like) {
			this.s_set_like = s_set_like;
		}

		public Long getS_set_doing() {
			return s_set_doing;
		}

		public void setS_set_doing(Long s_set_doing) {
			this.s_set_doing = s_set_doing;
		}

		public Long getS_set_create_uid() {
			return s_set_create_uid;
		}

		public void setS_set_create_uid(Long s_set_create_uid) {
			this.s_set_create_uid = s_set_create_uid;
		}

		public Date getS_set_create_datetime() {
			return s_set_create_datetime;
		}

		public void setS_set_create_datetime(Date s_set_create_datetime) {
			this.s_set_create_datetime = s_set_create_datetime;
		}

		public Long getS_set_update_uid() {
			return s_set_update_uid;
		}

		public void setS_set_update_uid(Long s_set_update_uid) {
			this.s_set_update_uid = s_set_update_uid;
		}

		public Date getS_set_update_datetime() {
			return s_set_update_datetime;
		}

		public void setS_set_update_datetime(Date s_set_update_datetime) {
			this.s_set_update_datetime = s_set_update_datetime;
		}

		public Long getS_set_prof_fullname() {
			return s_set_prof_fullname;
		}

		public void setS_set_prof_fullname(Long s_set_prof_fullname) {
			this.s_set_prof_fullname = s_set_prof_fullname;
		}

		public Long getS_set_prof_bday() {
			return s_set_prof_bday;
		}

		public void setS_set_prof_bday(Long s_set_prof_bday) {
			this.s_set_prof_bday = s_set_prof_bday;
		}

		public Long getS_set_prof_cert_location() {
			return s_set_prof_cert_location;
		}

		public void setS_set_prof_cert_location(Long s_set_prof_cert_location) {
			this.s_set_prof_cert_location = s_set_prof_cert_location;
		}

		public Long getS_set_prof_pol_land() {
			return s_set_prof_pol_land;
		}

		public void setS_set_prof_pol_land(Long s_set_prof_pol_land) {
			this.s_set_prof_pol_land = s_set_prof_pol_land;
		}

		public Long getS_set_prof_email() {
			return s_set_prof_email;
		}

		public void setS_set_prof_email(Long s_set_prof_email) {
			this.s_set_prof_email = s_set_prof_email;
		}

		public Long getS_set_prof_tel() {
			return s_set_prof_tel;
		}

		public void setS_set_prof_tel(Long s_set_prof_tel) {
			this.s_set_prof_tel = s_set_prof_tel;
		}

		public Long getS_set_prof_fax() {
			return s_set_prof_fax;
		}

		public void setS_set_prof_fax(Long s_set_prof_fax) {
			this.s_set_prof_fax = s_set_prof_fax;
		}

		public Long getS_set_prof_company() {
			return s_set_prof_company;
		}

		public void setS_set_prof_company(Long s_set_prof_company) {
			this.s_set_prof_company = s_set_prof_company;
		}

		public Long getS_set_prof_nickname() {
			return s_set_prof_nickname;
		}

		public void setS_set_prof_nickname(Long s_set_prof_nickname) {
			this.s_set_prof_nickname = s_set_prof_nickname;
		}

		public Long getS_set_prof_join_date() {
			return s_set_prof_join_date;
		}

		public void setS_set_prof_join_date(Long s_set_prof_join_date) {
			this.s_set_prof_join_date = s_set_prof_join_date;
		}

		public Long getS_set_prof_grade() {
			return s_set_prof_grade;
		}

		public void setS_set_prof_grade(Long s_set_prof_grade) {
			this.s_set_prof_grade = s_set_prof_grade;
		}

		public Long getS_set_prof_msn() {
			return s_set_prof_msn;
		}

		public void setS_set_prof_msn(Long s_set_prof_msn) {
			this.s_set_prof_msn = s_set_prof_msn;
		}

		public Long getS_set_my_files() {
			return s_set_my_files;
		}

		public void setS_set_my_files(Long s_set_my_files) {
			this.s_set_my_files = s_set_my_files;
		}

		public Long getS_set_my_collection() {
			return s_set_my_collection;
		}

		public void setS_set_my_collection(Long s_set_my_collection) {
			this.s_set_my_collection = s_set_my_collection;
		}

		public Long getS_set_my_credit() {
			return s_set_my_credit;
		}

		public void setS_set_my_credit(Long s_set_my_credit) {
			this.s_set_my_credit = s_set_my_credit;
		}

		public Long getS_set_my_learning_record() {
			return s_set_my_learning_record;
		}

		public void setS_set_my_learning_record(Long s_set_my_learning_record) {
			this.s_set_my_learning_record = s_set_my_learning_record;
		}

		public Long getS_set_my_learning_situation() {
			return s_set_my_learning_situation;
		}

		public void setS_set_my_learning_situation(Long s_set_my_learning_situation) {
			this.s_set_my_learning_situation = s_set_my_learning_situation;
		}

		public Long getSpt_source_usr_ent_id() {
			return spt_source_usr_ent_id;
		}

		public void setSpt_source_usr_ent_id(Long spt_source_usr_ent_id) {
			this.spt_source_usr_ent_id = spt_source_usr_ent_id;
		}

		public SnsAttention getSnsAttention() {
			return snsAttention;
		}

		public void setSnsAttention(SnsAttention snsAttention) {
			this.snsAttention = snsAttention;
		}

		public Long getS_set_voting() {
			return s_set_voting;
		}

		public void setS_set_voting(Long s_set_voting) {
			this.s_set_voting = s_set_voting;
		}
	
		
	
}