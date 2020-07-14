package com.peachkite.exception;

public class CryptoException extends Exception{
	
	static final long serialVersionUID = 2342304924934l;
	
	public CryptoException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public CryptoException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CryptoException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public CryptoException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CryptoException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
}
