package com.cw.wizbank.JsonMod.commonBean;

import java.util.Vector;

public class JsonTreeRootBean {
	private long id; 
	private String text;
	private String href;
	private boolean leaf;
	private boolean expanded;
	private Vector children;
	private String type;
	public Vector getChildren() {
		return children;
	}
	public void setChildren(Vector children) {
		this.children = children;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    } 

}
