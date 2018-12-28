package com.cw.wizbank.JsonMod.Course.bean;

/**
 * 结点(可以是目录结点，也可以是课程结点)
 * @author kimyu
 */
public class NodeBean {
	private long tnd_id; 		// 当前目录ID
	private String tnd_title; 	// 当前目录名
	private int order = 0; 		// 序号(在当前目录中无用，用于祖目录的序号)

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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
