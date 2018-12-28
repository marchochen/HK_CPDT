package com.cwn.wizbank.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;


public class FormatUtil {
	private static FormatUtil instance;	
	public static final NumberFormat MONEY_FORMATE = new DecimalFormat("##0.00");
	
	public static final NumberFormat MONEY_FORMATE_B = new DecimalFormat("##0.0000");
	
	public static final NumberFormat MONEY_FORMATE_LONG = new DecimalFormat("##0");
	
	public static FormatUtil getInstance(){
		if(instance==null){
			instance = new FormatUtil();
		}
		return instance;
	}	
	
	public String formatDouble(Double value){
		return this.formatDouble(value, "");
	}
	
	public String formatDouble(Double value,String defaultValue){
		if(value==null){
			return defaultValue;
		}else{
			return MONEY_FORMATE.format(value);
		}
	}
	
	public String formatDouble(Double value,String defaultValue,NumberFormat format){
		if(value==null){
			return defaultValue;
		}else{
			return format.format(value);
		}
	}
	
	public Double scaleDouble (Double value , int scale){
		BigDecimal bValue = new  BigDecimal(value);  
		return  bValue.setScale(scale,   BigDecimal.ROUND_HALF_UP).doubleValue(); 
	}
	
	public float scaleFloat(float value , int scale,int roundingMode){
		BigDecimal bValue = new  BigDecimal(value);  
		return  bValue.setScale(scale,   roundingMode).floatValue(); 
	}
	
	public static void main(String[] args) {
		FormatUtil fu = new FormatUtil();
		System.out.println(fu.scaleFloat(47.2727f, 2, BigDecimal.ROUND_HALF_UP));
	}
		
}
