package com.travelPlanner.exception;

public class EntityDoesnotExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityDoesnotExistException(String message) {
		super(message);
	}
}
