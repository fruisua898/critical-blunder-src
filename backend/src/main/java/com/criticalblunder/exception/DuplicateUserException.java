package com.criticalblunder.exception;

public class DuplicateUserException extends RuntimeException {

	private static final long serialVersionUID = -1076997414190448402L;

	public DuplicateUserException(String message) {
		super(message);
	}
}