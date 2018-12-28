package com.cw.wizbank.JsonMod.know;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;
import com.cw.wizbank.util.cwUtils;

/**
 * @author DeanChen
 * 
 */
public class KnowModuleParam extends BaseParam {

	// the param for tab
	private String activetab;

	// the param for question of know

	private int que_id;

	private int que_tcr_id;

	private int que_kca_id;

	private String que_title;

	private String que_content;

	private boolean que_popular_ind;

	private int que_reward_credits;

	private String que_status;

	private String que_update_timestamp;

	// the param for answer of know

	private int ans_id;

	private int ans_que_id;

	private String ans_content;
	private String ans_content_search; // field ans_content for search

	private int ans_create_ent_id;

	private String ans_refer_content;

	// the params for catalog
	private int kca_id;

	// the params for vote of answer
	private boolean vote_for_ind;

	// the param for active tab
	private String tab_que_type;

	// the param for my record
	private boolean home_ind;

	// the parameter for get catalog structure
	private boolean show_count_ind;

	// the parameter for know search
	private String search_id;

	private boolean simple_srh_ind;

	private String que_id_lst;

	private String kcr_create_timestamp_lst;

	private String kca_upd_timestamp;

	private int parent_kca_id;

	private String kca_title;

	private boolean kca_public_ind;

	private String srh_key;

	private String stylesheet;

	public String getQue_content() {
		return que_content;
	}

	public void setQue_content(String que_content) throws cwException {
		this.que_content = cwUtils.unicodeFrom(que_content, clientEnc, encoding);
	}

	public String getQue_status() {
		return que_status;
	}

	public void setQue_status(String que_status) {
		this.que_status = que_status;
	}

	public int getQue_id() {
		return que_id;
	}

	public void setQue_id(int que_id) {
		this.que_id = que_id;
	}

	public boolean isQue_popular_ind() {
		return que_popular_ind;
	}

	public void setQue_popular_ind(boolean que_popular_ind) {
		this.que_popular_ind = que_popular_ind;
	}

	public int getQue_reward_credits() {
		return que_reward_credits;
	}

	public void setQue_reward_credits(int que_reward_credits) {
		this.que_reward_credits = que_reward_credits;
	}

	public int getQue_tcr_id() {
		return que_tcr_id;
	}

	public void setQue_tcr_id(int que_tcr_id) {
		this.que_tcr_id = que_tcr_id;
	}

	public String getQue_title() {
		return que_title;
	}

	public void setQue_title(String que_title) throws cwException {
		this.que_title = cwUtils.unicodeFrom(que_title, clientEnc, encoding);
	}

	public int getQue_kca_id() {
		return que_kca_id;
	}

	public void setQue_kca_id(int que_kca_id) {
		this.que_kca_id = que_kca_id;
	}

	// the param for answer of know

	public int getAns_id() {
		return ans_id;
	}

	public void setAns_id(int ans_id) {
		this.ans_id = ans_id;
	}

	public int getAns_que_id() {
		return ans_que_id;
	}

	public void setAns_que_id(int ans_que_id) {
		this.ans_que_id = ans_que_id;
	}

	public String getAns_content() {
		return ans_content;
	}

	public void setAns_content(String ans_content) throws cwException {
		this.ans_content = cwUtils.unicodeFrom(ans_content, clientEnc, encoding);
		setAns_content_search(this.ans_content);
	}

	public String getAns_content_search() {
		return ans_content_search;
	}

	public void setAns_content_search(String ans_content_search) {
		if (ans_content_search != null) {
			this.ans_content_search = ans_content_search.toLowerCase();
		}
	}

	public int getKca_id() {
		return kca_id;
	}

	public void setKca_id(int kca_id) {
		this.kca_id = kca_id;
	}

	public boolean isVote_for_ind() {
		return vote_for_ind;
	}

	public void setVote_for_ind(boolean vote_for_ind) {
		this.vote_for_ind = vote_for_ind;
	}

	public String getAns_refer_content() {
		return ans_refer_content;
	}

	public void setAns_refer_content(String ans_refer_content) {
		this.ans_refer_content = ans_refer_content;
	}

	public String getTab_que_type() {
		return tab_que_type;
	}

	public void setTab_que_type(String tab_que_type) {
		this.tab_que_type = tab_que_type;
	}

	public String getActivetab() {
		return activetab;
	}

	public void setActivetab(String activetab) {
		this.activetab = activetab;
	}

	public boolean isHome_ind() {
		return home_ind;
	}

	public void setHome_ind(boolean home_ind) {
		this.home_ind = home_ind;
	}

	public String getQue_update_timestamp() {
		return que_update_timestamp;
	}

	public void setQue_update_timestamp(String que_update_timestamp) {
		this.que_update_timestamp = que_update_timestamp;
	}

	public boolean isShow_count_ind() {
		return show_count_ind;
	}

	public void setShow_count_ind(boolean show_count_ind) {
		this.show_count_ind = show_count_ind;
	}

	public int getAns_create_ent_id() {
		return ans_create_ent_id;
	}

	public void setAns_create_ent_id(int ans_create_ent_id) {
		this.ans_create_ent_id = ans_create_ent_id;
	}

	public String getSearch_id() {
		return search_id;
	}

	public void setSearch_id(String search_id) {
		this.search_id = search_id;
	}

	public boolean isSimple_srh_ind() {
		return simple_srh_ind;
	}

	public void setSimple_srh_ind(boolean simple_srh_ind) {
		this.simple_srh_ind = simple_srh_ind;
	}

	public void setQue_id_lst(String que_id_lst) {
		this.que_id_lst = que_id_lst;
	}

	public String getQue_id_lst() {
		return this.que_id_lst;
	}

	public void setKcr_create_timestamp_lst(String kcr_create_timestamp_lst) {
		this.kcr_create_timestamp_lst = kcr_create_timestamp_lst;
	}

	public String getKcr_create_timestamp_lst() {
		return this.kcr_create_timestamp_lst;
	}

	public void setKca_upd_timestamp(String kca_upd_timestamp) {
		this.kca_upd_timestamp = kca_upd_timestamp;
	}

	public String getKca_upd_timestamp() {
		return this.kca_upd_timestamp;
	}

	public void setParent_kca_id(int parent_kca_id) {
		this.parent_kca_id = parent_kca_id;
	}

	public int getParent_kca_id() {
		return this.parent_kca_id;
	}

	public void setKca_title(String kca_title) throws cwException {
		this.kca_title = cwUtils.unicodeFrom(kca_title, clientEnc, encoding);
	}

	public String getKca_title() {
		return this.kca_title;
	}

	public void setKca_public_ind(boolean kca_public_ind) {
		this.kca_public_ind = kca_public_ind;
	}

	public boolean getKca_public_ind() {
		return this.kca_public_ind;
	}

	public String getSrh_key() {
		return srh_key;
	}

	public void setSrh_key(String srh_key) throws cwException {
		this.srh_key = cwUtils.unicodeFrom(srh_key, clientEnc, encoding);
	}

	public String getStylesheet() {
		return stylesheet;
	}

	public void setStylesheet(String stylesheet) {
		this.stylesheet = stylesheet;
	}
}