package com.cwn.wizbank.exception;

public class EncryptException extends RuntimeException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6332789754201703375L;

	public EncryptException() {
		super();
	}

	public EncryptException(String message, Throwable cause) {
		super(message, cause);
	}

	public EncryptException(String message) {
		super(message);
	}

	public EncryptException(Throwable cause) {
		super(cause);
	}
}
