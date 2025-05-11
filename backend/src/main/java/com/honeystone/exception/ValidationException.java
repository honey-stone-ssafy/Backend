package com.honeystone.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends BusinessException {
    private final List<String> errors;

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public ValidationException(List<String> errors) {
        this("입력값이 유효하지 않습니다.", errors);
    }
} 