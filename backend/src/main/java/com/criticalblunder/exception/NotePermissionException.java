package com.criticalblunder.exception;

public class NotePermissionException extends SecurityException {

	private static final long serialVersionUID = 7580276173309318788L;

	public NotePermissionException(String message) {
        super(message);
    }
}