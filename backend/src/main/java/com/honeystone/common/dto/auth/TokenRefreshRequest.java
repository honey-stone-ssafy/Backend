package com.honeystone.common.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "리프레시 토큰 요청 DTO")
public class TokenRefreshRequest {

    @NotBlank(message = "리프레시 토큰은 필수입니다.")
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1...")
    private String refreshToken;
}
