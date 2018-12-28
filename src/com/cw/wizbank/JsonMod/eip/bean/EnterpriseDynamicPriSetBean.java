package com.cw.wizbank.JsonMod.eip.bean;

import java.sql.Timestamp;

public class EnterpriseDynamicPriSetBean {
		private long eip_dps_tcr_id ;          
		private long eip_dps_share_usr_inf_ind ;  //是否共享
		private Timestamp eip_dps_upd_date_time;
		private String eip_dps_upd_usr_id;
		public long getEip_dps_tcr_id() {
			return eip_dps_tcr_id;
		}
		public void setEip_dps_tcr_id(long eip_dps_tcr_id) {
			this.eip_dps_tcr_id = eip_dps_tcr_id;
		}
		public long getEip_dps_share_usr_inf_ind() {
			return eip_dps_share_usr_inf_ind;
		}
		public void setEip_dps_share_usr_inf_ind(long eip_dps_share_usr_inf_ind) {
			this.eip_dps_share_usr_inf_ind = eip_dps_share_usr_inf_ind;
		}
		public Timestamp getEip_dps_upd_date_time() {
			return eip_dps_upd_date_time;
		}
		public void setEip_dps_upd_date_time(Timestamp eip_dps_upd_date_time) {
			this.eip_dps_upd_date_time = eip_dps_upd_date_time;
		}
		public String getEip_dps_upd_usr_id() {
			return eip_dps_upd_usr_id;
		}
		public void setEip_dps_upd_usr_id(String eip_dps_upd_usr_id) {
			this.eip_dps_upd_usr_id = eip_dps_upd_usr_id;
		}
		
		
		
}
