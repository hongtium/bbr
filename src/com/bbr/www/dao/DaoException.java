package com.bbr.www.dao;

public class DaoException extends RuntimeException {
    protected Throwable throwable;

    /**
     * Method 'DaoException'
     *
     * @param message
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     * Method 'DaoException'
     *
     * @param message
     * @param throwable
     */
    public DaoException(String message, Throwable throwable) {
        super(message);
        this.throwable = throwable;
    }

    /**
     * Method 'getCause'
     *
     * @return Throwable
     */
    public Throwable getCause() {
        return throwable;
    }

}
