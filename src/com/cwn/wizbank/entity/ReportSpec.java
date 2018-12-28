package com.cwn.wizbank.entity;

import java.util.Date;

import com.cw.wizbank.JsonMod.supervise.bean.StaffReportBean;


public class ReportSpec implements java.io.Serializable {
	private static final long serialVersionUID = -6543496358865833392L;
		/**
		 * pk
		 * null
		 **/
		Integer rsp_id;
		/**
		 * pk
		 * null
		 **/
		Integer rsp_rte_id;
		/**
		 * null
		 **/
		Integer rsp_ent_id;
		/**
		 * null
		 **/
		String rsp_title;
		/**
		 * null
		 **/
		String rsp_xml;
		/**
		 * null
		 **/
		String rsp_create_usr_id;
		/**
		 * null
		 **/
		Date rsp_create_timestamp;
		/**
		 * null
		 **/
		String rsp_upd_usr_id;
		/**
		 * null
		 **/
		Date rsp_upd_timestamp;
		/**
		 * null
		 **/
		String rsp_content;
		
		StaffReportBean staffReportBean;
	
		public ReportSpec(){
		}
	
		public Integer getRsp_id(){
			return this.rsp_id;
		}		
		public void setRsp_id(Integer rsp_id){
			this.rsp_id = rsp_id;
		}
		public Integer getRsp_rte_id(){
			return this.rsp_rte_id;
		}		
		public void setRsp_rte_id(Integer rsp_rte_id){
			this.rsp_rte_id = rsp_rte_id;
		}
		public Integer getRsp_ent_id(){
			return this.rsp_ent_id;
		}		
		public void setRsp_ent_id(Integer rsp_ent_id){
			this.rsp_ent_id = rsp_ent_id;
		}
		public String getRsp_title(){
			return this.rsp_title;
		}		
		public void setRsp_title(String rsp_title){
			this.rsp_title = rsp_title;
		}
		public String getRsp_xml(){
			return this.rsp_xml;
		}		
		public void setRsp_xml(String rsp_xml){
			this.rsp_xml = rsp_xml;
		}
		public String getRsp_create_usr_id(){
			return this.rsp_create_usr_id;
		}		
		public void setRsp_create_usr_id(String rsp_create_usr_id){
			this.rsp_create_usr_id = rsp_create_usr_id;
		}
		public Date getRsp_create_timestamp(){
			return this.rsp_create_timestamp;
		}		
		public void setRsp_create_timestamp(Date rsp_create_timestamp){
			this.rsp_create_timestamp = rsp_create_timestamp;
		}
		public String getRsp_upd_usr_id(){
			return this.rsp_upd_usr_id;
		}		
		public void setRsp_upd_usr_id(String rsp_upd_usr_id){
			this.rsp_upd_usr_id = rsp_upd_usr_id;
		}
		public Date getRsp_upd_timestamp(){
			return this.rsp_upd_timestamp;
		}		
		public void setRsp_upd_timestamp(Date rsp_upd_timestamp){
			this.rsp_upd_timestamp = rsp_upd_timestamp;
		}
		public String getRsp_content(){
			return this.rsp_content;
		}		
		public void setRsp_content(String rsp_content){
			this.rsp_content = rsp_content;
		}
		public StaffReportBean getStaffReportBean() {
			return staffReportBean;
		}
		public void setStaffReportBean(StaffReportBean staffReportBean) {
			this.staffReportBean = staffReportBean;
		}
	
}