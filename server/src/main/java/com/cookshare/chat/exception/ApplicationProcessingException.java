package com.cookshare.chat.exception;

public class ApplicationProcessingException extends RuntimeException {
	public ApplicationProcessingException(String message, Throwable cause) {
		super(message, cause);
	}
}
