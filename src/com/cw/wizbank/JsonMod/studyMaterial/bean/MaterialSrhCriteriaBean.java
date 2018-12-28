package com.cw.wizbank.JsonMod.studyMaterial.bean;

public class MaterialSrhCriteriaBean {
	private String srh_key;
	private String[] key_type_lst;
	private String[] res_subtype_lst;
	private long cat_id;
	private long[] difficulty_lst;

	public String getSrh_key() {
		return srh_key;
	}

	public void setSrh_key(String srh_key) {
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

	public long getCat_id() {
		return cat_id;
	}

	public void setCat_id(long cat_id) {
		this.cat_id = cat_id;
	}

	public long[] getDifficulty_lst() {
		return difficulty_lst;
	}

	public void setDifficulty_lst(long[] difficulty_lst) {
		this.difficulty_lst = difficulty_lst;
	}

}
