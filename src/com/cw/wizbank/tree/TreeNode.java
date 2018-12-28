package com.cw.wizbank.tree;

import java.util.Vector;

public class TreeNode {
	private long id;
	private long pId;
	private String name;
	private String type;
	private boolean open;
	private boolean isParent;
	private Vector<TreeNode> children;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getpId() {
		return pId;
	}

	public void setpId(long pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isIsParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public Vector<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(Vector<TreeNode> children) {
		this.children = children;
	}

	public boolean isParent() {
		return isParent;
	}

}
