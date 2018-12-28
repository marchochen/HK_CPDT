package com.cw.wizbank.JsonMod.studyMaterial.bean;

import java.util.Vector;

public class MaterialCatalogBean {
	private long id;
	private long tcr_id;
	private String tcr_title;
	private String desc;
	private int count;

	private String text;
	private Vector children;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTcr_id() {
		return tcr_id;
	}

	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}

	public String getTcr_title() {
		return tcr_title;
	}

	public void setTcr_title(String tcr_title) {
		this.tcr_title = tcr_title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Vector getChildren() {
		return children;
	}

	public void setChildren(Vector children) {
		this.children = children;
	}

}
