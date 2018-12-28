/**
 * 
 */
package com.cwn.wizbank.exception;

/**
 * 
 * @author bill.lai
 * 2016-05-06 下午4:05:00
 */
public class ErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4081346134637628862L;

	public ErrorException() {
		super();
	}

	public ErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ErrorException(String message) {
		super(message);
	}

	public ErrorException(Throwable cause) {
		super(cause);
	}

	
	
}
