/**
 * 
 */
package com.cw.wizbank.supplier;

import java.sql.Timestamp;
import java.util.Date;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

/**
 * @author Leon.li 2013-5-24 5:10:49 
 *  emporary storage for the data form from request
 */
public class SupplierModuleParam extends BaseParam {


	private Integer splId;
	private String splName;
	private Timestamp splEstablishedDate;
	private String splRepresentative;
	private String splRegisteredCapital;
	private String splType;
	private String splAddress;
	private String splPostNum;
	private String splContact;
	private String splTel;
	private String splMobile;
	private String splEmail;
	private Integer splTotalStaff;
	private Integer splFullTimeInst;
	private Integer splPartTimeInst;
	private String splExpertise;
	private String splCourse;
	private String splStatus;
	private String splAttachment1;
	private String splAttachment2;
	private String splAttachment3;
	private String splAttachment4;
	private String splAttachment5;
	private String splAttachment6;
	private String splAttachment7;
	private String splAttachment8;
	private String splAttachment9;
	private String splAttachment10;
	private Timestamp splCreateDatetime;
	private String splCreateUsrId;
	private Timestamp splUpdateDatetime;
	private String splUpdateUsrId;
	
	private String sceItmName;
	private String sceString;
	private String smcString;
	
	private String isSearch;
	
	private String search_text;
	private long scm_id;
	private long scm_spl_id;
	private long scm_ent_id;
	private float scm_design_score;
	private float scm_teaching_score;
	private float scm_price_score;
	private float scm_score;
	private String scm_comment;
	private Timestamp scm_update_datetime;
	private Timestamp scm_create_datetime;
	private float scm_management_score;
	
	
	private long spl_id;
	private long spl_tcr_id;
	
	public long getSpl_id() {
		return spl_id;
	}
	
	public void setSpl_id(long spl_id) {
		this.spl_id = spl_id;
	}
	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	public String getSearch_text() {
		return search_text;
	}

	public Integer getSplId() {
		return splId;
	}

	public void setSplId(Integer splId) {
		this.splId = splId;
	}
	
	public String getSplName() {
		return splName;
	}

	public void setSplName(String splName) throws cwException {
		if("1".equals(isSearch)){
			splName=cwUtils.unicodeFrom(splName, clientEnc, encoding);
		}
		this.splName = splName;
	}
	public String getSplCourse() {
		return splCourse;
	}

	public void setSplCourse(String splCourse) throws cwException {
		if("1".equals(isSearch)){
			splCourse = cwUtils.unicodeFrom(splCourse, clientEnc, encoding);
		}
		this.splCourse = splCourse;
	}

	public String getSceString() {
		return sceString;
	}

	public void setSceString(String sceString) throws cwException {
		this.sceString = sceString;
	}
	
	public String getSmcString() {
		return smcString;
	}

	public void setSmcString(String smcString) throws cwException {
		//smcString=cwUtils.unicodeFrom(smcString, clientEnc, encoding);
		this.smcString = smcString;
	}
	
	
	public Timestamp getSplEstablishedDate() {
		return splEstablishedDate;
	}

	public void setSplEstablishedDate(Timestamp splEstablishedDate) {
		this.splEstablishedDate = splEstablishedDate;
	}

	public String getSplType() {
		return splType;
	}

	public void setSplType(String splType) throws cwException {
		//splType = cwUtils.unicodeFrom(splType, clientEnc, encoding);
		this.splType = splType;
	}

	public String getSplAddress() {
		return splAddress;
	}

	public void setSplAddress(String splAddress) throws cwException {
		//splAddress = cwUtils.unicodeFrom(splAddress, clientEnc, encoding);
		this.splAddress = splAddress;
	}

	public String getSplPostNum() {
		return splPostNum;
	}

	public void setSplPostNum(String splPostNum) {
		this.splPostNum = splPostNum;
	}

	public String getSplContact() {
		return splContact;
	}

	public void setSplContact(String splContact) throws cwException {
		//splContact = cwUtils.unicodeFrom(splContact, clientEnc, encoding);
		this.splContact = splContact;
	}

	public String getSplTel() {
		return splTel;
	}

	public void setSplTel(String splTel) {
		this.splTel = splTel;
	}

	public String getSplMobile() {
		return splMobile;
	}

	public void setSplMobile(String splMobile) {
		this.splMobile = splMobile;
	}

	public String getSplEmail() {
		return splEmail;
	}

	public void setSplEmail(String splEmail) throws cwException {
		//splEmail = cwUtils.unicodeFrom(splEmail, clientEnc, encoding);
		this.splEmail = splEmail;
	}

	public Integer getSplTotalStaff() {
		return splTotalStaff;
	}

	public void setSplTotalStaff(Integer splTotalStaff) {
		this.splTotalStaff = splTotalStaff;
	}

	public Integer getSplFullTimeInst() {
		return splFullTimeInst;
	}

	public void setSplFullTimeInst(Integer splFullTimeInst) {
		this.splFullTimeInst = splFullTimeInst;
	}

	public Integer getSplPartTimeInst() {
		return splPartTimeInst;
	}

	public void setSplPartTimeInst(Integer splPartTimeInst) {
		this.splPartTimeInst = splPartTimeInst;
	}

	public String getSplExpertise() {
		return splExpertise;
	}

	public void setSplExpertise(String splExpertise) throws cwException {
		//splExpertise = cwUtils.unicodeFrom(splExpertise, clientEnc, encoding);
		this.splExpertise = splExpertise;
	}

	public String getSplStatus() {
		return splStatus;
	}

	public void setSplStatus(String splStatus) {
		this.splStatus = splStatus;
	}

	public String getSplAttachment1() {
		return splAttachment1;
	}

	public void setSplAttachment1(String splAttachment1) {
		this.splAttachment1 = splAttachment1;
	}

	public String getSplAttachment2() {
		return splAttachment2;
	}

	public void setSplAttachment2(String splAttachment2) {
		this.splAttachment2 = splAttachment2;
	}

	public String getSplAttachment3() {
		return splAttachment3;
	}

	public void setSplAttachment3(String splAttachment3) {
		this.splAttachment3 = splAttachment3;
	}

	public String getSplAttachment4() {
		return splAttachment4;
	}

	public void setSplAttachment4(String splAttachment4) {
		this.splAttachment4 = splAttachment4;
	}

	public String getSplAttachment5() {
		return splAttachment5;
	}

	public void setSplAttachment5(String splAttachment5) {
		this.splAttachment5 = splAttachment5;
	}

	public String getSplAttachment6() {
		return splAttachment6;
	}

	public void setSplAttachment6(String splAttachment6) {
		this.splAttachment6 = splAttachment6;
	}

	public String getSplAttachment7() {
		return splAttachment7;
	}

	public void setSplAttachment7(String splAttachment7) {
		this.splAttachment7 = splAttachment7;
	}

	public String getSplAttachment8() {
		return splAttachment8;
	}

	public void setSplAttachment8(String splAttachment8) {
		this.splAttachment8 = splAttachment8;
	}

	public String getSplAttachment9() {
		return splAttachment9;
	}

	public void setSplAttachment9(String splAttachment9) {
		this.splAttachment9 = splAttachment9;
	}

	public String getSplAttachment10() {
		return splAttachment10;
	}

	public void setSplAttachment10(String splAttachment10) {
		this.splAttachment10 = splAttachment10;
	}

	public Timestamp getSplUpdateDatetime() {
		return splUpdateDatetime;
	}

	public void setSplUpdateDatetime(Timestamp splUpdateDatetime) {
		this.splUpdateDatetime = splUpdateDatetime;
	}

	public String getSceItmName() {
		return sceItmName;
	}

	public void setSceItmName(String sceItmName) throws cwException {
		//sceItmName = cwUtils.unicodeFrom(sceItmName, clientEnc, encoding);
		if("1".equals(isSearch)){
			sceItmName=cwUtils.unicodeFrom(sceItmName, clientEnc, encoding);
		}
		this.sceItmName = sceItmName;
	}

	public String getSplRegisteredCapital() {
		return splRegisteredCapital;
	}

	public void setSplRegisteredCapital(String splRegisteredCapital) {
		this.splRegisteredCapital = splRegisteredCapital;
	}

	public Timestamp getSplCreateDatetime() {
		return splCreateDatetime;
	}

	public void setSplCreateDatetime(Timestamp splCreateDatetime) {
		this.splCreateDatetime = splCreateDatetime;
	}

	public String getSplCreateUsrId() {
		return splCreateUsrId;
	}

	public void setSplCreateUsrId(String splCreateUsrId) {
		this.splCreateUsrId = splCreateUsrId;
	}

	public String getSplUpdateUsrId() {
		return splUpdateUsrId;
	}

	public void setSplUpdateUsrId(String splUpdateUsrId) {
		this.splUpdateUsrId = splUpdateUsrId;
	}

	public String getSplRepresentative() {
		return splRepresentative;
	}

	public void setSplRepresentative(String splRepresentative) {
		this.splRepresentative = splRepresentative;
	}
	public long getScm_id() {
		return scm_id;
	}

	public void setScm_id(long scm_id) {
		this.scm_id = scm_id;
	}

	public long getScm_spl_id() {
		return scm_spl_id;
	}

	public void setScm_spl_id(long scm_spl_id) {
		this.scm_spl_id = scm_spl_id;
	}

	public long getScm_ent_id() {
		return scm_ent_id;
	}

	public void setScm_ent_id(long scm_ent_id) {
		this.scm_ent_id = scm_ent_id;
	}

	public float getScm_design_score() {
		return scm_design_score;
	}

	public void setScm_design_score(float scm_design_score) {
		this.scm_design_score = scm_design_score;
	}

	public float getScm_teaching_score() {
		return scm_teaching_score;
	}

	public void setScm_teaching_score(float scm_teaching_score) {
		this.scm_teaching_score = scm_teaching_score;
	}

	public float getScm_price_score() {
		return scm_price_score;
	}

	public void setScm_price_score(float scm_price_score) {
		this.scm_price_score = scm_price_score;
	}

	public float getScm_score() {
		return scm_score;
	}

	public void setScm_score(float scm_score) {
		this.scm_score = scm_score;
	}

	public String getScm_comment() {
		return scm_comment;
	}

	public void setScm_comment(String scm_comment) throws cwException {
		scm_comment = cwUtils.unicodeFrom(scm_comment, clientEnc, encoding);
		this.scm_comment = scm_comment;
	}

	public Timestamp getScm_update_datetime() {
		return scm_update_datetime;
	}

	public void setScm_update_datetime(Timestamp scm_update_datetime) {
		this.scm_update_datetime = scm_update_datetime;
	}

	public Timestamp getScm_create_datetime() {
		return scm_create_datetime;
	}

	public void setScm_create_datetime(Timestamp scm_create_datetime) {
		this.scm_create_datetime = scm_create_datetime;
	}

	public float getScm_management_score() {
		return scm_management_score;
	}

	public void setScm_management_score(float scm_management_score) {
		this.scm_management_score = scm_management_score;
	}

	public String getIsSearch() {
		return isSearch;
	}

	public void setIsSearch(String isSearch) {
		this.isSearch = isSearch;
	}

	public long getSpl_tcr_id() {
		return spl_tcr_id;
	}

	public void setSpl_tcr_id(long spl_tcr_id) {
		this.spl_tcr_id = spl_tcr_id;
	}

}
