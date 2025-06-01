package com.honeystone.common.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "로그인 응답 DTO입니다.")
public class UserLoginResponse {

    @Schema(description = "JWT 액세스 토큰")
    private String accessToken;
    
    @Schema(description = "리프레시 토큰")
    private String refreshToken;

    @Schema(description = "사용자 정보")
    private GetUser user;
}
