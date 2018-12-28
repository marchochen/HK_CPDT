package com.cw.wizbank.JsonMod.Course.bean;

import java.util.Vector;

/**
 * 目录Bean
 * @author kimyu
 */
public class CatalogBean {
	private long tnd_id; 					// 目录ID(对应aeTreeNode表的tnd_id)
	private long id;						// 目录ID(对应aeTreeNode表的tnd_id)，用于前台目录树展现
	
	private String tnd_title; 				// 目录名
	private String text;					// 目录名，用于前台目录树展现
	
	private boolean isAlreadyShowed = false;// 是否已经在目录名后显示了数量
	private boolean expanded;				// 该目录是否可以展开
	private boolean leafage; 				// 该目录是否是叶子结点
	private Vector children;				// 用于存放该目录的叶子结点
	private int count;						// 该目录下(包括子目录)的所有用户可见课程的数量
	private long tcr_id;					// 目录所属的培训中心ID
	private String tcr_title;				// 培训中心名

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isLeafage() {
		return leafage;
	}

	public void setLeafage(boolean leafage) {
		this.leafage = leafage;
	}

	public Vector getChildren() {
		return children;
	}

	public void setChildren(Vector children) {
		this.children = children;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getTcr_id() {
		return tcr_id;
	}

	public void setTcr_id(long tcr_id) {
		this.tcr_id = tcr_id;
	}

	public boolean isAlreadyShowed() {
		return isAlreadyShowed;
	}

	public void setAlreadyShowed(boolean isAlreadyShowed) {
		this.isAlreadyShowed = isAlreadyShowed;
	}

	public long getTnd_id() {
		return tnd_id;
	}

	public void setTnd_id(long tnd_id) {
		this.tnd_id = tnd_id;
	}

	public String getTnd_title() {
		return tnd_title;
	}

	public void setTnd_title(String tnd_title) {
		this.tnd_title = tnd_title;
	}

	public String getTcr_title() {
		return tcr_title;
	}

	public void setTcr_title(String tcr_title) {
		this.tcr_title = tcr_title;
	}

}
