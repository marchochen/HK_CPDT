package com.cwn.wizbank.entity;


public class ProfessionItem implements java.io.Serializable {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		Long psi_id;
		
		Long psi_pfs_id;
		
		String psi_ugr_id;
		
		String psi_itm;
		
		Long itm_id;
		
		String itm_title;
		
		String itm_type;
		
		String itm_desc;
		
		String itm_icon;
		
		Long att_ats_id;
		
		Long app_tkh_id;
		
		Long cos_res_id;
		
		Long itr_group_ind;

		Long itr_grade_ind;
		
		Long itr_position_ind;
		
		public Long getItr_grade_ind() {
			return itr_grade_ind;
		}

		public void setItr_grade_ind(Long itr_grade_ind) {
			this.itr_grade_ind = itr_grade_ind;
		}

		public Long getItr_position_ind() {
			return itr_position_ind;
		}

		public void setItr_position_ind(Long itr_position_ind) {
			this.itr_position_ind = itr_position_ind;
		}

		public Long getItr_group_ind() {
			return itr_group_ind;
		}

		public void setItr_group_ind(Long itr_group_ind) {
			this.itr_group_ind = itr_group_ind;
		}

		public Long getPsi_id() {
			return psi_id;
		}

		public void setPsi_id(Long psi_id) {
			this.psi_id = psi_id;
		}

		public Long getPsi_pfs_id() {
			return psi_pfs_id;
		}

		public void setPsi_pfs_id(Long psi_pfs_id) {
			this.psi_pfs_id = psi_pfs_id;
		}

		public String getPsi_ugr_id() {
			return psi_ugr_id;
		}

		public void setPsi_ugr_id(String psi_ugr_id) {
			this.psi_ugr_id = psi_ugr_id;
		}

		public String getPsi_itm() {
			return psi_itm;
		}

		public void setPsi_itm(String psi_itm) {
			this.psi_itm = psi_itm;
		}

		public Long getItm_id() {
			return itm_id;
		}

		public void setItm_id(Long itm_id) {
			this.itm_id = itm_id;
		}

		public String getItm_title() {
			return itm_title;
		}

		public void setItm_title(String itm_title) {
			this.itm_title = itm_title;
		}

		public String getItm_type() {
			return itm_type;
		}

		public void setItm_type(String itm_type) {
			this.itm_type = itm_type;
		}

		public String getItm_desc() {
			return itm_desc;
		}

		public void setItm_desc(String itm_desc) {
			this.itm_desc = itm_desc;
		}

		public String getItm_icon() {
			return itm_icon;
		}

		public void setItm_icon(String itm_icon) {
			this.itm_icon = itm_icon;
		}

		public Long getAtt_ats_id() {
			return att_ats_id;
		}

		public void setAtt_ats_id(Long att_ats_id) {
			this.att_ats_id = att_ats_id;
		}

		public Long getApp_tkh_id() {
			return app_tkh_id;
		}

		public void setApp_tkh_id(Long app_tkh_id) {
			this.app_tkh_id = app_tkh_id;
		}

		public Long getCos_res_id() {
			return cos_res_id;
		}

		public void setCos_res_id(Long cos_res_id) {
			this.cos_res_id = cos_res_id;
		}
		
}