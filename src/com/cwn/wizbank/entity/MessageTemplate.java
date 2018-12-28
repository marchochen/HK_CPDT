package com.cwn.wizbank.entity;

import java.util.Date;


public class MessageTemplate implements java.io.Serializable {
	private static final long serialVersionUID = -7787272241218519884L;
		/**
		 * pk
		 * null
		 **/
		long mtp_id;
		/**
		 * null
		 **/
		String mtp_type;
		/**
		 * null
		 **/
		String mtp_subject;
		/**
		 * null
		 **/
		String mtp_content;
		/**
		 * null
		 **/
		String mtp_content_email_link;
		/**
		 * null
		 **/
		String mtp_content_pc_link;
		/**
		 * null
		 **/
		String mtp_content_mobile_link;
		/**
		 * null
		 **/
		String mtp_remark_label;
		/**
		 * null
		 **/
		Boolean mtp_web_message_ind;
		/**
		 * null
		 **/
		Boolean mtp_active_ind;
		/**
		 * null
		 **/
		long mtp_tcr_id;
		/**
		 * null
		 **/
		long mtp_update_ent_id;
		/**
		 * null
		 **/
		Date mtp_update_timestamp;
		/**
		 * null
		 **/
		String mtp_header_img;
		/**
		 * null
		 **/
		String mtp_footer_img;
	
		public MessageTemplate(){
		}
	
		public long getMtp_id(){
			return this.mtp_id;
		}		
		public void setMtp_id(long mtp_id){
			this.mtp_id = mtp_id;
		}
		public String getMtp_type(){
			return this.mtp_type;
		}		
		public void setMtp_type(String mtp_type){
			this.mtp_type = mtp_type;
		}
		public String getMtp_subject(){
			return this.mtp_subject;
		}		
		public void setMtp_subject(String mtp_subject){
			this.mtp_subject = mtp_subject;
		}
		public String getMtp_content(){
			return this.mtp_content;
		}		
		public void setMtp_content(String mtp_content){
			this.mtp_content = mtp_content;
		}
		public String getMtp_content_email_link(){
			return this.mtp_content_email_link;
		}		
		public void setMtp_content_email_link(String mtp_content_email_link){
			this.mtp_content_email_link = mtp_content_email_link;
		}
		public String getMtp_content_pc_link(){
			return this.mtp_content_pc_link;
		}		
		public void setMtp_content_pc_link(String mtp_content_pc_link){
			this.mtp_content_pc_link = mtp_content_pc_link;
		}
		public String getMtp_content_mobile_link(){
			return this.mtp_content_mobile_link;
		}		
		public void setMtp_content_mobile_link(String mtp_content_mobile_link){
			this.mtp_content_mobile_link = mtp_content_mobile_link;
		}
		public String getMtp_remark_label(){
			return this.mtp_remark_label;
		}		
		public void setMtp_remark_label(String mtp_remark_label){
			this.mtp_remark_label = mtp_remark_label;
		}
		public Boolean getMtp_web_message_ind(){
			return this.mtp_web_message_ind;
		}		
		public void setMtp_web_message_ind(Boolean mtp_web_message_ind){
			this.mtp_web_message_ind = mtp_web_message_ind;
		}
		public Boolean getMtp_active_ind(){
			return this.mtp_active_ind;
		}		
		public void setMtp_active_ind(Boolean mtp_active_ind){
			this.mtp_active_ind = mtp_active_ind;
		}
		public long getMtp_tcr_id(){
			return this.mtp_tcr_id;
		}		
		public void setMtp_tcr_id(Integer mtp_tcr_id){
			this.mtp_tcr_id = mtp_tcr_id;
		}
		public long getMtp_update_ent_id(){
			return this.mtp_update_ent_id;
		}		
		public void setMtp_update_ent_id(Integer mtp_update_ent_id){
			this.mtp_update_ent_id = mtp_update_ent_id;
		}
		public Date getMtp_update_timestamp(){
			return this.mtp_update_timestamp;
		}		
		public void setMtp_update_timestamp(Date mtp_update_timestamp){
			this.mtp_update_timestamp = mtp_update_timestamp;
		}
		public String getMtp_header_img(){
			return this.mtp_header_img;
		}		
		public void setMtp_header_img(String mtp_header_img){
			this.mtp_header_img = mtp_header_img;
		}
		public String getMtp_footer_img(){
			return this.mtp_footer_img;
		}		
		public void setMtp_footer_img(String mtp_footer_img){
			this.mtp_footer_img = mtp_footer_img;
		}
	
}