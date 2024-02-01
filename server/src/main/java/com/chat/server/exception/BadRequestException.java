package com.chat.server.exception;

public class BadRequestException extends ApiException {
    public BadRequestException(String errorCode, Object... var2) {
        super(errorCode, var2);
    }
}
