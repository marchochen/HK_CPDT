package com.cwn.wizbank.entity;


/**
 * 评价
 * @author leon.li
 * 2014-8-7 下午5:49:59
 */
public class SnsValuation implements java.io.Serializable {
	private static final long serialVersionUID = -3591939884738255152L;
		/**
		 * pk
		 * 
		 **/
		Long s_vlt_id;
		/**
		 * 
		 **/
		String s_vlt_type;
		/**
		 * 
		 **/
		Long s_vlt_score;
		/**
		 * 
		 **/
		String s_vlt_module;
		/**
		 * 
		 **/
		Long s_vlt_target_id;
		
		int s_vlt_is_comment;
	
		public SnsValuation(){
		}

		public Long getS_vlt_id() {
			return s_vlt_id;
		}

		public void setS_vlt_id(Long s_vlt_id) {
			this.s_vlt_id = s_vlt_id;
		}

		public String getS_vlt_type() {
			return s_vlt_type;
		}

		public void setS_vlt_type(String s_vlt_type) {
			this.s_vlt_type = s_vlt_type;
		}

		public Long getS_vlt_score() {
			return s_vlt_score;
		}

		public void setS_vlt_score(Long s_vlt_score) {
			this.s_vlt_score = s_vlt_score;
		}

		public String getS_vlt_module() {
			return s_vlt_module;
		}

		public void setS_vlt_module(String s_vlt_module) {
			this.s_vlt_module = s_vlt_module;
		}

		public Long getS_vlt_target_id() {
			return s_vlt_target_id;
		}

		public void setS_vlt_target_id(Long s_vlt_target_id) {
			this.s_vlt_target_id = s_vlt_target_id;
		}

		public int getS_vlt_is_comment() {
			return s_vlt_is_comment;
		}

		public void setS_vlt_is_comment(int s_vlt_is_comment) {
			this.s_vlt_is_comment = s_vlt_is_comment;
		}
	

}