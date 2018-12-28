
package com.cwn.wizbank.exception;

/**
 * 多点登录异常
 * 
 * @author andrew.xiao
 * 2016-10-28 10:30
 */
public class MultipleLoginException extends Exception {

	private static final long serialVersionUID = 3626460594816135741L;

	public MultipleLoginException() {
		super();
	}

	public MultipleLoginException(String message, Throwable cause) {
		super(message, cause);
	}

	public MultipleLoginException(String message) {
		super(message);
	}

	public MultipleLoginException(Throwable cause) {
		super(cause);
	}
	
}
