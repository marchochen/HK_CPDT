package com.cw.wizbank.qdb;

public class qdbException extends Exception {
	public qdbException() {
		super();
	}

	public qdbException(String message) {
		super(message);
	}

	public qdbException(String message, Throwable cause) {
		super(message, cause);
	}

	public qdbException(Throwable cause) {
		super(cause);
	}
}
