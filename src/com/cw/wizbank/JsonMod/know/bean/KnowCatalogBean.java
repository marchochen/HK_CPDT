package com.cw.wizbank.JsonMod.know.bean;

import java.util.Vector;

/**
 * @author DeanChen
 * 
 */
public class KnowCatalogBean {

	private int id;

	private String text;

	private String tnd_title;

	private int que_count;

	private Vector children;

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getQue_count() {
		return que_count;
	}

	public void setQue_count(int que_count) {
		this.que_count = que_count;
	}

	public Vector getChildren() {
		return this.children;
	}

	public void setChildren(Vector children) {
		this.children = children;
	}

	public String getTnd_title() {
		return tnd_title;
	}

	public void setTnd_title(String tnd_title) {
		this.tnd_title = tnd_title;
	}
}