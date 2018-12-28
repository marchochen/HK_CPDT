package com.cw.wizbank.JsonMod.Course.bean;

import java.util.Vector;

/**
 * 导航条
 * @author kimyu
 */
public class NodeNavigationBar {
	private NodeBean cur_node; // 当前结点(可以是目录结点，也可以是课程结点)
	private Vector parent_nav; // 祖先目录集合
	
	public NodeBean getCur_node() {
		return cur_node;
	}

	public void setCur_node(NodeBean cur_node) {
		this.cur_node = cur_node;
	}

	public Vector getParent_nav() {
		return parent_nav;
	}

	public void setParent_nav(Vector parent_nav) {
		this.parent_nav = parent_nav;
	}

}
