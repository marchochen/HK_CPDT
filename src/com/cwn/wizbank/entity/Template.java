package com.cwn.wizbank.entity;

import java.util.Date;


public class Template implements java.io.Serializable {
	private static final long serialVersionUID = -1365745452589979527L;
		/**
		 * pk
		 * null
		 **/
		String tpl_name;
		/**
		 * pk
		 * null
		 **/
		String tpl_lan;
		/**
		 * null
		 **/
		String tpl_desc;
		/**
		 * null
		 **/
		String tpl_note;
		/**
		 * null
		 **/
		String tpl_type;
		/**
		 * null
		 **/
		String tpl_thumbnail_url;
		/**
		 * null
		 **/
		String tpl_stylesheet;
	
		public Template(){
		}
	
		public String getTpl_name(){
			return this.tpl_name;
		}		
		public void setTpl_name(String tpl_name){
			this.tpl_name = tpl_name;
		}
		public String getTpl_lan(){
			return this.tpl_lan;
		}		
		public void setTpl_lan(String tpl_lan){
			this.tpl_lan = tpl_lan;
		}
		public String getTpl_desc(){
			return this.tpl_desc;
		}		
		public void setTpl_desc(String tpl_desc){
			this.tpl_desc = tpl_desc;
		}
		public String getTpl_note(){
			return this.tpl_note;
		}		
		public void setTpl_note(String tpl_note){
			this.tpl_note = tpl_note;
		}
		public String getTpl_type(){
			return this.tpl_type;
		}		
		public void setTpl_type(String tpl_type){
			this.tpl_type = tpl_type;
		}
		public String getTpl_thumbnail_url(){
			return this.tpl_thumbnail_url;
		}		
		public void setTpl_thumbnail_url(String tpl_thumbnail_url){
			this.tpl_thumbnail_url = tpl_thumbnail_url;
		}
		public String getTpl_stylesheet(){
			return this.tpl_stylesheet;
		}		
		public void setTpl_stylesheet(String tpl_stylesheet){
			this.tpl_stylesheet = tpl_stylesheet;
		}
	
}