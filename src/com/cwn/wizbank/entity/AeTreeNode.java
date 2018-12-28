package com.cwn.wizbank.entity;

import java.util.Date;


public class AeTreeNode implements java.io.Serializable {
	private static final long serialVersionUID = -93769955642183176L;

    public static final String TND_TYPE_CAT = "CATALOG";
    public static final String TND_TYPE_NORMAL = "NORMAL";
    public static final String TND_TYPE_LINK = "LINK";
    public static final String TND_TYPE_ITEM = "ITEM";
		/**
		 * pk
		 * 
		 **/
		Long tnd_id;
		/**
		 * 
		 **/
		String tnd_title;
		/**
		 * 
		 **/
		String tnd_status;
		/**
		 * 
		 **/
		String tnd_type;
		/**
		 * 
		 **/
		Long tnd_itm_cnt;
		/**
		 * 
		 **/
		Long tnd_on_itm_cnt;
		/**
		 * 
		 **/
		Long tnd_cat_id;
		/**
		 * 
		 **/
		Long tnd_parent_tnd_id;
		/**
		 * 
		 **/
		Long tnd_link_tnd_id;
		/**
		 * 
		 **/
		Long tnd_itm_id;
		/**
		 * 
		 **/
		Date tnd_create_timestamp;
		/**
		 * 
		 **/
		String tnd_create_usr_id;
		/**
		 * 
		 **/
		Date tnd_upd_timestamp;
		/**
		 * 
		 **/
		String tnd_upd_usr_id;
		/**
		 * 
		 **/
		String tnd_ext1;
		/**
		 * 
		 **/
		String tnd_ext2;
		/**
		 * 
		 **/
		String tnd_ext3;
		/**
		 * 
		 **/
		String tnd_ext4;
		/**
		 * 
		 **/
		String tnd_ext5;
		/**
		 * 
		 **/
		String tnd_desc;
		/**
		 * 
		 **/
		Long tnd_order;
		/**
		 * 
		 **/
		Long tnd_owner_ent_id;
		/**
		 * 
		 **/
		String tnd_code;
		/**
		 * 
		 **/
		Date tnd_syn_timestamp;
		
		AeCatalog aeCatalog;
		
		AeTreeNodeRelation aeTreeNodeRelation;
	
		public AeTreeNode(){
		}
	
		public Long getTnd_id(){
			return this.tnd_id;
		}		
		public void setTnd_id(Long tnd_id){
			this.tnd_id = tnd_id;
		}
		public String getTnd_title(){
			return this.tnd_title;
		}		
		public void setTnd_title(String tnd_title){
			this.tnd_title = tnd_title;
		}
		public String getTnd_status(){
			return this.tnd_status;
		}		
		public void setTnd_status(String tnd_status){
			this.tnd_status = tnd_status;
		}
		public String getTnd_type(){
			return this.tnd_type;
		}		
		public void setTnd_type(String tnd_type){
			this.tnd_type = tnd_type;
		}
		public Long getTnd_itm_cnt(){
			return this.tnd_itm_cnt;
		}		
		public void setTnd_itm_cnt(Long tnd_itm_cnt){
			this.tnd_itm_cnt = tnd_itm_cnt;
		}
		public Long getTnd_on_itm_cnt(){
			return this.tnd_on_itm_cnt;
		}		
		public void setTnd_on_itm_cnt(Long tnd_on_itm_cnt){
			this.tnd_on_itm_cnt = tnd_on_itm_cnt;
		}
		public Long getTnd_cat_id(){
			return this.tnd_cat_id;
		}		
		public void setTnd_cat_id(Long tnd_cat_id){
			this.tnd_cat_id = tnd_cat_id;
		}
		public Long getTnd_parent_tnd_id(){
			return this.tnd_parent_tnd_id;
		}		
		public void setTnd_parent_tnd_id(Long tnd_parent_tnd_id){
			this.tnd_parent_tnd_id = tnd_parent_tnd_id;
		}
		public Long getTnd_link_tnd_id(){
			return this.tnd_link_tnd_id;
		}		
		public void setTnd_link_tnd_id(Long tnd_link_tnd_id){
			this.tnd_link_tnd_id = tnd_link_tnd_id;
		}
		public Long getTnd_itm_id(){
			return this.tnd_itm_id;
		}		
		public void setTnd_itm_id(Long tnd_itm_id){
			this.tnd_itm_id = tnd_itm_id;
		}
		public Date getTnd_create_timestamp(){
			return this.tnd_create_timestamp;
		}		
		public void setTnd_create_timestamp(Date tnd_create_timestamp){
			this.tnd_create_timestamp = tnd_create_timestamp;
		}
		public String getTnd_create_usr_id(){
			return this.tnd_create_usr_id;
		}		
		public void setTnd_create_usr_id(String tnd_create_usr_id){
			this.tnd_create_usr_id = tnd_create_usr_id;
		}
		public Date getTnd_upd_timestamp(){
			return this.tnd_upd_timestamp;
		}		
		public void setTnd_upd_timestamp(Date tnd_upd_timestamp){
			this.tnd_upd_timestamp = tnd_upd_timestamp;
		}
		public String getTnd_upd_usr_id(){
			return this.tnd_upd_usr_id;
		}		
		public void setTnd_upd_usr_id(String tnd_upd_usr_id){
			this.tnd_upd_usr_id = tnd_upd_usr_id;
		}
		public String getTnd_ext1(){
			return this.tnd_ext1;
		}		
		public void setTnd_ext1(String tnd_ext1){
			this.tnd_ext1 = tnd_ext1;
		}
		public String getTnd_ext2(){
			return this.tnd_ext2;
		}		
		public void setTnd_ext2(String tnd_ext2){
			this.tnd_ext2 = tnd_ext2;
		}
		public String getTnd_ext3(){
			return this.tnd_ext3;
		}		
		public void setTnd_ext3(String tnd_ext3){
			this.tnd_ext3 = tnd_ext3;
		}
		public String getTnd_ext4(){
			return this.tnd_ext4;
		}		
		public void setTnd_ext4(String tnd_ext4){
			this.tnd_ext4 = tnd_ext4;
		}
		public String getTnd_ext5(){
			return this.tnd_ext5;
		}		
		public void setTnd_ext5(String tnd_ext5){
			this.tnd_ext5 = tnd_ext5;
		}
		public String getTnd_desc(){
			return this.tnd_desc;
		}		
		public void setTnd_desc(String tnd_desc){
			this.tnd_desc = tnd_desc;
		}
		public Long getTnd_order(){
			return this.tnd_order;
		}		
		public void setTnd_order(Long tnd_order){
			this.tnd_order = tnd_order;
		}
		public Long getTnd_owner_ent_id(){
			return this.tnd_owner_ent_id;
		}		
		public void setTnd_owner_ent_id(Long tnd_owner_ent_id){
			this.tnd_owner_ent_id = tnd_owner_ent_id;
		}
		public String getTnd_code(){
			return this.tnd_code;
		}		
		public void setTnd_code(String tnd_code){
			this.tnd_code = tnd_code;
		}
		public Date getTnd_syn_timestamp(){
			return this.tnd_syn_timestamp;
		}		
		public void setTnd_syn_timestamp(Date tnd_syn_timestamp){
			this.tnd_syn_timestamp = tnd_syn_timestamp;
		}

		public AeCatalog getAeCatalog() {
			return aeCatalog;
		}

		public void setAeCatalog(AeCatalog aeCatalog) {
			this.aeCatalog = aeCatalog;
		}

		public AeTreeNodeRelation getAeTreeNodeRelation() {
			return aeTreeNodeRelation;
		}

		public void setAeTreeNodeRelation(AeTreeNodeRelation aeTreeNodeRelation) {
			this.aeTreeNodeRelation = aeTreeNodeRelation;
		}
	
}