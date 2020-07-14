package com.peachkite.exception;

public class FaqPublishLimitReachedException  extends Exception{

    /**
     *
     */
    private static final long serialVersionUID = 1544423798712344449L;

    public FaqPublishLimitReachedException(String message, Throwable cause,
                                boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FaqPublishLimitReachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FaqPublishLimitReachedException(Throwable cause) {
        super(cause);
    }

    public FaqPublishLimitReachedException() {
        super();
    }

    public FaqPublishLimitReachedException(String message) {
        super(message);
    }
}
