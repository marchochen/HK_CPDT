package com.cw.wizbank.dao;

public class Order {
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";
	private String orderFieldName = "";
	private String orderWay = ASC;
	
	public Order(){}
	public Order(String orderFieldName){
		this.orderFieldName = (orderFieldName == null)?"":orderFieldName;
	}
	public Order(String orderFieldName, String orderWay){
		this.orderFieldName = (orderFieldName == null)?"":orderFieldName;
		this.orderWay = (orderWay==null || (!orderWay.equalsIgnoreCase(ASC) && !orderWay.equalsIgnoreCase(DESC)))?ASC:orderWay;
	}
	public String getOrderFieldName() {
		return orderFieldName;
	}
	public void setOrderFieldName(String orderFieldName) {
		this.orderFieldName = (orderFieldName == null)?"":orderFieldName;
	}
	public String getOrderWay() {
		return orderWay;
	}
	public void setOrderWay(String orderWay) {
		this.orderWay = (orderWay==null || (!orderWay.equalsIgnoreCase(ASC) && !orderWay.equalsIgnoreCase(DESC)))?ASC:orderWay;
	}

}
