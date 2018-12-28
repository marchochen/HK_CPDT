package com.cwn.wizbank.entity;



public class ResourceContent implements java.io.Serializable {
	private static final long serialVersionUID = -4857373221375427929L;
		/**
		 * pk
		 * null
		 **/
		Long rcn_res_id;
		/**
		 * pk
		 * null
		 **/
		Long rcn_sub_nbr;
		/**
		 * null
		 **/
		String rcn_desc;
		/**
		 * null
		 **/
		Long rcn_order;
		/**
		 * null
		 **/
		Long rcn_res_id_content;
		/**
		 * null
		 **/
		Long rcn_obj_id_content;
		/**
		 * null
		 **/
		Long rcn_score_multiplier;
		/**
		 * null
		 **/
		Long rcn_rcn_res_id_parent;
		/**
		 * null
		 **/
		Long rcn_rcn_sub_nbr_parent;
		/**
		 * pk
		 * null
		 **/
		Long rcn_tkh_id;
	
		public ResourceContent(){
		}
	
		public Long getRcn_res_id(){
			return this.rcn_res_id;
		}		
		public void setRcn_res_id(Long rcn_res_id){
			this.rcn_res_id = rcn_res_id;
		}
		public Long getRcn_sub_nbr(){
			return this.rcn_sub_nbr;
		}		
		public void setRcn_sub_nbr(Long rcn_sub_nbr){
			this.rcn_sub_nbr = rcn_sub_nbr;
		}
		public String getRcn_desc(){
			return this.rcn_desc;
		}		
		public void setRcn_desc(String rcn_desc){
			this.rcn_desc = rcn_desc;
		}
		public Long getRcn_order(){
			return this.rcn_order;
		}		
		public void setRcn_order(Long rcn_order){
			this.rcn_order = rcn_order;
		}
		public Long getRcn_res_id_content(){
			return this.rcn_res_id_content;
		}		
		public void setRcn_res_id_content(Long rcn_res_id_content){
			this.rcn_res_id_content = rcn_res_id_content;
		}
		public Long getRcn_obj_id_content(){
			return this.rcn_obj_id_content;
		}		
		public void setRcn_obj_id_content(Long rcn_obj_id_content){
			this.rcn_obj_id_content = rcn_obj_id_content;
		}
		public Long getRcn_score_multiplier(){
			return this.rcn_score_multiplier;
		}		
		public void setRcn_score_multiplier(Long rcn_score_multiplier){
			this.rcn_score_multiplier = rcn_score_multiplier;
		}
		public Long getRcn_rcn_res_id_parent(){
			return this.rcn_rcn_res_id_parent;
		}		
		public void setRcn_rcn_res_id_parent(Long rcn_rcn_res_id_parent){
			this.rcn_rcn_res_id_parent = rcn_rcn_res_id_parent;
		}
		public Long getRcn_rcn_sub_nbr_parent(){
			return this.rcn_rcn_sub_nbr_parent;
		}		
		public void setRcn_rcn_sub_nbr_parent(Long rcn_rcn_sub_nbr_parent){
			this.rcn_rcn_sub_nbr_parent = rcn_rcn_sub_nbr_parent;
		}
		public Long getRcn_tkh_id(){
			return this.rcn_tkh_id;
		}		
		public void setRcn_tkh_id(Long rcn_tkh_id){
			this.rcn_tkh_id = rcn_tkh_id;
		}
	
}