package com.cw.wizbank.dao.exception;

/*
 *当sql语句的执行异常时向用户抛出改异常
 * @author:wrren
 * 
 */
public class SQLStatementProcessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SQLStatementProcessException(){}
	
	public SQLStatementProcessException(String message){
		super(message);
	}

}
