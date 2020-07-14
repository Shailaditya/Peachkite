package com.peachkite.exception;

public class UserServiceException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1544423798712311639L;

	public UserServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserServiceException(Throwable cause) {
		super(cause);
	}

	public UserServiceException() {
		super();
	}

	public UserServiceException(String message) {
		super(message);
	}
}
