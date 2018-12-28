package com.cw.wizbank.dao.impl;

/*
 *该类用于调用存储过程时注册输出参数
 * @author:wrren
 * 
 */
public class OutParam {
	private String key;
	private int value;
	private int index;

	public OutParam() {

	}

	public OutParam(String key, int value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
