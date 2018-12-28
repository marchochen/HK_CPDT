package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.Map;


/**
 * 文章
 * @author leon.li
 * 2014-8-7 下午6:02:00
 */
public class Article implements java.io.Serializable {
	private static final long serialVersionUID = -4878457358624349831L;

		int art_id;
		/**
		 * 文章标题
		 **/
		String art_title;
		/**
		 * 图示
		 * */
		String art_icon_file;
		/**
		 * 文件简介
		 **/
		String art_introduction;
		/**
		 * 关键字
		 **/
		String art_keywords;
		/**
		 * 内容
		 **/
		String art_content;
		/**
		 * 作者id
		 **/
		int art_user_id;
		/**
		 * 创建时间
		 **/
		Date art_create_datetime;
		/**
		 * 更新时间
		 **/
		Date art_update_datetime;
		/**
		 * 修改人
		 **/
		int art_update_user_id;
		/**
		 * 来源
		 **/
		String art_location;
		/**
		 * 文章类型
		 **/
		String art_type;
		/**
		 * 文章状态， 0 删除 1发布 2未发布
		 **/
		int art_status;
		/**
		 * 培训中心id
		 **/
		int art_tcr_id;
		/**
		 * 是否推送到手机
		 **/
		int art_push_mobile;
		/**
		 * 是否是html，1 ： 是，表示要解析；   0：不是，表示不解析
		 **/
		int art_is_html;		
		
		/**
		 * 
		 */
		long art_access_count;
		/**
		 * 是否是新的文章
		 */
		int art_is_new;
		/**
		 * 评论总数
		 */
		long art_comment_count;
		
		RegUser user;
		
		TcTrainingCenter tcenter;
		
		ArticleType articleType;
		
		SnsCollect collect;
		
        SnsCount snsCount;
        
		SnsValuationLog userLike;
		
		Map<String,Object> sns;
		
		public Article(){
		}

		public int getArt_id() {
			return art_id;
		}

		public void setArt_id(int art_id) {
			this.art_id = art_id;
		}

		public String getArt_title() {
			return art_title;
		}

		public void setArt_title(String art_title) {
			this.art_title = art_title;
		}

       

		public String getArt_icon_file() {
			return art_icon_file;
		}

		public void setArt_icon_file(String art_icon_file) {
			this.art_icon_file = art_icon_file;
		}

		public String getArt_introduction() {
			return art_introduction;
		}

		public void setArt_introduction(String art_introduction) {
			this.art_introduction = art_introduction;
		}

		public String getArt_keywords() {
			return art_keywords;
		}

		public void setArt_keywords(String art_keywords) {
			this.art_keywords = art_keywords;
		}

		public String getArt_content() {
			return art_content;
		}

		public void setArt_content(String art_content) {
			this.art_content = art_content;
		}

		public int getArt_user_id() {
			return art_user_id;
		}

		public void setArt_user_id(int art_user_id) {
			this.art_user_id = art_user_id;
		}

		public Date getArt_create_datetime() {
			return art_create_datetime;
		}

		public void setArt_create_datetime(Date art_create_datetime) {
			this.art_create_datetime = art_create_datetime;
		}

		public Date getArt_update_datetime() {
			return art_update_datetime;
		}

		public void setArt_update_datetime(Date art_update_datetime) {
			this.art_update_datetime = art_update_datetime;
		}

		public int getArt_update_user_id() {
			return art_update_user_id;
		}

		public void setArt_update_user_id(int art_update_user_id) {
			this.art_update_user_id = art_update_user_id;
		}

		public String getArt_location() {
			return art_location;
		}

		public void setArt_location(String art_location) {
			this.art_location = art_location;
		}

		public String getArt_type() {
			return art_type;
		}

		public void setArt_type(String art_type) {
			this.art_type = art_type;
		}

		public int getArt_status() {
			return art_status;
		}

		public void setArt_status(int art_status) {
			this.art_status = art_status;
		}

		public int getArt_tcr_id() {
			return art_tcr_id;
		}

		public void setArt_tcr_id(int art_tcr_id) {
			this.art_tcr_id = art_tcr_id;
		}

		public int getArt_push_mobile() {
			return art_push_mobile;
		}

		public void setArt_push_mobile(int art_push_mobile) {
			this.art_push_mobile = art_push_mobile;
		}

		public int getArt_is_html() {
			return art_is_html;
		}

		public void setArt_is_html(int art_is_html) {
			this.art_is_html = art_is_html;
		}

		public int getArt_is_new() {
			return art_is_new;
		}

		public void setArt_is_new(int art_is_new) {
			this.art_is_new = art_is_new;
		}

		public long getArt_comment_count() {
			return art_comment_count;
		}

		public void setArt_comment_count(long art_comment_count) {
			this.art_comment_count = art_comment_count;
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

		public ArticleType getArticleType() {
			return articleType;
		}

		public void setArticleType(ArticleType articleType) {
			this.articleType = articleType;
		}

		public SnsCollect getSnsCollect() {
			return collect;
		}

		public void setSnsCollect(SnsCollect collect) {
			this.collect = collect;
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

		public long getArt_access_count() {
			return art_access_count;
		}

		public void setArt_access_count(long art_access_count) {
			this.art_access_count = art_access_count;
		}

		public Map<String, Object> getSns() {
			return sns;
		}

		public void setSns(Map<String, Object> sns) {
			this.sns = sns;
		}

}