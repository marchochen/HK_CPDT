package com.cw.wizbank.JsonMod.courseware;

import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class CoursewareModuleParam extends BaseParam {

    String aicc_zip;

    long course_id;

    String course_struct_xml_1;

    String course_struct_xml_cnt;

    Timestamp course_timestamp;

    String mod_desc;

    String mod_src_type;

    String mod_subtype;

    String mod_title;

    String mod_type;

    String res_title;

    String mod_status;
    String domain;
    String res_privilege;
    String res_src_type;
    String res_instructor_name;
    String res_instructor_organization;
    String res_format;
    String res_status;
    float res_duration;
    long res_id;
    long obj_id;
    long cos_id;
    String res_lan;
    String res_type;
    String res_subtype;
    String res_cnt_subtype;
    Timestamp res_timestamp;
    Timestamp mod_eff_start_datetime;
    Timestamp mod_eff_end_datetime;
    int res_difficulty;
    String res_desc;
    String sco_ver;
    int mod_mobile_ind;
    String mod_test_style;
    public String getMod_test_style() {
		return mod_test_style;
	}

	public void setMod_test_style(String modTestStyle) {
		mod_test_style = modTestStyle;
	}

	public String getAicc_zip() {
        return aicc_zip;
    }

    public long getCourse_id() {
        return course_id;
    }

    public String getCourse_struct_xml_1() {
        return course_struct_xml_1;
    }

    public String getCourse_struct_xml_cnt() {
        return course_struct_xml_cnt;
    }

    public String getMod_desc() {
        return mod_desc;
    }

    public String getMod_src_type() {
        return mod_src_type;
    }

    public String getMod_subtype() {
        return mod_subtype;
    }

    public String getMod_title() {
        return mod_title;
    }

    public String getMod_type() {
        return mod_type;
    }

    public String getRes_title() {
        return res_title;
    }

    public void setAicc_zip(String aiccZip) {
        aicc_zip = aiccZip;
    }

    public void setCourse_id(long courseId) {
        course_id = courseId;
    }

    public void setCourse_struct_xml_1(String courseStructXml_1) throws cwException {
        course_struct_xml_1 = cwUtils.unicodeFrom(courseStructXml_1, clientEnc, encoding,isBMultiPart());
    }

    public void setCourse_struct_xml_cnt(String courseStructXmlCnt) {
        course_struct_xml_cnt = courseStructXmlCnt;
    }

    public void setMod_desc(String modDesc) throws cwException {
        mod_desc = cwUtils.unicodeFrom(modDesc, clientEnc, encoding);
    }

    public void setMod_src_type(String modSrcType) {
        mod_src_type = modSrcType;
    }

    public void setMod_subtype(String modSubtype) {
        mod_subtype = modSubtype;
    }

    public void setMod_title(String modTitle) throws cwException {
        mod_title = cwUtils.unicodeFrom(modTitle, clientEnc, encoding);
    }

    public void setMod_type(String modType) {
        mod_type = modType;
    }

    public void setRes_title(String resTitle) {
        res_title = resTitle;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Timestamp getCourse_timestamp() {
        return course_timestamp;
    }

    public void setCourse_timestamp(Timestamp courseTimestamp) {
        course_timestamp = courseTimestamp;
    }

    public Timestamp getMod_eff_start_datetime() {
        return mod_eff_start_datetime;
    }

    public void setMod_eff_start_datetime(Timestamp modEffStartDatetime) {
        mod_eff_start_datetime = modEffStartDatetime;
    }

    public Timestamp getMod_eff_end_datetime() {
        return mod_eff_end_datetime;
    }

    public void setMod_eff_end_datetime(Timestamp modEffEndDatetime) {
        mod_eff_end_datetime = modEffEndDatetime;
    }

    public String getMod_status() {
        return mod_status;
    }

    public void setMod_status(String modStatus) {
        mod_status = modStatus;
    }

    public long getRes_id() {
        return res_id;
    }

    public void setRes_id(long resId) {
        res_id = resId;
    }

    public String getRes_subtype() {
        return res_subtype;
    }

    public void setRes_subtype(String resSubtype) {
        res_subtype = resSubtype;
    }

    public Timestamp getRes_timestamp() {
        return res_timestamp;
    }

    public void setRes_timestamp(Timestamp resTimestamp) {
        res_timestamp = resTimestamp;
    }

    public long getObj_id() {
        return obj_id;
    }

    public void setObj_id(long objId) {
        obj_id = objId;
    }

    public int getRes_difficulty() {
        return res_difficulty;
    }

    public void setRes_difficulty(int resDifficulty) {
        res_difficulty = resDifficulty;
    }

    public String getRes_desc() {
        return res_desc;
    }

    public void setRes_desc(String resDesc) {
        res_desc = resDesc;
    }

    public String getRes_type() {
        return res_type;
    }

    public void setRes_type(String resType) {
        res_type = resType;
    }

    public String getRes_privilege() {
        return res_privilege;
    }

    public void setRes_privilege(String resPrivilege) {
        res_privilege = resPrivilege;
    }

    public String getRes_src_type() {
        return res_src_type;
    }

    public void setRes_src_type(String resSrcType) {
        res_src_type = resSrcType;
    }

    public String getRes_lan() {
        return res_lan;
    }

    public void setRes_lan(String resLan) {
        res_lan = resLan;
    }

    public String getRes_cnt_subtype() {
        return res_cnt_subtype;
    }

    public void setRes_cnt_subtype(String resCntSubtype) {
        res_cnt_subtype = resCntSubtype;
    }

    public String getRes_instructor_name() {
        return res_instructor_name;
    }

    public void setRes_instructor_name(String resInstructorName) {
        res_instructor_name = resInstructorName;
    }

    public String getRes_instructor_organization() {
        return res_instructor_organization;
    }

    public void setRes_instructor_organization(String resInstructorOrganization) {
        res_instructor_organization = resInstructorOrganization;
    }

    public String getRes_format() {
        return res_format;
    }

    public void setRes_format(String resFormat) {
        res_format = resFormat;
    }

    public String getRes_status() {
        return res_status;
    }

    public void setRes_status(String resStatus) {
        res_status = resStatus;
    }

    public float getRes_duration() {
        return res_duration;
    }

    public void setRes_duration(float resDuration) {
        res_duration = resDuration;
    }

    public long getCos_id() {
        return cos_id;
    }

    public void setCos_id(long cosId) {
        cos_id = cosId;
    }

	public String getSco_ver() {
		return sco_ver;
	}

	public void setSco_ver(String scoVer) {
		sco_ver = scoVer;
	}

	public int getMod_mobile_ind() {
		return mod_mobile_ind;
	}

	public void setMod_mobile_ind(int mod_mobile_ind) {
		this.mod_mobile_ind = mod_mobile_ind;
	}

}
