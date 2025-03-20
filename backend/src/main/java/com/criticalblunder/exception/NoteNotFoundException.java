package com.criticalblunder.exception;

public class NoteNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7214600769234643527L;

	public NoteNotFoundException(String message) {
        super(message);
    }
}