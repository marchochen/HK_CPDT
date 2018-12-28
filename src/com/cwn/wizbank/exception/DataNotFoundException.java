/**
 * 
 */
package com.cwn.wizbank.exception;

/**
 * 找不到对象
 * @author leon.li
 * 2014-7-30 下午4:05:00
 */
public class DataNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4081346134637628862L;

	public DataNotFoundException() {
		super();
	}

	public DataNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataNotFoundException(String message) {
		super(message);
	}

	public DataNotFoundException(Throwable cause) {
		super(cause);
	}

	
	
}
