package com.cwn.wizbank.entity;

import java.util.Date;
import java.util.List;

/**
 * 用户
 * 2014-8-7 下午5:56:32
 */
public class RegUser implements java.io.Serializable {
	private static final long serialVersionUID = -6785481198651119596L;
		/**
		 * pk
		 * 
		 **/
		String usr_id;
		/**
		 * 用户ID
		 **/
		Long usr_ent_id;
		/**
		 * 密码
		 **/
		String usr_pwd;
		/**
		 * 
		 **/
		String usr_email;
		/**
		 * 
		 **/
		String usr_email_2;
		/**
		 * 
		 **/
		String usr_full_name_bil;
		/**
		 * 
		 **/
		String usr_initial_name_bil;
		/**
		 * 
		 **/
		String usr_last_name_bil;
		/**
		 * 
		 **/
		String usr_first_name_bil;
		/**
		 * 
		 **/
		String usr_display_bil;
		/**
		 * 
		 **/
		String usr_gender;
		/**
		 * 
		 **/
		Date usr_bday;
		/**
		 * 
		 **/
		String usr_hkid;
		/**
		 * 
		 **/
		String usr_other_id_no;
		/**
		 * 
		 **/
		String usr_other_id_type;
		/**
		 * 
		 **/
		String usr_tel_1;
		/**
		 * 
		 **/
		String usr_tel_2;
		/**
		 * 
		 **/
		String usr_fax_1;
		/**
		 * 
		 **/
		String usr_country_bil;
		/**
		 * 
		 **/
		String usr_postal_code_bil;
		/**
		 * 
		 **/
		String usr_state_bil;
		/**
		 * 
		 **/
		String usr_address_bil;
		/**
		 * 
		 **/
		String usr_class;
		/**
		 * 
		 **/
		String usr_class_number;
		/**
		 * 
		 **/
		Date usr_signup_date;
		/**
		 * 
		 **/
		String usr_last_login_role;
		/**
		 * 
		 **/
		Date usr_last_login_date;
		/**
		 * 
		 **/
		String usr_status;
		/**
		 * 
		 **/
		Date usr_upd_date;
		/**
		 * 
		 **/
		Long usr_ste_ent_id;
		/**
		 * 
		 **/
		String usr_ste_usr_id;
		/**
		 * 
		 **/
		String usr_extra_1;
		/**
		 * 
		 **/
		String usr_cost_center;
		/**
		 * 
		 **/
		Long usr_login_trial;
		/**
		 * 
		 **/
		String usr_login_status;
		/**
		 * 
		 **/
		String usr_remark_xml;
		/**
		 * 
		 **/
		String usr_extra_2;
		/**
		 * 
		 **/
		String usr_extra_3;
		/**
		 * 
		 **/
		String usr_extra_4;
		/**
		 * 
		 **/
		String usr_extra_5;
		/**
		 * 
		 **/
		String usr_extra_6;
		/**
		 * 
		 **/
		String usr_extra_7;
		/**
		 * 
		 **/
		String usr_extra_8;
		/**
		 * 
		 **/
		String usr_extra_9;
		/**
		 * 
		 **/
		String usr_extra_10;
		/**
		 * 
		 **/
		String usr_approve_usr_id;
		/**
		 * 
		 **/
		Date usr_approve_timestamp;
		/**
		 * 
		 **/
		String usr_approve_reason;
		/**
		 * 
		 **/
		Long usr_pwd_need_change_ind;
		/**
		 * 
		 **/
		Date usr_pwd_upd_timestamp;
		/**
		 * 
		 **/
		Long usr_syn_rol_ind;
		/**
		 * 
		 **/
		String usr_not_syn_gpm_type;
		/**
		 * 
		 **/
		Date usr_join_datetime;
		/**
		 * 
		 **/
		String usr_job_title;
		/**
		 * 
		 **/
		Long usr_app_approval_usg_ent_id;
		/**
		 * 
		 **/
		String usr_source;
		/**
		 * 
		 **/
		String usr_nickname;
		/**
		 * 
		 **/
		Long usr_choice_tcr_id;
		/**
		 * 
		 **/
		String usr_weixin_id;
		/**
		 * 用户组名称
		 **/
		String usg_display_bil;
		/**
		 * 职务名称
		 **/
		String ugr_display_bil;
		
		RegUserExtension userExt;
		
		SnsAttention snsAttention;
		/**
		 * 账号有效期
		 */
		Date urx_extra_datetime_11;
		/**
		 * 个人描述
		 */
		String urx_extra_44;
		/**
		 * 兴趣
		 */
		String urx_extra_45;
		/**
		 * 用户组编号
		 */
		String usr_usg_code;
		/**
		 * 职级编号
		 */
		String ugr_code;
		/**
		 * 岗位编号
		 */
		String upt_code;
		/**
		 * 岗位
		 */
		String upt_title;
		/**
		 * QQ
		 */
		String urx_extra_41;
		/**
		 * 最高报名审批用户组编号
		 */
		String usr_app_approval_usg_code;
		//非数据库字段
		String usr_photo;
		
		String iti_level;
		
		long iti_score;
		
		String is_instructor;
		
		InstructorInf instr;
	
		/**
		 * 用户角色
		 */
		List<AcRole>  acRole;
		/**
		 * 直属上司 
		 */
		List<RegUser> regUser;
		/**
		 * 下属部门
		 */
		List<UserGroup> userGroup;
		
		public RegUser(){

		}

		public RegUser(Long usr_ent_id, String usr_display_bil, String usr_photo) {
			super();
			this.usr_ent_id = usr_ent_id;
			this.usr_display_bil = usr_display_bil;
			this.usr_photo = usr_photo;
		}



		public String getUsr_id() {
			return usr_id;
		}


		public void setUsr_id(String usr_id) {
			this.usr_id = usr_id;
		}


		public Long getUsr_ent_id() {
			return usr_ent_id;
		}


		public void setUsr_ent_id(Long usr_ent_id) {
			this.usr_ent_id = usr_ent_id;
		}


		public String getUsr_pwd() {
			return usr_pwd;
		}


		public void setUsr_pwd(String usr_pwd) {
			this.usr_pwd = usr_pwd;
		}


		public String getUsr_email() {
			return usr_email;
		}


		public void setUsr_email(String usr_email) {
			this.usr_email = usr_email;
		}


		public String getUsr_email_2() {
			return usr_email_2;
		}


		public void setUsr_email_2(String usr_email_2) {
			this.usr_email_2 = usr_email_2;
		}


		public String getUsr_full_name_bil() {
			return usr_full_name_bil;
		}


		public void setUsr_full_name_bil(String usr_full_name_bil) {
			this.usr_full_name_bil = usr_full_name_bil;
		}


		public String getUsr_initial_name_bil() {
			return usr_initial_name_bil;
		}


		public void setUsr_initial_name_bil(String usr_initial_name_bil) {
			this.usr_initial_name_bil = usr_initial_name_bil;
		}


		public String getUsr_last_name_bil() {
			return usr_last_name_bil;
		}


		public void setUsr_last_name_bil(String usr_last_name_bil) {
			this.usr_last_name_bil = usr_last_name_bil;
		}


		public String getUsr_first_name_bil() {
			return usr_first_name_bil;
		}


		public void setUsr_first_name_bil(String usr_first_name_bil) {
			this.usr_first_name_bil = usr_first_name_bil;
		}


		public String getUsr_display_bil() {
			return usr_display_bil;
		}


		public void setUsr_display_bil(String usr_display_bil) {
			this.usr_display_bil = usr_display_bil;
		}


		public String getUsr_gender() {
			return usr_gender;
		}


		public void setUsr_gender(String usr_gender) {
			this.usr_gender = usr_gender;
		}


		public Date getUsr_bday() {
			return usr_bday;
		}


		public void setUsr_bday(Date usr_bday) {
			this.usr_bday = usr_bday;
		}


		public String getUsr_hkid() {
			return usr_hkid;
		}


		public void setUsr_hkid(String usr_hkid) {
			this.usr_hkid = usr_hkid;
		}


		public String getUsr_other_id_no() {
			return usr_other_id_no;
		}


		public void setUsr_other_id_no(String usr_other_id_no) {
			this.usr_other_id_no = usr_other_id_no;
		}


		public String getUsr_other_id_type() {
			return usr_other_id_type;
		}


		public void setUsr_other_id_type(String usr_other_id_type) {
			this.usr_other_id_type = usr_other_id_type;
		}


		public String getUsr_tel_1() {
			return usr_tel_1;
		}


		public void setUsr_tel_1(String usr_tel_1) {
			this.usr_tel_1 = usr_tel_1;
		}


		public String getUsr_tel_2() {
			return usr_tel_2;
		}


		public void setUsr_tel_2(String usr_tel_2) {
			this.usr_tel_2 = usr_tel_2;
		}


		public String getUsr_fax_1() {
			return usr_fax_1;
		}


		public void setUsr_fax_1(String usr_fax_1) {
			this.usr_fax_1 = usr_fax_1;
		}


		public String getUsr_country_bil() {
			return usr_country_bil;
		}


		public void setUsr_country_bil(String usr_country_bil) {
			this.usr_country_bil = usr_country_bil;
		}


		public String getUsr_postal_code_bil() {
			return usr_postal_code_bil;
		}


		public void setUsr_postal_code_bil(String usr_postal_code_bil) {
			this.usr_postal_code_bil = usr_postal_code_bil;
		}


		public String getUsr_state_bil() {
			return usr_state_bil;
		}


		public void setUsr_state_bil(String usr_state_bil) {
			this.usr_state_bil = usr_state_bil;
		}


		public String getUsr_address_bil() {
			return usr_address_bil;
		}


		public void setUsr_address_bil(String usr_address_bil) {
			this.usr_address_bil = usr_address_bil;
		}


		public String getUsr_class() {
			return usr_class;
		}


		public void setUsr_class(String usr_class) {
			this.usr_class = usr_class;
		}


		public String getUsr_class_number() {
			return usr_class_number;
		}


		public void setUsr_class_number(String usr_class_number) {
			this.usr_class_number = usr_class_number;
		}


		public Date getUsr_signup_date() {
			return usr_signup_date;
		}


		public void setUsr_signup_date(Date usr_signup_date) {
			this.usr_signup_date = usr_signup_date;
		}


		public String getUsr_last_login_role() {
			return usr_last_login_role;
		}


		public void setUsr_last_login_role(String usr_last_login_role) {
			this.usr_last_login_role = usr_last_login_role;
		}


		public Date getUsr_last_login_date() {
			return usr_last_login_date;
		}


		public void setUsr_last_login_date(Date usr_last_login_date) {
			this.usr_last_login_date = usr_last_login_date;
		}


		public String getUsr_status() {
			return usr_status;
		}


		public void setUsr_status(String usr_status) {
			this.usr_status = usr_status;
		}


		public Date getUsr_upd_date() {
			return usr_upd_date;
		}


		public void setUsr_upd_date(Date usr_upd_date) {
			this.usr_upd_date = usr_upd_date;
		}


		public Long getUsr_ste_ent_id() {
			return usr_ste_ent_id;
		}


		public void setUsr_ste_ent_id(Long usr_ste_ent_id) {
			this.usr_ste_ent_id = usr_ste_ent_id;
		}


		public String getUsr_ste_usr_id() {
			return usr_ste_usr_id;
		}


		public void setUsr_ste_usr_id(String usr_ste_usr_id) {
			this.usr_ste_usr_id = usr_ste_usr_id;
		}


		public String getUsr_extra_1() {
			return usr_extra_1;
		}


		public void setUsr_extra_1(String usr_extra_1) {
			this.usr_extra_1 = usr_extra_1;
		}


		public String getUsr_cost_center() {
			return usr_cost_center;
		}


		public void setUsr_cost_center(String usr_cost_center) {
			this.usr_cost_center = usr_cost_center;
		}


		public Long getUsr_login_trial() {
			return usr_login_trial;
		}


		public void setUsr_login_trial(Long usr_login_trial) {
			this.usr_login_trial = usr_login_trial;
		}


		public String getUsr_login_status() {
			return usr_login_status;
		}


		public void setUsr_login_status(String usr_login_status) {
			this.usr_login_status = usr_login_status;
		}


		public String getUsr_remark_xml() {
			return usr_remark_xml;
		}


		public void setUsr_remark_xml(String usr_remark_xml) {
			this.usr_remark_xml = usr_remark_xml;
		}


		public String getUsr_extra_2() {
			return usr_extra_2;
		}


		public void setUsr_extra_2(String usr_extra_2) {
			this.usr_extra_2 = usr_extra_2;
		}


		public String getUsr_extra_3() {
			return usr_extra_3;
		}


		public void setUsr_extra_3(String usr_extra_3) {
			this.usr_extra_3 = usr_extra_3;
		}


		public String getUsr_extra_4() {
			return usr_extra_4;
		}


		public void setUsr_extra_4(String usr_extra_4) {
			this.usr_extra_4 = usr_extra_4;
		}


		public String getUsr_extra_5() {
			return usr_extra_5;
		}


		public void setUsr_extra_5(String usr_extra_5) {
			this.usr_extra_5 = usr_extra_5;
		}


		public String getUsr_extra_6() {
			return usr_extra_6;
		}


		public void setUsr_extra_6(String usr_extra_6) {
			this.usr_extra_6 = usr_extra_6;
		}


		public String getUsr_extra_7() {
			return usr_extra_7;
		}


		public void setUsr_extra_7(String usr_extra_7) {
			this.usr_extra_7 = usr_extra_7;
		}


		public String getUsr_extra_8() {
			return usr_extra_8;
		}


		public void setUsr_extra_8(String usr_extra_8) {
			this.usr_extra_8 = usr_extra_8;
		}


		public String getUsr_extra_9() {
			return usr_extra_9;
		}


		public void setUsr_extra_9(String usr_extra_9) {
			this.usr_extra_9 = usr_extra_9;
		}


		public String getUsr_extra_10() {
			return usr_extra_10;
		}


		public void setUsr_extra_10(String usr_extra_10) {
			this.usr_extra_10 = usr_extra_10;
		}


		public String getUsr_approve_usr_id() {
			return usr_approve_usr_id;
		}


		public void setUsr_approve_usr_id(String usr_approve_usr_id) {
			this.usr_approve_usr_id = usr_approve_usr_id;
		}


		public Date getUsr_approve_timestamp() {
			return usr_approve_timestamp;
		}


		public void setUsr_approve_timestamp(Date usr_approve_timestamp) {
			this.usr_approve_timestamp = usr_approve_timestamp;
		}


		public String getUsr_approve_reason() {
			return usr_approve_reason;
		}


		public void setUsr_approve_reason(String usr_approve_reason) {
			this.usr_approve_reason = usr_approve_reason;
		}


		public Long getUsr_pwd_need_change_ind() {
			return usr_pwd_need_change_ind;
		}


		public void setUsr_pwd_need_change_ind(Long usr_pwd_need_change_ind) {
			this.usr_pwd_need_change_ind = usr_pwd_need_change_ind;
		}


		public Date getUsr_pwd_upd_timestamp() {
			return usr_pwd_upd_timestamp;
		}


		public void setUsr_pwd_upd_timestamp(Date usr_pwd_upd_timestamp) {
			this.usr_pwd_upd_timestamp = usr_pwd_upd_timestamp;
		}


		public Long getUsr_syn_rol_ind() {
			return usr_syn_rol_ind;
		}


		public void setUsr_syn_rol_ind(Long usr_syn_rol_ind) {
			this.usr_syn_rol_ind = usr_syn_rol_ind;
		}


		public String getUsr_not_syn_gpm_type() {
			return usr_not_syn_gpm_type;
		}


		public void setUsr_not_syn_gpm_type(String usr_not_syn_gpm_type) {
			this.usr_not_syn_gpm_type = usr_not_syn_gpm_type;
		}


		public Date getUsr_join_datetime() {
			return usr_join_datetime;
		}


		public void setUsr_join_datetime(Date usr_join_datetime) {
			this.usr_join_datetime = usr_join_datetime;
		}


		public String getUsr_job_title() {
			return usr_job_title;
		}


		public void setUsr_job_title(String usr_job_title) {
			this.usr_job_title = usr_job_title;
		}


		public Long getUsr_app_approval_usg_ent_id() {
			return usr_app_approval_usg_ent_id;
		}


		public void setUsr_app_approval_usg_ent_id(Long usr_app_approval_usg_ent_id) {
			this.usr_app_approval_usg_ent_id = usr_app_approval_usg_ent_id;
		}


		public String getUsr_source() {
			return usr_source;
		}


		public void setUsr_source(String usr_source) {
			this.usr_source = usr_source;
		}


		public String getUsr_nickname() {
			return usr_nickname;
		}


		public void setUsr_nickname(String usr_nickname) {
			this.usr_nickname = usr_nickname;
		}


		public Long getUsr_choice_tcr_id() {
			return usr_choice_tcr_id;
		}


		public void setUsr_choice_tcr_id(Long usr_choice_tcr_id) {
			this.usr_choice_tcr_id = usr_choice_tcr_id;
		}


		public String getUsr_weixin_id() {
			return usr_weixin_id;
		}


		public void setUsr_weixin_id(String usr_weixin_id) {
			this.usr_weixin_id = usr_weixin_id;
		}


		public String getUsr_photo() {
			return usr_photo;
		}


		public void setUsr_photo(String usr_photo) {
			this.usr_photo = usr_photo;
		}


		public RegUserExtension getUserExt() {
			return userExt;
		}


		public void setUserExt(RegUserExtension userExt) {
			this.userExt = userExt;
		}


		public String getUsg_display_bil() {
			return usg_display_bil;
		}


		public void setUsg_display_bil(String usg_display_bil) {
			this.usg_display_bil = usg_display_bil;
		}


		public String getUgr_display_bil() {
			return ugr_display_bil;
		}


		public void setUgr_display_bil(String ugr_display_bil) {
			this.ugr_display_bil = ugr_display_bil;
		}


		public SnsAttention getSnsAttention() {
			return snsAttention;
		}


		public void setSnsAttention(SnsAttention snsAttention) {
			this.snsAttention = snsAttention;
		}

		public String getIti_level() {
			return iti_level;
		}

		public void setIti_level(String iti_level) {
			this.iti_level = iti_level;
		}

		public String getIs_instructor() {
			return is_instructor;
		}

		public void setIs_instructor(String is_instructor) {
			this.is_instructor = is_instructor;
		}

		public long getIti_score() {
			return iti_score;
		}

		public void setIti_score(long iti_score) {
			this.iti_score = iti_score;
		}

		public InstructorInf getInstr() {
			return instr;
		}

		public void setInstr(InstructorInf instr) {
			this.instr = instr;
		}

		public Date getUrx_extra_datetime_11() {
			return urx_extra_datetime_11;
		}

		public void setUrx_extra_datetime_11(Date urx_extra_datetime_11) {
			this.urx_extra_datetime_11 = urx_extra_datetime_11;
		}

		public String getUrx_extra_44() {
			return urx_extra_44;
		}

		public void setUrx_extra_44(String urx_extra_44) {
			this.urx_extra_44 = urx_extra_44;
		}

		public String getUrx_extra_45() {
			return urx_extra_45;
		}

		public void setUrx_extra_45(String urx_extra_45) {
			this.urx_extra_45 = urx_extra_45;
		}

		public String getUsr_usg_code() {
			return usr_usg_code;
		}

		public void setUsr_usg_code(String usr_usg_code) {
			this.usr_usg_code = usr_usg_code;
		}

		public String getUgr_code() {
			return ugr_code;
		}

		public void setUgr_code(String ugr_code) {
			this.ugr_code = ugr_code;
		}

		public String getUpt_code() {
			return upt_code;
		}

		public void setUpt_code(String upt_code) {
			this.upt_code = upt_code;
		}

		public String getUpt_title() {
			return upt_title;
		}

		public void setUpt_title(String upt_title) {
			this.upt_title = upt_title;
		}

		public List<AcRole> getAcRole() {
			return acRole;
		}

		public void setAcRole(List<AcRole> acRole) {
			this.acRole = acRole;
		}

		public List<RegUser> getRegUser() {
			return regUser;
		}

		public void setRegUser(List<RegUser> regUser) {
			this.regUser = regUser;
		}

		public List<UserGroup> getUserGroup() {
			return userGroup;
		}

		public void setUserGroup(List<UserGroup> userGroup) {
			this.userGroup = userGroup;
		}

		public String getUrx_extra_41() {
			return urx_extra_41;
		}

		public void setUrx_extra_41(String urx_extra_41) {
			this.urx_extra_41 = urx_extra_41;
		}

		public String getUsr_app_approval_usg_code() {
			return usr_app_approval_usg_code;
		}

		public void setUsr_app_approval_usg_code(String usr_app_approval_usg_code) {
			this.usr_app_approval_usg_code = usr_app_approval_usg_code;
		}

}