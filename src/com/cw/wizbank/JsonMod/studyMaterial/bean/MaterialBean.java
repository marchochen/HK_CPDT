package com.cw.wizbank.JsonMod.studyMaterial.bean;

import java.util.Vector;

public class MaterialBean {
	private long id;
	private String title;
	private String desc;
	private String type;
	private String subtype;
	private int difficulty;
	private String src_type;
	private String src_link;
	private String full_path;
	//for scorm
	private Vector structure_lst;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public String getSrc_type() {
		return src_type;
	}

	public void setSrc_type(String src_type) {
		this.src_type = src_type;
	}

	public String getSrc_link() {
		return src_link;
	}

	public void setSrc_link(String src_link) {
		this.src_link = src_link;
	}

	public String getFull_path() {
		return full_path;
	}

	public void setFull_path(String full_path) {
		this.full_path = full_path;
	}

	public Vector getStructure_lst() {
		return structure_lst;
	}

	public void setStructure_lst(Vector structure_lst) {
		this.structure_lst = structure_lst;
	}
	
}
