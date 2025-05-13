package com.honeystone.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String message;
    private final String detail;

    public BusinessException(String message) {
        this(message, null);
    }

    public BusinessException(String message, String detail) {
        super(message);
        this.message = message;
        this.detail = detail;
    }
} 