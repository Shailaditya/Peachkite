package com.peachkite.exception;

public class UserNotFoundException extends UserServiceException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1606261832570368132L;
	
	public UserNotFoundException(String message) {
		super(message);
	}

}
