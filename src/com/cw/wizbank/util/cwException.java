package com.cw.wizbank.util;

public class cwException extends Exception {

	public cwException() {
		super();
	}

	public cwException(String message) {
		super(message);
	}

	public cwException(String message, Throwable cause) {
		super(message, cause);
	}

	public cwException(Throwable cause) {
		super(cause);
	}
}
