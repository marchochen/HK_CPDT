/**
 * 
 */
package com.cwn.wizbank.exception;

/**
 * 
 * @author leon.li
 * 2014-7-30 下午4:05:00
 */
public class MessageException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4081346134637628862L;

	public MessageException() {
		super();
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(Throwable cause) {
		super(cause);
	}

	
	
}
