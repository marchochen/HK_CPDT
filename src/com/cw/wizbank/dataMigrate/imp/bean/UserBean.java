package com.cw.wizbank.dataMigrate.imp.bean;

import java.sql.Timestamp;

public class UserBean extends ImportObject {
	private String user_id;
	private String password;
	private String name;
	private String nickname;
	private String gender;
	private Timestamp date_of_birth;
	private String email;
	private String phone;
	private String fax;
	private String extension_41;
	private String extension_42;
	private String job_title;
	private Timestamp join_date;
	private String source;
	private String extension_1;
	private String extension_2;
	private String extension_3;
	private String extension_4;
	private String extension_5;
	private String extension_6;
	private String extension_7;
	private String extension_8;
	private String extension_9;
	private String extension_10;
	private Timestamp extension_11;
	private String extension_12;
	private String extension_13;
	private String extension_14;
	private String extension_15;
	private String extension_16;
	private String extension_17;
	private String extension_18;
	private String extension_19;
	private String extension_20;
	private String extension_21;
	private String extension_22;
	private String extension_23;
	private String extension_24;
	private String extension_25;
	private String extension_26;
	private String extension_27;
	private String extension_28;
	private String extension_29;
	private String extension_30;
	private String extension_31;
	private String extension_32;
	private String extension_33;
	private String extension_34;
	private String extension_35;
	private String extension_36;
	private String extension_37;
	private String extension_38;
	private String extension_39;
	private String extension_40;
	private String extension_43;
	private String extension_44;
	private String extension_45;
	private String group_code_level1;
	private String group_code_level2;
	private String group_code_level3;
	private String group_code_level4;
	private String group_code_level5;
	private String group_code_level6;
	private String group_code_level7;;
	private String group_title_level1;
	private String group_title_level2;
	private String group_title_level3;
	private String group_title_level4;
	private String group_title_level5;
	private String group_title_level6;
	private String group_title_level7;
	private String grade_code_level1;
	private String grade_code_level2;
	private String grade_code_level3;
	private String grade_title_level1;
	private String grade_title_level2;
	private String grade_title_level3;
	private String competency;
	private String role;
	private String direct_supervisors;
	private String app_approval_usg_ent_id;
	private String supervised_groups;
	
	public long usr_usg_id;//用户关联的用户组ID
	public long usr_ent_id;//用户ID
	public long usr_ugr_id;//用户关联的职务ID
	public long upt_id;//用户关联的岗位ID
	public long highest_approval_usg_id;//最高报名审批用户组
	public String[] supervise_target_ent_ids;//下属部门

	
	public UserBean() {
	}

	public UserBean(int lineno, boolean valid) {
		this.lineno = lineno;
		this.valid = valid;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Timestamp getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(Timestamp date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getExtension_41() {
		return extension_41;
	}

	public void setExtension_41(String extension_41) {
		this.extension_41 = extension_41;
	}

	public String getExtension_42() {
		return extension_42;
	}

	public void setExtension_42(String extension_42) {
		this.extension_42 = extension_42;
	}

	public String getJob_title() {
		return job_title;
	}

	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}

	public Timestamp getJoin_date() {
		return join_date;
	}

	public void setJoin_date(Timestamp join_date) {
		this.join_date = join_date;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getExtension_1() {
		return extension_1;
	}

	public void setExtension_1(String extension_1) {
		this.extension_1 = extension_1;
	}

	public String getExtension_2() {
		return extension_2;
	}

	public void setExtension_2(String extension_2) {
		this.extension_2 = extension_2;
	}

	public String getExtension_3() {
		return extension_3;
	}

	public void setExtension_3(String extension_3) {
		this.extension_3 = extension_3;
	}

	public String getExtension_4() {
		return extension_4;
	}

	public void setExtension_4(String extension_4) {
		this.extension_4 = extension_4;
	}

	public String getExtension_5() {
		return extension_5;
	}

	public void setExtension_5(String extension_5) {
		this.extension_5 = extension_5;
	}

	public String getExtension_6() {
		return extension_6;
	}

	public void setExtension_6(String extension_6) {
		this.extension_6 = extension_6;
	}

	public String getExtension_7() {
		return extension_7;
	}

	public void setExtension_7(String extension_7) {
		this.extension_7 = extension_7;
	}

	public String getExtension_8() {
		return extension_8;
	}

	public void setExtension_8(String extension_8) {
		this.extension_8 = extension_8;
	}

	public String getExtension_9() {
		return extension_9;
	}

	public void setExtension_9(String extension_9) {
		this.extension_9 = extension_9;
	}

	public String getExtension_10() {
		return extension_10;
	}

	public void setExtension_10(String extension_10) {
		this.extension_10 = extension_10;
	}

	public Timestamp getExtension_11() {
		return extension_11;
	}

	public void setExtension_11(Timestamp extension_11) {
		this.extension_11 = extension_11;
	}

	public String getExtension_12() {
		return extension_12;
	}

	public void setExtension_12(String extension_12) {
		this.extension_12 = extension_12;
	}

	public String getExtension_13() {
		return extension_13;
	}

	public void setExtension_13(String extension_13) {
		this.extension_13 = extension_13;
	}

	public String getExtension_14() {
		return extension_14;
	}

	public void setExtension_14(String extension_14) {
		this.extension_14 = extension_14;
	}

	public String getExtension_15() {
		return extension_15;
	}

	public void setExtension_15(String extension_15) {
		this.extension_15 = extension_15;
	}

	public String getExtension_16() {
		return extension_16;
	}

	public void setExtension_16(String extension_16) {
		this.extension_16 = extension_16;
	}

	public String getExtension_17() {
		return extension_17;
	}

	public void setExtension_17(String extension_17) {
		this.extension_17 = extension_17;
	}

	public String getExtension_18() {
		return extension_18;
	}

	public void setExtension_18(String extension_18) {
		this.extension_18 = extension_18;
	}

	public String getExtension_19() {
		return extension_19;
	}

	public void setExtension_19(String extension_19) {
		this.extension_19 = extension_19;
	}

	public String getExtension_20() {
		return extension_20;
	}

	public void setExtension_20(String extension_20) {
		this.extension_20 = extension_20;
	}

	public String getExtension_21() {
		return extension_21;
	}

	public void setExtension_21(String extension_21) {
		this.extension_21 = extension_21;
	}

	public String getExtension_22() {
		return extension_22;
	}

	public void setExtension_22(String extension_22) {
		this.extension_22 = extension_22;
	}

	public String getExtension_23() {
		return extension_23;
	}

	public void setExtension_23(String extension_23) {
		this.extension_23 = extension_23;
	}

	public String getExtension_24() {
		return extension_24;
	}

	public void setExtension_24(String extension_24) {
		this.extension_24 = extension_24;
	}

	public String getExtension_25() {
		return extension_25;
	}

	public void setExtension_25(String extension_25) {
		this.extension_25 = extension_25;
	}

	public String getExtension_26() {
		return extension_26;
	}

	public void setExtension_26(String extension_26) {
		this.extension_26 = extension_26;
	}

	public String getExtension_27() {
		return extension_27;
	}

	public void setExtension_27(String extension_27) {
		this.extension_27 = extension_27;
	}

	public String getExtension_28() {
		return extension_28;
	}

	public void setExtension_28(String extension_28) {
		this.extension_28 = extension_28;
	}

	public String getExtension_29() {
		return extension_29;
	}

	public void setExtension_29(String extension_29) {
		this.extension_29 = extension_29;
	}

	public String getExtension_30() {
		return extension_30;
	}

	public void setExtension_30(String extension_30) {
		this.extension_30 = extension_30;
	}

	public String getExtension_31() {
		return extension_31;
	}

	public void setExtension_31(String extension_31) {
		this.extension_31 = extension_31;
	}

	public String getExtension_32() {
		return extension_32;
	}

	public void setExtension_32(String extension_32) {
		this.extension_32 = extension_32;
	}

	public String getExtension_33() {
		return extension_33;
	}

	public void setExtension_33(String extension_33) {
		this.extension_33 = extension_33;
	}

	public String getExtension_34() {
		return extension_34;
	}

	public void setExtension_34(String extension_34) {
		this.extension_34 = extension_34;
	}

	public String getExtension_35() {
		return extension_35;
	}

	public void setExtension_35(String extension_35) {
		this.extension_35 = extension_35;
	}

	public String getExtension_36() {
		return extension_36;
	}

	public void setExtension_36(String extension_36) {
		this.extension_36 = extension_36;
	}

	public String getExtension_37() {
		return extension_37;
	}

	public void setExtension_37(String extension_37) {
		this.extension_37 = extension_37;
	}

	public String getExtension_38() {
		return extension_38;
	}

	public void setExtension_38(String extension_38) {
		this.extension_38 = extension_38;
	}

	public String getExtension_39() {
		return extension_39;
	}

	public void setExtension_39(String extension_39) {
		this.extension_39 = extension_39;
	}

	public String getExtension_40() {
		return extension_40;
	}

	public void setExtension_40(String extension_40) {
		this.extension_40 = extension_40;
	}

	public String getExtension_43() {
		return extension_43;
	}

	public void setExtension_43(String extension_43) {
		this.extension_43 = extension_43;
	}

	public String getExtension_44() {
		return extension_44;
	}

	public void setExtension_44(String extension_44) {
		this.extension_44 = extension_44;
	}

	public String getExtension_45() {
		return extension_45;
	}

	public void setExtension_45(String extension_45) {
		this.extension_45 = extension_45;
	}

	public String getGroup_code_level1() {
		return group_code_level1;
	}

	public void setGroup_code_level1(String group_code_level1) {
		this.group_code_level1 = group_code_level1;
	}

	public String getGroup_code_level2() {
		return group_code_level2;
	}

	public void setGroup_code_level2(String group_code_level2) {
		this.group_code_level2 = group_code_level2;
	}

	public String getGroup_code_level3() {
		return group_code_level3;
	}

	public void setGroup_code_level3(String group_code_level3) {
		this.group_code_level3 = group_code_level3;
	}

	public String getGroup_code_level4() {
		return group_code_level4;
	}

	public void setGroup_code_level4(String group_code_level4) {
		this.group_code_level4 = group_code_level4;
	}

	public String getGroup_code_level5() {
		return group_code_level5;
	}

	public void setGroup_code_level5(String group_code_level5) {
		this.group_code_level5 = group_code_level5;
	}

	public String getGroup_code_level6() {
		return group_code_level6;
	}

	public void setGroup_code_level6(String group_code_level6) {
		this.group_code_level6 = group_code_level6;
	}

	public String getGroup_code_level7() {
		return group_code_level7;
	}

	public void setGroup_code_level7(String group_code_level7) {
		this.group_code_level7 = group_code_level7;
	}

	public String getGroup_title_level1() {
		return group_title_level1;
	}

	public void setGroup_title_level1(String group_title_level1) {
		this.group_title_level1 = group_title_level1;
	}

	public String getGroup_title_level2() {
		return group_title_level2;
	}

	public void setGroup_title_level2(String group_title_level2) {
		this.group_title_level2 = group_title_level2;
	}

	public String getGroup_title_level3() {
		return group_title_level3;
	}

	public void setGroup_title_level3(String group_title_level3) {
		this.group_title_level3 = group_title_level3;
	}

	public String getGroup_title_level4() {
		return group_title_level4;
	}

	public void setGroup_title_level4(String group_title_level4) {
		this.group_title_level4 = group_title_level4;
	}

	public String getGroup_title_level5() {
		return group_title_level5;
	}

	public void setGroup_title_level5(String group_title_level5) {
		this.group_title_level5 = group_title_level5;
	}

	public String getGroup_title_level6() {
		return group_title_level6;
	}

	public void setGroup_title_level6(String group_title_level6) {
		this.group_title_level6 = group_title_level6;
	}

	public String getGroup_title_level7() {
		return group_title_level7;
	}

	public void setGroup_title_level7(String group_title_level7) {
		this.group_title_level7 = group_title_level7;
	}

	public String getGrade_code_level1() {
		return grade_code_level1;
	}

	public void setGrade_code_level1(String grade_code_level1) {
		this.grade_code_level1 = grade_code_level1;
	}

	public String getGrade_code_level2() {
		return grade_code_level2;
	}

	public void setGrade_code_level2(String grade_code_level2) {
		this.grade_code_level2 = grade_code_level2;
	}

	public String getGrade_code_level3() {
		return grade_code_level3;
	}

	public void setGrade_code_level3(String grade_code_level3) {
		this.grade_code_level3 = grade_code_level3;
	}

	public String getGrade_title_level1() {
		return grade_title_level1;
	}

	public void setGrade_title_level1(String grade_title_level1) {
		this.grade_title_level1 = grade_title_level1;
	}

	public String getGrade_title_level2() {
		return grade_title_level2;
	}

	public void setGrade_title_level2(String grade_title_level2) {
		this.grade_title_level2 = grade_title_level2;
	}

	public String getGrade_title_level3() {
		return grade_title_level3;
	}

	public void setGrade_title_level3(String grade_title_level3) {
		this.grade_title_level3 = grade_title_level3;
	}

	public String getCompetency() {
		return competency;
	}

	public void setCompetency(String competency) {
		this.competency = competency;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDirect_supervisors() {
		return direct_supervisors;
	}

	public void setDirect_supervisors(String direct_supervisors) {
		this.direct_supervisors = direct_supervisors;
	}

	public String getApp_approval_usg_ent_id() {
		return app_approval_usg_ent_id;
	}

	public void setApp_approval_usg_ent_id(String app_approval_usg_ent_id) {
		this.app_approval_usg_ent_id = app_approval_usg_ent_id;
	}

	public String getSupervised_groups() {
		return supervised_groups;
	}

	public void setSupervised_groups(String supervised_groups) {
		this.supervised_groups = supervised_groups;
	}
	
	
	
}
