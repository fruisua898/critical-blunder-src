package com.criticalblunder.exception;

public class InvalidCredentialsException extends RuntimeException {

	private static final long serialVersionUID = -6089962861934072592L;

	public InvalidCredentialsException(String message) {
		super(message);
	}
}