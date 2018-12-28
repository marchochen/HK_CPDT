package com.cwn.wizbank.entity;

import java.util.Date;


public class Resources implements java.io.Serializable {
	private static final long serialVersionUID = -7866229955468756497L;
		/**
		 * pk
		 * null
		 **/
		Integer res_id;
		/**
		 * null
		 **/
		String res_lan;
		/**
		 * null
		 **/
		String res_title;
		/**
		 * null
		 **/
		String res_type;
		/**
		 * null
		 **/
		String res_subtype;
		/**
		 * null
		 **/
		String res_format;
		/**
		 * null
		 **/
		Integer res_difficulty;
		/**
		 * null
		 **/
		Double res_duration;
		/**
		 * null
		 **/
		String res_privilege;
		/**
		 * null
		 **/
		String res_usr_id_owner;
		/**
		 * null
		 **/
		String res_tpl_name;
		/**
		 * null
		 **/
		Integer res_res_id_root;
		/**
		 * null
		 **/
		Integer res_mod_res_id_test;
		/**
		 * null
		 **/
		String res_status;
		/**
		 * null
		 **/
		Date res_create_date;
		/**
		 * null
		 **/
		String res_upd_user;
		/**
		 * null
		 **/
		Date res_upd_date;
		/**
		 * null
		 **/
		String res_src_type;
		/**
		 * null
		 **/
		String res_src_link;
		/**
		 * null
		 **/
		String res_src_online_link;
		/**
		 * null
		 **/
		String res_code;
		/**
		 * null
		 **/
		String res_instructor_name;
		/**
		 * null
		 **/
		String res_instructor_organization;
		/**
		 * null
		 **/
		String res_annotation;
		/**
		 * null
		 **/
		String res_long_desc;
		/**
		 * null
		 **/
		String res_desc;
		/**
		 * null
		 **/
		String res_sco_version;
		/**
		 * null
		 **/
		Integer res_vod_duration;
		/**
		 * null
		 **/
		String res_img_link;
		/**
		 * null
		 **/
		String res_vod_main;
		/**
		 * null
		 **/
		String res_scor_identifier;
		/**
		 * null
		 **/
		Integer res_first_res_id;
		
		Long submit_num;
		
		Module mod;
		
		int pgr_attempt_nbr;
		
		String pgr_status;
		
		String pgr_completion_status;
		
		String trainingStatus;

		boolean  isRestore;
		
		boolean isTrainingCourse;
		
		
		
		/**
		 * null
		 **/
		ModuleEvaluation mov;
		
		TcTrainingCenter tcr;
		
		RegUser user;
		
		AeItem itm;
	
		public Integer getRes_id(){
			return this.res_id;
		}		
		public void setRes_id(Integer res_id){
			this.res_id = res_id;
		}
		public String getRes_lan(){
			return this.res_lan;
		}		
		public void setRes_lan(String res_lan){
			this.res_lan = res_lan;
		}
		public String getRes_title(){
			return this.res_title;
		}		
		public void setRes_title(String res_title){
			this.res_title = res_title;
		}
		public String getRes_type(){
			return this.res_type;
		}		
		public void setRes_type(String res_type){
			this.res_type = res_type;
		}
		public String getRes_subtype(){
			return this.res_subtype;
		}		
		public void setRes_subtype(String res_subtype){
			this.res_subtype = res_subtype;
		}
		public String getRes_format(){
			return this.res_format;
		}		
		public void setRes_format(String res_format){
			this.res_format = res_format;
		}
		public Integer getRes_difficulty(){
			return this.res_difficulty;
		}		
		public void setRes_difficulty(Integer res_difficulty){
			this.res_difficulty = res_difficulty;
		}
		public Double getRes_duration(){
			return this.res_duration;
		}		
		public void setRes_duration(Double res_duration){
			this.res_duration = res_duration;
		}
		public String getRes_privilege(){
			return this.res_privilege;
		}		
		public void setRes_privilege(String res_privilege){
			this.res_privilege = res_privilege;
		}
		public String getRes_usr_id_owner(){
			return this.res_usr_id_owner;
		}		
		public void setRes_usr_id_owner(String res_usr_id_owner){
			this.res_usr_id_owner = res_usr_id_owner;
		}
		public String getRes_tpl_name(){
			return this.res_tpl_name;
		}		
		public void setRes_tpl_name(String res_tpl_name){
			this.res_tpl_name = res_tpl_name;
		}
		public Integer getRes_res_id_root(){
			return this.res_res_id_root;
		}		
		public void setRes_res_id_root(Integer res_res_id_root){
			this.res_res_id_root = res_res_id_root;
		}
		public Integer getRes_mod_res_id_test(){
			return this.res_mod_res_id_test;
		}		
		public void setRes_mod_res_id_test(Integer res_mod_res_id_test){
			this.res_mod_res_id_test = res_mod_res_id_test;
		}
		public String getRes_status(){
			return this.res_status;
		}		
		public void setRes_status(String res_status){
			this.res_status = res_status;
		}
		public Date getRes_create_date(){
			return this.res_create_date;
		}		
		public void setRes_create_date(Date res_create_date){
			this.res_create_date = res_create_date;
		}
		public String getRes_upd_user(){
			return this.res_upd_user;
		}		
		public void setRes_upd_user(String res_upd_user){
			this.res_upd_user = res_upd_user;
		}
		public Date getRes_upd_date(){
			return this.res_upd_date;
		}		
		public void setRes_upd_date(Date res_upd_date){
			this.res_upd_date = res_upd_date;
		}
		public String getRes_src_type(){
			return this.res_src_type;
		}		
		public void setRes_src_type(String res_src_type){
			this.res_src_type = res_src_type;
		}
		public String getRes_src_link(){
			return this.res_src_link;
		}		
		public void setRes_src_link(String res_src_link){
			this.res_src_link = res_src_link;
		}
		public String getRes_src_online_link(){
			return this.res_src_online_link;
		}		
		public void setRes_src_online_link(String res_src_online_link){
			this.res_src_online_link = res_src_online_link;
		}
		public String getRes_code(){
			return this.res_code;
		}		
		public void setRes_code(String res_code){
			this.res_code = res_code;
		}
		public String getRes_instructor_name(){
			return this.res_instructor_name;
		}		
		public void setRes_instructor_name(String res_instructor_name){
			this.res_instructor_name = res_instructor_name;
		}
		public String getRes_instructor_organization(){
			return this.res_instructor_organization;
		}		
		public void setRes_instructor_organization(String res_instructor_organization){
			this.res_instructor_organization = res_instructor_organization;
		}
		public String getRes_annotation(){
			return this.res_annotation;
		}		
		public void setRes_annotation(String res_annotation){
			this.res_annotation = res_annotation;
		}
		public String getRes_long_desc(){
			return this.res_long_desc;
		}		
		public void setRes_long_desc(String res_long_desc){
			this.res_long_desc = res_long_desc;
		}
		public String getRes_desc(){
			return this.res_desc;
		}		
		public void setRes_desc(String res_desc){
			this.res_desc = res_desc;
		}
		public String getRes_sco_version(){
			return this.res_sco_version;
		}		
		public void setRes_sco_version(String res_sco_version){
			this.res_sco_version = res_sco_version;
		}
		public Integer getRes_vod_duration(){
			return this.res_vod_duration;
		}		
		public void setRes_vod_duration(Integer res_vod_duration){
			this.res_vod_duration = res_vod_duration;
		}
		public String getRes_img_link(){
			return this.res_img_link;
		}		
		public void setRes_img_link(String res_img_link){
			this.res_img_link = res_img_link;
		}
		public String getRes_vod_main(){
			return this.res_vod_main;
		}		
		public void setRes_vod_main(String res_vod_main){
			this.res_vod_main = res_vod_main;
		}
		public String getRes_scor_identifier(){
			return this.res_scor_identifier;
		}		
		public void setRes_scor_identifier(String res_scor_identifier){
			this.res_scor_identifier = res_scor_identifier;
		}
		public Integer getRes_first_res_id(){
			return this.res_first_res_id;
		}		
		public void setRes_first_res_id(Integer res_first_res_id){
			this.res_first_res_id = res_first_res_id;
		}
		public Long getSubmit_num() {
			return submit_num;
		}
		public void setSubmit_num(Long submit_num) {
			this.submit_num = submit_num;
		}
		public ModuleEvaluation getMov() {
			return mov;
		}

		public void setMov(ModuleEvaluation mov) {
			this.mov = mov;
		}

		public Module getMod() {
			return mod;
		}
		public void setMod(Module mod) {
			this.mod = mod;
		}
		
		public TcTrainingCenter getTcr() {
			return tcr;
		}
		public void setTcr(TcTrainingCenter tcr) {
			this.tcr = tcr;
		}
		
		public Resources(){
		}
		public int getPgr_attempt_nbr() {
			return pgr_attempt_nbr;
		}
		public void setPgr_attempt_nbr(int pgr_attempt_nbr) {
			this.pgr_attempt_nbr = pgr_attempt_nbr;
		}
		public String getPgr_status() {
			return pgr_status;
		}
		public void setPgr_status(String pgr_status) {
			this.pgr_status = pgr_status;
		}
		public String getPgr_completion_status() {
			return pgr_completion_status;
		}
		public void setPgr_completion_status(String pgr_completion_status) {
			this.pgr_completion_status = pgr_completion_status;
		}
		public String getTrainingStatus() {
			return trainingStatus;
		}
		public void setTrainingStatus(String trainingStatus) {
			this.trainingStatus = trainingStatus;
		}
		public RegUser getUser() {
			return user;
		}
		public void setUser(RegUser user) {
			this.user = user;
		}
		public AeItem getItm() {
			return itm;
		}
		public void setItm(AeItem itm) {
			this.itm = itm;
		}
		public boolean getIsRestore() {
			return isRestore;
		}
		public void setIsRestore(boolean isRestore) {
			this.isRestore = isRestore;
		}
		public boolean getIsTrainingCourse() {
			return isTrainingCourse;
		}
		public void IsTrainingCourse(boolean isTrainingCourse) {
			this.isTrainingCourse = isTrainingCourse;
		}
		
	
}