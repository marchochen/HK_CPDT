package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 公告
 * @author leon.li
 * 2014-8-1 上午9:48:26
 */
public class Message implements java.io.Serializable {
	private static final long serialVersionUID = -5568518316549184373L;
		/**
		 * pk
		 *
		 **/
		Long msg_id;
		/**
		 * 发消息人
		 **/
		String msg_usr_id;
		/**
		 * 消息类型
		 **/
		String msg_type;
		/**
		 * 消息标题
		 **/
		String msg_title;
		/**
		 * 内容
		 **/
		String msg_body;
		/**
		 * 开始时间
		 **/
		Date msg_begin_date;
		/**
		 * 结束时间
		 **/
		Date msg_end_date;
		/**
		 * 状态
		 **/
		String msg_status;
		/**
		 * 资源id
		 **/
		Long msg_res_id;
		/**
		 * null
		 **/
		Long msg_root_ent_id;
		/**
		 * null
		 **/
		Date msg_upd_date;
		/**
		 * null
		 **/
		String msg_level;
		/**
		 * null
		 **/
		Long msg_tcr_id;
		/**
		 * null
		 **/
		String msg_icon;
		/**
		 * null
		 **/
		Long msg_mobile_ind;
		/**
		 * 是否需要回执
		 **/
		boolean msg_receipt;
		
		boolean newest_ind;
		
		/**
		 * 当前用户是否已经阅读该message的辅助字段
		 */
		boolean curUserIsRead;
		
		private RegUser user;
		
		private TcTrainingCenter tcenter;
		
		public Message(){
		}

		public Long getMsg_id() {
			return msg_id;
		}

		public void setMsg_id(Long msg_id) {
			this.msg_id = msg_id;
		}

		public String getMsg_usr_id() {
			return msg_usr_id;
		}

		public void setMsg_usr_id(String msg_usr_id) {
			this.msg_usr_id = msg_usr_id;
		}

		public String getMsg_type() {
			return msg_type;
		}

		public void setMsg_type(String msg_type) {
			this.msg_type = msg_type;
		}

		public String getMsg_title() {
			return msg_title;
		}

		public void setMsg_title(String msg_title) {
			this.msg_title = msg_title;
		}

		public String getMsg_body() {
			return msg_body;
		}

		public void setMsg_body(String msg_body) {
			this.msg_body = msg_body;
		}

		public Date getMsg_begin_date() {
			return msg_begin_date;
		}

		public void setMsg_begin_date(Date msg_begin_date) {
			this.msg_begin_date = msg_begin_date;
		}

		public Date getMsg_end_date() {
			return msg_end_date;
		}

		public void setMsg_end_date(Date msg_end_date) {
			this.msg_end_date = msg_end_date;
		}

		public String getMsg_status() {
			return msg_status;
		}

		public void setMsg_status(String msg_status) {
			this.msg_status = msg_status;
		}

		public Long getMsg_res_id() {
			return msg_res_id;
		}

		public void setMsg_res_id(Long msg_res_id) {
			this.msg_res_id = msg_res_id;
		}

		public Long getMsg_root_ent_id() {
			return msg_root_ent_id;
		}

		public void setMsg_root_ent_id(Long msg_root_ent_id) {
			this.msg_root_ent_id = msg_root_ent_id;
		}

		public Date getMsg_upd_date() {
			return msg_upd_date;
		}

		public void setMsg_upd_date(Date msg_upd_date) {
			this.msg_upd_date = msg_upd_date;
		}

		public String getMsg_level() {
			return msg_level;
		}

		public void setMsg_level(String msg_level) {
			this.msg_level = msg_level;
		}

		public Long getMsg_tcr_id() {
			return msg_tcr_id;
		}

		public void setMsg_tcr_id(Long msg_tcr_id) {
			this.msg_tcr_id = msg_tcr_id;
		}

		public String getMsg_icon() {
			return msg_icon;
		}

		public void setMsg_icon(String msg_icon) {
			this.msg_icon = msg_icon;
		}

		public Long getMsg_mobile_ind() {
			return msg_mobile_ind;
		}

		public void setMsg_mobile_ind(Long msg_mobile_ind) {
			this.msg_mobile_ind = msg_mobile_ind;
		}

		public boolean isNewest_ind() {
			return newest_ind;
		}

		public void setNewest_ind(boolean newest_ind) {
			this.newest_ind = newest_ind;
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

		public boolean isMsg_receipt() {
			return msg_receipt;
		}

		public void setMsg_receipt(boolean msg_receipt) {
			this.msg_receipt = msg_receipt;
		}

		public boolean isCurUserIsRead() {
			return curUserIsRead;
		}

		public void setCurUserIsRead(boolean curUserIsRead) {
			this.curUserIsRead = curUserIsRead;
		}
		
}