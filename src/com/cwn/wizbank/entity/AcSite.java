package com.cwn.wizbank.entity;

import java.util.Date;


public class AcSite implements java.io.Serializable {
	private static final long serialVersionUID = -2485425632639867964L;
		/**
		 * pk
		 * null
		 **/
		long ste_ent_id;
		/**
		 * null
		 **/
		String ste_name;
		/**
		 * null
		 **/
		Date ste_join_datetime;
		/**
		 * null
		 **/
		String ste_status;
		/**
		 * null
		 **/
		String ste_domain;
		/**
		 * null
		 **/
		String ste_login_url;
		/**
		 * null
		 **/
		long ste_ird_id;
		/**
		 * null
		 **/
		Boolean ste_trusted;
		/**
		 * null
		 **/
		String ste_max_users;
		/**
		 * null
		 **/
		String ste_eff_start_date;
		/**
		 * null
		 **/
		String ste_eff_end_date;
		/**
		 * null
		 **/
		long ste_max_login_trial;
		/**
		 * null
		 **/
		Date ste_cov_syn_datetime;
		/**
		 * null
		 **/
		long ste_default_sys_ent_id;
		/**
		 * null
		 **/
		String ste_targeted_entity_lst;
		/**
		 * null
		 **/
		long ste_rsv_min_gap;
		/**
		 * null
		 **/
		long ste_rsv_min_len;
		/**
		 * null
		 **/
		String ste_rsv_link;
		/**
		 * null
		 **/
		String ste_lan_xml;
		/**
		 * null
		 **/
		long ste_guest_ent_id;
		/**
		 * null
		 **/
		String ste_ctl_4;
		/**
		 * null
		 **/
		long ste_target_by_peer_ind;
		/**
		 * null
		 **/
		String ste_ldap_host;
		/**
		 * null
		 **/
		String ste_ldap_dn;
		/**
		 * null
		 **/
		String ste_appr_staff_role;
		/**
		 * null
		 **/
		long ste_usr_pwd_valid_period;
		/**
		 * null
		 **/
		String ste_qr_mod_types;
		/**
		 * null
		 **/
		String ste_id;
		/**
		 * null
		 **/
		String ste_type;
		/**
		 * null
		 **/
		String ste_developer_id;
	
		public AcSite(){
		}
	
		public long getSte_ent_id(){
			return this.ste_ent_id;
		}		
		public void setSte_ent_id(long ste_ent_id){
			this.ste_ent_id = ste_ent_id;
		}
		public String getSte_name(){
			return this.ste_name;
		}		
		public void setSte_name(String ste_name){
			this.ste_name = ste_name;
		}
		public Date getSte_join_datetime(){
			return this.ste_join_datetime;
		}		
		public void setSte_join_datetime(Date ste_join_datetime){
			this.ste_join_datetime = ste_join_datetime;
		}
		public String getSte_status(){
			return this.ste_status;
		}		
		public void setSte_status(String ste_status){
			this.ste_status = ste_status;
		}
		public String getSte_domain(){
			return this.ste_domain;
		}		
		public void setSte_domain(String ste_domain){
			this.ste_domain = ste_domain;
		}
		public String getSte_login_url(){
			return this.ste_login_url;
		}		
		public void setSte_login_url(String ste_login_url){
			this.ste_login_url = ste_login_url;
		}
		public long getSte_ird_id(){
			return this.ste_ird_id;
		}		
		public void setSte_ird_id(long ste_ird_id){
			this.ste_ird_id = ste_ird_id;
		}
		public Boolean getSte_trusted(){
			return this.ste_trusted;
		}		
		public void setSte_trusted(Boolean ste_trusted){
			this.ste_trusted = ste_trusted;
		}
		public String getSte_max_users(){
			return this.ste_max_users;
		}		
		public void setSte_max_users(String ste_max_users){
			this.ste_max_users = ste_max_users;
		}
		public String getSte_eff_start_date(){
			return this.ste_eff_start_date;
		}		
		public void setSte_eff_start_date(String ste_eff_start_date){
			this.ste_eff_start_date = ste_eff_start_date;
		}
		public String getSte_eff_end_date(){
			return this.ste_eff_end_date;
		}		
		public void setSte_eff_end_date(String ste_eff_end_date){
			this.ste_eff_end_date = ste_eff_end_date;
		}
		public long getSte_max_login_trial(){
			return this.ste_max_login_trial;
		}		
		public void setSte_max_login_trial(long ste_max_login_trial){
			this.ste_max_login_trial = ste_max_login_trial;
		}
		public Date getSte_cov_syn_datetime(){
			return this.ste_cov_syn_datetime;
		}		
		public void setSte_cov_syn_datetime(Date ste_cov_syn_datetime){
			this.ste_cov_syn_datetime = ste_cov_syn_datetime;
		}
		public long getSte_default_sys_ent_id(){
			return this.ste_default_sys_ent_id;
		}		
		public void setSte_default_sys_ent_id(long ste_default_sys_ent_id){
			this.ste_default_sys_ent_id = ste_default_sys_ent_id;
		}
		public String getSte_targeted_entity_lst(){
			return this.ste_targeted_entity_lst;
		}		
		public void setSte_targeted_entity_lst(String ste_targeted_entity_lst){
			this.ste_targeted_entity_lst = ste_targeted_entity_lst;
		}
		public long getSte_rsv_min_gap(){
			return this.ste_rsv_min_gap;
		}		
		public void setSte_rsv_min_gap(long ste_rsv_min_gap){
			this.ste_rsv_min_gap = ste_rsv_min_gap;
		}
		public long getSte_rsv_min_len(){
			return this.ste_rsv_min_len;
		}		
		public void setSte_rsv_min_len(long ste_rsv_min_len){
			this.ste_rsv_min_len = ste_rsv_min_len;
		}
		public String getSte_rsv_link(){
			return this.ste_rsv_link;
		}		
		public void setSte_rsv_link(String ste_rsv_link){
			this.ste_rsv_link = ste_rsv_link;
		}
		public String getSte_lan_xml(){
			return this.ste_lan_xml;
		}		
		public void setSte_lan_xml(String ste_lan_xml){
			this.ste_lan_xml = ste_lan_xml;
		}
		public long getSte_guest_ent_id(){
			return this.ste_guest_ent_id;
		}		
		public void setSte_guest_ent_id(long ste_guest_ent_id){
			this.ste_guest_ent_id = ste_guest_ent_id;
		}
		public String getSte_ctl_4(){
			return this.ste_ctl_4;
		}		
		public void setSte_ctl_4(String ste_ctl_4){
			this.ste_ctl_4 = ste_ctl_4;
		}
		public long getSte_target_by_peer_ind(){
			return this.ste_target_by_peer_ind;
		}		
		public void setSte_target_by_peer_ind(long ste_target_by_peer_ind){
			this.ste_target_by_peer_ind = ste_target_by_peer_ind;
		}
		public String getSte_ldap_host(){
			return this.ste_ldap_host;
		}		
		public void setSte_ldap_host(String ste_ldap_host){
			this.ste_ldap_host = ste_ldap_host;
		}
		public String getSte_ldap_dn(){
			return this.ste_ldap_dn;
		}		
		public void setSte_ldap_dn(String ste_ldap_dn){
			this.ste_ldap_dn = ste_ldap_dn;
		}
		public String getSte_appr_staff_role(){
			return this.ste_appr_staff_role;
		}		
		public void setSte_appr_staff_role(String ste_appr_staff_role){
			this.ste_appr_staff_role = ste_appr_staff_role;
		}
		public long getSte_usr_pwd_valid_period(){
			return this.ste_usr_pwd_valid_period;
		}		
		public void setSte_usr_pwd_valid_period(long ste_usr_pwd_valid_period){
			this.ste_usr_pwd_valid_period = ste_usr_pwd_valid_period;
		}
		public String getSte_qr_mod_types(){
			return this.ste_qr_mod_types;
		}		
		public void setSte_qr_mod_types(String ste_qr_mod_types){
			this.ste_qr_mod_types = ste_qr_mod_types;
		}
		public String getSte_id(){
			return this.ste_id;
		}		
		public void setSte_id(String ste_id){
			this.ste_id = ste_id;
		}
		public String getSte_type(){
			return this.ste_type;
		}		
		public void setSte_type(String ste_type){
			this.ste_type = ste_type;
		}
		public String getSte_developer_id(){
			return this.ste_developer_id;
		}		
		public void setSte_developer_id(String ste_developer_id){
			this.ste_developer_id = ste_developer_id;
		}
	
}