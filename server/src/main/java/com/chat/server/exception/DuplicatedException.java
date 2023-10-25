package com.chat.server.exception;

public class DuplicatedException extends ApiException {
    public DuplicatedException(String errorCode, Object... var2) {
        super(errorCode, var2);
    }
}
