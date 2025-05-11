package com.honeystone.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "API 오류 응답 DTO")
public class ApiError {
    @Schema(description = "오류 메시지", example = "입력값이 유효하지 않습니다.")
    private final String message;

    @Schema(description = "상세 오류 정보 (문자열 또는 오류 목록)", example = "[\"제목은 필수입니다\", \"내용은 100자 이내여야 합니다\"]")
    private final Object detail;

    public ApiError(String message, String detail) {
        this.message = message;
        this.detail = detail;
    }

    public ApiError(String message, List<String> errors) {
        this.message = message;
        this.detail = errors;
    }
}
