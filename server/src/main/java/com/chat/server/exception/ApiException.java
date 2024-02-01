package com.chat.server.exception;

import com.chat.server.util.MessageUtil;

public class ApiException extends RuntimeException {
    private String message;

    public ApiException(String errorCode, Object... var2) {
        this.message = MessageUtil.getMessage(errorCode, var2);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
