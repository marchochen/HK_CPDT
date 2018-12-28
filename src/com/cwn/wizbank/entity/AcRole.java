package com.cwn.wizbank.entity;

import java.util.Date;


public class AcRole implements java.io.Serializable {
	private static final long serialVersionUID = -6993817279285436386L;
	
	/**
	 * 学员
	 */
	public static final String ROLE_NLRN_1 = "NLRN_1";
	/**
	 * 讲师
	 */
	public static final String ROLE_INSTR_1 = "INSTR_1";
	/**
	 * 培训管理员
	 */
	public static final String ROLE_TADM_1 = "TADM_1";
	/**
	 * 系统管理员
	 */
	public static final String ROLE_ADM_1 = "ADM_1";
	/**
	 * 考试监考员
	 */
	public static final String ROLE_EXA_1 = "EXA_1";
	
	/**
	 * 自定义角色的 level，比培训管理员低一级，培训管理员是2，越小越高
	 */
	public static final int ROLE_CUSTOM_LEVEL = 3;
	
	
		/**
		 * pk
		 * null
		 **/
		Long rol_id;
		/**
		 * null
		 **/
		String rol_ext_id;
		/**
		 * null
		 **/
		Long rol_seq_id;
		/**
		 * null
		 **/
		Long rol_ste_ent_id;
		/**
		 * null
		 **/
		String rol_url_home;
		/**
		 * null
		 **/
		String rol_xml;
		/**
		 * null
		 **/
		Boolean rol_ste_default_ind;
		/**
		 * null
		 **/
		Boolean rol_report_ind;
		/**
		 * null
		 **/
		String rol_skin_root;
		/**
		 * null
		 **/
		String rol_status;
		/**
		 * null
		 **/
		String rol_ste_uid;
		/**
		 * null
		 **/
		String rol_target_ent_type;
		/**
		 * null
		 **/
		Long rol_auth_level;
		/**
		 * null
		 **/
		Long rol_tc_ind;
		/**
		 * null
		 **/
		String rol_title;
		/**
		 * null
		 **/
		String rol_type;
		/**
		 * null
		 **/
		String rol_create_usr_id;
		/**
		 * null
		 **/
		Date rol_update_timestamp;
		/**
		 * null
		 **/
		String rol_update_usr_id;
		/**
		 * null
		 **/
		Date rol_create_timestamp;
		
		
		RegUser user;
		
		/**菜单栏*/
		AcFunction acFunction;
		/**新增角色菜单栏id*/
		String ftn_id_lst;
		
		/**
		 * 辅助属性，角色是否已被用户引用
		 */
		boolean isRefByUser;
		
		public AcRole(){
		}
	
		public Long getRol_id(){
			return this.rol_id;
		}		
		public void setRol_id(Long rol_id){
			this.rol_id = rol_id;
		}
		public String getRol_ext_id(){
			return this.rol_ext_id;
		}		
		public void setRol_ext_id(String rol_ext_id){
			this.rol_ext_id = rol_ext_id;
		}
		public Long getRol_seq_id(){
			return this.rol_seq_id;
		}		
		public void setRol_seq_id(Long rol_seq_id){
			this.rol_seq_id = rol_seq_id;
		}
		public Long getRol_ste_ent_id(){
			return this.rol_ste_ent_id;
		}		
		public void setRol_ste_ent_id(Long rol_ste_ent_id){
			this.rol_ste_ent_id = rol_ste_ent_id;
		}
		public String getRol_url_home(){
			return this.rol_url_home;
		}		
		public void setRol_url_home(String rol_url_home){
			this.rol_url_home = rol_url_home;
		}
		public String getRol_xml(){
			return this.rol_xml;
		}		
		public void setRol_xml(String rol_xml){
			this.rol_xml = rol_xml;
		}
		public Boolean getRol_ste_default_ind(){
			return this.rol_ste_default_ind;
		}		
		public void setRol_ste_default_ind(Boolean rol_ste_default_ind){
			this.rol_ste_default_ind = rol_ste_default_ind;
		}
		public Boolean getRol_report_ind(){
			return this.rol_report_ind;
		}		
		public void setRol_report_ind(Boolean rol_report_ind){
			this.rol_report_ind = rol_report_ind;
		}
		public String getRol_skin_root(){
			return this.rol_skin_root;
		}		
		public void setRol_skin_root(String rol_skin_root){
			this.rol_skin_root = rol_skin_root;
		}
		public String getRol_status(){
			return this.rol_status;
		}		
		public void setRol_status(String rol_status){
			this.rol_status = rol_status;
		}
		public String getRol_ste_uid(){
			return this.rol_ste_uid;
		}		
		public void setRol_ste_uid(String rol_ste_uid){
			this.rol_ste_uid = rol_ste_uid;
		}
		public String getRol_target_ent_type(){
			return this.rol_target_ent_type;
		}		
		public void setRol_target_ent_type(String rol_target_ent_type){
			this.rol_target_ent_type = rol_target_ent_type;
		}
		public Long getRol_auth_level(){
			return this.rol_auth_level;
		}		
		public void setRol_auth_level(Long rol_auth_level){
			this.rol_auth_level = rol_auth_level;
		}
		public Long getRol_tc_ind(){
			return this.rol_tc_ind;
		}		
		public void setRol_tc_ind(Long rol_tc_ind){
			this.rol_tc_ind = rol_tc_ind;
		}
		public String getRol_title(){
			return this.rol_title;
		}		
		public void setRol_title(String rol_title){
			this.rol_title = rol_title;
		}
		public String getRol_type(){
			return this.rol_type;
		}		
		public void setRol_type(String rol_type){
			this.rol_type = rol_type;
		}
		public String getRol_create_usr_id(){
			return this.rol_create_usr_id;
		}		
		public void setRol_create_usr_id(String rol_create_usr_id){
			this.rol_create_usr_id = rol_create_usr_id;
		}
		public Date getRol_update_timestamp(){
			return this.rol_update_timestamp;
		}		
		public void setRol_update_timestamp(Date rol_update_timestamp){
			this.rol_update_timestamp = rol_update_timestamp;
		}
		public String getRol_update_usr_id(){
			return this.rol_update_usr_id;
		}		
		public void setRol_update_usr_id(String rol_update_usr_id){
			this.rol_update_usr_id = rol_update_usr_id;
		}
		public Date getRol_create_timestamp(){
			return this.rol_create_timestamp;
		}		
		public void setRol_create_timestamp(Date rol_create_timestamp){
			this.rol_create_timestamp = rol_create_timestamp;
		}

		public RegUser getUser() {
			return user;
		}

		public void setUser(RegUser user) {
			this.user = user;
		}

		public AcFunction getAcFunction() {
			return acFunction;
		}

		public void setAcFunction(AcFunction acFunction) {
			this.acFunction = acFunction;
		}

		public String getFtn_id_lst() {
			return ftn_id_lst;
		}

		public void setFtn_id_lst(String ftn_id_lst) {
			this.ftn_id_lst = ftn_id_lst;
		}

		public boolean getIsRefByUser() {
			return isRefByUser;
		}

		public void setIsRefByUser(boolean isRefByUser) {
			this.isRefByUser = isRefByUser;
		}
		
}