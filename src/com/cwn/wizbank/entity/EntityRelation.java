package com.cwn.wizbank.entity;

import java.util.Date;

/**
 * 实体与实体关系表(层级关系,从属关系,根据ern_type区分)
 * @author leon.li
 * 2014-8-19 上午10:38:01
 */
public class EntityRelation implements java.io.Serializable {
	private static final long serialVersionUID = -6821526173133199459L;
		/**
		 * pk
		 * 子ID
		 **/
		Long ern_child_ent_id;
		/**
		 * pk
		 * 父ID
		 **/
		Long ern_ancestor_ent_id;
		/**
		 * 排序(多层级时候所属层级)
		 **/
		Long ern_order;
		/**
		 * 关系类型(如用户与用户组/用户与职位/用户组与用户组/职位与职位 这些关系对应不用类型)
		 **/
		String ern_type;
		/**
		 * 标识是否属于最近层级
		 **/
		Long ern_parent_ind;
		/**
		 * 同步时间
		 **/
		Date ern_syn_timestamp;
		/**
		 * 
		 **/
		Long ern_remain_on_syn;
		/**
		 * 
		 **/
		Date ern_create_timestamp;
		/**
		 * 
		 **/
		String ern_create_usr_id;
		
		/**
		 * null
		 **/
		Date ern_end_timestamp;
		
		Date ern_start_timestamp;
	
		public EntityRelation(){
		}
	
		public Long getErn_child_ent_id(){
			return this.ern_child_ent_id;
		}		
		public void setErn_child_ent_id(Long ern_child_ent_id){
			this.ern_child_ent_id = ern_child_ent_id;
		}
		public Long getErn_ancestor_ent_id(){
			return this.ern_ancestor_ent_id;
		}		
		public void setErn_ancestor_ent_id(Long ern_ancestor_ent_id){
			this.ern_ancestor_ent_id = ern_ancestor_ent_id;
		}
		public Long getErn_order(){
			return this.ern_order;
		}		
		public void setErn_order(Long ern_order){
			this.ern_order = ern_order;
		}
		public String getErn_type(){
			return this.ern_type;
		}		
		public void setErn_type(String ern_type){
			this.ern_type = ern_type;
		}
		public Long getErn_parent_ind(){
			return this.ern_parent_ind;
		}		
		public void setErn_parent_ind(Long ern_parent_ind){
			this.ern_parent_ind = ern_parent_ind;
		}
		public Date getErn_syn_timestamp(){
			return this.ern_syn_timestamp;
		}		
		public void setErn_syn_timestamp(Date ern_syn_timestamp){
			this.ern_syn_timestamp = ern_syn_timestamp;
		}
		public Long getErn_remain_on_syn(){
			return this.ern_remain_on_syn;
		}		
		public void setErn_remain_on_syn(Long ern_remain_on_syn){
			this.ern_remain_on_syn = ern_remain_on_syn;
		}
		public Date getErn_create_timestamp(){
			return this.ern_create_timestamp;
		}		
		public void setErn_create_timestamp(Date ern_create_timestamp){
			this.ern_create_timestamp = ern_create_timestamp;
		}
		public String getErn_create_usr_id(){
			return this.ern_create_usr_id;
		}		
		public void setErn_create_usr_id(String ern_create_usr_id){
			this.ern_create_usr_id = ern_create_usr_id;
		}

		public Date getErn_end_timestamp() {
			return ern_end_timestamp;
		}

		public void setErn_end_timestamp(Date ern_end_timestamp) {
			this.ern_end_timestamp = ern_end_timestamp;
		}

		public Date getErn_start_timestamp() {
			return ern_start_timestamp;
		}

		public void setErn_start_timestamp(Date ern_start_timestamp) {
			this.ern_start_timestamp = ern_start_timestamp;
		}
	
}