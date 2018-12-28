package com.cwn.wizbank.exception;

public class SessionTimeOutException extends Exception {

	private static final long serialVersionUID = 1L;

	public SessionTimeOutException() {
		super();
	}

	public SessionTimeOutException(String message) {
		super(message);
	}

	public SessionTimeOutException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionTimeOutException(Throwable cause) {
		super(cause);
	}
}