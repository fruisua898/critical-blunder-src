package com.criticalblunder.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4976804143968979035L;

	public UserNotFoundException(String message) {
        super(message);
    }
}
