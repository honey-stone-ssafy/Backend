package com.honeystone.globalExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.honeystone.common.dto.error.ApiError;
import com.honeystone.exception.BusinessException;
import com.honeystone.exception.ServerException;
import com.honeystone.exception.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBusinessException(BusinessException ex) {
        return new ApiError(ex.getMessage(), ex.getDetail());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(ValidationException ex) {
        return new ApiError(ex.getMessage(), ex.getErrors());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBindException(BindException ex) {
        // 기본 메시지 수집
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> {
                // BusinessException이 cause에 있는지 확인
                Throwable cause = fieldError.unwrap(Throwable.class);
                if (cause instanceof BusinessException be) {
                    return be.getMessage(); // 또는 be.getDetail()
                }
                return fieldError.getDefaultMessage(); // fallback
            })
            .collect(Collectors.toList());

        return new ApiError("입력값이 유효하지 않습니다.", errors);
    }


    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleServerException(ServerException ex) {
        return new ApiError(ex.getMessage(), ex.getDetail());
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleDB(DataAccessException ex) {
        return new ApiError("데이터베이스 처리 중 오류가 발생했습니다.", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof BusinessException be) {
            return new ApiError(be.getMessage(), be.getDetail());
        }
        return new ApiError("서버 내부 오류가 발생했습니다.", ex.getMessage());
    }

    
    @ExceptionHandler({ MethodArgumentTypeMismatchException.class, ConversionFailedException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEnumConversionException(Exception ex) {
        return new ApiError(
            "유효하지 않은 값이 입력되었습니다.",
            "Location(enum)의 허용된 값만 입력 가능합니다. 예: HONGDAE, ILSAN, MAGOK 등"
        );
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIllegalArgument(IllegalArgumentException ex) {
        return new ApiError("입력값이 잘못되었습니다.", ex.getMessage());
    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(field -> {
                String msg = field.getDefaultMessage();
                if (msg != null && msg.contains(";")) {
                    msg = msg.substring(msg.indexOf(";") + 1).trim();
                }
                return msg;
            })
            .collect(Collectors.toList());

        return new ApiError("입력값이 유효하지 않습니다.", errors);
    }

}
