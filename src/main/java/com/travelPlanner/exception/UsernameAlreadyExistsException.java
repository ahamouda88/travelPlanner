package com.travelPlanner.exception;

public class UsernameAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsernameAlreadyExistsException(String username) {
		super("User with username: '" + username + "' already exists!");
	}

}
