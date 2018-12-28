package com.cw.wizbank.JsonMod.studyGroup;

import java.sql.Timestamp;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

public class StudyGroupModuleParam extends BaseParam {
	private String sgp_search_key; //学习小组搜索关键字
	
	private long tcr_id;
	private long sgp_id;
	private String sgp_title;
	private String sgp_desc;
	private int sgp_public_type;
	private int sgp_send_email_ind;
	private String sgs_content;
	private String sgs_desc;
	private String sgs_type;
	private String sgs_title;
	
	private String url_success;
	private String url_failure;
	
	private String sgm_ent_id_itm;
	private String sgm_ent_id_usr;
	private String sgm_ent_id_usg;
	private long sgm_id;
	private long sgs_id;
	
	private long group_id;
	
	private String sgp_mgt_str; 
	
	private String tcr_id_lst;
	
	private boolean ta;
	
	private Timestamp sgp_upd_timestamp;
	private Timestamp sgs_upd_timestamp;
	private boolean is_keep_doc;

	private String sender_id;
	private String ent_ids;
	
	public boolean isIs_keep_doc() {
		return is_keep_doc;
	}

	public void setIs_keep_doc(boolean is_keep_doc) {
		this.is_keep_doc = is_keep_doc;
	}

	public Timestamp getSgs_upd_timestamp() {
		return sgs_upd_timestamp;
	}

	public void setSgs_upd_timestamp(Timestamp sgs_upd_timestamp) {
		this.sgs_upd_timestamp = sgs_upd_timestamp;
	}

	public Timestamp getSgp_upd_timestamp() {
		return sgp_upd_timestamp;
	}

	public void setSgp_upd_timestamp(Timestamp sgp_upd_timestamp) {
			this.sgp_upd_timestamp =sgp_upd_timestamp;
	}

	public boolean isTa() {
		return ta;
	}

	public void setTa(boolean ta) {
		this.ta = ta;
	}

	public String getSgp_mgt_str() {
		return sgp_mgt_str;
	}

	public void setSgp_mgt_str(String sgp_mgt_str) {
		this.sgp_mgt_str = sgp_mgt_str;
	}

	public long getGroup_id() {
		return group_id;
	}

	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}

	public long getSgm_id() {
		return sgm_id;
	}

	public void setSgm_id(long sgm_id) {
		this.sgm_id = sgm_id;
	}



	public String getSgp_desc() {
		return sgp_desc;
	}

	public void setSgp_desc(String sgp_desc) throws cwException {
		sgp_desc=cwUtils.unicodeFrom(sgp_desc, clientEnc, encoding);
		this.sgp_desc = sgp_desc;
	}

	public int getSgp_public_type() {
		return sgp_public_type;
	}

	public void setSgp_public_type(int sgp_public_type) {
		this.sgp_public_type = sgp_public_type;
	}

	public String getSgp_title() {
		return sgp_title;
	}

	public void setSgp_title(String sgp_title) throws cwException {
		sgp_title=cwUtils.unicodeFrom(sgp_title, clientEnc, encoding);
		this.sgp_title = sgp_title;
	}

	public long getSgp_id() {
		return sgp_id;
	}

	public void setSgp_id(long sgp_id) {
		this.sgp_id = sgp_id;
	}

	public String getSgp_search_key() {
		return sgp_search_key;
	}

	public void setSgp_search_key(String sgp_search_key) throws cwException {
		this.sgp_search_key = sgp_search_key;
	}

	public long getTcr_id() {
		return tcr_id;
	}

	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}

	public String getSgs_content() {
		return sgs_content;
	}

	public void setSgs_content(String sgs_content) throws cwException {
		sgs_content=cwUtils.unicodeFrom(sgs_content, clientEnc, encoding);
		this.sgs_content = sgs_content;
	}

	public String getSgs_desc() {
		return sgs_desc;
	}

	public void setSgs_desc(String sgs_desc) throws cwException {
		sgs_desc=cwUtils.unicodeFrom(sgs_desc, clientEnc, encoding, bMultiPart);
		this.sgs_desc = sgs_desc;
	}

	public String getSgs_type() {
		return sgs_type;
	}

	public void setSgs_type(String sgs_type) {
		this.sgs_type = sgs_type;
	}

	public String getUrl_failure() {
		return url_failure;
	}

	public void setUrl_failure(String url_failure) {
		this.url_failure = url_failure;
	}

	public String getUrl_success() {
		return url_success;
	}

	public void setUrl_success(String url_success) {
		this.url_success = url_success;
	}

	public long getSgs_id() {
		return sgs_id;
	}

	public void setSgs_id(long sgs_id) {
		this.sgs_id = sgs_id;
	}

	public String getSgs_title() {
		return sgs_title;
	}

	public void setSgs_title(String sgs_title) throws cwException {
		this.sgs_title = cwUtils.unicodeFrom(sgs_title, clientEnc, encoding, bMultiPart);
	}

	public String getSgm_ent_id_itm() {
		return sgm_ent_id_itm;
	}

	public void setSgm_ent_id_itm(String sgm_ent_id_itm) {
		this.sgm_ent_id_itm = sgm_ent_id_itm;
	}

	public String getSgm_ent_id_usg() {
		return sgm_ent_id_usg;
	}

	public void setSgm_ent_id_usg(String sgm_ent_id_usg) {
		this.sgm_ent_id_usg = sgm_ent_id_usg;
	}

	public String getSgm_ent_id_usr() {
		return sgm_ent_id_usr;
	}

	public void setSgm_ent_id_usr(String sgm_ent_id_usr) {
		this.sgm_ent_id_usr = sgm_ent_id_usr;
	}

	public String getTcr_id_lst() {
		return tcr_id_lst;
	}

	public void setTcr_id_lst(String tcr_id_lst) {
		this.tcr_id_lst = tcr_id_lst;
	}

	public int getSgp_send_email_ind() {
		return sgp_send_email_ind;
	}

	public void setSgp_send_email_ind(int sgp_send_email_ind) {
		this.sgp_send_email_ind = sgp_send_email_ind;
	}

	public String getSender_id() {
		return sender_id;
	}

	public void setSender_id(String sender_id) {
		this.sender_id = sender_id;
	}

	public String getEnt_ids() {
		return ent_ids;
	}

	public void setEnt_ids(String ent_ids) {
		this.ent_ids = ent_ids;
	}
}
