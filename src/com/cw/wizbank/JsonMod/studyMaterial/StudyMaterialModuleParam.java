package com.cw.wizbank.JsonMod.studyMaterial;

import com.cw.wizbank.JsonMod.BaseParam;
import com.cw.wizbank.util.cwException;

public class StudyMaterialModuleParam extends BaseParam {
	private long tcr_id;
	private long cat_id;

	private String srh_key;
	private String[] key_type_lst;
	private String[] res_subtype_lst;
	private long[] res_difficulty_lst;

	private long node_id;
	private String node_type;

	private String search_id;

	public long getTcr_id() {
		return tcr_id;
	}

	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}

	public long getCat_id() {
		return cat_id;
	}

	public void setCat_id(long cat_id) {
		this.cat_id = cat_id;
	}

	public String getSrh_key() {
		return srh_key;
	}

	public void setSrh_key(String srh_key) throws cwException {
		this.srh_key = srh_key;
	}

	public String[] getKey_type_lst() {
		return key_type_lst;
	}

	public void setKey_type_lst(String[] key_type_lst) {
		this.key_type_lst = key_type_lst;
	}

	public String[] getRes_subtype_lst() {
		return res_subtype_lst;
	}

	public void setRes_subtype_lst(String[] res_subtype_lst) {
		this.res_subtype_lst = res_subtype_lst;
	}

	public long getNode_id() {
		return node_id;
	}

	public void setNode_id(long node_id) {
		this.node_id = node_id;
	}

	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}

	public String getSearch_id() {
		return search_id;
	}

	public void setSearch_id(String search_id) {
		this.search_id = search_id;
	}

	public long[] getRes_difficulty_lst() {
		return res_difficulty_lst;
	}

	public void setRes_difficulty_lst(long[] res_difficulty_lst) {
		this.res_difficulty_lst = res_difficulty_lst;
	}

}
