package com.cookshare.chat.exception;

public class DatabaseServiceUnavailableException extends RuntimeException {
	public DatabaseServiceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

}
