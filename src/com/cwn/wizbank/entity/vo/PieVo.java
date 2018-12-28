package com.cwn.wizbank.entity.vo;

public class PieVo {
	private String label;
	
	private String key;
	
	private Double value;
	
	private Double percentage;
	
	private String str_value;
	
	public PieVo(){}
	
	public PieVo(String key , Double value, Double percentage){
		this.key = key;
		this.value = value;
		this.percentage =  percentage;
	}
	
	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public Double getValue() {
		return value;
	}


	public void setValue(Double value) {
		this.value = value;
	}


	public Double getPercentage() {
		return percentage;
	}


	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getStr_value() {
		return str_value;
	}

	public void setStr_value(String str_value) {
		this.str_value = str_value;
	}

}
