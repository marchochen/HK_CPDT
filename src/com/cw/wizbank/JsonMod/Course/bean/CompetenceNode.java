package com.cw.wizbank.JsonMod.Course.bean;

import java.util.Vector;

/**
 * 能力结点
 * @author kimyu
 */
public class CompetenceNode {
	long id;		// 用于前台树结构展现，该值唯一
	String ske_id;	// 能力或岗位在cmSkillEntity表中ID
	String text;
//	boolean leaf;
	boolean choice = true;	// 是否为可选：true:可选(默认)  false:不可选
	Vector children;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSke_id() {
		return ske_id;
	}

	public void setSke_id(String ske_id) {
		this.ske_id = ske_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isChoice() {
		return choice;
	}

	public void setChoice(boolean choice) {
		this.choice = choice;
	}

	public Vector getChildren() {
		return children;
	}

	public void setChildren(Vector children) {
		this.children = children;
	}
	
}
