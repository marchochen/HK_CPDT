package com.cwn.wizbank.entity;

import java.util.Date;


public class UserCredits implements java.io.Serializable {
	private static final long serialVersionUID = -998986767972166767L;
		/**
		 * pk
		 * 用户id
		 **/
		Long uct_ent_id;
		/**
		 * 积分总分
		 **/
		Double uct_total;
		/**
		 * 积分更新时间
		 **/
		Date uct_update_timestamp;
		/**
		 * 
		 **/
		Integer uct_zd_total;
		/**
		 * 排名
		 **/
		Long rownum;
		
		RegUser user;
		
		String usg_name;
		
		Long tcr_id;
	
		public UserCredits(){
		}
	
		public Long getUct_ent_id(){
			return this.uct_ent_id;
		}		
		public void setUct_ent_id(Long uct_ent_id){
			this.uct_ent_id = uct_ent_id;
		}
		public Double getUct_total(){
			return this.uct_total;
		}		
		public void setUct_total(Double uct_total){
			this.uct_total = uct_total;
		}
		public Date getUct_update_timestamp(){
			return this.uct_update_timestamp;
		}		
		public void setUct_update_timestamp(Date uct_update_timestamp){
			this.uct_update_timestamp = uct_update_timestamp;
		}
		public Integer getUct_zd_total(){
			return this.uct_zd_total;
		}		
		public void setUct_zd_total(Integer uct_zd_total){
			this.uct_zd_total = uct_zd_total;
		}
		public RegUser getUser() {
			return user;
		}
		public void setUser(RegUser user) {
			this.user = user;
		}
		public String getUsg_name() {
			return usg_name;
		}
		public void setUsg_name(String usg_name) {
			this.usg_name = usg_name;
		}
		public Long getRownum() {
			return rownum;
		}
		public void setRownum(Long rownum) {
			this.rownum = rownum;
		}
		public Long getTcr_id() {
			return tcr_id;
		}
		public void setTcr_id(Long tcr_id) {
			this.tcr_id = tcr_id;
		}
		
	
}