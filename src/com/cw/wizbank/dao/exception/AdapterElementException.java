package com.cw.wizbank.dao.exception;

/*
 *当把从数据库中查询出来的记录适配成xml字符串时，可能会发生该异常
 * @author:wrren
 * 
 */
public class AdapterElementException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AdapterElementException(){}
	
	public AdapterElementException(String message){
		super(message);
	}
}
