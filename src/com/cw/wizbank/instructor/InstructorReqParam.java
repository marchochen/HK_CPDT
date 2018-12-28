package com.cw.wizbank.instructor;

import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;
import com.cwn.wizbank.utils.CommonLog;

public class InstructorReqParam extends BaseParam {
	private long ref_ent_id;
	private boolean remain_photo_ind;
	private boolean upd_iti_img;

	private long itc_id;
	private long itc_ent_id;
	private long itc_itm_id;
	private long itc_iti_ent_id;
	private float itc_style_score;
	private float itc_quality_score;
	private float itc_structure_score;
	private float itc_interaction_score;
	private float itc_score;
	private String itc_comment;
	private String itc_create_usr_id;
	private Timestamp itc_create_datetime;
	private String itc_upd_usr_id;
	private Timestamp itc_upd_datetime;
	private String role_id;

	// Instructor
	public String search_text;
	public long iti_ent_id;
	public String iti_name;
	public String iti_gender;
	public Timestamp iti_bday;
	public String iti_mobile;
	public String iti_email;
	public String iti_img;
	public String iti_introduction;
	public String iti_level;
	public String iti_cos_type;
	public String iti_main_course;
	public String iti_type;
	public String iti_property;
	public String iti_highest_educational;
	public String iti_graduate_institutions;
	public String iti_address;
	public String iti_work_experience;
	public String iti_education_experience;
	public String iti_training_experience;
	public String iti_expertise_areas;
	public String iti_good_industry;
	public String iti_training_contacts;
	public String iti_training_tel;
	public String iti_training_email;
	public String iti_training_address;
	public String iti_status;
	public String iti_training_company;
	public String iti_type_mark;

	// InstructorCos
	public String ics_title;
	public String ics_fee;
	public String ics_hours;
	public String ics_target;
	public String ics_content;

	// Instructor Search
	private String iti_gw_str; // 岗位
	private float iti_score_from; // 评分范围
	private float iti_score_to; // 评分范围
	private String js_name;
	private int max_select;
	private String is_poup;
	private String for_time_table;
	private long ils_id;
	
	public String iti_img_select;
	public String default_image;
	public int iti_recommend;

	public String unicode(String val) {
		if (!bMultiPart) {
			try {
				val = cwUtils.unicodeFrom(val, clientEnc, encoding);
			} catch (cwException e) {
				CommonLog.error(e.getMessage(),e);
			}
		}
		return val;
	}

	public String getFor_time_table() {
		return for_time_table;
	}

	public void setFor_time_table(String forTimeTable) {
		for_time_table = forTimeTable;
	}

	public long getIls_id() {
		return ils_id;
	}

	public void setIls_id(long ilsId) {
		ils_id = ilsId;
	}

	public long getRef_ent_id() {
		return ref_ent_id;
	}

	public void setRef_ent_id(long ref_ent_id) {
		this.ref_ent_id = ref_ent_id;
	}

	public boolean isRemain_photo_ind() {
		return remain_photo_ind;
	}

	public void setRemain_photo_ind(boolean remain_photo_ind) {
		this.remain_photo_ind = remain_photo_ind;
	}

	public boolean isUpd_iti_img() {
		return upd_iti_img;
	}

	public void setUpd_iti_img(boolean upd_iti_img) {
		this.upd_iti_img = upd_iti_img;
	}

	public long getItc_id() {
		return itc_id;
	}

	public void setItc_id(long itc_id) {
		this.itc_id = itc_id;
	}

	public long getItc_ent_id() {
		return itc_ent_id;
	}

	public void setItc_ent_id(long itc_ent_id) {
		this.itc_ent_id = itc_ent_id;
	}

	public long getItc_itm_id() {
		return itc_itm_id;
	}

	public void setItc_itm_id(long itc_itm_id) {
		this.itc_itm_id = itc_itm_id;
	}

	public long getItc_iti_ent_id() {
		return itc_iti_ent_id;
	}

	public void setItc_iti_ent_id(long itc_iti_ent_id) {
		this.itc_iti_ent_id = itc_iti_ent_id;
	}

	public float getItc_style_score() {
		return itc_style_score;
	}

	public void setItc_style_score(float itc_style_score) {
		this.itc_style_score = itc_style_score;
	}

	public float getItc_quality_score() {
		return itc_quality_score;
	}

	public void setItc_quality_score(float itc_quality_score) {
		this.itc_quality_score = itc_quality_score;
	}

	public float getItc_structure_score() {
		return itc_structure_score;
	}

	public void setItc_structure_score(float itc_structure_score) {
		this.itc_structure_score = itc_structure_score;
	}

	public float getItc_interaction_score() {
		return itc_interaction_score;
	}

	public void setItc_interaction_score(float itc_interaction_score) {
		this.itc_interaction_score = itc_interaction_score;
	}

	public float getItc_score() {
		return itc_score;
	}

	public void setItc_score(float itc_score) {
		this.itc_score = itc_score;
	}

	public String getItc_comment() {
		return itc_comment;
	}

	public void setItc_comment(String itc_comment) {
		this.itc_comment = itc_comment;
	}

	public String getItc_create_usr_id() {
		return itc_create_usr_id;
	}

	public void setItc_create_usr_id(String itc_create_usr_id) {
		this.itc_create_usr_id = itc_create_usr_id;
	}

	public Timestamp getItc_create_datetime() {
		return itc_create_datetime;
	}

	public void setItc_create_datetime(Timestamp itc_create_datetime) {
		this.itc_create_datetime = itc_create_datetime;
	}

	public String getItc_upd_usr_id() {
		return itc_upd_usr_id;
	}

	public void setItc_upd_usr_id(String itc_upd_usr_id) {
		this.itc_upd_usr_id = itc_upd_usr_id;
	}

	public Timestamp getItc_upd_datetime() {
		return itc_upd_datetime;
	}

	public void setItc_upd_datetime(Timestamp itc_upd_datetime) {
		this.itc_upd_datetime = itc_upd_datetime;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public long getIti_ent_id() {
		return iti_ent_id;
	}

	public void setIti_ent_id(long iti_ent_id) {
		this.iti_ent_id = iti_ent_id;
	}


	public String getSearch_text() {
		return search_text;
	}
	public String getIti_name() {
		return iti_name;
	}
	public void setSearch_text(String search_text) {
		this.search_text = unicode(search_text);
	}
	public void setIti_name(String iti_name) {
		this.iti_name = unicode(iti_name);
	}

	public String getIti_gender() {
		return iti_gender;
	}

	public void setIti_gender(String iti_gender) {
		this.iti_gender = iti_gender;
	}

	public Timestamp getIti_bday() {
		return iti_bday;
	}

	public void setIti_bday(Timestamp iti_bday) {
		this.iti_bday = iti_bday;
	}

	public String getIti_mobile() {
		return iti_mobile;
	}

	public void setIti_mobile(String iti_mobile) {
		this.iti_mobile = iti_mobile;
	}

	public String getIti_email() {
		return iti_email;
	}

	public void setIti_email(String iti_email) {
		this.iti_email = iti_email;
	}

	public String getIti_img() {
		return iti_img;
	}

	public void setIti_img(String iti_img) {
		this.iti_img = iti_img;
	}

	public String getIti_introduction() {
		return iti_introduction;
	}

	public void setIti_introduction(String iti_introduction) {
		this.iti_introduction = unicode(iti_introduction);
	}

	public String getIti_level() {
		return iti_level;
	}

	public void setIti_level(String iti_level) {
		this.iti_level = iti_level;
	}

	public String getIti_cos_type() {
		return iti_cos_type;
	}

	public void setIti_cos_type(String iti_cos_type) {
		this.iti_cos_type = unicode(iti_cos_type);
	}

	public String getIti_main_course() {
		return iti_main_course;
	}

	public void setIti_main_course(String iti_main_course) {
		this.iti_main_course = unicode(iti_main_course);
	}

	public String getIti_type() {
		return iti_type;
	}

	public void setIti_type(String iti_type) {
		this.iti_type = iti_type;
	}

	public String getIti_property() {
		return iti_property;
	}

	public void setIti_property(String iti_property) {
		this.iti_property = iti_property;
	}

	public String getIti_highest_educational() {
		return iti_highest_educational;
	}

	public void setIti_highest_educational(String iti_highest_educational) {
		this.iti_highest_educational = unicode(iti_highest_educational);
	}

	public String getIti_graduate_institutions() {
		return iti_graduate_institutions;
	}

	public void setIti_graduate_institutions(String iti_graduate_institutions) {
		this.iti_graduate_institutions = unicode(iti_graduate_institutions);
	}

	public String getIti_address() {
		return iti_address;
	}

	public void setIti_address(String iti_address) {
		this.iti_address = unicode(iti_address);
	}

	public String getIti_work_experience() {
		return iti_work_experience;
	}

	public void setIti_work_experience(String iti_work_experience) {
		this.iti_work_experience = unicode(iti_work_experience);
	}

	public String getIti_education_experience() {
		return iti_education_experience;
	}

	public void setIti_education_experience(String iti_education_experience) {
		this.iti_education_experience = unicode(iti_education_experience);
	}

	public String getIti_training_experience() {
		return iti_training_experience;
	}

	public void setIti_training_experience(String iti_training_experience) {
		this.iti_training_experience = unicode(iti_training_experience);
	}

	public String getIti_expertise_areas() {
		return iti_expertise_areas;
	}

	public void setIti_expertise_areas(String iti_expertise_areas) {
		this.iti_expertise_areas = unicode(iti_expertise_areas);
	}

	public String getIti_good_industry() {
		return iti_good_industry;
	}

	public void setIti_good_industry(String iti_good_industry) {
		this.iti_good_industry = unicode(iti_good_industry);
	}

	public String getIti_training_contacts() {
		return iti_training_contacts;
	}

	public void setIti_training_contacts(String iti_training_contacts) {
		this.iti_training_contacts = unicode(iti_training_contacts);
	}

	public String getIti_training_tel() {
		return iti_training_tel;
	}

	public void setIti_training_tel(String iti_training_tel) {
		this.iti_training_tel = unicode(iti_training_tel);
	}

	public String getIti_training_email() {
		return iti_training_email;
	}

	public void setIti_training_email(String iti_training_email) {
		this.iti_training_email = unicode(iti_training_email);
	}

	public String getIti_training_address() {
		return iti_training_address;
	}

	public void setIti_training_address(String iti_training_address) {
		this.iti_training_address = unicode(iti_training_address);
	}

	public String getIti_status() {
		return iti_status;
	}

	public void setIti_status(String iti_status) {
		this.iti_status = iti_status;
	}

	public String getIti_training_company() {
		return iti_training_company;
	}

	public void setIti_training_company(String iti_training_company) {
		this.iti_training_company = unicode(iti_training_company);
	}

	public String getIti_type_mark() {
		return iti_type_mark;
	}

	public void setIti_type_mark(String iti_type_mark) {
		this.iti_type_mark = iti_type_mark;
	}

	public String getIcs_title() {
		return ics_title;
	}

	public void setIcs_title(String ics_title) {
		this.ics_title = unicode(ics_title);
	}

	public String getIcs_fee() {
		return ics_fee;
	}

	public void setIcs_fee(String ics_fee) {
		this.ics_fee = ics_fee;
	}

	public String getIcs_hours() {
		return ics_hours;
	}

	public void setIcs_hours(String ics_hours) {
		this.ics_hours = ics_hours;
	}

	public String getIcs_target() {
		return ics_target;
	}

	public void setIcs_target(String ics_target) {
		this.ics_target = unicode(ics_target);
	}

	public String getIcs_content() {
		return ics_content;
	}

	public void setIcs_content(String ics_content) {
		this.ics_content = unicode(ics_content);
	}

	public String getIti_gw_str() {
		return iti_gw_str;
	}

	public void setIti_gw_str(String iti_gw_str) {
		this.iti_gw_str = unicode(iti_gw_str);
	}

	public float getIti_score_from() {
		return iti_score_from;
	}

	public void setIti_score_from(float iti_score_from) {
		this.iti_score_from = iti_score_from;
	}

	public float getIti_score_to() {
		return iti_score_to;
	}

	public void setIti_score_to(float iti_score_to) {
		this.iti_score_to = iti_score_to;
	}

	public String getJs_name() {
		return js_name;
	}

	public void setJs_name(String js_name) {
		this.js_name = js_name;
	}

	public int getMax_select() {
		return max_select;
	}

	public void setMax_select(int max_select) {
		this.max_select = max_select;
	}

	public String getIs_poup() {
		return is_poup;
	}

	public void setIs_poup(String is_poup) {
		this.is_poup = is_poup;
	}

	public String getIti_img_select() {
		return iti_img_select;
	}

	public void setIti_img_select(String iti_img_select) {
		this.iti_img_select = iti_img_select;
	}

	public String getDefault_image() {
		return default_image;
	}

	public void setDefault_image(String default_image) {
		this.default_image = default_image;
	}

	public int getIti_recommend() {
		return iti_recommend;
	}

	public void setIti_recommend(int iti_recommend) {
		this.iti_recommend = iti_recommend;
	}

}