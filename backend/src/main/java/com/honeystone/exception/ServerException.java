package com.honeystone.exception;

import lombok.Getter;

@Getter
public class ServerException extends RuntimeException {
    private final String message;
    private final String detail;
    private final Throwable cause;

    public ServerException(String message) {
        this(message, null, null);
    }

    public ServerException(String message, String detail) {
        this(message, detail, null);
    }

    public ServerException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public ServerException(String message, String detail, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.detail = detail;
        this.cause = cause;
    }
} 