package com.miraldi.warehouse.services;

public class IllegalArgumentException extends java.lang.IllegalArgumentException {

    public IllegalArgumentException() {
    }

    public IllegalArgumentException(String s) {
        super(s);
    }

    public IllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgumentException(Throwable cause) {
        super(cause);
    }
}