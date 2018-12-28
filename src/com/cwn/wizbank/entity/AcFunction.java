package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;


public class AcFunction implements java.io.Serializable {
	private static final long serialVersionUID = -7737216674915494717L;
		/**
		 * pk
		 * null
		 **/
		Integer ftn_id;
		/**
		 * null
		 **/
		String ftn_ext_id;
		/**
		 * null
		 **/
		String ftn_level;
		/**
		 * null
		 **/
		String ftn_type;
		/**
		 * null
		 **/
		Date ftn_creation_timestamp;
		/**
		 * null
		 **/
		String ftn_tc_related;
		/**
		 * null
		 **/
		String ftn_status;
		
		String ftn_assign;
		
		Integer ftn_parent_id;
		
		Integer ftn_order;
		
		Integer uff_fun_id;
		
		List<AcFunction> subFunctions;
		List<AcFunction> Functions;
	
		public String getFtn_assign() {
			return ftn_assign;
		}

		public void setFtn_assign(String ftn_assign) {
			this.ftn_assign = ftn_assign;
		}

		public AcFunction(){
		}
	
		public Integer getFtn_id(){
			return this.ftn_id;
		}		
		public void setFtn_id(Integer ftn_id){
			this.ftn_id = ftn_id;
		}
		public String getFtn_ext_id(){
			return this.ftn_ext_id;
		}		
		public void setFtn_ext_id(String ftn_ext_id){
			this.ftn_ext_id = ftn_ext_id;
		}
		public String getFtn_level(){
			return this.ftn_level;
		}		
		public void setFtn_level(String ftn_level){
			this.ftn_level = ftn_level;
		}
		public String getFtn_type(){
			return this.ftn_type;
		}		
		public void setFtn_type(String ftn_type){
			this.ftn_type = ftn_type;
		}
		public Date getFtn_creation_timestamp(){
			return this.ftn_creation_timestamp;
		}		
		public void setFtn_creation_timestamp(Date ftn_creation_timestamp){
			this.ftn_creation_timestamp = ftn_creation_timestamp;
		}
		public String getFtn_tc_related(){
			return this.ftn_tc_related;
		}		
		public void setFtn_tc_related(String ftn_tc_related){
			this.ftn_tc_related = ftn_tc_related;
		}
		public String getFtn_status(){
			return this.ftn_status;
		}		
		public void setFtn_status(String ftn_status){
			this.ftn_status = ftn_status;
		}

		public Integer getFtn_parent_id() {
			return ftn_parent_id;
		}

		public void setFtn_parent_id(Integer ftn_parent_id) {
			this.ftn_parent_id = ftn_parent_id;
		}

		public Integer getFtn_order() {
			return ftn_order;
		}

		public void setFtn_order(Integer ftn_order) {
			this.ftn_order = ftn_order;
		}

		public List<AcFunction> getSubFunctions() {
			return subFunctions;
		}

		public void setSubFunctions(List<AcFunction> subFunctions) {
			this.subFunctions = subFunctions;
		}

		public Integer getUff_fun_id() {
			return uff_fun_id;
		}

		public void setUff_fun_id(Integer uff_fun_id) {
			this.uff_fun_id = uff_fun_id;
		}

		public List<AcFunction> getFunctions() {
			return Functions;
		}

		public void setFunctions(List<AcFunction> functions) {
			Functions = functions;
		}
		
		
	
}