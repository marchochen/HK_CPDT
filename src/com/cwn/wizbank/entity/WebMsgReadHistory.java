package com.cwn.wizbank.entity;

import java.util.Date;


public class WebMsgReadHistory implements java.io.Serializable {
	private static final long serialVersionUID = -5111963431381433295L;
		/**
		 * 邮件ID
		 **/
		Long wmrh_wmsg_id;
		/**
		 * 状态
		 **/
		String wmrh_status;
		/**
		 * 第一次阅读时间
		 **/
		Date wmrh_read_datetime;
	
		public WebMsgReadHistory(){
		}
	
		public Long getWmrh_wmsg_id(){
			return this.wmrh_wmsg_id;
		}		
		public void setWmrh_wmsg_id(Long wmrh_wmsg_id){
			this.wmrh_wmsg_id = wmrh_wmsg_id;
		}
		public String getWmrh_status(){
			return this.wmrh_status;
		}		
		public void setWmrh_status(String wmrh_status){
			this.wmrh_status = wmrh_status;
		}
		public Date getWmrh_read_datetime(){
			return this.wmrh_read_datetime;
		}		
		public void setWmrh_read_datetime(Date wmrh_read_datetime){
			this.wmrh_read_datetime = wmrh_read_datetime;
		}
	
}